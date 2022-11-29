package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class InvesAndQueryAndFvrParallelFlowTableDTO extends AbstractTableDTO  implements Serializable{

	private String type;
	private String initiatedDate;
	private String remarks;
	private String status;
	private Boolean fvrInvsQueryCancelStatus = Boolean.FALSE;
	private Boolean proceedWithOutCheckStatus;
	private String cancelRemarks;
	private Long workFlowKey;
	private Long statusKey;
	private Boolean isCanCelRodStatus = false;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInitiatedDate() {
		return initiatedDate;
	}
	public void setInitiatedDate(String initiatedDate) {
		this.initiatedDate = initiatedDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getFvrInvsQueryCancelStatus() {
		return fvrInvsQueryCancelStatus;
	}
	public void setFvrInvsQueryCancelStatus(Boolean fvrInvsQueryCancelStatus) {
		this.fvrInvsQueryCancelStatus = fvrInvsQueryCancelStatus;
	}
	public Boolean getProceedWithOutCheckStatus() {
		return proceedWithOutCheckStatus;
	}
	public void setProceedWithOutCheckStatus(Boolean proceedWithOutCheckStatus) {
		this.proceedWithOutCheckStatus = proceedWithOutCheckStatus;
	}
	public String getCancelRemarks() {
		return cancelRemarks;
	}
	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}
	public Long getWorkFlowKey() {
		return workFlowKey;
	}
	public void setWorkFlowKey(Long workFlowKey) {
		this.workFlowKey = workFlowKey;
	}
	public Long getStatusKey() {
		return statusKey;
	}
	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}
	public Boolean getIsCanCelRodStatus() {
		return isCanCelRodStatus;
	}
	public void setIsCanCelRodStatus(Boolean isCanCelRodStatus) {
		this.isCanCelRodStatus = isCanCelRodStatus;
	}
	
	
}
