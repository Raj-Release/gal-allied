

/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.fss.ChequeDetails;
import com.shaic.domain.fss.FileStorage;

/**
 * 
 *
 * 
 */
@Stateless
public class SearchDataEntryService extends AbstractDAO<FileStorage>{

	
	@PersistenceContext
	protected EntityManager entityManager;
	public SearchDataEntryService(){
		super();
	}
	public  Page<SearchDataEntryTableDTO> search(
			SearchDataEntryFormDTO searchFormDTO,
			String userName, String passWord) {
		
		List<FileStorage> list1 = new ArrayList<FileStorage>();
		List<Long> list2 = new ArrayList<Long>();
		try{
		String claimNo = null != searchFormDTO.getClaimNo() && !searchFormDTO.getClaimNo().isEmpty() ? searchFormDTO.getClaimNo() :null;
		String patientName = null != searchFormDTO.getPatientName()&& !searchFormDTO.getPatientName().isEmpty() ? searchFormDTO.getPatientName() : null;
		String chequeNo = null != searchFormDTO.getChequeNo()&& !searchFormDTO.getChequeNo().isEmpty() ? searchFormDTO.getChequeNo() : null;
		
		if(chequeNo != null){
			list2 = getFileStorageByChequeList(chequeNo);
		}
		
		//Native query
		/*	String query = "select m.* from IMS_CLS_WRH_TRN_FILE_STORAGE m inner join IMS_CLS_WRH_TRN_CHEQUE_DETAILS n on m.FILE_STORAGE_KEY = n.CHEQUE_KEY inner join MAS_WRH_CLIENT o on m.CLIENT_KEY = o.CLIENT_KEY inner join MAS_WRH_STORAGE_LOCATION p on m.STORAGE_KEY = p.STORAGE_KEY inner join MAS_WRH_RACK q on m.RACK_KEY = q.RACK_KEY inner join MAS_WRH_SHELF r on m.SHELF_KEY = r.SHELF_KEY where UPPER(m.CLAIM_NUMBER) like '%"
					+ claimNo.toUpperCase()
					+ "%' or UPPER(m.PATIENT_NAME) like '%"
					+ patientName.toUpperCase()
					+ "%' or UPPER(n.CHEQUE_NUMBER) like '%"
					+ chequeNo.toUpperCase() + "%' and m.ACTIVE_STATUS = 'Y' and ROWNUM <= 10";
			Query nativeQuery = entityManager.createNativeQuery(query);
		
		List<Object[]> objList = (List<Object[]>) nativeQuery
				.getResultList();
		List<FileStorage> list = new ArrayList<FileStorage>();
			for (Object[] obj : objList) {
				FileStorage file = new FileStorage();
				file.setKey(((BigDecimal) obj[0]).longValue());
				file.setClient((MasClient) obj[1]);
				file.setStorage((MasStorageLocation) obj[2]);
				file.setRack((MasRack) obj[3]);
				file.setShelf((MasShelf) obj[4]);
				file.setClaimId((String) obj[5]);
				file.setYear(((BigDecimal) obj[6]).intValue());
				file.setPatientName((String) obj[7]);
				file.setAlmirahNo((String) obj[8]);
				file.setAddlShelfNo(((BigDecimal) obj[9]).longValue());
				file.setInOutFlag((String) obj[10]);
				file.setRejectFlag((String) obj[11]);
				file.setRemarks((String) obj[12]);
				file.setCreatedBy((String) obj[13]);
				file.setCreatedDate((Timestamp) obj[14]);
				file.setModifiedBy((String) obj[15]);
				file.setModifiedDate((Timestamp) obj[16]);
				file.setActivateStatus((String) obj[17]);

				list.add(file);
			}*/
		
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<FileStorage> criteriaQuery = criteriaBuilder.createQuery(FileStorage.class);
		
		Root<FileStorage> root = criteriaQuery.from(FileStorage.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		
		if(null != chequeNo){
				Expression<Long> exp = root.get("key");
				Predicate predicate = exp.in(list2);
				//Predicate condition3 = criteriaBuilder.in(arg0)(root.<Long>get("key"), "%"+chequeNo+"%");
				conditionList.add(predicate);	
		}
		if(null != claimNo){
		Predicate condition1 = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("claimId")), "%"+claimNo.toUpperCase()+"%");
		conditionList.add(condition1);
		}
		if(null != patientName){
		Predicate condition2 = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("patientName")), "%"+patientName.toUpperCase()+"%");
		conditionList.add(condition2);
		}
		
		
		Boolean isFilterAvailable = true;
		
		if(claimNo == null && patientName == null && chequeNo == null){
			criteriaQuery.select(root);
			isFilterAvailable = false;
			} else{
		criteriaQuery.select(root).where(
				criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));

			}
		 
		final TypedQuery<FileStorage> typedQuery = entityManager.createQuery(criteriaQuery);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		int firtResult;
		if(pageNumber > 1){
			firtResult = (pageNumber-1) *10;
		}else{
			firtResult = 1;
		}

	//	listIntimations1 = typedQuery.setFirstResult(firtResult).setMaxResults(25).getResultList();
		//if(isFilterAvailable){
	//	List<SearchRRCRequestTableDTO> searchRRCDTO = new ArrayList<SearchRRCRequestTableDTO>();
			
			//if(pageNumber <= 1)
		//	{
			
		//}
			
			List<FileStorage> listObj = null;
			
		//else{
			if(isFilterAvailable){
				list1 = typedQuery.getResultList();
				//	List<RRCRequest> listIntimations = getRevisedUniqueRecords(listIntimations1);
				//totalList =  convertDoToDto(listIntimations1);
//				listIntimationsObj = totalList;
				listObj = list1;
			}else{
				listObj = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}
			
		//	List<RRCRequest> revisedlistIntimations = getRevisedUniqueRecords(listIntimationsObj);
		//}
//		List<RRCRequest> listIntimations = getRevisedUniqueRecords(listIntimations1);

//		List<RRCRequest> listIntimations = RRCRequestRemoveDuplicate.getUniqueRecords(listIntimations1);		
		
		List<FileStorage> doList = listObj;
		List<SearchDataEntryTableDTO> tableDTO = null ;
		//List<RRCRequest> doList = uniqueList;
		tableDTO = convertDoToDto(doList);	
		
		Page<SearchDataEntryTableDTO> page = new Page<SearchDataEntryTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber+1);
		page.setHasNext(true);
		if(tableDTO != null && tableDTO.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(tableDTO);
		page.setIsDbSearch(true);
		
		if(null != doList && !doList.isEmpty())
		{
			page.setTotalList(doList);
		}
		//}
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;	
	}
	


	
	@Override
	public Class<FileStorage> getDTOClass() {
		// TODO Auto-generated method stub
		return FileStorage.class;
	} 
	
public List<SearchDataEntryTableDTO> convertDoToDto(List<FileStorage> doList){
	
	List<SearchDataEntryTableDTO> tableDTOList = null;
	
	if(doList != null && !doList.isEmpty()) {
		SearchDataEntryMapper searchDataEntryMapper =  SearchDataEntryMapper.getInstance();
		tableDTOList = searchDataEntryMapper.getDataEntryList(doList);
	}
	
	return tableDTOList;
	
	
}

public List<Long> getFileStorageByChequeList(String chequeNo) {
	
	Query query = entityManager
			.createNamedQuery("ChequeDetails.findByChequeNumber");
	query = query.setParameter("chequeNo", chequeNo.toUpperCase());
	
	List<ChequeDetails> chequeList = (List<ChequeDetails>)query.getResultList();
	List<Long> keyList = new ArrayList<Long>();
	//For empty list
	keyList.add(0L);
	if(chequeList != null && !chequeList.isEmpty()){
		for (ChequeDetails value : chequeList) {
			keyList.add(value.getFileStorage().getKey());
		}
		
	}
	return keyList;
	

}
}