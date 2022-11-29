package com.shaic.claim.viewEarlierRodDetails;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;

public class EsclateToRawTableDTO extends AbstractTableDTO{
	private SelectValue esclateCategory;
	private SelectValue esclateSubCategory;
	private String esclateToRawRemarks;
	private List<EsclateToRawTableDTO> deletedList;
	private String category;
	private String subCategory;
	private String remarksToRaw;
	private String remarksFromRaw;
	private String resolutionFromRaw;
	private Integer rollNo;
	private Boolean isSubCategoryAvailable = Boolean.FALSE;
	private String recordType;;
	
	public SelectValue getEsclateCategory() {
		return esclateCategory;
	}
	public void setEsclateCategory(SelectValue esclateCategory) {
		this.esclateCategory = esclateCategory;
	}
	public SelectValue getEsclateSubCategory() {
		return esclateSubCategory;
	}
	public void setEsclateSubCategory(SelectValue esclateSubCategory) {
		this.esclateSubCategory = esclateSubCategory;
	}
	public String getEsclateToRawRemarks() {
		return esclateToRawRemarks;
	}
	public void setEsclateToRawRemarks(String esclateToRawRemarks) {
		this.esclateToRawRemarks = esclateToRawRemarks;
	}
	public List<EsclateToRawTableDTO> getDeletedList() {
		return deletedList;
	}
	public void setDeletedList(List<EsclateToRawTableDTO> deletedList) {
		this.deletedList = deletedList;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getRemarksToRaw() {
		return remarksToRaw;
	}
	public void setRemarksToRaw(String remarksToRaw) {
		this.remarksToRaw = remarksToRaw;
	}
	public String getRemarksFromRaw() {
		return remarksFromRaw;
	}
	public void setRemarksFromRaw(String remarksFromRaw) {
		this.remarksFromRaw = remarksFromRaw;
	}
	public String getResolutionFromRaw() {
		return resolutionFromRaw;
	}
	public void setResolutionFromRaw(String resolutionFromRaw) {
		this.resolutionFromRaw = resolutionFromRaw;
	}
	public Integer getRollNo() {
		return rollNo;
	}
	public void setRollNo(Integer rollNo) {
		this.rollNo = rollNo;
	}
	public Boolean getIsSubCategoryAvailable() {
		return isSubCategoryAvailable;
	}
	public void setIsSubCategoryAvailable(Boolean isSubCategoryAvailable) {
		this.isSubCategoryAvailable = isSubCategoryAvailable;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
}
