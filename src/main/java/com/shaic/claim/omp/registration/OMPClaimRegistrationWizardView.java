package com.shaic.claim.omp.registration;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationTableDTO;
import com.shaic.domain.TmpCPUCode;


public interface OMPClaimRegistrationWizardView extends GMVPView{
	public void initView(OMPSearchClaimRegistrationTableDTO searchResult);
//	void setCurrenceyMaster(BeanItemContainer<SelectValue> currencyMaster);
	void setPreviousClaims(List<OMPPreviousClaimTableDTO> previousClaimList);
	void setBalanceSumInsued(Double balanceSI);
	void setCpuObject(TmpCPUCode tmpCpuCode);
	void submitClaimRegister(ClaimDto claimDto, Boolean isProceedFurther);
    void registerClicked();
//    public boolean onAdvance();
    void suggestRejectionClicked();
    void cancelClaimRegistration();
	void setClaimDetails(ClaimDto claimDto);
	void setRegistrationDetails(OMPSearchClaimRegistrationTableDTO searchClaimRegistrationTableDto);
//	void setDataField(OMPSearchClaimRegistrationTableDTO bean,
//			NewIntimationDto intimationDto);
//	void setSublimt(List<SublimitFunObject> sublimitList);

//	void buildRRCRequestSuccessLayout(String rrcRequestNo);
//	void buildValidationUserRRCRequestLayout(Boolean isValid);
//	void loadRRCRequestDropDownValues(BeanItemContainer<SelectValue> mastersValueContainer);

}
