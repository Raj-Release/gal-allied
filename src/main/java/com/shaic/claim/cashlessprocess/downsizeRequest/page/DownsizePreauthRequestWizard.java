package com.shaic.claim.cashlessprocess.downsizeRequest.page;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.MasterRemarks;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface DownsizePreauthRequestWizard extends GMVPView {
	
	void buildSuccessLayout(String message);

	void setDiagnosisSumInsuredValuesFromDB(List<MedicalDecisionTableDTO> medicalDecisionTableList);
	
	void setDownsizeAmount(Double amount);

	void setHospitalizationDetails(Map<Integer, Object> hospitalizationDetails);

	void setBalanceSumInsured(Double balanceSI, List<Double> copayValue);

	void setWizardPageReferenceData(Map<String, Object> referenceData);

	void setDiagnosisSumInsuredValuesFromDB(
			Map<String, Object> medicalDecisionTableValue, String diagnosis);

	void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> icdCodeContainer);
	
	void setdownsizeRemarks(MasterRemarks remarks);
	
	void showErrorMessage();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);
	

}
