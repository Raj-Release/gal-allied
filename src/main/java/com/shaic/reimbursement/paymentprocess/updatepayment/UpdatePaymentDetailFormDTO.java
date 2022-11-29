
/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

/**
 * @author ntv.narenj
 *
 */
public class UpdatePaymentDetailFormDTO extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String claimNo;
	private String lotNo;
	private String rodNO;
	private SelectValue cpuCode;
	private SelectValue cliamType;
	private SelectValue type;
	private SelectValue paymentMode;
	private SelectValue claimant;
	private SelectValue batchType;
	private SelectValue zone;
	private Date fromDate;
	private String fromDateValue;
	private Date toDate;
	private String toDateValue;
	private String batchNo;
	private Double penalInterest;
	private Integer irdaTAT;
	private SpecialSelectValue product;
		
	//private SelectValue type;
	

	
	public Integer getIrdaTAT() {
		return irdaTAT;
	}
	public Double getPenalInterest() {
		return penalInterest;
	}
	public void setPenalInterest(Double penalInterest) {
		this.penalInterest = penalInterest;
	}
	public void setIrdaTAT(Integer irdaTAT) {
		this.irdaTAT = irdaTAT;
	}
	public SelectValue getZone() {
		return zone;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public String getFromDateValue() {
		return fromDateValue;
	}
	public void setFromDateValue(String fromDateValue) {
		this.fromDateValue = fromDateValue;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getToDateValue() {
		return toDateValue;
	}
	public void setToDateValue(String toDateValue) {
		this.toDateValue = toDateValue;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public SelectValue getBatchType() {
		return batchType;
	}
	public void setBatchType(SelectValue batchType) {
		this.batchType = batchType;
	}
	public void setZone(SelectValue zone) {
		this.zone = zone;
	}
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
	public SelectValue getType() {
		return type;
	}
	public void setType(SelectValue type) {
		this.type = type;
	}
	public SelectValue getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(SelectValue paymentMode) {
		this.paymentMode = paymentMode;
	}
	public SpecialSelectValue getProduct() {
		return product;
	}
	public void setProduct(SpecialSelectValue product) {
		this.product = product;
	}

	
	
}
