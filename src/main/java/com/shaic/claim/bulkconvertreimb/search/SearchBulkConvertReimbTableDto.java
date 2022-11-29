package com.shaic.claim.bulkconvertreimb.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;

public class SearchBulkConvertReimbTableDto extends AbstractTableDTO implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int srlNo;

	private Long conversionDetailsKey;
	
	private int intimatedDays;

	private String claimType;
	
	private Long preauthKey;
	
	private String cpuCode;

	private String claimNumber;
	
	private ClaimDto claimDto;
	
	private Long claimKey;
	
	private String intimationNumber;

	private Long intimationKey;
	
	private Date intimatedDate;
	
	private String intimatedDateValue;

	private String claimStatus;
	
	private boolean selected = false;
	
	private String crno;
	
	private String subcrno;
	
	private String docToken;
	
	private String category;
	
	private String letterDateValue;
	
	private Long printCount;
	
	private String printFlag;
	
	private String searchType;
	
	private String fileUrl;
	
	private Long conversionTypeId;
	
	private SelectValue reasonforConversion;
	
	private Boolean isOnLoad = Boolean.FALSE;
			
	public int getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(int srlNo) {
		this.srlNo = srlNo;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public int getIntimatedDays() {
		return intimatedDays;
	}

	public void setIntimatedDays(int intimatedDays) {
		this.intimatedDays = intimatedDays;
	}

	public Date getIntimatedDate() {
		return intimatedDate;
	}

	public void setIntimatedDate(Date intimatedDate) {
		this.intimatedDate = intimatedDate;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getIntimationNumber() {
		return intimationNumber;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public String getIntimatedDateValue() {
		return intimatedDateValue;
	}

	public void setIntimatedDateValue(String intimatedDateValue) {
		this.intimatedDateValue = intimatedDateValue;
	}

	public String getCrno() {
		return crno;
	}

	public void setCrno(String crno) {
		this.crno = crno;
	}

	public String getSubcrno() {
		return subcrno;
	}

	public void setSubcrno(String subcrno) {
		this.subcrno = subcrno;
	}

	public String getDocToken() {
		return docToken;
	}

	public void setDocToken(String docToken) {
		this.docToken = docToken;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLetterDateValue() {
		return letterDateValue;
	}

	public void setLetterDateValue(String letterDateValue) {
		this.letterDateValue = letterDateValue;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}

	public Long getConversionDetailsKey() {
		return conversionDetailsKey;
	}

	public void setConversionDetailsKey(Long conversionDetailsKey) {
		this.conversionDetailsKey = conversionDetailsKey;
	}

	public Long getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Long printCount) {
		this.printCount = printCount;
	}

	public String getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public SelectValue getReasonforConversion() {
		return reasonforConversion;
	}

	public void setReasonforConversion(SelectValue reasonforConversion) {
		this.reasonforConversion = reasonforConversion;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public Long getConversionTypeId() {
		return conversionTypeId;
	}

	public void setConversionTypeId(Long conversionTypeId) {
		this.conversionTypeId = conversionTypeId;
	}

	public Boolean getIsOnLoad() {
		return isOnLoad;
	}

	public void setIsOnLoad(Boolean isOnLoad) {
		this.isOnLoad = isOnLoad;
	}	
	
	
}
