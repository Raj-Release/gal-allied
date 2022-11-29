package com.shaic.paclaim.manageclaim.reopenclaim.pageRodLevel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;

public class PAReOpenRodLevelClaimDTO {
	
	private String claimStatus;
	
	private Date closedDate;
	 
	private String closedStrDate;
	
	private String closedRemarks;
	
	@NotNull(message = "Please Select Reason for Re-Open")
	private SelectValue reOpenReason;
	
	@NotNull(message = "Please Enter Re-Open Remarks")
	@Size(min = 1, message = "Please Enter Re-Open Remarks")
	private String reOpenRemarks;
	
	@NotNull(message = "Please Enter Provision Amount")
	private Double provisionAmount;
	

	private List<ViewDocumentDetailsDTO> documentDetails = new ArrayList<ViewDocumentDetailsDTO>();
	
	private Long closeClaimKey;

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public String getClosedStrDate() {
		return closedStrDate;
	}

	public void setClosedStrDate(String closedStrDate) {
		this.closedStrDate = closedStrDate;
	}

	public String getClosedRemarks() {
		return closedRemarks;
	}

	public void setClosedRemarks(String closedRemarks) {
		this.closedRemarks = closedRemarks;
	}

	public SelectValue getReOpenReason() {
		return reOpenReason;
	}

	public void setReOpenReason(SelectValue reOpenReason) {
		this.reOpenReason = reOpenReason;
	}

	public String getReOpenRemarks() {
		return reOpenRemarks;
	}

	public void setReOpenRemarks(String reOpenRemarks) {
		this.reOpenRemarks = reOpenRemarks;
	}

	public Double getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(Double provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	public Long getCloseClaimKey() {
		return closeClaimKey;
	}

	public void setCloseClaimKey(Long closeClaimKey) {
		this.closeClaimKey = closeClaimKey;
	}

	public List<ViewDocumentDetailsDTO> getDocumentDetails() {
		return documentDetails;
	}

	public void setDocumentDetails(List<ViewDocumentDetailsDTO> documentDetails) {
		this.documentDetails = documentDetails;
	}

}
