package com.shaic.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class UsertoCPUMappingService {


	@PersistenceContext
	protected EntityManager entityManager;
	
	//private final Logger log = LoggerFactory.getLogger(ReimbursementRejectionService.class);
	
	public BeanItemContainer<SelectValue> getUserCpuContainer(String userId){
		
		Query findAll = entityManager.createNamedQuery("UserCpuMapping.findCPUForUserId");
		findAll.setParameter("userId", userId);
		@SuppressWarnings("unchecked")
		List<UserCpuMapping> resultCPUCodeList = (List<UserCpuMapping>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		
		if(resultCPUCodeList != null && !resultCPUCodeList.isEmpty()){
			for (UserCpuMapping userCpuCode : resultCPUCodeList) {
				SelectValue select = new SelectValue();
				
				Query findActualCpu = entityManager.createNamedQuery("TmpCPUCode.findByCode");
				
				findActualCpu.setParameter("cpuCode", userCpuCode.getCpuCode());
				
				List<TmpCPUCode> originalCPUList = (List<TmpCPUCode>)findActualCpu.getResultList();
				
				if(originalCPUList != null && !originalCPUList.isEmpty()){
					TmpCPUCode tmpCpu = originalCPUList.get(0);
							entityManager.refresh(tmpCpu);
					select.setId(tmpCpu.getKey());
					select.setValue(tmpCpu.getCpuCode().toString()+ " - " + tmpCpu.getDescription());
				}
				
				selectValuesList.add(select);
			}	
			
		}
		else
		{
			Query finalAllCPU = entityManager.createNamedQuery("TmpCPUCode.findAll");
			@SuppressWarnings("unchecked")
			List<TmpCPUCode> actualCPUCodeList = (List<TmpCPUCode>) finalAllCPU
					.getResultList();
			//List<SelectValue> selectValuesCpuList = new ArrayList<SelectValue>();
			/*BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);*/
			if(actualCPUCodeList != null && !actualCPUCodeList.isEmpty()){
			for (TmpCPUCode cpuCode : actualCPUCodeList) {
				SelectValue select = new SelectValue();
				select.setId(cpuCode.getKey());
				select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
				selectValuesList.add(select);
			}
			}
		}
		
		
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		cpuCodeContainer.addAll(selectValuesList);
		
		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return cpuCodeContainer;
	}

/*	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getRevisedUserCpuContainer(String userId){
		
		Query findOrgforUser = entityManager.createNamedQuery("UserToOrgMapping.findOrgForUserId");
		findOrgforUser.setParameter("userId", userId);
		
		List<UserToOrgMapping> orgUserList = (List<UserToOrgMapping>)findOrgforUser.getResultList();
		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		
		if(orgUserList != null && !orgUserList.isEmpty()){
			
			for(UserToOrgMapping orgUserObj : orgUserList){
				Query findAll = entityManager.createNamedQuery("OrgToCPUMapping.findCPUMapForOrgId");
				findAll.setParameter("orgId", orgUserObj.getOrgId());		
			
			List<OrgToCPUMapping> resultCPUCodeList = (List<OrgToCPUMapping>) findAll
					.getResultList();
			
			
			if(resultCPUCodeList != null && !resultCPUCodeList.isEmpty()){
				for (OrgToCPUMapping userCpuCode : resultCPUCodeList) {
					SelectValue select = new SelectValue();
					
					Query findActualCpu = entityManager.createNamedQuery("TmpCPUCode.findByCode");
					
					findActualCpu.setParameter("cpuCode", Long.valueOf(userCpuCode.getCpuCode()));
					
					List<TmpCPUCode> originalCPUList = (List<TmpCPUCode>)findActualCpu.getResultList();
					
					if(originalCPUList != null && !originalCPUList.isEmpty()){
						TmpCPUCode tmpCpu = originalCPUList.get(0);
								entityManager.refresh(userCpuCode);
						select.setId(tmpCpu.getKey());
						select.setValue(tmpCpu.getCpuCode().toString()+ " - " + tmpCpu.getDescription());
					}
					
					selectValuesList.add(select);
				}	
			}
		  }
		}
		else
		{
			Query finalAllCPU = entityManager.createNamedQuery("TmpCPUCode.findAll");
			@SuppressWarnings("unchecked")
			List<TmpCPUCode> actualCPUCodeList = (List<TmpCPUCode>) finalAllCPU
					.getResultList();
			List<SelectValue> selectValuesCpuList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			if(actualCPUCodeList != null && !actualCPUCodeList.isEmpty()){
			for (TmpCPUCode cpuCode : actualCPUCodeList) {
				SelectValue select = new SelectValue();
				select.setId(cpuCode.getKey());
				select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
				selectValuesList.add(select);
			}
			}
		}
		
		
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		cpuCodeContainer.addAll(selectValuesList);
		
		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return cpuCodeContainer;
	}*/
	
	public BeanItemContainer<SelectValue> getRevisedUserCpuContainerWithoutDescription(String userId)
	{
		Query findOrgforUser = entityManager.createNamedQuery("UserToOrgMapping.findOrgForUserId");
		findOrgforUser.setParameter("userId", userId);
		
		List<UserToOrgMapping> orgUserList = (List<UserToOrgMapping>)findOrgforUser.getResultList();
		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		
		if(orgUserList != null && !orgUserList.isEmpty()){
			
			for(UserToOrgMapping orgUserObj : orgUserList){
				Query findAll = entityManager.createNamedQuery("OrgToCPUMapping.findCPUMapForOrgId");
				findAll.setParameter("orgId", orgUserObj.getOrgId());		
			
			List<OrgToCPUMapping> resultCPUCodeList = (List<OrgToCPUMapping>) findAll
					.getResultList();
			
			
			if(resultCPUCodeList != null && !resultCPUCodeList.isEmpty()){
				for (OrgToCPUMapping userCpuCode : resultCPUCodeList) {
					SelectValue select = new SelectValue();
					
					Query findActualCpu = entityManager.createNamedQuery("TmpCPUCode.findByCode");
					
					findActualCpu.setParameter("cpuCode", Long.valueOf(userCpuCode.getCpuCode()));
					
					List<TmpCPUCode> originalCPUList = (List<TmpCPUCode>)findActualCpu.getResultList();
					
					if(originalCPUList != null && !originalCPUList.isEmpty()){
						TmpCPUCode tmpCpu = originalCPUList.get(0);
								entityManager.refresh(userCpuCode);
						select.setId(tmpCpu.getKey());
						select.setValue(tmpCpu.getCpuCode().toString());
					}
					
					selectValuesList.add(select);
				}	
			}
		  }
		}
		else
		{
			Query finalAllCPU = entityManager.createNamedQuery("TmpCPUCode.findAll");
			@SuppressWarnings("unchecked")
			List<TmpCPUCode> actualCPUCodeList = (List<TmpCPUCode>) finalAllCPU
					.getResultList();
			/*List<SelectValue> selectValuesCpuList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);*/
			if(actualCPUCodeList != null && !actualCPUCodeList.isEmpty()){
			for (TmpCPUCode cpuCode : actualCPUCodeList) {
				SelectValue select = new SelectValue();
				select.setId(cpuCode.getKey());
				select.setValue(cpuCode.getCpuCode().toString());
				selectValuesList.add(select);
			}
			}
		}
		
		
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		cpuCodeContainer.addAll(selectValuesList);
		
		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return cpuCodeContainer;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getRevisedUserCpuContainer(String userId){
		
		Query findOrgforUser = entityManager.createNamedQuery("UserToOrgMapping.findOrgForUserId");
		findOrgforUser.setParameter("userId", userId);
		
		List<UserToOrgMapping> orgUserList = (List<UserToOrgMapping>)findOrgforUser.getResultList();
		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		
		if(orgUserList != null && !orgUserList.isEmpty()){
			
			for(UserToOrgMapping orgUserObj : orgUserList){
				Query findAll = entityManager.createNamedQuery("OrgToCPUMapping.findCPUMapForOrgId");
				findAll.setParameter("orgId", orgUserObj.getOrgId());		
			
			List<OrgToCPUMapping> resultCPUCodeList = (List<OrgToCPUMapping>) findAll
					.getResultList();
			
			
			if(resultCPUCodeList != null && !resultCPUCodeList.isEmpty()){
				for (OrgToCPUMapping userCpuCode : resultCPUCodeList) {
					SelectValue select = new SelectValue();
					
					Query findActualCpu = entityManager.createNamedQuery("TmpCPUCode.findByCode");
					
					findActualCpu.setParameter("cpuCode", Long.valueOf(userCpuCode.getCpuCode()));
					
					List<TmpCPUCode> originalCPUList = (List<TmpCPUCode>)findActualCpu.getResultList();
					
					if(originalCPUList != null && !originalCPUList.isEmpty()){
						TmpCPUCode tmpCpu = originalCPUList.get(0);
								entityManager.refresh(userCpuCode);
						select.setId(tmpCpu.getKey());
						select.setValue(tmpCpu.getCpuCode().toString()+ " - " + tmpCpu.getDescription());
					}
					
					selectValuesList.add(select);
				}	
			}
		  }
		}
		else
		{
			Query finalAllCPU = entityManager.createNamedQuery("TmpCPUCode.findAll");
			@SuppressWarnings("unchecked")
			List<TmpCPUCode> actualCPUCodeList = (List<TmpCPUCode>) finalAllCPU
					.getResultList();
			/*List<SelectValue> selectValuesCpuList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);*/
			if(actualCPUCodeList != null && !actualCPUCodeList.isEmpty()){
			for (TmpCPUCode cpuCode : actualCPUCodeList) {
				SelectValue select = new SelectValue();
				select.setId(cpuCode.getKey());
				select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
				selectValuesList.add(select);
			}
			}
		}
		
		
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		cpuCodeContainer.addAll(selectValuesList);
		
		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return cpuCodeContainer;
	}
	
	
	public BeanItemContainer<SelectValue> getUserCpuContainerWithoutDescription(String userId){
		
		Query findAll = entityManager.createNamedQuery("UserCpuMapping.findCPUForUserId");
		findAll.setParameter("userId", userId);
		@SuppressWarnings("unchecked")
		List<UserCpuMapping> resultCPUCodeList = (List<UserCpuMapping>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		
		if(resultCPUCodeList != null && !resultCPUCodeList.isEmpty()){
			for (UserCpuMapping userCpuCode : resultCPUCodeList) {
				SelectValue select = new SelectValue();
				
				Query findActualCpu = entityManager.createNamedQuery("TmpCPUCode.findByCode");
				
				findActualCpu.setParameter("cpuCode", userCpuCode.getCpuCode());
				
				List<TmpCPUCode> originalCPUList = (List<TmpCPUCode>)findActualCpu.getResultList();
				
				if(originalCPUList != null && !originalCPUList.isEmpty()){
					TmpCPUCode tmpCpu = originalCPUList.get(0);
							entityManager.refresh(tmpCpu);
					select.setId(tmpCpu.getKey());
					select.setValue(tmpCpu.getCpuCode().toString());
				}
				
				selectValuesList.add(select);
			}	
			
		}
		else
		{
			Query finalAllCPU = entityManager.createNamedQuery("TmpCPUCode.findAll");
			@SuppressWarnings("unchecked")
			List<TmpCPUCode> actualCPUCodeList = (List<TmpCPUCode>) finalAllCPU
					.getResultList();
			/*List<SelectValue> selectValuesCpuList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);*/
			if(actualCPUCodeList != null && !actualCPUCodeList.isEmpty()){
			for (TmpCPUCode cpuCode : actualCPUCodeList) {
				SelectValue select = new SelectValue();
				select.setId(cpuCode.getKey());
				select.setValue(cpuCode.getCpuCode().toString());
				selectValuesList.add(select);
			}
			}
		}
		
		
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		cpuCodeContainer.addAll(selectValuesList);
		
		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		return cpuCodeContainer;
	}




	
	public List<Long> getCPUCodeList(String userId,EntityManager em){
		this.entityManager = em;
		
		BeanItemContainer<SelectValue> cpuContainer = getRevisedUserCpuContainer(userId); // changed by Lakshmi due to Latest CPU Maping based on ims_sec_user 
		
		List<SelectValue> selectValueList = cpuContainer.getItemIds();
		
		List<Long> cpuKeyList = new ArrayList<Long>();
		
		if(selectValueList != null && !selectValueList.isEmpty()){
		
			for(SelectValue select :selectValueList){
				cpuKeyList.add(select.getId());
			}
		}
		
		return cpuKeyList;
		
	}
	
	public List<Long> getCPUCodeValueList(String userId,EntityManager em){
		this.entityManager = em;
		
		BeanItemContainer<SelectValue> cpuContainer = getUserCpuContainer(userId);
		
		List<SelectValue> selectValueList = cpuContainer.getItemIds();
		
		List<Long> cpuCodeValueList = new ArrayList<Long>();
		
		if(selectValueList != null && !selectValueList.isEmpty()){
		
			for(SelectValue select :selectValueList){
				cpuCodeValueList.add(select.getId());
			}
		}
		
		return cpuCodeValueList;
		
	}
	
	public List<Long> getCPUValueList(String userId,EntityManager em){
		this.entityManager = em;
		
		BeanItemContainer<SelectValue> cpuContainer = getUserCpuContainer(userId);
		
		List<SelectValue> selectValueList = cpuContainer.getItemIds();
		
		List<Long> cpuCodeValueList = new ArrayList<Long>();
		
		if(selectValueList != null && !selectValueList.isEmpty()){
		
			for(SelectValue select :selectValueList){
				
				String[] cpuList = select.getValue().split(" "); 
				
//				select.setValue(cpuList[0]);
				
				cpuCodeValueList.add(new Long(cpuList[0]));
			}
		}
		
		return cpuCodeValueList;
		
	}
	
	public List<Long> getUserCpuList(String userId)
 {
		Query findOrgforUser = entityManager
				.createNamedQuery("UserToOrgMapping.findOrgForUserId");
		findOrgforUser.setParameter("userId", userId);

		List<UserToOrgMapping> orgUserList = (List<UserToOrgMapping>) findOrgforUser
				.getResultList();

		List<Long> selectValuesList = new ArrayList<Long>();

		if (orgUserList != null && !orgUserList.isEmpty()) {

			for (UserToOrgMapping orgUserObj : orgUserList) {
				Query findAll = entityManager
						.createNamedQuery("OrgToCPUMapping.findCPUMapForOrgId");
				findAll.setParameter("orgId", orgUserObj.getOrgId());

				List<OrgToCPUMapping> resultCPUCodeList = (List<OrgToCPUMapping>) findAll
						.getResultList();

				if (resultCPUCodeList != null && !resultCPUCodeList.isEmpty()) {
					for (OrgToCPUMapping userCpuCode : resultCPUCodeList) {

						Query findActualCpu = entityManager
								.createNamedQuery("TmpCPUCode.findByCode");

						findActualCpu.setParameter("cpuCode",
								Long.valueOf(userCpuCode.getCpuCode()));

						List<TmpCPUCode> originalCPUList = (List<TmpCPUCode>) findActualCpu
								.getResultList();

						if (originalCPUList != null
								&& !originalCPUList.isEmpty()) {
							TmpCPUCode tmpCpu = originalCPUList.get(0);
							entityManager.refresh(userCpuCode);
							if (tmpCpu != null) {
								selectValuesList.add(tmpCpu.getCpuCode());
							}

						}

					}
				}
			}

		}
		return selectValuesList;
	}

}
