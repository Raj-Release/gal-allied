package com.shaic.claim.outpatient.processOP.pages.settlement;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.outpatient.processOPpages.ProcessOPSettlementPage;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.vaadin.ui.Component;

public class OPClaimSettlementPageViewImpl extends AbstractMVPView implements OPClaimSettlementPageInterface {

	private static final long serialVersionUID = 5576743706797691367L;
	
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
//	@Inject
//	private OPClaimSettlementPageUI claimAndDocumentPage;
	
	@Inject
	private ProcessOPSettlementPage claimAndDocumentPage;
	
	@Override
	public void resetView() {
		init(bean, this.wizard);
	}

	@Override
	public String getCaption() {
		return "Settlement";
	}
	
	@Override
	public void init(OutPatientDTO bean) {
		this.bean = bean;
		
	}

	@Override
	public Component getContent() {
		Component comp =  claimAndDocumentPage.getContent();
		//setCompositionRoot(comp);
//		fireViewEvent(OPClaimAssessmentPagePresenter.SET_UP_REFERENCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
//		claimAndDocumentPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		if(claimAndDocumentPage.validatePage()){
			claimAndDocumentPage.showErrorMessage(claimAndDocumentPage.getErrMsg().toString());
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean onBack() {
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(OutPatientDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		claimAndDocumentPage.init(bean, wizard);
		
	}

	@Override
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO) {
		claimAndDocumentPage.setUpIFSCDetails(viewSearchCriteriaDTO);		
	}

	@Override
	public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO) {
		claimAndDocumentPage.populatePreviousPaymentDetails(tableDTO);
	}

}
