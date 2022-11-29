/**
 * 
 */
package com.shaic.arch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;



/**
 * 
 *  @author ntv.vijayar
 * 
 *         Default converter provided to convert JPEG and PNG images to
 *         BufferedImage
 * 
 */
public class PNGJPEGToBufferedImageConverter implements
		ImageToBufferedImageConverter {

	@Override
	public ArrayList<BufferedImage> convert(String sourcePath)
			throws IOException {
		ArrayList<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();

		bufferedImages.add(ImageIO.read(new File(sourcePath)));
		return bufferedImages;
	}

}
