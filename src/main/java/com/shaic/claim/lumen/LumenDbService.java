package com.shaic.claim.lumen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
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

import org.apache.commons.lang.StringUtils;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.lumen.components.MISDocumentDTO;
import com.shaic.claim.lumen.components.MISQueryReplyDTO;
import com.shaic.claim.lumen.components.MISSubDTO;
import com.shaic.claim.lumen.create.LumenPolicySearchResultTableDTO;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.create.LumenSearchFormDTO;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.policyupload.PolicyDocumentTableDTO;
import com.shaic.claim.lumen.policyupload.PolicyFileData;
import com.shaic.claim.lumen.querytomis.DocumentAckTableDTO;
import com.shaic.claim.lumen.search.LumenSearchReqFormDTO;
import com.shaic.claim.lumen.upload.DocumentTableDTO;
import com.shaic.claim.lumen.upload.FileData;
import com.shaic.claim.policy.search.ui.PremInsuredDetails;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.LumenDetails;
import com.shaic.domain.LumenQuery;
import com.shaic.domain.LumenQueryDetails;
import com.shaic.domain.LumenQueryDocument;
import com.shaic.domain.LumenRequest;
import com.shaic.domain.LumenTrials;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.Specialist;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;

@Stateless
public class LumenDbService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	LumenTransactions lumenTrans;

	private String userId;

	@EJB
	private MasterService masterService;
	
	 @EJB
	 private PremiaPullService premiaPullService;
	 
	 @EJB
	private PolicyService policyService;

	/*private List<LumenRequestDTO> lumenResult;

	public List<LumenRequestDTO> getLumenResult() {
		return lumenResult;
	}

	public void setLumenResult(List<LumenRequestDTO> lumenResult) {
		this.lumenResult = lumenResult;
	}*/
	 
	 public Policy getPolicyByPolicyNumber(String argPolicyNumber){
		Policy obj = policyService.getPolicyByPolicyNubember(argPolicyNumber);
		return obj;
	 }

	public BeanItemContainer<SelectValue> getLumenSearchType(){
		BeanItemContainer<SelectValue> container = masterService.getMasterValueByReferenceForRRCEmployeeCredit(ReferenceTable.LUMEN_SEARCH_TYPE);
		return container;
	}

	@SuppressWarnings("static-access")
	public Page<LumenSearchResultTableDTO> getSearchResult(LumenSearchFormDTO formDTO, String userName){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Claim> claimSearchList = new ArrayList<Claim>();		
		List<LumenSearchResultTableDTO> lumenSearchResult = new ArrayList<LumenSearchResultTableDTO>();

		// Intimation Type Search
		CriteriaQuery<Claim> claimCriteriaQuery = builder.createQuery(Claim.class);
		Root<Claim> claimRoot = claimCriteriaQuery.from(Claim.class);
		if(StringUtils.isNotBlank(formDTO.getIntimationNumber())){
			Predicate searchIntimation = builder.like(claimRoot.<Intimation>get("intimation").<String>get("intimationId"),"%"+formDTO.getIntimationNumber()+"%");
			predicates.add(searchIntimation);
		}
//		Predicate claimStatus_1 = builder.notEqual(claimRoot.<Status>get("status").<Long>get("key"),ReferenceTable.FINANCIAL_SETTLED);
//		predicates.add(claimStatus_1);		
//		Predicate claimStatus_2 = builder.notEqual(claimRoot.<Status>get("status").<Long>get("key"),ReferenceTable.PAYMENT_SETTLED);
//		predicates.add(claimStatus_2);
//		Predicate claimStatus_3 = builder.notEqual(claimRoot.<Status>get("status").<Long>get("key"),ReferenceTable.CLAIM_CLOSED_STATUS);
//		predicates.add(claimStatus_3);
		List<Long> listOfClosedStatus = new ArrayList<Long>();
		//Closed Status
		listOfClosedStatus.add(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.FINANCIAL_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.CLAIM_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.BILLING_CLOSED_STATUS);
		listOfClosedStatus.add(140L);		
		listOfClosedStatus.add(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.PREAUTH_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.CREATE_ROD_CLOSED_STATUS);
		listOfClosedStatus.add(214L);
		
		//Settled Status
		listOfClosedStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfClosedStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfClosedStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);
		
		//R1219
		/*Predicate claimStatus_3 = builder.not(claimRoot.<Status>get("status").<Long>get("key").in(listOfClosedStatus));
		predicates.add(claimStatus_3);*/
		Predicate claimStatus_4 = builder.equal(claimRoot.<Long>get("lobId"),ReferenceTable.HEALTH_LOB_KEY);
		predicates.add(claimStatus_4);

		claimCriteriaQuery.select(claimRoot).where(builder.and(predicates.toArray(new Predicate[] {})));
		final TypedQuery<Claim> claimquery = entityManager.createQuery(claimCriteriaQuery);
		claimSearchList = claimquery.getResultList();

		if(claimSearchList != null && claimSearchList.size() > 0){
			lumenSearchResult = CreateLumenMapper.getInstance().getDetails(claimSearchList);
			userId = userName;
			updateOtherDetails(lumenSearchResult);
		}

		Page<LumenSearchResultTableDTO> page = new Page<LumenSearchResultTableDTO>();
		page.setPageItems(lumenSearchResult);
		page.setTotalRecords(lumenSearchResult.size());
		page.setTotalList(lumenSearchResult);
		return page;
	}
	
	public void updateOtherDetails(List<LumenSearchResultTableDTO> lumenSearchResult){
		for(LumenSearchResultTableDTO rec : lumenSearchResult){
			//setting hospital Name
			Hospitals hosRec = masterService.getHospitalDetails(rec.getHospitalNameId());
			if(hosRec != null){
				rec.setHospitalName(hosRec.getName());
			}
			// setting policy issuing office name
			OrganaizationUnit officeRec = getBranchCode(rec.getPolicy().getHomeOfficeCode());
			if(officeRec != null){
				rec.setPolicyIssuingOffice(officeRec.getOrganizationUnitName());
			}
			//setting the login id
			rec.setLoginId(userId);
			//setting the employee Name
			TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(userId.toLowerCase());
			if(null != employeeObj){
				rec.setEmpName(employeeObj.getEmpFirstName());
			}
		}
	}
	
	
	@SuppressWarnings("static-access")
	public Page<LumenPolicySearchResultTableDTO> getPolicySearchResult(LumenSearchFormDTO formDTO, String userName) throws ParseException{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Policy> policySearchList = new ArrayList<Policy>();		
		List<LumenPolicySearchResultTableDTO> lumenSearchResult = new ArrayList<LumenPolicySearchResultTableDTO>();

		CriteriaQuery<Policy> policyCriteriaQuery = builder.createQuery(Policy.class);
		Root<Policy> policyRoot = policyCriteriaQuery.from(Policy.class);
		if(StringUtils.isNotBlank(formDTO.getPolicyNumber())){
			Predicate searchIntimation = builder.equal(policyRoot.<String>get("policyNumber"), formDTO.getPolicyNumber());
			predicates.add(searchIntimation);
		}
		policyCriteriaQuery.select(policyRoot).where(builder.and(predicates.toArray(new Predicate[] {})));
		final TypedQuery<Policy> policyquery = entityManager.createQuery(policyCriteriaQuery);
		policySearchList = policyquery.getResultList();

		if(policySearchList != null && policySearchList.size() > 0){
			lumenSearchResult = CreateLumenPolicyMapper.getInstance().getDetails(policySearchList);
			userId = userName;
			updateOtherDetailsForPolicySearch(lumenSearchResult);
		}else{
			PremPolicyDetails policyDataObj = premiaPullService.fetchPolicyDetailsFromPremia(formDTO.getPolicyNumber());
			if(policyDataObj != null){
				LumenPolicySearchResultTableDTO searchResultObj = new LumenPolicySearchResultTableDTO();
				//policy key not coming from premia service hence assigning it as 0L. Don't make it as null it affect the Policy related View in lumen.
				searchResultObj.setKey(0L);
				searchResultObj.setPolicyNumber(policyDataObj.getPolicyNo());
				searchResultObj.setProposerCode(Integer.parseInt(policyDataObj.getProposerCode()));
				searchResultObj.setProposerName(policyDataObj.getProposerName());
				// no of insured coming - insuredDetails list  
				searchResultObj.setNoOfInsured(policyDataObj.getInsuredDetails().size());
				List<Insured> listOfInsured = new ArrayList<Insured>();
				Insured newInsuredObj = null;
				for(PremInsuredDetails prec: policyDataObj.getInsuredDetails()){
					newInsuredObj = new Insured();
					newInsuredObj.setInsuredName(prec.getInsuredName());
					listOfInsured.add(newInsuredObj);
				}
				searchResultObj.setPolicyType(policyDataObj.getPolicyType());
				searchResultObj.setProductType(policyDataObj.getProductName());
				searchResultObj.setListOfInsured(listOfInsured);
				searchResultObj.setProductName(policyDataObj.getProductName());
				//searchResultObj.setPolicyIssuingOffice(policyDataObj.get); not coming
				//8/25/2016 12:00:00 AM
				if(!StringUtils.isBlank(policyDataObj.getPolicyStartDate())){
					Date  fromDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a", Locale.ENGLISH).parse(policyDataObj.getPolicyStartDate());
					String dateAsString = new SimpleDateFormat("dd-M-yyyy HH:mm:ss a").format(fromDate);			
					searchResultObj.setPolicyStartDate(new SimpleDateFormat("dd-M-yyyy HH:mm:ss a").parse(dateAsString));
				}else{
					searchResultObj.setPolicyStartDate(null);
				}
			
				if(!StringUtils.isBlank(policyDataObj.getPolicyStartDate())){
					Date  toDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a", Locale.ENGLISH).parse(policyDataObj.getPolicyStartDate());
					String todateAsString = new SimpleDateFormat("dd-M-yyyy HH:mm:ss a").format(toDate);			
					searchResultObj.setPolicyEndDate(new SimpleDateFormat("dd-M-yyyy HH:mm:ss a").parse(todateAsString));
				}else{
					searchResultObj.setPolicyEndDate(null);
				}
				
				userId = userName;
				
				//setting the login id
				searchResultObj.setLoginId(userId);
				//setting the employee Name
				TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(userId.toLowerCase());
				searchResultObj.setEmpName(employeeObj.getEmpFirstName());
				
				lumenSearchResult.add(searchResultObj);
			}
		}

		Page<LumenPolicySearchResultTableDTO> page = new Page<LumenPolicySearchResultTableDTO>();
		page.setPageItems(lumenSearchResult);
		page.setTotalRecords(lumenSearchResult.size());
		page.setTotalList(lumenSearchResult);
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public void updateOtherDetailsForPolicySearch(List<LumenPolicySearchResultTableDTO> lumenSearchResult){
		for(LumenPolicySearchResultTableDTO rec : lumenSearchResult){
			// setting policy and product type
			rec.setPolicyType(rec.getSelectPolicyType().getValue());
			rec.setProductType(rec.getSelectProductType().getValue());
			
			// getting and setting insured details.
			Query query = entityManager.createNamedQuery("Insured.findByPolicykey1");
			query = query.setParameter("policykey", rec.getPolicyKey());		        
			List<Insured> insuredList  = query.getResultList();
			rec.setListOfInsured(insuredList);
			rec.setNoOfInsured(insuredList.size());

			// setting policy issuing office name
			OrganaizationUnit officeRec = getBranchCode(rec.getHomeOfficeCode());
			if(officeRec != null){
				rec.setPolicyIssuingOffice(officeRec.getOrganizationUnitName());
			}
			//setting the login id
			rec.setLoginId(userId);
			//setting the employee Name
			TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(userId.toLowerCase());
			rec.setEmpName(employeeObj.getEmpFirstName());
		}
	}

	@SuppressWarnings("unchecked")
	public OrganaizationUnit getBranchCode(String branchCode)
	{
		Query query = entityManager.createNamedQuery("OrganaizationUnit.findByBranchode");
		query = query.setParameter("parentKey", branchCode);
		List<OrganaizationUnit> orgList = query.getResultList();
		if(null != orgList && !orgList.isEmpty())
		{
			return orgList.get(0);
		}
		return null;
	}

	public String saveLumenRequest(LumenSearchResultTableDTO dtoBean)  throws Exception{
		LumenRequest lumenObj;
		try {
			lumenObj = lumenTrans.persistLumenObj(dtoBean);
			lumenObj = entityManager.find(LumenRequest.class, lumenObj.getKey());
			entityManager.refresh(lumenObj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return lumenObj.getLumenRefNumber();
	}
	
	public String savePolicyLumenRequest(LumenPolicySearchResultTableDTO dtoBean)  throws Exception{
		LumenRequest lumenObj;
		try {
			lumenObj = lumenTrans.persistPolicyLumenObj(dtoBean);
			lumenObj = entityManager.find(LumenRequest.class, lumenObj.getKey());
			entityManager.refresh(lumenObj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return lumenObj.getLumenRefNumber();
	}
	
	public String savePopupLumenRequest(LumenSearchResultTableDTO dtoBean, String screenName)  throws Exception{
		LumenRequest lumenObj;
		try {
			lumenObj = lumenTrans.persistPopupLumenObj(dtoBean, screenName);
			lumenObj = entityManager.find(LumenRequest.class, lumenObj.getKey());
			entityManager.refresh(lumenObj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return lumenObj.getLumenRefNumber();
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	public List<LumenTrialsDTO> getLumenTrailsData(Long lumenKey){
		List<LumenTrialsDTO> lumenTrialsDTOObj =  new ArrayList<LumenTrialsDTO>();
		if(lumenKey != null){
			Query query = entityManager.createNamedQuery("LumenTrials.findByLumenKey");
			query.setParameter("lumenKey", lumenKey);
			List<LumenTrials> lumenTrialsList = query.getResultList();
			lumenTrialsDTOObj = LumenTrailsMapper.getInstance().getLumenTrialDetails(lumenTrialsList);
			updateLumenTrailsDetails(lumenTrialsDTOObj);
		}
		return lumenTrialsDTOObj;
	}
	
	@SuppressWarnings("unchecked")
	public LumenTrials getLevelOneDataFromTrails(LumenRequestDTO dtoObj, String argStageName){
		List<LumenTrials> lumenTrialsList = null;
		if(dtoObj != null){
			Query query = entityManager.createNamedQuery("LumenTrials.findByLumenKeyWithStatus");
			query.setParameter("lumenKey", dtoObj.getLumenRequestKey());
			query.setParameter("lumenStatus", "Approved");
			query.setParameter("trailStage", argStageName);
			lumenTrialsList = query.getResultList();
			for(LumenTrials rec : lumenTrialsList){
				//setting the employee Name
				TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(rec.getCreatedBy().toLowerCase());
				dtoObj.setRemarksEmpName(employeeObj.getEmpFirstName());
			}
		}
		if(lumenTrialsList == null || lumenTrialsList.isEmpty()){
			return null;
		}else{
			return lumenTrialsList.get(0);
		}
	}
	
	public void updateLumenTrailsDetails(List<LumenTrialsDTO> lumenTrailsResult){
		StringBuilder sbObj = new StringBuilder();
		for(LumenTrialsDTO rec : lumenTrailsResult){
			//setting the employee Name
			TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(rec.getInitiatedBy().toLowerCase());
			sbObj.append(rec.getInitiatedBy());
			sbObj.append("-");
			sbObj.append(employeeObj.getEmpFirstName());
			rec.setInitiatedBy(sbObj.toString());
			sbObj.setLength(0);
		}
	}

	@SuppressWarnings("static-access")
	public Page<LumenRequestDTO> getLumenSearchData(LumenSearchReqFormDTO formDTO, String userName){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<LumenRequest> lumenSearchResultList = new ArrayList<LumenRequest>();		
		List<LumenRequestDTO> lumenSearchResult = new ArrayList<LumenRequestDTO>();
		List<Long> listOfCpu = new ArrayList<Long>();

		CriteriaQuery<LumenRequest> lumenCriteriaQuery = builder.createQuery(LumenRequest.class);
		Root<LumenRequest> lumenRoot = lumenCriteriaQuery.from(LumenRequest.class);

		if(StringUtils.isNotBlank(formDTO.getIntimationNumber())){
			Predicate searchByIntimation = builder.like(lumenRoot.<String>get("intimationNumber"),"%"+formDTO.getIntimationNumber()+"%");
			predicates.add(searchByIntimation);
		}
		if(StringUtils.isNotBlank(formDTO.getPolicyNumber())){
			Predicate searchByPolicyNumber = builder.equal(lumenRoot.<String>get("policyNumber"),formDTO.getPolicyNumber());
			predicates.add(searchByPolicyNumber);
		}
		if(formDTO.getCmbSource() != null && StringUtils.isNotBlank(formDTO.getCmbSource().getValue())){
			Predicate searchBySource = builder.equal(lumenRoot.<Stage>get("requestedStageId").<Long>get("key"),formDTO.getCmbSource().getId());
			predicates.add(searchBySource);
		}
		if(formDTO.getCmbStatus() != null && StringUtils.isNotBlank(formDTO.getCmbStatus().getValue())){
			Predicate searchByStatus = builder.equal(lumenRoot.<Status>get("status").<Long>get("key"),formDTO.getCmbStatus().getId());
			predicates.add(searchByStatus);
		}
//		if(formDTO.getCmbCPUOffice() != null && StringUtils.isNotBlank(formDTO.getCmbCPUOffice().getValue())){
//			Predicate searchByCPU = builder.equal(lumenRoot.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"),formDTO.getCmbCPUOffice().getId());
//			predicates.add(searchByCPU);
//		}
		if(formDTO.getCpuCodeMulti() != null){
			String cpuSearch = formDTO.getCpuCodeMulti().toString();
			if(!cpuSearch.equals("[]")){
				String temp[] = cpuSearch.split(",");
				listOfCpu.clear();
				for (int i = 0; i < temp.length; i++) {
					String valtemp[] = temp[i].split("-");
					String val = valtemp[0].replaceAll("\\[", "");
					listOfCpu.add(Long.valueOf(val.trim()));
				}
			}
			
		}
		if(listOfCpu != null && listOfCpu.size() >0){
			Predicate searchByCPU = lumenRoot.<Claim>get("claim").<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("cpuCode").in(listOfCpu);
			predicates.add(searchByCPU);
		}
		if(formDTO.getFrmDate() != null && formDTO.getToDate() != null){
			//GALAXYMAIN-9307 - Fixed the issue by converting date to string using TO_CHAR function. Issue : Date field search doesn't brng any records.
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String formatFromDate = formatter.format(formDTO.getFrmDate());
			String formatTODate = formatter.format(formDTO.getToDate());
			Expression<String> dateStringExpr = builder.function("TO_CHAR", String.class, lumenRoot.<Date>get("createdDate"), builder.literal("yyyyMMdd"));
			Predicate searchByDateRange = builder.between(dateStringExpr, formatFromDate, formatTODate);
			predicates.add(searchByDateRange);	
			//System.out.println(" Date Range "+formatFromDate+" to "+formatTODate);
		}
		
		
		Predicate searchByUserName = builder.equal(lumenRoot.<String>get("createdBy"),userName);
		predicates.add(searchByUserName);

		lumenCriteriaQuery.select(lumenRoot).where(builder.and(predicates.toArray(new Predicate[] {})));
		TypedQuery<LumenRequest> lumenquery = entityManager.createQuery(lumenCriteriaQuery);
		lumenSearchResultList = lumenquery.getResultList();

		if(lumenSearchResultList != null && lumenSearchResultList.size() > 0){
			lumenSearchResult = LumenRequestMapper.getInstance().getLumenDetails(lumenSearchResultList);
		}
		
		Page<LumenRequestDTO> page = new Page<LumenRequestDTO>();
		page.setPageItems(lumenSearchResult);
		page.setTotalRecords(lumenSearchResult.size());
		page.setTotalList(lumenSearchResult);
		return page;
	}

	public BeanItemContainer<SelectValue> getFileCategory(){
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue typeLumen = new SelectValue();
		typeLumen.setValue("Lumen");
		container.addBean(typeLumen);		
		return container;
	}

	public DocumentDetails updateDocumentTable(Object dtobean, FileData fileData){
		DocumentDetails uploadedDocObj = null;
		try {
			if(dtobean instanceof LumenSearchResultTableDTO){
				uploadedDocObj = lumenTrans.persistDocumentDetailObj((LumenSearchResultTableDTO)dtobean,fileData);
			}else if(dtobean instanceof LumenRequestDTO){
				uploadedDocObj = lumenTrans.persistDocumentDetailObj((LumenRequestDTO)dtobean,fileData);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while inserting lumen document details "+e.getMessage());
			e.printStackTrace();
		}
		return uploadedDocObj;
	}
	
	public DocumentDetails policyUpdateDocumentTable(Object dtobean, PolicyFileData fileData){
		DocumentDetails uploadedDocObj = null;
		try {
			if(dtobean instanceof LumenPolicySearchResultTableDTO){
				uploadedDocObj = lumenTrans.persistDocumentDetailObj((LumenPolicySearchResultTableDTO)dtobean,fileData);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while inserting lumen document details "+e.getMessage());
			e.printStackTrace();
		}
		return uploadedDocObj;
	}
	
	
	
	public DocumentDetails updateMISDocumentTable(LumenRequestDTO dtobean, FileData fileData){
		DocumentDetails uploadedDocObj = null;
		try {
			/*if(dtobean instanceof LumenSearchResultTableDTO){
				uploadedDocObj = lumenTrans.persistDocumentDetailObj((LumenSearchResultTableDTO)dtobean,fileData);
			}*/
			if(dtobean instanceof LumenRequestDTO){
				uploadedDocObj = lumenTrans.persistDocumentDetailObj(dtobean,fileData);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while inserting lumen document details "+e.getMessage());
			e.printStackTrace();
		}
		return uploadedDocObj;
	}

	/*@SuppressWarnings({ "unchecked", "static-access" })
	public List<DocumentTableDTO> getDocumentDetailsFromDb(String argIntimationNumber){
		List<DocumentTableDTO> documentDetailsDTOList =  null;
		List<DocumentDetails> documentDetailsList = null;
		if(!StringUtils.isBlank(argIntimationNumber)){
			Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNoWithDocType");
			query = query.setParameter("intimationNumber", argIntimationNumber);
			query = query.setParameter("documentType", "Lumen");
			documentDetailsList  = query.getResultList();
		}
		documentDetailsDTOList = DocumentDetailsMapper.getInstance().getDocumentDetails(documentDetailsList);		
		return documentDetailsDTOList;
	}*/

	@SuppressWarnings({ "static-access" })
	public List<DocumentTableDTO> getDocumentDetailsFromCache(List<DocumentDetails> argListOfDocs){
		List<DocumentTableDTO> documentDetailsDTOList =  new ArrayList<DocumentTableDTO>();
		documentDetailsDTOList = DocumentDetailsMapper.getInstance().getDocumentDetails(argListOfDocs);
		return documentDetailsDTOList;
	}
	
	@SuppressWarnings({ "static-access" })
	public List<PolicyDocumentTableDTO> getPolicyDocumentDetailsFromCache(List<DocumentDetails> argListOfDocs){
		List<PolicyDocumentTableDTO> documentDetailsDTOList =  new ArrayList<PolicyDocumentTableDTO>();
		documentDetailsDTOList = PolicyDocumentDetailsMapper.getInstance().getDocumentDetails(argListOfDocs);
		return documentDetailsDTOList;
	}
	
	public DocumentAckTableDTO prepareDocumentAckTableData(LumenQueryDetailsDTO argObj, DocumentDetails docObj){
		DocumentAckTableDTO  newAckTableDTOObj = new DocumentAckTableDTO();
		// adding details from lumen QueryDetails obj
		newAckTableDTOObj.setLumenRequest(argObj.getLumenRequest());
		newAckTableDTOObj.setLumenQuery(argObj.getLumenQuery());
		newAckTableDTOObj.setQueryRemarks(argObj.getQueryRemarks());
		newAckTableDTOObj.setReplyRemarks(argObj.getReplyRemarks());
		newAckTableDTOObj.setQueryDetailsKey(argObj.getQueryDetailsKey());
		
		// adding details from documentDetails obj
		newAckTableDTOObj.setFileName(docObj.getFileName());
		newAckTableDTOObj.setFileType(docObj.getDocumentType());
		newAckTableDTOObj.setDocumentToken(docObj.getDocumentToken());
		newAckTableDTOObj.setUploadedBy(docObj.getCreatedBy());
		newAckTableDTOObj.setSfFileName(docObj.getSfFileName());
		newAckTableDTOObj.setUploadedDate(docObj.getCreatedDate());
		newAckTableDTOObj.setDeletedFlag(docObj.getDeletedFlag());
		
		return newAckTableDTOObj;
	}

	public void removeDocumentRec(DocumentTableDTO dtobean){
		try {
			lumenTrans.deleteDocumentDetailObj(dtobean);
		} catch (Exception e) {
			System.out.println("Exception occurred while updating lumen document details "+e.getMessage());
			e.printStackTrace();
		}
	}
	// This method is for Common Upload Lumen Document popup....
	public void removeDocumentRecInCache(List<DocumentDetails> argListOfDocs, DocumentTableDTO dtobean){
		try {
			String uniquetxt = dtobean.getSfFileName();
			for(DocumentDetails rec : argListOfDocs){
				if(rec.getSfFileName().equals(uniquetxt)){
					rec.setDeletedFlag("Y");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// This method is for Policy Upload Lumen Document popup....
		public void removeDocumentRecInCache(List<DocumentDetails> argListOfDocs, PolicyDocumentTableDTO dtobean){
			try {
				String uniquetxt = dtobean.getSfFileName();
				for(DocumentDetails rec : argListOfDocs){
					if(rec.getSfFileName().equals(uniquetxt)){
						rec.setDeletedFlag("Y");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//This method is for Marking deleted record in DocumentDetails table
	public void removeDocumentRecInCache(List<DocumentDetails> argListOfDocs, DocumentAckTableDTO dtobean){
		try {
			String uniquetxt = dtobean.getSfFileName();
			for(DocumentDetails rec : argListOfDocs){
				if(rec.getSfFileName().equals(uniquetxt)){
					rec.setDeletedFlag("Y");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//This method is for Marking deleted record in MIS ACK Document table
	public void removeMISDocumentRecInCache(List<DocumentAckTableDTO> argListOfDocs, DocumentAckTableDTO dtobean){
		try {
			String uniquetxt = dtobean.getSfFileName();
			for(DocumentAckTableDTO rec : argListOfDocs){
				if(rec.getSfFileName().equals(uniquetxt)){
					rec.setDeletedFlag("Y");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//Common carousel data population
	public void getCarouselDetailsFromLumen(LumenRequestDTO rec){
		if(rec != null){
			//setting hospital Name
			if(rec.getIntimation() != null){
				Hospitals hosRec = masterService.getHospitalDetails(rec.getIntimation().getHospital());
				if(hosRec != null){
					rec.setHospitalName(hosRec.getName());
				}
			}else{
				rec.setHospitalName("");
			}
			// setting policy issuing office name
			if(rec.getPolicy() != null){
				OrganaizationUnit officeRec = getBranchCode(rec.getPolicy().getHomeOfficeCode());
				if(officeRec != null){
					rec.setPolicyIssuingOffice(officeRec.getOrganizationUnitName());
				}
			}else{
				rec.setPolicyIssuingOffice("");

				// Creating Policy object for policies which are not available in Db....
				PremPolicyDetails policyDataObj = premiaPullService.fetchPolicyDetailsFromPremia(rec.getPolicyNumber());
				if(policyDataObj != null){
					Policy searchResultObj = new Policy();
					//key not coming
					searchResultObj.setPolicyNumber(policyDataObj.getPolicyNo());
					searchResultObj.setProposerCode(policyDataObj.getProposerCode());
					searchResultObj.setProposerFirstName(policyDataObj.getProposerName());	

					MastersValue policyTypeValue = new MastersValue();
					policyTypeValue.setValue(policyDataObj.getPolicyType());
					searchResultObj.setPolicyType(policyTypeValue);

					MastersValue productTypeValue = new MastersValue();
					productTypeValue.setValue(policyDataObj.getProductName());
					searchResultObj.setProductType(productTypeValue);
					
					Product productObj = new Product();
					productObj.setProductType(policyDataObj.getProductName());
					searchResultObj.setProduct(productObj);
					
					searchResultObj.setProductName(policyDataObj.getProductName());					
					rec.setPolicy(searchResultObj);
				}
			}
			
			//IMS-SUPPORT-20964 - Commenting due to the initiate user name conflict.
			//setting the login id
			//userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
			//rec.setLoginId(userId);
			
			//setting the employee Name
			TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(rec.getLoginId().toLowerCase());
			rec.setEmpName(employeeObj.getEmpFirstName());
		}
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	public Map<String, Object> getAllLumenDetails(LumenRequestDTO requestObj){

		List<LumenDetails> lumenDetailsList = null;
		List<LumenQuery> tempQueryList = null;
		List<LumenQuery> lumenQueryList = new ArrayList<LumenQuery>();
		List<LumenQuery> lumenMISQueryList = new ArrayList<LumenQuery>();
		List<LumenQueryDetails> lumenQueryDetailsList = null;
		List<Long> queryKey = new ArrayList<Long>();
		
//		boolean isQueryAvailable = false;

		Map<String, Object> lumenAllObj = new HashMap<String, Object>();
		// Lumen Details 
		if(requestObj != null){
			Query query = entityManager.createNamedQuery("LumenDetails.findByLumenKey");
			query = query.setParameter("lumenReqKey", requestObj.getLumenRequestKey());
			lumenDetailsList  = query.getResultList();
		}

		//pass this to mapper
		List<LumenDetailsDTO> detailsDTOObj = LumenDetailsMapper.getInstance().getLumenDetailsData(lumenDetailsList);
		lumenAllObj.put("DetailsObj", detailsDTOObj);


		// Lumen Query 
		if(requestObj != null){
			Query query = entityManager.createNamedQuery("LumenQuery.findByLumenKey");
			query = query.setParameter("lumenReqKey", requestObj.getLumenRequestKey());
			tempQueryList  = query.getResultList();
		}
		for(LumenQuery rec: tempQueryList){
			if(rec.getQueryType().equalsIgnoreCase("MIS")){
				lumenMISQueryList.add(rec);
			}else{
				lumenQueryList.add(rec);
			}
		}
		List<LumenQueryDTO> queryDTOObj = LumenQueryMapper.getInstance().getLumenQueryDetails(lumenQueryList);
//		if(queryDTOObj.size() > 0){
//			isQueryAvailable = true;
//		}
				
		List<MISQueryReplyDTO> MISDTOObj = MISQueryMapper.getInstance().getLumenQueryDetails(lumenMISQueryList);

		
		// Lumen Query Details
		if(requestObj != null){
			Query query = entityManager.createNamedQuery("LumenQueryDetails.findByLumenKeyWithQueryKey");
			query = query.setParameter("lumenReqKey", requestObj.getLumenRequestKey());

			if(queryDTOObj == null){
				queryKey.add(0L);
			}else{
				if(queryDTOObj.isEmpty()){
					queryKey.add(0L);
				}else{
					for(LumenQueryDTO rec : queryDTOObj){
						queryKey.add(rec.getQueryKey());
					}
				}
			}
			
			if(MISDTOObj == null){
				queryKey.add(0L);
			}else{
				if(MISDTOObj.isEmpty()){
					queryKey.add(0L);
				}else{
					for(MISQueryReplyDTO rec : MISDTOObj){
						queryKey.add(rec.getQueryKey());
					}
				}
			}
			
			query = query.setParameter("lumenQueryKey", queryKey);
			lumenQueryDetailsList  = query.getResultList();
		}

		List<LumenQueryDetailsDTO> queryDTODetailsObj = LumenQueryDetailsMapper.getInstance().getLumenQueryDetails(lumenQueryDetailsList);
		if(!queryDTODetailsObj.isEmpty()){
			for (LumenQueryDetailsDTO rec : queryDTODetailsObj) {
				for(LumenQueryDTO r : queryDTOObj){
					if(rec.getLumenQuery().getKey() == r.getQueryKey()){
						r.setQuery(rec.getQueryRemarks());
					}
				}
			}
		}		
		
		lumenAllObj.put("QueryObj", queryDTOObj);
		lumenAllObj.put("MISQueryObj", MISDTOObj);
		lumenAllObj.put("QueryDetailsObjList", queryDTODetailsObj);

		return lumenAllObj;
	}

	public void saveLumenLevelOneRequest(LumenRequestDTO argLumenRequestDTO) throws Exception{
		try {
			lumenTrans.mergeLevelOneChanges(argLumenRequestDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public void saveLumenInitiatorRequest(LumenRequestDTO argLumenRequestDTO) throws Exception{
		try {
			lumenTrans.mergeInitiatorChanges(argLumenRequestDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void saveLumenCoordinatorRequest(LumenRequestDTO argLumenRequestDTO) throws Exception{
		try {
			lumenTrans.mergeCoordinatorChanges(argLumenRequestDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void saveLumenMISQueryRequest(LumenRequestDTO argLumenRequestDTO) throws Exception{
		try {
			lumenTrans.mergeMISChanges(argLumenRequestDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public void saveLumenLevelTwoRequest(LumenRequestDTO argLumenRequestDTO) throws Exception{
		try {
			lumenTrans.mergeLevelTwoChanges(argLumenRequestDTO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	
	// Common search logic for all lumen process screens using getTask....
	public Page<LumenRequestDTO> getLumenProcessSearchData(LumenSearchReqFormDTO formDTO, String userName, String argScreenName){
		CommonSearchFormData searchCriteria = new CommonSearchFormData();
		
		
		if(StringUtils.isNotBlank(formDTO.getIntimationNumber())){
			searchCriteria.setIntimationNumber(formDTO.getIntimationNumber());
			//System.out.println("Intimation No "+formDTO.getIntimationNumber());
		}
		if(StringUtils.isNotBlank(formDTO.getPolicyNumber())){
			searchCriteria.setPolicyNumber(formDTO.getPolicyNumber());
			//System.out.println("Policy No "+formDTO.getPolicyNumber());
		}
		if(formDTO.getCmbSource() != null && StringUtils.isNotBlank(formDTO.getCmbSource().getValue())){
			searchCriteria.setSource(formDTO.getCmbSource().getValue()); // request initiated stage.
			//System.out.println("request initiated stage (Source) "+formDTO.getCmbSource().getValue());
		}
		if(formDTO.getCmbStatus() != null && StringUtils.isNotBlank(formDTO.getCmbStatus().getValue())){
			searchCriteria.setStatus(formDTO.getCmbStatus().getValue()); // status
			//System.out.println(" status "+formDTO.getCmbStatus().getValue());
		}
//		if(formDTO.getCmbCPUOffice() != null && StringUtils.isNotBlank(formDTO.getCmbCPUOffice().getValue())){
//			searchCriteria.setCpuCode(formDTO.getCmbCPUOffice().getId());
//			//System.out.println(" CPU "+formDTO.getCmbCPUOffice().getId());
//		}
		if(formDTO.getCpuCodeMulti() != null){
			String cpuSearch = formDTO.getCpuCodeMulti().toString();
			String cpuCodes = "";
			StringBuilder listOfCpuCode = new StringBuilder();
			if(!cpuSearch.equals("[]")){
				String temp[] = cpuSearch.split(",");
				listOfCpuCode.setLength(0);
				for (int i = 0; i < temp.length; i++) {
					String valtemp[] = temp[i].split("-");
					String val = valtemp[0].replaceAll("\\[", "");
					listOfCpuCode.append(val.trim());
					listOfCpuCode.append("$");
				}
				cpuCodes = listOfCpuCode.substring(0,(listOfCpuCode.length() - 1));
			}
			searchCriteria.setCpuCodeMulti(cpuCodes);
			
		}
		if(formDTO.getCmbEmpName() != null){
			//if(searchFormDTO.getCpuCodeMulti() != null){
				String searchVal = "";
				StringBuilder listOfIDs = new StringBuilder();
				String empIdSearch = formDTO.getCmbEmpName().toString();
				if(!empIdSearch.equals("[]")){
					String temp[] = empIdSearch.split(",");
					listOfIDs.setLength(0);
					for (int i = 0; i < temp.length; i++) {
						String valtemp[] = temp[i].split("-");
						String val = valtemp[0].replaceAll("\\[", "");
						listOfIDs.append(val.trim());
						listOfIDs.append("$");
					}
					searchVal = listOfIDs.substring(0, (listOfIDs.length() - 1));
				}
			searchCriteria.setEmpName(searchVal);
			System.out.println(" EmpName "+searchVal);
		}
		
		if(formDTO.getFrmDate() != null){
			searchCriteria.setFromDate(formDTO.getFrmDate());
			//System.out.println(" FromDate "+formDTO.getFrmDate());
		}
		if(formDTO.getToDate() != null){
			searchCriteria.setToDate(formDTO.getToDate());
			//System.out.println(" ToDate "+formDTO.getToDate());
		}
		/*if(formDTO.getDocNameList() != null && formDTO.getDocNameList().size() > 0){
			StringBuilder listOfId = new StringBuilder();
			for(SelectedDoctorDTO rec : formDTO.getDocNameList()){
				listOfId.append(rec.getDoctorId());
				listOfId.append(",");
			}
			String valOfString = listOfId.substring(0, listOfId.toString().length() - 1);
			System.out.println(" Doctor's Name "+valOfString);
			searchCriteria.setLisOfDoctorsId(valOfString);
		}*/
		
		if(argScreenName.equals("ProcessLevelOneSearch")){
			searchCriteria.setCurrentQ("LREG");
		}
		if(argScreenName.equals("ProcessCoordinatorSearch")){
			searchCriteria.setCurrentQ("LCOR");
		}
		if(argScreenName.equals("ProcessLevelTwoSearch")){
			searchCriteria.setCurrentQ("L2AP");
		}
		if(argScreenName.equals("ProcessQueryToMISSearch")){
			searchCriteria.setCurrentQ("LMIS");
		}
		if(argScreenName.equals("InitiatorQueryCaseSearch")){
			searchCriteria.setCurrentQ("LQRY");
		}
		
		Object[] params = SHAUtils.getArrayListForLumenGetTask(searchCriteria);
		Object[] wrapperArrayparams = new Object[1];
		wrapperArrayparams[0] = params;
		DBCalculationService dbCalService = new DBCalculationService();
		List<LumenRequestDTO> lumenSearchResult = dbCalService.getTaskProcedureLumen(wrapperArrayparams);
		Map<Long, List<Object>> keys = new HashMap<Long, List<Object>>();
		List<Object> listOfVal = null;
		if(lumenSearchResult != null && lumenSearchResult.size() > 0){
			for(LumenRequestDTO rec : lumenSearchResult){
				listOfVal = new ArrayList<Object>();
				listOfVal.add(0, rec.getWorkFlowKey());
				listOfVal.add(1, rec.getOutcome());
				keys.put(rec.getLumenRequestKey(), listOfVal);
			}
		}
		searchCriteria.setResultKeys(keys);
		//System.out.println("keys : "+keys);

		Page<LumenRequestDTO> page = new Page<LumenRequestDTO>();
		page.setPageItems(lumenSearchResult);
		if(!lumenSearchResult.isEmpty()){
			page.setTotalRecords(lumenSearchResult.get(0).getTotalRecords().intValue());
		}else{
			page.setTotalRecords(lumenSearchResult.size());
		}
		page.setTotalList(lumenSearchResult);
		return page;
	}
	
	@SuppressWarnings({"static-access" })
	public LumenRequestDTO getLumenDataFromTable(LumenRequestDTO lumenReqObj){
		System.out.println("Selected Key : "+lumenReqObj.getLumenRequestKey());	
		Long wk_key = lumenReqObj.getWorkFlowKey();
		String outCome = lumenReqObj.getOutcome();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<LumenRequest> lumenSearchResultList = new ArrayList<LumenRequest>();		
		List<LumenRequestDTO> lumenSearchResult = new ArrayList<LumenRequestDTO>();

		CriteriaQuery<LumenRequest> lumenCriteriaQuery = builder.createQuery(LumenRequest.class);	
		Root<LumenRequest> lumenRoot = lumenCriteriaQuery.from(LumenRequest.class);

		Expression<Long> exp = lumenRoot.get("key");
		Predicate searchByIntimation = exp.in(lumenReqObj.getLumenRequestKey());
		predicates.add(searchByIntimation);

		lumenCriteriaQuery.select(lumenRoot).where(builder.and(predicates.toArray(new Predicate[] {})));
		TypedQuery<LumenRequest> lumenquery = entityManager.createQuery(lumenCriteriaQuery);
		lumenSearchResultList = lumenquery.getResultList();

		if(lumenSearchResultList != null && lumenSearchResultList.size() > 0){
			lumenSearchResult = LumenRequestMapper.getInstance().getLumenDetails(lumenSearchResultList);
		}

		lumenSearchResult.get(0).setWorkFlowKey(wk_key);
		lumenSearchResult.get(0).setOutcome(outCome);
		//setting policy no for policy search
		lumenSearchResult.get(0).setPolicyNumber(lumenReqObj.getPolicyNumber());
		return lumenSearchResult.get(0);
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public Map<String, List<?>> getMISReplyDetails(MISQueryReplyDTO queryReplyDTO){
		Map<String, List<?>> tableDataHolder = new HashMap<String, List<?>>();
		List<LumenQueryDetails> lumenQueryDetailsList = null;
		List<LumenQueryDocument> lumenQueryDocumentList = null;
		List<Long> queryDetailsKeyList = new ArrayList<Long>();
		if(queryReplyDTO != null){
			Query query = entityManager.createNamedQuery("LumenQueryDetails.findByLumenKeyWithQueryKey");
			query = query.setParameter("lumenReqKey", queryReplyDTO.getLumenRequest().getKey());
			query = query.setParameter("lumenQueryKey", queryReplyDTO.getQueryKey());
			lumenQueryDetailsList  = query.getResultList();
		}

		List<MISSubDTO> queryDTODetailsObj = MISSubQueryDetailsMapper.getInstance().getLumenQueryDetails(lumenQueryDetailsList);
		for(MISSubDTO r : queryDTODetailsObj){
			queryDetailsKeyList.add(r.getLumenQueryDetailsKey());
			r.setQueryRaisedBy(queryReplyDTO.getQueryRaisedBy());
			r.setQueryRaisedDate(queryReplyDTO.getQueryRaisedDate());
		}
		tableDataHolder.put("replyData", queryDTODetailsObj);
		
		if(queryReplyDTO != null){
			Query query = entityManager.createNamedQuery("LumenQueryDocument.findByLumenKey");
			query = query.setParameter("lumenReqKey", queryReplyDTO.getLumenRequest().getKey());
			query = query.setParameter("qryDetailsList", queryDetailsKeyList);
			lumenQueryDocumentList  = query.getResultList();
		}
		List<MISDocumentDTO> queryDocumentDetailsObj = MISQueryDocumentDetailsMapper.getInstance().getLumenQueryDetails(lumenQueryDocumentList);
		for(MISDocumentDTO r : queryDocumentDetailsObj){
			r.setUploadedFileType("Lumen");
		}
		tableDataHolder.put("documentData", queryDocumentDetailsObj);
		return tableDataHolder;
	}
	
	public BeanItemContainer<SelectValue> getLumenSearchStatus(){		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		Map<Long, String> stsVal = new  HashMap<Long, String>();
		stsVal.put(900L, "Lumen Initiated");			
		stsVal.put(911L, "Level 1 Approved");
		stsVal.put(912L, "Level 1 Rejected");
		stsVal.put(913L, "Level 1 Replied");
		stsVal.put(914L, "Level 1 Query Raised");
		stsVal.put(915L, "Level 1 Refer to MIS");
		stsVal.put(916L, "Level 1 Closed");		
		stsVal.put(920L, "Coordinator Approved");
		stsVal.put(921L, "Coordinator Replied");
		stsVal.put(922L, "Coordinator Query Raised");
		stsVal.put(923L, "Coordinator Refer to MIS");		
		stsVal.put(930L, "Level 2 Approved");
		stsVal.put(931L, "Level 2 Rejected");
		stsVal.put(932L, "Level 2 Query Raised");
		stsVal.put(933L, "Level 2 Refer to MIS");
		stsVal.put(934L, "Level 2 Closed");		
		stsVal.put(940L, "MIS Replied");		
		stsVal.put(941L, "Initiator Replied");
		
		for (Map.Entry<Long, String> entry : stsVal.entrySet()) {
			SelectValue selected = new SelectValue();
			selected.setId(entry.getKey());
			selected.setValue(entry.getValue());
			selectValuesList.add(selected);
		}
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	// Status Drop-down values for all process Screens
	public BeanItemContainer<SelectValue> getLevelOneStatus() {
		List<String> mastersValueList = new ArrayList<String>();
		mastersValueList.add("Lumen Initiated");
		mastersValueList.add("Level 2 Query Raised");
		mastersValueList.add("Coordinator Replied");
		mastersValueList.add("Initiator Replied");
		mastersValueList.add("MIS Replied");		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (String value : mastersValueList) {
			select = new SelectValue();
			select.setValue(value);
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getCoordinatorStatus() {
		List<String> mastersValueList = new ArrayList<String>();
		mastersValueList.add("Level 1 Approved");
		mastersValueList.add("Level 2 Query Raised");
		mastersValueList.add("Level 1 Replied");
		mastersValueList.add("Initiator Replied");
		mastersValueList.add("MIS Replied");		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (String value : mastersValueList) {
			select = new SelectValue();
			select.setValue(value);
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getLevelTwoStatus() {
		List<String> mastersValueList = new ArrayList<String>();
		mastersValueList.add("Level 1 Approved");
		mastersValueList.add("Coordinator Approved");
		mastersValueList.add("Coordinator Replied");
		mastersValueList.add("Level 1 Replied");
		mastersValueList.add("Initiator Replied");
		mastersValueList.add("MIS Replied");		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (String value : mastersValueList) {
			select = new SelectValue();
			select.setValue(value);
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getIntiatorStatus() {
		List<String> mastersValueList = new ArrayList<String>();
		mastersValueList.add("Coordinator Query Raised");
		mastersValueList.add("Level 1 Query Raised");
		mastersValueList.add("Level 2 Query Raised");
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (String value : mastersValueList) {
			select = new SelectValue();
			select.setValue(value);
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getMISQueryStatus() {
		List<String> mastersValueList = new ArrayList<String>();
		mastersValueList.add("Level 1 Refer to MIS");
		mastersValueList.add("Level 2 Refer to MIS");
		mastersValueList.add("Coordinator Refer to MIS");
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (String value : mastersValueList) {
			select = new SelectValue();
			select.setValue(value);
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	@SuppressWarnings("static-access")
	public LumenSearchResultTableDTO buildInitiatePage(String argNo){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Claim> claimSearchList = new ArrayList<Claim>();		
		List<LumenSearchResultTableDTO> lumenSearchResult = new ArrayList<LumenSearchResultTableDTO>();
		
		CriteriaQuery<Claim> claimCriteriaQuery = builder.createQuery(Claim.class);
		Root<Claim> claimRoot = claimCriteriaQuery.from(Claim.class);
		Predicate searchIntimation = builder.like(claimRoot.<Intimation>get("intimation").<String>get("intimationId"),"%"+argNo+"%");
		predicates.add(searchIntimation);

		claimCriteriaQuery.select(claimRoot).where(builder.and(predicates.toArray(new Predicate[] {})));
		final TypedQuery<Claim> claimquery = entityManager.createQuery(claimCriteriaQuery);
		claimSearchList = claimquery.getResultList();

		if(claimSearchList != null && claimSearchList.size() > 0){
			lumenSearchResult = CreateLumenMapper.getInstance().getDetails(claimSearchList);
			userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
			updateOtherDetails(lumenSearchResult);
		}
		return lumenSearchResult.get(0);
	}
	
	@SuppressWarnings("deprecation")
	public BeanItemContainer<SelectValue> getInsuredNameList(LumenPolicySearchResultTableDTO argObj){
		List<Insured> tempList = argObj.getListOfInsured();
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		for (Insured rec : tempList) {
			SelectValue selected = new SelectValue();
			selected.setValue(rec.getInsuredName());
			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(selectValueList);
		return container;
	}
	
	@SuppressWarnings({ "unchecked" })
	public Long getStageBySpecialistKey(Long specialistKey){
		if(specialistKey != null){
			Query query = entityManager.createNamedQuery("Specialist.findKey");
			query.setParameter("primaryKey", specialistKey);
			List<Specialist> lumenTrialsList = query.getResultList();
			return lumenTrialsList.get(0).getStage().getKey();
		}else{
			return null;
		}
	}
}
