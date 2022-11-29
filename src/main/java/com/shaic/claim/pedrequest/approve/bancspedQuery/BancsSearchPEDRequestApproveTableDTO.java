package com.shaic.claim.pedrequest.approve.bancspedQuery;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class BancsSearchPEDRequestApproveTableDTO extends AbstractTableDTO implements Serializable {

	
	private static final long serialVersionUID = -7529836927598921914L;

	private Integer sno;

	private String intimationNo;

	private String claimNo;

	private String policyNo;

	private String insuredPatientName;

	private String cpuCode;

	private String hospitalName;

	private String hospitalAddress;

	private String hospitalCity;
	
	private Date pedInitiated;
	
	private String strPedInitiated;

	private String pedSuggestion;

	private String pedStatus;
	
	private Long renewalDue;
	
/*	private String doorApartmentNumber;
	private String plotGateNumber;
	private String buildingName;
	private String streetName;
	private String locality;
	private String district;
	private String state;
	private String country;
	private String pinCode;*/
	private Long hospitalTypeId;
	//private String address;
	
	private RRCDTO rrcDTO;
	
	private Boolean isPolicyValidate;
	
	private String crmFlagged;
	
	private String crcFlaggedReason;
    
    private String crcFlaggedRemark;
    
    public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
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

	public NewIntimationDto getNewIntimationDTO() {
		return newIntimationDTO;
	}

	public void setNewIntimationDTO(NewIntimationDto newIntimationDTO) {
		this.newIntimationDTO = newIntimationDTO;
	}

	public PreauthDTO getPreAuthDto() {
		return preAuthDto;
	}

	public void setPreAuthDto(PreauthDTO preAuthDto) {
		this.preAuthDto = preAuthDto;
	}

	private NewIntimationDto newIntimationDTO;
    
    private PreauthDTO preAuthDto = new PreauthDTO();
	
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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
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

	public String getPedSuggestion() {
		return pedSuggestion;
	}

	public void setPedSuggestion(String pedSuggestion) {
		this.pedSuggestion = pedSuggestion;
	}

	public String getPedStatus() {
		return pedStatus;
	}

	public void setPedStatus(String pedStatus) {
		this.pedStatus = pedStatus;
	}

	/*public String getDoorApartmentNumber() {
		return doorApartmentNumber;
	}

	public void setDoorApartmentNumber(String doorApartmentNumber) {
		this.doorApartmentNumber = doorApartmentNumber;
	}

	public String getPlotGateNumber() {
		return plotGateNumber;
	}

	public void setPlotGateNumber(String plotGateNumber) {
		this.plotGateNumber = plotGateNumber;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}*/

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public RRCDTO getRrcDTO() {
		return rrcDTO;
	}

	public void setRrcDTO(RRCDTO rrcDTO) {
		this.rrcDTO = rrcDTO;
	}

	public Date getPedInitiated() {
		return pedInitiated;
	}

	public void setPedInitiated(Date pedInitiated) {
		this.pedInitiated = pedInitiated;
	}

	public String getStrPedInitiated() {
		return strPedInitiated;
	}

	public void setStrPedInitiated(String strPedInitiated) {
		this.strPedInitiated = strPedInitiated;
	}

	public Long getRenewalDue() {
		return renewalDue;
	}

	public void setRenewalDue(Long renewalDue) {
		this.renewalDue = renewalDue;
	}

	public Boolean getIsPolicyValidate() {
		return isPolicyValidate;
	}

	public void setIsPolicyValidate(Boolean isPolicyValidate) {
		this.isPolicyValidate = isPolicyValidate;
	}



/*	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
*/
	
	
}
