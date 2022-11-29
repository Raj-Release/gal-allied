package com.shaic.claim.rod.wizard.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.shaic.arch.fields.dto.SelectValue;

public class SectionDetailsTableDTO implements Serializable {
	
	private static final long serialVersionUID = 6200656197201856307L;

	@NotNull(message = "Please Select Section")
	private SelectValue section;
	
	@NotNull(message = "Please Select Cover")
	private SelectValue cover;
	
	@NotNull(message = "Please Select Sub Cover")
	private SelectValue subCover;
	
	public SelectValue getSection() {
		return section;
	}

	public void setSection(SelectValue section) {
		this.section = section;
	}

	public SelectValue getCover() {
		return cover;
	}

	public void setCover(SelectValue cover) {
		this.cover = cover;
	}

	public SelectValue getSubCover() {
		return subCover;
	}

	public void setSubCover(SelectValue subCover) {
		this.subCover = subCover;
	}

	

	
}
