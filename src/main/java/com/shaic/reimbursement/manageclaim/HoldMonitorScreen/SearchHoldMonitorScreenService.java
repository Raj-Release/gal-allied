package com.shaic.reimbursement.manageclaim.HoldMonitorScreen;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.ims.bpm.claim.DBCalculationService;



@Stateless
public class SearchHoldMonitorScreenService extends AbstractDAO<Claim> {
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchHoldMonitorScreenService(){
		super();
	}
	
	public  Page<SearchHoldMonitorScreenTableDTO> search(
			SearchHoldMonitorScreenFormDTO searchFormDTO,
			String userName, String passWord) {
		try{
			
			String intimationNo = searchFormDTO != null && searchFormDTO.getIntimationNumber() != null ? searchFormDTO.getIntimationNumber() : null; 
			String userId = searchFormDTO != null && searchFormDTO.getUser() != null ? searchFormDTO.getUser().getValue() : null;
			String type = searchFormDTO != null && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() : null;
			Long cpuCode =  searchFormDTO != null && searchFormDTO.getCpuCode() !=null ? searchFormDTO.getCpuCode().getId() : null;
			String menuType = "M";
			String screenType = type;

			if(userId != null && !userId.isEmpty()){
				String[] splitid = userId.split("-");
				String id = splitid[0];
				userId = id.trim();
			}
			

			if(type != null && !type.isEmpty()){
				if(type.equalsIgnoreCase("common for billing & FA")){
					type = SHAConstants.BILLING_CURRENT_KEY;
				} else if(type.equalsIgnoreCase("Process Claim Request - Auto Allocation")) {
					type = SHAConstants.MA_CURRENT_QUEUE;
				}else if(type.equalsIgnoreCase("Process Claim Financial - Auto Allocation")) {
					type = SHAConstants.FA_CURRENT_QUEUE;
				} else if(type.equalsIgnoreCase("Process Claim Billing - Auto Allocation")) {
					type = SHAConstants.BILLING_CURRENT_KEY;
				}
			}
			 if(searchFormDTO.getScreenName() != null && searchFormDTO.getScreenName().equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_FOR_FINANCIAL_AUTO)){
				 type = SHAConstants.FA_CURRENT_QUEUE; 
			 }
			 if(searchFormDTO.getScreenName() != null && searchFormDTO.getScreenName().equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION)){
				 type = SHAConstants.MA_CURRENT_QUEUE; 
			 }else if(searchFormDTO.getScreenName() != null && searchFormDTO.getScreenName().equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_FOR_COMMON_BILLING_FA_AUTO)){
				 type = SHAConstants.BILLING_CURRENT_KEY; 
			 }else if(searchFormDTO.getScreenName() != null && searchFormDTO.getScreenName().equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_BILLING_AUTO_ALLOCATION)){
				 type = SHAConstants.BILLING_CURRENT_KEY; 
				 screenType = SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_BILLING_AUTO_ALLOCATION;
			 }
			List<SearchHoldMonitorScreenTableDTO> holdClaims = dbCalculationService.getHoldMonitorClaimsForBillingFA(intimationNo, userId, type,menuType,cpuCode,screenType);			
			Page<SearchHoldMonitorScreenTableDTO> page = new Page<SearchHoldMonitorScreenTableDTO>();
			page.setPageItems(holdClaims);
			page.setTotalRecords(holdClaims.size());
			return page;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateStageInformation(SearchHoldMonitorScreenTableDTO dto,Reimbursement preauthObj){
		
		StageInformation stgInformation = new StageInformation();
		Claim calimObj = getClaimsByIntimationNumber(dto.getIntimationNumber());
		stgInformation.setIntimation(calimObj.getIntimation());				
		stgInformation.setClaimType(calimObj.getClaimType());
		Stage preauthStage = new Stage();
		if(dto.getScreenName() != null && dto.getScreenName().equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_REQUEST_AUTO_ALLOCATION)){
			preauthStage.setKey(ReferenceTable.CLAIM_REQUEST_STAGE); 
		}else if(dto.getScreenName() != null && dto.getScreenName().equalsIgnoreCase(SHAConstants.HOLD_MONITORING_PROCESS_CLAIM_BILLING_AUTO_ALLOCATION)){
			preauthStage.setKey(ReferenceTable.BILLING_STAGE); 
		}else{
			preauthStage.setKey(ReferenceTable.FINANCIAL_STAGE);
		}
		stgInformation.setStage(preauthStage);
		Status status = new Status();
		status.setKey(ReferenceTable.PREAUTH_RELEASE_STATUS_KEY);
		status.setProcessValue(ReferenceTable.PREAUTH_RELEASE_STATUS);
		if(preauthObj!=null){
			stgInformation.setReimbursement(preauthObj);
//			stgInformation.setStage(preauthObj.getStage());
		}
		stgInformation.setStatus(status);
		stgInformation.setClaim(calimObj);
		String strUserName = dto.getUsername();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		stgInformation.setCreatedBy(userNameForDB);
		//stgInformation.setCreatedBy(preauth.getModifiedBy());
		stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		stgInformation.setStatusRemarks(dto.getHoldRemarks());
		
		entityManager.persist(stgInformation);
		entityManager.flush();
	}

	
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthByKey(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()){
			return singleResult.get(0);
		}
		return null;
	}
	

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimsByIntimationNumber(String intimationNumber) {
		List<Claim> resultClaim = null;
		if (intimationNumber != null) {

			Query findByIntimationNum = entityManager.createNamedQuery(
					"Claim.findByIntimationNumber").setParameter(
					"intimationNumber", intimationNumber);

			try {
				resultClaim = (List<Claim>) findByIntimationNum.getResultList();
				
				if(resultClaim != null && !resultClaim.isEmpty()){
					return resultClaim.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}