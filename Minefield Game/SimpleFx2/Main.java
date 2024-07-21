package SimpleFx2;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// This main class run the program of competition between singers by FXML.
public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		VBox vbox;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("OfraVsYardena.fxml"));
			vbox = loader.load();
			stage.setTitle("Ofra Haza VS Yardena Arazi");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
	}
}
