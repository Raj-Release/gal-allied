package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
//@Table(name="IMS_CLS_PED_INITIATE_HIS")
public class CompositeKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	
	public CompositeKey(){
		
	}
	
	public CompositeKey(Long primaryKey){
		this.primaryKey = primaryKey;
	}

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + 1;
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
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (primaryKey != other.primaryKey)
			return false;
		return true;
	}

}
