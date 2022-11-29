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
import javax.persistence.Transient;

/**
 * The persistent class for the MAS_COUNTRY_T database table.
 * 
 */
@Entity
@Table(name="MAS_EMPLOYEE")
@NamedQueries({
	@NamedQuery(name="TmpEmployee.findAll", query="SELECT m FROM TmpEmployee m where m.activeStatus is not null and m.activeStatus = 1 order by m.empFirstName asc"),
	@NamedQuery(name="TmpEmployee.findByCPUId", query="SELECT m FROM TmpEmployee m WHERE m.empCPUId = :cpuId and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.findByEmpName", query="SELECT m FROM TmpEmployee m WHERE Lower(m.empFirstName) = :empName and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.getEmpByName", query="SELECT m FROM TmpEmployee m WHERE m.empFirstName LIKE :empName and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.getEmpByLoginId", query="SELECT m FROM TmpEmployee m WHERE Lower(m.loginId) LIKE :loginId and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.findByKey", query = "SELECT m FROM TmpEmployee m WHERE m.key = :primaryKey and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.getEmpByLoginIdWithInactive", query="SELECT m FROM TmpEmployee m WHERE Lower(m.loginId) LIKE :loginId"),
	@NamedQuery(name="TmpEmployee.findAllEmployees", query="SELECT m FROM TmpEmployee m order by m.empFirstName asc"),
	@NamedQuery(name="TmpEmployee.findByEmpRole", query="SELECT m FROM TmpEmployee m WHERE m.empRole is not null and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.findByEmpId", query="SELECT m FROM TmpEmployee m WHERE Lower(m.empId) = :empId and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.getEmployeeName", query="SELECT m FROM TmpEmployee m WHERE m.createdDate = (SELECT MAX(o.createdDate) FROM TmpEmployee o where o.empId in(:empIds))"),
	@NamedQuery(name="TmpEmployee.findByEmpIdWithInactive", query="SELECT m FROM TmpEmployee m WHERE Lower(m.empId) = :empId "),
	@NamedQuery(name="TmpEmployee.findEmpListByEmpIds", query="SELECT m FROM TmpEmployee m WHERE m.empId IN (:empList) and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.findPCCRole", query="SELECT m FROM TmpEmployee m WHERE m.pccRole = :pccRole and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.findPCCEmpIds", query="SELECT m FROM TmpEmployee m WHERE m.empId in (:empIds) and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="TmpEmployee.getEmpByExactLoginId", query="SELECT m FROM TmpEmployee m WHERE Lower(m.loginId) = :loginId and m.activeStatus is not null and m.activeStatus = 1")

})
public class TmpEmployee implements Serializable {

	private static final long serialVersionUID = 1086997314154372927L;

	@Id
	@SequenceGenerator(name="MAS_EMPLOYEE_KEY_GENERATOR", sequenceName = "SEQ_EMPLOYEE_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MAS_EMPLOYEE_KEY_GENERATOR" )
	@Column(name = "EMPLOYEE_KEY")
	private Long key;
	
	@Column(name="EMP_ID")
	private String empId;
	
	@Column(name = "LOGIN_ID")
	private String loginId;
	
//	@Column(name="ORGANIZATION_UNIT_ID")
	@Transient
	private Long organizationUnitId;

	@Column(name="EMP_FIRST_NAME")
	private String empFirstName;

	@Column(name="EMP_MIDDLE_NAME")
	private String empMiddleName;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="EMP_LAST_NAME")
	private String empLastName;
	
	@Column(name="EMP_CPU_ID")
	private Long empCPUId;
	
	@Column(name="AUTH_MIN_AMT")
	private Long authminAmt;
	
	@Column(name="AUTH_MAX_AMT")
	private Long authmaxAmt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="EMP_ROLE")
	private String empRole;
	
	@Column(name="PCC_ROLE")
	private String pccRole;
	
	@Transient
	private String employeeWithNames;

	public Long getOrganizationUnitId() {
		return organizationUnitId;
	}

	public void setOrganizationUnitId(Long organizationUnitId) {
		this.organizationUnitId = organizationUnitId;
	}

	public String getEmpFirstName() {
		return empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	public String getEmpMiddleName() {
		return empMiddleName;
	}

	public void setEmpMiddleName(String empMiddleName) {
		this.empMiddleName = empMiddleName;
	}

	public String getEmpLastName() {
		return empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	public Long getEmpCPUId() {
		return empCPUId;
	}

	public void setEmpCPUId(Long empCPUId) {
		this.empCPUId = empCPUId;
	}
	
	public Long getAuthminAmt() {
		return authminAmt;
	}

	public void setAuthminAmt(Long authminAmt) {
		this.authminAmt = authminAmt;
	}

	public Long getAuthmaxAmt() {
		return authmaxAmt;
	}

	public void setAuthmaxAmt(Long authmaxAmt) {
		this.authmaxAmt = authmaxAmt;
	}

	public String getEmployeeWithNames() {
		return (this.empId + " - " + (this.empFirstName != null ? this.empFirstName: "") + (this.empMiddleName != null ? (" - " + this.empMiddleName) : "") + (this.empLastName != null ? (" - " + this.empLastName) : ""));
	}

	public void setEmployeeWithNames(String employeeWithNames) {
		this.employeeWithNames = (this.empId + " - " + this.empFirstName + " - " + (this.empMiddleName != null ? this.empMiddleName : "" ) + " - " + this.empLastName);
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getKey() {
		return key;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
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

	@Override
	public String toString() {
		return "TmpEmployee [key=" + key + ", empId=" + empId + ", loginId="
				+ loginId + ", organizationUnitId=" + organizationUnitId
				+ ", empFirstName=" + empFirstName + ", empMiddleName="
				+ empMiddleName + ", activeStatus=" + activeStatus
				+ ", empLastName=" + empLastName + ", empCPUId=" + empCPUId
				+ ", authminAmt=" + authminAmt + ", authmaxAmt=" + authmaxAmt
				+ ", createdDate=" + createdDate + ", modifiedDate="
				+ modifiedDate + ", employeeWithNames=" + employeeWithNames
				+ "]";
	}

	public String getEmpRole() {
		return empRole;
	}

	public void setEmpRole(String empRole) {
		this.empRole = empRole;
	}

	public String getPccRole() {
		return pccRole;
	}

	public void setPccRole(String pccRole) {
		this.pccRole = pccRole;
	}
	
}