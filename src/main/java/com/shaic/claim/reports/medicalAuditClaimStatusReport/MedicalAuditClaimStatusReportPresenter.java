package com.shaic.claim.reports.medicalAuditClaimStatusReport;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.domain.reimbursement.ReimbursementService;
@ViewInterface(MedicalAuditClaimStatusReportView.class)
public class MedicalAuditClaimStatusReportPresenter extends
AbstractMVPPresenter<MedicalAuditClaimStatusReportView> {

	public static final String SEARCH_MEDICAL_AUDIT_CLAIM_DETAILS_REPORT = "Search Medical Audit Claim Status Details Report";
	
	@EJB
	private ReimbursementService reimbservice;
	
	
	
	@Override
	public void viewEntered() {
				
	}
	
	 protected void showClaimPolicySearch(@Observes @CDIEvent(SEARCH_MEDICAL_AUDIT_CLAIM_DETAILS_REPORT) final ParameterDTO parameters)
	    {
		   Map<String,Object> searchFilter = (Map<String,Object>) parameters.getPrimaryParameter();
		   
		   List<MedicalAuditClaimStatusReportDto> medicalAuditClaimStatusReportListDto = (List<MedicalAuditClaimStatusReportDto>) reimbservice.getMedicalAuditClaimStatusDetails(searchFilter);
		    
		   view.medicalAuditClaimStatusReportDetailsView(medicalAuditClaimStatusReportListDto);
	    }
	

}
