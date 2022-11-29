package com.shaic.claim.rod.citySearchCriteria;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.bulkconvertreimb.search.SearchBatchConvertedTableDto;
import com.shaic.claim.outpatient.processOP.wizard.ProcessOPClaimWizardPresenter;
import com.shaic.claim.outpatient.processOPpages.ProcessOPBillDetailsPage;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claims.reibursement.rod.UploadNEFTDetails.UploadNEFTDetailsPresenter;
import com.shaic.claims.reibursement.rod.addaditionaldocumentsPaymentInfo.AddAditionalDocumentsPaymentInfoPagePresenter;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class SearchPayableAtTable extends GBaseTable<SearchPayableAtTableDTO>{

	private final static Object[] NATURAL_COL_ORDER_SEARCH = new Object[]{"serialNumber","payable"};
	
	private Window popup;
	
	private String presenterString;
	
	private CreateAndSearchLotTableDTO lotAndBatchDTO;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<SearchPayableAtTableDTO>(SearchPayableAtTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER_SEARCH);
		table.setWidth("100%");
		table.setColumnWidth("payable", 140);
		
		setSizeFull();
		table.removeGeneratedColumn("selectDetails");
		
		table.addGeneratedColumn("selectDetails", 
				new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnSelect = new Button();
				btnSelect.setStyleName(ValoTheme.BUTTON_LINK);
				btnSelect.setCaption("Select");
				final SearchPayableAtTableDTO viewSearchCriteriaTableDTO = (SearchPayableAtTableDTO)itemId;
				btnSelect.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						
						if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString)){
							fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_ROD_PAYABLE_DETAILS, viewSearchCriteriaTableDTO.getPayable());	
						}
						else if(SHAConstants.BILLING_SCREEN.equalsIgnoreCase(presenterString)){
							fireViewEvent(BillingHospitalizationPagePresenter.SETUP_BILLING_PAYABLE_DETAILS, viewSearchCriteriaTableDTO.getPayable());
						}else if(SHAConstants.FINANCIAL_APPROVER.equalsIgnoreCase(presenterString)){
							fireViewEvent(FinancialHospitalizationPagePresenter.SETUP_FA_PAYABLE_DETAILS, viewSearchCriteriaTableDTO.getPayable());
						}else if(SHAConstants.CREATE_LOT.equalsIgnoreCase(presenterString)){
							fireViewEvent(CreateAndSearchLotPresenter.CHANGE_PAYABLE_AT_LOT, viewSearchCriteriaTableDTO.getPayable(),lotAndBatchDTO);
						}else if(SHAConstants.CREATE_BATCH.equalsIgnoreCase(presenterString)){
							fireViewEvent(SearchCreateBatchPresenter.CHANGE_PAYABLE_AT_BATCH, viewSearchCriteriaTableDTO.getPayable(),lotAndBatchDTO);
						}
						else if(SHAConstants.ADD_ADDITIONAL_PAYMENT_INFO.equalsIgnoreCase(presenterString)){
							fireViewEvent(AddAditionalDocumentsPaymentInfoPagePresenter.SETUP_PAYMENT_INFO_DOCUMENTS_PAYABLE_DETAILS, viewSearchCriteriaTableDTO.getPayable());	
						}
						else if(SHAConstants.UPLOAD_NEFT_DETAILS.equalsIgnoreCase(presenterString)){
							fireViewEvent(UploadNEFTDetailsPresenter.SETUP_UPLOAD_NEFT_DETAILS_PAYABLE_DETAILS, viewSearchCriteriaTableDTO.getPayable());	
						}
						else if(SHAConstants.PA_ADD_ADDITIONAL_PAYMENT_INFO.equalsIgnoreCase(presenterString)){
							fireViewEvent(com.shaic.paclaim.addAdditionalDocPaymentInfo.search.AddAditionalDocumentsPaymentInfoPagePresenter.SETUP_PAYMENT_INFO_DOCUMENTS_PAYABLE_DETAILS, viewSearchCriteriaTableDTO.getPayable());	
						}
						else if(SHAConstants.OP_PROCESS_SCREEN.equalsIgnoreCase(presenterString)){
							fireViewEvent(ProcessOPClaimWizardPresenter.SETUP_OP_PAYABLE_DETAILS, viewSearchCriteriaTableDTO.getPayable());	
						}
						
						
						popup.close();	
					}
				});
				return btnSelect;
			}
		});
		
		table.setColumnHeader("selectDetails", "Select");
	}

	@Override
	public void tableSelectHandler(SearchPayableAtTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "payable_search_criteria_table-";
	}
	
	public void setWindowObject(Window popup){
		this.popup = popup;
	}
	
	public void initPresenter(String presenterString)
	{
		this.presenterString = presenterString;
	}

	public void setLotAndBatchDTO(CreateAndSearchLotTableDTO lotAndBatchDTO) {
		this.lotAndBatchDTO = lotAndBatchDTO;
	}

}
