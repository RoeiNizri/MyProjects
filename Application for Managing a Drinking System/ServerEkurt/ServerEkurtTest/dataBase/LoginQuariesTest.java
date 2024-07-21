package dataBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import common.Action;
import common.Response;
import common.Transaction;
import enums.RegionEnum;
import logic.User;

/*
 * class under testing = login query
 */
public class LoginQuariesTest {

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
	 * Create all the userName and Password that we have on our system for the
	 * testing
	 **/

	/* User name for delivery agent */
	private String userNameDeliveryWorker = "do_north";
	/* User name for service worker */
	private String userNameServiceWorker = "service";
	/* User name for subscriber */
	private String userNameSubscriber = "sub1";
	/* User name for ceo */
	private String userNameCeo = "ceo";
	/* User name for customer */
	private String userNameCustomer = "customer";
	/* User name for regional manager */
	private String userNameRegionalManager = "rm_north";
	/* User name for marketing worker */
	private String userNameMarketingWorker = "mw_uae";
	/* User name for storage worker */
	private String userNameStorageWorker = "storage";

	/* User name for customer that already login */
	private String userNameCustomerLoggedIn = "customer_test";

	// Generic password
	private String password = "123456";

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/* Before each test we will do all those actions for clean start in each test */
	@Before
	public void setUp() throws Exception {
		/* Building our information that will connect to the SQL server */
		List<String> data = new ArrayList<>();
		data.add(DB_NAME);
		data.add(DB_USER);
		data.add(DB_PASSWORD);
		/* Set connection to DB that will use us for the testing the real DB */
		dbController.connectToDB(data);

		
		 /* Set the Transaction to our Login Action, using our sql query LOGIN_USERNAME_PASSWORD*/
		TransactionClassUnderTest = new Transaction(Action.LOGIN_USERNAME_PASSWORD, null, null);
	}

	
	/////////////////////////////////////// testing successful login for our users///////////////////////////////
	/**
	 * Test description:Checking if delivery_agent with the correct user name and password manage to connect.
	 * input:userName = "userNameDeliveryWorker" , password= "123456",answerRecived = delivery_agent account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest
	 */
	@Test
	public void delivery_agent_login_success() throws Exception {

		/*  in answerExpectedForTest we build a new User object */
		User answerExpectedForTest = new User(315478523, "David", "Cohen", "0542525857", "david@gmail.com",
				RegionEnum.NORTH);
		/*  in answerReceived we first initialize it to be null */
		User answerReceived = null;
		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */
		args.put("username", userNameDeliveryWorker);
		args.put("password", password);

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();

		/* Check if the answer is correct - use Assert true */

		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}
	/**
	 * Test description:Checking if service_worker with the correct user name and password manage to connect.
	 * input:userName = "userNameServiceWorker" , password= "123456",answerRecived = service_worker account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest
	 */
	@Test
	public void service_worker_login_success() throws Exception {
		/*  in answerExpectedForTest we build a new User object */

		User answerExpectedForTest = new User(325474523, "Noa", "Yaakobi", "0547396581", "noa@gmail.com",
				RegionEnum.WORLDWIDE);
		/*  in answerReceived we first initialize it to be null */

		User answerReceived = null;
		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */

		args.put("username", userNameServiceWorker);
		args.put("password", password);

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();
		
		/* Check if the answer is correct - use Assert true */
		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}

	/**
	 * Test description:Checking if subscriber with the correct user name and password manage to connect.
	 * input:userName = "userNameSubscriber" , password= "123456",answerRecived = subscriber account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest.
	 */
	@Test
	public void subscriber_worker_login_success() throws Exception {

		/*  in answerExpectedForTest we build a new User object */
		User answerExpectedForTest = new User(316148675, "Alin", "Mig", "0524564478", "alin@gmail.com",
				RegionEnum.SOUTH);
		/*  in answerReceived we first initialize it to be null */

		User answerReceived = null;
		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */
		args.put("username", userNameSubscriber);
		args.put("password", password);

		
		 /* Set the Transaction with the username and password we have in our HashMap */
		
		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();

		/* Check if the answer is correct - use Assert true */

		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}
	/**
	 * Test description:Checking if ceo with the correct user name and password manage to connect.
	 * input:userName = "userNameCeo" , password= "123456",answerRecived = ceo account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest
	 */
	@Test
	public void ceo_login_success() throws Exception {

		/*  in answerExpectedForTest we build a new User object */
		User answerExpectedForTest = new User(208657320, "Ilanit", "Hanooko", "0522098746", "ilanit@gmail.com",
				RegionEnum.WORLDWIDE);
		/*  in answerReceived we first initialize it to be null */

		User answerReceived = null;


		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */

		args.put("username", userNameCeo);
		args.put("password", password);
		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();

		/* Check if the answer is correct - use Assert true */

		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}
	/**
	 * Test description:Checking if customer with the correct user name and password manage to connect.
	 * input:userName = "userNameCustomer" , password= "123456",answerRecived = customer account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest
	 */
	@Test
	public void customer_login_success() throws Exception {

		/*  in answerExpectedForTest we build a new User object */
		User answerExpectedForTest = new User(312238678, "Noy", "Biton", "0523506857", "noy@gmail.com",
				RegionEnum.NORTH);
		/*  in answerReceived we first initialize it to be null */

		User answerReceived = null;
		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */

		args.put("username", userNameCustomer);
		args.put("password", password);

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();

		/* Check if the answer is correct - use Assert true */

		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}
	/**
	 * Test description:Checking if regional_manager with the correct user name and password manage to connect.
	 * input:userName = "userNameRegionalManager" , password= "123456",answerRecived = regional_manager account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest
	 */
	@Test
	public void regional_manager_login_success() throws Exception {

		/*  in answerExpectedForTest we build a new User object */
		User answerExpectedForTest = new User(207452158, "Israel", "Levi", "0523658741", "israel@gmail.com",
				RegionEnum.NORTH);
		/*  in answerReceived we first initialize it to be null */

		User answerReceived = null;
		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */

		args.put("username", userNameRegionalManager);
		args.put("password", password);

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();

		/* Check if the answer is correct - use Assert true */

		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}
	/**
	 * Test description:Checking if marketing_worker with the correct user name and password manage to connect.
	 * input:userName = "userNameMarketingWorker" , password= "123456",answerRecived = marketing_worker account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest
	 */
	@Test
	public void marketing_worker_login_success() throws Exception {

		/*  in answerExpectedForTest we build a new User object */
		User answerExpectedForTest = new User(205678412, "Moshe", "Katz", "0547712546", "moshe@gmail.com",
				RegionEnum.UAE);
		/*  in answerReceived we first initialize it to be null */

		User answerReceived = null;
		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */

		args.put("username", userNameMarketingWorker);
		args.put("password", password);

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();

		/* Check if the answer is correct - use Assert true */

		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}
	/**
	 * Test description:Checking if storage_worker with the correct user name and password manage to connect.
	 * input:userName = "userNameStorageWorker" , password= "123456",answerRecived = storage_worker account object.
	 *  expected result(output): answerRecived equals to answerExpectedForTest
	 */
	@Test
	public void storage_worker_login_success() throws Exception {

		/*  in answerExpectedForTest we build a new User object */
		User answerExpectedForTest = new User(325478521, "Shimon", "Peled", "0548521465", "shimon@gmail.com",
				RegionEnum.WORLDWIDE);
		/*  in answerReceived we first initialize it to be null */

		User answerReceived = null;
		HashMap<String, String> args = new HashMap<String, String>();
		/*  Using the Hashmap we Insert in the Login object the userName and password of our user */

		args.put("username", userNameStorageWorker);
		args.put("password", password);

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = (User) TransactionClassUnderTest.getData();

		/* Check if the answer is correct - use Assert true */

		assertEquals(answerReceived.getId(), answerExpectedForTest.getId());
	}



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Test description:Checking if when we  insert a user with empty fields our login failes.
	 * input:userName = "" , password= "".
	 *  expected result(output):we insert invaild input so the test should return response that says the logged in failed.
	 */
	@Test
	public void login_failed_for_user_emptyFields() {

		Response answerExpectedForTest = Response.INCORRECT_VALUES;
		/*  in answerReceived we first initialize it to be null */
		Response answerReceived = null;

		/* Insert in the Login object the userName and password of not exist user -  */
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("username", "");
		args.put("password", "");

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);

		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = TransactionClassUnderTest.getResponse();
		
		/* Check if the answer is correct - use Assert true */
		
		assertTrue(answerReceived == answerExpectedForTest);
	}
	/**
	 * Test description:Checking if when we  insert a user that does not exist in our project.
	 * input:userName = "hey" , password= bye"".
	 *  expected result(output):we insert invaild input so the test should return response that says the user not exist
	 */
	@Test
	public void login_failed_for_user_not_exist() {

		/* response that will indicate that the user does not exist. */
		Response answerExpectedForTest = Response.INCORRECT_VALUES;
		Response answerReceived = null;

		/* Insert in the Login object the userName and password of not exist user -  */
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("username", "hey");
		args.put("password", "bye");
		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);

		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = TransactionClassUnderTest.getResponse();
		/* Check if the answer is correct - use Assert true.
		  the user not exist so the test should return response that says the user not exist
		*/
		assertTrue(answerReceived == answerExpectedForTest);
	}
	
	/**
	 * Test description:Checking if we inserting an logged in user.
	 *  input:userName = "userNameCustomerLoggedIn" , password="123456" ,
	 *  expected result= answerRecived = response USER_ALREADY_LOGGEDIN
	 */
	@Test
	public void login_failed_for_user_alreday_loged_in() {

		/* response that will indicate that the user exist and logged in) */
		Response answerExpectedForTest =Response.ALREADY_LOGGED_IN;
		Response answerReceived = null;

		/* Insert in the Login object the userName and password of user exist and logged in */
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("username", userNameCustomerLoggedIn);
		args.put("password", password);

		 /* Set the Transaction with the username and password we have in our HashMap */

		TransactionClassUnderTest.setData(args);
		LoginQuaries.loginByUsernameAndPassword(TransactionClassUnderTest);
		answerReceived = TransactionClassUnderTest.getResponse();
		
		/* Check if the answer is correct - use Assert true 
		 * the user not exist so the test should return response that says the user user exist and logged in
		*/
		assertTrue(answerReceived == answerExpectedForTest);
	}
}
