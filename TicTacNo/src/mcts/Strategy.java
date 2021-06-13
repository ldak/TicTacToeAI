package mcts;

import java.io.Serializable;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.SynchronousQueue;



public class Strategy implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4243574132763905993L;
	public List<HashMap<String, List<Move>>> data;
	
	public int win=0;
	public int tie=0;
	public int games=0;
	
	public Strategy(){
		data=new ArrayList<>();
		data.add(new HashMap<>());
	}
	
	public void log(){
		System.out.println("Games: "+games);
		System.out.println("Wins: "+win);
		System.out.println("Ties: "+tie);
		System.out.println("Losses: "+(games-win-tie));
		System.out.println("HashMaps: "+data.size());
		System.out.println("HashMap size: "+data.get(data.size()-1).size());
		System.out.println("-----------------");
	}
	
	public Move getMove(String[] board,int timesPrevMovePlayed){
		List<Move> moves=null;
		for (HashMap<String, List<Move>> hashMap : data) {
			moves=hashMap.get(Arrays.toString(board));
			if (moves!=null) {
				break;
			}
		}
		if (moves==null) {
			initilize(board);
			moves=data.get(data.size()-1).get(Arrays.toString(board));
		}
		
		Move chosen=moves.get(0);
		double max=moves.get(0).calculateSum(timesPrevMovePlayed);
		for (Move move : moves) {
			if (max<move.calculateSum(timesPrevMovePlayed)) {
				max=move.calculateSum(timesPrevMovePlayed);
				chosen=move;
			}
		}
		return chosen;
	}
	
	public void initilize(String[] board){
		try{
			
			data.get(data.size()-1).put(Arrays.toString(board), getPossibleMoves(board));
		}catch(Throwable e){
			System.out.println("new hash map");
			data.add(new HashMap<>());
			initilize(board);
		}
	}

	private List<Move> getPossibleMoves(String[] board) {
		List<Move> moves=new ArrayList<>();
		for (int i = 0; i < board.length; i++) {
			if (board[i].contentEquals(" ")) {
				moves.add(new Move(i));
			}
		}
		
		return moves;
	}
	
}
