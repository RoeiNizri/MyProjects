package client;

import java.time.Duration;
import java.time.Instant;
import clientUtil.ClientUtils;
import client_gui.ConnectToServerController;
import client_gui.LoginController;
import client_gui.MainDashboradController;
import client_gui.TimeOutWarningController;
import enums.RoleEnum;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

/**

*TimeMeasureThread is a class that runs a thread that checks for inactivity in the client application.
*The thread starts by measuring the time from when it is instantiated, and if the time exceeds 1 minute,
*a warning window is displayed to the user. If the time exceeds 2 minutes, the thread closes and the user is logged out.
*The thread can be reset by calling the resetStartTime() method.
*/

public class TimeMeasureThread extends Thread implements Runnable {

	private static Instant startTime;
	private boolean running;
	public static boolean flag;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public TimeMeasureThread() {
		startTime = Instant.now();
		running = true;
		flag = true;
	}
	/**
	*The run method for the thread. This method checks for inactivity every second and displays a warning window
	*if the time exceeds 5 minute. If the time exceeds 6 minutes, the thread closes and the user is logged out.
	*/

	public void run() {
		while (running) {
			if (Duration.between(startTime, Instant.now()).toMinutes() >= 1 && flag) {
				flag = false;
				Platform.runLater(() -> {
					try {
						new TimeOutWarningController().start(new Stage());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}
			if (Duration.between(startTime, Instant.now()).toMinutes() >= 2) {
				// Close the thread after 10 minutes of inactivity
				running = false;
				flag = true;
				System.out.println("5 minutes of inactivity. Closing thread.");
				if (ClientUtils.currUser != null) {
					if (ClientUtils.currUser.getRole().equals(RoleEnum.CUSTOMER)
							|| ClientUtils.currUser.getRole().equals(RoleEnum.SUBSCRIBER)) {
						MainDashboradController.PopUpWindowController.stop();
					}
						LoginController.logout(ClientUtils.currUser.getUsername());

				}
				for (Window window : Window.getWindows()) {
					if (window instanceof Stage) {
						Platform.runLater(() -> ((Stage) window).close());
					}
				}
				Platform.runLater(() -> {
					try {
						new LoginController().start(new Stage());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

				break;
			}
			try {
				Thread.sleep(1000); // check every 1 second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	*Resets the start time for the thread.
	*/

	public static void resetStartTime() {
		startTime = Instant.now();
	}
	/**
	*Attaches an event handler to the scene or any other node.
	*@param eventHandler An instance of EventHandler<MouseEvent> to handle mouse events.
	*/
	public void attachEventHandler(EventHandler<MouseEvent> eventHandler) {
		// Attach the event handler to the scene or any other node
	}

}
