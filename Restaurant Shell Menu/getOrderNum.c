#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <dirent.h> 
#define BUFF_SIZE 30
//This function count files in path.
void countFiles(char* path);

int main(int argc, char* argv[])
{
	char restName[BUFF_SIZE];
	if (argc != 2)
	{
		printf("Error! Enter 1 argument please.");
		exit(1);
	}
	strcpy(restName, argv[1]);
	strcat(restName, "_Order");
	chdir("/home/braude/HW1");
	countFiles(restName);

}
void countFiles(char* path)
{
	struct dirent* dp;
	int c = -2;
	DIR* dir = opendir(path);
	// Unable to open directory stream
	if (!dir)
	{
		printf("Error! There isn't order file.");
		exit(1);
	}
	while ((dp = readdir(dir)) != NULL)
		c++;
	// Close directory stream
	closedir(dir);
	printf("There are %d orders.", c);
}