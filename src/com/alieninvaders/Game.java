package com.alieninvaders;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {

	//The BufferStrategy that allows for the usage of accelerated page flipping
	private BufferStrategy strategy;
	//true if game is currently "running" or "looping"
	private boolean gameRunning = true;
	//list of all entities that exist in the game
	private ArrayList entities = new ArrayList();
	//The list of entities that need to be removed from the game this loop
	private ArrayList removeList = new ArrayList();
	//Entity representing the player
	private Entity ship;
	//Speed at which ship should move(pixels/sec)
	private double moveSpeed = 300;
	//Time at which last fired a shot
	private long lastFire = 0;
	//Interval b/w players shot(ms)
	private long firingInterval = 500;
	//Number of aliens on the screen
	private int alienCount;

	//Message to display while waiting for key press
	private String message = "";
	//true if we're holding up game until key has been pressed
	private boolean waitingForKeyPress = true;
	//true if left key is pressed
	private boolean leftPressed = false;
	//true if right key is pressed
	private boolean rightPressed = false;
	//true if we are firing
	private boolean firePressed = false;
	//True if game logic needs to be applied this loop, normally as result of game event
	private boolean logicRequiredThisLoop = false;

	//entry point into game
	public static void main(String[] args){
		Game g = new Game();
		
		g.gameLoop();
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
		
		//request focus so key events come to us
		requestFocus();
		
		//create the buffering strategy which will allow AWT to manage accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		initEntities();

	}//END GAME CONSTRUCTOR

	/*
	 * Initialize the starting state of entities(ship and aliens).
	 * Each enitity will be added to the overall list of entities in the game.
	 */
	private void initEntities(){
		//create a new player ship and place it roughly in the center of screen
		ship = new ShipEntity(this, "ship.gif", 370, 550);
		entities.add(ship);

		//create block of aliens (5 rows by 12 aliens evenly spaced)
		alienCount = 0;
		for(int row = 0; row < 5; row++){
			for(int col = 0; col < 12; col++){
				Entity alien  = 
						new AlienEntity(this, "alien.gif", 100 + (col*50), 50 + (row*30));
				entities.add(alien);
				alienCount++;
			}
		}
	}


	/*
	 * Start a fresh game, this will clear any old data and create a new set.
	 */
	private void startGame(){
		//clear out any existing entities and initialize a new set.
		entities.clear();
		initEntities();

		//wipe current keyboard settings
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
	
	/*
	 * Notification that player has died
	 */
	public void notifyDeath(){
		message = "You died! Try again?";
		waitingForKeyPress = true;
	}
	
	/*
	 * Notification of win since all aliens are killed
	 */
	public void notifyWin(){
		message = "Well done! You win!";
		waitingForKeyPress = true;
	}
	
	/*
	 * Notification alien has been killed
	 */
	public void notifyAlienKilled(){
		//reduce alien count, if none left, player has won
		alienCount--;
		
		if(alienCount == 0){
			notifyWin();
		}
		
		//if aliens still exist, speed them up
		for(int i = 0; i < entities.size(); i++){
			Entity entity = (Entity)entities.get(i);
			
			if(entity instanceof AlienEntity){
				//speed up by 2%
				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
			}
		}
	}
	
	/*
	 * Attempt to fire
	 */
	public void tryToFire(){
		//check if waited long enough to fire
		if(System.currentTimeMillis() - lastFire < firingInterval){
			return;
		}
		//if waited long enough, create shot entity, and record time
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "shot.gif", ship.getX() + 10, ship.getY() - 30);
		entities.add(shot);
	}
	
	/*
	 * Notification from Game Entity that logic is needed at next opportunity
	 */
	public void updateLogic(){
		logicRequiredThisLoop = true;
	}
	
	/*
	 * Remove entity from game. The entity removed will no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed.
	 */
	public void removeEntity(Entity entity){
		removeList.remove(entity);
	}
	
	
	
	/*
	 * The Main Game Loop. This loop is running during all game play and 
	 * is responsible for the following activities:
	 * <p>
	 * -Working out speed of game loop to update moves appropiately.
	 * -Moving game entities.
	 * -Drawing screen contents(entities, text)
	 * -Updating game events.
	 * -Checking for input.
	 * <p>
	 */
	public void gameLoop(){
		long lastLoopTime = System.currentTimeMillis();

		//loop that runs until game ends
		while(gameRunning){
			//calculate how much time passed since the last frame,
			//in order to calculate how far entities should move in
			//this frame
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			//Get hold of a graphics context for accelerated surface and wipe it
			Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);

			//cycle and move entities according to the delta
			if(!waitingForKeyPress){
				for(int i = 0; i < entities.size(); i++){
					Entity entity = (Entity)entities.get(i);
					entity.move(delta);
				}
			}

			//cycle around and draw all the entities in the game
			for(int i = 0; i < entities.size(); i++){
				Entity entity = (Entity)entities.get(i);
				entity.draw(g);
			}

			//brute force collision checking; compare every entity against every other entity.
			//If any of them collides, notify both entities that a collision has occurred.
			for(int i = 0; i < entities.size(); i++){
				for(int j = i + 1; j < entities.size(); j++){
					Entity me = (Entity)entities.get(i);
					Entity him = (Entity)entities.get(j);

					if(me.collidesWith(him)){
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}

			//wipe entities marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			//if game event indicated that game logic should be resolved, 
			//cycle through every entity requesting that their personal logic be 
			//considered
			if(logicRequiredThisLoop){
				for(int i = 0; i < entities.size(); i++){
					Entity entity = (Entity)entities.get(i);
					entity.doLogic();
				}
				logicRequiredThisLoop = false;
			}

			if(waitingForKeyPress){
				g.setColor(Color.white);
				g.drawString(message, (800-g.getFontMetrics().stringWidth(message))/2, 250);
				g.drawString("Press any key", (800-g.getFontMetrics().stringWidth("Press any key"))/2, 300);
			}
			
			//done drawing, so wipe graphics and flip buffer over
			g.dispose();
			strategy.show();
			
			//resolve movement of ship. First assume ship isn't moving.
			//If either cursor key is pressed then update movement accordingly.
			ship.setHorizontalMovement(0);
			if((leftPressed) && !(rightPressed)){
				ship.setHorizontalMovement(-moveSpeed);
			}
			if(!(leftPressed) && (rightPressed)){
				ship.setHorizontalMovement(moveSpeed);
			}
			
			//if pressing fire, attempt fire
			if(firePressed){
				tryToFire();
			}
			
			//sleep for a bit 
			try{
				Thread.sleep(10);
			}catch(Exception e){
				
			}
			
		}//END GAME LOOP
	}

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
			//if we're waiting for "any key" pressed then we don't want to 
			//do anything with just a "press"
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

		/*
		 * Notification from AWT that key has been released.
		 * 
		 * (non-Javadoc)
		 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
		 * @param e Details of key that was released.
		 */
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
			//If we're waiting for "any key" typed the 
			//check if we've received any recently.

			//We may have had keyType() event from user
			//releasing from shoot or move keys, therefore 
			//we use a "pressCount" counter

			if(waitingForKeyPress){
				if(pressCount == 1){
					//we have received our key typed event 
					//so we can now mark it accordingly and
					//start the game!
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				}else{
					pressCount++;
				}
			}

			//if we hit escape, then quit game.
			if(e.getKeyChar() == 27){
				System.exit(0);
			}
		}

	}

}