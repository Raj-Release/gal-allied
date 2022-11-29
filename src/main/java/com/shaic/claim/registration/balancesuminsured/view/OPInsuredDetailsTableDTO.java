package com.shaic.claim.registration.balancesuminsured.view;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.table.AbstractTableDTO;

public class OPInsuredDetailsTableDTO extends AbstractTableDTO implements
		Serializable {
	private static final long serialVersionUID = -5739464782457615418L;

	private Long key;

	private String insuredPatientName;

	@NotNull(message = "Please Choose Op Check-up Date")
	private String checkupDate;

	@NotNull(message = "Please Enter Reason For OP Check-up Visit")
	@Size(min = 1, message = "Please Enter Reason For OP Check-up Visit")
	private String reasonForCheckup;

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getCheckupDate() {
		return checkupDate;
	}

	public void setCheckupDate(String checkupDate) {
		this.checkupDate = checkupDate;
	}

	public String getReasonForCheckup() {
		return reasonForCheckup;
	}

	public void setReasonForCheckup(String reasonForCheckup) {
		this.reasonForCheckup = reasonForCheckup;
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
