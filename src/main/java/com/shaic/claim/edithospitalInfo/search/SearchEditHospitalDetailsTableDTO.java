package com.shaic.claim.edithospitalInfo.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchEditHospitalDetailsTableDTO  extends AbstractTableDTO  implements Serializable {

	private static final long serialVersionUID = 4016831167620717224L;
	
	private Long key;

	private String intimationNo;

	private Date intimationDate;
	
	//private String intimationDate1;
	
	private String dateOfIntimation;
	private String hospitalType;

	private String status;

	private String policyNo;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Date getIntimationDate() {
		return intimationDate;
	}

	public void setIntimationDate(Date intimationDate) {
		
		if(intimationDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);
			setDateOfIntimation(dateformat);
			}
		
		this.intimationDate = intimationDate;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	
	public String getDateOfIntimation() {
		return dateOfIntimation;
	}

	public void setDateOfIntimation(String dateOfIntimation) {
		this.dateOfIntimation = dateOfIntimation;
	}

	/*public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}*/

}

