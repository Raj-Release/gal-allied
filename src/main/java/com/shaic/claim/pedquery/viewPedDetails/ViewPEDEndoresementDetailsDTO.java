package com.shaic.claim.pedquery.viewPedDetails;

import java.io.Serializable;

public class ViewPEDEndoresementDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long key;
	private String pedCode;
	private String description;
	private String icdChapter;
	private String icdBlock;
	private String icdCode;
	private String source;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	private String othersSpecify;
	private String doctorRemarks;

	public String getPedCode() {
		return pedCode;
	}

	public void setPedCode(String pedCode) {
		this.pedCode = pedCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcdChapter() {
		return icdChapter;
	}

	public void setIcdChapter(String icdChapter) {
		this.icdChapter = icdChapter;
	}

	public String getIcdBlock() {
		return icdBlock;
	}

	public void setIcdBlock(String icdBlock) {
		this.icdBlock = icdBlock;
	}

	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOthersSpecify() {
		return othersSpecify;
	}

	public void setOthersSpecify(String othersSpecify) {
		this.othersSpecify = othersSpecify;
	}

	public String getDoctorRemarks() {
		return doctorRemarks;
	}

	public void setDoctorRemarks(String doctorRemarks) {
		this.doctorRemarks = doctorRemarks;
	}

}
