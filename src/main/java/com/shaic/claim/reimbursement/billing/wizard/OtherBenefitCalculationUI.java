package com.shaic.claim.reimbursement.billing.wizard;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.OtherBenefitsTableDto;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class OtherBenefitCalculationUI extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ReimbursementOtherBenifitsTable otherBenefitCalculationTable;
	
	private PreauthDTO bean;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	public void initView(PreauthDTO bean) 
	{	
		this.bean = bean;
		otherBenefitCalculationTable.init(bean);
		List<OtherBenefitsTableDto> otherBenefitTableDtoList = bean.getPreauthMedicalDecisionDetails().getOtherBenefitTableDtoList();
		
		Boolean isPreferred = false;
		
		if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
				|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			if(bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital() != null 
					&& bean.getNewIntimationDTO().getHospitalDto().getIsPreferredHospital().equalsIgnoreCase("Y")){
				isPreferred = true;
			}
			
		}
		if(otherBenefitTableDtoList != null && ! otherBenefitTableDtoList.isEmpty()){
			for (OtherBenefitsTableDto otherBenefitsTableDto : otherBenefitTableDtoList) {
				otherBenefitsTableDto.setNoOfdaysLimit(bean.getNoOfdaysLimit());
				otherBenefitsTableDto.setIsPreferred(isPreferred);
				if(otherBenefitsTableDto.getBenefitName().equalsIgnoreCase(SHAConstants.treatementForPreferred)){
					if(! isPreferred){
						otherBenefitsTableDto.setEligibleAmt(0d);
					}
				}
				otherBenefitCalculationTable.addBeanToList(otherBenefitsTableDto);
			}
		}
		VerticalLayout mainHor = new VerticalLayout(otherBenefitCalculationTable);
		
		mainHor.setCaption("Other Benefit Calculation");
		setCompositionRoot(mainHor);
		
	}
	
	public TextField getDummyFieldForLisener(){
		return otherBenefitCalculationTable.dummyField;
	}
	
	public void setOtherBenefitsValues(){
		if(otherBenefitCalculationTable != null){
			List<OtherBenefitsTableDto> values = otherBenefitCalculationTable.getValues();
		    if(this.bean != null && values != null && ! values.isEmpty() && this.bean.getPreauthDataExtractionDetails() != null){
		    	this.bean.getPreauthDataExtractionDetails().setOtherBenfitOpt(true);
		    	this.bean.getPreauthDataExtractionDetails().setOtherBenefitsList(values);
		    }
			
		}
		
	}

}
