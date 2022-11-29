package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long policySysId;
	private Long endorsementIndex;
	private String policyNumber;
	
	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	CompositeKey(){
		
	}

	public CompositeKey(Long policySysId, Long endorsementIndex,String policyNumber) {
		super();
		this.policySysId = policySysId;
		this.endorsementIndex = endorsementIndex;
		this.policyNumber = policyNumber;
	}

	public Long getPolicySysId() {
		return policySysId;
	}

	public void setPolicySysId(Long policySysId) {
		this.policySysId = policySysId;
	}

	public Long getEndorsementIndex() {
		return endorsementIndex;
	}

	public void setEndorsementIndex(Long endorsementIndex) {
		this.endorsementIndex = endorsementIndex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((endorsementIndex == null) ? 0 : endorsementIndex.hashCode());
		result = prime * result
				+ ((policySysId == null) ? 0 : policySysId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKey other = (CompositeKey) obj;
		if (endorsementIndex == null) {
			if (other.endorsementIndex != null)
				return false;
		} else if (!endorsementIndex.equals(other.endorsementIndex))
			return false;
		if (policySysId == null) {
			if (other.policySysId != null)
				return false;
		} else if (!policySysId.equals(other.policySysId))
			return false;
		return true;
	}
	

}
