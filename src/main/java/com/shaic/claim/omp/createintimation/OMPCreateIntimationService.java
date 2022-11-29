package com.shaic.claim.omp.createintimation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationClaimMapper;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationSearchDTO;
import com.shaic.claim.omp.newregistration.OMPNewRegistrationSearchTable;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremInsuredOMPDetails;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.PremPolicyRiskCover;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyRiskCover;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource.Builder;

@Stateless
public class OMPCreateIntimationService extends AbstractDAO<OMPIntimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private OMPIntimationService ompIntService;
	
	@EJB
	private PolicyService policyService;

	@Override
	public Class<OMPIntimation> getDTOClass() {
		return OMPIntimation.class;
	}
	
	@SuppressWarnings("static-access")
	public  Page<OMPCreateIntimationTableDTO> generateTableData(OMPCreateIntimationFormDTO searchFormDTO, String userName, String passWord) {		
		List<OMPIntimation> pageItemList = getOMPIntimationInformation(entityManager, searchFormDTO);
		List<OMPCreateIntimationTableDTO> ompIntimationTableDTO = new ArrayList<OMPCreateIntimationTableDTO>();
		ompIntimationTableDTO = OMPIntimationMapper.getInstance().getOMPIntimationList(pageItemList);		
		updateIntimationWithClaimValues(ompIntimationTableDTO);
		for(OMPCreateIntimationTableDTO searchDto:ompIntimationTableDTO){
			String date = SHAUtils.getDateWithoutTime(pageItemList.get(0).getIntimationDate());
			searchDto.setIntimationdateString(date);
			
		}
		Page<OMPCreateIntimationTableDTO> page = new Page<OMPCreateIntimationTableDTO>();
		page.setPageItems(ompIntimationTableDTO);
		page.setTotalRecords(ompIntimationTableDTO.size());
		page.setTotalList(ompIntimationTableDTO);
		return page;
	}
	
	//R1276
	public  Page<OMPNewRegistrationSearchDTO> generateRegistrationTableData(OMPNewRegistrationSearchDTO searchFormDTO, String userName, String passWord, OMPNewRegistrationSearchTable tableObj) throws ParseException {		
//		List<OMPIntimation> pageItemList = getOMPIntimationInformation(entityManager, searchFormDTO);
//		List<OMPNewRegistrationSearchDTO> ompIntimationTableDTO = new ArrayList<OMPNewRegistrationSearchDTO>();
//		ompIntimationTableDTO = OMPIntimationMapper.getInstance().getOMPIntimationList(pageItemList);		
//		updateIntimationWithClaimValues(ompIntimationTableDTO);
//		for(OMPCreateIntimationTableDTO searchDto:ompIntimationTableDTO){
//			String date = SHAUtils.getDateWithoutTime(pageItemList.get(0).getIntimationDate());
//			searchDto.setIntimationdateString(date);
//			
//		}
//		Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.POLICY_NUMBER, searchFormDTO.getPolicyNoForm());
		mapValues.put(SHAConstants.INTIMATION_NO, searchFormDTO.getIntimationNoForm());
		
		if(searchFormDTO.getIntimationDateForm() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String intimation_Date = sdf.format(searchFormDTO.getIntimationDateForm());
			mapValues.put(SHAConstants.INTIMATION_DATE, intimation_Date);
		}else{
			mapValues.put(SHAConstants.INTIMATION_DATE, "");
		}
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.MANUAL_REGISTRATION_CURRENT_QUEUE);
		
		Object[] setMapValues = SHAUtils.setOMPObjArrayForGetTask(mapValues);
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<Map<String, Object>> taskProcedure = dbCalculationService.getOMPTaskProcedure(setMapValues);
		int pageNumber = searchFormDTO.getPageable().getPageNumber();
//		System.out.println("pageNumber : "+pageNumber);
		
		List<Long> listkeys = new ArrayList<Long>();
		List<Long> pageHolderkeys = new ArrayList<Long>();
		Map<Long, String> keys = new LinkedHashMap<Long, String>();
		Integer totalRec = null;
		if (null != taskProcedure) {
			for (Map<String, Object> outPutArray : taskProcedure) {
				String intimationValue = (String) outPutArray.get(SHAConstants.INTIMATION_NO);
				Long wkey = (Long) outPutArray.get(SHAConstants.WK_KEY);
				totalRec = (Integer)outPutArray.get(SHAConstants.TOTAL_RECORDS);
//				System.out.println("wkey :"+wkey);
				listkeys.add(wkey);
				keys.put(wkey, intimationValue);
			}
		}	
		
		 /*int fromIndex = (pageNumber - 1) * 5;
	     int toIndex = Math.min(listkeys.size(), Math.abs(pageNumber * 5));
	     
	     System.out.println("fromIndex : "+fromIndex);
	     System.out.println("toIndex : "+toIndex);
	     
	     if(fromIndex > toIndex){
	    	 fromIndex = 0;
	    	 toIndex = Math.min(listkeys.size(), Math.abs(0 * 5));
	     }
	     
	     pageHolderkeys = listkeys.subList(fromIndex, toIndex);
	     
	     Map<Long, String> actualkeys = new LinkedHashMap<Long, String>();
	     for(Long rec : pageHolderkeys){
	    	 actualkeys.put(rec,  keys.get(rec));
	     }*/
		
		List<OMPNewRegistrationSearchDTO> listOfDTOs = new ArrayList<OMPNewRegistrationSearchDTO>();
		int i = 1;
		for (Map.Entry<Long,String>  entry : keys.entrySet()) {
			OMPIntimation intimation = ompIntService.searchbyIntimationNo(entry.getValue());
			OMPClaim claim = getOMPClaim(intimation);
			OMPNewRegistrationSearchDTO dtoObj = OMPNewRegistrationClaimMapper.getInstance().getClaimDto(claim);
			if(dtoObj != null){
				if(dtoObj.getIntimationDateTbl() != null){
					DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
					DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date date = originalFormat.parse(dtoObj.getIntimationDateTbl().toString());
					String formattedDate = targetFormat.format(date);
					dtoObj.setIntimationDateTblStr(formattedDate);
				}else{
					dtoObj.setIntimationDateTblStr("");
				}
				dtoObj.setSnoTbl(i);
				dtoObj.setWfKey(entry.getKey());
				listOfDTOs.add(dtoObj);
				i++;
			}
		}
		Page<OMPNewRegistrationSearchDTO> regpage = new Page<OMPNewRegistrationSearchDTO>();
//		searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
//		searchFormDTO.getPageable().setNextFlag(true);
//		regpage.setHasNext(true);
		regpage.setPageItems(listOfDTOs);
//		regpage.setTotalRecords(listOfDTOs.size());
//		regpage.setTotalRecords(listkeys.size());
		regpage.setTotalRecords(totalRec);
		regpage.setTotalList(listOfDTOs);
		regpage.setIsDbSearch(false);
		
		
		/*if(null != pageHolderkeys && pageHolderkeys.isEmpty()) {
			System.out.println("Coming in PageHolder condition");
			regpage.setHasNext(false);
//			searchFormDTO.getPageable().setNextFlag(false);
			searchFormDTO.getPageable().setPageNumber(1);
		}
		else if(null !=listkeys && toIndex >= listkeys.size()){
			System.out.println("Coming in listkeys condition");
			//This values will set once the table reaches the last page.
			regpage.setHasNext(false);
			searchFormDTO.getPageable().setNextFlag(false);
			if(tableObj.getTable() != null){
				if(toIndex == listkeys.size()){
					searchFormDTO.getPageable().setPageNumber(pageNumber); // pageNumber will have the page no count.....
//					searchFormDTO.getTable().handlePageFirstAndLast();
					searchFormDTO.getPageable().next();
				}
			}
			regpage.setPageNumber(pageNumber);
		}else{
			System.out.println("Coming in else condition");
			regpage.setHasNext(true);
			regpage.setPageNumber(pageNumber);
			searchFormDTO.getPageable().setNextFlag(true);
		}*/
		
		/*if(listOfDTOs.isEmpty()){
			searchFormDTO.getPageable().setPageNumber(1);
		}*/
//		regpage.setIsDbSearch(true);
		return regpage;
	}
	
	@SuppressWarnings("unchecked")
	public OMPClaim getOMPClaim(OMPIntimation argIntimation){
		OMPClaim claimObj = null;
		Query claimByIntimationQuery = entityManager.createNamedQuery("OMPClaim.findByOMPIntimationKey");
		claimByIntimationQuery.setParameter("intimationKey", argIntimation.getKey());
		claimByIntimationQuery.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
		List<OMPClaim> claimList = claimByIntimationQuery.getResultList();
		if(claimList != null && !claimList.isEmpty()){
			claimObj = claimList.get(0);
		}		
		return claimObj;
	}
	
	public  Page<OMPCreateIntimationTableDTO> getAddIntimationData(String PolicyNumber) {	
	/*	Builder builder = PremiaService.getInstance().getBuilder();
		String params = "{\"HealthCardNo\":\"\",\"InsuredDOB\":\"\",\"InsuredName\":\"\",\"MobileNo\":\"\",\"OfficeCode\":\"\",\"PolicyNo\":\""+PolicyNumber+"\",\"ProductName\":\"\",\"ProposerDOB\":\"\",\"ProposerName\":\"\",\"ReceiptNo\":\"\"}";
		List<OMPCreateIntimationPolicyDetails> listofpolicyDetails = builder.post(new GenericType<List<OMPCreateIntimationPolicyDetails>>() {}, params);
		if(listofpolicyDetails!=null && !listofpolicyDetails.isEmpty()){*/
		PremPolicyDetails policyDetails =null;
		//Bancs Changes Start
		Policy policyObj = null;
		Builder builder = null;
		
		if (PolicyNumber != null) {
			policyObj = policyService.getByPolicyNumber(PolicyNumber);
			if (policyObj != null) {
				 if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					//builder = BancsSevice.getInstance().getOMPPolicyDetail();
				}else{
					builder = PremiaService.getInstance().getOMPPolicyDetail();
					policyDetails = builder.post(new GenericType<PremPolicyDetails>() {
					}, "\"" + PolicyNumber + "\"");
					
				}
			}else{
				builder = PremiaService.getInstance().getOMPPolicyDetail();
				policyDetails = builder.post(new GenericType<PremPolicyDetails>() {
				}, "\"" + PolicyNumber + "\"");
			}
		}
		
		//Bancs Changes End
		//Builder builder = PremiaService.getInstance().getOMPPolicyDetail();
		try{
//			policyDetails = new PremPolicyDetails();
		 
		}catch(UniformInterfaceException e){
			System.out.println("----------- -----# Wrong Policy Number Entered #-------------- ------- ");
			System.out.println(e.getMessage());
		}
		if(policyDetails!=null){
//		OMPCreateIntimationPolicyDetails policyDetails = listofpolicyDetails.get(0);
		
		OMPCreateIntimationTableDTO policyDetailObject = new OMPCreateIntimationTableDTO();
		policyDetailObject.setPolicyNo(policyDetails.getPolicyNo());
		PremInsuredOMPDetails premiaInsuredOMPDetails = null;
		if(policyDetails.getPremiaInsuredOmpCorpdetails()!=null){
			premiaInsuredOMPDetails = policyDetails.getPremiaInsuredOmpCorpdetails();
		}else if(policyDetails.getPremiaInsuredOmpdetails()!=null){
			premiaInsuredOMPDetails = policyDetails.getPremiaInsuredOmpdetails();
		}else if(policyDetails.getPremiaInsuredOmpStudentdetails()!=null){
			premiaInsuredOMPDetails = policyDetails.getPremiaInsuredOmpStudentdetails();
		}
		if(premiaInsuredOMPDetails!=null){
			policyDetailObject.setProposername(policyDetails.getProposerName());
			policyDetailObject.setInsuredName(premiaInsuredOMPDetails.getInsuredName());
			policyDetailObject.setPassportNo(premiaInsuredOMPDetails.getPassportNo());
			policyDetailObject.setPlan(premiaInsuredOMPDetails.getPlan());
		}
		policyDetailObject.setProductCodeOrName(policyDetails.getProductCode() +" / " +policyDetails.getProductName());		
		Date endDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(policyDetails.getPolicyEndDate())));
		Date startDate = SHAUtils.formatPremiaDate(SHAUtils.formatPremiaDateAsString(new Date(policyDetails.getPolicyStartDate())));
//		policyDetailObject.setPolicyCoverFromDateStr(SHAUtils.getDateFormat(policyDetails.getPolicyStartDate()));
//		policyDetailObject.setPolicyCoverToDateStr(SHAUtils.getDateFormat(policyDetails.getPolicyEndDate()));
		policyDetailObject.setPolicyCoverPeriodToDate(endDate);
		policyDetailObject.setPolicyCoverPeriodFromDate(startDate);
		
		Double sumInsured =0d;
		String cover1 = "OMP-CVR-001";
		String cover2 = "STP-CVR-001";
		String cover3 = "CFT-CVR-001";
		List<PremPolicyRiskCover> premPolicyRiskCover = policyDetails.getPremInsuredRiskCoverDetails();
		 if(premPolicyRiskCover != null && ! premPolicyRiskCover.isEmpty()){
	        	for (PremPolicyRiskCover premPolicyRiskCover2 : premPolicyRiskCover) {
	        		PolicyRiskCover riskCover = new PolicyRiskCover();
	        		riskCover.setCoverCode(premPolicyRiskCover2.getCoverCode());
	        		riskCover.setCoverCodeDescription(premPolicyRiskCover2.getCoverDescription());
	        		riskCover.setSumInsured(Double.parseDouble(premPolicyRiskCover2.getSumInsured()));
	        		if(cover1.equalsIgnoreCase(premPolicyRiskCover2.getCoverCode())){
	        			sumInsured = Double.parseDouble(premPolicyRiskCover2.getSumInsured());
	        		}else if(cover2.equalsIgnoreCase(premPolicyRiskCover2.getCoverCode())){
	        			sumInsured = Double.parseDouble(premPolicyRiskCover2.getSumInsured());
	        		}else if(cover3.equalsIgnoreCase(premPolicyRiskCover2.getCoverCode())){
	        			sumInsured = Double.parseDouble(premPolicyRiskCover2.getSumInsured());
	        		}
	        		//Suganesh
				}
	        }
		 policyDetailObject.setSumInsured(sumInsured);
		
		
		List<OMPCreateIntimationTableDTO> ompIntimationTableDTO = new ArrayList<OMPCreateIntimationTableDTO>();
		ompIntimationTableDTO.add(policyDetailObject);
		
		Page<OMPCreateIntimationTableDTO> page = new Page<OMPCreateIntimationTableDTO>();
		page.setPageItems(ompIntimationTableDTO);
		page.setTotalRecords(ompIntimationTableDTO.size());
		page.setTotalList(ompIntimationTableDTO);
		
		return page;
		}else{
			return null;
		}
	}
	
	
	public static List<OMPIntimation> getOMPIntimationInformation(EntityManager entityManager, OMPCreateIntimationFormDTO searchFormDTO) {		
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OMPIntimation> criteriaQuery = builder.createQuery(OMPIntimation.class);
		Root<OMPIntimation> intimationRoot = criteriaQuery.from(OMPIntimation.class);
		boolean claimSearch = false;
		Long statusKey = null;
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<OMPIntimation> resultList = null;
		if(StringUtils.isNotBlank(searchFormDTO.getClaimNumber())){

			final CriteriaQuery<OMPClaim> claimCriteriaQuery = builder
					.createQuery(OMPClaim.class);

			Root<OMPClaim> claimRoot = claimCriteriaQuery
					.from(OMPClaim.class);
			Join<OMPClaim, OMPIntimation> claimJoin = claimRoot.join(
					"intimation", JoinType.INNER);

			Predicate claimNumberPredicate = builder.like(
					claimRoot.<String> get("claimId"), "%"+searchFormDTO.getClaimNumber());
			predicates.add(claimNumberPredicate);
			
			statusKey = ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY;
			Predicate statusPredicate = builder.equal(claimRoot.<OMPIntimation> get("intimation")
					.<Status> get("status")
					.<Long> get("key"),
							statusKey);
			predicates.add(statusPredicate);
			
			if (searchFormDTO.getPolicyNo() != null && searchFormDTO.getPolicyNo().length()>0) {
				Predicate policyPredicate = builder.like(
						claimRoot.<OMPIntimation> get("intimation")
								.<Policy> get("policy")
								.<String> get("policyNumber"),
								"%"+searchFormDTO.getPolicyNo()+"%");
				predicates.add(policyPredicate);
			}

			if (searchFormDTO.getIntimationNo() != null && searchFormDTO.getIntimationNo().length()>0) {
				Predicate intimationNoPredicate = builder.like(
						claimRoot.<OMPIntimation> get("intimation")
								.<String> get("intimationId"),
								"%"+searchFormDTO.getIntimationNo()+"%");
				predicates.add(intimationNoPredicate);
			}

			if (searchFormDTO.getInsuredName() != null && searchFormDTO.getInsuredName().length()>0) {
				Predicate insuredNamePredicate = builder.like(
						claimRoot.<OMPIntimation> get("intimation")
								.<Insured> get("insured")
								.<String> get("insuredName"),
								"%"+searchFormDTO.getInsuredName()+"%");
				predicates.add(insuredNamePredicate);
			}




			claimCriteriaQuery.select(claimRoot).where(
					builder.and(predicates
							.toArray(new Predicate[] {})));

			final TypedQuery<OMPClaim> claimQuery = entityManager
					.createQuery(claimCriteriaQuery);
			List<OMPClaim> claimList = new ArrayList<OMPClaim>();
			
			claimList = (List<OMPClaim>) claimQuery.getResultList();
			
			
			
			resultList = new ArrayList<OMPIntimation>();
			
			if (claimList != null && !claimList.isEmpty()) {
				for (OMPClaim claim : claimList) {
					resultList.add(claim.getIntimation());
				}
			}
		
		}else{
			if (searchFormDTO.getIntimationStatus() != null) {
				if((ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY).equals(searchFormDTO.getIntimationStatus().getId())){
					statusKey = ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY;
				}else if((ReferenceTable.INTIMATION_SAVE_STATUS_KEY).equals(searchFormDTO.getIntimationStatus().getId())){
					statusKey = ReferenceTable.INTIMATION_SAVE_STATUS_KEY;
				}
				Predicate statusPredicate = builder.equal(
						intimationRoot.<Status> get("status")
								.<Long> get("key"),
								statusKey);
				predicates.add(statusPredicate);
			}

			if (StringUtils.isNotBlank(searchFormDTO.getPolicyNo())) {
				Predicate policyPredicate = builder.like(
						intimationRoot.<Policy> get("policy")
								.<String> get("policyNumber"),
								"%"+searchFormDTO.getPolicyNo()+"%");
				predicates.add(policyPredicate);
			}

			if (StringUtils.isNotBlank(searchFormDTO.getIntimationNo())) {
				Predicate intimationNoPredicate = builder
						.like(intimationRoot
								.<String> get("intimationId"),
								"%"+searchFormDTO.getIntimationNo()+"%");
				predicates.add(intimationNoPredicate);
			}

			if (StringUtils.isNotBlank(searchFormDTO.getInsuredName())) {
				Predicate insuredNamePredicate = builder.like(
						builder.upper(intimationRoot.<Insured> get(
								"insured").<String> get(
								"insuredName")), "%"+searchFormDTO.getInsuredName()+"%");
				predicates.add(insuredNamePredicate);
			}



			criteriaQuery.select(intimationRoot).where(
					builder.and(predicates
							.toArray(new Predicate[] {})));

			final TypedQuery<OMPIntimation> intimationquery = entityManager.createQuery(criteriaQuery);
			resultList = intimationquery.getResultList();
		
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public void updateIntimationWithClaimValues(List<OMPCreateIntimationTableDTO> intimationObjects){
		
		Query getClaimNoForIntimation = entityManager.createNamedQuery("OMPClaim.findByOMPIntimationKey");
		List<OMPClaim> listOfClaimObj = null;
		
		for (OMPCreateIntimationTableDTO objEntry  : intimationObjects){
			getClaimNoForIntimation = getClaimNoForIntimation.setParameter("intimationKey", objEntry.getIntimationKey());
			getClaimNoForIntimation = getClaimNoForIntimation.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);			
			listOfClaimObj = getClaimNoForIntimation.getResultList();
			if(listOfClaimObj != null && !listOfClaimObj.isEmpty()){
				objEntry.setClaimno(listOfClaimObj.get(0).getClaimId());
				objEntry.setStatus(listOfClaimObj.get(0).getStatus().getProcessValue());
			}else{
				objEntry.setClaimno("");
				objEntry.setStatus("");
			}		
			if(listOfClaimObj!= null && listOfClaimObj.size()>0 && listOfClaimObj.get(0).getHospitalName()!= null){
				objEntry.setHospitalname(listOfClaimObj.get(0).getHospitalName());
			}else{
				objEntry.setHospitalname("");
			}
			
		}
	}	
	
	public OMPIntimation submitOMPIntimation(OMPIntimation intimationObj) throws Exception{
		if (intimationObj.getKey() != null) {
			intimationObj.setModifiedDate(new Date());
			entityManager.merge(intimationObj);
		} else {
			entityManager.persist(intimationObj);
			//entityManager.refresh(intimationObj);
			//intimationObj = entityManager.find(OMPIntimation.class, intimationObj.getKey());
		}
		//intimationObj = getOMPIntimationByKey(intimationObj.getKey());
		entityManager.flush();
		return intimationObj;
	}
	
	public OMPIntimation registerOMPIntimation(OMPIntimation intimationObj) throws Exception{
		entityManager.merge(intimationObj);
		entityManager.flush();
		return intimationObj;
	}
	
	public Date parseDateValue(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date resultdate = null;		
		try {
			resultdate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultdate;
	}
	
	public OMPClaim getClaimforOMPIntimation(Long intimationKey) {
		OMPClaim a_claim = null;
		if (intimationKey != null) {
			Query findByIntimationKey = entityManager
					.createNamedQuery("OMPClaim.findByOMPIntimationKey");
			findByIntimationKey = findByIntimationKey.setParameter("intimationKey", intimationKey);
			findByIntimationKey = findByIntimationKey.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
			try {

				if (findByIntimationKey.getResultList().size() > 0) {					
					a_claim = (OMPClaim)findByIntimationKey.getResultList().get(0);
					entityManager.refresh(a_claim);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		} else {
			// intimationKey null
		}
		return a_claim;
	}
	
	@SuppressWarnings({"unchecked"})
	public OMPIntimation getOMPIntimationByKey(Long intimationKey) {
		Query findByKey = entityManager.createNamedQuery("OMPIntimation.findByKey").setParameter("intiationKey", intimationKey);
		List<OMPIntimation> intimationList = (List<OMPIntimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public OMPIntimation getOMPIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery("OMPIntimation.findByOMPIntimationNo");
		findByKey = findByKey.setParameter("intimationNo", intimationNo);
		findByKey = findByKey.setParameter("lobId", ReferenceTable.OMP_LOB_KEY);
		List<OMPIntimation> intimationList = (List<OMPIntimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
	public void submitDBProcedureForOMPIntimation(OMPIntimation ompIntimation){
		Hospitals hospital = null;		
		if(hospital == null){
			hospital = hospitalService.getHospitalById(ompIntimation.getHospital());
		}
		Object[] objArray = SHAUtils.getArrayListForOMPIntimationSubmit(ompIntimation, hospital);
		Object[] objArrayForSubmit = (Object[]) objArray[0];
		objArrayForSubmit[SHAConstants.INDEX_OUT_COME] = SHAConstants.MANUAL_REGISTRATION_OUTCOME;
		DBCalculationService dbCalService = new DBCalculationService();
		dbCalService.initiateOMPTaskProcedure(objArray);
	}
	
}
