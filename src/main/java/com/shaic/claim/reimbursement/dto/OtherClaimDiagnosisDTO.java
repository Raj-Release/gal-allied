package com.shaic.claim.reimbursement.dto;

import java.io.Serializable;
import java.util.Date;

public class OtherClaimDiagnosisDTO implements Serializable {

	private static final long serialVersionUID = -4103567273365059509L;
	
	private Long key;
	
	private Date hospitalizationDate;
	
	private String diagnosis;

	public Date getHospitalizationDate() {
		return hospitalizationDate;
	}

	public void setHospitalizationDate(Date hospitalizationDate) {
		this.hospitalizationDate = hospitalizationDate;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}


}
