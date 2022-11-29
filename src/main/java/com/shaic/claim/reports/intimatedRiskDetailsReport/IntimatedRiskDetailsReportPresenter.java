package com.shaic.claim.reports.intimatedRiskDetailsReport;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.IntimationService;
import com.shaic.domain.reimbursement.ReimbursementService;
@ViewInterface(IntimatedRiskDetailsReportView.class)
public class IntimatedRiskDetailsReportPresenter extends
AbstractMVPPresenter<IntimatedRiskDetailsReportView> {

	public static final String SEARCH_INTIMATED_RISK_DETAILS_REPORT = "Search Intimated Risk Details Report";
	
	@EJB
	private IntimationService intimationservice;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_INTIMATED_RISK_DETAILS_REPORT) final ParameterDTO parameters)
	    {
		   Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		   
		   List<IntimatedRiskDetailsReportDto> intimatedRiskListDto = (List<IntimatedRiskDetailsReportDto>) intimationservice.getIntimatedRiskDetails(searchFilter);
		    
		   view.intimatedRiskReportDetailsView(intimatedRiskListDto);
	    }
	

}
