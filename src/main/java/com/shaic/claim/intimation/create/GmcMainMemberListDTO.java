
package com.shaic.claim.intimation.create;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shaic.domain.preauth.GmcMainMemberList;



public class GmcMainMemberListDTO implements Serializable {
	private static final long serialVersionUID = 3760145401215411454L;
	
	private Long key;

	private Long policySysId;

	private String policyNumber;

	private Integer endorsementNumber;

	private Long riskId;

	private String productCode;

	private String sectionCode;

	private Long memberId;

	private String mainMemberName;

	private String insuredName;

	private String employeeId;

	private Integer age;
	
	private String idCardNumber;
	
	private GmcMainMemberList gmcMainMemberList;

	public Long getPolicySysId() {
		return policySysId;
	}

	public void setPolicySysId(Long policySysId) {
		this.policySysId = policySysId;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public Integer getEndorsementNumber() {
		return endorsementNumber;
	}

	public void setEndorsementNumber(Integer endorsementNumber) {
		this.endorsementNumber = endorsementNumber;
	}

	public Long getRiskId() {
		return riskId;
	}

	public void setRiskId(Long riskId) {
		this.riskId = riskId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMainMemberName() {
		return mainMemberName;
	}

	public void setMainMemberName(String mainMemberName) {
		this.mainMemberName = mainMemberName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public GmcMainMemberList getGmcMainMemberList() {
		return gmcMainMemberList;
	}

	public void setGmcMainMemberList(GmcMainMemberList gmcMainMemberList) {
		this.gmcMainMemberList = gmcMainMemberList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result
				+ ((employeeId == null) ? 0 : employeeId.hashCode());
		result = prime
				* result
				+ ((endorsementNumber == null) ? 0 : endorsementNumber
						.hashCode());
		result = prime
				* result
				+ ((gmcMainMemberList == null) ? 0 : gmcMainMemberList
						.hashCode());
		result = prime * result
				+ ((idCardNumber == null) ? 0 : idCardNumber.hashCode());
		result = prime * result
				+ ((insuredName == null) ? 0 : insuredName.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result
				+ ((mainMemberName == null) ? 0 : mainMemberName.hashCode());
		result = prime * result
				+ ((memberId == null) ? 0 : memberId.hashCode());
		result = prime * result
				+ ((policyNumber == null) ? 0 : policyNumber.hashCode());
		result = prime * result
				+ ((policySysId == null) ? 0 : policySysId.hashCode());
		result = prime * result
				+ ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + ((riskId == null) ? 0 : riskId.hashCode());
		result = prime * result
				+ ((sectionCode == null) ? 0 : sectionCode.hashCode());
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
		GmcMainMemberListDTO other = (GmcMainMemberListDTO) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		if (endorsementNumber == null) {
			if (other.endorsementNumber != null)
				return false;
		} else if (!endorsementNumber.equals(other.endorsementNumber))
			return false;
		if (gmcMainMemberList == null) {
			if (other.gmcMainMemberList != null)
				return false;
		} else if (!gmcMainMemberList.equals(other.gmcMainMemberList))
			return false;
		if (idCardNumber == null) {
			if (other.idCardNumber != null)
				return false;
		} else if (!idCardNumber.equals(other.idCardNumber))
			return false;
		if (insuredName == null) {
			if (other.insuredName != null)
				return false;
		} else if (!insuredName.equals(other.insuredName))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (mainMemberName == null) {
			if (other.mainMemberName != null)
				return false;
		} else if (!mainMemberName.equals(other.mainMemberName))
			return false;
		if (memberId == null) {
			if (other.memberId != null)
				return false;
		} else if (!memberId.equals(other.memberId))
			return false;
		if (policyNumber == null) {
			if (other.policyNumber != null)
				return false;
		} else if (!policyNumber.equals(other.policyNumber))
			return false;
		if (policySysId == null) {
			if (other.policySysId != null)
				return false;
		} else if (!policySysId.equals(other.policySysId))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		if (riskId == null) {
			if (other.riskId != null)
				return false;
		} else if (!riskId.equals(other.riskId))
			return false;
		if (sectionCode == null) {
			if (other.sectionCode != null)
				return false;
		} else if (!sectionCode.equals(other.sectionCode))
			return false;
		return true;
	}

	}
