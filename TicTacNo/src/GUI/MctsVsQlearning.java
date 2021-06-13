package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mcts.MctsBot;
import mcts.Strategy;
import q_learning.QBot;
import q_learning.QTable;

import javax.swing.JButton;

public class MctsVsQlearning extends JFrame implements Runnable{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MctsVsQlearning frame = new MctsVsQlearning();
					frame.setVisible(true);
					new Thread(frame).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JButton[] buttons=new JButton[9];
	public boolean playerMove=false;
	public MctsBot bot1;
	public QBot bot2;
	public File saveFile=new File("./ticTacToeMcts.save");
	private long numberInitializations=500000;
	private long games=0;
	public MctsVsQlearning() {
		Strategy strategy =new Strategy();
		QTable strategy2 =new QTable(0.9f,0.95f);
		bot1=new MctsBot(strategy);
		try {
			bot1.loadStrategy(saveFile);;
		} catch (ClassNotFoundException | IOException e1) {
			System.out.println("no save file");
			System.out.println("creating new bot");
			
		}
		bot2=new QBot(strategy2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent windowEvent) {
	            try {
					bot1.saveStrategy(saveFile);
					System.out.println(saveFile.getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    });
		
		int startX=65,startY=80,plusX=300/3,plusY=250/3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				JButton btn = new JButton(" ");
				btn.setBounds(startX+j*plusX, startY+i*plusY, plusX-10, plusY-10);
				btn.setFont(new Font("sherif",Font.BOLD,30));
				btn.addActionListener((a)->{
					if (btn.getText().equals(" ")&&playerMove) {
						btn.setText("O");
						btn.setForeground(Color.RED);
						playerMove=false;
						
					}
				});
				contentPane.add(btn);
				buttons[i*3+j]=btn;
			}
		}
	}
	private long waitMilisec=0;
	private void gameLogic() throws InterruptedException {
		String[] board =getBoard();
		
		
		int i=bot1.load(board).getI();
		buttons[i].setText("X");
		buttons[i].setForeground(Color.GREEN);
		Thread.sleep(waitMilisec);
		if (isWin(getBoard(),"X")) {
			bot1.win();
			bot1.getStrategy().win++;
			bot2.setRelard(0f);
			resetBoard();
		}
		if (isBoardFull()) {
			bot1.getStrategy().tie++;
			bot2.setRelard(0.5f);
			resetBoard();
		}
		
		board =getBoard();
		
		bot2.loadBoard(board);
		i=bot2.getMove();
		buttons[i].setText("O");
		buttons[i].setForeground(Color.RED);
		Thread.sleep(waitMilisec);
		
		if (isWin(getBoard(),"O")) {
			
			bot2.setRelard(1f);
			resetBoard();	
		}
		if (isBoardFull()) {
			bot2.setRelard(0.5f);
			resetBoard();
		}
	}

	private boolean isBoardFull() {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].getText().equals(" ")) {
				return false;
			}
		}
		return true;
	}

	private void resetBoard() {
		games++;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText(" ");
		}
		bot1.getStrategy().games++;
		bot1.reset();
		bot2.resetBoard();
	}

	private boolean isWin(String[] s,String string) {
		for (int i = 0; i <3; i++) {
			if (s[0+3*i]==s[1+3*i]&&s[1+3*i]==s[2+3*i]&&s[2+3*i].equals(string)) {
				return true;
			}
			if (s[0+i]==s[3+i]&&s[3+i]==s[6+i]&&s[6+i].equals(string)) {
				return true;
			}
		}
		if (s[0]==s[4]&&s[4]==s[8]&&s[8].equals(string)) {
			return true;
		}
		if (s[2]==s[4]&&s[4]==s[6]&&s[6].equals(string)) {
			return true;
		}
		return false;
	}

	private String[] getBoard() {
		String[] str=new String[buttons.length];
		for (int i = 0; i < str.length; i++) {
			str[i]=buttons[i].getText();
		}
		return str;
	}
	
	@Override
	public void run() {
		int br=0;
		while(true){
			try {
				gameLogic();
				if (br++==numberInitializations) {
					System.out.println("__________");
					bot1.getStrategy().log();
					br=0;
					//this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				}
				if (bot1.getStrategy().games==1000000) {
					waitMilisec=500;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
