package com.shaic.claim.cvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.junit.internal.matchers.Each;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.AuditCategory;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditProcessor;
import com.shaic.arch.table.AuditTeam;
import com.shaic.arch.table.Page;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthFormDTO;
import com.shaic.domain.CVCStageHeader;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimAuditQuery;
import com.shaic.domain.MasterService;
import com.shaic.domain.Product;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;
@Stateless
public class SearchCVCService  extends AbstractDAO <Preauth>{
	
	 private final Logger log = LoggerFactory.getLogger(SearchCVCService.class);
	
	
	 @EJB
	 private MasterService masterService;
	 
	public SearchCVCService()	
	{
		super();
	}
	
	@Override
	public Class<Preauth> getDTOClass() {
		// TODO Auto-generated method stub
		return Preauth.class;
	}
	
	@SuppressWarnings("unused")
	public SearchCVCTableDTO search(SearchCVCFormDTO formDTO, String userName, String passWord)
	{
		try 
		{
			SearchCVCTableDTO tableDto = null ;
			
			String claimType = null != formDTO.getClaimTypeId()? formDTO.getClaimTypeId(): null;
			String intimationNo = null != formDTO.getIntimationNo()? formDTO.getIntimationNo(): null;
			String auditReason = null != formDTO.getReason()? formDTO.getReason(): null;
			String year = null != formDTO.getYear()? formDTO.getYear(): null;
			List<String> listOfProduct= new ArrayList<String>();
			List<Long> listOfProductKey= new ArrayList<Long>();
			Long productFlag=0L;
			
			if(formDTO.getProductNameMulti() != null){
				String productSearch = formDTO.getProductNameMulti().toString();
				if(!productSearch.equals("[]")){
					String temp[] = productSearch.split(",");
					listOfProduct.clear();
					for (int i = 0; i < temp.length; i++) {
						String valtemp[] = temp[i].split(":");
						String val = valtemp[0].replaceAll("\\[", "");
						if((val.trim()).equalsIgnoreCase("All")||(val.trim()).equalsIgnoreCase("All]")){
							listOfProduct.remove(val);
						}
						else{
							listOfProduct.add((val.trim()));
						}
						
					}
				}
				
			}
			if(listOfProduct!=null && !listOfProduct.isEmpty()){
				for(String productCode : listOfProduct){
					List<Product> productList= masterService.getProductListByProductCode(productCode);
					if(productList!=null && !productList.isEmpty()){
						for(Product product: productList){
							Long productKey=product.getKey();
							listOfProductKey.add(productKey);
						}
					}

				}
			}
			
			//Object[] resultArray = new Object[1];
			Object[] inputArray = new Object[listOfProductKey.size()];
			if(listOfProductKey.size()==0){
				System.out.println("Inside size 0");
				productFlag=0L;
			}
			else if(listOfProductKey.size()>0){
				productFlag=1L;
				System.out.println("Inside size >0");
			}
			else{
				productFlag=0L;
				System.out.println("Inside else");
			}
			String productList ="";
			for (int i=0;i<listOfProductKey.size();i++) {
				productList= productList+(listOfProductKey.get(i) != null ? listOfProductKey.get(i).toString(): "")+(",");
				
				/*Object[] attributes = new Object[1];
				attributes[0] =  listOfProductKey.get(i) != null ? listOfProductKey.get(i): "";
				inputArray[i] =attributes;
				System.out.println(inputArray.toString());*/
			}
			if(productList.length()>0){
			productList=productList.substring(0,(productList.length()-1));
			}
			System.out.println("Product List"+productList);
			//resultArray[0]= inputArray;
			DBCalculationService dbCalculationService = new DBCalculationService();
			
			if (null != intimationNo && !intimationNo.isEmpty()) {
				tableDto = dbCalculationService.getCVCAuditDataForIntimationNumber(userName,intimationNo,year);	
			}else {
				log.info("CVC AUDIT BEFORE PROCEDURE CALL"+ new Date());
				//System.out.println("Input Array"+inputArray.toString());
				tableDto = dbCalculationService.getCVCAuditData(userName,claimType,productList,productFlag);	
				log.info("CVC AUDIT AFTER PROCEDURE CALL"+"*****"+tableDto.getIntimationKey()+"*****"+new Date());
			}
			
			if(auditReason != null) {
				tableDto.setAuditReason(auditReason);
			}
			
			if(tableDto != null/* && tableDto.getIntimationKey() != null*/) {
			tableDto.setUsername(userName);
			} else {
				return null;
			}
			return tableDto;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	public void submitCVCAuditDetails(SearchCVCTableDTO cvcTableDto){
		
		AuditDetails auditDetails = new AuditDetails();
		if(null != cvcTableDto){
			auditDetails.setIntimationKey(cvcTableDto.getIntimationKey());
			Claim clmObj = new Claim();
			clmObj.setKey(cvcTableDto.getClaimKey());
			auditDetails.setClaim(clmObj);
			auditDetails.setTransactionKey(cvcTableDto.getTransactionKey());
			auditDetails.setClaimType(cvcTableDto.getClaimType());
			auditDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			auditDetails.setCreatedBy(cvcTableDto.getUsername());
			auditDetails.setActiveStatus(SHAConstants.YES_FLAG);
			auditDetails.setAuditRemarks(cvcTableDto.getAuditRemarks());
			auditDetails.setAuditStatus(cvcTableDto.getAuditStatus());
			auditDetails.setAuditReason(cvcTableDto.getAuditReason());
			if (null != auditDetails && null != auditDetails.getAuditStatus() 
					&& auditDetails.getAuditStatus().equalsIgnoreCase(SHAConstants.ERROR_STRING)) {
//				auditDetails.setErrorTeam(cvcTableDto.getTeam());
//				auditDetails.setErrorCategory(cvcTableDto.getErrorCategory());
				auditDetails.setMonetaryResult(cvcTableDto.getMonetaryResult());
//				auditDetails.setErrorProcessor(cvcTableDto.getProcessor());
//				auditDetails.setRemediationStatus(cvcTableDto.getRemediationStatus());
				auditDetails.setRemediationStatus(cvcTableDto.getQryStatus());
				auditDetails.setAmountInvolved(Long.parseLong(cvcTableDto.getAmountInvolved()));
			}
			if (null != auditDetails && null != auditDetails.getAuditStatus() 
					&& auditDetails.getAuditStatus().equalsIgnoreCase(SHAConstants.NO_ERROR_STRING)) {
				auditDetails.setRemediationStatus(SHAConstants.COMPLETED_STRING);
				auditDetails.setFinalAuditStatus(SHAConstants.NO_ERROR_STRING);
			}
			
			if(auditDetails.getRemediationStatus().equalsIgnoreCase(SHAConstants.COMPLETED_STRING)
					&& auditDetails.getFinalAuditStatus().equalsIgnoreCase(SHAConstants.NO_ERROR_STRING)) {
				auditDetails.setCompletedDate(new Timestamp(System.currentTimeMillis()));
			}
			
			entityManager.persist(auditDetails);
			entityManager.flush();
			entityManager.clear();
			
			if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getTeam() != null) {
				List<String> listOfTeam = getListFromMultiSelectComponent(cvcTableDto.getTeam());
				ClaimAuditQuery clmAudQry = null;
				for (String strTeam : listOfTeam) {
					AuditTeam auditTeam = new AuditTeam();
					auditTeam.setAuditKey(auditDetails.getKey());
					auditTeam.setAuditTeam(strTeam);
					auditTeam.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					auditTeam.setCreatedBy(cvcTableDto.getUsername());
					auditTeam.setActiveStatus(SHAConstants.YES_FLAG);
					entityManager.persist(auditTeam);
					entityManager.flush();
					entityManager.clear();
					
					if (null != auditDetails && null != auditDetails.getAuditStatus() 
							&& auditDetails.getAuditStatus().equalsIgnoreCase(SHAConstants.ERROR_STRING)
							&& !(strTeam.trim().toLowerCase().contains("billing") || strTeam.trim().toLowerCase().contains("financial"))) {
						clmAudQry = new ClaimAuditQuery();
						clmAudQry.setAudit(auditDetails);
						clmAudQry.setTeamName(strTeam);
						clmAudQry.setStatus(cvcTableDto.getQryStatus());
						if(strTeam.trim().toLowerCase().contains("cashless")){
							
							clmAudQry.setQueryRemarks(cvcTableDto.getClsQryRemarks());
						}
						else if(strTeam.trim().toLowerCase().contains("medical")){
							clmAudQry.setQueryRemarks(cvcTableDto.getMedicaQrylRemarks());
						}/*else if(strTeam.trim().toLowerCase().contains("billing") || strTeam.trim().toLowerCase().contains("financial")){
							clmAudQry.setQueryRemarks(cvcTableDto.getBillingFaQryRemarks());
						}*/
						
						clmAudQry.setQryRaiseDate(new Timestamp(System.currentTimeMillis()));
						clmAudQry.setCreatedBy(cvcTableDto.getUsername());
						clmAudQry.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						clmAudQry.setActiveStatus(1L);
						entityManager.persist(clmAudQry);
						entityManager.flush();
						entityManager.clear();
					}
				}
				
				if(listOfTeam.contains(SHAConstants.AUDIT_TEAM_BILLING) || listOfTeam.contains(SHAConstants.AUDIT_TEAM_FINANCIAL)){
					clmAudQry = new ClaimAuditQuery();
					clmAudQry.setAudit(auditDetails);
					clmAudQry.setTeamName(listOfTeam.contains(SHAConstants.AUDIT_TEAM_BILLING) ? SHAConstants.AUDIT_TEAM_BILLING : SHAConstants.AUDIT_TEAM_FINANCIAL);
					clmAudQry.setStatus(cvcTableDto.getQryStatus());
					clmAudQry.setQueryRemarks(cvcTableDto.getBillingFaQryRemarks());
					clmAudQry.setQryRaiseDate(new Timestamp(System.currentTimeMillis()));
					clmAudQry.setCreatedBy(cvcTableDto.getUsername());
					clmAudQry.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					clmAudQry.setActiveStatus(1L);
					entityManager.persist(clmAudQry);
					entityManager.flush();
					entityManager.clear();
				}
				
			}
			
			if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getErrorCategory() != null) {
				List<String> listOfErrorCategory = getListFromMultiSelectComponent(cvcTableDto.getErrorCategory());
				for (String strTeam : listOfErrorCategory) {
					AuditCategory auditCategory = new AuditCategory();
					auditCategory.setAuditKey(auditDetails.getKey());
					auditCategory.setAuditCategory(Long.parseLong(strTeam));
					if (null != auditCategory && auditCategory.getAuditCategory() != null 
							&& auditCategory.getAuditCategory().toString().contains(SHAConstants.CVC_OTHERS_MAS_KEY)) {
						auditCategory.setAuditCategoryOthrRmks(cvcTableDto.getOtherRemarks());
					}
					auditCategory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					auditCategory.setCreatedBy(cvcTableDto.getUsername());
					auditCategory.setActiveStatus(SHAConstants.YES_FLAG);
					entityManager.persist(auditCategory);
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getProcessor() != null) {
				List<String> listOfProcessor = getListFromMultiSelectComponent(cvcTableDto.getProcessor());
				for (String strTeam : listOfProcessor) {
					AuditProcessor auditProcessor = new AuditProcessor();
					auditProcessor.setAuditKey(auditDetails.getKey());
					auditProcessor.setAuditProcessor(strTeam);
					auditProcessor.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					auditProcessor.setCreatedBy(cvcTableDto.getUsername());
					auditProcessor.setActiveStatus(SHAConstants.YES_FLAG);
					entityManager.persist(auditProcessor);
					entityManager.flush();
					entityManager.clear();
				}
			}
		}
		
		if(null != cvcTableDto.getStageKey()){
			CVCStageHeader stage = getStageHeaderByKey(cvcTableDto.getStageKey());
			if(null != stage){
				stage.setCvcAuditCompleted(1);
				stage.setCvcLockFlag(0);
				entityManager.merge(stage);
				entityManager.flush();
				entityManager.clear();
			}
			
		}
		
		if(null != cvcTableDto.getClaimType() && null != cvcTableDto.getTransactionKey()){
			if(SHAConstants.CASHLESS_CHAR.equalsIgnoreCase(cvcTableDto.getClaimType())){
				Preauth preauth = getPreauthListByKey(cvcTableDto.getTransactionKey());
				if(null != preauth){
					preauth.setCvcAuditFlag(SHAConstants.YES_FLAG);
					preauth.setCvcLockFlag(SHAConstants.N_FLAG);
					entityManager.merge(preauth);
					entityManager.flush();
					entityManager.clear();
				}
			}
			else
			{
				Reimbursement reimbursement = getReimbursementObjectByKey(cvcTableDto.getTransactionKey());
					if(null != reimbursement){
						reimbursement.setCvcAuditFlag(SHAConstants.YES_FLAG);
						reimbursement.setCvcLockFlag(SHAConstants.N_FLAG);
						entityManager.merge(reimbursement);
						entityManager.flush();
						entityManager.clear();
					}
			}
		}
}
	
	public void submitPostProcessClaimCVCAuditDetails(SearchCVCTableDTO cvcTableDto){
		
		AuditDetails auditDetails = new AuditDetails();
		if(null != cvcTableDto){
			auditDetails.setIntimationKey(cvcTableDto.getIntimationKey());
			Claim clmObj = new Claim();
			clmObj.setKey(cvcTableDto.getClaimKey());
			auditDetails.setClaim(clmObj);
			auditDetails.setTransactionKey(cvcTableDto.getTransactionKey());
			auditDetails.setClaimType(cvcTableDto.getClaimType());
			auditDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			auditDetails.setCreatedBy(cvcTableDto.getUsername());
			auditDetails.setActiveStatus(SHAConstants.YES_FLAG);
			auditDetails.setAuditRemarks(cvcTableDto.getAuditRemarks());
			auditDetails.setAuditStatus(cvcTableDto.getAuditStatus());
			auditDetails.setAuditReason(cvcTableDto.getAuditReason());
			if (null != auditDetails && null != auditDetails.getAuditStatus() 
					&& auditDetails.getAuditStatus().equalsIgnoreCase(SHAConstants.ERROR_STRING)) {
//				auditDetails.setErrorTeam(cvcTableDto.getTeam());
//				auditDetails.setErrorCategory(cvcTableDto.getErrorCategory());
				auditDetails.setMonetaryResult(cvcTableDto.getMonetaryResult());
//				auditDetails.setErrorProcessor(cvcTableDto.getProcessor());
//				auditDetails.setRemediationStatus(cvcTableDto.getRemediationStatus());
				auditDetails.setRemediationStatus(cvcTableDto.getQryStatus());
				auditDetails.setAmountInvolved(Long.parseLong(cvcTableDto.getAmountInvolved()));
				auditDetails.setCompletedDate(new Timestamp(System.currentTimeMillis()));
			}
			if (null != auditDetails && null != auditDetails.getAuditStatus() 
					&& auditDetails.getAuditStatus().equalsIgnoreCase(SHAConstants.NO_ERROR_STRING)) {
				auditDetails.setRemediationStatus(SHAConstants.COMPLETED_STRING);
				auditDetails.setFinalAuditStatus(SHAConstants.NO_ERROR_STRING);
			}
			
			
			entityManager.persist(auditDetails);
			entityManager.flush();
			entityManager.clear();
			
			if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getTeam() != null) {
				List<String> listOfTeam = getListFromMultiSelectComponent(cvcTableDto.getTeam());
				ClaimAuditQuery clmAudQry = null;
				for (String strTeam : listOfTeam) {
					AuditTeam auditTeam = new AuditTeam();
					auditTeam.setAuditKey(auditDetails.getKey());
					auditTeam.setAuditTeam(strTeam);
					auditTeam.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					auditTeam.setCreatedBy(cvcTableDto.getUsername());
					auditTeam.setActiveStatus(SHAConstants.YES_FLAG);
					entityManager.persist(auditTeam);
					entityManager.flush();
					entityManager.clear();
					
					if (null != auditDetails && null != auditDetails.getAuditStatus() 
							&& auditDetails.getAuditStatus().equalsIgnoreCase(SHAConstants.ERROR_STRING)
							&& !(strTeam.trim().toLowerCase().contains("billing") || strTeam.trim().toLowerCase().contains("financial"))) {
						clmAudQry = new ClaimAuditQuery();
						clmAudQry.setAudit(auditDetails);
						clmAudQry.setTeamName(strTeam);
						clmAudQry.setStatus(cvcTableDto.getQryStatus());
						/*if(strTeam.trim().toLowerCase().contains("cashless")){
							
							clmAudQry.setQueryRemarks(cvcTableDto.getClsQryRemarks());
						}
						else if(strTeam.trim().toLowerCase().contains("medical")){
							clmAudQry.setQueryRemarks(cvcTableDto.getMedicaQrylRemarks());
						}else if(strTeam.trim().toLowerCase().contains("billing") || strTeam.trim().toLowerCase().contains("financial")){
							clmAudQry.setQueryRemarks(cvcTableDto.getBillingFaQryRemarks());
						}*/
						
						clmAudQry.setQryRaiseDate(new Timestamp(System.currentTimeMillis()));
						clmAudQry.setCreatedBy(cvcTableDto.getUsername());
						clmAudQry.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						clmAudQry.setActiveStatus(1L);
						entityManager.persist(clmAudQry);
						entityManager.flush();
						entityManager.clear();
					}
				}
				
				if(listOfTeam.contains(SHAConstants.AUDIT_TEAM_BILLING) || listOfTeam.contains(SHAConstants.AUDIT_TEAM_FINANCIAL)){
					clmAudQry = new ClaimAuditQuery();
					clmAudQry.setAudit(auditDetails);
					clmAudQry.setTeamName(listOfTeam.contains(SHAConstants.AUDIT_TEAM_BILLING) ? SHAConstants.AUDIT_TEAM_BILLING : SHAConstants.AUDIT_TEAM_FINANCIAL);
					clmAudQry.setStatus(cvcTableDto.getQryStatus());
//					clmAudQry.setQueryRemarks(cvcTableDto.getBillingFaQryRemarks());
					clmAudQry.setQryRaiseDate(new Timestamp(System.currentTimeMillis()));
					clmAudQry.setCreatedBy(cvcTableDto.getUsername());
					clmAudQry.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					clmAudQry.setActiveStatus(1L);
					entityManager.persist(clmAudQry);
					entityManager.flush();
					entityManager.clear();
				}
				
			}
			
			if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getErrorCategory() != null) {
				List<String> listOfErrorCategory = getListFromMultiSelectComponent(cvcTableDto.getErrorCategory());
				for (String strTeam : listOfErrorCategory) {
					AuditCategory auditCategory = new AuditCategory();
					auditCategory.setAuditKey(auditDetails.getKey());
					auditCategory.setAuditCategory(Long.parseLong(strTeam));
					if (null != auditCategory && auditCategory.getAuditCategory() != null 
							&& auditCategory.getAuditCategory().toString().contains(SHAConstants.CVC_OTHERS_MAS_KEY)) {
						auditCategory.setAuditCategoryOthrRmks(cvcTableDto.getOtherRemarks());
					}
					auditCategory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					auditCategory.setCreatedBy(cvcTableDto.getUsername());
					auditCategory.setActiveStatus(SHAConstants.YES_FLAG);
					entityManager.persist(auditCategory);
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			if(auditDetails.getKey() != null && null != cvcTableDto && cvcTableDto.getProcessor() != null) {
				List<String> listOfProcessor = getListFromMultiSelectComponent(cvcTableDto.getProcessor());
				for (String strTeam : listOfProcessor) {
					AuditProcessor auditProcessor = new AuditProcessor();
					auditProcessor.setAuditKey(auditDetails.getKey());
					auditProcessor.setAuditProcessor(strTeam);
					auditProcessor.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					auditProcessor.setCreatedBy(cvcTableDto.getUsername());
					auditProcessor.setActiveStatus(SHAConstants.YES_FLAG);
					entityManager.persist(auditProcessor);
					entityManager.flush();
					entityManager.clear();
				}
			}
		}
		
		if(null != cvcTableDto.getStageKey()){
			CVCStageHeader stage = getStageHeaderByKey(cvcTableDto.getStageKey());
			if(null != stage){
				stage.setCvcAuditCompleted(1);
				stage.setCvcLockFlag(0);
				entityManager.merge(stage);
				entityManager.flush();
				entityManager.clear();
			}
			
		}
		
		if(null != cvcTableDto.getClaimType() && null != cvcTableDto.getTransactionKey()){
			if(SHAConstants.CASHLESS_CHAR.equalsIgnoreCase(cvcTableDto.getClaimType())){
				Preauth preauth = getPreauthListByKey(cvcTableDto.getTransactionKey());
				if(null != preauth){
					preauth.setCvcAuditFlag(SHAConstants.YES_FLAG);
					preauth.setCvcLockFlag(SHAConstants.N_FLAG);
					entityManager.merge(preauth);
					entityManager.flush();
					entityManager.clear();
				}
			}
			else
			{
				Reimbursement reimbursement = getReimbursementObjectByKey(cvcTableDto.getTransactionKey());
					if(null != reimbursement){
						reimbursement.setCvcAuditFlag(SHAConstants.YES_FLAG);
						reimbursement.setCvcLockFlag(SHAConstants.N_FLAG);
						entityManager.merge(reimbursement);
						entityManager.flush();
						entityManager.clear();
					}
			}
		}
	}
	
	

	@SuppressWarnings("unchecked")
	public Preauth getPreauthListByKey(Long preauthKey) {

		Query findByKey = entityManager.createNamedQuery("Preauth.findByKey")
				.setParameter("preauthKey", preauthKey);
		List<Preauth> preauthList = (List<Preauth>) findByKey.getResultList();
		if (null != preauthList && !preauthList.isEmpty()) {
			return preauthList.get(0);

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public CVCStageHeader getStageHeaderByKey(Long key){

		Query findByKey = entityManager.createNamedQuery("CVCStageHeader.findByKey")
				.setParameter("key", key);
		List<CVCStageHeader> cvcList = (List<CVCStageHeader>) findByKey.getResultList();
		if (null != cvcList && !cvcList.isEmpty()) {
			return cvcList.get(0);

		}
		return null;
	}
	
	public Reimbursement getReimbursementObjectByKey(Long key) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", key);		
		List<Reimbursement> reimbursementList = (List<Reimbursement>) query.getResultList();
		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			return reimbursementList.get(0);
		}
		return null;
	}
	
	public void updateUnLockFlag(SearchCVCTableDTO cvcTableDto){
		
		if(null != cvcTableDto.getStageKey()){
			CVCStageHeader stage = getStageHeaderByKey(cvcTableDto.getStageKey());
			if(null != stage){
				stage.setCvcLockFlag(0);
				stage.setReleasedDate(new Timestamp(System.currentTimeMillis()));
				stage.setLockedBy(null);
				stage.setLockedDate(null);
				entityManager.merge(stage);
				entityManager.flush();
				entityManager.clear();
			}
		}
			
		if(null != cvcTableDto.getClaimType() && null != cvcTableDto.getTransactionKey()){
			if(SHAConstants.CASHLESS_CHAR.equalsIgnoreCase(cvcTableDto.getClaimType())){
				Preauth preauth = getPreauthListByKey(cvcTableDto.getTransactionKey());
				if(null != preauth){
					preauth.setCvcLockFlag(SHAConstants.N_FLAG);
					entityManager.merge(preauth);
					entityManager.flush();
					entityManager.clear();
				}
			}
			else
			{
				Reimbursement reimbursement = getReimbursementObjectByKey(cvcTableDto.getTransactionKey());
				if(null != reimbursement){
					reimbursement.setCvcLockFlag(SHAConstants.N_FLAG);
					entityManager.merge(reimbursement);
					entityManager.flush();
					entityManager.clear();
				}
			}
		}
	}
	
	public List<String> getListFromMultiSelectComponent(Object object){
		
		String string = object.toString();
		if(!string.equals("[]")){
			String temp[] = string.split(",");
			List<String> listOfString = new ArrayList<String>();
			listOfString.clear();
			for (int i = 0; i < temp.length; i++) {
				String val = temp[i].replaceAll("\\[", "").replaceAll("\\]", "");
				listOfString.add(val.trim());
			}
			return listOfString;
		}
		return new ArrayList<String>();
		
	}
}
