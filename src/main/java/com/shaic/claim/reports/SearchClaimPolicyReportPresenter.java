package com.shaic.claim.reports;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.reimbursement.ReimbursementService;
@ViewInterface(SearchClaimPolicyReportView.class)
public class SearchClaimPolicyReportPresenter extends
AbstractMVPPresenter<SearchClaimPolicyReportView> {

	public static final String SEARCH_CLM_POLICY_WISE = "Search Claim Policy Wise";
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ClaimsReportService reportService;
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_CLM_POLICY_WISE) final ParameterDTO parameters)
	    {
		   String policyNo = (String) parameters.getPrimaryParameter();
		   String userName=(String)parameters.getSecondaryParameter(0, String.class);
//		   List<PolicywiseClaimReportDto> claimReportDto = (List<PolicywiseClaimReportDto>) reimbursementService.getClaimsPolicyWise(policyNo);
		   List<PolicywiseClaimReportDto> claimReportDto = (List<PolicywiseClaimReportDto>) reportService.getClaimsPolicyWise(policyNo,userName);
		   view.searchClaiPolicymwise(claimReportDto);
	    }
	

}
