package com.shaic.claim.pancard.page;

import java.io.File;
import java.io.FileInputStream;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAFileUtils;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;

public class UpdatePanCardPdfPage extends ViewComponent {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final Embedded imageViewer = new Embedded("Uploaded Image");
	
	public static String dataDir = System.getProperty("jboss.server.data.dir");
	
	public void init(String name,String token){
		
		Panel panel = new Panel();
		panel.setCaption("Uploaded Documents View");
       // panel.setSizeFull();
        panel.setHeight("450px");
		
		if(name.endsWith(".JPG") || name.endsWith(".jpg"))
		{
//			final String imageUrl = filepath;
			final String imageUrl = SHAFileUtils.viewFileByToken(token);
			if(imageUrl != null) {
			/*	File f = new File(imageUrl);
				if(f != null){
				String url = f.getAbsolutePath();*/
				imageViewer.setSource(new ExternalResource(imageUrl));
			    imageViewer.setVisible(true);  
			    imageViewer.setHeight("500px");
			    panel.setContent(imageViewer);	
				//}
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
			    imageViewer.setHeight("500px");
			    panel.setContent(imageViewer);	
			}
			
		}
		else{

			final String imageUrl = SHAFileUtils.viewFileByToken(token);
			if(null != imageUrl)
			{
				Button saveExcel = new Button();
				Resource res = new FileResource(new File(imageUrl));
				FileDownloader fd = new FileDownloader(res);
				fd.extend(saveExcel);
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+ name;
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
				StreamResource r = new StreamResource(s, name);
		        r.setMIMEType("application/pdf");
		        e.setSource(r);
		        panel.setContent(e);
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
				StreamResource r = new StreamResource(s, name);
		        r.setMIMEType("application/pdf");
		        e.setSource(r);
				panel.setContent(e);
				//Notification.show("Error", "" + "Sorry, the requested file does not exist in the server.", Type.ERROR_MESSAGE);
			}
			
		}
		setCompositionRoot(panel);
		
	}

}
