package com.shaic.claim.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shaic.arch.fields.dto.SelectValue;

public class NursingChargesMatchingDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8211273569546040302L;
	private Long id;
	private String itemName;
	private String billNumber;
	private Double claimedNoOfDays;
	private Double perDayAmount;
	private Double allocatedClaimedNoOfDays;
	private Double unAllocatedDays;
	private Double mapToRoomDays;
	private Long mappingId;
	private SelectValue mappingRoomRentKeys;
	private List<SelectValue> listValues = new ArrayList<SelectValue>();
	private Long key;
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
	public Double getAllocatedClaimedNoOfDays() {
		return allocatedClaimedNoOfDays;
	}
	public Double getUnAllocatedDays() {
		return unAllocatedDays;
	}
	public Double getMapToRoomDays() {
		return mapToRoomDays;
	}
	public void setAllocatedClaimedNoOfDays(Double allocatedClaimedNoOfDays) {
		this.allocatedClaimedNoOfDays = allocatedClaimedNoOfDays;
	}
	public void setUnAllocatedDays(Double unAllocatedDays) {
		this.unAllocatedDays = unAllocatedDays;
	}
	public void setMapToRoomDays(Double mapToRoomDays) {
		this.mapToRoomDays = mapToRoomDays;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMappingId() {
		return mappingId;
	}
	public void setMappingId(Long mappingId) {
		this.mappingId = mappingId;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((NursingChargesMatchingDTO) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }
	public SelectValue getMappingRoomRentKeys() {
		return mappingRoomRentKeys;
	}
	public void setMappingRoomRentKeys(SelectValue mappingRoomRentKeys) {
		this.mappingRoomRentKeys = mappingRoomRentKeys;
	}
	public List<SelectValue> getListValues() {
		return listValues;
	}
	public void setListValues(List<SelectValue> listValues) {
		this.listValues = listValues;
	}
}
