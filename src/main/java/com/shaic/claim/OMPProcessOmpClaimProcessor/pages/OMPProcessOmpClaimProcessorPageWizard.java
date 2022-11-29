package com.shaic.claim.OMPProcessOmpClaimProcessor.pages;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;

public interface OMPProcessOmpClaimProcessorPageWizard extends GMVPView {
	
	void buildSuccessLayout();
	void buildSaveLayout();
	void init(OMPClaimProcessorDTO bean, BeanItemContainer<SelectValue> classification, BeanItemContainer<SelectValue> subClassification, 
			BeanItemContainer<SelectValue> paymentTo, BeanItemContainer<SelectValue> paymentMode, BeanItemContainer<SelectValue> eventCode, 
			BeanItemContainer<SelectValue> currencyValue, BeanItemContainer<SelectValue> negotiatorName, BeanItemContainer<SelectValue> modeOfReciept, 
			BeanItemContainer<SelectValue> documentRecievedFrom, BeanItemContainer<SelectValue> documentType, BeanItemContainer<SelectValue> country);

	void generateFieldsOnNegotiate(HorizontalLayout horizontalLayout, BeanItemContainer<SelectValue> negotiatorName);

	void generateFieldsForRejection(HorizontalLayout horizontalLayout);

	void generateFieldsOnApproval(HorizontalLayout horizontalLayout);

	void setReferenceDate(Map<String, Object> referenceDataMap);
	void cancelIntimation();
	void generateFieldsOnApprove(
			OMPClaimCalculationViewTableDTO calculationViewTableDTO);
	void generateFieldsOnRejectionRemark(
			OMPClaimCalculationViewTableDTO calculationViewTableDTO);

}
