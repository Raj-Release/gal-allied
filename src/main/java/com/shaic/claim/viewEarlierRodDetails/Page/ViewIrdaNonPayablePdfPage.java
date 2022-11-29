package com.shaic.claim.viewEarlierRodDetails.Page;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewIrdaNonPayablePdfPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    final Embedded imageViewer = new Embedded("Uploaded Image");
	
	Panel panel = new Panel();
	
	
	//private String fileName;
	
	//private String tokenName;
	
	//private Window popUp;
	
	public void init(String fileName,String tokenName,final Window popUp){
		
		panel.setWidth("100%");
		panel.setHeight("700px");
		
		/*this.fileName = fileName;
		this.tokenName = tokenName;
		this.popUp = popUp;*/
		

				final String url = System.getProperty("jboss.server.data.dir") + File.separator
						+"IRDA_Non_payable_list.pdf";
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
		        
		        Path p = Paths.get(url);
				fileName = p.getFileName().toString();
				
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
		        
		       
				//Notification.show("Error", "" + "Sorry, the requested file does not exist in the server.", Type.ERROR_MESSAGE);
//			}
			//Added for dms
//		}
				Button closeButton = new Button("Close");
				closeButton.setStyleName(ValoTheme.BUTTON_DANGER);

				closeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						popUp.close();
//						fireViewEvent(MenuItemBean.PROCESS_PED_QUERY,true);
						
					}
				});
				
				
				 VerticalLayout mainVertical = new VerticalLayout(e,closeButton);
				 mainVertical.setHeight("100%");
				 mainVertical.setComponentAlignment(closeButton, Alignment.BOTTOM_CENTER);
			        
				panel.setContent(mainVertical);
		
	setCompositionRoot(panel);
	
	
	
		
	}

}
