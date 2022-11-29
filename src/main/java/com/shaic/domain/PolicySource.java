package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="POLICY_SOURCE")
@NamedQueries({
@NamedQuery(name="PolicySource.findByPolicyNumber", query = "SELECT o FROM PolicySource o where o.policyNumber = :policyNumber")
})
public class PolicySource {

	@Id
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;

	@Column(name = "POLICY_SOURCE_FROM")
	private String policySourceFrom;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicySourceFrom() {
		return policySourceFrom;
	}

	public void setPolicySourceFrom(String policySourceFrom) {
		this.policySourceFrom = policySourceFrom;
	}
	
	

}
