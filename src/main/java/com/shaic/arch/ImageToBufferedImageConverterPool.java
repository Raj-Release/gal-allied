/**
 * 
 */
package com.shaic.arch;





import java.util.HashMap;

/**
 * 
 * 
 *         Contains all the registered converters which are used to convert from
 *         Image to BufferedImage
 * 
 */
public class ImageToBufferedImageConverterPool {

	// Hashmap which collects all the converters
	static HashMap<String, ImageToBufferedImageConverter> converters = new HashMap<String, ImageToBufferedImageConverter>();

	//static HashMap<String, ImageToBufferedImageConverterPool> converters = new HashMap<String, ImageToBufferedImageConverterPool>();

	/**
	 * Register a new converter. The object passed in converter parameter should
	 * implement the interface {@link Image2BufferedImageConverter}
	 * 
	 * @param imageType
	 * @param converter
	 */
	public static void registerNewConverter(String imageType,
			ImageToBufferedImageConverter converter) {
		converters.put(imageType, converter);
	}

	/**
	 * Returns a registered converter for a particular image type
	 * 
	 * @param imageType
	 * @return
	 */
	public static ImageToBufferedImageConverter getConverter(String imageType) {
		return converters.get(imageType);
	}

}
