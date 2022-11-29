package com.shaic.claim.withdrawPostProcessWizard;
import java.util.List;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.preauth.MasterRemarks;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface WithdrawPreauthPostProcessWizard extends GMVPView,WizardStep<PreauthDTO> {

	void buildSuccessLayout();

	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

	void buildWithdrawFields(BeanItemContainer<SelectValue> selectValueContainer);

	void buildWithdrawAndRejctFields(
			BeanItemContainer<SelectValue> selectValueContainer,
			BeanItemContainer<SelectValue> selectValueContainer2);

	void setRemarks(MasterRemarks remarks, String decision);
	
	void setUploadDTOBillEntryDtls(UploadDocumentDTO uploadDocDto);
	void setUpCategoryValues(BeanItemContainer<SelectValue> selectValueContainer);
	void setBillEntryFinalStatus(UploadDocumentDTO uploadDocDto);
	void setBillClassificationBillEntries(List<UploadDocumentDTO> uploadDocDto);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);


}


