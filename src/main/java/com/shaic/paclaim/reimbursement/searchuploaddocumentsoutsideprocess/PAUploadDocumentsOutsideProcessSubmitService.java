package com.shaic.paclaim.reimbursement.searchuploaddocumentsoutsideprocess;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived.UploadDocumentsForAckNotReceivedPageTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.v7.data.util.BeanItemContainer;
@Stateless
public class PAUploadDocumentsOutsideProcessSubmitService {


	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(PAUploadDocumentsOutsideProcessSubmitService.class); 
		
	
	public DocumentDetails submitSearchOrUploadDocumentsForAckNotReceived(UploadDocumentDTO uploadDto){
		List<UploadDocumentDTO> uploadDocsDTO = uploadDto.getUploadDocsList();
		String userName = SHAUtils.getUserNameForDB(uploadDto.getUsername());
	
	if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
			if (null != uploadDocumentDTO.getFileType()	&& !("").equalsIgnoreCase(uploadDocumentDTO.getFileType().getValue())) {
				
				DocumentDetails documentDetails =  new DocumentDetails();
								
				documentDetails.setIntimationNumber(uploadDto.getIntimationNo());
				documentDetails.setClaimNumber(uploadDto.getClaimNo());
				if(null != uploadDocumentDTO.getReferenceNoValue() && uploadDocumentDTO.getReferenceNoValue().startsWith("ROD"))
				{
					documentDetails.setReimbursementNumber(uploadDocumentDTO.getReferenceNoValue());					
				}
				else
				{
					documentDetails.setCashlessNumber(uploadDocumentDTO.getReferenceNoValue());	
				}
				documentDetails.setFileName(uploadDocumentDTO.getFileName());
				documentDetails.setDocumentType(uploadDocumentDTO.getFileTypeValue());
				if(null != uploadDocumentDTO.getDmsDocToken()){
				documentDetails.setDocumentToken(Long.parseLong(uploadDocumentDTO.getDmsDocToken()));
				}
				documentDetails.setDocumentSource(SHAConstants.POST_PROCESS); 
				
				documentDetails.setDocSubmittedDate(new Timestamp(System.currentTimeMillis()));
				documentDetails.setDocAcknowledgementDate(new Timestamp(System.currentTimeMillis()));
				documentDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				documentDetails.setCreatedBy(userName);
				documentDetails.setKey(uploadDocumentDTO.getDocDetailsKey());
				//documentDetails.setDeletedFlag(deletedFlag);
				
				if(null!= documentDetails.getKey()){
					
					entityManager.merge(documentDetails); 
					entityManager.flush();
				}
				else 
				{	
					entityManager.persist(documentDetails);
					entityManager.flush();
				}
				
				List<UploadDocumentDTO> deletedDocsList = uploadDto.getDeletedDocumentList();
				if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {
						if(null != uploadDocumentDTO2.getDocDetailsKey())
						{
							DocumentDetails documentDetailsObj = getDocumentDetailsByKey(uploadDocumentDTO2.getDocDetailsKey());
							documentDetailsObj.setDeletedFlag("Y");
							if (null != uploadDocumentDTO2.getDocDetailsKey()) {
								//documentDetailsObj.setModifiedBy("");
								//documentDetailsObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(documentDetailsObj);
								entityManager.flush();
								log.info("------RODDocumentSummary------>"+documentDetailsObj+"<------------");
							} /*
							 * else { entityManager.persist(rodSummary);
							 * entityManager.flush(); entityManager.refresh(rodSummary);
							 * }
							 */
	
						}
					}
				}
				
				
			}
		}
	}
		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgment(String ackNo) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findAckNo");
		query.setParameter("ackNo", ackNo);

		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query.getResultList();
		
		for (DocAcknowledgement docAcknowledgement : reimbursementList) {
			entityManager.refresh(docAcknowledgement);
		}

		if (reimbursementList.size() > 0) {
			return reimbursementList.get(0);
		}

		return null;

	}
	
	
	public AcknowledgeDocument getAcknowledgeDocument(Long key)
	{
		Query query = entityManager.createNamedQuery("AcknowledgeDocument.findByKey");
		query = query.setParameter("key", key);
		List<AcknowledgeDocument> ackDocList = query.getResultList();
		if(null != ackDocList && !ackDocList.isEmpty())
		{
			entityManager.refresh(ackDocList.get(0));
			return ackDocList.get(0);
		}
		return null;
	}
	
	public DocumentDetails getDocumentDetailsByIntimationNo(String intimationNo)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findByIntimationNo");
		query = query.setParameter("intimationNumber", intimationNo);
		List<DocumentDetails> docdetailsList = query.getResultList();
		if(null != docdetailsList && !docdetailsList.isEmpty())
		{
			entityManager.refresh(docdetailsList.get(0));
			return docdetailsList.get(0);
		}
		return null;
	}

	public DocAcknowledgement getDocAckByClaim(Long key)
	{
		Query query = entityManager.createNamedQuery("DocAcknowledgement.getByClaimKey");
		query = query.setParameter("claimKey", key);
		List<DocAcknowledgement> ackDocList = query.getResultList();
		if(null != ackDocList && !ackDocList.isEmpty())
		{
			entityManager.refresh(ackDocList.get(0));
			return ackDocList.get(0);
		}
		return null;
			
		
	}
	
public BeanItemContainer<SelectValue> getReimbursementByClaim(Long claimKey)
	
	{
		Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbList = query.getResultList();
		
		
		Query preauthQuery = entityManager.createNamedQuery("Preauth.findByClaimKeyInAsc");
		preauthQuery = preauthQuery.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = preauthQuery.getResultList();
		
		BeanItemContainer<SelectValue> referenceNoContainer = new BeanItemContainer<SelectValue>(SelectValue.class);;
		
		
		
		
		if(null != reimbList && !reimbList.isEmpty())
		{
			for (Reimbursement reimbursement : reimbList) {
				entityManager.refresh(reimbursement);
				SelectValue select = new SelectValue();
				select.setId(reimbursement.getKey());
				select.setValue(reimbursement.getRodNumber());
				referenceNoContainer.addBean(select);
			}	
			
		}
		if(null != preauthList && !preauthList.isEmpty())
		{
			for (Preauth preauth : preauthList) {
				
				entityManager.refresh(preauth);
				SelectValue select = new SelectValue();
				select.setId(preauth.getKey());
				select.setValue(preauth.getPreauthId());
				referenceNoContainer.addBean(select);
				
			}
		}
		return referenceNoContainer;
	}

public List<UploadDocumentsForAckNotReceivedPageTableDTO> getPreauthDetailsForUpload(Long claimKey)
{
	Query query = entityManager.createNamedQuery("Preauth.findByClaimKeyInAsc");
	query = query.setParameter("claimkey", claimKey);
	List<Preauth> preauthList = query.getResultList(); 
	List<UploadDocumentsForAckNotReceivedPageTableDTO> tableDTOList = null;
	
	DocAcknowledgement docAck = getDocAckByClaim(claimKey);
	
	if(null != preauthList && !preauthList.isEmpty())
	{
		tableDTOList = new ArrayList<UploadDocumentsForAckNotReceivedPageTableDTO>();
		for (Preauth preauth : preauthList) {
			UploadDocumentsForAckNotReceivedPageTableDTO uploadDTO = new UploadDocumentsForAckNotReceivedPageTableDTO();
			uploadDTO.setReferenceNumber(preauth.getPreauthId());
			uploadDTO.setReferenceType(SHAConstants.SEARCH_UPLOAD_REFERENCE_TYPE);
			if(null != preauth.getTreatmentType())
				uploadDTO.setTreatmentType(preauth.getTreatmentType().getValue());
			uploadDTO.setTreatmentRemarks(preauth.getTreatmentRemarks());
			uploadDTO.setRequestedAmt(preauth.getTotalApprovalAmount());
			if(null != preauth.getStatus())
				uploadDTO.setStatus(preauth.getStatus().getProcessValue());
			if(null != docAck)
				{
				 if(null != docAck.getDocumentReceivedFromId())
					 	uploadDTO.setDocumentReceivedFrom(docAck.getDocumentReceivedFromId().getValue());
				   uploadDTO.setDocumentReceivedDate(docAck.getDocumentReceivedDate());
				}
				tableDTOList.add(uploadDTO);
			// Data population logic to be implemented.
		}
	}
	return tableDTOList;
}

public List<UploadDocumentsForAckNotReceivedPageTableDTO> getRODDetailsForUpload(Long claimKey)
{
	Query query = entityManager.createNamedQuery("Reimbursement.findByClaimKey");
	query = query.setParameter("claimKey", claimKey);
	List<Reimbursement> reimbList = query.getResultList(); 
	List<UploadDocumentsForAckNotReceivedPageTableDTO> tableDTOList = null;
	
	DocAcknowledgement docAck = getDocAckByClaim(claimKey);
	
	if(null != reimbList && !reimbList.isEmpty())
	{
		tableDTOList = new ArrayList<UploadDocumentsForAckNotReceivedPageTableDTO>();
		for (Reimbursement reimbursement : reimbList) {
			UploadDocumentsForAckNotReceivedPageTableDTO uploadDTO = new UploadDocumentsForAckNotReceivedPageTableDTO();
			
			uploadDTO.setRodNumber(reimbursement.getRodNumber());
			if(null != reimbursement && null != reimbursement.getStatus())
				uploadDTO.setStatus(reimbursement.getStatus().getProcessValue());
			if(null != docAck)
				{
				 if(null != docAck.getDocumentReceivedFromId())
					 	uploadDTO.setDocumentReceivedFrom(docAck.getDocumentReceivedFromId().getValue());
				   uploadDTO.setDocumentReceivedDate(docAck.getDocumentReceivedDate());
				}
			
			  if(null != docAck.getModeOfReceiptId())
			  {
				 	uploadDTO.setModeOfReceipt(docAck.getModeOfReceiptId().getValue());
			  }
			  uploadDTO.setBillClassification(getBillClassificationValue(reimbursement));
				tableDTOList.add(uploadDTO);
				
			// Data population logic to be implemented.
		}
	}
	return tableDTOList;
}


private String getBillClassificationValue(Reimbursement reimbursement ) {
	StringBuilder strBuilder = new StringBuilder();
	if (("Y").equals(reimbursement.getDocAcknowLedgement().getHospitalisationFlag())) {
		strBuilder.append("Hospitalization");
		strBuilder.append(",");
	}
	
	if (("Y").equals(reimbursement.getDocAcknowLedgement().getHospitalizationRepeatFlag())) {
		strBuilder.append("Hospitalization Repeat");
		strBuilder.append(",");
	}

	
	if (("Y").equals(reimbursement.getDocAcknowLedgement().getPreHospitalisationFlag())) {
		strBuilder.append("Pre-Hospitalization");
		strBuilder.append(",");
	}
	if (("Y").equals(reimbursement.getDocAcknowLedgement().getPostHospitalisationFlag())) {
		strBuilder.append("Post-Hospitalization");
		strBuilder.append(",");
	}

	if (("Y").equals(reimbursement.getDocAcknowLedgement().getPartialHospitalisationFlag())) {
		strBuilder.append("Partial-Hospitalization");
		strBuilder.append(",");
	}

	if (("Y").equals(reimbursement.getDocAcknowLedgement().getLumpsumAmountFlag())) {
		strBuilder.append("Lumpsum Amount");
		strBuilder.append(",");

	}
	/*if (("Y").equals(reimbursement.getDocAcknowLedgement().get)) {
		strBuilder.append("Add on Benefits (Hospital cash)");
		strBuilder.append(",");

	}
	if (("Y").equals(reimbursement.getDocAcknowLedgement().getAddOnBenefitsPatientCareFlag())) {
		strBuilder.append("Add on Benefits (Patient Care)");
		strBuilder.append(",");
	}*/

	return strBuilder.toString();
}


public DocumentDetails getDocumentDetailsByKey(Long docKey) {
	
	Query query = entityManager.createNamedQuery("DocumentDetails.findByKey");
	query = query.setParameter("key", docKey);
	List<DocumentDetails> docDetailsList = query.getResultList();
	if(null != docDetailsList && !docDetailsList.isEmpty()) {
		entityManager.refresh(docDetailsList.get(0));
		return docDetailsList.get(0);
	}
	return null;
}



}
