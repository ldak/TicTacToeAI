package mcts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MctsBot {
	private Strategy strategy;
	private List<Move> moves=new ArrayList<>();
	private int timesPrevMovePlayed=0;

	public MctsBot(Strategy strategy) {
		this.strategy = strategy;
	
	}
	
	public Move load(String[] board){
		Move move=strategy.getMove(board, timesPrevMovePlayed);
		
		move.played();
		timesPrevMovePlayed=move.getTimesPlayed();
		moves.add(move);
		return move;
	}

	public void win(){
		for (Move move : moves) {
			move.win();
		}
	}
	
	public void reset(){
		moves.clear();
		timesPrevMovePlayed=0;
	}
	
	public void saveStrategy(File file) throws FileNotFoundException, IOException{
		if (!file.exists()) {
			file.createNewFile();
		}
		ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file));
		out.writeObject(strategy);
		out.close();
	}
	
	public void loadStrategy(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
		ObjectInputStream in=new ObjectInputStream(new FileInputStream(file));
		strategy=(Strategy) in.readObject();
		in.close();
	}
	
	public Strategy getStrategy() {
		return strategy;
	}
}
