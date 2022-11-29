package com.shaic.claim.lumen.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

@SuppressWarnings("serial")
public class LumenSearchReqFormDTO extends AbstractSearchDTO implements Serializable{

	private String intimationNumber;
	private String policyNumber;
	private SelectValue cmbCPUOffice;
	private Object cmbEmpName;
	private SelectValue cmbSource;
	private SelectValue cmbStatus;
	private Object cpuCodeMulti;
	
	private SelectValue cmbDBEmpName;
	
	private Date frmDate;
	private Date toDate;
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public SelectValue getCmbCPUOffice() {
		return cmbCPUOffice;
	}
	public void setCmbCPUOffice(SelectValue cmbCPUOffice) {
		this.cmbCPUOffice = cmbCPUOffice;
	}
	public Object getCmbEmpName() {
		return cmbEmpName;
	}
	public void setCmbEmpName(Object cmbEmpName) {
		this.cmbEmpName = cmbEmpName;
	}
	public SelectValue getCmbSource() {
		return cmbSource;
	}
	public void setCmbSource(SelectValue cmbSource) {
		this.cmbSource = cmbSource;
	}
	public SelectValue getCmbStatus() {
		return cmbStatus;
	}
	public void setCmbStatus(SelectValue cmbStatus) {
		this.cmbStatus = cmbStatus;
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
	public SelectValue getCmbDBEmpName() {
		return cmbDBEmpName;
	}
	public void setCmbDBEmpName(SelectValue cmbDBEmpName) {
		this.cmbDBEmpName = cmbDBEmpName;
	}
	public Object getCpuCodeMulti() {
		return cpuCodeMulti;
	}
	public void setCpuCodeMulti(Object cpuCodeMulti) {
		this.cpuCodeMulti = cpuCodeMulti;
	}
	
}
