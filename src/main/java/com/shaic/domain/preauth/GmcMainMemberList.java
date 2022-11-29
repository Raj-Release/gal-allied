
package com.shaic.domain.preauth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
* The Master for MAS_DOC_REJECT.
* 
*/
@Entity
@Table(name = "PGIV_MAIN_MEMBER_LIST_ALL")
@NamedQueries({
	@NamedQuery(name = "GmcMainMemberList.findAll", query = "SELECT i FROM GmcMainMemberList i"),
	@NamedQuery(name = "GmcMainMemberList.findByMemberId", query = "SELECT i FROM GmcMainMemberList i where i.memberId = :memberId and i.policyNumber = :policyNumber"),
	@NamedQuery(name = "GmcMainMemberList.findByPolicyNo", query = "SELECT i FROM GmcMainMemberList i where  i.policyNumber = :policyNumber"),
	@NamedQuery(name = "GmcMainMemberList.findByRiskId", query = "SELECT i FROM GmcMainMemberList i where  i.riskId = :riskId and i.policyNumber = :policyNumber"),
	
})

public class GmcMainMemberList implements Serializable {
	private static final long serialVersionUID = 3760145401215411454L;

	@Column(name = "POL_SYS_ID")
	private Long policySysId;

	@Column(name = "POL_NO")
	private String policyNumber;

	@Column(name = "END_NO_IDX")
	private Integer endorsementNumber;

	@Id
	@Column(name = "RISK_ID")
	private Long riskId;

	@Column(name = "PROD_CODE")
	private String productCode;
	
	@Column(name="SECTIONCODE")
	private String sectionCode;
	
	@Column(name="MEMBER_ID")
	private Long memberId;
	
	@Column(name="MAIN_MEMBER_NAME")
	private String mainMemberName;
	
	@Column(name="INSURED_NAME")
	private String insuredName;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeId;
	
	@Column(name="AGE")
	private Integer age;
	
	@Column(name="ID_CARD_NO")
	private String idCardNumber;
	
	@Column(name="MEMBER_TYPE")
	private String memberType;
	
	@Column(name="REC_TYPE")
	private String recType;
	
	@Column(name="EFFECTIVE_FM_DM")
	private Date effectiveFromDate;
	
	@Column(name="EFFECTIVE_TO_DT")
	private Date effectiveToDate;

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
		result = prime * result
				+ ((idCardNumber == null) ? 0 : idCardNumber.hashCode());
		result = prime * result
				+ ((insuredName == null) ? 0 : insuredName.hashCode());
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
		GmcMainMemberList other = (GmcMainMemberList) obj;
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

	public String getMemberType() {
		return memberType;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public Date getEffectiveToDate() {
		return effectiveToDate;
	}

	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	}
