package com.shaic.claim.userproduct.document;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import oracle.sql.DATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasAreaCodeMapping;
import com.shaic.domain.MasAreaOrgUserCpuMapping;
import com.shaic.domain.MasOrgCpuMapping;
import com.shaic.domain.MasRoleLimit;
import com.shaic.domain.MasUser;
import com.shaic.domain.MasUserAutoAllocation;
import com.shaic.domain.MasUserLimitMapping;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UserToOrgMapping;
import com.vaadin.v7.data.util.BeanItemContainer;


@Stateless
public class UserProductMappingService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(UserProductMappingService.class);
	
	
	public void updateUserProductMapping(ProductAndDocumentTypeDTO userProductDTO,String userName) {
		
		
		List<ProductDocTypeDTO> productDocumentDetails = userProductDTO.getProductsList();
		
		
		for (ProductDocTypeDTO userProduct : productDocumentDetails) {
			if(userProduct.getEnhancementValue() != null || userProduct.getPreauthValue() != null){
		
			UserProductMapping userProductMapping = new UserProductMapping();
			
			
			MasUser empkey = getEmployeeKey(userProductDTO.getDoctorName());
			
			List<UserProductMapping> prodList = getProductList(empkey.getEmpId());
			
			List<Long> userProducts =new ArrayList<Long>();
			
			for (UserProductMapping prods : prodList) {
				if(prods.getProductKey() != null) {
					userProducts.add(prods.getProductKey());
				}
			}
			
				userProductMapping.setUserNameId(empkey.getEmpId());
				userProductMapping.setProductCode(userProduct.getProdCode());
				userProductMapping.setProductKey(userProduct.getProductTypeKey());
				if(userProduct.getEnhancementValue() != null) {
				userProductMapping.setEnhancementEligibility(userProduct.getEnhancementValue().equals(true) ? "Y":"N");
				}
				if(userProduct.getPreauthValue() != null) {
				userProductMapping.setPreauthEligibility(userProduct.getPreauthValue().equals(true) ? "Y":"N");
				}
				
//		}		
		userProductMapping.setCreatedBy(userName);
//		userProductMapping.setModifiedBy(userName);
		if(userProduct.getActiveStatus() != null) {
		userProductMapping.setActiveStatus(userProduct.getActiveStatus()); 
		}
		userProductMapping.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		
		userProductMapping.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		
		if(prodList == null || prodList.isEmpty() || !(userProducts.contains(userProduct.getProductTypeKey()))){
			entityManager.persist(userProductMapping);
			entityManager.flush();
			entityManager.clear();
		} else {
			for (UserProductMapping userProductMapping2 : prodList) {
				if(!(empkey.getEmpId().equals(userProductMapping2.getUserNameId()) && userProduct.getProdCode().equals(userProductMapping2.getProductCode()) 
						&& userProduct.getProductTypeKey().equals(userProductMapping2.getProductKey()))) {
					if(userProductMapping2.getKey() == null) {
					entityManager.persist(userProductMapping);
					entityManager.flush();
					entityManager.clear();
					}
				}
				 else if(userProduct.getProdCode().equals(userProductMapping2.getProductCode()) && userProduct.getProductTypeKey().equals(userProductMapping2.getProductKey())){
					if(userProduct.getPreauthValue() != null) {
					userProductMapping2.setPreauthEligibility(userProduct.getPreauthValue().equals(true) ? "Y":"N");
					}
					if(userProduct.getEnhancementValue() != null) {
					userProductMapping2.setEnhancementEligibility(userProduct.getEnhancementValue().equals(true) ? "Y":"N");
					}
					userProductMapping2.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(userProductMapping2);
					entityManager.flush();
					entityManager.clear();
				}
			}
				
			}
		
			
		}
		}
	}
	
	/*public void updateFlagValue(UserProductMapping userProduct) {

		String preauthFlg = userProduct.getPreauthEligibility() != null ? userProduct.getPreauthEligibility() : "N";
		String enhanceFlg = userProduct.getEnhancementEligibility() != null ? userProduct.getEnhancementEligibility() : "N";

		String query = "UPDATE MAS_USER_PRODUCT_MAPPING SET PREAUTH_ELG_FLAG = '"
				+ preauthFlg
				+ "', ENHANCE_ELG_FLAG = '"
				+ enhanceFlg
				+ "', MODIFIED_DATE = TO_TIMESTAMP('"+userProduct.getModifiedDate()+"','YYYY-MM-DD HH24:MI:SS.FF')"
				+ "where EMP_PRD_KEY = "
				+ userProduct.getKey()
				+ " and USER_ID = '"
				+ userProduct.getUserNameId() + "'";
			Query nativeQuery = entityManager.createNativeQuery(query);
			
			System.out.println(query);

			nativeQuery.executeUpdate();
	}*/
	
	
	public void updateUserAutoAllocation(
			ProductAndDocumentTypeDTO userProductDTO, String userName) {

		MasUserAutoAllocation user = getAllocationByName(userProductDTO
				.getDoctorId());
		
	
		MastersValue userTypes = null;
		if (user != null) {
			
			user.setQueueCount(userProductDTO.getQueueCount());
			user.setWindowStartTime(userProductDTO.getStartTime());
			user.setWindowEndTime(userProductDTO.getEndTime());

			SelectValue selectValue = userProductDTO.getStatus();

			if (selectValue != null) {
				MastersValue masValue = new MastersValue();

				masValue.setKey(selectValue.getId());
				masValue.setValue(selectValue.getValue());
				user.setEligibleId(masValue);
				user.setEligibleDesc(selectValue.getValue());
				if (selectValue.getValue() != null
						&& (selectValue.getValue()
								.equalsIgnoreCase(SHAConstants.MAS_NOT_ELIGIBLE))) {
					user.setEligibleFlag(SHAConstants.N_FLAG);
				} else if (selectValue.getValue().equalsIgnoreCase(
						SHAConstants.MAS_ELIGIBLE)) {
					user.setEligibleFlag(SHAConstants.YES_FLAG);
				}

			}
			
			SelectValue userSelectValue = userProductDTO.getType();
			
			if (userSelectValue != null) {

				userTypes = getMastersValue(userSelectValue.getValue(),
						ReferenceTable.AUTO_USERS);
				if (userTypes != null) {
					user.setUserType(userTypes);
				}
			}

			user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			user.setModifiedBy(userName);

			entityManager.merge(user);
			entityManager.flush();
			entityManager.clear();
		}else{
			user = new MasUserAutoAllocation();
			user.setDoctorId(userProductDTO.getDoctorId());
			user.setDocutorName(userProductDTO.getDoctorName());
			user.setQueueCount(userProductDTO.getQueueCount());
			user.setWindowStartTime(userProductDTO.getStartTime());
			user.setWindowEndTime(userProductDTO.getEndTime());

			SelectValue selectValue = userProductDTO.getStatus();

			if (selectValue != null) {
				MastersValue masValue = new MastersValue();

				masValue.setKey(selectValue.getId());
				masValue.setValue(selectValue.getValue());
				user.setEligibleId(masValue);
				user.setEligibleDesc(selectValue.getValue());
				if (selectValue.getValue() != null
						&& (selectValue.getValue()
								.equalsIgnoreCase(SHAConstants.MAS_NOT_ELIGIBLE))) {
					user.setEligibleFlag(SHAConstants.N_FLAG);
				} else if (selectValue.getValue().equalsIgnoreCase(
						SHAConstants.MAS_ELIGIBLE)) {
					user.setEligibleFlag(SHAConstants.YES_FLAG);
				}
	
			}
			
			SelectValue userSelectValue = userProductDTO.getType();
			
			if (userSelectValue != null) {

				userTypes = getMastersValue(userSelectValue.getValue(),
						ReferenceTable.AUTO_USERS);
				if (userTypes != null) {
					user.setUserType(userTypes);
				}
			}

			user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			user.setCreatedBy(userName);
			entityManager.persist(user);
			log.info("------Auto Allocation User------>"+user+"<------------");
			entityManager.flush();
			entityManager.clear();
		}
	}
	
	public void updateUserCpu(ProductAndDocumentTypeDTO userProductDTO,String userName) {
		
		List<ApplicableCpuDTO> cpuDetailsDto = userProductDTO.getApplicableCpuList();
		MasUser empkey=getEmployeeKey(userProductDTO.getDoctorName());
		MasUser userTypes = getEmpById(userProductDTO.getDoctorId());
		MasUser getShiftDetails = getShiftDetailsById(userProductDTO.getDoctorId());
			SelectValue userSelectValue = userProductDTO.getType();
			
			if (userSelectValue != null) {
					if(userSelectValue.getValue().equalsIgnoreCase(SHAConstants.CPU_ALLOCATION_CORP_USER)) {
						userTypes.setCorporateUserFlag(SHAConstants.YES_FLAG);
					
				}else if(userSelectValue.getValue().equalsIgnoreCase(SHAConstants.CPU_ALLOCATION_CPU_USER)){
					userTypes.setCorporateUserFlag(SHAConstants.N_FLAG);
				}
					userTypes.setUserId(empkey.getEmpId());
					userTypes.setUserName(userProductDTO.getDoctorName());	
					
					
					
					if(userProductDTO.getHighValueClaim().equals(true) && userProductDTO.getHighValueClaim()!=null){
					userTypes.setHighValueFlag(SHAConstants.YES_FLAG);
					userTypes.setShiftStartTime(userProductDTO.getShiftStartTime());
					userTypes.setShiftEndTime(userProductDTO.getShiftEndTime());
					}else if(userProductDTO.getHighValueClaim().equals(false) && userProductDTO.getHighValueClaim()!=null) {
					userTypes.setHighValueFlag(SHAConstants.N_FLAG);
					userTypes.setShiftStartTime("");
					userTypes.setShiftEndTime("");
					}
				
					
				entityManager.merge(userTypes);
			}
			if(cpuDetailsDto!=null) {
			for (ApplicableCpuDTO applicableCpuDTO : cpuDetailsDto) {
				if(applicableCpuDTO.getAccessability()!=null) {
					
					TmpCPUCode tmpCpuCode = getCpuByCode(Long.valueOf(applicableCpuDTO.getCpuCode()));
					
					UserToOrgMapping userOrgMap = getCpuListByOrgId(tmpCpuCode.getOrgId(),empkey.getEmpId());
					
					UserToOrgMapping userMapping;
					if(userOrgMap != null){
						if(applicableCpuDTO.getAccessability() != null){
							userOrgMap.setActiveStatus(applicableCpuDTO.getAccessability() ? SHAConstants.YES_FLAG : SHAConstants.N_FLAG);
						}
						userOrgMap.setModifiedBy(userName);
						userOrgMap.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(userOrgMap);
						entityManager.flush();
						entityManager.clear();
						
					}else 
					{
					userMapping = new UserToOrgMapping();
					userMapping.setOrgId(tmpCpuCode.getOrgId());
					userMapping.setUserId(empkey.getEmpId());
					userMapping.setCreatedBy(userName);
					userMapping.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					if(applicableCpuDTO.getAccessability() != null && applicableCpuDTO.getAccessability().equals(SHAConstants.YES_FLAG)) {
						userMapping.setActiveStatus(SHAConstants.YES_FLAG); 
					}
					entityManager.persist(userMapping);
					entityManager.flush();
					entityManager.clear();
					}
				}
				}
		}
	}
	
//As per new requirement
	/*public void updateUserCpu(ProductAndDocumentTypeDTO userProductDTO,String userName) {
		
		List<ApplicableCpuDTO> cpuDetailsDto = userProductDTO.getApplicableCpuList();
		
		List<ApplicableCpuDTO> useCpuCOdes=new ArrayList<ApplicableCpuDTO>();
		
		MasUser empkey1=getEmployeeKey(userProductDTO.getDoctorName());
		
		List<UserToOrgMapping> usercpuList = getCpuList(empkey1.getEmpId());
		
		List<Long> availableCpu = new ArrayList<Long>();
		
		if(cpuDetailsDto != null) {
		for (ApplicableCpuDTO applicableCpuDTO : cpuDetailsDto) {
			if(applicableCpuDTO.getAccessability() != null) {
				UserToOrgMapping userMapping = new UserToOrgMapping();
				TmpCPUCode userCpuMapping =  new TmpCPUCode();
				userCpuMapping.setCpuCode(applicableCpuDTO.getCpuCode());
			TmpCPUCode tmpCpuCode = (TmpCPUCode) getCpuCode();
			userMapping.setCpuCode(tmpCpuCode.getDescription());
			userMapping.setUserId(empkey1.getEmpId());
			userMapping.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			userMapping.setActiveStatus(applicableCpuDTO.getActiveStatus() != null ? applicableCpuDTO.getActiveStatus():1l);
			if(usercpuList == null || usercpuList.isEmpty() || !(availableCpu.contains(applicableCpuDTO.getCpuCode()))) {
				entityManager.persist(userMapping);
				entityManager.flush();
			} else {
			for (UserCpuMapping userCpuMappingdet : usercpuList) {
				if(!(userCpuMappingdet.getUserId().equals(empkey1.getEmpId()) && userCpuMappingdet.getCpu().equals(tmpCpuCode.getDescription()))) {
					if(userCpuMappingdet.getId() == null){
					entityManager.persist(userMapping);
					}
				} else if(userCpuMappingdet.getCpu().equals(tmpCpuCode.getDescription()) && userCpuMappingdet.getCpuCode().equals(tmpCpuCode.getCpuCode())){
						userCpuMappingdet.setActiveStatus(applicableCpuDTO.getActiveStatus() != null ? applicableCpuDTO.getActiveStatus():1l);
						entityManager.merge(userCpuMappingdet);
						
				}
					
				 
				entityManager.flush();
			}
			
			}
			
			
			}
		}
		
		}
	}
	*/
	/*public void updateEmployee(ProductAndDocumentTypeDTO userProductDTO) {
		TmpEmployee empkey = getEmployeeKey(userProductDTO.getDoctorName());
		Long minAmount = userProductDTO.getMinAmount();
		Long mazAmount = userProductDTO.getMaxAmount();
		if(userProductDTO.getMaxAmount()>0 && userProductDTO.getMinAmount()>=0){
		if(empkey.getEmpId() != null) {
		empkey.setAuthminAmt(minAmount);
		empkey.setAuthmaxAmt(mazAmount);
		entityManager.merge(empkey);
		} else {
			entityManager.persist(empkey);
		}
		
		}
	}*/
	
	
	
	private MasUser getEmployeeKey(String initiatorId) {
		MasUser userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getEmpByName").setParameter("userName", initiatorId);
		try {
			userDetail = (MasUser) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}

	}
	 
	public MasUser getEmpById(String initiatorId) {
		MasUser userDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"MasUser.getEmpById").setParameter("userId", initiatorId);
		try {
			userDetail = (MasUser) findByTransactionKey.getSingleResult();
			return userDetail;
		} catch (Exception e) {
			return null;
		}

	}
	
	 @SuppressWarnings("unchecked")
		public List<TmpCPUCode> getCpuCode() {
				Query findAll = entityManager.createNamedQuery("TmpCPUCode.findByCpuCode");
				List<TmpCPUCode> cpuList = (List<TmpCPUCode>) findAll
						.getResultList();
				return cpuList;
			}
	 
	 public TmpCPUCode getCpuByCode(Long cpuCode) {
		 Query list = entityManager.createNamedQuery("TmpCPUCode.findByCode").setParameter("cpuCode", cpuCode) ;
		 List<TmpCPUCode> cpuCodeList = list.getResultList();
		 
		 if(cpuCodeList != null && !cpuCodeList.isEmpty()){
				return cpuCodeList.get(0);
			}
			 return null;
	 }
	 
	 public List<UserToOrgMapping> getCpuList(String userID) {
		 Query cpuList = entityManager.createNamedQuery("UserToOrgMapping.findByUserId")
				 .setParameter("userId", userID);
		 List<UserToOrgMapping> cpuValue = (List<UserToOrgMapping>)cpuList.getResultList();
		 return cpuValue;
	 }
	 
	 /*public List<MasOrgUserMapping> getList(String empKey) {
		 Query list = entityManager.createNamedQuery("MasOrgUserMapping.findORGForUserId").setParameter("userId", empKey);
		 List<MasOrgUserMapping> usrValues = (List<MasOrgUserMapping>)list.getResultList();
		 return usrValues;
	 }*/
	 
	 public List<MasOrgCpuMapping> getCPUList(String orgKey) {
		 Query list = entityManager.createNamedQuery("MasOrgCpuMapping.findCPUForUserId").setParameter("orgId", orgKey);
		 List<MasOrgCpuMapping> usrValues = (List<MasOrgCpuMapping>)list.getResultList();
		 return usrValues;
	 }
	 
	 public List<UserProductMapping> getProductList(String userID) {
		 Query cpuList = entityManager.createNamedQuery("UserProductMapping.findByUserId")
				 .setParameter("userId", userID);
		 List<UserProductMapping> productValue = (List<UserProductMapping>)cpuList.getResultList();
		 return productValue;
	 }
	 
	 public UserToOrgMapping getCpuListByOrgId(String orgId,String userId){
		Query list = entityManager.createNamedQuery("UserToOrgMapping.findByOrgIdUserId").setParameter("orgId", orgId).setParameter("userId", userId);
		List<UserToOrgMapping> cpuList = list.getResultList();
		if(cpuList != null && !cpuList.isEmpty()){
			return cpuList.get(0);
		}
		 return null;
		 
	 }
	 
	public MasRoleLimit getAmountDetails(String role) {

		MasRoleLimit roleLimit = null;

		role = role.toLowerCase();

		Query query = entityManager
				.createNamedQuery("MasRoleLimit.getAmtByRole");
		query.setParameter("role", role);

		List<MasRoleLimit> list = query.getResultList();
		for (MasRoleLimit roleLimit2 : list) {
			roleLimit = roleLimit2;
		}

		return roleLimit;

	}
	 
	 public MasUser getEmployeeByName(String userName){
			
		 	MasUser tmpEmployee = null;
			if(userName != null) {
			userName = userName.toLowerCase();
			
			Query query = entityManager.createNamedQuery("MasUser.findByEmpName");
			query.setParameter("userName", userName);
			
			List<MasUser> tmpEmployeeList = query.getResultList();
			if(tmpEmployeeList != null) {
			for (MasUser tmpEmployee2 : tmpEmployeeList) {
				tmpEmployee = tmpEmployee2;
			}
			
			return tmpEmployee;
			
			}
			}
			return null;
			
		}
	 
	 public MasUserAutoAllocation getAllocationByName(String userName){
			
		 	MasUserAutoAllocation doctor = null;
			
			userName = userName.toLowerCase();
			
			Query query = entityManager.createNamedQuery("MasUserAutoAllocation.findByDoctor");
			query.setParameter("doctorId", userName);
			
			List<MasUserAutoAllocation> doctorList = query.getResultList();
			for (MasUserAutoAllocation doctor1 : doctorList) {
				doctor = doctor1;
			}
			
			return doctor;
			
			
		}
	 
	 @SuppressWarnings("unchecked")
		public List<Product> getProductDetails() {
				Query findAll = entityManager.createNamedQuery("Product.findAll");
				List<Product> nonnetworkHospitalList = (List<Product>) findAll
						.getResultList();
				return nonnetworkHospitalList;
			}
	 
	 
	@SuppressWarnings("unchecked")
	public MasUserLimitMapping getRoleDetails(String userId) {

		userId = userId.toLowerCase();

		List<String> claimTypes = Arrays.asList("BOTH", "CASHLESS");
		Query findAll = entityManager
				.createNamedQuery("MasUserLimitMapping.getRoleByUser");
		findAll.setParameter("userName", userId).setParameter("typeList",
				claimTypes);

		try {
			MasUserLimitMapping userRole = (MasUserLimitMapping) findAll
					.getSingleResult();
			return userRole;
		} catch (Exception e) {
			return null;
		}
	}
	 
	 @SuppressWarnings("unchecked")
		public List<Product> getHealthProductDetails() {
				Query findAll = entityManager.createNamedQuery("Product.findHealth");
				List<Product> nonnetworkHospitalList = (List<Product>) findAll
						.getResultList();
				return nonnetworkHospitalList;
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
	 
	 public List<UserToOrgMapping> getOrgMappingByUser(String userName){
		 Query query = entityManager.createNamedQuery("UserToOrgMapping.findOrgForUserId");
		 query.setParameter("userId", userName);
		 List<UserToOrgMapping> list = null;
		 
		 list =  (List<UserToOrgMapping>)query.getResultList();
		 
		 return list;
		 
	 }
	 
	 public List<TmpCPUCode> getCPUByOrgList(List<String> orgList){
		 
		 Query query = entityManager.createNamedQuery("TmpCPUCode.findByOrg");
		 query.setParameter("orgList", orgList);
		 List<TmpCPUCode> list = null;
		 
		 list =  (List<TmpCPUCode>)query.getResultList();
		 
		 return list;
	 }
	 
	
	 
	 public MasUser getUserName(String initiatorId)
		{
		  MasUser tmpEmployeeDetails;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"MasUser.getById").setParameter("userId", initiatorId.toLowerCase());
			try{
				tmpEmployeeDetails =(MasUser) findByTransactionKey.getSingleResult();
				if(tmpEmployeeDetails != null) {
				return tmpEmployeeDetails;
				}else{
					return null;
				}
			}
			catch(Exception e)
			{
				return null;
			}								
		}
	 
	 public MasUser getShiftDetailsById(String userId) {
		 MasUser userDetail;
		 Query query = entityManager.createNamedQuery(
					"MasUser.getByType").setParameter("userId", userId);;
					try {
						userDetail = (MasUser) query.getSingleResult();
						return userDetail;
					} catch (Exception e) {
						return null;
					}
	 }
	 
	 public TmpCPUCode getMasCpuCode(Long cpuCode){
			Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
			query = query.setParameter("cpuCode", cpuCode);
			List<TmpCPUCode> listOfTmpCodes = query.getResultList();
			if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty()){
				return listOfTmpCodes.get(0);
			}
			return null;
		}
	 
	 public BeanItemContainer<SelectValue> getAreaCodeByCPUList(String cpuCode){
		 try{
			 Query query = entityManager.createNamedQuery("MasAreaCodeMapping.findByCpuCode");
			 query.setParameter("cpuCodeValue", cpuCode);
			 List<MasAreaCodeMapping> list = null;

			 list =  (List<MasAreaCodeMapping>)query.getResultList();

			 if(list != null) {
				 List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
				 BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				 SelectValue select = null;
				 for (MasAreaCodeMapping value : list) {
					 select = new SelectValue();
					 select.setId(value.getKey());
					 select.setValue(value.getAreaCode() + "-" + value.getAreaName());
					 selectValuesList.add(select);
				 }
				 mastersValueContainer.addAll(selectValuesList);
				 return mastersValueContainer;
			 }else {
				 return null; 
			 }
		 }
		 catch(Exception e) {
			 e.printStackTrace();
			 return null;
		 }
    }
	 
	 public List<MasAreaOrgUserCpuMapping> getUserAreaOrgMappingDetails(String orgId,
				String userId) {
			Query query = entityManager
					.createNamedQuery("MasAreaOrgUserCpuMapping.findByOrgIdCpu");
			query.setParameter("orgId",  orgId);
			query.setParameter("userId", userId);
			List<MasAreaOrgUserCpuMapping> tmpEmployeeList = query.getResultList();
			if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
				return tmpEmployeeList;
			}
			return tmpEmployeeList;

		}
}
