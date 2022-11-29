package com.shaic.paclaim.reminder;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class ShowPAReminderLetterPage extends ViewComponent {
	
	private ShowPAReminderLetterPage instance;
	SearchGeneratePARemainderViewImpl parent;
	
	public void initView(final SearchGeneratePARemainderViewImpl parent, String fileUrl) {
		this.instance = this;
		this.parent = parent;
		
		final String filepath = fileUrl;
		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();
		StreamResource.StreamSource s = SHAUtils.getStreamResource(filepath);

		/*StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(filepath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};*/

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		
//		Panel letterPanel = new Panel();
//		letterPanel.setHeight("80%");
//		letterPanel.setWidth("60%");
//		letterPanel.setSizeFull();
//		letterPanel.setContent(e);
		VerticalLayout letterLayout = new VerticalLayout();
		letterLayout.addComponent(e);
		letterLayout.setHeight("520");
		Button submitBtn = new Button("Submit");
		VerticalLayout wholeLayout = new VerticalLayout();
		wholeLayout.setSizeFull();
		wholeLayout.addComponent(letterLayout);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponent(submitBtn);
		wholeLayout.addComponent(hLayout);
		wholeLayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
		wholeLayout.setSpacing(false);
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				parent.submitReminderLetter();				
			}
		});
		setSizeFull();
		setCompositionRoot(wholeLayout);
	}
	

}
