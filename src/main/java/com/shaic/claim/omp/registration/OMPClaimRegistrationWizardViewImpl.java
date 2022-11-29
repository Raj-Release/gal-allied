package com.shaic.claim.omp.registration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.GMVPView;
import com.shaic.arch.utils.Props;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.TmpCPUCode;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
@Alternative
@SuppressWarnings("serial")
@Theme(Props.THEME_NAME)
public class OMPClaimRegistrationWizardViewImpl extends AbstractMVPView implements OMPClaimRegistrationWizardView, GMVPView{
	
    @Inject
	private Instance<OMPClaimRegistrationWizardForm> claimRegistrationInstance;	 
    
    
    private OMPClaimRegistrationWizardForm claimRegistration;
    
    private List<SublimitFunObject> resultSublimitList;
    
    private OMPSearchClaimRegistrationTableDTO searchResult;
    
    private NewIntimationDto intimationDto;
    
    private BeanFieldGroup<OMPBalanceSiTableDTO> binder;
    
    private OMPClaimRegistrationWizardForm  registrationPageObj;
	 
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}
	

	
	public void initView(OMPSearchClaimRegistrationTableDTO searchResult)
	{
		 setSizeFull();
	     claimRegistration = claimRegistrationInstance.get();
	     claimRegistration.init();
	     claimRegistration.initView(searchResult);         
	     setCompositionRoot(claimRegistration);
	}

	

	@Override
	public void submitClaimRegister(ClaimDto claimDto, Boolean isProceedFurther) {
			claimRegistration.submitRegistration(claimDto, isProceedFurther);
	}
	
	@Override
	public void registerClicked() {
		claimRegistration.registerClick();
	}
	
	
	@Override
	public void cancelClaimRegistration() {
		claimRegistration.cancelRegistration();
	}
	
	@Override
	public void resetView() {
		if(claimRegistration != null) {
			claimRegistration.init();
		}
		
	}


	@Override
	public void suggestRejectionClicked() {
		
		claimRegistration.suggestRejectionClick();
	}

	@Override
	public void setClaimDetails(ClaimDto claimDto) {
		
		claimRegistration.setClaimDetails(claimDto);
	}

	@Override
	public void setRegistrationDetails(
			OMPSearchClaimRegistrationTableDTO searchClaimRegistrationTableDto) {
		
		searchResult = searchClaimRegistrationTableDto;
	}

//	@Override
//	public void setCurrenceyMaster(BeanItemContainer<SelectValue> currencyMaster) {
//
//		claimRegistration.setCurrencyMaster(currencyMaster);
//	}

	@Override
	public void setPreviousClaims(List<OMPPreviousClaimTableDTO> previousClaimList) {
		claimRegistration.setPreviousClaimsDtoList(previousClaimList);
	}

	@Override
	public void setCpuObject(TmpCPUCode tmpCpuCode) {
		
		claimRegistration.setCpuObject(tmpCpuCode);
	}

//	@Override
//	public void setSublimt(List<SublimitFunObject> sublimitList) {
//		resultSublimitList = sublimitList;
//		claimRegistration.setSublimitList(resultSublimitList);		
//	}

	@Override
	public void setBalanceSumInsued(Double balanceSI) {
		claimRegistration.setBalanceSumInsured(balanceSI);
	}

	
//	public boolean onAdvance() {	
//		searchResult = claimRegistration.getSearchDTO();
//		return claimRegistration.validatePage();
//		
//	}
//	@Override
//	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
//		// TODO Auto-generated method stub
//		claimRegistration.buildRRCRequestSuccessLayout(rrcRequestNo);
//		
//	}

//	@Override
//	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
//		// TODO Auto-generated method stub
//		claimRegistration.buildValidationUserRRCRequestLayout(isValid);
//	}

//	@Override
//	public void loadRRCRequestDropDownValues(
//			BeanItemContainer<SelectValue> mastersValueContainer) {
//		// TODO Auto-generated method stub
//		claimRegistration.loadRRCRequestDropDownValues(mastersValueContainer);
//		
//	}
		
}