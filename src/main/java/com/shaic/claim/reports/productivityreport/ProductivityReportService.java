package com.shaic.claim.reports.productivityreport;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ProductivityReportService extends AbstractDAO<Intimation>{
	@PersistenceContext
	protected EntityManager entityManager;
	protected Date date;
	
	@Resource
	private UserTransaction utx;
	
	public ProductivityReportService() {
		super();
	}
	
	public  Page<ProductivityReportTableDTO> search(ProductivityReportFormDTO productivityFormDTO,String userName, String passWord, DBCalculationService dbService) {
		
		

		//List<Preauth> listIntimations = new ArrayList<Preauth>(); 
		try
		{
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);	
			utx.begin();
			
			date = null != productivityFormDTO.getDate() ? productivityFormDTO.getDate() : null;
		   
						
			/*final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Preauth> criteriaQuery = criteriaBuilder.createQuery(Preauth.class);
			
			Root<Preauth> root = criteriaQuery.from(Preauth.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	*/
			
				
			/*if(null != fromDate)
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
				//Predicate condition7 = criteriaBuilder.(root.<Date>get("createdDate"), toDate);
			
				conditionList.add(condition7);
			}
			
		if (fromDate == null && toDate == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			
			if(fromDate == null && toDate != null)
			{
				
				
			}
			
			final TypedQuery<Preauth> typedQuery = entityManager.createQuery(criteriaQuery);*/			
			
			
			ClaimsReportService.popinReportLog(entityManager, userName, "ProductivityReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<ProductivityReportTableDTO> resultList = dbService.getProductivityReportList(new java.sql.Date(date.getTime()));
			
			Page<ProductivityReportTableDTO> page = new Page<ProductivityReportTableDTO>();
			page.setPageItems(resultList);
			ClaimsReportService.popinReportLog(entityManager, userName, "ProductivityReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			utx.commit();
			return page;
		  
		}
			catch (Exception e) 
		   {
				ClaimsReportService.popinReportLog(entityManager, userName, "ProductivityReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
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
	
	/*private List<DailyReportTableDTO> getHospitalDetails(List<DailyReportTableDTO> tableDTO)
	{
		
		for(int index = 0; index < tableDTO.size(); index++){
			
			Hospitals hospitalDetail = getHospitalDetail(tableDTO.get(index).getHospitalNameId());
			if (null != hospitalDetail) {
				tableDTO.get(index).setHospitalType(hospitalDetail.getHospitalType().getValue());
				//tableDTO.get(index).setHospitalDate(hospitalDetail.);
				tableDTO.get(index).setHospitalName(hospitalDetail.getName());
				tableDTO.get(index).setHospitalState(hospitalDetail.getState());				
				tableDTO.get(index).setHospitalCity(hospitalDetail.getCity());
				tableDTO.get(index).setHospitalCode(hospitalDetail.getHospitalCode());
				
			}	
			
			 FieldVisitRequest fvrDetail = getFvrDetails(tableDTO.get(index).getKey());
			 if(fvrDetail != null)
			 {
				
				tableDTO.get(index).setFieldDoctorNameAllocated(fvrDetail.getAllocationTo().getValue());
				tableDTO.get(index).setDateAndTimeOfAllocation(fvrDetail.getCreatedDate());
				//tableDTO.get(index).setContactNoOftheDoctor(fvrDetail.get);
				//tableDTO.get(index).setContactNoOftheDoctor(getDocMobileNo(fvrDetail.getRepresentativeCode()));
			 }
			 
			 if (null != fvrDetail)
			 {
			 
			 if(null != fvrDetail.getRepresentativeCode())
			 {
								 
			 TmpFvR fvrContactsDetail = getDocMobileNo(fvrDetail.getRepresentativeCode());
			 if(null != fvrContactsDetail)
			 {
				tableDTO.get(index).setContactNoOftheDoctor(null != fvrContactsDetail.getMobileNumber() ? fvrContactsDetail.getMobileNumber().toString() : "");
			 }
			 }
			 }

	}
		return tableDTO;
			
	}*/
	
	/*private Hospitals getHospitalDetail(Long hospitalId){

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
	}*/
	
	  /*private FieldVisitRequest getFvrDetails(Long intimationKey)
		        {
					
				Query findByIntimationKey = entityManager.createNamedQuery(
						"FieldVisitRequest.findByIntimationKey").setParameter("intimationKey", intimationKey);
				
				FieldVisitRequest fvrDetail;
				try{
					
					fvrDetail = (FieldVisitRequest) findByIntimationKey.getSingleResult();
					return fvrDetail;
					//List<FieldVisitRequest> fvrDetail = (List<FieldVisitRequest>) findByIntimationKey.getResultList();
				
					//if(!fvrDetail.isEmpty())
					//return fvrDetail.get(fvrDetail.size()-1);
				
				}catch(Exception e)
				{
					return null;
					
				}
			
				
			
		}*/
	  
	  /*private TmpFvR getDocMobileNo(String representativeCode)
	  {
		  
		  Query findByCode = entityManager.createNamedQuery(
					"TmpFvR.findByCode").setParameter("code", representativeCode);
			
		  TmpFvR fvrContactsDetail;
			try{
				
				fvrContactsDetail = (TmpFvR) findByCode.getSingleResult();
				return fvrContactsDetail;
				
			}catch(Exception e)
			{
				return null;
				
			}
		  
	  }*/
	  
	  
	 /* public Preauth getLatestPreauth(Long claimKey){
			
			
			Query query = entityManager.createNamedQuery("Preauth.findLatestPreauthByClaim");
			query.setParameter("claimkey", claimKey);
			@SuppressWarnings("unchecked")
			List<Preauth> resultList = (List<Preauth>) query.getResultList();
			
			if(resultList != null && ! resultList.isEmpty()){
				return resultList.get(0);
			}
			
			return null;
		}*/
	  
	  
	  
		


	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

}
