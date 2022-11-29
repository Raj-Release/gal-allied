/**
 * 
 */
package com.shaic.arch;






import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;



/**
 * 
 *
 * @author ntv.vijayar
 *
 
 * 
 *         Core class used to convert image to pdf. PDF created will have
 *         default margin of 10 pixels and page size of A4. Can be modified
 *         using the provided setters and getters.
 */
public class ImageToPDF {

	private float marginLeft = 10;
	private float marginTop = 10;
	private PDRectangle pageSize = PDPage.PAGE_SIZE_A4;

	/**
	 * Constructor registers all default converters available.
	 */
	public ImageToPDF() {
		registerDefaultConverters();
	}

	// Setters and Getters for all values
	public float getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public float getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	public PDRectangle getPageSize() {
		return pageSize;
	}

	/**
	 * Constants available in {@link PDPage}
	 * 
	 * @param pageSize
	 */
	public void setPageSize(PDRectangle pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Registers all default converters.
	 */
	private void registerDefaultConverters() {
		ImageToBufferedImageConverterPool.registerNewConverter(SHAConstants.JPEG,
				new PNGJPEGToBufferedImageConverter());
		ImageToBufferedImageConverterPool.registerNewConverter(SHAConstants.PNG,
				new PNGJPEGToBufferedImageConverter());
		
	}

	/**
	 * Converts the images provides to pdf and writes to the target path
	 * specified. Image array to be passed must be an instance of {@link Image}
	 * 
	 * @param targetPDFPath
	 * @param sourceImages
	 */
	public void convert(String targetPDFPath, CustomImage... sourceImages) {
		
		PDDocument pdDocument = new PDDocument();
		try {

			// Create a document object
			

			for (int i = 0; i < sourceImages.length; i++) {

				ArrayList<BufferedImage> soruceImgs = sourceImages[i]
						.getBufferedImages();

				for (BufferedImage soruceImg : soruceImgs) {
					// Create a blank page with proper margins.
					PDPage newPage = new PDPage(pageSize);

					// Add the new page to the document
					pdDocument.addPage(newPage);

					// Get access to the content stream of the page
					PDPageContentStream pageStream = new PDPageContentStream(
							pdDocument, newPage, false, true);

					/**
					 * Resize the image to fit the page.
					 * 
					 * At the time of writing this code, I could not find a
					 * direct way to fit the image to the pdf. I am taking the
					 * height and size of the image and reducing it relatively
					 * to fit the page.
					 * 
					 */

					// Read image width and height
					float imageWidth = soruceImg.getWidth();
					float imageHeight = soruceImg.getHeight();

					// Read page width and height. Ignoring the margins
					float pageWidth = newPage.findMediaBox().getWidth()
							- (marginLeft * 2);
					float pageHeight = newPage.findMediaBox().getHeight()
							- (marginTop);

					// Holders for new width and height
					float newImageWidth = 0;
					float newImageHeight = 0;

					float diffWidthRatio = 1;
					float diffHeightRatio = 1;

					if (imageWidth <= pageWidth && imageHeight <= pageHeight) {

						// If image fits into the page, leave it as is.
						newImageWidth = imageWidth;
						newImageHeight = imageHeight;
					} else if (imageWidth > pageWidth
							|| imageHeight > pageHeight) {

						// If image does not fit into the page, shrink its width
						// first
						if (imageWidth > pageWidth) {
							newImageWidth = pageWidth;
							diffWidthRatio = newImageWidth / imageWidth;
							newImageHeight = imageHeight * diffWidthRatio;
							diffHeightRatio = newImageHeight / imageHeight;
						}

						// After shrinking width, check if height is fitting in,
						// else shrink height as well.
						if (newImageHeight > pageHeight) {
							newImageHeight = pageWidth;
							diffHeightRatio = newImageHeight / newImageHeight;
							newImageWidth = newImageWidth * diffHeightRatio;
							diffWidthRatio = newImageWidth / imageWidth;

						}
					}

					// Convert the image into a new one
					BufferedImage newBuffImage = new BufferedImage(
							(int) Math.floor(newImageWidth),
							(int) Math.floor(newImageHeight),
							BufferedImage.TYPE_INT_RGB);

					// Apply scaling transformation to fit the image to page
					AffineTransform at = AffineTransform.getScaleInstance(
							diffWidthRatio, diffHeightRatio);
					Graphics2D g = newBuffImage.createGraphics();
					g.drawRenderedImage(soruceImg, at);

					// Create a PDJpeg object to add the image to the PDF page
					PDJpeg image = new PDJpeg(pdDocument, newBuffImage);

					// Add image to the page
					pageStream.drawImage(image, marginLeft, pageHeight
							- newImageHeight - marginTop);

					// Close the content stream
					pageStream.close();
				}

			}

			// Save the document
			pdDocument.save(targetPDFPath);

			// Close it finally
			//pdDocument.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (COSVisitorException e) {
			e.printStackTrace();
		}finally{
			try {
				pdDocument.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
