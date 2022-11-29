package com.shaic.claim;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Window;

public class ViewDocuments extends Window{
	public static String dataDir = System.getProperty("jboss.server.data.dir");
	@PostConstruct
	public void initView(){
		this.setResizable(true);
		this.setCaption("View Documents PDF");
		this.setWidth("800");
		this.setHeight("600");
		this.setModal(true);
		this.center();
		final String filepath = dataDir +"/Medi-Classic-Individual.pdf";

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 9138325634649289303L;

			public InputStream getStream() {
				try {

					File f = new File(filepath);
					System.out.println(f.getCanonicalPath());
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		this.setContent(e);
		
		
		
	}
	
		
	}