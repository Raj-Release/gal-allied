package com.shaic.claim.reports.shadowProvision;

import com.shaic.arch.table.AbstractTableDTO;

public class SearchShadowProvisionDTO extends AbstractTableDTO {
	
   private String fileName;
   
   private String remarks;
   
   private String successCount;
   
   private String cpuCode;
   
   private String errorCount;
   
   private String intimationNumber;
   
   private Integer existingProvision;
   
   private Integer newProvision;
   
   private String errCode;
   
   private String errDescription;
   
   private String batchNumber;

   public String getFileName() {
	return fileName;
   }

public void setFileName(String fileName) {
	this.fileName = fileName;
}

public String getRemarks() {
	return remarks;
}

public void setRemarks(String remarks) {
	this.remarks = remarks;
}

public String getSuccessCount() {
	return successCount;
}

public void setSuccessCount(String successCount) {
	this.successCount = successCount;
}

public String getErrorCount() {
	return errorCount;
}

public void setErrorCount(String errorCount) {
	this.errorCount = errorCount;
}

public String getIntimationNumber() {
	return intimationNumber;
}

public void setIntimationNumber(String intimationNumber) {
	this.intimationNumber = intimationNumber;
}

public Integer getExistingProvision() {
	return existingProvision;
}

public void setExistingProvision(Integer existingProvision) {
	this.existingProvision = existingProvision;
}

public Integer getNewProvision() {
	return newProvision;
}

public void setNewProvision(Integer newProvision) {
	this.newProvision = newProvision;
}

public String getCpuCode() {
	return cpuCode;
}

public void setCpuCode(String cpuCode) {
	this.cpuCode = cpuCode;
}

public String getErrDescription() {
	return errDescription;
}

public void setErrDescription(String errDescription) {
	this.errDescription = errDescription;
}

public String getErrCode() {
	return errCode;
}

public void setErrCode(String errCode) {
	this.errCode = errCode;
}

public String getBatchNumber() {
	return batchNumber;
}

public void setBatchNumber(String batchNumber) {
	this.batchNumber = batchNumber;
}

}
