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
import javax.persistence.Transient;

@Entity
@Table(name="MAS_USER_CPU_MAPPING")
@NamedQueries({
@NamedQuery(name="UserCpuMapping.findCPUForUserId", query="SELECT c FROM UserCpuMapping c where c.userId = :userId and c.activeStatus = 1"),
@NamedQuery(name="UserCpuMapping.findEmpKey", query="SELECT c FROM UserCpuMapping c where c.id = :id"),
@NamedQuery(name="UserCpuMapping.findByUserId", query="SELECT c FROM UserCpuMapping c where c.userId = :userId")})
public class UserCpuMapping implements Serializable{

//@EmbeddedId	
//private UsertoCpuIDClass id;

@Id
@SequenceGenerator(name="IMS_CLS_SEQ_USER_CPU_KEY_GENERATOR", sequenceName = "SEQ_USER_CPU_KEY" , allocationSize = 1)
@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_SEQ_USER_CPU_KEY_GENERATOR" )
@Column(name="USER_CPU_KEY")
private Long id; 

@Column(name ="USER_ID")
protected String userId;

@Column(name ="CPU_CODE")
protected Long cpuCode;

@Column(name="CPU")
protected String cpu;

@Column(name = "CREATED_DATE")
private Date createdDate;

	
@Column(name="ACTIVE_STATUS")
private Long activeStatus;


public Long getActiveStatus() {
	return activeStatus;
}

public void setActiveStatus(Long activeStatus) {
	this.activeStatus = activeStatus;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public Long getCpuCode() {
	return cpuCode;
}

public void setCpuCode(Long cpuCode) {
	this.cpuCode = cpuCode;
}

public String getCpu() {
	return cpu;
}

public void setCpu(String cpu) {
	this.cpu = cpu;
}

public Date getCreatedDate() {
	return createdDate;
}

public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}


}
