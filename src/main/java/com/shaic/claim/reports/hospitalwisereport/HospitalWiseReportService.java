package com.shaic.claim.reports.hospitalwisereport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
@Stateless
public class HospitalWiseReportService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public HospitalWiseReportService() {
		super();
	}
	
	public  Page<HospitalWiseReportTableDTO> search(HospitalWiseReportFormDTO hospitalFormDTO,String userName, String passWord,UsertoCPUMappingService userCPUMapService) {
		
		List<Reimbursement> listIntimations = new ArrayList<Reimbursement>(); 
		try{
		
		Long dateType = null != hospitalFormDTO.getDateType()? hospitalFormDTO.getDateType().getId() : null;
		String hospitalCode =null != hospitalFormDTO.getHospitalCode() ? hospitalFormDTO.getHospitalCode(): null;
		Date fromDate = null != hospitalFormDTO.getFromDate() ? hospitalFormDTO.getFromDate() : null;
		Date toDate = null != hospitalFormDTO.getToDate() ? hospitalFormDTO.getToDate() : null;
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Reimbursement> criteriaQuery = criteriaBuilder.createQuery(Reimbursement.class);
		
		Long hospitalkey = null;
		
		if(hospitalCode != "")
		{
		 hospitalkey = getHospitalDetails(hospitalCode).getKey();
		}
		

		 Root<Reimbursement> root = criteriaQuery.from(Reimbursement.class);
 		 List<Predicate> conditionList = new ArrayList<Predicate>();
		
		
		/*if(fromDate != null && toDate != null){
			Predicate condition5 = criteriaBuilder.greaterThanOrEqualTo(root.<Intimation>get("intimation").<Date>get("createdDate"), fromDate);
			conditionList.add(condition5);
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();
			Predicate condition6 = criteriaBuilder.lessThanOrEqualTo(root.<Intimation>get("intimation").<Date>get("createdDate"), toDate);
			conditionList.add(condition6);
			}*/
		
		
		  if(dateType!= null){
			
			if(dateType == 1){
				
				if(fromDate != null && toDate != null){
					Predicate condition5 = criteriaBuilder.greaterThanOrEqualTo(root.<Claim>get("claim").<Intimation>get("intimation").<Date>get("createdDate"), fromDate);
					conditionList.add(condition5);
					Calendar c = Calendar.getInstance();
					c.setTime(toDate);
					c.add(Calendar.DATE, 1);
					toDate = c.getTime();
					Predicate condition6 = criteriaBuilder.lessThanOrEqualTo(root.<Claim>get("claim").<Intimation>get("intimation").<Date>get("createdDate"), toDate);
					conditionList.add(condition6);
			      }
		      }
			
			else{				
				if(fromDate != null && toDate != null){
					Predicate condition5 = criteriaBuilder.greaterThanOrEqualTo(root.<Claim>get("claim").<Intimation>get("intimation").<Date>get("admissionDate"), fromDate);

					conditionList.add(condition5);
					Calendar c = Calendar.getInstance();
					c.setTime(toDate);
					c.add(Calendar.DATE, 1);
					toDate = c.getTime();
					Predicate condition6 = criteriaBuilder.lessThanOrEqualTo(root.<Claim>get("claim").<Intimation>get("intimation").<Date>get("admissionDate"), toDate);

					conditionList.add(condition6);
			       }		
			
		         }
			
		     }
		  
		  String hospitalisationFlag = "Y";
		 
		  
		
		if(hospitalkey !=null)
			
		{
			Predicate condition1 = criteriaBuilder.equal(root.<Claim>get("claim").<Intimation>get("intimation").<String>get("hospital"),hospitalkey);
			conditionList.add(condition1);
			
			
		}
		Predicate condition2 = criteriaBuilder.equal(root.<DocAcknowledgement>get("docAcknowLedgement").<String>get("hospitalisationFlag"),hospitalisationFlag);
		conditionList.add(condition2);
		
		List<Long> cpuKeyList = userCPUMapService.getCPUCodeList(userName, entityManager);
		Predicate cpuCondition = root.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key").in(cpuKeyList);
		conditionList.add(cpuCondition);
		
		
		/*Predicate condition3 = criteriaBuilder.equal(root.<Stage>get("stage").<Long>get("key"),ReferenceTable.FINANCIAL_STAGE);
		conditionList.add(condition3);			
		Predicate condition4 = criteriaBuilder.equal(root.<Status>get("status").<Long>get("key"),ReferenceTable.FINANCIAL_APPROVE_STATUS);
		conditionList.add(condition4);
						*/
		if (dateType == null && hospitalCode == null )
		{
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[] {}));
		} 
		else 
		{
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
		}

		final TypedQuery<Reimbursement> typedQuery = entityManager.createQuery(criteriaQuery);
//		int pageNumber = hospitalFormDTO.getPageable().getPageNumber();
		/*@SuppressWarnings("unused")
		int firtResult;
		
		if (pageNumber > 1) {
			firtResult = (pageNumber - 1) * 10;
		} 
		else {
			firtResult = 1;
		}*/

		
		SHAUtils.popinReportLog(entityManager, userName, "HospitalWiseReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
				
		listIntimations = typedQuery.getResultList();
				
	    List<Reimbursement> doList = listIntimations;

		List<HospitalWiseReportTableDTO> tableDTO = HospitalWiseReportMapper.getInstance().getHospitalWiseTableObjects(doList);

		tableDTO = getTableDetails(tableDTO);
	
		List<HospitalWiseReportTableDTO> result = new ArrayList<HospitalWiseReportTableDTO>();
		result.addAll(tableDTO);
		Page<HospitalWiseReportTableDTO> page = new Page<HospitalWiseReportTableDTO>();
//		hospitalFormDTO.getPageable().setPageNumber(pageNumber + 1);
//		page.setHasNext(true);
//		if (result.isEmpty()) {
//			hospitalFormDTO.getPageable().setPageNumber(1);
//		}
//		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		page.setIsDbSearch(true);
		SHAUtils.popinReportLog(entityManager, userName, "HospitalWiseReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
		return page;
	  }
		catch (Exception e) 
	   {
			SHAUtils.popinReportLog(entityManager, userName, "HospitalWiseReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
	     e.printStackTrace();
	   }
	   return null;	
	}

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	private List<HospitalWiseReportTableDTO> getTableDetails(List<HospitalWiseReportTableDTO> tableDTO)
	{
		Hospitals hospitalDetail;
		List<Preauth> preauthList;
		FieldVisitRequest fvrDetail;
		List<PedValidation> pedValidationList;
		ClaimPayment checkDetails;
		for(int index = 0; index < tableDTO.size(); index++)
		{
			hospitalDetail = getHospitalDetail(tableDTO.get(index).getHospitalTypeId());
			if(hospitalDetail != null)
			{
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
			     tableDTO.get(index).setHospitalCode(hospitalDetail.getHospitalCode());
			}
			
			preauthList = getAmtDetails(tableDTO.get(index).getKey());
			if(preauthList != null && !preauthList.isEmpty())
			{
				tableDTO.get(index).setCashlessAuthorizedAmount(preauthList.get(0).getTotalApprovalAmount());
			}
					
			 fvrDetail = getFvrDetails(tableDTO.get(index).getKey());
			 if(fvrDetail != null)
			 {
				Date fvrDate = fvrDetail.getAssignedDate();
				if(null!=fvrDate)
					{
						String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(fvrDate);
						tableDTO.get(index).setFvrAllocatedDt(dateformat);
					}
				tableDTO.get(index).setFieldVisitor(fvrDetail.getRepresentativeName());
			 }
			 
			
			/* 
			 Reimbursement dateDetail = getDateDetail(tableDTO.get(index).getKey());
			 if(dateDetail != null)
			 {
				 	tableDTO.get(index).setDateOfAdmission(dateDetail.getDateOfAdmission());
				 	tableDTO.get(index).setDurationOfStay(dateDetail.getNumberOfDays());
				 	tableDTO.get(index).setDateOfDischarge(dateDetail.getDateOfDischarge());
				 	//tableDTO.get(index).setCashlessAuthorizedAmount(dateDetail.getApprovedAmount());
			}
						 */
			 
			pedValidationList = getDaiganosis(tableDTO.get(index).getPreauthKey());
			StringBuffer icdCode = new StringBuffer("");
			StringBuffer daignosis = new StringBuffer("");
			
			 if(!pedValidationList.isEmpty())
			 {
					icdCode.append(pedValidationList.get(pedValidationList.size()-1).getIcdCodeId());
					daignosis.append(getDiagnosisName(pedValidationList.get(pedValidationList.size()-1).getDiagnosisId()));
			 }
			 
			 tableDTO.get(index).setIcdCodes(icdCode.toString());
			 tableDTO.get(index).setDiagnosis(daignosis.toString());
			 
			 
			 checkDetails = getCheckDetails(tableDTO.get(index).getRodNO());
			 if(null != checkDetails)
			 {
				 tableDTO.get(index).setChequeDate(null != checkDetails.getChequeDDDate() ? checkDetails.getChequeDDDate() : null);
				 tableDTO.get(index).setChequeNo(checkDetails.getChequeDDNumber());
				 tableDTO.get(index).setChequeAmount(checkDetails.getNetAmount());
				 Double tdsAmnt = null != checkDetails.getTdsAmount() ? checkDetails.getTdsAmount() : 0d;
				 Double netAmnt = null != checkDetails.getNetAmount() ? checkDetails.getNetAmount() : 0d;
				
				tableDTO.get(index).setPaidAmt(netAmnt + tdsAmnt);
			/*	
				if(null == tableDTO.get(index).getPaidAmt())
				{
					tableDTO.get(index).setPaidAmt(0d);
				}*/
					
				 Double claimAmnt = null != tableDTO.get(index).getClaimedAmt() ? tableDTO.get(index).getClaimedAmt() : 0d;
				 Double paidAmnt = null != tableDTO.get(index).getPaidAmt() ? tableDTO.get(index).getPaidAmt() : 0d;
				
				 tableDTO.get(index).setOutstandingAmount(claimAmnt - paidAmnt );
				 
			 }
			 hospitalDetail = null;
			 preauthList = null;
			 fvrDetail = null;
			 pedValidationList = null;
			 checkDetails = null;
				
		    }
		
		
		   return tableDTO;
		
	      }
	
			

	    @SuppressWarnings("unchecked")
		private  List<Preauth>  getAmtDetails(Long key)
	    {
	    	List<Preauth> claimAmtDeatil = null;
	    	//Double claimAmt=0d;
	    
	    	Query findByLatestIntimationKey = entityManager
					.createNamedQuery("Preauth.findByLatestIntimationKey");
	    	findByLatestIntimationKey.setParameter("intimationKey",key);
			try
			{
				claimAmtDeatil = findByLatestIntimationKey.getResultList();
				
			}
	    	
			catch(Exception e)
			{
				
			}
	    	
	    	
			return claimAmtDeatil;
	    	
	    }
	        
	    
	    	 
	        private FieldVisitRequest getFvrDetails(
	 		Long intimationKey)
	        {
				
			Query findByIntimationKey = entityManager.createNamedQuery(
					"FieldVisitRequest.findByIntimationKey").setParameter("intimationKey", intimationKey);
			try{
				List<FieldVisitRequest> fvrDetail = (List<FieldVisitRequest>) findByIntimationKey.getResultList();
			
				if(!fvrDetail.isEmpty())
				return fvrDetail.get(fvrDetail.size()-1);
			
			}catch(Exception e)
			{

				
			}
		
			return null;
		
	}
	
	
	
	
		
	
	private Hospitals getHospitalDetails(String hospitalId)
	{
		Hospitals hospitalDetail;
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByCode").setParameter("hospitalCode", hospitalId);
		try{
			hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			return hospitalDetail;
		}
		catch(Exception e)
		{
		
			return null;
		}
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
	private List<PedValidation> getDaiganosis(Long preauthKey){
		Query findByPreathKey = entityManager.createNamedQuery(
				"PedValidation.findByTransactionKey").setParameter("transactionKey", preauthKey);
		List<PedValidation> pedValidationList;
		try{
			
			pedValidationList =  findByPreathKey.getResultList();
			
			
			
		return pedValidationList;
		}
		catch(Exception e){
			return null;
		}
	}
	
	
	  private String getDiagnosisName(Long diagnosisId){
		Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
		diagnosis.setParameter("diagnosisKey", diagnosisId);
		Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
		if(masters != null){
			return masters.getValue();
		}
		return null;
	}
	
	  
	  public ClaimPayment getCheckDetails(String rodNo) {
			List <ClaimPayment> checkDetail;
						
				Query findByRodNo = entityManager.createNamedQuery("ClaimPayment.findByRodNo");
				findByRodNo.setParameter("rodNumber", rodNo);
						
				try{
					checkDetail = findByRodNo.getResultList();
					if(null != checkDetail && !checkDetail.isEmpty())
					return checkDetail.get(0);
					
				}catch(Exception e)
				{
					e.printStackTrace();
					
				}
				return null;
	
}

	  
	
}
	


