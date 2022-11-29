package com.shaic.claim.ompviewroddetails;

import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.Intimation;
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

public class OMPRodAndBillEntryDetailTable extends GBaseTable<OMPRodAndBillEntryDetailTableDTO>{
	
	private static final long serialVersionUID = 1L;
	
	private Window popup;
	@Inject
	private OMPViewHistoryRodAndBillEntryDetailTable viewClaimHistoryRequest;
	@Inject
	private OMPViewRejectionRodDetailTable viewRejectionDetails;
	@Inject
	private OMPViewReconsiderationRodDetailTable viewReconsiderationRodDetails;
	
	
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType",
		"documentReceivedDate","lastDocumentReceivedDate","modeofReceipt","approvedAmount","status"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initTable(){
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPRodAndBillEntryDetailTableDTO>(OMPRodAndBillEntryDetailTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNo","claimType","eventCode","classification","documentReceivedFrom","rodType",
			"documentReceivedDate","lastDocumentReceivedDate","modeofReceipt","approvedAmount","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
		table.removeGeneratedColumn("viewTrails");
		table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Trails");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Intimation intimation = financialDto.getClaim().getIntimation();
							
							Long rodKey = financialDto.getReimbursementKey();
							
								if (intimation != null) {

							//		viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
								   
									popup = new com.vaadin.ui.Window();
									popup.setCaption("View History");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewClaimHistoryRequest);
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
								}else{
									getErrorMessage("History is not available");
								}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.setColumnHeader("viewTrails", "viewTrails");
		
		
		table.removeGeneratedColumn("viewRejection");
		table.addGeneratedColumn("viewRejection", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Rejection");
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
								   
									popup = new com.vaadin.ui.Window();
									popup.setCaption("View Rejection Details");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewRejectionDetails);
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
		
		table.setColumnHeader("viewRejection", "viewRejection ");
		
		table.removeGeneratedColumn("viewReconsideration");
		table.addGeneratedColumn("viewReconsideration", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("View Reconsideration");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							ViewDocumentDetailsDTO financialDto = (ViewDocumentDetailsDTO)itemId;
							
							Intimation intimation = financialDto.getClaim().getIntimation();
							
							Long rodKey = financialDto.getReimbursementKey();
							
								if (intimation != null) {

							//		viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
								   
									popup = new com.vaadin.ui.Window();
									popup.setCaption("View Reconsideration");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewReconsiderationRodDetails);
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
								}else{
									getErrorMessage("History is not available");
								}
								
				        }

					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		table.setColumnHeader("viewReconsideration", "view Reconsideration ");
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
	public void tableSelectHandler(OMPRodAndBillEntryDetailTableDTO t) {
		
		
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "omprodandbillentry-details-";
	}

}
