/**
 * 
 */
package com.shaic.domain.outpatient;

import java.sql.Timestamp;

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
import com.shaic.domain.OPClaim;

/**
 * @author ntv.narasimhaj
 *
 */

@Entity
@Table(name="IMS_CLS_OP_HC_HOSPITAL_DTLS")
@NamedQueries({
@NamedQuery(name="OPHCHospitalDetails.findAll", query="SELECT r FROM OPHCHospitalDetails r"),
@NamedQuery(name ="OPHCHospitalDetails.findByKey",query="SELECT r FROM OPHCHospitalDetails r WHERE r.key = :key"),
@NamedQuery(name ="OPHCHospitalDetails.findByHCKey",query="SELECT r FROM OPHCHospitalDetails r WHERE r.opHealthCheckup.key = :key")
})
public class OPHCHospitalDetails extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_HOSPITAL_KEY_GENERATOR", sequenceName = "SEQ_OP_HC_HOSPITAL_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_HOSPITAL_KEY_GENERATOR")
	@Column(name="OP_HC_HOSPITAL_DTLS_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="OP_HEALTH_CHECKUP_KEY", nullable=false)
	private OPHealthCheckup opHealthCheckup;
	
	@OneToOne
	@JoinColumn(name = "CLAIM_KEY", nullable = false)
	private OPClaim claim;
	
	@Column(name="BENEFIT_AVAILED")
	private String benefitAvailedId;
	
	@Column(name="HOSPITAL_TYPE")
	private Long hospitalType;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="HOSPITAL_NAME")
	private String hospitalName;
	
	@Column(name="HOSPITAL_ADDRESS")
	private String hospitalAddress;
	
	@Column(name="HOSPITAL_CONSULTATION_NAME")
	private String hospitalConsulationName;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="HOSPITAL_CONTACT_NO")
	private String hospitalContactNo;
	
	@Column(name="HOSPITAL_FAX_NO")
	private String hospitalFaxNo;
	
	@Column(name="HOSPITAL_DOCTORS_NAME")
	private String hospitalDoctorsName;
	
	@Column(name="CLINIC_FLAG")
	private String clinicFlag;
	
	@Override
	public Long getKey() {
		return key;
	}
	
	@Override
	public void setKey(Long key) {
		this.key = key;
	}

	public OPHealthCheckup getOpHealthCheckup() {
		return opHealthCheckup;
	}

	public void setOpHealthCheckup(OPHealthCheckup opHealthCheckup) {
		this.opHealthCheckup = opHealthCheckup;
	}

	public OPClaim getClaim() {
		return claim;
	}

	public void setClaim(OPClaim claim) {
		this.claim = claim;
	}

	public String getBenefitAvailedId() {
		return benefitAvailedId;
	}

	public void setBenefitAvailedId(String benefitAvailedId) {
		this.benefitAvailedId = benefitAvailedId;
	}

	public Long getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(Long hospitalType) {
		this.hospitalType = hospitalType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getHospitalConsulationName() {
		return hospitalConsulationName;
	}

	public void setHospitalConsulationName(String hospitalConsulationName) {
		this.hospitalConsulationName = hospitalConsulationName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getHospitalContactNo() {
		return hospitalContactNo;
	}

	public void setHospitalContactNo(String hospitalContactNo) {
		this.hospitalContactNo = hospitalContactNo;
	}

	public String getHospitalFaxNo() {
		return hospitalFaxNo;
	}

	public void setHospitalFaxNo(String hospitalFaxNo) {
		this.hospitalFaxNo = hospitalFaxNo;
	}

	public String getHospitalDoctorsName() {
		return hospitalDoctorsName;
	}

	public void setHospitalDoctorsName(String hospitalDoctorsName) {
		this.hospitalDoctorsName = hospitalDoctorsName;
	}

	public String getClinicFlag() {
		return clinicFlag;
	}

	public void setClinicFlag(String clinicFlag) {
		this.clinicFlag = clinicFlag;
	}

	


}
