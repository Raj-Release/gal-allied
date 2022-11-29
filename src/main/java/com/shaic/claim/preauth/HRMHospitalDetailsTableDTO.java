package com.shaic.claim.preauth;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class HRMHospitalDetailsTableDTO extends AbstractTableDTO  implements Serializable{

	private String hardCodedString;	
	private String hardCodedStringValue;
	private String hardCodedString1;
	private String hardCodedStringValue1;
	
	public String getHardCodedString() {
		return hardCodedString;
	}
	public void setHardCodedString(String hardCodedString) {
		this.hardCodedString = hardCodedString;
	}
	public String getHardCodedStringValue() {
		return hardCodedStringValue;
	}
	public void setHardCodedStringValue(String hardCodedStringValue) {
		this.hardCodedStringValue = hardCodedStringValue;
	}
	public String getHardCodedString1() {
		return hardCodedString1;
	}
	public void setHardCodedString1(String hardCodedString1) {
		this.hardCodedString1 = hardCodedString1;
	}
	public String getHardCodedStringValue1() {
		return hardCodedStringValue1;
	}
	public void setHardCodedStringValue1(String hardCodedStringValue1) {
		this.hardCodedStringValue1 = hardCodedStringValue1;
	}
	
	

	
}
