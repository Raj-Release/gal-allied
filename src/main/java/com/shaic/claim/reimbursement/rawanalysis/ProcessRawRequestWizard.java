package com.shaic.claim.reimbursement.rawanalysis;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessRawRequestWizard extends GMVPView{
	
	public void init(SearchProcessRawRequestTableDto searchDTO);
	
	public void setTableValues(List<RawInitiatedRequestDTO> intiatedDtls);
	
	public void setRepliedRawTableValues(List<RawInitiatedRequestDTO> repliedDtls);
	
	public void setResolutionData(BeanItemContainer<SelectValue> resolutionRaws);
	
	public void buildSuccessLayout();

}
