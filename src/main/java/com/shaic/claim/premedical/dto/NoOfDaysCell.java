package com.shaic.claim.premedical.dto;

public class NoOfDaysCell extends BaseCell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3206047704908810144L;

	private Long key;
	
	private Long preauthKey;

	private Long stageId;
	
	private String stageName;
	
	private Long statusId;
	
	private Long subStatusId;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public Long getSubStatusId() {
		return subStatusId;
	}

	public void setSubStatusId(Long subStatusId) {
		this.subStatusId = subStatusId;
	}
	
}
