package com.shaic.claim.sublimit;

import java.io.Serializable;

public class ViewSublimitDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subLimitName;
	private String subLimitAmt;

	public String getSubLimitName() {
		return subLimitName;
	}

	public void setSubLimitName(String subLimitName) {
		this.subLimitName = subLimitName;
	}

	public String getSubLimitAmt() {
		return subLimitAmt;
	}

	public void setSubLimitAmt(String subLimitAmt) {
		this.subLimitAmt = subLimitAmt;
	}

}
