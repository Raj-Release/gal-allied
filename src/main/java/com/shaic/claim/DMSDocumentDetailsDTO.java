/**
 * 
 */
package com.shaic.claim;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ntv.vijayar
 *
 */
public class DMSDocumentDetailsDTO {

	private String documentType;
	
	private String fileName;
	
	private String documentSource;
	
	private String dmsDocToken;

	private String intimationNo;
	
	private String claimNo;
	
	private Long slNo;
	
	private String galaxyFileName;
	
	private Date documentCreatedDate;
	
	private String documentCreatedDateValue;
	
	private String reimbursementNumber;
	
	private String cashlessNumber;
	
	private String cashlessOrReimbursement;
	
	private String fileViewURL;
	
	// Added for claims dms view page
	private String rodNoFormat;
	
	private String fileType;
	private String dmsRestApiURL;
	
	private String deductionRemarks;
	
	private String hospitalCode;
	
	private String hosiptalDiscount;
	private String lumen;
	
	private String docVersion;

	public String getDmsRestApiURL() {
		return dmsRestApiURL;
	}

	public void setDmsRestApiURL(String dmsRestApiURL) {
		this.dmsRestApiURL = dmsRestApiURL;
	}

	public String getCashlessOrReimbursement() {
		return cashlessOrReimbursement;
	}

	public void setCashlessOrReimbursement(String cashlessOrReimbursement) {
		this.cashlessOrReimbursement = cashlessOrReimbursement;
	}

	public String getReimbursementNumber() {
		return reimbursementNumber;
	}

	public void setReimbursementNumber(String reimbursementNumber) {
		this.reimbursementNumber = reimbursementNumber;
	}

	public String getCashlessNumber() {
		return cashlessNumber;
	}

	public void setCashlessNumber(String cashlessNumber) {
		this.cashlessNumber = cashlessNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public String getDmsDocToken() {
		return dmsDocToken;
	}

	public void setDmsDocToken(String dmsDocToken) {
		this.dmsDocToken = dmsDocToken;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	public String getGalaxyFileName() {
		return galaxyFileName;
	}

	public void setGalaxyFileName(String galaxyFileName) {
		this.galaxyFileName = galaxyFileName;
	}

	public String getDocumentCreatedDateValue() {
		return documentCreatedDateValue;
	}

	public void setDocumentCreatedDateValue(String documentCreatedDateValue) {
		this.documentCreatedDateValue = documentCreatedDateValue;
	}

	public Date getDocumentCreatedDate() {
		return documentCreatedDate;
	}
	
	public String getFileViewURL() {
		return fileViewURL;
	}

	public void setFileViewURL(String fileViewURL) {
		this.fileViewURL = fileViewURL;
	}

	public void setDocumentCreatedDate(Date documentCreatedDate) {
		
		
		if(null != documentCreatedDate){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(documentCreatedDate);
			setDocumentCreatedDateValue(dateformat);
			this.documentCreatedDate = documentCreatedDate;
		}
		
		
	}

	public String getRodNoFormat() {
		return rodNoFormat;
	}

	public void setRodNoFormat(String rodNoFormat) {
		this.rodNoFormat = rodNoFormat;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDeductionRemarks() {
		return deductionRemarks;
	}

	public void setDeductionRemarks(String deductionRemarks) {
		this.deductionRemarks = deductionRemarks;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHosiptalDiscount() {
		return hosiptalDiscount;
	}

	public void setHosiptalDiscount(String hosiptalDiscount) {
		this.hosiptalDiscount = hosiptalDiscount;
	}
	
	public String getLumen() {
		return lumen;
	}
	public void setLumen(String lumen) {
		this.lumen = lumen;
	}

	public String getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(String docVersion) {
		this.docVersion = docVersion;
	}		
}
