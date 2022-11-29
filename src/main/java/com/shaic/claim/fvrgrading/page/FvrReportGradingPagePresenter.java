package com.shaic.claim.fvrgrading.page;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingTableDto;
import com.shaic.domain.MasterService;
import com.shaic.domain.reimbursement.ReimbursementService;

@ViewInterface(FvrReportGradingPageView.class)
public class FvrReportGradingPagePresenter extends AbstractMVPPresenter<FvrReportGradingPageView> {
	
	/**
	 * 
	 */
		
	public static final String SUBMIT_FVR_GRADING="Submit Fvr Grading";
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private MasterService masterService;
	
	

//	public void setUpReference(@Observes @CDIEvent(SUBMIT_EVENT) final ParameterDTO parameters)
//	{
//		ConvertClaimDTO bean=(ConvertClaimDTO)parameters.getPrimaryParameter();
//		
//		SearchConvertClaimTableDto searchFormDto=(SearchConvertClaimTableDto) parameters.getSecondaryParameter(0,SearchConvertClaimTableDto.class);
//
//		Boolean result=claimService.saveConversionReason(bean,"submit",searchFormDto);
//		
//		if(result){
//			view.result();
//		}
//	}

	public void setUpconversion(@Observes @CDIEvent(SUBMIT_FVR_GRADING) final ParameterDTO parameters)
	{
		
		FvrReportGradingPageDto bean=(FvrReportGradingPageDto)parameters.getPrimaryParameter();
		
		SearchFvrReportGradingTableDto searchFormDto=(SearchFvrReportGradingTableDto) parameters.getSecondaryParameter(0,SearchFvrReportGradingTableDto.class);
				
		Boolean result=reimbursementService.submitFVRGradingDetail(bean, searchFormDto);
		
		if(result){
			view.result();
		}
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	

}
