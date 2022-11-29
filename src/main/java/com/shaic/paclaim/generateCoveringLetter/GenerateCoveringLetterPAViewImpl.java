package com.shaic.paclaim.generateCoveringLetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.registration.GenerateCoveringLetterSearchTableDto;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.newcode.wizard.IWizardPartialComplete;
import com.vaadin.sass.internal.util.StringUtil;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class GenerateCoveringLetterPAViewImpl extends AbstractMVPView implements GenerateLetterPAClaimView 
{

	@Inject private GenerateCoveringLetterPADetailUI coveringLetterDraftInstance;
	
	@Inject 
	private ConfirmPACoveringLetterViewImpl coveringLetterDetailsUI;
    
	@Inject private Instance<MVPView> views;
	
	@EJB 
	private IntimationService intimationService;
	@EJB 
	private ClaimService claimService;
	@EJB 
	private PolicyService policyService;
	
	@Inject
	private TextBundle tb;	
	
	private String strCaptionString;
	
	private IWizardPartialComplete wizard;
	
	private GenerateCoveringLetterSearchTableDto bean;
	
	public void init(GenerateCoveringLetterSearchTableDto bean, GWizard wizard){
		
		localize(null);
		this.bean = bean;
		coveringLetterDraftInstance.init(bean,wizard);
	}	
		
	
		@PostConstruct
		public void initView() {
		
		}

		@Override
		public String getCaption() {
			return strCaptionString;
		}
		
		@Override
		public void setupReferences(Map<String, Object> referenceData) {
			
		}

		@Override
		public boolean onAdvance() {
			
			boolean navigationAllowed = coveringLetterDraftInstance.validatePage();
			
			return navigationAllowed;
			
		}

		@Override
		public boolean onBack() {
			
			return true;
		}

		@Override
		public boolean onSave() {
			return false;
		}

		protected void localize(
	            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
	        strCaptionString = tb.getText(textBundlePrefixString() + "decideonpacovering");
	    }
	    
		private String textBundlePrefixString()
		{
			return "pacoveringletter-";
		}
		
		public void generateCoveringLetter(ClaimDto claimDto)
		{
			
			
			String hosptialName = claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().getName();
			
			if(hosptialName != null && !("").equalsIgnoreCase(hosptialName)){
				
//				hosptialName.replace("&", "and");
				
				hosptialName = StringUtil.replaceSubString(hosptialName, "&", "and");
				claimDto.getNewIntimationDto().getHospitalDto().getRegistedHospitals().setName(hosptialName);
			}
			
			List<ClaimDto> claimDtoList = new ArrayList<ClaimDto>();
			claimDtoList.add(claimDto);
			DocumentGenerator docGenarator = new DocumentGenerator();
			String fileUrl = null;
			ReportDto reportDto = new ReportDto();
			reportDto.setClaimId(claimDto.getClaimId());
			reportDto.setBeanList(claimDtoList);
			
			fileUrl = docGenarator.generatePdfDocument("ClaimFormCoveringLetter", reportDto);
			
			if(!ValidatorUtils.isNull(fileUrl))
			{
							
			 claimService.updateClaim(claimDto.getKey());
			//claimService.create(claim);
			 
			 
//			 coveringLetterDetailsUI = coveringLetterDetailsUIInstance.get();
//					 coveringLetterDetailsUI.openPdfFileInWindow(fileUrl);
			}
			else
			{
				//Exception while PDF Letter Generation
			}	
			
		}


		@Override
		public void resetView() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void init(GenerateCoveringLetterSearchTableDto bean) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void getUpdatedBean() {
			coveringLetterDraftInstance.validatePage();
		}

		@Override
		public Component getContent() {
			
			return coveringLetterDraftInstance.getWoleLayout();
			
			
		}

}
