/**
 * 
 */
package com.shaic.reimbursement.financialapprover.processclaimrequestBenefits.search;

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
public class SearchProcessClaimRequestBenefitsFinancService extends AbstractDAO<Claim>{

	@EJB
	private DBCalculationService dbCalculationService;
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public SearchProcessClaimRequestBenefitsFinancService() {
		super();
		
	}
	
	/***
	 * Search bpmn to db migration to be completed. 
	 * Temporarily commenting the same.
	 * */
	
	public  Page<SearchProcessClaimRequestBenefitsFinancTableDTO> search(
			SearchProcessClaimRequestBenefitsFinancFormDTO searchFormDTO,
			String userName, String passWord) {
		

		try{/*
			String intimationNo =  searchFormDTO.getIntimationNo() ;
			String 	policyNo = searchFormDTO.getPolicyNo();
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
		
			ProcessClaimRequestTask processClaimRequest = BPMClientContext.getprocessClaimRequestTask(userName, passWord);
			
			Pageable pageable = null;
			
			PagedTaskList taskList = processClaimRequest.getTasks(userName,pageable,payloadBOType);
			
			
		
			if(null != processClaimRequest){
				List<HumanTask> humanTaskList = taskList.getHumanTasks();
				if(null == humanTaskList | humanTaskList.isEmpty()){
					return null;
				}
				
				for(HumanTask humanTask : humanTaskList){
					PayloadBOType payloadBO = humanTask.getPayload();
					if(payloadBO.getRod() != null){
					intimationNoList.add(payloadBO.getIntimation().getIntimationNumber());
					humanTaskListDTO.add(humanTask);
					
					rodKeyList.add(payloadBO.getRod().getKey());
					}
				}
			}
			List<SearchProcessClaimRequestBenefitsFinancTableDTO> tableDTO = new ArrayList<SearchProcessClaimRequestBenefitsFinancTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				if(index < rodKeyList.size()){
				intimationNo = intimationNoList.get(index);
				
				 humanTaskDTO = humanTaskListDTO.get(index);
					
				 rodKey = rodKeyList.get(index);
				 tableDTO.addAll(getIntimationData(intimationNo, rodKey,  humanTaskDTO ));
					}
			}
		
			Page<SearchProcessClaimRequestBenefitsFinancTableDTO> page = new Page<SearchProcessClaimRequestBenefitsFinancTableDTO>();
			page.setPageItems(tableDTO);
			return page;
			*/
		return null;	
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
			return null;	
			
	}

	
	private List<SearchProcessClaimRequestBenefitsFinancTableDTO> getIntimationData(String intimationNo,  Long rodKey){
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
			
			List<SearchProcessClaimRequestBenefitsFinancTableDTO> tableDTO = SearchProcessClaimRequestBenefitsFinancMapper.getInstance().getIntimationDTO(claimList);
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
	

	private List<SearchProcessClaimRequestBenefitsFinancTableDTO> getHospitalDetails(
			List<SearchProcessClaimRequestBenefitsFinancTableDTO> tableDTO, Long rodKey) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 System.out.print(rodKey);
				 tableDTO.get(index).setRodKey(rodKey);
				// tableDTO.get(index).setHumanTaskDTO(humanTask);
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 tableDTO.get(index).setCpuName(getTmpCPUName(tableDTO.get(index).getCpuId()).getDescription());
				 tableDTO.get(index).setBalanceSI(getBalanceSI(tableDTO.get(index).getPolicyKey(),tableDTO.get(index).getInsuredKey() ,
				 tableDTO.get(index).getClaimKey(),rodKey));
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
//	private Double getBalanceSI(Long policyKey, Long insuredKey, Long claimKey,
//			Double sumInsured){
//		Double balanceSI = 0.0;
//		System.out.println("policyKey----------"+policyKey+"-------------insuredkey----------"+insuredKey+"-----------cliamkey--------"+claimKey+"");
//		if(policyKey !=null && insuredKey != null && sumInsured != null){
//		balanceSI = dbCalculationService.getBalanceSI(policyKey ,insuredKey, claimKey, sumInsured).get(SHAConstants.TOTAL_BALANCE_SI);
//		}
//				return balanceSI;
//		
//	}
	private Double getBalanceSI(Long policyKey, Long insuredKey, Long claimKey,Long rodKey){
		Double balanceSI = 0.0;
		if(policyKey !=null && insuredKey != null){
		balanceSI = dbCalculationService.getBalanceSIForReimbursement(policyKey ,insuredKey, claimKey, rodKey).get(SHAConstants.CURRENT_BALANCE_SI);
		}
				return balanceSI;
		
	}
	
	private List<SearchProcessClaimRequestBenefitsFinancTableDTO> getSpecilityAndTreatementType(List<SearchProcessClaimRequestBenefitsFinancTableDTO> intimationList, Long rodKey){
		
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
			StringBuffer specilityValue =new StringBuffer();
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
		SelectValue select  = null;
		for (SpecialityType value : specialityValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue().toString());
			selectValuesList.add(select);
		}
		specilityValueContainer.addAll(selectValuesList);

		return specilityValueContainer;
	}

}
