package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "IMS_CLS_INSURED_PED_DETAILS")
@NamedQueries({
		@NamedQuery(name = "InsuredPedDetails.findAll", query = "SELECT i FROM InsuredPedDetails i"),
		@NamedQuery(name = "InsuredPedDetails.findAllDescriptions", query = "SELECT i.pedDescription FROM InsuredPedDetails i"),
		@NamedQuery(name = "InsuredPedDetails.findByinsuredKey", query = "select o from InsuredPedDetails o where o.insuredKey = :insuredKey") })
public class InsuredPedDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5518915920000548200L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_INSURED_PED_KEY_GENERATOR", sequenceName = "SEQ_CLS_INSURED_PED_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_INSURED_PED_KEY_GENERATOR" ) 
	@Column(name = "INSURED_PED_KEY")
	private Long key;

	@Column(name = "INSURED_KEY")
	private Long insuredKey;

	@Column(name = "PED_CODE")
	private String pedCode;

	@Column(name = "PED_DESCRIPTION")
	private String pedDescription;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PED_EFF_FRM_DT")
	private Date pedEffectiveFromDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PED_EFF_TO_DT")
	private Date pedEffectiveToDate;
	
	@Column(name = "PED_TYPE")
	private String pedType;
	
	@Transient
	private String strEffectivedFromDate;
	
	@Transient
	private String strEffectiveToDate;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	public Long getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}
	public String getPedCode() {
		return pedCode;
	}
	public void setPedCode(String pedCode) {
		this.pedCode = pedCode;
	}
	public String getPedDescription() {
		return pedDescription;
	}
	public void setPedDescription(String pedDescription) {
		this.pedDescription = pedDescription;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public Date getPedEffectiveFromDate() {
		return pedEffectiveFromDate;
	}

	public void setPedEffectiveFromDate(Date pedEffectiveFromDate) {
		this.pedEffectiveFromDate = pedEffectiveFromDate;
	}

	public Date getPedEffectiveToDate() {
		return pedEffectiveToDate;
	}

	public void setPedEffectiveToDate(Date pedEffectiveToDate) {
		this.pedEffectiveToDate = pedEffectiveToDate;
	}

	public String getPedType() {
		return pedType;
	}

	public void setPedType(String pedType) {
		this.pedType = pedType;
	}

	public String getStrEffectivedFromDate() {
		return strEffectivedFromDate;
	}

	public void setStrEffectivedFromDate(String strEffectivedFromDate) {
		this.strEffectivedFromDate = strEffectivedFromDate;
	}

	public String getStrEffectiveToDate() {
		return strEffectiveToDate;
	}

	public void setStrEffectiveToDate(String strEffectiveToDate) {
		this.strEffectiveToDate = strEffectiveToDate;
	}

	

}
