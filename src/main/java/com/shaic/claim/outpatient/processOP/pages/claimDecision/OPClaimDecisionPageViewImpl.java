package com.shaic.claim.outpatient.processOP.pages.claimDecision;

import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.processOPpages.ConsultationTabPage;
import com.shaic.claim.outpatient.processOPpages.DiagnosticTabChange;
import com.shaic.claim.outpatient.processOPpages.MedicineTabPage;
import com.shaic.claim.outpatient.processOPpages.PhysioTherapyTabPage;
import com.shaic.claim.outpatient.processOPpages.ProcessOPClaimDetailsPage;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.vaadin.ui.Component;

public class OPClaimDecisionPageViewImpl extends AbstractMVPView implements OPClaimDecisionPageInterface {

	private static final long serialVersionUID = 5576743706797691367L;
	
	private OutPatientDTO bean;
	
	private GWizard wizard;
	
//	@Inject
//	private OPClaimDecisionPageUI claimAndDocumentPage; // File To be Removed....
	
	@Inject
	private ProcessOPClaimDetailsPage claimAndDocumentPage;
	
	@Inject
	private ConsultationTabPage consultationTabPage;
	
	@Inject
	private DiagnosticTabChange diagnosticTabChange;
	
	@Inject
	private PhysioTherapyTabPage physioTherapyTabPage;
	
	@Override
	public void resetView() {
		init(bean, this.wizard);
	}

	@Override
	public String getCaption() {
		return "Claim Details";
	}
	
	@Override
	public void init(OutPatientDTO bean) {
		this.bean = bean;
	}

	@Override
	public Component getContent() {
		Component comp =  claimAndDocumentPage.getContent();
		//fireViewEvent(OPClaimDecisionPagePresenter.SET_UP_REFERENCE, bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		//claimAndDocumentPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		if(claimAndDocumentPage.validatePage()){
			claimAndDocumentPage.showErrorMessage(claimAndDocumentPage.getErrMsg().toString());
			return false;
		}else{
			claimAndDocumentPage.setTabSheetValues();
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
		consultationTabPage.initView(bean);
		diagnosticTabChange.initView(bean);
		physioTherapyTabPage.initView(bean);
		
	}
//	@Override
//	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO) {
//		// TODO Auto-generated method stub
//		claimAndDocumentPage.setUpIFSCDetails(viewSearchCriteriaDTO);
//
//	}

	@Override
	public void setHospitalDetails(HospitalDto dto) {
		// 
		consultationTabPage.setHospitalDetails(dto);
	}
	
	public void setDiagnosticHospitalDetails(HospitalDto dto) {
		// 
		diagnosticTabChange.setHospitalDetails(dto);
	}
	
	public void clearTable(){
		consultationTabPage.clearTable();
	}

}
