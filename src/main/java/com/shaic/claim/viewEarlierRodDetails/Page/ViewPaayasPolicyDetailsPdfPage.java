package com.shaic.claim.viewEarlierRodDetails.Page;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.Hospitals;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPaayasPolicyDetailsPdfPage extends Window{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    final Embedded imageViewer = new Embedded("Uploaded Image");
	
	Panel panel = new Panel();
	
	public void init(NewIntimationDto intimationDto){
		
		setHeight("90%");
		setWidth("90%");
		setModal(true);
		setClosable(true);
		setResizable(true);
		
		String fileUrl = null;
		if(intimationDto.getHospitalDto().getDiscount() != null){
			if(intimationDto.getHospitalDto().getDiscount().equalsIgnoreCase(SHAConstants.HOSPITAL_RSBY)){
				fileUrl = System.getProperty("jboss.server.data.dir") + File.separator
						+"RSBY CATEGORY PACKAGE CHARGES UPDATED.pdf";
			}else if(intimationDto.getHospitalDto().getDiscount().equalsIgnoreCase(SHAConstants.HOSPITAL_NABH)){
				fileUrl = System.getProperty("jboss.server.data.dir") + File.separator
						+"CGHS PACKAG RATES NABH AND NABL.pdf";
			}else if(intimationDto.getHospitalDto().getDiscount().equalsIgnoreCase(SHAConstants.HOSPITAL_NON_NABH)){
				fileUrl = System.getProperty("jboss.server.data.dir") + File.separator
						+"CGHS PACKAG RATES NON  NABH AND NON NABL.pdf";
			}
		}

				final String url = fileUrl;
				
				
				
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
		        
		        Path p = Paths.get(url);
				String fileName = p.getFileName().toString();
				
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
		        e.setWidth("100%");
		       
				Button closeButton = new Button("Close");
				closeButton.setStyleName(ValoTheme.BUTTON_DANGER);
				
				final ViewPaayasPolicyDetailsPdfPage paayasPdfPage = this;

				closeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						
						paayasPdfPage.close();
						//popUp.close();
//						fireViewEvent(MenuItemBean.PROCESS_PED_QUERY,true);
						
					}
				});
				
				
				 VerticalLayout mainVertical = new VerticalLayout(e);
				 mainVertical.setHeight("100%");
				// mainVertical.setComponentAlignment(closeButton, Alignment.BOTTOM_CENTER);
			        
				panel.setContent(mainVertical);
				panel.setHeight("180%");
		        setContent(panel);
	
	
	}
	
public void init(Hospitals hospitals){
		
		setHeight("90%");
		setWidth("90%");
		setModal(true);
		setClosable(true);
		setResizable(true);
		
		String fileUrl = null;
		if(hospitals.getDiscount() != null){
			if(hospitals.getDiscount().equalsIgnoreCase(SHAConstants.HOSPITAL_RSBY)){
				fileUrl = System.getProperty("jboss.server.data.dir") + File.separator
						+"RSBY CATEGORY PACKAGE CHARGES UPDATED.pdf";
			}else if(hospitals.getDiscount().equalsIgnoreCase(SHAConstants.HOSPITAL_NABH)){
				fileUrl = System.getProperty("jboss.server.data.dir") + File.separator
						+"CGHS PACKAG RATES NABH AND NABL.pdf";
			}else if(hospitals.getDiscount().equalsIgnoreCase(SHAConstants.HOSPITAL_NON_NABH)){
				fileUrl = System.getProperty("jboss.server.data.dir") + File.separator
						+"CGHS PACKAG RATES NON  NABH AND NON NABL.pdf";
			}
		}

				final String url = fileUrl;
				
				
				
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
		        
		        Path p = Paths.get(url);
				String fileName = p.getFileName().toString();
				
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
		        e.setWidth("100%");
		       
				Button closeButton = new Button("Close");
				closeButton.setStyleName(ValoTheme.BUTTON_DANGER);
				closeButton.setWidth("-1px");
				closeButton.setHeight("-10px");
				final ViewPaayasPolicyDetailsPdfPage paayasPdfPage = this;

				closeButton.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						paayasPdfPage.close();
						//popUp.close();
//						fireViewEvent(MenuItemBean.PROCESS_PED_QUERY,true);
						
					}
				});
				
				
				 VerticalLayout mainVertical = new VerticalLayout(e);
				 mainVertical.setHeight("100%");
				// mainVertical.setComponentAlignment(closeButton, Alignment.BOTTOM_CENTER);
			        
				panel.setContent(mainVertical);
				panel.setHeight("180%");
		        setContent(panel);
	
	
	}

public void ViewHealthCard(String healthCardNumber){
	
			setHeight("90%");
			setWidth("90%");
			setModal(true);
			setClosable(true);
			setResizable(true);
	
			String fileUrl = null;	
			fileUrl = PremiaService.getHealthCardViewDetails(healthCardNumber);

			final String url = fileUrl;
			
			
			
	        Embedded e = new Embedded();
	        e.setSizeFull();
	        e.setType(Embedded.TYPE_BROWSER);
	        
	        Path p = Paths.get(url);
			String fileName = p.getFileName().toString();
			
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
	        e.setWidth("100%");
	       
			Button closeButton = new Button("Close");
			closeButton.setStyleName(ValoTheme.BUTTON_DANGER);
			closeButton.setWidth("-1px");
			closeButton.setHeight("-10px");
			final ViewPaayasPolicyDetailsPdfPage paayasPdfPage = this;

			closeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					paayasPdfPage.close();
					//popUp.close();
//					fireViewEvent(MenuItemBean.PROCESS_PED_QUERY,true);
					
				}
			});
			
			
			 VerticalLayout mainVertical = new VerticalLayout(e);
			 mainVertical.setHeight("100%");
			// mainVertical.setComponentAlignment(closeButton, Alignment.BOTTOM_CENTER);
		        
			panel.setContent(mainVertical);
			panel.setHeight("180%");
	        setContent(panel);


  }

}
