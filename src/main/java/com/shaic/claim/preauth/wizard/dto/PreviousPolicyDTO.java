package com.shaic.claim.preauth.wizard.dto;

import java.util.List;

import javax.ejb.EJB;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.registration.previousinsurance.view.PreviousInsuranceInsuredDetailsTableDTO;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreviousPolicy;
import com.shaic.ims.bpm.claim.BPMClientContext;

public class PreviousPolicyDTO {
	
	private String previousInsurerName;
	
	private String policyNumber;
	
	private String policyFromDate;
	
	private String policyToDate;
	
	private Long underWritingYear;
	
	private Double sumInsured;
	
	private String productName;
	
	private String policyScheduleUrl;
	
	private List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuredDetails;
	
	@EJB
	private IntimationService intimationService;
	
	public PreviousPolicyDTO(PreviousPolicy previousPolicy) {
		this.previousInsurerName = previousPolicy.getPreviousInsurerName();
		this.policyNumber = previousPolicy.getPolicyNumber();
		this.policyFromDate = previousPolicy.getPolicyFrmDate() != null ? SHAUtils.formatDate(previousPolicy.getPolicyFrmDate()) : "";
		this.policyToDate = previousPolicy.getPolicyToDate() != null ? SHAUtils.formatDate(previousPolicy.getPolicyToDate()) : "";
		this.underWritingYear = previousPolicy.getUnderWritingYear();
		this.sumInsured = previousPolicy.getSumInsured();
		this.productName = previousPolicy.getProductName();
		String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		String dmsToken = intimationService.createDMSToken(previousPolicy.getPolicyNumber());
		this.policyScheduleUrl = strDmsViewURL + dmsToken;
	}

	public String getPreviousInsurerName() {
		return previousInsurerName;
	}

	public void setPreviousInsurerName(String previousInsurerName) {
		this.previousInsurerName = previousInsurerName;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyFromDate() {
		return policyFromDate;
	}

	public void setPolicyFromDate(String policyFromDate) {
		this.policyFromDate = policyFromDate;
	}

	public String getPolicyToDate() {
		return policyToDate;
	}

	public void setPolicyToDate(String policyToDate) {
		this.policyToDate = policyToDate;
	}

	public Long getUnderWritingYear() {
		return underWritingYear;
	}

	public void setUnderWritingYear(Long underWritingYear) {
		this.underWritingYear = underWritingYear;
	}

	public Double getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPolicyScheduleUrl() {
		return policyScheduleUrl;
	}

	public void setPolicyScheduleUrl(String policyScheduleUrl) {
		this.policyScheduleUrl = policyScheduleUrl;
	}

	public List<PreviousInsuranceInsuredDetailsTableDTO> getPreviousInsuredDetails() {
		return previousInsuredDetails;
	}

	public void setPreviousInsuredDetails(
			List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuredDetails) {
		this.previousInsuredDetails = previousInsuredDetails;
	}
	
}
