package com.shaic.claim.userproduct.document;



import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.userproduct.document.search.SearchDoctorDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;


public interface ProductAndDocumentTypeView extends GMVPView {
	
	void init(ProductAndDocumentTypeDTO productDocTypeDto, BeanItemContainer<SelectValue> selectValueContainer, BeanItemContainer<SelectValue> userTypeContainer);
	
	public void setDoctorDetails(SearchDoctorDetailsTableDTO viewSearchCriteriaDTO);
	
	//public void generateFieldsBasedOnClick(Boolean value);
	
	void submitValues();

	void setUpReference(ProductAndDocumentTypeDTO reopenClaimDTO);
}
