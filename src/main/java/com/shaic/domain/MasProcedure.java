//package com.shaic.domain;
//
//import java.io.Serializable;
//import javax.persistence.*;
//import java.sql.Timestamp;
//
//
///**
// * The persistent class for the MAS_PROCEDURE database table.
// * 
// */
//@Entity
//@Table(name="MAS_PROCEDURE")
//@NamedQuery(name="MasProcedure.findAll", query="SELECT m FROM MasProcedure m")
//public class MasProcedure implements Serializable {
//	
//	
//	private static final long serialVersionUID = -4161936970970219700L;
//
//	@Id
//	@Column(name="PROCEDURE_KEY")
//	private long key;
//
//	@Column(name="ACTIVE_STATUS")
//	private Long activeStatus;
//	
//
////	@Column(name="ACTIVE_STATUS_DATE")
////	private Timestamp activeStatusDate;
//
//	@Column(name="CREATED_BY")
//	private String createdBy;
//
//	@Column(name="CREATED_DATE")
//	private Timestamp createdDate;
//
//	@Column(name="DAY_CARE_FLAG")
//	private String dayCareFlag;
//
//	@Column(name="MODIFIED_BY")
//	private String modifiedBy;
//
//	@Column(name="MODIFIED_DATE")
//	private Timestamp modifiedDate;
//
//	@Column(name="PROCEDURE_CODE")
//	private String procedureCode;
//
//	@Column(name="PROCEDURE_NAME")
//	private String procedureName;
//
////	@Column(name="\"VERSION\"")
////	private Long version;
//
//	public MasProcedure() {
//	}
//
//	public long getKey() {
//		return this.key;
//	}
//
//	public void setKey(long key) {
//		this.key = key;
//	}
//
//	public Long getActiveStatus() {
//		return this.activeStatus;
//	}
//
//	public void setActiveStatus(Long activeStatus) {
//		this.activeStatus = activeStatus;
//	}
//
////	public Timestamp getActiveStatusDate() {
////		return this.activeStatusDate;
////	}
////
////	public void setActiveStatusDate(Timestamp activeStatusDate) {
////		this.activeStatusDate = activeStatusDate;
////	}
//
//	public String getCreatedBy() {
//		return this.createdBy;
//	}
//
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}
//
//	public Timestamp getCreatedDate() {
//		return this.createdDate;
//	}
//
//	public void setCreatedDate(Timestamp createdDate) {
//		this.createdDate = createdDate;
//	}
//
//	public String getDayCareFlag() {
//		return this.dayCareFlag;
//	}
//
//	public void setDayCareFlag(String dayCareFlag) {
//		this.dayCareFlag = dayCareFlag;
//	}
//
//	public String getModifiedBy() {
//		return this.modifiedBy;
//	}
//
//	public void setModifiedBy(String modifiedBy) {
//		this.modifiedBy = modifiedBy;
//	}
//
//	public Timestamp getModifiedDate() {
//		return this.modifiedDate;
//	}
//
//	public void setModifiedDate(Timestamp modifiedDate) {
//		this.modifiedDate = modifiedDate;
//	}
//
//	public String getProcedureCode() {
//		return this.procedureCode;
//	}
//
//	public void setProcedureCode(String procedureCode) {
//		this.procedureCode = procedureCode;
//	}
//
//	public String getProcedureName() {
//		return this.procedureName;
//	}
//
//	public void setProcedureName(String procedureName) {
//		this.procedureName = procedureName;
//	}
//
////	public Long getVersion() {
////		return this.version;
////	}
////
////	public void setVersion(Long version) {
////		this.version = version;
////	}
//
//}