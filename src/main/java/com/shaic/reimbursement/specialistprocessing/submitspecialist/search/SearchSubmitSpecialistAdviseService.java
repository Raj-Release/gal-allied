/**
 * 
 */
package com.shaic.reimbursement.specialistprocessing.submitspecialist.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.SpecialityType;

/**
 * @author ntv.narenj 
 * 
 * This file is not in use. Please refer SubmitSpecialistService.java for search service.
 *
 */
@Stateless
public class SearchSubmitSpecialistAdviseService extends AbstractDAO<Claim>{

	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public SearchSubmitSpecialistAdviseService() {
		super();
		
	}
	
	public  Page<SearchSubmitSpecialistAdviseTableDTO> search(
			SearchSubmitSpecialistAdviseFormDTO searchFormDTO,
			String userName, String passWord) {/*
		
		
		try{
			String intimationNo = null != searchFormDTO  && !searchFormDTO.getIntimationNo().isEmpty() ? searchFormDTO.getIntimationNo() :null;
			String claimNo = null != searchFormDTO  && !searchFormDTO.getClaimNo().isEmpty() ? searchFormDTO.getClaimNo() : null;
			String cpuCode =  null != searchFormDTO  && searchFormDTO.getCpuCode() != null ? searchFormDTO.getCpuCode().getValue() : null;
			
			
			HumanTask humanTaskDTO = null;
			Long rodKey;
			List<String> intimationNoList = new ArrayList<String>();
			List<HumanTask> humanTaskListDTO = new ArrayList<HumanTask>();
			List<Long> rodKeyList = new ArrayList<Long>();
			PayloadBOType payloadBOType = new PayloadBOType();
			payloadBOType.getIntimation().setIntimationNumber(intimationNo);
			payloadBOType.getPolicy().setPolicyId(policyNo);
			payloadBOType.getClaimRequest().setCpuCode(String.valueOf(cpuCode));
		
			SpecialistTask specialistTask = BPMClientContext.getSpecialistTask(userName, passWord);
			
			
			
			Pageable pageable = null;
			
			PagedTaskList taskList = specialistTask.getTasks(userName,pageable,payloadBOType);
			
			
		
			if(null != specialistTask){
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
			List<SearchSubmitSpecialistAdviseTableDTO> tableDTO = new ArrayList<SearchSubmitSpecialistAdviseTableDTO>();
			for(int index = 0 ; index < intimationNoList.size() ; index++){
				
				intimationNo = intimationNoList.get(index);
				
				 humanTaskDTO = humanTaskListDTO.get(index);
				 
				 rodKey = rodKeyList.get(index);
				 
				 
			
				 tableDTO.addAll(getIntimationData(intimationNo, rodKey,  humanTaskDTO ));
				 
			}
		
			Page<SearchSubmitSpecialistAdviseTableDTO> page = new Page<SearchSubmitSpecialistAdviseTableDTO>();
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
	private List<SearchSubmitSpecialistAdviseTableDTO> getIntimationData(String intimationNo,  Long rodKey){
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
			
			/**
			 * This file is not in used. Please discard the code
			 */
			 
			List<SearchSubmitSpecialistAdviseTableDTO> tableDTO = null;//SearchSubmitSpecialistAdviseMapper.getClaimDTO(claimList);
			//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
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
	

	private List<SearchSubmitSpecialistAdviseTableDTO> getHospitalDetails(
			List<SearchSubmitSpecialistAdviseTableDTO> tableDTO, Long rodKey) {
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			
			Query findByHospitalKey = entityManager.createNamedQuery(
					"Hospitals.findByKey").setParameter("key", tableDTO.get(index).getHospitalNameID());
			try{
			 hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			 if(hospitalDetail != null){
				 tableDTO.get(index).setRodKey(rodKey);
				 //tableDTO.get(index).setHumanTaskDTO(humanTask); 
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				 tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
				 tableDTO.get(index).setHospitalCity(hospitalDetail.getCityId()+" ,"+hospitalDetail.getCity());
				 tableDTO.get(index).setCpuId(hospitalDetail.getCpuId());
				 Long cpuCode = null != getTmpCPUCode(tableDTO.get(index).getCpuId()) ? getTmpCPUCode(tableDTO.get(index).getCpuId()).getCpuCode() : 0l;
				 tableDTO.get(index).setCpuCode(cpuCode);
				 tableDTO.get(index).setSpecialityType(getSpecialityType(tableDTO.get(index).getClaimKey()));
				
			 }
			}catch(Exception e){
				continue;
			}
		
		}
		
		
		return tableDTO;
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
	
	private String getSpecialityType(Long claimKey){
		try{
			StringBuffer specilityValue = new StringBuffer();
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

}

