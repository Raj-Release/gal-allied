package com.shaic.domain.preauth;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "IMS_CLS_USER_LOG_DTLS")
@NamedQueries({
	@NamedQuery(name = "UserLoginDetails.findAll", query = "SELECT i FROM UserLoginDetails i"),
	@NamedQuery(name = "UserLoginDetails.findByUserId", query = "SELECT i FROM UserLoginDetails i where (i.loginId) LIKE :loginId and i.logOnFlag = 'Y' order by i.key desc"),
	@NamedQuery(name="UserLoginDetails.find", query="SELECT u FROM UserLoginDetails u where Lower(u.loginId) = :userId and u.logOnFlag = 'Y' order by u.key desc"),
	@NamedQuery(name = "UserLoginDetails.findKey", query = "SELECT i FROM UserLoginDetails i where (i.loginId) LIKE :loginId order by i.key desc")
})

public class UserLoginDetails extends AbstractEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_USER_LOG_DTLS_KEY_GENERATOR", sequenceName = "SEQ_USER_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_USER_LOG_DTLS_KEY_GENERATOR" ) 
	@Column(name="USER_KEY", updatable=false)
	private Long key;
	
	@Column(name = "LOGIN_ID")
	private String loginId;
	
	
	@Column(name = "CURRENT_DATE")
	private Date currentDate;
	
	@Column(name = "LOGIN_NAME")
	private String loginName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_DATE_TIME")
	private Date loginDateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGOUT_DATE_TIME")
	private Date logoutDateTime;
	
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "CREATED_BY")
	private String createdBY;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "LOG_ON_FALG")
	private String logOnFlag;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;


	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Date getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	public Date getLogoutDateTime() {
		return logoutDateTime;
	}

	public void setLogoutDateTime(Date logoutDateTime) {
		this.logoutDateTime = logoutDateTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedBY() {
		return createdBY;
	}

	public void setCreatedBY(String createdBY) {
		this.createdBY = createdBY;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getLogOnFlag() {
		return logOnFlag;
	}

	public void setLogOnFlag(String logOnFlag) {
		this.logOnFlag = logOnFlag;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	
	
	

}
