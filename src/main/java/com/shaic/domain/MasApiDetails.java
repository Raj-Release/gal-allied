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
@Entity
@Table(name = "MAS_API_DTLS")
@NamedQueries({
	@NamedQuery(name = "MasApiDetails.findByAuth", query = "SELECT o FROM MasApiDetails o where o.apiUserId = :userName and o.apiUserPwd = :password and o.activeFlag = 1 ")
})
public class MasApiDetails implements Serializable{

	private static final long serialVersionUID = -4706833731779331476L;

	@Id
	@SequenceGenerator(name="MAS_API_DTLS_KEY_GENERATOR", sequenceName = "SEQ_API_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_API_DTLS_KEY_GENERATOR" ) 
	@Column(name = "API_KEY")
	private Long key;
	
	@Column(name = "API_NAME")
	private String apiName;
	
	@Column(name = "API_USER_ID")
	private String apiUserId;
	
	@Column(name = "API_USER_PWD")
	private String apiUserPwd;
	
	@Column(name = "ACTIVE_FLAG")
	private Integer activeFlag;
	
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
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiUserId() {
		return apiUserId;
	}

	public void setApiUserId(String apiUserId) {
		this.apiUserId = apiUserId;
	}

	public String getApiUserPwd() {
		return apiUserPwd;
	}

	public void setApiUserPwd(String apiUserPwd) {
		this.apiUserPwd = apiUserPwd;
	}

	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
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
	
}
