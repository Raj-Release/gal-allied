package com.shaic.reimbursement.paymentprocess.stoppaymenttracking.request;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.intimation.search.SearchIntimationFormDto;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper.ZonalMedicalReviewMapper;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuMapper;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.BancsPaymentCancel;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PaymentModeTrail;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.paymentprocess.paymentreprocess.PaymentReprocessDBService;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean.StopPaymentRequest;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.bean.StopPaymentTrails;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.PaymentValidationMapper;
import com.vaadin.ui.UI;

@Stateless
public class StopPaymentRequestService {
	
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;
	
	private final Logger log = LoggerFactory.getLogger(StopPaymentRequestService.class);
	
	public StopPaymentRequestService() {
		super();
	}
	

	public Page<StopPaymentRequestDto> showStopPaymentReuest(StopPaymentRequestFormDTO searchDto) {

		
		List<ClaimPayment> listClaimPayments = new ArrayList<ClaimPayment>(); 
		
		try{
		
			String intimationNo = null != searchDto.getIntimationNo() ? searchDto.getIntimationNo() : null;
			String UtrNumber = null != searchDto.getUtrNumber() ? searchDto.getUtrNumber() : null;

			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<ClaimPayment> criteriaQuery = criteriaBuilder.createQuery(ClaimPayment.class);
			
			Root<ClaimPayment> root = criteriaQuery.from(ClaimPayment.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			
			if(null != intimationNo && !("").equalsIgnoreCase(intimationNo))
			{
				Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationNumber"),intimationNo);
				conditionList.add(condition1);
			}
			
			if(null != UtrNumber && !("").equalsIgnoreCase(UtrNumber))
			{
				
				Predicate condition1 = criteriaBuilder.equal(root.<String>get("chequeDDNumber"),UtrNumber);
				conditionList.add(condition1);
			}
			Predicate searchStatus = criteriaBuilder.equal(root.<Status>get("statusId").<Long>get("key"), ReferenceTable.PAYMENT_SETTLED);
			conditionList.add(searchStatus);
			
			Predicate condition4 = criteriaBuilder.equal(root.<String>get("paymentType"),ReferenceTable.PAYMENT_TYPE_CHEQUE);
			conditionList.add(condition4);
			

			
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			

			final TypedQuery<ClaimPayment> typedQuery = entityManager.createQuery(criteriaQuery);
			
			int pageNumber = searchDto.getPageable().getPageNumber();
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
			@SuppressWarnings("static-access")
			List<StopPaymentRequestDto> tableDTO = PaymentRequestMapper.getInstance().getpaymentRequestTableObjects(doList);			
			
			List<StopPaymentRequestDto> finalResultList = new ArrayList<StopPaymentRequestDto>();
			
		
			
			int i=1;
			for (StopPaymentRequestDto stopPaymentRequestDto : tableDTO) {
				stopPaymentRequestDto.setSerialNumber(i);
				Intimation intimation = intimationService
						.searchbyIntimationNo(stopPaymentRequestDto.getIntimationNo());
				StopPaymentRequest paymentReqDtls = getPaymentRequestDtlsForRod(stopPaymentRequestDto.getRodNumber());
				
				if(paymentReqDtls == null){
				Claim claimByKey = claimService.getClaimsByIntimationNumber(intimation.getIntimationId());
				NewIntimationDto newIntimationDto = new NewIntimationDto();
				
				ClaimDto claimDTO = null;
				if(claimByKey != null){
					newIntimationDto = intimationService.getIntimationDto(claimByKey
							.getIntimation());
					claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
					Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(claimDTO.getKey());
					claimDTO.setCashlessAppAmt(latestPreauthByClaim != null ? latestPreauthByClaim.getTotalApprovalAmount() : 0d);
					claimDTO.setNewIntimationDto(newIntimationDto);
				}
				stopPaymentRequestDto.setPreauthDto(new PreauthDTO());
				if(stopPaymentRequestDto.getPreauthDto() != null){
					stopPaymentRequestDto.getPreauthDto().setNewIntimationDTO(newIntimationDto);
					stopPaymentRequestDto.getPreauthDto().setClaimDTO(claimDTO);
				}
				
				i++;
				finalResultList.add(stopPaymentRequestDto);
				}
			}
				
			
			Page<StopPaymentRequestDto> page = new Page<StopPaymentRequestDto>();
			searchDto.getPageable().setPageNumber(pageNumber + 1);
			if(finalResultList.size()<=10) 
				
			{
				page.setHasNext(true);
			}
			else
			{
			page.setHasNext(true);
			}
			if (finalResultList.isEmpty()) {
				searchDto.getPageable().setPageNumber(1);
			}
			
			page.setPageNumber(pageNumber);
			page.setPageItems(finalResultList);
			page.setIsDbSearch(false);
			page.setTotalRecords(finalResultList.size());
			page.setTotalList(finalResultList);
			return page;
		  }
			catch (Exception e) 
		   {
		     e.printStackTrace();
		   }
		   return null;	
		
	}


	@SuppressWarnings("null")
	public void submitStopReqeust(StopPaymentRequestDto tableDto) {
		StopPaymentRequest paymentRequest=new StopPaymentRequest();
		paymentRequest= getPaymentKey(tableDto.getStopPaymentKey());
		if(paymentRequest !=null){
			paymentRequest.setIntimationNo(tableDto.getIntimationNo()!=null? tableDto.getIntimationNo():null);
			paymentRequest.setRodNo(tableDto.getRodNumber()!=null? tableDto.getRodNumber():null);
			MastersValue master=new MastersValue();
			master.setKey(tableDto.getReasonForStopPaymentKey());
			master.setValue(tableDto.getReasonForStopPaymentValue());
			paymentRequest.setReasonStopPaymnt(master);
			paymentRequest.setIntimationKey(tableDto.getPreauthDto().getNewIntimationDTO().getKey());
			paymentRequest.setPolicyNo(tableDto.getPreauthDto().getNewIntimationDTO().getPolicy().getPolicyNumber());
			paymentRequest.setOtherRemarks(tableDto.getOtherRemarks());
			paymentRequest.setStopPaymentReamrks(tableDto.getStopPaymentReqRemarks());
			paymentRequest.setStatus("Stop payment requested");
			paymentRequest.setValidationRemarks(tableDto.getValidationRemarks());
			paymentRequest.setAction(tableDto.getAction());
			paymentRequest.setUtrNumber(tableDto.getUtrNumber());
			paymentRequest.setPaymentMode(tableDto.getReIssuingPaymentMode());
			
		}else{
			paymentRequest=new StopPaymentRequest();
			paymentRequest.setIntimationNo(tableDto.getIntimationNo()!=null? tableDto.getIntimationNo():null);
			paymentRequest.setRodNo(tableDto.getRodNumber()!=null? tableDto.getRodNumber():null);
			MastersValue master=new MastersValue();
			master.setKey(tableDto.getReasonForStopPaymentKey());
			master.setValue(tableDto.getReasonForStopPaymentValue());
			paymentRequest.setReasonStopPaymnt(master);
			paymentRequest.setIntimationKey(tableDto.getPreauthDto().getNewIntimationDTO().getKey());
			paymentRequest.setPolicyNo(tableDto.getPreauthDto().getNewIntimationDTO().getPolicy().getPolicyNumber());
			paymentRequest.setOtherRemarks(tableDto.getOtherRemarks());
			paymentRequest.setStopPaymentReamrks(tableDto.getStopPaymentReqRemarks());
			paymentRequest.setStatus("Stop payment requested");
			paymentRequest.setValidationRemarks(tableDto.getValidationRemarks());
			paymentRequest.setAction(tableDto.getAction());
			paymentRequest.setUtrNumber(tableDto.getUtrNumber());
			paymentRequest.setPaymentMode(tableDto.getReIssuingPaymentMode());
			
			
			List<UploadDocumentDTO> uploadedDocList = tableDto.getUploadedDocsTableList();
			if(uploadedDocList != null && !uploadedDocList.equals("")){
			for (UploadDocumentDTO uploadDto : uploadedDocList) {
				if (null != uploadDto.getFileType()	&& !("").equalsIgnoreCase(uploadDto.getFileType().getValue())){
					
				DocumentDetails documentDetails=new DocumentDetails();
				documentDetails.setIntimationNumber(tableDto.getIntimationNo());
				documentDetails.setClaimNumber(tableDto.getPreauthDto().getClaimDTO().getClaimId());
				documentDetails.setReimbursementNumber(uploadDto.getRodNo());
				
				documentDetails.setFileName(uploadDto.getFileName());
				documentDetails.setDocumentType(uploadDto.getFileTypeValue());
				if(null != uploadDto.getDmsDocToken()){
				documentDetails.setDocumentToken(Long.parseLong(uploadDto.getDmsDocToken()));
				}
				documentDetails.setDocumentSource("Stop Pyament Request"); 
				
				documentDetails.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
				documentDetails.setDocAcknowledgementDate(new Timestamp(System.currentTimeMillis()));
				documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				documentDetails.setCreatedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				documentDetails.setKey(uploadDto.getDocDetailsKey());
				
				if(null!= documentDetails.getKey()){
					
					entityManager.merge(documentDetails); 
					entityManager.flush();
				}
				else 
				{	
					entityManager.persist(documentDetails);
					entityManager.flush();
				}
			 }
			}
			}
			
		}
		if(tableDto.getStopPaymentKey() !=null && paymentRequest!=null && paymentRequest.getKey()!=null){
			paymentRequest.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
			paymentRequest.setModifiedDate(new Date());
			entityManager.merge(paymentRequest);
			entityManager.flush();
			entityManager.clear();
			log.info("------StopPaymentRequest------>"+paymentRequest+"<------------");	
		} else {
			paymentRequest.setCreatedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
			paymentRequest.setCreatedDate(new Date());
			entityManager.persist(paymentRequest);
			entityManager.flush();
			entityManager.clear();
			log.info("------StopPaymentRequest------>"+paymentRequest+"<------------");	
		}
		
	}


	public StopPaymentRequest getPaymentKey(Long key) {

		Query query = entityManager.createNamedQuery("StopPaymentRequest.findByKey");
		query.setParameter("Key", key);
		List<StopPaymentRequest> PaymentList = (List<StopPaymentRequest>)query.getResultList();
		
		if(PaymentList != null && ! PaymentList.isEmpty()){	
			return PaymentList.get(0);
		}else{
			return null;
		}
	
	
	}


	public Page<StopPaymentRequestDto> showStopPaymentValidation(StopPaymentRequestFormDTO searchDto) {


		
		List<StopPaymentRequest> listClaimPayments = new ArrayList<StopPaymentRequest>(); 
		
		try{
		
			String intimationNo = null != searchDto.getIntimationNo() ? searchDto.getIntimationNo() : null;
			String UtrNumber = null != searchDto.getUtrNumber() ? searchDto.getUtrNumber() : null;

			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<StopPaymentRequest> criteriaQuery = criteriaBuilder.createQuery(StopPaymentRequest.class);
			
			Root<StopPaymentRequest> root = criteriaQuery.from(StopPaymentRequest.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			
			if(null != intimationNo && !("").equalsIgnoreCase(intimationNo))
			{
				Predicate condition1 = criteriaBuilder.equal(root.<String>get("intimationNo"),  intimationNo);
				conditionList.add(condition1);
			}
			
			if(null != UtrNumber && !("").equalsIgnoreCase(UtrNumber))
			{
				Predicate condition1 = criteriaBuilder.equal(root.<String>get("utrNumber"),  UtrNumber);
				conditionList.add(condition1);
			}
			Predicate searchStatus = criteriaBuilder.equal(root.<String>get("status"), ReferenceTable.STOP_PAYMENT_REQUEST);
			conditionList.add(searchStatus);
			
			

			
			criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			

			final TypedQuery<StopPaymentRequest> typedQuery = entityManager.createQuery(criteriaQuery);
			
			int pageNumber = searchDto.getPageable().getPageNumber();
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
			
			/*if( listClaimPayments.size()>10)
			{
				listClaimPayments = typedQuery.setFirstResult(firtResult).setMaxResults(10).getResultList();
			}*/
					
		    List<StopPaymentRequest> doList = listClaimPayments;
		    
		    StopPaymentRequest paymentValidate = null;		    
		    if(null != doList && !doList.isEmpty())
		    {		    			    
		    	paymentValidate = doList.get(0);
		    entityManager.refresh(paymentValidate);
		    }
			@SuppressWarnings("static-access")
			List<StopPaymentRequestDto> tableDTO = PaymentValidationMapper.getInstance().getpaymentValidationTableObjects(doList);			
			
			List<StopPaymentRequestDto> finalResultList = new ArrayList<StopPaymentRequestDto>();
		
			finalResultList.addAll(tableDTO);
			
			int i=1;
			for (StopPaymentRequestDto stopPaymentRequestDto : tableDTO) {
				stopPaymentRequestDto.setSerialNumber(i);
				Intimation intimation = intimationService
						.searchbyIntimationNo(stopPaymentRequestDto.getIntimationNo());
				
				
				Claim claimByKey = claimService.getClaimsByIntimationNumber(intimation.getIntimationId());
				NewIntimationDto newIntimationDto = new NewIntimationDto();
				
				
				ClaimDto claimDTO = null;
				if(claimByKey != null){
					newIntimationDto = intimationService.getIntimationDto(claimByKey
							.getIntimation());
					claimDTO =  ClaimMapper.getInstance().getClaimDto(claimByKey);
					
					Preauth latestPreauthByClaim = preauthService.getLatestPreauthByClaim(claimDTO.getKey());
					claimDTO.setCashlessAppAmt(latestPreauthByClaim != null ? latestPreauthByClaim.getTotalApprovalAmount() : 0d);
					claimDTO.setNewIntimationDto(newIntimationDto);
				}
				stopPaymentRequestDto.setPreauthDto(new PreauthDTO());
				
				if(stopPaymentRequestDto.getPreauthDto() != null){
					stopPaymentRequestDto.getPreauthDto().setNewIntimationDTO(newIntimationDto);
					stopPaymentRequestDto.getPreauthDto().setClaimDTO(claimDTO);
				}
				
				i++;
			}
			
			Page<StopPaymentRequestDto> page = new Page<StopPaymentRequestDto>();
			searchDto.getPageable().setPageNumber(pageNumber + 1);
			if(finalResultList.size()<=10) 
				
			{
				page.setHasNext(true);
			}
			else
			{
			page.setHasNext(true);
			}
			if (finalResultList.isEmpty()) {
				searchDto.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(finalResultList);
			page.setIsDbSearch(false);
			page.setTotalRecords(finalResultList.size());
			return page;
		  }
			catch (Exception e) 
		   {
		     e.printStackTrace();
		   }
		   return null;	
		
	
	}


	public void submitStopValidate(StopPaymentRequestDto tableDto) {

		StopPaymentRequest paymentRequest=null;
		paymentRequest= getPaymentKey(tableDto.getStopPaymentKey());
		if(paymentRequest !=null){
			paymentRequest.setIntimationNo(tableDto.getIntimationNo());
			paymentRequest.setRodNo(tableDto.getRodNumber());
			MastersValue master=new MastersValue();
			master.setKey(tableDto.getReasonForStopPaymentKey());
			master.setValue(tableDto.getReasonForStopPaymentValue());
			paymentRequest.setReasonStopPaymnt(master);
			//paymentRequest.setReasonStopPaymnt(tableDto.getReasonForStopPaymentKey());
			//paymentRequest.setIntimationKey(tableDto.getPreauthDto().getIntimationKey());
			//paymentRequest.setPolicyNo(tableDto.getPreauthDto().getNewIntimationDTO().getPolicyNumber());
			//paymentRequest.setOtherRemarks(tableDto.getOtherRemarks());
			//paymentRequest.setStopPaymentReamrks(tableDto.getStopPaymentReqRemarks());
			paymentRequest.setStatus("Stop payment validated");
			paymentRequest.setValidationRemarks(tableDto.getStopPaymentResReamrks());
			paymentRequest.setAction(tableDto.getForActionTaken());
			paymentRequest.setUtrNumber(tableDto.getUtrNumber());
			
			paymentRequest.setPaymentCreditDate(tableDto.getPaymentCreditDate());
			paymentRequest.setPaidDate(tableDto.getPaidDate());
			
		}else{
			paymentRequest=new StopPaymentRequest();
			paymentRequest.setIntimationNo(tableDto.getIntimationNo()!=null? tableDto.getIntimationNo():null);
			paymentRequest.setRodNo(tableDto.getRodNumber()!=null? tableDto.getRodNumber():null);
			MastersValue master=new MastersValue();
			master.setKey(tableDto.getReasonForStopPaymentKey());
			master.setValue(tableDto.getReasonForStopPaymentValue());
			paymentRequest.setReasonStopPaymnt(master);
			//paymentRequest.setReasonStopPaymnt(tableDto.getReasonForStopPaymentKey());
			paymentRequest.setIntimationKey(tableDto.getPreauthDto().getIntimationKey());
			paymentRequest.setPolicyNo(tableDto.getPreauthDto().getNewIntimationDTO().getPolicyNumber());
			paymentRequest.setOtherRemarks(tableDto.getOtherRemarks());
			paymentRequest.setStopPaymentReamrks(tableDto.getStopPaymentReqRemarks());
			paymentRequest.setStatus("Stop payment validated");
			paymentRequest.setValidationRemarks(tableDto.getStopPaymentResReamrks());
			paymentRequest.setAction(tableDto.getForActionTaken());
			paymentRequest.setUtrNumber(tableDto.getUtrNumber());
			
			paymentRequest.setPaymentCreditDate(tableDto.getPaymentCreditDate());
			paymentRequest.setPaidDate(tableDto.getPaidDate());
			
		}
		if(tableDto.getStopPaymentKey() !=null && paymentRequest!=null && paymentRequest.getKey()!=null){
			paymentRequest.setModifiedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
			paymentRequest.setModifiedDate(new Date());
			entityManager.merge(paymentRequest);
			entityManager.flush();
			entityManager.clear();
			log.info("------StopPaymentRequest------>"+paymentRequest+"<------------");	
		} else {
			paymentRequest.setCreatedBy(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
			paymentRequest.setCreatedDate(new Date());
			entityManager.persist(paymentRequest);
			entityManager.flush();
			entityManager.clear();
			log.info("------StopPaymentRequest------>"+paymentRequest+"<------------");	
		}
		
	
		
	}
	
	private StopPaymentRequest getPaymentRequestDtls(Long intimationKey){


		Query query = entityManager.createNamedQuery("StopPaymentRequest.findByintimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<StopPaymentRequest> PaymentList = (List<StopPaymentRequest>)query.getResultList();
		
		if(PaymentList != null && ! PaymentList.isEmpty()){	
			return PaymentList.get(0);
		}else{
			return null;
		}
	
	
	
	}
	private StopPaymentRequest getPaymentRequestDtlsForRod(String rodNo){


		Query query = entityManager.createNamedQuery("StopPaymentRequest.findByRodNumber");
		query.setParameter("rodNo", rodNo);
		List<StopPaymentRequest> PaymentList = (List<StopPaymentRequest>)query.getResultList();
		
		if(PaymentList != null && ! PaymentList.isEmpty()){	
			return PaymentList.get(0);
		}else{
			return null;
		}
	
	
	
	}
	

	public List<StopPaymentTrails> getStopPaymentTrailByRodNo(String rodNo) {
		Query query = entityManager.createNamedQuery(
				"StopPaymentTrails.findByrodNo").setParameter("rodNo", rodNo);
		List<StopPaymentTrails> stopPaymentTrail = query.getResultList();
		if (stopPaymentTrail != null && !stopPaymentTrail.isEmpty()) {
			return stopPaymentTrail;
		}
		return null;
	}

}
