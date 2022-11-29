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

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="IMS_CLS_STG_HRM_DETAILS")
@NamedQueries({
	@NamedQuery(name ="HrmDetails.findByIntimationNo",query="SELECT r FROM HrmDetails r WHERE r.pchdClaimIntimationNo = :intimationNO")
})

public class HrmDetails extends AbstractEntity implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_HRM_DETAILS_KEY_GENERATOR", sequenceName = "SEQ_HRM_DETAILS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_HRM_DETAILS_KEY_GENERATOR" )
	@Column(name = "PCHD_SYS_ID")
	private Long key;
	
	@Column(name = "PCHD_HRM_ID ")
	private String pchdHrmId;
	
	@Column(name = "PCHD_PROVIDER_CODE")
	private String pchdProviderCode;
	
	@Column(name = "PCHD_ANH_NANH")
	private String pchdAnhNanh;
	
	@Column(name = "PCHD_SRG_PROC")
	private String pchdSurgicalProcedure;
	
	@Column(name = "PCHD_CLM_INTM_NO")
	private String pchdClaimIntimationNo;
	
	@Column(name = "PCHD_CLM_AMT")
	private Double pchdClaimAmount;
	
	@Column(name = "PCHD_PKG_AMT")
	private Double pchdPackageAmount;
	
	@Column(name = "PCHD_REP_REMARKS")
	private String pchdRepresentativeRemarks;
	
	@Column(name = "PCHD_REQ_REMARKS")
	private String pchdRequestRemarks;
	
	@Column(name = "PCHD_READ_FLG")
	private Integer pchdReadFlag;
	
	@Column(name = "PCHD_REQ_ID")
	private String pchdRequestId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PCHD_REQ_DT")
	private Date pchdRequestDate;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PCHD_ASSGN_DT ")
	private Date pchdAssigneeDate;
	
	@Column(name = "GAL_TRANS_ID")
	private Long galTransactionId;	

	@Column(name = "PCHD_DR_ID")
	private String doctorId;	
	
	@Column(name = "PCHD_DR_NAME")
	private String doctorName;
	 
	@Column(name = "PCHD_DR_TEL_NO")
	private String docTelephoneNo;
	
	@Column(name = "PCHD_REQ_TYP")
	private String requestType;
	
	
	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDocTelephoneNo() {
		return docTelephoneNo;
	}

	public void setDocTelephoneNo(String docTelephoneNo) {
		this.docTelephoneNo = docTelephoneNo;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPchdHrmId() {
		return pchdHrmId;
	}

	public void setPchdHrmId(String pchdHrmId) {
		this.pchdHrmId = pchdHrmId;
	}

	public String getPchdProviderCode() {
		return pchdProviderCode;
	}

	public void setPchdProviderCode(String pchdProviderCode) {
		this.pchdProviderCode = pchdProviderCode;
	}

	public String getPchdAnhNanh() {
		return pchdAnhNanh;
	}

	public void setPchdAnhNanh(String pchdAnhNanh) {
		this.pchdAnhNanh = pchdAnhNanh;
	}

	public String getPchdSurgicalProcedure() {
		return pchdSurgicalProcedure;
	}

	public void setPchdSurgicalProcedure(String pchdSurgicalProcedure) {
		this.pchdSurgicalProcedure = pchdSurgicalProcedure;
	}

	public String getPchdClaimIntimationNo() {
		return pchdClaimIntimationNo;
	}

	public void setPchdClaimIntimationNo(String pchdClaimIntimationNo) {
		this.pchdClaimIntimationNo = pchdClaimIntimationNo;
	}

	public Double getPchdClaimAmount() {
		return pchdClaimAmount;
	}

	public void setPchdClaimAmount(Double pchdClaimAmount) {
		this.pchdClaimAmount = pchdClaimAmount;
	}

	public Double getPchdPackageAmount() {
		return pchdPackageAmount;
	}

	public void setPchdPackageAmount(Double pchdPackageAmount) {
		this.pchdPackageAmount = pchdPackageAmount;
	}

	

	public String getPchdRepresentativeRemarks() {
		return pchdRepresentativeRemarks;
	}

	public void setPchdRepresentativeRemarks(String pchdRepresentativeRemarks) {
		this.pchdRepresentativeRemarks = pchdRepresentativeRemarks;
	}

	public String getPchdRequestRemarks() {
		return pchdRequestRemarks;
	}

	public void setPchdRequestRemarks(String pchdRequestRemarks) {
		this.pchdRequestRemarks = pchdRequestRemarks;
	}

	public Integer getPchdReadFlag() {
		return pchdReadFlag;
	}

	public void setPchdReadFlag(Integer pchdReadFlag) {
		this.pchdReadFlag = pchdReadFlag;
	}

	public String getPchdRequestId() {
		return pchdRequestId;
	}

	public void setPchdRequestId(String pchdRequestId) {
		this.pchdRequestId = pchdRequestId;
	}

	public Date getPchdRequestDate() {
		return pchdRequestDate;
	}

	public void setPchdRequestDate(Date pchdRequestDate) {
		this.pchdRequestDate = pchdRequestDate;
	}

	public Date getPchdAssigneeDate() {
		return pchdAssigneeDate;
	}

	public void setPchdAssigneeDate(Date pchdAssigneeDate) {
		this.pchdAssigneeDate = pchdAssigneeDate;
	}

	public Long getGalTransactionId() {
		return galTransactionId;
	}

	public void setGalTransactionId(Long galTransactionId) {
		this.galTransactionId = galTransactionId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
}
