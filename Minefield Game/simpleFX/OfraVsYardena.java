package simpleFX;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// This class implements competition between singers
public class OfraVsYardena extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(updateVBox(), 500, 100);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Ofra Haza VS Yardena Arazi");
		primaryStage.show();
	}

	private int i; // result

	// This method creates & updates vbox by click.
	private VBox updateVBox() {
		VBox vbox = new VBox();
		Label vote = new Label("0");
		Label vs = new Label("VS");
		Button hazaBtn = new Button("Ofra Haza");
		Button araziBtn = new Button("Yardena Arazi");
		GridPane gridBtns = new GridPane();

		class increaser implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) { // Increase the result in label by click.
				i++;
				vote.setText("" + i);
				updateLabel(vote, i);
			}
		}
		class decreaser implements EventHandler<ActionEvent> {
			@Override
			public void handle(ActionEvent event) { // Decrease the result in label by click.
				i--;
				vote.setText("" + i);
				updateLabel(vote, i);
			}
		}
		hazaBtn.setOnAction(new increaser());
		araziBtn.setOnAction(new decreaser());
		vote.setPrefHeight(40);
		vote.setStyle("-fx-background-color:yellow");
		vote.setAlignment(Pos.CENTER);
		vote.setPrefWidth(Double.MAX_VALUE);
		vote.setFont(Font.font("Ariel", FontWeight.BOLD, 18));
		vs.setFont(Font.font("Ariel", FontWeight.BOLD, 18));
		hazaBtn.setTextAlignment(TextAlignment.CENTER);
		hazaBtn.setAlignment(Pos.CENTER);
		hazaBtn.setStyle("-fx-text-fill: green");
		araziBtn.setTextAlignment(TextAlignment.CENTER);
		araziBtn.setAlignment(Pos.CENTER);
		araziBtn.setStyle("-fx-text-fill: red");
		gridBtns.setAlignment(Pos.CENTER);
		gridBtns.add(araziBtn, 0, 0);
		gridBtns.add(vs, 1, 0);
		gridBtns.add(hazaBtn, 2, 0);
		gridBtns.setHgap(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));
		vbox.getChildren().addAll(gridBtns, vote);
		return vbox;
	}
	
	// This method updates the label bar color by value.
	private void updateLabel(Label lbl, int i) {
		if (i > 0)
			lbl.setStyle("-fx-background-color:green");
		if (i < 0)
			lbl.setStyle("-fx-background-color:red");
		if (i == 0)
			lbl.setStyle("-fx-background-color:yellow");
	}
}
