package com.shaic.claim.reports.ackwithoutrodreport;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;


public class AckWithoutRodTableDto extends AbstractTableDTO{
	
	private Date frmDate;
	private Date toDate;
	private SelectValue cpuCodeSelect;
	private SelectValue docRecvdFrmSelect;	
	
	private String intimationNumber;
	private String billRcvdDate;
	private String ackDate;
	private String docRecdFrm;
	private String cpuCode;
	private String cpu;
	private String claimType;
	private String productCode;
	private String productName;
	private String userName;
	private String userId;
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getBillRcvdDate() {
		return billRcvdDate;
	}
	public void setBillRcvdDate(String billRcvdDate) {
		this.billRcvdDate = billRcvdDate;
	}
	public String getAckDate() {
		return ackDate;
	}
	public void setAckDate(String ackDate) {
		this.ackDate = ackDate;
	}
	public String getDocRecdFrm() {
		return docRecdFrm;
	}
	public void setDocRecdFrm(String docRecdFrm) {
		this.docRecdFrm = docRecdFrm;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getFrmDate() {
		return frmDate;
	}
	public void setFrmDate(Date frmDate) {
		this.frmDate = frmDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public SelectValue getCpuCodeSelect() {
		return cpuCodeSelect;
	}
	public void setCpuCodeSelect(SelectValue cpuCodeSelect) {
		this.cpuCodeSelect = cpuCodeSelect;
	}
	public SelectValue getDocRecvdFrmSelect() {
		return docRecvdFrmSelect;
	}
	public void setDocRecvdFrmSelect(SelectValue docRecvdFrmSelect) {
		this.docRecvdFrmSelect = docRecvdFrmSelect;
	}
}
