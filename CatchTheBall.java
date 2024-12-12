import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
public class CatchTheBall  extends Frame implements KeyListener,Runnable {
	
	private int paddlex = 150; // staring position for paddle
	private int paddleWidth = 100;
	private int paddleHeight = 20;
	private int ballX = 200;
	private int ballY = 50;
	private int ballDiamiter = 30;
	private int ballVelX = 3; // horizontal velocity
	private int ballVelY = 5; // Vertical velocity
	private int score = 0;
	private Button restartButton;
	private boolean gameRunning = true;
	private int paddleX;
	
	CatchTheBall(){
		setTitle("Catch The Ball");
		setSize(400, 600);
		setLayout(null); // start button position
		setVisible(true);
		addKeyListener (this);
		
		// Adding restart button
				restartButton = new Button("Restart Game");
				restartButton.setBounds(150,350,100,50);
				restartButton.setVisible(false);
				restartButton.addActionListener(e-> restartGame());
				add(restartButton);
		
		// add windows listeners to handle the close button
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Thread gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void restartGame() {
		// TODO Auto-generated method stub
		score = 0;
		 ballX = 200;
		 ballY = 50;
	     ballVelX = 3; // horizontal velocity
		 ballVelY = 5; // Vertical velocity
		 gameRunning = true;
		 restartButton.setVisible(false);
		 repaint();
		 requestFocus();
		 new Thread(this).start();

	}

	@Override
	public void paint(Graphics g) {
if (gameRunning) {
			
			// Draw Paddle
			g.setColor(Color.blue);
			g.fillRect(paddlex,550, paddleWidth, paddleHeight);
			
			// draw ball 
			g.setColor(Color.red);
			g.fillOval(ballX, ballY, ballDiamiter, ballDiamiter);
			
			// score
			g.setColor(Color.black);
			g.drawString("Score : "+ score,10, 50);
		}
		else {
			g.setColor(Color.black);
			g.drawString("Game Over ! Your Score is :"+score, 150, 300);
		}
	}
	private void playSound(String filepath)
	{

		File SoundFile = new File(filepath);
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(SoundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			throw new RuntimeException(e);
		} catch (LineUnavailableException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			System.out.println("Error in playing sound: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(gameRunning) {
			// updating ball position
			ballX+=ballVelX;
			ballY+=ballVelY;
			
			// check for wall collision
			if(ballX<=0 || ballX + ballDiamiter >=getWidth()) {
				ballVelX=-ballVelX; // reverse the horizontal direction
			}
			// check if ball hits the paddle 
			if(ballY + ballDiamiter >=550 && ballX + ballDiamiter >=paddleX && ballX <=paddleX+ paddleWidth)
				playSound("click2.wav");
				ballVelY=-ballVelY; // reverse the vertical direction
			
			
			if(ballY <=0) {
				ballVelY=-ballVelY; // reverse horizontal direction
				int paddleCenter = paddleX + paddleWidth/2;
				int ballCenter = ballX + ballDiamiter / 2;
				
				ballVelX+=(ballCenter - paddleCenter)/10; // add variation in movement
				score++;
			}
			
			// check if ball goes out of boundry
			if(ballY > getHeight())
			{
				gameRunning = false;
				restartButton.setVisible(true);
				
			}
			
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()== KeyEvent.VK_LEFT && paddleX >0 ) {
			paddleX-=20; // move paddle left
	}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && paddleX< getWidth()-paddleWidth) {
			paddleX+=20; // move paddle right
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CatchTheBall b = new CatchTheBall();

	}

}