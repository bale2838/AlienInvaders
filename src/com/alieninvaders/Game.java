package com.alieninvaders;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
	//true if we're holding up game until key has been pressed
	private boolean waitingForKeyPress = true;

	//true if left key is pressed
	private boolean leftPressed = false;

	//true if right key is pressed
	private boolean rightPressed = false;

	//true if we are firing
	private boolean firePressed = false;

	//entry point into game
	public static void main(String[] args){

		Game g = new Game();

	}

	/*
	 * Construct game and prepare to run it.
	 */

	public Game(){
		//contains the game
		JFrame container = new JFrame("Alien Invaders");

		//get content of frame and set resolution
		JPanel panel = (JPanel)container.getContentPane();
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(null);


		//setup canvas size and put it into content of the game
		setBounds(0, 0, 800, 600);
		panel.add(this);

		//tell AWT to not repaint the canvas since we are doing that ourself
		//accelerated mode
		setIgnoreRepaint(true);

		//make window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		//add listener to respond to user closing window. We want to
		//exit if they do.
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});

		//add key input system to the canvas so we can respond to a key pressed
		addKeyListener(new KeyInputHandler());

	}//END GAME CONSTRUCTOR

	/*
	 * An inner class used to handle keyboard input from the user.
	 */

	private class KeyInputHandler extends KeyAdapter {
		private int pressCount = 1;

		/*
		 * Notification from AWT that a key has been pressed.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
		 * @param e Details of the key that was pressed.
		 */

		public void keyPressed(KeyEvent e){
			//if we're waiting for "any key" typed then we don't want to
			//do anything with just a "pressed"
			if(waitingForKeyPress){
				return;
			}

			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftPressed = true;
			}

			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightPressed = true;
			}

			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				firePressed = true;
			}
		}

		public void keyReleased(KeyEvent e){
			//if we're waiting for "any key" typed then we don't want to
			//do anything with just a "released"
			if(waitingForKeyPress){
				return;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftPressed = false;
			}
		
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightPressed = false;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				firePressed = false;
			}	
		}
		
		public void keyTyped(KeyEvent e){
			
		}
		
		
	}
}