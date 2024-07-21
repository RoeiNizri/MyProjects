#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#define BUFF_SIZE 30

void Write2File(int fdRest, char* temp);//This function write to file
//This function create direction.
void createDir(char* dir);

//CreateMenu Progress
int main(int argc, char* argv[])
{
	char buff[BUFF_SIZE], temp[BUFF_SIZE], temp2[BUFF_SIZE], restName[BUFF_SIZE];
	char n[] = "\n", line[] = "..........", price[10], dots[BUFF_SIZE];
	int i, j = 1, fdRest, m = atoi(argv[2]), flag, s = 0, c = 0, k, type = 96;
	if (argc != 3)
	{
		printf("Error! Enter 2 arguments please.\n");
		exit(1);
	}
	strcpy(restName, argv[1]);
	strcat(restName, ".txt");
	fdRest = open(restName, O_WRONLY | O_CREAT, 0664);	// Creates a file of menu
	if (fdRest == -1)
	{
		perror("Error: Open menu.");
		exit(1);
	}
	strcpy(restName, argv[1]);
	createDir(restName);
	strcpy(restName, argv[1]);
	strcat(restName, " Menu\n");
	Write2File(fdRest, restName);// Writing the name of restaurant
	for (i = 0; i < m; i++)
	{
		flag = 1;
		type++;
		strcpy(temp, "");
		strcpy(buff, "");
		printf("Insert Type Dish %c:\n", type);
		scanf(" %[^\n]s", buff);
		temp2[0] = (char)type;
		sprintf(temp2 + 1, "%s", ". ");
		strcat(temp, temp2);
		strcat(temp, buff);
		strcat(temp, "\n");
		Write2File(fdRest, temp);
		while (flag) {
			strcpy(temp, "");
			strcpy(buff, "");
			strcpy(price, "");
			printf("Insert dish (name & price) %d:\n", j++);
			scanf(" %[^\n]s", buff);
			if (strcmp("Stop", buff) == 0) {
				flag = 0;
				j = 1;
				s = 0;
				c = 0;
				strcpy(temp, "");
				strcpy(buff, "");
				strcpy(price, "");
			}
			else {
				memset(dots, 0, BUFF_SIZE);
				strcpy(dots, "");
				memset(dots, '.', BUFF_SIZE - 5);
				strcat(dots, "NIS");
				for (k = 0; k < strlen(buff); k++)
				{
					if (isdigit(buff[k])) break;
					dots[k] = buff[k];
				}
				k--;
				while (k < strlen(buff)) {
					dots[((strlen(dots) - 3) - (strlen(buff) - k))] = buff[k++];
				}
				strcat(temp, dots);
				strcat(temp, n);
				Write2File(fdRest, temp);
			}
		}
	}
	printf("Succesfully created\n");
	Write2File(fdRest, "\n\nBon Appetit\n\n");
	close(fdRest);
	return 0;
}
// This function get file and string and writes the string to file.
void Write2File(int fdRest, char* temp)
{
	if (write(fdRest, temp, strlen(temp)) == -1)	// Writing types
	{
		close(fdRest);
		perror("Error: Write in menu.");
		exit(1);
	}
}
// This function creates a folder - path - direction.
void createDir(char* dir) {
	int fd_dir;
	strcat(dir, "_Order");
	if ((fd_dir = mkdir(dir, 0777)) == -1) {
		perror("Error: Can't create a folder - path.");
		exit(1);
	}
}