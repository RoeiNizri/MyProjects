#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <ctype.h>
#include <time.h>
#include <assert.h>
#define BUFF_SIZE 30

// This function get file and string and writes the string to file.
int Write2File(int fdRest, char* temp);
// This function creates a folder - path - direction.
void createDir(char* dir);
// This function get file and string and writes the string to file.
int createFile(char* file);
// This funcion returns int of a price.
int GetPrice(char* file, char* productName);
// This function opens a file.
int openFile(char* file);
// This function copys from file to ../dir/file.
void copyFile(char* dir, char* file);
//MakeOrder Progress
int main(int argc, char* argv[])
{
	char buff[BUFF_SIZE], temp[BUFF_SIZE], restName[BUFF_SIZE], buffCount[BUFF_SIZE], temp2[BUFF_SIZE];
	char ch, s[11], buffPrice[BUFF_SIZE], buffName[BUFF_SIZE];
	int fdOrder, fdFrom, fdTemp, rbytes = 1, sum = 0, cnt = 0, k = 0, i = 0, flag = 0, flag2, m1, m2, price;
	time_t t = time(NULL);
	struct tm* tm = localtime(&t);
	if (argc != 3)
	{
		printf("Error! Enter 2 arguments please.\n");
		exit(1);
	}
	strcpy(restName, argv[1]);
	strcat(restName, ".txt");
	if (openFile(restName) == -1)
	{
		printf("Error: Restaurant Not Found.\n");
		exit(1);
	}

	do
	{
		strcpy(restName, "");
		strcat(restName, "~/");
		strcpy(restName, argv[1]);
		strcat(restName, "_Order");
		chdir(restName);
		flag2 = 0;
		sum = 0;
		fdTemp = createFile("temp.txt"); //Create file in the orders folder
		printf("Insert your order (Finish to finish):\n");
		do
		{
			i = 0;
			k = 0;
			strcpy(temp2, "");
			strcpy(buffCount, "");
			memset(temp2, 0, sizeof(temp2));
			rbytes = 1;
			strcpy(buff, "");
			memset(buff, 0, sizeof(buff));
			scanf(" %[^\n]s", buff);
			if (strcmp(buff, "Finish") == 0)flag2 = 1;
			else
			{
				strcat(buff, "\n");
				Write2File(fdTemp, buff);
				//Count Dishes
				while (!(buff[i] == '\n' || buff[i] == '\0'))
				{
					if (buff[i] >= '0' && buff[i] <= '9')
						buffCount[k++] = (buff[i]);
					i++;
				}
				cnt = atoi(buffCount);
				i = 0;
				//GetPrice	
				do
				{
					temp2[i] = buff[i];
					i++;
				} while (!(buff[i] >= '0' && buff[i] <= '9') && i < strlen(buff) && buff[i] != '\n' && buff[i] != '\0');

				//CHdir
				strcpy(restName, "/home/braude/HW1");
				chdir(restName);
				strcpy(restName, "");
				strcpy(restName, argv[1]);
				strcat(restName, ".txt");
				sum = sum + (GetPrice(restName, temp2) * cnt);
			}
		} while (flag2 == 0);
		printf("Total price: %d NIS (Confrim - to aprove, other please insert new order please.\n", sum);
		scanf(" %[^\n]s", buff);
		if (strcmp(buff, "Confirm") == 0) {
			flag = 1;
			memset(buff, 0, sizeof(buff));
			sprintf(buff, "Total Price: %d NIS\nDate:", sum);
			strcpy(restName, "");
			strcat(restName, "~/");
			strcpy(restName, argv[1]);
			strcat(restName, "_Order");
			chdir(restName);
			Write2File(fdTemp, buff);
			strcpy(s, "");
			assert(strftime(s, sizeof(s), "%d/%m/%Y", tm));
			Write2File(fdTemp, s);
			copyFile("temp.txt", argv[2]);// Creates a file of order	
			remove("temp.txt");
		}
	} while (flag == 0);
	printf("Order created!");
	strcpy(temp, restName);
	strcat(temp, "/");
	strcat(temp, argv[2]);
	chmod(temp, O_RDONLY);
	//close(fdOrder);
	return 0;
}
// This function get file and string and writes the string to file.
int Write2File(int fdRest, char* temp)
{
	if (write(fdRest, temp, strlen(temp)) == -1)	// Writing types
	{
		close(fdRest);
		perror("Error: Write in menu.");
		return -1;
	}
}
// This function creates a folder - path - direction.
void createDir(char* dir) {
	int fd_dir;
	if ((fd_dir = mkdir(dir, 0777)) == -1) {
		perror("Error: Can't create a folder - path.");
		exit(1);
	}
}
int createFile(char* file) {
	int fd_copy;
	if ((fd_copy = open(file, O_WRONLY | O_CREAT, 0664)) == -1) {
		perror("Error: Can't create the file.");
		exit(1);
	}
	return fd_copy;
}

int GetPrice(char* file, char* productName) {
	char ch, temp[BUFF_SIZE], buff[BUFF_SIZE], buffPrice[BUFF_SIZE];
	int i, m1, m2, flag = 1, fdFrom, rbytes = 1;
	fdFrom = openFile(file);
	while (rbytes > 0)
	{
		memset(buffPrice, 0, sizeof(buffPrice));
		memset(buff, 0, sizeof(buff));
		m1 = 0;
		m2 = 0;
		while (m1 < BUFF_SIZE && rbytes>0)
		{
			if ((rbytes = read(fdFrom, &ch, 1)) == -1) {
				perror("Error: Can't read the file\n");
				exit(1);
			}
			buff[m1++] = ch;
			if (ch >= '0' && ch <= '9')
				buffPrice[m2++] = ch;
			if (ch == '\n' || ch == '\0')
				break;
		}if (strstr(buff, productName) != NULL) {
			close(fdFrom);
			return atoi(buffPrice);
		}
	}
	printf("There isn't dish with this name.\n");
	close(fdFrom);
	return 0;
}
int openFile(char* file) {
	int fd_from;
	if ((fd_from = open(file, O_RDONLY)) == -1) {
		perror("Error: Can't open the file, check the name.\n");
		exit(1);
	}
	return fd_from;
}
void copyFile(char* dir, char* file) {
	char buff[BUFF_SIZE];
	int rbytes, wbytes;
	int fd_from, fd_to;
	strcat(file, ".txt");
	fd_to = createFile(file);
	fd_from = openFile(dir);
	do {
		if ((rbytes = read(fd_from, buff, BUFF_SIZE)) == -1) {
			perror("Error: Can't read the file\n");
			exit(1);
		}
		if ((wbytes = write(fd_to, buff, rbytes)) == -1) {
			perror("Error: Can't copy the file\n");
			exit(1);
		}
		if (rbytes != wbytes) {
			perror("Error: Can't write to file\n");;
			exit(1);
		}
	} while (rbytes > 0);
	close(fd_from);
	close(fd_to);
}