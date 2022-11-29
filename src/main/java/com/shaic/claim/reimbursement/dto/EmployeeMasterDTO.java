/**
 * 
 */
package com.shaic.claim.reimbursement.dto;

/**
 * @author ntv.vijayar
 *
 */
public class EmployeeMasterDTO {

	private Long employeeTblkey;
	
	private String employeeId;
	
	private String employeeName;
	
	private String employeeFirstName;
	
	private String employeeMiddleName;
	
	private String employeeLastName;
	
	private String employeeDept;
	
	private String employeeZone;
	
	private String empCpuId;
	
	private String loginId;

	public Long getEmployeeTblkey() {
		return employeeTblkey;
	}

	public void setEmployeeTblkey(Long employeeTblkey) {
		this.employeeTblkey = employeeTblkey;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/*public String getEmployeeName() {
		//return employeeName;
		return employeeFirstName + employeeMiddleName + employeeLastName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}*/

	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeMiddleName() {
		return employeeMiddleName;
	}

	public void setEmployeeMiddleName(String employeeMiddleName) {
		this.employeeMiddleName = employeeMiddleName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getEmployeeDept() {
		return employeeDept;
	}

	public void setEmployeeDept(String employeeDept) {
		this.employeeDept = employeeDept;
	}

	public String getEmployeeZone() {
		return employeeZone;
	}

	public void setEmployeeZone(String employeeZone) {
		this.employeeZone = employeeZone;
	}

	public String getEmpCpuId() {
		return empCpuId;
	}

	public void setEmpCpuId(String empCpuId) {
		this.empCpuId = empCpuId;
	}

	public String getEmployeeName() {
	//	return employeeName;
		return employeeFirstName + employeeMiddleName + employeeLastName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	
}
