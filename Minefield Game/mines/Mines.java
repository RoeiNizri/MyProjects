package mines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mines {
	private int height, width;
	private int numMines;
	private Point[][] board;
	private List<Place> markedPlaces = new ArrayList<>();
	private boolean showAll = false;

	// Nested classes for each point and place.
	private class Point {
		private boolean open, flag, mine;
		private int numOfMines;

		public void setNumOfMines(int numOfMines) {
			this.numOfMines = numOfMines;
		}
	}

	private class Place {
		@SuppressWarnings("unused")
		private int i, j;

		public Place(int i, int j) {
			this.i = i;
			this.j = j;
		}
	}

	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		board = new Point[height][width];
		this.numMines = numMines;
		// Booting
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				board[i][j] = new Point();
		fillMines();
	}

	private void fillMines() {
		Random rnd = new Random();
		int i, j;
		for (int minesCnt = 0; minesCnt < numMines; minesCnt++) {
			i = rnd.nextInt(height);
			j = rnd.nextInt(width);
			while (board[i][j].mine) {
				i = rnd.nextInt(height);
				j = rnd.nextInt(width);
			}
			board[i][j].mine = true;
			updateBoard(i, j);
		}
	}

	public boolean addMine(int i, int j) {
		if (board[i][j].mine)
			throw new IllegalArgumentException();
		if (i >= height || j >= width)
			throw new IndexOutOfBoundsException();
		board[i][j].mine = true;
		numMines++;
		updateBoard(i, j);
		return true;
	}

	// This method updates the board according to around points. 8 cases.
	private void updateBoard(int i, int j) {
		if (i - 1 >= 0 && j - 1 >= 0 && !board[i - 1][j - 1].mine)
			board[i - 1][j - 1].setNumOfMines(board[i - 1][j - 1].numOfMines + 1);
		if (i - 1 >= 0 && !board[i - 1][j].mine)
			board[i - 1][j].setNumOfMines(board[i - 1][j].numOfMines + 1);
		if (i - 1 >= 0 && j + 1 < width && !board[i - 1][j + 1].mine)
			board[i - 1][j + 1].setNumOfMines(board[i - 1][j + 1].numOfMines + 1);

		if (j - 1 >= 0 && !board[i][j - 1].mine)
			board[i][j - 1].setNumOfMines(board[i][j - 1].numOfMines + 1);
		if (j + 1 < width && !board[i][j + 1].mine)
			board[i][j + 1].setNumOfMines(board[i][j + 1].numOfMines + 1);

		if (i + 1 < height && j - 1 >= 0 && !board[i + 1][j - 1].mine)
			board[i + 1][j - 1].setNumOfMines(board[i + 1][j - 1].numOfMines + 1);
		if (i + 1 < height && !board[i + 1][j].mine)
			board[i + 1][j].setNumOfMines(board[i + 1][j].numOfMines + 1);
		if (i + 1 < height && j + 1 < width && !board[i + 1][j + 1].mine)
			board[i + 1][j + 1].setNumOfMines(board[i + 1][j + 1].numOfMines + 1);
	}

	// This recursive method return true if it isn't a mine. if there isn't mine
	// around open them.
	public boolean open(int i, int j) {
		if (i >= height || j >= width)
			throw new IndexOutOfBoundsException();

		if (board[i][j].open)
			return true;

		if (board[i][j].mine) {
			board[i][j].open = true;
			return false;
		}

		if (!board[i][j].mine && board[i][j].numOfMines != 0) {
			board[i][j].open = true;
			return true;
		}

		if (board[i][j].numOfMines == 0)
			board[i][j].open = true;

		markedPlaces.add(new Place(i, j));
		if (i - 1 >= 0 && j - 1 >= 0 && !board[i - 1][j - 1].mine)
			if (!markedPlaces.contains(new Place(i - 1, j - 1)))
				open(i - 1, j - 1);
		if (i - 1 >= 0 && !board[i - 1][j].mine)
			if (!markedPlaces.contains(new Place(i - 1, j)))
				open(i - 1, j);
		if (i - 1 >= 0 && j + 1 < width && !board[i - 1][j + 1].mine)
			if (!markedPlaces.contains(new Place(i - 1, j + 1)))
				open(i - 1, j + 1);

		if (j - 1 >= 0 && !board[i][j - 1].mine)
			if (!markedPlaces.contains(new Place(i, j - 1)))
				open(i, j - 1);
		if (j + 1 < width && !board[i][j + 1].mine)
			if (!markedPlaces.contains(new Place(i, j + 1)))
				open(i, j + 1);

		if (i + 1 < height && j - 1 >= 0 && !board[i + 1][j - 1].mine)
			if (!markedPlaces.contains(new Place(i + 1, j - 1)))
				open(i + 1, j - 1);
		if (i + 1 < height && !board[i + 1][j].mine)
			if (!markedPlaces.contains(new Place(i + 1, j)))
				open(i + 1, j);
		if (i + 1 < height && j + 1 < width && !board[i + 1][j + 1].mine)
			if (!markedPlaces.contains(new Place(i + 1, j + 1)))
				open(i + 1, j + 1);
		return true;
	}

	// This method marks flag or delete if exist.
	public void toggleFlag(int x, int y) {
		if (x >= height || y >= width)
			throw new IndexOutOfBoundsException();
		if (!board[x][y].open && !board[x][y].flag)
			board[x][y].flag = true;
		else if (board[x][y].flag)
			board[x][y].flag = false;
	}

	// This method return if the player won.
	public boolean isDone() {
		int closed = 0, opened = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				if (!board[i][j].open && board[i][j].mine)
					closed++;
				if (board[i][j].open)
					opened++;
			}
		if (closed == numMines && opened == ((height * width) - numMines))
			return true;
		return false;
	}

	// This method convert element to string.
	public String get(int i, int j) {
		if (board[i][j].flag && !showAll)
			return "F";
		if ((!board[i][j].open && !showAll) || (board[i][j].mine && !showAll))
			return ".";
		if (board[i][j].open || showAll)
			if (board[i][j].mine)
				return "X";
		if ((board[i][j].open || showAll) && board[i][j].numOfMines != 0)
			return String.valueOf(board[i][j].numOfMines);
		return " ";
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	public String toString() {
		StringBuilder mineSB = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				mineSB.append(get(i, j));
			mineSB.append("\n");
		}
		return mineSB.toString();
	}

}
