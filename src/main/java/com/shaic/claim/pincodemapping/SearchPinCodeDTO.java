package com.shaic.claim.pincodemapping;

import java.io.Serializable;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;


public class SearchPinCodeDTO extends AbstractSearchDTO implements Serializable{

	/**
	 * 
	 */
	
	private String pinCode;

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
}
