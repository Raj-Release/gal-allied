package com.shaic.paclaim.reimbursement.bulkreminder;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.reimbursement.reminderBulkSearch.BulkReminderResultDto;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class ShowPAReminderLetterBulkPage extends ViewComponent {
	
	private ShowPAReminderLetterBulkPage instance;
	SearchGeneratePARemainderBulkViewImpl parent;
	
	public void initView(final SearchGeneratePARemainderBulkViewImpl parent, String fileUrl,BulkReminderResultDto bulkReminderDto) {
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
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		SHAUtils.closeStreamResource(s);
		e.setSizeFull();
		e.setHeight("90%");
		
//		VerticalLayout letterLayout = new VerticalLayout();
//		letterLayout.addComponent(e);
		Button submitBtn = new Button("Submit");
		submitBtn.setData(bulkReminderDto);
		VerticalLayout wholeLayout = new VerticalLayout();
		wholeLayout.addComponent(e);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponent(submitBtn);
		hLayout.setWidth("100%");
		hLayout.setComponentAlignment(submitBtn, Alignment.MIDDLE_CENTER);
//		hLayout.setHeightUndefined();
		wholeLayout.addComponent(hLayout);
		wholeLayout.setSizeFull();
		wholeLayout.setHeight("190%");
		wholeLayout.setSpacing(false);
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				BulkReminderResultDto bulkDto = (BulkReminderResultDto)((Button)event.getSource()).getData();
				parent.submitReminderLetterBulkReminderResultDto(bulkDto);				
			}
		});
		setHeight("100%");
		setCompositionRoot(wholeLayout);
	}
	

}
