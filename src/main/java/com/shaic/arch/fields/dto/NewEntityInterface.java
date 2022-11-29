package com.shaic.arch.fields.dto;

import java.io.Serializable;

public interface NewEntityInterface extends Serializable {
	
	public boolean equals(Object obj);
	
	public int hashCode();
	
	public boolean isNew();
	
	public Long getKey();
	
	public void setKey();
	

}
