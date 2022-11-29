package com.shaic.claim.pincodemapping;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.MASPincodeZoneClass;
import com.vaadin.v7.data.util.BeanItemContainer;


@Stateless
public class SearchPinCodeService  {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(SearchPinCodeService.class);
	
	public SearchPinCodeService() {
		super();
	}
	
	
	public  Page<SearchPinCodeTableDTO> search(
			SearchPinCodeDTO searchFormDTO) {
		
		try{
			
			String pinCode = null != searchFormDTO.getPinCode() && !searchFormDTO.getPinCode().isEmpty() ? searchFormDTO.getPinCode() :null;
			
			List<SearchPinCodeTableDTO> pinCodeList = null;
			
			if(pinCode != null && !pinCode.isEmpty()){
				pinCodeList = getPinCodeList(pinCode);
			}
			
			
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		Page<SearchPinCodeTableDTO> page = new Page<SearchPinCodeTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		page.setHasNext(false);
		
		if (pinCodeList != null && pinCodeList.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(pinCodeList);
		
		page.setIsDbSearch(true);
		return page;
		}
	catch(Exception e){
		e.printStackTrace();
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh"+e.getMessage()+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}
		return null;
			
	}
	
	@SuppressWarnings("unchecked")
	private List<SearchPinCodeTableDTO> getPinCodeList(String code) {
		
		List<SearchPinCodeTableDTO> list = null;
		try{
			String query = "select distinct a.PINCODE,a.ZONE,a.CITY_CLASS,b.PIN_STATE_NAME,b.PIN_DIST_NAME,b.PIN_CITY_NAME,b.PIN_CATG,b.PIN_FLEX_01 from MAS_PINCODE_ZONE_CLASS a ,MAS_PINCODES b where a.PINCODE=b.PIN_CODE and  a.PINCODE='"+code+"'";
			Query nativeQuery = entityManager.createNativeQuery(query);
			
			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			list = new ArrayList<SearchPinCodeTableDTO>();
			SearchPinCodeTableDTO pinCodeDto = null;
			for (Object[] obj : objList) {
				pinCodeDto = new SearchPinCodeTableDTO();
				BigDecimal pinCode = (BigDecimal)obj[0];
				pinCodeDto.setPinCode(String.valueOf(pinCode));

				pinCodeDto.setStrZone((String)obj[1]);
				pinCodeDto.setStrClass((String)obj[2]);
				pinCodeDto.setState((String)obj[3]);
				
				pinCodeDto.setDistrict((String)obj[4]);
				pinCodeDto.setCity((String)obj[5]);
				pinCodeDto.setCategory((String)obj[6]);
				pinCodeDto.setTier((String)obj[7]);
				
				BeanItemContainer<SelectValue> selectValueContainerForClass = getCityClassSelectValueContainer();
				pinCodeDto.setCityClassList(selectValueContainerForClass);
				
				BeanItemContainer<SelectValue> selectValueContainerForZone = getZoneSelectValueContainer();
				pinCodeDto.setZoneList(selectValueContainerForZone);
				pinCodeDto.setSerialNo(objList.indexOf(obj)+1);
				
				list.add(pinCodeDto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public Integer updatePincodeMaster(
			List<SearchPinCodeTableDTO> tableDTOList, String userName) {

		Integer iNoOfRecordsSentToUpdate = 0;

		if (null != tableDTOList && !tableDTOList.isEmpty()) {

			for (SearchPinCodeTableDTO tableDTO : tableDTOList) {
				if (null != tableDTO.getCheckBoxStatus()
						&& ("true").equalsIgnoreCase(tableDTO
								.getCheckBoxStatus())) {

					MASPincodeZoneClass zoneClass = getClass(Long.valueOf(tableDTO.getPinCode()));
					tableDTO.setUsername(userName);
					if (zoneClass != null) {
						iNoOfRecordsSentToUpdate = savePincodeMaster(zoneClass, tableDTO,
								iNoOfRecordsSentToUpdate);
					}
				}
			}
		}
		return iNoOfRecordsSentToUpdate;
	}
	
	private Integer savePincodeMaster(MASPincodeZoneClass pinCodeMaster,
			SearchPinCodeTableDTO tableDTO,
			Integer iNoOfRecordsSentToUpdate) {
		try {
			
			if(tableDTO.getZone() != null){
				pinCodeMaster.setZone(tableDTO.getZone().getValue());
			}
			
			if(tableDTO.getCityClass() != null){
				pinCodeMaster.setCityClass(tableDTO.getCityClass().getValue());
			}
			pinCodeMaster.setCreatedBy(tableDTO.getUsername());
			pinCodeMaster.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			
			if (null != pinCodeMaster.getPinCode()) {
				entityManager.merge(pinCodeMaster);
				entityManager.flush();
				iNoOfRecordsSentToUpdate++;
			}
		} catch (Exception e) {
			log.error("Error occured while saving cpu record" + tableDTO.getPinCode());
			e.printStackTrace();
			return iNoOfRecordsSentToUpdate;
		}
		
		return iNoOfRecordsSentToUpdate;
	}
	 
	/*private List<SearchPinCodeTableDTO> getCPUDTO(List<TmpCPUCode> cpuList) {

		List<SearchPinCodeTableDTO> tableDTOList = null;
		if (cpuList != null && !cpuList.isEmpty()) {
			tableDTOList = new ArrayList<SearchPinCodeTableDTO>();
			SearchPinCodeTableDTO dto = null;
			for (TmpCPUCode tmpCPUCode : cpuList) {
				dto = new SearchPinCodeTableDTO();
				dto.setCpuCode(tmpCPUCode.getCpuCode());
				dto.setCpuCodestr(String.valueOf(tmpCPUCode.getCpuCode()));
				dto.setCpuName(tmpCPUCode.getDescription());
				dto.setCpu(String.valueOf(tmpCPUCode.getCpuCode()) + " - " + tmpCPUCode.getDescription());
				tableDTOList.add(dto);
				dto = null;
			}
		}
		return tableDTOList;

	}*/
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getZoneSelectValueContainer() {
		
		Query query = entityManager
				.createNamedQuery("MASPincodeZoneClass.findDistinctZone");
		List<String> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		Long count = 1L;
		for (String value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(count);
			select.setValue(value);
			selectValuesList.add(select);
			count++;
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
		
		/*Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MASPincodeZoneClass.class)
										.addOrder(Order.asc("zone"))
										.setProjection(Projections.distinct(Projections.projectionList()
										.add(Projections.property("zone"), "value")))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		
		
		
		return selectValueContainer;*/
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getCityClassSelectValueContainer() {
		
		Query query = entityManager
				.createNamedQuery("MASPincodeZoneClass.findDistinctCityClass");
		List<String> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		Long count = 1L;
		for (String value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(count);
			select.setValue(value);
			selectValuesList.add(select);
			count++;
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
		
		/*Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MASPincodeZoneClass.class)
										.addOrder(Order.asc("cityClass"))
										.setProjection(Projections.distinct(Projections.projectionList()
										.add(Projections.property("cityClass"), "value")))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		
		
		
		return selectValueContainer;*/
	}
	
	private MASPincodeZoneClass getClass(Long pincode)
    {
    	Query query = entityManager.createNamedQuery("MASPincodeZoneClass.findByPinCode");
    	query = query.setParameter("pincode", pincode);
    	List<MASPincodeZoneClass> pinCodeList = query.getResultList();
    	if(null != pinCodeList && !pinCodeList.isEmpty())
    	{
    		return pinCodeList.get(0);
    	}
    	return null;
    	
    }
	
	public List<ZoneClassHistoryDto> getTableListValues(Long code) {
		List<ZoneClassHistoryDto> list = null;
		try {
			String query = "select m.* from ims_mastxn.IMS_CLS_STAGE_PINCODE m where m.PINCODE="
					+ code + " and m.ACITVE_STATUS = 'Y'";
			Query nativeQuery = entityManager.createNativeQuery(query);

			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			list = new ArrayList<ZoneClassHistoryDto>();
			ZoneClassHistoryDto pinCodeDto = null;
			for (Object[] obj : objList) {
				pinCodeDto = new ZoneClassHistoryDto();
				pinCodeDto.setCreatedBy((String) obj[5]);
				pinCodeDto.setCreatedDate(((Timestamp) obj[4]).toString());
				pinCodeDto.setRemarks((String) obj[3]);

				list.add(pinCodeDto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
