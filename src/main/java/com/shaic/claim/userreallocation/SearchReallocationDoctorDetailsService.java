package com.shaic.claim.userreallocation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.MasUser;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.UserLoginDetails;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;


@Stateless
public class SearchReallocationDoctorDetailsService  {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(SearchReallocationDoctorDetailsService.class);
	
	public SearchReallocationDoctorDetailsService() {
		super();
	}
	
	public  Page<SearchReallocationDoctorDetailsTableDTO> search(
			SearchReallocationDoctorNameDTO searchFormDTO) {
		
		try{
			
			String doctorName = null != searchFormDTO.getDoctorName() && !searchFormDTO.getDoctorName().isEmpty() ? searchFormDTO.getDoctorName() :null;
			
			DBCalculationService db = new DBCalculationService();
			MasUser user = null;
			
			if(doctorName != null){
				user = getEmployeeKey(doctorName);
			}
			
			
			List<Map<String, Object>> list = db.getUserForReallocation((user != null) ? user.getEmpId() : null);
			
			BeanItemContainer<SelectValue> selectValueContainerForStatus = getReallocationUserStatusByMaster();
			
			//MasUser fvrInitiatorDetail = getDoctorDetails(doctorName);
			//listOfClaim.add(fvrInitiatorDetail);
			
			List<SearchReallocationDoctorDetailsTableDTO> result = new ArrayList<SearchReallocationDoctorDetailsTableDTO>();
			
			for(Map<String, Object> dbObj : list){
				SearchReallocationDoctorDetailsTableDTO doctorTableDTO = new SearchReallocationDoctorDetailsTableDTO();
				doctorTableDTO.setEmpId((String)dbObj.get(SHAConstants.REALLOCATION_LOGIN_ID));
				doctorTableDTO.setDoctorName((String)dbObj.get(SHAConstants.REALLOCATION_LOGIN_NAME));
				doctorTableDTO.setLoginDate((String)dbObj.get(SHAConstants.REALLOCATION_LOGIN_DATE_TIME));
				Integer id = (Integer)dbObj.get(SHAConstants.REALLOCATION_ACTIVE_STATUS);
				
				if(id != null){
				if(id.equals(1)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.REALLOCATION_ACTIVE_STATUS);
					selectValue1.setValue(SHAConstants.REALLOCATION_ACTIVE);
					doctorTableDTO.setStatus(selectValue1);
				}else if(id.equals(0)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.REALLOCATION_HOLD_STATUS);
					selectValue1.setValue(SHAConstants.REALLOCATION_HOLD);
					doctorTableDTO.setStatus(selectValue1);
				}}
				
				doctorTableDTO.setStatusList(selectValueContainerForStatus);
				
				
				doctorTableDTO.setAssignedCount((Integer)dbObj.get(SHAConstants.REALLOCATION_TOTAL_ASSIGNED));
				doctorTableDTO.setCompletedCount((Integer)dbObj.get(SHAConstants.REALLOCATION_COMPLETED));
				doctorTableDTO.setPendingCount((Integer)dbObj.get(SHAConstants.REALLOCATION_PENDING));
				
				result.add(doctorTableDTO);
			}
			
		//List<SearchReallocationDoctorDetailsTableDTO> tableDTO = SearchReallocationDoctorDetailsMapper.getClaimDTO(listOfClaim);
		//tableDTO = SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
	
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		Page<SearchReallocationDoctorDetailsTableDTO> page = new Page<SearchReallocationDoctorDetailsTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
	
		if(result.size()<=10)
		{
			page.setHasNext(false);
		}
		else
		{
		page.setHasNext(true);
		}
		if (result.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		if (null != list) {
			page.setTotalRecords(list.size());
		}
		if (null != result) {
			page.setTotalList(result);
		}
		page.setIsDbSearch(true);
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
			
	}
	
	public Map<String, Object> updateStatusProcessing(
			List<SearchReallocationDoctorDetailsTableDTO> tableDTOList,
			String userName) {
		Map<String, Object> map = new HashMap<String, Object>();

		int itotalNoOfRecords = 0;
		int iNoOfRecordsSentToUpdate = 0;

		Boolean isError = false;

		if (null != tableDTOList && !tableDTOList.isEmpty()) {
			itotalNoOfRecords = tableDTOList.size();

			if (!isError) {
				for (SearchReallocationDoctorDetailsTableDTO tableDTO : tableDTOList) {
					if (null != tableDTO.getCheckBoxStatus()
							&& ("true").equalsIgnoreCase(tableDTO
									.getCheckBoxStatus())) {

						UserLoginDetails userLog = getUserById(tableDTO
								.getEmpId());

						if (userLog != null) {

							if (userLog.getActiveStatus() != null) {

								if (tableDTO.getStatusValue() != null) {

									if (!(userLog.getActiveStatus()
											.equals(tableDTO.getStatusValue()
													.longValue()))) {
										userLog.setActiveStatus(tableDTO
												.getStatusValue().longValue());
										userLog.setModifiedBy(userName);
										userLog.setModifiedDate(new Timestamp(
												System.currentTimeMillis()));
										iNoOfRecordsSentToUpdate++;
										if (null != userLog.getKey()) {
											entityManager.merge(userLog);
											entityManager.flush();
											entityManager.clear();
										}
									}

								}
							} else {
								if (tableDTO.getStatusValue() != null) {

									userLog.setActiveStatus(tableDTO
											.getStatusValue().longValue());
									userLog.setModifiedBy(userName);
									userLog.setModifiedDate(new Timestamp(
											System.currentTimeMillis()));
									iNoOfRecordsSentToUpdate++;
									if (null != userLog.getKey()) {
										entityManager.merge(userLog);
										entityManager.flush();
										entityManager.clear();
									}
								}

							}
						}
					}
				}
			}
		}

		map.put(SHAConstants.TOTAL_NO_REALLOCATION_RECORDS,
				String.valueOf(itotalNoOfRecords));
		map.put(SHAConstants.NO_RECORDS_SENT_TO_REALLOCATION,
				String.valueOf(iNoOfRecordsSentToUpdate));
		if (isError)
			map.put(SHAConstants.REALLOCATION_ERROR, "true");
		else
			map.put(SHAConstants.REALLOCATION_ERROR, "false");

		return map;
	}
	
	
	 private MasUser getEmployeeKey(String name)
		{
		 MasUser user;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"MasUser.getEmpByName").setParameter("userName", name);
			try{
				user =(MasUser) findByTransactionKey.getSingleResult();
				return user;
			}
			catch(Exception e)
			{
				return null;
			}
								
		}
	 
	public UserLoginDetails getUserById(String userId) {

		UserLoginDetails user = null;

		userId = userId.toLowerCase();

		Query query = entityManager.createNamedQuery("UserLoginDetails.find");
		query.setParameter("userId", userId);

		List<UserLoginDetails> userLogDetails = query.getResultList();

		if (userLogDetails != null && !userLogDetails.isEmpty()) {
			if(userLogDetails.get(0) != null)
			user = userLogDetails.get(0);
		}

		return user;

	}
	 
	 @SuppressWarnings({ "unchecked", "unused" })
		public BeanItemContainer<SelectValue> getReallocationUserStatusByMaster() {
			// Query findAll =
			// entityManager.createNamedQuery("CityTownVillage.findAll");
			Query query = entityManager
					.createNamedQuery("MastersValue.findByMasterTypeCode");
			query = query.setParameter("masterTypeCode", SHAConstants.MASTER_TYPE_CODE_REALLOCATION);
			List<MastersValue> mastersValueList = query.getResultList();
			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			for (MastersValue value : mastersValueList) {
				SelectValue select = new SelectValue();
				select.setId(value.getKey());
				select.setValue(value.getValue());
				selectValuesList.add(select);
			}
			mastersValueContainer.addAll(selectValuesList);

			return mastersValueContainer;
		}
	
}
