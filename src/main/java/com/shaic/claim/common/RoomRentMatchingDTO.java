package com.shaic.claim.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomRentMatchingDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8211273569546040302L;
	private Long id;
	private String itemName;
	private String billNumber;
	private Double claimedNoOfDays = 0d;
	private Double allowedNoOfDays = 0d;
	private Double perDayAmount = 0d;
	private Boolean status = false;
	private Long identityId;
	private Map<Long, Integer> allocatedValues;
	private List<NursingChargesMatchingDTO> nursingChargesDTOList;
	
	public RoomRentMatchingDTO() {
		nursingChargesDTOList = new ArrayList<NursingChargesMatchingDTO>();
		allocatedValues = new HashMap<Long, Integer>();
	}
	
	public String getItemName() {
		return itemName;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public Double getClaimedNoOfDays() {
		return claimedNoOfDays;
	}
	public Double getPerDayAmount() {
		return perDayAmount;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public void setClaimedNoOfDays(Double claimedNoOfDays) {
		this.claimedNoOfDays = claimedNoOfDays;
	}
	public void setPerDayAmount(Double perDayAmount) {
		this.perDayAmount = perDayAmount;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Double getAllowedNoOfDays() {
		return allowedNoOfDays;
	}
	public List<NursingChargesMatchingDTO> getNursingChargesDTOList() {
		return nursingChargesDTOList;
	}

	public void setNursingChargesDTOList(
			List<NursingChargesMatchingDTO> nursingChargesDTOList) {
		this.nursingChargesDTOList = nursingChargesDTOList;
	}

	public void setAllowedNoOfDays(Double allowedNoOfDays) {
		this.allowedNoOfDays = allowedNoOfDays;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Map<Long, Integer> getAllocatedValues() {
		return allocatedValues;
	}

	public void setAllocatedValues(Map<Long, Integer> allocatedValues) {
		this.allocatedValues = allocatedValues;
	}

	public Long getIdentityId() {
		return identityId;
	}

	public void setIdentityId(Long identityId) {
		this.identityId = identityId;
	}
}
