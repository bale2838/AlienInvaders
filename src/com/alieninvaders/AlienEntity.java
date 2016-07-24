package com.alieninvaders;

/*
 * An entity that represents one of the space invader aliens
 */
public class AlienEntity extends Entity {
	//The alien's horizontal speed
	private double moveSpeed = 75;
	//Game where entity exists
	private Game game;
	//
	private boolean movedDown = false;

	/*
	 * Construct a new alien entity
	 * 
	 * @param game The game in which this entity is created
	 * @param ref The sprite that is displayed for this alien
	 * @param x The initial x location of this alien
	 * @param y The initial y location of this alien
	 */
	public AlienEntity(Game game, String ref, int x, int y){
		super(ref, x, y);
		this.game = game;
		dx = -moveSpeed;
		//dy = moveSpeed;
		
	}

	/*
	 * Request that alien move based on time elapsed
	 * 
	 * @param delta Time that elapsed since last frame
	 */
	public void move(long delta){
		//if reached left side of screen and moving left, request logic
		if((dx < 0) && (x < 10)){
			game.updateLogic();
		}

		//if reached right side of screen and moving right, request logic
		if((dx > 0) && (x > game.getWidth() - 20)){
			game.updateLogic();
		}

		//proceed with move
		super.move(delta);
	}

	/*
	 * Update game logic related to aliens
	 */
	public void doLogic(){
		//swap over horizontal movement and move down screen a bit
		dx  = -dx;
		y += 10;

		//if reached bottom of screen, player dies
		if(y > game.getHeight() - 30){
			game.notifyDeath();
		}
	}

	/*
	 * Notification that this alien has collided with another entity
	 */
	public void collidedWith(Entity other) {
		//collisions with aliens handled elsewhere

	}

}