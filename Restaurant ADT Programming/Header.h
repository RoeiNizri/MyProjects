#ifndef header
#define header
#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
typedef struct table					//a maneger struct, manage a table
{
	struct dish *ordersList,*tail;		//the dishes that this table ordered
	int cntOrders;						//how many dishes each table ordered
}table;
typedef struct Dish						//Struct for dish
{
	char* name;
	int price,
		stock,							//the amount of that dish in stock
		amountOfOrders;					//the amount of orders each dish got
	struct dish* next;
}dish;
typedef struct resturant				//Struct for resturant
{	
	int amountOfTables;					//the amount of tabls in the resturant
	struct table* tablesArr;
	struct dish* menu;
}resturant;

void RemoveTable(int tableNum, resturant rest,FILE*);
void deleteOrder(int tabelNum, dish* toDel, resturant* res);
int inTable(int tableNum, char* productName, resturant res, dish** theOrder);
int inMenu(char* productName, resturant res, dish** theDish);
void RemoveItem(int tableNumber, char* productName, int amount, resturant rest, FILE*);
void createOrderList(int amount, char* order, int stock, int price, table* itable);// , int* cnt);
void OrderItem(int tableNumber, char* productName, int amount, resturant res, FILE*);
void AddItems(char* productName, int quantity, resturant res, FILE*);
void addToHead(dish** menu, dish* temp);
void CreateProducts(FILE* pf, resturant* res, FILE*);
void Error_Msg(char* msg);
void deleteResturant(resturant* res);
#endif