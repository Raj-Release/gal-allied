package com.shaic.claim.icdSublimitMapping;

import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;

public class IcdSublimitMappingDto {
	
	private Long icdSublimitMapKey;
	private SelectValue IcdCodeSelect;
	private String icdDescripiton;
	private Long icdDescripitonKey;	
	private boolean selected;
	private String sno;
	
	public SelectValue getIcdCodeSelect() {
		return IcdCodeSelect;
	}
	public void setIcdCodeSelect(SelectValue icdCodeSelect) {
		IcdCodeSelect = icdCodeSelect;
	}
	public String getIcdDescripiton() {
		return icdDescripiton;
	}
	public void setIcdDescripiton(String icdDescripiton) {
		this.icdDescripiton = icdDescripiton;
	}
	public Long getIcdDescripitonKey() {
		return icdDescripitonKey;
	}
	public void setIcdDescripitonKey(Long icdDescripitonKey) {
		this.icdDescripitonKey = icdDescripitonKey;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public Long getIcdSublimitMapKey() {
		return icdSublimitMapKey;
	}
	public void setIcdSublimitMapKey(Long icdSublimitMapKey) {
		this.icdSublimitMapKey = icdSublimitMapKey;
	}
	
}
