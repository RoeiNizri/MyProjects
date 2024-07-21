package clientUtil;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.*;

/**
 * util class that enables you to perform methods on the current client (user of
 * system) connected to the system
 */
public class ClientUtils {
    public static User currUser;
    public static PickUpOrder pickupOrderInProcess;
    public static DeliveryOrder deliveryOrderInProcess;
    public static LocalOrder localOrderInProcess;
    public static List<HBox> cartProducts = new ArrayList<>();
    public static List<VBox> categoryProducts = new ArrayList<>();
    public static boolean cartFlag; //a flag for cart page to display order summary or shopping cart
    public static boolean cartDisplayFlag;
    public static String configuration;
    public static String machine;
    public static PopUpMessage Popmsg;
}