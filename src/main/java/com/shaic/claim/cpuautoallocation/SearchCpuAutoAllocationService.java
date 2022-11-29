package com.shaic.claim.cpuautoallocation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.MasCpuAutoAllocation;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.vaadin.v7.data.util.BeanItemContainer;


@Stateless
public class SearchCpuAutoAllocationService  {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(SearchCpuAutoAllocationService.class);
	
	public SearchCpuAutoAllocationService() {
		super();
	}
	
	
	public List<SearchCpuAutoAllocationTableDTO> search() {

		try {

			List<TmpCPUCode> cpuList = getCpuCode();

			List<SearchCpuAutoAllocationTableDTO> tableDTOList = getCPUDTO(cpuList);

			return tableDTOList;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
					+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		}
		return null;

	}
	
	public  Page<SearchCpuAutoAllocationTableDTO> search(
			SearchCpuAutoAllocationDTO searchFormDTO) {
		
		try{
			
			String cpuName = null != searchFormDTO.getCpuName() && !searchFormDTO.getCpuName().isEmpty() ? searchFormDTO.getCpuName() :null;
			
			List<MasCpuAutoAllocation> cpuList = null;
			
			if(cpuName != null && !cpuName.isEmpty()){
				if(cpuName.contains("-")){
					String[] split = cpuName.split("-");
					if(split != null){
						String cpuCodeStr = split[0];
						if(cpuCodeStr != null) {
							cpuCodeStr = cpuCodeStr.replaceAll("\\s","");
						}
						cpuList = getCpuAllocationList(Long.valueOf(cpuCodeStr));
					}
				}else{
					cpuList  = getCpuAllocationByName(cpuName);
				}
				
				
			}
			
			List<SearchCpuAutoAllocationTableDTO> result = null;
			
			if(cpuList != null && !cpuList.isEmpty()){
				result = new ArrayList<SearchCpuAutoAllocationTableDTO>();
				result = getCpuTableDTO(cpuList);
			}
			
			
			
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
		Page<SearchCpuAutoAllocationTableDTO> page = new Page<SearchCpuAutoAllocationTableDTO>();
		searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
		page.setHasNext(false);
		
		if (result != null && result.isEmpty()) {
			searchFormDTO.getPageable().setPageNumber(1);
		}
		page.setPageNumber(pageNumber);
		page.setPageItems(result);
		
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
	private List<TmpCPUCode> getCpuCode() {
		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAllAsc");
		List<TmpCPUCode> cpuList = (List<TmpCPUCode>) findAll.getResultList();
		return cpuList;
	}
	
	@SuppressWarnings("unchecked")
	private List<MasCpuAutoAllocation> getCpuAllocationList(Long code) {
		
		List<MasCpuAutoAllocation> cpuList = null;
		try{
		Query findAll = entityManager.createNamedQuery("MasCpuAutoAllocation.findByCode");
		findAll = findAll.setParameter("cpuCode", code);
		cpuList = (List<MasCpuAutoAllocation>) findAll.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return cpuList;
	}
	
	@SuppressWarnings("unchecked")
	private MasCpuAutoAllocation getCpuAllocation(Long code) {
		try {
			Query findAll = entityManager
					.createNamedQuery("MasCpuAutoAllocation.findByCode");
			findAll = findAll.setParameter("cpuCode", code);
			List<MasCpuAutoAllocation> cpuList = (List<MasCpuAutoAllocation>) findAll
					.getResultList();
			if (null != cpuList && !cpuList.isEmpty()) {
				return cpuList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<MasCpuAutoAllocation> getCpuAllocationByName(String name) {
		
		List<MasCpuAutoAllocation> cpuList = null;
		try{
		Query findAll = entityManager.createNamedQuery("MasCpuAutoAllocation.findByName");
		findAll = findAll.setParameter("cpuName", "%"+name.toLowerCase()+"%");
		cpuList = (List<MasCpuAutoAllocation>) findAll.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return cpuList;
	}
	 
	private List<SearchCpuAutoAllocationTableDTO> getCpuTableDTO(List<MasCpuAutoAllocation> cpuList) {

		List<SearchCpuAutoAllocationTableDTO> tableDTOList = new ArrayList<SearchCpuAutoAllocationTableDTO>();
		SearchCpuAutoAllocationTableDTO dto = null;
		for (MasCpuAutoAllocation masCpu : cpuList) {
			dto = new SearchCpuAutoAllocationTableDTO();
			dto.setCpuCode(masCpu.getCpucode());
			dto.setCpuCodestr(String.valueOf(masCpu.getCpucode()));
			dto.setCpuName(masCpu.getCpuName());
			dto.setMinAmt(masCpu.getMinAmount());
			dto.setMaxAmt(masCpu.getMaxAmount());
			
			if(masCpu.getWithinLimit() != null){
				if(masCpu.getWithinLimit().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.COMMONMASTER_YES);
					selectValue1.setValue(SHAConstants.YES);
					dto.setWithinLimit(selectValue1);
				}else if(masCpu.getWithinLimit().equalsIgnoreCase(SHAConstants.N_FLAG)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.COMMONMASTER_NO);
					selectValue1.setValue(SHAConstants.No);
					dto.setWithinLimit(selectValue1);
				}}
			
			BeanItemContainer<SelectValue> selectValueContainerForWithin = getSelectValueContainer(ReferenceTable.COMMON_VALUES);
			dto.setWithinLimitList(selectValueContainerForWithin);
			
			
			if(masCpu.getProcessCases() != null){
				
				SelectValue selectValue1 = new SelectValue();
				selectValue1.setId(masCpu.getProcessCases().getKey());
				selectValue1.setValue(masCpu.getProcessCases().getValue());
				dto.setLimitCases(selectValue1);
				}
		
			
			BeanItemContainer<SelectValue> selectValueContainerForCases = getSelectValueContainer(ReferenceTable.CPU_AUTO_CASES);
			dto.setLimitCasesList(selectValueContainerForCases);
			
			
			if(masCpu.getAboveLimit() != null){
				if(masCpu.getAboveLimit().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.COMMONMASTER_YES);
					selectValue1.setValue(SHAConstants.YES);
					dto.setAboveLimit(selectValue1);
				}else if(masCpu.getAboveLimit().equalsIgnoreCase(SHAConstants.N_FLAG)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.COMMONMASTER_NO);
					selectValue1.setValue(SHAConstants.No);
					dto.setAboveLimit(selectValue1);
				}}
			
			BeanItemContainer<SelectValue> selectValueContainerForAbove = getSelectValueContainer(ReferenceTable.COMMON_VALUES);
			dto.setAboveLimitList(selectValueContainerForAbove);
			
			if(masCpu.getProcessCorp() != null){
				if(masCpu.getProcessCorp().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.COMMONMASTER_YES);
					selectValue1.setValue(SHAConstants.YES);
					dto.setCorpOffice(selectValue1);
				}else if(masCpu.getProcessCorp().equalsIgnoreCase(SHAConstants.N_FLAG)){
					SelectValue selectValue1 = new SelectValue();
					selectValue1.setId(ReferenceTable.COMMONMASTER_NO);
					selectValue1.setValue(SHAConstants.No);
					dto.setCorpOffice(selectValue1);
				}}
			
			BeanItemContainer<SelectValue> selectValueContainerForCorp = getSelectValueContainer(ReferenceTable.COMMON_VALUES);
			dto.setCorpOfficeList(selectValueContainerForCorp);
			dto.setSerialNo(cpuList.indexOf(masCpu)+1);
			tableDTOList.add(dto);
		}
		
		
		return tableDTOList;
	}
	
	public Integer updateCpuMaster(
			List<SearchCpuAutoAllocationTableDTO> tableDTOList, String userName) {

		Integer iNoOfRecordsSentToUpdate = 0;

		if (null != tableDTOList && !tableDTOList.isEmpty()) {

			for (SearchCpuAutoAllocationTableDTO tableDTO : tableDTOList) {
				if (null != tableDTO.getCheckBoxStatus()
						&& ("true").equalsIgnoreCase(tableDTO
								.getCheckBoxStatus())) {

					MasCpuAutoAllocation cpuMaster = getCpuAllocation(tableDTO
							.getCpuCode());
					tableDTO.setUsername(userName);
					if (cpuMaster != null) {
						iNoOfRecordsSentToUpdate = saveCPUMaster(cpuMaster, tableDTO,
								iNoOfRecordsSentToUpdate);
					}
				}
			}
		}
		return iNoOfRecordsSentToUpdate;
	}
	
	private Integer saveCPUMaster(MasCpuAutoAllocation cpuMaster,
			SearchCpuAutoAllocationTableDTO tableDTO,
			Integer iNoOfRecordsSentToUpdate) {
		try {
			MastersValue aboveLimitCases = null;
			if (tableDTO.getWithinLimit() != null) {
				if (tableDTO.getWithinLimit().getId()
						.equals(ReferenceTable.COMMONMASTER_YES)) {
					cpuMaster.setWithinLimit(SHAConstants.YES_FLAG);
				} else if (tableDTO.getWithinLimit().getId()
						.equals(ReferenceTable.COMMONMASTER_NO)) {
					cpuMaster.setWithinLimit(SHAConstants.N_FLAG);
				}

			}
			if (tableDTO.getLimitCases() != null) {
				aboveLimitCases = getMastersValue(
						tableDTO.getLimitCases().getValue(),
						ReferenceTable.CPU_AUTO_CASES);
				if(aboveLimitCases != null){
					cpuMaster.setProcessCases(aboveLimitCases);	
				}
			}

			if (tableDTO.getAboveLimit() != null) {
				if (tableDTO.getAboveLimit().getId()
						.equals(ReferenceTable.COMMONMASTER_YES)) {
					cpuMaster.setAboveLimit(SHAConstants.YES_FLAG);
				} else if (tableDTO.getAboveLimit().getId()
						.equals(ReferenceTable.COMMONMASTER_NO)) {
					cpuMaster.setAboveLimit(SHAConstants.N_FLAG);
				}

			}
			if (tableDTO.getCorpOffice() != null) {
				if (tableDTO.getCorpOffice().getId()
						.equals(ReferenceTable.COMMONMASTER_YES)) {
					cpuMaster.setProcessCorp(SHAConstants.YES_FLAG);
				} else if (tableDTO.getCorpOffice().getId()
						.equals(ReferenceTable.COMMONMASTER_NO)) {
					cpuMaster.setProcessCorp(SHAConstants.N_FLAG);
				}

			}
			cpuMaster.setModifiedBy(tableDTO.getUsername());
			cpuMaster
					.setModifiedDate(new Timestamp(System.currentTimeMillis()));

			if (null != cpuMaster.getCpuKey()) {
				entityManager.merge(cpuMaster);
				entityManager.flush();
				iNoOfRecordsSentToUpdate++;
			}
		} catch (Exception e) {
			log.error("Error occured while saving cpu record" + tableDTO.getCpuName());
			e.printStackTrace();
			return iNoOfRecordsSentToUpdate;
		}
		
		return iNoOfRecordsSentToUpdate;
	}
	 
	private List<SearchCpuAutoAllocationTableDTO> getCPUDTO(List<TmpCPUCode> cpuList) {

		List<SearchCpuAutoAllocationTableDTO> tableDTOList = null;
		if (cpuList != null && !cpuList.isEmpty()) {
			tableDTOList = new ArrayList<SearchCpuAutoAllocationTableDTO>();
			SearchCpuAutoAllocationTableDTO dto = null;
			for (TmpCPUCode tmpCPUCode : cpuList) {
				dto = new SearchCpuAutoAllocationTableDTO();
				dto.setCpuCode(tmpCPUCode.getCpuCode());
				dto.setCpuCodestr(String.valueOf(tmpCPUCode.getCpuCode()));
				dto.setCpuName(tmpCPUCode.getDescription());
				dto.setCpu(String.valueOf(tmpCPUCode.getCpuCode()) + " - " + tmpCPUCode.getDescription());
				tableDTOList.add(dto);
				dto = null;
			}
		}
		return tableDTOList;

	}
	
	private MastersValue getMastersValue(String masterValue, String masterCode) {
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKeyByCodeAndValue");
		query = query.setParameter("parentKey", masterCode);
		query = query.setParameter("value", masterValue);

		List<MastersValue> masterValueList = new ArrayList<MastersValue>();
		masterValueList = query.getResultList();
		if (null != masterValueList && !masterValueList.isEmpty()) {
			return masterValueList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainer(String a_key) {
		
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MastersValue.class)
										.add(Restrictions.eq("code", a_key))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
										.add(Projections.property("key"), "id")
										.add(Projections.property("value"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		
		
		
		return selectValueContainer;
	}
	 
	
}
