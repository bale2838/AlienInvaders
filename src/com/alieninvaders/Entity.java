package com.alieninvaders;

import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * An Entity represents any element that appears in the game.
 * Responsible for resolving collisions and movement based on 
 * a set of properties defined in either by subclass or externally.
 */
public abstract class Entity {
	//Current x location of this entity
	protected double x;
	//Current y location of this entity
	protected double y;
	//Sprite that represents the entity
	protected Sprite sprite;
	//Current horizontal speed of the entity (pixels/second)
	protected double dx;
	//Current vertical speed of the entity (pixels/second)
	protected double dy;
	//Rectangle used for this entity during collision checking
	private Rectangle me = new Rectangle();
	//Rectangle used for the other entity during collision checking
	private Rectangle him = new Rectangle();

	/*
	 * Construct entity based on sprite image and given location.
	 */
	public Entity(String ref, int x, int y){
		this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}

	/*
	 * Request that this entity move itself based on a certain amount of time passing.
	 * 
	 * @param delta The amount of time that passed in milliseconds
	 */
	public void move(long delta){
		//update location of entity based on move speeds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}
	
	/*
	 * Set horizontal speed of this entity
	 * 
	 * @param dx The horizontal speed of this entity(pixels/second)
	 */
	public void setHorizontalMovement(double dx){
		this.dx = dx;
	}
	
	/*
	 * Set vertical speed of entity
	 * 
	 * @param dy The vertical speed of this entity(pixels/speed)
	 */
	public void setVerticalMovement(double dy){
		this.dy = dy;
	}
	
	/*
	 * Get horizontal speed of this entity
	 * 
	 * @return The horizontal speed of the entity(pixels/second)
	 */
	public double getHorizontalMovement(){
		return dx;
	}
	
	/*
	 * Get vertical speed of this entity
	 * 
	 * @return The vertical speed of this entity(pixels/second)
	 */
	public double getVerticalMovement(){
		return dy;
	}

	/*
	 * Draw this entity to the graphics context provided
	 * 
	 * @param g the graphics context on which to draw
	 */
	public void draw(Graphics g){
		sprite.draw(g, (int)x, (int)y);
	}
	
	/*
	 * Do logic associated with this entity. This method will be called periodically
	 * based on game events.
	 */
	public void doLogic(){
		
	}
	
	/*
	 * Get x location of this entity
	 * 
	 * @return The x location of this entity
	 */
	public int getX(){
		return (int) x;
	}
	
	/*
	 * Get y location of this entity
	 * 
	 * @return The y location of this entity
	 */
	public int getY(){
		return (int)y;
	}
	
	/*
	 * Check if this entity collided with another entity
	 * 
	 * @param other The other entity to check collisions against
	 * @return True if entities collided with each other
	 */
	public boolean collidesWith(Entity other){
		me.setBounds((int)x, (int)y, sprite.getWidth(), sprite.getHeight());
		him.setBounds((int)other.x, (int)other.y, other.sprite.getWidth(), other.sprite.getHeight());
		return me.intersects(him);
	}
	
	/*
	 * Notification that this entity collided with another 
	 * 
	 * @param other The other entity with which this entity collided with
	 */
	public abstract void collidedWith(Entity other);

}