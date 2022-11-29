package com.shaic.claim.legal.processconsumerforum.page.advocatefee;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.claim.legal.processconsumerforum.page.consumerforum.CaseDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;

public class AdvocateFeeDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1657870501626109263L;
	
	public AdvocateFeeDTO() {
		
		intimationDetailsDTO = new IntimationDetailsDTO();
		caseDetailsDTO = new CaseDetailsDTO();
		
	}

	private IntimationDetailsDTO intimationDetailsDTO;
	
	private CaseDetailsDTO caseDetailsDTO;
	
	private Boolean isPartPayment;
	
	private Boolean isFullPayment;
	
	private Double advocateFee;
	
	@NotNull(message="Please Enter Advocate Name")
	@Size(min = 1 , message = "Please Enter Advocate Name")
	private String advocateName;
	
	@NotNull(message="Please Enter Amount Paid")
	private Double amtPaid;
	
	@NotNull(message="Please Enter DD Name")
	@Size(min = 1 , message = "Please Enter DD Name")
	private String ddName;
	
	private String userName;

	public IntimationDetailsDTO getIntimationDetailsDTO() {
		return intimationDetailsDTO;
	}

	public void setIntimationDetailsDTO(IntimationDetailsDTO intimationDetailsDTO) {
		this.intimationDetailsDTO = intimationDetailsDTO;
	}

	public CaseDetailsDTO getCaseDetailsDTO() {
		return caseDetailsDTO;
	}

	public void setCaseDetailsDTO(CaseDetailsDTO caseDetailsDTO) {
		this.caseDetailsDTO = caseDetailsDTO;
	}

	public Boolean getIsPartPayment() {
		return isPartPayment;
	}

	public void setIsPartPayment(Boolean isPartPayment) {
		this.isPartPayment = isPartPayment;
	}

	public Boolean getIsFullPayment() {
		return isFullPayment;
	}

	public void setIsFullPayment(Boolean isFullPayment) {
		this.isFullPayment = isFullPayment;
	}

	public Double getAdvocateFee() {
		return advocateFee;
	}

	public void setAdvocateFee(Double advocateFee) {
		this.advocateFee = advocateFee;
	}

	public String getAdvocateName() {
		return advocateName;
	}

	public void setAdvocateName(String advocateName) {
		this.advocateName = advocateName;
	}

	public Double getAmtPaid() {
		return amtPaid;
	}

	public void setAmtPaid(Double amtPaid) {
		this.amtPaid = amtPaid;
	}

	public String getDdName() {
		return ddName;
	}

	public void setDdName(String ddName) {
		this.ddName = ddName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
