package client_gui;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import client.ClientController;
import clientInterfaces.IClientController;
import clientInterfaces.IFxml;
import common.Response;
import javafx.event.ActionEvent;

public class LoginControllerTest {

	LoginController loginController;
	ClientController clientController;
	StubClientController stubClientController;
	StubIFxml stubIFxml;

	String userNameField, passwordField, errorLabel;

	class StubClientController implements IClientController {

		@Override
		public void accept(Object message) {
		}

	}

	class StubIFxml implements IFxml {
		@Override
		public String getTextUserField() {
			// TODO Auto-generated method stub
			return userNameField;
		}

		@Override
		public String getTextPassField() {
			// TODO Auto-generated method stub
			return passwordField;
		}

		@Override
		public void setText(String text) {
			// TODO Auto-generated method stub
			errorLabel = text;
		}
	}

	@Before
	public void setUp() throws Exception {

		stubClientController = new StubClientController();
		stubIFxml = new StubIFxml();
		loginController = new LoginController(stubIFxml, stubClientController);
	}

	@After
	public void tearDown() throws Exception {

		errorLabel = null;

	}

	/**
	 * Test description:Checking if when we insert a user that does not exist.
	 * input:userName = "hey" , password= "bye". 
	 * expected result(output): message to client: "Wrong username or password - Please try again".
	 */
	@Test
	public void login_failed_for_user_not_exist() {
		userNameField = "hey";
		passwordField = "bye";
		String errorLabel = LoginController.onButtonPressLogin_Test(userNameField, passwordField,
				Response.INCORRECT_VALUES);
		assertEquals("Wrong username or password - Please try again", errorLabel);
	}

	/**
	 * Test description: Checking if we inserting an already logged in user.
	 * input: userName ="customer_test" , password="123456" 
	 * expected result:  message to client: "User is already logged in".
	 */
	@Test
	public void login_failed_for_user_alreday_loged_in() {
		userNameField = "customer_test";
		passwordField = "123456";
		String errorLabel = LoginController.onButtonPressLogin_Test(userNameField, passwordField,
				Response.ALREADY_LOGGED_IN);
		assertEquals("User is already logged in", errorLabel);
	}

	/**
	 * Test description: Checking if when we  insert a user with empty fields our login failes.
	 * input: userName = "" , password= "". 
	 * expected result:  message to client: "Please supply both username and password".
	 */
	@Test
	public void login_failed_for_user_emptyFields()
	{
		userNameField = "";
		passwordField = "";
		String errorLabel = LoginController.onButtonPressLogin_Test(userNameField, passwordField,
				Response.INCORRECT_VALUES);
		assertEquals("Please supply both username and password", errorLabel);
	}

	/**
	 * Test description:  Checking if we inserting valid username and password
	 * input: userName = "user" , password= "123456". 
	 * expected result:  message to client: "Successful Login".
	 */
	@Test
	public void login_for_user_succesfull()
	{
		userNameField = "user";
		passwordField = "123456";
		String errorLabel = LoginController.onButtonPressLogin_Test(userNameField, passwordField,
				Response.LOGGED_IN_SUCCESS);
		assertEquals("Successful Login", errorLabel);
	}

}
