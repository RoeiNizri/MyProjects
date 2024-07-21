#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#define BUFF_SIZE 256

// This function prints a text file.
void PrintFile(char* file);
// This function opens a file.
int openFile(char* file);

int main(int argc, char* argv[])
{
	if (argc != 2)
	{
		printf("Error! Enter 1 argument please.\n");
		exit(1);
	}
	strcat(argv[1], ".txt");
	PrintFile(argv[1]);
}
void PrintFile(char* file) {
	char buff[BUFF_SIZE];
	int fd_from, rbytes;
	fd_from = openFile(file);
	// Read % Print
	do {
		if ((rbytes = read(fd_from, buff, 256)) == -1) {
			perror("Error: Can't read the file");
			exit(1);
		}
	} while (rbytes > 0);
	printf("%s", buff);
	close(fd_from);
}
int openFile(char* file) {
	int fd_from;
	if ((fd_from = open(file, O_RDONLY)) == -1) {
		perror("Error: Can't open the file, check the name.");
		exit(1);
	}
	return fd_from;
}