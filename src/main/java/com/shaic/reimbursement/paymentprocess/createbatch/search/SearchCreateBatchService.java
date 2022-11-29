/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.leagalbilling.LegalBillingDTO;
import com.shaic.claim.leagalbilling.LegalTaxDeduction;
import com.shaic.claim.leagalbilling.LegalTaxDeductionMapper;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotMapper;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.domain.MasHospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.NomineeDetails;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * @author ntv.narenj
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SearchCreateBatchService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	List<String> batchNoList = new ArrayList<String>();	

	public  Page<CreateAndSearchLotTableDTO> search(
			SearchCreateBatchFormDTO searchFormDTO,
			String userName, String passWord) {
		Page<CreateAndSearchLotTableDTO> page = null;
		
		try
		{	
			utx.setTransactionTimeout(360000);
			utx.begin();
			String presenterString = null != searchFormDTO.getMenuString() ? searchFormDTO.getMenuString() : null;
			Date fromDate = null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
			Date toDate = null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;
			String batchNo = null != searchFormDTO.getBatchNo() ? searchFormDTO.getBatchNo() : null;
			String lotNo = null != searchFormDTO.getLotNo() ? searchFormDTO.getLotNo() : null;
			String intimationNo = null != searchFormDTO.getIntimationNo() ? searchFormDTO.getIntimationNo() : null;
			String rodNo = null != searchFormDTO.getRodNO() ? searchFormDTO.getRodNO() : null;
			String claimNo = null != searchFormDTO.getClaimNo() ? searchFormDTO.getClaimNo() : null;
			SelectValue selpaymentCpuCodeValue = null;
			if(presenterString !=null && !presenterString.equals("CREATE_BATCH")){
				selpaymentCpuCodeValue = null != searchFormDTO.getPayCpuCode() ? searchFormDTO.getPayCpuCode() : null;
			}
			SelectValue selZone = null != searchFormDTO.getZone() ? searchFormDTO.getZone() : null;
			SelectValue selClaimType = null != searchFormDTO.getCliamType() ? searchFormDTO.getCliamType() : null;			
			SelectValue claimantType = null != searchFormDTO.getClaimant() ? searchFormDTO.getClaimant() : null;			
			SelectValue paymentModeType = null != searchFormDTO.getPaymentMode() ? searchFormDTO.getPaymentMode() : null;			
			SelectValue selProduct = null != searchFormDTO.getProduct() ? searchFormDTO.getProduct() : null;
			SelectValue selBatchValue = null != searchFormDTO.getBatchType() ? searchFormDTO.getBatchType() : null;
			SelectValue selTypeValue = null != searchFormDTO.getType() ? searchFormDTO.getType() : null;
			SelectValue penalDueDays = null != searchFormDTO.getPenalDueDays() ? searchFormDTO.getPenalDueDays() : null;
			
			StringBuffer productCodeWithName;
			String strType = null;
			Long typeId = 0l;			
			String strProduct = null;
			Long productKey = 0L;
			String batchType = null;
			Long paymentCpuCode = 0L;			
			Long zoneCpu = 0L;
			String strClaimType = null;			
			String strClaimant = null;
			String paymentMode = null;
			String cpuSearch = null;
			StringBuilder multiCpu = null;
			Long dueDays = 99L;  //--Default value hardcoded to get all the values
			Long batchTypeStatus = 0L;
			java.sql.Date sqlFrmDate = null;
			java.sql.Date sqlToDate = null;
			SelectValue verificationType = null;
			String strVerificationType= null;
			String paymentcpuSearch = null;
			StringBuilder paymentMultiCpu = null;

			
			
			Map<String, Object> mapValues = new WeakHashMap<String, Object>();	
			List<CreateAndSearchLotTableDTO> finalSearchList = null;
			List<Long> listOfCPUs = new ArrayList<Long>();
			List<Long> listOfPaymentCPUs = new ArrayList<Long>();
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			
			if(null != batchNoList)
			{
				batchNoList.clear();
			}
			
			if(null != selBatchValue && null != selBatchValue.getValue())
			{
				batchType = selBatchValue.getValue();
				if((SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue()))
				{
					batchType = SHAConstants.SEARCH_BATCH_TYPE;
					batchTypeStatus = ReferenceTable.BATCH_CREATED_STATUS;
				}
				else if((SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue())){
					batchTypeStatus = ReferenceTable.LOT_CREATED_STATUS;
				}
				if(presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) && !(SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue())){
					batchTypeStatus = ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH;
				}else if(presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2) && !(SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue())){
					batchTypeStatus = ReferenceTable.PAYMENT_LVL1_TYPE_SEARCH_BATCH;
				}
				
				
				String bancsPaymentFlag = null;
				MastersValue paymentMenuBasedOnMAsterFlag = masterService.getMasterCodeFlag(SHAConstants.MASTER_CODE);
				if(paymentMenuBasedOnMAsterFlag != null && paymentMenuBasedOnMAsterFlag.getMappingCode() != null) {
				bancsPaymentFlag = paymentMenuBasedOnMAsterFlag.getMappingCode();
				}
				if(bancsPaymentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
				if(presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2) && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue())){
					batchTypeStatus = ReferenceTable.PAYMENT_LVL2_TYPE_CREATE_MASTERCODE_Y;
				}
				if(presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue())) {
					batchTypeStatus = ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH;
				}
				}else if(bancsPaymentFlag.equalsIgnoreCase(SHAConstants.N_FLAG)) {
					if(presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2) && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue())){
						batchTypeStatus = ReferenceTable.PAYMENT_LVL2_TYPE_CREATE_MASTERCODE_Y;
					}
					if(presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(selBatchValue.getValue())) {
						batchTypeStatus = ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH;
					}
				}
				
				
				
				
				
			
			}
			if (null != searchFormDTO && null != batchType	&& (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(batchType)) {				
				
				if(null != selTypeValue && null != selTypeValue.getValue())
				{
					strType = selTypeValue.getValue();
					if((SHAConstants.HOLD_FLAG).equalsIgnoreCase(selTypeValue.getValue()))
					{
						strType = SHAConstants.HOLD_FLAG;
					}
					typeId = selTypeValue.getId();
				}
					
				if(presenterString !=null && !presenterString.equals("CREATE_BATCH")){
				  if(null != selpaymentCpuCodeValue && null != selpaymentCpuCodeValue.getValue()){
					   String cpuCodeVal[] = selpaymentCpuCodeValue.getValue().split("-");
					   String strcpuCode = cpuCodeVal[0];
					   if(null != strcpuCode){
						 paymentCpuCode = Long.valueOf(strcpuCode.trim());
					 }
				 }
				}
				
				if(searchFormDTO.getPaymentCpuCodeMulti() != null){
					paymentcpuSearch = searchFormDTO.getPaymentCpuCodeMulti().toString();					
					if(!paymentcpuSearch.equals("[]")){
						paymentMultiCpu = new StringBuilder();
						String temp[] = paymentcpuSearch.split(",");
						listOfPaymentCPUs.clear();
						for (int i = 0; i < temp.length; i++) {
							String valtemp[] = temp[i].split("-");
							String val = valtemp[0].replaceAll("\\[", "");
							listOfPaymentCPUs.add(Long.valueOf(val.trim()));							
							if(temp.length-1 == i){
								paymentMultiCpu = paymentMultiCpu.append(val);
							}
							else
							{
								paymentMultiCpu = paymentMultiCpu.append(val).append(",");
							}
							
						}
						
					}					
				}
				
				if(searchFormDTO.getCpuCodeMulti() != null){
					cpuSearch = searchFormDTO.getCpuCodeMulti().toString();					
					if(!cpuSearch.equals("[]")){
						multiCpu = new StringBuilder();
						String temp[] = cpuSearch.split(",");
						listOfCPUs.clear();
						for (int i = 0; i < temp.length; i++) {
							String valtemp[] = temp[i].split("-");
							String val = valtemp[0].replaceAll("\\[", "");
							listOfCPUs.add(Long.valueOf(val.trim()));							
							if(temp.length-1 == i){
								multiCpu = multiCpu.append(val);
							}
							else
							{
								multiCpu = multiCpu.append(val).append(",");
							}
							
						}
						
					}					
				}
				//commented by noufel for CR GLX2020195 since zone has been set as PAN India by default.			
//				if(null != selZone && null != selZone.getValue() && (SHAConstants.KERALA_ZONE).equalsIgnoreCase(selZone.getValue()))
//				{
//					zoneCpu = ReferenceTable.KERALA_ZONE_CPU_CODE;
//					mapValues.put(SHAConstants.ZONE, SHAConstants.KERALA_ZONE_VALUE);
//				}else
//				{
//					zoneCpu = 0L;
//					mapValues.put(SHAConstants.ZONE, SHAConstants.NON_KERALA_ZONE_VALUE);
//				}
				
				zoneCpu = 0L;
				mapValues.put(SHAConstants.ZONE, SHAConstants.PAN_INDIA);
				if(null != selClaimType && null != selClaimType.getValue())
				{
					strClaimType = selClaimType.getValue();
				}
				
				if(null != claimantType && null != claimantType.getValue())
				{
					strClaimant = claimantType.getValue();
				}
			
				if(null != paymentModeType && null != paymentModeType.getValue())
				{
					paymentMode = paymentModeType.getValue();
					if(SHAConstants.CHEQUE_DD.equalsIgnoreCase(paymentMode))
					{
						paymentMode = SHAConstants.PAYMENT_MODE_CHEQUE;
						mapValues.put(SHAConstants.PAYMENT_TYPE, SHAConstants.PAYMENT_MODE_CHEQUE);
					}else{
						mapValues.put(SHAConstants.PAYMENT_TYPE, SHAConstants.NEFT_TYPE);
					}
				}
				
				if (null != selProduct && null != selProduct.getValue()) {
					strProduct = selProduct.getCommonValue();
					productKey = selProduct.getId();
				}				 
			}
			
			verificationType = searchFormDTO.getVerificationType();
			if (null != verificationType && null != verificationType.getValue()) {
				if(verificationType.getValue().equalsIgnoreCase(ReferenceTable.VERIFICATION_REQUIRED)){
					strVerificationType = "VR";
				}else if(verificationType.getValue().equalsIgnoreCase(ReferenceTable.VERIFICATION_NOT_REQUIRED)){
					strVerificationType = "VNR";
				}
			}
			
			
			if (null != searchFormDTO && null != batchType
					&& (SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(batchType)) {
				
				
				fromDate = null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
				toDate = null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;
				batchNo = null != searchFormDTO.getBatchNo() ? searchFormDTO.getBatchNo() : null;
				selProduct = searchFormDTO.getProduct();
				
				java.util.Date utilFromDate = fromDate;
				java.util.Date utilToDate = toDate;
				if(null != utilFromDate){
					sqlFrmDate = new java.sql.Date(utilFromDate.getTime()); 				    	
				}
				if(null != utilToDate){
					sqlToDate = new java.sql.Date(utilToDate.getTime());
				}
				
				if(null != selClaimType && null != selClaimType.getValue())
				{
					strClaimType = selClaimType.getValue();
				}
				if (null != selProduct && null != selProduct.getValue()) {
					strProduct = selProduct.getCommonValue();
					productKey = selProduct.getId();
				}
					
			}
			
			if(null != penalDueDays && null != penalDueDays.getId()){
				
					if(ReferenceTable.PENAL_EXCEEDS_IRDA_TAT.equals(penalDueDays.getId())){
					dueDays = 1l;
					}
					if(ReferenceTable.PENAL_DUE_TODAY.equals(penalDueDays.getId())){
						dueDays = 0l;
					}
					else if(ReferenceTable.PENAL_DUE_IN_1_DAY.equals(penalDueDays.getId())){
						dueDays = -1l;
					}
					else if(ReferenceTable.PENAL_DUE_IN_2_DAY.equals(penalDueDays.getId())){
						dueDays = -2l;
					}
					else if(ReferenceTable.PENAL_DUE_IN_3_DAY.equals(penalDueDays.getId())){
						dueDays = -3l;
					}
					else if(ReferenceTable.PENAL_DUE_IN_4_DAY.equals(penalDueDays.getId())){
						dueDays = -4l;
					}
					else if(ReferenceTable.PENAL_DUE_IN_5_DAY.equals(penalDueDays.getId())){
						dueDays = -5l;
					 					  
				}
			}
			
			if(null != lotNo && !("").equalsIgnoreCase(lotNo))
			{
				mapValues.put(SHAConstants.LOT_NUMBER_VALUE, lotNo);
			}
			if(null != intimationNo && !("").equalsIgnoreCase(intimationNo))
			{
				mapValues.put(SHAConstants.INTIMATION_NO, intimationNo);
			}
			if(null != listOfCPUs && listOfCPUs.size() > 0)
			{
				String cpuList = null!= multiCpu ? String.valueOf(multiCpu) : null;
				mapValues.put(SHAConstants.CPU_CODE, cpuList);
			}
			if(presenterString !=null && !presenterString.equals("CREATE_BATCH")){
			  if(null != paymentCpuCode){
				mapValues.put(SHAConstants.PAYMENT_CPU_CODE, paymentCpuCode);
			  }	
			}
			if(null != listOfPaymentCPUs && listOfPaymentCPUs.size() > 0)
			{
				String paymentcpuList = null!= paymentMultiCpu ? String.valueOf(paymentMultiCpu) : null;
				mapValues.put(SHAConstants.PAYMENT_CPU_CODE, paymentcpuList);
			}
			
			if(null != strClaimType && !("").equalsIgnoreCase(strClaimType))
			{
				mapValues.put(SHAConstants.CLAIM_TYPE, strClaimType);
			}
						
			if(null != strType && (SHAConstants.CREATE_BATCH_FRESH).equalsIgnoreCase(strType))
			{
				mapValues.put(SHAConstants.HOLD_TYPE, SHAConstants.FRESH);
			}
			else if(null != strType && (SHAConstants.PAYMENT_STATUS_CORRECTION).equalsIgnoreCase(strType))
			{
				mapValues.put(SHAConstants.HOLD_TYPE, SHAConstants.CORRECTION);
			}
			else if(null != strType && (SHAConstants.CREATE_BATCH_HOLD).equalsIgnoreCase(strType))
			{
				mapValues.put(SHAConstants.HOLD_TYPE, SHAConstants.HOLD);
			}
			
			int firtResult;
 			if(pageNumber > 1){ 
				firtResult = (pageNumber-1) *25;
			}else{
				firtResult = 0;
			}
 			
			List<ClaimPayment> claimPaymentList =  new ArrayList<ClaimPayment>(); 
			List<ClaimPayment> claimPaymentList1 =  new ArrayList<ClaimPayment>();
			
			DBCalculationService dbCalculationService = new DBCalculationService();

			if(presenterString !=null && presenterString.equals("CREATE_BATCH")){
				claimPaymentList1 = dbCalculationService.getCreateBatchPaymentDetails(batchTypeStatus, typeId, zoneCpu, paymentMode, 
						(null!= multiCpu ? String.valueOf(multiCpu) : null), intimationNo,/* paymentCpuCode*/(null!= paymentMultiCpu ? String.valueOf(paymentMultiCpu) : null), strClaimType, strClaimant, lotNo, productKey, dueDays,batchNo,sqlFrmDate,sqlToDate,entityManager,strVerificationType);
			}else{
				
				if(presenterString != null && (presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || presenterString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
					fromDate = null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
					toDate = null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;
					java.util.Date utilFromDate = fromDate;
					java.util.Date utilToDate = toDate;
					if(null != utilFromDate){
						sqlFrmDate = new java.sql.Date(utilFromDate.getTime()); 				    	
					}
					if(null != utilToDate){
						sqlToDate = new java.sql.Date(utilToDate.getTime());
					}
				}	
				
				claimPaymentList1 = dbCalculationService.getCreateBatchPaymentDetailsForBancs(batchTypeStatus, typeId, zoneCpu, paymentMode, 
						(null!= multiCpu ? String.valueOf(multiCpu) : null), intimationNo, paymentCpuCode, strClaimType, strClaimant, lotNo, productKey, dueDays,batchNo,sqlFrmDate,sqlToDate,presenterString,entityManager,strVerificationType);
			}

			
			for (ClaimPayment claimPayment : claimPaymentList1) {
				Long hospitalKey = claimPayment.getHospitalKey();
				if(null != hospitalKey){
					MasHospitals hospitalById = getMasHospitalById(hospitalKey,entityManager);
						if(null != hospitalById){
							claimPayment.setHospital(hospitalById);
						}
				}
			}
						
			      int fromIndex = (pageNumber - 1) * 25;
			      int toIndex = Math.min(claimPaymentList1.size(), Math.abs(pageNumber * 25));

			    claimPaymentList = claimPaymentList1.subList(fromIndex, toIndex);
			
			Long totalCount = Long.valueOf(claimPaymentList1.size());
			
			if(totalCount > BPMClientContext.getLotBatchFectchSize()){
				List<CreateAndSearchLotTableDTO> cpucodeList = new ArrayList<CreateAndSearchLotTableDTO>();
				
				Object[] setMapValues = SHAUtils.setMappingValueForBatch(mapValues);
				
				cpucodeList = dbCalculationService.getCpuWiseCountForBatch(setMapValues);	
				
				page = new Page<CreateAndSearchLotTableDTO>();
				page.setPageItems(cpucodeList);
				utx.commit();
				return page;
				
			}			
				
			
			List<CreateAndSearchLotTableDTO> interList = new ArrayList<CreateAndSearchLotTableDTO>();

			if (null != searchFormDTO && null != batchType
					&& (SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(batchType))
			{
				List<CreateAndSearchLotTableDTO> tableDTO = getTableDTOList(claimPaymentList,userName,passWord);
				
				if(null != tableDTO && !tableDTO.isEmpty() && null != batchNoList) 
				{
					finalSearchList = new ArrayList<CreateAndSearchLotTableDTO>();
					for (int i = 0 ; i <batchNoList.size(); i++) {
						String batchNumber = batchNoList.get(i);
						int iClaimCnt = 0;
						if(null != interList)
						{
							interList.clear();
						}
						for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTO) {

							if(createAndSearchLotTableDTO.getCpuCode()!=null){
								TmpCPUCode masCpuCode = null;
								try {
									String cpuCodeVal[] = createAndSearchLotTableDTO.getCpuCode().split("-");

									String strcpuCode = cpuCodeVal[0];
									masCpuCode = getMasCpuCode(Long.parseLong(strcpuCode.trim()));
								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(masCpuCode!=null){
									createAndSearchLotTableDTO.setCpuName(masCpuCode.getDescription());
								}
							}
							if(createAndSearchLotTableDTO.getRodNo() != null){
								Reimbursement reimbursement= getReimbursementbyNO(createAndSearchLotTableDTO.getRodNo());
								if(reimbursement.getClaim() !=null 
										&& reimbursement.getClaim().getLegalClaim() !=null
										&& reimbursement.getClaim().getLegalClaim().equals("Y")){
									createAndSearchLotTableDTO.setLegalPayment(true);
									LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursement.getKey());
									if(taxDeduction !=null){
										LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
										LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
										billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
										createAndSearchLotTableDTO.setLegalBillingDTO(billingDTO);
									}
								}else{
									createAndSearchLotTableDTO.setLegalPayment(false);
								}
							}	
							if(null != batchNumber && batchNumber.equalsIgnoreCase(createAndSearchLotTableDTO.getAccountBatchNo()))
							{
								iClaimCnt++;
								createAndSearchLotTableDTO.setClaimCount(iClaimCnt);
								interList.add(createAndSearchLotTableDTO);
							}		
						}
						if(null != interList && !interList.isEmpty())
						{
							int iSize = interList.size();
							finalSearchList.add(interList.get(iSize-1));
						}
					}
				}
			}
			
			List<CreateAndSearchLotTableDTO> completeTableDTOListObj = setBatchDetailsValues(userName, passWord, claimPaymentList,searchFormDTO);
			List<CreateAndSearchLotTableDTO> completeTableDTOListObj1 = setBatchDetailsValues(userName, passWord, claimPaymentList1,searchFormDTO);
			
			
			page = new Page<CreateAndSearchLotTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if(null != claimPaymentList && claimPaymentList.isEmpty()) {
				page.setHasNext(false);
				searchFormDTO.getPageable().setPageNumber(1);
			}
			else if(null !=claimPaymentList1 && toIndex >= claimPaymentList1.size()){
				page.setHasNext(false);
				page.setPageNumber(pageNumber);
			}
			else{
				page.setPageNumber(pageNumber);
				page.setHasNext(true);
			}
			
			/*if(tableDTO.isEmpty()){
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(tableDTO);*/
			// Added for displaying total no records in create and search batch.-- starts
			if (null != searchFormDTO && null != batchType
					&& (SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(batchType) && null != finalSearchList)
			{
				page.setTotalRecords(finalSearchList.size());
				page.setPageItems(finalSearchList);

			}
			else if(null != claimPaymentList1)
			{
				page.setTotalRecords(claimPaymentList1.size());
			}
			
			if (null != completeTableDTOListObj) {
				
				Collections.sort(completeTableDTOListObj, new Comparator<CreateAndSearchLotTableDTO>(){
					   public int compare(CreateAndSearchLotTableDTO o1, CreateAndSearchLotTableDTO o2){
					      return o2.getNumberofdays() - o1.getNumberofdays();
					   }
					});
				page.setTotalList(completeTableDTOListObj1);
				page.setPageItems(completeTableDTOListObj);
			}
			// Added for displaying total no records in create and search batch.-- ends
			page.setIsDbSearch(true);
			utx.commit();
			return page;			
		}
 		catch (Exception e) {
		
			//return page;
 			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		 
		 
		return page;	
	}
	private List<CreateAndSearchLotTableDTO> setBatchDetailsValues(String userName, String passWord,
			List<ClaimPayment> totalPaymentList,SearchCreateBatchFormDTO searchFormDTO) 
	{
		StringBuffer productCodeWithName;
		
		List<CreateAndSearchLotTableDTO> completeTableDTOList = CreateAndSearchLotMapper.getInstance()
				.getListOfCreateAndSearchLotTableDTO(totalPaymentList);	
		
				
		if (null != completeTableDTOList && !completeTableDTOList.isEmpty()) {
			int i =1;
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : completeTableDTOList) {
				
				DBCalculationService calculationService = new DBCalculationService();
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
				
				if(createAndSearchLotTableDTO.getPayeeNameStr()!=null){
					SelectValue payeeName = new SelectValue();
					payeeName.setId(0l);
					payeeName.setValue(createAndSearchLotTableDTO.getPayeeNameStr());
					createAndSearchLotTableDTO.setTempPayeeName(payeeName);
				}
				createAndSearchLotTableDTO.setUsername(userName);
				createAndSearchLotTableDTO.setPassword(passWord);				

				if(createAndSearchLotTableDTO.getCpuCode()!=null){
					TmpCPUCode masCpuCode = null;
					try {
						String cpuCodeVal[] = createAndSearchLotTableDTO.getCpuCode().split("-");

						String strcpuCode = cpuCodeVal[0];
						masCpuCode = getMasCpuCode(Long.parseLong(strcpuCode.trim()));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(masCpuCode!=null){
						createAndSearchLotTableDTO.setCpuName(masCpuCode.getDescription());
					}
				}
				if(createAndSearchLotTableDTO.getDocumentReceivedFrom()!=null && 
						!createAndSearchLotTableDTO.getDocumentReceivedFrom().equals(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
					createAndSearchLotTableDTO.setIsInsured(Boolean.TRUE);
				}
				if (null != createAndSearchLotTableDTO
						&& (ReferenceTable.PAYMENT_TYPE_CHEQUE)
						.equalsIgnoreCase(createAndSearchLotTableDTO
								.getPaymentTypeValue())) {

					createAndSearchLotTableDTO
							.setPaymentModeFlag(SHAConstants.YES_FLAG);
					
					createAndSearchLotTableDTO.setChangedPaymentMode(ReferenceTable.PAYMENT_TYPE_CHEQUE);
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

				BankMaster cityName = getCityName(createAndSearchLotTableDTO
						.getIfscCode());
				if (null != cityName)

				{
					createAndSearchLotTableDTO.setCity(cityName.getCity());
					createAndSearchLotTableDTO.setPayableAt(cityName
							.getCity());
				}	
				
				productCodeWithName = new StringBuffer();
				if(null != createAndSearchLotTableDTO.getProductName()){
					
					createAndSearchLotTableDTO.setProduct(String.valueOf(productCodeWithName.append(createAndSearchLotTableDTO.getProduct()).append("-").append(null != createAndSearchLotTableDTO.getProductName()?createAndSearchLotTableDTO.getProductName() : "")));
				}
				else
				{
					createAndSearchLotTableDTO.setProduct(String.valueOf(productCodeWithName.append(createAndSearchLotTableDTO.getProduct()).append("").append(null != createAndSearchLotTableDTO.getProductName()?createAndSearchLotTableDTO.getProductName() : "")));
				}
				
				Claim claimObj = new Claim();
				if(null != createAndSearchLotTableDTO.getClaimNo())
				{
				 claimObj = searchByClaimNum(createAndSearchLotTableDTO.getClaimNo());
				}
				
				
				if(claimObj != null)
				{			
					ClaimDto claimDto = ClaimMapper.getInstance().getClaimDto(claimObj);
					createAndSearchLotTableDTO.setClaimDto(claimDto);
				}

				if((null != createAndSearchLotTableDTO && null != createAndSearchLotTableDTO.getSaveFlag() &&
						!(SHAConstants.YES_FLAG).equalsIgnoreCase(createAndSearchLotTableDTO.getSaveFlag()))
						||(null != createAndSearchLotTableDTO && null == createAndSearchLotTableDTO.getSaveFlag())
						|| (null != createAndSearchLotTableDTO && null != createAndSearchLotTableDTO.getSaveFlag() &&
								(SHAConstants.YES_FLAG).equalsIgnoreCase(createAndSearchLotTableDTO.getSaveFlag())
								&& createAndSearchLotTableDTO.getNoofdaysexceeding() != null && createAndSearchLotTableDTO.getNoofdaysexceeding() < 0)){
					
					getPenalInterestCalculation(createAndSearchLotTableDTO, searchFormDTO);
				}
				else
				{
					noOfDelayDaysCalculation(createAndSearchLotTableDTO, searchFormDTO);
				}
				createAndSearchLotTableDTO.setReceiptOfDocumentsDTO(new ReceiptOfDocumentsDTO());
				createAndSearchLotTableDTO.getReceiptOfDocumentsDTO().setSourceRiskID(createAndSearchLotTableDTO.getSourceRskID());
				createAndSearchLotTableDTO.getReceiptOfDocumentsDTO().setPolicyNo(createAndSearchLotTableDTO.getPolicyNo());
				createAndSearchLotTableDTO.getReceiptOfDocumentsDTO().setIntimationNo(createAndSearchLotTableDTO.getIntimationNo());
				if(createAndSearchLotTableDTO.getRodNo() != null){
					Reimbursement reimbursement= getReimbursementbyNO(createAndSearchLotTableDTO.getRodNo());
					if(reimbursement.getClaim() !=null 
							&& reimbursement.getClaim().getLegalClaim() !=null
							&& reimbursement.getClaim().getLegalClaim().equals("Y")){
						createAndSearchLotTableDTO.setLegalPayment(true);
						LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursement.getKey());
						if(taxDeduction !=null){
							LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
							LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
							billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
							createAndSearchLotTableDTO.setLegalBillingDTO(billingDTO);
						}
					}else{
						createAndSearchLotTableDTO.setLegalPayment(false);
					}
					//code added for seting payment party code in create and batch lot screen
					if(reimbursement != null && reimbursement.getClaim() !=null && reimbursement.getClaim().getIntimation() != null && reimbursement.getClaim().getIntimation().getPolicy().getPaymentParty() != null){
						createAndSearchLotTableDTO.setPaymentPartyMode(reimbursement.getClaim().getIntimation().getPolicy().getPaymentParty());
					}
				}	
			}
			
		}
		return completeTableDTOList;
	}
	
	public Map<String , Object> updateBatchNumberForPaymentProcessing(List<CreateAndSearchLotTableDTO> tableDTOList,String typeOfService, String menuString)
	{
		try
		{
		utx.setTransactionTimeout(360000);
		utx.begin();
		Map<String, Object> lotCreationMap = new HashMap<String, Object>();
		String strBatchNo = null;
		int itotalNoOfRecords = 0;
		//int iNoOfRecordsSentToChecker = 0;
		if(null != tableDTOList && !tableDTOList.isEmpty())
		{
			itotalNoOfRecords = tableDTOList.size();
			DBCalculationService dbCalculationService = new DBCalculationService();
			Long batchNo = dbCalculationService.generateSequence(SHAConstants.BATCH_NO_SEQUENCE_NAME);
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTOList) {		
				if(null != createAndSearchLotTableDTO.getCheckBoxStatus() && ("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
				{
					ClaimPayment claimPayment = populateClaimPaymentObject(createAndSearchLotTableDTO);
					//MastersValue paymentStatus = new MastersValue();
					if((SHAConstants.HOLD_PENDING_SERVICE).equalsIgnoreCase(typeOfService))
					{
						claimPayment.setBatchHoldFlag(SHAConstants.YES_FLAG);
						claimPayment.setRemarks(createAndSearchLotTableDTO.getRemarksForHold());
						
						//As per sathis sir, below mentioned condition commented,
						
						/*paymentStatus.setKey(ReferenceTable.PAYMENT_STATUS_HOLD_PENDING);
						claimPayment.setPaymentStatus(paymentStatus);*/
						
						
						/**
						 * status id will be updated only db on reverse feed.
						 * */
						
						Status status = new Status();
						
						status.setKey(ReferenceTable.LOT_CREATED_STATUS);
						if(menuString != null && (menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
							status.setKey(ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH);							
						}
						
						claimPayment.setStatusId(status);
						//Added for painting success layout for hold pending case
						lotCreationMap.put(SHAConstants.HOLD_PENDING_SERVICE, SHAConstants.YES_FLAG);
					}
					else if((SHAConstants.UNHOLD_SERVICE).equalsIgnoreCase(typeOfService))
					{						
						
						claimPayment.setBatchHoldFlag(SHAConstants.N_FLAG);						
						
						/**
						 * status id will be updated only db on reverse feed.
						 * */
						
						Status status = new Status();
						status.setKey(ReferenceTable.LOT_CREATED_STATUS);
						if(menuString != null && (menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
							status.setKey(ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH);							
						}
						claimPayment.setStatusId(status);
						//Added for painting success layout for hold pending case
						lotCreationMap.put(SHAConstants.UNHOLD_SERVICE, SHAConstants.YES_FLAG);
					}
					else if ((SHAConstants.CREATE_BATCH_SERVICE).equalsIgnoreCase(typeOfService))
					{
					
						/**
						 * During course of development, FA approval date was appened before sequence number. During production
						 * issue has been raised and as per sathish suggestion, instead of FA approval date , current system date when
						 * then batch is been created will be appended.
						 * */
						
						strBatchNo = SHAConstants.BATCH_NO_FORMAT+SHAUtils.formatDateForBatchNo(new Timestamp(System.currentTimeMillis()))+"-"+batchNo;
						claimPayment.setBatchNumber(strBatchNo);
						claimPayment.setBatchHoldFlag(SHAConstants.N_FLAG);
						
						/**
						 * The below code is commented, since, the payment status
						 * shouldn't be changed. Watever comes from lot creation
						 * same will be retained. This we had encountered while testing
						 * correction record. For correction record, the payment status
						 * would be correction after lot creation. But during batch,
						 * we override with fresh status. This shouldn't happen and hence commenting the
						 * code. But in case before creating batch, if they hold, the status would change
						 * and again when they create batch, hold status will be retained in payment status. This
						 * is wrong. Had discussed with sathish sir on this. Asked me to hold the same.
						 * */
						
				
						Status batchStatus = new Status();
						batchStatus.setKey(ReferenceTable.BATCH_CREATED_STATUS);
						
						claimPayment.setBatchStatus(batchStatus);
						
						/**
						 * status id will be updated only db on reverse feed.
						 * */
						
						Status status = new Status();
						status.setKey(ReferenceTable.BATCH_CREATED_STATUS);
						claimPayment.setStatusId(status);
						claimPayment.setBatchCreatedDate(new Timestamp(System.currentTimeMillis()));
						
						if(null != createAndSearchLotTableDTO.getLastAckDate())
						{
							claimPayment.setLastAckDate(createAndSearchLotTableDTO.getLastAckDate());
						}
						
						if(null != createAndSearchLotTableDTO.getNumberofdays())
						{
						claimPayment.setDelayDays(Long.valueOf(createAndSearchLotTableDTO.getNumberofdays()));	
						}
						else
						{
							claimPayment.setDelayDays(0l);
						}
						if(null != createAndSearchLotTableDTO.getNoofdaysexceeding())
						{
							claimPayment.setAllowedDelayDays(Long.valueOf(createAndSearchLotTableDTO.getNoofdaysexceeding()));
						}
						else
						{
							claimPayment.setAllowedDelayDays(0l);
						}

						if(null != createAndSearchLotTableDTO.getIntrestRate())
						{
						claimPayment.setInterestRate(createAndSearchLotTableDTO.getIntrestRate());	
						}
						else
						{
							claimPayment.setInterestRate(0d);
						}

						//Double approvedAmnt = 0d;
						//Double intrestAmnt = 0d;
						
						if(null != createAndSearchLotTableDTO.getIntrestAmount())
						{
											
							claimPayment.setInterestAmount(createAndSearchLotTableDTO.getIntrestAmount());
						}
						else
						{
							claimPayment.setInterestAmount(0d);
						}

						if(null !=createAndSearchLotTableDTO.getPenalTotalAmnt())
						{
							
							claimPayment.setTotalAmount(createAndSearchLotTableDTO.getPenalTotalAmnt());
							
						}
						
						if(null != createAndSearchLotTableDTO.getRemarks())
						{
						claimPayment.setIntrestRemarks(createAndSearchLotTableDTO.getRemarks());	
						}
						
						//Added for painting success layout
						lotCreationMap.put(SHAConstants.HOLD_PENDING_SERVICE, SHAConstants.N_FLAG);
					}
					if(null != claimPayment.getKey())
					{
						entityManager.merge(claimPayment);
						entityManager.flush();
						entityManager.clear();
					//	entityManager.refresh(claimPayment);
					}
					//iNoOfRecordsSentToChecker ++;
				}
			}
		}
		lotCreationMap.put(SHAConstants.BATCH_NUMBER, strBatchNo);
		lotCreationMap.put(SHAConstants.TOTAL_NO_LOT_RECORDS, String.valueOf(itotalNoOfRecords));
		
		//lotCreationMap.put()
		utx.commit();
		return lotCreationMap;
		}catch (Exception e) {
		
			//return page;
 			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	private ClaimPayment populateClaimPaymentObject(CreateAndSearchLotTableDTO createAndSearchLotTableDTO )
	{
		ClaimPayment claimPayment = getClaimPayment(createAndSearchLotTableDTO.getClaimPaymentKey());
		
		if(null != claimPayment)
		{
			if(ReferenceTable.PAYMENT_TYPE_CHEQUE.equalsIgnoreCase(claimPayment.getPaymentType()))
			{
				createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.YES_FLAG);
			}
			else
			{
				createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.N_FLAG);
			}
		}		
		
		if(null != claimPayment)
		{
			if(ReferenceTable.PAYMENT_TYPE_CHEQUE.equalsIgnoreCase(claimPayment.getPaymentType()))
			{
				createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.YES_FLAG);
			}
			else
			{
				createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.N_FLAG);
			}
		}
		if(null != createAndSearchLotTableDTO.getPaymentModeFlag() && (SHAConstants.YES_FLAG).equalsIgnoreCase(createAndSearchLotTableDTO.getPaymentModeFlag()))
		{
			claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
			claimPayment.setAccountNumber(null);
			claimPayment.setIfscCode(null);
			claimPayment.setBankCode(null);
			claimPayment.setBankName(null);
			claimPayment.setBranchName(null);
			if(null != createAndSearchLotTableDTO.getPayableAt())
				claimPayment.setPayableAt(createAndSearchLotTableDTO.getPayableAt());
			
		}
		else
		{
			claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
			claimPayment.setPayableAt(null);
			if(null != createAndSearchLotTableDTO.getBeneficiaryAcntNo())
				claimPayment.setAccountNumber(createAndSearchLotTableDTO.getBeneficiaryAcntNo());
				if(null != createAndSearchLotTableDTO.getIfscCode())
				claimPayment.setIfscCode(createAndSearchLotTableDTO.getIfscCode());
				if(null != createAndSearchLotTableDTO.getBranchName())
				claimPayment.setBranchName(createAndSearchLotTableDTO.getBranchName());
				if(null != createAndSearchLotTableDTO.getBankName())
				claimPayment.setBankName(createAndSearchLotTableDTO.getBankName());
		}
		
		if(null != createAndSearchLotTableDTO.getCmbPayeeName())
		{
			claimPayment.setPayeeName(createAndSearchLotTableDTO.getCmbPayeeName().getValue());
		}
		if(null != createAndSearchLotTableDTO.getEmailID())
		claimPayment.setEmailId(createAndSearchLotTableDTO.getEmailID());
		/*
		 * Reason for change to be implemented .
		 * */
		//claimPayment.set
		if(null != createAndSearchLotTableDTO.getPanNo() )
		claimPayment.setPanNumber(createAndSearchLotTableDTO.getPanNo());
		/*
		 * Legal heir first name to be implemented.
		 * */
		if(null != createAndSearchLotTableDTO.getPayableAt())
		claimPayment.setPayableAt(createAndSearchLotTableDTO.getPayableAt());
		if(null != createAndSearchLotTableDTO.getBeneficiaryAcntNo())
		claimPayment.setAccountNumber(createAndSearchLotTableDTO.getBeneficiaryAcntNo());
		if(null != createAndSearchLotTableDTO.getIfscCode())
		claimPayment.setIfscCode(createAndSearchLotTableDTO.getIfscCode());
		if(null != createAndSearchLotTableDTO.getBranchName())
		claimPayment.setBranchName(createAndSearchLotTableDTO.getBranchName());
		if(null != createAndSearchLotTableDTO.getBankName())
		claimPayment.setBankName(createAndSearchLotTableDTO.getBankName());
		if(null != createAndSearchLotTableDTO.getReasonForChange()){
			claimPayment.setReasonForChange(createAndSearchLotTableDTO.getReasonForChange());
		}
		if(null != createAndSearchLotTableDTO.getLegalFirstName()){
			claimPayment.setLegalHeirName(createAndSearchLotTableDTO.getLegalFirstName());
		}
		
		if(null != createAndSearchLotTableDTO.getPayModeChangeReason()){
			claimPayment.setPayModeChangeReason(createAndSearchLotTableDTO.getPayModeChangeReason());
		}
		
		claimPayment.setModifiedBy(createAndSearchLotTableDTO.getUsername());
		claimPayment.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		
		/**
		 * City to be implemented.
		 */
		return claimPayment;
		
	}
	
	
	private BankMaster getCityName(String ifscCode)
	{
		Query query = entityManager.createNamedQuery("BankMaster.findByIfscCode");
		query = query.setParameter("ifscCode", ifscCode);
		List<BankMaster> bankList = query.getResultList();
		if(null != bankList && !bankList.isEmpty())
		{
			return bankList.get(0);
		}
		else
		{
			return null;
		}
		
	}
	
	private OrganaizationUnit getZonalEmailDetails(String pioCode)
	{
		Query query = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId");
		query = query.setParameter("officeCode", pioCode);
		List<OrganaizationUnit> branchList = query.getResultList();
		if(null != branchList && !branchList.isEmpty())
		{
			return branchList.get(0);
		}
		else
		{
			return null;
		}
		
	}
	
	
	public ClaimPayment getClaimPayment(Long key)
	{
		Query query = entityManager.createNamedQuery("ClaimPayment.findByKey");
		query = query.setParameter("primaryKey", key);
		List<ClaimPayment> claimPaymentList = query.getResultList();
		if(null != claimPaymentList && !claimPaymentList.isEmpty())
		{
			for (ClaimPayment claimPayment : claimPaymentList) {
				entityManager.refresh(claimPayment);
			}
			
			return claimPaymentList.get(0);
		}
		else
		{
			return null;
		}
		
	}	
	
	
	public Boolean savePaymentDetails(CreateAndSearchLotTableDTO createAndSearchLotTableDTO)
	{
		try
		{
		utx.setTransactionTimeout(360000);
		utx.begin();
		ClaimPayment claimPayment = getClaimPayment(createAndSearchLotTableDTO.getClaimPaymentKey());
		
		if(createAndSearchLotTableDTO.getPaymentCpuCode()!=null){
			SelectValue selCpuCodeValue = createAndSearchLotTableDTO.getPaymentCpuCode();
			if (null != selCpuCodeValue
					&& null != selCpuCodeValue.getValue()) {
				// cpuCode = selCpuCodeValue.getValue();
				String cpuCodeVal[] = selCpuCodeValue.getValue().split("-");

				String strcpuCode = cpuCodeVal[0];
				if (null != strcpuCode)
				claimPayment.setPaymentCpuCode(Long.parseLong(strcpuCode.trim()));
			}
		}
		if(createAndSearchLotTableDTO.getPaymentCpucodeTextValue()!=null){
			
				String value = createAndSearchLotTableDTO.getPaymentCpucodeTextValue();
				String cpuCodeVal[] = value.split("-");

				String strcpuCode = cpuCodeVal[0];
				if (null != strcpuCode)
				claimPayment.setPaymentCpuCode(Long.parseLong(strcpuCode.trim()));
			
		}
		
		if (null != createAndSearchLotTableDTO.getReasonForChange())
			claimPayment.setReasonForChange(createAndSearchLotTableDTO
					.getReasonForChange());
		
		if(createAndSearchLotTableDTO.getPaymentType()!=null && createAndSearchLotTableDTO.getPaymentType().equals("NEFT")){
			claimPayment.setPaymentType(ReferenceTable.BANK_TRANSFER);
		}else if(createAndSearchLotTableDTO.getPaymentType()!=null && createAndSearchLotTableDTO.getPaymentType().equals("CHEQUE / DD")){
			claimPayment.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
		}
		if (null != createAndSearchLotTableDTO.getPayableAt())
			claimPayment.setPayableAt(createAndSearchLotTableDTO
					.getPayableAt());
		if (null != createAndSearchLotTableDTO.getBeneficiaryAcntNo())
			claimPayment.setAccountNumber(createAndSearchLotTableDTO
					.getBeneficiaryAcntNo());
		if (null != createAndSearchLotTableDTO.getIfscCode())
			claimPayment.setIfscCode(createAndSearchLotTableDTO
					.getIfscCode());
		if (null != createAndSearchLotTableDTO.getBranchName())
			claimPayment.setBranchName(createAndSearchLotTableDTO
					.getBranchName());
		if (null != createAndSearchLotTableDTO.getBankName())
			claimPayment.setBankName(createAndSearchLotTableDTO
					.getBankName());
		
		if (null != createAndSearchLotTableDTO.getPayeeNameStr()) {
			claimPayment.setPayeeName(createAndSearchLotTableDTO
					.getPayeeNameStr());
		}else{
			claimPayment.setPayeeName(null);
		}
		if(null != createAndSearchLotTableDTO.getEmailID())
		claimPayment.setZonalMailId(createAndSearchLotTableDTO.getEmailID());
		
		if(null != createAndSearchLotTableDTO.getReasonForChange())
			claimPayment.setReasonForChange(createAndSearchLotTableDTO.getReasonForChange());
		
		if(null != createAndSearchLotTableDTO.getPayModeChangeReason())
			claimPayment.setPayModeChangeReason(createAndSearchLotTableDTO.getPayModeChangeReason());
		/*
		 * Reason for change to be implemented .
		 * */
		//claimPayment.set
		if(null != createAndSearchLotTableDTO.getPanNo() )
		claimPayment.setPanNumber(createAndSearchLotTableDTO.getPanNo());
		
		if(null != createAndSearchLotTableDTO.getLegalFirstName())
			claimPayment.setLegalHeirName(createAndSearchLotTableDTO.getLegalFirstName());
		
		claimPayment.setVerifiedStatusFlag(createAndSearchLotTableDTO.getVerifiedStatusFlag());
		
		claimPayment.setVerifiedUserId(createAndSearchLotTableDTO.getUserId());

		claimPayment.setVerifiedDate(new Date());
		
		//Added for view trail while holding
		if(claimPayment.getBatchHoldFlag() != null && claimPayment.getBatchHoldFlag().equals(SHAConstants.YES_FLAG)){
			claimPayment.setSaveFlag(SHAConstants.YES_FLAG);
		}
		
		if(null != claimPayment.getKey())
		{
			entityManager.merge(claimPayment);
			entityManager.flush();
			entityManager.clear();
			
		}
		utx.commit();
		
		return true;
		}
		catch(Exception e)
		{
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
		
	}
	//for payment verification level1 or level2 updation
	public void updatePaymentLevel1(CreateAndSearchLotTableDTO tableDTO, String menu){
		try{
		utx.setTransactionTimeout(360000);
		utx.begin();
		String bancsPaymentFlag = null;
		MastersValue paymentMenuBasedOnMAsterFlag = masterService.getMasterCodeFlag(SHAConstants.MASTER_CODE);
		ClaimPayment paymentBancs = getClaimPayment(tableDTO.getClaimPaymentKey());
		Status status = new Status();
		if(paymentMenuBasedOnMAsterFlag != null && paymentMenuBasedOnMAsterFlag.getMappingCode() != null) {
			bancsPaymentFlag = paymentMenuBasedOnMAsterFlag.getMappingCode();
		}
		/*if(bancsPaymentFlag != null && bancsPaymentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
		status.setKey(ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH);
		}else if(bancsPaymentFlag != null && bancsPaymentFlag.equalsIgnoreCase(SHAConstants.N_FLAG)) {
			status.setKey(ReferenceTable.PAYMENT_LVL1_TYPE_SEARCH_BATCH);
		}*/
		
		if(menu != null && menu.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1)) {
			status.setKey(ReferenceTable.PAYMENT_LVL2_TYPE_CREATE_MASTERCODE_Y);
		}else if(menu != null && menu.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)) {
			status.setKey(ReferenceTable.PAYMENT_LVL2_TYPE_SEARCH_BATCH);
		}
		paymentBancs.setStatusId(status);
		if(null != tableDTO.getPanNo()) {
		paymentBancs.setPanNumber(tableDTO.getPanNo()); }
		if(null != tableDTO.getMicrCode()) {
			paymentBancs.setMicrCode(tableDTO.getMicrCode()); }
		if(null != tableDTO.getAccountType()) {
			paymentBancs.setAccountType(tableDTO.getAccountType()); }
		//GALAXYMAIN-13156
		if(null != tableDTO.getNominee()){
			paymentBancs.setNomineeName(tableDTO.getNominee());
		}
		/*if(null != tableDTO.getNameAsPerBankAccount()) {
			paymentBancs.setNomineeName(tableDTO.getNameAsPerBankAccount());}*/
		if(null != tableDTO.getBranchName()) {
			paymentBancs.setBranchName(tableDTO.getBranchName()); }
		if(tableDTO.getRecordCount() != null ){
		paymentBancs.setSelectCount(tableDTO.getRecordCount().toString()); }
		paymentBancs.setEmailId(tableDTO.getEmailID());
		if(tableDTO.getPriorityFlag() != null) {
		paymentBancs.setBancsPriorityFlag(tableDTO.getPriorityFlag().toString());
		}
		if(paymentBancs != null) {
			if(tableDTO.getPaymentType()!=null && tableDTO.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)){
				paymentBancs.setPaymentType(ReferenceTable.BANK_TRANSFER);
			}else if(tableDTO.getPaymentType()!=null && tableDTO.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
				paymentBancs.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
			}
		}
		String vnrFlag = null;
		if(SHAConstants.DOC_RECEIVED_FROM_INSURED.equalsIgnoreCase(tableDTO.getDocReceivedFrom()) && 
				(tableDTO.getPaymentType()!=null && tableDTO.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER))){
			
			if(menu != null && (menu.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) ||  menu.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
				vnrFlag = callPaymentFunDeDupProcedure(tableDTO.getIntimationNo(),tableDTO.getBeneficiaryAcntNo(),tableDTO.getIfscCode(),
						tableDTO.getPayeeNameStr() != null ? tableDTO.getPayeeNameStr().toUpperCase():null,
								  (tableDTO.getNominee() != null ?  tableDTO.getNominee().toUpperCase() : (tableDTO.getLegalHeir() != null ? tableDTO.getLegalHeir().toUpperCase():null)));
			}
			else {
				vnrFlag = callPaymentFunDeDupProcedure(tableDTO.getIntimationNo(),tableDTO.getBeneficiaryAcntNo(),tableDTO.getIfscCode(),
						tableDTO.getPayeeNameStr() != null ? tableDTO.getPayeeNameStr().toUpperCase():null,
								  tableDTO.getLegalFirstName() != null ?tableDTO.getLegalFirstName().toUpperCase():null);
			}
			
			if(null != vnrFlag){
				paymentBancs.setVnrFlag(vnrFlag);
			}
			
		}else{
			paymentBancs.setVnrFlag(SHAConstants.VERIFICATION_NOT_REQUIRED);
		}
		
		if (null != tableDTO.getPayeeNameStr()) {
			paymentBancs.setPayeeName(tableDTO
					.getPayeeNameStr());
		}else{
			paymentBancs.setPayeeName(null);
		}
		
		if(null != tableDTO.getPayeeRelationship()){
			paymentBancs.setPayeeRelationship(tableDTO.getPayeeRelationship());
		}else{
			paymentBancs.setPayeeRelationship(null);
		}
		
		if (null != tableDTO.getPayableAt())
			paymentBancs.setPayableAt(tableDTO
					.getPayableAt());
		if (null != tableDTO.getBeneficiaryAcntNo())
			paymentBancs.setAccountNumber(tableDTO
					.getBeneficiaryAcntNo());
		if (null != tableDTO.getIfscCode())
			paymentBancs.setIfscCode(tableDTO
					.getIfscCode());
		
		if (null != tableDTO.getBankName())
			paymentBancs.setBankName(tableDTO
					.getBankName());
		
		
		if(null != tableDTO.getEmailID())
			paymentBancs.setZonalMailId(tableDTO.getEmailID());
		
		if(null != tableDTO.getReasonForChange())
			paymentBancs.setReasonForChange(tableDTO.getReasonForChange());
		
		if(null != tableDTO.getPayModeChangeReason())
			paymentBancs.setPayModeChangeReason(tableDTO.getPayModeChangeReason());
		
		
		if(null != tableDTO.getLegalFirstName())
			paymentBancs.setLegalHeirName(tableDTO.getLegalFirstName());
		
		if(null != tableDTO.getAccountPreference()){
			paymentBancs.setAccountPreference(tableDTO.getAccountPreference());
		}
		
		if(paymentBancs.getKey()!=null){
            paymentBancs.setModifiedBy(tableDTO.getUsername());
            paymentBancs.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		}
		
		entityManager.merge(paymentBancs);
		entityManager.flush();
		entityManager.clear();
		utx.commit();
		}catch (Exception e) {
		
			//return page;
 			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
	
	public String callPaymentFunDeDupProcedure(String intimationNo, String accountNo,String ifscCode, String payeeName,
			String legalHierName) {
		
		Query cs = entityManager.createNativeQuery(
				"select FUN_PAYMENT_DE_DUPE (?1,?2,?3,?4,?5) from dual");
		cs.setParameter(1, intimationNo);
		cs.setParameter(2, accountNo);
		cs.setParameter(3, ifscCode);
		cs.setParameter(4, payeeName);
		cs.setParameter(5, legalHierName);
		String result = (String) cs.getSingleResult();
		return result;
	}
	public void updateHold(String remarks,CreateAndSearchLotTableDTO tableDTO){
		try{
		utx.setTransactionTimeout(360000);
		utx.begin();
		ClaimPayment paymentBancs = getClaimPayment(tableDTO.getClaimPaymentKey());
		paymentBancs.setSelectCount(tableDTO.getRecordCount().toString());
		paymentBancs.setEmailId(tableDTO.getEmailID());
		if(tableDTO.getPriorityFlag() != null){
		paymentBancs.setBancsPriorityFlag(tableDTO.getPriorityFlag().toString());
		}
		paymentBancs.setBatchHoldFlag(SHAConstants.YES_FLAG);
		paymentBancs.setMicrCode(tableDTO.getMicrCode());
		paymentBancs.setBranchName(tableDTO.getBranchName());
		paymentBancs.setNomineeName(tableDTO.getNameAsPerBankAccount());
		paymentBancs.setIfscCode(tableDTO.getIfscCode());
		paymentBancs.setVirtualPaymentAddr(tableDTO.getVirtualPaymentProcess());
		paymentBancs.setRemarks(remarks);
		paymentBancs.setPanNumber(tableDTO.getPanNo());
		if(paymentBancs != null) {
			if(tableDTO.getPaymentType()!=null && tableDTO.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_BANK_TRANSFER)){
				paymentBancs.setPaymentType(ReferenceTable.BANK_TRANSFER);
			}else if(tableDTO.getPaymentType()!=null && tableDTO.getPaymentType().getId().equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
				paymentBancs.setPaymentType(ReferenceTable.PAYMENT_TYPE_CHEQUE);
			}
		}
		entityManager.merge(paymentBancs);
		entityManager.flush();
		entityManager.clear();
		utx.commit();
		}catch (Exception e) {
		
			//return page;
 			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
	
	
	public List<CreateAndSearchLotTableDTO> getBatchDetails(CreateAndSearchLotTableDTO tableDTO)
	{
		List<CreateAndSearchLotTableDTO> tableDTOList = null;
		List<CreateAndSearchLotTableDTO> finalList =  new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != tableDTO)
		{
			String userName = tableDTO.getUsername();
			String passWord = tableDTO.getPassword();
			Query query = entityManager.createNamedQuery("ClaimPayment.findByBatchNo");
			query = query.setParameter("batchNumber", tableDTO.getAccountBatchNo());		
			List<ClaimPayment> claimPaymentList = query.getResultList();
			if(null != claimPaymentList && !claimPaymentList.isEmpty())
			{			
				tableDTOList = getTableDTOList(claimPaymentList,userName, passWord);
				int rowCount = 1;
				for (CreateAndSearchLotTableDTO createLotTableDto : tableDTOList) {
					
					createLotTableDto.setSerialNumber(rowCount);
					rowCount++;
					finalList.add(createLotTableDto);
					
				}
			}
		}
		return finalList;
	}
	
	
	public List<CreateAndSearchLotTableDTO> getBatchDetailsList(String batchNo)
	{
		
			Query query = entityManager.createNamedQuery("ClaimPayment.findByBatchNo");
			query = query.setParameter("batchNumber", batchNo);		
			List<ClaimPayment> claimPaymentList = query.getResultList();
			if(null != claimPaymentList && !claimPaymentList.isEmpty())
			{		
				Double cashlessTotalAppAmt = 0d;
				Double reimbTotalAppAmt = 0d;
				Double totalCashlessPenalIntAmnt = 0d;
				Double totalReimbPenalIntAmnt = 0d;
				Integer cashlessClaimCount = 0;
				Integer reimbClaimCount = 0;
				List<CreateAndSearchLotTableDTO> finalList = new ArrayList<CreateAndSearchLotTableDTO>();
				
				for (ClaimPayment claimPayment : claimPaymentList) {
					
					if(SHAConstants.CASHLESS_CLAIM_TYPE.equalsIgnoreCase(claimPayment.getClaimType())){
						
						cashlessTotalAppAmt += claimPayment.getTotalApprovedAmount();						
						totalCashlessPenalIntAmnt += claimPayment.getInterestAmount();
						cashlessClaimCount ++;
					}
					
					else if(SHAConstants.REIMBURSEMENT_CLAIM_TYPE.equalsIgnoreCase(claimPayment.getClaimType())){
						
						reimbTotalAppAmt += claimPayment.getTotalApprovedAmount();						
						totalReimbPenalIntAmnt += claimPayment.getInterestAmount();
						reimbClaimCount ++;
					}
				}
				if(cashlessClaimCount>0){
					
					CreateAndSearchLotTableDTO cashlessDTO = new CreateAndSearchLotTableDTO();
					cashlessDTO.setClaimType(SHAConstants.CASHLESS_CLAIM_TYPE);
					cashlessDTO.setClaimCount(cashlessClaimCount);
					cashlessDTO.setApprovedAmt(cashlessTotalAppAmt);
					cashlessDTO.setIntrestAmount(totalCashlessPenalIntAmnt);
					Double sumOfApproveIntAmnt = cashlessDTO.getApprovedAmt() + cashlessDTO.getIntrestAmount();
					cashlessDTO.setSumOfApproveIntAmnt(sumOfApproveIntAmnt);
					
					finalList.add(cashlessDTO);
				}
				
				if(reimbClaimCount>0){
					
					CreateAndSearchLotTableDTO reimbDTO = new CreateAndSearchLotTableDTO();
					reimbDTO.setClaimType(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
					reimbDTO.setClaimCount(reimbClaimCount);
					reimbDTO.setApprovedAmt(reimbTotalAppAmt);
					reimbDTO.setIntrestAmount(totalReimbPenalIntAmnt);
					Double sumOfApproveIntAmnt = reimbDTO.getApprovedAmt() + reimbDTO.getIntrestAmount();
					reimbDTO.setSumOfApproveIntAmnt(sumOfApproveIntAmnt);
					
					finalList.add(reimbDTO);
				
			}
				return finalList;	
		}
			return null;
	}
	
	private List<CreateAndSearchLotTableDTO> getTableDTOList(List<ClaimPayment> claimPaymentList,String userName, String passWord)
	{
		List<CreateAndSearchLotTableDTO> tableDTO = CreateAndSearchLotMapper.getInstance().getListOfCreateAndSearchLotTableDTO(claimPaymentList);
		
		
		List<String> listOfHospCode1 = new ArrayList<String>();
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTO) {
			
			if(createAndSearchLotTableDTO.getHospitalCode() != null){
				listOfHospCode1.add(createAndSearchLotTableDTO.getHospitalCode());
			}
			
			
		}		
		
		if(null != tableDTO && !tableDTO.isEmpty())
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDTO) {
				
				if(null != batchNoList && !batchNoList.contains(createAndSearchLotTableDTO.getAccountBatchNo()))
				{
					batchNoList.add(createAndSearchLotTableDTO.getAccountBatchNo());
				}
				
				createAndSearchLotTableDTO.setUsername(userName);
				createAndSearchLotTableDTO.setPassword(passWord);	
				Policy policyDetails = getPolicyByPolicyNubember(createAndSearchLotTableDTO.getPolicyNo());
				if (policyDetails !=null && policyDetails.getPaymentParty() != null && !policyDetails.getPaymentParty().isEmpty()){
					createAndSearchLotTableDTO.setPaymentPolicyType(policyDetails.getGmcPolicyType());
				}
				
				if(null != createAndSearchLotTableDTO && (ReferenceTable.PAYMENT_TYPE_CHEQUE).equalsIgnoreCase(createAndSearchLotTableDTO.getPaymentTypeValue()))
				{
					createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.YES_FLAG);
					createAndSearchLotTableDTO.setChangedPaymentMode(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					SelectValue paymentType = new SelectValue();
					paymentType.setId(1l);
					paymentType.setValue(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					createAndSearchLotTableDTO.setPaymentType(paymentType);
				}
				else
				{
					createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.N_FLAG);
					createAndSearchLotTableDTO.setChangedPaymentMode(SHAConstants.NEFT_TYPE);
					SelectValue paymentType = new SelectValue();
					paymentType.setId(0l);
					paymentType.setValue(SHAConstants.NEFT_TYPE);
					createAndSearchLotTableDTO.setPaymentType(paymentType);
				}
				
			
				BankMaster cityName = getCityName(createAndSearchLotTableDTO.getIfscCode());
				if(null != cityName)
				
				{
					createAndSearchLotTableDTO.setCity(cityName.getCity());
					createAndSearchLotTableDTO.setPayableAt(cityName.getCity());
					createAndSearchLotTableDTO.setBankName(cityName.getBankName());
					

				}
				if(createAndSearchLotTableDTO.getPayeeNameStr()!=null){
					SelectValue payeeName = new SelectValue();
					payeeName.setId(0l);
					payeeName.setValue(createAndSearchLotTableDTO.getPayeeNameStr());
					createAndSearchLotTableDTO.setTempPayeeName(payeeName);
					createAndSearchLotTableDTO.setPayeeName(payeeName);
					
				}
				if(createAndSearchLotTableDTO.getPaymentCpuString()!=null){
					SelectValue paymentCpu = new SelectValue();
					paymentCpu.setId(0l);
					paymentCpu.setValue(createAndSearchLotTableDTO.getPaymentCpuString());
					createAndSearchLotTableDTO.setPaymentCpuCode(paymentCpu);
					if(createAndSearchLotTableDTO.getPaymentCpuString() != null){
						TmpCPUCode masCpuCode = getMasCpuCode(Long.valueOf(createAndSearchLotTableDTO.getPaymentCpuString()));
						createAndSearchLotTableDTO.setPaymentCpuCodeName(masCpuCode != null ? masCpuCode.getDescription() : "");
					}
				}
				
				createAndSearchLotTableDTO.setServiceTax(0d);
				Double total = 0d;
				if(null != createAndSearchLotTableDTO.getApprovedAmt()){
					total = createAndSearchLotTableDTO.getApprovedAmt() + createAndSearchLotTableDTO.getServiceTax();
				}
				if(null != total)
				{
				createAndSearchLotTableDTO.setSumOfApprovedAndServiceTax(total);
				}
				
				Boolean isFvrOrInvesInitiated = isFvrOrInvesInitiated(createAndSearchLotTableDTO);
				
				createAndSearchLotTableDTO.setIsFvrOrInvesInitiated(isFvrOrInvesInitiated);
				
				BPMClientContext obj = new BPMClientContext();
				
				if(isFvrOrInvesInitiated){		
					if(obj.getIRDAForFvrAndInvs()!=null){
						createAndSearchLotTableDTO.setIrdaTAT(Integer.valueOf(obj.getIRDAForFvrAndInvs()));
					}
				}
				else
				{
					if(obj.getIRDATATDays()!=null){
						createAndSearchLotTableDTO.setIrdaTAT(Integer.valueOf(obj.getIRDATATDays()));
					}
				}
				
				
			}
		}
		return tableDTO;
	}
	
	
	public Reimbursement getReimbursementObject(String rodNo)
	{
		Query query = entityManager.createNamedQuery("Reimbursement.findRodByNumber");
		query = query.setParameter("rodNumber", rodNo);
		List<Reimbursement> reimbursementObjectList = query.getResultList();
		if(null != reimbursementObjectList && !reimbursementObjectList.isEmpty())
		{
			entityManager.refresh(reimbursementObjectList.get(0));
			return reimbursementObjectList.get(0);
		}
		return null;
	}

	
	public DocAcknowledgement getDocAcknowledgeBasedOnROD(Long rodKey)
	{
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findByRODKey");
		query = query.setParameter("rodKey", rodKey);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
			
				entityManager.refresh(docAckList.get(0));
				return docAckList.get(0);
		}
		
		return null;
	}
	
	public Intimation getIntimationKey(String intimationNo)
	{
		Query query = entityManager.createNamedQuery("Intimation.findByIntimationNumber");
		query = query.setParameter("intimationNo", intimationNo);
		List<Intimation> intimationList = query.getResultList();
		if(null != intimationList && !intimationList.isEmpty())
		{
			
				entityManager.refresh(intimationList.get(0));
				return intimationList.get(0);
		}
		
		return null;
	}
	
	public Claim getclaimDetails(Long intimationKey)
	{
		Query query = entityManager.createNamedQuery("Claim.findByIntimationKey");
		query = query.setParameter("intimationKey", intimationKey);
		List<Claim> claimList = query.getResultList();
		if(null != claimList && !claimList.isEmpty())
		{
			
				entityManager.refresh(claimList.get(0));
				return claimList.get(0);
		}
		
		return null;
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

	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	private void getPenalInterestCalculation(CreateAndSearchLotTableDTO createAndSearchLotTableDTO,SearchCreateBatchFormDTO searchFormDTO){
		
		
		noOfDelayDaysCalculation(createAndSearchLotTableDTO, searchFormDTO);
		Integer noOfDaysExceeding = createAndSearchLotTableDTO.getNumberofdays() > createAndSearchLotTableDTO.getIrdaTAT()? createAndSearchLotTableDTO.getNumberofdays() : 0;
		
		//Integer noOfDaysExceeding = createAndSearchLotTableDTO.getNoofdaysexceeding();
		createAndSearchLotTableDTO.setNoofdaysexceeding(noOfDaysExceeding);
		createAndSearchLotTableDTO.setNoOfDaysExceedingforCalculation(noOfDaysExceeding);
		
		Double  faApprovedAmnt = null != createAndSearchLotTableDTO.getFaApprovedAmnt() ? createAndSearchLotTableDTO.getFaApprovedAmnt() : 0d;
		Double interestRate = 0d;
		
		String legalFlag =  createAndSearchLotTableDTO.getClaimDto() != null ? null != createAndSearchLotTableDTO.getClaimDto().getLegalFlag() ? createAndSearchLotTableDTO.getClaimDto().getLegalFlag() : null : null;
		String docReceivedFrom = null != createAndSearchLotTableDTO.getDocReceivedFrom() ? createAndSearchLotTableDTO.getDocReceivedFrom() : null;
		if(/*(createAndSearchLotTableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) ||*/
				(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL.equalsIgnoreCase(docReceivedFrom)) ||
				createAndSearchLotTableDTO.getNoofdaysexceeding() <-3 || 
				(null != legalFlag &&(legalFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) || 
						legalFlag.equalsIgnoreCase(SHAConstants.N_FLAG))))
		{
			interestRate = 0d;
		}
		else
		{
		interestRate = null != createAndSearchLotTableDTO.getIntrestRate() ? createAndSearchLotTableDTO.getIntrestRate() : 0d;
		}
		Double noOfExceedingDays1 = null != createAndSearchLotTableDTO.getNoofdaysexceeding() ?  createAndSearchLotTableDTO.getNoofdaysexceeding() : 0d;				
	
		
		Double noOfExceedingDays = Math.abs(noOfExceedingDays1/365);					
	
		
		Double penalInterestAmount = faApprovedAmnt*(interestRate/100)*(noOfExceedingDays);
		Double approvedAmnt = 0d;
		//Double intrestAmnt = 0d;
		if(null != penalInterestAmount)
		{
			
			double decimalNo =  penalInterestAmount*10 % 10;
			int converttoInt = (int) decimalNo;
			
			if(converttoInt >= 5)
			{
				approvedAmnt =  Math.ceil(penalInterestAmount);
			}
			else
			{
				approvedAmnt =Math.floor(penalInterestAmount);	
			}
			
		createAndSearchLotTableDTO.setIntrestAmount(Math.abs(approvedAmnt));
		createAndSearchLotTableDTO.setInterestAmntForCalculation(Math.abs(approvedAmnt));
		
		}
		/*else
		{
			createAndSearchLotTableDTO.setIntrestAmount(0d);
			createAndSearchLotTableDTO.setInterestAmntForCalculation(0d);
		}*/
		
		if(createAndSearchLotTableDTO.getNoOfDaysExceedingforCalculation() <=0)
		{
			createAndSearchLotTableDTO.setIntrestAmount(0d);
		}
		Policy policyDetails = getPolicyByPolicyNubember(createAndSearchLotTableDTO.getPolicyNo());
		if (policyDetails !=null && policyDetails.getPaymentParty() != null && !policyDetails.getPaymentParty().isEmpty()){
			createAndSearchLotTableDTO.setPaymentPolicyType(policyDetails.getGmcPolicyType());
		}
		
		Double penalTotalamnt = faApprovedAmnt+createAndSearchLotTableDTO.getIntrestAmount();
		
		double decimalOfApproveAmnt =  penalTotalamnt*10 % 10;
		int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;	
		
		if(converttoIntOfApproveAmnt >= 5)
		{
			createAndSearchLotTableDTO.setPenalTotalAmnt(Math.ceil(penalTotalamnt));
		}
		
		else
		{
			createAndSearchLotTableDTO.setPenalTotalAmnt(Math.floor(penalTotalamnt));
		}
	}
	private void noOfDelayDaysCalculation(
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO,
			SearchCreateBatchFormDTO searchFormDTO) {
		long dateDiff = 0l;
		if(null != createAndSearchLotTableDTO.getLastAckDate())
		{
				
		//long lastAckTime = createAndSearchLotTableDTO.getLastAckDate().getTime();
	   
	      Calendar faDate = Calendar.getInstance();
	      faDate.setTime(createAndSearchLotTableDTO.getFaApprovedDate());
	      String irdaPlusDays = BPMClientContext.getFAPlusDays();
	      if(irdaPlusDays!=null){
	    	  faDate.add(Calendar.DATE, Integer.valueOf(irdaPlusDays));
	      }
	      Date faApproveDate = faDate.getTime();
	      
	      if(null != faApproveDate)
	      {
	    	  createAndSearchLotTableDTO.setNextDayOfFaApprovedDate(faApproveDate);
	      }
	      
	      faDate.setTimeInMillis(faApproveDate.getTime());
	      dateDiff = SHAUtils.getDaysBetweenDate(faApproveDate,createAndSearchLotTableDTO.getLastAckDate());
		}
	      
	 	
	     Integer intDatediff = (int) (long) dateDiff;	
	     if(null != intDatediff)
	     {
	     
	      createAndSearchLotTableDTO.setNumberofdays(Math.abs(intDatediff));
	     }		      
	     
		createAndSearchLotTableDTO.setIntrestRate(searchFormDTO.getPenalInterest());
		
		Boolean isFvrOrInvesInitiated = isFvrOrInvesInitiated(createAndSearchLotTableDTO);
		
		createAndSearchLotTableDTO.setIsFvrOrInvesInitiated(isFvrOrInvesInitiated);
		
		BPMClientContext obj = new BPMClientContext();
		
		if(isFvrOrInvesInitiated){		
			if(obj.getIRDAForFvrAndInvs()!=null){
				createAndSearchLotTableDTO.setIrdaTAT(Integer.valueOf(obj.getIRDAForFvrAndInvs()));
			}
		}
		else
		{
			if(obj.getIRDATATDays()!=null){
				createAndSearchLotTableDTO.setIrdaTAT(Integer.valueOf(obj.getIRDATATDays()));
			}else{
				createAndSearchLotTableDTO.setIrdaTAT(0);
			}
		}	
		
	}
	
	
	
	public Boolean isFvrOrInvesInitiated(CreateAndSearchLotTableDTO createAndSearchLotTableDTO){
		
		Boolean isFvrInvesInitiated = Boolean.FALSE; 	
		
		Query findClaim = entityManager.createNamedQuery(
				"Claim.findByClaimNumber").setParameter("claimNumber",
						createAndSearchLotTableDTO.getClaimNo());		
		   List<Claim> claimList = findClaim.getResultList();
		   if(null != claimList && !claimList.isEmpty()){
			   
			   Claim claimObj = claimList.get(0);
			   
		   if(null != claimObj ){		
		
		Query findFvr = entityManager.createNamedQuery(
				"FieldVisitRequest.findByClaimKeyAndStage").setParameter("claimKey",
						claimObj.getKey());			
		List<Long> stageList = new ArrayList<Long>();
		stageList.add(ReferenceTable.PREAUTH_STAGE);
		stageList.add(ReferenceTable.ENHANCEMENT_STAGE);
		stageList.add(ReferenceTable.CLAIM_REGISTRATION_STAGE);
		findFvr.setParameter("stageList", stageList);
		
		   List<FieldVisitRequest> fvrList = findFvr.getResultList();
		   
			Query findInves = entityManager.createNamedQuery(
					"Investigation.findByClaimKey").setParameter("claimKey",
							claimObj.getKey());
			 List<Investigation> invesList = findInves.getResultList();
		   
		   if(null != fvrList && !fvrList.isEmpty()){ 			 				   
			   
			   for (FieldVisitRequest fieldVisit : fvrList) {
				
				   if(!(ReferenceTable.FVRCANCELLED.equals(fieldVisit.getStatus().getKey())) && !(ReferenceTable.SKIPFVR.equals(fieldVisit.getStatus().getKey()))){ 
					   
					   isFvrInvesInitiated = Boolean.TRUE;	
					   break;
				   }	
			}		   
			  		 		
			}
		   
		   if(!isFvrInvesInitiated){
			   
		    if(null != invesList && !invesList.isEmpty()){
			   
			  for (Investigation investigation : invesList) {
				  
				  if( !(ReferenceTable.INITIATE_INVESTIGATION_DIS_APPROVED.equals(investigation.getStatus().getKey()))){
					   
					   isFvrInvesInitiated = Boolean.TRUE;	
					   break;
				   }
			}
			   
			   
		   } 
		 }
		   }
	}
		return isFvrInvesInitiated;
		
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
	
	private BeanItemContainer<SelectValue> getValuesForNameDropDown(Intimation intimation, CreateAndSearchLotTableDTO createAndSearchLotTableDTO)
	{
		
		Policy policy = getPolicyByPolicyNubember(intimation.getPolicy().getPolicyNumber());
		if(null != policy)
		{
		String proposerName =  policy.getProposerFirstName();
		List<Insured> insuredList = policy.getInsured();
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		List<SelectValue> payeeValueList = new ArrayList<SelectValue>();
		
		SelectValue selectValue= null;
		for (int i = 0; i < insuredList.size(); i++) {
			
			Insured insured = insuredList.get(i);
			selectValue = new SelectValue();
			//SelectValue payeeValue = new SelectValue();
			selectValue.setId(Long.valueOf(String.valueOf(i)));
			selectValue.setValue(insured.getInsuredName());
			
			//payeeValue.setId(Long.valueOf(String.valueOf(i)));
			//payeeValue.setValue(insured.getInsuredName());
			
			selectValueList.add(selectValue);
			//payeeValueList.add(payeeValue);
		}
		payeeValueList.addAll(selectValueList);
		
		for (int i = 0; i < insuredList.size(); i++) {
			Insured insured = insuredList.get(i);
			List<NomineeDetails> nomineeDetails = getNomineeDetails(insured.getKey());
			for (NomineeDetails nomineeDetails2 : nomineeDetails) {
				selectValue = new SelectValue();
				selectValue.setId(nomineeDetails2.getKey());
				selectValue.setValue(nomineeDetails2.getNomineeName());
				payeeValueList.add(selectValue);
				selectValue = null;
			}
			
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		
		SelectValue payeeSelValue = new SelectValue();
		int iSize = payeeValueList.size() +1;
		payeeSelValue.setId(Long.valueOf(String.valueOf(iSize)));
		payeeSelValue.setValue(proposerName);
		
		payeeValueList.add(payeeSelValue);
		
		if(createAndSearchLotTableDTO.getDocumentReceivedFrom()!=null && createAndSearchLotTableDTO.getDocumentReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
			Hospitals hosptial = getHospitalById(intimation.getHospital());
			
			if(hosptial != null && hosptial.getPayableAt() != null){
				SelectValue hospitalName = new SelectValue();
				hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
				hospitalName.setValue(hosptial.getPayableAt());
				payeeValueList.add(hospitalName);
				createAndSearchLotTableDTO.setHospitalPayableAt(hosptial.getPayableAt());
			}
			else if(hosptial != null && hosptial.getName() != null){
				SelectValue hospitalName = new SelectValue();
				hospitalName.setId(Long.valueOf(payeeValueList.size()+1));
				hospitalName.setValue(hosptial.getName());
				payeeValueList.add(hospitalName);
				createAndSearchLotTableDTO.setHospitalPayableName(hosptial.getName());
			}
		}
		
		
		BeanItemContainer<SelectValue> insuredPatientNameValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		insuredPatientNameValueContainer.addAll(payeeValueList);
		DBCalculationService dbCalculationService = new DBCalculationService();
		BeanItemContainer<SelectValue> payeeNameValueContainer = dbCalculationService.getPayeeName(intimation.getPolicy().getKey(),
				intimation.getKey());
		
		if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){	
			
			return payeeNameValueContainer;
		}
		else
		{
			return insuredPatientNameValueContainer;
		}
		
		}else{
			return null;
		}
	}
	
	public List<NomineeDetails> getNomineeDetails(Long insuredKey){
		
		Query query = entityManager.createNamedQuery("NomineeDetails.findByInsuredKey");
		query = query.setParameter("insuredKey", insuredKey);
		
		List<NomineeDetails> nomineeDetails = (List<NomineeDetails>) query.getResultList();
		return nomineeDetails;
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Deprecated
	public Policy getPolicy(String policyNumber) {
		Query query = entityManager.createNamedQuery("Policy.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);
		List<Policy> policyList = query.getResultList();
		for (Policy policy : policyList) {
			entityManager.refresh(policy);
		}
		Policy policy = null;
		if(policyList != null && !policyList.isEmpty())
		{
			/**
			 * Will IMS_CLS_POLICY will hold multiple
			 * entries for a single policy number? ---
			 * Needs to clarified with DBA team.
			 * */
			for (Policy resultPolicy : policyList) {
				policy = resultPolicy;
				break;
			}
			if(policy != null){
				List<Insured> insuredList = getInsuredList(policyNumber,entityManager);
				policy.setInsured(insuredList);
			}
		}
		return policy;
	}
	
	public Hospitals getHospitalById(Long key){
		
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);
		
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		
		return null;
		
	}

public List<Insured> getInsuredList(String policyNumber,EntityManager entityManager){
	InsuredService insuredService = new InsuredService();
	List<Insured> insuredList = insuredService.getInsuredListByPolicyNo(policyNumber, entityManager);
	return insuredList;
}

public  Page<CreateAndSearchLotTableDTO> QuickSearch(
		SearchCreateBatchFormDTO searchFormDTO,
		String userName, String passWord) {
	Page<CreateAndSearchLotTableDTO> page = null;
	
	try
	{	

		
		String intimationNo = null ;
		StringBuffer productCodeWithName;
		
		Long quickSrchStatus = ReferenceTable.LOT_CREATED_STATUS;
		
		intimationNo = searchFormDTO.getQuickIntimationNo();
		
		List<CreateAndSearchLotTableDTO> finalSearchList = null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<ClaimPayment> criteriaQuery = criteriaBuilder.createQuery(ClaimPayment.class);
		
		Root<ClaimPayment> root = criteriaQuery.from(ClaimPayment.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		
		
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		
		if(null != intimationNo && !("").equalsIgnoreCase(intimationNo))
		{
			Predicate condition2 = criteriaBuilder.like(root.<String>get("intimationNumber"), "%"+intimationNo+"%");
			conditionList.add(condition2);
			pageNumber = 1;
		}
		
		if(searchFormDTO.getMenuString() != null && ("PAYMENT_VERIFICATION_L1").equalsIgnoreCase(searchFormDTO.getMenuString()))
		{
			quickSrchStatus = ReferenceTable.PAYMENT_LVL1_TYPE_CREATE_BATCH;
			
			Predicate condition8 = criteriaBuilder.equal(root.<Status>get("statusId").<Long>get("key"), quickSrchStatus);
			conditionList.add(condition8);
		}		
		else if(searchFormDTO.getMenuString() != null && ("PAYMENT_VERIFICATION_L2").equalsIgnoreCase(searchFormDTO.getMenuString()))
		{
			quickSrchStatus = ReferenceTable.PAYMENT_LVL2_TYPE_CREATE_MASTERCODE_Y;
			Predicate condition8 = criteriaBuilder.equal(root.<Status>get("statusId").<Long>get("key"), quickSrchStatus);
			conditionList.add(condition8);
		}
		else {
			Predicate condition8 = criteriaBuilder.equal(root.<Status>get("lotStatus").<Long>get("key"), quickSrchStatus);
			conditionList.add(condition8);
		}
		
		Predicate condition9 = criteriaBuilder.equal(root.<String>get("batchHoldFlag"),SHAConstants.N_FLAG);
		conditionList.add(condition9);
		
		if(!(searchFormDTO.getMenuString() != null && (("PAYMENT_VERIFICATION_L1").equalsIgnoreCase(searchFormDTO.getMenuString())
				|| ("PAYMENT_VERIFICATION_L2").equalsIgnoreCase(searchFormDTO.getMenuString())))) {
			
			Predicate condition10 = criteriaBuilder.isNull(root.<String>get("batchNumber"));
			conditionList.add(condition10);
		}	
		
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		final TypedQuery<ClaimPayment> typedQuery = entityManager.createQuery(criteriaQuery);
		
		
		
		int firtResult;
			if(pageNumber > 1){
			firtResult = (pageNumber-1) *25;
		}else{
			firtResult = 0;
		}
		
		List<ClaimPayment> totalPaymentList = new ArrayList<ClaimPayment>();
	

		totalPaymentList = typedQuery.setFirstResult(firtResult).setMaxResults(25).getResultList();

		List result = new ArrayList<ClaimPayment>();
		
		if(totalPaymentList.size()>=25){
			 result = typedQuery.setFirstResult(totalPaymentList.size()+1).setMaxResults(1).getResultList();
			}
	
		List<CreateAndSearchLotTableDTO> tableDTO = getTableDTOList(totalPaymentList,userName,passWord);

		List<CreateAndSearchLotTableDTO> completeTableDTOList = CreateAndSearchLotMapper.getInstance()
				.getListOfCreateAndSearchLotTableDTO(totalPaymentList);	
		
				
		if (null != completeTableDTOList && !completeTableDTOList.isEmpty()) {
			int i =1;
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : completeTableDTOList) {
				
				DBCalculationService calculationService = new DBCalculationService();
				//IMSSUPPOR-26406
				/*if(createAndSearchLotTableDTO.getIntimationNo()!=null){
					Intimation intimationByNo = getIntimationByNo(createAndSearchLotTableDTO.getIntimationNo());
					if(intimationByNo!=null){
							BeanItemContainer<SelectValue> gmcEmployeeName = getValuesForNameDropDown(intimationByNo,createAndSearchLotTableDTO);
							if (gmcEmployeeName != null) {
								
								for(int value2 = 0 ; value2<gmcEmployeeName.size() ; value2++)
							 	{
									if(createAndSearchLotTableDTO.getPayeeNameStr()!=null && createAndSearchLotTableDTO.getPayeeNameStr().equalsIgnoreCase(gmcEmployeeName.getIdByIndex(value2).getValue())){
										createAndSearchLotTableDTO.setPayeeName(gmcEmployeeName.getIdByIndex(value2));
										createAndSearchLotTableDTO.setTempPayeeName(createAndSearchLotTableDTO.getPayeeName());
										break;
									}
							 	}
								createAndSearchLotTableDTO.setPayeeNameContainer(gmcEmployeeName);
							}
					}
				}*/
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
				createAndSearchLotTableDTO.setUsername(userName);
				createAndSearchLotTableDTO.setPassword(passWord);				

				if(createAndSearchLotTableDTO.getCpuCode()!=null){
					TmpCPUCode masCpuCode = null;
					try {
						String cpuCodeVal[] = createAndSearchLotTableDTO.getCpuCode().split("-");

						String strcpuCode = cpuCodeVal[0];
						masCpuCode = getMasCpuCode(Long.parseLong(strcpuCode.trim()));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(masCpuCode!=null){
						createAndSearchLotTableDTO.setCpuName(masCpuCode.getDescription());
					}
				}
				if(createAndSearchLotTableDTO.getDocumentReceivedFrom()!=null && 
						!createAndSearchLotTableDTO.getDocumentReceivedFrom().equals(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
					createAndSearchLotTableDTO.setIsInsured(Boolean.TRUE);
				}
				Policy policyDetails = getPolicyByPolicyNubember(createAndSearchLotTableDTO.getPolicyNo());
				if (policyDetails !=null && policyDetails.getPaymentParty() != null && !policyDetails.getPaymentParty().isEmpty()){
					createAndSearchLotTableDTO.setPaymentPolicyType(policyDetails.getGmcPolicyType());
					createAndSearchLotTableDTO.setPaymentPartyMode(policyDetails.getPaymentParty());
				}
				if (null != createAndSearchLotTableDTO
						&& (ReferenceTable.PAYMENT_TYPE_CHEQUE)
						.equalsIgnoreCase(createAndSearchLotTableDTO
								.getPaymentTypeValue())) {

					createAndSearchLotTableDTO
							.setPaymentModeFlag(SHAConstants.YES_FLAG);
					
					createAndSearchLotTableDTO.setChangedPaymentMode(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					createAndSearchLotTableDTO.setBeneficiaryAcntNo(null);
					createAndSearchLotTableDTO.setIfscCode(null);
					createAndSearchLotTableDTO.setBankCode(null);
					createAndSearchLotTableDTO.setBankName(null);
					createAndSearchLotTableDTO.setBranchName(null);
					
					SelectValue paymentType = new SelectValue();
					paymentType.setId(1l);
					paymentType.setValue(ReferenceTable.PAYMENT_TYPE_CHEQUE);
					createAndSearchLotTableDTO.setPaymentType(paymentType);
					
				} else {
					createAndSearchLotTableDTO
							.setPaymentModeFlag(SHAConstants.N_FLAG);
					
					createAndSearchLotTableDTO.setChangedPaymentMode(SHAConstants.NEFT_TYPE);
					
					SelectValue paymentType = new SelectValue();
					paymentType.setId(0l);
					paymentType.setValue(SHAConstants.NEFT_TYPE);
					createAndSearchLotTableDTO.setPaymentType(paymentType);
				}

				/*
				 * Reimbursement reasonForChange =
				 * getReasonForChangePayeeName
				 * (createAndSearchLotTableDTO.getRodNo()); if(null !=
				 * reasonForChange) {
				 * createAndSearchLotTableDTO.setReasonForChange
				 * (reasonForChange.getReasonForChange());
				 * 
				 * }
				 */

				BankMaster cityName = getCityName(createAndSearchLotTableDTO
						.getIfscCode());
				if (null != cityName)

				{
					createAndSearchLotTableDTO.setCity(cityName.getCity());
					createAndSearchLotTableDTO.setPayableAt(cityName
							.getCity());
				}	
				
				productCodeWithName = new StringBuffer();
				if(null != createAndSearchLotTableDTO.getProductName()){
					
					createAndSearchLotTableDTO.setProduct(String.valueOf(productCodeWithName.append(createAndSearchLotTableDTO.getProduct()).append("-").append(null != createAndSearchLotTableDTO.getProductName()?createAndSearchLotTableDTO.getProductName() : "")));
				}
				else
				{
					createAndSearchLotTableDTO.setProduct(String.valueOf(productCodeWithName.append(createAndSearchLotTableDTO.getProduct()).append("").append(null != createAndSearchLotTableDTO.getProductName()?createAndSearchLotTableDTO.getProductName() : "")));
				}
				if((null != createAndSearchLotTableDTO && null != createAndSearchLotTableDTO.getSaveFlag() &&
						!(SHAConstants.YES_FLAG).equalsIgnoreCase(createAndSearchLotTableDTO.getSaveFlag()))
						||(null != createAndSearchLotTableDTO && null == createAndSearchLotTableDTO.getSaveFlag())
						|| (null != createAndSearchLotTableDTO && null != createAndSearchLotTableDTO.getSaveFlag() &&
								(SHAConstants.YES_FLAG).equalsIgnoreCase(createAndSearchLotTableDTO.getSaveFlag())
								&& createAndSearchLotTableDTO.getNoofdaysexceeding() != null && createAndSearchLotTableDTO.getNoofdaysexceeding() < 0)){
					getPenalInterestCalculation(createAndSearchLotTableDTO, searchFormDTO);
				}
				else
				{
					noOfDelayDaysCalculation(createAndSearchLotTableDTO, searchFormDTO);
				}
				if(createAndSearchLotTableDTO.getRodNo() != null){
					Reimbursement reimbursement= getReimbursementbyNO(createAndSearchLotTableDTO.getRodNo());
					if(reimbursement.getClaim() !=null 
							&& reimbursement.getClaim().getLegalClaim() !=null
							&& reimbursement.getClaim().getLegalClaim().equals("Y")){
						createAndSearchLotTableDTO.setLegalPayment(true);
						LegalTaxDeduction taxDeduction = reimbursementService.getTaxDeductionFromRODKey(reimbursement.getKey());
						 if(taxDeduction !=null){
							 LegalTaxDeductionMapper taxDeductionMapper = LegalTaxDeductionMapper.getInstance();
							 LegalBillingDTO billingDTO = taxDeductionMapper.getDTOFromTaxDeduction(taxDeduction);
							 billingDTO = reimbursementService.calculateLegalSettlment(billingDTO);
							 createAndSearchLotTableDTO.setLegalBillingDTO(billingDTO);
						 }
					}else{
						createAndSearchLotTableDTO.setLegalPayment(false);
					}
				}
			}
		}

		
		page = new Page<CreateAndSearchLotTableDTO>();
		//int pageNumber = searchFormDTO.getPageable().getPageNumber();
		searchFormDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(result.isEmpty()) {
			page.setHasNext(false);
		}
		else{
			page.setHasNext(true);
		}
		
		if(tableDTO.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(tableDTO);
		// Added for displaying total no records in create and search batch.-- starts
		{
			page.setTotalRecords(totalPaymentList.size());
		}
		
		if (null != completeTableDTOList) {
			
			Collections.sort(completeTableDTOList, new Comparator<CreateAndSearchLotTableDTO>(){
				   public int compare(CreateAndSearchLotTableDTO o1, CreateAndSearchLotTableDTO o2){
				      return o2.getNumberofdays() - o1.getNumberofdays();
				   }
				});
			page.setTotalList(completeTableDTOList);
			page.setPageItems(completeTableDTOList);
		}
		// Added for displaying total no records in create and search batch.-- ends
		page.setIsDbSearch(true);
		//return page;			
	}
		catch (Exception e) {
	
		//return page;
	
		e.printStackTrace();
	}
	 
	 
	return page;	
}

public CreateAndSearchLotTableDTO getBatchDetails(String batchNo)
{
	CreateAndSearchLotTableDTO tableDTO = null;
		Query query = entityManager.createNamedQuery("ClaimPayment.findByBatchNo");
		query = query.setParameter("batchNumber", batchNo);		
		List<ClaimPayment> claimPaymentList = query.getResultList();
		if(null != claimPaymentList && !claimPaymentList.isEmpty())
		{			
			tableDTO  = new CreateAndSearchLotTableDTO();
			tableDTO.setClaimCount(claimPaymentList.size());
			Double total = 0d;
			Double totalAppAmt = 0d;
			for (ClaimPayment claimPayment : claimPaymentList) {					
				
				totalAppAmt += claimPayment.getTotalApprovedAmount();
				tableDTO.setServiceTax(0d);						
				total += claimPayment.getTotalApprovedAmount() ;
					
			}
			tableDTO.setApprovedAmt(totalAppAmt);
			if(null != total)
			{
				tableDTO.setSumOfApprovedAndServiceTax(total);
			}	
			
		}
		return tableDTO;
	
}

@SuppressWarnings("unchecked")
public List<TmpCPUCode> getCpuCodeList() {
	
	Session session = (Session) entityManager.getDelegate();
	@SuppressWarnings("unchecked")
	List<TmpCPUCode> selectValuesList = session.createCriteria(TmpCPUCode.class)
	.addOrder(Order.asc("key"))
	.setProjection(Projections.projectionList()
			.add(Projections.property("key"), "key")
			.add(Projections.property("cpuCode"), "cpuCode")
			.add(Projections.property("description"), "description"))
			.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(TmpCPUCode.class)).list();	
	return selectValuesList;
}

public Claim searchByClaimNum(String claimNumber) {
	Query findByClaimNumber = entityManager.createNamedQuery(
			"Claim.findByClaimNumber").setParameter("claimNumber",
			claimNumber);
	List<Claim> claimList = findByClaimNumber.getResultList();
	if(null != claimList && !claimList.isEmpty())
	{
			entityManager.refresh(claimList.get(0));
			return claimList.get(0);
	}
	
	return null;

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

private MasHospitals getMasHospitalById(Long key,EntityManager entityManager) {

	Query query = entityManager.createNamedQuery("MasHospitals.findByKey");
	query.setParameter("key", key);

	List<MasHospitals> resultList = (List<MasHospitals>) query.getResultList();

	if (resultList != null && !resultList.isEmpty()) {
		return resultList.get(0);
	}
	return null;

}

private Reimbursement getReimbursementbyNO(String rodNO) {
	Query query = entityManager
			.createNamedQuery("Reimbursement.findRodByNumber");
	query = query.setParameter("rodNumber", rodNO);
	List<Reimbursement> reimbursementList = query.getResultList();
	if (null != reimbursementList && !reimbursementList.isEmpty()) {
		return reimbursementList.get(0);
	} else {
		return null;
	}

}

}