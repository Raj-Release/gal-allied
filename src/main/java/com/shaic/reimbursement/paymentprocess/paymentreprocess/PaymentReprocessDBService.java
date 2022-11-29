package com.shaic.reimbursement.paymentprocess.paymentreprocess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import oracle.jdbc.OracleTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.BancsPaymentCancelMapper;
import com.shaic.domain.BancsPaymentCancel;
import com.shaic.domain.Claim;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.UI;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
@SuppressWarnings("deprecation")
public class PaymentReprocessDBService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private MasterService masterService;
	private final Logger log = LoggerFactory.getLogger(PaymentReprocessDBService.class);

	public BeanItemContainer<SelectValue> getPaymentMode(){
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selectVal = new SelectValue();
		selectVal.setId(461L);
		selectVal.setValue("CHEQUE");
		container.addBean(selectVal);
		return container;
	}

	public BeanItemContainer<SelectValue> getClaimant(){
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selectVal = new SelectValue();
		selectVal.setId(1541L);
		selectVal.setValue("Insured");
		container.addBean(selectVal);
		return container;
	}

	public BeanItemContainer<SelectValue> getPaymentProcessType(){
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selectVal = new SelectValue();
		selectVal.setId(10001L);
		selectVal.setValue("Reprocess");
		container.addBean(selectVal);
		return container;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getMastersValuebyTypeCode(String masterTypeCode) {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCodeWithStatus");
		query = query.setParameter("masterTypeCode", masterTypeCode);
		List<MastersValue> mastersValueList = query.getResultList();
		BeanItemContainer<SelectValue> resultContainer = getResultContainer(mastersValueList);		
		return resultContainer;
	}

	private BeanItemContainer<SelectValue> getResultContainer(List<MastersValue> a_mastersValueList) {
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

		for (MastersValue mastersValue : a_mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(mastersValue.getKey());
			select.setValue(mastersValue.getValue());
			selectValuesList.add(select);
		}
		resultContainer.addAll(selectValuesList);

		return resultContainer;
	}

	public Page<PaymentReprocessSearchResultDTO> performSearch(PaymentReprocessSearchFormDTO argFormDTO,String userName, String argScreenName){
		Page<PaymentReprocessSearchResultDTO> page = new Page<PaymentReprocessSearchResultDTO>();
		List<PaymentReprocessSearchResultDTO> createBatchPaymentList = new ArrayList<PaymentReprocessSearchResultDTO>();
		final String PAYMENT_REPROCESS_PROC = "{call PRC_GB_GET_PAYMENT_CANCEL(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try{
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall(PAYMENT_REPROCESS_PROC);
			cs.setString(1, argFormDTO.getType().getValue());
			cs.setString(2, argFormDTO.getIntimationNumber());
			cs.setString(3, argFormDTO.getUprId());
			cs.setString(4, argFormDTO.getUtrNo());
			cs.setString(5, argFormDTO.getCpuCode());
			cs.setString(6, argFormDTO.getPaymentCPU());
			cs.setString(7, argFormDTO.getClaimType());
			cs.setString(8, argFormDTO.getClaimant().getValue());
			cs.setString(9, argFormDTO.getPaymentMode().getValue());
			cs.setString(10, argFormDTO.getReprocessType().getValue());
			
			cs.registerOutParameter(11, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();
			
			rs = (ResultSet) cs.getObject(11);
			if (rs != null) {
				PaymentReprocessSearchResultDTO paymentProcess = null;
				while (rs.next()) {

					paymentProcess = new PaymentReprocessSearchResultDTO();

					paymentProcess.setKey(rs.getLong("PAYMENT_KEY"));
					paymentProcess.setIntimationNumber(rs.getString("INTIMATION_NUMBER"));
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
					
					
					paymentProcess.setReprocessType(argFormDTO.getReprocessType().getValue());
					paymentProcess.setPaymenetCancelTypeKey(argFormDTO.getReprocessType().getId());
					createBatchPaymentList.add(paymentProcess);
					page.setPageItems(createBatchPaymentList);
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
	
	public void submitCancelReprocessRemarks(PaymentReprocessSearchResultDTO dto){
		
			BancsPaymentCancelMapper mapper=new BancsPaymentCancelMapper();
			BancsPaymentCancel paymentCancel = null; 
			paymentCancel= getPaymentKey(dto.getKey());
			if(paymentCancel!=null){
				
				paymentCancel.setIntimationNumber(dto.getIntimationNumber());
				paymentCancel.setClaimNumber(dto.getClaimNumber());
				paymentCancel.setRodNumber(dto.getRodNumber());
				paymentCancel.setPolicyNumber(dto.getPolicyNumber());
				paymentCancel.setPaymentKey(dto.getPaymentKey());
				paymentCancel.setRodKey(dto.getRodKey());
				paymentCancel.setBancsUprId(dto.getBancsUprId());
				paymentCancel.setClaimType(dto.getClaimType());
				paymentCancel.setPaymentType(dto.getPaymentType());
				paymentCancel.setPaymenetCancelTypeKey(dto.getPaymenetCancelTypeKey());
				paymentCancel.setPaymentCancelGlxRead(dto.getPaymentCancelBancsRead());
				paymentCancel.setPaymentCancelBancsRead(dto.getPaymentCancelBancsRead());
				paymentCancel.setPaymenetCancelRemarks(dto.getPaymenetCancelRemarks());
				//paymentCancel.setBancsReadDate(new Date());
				paymentCancel.setGlxReadDate(new Date());

			}else{
			paymentCancel=new BancsPaymentCancel();
			paymentCancel.setIntimationNumber(dto.getIntimationNumber());
			paymentCancel.setClaimNumber(dto.getClaimNumber());
			paymentCancel.setRodNumber(dto.getRodNumber());
			paymentCancel.setPolicyNumber(dto.getPolicyNumber());
			paymentCancel.setPaymentKey(dto.getPaymentKey());
			paymentCancel.setRodKey(dto.getRodKey());
			paymentCancel.setBancsUprId(dto.getBancsUprId());
			paymentCancel.setClaimType(dto.getClaimType());
			paymentCancel.setPaymentType(dto.getPaymentType());
			paymentCancel.setPaymenetCancelTypeKey(dto.getPaymenetCancelTypeKey());
			paymentCancel.setPaymentCancelGlxRead(dto.getPaymentCancelBancsRead());
			paymentCancel.setPaymentCancelBancsRead(dto.getPaymentCancelBancsRead());
			paymentCancel.setPaymenetCancelRemarks(dto.getPaymenetCancelRemarks());
			//paymentCancel.setBancsReadDate(new Date());
			paymentCancel.setGlxReadDate(new Date());
			}
			if(dto.getKey() !=null && paymentCancel!=null && paymentCancel.getKey()!=null){
				paymentCancel.setModifedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				paymentCancel.setModifiedDate(new Date());
				entityManager.merge(paymentCancel);
				entityManager.flush();
				entityManager.clear();
				log.info("------BancsPaymentCancel------>"+paymentCancel+"<------------");	
			} else {
				paymentCancel.setCreatedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				paymentCancel.setCreatedDate(new Date());
			/*	paymentCancel.setModifedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				paymentCancel.setModifiedDate(new Date());*/
				entityManager.persist(paymentCancel);
				entityManager.flush();
				entityManager.clear();
				log.info("------BancsPaymentCancel------>"+paymentCancel+"<------------");	
			}
	}
	
	@SuppressWarnings("unchecked")
	public BancsPaymentCancel getPaymentKey(Long paymentKey) {
		Query query = entityManager.createNamedQuery("BancsPaymentCancel.findByPaymentKey");
		query.setParameter("paymentKey", paymentKey);
		List<BancsPaymentCancel> bancsPaymentList = (List<BancsPaymentCancel>)query.getResultList();
		
		if(bancsPaymentList != null && ! bancsPaymentList.isEmpty()){	
			return bancsPaymentList.get(0);
		}else{
			return null;
		}
	
	}

}
