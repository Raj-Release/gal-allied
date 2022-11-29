/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.Date;

/**
 * @author ntv.vijayar
 *
 */
public class UploadedDocumentsDTO {
	
	private String fileType;
	
	private String fileName;
	
	private String rodNo;
	
	private String billNo;
	
	private Date billDate;
	
	private Long noOfItems;
	
	private Long billValue;

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the rodNo
	 */
/*	public String getRodNo() {
		return rodNo;
	}

	*//**
	 * @param rodNo the rodNo to set
	 *//*
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
*/
	/**
	 * @return the billNo
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * @param billNo the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @return the billDate
	 */
	public Date getBillDate() {
		return billDate;
	}

	/**
	 * @param billDate the billDate to set
	 */
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	/**
	 * @return the noOfItems
	 */
	public Long getNoOfItems() {
		return noOfItems;
	}

	/**
	 * @param noOfItems the noOfItems to set
	 */
	public void setNoOfItems(Long noOfItems) {
		this.noOfItems = noOfItems;
	}

	/**
	 * @return the billValue
	 */
	public Long getBillValue() {
		return billValue;
	}

	/**
	 * @param billValue the billValue to set
	 */
	public void setBillValue(Long billValue) {
		this.billValue = billValue;
	}

	/**
	 * @return the rodNo
	 */
	public String getRodNo() {
		return rodNo;
	}

	/**
	 * @param rodNo the rodNo to set
	 */
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

}
