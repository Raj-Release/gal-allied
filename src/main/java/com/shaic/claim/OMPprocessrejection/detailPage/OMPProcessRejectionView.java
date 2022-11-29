package com.shaic.claim.OMPprocessrejection.detailPage;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPProcessRejectionView extends GMVPView{
	
	public void generateFieldBasedOnConfirmClick(BeanItemContainer<SelectValue> selectedValue);
	public void generateFieldBasedOnWaiveClick(Boolean isChecked);
	public void setReferenceData(SearchProcessRejectionTableDTO searchDTO);
	public void savedResult();
//	public void openPdfFileInWindow(Claim claim, PreauthDTO preauthDTO);
	
}
