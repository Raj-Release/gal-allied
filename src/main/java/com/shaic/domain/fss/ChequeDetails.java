package com.shaic.domain.fss;

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
@Entity
@Table(name="IMS_CLS_WRH_TRN_CHEQUE_DETAILS")
@NamedQueries({
	@NamedQuery(name="ChequeDetails.findChequeByStorageKey", query="SELECT m FROM ChequeDetails m where m.fileStorage is not null and m.fileStorage.key = :fileStorageKey and m.activateStatus is not null and m.activateStatus = 'Y'"),
	@NamedQuery(name="ChequeDetails.findByChequeNumber", query="SELECT m FROM ChequeDetails m where Upper(m.chequeNo) like :chequeNo and m.activateStatus is not null and m.activateStatus = 'Y'"),
	@NamedQuery(name="ChequeDetails.findChequeByKey", query="SELECT m FROM ChequeDetails m where m.key is not null and m.key = :key and m.activateStatus is not null and m.activateStatus = 'Y'")
})
public class ChequeDetails extends AbstractEntity {

	@Id
	@SequenceGenerator(name="IMS_CLS_WRH_TRN_CHEQUE_DETAILS_KEY_GENERATOR", sequenceName = "SEQ_CHEQUE_KEY" , allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_WRH_TRN_CHEQUE_DETAILS_KEY_GENERATOR" )
	@Column(name="CHEQUE_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="FILE_STORAGE_KEY", nullable=true)
	private FileStorage fileStorage;
	
	@Column(name="CHEQUE_NUMBER")
	private String chequeNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHEQUE_DATE")
	private Date chequeDate;
	
	@Column(name="BANK_NAME")
	private String bankName;
	
	@Column(name="BANK_BRANCH")
	private String bankBranch;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="ACTIVE_STATUS")
	private String activateStatus;
	
	public FileStorage getFileStorage() {
		return fileStorage;
	}

	public void setFileStorage(FileStorage fileStorage) {
		this.fileStorage = fileStorage;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getActivateStatus() {
		return activateStatus;
	}

	public void setActivateStatus(String activateStatus) {
		this.activateStatus = activateStatus;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}
	

}
