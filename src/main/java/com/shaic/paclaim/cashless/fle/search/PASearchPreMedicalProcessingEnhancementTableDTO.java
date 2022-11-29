package com.shaic.paclaim.cashless.fle.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class PASearchPreMedicalProcessingEnhancementTableDTO extends AbstractTableDTO implements Serializable {


	private static final long serialVersionUID = 4016831167620717224L;
	
	private Integer sno;

	private String intimationNo;

	private String intimationSource;

	private String cpuNAME;

	private String productName;
	
	private Long productKey;
	
	private String insuredPatientName;
	
	private String paPatientName;
	
	private String hospitalName;
	
	private String hospitalTypeName;
	
	private String networkHospitalType;
	
	private Double enhancementReqAmt;
	
	private Double balanceSI;
	
	private String docsRecievedTime;
	
	private String type;
	
	private String transactionType;

	
	private Long hospitalTypeId;

	private String policyNo;

	//private Double sumInsured;
	private Integer sumInsured;
	
	private Long policyKey;
	
	private Long insuredId;
	
	private Long insuredKey;
	
	private Long intimationKey;
	
	
	private Date docsReceivedDate;
	//private HumanTask humanTask;
	
	private Long nhpUpdDocumentKey;

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(String intimationSource) {
		this.intimationSource = intimationSource;
	}

	public String getCpuNAME() {
		return cpuNAME;
	}

	public void setCpuNAME(String cpuNAME) {
		this.cpuNAME = cpuNAME;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	
	public String getHospitalTypeName() {
		return hospitalTypeName;
	}

	public void setHospitalTypeName(String hospitalTypeName) {
		this.hospitalTypeName = hospitalTypeName;
	}

	public String getNetworkHospitalType() {
		return networkHospitalType;
	}

	public void setNetworkHospitalType(String networkHospitalType) {
		this.networkHospitalType = networkHospitalType;
	}

	public Double getEnhancementReqAmt() {
		return enhancementReqAmt;
	}

	public void setEnhancementReqAmt(Double enhancementReqAmt) {
		this.enhancementReqAmt = enhancementReqAmt;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}

	public String getDocsRecievedTime() {
		return docsRecievedTime;
	}

	public void setDocsRecievedTime(String docsRecievedTime) {
		this.docsRecievedTime = docsRecievedTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Integer getSumInsured() {
		return sumInsured;
	}

	public void setSumInsured(Integer sumInsured) {
		this.sumInsured = sumInsured;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getInsuredId() {
		return insuredId;
	}

	public void setInsuredId(Long insuredId) {
		this.insuredId = insuredId;
	}

	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public Date getDocsReceivedDate() {
		return docsReceivedDate;
	}

	public void setDocsReceivedDate(Date docsReceivedDate) {
		
		if(docsReceivedDate !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy HH:MM:SS").format(docsReceivedDate);
			//setDateOfAdmission(dateformate);
			setDocsRecievedTime(dateformate);
			this.docsReceivedDate = docsReceivedDate;
			}
		
		
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public String getPaPatientName() {
		return paPatientName;
	}

	public void setPaPatientName(String paPatientName) {
		this.paPatientName = paPatientName;
	}

	public Long getNhpUpdDocumentKey() {
		return nhpUpdDocumentKey;
	}

	public void setNhpUpdDocumentKey(Long nhpUpdDocumentKey) {
		this.nhpUpdDocumentKey = nhpUpdDocumentKey;
	}

	

	/*public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}
*/
	
}
