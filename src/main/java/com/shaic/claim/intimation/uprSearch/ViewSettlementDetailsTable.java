package com.shaic.claim.intimation.uprSearch;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationAndViewDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentModeTrailTable;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTrailTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PaymentModeTrail;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class ViewSettlementDetailsTable extends GBaseTable<PaymentProcessCpuPageDTO>{
	
	private final static Object COLUM_HEADER_NEFT_DETAILS[] = new Object[] {
		"accNumber", "ifscCode","bankName", "bankBranchName","payeeName","settledAmount"};
	
	private final static Object COLUM_HEADER_CHEQUE_DD_DETAILS[] = new Object[] {
		"ddNo", "ddDateValue", "settledAmount", "payableAt"};	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		BeanItemContainer<PaymentProcessCpuPageDTO> paymentDtoContainer = new BeanItemContainer<PaymentProcessCpuPageDTO>(PaymentProcessCpuPageDTO.class);
	
	    table.setContainerDataSource(paymentDtoContainer);
	    table.setVisibleColumns(COLUM_HEADER_CHEQUE_DD_DETAILS);
	    table.setPageLength(table.size()+1);
	    
		
	}

	@Override
	public void tableSelectHandler(PaymentProcessCpuPageDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "settlementdtls-";
	}
	
	@SuppressWarnings("deprecation")
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(3);
		}
		
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

	public void setNeftColumnHeaders() {
		table.setVisibleColumns(COLUM_HEADER_NEFT_DETAILS);
		setColumHeadings();
	}
	
	public void setCheqDDColumnHeaders() {
		table.setVisibleColumns(COLUM_HEADER_CHEQUE_DD_DETAILS);
		setColumHeadings();
	}
	
	public void setColumHeadings() {
		
		table.setColumnHeader("accNumber", "Account No");
		table.setColumnHeader("ifscCode", "IFSC Code");
		table.setColumnHeader("bankName", "Bank Name");
		table.setColumnHeader("bankBranchName", "Branch Name");
		table.setColumnHeader("payeeName", "Payee Name");
		table.setColumnHeader("settledAmount", "Payable Amount");
		table.setColumnHeader("settledAmount", "Payable Amount");
		table.setColumnHeader("ddNo", "Cheque No");
		table.setColumnHeader("ddDateValue", "Cheque Date");
		table.setColumnHeader("payableAt", "Payable At");						
	}
}