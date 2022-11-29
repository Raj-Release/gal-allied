/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Investigation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class UpdatePaymentDetailService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	

	
	//private String hospitalName = null;
	
	List<String> batchNoList = new ArrayList<String>();	
	//private int iSlNo = 1;
	
	

	public  Page<UpdatePaymentDetailTableDTO> search(
			UpdatePaymentDetailFormDTO searchFormDTO,
			String userName, String passWord) {
		Page<UpdatePaymentDetailTableDTO> page = null;
		
		try
		{
			Object[] resultArray = new Object[1];
			Object[] inputArray = new Object[7];
			if(searchFormDTO.getIntimationNo()!=null){
				inputArray[0] = searchFormDTO.getIntimationNo();
			}
			if(searchFormDTO.getCliamType()!=null){
				inputArray[1] = searchFormDTO.getCliamType().getValue();
			}
			if(searchFormDTO.getCpuCode()!=null){
				inputArray[2] = searchFormDTO.getCpuCode().getId();
			}
			if(searchFormDTO.getFromDate()!=null){
				inputArray[3] = SHAUtils.getDateWithoutTime(searchFormDTO.getFromDate());
			}
			if(searchFormDTO.getToDate()!=null){
				inputArray[4] = SHAUtils.getDateWithoutTime(searchFormDTO.getToDate());
			}
			if(searchFormDTO.getProduct()!=null){
				inputArray[5] = searchFormDTO.getProduct().getValue();
			}
			if(searchFormDTO.getType()!=null){
				String type ="";
				if("No".equalsIgnoreCase(searchFormDTO.getType().getValue())){
					type = "N";
				}else if("Hold".equalsIgnoreCase(searchFormDTO.getType().getValue()))
					type = "H";
				inputArray[6] = type;
			}
			resultArray[0]= inputArray;
			page = new Page<UpdatePaymentDetailTableDTO>();
			page.setHasNext(true);
			
			DBCalculationService calculationService = new DBCalculationService();
			List<UpdatePaymentDetailTableDTO> paymentDetailProcedureList = calculationService.getPaymentDetailProcedure(resultArray);
			if(paymentDetailProcedureList.isEmpty()) {
				page.setHasNext(false);
			}
			else{
				page.setHasNext(true);
			}
			if(null != paymentDetailProcedureList)
			{
				page.setTotalRecords(paymentDetailProcedureList.size());
				page.setTotalList(paymentDetailProcedureList);
				page.setPageItems(paymentDetailProcedureList);
			}
			
		}
 		catch (Exception e) {
		
			//return page;
		
			e.printStackTrace();
		}
		 
		 
		return page;	
	}
	
	public Map<String , Object> updateBatchNumberForPaymentProcessing(List<CreateAndSearchLotTableDTO> tableDTOList,String typeOfService)
	{
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
		return lotCreationMap;
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
	
	
	private ClaimPayment getClaimPayment(Long key)
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
	
	
	private Reimbursement getReimbursement(Long key)
	{
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey");
		query = query.setParameter("primaryKey", key);
		List<Reimbursement> reimbursementList = query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			for (Reimbursement reimbursement : reimbursementList) {
				entityManager.refresh(reimbursement);
			}
			
			return reimbursementList.get(0);
		}
		else
		{
			return null;
		}
		
	}
	
	public Boolean savePaymentDetails(UpdatePaymentDetailTableDTO paymentDetailTableDTO, String holdFlag)
	{
		try
		{
		/*ClaimPayment claimPayment = getClaimPayment(paymentDetailTableDTO.getPaymentKey());
		
			if(paymentDetailTableDTO.getPaymentType()!=null){
				claimPayment.setPaymentType(paymentDetailTableDTO.getPaymentType().getValue());
			}
				if(null != paymentDetailTableDTO.getBeneficiaryAcno())
				claimPayment.setAccountNumber(paymentDetailTableDTO.getBeneficiaryAcno());
				if(null != paymentDetailTableDTO.getIfscCode())
				claimPayment.setIfscCode(paymentDetailTableDTO.getIfscCode());
				if(null != paymentDetailTableDTO.getBranchName())
				claimPayment.setBranchName(paymentDetailTableDTO.getBranchName());
				if(null != paymentDetailTableDTO.getBankName())
				claimPayment.setBankName(paymentDetailTableDTO.getBankName());
				if(null != paymentDetailTableDTO.getPaymentCpuCode())
					claimPayment.setCpuCode(Long.parseLong(paymentDetailTableDTO.getPaymentCpuCode()));
				if(null != paymentDetailTableDTO.getProposerName())
					claimPayment.setProposerName(paymentDetailTableDTO.getProposerName());
				if(null != paymentDetailTableDTO.getEmployeeName())
					claimPayment.setGmcEmployeeName(paymentDetailTableDTO.getEmployeeName());
		if(null != paymentDetailTableDTO.getPayeeName())
		{
			claimPayment.setPayeeName(paymentDetailTableDTO.getPayeeName());
		}
		if(null != paymentDetailTableDTO.getEmailID())
		claimPayment.setEmailId(paymentDetailTableDTO.getEmailID());
		
		if(null != paymentDetailTableDTO.getReasonForChange())
			claimPayment.setReasonForChange(paymentDetailTableDTO.getReasonForChange());
		
		
		if(null != claimPayment.getKey())
		{
			entityManager.merge(claimPayment);
			entityManager.flush();
			entityManager.clear();
			
		}*/
		if(null != paymentDetailTableDTO.getRodKey() && holdFlag!=null) {
			Reimbursement reimbursement = getReimbursement(paymentDetailTableDTO.getRodKey());
			if(reimbursement!=null ){
				reimbursement.setUpdatePaymentDtlsFlag(holdFlag);
				reimbursement.setPayableAt(paymentDetailTableDTO.getPayableCity());
				reimbursement.setAccountNumber(paymentDetailTableDTO.getBeneficiaryAcno());
				reimbursement.setReasonForChange(paymentDetailTableDTO.getReasonForChange());
				if(paymentDetailTableDTO.getPaymentType()!=null){
					reimbursement.setPaymentModeId(paymentDetailTableDTO.getPaymentType().getId());
				}
				if(paymentDetailTableDTO.getPayeeName()!=null){
					SelectValue payeeName = paymentDetailTableDTO.getPayeeName();
					reimbursement.setPayeeName(payeeName.getValue());
				}
				reimbursement.setPayeeEmailId(paymentDetailTableDTO.getEmailID());
				if(paymentDetailTableDTO.getIfscCode()!=null){
					BankMaster cityName = getCityName(paymentDetailTableDTO.getIfscCode());
					if(cityName!=null){
						reimbursement.setBankId(cityName.getKey());
					}
				}
				if(holdFlag.equals("Y")){
					Stage stage = new Stage();
					stage.setKey(ReferenceTable.UPDATE_PAYMENT_STAGE);
					reimbursement.setStage(stage);
					
					Status status = new Status();
					status.setKey(ReferenceTable.UPDATE_PAYMENT_STATUS);
					reimbursement.setStatus(status);
				}
				if(paymentDetailTableDTO.getPaymentCpuCode()!=null){
					String value = paymentDetailTableDTO.getPaymentCpuCode().getValue();
					value =value.trim().substring(0, value.indexOf("-"));
					reimbursement.setPaymentCpuCode(Long.parseLong(value));
				}
				entityManager.merge(reimbursement);
				entityManager.flush();
				entityManager.clear();
			}
		}
		
		return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public List<CreateAndSearchLotTableDTO> getBatchDetails(CreateAndSearchLotTableDTO tableDTO)
	{
		List<CreateAndSearchLotTableDTO> tableDTOList = null;
		List<CreateAndSearchLotTableDTO> finalList =  new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != tableDTO)
		{/*
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
		*/}
		return finalList;
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
	
	private List<UpdatePaymentDetailTableDTO> getTableDTOList(List<Reimbursement> claimPaymentList,String userName, String passWord)
	{
		List<UpdatePaymentDetailTableDTO> tableDTO = UpdatePaymentDetailMapper.getInstance().getListOfCreateAndSearchLotTableDTO(claimPaymentList);
		
		
		List<String> listOfHospCode1 = new ArrayList<String>();
		for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : tableDTO) {
			
			/*if(createAndSearchLotTableDTO.getHospitalCode() != null){
				listOfHospCode1.add(createAndSearchLotTableDTO.getHospitalCode());
			}*/
			
		}		
		
		if(null != tableDTO && !tableDTO.isEmpty())
		{
			for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : tableDTO) {
				
				/*if(null != batchNoList && !batchNoList.contains(createAndSearchLotTableDTO.getAccountBatchNo()))
				{
					batchNoList.add(createAndSearchLotTableDTO.getAccountBatchNo());
				}*/
				
				createAndSearchLotTableDTO.setUsername(userName);
				createAndSearchLotTableDTO.setPassword(passWord);			
				
				/*if(null != createAndSearchLotTableDTO && (ReferenceTable.PAYMENT_TYPE_CHEQUE).equalsIgnoreCase(createAndSearchLotTableDTO.getPaymentType()))
				{
					//createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.YES_FLAG);
					//createAndSearchLotTableDTO.setChangedPaymentMode(ReferenceTable.PAYMENT_TYPE_CHEQUE);
				}
				else
				{
					//createAndSearchLotTableDTO.setPaymentModeFlag(SHAConstants.N_FLAG);
					//createAndSearchLotTableDTO.setChangedPaymentMode(SHAConstants.NEFT_TYPE);
				}*/
				
			
				BankMaster cityName = getCityName(createAndSearchLotTableDTO.getIfscCode());
				if(null != cityName)
				
				{
					//createAndSearchLotTableDTO.setCity(cityName.getCity());
					//createAndSearchLotTableDTO.setPayableAt(cityName.getCity());
					createAndSearchLotTableDTO.setBankName(cityName.getBankName());
					

				}				

				
				//createAndSearchLotTableDTO.setServiceTax(0d);
				Double total = 0d;
				if(null != createAndSearchLotTableDTO.getApprovedAmt()){
					//total = createAndSearchLotTableDTO.getApprovedAmt() + createAndSearchLotTableDTO.getServiceTax();
				}
				if(null != total)
				{
				//createAndSearchLotTableDTO.setSumOfApprovedAndServiceTax(total);
				}
				
			//	Boolean isFvrOrInvesInitiated = isFvrOrInvesInitiated(createAndSearchLotTableDTO);
				
				//createAndSearchLotTableDTO.setIsFvrOrInvesInitiated(isFvrOrInvesInitiated);
				
				BPMClientContext obj = new BPMClientContext();
				
				/*if(isFvrOrInvesInitiated){		
					
					createAndSearchLotTableDTO.setIrdaTAT(Integer.valueOf(obj.getIRDAForFvrAndInvs()));
				}
				else
				{
					createAndSearchLotTableDTO.setIrdaTAT(Integer.valueOf(obj.getIRDATATDays()));
				}*/
				
				
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

	public void updatePaymentDetail(List<UpdatePaymentDetailTableDTO> tableDTOList, String holdFlag) {
		for (UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO : tableDTOList) {
				savePaymentDetails(updatePaymentDetailTableDTO, holdFlag);
		}
		
	}
}
