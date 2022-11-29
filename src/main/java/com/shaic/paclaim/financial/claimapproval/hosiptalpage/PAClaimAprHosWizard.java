package com.shaic.paclaim.financial.claimapproval.hosiptalpage;

import java.util.List;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.zybnet.autocomplete.server.AutocompleteField;

public interface PAClaimAprHosWizard extends GMVPView, GWizardListener {
	void buildSuccessLayout();
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void loadEmployeeMasterData(AutocompleteField<EmployeeMasterDTO> field , List<EmployeeMasterDTO> employeeDetailsList);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void buildFailureLayout();
}
