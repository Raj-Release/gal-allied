package com.shaic.newcode.wizard.dto;

import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.HospitalPackage;

public class ANHPackageReatesDto {
	
	private String sno;
	private String procedureName;
	private String minDays;
	private String maxDays;
	private String singleRoomAC;
	private String sharingOccupancySingleRoomNonAc;
	private String generalWard;
	private String semiPrivateRoomAc;
	private String specialRoomNonAc;
	private String singleRoomNonAc;
	private String sharingSemiPrivateRoomNonAc;
	private String deluxRoom;
	
	public ANHPackageReatesDto(){
		
	}
	
	public ANHPackageReatesDto setPackageRates(HospitalPackage anhPackage){
		
		this.minDays = anhPackage.getMinDurationOfStay() != null ? anhPackage.getMinDurationOfStay().toString() : "";
		this.maxDays = anhPackage.getMaxDurationOfStay() != null ? anhPackage.getMaxDurationOfStay().toString() : "";
		
		if(anhPackage.getRoomCategory() != null && anhPackage.getRoomCategory().getKey() != null ){
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SINGLE_ROOM_AC)){  
			this.singleRoomAC = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SHARING_OCCUPANCY_SINGLE_ROOM_NON_AC)){  
			this.sharingOccupancySingleRoomNonAc = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.GENERAL_WARD)){
			this.generalWard = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SEMI_PRIVATE_ROOM_AC)){
			this.semiPrivateRoomAc = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SPECIAL_ROOM_NON_AC)){
			this.specialRoomNonAc = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SINGLE_ROOM_NON_AC)){
			this.singleRoomNonAc = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SHARING_SEMI_PRIVATE_ROOM_NON_AC)){
			this.sharingSemiPrivateRoomNonAc = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.DELUXE)){
			this.deluxRoom = anhPackage.getRate() != null ? String.valueOf(anhPackage.getRate().intValue()) : "";
		}	
		}
		return this;
		
	}
	
	public ANHPackageReatesDto(HospitalPackage anhPackage){
		
		this.minDays = anhPackage.getMinDurationOfStay() != null ? anhPackage.getMinDurationOfStay().toString() : "";
		this.maxDays = anhPackage.getMaxDurationOfStay() != null ? anhPackage.getMaxDurationOfStay().toString() : "";
		
		if(anhPackage.getRoomCategory() != null && anhPackage.getRoomCategory().getKey() != null ){
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SINGLE_ROOM_AC)){  
			this.singleRoomAC = anhPackage.getRate().toString();
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SHARING_OCCUPANCY_SINGLE_ROOM_NON_AC)){  
			this.sharingOccupancySingleRoomNonAc = anhPackage.getRate().toString();
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.GENERAL_WARD)){
			this.generalWard = anhPackage.getRate().toString();
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SEMI_PRIVATE_ROOM_AC)){
			this.semiPrivateRoomAc = anhPackage.getRate().toString();
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SPECIAL_ROOM_NON_AC)){
			this.specialRoomNonAc = anhPackage.getRate().toString();
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SINGLE_ROOM_NON_AC)){
			this.singleRoomNonAc = anhPackage.getRate().toString();
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.SHARING_SEMI_PRIVATE_ROOM_NON_AC)){
			this.sharingSemiPrivateRoomNonAc = anhPackage.getRate().toString();
		}
		if(anhPackage.getRoomCategory().getKey().equals(ReferenceTable.DELUXE)){
			this.deluxRoom = anhPackage.getRate().toString();
		}	
		}
		
	}
	
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}	
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public String getSingleRoomAC() {
		return singleRoomAC;
	}
	public void setSingleRoomAC(String singleRoomAC) {
		this.singleRoomAC = singleRoomAC;
	}
	public String getMinDays() {
		return minDays;
	}
	public void setMinDays(String minDays) {
		this.minDays = minDays;
	}
	public String getMaxDays() {
		return maxDays;
	}
	public void setMaxDays(String maxDays) {
		this.maxDays = maxDays;
	}
	public String getSharingOccupancySingleRoomNonAc() {
		return sharingOccupancySingleRoomNonAc;
	}
	public void setSharingOccupancySingleRoomNonAc(
			String sharingOccupancySingleRoomNonAc) {
		this.sharingOccupancySingleRoomNonAc = sharingOccupancySingleRoomNonAc;
	}
	public String getGeneralWard() {
		return generalWard;
	}
	public void setGeneralWard(String generalWard) {
		this.generalWard = generalWard;
	}
	public String getSemiPrivateRoomAc() {
		return semiPrivateRoomAc;
	}
	public void setSemiPrivateRoomAc(String semiPrivateRoomAc) {
		this.semiPrivateRoomAc = semiPrivateRoomAc;
	}
	public String getSpecialRoomNonAc() {
		return specialRoomNonAc;
	}
	public void setSpecialRoomNonAc(String specialRoomNonAc) {
		this.specialRoomNonAc = specialRoomNonAc;
	}
	public String getSingleRoomNonAc() {
		return singleRoomNonAc;
	}
	public void setSingleRoomNonAc(String singleRoomNonAc) {
		this.singleRoomNonAc = singleRoomNonAc;
	}
	public String getSharingSemiPrivateRoomNonAc() {
		return sharingSemiPrivateRoomNonAc;
	}
	public void setSharingSemiPrivateRoomNonAc(String sharingSemiPrivateRoomNonAc) {
		this.sharingSemiPrivateRoomNonAc = sharingSemiPrivateRoomNonAc;
	}
	public String getDeluxRoom() {
		return deluxRoom;
	}
	public void setDeluxRoom(String deluxRoom) {
		this.deluxRoom = deluxRoom;
	}
	
	
	

}
