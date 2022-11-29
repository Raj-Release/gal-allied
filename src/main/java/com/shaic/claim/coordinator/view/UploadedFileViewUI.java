package com.shaic.claim.coordinator.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAFileUtils;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UploadedFileViewUI extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final Embedded imageViewer = new Embedded("Uploaded Image");
	
	Panel panel = new Panel();
	
	private Window popup;
	
	/*private String fileName;
	
	private String tokenName;*/
	
	private Page currentPage;
	
	
	public void setCurrentPage(Page currentPage){
		this.currentPage = currentPage;
	}
	
	public void init(final Window popUp,String fileName,String tokenName){
		
		panel.setWidth("500px");
		
		this.popup = popUp;
		
		VerticalLayout mainVertical = new VerticalLayout();
		
		//this.fileName = fileName;
		//this.tokenName = tokenName;
		
		if(fileName.endsWith(".JPG") || fileName.endsWith(".jpg"))
		{
			String imageUrl = SHAFileUtils.viewFileByToken(tokenName);
			if(imageUrl != null) {
				imageViewer.setSource(new ExternalResource(imageUrl));
			    imageViewer.setVisible(true);  
			    imageViewer.setHeight("100%");
			    imageViewer.setWidth("100%");
			    Panel imagePanel = new Panel();
			    imagePanel.setContent(imageViewer);
			    mainVertical.addComponent(imagePanel);	
			}
			else
			{
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"SampleBill.JPG";
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				imageViewer.setSource(new StreamResource(s, "SampleBill.jpg"));
			    imageViewer.setVisible(true);  
//			    imageViewer.setHeight("1000px");
//			    imageViewer.setWidth("1000px");
			    Panel imagePanel = new Panel();
			    imagePanel.setContent(imageViewer);
			    imagePanel.setWidth("100%");
			    imagePanel.setHeight("100%");
			    mainVertical.addComponent(imagePanel);		
//				final String url = System.getProperty("jboss.server.data.dir") + "/"
//						+"SampleBill.JPG";
//				imageViewer.setSource(new ExternalResource(url));
//			    imageViewer.setVisible(true);  
//			    imageViewer.setHeight("500px");
//			    Panel imagePanel = new Panel();
//			    imagePanel.setContent(imageViewer);
//			    hsplitPanel.setFirstComponent(imagePanel);	

			//    hsplitPanel.setFirstComponent(imagePanel);	
				/*final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"SampleBill.JPG";
				imageViewer.setSource(new ExternalResource(url));
			    imageViewer.setVisible(true);  
			    imageViewer.setHeight("500px");
			    Panel imagePanel = new Panel();
			    imagePanel.setContent(imageViewer);
			    hsplitPanel.setFirstComponent(imagePanel);	*/

			}

		}
		
		else if(fileName.endsWith(".PDF") || fileName.endsWith(".pdf") || fileName.endsWith("doc") || fileName.endsWith("docx"))
		{
//			final String imageUrl = SHAFileUtils.viewFileByToken(tokenName);
//			if(null != imageUrl)
//			{
//				Button saveExcel = new Button();
//				Resource res = new FileResource(new File(imageUrl));
//				FileDownloader fd = new FileDownloader(res);
//				fd.extend(saveExcel);
//				final String url = System.getProperty("jboss.server.data.dir") + "/"
//						+ fileName;
//		        Embedded e = new Embedded();
//		        e.setSizeFull();
//		        e.setType(Embedded.TYPE_BROWSER);
//				StreamResource.StreamSource s = new StreamResource.StreamSource() {
//
//					public FileInputStream getStream() {
//						try {
//							File f = new File(url);
//							FileInputStream fis = new FileInputStream(f);
//							return fis;
//						} catch (Exception e) {
//							e.printStackTrace();
//							return null;
//						}
//					}
//				};
//				StreamResource r = new StreamResource(s, fileName);
//		        r.setMIMEType("application/pdf");
//		        e.setSource(r);
		        
				final String imageUrl = SHAFileUtils.viewFileByToken(tokenName);
				if(null != imageUrl)
				{
	/*				Button saveExcel = new Button();
					Resource res = new FileResource(new File(imageUrl));
					FileDownloader fd = new FileDownloader(res);
					fd.extend(saveExcel);
					final String url = System.getProperty("jboss.server.data.dir") + "/"
							+ bean.getFileName();
			        Embedded e = new Embedded();
			        e.setSizeFull();
			        e.setType(Embedded.TYPE_BROWSER);
					StreamResource.StreamSource s = new StreamResource.StreamSource() {
		
						public FileInputStream getStream() {
							try {
								File f = new File(url);
								FileInputStream fis = new FileInputStream(f);
								return fis;
							} catch (Exception e) {
								e.printStackTrace();
								return null;
							}
						}
					};
					StreamResource r = new StreamResource(s, bean.getFileName());
			        r.setMIMEType("application/pdf");
			        e.setSource(r);
			        e.setSizeFull();*/
					 Embedded e = new Embedded();
				     e.setSizeFull();
				     e.setType(Embedded.TYPE_BROWSER);
			         StreamResource.StreamSource source = new StreamResource.StreamSource() {
	                     public InputStream getStream() {
	                        
	                     	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                     	InputStream is = null;
	                     	URL u = null;
	                     	URLConnection urlConnection = null;
	                     	try {
//	                     		u =  new URL(imageUrl);
//	                     		
//	                     	  is = u.openStream();
	                     		u =  new URL(imageUrl);
	        					urlConnection =  u.openConnection();
	        					is = urlConnection.getInputStream();
	                     	  
	                     	  
//	                     	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
	        					byte[]  byteChunk = null;
	        					/*if(urlConnection.getContentLength() > 25000){
	    							byteChunk = new byte[25000];
	    						} else {
	    							byteChunk = new byte[urlConnection.getContentLength()];
	    						}*/
	    						
	    						byteChunk = new byte[urlConnection.getContentLength()];
	    						
	                     	  int n;

	                     	  while ( (n = is.read(byteChunk)) > 0 ) {
	                     	    baos.write(byteChunk, 0, n);
	                     	  }
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
	             StreamResource r = new StreamResource(source,fileName);
	             if(fileName.endsWith("docx") || fileName.endsWith("doc"))
	             {
	             	r.setMIMEType("application/msword");
	             }
	             else
	             {
	             	r.setMIMEType("application/pdf");
	             }
	             //r.setMIMEType("application/pdf");
	             
	             r.setFilename(fileName+System.currentTimeMillis());
	             r.setStreamSource(source);
				 e.setSource(r);
//                    getUI().getPage().open(r, "_blank", false);
                    
		        Panel imagePanel = new Panel(e);
		        imagePanel.setHeight("700px");
		        mainVertical.addComponent(imagePanel);	
		        panel.setSizeFull();
		        panel.setHeight("600px");
			}
			else
			{
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"BILL.PDF";
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {

					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s,fileName);
		        r.setMIMEType("application/pdf");
		        e.setSource(r);
		        e.setSizeFull();
		        Panel imagePanel = new Panel(e);
		        imagePanel.setHeight("700px");
		        mainVertical.addComponent(imagePanel);	
		        mainVertical.setSizeFull();
				panel.setSizeFull();
//				panel.setHeight("600px");
				//Notification.show("Error", "" + "Sorry, the requested file does not exist in the server.", Type.ERROR_MESSAGE);
			}
			//Added for dms
		}							
		else if(fileName.endsWith(".html") || fileName.endsWith(".HTML"))
		{
			final String imageUrl = SHAFileUtils.viewFileByToken(tokenName);
			if(null != imageUrl)
			{
				if(getUI() == null && currentPage != null){
					currentPage.open(imageUrl, "_blank");
				}else{
					getUI().getPage().open(imageUrl, "_blank");
				}

			}
			else
			{
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"BILL.PDF";
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {

					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s,fileName);
		        r.setMIMEType("application/pdf");
		        e.setSource(r);
		        e.setSizeFull();
		        Panel imagePanel = new Panel(e);
		        imagePanel.setHeight("700px");
		        mainVertical.addComponent(imagePanel);	
		        mainVertical.setSizeFull();
				panel.setSizeFull();
			}
		}						
		else if(fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))
		{
			final String imageUrl = SHAFileUtils.viewFileByToken(tokenName);
			if(null != imageUrl)
			{
				if(getUI() == null && currentPage != null){
					currentPage.open(imageUrl, "_blank");
				}else{
					getUI().getPage().open(imageUrl, "_blank");
				}

				
			}
			else
			{
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"Amount considered and Decision Table.xlsx";
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
	
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s, "Amount considered and Decision Table.xlsx");
				r.setMIMEType("application/x-msexcel");
				r.setFilename("Amount considered and Decision Table.xlsx");
		        e.setSource(r);
		        e.setSizeFull();
		        showPopup(e);
			}
		} else if (fileName.endsWith(".zip") || fileName.endsWith(".ZIP")){
			
			String imageUrl = SHAFileUtils.viewFileByToken(tokenName);

			if(getUI() == null && currentPage != null){
				currentPage.open(imageUrl, "_blank");
			}else{
				getUI().getPage().open(imageUrl, "_blank");
			}
		
		
			
		}
		
		Button submitButton = new Button("Close");
		submitButton.setStyleName(ValoTheme.BUTTON_DANGER);
		
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {					
				popUp.close();
			}
		});
		
		mainVertical.addComponent(submitButton);
		mainVertical.setComponentAlignment(submitButton, Alignment.BOTTOM_CENTER);
		panel.setContent(mainVertical);
		panel.setHeight("700px");
		panel.setWidth("100%");
		
	    setCompositionRoot(panel);
		
	}
	
	private void showPopup(Embedded e)
	{

		/**
		 * On click, the rrc histiory needs to displayed.
		 * */
		popup = new com.vaadin.ui.Window();
		
		
		
		/*viewClaimWiseRRCHistoryPage.init(bean,popup);
		viewClaimWiseRRCHistoryPage.initPresenter(SHAConstants.VIEW_RRC_REQUEST);
		viewClaimWiseRRCHistoryPage.getContent();*/
		
		popup.setCaption("View Uploaded File");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(e);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	
	}
	
	

}
