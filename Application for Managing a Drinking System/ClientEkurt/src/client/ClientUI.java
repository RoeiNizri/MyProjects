package client;

import client_gui.ConnectToServerController;
import common.Action;
import common.Transaction;
import client_gui.CeoDashboardController;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * This class is responsible for launching the client-side graphical user interface (GUI) for the chat application. 
 */
public class ClientUI extends Application {
	public static ClientController chat; // only one instance

	String args[];
	public static void main( String args[] ) throws Exception
	   { 
			
			clientUtil.ClientUtils.configuration = args[0];
			if(args[0].equals("ek")) {
				clientUtil.ClientUtils.machine = args[1];
			}

		    launch(args);
	   } // end main
	 

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ConnectToServerController ConnectToServer = new ConnectToServerController();
		ConnectToServer.start(primaryStage);
	}
}
