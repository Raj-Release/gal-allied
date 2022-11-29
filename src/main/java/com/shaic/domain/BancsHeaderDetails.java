package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
/**
 * @author karthikeyan.r
 * The persistent class for the MAS_HOSPITAL_SCORE database table.
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name="MAS_BANCS_WS_USER_DTLS")
@NamedQueries({
	@NamedQuery(name="BancsHeaderDetails.findAll", query="SELECT m FROM BancsHeaderDetails m"),
	@NamedQuery(name="BancsHeaderDetails.findByRequestType", query="SELECT m FROM BancsHeaderDetails m WHERE m.webserviceReqType = :type")
})
public class BancsHeaderDetails extends AbstractEntity implements Serializable  {

	@Id
	@Column(name = "WS_USER_DTLS_KEY")
	private Long key;
	
	@Column(name = "WS_NAME")
	private String webserviceName;
	
	@Column(name = "WS_DESCRIPTION")
	private String webserviceDesc;
	
	@Column(name = "WS_URL")
	private String webserviceUrl;
	
	@Column(name = "WS_TRANS_ID_SEQ")
	private String webserviceSeq;
	
	@Column(name = "WS_BUSSINESS_CHANNEL")
	private String webserviceBC;
	
	@Column(name = "WS_USER_CODE")
	private String webserviceUserCode;
	
	@Column(name = "WS_ROLE_CODE")
	private String webserviceRoleCode;
	
	@Column(name = "WS_REQUEST_TYPE")
	private String webserviceReqType;
	
	@Column(name = "ACTIVE_STATUS")
	private String activeStatus;
	
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
	
	@Column(name = "WS_USER_NAME")
	private String wsUserName;
	
	@Column(name = "WS_PASSWORD")
	private String wsPassword;
	
	@Override
	public Long getKey() {
		return key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public String getWebserviceName() {
		return webserviceName;
	}

	public void setWebserviceName(String webserviceName) {
		this.webserviceName = webserviceName;
	}

	public String getWebserviceDesc() {
		return webserviceDesc;
	}

	public void setWebserviceDesc(String webserviceDesc) {
		this.webserviceDesc = webserviceDesc;
	}

	public String getWebserviceUrl() {
		return webserviceUrl;
	}

	public void setWebserviceUrl(String webserviceUrl) {
		this.webserviceUrl = webserviceUrl;
	}

	public String getWebserviceSeq() {
		return webserviceSeq;
	}

	public void setWebserviceSeq(String webserviceSeq) {
		this.webserviceSeq = webserviceSeq;
	}

	public String getWebserviceBC() {
		return webserviceBC;
	}

	public void setWebserviceBC(String webserviceBC) {
		this.webserviceBC = webserviceBC;
	}

	public String getWebserviceUserCode() {
		return webserviceUserCode;
	}

	public void setWebserviceUserCode(String webserviceUserCode) {
		this.webserviceUserCode = webserviceUserCode;
	}

	public String getWebserviceRoleCode() {
		return webserviceRoleCode;
	}

	public void setWebserviceRoleCode(String webserviceRoleCode) {
		this.webserviceRoleCode = webserviceRoleCode;
	}

	public String getWebserviceReqType() {
		return webserviceReqType;
	}

	public void setWebserviceReqType(String webserviceReqType) {
		this.webserviceReqType = webserviceReqType;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
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
	
	public String getWsUserName() {
		return wsUserName;
	}

	public void setWsUserName(String wsUserName) {
		this.wsUserName = wsUserName;
	}

	public String getWsPassword() {
		return wsPassword;
	}

	public void setWsPassword(String wsPassword) {
		this.wsPassword = wsPassword;
	}

}
