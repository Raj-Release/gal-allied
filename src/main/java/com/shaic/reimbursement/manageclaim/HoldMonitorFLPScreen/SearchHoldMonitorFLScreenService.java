package com.shaic.reimbursement.manageclaim.HoldMonitorFLPScreen;

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
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenFormDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;



@Stateless
public class SearchHoldMonitorFLScreenService extends AbstractDAO<Claim> {
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public SearchHoldMonitorFLScreenService(){
		super();
	}
	
	public  Page<SearchHoldMonitorScreenTableDTO> search(SearchHoldMonitorScreenFormDTO searchFormDTO, String userName, String passWord) {
		try{
			
			String intimationNo = searchFormDTO != null && searchFormDTO.getIntimationNumber() != null ? searchFormDTO.getIntimationNumber() : null; 
			String userId = searchFormDTO != null && searchFormDTO.getUser() != null ? searchFormDTO.getUser().getValue() : null;
			String type = searchFormDTO != null && searchFormDTO.getType() != null ? searchFormDTO.getType().getValue() : null;
			Long cpuCode =  searchFormDTO != null && searchFormDTO.getCpuCode() !=null ? searchFormDTO.getCpuCode().getId() : null;
			String menuType = "M";

			if(userId != null && !userId.isEmpty()){
				String[] splitid = userId.split("-");
				String id = splitid[0];
				userId = id.trim();
			}

			if(type != null && !type.isEmpty()){
				if(type.equalsIgnoreCase("First Level Preauth")){
					type = SHAConstants.FLP_CURRENT_QUEUE;
				} else if(type.equalsIgnoreCase("First Level Enhancement")){
					type = SHAConstants.FLE_CURRENT_QUEUE;
				}else{
					
				}
			}
			DBCalculationService dbCalculationService = new DBCalculationService();
			List<SearchHoldMonitorScreenTableDTO> holdClaims = dbCalculationService.getFLPHoldMonitorClaims(intimationNo, userId, type,menuType,cpuCode);
			
			Page<SearchHoldMonitorScreenTableDTO> page = new Page<SearchHoldMonitorScreenTableDTO>();
			page.setPageItems(holdClaims);
			page.setTotalRecords(holdClaims.size());
			return page;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void updateStageInformation(SearchHoldMonitorScreenTableDTO dto,Preauth preauthObj){
		
		/*StageInformation stgInformation = new StageInformation();
		
		stgInformation.setIntimation(preauthObj.getIntimation());				
		stgInformation.setClaimType(preauthObj.getClaim().getClaimType());
		stgInformation.setStage(preauthObj.getStage());
		Status status = new Status();
		status.setKey(ReferenceTable.PREAUTH_RELEASE_STATUS_KEY);
		status.setProcessValue(ReferenceTable.PREAUTH_RELEASE_STATUS);
		stgInformation.setPreauth(preauthObj);
		stgInformation.setStatus(status);
		stgInformation.setClaim(preauthObj.getClaim());
		String strUserName = dto.getUsername();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		stgInformation.setCreatedBy(userNameForDB);
		//stgInformation.setCreatedBy(preauth.getModifiedBy());
		stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		stgInformation.setStatusRemarks(dto.getHoldRemarks());
		
		entityManager.persist(stgInformation);
		entityManager.flush();*/

		Claim claim = null;
		if(preauthObj != null){
			claim = preauthObj.getClaim();
		}else{
			Query query = entityManager.createNamedQuery("Claim.findByIntimationNo");
			query = query.setParameter("intimationNumber", dto.getIntimationNumber());
			claim = (Claim) query.getSingleResult();
		}
		StageInformation stgInformation = new StageInformation();
		stgInformation.setIntimation(claim.getIntimation());				
		stgInformation.setClaimType(claim.getClaimType());
		if(preauthObj !=null){
			stgInformation.setStage(preauthObj.getStage());
		}else{
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.PROCESS_PRE_MEDICAL);
			stgInformation.setStage(stage);
		}
		
		Status status = new Status();
		status.setKey(ReferenceTable.PREAUTH_RELEASE_STATUS_KEY);
		status.setProcessValue(ReferenceTable.PREAUTH_RELEASE_STATUS);
		stgInformation.setPreauth(preauthObj);
		stgInformation.setStatus(status);
		stgInformation.setClaim(claim);
		String strUserName = dto.getUsername();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		stgInformation.setCreatedBy(userNameForDB);
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
	
	public void updateStageInformation(Preauth preauth, PreauthDTO bean){

		Claim claim = null;
		if(preauth !=null){
			claim = preauth.getClaim();
		}else{
			Query query = entityManager.createNamedQuery("Claim.findByIntimationNo");
			query = query.setParameter("intimationNumber", bean.getNewIntimationDTO().getIntimationId());
			claim = (Claim) query.getSingleResult();
		}
		StageInformation stgInformation = new StageInformation();
		stgInformation.setIntimation(claim.getIntimation());				
		stgInformation.setClaimType(claim.getClaimType());
		if(preauth !=null){
			stgInformation.setStage(preauth.getStage());
		}else{
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.PROCESS_PRE_MEDICAL);
			stgInformation.setStage(stage);
		}
		
		Status status = new Status();
		status.setKey(ReferenceTable.PREAUTH_HOLD_STATUS_KEY);
		status.setProcessValue(ReferenceTable.PREAUTH_HOLD_STATUS);
		stgInformation.setPreauth(preauth);
		stgInformation.setStatus(status);
		stgInformation.setClaim(claim);
		String strUserName = bean.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		stgInformation.setCreatedBy(userNameForDB);
		stgInformation.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		stgInformation.setStatusRemarks(bean.getPreMedicalPreauthMedicalDecisionDetails().getHoldRemarks());

		entityManager.persist(stgInformation);
		entityManager.flush();
	}

}
