package com.shaic.claim.cashlessprocess.flagreconsiderationrequest.search;

import java.io.Serializable;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author NEWUSER
 *
 */
public class SearchFlagReconsiderationReqTableDTO extends AbstractTableDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long rodKey;
	
	private String rodNumber;
	
	private String billClassification;
	
	private Long statusKey;
	
	private Long claimedAmount;
	
	private Long approvedAmount;
	
	private String rodStatus;
	
	private String crmFlagged;
	
	private Long docAcknowLedgementKey;

	private String intimationNumber;
	
	private NewIntimationDto newIntimationDTO;
	
	private ClaimDto claimDto;
	

	private String rejectRemarks;
	

	private String flagStatus;
	

	private String disableReconsiderationReq;;
	

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}

	public Long getClaimedAmount() {
		return claimedAmount;
	}

	public void setClaimedAmount(Long claimedAmount) {
		this.claimedAmount = claimedAmount;
	}

	public Long getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Long approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public String getRodStatus() {
		return rodStatus;
	}

	public void setRodStatus(String rodStatus) {
		this.rodStatus = rodStatus;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public Long getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(Long statusKey) {
		this.statusKey = statusKey;
	}

	public Long getDocAcknowLedgementKey() {
		return docAcknowLedgementKey;
	}

	public void setDocAcknowLedgementKey(Long docAcknowLedgementKey) {
		this.docAcknowLedgementKey = docAcknowLedgementKey;
	}

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getRejectRemarks() {
		return rejectRemarks;
	}

	public void setRejectRemarks(String rejectRemarks) {
		this.rejectRemarks = rejectRemarks;
	}

	public String getFlagStatus() {
		return flagStatus;
	}

	public void setFlagStatus(String flagStatus) {
		this.flagStatus = flagStatus;
	}

	public String getDisableReconsiderationReq() {
		return disableReconsiderationReq;
	}

	public void setDisableReconsiderationReq(String disableReconsiderationReq) {
		this.disableReconsiderationReq = disableReconsiderationReq;
	}

	
	
}
