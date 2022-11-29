package com.shaic.paclaim.cashless.preauth.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.cmn.login.ImsUser;

public class PASearchPreauthTableDTO  extends AbstractTableDTO  implements Serializable {
	
	private String intimationNo;
	
	private String intimationSource;
	
	private String cpuName;
	
	private String productName;
	
	private Long productKey;
	
	private String insuredPatientName;
	
	private String paPatientName;
	
	private String hospitalName;
	
	private String networkHospType;
	
	private String hospitalTypeName;
	
	private String preAuthReqAmt;
	
	private String treatmentType;
	
	private String speciality;
	private String enhancementReqAmt;
	
	private Double balanceSI;
	
	private Date docReceivedTimeForReg;
	
	private Date docReceivedTimeForMatch;
	
	private String strDocReceivedTimeForReg;
	
	private Date docReceivedTimeForRegDate;
	
	private String strDocReceivedTimeForMatch;
	
	private Date docReceivedTimeForMatchDate;
	
	private String type;
	
	private String transactionType;
	
	private Long hospitalTypeId;
	
	private String policyNo;
	
	//private Double sumInsured;
	private Integer sumInsured;
	
	private Long policyKey;
	
	private Long insuredId;
	
	private Long insuredKey;
	
	private Long claimKey;
	
	private Long intimationKey;
	
	private Double claimedAmountAsPerBill;
	
	private Boolean CMA6 = false;
	private Boolean CMA5 = false;
	private Boolean CMA4 = false;
	private Boolean CMA3 = false;
	private Boolean CMA2 = false;
	private Boolean CMA1 = false; 
	
	private ImsUser imsUser;
	
	private String crmFlagged;
	
	private String aboveCPULimitCorp;
	
	private Long nhpUpdDocumentKey;
	
	
//	private HumanTask humanTask;
	
	//Added for treatment  type and speciality.
	
	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getEnhancementReqAmt() {
		return enhancementReqAmt;
	}

	public void setEnhancementReqAmt(String enhancementReqAmt) {
		this.enhancementReqAmt = enhancementReqAmt;
	}

	
	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
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

	public String getPreAuthReqAmt() {
		return preAuthReqAmt;
	}

	public void setPreAuthReqAmt(String preAuthReqAmt) {
		this.preAuthReqAmt = preAuthReqAmt;
	}

	public Double getBalanceSI() {
		return balanceSI;
	}

	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIntimationSource() {
		return intimationSource;
	}

	public void setIntimationSource(String intimationSource) {
		this.intimationSource = intimationSource;
	}

	public String getNetworkHospType() {
		return networkHospType;
	}

	public void setNetworkHospType(String networkHospType) {
		this.networkHospType = networkHospType;
	}
	
	public String getHospitalTypeName() {
		return hospitalTypeName;
	}

	public void setHospitalTypeName(String hospitalTypeName) {
		this.hospitalTypeName = hospitalTypeName;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public Date getDocReceivedTimeForReg() {
		return docReceivedTimeForReg;
	}

	public void setDocReceivedTimeForReg(Date docReceivedTimeForReg) {
		this.docReceivedTimeForReg = docReceivedTimeForReg;
	}

	public Date getDocReceivedTimeForMatch() {
		return docReceivedTimeForMatch;
	}

	public void setDocReceivedTimeForMatch(Date docReceivedTimeForMatch) {
		this.docReceivedTimeForMatch = docReceivedTimeForMatch;
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

	public String getStrDocReceivedTimeForReg() {
		return strDocReceivedTimeForReg;
	}

	public void setStrDocReceivedTimeForReg(String strDocReceivedTimeForReg) {
		this.strDocReceivedTimeForReg = strDocReceivedTimeForReg;
	}

	public String getStrDocReceivedTimeForMatch() {
		return strDocReceivedTimeForMatch;
	}

	public void setStrDocReceivedTimeForMatch(String strDocReceivedTimeForMatch) {
		this.strDocReceivedTimeForMatch = strDocReceivedTimeForMatch;
	}

	public Date getDocReceivedTimeForRegDate() {
		return docReceivedTimeForRegDate;
	}

	public void setDocReceivedTimeForRegDate(Date docReceivedTimeForRegDate) {
		if(docReceivedTimeForRegDate !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForRegDate);
			//setDateOfAdmission(dateformate);
			setStrDocReceivedTimeForReg(dateformate);
			this.docReceivedTimeForRegDate = docReceivedTimeForRegDate;
			}
		
		
		
	}

	public Date getDocReceivedTimeForMatchDate() {
		return docReceivedTimeForMatchDate;
	}

	public void setDocReceivedTimeForMatchDate(Date docReceivedTimeForMatchDate) {
		
		if(docReceivedTimeForMatchDate !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.ss").format(docReceivedTimeForMatchDate);
			//setDateOfAdmission(dateformate);
			setStrDocReceivedTimeForMatch(dateformate);
			this.docReceivedTimeForMatchDate = docReceivedTimeForMatchDate;
			}
		
	}

	
	public Boolean getCMA4() {
		return CMA4;
	}

	public void setCMA4(Boolean cMA4) {
		CMA4 = cMA4;
	}

	public ImsUser getImsUser() {
		return imsUser;
	}

	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}

	public Boolean getCMA1() {
		return CMA1;
	}

	public void setCMA1(Boolean cMA1) {
		CMA1 = cMA1;
	}

	public Boolean getCMA2() {
		return CMA2;
	}

	public void setCMA2(Boolean cMA2) {
		CMA2 = cMA2;
	}

	public Boolean getCMA3() {
		return CMA3;
	}

	public void setCMA3(Boolean cMA3) {
		CMA3 = cMA3;
	}

	public Boolean getCMA6() {
		return CMA6;
	}

	public void setCMA6(Boolean cMA6) {
		CMA6 = cMA6;
	}

	public Boolean getCMA5() {
		return CMA5;
	}

	public void setCMA5(Boolean cMA5) {
		CMA5 = cMA5;
	}

	public Double getClaimedAmountAsPerBill() {
		return claimedAmountAsPerBill;
	}

	public void setClaimedAmountAsPerBill(Double claimedAmountAsPerBill) {
		this.claimedAmountAsPerBill = claimedAmountAsPerBill;
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

	public String getCrmFlagged() {
		return crmFlagged;
	}

	public void setCrmFlagged(String crmFlagged) {
		this.crmFlagged = crmFlagged;
	}

	public String getAboveCPULimitCorp() {
		return aboveCPULimitCorp;
	}

	public void setAboveCPULimitCorp(String aboveCPULimitCorp) {
		this.aboveCPULimitCorp = aboveCPULimitCorp;
	}

	public Long getNhpUpdDocumentKey() {
		return nhpUpdDocumentKey;
	}

	public void setNhpUpdDocumentKey(Long nhpUpdDocumentKey) {
		this.nhpUpdDocumentKey = nhpUpdDocumentKey;
	}



	/*public Date getStrDocReceivedTimeForMatch() {
		return strDocReceivedTimeForMatch;
	}

	public void setStrDocReceivedTimeForMatch(Date strDocReceivedTimeForMatch) {
		this.strDocReceivedTimeForMatch = strDocReceivedTimeForMatch;
	}
*/
/*	public HumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/



		
}
