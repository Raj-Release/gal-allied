package com.shaic.paclaim.convertClaimToReimbursement.search;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.ui.PAMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;


public class PASearchConvertReimbursementTable extends
GBaseTable<SearchConvertClaimTableDto> {

/**
* 
*/
private static final long serialVersionUID = 1L;

@EJB
private AcknowledgementDocumentsReceivedService ackDocReceivedService;

@EJB
private ClaimService claimService;

public static final Object[] VISIBLE_COL_ORDER = new Object[] { "serialNumber",
	"lob", "claimType", "cpuCode", "claimNumber", "insuredPatientName",
	"hospitalName", "dateOfAdmission", "claimStatus" };

@Override
public void removeRow() {
table.removeAllItems();

}

@Override
public void initTable() {
setSizeFull();
table.setContainerDataSource(new BeanItemContainer<SearchConvertClaimTableDto>(
		SearchConvertClaimTableDto.class));
table.setVisibleColumns(VISIBLE_COL_ORDER);

}

@Override
public void tableSelectHandler(SearchConvertClaimTableDto t) {


// VaadinSession session = getSession();
//	
//	Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
//	
//	if(! isActiveHumanTask){
//	
//		SHAUtils.setActiveOrDeactiveClaim(t.geiftUsername(),t.getPassword(),t.getTaskNumber(),session);
	if(t.getKey() != null){
		Claim claimByClaimKey = claimService.getClaimByClaimKey(t.getKey());
		if(ackDocReceivedService.getPADBTaskForPreauth(claimByClaimKey.getIntimation(), SHAConstants.REIM_CONVERSION_PROCESS_CURRENT_QUEUE)){
			getErrorMessage("Pending for Convert Claim (In process)");
		}else if(claimByClaimKey.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
			getErrorMessage("Claim is already closed");
		}else {
			fireViewEvent(PAMenuPresenter.SHOW_CONVERT_REIMBURSEMENT, t);
		}
	}else{
		fireViewEvent(PAMenuPresenter.SHOW_CONVERT_REIMBURSEMENT, t);
	}
	
	
		
		
//	}else{
//		getErrorMessage("This record is already opened by another user");
//	}


// fireViewEvent(MenuPresenter.SHOW_HOSPITAL, t.getKey());
}

@Override
public String textBundlePrefixString() {
return "convertClaimType-";
}

protected void tablesize(){
table.setPageLength(table.size()+1);
int length =table.getPageLength();
if(length>=7){
	table.setPageLength(7);
}

}

public void getErrorMessage(String eMsg){

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

