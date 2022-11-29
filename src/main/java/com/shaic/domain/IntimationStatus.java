package com.shaic.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_INTIMATION_STATUS_T database table.
 * 
 */
@Entity
@Table(name="MAS_INTIMATION_STATUS")
@NamedQuery(name="IntimationStatus.findAll", query="SELECT m FROM IntimationStatus m")
public class IntimationStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name="\"KEY\"")
	private BigDecimal key;


	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	@Column(name="ACTIVE_STATUS_DATE")
	private BigDecimal activeStatusDate;

	@Column(name="ACTIVE_STATUS_REMARKS")
	private String activeStatusRemarks;

	@Column(name="AUDIT_ID")
	private String auditId;

	@Column(name="CREATED_DATE")
	private BigDecimal createdDate;

	@Column(name="DEPLOYMENT_ID")
	private String deploymentId;

	private String description;

	@Column(name="\"DISPLAY\"")
	private String display;

	@Column(name="EFFECTIVE_FROM")
	private BigDecimal effectiveFrom;

	@Column(name="EFFECTIVE_TO")
	private BigDecimal effectiveTo;

	@Column(name="FK_INTEGRATED_SYSTEM_KEY_1")
	private BigDecimal fkIntegratedSystemKey1;

	@Column(name="FK_INTEGRATED_SYSTEM_KEY_2")
	private BigDecimal fkIntegratedSystemKey2;

	@Column(name="FK_INTEGRATED_SYSTEM_KEY_3")
	private BigDecimal fkIntegratedSystemKey3;

	@Column(name="FK_INTEGRATED_SYSTEM_KEY_4")
	private BigDecimal fkIntegratedSystemKey4;

	@Column(name="FK_INTEGRATED_SYSTEM_KEY_5")
	private BigDecimal fkIntegratedSystemKey5;

	@Column(name="FK_NAMESPACE_KEY")
	private BigDecimal fkNamespaceKey;

	@Column(name="FK_SERVICE_APPVERMOD_ASGT_KEY")
	private BigDecimal fkServiceAppvermodAsgtKey;

	@Column(name="INTEGRATED_SYSTEM_VALUE_1")
	private String integratedSystemValue1;

	@Column(name="INTEGRATED_SYSTEM_VALUE_2")
	private String integratedSystemValue2;

	@Column(name="INTEGRATED_SYSTEM_VALUE_3")
	private String integratedSystemValue3;

	@Column(name="INTEGRATED_SYSTEM_VALUE_4")
	private String integratedSystemValue4;

	@Column(name="INTEGRATED_SYSTEM_VALUE_5")
	private String integratedSystemValue5;

	@Column(name="MODIFIED_DATE")
	private BigDecimal modifiedDate;

	@Column(name="PERSISTED_DATE")
	private Timestamp persistedDate;

	@Column(name="\"TYPE\"")
	private String type;

	@Column(name="\"VALUE\"")
	private String value;

	@Column(name="\"VERSION\"")
	private BigDecimal version;

	public IntimationStatus() {
	}

	public String getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public BigDecimal getActiveStatusDate() {
		return this.activeStatusDate;
	}

	public void setActiveStatusDate(BigDecimal activeStatusDate) {
		this.activeStatusDate = activeStatusDate;
	}

	public String getActiveStatusRemarks() {
		return this.activeStatusRemarks;
	}

	public void setActiveStatusRemarks(String activeStatusRemarks) {
		this.activeStatusRemarks = activeStatusRemarks;
	}

	public String getAuditId() {
		return this.auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public BigDecimal getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(BigDecimal createdDate) {
		this.createdDate = createdDate;
	}

	public String getDeploymentId() {
		return this.deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplay() {
		return this.display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public BigDecimal getEffectiveFrom() {
		return this.effectiveFrom;
	}

	public void setEffectiveFrom(BigDecimal effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public BigDecimal getEffectiveTo() {
		return this.effectiveTo;
	}

	public void setEffectiveTo(BigDecimal effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public BigDecimal getFkIntegratedSystemKey1() {
		return this.fkIntegratedSystemKey1;
	}

	public void setFkIntegratedSystemKey1(BigDecimal fkIntegratedSystemKey1) {
		this.fkIntegratedSystemKey1 = fkIntegratedSystemKey1;
	}

	public BigDecimal getFkIntegratedSystemKey2() {
		return this.fkIntegratedSystemKey2;
	}

	public void setFkIntegratedSystemKey2(BigDecimal fkIntegratedSystemKey2) {
		this.fkIntegratedSystemKey2 = fkIntegratedSystemKey2;
	}

	public BigDecimal getFkIntegratedSystemKey3() {
		return this.fkIntegratedSystemKey3;
	}

	public void setFkIntegratedSystemKey3(BigDecimal fkIntegratedSystemKey3) {
		this.fkIntegratedSystemKey3 = fkIntegratedSystemKey3;
	}

	public BigDecimal getFkIntegratedSystemKey4() {
		return this.fkIntegratedSystemKey4;
	}

	public void setFkIntegratedSystemKey4(BigDecimal fkIntegratedSystemKey4) {
		this.fkIntegratedSystemKey4 = fkIntegratedSystemKey4;
	}

	public BigDecimal getFkIntegratedSystemKey5() {
		return this.fkIntegratedSystemKey5;
	}

	public void setFkIntegratedSystemKey5(BigDecimal fkIntegratedSystemKey5) {
		this.fkIntegratedSystemKey5 = fkIntegratedSystemKey5;
	}

	public BigDecimal getFkNamespaceKey() {
		return this.fkNamespaceKey;
	}

	public void setFkNamespaceKey(BigDecimal fkNamespaceKey) {
		this.fkNamespaceKey = fkNamespaceKey;
	}

	public BigDecimal getFkServiceAppvermodAsgtKey() {
		return this.fkServiceAppvermodAsgtKey;
	}

	public void setFkServiceAppvermodAsgtKey(BigDecimal fkServiceAppvermodAsgtKey) {
		this.fkServiceAppvermodAsgtKey = fkServiceAppvermodAsgtKey;
	}

	public String getIntegratedSystemValue1() {
		return this.integratedSystemValue1;
	}

	public void setIntegratedSystemValue1(String integratedSystemValue1) {
		this.integratedSystemValue1 = integratedSystemValue1;
	}

	public String getIntegratedSystemValue2() {
		return this.integratedSystemValue2;
	}

	public void setIntegratedSystemValue2(String integratedSystemValue2) {
		this.integratedSystemValue2 = integratedSystemValue2;
	}

	public String getIntegratedSystemValue3() {
		return this.integratedSystemValue3;
	}

	public void setIntegratedSystemValue3(String integratedSystemValue3) {
		this.integratedSystemValue3 = integratedSystemValue3;
	}

	public String getIntegratedSystemValue4() {
		return this.integratedSystemValue4;
	}

	public void setIntegratedSystemValue4(String integratedSystemValue4) {
		this.integratedSystemValue4 = integratedSystemValue4;
	}

	public String getIntegratedSystemValue5() {
		return this.integratedSystemValue5;
	}

	public void setIntegratedSystemValue5(String integratedSystemValue5) {
		this.integratedSystemValue5 = integratedSystemValue5;
	}

	public BigDecimal getKey() {
		return this.key;
	}

	public void setKey(BigDecimal key) {
		this.key = key;
	}

	public BigDecimal getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(BigDecimal modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Timestamp getPersistedDate() {
		return this.persistedDate;
	}

	public void setPersistedDate(Timestamp persistedDate) {
		this.persistedDate = persistedDate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}