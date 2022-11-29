/**
 * 
 */
package com.shaic.domain;

/**
 * @author ntv.vijayar
 *
 */
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * The persistent class for the MAS_DIAGNOSIS_T database table.
 * 
 */
@Entity
@Table(name="MAS_DOCUMENT_TYPE")
@NamedQueries({
	@NamedQuery(name="DocumentCheckListMaster.findAll", query="SELECT m FROM DocumentCheckListMaster m where m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="DocumentCheckListMaster.findByKey",query="SELECT o FROM DocumentCheckListMaster o where o.key = :primaryKey"),
	@NamedQuery(name="DocumentCheckListMaster.findByValue",query="SELECT o FROM DocumentCheckListMaster o where o.value = :docTypeValue"),
	@NamedQuery(name= "DocumentCheckListMaster.findByMasterType", query="SELECT m FROM DocumentCheckListMaster m where m.masterType = :masterType and  m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name= "DocumentCheckListMaster.findByBenefitType", query="SELECT m FROM DocumentCheckListMaster m where m.masterType = :masterType and Lower(m.benefitType) = :benefitType and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name= "DocumentCheckListMaster.findMandatoryDocByBenefitType", query="SELECT m FROM DocumentCheckListMaster m where m.masterType = :masterType and m.mandatoryDocFlag is not null and m.mandatoryDocFlag = 'Y' and Lower(m.benefitType) = :benefitType and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name= "DocumentCheckListMaster.findByMasterTypeAndMandatoryRecord", query="SELECT m FROM DocumentCheckListMaster m where m.mandatoryDocFlag = 'Y' and m.masterType = :masterType and  m.activeStatus is not null and m.activeStatus = 1")
})

public class DocumentCheckListMaster extends AbstractEntity /*implements Serializable*/ {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOCUMENT_TYPE_KEY")
	private Long key;
	
	@Column(name="SEQUENCE_NUMBER")
	private Long sequenceNumber;
	
	@Column(name="VALUE")
	private String value;

	@Column(name="MANDATORY_DOCUMENT_FLAG")
	private String mandatoryDocFlag;
	
	@Column(name="REQUIRED_DOCUMENT_TYPE")
	private String requiredDocType;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name = "MASTER_TYPE")
	private String masterType;
	
	@Column(name = "BENEFIT_TYPE")
	private String benefitType;
	

	/*@Column(name="VERSION")
	private Long version;*/

	public DocumentCheckListMaster() {
		
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMandatoryDocFlag() {
		return mandatoryDocFlag;
	}

	public void setMandatoryDocFlag(String mandatoryDocFlag) {
		this.mandatoryDocFlag = mandatoryDocFlag;
	}

	public String getRequiredDocType() {
		return requiredDocType;
	}

	public void setRequiredDocType(String requiredDocType) {
		this.requiredDocType = requiredDocType;
	}

	

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/*public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}*/

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getMasterType() {
		return masterType;
	}

	public void setMasterType(String masterType) {
		this.masterType = masterType;
	}

	public String getBenefitType() {
		return benefitType;
	}

	public void setBenefitType(String benefitType) {
		this.benefitType = benefitType;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((DocumentCheckListMaster) obj).getKey());
        }
    }

    @Override
    public int hashCode() {
        if (key != null) {
            return key.hashCode();
        } else {
            return super.hashCode();
        }
    }

	

}