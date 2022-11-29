/**
 * 
 */
package com.shaic.claim.paymentprocess.createbatch.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateOrSearchBatchFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String claimNo;
	private String lotNo;
	private String rodNO;
	private SelectValue cpuCode;
	private SelectValue cliamType;
	private SelectValue claimant;
	private Date startDate;
	private Date endDate;
	public String getIntimationNo() {
		return intimationNo;
		
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getRodNO() {
		return rodNO;
	}
	public void setRodNO(String rodNO) {
		this.rodNO = rodNO;
	}
	public SelectValue getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(SelectValue cpuCode) {
		this.cpuCode = cpuCode;
	}
	public SelectValue getCliamType() {
		return cliamType;
	}
	public void setCliamType(SelectValue cliamType) {
		this.cliamType = cliamType;
	}
	public SelectValue getClaimant() {
		return claimant;
	}
	public void setClaimant(SelectValue claimant) {
		this.claimant = claimant;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	

}
