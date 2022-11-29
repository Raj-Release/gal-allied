

/**
 * 
 */
package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.CompositeKey;

/**
 * @author vijayar
 *
 */

@Entity
@Table(name="PGIT_END_APPR_DTLS")
//@Table(name="IMS_CLS_STG_INTIMATION")
@NamedQueries({
	//@NamedQuery(name="PremiaIntimationTable.findAll", query="SELECT o FROM PremiaIntimationTable o where o.giSavedType <> :savedType and o.giHospitalTypeYn <> :hospType order by o.giCreatedOn desc "),
//	@NamedQuery(name="PremiaIntimationTable.findAll", query="SELECT o FROM PremiaIntimationTable o where o.giSavedType <> :savedType and o.giPACategory = :claimType order by o.giCreatedOn asc"),
	@NamedQuery(name="PremiaEndorsementTable.findAll", query="SELECT o FROM PremiaEndorsementTable o where o.readFlag is null order by o.giCreatedOn asc"),
	@NamedQuery(name="PremiaEndorsementTable.findByPolicyNo", query="SELECT o FROM PremiaEndorsementTable o where o.policyNumber = :policyNumber order by o.giCreatedOn asc"),
})
//@IdClass(CompositeKey.class)
public class PremiaEndorsementTable extends AbstractEntity implements Serializable {
	
	@Id
	@Column(name = "AED_POL_SYS_ID")
	private Long policySysId;
	
	@Id
	@Column(name = "AED_POL_END_NO_IDX")
	private Long endorsementIndex;
	
	@Id
	@Column(name = "AED_POL_NO")
	private String policyNumber;
	
	@Column(name = "AED_RISK_ID")
	private String riskId;
	
	@Column(name = "AED_PROD_CODE")
	private String productCode;
	
	@Column(name = "AED_READ_FLG")
	private String readFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AED_CR_DT")
	private Date giCreatedOn;
	
	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

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

	public String getRiskId() {
		return riskId;
	}

	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public Date getGiCreatedOn() {
		return giCreatedOn;
	}

	public void setGiCreatedOn(Date giCreatedOn) {
		this.giCreatedOn = giCreatedOn;
	}

	public Long getEndorsementIndex() {
		return endorsementIndex;
	}

	public void setEndorsementIndex(Long endorsementIndex) {
		this.endorsementIndex = endorsementIndex;
	}

	/*@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((endorsementIndex == null) ? 0 : endorsementIndex.hashCode());
		result = prime * result
				+ ((giCreatedOn == null) ? 0 : giCreatedOn.hashCode());
		result = prime * result
				+ ((policyNumber == null) ? 0 : policyNumber.hashCode());
		result = prime * result
				+ ((policySysId == null) ? 0 : policySysId.hashCode());
		result = prime * result
				+ ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result
				+ ((readFlag == null) ? 0 : readFlag.hashCode());
		result = prime * result + ((riskId == null) ? 0 : riskId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PremiaEndorsementTable other = (PremiaEndorsementTable) obj;
		if (endorsementIndex == null) {
			if (other.endorsementIndex != null)
				return false;
		} else if (!endorsementIndex.equals(other.endorsementIndex))
			return false;
		if (giCreatedOn == null) {
			if (other.giCreatedOn != null)
				return false;
		} else if (!giCreatedOn.equals(other.giCreatedOn))
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
		if (readFlag == null) {
			if (other.readFlag != null)
				return false;
		} else if (!readFlag.equals(other.readFlag))
			return false;
		if (riskId == null) {
			if (other.riskId != null)
				return false;
		} else if (!riskId.equals(other.riskId))
			return false;
		return true;
	}*/

}
