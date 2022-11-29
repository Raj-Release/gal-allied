package com.shaic.claim.reports.medicalAuditCashlessIssueanceReport;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.ClaimService;
@ViewInterface(MedicalAuditCashlessIssuanceReportView.class)
public class MedicalAuditCashlessIssuanceReportPresenter extends
AbstractMVPPresenter<MedicalAuditCashlessIssuanceReportView> {

	public static final String SEARCH_MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT = "Search Medical Audit Cashless Issuance Details Report";
	
	@EJB
	private ClaimService claimService;
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_MEDICAL_AUDIT_CASHLESS_ISSUANCE_REPORT) final ParameterDTO parameters)
	    {
		   Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		   
		   List<MedicalAuditCashlessIssuanceReportDto> medicalAuditCashlessIssuanceListDto = (List<MedicalAuditCashlessIssuanceReportDto>) claimService.getMedicalAuditCashlessIssuanceDetails(searchFilter);
		    
		   view.medicalAuditClaimStatusReportDetailsView(medicalAuditCashlessIssuanceListDto);
	    }
	

}
