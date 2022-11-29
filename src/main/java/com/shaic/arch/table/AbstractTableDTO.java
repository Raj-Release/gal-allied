package com.shaic.arch.table;

import java.io.Serializable;

import com.shaic.cmn.login.ImsUser;
//import com.shaic.ims.bpm.claim.HumanTask;



public class AbstractTableDTO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1694106084918069390L;
	
	private Long key;
	
	//private HumanTask humanTask;
	
	private ImsUser userRole;
	
	private String username;
	
	private String subCoverCode;
	
	private String sectionCode;
	
	private String coverCode;
	
	protected int serialNumber;
	
	private Integer taskNumber;
	
	private Long workFlowKey;
	
	private String diagnosis = "";
	
	private Object dbOutArray;
	
	private String companyName;
	
	private String colorCode;
	
	private String colorCodeCell;
	
	private String rowDescRow;
	
	private Pageable pageable;
	
	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public ImsUser getUserRole() {
		return userRole;
	}

	public void setUserRole(ImsUser userRole) {
		this.userRole = userRole;
	}

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(Integer taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getSubCoverCode() {
		return subCoverCode;
	}

	public void setSubCoverCode(String subCoverCode) {
		this.subCoverCode = subCoverCode;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getCoverCode() {
		return coverCode;
	}

	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}

	public Long getWorkFlowKey() {
		return workFlowKey;
	}

	public void setWorkFlowKey(Long workFlowKey) {
		this.workFlowKey = workFlowKey;
	}

	public Object getDbOutArray() {
		return dbOutArray;
	}

	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorCodeCell() {
		return colorCodeCell;
	}

	public void setColorCodeCell(String colorCodeCell) {
		this.colorCodeCell = colorCodeCell;
	}

	public String getRowDescRow() {
		return rowDescRow;
	}

	public void setRowDescRow(String rowDescRow) {
		this.rowDescRow = rowDescRow;
	}

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}
	

}
