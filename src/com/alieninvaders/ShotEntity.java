package com.alieninvaders;

/*
 * Entity representing the shot fired by the ship
 */
public class ShotEntity extends Entity {
	//Vertical speed at which players shot moves
	private double moveSpeed = -300;
	//Game in which entity exists
	private Game game;
	//True if this shot has been used i.e. it hit something
	private boolean used = false;
	
	/*
	 * Create new shot from the player
	 * 
	 * @param game The game in which shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public ShotEntity(Game game, String sprite, int x, int y){
		super(sprite, x, y);
		
		this.game = game;
		dy = moveSpeed;
	}
	
	/*
	 * Move shot based on delta(time elapsed)
	 * 
	 * @param delta Time that has elapsed since last move
	 */
	public void move (long delta){
		//proceed with normal move
		super.move(delta);
		
		//if shoot off screen, remove ourself
		if(y < -100){
			game.removeEntity(this);
		}
	}
	
	/*
	 * Notification that this shot has collided with another entity.
	 * 
	 * @param other The other entity with which we've collided
	 */
	public void collidedWith(Entity other){
		//prevented double kills, if we've already hit something, don't collide
		if(used){
			return;
		}
		//if we hit an alien, kill it
		if(other instanceof AlienEntity){
			//remove affected entities
			game.removeEntity(this);
			game.removeEntity(other);
			
			//notify game that alien has been killed
			game.notifyAlienKilled();
			used = true;
		}
	}

}