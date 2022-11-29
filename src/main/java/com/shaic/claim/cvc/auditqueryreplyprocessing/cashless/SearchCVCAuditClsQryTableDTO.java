package com.shaic.claim.cvc.auditqueryreplyprocessing.cashless;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tools.ant.taskdefs.Sleep;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.ClaimAuditQuery;


public class SearchCVCAuditClsQryTableDTO extends AbstractTableDTO  implements Serializable{
	
	private String intimationNumber;
	private String transactionNumber;
	private String clmAuditStatus;
	private Long transactionKey;
	
	
	private String clsAuditQryRemarks;
//	private Date clsAuditQueryRaisedDt;
	private String clsAuditQueryRaisedDtStr;
	
	private String clsAuditQryReplyRemarks;
	private String clsQryReplyBy;
	private String clsQryReplyRole;
//	private Date clsQryReplyDt;
	private String clsQryReplyDtStr;
	
	/*private String clsAuditAdlQryRemarks;
	private Date clsAuditAdlQueryRaisedDt;
	private String clsAuditAdlQryReplyRemarks;*/

	
	private String medicalAuditQryRemarks;
//	private Date medicalAuditQueryRaisedDt;
	private String medicalAuditQueryRaisedDtStr;
	private String medicalAuditQryReplyRemarks;
	private String medicalAuditQryReplyBy;
	private String medicalAuditQryReplyRole;
//	private Date medicalAuditQryReplyDt;
	private String medicalAuditQryReplyDtStr;
	/*private String medicalAuditAdlQryRemarks;
	private Date medicalAuditAdlQueryRaisedDt;
	private String medicalAuditAdlQryReplyRemarks;*/
	
	private String billinFaAuditQryRemarks;
//	private Date billinFaAuditQueryRaisedDt;
	private String billinFaAuditQueryRaisedDtStr;
	private String billinFaAuditQryReplyRemarks;
	private String billinFaAuditQryReplyBy;
	private String billinFaAuditQryReplyRole;
//	private Date billinFaAuditQryReplyDt;
	private String billinFaAuditQryReplyDtStr;
	
	/*private String billinFaAuditAdlQryRemarks;
	private Date billinFaAuditAdlQueryRaisedDt;
	private String billinFaAuditAdlQryReplyRemarks;*/
	
	private int sNo;
	
	private Long qryKey;
	private String qryStatus;
	private String qryAccptFlag;
	
	
	private PreauthDTO preauthDto;
	
	private String presenterString;

	private boolean clmAuditHeadUser;
	
	private Long auditKey;
	
	private String qryFinalStatus;
	private String qryFinalRemarks;
	private String qryFinalisedDateStr;
	
	private String claimsReply;
	
	private SelectValue claimsReplySelect;
	
	public SearchCVCAuditClsQryTableDTO() {
		super();
	}
	
	public SearchCVCAuditClsQryTableDTO(ClaimAuditQuery claimAuditQuery) {
		
		this.qryAccptFlag = claimAuditQuery.getFinalisedStatus() != null && claimAuditQuery.getFinalisedStatus().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_PENDING) ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG;

		this.clsAuditQryRemarks = this.qryAccptFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) ? claimAuditQuery.getFinalisedRemarks() : claimAuditQuery.getQueryRemarks();
		this.clsAuditQueryRaisedDtStr = this.qryAccptFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) ? (claimAuditQuery.getFinalisedDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getFinalisedDate()) : "") : (claimAuditQuery.getQryRaiseDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getQryRaiseDate()) : "");
		this.clsAuditQryReplyRemarks = claimAuditQuery.getQueryReplyRemarks();
		this.clsQryReplyBy = claimAuditQuery.getReplyby();
		this.clsQryReplyRole = claimAuditQuery.getReplyRole();
		this.clsQryReplyDtStr = claimAuditQuery.getReplyDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getReplyDate()) : "";		
		this.medicalAuditQryRemarks = this.qryAccptFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) ? claimAuditQuery.getFinalisedRemarks() : claimAuditQuery.getQueryRemarks();
		this.medicalAuditQueryRaisedDtStr = this.qryAccptFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) ? (claimAuditQuery.getFinalisedDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getFinalisedDate()) : "") : (claimAuditQuery.getQryRaiseDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getQryRaiseDate()) : "");
		this.medicalAuditQryReplyRemarks = claimAuditQuery.getQueryReplyRemarks();
		this.medicalAuditQryReplyBy = claimAuditQuery.getReplyby();
		this.medicalAuditQryReplyRole = claimAuditQuery.getReplyRole();
		this.medicalAuditQryReplyDtStr = claimAuditQuery.getReplyDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getReplyDate()) : "";
		this.billinFaAuditQryRemarks = this.qryAccptFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) ? claimAuditQuery.getFinalisedRemarks() : claimAuditQuery.getQueryRemarks();
		this.billinFaAuditQueryRaisedDtStr = this.qryAccptFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) ? (claimAuditQuery.getFinalisedDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getFinalisedDate()) : "") : (claimAuditQuery.getQryRaiseDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getQryRaiseDate()) : "");
		this.billinFaAuditQryReplyRemarks = claimAuditQuery.getQueryReplyRemarks();
		this.billinFaAuditQryReplyBy = claimAuditQuery.getReplyby();
		this.billinFaAuditQryReplyRole = claimAuditQuery.getReplyRole();
		this.billinFaAuditQryReplyDtStr = claimAuditQuery.getReplyDate() != null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(claimAuditQuery.getReplyDate()) : "";
		this.qryKey = claimAuditQuery.getKey();
		this.qryStatus = claimAuditQuery.getStatus();
		this.claimsReply = claimAuditQuery.getClaimsReply();
	}
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getClmAuditStatus() {
		return clmAuditStatus;
	}
	public void setClmAuditStatus(String clmAuditStatus) {
		this.clmAuditStatus = clmAuditStatus;
	}
	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}
	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}
	public Long getTransactionKey() {
		return transactionKey;
	}
	public void setTransactionKey(Long transactionKey) {
		this.transactionKey = transactionKey;
	}
	public String getPresenterString() {
		return presenterString;
	}
	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
	}
	public String getClsAuditQryRemarks() {
		return clsAuditQryRemarks;
	}
	public void setClsAuditQryRemarks(String clsAuditQryRemarks) {
		this.clsAuditQryRemarks = clsAuditQryRemarks;
	}
	public String getClsAuditQryReplyRemarks() {
		return clsAuditQryReplyRemarks;
	}
	public void setClsAuditQryReplyRemarks(String clsAuditQryReplyRemarks) {
		this.clsAuditQryReplyRemarks = clsAuditQryReplyRemarks;
	}
	/*public String getClsAuditAdlQryRemarks() {
		return clsAuditAdlQryRemarks;
	}
	public void setClsAuditAdlQryRemarks(String clsAuditAdlQryRemarks) {
		this.clsAuditAdlQryRemarks = clsAuditAdlQryRemarks;
	}
	public String getClsAuditAdlQryReplyRemarks() {
		return clsAuditAdlQryReplyRemarks;
	}
	public void setClsAuditAdlQryReplyRemarks(String clsAuditAdlQryReplyRemarks) {
		this.clsAuditAdlQryReplyRemarks = clsAuditAdlQryReplyRemarks;
	}*/
	public String getMedicalAuditQryRemarks() {
		return medicalAuditQryRemarks;
	}
	public void setMedicalAuditQryRemarks(String medicalAuditQryRemarks) {
		this.medicalAuditQryRemarks = medicalAuditQryRemarks;
	}
	public String getMedicalAuditQryReplyRemarks() {
		return medicalAuditQryReplyRemarks;
	}
	public void setMedicalAuditQryReplyRemarks(String medicalAuditQryReplyRemarks) {
		this.medicalAuditQryReplyRemarks = medicalAuditQryReplyRemarks;
	}
	/*public String getMedicalAuditAdlQryRemarks() {
		return medicalAuditAdlQryRemarks;
	}
	public void setMedicalAuditAdlQryRemarks(String medicalAuditAdlQryRemarks) {
		this.medicalAuditAdlQryRemarks = medicalAuditAdlQryRemarks;
	}
	public String getMedicalAuditAdlQryReplyRemarks() {
		return medicalAuditAdlQryReplyRemarks;
	}
	public void setMedicalAuditAdlQryReplyRemarks(
			String medicalAuditAdlQryReplyRemarks) {
		this.medicalAuditAdlQryReplyRemarks = medicalAuditAdlQryReplyRemarks;
	}*/
	public String getBillinFaAuditQryRemarks() {
		return billinFaAuditQryRemarks;
	}
	public void setBillinFaAuditQryRemarks(String billinFaAuditQryRemarks) {
		this.billinFaAuditQryRemarks = billinFaAuditQryRemarks;
	}
	public String getBillinFaAuditQryReplyRemarks() {
		return billinFaAuditQryReplyRemarks;
	}
	public void setBillinFaAuditQryReplyRemarks(String billinFaAuditQryReplyRemarks) {
		this.billinFaAuditQryReplyRemarks = billinFaAuditQryReplyRemarks;
	}
	/*public String getBillinFaAuditAdlQryRemarks() {
		return billinFaAuditAdlQryRemarks;
	}
	public void setBillinFaAuditAdlQryRemarks(String billinFaAuditAdlQryRemarks) {
		this.billinFaAuditAdlQryRemarks = billinFaAuditAdlQryRemarks;
	}
	public String getBillinFaAuditAdlQryReplyRemarks() {
		return billinFaAuditAdlQryReplyRemarks;
	}
	public void setBillinFaAuditAdlQryReplyRemarks(
			String billinFaAuditAdlQryReplyRemarks) {
		this.billinFaAuditAdlQryReplyRemarks = billinFaAuditAdlQryReplyRemarks;
	}*/
	public String getClsAuditQueryRaisedDtStr() {
		return clsAuditQueryRaisedDtStr;
	}
	public void setClsAuditQueryRaisedDtStr(String clsAuditQueryRaisedDtStr) {
		this.clsAuditQueryRaisedDtStr = clsAuditQueryRaisedDtStr;
	}
	public String getClsQryReplyBy() {
		return clsQryReplyBy;
	}
	public void setClsQryReplyBy(String clsQryReplyBy) {
		this.clsQryReplyBy = clsQryReplyBy;
	}
	public String getClsQryReplyRole() {
		return clsQryReplyRole;
	}
	public void setClsQryReplyRole(String clsQryReplyRole) {
		this.clsQryReplyRole = clsQryReplyRole;
	}
	public String getClsQryReplyDtStr() {
		return clsQryReplyDtStr;
	}
	public void setClsQryReplyDtStr(String clsQryReplyDtStr) {
		this.clsQryReplyDtStr = clsQryReplyDtStr;
	}
	/*public Date getClsAuditAdlQueryRaisedDt() {
		return clsAuditAdlQueryRaisedDt;
	}
	public void setClsAuditAdlQueryRaisedDt(Date clsAuditAdlQueryRaisedDt) {
		this.clsAuditAdlQueryRaisedDt = clsAuditAdlQueryRaisedDt;
	}*/
	public String getMedicalAuditQueryRaisedDtStr() {
		return medicalAuditQueryRaisedDtStr;
	}
	public void setMedicalAuditQueryRaisedDtStr(String medicalAuditQueryRaisedDtStr) {
		this.medicalAuditQueryRaisedDtStr = medicalAuditQueryRaisedDtStr;
	}
	public String getMedicalAuditQryReplyBy() {
		return medicalAuditQryReplyBy;
	}
	public void setMedicalAuditQryReplyBy(String medicalAuditQryReplyBy) {
		this.medicalAuditQryReplyBy = medicalAuditQryReplyBy;
	}
	public String getMedicalAuditQryReplyRole() {
		return medicalAuditQryReplyRole;
	}
	public void setMedicalAuditQryReplyRole(String medicalAuditQryReplyRole) {
		this.medicalAuditQryReplyRole = medicalAuditQryReplyRole;
	}
	public String getMedicalAuditQryReplyDtStr() {
		return medicalAuditQryReplyDtStr;
	}
	public void setMedicalAuditQryReplyDtStr(String medicalAuditQryReplyDtStr) {
		this.medicalAuditQryReplyDtStr = medicalAuditQryReplyDtStr;
	}
	/*public Date getMedicalAuditAdlQueryRaisedDt() {
		return medicalAuditAdlQueryRaisedDt;
	}
	public void setMedicalAuditAdlQueryRaisedDt(Date medicalAuditAdlQueryRaisedDt) {
		this.medicalAuditAdlQueryRaisedDt = medicalAuditAdlQueryRaisedDt;
	}*/
	public String getBillinFaAuditQueryRaisedDtStr() {
		return billinFaAuditQueryRaisedDtStr;
	}
	public void setBillinFaAuditQueryRaisedDtStr(String billinFaAuditQueryRaisedDtStr) {
		this.billinFaAuditQueryRaisedDtStr = billinFaAuditQueryRaisedDtStr;
	}
	public String getBillinFaAuditQryReplyBy() {
		return billinFaAuditQryReplyBy;
	}
	public void setBillinFaAuditQryReplyBy(String billinFaAuditQryReplyBy) {
		this.billinFaAuditQryReplyBy = billinFaAuditQryReplyBy;
	}
	public String getBillinFaAuditQryReplyRole() {
		return billinFaAuditQryReplyRole;
	}
	public void setBillinFaAuditQryReplyRole(String billinFaAuditQryReplyRole) {
		this.billinFaAuditQryReplyRole = billinFaAuditQryReplyRole;
	}
	public String getBillinFaAuditQryReplyDtStr() {
		return billinFaAuditQryReplyDtStr;
	}
	public void setBillinFaAuditQryReplyDtStr(String billinFaAuditQryReplyDtStr) {
		this.billinFaAuditQryReplyDtStr = billinFaAuditQryReplyDtStr;
	}
	/*public Date getBillinFaAuditAdlQueryRaisedDt() {
		return billinFaAuditAdlQueryRaisedDt;
	}
	public void setBillinFaAuditAdlQueryRaisedDt(Date billinFaAuditAdlQueryRaisedDt) {
		this.billinFaAuditAdlQueryRaisedDt = billinFaAuditAdlQueryRaisedDt;
	}*/
	public int getsNo() {
		return sNo;
	}
	public void setsNo(int sNo) {
		this.sNo = sNo;
	}
	public String getQryAccptFlag() {
		return qryAccptFlag;
	}
	public void setQryAccptFlag(String qryAccptFlag) {
		this.qryAccptFlag = qryAccptFlag;
	}
	public Long getQryKey() {
		return qryKey;
	}
	public void setQryKey(Long qryKey) {
		this.qryKey = qryKey;
	}
	public String getQryStatus() {
		return qryStatus;
	}
	public void setQryStatus(String qryStatus) {
		this.qryStatus = qryStatus;
	}

	public boolean isClmAuditHeadUser() {
		return clmAuditHeadUser;
	}

	public void setClmAuditHeadUser(boolean clmAuditHeadUser) {
		this.clmAuditHeadUser = clmAuditHeadUser;
	}

	public Long getAuditKey() {
		return auditKey;
	}

	public void setAuditKey(Long auditKey) {
		this.auditKey = auditKey;
	}

	public String getQryFinalStatus() {
		return qryFinalStatus;
	}

	public void setQryFinalStatus(String qryFinalStatus) {
		this.qryFinalStatus = qryFinalStatus;
	}

	public String getQryFinalisedDateStr() {
		return qryFinalisedDateStr;
	}

	public void setQryFinalisedDateStr(String qryFinalisedDateStr) {
		this.qryFinalisedDateStr = qryFinalisedDateStr;
	}

	public String getQryFinalRemarks() {
		return qryFinalRemarks;
	}

	public void setQryFinalRemarks(String qryFinalRemarks) {
		this.qryFinalRemarks = qryFinalRemarks;
	}

	public String getClaimsReply() {
		return claimsReply;
	}

	public void setClaimsReply(String claimsReply) {
		this.claimsReply = claimsReply;
	}

	public SelectValue getClaimsReplySelect() {
		return claimsReplySelect;
	}

	public void setClaimsReplySelect(SelectValue claimsReplySelect) {
		this.claimsReplySelect = claimsReplySelect;
	}
	
	
	
}