/**
 * 
 */
package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author ntv.vijayar
 *
 */

/**
 * The persistent class for the IMS_CLS_RRC_REQUEST database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_RRC_DETAILS")
@NamedQueries({
	@NamedQuery(name="RRCDetails.findAll", query="SELECT r FROM RRCDetails r"),
	@NamedQuery(name="RRCDetails.findByRequestKey", query="SELECT r FROM RRCDetails r where r.rrcRequest = :rrcRequestKey"),
	@NamedQuery(name="RRCDetails.findByRequestKeyAndEmployeeId", query="SELECT r.rrcRequest FROM RRCDetails r where r.employeeId = :employeeId and r.rrcRequest IN (:rrcRequestKeylist)"),
	@NamedQuery(name="RRCDetails.findByEmployeeId", query="SELECT r FROM RRCDetails r where r.employeeId = :employeeId and r.rrcRequest = :rrcRequestKey"),
	@NamedQuery(name="RRCDetails.getEmployeeDataByRequestKey", query="SELECT r.rrcDetailsKey,r.rrcRequest, r.employeeId, r.employeeName,r.creditTypeId.key,r.score,r.remarks FROM RRCDetails r where r.rrcRequest = :rrcRequestKey")
})

public class RRCDetails  extends AbstractEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1086997314154372927L;

	@Id
	@SequenceGenerator(name="IMS_CLS_RRC_DETAILS_GENERATOR", sequenceName = "SEQ_RRC_DETAILS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_RRC_DETAILS_GENERATOR" ) 
	@Column(name="RRC_DETAILS_KEY")
	private Long rrcDetailsKey;
	
//	@OneToOne
//	@JoinColumn(name="RRC_REQUEST_KEY", nullable=true)
//	private RRCRequest rrcRequest;
	
	@Column(name="RRC_REQUEST_KEY")
	private Long rrcRequest;
	
	@OneToOne
	@JoinColumn(name="CREDIT_TYPE_ID", nullable=true)
	private MastersValue creditTypeId;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "SCORE")
	private Long score;
	
	@Column(name = "EMPLOYEE_ID")
	private String employeeId;
	
	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;
	
	@Column(name = "EMPLOYEE_ZONE")
	private String employeeZone;
	
	@Column(name = "EMPLOYEE_DEPT")
	private String employeeDept;
	
	@OneToOne
	@JoinColumn(name = "CONTRIBUTOR_TYPE_ID", nullable=true)
	private MastersValue contributorTypeId;

	public Long getRrcDetailsKey() {
		return rrcDetailsKey;
	}

	public void setRrcDetailsKey(Long rrcDetailsKey) {
		this.rrcDetailsKey = rrcDetailsKey;
	}

//	public RRCRequest getRrcRequest() {
//		return rrcRequest;
//	}
//
//	public void setRrcRequest(RRCRequest rrcRequest) {
//		this.rrcRequest = rrcRequest;
//	}

	public MastersValue getCreditTypeId() {
		return creditTypeId;
	}

	public void setCreditTypeId(MastersValue creditTypeId) {
		this.creditTypeId = creditTypeId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/*public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}*/

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeZone() {
		return employeeZone;
	}

	public void setEmployeeZone(String employeeZone) {
		this.employeeZone = employeeZone;
	}

	public String getEmployeeDept() {
		return employeeDept;
	}

	public void setEmployeeDept(String employeeDept) {
		this.employeeDept = employeeDept;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getRrcRequest() {
		return rrcRequest;
	}

	public void setRrcRequest(Long rrcRequest) {
		this.rrcRequest = rrcRequest;
	}

	public MastersValue getContributorTypeId() {
		return contributorTypeId;
	}

	public void setContributorTypeId(MastersValue contributorTypeId) {
		this.contributorTypeId = contributorTypeId;
	}
	
	
	
	
}
