package com.shaic.domain.preauth;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Status;


/**
 * The persistent class for the IMS_CLS_PRE_AUTH_QUERY_T database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_CASHLESS_QUERY")
@NamedQueries({
@NamedQuery(name="PreauthQuery.findAll", query="SELECT i FROM PreauthQuery i"),
@NamedQuery(name="PreauthQuery.findKey", query="SELECT o FROM PreauthQuery o where o.key = :preAuthKey"),
@NamedQuery(name="PreauthQuery.findBypreauth", query="SELECT o FROM PreauthQuery o where o.preauth.key = :preAuthPrimaryKey"),
@NamedQuery(name="PreauthQuery.findByLatestpreauth", query="SELECT o FROM PreauthQuery o where o.preauth.key = :preAuthKey order by o.key desc")
})
public class PreauthQuery extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_CASHLESS_QUERY_KEY_GENERATOR", sequenceName = "SEQ_CASHLESS_QUERY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_CASHLESS_QUERY_KEY_GENERATOR" ) 
	@Column(name="CASHLESS_QUERY_KEY")
	private Long key;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@OneToOne
	@JoinColumn(name="CASHLESS_KEY", nullable=false)
	private Preauth preauth;

//	@Column(name="MIGRATED_APPLICATION_ID")
//	private Long migratedApplicationId;

//	@Column(name="MIGRATED_CODE")
//	private String migratedCode;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="OFFICE_CODE")
	private String officeCode;

	@Column(name="QUERY_REMARKS")
	private String queryRemarks;
	
	@OneToOne
	@JoinColumn(name="STAGE_ID",nullable=false)
	private Stage stage;
	
	@OneToOne
	@JoinColumn(name="STATUS_ID",nullable=false)
	private Status status;
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long documentToken;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOCUMENT_RECEIVED_DATE")
	private Date documentReceivedDate;
	
	@Column(name = "DOCTOR_NOTE")
	private String doctorNote;
	
//	@Column(name="SUB_STATUS_ID")
//	private Long subStatusId;
//	
//	@Column(name="STATUS_DATE")
//	private Timestamp statusDate;
//
//	@Column(name="VERSION")
//	private Long version;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUERY_TYPE_ID", nullable = true)
	private MastersValue queryType;

	public PreauthQuery() {
	}

	public Long getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}


	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Preauth getPreauth() {
		return this.preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

//	public Long getMigratedApplicationId() {
//		return this.migratedApplicationId;
//	}

//	public void setMigratedApplicationId(Long migratedApplicationId) {
//		this.migratedApplicationId = migratedApplicationId;
//	}

//	public String getMigratedCode() {
//		return this.migratedCode;
//	}

//	public void setMigratedCode(String migratedCode) {
//		this.migratedCode = migratedCode;
//	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getOfficeCode() {
		return this.officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getQueryRemarks() {
		return this.queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public String getDoctorNote() {
		return doctorNote;
	}

	public void setDoctorNote(String doctorNote) {
		this.doctorNote = doctorNote;
	}

	public MastersValue getQueryType() {
		return queryType;
	}

	public void setQueryType(MastersValue queryType) {
		this.queryType = queryType;
	}
	
}