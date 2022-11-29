package com.shaic.claim.reports.agentbrokerreport;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class AgentBrokerReportTableDTO  extends AbstractTableDTO  implements Serializable{
	private String policyNo;
	private String intimationNo;
	private String smCode;
	private String smName;
	private String agentBrokerCode;
	private String agentBrokerName;
	private String policyIssueOffice;
	private String hospitalName;
	private String cashlessSettledAmount;
	private String reimbursementSettledAmount;
	private String outstandingAmount;
	private String totalAmount;
	private Long hospitalId;
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getIntimationNo() {
		return intimationNo;
	}

	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getSmCode() {
		return smCode;
	}
	public void setSmCode(String smCode) {
		this.smCode = smCode;
	}
	public String getSmName() {
		return smName;
	}
	public void setSmName(String smName) {
		this.smName = smName;
	}
	public String getAgentBrokerCode() {
		return agentBrokerCode;
	}
	public void setAgentBrokerCode(String agentBrokerCode) {
		this.agentBrokerCode = agentBrokerCode;
	}
	public String getAgentBrokerName() {
		return agentBrokerName;
	}
	public void setAgentBrokerName(String agentBrokerName) {
		this.agentBrokerName = agentBrokerName;
	}
	public String getPolicyIssueOffice() {
		return policyIssueOffice;
	}
	public void setPolicyIssueOffice(String policyIssueOffice) {
		this.policyIssueOffice = policyIssueOffice;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getCashlessSettledAmount() {
		return cashlessSettledAmount;
	}
	public void setCashlessSettledAmount(String cashlessSettledAmount) {
		this.cashlessSettledAmount = cashlessSettledAmount;
	}
	public String getReimbursementSettledAmount() {
		return reimbursementSettledAmount;
	}
	public void setReimbursementSettledAmount(String reimbursementSettledAmount) {
		this.reimbursementSettledAmount = reimbursementSettledAmount;
	}
	public String getOutstandingAmount() {
		return outstandingAmount;
	}
	public void setOutstandingAmount(String outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
