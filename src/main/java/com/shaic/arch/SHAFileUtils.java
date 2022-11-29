package com.shaic.arch;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.WeakHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.util.Log;
import org.json.JSONObject;

import com.bct.cuecent.dms.ejb.session.fileupload.FileUploaderStatelessBeanRemote;
import com.bct.cuecent.dms.ejb.session.view.FileViewStatelessBeanRemote;
import com.bct.cuecent.dms.ejb.util.ApplicationException;
import com.bct.cuecent.dms.ejb.util.ServiceConstants;
import com.bct.cuecent.dms.ejb.util.ServiceLocator;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.shaic.arch.utils.Props;
import com.shaic.claim.policy.search.ui.DmsUrlResponse;
import com.shaic.claim.policy.search.ui.DmsUrlResponseValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Notification;

public class SHAFileUtils {

	public static WeakHashMap sendFileToDMSServer(String strFileName, byte[] fileAsbyteArray) {
		String url = System.getProperty("jboss.server.data.dir") + File.separator + strFileName;
		String fileExtension = FilenameUtils.getExtension(url);
		File file = new File(url);
		HashMap statuMap = null;

		/*Object obj;
		try {
			obj = ServiceLocator.lookupBean(ServiceConstants.FILE_SPEC_VALIDATION_BEAN);
			System.out.println(obj);
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		String sdtr= ServiceConstants.FILE_SPEC_VALIDATION_BEAN;
		*/
		
		/*HashMap<String, Object> inParameters = new HashMap<String, Object>();
		inParameters.put("fileTokenId", StringUtils.EMPTY + SHAUtils.getTokenNumber());
		inParameters.put("applicationCode", Props.DMS_APP_ID);
		inParameters.put("templateId", "10");
		inParameters.put("fileExtension", fileExtension);
		inParameters.put("fieldname", "fileUploadShow");
		inParameters.put("fileName", SHAUtils.getOnlyStrings(strFileName));
		inParameters.put("fileSize", file.length());
		inParameters.put("filebytesArray", fileAsbyteArray);
		Context ctx = getContext();
		FileUploaderStatelessBeanRemote bean;
		try {
			bean = (FileUploaderStatelessBeanRemote) ctx.lookup(ServiceConstants.FILE_UPLOAD_BEAN);
			statuMap = bean.fileUpload(inParameters);
			System.out.println("---Status Map---"+statuMap);
			inParameters = null;
		} catch (NamingException ne) {
			ne.printStackTrace();
		} catch (ApplicationException ae) {
			ae.printStackTrace();
			statuMap = new HashMap();
			Notification.show("Error", ae.getMessage(), Notification.Type.ERROR_MESSAGE);
			statuMap.put("status", false);
			statuMap.put("message", ae.getMessage());
		} catch (Exception exception) {
			exception.printStackTrace();
			statuMap = new HashMap();
			Notification.show("Error", exception.getMessage(), Notification.Type.ERROR_MESSAGE);
			statuMap.put("status", false);
			statuMap.put("message", exception.getMessage());
		}*/
		
		WeakHashMap uploadStatus = sendFilesToDMS(
				strFileName, file);
		//for clearing heap
		file = null;
		
		return uploadStatus;
	}
	

	
	public static File downloadFileForCombinedView(String fileName,final String fileURL,Path tempDir)
	{
		final File tmpFile ;
		final Path path;
		try
		{
			//		tmpFile = File.createTempFile(fileName,String.valueOf(System.currentTimeMillis()));
			//tmpFile = File.createTempFile(fileName,"");
			//tmpFile = File.createTempFile(fileName, ".pdf");
			path = Files.createTempFile(tempDir, fileName, ".pdf");
			tmpFile = path.toFile();
			/*String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" +File.separator
					+ fileName; */
			if(null != tmpFile)
			{
				
			   StreamResource.StreamSource source = new StreamResource.StreamSource() {
                   public InputStream getStream() {
                      
                   	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   	
                   	InputStream is = null;
                   	FileOutputStream fos = null;
                   	
                   	URL u = null;
                   	URLConnection urlConnection = null;
                   	try {
						u =  new URL(fileURL);
						urlConnection =  u.openConnection();
						is = urlConnection.getInputStream();
						//is = u.openStream();
					  
						//byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
						byte[]  byteChunk = null;
						if(urlConnection.getContentLength() > 25000){
							byteChunk = new byte[25000];
						} else {
							byteChunk = new byte[urlConnection.getContentLength()];
						}
						
						//byte[] byteChunk = new byte[urlConnection.getContentLength()];
						int n;
					
						while ( (n = is.read(byteChunk)) > 0 ) {
							baos.write(byteChunk, 0, n);
						}
						fos = new FileOutputStream(tmpFile);
						fos.write(baos.toByteArray());
						byteChunk = null;
                   	}
                   	catch (IOException e) {
                   	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
                   	  e.printStackTrace ();
                   	  // Perform any other exception handling that's appropriate.
                   	}
                   	finally {
                   	  if (is != null) {
	                   		  try
	                   		  {
	                   			  is.close();
	                   		  }
	                   		  catch(Exception e)
	                   		  {
	                   			  e.printStackTrace();
	                   		  }
                   		  }
                   	if (null != fos) {
	                 		  try
	                 		  {
	                 			 fos.close();
	                 		  }
	                 		  catch(Exception e)
	                 		  {
	                 			  e.printStackTrace();
	                 		  }
                 		  }
                   	if (null != urlConnection) {
               		  try
               		  {
               			urlConnection.getInputStream().close();
               		  }
               		  catch(Exception e)
               		  {
               			  e.printStackTrace();
               		  }
           		  }
                   	}
                   	return new ByteArrayInputStream(baos.toByteArray());
                   }
           };
           
           StreamResource r = new StreamResource(source, fileName);
           r.getStream();
           r.setMIMEType("application/pdf");
           r.setStreamSource(source);
           r.setFilename(fileName);
           SHAUtils.closeStreamResource(source);
			}
			return tmpFile;
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	public static File downloadImageToTemp(String fileName, final String fileURL, Path tempDir)
	{

		final File tmpFile ;
		final Path path;
		try
		{
			//		tmpFile = File.createTempFile(fileName,String.valueOf(System.currentTimeMillis()));
			//tmpFile = File.createTempFile(fileName,"");
			//tmpFile = File.createTempFile(fileName, ".pdf");
			path = Files.createTempFile(tempDir, fileName, ".jpg");
			tmpFile = path.toFile();
			/*String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" +File.separator
					+ fileName; */
			if(null != tmpFile)
			{
				
			   StreamResource.StreamSource source = new StreamResource.StreamSource() {
                   public InputStream getStream() {
                      
                   	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   	
                   	InputStream is = null;
                   	FileOutputStream fos = null;
                   	
                   	URL u = null;
                	URLConnection urlConnection = null;
                   	try {
//                   		u =  new URL(fileURL);
//                   	  is = u.openStream();
                   		u =  new URL(fileURL);
						urlConnection =  u.openConnection();
						is = urlConnection.getInputStream();
                   	  
					//byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
						byte[]  byteChunk = null;
						if(urlConnection.getContentLength() > 25000){
							byteChunk = new byte[25000];
						} else {
							byteChunk = new byte[urlConnection.getContentLength()];
						}
                   	  int n;

                   	  while ( (n = is.read(byteChunk)) > 0 ) {
                   	    baos.write(byteChunk, 0, n);
                   	  }
                   	 fos = new FileOutputStream(tmpFile);
                   	fos.write(baos.toByteArray());
                   	byteChunk = null;
                   	}
                   	catch (IOException e) {
                   	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
                   	  e.printStackTrace ();
                   	  // Perform any other exception handling that's appropriate.
                   	}
                   	finally {
                   	  if (is != null) {
	                   		  try
	                   		  {
	                   			  is.close();
	                   		  }
	                   		  catch(Exception e)
	                   		  {
	                   			  e.printStackTrace();
	                   		  }
                   		  }
                   	if (null != fos) {
	                 		  try
	                 		  {
	                 			 fos.close();
	                 		  }
	                 		  catch(Exception e)
	                 		  {
	                 			  e.printStackTrace();
	                 		  }
                 		  }
		                   	if (null != urlConnection) {
		               		  try
		               		  {
		               			urlConnection.getInputStream().close();
		               		  }
		               		  catch(Exception e)
		               		  {
		               			  e.printStackTrace();
		               		  }
		           		  }
                   	}
                   	return new ByteArrayInputStream(baos.toByteArray());
                   }
           };
           
           StreamResource r = new StreamResource(source, fileName);
           r.getStream();
           r.setMIMEType("image/jpeg");
           r.setStreamSource(source);
           r.setFilename(fileName);
           SHAUtils.closeStreamResource(source);
			}
			return tmpFile;
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
	
	}
	
	
	
	/*public static File convertImageToPDF(String fileName,final String fileURL,Path tempDir)
	{
		final File tmpFile ;
		final Path path;
		final CustomImage image;
		try
		{
			//		tmpFile = File.createTempFile(fileName,String.valueOf(System.currentTimeMillis()));
			//tmpFile = File.createTempFile(fileName,"");
			//tmpFile = File.createTempFile(fileName, ".pdf");
			  URL url = new URL(fileURL);
			  image = ImageIO.read(url);
			path = Files.createTempFile(tempDir, fileName, ".pdf");
			tmpFile = path.toFile();
			ImageToPDF imageToPdf = new ImageToPDF();
			String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" +File.separator
					+ fileName; 
			if(null != tmpFile)
			{
				imageToPdf.convert(tmpFile, image);
			}
			return tmpFile;
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
	}*/
	
/*	public static List<byte[]> getByteArrayFromFiles(List<String> fileNameList)
	{
		List<byte[]> byteArrayList = new ArrayList<byte[]>();
		try
		{
			if(null != fileNameList && !fileNameList.isEmpty())
			{
				for (String fileName : fileNameList) {
					File file = new File (System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" +File.separator
							+ fileName);
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
					
					byteArrayList.add(fileAsbyteArray);
					
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return byteArrayList;
	}
	*/
//	public static List<byte[]> getByteArrayFromFiles(List<File> fileList)
//	{
//		List<byte[]> byteArrayList = new ArrayList<byte[]>();
//		try
//		{
//			if(null != fileList && !fileList.isEmpty())
//			{
//				for (File file : fileList) {
//					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
//					byteArrayList.add(fileAsbyteArray);
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		
//		return byteArrayList;
//	}

	
	public static List<byte[]> getByteArrayFromFiles(List<File> fileList)
	{
		List<byte[]> byteArrayList = new ArrayList<byte[]>();
		PDDocument pdfDocument = null;
		try
		{
			if(null != fileList && !fileList.isEmpty())
			{
				for (File file : fileList) {
				    
				    try
				    {
				    	//The encrypted/password protected PDF document has not been send for merge
				        //pdfDocument = PDDocument.load(file);
				        pdfDocument = PDDocument.loadNonSeq(file, null);
				        if(pdfDocument != null && !pdfDocument.isEncrypted()){
							byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
							byteArrayList.add(fileAsbyteArray);
				        }
				    }
				    catch(Exception e){
				    	try{
				    		byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
							byteArrayList.add(fileAsbyteArray);
							fileAsbyteArray = null;
				    	}catch(Exception e1){
				    		e1.printStackTrace();
				    	}
				    	
				        e.printStackTrace();
				    }
				    /*if(pdfDocument != null){
				    	pdfDocument.close();
				    }*/
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			  if(pdfDocument != null){
			    	try {
						pdfDocument.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
		}
		
		
		return byteArrayList;
	}
	
	public static void main (String args[])
	{
		String url = "/app:company_home/cm:InboundDMS/cm:_x0032_015/cm:_x0031_2/cm:_x0032_8/cm:_x0032_0";
		String fileName = "_e4438f58-3333-4831-92ff-e7cbe56f8301Kanchana - Final Bill.pdf";
		Long fileSize = 2277867l;
		Map docMap = uploadDocumentByUrlWebService(url, fileName, fileSize);
		if(null != docMap)
		{
			String tokenId = (String)docMap.get("fileKey");
			System.out.println("----token id"+tokenId);
		}
	}

	
	public static void getDocumentTokenByUrl(List<HashMap> mapList)
	{
		List<String> tokenList = new ArrayList();
		if(null != mapList && !mapList.isEmpty())
		{
			for (HashMap hashMap : mapList) {
				String url = (String) hashMap.get("url");
				String actualFileName = (String) hashMap.get("fileName");
				Long fileSize = (Long)hashMap.get("fileSize");
				HashMap docMap = getDocumentTokenByUrl(url,actualFileName,fileSize);
				if(null != docMap)
				{
					String tokenId = (String)docMap.get("fileKey");
					System.out.println("----token id"+tokenId);
					tokenList.add(tokenId);
					System.out.println("----token idList------"+tokenList);
				}
				
			}
		}
	}
	
	public static HashMap getDocumentTokenByUrl(String url,String actualFileName,Long fileSize)
	{
		Context ctx = getContext();
		FileUploaderStatelessBeanRemote bean;
		HashMap statusMap = null;
		try {
			
			HashMap<String, Object> inParameters = new HashMap<String, Object>();
			inParameters.put("fileTokenId", "" + SHAUtils.getTokenNumber());
			inParameters.put("applicationCode", Props.DMS_APP_ID);
			inParameters.put("templateId", "10");
			inParameters.put("dmsurl", url);
			inParameters.put("fileName", actualFileName);
			if(null != fileSize)
			inParameters.put("fileSize", String.valueOf(fileSize));
			
			bean = (FileUploaderStatelessBeanRemote) ctx
					.lookup(ServiceConstants.FILE_UPLOAD_BEAN);
			statusMap = bean.getFileKey(inParameters);
	
			//statusMap = bean.fileUpload(Props.DMS_APP_ID, SHAUtils.getTokenNumber(), "10", url);
		//	statusMap = 
			//bean.
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			statusMap = new HashMap();
			e.printStackTrace();
			Notification.show("Error", e.getMessage(),
					Notification.Type.ERROR_MESSAGE);
			statusMap.put("status", false);
			statusMap.put("message", e.getMessage());
			e.printStackTrace();
		}
		return statusMap;
	}

	public static String viewFileByToken( String fileKey) {

		/*try {
			FileViewStatelessBeanRemote fileViewBean = (FileViewStatelessBeanRemote) ServiceLocator
					.lookupBean(ServiceConstants.FILE_VIEW_BEAN);

			// String fileKey = "1058";
			// String appId = "HM001";
			// HashMap outstatus2 = fileViewBean.fileAsbyteArray(appId,
			// fileKey);
			String fileUrl = fileViewBean.fileView(Props.DMS_APP_ID, fileKey);
			System.out.println("fileUrl  from The DMS Service is :   "+ fileUrl);
			return fileUrl;
	
		} catch (ApplicationException e) {
			e.printStackTrace();
			System.out.println(e);	

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		
		return null;*/
		
		String viewFileByTokenService = viewFileByTokenService(fileKey);
		return viewFileByTokenService;

	}
	
/*	public static void main (String args[])
	{
		String url = SHAFileUtils.viewFileByToken("7902");
		System.out.println("-=---url----"+url);
	}
*/
	public static Context getContext() {
		Properties properties = new Properties();
		// =false
		properties
				.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED",
						"false");
		properties.put("remote.connections", "default");
		// properties.put("remote.connection.default.host","127.0.0.1");
		properties.put("remote.connection.default.host", "192.168.1.68");
		properties.put("remote.connection.default.port", "4447");
		properties
				.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS",
						"false");
		properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		properties.put(Context.SECURITY_PRINCIPAL, "sadmin");
		properties.put(Context.SECURITY_CREDENTIALS, "admin123");

		// properties.put("java.naming.factory.initial",
		// "org.jboss.naming.remote.client.InitialContextFactory");
		// properties.put("java.naming.provider.url",
		// "remote://192.168.1.68:4447");
		// properties.put("java.naming.factory.url.pkgs",
		// "org.jboss.ejb.client.naming");

		Context ctx = null;
		try {
			ctx = new InitialContext(properties);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ctx;
	}

	/*public static file convertImagesToPDF(String fileName)
	{
		 Document convertJpgToPdf=new Document();
		    //Create PdfWriter for Document to hold physical file
		    PdfWriter.getInstance(convertJpgToPdf, new FileOutputStream("c:\\java\\ConvertImagetoPDF.pdf"));
		    convertJpgToPdf.open();
		    //Get the input image to Convert to PDF
		    Image convertJpg=Image.getInstance("c:\\java\\test.jpg");
		    //Add image to Document
		    convertJpgToPdf.add(convertJpg);
		    //Close Document
		    convertJpgToPdf.close(); 
	}*/

	public static File convertImageToPDF(String fileName)
	{
		//Document document = new Document();
		//Document document = new Document(new Rectangle(15000, 10000));
		//Document document = new Document(new Rectangle(4000, 3000));
		
		float marginLeft = 0f, marginRight = 50f, marginTop = -82f, marginBottom = -0f;
		Document document = new Document();
		float width = document.getPageSize().getWidth()- (marginLeft + marginRight);
		float height = document.getPageSize().getHeight()- (marginBottom + marginTop);
		String url = System.getProperty("jboss.server.data.dir") + File.separator + fileName;
		File file = null;
		String strFileName = null;
		
		/*if(null != fileName && fileName.endsWith(".jpg"))
		{
			fileNameArr = fileName.split(".jpg");
		}
		else if (null != fileName && fileName.endsWith(".jpeg"))
		{
			fileNameArr = fileName.split(".jpeg");
		}
		else if(null != fileName && fileName.endsWith(".JPG"))
		{
			fileNameArr = fileName.split(".JPG");
		}
		else if (null != fileName && fileName.endsWith(".JPEG"))
		{
			fileNameArr = fileName.split(".JPEG");
		}*/
		
		if(null != fileName && (fileName.toLowerCase().endsWith("jpg") || fileName.toLowerCase().endsWith("jpeg"))){
			strFileName  = fileName.substring(0, fileName.lastIndexOf("."));
		}
		
		if(null != strFileName)
		{
			//String strFileName =  fileNameArr[0];
			// .gif and .jpg are ok too!
		    String output =  System.getProperty("jboss.server.data.dir") + File.separator + strFileName +".pdf";
		    FileOutputStream fos = null;
		    try {
		      fos = new FileOutputStream(output);
		      PdfWriter writer = PdfWriter.getInstance(document, fos);
		      writer.open();
		      document.open();
		      Image img = Image.getInstance(url);
		      /*img.setScaleToFitLineWhenOverflow(true);
		      img.setAbsolutePosition((PageSize.POSTCARD.getWidth() - img.getScaledWidth()) / 2, (PageSize.POSTCARD.getHeight() - img.getScaledHeight()) / 2);
		      img.scalePercent(1000);*/
		      img.scaleToFit(width, height);
		      document.add(img);
		      document.close();
		      writer.close();
		      String fileUrl = System.getProperty("jboss.server.data.dir") + File.separator + strFileName +".pdf";
		      file = new File(fileUrl);
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally{
		    	if(null != fos){
		    		try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    }
	    }

		return file;
	}
	
	
/*	public static File convertImageToPDF(String fileName)
	{
		//Document document = new Document();
		//  Document document = new Document(new Rectangle(15000, 10000));
		  Document document = new Document(new Rectangle(4000, 3000));
		String url = System.getProperty("jboss.server.data.dir") + "/"
				+ fileName;
		String fileNameArr[] = null;
		byte[] fileAsbyteArray = null;
		File file = null;
		if(null != fileName && fileName.endsWith(".jpg"))
		{
			fileNameArr = fileName.split(".jpg");
		}
		else if (null != fileName && fileName.endsWith(".jpeg"))
		{
			fileNameArr = fileName.split(".jpeg");
		}
		
		if(null != fileNameArr)
		{
			String strFileName =  fileNameArr[0];
			
		     // .gif and .jpg are ok too!
		    String output =  System.getProperty("jboss.server.data.dir") + "/" + strFileName+".pdf";
		    try {
		      FileOutputStream fos = new FileOutputStream(output);
		      PdfWriter writer = PdfWriter.getInstance(document, fos);
		      writer.open();
		      document.open();
		      Image img = Image.getInstance(url);
		      //img.setScaleToFitLineWhenOverflow(true);
		      img.setAbsolutePosition(
		              (PageSize.POSTCARD.getWidth() - img.getScaledWidth()) / 2,
		              (PageSize.POSTCARD.getHeight() - img.getScaledHeight()) / 2);
		      //img.scalePercent(1000);
		      document.add(img);
		      document.close();
		      writer.close();
		      String fileUrl = System.getProperty("jboss.server.data.dir") + "/"
						+ strFileName +".pdf";
		       file = new File(fileUrl);
		     
		    }
		    catch (Exception e) {
		        e.printStackTrace();
		      }
	    }
		return file;
	}*/


	// public static FileOutputStream byeArrayToFile(String filename) {
	//
	// String strFilePath = "Your path";
	// try {
	// FileOutputStream fos = new FileOutputStream(strFilePath);
	//
	// fos.write(strContent.getBytes());
	// fos.close();
	// return fos;
	// } catch (FileNotFoundException ex) {
	// System.out.println("FileNotFoundException : " + ex);
	// } catch (IOException ioe) {
	// System.out.println("IOException : " + ioe);
	// }
	// }
	
	public static Path createTempDirectory(String batchId)
	{
		Path tempDir = null;
		try
		{
			tempDir = Files.createTempDirectory(batchId);
			 File file = tempDir.toFile();
			 System.out.println("-----the file path----"+file.getAbsolutePath());
		}
		catch(Exception e)
		{
			System.out.println("-----invalid name for dir------"+batchId);
			e.printStackTrace();
		}
		return tempDir;
	}

	
	public static File mergeDocuments(List<File> fileList, Path path,String batchId)
 {
		PDFMergerUtil pdfMergerUtil = PDFMergerUtil.getInstance();
		List<byte[]> byteArrayStream = SHAFileUtils
				.getByteArrayFromFiles(fileList);

		if (batchId.equalsIgnoreCase("OMPPaymentLetter")) {
			String folderName = batchId;
			if (null != byteArrayStream && null != pdfMergerUtil) {
				return (pdfMergerUtil.mergeDocuments(byteArrayStream, path,
						folderName));
			}
		} else {
			String str[] = batchId.split("-");
			int iSize = str.length;
			String folderName = str[iSize - 1];

			if (null != byteArrayStream && null != pdfMergerUtil) {
				return (pdfMergerUtil.mergeDocuments(byteArrayStream, path,
						folderName));
			}
		}
		return null;
	}
	
	@SuppressWarnings("static-access")
	public static WeakHashMap sendFilesToDMS(String fileName, File file) {
		String tokenId = "";
		Random randomGenerator = new Random();
		int randomId = randomGenerator.nextInt(10000);
		StringBuffer tokenBfr = new StringBuffer();
		Date date  = new Date();
		BPMClientContext clientContext = new BPMClientContext();
		
		tokenBfr.append(randomId).append(date.getTime());
		tokenId = String.valueOf(tokenBfr);
		String httpURL = clientContext.getDmsWebserviceUrl() + "upload"+"/"+tokenId+"/"
				+SHAConstants.ALFRESCO_APP_ID+"/"+SHAConstants.ALFRESCO_TEMP_ID;
		WeakHashMap uploadMultipartFile = uploadMultipartFile(httpURL,file);
		//for clearing heap
		randomGenerator = null;
		tokenBfr = null;
		return uploadMultipartFile;
	}
	
	public static WeakHashMap uploadMultipartFile(String url,File fileToUpload) {
		Result result = null;
		WeakHashMap statusMap = null;
		try {

			ClientConfig clientConfig = new DefaultClientConfig();
			//clientConfig.getClasses().add(JacksonJsonProvider.class);
			
			Client client = Client.create(clientConfig);
			//String strPremiaFlag = url;
			WebResource webResource = client.resource(url);
			 

			webResource.accept(MediaType.MULTIPART_FORM_DATA);
			
			//
			
			FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("uploadFile",
	                fileToUpload,
	                MediaType.APPLICATION_OCTET_STREAM_TYPE);
	        fileDataBodyPart.setContentDisposition(
	                FormDataContentDisposition.name("uploadFile")
	                        .fileName(SHAUtils.getOnlyStrings(fileToUpload.getName())).build());
		    
		 
	        final MultiPart multiPart = new FormDataMultiPart()
	                .field("description", " ", MediaType.TEXT_PLAIN_TYPE)
	                //.field("characterProfile", jsonToSend, MediaType.APPLICATION_JSON_TYPE)
	                .field("filename", fileToUpload.getName(), MediaType.TEXT_PLAIN_TYPE)
	                .bodyPart(fileDataBodyPart);
	        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		    
		    ClientResponse response = webResource
	                .type("multipart/form-data").post(ClientResponse.class,
	                        multiPart);
		   
		    
		    String jsonInString = getStringFromInputStream(response.getEntityInputStream());
		    ObjectMapper mapper = new ObjectMapper();
		    UploadResponse responseEntity = mapper.readValue(jsonInString, UploadResponse.class);
		    result = responseEntity.getResult();
		    if (response.getStatus() != 200) {
		    	statusMap = new WeakHashMap();
				Notification.show("Error",result.getMessage() , Notification.Type.ERROR_MESSAGE);
				statusMap.put("status", false);
				statusMap.put("message", result.getMessage());
			}else{
				statusMap = new WeakHashMap();
				statusMap.put("status", true);
				statusMap.put("fileKey", result.getKey());
				statusMap.put("message", result.getMessage());
			}
		    System.out.println("INFO >>> Response from API was: " + result.getMessage());
		    System.out.println("INFO >>> Response from API was: " + result.getKey());
		    System.out.println("INFO >>> Response from API was: " + response.getStatus());
		    System.out.println("INFO >>> Response from API was: " + result);
	        client.destroy();
	        //for clearing heap
	        clientConfig = null;
	        client = null;
	        webResource = null;
	        fileDataBodyPart = null;
	        response = null;
	        mapper = null;
	        responseEntity = null;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return statusMap;
	}	
	
	private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        final StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
	
	public static String viewFileByTokenService( String fileKey) {

		try {
			/*FileViewStatelessBeanRemote fileViewBean = (FileViewStatelessBeanRemote) ServiceLocator
					.lookupBean(ServiceConstants.FILE_VIEW_BEAN);

			// String fileKey = "1058";
			// String appId = "HM001";
			// HashMap outstatus2 = fileViewBean.fileAsbyteArray(appId,
			// fileKey);
			String fileUrl = fileViewBean.fileView(Props.DMS_APP_ID, fileKey);
			System.out.println("fileUrl  from The DMS Service is :   "+ fileUrl);
			//return fileUrl;
			*/
			String code = "";
			String key = "";
			String message = "";
			String responseJsonInString = "";
			String reqString = "fileKey="+fileKey+"&appId="+SHAConstants.ALFRESCO_APP_ID;
			if(fileKey != null) {
				responseJsonInString = downloadApi(reqString);
				if(responseJsonInString != null) {
					JSONObject jsonParent = new JSONObject(responseJsonInString);
					JSONObject json = jsonParent.getJSONObject("result");
					code = json.getString("code");
					if(code.equalsIgnoreCase("ALFDW002")) {
						key = json.getString("key");
						System.out.println("INFO >>> Download Response from API: " + fileKey);
						return key;
					}
				}
			}
	
		} 
		catch(Exception ee){
			ee.printStackTrace();
		}
		
		return null;

	}
	
		/**
		 * Call Alfresco Download API
		 * 
		 * @param requestJsonInString
		 * @return response JSON in string
		 */
		public static String downloadApi(String requestJsonInString) {
			String responseJsonInString = new String();
			BPMClientContext clientContext = new BPMClientContext();
			try {
				URL url = new URL(clientContext.getDmsWebserviceUrl() + "download");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod(HttpMethod.POST);
				conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);

				OutputStream os = conn.getOutputStream();
				os.write(requestJsonInString.getBytes());
				os.flush();

				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));			
				for (String line; (line = br.readLine()) != null; responseJsonInString += line);

				conn.disconnect();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseJsonInString;
		}
		
		@SuppressWarnings("unchecked")
		public  static HashMap uploadDocumentByUrlWebService(String url,String actualFileName,Long fileSize) {
			
			DmsUrlResponseValue jsonElement = new DmsUrlResponseValue();
			BPMClientContext clientContext = new BPMClientContext();
			HashMap statusMap = null;
			
			try {
				ClientConfig config = new DefaultClientConfig();
				Client client = Client.create(config);
				WebResource webResource = client.resource(clientContext.getDmsWebserviceUrl()+ "getFileKey");
				webResource.accept(MediaType.APPLICATION_FORM_URLENCODED);
				webResource.method(HttpMethod.POST);

				MultivaluedMapImpl formParameters = new MultivaluedMapImpl();
				formParameters.putSingle("tokenId", "" + SHAUtils.getTokenNumber());//
		        formParameters.putSingle("appId", SHAConstants.ALFRESCO_APP_ID);// 
		        formParameters.putSingle("tempId", SHAConstants.ALFRESCO_TEMP_ID);
		        formParameters.putSingle("dmsurl", url);
		        formParameters.putSingle("fileName", actualFileName);
		        formParameters.putSingle("fileSize", fileSize);//  - reference file key-70204
				ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formParameters);
				//responseValue = response.getEntity(String.class);
				Gson gson = new Gson();
				DmsUrlResponse fromJson = gson.fromJson(response.getEntity(String.class),DmsUrlResponse.class);
				jsonElement = fromJson.getResult();
				
				System.out.println("File Token >>>>>>>>>>>>>> "+ jsonElement.getKey());
				System.out.println("Status Code >>>>>>>>>>>>>> "+ jsonElement.getCode());
				System.out.println("Message >>>>>>>>>>>>>> "+ jsonElement.getMessage());
				
				statusMap = new HashMap();
				statusMap.put("fileKey", jsonElement.getKey());
				statusMap.put("status", true);
				statusMap.put("message", jsonElement.getMessage());
				

			} catch(Exception e) {
				statusMap = new HashMap();
				e.printStackTrace();
				Notification.show("Error", e.getMessage(),
						Notification.Type.ERROR_MESSAGE);
				statusMap.put("status", false);
				statusMap.put("message", e.getMessage());
				e.printStackTrace();
			}
		
			return statusMap;
		}
		
		public static WeakHashMap sendGeneratedFileToDMS(String strFileName, byte[] fileAsbyteArray,File file) {
			
			WeakHashMap uploadStatus = sendFilesToDMS(
					strFileName, file);
			
			return uploadStatus;
		}
	
		/* Please don't use the writeAndSendFileToDMS method for any other document upload logic, the below method is customized 
		only for the need of Hospital Portal. */
		@SuppressWarnings("rawtypes")
		public static WeakHashMap writeAndSendFileToDMS(String strFileName, byte[] fileAsbyteArray){
			WeakHashMap uploadStatus = null;
			String finalFileName = "";
			boolean isImageFileUploading = false;

			if(strFileName.toLowerCase().endsWith("jpg") || strFileName.toLowerCase().endsWith("jpeg")){
				isImageFileUploading = true;
			}else{
				finalFileName = strFileName;
			}

			try{
				if(isImageFileUploading){
					String tempUrl = System.getProperty("jboss.server.data.dir") + File.separator + strFileName;
					File uDoc =  new File(tempUrl);
					FileUtils.writeByteArrayToFile(uDoc, fileAsbyteArray);

					// converting Image to pdf file ....
					File convertedFile  = SHAFileUtils.convertImageToPDF(strFileName);
					finalFileName = convertedFile.getName();
					System.out.println("Converted File Name"+finalFileName);
					// Emptying the array to load the converted pdf file bytes.
					fileAsbyteArray = null;
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
				}
				System.out.println("Final File Name : "+finalFileName);
				String url = System.getProperty("jboss.server.data.dir") + File.separator + finalFileName;
				System.out.println("Hospital Document Upload URL"+url);
				File uDoc =  new File(url);
				FileUtils.writeByteArrayToFile(uDoc, fileAsbyteArray);
				uploadStatus = sendFilesToDMS(strFileName, uDoc);
				System.out.println("File Upload Status : "+uploadStatus);
			}catch(Exception exp){
				System.out.println("Exception Occurred while Uploading Document"+exp.getMessage());
			}
			return uploadStatus;
		}
		
		/* Please don't use the writeAndSendFileToDMS method for any other document upload logic, the below method is customized 
		only for the need of Hospital Portal. */
		@SuppressWarnings("rawtypes")
		public static WeakHashMap writeAndSendFileToDMSFVR(String strFileName, byte[] fileAsbyteArray){
			WeakHashMap uploadStatus = null;
			String finalFileName = "";
			boolean isImageFileUploading = false;
			
			

			if(strFileName.toLowerCase().endsWith("jpg") || strFileName.toLowerCase().endsWith("jpeg")){
				isImageFileUploading = true;
			}else{
				finalFileName = strFileName;
			}

			try{
				if(isImageFileUploading){
					String tempUrl = System.getProperty("jboss.server.data.dir") + File.separator + strFileName;
					File uDoc =  new File(tempUrl);
					FileUtils.writeByteArrayToFile(uDoc, fileAsbyteArray);

					// converting Image to pdf file ....
					File convertedFile  = SHAFileUtils.convertImageToPDF(strFileName);
					finalFileName = convertedFile.getName();
					System.out.println("Converted File Name"+finalFileName);
					// Emptying the array to load the converted pdf file bytes.
					fileAsbyteArray = null;
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
				}
				System.out.println("Final File Name : "+finalFileName);
				String url = System.getProperty("jboss.server.data.dir") + File.separator + finalFileName;
				System.out.println("FVR Document Upload URL"+url);
				File uDoc =  new File(url);
				FileUtils.writeByteArrayToFile(uDoc, fileAsbyteArray);
				uploadStatus = sendFilesToDMS(strFileName, uDoc);
			}catch(Exception exp){
				System.out.println("Exception Occurred while Uploading Document"+exp.getMessage());
			}
			return uploadStatus;
		}
		
		public static void writeStringToFile(String argFileName, String argStrFileContent){
			try{
				argFileName = argFileName.substring(0, argFileName.lastIndexOf(".")-1);
				argFileName = argFileName+".txt";
				File dir = new File(System.getProperty("jboss.server.data.dir")+File.separator+"HP_INPUT_DOCS");
				if(!dir.exists()){
					dir.mkdirs();
				}
				File hpDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+"HP_INPUT_DOCS"+ File.separator + argFileName);
				FileUtils.writeStringToFile(hpDestination, argStrFileContent);
			}catch(Exception exp){
				System.out.println("Exception Occurred while writing Document"+exp.getMessage());
			}
		}

		public static void moveFilesToStatusFolder(boolean argFlag, String strFileName){
			//Moving uploaded file to corresponding folder ...
			String url = System.getProperty("jboss.server.data.dir") + File.separator + strFileName;
			File uDoc =  new File(url);
			if(argFlag){
				File sdir = new File(System.getProperty("jboss.server.data.dir")+File.separator+"HP_SUCCESS_DOCS");
				if(!sdir.exists()){
					sdir.mkdirs();
				}
				File successDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+"HP_SUCCESS_DOCS"+ File.separator + strFileName);
				uDoc.renameTo(successDestination);
			}else{
				File fdir = new File(System.getProperty("jboss.server.data.dir")+File.separator+"HP_FAILED_DOCS");
				if(!fdir.exists()){
					fdir.mkdirs();
				}
				File failureDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+"HP_FAILED_DOCS"+ File.separator + strFileName);
				uDoc.renameTo(failureDestination);
			}
		}
		
		public static void writeStringToFileFVR(String argFileName, String argStrFileContent){
			try{
				argFileName = argFileName.substring(0, argFileName.lastIndexOf(".")-1);
				argFileName = argFileName+".txt";
				
				File dir = new File(System.getProperty("jboss.server.data.dir")+File.separator+"FVR_INPUT_DOCS");
				if(!dir.exists()){
					dir.mkdirs();
				}
				File hpDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+"FVR_INPUT_DOCS"+ File.separator + argFileName);
				FileUtils.writeStringToFile(hpDestination, argStrFileContent);
			}catch(Exception exp){
				System.out.println("Exception Occurred while writing Document"+exp.getMessage());
			}
		}

		public static void moveFilesToStatusFolderFVR(boolean argFlag, String strFileName){
			//Moving uploaded file to corresponding folder ...
			
			String url = System.getProperty("jboss.server.data.dir") + File.separator + strFileName;
			File uDoc =  new File(url);
			if(argFlag){
				File sdir = new File(System.getProperty("jboss.server.data.dir")+File.separator+"FVR_SUCCESS_DOCS");
				if(!sdir.exists()){
					sdir.mkdirs();
				}
				File successDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+"FVR_SUCCESS_DOCS"+ File.separator + strFileName);
				uDoc.renameTo(successDestination);
			}else{
				File fdir = new File(System.getProperty("jboss.server.data.dir")+File.separator+"FVR_FAILED_DOCS");
				if(!fdir.exists()){
					fdir.mkdirs();
				}
				File failureDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+"FVR_FAILED_DOCS"+ File.separator + strFileName);
				uDoc.renameTo(failureDestination);
			}
		}
		
		public static void writeStringToFileROD(String argFileName, String argStrFileContent, String argFolderName){
			try{
				argFileName = argFileName.substring(0, argFileName.lastIndexOf(".")-1);
				argFileName = argFileName+".txt";
				File dir = new File(System.getProperty("jboss.server.data.dir")+File.separator+argFolderName);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File hpDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+argFolderName+ File.separator + argFileName);
				FileUtils.writeStringToFile(hpDestination, argStrFileContent);
			}catch(Exception exp){
				System.out.println("Exception Occurred while writing Document"+exp.getMessage());
			}
		}
		
		
		public static void moveFilesToStatusFolderROD(boolean argFlag, String strFileName, String argSucessFolderName, String argFailedFolderName){
			//Moving uploaded file to corresponding folder ...
			String url = System.getProperty("jboss.server.data.dir") + File.separator + strFileName;
			File uDoc =  new File(url);
			if(argFlag){
				File sdir = new File(System.getProperty("jboss.server.data.dir")+File.separator+argSucessFolderName);
				if(!sdir.exists()){
					sdir.mkdirs();
				}
				File successDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+argSucessFolderName+ File.separator + strFileName);
				uDoc.renameTo(successDestination);
			}else{
				File fdir = new File(System.getProperty("jboss.server.data.dir")+File.separator+argFailedFolderName);
				if(!fdir.exists()){
					fdir.mkdirs();
				}
				File failureDestination = new File(System.getProperty("jboss.server.data.dir")+File.separator+argFailedFolderName+ File.separator + strFileName);
				uDoc.renameTo(failureDestination);
			}
		}
	
		public static Path createDirectory(String intimationNumber)
		{
			Path path = null;
			try
			{
				intimationNumber = intimationNumber.replaceAll("/", "_");
				String url = System.getProperty("jboss.server.data.dir") +File.separator + "claim_documents"+File.separator+intimationNumber;
				 path = Paths.get(url);
				 path = Files.createDirectories(path);
			}
			catch(Exception e)
			{
				System.out.println("-----invalid name for dir------"+intimationNumber);
				e.printStackTrace();
			}
			return path;
		}
		
		public static Path createDirectoryTemp(String intimationNo)
		{
			Path tempDir = null;
			try
			{
				String str[] = intimationNo.split("/");
				int iSize = str.length;
				String folderName = str[iSize-1];
				
				intimationNo = intimationNo.replaceAll("/", "_");
				 File baseDir = new File(System.getProperty("java.io.tmpdir"));
				 File tempDirectory = new File(baseDir, "claims_documents"+File.separator+intimationNo);
				 tempDirectory.mkdir();
				 tempDir = Paths.get(tempDirectory.getAbsolutePath());
				//tempDir = Files.createTempDirectory("intimation");
				 System.out.println("-----the file path----"+tempDirectory.getAbsolutePath());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return tempDir;
		}
		
		
		public static void downloadUsingNIO(String fileUrl,String filename,Path directory) throws IOException {
			 try{
//				 /*filename = "others.pdf";
//				 fileU*/rl = "http://www.alfs.starhealth.in:8080/alfresco/webdav/Galaxy/2016/01/14/14/73516_ID PROOF.PDF?ticket=TICKET_25b909e0e252de876561c8473ac5f4260b895a6b";

			    String filePath = directory.toString();
			    filePath = filePath+File.separator+filename;
		        URL url = new URL(fileUrl);
		        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		        FileOutputStream fos = new FileOutputStream(filePath);
		        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		        fos.close();
		        rbc.close();
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		   }
		
		public static Path createDirectory()
		{
			Path path = null;
			try
			{
				String url = System.getProperty("jboss.server.data.dir") +File.separator + "claim_documents";
				 path = Paths.get(url);
				 path = Files.createDirectories(path);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return path;
		}
	
}