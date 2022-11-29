package com.shaic.claim.viewEarlierRodDetails;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.domain.Intimation;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
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

public class FinancialApprovalTable extends GBaseTable<FinancialApprovalTableDTO> {
	
	/**
	 * 
	 */
	/*private static final long serialVersionUID = 1L;
	public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","documentReceivedFrom","approvalType","faDate","amount","status"
		,"paymentType","transactionNo","transactionDate","financialRemarks"};*/
	
	/*public static final Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno","rodNumber","approvalType","benefitCover","documentReceivedFrom","rodType","faDate","amount","status"
		,"paymentType","transactionNo","transactionDate","financialRemarks"};*/
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	private Window popup;


	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}
	
	
	
	public void setPAColumsnForFinancial()
	{
		 Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno","rodNumber","approvalType","benefitCover","documentReceivedFrom","rodType","faDate","amount","status"
			,"paymentType","transactionNo","transactionDate","financialRemarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER_PA);
		table.setColumnHeader("benefitCover", "Benefit/Cover");
		table.setColumnHeader("rodNumber", "ROD No");
		table.setColumnHeader("rodType", "ROD Type");
		table.setColumnHeader("approvalType", "Claim Type");
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<FinancialApprovalTableDTO>(
				FinancialApprovalTableDTO.class));		
		 Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNumber","typeOfClaim","billClassification","documentReceivedFrom","rodType"/*,"approvalType"*/,"faDate","amount","status"
				 /*,"paymentType","transactionNo","transactionDate"*/,"financialRemarks"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
	}

	@Override
	public void tableSelectHandler(FinancialApprovalTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-financial-approval-table-";
	}
	
	
	public void generateAuditDetails(){
		
		table.removeGeneratedColumn("auditDetails");
		table.addGeneratedColumn("auditDetails", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button button = new Button("Audit Details");
		    	button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						FinancialApprovalTableDTO financialDto = (FinancialApprovalTableDTO)itemId;
						Long rodKey = financialDto.getRodKey();
						
						Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(rodKey);
						
						Intimation intimation = null;
						
						if(reimbursementByKey != null){
							intimation = reimbursementByKey.getClaim().getIntimation();
						}
						
							if (intimation != null) {

								viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
							   
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
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	return button;
		      }
		});
		table.setColumnWidth("auditDetails",180);
		table.setColumnHeader("auditDetails", "Audit Details");
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

}
