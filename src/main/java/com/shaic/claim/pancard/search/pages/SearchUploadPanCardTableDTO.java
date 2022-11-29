/**
 * 
 */
package com.shaic.claim.pancard.search.pages;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;


/**
 * @author ntv.narenj
 *
 */
public class SearchUploadPanCardTableDTO extends AbstractTableDTO  implements Serializable{

	private Long key;
	private String intimationNo;
	private String policyNo;
	private String proposerName;
	private Date policyStartDate;
	private Date policyEndDate;
	private Object dbOutArray ;
	private NewIntimationDto newIntimationDto;
	
	@NotNull(message = "Please Enter Pan Card No.")
	@Size(min = 10,max = 10 , message = "Pan Card no should be 10 Character")
	private String panCardNo;
	private String panRemarks;
	
	
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public Object getDbOutArray() {
		return dbOutArray;
	}
	public void setDbOutArray(Object dbOutArray) {
		this.dbOutArray = dbOutArray;
	}
	public Date getPolicyStartDate() {
		return policyStartDate;
	}
	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}
	public Date getPolicyEndDate() {
		return policyEndDate;
	}
	public void setPolicyEndDate(Date policyEndDate) {
		this.policyEndDate = policyEndDate;
	}
	public String getProposerName() {
		return proposerName;
	}
	public void setProposerName(String proposerName) {
		this.proposerName = proposerName;
	}
	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}
	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}
	public String getPanCardNo() {
		return panCardNo;
	}
	public void setPanCardNo(String panCardNo) {
		this.panCardNo = panCardNo;
	}
	public String getPanRemarks() {
		return panRemarks;
	}
	public void setPanRemarks(String panRemarks) {
		this.panRemarks = panRemarks;
	}
}
