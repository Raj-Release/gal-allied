/**
 * 
 */
package com.shaic.arch;

/**
 * @author ntv.vijayar
 *
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.util.ArrayList;




public class CustomImage extends Image{

	String imageType;
	String sourcePath;
	ArrayList<BufferedImage> bufferedImages;

	/*@SuppressWarnings("unused")
	private CustomImage() {

	}*/

	/**
	 * Adds a new image. The image type passed must have converter implemented
	 * and registered
	 * 
	 * @param sourcePath
	 * @param imageType
	 * @throws IOException
	 */
	public CustomImage(String sourcePath, String imageType) throws IOException {
		this.imageType = imageType;
		this.sourcePath = sourcePath;

		// Convert the image to buffered images depending on the image type
		 this.bufferedImages = ImageToBufferedImageConverterPool.getConverter(
				imageType).convert(this.sourcePath);
		 
	}

	/**
	 * Returns buffered images read from the image. Usually will be 1 for simple
	 * file formats
	 * 
	 * @return
	 */
	public ArrayList<BufferedImage> getBufferedImages() {
		return bufferedImages;
	}

	@Override
	public int getWidth(ImageObserver observer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight(ImageObserver observer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ImageProducer getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graphics getGraphics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(String name, ImageObserver observer) {
		// TODO Auto-generated method stub
		return null;
	}
}

