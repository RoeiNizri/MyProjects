package mines;

import javafx.scene.control.Button;

// This class extends button to get x and y source.
public class FieldButtons extends Button{
	private int x, y;

	public FieldButtons(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
