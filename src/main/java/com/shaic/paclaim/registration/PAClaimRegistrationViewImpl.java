package com.shaic.paclaim.registration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.Props;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.domain.SublimitFunObject;
import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
@Theme(Props.THEME_NAME)
public class PAClaimRegistrationViewImpl extends AbstractMVPView implements PAClaimRegistrationView, GMVPView{
	    
    @Inject
    private PAClaimRegistration claimRegistration;
    
    private List<SublimitFunObject> resultSublimitList;
    
    private SearchClaimRegistrationTableDto searchResult;
	 
	@PostConstruct
	public void init() {
		addStyleName("view");
	        
	}
	
	public void initView(SearchClaimRegistrationTableDto searchResult)
	{
		 setSizeFull();
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
			SearchClaimRegistrationTableDto searchClaimRegistrationTableDto) {
		searchResult = searchClaimRegistrationTableDto;
	}

	@Override
	public void setCurrenceyMaster(BeanItemContainer<SelectValue> currencyMaster) {

		claimRegistration.setCurrencyMaster(currencyMaster);
	}

	@Override
	public void setPreviousClaims(List<PreviousClaimsTableDTO> previousClaimList) {
		claimRegistration.setPreviousClaimsDtoList(previousClaimList);
	}

//	@Override
//	public void setCpuObject(TmpCPUCode tmpCpuCode) {
//		
//		claimRegistration.setCpuObject(tmpCpuCode);
//	}

	@Override
	public void setSublimt(List<SublimitFunObject> sublimitList) {
		resultSublimitList = sublimitList;
		claimRegistration.setSublimitList(resultSublimitList);		
	}

	@Override
	public void init(BeanItemContainer<SelectValue> selectValueForCategory) {
		// TODO Auto-generated method stub
		claimRegistration.setDropDownValues(selectValueForCategory);
	}

	
	
//	@Override
//	public void setBalanceSumInsued(Double balanceSI) {
//		claimRegistration.setBalanceSumInsured(balanceSI);
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