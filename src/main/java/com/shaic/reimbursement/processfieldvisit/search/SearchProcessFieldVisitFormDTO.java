/**
 * 
 */
package com.shaic.reimbursement.processfieldvisit.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessFieldVisitFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private SelectValue cpuCode;
	private String policyNo;
	private SelectValue hospitalType;
	private SelectValue productCode;
	private SelectValue fvrCpuCode;
	private Date intimatedDate;
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(SelectValue hospitalType) {
		this.hospitalType = hospitalType;
	}
	public SelectValue getProductCode() {
		return productCode;
	}
	public void setProductCode(SelectValue productCode) {
		this.productCode = productCode;
	}
	public SelectValue getFvrCpuCode() {
		return fvrCpuCode;
	}
	public void setFvrCpuCode(SelectValue fvrCpuCode) {
		this.fvrCpuCode = fvrCpuCode;
	}
	public Date getIntimatedDate() {
		return intimatedDate;
	}
	public void setIntimatedDate(Date intimatedDate) {
		this.intimatedDate = intimatedDate;
	}
	
	

}
