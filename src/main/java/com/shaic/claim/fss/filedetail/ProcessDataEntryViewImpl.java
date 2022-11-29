/**
 * 
 */
package com.shaic.claim.fss.filedetail;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.domain.PDIntimationPremia;
import com.vaadin.ui.Component;

/**
 * 
 *
 */
public class ProcessDataEntryViewImpl extends AbstractMVPView 
implements ProcessDataEntryView {
	
	@Inject
	private ProcessDataEntryPage dataEnryPage;
	
	private SearchDataEntryTableDTO bean;

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(SearchDataEntryTableDTO bean) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCaption() {
		//return "Document Details";
		return "File Storage Data Entry";
	}

	@Override
	public Component getContent() {
		
		Component comp =  dataEnryPage.getContent();
		fireViewEvent(SetDataEntryPresenter.CHEQUE_DETAILS_SETUP_REFERENCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		dataEnryPage.setupReferences();
	}

	@Override
	public boolean onAdvance() {
		if(dataEnryPage.validatePage())
		{
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
		if(dataEnryPage.validatePage())
		{
			return true;
		}
		return false;
	}

	@Override
	public void init(SearchDataEntryTableDTO bean, GWizard wizard) {
		this.bean = bean;
		dataEnryPage.initPresenter(SHAConstants.ADD_DATA_ENTRY);
		dataEnryPage.init(this.bean);
		
	}

	@Override
	public void setTableValuesToDTO() {
		dataEnryPage.setTableValuesToDTO();
		
	}

	@Override
	public void setUpListenerContainer(Map<String, Object> containerMap) {
		// TODO Auto-generated method stub
		dataEnryPage.setSelectValueListener(containerMap);
	}

	@Override
	public void setPremiaIntimation(PDIntimationPremia intimation) {
		dataEnryPage.setPremiaIntimation(intimation);
	}

}
