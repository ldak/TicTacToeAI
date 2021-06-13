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

import q_learning.QBot;
import q_learning.QTable;

import javax.swing.JButton;

public class Game extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game frame = new Game();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
//5141
	/**
	 * Create the frame.
	 */
	public JButton[] buttons=new JButton[9];
	public boolean playerMove=true;
	public QBot bot;
	public File saveFile=new File("./ticTacToe2.save");
	
	
	public Game() {
		QTable strategy =new QTable(0.9f,0.95f);
		try {
			bot =new QBot(saveFile);
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("no save file");
			System.out.println("creating new bot");
			bot=new QBot(strategy);
			
		}
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
					bot.saveStrateqy(saveFile);
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
						gameLogic();
					}
				});
				contentPane.add(btn);
				buttons[i*3+j]=btn;
			}
		}
	}

	private void gameLogic() {
		String[] board =getBoard();
		if (isWin(board,"O")) {
			bot.setRelard(0f);
			bot.getStrategy().lost();
			resetBoard();
			log();
		}else if (isBoardFull(board)) {
			bot.setRelard(0.5f);
			bot.getStrategy().draw();
			resetBoard();
			log();
			board=getBoard();
		}
		bot.loadBoard(board);
		int i=bot.getMove();
		buttons[i].setText("X");
		buttons[i].setForeground(Color.GREEN);
		board=getBoard();
		if (isWin(board,"X")) {
			bot.setRelard(1f);
			bot.getStrategy().win();
			resetBoard();
			
			log();
		}else if (isBoardFull(board)) {
			bot.setRelard(0.5f);
			bot.getStrategy().draw();
			resetBoard();
			log();
		}
		
		playerMove=true;
	}

	private void log() {
		// TODO Auto-generated method stub
		System.out.println("____________");
		bot.getStrategy().log();
	}

	private boolean isBoardFull(String[] board) {
		for (int i = 0; i <board.length; i++) {
			if (board[i].contentEquals(" ")) {
				return false;
			}
		}
		System.out.println("board is full");
		return true;
	}

	private void resetBoard() {
		paint(this.getGraphics());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText(" ");
		}
		bot.resetBoard();
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
}
