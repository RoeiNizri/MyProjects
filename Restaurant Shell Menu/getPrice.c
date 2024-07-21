#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#define BUFF_SIZE 30

// This function opens a file.
int openFile(char* file);

int main(int argc, char* argv[])
{
    char rest[BUFF_SIZE], dish[BUFF_SIZE];
    char ch, buff[BUFF_SIZE], buffPrice[BUFF_SIZE];
    int i, m1, m2, flag = 1, fdFrom, rbytes = 1;
    strcpy(rest, argv[1]);
    strcat(rest, ".txt");
    for (i = 2; i < argc; i++)
        sprintf(dish, "%s ", argv[i]);
    fdFrom = openFile(rest);
    while (rbytes > 0)
    {
        strcpy(buff, "");
        memset(buffPrice, 0, sizeof(buffPrice));
        memset(buff, 0, sizeof(buff));
        m1 = 0;
        m2 = 0;
        while (m1 < BUFF_SIZE && rbytes>0)
        {
            if ((rbytes = read(fdFrom, &ch, 1)) == -1) {
                perror("Error: Can't read the file");
                exit(1);
            }
            buff[m1++] = ch;
            if (ch >= '0' && ch <= '9')
                buffPrice[m2++] = ch;
            if (ch == '\n' || ch == '\0')
                break;
        }if (strstr(buff, dish) != NULL)
        {
            printf("%d NIS\n", atoi(buffPrice));
            flag = 0;
            break;
        }
    }
    if (flag)
        printf("Dish not in the menu!\n");
    close(fdFrom);
    return 0;
}

int openFile(char* file) {
    int fd_from;
    if ((fd_from = open(file, O_RDONLY)) == -1) {
        perror("Error: Can't open the file, check the name.");
        exit(1);
    }
    return fd_from;
}