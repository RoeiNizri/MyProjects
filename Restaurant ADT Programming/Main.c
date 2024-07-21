#include "header.h" //The header of this main
void main()
{
	
	FILE* in,*dishF,*out;
	resturant rest;
	int line1,funcNum,quantity,tableNum,i;
	char product[50];
	rest.menu=NULL;
	
	in = fopen("instructions.txt", "rt");
	if (in == NULL)
		Error_Msg("Input file is wrong");
	dishF = fopen("Manot.txt", "rt");
	if (dishF == NULL)
		Error_Msg("Input file is wrong");
	out = fopen("output.txt", "wt");
	if (out == NULL)
		Error_Msg("Input file is wrong");

	fscanf(in,"%d",&line1);
	rest.amountOfTables = line1;
	rest.tablesArr = (table*)malloc((rest.amountOfTables+1)*sizeof(table));
	//alocated 1 cell more than the amount of tables, in order to ignore the cell in the 0's place
	for (i = 1; i <= rest.amountOfTables; i++)
	{
		rest.tablesArr[i].ordersList = NULL;
		rest.tablesArr[i].tail = NULL;
		rest.tablesArr->cntOrders = 0;
	}

	while (fscanf(in, "%d", &funcNum) != EOF)
	{
		if (funcNum == 1)
			CreateProducts(dishF, &rest,out);
		else if (funcNum == 2)
		{
			fscanf(in,"%s%d",product,&quantity);
			AddItems(product, quantity, rest,out);
		}
		else if (funcNum == 3)
		{
			fscanf(in, "%d%s%d",&tableNum, product, &quantity);
			OrderItem(tableNum, product, quantity, rest,out);
		}
		else if (funcNum == 4)
		{
			fscanf(in,"%d%s%d", &tableNum, product, &quantity);
			RemoveItem(tableNum, product, quantity, rest,out);
		}
		else if (funcNum==5)
		{
			fscanf(in, "%d", &tableNum);
			RemoveTable(tableNum, rest,out);
		}
	}
	fclose(dishF);
	fclose(out);
	fclose(in);
	deleteResturant(&rest);
}