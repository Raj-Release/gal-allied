package com.shaic.claim.processRejectionPage;

import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.domain.Claim;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface ProcessRejectionView extends GMVPView, WizardStep<ProcessRejectionDTO> {
	
	public void generateFieldBasedOnConfirmClick(BeanItemContainer<SelectValue> selectedValue);
	public void generateFieldBasedOnWaiveClick(Boolean isChecked);
	public void setReferenceData(SearchProcessRejectionTableDTO searchDTO, NewIntimationDto intimationDto);
	public void savedResult();
	public void openPdfFileInWindow(Claim claim, PreauthDTO preauthDTO);
	
	void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);
	
	 void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);
	 
	 void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
