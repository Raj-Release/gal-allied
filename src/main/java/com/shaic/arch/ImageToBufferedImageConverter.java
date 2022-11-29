/**
 * 
 */
package com.shaic.arch;

/**
 * @author ntv.vijayar
 *
 */


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * 
 *      Interface to be implemented by any image to BufferedImage converters.
 */
public interface ImageToBufferedImageConverter {

	/**
	 * 
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 */
	public ArrayList<BufferedImage> convert(String sourceFile)
			throws IOException;
}

