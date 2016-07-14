package com.alieninvaders;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/*
 * A resource manager for sprites in the game. 
 * 
 * <p>
 * [singleton]
 * <p>
 */
public class SpriteStore {
	//The single instance of this class
	private static SpriteStore single = new SpriteStore();
	
	/*
	 * Gets the single SpriteStore instance of this class.
	 * 
	 * @return The single instance of this class.
	 */
	public static SpriteStore get(){
		return single;
	}
	
	//The cached sprite map: ref -> spriteInstance
	private HashMap sprites = new HashMap();
	
	/*
	 * Get sprite from the store.
	 * 
	 * @param The ref to the image to use for the sprite.
	 * @return A sprite instance containing an accelerate image of the reference.
	 */
	public Sprite getSprite(String ref){
		//If sprite exists in cache, return existing version.
		if(sprites.get(ref) != null){
			return (Sprite)sprites.get(ref);
		}
		
		//Otherwise, grab sprite from resource loader.
		BufferedImage sourceImage = null;
		
		try{
			//ClassLoader.getReource() ensures we get sprite from appropiate place,
			//which helps with deploying the game with things like webstart. 
			//Can also do a file lookup here.
			URL url = this.getClass().getClassLoader().getResource(ref);
			
			if(url == null){
				fail("Can't find ref: " + ref);
			}
			
			//use ImageIO to read image in
			sourceImage = ImageIO.read(url);
		}catch(IOException e){
			fail("Failed to load: " + ref);
		}
		
		//create accelerated image of right size to store the sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
		
		//draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage, 0, 0, null);
		
		//create a new sprite, add to cache, and return it
		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);
		
		return sprite;
	}
	
	/*
	 * Utility method to handle resource loading failure.
	 * 
	 * @param message The message to display on failure
	 */
	public void fail(String message){
		//dump message and exit game
		System.err.println(message);
		System.exit(0);
	}

}