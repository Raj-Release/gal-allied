package com.shaic.claim.pcc.hrmp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.pcc.hrmprocessing.HRMProcessing;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasHospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;

/**
 * @author PanneerSelvam.M
 *
 */
@Stateless
public class SearchHRMPService  extends AbstractDAO<Preauth> {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private MasterService  masterService;
	
	Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
	
	public SearchHRMPService(){
		super();
	}
	
	@Override
	public Class<Preauth> getDTOClass() {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Preauth> getPreauthByIntimationKey(Long intimationKey) {
		Query query = entityManager
				.createNamedQuery("Preauth.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null){
			return singleResult;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HRMProcessing> getHRMprocessingDataByAllID(Long intimationKey , Long cashlessKey , String hrmDoctorCode , Long statusKey) {
		Query query = entityManager
				.createNamedQuery("HRMProcessing.findByLoginId");
		query.setParameter("intimationKey", intimationKey);
		query.setParameter("cashlessKey", cashlessKey);
		query.setParameter("hrmDoctorCode", hrmDoctorCode);
		query.setParameter("statusKey", statusKey);
		List<HRMProcessing> singleResult = (List<HRMProcessing>) query.getResultList();
		if(singleResult != null && !singleResult.isEmpty()){
			return singleResult;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByIntimation(Long intimationKey) {
		List<Claim> a_claimList = new ArrayList<Claim>();
		if (intimationKey != null) {

			Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			try {
				a_claimList = (List<Claim>) findByIntimationKey.getResultList();
				
				if( a_claimList != null ){
					return a_claimList.get(0);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}

		}
		return null;

	}
	
	public Hospitals getHospitalDetailsByKey(Long hospKey){
		Hospitals hospitalObj = null; 
		Query query = entityManager
				.createNamedQuery("Hospitals.findByKey");
		query = query.setParameter("key", hospKey);
		List<Hospitals> hospitalList = query.getResultList();
		if(null != hospitalList && !hospitalList.isEmpty())
		{
			hospitalObj = hospitalList.get(0);
			entityManager.refresh(hospitalObj);
			
		}
		return hospitalObj;
	}
	
	protected Boolean updatePendingHRMDetails(String hrmRemarks, Long hrmKey) {

		try {
			HRMProcessing hrmProcessing = getHRMprocessingDataByID(hrmKey);
			if(null != hrmProcessing)
			{
				hrmProcessing.setHrmRemarks(hrmRemarks);
				entityManager.merge(hrmProcessing);
				entityManager.flush();
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
     return false;
	}
	
	@SuppressWarnings("unchecked")
	public HRMProcessing getHRMprocessingDataByID(Long hrmKey) {
		Query query = entityManager
				.createNamedQuery("HRMProcessing.findById");
		query.setParameter("hrmKey", hrmKey);
		List<HRMProcessing> singleResult = (List<HRMProcessing>) query.getResultList();
		if(singleResult != null && !singleResult.isEmpty()){
			return singleResult.get(0);
		}
		return null;
	}
	
	public Boolean updatePendingHRMDetail(HRMProcessing hrmProcessing){
		try {
			entityManager.merge(hrmProcessing);
			entityManager.flush();
			entityManager.clear();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Status getStatusByKey(Long a_key) {
		Status statusObj = null;
		try{
			if(a_key != null){
				Query query = entityManager
						.createNamedQuery("Status.findByKey");
				query = query.setParameter("statusKey", a_key);
				List<Status> mastersValueList = query.getResultList();
				if(mastersValueList != null && !mastersValueList.isEmpty()){
					for (Status value : mastersValueList) {
						entityManager.refresh(value);
						statusObj = value;
					}
				}
				mastersValueList = null;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return statusObj;
	}

	public HRMProcessing getHRMAndDivisionHeadRemarksDetails(Long intimationKey) {
		try {
			Query query = entityManager.createNamedQuery("HRMProcessing.findByIntimationKey");
			query.setParameter("intimationKey", intimationKey);
			List<HRMProcessing> singleResult = (List<HRMProcessing>) query.getResultList();
			if(singleResult != null && !singleResult.isEmpty()){
				return singleResult.get(0);
			}
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SearchHRMPCompletedTableDTO> getHRMDivisionHeadViewDetails(Intimation intimation){
		try {
			List<SearchHRMPCompletedTableDTO> searchHRMPCompletedTableDTOList = new ArrayList<SearchHRMPCompletedTableDTO>();
			Claim claim = getClaimByIntimation(intimation.getKey());
			HRMProcessing hrmProcessing = getHRMAndDivisionHeadRemarksDetails(intimation.getKey());
			SearchHRMPCompletedTableDTO hrmCompleted = new SearchHRMPCompletedTableDTO();
			hrmCompleted.setIntimationNo(intimation.getIntimationId());
			hrmCompleted.setReferenceNo(claim.getClaimId());
			hrmCompleted.setDateOfAdmission(claim.getDataOfAdmission().toString());
			if(hrmProcessing != null && hrmProcessing.getHrmDoctorCode() != null){
				TmpEmployee userLoginDetails = masterService.getUserLoginDetail(hrmProcessing.getHrmDoctorCode().toLowerCase());
				
				if(userLoginDetails != null && userLoginDetails.getLoginId() != null && userLoginDetails != null && userLoginDetails.getEmpFirstName() != null){
					hrmCompleted.setHrmUserId( userLoginDetails.getLoginId() +" - "+userLoginDetails.getEmpFirstName());	
				}else if(userLoginDetails != null && userLoginDetails.getLoginId() != null){
					hrmCompleted.setHrmUserId( userLoginDetails.getLoginId());	
				}
			}
			
			if(hrmProcessing != null && hrmProcessing.getHrmCompletedDate() != null){
				hrmCompleted.setHrmDate(hrmProcessing.getHrmCompletedDate().toString());	
			}
			if(hrmProcessing != null && hrmProcessing.getHrmRemarks() != null){
				hrmCompleted.setHrmRemarks(hrmProcessing.getHrmRemarks());	
			}
			if(hrmProcessing != null && hrmProcessing.getZonalRemarks() != null)
			{
				if(hrmProcessing != null && hrmProcessing.getZonalCompletedDate() != null ){
					hrmCompleted.setDivissionHeadDate(hrmProcessing.getZonalCompletedDate().toString());	
				}
				if(hrmProcessing != null &&  hrmProcessing.getZonalHeadCode() != null){
					
					TmpEmployee tmpEmployee = masterService.getUserLoginDetail(hrmProcessing.getZonalHeadCode());
					
					if(tmpEmployee != null && tmpEmployee.getLoginId() != null && tmpEmployee != null && tmpEmployee.getEmpFirstName() != null){
						hrmCompleted.setDivissionHeadUserId( tmpEmployee.getLoginId() +" - "+tmpEmployee.getEmpFirstName());	
					}
					else if(tmpEmployee != null && tmpEmployee.getLoginId() != null || tmpEmployee != null && tmpEmployee.getEmpFirstName() != null){
						hrmCompleted.setDivissionHeadUserId( tmpEmployee.getLoginId());	
					}
					
				}
				if(hrmProcessing != null && hrmProcessing.getZonalRemarks() != null ){
					hrmCompleted.setDivissionHeadRemarks(hrmProcessing.getZonalRemarks());
				}
			}
			
			if(hrmProcessing != null && hrmProcessing.getEsclationLevel() != null ){
				hrmCompleted.setStatus(hrmProcessing.getEsclationLevel());
			}
			searchHRMPCompletedTableDTOList.add(hrmCompleted);
			return searchHRMPCompletedTableDTOList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Page<SearchHRMPTableDTO> search(SearchHRMPFormDTO formDto,String userName , String tabStatus) {
		
		List<HRMProcessing> reimbursementList = new ArrayList<HRMProcessing>();
		List<SearchHRMPTableDTO> tableDtoList = new ArrayList<SearchHRMPTableDTO>();
		Integer totalRecords = 0; 
		
		try{
			String intimationNo = null != formDto && formDto.getIntimationNumber() != null && !formDto.getIntimationNumber().isEmpty() ? formDto
					.getIntimationNumber() : null;
			Long cpuKey = null != formDto && formDto.getCpuCode() != null && formDto.getCpuCode().getId() != null ? formDto
					.getCpuCode().getId() : null;
					
			Long hospitalKey = formDto != null && formDto.getHospitalCode() != null && formDto.getHospitalCode().getId() != null ? formDto.getHospitalCode().getId() : null;

			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<HRMProcessing> criteriaQuery = criteriaBuilder.createQuery(HRMProcessing.class);

			Root<HRMProcessing> root = criteriaQuery.from(HRMProcessing.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();
			
			if (intimationNo != null) {
				Predicate condition1 = criteriaBuilder.like(
						root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<String> get("intimationId"), "%"
								+ intimationNo + "%");
				conditionList.add(condition1);
			}
			if (cpuKey != null) {
				Predicate condition2 = criteriaBuilder.equal(
						root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<TmpCPUCode> get("cpuCode")
								.<Long> get("key"),  cpuKey);
				conditionList.add(condition2);
			}
			
			if(hospitalKey != null){
				Predicate condition3 = criteriaBuilder.equal(
						root.<Claim> get("claim")
								.<Intimation> get("intimation")
								.<Long> get("hospital"),  hospitalKey);
				conditionList.add(condition3);
			}
			if(userName != null){
				Predicate condition3 = criteriaBuilder.equal(root.<String>get("hrmDoctorCode"), userName.toUpperCase());
				conditionList.add(condition3);
				
				Predicate condition4 = criteriaBuilder.equal(root.<String>get("activeFlag"), SHAConstants.YES_FLAG);
				conditionList.add(condition4);
			}			
			
			if(tabStatus.equalsIgnoreCase(SHAConstants.HRM_PENDING_TAB)){
				Predicate condition4 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),ReferenceTable.HRM_INITINATE_KEY);
				conditionList.add(condition4);
			
			}else if(tabStatus.equalsIgnoreCase(SHAConstants.HRM_COMPLETED_TAB)){
				
				List<Long> cpuKeyList = new ArrayList<Long>();
				cpuKeyList.add(ReferenceTable.HRM_COMPLETED_KEY);
				cpuKeyList.add(ReferenceTable.ZONAL_COMPLETED_KEY);
				
				Predicate condition4 = root.<Status>get("status").<Long>get("key").in(cpuKeyList);
				conditionList.add(condition4);
			}
			
			
			if(intimationNo == null && cpuKey == null && hospitalKey == null){
				criteriaQuery.select(root).where(conditionList.toArray(new Predicate[]{}));
				} else{
			criteriaQuery.select(root).where(
					criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				}
			criteriaQuery.orderBy(criteriaBuilder.asc(root.<Claim> get("claim").<Claim> get("dataOfAdmission")));
			/*final TypedQuery<HRMProcessing> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = formDto.getPageable().getPageNumber();
			
			reimbursementList = typedQuery.getResultList();*/
			
			final TypedQuery<HRMProcessing> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = formDto.getPageable().getPageNumber();
			int firtResult;
			if(pageNumber > 1){
				firtResult = (pageNumber-1) *10;
			}else{
				firtResult = 0;
			}
			
			if(intimationNo == null /*&& cpuKey == null && hospitalKey == null*/ /*&& listIntimations.size()>10*/){
				reimbursementList = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}else{
				reimbursementList = typedQuery.getResultList();
			}
			
			totalRecords = typedQuery.getResultList().size();
			
			if(reimbursementList != null){
				TmpEmployee userLoginDetails = masterService.getUserLoginDetail(userName);
				SearchHRMPTableDTO dt = null;
				for(HRMProcessing hrmProcessing :reimbursementList){
					if(hrmProcessing != null && hrmProcessing.getPreauth() != null && hrmProcessing.getPreauth().getKey() != null && 
							hrmProcessing.getIntimation() != null && hrmProcessing.getIntimation().getIntimationId() != null && hrmProcessing.getClaim() != null
							&& hrmProcessing.getStatus() != null){
					dt= new SearchHRMPTableDTO();
					if(hrmProcessing != null && hrmProcessing.getEsclationLevel() != null ){
						dt.setStatus(hrmProcessing.getEsclationLevel());
					}

					NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance().getNewIntimationDto(hrmProcessing.getIntimation());
					Claim claim = hrmProcessing.getClaim();
					if(claim.getDataOfAdmission() != null){
						String admissionDate = SHAUtils.getDateFormat(claim.getDataOfAdmission().toString());
						dt.setDateOfAdmission(admissionDate);
					}
					
					long dayCount = SHAUtils.getDaysBetweenDate(claim.getDataOfAdmission(), new Date());
					
					if(userLoginDetails.getLoginId().equalsIgnoreCase(userName))
					{
						if(userLoginDetails != null && userLoginDetails.getLoginId() != null && userLoginDetails != null && userLoginDetails.getEmpFirstName() != null){
							dt.setUserName( userLoginDetails.getLoginId() +" - "+userLoginDetails.getEmpFirstName());	
						}else if(userLoginDetails != null && userLoginDetails.getLoginId() != null){
							dt.setUserName( userLoginDetails.getLoginId());	
						}

						Long hospitalIndividualKey = hrmProcessing.getIntimation().getHospital();
						Hospitals hospitals = getHospitalDetailsByKey(hospitalIndividualKey);

						dt.setTabStatus(tabStatus);
						dt.setKey(hrmProcessing.getKey());
						dt.setCashlessKey(hrmProcessing.getPreauth().getKey());
						dt.setClaimKey(hrmProcessing.getClaim().getKey());
						dt.setIntimationNo(hrmProcessing.getIntimation().getIntimationId());
						dt.setReferenceNo(claim.getClaimId());
						if(newIntimationDto != null && newIntimationDto.getCpuCode() != null){
							int cpuInt = Integer.parseInt(newIntimationDto.getCpuCode());
							Long cpuCode = (long) cpuInt;
							TmpCPUCode tmpCPUCode= getMasCpuCode(cpuCode);
							if(tmpCPUCode != null)
							dt.setCpuCode(newIntimationDto.getCpuCode() +" - "+tmpCPUCode.getDescription());
						}
						dt.setHospitalCode(hospitals.getHospitalCode()+" - "+hospitals.getName());
						dt.setHospitalType(hospitals.getHospitalTypeName());
						dt.setHrmObj(hrmProcessing);
						dt.setLoginId(userName);
						dt.setAgeing(Long.toString(dayCount));
						if(hrmProcessing != null &&  hrmProcessing.getZonalHeadCode() != null){
							
							TmpEmployee tmpEmployee = masterService.getUserLoginDetail(hrmProcessing.getZonalHeadCode());
							
							if(tmpEmployee != null && tmpEmployee.getLoginId() != null && tmpEmployee != null && tmpEmployee.getEmpFirstName() != null){
								dt.setDivissionHeadUserId( tmpEmployee.getLoginId() +" - "+tmpEmployee.getEmpFirstName());	
							}
							else if(tmpEmployee != null && tmpEmployee.getLoginId() != null || tmpEmployee != null && tmpEmployee.getEmpFirstName() != null){
								dt.setDivissionHeadUserId( tmpEmployee.getLoginId());	
							}

						}
						if(hrmProcessing != null &&  hrmProcessing.getHrmDoctorCode() != null){

							TmpEmployee tmpEmployee = masterService.getUserLoginDetail(hrmProcessing.getHrmDoctorCode());

							if(tmpEmployee != null && tmpEmployee.getLoginId() != null && tmpEmployee != null && tmpEmployee.getEmpFirstName() != null){
								dt.setHrmUserId( tmpEmployee.getLoginId() +" - "+tmpEmployee.getEmpFirstName());	
							}
							else if(tmpEmployee != null && tmpEmployee.getLoginId() != null || tmpEmployee != null && tmpEmployee.getEmpFirstName() != null){
								dt.setHrmUserId( tmpEmployee.getLoginId());	
							}

						}

						tableDtoList.add(dt);
					}
				}
				}
			}
			List<SearchHRMPTableDTO> result = new ArrayList<SearchHRMPTableDTO>();
			result.addAll(tableDtoList);

			if (null != result) {

				Collections.sort(result, new Comparator<SearchHRMPTableDTO>(){
					public int compare(SearchHRMPTableDTO o1, SearchHRMPTableDTO o2){
						return o2.getAgeing()!= null && o1.getAgeing() != null && !o2.getAgeing().isEmpty() && !o1.getAgeing().isEmpty() ? Integer.valueOf(o2.getAgeing()) - Integer.valueOf(o1.getAgeing()) : 0;
					}
				});

			}
			
			Page<SearchHRMPTableDTO> page = new Page<SearchHRMPTableDTO>();
			formDto.getPageable().setPageNumber(pageNumber+1);
			page.setHasNext(true);
			if(result.isEmpty()){
				formDto.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(false);
			page.setTotalRecords(totalRecords);
			return page;
	
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public TmpCPUCode getMasCpuCode(Long cpuCode){
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}
}