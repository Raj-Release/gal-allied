package com.shaic.claim.userproduct.document.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchDoctorDetailsTableDTO extends AbstractTableDTO  implements Serializable {
	
	private String empId;
	
	private String doctorName;
	
	private String loginId;
	
	private Long minAmt;
	
	private Long maxAmt;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Long getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(Long minAmt) {
		this.minAmt = minAmt;
	}

	public Long getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(Long maxAmt) {
		this.maxAmt = maxAmt;
	}

}
