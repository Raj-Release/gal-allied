package com.shaic.claim.policy.search.ui.opsearch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpDTO;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpMapper;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpService;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpTableDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OutpatientService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

@Stateless
public class SearchSettlementLetterProcessOPClaimRequestService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private OutpatientService outPatientService;

	private final Logger log = LoggerFactory
			.getLogger(CreateBatchOpService.class);

	public SearchSettlementLetterProcessOPClaimRequestService() {
		super();
	}
	
	public Page<CreateBatchOpTableDTO> search(
			CreateBatchOpDTO searchFormDTO, String userName,
			String passWord) {
		Page<CreateBatchOpTableDTO> page = null;

		try {
		
			String strType = null;
			String intimationNo = null;
			String claimNo = null;
			String batchNo = null;
			String strClaimType = null;
			SelectValue selClaimType = null;
			SelectValue claimantType = null;
			String strClaimant = null;
			Date startDate = null;
			Date toDate = null;	
			SelectValue selPIOCode = null;
			List<Long> listOfCPUs = new ArrayList<Long>();
			
			StringBuilder multiCpu = null;
			String cpuSearch = null;
			
//			SelectValue selTypeValue = searchFormDTO.getType();
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();
//			if (null != selTypeValue && null != selTypeValue.getValue()) {
//				strType = selTypeValue.getValue();
//			}

			/*if (null != searchFormDTO && null != strType
					&& (SHAConstants.CREATE_BATCH_OP).equalsIgnoreCase(strType)) {*/
				
//				startDate = searchFormDTO.getFromDate();
//				toDate = searchFormDTO.getToDate();
				intimationNo = searchFormDTO.getIntimationNo();
				
//				selClaimType = searchFormDTO.getClaimType();
//				if (null != selClaimType && null != selClaimType.getValue()) {
//					strClaimType = selClaimType.getValue();
//				}
//				
////			}
//
//			else if (null != searchFormDTO && null != strType
//					&& (SHAConstants.SEARCH_BATCH_OP).equalsIgnoreCase(strType)) {
//				
//			}

			final CriteriaBuilder criteriaBuilder = entityManager
					.getCriteriaBuilder();
			final CriteriaBuilder criteriaBuilderCount = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Long> criteriaQueryCount = criteriaBuilderCount.createQuery(Long.class);
			final CriteriaQuery<OPHealthCheckup> criteriaQuery = criteriaBuilder
					.createQuery(OPHealthCheckup.class);

			Root<OPHealthCheckup> root = criteriaQuery.from(OPHealthCheckup.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();

			if(StringUtils.isNotBlank(searchFormDTO.getIntimationNo())){
				Predicate searchByIntimation = criteriaBuilder.like(root.<OPClaim>get("claim").<OPIntimation>get("intimation").<String>get("intimationId"), "%"+searchFormDTO.getIntimationNo()+"%");
				conditionList.add(searchByIntimation);
//				System.out.println("OP Intimation No : "+searchFormDTO.getIntimationNo());
			}
			
			if(StringUtils.isNotBlank(searchFormDTO.getPolicyNumber())){
				Predicate searchByPolicy = criteriaBuilder.equal(root.<OPClaim>get("claim").<OPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), searchFormDTO.getPolicyNumber());
				conditionList.add(searchByPolicy);
//				System.out.println("OP Policy No : "+searchFormDTO.getPolicyNumber());
			}
			
			if(StringUtils.isNotBlank(searchFormDTO.getHealthCardNo())){
				Predicate searchByHealthCardNo = criteriaBuilder.equal(root.<OPClaim>get("claim").<OPIntimation>get("intimation").<Insured>get("insured").<String>get("healthCardNumber"), searchFormDTO.getHealthCardNo());
				conditionList.add(searchByHealthCardNo);
//				System.out.println("OP Health Card Number : "+searchFormDTO.getHealthCardNo());
			}
			if (StringUtils.isNotBlank(searchFormDTO.getClaimNo())) {
				Predicate searchByClaimNo = criteriaBuilder.equal(root.<OPClaim>get("claim").<String>get("claimId"), searchFormDTO.getClaimNo());
				conditionList.add(searchByClaimNo);
			}
			
			if(searchFormDTO.getPioCode() != null){
				Predicate searchByPIOCode = criteriaBuilder.like(root.<OPClaim>get("claim").<OPIntimation>get("intimation").<Policy>get("policy").<String>get("policyNumber"), "%"+searchFormDTO.getPioCode().getValue()+"%");
				conditionList.add(searchByPIOCode);
			}
				
				Predicate condition3 = criteriaBuilder.equal(
						root.<MastersValue> get("status").<String> get(
								"key"), ReferenceTable.OP_PAYMENT_SETTLED);
				conditionList.add(condition3);
				
				Predicate condition4 = criteriaBuilder.equal(
						root.<String> get("settlementLetterFlag"), SHAConstants.N_FLAG);
				conditionList.add(condition4);
			
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList
							.toArray(new Predicate[] {})));
			
			/*criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));*/
			
			final TypedQuery<OPHealthCheckup> typedQuery = entityManager
					.createQuery(criteriaQuery);
			
			criteriaQueryCount.select(criteriaBuilderCount.count(criteriaQueryCount.from(OPHealthCheckup.class)));
			
			criteriaQueryCount.where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			
			Long totalCount = entityManager.createQuery(criteriaQueryCount).getSingleResult();
	
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			int firtResult;
			if (pageNumber > 1) {
				firtResult = (pageNumber - 1) * 25;
			} else {
				firtResult = 0;
			}			

			List<OPHealthCheckup> claimPaymentList = new ArrayList<OPHealthCheckup>();
			List<OPHealthCheckup> totalPaymentList = new ArrayList<OPHealthCheckup>();


			totalPaymentList = typedQuery.getResultList();
			claimPaymentList = typedQuery.setFirstResult(firtResult)
					.setMaxResults(25).getResultList();

			List result = new ArrayList<ClaimPayment>();

			if (claimPaymentList.size() >= 25) {
				result = typedQuery.setFirstResult(claimPaymentList.size() + 1)
						.setMaxResults(1).getResultList();
			}
			List<CreateBatchOpTableDTO> tableDTO = CreateBatchOpMapper.getInstance()
					.getListOfOPSearchBatchTableDTO(claimPaymentList);
			if (null != tableDTO && !tableDTO.isEmpty()) {
				int i =1;
				
				for (CreateBatchOpTableDTO createAndSearchLotTableDTO : tableDTO) {
					DBCalculationService calculationService = new DBCalculationService();
					if(createAndSearchLotTableDTO.getIntimationNo()!=null){
		
					}
					Policy policyDetails = getPolicyByPolicyNubember(createAndSearchLotTableDTO.getPolicyNo());
					
					if(createAndSearchLotTableDTO.getOpHealthCheckUpKey() != null){
						List<OPDocumentBillEntry> billdetails = outPatientService.getOpBillEntryDetails(createAndSearchLotTableDTO.getOpHealthCheckUpKey());
						Double billAmt = 0d;
						Double deductibleAmt = 0d;
						Double payableAmt = 0d;
						if(billdetails != null && !billdetails.isEmpty()){
							for (OPDocumentBillEntry opDocumentBillEntry : billdetails) {
								if(opDocumentBillEntry.getClaimedAmount() != null){
									billAmt += opDocumentBillEntry.getClaimedAmount();
								}
								if(opDocumentBillEntry.getDeductibleAmt() != null){
									deductibleAmt += opDocumentBillEntry.getDeductibleAmt();
								}
								if(opDocumentBillEntry.getPayableAmt() != null){
									payableAmt += opDocumentBillEntry.getPayableAmt();
								}
							}
						}
						createAndSearchLotTableDTO.setBillAmt(billAmt);
						createAndSearchLotTableDTO.setTotalBillAmt(billAmt);
						createAndSearchLotTableDTO.setDeductionAmt(deductibleAmt);
						createAndSearchLotTableDTO.setPayableAmt(payableAmt);
						createAndSearchLotTableDTO.setTotalPayableAmt(payableAmt);
					}
					
					if(createAndSearchLotTableDTO.getPaymentCpucodeTextValue()!=null){
						TmpCPUCode masCpuCode = null;
						try {
							masCpuCode = getMasCpuCode(Long.parseLong(createAndSearchLotTableDTO.getPaymentCpucodeTextValue()));
							createAndSearchLotTableDTO.setPaymentCpucodeTextValue(masCpuCode.getCpuCode().toString() + " - " + masCpuCode.getDescription());
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();	
						}
					}
					
					if(createAndSearchLotTableDTO.getPaymentCpuString()!=null){
						TmpCPUCode masCpuCode = null;
						try {
							masCpuCode = getMasCpuCode(Long.parseLong(createAndSearchLotTableDTO.getPaymentCpuString()));
							if(masCpuCode!=null){
								SelectValue select = new SelectValue();
								select.setId(masCpuCode.getKey());
								select.setValue(masCpuCode.getCpuCode().toString() + " - " + masCpuCode.getDescription());
								createAndSearchLotTableDTO.setPaymentCpuCode(select);
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();	
						}
					}

					if (null != createAndSearchLotTableDTO
							&& (ReferenceTable.PAYMENT_TYPE_CHEQUE)
									.equalsIgnoreCase(createAndSearchLotTableDTO
											.getPaymentTypeValue())) {
						createAndSearchLotTableDTO
								.setPaymentModeFlag(SHAConstants.YES_FLAG);
						
						createAndSearchLotTableDTO.setChangedPaymentMode(ReferenceTable.PAYMENT_TYPE_CHEQUE);
						
						//createAndSearchLotTableDTO.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
						createAndSearchLotTableDTO.setBeneficiaryAcntNo(null);
						createAndSearchLotTableDTO.setIfscCode(null);
						createAndSearchLotTableDTO.setBankCode(null);
						createAndSearchLotTableDTO.setBankName(null);
						createAndSearchLotTableDTO.setBranchName(null);
						
					} else {
						createAndSearchLotTableDTO
								.setPaymentModeFlag(SHAConstants.N_FLAG);
						createAndSearchLotTableDTO.setChangedPaymentMode(SHAConstants.NEFT_TYPE);
					}		
					
					if(null != createAndSearchLotTableDTO && 
							createAndSearchLotTableDTO.getPaymentStatusKey() != null){
						if(createAndSearchLotTableDTO.getPaymentStatusKey().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
							createAndSearchLotTableDTO.setPaymentStatus("CHEQUE");
							createAndSearchLotTableDTO.setPaymentTypeValue("CHEQUE / DD");
						} else if(createAndSearchLotTableDTO.getPaymentStatusKey().equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)){
							createAndSearchLotTableDTO.setPaymentStatus("NEFT");
							createAndSearchLotTableDTO.setPaymentTypeValue("BANK TRANSFER");
						}
					}

					BankMaster cityName = getCityName(createAndSearchLotTableDTO 
							.getIfscCode());
					if (null != cityName)

					{
						createAndSearchLotTableDTO.setBankName(cityName.getBankName());
						createAndSearchLotTableDTO.setBranchName(cityName.getBranchName());
						createAndSearchLotTableDTO.setCity(cityName.getCity());
						createAndSearchLotTableDTO.setPayableAt(cityName
								.getCity());
					}					
					 
					i++;
				}
			}

			/**
			 * The below code is added for downloading all the payment records
			 * in a single select. For this, we need to have a complete list of
			 * records without pagination. Also, this list needs to be converted
			 * to DTO list for excel report generation. Hence adding below code.
			 * 
			 * **/

			
			List<CreateBatchOpTableDTO> completeTableDTOList = CreateBatchOpMapper
					.getListOfOPSearchBatchTableDTO(totalPaymentList);
						
			
			if (null != completeTableDTOList && !completeTableDTOList.isEmpty()) {
				int i =1;
				for (CreateBatchOpTableDTO createAndSearchLotTableDTO : completeTableDTOList) {
					
					//DBCalculationService calculationService = new DBCalculationService();
					if(createAndSearchLotTableDTO.getIntimationNo()!=null){
						Intimation intimationByNo = getIntimationByNo(createAndSearchLotTableDTO.getIntimationNo());
						if(intimationByNo!=null){
							
							
						}
					}
					
					if(createAndSearchLotTableDTO.getOpHealthCheckUpKey() != null){
						List<OPDocumentBillEntry> billdetails = outPatientService.getOpBillEntryDetails(createAndSearchLotTableDTO.getOpHealthCheckUpKey());
						Double billAmt = 0d;
						Double deductibleAmt = 0d;
						Double payableAmt = 0d;
						if(billdetails != null && !billdetails.isEmpty()){
							for (OPDocumentBillEntry opDocumentBillEntry : billdetails) {
								if(opDocumentBillEntry.getClaimedAmount() != null){
									billAmt += opDocumentBillEntry.getClaimedAmount();
								}
								if(opDocumentBillEntry.getDeductibleAmt() != null){
									deductibleAmt += opDocumentBillEntry.getDeductibleAmt();
								}
								if(opDocumentBillEntry.getPayableAmt() != null){
									payableAmt += opDocumentBillEntry.getPayableAmt();	
								}
							}
						}
						createAndSearchLotTableDTO.setBillAmt(billAmt);
						createAndSearchLotTableDTO.setTotalBillAmt(billAmt);
						createAndSearchLotTableDTO.setDeductionAmt(deductibleAmt);
						createAndSearchLotTableDTO.setPayableAmt(payableAmt);
						createAndSearchLotTableDTO.setTotalPayableAmt(payableAmt);
					}
					if(createAndSearchLotTableDTO.getCpuCode()!=null){
						TmpCPUCode masCpuCode = null;
						try {
							masCpuCode = getMasCpuCode(Long.parseLong(createAndSearchLotTableDTO.getCpuCode()));
							createAndSearchLotTableDTO.setCpuCode(masCpuCode.getCpuCode().toString() + " - " + masCpuCode.getDescription());
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(masCpuCode!=null){
							createAndSearchLotTableDTO.setCpuName(masCpuCode.getDescription());
						}
					}
					
					if(createAndSearchLotTableDTO.getPayeeNameStr()!=null){
						SelectValue payeeName = new SelectValue();
						payeeName.setId(0l);
						payeeName.setValue(createAndSearchLotTableDTO.getPayeeNameStr());
						createAndSearchLotTableDTO.setTempPayeeName(payeeName);
					}
					
					
					
					if(createAndSearchLotTableDTO.getPaymentCpuString()!=null){
						TmpCPUCode masCpuCode = null;
						try {
							masCpuCode = getMasCpuCode(Long.parseLong(createAndSearchLotTableDTO.getPaymentCpuString()));
							if(masCpuCode!=null){
								SelectValue select = new SelectValue();
								select.setId(masCpuCode.getCpuCode());
								select.setValue(masCpuCode.getCpuCode().toString() + " - " + masCpuCode.getDescription());
								createAndSearchLotTableDTO.setPaymentCpuCode(select);
							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();	
						}
					}
					
					createAndSearchLotTableDTO.setSerialNo(i);

					if (null != createAndSearchLotTableDTO
							&& (ReferenceTable.PAYMENT_TYPE_CHEQUE)
							.equalsIgnoreCase(createAndSearchLotTableDTO
									.getPaymentTypeValue())) {
						createAndSearchLotTableDTO
								.setPaymentModeFlag(SHAConstants.YES_FLAG);
						createAndSearchLotTableDTO.setBeneficiaryAcntNo(null);
						createAndSearchLotTableDTO.setIfscCode(null);
						createAndSearchLotTableDTO.setBankCode(null);
						createAndSearchLotTableDTO.setBankName(null);
						createAndSearchLotTableDTO.setBranchName(null);
					} else {
						createAndSearchLotTableDTO
								.setPaymentModeFlag(SHAConstants.N_FLAG);
					}
					

					BankMaster cityName = getCityName(createAndSearchLotTableDTO
							.getIfscCode());
					if (null != cityName)

					{
						createAndSearchLotTableDTO.setBankName(cityName.getBankName());
						createAndSearchLotTableDTO.setBranchName(cityName.getBranchName());
						createAndSearchLotTableDTO.setCity(cityName.getCity());
						createAndSearchLotTableDTO.setPayableAt(cityName
								.getCity());
					}					
					i++;
				}
			}

			page = new Page<CreateBatchOpTableDTO>();			
			searchFormDTO.getPageable().setPageNumber(pageNumber);
			page.setHasNext(true);
			if (result.isEmpty()) {
				page.setHasNext(false);
			} else {
				page.setHasNext(true);
			}

			if (tableDTO.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(tableDTO);		
			if (null != totalPaymentList) {
				page.setTotalRecords(totalPaymentList.size());
			}
			if (null != completeTableDTOList) {
				page.setTotalList(completeTableDTOList);
			}		
			page.setIsDbSearch(true);
		
			return page;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}
	
	public Policy getPolicyByPolicyNubember(String policyNumber){
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query.setParameter("policyNumber", policyNumber);
		
		List<Policy> result =  (List<Policy>) query.getResultList();
		if(result != null && !result.isEmpty()){
			return result.get(0);
		}
		return null;
	}
	
	public void updateChequeDetails(OPHealthCheckup ophealthChekupDtls,OPClaim opClaim){
		if(ophealthChekupDtls.getKey() != null){
			entityManager.merge(ophealthChekupDtls);
			entityManager.flush();
		}
	}
	
	public TmpCPUCode getMasCpuCode(Long cpuCode){
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	private BankMaster getCityName(String ifscCode) {
		Query query = entityManager
				.createNamedQuery("BankMaster.findByIfscCode");
		query = query.setParameter("ifscCode", ifscCode);
		List<BankMaster> bankList = query.getResultList();
		if (null != bankList && !bankList.isEmpty()) {
			return bankList.get(0);
		} else {
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public OPHealthCheckup getOPByKey(Long opcheckupKey) {
		Query findByKey = entityManager.createNamedQuery(
				"OPHealthCheckup.findByKey").setParameter(
				"primaryKey", opcheckupKey);

		List<OPHealthCheckup> intimationList = (List<OPHealthCheckup>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	public void uploadOPSettlementLetterToDMs(ClaimDto claimDto,OPHealthCheckup opHealthCheckup){

		if(null != claimDto.getDocFilePath() && !claimDto.getDocFilePath().isEmpty())
		{
			String strUserName = claimDto.getModifiedBy();
			String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
			
			WeakHashMap dataMap = new WeakHashMap();
			if(null != claimDto)
			{
				dataMap.put("intimationNumber",claimDto.getNewIntimationDto().getIntimationId());
				dataMap.put("claimNumber",claimDto.getClaimId());

				dataMap.put("filePath", claimDto.getDocFilePath());
				dataMap.put("docType", claimDto.getDocType());
				dataMap.put("docSources", SHAConstants.OP_SETTLEMENT_LETTER);
				dataMap.put("createdBy", userNameForDB);
				String docToken = SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
				if(docToken != null){
					opHealthCheckup.setSettlementLetterFlag(SHAConstants.YES_FLAG);
					entityManager.merge(opHealthCheckup);
					entityManager.flush();
				}
			}			
			
		}
	}
	
}
