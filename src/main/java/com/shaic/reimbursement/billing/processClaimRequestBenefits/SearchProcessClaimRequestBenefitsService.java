/**
 * 
 */
package com.shaic.reimbursement.billing.processClaimRequestBenefits;

import java.util.ArrayList;
import java.util.List;

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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchProcessClaimRequestBenefitsService extends AbstractDAO<Claim>{

	@EJB
	private DBCalculationService dbCalculationService;
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	
	
	public SearchProcessClaimRequestBenefitsService() {
		super();
		
	}
	
	
	/***
	 * Search bpmn to db migration to be completed. 
	 * Temporarily commenting the same.
	 * */
	
	public  Page<SearchProcessClaimRequestBenefitsTableDTO> search(
			SearchProcessClaimRequestBenefitsFormDTO searchFormDTO,
			String userName, String passWord) {/*
		
		try{
			String intimationNo = !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
			String policyNo = !searchFormDTO.getPolicyNo().isEmpty() ? searchFormDTO.getPolicyNo() : null;
			Long insuredSource = searchFormDTO.getIntimationSource() !=null ? searchFormDTO.getIntimationSource().getId() : null;
			Long hospitaltype = searchFormDTO.getHospitalType()!=null ? searchFormDTO.getHospitalType().getId() : null;
			Long networkHospitalId = searchFormDTO.getNetworkHospType() !=null ? searchFormDTO.getNetworkHospType().getId() : null;
			Long treatementType = searchFormDTO.getTreatementType() !=null ? searchFormDTO.getTreatementType().getId() : null;
			
			
			
			HumanTask humanTaskDTO = null;
			Long rodKey;
			List<String> intimationNoList = new ArrayList<String>();
			List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			List<Long> rodKeyList = new ArrayList<Long>();
			PayloadBOType payloadBOType = new PayloadBOType();
			payloadBOType.getIntimation().setIntimationNumber(intimationNo);
			payloadBOType.getPolicy().setPolicyId(policyNo);
			payloadBOType.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
		
		//	ProcessClaimRequestTask processClaimRequest = BPMClientContext.getprocessClaimRequestTask(userName, passWord);
			
			//Pageable pageable = null;
			
			//PagedTaskList taskList = processClaimRequest.getTasks(userName,pageable,payloadBOType);
			
			
		
			if(null != processClaimRequest){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
					humanTaskListDTO.add(humanTask);
					rodKeyList.add(payloadBO.getRod().getKey());
					
				}
			}
			
			//Hardcoding the intimation no list , since bpm services are not up.
			
			intimationNoList.add("CLI/2015/111215/0000928");
			rodKeyList.add(5155l);
			List<SearchProcessClaimRequestBenefitsTableDTO> tableDTO = new ArrayList<SearchProcessClaimRequestBenefitsTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
				
				// humanTaskDTO = humanTaskListDTO.get(index);
				 
				 rodKey = rodKeyList.get(index);
				 
				 
			
				 tableDTO.addAll(getIntimationData(intimationNo, rodKey,  humanTaskDTO ));
				 
			}
		
			Page<SearchProcessClaimRequestBenefitsTableDTO> page = new Page<SearchProcessClaimRequestBenefitsTableDTO>();
			page.setPageItems(tableDTO);
			return page;
			}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
			
	*/
		return null;
		}

	
	private List<SearchProcessClaimRequestBenefitsTableDTO> getIntimationData(String intimationNo,  Long rodKey){
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Claim> criteriaQuery = criteriaBuilder.createQuery(Claim.class);
	
		List<Claim> claimList = new ArrayList<Claim>(); 
		
		Root<Claim> root = criteriaQuery.from(Claim.class);
	
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(intimationNo != null){
			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<String>get("intimationId"), intimationNo);
			conditionList.add(condition1);
		}
		try{
		if(!intimationNo.isEmpty() || intimationNo != null){
			criteriaQuery.select(root).where(
			criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			final TypedQuery<Claim> typedQuery = entityManager.createQuery(criteriaQuery);
			claimList = typedQuery.getResultList();
		}
			
	
			for(Claim inti:claimList){
			System.out.println(inti.getIntimation().getIntimationId()+"oooooooooooooooooooooooooo"+inti.getIntimation().getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			}
			
			List<SearchProcessClaimRequestBenefitsTableDTO> tableDTO = SearchProcessClaimRequestBenefitsMapper.getInstance().getClaimDTO(claimList);
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			tableDTO = getSpecilityAndTreatementType(tableDTO, rodKey);
			tableDTO = getHospitalDetails(tableDTO, rodKey);
	
				return tableDTO;
		}catch(Exception e){
			return null;
		}
	}

	

	@Override
	public Class<Claim> getDTOClass() {
		// TODO Auto-generated method stub
		return Claim.class;
	}
	

	private List<SearchProcessClaimRequestBenefitsTableDTO> getHospitalDetails(
			List<SearchProcessClaimRequestBenefitsTableDTO> tableDTO, Long rodKey) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 System.out.print(rodKey);
				 tableDTO.get(index).setRodKey(rodKey);
			//	 tableDTO.get(index).setHumanTaskDTO(humanTask);
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 tableDTO.get(index).setCpuName(getTmpCPUName(tableDTO.get(index).getCpuId()).getDescription());
				 
				 Claim claimByKey = getClaimByKey(tableDTO.get(index).getKey());
				if(claimByKey != null) {
					tableDTO.get(index).setCoverCode(claimByKey.getClaimCoverCode());
					 tableDTO.get(index).setSubCoverCode(claimByKey.getClaimSubCoverCode());
					 tableDTO.get(index).setSectionCode(claimByKey.getClaimSectionCode());
				}
				 
				 
				 tableDTO.get(index).setBalanceSI(getBalanceSI(tableDTO.get(index).getPolicyKey(),tableDTO.get(index).getInsuredKey() ,
				 tableDTO.get(index).getClaimKey(),tableDTO.get(index).getSumInsured(),tableDTO.get(index).getIntimationKey(),tableDTO.get(index).getSubCoverCode()));
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
	}
	
	

	private TmpCPUCode getTmpCPUName(Long cpuId){
		try{
		Query findCpuCode = entityManager.createNamedQuery("TmpCPUCode.findByKey").setParameter("cpuId", cpuId);
		 TmpCPUCode tmpCPUCode = (TmpCPUCode) findCpuCode.getSingleResult();
		return tmpCPUCode;
		}catch(Exception e){
			
		}
		return null;
	}
	private Double getBalanceSI(Long policyKey, Long insuredKey, Long claimKey,
			Double sumInsured,Long intimationKey, String subCover){
		Double balanceSI = 0.0;
		System.out.println("policyKey----------"+policyKey+"-------------insuredkey----------"+insuredKey+"-----------cliamkey--------"+claimKey+"");
		if(policyKey !=null && insuredKey != null && sumInsured != null){
			
			Claim claimObj = getClaimByKey(claimKey);
			if(null != claimObj.getIntimation() && null != claimObj.getIntimation().getPolicy() && 
					null != claimObj.getIntimation().getPolicy().getProduct() && 
					null != claimObj.getIntimation().getPolicy().getProduct().getKey() &&
					!(ReferenceTable.getGPAProducts().containsKey(claimObj.getIntimation().getPolicy().getProduct().getKey()))){
			
				balanceSI = dbCalculationService.getBalanceSI(policyKey ,insuredKey, claimKey, sumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI);
			}
			else
			{
				balanceSI = dbCalculationService.getGPABalanceSI(policyKey ,insuredKey, claimKey, sumInsured,intimationKey).get(SHAConstants.TOTAL_BALANCE_SI);
			}
		}
				return balanceSI;
		
	}
	private List<SearchProcessClaimRequestBenefitsTableDTO> getSpecilityAndTreatementType(List<SearchProcessClaimRequestBenefitsTableDTO> intimationList, Long rodKey){
		
		for(int index = 0; index < intimationList.size(); index++){
			System.out.println("Intimationkey+++++++++++++++++++++"+intimationList.get(index).getIntimationKey());
						
						
							intimationList.get(index).setSpeciality(getSpecialityType(intimationList.get(index).getClaimKey()));
							intimationList.get(index).setTreatementType(getTreatementType(getAcknowledgementKey(rodKey)));
							intimationList.get(index).setRequestedAmt(getRequestAmt(rodKey));

		}
		return intimationList;
	}
	private String getSpecialityType(Long claimKey){
		try{
			StringBuffer specilityValue =new StringBuffer();
			Query findCpuCode = entityManager.createNamedQuery("Speciality.findByClaimKey").setParameter("claimKey", claimKey);
			List<Speciality> SpecialityList = findCpuCode.getResultList();
			for(Speciality speciality : SpecialityList){ 
				Query findSpecilty = entityManager.createNamedQuery("SpecialityType.findByKey").setParameter("key", speciality.getSpecialityType().getKey());
				SpecialityType result = (SpecialityType) findSpecilty.getSingleResult(); 
				specilityValue.append(result.getValue()).append(", ");
			}
			
			return specilityValue.toString();
			}catch(Exception e){
				return null;
			}
		
	}
	
	private String getTreatementType(Long ackKey){
		try{
			StringBuffer specilityValue = new StringBuffer();
			Query findCpuCode = entityManager.createNamedQuery("Reimbursement.findByAcknowledgement").setParameter("docAcknowledgmentKey", ackKey);
			List<Reimbursement> SpecialityList = findCpuCode.getResultList();
			for(Reimbursement speciality : SpecialityList){ 
				System.out.println("treatement type"+speciality.getTreatmentType().getValue());
				specilityValue.append(speciality.getTreatmentType().getValue()).append(", ");
			}
			
			return specilityValue.toString();
			}catch(Exception e){
				
				return null;
			}
	}
	
	
	private Double getRequestAmt(Long rodKey){
		try{
			
			Double value = 0.0;
			Query findCpuCode = entityManager.createNamedQuery("DocAcknowledgement.findByRODKey").setParameter("rodKey", rodKey);
			List<DocAcknowledgement> SpecialityList = findCpuCode.getResultList();
			for(DocAcknowledgement speciality : SpecialityList){ 
				System.out.println("treatement type"+(speciality.getPostHospitalizationClaimedAmount()+speciality.getPostHospitalizationClaimedAmount()
						+speciality.getHospitalizationClaimedAmount()));
				 value = speciality.getPostHospitalizationClaimedAmount()+speciality.getPostHospitalizationClaimedAmount()
						+speciality.getHospitalizationClaimedAmount();
			}
			
			return value;
			}catch(Exception e){
				
				return null;
			}
	}
	
	private Long getAcknowledgementKey(Long rodKey){
		try{
			
			Long value = 0L;
			Query findCpuCode = entityManager.createNamedQuery("DocAcknowledgement.findByRODKey").setParameter("rodKey", rodKey);
			List<DocAcknowledgement> SpecialityList = findCpuCode.getResultList();
			for(DocAcknowledgement docAcknowledgement : SpecialityList){ 
				 value = docAcknowledgement.getKey();
				
			}
			
			return value;
			}catch(Exception e){
				
				return null;
			}
	}
	
	/*------------------------------------------------------FORM SERVICE--------------------------------------------------------*/
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getSpecialityValueByReference(Long treatmentTypeId) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("SpecialityType.findBytreatmentTypeId").setParameter("treatmentTypeId", treatmentTypeId);
		List<SpecialityType> specialityValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> specilityValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select= null;
		for (SpecialityType value : specialityValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue().toString());
			selectValuesList.add(select);
		}
		specilityValueContainer.addAll(selectValuesList);

		return specilityValueContainer;
	}
	
	
	public Claim getClaimByKey(Long key) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", key);
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

}
