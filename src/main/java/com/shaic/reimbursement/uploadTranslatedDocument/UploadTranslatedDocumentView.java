package com.shaic.reimbursement.uploadTranslatedDocument;

import java.util.List;
import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fileUpload.FileUploadDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface UploadTranslatedDocumentView extends GMVPView {
	
	public void lists(List<FileUploadDTO> lists);

	public void setReferenceData(Map<String, Object> referenceData, NewIntimationDto intimationDto, FileUploadDTO fileUploadDto, SearchProcessTranslationTableDTO fileUploadDTO2, ClaimDto claimDto);

	public void result();
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
