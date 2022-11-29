package com.shaic.claim.viewEarlierRodDetails.dto;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewSectionDetailsTableDTO extends AbstractTableDTO {

	private String section;
	private String cover;
	private String subCover;
	
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getSubCover() {
		return subCover;
	}
	public void setSubCover(String subCover) {
		this.subCover = subCover;
	}

}
