/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.detailsPage;

import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
public interface ProcessRRCRequestDataExtractionWizard extends GMVPView, GWizardListener,WizardStep<PreauthDTO> {
	
	void buildSuccessLayout(String rrcRequestNo);
	
	void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value);

	void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value);

}
