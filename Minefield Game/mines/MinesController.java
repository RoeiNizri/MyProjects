package mines;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MinesController {
	private Mines field;
	private FieldButtons[][] fieldButtons;
	private int width, height, numOfMines;
	private HBox hbox;
	private Stage stage;
	private Label resultLbl;

	@FXML
	private TextField textFieldWidth;

	@FXML
	private TextField textFieldHeight;

	@FXML
	private TextField textFieldMines;

	@FXML
	void pressReset(ActionEvent event) {
		startGame();
	}

	public void startGame() {
		GridPane gridPane = new GridPane();
		List<ColumnConstraints> column = new ArrayList<>();
		List<RowConstraints> row = new ArrayList<>();
		getBoardVal();
		field = new Mines(height, width, numOfMines);
		fieldButtons = new FieldButtons[height][width];
		createButtons(column, row);
		bootGame(gridPane);
		gridPane.getColumnConstraints().addAll(column);
		gridPane.getRowConstraints().addAll(row);
		hbox.getChildren().remove(hbox.getChildren().size() - 1);
		hbox.getChildren().add(gridPane);
		hbox.autosize();
		stage.sizeToScene();
	}

	// This method get value from text field.
	private void getBoardVal() {
		width = Integer.valueOf(textFieldWidth.getText());
		height = Integer.valueOf(textFieldHeight.getText());
		numOfMines = Integer.valueOf(textFieldMines.getText());
	}

	private void createButtons(List<ColumnConstraints> column, List<RowConstraints> row) {
		for (int i = 0; i < width; i++)
			column.add(new ColumnConstraints(40));
		for (int i = 0; i < height; i++)
			row.add(new RowConstraints(40));
	}

	// This method set all the buttons and booting the game until win or lose.
	private void bootGame(GridPane gridPane) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				fieldButtons[i][j] = new FieldButtons(i, j);
				fieldButtons[i][j].setText(field.get(i, j));
				fieldButtons[i][j].setStyle("-fx-background-color: \r\n" + "#000000,\r\n"
						+ "linear-gradient(#D3D3D3, #BDBDBD),\r\n" + "linear-gradient(#9E9E9E, #7D7D7D),\r\n"
						+ "linear-gradient(#696969, #BDBDBD); -fx-text-fill: #ffffff");
				fieldButtons[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.PRIMARY) { // primary mouse's button will open field.
							boolean reset = field.open(((FieldButtons) event.getSource()).getX(),
									((FieldButtons) event.getSource()).getY());
							updateButtons();
							if (!reset) {
								field.setShowAll(true);
								updateButtons();
								popWindow(true, false);
							}
							if (field.isDone()) {
								popWindow(false, true);
							}
						}
						if (event.getButton() == MouseButton.SECONDARY) { // secondary mouse's button will mark flag.
							int x = ((FieldButtons) event.getSource()).getX();
							int y = ((FieldButtons) event.getSource()).getY();
							field.toggleFlag(x, y);
						}
					}
				});
				fieldButtons[i][j].setMaxWidth(Double.MAX_VALUE);
				fieldButtons[i][j].setMaxHeight(Double.MAX_VALUE);
				gridPane.add(fieldButtons[i][j], j, i);
			}
		}
	}

	// This sub method of "bootGame" updates the style field of the buttons.
	private void updateButtons() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				fieldButtons[i][j].setText(field.get(i, j));
				if (!fieldButtons[i][j].getText().equals(".") && !fieldButtons[i][j].getText().equals(" ")
						&& !fieldButtons[i][j].getText().equals("X")) {
					fieldButtons[i][j].setGraphic(null);
					fieldButtons[i][j].setStyle("-fx-background-color: \r\n" + "linear-gradient(#57C84D, #83D475),\r\n"
							+ "linear-gradient(#ABE098, #C5E8B7),\r\n" + "linear-gradient(#C5E8B7, #ABE098),\r\n"
							+ "linear-gradient(#C5E8B7 0%, #57C84D 50%, #ABE098 100%),\r\n"
							+ "linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\r\n"
							+ "-fx-background-insets: 0,1,2,3,0;\r\n" + "-fx-text-fill: #654b00;\r\n"
							+ "-fx-font-weight: bold;\r\n" + "-fx-font-size: 15px;\r\n;");
				}
				if (fieldButtons[i][j].getText().equals("X")) {
					fieldButtons[i][j].setText("");
					fieldButtons[i][j].setStyle("-fx-background-color: \r\n" + "rgba(0,0,0,0.08),\r\n"
							+ "linear-gradient(#414141, #000000),\r\n"
							+ "linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #DC1C13 100%);\r\n"
							+ "-fx-background-insets: 0 0 -1 0,0,1;");
					Image xImage = new Image("mines/bomb.png");
					ImageView view = new ImageView(xImage);
					view.setFitHeight(15);
					view.setPreserveRatio(true);
					fieldButtons[i][j].setGraphic(view);
				}
				if (fieldButtons[i][j].getText().equals("F")) {
					fieldButtons[i][j].setText("");
					fieldButtons[i][j].setStyle("-fx-background-color: \r\n" + "rgba(0,0,0,0.08),\r\n"
							+ "linear-gradient(#9a9a9a, #909090),\r\n"
							+ "linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);\r\n"
							+ "-fx-background-insets: 0 0 -1 0,0,1;");
					Image flagImage = new Image("mines/flag.png");
					ImageView view = new ImageView(flagImage);
					view.setFitHeight(25);
					view.setPreserveRatio(true);
					fieldButtons[i][j].setGraphic(view);
				}
			}
	}

	// This method creates scene and hbox to result of the game.
	private void popWindow(boolean isOver, boolean isDone) {
		Scene window = new Scene(createWindow(isOver, isDone), 300, 100);
		Stage windowStage = new Stage();
		windowStage.setTitle("Boom");
		windowStage.setScene(window);
		windowStage.show();
	}

	// This method desgins the hbox.
	private HBox createWindow(boolean isOver, boolean isDone) {
		HBox hbox = new HBox();
		if (isOver) {
			resultLbl = new Label("You Lose!");
			resultLbl.setStyle("-fx-background-color:red");
		} else if (isDone) {
			resultLbl = new Label("You Won!");
			resultLbl.setStyle("-fx-background-color:green");
		}
		resultLbl.setFont(Font.font(null, FontWeight.BOLD, 20));
		hbox.setPadding(new Insets(30, 20, 20, 80));
		hbox.getChildren().addAll(resultLbl);
		return hbox;
	}

	public TextField getTextFieldWidth() {
		return textFieldWidth;
	}

	public TextField getTextFieldHeight() {
		return textFieldHeight;
	}

	public TextField getTextFieldMines() {
		return textFieldMines;
	}

	public void setHbox(HBox hbox) {
		this.hbox = hbox;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}