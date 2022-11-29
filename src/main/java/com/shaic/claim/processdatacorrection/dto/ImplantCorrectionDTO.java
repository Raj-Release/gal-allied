package com.shaic.claim.processdatacorrection.dto;

import java.util.Date;

import com.shaic.arch.table.AbstractRowEnablerDTO;

public class ImplantCorrectionDTO extends AbstractRowEnablerDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8123361047117854711L;

	private Long key;
	
	private String implantName;

	private Double implantCost; 
	
	private String implantType; 

	private String actualImplantName;

	private Double actualImplantCost; 
	
	private String actualImplantType;
	
	private Boolean hasChanges = false;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getImplantName() {
		return implantName;
	}

	public void setImplantName(String implantName) {
		this.implantName = implantName;
	}

	public Double getImplantCost() {
		return implantCost;
	}

	public void setImplantCost(Double implantCost) {
		this.implantCost = implantCost;
	}

	public String getImplantType() {
		return implantType;
	}

	public void setImplantType(String implantType) {
		this.implantType = implantType;
	}

	public String getActualImplantName() {
		return actualImplantName;
	}

	public void setActualImplantName(String actualImplantName) {
		this.actualImplantName = actualImplantName;
	}

	public Double getActualImplantCost() {
		return actualImplantCost;
	}

	public void setActualImplantCost(Double actualImplantCost) {
		this.actualImplantCost = actualImplantCost;
	}

	public String getActualImplantType() {
		return actualImplantType;
	}

	public void setActualImplantType(String actualImplantType) {
		this.actualImplantType = actualImplantType;
	}

	public Boolean getHasChanges() {
		return hasChanges;
	}

	public void setHasChanges(Boolean hasChanges) {
		this.hasChanges = hasChanges;
	} 
	
}
