package com.shaic.reimbursement.investigationmaster;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import oracle.sql.DATE;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasInvZone;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.State;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpInvestigation;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationFormDTO;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationMapper;
import com.shaic.reimbursement.investigation.assigninvestigation.search.SearchAssignInvestigationTableDTO;

@Stateless
public class InvestigationMasterService  extends AbstractDAO<Intimation>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private InvestigationService investigationService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	
	public InvestigationMasterService() {
		super();
		
	}
	/***
	 * This menu search needs to be migrated to DB.
	 * As of now, to avoid bpmn references, commenting 
	 * below search. Who ever takes up this migration for
	 * this menu, please uncomment the below method and
	 * migrate the same. 
	 * 
	 */
	public  Page<InvestigationMasterTableDTO> search(InvestigationMasterFormDTO searchFormDTO,String userName, String passWord) {
		
		try{
			String investigatorType = null != searchFormDTO && null != searchFormDTO.getInvestigatorType() ? searchFormDTO.getInvestigatorType().getValue() != null ? searchFormDTO.getInvestigatorType().getValue(): null : null;
			Long investigatorName = null != searchFormDTO && searchFormDTO.getInvestigatorName() != null ? searchFormDTO.getInvestigatorName().getId() != null ? searchFormDTO.getInvestigatorName().getId(): null : null;

			Integer totalRecords = 0; 
			if(investigatorType!=null && investigatorType.equalsIgnoreCase("Private")){
				
				final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<MasPrivateInvestigator> criteriaQuery = criteriaBuilder.createQuery(MasPrivateInvestigator.class);
				
				List<MasPrivateInvestigator> tmpPrivateInvestigationList = new ArrayList<MasPrivateInvestigator>(); 
				
				Root<MasPrivateInvestigator> root = criteriaQuery.from(MasPrivateInvestigator.class);
				
				List<Predicate> conditionList = new ArrayList<Predicate>();
				
				/*if(investigatorType != null){
					Predicate condition1 = criteriaBuilder.equal(root.<MastersValue>get("allocationTo").<String>get("value"), investigatorType);
					conditionList.add(condition1);
					criteriaQuery.select(root).where(
							criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}*/
				
				if(investigatorName != null){
					Predicate condition1 = criteriaBuilder.equal(root.<Long>get("privateInvestigationKey"), investigatorName);
					conditionList.add(condition1);
					criteriaQuery.select(root).where(
							criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
				
				final TypedQuery<MasPrivateInvestigator> typedQuery = entityManager.createQuery(criteriaQuery);
				tmpPrivateInvestigationList = typedQuery.getResultList();
				
				List<InvestigationMasterTableDTO> tableDTO = InvestigationMasterMapper.getInstance().getPrivateIvestigationMasterDetails(tmpPrivateInvestigationList);
				for (InvestigationMasterTableDTO investigationMasterTableDTO : tableDTO) {
					investigationMasterTableDTO.setInvestigatorType("Private");
					if(investigationMasterTableDTO.getStatusToDisplay() !=null && investigationMasterTableDTO.getStatusToDisplay().equalsIgnoreCase("Y")){
						investigationMasterTableDTO.setStatusToDisplay("Active");
					}
					if(investigationMasterTableDTO.getStatusToDisplay() !=null && investigationMasterTableDTO.getStatusToDisplay().equalsIgnoreCase("N")){
						investigationMasterTableDTO.setStatusToDisplay("InActive");
					}

					if(investigationMasterTableDTO.getMobileNo()!=null){
						investigationMasterTableDTO.setMobileNo(String.valueOf(investigationMasterTableDTO.getMobileNo()));
					}
				}
			
				Pageable pageable = searchFormDTO.getPageable();
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
				

			
				Page<InvestigationMasterTableDTO> page = new Page<InvestigationMasterTableDTO>();
				page.setPageItems(tableDTO);
				//page.setTotalRecords(totalRecords);
				return page;
			}
			else{
				final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<TmpInvestigation> criteriaQuery = criteriaBuilder.createQuery(TmpInvestigation.class);
				
				List<TmpInvestigation> tmpInvestigationList = new ArrayList<TmpInvestigation>(); 
				
				Root<TmpInvestigation> root = criteriaQuery.from(TmpInvestigation.class);
				
				List<Predicate> conditionList = new ArrayList<Predicate>();
				
				if(investigatorType != null){
					Predicate condition1 = criteriaBuilder.equal(root.<MastersValue>get("allocationTo").<String>get("value"), investigatorType);
					conditionList.add(condition1);
					criteriaQuery.select(root).where(
							criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
				
				if(investigatorName != null){
					Predicate condition1 = criteriaBuilder.equal(root.<Long>get("key"), investigatorName);
					conditionList.add(condition1);
					criteriaQuery.select(root).where(
							criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
				
				final TypedQuery<TmpInvestigation> typedQuery = entityManager.createQuery(criteriaQuery);
				tmpInvestigationList = typedQuery.getResultList();
				
				List<InvestigationMasterTableDTO> tableDTO = InvestigationMasterMapper.getInstance().getIvestigationMasterDetails(tmpInvestigationList);
				for (InvestigationMasterTableDTO investigationMasterTableDTO : tableDTO) {
					
					if(investigationMasterTableDTO.getStatus() !=null && investigationMasterTableDTO.getStatus().toString().equalsIgnoreCase("1")){
						investigationMasterTableDTO.setStatusToDisplay("Active");
					}
					if(investigationMasterTableDTO.getStatus() !=null && investigationMasterTableDTO.getStatus().toString().equalsIgnoreCase("0")){
						investigationMasterTableDTO.setStatusToDisplay("InActive");
					}
					if(investigationMasterTableDTO.getMobileNo()!=null){
						investigationMasterTableDTO.setMobileNo(String.valueOf(investigationMasterTableDTO.getMobileNo()));
					}


				}
				Pageable pageable = searchFormDTO.getPageable();
				pageable.setPageNumber(1);
				pageable.setPageSize(BPMClientContext.ITEMS_PER_PAGE != null ? Integer.valueOf(BPMClientContext.ITEMS_PER_PAGE) : 10);
			
				

			
				Page<InvestigationMasterTableDTO> page = new Page<InvestigationMasterTableDTO>();
				page.setPageItems(tableDTO);
				//page.setTotalRecords(totalRecords);
				return page;
			}
		
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
		
			
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

	private TmpCPUCode getTmpCPUCode(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}


	
	public void saveInvestigatorMasterDetails(InvestigationMasterDTO invMasterDTO){

		if(invMasterDTO.getInvestigatorTypeSelectValue() !=null && invMasterDTO.getInvestigatorTypeSelectValue().getValue().equalsIgnoreCase("Private")){
			MasPrivateInvestigator privateInvestigator =null;
			if(invMasterDTO.getPrivateInvestigatorKey()!=null){
			privateInvestigator = investigationService.getPrivateInvestigatorByKey(invMasterDTO.getPrivateInvestigatorKey());
			}
			if (privateInvestigator == null) {
				privateInvestigator = new MasPrivateInvestigator();

				privateInvestigator.setInvestigatorName(invMasterDTO.getInvestigatorName());
				SelectValue gender = invMasterDTO.getGender();
				if (gender != null) {
					MastersValue genderMastersValue = new MastersValue();
					genderMastersValue.setKey(gender.getId());
					genderMastersValue.setValue(gender.getValue());

				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Male")){
					privateInvestigator.setGender("M");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Female")){
					privateInvestigator.setGender("F");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("TransGender")){
					privateInvestigator.setGender("T");
				}
				privateInvestigator.setMobileNumberOne(Long.valueOf(invMasterDTO.getMobileNumber()));
				if(invMasterDTO.getAdditionalMobileNumber()!=null){
					privateInvestigator.setMobileNumberTwo(Long.valueOf(invMasterDTO.getAdditionalMobileNumber()));
				}
				if(invMasterDTO.getPhoneNumber()!=null){
					privateInvestigator.setPhoneNumber(Long.valueOf(invMasterDTO.getPhoneNumber()));
				}

				if(invMasterDTO.getStatus()!=null){
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("true")){
						privateInvestigator.setActiveStatus("Y");
					}
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("false")){
						privateInvestigator.setActiveStatus("N");
					}
				}
				if(invMasterDTO.getInvestigationZoneName()!=null&& invMasterDTO.getInvestigationZoneName().getValue()!=null){
					privateInvestigator.setZoneName(invMasterDTO.getInvestigationZoneName().getValue());
				}
				privateInvestigator.setCordinatorName(invMasterDTO.getCoordinatorName());
				privateInvestigator.setCoridnatorCode(invMasterDTO.getCoordinatorCode());;
				privateInvestigator.setConsultancy(invMasterDTO.getConsultancyName());
				privateInvestigator.setEmailId(invMasterDTO.getEmailID());
				privateInvestigator.setContactPerson(invMasterDTO.getContactPerson());
				privateInvestigator.setToEmailId(invMasterDTO.getToEmailID());
				privateInvestigator.setCcEmailId(invMasterDTO.getCcEmailID());
				entityManager.persist(privateInvestigator);
				invMasterDTO.setKey(privateInvestigator.getPrivateInvestigationKey());

			}
			else {

				privateInvestigator.setInvestigatorName(invMasterDTO.getInvestigatorName());
				SelectValue gender = invMasterDTO.getGender();
				if (gender != null) {
					MastersValue genderMastersValue = new MastersValue();
					genderMastersValue.setKey(gender.getId());
					genderMastersValue.setValue(gender.getValue());

				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Male")){
					privateInvestigator.setGender("M");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Female")){
					privateInvestigator.setGender("F");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("TransGender")){
					privateInvestigator.setGender("T");
				}
				if(invMasterDTO.getInvestigationZoneName()!=null&& invMasterDTO.getInvestigationZoneName().getValue()!=null){
					privateInvestigator.setZoneName(invMasterDTO.getInvestigationZoneName().getValue());
				}
				privateInvestigator.setMobileNumberOne(Long.valueOf(invMasterDTO.getMobileNumber()));
				if(invMasterDTO.getAdditionalMobileNumber()!=null){
					privateInvestigator.setMobileNumberTwo(Long.valueOf(invMasterDTO.getAdditionalMobileNumber()));
				}
				if(invMasterDTO.getPhoneNumber() !=null){
					privateInvestigator.setPhoneNumber(Long.valueOf(invMasterDTO.getPhoneNumber()));
				}
				if(invMasterDTO.getStatus()!=null){
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("true")){
						privateInvestigator.setActiveStatus("Y");
					}
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("false")){
						privateInvestigator.setActiveStatus("N");
					}
				}
				privateInvestigator.setCordinatorName(invMasterDTO.getCoordinatorName());
				privateInvestigator.setCoridnatorCode(invMasterDTO.getCoordinatorCode());
				privateInvestigator.setConsultancy(invMasterDTO.getConsultancyName());
				privateInvestigator.setEmailId(invMasterDTO.getEmailID());
				privateInvestigator.setContactPerson(invMasterDTO.getContactPerson());
				privateInvestigator.setToEmailId(invMasterDTO.getToEmailID());
				privateInvestigator.setCcEmailId(invMasterDTO.getCcEmailID());

				entityManager.merge(privateInvestigator);
			}

			entityManager.flush();
			entityManager.clear();		
		}else{
			TmpInvestigation tmpInvestigation = investigationService.getTmpInvestigationByInvestigatorCodeWithoutStatus(invMasterDTO.getInvestigatorCode());
			if (tmpInvestigation == null) {
				tmpInvestigation = new TmpInvestigation();

				SelectValue investigatorType = invMasterDTO.getInvestigatorTypeSelectValue();
				if (investigatorType != null) {
					MastersValue mastersValue = new MastersValue();
					mastersValue.setKey(investigatorType.getId());
					mastersValue.setValue(investigatorType.getValue());
					tmpInvestigation.setAllocationTo(mastersValue);
				}
				String maxInvestigatorCode =dbCalculationService.getMaxInvestigatorCode();
				String id=getIdFromString(maxInvestigatorCode);
				Long value =Long.valueOf(id)+1;
				tmpInvestigation.setInvestigatorCode("INV-"+value);
				tmpInvestigation.setInvestigatorName(invMasterDTO.getInvestigatorName());
				SelectValue gender = invMasterDTO.getGender();
				if (gender != null) {
					MastersValue genderMastersValue = new MastersValue();
					genderMastersValue.setKey(gender.getId());
					genderMastersValue.setValue(gender.getValue());

				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Male")){
					tmpInvestigation.setGender("M");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Female")){
					tmpInvestigation.setGender("F");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("TransGender")){
					tmpInvestigation.setGender("");
				}
				if(invMasterDTO.getStatus()!=null){
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("true")){
						tmpInvestigation.setActiveStatus(1L);
					}
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("false")){
						tmpInvestigation.setActiveStatus(0L);
					}
				}
				tmpInvestigation.setMobileNumber(Long.valueOf(invMasterDTO.getMobileNumber()));
				if(invMasterDTO.getAdditionalMobileNumber()!=null){
					tmpInvestigation.setAdditionalMobileNumber(Long.valueOf(invMasterDTO.getAdditionalMobileNumber()));
				}
				if(invMasterDTO.getPhoneNumber()!=null){
					tmpInvestigation.setPhoneNumber(Long.valueOf(invMasterDTO.getPhoneNumber()));
				}
				CityTownVillage cityByKey = null;
				State stateByKey = null;
				cityByKey = masterService.getCityByKey(invMasterDTO.getCity().getId());
				stateByKey = masterService.getStateByKey(invMasterDTO.getState().getId());
				tmpInvestigation.setState(stateByKey);
				tmpInvestigation.setCityTownVillage(cityByKey);
				tmpInvestigation.setMaxCount(invMasterDTO.getAllocationCount());
				if(invMasterDTO.getBranchCode()!=null){
					tmpInvestigation.setBranchCode(invMasterDTO.getBranchCode());
				}
				tmpInvestigation.setStarEmployeeID(invMasterDTO.getStarEmployeeID());
				tmpInvestigation.setEmailID(invMasterDTO.getEmailID());
				entityManager.persist(tmpInvestigation);
				invMasterDTO.setKey(tmpInvestigation.getKey());
			}
			else {

				SelectValue investigatorType = invMasterDTO.getInvestigatorTypeSelectValue();
				if (investigatorType != null) {
					MastersValue mastersValue = new MastersValue();
					mastersValue.setKey(investigatorType.getId());
					mastersValue.setValue(investigatorType.getValue());
					tmpInvestigation.setAllocationTo(mastersValue);
				}
				tmpInvestigation.setInvestigatorName(invMasterDTO.getInvestigatorName());
				SelectValue gender = invMasterDTO.getGender();
				if (gender != null) {
					MastersValue genderMastersValue = new MastersValue();
					genderMastersValue.setKey(gender.getId());
					genderMastersValue.setValue(gender.getValue());

				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Male")){
					tmpInvestigation.setGender("M");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("Female")){
					tmpInvestigation.setGender("F");
				}
				if(gender.getValue()!=null && gender.getValue().equalsIgnoreCase("TransGender")){
					tmpInvestigation.setGender("");
				}
				
				if(invMasterDTO.getStatus()!=null){
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("true")){
						tmpInvestigation.setActiveStatus(1L);
					}
					if(invMasterDTO.getStatus().toString().equalsIgnoreCase("false")){
						tmpInvestigation.setActiveStatus(0L);
					}
				}
				tmpInvestigation.setMobileNumber(Long.valueOf(invMasterDTO.getMobileNumber()));
				if(invMasterDTO.getAdditionalMobileNumber()!=null){
					tmpInvestigation.setAdditionalMobileNumber(Long.valueOf(invMasterDTO.getAdditionalMobileNumber()));
				}
				if(invMasterDTO.getPhoneNumber() !=null){
					tmpInvestigation.setPhoneNumber(Long.valueOf(invMasterDTO.getPhoneNumber()));
				}
				CityTownVillage cityByKey = null;
				State stateByKey = null;
				cityByKey = masterService.getCityByKey(invMasterDTO.getCity().getId());
				stateByKey = masterService.getStateByKey(invMasterDTO.getState().getId());
				tmpInvestigation.setState(stateByKey);
				tmpInvestigation.setCityTownVillage(cityByKey);
				tmpInvestigation.setMaxCount(invMasterDTO.getAllocationCount());
				if(invMasterDTO.getBranchCode()!=null){
					tmpInvestigation.setBranchCode(invMasterDTO.getBranchCode());
				}
				tmpInvestigation.setStarEmployeeID(invMasterDTO.getStarEmployeeID());
				tmpInvestigation.setEmailID(invMasterDTO.getEmailID());
				entityManager.merge(tmpInvestigation);
			}

			entityManager.flush();
			entityManager.clear();		
		}
	}
	
	public  String getIdFromString(String InvestigatorId)
	{
		if(null != InvestigatorId)
		{
			String arr[] = InvestigatorId.split("-");
			return arr[1];
		}
		return null;
	}
	
}
