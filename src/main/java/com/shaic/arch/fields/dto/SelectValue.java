package com.shaic.arch.fields.dto;

import java.io.Serializable;

public class SelectValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6149844628869871420L;
	
	private Long id;
	
	private String value;
	
	private String commonValue;
	
	private String sourceRiskId;	
	private String relationshipWithProposer;
	private String nameAsPerBankAccount;
	private String roleName;
	
	public SelectValue()
	{
		
	}
	
	public SelectValue(Long id, String value)
	{
		this.id = id;
		this.value = value;
	}
	
	/*public SelectValue(SelectValue input)
	{
		this.id = input.id;
		this.value= input.value;
		this.commonValue=input.commonValue;
	}
	*/
	public SelectValue(Long id, String value, String commonValue)
	{
		this.id = id;
		this.value = value;
		this.commonValue = commonValue;
	}

	public SelectValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}

	 @Override
	    public boolean equals(Object obj) {
	        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
	            return false;
	        } else if (getId() == null) {
	            return obj == this;
	        } else {
	            return getId().equals(((SelectValue) obj).getId());
	        }
	    }

	    @Override
	    public int hashCode() {
	        if (id != null) {
	            return id.hashCode();
	        } else {
	            return super.hashCode();
	        }
	    }

		public String getCommonValue() {
			return commonValue;
		}

		public void setCommonValue(String commonValue) {
			this.commonValue = commonValue;
		}

		public String getSourceRiskId() {
			return sourceRiskId;
		}

		public void setSourceRiskId(String sourceRiskId) {
			this.sourceRiskId = sourceRiskId;
		}

		public String getRelationshipWithProposer() {
			return relationshipWithProposer;
		}

		public void setRelationshipWithProposer(String relationshipWithProposer) {
			this.relationshipWithProposer = relationshipWithProposer;
		}

		public String getNameAsPerBankAccount() {
			return nameAsPerBankAccount;
		}

		public void setNameAsPerBankAccount(String nameAsPerBankAccount) {
			this.nameAsPerBankAccount = nameAsPerBankAccount;
		}
		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

}
