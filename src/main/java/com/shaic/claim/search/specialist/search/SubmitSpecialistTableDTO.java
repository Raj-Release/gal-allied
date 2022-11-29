package com.shaic.claim.search.specialist.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SubmitSpecialistTableDTO extends AbstractTableDTO implements Serializable {

	private static final long serialVersionUID = -4784233338391482167L;

	
	

	private Long claimKey;
	private Long intimationkey;
	private String policyNo;
	private Long rodKey;
	private Long lOBId;
	private Boolean isReimburementFlag;
	private String doctorId;
	
	private Integer sno;

	private String intimationNo;

	private String claimNo;

	private String insuredPatientName;

	private String lob;

	private String referredBy;
	
	private String doctorName;
	
	private String doctorComments;

	private String productName;
	
	
	private RRCDTO rrcDTO;
	
	private Long stageId;
	
	private Preauth preauth;
	
    private Long cpuId;
    
    private String cpuCode;
    
    private String hospitalName;
    
    private Long hospitalkey;
    
    private String reasonForAdmission;
    
    private String specialityType;
    
    private String claimStatus;
    
    private String hospitalAddress;
    
    private String hospitalCity;
    
    private String diagnosis;
    
    private Date createDate;
    
    private String dateOfRefer;
    
	private String crmFlagged;
	
	private NewIntimationDto newIntimationDto;
	
	private PreauthDTO preauthDTO;
	
	private String crcFlaggedReason;
	
	private String crcFlaggedRemark;
	
	private SubmitSpecialistFormDTO searchFormDTO;

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}



	public String getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(String referredBy) {
		this.referredBy = referredBy;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	
	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public Long getIntimationkey() {
		return intimationkey;
	}

	public void setIntimationkey(Long intimationkey) {
		this.intimationkey = intimationkey;
	}
	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getlOBId() {
		return lOBId;
	}

	public void setlOBId(Long lOBId) {
		this.lOBId = lOBId;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}

	public Long getStageId() {
		return stageId;
	}

	public void setStageId(Long stageId) {
		this.stageId = stageId;
	}
	public Boolean getIsReimburementFlag() {
		return isReimburementFlag;
	}

	public void setIsReimburementFlag(Boolean isReimburementFlag) {
		this.isReimburementFlag = isReimburementFlag;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getReasonForAdmission() {
		return reasonForAdmission;
	}

	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}

	public String getSpecialityType() {
		return specialityType;
	}

	public void setSpecialityType(String specialityType) {
		this.specialityType = specialityType;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public Long getHospitalkey() {
		return hospitalkey;
	}

	public void setHospitalkey(Long hospitalkey) {
		this.hospitalkey = hospitalkey;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorComments() {
		return doctorComments;
	}

	public void setDoctorComments(String doctorComments) {
		this.doctorComments = doctorComments;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDateOfRefer() {
		return dateOfRefer;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public void setDateOfRefer(String dateOfRefer) {
		this.dateOfRefer = dateOfRefer;
	}

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}

	public String getCrcFlaggedReason() {
		return crcFlaggedReason;
	}

	public void setCrcFlaggedReason(String crcFlaggedReason) {
		this.crcFlaggedReason = crcFlaggedReason;
	}

	public String getCrcFlaggedRemark() {
		return crcFlaggedRemark;
	}

	public void setCrcFlaggedRemark(String crcFlaggedRemark) {
		this.crcFlaggedRemark = crcFlaggedRemark;
	}

	public SubmitSpecialistFormDTO getSearchFormDTO() {
		return searchFormDTO;
	}

	public void setSearchFormDTO(SubmitSpecialistFormDTO searchFormDTO) {
		this.searchFormDTO = searchFormDTO;
	}
	
/*	public String getLoB() {
		return loB;
	}

	public void setLoB(String loB) {
		this.loB = loB;
	}
*/
	

}