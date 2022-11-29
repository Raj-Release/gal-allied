package com.shaic.claim.ompviewroddetails;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPProcessingDetailsTable extends GBaseTable<OMPProcessingDetailsTableDTO>{
	
@Inject	
private OMPViewNegotiationDetailsTable viewNegotiationDetailsObj;
	
	
private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType","ompProcessApprovedDate"
		,"amount","status","typeofPayment","chequeOrTransactionno","chequeOrTransactionnoDate","remarks"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPProcessingDetailsTableDTO>(OMPProcessingDetailsTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType","ompProcessApprovedDate"
			,"amount","status","typeofPayment","chequeOrTransactionno","chequeOrTransactionnoDate","remarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("view");
		table.addGeneratedColumn("view", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Bill Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						//	ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
						//	Intimation intimation = financialDto.getClaim().getIntimation();
							
					//		Long rodKey = financialDto.getReimbursementKey();
							
					//			if (intimation != null) {

							//		viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
								   
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("Amount Claimed Details");
									popup.setWidth("75%");
									popup.setHeight("75%");
									
									// Create amount claimed details table
									
							//		popup.setContent(viewClaimHistoryRequest);
									popup.setClosable(true);
									popup.center();
									popup.setResizable(false);
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
							//	}else{
							//		getErrorMessage("History is not available");
							//	}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.setColumnHeader("view", "view");
		
		
		table.removeGeneratedColumn("view");
		table.addGeneratedColumn("view", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Negotiation Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							OMPProcessingDetailsTableDTO financialDto = (OMPProcessingDetailsTableDTO)itemId;
							
						//	Intimation intimation = financialDto.getClaim().getIntimation();
							
						//	Long rodKey = financialDto.getReimbursementKey();
							
							//	if (intimation != null) {

						//			List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgmentService.getViewAcknowledgementList(rodKey);
									
							//		viewAcknowledgementTableObj.init(financialDto.getRodNumber(), false, false);
							//		viewAcknowledgementTableObj.setTableList(viewAcknowledgementList);
								   
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("Negotiation Details");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewNegotiationDetailsObj);
									popup.setClosable(true);
									popup.center();
									popup.setResizable(false);
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
							//	else{
							//		getErrorMessage("No Negotiation Details");
								//}
								
				     //   }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
										
		      }
		});
		
		table.setColumnHeader("view", "View ");
	}

	@Override
	public void tableSelectHandler(OMPProcessingDetailsTableDTO t) {
		
		
	}
	public void getErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}



	@Override
	public String textBundlePrefixString() {
		
		return "ompprocess-details-";
	}

}
