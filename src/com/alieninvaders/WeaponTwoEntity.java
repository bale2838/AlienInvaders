package com.alieninvaders;

import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * Entity representing the second level of the shot fired by the ship
 */
public class WeaponTwoEntity extends Entity {
	//Vertical speed at which players shot moves
	private double moveSpeed = -200;
	//Game in which the entity exists
	private Game game;
	//True if this shot has been used i.e. it hit something
	private boolean used = false;
	//Current x location of second bullet
	protected double x2;
	//Current y location of second bullet
	protected double y2;
	//Rectangle used for the left bullet of this entity
	private Rectangle meLeft = new Rectangle();
	//Rectangle used for the right bullet of this entity
	private Rectangle meRight = new Rectangle();
	//Rectangle used for the other entity
	private Rectangle him  = new Rectangle();

	//Indicates the left shot hit the alien
	private boolean hitLeft;
	//Indicates the right shot hit the alien
	private boolean hitRight;

	/*
	 * Create new shot from the player
	 */
	public WeaponTwoEntity(Game game, String sprite, int x, int y) {
		super(sprite, x, y);

		//define second bullet location
		this.x2 = x + 20;
		this.y2 = y;
		
		hitLeft = false;
		hitRight = false;

		this.game = game;
		dy = moveSpeed;
	}

	/*
	 * Move shot based on delta(time elapsed)
	 */
	@Override
	public void move(long delta){
		//proceed with normal move
		if(!hitLeft){
			super.move(delta);
		}else{
			//move bullet offscreen since shot already hit target
			x = y = 5*(-game.getWidth()/game.getWidth());
		}
		//move second bullet
		if(!hitRight){
			x2 += (delta * dx)/1000;
			y2 += (delta * dy)/1000;
		}else{
			x2 = y2 = 5*(-game.getWidth()/game.getWidth());
		}

		if(y < 0){
			game.removeEntity(this);
		}
	}

	@Override
	public void draw(Graphics g){
		if(!hitLeft){
			sprite.draw(g, (int)x, (int)y);
		}
		if(!hitRight){
			sprite.draw(g, (int)x2, (int)y2);
		}
	}

	/*
	 * Check if this entity collided with the other
	 */
	public boolean collidesWith(Entity other){
		meLeft.setBounds((int)x, (int)y, sprite.getWidth(), sprite.getHeight());
		meRight.setBounds((int)x2, (int)y2, sprite.getWidth(), sprite.getHeight());

		him.setBounds((int)other.x, (int)other.y, other.sprite.getWidth(), other.sprite.getHeight());

		if(meLeft.intersects(him)){
			hitLeft = true;
			return hitLeft;
		}
		if(meRight.intersects(him)){
			hitRight = true;
			return hitRight;
		}
		return false;

	}

	/*
	 * Notification that shot has collided with another entity
	 */
	public void collidedWith(Entity other) {
		//if hit something, don't hit again
		//		if(!hitLeft || !hitRight){
		//			return;
		//		}

		//if either bullet still exists and hit an alien, kill it
		if((!hitLeft || !hitRight) && (other instanceof AlienEntity)){
			//remove affected entities
			//game.removeEntity(this);
			game.removeEntity(other);

			//notify game that alien has been killed
			game.notifyAlienKilled();
			//used = true;
		}else if(hitLeft && hitRight){
			game.removeEntity(this);
		}
	}
}
