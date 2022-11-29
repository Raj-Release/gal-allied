package com.shaic.claim.reports.hospitalintimationstatus;

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
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
@Stateless
public class HospitalIntimationReportStatusService extends AbstractDAO<Intimation>{
	@PersistenceContext
	protected EntityManager entityManager;
	
	public HospitalIntimationReportStatusService() {
		super();
	}
	
	public  Page<HospitalIntimationReportStatusTableDTO> search(HospitalIntimationStatusReportFormDTO hospitalFormDTO,String userName, String passWord) {
		
		

		List<Intimation> listIntimations = new ArrayList<Intimation>(); 
		try
		{
			Date fromDate = null != hospitalFormDTO.getFromDate() ? hospitalFormDTO.getFromDate() : null;
			Date toDate = null != hospitalFormDTO.getToDate() ? hospitalFormDTO.getToDate() : null;
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
			
			Root<Intimation> root = criteriaQuery.from(Intimation.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			/*if(null != fromDate && null != toDate)
			{
				Predicate condition1 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition1);	
				
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition2 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition2);
			}*/
			
			
			if(null != fromDate)
			{
				Predicate condition6 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition6);
			}
			
				
							
			if(null!= toDate)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();					
				
				Predicate condition7 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				
			
				conditionList.add(condition7);
			}
			Predicate condition2 = criteriaBuilder.equal(root.<Intimation>get("intimationSource").<Long>get("key"),ReferenceTable.HOSPITAL_PORTAL);
			conditionList.add(condition2);
			
			if (fromDate == null && toDate == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			
			final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = hospitalFormDTO.getPageable().getPageNumber();
			int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 1;
			} 
			
			SHAUtils.popinReportLog(entityManager, userName, "HospitalIntimationReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			listIntimations = typedQuery.getResultList();
			
			if( listIntimations.size()>10)
			{
				listIntimations = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
				}
			
		    List<Intimation> doList = listIntimations;

			List<HospitalIntimationReportStatusTableDTO> tableDTO = HospitalIntimationReportStatusMapper.getInstance().getHospitalIntimationTableObjects(doList);
			
            
			tableDTO = getHospitalDetails(tableDTO);
		
			List<HospitalIntimationReportStatusTableDTO> result = new ArrayList<HospitalIntimationReportStatusTableDTO>();
			result.addAll(tableDTO);
			Page<HospitalIntimationReportStatusTableDTO> page = new Page<HospitalIntimationReportStatusTableDTO>();			
			hospitalFormDTO.getPageable().setPageNumber(pageNumber + 1);
			if(result.size()<=10)
			{
				page.setHasNext(false);
			}
			else
			{
			page.setHasNext(true);
			}
			if (result.isEmpty()) {
			hospitalFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);

			SHAUtils.popinReportLog(entityManager, userName, "HospitalIntimationReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			return page;
		  }
			catch (Exception e) 
		   {
				SHAUtils.popinReportLog(entityManager, userName, "HospitalIntimationReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		     e.printStackTrace();
		   }
		   return null;	
		}
	
	private List<HospitalIntimationReportStatusTableDTO> getHospitalDetails(List<HospitalIntimationReportStatusTableDTO> tableDTO)
	{
		Hospitals hospitalDetail;
		Preauth statusDetail;
		for(int index = 0; index < tableDTO.size(); index++)
		{
			hospitalDetail = getHospitalDetail(tableDTO.get(index).getHospitalNameId());
			if(hospitalDetail != null)
			{
				 tableDTO.get(index).setHospitalName(hospitalDetail.getName());
			     tableDTO.get(index).setHospitalAddress(hospitalDetail.getAddress());
			     tableDTO.get(index).setTelephoneNo(hospitalDetail.getPhoneNumber());
			     tableDTO.get(index).setFax(hospitalDetail.getFax());
			     tableDTO.get(index).setCity(hospitalDetail.getCity());
			     tableDTO.get(index).setState(hospitalDetail.getState());
			  		          		     
			}
			
			statusDetail = getStatusDetail(tableDTO.get(index).getIntimationKey());
			
			if(statusDetail != null)
			{
				if(statusDetail.getStatus().getKey()== ReferenceTable.PREAUTH_APPROVE_STATUS || statusDetail.getStatus().getKey()== ReferenceTable.ENHANCEMENT_APPROVE_STATUS ||
				statusDetail.getStatus().getKey()== ReferenceTable.PREAUTH_REJECT_STATUS || statusDetail.getStatus().getKey()== ReferenceTable.ENHANCEMENT_APPROVE_STATUS ||
				statusDetail.getStatus().getKey()== ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS || statusDetail.getStatus().getKey()== ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS
				|| statusDetail.getStatus().getKey()== ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS  || statusDetail.getStatus().getKey() == ReferenceTable.ENHANCEMENT_DOWNSIZE_STAGE)
				{
					tableDTO.get(index).setStage(statusDetail.getStatus().getProcessValue());
				}
				else
				{
					tableDTO.get(index).setStage("In Process");
				}
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
	private Preauth getStatusDetail(Long intimationKey){
		Query findByintimationNo = entityManager.createNamedQuery(
				"Preauth.findPreAuthIdInDescendingOrder").setParameter("intimationKey", intimationKey);
		List<Preauth> statusDetail;
		try{
			
				statusDetail = (List<Preauth>) findByintimationNo.getResultList();
				if(statusDetail != null && ! statusDetail.isEmpty())
				{
					entityManager.refresh(statusDetail.get(0));
					return statusDetail.get(0);
				}
				
		return null;
		}
		catch(Exception e){
			return null;
		}
	}


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
		
	

}
