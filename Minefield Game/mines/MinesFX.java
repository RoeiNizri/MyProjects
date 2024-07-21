package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MinesFX extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox hbox;
		MinesController controller;
		stage.setTitle("Minesweeper");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Minesweeper.fxml"));
			hbox = loader.load();
			controller = loader.getController();
			GridPane grindPane = new GridPane();
			controller.setHbox(hbox);
			// Default settings
			TextField textFieldWidth = controller.getTextFieldWidth();
			TextField textFieldHeight = controller.getTextFieldHeight();
			TextField textFieldMines = controller.getTextFieldMines();
			textFieldWidth.setText("10");
			textFieldHeight.setText("10");
			textFieldMines.setText("10");
			hbox.setPadding(new Insets(10));
			controller.setStage(stage);
			// Setting background
			BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
			BackgroundImage image = new BackgroundImage(new Image("mines/Panel.png"), BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
			hbox.setBackground(new Background(image));
			hbox.getChildren().add(grindPane);
			controller.startGame();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(hbox);
		stage.setScene(scene);
		stage.show();
	}

}
