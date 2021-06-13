package mcts;

import java.io.Serializable;

public class Move implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4902297492244949331L;
	private int i;
	private int timesPlayed;
	private int wins;
	
	public Move(int i){
		this.i=i;
		
		timesPlayed=0;
		wins=0;
	}
	
	public double calculateSum(int timesPrevMovePlayed){
		 if (timesPlayed == 0) {
	            return Integer.MAX_VALUE;
	        }
	    return ((double) wins / (double) timesPlayed) 
	          + 1.41 * Math.sqrt(Math.log(timesPrevMovePlayed) / (double) timesPlayed);
	}
	
	public boolean isPlayed(){
		return timesPlayed==0;
	}
	
	public void win(){
		wins++;
	}
	
	public void played(){
		timesPlayed++;
	}

	public int getI() {
		return i;
	}

	public int getTimesPlayed() {
		return timesPlayed;
	}
}
