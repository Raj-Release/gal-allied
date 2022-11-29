package com.shaic.claim.medical.opinion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.OpinionValidation;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class OpinionValidationService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	

	public Page<OpinionValidationTableDTO> search(
			OpinionValidationFormDTO searchFormDTO, BeanItemContainer<SelectValue> doctorNames) {
		List<OpinionValidation> listIntimations = new ArrayList<OpinionValidation>(); 
		try{
			
		
		String intimationNo = searchFormDTO.getIntimationNo() != null ?searchFormDTO.getIntimationNo() : null;
		String employeeName = searchFormDTO.getEmployeeName() != null ?searchFormDTO.getEmployeeName() : null;
		Date activityFromDate = searchFormDTO.getActivityFromDate() != null ?searchFormDTO.getActivityFromDate() : null;
		Date activityToDate = searchFormDTO.getActivityToDate() != null ?searchFormDTO.getActivityToDate() : null;
		String source = searchFormDTO.getSource() != null ? searchFormDTO.getSource().getValue() : null;
		
		List<Long> listOfCPUs = new ArrayList<Long>();
		
		if(searchFormDTO.getCpuCodeMulti() != null){
			String cpuSearch = searchFormDTO.getCpuCodeMulti().toString();
			if(!cpuSearch.equals("[]")){
				String temp[] = cpuSearch.split(",");
				listOfCPUs.clear();
				for (int i = 0; i < temp.length; i++) {
					String valtemp[] = temp[i].split("-");
					String val = valtemp[0].replaceAll("\\[", "");
					listOfCPUs.add(Long.valueOf(val.trim()));
				}
			}
		}
		
		List<String> docIds = new ArrayList<String>();
		
		if(searchFormDTO.getDoctorNameMulti() != null) {
			String[] selectedIds = null;
			String docNameSearch = searchFormDTO.getDoctorNameMulti().toString().substring(1, searchFormDTO.getDoctorNameMulti().toString().length() - 1);
			if (docNameSearch != null && StringUtils.isNotBlank(docNameSearch)) {
				selectedIds = docNameSearch.split(",");
				final List<SelectValue> docNames = (List<SelectValue>) doctorNames.getItemIds();
				if (selectedIds != null && selectedIds.length > 0) { 
					for (String name : selectedIds) {
						for (SelectValue value : docNames) {
							if (value.getValue().equalsIgnoreCase(name.trim())) {
								docIds.add(value.getCommonValue());
							}
						}
					}
				}
			}
		}
		
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OpinionValidation> criteriaQuery = criteriaBuilder.createQuery(OpinionValidation.class);
		
		
		Root<OpinionValidation> root = criteriaQuery.from(OpinionValidation.class);
 		List<Predicate> conditionList = new ArrayList<Predicate>();
 		
 		Predicate pendingOpinionCriteria = root.<Long>get("opinionStatus").in(SHAConstants.OPINION_STATUS_PENDING);
 		conditionList.add(pendingOpinionCriteria);
 		
 		  String empId = null;
		     if(employeeName !=null){
		    	 empId = employeeName.split("\\ ")[0];
		     }
		     
 		if (employeeName != null) {
 			Predicate employeeNameCriteria = criteriaBuilder.equal(root.<String>get("assignedDocName"), empId); 
 			conditionList.add(employeeNameCriteria);
 		}
 		
 		if (activityFromDate != null && activityToDate != null) {

 			Date fromDate = activityFromDate;
 			Calendar c = Calendar.getInstance();
			c.setTime(activityFromDate);
			c.set(Calendar.HOUR_OF_DAY, c.getMinimum(Calendar.HOUR_OF_DAY));
			c.set(Calendar.MINUTE, c.getMinimum(Calendar.MINUTE));
			c.set(Calendar.SECOND, c.getMinimum(Calendar.SECOND));
			c.set(Calendar.MILLISECOND, c.getMinimum(Calendar.MILLISECOND));
			fromDate = c.getTime();
 			Predicate dateFromCriteria = criteriaBuilder.greaterThan(root.<Date>get("assignedDate"), fromDate);
			conditionList.add(dateFromCriteria);
			
			Date toDate = activityToDate;
			c.setTime(activityToDate);
			c.set(Calendar.HOUR_OF_DAY, c.getMaximum(Calendar.HOUR_OF_DAY));
			c.set(Calendar.MINUTE, c.getMaximum(Calendar.MINUTE));
			c.set(Calendar.SECOND, c.getMaximum(Calendar.SECOND));
			c.set(Calendar.MILLISECOND, c.getMaximum(Calendar.MILLISECOND));
			toDate = c.getTime();
			Predicate dateToCriteria = criteriaBuilder.lessThan(root.<Date>get("assignedDate"), toDate);
			conditionList.add(dateToCriteria);
 		}
				
		if (intimationNo != null) {
			Predicate intimationCriteria = criteriaBuilder.like(root.<String>get("intimationNumber"), "%"+intimationNo+"%");
			conditionList.add(intimationCriteria);
		}
		
		if (source != null) {
			Predicate sourceCriteria = criteriaBuilder.like(root.<String>get("claimStage"), "%"+source+"%");
			conditionList.add(sourceCriteria);
		}
		
		if (listOfCPUs != null && listOfCPUs.size() > 0) {
			Predicate cpuNameCriteria =  root.<Long> get("cpuCode").in(listOfCPUs);
			conditionList.add(cpuNameCriteria);
		}
		
		if (docIds != null && docIds.size() > 0) {
			Predicate docNameCriteria =  root.<Long> get("updatedBy").in(docIds);
			conditionList.add(docNameCriteria);
		}
		
		criteriaQuery.select(root).where(conditionList.toArray(new Predicate[] {}));
		final TypedQuery<OpinionValidation> typedQuery = entityManager.createQuery(criteriaQuery);
		
		listIntimations = typedQuery.getResultList();
				
	    List<OpinionValidation> doList = listIntimations;

		List<OpinionValidationTableDTO> tableDTO = OpinionValidationMapper.getInstance().getOpinionValidationTableObjects(doList);
		tableDTO = getUpdatedBy(tableDTO);       
		List<OpinionValidationTableDTO> result = new ArrayList<OpinionValidationTableDTO>();
		result.addAll(tableDTO);
		Page<OpinionValidationTableDTO> page = new Page<OpinionValidationTableDTO>();
		page.setPageItems(result);
		page.setIsDbSearch(true);
		return page;
	  }
		catch (Exception e) 
	   {
	     e.printStackTrace();
	   }
	   return null;		
	}


	public void submitStatus(HashMap<Long, Boolean> opinionStatusMap, HashMap<Long, String> opinionRemarksMap, String userName) {
		if (opinionStatusMap != null && userName != null) { 
			for (Entry<Long, Boolean> entry : opinionStatusMap.entrySet()) {
			    OpinionValidation opinionTable = getOpinionObject(entry.getKey());
			    if (opinionTable != null) {
			    	if (entry.getValue() == Boolean.TRUE) {
			    		opinionTable.setOpinionStatus(SHAConstants.OPINION_STATUS_APPROVE);
			    	} else {
			    		opinionTable.setOpinionStatus(SHAConstants.OPINION_STATUS_REJECT);
			    	}
				     if(userName !=null){
				    	 userName = userName.split("\\ ")[0];
				     }
			    	opinionTable.setModifiedBy(userName.toUpperCase());
//			    	opinionTable.setUpdatedBy(userName.toUpperCase());
			    	opinionTable.setModifiedDateTime(new Timestamp(System.currentTimeMillis()));
//			    	opinionTable.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
			    	opinionTable.setOpinionStatusDate(new Timestamp(System.currentTimeMillis()));
			    	if (opinionRemarksMap != null && opinionRemarksMap.size() > 0) {
			    		String remarks = opinionRemarksMap.get(entry.getKey());
			    		opinionTable.setApproveRejectRemarks(remarks);
			    	}
			    	entityManager.merge(opinionTable);
			    }
			}
		}
	}
	
	public OpinionValidation getOpinionObject(Long opinionKey) {
		TypedQuery<OpinionValidation> query = entityManager.createNamedQuery("OpinionValidation.findByKey", OpinionValidation.class);
		query.setParameter("primaryKey", opinionKey);
		List<OpinionValidation>	resultList = query.getResultList();
		if (null != resultList && !resultList.isEmpty()) {
			return resultList.get(0);
		} 
		return null;
	}

	public Page<OpinionValidationTableDTO> fetchCompletedCases(OpinionValidationFormDTO searchFormDTO) {
		List<OpinionValidation> opinionValidations = new ArrayList<OpinionValidation>(); 
		try {
			String employeeName = searchFormDTO.getEmployeeName() != null ?searchFormDTO.getEmployeeName().toUpperCase() : null;
			Date fromDate = searchFormDTO.getFromDate() != null ? searchFormDTO.getFromDate() : null;
			Date toDate = searchFormDTO.getToDate() != null ? searchFormDTO.getToDate() : null;
			Long statusCode  = searchFormDTO.getStatus();
		
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<OpinionValidation> criteriaQuery = criteriaBuilder.createQuery(OpinionValidation.class);
		
			Root<OpinionValidation> root = criteriaQuery.from(OpinionValidation.class);
			List<Predicate> conditionList = new ArrayList<Predicate>();
			 String empId = null;
		     if(employeeName !=null){
		    	 empId = employeeName.split("\\ ")[0];
		     }
			if (employeeName != null) {
				Predicate employeeNameCriteria = criteriaBuilder.equal(root.<String>get("assignedDocName"), empId);
				conditionList.add(employeeNameCriteria);
			}
			
			Predicate opinionStatusCriteria = criteriaBuilder.notEqual(root.<String>get("opinionStatus"), SHAConstants.OPINION_STATUS_PENDING); 
	 		conditionList.add(opinionStatusCriteria);
 		
			if (null != fromDate && null != toDate) {
				Predicate startDateCriteria = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("updatedDateTime"), fromDate);
				conditionList.add(startDateCriteria);	
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate endDateCriteria = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("updatedDateTime"), toDate);
				conditionList.add(endDateCriteria);
			}
			
			if (statusCode != null) {
				Predicate statusCriteria = criteriaBuilder.equal(root.<Long>get("opinionStatus"), statusCode); 
				conditionList.add(statusCriteria);
			}
				
			criteriaQuery.select(root).where(conditionList.toArray(new Predicate[] {}));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.<Date>get("updatedDateTime")));
			final TypedQuery<OpinionValidation> typedQuery = entityManager.createQuery(criteriaQuery);
			if (fromDate == null && toDate == null && statusCode ==  null) {
				typedQuery.setMaxResults(10);
			}
			opinionValidations = typedQuery.getResultList();
			List<OpinionValidationTableDTO> tableDTO = OpinionValidationMapper.getInstance().getOpinionValidationTableObjects(opinionValidations);
			tableDTO = getCreatedByID(tableDTO);  
			List<OpinionValidationTableDTO> result = new ArrayList<OpinionValidationTableDTO>();
			result.addAll(tableDTO);
			
			if(result != null && !result.isEmpty()) {
				for (OpinionValidationTableDTO tableDto : result) {
					tableDto.setOpinionStatusValue(getStatusByKey(tableDto.getOpinionStatus()));
				}
			}
			Page<OpinionValidationTableDTO> page = new Page<OpinionValidationTableDTO>();
			page.setPageItems(result);
			page.setIsDbSearch(true);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
	   return null;		
	}
	
	public String getStatusByKey(Long statusKey) {
		TypedQuery<Status> query = entityManager.createNamedQuery("Status.findByKey", Status.class);
		query.setParameter("statusKey", statusKey);
		List<Status> resultList = query.getResultList();
		if (null != resultList && !resultList.isEmpty()) {
			return resultList.get(0).getProcessValue();
		} 
		return null;
	}

	public Set<String> getEmployeeIdsByAssignedUser(String userName) {
		Set<String> userIds = null; 
		try {
			if (userName != null && !userName.isEmpty()){
				Query query = entityManager.createNamedQuery("OpinionValidation.findByAssignedUser");
				query = query.setParameter("assignedUserId", userName.toUpperCase());
				query = query.setParameter("status", SHAConstants.OPINION_STATUS_PENDING); //Show only pending cases
				List<String> userList = query.getResultList();
				if (userList != null && !userList.isEmpty()) {
					userIds = new HashSet<String>();
					userIds.addAll(userList);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return userIds;
	}	
	
	public TmpEmployee getEmployeeByName(String userName){
		userName = userName.toLowerCase();
		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpId");
		query.setParameter("empId", userName);
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);
		}
		return null;
	}
	
	private List<OpinionValidationTableDTO> getUpdatedBy(List<OpinionValidationTableDTO> tableDTO)
	{
		String empName = "";
		for(int index = 0; index < tableDTO.size(); index++)
		{
			if(null != tableDTO.get(index).getUpdatedBy()){
				 TmpEmployee employeeObj = getEmployeeByNameWithInactive(tableDTO.get(index).getUpdatedBy());
				 if(employeeObj != null ){
					 empName = employeeObj.getEmpId()+ "-" + employeeObj.getEmpFirstName();
					 tableDTO.get(index).setUpdatedBy(empName);
				 }
			}
		}
		return tableDTO;
	}
	
	private List<OpinionValidationTableDTO> getCreatedByID(List<OpinionValidationTableDTO> tableDTO)
	{
		String empName = "";
		for(int index = 0; index < tableDTO.size(); index++)
		{
			if(null != tableDTO.get(index).getCreatedBy()){
				 TmpEmployee employeeObj = getEmployeeByNameWithInactive(tableDTO.get(index).getCreatedBy());
				 if(employeeObj != null){
					 empName = employeeObj.getEmpId()+ "-" + employeeObj.getEmpFirstName();
					 tableDTO.get(index).setCreatedBy(empName);
				 }
			}
		}
		return tableDTO;                                   
	}
	public List<OpinionValidation> getOpinionObjectBYIntimationNO(String intimationNO) {
		TypedQuery<OpinionValidation> query = entityManager.createNamedQuery("OpinionValidation.findByIntimationNo", OpinionValidation.class);
		query.setParameter("intimationNo", intimationNO);
		List<OpinionValidation>	resultList = query.getResultList();
		if (null != resultList && !resultList.isEmpty()) {
			return resultList;
		} 
		return null;
	}
	
	public TmpEmployee getEmployeeByNameWithInactive(String userName){
		userName = userName.toLowerCase();
		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpIdWithInactive");
		query.setParameter("empId", userName);
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);
		}
		return null;
	}
}
