package server_gui;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import Utils.Constants;
import dataBase.ServerQueries;
import dataBase.dbController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.ServerUI;


/**
*
* 
* ServerScreenController is the controller class for the server GUI. It handles
* all the user interactions with the GUI elements and communicates with the
* ServerUI class to start and stop the server. It also contains methods for
* displaying error messages and importing users from external systems.
* 
* @author Ran Polac, Roei Nizri, Alex, Ilanit, Noy Biton, Alin
* @version 1.0
* @since 15/01/2023
*/
public class ServerScreenController implements Initializable {
	
	@FXML
	private Button BTNConnect;
	@FXML
	private Button BTNDisconnect;
	@FXML
	private Button BTNImport;
	@FXML
	private Pane ServerPane;
	@FXML
	private TextField TxtDataBase;
	@FXML
	private TextField TxtIp;
    @FXML
    private PasswordField TxtPassword;
	@FXML
	private TextField TxtPort;
	@FXML
	private TextField TxtUserName;
	@FXML
	private TextField portxt;
	@FXML
	private Label errorLabel = new Label();
	@FXML
	private GridPane ConnectedUsers;
	int isClicked = 0;

	private List<String> data = new ArrayList<String>(); // Array to pass information
	private String hostName;
	private Label ServerManagerIp, ServerManagerStatus, ServerManagerHost;

	private String getport() {
		return TxtPort.getText();
	}
	/**
	 * The method start is used to initialize the server GUI and display it on the
	 * primary stage. It loads the FXML file for the server GUI and sets the title
	 * and icon for the stage. The method also sets the stage to be non-resizable
	 * and adds an event handler for when the user closes the stage to call the
	 * DisconnectInX method.
	 *
	 * @param primaryStage The stage on which the ServerFXML.fxml file will be
	 *                     displayed.
	 */
	public void start(Stage primaryStage) throws Exception {
		//generalMethods.displayScreen(primaryStage, getClass(), "/server_gui/ServerFXML.fxml", "Ekurt Server");
		Parent root = FXMLLoader.load(getClass().getResource("/server_gui/ServerFXML.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Ekurt Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("server_images/EKURT.png")));
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			DisconnectInX(event);
	    });
	}
	
	/**
	 * The method Connect is an event handler for when the user clicks the "Connect"
	 * button. It retrieves the data from the TextFields for the database name,
	 * username, password and port and adds them to an ArrayList. It then calls the
	 * runServer method of the ServerUI class and passes the port number and data
	 * ArrayList as arguments. If the server is successfully started, it creates
	 * labels for the IP, host, and status and adds them to the ConnectedUsers
	 * GridPane.
	 * 
	 * @param event The mouse event that triggers this method.
	 */
	@FXML
	void Connect(ActionEvent event) {
		isClicked =1;
		String p;
		data.add(TxtDataBase.getText());
		data.add(TxtUserName.getText());
		data.add(TxtPassword.getText());
		System.out.println(data.toString());
		p = getport();
		if (p.trim().isEmpty()) {
			System.out.println("Enter a port number please.");

		} else {

			if (ServerUI.runServer(p, data)) {
				// IP
				ServerManagerIp = new Label();
				ServerManagerIp.setText(TxtIp.getText());
				ConnectedUsers.add(ServerManagerIp, 0, 0);
				// HOST
				ServerManagerHost = new Label();
				ServerManagerHost.setText(hostName);
				ConnectedUsers.add(ServerManagerHost, 1, 0);
				// STATUS
				ServerManagerStatus = new Label();
				ServerManagerStatus.setText("Connected-server");
				ConnectedUsers.add(ServerManagerStatus, 2, 0);
				ServerQueries.logoutAll(dbController.conn);
				errorLabel.setVisible(false);
			}
			else
			{
				setErrorNotValid("Wrong username or password - Please try again");
			}
		}
		

	}
	/**
	 * The method Disconnect is an event handler for when the user clicks the
	 * "Disconnect" button. It clears the children of the ConnectedUsers GridPane
	 * and calls the stopServer method of the ServerUI class to stop the server.
	 * 
	 * 
	 * @param event The mouse event that triggers this method.
	 */
	@FXML
	void Disconnect(ActionEvent event) {
		ConnectedUsers.getChildren().clear();// Clear gridpane
		ServerUI.stopServer();
	}
	
	/**
	 * The method DisconnectInX is used to stop the server when the user closes the
	 * stage.
	 * 
	 * 
	 * @param event The mouse event that triggers this method.
	 */
	@FXML
	void DisconnectInX(WindowEvent event) {
		ServerUI.stopServer();
	}
	
	/**
	 * The method Import is an event handler for when the user clicks the "Import"
	 * button. It checks whether the server is connected and calls the
	 * importUsersFromExternalSystem method of the ServerQueries class to import
	 * users
	 * 
	 * @param event The mouse event that triggers this method.
	 */	@FXML
	void Import(ActionEvent event) {
		if(isClicked ==0)
		{
			setErrorNotValid("The users from external system wasn't imported.");
		}
		else if(ServerQueries.importUsersFromExternalSystem(dbController.conn)) {
			setErrorValid("The users from external system was imported.");;
		} else {
			setErrorNotValid("The users from external system imported but\n there is duplicates. Duplicate users not imported,\n new user imported.");
		}
	}
	 /**
		 * The initialize() method is called when the FXML file is loaded. It sets the
		 * default values for the text fields and loads the initial data for the GUI. It
		 * also sets the error label to invisible.
		 * 
		 * @param location  The location of the FXML file
		 * @param resources The resources used to load the FXML file
		 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadData();
		errorLabel.setVisible(false);
	}
	/**
	 * The loadData() method sets the default values for the text fields and loads
	 * the initial data for the GUI.
	 */
	private void loadData() {
		this.TxtPort.setText(String.valueOf(5555));
		try {
			this.TxtIp.setText(InetAddress.getLocalHost().getHostAddress());
			this.TxtIp.setDisable(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.TxtDataBase.setText("jdbc:mysql://localhost/ekurt?serverTimezone=IST");
		this.TxtUserName.setText("root");
		this.TxtPassword.setText("");
		try {
			hostName = InetAddress.getLocalHost().getHostName();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * This method is used to set the error message label to have a red text color
	 * and a specific style (from Constants class) to indicate that the error is not
	 * valid. It also sets the text of the label to the error message passed as a
	 * parameter and makes the label visible.
	 */
	private void setErrorNotValid(String error) {
		errorLabel.setTextFill(Color.RED);
		errorLabel.setStyle(Constants.TEXT_NOT_VALID_STYLE);
		errorLabel.setText(error);
		errorLabel.setVisible(true);
	}
	/**
	 * 
	 * This method is used to set the error message label to have a green text color
	 * and a specific style (from Constants class) to indicate that the error is
	 * valid. It also sets the text of the label to the error message passed as a
	 * parameter and makes the label visible.
	 */
	private void setErrorValid(String error) {
		errorLabel.setTextFill(Color.GREEN);
		errorLabel.setStyle(Constants.TEXT_FIELD_VALID_STYLE);
		errorLabel.setVisible(true);
		errorLabel.setText(error);
	}
}
