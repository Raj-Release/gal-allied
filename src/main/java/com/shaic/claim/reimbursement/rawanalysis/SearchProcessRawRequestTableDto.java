package com.shaic.claim.reimbursement.rawanalysis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SearchProcessRawRequestTableDto extends AbstractTableDTO implements Serializable{

	private String intimationNo;
	private String policyNo;
	private String insuredPatientName;
	private Long hospitalId;
	private String hospitalName;
	private String hospitalCity;
	private String classOfBusiness;
	private Long cpuId;
	private String cpuCode;
	private String productCode;
	private String claimType;
	private Date requestDate;
	private String category;
	private Long intimationKey;
	private Long claimKey;
	private String productName;
	private Long requestStageId;
	private Long repliedStatusId;
	private NewIntimationDto intimationDto;
	private ClaimDto claimDto;
	private PreauthDTO preauthDTO;
	private List<RawInitiatedRequestDTO> raqInitiatedDto;
	private String hospitalCode;
	private Object workFlowObject;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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
	public Long getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalCity() {
		return hospitalCity;
	}
	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}
	public String getClassOfBusiness() {
		return classOfBusiness;
	}
	public void setClassOfBusiness(String classOfBusiness) {
		this.classOfBusiness = classOfBusiness;
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
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getRequestStageId() {
		return requestStageId;
	}
	public void setRequestStageId(Long requestStageId) {
		this.requestStageId = requestStageId;
	}
	public Long getRepliedStatusId() {
		return repliedStatusId;
	}
	public void setRepliedStatusId(Long repliedStatusId) {
		this.repliedStatusId = repliedStatusId;
	}
	public NewIntimationDto getIntimationDto() {
		return intimationDto;
	}
	public void setIntimationDto(NewIntimationDto intimationDto) {
		this.intimationDto = intimationDto;
	}
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}
	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}
	public List<RawInitiatedRequestDTO> getRaqInitiatedDto() {
		return raqInitiatedDto;
	}
	public void setRaqInitiatedDto(List<RawInitiatedRequestDTO> raqInitiatedDto) {
		this.raqInitiatedDto = raqInitiatedDto;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public Object getWorkFlowObject() {
		return workFlowObject;
	}
	public void setWorkFlowObject(Object workFlowObject) {
		this.workFlowObject = workFlowObject;
	}
}
