package server;

import java.io.IOException;
import java.util.List;
import dataBase.dbController;
import javafx.application.Application;
import javafx.stage.Stage;
import server_gui.ServerScreenController;


/**
 * 
 * ServerUI is a class that is responsible for the graphical user interface of
 * the server application. It extends the javafx.application.Application class
 * and overrides the start method to create and display the server screen. It
 * also contains methods for running and stopping the server as well as
 * connecting to the database.
 * 
 * @author Ran Polac, Roei Nizri, Alex, Ilanit, Noy Biton, Alin
 * @version (15/12/2022)
 */
public class ServerUI extends Application {

	final public static int DEFAULT_PORT = 5555;
	static EchoServer sv;
	static dbController db;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ServerScreenController server = new ServerScreenController(); // create frame
		server.start(primaryStage);
	}
	/**
	 * The runServer method takes in a string parameter representing the port number
	 * and a List of strings representing the data needed to connect to the
	 * database. It attempts to connect to the database and runs the server on the
	 * specified port if the connection is successful. It returns a boolean
	 * indicating whether the server was successfully started or not.
	 */
	public static boolean runServer(String p, List<String> data) {
		int port = 0; // Port:
		if (dbController.connectToDB(data)) {
			try {
				port = Integer.parseInt(p); // Set port 5555

			} catch (Throwable t) {
				System.out.println("ERROR - Could not connect!");
				return false;
			}

			sv = new EchoServer(port);

			try {
				sv.listen(); // Listening for connections
			} catch (Exception ex) {
				System.out.println("ERROR - Could not listen for clients!");
				return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * The stopServer method is responsible for stopping the server from listening
	 * for connections and closing the port.
	 */
	public static void stopServer() {
		sv.stopListening(); // stopping listening to the port
		try {
			sv.close(); // closing the port.
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
