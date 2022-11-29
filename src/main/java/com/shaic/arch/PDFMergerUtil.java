/**
 * 
 */
package com.shaic.arch;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.util.PDFMergerUtility;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;



 public class PDFMergerUtil {
	 
	 public static  PDFMergerUtil pdfUtil;
	 
	 
	 private PDFMergerUtil()
	 {
		 
	 }
	 
	 public static  PDFMergerUtil getInstance()
	 {
		 if(null == pdfUtil)
		 {
			 pdfUtil = new PDFMergerUtil();
		 }
		 return pdfUtil;
	 }
 
	public  void concatPDFWithPDFBOX( final List<InputStream> streamOfPDFFiles, final OutputStream outputStream) 
	{
		 
		try {
		 
			PDFMergerUtility mergePdf = new PDFMergerUtility();
			for (InputStream inputDoc : streamOfPDFFiles) {
			mergePdf.addSource(inputDoc);
			
			
			
		}
			mergePdf.setDestinationStream(outputStream);
			mergePdf.mergeDocuments();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
 
	public  File mergeDocuments(final List<byte[]> documentContents,Path tempDir,String folderName) 
	{
		FileOutputStream fos = null;
		//byte[] outputData = null;
		//String timeStamp = null;
		File file = null;
		try
		{
		List<InputStream> inputStreams = new ArrayList<InputStream>(
		documentContents.size());
		 
		for (byte[] content : documentContents) {
		inputStreams.add(new ByteArrayInputStream(content));
		}
		
	//	file = File.createTempFile("CASHLESS_DOCUMENT_"+String.valueOf(System.currentTimeMillis())+".pdf","");
		
		Path path = Files.createTempFile(tempDir, folderName+"_", ".pdf");
		file = path.toFile();
		System.out.println("---the file path---"+file.getAbsolutePath());
		
		/*timeStamp = String.valueOf(System.currentTimeMillis());
		String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" + File.separator + "mergeddocuments" + File.separator +
				 "CASHLESS_DOCUMENT"+"_"+timeStamp+ ".pdf";*/
		//File file = new File(tmpFile.getAbsolutePath());
		 fos = new FileOutputStream(file);
		//ByteArrayOutputStream mergedStream = new ByteArrayOutputStream();
		 
		//long startTime2 = System.currentTimeMillis();
		 
		//concatPDFWithPDFBOX(inputStreams, fos);
		doMerge(inputStreams, fos);
		 
		//long endTime2 = System.currentTimeMillis();
		 
		/*ItextDocumentMergerService.LOG.info(" Time to create PDF with PDF Box "
		+ (endTime2 - startTime2));*/
		 
		 //outputData = mergedStream.toByteArray();
		}
		catch(Exception e)
		{
			
		}
		 finally
		 {
			 if(null != fos)
			 {
				 try
        		  {
        			 fos.close();
        		  }
        		  catch(Exception e)
        		  {
        			  e.printStackTrace();
        		  }
			 }
		 }
		return file;
	}
	
	public static void doMerge(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException { /*Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        
        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, i);
                //add the page to the destination pdf
                cb.addTemplate(page, 0, 0);
            }
        }*/
        
		Document document = new Document();
        PdfCopy copy = new PdfSmartCopy(document, outputStream);
        document.open();
        
        for (InputStream in : list) {
            PdfReader reader = new PdfReader(in);
            copy.addDocument(reader);
            reader.close();
        }
        if(null != document){
        document.close();
        }
        if(null != outputStream){
        outputStream.close();
        }
     }

 
}