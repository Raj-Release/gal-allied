package com.shaic.claim.reports.executivesummaryreqort;


/**
 * @author Lakshminarayana
 *
 */
public class ExecutiveStatusSummaryReportDto {
	
	private int sno;
	private Long stageId;
	private Long statusId;
	private Long stageCount;
	private String stageName;
	private String executiveName;
	private String executiveId;
	private String statusName;
		
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public Long getStageId() {
		return stageId;
	}
	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
	public Long getStageCount() {
		return stageCount;
	}
	public void setStageCount(Long stageCount) {
		this.stageCount = stageCount;
	}
	public String getStageName() {
		return stageName;
	}
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	public String getExecutiveName() {
		return executiveName;
	}
	public void setExecutiveName(String executiveName) {
		this.executiveName = executiveName;
	}
	public String getExecutiveId() {
		return executiveId;
	}
	public void setExecutiveId(String executiveId) {
		this.executiveId = executiveId;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}    
	
}
