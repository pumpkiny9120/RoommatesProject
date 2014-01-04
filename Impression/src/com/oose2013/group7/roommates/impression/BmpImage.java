package com.oose2013.group7.roommates.impression;

import android.graphics.Bitmap;

/**
 * The class is used to store an image and its position on the canvas.
 * @author lbowen5
 */

public class BmpImage {
	public Bitmap bmp;									//original image
	public int w, h;									//image dimension
	public int startx, starty;							//image top left corner
		
	/**
	 * Constructor.
	 * @param Source
	 */
	public BmpImage(Bitmap Source){
		bmp=Source;
		w=bmp.getWidth();
		h=bmp.getHeight();
	}
	
	/**
	 * Set the top left corner of the image.
	 * @param x
	 * @param y
	 */
	public void setStartPosition(int x, int y){
		startx=x;
		starty=y;
	}
}