package com.shaic.claim.pcc.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.TmpCPUCode;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

public class SearchProcessPCCRequestTableDTO extends AbstractTableDTO implements Serializable {

	private Long key;
	
	private int serialNumber;
	
	private String intimationNo;
	
	private String insuredPatientName;
	
	private Long hospitalId;
	
	private String hospitalName;
	
	private String cpuName;
	
	private String productName;
	
	private String pccSource;
	
	private String pccCatagory;
	
	private String hospitalCode;
	
	private Object workFlowObject;
	
	private String claimType;
	
	private Long intimationKey;
	
	private NewIntimationDto intimationDto;
	
	private ClaimDto claimDto;
	
	private String userName;
	
	private String dateAndTime;
	
	private Date dateAndTime1;
	
	private TmpCPUCode cpu;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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

	public String getCpuName() {
		return cpuName;
	}

	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPccCatagory() {
		return pccCatagory;
	}

	public void setPccCatagory(String pccCatagory) {
		this.pccCatagory = pccCatagory;
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

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public String getPccSource() {
		return pccSource;
	}

	public void setPccSource(String pccSource) {
		this.pccSource = pccSource;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public Date getDateAndTime1() {
		return dateAndTime1;
	}

	public void setDateAndTime1(Date dateAndTime1) {
		String dateformate =new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dateAndTime1);
		setDateAndTime(dateformate);
		this.dateAndTime1 = dateAndTime1;
	}

	public TmpCPUCode getCpu() {
		return cpu;
	}

	public void setCpu(TmpCPUCode cpu) {
		this.cpu = cpu;
		if(this.cpu != null){
			this.cpuName = this.cpu.getCpuCode().toString() + " - " + this.cpu.getDescription();
		}
	}
	
	
	
	
}
