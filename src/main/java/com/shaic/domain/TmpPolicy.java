//package com.shaic.domain;
//
//import java.io.Serializable;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Transient;
//
//import com.shaic.arch.fields.dto.AbstractEntity;
//
///**
// * The persistent class for the IMS_CLS_POLICYY database table.
// * 
// */
//@Entity
//@Table(name = "IMS_TMP_POLICY")
//@NamedQueries({
//		@NamedQuery(name = "TmpPolicy.findAll", query = "SELECT o FROM TmpPolicy o"),
//		@NamedQuery(name = "TmpPolicy.findByPolicy", query = "select o from TmpPolicy o where o.polNo = :parentKey"),
//		@NamedQuery(name = "TmpPolicy.findByPolicyByPolType", query = "select o from TmpPolicy o where o.polNo = :parentKey and Lower(o.polType) like :poltype"),		
//		@NamedQuery(name = "TmpPolicy.findById", query = "select o from TmpPolicy o where o.polNo = :primaryKey"),
//		@NamedQuery(name = "TmpPolicy.findByProposerName", query = "SELECT p from TmpPolicy p where p.polAssrName Like  :proposerName "),
//		@NamedQuery(name = "TmpPolicy.findByProposerNameAndDob", query = "SELECT p from TmpPolicy p where Lower(p.polAssrName) Like  :proposerName and p.polAssrDOB = :dob ") })
//public class TmpPolicy extends AbstractEntity implements Serializable {
//
//	public TmpPolicy() {
//		super();
//	}
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -232882945631631662L;
//
//	
///*	@Column(name = "POL_SYS_ID")
//	private Long polSysId;
//*/
//	@Column(name = "PROPOSER_CODE")
//	private String polAssrCode;
//
//	@Column(name = "RECEIPT_NUMBER")
//	private String polReceiptNo;
//
//	/*@Column(name = "POL_SENIOR_CODE")
//	private String polSeniorCode;
//*/
///*	@Column(name = "POL_NO")
//	private String polNo;*/
//	@Id
//	@Column(name = "POLICY_NUMBER")
//	private String polNo;
//	
//	@Column(name="PRODUCT_TYPE")
//	private String productType;
//	
//	@Column(name = "PRODUCT_NAME")
//	private String productName;
//	
//	@Column(name = "LOB")
//	private String lineofBusiness;
//	
//	/*@Column(name = "POL_END_NO")
//	private String polEndNo;*/
//	
//	@Column(name = "POLICY_ENDORSEMENT_NUMBER")
//	private String polEndNo;
//	
//	@Column(name = "PROPOSER_NAME")
//	private String polAssrName;
//
//	@Column(name = "PROPOSER_DOB")
//	private Date polAssrDOB;
//
///*	@Column(name = "POL_ADDR_01")
//	private String polAddr01;
//
//	@Column(name = "POL_ADDR_02")
//	private String polAddr02;*/
//
//	@Column(name = "REGISTERED_MOBILE_NUMBER")
//	private Long registerdMobileNumber;
//
//	@Column(name = "PROPOSER_ADDRESS")
//	private String polAddr01;
//	
///*	@Column(name = "POL_ADDR_03")
//	private String polAddr03;
//*/
//	@Temporal(TemporalType.DATE)
//	@Column(name = "POLICY_START_DATE")
//	private Date polFmDt;
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "POLICY_END_DATE")
//	private Date polToDt;
//
//	@Column(name = "PROPOSER_TELEPHONE_NUMBER")
//	private String polTelNo;
//
//	@Column(name = "PROPOSER_FAX_NUMBER")
//	private String polFaxNo;
//
//	@Column(name = "PROPOSER_EMAIL_ID")
//	private String polEmailId;
//
//	@Column(name = "GROSS_PREMIUM")
//	private Double polGrossPrem;
//
//	@Column(name = "PREMIUM_TAX")
//	private Double polPremTax;
//
//	@Column(name = "RENEWAL_POLICY_NUMBER")
//	private String polhRenPolNo;
//
//	@Column(name = "ISSUING_OFFICE_CODE")
//	private String polhDivnCode;
//
//	@Column(name = "PROPOSER_OFFICE_ADDRESS")
//	private String polOffAddress1;
//
//	/*@Column(name = "POL_OFF_ADDRESS_2")
//	private String polOffAddress2;
//
//	@Column(name = "POL_OFF_ADDRESS_3")
//	private String polOffAddress3;
//*/
//	@Column(name = "PROPOSER_OFFICE_TELEPHONE_NO")
//	private String polOffPhone;
//
//	@Column(name = "PROPOSER_OFFICE_EMAIL_ID")
//	private String polOffEmail;
//
//	@Column(name = "PROPOSER_OFFICE_FAX_NUMBER")
//	private String polOffFax;
//
//	
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "RECEIPT_DATE")
//	private Date polReceiptDate;
//
//	@Column(name = "AGENT_CODE")
//	private String polAgent;
//	
//	@Column(name = "AGENT_NAME")
//	private String agentName;
//
//	@Column(name = "PRODUCT_CODE")
//	private String polProductCode;
//
//	@Column(name = "STAMP_DUTY")
//	private Double polStampDuty;
//
//	@Column(name = "TOTAL_AMOUNT")
//	private Double polTotalAmt;
//
//	@Column(name = "SM_CODE")
//	private String polSmCode;
//
//	@Column(name = "SM_NAME")
//	private String polSmName;
//
//	@Column(name = "POLICY_TYPE")
//	private String polType;
//
//	@Column(name = "SUM_INSURED")
//	private Double polSumInsured;
//
//	@Column(name = "POLICY_STATUS")
//	private String polStatus;
//
//	@Column(name = "CUMULATIVE_BONUS")
//	private Double polCumulativeBonus;
//
//
//	
//	/*@Column(columnDefinition = "VARCHAR(1)", name = "CORPORATE_BUFFER_FLAG", length = 1)
//	String corporateBufferFlag;*/
//	 
//	/*@Column(name = "TOT_BUFFER_AMOUNT")
//	private Double totaBufferAmount; */
//	
//	@Column(name = "CO_PAY")
//	private Long copay;
//	
//	
//
//	
//	
//	
//	@Column(name = "SUM_INSURED_II")
//	private Double sumInsuredII;
//	
//	/*@Column(name="POL_HEALTH_CARD_NUMBER")
//	private String healthCardNumber;*/	
//	
//	@Column(name = "RESTORED_SI")
//	private Double restoredSI;
//	
//	@Column(name = "RECHARGED_SI")
//	private Double rechargedSI;
//	
//	@Column(name = "JUNIOR_MARKETING_CODE")
//	private String juniorMarketingCode;
//	
//	@Column(name = "JUNIOR_MARKETING_NAME")
//	private String juniorMarketingName;
//
//
//	@Transient
//	private List<TmpInsured> tmpInsured;
//
//	
//
//	public List<TmpInsured> getTmpInsured() {
//		return tmpInsured;
//	}
//
//	public void setTmpInsured(List<TmpInsured> tmpInsured) {
//		this.tmpInsured = tmpInsured;
//	}
//
//	
//
//	public String getPolAssrCode() {
//		// return polAssrCode!= null ?polAssrCode : "";
//		return polAssrCode;
//	}
//
//	public void setPolAssrCode(String polAssrCode) {
//		this.polAssrCode = polAssrCode;
//	}
//
//	public String getPolReceiptNo() {
//		// return PolReceiptNo!= null ?PolReceiptNo : "";
//		return polReceiptNo;
//	}
//
//	public void setPolReceiptNo(String polReceiptNo) {
//		this.polReceiptNo = polReceiptNo;
//	}
//
//	
//
//	public String getPolNo() {
//		return polNo;
//	}
//
//	public void setPolNo(String polNo) {
//		this.polNo = polNo;
//	}
//
//	public String getPolEndNo() {
//		return polEndNo;
//		// return polEndNo!= null ?polEndNo : "";
//
//	}
//
//	public void setPolEndNo(String polEndNo) {
//		this.polEndNo = polEndNo;
//	}
//
//	public String getPolAssrName() {
//		// return polAssrName!= null ?polAssrName : "";
//		return polAssrName;
//	}
//
//	public void setPolAssrName(String polAssrName) {
//		this.polAssrName = polAssrName;
//	}
//	
//	
//
//	/*public String getPolAddr01() {
//		return polAddr01;
//		// return polAddr01!= null ?polAddr01 : "";
//	}
//
//	public void setPolAddr01(String polAddr01) {
//		this.polAddr01 = polAddr01;
//	}
//
//	public String getPolAddr02() {
//		return polAddr02;
//		// return polAddr02!= null ?polAddr02 : "";
//	}
//
//	public void setPolAddr02(String polAddr02) {
//		this.polAddr02 = polAddr02;
//	}
//
//	public String getPolAddr03() {
//		return polAddr03;
//		// return polAddr03!= null ?polAddr03 : "";
//	}
//
//	public void setPolAddr03(String polAddr03) {
//		this.polAddr03 = polAddr03;
//	}*/
//
//	public Date getPolFmDt() {
//		return polFmDt;
//
//	}
//
//	public void setPolFmDt(Date polFmDt) {
//		this.polFmDt = polFmDt;
//
//	}
//
//	public Date getPolToDt() {
//		return polToDt;
//	}
//
//	public void setPolToDt(Date polToDt) {
//		this.polToDt = polToDt;
//	}
//
//	public String getPolTelNo() {
//		return polTelNo;
//		// return polTelNo!= null ?polTelNo : "";
//	}
//
//	public void setPolTelNo(String polTelNo) {
//		this.polTelNo = polTelNo;
//	}
//
//	public String getPolFaxNo() {
//		return polFaxNo;
//		// return polFaxNo!= null ?polFaxNo : "";
//	}
//
//	public void setPolFaxNo(String polFaxNo) {
//		this.polFaxNo = polFaxNo;
//	}
//
//	public String getPolEmailId() {
//		// return polEmailId!= null ?polEmailId : "";
//		return polEmailId;
//	}
//
//	public void setPolEmailId(String polEmailId) {
//		this.polEmailId = polEmailId;
//	}
//
//	public Double getPolGrossPrem() {
//		// return polGrossPrem!= null ?polGrossPrem : "";
//		return polGrossPrem;
//	}
//
//	public void setPolGrossPrem(Double polGrossPrem) {
//		this.polGrossPrem = polGrossPrem;
//	}
//
//	public Double getPolPremTax() {
//
//		return polPremTax;
//	}
//
//	public void setPolPremTax(Double polPremTax) {
//		this.polPremTax = polPremTax;
//	}
//
//	public String getPolhRenPolNo() {
//		// return polhRenPolNo!= null ?polhRenPolNo : "";
//		return polhRenPolNo;
//	}
//
//	public void setPolhRenPolNo(String polhRenPolNo) {
//		this.polhRenPolNo = polhRenPolNo;
//	}
//
//	public String getPolhDivnCode() {
//		return polhDivnCode;
//		// return polhDivnCode!= null ?polhDivnCode : "";
//	}
//
//	public void setPolhDivnCode(String polhDivnCode) {
//		this.polhDivnCode = polhDivnCode;
//	}
//
//	public String getPolOffAddress1() {
//		return polOffAddress1;
//		// return polOffAddress1!= null ?polOffAddress1 : "";
//	}
//
//	public void setPolOffAddress1(String polOffAddress1) {
//		this.polOffAddress1 = polOffAddress1;
//	}
//
//	/*public String getPolOffAddress2() {
//		// return polOffAddress2!= null ?polOffAddress2 : "";
//		return polOffAddress2;
//	}
//
//	public void setPolOffAddress2(String polOffAddress2) {
//		this.polOffAddress2 = polOffAddress2;
//	}
//
//	public String getPolOffAddress3() {
//		// return polOffAddress3!= null ?polOffAddress3 : "";
//		return polOffAddress3;
//	}
//
//	public void setPolOffAddress3(String polOffAddress3) {
//		this.polOffAddress3 = polOffAddress3;
//	}*/
//
//	public String getPolOffPhone() {
//		// return polOffPhone!= null ?polOffPhone : "";
//		return polOffPhone;
//	}
//
//	public void setPolOffPhone(String polOffPhone) {
//		this.polOffPhone = polOffPhone;
//	}
//
//	public String getPolOffEmail() {
//		// return polOffEmail!= null ?polOffEmail : "";
//		return polOffEmail;
//	}
//
//	public void setPolOffEmail(String polOffEmail) {
//		this.polOffEmail = polOffEmail;
//	}
//
//	
//	public Date getPolReceiptDate() {
//		return polReceiptDate;
//	}
//
//	public void setPolReceiptDate(Date polReceiptDate) {
//		this.polReceiptDate = polReceiptDate;
//	}
//
//	public String getPolAgent() {
//		// return polAgent!= null ?polAgent : "";
//		return polAgent;
//	}
//
//	public void setPolAgent(String polAgent) {
//		this.polAgent = polAgent;
//	}
//
//	public String getPolProductCode() {
//		// return polProduct!= null ?polProduct : "";
//		return polProductCode;
//	}
//
//	public void setPolProductCode(String polProductCode) {
//		this.polProductCode = polProductCode;
//	}
//
//	public Double getPolStampDuty() {
//		// return polStampDuty!= null ?polStampDuty : "";
//		return polStampDuty;
//	}
//
//	public void setPolStampDuty(Double polStampDuty) {
//		this.polStampDuty = polStampDuty;
//	}
//
//	public Double getPolTotalAmt() {
//		// return polTotalAmt!= null ?polTotalAmt : "";
//		return polTotalAmt;
//	}
//
//	public void setPolTotalAmt(Double polTotalAmt) {
//		this.polTotalAmt = polTotalAmt;
//	}
//
//	public String getPolSmCode() {
//		// return polSmCode!= null ?polSmCode : "";
//		return polSmCode;
//	}
//
//	public void setPolSmCode(String polSmCode) {
//		this.polSmCode = polSmCode;
//	}
//
//	public String getPolSmName() {
//		return polSmName;
//		// return polSmName!= null ?polSmName : "";
//	}
//
//	public void setPolSmName(String polSmName) {
//		this.polSmName = polSmName;
//	}
//
//	public String getPolType() {
//		// return polType!= null ?polType : "";
//		return polType;
//	}
//
//	public void setPolType(String polType) {
//		this.polType = polType;
//	}
//
//
//	public Double getPolSumInsured() {
//		// return polSumInsured!= null ?polSumInsured : "";
//		return polSumInsured;
//	}
//
//	public void setPolSumInsured(Double polSumInsured) {
//		this.polSumInsured = polSumInsured;
//	}
//
//	public String getPolStatus() {
//		return polStatus;
//		// return polStatus!= null ?polStatus : "";
//	}
//
//	public void setPolStatus(String polStatus) {
//		this.polStatus = polStatus;
//
//	}
//
//	public Date getPolAssrDOB() {
//		return polAssrDOB;
//	}
//
//	public void setPolAssrDOB(Date polAssrDOB) {
//		this.polAssrDOB = polAssrDOB;
//	}
//
//	public Double getPolCumulativeBonus() {
//		return polCumulativeBonus;
//	}
//
//	public void setPolCumulativeBonus(Double polCumulativeBonus) {
//		this.polCumulativeBonus = polCumulativeBonus;
//	}
//
//	
//	
//
//	public String getRegisterdMobileNumber() {
//		return registerdMobileNumber != null ? registerdMobileNumber.toString() : "";
//	}
//
//	public void setRegisterdMobileNumber(Long registerdMobileNumber) {
//		this.registerdMobileNumber = registerdMobileNumber;
//	}
//
//	public String getLineofBusiness() {
//		return lineofBusiness;
//	}
//
//	public void setLineofBusiness(String lineofBusiness) {
//		this.lineofBusiness = lineofBusiness;
//	}
//
//	/*@Override
//	public Long getKey() {
//		return getPolNo();
//	}*/
//
//	@Override
//	public void setKey(Long key) {
//		
//	}
//
//	/*public String getCorporateBufferFlag() {
//		return corporateBufferFlag;
//	}
//
//	public void setCorporateBufferFlag(String corporateBufferFlag) {
//		this.corporateBufferFlag = corporateBufferFlag;
//	}*/
//
//
//	public Double getSumInsuredII() {
//		return sumInsuredII;
//	}
//
//	public void setSumInsuredII(Double sumInsuredII) {
//		this.sumInsuredII = sumInsuredII;
//	}
//
//	public Long getCopay() {
//		return copay;
//	}
//
//	public void setCopay(Long copay) {
//		this.copay = copay;
//	}
//
//
//	public String getProductType() {
//		return productType;
//	}
//
//	public void setProductType(String productType) {
//		this.productType = productType;
//	}
//
//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
//
//	public String getPolAddr01() {
//		return polAddr01;
//	}
//
//	public void setPolAddr01(String polAddr01) {
//		this.polAddr01 = polAddr01;
//	}
//
//	public String getAgentName() {
//		return agentName;
//	}
//
//	public void setAgentName(String agentName) {
//		this.agentName = agentName;
//	}
//
//	public Double getRestoredSI() {
//		return restoredSI;
//	}
//
//	public void setRestoredSI(Double restoredSI) {
//		this.restoredSI = restoredSI;
//	}
//
//	public Double getRechargedSI() {
//		return rechargedSI;
//	}
//
//	public void setRechargedSI(Double rechargedSI) {
//		this.rechargedSI = rechargedSI;
//	}
//
//	public String getJuniorMarketingCode() {
//		return juniorMarketingCode;
//	}
//
//	public void setJuniorMarketingCode(String juniorMarketingCode) {
//		this.juniorMarketingCode = juniorMarketingCode;
//	}
//
//	public String getJuniorMarketingName() {
//		return juniorMarketingName;
//	}
//
//	public void setJuniorMarketingName(String juniorMarketingName) {
//		this.juniorMarketingName = juniorMarketingName;
//	}
//
//	public String getPolOffFax() {
//		return polOffFax;
//	}
//
//	public void setPolOffFax(String polOffFax) {
//		this.polOffFax = polOffFax;
//	}
//	
//	@Override
//	public Long getKey() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	
//
//}