package com.shaic.claim.cvc.auditreport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class ClaimsAuditReportViewImpl extends AbstractMVPView implements ClaimAuditReportView{
	
		
		@Inject
		private ClaimsAuditReportForm  searchForm;
		
		private VerticalLayout mainPanel;
		
		@PostConstruct
		protected void initView() {
			addStyleName("view");
			setSizeFull();
			searchForm.init();
			searchForm.getContent();
							
			mainPanel = new VerticalLayout();
			mainPanel.addComponent(searchForm);
			
//			vLayout = new VerticalLayout(searchResultTable);
//			vLayout = new VerticalLayout();
//			addFooterButtons();
			
//			mainPanel.setSecondComponent(vLayout);
//			mainPanel.setSplitPosition(42);
			setHeight("670");
			mainPanel.setHeight("650");
			setCompositionRoot(mainPanel);
//			searchResultTable.addSearchListener(this);
			searchForm.addSearchListener(this);
					
			resetView();
		}

		@Override
		public void doSearch() {
			
		}

		
		@Override
		public void resetView() {
			// TODO Auto-generated method stub
			searchForm.resetFields();
		}

		
	

		@Override
		public void resetSearchResultTableValues() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void showErrorMsg(String msg) {

			Label successLabel = new Label(msg, ContentMode.HTML);			
			VerticalLayout layout = new VerticalLayout(successLabel);
			layout.setSpacing(true);
			layout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setWidth("10%");
			dialog.show(getUI().getCurrent(), null, true);
			
		}

	}

