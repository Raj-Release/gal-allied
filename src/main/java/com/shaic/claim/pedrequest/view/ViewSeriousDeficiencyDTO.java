package com.shaic.claim.pedrequest.view;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ViewSeriousDeficiencyDTO  extends AbstractTableDTO  implements Serializable {
	
	String intimationNumber;
	String hospitalCode;
	
	public String getIntimationNumber() {
		return intimationNumber;
	}
	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
}