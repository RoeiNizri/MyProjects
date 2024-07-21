package dataBase;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import common.*;

/*
 * class under testing = report query
 */
public class CEOQuariesTest {

	/*  DB name */
	final public static String DB_NAME = "jdbc:mysql://localhost/ekurt?serverTimezone=IST";

	/*  DB user name */
	final public static String DB_USER = "root";

	/*  DB password for specific user */
	final public static String DB_PASSWORD = "Aa123456";

	
	/* Transaction received from the test to make our tests */
	public Transaction TransactionClassUnderTest;

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * before each test we setup the variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		/* Before each test we will do all those actions for clean start in each test */
		List<String> data = new ArrayList<>();
		data.add(DB_NAME);
		data.add(DB_USER);
		data.add(DB_PASSWORD);
		/* Set connection to DB that will use us for the testing the real DB */
		dbController.connectToDB(data);

				 /* Set the Transaction to our TransactionClassUnderTest, using our sql query GET_INVENTORY_REPORT*/
		
		TransactionClassUnderTest = new Transaction(Action.GET_INVENTORY_REPORT, null, null);
	}

	/**
	 * Test description: tests the functionality of the getInventoryReport method in the CEOQuaries class.
	 *  then checks if the response received is the same as the expected response.
	 * input:String machine = "Braude"
	 *output: answerRecived equals to answerExpectedForTest, both reports are the same. the check needs to return true
	 * 
	 */
	@Test
	public void Inventory_Report_success() {
	    // expected result from CEOQuaries.getInventoryReport 
	    Response answerExpectedForTest = Response.GETINVENTORYREPORT_SUCCESSFULLY;
	    Response answerReceived = null;
	    // machine name to be passed as a parameter to the getInventoryReport method
	    String machine = "Braude";
	    // sets the Transaction for the TransactionClassUnderTest with the data we got

	    TransactionClassUnderTest.setData(machine);
	    // calls the getInventoryReport method for the specific machine Braude
	    CEOQuaries.getInventoryReport(TransactionClassUnderTest, dbController.conn);
	    answerReceived = TransactionClassUnderTest.getResponse();
	    // checks if the response received is the same as the expected response
	    assertTrue(answerReceived == answerExpectedForTest);
	}
	/**
	 * Test description: tests the functionality of the getInventoryReport method in the CEOQuaries class.
	 *  then checks if the response received is the same as the expected response.
	 * input:String machine = ""
	 *output: answerRecived not equals to answerExpectedForTest, both reports are not the same. the check doesnt needs to return true
	 * 
	 */
	@Test
	public void Inventory_Report_Unsuccess() {

	    // expected result from CEOQuaries.getInventoryReport 
		Response answerExpectedForTest = Response.GETINVENTORYREPORT_UNSUCCESSFULLY;
		Response answerReceived = null;

	    // machine name to be passed as a parameter to the getInventoryReport method = in our case nothing.
		String machine = "";

	    // sets the Transaction for the TransactionClassUnderTest with the data we got

		TransactionClassUnderTest.setData(machine);
	    // calls the getInventoryReport method for the specific machine = in our case there is not such machine.

		CEOQuaries.getInventoryReport(TransactionClassUnderTest, dbController.conn);
		answerReceived = TransactionClassUnderTest.getResponse();

	    // checks if the response received is the same as the expected response

		assertTrue(answerReceived == answerExpectedForTest);
	}

	/**
	 * Test description: Test case for testing the functionality of  creating an inventory report in the XSL format.
	 * input: String machine = "Braude"
	 * output: Asserts that the output received from the "getInventoryReportToXsl" method  is equal to the expected output.
	 */
	@Test
	public void Create_Inventory_Xsl_Success() {

	    // expected result from CEOQuaries.getInventoryReportToXLS

		Response answerExpectedForTest = Response.GET_INVENTORY_REPORT_TO_XLS_SUCCESSFULLY;
		Response answerReceived = null;

	    // machine name to be passed as a parameter to the getInventoryReport method

		String machine = "Braude";
	    // sets the Transaction for the TransactionClassUnderTest with the data we got
		TransactionClassUnderTest.setData(machine);
		CEOQuaries.getInventoryReportToXsl(TransactionClassUnderTest, dbController.conn);
		answerReceived = TransactionClassUnderTest.getResponse();

	    // checks if the response received is the same as the expected response
		// in our case should return True.
		assertTrue(answerReceived == answerExpectedForTest);
	}

	/**
	 * Test description: Test case for testing the functionality of  creating an inventory report in the XSL format.
	 * input: String machine = ""
	 * output Asserts that the output received from the "getInventoryReportToXsl" method  is equal to the expected output.
	 */
	@Test
	public void Create_Inventory_Xsl_unSuccess() {

	    // expected result from CEOQuaries.getInventoryReportToXLS

		Response answerExpectedForTest = Response.GET_INVENTORY_REPORT_TO_XLS_UNSUCCESSFULLY;
		Response answerReceived = null;

	    // machine name to be passed as a parameter to the getInventoryReport method : in our case no machine name

		String machine = "";

	    // sets the Transaction for the TransactionClassUnderTest with the data we got
		TransactionClassUnderTest.setData(machine);
		CEOQuaries.getInventoryReportToXsl(TransactionClassUnderTest, dbController.conn);
		answerReceived = TransactionClassUnderTest.getResponse();
	    // checks if the response received is the same as the expected response
		// in our case should return False.

		assertTrue(answerReceived == answerExpectedForTest);
	}
}
