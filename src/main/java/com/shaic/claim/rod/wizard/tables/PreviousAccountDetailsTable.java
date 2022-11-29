/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

/**
 * @author ntv.vijayar
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.outpatient.processOP.pages.assessmentsheet.OPClaimAssessmentPagePresenter;
import com.shaic.claim.outpatient.processOP.pages.settlement.OPClaimSettlementPagePresenter;
import com.shaic.claim.reimbursement.billing.pages.billHospitalization.BillingHospitalizationPagePresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization.FinancialHospitalizationPagePresenter;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claims.reibursement.rod.UploadNEFTDetails.UploadNEFTDetailsPresenter;
import com.shaic.claims.reibursement.rod.addaditionaldocumentsPaymentInfo.AddAditionalDocumentsPaymentInfoPagePresenter;
import com.shaic.reimbursement.paymentprocess.createbatch.search.SearchCreateBatchPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author ntv.narenj
 *
 */
public class PreviousAccountDetailsTable extends GBaseTable<PreviousAccountDetailsDTO>{

	private Window popup; 
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNo","chkSelect","source","policyClaimNo","intimationNumber","rodNumber","receiptDate", "bankAccountNo", "bankName", "bankCity","bankBranch","accountType",
		"ifsccode"} ;
	
	
	
	//public List<Component> compList = null;
	public HashMap<Long,Component> compMap = null;
	public List<PreviousAccountDetailsDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	private String presenterString;
	private CheckBox chkBox;
	private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
	
	private CreateAndSearchLotTableDTO createBatchTableDTO;
	

	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<PreviousAccountDetailsDTO>(PreviousAccountDetailsDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		generatecolumns();
	}

	public void setPresenterString(String presenterString)
	{
		this.presenterString = presenterString;
		
	}
	
	public void setPresenterStringForLotAndBatch(String presenterString,CreateAndSearchLotTableDTO createBatchTableDTO)
	{
		this.presenterString = presenterString;
		this.createBatchTableDTO = createBatchTableDTO;
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-previous-account-details-";
	}
	
	
	private void generatecolumns(){
		
	//	compList = new ArrayList<Component>();
		compMap = new HashMap<Long, Component>();
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO) itemId;
					chkBox = new CheckBox("");					
					chkBox.setData(tableDTO);	
					chkBox.setValue(false);
					//Vaadin8-setImmediate() chkBox.setImmediate(true);
					addListener(chkBox);
				return chkBox;
			}
		});
		
	}
	
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{								  
					boolean value = (Boolean) event.getProperty().getValue();	
					checkBoxList.add(chkBox);
					PreviousAccountDetailsDTO tableDTO = (PreviousAccountDetailsDTO)chkBox.getData();
					tableDTO.setChkSelect(value);
					
					if(value)					
					{							
						tableSelectHandler(tableDTO);						
					}
					
					else
					{
						if(null != checkBoxList && !checkBoxList.isEmpty())
						{
							checkBoxList.remove(chkBox);
						}
					}
					
					
				}
				
				
			/*	if(null != checkBoxList && !checkBoxList.isEmpty())
				{
					if(checkBoxList.size() > 1)
					{
						String err = "Sorry you can not select more than one record";
						showErrorMessage(err);
					}
					
				}*/
			}
		});
	}
	
	
	
		
		/*if(null != compList && !compList.isEmpty())
		{
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
			}
		}*/
		//System.out.println("----------");
	
	public List<PreviousAccountDetailsDTO> getTableAllItems()
	{
		return (List<PreviousAccountDetailsDTO>)table.getItemIds();
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


	
	/*public List<CreateAndSearchLotTableDTO> getTableItems()
	{
		return tableDataList;
	}*/
	
	public void resetTableDataList()
	{
		if(null != tableDataList)
		{
			tableDataList.clear();
		}
		if(null != tableDataKeyList)
		{
			tableDataKeyList.clear();
		}
	}

	@Override
	public void tableSelectHandler(PreviousAccountDetailsDTO t) {
		
		
		if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(CreateRODDocumentDetailsPresenter.CREATE_ROD_POPULATE_PREVIOUS_ACCT_DETAILS,t);
		}
		else if(SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(FinancialHospitalizationPagePresenter.FINANCIAL_POPULATE_PREVIOUS_ACCT_DETAILS,t);
		}
		else if(SHAConstants.BILLING.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(BillingHospitalizationPagePresenter.BILLING_POPULATE_PREVIOUS_ACCT_DETAILS,t);
		}
		else if(SHAConstants.CREATE_LOT_PAYMENT.equalsIgnoreCase(presenterString))
		{
			//fireViewEvent(CreateAndSearchLotPresenter.CREATE_AND_LOT_POPULATE_PREVIOUS_ACCT_DETAILS,t,createBatchTableDTO);
		}
		else if(SHAConstants.CREATE_BATCH_PAYMENT.equalsIgnoreCase(presenterString))
		{
			//fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_POPULATE_PREVIOUS_ACCT_DETAILS,t,createBatchTableDTO);
		}else if(SHAConstants.ADD_ADDITIONAL_PAYMENT_INFO.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AddAditionalDocumentsPaymentInfoPagePresenter.PAYMENT_INFO_POPULATE_PREVIOUS_ACCT_DETAILS,t);
		}else if(SHAConstants.UPLOAD_NEFT_DETAILS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(UploadNEFTDetailsPresenter.UPLOAD_NEFT_DETAILS_POPULATE_PREVIOUS_ACCT_DETAILS,t);
		}else if(SHAConstants.PA_ADD_ADDITIONAL_PAYMENT_INFO.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(com.shaic.paclaim.addAdditionalDocPaymentInfo.search.AddAditionalDocumentsPaymentInfoPagePresenter.PAYMENT_INFO_POPULATE_PREVIOUS_ACCT_DETAILS,t);
//			fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_POPULATE_PREVIOUS_ACCT_DETAILS,t,editPaymentDetailsView);
		}else if(SHAConstants.OP_IFSC.equalsIgnoreCase(presenterString)){
			fireViewEvent(OPClaimAssessmentPagePresenter.OP_POPULATE_PREVIOUS_ACCT_DETAILS,t);
		}
		
		
		
		
	}
	
	public String isValidate()
	{
		String err = ""; 		
		
		
		
		List<PreviousAccountDetailsDTO> tableDataList = (List<PreviousAccountDetailsDTO>) table.getItemIds();
		
		List<PreviousAccountDetailsDTO> finalListForProcessing =  null;
		
	
		if(null != tableDataList && !tableDataList.isEmpty())
		{
			finalListForProcessing =  new ArrayList<PreviousAccountDetailsDTO>();
			for (PreviousAccountDetailsDTO previousAccountDetailsDTO : tableDataList) {
				
				Boolean chkBoxStatus = null != previousAccountDetailsDTO.getChkSelect() ?previousAccountDetailsDTO.getChkSelect() : false;
				
				//if(null != chkBox && true == chkBox.getValue())
				if(true == chkBoxStatus)
				{
					finalListForProcessing.add(previousAccountDetailsDTO);
				}
				
		 }
			if( null != finalListForProcessing && true == finalListForProcessing.isEmpty())
			{
				
				err = "Please select a record for further process";
				showErrorMessage(err);
			}
			
			if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
			{
				if(finalListForProcessing.size() > 1)
				{
					err = "Sorry you can not select more than one record";
					showErrorMessage(err);
					//clearCheckBoxValue();
				}
			}
		}
		
		
		//if(true == tableDataList.isEmpty())
		else
		{
			err = "No Previous Account Details available";
			showErrorMessage(err);
		}
		
		return err;
		
	}
	public void clearCheckBoxValue()
	{
		
		List<PreviousAccountDetailsDTO> tableList = (List<PreviousAccountDetailsDTO>) table.getItemIds();	
	
		if(null != tableList && !tableList.isEmpty())
		{
			for (PreviousAccountDetailsDTO previousAccountDetailsDTO : tableList) {
				
				chkBox.setValue(false);
				chkBox.setValue(null);
				previousAccountDetailsDTO.setChkSelect(false);
				previousAccountDetailsDTO.setChkSelect(null);
		
			}
		}
	}
	
	public void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
}



