#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#define BUFF_SIZE 256
int main(int argc, char* argv[])
{
	char buff[BUFF_SIZE];
	char* funcArr[5];//array of strings to inputs
	int i, size;
	char* oparation;

	while (1)
	{
		for (i = 0; i < 5; i++)
			funcArr[i] = NULL;
		size = 0;
		printf("AdvShell>");
		scanf(" %[^\n]s", buff);
		oparation = strtok(buff, " ");//name of function
		if (strcmp(oparation, "CreateMenu") == 0)
			size = 3;
		else if (strcmp(oparation, "getMenu") == 0)
			size = 2;
		else if (strcmp(oparation, "MakeOrder") == 0)
			size = 3;
		else if (strcmp(oparation, "getPrice") == 0)
			size = 3;
		else if (strcmp(oparation, "getOrderNum") == 0)
			size = 2;
		else if (strcmp("exit", oparation) == 0)
		{
			printf("GoodBye...\n");
			exit(1);
		}
		else//original shell order
		{
			for (i = 0; i < 4; i++)
			{
				funcArr[i] = oparation;
				oparation = strtok(NULL, " ");
			}
			if (fork() == 0)//if the current process is the son
			{
				execvp(funcArr[0], funcArr);
				printf("Not Supported\n");
				exit(1);
			}
			else if (wait(NULL) == -1)
			{
				printf("Waiting Error!");
				exit(1);
			}
		}

		if (size != 0)//if the function is from the new shell
		{
			for (i = 0; i < size; i++)
			{
				funcArr[i] = oparation;
				oparation = strtok(NULL, " ");
			}

			if (fork() == 0)//if the current process is the son
			{
				execv(funcArr[0], funcArr);
				printf("Not Supported\n");
				exit(1);
			}
			else if (wait(NULL) == -1)
			{
				printf("Waiting Error!\n");
				exit(1);
			}
		}
	}
	return 0;
}