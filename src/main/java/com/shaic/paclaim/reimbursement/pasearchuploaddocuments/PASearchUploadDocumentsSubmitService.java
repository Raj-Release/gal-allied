package com.shaic.paclaim.reimbursement.pasearchuploaddocuments;

import java.sql.Timestamp;
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
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.AcknowledgeDocument;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class PASearchUploadDocumentsSubmitService {



	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(PASearchUploadDocumentsSubmitService.class); 
	
	public AcknowledgeDocument submitSearchOrUploadDocuments(UploadDocumentDTO uploadDto)
	{
	
	List<UploadDocumentDTO> uploadDocsDTO = uploadDto.getUploadDocsList();
		String userName = SHAUtils.getUserNameForDB(uploadDto.getUsername());
	
	if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
			if (null != uploadDocumentDTO.getFileType()	&& !("").equalsIgnoreCase(uploadDocumentDTO.getFileType().getValue())) {
				AcknowledgeDocument acknowledgeDocument = null;
				if(null != uploadDocumentDTO.getAckDocKey())
				{
					acknowledgeDocument = getAcknowledgeDocument(uploadDocumentDTO.getAckDocKey());
				}
				else
				{
					acknowledgeDocument = new AcknowledgeDocument();
				}
				acknowledgeDocument.setBillNumber(uploadDocumentDTO.getBillNo());
				acknowledgeDocument.setBillAmount(uploadDocumentDTO.getBillValue());
				//acknowledgeDocument.setBillDate(new Timestamp(System.currentTimeMillis()));
				acknowledgeDocument.setNoOfItems(uploadDocumentDTO.getNoOfItems());
				acknowledgeDocument.setFileName(uploadDocumentDTO.getFileName());

				acknowledgeDocument.setCreatedBy(userName);
				/**
				 * 
				 * */
				
				
				if(null != uploadDocumentDTO.getFileType())
				{
					MastersValue masValue = new MastersValue();
					masValue.setKey( uploadDocumentDTO.getFileType().getId());
					acknowledgeDocument.setFileType(masValue);
				}
				
				
				
				acknowledgeDocument.setCreatedDate(new Timestamp(System.currentTimeMillis()));				
				acknowledgeDocument.setDocToken(uploadDocumentDTO.getDmsDocToken());
				acknowledgeDocument.setModifiedBy(userName);
				acknowledgeDocument.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				acknowledgeDocument.setDeleteFlag(SHAConstants.N_FLAG);
				
				DocAcknowledgement docAcknowledgement = getDocAcknowledgment(uploadDto.getAcknowledgementNo());
				if(null != docAcknowledgement)
						{
						acknowledgeDocument.setDocAcknowledgement(docAcknowledgement);
						acknowledgeDocument.setClaimKey(docAcknowledgement.getClaim().getKey());
						}
				
				if(null != acknowledgeDocument.getKey())
				{
					entityManager.merge(acknowledgeDocument);
					entityManager.flush();
				}
				else
				{
				
					entityManager.persist(acknowledgeDocument);
					entityManager.flush();
				}
				//	log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
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
					if(reimbursement.getStatus().getKey().equals(ReferenceTable.CREATE_ROD_CLOSED) || reimbursement.getStatus().getKey().equals(ReferenceTable.BILL_ENTRY_CLOSED)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.ZONAL_REVIEW_CLOSED) || reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_CLOSED)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_CLOSED) || reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_CLOSED)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS) 
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS)|| reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.CLAIM_REQUEST_REJECT_STATUS) || reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_SETTLED)
							|| reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)|| reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED)){
				entityManager.refresh(reimbursement);
				SelectValue select = new SelectValue();
				select.setId(reimbursement.getKey());
				select.setValue(reimbursement.getRodNumber());
				referenceNoContainer.addBean(select);
					}
			}	
			
		}
		if(null != preauthList && !preauthList.isEmpty())
		{
			for (Preauth preauth : preauthList) {
				if((! preauth.getStatus().getKey().equals(ReferenceTable.CLAIM_REOPENED_STATUS) && (!preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)))
						|| (preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_SUGGEST_REJECTION_STATUS))
						|| (preauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_SUGGEST_REJECTION_STATUS)) || (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS))
						||(preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)) || (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS))
						|| (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_DENIAL_OF_CASHLESS_STATUS)) || (preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS))
						|| (preauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)) || (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS))
						|| (preauth.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) || (preauth.getStatus().getKey().equals(ReferenceTable.PAYMENT_SETTLED))
						|| (preauth.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))){
				entityManager.refresh(preauth);
				SelectValue select = new SelectValue();
				select.setId(preauth.getKey());
				select.setValue(preauth.getPreauthId());
				referenceNoContainer.addBean(select);
				}
			}
		}
		return referenceNoContainer;
	}

}
