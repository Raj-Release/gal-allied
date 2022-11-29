package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import oracle.jdbc.OracleTypes;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Page;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.BancsPaymentCancel;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.InsuredService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.paymentprocess.paymentreprocess.PaymentReprocessSearchFormDTO;
import com.shaic.reimbursement.paymentprocess.paymentreprocess.PaymentReprocessSearchResultDTO;

@Stateless
@SuppressWarnings("deprecation")
public class PaymentRecoveryService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private InsuredService insuredService;
	
	
	public Page<PaymentInitiateRecoveryTableDTO> performSearch(PaymentInitiateRecoverySearchFormDTO argFormDTO,String userName, String argScreenName){
		Page<PaymentInitiateRecoveryTableDTO> page = new Page<PaymentInitiateRecoveryTableDTO>();
		List<PaymentInitiateRecoveryTableDTO> initiateRecoveryList = new ArrayList<PaymentInitiateRecoveryTableDTO>();
		final String PAYMENT_REPROCESS_PROC = "{call PRC_GB_GET_PAYMENT_CANCEL(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try{
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PAYMENT_REPROCESS_PROC);
			cs.setString(1, null);
			cs.setString(2, argFormDTO.getIntimationNo());
			cs.setString(3, null);
			cs.setString(4, null);
			cs.setString(5, null);
			cs.setString(6, null);
			cs.setString(7, null);
			cs.setString(8, argFormDTO.getDocumentReceivedFrom().getValue());
			cs.setString(9, null);
			cs.setString(10, null);
			
			cs.registerOutParameter(11, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			
			rs = (ResultSet) cs.getObject(11);
			if (rs != null) {
				PaymentInitiateRecoveryTableDTO paymentProcess = null;
				while (rs.next()) {

					paymentProcess = new PaymentInitiateRecoveryTableDTO();

					paymentProcess.setKey(rs.getLong("PAYMENT_KEY"));
					paymentProcess.setIntimationNo(rs.getString("INTIMATION_NUMBER"));
					paymentProcess.setPolicyNumber(rs.getString("POLICY_NUMBER"));
					paymentProcess.setRodNumber(rs.getString("ROD_NUMBER"));
					paymentProcess.setRecordCount(rs.getString("RECORD_COUNT"));//Record count//RECORD_COUNT
					paymentProcess.setProductCode(rs.getString("PRODUCT_CODE"));
					paymentProcess.setCpuCode(rs.getLong("CPU_CODE"));
					paymentProcess.setPaymentCpuCode(rs.getLong("PAYMENT_CPU_CODE"));
					paymentProcess.setEmailId(rs.getString("EMAIL_ID"));
					paymentProcess.setPaymentType(rs.getString("PAYMENT_TYPE"));
					paymentProcess.setClaimType(rs.getString("CLAIM_TYPE"));
					paymentProcess.setPriorityFlag(rs.getString("BANCS_PRIORITY_FLAG"));//priority flag		//BANCS_PRIORITY_FLAG	
					paymentProcess.setPayeeName(rs.getString("PAYEE_NAME"));//payee details
					//paymentProcess.setPayeeRelationship(rs.getString("PAYEE_RELATIONSHIP"));
					paymentProcess.setNomineeName(rs.getString("NOMINEE_NAME"));
					paymentProcess.setNomineeRelationship(rs.getString("NOMINEE_RELATIONSHIP"));
					paymentProcess.setLegalHeirName(rs.getString("LEGAL_HEIR_NAME"));
					paymentProcess.setLegalHeirRelationship(rs.getString("LEGAL_HEIR_RELATIONSHIP"));
					paymentProcess.setAccType(rs.getString("ACCOUNT_TYPE"));
					paymentProcess.setIfscCode(rs.getString("IFSC_CODE"));
					paymentProcess.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
					//name of bank //BANK_NAME
					paymentProcess.setBranchName(rs.getString("BRANCH_NAME"));
					paymentProcess.setNameAsperBankAC(rs.getString("PAYEE_NAME"));//name as per bank acc
					paymentProcess.setPanNumber(rs.getString("PAN_NUMBER"));
					paymentProcess.setMicrCode(rs.getString("MICR_CODE"));
					paymentProcess.setVirtualPaymentAddr(rs.getString("VIRTUAL_PAYMENT_ADDRESS"));
					//paymentProcess.setProposerName(rs.getString("PROPOSER_NAME"));
					//paymentProcess.setGmcEmployeeName(rs.getString("EMPLOYEE_NAME"));	
					paymentProcess.setPayableAt(rs.getString("PAYABLE_AT"));//payable city
					paymentProcess.setBancsUprId(rs.getString("BANCS_UPR_ID"));//upr id //BANCS_UPR_ID
					paymentProcess.setUtrNumber(rs.getString("CHEQUE_DD_NUMBER"));//UTR/Instrument No//CHEQUE_DD_NUMBER
					//paymentProcess.setBankName(rs.getString("BANK_NAME"));
					paymentProcess.setChequeDDDate(rs.getDate("CHEQUE_DD_DATE"));	
					//search payble city
					paymentProcess.setHospitalCode(rs.getString("HOSPITAL_CODE"));//provider code
					paymentProcess.setReconisderFlag(rs.getString("RECONSIDER_FLAG"));
					paymentProcess.setApprovedAmount(rs.getDouble("APPROVED_AMOUNT"));
					paymentProcess.setLastAckDate(rs.getDate("LAST_ACKNOW_DT"));
					paymentProcess.setFaApprovalDate(rs.getDate("FA_APPROVAL_DATE"));//Date of Financial Approval //FA_APPROVAL_DATE
					paymentProcess.setDelayDays(rs.getLong("DELAY_DAYS"));//No of Day for Processing
					//paymentProcess.setiRDATAT(rs.getLong("DELAY_DAYS"));//IRDA TAT 
					paymentProcess.setInterestRate(rs.getDouble("INT_RATE"));//No of Days Interest Payable
					paymentProcess.setInterestAmount(rs.getDouble("INT_AMT"));//Penal Interest Amount
					paymentProcess.setTotalAmount(rs.getDouble("TOTAL_AMOUNT"));
					paymentProcess.setIntrestRemarks(rs.getString("INT_REMARKS"));
					paymentProcess.setRemarks(rs.getString("REMARKS"));	
					
					paymentProcess.setClaimNumber(rs.getString("CLAIM_NUMBER"));
					paymentProcess.setPaymentKey(rs.getLong("PAYMENT_KEY"));
					paymentProcess.setRodKey(rs.getLong("ROD_KEY"));
					
					paymentProcess.setDocumentReceivedFrom(argFormDTO.getDocumentReceivedFrom());
					
					Double tds = rs.getDouble("TDS_AMOUNT");
					paymentProcess.setTdsDeducted(tds.toString());
					
					Double settleAmt = rs.getDouble("TOT_APPROVED_AMOUNT");
					paymentProcess.setAmountSettled(settleAmt.toString());
					
					paymentProcess.setPartyCode(rs.getString("SOURCE_RISK_ID"));
					
					Reimbursement reimbursementDtls = reimbursementService.getReimbursementByKey(paymentProcess.getRodKey());
					DocAcknowledgement docAcknowledgement = getDocAcknowledgementBasedOnKey(reimbursementDtls.getDocAcknowLedgement().getKey());
					Claim claimdlts = claimService.getClaimByClaimKey(docAcknowledgement.getClaim().getKey());
//					Below Code Commented as per satish sir instruction
//					String partyCode = "";
//					if(claimdlts.getIntimation().getInsured().getSourceRiskId() != null){
//						partyCode = claimdlts.getIntimation().getInsured().getSourceRiskId(); 
//						paymentProcess.setPartyCode(partyCode);
//					}
//					Insured insuredDtls = insuredService.getInsuredByInsuredKey(claimdlts.getIntimation().getInsured().getKey());
					String billClassifaction = getBillClassificationValue(docAcknowledgement);
					if(billClassifaction != null && !billClassifaction.isEmpty()){
						paymentProcess.setBillClassification(billClassifaction);
					}
					if(claimdlts != null){
						/*String processtype = claimdlts.getProcessClaimType();
						paymentProcess.setLob(processtype);*/
						Long lobId = claimdlts.getLobId();
						if(lobId != null && lobId.equals(ReferenceTable.HEALTH_LOB_KEY)){
							paymentProcess.setLob("Individual Health");
						} else if(lobId != null){
							paymentProcess.setLob("Personal Accident");
						}
						
					}
					if(paymentProcess.getPaymentType() != null){
						if(paymentProcess.getPaymentType().equalsIgnoreCase(SHAConstants.CHEQUE)){
							paymentProcess.setPreviousPaymentMode("DD");
						} else if (paymentProcess.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
							paymentProcess.setPreviousPaymentMode("NEFT");
						}
					}
//					paymentProcess.setReprocessType(argFormDTO.getReprocessType().getValue());
//					paymentProcess.setPaymenetCancelTypeKey(argFormDTO.getReprocessType().getId());*/
					initiateRecoveryList.add(paymentProcess);
					page.setPageItems(initiateRecoveryList);
					page.setIsDbSearch(true);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return page;
	}
	
	public void updatePaymentInitiateRecoveryData(PaymentInitiateRecoveryTableDTO dto){
		BancsPaymentCancel paymentCancel = new BancsPaymentCancel();
		paymentCancel.setIntimationNumber(dto.getIntimationNo() != null ? dto.getIntimationNo() : null);
		paymentCancel.setClaimNumber(dto.getClaimNumber() != null ? dto.getClaimNumber() : null);
		paymentCancel.setRodNumber(dto.getRodNumber() != null ? dto.getRodNumber() : null);
		paymentCancel.setPolicyNumber(dto.getPolicyNumber() != null ? dto.getPolicyNumber() : null);
		paymentCancel.setPaymentKey(dto.getPaymentKey() != null ? dto.getPaymentKey() : null);
		paymentCancel.setRodKey(dto.getRodKey() != null ? dto.getRodKey() : null);
		paymentCancel.setBancsUprId(dto.getBancsUprId() != null ? dto.getBancsUprId() : null);
		paymentCancel.setClaimType(dto.getClaimType() != null ? dto.getClaimType() : null);
		paymentCancel.setPaymentType(dto.getPaymentTypeVal().getValue() != null ? dto.getPaymentTypeVal().getValue() : "");
		paymentCancel.setPaymenetCancelTypeKey(dto.getPaymenetCancelTypeKey() != null ? dto.getPaymenetCancelTypeKey() : null);
		paymentCancel.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : null);
		paymentCancel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		paymentCancel.setTransactionDate(new Timestamp(System.currentTimeMillis()));
		paymentCancel.setApprovedAmount(Double.valueOf(dto.getAmountSettled()));
		paymentCancel.setPaybleAt(dto.getPayableAt() != null ? dto.getPayableAt() : null);
		/*paymentCancel.setPayableName(dto.getPayableName() != null ? dto.getPayableName() : null);
		paymentCancel.setInstrumentNumber(dto.getInstrumentNo() != null ? dto.getInstrumentNo() : null);
		paymentCancel.setInstrumentDate(dto.getInstrumentDate() != null ? dto.getInstrumentDate() : null);*/
		paymentCancel.setPayableName(dto.getPayeeName() != null ? dto.getPayeeName() : (dto.getNomineeName() != null ? dto.getNomineeName():(dto.getLegalHeirName() != null ? dto.getLegalHeirName():"")));
		paymentCancel.setInstrumentNumber(dto.getUtrNumber() != null ? dto.getUtrNumber() : null);
		paymentCancel.setInstrumentDate(dto.getChequeDDDate() != null ? dto.getChequeDDDate() : null);
		paymentCancel.setRemittanceBankName(dto.getRemittanceBankName() != null ? dto.getRemittanceBankName() : null);
		paymentCancel.setRemittancebankBranch(dto.getRemittanceBankBranch() != null ? dto.getRemittanceBankBranch() : null);
		paymentCancel.setRemittanceAccount(dto.getRemittanceAccount() != null ? dto.getRemittanceAccount() : null);
		paymentCancel.setPartyCode(dto.getPartyCode() != null ? dto.getPartyCode() : "");
		paymentCancel.setLob(dto.getLob() != null ? dto.getLob() : "");
		paymentCancel.setCpu(dto.getCpuCode() != null  ? String.valueOf(dto.getCpuCode()) : "");
		paymentCancel.setPaymentCpuCode(dto.getPaymentCpuCode() != null ? String.valueOf(dto.getPaymentCpuCode()) : "");
		paymentCancel.setAmountChangedFlag(SHAConstants.YES_FLAG);
		paymentCancel.setPaymenetCancelTypeKey(dto.getPaymentCancelType().getId() != null ? dto.getPaymentCancelType().getId() : null);
		paymentCancel.setRecoveryReasonId(dto.getReasonForRecovery().getId() != null ? dto.getReasonForRecovery().getId() : null);
		paymentCancel.setNatureRecoveryId(dto.getNatureofRecovery().getId() != null ? dto.getNatureofRecovery().getId() : null);
		paymentCancel.setPreviousPaymentMode(dto.getPreviousPaymentMode() != null ? dto.getPreviousPaymentMode() : null);
		
		entityManager.persist(paymentCancel);
		entityManager.flush();
	}
	
	public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		DocAcknowledgement docAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		if (null != query) {
			docAcknowledgement = (DocAcknowledgement) query.getSingleResult();
		}
		return docAcknowledgement;
	}
	
	private String getBillClassificationValue(DocAcknowledgement docAck) {
		StringBuilder strBuilder = new StringBuilder();
		// StringBuilder amtBuilder = new StringBuilder();
		// Double total = 0d;
		try {
			if (("Y").equals(docAck.getHospitalisationFlag())) {
				strBuilder.append("Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPreHospitalisationFlag())) {
				strBuilder.append("Pre-Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPostHospitalisationFlag())) {
				strBuilder.append("Post-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getPartialHospitalisationFlag())) {
				strBuilder.append("Partial-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getLumpsumAmountFlag())) {
				strBuilder.append("Lumpsum Amount");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getHospitalCashFlag())) {
				strBuilder.append("Add on Benefits (Hospital cash)");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getPatientCareFlag())) {
				strBuilder.append("Add on Benefits (Patient Care)");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getHospitalizationRepeatFlag())) {
				strBuilder.append("Hospitalization Repeat");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getCompassionateTravel())) {
				strBuilder.append("Compassionate Travel");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getRepatriationOfMortalRemain())) {
				strBuilder.append("Repatriation Of Mortal Remains");
				strBuilder.append(",");
			}
			
			if(null != docAck.getClaim()&& docAck.getClaim().getIntimation() != null && docAck.getClaim().getIntimation().getPolicy() != null &&
					(ReferenceTable.FHO_REVISED_PRODUCT_2021_KEY.equals(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey())
							|| ReferenceTable.getValuableServiceProviderForFHO().containsKey(docAck.getClaim().getIntimation().getPolicy().getProduct().getKey()))){
				if (("Y").equals(docAck.getPreferredNetworkHospita())) {
					strBuilder.append("Valuable Service Provider (Hospital)");
					strBuilder.append(",");
				}
			}
			else{ 
				if (("Y").equals(docAck.getPreferredNetworkHospita())) {
					strBuilder.append("Preferred Network Hospital");
					strBuilder.append(",");
				}
			}
			
			if (("Y").equals(docAck.getSharedAccomodation())) {
				strBuilder.append("Shared Accomodation");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getEmergencyMedicalEvaluation())) {
				strBuilder.append("Emergency Medical Evacuation");
				strBuilder.append(",");
			}
			
			if (("Y").equals(docAck.getStarWomenCare())) {
				strBuilder.append("Star Mother Cover");
				strBuilder.append(",");
			}
			
			//added for new product076
			if (("Y").equals(docAck.getProdHospBenefitFlag())) {	
			strBuilder.append("Hospital Cash");
			}
			
			// rodQueryDTO.setClaimedAmount(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuilder.toString();
	}

}