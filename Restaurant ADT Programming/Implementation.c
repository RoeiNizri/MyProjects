#include "header.h" //The header of this functions
void Error_Msg(char* msg)
{
	printf("\n%s", msg);
	exit(1);
}
//the function gets a pointer to a menu and an output files and a pointer to a resturant
//then insert the info fron the menu file to a connected list of dishes in the resturat
void CreateProducts(FILE* pf, resturant* res, FILE* out)
{
	dish* p, * temp, * theDish;//pointer to a dish that found in the menu(not used in this func)
	int price, quantity;
	char str[50];
	while (fscanf(pf, "%s%d%d", str, &quantity, &price) != EOF)
	{
		if (inMenu(str, *res, &theDish) == 1)//if the dish is already in the menu
			fprintf(out, "\n%s is already in the menu", str);

		else
		{
			if ((price < 0) || (quantity < 0))
				fprintf(out, "Price or amount in the stock must be positive.");
			else
			{
				temp = (dish*)malloc(sizeof(dish));
				temp->name = (char*)malloc((strlen(str) + 1) * sizeof(char));
				if ((temp == NULL) || (temp->name == NULL))
					Error_Msg("No memory");
				strcpy(temp->name, str);
				temp->price = price;
				temp->stock = quantity;
				addToHead(&res->menu, temp);
			}
		}
	}
	fprintf(out,"The kitchen was created!");
}
//helping function that gets a double pointer to a menu and a pointer to a dish, 
//then adds that dish to the head of the menu(connected list)
void addToHead(dish** menu, dish* temp)
{

	if (*menu == NULL)		//if the menu is empty
	{
		*menu = temp;
		temp->next = NULL;
	}
	else
	{
		temp->next = *menu;
		*menu = temp;
	}
}
//the function gets a product name, an amount to add, a resturant and an output file
//then adds the amount to the product's stock
void AddItems(char* productName, int quantity, resturant res, FILE* out)
{
	int i;
	dish* theDish;//pointer to the dish we looked for-in the menu
	if (inMenu(productName, res, &theDish) == 1)//if productName is in menu
	{
		theDish->stock += quantity;
		fprintf(out,"\n%d %s were added to the kitchen.",quantity,productName);
	}
	else
		fprintf(out, "\nThere isn't %s in the menu.", theDish);
}

//the function gets a table number, an amount ordered by it, a resturant and an uotput file
//the function addes the order to the connected list of dishes in table num tableNum
void OrderItem(int tableNumber, char* productName, int amount, resturant res, FILE*out)
{
	dish * theOrder,//pointing to a dish we searched- in the order list
		* theDish;//pointing to a dish we searched- in the menu
	table* p;
	if (tableNumber > res.amountOfTables)
		fprintf(out,"\nThere isn't table with this number at our resturant :).");
	else if (inMenu(productName, res, &theDish) == 1)//if the dish is in the menu
	{
		if ((amount < 0) || (amount > theDish->stock))
			fprintf(out,"\nThere isn't enough %s in the stock",productName);
		else
		{
			//if the order was already ordered by that table(no need to add to cntOrders which counts 
			//the number of dishes in each orders connected list)
			if (inTable(tableNumber, productName, res, &theOrder) == 1)
			{
				theOrder->stock -= amount;//updating the new stock in the order list
				theDish->stock -= amount;//updating the new stock in the dish data
				theOrder->amountOfOrders += amount;//updating the amount of each dish that was ordered by table i
			}
			else//if the order havent been ordered by that table yet
			{
				theDish->stock -= amount;				//updating the stock in the menu after the order
				p = &res.tablesArr[tableNumber];//pointer to a table
				createOrderList(amount, theDish->name, theDish->stock, theDish->price, p);
				fprintf(out,"\n%d %s were ordered to table number %d",amount,productName,tableNumber);
			}
		}
	}
	else
		fprintf(out,"\nThere isn't %s in the menu",productName);
}
//helping function that create a dish node, insert the given data to it and adds it to 
//a list of orders in the table she got a pointer to
//also updates the number of dishes in the order list of tha specific table(for realloc)
void createOrderList(int amount, char* order, int stock, int price, table* itable)
{
	dish* temp,*p;
	temp = (dish*)malloc(sizeof(dish));
	temp->name= (char*)malloc((strlen(order) + 1) * sizeof(char));
	strcpy(temp->name, order);
	temp->stock = stock;
	temp->price = price;
	temp->amountOfOrders=amount;
	p = itable->ordersList;
	if (p == NULL)		//if there are no orders yet
	{
		itable->ordersList = (dish*)malloc(sizeof(dish));
		itable->ordersList = temp;
		itable->tail = temp;
		temp->next = NULL;
		itable->cntOrders++;
	}
	else
	{
		itable->ordersList = (dish*)realloc(itable->ordersList, (itable->cntOrders + 1) * sizeof(dish));
		temp->next = itable->tail;
		itable->tail = temp;
		temp->next = NULL;
		itable->cntOrders++;
	}
}
//the function gets a table number, a product name, an amount, a resturant and a pointer 
//to an output file. the func removs the amount of the given dish from the given table
void RemoveItem(int tableNumber, char* productName, int amount, resturant rest, FILE* out)
{
	dish* theDish,//pointer to a dish that found in the menu
		* theOrder,//pointer to an order that found in the orders list of the table
		* p;
	p = rest.tablesArr[tableNumber].ordersList;
	if (tableNumber > rest.amountOfTables)//if table ok
		fprintf(out,"\nTable %d does not exist in this resturant", tableNumber);
	else if(p == NULL)
		fprintf(out, "\nTable number %d doesn't ordered anything-cant remove the dish", tableNumber);
	else if ((inMenu(productName, rest, &theDish) == 0) || (inTable(tableNumber, productName, rest, &theOrder) == 0))
		fprintf(out,"\n Wrong product");
	else if ((amount > p->amountOfOrders) || (amount < 0))
		fprintf(out, "\n Wrong quantity to cancel(%d)",amount);
	else
	{
		theOrder->amountOfOrders -= amount;//decrasing the amount of orders to that table in the amount of returns
		//if after decreasing from that dish quantity of orderes, the dish will be deleted from order list
		if (theOrder->amountOfOrders == 0)
			deleteOrder(tableNumber, theOrder, &rest);
		fprintf(out,"\n%d %s were returned to the kitchen from table %d",amount,productName,tableNumber);
	}
}
//the function gets a product name, a resturant and a double pointer to a dish
//the function returns 1 if that product is in the menu, 0 if not and updates the double pointer to point 
//on the dish we searched in the menu
int inMenu(char* productName, resturant res, dish** theDish)
{
	dish* p;
	p = res.menu;
	while (p != NULL)
	{
		if (strcmp(p->name, productName) == 0)//if there is a dish as productName in the menu
		{
			*theDish = p;
			return 1;
		}
		p = p->next;			//moving on to the next dish
	}
	return 0;
}
//the function gets a product name, a table number, a resturant and a double pointer to a dish
//the function returns 1 if that product is in the order list of the given table and 0 if not.
//the func also updates the double pointer to point on the dish we searched in the order list
int inTable(int tableNum, char* productName, resturant res, dish** theOrder)
{
	dish* p = res.tablesArr[tableNum].ordersList; //p points to the connected list of orders of a table
	while (p != NULL)
	{
		if (strcmp(p->name, productName) == 0)//if the order is already ordered by that table
		{
			*theOrder = p;
			return 1;
		}
		p = p->next;
	}
	return 0;
}
//the function gets a table number, an address of a dish to delete and a pointer to a resturant
//the func delete the dish node from the order list of the given table
void deleteOrder(int tabelNum, dish* toDel, resturant* res)
{
	dish* temp, * p;
	p = res->tablesArr[tabelNum].ordersList;
	if (p == NULL)
		return NULL;
	if (toDel == p)
	{
		temp = p;
		res->tablesArr[tabelNum].ordersList = p->next;
		free(temp->name);
		free(temp);
	}
	else
	{
		while (p->next != toDel)//while we are not in the dish before the one we deleting
			p = p->next;
		temp = p->next;
		p->next = temp->next;
		free(temp->name);
		free(temp);
	}
}
//the function gets a table number, a resturant amd a pointer to an output file.
//the function calculates the check to a leaving table and removes it from the resturant
void RemoveTable(int tableNum, resturant rest, FILE*out)
{
	dish* temp, * p;
	int check = 0, tip=0;
	p = rest.tablesArr[tableNum].ordersList;//p is a pointer to the order list of that table
	if (p == NULL)
		fprintf(out,"\nThere aren't orders to table %d",tableNum);
	else
	{
		while (p != NULL)//deleting all the order list of table i
		{
			check += p->price * (p->amountOfOrders);
			fprintf(out,"\nTable %d ordered: %d %s.",tableNum,p->amountOfOrders,p->name);
			temp = p;
			p = p->next;
			temp->next = NULL;
			free(temp->name);
			free(temp);
		}
		rest.tablesArr[tableNum].ordersList = NULL;
		tip = check / 10;
		fprintf(out,"\nThank you, wish you enjoy with us.\n Price: %d¤\n Tip(10%c): %d¤\n Total price: %d¤", check, '%', tip, check+tip);
	}
}
//the function gets a pointer to a resturant and delets its tables and data
void deleteResturant(resturant *res)
{
	int i;
	dish* temp,*p;
	for (i = 1; i <= res->amountOfTables; i++)//going throgh all the tables in the resturant
	{
		p = res->tablesArr[i].ordersList;
		while (p != NULL)
		{
			temp = p;
			p = p->next;
			free(temp->name);
			free(temp);
		}
	}
	p = res->menu;
	while (p->next!=NULL)//freeing all th dishes in the resturant's menu
	{
		temp = p;
		free(temp->name);
		p = p->next;
		temp->next = NULL;
		free(temp);
	}
}
