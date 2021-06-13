package q_learning;

import java.awt.geom.FlatteningPathIterator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class QTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7211283484112107908L;
	private HashMap<String, float[]> qTable = new HashMap<>();
	private float alpha;
	private float gamma;
	private int win=0;
	private int lost=0;
	private int draw=0;
	
	public QTable(float alpha, float gamma) {
		this.alpha = alpha;
		this.gamma = gamma;
	}
	
	public void lost(){
		lost++;
	}
	public void draw(){
		draw++;
	}
	public void win(){
		win++;
	}
	
	public void log(){
		System.out.println("Wins: "+win);
		System.out.println("Lost: "+ lost);
		System.out.println("Draw: "+draw);
		System.out.println("Maps: "+qTable.keySet().size());
	}

	public void set(String[] board, int move, float value) {
		float[] f =getMoves(board); 
		f[move] = value;
		qTable.put(Arrays.toString(board), f);
	}

	public void update(String[] board, int move, String[] nextBoard) {
		float[] f = getMoves(board); 
		f[move] = f[move] + alpha * (gamma * getMaxValue(nextBoard) - f[move]);
		qTable.put(Arrays.toString(board), f);
	}

	public float getMaxValue(String[] board) {
		float[] f = getMoves(board); 
		if (f == null) {
			System.err.println("faaaaaaaaaaaaaaaaaaaaaaaaaaaaakkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
			return 0f;
		}
		int i = 0;
		float max = f[i++];
		while (!(max == max)) {
			max = f[i++];
		}
		for (; i < f.length; i++) {
			if (max < f[i]) {
				max = f[i];
			}
		}
		return max;
	}

	public int getGoodMove(String[] board) {
		List<Integer> moves = new ArrayList<>();
		float[] f = getMoves(board); 
		int i = 0;
		float max = f[i++];
		
		while (!(max==max)) {
			max = f[i++];
		}
		for (i=0; i < f.length; i++) {
			if (max < f[i]) {
				max = f[i];
				moves.clear();
				moves.add(i);
			} else if (max == f[i]) {
				moves.add(i);
			}
		}
		Random rnd = new Random();
		i = rnd.nextInt(moves.size());
		return moves.get(i);
	}
	
	private float[] getMoves(String[] board) {
		float[] f = qTable.get(Arrays.toString(board));
		if (f==null) {
			initializeHash(board);
			f = qTable.get(Arrays.toString(board));
		}
		return f;
	}
	
	private void initializeHash(String[] m) {

		float[] f = new float[m.length];
		for (int i = 0; i < f.length; i++) {
			f[i] = m[i].contentEquals(" ") ? 0.6f : Float.NaN;
		}
		qTable.put(Arrays.toString(m), f);
	}
	/*
	 * private void initializeHash() { String[] arr={" ","o","x"}; for (int i1 =
	 * 0; i1 < arr.length; i1++) { for (int i2 = 0; i2 < arr.length; i2++) { for
	 * (int i3 = 0; i3 < arr.length; i3++) { for (int i4 = 0; i4 < arr.length;
	 * i4++) { for (int i5 = 0; i5 < arr.length; i5++) { for (int i6 = 0; i6 <
	 * arr.length; i6++) { for (int i7 = 0; i7 < arr.length; i7++) { for (int i8
	 * = 0; i8 < arr.length; i8++) { for (int i9 = 0; i9 < arr.length; i9++) {
	 * String[] m=new String[9]; m[0]=arr[i1]; m[1]=arr[i2]; m[2]=arr[i3];
	 * m[3]=arr[i4]; m[4]=arr[i5]; m[5]=arr[i6]; m[6]=arr[i7]; m[7]=arr[i8];
	 * m[8]=arr[i9]; initializeHash(m); } } } } } } } } }
	 * System.out.println("initialized"); }
	 */

}
