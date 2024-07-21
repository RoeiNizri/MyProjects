package SimpleFx2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

// This class implements FXML controller.
public class Controller {

	@FXML
	private Button hazaBtn;

	@FXML
	private Button araziBtn;

	@FXML
	private Label vote;

	private int i = 0;

	@FXML
	// Decrease the result in label by click on Yardena.
	void decYardenaAction(ActionEvent event) {
		i--;
		vote.setText("" + i);
		updateLabel(vote, i);
	}

	@FXML
	// Increase the result in label by click on Ofra.
	void incOfraAction(ActionEvent event) {
		i++;
		vote.setText("" + i);
		updateLabel(vote, i);
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
