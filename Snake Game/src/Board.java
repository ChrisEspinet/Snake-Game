import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

	private final int BOARD_WIDTH = 300;
	private final int BOARD_HEIGHT = 300;
	private final int DOT_SIZE = 10;
	private final int TOTAL_DOTS = 900;
	private final int RANDOM_POSITION = 29;
	private final int GAME_DELAY = 140;
	
	private final int x[] = new int[TOTAL_DOTS];
	private final int y[] = new int[TOTAL_DOTS];
	
	private int dots;
	private int apple_x;
	private int apple_y;
	
	private boolean leftDirection = false;
	private boolean rightDirection = true;
	private boolean upDirection = false;
	private boolean downDirection = false;
	private boolean inGame = true;
	
	private Timer timer;
	private Image ball;
	private Image apple;
	private Image head;
	
	public Board() {
		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);
		setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		loadImages();
		initGame();
	}
	
	private void loadImages() {
		ImageIcon id = new ImageIcon(getClass().getResource("Images/dot.png"));
		ball = id.getImage();
		
		ImageIcon ia = new ImageIcon(getClass().getResource("Images/apple.png"));
		apple = ia.getImage();
		
		ImageIcon ih = new ImageIcon(getClass().getResource("Images/head.png"));
		head = ih.getImage();
		
	}
	
	private void initGame() {
		dots = 3;
		
		for(int i = 0; i<dots; i++) {
			x[i] = 50 - i*10;
			y[i] = 50;
		}
		
		locateApple();
		
		timer = new Timer(GAME_DELAY, this);
		timer.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	private void doDrawing(Graphics g) {
		if (inGame)	{
			g.drawImage(apple, apple_x, apple_y, this);
			
			for (int i=0; i<dots; i++) {
				if (i==0) {
					g.drawImage(head, x[i], y[i], this);
				} 
				else {
					g.drawImage(ball, x[i], y[i], this);
				}
			}
			
			Toolkit.getDefaultToolkit().sync();
			
		}
		else {
			gameOver(g);
		}
	}
	
	
	private void gameOver(Graphics g) {
		String message = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 18);
		FontMetrics metrics = getFontMetrics(small);
		
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (BOARD_WIDTH - metrics.stringWidth(message)) / 2, BOARD_HEIGHT / 2);
	}
	
	private void checkApple() {
		if ((x[0] == apple_x) && (y[0] == apple_y)) {
			dots++;
			locateApple();
		}
	}

	private void move()	{
		for (int i=dots; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		if (leftDirection) {
			x[0] -= DOT_SIZE;
		}
		
		if (rightDirection) {
			x[0] += DOT_SIZE;
		}
		
		if (upDirection) {
			y[0] -= DOT_SIZE;
		}
		
		if (downDirection) {
			y[0] += DOT_SIZE;
		}
	}
	
	private void checkCollision() {
		for (int i=dots; i>0; i--) {
			if ((i>4) && (x[0] == x[i]) && (y[0] == y[i])) {
				inGame = false;
			}
		}
		
		if (y[0] >= BOARD_HEIGHT) {
			inGame = false;
		}
		
		if (y[0] < 0) {
			inGame = false;
		}
		
		if (x[0] >= BOARD_WIDTH) {
			inGame = false;
		}
		
		if (x[0] < 0) {
			inGame = false;
		}
		
		if (!inGame) {
			timer.stop();
		}
	}
	
	private void locateApple() {
		int random = (int)(Math.random() * RANDOM_POSITION);
		apple_x = random * DOT_SIZE;
		
		random = (int)(Math.random() * RANDOM_POSITION);
		apple_y = random * DOT_SIZE;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(inGame) {
			checkApple();
			checkCollision();
			move();
		}
		
		repaint();
	}
	
	private class TAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_LEFT && !rightDirection) {
				leftDirection = true;
				upDirection = false;
				downDirection = false;
			}
			
			if (key == KeyEvent.VK_RIGHT && !leftDirection) {
				rightDirection = true;
				upDirection = false;
				downDirection = false;
			}
			
			if (key == KeyEvent.VK_UP && !downDirection) {
				upDirection = true;
				leftDirection = false;
				rightDirection = false;
			}
			
			if (key == KeyEvent.VK_DOWN && !upDirection) {
				downDirection = true;
				leftDirection = false;
				rightDirection = false;
			}
			
			
		}
	}

}
