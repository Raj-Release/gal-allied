package com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload;
import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPViewDocumentDetailsTableDTO extends AbstractTableDTO  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String classification;
	private String subClassification;
	private String category;
	private Long claimKey;
	private Long ackKey;
	
	
	public String getSubClassification() {
		return subClassification;
	}
	public void setSubClassification(String subClassification) {
		this.subClassification = subClassification;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Long getAckKey() {
		return ackKey;
	}
	public void setAckKey(Long ackKey) {
		this.ackKey = ackKey;
	}
	
}