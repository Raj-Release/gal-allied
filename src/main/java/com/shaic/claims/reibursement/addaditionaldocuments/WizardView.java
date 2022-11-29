/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

/**
 * @author ntv.srikanthp
 *
 */
public interface WizardView  extends GMVPView, GWizardListener {
	void buildSuccessLayout();
	/*void buildRRCRequestSuccessLayout(String rrcRequestNo);
	void buildValidationUserRRCRequestLayout(Boolean isValid);
	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);*/
	//void setWizardPageReferenceData(Map<String,Object> referenceData);
}
