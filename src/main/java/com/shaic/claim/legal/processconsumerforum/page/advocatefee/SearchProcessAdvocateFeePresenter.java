package com.shaic.claim.legal.processconsumerforum.page.advocatefee;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.SearchProcessConsumerForumTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.LegalAdvocateFee;
import com.shaic.domain.LegalConsumer;

@ViewInterface(SearchProcessAdvocateFeeView.class)
public class SearchProcessAdvocateFeePresenter extends AbstractMVPPresenter<SearchProcessAdvocateFeeView>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SearchProcessAdvocateFeeService advocateFeeService;
	
	public static final String LEGAL_SAVE_ADVOCATE_FEE = "legal_save_advocate_fee";
	
	public static final String POPULATE_FIELD_VALUES_ADVOCATE_FEE = "Populate Field Values advocate fee";
	
	@EJB
	private ClaimService claimService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("static-access")
	public void save(@Observes @CDIEvent(LEGAL_SAVE_ADVOCATE_FEE) final ParameterDTO parameters) {
		AdvocateFeeDTO advocateFeeDTO = (AdvocateFeeDTO) parameters.getPrimaryParameter();
		if(advocateFeeDTO!=null){
			IntimationDetailsDTO intimationDetailsDTO = advocateFeeDTO.getIntimationDetailsDTO();
			if(intimationDetailsDTO!=null){
				if(intimationDetailsDTO.getIntimationNo()!=null){
					Claim claimsByIntimationNumber = claimService.getClaimsByIntimationNumber(intimationDetailsDTO.getIntimationNo());
					intimationDetailsDTO.setClaimKey(claimsByIntimationNumber);
				}
			}
		}
		advocateFeeService.saveLegalAdvocateFee(advocateFeeDTO);
		view.buildSuccessLayout();
		//System.out.println(consumerForumDTO);
	}
	
	@SuppressWarnings("unchecked")
	public void populateFieldValues(@Observes @CDIEvent(POPULATE_FIELD_VALUES_ADVOCATE_FEE) final ParameterDTO parameters) {
		SearchProcessConsumerForumTableDTO dto  = (SearchProcessConsumerForumTableDTO) parameters.getPrimaryParameter();
		
		if (dto != null) {
			LegalConsumer legalConsumer = advocateFeeService.getConsumerByIntimationNumber(dto.getIntimationNumber(),SHAConstants.LEGAL_CONSUMER);
			LegalAdvocateFee legalAdvocate =advocateFeeService.getLegalByIntimationNumberAndType(dto.getIntimationNumber() , SHAConstants.LEGAL_ADVOCATE_FEE);
			if(legalConsumer != null) {
				view.populateFiledValues(legalConsumer,legalAdvocate);
			}
			
		}
	}
	
}	
