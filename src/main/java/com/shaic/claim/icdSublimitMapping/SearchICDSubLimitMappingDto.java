package com.shaic.claim.icdSublimitMapping;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class SearchICDSubLimitMappingDto extends SearchTableDTO{

	private String sublimitSrch;
	
	private String sno;
	private String sublimitName;
	private Long sublimitKey;
	private SelectValue icdChapterSelect;
	private SelectValue icdBlockSelect;
	
	private String icdChapter;
	private Long icdChapterKey;
	private String icdBlock;
	private Long icdBlockKey;
	private List<IcdSublimitMappingDto> IcdCodeSelectList;
	
	public String getSublimitSrch() {
		return sublimitSrch;
	}
	public void setSublimitSrch(String sublimitSrch) {
		this.sublimitSrch = sublimitSrch;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public String getSublimitName() {
		return sublimitName;
	}
	public void setSublimitName(String sublimitName) {
		this.sublimitName = sublimitName;
	}
	public Long getSublimitKey() {
		return sublimitKey;
	}
	public void setSublimitKey(Long sublimitKey) {
		this.sublimitKey = sublimitKey;
	}
	public String getIcdChapter() {
		return icdChapter;
	}
	public void setIcdChapter(String icdChapter) {
		this.icdChapter = icdChapter;
	}
	public Long getIcdChapterKey() {
		return icdChapterKey;
	}
	public void setIcdChapterKey(Long icdChapterKey) {
		this.icdChapterKey = icdChapterKey;
	}
	public String getIcdBlock() {
		return icdBlock;
	}
	public void setIcdBlock(String icdBlock) {
		this.icdBlock = icdBlock;
	}
	public Long getIcdBlockKey() {
		return icdBlockKey;
	}
	public void setIcdBlockKey(Long icdBlockKey) {
		this.icdBlockKey = icdBlockKey;
	}
	public SelectValue getIcdChapterSelect() {
		return icdChapterSelect;
	}
	public void setIcdChapterSelect(SelectValue icdChapterSelect) {
		this.icdChapterSelect = icdChapterSelect;
	}
	public SelectValue getIcdBlockSelect() {
		return icdBlockSelect;
	}
	public void setIcdBlockSelect(SelectValue icdBlockSelect) {
		this.icdBlockSelect = icdBlockSelect;
	}
	public List<IcdSublimitMappingDto> getIcdCodeSelectList() {
		return IcdCodeSelectList;
	}
	public void setIcdCodeSelectList(List<IcdSublimitMappingDto> icdCodeSelectList) {
		IcdCodeSelectList = icdCodeSelectList;
	}	
	
}
