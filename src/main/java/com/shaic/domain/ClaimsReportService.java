
package com.shaic.domain;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ReimbursementDto;
import com.shaic.claim.ReimbursementMapper;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.registration.balancesuminsured.view.BalanceSumInsuredDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper.ZonalMedicalReviewMapper;
import com.shaic.claim.reports.PolicywiseClaimReportDto;
import com.shaic.claim.reports.callcenterDashBoard.CallcenterDashBoardReportDto;
import com.shaic.claim.reports.claimsdailyreportnew.ClaimsDailyReportDto;
import com.shaic.claim.reports.claimstatusreportnew.ClaimsStatusReportDto;
import com.shaic.claim.reports.notAdheringToANHReport.NewIntimationNotAdheringToANHDto;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.PreauthQuery;
import com.shaic.domain.preauth.StageInformation;
import com.shaic.domain.preauth.TmpFvR;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinSession;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ClaimsReportService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
//	@EJB
//	private DBCalculationService dbCalculationService;
	
	private final Logger log = LoggerFactory.getLogger(ReimbursementService.class);
	
	public List<ClaimsDailyReportDto> getClaimsDailyReport(Map<String,Object> filters,UsertoCPUMappingService usertoCPUMapService){
		List<ClaimsDailyReportDto> resultDto = new ArrayList<ClaimsDailyReportDto>();
		
		Date fromDate = null;
		Date endDate = null;
		Long cpuKey = null;
		Long clmTypeKey = null;
		String userId = "";
		if(filters != null && !filters.isEmpty()){
			
			if(filters.containsKey("fromDate") && filters.get("fromDate") != null){
				
				fromDate = (Date)filters.get("fromDate");
			}
			
			if(filters.containsKey("endDate") && filters.get("endDate") != null){
				
				endDate = (Date)filters.get("endDate");
			}
			
			if(filters.containsKey("cpuKey") && filters.get("cpuKey") != null){
				
				cpuKey = (Long)filters.get("cpuKey");
			}
			if(filters.containsKey("clmTypeKey") && filters.get("clmTypeKey") != null){
				
				clmTypeKey = (Long)filters.get("clmTypeKey");
			}
			if(filters.containsKey(BPMClientContext.USERID) && filters.get(BPMClientContext.USERID) != null){
				
				userId = (String)filters.get(BPMClientContext.USERID);
			}
							
//		List<Claim> resultClaimList = new ArrayList<Claim>();
			
		List<StageInformation> resultClaimStgList = new ArrayList<StageInformation>();
		
		try{
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();
			
			if (fromDate != null && endDate != null) {			
			final CriteriaBuilder builder = entityManager
					.getCriteriaBuilder();
			
//			final CriteriaQuery<Claim> criteriaClaimQuery = builder
//					.createQuery(Claim.class);
			
			final CriteriaQuery<StageInformation> criteriaClaimStgQuery = builder
					.createQuery(StageInformation.class);

			Root<StageInformation> claimStgRoot = criteriaClaimStgQuery.from(StageInformation.class);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			if(cpuKey != null){
				Predicate cpuPredicate = builder.equal(claimStgRoot.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuKey);
//				Predicate cpuPredicate = builder.equal(claimRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuKey);
				predicates.add(cpuPredicate);
			}
			else
			{
				List<Long> cpuKeyList = usertoCPUMapService.getCPUCodeList(userId, entityManager);
//				Predicate cpuKeyListPredicate = claimStgRoot.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
				Predicate cpuKeyListPredicate = claimStgRoot.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
//				Predicate cpuKeyListPredicate = claimRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
				predicates.add(cpuKeyListPredicate);
			}
			if(clmTypeKey != null){
//				Predicate clmTypePredicate = builder.equal(claimStgRoot.<Claim>get("claim").<MastersValue>get("claimType").<Long>get("key"), clmTypeKey);
				Predicate clmTypePredicate = builder.equal(claimStgRoot.<Claim>get("claim").<MastersValue>get("claimType").<Long>get("key"), clmTypeKey);
//				Predicate clmTypePredicate = builder.equal(claimRoot.<MastersValue>get("claimType").<Long>get("key"), clmTypeKey);
				predicates.add(clmTypePredicate);
			}
						
//			Predicate registeredPredicate = builder.equal(claimStgRoot.<Status>get("status").<Long>get("key"), ReferenceTable.INTIMATION_REGISTERED_STATUS);
			
			Predicate registeredPredicate = builder.equal(claimStgRoot.<Status>get("status").<Long>get("key"), ReferenceTable.INTIMATION_REGISTERED_STATUS);
			
//			Predicate registeredPredicate = builder.equal(claimRoot.<Status>get("status").<Long>get("key"), ReferenceTable.INTIMATION_REGISTERED_STATUS);
			
			predicates.add(registeredPredicate);
			
				Expression<Date> fromDateExpression = claimStgRoot
						.<Date> get("createdDate");
				Predicate fromDatePredicate = builder
						.greaterThanOrEqualTo(fromDateExpression,
								fromDate);
				predicates.add(fromDatePredicate);

				Expression<Date> toDateExpression = claimStgRoot
						.<Date> get("createdDate");
				Calendar c = Calendar.getInstance();
				c.setTime(endDate);
				c.add(Calendar.DATE, 1);
				endDate = c.getTime();
				Predicate toDatePredicate = builder
						.lessThanOrEqualTo(toDateExpression, endDate);
				predicates.add(toDatePredicate);
			
			criteriaClaimStgQuery.select(claimStgRoot).where(
					builder.and(predicates
							.toArray(new Predicate[] {})));

			final TypedQuery<StageInformation> claimQuery = entityManager
					.createQuery(criteriaClaimStgQuery);
		
			ClaimsReportService.popinReportLog(entityManager, userId, "ClaimsDailyReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			
//			resultClaimList = claimQuery.getResultList();
			
			resultClaimStgList = claimQuery.getResultList();
			
			WeakHashMap<Long,Object> HospMap = getHospitalsForClaimStg(resultClaimStgList);
		
//		if(resultClaimList != null && !resultClaimList.isEmpty()){
		if(resultClaimStgList != null && !resultClaimStgList.isEmpty()){	
			DBCalculationService dbcalService = new DBCalculationService();
			Claim claim;
			ClaimsDailyReportDto clmDailyReportDto;
			Hospitals hospital;
			List<FieldVisitRequest> fvrReqList;
			FieldVisitRequest fvrReq;
			List<TmpFvR> fvrList;
			for(StageInformation stgInfoObj : resultClaimStgList){
//			for(Claim claimObj : resultClaimList){
				claim = stgInfoObj.getClaim();
//				claim = claimObj;
				entityManager.refresh(claim);
				entityManager.refresh(claim.getIntimation());
				entityManager.refresh(claim.getIntimation().getPolicy());
				entityManager.refresh(claim.getIntimation().getInsured());
				entityManager.refresh(claim.getIntimation().getCpuCode());
				clmDailyReportDto = new ClaimsDailyReportDto(claim);
				
//				ClaimsDailyReportDto clmDailyReportDto = RevisedClaimMapper.getInstance().getClaimsDailyReportDto(claim);
//				ClaimsDailyReportDto clmDailyReportDto = RevisedClaimMapper.getClaimsDailyReportDto(claim);
//				
//				Query hospitalQuery  = entityManager.createNamedQuery("Hospitals.findByKey");
//				if(claim.getIntimation().getHospital() != null){
//					hospitalQuery.setParameter("key", claim.getIntimation().getHospital());
//					hospital = (Hospitals)hospitalQuery.getResultList().get(0);
					
					hospital = (Hospitals)HospMap.get(claim.getKey());
					if(hospital != null){
						entityManager.refresh(hospital);
						
						clmDailyReportDto.setHospitalName(hospital.getName());
						clmDailyReportDto.setHospitalCode(hospital.getHospitalCode());
						clmDailyReportDto.setHospitalType(hospital.getHospitalType() != null && (ReferenceTable.NETWORK_HOSPITAL_TYPE_ID).equals(hospital.getHospitalType().getKey()) ? "NETWORK" : "NON-NETWORK");
						clmDailyReportDto.setHospitalCity(hospital.getCity());
						clmDailyReportDto.setHospitalState(hospital.getState());
						clmDailyReportDto.setANHStatus(hospital.getNetworkHospitalType() != null ? (hospital.getNetworkHospitalType().toLowerCase().contains("agreed") ? "ANH" : "Non ANH") : "");
						clmDailyReportDto.setHospitalType(hospital.getHospitalType() != null ? hospital.getHospitalType().getValue() :"");
					}					
//				}				
//				NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claim.getIntimation(), entityManager);
//				claimDto.setNewIntimationDto(intimationDto);				
				
//				clmDailyReportDto.setSno(resultClaimList.indexOf(claimObj)+1);
				clmDailyReportDto.setSno(resultClaimStgList.indexOf(stgInfoObj)+1);
				clmDailyReportDto.setHospitalDate(claim.getIntimation().getAdmissionDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(claim.getIntimation().getAdmissionDate()) : "");
				clmDailyReportDto.setPlannedAdmission(clmDailyReportDto.getPlannedAdmission() != null && StringUtils.containsIgnoreCase(clmDailyReportDto.getPlannedAdmission(), "plan") ? "Yes" : "No");
				
				Double sumInsured = dbcalService.getInsuredSumInsured(String.valueOf(claim.getIntimation().getInsured().getInsuredId()), claim.getIntimation().getPolicy().getKey(),claim.getIntimation().getInsured().getLopFlag());
				clmDailyReportDto.setSuminsured(String.valueOf(sumInsured.intValue()));
				
//				Query fvrQuery = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKey");
//				fvrQuery.setParameter("claimKey", claim.getKey());
//				fvrReqList = (List<FieldVisitRequest>)fvrQuery.getResultList();
//				
//				if(fvrReqList != null && !fvrReqList.isEmpty()){
//					fvrReq = fvrReqList.get(fvrReqList.size()-1);
//					entityManager.refresh(fvrReq);
//					clmDailyReportDto.setFieldDoctorNameAllocated(fvrReq.getRepresentativeName() != null ? fvrReq.getRepresentativeName() : "");
//			
//					if(fvrReq.getRepresentativeCode() != null){
//						Query q = entityManager.createNamedQuery("TmpFvR.findByCode");
//						q.setParameter("code", fvrReq.getRepresentativeCode());
//						fvrList = q.getResultList();
//						if(fvrList != null && !fvrList.isEmpty()){
//							entityManager.refresh(fvrList.get(0));
//							String contactNo = "";
//							if(fvrList.get(0).getMobileNumber() != null){
//								contactNo = fvrList.get(0).getMobileNumber() != null ? fvrList.get(0).getMobileNumber().toString() : "";
//							}
//							if(fvrList.get(0).getPhoneNumber() != null){
//								contactNo = ("").equalsIgnoreCase(contactNo) ? ( fvrList.get(0).getPhoneNumber().toString()) : ( contactNo + " / " + fvrList.get(0).getPhoneNumber().toString());
//							}
//							clmDailyReportDto.setContactNumOfDoctor(contactNo);
//						}
//					}
//					
//					SimpleDateFormat dtFrmt = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
//					clmDailyReportDto.setDataOfAllocationWithTime(fvrReq.getAssignedDate() != null ? dtFrmt.format(fvrReq.getAssignedDate()).toUpperCase():"");
//				}				
				
				resultDto.add(clmDailyReportDto);
				clmDailyReportDto = null;
			}
			dbcalService = null;
			ClaimsReportService.popinReportLog(entityManager, userId, "ClaimsDailyReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
		}
		}
		utx.commit();
		}catch(Exception e){
			
			ClaimsReportService.popinReportLog(entityManager, userId, "ClaimsDailyReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	  }	
		return resultDto;
	}
	
	public WeakHashMap<Long,Object> getHospitalsForClaims(List<Claim> resultClaimList){
		WeakHashMap<Long,Object> HospMap = new WeakHashMap<Long, Object>();
		
		for (Claim claim : resultClaimList) {
			HospMap.put(claim.getKey(), getHospitalDetailsByKey(claim.getIntimation().getHospital()));
		}		
		return HospMap;
		
	}
	
	public WeakHashMap<Long,Object> getHospitalsForClaimStg(List<StageInformation> resultClaimStgList){
		WeakHashMap<Long,Object> HospMap = new WeakHashMap<Long, Object>();
		
		for (StageInformation claimStg : resultClaimStgList) {
			HospMap.put(claimStg.getClaim().getKey(), getHospitalDetailsByKey(claimStg.getClaim().getIntimation().getHospital()));
		}		
		return HospMap;
		
	}
	
	public List<ClaimsDailyReportDto> getClaimsDailyReport_From_Claim_Table(Map<String,Object> filters,UsertoCPUMappingService usertoCPUMapService){
		List<ClaimsDailyReportDto> resultDto = new ArrayList<ClaimsDailyReportDto>();
		
		Date fromDate = null;
		Date endDate = null;
		Long cpuKey = null;
		Long clmTypeKey = null;
		String userId = "";
		if(filters != null && !filters.isEmpty()){
			
			if(filters.containsKey("fromDate") && filters.get("fromDate") != null){
				
				fromDate = (Date)filters.get("fromDate");
			}
			
			if(filters.containsKey("endDate") && filters.get("endDate") != null){
				
				endDate = (Date)filters.get("endDate");
			}
			
			if(filters.containsKey("cpuKey") && filters.get("cpuKey") != null){
				
				cpuKey = (Long)filters.get("cpuKey");
			}
			if(filters.containsKey("clmTypeKey") && filters.get("clmTypeKey") != null){
				
				clmTypeKey = (Long)filters.get("clmTypeKey");
			}
			if(filters.containsKey(BPMClientContext.USERID) && filters.get(BPMClientContext.USERID) != null){
				
				userId = (String)filters.get(BPMClientContext.USERID);
			}
							
		List<Claim> resultClaimList = new ArrayList<Claim>();
		
		try{
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();
			
			if (fromDate != null && endDate != null) {			
			final CriteriaBuilder builder = entityManager
					.getCriteriaBuilder();
			final CriteriaQuery<Claim> criteriaClaimQuery = builder
					.createQuery(Claim.class);

			Root<Claim> claimRoot = criteriaClaimQuery.from(Claim.class);
			/*Join<Claim,Intimation> intimationJoin = claimRoot.join(
					"intimation", JoinType.INNER);*/
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			if(cpuKey != null){
				Predicate cpuPredicate = builder.equal(claimRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuKey);
				predicates.add(cpuPredicate);
			}
			else
			{
				List<Long> cpuKeyList = usertoCPUMapService.getCPUCodeList(userId, entityManager);
				Predicate cpuKeyListPredicate = claimRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
				predicates.add(cpuKeyListPredicate);
			}
			
			if(clmTypeKey != null){
				Predicate clmTypePredicate = builder.equal(claimRoot.<MastersValue>get("claimType").<Long>get("key"), clmTypeKey);
				predicates.add(clmTypePredicate);
			}
			
			
			Predicate registeredPredicate = builder.equal(claimRoot.<Status>get("status").<Long>get("key"), ReferenceTable.INTIMATION_REGISTERED_STATUS);
			predicates.add(registeredPredicate);
			
				Expression<Date> fromDateExpression = claimRoot
						.<Date> get("createdDate");
				Predicate fromDatePredicate = builder
						.greaterThanOrEqualTo(fromDateExpression,
								fromDate);
				predicates.add(fromDatePredicate);

				Expression<Date> toDateExpression = claimRoot
						.<Date> get("createdDate");
				Calendar c = Calendar.getInstance();
				c.setTime(endDate);
				c.add(Calendar.DATE, 1);
				endDate = c.getTime();
				Predicate toDatePredicate = builder
						.lessThanOrEqualTo(toDateExpression, endDate);
				predicates.add(toDatePredicate);
			
			criteriaClaimQuery.select(claimRoot).where(
					builder.and(predicates
							.toArray(new Predicate[] {})));

			final TypedQuery<Claim> claimQuery = entityManager
					.createQuery(criteriaClaimQuery);
		
			ClaimsReportService.popinReportLog(entityManager, userId, "ClaimsDailyReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			resultClaimList = claimQuery.getResultList();
			WeakHashMap<Long,Object> HospMap = getHospitalsForClaims(resultClaimList);
			
		if(resultClaimList != null && !resultClaimList.isEmpty()){
			DBCalculationService dbcalService = new DBCalculationService();
			ClaimsDailyReportDto clmDailyReportDto;
			List<FieldVisitRequest> fvrReqList;
			List<TmpFvR> fvrList;
			FieldVisitRequest fvrReq;
			Hospitals hospital;
			for(Claim claim : resultClaimList){
				entityManager.refresh(claim);
				entityManager.refresh(claim.getIntimation());
				entityManager.refresh(claim.getIntimation().getPolicy());
				entityManager.refresh(claim.getIntimation().getInsured());
				entityManager.refresh(claim.getIntimation().getCpuCode());
				clmDailyReportDto = new ClaimsDailyReportDto(claim);
				
//				ClaimsDailyReportDto clmDailyReportDto = RevisedClaimMapper.getInstance().getClaimsDailyReportDto(claim);
//				ClaimsDailyReportDto clmDailyReportDto = RevisedClaimMapper.getClaimsDailyReportDto(claim);
				
//				Query hospitalQuery  = entityManager.createNamedQuery("Hospitals.findByKey");
//				
//				if(claim.getIntimation().getHospital() != null){
//					hospitalQuery.setParameter("key", claim.getIntimation().getHospital());
//					hospital = (Hospitals)hospitalQuery.getResultList().get(0);
				
					hospital = (Hospitals)HospMap.get(claim.getKey());
					if(hospital != null){
						entityManager.refresh(hospital);
						
						clmDailyReportDto.setHospitalName(hospital.getName());
						clmDailyReportDto.setHospitalCode(hospital.getHospitalCode());
						clmDailyReportDto.setHospitalCity(hospital.getCity());
						clmDailyReportDto.setHospitalState(hospital.getState());
						clmDailyReportDto.setANHStatus(hospital.getNetworkHospitalType() != null ? (hospital.getNetworkHospitalType().toLowerCase().contains("agreed") ? "ANH" : "Non ANH") : "");
						clmDailyReportDto.setHospitalType(hospital.getHospitalType() != null ? hospital.getHospitalType().getValue() :"");
					}					
//				}
				
//				NewIntimationDto intimationDto = getIntimationService().getIntimationDto(claim.getIntimation(), entityManager);
//				claimDto.setNewIntimationDto(intimationDto);
				
				
				clmDailyReportDto.setSno(resultClaimList.indexOf(claim)+1);
				
				clmDailyReportDto.setHospitalDate(claim.getIntimation().getAdmissionDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(claim.getIntimation().getAdmissionDate()) : "");
				clmDailyReportDto.setHospitalType(clmDailyReportDto.getHospitalType() != null && ("Network").equalsIgnoreCase(clmDailyReportDto.getHospitalType()) ? "NETWORK" : "NON-NETWORK");
				clmDailyReportDto.setPlannedAdmission(clmDailyReportDto.getPlannedAdmission() != null && StringUtils.containsIgnoreCase(clmDailyReportDto.getPlannedAdmission(), "plan") ? "Yes" : "No");
				
				Double sumInsured = dbcalService.getInsuredSumInsured(String.valueOf(claim.getIntimation().getInsured().getInsuredId()), claim.getIntimation().getPolicy().getKey(),claim.getIntimation().getInsured().getLopFlag());
				clmDailyReportDto.setSuminsured(String.valueOf(sumInsured.intValue()));
				
				Query fvrQuery = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKey");
				fvrQuery.setParameter("claimKey", claim.getKey());
				fvrReqList = (List<FieldVisitRequest>)fvrQuery.getResultList();
				
				if(fvrReqList != null && !fvrReqList.isEmpty()){
					fvrReq = fvrReqList.get(fvrReqList.size()-1);
					entityManager.refresh(fvrReq);
					clmDailyReportDto.setFieldDoctorNameAllocated(fvrReq.getRepresentativeName() != null ? fvrReq.getRepresentativeName() : "");
			
					if(fvrReq.getRepresentativeCode() != null){
						Query q = entityManager.createNamedQuery("TmpFvR.findByCode");
						q.setParameter("code", fvrReq.getRepresentativeCode());
						fvrList = q.getResultList();
						if(fvrList != null && !fvrList.isEmpty()){
							entityManager.refresh(fvrList.get(0));
							String contactNo = "";
							if(fvrList.get(0).getMobileNumber() != null){
								contactNo = fvrList.get(0).getMobileNumber() != null ? fvrList.get(0).getMobileNumber().toString() : "";
							}
							if(fvrList.get(0).getPhoneNumber() != null){
								contactNo = ("").equalsIgnoreCase(contactNo) ? ( fvrList.get(0).getPhoneNumber().toString()) : ( contactNo + " / " + fvrList.get(0).getPhoneNumber().toString());
							}
							clmDailyReportDto.setContactNumOfDoctor(contactNo);
						}
					}
					
					SimpleDateFormat dtFrmt = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
					clmDailyReportDto.setDataOfAllocationWithTime(fvrReq.getAssignedDate() != null ? dtFrmt.format(fvrReq.getAssignedDate()).toUpperCase():"");
				}				
				
				resultDto.add(clmDailyReportDto);
				clmDailyReportDto = null;
				hospital = null;
				fvrReqList = null;
				fvrReq = null;
				fvrList = null;
			}
			dbcalService = null;
			ClaimsReportService.popinReportLog(entityManager, userId, "ClaimsDailyReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
		}
		}
		utx.commit();
		}catch(Exception e){
			ClaimsReportService.popinReportLog(entityManager, userId, "ClaimsDailyReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	  }	
		return resultDto;
	}	
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<ClaimsStatusReportDto> getClaimsStatusReport(
			Map<String, Object> filters, UsertoCPUMappingService userCPUMapService, DBCalculationService dbService) {
		List<ClaimsStatusReportDto> resultDtoList = new ArrayList<ClaimsStatusReportDto>();
		//List<ClaimsStatusReportDto> paidResultDtoList = new ArrayList<ClaimsStatusReportDto>();
		Date fromDate = null;
		Date endDate = null;
		String stageName = null;
		String cpuCode = null;
		String userId = "";
		String period = null;
		Date fyFromDate = null;
		Date fyEndDate = null;
		if (filters != null && !filters.isEmpty()) {

			if (filters.containsKey("fromDate")
					&& filters.get("fromDate") != null) {

				fromDate = (Date) filters.get("fromDate");
			}

			if (filters.containsKey("endDate")
					&& filters.get("endDate") != null) {

				endDate = (Date) filters.get("endDate");
			}

			if (filters.containsKey("claimStageName")
					&& filters.get("claimStageName") != null) {

				stageName = (String) filters.get("claimStageName");
				
			}
			
			if (filters.containsKey("cpuKey") && filters.get("cpuKey") != null) {

				cpuCode = (String) filters.get("cpuKey");
			}
			if (filters.containsKey(BPMClientContext.USERID) && filters.get(BPMClientContext.USERID) != null){
				userId = (String)filters.get(BPMClientContext.USERID);
			}
			
			if(filters.containsKey("periodType")
					&& filters.get("periodType") != null){
				period = (String) filters.get("periodType");
			}
			
			if (filters.containsKey("fyFromDate")
					&& filters.get("fyFromDate") != null) {

				fyFromDate = (Date) filters.get("fyFromDate");
			}

			if (filters.containsKey("fyEndDate")
					&& filters.get("fyEndDate") != null) {

				fyEndDate = (Date) filters.get("fyEndDate");
			}
			
			
			
			/*List<Reimbursement> resultReimbursementList = new ArrayList<Reimbursement>();
			List<Claim> resultClaimList = new ArrayList<Claim>();
			List<Preauth> resultPreauthList = new ArrayList<Preauth>();
			List<StageInformation> stageInfoList = new ArrayList<StageInformation>();*/
			
			try {		
				VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
				
				utx.setTransactionTimeout(3600);
				utx.begin();

				/*final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				
				final CriteriaQuery<StageInformation> criteriaStageInfoQuery = builder
						.createQuery(StageInformation.class);*/
				
				
//				final CriteriaQuery<Reimbursement> criteriaReimbQuery = builder
//						.createQuery(Reimbursement.class);
//				final CriteriaQuery<Preauth> criteriaCashlessQuery = builder
//						.createQuery(Preauth.class);				
//				final CriteriaQuery<Claim> criteriaClaimQuery = builder
//						.createQuery(Claim.class);
				
				
				//Root<StageInformation> stageInfoRoot = criteriaStageInfoQuery.from(StageInformation.class);

//				Root<Preauth> preauthRoot = criteriaCashlessQuery.from(Preauth.class);
//				Root<Claim> claimRoot = criteriaClaimQuery.from(Claim.class);
				
				
				
//				Join<Claim,Intimation> intimationJoin = claimRoot.join(
//						"intimation", JoinType.INNER);				
//				
//				Root<Reimbursement> reimbRoot = criteriaReimbQuery.from(Reimbursement.class);
//				Join<Reimbursement,Claim> claimJoin = reimbRoot.join(
//						"claim", JoinType.INNER);
				/*List<Predicate> predicates = new ArrayList<Predicate>();
				
				if(fromDate != null && endDate != null){
					Expression<Date> fromDateExpression = stageInfoRoot
							.<Date> get("createdDate");
					Predicate fromDatePredicate = builder
							.greaterThanOrEqualTo(fromDateExpression,
									fromDate);
					predicates.add(fromDatePredicate);

					Expression<Date> toDateExpression = stageInfoRoot
							.<Date> get("createdDate");
					Calendar c = Calendar.getInstance();
					c.setTime(endDate);
					c.add(Calendar.DATE, 1);
					endDate = c.getTime();					
					Predicate toDatePredicate = builder
							.lessThanOrEqualTo(toDateExpression, endDate);
					predicates.add(toDatePredicate);
			}
				if(cpuKey != null){
					Predicate cpuKeyPred = builder.equal(stageInfoRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"),cpuKey);
					predicates.add(cpuKeyPred);
				}
				else{
					List<Long> cpuKeyList = userCPUMapService.getCPUCodeList(userId, entityManager);
					Predicate cpuKeyListPred = stageInfoRoot.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
					predicates.add(cpuKeyListPred);
				}

				if (stageName != null) {
					
					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLOSED_CLAIMS)){
						
						List<Long> closedStatusKeys = new ArrayList<Long>();
						
						closedStatusKeys.add(ReferenceTable.CREATE_ROD_CLOSED);
						closedStatusKeys.add(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
						closedStatusKeys.add(ReferenceTable.ZONAL_REVIEW_CLOSED);
						closedStatusKeys.add(ReferenceTable.CLAIM_REQUEST_CLOSED);
						closedStatusKeys.add(ReferenceTable.BILLING_CLOSED_STATUS);
						closedStatusKeys.add(ReferenceTable.FINANCIAL_CLOSED);*/
												
//						Predicate rodStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"),ReferenceTable.CREATE_ROD_STAGE_KEY);
//						Predicate rodStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CREATE_ROD_CLOSED);
						
//						Predicate rodClosedPred = builder.and(rodStagePred,rodStatusPred);
					
//						Predicate billEntryStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILL_ENTRY_STAGE_KEY);
//						Predicate billEntryStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
					
//						Predicate billingClosedPred = builder.and(billEntryStagePred,billEntryStatusPred);
						
//						Predicate zonalReviewStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//						Predicate zonalReviewStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.ZONAL_REVIEW_CLOSED);
						
//						Predicate zonalreviewClosedPed = builder.and(zonalReviewStagePred,zonalReviewStatusPred);
						
//						Predicate processClaimStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
//						Predicate processClaimStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.CLAIM_REQUEST_CLOSED);
						
//						Predicate processClaimReqClosedPred = builder.and(processClaimStagePred,processClaimStatusPred); 
						
//						Predicate claimBillingStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.BILLING_STAGE);
//						Predicate claimBillingStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.BILLING_CLOSED_STATUS);

//						Predicate claimBillingClosedPred = builder.and(claimBillingStagePred,claimBillingStatusPred);
											
//						Predicate finStagePred = builder.equal(stageInfoRoot.<Stage>get("stage").<Long>get("key"), ReferenceTable.FINANCIAL_STAGE);
//						Predicate finStatusPred = builder.equal(stageInfoRoot.<Status>get("status").<Long>get("key"), ReferenceTable.FINANCIAL_CLOSED);

//						Predicate finanClosedPred = builder.and(finStagePred,finStatusPred);
						
//						Predicate claimClosedPred = builder.or(rodClosedPred,billingClosedPred,zonalreviewClosedPed,processClaimReqClosedPred,claimBillingClosedPred,finanClosedPred);
						
//						Predicate claimClosedPred = builder.or(rodStatusPred,billEntryStatusPred,zonalReviewStatusPred,processClaimStatusPred,claimBillingStatusPred,finStatusPred);
						
					/*	
						Predicate claimClosedPred = stageInfoRoot.<Status>get("status").<Long>get("key").in(closedStatusKeys);
						
						
						predicates.add(claimClosedPred);
						
					}*/
					/*else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.REJECTED_CLAIMS)){
				
//						Predicate finRejectStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.FINANCIAL_STAGE);
						
//						List<Long> financialRejKeyList = new ArrayList<Long>();      
						
//						financialRejKeyList.add(ReferenceTable.FINANCIAL_REJECT_STATUS);
//						financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS);
//						financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS);
//						financialRejKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS);
						
						Predicate finRejectStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"),ReferenceTable.FINANCIAL_REJECT_STATUS);
						
//						Predicate financialRejectpred = builder.and(finRejectStagePredicate,finRejectStatusPredicate);
						
//						Predicate medRejectStagePredicate = builder.equal( stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						
//						List<Long> medicalRejKeyList = new ArrayList<Long>();
//						medicalRejKeyList.add(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
//						medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS);
//						medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS);
//						medicalRejKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_REDRAFT_REJECT_STATUS);
						
						Predicate medRejectStatusPredicate = builder.equal( stageInfoRoot.<Status> get("status").<Long> get("key"),ReferenceTable.CLAIM_REQUEST_REJECT_STATUS);
						
//						Predicate  medRejectPredicate = builder.and(medRejectStagePredicate,medRejectStatusPredicate);
						
//						Predicate claimRejectPred = builder.or(medRejectPredicate,financialRejectpred);
						
						Predicate claimRejectPred = builder.or(medRejectStatusPredicate,finRejectStatusPredicate);
						
						predicates.add(claimRejectPred);
						
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PAID_STAUS)){
						
//						Predicate finApproveStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
						Predicate finApproveStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.FINANCIAL_APPROVE_STATUS);
//						Predicate approvePred = builder.and(finApproveStagePredicate,finApproveStatusPredicate); 
		
						predicates.add(finApproveStatusPredicate);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){       
						
//						List<Long> rodStageKey = new ArrayList<Long>();
//						rodStageKey.add(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);					ONlY  ACK to be handled in case of bills received status.
//						rodStageKey.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
//						rodStageKey.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
//						rodStageKey.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//						
//						Predicate rodStagePredicate = claimRoot.<Stage> get("stage").<Long> get("key").in(rodStageKey);						
						
//						List<Long> rodStageKey = new ArrayList<Long>();
//						rodStageKey.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
//						rodStageKey.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
//						rodStageKey.add(ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
//						Predicate rodStagePredicate = stageInfoRoot.<Stage> get("stage").<Long> get("key").in(rodStageKey);
						
						Predicate rodStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.CREATE_ROD_STATUS_KEY);

//						Predicate rodApprovedPredicate = builder.and(rodStagePredicate,rodStatusPredicate);
						
//						predicates.add(rodApprovedPredicate);
						predicates.add(rodStatusPredicate);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
						
					
						List<Long> cashlessStatusKeys = new ArrayList<Long>();
						
						cashlessStatusKeys.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
						cashlessStatusKeys.add(ReferenceTable.PREAUTH_APPROVE_STATUS);
						cashlessStatusKeys.add(ReferenceTable.PREAUTH_REJECT_STATUS);
						cashlessStatusKeys.add(ReferenceTable.PREAUTH_QUERY_STATUS);
						cashlessStatusKeys.add(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
						cashlessStatusKeys.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS);
						cashlessStatusKeys.add(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
						cashlessStatusKeys.add(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
						cashlessStatusKeys.add(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
						cashlessStatusKeys.add(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
						cashlessStatusKeys.add(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
						cashlessStatusKeys.add(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);
						cashlessStatusKeys.add(ReferenceTable.PROCESS_REJECTED);
						cashlessStatusKeys.add(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
						cashlessStatusKeys.add(ReferenceTable.DOWNSIZE_APPROVED_STATUS);
						cashlessStatusKeys.add(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
						
						*/
						
//						preauthKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
						
//						Predicate preAuthPremedicalStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
				
//						List<Long> preauthPremedicalstatusKeyList = new ArrayList<Long>();
//						preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS);
//						preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS);
//						preauthPremedicalstatusKeyList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SEND_FOR_PROCESSING_STATUS);
						
//						Predicate preAuthPremedicalStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preauthPremedicalstatusKeyList);
						
//						Predicate preAuthPremedicalPredicate = builder.and(preAuthPremedicalStagePredicate,preAuthPremedicalStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.PREAUTH_STAGE);
//						Predicate preAuthStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PREAUTH_STAGE);
						
//						List<Long> preauthstatusKeyList = new ArrayList<Long>();
//						preauthstatusKeyList.add(ReferenceTable.PREAUTH_APPROVE_STATUS);
//						preauthstatusKeyList.add(ReferenceTable.PREAUTH_REJECT_STATUS);
//						preauthstatusKeyList.add(ReferenceTable.PREAUTH_QUERY_STATUS);
//						preauthstatusKeyList.add(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS);
						
//						Predicate preAuthStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preauthstatusKeyList);
						
//						Predicate preAuthPredicate = builder.and(preAuthStagePredicate,preAuthStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
//						Predicate preMedicalEnhStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
						
//						List<Long> preMedicalEnhstatusKeyList = new ArrayList<Long>();
//						preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS);
//						preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS);
//						preMedicalEnhstatusKeyList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_PROCESSING_STATUS);
						
//						Predicate preMedicalEnhStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(preMedicalEnhstatusKeyList);
						
//						Predicate preMedicalEnhPredicate = builder.and(preMedicalEnhStagePredicate,preMedicalEnhStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.ENHANCEMENT_STAGE);
//						Predicate enhStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.ENHANCEMENT_STAGE);
						
//						List<Long> enhstatusKeyList = new ArrayList<Long>();
//						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_APPROVE_STATUS);
//						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_REJECT_STATUS);
//						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_QUERY_STATUS);
//						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS);
//						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS);
//						enhstatusKeyList.add(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS);
						
//						Predicate enhStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(enhstatusKeyList);
						
//						Predicate enhPredicate = builder.and(enhStagePredicate,enhStatusPredicate);
						
						
//						preauthKeyList.add(ReferenceTable.PROCESS_REJECTION_STAGE);
//						Predicate processRejectionStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_REJECTION_STAGE);
						
//						List<Long> processRejectionstatusKeyList = new ArrayList<Long>();
//						processRejectionstatusKeyList.add(ReferenceTable.PROCESS_REJECTED);
//						processRejectionstatusKeyList.add(ReferenceTable.PREMEDICAL_WAIVED_REJECTION);
						
//						Predicate processRejectionStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(processRejectionstatusKeyList);
						
//						Predicate processRejectionPredicate = builder.and(processRejectionStagePredicate,processRejectionStatusPredicate);	
								
								
								
//						preauthKeyList.add(ReferenceTable.DOWNSIZE_STAGE);
//						Predicate downsizeStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.DOWNSIZE_STAGE);
						
//						List<Long> downsizestatusKeyList = new ArrayList<Long>();
//						downsizestatusKeyList.add(ReferenceTable.DOWNSIZE_APPROVED_STATUS);
						
//						Predicate downsizeStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(downsizestatusKeyList);
						
//						Predicate downsizePredicate = builder.and(downsizeStagePredicate,downsizeStatusPredicate);	
						
						
//						preauthKeyList.add(ReferenceTable.WITHDRAW_STAGE);
//						Predicate withDrawStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.WITHDRAW_STAGE);
						
//						List<Long> withDrawstatusKeyList = new ArrayList<Long>();
//						withDrawstatusKeyList.add(ReferenceTable.STANDALONE_WITHDRAW_STATUS);
						
//						Predicate withDrawStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(withDrawstatusKeyList);
						
//						Predicate withDrawPredicate = builder.and(withDrawStagePredicate,withDrawStatusPredicate);	
						
								
//						Predicate cashlessPredicate = builder.or(preAuthPremedicalPredicate,preAuthPredicate,preMedicalEnhPredicate,enhPredicate,processRejectionPredicate,downsizePredicate,withDrawPredicate);
						
					/*	Predicate cashlessPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(cashlessStatusKeys);
						
						predicates.add(cashlessPredicate); 
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.BILLING_COMPLETED)){
//						Predicate billingStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"), ReferenceTable.BILLING_STAGE);
						Predicate billingStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
						
//						Predicate billingApprovedPred = builder.and(billingStagePredicate,billingStatusPredicate);
//						predicates.add(billingApprovedPred);
						
						predicates.add(billingStatusPredicate);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.MEDICAL_APPROVAL)){
//						Predicate medicalApprovStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						Predicate medicalApprovStatusPredicate = builder.equal(stageInfoRoot.<Status> get("status").<Long> get("key"), ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);

//						Predicate medicalApprovedPred = builder.and(medicalApprovStagePredicate,medicalApprovStatusPredicate);
//						predicates.add(medicalApprovedPred);
						predicates.add(medicalApprovStatusPredicate);
					}
					else if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIM_QUERY)){
						
//						Predicate medicalQueryStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						
						List<Long> queryStatusKeyList = new ArrayList<Long>();
						queryStatusKeyList.add(ReferenceTable.CLAIM_REQUEST_QUERY_STATUS);
						queryStatusKeyList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
						
						
						
						
//						queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS);
//						queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REDRAFT_STATUS);
//						queryStatusKeyList.add(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS);
						
//						Predicate medicalQueryStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(queryStatusKeyList);
						
//						Predicate clmMedicalQueryPred = builder.and(medicalQueryStagePredicate,medicalQueryStatusPredicate);						
			
						
//						Predicate financialQueryStagePredicate = builder.equal(stageInfoRoot.<Stage> get("stage").<Long> get("key"),ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
//						List<Long> finQuerystatusKeyList = new ArrayList<Long>();
						
//						finQuerystatusKeyList.add(ReferenceTable.FINANCIAL_QUERY_STATUS);
//						finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS);
//						finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS);
//						finQuerystatusKeyList.add(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS);
						
//						Predicate financialQueryStatusPredicate = stageInfoRoot.<Status> get("status").<Long> get("key").in(finQuerystatusKeyList);

//						Predicate financialQueryPred = builder.and(financialQueryStagePredicate,financialQueryStatusPredicate);
						
						
//						Predicate claimQueryPred = builder.or(clmMedicalQueryPred,financialQueryPred);
						Predicate claimQueryPred = stageInfoRoot.<Status> get("status").<Long> get("key").in(queryStatusKeyList);
						
						
						predicates.add(claimQueryPred);
						
					}
					}*/
				
					
//					if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){           ONlY  ACK to be handled in case of bills received status.
//						
//						Predicate clmCpuPredicate = builder.equal(claimRoot.<Intimation> get("intimation").<TmpCPUCode> get("cpuCode")
//													.<Long> get("key"), cpuKey);
//						predicates.add(clmCpuPredicate);
//					}
//					else{
//					Predicate reimbCpuPredicate = builder.equal(
//							stageInfoRoot.<Claim>get("claim").<Intimation> get("intimation")
//									.<TmpCPUCode> get("cpuCode")
//									.<Long> get("key"), cpuKey);
//					predicates.add(reimbCpuPredicate);
//					}				

//				if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_PRE_AUTH_STATUS)){
////					criteriaCashlessQuery.select(preauthRoot).where(
////							builder.and(predicates.toArray(new Predicate[] {})));
//					
////					final TypedQuery<Preauth> finalpreauthQuery = entityManager
////							.createQuery(criteriaCashlessQuery);
////					resultPreauthList = finalpreauthQuery.getResultList();
//					
//					
//					criteriaStageInfoQuery.select(stageInfoRoot).where(
//							builder.and(predicates.toArray(new Predicate[] {})));
//					
//					final TypedQuery<StageInformation> finalpreauthQuery = entityManager
//							.createQuery(criteriaStageInfoQuery);
//										
//					stageInfoList = finalpreauthQuery.getResultList();
//					
//					if(stageInfoList != null && !stageInfoList.isEmpty()){
//						
//						for(StageInformation stageInfo : stageInfoList){
//							resultClaimList.add(stageInfo.getClaim());	
//						}
//					}					
//				}
				
//				if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIMS_BILL_RECEIVED_STATUS)){     ONlY  ACK to be handled in case of bills received status.
//					criteriaClaimQuery.select(claimRoot).where(
//							builder.and(predicates.toArray(new Predicate[] {})));
//					
//					final TypedQuery<Claim> finalClaimQuery = entityManager
//							.createQuery(criteriaClaimQuery);
//					resultClaimList = finalClaimQuery.getResultList();
//				}
//				else{
//				}
				
				
				/*	criteriaStageInfoQuery.select(stageInfoRoot).where(
							builder.and(predicates.toArray(new Predicate[] {})));	
					final TypedQuery<StageInformation> reimbQuery = entityManager
							.createQuery(criteriaStageInfoQuery);
					stageInfoList = reimbQuery.getResultList();*/

								

//				ArrayList<Reimbursement> tempResultReimbursementList = null; 
//				if (resultClaimList != null && !resultClaimList.isEmpty()){
//					tempResultReimbursementList= new ArrayList<Reimbursement>();
//					for(Claim claimObj : resultClaimList){
//						Reimbursement reimbObj = new Reimbursement();
//						List<Reimbursement> reimbObjList = (new ReimbursementService()).getReimbursementByClaimKey(claimObj.getKey(), entityManager);
//						if(reimbObjList != null && reimbObjList.size() > 0 ){							
//							Reimbursement reimb = reimbObjList.get(0);
//							entityManager.refresh(reimb);
//							reimbObj = reimb;
//						}
//						reimbObj.setClaim(claimObj);
//						tempResultReimbursementList.add(reimbObj);
//					}					
//				}			
//
//				if(stageInfoList == null || stageInfoList.isEmpty()){     
//					stageInfoList= new ArrayList<StageInformation>();
//					if(tempResultReimbursementList != null && !tempResultReimbursementList.isEmpty()){
//						stageInfoList.addAll(tempResultReimbursementList);
//					}
//				}
//				else{
//					if(tempResultReimbursementList != null && !tempResultReimbursementList.isEmpty()){
//						resultReimbursementList.addAll(tempResultReimbursementList);
//					}	
//				}				
//					List<StageInformation> finalStageList = new ArrayList<StageInformation>();
//					
//				if (stageInfoList != null && !stageInfoList.isEmpty()) {					
//					
//					if (cpuKey != null) {						
//						for(StageInformation stageInfoObj :  stageInfoList){
//							entityManager.refresh(stageInfoObj);
//							Reimbursement reimbObj = (new ReimbursementService()).getReimbursementbyRod(stageInfoObj.getReimbursementKey(), entityManager);
//							if(reimbObj != null){
//								entityManager.refresh(reimbObj);	
//							}
//							
//							Claim claim = stageInfoObj.getClaim();
//							if(claim != null){
//								entityManager.refresh(claim);	
//							
//								if(claim.getIntimation() != null && claim.getIntimation().getCpuCode() != null && claim.getIntimation().getCpuCode().getKey().equals(cpuKey)){
//									finalStageList.add(stageInfoObj);
//								}
//							}
//						}
//					}
//					else{
//						finalStageList.addAll(stageInfoList);	
//					}
//					
//					if(!finalStageList.isEmpty() ){
//					for(StageInformation stageInfo :  finalStageList){
					
					
					/*if(stageInfoList != null && !stageInfoList.isEmpty()){
					for(StageInformation stageInfo :  stageInfoList){
						
						entityManager.refresh(stageInfo);
						//Reimbursement reimbObj = null;
						Reimbursement reimbObj = stageInfo.getReimbursement();
						//if(stageInfo.getReimbursementKey() != null){
							//reimbObj = (new ReimbursementService()).getReimbursementbyRod(stageInfo.getReimbursementKey(), entityManager);
							if(reimbObj != null){
								entityManager.refresh(reimbObj);
							}
						//}
						
						Claim claim = stageInfo.getClaim();
						entityManager.refresh(claim);

						ClaimMapper clmMapper = ClaimMapper.getInstance();
						ClaimDto claimDto = clmMapper.getClaimDto(claim);						

//						NewIntimationDto intimationDto = (new IntimationService()).getIntimationDto(claim.getIntimation(),
//										entityManager);
						
//						NewIntimationDto intimationDto = NewIntimationMapper.getInstance().getNewIntimationDto(claim.getIntimation());
						
						Long hospKey = claim.getIntimation().getHospital();
						
						Hospitals hospObj = getHospitalDetailsByKey(hospKey);
						
//						HospitalDto hospDto = new HospitalDto(hospObj);
//						hospDto.setRegistedHospitals(hospObj);
//						intimationDto.setHospitalDto(hospDto);
//						
//						claimDto.setNewIntimationDto(intimationDto);
						
//						ClaimsStatusReportDto clmsStatusReportDto = new ClaimsStatusReportDto(
//								claimDto);
						
						String hospName = hospObj.getName() != null ? hospObj.getName() : "";
						ClaimsStatusReportDto clmsStatusReportDto = new ClaimsStatusReportDto(
								claim,hospName);
						
						clmsStatusReportDto.setReimbursementKey(stageInfo.getReimbursement().getKey());
						clmsStatusReportDto.setSno(stageInfoList
								.indexOf(stageInfo) + 1);
						String finyear="";
						if(claim.getIntimation().getCreatedDate() != null ){
							StringBuffer dateStr = new StringBuffer( new SimpleDateFormat("dd/MM/yyyy").format(claim.getIntimation().getCreatedDate()));
							finyear = dateStr.substring(dateStr.length()-4, dateStr.length());
						}
						clmsStatusReportDto.setFinacialYear(finyear);
						String diagnosis = "";
						String icdCode = "";
					    if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PRE_AUTH_STATUS) && stageInfo.getPreauth() != null){
					    	
					    	Preauth preauthObj =  stageInfo.getPreauth();
							//Query q = entityManager
								//	.createNamedQuery("Preauth.findByKey");
							//q.setParameter("preauthKey", stageInfo.getCashlessKey());
							//List<Preauth> preauthList = q.getResultList();
							//if(preauthList != null && !preauthList.isEmpty()){
						
							//preauthObj =  preauthList.get(0);
							//entityManager.refresh(preauthObj);
					    	if(preauthObj != null && preauthObj.getKey() != null){
					    	diagnosis = (new PreauthService())
									.getDiagnosisByPreauthKey(preauthObj.getKey(), entityManager);	
							Query diagQuery = entityManager
									.createNamedQuery("PedValidation.findByTransactionKey");
							diagQuery.setParameter("transactionKey", 
									preauthObj.getKey());

							List<PedValidation> diagList = diagQuery
									.getResultList();
							if (diagList != null && !diagList.isEmpty()) {

								List<DiagnosisDetailsTableDTO> diagListDto = (PreMedicalMapper.getInstance())
										.getNewPedValidationTableListDto(diagList);

								for (DiagnosisDetailsTableDTO diagDto : diagListDto) {
									
									if (diagDto.getIcdCode() != null) {
										IcdCode icdCodeObj = entityManager
												.find(IcdCode.class, diagDto
														.getIcdCode().getId());
										if (icdCodeObj != null) {
											icdCode = ("")
													.equalsIgnoreCase(icdCode) ? icdCodeObj
													.getValue() : icdCode
													+ " , "
													+ icdCodeObj.getValue();
										}

									}
								}
								clmsStatusReportDto.setIcdCode(icdCode);
								clmsStatusReportDto.setDiagnosis(diagnosis);
							}
					     }
							
							Query historyQ = entityManager.createNamedQuery("StageInformation.findCashlessByStageInfoKey");
							historyQ.setParameter("stageInfoKey",stageInfo.getKey());
							
							List<StageInformation> stageList = historyQ.getResultList();
							
							if(stageList != null && !stageList.isEmpty()){
								StageInformation stageObj = stageList.get(0);
								if(stageObj != null){
									entityManager.refresh(stageObj);
									clmsStatusReportDto.setPreauthApprovalDate(stageObj.getCreatedDate() != null ? new SimpleDateFormat("dd-MM-yy").format(stageObj.getCreatedDate()) : "");
								}
								if(preauthObj != null && preauthObj.getTotalApprovalAmount() != null){
									clmsStatusReportDto.setPreauthAmt(String.valueOf(preauthObj.getTotalApprovalAmount().intValue()));
								}
							}
							
							String statusValue =  stageInfo.getStage() != null && stageInfo.getStage().getStageName() != null ? stageInfo.getStage().getStageName() + " - " : ""; 
							statusValue = statusValue + ( stageInfo.getStatus() != null && stageInfo.getStatus().getProcessValue() != null ? stageInfo.getStatus().getProcessValue() : "") ;
							clmsStatusReportDto.setStatus(statusValue);
							resultDtoList.add(clmsStatusReportDto);
							clmsStatusReportDto = null;
					    }
					    else
					    {						
					    clmsStatusReportDto.setCpuCode(String.valueOf(claim.getIntimation().getCpuCode().getCpuCode()));
					    clmsStatusReportDto.setInitialProvisionAmount(claim.getCurrentProvisionAmount() != null ? String.valueOf(claim.getCurrentProvisionAmount().intValue()) : "");
					    clmsStatusReportDto.setFvrUploaded("N");
					    
					    Double deductAmt = 0d;
						Double faApprovedAmt = 0d;
						String paidDate = "";
						Double claimedAmt = 0d;
						Double billingApproveAmt = 0d;
						
//						claimedAmt =   reimbObj.getCurrentProvisionAmt() != null ? reimbObj.getCurrentProvisionAmt() : 0d;
						
						if(clmsStatusReportDto.getReimbursementKey() != null){
						Query docSummaryQuery = entityManager.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
						docSummaryQuery.setParameter("reimbursementKey", clmsStatusReportDto.getReimbursementKey());
						
						List<RODDocumentSummary> docSummaryList = docSummaryQuery.getResultList();
						if(docSummaryList != null && !docSummaryList.isEmpty()){
							
							reimbObj = docSummaryList.get(0).getReimbursement();
							if(reimbObj != null){
								entityManager.refresh(reimbObj);
							}
							for(RODDocumentSummary rodBillSummary : docSummaryList){
								
								claimedAmt += rodBillSummary.getBillAmount() != null ? rodBillSummary.getBillAmount() : 0d; 		
							}						
						}
						
						}
						
						if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PAID_STAUS))   
						{
						List<ClaimPayment> paymentListByRodNumber = (new PaymentProcessCpuService()).getPaymentDetailsByRodNumber(reimbObj.getRodNumber(),this.entityManager);
						
						if(paymentListByRodNumber != null && !paymentListByRodNumber.isEmpty()){
							
							for(ClaimPayment paymentObj : paymentListByRodNumber){
								paidDate =	paymentObj.getCreatedDate() != null ?	(("").equalsIgnoreCase(paidDate) 
														? new SimpleDateFormat("dd/MM/yy").format(paymentObj.getCreatedDate()) 
																	: paidDate + " - " + new SimpleDateFormat("dd/MM/yy").format(paymentObj.getCreatedDate())) : paidDate;
								faApprovedAmt += ( paymentObj.getApprovedAmount() != null ?  paymentObj.getApprovedAmount() : 0d); 
							
							}
						}
						
						clmsStatusReportDto.setPaidDate(paidDate);
						clmsStatusReportDto.setPaidAmout(String.valueOf(faApprovedAmt.intValue()));
						
//						Reimbursement rodObj = (new ReimbursementService()).getReimbursementbyRod(clmsStatusReportDto.getReimbursementKey(), entityManager);
						
						
							
							if(reimbObj != null && paymentListByRodNumber == null){
								paidDate = "";
								faApprovedAmt = 0d;	
								if(("0").equalsIgnoreCase(clmsStatusReportDto.getPaidAmout())){
									faApprovedAmt += reimbObj.getFinancialApprovedAmount() != null ? reimbObj.getFinancialApprovedAmount() : 0d;
								}
								
								if(("").equalsIgnoreCase(clmsStatusReportDto.getPaidDate())){
									paidDate = reimbObj.getFinancialCompletedDate() != null ? (("").equalsIgnoreCase(paidDate) ? new SimpleDateFormat("dd/MM/yy").format(reimbObj.getFinancialCompletedDate()) : paidDate + " - " + new SimpleDateFormat("dd/MM/yy").format(reimbObj.getFinancialCompletedDate())):paidDate;
								}
							}
						
//						List<RODDocumentSummary> billsList = (new CreateRODService()).getBillDetailsByRodKey(reimbObj.getKey(),entityManager);
//						
//						if(billsList != null && !billsList.isEmpty()){
//							
//							for(RODDocumentSummary rodBill : billsList){
//								entityManager.refresh(rodBill);
//								List<RODBillDetails> rodBillDetailsList = (new CreateRODService()).getBillEntryDetails(rodBill.getKey(),this.entityManager);
//								if(rodBillDetailsList != null && !rodBillDetailsList.isEmpty()){
//								
//									for(RODBillDetails rodBillDetail :rodBillDetailsList){
//										entityManager.refresh(rodBillDetail);
//										deductAmt += rodBillDetail.getNonPayableAmount() != null ? rodBillDetail.getNonPayableAmount() : 0d;
//									}
//								}
//							}
//						}
								
//						//if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PAID_STAUS))   
//						//{	
							if(claimedAmt != null && claimedAmt > 0 && clmsStatusReportDto.getPaidAmout() != null ) {
								deductAmt = claimedAmt.doubleValue() > faApprovedAmt ? claimedAmt.doubleValue() - faApprovedAmt : Integer.valueOf("0");
							}
						}
						
						
						billingApproveAmt = reimbObj.getBillingApprovedAmount() != null ? reimbObj.getBillingApprovedAmount() : 0d;
						clmsStatusReportDto.setClaimedAmount(claimedAmt != null ? String.valueOf(claimedAmt.intValue()) : "");
						clmsStatusReportDto.setBillAmount(clmsStatusReportDto.getClaimedAmount());
						clmsStatusReportDto.setBillingApprovedAmount(String.valueOf(billingApproveAmt.intValue()));
						clmsStatusReportDto.setDeductedAmount(String.valueOf(deductAmt.toString()));
						clmsStatusReportDto.setPaidAmout(("0").equalsIgnoreCase(clmsStatusReportDto.getPaidAmout()) ? String.valueOf(faApprovedAmt.intValue()) : clmsStatusReportDto.getPaidAmout());
						clmsStatusReportDto.setPaidDate(("").equalsIgnoreCase(clmsStatusReportDto.getPaidDate()) ? paidDate : clmsStatusReportDto.getPaidDate());					    
					    
						DBCalculationService dbcalService = new DBCalculationService();
						Double sumInsured = dbcalService.getInsuredSumInsured(
								String.valueOf(claim.getIntimation()
										.getInsured().getInsuredId()),
								claim.getIntimation().getPolicy()
										.getKey());
						clmsStatusReportDto.setSuminsured(String
								.valueOf(sumInsured.intValue()));
						if(stageName.equalsIgnoreCase(SHAConstants.CLAIM_QUERY))
						{
						
						ReimbursementQuery reimbQueryObj = (new ReimbursementService()).getReimbursementQueryByReimbursmentKey(reimbObj.getKey(),entityManager);
						if(reimbQueryObj != null){
							entityManager.refresh(reimbQueryObj);
							clmsStatusReportDto.setQueryRaisedDate(reimbQueryObj.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbQueryObj.getCreatedDate()) : "");
							clmsStatusReportDto.setQueryRaisedRemarks(reimbQueryObj.getQueryRemarks() != null ? reimbQueryObj.getQueryRemarks() : "");
							clmsStatusReportDto.setQueryReplyDate(reimbQueryObj.getQueryReplyDate() != null ? new SimpleDateFormat("dd/MM/yy").format(reimbQueryObj.getQueryReplyDate()) : "");
						
							//if(stageName.equalsIgnoreCase(SHAConstants.CLAIM_QUERY))
							//{
								clmsStatusReportDto.setUserId(reimbQueryObj.getModifiedBy() != null ? reimbQueryObj.getModifiedBy() : "");
							}
						
						}
						
						if(stageName.equalsIgnoreCase(SHAConstants.REJECTED_CLAIMS))
						{
						ReimbursementRejection reimbreject = (new ReimbursementRejectionService()).getRejectionByReimbursementKey(reimbObj.getKey(), entityManager);
						if(reimbreject != null){
							entityManager.refresh(reimbreject);
							clmsStatusReportDto.setRejectDate(stageInfo.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(stageInfo.getCreatedDate()) : "");
							clmsStatusReportDto.setRejectionRemarks(reimbreject.getRejectionRemarks() != null ? reimbreject.getRejectionRemarks(): "");
						
							//if(stageName.equalsIgnoreCase(SHAConstants.REJECTED_CLAIMS))
							//{
								clmsStatusReportDto.setUserId(reimbreject.getCreatedBy() != null ? reimbreject.getCreatedBy() : "");
								clmsStatusReportDto.setUserName(reimbreject.getCreatedBy() != null ? reimbreject.getCreatedBy() : "");
							}
						
						}													
								icdCode ="";
								diagnosis = "";
								Query diagQuery = entityManager
										.createNamedQuery("PedValidation.findByTransactionKey");
								diagQuery.setParameter("transactionKey",reimbObj.getKey());
								List<PedValidation> diagnosisList = (List<PedValidation>) diagQuery
				 					.getResultList();
								diagnosis = "";
								if (diagnosisList != null
										&& !diagnosisList.isEmpty()) {
									for (PedValidation pedDiagnosis : diagnosisList) {

										if (pedDiagnosis != null) {
											if (pedDiagnosis.getDiagnosisId() != null) {
												Diagnosis diag = entityManager
														.find(Diagnosis.class,
																pedDiagnosis
																		.getDiagnosisId());
												if (diag != null) {
													diagnosis = !("")
															.equalsIgnoreCase(diagnosis) ? diagnosis
															+ " , "
															+ diag.getValue()
															: diag.getValue();
												}
											}
										}
										
										if (pedDiagnosis.getIcdCodeId() != null) {
											IcdCode icdCodeObj = entityManager
													.find(IcdCode.class, pedDiagnosis.getIcdCodeId());
											if (icdCodeObj != null) {
												icdCode = ("")
														.equalsIgnoreCase(icdCode) ? icdCodeObj
														.getValue() : icdCode
														+ " , "
														+ icdCodeObj.getValue();
											}

										}
									}
								}
								
								if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIM_QUERY) || StringUtils.equalsIgnoreCase(stageName,SHAConstants.REJECTED_CLAIMS) || StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLOSED_CLAIMS)){
								
									Query closeClaimQuery = entityManager
											.createNamedQuery("CloseClaim.getByReimbursmentKey");
									closeClaimQuery.setParameter("reimbursmentKey", reimbObj.getKey());

									List<CloseClaim> closedClaimList = closeClaimQuery.getResultList();
									
									if(closedClaimList != null && !closedClaimList.isEmpty()){
										CloseClaim closedClaim = closedClaimList.get(0);
										entityManager.refresh(closedClaim);
										String closeDate = closedClaim.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(closedClaim.getCreatedDate()) : "";
										
										if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLAIM_QUERY) || StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLOSED_CLAIMS)){
											clmsStatusReportDto.setCloseDate(closeDate);	
										}
										
										if(StringUtils.equalsIgnoreCase(stageName,SHAConstants.REJECTED_CLAIMS) || StringUtils.equalsIgnoreCase(stageName,SHAConstants.CLOSED_CLAIMS)){
											clmsStatusReportDto.setCloseStage(closedClaim.getStage().getStageName() != null ? closedClaim.getStage().getStageName() : "");	
											clmsStatusReportDto.setUserId(closedClaim.getCreatedBy() != null ? closedClaim.getCreatedBy() : "");
											clmsStatusReportDto.setUserName(clmsStatusReportDto.getUserId());
										}
										
										clmsStatusReportDto.setClosedRemarks(closedClaim.getClosingRemarks() != null ? closedClaim.getClosingRemarks() :"");
										
										
									}									
								}
								
								Query docReceivedQuery = entityManager
										.createNamedQuery("DocAcknowledgement.findAckByClaim");
//										.createNamedQuery("Reimbursement.findFirstRODByClaimKey");
								docReceivedQuery.setParameter("claimkey", claim.getKey());
//								List<Reimbursement> reimbBillsList = docReceivedQuery.getResultList();
								
								List<DocAcknowledgement> reimbBillsList = docReceivedQuery.getResultList();
								
								if( reimbBillsList != null && !reimbBillsList.isEmpty()){									
									clmsStatusReportDto.setNoofTimeBillRec(String.valueOf(reimbBillsList.size()));
									
//									for(Reimbursement reimBillbObj : reimbBillsList){
									String billReceivedDate = "";
									for(DocAcknowledgement reimBillbObj : reimbBillsList){
									entityManager.refresh(reimBillbObj);
									billReceivedDate = reimBillbObj.getCreatedDate() != null ? ( !billReceivedDate.isEmpty() ? (billReceivedDate + ", " + new SimpleDateFormat("dd/MM/yy").format(reimBillbObj.getCreatedDate()) ) : new SimpleDateFormat("dd/MM/yy").format(reimBillbObj.getCreatedDate())) : ""; 
									}
									clmsStatusReportDto.setBillReceivedDate(billReceivedDate);
								}

//								DocAcknowledgement docObj = reimbBillsList.get(0);
//								Double billAmount = 0d;
//								for(DocAcknowledgement docKObj : docAckList){
//								billAmount = docKObj.getHospitalizationClaimedAmount() != null ? docKObj.getHospitalizationClaimedAmount(): billAmount;
//								billAmount = docKObj.getPreHospitalizationClaimedAmount() != null ? billAmount + docKObj.getPreHospitalizationClaimedAmount() : billAmount;
//								billAmount = docKObj.getPostHospitalizationClaimedAmount() != null ? billAmount + docKObj.getPostHospitalizationClaimedAmount() : billAmount;
//								}
//								clmsStatusReportDto.setBillAmount(billAmount != null && billAmount != 0d ? String.valueOf(billAmount.intValue()) :"");
								//clmsStatusReportDto.setClaimedAmount(billAmount.toString());
								
								
								
								clmsStatusReportDto.setIcdCode(icdCode);
								clmsStatusReportDto.setDiagnosis(diagnosis);
						
						String clmClassification = "";
						
						if(reimbObj != null){
							
							Query diagQ = entityManager.createNamedQuery("PedValidation.findByTransactionKey");
							diagQ.setParameter("transactionKey", reimbObj.getKey());
							
							List<PedValidation> diagList = diagQ.getResultList();
							
							if(diagList != null && !diagList.isEmpty()){
								
								for(PedValidation diagObj : diagList){
									
									Query pedValidQ = entityManager.createNamedQuery("DiagnosisPED.findByPEDValidationKey");
									pedValidQ.setParameter("pedValidationKey", diagObj.getKey());
									List<DiagnosisPED> pedValidationList = pedValidQ.getResultList();
									
									if(null != pedValidationList && !pedValidationList.isEmpty()){
										for(DiagnosisPED pedValidObj : pedValidationList){
											clmClassification += (pedValidObj.getExclusionDetails().getExclusion() != null ? pedValidObj.getExclusionDetails().getExclusion() : clmClassification) + " - " +( pedValidObj.getDiagonsisImpact() != null && pedValidObj.getDiagonsisImpact().getValue() != null ? pedValidObj.getDiagonsisImpact().getValue() : clmClassification );
										}
									}
								}
							}
						}
						
						clmsStatusReportDto.setClaimCoverage(clmClassification);
						
						String billingUser ="";
						String medicalApprover="";
						String fAApprover="";
						StageInformation result=null;
						Query stageUserquery = entityManager.createNamedQuery("StageInformation.findByReimbStageNStatus");
						
						stageUserquery.setParameter("rodKey",reimbObj.getKey());
						stageUserquery.setParameter("stageKey", ReferenceTable.BILLING_STAGE);
						stageUserquery.setParameter("statusKey", ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER);
						
						if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty() ){
							
							result = (StageInformation)stageUserquery.getResultList().get(0);
							
							entityManager.refresh(result);
							if(result != null){
								billingUser = result.getCreatedBy() != null ? result.getCreatedBy() : "";
							
								if(stageName.equalsIgnoreCase(SHAConstants.BILLING_COMPLETED))
								{
									clmsStatusReportDto.setUserId(billingUser);
									
									deductAmt = claimedAmt > billingApproveAmt ? claimedAmt - billingApproveAmt : 0d;

									clmsStatusReportDto.setDeductedAmount(String.valueOf(deductAmt));

								}
							
							}
						}
						stageUserquery.setParameter("stageKey", ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
						stageUserquery.setParameter("statusKey", ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS);    
						
						if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty()){
							
							result = (StageInformation)stageUserquery.getResultList().get(0);
							
							entityManager.refresh(result);
							if(result != null){
							
							medicalApprover = result.getCreatedBy() != null ? result.getCreatedBy() : "";
							clmsStatusReportDto.setMaApprovedDate(result.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yy").format(result.getCreatedDate()): "");
							clmsStatusReportDto.setUserId(medicalApprover);	
							
							}
						}
													
						stageUserquery.setParameter("stageKey", ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY); 
						stageUserquery.setParameter("statusKey", ReferenceTable.FINANCIAL_APPROVE_STATUS);
						if(stageUserquery.getResultList() != null && !stageUserquery.getResultList().isEmpty()){
							result = (StageInformation)stageUserquery.getResultList().get(0);
							
							entityManager.refresh(result);
							if(result != null){
								fAApprover = result.getCreatedBy() != null ? result.getCreatedBy() : "";
							}
						}
						
						clmsStatusReportDto.setBillingPerson(billingUser != null ? billingUser : "");
						clmsStatusReportDto.setMedicalApprovalPerson(medicalApprover != null ? medicalApprover : "");
						clmsStatusReportDto.setFinancialApprovalPerson(fAApprover != null ? fAApprover : "");
						clmsStatusReportDto.setUserName(clmsStatusReportDto.getUserId());						
						
										
						resultDtoList.add(clmsStatusReportDto);
						clmsStatusReportDto = null;
				} 
				}
				}*/
					/*if(!resultDtoList.isEmpty()){
					if(stageName.equalsIgnoreCase(SHAConstants.CLAIMS_PAID_STAUS)){
						
						List<ClaimsStatusReportDto> paidList = new ArrayList<ClaimsStatusReportDto>();
						SimpleDateFormat dtFrmt = new SimpleDateFormat("dd/MM/yy");
//						if(null != fromDate )
//						{
//							fromDate = dtFrmt.parse(fromDate.toString());
//						}
//						if(null != endDate)
//						{
//							endDate =  dtFrmt.parse(endDate.toString());
//						}
						for(ClaimsStatusReportDto resultDto : resultDtoList){
							
							resultDto.setUserId(resultDto.getFinancialApprovalPerson());
							resultDto.setUserName(resultDto.getUserId());
							if(fromDate.compareTo(endDate) == 0){
								if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) == 0){
									paidList.add(resultDto);
								}
							}
							else{
								if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) == 0){
									paidList.add(resultDto);
								}
								else if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(fromDate) > 0){
									paidList.add(resultDto);
								}
								else if(dtFrmt.parse(resultDto.getPaidDate()).compareTo(endDate) <= 0){
									paidList.add(resultDto);
								}
								
							}
						}
						
						if(paidList != null && !paidList.isEmpty()){
							resultDtoList.clear();
							for(ClaimsStatusReportDto paidDto :paidList){
								paidDto.setSno(paidList.indexOf(paidDto)+1);
								resultDtoList.add(paidDto);
							}
						}
						
					}		
					
				}*/
				java.sql.Date fDate = null;
				java.sql.Date eDate = null;
				java.sql.Date fyFDate = null;
				java.sql.Date fyEDate = null;
				if(fromDate != null){
					fDate = new java.sql.Date(fromDate.getTime());
					fyFDate = fDate; 
				}
				if(endDate != null){
					eDate = new java.sql.Date(endDate.getTime());
					fyEDate = eDate; 
				}
				if(fyFromDate != null){
					fyFDate = new java.sql.Date(fyFromDate.getTime());
				}
				if(fyEndDate != null){
					fyEDate = new java.sql.Date(fyEndDate.getTime());
				}
				
				ClaimsReportService.popinReportLog(entityManager, userId, "Claims Status Report New",new Date(),new Date(),SHAConstants.RPT_BEGIN);
					resultDtoList = dbService.getClaimsReportList(fDate, eDate,stageName, cpuCode, userId,period, fyFDate, fyEDate);
					ClaimsReportService.popinReportLog(entityManager, userId, "Claims Status Report New",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
					
				utx.commit();

			} catch (Exception e) {
				ClaimsReportService.popinReportLog(entityManager, userId, "Claims Status Report New",new Date(),new Date(),SHAConstants.RPT_ERROR);
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

		}
		
		return resultDtoList;
	}
	
	private IntimationService getIntimationService()
	{
		IntimationService intimationService = new IntimationService();
		
		return intimationService;
	}
	
	
public List<CallcenterDashBoardReportDto> getCallcenterDashBoardSearchResult(SearchIntimationFormDto searchDto){
				
		List<CallcenterDashBoardReportDto> resultList = new ArrayList<CallcenterDashBoardReportDto>();
		
		String userId = searchDto.getUsername();
		
		try{
			
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();
			searchDto.setPageable(null);
//			Page<NewIntimationDto> searchResultPageDto = getIntimationService().searchCombinationforViewAndEditReport(searchDto, entityManager);
			
			ClaimsReportService.popinReportLog(entityManager, userId, "CallcenterDashBoard",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<Intimation> intimationsList =  getIntimationService().searchIntimationByFilters(searchDto.getFilters(),null,entityManager);
			
			if (intimationsList != null && !intimationsList.isEmpty()) {
				CallcenterDashBoardReportDto callcenterDashBoardDto;
				List<Claim> claimsList;
				Claim clmObje;
				Hospitals hospital;
				Query query = entityManager.createNamedQuery("Claim.findByIntimationKey");
				for (Intimation a_intimation : intimationsList) {
					callcenterDashBoardDto = new CallcenterDashBoardReportDto(a_intimation);
					query.setParameter("intimationKey", a_intimation.getKey());
					
					claimsList = (List<Claim>)query.getResultList();
					
					if(claimsList != null && !claimsList.isEmpty()){
						clmObje = claimsList.get(0);
						entityManager.refresh(clmObje);
					
					if(clmObje != null && (ReferenceTable.OP_STAGE).equals(clmObje.getStage().getKey())){
						continue;
					}
					else{
						callcenterDashBoardDto.setClaimNo(clmObje.getClaimId() != null ? clmObje.getClaimId() : "");
						hospital = null;
						if (!ValidatorUtils.isNull(a_intimation.getHospital())) {
							if (a_intimation.getHospitalType().getValue()
								.toLowerCase().contains("network")) {
//							hospital = entityManager.find(Hospitals.class,
//									a_intimation.getHospital());
							if(a_intimation.getHospital() != null){
								hospital = getHospitalDetailsByKey(a_intimation.getHospital());
							}
							if (hospital != null) {
								callcenterDashBoardDto.setHospitalName(hospital.getName() != null ? hospital.getName() : "");
							}
							
						}
					}
					}
				}
					resultList.add(callcenterDashBoardDto);
					callcenterDashBoardDto = null;
			}
				
			}
			ClaimsReportService.popinReportLog(entityManager, userId, "CallcenterDashBoard",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			
//		if(searchResultPageDto != null){
//			List<NewIntimationDto> intimationDtoList = searchResultPageDto.getPageItems(); 
//			
//			
//			if(intimationDtoList != null && !intimationDtoList.isEmpty()){
//				
//				for(NewIntimationDto intiamtionDto : intimationDtoList){
//					
//					CallcenterDashBoardReportDto callcenterDashBoardDto = new CallcenterDashBoardReportDto(intiamtionDto);
//					
//					Query query = entityManager.createNamedQuery("Claim.findByIntimationKey");
//					
//					query.setParameter("intimationKey", intiamtionDto.getKey());
//					
//					List<Claim> claimsList = (List<Claim>)query.getResultList();
//					
//					if(claimsList != null && !claimsList.isEmpty()){
//						Claim clmObje = claimsList.get(0);
//						entityManager.refresh(clmObje);
//						callcenterDashBoardDto.setClaimNo(clmObje.getClaimId() != null ? clmObje.getClaimId() : "");
//					}
//					
//					resultList.add(callcenterDashBoardDto);
//				}				
//			}			
//		}
		utx.commit();
		}
		catch(Exception e){
			
			ClaimsReportService.popinReportLog(entityManager, userId, "CallcenterDashBoard",new Date(),new Date(),SHAConstants.RPT_ERROR);
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return resultList;		
	}
	
//	public Claim getClaimforIntimation(Long intimationKey) {
//		Claim a_claim = null;
//		if (intimationKey != null) {
//
//			Query findByIntimationKey = entityManager.createNamedQuery(
//					"Claim.findByIntimationKey").setParameter("intimationKey",
//					intimationKey);
//
//			try {
//				List<Claim> claimList = (List<Claim>) findByIntimationKey
//						.getResultList();
//				if (claimList != null && !claimList.isEmpty()) {
//					a_claim = claimList.get(0);
//					entityManager.refresh(a_claim);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			// intimationKey null
//		}
//
//		return a_claim;
//	}

	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	public static void popinReportLog(EntityManager entityManager, String userId, String reportName,Date exeDate,Date txnDate,String statusFlag){
		
		try{
		
			ReportLog reportLogObj = new ReportLog();
			
			reportLogObj.setReportName(reportName);
			reportLogObj.setCurrentDate(exeDate);
			reportLogObj.setReportTime(txnDate);
			reportLogObj.setStatusFlag(statusFlag);
			reportLogObj.setUserId(userId);
			reportLogObj.setAciveStatusFlag("Y");
			
			entityManager.persist(reportLogObj);
			// Below Line added for memory issue
			entityManager.flush(); 
			entityManager.clear();
		}
		catch(Exception e){
			e.printStackTrace();	
		}
		
		
	}
	
	public List<NewIntimationDto> getPreauthFormDocReport(Map<String,Object> filters,DBCalculationService dbCalService){
		Date fromDate = null;
		Date toDate = null;
		String userId = "";
		
		List<NewIntimationDto> finalList = new ArrayList<NewIntimationDto>();
		
		if(filters != null && !filters.isEmpty() && filters.containsKey(BPMClientContext.USERID) && filters.get(BPMClientContext.USERID) != null){
			
			userId = String.valueOf(filters.get(BPMClientContext.USERID));
		}
		
		try
		{	
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();
			
			if(filters != null && !filters.isEmpty()){
				
				if(filters.containsKey("fromDate") && filters.get("fromDate") != null){
					
					fromDate = (Date)filters.get("fromDate");
				}
				
				if(filters.containsKey("endDate") && filters.get("endDate") != null){
					
					toDate = (Date)filters.get("endDate");
				}
			
				if(null != fromDate && null != toDate)
				{
					java.util.Date utilFromDate = fromDate;
					java.util.Date utilToDate = toDate;
					SelectValue cpuSelect = filters.containsKey("cpucode") ? (SelectValue)filters.get("cpucode") : null;
					String[] cpusplit = cpuSelect != null ? cpuSelect.getValue().split(" - ") : null;
					Long cpuCode = cpusplit != null && cpusplit.length > 0 ? Long.valueOf(cpusplit[0]) : 0l; 
					
					
				    java.sql.Date sqlFrmDate = new java.sql.Date(utilFromDate.getTime()); 
				    java.sql.Date sqlToDate = new java.sql.Date(utilToDate.getTime());				    
				    
				    ClaimsReportService.popinReportLog(entityManager, userId, "PreauthFormDocReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				    finalList = dbCalService.getPreauthFormDocList(sqlFrmDate,sqlToDate,cpuCode,userId); 
				    ClaimsReportService.popinReportLog(entityManager, userId, "PreauthFormDocReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				}
		   }
			utx.commit();
		}catch(Exception e){
				e.printStackTrace();
				ClaimsReportService.popinReportLog(entityManager, userId, "PreauthFormDocReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
		return finalList;
	}

	@SuppressWarnings("unchecked")
	public PolicywiseClaimReportDto getICRDetailsByPolicyNumber(
			String policyNumber) {
		
		PolicywiseClaimReportDto policyClaimDto = null;
		if (policyNumber != null) {

			try {

				VaadinSession.getCurrent().getSession()
						.setMaxInactiveInterval(3600);
				utx.setTransactionTimeout(3600);
				utx.begin();

				Query icrQuery = entityManager
						.createNamedQuery("IcrDetails.findByPolicyNumber");
				icrQuery = icrQuery.setParameter("policyNumber", policyNumber);
				List<IcrDetails> icrList = icrQuery.getResultList();

				if (icrList != null && !icrList.isEmpty()) {

					policyClaimDto = new PolicywiseClaimReportDto();

					Query polQuery = entityManager
							.createNamedQuery("Policy.findByPolicyNumber");
					polQuery = polQuery.setParameter("policyNumber",
							policyNumber);

					List<Policy> policyList = polQuery.getResultList();

					if (policyList != null
							&& !policyList.isEmpty()) {

//						String totalNoOfClaims = String
//								.valueOf(claimByPolicyList.size());
//
//						System.out
//								.println("Total Records : " + totalNoOfClaims);
//						policyClaimDto.setTotalNoOfClaims(totalNoOfClaims);
						
						entityManager.refresh(policyList.get(0));

						
						policyClaimDto.setMainMemberName(policyList.get(0).getProposerFirstName() != null ?
								policyList.get(0).getProposerFirstName() 
									: "");
					}	
					policyClaimDto.setPolicyNumber(policyList.get(0)
							.getPolicyNumber());
					
					policyClaimDto.setPolicyPeriodFrom(policyList.get(0)
							.getPolicyFromDate() != null ?
							new SimpleDateFormat("dd/MM/yyyy").format(policyList.get(0)
									.getPolicyFromDate())
									: "");
					policyClaimDto.setPolicyPeriodTo(policyList.get(0)
							.getPolicyToDate() != null ?
							new SimpleDateFormat("dd/MM/yyyy").format(policyList.get(0)
									.getPolicyToDate())
									: "");							
							
						policyClaimDto.setPremium(icrList.get(0)
								.getEndorPremium());
						policyClaimDto
								.setClaimedAmount((icrList.get(0)
										.getOutstandingAmount() != null ? icrList
										.get(0).getOutstandingAmount() : 0)
										+ (icrList.get(0).getPaidAmount() != null ? icrList
												.get(0).getPaidAmount() : 0));
						policyClaimDto.setIcrRatio(icrList.get(0)
								.getClaimRatio());
						
						policyClaimDto.setColor(icrList.get(0)
								.getColor());
					
				}
				utx.commit();
			} catch (Exception e) {

				log.error(e.toString());
				e.printStackTrace();
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}	
		return policyClaimDto;
	}	
	
	@SuppressWarnings("unchecked")
	public List<PolicywiseClaimReportDto> getClaimsPolicyWise(
			String policyNumber, String user) {

		List<PolicywiseClaimReportDto> resultList = new ArrayList<PolicywiseClaimReportDto>();

		if (policyNumber != null) {

			try {
				
				VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
				utx.setTransactionTimeout(3600);
				utx.begin();
				
				ClaimsReportService.popinReportLog(entityManager, user, "SearchClaimPolicyWiseReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);	
				//Long claimKey = null;
				Query claimQuery = entityManager
						.createNamedQuery("Claim.findByPolicyNumber");
				claimQuery = claimQuery.setParameter("policyNumber", policyNumber);

				List<Claim> claimByPolicyList = claimQuery.getResultList();
				
//				List<Preauth> tempPreauthList = claimQuery.getResultList();

				if (claimByPolicyList != null && !claimByPolicyList.isEmpty()) {

					String totalNoOfClaims = String.valueOf(claimByPolicyList.size());
					
					System.out.println("Total Records : " + totalNoOfClaims);
					PolicywiseClaimReportDto policyClaimDto = null;
					ClaimDto claimDto = null;
					Insured insuredByKey = null;
					Insured MainMemberInsured = null;
					for (Claim claimObj : claimByPolicyList) {
						entityManager.refresh(claimObj);

						claimDto = getClaimDto(claimObj);
						
						policyClaimDto = new PolicywiseClaimReportDto(claimDto);
						policyClaimDto.setBasepremium(claimObj.getIntimation().getPolicy().getTotalPremium());
						
						if(ReferenceTable.getGMCProductList().containsKey(claimObj.getIntimation().getPolicy().getProduct().getKey()) ||
								ReferenceTable.STAR_GOLD_GROUP_FOR_BANK_CUSTOMERS_KEY.equals(claimObj.getIntimation().getPolicy().getProduct().getKey())){
						     insuredByKey = getInsuredByKey(claimObj.getIntimation().getInsured().getKey());
						     
						     if(insuredByKey.getDependentRiskId() == null){
						    	  MainMemberInsured = insuredByKey;
						     }else{
						    	 MainMemberInsured = getInsuredByPolicyAndInsuredId(claimObj.getIntimation().getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(),claimObj.getIntimation().getProcessClaimType());
						     }
						      
						     if(MainMemberInsured != null){
						    	 policyClaimDto.setEmployeeID(MainMemberInsured.getInsuredEmployeeId());
						     }
						}      
						
						
						resultList.add(policyClaimDto);
						claimDto = null;
						policyClaimDto = null;
						MainMemberInsured = null;
					
					}
					for(PolicywiseClaimReportDto polClaimObj : resultList){
					
						polClaimObj = getCashlessClaimsDetails(polClaimObj,claimByPolicyList);
						polClaimObj = UpDatePolicyClaimObj(polClaimObj,claimByPolicyList);						
						polClaimObj.setSno(resultList.indexOf(polClaimObj)+1);
						polClaimObj.setTotalNoOfClaims(String.valueOf(resultList.size()));
						
						if(polClaimObj.getPaidAmount() != null && Double.valueOf(polClaimObj.getPaidAmount()).intValue() != 0){ 
							List<ViewTmpClaimPayment> paidDtoList = getRimbursementForPayment(polClaimObj.getClaimNo());
							
							if(paidDtoList != null && !paidDtoList.isEmpty()){
								String paidDate = "";
								String payMode = "";
								String chqNo = "";
								String chqDt = "";
								for (ViewTmpClaimPayment viewTmpClaimPayment : paidDtoList) {
									payMode = payMode.isEmpty() ? (viewTmpClaimPayment.getPaymentType() != null ? viewTmpClaimPayment.getPaymentType() : "") : (viewTmpClaimPayment.getPaymentType() != null ? (payMode + ", " +viewTmpClaimPayment.getPaymentType()) : payMode);
									paidDate = paidDate.isEmpty() ? (viewTmpClaimPayment.getCreatedDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(viewTmpClaimPayment.getCreatedDate()) : "") : (viewTmpClaimPayment.getCreatedDate() != null ? (paidDate + ", " +new SimpleDateFormat("dd/MM/yyyy").format(viewTmpClaimPayment.getCreatedDate())) : paidDate);
									chqNo = chqNo.isEmpty() ? (viewTmpClaimPayment.getChequeDDNumber() != null ? viewTmpClaimPayment.getChequeDDNumber() : "") : (viewTmpClaimPayment.getChequeDDNumber() != null ? (chqNo + ", " +viewTmpClaimPayment.getChequeDDNumber()) : chqNo);
									chqDt = chqDt.isEmpty() ? (viewTmpClaimPayment.getChequeDDDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(viewTmpClaimPayment.getChequeDDDate()) : "") : (viewTmpClaimPayment.getChequeDDDate() != null ? (chqDt + ", " +new SimpleDateFormat("dd/MM/yyyy").format(viewTmpClaimPayment.getChequeDDDate())) : chqDt);
								}
								polClaimObj.setPaidDate(paidDate);
								polClaimObj.setModeOfPayment(payMode);
								polClaimObj.setChequeDate(chqDt);
								polClaimObj.setChequeNo(chqNo);
						   }
						}
					}
					
					ClaimsReportService.popinReportLog(entityManager, user, "SearchClaimPolicyWiseReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				}
				utx.commit();	
					
					
			} catch (Exception e) {
				
				log.error(e.toString());
				e.printStackTrace();
				ClaimsReportService.popinReportLog(entityManager, user, "SearchClaimPolicyWiseReport", new Date(),new Date(),SHAConstants.RPT_ERROR);
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return resultList;
	}
	
	
	public List<ViewTmpClaimPayment> getRimbursementForPayment(String claimNumber) {

		Query query = entityManager
				.createNamedQuery("ViewTmpClaimPayment.findByClaimNumber");
		query.setParameter("claimNumber", claimNumber);

		List<ViewTmpClaimPayment> reimbursementList = (List<ViewTmpClaimPayment>) query
				.getResultList();

		if (reimbursementList != null && !reimbursementList.isEmpty()) {
			return reimbursementList;
		}

		return null;

	}
	private ClaimDto getClaimDto(Claim claimObj){
		
		//ClaimService clmService = new ClaimService();
		IntimationService intimationService = new IntimationService();
		
		ClaimDto claimDto = ClaimMapper.getInstance().getClaimDto(claimObj);
		NewIntimationDto intimationDto = intimationService.getIntimationDto(claimObj.getIntimation(), entityManager);
		
		claimDto.setNewIntimationDto(intimationDto);
		
		return claimDto;		
		
	}
	
	@SuppressWarnings({ "unchecked" })
	private PolicywiseClaimReportDto getCashlessClaimsDetails(PolicywiseClaimReportDto policyClmDto, List<Claim> clmList){

		if (clmList != null && !clmList.isEmpty()) {
			List<Preauth> preauthResultList = null;
			Preauth preauthObj = null;
			PreauthMapper preauthMapper = PreauthMapper.getInstance();
			ClaimDto clmDto = null;
			PreauthDTO preauthObjDto = null;
			NewIntimationDto intimationDto = null;
			DBCalculationService dbCalService = new DBCalculationService();
			Map<String, Object> outstandingPaidDetails = null;
			BalanceSumInsuredDTO paymentAmtDto = null;
			List<PreauthQuery> cashlessQueryList = null;
			for (Claim clmObj : clmList) {

				Query preauthQuery = entityManager
						.createNamedQuery("Preauth.findPreAuthIdInDescendingOrder");
				preauthQuery = preauthQuery.setParameter("intimationKey",
						clmObj.getIntimation().getKey());

				preauthResultList = (List<Preauth>) preauthQuery
						.getResultList();

				if (preauthResultList != null && !preauthResultList.isEmpty()) {
					preauthObj = preauthResultList.get(0);
					if(preauthObj != null) {

						if (preauthObj
								.getIntimation()
								.getIntimationId()
								.equalsIgnoreCase(
										policyClmDto.getIntimationNo())) {

							
//							PreauthMapper.getAllMapValues();
//							NewIntimationMapper intimationMapper = new NewIntimationMapper();
//							ClaimMapper clmMapper = new ClaimMapper();

							clmDto = getClaimDto(preauthObj.getClaim());
							preauthObjDto = preauthMapper
									.getPreauthDTO(preauthObj);
							intimationDto = clmDto
									.getNewIntimationDto();
							preauthObjDto.setNewIntimationDTO(intimationDto);
							preauthObjDto.setClaimDTO(clmDto);
							
							Long claimedAmt = (new PreauthService()).getClaimAmountByPreauth(preauthObj.getKey(),entityManager);
							
							policyClmDto.setClaimedAmount(claimedAmt != null ? claimedAmt : 0d);
							
							policyClmDto = updateDiagnosisDetails(policyClmDto, preauthObjDto);
							
							
							if (clmDto
									.getNewIntimationDto().getPolicy()
									.getProduct().getKey().equals(401l)) {

								Double sumInsured = dbCalService
										.getInsuredSumInsuredForGMC(
												clmDto.getNewIntimationDto()
														.getPolicy()
														.getKey(),
												clmDto.getNewIntimationDto()
														.getInsuredPatient()
														.getKey(),
												clmDto.getNewIntimationDto()
														.getPolicy()
														.getSectionCode());

								outstandingPaidDetails = dbCalService
										.getGmcSI(clmDto.getNewIntimationDto()
														.getInsuredPatient()
														.getKey(),
												  clmDto.getNewIntimationDto()
														.getIntimationId(),
												sumInsured,
												  clmDto.getNewIntimationDto()
														.getPolicy()
														.getProduct()
														.getKey());

								if (outstandingPaidDetails != null) {
									if (outstandingPaidDetails
											.containsKey("outstandingAmout")) {
										policyClmDto
												.setOutstandingAmount(String
														.valueOf(outstandingPaidDetails
																.get("outstandingAmout")));
									}
									if (outstandingPaidDetails
											.containsKey("paidAmout")) {

										policyClmDto
												.setPaidAmount(String
														.valueOf(outstandingPaidDetails
																.get("paidAmout")));
									}
								}
								
								outstandingPaidDetails = null;
							} else {
							
							
							Double sumInsured = dbCalService
									.getInsuredSumInsured(preauthObjDto
											.getNewIntimationDTO()
											.getInsuredPatient().getInsuredId()
											.toString(), preauthObjDto
											.getNewIntimationDTO().getPolicy()
											.getKey(),preauthObjDto
											.getNewIntimationDTO()
											.getInsuredPatient().getLopFlag());

							paymentAmtDto = dbCalService
									.getClaimsOutstandingAmt(preauthObjDto
											.getNewIntimationDTO()
											.getInsuredPatient().getKey(),
											preauthObjDto.getNewIntimationDTO()
													.getIntimationId(),
											sumInsured);

							policyClmDto.setOutstandingAmount(String.valueOf(paymentAmtDto
									.getOutstandingAmout()));
							policyClmDto.setPaidAmount(String.valueOf(paymentAmtDto
									.getPreviousClaimPaid()));
							paymentAmtDto = null;
							}
							Query cashlessQuery = entityManager
									.createNamedQuery("PreauthQuery.findBypreauth");
							cashlessQuery = cashlessQuery.setParameter("preAuthPrimaryKey",
									preauthObj.getKey());

							cashlessQueryList = cashlessQuery
									.getResultList();

							if (cashlessQueryList != null && !cashlessQueryList.isEmpty()) {
								
								Date qDate = cashlessQueryList.get(0).getCreatedDate();
								String queryRiseDate = qDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(qDate) : "";
								
								policyClmDto.setQueryRaisedDate(queryRiseDate);
																
								Date queryReceivedDate = cashlessQueryList.get(0)
										.getModifiedDate();
								String billDateString = queryReceivedDate != null ? new SimpleDateFormat(
										"dd/MM/yyyy").format(queryReceivedDate)
										: "";
								policyClmDto.setrODDate(billDateString);
							}

							policyClmDto.setBasepremium(preauthObjDto
									.getClaimDTO().getNewIntimationDto()
									.getPolicy().getTotalPremium());
							
							cashlessQueryList = null;
							clmDto = null;
							preauthObjDto = null;
							intimationDto = null;
							
						}
					}
				}
				preauthResultList = null;
			}
		}
		return policyClmDto;
		
	}
	
	@SuppressWarnings("unchecked")
	private PolicywiseClaimReportDto UpDatePolicyClaimObj(
			PolicywiseClaimReportDto policyclmDto, List<Claim> clmList) {
		try {
			if (clmList != null && !clmList.isEmpty()) {
				ReceiptOfDocumentsDTO rodDTO = null;
				List<Reimbursement> reimbursementList = null;
				Reimbursement reimbursement = null;
				ReimbursementMapper reimbMapper = new ReimbursementMapper();
				ReimbursementDto reimbDto = null;
				ClaimDto clmDto = null;
				ZonalMedicalReviewMapper preauthMapper = ZonalMedicalReviewMapper
						.getInstance();
				PreauthDTO preauthDto = null;
				List<DocAcknowledgement> docAckList = null;	
				CreateRODService createRODServicenew = new CreateRODService();
				DBCalculationService dbCalService = new DBCalculationService();
				Map<String, Object> outstandingPaidDetails = null;
				BalanceSumInsuredDTO paymentAmtDto = null;
				PreauthService preauthService = new PreauthService();
				List<ReimbursementQuery> reimbQueryList = null;
				List<ReimbursementRejection> rejectionList = null;
				MastersValue managementType = null; 
						
				for (Claim clm : clmList) {

					Long claimKey = clm.getKey();

					Query claimQuery = entityManager
							.createNamedQuery("Reimbursement.findByClaimKey");
					claimQuery = claimQuery.setParameter("claimKey", claimKey);

					reimbursementList = claimQuery
							.getResultList();

					if (reimbursementList != null
							&& !reimbursementList.isEmpty()) {
						 reimbursement = reimbursementList
								.get(reimbursementList.size() - 1);
						if (reimbursement != null) {

							if (reimbursement
									.getClaim()
									.getIntimation()
									.getIntimationId()
									.equalsIgnoreCase(
											policyclmDto.getIntimationNo())) {

								policyclmDto.setClaimedAmount(reimbursement
										.getClaim().getClaimedAmount());
								reimbDto = reimbMapper
										.getReimbursementDto(reimbursement);
								clmDto = getClaimDto(reimbursement
										.getClaim());
								reimbDto.setClaimDto(clmDto);
								policyclmDto
										.setDataOfDischarge(reimbDto
												.getDateOfDischarge() != null ? new SimpleDateFormat(
												"dd/MM/yyyy").format(reimbDto
												.getDateOfDischarge()) : "");

								
								// ZonalMedicalReviewMapper.getAllMapValues();

								preauthDto = preauthMapper
										.getReimbursementDTO(reimbursement);

								policyclmDto = updateDiagnosisDetails(
										policyclmDto, preauthDto);

								Query rodQuery = entityManager
										.createNamedQuery("DocAcknowledgement.findByClaimKey");
								rodQuery = rodQuery.setParameter("claimkey",
										reimbDto.getClaimDto().getKey());

								docAckList = rodQuery
										.getResultList();

								String clmClassification = "";

								if (docAckList != null && !docAckList.isEmpty()) {
									Date billreceivedDate = docAckList.get(0)
											.getDocumentReceivedDate();
									String billDateString = new SimpleDateFormat(
											"dd/MM/yyyy")
											.format(billreceivedDate);
									policyclmDto.setrODDate(billDateString);

								}
								docAckList = null;
								if (reimbursement != null
										&& reimbursement.getClaim() != null
										&& reimbursement.getClaim().getKey() != null) {

									rodDTO = new ReceiptOfDocumentsDTO();

									rodDTO = createRODServicenew
											.getBillClassificationFlagDetails(
													reimbursement.getClaim()
															.getKey(), rodDTO,
													entityManager);

									if (reimbursement.getDocAcknowLedgement()
											.getHospitalisationFlag() != null) {

										clmClassification = ("Y")
												.equalsIgnoreCase(reimbursement
														.getDocAcknowLedgement()
														.getHospitalisationFlag()) ? (("")
												.equals(clmClassification) ? "Hospitalisation"
												: clmClassification + " , "
														+ "Hospitalisation")
												: clmClassification;

									}

									if (rodDTO.getPreHospitalizationFlag() != null) {
										clmClassification = ("Y")
												.equalsIgnoreCase(rodDTO
														.getPreHospitalizationFlag()) ? (("")
												.equals(clmClassification) ? "Pre-Hospitalisation"
												: clmClassification + " , "
														+ "Pre-Hospitalisation")
												: clmClassification;
									}

									if (rodDTO.getPostHospitalizationFlag() != null) {

										clmClassification = ("Y")
												.equalsIgnoreCase(rodDTO
														.getPostHospitalizationFlag()) ? (("")
												.equals(clmClassification) ? "Post-Hospitalisation"
												: clmClassification
														+ " , "
														+ "Post-Hospitalisation")
												: clmClassification;
									}

									policyclmDto
											.setClaimClassification(clmClassification);

								}
								
								rodDTO = null;
								
								if (reimbDto.getClaimDto()
										.getNewIntimationDto()
										.getInsuredPatient().getInsuredId() != null) {
									
									

									if (reimbDto.getClaimDto()
											.getNewIntimationDto().getPolicy()
											.getProduct().getKey().equals(401l)) {

										Double sumInsured = dbCalService
												.getInsuredSumInsuredForGMC(
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getPolicy()
																.getKey(),
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getInsuredPatient()
																.getKey(),
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getPolicy()
																.getSectionCode());

										outstandingPaidDetails = dbCalService
												.getGmcPaidNOutStanding(reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getInsuredPatient()
																.getKey(),
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getIntimationId(),
														sumInsured,
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getPolicy()
																.getProduct()
																.getKey());

										if (outstandingPaidDetails != null) {
											if (outstandingPaidDetails
													.containsKey("outstandingAmount")) {
												policyclmDto
														.setOutstandingAmount(String
																.valueOf(outstandingPaidDetails
																		.get("outstandingAmount")));
											}
											if (outstandingPaidDetails
													.containsKey("paidAmount")) {

												policyclmDto
														.setPaidAmount(String
																.valueOf(outstandingPaidDetails
																		.get("paidAmount")));
											}
										}
										outstandingPaidDetails = null;
									} else {

										Double sumInsured = dbCalService
												.getInsuredSumInsured(
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getInsuredPatient()
																.getInsuredId()
																.toString(),
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getPolicy()
																.getKey(),
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getInsuredPatient()
																.getLopFlag());

										paymentAmtDto = dbCalService
												.getClaimsPaidNOutStandingAmt(
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getInsuredPatient()
																.getKey(),
														reimbDto.getClaimDto()
																.getNewIntimationDto()
																.getIntimationId(),
														sumInsured);
										policyclmDto
												.setOutstandingAmount(String.valueOf(paymentAmtDto
														.getProvisionAmout()));
										policyclmDto
												.setPaidAmount(String.valueOf(paymentAmtDto
														.getPaidAmout()));
										paymentAmtDto = null;
									}

								}

								Long claimedAmt = preauthService
										.getClaimedAmountForRODByClaimKey(
												reimbursement.getClaim()
														.getKey(),
												entityManager);
								policyclmDto
										.setClaimedAmount(claimedAmt != null ? Double
												.valueOf(claimedAmt.toString())
												: 0d);

								if (reimbDto.getTreatmentTypeId() != null) {
									managementType = entityManager
											.find(MastersValue.class, reimbDto
													.getTreatmentTypeId());

									policyclmDto
											.setManagementType(managementType != null ? managementType
													.getValue() : "");
								}
								
								managementType = null;

								Query reimbquery = entityManager
										.createNamedQuery("ReimbursementQuery.findByReimbursement");
								reimbquery = reimbquery.setParameter(
										"reimbursementKey", reimbDto.getKey());

								reimbQueryList = reimbquery
										.getResultList();

								if (reimbQueryList != null
										&& !reimbQueryList.isEmpty()) {

									Date qDate = reimbQueryList.get(0)
											.getCreatedDate();
									String queryRiseDate = qDate != null ? new SimpleDateFormat(
											"dd/MM/yyyy").format(reimbQueryList
											.get(0).getCreatedDate()) : "";
									policyclmDto
											.setQueryRaisedDate(queryRiseDate);
									policyclmDto
											.setQueryReason(reimbQueryList.get(
													0).getQueryRemarks() != null ? reimbQueryList
													.get(0).getQueryRemarks()
													: "");
									
									
									policyclmDto
									.setQueryReplyReceivedDate(reimbQueryList.get(
											0).getQueryReplyDate() != null ? new SimpleDateFormat(
													"dd/MM/yyyy").format(reimbQueryList.get(0).getQueryReplyDate()): "");

								}
								
								reimbQueryList = null;
								
								/*
								 * Query reimbqueryRecieved = entityManager
								 * .createNamedQuery(
								 * "ReimbursementQuery.findByReimbursementForQueryReceived"
								 * ); reimbqueryRecieved = reimbqueryRecieved
								 * .setParameter("reimbursementKey",
								 * reimbDto.getKey());
								 * 
								 * List<ReimbursementQuery>
								 * reimbQueryReceivedList =
								 * (List<ReimbursementQuery>)reimbqueryRecieved
								 * .getResultList();
								 */

								/*
								 * if (reimbQueryReceivedList != null &&
								 * !reimbQueryReceivedList.isEmpty()) { Date
								 * qrDate = reimbQueryReceivedList.get(0)
								 * .getModifiedDate(); String queryReceivedDate
								 * = qrDate != null ? new SimpleDateFormat(
								 * "dd/MM/yyyy").format(qrDate) : "";
								 * 
								 * }
								 */

								Query reimbRejection = entityManager
										.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
								reimbRejection = reimbRejection.setParameter(
										"reimbursementKey", reimbDto.getKey());

								rejectionList = reimbRejection
										.getResultList();

								if (rejectionList != null
										&& !rejectionList.isEmpty()) {
									policyclmDto
											.setRejectionReason(rejectionList
													.get(0)
													.getRejectionRemarks());
									Date rDate = rejectionList.get(0)
											.getCreatedDate();
									String rejectionDate = rDate != null ? new SimpleDateFormat(
											"dd/MM/yyyy").format(rDate) : "";
									policyclmDto
											.setClaimRejectedDate(rejectionDate);

								}

								rejectionList = null;
								
								if(claimedAmt == null ||(claimedAmt != null && claimedAmt.intValue() == 0)){
									policyclmDto.setClaimedAmount(Double
											.valueOf(reimbDto.getClaimDto().getClaimedAmount()));
								}
								
							}
							reimbDto = null;
							clmDto = null;
							preauthDto = null;

						}
						reimbursement = null;

					}
					reimbursementList = null;

				}
			}

		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}

		return policyclmDto;
	}
	
	private PolicywiseClaimReportDto updateDiagnosisDetails(PolicywiseClaimReportDto policyClmDto, PreauthDTO preauthDto){
	
		List<PedValidation> pedDiagList =	(new PreauthService()).findPedValidationByPreauthKey(preauthDto.getKey(),entityManager);
		List<DiagnosisDetailsTableDTO> diagList =( PreMedicalMapper.getInstance()).getNewPedValidationTableListDto(pedDiagList);

		if (diagList != null && !diagList.isEmpty()) {

			String diagnosis = "";
			String icdCode = "";
			String icdCodeDesc = "";
			String icdChapter = "";
			String icdBlock = "";
			Diagnosis diagObj = null;
			IcdChapter icdChapterObj = null;
			IcdCode icdCodeObj = null;
			IcdBlock icdBlkObj = null;
			for (DiagnosisDetailsTableDTO diagDto : diagList) {

				diagObj = entityManager.find(
						Diagnosis.class,
						diagDto.getDiagnosisId());

				if (diagObj != null) {
					diagnosis = !("")
							.equalsIgnoreCase(diagnosis) ? diagnosis
							+ " / "
							+ diagObj.getValue()
							: diagObj.getValue();
				}

				icdChapterObj = entityManager
						.find(IcdChapter.class, diagDto
								.getIcdChapter()
								.getId());
			icdCodeObj = entityManager
						.find(IcdCode.class, diagDto
								.getIcdCode().getId());
			icdBlkObj = entityManager
						.find(IcdBlock.class, diagDto
								.getIcdBlock().getId());
//				if (icdChapterObj != null) {
//					icdCode = icdCode.equals("") ? icdChapterObj
//							.getValue() : icdChapter
//							+ ", "
//							+ icdChapterObj.getValue();
//				}
				if (icdCodeObj != null) {
					icdCode = icdCode.equals("") ? icdCodeObj
							.getValue() : icdCode
							+ ", "
							+ icdCodeObj.getValue();
							
							icdCodeDesc = icdCodeDesc.equals("") ? icdCodeObj
									.getDescription() : icdCodeDesc
									+ ", "
									+ icdCodeObj.getDescription();
				}
//				if (icdBlkObj != null) {
//					icdBlock = icdBlock.equals("") ? icdBlkObj
//							.getValue() : icdBlock
//							+ ", "
//							+ icdBlkObj.getValue();
//				}

			}

			policyClmDto.setiCDCode(icdCode);
			policyClmDto.setiCDDescription(icdCodeDesc);
			if (diagnosis != null) {
				policyClmDto
						.setProvisionalDiagnosis(diagnosis);
			}
			icdCode = null;
			diagObj = null;
		}
		return policyClmDto;
	
	}
	
	public Insured getInsuredByKey(Long key) {

		Query query = entityManager
				.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", key);
		List<Insured> insuredList = (List<Insured>) query.getResultList();
		if (insuredList != null && ! insuredList.isEmpty())
			return insuredList.get(0);
		return null;

	}
	
	public Insured getInsuredByPolicyAndInsuredId(String policyNo , Long insuredId, String lobFlag) {
		Query query = entityManager.createNamedQuery("Insured.findByInsuredIdAndPolicyNo");
		query = query.setParameter("policyNo", policyNo);
		if(null != insuredId)
		query = query.setParameter("insuredId", insuredId);
		query = query.setParameter("lobFlag", lobFlag != null && !lobFlag.isEmpty() ? lobFlag : SHAConstants.HEALTH_LOB_FLAG);
		Insured insuredResult = null;
		List<Insured> insuredList = query.getResultList();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			for (Insured insured : insuredList) {
				if(SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(insured.getLopFlag())){
					insuredResult = insured;
					break;
				}
				else if(SHAConstants.PA_CLAIM_TYPE.equalsIgnoreCase(insured.getLopFlag())){
					insuredResult = insured;
					break;					
				}
			}
		}
		return insuredResult;
	}

	public List<NewIntimationNotAdheringToANHDto> getNotAdheringToANHReport(WeakHashMap<String, Object> filters,DBCalculationService dbCalService) {

	
		Date fromDate = null;
		Date toDate = null;
		String userId = "";
		
		List<NewIntimationNotAdheringToANHDto> finalList = new ArrayList<NewIntimationNotAdheringToANHDto>();
		
		if(filters != null && !filters.isEmpty() && filters.containsKey(BPMClientContext.USERID) && filters.get(BPMClientContext.USERID) != null){
			
			userId = String.valueOf(filters.get(BPMClientContext.USERID));
		}
		
		try
		{	
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();
			
			if(filters != null && !filters.isEmpty()){
				
				if(filters.containsKey("fromDate") && filters.get("fromDate") != null){
					
					fromDate = (Date)filters.get("fromDate");
				}
				
				if(filters.containsKey("endDate") && filters.get("endDate") != null){
					
					toDate = (Date)filters.get("endDate");
				}
			
				if(null != fromDate && null != toDate)
				{
					java.util.Date utilFromDate = fromDate;
					java.util.Date utilToDate = toDate;
					SelectValue cpuSelect = filters.containsKey("cpucode") ? (SelectValue)filters.get("cpucode") : null;
					String[] cpusplit = cpuSelect != null ? cpuSelect.getValue().split(" - ") : null;
					Long cpuCode = cpusplit != null && cpusplit.length > 0 ? Long.valueOf(cpusplit[0]) : 0l; 
					
					
				    java.sql.Date sqlFrmDate = new java.sql.Date(utilFromDate.getTime()); 
				    java.sql.Date sqlToDate = new java.sql.Date(utilToDate.getTime());				    
				    
				    ClaimsReportService.popinReportLog(entityManager, userId, "NotAdheringToANHReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				    finalList = dbCalService.getNotAdheringToANHRList(sqlFrmDate,sqlToDate,cpuCode,userId); 
				    ClaimsReportService.popinReportLog(entityManager, userId, "NotAdheringToANHReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				}
		   }
			utx.commit();
		}catch(Exception e){
				e.printStackTrace();
				ClaimsReportService.popinReportLog(entityManager, userId, "NotAdheringToANHReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
		return finalList;
	

	}
	
	public List<ViewNegotiationDetailsDTO> getNegotiationReportDetails(Map<String,Object> filters,DBCalculationService dbService){
		
		List<ViewNegotiationDetailsDTO> searchResultList = new ArrayList<ViewNegotiationDetailsDTO>();
		
		Date frmDate = null;
		Date toDate = null;
		Long stageId = null;
		Long cpuId= null;
		String userId = null;
		String intimationNo = null;
		
		try{
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();
			
			if(filters != null && !filters.isEmpty()){
					if(filters.containsKey("fromDate") && filters.get("fromDate") != null){
						frmDate = (Date) filters.get("fromDate");
					}
					if(filters.containsKey("endDate") && filters.get("endDate") != null){
						toDate = (Date) filters.get("endDate");
					}
					if(filters.containsKey("cpuKey") && filters.get("cpuKey") != null){
						Long cpuCode = (Long) filters.get("cpuKey");
						TmpCPUCode cpuDtls = getMasCpuCode(cpuCode);
						if(cpuDtls != null){
							cpuId = cpuDtls.getKey();
						}
						
					}
					if(filters.containsKey("clmStageKey") && filters.get("clmStageKey") != null){
						stageId = (Long) filters.get("clmStageKey");
					}
					if(filters.containsKey("UserName") && filters.get("UserName") != null){
						userId = (String) filters.get("UserName");
					}
					if(filters.containsKey("intimationNo") && filters.get("intimationNo") != null){
						intimationNo = (String) filters.get("intimationNo");
					}
					
//					if(frmDate != null && toDate != null){
						java.util.Date utilFromDate = frmDate != null ? frmDate : null;
						java.util.Date utilToDate = toDate != null ? toDate : null;
						java.sql.Date sqlFrmDate = null;
						java.sql.Date sqlToDate = null;
						if(utilFromDate != null){
							sqlFrmDate = new java.sql.Date(utilFromDate.getTime());
						}
						if(utilToDate != null){
							sqlToDate = new java.sql.Date(utilToDate.getTime());
						}
					    
					    ClaimsReportService.popinReportLog(entityManager, userId, "NegotiationReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
					    searchResultList = dbService.getNegotiationReport(sqlFrmDate,sqlToDate,stageId,intimationNo,cpuId,userId); 
					    ClaimsReportService.popinReportLog(entityManager, userId, "NegotiationReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
//					}
			}
			utx.commit();
		}catch(Exception e){
			e.printStackTrace();
			ClaimsReportService.popinReportLog(entityManager, userId, "NegotiationReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
		return searchResultList;

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
