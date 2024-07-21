package client;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import clientUtil.ClientUtils;
import client_gui.ConnectToServerController;
import client_gui.LoginController;
import client_gui.PopUpWindowFXController;
import client_gui.TimeOutWarningController;
import common.Action;
import common.Response;
import common.Transaction;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Window;
import logic.PopUpMessage;
/**
 * The PopUpWindowController class is a scheduler that runs a task every minute to check for new pop-up messages.
 * 
 * @author Alex,Alin,Noy,Ilanit,Ran,Roei
 * @version 1.0
 */

public class PopUpWindowController {
	/**
     * The scheduler is used to schedule the task that runs every minute.
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    /**
     * The EveryMinuteTask method is used to schedule a task that runs every minute to check for new pop-up messages.
     * The task sends a request to the server, and if a message is received, it is displayed on the screen.
     */ 	
	public void EveryMinuteTask() {
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				Transaction msg = new Transaction(Action.POP_UP, ClientUtils.currUser);
				ClientUI.chat.accept(msg);
				msg = ClientUI.chat.getObj();
				if (msg.getResponse() == Response.POPUP) {
					ClientUtils.Popmsg = (PopUpMessage) msg.getData();
					Platform.runLater(() -> {
						try {
							new PopUpWindowFXController().start(new Stage());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				}
			}
		}, 0, 6, TimeUnit.MINUTES);
	}
    /**
     * The stop method is used to stop the scheduler and shutdown the thread pool.*/
    public void stop() {
        scheduler.shutdown();
    }
}
