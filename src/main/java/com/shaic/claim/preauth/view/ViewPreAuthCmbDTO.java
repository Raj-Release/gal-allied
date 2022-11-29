package com.shaic.claim.preauth.view;

import java.io.Serializable;

import com.shaic.arch.fields.dto.SelectValue;

public class ViewPreAuthCmbDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SelectValue preReferenceId;

	public SelectValue getPreReferenceId() {
		return preReferenceId;
	}

	public void setPreReferenceId(SelectValue preReferenceId) {
		this.preReferenceId = preReferenceId;
	}

}
