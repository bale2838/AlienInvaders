package com.alieninvaders;

import java.awt.Graphics;
import java.awt.Image;


/*
 * A sprite to be displayed on the screen. 
 * A sprite contains no state information i.e. its just an image with no location.
 * This will allow the use of multiple sprites in alot of different places without
 * having to store multiple copies of the image.
 */
public class Sprite {
	//The image to be drawn
	private Image image;
	
	/*
	 * Create a new sprite based on the image.
	 * 
	 * @param image The image that is this sprite.
	 */
	public Sprite(Image image){
		this.image = image;
	}
	
	/*
	 * Get the width of the drawn sprite
	 * 
	 * @return The width in pixels of this sprite.
	 */
	public int getWidth(){
		return image.getWidth(null);
	}
	
	/*
	 * Get the height of the drawn sprite.
	 * 
	 * @return The height in pixels of the sprite.
	 */
	public int getHeight(){
		return image.getHeight(null);
	}
	
	/*
	 * Draw the sprite onto graphics context provided.
	 * 
	 * @param g The graphics context on which to draw the sprite
	 * @param x The x location at which to draw the sprite
	 * @param y The y location at which to draw the sprite
	 */
	public void draw(Graphics g, int x, int y){
		g.drawImage(image, x, y, null);
	}
	

}
