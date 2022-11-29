package com.shaic.claim.lumen.components;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LetterAddressDetails implements Serializable{

	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String state;
	private String pincode;
	
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@Override
	public String toString() {
		String taddressLine1 = (getAddressLine1() == null)?"":getAddressLine1();
		String taddressLine2 = (getAddressLine2() == null)?"":getAddressLine2();
		String taddressLine3 = (getAddressLine3() == null)?"":getAddressLine3();
		String tcity = (getCity() == null)?"":getCity();
		String tstate = (getState() == null)?"":getState();
		String tpincode = (getPincode() == null)?"":getPincode();		
		return taddressLine1+""+taddressLine2+""+taddressLine3+""+tcity+""+tstate+""+tpincode;
	}
}
