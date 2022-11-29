package com.shaic.claim.reports.fvrassignmentreport;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.Intimation;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@Stateless

@TransactionManagement(TransactionManagementType.BEAN)

public class FVRAssignmentReportService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;

	private Long reportType;
	
	public FVRAssignmentReportService() {
		super();
	}
	
//	public  Page<FVRAssignmentReportTableDTO> search(FVRAssignmentReportFormDTO fvrFormDTO,String userName, String passWord,UsertoCPUMappingService userCPUMapService) {
	public  Page<FVRAssignmentReportTableDTO> search(FVRAssignmentReportFormDTO fvrFormDTO,String userName, String passWord,UsertoCPUMappingService userCPUMapService,DBCalculationService dbService) {
		
		try
		{
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);	
			utx.begin();
			
			Long cpuCode = null != fvrFormDTO.getCpuCode()? fvrFormDTO.getCpuCode().getId(): null;
			String cpuCodeValue = null != fvrFormDTO.getCpuCode()? fvrFormDTO.getCpuCode().getValue() : null;
			
			if(null != cpuCodeValue)
				{
					String cpuCodeValues[] = cpuCodeValue != null ? cpuCodeValue.split(" ") : null;
					cpuCodeValue = cpuCodeValues != null && cpuCodeValues.length > 0 ? cpuCodeValues[0] : null;
					
					cpuCode = cpuCodeValue != null ? Long.valueOf(cpuCodeValue) : 0l;
				}
			String fvrCpuCode = null != fvrFormDTO.getFvrCpuCode() ? fvrFormDTO.getFvrCpuCode().getValue(): null;
			Long fvrCpuCodeValue = null;
			if(null != fvrCpuCode)
			{
				String cpuCodeValues[] = fvrCpuCode != null ? fvrCpuCode.split(" ") : null;
				fvrCpuCode = cpuCodeValues != null && cpuCodeValues.length > 0 ? cpuCodeValues[0] : null;
				fvrCpuCodeValue = Long.valueOf(fvrCpuCode); 
			}
			reportType =null != fvrFormDTO.getReportType() ? fvrFormDTO.getReportType().getId() : 0l;		
			Date fromDate = null != fvrFormDTO.getFromDate() ? fvrFormDTO.getFromDate() : null;
			Date toDate = null != fvrFormDTO.getToDate() ? fvrFormDTO.getToDate() : null;
			Long clmType = 0l;
			
//			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();			
//			final CriteriaQuery<FieldVisitRequest> criteriaQuery = criteriaBuilder.createQuery(FieldVisitRequest.class);			
//			Root<FieldVisitRequest> root = criteriaQuery.from(FieldVisitRequest.class);			
//			
//			List<Predicate> conditionList = new ArrayList<Predicate>();	
//			
//						
//			if(null != reportType)
//			{
//				
//				String strClaimType = null;
//				Long typeId = 0l;
//				SelectValue selClaimType = fvrFormDTO.getClaimType();
//				if(null != selClaimType && null != selClaimType.getValue())
//				{
//					strClaimType = selClaimType.getValue();
//					typeId = selClaimType.getId();
//				}
//				
//				
//				
//			
//				
//			if(null != strClaimType && !("").equalsIgnoreCase(strClaimType))
//			{
//				Predicate condition6 = criteriaBuilder.equal(root.<Claim>get("claim").<MastersValue>get("claimType").<Long>get("key"),typeId);
//				conditionList.add(condition6);
//			}
//			if(null != cpuCode)
//			{
//	        
//			Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), cpuCode);
//				conditionList.add(condition1);
//			}
//			else{
//				List<Long> cpuKeyList = userCPUMapService.getCPUCodeList(userName, entityManager);
//				Predicate UserCpuCondition = root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
//				conditionList.add(UserCpuCondition);
//			}
//			
//			if(null != fvrCpuCode)
//			{
//				String cpuCodeValues[] = fvrCpuCode != null ? fvrCpuCode.split(" ") : null;
//				fvrCpuCode = cpuCodeValues != null && cpuCodeValues.length > 0 ? cpuCodeValues[0] : null;
//				Predicate condition3 = criteriaBuilder.equal(root.<Long>get("fvrCpuId"), fvrCpuCode);
//				conditionList.add(condition3);	
//			}
//			
//			if(null != reportType && reportType == 51l){
//				
//				if(null != fromDate && null != toDate)
//				{
//					Predicate condition3 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("assignedDate"), fromDate);
//					conditionList.add(condition3);	
//					Calendar c = Calendar.getInstance();
//					c.setTime(toDate);
//					c.add(Calendar.DATE, 1);
//					toDate = c.getTime();
//					Predicate condition4 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("assignedDate"), toDate);
//					conditionList.add(condition4);
//				}				
//				
//			}
//			
//			if(null != reportType && reportType == 49l){
//				
//				Predicate condition3 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
//				conditionList.add(condition3);	
//				Calendar c = Calendar.getInstance();
//				c.setTime(toDate);
//				c.add(Calendar.DATE, 1);
//				toDate = c.getTime();
//				Predicate condition4 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
//				conditionList.add(condition4);
//				
//			}
//			
//			if(null != reportType && reportType == 52l){
//				
//				Predicate condition3 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("modifiedDate"), fromDate);
//				conditionList.add(condition3);	
//				Calendar c = Calendar.getInstance();
//				c.setTime(toDate);
//				c.add(Calendar.DATE, 1);
//				toDate = c.getTime();
//				Predicate condition4 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("modifiedDate"), toDate);
//				conditionList.add(condition4);
//			}
//			
//			if(null != reportType && reportType == 51l)
//			{
//							
//				
//				List<Long> myStatusList = new ArrayList<Long>();				
//				myStatusList.add(ReferenceTable.FVR_REPLY_RECEIVED);		
//				myStatusList.add(ReferenceTable.ASSIGNFVR);				
//				Expression<Long> exp = root.<Status>get("status").<Long>get("key");
//				Predicate condition3 = exp.in(myStatusList);				
//				conditionList.add(condition3);
//				
//			}
//			else
//			{
//				Predicate condition1= criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"), reportType);				
//				conditionList.add(condition1);	
//				
//			}
//				
//			}
//			
//	
//				if (cpuCode == null && reportType == null && fromDate == null && toDate == null) 
//				{
//					criteriaQuery.select(root);
//					
//				} 
//				else 
//				{
//					criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
//					
//				}
//				
				int pageNumber = fvrFormDTO.getPageable().getPageNumber();
//				int firtResult;
//				if (pageNumber > 1) 
//				{
//					firtResult = (pageNumber - 1) * 10;
//				} 
//				else 
//				{
//					firtResult = 1;
//				}
//				
//			
//					
//				final TypedQuery<FieldVisitRequest> typedQuery = entityManager.createQuery(criteriaQuery);
//				fieldvisitList = typedQuery.getResultList();
//				if (cpuCode == null && reportType == null && fromDate == null && toDate == null && fieldvisitList.size() > 10) 
//				{
//					fieldvisitList = typedQuery.setFirstResult(firtResult)
//							.setMaxResults(10).getResultList();
//				}
//				
//				List<FieldVisitRequest> doList = fieldvisitList;
//				
//				List<FVRAssignmentReportTableDTO> tableDTO = FVRAssignmentReportMapper.getInstance().getFVRAssignmentReportTableObjects(doList);
//				
//				for (FVRAssignmentReportTableDTO fvrAssignmentReportTableDTO : tableDTO)
//				{
//					
//					TmpEmployee fvrInitiatorDetail = getFvrInitiatorDetails(fvrAssignmentReportTableDTO.getInitiatorId());
//					  if(null != fvrInitiatorDetail){
//						   
//						  fvrAssignmentReportTableDTO.setInitiatorName(fvrInitiatorDetail.getEmpFirstName());
//					  }
//					
//					
//					fvrAssignmentReportTableDTO.setFvrTime(SHAUtils.getTimeFromDate(fvrAssignmentReportTableDTO.getFvrDate()));					
//					
//					fvrAssignmentReportTableDTO.setFvrAssignedTime(SHAUtils.getTimeFromDate(fvrAssignmentReportTableDTO.getFvrAssignedDate()));					
//					
//					fvrAssignmentReportTableDTO.setFvrReceivedTime(SHAUtils.getTimeFromDate(fvrAssignmentReportTableDTO.getFvrReceivedDate()));
//					
//					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");	
//					
//					if(null != fvrAssignmentReportTableDTO.getFvrDate() && null != fvrAssignmentReportTableDTO.getFvrReceivedDate() && fvrAssignmentReportTableDTO.getFvrAssignedDate() != null)
//					{
//					Date fvrAssignTime = format.parse(SHAUtils.formatDateToFindDifference(fvrAssignmentReportTableDTO.getFvrAssignedDate()));
//					Date fvrReceivedTime = format.parse(SHAUtils.formatDateToFindDifference(fvrAssignmentReportTableDTO.getFvrReceivedDate()));
//					
//					long diff = fvrReceivedTime.getTime() - fvrAssignTime.getTime();
//					
//					//long diffSeconds = diff / 1000 % 60;
//					
//					long diffMinutes = diff / (60 * 1000) % 60;
//					long diffHours = diff / (60 * 60 * 1000) % 24;
//					long diffDays = diff / (24 * 60 * 60 * 1000);
//				   
//					fvrAssignmentReportTableDTO.setTat(String.valueOf(diffDays) + "Days" + String.valueOf(diffHours) + "Hours" + String.valueOf(diffMinutes) + " Minutes");
//					}
//					else
//					{
//						fvrAssignmentReportTableDTO.setTat(String.valueOf(0) + "Days" + String.valueOf(0) + "Hours" + String.valueOf(0) + " Minutes");
//					}
//				}
//			tableDTO = getHospitalDetails(tableDTO);
				
				SelectValue selClaimType = fvrFormDTO.getClaimType();
				if(null != selClaimType && null != selClaimType.getId())
				{
					clmType = selClaimType.getId();
				}	
			
			Map<Long,String> fvrReportTypeMap = SHAUtils.getFVRReportTypeMap();
			String fvrReportType =	fvrReportTypeMap.get(reportType);
			
			ClaimsReportService.popinReportLog(entityManager, userName, "FVRAssignmentReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<FVRAssignmentReportTableDTO> resultList = dbService.getFVRReportList(new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()),fvrReportType, clmType, cpuCode, fvrCpuCodeValue,userName);
				
//				List<FVRAssignmentReportTableDTO> result = new ArrayList<FVRAssignmentReportTableDTO>();
//				
//				result.addAll(tableDTO);
				
				Page<FVRAssignmentReportTableDTO> page = new Page<FVRAssignmentReportTableDTO>();
				fvrFormDTO.getPageable().setPageNumber(pageNumber + 1);
				page.setHasNext(true);
				if (resultList.isEmpty()) 
				{
					fvrFormDTO.getPageable().setPageNumber(1);
				}
				page.setPageNumber(pageNumber);
				page.setPageItems(resultList);
				page.setIsDbSearch(true);
				ClaimsReportService.popinReportLog(entityManager, userName, "FVRAssignmentReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
				
				utx.commit();
				 
				return page;
				
		} 
		catch (Exception e) {
			ClaimsReportService.popinReportLog(entityManager, userName, "FVRAssignmentReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	
		
	
	/*private List<FVRAssignmentReportTableDTO> getHospitalDetails(List<FVRAssignmentReportTableDTO> tableDTO) {
		
		Hospitals hospitalDetail;

		  for (int index = 0; index < tableDTO.size(); index++) {

			Query findByHospitalKey = entityManager.createNamedQuery("Hospitals.findByKey").setParameter("key",	tableDTO.get(index).getHospitalNameId());
			try {

				hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
				if (null != hospitalDetail) {
					tableDTO.get(index).setHospitalName(hospitalDetail.getName());
					tableDTO.get(index).setHospitalType(hospitalDetail.getHospitalType().getValue());
					tableDTO.get(index).setLocation(hospitalDetail.getCity());
					tableDTO.get(index).setHospitalCode(hospitalDetail.getHospitalCode());
					
				}	
				
			//	FieldVisitRequest fvrInitiatorDetail = getFvrInitiatorDetails()
						
				if(reportType == 52l)
				{
					 tableDTO.get(index).setFvrExecutiveComments(null);
				}
				if(reportType == 51l || reportType == 49l)
				{
					 tableDTO.get(index).setFvrNotRequiredComments(null);
				}
				
			} catch (Exception e) {
				continue;
			}

		}

		return tableDTO;
	}*/
	  
	  
	  /*private FieldVisitRequest getFvrAssignmentPending(Long preauthKey)
			{
		  FieldVisitRequest fvrPendingDetail;
				Query findByTransactionKey = entityManager.createNamedQuery(
						"FieldVisitRequest.findByTransactionKey").setParameter("transactionKey", preauthKey);
				try{
					fvrPendingDetail =(FieldVisitRequest) findByTransactionKey.getSingleResult();
					return fvrPendingDetail;
				}
				catch(Exception e)
				{
					return null;
				}
									
			}*/
	  
	  
	  /*private TmpEmployee getFvrInitiatorDetails(String initiatorId)
		{
		  TmpEmployee fvrInitiatorDetail;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId);
			try{
				fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
				return fvrInitiatorDetail;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}
	  
	  private Claim getFvrPendingDetails(Long claimkey)
		{
		  Claim fvrPendingDetail;
			Query findByClaimKey = entityManager.createNamedQuery(
					"Preauth.findByClaimKey").setParameter("key", claimkey);
			try{
				fvrPendingDetail = (Claim) findByClaimKey.getSingleResult();
				return fvrPendingDetail;
			}
			catch(Exception e)
			{
				return null;
			}
			
			
				
		}*/
	  
	
	
	/*public Preauth getLatestPreauthByClaim(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
		query.setParameter("claimkey", claimKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}
	
	private Preauth getFVRNotRequiredDetails(Long transactionKey)
	{
		Preauth fvrDetail; 
		Query findByTransactionKey = entityManager.createNamedQuery(
				"Preauth.findByKey").setParameter("preauthKey", transactionKey);
		try{
			fvrDetail = (Preauth) findByTransactionKey.getSingleResult();
			return fvrDetail;
		}
		catch(Exception e)
		{
			return null;
		}
		
		
			
	}*/
	
	
		
	
	/*String fvrNotRequestRemarks = null != getLatestPreauthByClaim(tableDTO.get(index).getClaimKey()) ? getLatestPreauthByClaim(tableDTO.get(index).getClaimKey())
			.getFvrNotRequiredRemarks().getValue() : "";
	tableDTO.get(index).setFvrNotRequiredComments(fvrNotRequestRemarks);*/
	
		}
		
			
	
		
		

		
