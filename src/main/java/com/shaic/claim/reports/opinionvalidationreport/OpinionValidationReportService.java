package com.shaic.claim.reports.opinionvalidationreport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.Intimation;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

/**
 * @author GokulPrasath.A
 *
 */
public class OpinionValidationReportService extends AbstractDAO<Intimation> {
	@PersistenceContext
	protected EntityManager entityManager;
	protected Date fromDate;
	protected Date toDate;
	protected String role;
	protected String empName;
	protected Long opinionStatus;
	protected Long validatedStatus;
	
	@Resource
	private UserTransaction utx;
	
	public OpinionValidationReportService() {
		super();
	}
	
	public  Page<OpinionValidationReportTableDTO> search(OpinionValidationReportFormDTO searchFormDTO,String userName, String passWord, DBCalculationService dbService) {
		

		try
		{

			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);	
			utx.begin();
			
			 fromDate = null != searchFormDTO.getFromDate() ? searchFormDTO.getFromDate() : null;
		     toDate = null != searchFormDTO.getToDate() ? searchFormDTO.getToDate() : null;
		     //role = null != searchFormDTO.getRole() ? searchFormDTO.getRole() : null;
		     empName = null != searchFormDTO.getEmployeeName() ? searchFormDTO.getEmployeeName() : null;
		     if(searchFormDTO.getOpinionStatus() != null){
		    	 opinionStatus = searchFormDTO.getOpinionStatus().getId(); //-- 1 means Completed  ,2 Means Pending
		     }else{
		    	 opinionStatus = 0L;
		     }
		     if(searchFormDTO.getValidatedStatus() != null){
		    	 validatedStatus = searchFormDTO.getValidatedStatus().getId();
		     }else{
		    	 validatedStatus = 0L;
		     }
		     
		    /* String strRole = null;
		     if(role != null && !role.isEmpty() && !role.equalsIgnoreCase("[]")){
		    	 strRole = role.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\,\\ ", "\\,");
		     }*/
		     
		     String strRole = null;
		     StringBuffer multiSelectRole = new StringBuffer();
		     List<String> listOfRols = new ArrayList<String>();
		     if(searchFormDTO.getRole() != null){
		    	 Object value = searchFormDTO.getRole();
					Set<Object> listOfSelectedItems =  new HashSet<Object>();
					listOfSelectedItems.addAll((Collection)value);
					Iterator<Object> iterator = listOfSelectedItems.iterator();
					while(iterator.hasNext()){
						SelectValue next = (SelectValue)iterator.next();						
						multiSelectRole = multiSelectRole.append(next.getCommonValue()).append(",");
							
					}
				}
		     
		     String empId = null;
		     if(empName !=null){
		    	 empId = empName.split("\\ ")[0];
		     }
						
			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			
			ClaimsReportService.popinReportLog(entityManager, userName, "OpinionValidationReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<OpinionValidationReportTableDTO> resultList = dbService.getOpinionValidationReportList((null!= multiSelectRole ? String.valueOf(multiSelectRole) : null), empId, new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()),opinionStatus,validatedStatus);
			
			Page<OpinionValidationReportTableDTO> page = new Page<OpinionValidationReportTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (resultList.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(resultList);
			page.setIsDbSearch(true);
			ClaimsReportService.popinReportLog(entityManager, userName, "OpinionValidationReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			utx.commit();
			return page;
		  
		}
			catch (Exception e) 
		   {
				ClaimsReportService.popinReportLog(entityManager, userName, "OpinionValidationReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
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
	
	public String getEmployeeByName(String userName){
		
		String tmpEmployee = null;
		
		userName = userName.toLowerCase();
		
		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpId");
		query.setParameter("empId", userName);
		
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			for (TmpEmployee tmpEmployeeName : tmpEmployeeList) {
				tmpEmployee = tmpEmployeeName.getEmpFirstName();
			}
		}
		
		return tmpEmployee;
		
		
	}
	

}
