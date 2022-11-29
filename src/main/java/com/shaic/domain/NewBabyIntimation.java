package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the IMS_CLS_NEW_BABY_INTIMATION database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_NEW_BABY_INTIMATION")
@NamedQueries({
	@NamedQuery(name="NewBabyIntimation.findAll", query="SELECT i FROM NewBabyIntimation i"),
	@NamedQuery(name="NewBabyIntimation.findByIntimation", query="SELECT i FROM NewBabyIntimation i where i.intimation.key = :intimationKey")
})
public class NewBabyIntimation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NEWBABY_INTIMATION_KEY")
	@SequenceGenerator(name="SEQ_NEW_BABY_INTIMATION_KEY_GENERATOR", sequenceName = "SEQ_NEW_BABY_INTIMATION_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_NEW_BABY_INTIMATION_KEY_GENERATOR" ) 
	private Long key;

	@ManyToOne()
	@JoinColumn(name="INTIMATION_KEY")
	private GalaxyIntimation intimation;
	
	@Column(nullable = true, columnDefinition = "Varchar", name="ACTIVE_STATUS", length = 1)
	private Boolean activeStatus;

	public GalaxyIntimation getIntimation() {
		return intimation;
	}

	public void setIntimation(GalaxyIntimation intimation) {
		this.intimation = intimation;
	}

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="BABY_RELATIONSHIP_ID", insertable=true,updatable=true,nullable=false,unique=true)
	private MastersValue babyRelationship;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;

	@Column(name="BABY_NAME")
	private String name;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	
	public NewBabyIntimation() {
		intimation = new GalaxyIntimation();
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Boolean getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}

	public MastersValue getBabyRelationship() {
		return babyRelationship;
	}

	public void setBabyRelationship(MastersValue babyRelationship) {
		this.babyRelationship = babyRelationship;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((NewBabyIntimation) obj).getKey());
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