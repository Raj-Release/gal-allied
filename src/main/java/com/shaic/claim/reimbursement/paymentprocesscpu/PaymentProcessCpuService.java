package com.shaic.claim.reimbursement.paymentprocesscpu;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.Intimation;
import com.shaic.domain.LegalHeir;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.reimbursement.medicalapproval.processclaimrequestzonal.search.SearchProcessClaimRequestZonalTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

@Stateless
public class PaymentProcessCpuService extends AbstractDAO<Intimation>{

	@PersistenceContext
	protected EntityManager entityManager;
	protected String cpu;
	protected String cpuLotNo;
	protected String branch;
	   /**
	    *   Commented as per  Management Change on  18-05-2018  
	    */
//	protected String letterStatus;   
	@EJB
	private ReimbursementService reimbursementService;
	
	@Inject
	private ViewQueryDetailsTable queryDetailsTableObj;
	
	public PaymentProcessCpuService() {
		super();
	}
	
	public  Page< PaymentProcessCpuTableDTO> search( PaymentProcessCpuFormDTO cpuFormDTO,String userName, String passWord,UsertoCPUMappingService usertoCPUMapService) {
		
		List<ClaimPayment> listClaimPayments = new ArrayList<ClaimPayment>(); 
		
		try{
		
			Date fromDate = null != cpuFormDTO.getFromDate() ? cpuFormDTO.getFromDate() : null;
			Date toDate = null != cpuFormDTO.getToDate() ? cpuFormDTO.getToDate() : null;
			String intimationNo = null != cpuFormDTO.getIntimationNo() ? cpuFormDTO.getIntimationNo() : null;
	//		String intimationSeqNo = null != cpuFormDTO.getIntimationSeqNo() ? cpuFormDTO.getIntimationSeqNo() : null;
			String claimNo = null != cpuFormDTO.getClaimNumber() ? cpuFormDTO.getClaimNumber() : null;
			
			   /**
			    *   Commented as per  Management Change on  18-05-2018  
			    */
			/*letterStatus = null;			
			SelectValue statusValue = cpuFormDTO.getStatus();
			if(null != statusValue && null != statusValue.getValue())
			{
				letterStatus = statusValue.getValue();
			}*/
			
			 cpu = null;
			SelectValue cpuValue = cpuFormDTO.getCpu();
			if(null != cpuValue && null != cpuValue.getValue())
			{     
				 String Value[] = cpuValue.getValue().split(" ");
				 cpu = Value[0];
				 
			}
			else{
				List<Long> cpuKeyList = usertoCPUMapService.getCPUCodeList(userName, entityManager);
			}
			
		    cpuLotNo = null;
			SelectValue lotValue = cpuFormDTO.getCpuLotNo();
			if(null != lotValue && null != lotValue.getValue())
			{
				 cpuLotNo = lotValue.getValue();
			}
			
		    branch = null;
			SelectValue branchValue = cpuFormDTO.getBranch();
			if(null != branchValue && null != branchValue.getValue())
			{
				 branch = branchValue.getValue();
			}
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<ClaimPayment> criteriaQuery = criteriaBuilder.createQuery(ClaimPayment.class);
			
			Root<ClaimPayment> root = criteriaQuery.from(ClaimPayment.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			if(null != fromDate)
			{
				Predicate condition1 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition1);
			}
			if(null != toDate)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition2 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition2);
				
				/*Predicate condition2 = criteriaBuilder.equal(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition2);*/
			}
			if(null != intimationNo && !("").equalsIgnoreCase(intimationNo))
			{
				//Predicate condition1 = criteriaBuilder.like(root.<Intimation>get("intimation").<String>get("intimationId"), "%"+intimationNo+"%");
				//Predicate condition3 = criteriaBuilder.equal(root.<String>get("intimationNumber"), intimationNo);
				Predicate condition3 = criteriaBuilder.like(root.<String>get("intimationNumber"),  "%"+intimationNo+"%");
				conditionList.add(condition3);
			}
			
			if(null != claimNo && !("").equalsIgnoreCase(claimNo))
			{
				//Predicate condition4 = criteriaBuilder.equal(root.<String>get("claimNumber"), claimNo);
				Predicate condition4 = criteriaBuilder.like(root.<String>get("claimNumber"),  "%"+claimNo+"%");

				conditionList.add(condition4);
			}
			
			/*if(null !=  intimationSeqNo && !("").equalsIgnoreCase(intimationSeqNo))
			{
				Predicate condition5 = criteriaBuilder.equal(root.<String>get("intimationNumber"),"%"+intimationSeqNo+"%");
				conditionList.add(condition5);
			}*/
			
			if(null != cpu)
			{
				Predicate condition6 = criteriaBuilder.equal(root.<Long>get("cpuCode"), new Long(cpu));
				conditionList.add(condition6);
			}
			else{
					List<Long> cpuCodeValueList = usertoCPUMapService.getCPUValueList(userName, entityManager);
					Predicate userCPUcondition = root.<Long>get("cpuCode").in(cpuCodeValueList);
					conditionList.add(userCPUcondition);
			}
				
			
			if(null != cpuLotNo)
			{
				Predicate condition7 = criteriaBuilder.equal(root.<String>get("lotNumber"), cpuLotNo);
				conditionList.add(condition7);
			}
			
			
			
			//String lettersPrinted = "Yes";
			//String lettersPending = "No";
			
			
			   /**
			    *   Commented as per  Management Change on  18-05-2018  To Enable On-Demand Letter Generation  
			    */
			
			/*if(null != letterStatus)
			{
				if(letterStatus.equals(SHAConstants.LETTERS_PRINTED))
				{
					Predicate condition8 = criteriaBuilder.equal(root.<String>get("letterPrintingMode"),SHAConstants.YES_FLAG);
					conditionList.add(condition8);
				}
									
				if(letterStatus.equals(SHAConstants.LETTERS_PRINT_PENDING))
				{
					Predicate condition8 = criteriaBuilder.equal(root.<String>get("letterPrintingMode"),SHAConstants.N_FLAG);
					conditionList.add(condition8);
				}
				//Email sent and email sent and print pending is yet to be implemented.
			}*/
			
			
			Predicate condition9 = criteriaBuilder.equal(root.<Status>get("statusId").<Long>get("key"),ReferenceTable.PAYMENT_SETTLED);
			conditionList.add(condition9);
			
			/*Predicate condition8 = criteriaBuilder.notEqual(root.<String>get("letterPrintingMode"),SHAConstants.YES_FLAG);
			//conditionList.add(condition8);
			
			Predicate condition7 = criteriaBuilder.isNull(root.<String>get("letterPrintingMode"));
			//conditionList.add(condition7);
			
			Predicate condition10 = criteriaBuilder.or(condition8,condition7);
			conditionList.add(condition10);*/
			
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			

			final TypedQuery<ClaimPayment> typedQuery = entityManager.createQuery(criteriaQuery);
			
			int pageNumber = cpuFormDTO.getPageable().getPageNumber();
			int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 1;
			}
			
			listClaimPayments = typedQuery.getResultList();
			
			if( listClaimPayments.size()>10)
			{
				listClaimPayments = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}
					
		    List<ClaimPayment> doList = listClaimPayments;
		    
		    ClaimPayment claimPayment = null;		    
		    if(null != doList && !doList.isEmpty())
		    {		    			    
		    claimPayment = doList.get(0);
		    entityManager.refresh(claimPayment);
		    }
			List<PaymentProcessCpuTableDTO> tableDTO = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(doList);			
		//	List<PaymentProcessCpuTableDTO> result = new ArrayList<PaymentProcessCpuTableDTO>();	
			
			List<PaymentProcessCpuTableDTO> finalResultList = new ArrayList<PaymentProcessCpuTableDTO>();
		
			
			if(null != branch && !("").equalsIgnoreCase(branch))
			{
				List<TmpCPUCode> cpuCode = getCpuCodeByBranch(branch);
				if(null != cpuCode && !cpuCode.isEmpty())
				{
					if(null != tableDTO && !tableDTO.isEmpty())
					{
						for (TmpCPUCode tmpCPUCode : cpuCode) {
							for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO : tableDTO) {
								if(tmpCPUCode.getCpuCode().equals(paymentProcessCpuTableDTO.getCpuCode()))
								{
									finalResultList.add(paymentProcessCpuTableDTO);
								}
								
							}
						}
					}
					/*else
					{
						
						

						Predicate condition11 = criteriaBuilder.equal(root.<Long>get("cpuCode"), cpuCode.getKey());
						conditionList.add(condition11);
						criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
						final TypedQuery<ClaimPayment> typedQueryForBranch = entityManager.createQuery(criteriaQuery);
						List<ClaimPayment> branchPaymentList = typedQueryForBranch.getResultList();
						List<PaymentProcessCpuTableDTO> branchResultList = PaymentProcessCpuMapper.getpaymentCpuTableObjects(branchPaymentList);
						result.addAll(branchResultList);
					}*/
					
				}
			}
			else
			{
				/*int rowCount = 1;
				for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO : tableDTO) {
					
					{												
						if(null != paymentProcessCpuTableDTO.getLetterFlag())
							{
								if(!(paymentProcessCpuTableDTO.getLetterFlag().equals(SHAConstants.YES_FLAG)))
								{
									
									finalResultList.add(paymentProcessCpuTableDTO);
								}
								else
								{
									finalResultList.add(paymentProcessCpuTableDTO);
								}
						}
						else
						{
							finalResultList.add(paymentProcessCpuTableDTO);
						}
					}
					
				}*/
				
				finalResultList.addAll(tableDTO);
			}
			
			/*		
			List<PaymentProcessCpuTableDTO> result = new ArrayList<PaymentProcessCpuTableDTO>();*/
			
			Page<PaymentProcessCpuTableDTO> page = new Page<PaymentProcessCpuTableDTO>();
			cpuFormDTO.getPageable().setPageNumber(pageNumber + 1);
			if(finalResultList.size()<=10) 
				
			{
				page.setHasNext(true);
			}
			else
			{
			page.setHasNext(true);
			}
			if (finalResultList.isEmpty()) {
				cpuFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			
			Collections.sort(finalResultList, getPaymentComparator());			
			
			page.setPageItems(finalResultList);
			page.setIsDbSearch(true);
			return page;
		  }
			catch (Exception e) 
		   {
		     e.printStackTrace();
		   }
		   return null;	
		}
			
	private Comparator<PaymentProcessCpuTableDTO> getPaymentComparator(){
		
		class SortablePaymentProcessCpuTableDTO extends PaymentProcessCpuTableDTO implements Comparator<PaymentProcessCpuTableDTO> {

			@Override
			public int compare(PaymentProcessCpuTableDTO obj1,
					PaymentProcessCpuTableDTO obj2) {
				
				return obj1.getModifiedDate().compareTo(obj2.getModifiedDate()) ;
				
			}
			
		}
		return new SortablePaymentProcessCpuTableDTO();
		
	}
	
	
	public ClaimPayment getLetterFlagDetails(Long key)
	{
			
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByKey").setParameter("primaryKey", key);
		try{
			List<ClaimPayment> letterFlagDetail = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(letterFlagDetail != null && !letterFlagDetail.isEmpty()){
				for(ClaimPayment claimPaymentDetail : letterFlagDetail){
					entityManager.refresh(claimPaymentDetail);
					return letterFlagDetail.get(0);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		
		return null;
	}
	private  List<TmpCPUCode> getCpuCodeByBranch(String description)
	{
		Query query = entityManager.createNamedQuery("TmpCPUCode.findByDescription");
		query = query.setParameter("description", description);
		List<TmpCPUCode> cpuCodeList = query.getResultList();
		if(null != cpuCodeList && !cpuCodeList.isEmpty())
		{
			for (TmpCPUCode tmpCPUCode : cpuCodeList) {
				entityManager.refresh(tmpCPUCode);
			}
			
			return cpuCodeList;
		}
		return null;
	}
	public List<ClaimPayment> getPaymentDetailsByClaimNumber(String claimNumber,EntityManager em)
	{
		this.entityManager = em;
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(claimNumber != null){
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByClaimNumber").setParameter("claimNumber", claimNumber);
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
				for(ClaimPayment claimPaymentDetail : paymentDetailsList){
					entityManager.refresh(claimPaymentDetail);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return paymentDetailsList;
	}
	
	public List<ClaimPayment> getPaymentDetailsByRodNumber(String rodNumber,EntityManager em)
	{
		this.entityManager = em;
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(rodNumber != null){
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByRodNo").setParameter("rodNumber", rodNumber);
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
				for(ClaimPayment claimPaymentDetail: paymentDetailsList){
					entityManager.refresh(claimPaymentDetail);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return paymentDetailsList;
	}
	
	public List<ClaimPayment> getPaymentDetailsByRodNumber(String rodNumber,Long status)
	{
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(rodNumber != null){
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByRodNoAndStatus");
			findByPaymentKey.setParameter("rodNumber", rodNumber);
			findByPaymentKey.setParameter("statusId", status);
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
				for(ClaimPayment claimPaymentDetail: paymentDetailsList){
					entityManager.refresh(claimPaymentDetail);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return paymentDetailsList;
	}
	
	public List<ClaimPayment> getPaymentDetailsByClaimNumberForView(String claimNumber)
	{
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(claimNumber != null){
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByClaimNumber").setParameter("claimNumber", claimNumber);
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			if(paymentDetailsList != null && !paymentDetailsList.isEmpty()){
				for(ClaimPayment claimPaymentDetail : paymentDetailsList){
					entityManager.refresh(claimPaymentDetail);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return paymentDetailsList;
	}
	
	 
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ClaimPayment getPaymentClaimsDetails(PaymentProcessCpuPageDTO pageDto)
	{
		ClaimPayment claimPaymentDetail;
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByKey").setParameter("primaryKey", pageDto.getClaimPaymentKey());
		try{
			claimPaymentDetail = (ClaimPayment) findByPaymentKey.getSingleResult();
			
			
		 //  claimPaymentDetail.setLetterPrintingMode(pageDto.getLetterPrintingMode().toString());
			if(pageDto.getLetterPrintingMode().equals(true))
		   {
			   claimPaymentDetail.setLetterPrintingMode("Y"); 
		   }
		   
			else
			{
				claimPaymentDetail.setLetterPrintingMode("N"); 
			}
		   		   		
		   entityManager.merge(claimPaymentDetail);
		   entityManager.flush();
		   entityManager.clear();
		   
		   /**
		    *   Payment Letters Upload to DMS disabled   -  Commented as per Management Change    on  18-05-2018  
		    */
		   
		   /*HashMap dataMap = pageDto.getFilePathAndTypeMap();
			
			dataMap.put("intimationNumber",claimPaymentDetail.getIntimationNumber());
			dataMap.put("claimNumber",claimPaymentDetail.getClaimNumber());
			dataMap.put("createdBy",claimPaymentDetail.getCreatedBy());
			
			if(null != claimPaymentDetail.getClaimType())
			{
				if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(pageDto.getClaimDto().getClaimType().getId()))
					{
						Preauth preauth = SHAUtils.getPreauthClaimKey(entityManager , pageDto.getClaimDto().getClaimType().getId());
						if(null != preauth && dataMap != null)
							dataMap.put("cashless", preauth.getPreauthId());
							dataMap.put("reimbursementNumber", claimPaymentDetail.getRodNumber());
					}
				else if((ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY).equals(pageDto.getClaimDto().getClaimType().getId()) && dataMap != null)
				{
					dataMap.put("reimbursementNumber", claimPaymentDetail.getRodNumber());
				}
			}
			
			
			if(dataMap != null) {
				String paymentAndDischargeFilePath = (String) dataMap.get("PaymentVoucherFilePath");
				String paymentAndDischargeDocType = (String) dataMap.get("PaymentVoucherDocType");
				dataMap.put("filePath", paymentAndDischargeFilePath);
				dataMap.put("docType", paymentAndDischargeDocType);
				uploadGeneratedLetterToDMS(dataMap);
				
				String dvCoveringFilePath = (String)dataMap.get("HospitalPaymentLetterFilePath");
				String dvCoveringDocType = (String) dataMap.get("HospitalPaymentLetterDocType");
				dataMap.put("filePath", dvCoveringFilePath);
				dataMap.put("docType", dvCoveringDocType);
				uploadGeneratedLetterToDMS(dataMap);
				
				String dischargeVoucherFilePath = (String)dataMap.get("DischargeVoucherFilePath");
				String dischargeVoucherDocType = (String) dataMap.get("DischargeVoucherDocType");
				dataMap.put("filePath", dischargeVoucherFilePath);
				dataMap.put("docType", dischargeVoucherDocType);
				uploadGeneratedLetterToDMS(dataMap);				
				
			}*/
			
		   			
			return claimPaymentDetail;
		}
		catch(Exception e)
		{
		
			return null;
		}
	}
	
	public void uploadGeneratedLetterToDMS(WeakHashMap dataMap)
	{
		String filePath = (String)dataMap.get("filePath");
		if(null != filePath && !("").equalsIgnoreCase(filePath))
		{
			SHAUtils.uploadGeneratedLetterToDMS(entityManager,dataMap);
		}
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getBranchFromCPUCode(Long cpuCode) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		List<TmpCPUCode> tmpCpudCodeList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!tmpCpudCodeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (TmpCPUCode tmpCPUCode : tmpCpudCodeList) {
				SelectValue selectValue = new SelectValue();
				selectValue.setId(tmpCPUCode.getKey().longValue());
				selectValue.setValue(tmpCPUCode.getDescription());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}
	
	public List<RODDocumentSummary> getBillDetails(Long reimbursementKey){
		Query findByReimbursementKey = entityManager.createNamedQuery(
				"RODDocumentSummary.findByReimbursementKey").setParameter("reimbursementKey", reimbursementKey);
		List<RODDocumentSummary> rodDocumentDetail;
		try{
			
			rodDocumentDetail = (List<RODDocumentSummary>) findByReimbursementKey.getResultList();
			
		return rodDocumentDetail;
		}
		catch(Exception e){
			return null;
		}
	}
	
	/*public Preauth getBillDetails(Long reimbursementKey) {
		Query query = entityManager.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query.setParameter("reimbursement", reimbursementKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		
		return null;
		
		
	}*/
	
		
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	
	
	public void setUpdatePaymentDetails(PaymentProcessCpuFormDTO updatePaymentDetailsDTO){
		
		
		
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("90%");
		popup.setContent(queryDetailsTableObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
		 * 
		 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public String getPaymentTemplateNameWithVersion(String docType,Date paymentDate){
		String templateName = null;
		if(paymentDate == null){
				paymentDate = new Date();
		}
		
		String query = "SELECT TEMPLATE_NAME FROM  MAS_LETTER_TEMPLATE WHERE DOCUMENT_TYPE='" 
						+ docType 
						+"' AND TO_DATE('" 
						+ new SimpleDateFormat("dd/MM/yyyy").format(paymentDate) 
						+ "','DD/MM/YYYY') BETWEEN trunc(FROM_DT) AND trunc(TO_DT) AND ACTIVE_FLAG='Y'";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
				
		List<Object> objList = nativeQuery.getResultList();
		
		if(null != objList && !objList.isEmpty())
		{
			templateName = objList.get(0).toString();
		}
		
		return templateName;
	}
	
	public Intimation getIntimationObject(String strIntimationNo) {
		TypedQuery<Intimation> query = getEntityManager().createNamedQuery("Intimation.findByIntimationNumber", Intimation.class);
		query.setParameter("intimationNo", strIntimationNo);
		List<Intimation>	resultList = query.getResultList();
		if (null != resultList && 0 != resultList.size())
		{
			return resultList.get(0);
			
		}
		else
		{
			return null;
		}
	}
	
	public LegalHeir getlegalHeirListByTransactionKey(Long rodKey) {
		Query query = getEntityManager().createNamedQuery("LegalHeir.findByTransactionKey");
		query.setParameter("transacKey", rodKey);
		List<LegalHeir> resultList = query.getResultList();
		if(!resultList.isEmpty() && resultList != null) {
			return resultList.get(0);
		}
		return null;
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public OrganaizationUnit getInsuredOfficeNameByDivisionCode(
			String issuingOfficeCode) {
		try{
			List<OrganaizationUnit> organizationList = new ArrayList<OrganaizationUnit>();
			if (issuingOfficeCode != null) {
				Query findAll = entityManager.createNamedQuery(
						"OrganaizationUnit.findByUnitId").setParameter(
						"officeCode", issuingOfficeCode);
				organizationList = (List<OrganaizationUnit>) findAll
						.getResultList();
				if (organizationList != null && !organizationList.isEmpty()) {
					return organizationList.get(0);
				}
			}
		}
		catch(Exception e){
				e.printStackTrace();
		}
		return null;
	}
}