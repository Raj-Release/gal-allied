package com.shaic.claim.omppaymentletterbulk;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.reimbursement.printReminderLetterBulk.PrintBulkReminderResultDto;
import com.shaic.reimbursement.printReminderLetterBulk.SearchPrintRemainderBulkViewImpl;
import com.shaic.reimbursement.printReminderLetterBulk.ShowPrintReminderLetterBulkPage;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.VerticalLayout;

public class ShowPrintPaymentLetterBulkPage extends ViewComponent {
	
	private ShowPrintPaymentLetterBulkPage instance;
	SearchPrintPaymentBulkViewImpl parent;
	
	public void initView(final SearchPrintPaymentBulkViewImpl parent, String fileUrl,PrintBulkPaymentResultDto bulkReminderDto) {
		this.instance = this;
		this.parent = parent;
		
		final String filepath = fileUrl;
		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

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
		};
		
		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		e.setSizeFull();
		e.setHeight("90%");
		

		VerticalLayout wholeLayout = new VerticalLayout();
		wholeLayout.addComponent(e);

		wholeLayout.setHeight("100%");
		wholeLayout.setSpacing(false);

		setHeight("100%");
		setCompositionRoot(wholeLayout);
	}
}

