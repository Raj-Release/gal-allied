package com.shaic.claim.negotiation;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.MedicalDecisionTableDTO;
import com.shaic.domain.preauth.ExclusionDetails;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessNegotiationWizard extends GMVPView{

	void buildSuccessLayout(String message);
	
	void setDiagnosisSumInsuredValuesFromDB(List<MedicalDecisionTableDTO> medicalDecisionTableList);
	
	void setDownsizeAmount(Double amount);

	void setNegotiationHospitalizationDetails(Map<Integer, Object> hospitalizationDetails);

	void setBalanceSumInsured(Double balanceSI, List<Double> copayValue);

	void setWizardPageReferenceData(Map<String, Object> referenceData);

	void setDiagnosisSumInsuredValuesFromDB(
			Map<String, Object> medicalDecisionTableValue, String diagnosis);

	void setExclusionDetails(
			BeanItemContainer<ExclusionDetails> icdCodeContainer);
	
	void showErrorMessage();
	
	void buildNegotiationRRCRequestSuccessLayout(String rrcRequestNo);
	void buildNegotiationValidationUserRRCRequestLayout(Boolean isValid);
	void loadNegotiationRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	
	void viewClaimAmountDetails();
	
	void viewBalanceSumInsured(String intimationId);
	
	void setReferenceDetailsForMedicalDecision(Map<String, Object> referenceData);
	
	 void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
