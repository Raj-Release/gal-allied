//package com.shaic.claim.policy.search.ui;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.ejb.Stateless;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceContextType;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import com.shaic.arch.fields.dto.AbstractDAO;
//import com.shaic.arch.table.Page;
//import com.shaic.claim.premedical.search.ProcessPreMedicalTableDTO;
//import com.shaic.domain.Claim;
//import com.shaic.domain.OrganaizationUnit;
//import com.shaic.domain.PolicyService;
//import com.vaadin.v7.ui.TextField;
//@Stateless
//public class NewSearchPolicyService extends AbstractDAO <TmpPolicy> {
//	
//	@PersistenceContext
//	protected EntityManager entityManager;
//	private PolicyService policyService ;
//	
//	List<NewSearchPolicyTableDTO> resultListForTmpPolicy = new ArrayList<NewSearchPolicyTableDTO>(); 
//
//	
//	 public NewSearchPolicyService() {
//		 
//		 super();
//		
//	}
//
//	public Page<NewSearchPolicyTableDTO> search(NewSearchPolicyFormDTO formDTO, String strUserName, String strPassword)
//	{
//		
//		if(null != formDTO)
//		{
//			
//			if(formDTO.getFromPremia())
//			{
//				//resultListForTmpPolicy = retrieveCorrespondingValuesFromPremia(formDTO);
//			}
//			else
//			{
//				resultListForTmpPolicy = retreiveDataFromDB(formDTO);
//			}
//			
//			Page<NewSearchPolicyTableDTO> page = new Page<NewSearchPolicyTableDTO>();
//			Page<TmpPolicy> pagedList = super.pagedList(formDTO.getPageable());
//			page.setPageNumber(pagedList.getPageNumber());
//			page.setPageItems(resultListForTmpPolicy);
//			page.setPagesAvailable(pagedList.getPagesAvailable());
//			return page;
//		}
//		else
//		{
//		return null;
//		}
//		
//	}
//	
//	
//	private List<NewSearchPolicyTableDTO> retreiveDataFromDB(NewSearchPolicyFormDTO formDTO)
//	{
//		String strPolicyNumber = formDTO.getPolNo();
//		String strHealthCardNumber = formDTO.getHealthCardNumber();
//		String strReceiptNumber = formDTO.getPolicyReceiptNo();
//		Long strRegisteredMobileNumber = formDTO.getRegisterdMobileNumber();
//		String strProductName = "";
//		String strProductType = "";
//		String strPolhDivnCode = "";
//		String strProposerName = formDTO.getPolAssrName();
//		String strInsuredName = formDTO.getInsuredName();
//		Date insuredDob = formDTO.getInsuredDateOfBirth();
//		
//		resultListForTmpPolicy = new ArrayList<NewSearchPolicyTableDTO>(); 
//		
//		if(null != formDTO.getProductName())
//		{
//			strProductName = formDTO.getProductName().getValue();
//		}
//		if(null != formDTO.getProductType())
//		{
//			strProductType = formDTO.getProductType().getValue();
//		}
//		if(null != formDTO.getPolhDivnCode())
//		{
//			strPolhDivnCode = formDTO.getPolhDivnCode().getValue();
//		}
//		
//		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<TmpPolicy> criteriaQuery = builder
//				.createQuery(TmpPolicy.class);
//		
//		Root<TmpPolicy> searchRoot = criteriaQuery.from(TmpPolicy.class);
//		
//		List<Predicate> predicates = new ArrayList<Predicate>();
//
//		
//		if (null != strPolicyNumber && !("").equals(strPolicyNumber)) {
//			Predicate policyNumberPredicate = builder.like(
//					searchRoot.<String> get("polNo"), "%"
//							+ strPolicyNumber + "%");
//			predicates.add(policyNumberPredicate);
//		}
//		
//		if (null != strReceiptNumber && !("").equals(strReceiptNumber) ) {
//			Predicate receiptNumberPredicate = builder.like(
//					searchRoot.<String> get("polReceiptNo"), "%"
//							+ strReceiptNumber + "%");
//			predicates.add(receiptNumberPredicate);
//		}
//		
//		if (null != strRegisteredMobileNumber && !("").equals(strRegisteredMobileNumber) ) {
//			Predicate mobileNoPredicate = builder.equal(
//					searchRoot.<Long> get("registerdMobileNumber"),  strRegisteredMobileNumber );
//			predicates.add(mobileNoPredicate);
//		}
//		
//		if(null != strProductName && !("").equals(strProductName) )
//		{
//			Predicate productNamePredicate =  builder.like(
//					searchRoot.<String> get("productName"), "%"
//							+ strProductName + "%");
//			predicates.add(productNamePredicate);
//			
//		}
//	
//		if(null != strProductType && !("").equals(strProductType) )
//		{
//			Predicate productTypePredicate =  builder.like(
//					searchRoot.<String> get("productTypeId"), "%"
//							+ strProductType + "%");
//			predicates.add(productTypePredicate);
//		}
//		
//		if(null != strProposerName && !("").equals(strProposerName) )
//		{
//			Predicate proposerNamePredicate =  builder.like(
//					searchRoot.<String> get("polAssrName"), "%"
//							+ strProposerName + "%");
//			predicates.add(proposerNamePredicate);
//		}
//		
//		criteriaQuery.select(searchRoot).where(
//				builder.and(predicates.toArray(new Predicate[] {})));
//		
//		final TypedQuery<TmpPolicy> searchTmpPolicyQuery = entityManager
//				.createQuery(criteriaQuery);
//		
//		List<TmpPolicy> pageItemList  = searchTmpPolicyQuery.getResultList();
//		
//		resultListForTmpPolicy = SearchPolicyMapper.getTmpPolicyDetails(pageItemList);
//		
//		final CriteriaBuilder insuredBuilder = entityManager.getCriteriaBuilder();
//		final CriteriaQuery<TmpInsured> insuredCriteriaQuery = insuredBuilder
//				.createQuery(TmpInsured.class);
//		
//		Root<TmpInsured> searchRootForTmpInsured = insuredCriteriaQuery.from(TmpInsured.class);
//		
//		List<Predicate> predicateForInsured = new ArrayList<Predicate>();
//
//		List<TmpInsured> resultListForTmpInsured = new ArrayList<TmpInsured>();
//		
//		if (null != strPolicyNumber && !("").equals(strPolicyNumber) ) {
//			Predicate policyNumberPredicate = builder.like(
//					searchRootForTmpInsured.<String> get("policyNo"), "%"
//							+ strPolicyNumber + "%");
//			predicates.add(policyNumberPredicate);
//		}
//		
//		if (null != strHealthCardNumber && !("").equals(strHealthCardNumber) ) {
//			Predicate healthCardNoPredicate = builder.like(
//					searchRootForTmpInsured.<String> get("healthCardNumber"), "%"
//							+ strHealthCardNumber + "%");
//			predicates.add(healthCardNoPredicate);
//		}
//		if (null != strInsuredName && !("").equals(strInsuredName) ) {
//			Predicate insuredNamePredicate = builder.like(
//					searchRootForTmpInsured.<String> get("insuredName"), "%"
//							+ strInsuredName + "%");
//			predicates.add(insuredNamePredicate);
//		}
//		
//		if (null != insuredDob && !("").equals(insuredDob)) {
//			Predicate insuredDobpredicate = builder.equal(
//					searchRootForTmpInsured.<String> get("insuredDateOfBirth"), 
//					insuredDob );
//			predicates.add(insuredDobpredicate);
//		}
//		
//		insuredCriteriaQuery.select(searchRootForTmpInsured).where(
//				builder.and(predicateForInsured.toArray(new Predicate[] {})));
//		final TypedQuery<TmpInsured> searchTmpInsuredQuery = entityManager
//				.createQuery(insuredCriteriaQuery);
//		resultListForTmpInsured  = searchTmpInsuredQuery.getResultList();		
//		List<NewSearchPolicyTableDTO> resultListForInsured = SearchPolicyMapper.getTmpInsuredDetails(resultListForTmpInsured);
//
//		for(NewSearchPolicyTableDTO objDTOForTmpPolicy : resultListForTmpPolicy)
//		{
//			if(("").equals(strPolhDivnCode))
//			{
//				strPolhDivnCode = objDTOForTmpPolicy.getTmpPolIssueCode();
//			}
//			objDTOForTmpPolicy.setInsuredOffice(getInsuredOfficeName(strPolhDivnCode));
//			for(NewSearchPolicyTableDTO objDTOTmpInsured :  resultListForInsured )
//			{
//				if(objDTOForTmpPolicy.getPolNo().equals(objDTOTmpInsured.getTmpInsuredPolNo()))
//				{
//					objDTOForTmpPolicy.setHealthCardNo(objDTOTmpInsured.getHealthCardNo());
//					objDTOForTmpPolicy.setInsuredName(objDTOTmpInsured.getInsuredName());
//				}
//				
//			}	
//		}
//		return resultListForTmpPolicy;
//	}
//	
//	private String getInsuredOfficeName(String strPolIssueOfficeCode)
//	{
//		String strOfficeName = "";
//		OrganaizationUnit organizationUnit = getPolicyServiceInstance().getInsuredOfficeNameByDivisionCode(entityManager, strPolIssueOfficeCode);
//   	  	if(null != organizationUnit)
//   	  	{
//   	  		strOfficeName =organizationUnit.getOrganizationUnitName();
//   	  	}	
//   	  	return strOfficeName;
//		
//	}
//	
//	private PolicyService getPolicyServiceInstance()
//	{
//		if (null == policyService)
//		{
//			policyService = new PolicyService();
//		}
//		
//		return policyService;
//	}
//	
//	public Class<TmpPolicy> getDTOClass() {
//		// TODO Auto-generated method stub
//		return TmpPolicy.class;
//	}
//
//}
//>>>>>>> d00f1f2e641bb64012547c239acb2245a7da80f9
