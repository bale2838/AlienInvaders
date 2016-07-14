package com.alieninvaders;

/*
 * An entity that represents one of the space invader aliens
 */
public class AlienEntity extends Entity {
	//The alien's horizontal speed
	private double moveSpeed = 75;

	//Game where entity exists
	Game game;

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
		//TODO Figure out why this is
		dx = -moveSpeed;
	}

	/*
	 * Request that alien move based on time elapsed
	 * 
	 * @param delta Time that elapsed since last frame
	 * (non-Javadoc)
	 * @see com.alieninvaders.Entity#collidedWith(com.alieninvaders.Entity)
	 */
	public void move(long delta){
		//if reached left side of screen and moving left, request logic
		if((dx < 0) && (x < 10)){
			game.updateLogic();
		}

		//if reached right side of screen and moving right, request logic
		if((dx > 0) && (x > 750)){
			game.updateLogic();
		}

		//proceed with move
		super.move(delta);
	}

	/*
	 * Update game logic related to aliens
	 * 
	 * (non-Javadoc)
	 * @see com.alieninvaders.Entity#collidedWith(com.alieninvaders.Entity)
	 */
	public void doLogic(){
		//swap over horizontal movement and move down screen a bit
		dx  = -dx;
		y += 10;

		//if reached bottom of screen, player dies
		if(y > 570){
			game.notifyDeath();
		}
	}

	/*
	 * Notification that this alien has collided with another entity
	 * 
	 * @param other The other entity
	 * (non-Javadoc)
	 * @see com.alieninvaders.Entity#collidedWith(com.alieninvaders.Entity)
	 */
	public void collidedWith(Entity other) {
		//collisions with aliens handled elsewhere

	}

}