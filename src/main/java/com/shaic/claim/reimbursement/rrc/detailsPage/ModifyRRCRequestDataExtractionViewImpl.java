/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.detailsPage;

/**
 * @author ntv.vijayar
 *
 */


import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.vaadin.ui.Component;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
public class ModifyRRCRequestDataExtractionViewImpl extends AbstractMVPView 
implements ModifyRRCRequestDataExtractionView {
	
	@Inject
	private ModifyRRCRequestDataExtractionPage documentDetailsPage;
	
	private RRCDTO bean;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(RRCDTO bean) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCaption() {
		//return "Document Details";
		return "Modify RRC Request";
	}

	@Override
	public Component getContent() {
		
		Component comp =  documentDetailsPage.getContent();
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		if(documentDetailsPage.validatePage())
		{
			documentDetailsPage.setTableValuesToDTO();
			return true;
		}
		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		if(documentDetailsPage.validatePage())
		{
			documentDetailsPage.setTableValuesToDTO();
			return true;
		}
		return false;
	}

	@Override
	public void init(RRCDTO bean, GWizard wizard) {
		this.bean = bean;
		documentDetailsPage.initPresenter(SHAConstants.MODIFY_RRC_REQUEST);
		documentDetailsPage.init(this.bean);
		
	}

	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,
			GComboBox subCategory, SelectValue value) {
		documentDetailsPage.setsubCategoryValues(selectValueContainer, subCategory, value);
		
	}

	@Override
	public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,
			GComboBox source, SelectValue value) {
		
		documentDetailsPage.setsourceValues(selectValueContainer, source, value);
	}
	
	@Override
	public void invalidate(){
		documentDetailsPage.invalidate();
	}

}
