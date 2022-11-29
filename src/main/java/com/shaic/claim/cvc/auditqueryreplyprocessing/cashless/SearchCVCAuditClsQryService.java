package com.shaic.claim.cvc.auditqueryreplyprocessing.cashless;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AuditCategory;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditProcessor;
import com.shaic.arch.table.AuditTeam;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.OMPProcessOmpClaimApprover.search.OMPProcessOmpClaimApproverTableDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimAuditQuery;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasClmAuditUserMapping;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverMapper;
import com.shaic.reimbursement.rod.acknowledgementdocumentreceiver.search.SearchAcknowledgementDocumentReceiverTableDTO;


@Stateless
public class SearchCVCAuditClsQryService extends AbstractDAO<Preauth>{
	
	@Inject
	private IntimationService intimationService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	public SearchCVCAuditClsQryService() {
		super();
	}
	
	
	@SuppressWarnings({ "null" })
	public Page<SearchCVCAuditActionTableDTO> search(SearchCVCAuditClsQryFormDTO searchQryFormDTO, String userName, String passWord) {
		
		List<SearchCVCAuditActionTableDTO> tableDtoList = new ArrayList<SearchCVCAuditActionTableDTO>();
		
		List<AuditDetails> auditDetailsList = new ArrayList<AuditDetails>();
			
		Integer totalRecords = 0; 
		try{

			List<ClaimAuditQuery>  auditQrylist = getCVCAuditPendingListByUserAndRoleClmType(searchQryFormDTO.getCpuCode(),searchQryFormDTO.getCpuListContainer().getItemIds()
					/*searchQryFormDTO.getUserRoleName().toLowerCase(),searchQryFormDTO.getUserId(),*/);
			if (null != auditQrylist && !auditQrylist.isEmpty()) {
			for (ClaimAuditQuery auditqry : auditQrylist) {
				auditDetailsList.add(auditqry.getAudit());
				}
			totalRecords = auditQrylist.size();
			}
			
			for (AuditDetails auditDetails : auditDetailsList) {
				
				SearchCVCAuditActionTableDTO tableDto = new SearchCVCAuditActionTableDTO();
				
				if (null != auditDetails && auditDetails.getClaimType().equalsIgnoreCase(SHAConstants.CASHLESS_CHAR)){
					Preauth cashless = getPreauthListByKey(auditDetails.getTransactionKey());
					tableDto.setTransactionKey(cashless.getKey());
					tableDto.setTransactionNumber(cashless.getPreauthId());
					tableDto.setClaimType(SHAConstants.CASHLESS_STRING);
					tableDto.setClaimKey(cashless.getClaim().getKey());
				}
				
				Intimation intimation = intimationService.getIntimationByKey(auditDetails.getIntimationKey());
				tableDto.setAuditKey(auditDetails.getKey());
				tableDto.setIntimationNumber(intimation.getIntimationId());
				tableDto.setIntimationKey(auditDetails.getIntimationKey());
				tableDto.setAuditBy(auditDetails.getCreatedBy());
				tableDto.setAuditDate(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(auditDetails.getCreatedDate()));
				if (null != auditQrylist && !auditQrylist.isEmpty()) {
					for (ClaimAuditQuery auditqry : auditQrylist) {
						if(auditqry.getAudit().getKey() == auditDetails.getKey()){
							tableDto.setClmAuditStatus(auditqry.getStatus());
							tableDto.setAuditQueryRaisedDtStr(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(auditqry.getFinalisedDate() != null ? auditqry.getFinalisedDate() : auditqry.getCreatedDate()));
						}
					}
					
				}
				tableDtoList.add(tableDto);
				
				if (null != tableDtoList) {
					
					Collections.sort(tableDtoList, new Comparator<SearchCVCAuditActionTableDTO>(){
						   public int compare(SearchCVCAuditActionTableDTO o1, SearchCVCAuditActionTableDTO o2){
							   int temp = 0;
						      if(o2.getAuditQueryRaisedDtStr()!= null && o1.getAuditQueryRaisedDtStr() != null && !o2.getAuditQueryRaisedDtStr().isEmpty() && !o1.getAuditQueryRaisedDtStr().isEmpty()) {
						       Long date1 = 0l;
						       Long date2 = 0l;
							try {
								date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o1.getAuditQueryRaisedDtStr()).getTime();
								date2 =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(o2.getAuditQueryRaisedDtStr()).getTime();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						     temp = date1.intValue() - date2.intValue(); 
						      }
						      return temp;
						   }
						});
				
				}
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Page<SearchCVCAuditActionTableDTO> page = new Page<SearchCVCAuditActionTableDTO>();
		page.setHasNext(true);
		page.setTotalRecords(totalRecords);
		page.setPageItems(tableDtoList);
		page.setIsDbSearch(false);
		
	return page;
	}
		
		
	@Override
	public Class<Preauth> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SearchCVCAuditClsQryTableDTO> getCVCAuditQryReplyPendingTeamWise(Long auditKey,String teamName) {
		
		List<SearchCVCAuditClsQryTableDTO> resultListDto = new ArrayList<SearchCVCAuditClsQryTableDTO>();
		List<ClaimAuditQuery> auditQrydetailsList = new ArrayList<ClaimAuditQuery>();
			try {
				Query query = entityManager.createNamedQuery(
						"ClaimAuditQuery.findByAuditKeyAndStatusTeamWise").setParameter("auditKey", auditKey);
				query.setParameter("teamName", teamName.toLowerCase());
				query.setParameter("status", SHAConstants.AUDIT_QUERY_REPLY_PENDING.toLowerCase());
				
				auditQrydetailsList = (List<ClaimAuditQuery>) query.getResultList();

				if (auditQrydetailsList != null && !auditQrydetailsList.isEmpty()) {
					
					SearchCVCAuditClsQryTableDTO auditQryDto = null;
					for (ClaimAuditQuery claimAuditQuery : auditQrydetailsList) {
						auditQryDto = new SearchCVCAuditClsQryTableDTO(claimAuditQuery);
						auditQryDto.setsNo(auditQrydetailsList.indexOf(claimAuditQuery)+1);
						resultListDto.add(auditQryDto);
					}	
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		return resultListDto;
	}

	public List<ClaimAuditQuery> getCVCAuditPendingListByUserAndRoleClmType(/*String strRole, String userId*/String cpuCode, List<SelectValue> cpuSelectList)
	{
		List<Long> cpuList = new ArrayList<Long>();
		List<ClaimAuditQuery> cvcAuditIntimationList = new ArrayList<ClaimAuditQuery>(); 
		
		try {
			if(cpuCode != null && !cpuCode.isEmpty()){
			
				cpuList = getListFromMultiSelectCpu(cpuCode);
			}	
			
			if(cpuList == null || cpuList.isEmpty()) {
				
				cpuList = getListFromMultiSelectCpu(cpuSelectList.toString());
			}	
					
			// cpuList for general
		/*	if(strRole != null) {
				cpuList = getClmAuditRplyUserCpuByRoleClmType(strRole,userId);
			}	*/
			
			if(cpuList != null && !cpuList.isEmpty()) {
				Query query = entityManager
						.createNamedQuery("ClaimAuditQuery.findFaQryByRemediationStatusAndCpu");
				
				query.setParameter("remediationStatus", SHAConstants.AUDIT_QUERY_REPLY_PENDING);
				query.setParameter("cpuCodeList", cpuList);
				query.setParameter("team", SHAConstants.AUDIT_TEAM_CASHLESS);
				cvcAuditIntimationList = (List<ClaimAuditQuery>) query.getResultList();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return cvcAuditIntimationList;

	}
	
	public List<Long> getClmAuditRplyUserCpuByRoleClmType(String strRole, String userId) {
		List<Long> cpuCodeLsit = new ArrayList<Long>();
		try {
			if(strRole != null) {
			
				Query query = entityManager
						.createNamedQuery("MasClmAuditUserMapping.findQryRplUserCPUByRoleClmProcessTypeUserID");
				
				query.setParameter("roleName", strRole.toLowerCase());
				query.setParameter("clmProcessType", SHAConstants.MEDICAL.toLowerCase());
				query.setParameter("clmType1", SHAConstants.CASHLESS.toLowerCase());
				query.setParameter("clmType2", SHAConstants.CASHLESS.toLowerCase());
				
				List<String> empIdList = getListFromMultiSelectComponent(userId);
				
				if(empIdList != null && !empIdList.isEmpty()) {
					List<MasClmAuditUserMapping> clmAuditRplUserLsit = null;
					for (String empId : empIdList) {
						query.setParameter("userId", empId);
						
						clmAuditRplUserLsit = (List<MasClmAuditUserMapping>) query.getResultList();
						
						if(clmAuditRplUserLsit !=null && !clmAuditRplUserLsit.isEmpty())
						{
							for (MasClmAuditUserMapping masClmAuditUserMapping : clmAuditRplUserLsit) {
								
								cpuCodeLsit.add(masClmAuditUserMapping.getCpuCode());
							}
							
						}
					}
				}				
		
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return cpuCodeLsit;
		
		
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
	
	public List<AuditDetails> getCVCAuditActionIntimationList()
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByRemediationStatus");
		query.setParameter("remediationStatus", SHAConstants.CVC_PENDING);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList;
		}

		return null;

	}
	
	@SuppressWarnings("unused")
	public void submitCVCAuditDetails(SearchCVCAuditActionTableDTO cvcTableDto){
		AuditDetails auditDetails = getCVCAuditActionIntimation(cvcTableDto.getIntimationKey());
		List<SearchCVCAuditClsQryTableDTO> clsQuryList = cvcTableDto.getClsQryList();
		
		if(auditDetails != null && clsQuryList != null && !clsQuryList.isEmpty()) {
			for (SearchCVCAuditClsQryTableDTO searchCVCAuditClsQryTableDTO : clsQuryList) {
				
				if(searchCVCAuditClsQryTableDTO.getQryStatus() != null
						&& SHAConstants.AUDIT_QUERY_REPLY_PENDING.equalsIgnoreCase(searchCVCAuditClsQryTableDTO.getQryStatus())) {
					ClaimAuditQuery clmAuditQry = findAudQryByKey(searchCVCAuditClsQryTableDTO.getQryKey());
					if(clmAuditQry != null) {
						clmAuditQry.setQueryReplyRemarks(searchCVCAuditClsQryTableDTO.getClsAuditQryReplyRemarks());
						clmAuditQry.setReplyDate(new Timestamp(System.currentTimeMillis()));
//						clmAuditQry.setReplyby(cvcTableDto.getUsername());
						clmAuditQry.setReplyby(cvcTableDto.getQryReplyBy());
						clmAuditQry.setReplyRole(cvcTableDto.getQryReplyRole());
						clmAuditQry.setStatus(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED);	
						clmAuditQry.setClaimsReply(searchCVCAuditClsQryTableDTO.getClaimsReply());
						entityManager.merge(clmAuditQry);
						
						
					}					
				}				
			}
			auditDetails.setCvcLockFlag(0);
			auditDetails.setLockedBy(null);
			auditDetails.setLockedDate(null);
			auditDetails.setRemediationStatus(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED);
			entityManager.merge(auditDetails);
		}		

	}
	
	public AuditDetails getCVCAuditActionIntimation(Long intimationKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByKey");
		query.setParameter("intimationKey", intimationKey);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<Intimation> getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNo").setParameter(
				"intimationNo", "%"+intimationNo+"%");

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			return intimationList;
		}
		return null;
	}
	
	public List<String> getListFromMultiSelectComponent(Object object){
		
		String string = object.toString();
		if(!string.equals("[]")){
			String temp[] = string.split(",");
			List<String> listOfString = new ArrayList<String>();
			listOfString.clear();
			for (int i = 0; i < temp.length; i++) {
				String val = temp[i].replaceAll("\\[", "").replaceAll("\\]", "");
				String temp1[] = val.split("-");
				listOfString.add(temp1[0].trim());
			}
			return listOfString;
		}
		return null;
		
	}
	
	public List<Long> getListFromMultiSelectCpu(Object object){
		
		String string = object.toString();
		if(!string.equals("[]")){
			String temp[] = string.split(",");
			List<Long> listOfString = new ArrayList<Long>();
			listOfString.clear();
			for (int i = 0; i < temp.length; i++) {
				String val = temp[i].replaceAll("\\[", "").replaceAll("\\]", "");
				String temp1[] = val.split("-");
				if(temp1.length >= 0){
				listOfString.add(Long.valueOf(temp1[0].trim()));
				}
			}
			return listOfString;
		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditTeam> getSelectedTeam(Long auditKey) {
		Query findByKey = entityManager
				.createNamedQuery("AuditTeam.findByAuditActiveStatus");
		findByKey.setParameter("auditKey", auditKey);

		List<AuditTeam> auditTeamList = (List<AuditTeam>) findByKey
				.getResultList();

		if (!auditTeamList.isEmpty()) {
			return auditTeamList;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditCategory> getSelectedCategory(Long auditKey) {
		Query findByKey = entityManager
				.createNamedQuery("AuditCategory.findByAuditActiveStatus");
		findByKey.setParameter("auditKey", auditKey);

		List<AuditCategory> auditCategoryList = (List<AuditCategory>) findByKey
				.getResultList();

		if (!auditCategoryList.isEmpty()) {
			return auditCategoryList;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<AuditProcessor> getSelectedProcessor(Long auditKey) {
		Query findByKey = entityManager
				.createNamedQuery("AuditProcessor.findByAuditActiveStatus");
		findByKey.setParameter("auditKey", auditKey);

		List<AuditProcessor> auditProcessorList = (List<AuditProcessor>) findByKey
				.getResultList();

		if (!auditProcessorList.isEmpty()) {
			return auditProcessorList;
		}
		return null;
	}
	
	public void updateCVCLockKey(SearchCVCAuditActionTableDTO key) {
		if(null != key){
			AuditDetails auditDetails = getCVCAuditActionKeyData(key);
			if(null != auditDetails){
				auditDetails.setCvcLockFlag(1);
				auditDetails.setLockedBy(key.getUsername());
				auditDetails.setLockedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(auditDetails);
				entityManager.flush();
				entityManager.clear();
			}
			
		}
	}
	
	public void updateCVCLockKeyForCancel(SearchCVCAuditActionTableDTO key) {
		if(null != key){
			AuditDetails auditDetails = getCVCAuditActionKeyData(key);
			if(null != auditDetails){
				auditDetails.setCvcLockFlag(0);
				auditDetails.setLockedBy(null);
				auditDetails.setLockedDate(null);
				entityManager.merge(auditDetails);
				entityManager.flush();
				entityManager.clear();
			}
			
		}
	}
	
	public AuditDetails getCVCAuditActionKeyData(SearchCVCAuditActionTableDTO auditKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByAuditKey");
		query.setParameter("key", auditKey.getAuditKey());

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}
	
	public ClaimAuditQuery findAudQryByKey(Long qryKey){
		Query query = entityManager.createNamedQuery("ClaimAuditQuery.findAudQryByKey")
				.setParameter("qryKey", qryKey);		
		List<ClaimAuditQuery> auditQrydetails =  (List<ClaimAuditQuery>) query.getResultList();

		if(auditQrydetails !=null && !auditQrydetails.isEmpty())
		{
			return auditQrydetails.get(0);
		}
		
		return null;
		
	}

}
