package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="MAS_SEC_USER")
@NamedQueries({
	@NamedQuery(name="EmpSecUser.findAll", query="SELECT m FROM EmpSecUser m where m.activeStatus is not null and m.activeStatus = 'Y' order by m.userName asc"),
	@NamedQuery(name="EmpSecUser.findByEmpName", query="SELECT m FROM EmpSecUser m WHERE Lower(m.userName) LIKE :empName and m.activeStatus is not null and m.activeStatus = 'Y'"),
	@NamedQuery(name="EmpSecUser.findEmpByUserType", query = "SELECT m FROM EmpSecUser m WHERE Lower(m.userType) LIKE :userType and m.activeStatus is not null and m.activeStatus = 'Y'"),
	@NamedQuery(name="EmpSecUser.getEmpByLoginId", query="SELECT m FROM EmpSecUser m WHERE Lower(m.userId) LIKE :loginId and m.activeStatus is not null and m.activeStatus = 'Y'"),
	@NamedQuery(name="EmpSecUser.findByKey", query = "SELECT m FROM EmpSecUser m WHERE m.key = :primaryKey and m.activeStatus is not null and m.activeStatus = 'Y'")
})
public class EmpSecUser {

	@Id
	@Column(name="USER_KEY")
	private Long key;

	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="EMP_ID")
	private String empId;
	
	@Column(name="USER_NAME")
	private String userName;
	 
	@Column(name="USER_TYPE")
	private String userType;
	
	@Column(name="MIN_AMOUNT")
	private Long minAmt;
	    
	@Column(name="MAX_AMOUNT")
	private Long maxAmt;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	  
	@Column(name="MODIFIED_BY")
	private String midifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFIED_DATE")
	private Date moifiedDate;  
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(Long minAmt) {
		this.minAmt = minAmt;
	}

	public Long getMaxAmt() {
		return maxAmt;
	}

	public void setMaxAmt(Long maxAmt) {
		this.maxAmt = maxAmt;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getMidifiedBy() {
		return midifiedBy;
	}

	public void setMidifiedBy(String midifiedBy) {
		this.midifiedBy = midifiedBy;
	}

	public Date getMoifiedDate() {
		return moifiedDate;
	}

	public void setMoifiedDate(Date moifiedDate) {
		this.moifiedDate = moifiedDate;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
		
}
