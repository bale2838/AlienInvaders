package com.alieninvaders;

/*
 * Entity that represents the player's ship.
 */
public class ShipEntity extends Entity {
	//The game in which the ShipEntity exists
	private Game game;

	/*
	 * Construct new entity to represent the ship
	 * 
	 * @param game The game in which ship exists
	 * @param ref The reference to the sprite to show for the ship
	 * @param x The initial x location of the player's ship
	 * @param y The initial y location of the player's ship
	 */
	public ShipEntity(Game game, String ref, int x, int y){
		super(ref, x, y);

		this.game = game;
	}

	/*
	 * Request that the ship move itself based on delta(elapsed time)
	 * 
	 * @param delta The time in ms that has elapsed since the last frame
	 * (non-Javadoc)
	 * @see com.alieninvaders.Entity#collidedWith(com.alieninvaders.Entity)
	 */

	public void move(long delta){
		//if moving left and reached left hand side of screen, don't move
		if((dx < 0) && (x < 10)){
			return;
		}

		//if we're moving right and reached right hand side of screen, don't move
		if((dx > 0) && (x > 750)){
			return;
		}

		super.move(delta);
	}

	/*
	 * Notification that player's ship collided with something
	 * 
	 * (non-Javadoc)
	 * @see com.alieninvaders.Entity#collidedWith(com.alieninvaders.Entity)
	 */
	public void collidedWith(Entity other) {
		//if it's an alien, notify game that player is dead
		if(other instanceof AlienEntity){
			game.notifyDeath();
		}

	}

}