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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author karthikeyan.r
 * The persistent class for the IMS_CLS_LUMEN_LETTER database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_LUMEN_LETTER")
@NamedQueries({
	@NamedQuery(name="LumenLetterDetails.findAll", query="SELECT m FROM LumenLetterDetails m"),
	@NamedQuery(name="LumenLetterDetails.findByLumenKey", query="SELECT m FROM LumenLetterDetails m where m.lumenRequest.key = :lumenReqKey")	
})
public class LumenLetterDetails extends AbstractEntity implements Serializable  {

	@Id
	@SequenceGenerator(name="IMS_CLS_LUMEN_LETTER_DETAILS_GENERATOR", sequenceName = "SEQ_LUMEN_LETTER_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LUMEN_LETTER_DETAILS_GENERATOR")
	@Column(name="LUMEN_LETTER_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_REQUEST_KEY", nullable=false)
	private LumenRequest lumenRequest;
	
	@Column(name = "LETTER_TO")
	private String letterTo;
	
	@Column(name = "SUBJECT")
	private String letterSubject;
	
	@Column(name = "LETTER_CONTENT")
	private String letterContent;
	
	@Column(name = "ADDRESS_1")
	private String addressLine1;
	
	@Column(name = "ADDRESS_2")
	private String addressLine2;
	
	@Column(name = "ADDRESS_3")
	private String addressLine3;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "PINCODE")
	private String pincode;
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long documentToken;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}

	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}

	public String getLetterTo() {
		return letterTo;
	}

	public void setLetterTo(String letterTo) {
		this.letterTo = letterTo;
	}

	public String getLetterSubject() {
		return letterSubject;
	}

	public void setLetterSubject(String letterSubject) {
		this.letterSubject = letterSubject;
	}

	public String getLetterContent() {
		return letterContent;
	}

	public void setLetterContent(String letterContent) {
		this.letterContent = letterContent;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
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

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
}
