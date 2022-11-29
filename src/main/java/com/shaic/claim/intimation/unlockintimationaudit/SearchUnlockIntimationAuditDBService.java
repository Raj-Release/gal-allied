package com.shaic.claim.intimation.unlockintimationaudit;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.Page;
import com.shaic.domain.CVCStageHeader;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class SearchUnlockIntimationAuditDBService {

	@PersistenceContext
	protected EntityManager entityManager;

	private final Logger log = LoggerFactory.getLogger(SearchUnlockIntimationAuditDBService.class);

	public SearchUnlockIntimationAuditDBService(){

	}

	@SuppressWarnings("unchecked")
	public List<CVCStageHeader> getCVCStageByintiNO(String intimationNO) {
		Query query = entityManager.createNamedQuery("CVCStageHeader.findByIntimationIdandlock");
		query.setParameter("intimationNumber", intimationNO);	
		List<CVCStageHeader> singleResult = (List<CVCStageHeader>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			return singleResult;
		}		
		return null;				
	}
	
	@SuppressWarnings("unchecked")
	public CVCStageHeader setCVCStageByintiNO(String intimationNO) {
		Query query = entityManager.createNamedQuery("CVCStageHeader.findByIntimationIdandlock");
		query.setParameter("intimationNumber", intimationNO);	
		List<CVCStageHeader> singleResult = (List<CVCStageHeader>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			return singleResult.get(0);
		}		
		return null;				
	}

	public  Page<SearchUnlockIntimationAuditDBTableDTO> searchForUnlockIntimation(
			SearchUnlockIntimationAuditDBFormDTO formDTO,
			String userName, String passWord) throws NamingException {
		    String intimationNo = formDTO.getIntimationNo() != null ? formDTO.getIntimationNo() : null ;		
		    Page<SearchUnlockIntimationAuditDBTableDTO> page = new Page<SearchUnlockIntimationAuditDBTableDTO>();		
		    List<CVCStageHeader> cvcStageByintiNO = getCVCStageByintiNO(intimationNo);	
		    List<Intimation> intimationno = getByintimationNo(intimationNo);

		    List<SearchUnlockIntimationAuditDBTableDTO> commonList = new ArrayList<SearchUnlockIntimationAuditDBTableDTO>();
		    if(cvcStageByintiNO!=null){
				for(CVCStageHeader header:cvcStageByintiNO)
				{
			SearchUnlockIntimationAuditDBTableDTO searchUnlockIntimationTableDTO = new SearchUnlockIntimationAuditDBTableDTO();
			searchUnlockIntimationTableDTO.setIntimationNo(header.getIntimation() !=null ? header.getIntimation().getIntimationId():"");
			searchUnlockIntimationTableDTO.setLockedBy(header.getLockedBy());
			searchUnlockIntimationTableDTO.setStage(header.getStage() !=null ? header.getStage().getStageName() : "" );
			commonList.add(searchUnlockIntimationTableDTO);
		}
		    }
		    else if(intimationno!=null){
		    	for(Intimation intim:intimationno)
		    	{
		    List<AuditDetails> auditdetilsByintkey = getauditdetailsByintimationkey(intim.getKey());
		    if(auditdetilsByintkey!=null){
				for(AuditDetails details:auditdetilsByintkey)
				{
			SearchUnlockIntimationAuditDBTableDTO searchUnlockIntimationTableDTO = new SearchUnlockIntimationAuditDBTableDTO();
			searchUnlockIntimationTableDTO.setIntimationNo(intimationNo);
			searchUnlockIntimationTableDTO.setLockedBy(details.getLockedBy());
			List<CVCStageHeader> getstagename = getstagename(details.getTransactionKey());
			searchUnlockIntimationTableDTO.setStage(getstagename.get(0).getStage()!=null?getstagename.get(0).getStage().getStageName() : "" );
			commonList.add(searchUnlockIntimationTableDTO);
		}
		    }}}
		page.setPageItems(commonList);
		return page;
	}	

	@SuppressWarnings("unchecked")
	private List<Intimation> getByintimationNo(String intimationNo) {
		// TODO Auto-generated method stub
				Query query = entityManager.createNamedQuery("Intimation.findByIntimationNo");
				query.setParameter("intimationNo", intimationNo);	
				List<Intimation> singleResult = (List<Intimation>) query.getResultList();
				if(singleResult != null && ! singleResult.isEmpty()) {
					return singleResult;
				}		
				return null;
	}
	
	@SuppressWarnings("unchecked")
	private Intimation setByintimationNo(String intimationNo) {
		// TODO Auto-generated method stub
				Query query = entityManager.createNamedQuery("Intimation.findByIntimationNo");
				query.setParameter("intimationNo", intimationNo);	
				List<Intimation> singleResult = (List<Intimation>) query.getResultList();
				if(singleResult != null && ! singleResult.isEmpty()) {
					return singleResult.get(0);
				}		
				return null;
	}

	@SuppressWarnings("unchecked")
	private List<AuditDetails> getauditdetailsByintimationkey(Long intimationKey) {
		// TODO Auto-generated method stub
		Query query = entityManager.createNamedQuery("AuditDetails.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);	
		List<AuditDetails> singleResult = (List<AuditDetails>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			return singleResult;
		}		
		return null;
	}

	@SuppressWarnings("unchecked")
	private AuditDetails setauditdetailsByintimationkey(Long intimationKey) {
		// TODO Auto-generated method stub
		Query query = entityManager.createNamedQuery("AuditDetails.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);	
		List<AuditDetails> singleResult = (List<AuditDetails>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			return singleResult.get(0);
		}		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<CVCStageHeader> getstagename(Long transactionkey) {
		// TODO Auto-generated method stub
		Query query = entityManager.createNamedQuery("CVCStageHeader.findByTransactionKey");
		query.setParameter("transactionKey", transactionkey);	
		List<CVCStageHeader> singleResult = (List<CVCStageHeader>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			return singleResult;
		}		
		return null;
	}

	public  void unlockauditIntimation(String intimationNo,String Username){
		CVCStageHeader cvcStageByintiNO = setCVCStageByintiNO(intimationNo);
		Intimation intimKey= setByintimationNo(intimationNo);
	

		if(cvcStageByintiNO!=null){
			
			cvcStageByintiNO.setLockedBy(null);
			cvcStageByintiNO.setLockedDate(null);
			cvcStageByintiNO.setReleasedDate(new Timestamp(System.currentTimeMillis()));
			cvcStageByintiNO.setReleasedBy(Username);
			cvcStageByintiNO.setCvcLockFlag(0);
			entityManager.merge(cvcStageByintiNO);
			entityManager.flush();
			entityManager.clear();  
		}
		else if(intimKey!=null)
		{
		AuditDetails auditdetails = setauditdetailsByintimationkey(intimKey.getKey());
		if(auditdetails!=null){
			
			auditdetails.setLockedBy(null);
			auditdetails.setLockedDate(null);
			auditdetails.setReleasedDate(new Timestamp(System.currentTimeMillis()));
			auditdetails.setReleasedBy(Username);
			auditdetails.setCvcLockFlag(0);
			entityManager.merge(auditdetails);
			entityManager.flush();
			entityManager.clear();  
		}
		}
		
	}
}

