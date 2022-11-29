/**
 * 
 */
package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author SARAVANA
 *
 */
@Entity
@Table(name = "INITMATION_IMPORT_VALIDATION")
@NamedQueries({
	@NamedQuery(name = "IntimationImportValidation.findAll", query = "SELECT m FROM BankMaster m"),
	@NamedQuery(name = "IntimationImportValidation.findByPolicyNumber", query = "SELECT m FROM IntimationImportValidation m where m.policyNumber = :policyNumber"),
})
public class IntimationImportValidation implements Serializable {
	
	private static final long serialVersionUID = 8063515700387603522L;

	@Id
	@Column(name = "POLICY_NUMBER")
	private String policyNumber;
	
	@Column(name= "PREMIA_INTIMATION_COUNT")
	private Long premiaIntimationCount;

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Long getPremiaIntimationCount() {
		return premiaIntimationCount;
	}

	public void setPremiaIntimationCount(Long premiaIntimationCount) {
		this.premiaIntimationCount = premiaIntimationCount;
	}


}
