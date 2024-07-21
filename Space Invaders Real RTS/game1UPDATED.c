#include <dos.h>
#include <stdio.h>

#define ARROW_NUMBER 30
#define TARGET_NUMBER 4
#define ARRSIZE 1000

typedef struct position
{
    int x;
    int y;

}  POSITION;

char display_draft[25][80];
POSITION target_pos[TARGET_NUMBER];
POSITION arrow_pos[ARROW_NUMBER];

void  interrupt(*old_int9)(void);

char entered_ascii_codes[ARRSIZE];
int tail = -1;
char display[4002];
int  value;
int  far* b800h; // Pointer to video buffer

char ch_arr[ARRSIZE];
int front = -1;
int rear = -1;

int point_in_cycle;
int gcycle_length;
int gno_of_pids;

int initial_run = 1;
int gun_position;
int no_of_arrows;
int target_disp = 80 / TARGET_NUMBER;
char ch;

int no_of_targets;

char StringScore[] = "Score = xx";
int score = 0;          // number of kills
int isWin = 1;          // This flag will be 1, untill we lose
int time = 2;


// Function to handle program termination
void my_halt()
{
    setvect(9, old_int9);
    asm{ CLI }
    exit();
}


// Interrupt handler for keyboard input
void  interrupt new_int9(void)
{
    char result = 0;
    int scan = 0;
    int ascii = 0;

    (*old_int9)();


    asm{
      MOV AH,1
      INT 16h
      JZ Skip1
      MOV AH,0
      INT 16h
      MOV BYTE PTR scan,AH
      MOV BYTE PTR ascii,AL
    } //asm

    ascii = 0;
    if (scan == 75)
        ascii = 'a';
    else
        if (scan == 72)
            ascii = 'w';
        else
            if (scan == 77)
                ascii = 'd';
// CHANGE
 //   if ((scan == 46) && (ascii == 3)) // Ctrl-C?
// END OF CHANGE
        if (scan == 1) // Esc?
            my_halt(); // terminate program

    if ((ascii != 0) && (tail < ARRSIZE))
    {
        entered_ascii_codes[++tail] = ascii;
    } // if

Skip1:

}


/*
    This function updates the StringScore with the current score.
    Example: score = xx
*/
void UpdateStringScore()
{
    StringScore[8] = ((char)(score / 10) + '0');
    StringScore[9] = ((char)(score % 10) + '0');
}

// Function to display characters on the screen
void displayer(void)
{
    int i, j;
    ((unsigned long int)b800h) = 0xB800 * 65536;
    for (i = 0; i < 25; i++)
    {
        for (j = 0; j < 80; j++)
        {
            if (display_draft[i][j] == NULL)
                display_draft[i][j] = ' ';  // Replace NULL with space
        }
    }
    for (i = 0; i < 25; i++)
    {
        for (j = 0; j < 80; j++)
        {
            int col = 256 * 10;
            if (display_draft[i][j] == '/' || display_draft[i][j] == '^' || display_draft[i][j] == '\\' || display_draft[i][j] == '|')
                col = 256 * 8;
            else if (display_draft[i][j] == '*')
                col = 256 * 4;
            else if (display_draft[i][j] == ' ')
                col = 256 * 14 + 16 * 0;
            value = display_draft[i][j] + col;
            b800h[i * 80 + j] = value;
        }
    }
}

// Function to receive keyboard input
void receiver()
{
    char temp;
// CHANGE
    int i;
   
    i = 0;
//    while (tail > -1)
    while (i <= tail)
    {
//        temp = entered_ascii_codes[tail];
        temp = entered_ascii_codes[i];
        rear++;
//        tail--;
       i++;
// CHANGE
        if (rear < ARRSIZE)
            ch_arr[rear] = temp;
        if (front == -1)
            front = 0;
    } // while
   tail = 0;
// END OF CHANGE
}

// Function to update game state
void updater()
{
    int i, j, k, isTargetHit;

    if (initial_run == 1)
    {
        initial_run = 0;
        no_of_arrows = 0;
        no_of_targets = TARGET_NUMBER;
        gun_position = 39;
        target_pos[0].x = 3;
        target_pos[0].y = 0;

        for (i = 1; i < TARGET_NUMBER; i++)
        {
            target_pos[i].x = i * target_disp;
            target_pos[i].y = 0;
        }
        for (i = 0; i < ARROW_NUMBER; i++)
            arrow_pos[i].x = arrow_pos[i].y = -1;
    }

    while (front != -1)
    {
        ch = ch_arr[front];
        if (front != rear)
            front++;
        else
            front = rear = -1;

        if ((ch == 'a') || (ch == 'A'))
            if (gun_position >= 2)
                gun_position--;
            else;
        else if ((ch == 'd') || (ch == 'D'))
            if (gun_position <= 78)
                gun_position++;
            else;
        else if ((ch == 'w') || (ch == 'W'))
            if (no_of_arrows < ARROW_NUMBER)
            {
                arrow_pos[no_of_arrows].x = gun_position;
                arrow_pos[no_of_arrows].y = 23;
                no_of_arrows++;
            }
    }

    ch = 0;
    for (i = 0; i < 25; i++)
        for (j = 0; j < 80; j++)
            display_draft[i][j] = ' ';  // Blank out display

    // Draw gun
    display_draft[22][gun_position] = '^';
    display_draft[23][gun_position - 1] = '/';
    display_draft[23][gun_position] = '|';
    display_draft[23][gun_position + 1] = '\\';
    display_draft[24][gun_position] = '|';

    for (i = 0; i < ARROW_NUMBER; i++)
    {
        if (arrow_pos[i].x != -1)
        {
            if (arrow_pos[i].y > 0)
                arrow_pos[i].y--;
            else
            {
                arrow_pos[i].x = -1;    // Arrow disappears
                no_of_arrows--;
            }

            // Check if arrow hits target
            isTargetHit = 0;
            for (k = 0; k < TARGET_NUMBER; k++)
            {
                if ((arrow_pos[i].x == target_pos[k].x) && ((arrow_pos[i].y == target_pos[k].y) || (arrow_pos[i].y + 1) == target_pos[k].y))
                {
                    score++;                // Update score
                    target_pos[k].x = -1;   // Target killed
                    arrow_pos[i].x = -1;    // Arrow disappears
                    isTargetHit = 1;
                    no_of_targets--;
                    no_of_arrows--;
                    break;
                }
            }
            if (isTargetHit == 0)       // If arrow didn't kill target, put arrow on screen
            {
                display_draft[arrow_pos[i].y][arrow_pos[i].x] = '^';
                display_draft[arrow_pos[i].y + 1][arrow_pos[i].x] = '|';
            }
        }
    }
    if (no_of_targets == 0)
    {
        no_of_targets = TARGET_NUMBER;

        for (i = 1; i < TARGET_NUMBER; i++)
        {
            target_pos[i].x = i * target_disp;
            target_pos[i].y = 2;
        }
        time = time / 2;
    }

    for (i = 0; i < TARGET_NUMBER; i++)
        if (target_pos[i].x != -1)
        {
            if (target_pos[i].y < 22)
                target_pos[i].y++;
            else
            {
                isWin = 0;
                break;
            }
            display_draft[target_pos[i].y][target_pos[i].x] = '*';
        }

    // Print score
    UpdateStringScore();
    for (i = 0; i < 10; i++)
        display_draft[1][34 + i] = StringScore[i];

    display[4002] = '\0';

}

int main()
{
    int uppid, dispid, recvpid;
    asm{
                  MOV AH,3
                  INT 10h
    }
    old_int9 = getvect(9);
    setvect(9, new_int9);

    while (isWin)
    {
        receiver();
        updater();
        displayer();
        sleep(time);
    }
    clrscr();
    printf("You Lose :( , Your score is: %d", score);
// CHANGE
     my_halt(); 
// END OF CHANGE
    return 0;
}
