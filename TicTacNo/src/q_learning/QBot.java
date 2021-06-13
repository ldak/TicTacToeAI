package q_learning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class QBot {

	private QTable strategy;
	private String[] board = null;
	private int move;

	public QBot(QTable strategy) {
		this.strategy = strategy;
	}

	public QBot(File file) throws FileNotFoundException, ClassNotFoundException, IOException {
		loadStrateqy(file);
	}

	public void loadBoard(String[] newBoard) {
		if (board != null) {
			strategy.update(board, move, newBoard);
		}
		board = newBoard;
	}

	public void resetBoard() {
		board = null;
	}

	public int getMove() {
		move = strategy.getGoodMove(board);
		return move;
	}

	public void setRelard(float value) {
		strategy.set(board, move, value);
	}

	private void loadStrateqy(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		this.strategy = (QTable) in.readObject();
	}

	public void saveStrateqy(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		}

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(strategy);
		out.close();

	}

	public QTable getStrategy() {
		return strategy;
	}

}
