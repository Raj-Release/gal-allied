package com.shaic.reimbursement.fraudidentification;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;


public interface FraudIdentificationView extends GMVPView{
	
	public void initView();
	public void loadParameterDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void generateTableForFraudIdentificationView(List<FraudIdentificationTableDTO> dtoList);
	public void buildSuccessLayout();
	public void buildFailureLayout(String message, String parameterType);
	public void insertNewRecord(FraudIdentificationTableDTO dto);
	public void showDetails(FraudIdentificationTableDTO fraudIdentificationTableDTO);
}