package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractRowEnablerDTO;

public class ProcessingDoctorDetailsDTO extends AbstractRowEnablerDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2142201719774943832L;

	private Long key;
	
	private String doctorIdAndName;
	
	private String referToIcacRemarks;
	
	private Date remarksRaisedDateTime;
	
	private int serialNo;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getDoctorIdAndName() {
		return doctorIdAndName;
	}

	public void setDoctorIdAndName(String doctorIdAndName) {
		this.doctorIdAndName = doctorIdAndName;
	}

	public String getReferToIcacRemarks() {
		return referToIcacRemarks;
	}

	public void setReferToIcacRemarks(String referToIcacRemarks) {
		this.referToIcacRemarks = referToIcacRemarks;
	}

	public Date getRemarksRaisedDateTime() {
		return remarksRaisedDateTime;
	}

	public void setRemarksRaisedDateTime(Date remarksRaisedDateTime) {
		this.remarksRaisedDateTime = remarksRaisedDateTime;
	}
	
}
