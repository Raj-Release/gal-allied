package com.shaic.claim.outpatient.registerclaim.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.domain.Insured;

public class InsuredDetailsDTO implements Serializable{
	private static final long serialVersionUID = -5739464782457615418L;

	private Integer serialNumber;
	
	private Long key;
	
	private Insured insuredPatientName;
	
	@NotNull(message = "Please Choose Op Check-up Date")
	private Date checkupDate;
	
	@NotNull(message = "Please Enter Reason For OP Check-up Visit")
	@Size(min = 1, message = "Please Enter Reason For OP Check-up Visit")
	private String reasonForCheckup;

	public Insured getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(Insured insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public Date getCheckupDate() {
		return checkupDate;
	}

	public void setCheckupDate(Date checkupDate) {
		this.checkupDate = checkupDate;
	}

	public String getReasonForCheckup() {
		return reasonForCheckup;
	}

	public void setReasonForCheckup(String reasonForCheckup) {
		this.reasonForCheckup = reasonForCheckup;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
}
