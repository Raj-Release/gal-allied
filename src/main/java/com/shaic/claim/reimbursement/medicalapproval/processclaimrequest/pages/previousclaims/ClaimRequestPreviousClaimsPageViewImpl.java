package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.previousclaims;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;

public class ClaimRequestPreviousClaimsPageViewImpl extends AbstractMVPView 
implements ClaimRequestPreviousClaimsPageInterface {

	private static final long serialVersionUID = 4534162762623368472L;

	private PreauthDTO bean;
	
	private GWizard wizard;
	
	@Inject
	private ClaimRequestPreviousClaimsPageUI previousClaimPage;
	
	/*	*/
	
	//private String strCaptionString;
	
	@Override
	public String getCaption() {
		return "Previous Claim Details";
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(PreauthDTO bean) {
		this.bean = bean;
		
	}
	
	@Override
	public void init(PreauthDTO bean, GWizard wizard) {
		this.bean = bean;
		this.wizard = wizard;
		
	}

	@Override
	public Component getContent() {
		previousClaimPage.init(bean,this.wizard);
		Component comp =  previousClaimPage.getContent();
		//setCompositionRoot(comp);
		fireViewEvent(ClaimRequestPreviousClaimsPagePresenter.MEDICAL_APPROVAL_PREVIOUS_CLAIMS_SETUP_REFERNCE, this.bean);
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		previousClaimPage.setupReferences(referenceData);
		
	}

	@Override
	public boolean onAdvance() {
		return previousClaimPage.validatePage();
	}

	@Override
	public boolean onBack() {
		bean.setAlertMessageOpened(false);
		bean.setDialysisOpened(false);
		bean.setIsDialysis(false);
		bean.setIsComparisonDone(false);
		bean.setIsBack(true);
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
             strCaptionString = tb.getText(textBundlePrefixString() + "documentdetails");
        }
	
	private String textBundlePrefixString()
	{
		return "medical-approval-";
	}*/

	@Override
	public void getPreviousClaimDetails(
			List<PreviousClaimsTableDTO> previousClaimDTOList) {
		previousClaimPage.setPreviousClaims(previousClaimDTOList);
	}

	@Override
	public void genertateFieldsBasedOnRelapseOfIllness(
			Map<String, Object> referenceData) {
		previousClaimPage.generateFieldsBasedOnRelapseOfIllness(referenceData);
	}

	@Override
	public void setPreviousClaimDetailsForPolicy(
			List<PreviousClaimsTableDTO> previousClaimDTOList) {
		previousClaimPage.setPreviousClaimDetailsForPolicy(previousClaimDTOList);
		
	}
	
	@Override
	public void generateApproveLayout() {
		
		previousClaimPage.generateButton(10, null);
		
	}
	
	@Override
	public void generateCancelRodLayout() {
		previousClaimPage.generateButton(11, null);
		
	}
	
	@Override
	public void generateQueryLayout() {
		previousClaimPage.generateButton(8, null);
		
	}

	@Override
	public void generateRejectionLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		previousClaimPage.generateButton(9, selectValueContainer);
		
	}

	@Override
	public void generateEscalateLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		previousClaimPage.generateButton(6, selectValueContainer);
		
	}

	@Override
	public void generateEscalateReplyLayout() {
		previousClaimPage.generateButton(5, null);
		
	}
	
	@Override
	public void generateReferCoOrdinatorLayout(
			BeanItemContainer<SelectValue> selectValueContainer) {
		previousClaimPage.generateButton(4, selectValueContainer);
		
	}
	
	@Override
	public void genertateSpecialistLayout(
			BeanItemContainer<SelectValue> selectValueContainerForSpecialist) {
		previousClaimPage.generateButton(7, selectValueContainerForSpecialist);
		
	}

	@Override
	public void genertateSentToReplyLayout() {
		previousClaimPage.generateButton(1, null);
	}
	

}
