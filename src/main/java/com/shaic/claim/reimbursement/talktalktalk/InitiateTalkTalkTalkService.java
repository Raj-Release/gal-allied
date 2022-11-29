package com.shaic.claim.reimbursement.talktalktalk;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTableDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.RRCCategory;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TalkTalkTalk;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.ImplantDetails;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.fraudidentification.FraudIdentification;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationTableDTO;
import com.vaadin.ui.Notification;



@Stateless
public class InitiateTalkTalkTalkService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	public InitiateTalkTalkTalkService(){
		super();
	}
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private InitiateTalkTalkTalkService talkService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	
	public  Page<InitiateTalkTalkTalkTableDTO> search(InitiateTalkTalkTalkFormDTO searchFormDTO,String userName, String passWord) 

	{
		List<Claim> listIntimations = new ArrayList<Claim>(); 
		List<OMPClaim> listOMPIntimations = new ArrayList<OMPClaim>(); 
		List<OPClaim> listOPIntimations = new ArrayList<OPClaim>(); 
		try{
			String intimationNo = null != searchFormDTO.getIntimationNo() && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
			String year =  null != searchFormDTO.getIntimationyear() && searchFormDTO.getIntimationyear().getValue() != null && !searchFormDTO.getIntimationyear().getValue().isEmpty() ? searchFormDTO.getIntimationyear().getValue() : null;


			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
			final CriteriaQuery<OMPClaim> ompCriteriaQuery = criteriaBuilder.createQuery(OMPClaim.class);
			final CriteriaQuery<OPClaim> opCriteriaQuery = criteriaBuilder.createQuery(OPClaim.class);

			Root<Claim> root = criteriaQuery.from(Claim.class);
			Root<OMPClaim> ompRoot = ompCriteriaQuery.from(OMPClaim.class);
			Root<OPClaim> opRoot = opCriteriaQuery.from(OPClaim.class);

			List<Predicate> conditionList = new ArrayList<Predicate>();

			Intimation intimation =getIntimationByNo(intimationNo);
			OMPIntimation ompintimation =searchbyIntimationNo(intimationNo);
			OPIntimation opintimation =intimationService.getOPIntimationByNumber(intimationNo);
			if(intimation !=null){
				if(null != intimationNo)
				{
					System.out.println("Initmation*******************");
					Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
					conditionList.add(condition1);
				}
				if(null != year)
				{

					Predicate yearCondition = criteriaBuilder.equal(root.<Intimation>get("intimation").<Long>get("intimationYear"),Long.parseLong(year));
					conditionList.add(yearCondition);
				}

				Predicate closeStatus = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.CLAIM_CLOSED_STATUS);
				Predicate registeredStatus = criteriaBuilder.notEqual( root.<Status>get("status").<Long>get("key"),ReferenceTable.INTIMATION_REGISTERED_STATUS);
				Predicate statusCondition = criteriaBuilder.and(closeStatus,registeredStatus);
				conditionList.add(statusCondition);

				if (intimationNo == null && year == null) 
				{
					criteriaQuery.select(root);

				} 
				else 
				{
					criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
				

				final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
				listIntimations = typedQuery.getResultList();

				List<Claim> doList = listIntimations;
				List<InitiateTalkTalkTalkTableDTO> tableDTO = InitiateTalkTalkTalkMapper.getInstance().getInitiateTalkTalkTalkTableObjects(doList);
				tableDTO = getHospitalDetails(tableDTO);
				List<InitiateTalkTalkTalkTableDTO> result = new ArrayList<InitiateTalkTalkTalkTableDTO>();

				/*if(year != null && intimation.getIntimationId() != null){
				String[] intimationSp = intimation.getIntimationId().split("/");
				String intYear = intimationSp[1];
				if(intYear != null && year.equals(intYear)){
					result.addAll(tableDTO);
				}
			} else {
				result.addAll(tableDTO);
			}*/
				//result.addAll(tableDTO);
				Page<InitiateTalkTalkTalkTableDTO> page = new Page<InitiateTalkTalkTalkTableDTO>();			
				//	searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
				if(result.size()<=10)
				{
					page.setHasNext(false);
				}
				else
				{
					page.setHasNext(true);
				}
				if (result.isEmpty()) {
					//		searchFormDTO.getPageable().setPageNumber(1);
				}
				//	page.setPageNumber(pageNumber);
				page.setPageItems(tableDTO);
				page.setIsDbSearch(true);

				return page;

			}
			else if(ompintimation !=null){

				if(null != intimationNo)
				{
					System.out.println("OMPInitmation*******************");
					Predicate condition1 = criteriaBuilder.like(ompRoot.<OMPIntimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
					conditionList.add(condition1);
				}
				/*if(null != year)
				{

					Predicate yearCondition = criteriaBuilder.equal(ompRoot.<OMPIntimation>get("intimation").<Long>get("intimationYear"),Long.parseLong(year));
					conditionList.add(yearCondition);
				}*/

				Predicate closeStatus = criteriaBuilder.notEqual( ompRoot.<Status>get("status").<Long>get("key"),ReferenceTable.CLAIM_CLOSED_STATUS);
				Predicate registeredStatus = criteriaBuilder.notEqual( ompRoot.<Status>get("status").<Long>get("key"),ReferenceTable.INTIMATION_REGISTERED_STATUS);
				Predicate statusCondition = criteriaBuilder.and(closeStatus,registeredStatus);
				conditionList.add(statusCondition);

				if (intimationNo == null && year == null) 
				{
					ompCriteriaQuery.select(ompRoot);

				} 
				else 
				{
					ompCriteriaQuery.select(ompRoot).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}

				final TypedQuery<OMPClaim> typedQuery = entityManager.createQuery(ompCriteriaQuery);
				listOMPIntimations = typedQuery.getResultList();

				List<OMPClaim> doList = listOMPIntimations;
				List<InitiateTalkTalkTalkTableDTO> tableDTO = InitiateTalkTalkTalkMapper.getInstance().getInitiateTalkTalkTalkTableOMPObjects(doList);
				tableDTO = getHospitalDetails(tableDTO);
				
				List<InitiateTalkTalkTalkTableDTO> result = new ArrayList<InitiateTalkTalkTalkTableDTO>();
				
				if(year != null && ompintimation.getIntimationId() != null){
					String[] intimationSp = ompintimation.getIntimationId().split("/");
					String intYear = intimationSp[1];
					if(intYear != null && year.equals(intYear)){
						result.addAll(tableDTO);
					}
				} else {
					result.addAll(tableDTO);
				}
				
				Page<InitiateTalkTalkTalkTableDTO> page = new Page<InitiateTalkTalkTalkTableDTO>();			
				if(result.size()<=10)
				{
					page.setHasNext(false);
				}
				else
				{
					page.setHasNext(true);
				}
				if (result.isEmpty()) {
				}
				page.setPageItems(tableDTO);
				page.setIsDbSearch(true);

				return page;

			}else if(opintimation!=null){
				if(null != intimationNo)
				{
					System.out.println("OPInitmation*******************");
					Predicate condition1 = criteriaBuilder.like(opRoot.<OPIntimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
					conditionList.add(condition1);
				}
				/*if(null != year)
				{

					Predicate yearCondition = criteriaBuilder.equal(opRoot.<OPIntimation>get("intimation").<Long>get("intimationYear"),Long.parseLong(year));
					conditionList.add(yearCondition);
				}*/

				Predicate closeStatus = criteriaBuilder.notEqual( opRoot.<Status>get("status").<Long>get("key"),ReferenceTable.CLAIM_CLOSED_STATUS);
				Predicate registeredStatus = criteriaBuilder.notEqual( opRoot.<Status>get("status").<Long>get("key"),ReferenceTable.INTIMATION_REGISTERED_STATUS);
				Predicate statusCondition = criteriaBuilder.and(closeStatus,registeredStatus);
				conditionList.add(statusCondition);

				if (intimationNo == null && year == null) 
				{
					opCriteriaQuery.select(opRoot);

				} 
				else 
				{
					opCriteriaQuery.select(opRoot).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}

				final TypedQuery<OPClaim> typedQuery = entityManager.createQuery(opCriteriaQuery);
				listOPIntimations = typedQuery.getResultList();

				List<OPClaim> doList = listOPIntimations;
				List<InitiateTalkTalkTalkTableDTO> tableDTO = InitiateTalkTalkTalkMapper.getInstance().getInitiateTalkTalkTalkTableOPObjects(doList);
				tableDTO = getHospitalDetails(tableDTO);
				List<InitiateTalkTalkTalkTableDTO> result = new ArrayList<InitiateTalkTalkTalkTableDTO>();

				if(year != null && opintimation.getIntimationId() != null){
					String[] intimationSp = opintimation.getIntimationId().split("/");
					String intYear = intimationSp[1];
					if(intYear != null && year.equals(intYear)){
						result.addAll(tableDTO);
					}
				} else {
					result.addAll(tableDTO);
				}
				//result.addAll(tableDTO);
				Page<InitiateTalkTalkTalkTableDTO> page = new Page<InitiateTalkTalkTalkTableDTO>();			
				//	searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
				if(result.size()<=10)
				{
					page.setHasNext(false);
				}
				else
				{
					page.setHasNext(true);
				}
				if (result.isEmpty()) {
					//		searchFormDTO.getPageable().setPageNumber(1);
				}
				//	page.setPageNumber(pageNumber);
				page.setPageItems(tableDTO);
				page.setIsDbSearch(true);

				return page;
			}


		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;	
	}




	private List<InitiateTalkTalkTalkTableDTO> getHospitalDetails(List<InitiateTalkTalkTalkTableDTO> tableDTO)
	{

		for(int index = 0; index < tableDTO.size(); index++)
		{
			Hospitals hospitalDetail = getHospitalDetail(tableDTO.get(index).getHospitalNameId());
			if(hospitalDetail != null)
			{
				tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				tableDTO.get(index).setHospitalCity(hospitalDetail.getCity());


			}

		}
		return tableDTO;
	}


	private Hospitals getHospitalDetail(Long hospitalId){
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hospitalId);
		Hospitals hospitalDetail;
		try{

			hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			return hospitalDetail;
		}
		catch(Exception e){
			return null;
		}
	}

	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursementByKey(Long rodKey){
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey").setParameter(
						"primaryKey", rodKey);
		List<Reimbursement> rodList = query.getResultList();

		if(rodList != null && !rodList.isEmpty()) {
			for (Reimbursement reimbursement : rodList) {
				entityManager.refresh(reimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	} 


	private Long getRRCRequestCountByClaim(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("RRCRequest.CountAckByClaimKey");
		query.setParameter("claimkey", claimKey);
		Long countOfRRCReq = (Long) query.getSingleResult();
		return countOfRRCReq;
	}

	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claim = (List<Claim>)query.getResultList();

		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}

	public TmpEmployee getEmployeeByID(String empId) {

		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpId");
		query.setParameter("empId", empId.toLowerCase());
		List<TmpEmployee> resultList = (List<TmpEmployee>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			entityManager.refresh(resultList.get(0));
			return resultList.get(0);
		}
		return null;
	}
	
	public void saveInitiateTalkTalkTalkDetails(PreauthDTO preauthDTO){
		List<InitiateTalkTalkTalkDTO> initiateTalkTalkTalkDTOList = preauthDTO.getInitiateTalkTalkTalkDTOList();

		if(initiateTalkTalkTalkDTOList !=null && !initiateTalkTalkTalkDTOList.isEmpty()) {
			for (InitiateTalkTalkTalkDTO talkDTO: initiateTalkTalkTalkDTOList) {		
				
				TalkTalkTalk talkTalkTalk = getTalkTalkTalkbyTalkKey(talkDTO.getKey());
				/*Intimation intimation = null;
				intimation = intimationService.getIntimationByKey(preauthDTO.getIntimationKey());*/
				
				if (talkTalkTalk == null) {
					talkTalkTalk = new TalkTalkTalk();
					talkTalkTalk.setIntimationKey(preauthDTO.getIntimationKey());;
					talkTalkTalk.setIntimationNumber(preauthDTO.getIntimationNumber());
					SelectValue typeofComm = talkDTO.getTypeOfCommunication();
					if (typeofComm != null) {
						MastersValue mastersValue = new MastersValue();
						mastersValue.setKey(typeofComm.getId());
						mastersValue.setValue(typeofComm.getValue());
						talkTalkTalk.setTypeOfCommunication(mastersValue);
					}
					talkTalkTalk.setSpokenTo(talkDTO.getTalkSpokento());
					talkTalkTalk.setDateAndTimeofCall(talkDTO.getTalkSpokenDate());
					talkTalkTalk.setContactNumber(Long.parseLong(talkDTO.getTalkMobto()));
					talkTalkTalk.setRemarks(talkDTO.getRemarks());
					talkTalkTalk.setProcessingUserName(talkDTO.getProcessingUserName());
					talkTalkTalk.setActiveStatus(1L);	
					talkTalkTalk.setCreatedBy(preauthDTO.getStrUserName());
					talkTalkTalk.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					if(talkDTO.getCallHitRefNo() != null && !talkDTO.getCallHitRefNo().isEmpty()){
					talkTalkTalk.setReferenceId(talkDTO.getCallHitRefNo());
					}
					if(talkDTO.getEndCallConvoxId() != null && !talkDTO.getEndCallConvoxId().isEmpty()){
					talkTalkTalk.setConvoxId(Long.valueOf(talkDTO.getEndCallConvoxId()));
					}
					entityManager.persist(talkTalkTalk);
					talkDTO.setKey(talkTalkTalk.getTalkKey());
				}
				else {
					
					talkTalkTalk.setIntimationKey(preauthDTO.getIntimationKey());;
					talkTalkTalk.setIntimationNumber(preauthDTO.getIntimationNumber());
					SelectValue typeofComm = talkDTO.getTypeOfCommunication();
					if (typeofComm != null) {
						MastersValue mastersValue = new MastersValue();
						mastersValue.setKey(typeofComm.getId());
						mastersValue.setValue(typeofComm.getValue());
						talkTalkTalk.setTypeOfCommunication(mastersValue);
					}

					talkTalkTalk.setSpokenTo(talkDTO.getTalkSpokento());
					talkTalkTalk.setDateAndTimeofCall(talkDTO.getTalkSpokenDate());
					talkTalkTalk.setContactNumber(Long.parseLong(talkDTO.getTalkMobto()));
					talkTalkTalk.setRemarks(talkDTO.getRemarks());
					talkTalkTalk.setProcessingUserName(talkDTO.getProcessingUserName());
					talkTalkTalk.setActiveStatus(1L);	
					talkTalkTalk.setModifiedBy(preauthDTO.getStrUserName());
					talkTalkTalk.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					if(talkDTO.getCallHitRefNo() != null){
					talkTalkTalk.setReferenceId(talkDTO.getCallHitRefNo());
					}
					if(talkDTO.getEndCallConvoxId() != null && !talkDTO.getEndCallConvoxId().isEmpty()){
					talkTalkTalk.setConvoxId(Long.valueOf(talkDTO.getEndCallConvoxId()));
					}
					entityManager.merge(talkTalkTalk);
				}
				entityManager.flush();
				entityManager.clear();		
			}
		}
}

	
	public TalkTalkTalk getTalkTalkTalkbyClaimKey(Long claimKey){
		Query  query = entityManager.createNamedQuery("TalkTalkTalk.findByClaimKey");
		query = query.setParameter("claimKey",claimKey);
		List<TalkTalkTalk> listOfObj = query.getResultList();
		
		if(listOfObj != null && !listOfObj.isEmpty()){
			return listOfObj.get(0);
		}
		return null;
	}
	
	public TalkTalkTalk getTalkTalkTalkbyTalkKey(Long talkKey){
		Query  query = entityManager.createNamedQuery("TalkTalkTalk.findByKey");
		query = query.setParameter("talkKey",talkKey);
		List<TalkTalkTalk> listOfObj = query.getResultList();
		
		if(listOfObj != null && !listOfObj.isEmpty()){
			return listOfObj.get(0);
		}
		return null;
	}
	
	
	public Reimbursement getRODObjectByClaimKey(Long claimKey)
	{
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey").setParameter(
						"claimKey", claimKey);
		List<Reimbursement> rodList = query.getResultList();
		Reimbursement reimbursementObj = null;
		if(null != rodList && !rodList.isEmpty())
		{
			reimbursementObj = rodList.get(0);
		}
		return reimbursementObj;
		//entityManager.refresh(reimbursement);
		
	}


	 
	 public List<TalkTalkTalk> getTalkTalkTalkDetailsByIntimationKeyList(Long intimationKey) {
			Query query = entityManager
					.createNamedQuery("TalkTalkTalk.findByIntimationKey");
			query = query.setParameter("intimationKey",intimationKey);

			List<TalkTalkTalk> talkTalkTalkDetails = query.getResultList();
			if (talkTalkTalkDetails != null && !talkTalkTalkDetails.isEmpty()) {
				return talkTalkTalkDetails;
			}

			return talkTalkTalkDetails;
		}
	 
	 public List<TalkTalkTalk> getTalkTalkTalkDetailsByIntimationNumList(String intimationNumber) {
			Query query = entityManager
					.createNamedQuery("TalkTalkTalk.findByIntimationNumber");
			query = query.setParameter("intimationNumber",intimationNumber);

			List<TalkTalkTalk> talkTalkTalkDetails = query.getResultList();
			if (talkTalkTalkDetails != null && !talkTalkTalkDetails.isEmpty()) {
				return talkTalkTalkDetails;
			}

			return talkTalkTalkDetails;
		}
	 
	 @SuppressWarnings("unchecked")
		public Intimation getIntimationByNo(String intimationNo) {
		 
		 Query findByKey = entityManager.createNamedQuery(
					"Intimation.findByIntimationId").setParameter(
							"intimationNo",  "%"+intimationNo.toLowerCase()+"%");

			List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();
			if (!intimationList.isEmpty()) {
				entityManager.refresh(intimationList.get(0));
				return intimationList.get(0);

			}
			return null;
		}
	 
	 @SuppressWarnings("unchecked")
		public OMPIntimation searchbyIntimationNo(String intimationNo) {
			OMPIntimation intimation = null;
			if (null != intimationNo &&  !("").equalsIgnoreCase(intimationNo)) {

				Query findByIntimNo = entityManager.createNamedQuery(
						"OMPIntimation.findByOMPIntimationNumber").setParameter(
						"intimationNo", "%"+intimationNo+"%");
				try {
					//findByIntimNo.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
					List<OMPIntimation> intimationQueryResultList = (List<OMPIntimation>) findByIntimNo
							.getResultList();
					if (intimationQueryResultList != null
							&& !intimationQueryResultList.isEmpty()) {
						intimation = (OMPIntimation) intimationQueryResultList.get(0);
						entityManager.refresh(intimation);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			return intimation;
		}
	 
	 @SuppressWarnings("unchecked")
		public Claim getClaimByIntimationNo(String intimationNo) {
			Query findByKey = entityManager.createNamedQuery("Claim.findByIntimationNo").setParameter("intimationNumber", intimationNo);
			List<Claim> claims = (List<Claim>) findByKey.getResultList();
			if (!claims.isEmpty()) {
				entityManager.refresh(claims.get(0));
				return claims.get(0);
			}
			return null;
		}
	 
	 

		public List<InitiateTalkTalkTalkDTO> setViewTalkTalkTalkTableValuesHosp(String initmationNumber)
		{

			List<InitiateTalkTalkTalkDTO> finalTableDTOList = new ArrayList<InitiateTalkTalkTalkDTO>();
			//Claim claim =getClaimByIntimationNo(initmationNumber);
			//Intimation intimation =getIntimationByNo(initmationNumber);

			if(initmationNumber !=null){

				List<TalkTalkTalk> talkTalkTalkViewDetailsList = getTalkTalkTalkDetailsByIntimationNumList(initmationNumber);

				if(null != talkTalkTalkViewDetailsList){

					InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO = null;

					for (TalkTalkTalk talkTalkTalk : talkTalkTalkViewDetailsList) {

						if(talkTalkTalk.getTypeOfCommunication() !=null && talkTalkTalk.getTypeOfCommunication().getKey().equals(11212l)){
						initiateTalkTalkTalkDTO = new InitiateTalkTalkTalkDTO();
						if(talkTalkTalk.getTypeOfCommunication() !=null){
							MastersValue masterValue = talkTalkTalk.getTypeOfCommunication();
							SelectValue typeOfComm=new SelectValue();
							typeOfComm.setValue(masterValue.getValue());
							typeOfComm.setId(masterValue.getKey());
							initiateTalkTalkTalkDTO.setTypeOfCommunication(typeOfComm);

						}
						if(talkTalkTalk.getDateAndTimeofCall() !=null){
							initiateTalkTalkTalkDTO.setTalkSpokenDate(talkTalkTalk.getDateAndTimeofCall());
						}

						if(talkTalkTalk.getContactNumber() !=null){
							initiateTalkTalkTalkDTO.setTalkMobto(talkTalkTalk.getContactNumber().toString());
						}
						if(talkTalkTalk.getSpokenTo() !=null){
							initiateTalkTalkTalkDTO.setTalkSpokento(talkTalkTalk.getSpokenTo());
						}
						if(talkTalkTalk.getRemarks() !=null){
							initiateTalkTalkTalkDTO.setRemarks(talkTalkTalk.getRemarks());
						}
						if(talkTalkTalk.getProcessingUserName() !=null){
							initiateTalkTalkTalkDTO.setProcessingUserName(talkTalkTalk.getProcessingUserName());
						}
						
//						initiateTalkTalkTalkDTO.setFileName("http://192.168.4.157:8157/calls/2022-05-19/out/6385132233_TVC_TVCQ_VP040105_manual_1009_20220519104103.wav");
						if(talkTalkTalk.getReferenceId() != null){
							DialerStatusLog dialerStatus = talkService.getDialerStatusLog(talkTalkTalk.getReferenceId(),talkTalkTalk.getConvoxId().toString());
							if(dialerStatus != null){
								/*String callstartDateTime = dialerStatus.getCallDate().concat(" ").concat(dialerStatus.getCallHour()).concat(":").concat(dialerStatus.getCallMin());
								Long callMins = Long.valueOf(dialerStatus.getCallMin());
								String callDurationSplit[] = dialerStatus.getCallDuration().split(":");
								String hour = callDurationSplit[0];
								String min = callDurationSplit[1];
								Long endcallmin = callMins+Long.valueOf(min);
								Long endCallHour = 0l;
								if(endcallmin > 60){
									endcallmin = endcallmin - 60;
									if(10 < endcallmin){
										
									} else {
										String zeromin = "0";
										String mins = zeromin.concat(endcallmin.toString());
										endcallmin = Long.valueOf(mins);
									}
									endCallHour = Long.valueOf(dialerStatus.getCallHour())+1;
								} else {
									endCallHour = Long.valueOf(dialerStatus.getCallHour());
								}
								String endCall = dialerStatus.getCallDate().concat(" ").concat(endCallHour.toString()).concat(":").concat(endcallmin.toString());*/
								initiateTalkTalkTalkDTO = dbCalculationService.getDailerEndCallDateTime(dialerStatus.getCallReferenceId(), initiateTalkTalkTalkDTO);
								String starTime = SHAUtils.formateDateForDialerHistory(initiateTalkTalkTalkDTO.getDialerCallStartTime());
								String endTime = SHAUtils.formateDateForDialerHistory(initiateTalkTalkTalkDTO.getDialerCallEndTime());
								initiateTalkTalkTalkDTO.setCallStartTime(starTime);
								initiateTalkTalkTalkDTO.setCallEndTime(endTime);
								initiateTalkTalkTalkDTO.setCallDuration(dialerStatus.getCallDuration());
								initiateTalkTalkTalkDTO.setFileName(dialerStatus.getFileName());
								
								
							}
						}

						finalTableDTOList.add(initiateTalkTalkTalkDTO);
					}
				}

				}	 
			}
			return finalTableDTOList;

		}
		
		public List<InitiateTalkTalkTalkDTO> setViewTalkTalkTalkTableValuesCustomer(String initmationNumber)
		{

			List<InitiateTalkTalkTalkDTO> finalTableDTOList = new ArrayList<InitiateTalkTalkTalkDTO>();
			//Claim claim =getClaimByIntimationNo(initmationNumber);
			//Intimation intimation =getIntimationByNo(initmationNumber);

			if(initmationNumber !=null){

				List<TalkTalkTalk> talkTalkTalkViewDetailsList = getTalkTalkTalkDetailsByIntimationNumList(initmationNumber);

				if(null != talkTalkTalkViewDetailsList){

					InitiateTalkTalkTalkDTO initiateTalkTalkTalkDTO = null;

					for (TalkTalkTalk talkTalkTalk : talkTalkTalkViewDetailsList) {
						if(talkTalkTalk.getTypeOfCommunication() !=null && talkTalkTalk.getTypeOfCommunication().getKey().equals(11222l)){
						initiateTalkTalkTalkDTO = new InitiateTalkTalkTalkDTO();
						if(talkTalkTalk.getTypeOfCommunication() !=null){
							MastersValue masterValue = talkTalkTalk.getTypeOfCommunication();
							SelectValue typeOfComm=new SelectValue();
							typeOfComm.setValue(masterValue.getValue());
							typeOfComm.setId(masterValue.getKey());
							initiateTalkTalkTalkDTO.setTypeOfCommunication(typeOfComm);

						}
						if(talkTalkTalk.getDateAndTimeofCall() !=null){
							initiateTalkTalkTalkDTO.setTalkSpokenDate(talkTalkTalk.getDateAndTimeofCall());
						}

						if(talkTalkTalk.getContactNumber() !=null){
							initiateTalkTalkTalkDTO.setTalkMobto(talkTalkTalk.getContactNumber().toString());
						}
						if(talkTalkTalk.getSpokenTo() !=null){
							initiateTalkTalkTalkDTO.setTalkSpokento(talkTalkTalk.getSpokenTo());
						}
						if(talkTalkTalk.getRemarks() !=null){
							initiateTalkTalkTalkDTO.setRemarks(talkTalkTalk.getRemarks());
						}
						if(talkTalkTalk.getProcessingUserName() !=null){
							initiateTalkTalkTalkDTO.setProcessingUserName(talkTalkTalk.getProcessingUserName());
						}
						
//						initiateTalkTalkTalkDTO.setFileName("http://192.168.4.157:8157/calls/2022-05-19/out/6385132233_TVC_TVCQ_VP040105_manual_1009_20220519104103.wav");
						if(talkTalkTalk.getReferenceId() != null){
							DialerStatusLog dialerStatus = talkService.getDialerStatusLog(talkTalkTalk.getReferenceId(),talkTalkTalk.getConvoxId().toString());
							if(dialerStatus != null){
								initiateTalkTalkTalkDTO = dbCalculationService.getDailerEndCallDateTime(dialerStatus.getCallReferenceId(), initiateTalkTalkTalkDTO);
								String starTime = SHAUtils.formateDateForDialerHistory(initiateTalkTalkTalkDTO.getDialerCallStartTime());
								String endTime = SHAUtils.formateDateForDialerHistory(initiateTalkTalkTalkDTO.getDialerCallEndTime());
								initiateTalkTalkTalkDTO.setCallStartTime(starTime);
								initiateTalkTalkTalkDTO.setCallEndTime(endTime);
								initiateTalkTalkTalkDTO.setCallDuration(dialerStatus.getCallDuration());
								initiateTalkTalkTalkDTO.setFileName(dialerStatus.getFileName());
								
								
							}
						}

						finalTableDTOList.add(initiateTalkTalkTalkDTO);
					}
				}

				}	 
			}
			return finalTableDTOList;

		}
		
		
		
		public OPClaim getOPClaimByKey(Long key) {
			Query query = entityManager.createNamedQuery("OPClaim.findByClaimKey");
			query.setParameter("claimKey", key);
			List<OPClaim> claim = (List<OPClaim>)query.getResultList();
			if(claim != null && ! claim.isEmpty()){
				return claim.get(0);
			}
			else{
				return null;
			}
		}
		
		@SuppressWarnings("unchecked")
		public OMPClaim getOMPClaimByKey(Long argClaimKey) {
			List<OMPClaim> resultClaim = null;
			if (argClaimKey != null) {
				Query findByIntimationNum = entityManager.createNamedQuery("OMPClaim.findOMPByClaimKey").setParameter("claimkey", argClaimKey);
				try {
					resultClaim = (List<OMPClaim>) findByIntimationNum.getResultList();
					if(resultClaim != null && !resultClaim.isEmpty()){
						return resultClaim.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		 public DialerStatusLog getDialerStatusLog(String callReferenceId,String conCallId) {
				Query query = entityManager
						.createNamedQuery("DialerStatusLog.findByRefId");
				query = query.setParameter("callReferenceId",callReferenceId);
				query.setParameter("conCallId",conCallId);
				List<DialerStatusLog> listOfObj = query.getResultList();
				
				if(listOfObj != null && !listOfObj.isEmpty()){
					return listOfObj.get(0);
				}
				return null;
		}
		
}


