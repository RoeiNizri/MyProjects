/*The program gets list of students from a file and gets operation by the user to output file.*/
#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
typedef struct Student //Struct for student
{
	char* name;
	long id;
	int matamGrade;
	char program; //Y-new, N-old
	char labs[6];
	char finalG;
}student;

typedef struct University //Struct for university
{
	student* arr;
	int amount;
}uni;
void Error_Msg(char* str);//The function prints error messages and exit.
void StudentsToUni(FILE* in, uni* u);//Option 1:The function gets a pointer to a file and a university and fills the students array  from the data's file.
void finalGradePrint(FILE* out, uni* u);//Option2:The function gets a  pointer to a file and a university and inserts to the final 
										 //grade of each student - 1 if the student served 3 or more labs and 0 if less.
void beforAndAfter(FILE* out, uni* u);//Option3:The function gets a pointer to a file and a university calculates and prints the final grade of labs(0 or 1) and the grade. 
void statisticsPrint( FILE* OUT, uni* u);//Option4:The function gets a pointer to a file and a university and prints average,standard deviation,range,amount of student in each program. 
void blackListPrint(FILE* out, uni* u); //Option5:The function gets a pointer to a file and a university and prints the students that didn't served more then 3 labs - "black list".
int checkLabs(student stu);//The function returns 0 if the student didnt serve 3 or more labs and 1 otherwise - auxiliary function.
void endP(FILE* out);//The function prints the end of program in the file and to user.
void freeUniversity(uni* u);// The function frees memory.
int main()
{
	uni u;
	FILE* in, * out;
	char wantedProg;
	int flag = 1, selection;//selection - the number of opparation the user chose
	in = fopen("input.txt", "rt");
	if (in == NULL)
		Error_Msg("The input file is wrong");
	if (in == EOF)
		Error_Msg("The file is empty");
	printf("Enter a number of opperation between 1 to 6. First of all press 1 to get students to the university please:\n");
	scanf("%d", &selection);
	while (selection != 1)//Operation 1 must be done first
	{
		printf("Operation 1 must be done first.\n");
		printf("Enter a number of opperation between 1 to 6. First of all press 1 to get students to the university please: \n");
		scanf("%d", &selection);
	}
	printf("Thanks. We can begin.\n\n");
	StudentsToUni(in, &u);
	fclose(in);
	out = fopen("output.txt", "wt");
	if (out == NULL)
		Error_Msg("Input file error!");
	while (selection != 6)
	{
		printf("Enter a number of opperation please:\n");
		scanf("%d", &selection);
		if (selection == 1)
			printf("First opperation can only be performed once! Enter another number between 2 to 6 please:\n");
		else
		{
			if (selection == 2)
			{
				fprintf(out, "Option 2:\n\n");
				finalGradePrint(out, &u);
			}
			else if (selection == 3)
			{
				fprintf(out, "\nOption 3:\n");
				beforAndAfter(out, &u);
			}
			else if (selection == 4)
			{
				fprintf(out, "\nOption 4:\n");
				statisticsPrint(out, &u);
			}
			else if (selection == 5)
			{
				fprintf(out, "\nOption 5:\n");
				blackListPrint(out, &u);
			}
			else if (selection == 6)
			{
				fprintf(out, "\nOption 6:\n");
				endP(out);
			}
			else 
				printf("Enter a number of opperation between 1-6 please:\n");
		}
	}
	fclose(out);
	freeUniversity(&u);
	return 0;
}
void freeUniversity(uni *u)
{
	int i;
	for (i = 0; i < u->amount; i++)
		free(u->arr[i].name);
	free(u->arr);
}
void Error_Msg(char* str)
{
	printf("\n%s", str);
	exit(1);
}
void StudentsToUni(FILE* in, uni* u)
{
	char str[100],buffer;
	int i = 0;
	u->arr = (student*)malloc(sizeof(student));
	if (u->arr == NULL)
		Error_Msg("No memory");
	while(fscanf(in, "%s%ld%d%s%c%c", str, &u->arr[i].id, &u->arr[i].matamGrade, u->arr[i].labs, &buffer, &u->arr[i].program)!=EOF)
	{
		u->arr[i].name = (char*)malloc((strlen(str) + 1) * sizeof(char));
		if (u->arr[i].name == NULL)
			Error_Msg("No memory");
		strcpy(u->arr[i].name, str);
		i++;
		u->arr = (student*)realloc(u->arr, (i + 1) * sizeof(student));
	}
	u->amount = i;
}
void finalGradePrint(FILE* out, uni* u)
{
	int i, cnt = 0, j;
	for (i = 0; i < u->amount; i++)
	{	
		fprintf(out,"Student %d: ", i+1);
		if (checkLabs(u->arr[i])==1)
			u->arr[i].finalG = '1';
		else
			u->arr[i].finalG = '0';
		fprintf(out, "%s,%ld,%d,%c,%c\n", u->arr[i].name, u->arr[i].id, u->arr[i].matamGrade, u->arr[i].finalG, u->arr[i].program);
	}
}
void beforAndAfter(FILE* out, uni* u)
{
	int i, cnt = 0, finalGrade=0;
	fprintf(out, "\nBefore:\n");
	finalGradePrint(out,u);
	fprintf(out, "\nAfter:\n");
	for (i = 0; i < u->amount; i++)
	{
		if ((checkLabs(u->arr[i])==1)&& (u->arr[i].matamGrade >= 55))
		{
			if (u->arr[i].program == 'N')//if the student in old program
			{
				finalGrade = 0.1 * 100;
				finalGrade += 0.9 * u->arr[i].matamGrade;
			}
			else
			{
				finalGrade = 0.2 * 100;
				finalGrade += 0.8 * u->arr[i].matamGrade;
			}
		}
		else//if the labs grade is 0 or the matam grade is under 55, the grade stays the same
			finalGrade = u->arr[i].matamGrade;
		fprintf(out, "Student %d: ", i + 1);
		fprintf(out, "%s,%ld,%d,%c, Final:%d\n", u->arr[i].name, u->arr[i].id, u->arr[i].matamGrade, u->arr[i].finalG,finalGrade);
	}
}
void statisticsPrint(FILE* out, uni*u)
{
	int Choice, i, count = 0, min = 100, max = 0;
	float avg = 0, deviation = 0, sum = 0;
	char prog;
	printf("Enter N for old program, or Y for new program please: \n");
	rewind(stdin);
	scanf("%c", &prog);
	while (prog != 'N' && prog != 'Y')
	{
		printf("Enter ONLY N for old program, or Y for new program please: \n");
		rewind(stdin);
		scanf("%c", &prog);
	}
	for (i = 0; i < u->amount; i++)
	{
		if (u->arr[i].program == prog)
		{
			sum += u->arr[i].matamGrade;
			count++;
			if (u->arr[i].matamGrade < min)
				min = u->arr[i].matamGrade;
			if (u->arr[i].matamGrade > max)
				max = u->arr[i].matamGrade;
		}
	}
	avg= sum/count;
	for (i = 0; i < u->amount; i++)
	{
		if (u->arr[i].program == prog)
			deviation += (float)pow(u->arr[i].matamGrade - avg, 2);
	}
	deviation /= (float)count;
	deviation = sqrt(deviation);
	fprintf(out, "\nAverage = %.2f\nStandard Deviation = %.2f\nTotal Students amount = %d\nStudents in the program= %d\nRange = %d - %d\n", avg, deviation, u->amount,count, max, min);
}
void blackListPrint(FILE *out,uni*u)
{
	int i;
	fprintf(out, "\nBlack List:");
	for (i = 0; i < u->amount; i++)
	{
		if(checkLabs(u->arr[i])==0)
			fprintf(out, "%s,%ld\n", u->arr[i].name, u->arr[i].id);
	}
}
int checkLabs(student stu)
{
	int cnt = 0, j;
	for (j = 0; j < 5; j++)
	{
		if (stu.labs[j] == '1')//if the lab was delivered
			cnt++;
	}
	if (cnt >= 3)
		return 1;
	else
		return 0;
}
void endP(FILE *out)
{
	printf("\nThank you. The output in output.txt file.");
	fprintf(out,"\n*End of the program.*");
}