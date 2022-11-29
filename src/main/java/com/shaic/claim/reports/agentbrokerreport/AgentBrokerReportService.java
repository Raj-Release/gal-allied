package com.shaic.claim.reports.agentbrokerreport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.reports.dailyreport.DailyReportTableDTO;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AgentBrokerReportService extends AbstractDAO<Intimation> {
	
	@PersistenceContext
	protected EntityManager entityManager;
	protected Date fromDate;
	protected Date toDate;
	@Resource
	private UserTransaction utx;
	
	public AgentBrokerReportService() {
		super();
	}
	
	public  Page<AgentBrokerReportTableDTO> search(AgentBrokerReportFormDTO hospitalFormDTO,String userName, String passWord,DBCalculationService dbCalService) {
		
		//List<Intimation> listIntimations = new ArrayList<Intimation>(); 
		try
		{
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);
			utx.begin();
			 fromDate = null != hospitalFormDTO.getFromDate() ? hospitalFormDTO.getFromDate() : null;
			 toDate = null != hospitalFormDTO.getToDate() ? hospitalFormDTO.getToDate() : null;
			/*
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Intimation> criteriaQuery = criteriaBuilder.createQuery(Intimation.class);
			
			Root<Intimation> root = criteriaQuery.from(Intimation.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			if(null != fromDate && null != toDate)
			{
				Predicate condition1 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition1);	
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition2 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition2);
			}
			
			if(userName != null && !userName.isEmpty()){
				List<Long> cpuIdList = cpuMapService.getUserCpuList(userName);
				Predicate cpuCondition = root.<TmpCPUCode>get("cpuCode").<Long>get("cpuCode").in(cpuIdList);
				conditionList.add(cpuCondition);
			}
			
			
			if (fromDate == null && toDate == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			
			final TypedQuery<Intimation> typedQuery = entityManager.createQuery(criteriaQuery);
			
			int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 1;
			}
			*/
			
			int pageNumber = hospitalFormDTO.getPageable().getPageNumber();
			
			ClaimsReportService.popinReportLog(entityManager, userName, "AgentBrokerReportService",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<AgentBrokerReportTableDTO> resultList = dbCalService.getAgentBrokerList(new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()),userName);
			
			
			/*listIntimations = typedQuery.getResultList();
			
		    List<Intimation> doList = listIntimations;*/

		    
		  /*  AgentBrokerReportMapper agentBrokerReportMapper = AgentBrokerReportMapper.getInstance();
			List<AgentBrokerReportTableDTO> tableDTO = agentBrokerReportMapper.getAgentReportTableObjects(doList);

			tableDTO = getHospitalDetails(tableDTO);
		
			List<AgentBrokerReportTableDTO> result = new ArrayList<AgentBrokerReportTableDTO>();
			result.addAll(tableDTO);*/
			Page<AgentBrokerReportTableDTO> page = new Page<AgentBrokerReportTableDTO>();
			hospitalFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (resultList.isEmpty()) {
				hospitalFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(resultList);
			page.setIsDbSearch(true);

			ClaimsReportService.popinReportLog(entityManager, userName, "AgentBrokerReportService",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			utx.commit();
			return page;
		  }
			catch (Exception e) 
		   {
				ClaimsReportService.popinReportLog(entityManager, userName, "AgentBrokerReportService",new Date(),new Date(),SHAConstants.RPT_ERROR);
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
	
/*	private List<AgentBrokerReportTableDTO> getHospitalDetails(List<AgentBrokerReportTableDTO> tableDTO)
	{
		Hospitals hospitalDetail;
		for(int index = 0; index < tableDTO.size(); index++){
			hospitalDetail = getHospitalDetail(tableDTO.get(index).getHospitalId());
			if (null != hospitalDetail) {
				
				tableDTO.get(index).setHospitalName(hospitalDetail.getName());
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
	
	  /*private FieldVisitRequest getFvrDetails(Long intimationKey)     {
					
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
			
		}*/

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

}
