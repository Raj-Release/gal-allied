package com.shaic.newcode.wizard.dto;

import java.util.List;

public class HospitalPackageRatesDto {
	
	private String hospitalDetails;
	private List<ANHPackageReatesDto> packageReatesList;
	public String getHospitalDetails() {
		return hospitalDetails;
	}
	public void setHospitalDetails(String hospitalDetails) {
		this.hospitalDetails = hospitalDetails;
	}
	public List<ANHPackageReatesDto> getPackageReatesList() {
		return packageReatesList;
	}
	public void setPackageReatesList(List<ANHPackageReatesDto> packageReatesList) {
		this.packageReatesList = packageReatesList;
	}
	
}
