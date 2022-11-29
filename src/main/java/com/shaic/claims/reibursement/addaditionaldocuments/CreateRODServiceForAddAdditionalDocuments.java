/**
 * 
 */
package com.shaic.claims.reibursement.addaditionaldocuments;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDetailsDTO;
import com.shaic.claim.reimbursement.dto.OtherClaimDiagnosisDTO;
import com.shaic.claim.reimbursement.dto.ZonalReviewUpdateHospitalDetailsDTO;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.mapper.ZonalMedicalReviewMapper;
/*import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.payloadbo.PayloadBOType;
 import com.oracle.xmlns.bpm.bpmobject.claimreimbcabusinessobjects.rod.RODType;*/
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.mapper.AcknowledgeDocumentReceivedMapper;
import com.shaic.claim.rod.wizard.pages.CreateRODMapper;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitalisation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasBillDetailsType;
import com.shaic.domain.MasIrdaLevel1;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PhysicalDocumentVerification;
import com.shaic.domain.PhysicalDocumentVerificationDetails;
import com.shaic.domain.PostHospitalisation;
import com.shaic.domain.PreHospitalisation;
import com.shaic.domain.PreviousClaimedHistory;
import com.shaic.domain.PreviousClaimedHospitalization;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.Status;
import com.shaic.domain.UpdateHospital;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.ImplantDetails;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementService;
/*import com.shaic.ims.bpm.claim.modelv2.HumanTask;
 import com.shaic.ims.bpm.claim.servicev2.InvokeHumanTaskServiceV2;*/
import com.vaadin.ui.Notification;

/**
 * @author ntv.vijayar
 *
 */
@Stateless
public class CreateRODServiceForAddAdditionalDocuments {

	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;

	@PersistenceContext
	protected EntityManager entityManager;

	public DocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		List<DocAcknowledgement> docAcknowledgementList = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		docAcknowledgementList = query.getResultList();
		if (null != docAcknowledgementList && !docAcknowledgementList.isEmpty()) {
			entityManager.refresh(docAcknowledgementList.get(0));
			return docAcknowledgementList.get(0);
			//docAcknowledgement = (DocAcknowledgement) query.getSingleResult();
			
		}
		return null;
		//return docAcknowledgement;
	}
	
	public DocAcknowledgement getAcknowledgementByClaimKey(Long claimKey) {
		List<DocAcknowledgement> docAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claimKey);
		if (null != query) {
			docAcknowledgement = (List<DocAcknowledgement>) query.getResultList();
		}
		if(docAcknowledgement != null && !docAcknowledgement.isEmpty()){
			return docAcknowledgement.get(docAcknowledgement.size()-1);
		}
		return null;
	}

	public List<DocumentCheckListDTO> getRODDocumentList(
			MasterService masterService, DocAcknowledgement objDocAck) {

		List<DocumentCheckListDTO> documentCheckListDTO = null;
		List<RODDocumentCheckList> rodDocCheckList = masterService
				.getRODDocumentListValues(objDocAck);

		if (null != rodDocCheckList && !rodDocCheckList.isEmpty()) {
			/*
			 * for (RODDocumentCheckList rodDocumentCheckList : rodDocCheckList)
			 * {
			 * 
			 * }
			 */
			CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
			documentCheckListDTO = createRODMapper
					.getRODDocumentCheckList(rodDocCheckList);
		}

		return documentCheckListDTO;
	}

	public Preauth getLatestPreauthForClaim(Long claimKey) {
		Preauth preauth = null;
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query = query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = query.getResultList();
		if (null != preauthList && !preauthList.isEmpty()) {
			int size = preauthList.size();
			preauth = preauthList.get(size - 1);
		}
		return preauth;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitROD(ReceiptOfDocumentsDTO rodDTO) {
		try {
			saveRODValues(rodDTO);

		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Already Submitted. Please Try Another Record.");
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitBillEntryValues(ReceiptOfDocumentsDTO rodDTO) {
		try {
//			CreateRODMapper rodMapper = new CreateRODMapper();
			DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());
			//docAck = getAcknowledgementByClaimKey(docAck.getClaim().getKey());

			DocAcknowledgement docAckNew = new DocAcknowledgement();

			if (docAck != null) {
				
				if(null != rodDTO.getClaimDTO())
				{
					DocAcknowledgement docAckObj = getDocAckByClaim(rodDTO.getClaimDTO().getKey());

					if (null != docAckObj.getAcknowledgeNumber()) {
						String ackNumber = docAckObj.getAcknowledgeNumber();
						String[] ack1 = ackNumber.split("/");
						String ackNo = "";
						for (int index = 0; index < ack1.length - 1; index++) {
							ackNo = ackNo + ack1[index] + "/";
						}
						
						String ack2 = ack1[ack1.length - 1];
						Long ack3 = Long.parseLong(ack2);
						ack3++;
						ackNo = ackNo + ack3.toString();
						docAckNew.setAcknowledgeNumber(ackNo);
					}	
				}

				/*if (docAck.getAcknowledgeNumber() != null) {
					String ackNumber = docAck.getAcknowledgeNumber();
					String[] ack1 = ackNumber.split("/");
					String ackNo = "";
					for (int index = 0; index < ack1.length - 1; index++) {
						ackNo = ackNo + ack1[index] + "/";
					}
					
					String ack2 = ack1[ack1.length - 1];
					Long ack3 = Long.parseLong(ack2);
					ack3++;
					
					ackNo = ackNo + ack3.toString();						
					docAckNew.setAcknowledgeNumber(ackNo);
				}*/

				if (docAck.getHospitalisationFlag() != null) {
					docAckNew.setHospitalisationFlag(docAck
							.getHospitalisationFlag());
				}

				if (docAck.getPreHospitalisationFlag() != null) {
					docAckNew.setPreHospitalisationFlag(docAck
							.getPreHospitalisationFlag());
				}

				if (docAck.getPreHospitalisationFlag() != null) {
					docAckNew.setPreHospitalisationFlag(docAck
							.getPreHospitalisationFlag());
				}

				if (docAck.getPostHospitalisationFlag() != null) {
					docAckNew.setPostHospitalisationFlag(docAck
							.getPostHospitalisationFlag());
				}

				if (docAck.getClaim() != null) {
					docAckNew.setClaim(getClaimByClaimKey(docAck.getClaim().getKey()));
				}

				/*
				 * if (rodDTO.getAcknowledgementNumber() != null) {
				 * docAckNew.setAcknowledgeNumber(rodDTO
				 * .getAcknowledgementNumber().toString()); }
				 */

				if (docAck.getRodKey() != null) {
					docAckNew.setRodKey(docAck.getRodKey());
				}

				if (docAck.getReconsiderationRequest() != null) {
					docAckNew.setReconsiderationRequest(docAck
							.getReconsiderationRequest());
				}

				if (docAck.getPartialHospitalisationFlag() != null) {
					docAckNew.setPartialHospitalisationFlag(docAck
							.getPartialHospitalisationFlag());
				}

				if (docAck.getLumpsumAmountFlag() != null) {
					docAckNew.setLumpsumAmountFlag(docAck
							.getLumpsumAmountFlag());
				}

				if (docAck.getHospitalCashFlag() != null) {
					docAckNew.setHospitalCashFlag(docAck.getHospitalCashFlag());
				}

				if (docAck.getPatientCareFlag() != null) {
					docAckNew.setPatientCareFlag(docAck.getPatientCareFlag());
				}

				if (docAck.getActiveStatus() != null) {
					docAckNew.setActiveStatus(docAck.getActiveStatus());
				}
				if (docAck.getOfficeCode() != null) {
					docAckNew.setOfficeCode(docAck.getOfficeCode());
				}
				if (docAck.getStage() != null) {
					docAckNew.setStage(docAck.getStage());
				}
				if (docAck.getStatus() != null) {
					Status status = getStatusByKey(ReferenceTable.ADD_ADDITIONAL_DOCUMENTS_STATUS);
					if(null != status)
						//docAckNew.setStatus(docAck.getStatus());
						/**
						 * The below code was added after
						 * a new status was introduced for
						 * add additional documents.
						 * */
						docAckNew.setStatus(status);
				}
				if (rodDTO.getStrUserName() != null) {
					docAckNew.setCreatedBy(rodDTO.getStrUserName());
				}
				if (docAck.getCreatedDate() != null) {
					docAckNew.setCreatedDate(docAck.getCreatedDate());
				}
				if (docAck.getModifiedBy() != null) {
//					docAckNew.setModifiedBy(docAck.getModifiedBy());
				}
				if (docAck.getModifiedDate() != null) {
//					docAckNew.setModifiedDate(docAck.getModifiedDate());
				}
				if (docAck.getHospitalizationClaimedAmount() != null) {
					docAckNew.setHospitalizationClaimedAmount(docAck
							.getHospitalizationClaimedAmount());
				}
				if (docAck.getPreHospitalizationClaimedAmount() != null) {
					docAckNew.setPreHospitalizationClaimedAmount(docAck
							.getPreHospitalizationClaimedAmount());
				}
				if (docAck.getPostHospitalizationClaimedAmount() != null) {
					docAckNew.setPostHospitalizationClaimedAmount(docAck
							.getPostHospitalizationClaimedAmount());
				}
				if (docAck.getHospitalizationRepeatFlag() != null) {
					docAckNew.setHospitalizationRepeatFlag(docAck
							.getHospitalizationRepeatFlag());
				}
				
				//IMSSUPPOR-35917 support Fix  hospital cash product
				if (docAck.getProdHospBenefitFlag() != null) {
					docAckNew.setProdHospBenefitFlag(docAck
							.getProdHospBenefitFlag());
				}
				
				if (docAck.getProdHospBenefitClaimedAmount() != null) {
					docAckNew.setProdHospBenefitClaimedAmount(docAck
							.getProdHospBenefitClaimedAmount());
				}

			}

			if (rodDTO.getDocumentDetails() != null
					&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null) {
				MastersValue docReceivedFrom = new MastersValue();
				docReceivedFrom.setKey(rodDTO.getDocumentDetails()
						.getDocumentsReceivedFrom().getId());
				docReceivedFrom.setValue(rodDTO.getDocumentDetails()
						.getDocumentsReceivedFrom().getValue());
				docAckNew.setDocumentReceivedFromId(docReceivedFrom);
			}

			if (rodDTO.getDocumentDetails() != null
					&& rodDTO.getDocumentDetails().getModeOfReceipt() != null) {
				MastersValue modeOfReceipt = new MastersValue();
				modeOfReceipt.setKey(rodDTO.getDocumentDetails()
						.getModeOfReceipt().getId());
				modeOfReceipt.setValue(rodDTO.getDocumentDetails()
						.getModeOfReceipt().getValue());
				docAckNew.setModeOfReceiptId(modeOfReceipt);
			}

			if (rodDTO.getDocumentDetails().getDocumentsReceivedDate() != null) {
				docAckNew.setDocumentReceivedDate(rodDTO.getDocumentDetails()
						.getDocumentsReceivedDate());
			}
			
			if(null != rodDTO.getDocumentDetails().getAcknowledgmentContactNumber())
			{
				docAckNew.setInsuredContactNumber(rodDTO.getDocumentDetails().getAcknowledgmentContactNumber());
			}

			if (rodDTO.getDocumentDetails().getEmailId() != null) {
				docAckNew.setInsuredEmailId(rodDTO.getDocumentDetails()
						.getEmailId());
			}

			if (rodDTO.getDocumentDetails().getAdditionalRemarks() != null) {
				docAckNew.setAdditionalRemarks(rodDTO.getDocumentDetails()
						.getAdditionalRemarks());
			}
			
			if (docAck.getReconsiderationRequest() != null) {
				docAckNew.setReconsiderationRequest(docAck
						.getReconsiderationRequest());
				if(docAck.getPaymentCancellationFlag()!=null){
					docAckNew.setPaymentCancellationFlag(docAck.getPaymentCancellationFlag());
				}
			}
			
			if(null != rodDTO.getDocumentDetails().getSourceOfDocumentValue()){
				
				docAckNew.setSourceOfDocument(rodDTO.getDocumentDetails().getSourceOfDocumentValue());
			}
			Boolean flag = true;
			int increment = 0;
			
			Reimbursement reimbursement = null;
			reimbursement = reimbursementService.getReimbursementbyRod(
					rodDTO.getDocumentDetails().getRodKey(), entityManager);

			if(null != rodDTO.getDocumentDetails().getSourceOfDocumentValue() &&
					(SHAConstants.SOURCE_DOC_INSURED.equalsIgnoreCase(rodDTO.getDocumentDetails().getSourceOfDocumentValue()))){
			do {
				if(increment>10){
					flag = false;
				}
				try {
					entityManager.persist(docAckNew);
					entityManager.flush();
					entityManager.clear();
					flag = false;
				} catch (Exception e) {
					e.printStackTrace();
					increment++;
					if (docAckNew.getAcknowledgeNumber() != null) {
						String ackNumber = docAckNew.getAcknowledgeNumber();
						String[] ack1 = ackNumber.split("/");
						String ackNo = "";
						for (int index = 0; index < ack1.length - 1; index++) {
							ackNo = ackNo + ack1[index] + "/";
						}
						
						String ack2 = ack1[ack1.length - 1];
						Long ack3 = Long.parseLong(ack2);
						ack3++;
						
						ackNo = ackNo + ack3.toString();						
						docAckNew.setAcknowledgeNumber(ackNo);
					}
					
				}
			} while (flag);
		
			//entityManager.refresh(docAckNew);
			docAck = getDocAcknowledgementBasedOnKey(docAckNew.getKey());


			if (rodDTO.getDocumentDetails() != null
					&& rodDTO.getDocumentDetails().getRodKey() != null) {				
				reimbursement.setDocAcknowLedgement(docAck);
				entityManager.merge(reimbursement);
			}
		}
			List<DocumentCheckListDTO> docCheckList = rodDTO.getDocumentDetails()
					.getDocumentCheckList();
			//if (rodDTO.getDocumentDetails().getDocumentCheckList().size() > 0) {
			if(null != docCheckList && !docCheckList.isEmpty()) {
				AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();
//				AcknowledgeDocumentReceivedMapper ackDocRecMapper = new AcknowledgeDocumentReceivedMapper();
				for (DocumentCheckListDTO docCheckListDTO : docCheckList) {
					// if(null != docCheckListDTO.getNoOfDocuments())
					if (null != docCheckListDTO.getReceivedStatus()
							&& !("").equalsIgnoreCase(docCheckListDTO
									.getReceivedStatus().getValue())) {
						/*
						 * if(null != docCheckListDTO.getDocChkLstKey()) {
						 * RODDocumentCheckList documentDetails =
						 * findRODDocumentCheckListByKey
						 * (docCheckListDTO.getDocChkLstKey());
						 * entityManager.merge(documentDetails);
						 * entityManager.flush(); } else
						 */
						// {
						RODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
								.getRODDocumentCheckList(docCheckListDTO);
						rodDocumentCheckList.setDocAcknowledgement(docAck);
						// findRODDocumentCheckListByKey(masterService);
						entityManager.persist(rodDocumentCheckList);
						entityManager.flush();
						entityManager.clear();
						//log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
						// }
					}
				}
				
				/*for (DocumentCheckListDTO documentCheckListDTO : rodDTO
						.getDocumentDetails().getDocumentCheckList()) {
					RODDocumentCheckList rodDocumentList = new RODDocumentCheckList();
					if (docAck != null) {
						rodDocumentList.setDocAcknowledgement(docAck);
					}
					if (documentCheckListDTO.getDocTypeId() != null) {
						rodDocumentList.setDocumentTypeId(documentCheckListDTO
								.getDocTypeId());
					}
					if (documentCheckListDTO.getReceivedStatus() != null) {
						MastersValue receivedStatus = new MastersValue();
						receivedStatus.setKey(documentCheckListDTO
								.getReceivedStatus().getId());
						receivedStatus.setValue(documentCheckListDTO
								.getReceivedStatus().getValue());
						rodDocumentList.setReceivedStatusId(receivedStatus);
					}
					if (documentCheckListDTO.getNoOfDocuments() != null) {
						rodDocumentList.setNoOfDocuments(documentCheckListDTO
								.getNoOfDocuments());
					}
					if (documentCheckListDTO.getRemarks() != null) {
						rodDocumentList.setRemarks(documentCheckListDTO
								.getRemarks());
					}
					if (documentCheckListDTO.getCreatedDate() != null) {
						rodDocumentList.setCreatedDate(documentCheckListDTO
								.getCreatedDate());
					}
					entityManager.persist(rodDocumentList);
					entityManager.flush();
					entityManager.refresh(rodDocumentList);
				}*/

				List<UploadDocumentDTO> uploadDocsDTO = rodDTO
						.getUploadDocsList();
				for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {

					saveBillEntryValues(uploadDocumentDTO);
					
					
					/*

					RODDocumentSummary rodSummary = CreateRODMapper
							.getDocumentSummary(uploadDocumentDTO);
					rodSummary.setReimbursement(reimbursement);
					if(null != rodSummary.getKey())
					{
						entityManager.merge(rodSummary);
					}
					else
					{
						entityManager.persist(rodSummary);
					}
					entityManager.flush();

					
					 * if (null != uploadDocumentDTO.getDocSummaryKey()) {
					 * entityManager.merge(rodSummary); entityManager.flush(); }
					 * else { entityManager.persist(rodSummary);
					 * entityManager.flush(); entityManager.refresh(rodSummary);
					 * }
					 

					List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
							.getBillEntryDetailList();
					if (null != billEntryDetailsList
							&& !billEntryDetailsList.isEmpty()) {
						for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

							if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
									billEntryDetailsDTO.getBillNo())) {
								RODBillDetails rodBillDetails = rodMapper
										.getRODBillDetails(billEntryDetailsDTO);
								rodBillDetails
										.setRodDocumentSummaryKey(rodSummary);
								rodBillDetails.setDeletedFlag(SHAConstants.N_FLAG);
								if(null != rodBillDetails.getKey())
								{
									entityManager.merge(rodBillDetails);
								}
								else
								{
									entityManager.persist(rodBillDetails);
								}
								entityManager.flush();

							}
						}
					}

				*/}
				
				List<UploadDocumentDTO> deletedDocsList = rodDTO.getUploadDocumentsDTO().getDeletedDocumentList();
				if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

						
						
						RODDocumentSummary rodSummaryObj = CreateRODMapper.getInstance()
								.getDocumentSummary(uploadDocumentDTO2);
						rodSummaryObj.setReimbursement(reimbursement);
						rodSummaryObj.setDeletedFlag("Y");

						if (null != uploadDocumentDTO2.getDocSummaryKey()) {
							rodSummaryObj.setModifiedBy(uploadDocumentDTO2.getStrUserName());
							rodSummaryObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							entityManager.merge(rodSummaryObj);
							entityManager.flush();
							entityManager.clear();
							//log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
						} /*
						 * else { entityManager.persist(rodSummary);
						 * entityManager.flush(); entityManager.refresh(rodSummary);
						 * }
						 */
						
						DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummaryObj.getDocumentToken());
						if(null != details)
						{
							details.setDeletedFlag("Y");
							entityManager.merge(details);
							entityManager.flush();
							entityManager.clear();
							//log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
						}
					}
				}
				
				
				if(null != rodDTO.getDocFilePath() && !("").equalsIgnoreCase(rodDTO.getDocFilePath()))
				{
					WeakHashMap dataMap = new WeakHashMap();
					dataMap.put("intimationNumber",docAck.getClaim().getIntimation().getIntimationId());
					Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
					if(null != objClaim)
					{
						dataMap.put("claimNumber",objClaim.getClaimId());
						if(null != objClaim.getClaimType())
						{
							if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
								{
									Preauth preauth = getPreauthClaimKey( objClaim.getKey());
									if(null != preauth)
										dataMap.put("cashlessNumber", preauth.getPreauthId());
								}
						}
					}
					dataMap.put("filePath", rodDTO.getDocFilePath());
					dataMap.put("docType", rodDTO.getDocType());
					dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
					dataMap.put("createdBy", rodDTO.getStrUserName());
					SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Already Submitted. Please Try Another Record.");
		}

	}
	
	
	public Boolean saveBillEntryValues(UploadDocumentDTO uploadDocumentDTO)
	{
//		CreateRODMapper rodMapper = new CreateRODMapper();
		CreateRODMapper rodMapper = CreateRODMapper.getInstance();
			try{
				if(null != uploadDocumentDTO)
				{
					Reimbursement reimbursement = getReimbursementObjectByKey(uploadDocumentDTO.getRodKey());
					RODDocumentSummary rodSummary = null;
					if(null != uploadDocumentDTO.getDocSummaryKey())
					{
						 
								RODDocumentSummary rodSummaryObj = getRODDocumentSummaryDetailsByKey(uploadDocumentDTO.getDocSummaryKey());
								 rodSummary = CreateRODMapper
											.getDocumentSummary(uploadDocumentDTO);
								 rodSummary.setCreatedBy(rodSummaryObj.getCreatedBy());
								 rodSummary.setCreatedDate(rodSummaryObj.getCreatedDate());
					}
					else
					{
						 rodSummary = CreateRODMapper
									.getDocumentSummary(uploadDocumentDTO);
					}
				
				
				rodSummary.setReimbursement(reimbursement);
				rodSummary.setDeletedFlag("N");
				if (null != uploadDocumentDTO.getDocSummaryKey()) {
					
					rodSummary.setModifiedBy(uploadDocumentDTO.getStrUserName());
					rodSummary.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					
					entityManager.merge(rodSummary);
					entityManager.flush();
					//log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
				} else {
					
					rodSummary.setCreatedBy(uploadDocumentDTO.getStrUserName());
					rodSummary.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(rodSummary);
					entityManager.flush();
					//log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
					entityManager.refresh(rodSummary);
				}
				
				uploadDocumentDTO.setDocSummaryKey(rodSummary.getKey());

				List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
						.getBillEntryDetailList();
				if (null != billEntryDetailsList
						&& !billEntryDetailsList.isEmpty()) {
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

						if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
								billEntryDetailsDTO.getBillNo())) {
							/*if(isQueryReceived){

							}*/
							RODBillDetails  rodBillDetails = null;
							if(null != billEntryDetailsDTO.getBillDetailsKey())
							{
								 rodBillDetails = getBillEntryDetailsByKey( billEntryDetailsDTO.getBillDetailsKey());
							}
							else
							{
							 rodBillDetails = rodMapper
										.getRODBillDetails(billEntryDetailsDTO);
							}
							
							rodBillDetails.setRodDocumentSummaryKey(rodSummary);
							rodBillDetails.setDeletedFlag("N");
							rodBillDetails.setReimbursementKey(reimbursement.getKey());
							
							if (uploadDocumentDTO.getBillEntryDetailsDTO()
									.getZonalRemarks() != null) {
								rodSummary.setZonalRemarks(uploadDocumentDTO
										.getBillEntryDetailsDTO().getZonalRemarks());
							} else {
								if (billEntryDetailsDTO.getZonalRemarks() != null) {
									rodSummary.setZonalRemarks(billEntryDetailsDTO
											.getZonalRemarks());
								}
							}
							if (uploadDocumentDTO.getBillEntryDetailsDTO()
									.getCorporateRemarks() != null) {
								rodSummary.setCorporateRemarks(uploadDocumentDTO
										.getBillEntryDetailsDTO().getCorporateRemarks());
							} else {
								if (billEntryDetailsDTO.getCorporateRemarks() != null) {
									rodSummary.setCorporateRemarks(billEntryDetailsDTO
											.getCorporateRemarks());
								}
							}
							if (uploadDocumentDTO.getBillEntryDetailsDTO()
									.getBillingRemarks() != null) {
								rodSummary.setBillingRemarks(uploadDocumentDTO
										.getBillEntryDetailsDTO().getBillingRemarks());
							} else {
								if (billEntryDetailsDTO.getBillingRemarks() != null) {
									rodSummary.setBillingRemarks(billEntryDetailsDTO
											.getBillingRemarks());
								}
							}
							
							
							if(null != rodBillDetails.getKey())
							{
								//rodBillDetails.setM(strUserName);
								// modified date and time field is not available in bill details table.
								//rodBillDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								entityManager.merge(rodBillDetails);
								entityManager.flush();
								//log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}
							else
							{
								// created date and time field is not available in bill details table.
								entityManager.persist(rodBillDetails);
								entityManager.flush();
								//log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							}
							entityManager.clear();
							billEntryDetailsDTO.setBillDetailsKey(rodBillDetails.getKey());

						}
					}
				}
				List<BillEntryDetailsDTO> deletedBillEntryDetailsList = uploadDocumentDTO
						.getDeletedBillList();
				if (null != deletedBillEntryDetailsList
						&& !deletedBillEntryDetailsList.isEmpty()) {
					for (BillEntryDetailsDTO billEntryDetailsDTO : deletedBillEntryDetailsList) {

						// if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(billEntryDetailsDTO.getBillNo()))
						{
							@SuppressWarnings("static-access")
							RODBillDetails rodBillDetails = rodMapper
									.getRODBillDetails(billEntryDetailsDTO);
							rodBillDetails.setRodDocumentSummaryKey(rodSummary);
							rodBillDetails.setDeletedFlag("Y");
							/*
							 * rodSummary.setZonalRemarks(uploadDocumentDTO.
							 * getBillEntryDetailsDTO().getZonalRemarks());
							 * rodSummary.setCorporateRemarks(uploadDocumentDTO.
							 * getBillEntryDetailsDTO().getCorporateRemarks());
							 * rodSummary.setBillingRemarks(uploadDocumentDTO.
							 * getBillEntryDetailsDTO().getBillingRemarks());
							 */
							/*
							 * entityManager.merge(rodSummary);
							 * entityManager.flush();
							 */

							if (rodBillDetails.getKey() != null) {
								entityManager.merge(rodBillDetails);
							} /*
							 * else { entityManager.persist(rodBillDetails); }
							 */
						//	log.info("------RODBillDetails------>"+rodBillDetails+"<------------");
							entityManager.flush();
							entityManager.clear();
						}
					}
				}
				
				
				
				/**
				 * Below method is added for enabling save bill entry
				 * option in bill entry pop up. Same is now resued in save also.
				 * */
				//saveBillEntryValues(uploadDocumentDTO,rodSummary);

			}
			return true;
		}
		catch(Exception e)
		{
			//log.error("Error occured while saving bill entry details"+e);
			e.printStackTrace();
			return false;
		}
	}
	
	public RODDocumentSummary getRODDocumentSummaryDetailsByKey(Long key) {
		List<RODDocumentSummary> rodDocSummary = null;
		//RODDocumentSummary rodDocSummaryObj = null;
		List<UploadDocumentDTO> uploadDocsDTO = new ArrayList<UploadDocumentDTO>();
		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByKey");
		query.setParameter("primaryKey", key);
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			rodDocSummary = query.getResultList();
			if(null != rodDocSummary && !rodDocSummary.isEmpty())
			{
				entityManager.refresh(rodDocSummary.get(0));
				return rodDocSummary.get(0);
			}
		
		
		}
		return null;
	}
	
	public RODBillDetails getBillEntryDetailsByKey(Long billEntryDetailsKey)
	{
		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByKey");
		query = query.setParameter("primaryKey", billEntryDetailsKey);
		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();
		if(null != billDetails && !billDetails.isEmpty())
		{
				entityManager.refresh(billDetails.get(0));
				return billDetails.get(0);
		}
		return null;

	}

	@SuppressWarnings({ "unchecked" })
	public List<MasBillDetailsType> getBillDetails(Long billCategory) {

		Query query = entityManager
				.createNamedQuery("MasBillDetailsType.findByBillClassification");
		query = query.setParameter("billClassificationKey", billCategory);

		List<MasBillDetailsType> billDetails = (List<MasBillDetailsType>) query
				.getResultList();

		return billDetails;

	}

	@SuppressWarnings({ "unchecked" })
	public List<RODBillDetails> getBillEntryDetails(Long billDocumentSummaryKey) {

		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByRodDocumentSummaryKey");
		query = query.setParameter("summaryKey", billDocumentSummaryKey);

		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();

		return billDetails;

	}

	public List<MasIrdaLevel1> getIrdaLevelOneList() {

		Query query = entityManager.createNamedQuery("MasIrdaLevel1.findAll");

		List<MasIrdaLevel1> billDetails = (List<MasIrdaLevel1>) query
				.getResultList();

		return billDetails;

	}

	@SuppressWarnings("unchecked")
	public List<PreHospitalisation> getPreHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("PreHospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<PreHospitalisation> billDetails = (List<PreHospitalisation>) query
				.getResultList();
		if (billDetails != null && !billDetails.isEmpty()) {
			for (PreHospitalisation preHospitalisation : billDetails) {
				entityManager.refresh(preHospitalisation);
			}
		}

		return billDetails;

	}

	@SuppressWarnings("unchecked")
	public List<RODDocumentSummary> getBillDetailsByRodKey(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", rodKey);

		List<RODDocumentSummary> billDetails = (List<RODDocumentSummary>) query
				.getResultList();

		return billDetails;

	}

	@SuppressWarnings("unchecked")
	public List<RODBillDetails> getBilldetailsByDocumentSummayKey(
			Long summaryKey) {

		Query query = entityManager
				.createNamedQuery("RODBillDetails.findByRodDocumentSummaryKey");
		query = query.setParameter("summaryKey", summaryKey);

		List<RODBillDetails> billDetails = (List<RODBillDetails>) query
				.getResultList();

		return billDetails;

	}

	@SuppressWarnings("unchecked")
	public List<PostHospitalisation> getPostHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("PostHospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<PostHospitalisation> billDetails = (List<PostHospitalisation>) query
				.getResultList();
		if (billDetails != null && !billDetails.isEmpty()) {
			for (PostHospitalisation postHospitalisation : billDetails) {
				entityManager.refresh(postHospitalisation);
			}
		}

		return billDetails;

	}

	@SuppressWarnings("unchecked")
	public List<Hospitalisation> getHospitalisationList(Long rodKey) {

		Query query = entityManager
				.createNamedQuery("Hospitalisation.findByReimbursement");
		query = query.setParameter("reimbursementKey", rodKey);

		List<Hospitalisation> billDetails = (List<Hospitalisation>) query
				.getResultList();

		for (Hospitalisation hospitalisation : billDetails) {
			entityManager.refresh(hospitalisation);
		}

		return billDetails;

	}

	private void saveRODValues(ReceiptOfDocumentsDTO rodDTO) {

//		CreateRODMapper createRODMapper = new CreateRODMapper();
		CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
		DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
				.getDocumentDetails().getDocAcknowledgementKey());

		Reimbursement reimbursement = null;

		if (null != rodDTO.getReconsiderRODdto()
				&& null != rodDTO.getReconsiderRODdto().getRodKey()) {
			Long rodKey = rodDTO.getReconsiderRODdto().getRodKey();
			/*
			 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
			 * entityManager.flush();
			 */
			reimbursement = getReimbursementObjectByKey(rodKey);
			/*
			 * reimbursement.setDocAcknowLedgement(docAck);
			 * entityManager.persist(reimbursement); entityManager.flush();
			 */

		} else if (null != rodDTO.getRodqueryDTO()
				&& null != rodDTO.getRodqueryDTO().getReimbursementKey()) {
			Long rodKey = rodDTO.getRodqueryDTO().getReimbursementKey();
			/*
			 * docAck.setRodKey(rodKey); entityManager.merge(docAck);
			 * entityManager.flush();
			 */
			reimbursement = getReimbursementObjectByKey(rodKey);
			/*
			 * reimbursement.setDocAcknowLedgement(docAck);
			 * entityManager.persist(reimbursement); entityManager.flush();
			 */

		} else {
			// rodDTO.getDocumentDetails().se
			/**
			 * Since we dont know the possible status of the ROD, sample values
			 * are set. Later this will be changed.
			 * */

			rodDTO.setStatusKey(ReferenceTable.CREATE_ROD_STATUS_KEY);
			rodDTO.setStageKey(ReferenceTable.CREATE_ROD_STAGE_KEY);

			if (rodDTO.getPreauthDTO().getIsPreviousROD()) {
				ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//				ZonalMedicalReviewMapper.getAllMapValues();
				DocumentDetailsDTO docsDTO = rodDTO.getDocumentDetails();
				rodDTO.getPreauthDTO().setDateOfAdmission(
						docsDTO.getDateOfAdmission());
				rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
						.setReasonForChange(docsDTO.getReasonForChange());
				rodDTO.getPreauthDTO().setPaymentModeId(
						docsDTO.getPaymentModeFlag());
				if (null != docsDTO.getPayeeName()) {
					rodDTO.getPreauthDTO().setPayeeName(
							docsDTO.getPayeeName().getValue());
				}
				rodDTO.getPreauthDTO().setPayeeEmailId(docsDTO.getEmailId());
				rodDTO.getPreauthDTO().setPanNumber(docsDTO.getPanNo());
				rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
						.setLegalFirstName(docsDTO.getLegalFirstName());
				rodDTO.getPreauthDTO().setAccountNumber(docsDTO.getAccountNo());
				rodDTO.getPreauthDTO().setBankId(docsDTO.getBankId());
				rodDTO.getPreauthDTO()
						.getPreauthDataExtractionDetails()
						.setRoomCategory(
								rodDTO.getClaimDTO().getNewIntimationDto()
										.getRoomCategory());
				rodDTO.getPreauthDTO().setStatusValue(rodDTO.getStatusValue());
				rodDTO.getPreauthDTO().setStatusKey(rodDTO.getStatusKey());
				rodDTO.getPreauthDTO().setStageKey(rodDTO.getStageKey());
				rodDTO.getPreauthDTO().setRodNumber(
						rodDTO.getDocumentDetails().getRodNumber());
				rodDTO.getPreauthDTO().getPreauthDataExtractionDetails()
						.setDocAckknowledgement(docAck);
				reimbursement = savePreauthValues(rodDTO.getPreauthDTO(), true);
			} else {
				reimbursement = createRODMapper.getReimbursementDetails(rodDTO);
				reimbursement.setActiveStatus(1l);
				reimbursement.setClaim(searchByClaimKey(rodDTO.getClaimDTO()
						.getKey()));
				reimbursement.setRodNumber(rodDTO.getDocumentDetails()
						.getRodNumber());
				reimbursement.setDocAcknowLedgement(docAck);
				entityManager.persist(reimbursement);
				entityManager.flush();
				entityManager.clear();
			}

			// entityManager.refresh(reimbursement);
		}

		List<UploadDocumentDTO> uploadDocsDTO = rodDTO.getUploadDocsList();

		if (null != uploadDocsDTO && !uploadDocsDTO.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
				if (null != uploadDocumentDTO.getFileType()
						&& !("").equalsIgnoreCase(uploadDocumentDTO
								.getFileType().getValue())) {
					RODDocumentSummary rodSummary = createRODMapper
							.getDocumentSummary(uploadDocumentDTO);
					rodSummary.setReimbursement(reimbursement);
					rodSummary.setDocumentToken(uploadDocumentDTO
							.getDmsDocToken());
					entityManager.persist(rodSummary);
					entityManager.flush();
					entityManager.clear();
					// entityManager.refresh(rodSummary);
				}
			}
		}

		// Update the ROD KEY in DocAcknowledgement Table.

		if (null != docAck) {
			DocAcknowledgement docAcknowledgement = createRODMapper
					.getAcknowledgeDocumentList(rodDTO);
			if ((null != rodDTO.getDocumentDetails()
					.getHospitalizationClaimedAmount())
					&& !("").equals(rodDTO.getDocumentDetails()
							.getHospitalizationClaimedAmount())
					&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
							.getHospitalizationClaimedAmount())))
				docAcknowledgement.setHospitalizationClaimedAmount(Double
						.parseDouble(rodDTO.getDocumentDetails()
								.getHospitalizationClaimedAmount()));
			if ((null != rodDTO.getDocumentDetails()
					.getPreHospitalizationClaimedAmount())
					&& !("").equals(rodDTO.getDocumentDetails()
							.getPreHospitalizationClaimedAmount())
					&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
							.getPreHospitalizationClaimedAmount())))
				docAcknowledgement.setPreHospitalizationClaimedAmount(Double
						.parseDouble(rodDTO.getDocumentDetails()
								.getPreHospitalizationClaimedAmount()));
			if ((null != rodDTO.getDocumentDetails()
					.getPostHospitalizationClaimedAmount())
					&& !("").equals(rodDTO.getDocumentDetails()
							.getPostHospitalizationClaimedAmount())
					&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
							.getPostHospitalizationClaimedAmount())))
				docAcknowledgement.setPostHospitalizationClaimedAmount(Double
						.parseDouble(rodDTO.getDocumentDetails()
								.getPostHospitalizationClaimedAmount()));
			docAcknowledgement.setClaim(docAck.getClaim());
			docAcknowledgement.setStage(docAck.getStage());
			docAcknowledgement.setStatus(docAck.getStatus());
			docAcknowledgement.setInsuredContactNumber(docAck
					.getInsuredContactNumber());
			docAcknowledgement.setInsuredEmailId(docAck.getInsuredEmailId());
			docAcknowledgement.setAcknowledgeNumber(docAck
					.getAcknowledgeNumber());

			docAcknowledgement.setRodKey(reimbursement.getKey());

			entityManager.merge(docAcknowledgement);
			entityManager.flush();
			entityManager.clear();
			// entityManager.refresh(docAcknowledgement);
		}

		List<DocumentCheckListDTO> docCheckListVal = rodDTO
				.getDocumentDetails().getDocumentCheckList();
		if (!docCheckListVal.isEmpty()) {
			for (DocumentCheckListDTO docCheckListDTO : docCheckListVal) {
				if (null != docCheckListDTO.getRodReceivedStatusFlag()) {
					RODDocumentCheckList rodDocumentCheckList = createRODMapper
							.getRODCheckListForUpdation(docCheckListDTO);
					rodDocumentCheckList.setDocAcknowledgement(docAck);
					if (null != docCheckListDTO.getDocTypeId()) {
						entityManager.merge(rodDocumentCheckList);
						entityManager.flush();
						entityManager.clear();
						// entityManager.refresh(rodDocumentCheckList);
					} else {

						rodDocumentCheckList.setDocumentTypeId(docCheckListDTO
								.getKey());
						rodDocumentCheckList.setKey(null);
						entityManager.persist(rodDocumentCheckList);
						entityManager.flush();
						entityManager.clear();
						// entityManager.refresh(rodDocumentCheckList);
					}

				}
			}
		}

		// Intimation objIntimation =
		// getIntimationByKey(rodDTO.getClaimDTO().getNewIntimationDto().getKey());

		// Need to check whether the insured object

		/*
		 * if(null != objIntimation) { Insured insured =
		 * getCLSInsured(objIntimation.getInsured().getKey());
		 * objIntimation.setAdmissionDate
		 * (rodDTO.getDocumentDetails().getDateOfAdmission());
		 * insured.setInsuredName
		 * (rodDTO.getDocumentDetails().getInsuredPatientName().getValue());
		 * objIntimation.setInsured(insured);
		 * 
		 * entityManager.merge(objIntimation); entityManager.flush(); }
		 */
		submitTaskToBPM(rodDTO, reimbursement);
	}

	public Reimbursement savePreauthValues(PreauthDTO preauthDTO,
			Boolean isZonalReview) {
		ZonalMedicalReviewMapper reimbursementMapper = ZonalMedicalReviewMapper.getInstance();
//		ZonalMedicalReviewMapper.getAllMapValues();
		Reimbursement reimbursement = reimbursementMapper
				.getReimbursement(preauthDTO);
		reimbursement.setActiveStatus(1l);
		Claim searchByClaimKey2 = searchByClaimKey(preauthDTO.getClaimKey());
		reimbursement.setClaim(searchByClaimKey2);

		Claim currentClaim = null;

		if (reimbursement.getClaim() != null) {
//			currentClaim = searchByClaimKey(reimbursement.getClaim().getKey());
//			currentClaim.setStatus(reimbursement.getStatus());
//			currentClaim.setStage(reimbursement.getStage());
//			entityManager.merge(currentClaim);
//			entityManager.flush();
		}

		if (preauthDTO.getPreauthPreviousClaimsDetails()
				.getAttachToPreviousClaim() != null) {
			Claim searchByClaimKey = searchByClaimKey(preauthDTO
					.getPreauthPreviousClaimsDetails()
					.getAttachToPreviousClaim().getId());
			searchByClaimKey.setClaimLink(preauthDTO.getClaimKey());
			entityManager.merge(searchByClaimKey);
			entityManager.flush();
			entityManager.clear();
		}

		/*
		 * if(reimbursement.getStatus().getKey().equals(ReferenceTable.
		 * ZONAL_REVIEW_APPROVE_STATUS) ||
		 * reimbursement.getStatus().getKey().equals
		 * (ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) { String processflag =
		 * "A";
		 * if(preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() !=
		 * null &&
		 * preauthDTO.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt
		 * () != null &&
		 * preauthDTO.getPreauthDataExtractionDetails().getTotalApprAmt() >
		 * preauthDTO
		 * .getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()) {
		 * processflag = "R"; } reimbursement.setApprovedAmount(preauthDTO.
		 * getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt()); }
		 */

		reimbursement.setAutomaticRestoration(preauthDTO
				.getPreauthDataExtractionDetails().getAutoRestoration()
				.toLowerCase().contains("not") ? "N" : "Y");
		/**
		 * Insured key is a new column added in IMS_CLS_CASHLESS TABLE during
		 * policy refractoring activity. Below code is added for inserting value
		 * in the insured key column.
		 * */
		// reimbursement.setInsuredKey(preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		/*
		 * if(preauthDTO.getKey() != null) { entityManager.merge(reimbursement);
		 * } else
		 */
		reimbursement.setKey(null);
		entityManager.persist(reimbursement);
		entityManager.flush();
		entityManager.clear();

		preauthDTO.setKey(reimbursement.getKey());

		/*
		 * preauthDTO.getCoordinatorDetails().setPreauthKey(reimbursement.getKey(
		 * ));
		 * preauthDTO.getCoordinatorDetails().setIntimationKey(reimbursement.
		 * getClaim().getIntimation().getKey());
		 * preauthDTO.getCoordinatorDetails
		 * ().setPolicyKey(reimbursement.getClaim
		 * ().getIntimation().getPolicy().getKey());
		 */
		ZonalReviewUpdateHospitalDetailsDTO updateHospitalDetails = preauthDTO
				.getPreauthDataExtractionDetails().getUpdateHospitalDetails();
		UpdateHospital updateHospital = reimbursementMapper
				.getUpdateHospital(updateHospitalDetails);
		if (updateHospital != null) {
			updateHospital.setReimbursement(reimbursement);

			if (updateHospital.getKey() != null) {
				entityManager.merge(updateHospital);
			} else {
				entityManager.persist(updateHospital);
			}
			entityManager.flush();
			entityManager.clear();
			updateHospitalDetails.setKey(updateHospital.getKey());
		}

		OtherClaimDetailsDTO otherClaimDetails = preauthDTO
				.getPreauthDataExtractionDetails().getOtherClaimDetails();
		if (null != preauthDTO.getPreauthDataExtractionDetails()
				.getCoveredPreviousClaimFlag()
				&& preauthDTO.getPreauthDataExtractionDetails()
						.getCoveredPreviousClaimFlag().toLowerCase()
						.equalsIgnoreCase("y")) {
			PreviousClaimedHistory claimedHistory = reimbursementMapper
					.getClaimedHistory(otherClaimDetails);
			claimedHistory.setReimbursement(reimbursement);
			if (claimedHistory.getKey() != null) {
				entityManager.merge(claimedHistory);
			} else {
				entityManager.persist(claimedHistory);
			}
			entityManager.flush();
			entityManager.clear();
			preauthDTO.getPreauthDataExtractionDetails().getOtherClaimDetails()
					.setKey(claimedHistory.getKey());

			List<OtherClaimDiagnosisDTO> otherClaimDetailsList = preauthDTO
					.getPreauthDataExtractionDetails()
					.getOtherClaimDetailsList();
			if (!otherClaimDetailsList.isEmpty()) {
				for (OtherClaimDiagnosisDTO otherClaimsDiagDTO : otherClaimDetailsList) {
					PreviousClaimedHospitalization claimedHospitalization = reimbursementMapper
							.getClaimedHospitalization(otherClaimsDiagDTO);
					claimedHospitalization
							.setPreviousClaimedHistory(claimedHistory);
					if (isZonalReview) {
						claimedHospitalization.setKey(null);
					}

					if (claimedHospitalization.getKey() != null) {
						entityManager.merge(claimedHospitalization);
					} else {
						entityManager.persist(claimedHospitalization);
						otherClaimsDiagDTO.setKey(claimedHospitalization
								.getKey());
					}
				}
				entityManager.flush();
				entityManager.clear();
			}
		}

		entityManager.clear();
		if (preauthDTO.getCoordinatorDetails() != null
				&& preauthDTO.getCoordinatorDetails().getRefertoCoordinator() != null) {
			if (preauthDTO.getCoordinatorDetails().getRefertoCoordinatorFlag()
					.toLowerCase().equalsIgnoreCase("y")) {
				Coordinator coordinator = reimbursementMapper
						.getCoordinator(preauthDTO.getCoordinatorDetails());
				coordinator.setActiveStatus(1l);
				coordinator.setStage(reimbursement.getStage());
				coordinator.setStatus(reimbursement.getStatus());
				coordinator.setClaim(reimbursement.getClaim());
				if (isZonalReview) {
					coordinator.setKey(null);
				}

				if (coordinator.getKey() != null) {
					entityManager.merge(coordinator);
				} else {
					entityManager.persist(coordinator);
				}
				entityManager.flush();
				entityManager.clear();
				preauthDTO.getCoordinatorDetails().setKey(coordinator.getKey());
			}
		}

		List<SpecialityDTO> specialityDTOList = preauthDTO
				.getPreauthDataExtractionDetails().getSpecialityList();
		if (!specialityDTOList.isEmpty()) {
			for (SpecialityDTO specialityDTO : specialityDTOList) {
				Speciality speciality = reimbursementMapper
						.getSpeciality(specialityDTO);
				// speciality.setPreauth(preauth);
				speciality.setClaim(reimbursement.getClaim());
				speciality.setStage(reimbursement.getStage());
				speciality.setStatus(reimbursement.getStatus());
				if (isZonalReview) {
					speciality.setKey(null);
				}

				if (speciality.getKey() != null) {
					entityManager.merge(speciality);
				} else {
					entityManager.persist(speciality);
					specialityDTO.setKey(speciality.getKey());
				}
			}
			entityManager.flush();
		}

		entityManager.clear();

		/*
		 * Map<Long, String> keyMap = new HashMap<Long, String>();
		 * keyMap.put(ReferenceTable.BILLING_COORDINATOR_REPLY_RECEIVED,
		 * "coordinatorreply");
		 * keyMap.put(ReferenceTable.BILLING_REFER_TO_COORDINATOR,
		 * "coordinator");
		 * keyMap.put(ReferenceTable.BILLING_REFER_TO_MEDICALA_APPROVER,
		 * "medical");
		 * keyMap.put(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER,
		 * "financial"); keyMap.put(ReferenceTable.BILLING_BENEFITS_APPROVED,
		 * "benefits");
		 * 
		 * if(reimbursement.getStatus().getKey().equals(ReferenceTable.
		 * ZONAL_REVIEW_APPROVE_STATUS) ||
		 * reimbursement.getStatus().getKey().equals
		 * (ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS) ||
		 * keyMap.containsKey(reimbursement.getStatus().getKey())) {
		 * List<DiagnosisProcedureTableDTO> medicalDecisionTableDTO =
		 * preauthDTO.
		 * getPreauthMedicalDecisionDetails().getMedicalDecisionTableDTO(); for
		 * (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO :
		 * medicalDecisionTableDTO) {
		 * if(diagnosisProcedureTableDTO.getDiagnosisDetailsDTO() != null) {
		 * 
		 * DiagnosisDetailsTableDTO diagnosisDetailsDTO =
		 * diagnosisProcedureTableDTO.getDiagnosisDetailsDTO();
		 * diagnosisDetailsDTO
		 * .setAmountConsideredAmount(diagnosisProcedureTableDTO
		 * .getAmountConsidered() != null ?
		 * diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setNetAmount(diagnosisProcedureTableDTO.getNetAmount() != null ?
		 * diagnosisProcedureTableDTO.getNetAmount().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmount() !=
		 * null ? diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() :
		 * 0d);
		 * diagnosisDetailsDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString
		 * (diagnosisProcedureTableDTO.getCoPayPercentage() != null ?
		 * diagnosisProcedureTableDTO.getCoPayPercentage().getValue() : "0"));
		 * diagnosisDetailsDTO
		 * .setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ?
		 * diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
		 * diagnosisDetailsDTO
		 * .setApprovedAmount(diagnosisProcedureTableDTO.getNetApprovedAmt() !=
		 * null ? diagnosisProcedureTableDTO.getNetApprovedAmt().doubleValue() :
		 * 0d);
		 * diagnosisDetailsDTO.setApproveRemarks(diagnosisProcedureTableDTO.
		 * getRemarks());
		 * diagnosisDetailsDTO.setDiffAmount((diagnosisDetailsDTO.
		 * getApprovedAmount() != null ? diagnosisDetailsDTO.getApprovedAmount()
		 * : 0d) - (diagnosisDetailsDTO.getOldApprovedAmount() != null ?
		 * diagnosisDetailsDTO.getOldApprovedAmount() : 0d )); } else
		 * if(diagnosisProcedureTableDTO.getProcedureDTO() != null) {
		 * ProcedureDTO procedureDTO =
		 * diagnosisProcedureTableDTO.getProcedureDTO();
		 * procedureDTO.setAmountConsideredAmount
		 * (diagnosisProcedureTableDTO.getAmountConsidered() != null ?
		 * diagnosisProcedureTableDTO.getAmountConsidered().doubleValue() : 0d);
		 * procedureDTO.setNetAmount(diagnosisProcedureTableDTO.getNetAmount()
		 * != null ? diagnosisProcedureTableDTO.getNetAmount().doubleValue() :
		 * 0d);
		 * procedureDTO.setMinimumAmount(diagnosisProcedureTableDTO.getMinimumAmount
		 * () != null ?
		 * diagnosisProcedureTableDTO.getMinimumAmount().doubleValue() : 0d);
		 * procedureDTO.setCopayPercentage(SHAUtils.getDoubleValueFromString(
		 * diagnosisProcedureTableDTO.getCoPayPercentage() != null ?
		 * diagnosisProcedureTableDTO.getCoPayPercentage().getValue() : "0"));
		 * procedureDTO
		 * .setCopayAmount(diagnosisProcedureTableDTO.getCoPayAmount() != null ?
		 * diagnosisProcedureTableDTO.getCoPayAmount().doubleValue() : 0d);
		 * procedureDTO
		 * .setApprovedAmount(diagnosisProcedureTableDTO.getNetApprovedAmt() !=
		 * null ? diagnosisProcedureTableDTO.getNetApprovedAmt().doubleValue() :
		 * 0d);
		 * procedureDTO.setApprovedRemarks(diagnosisProcedureTableDTO.getRemarks
		 * ()); procedureDTO.setDiffAmount((procedureDTO.getApprovedAmount() !=
		 * null ? procedureDTO.getApprovedAmount() : 0d) -
		 * (procedureDTO.getOldApprovedAmount() != null ?
		 * procedureDTO.getOldApprovedAmount() : 0d )); } } }
		 */

		List<ProcedureDTO> procedureList = preauthDTO
				.getPreauthMedicalProcessingDetails()
				.getProcedureExclusionCheckTableList();
		if (!procedureList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureList) {
				Procedure procedure = reimbursementMapper
						.getProcedure(procedureDTO);
				procedure.setTransactionKey(reimbursement.getKey());
				procedure.setStage(reimbursement.getStage());
				procedure.setStatus(reimbursement.getStatus());

				if (!reimbursement.getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					if (procedureDTO.getCopay() != null) {
						procedure.setCopayPercentage(Double
								.valueOf(procedureDTO.getCopay().getValue()));
					}

				}

				if (reimbursement.getKey().equals(
						ReferenceTable.FINANCIAL_APPROVE_STATUS)|| ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey())) {
					procedure.setProcessFlag("A");
					if (procedureDTO.getOldApprovedAmount() != null
							&& procedureDTO.getApprovedAmount() != null
							&& procedureDTO.getOldApprovedAmount() > procedureDTO
									.getApprovedAmount()) {
						procedure.setProcessFlag("R");
					}
				}

				if (isZonalReview) {
					procedure.setKey(null);
				}

				if (procedure.getKey() != null) {
					entityManager.merge(procedure);
				} else {
					entityManager.persist(procedure);
					procedureDTO.setKey(procedure.getKey());
				}
			}
			entityManager.flush();
			entityManager.clear();
		}

		List<DiagnosisDetailsTableDTO> pedValidationTableList = preauthDTO
				.getPreauthDataExtractionDetails().getDiagnosisTableList();
		// Iterate pedValidationTable List.
		if (!pedValidationTableList.isEmpty()) {
			for (DiagnosisDetailsTableDTO pedValidationDTO : pedValidationTableList) {

				// Method to insert into MAS Diagnosis.
				// saveToMasterDiagnosis(pedValidationDTO);

				PedValidation pedValidation = reimbursementMapper
						.getPedValidation(pedValidationDTO);
				pedValidation.setTransactionKey(reimbursement.getKey());
				pedValidation.setIntimation(reimbursement.getClaim()
						.getIntimation());
				pedValidation.setPolicy(reimbursement.getClaim()
						.getIntimation().getPolicy());
				pedValidation.setStage(reimbursement.getStage());
				pedValidation.setStatus(reimbursement.getStatus());
				if (isZonalReview) {
					pedValidation.setKey(null);
				}
				if (reimbursement.getKey().equals(
						ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
					pedValidation.setProcessFlag("A");
					if (pedValidationDTO.getOldApprovedAmount() != null
							&& pedValidationDTO.getApprovedAmount() != null
							&& pedValidationDTO.getOldApprovedAmount() > pedValidationDTO
									.getApprovedAmount()) {
						pedValidation.setProcessFlag("R");
					}
				}

				if (!reimbursement.getKey().equals(
						ReferenceTable.ZONAL_REVIEW_APPROVE_STATUS)
						&& !reimbursement.getKey().equals(
								ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {
					List<PedDetailsTableDTO> pedList = pedValidationDTO
							.getPedList();
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						if (pedDetailsTableDTO.getCopay() != null) {
							pedValidation.setCopayPercentage(Double
									.valueOf(pedDetailsTableDTO.getCopay()
											.getValue()));
						}
					}
				}

				if (pedValidation.getKey() != null) {
					entityManager.merge(pedValidation);
				} else {
					entityManager.persist(pedValidation);
					pedValidationDTO.setKey(pedValidation.getKey());
				}
				entityManager.flush();
				entityManager.clear();
				List<PedDetailsTableDTO> pedList = pedValidationDTO
						.getPedList();
				if (!pedList.isEmpty()) {
					for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
						DiagnosisPED diagnosisPED = reimbursementMapper
								.getDiagnosisPED(pedDetailsTableDTO);
						diagnosisPED.setPedValidation(pedValidation);
						if (pedDetailsTableDTO
								.getPedExclusionImpactOnDiagnosis() != null) {
							MastersValue value = new MastersValue();
							value.setKey(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis().getId()
									: null);
							value.setKey(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis().getId()
									: null);
							value.setValue(pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis() != null ? pedDetailsTableDTO
									.getPedExclusionImpactOnDiagnosis()
									.getValue() : null);
							diagnosisPED.setDiagonsisImpact(value);
						}
						if (pedDetailsTableDTO.getExclusionDetails() != null) {
							ExclusionDetails exclusionValue = new ExclusionDetails();
							exclusionValue
									.setKey(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getId()
											: null);
							exclusionValue
									.setKey(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getId()
											: null);
							exclusionValue
									.setExclusion(pedDetailsTableDTO
											.getExclusionDetails() != null ? pedDetailsTableDTO
											.getExclusionDetails().getValue()
											: null);
							diagnosisPED.setExclusionDetails(exclusionValue);
						}
						diagnosisPED.setDiagnosisRemarks(pedDetailsTableDTO
								.getRemarks());
						if (isZonalReview) {
							diagnosisPED.setKey(null);
						}

						if (diagnosisPED.getKey() != null) {
							entityManager.merge(diagnosisPED);
						} else {
							entityManager.persist(diagnosisPED);
							pedDetailsTableDTO.setKey(diagnosisPED.getKey());
						}
						entityManager.flush();
						entityManager.clear();
					}
				}
			}

		}

		return reimbursement;
	}

	public Reimbursement getReimbursementObjectByKey(Long key) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey")
				.setParameter("primaryKey", key);

		Reimbursement reimbursement = (Reimbursement) query.getSingleResult();
		entityManager.refresh(reimbursement);
		return reimbursement;
	}

	public Claim searchByClaimKey(Long a_key) {
		Claim find = entityManager.find(Claim.class, a_key);
		entityManager.refresh(find);
		return find;
	}
	
	@SuppressWarnings("unchecked")
	public Claim getClaimByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Claim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Claim> claim = (List<Claim>)query.getResultList();
		
		if(claim != null && ! claim.isEmpty()){
			for (Claim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		}
		else{
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Intimation getIntimationByKey(Long intimationKey) {

		Query findByKey = entityManager
				.createNamedQuery("Intimation.findByKey").setParameter(
						"intiationKey", intimationKey);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			return intimationList.get(0);

		}
		return null;
	}

	/**
	 * The below method is used to fetch Insured details from IMS_CLS_INSURED
	 * Table. This table is a new table which was introduced during policy table
	 * change requirement.
	 * 
	 * */
	public Insured getCLSInsured(Long key) {

		Query query = entityManager.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", key);
		if (query.getResultList().size() != 0)
			return (Insured) query.getSingleResult();
		return null;

	}

	public List<UploadDocumentDTO> getRODSummaryDetails(Long key) {
		List<RODDocumentSummary> rodDocSummary = null;
		List<UploadDocumentDTO> uploadDocsDTO = null;
		Query query = entityManager
				.createNamedQuery("RODDocumentSummary.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			rodDocSummary = query.getResultList();
			for (RODDocumentSummary docSummary : rodDocSummary) {
				entityManager.refresh(docSummary);
			}
			
			CreateRODMapper createRODMapper = CreateRODMapper.getInstance();
			uploadDocsDTO = createRODMapper.getUploadDocumentDTO(rodDocSummary);

		}

		return uploadDocsDTO;
	}

	public Hospitals getHospitalsDetails(Long key, MasterService masterService) {
		return masterService.getHospitalDetails(key);
	}

	public BankMaster getBankMaster(String ifscCode, MasterService masterService) {
		return masterService.getBankDetails(ifscCode);
	}

	/*
	 * public List<DocumentDetailsDTO> getDocumentDetailsDTO (Long claimKey) {
	 * Query query =
	 * entityManager.createNamedQuery("DocAcknowledgement.findByClaimKey");
	 * query.setParameter("claimkey", claimKey); List<DocumentDetailsDTO>
	 * documentDetailsDTO = null; if(null != query.getResultList() &&
	 * !query.getResultList().isEmpty()) {
	 * 
	 * documentDetailsDTO =
	 * CreateRODMapper.getAcknowledgeDocumentList(query.getResultList()); }
	 * 
	 * 
	 * 
	 * return documentDetailsDTO; }
	 */

	public List<DocumentDetailsDTO> getDocumentDetailsDTO(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<DocumentDetailsDTO> documentDetailsDTO = null;
		if (null != query.getResultList() && !query.getResultList().isEmpty()) {
			List<DocAcknowledgement> docAcknowlegementList = (List<DocAcknowledgement>) query
					.getResultList();
			documentDetailsDTO = new ArrayList<DocumentDetailsDTO>();
			for (DocAcknowledgement docAcknowledgement : docAcknowlegementList) {
				if (null != docAcknowledgement.getRodKey()) {
					DocumentDetailsDTO documentDetailDTO = new DocumentDetailsDTO();
					documentDetailDTO.setRodKey(docAcknowledgement.getRodKey());
					documentDetailDTO.setHospitalizationFlag(docAcknowledgement
							.getHospitalisationFlag());
					documentDetailDTO
							.setPreHospitalizationFlag(docAcknowledgement
									.getPreHospitalisationFlag());
					documentDetailDTO
							.setPostHospitalizationFlag(docAcknowledgement
									.getPostHospitalisationFlag());
					documentDetailDTO
							.setPartialHospitalizationFlag(docAcknowledgement
									.getPartialHospitalisationFlag());
					documentDetailDTO.setLumpSumAmountFlag(docAcknowledgement
							.getLumpsumAmountFlag());
					documentDetailDTO
							.setAddOnBenefitsHospitalCashFlag(docAcknowledgement
									.getHospitalCashFlag());
					documentDetailDTO
							.setAddOnBenefitsPatientCareFlag(docAcknowledgement
									.getPatientCareFlag());
					documentDetailsDTO.add(documentDetailDTO);
				}
			}
		}
		return documentDetailsDTO;
	}

	/**
	 * During bug fixing activity , below method implementation logic was
	 * changed. Planned to change method signature also, but since without
	 * checking impact analysis this can't be changed. Hence retaining the same.
	 * 
	 * */
	public DocumentDetailsDTO getPreviousRODDetailsForClaim(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findMaxRODKey");
		query.setParameter("claimkey", claimKey);
		DocumentDetailsDTO docDTO = null;
		if (null != query.getSingleResult()) {
			docDTO = new DocumentDetailsDTO();
			Long rodKey = (Long) query.getSingleResult();
			Query query1 = entityManager
					.createNamedQuery("Reimbursement.findByKey");
			query1.setParameter("primaryKey", rodKey);
			Reimbursement objReimbursement = (Reimbursement) query1
					.getSingleResult();
			docDTO.setAccountNo(objReimbursement.getAccountNumber());
			docDTO.setPayableAt(objReimbursement.getPayableAt());

			docDTO.setEmailId(objReimbursement.getPayeeEmailId());
			docDTO.setPanNo(objReimbursement.getPanNumber());
			SelectValue selValue = new SelectValue();
			// selValue.setId(id);
			selValue.setValue(objReimbursement.getPayeeName());
			docDTO.setPayeeName(selValue);
			docDTO.setReasonForChange(objReimbursement.getReasonForChange());
			docDTO.setPayModeChangeReason(objReimbursement.getPayModeChangeReason());
			docDTO.setLegalFirstName(objReimbursement.getLegalHeirFirstName());
			if (null != objReimbursement.getBankId()) {
				BankMaster masBank = getBankDetails(objReimbursement
						.getBankId());
				docDTO.setBankName(masBank.getBankName());
				docDTO.setCity(masBank.getCity());
				docDTO.setIfscCode(masBank.getIfscCode());
			}
		}
		return docDTO;
	}

	public Reimbursement getPreviousRODDetails(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Reimbursement> reimbursementList = query.getResultList();
		Reimbursement reimbursement = null;
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			reimbursement = reimbursementList.get(0);
		}
		return reimbursement;

	}

	public BankMaster getBankDetails(Long key) {
		BankMaster masBank = null;
		Query query = entityManager.createNamedQuery("BankMaster.findByKey");
		query = query.setParameter("key", key);
		List<BankMaster> bankMasList = query.getResultList();
		if (null != bankMasList && !bankMasList.isEmpty()) {
			for (BankMaster bankMaster : bankMasList) {
				masBank = bankMaster;
			}
		}
		// BankMaster masBank = (BankMaster)query.getSingleResult();
		return masBank;
	}

	public Long getLatestDocAcknowledgementKey(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findMaxDocAckKey");
		query.setParameter("rodKey", rodKey);
		Long docAckKey = (Long) query.getSingleResult();
		return docAckKey;
	}

	public Long getACknowledgeNumberCountByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.CountAckByClaimKey");
		query = query.setParameter("claimkey", claimKey);
		// Integer.parseInt(Strin)
		Long countOfAck = (Long) query.getSingleResult();
		return countOfAck;
	}

	public ReceiptOfDocumentsDTO getBillClassificationFlagDetails(
			Long claimKey, ReceiptOfDocumentsDTO rodDTO) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			int iSize = reimbursementList.size();
			Reimbursement rod = reimbursementList.get(iSize - 1);
			rodDTO.setHospitalizationFlag(rod.getDocAcknowLedgement()
					.getHospitalisationFlag());
			rodDTO.setPartialHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPartialHospitalisationFlag());
			rodDTO.setPostHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPostHospitalisationFlag());
			rodDTO.setPreHospitalizationFlag(rod.getDocAcknowLedgement()
					.getPreHospitalisationFlag());
		}
		return rodDTO;
	}

	public List<Reimbursement> getReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementList = query.getResultList();
		return reimbursementList;
	}

	/*
	 * public void saveUploadDocuments(List<UploadDocumentDTO> uploadDocsList) {
	 * 
	 * 
	 * }
	 */

	private void submitTaskToBPM(ReceiptOfDocumentsDTO rodDTO,
			Reimbursement reimbursement) {/*
		SubmitRODTask rodSubmitTask = BPMClientContext
				.getCreateRODSubmitService(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForROD = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForROD.getPayload();
		humanTaskForROD.setOutcome("APPROVE");
		// DocReceiptACKType receiptAckType = new DocReceiptACKType();
		// receiptAckType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		// receiptAckType.setKey(reimbursement.getKey());
		RODType rodType = new RODType();
		rodType.setAckNumber(rodDTO.getDocumentDetails().getRodNumber());
		rodType.setKey(reimbursement.getKey());
		List<UploadDocumentDTO> uploadDocsList = rodDTO.getUploadDocsList();

		if (null != uploadDocsList && !uploadDocsList.isEmpty()) {
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsList) {
				if (null != uploadDocumentDTO.getFileType()
						&& null != uploadDocumentDTO.getFileType().getValue()
						&& uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
					payloadBO.getDocReceiptACK().setIsBillAvailable(true);
					break;
				} else {
					payloadBO.getDocReceiptACK().setIsBillAvailable(false);
				}
			}
		}
		*//***
		 * The below condition check was added , since the record which was
		 * submitted with hospitalization during ROD level didn't move to zonal
		 * medical review screen.
		 *//*
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			payloadBO.getDocReceiptACK().setHospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			payloadBO.getDocReceiptACK().setPartialhospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			payloadBO.getDocReceiptACK().setPrehospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPrehospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
			payloadBO.getDocReceiptACK().setPosthospitalization(true);
		} else {
			payloadBO.getDocReceiptACK().setPosthospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
			payloadBO.getDocReceiptACK().setLumpsumamount(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setLumpsumamount(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitshospcash(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(true);
			payloadBO.getDocReceiptACK().setIsBillAvailable(true);
		} else {
			payloadBO.getDocReceiptACK().setAddonbenefitspatientcare(false);
		}

		payloadBO.setRod(rodType);
		// payloadBO.setDocReceiptACK(receiptAckType);
		humanTaskForROD.setPayload(payloadBO);
		try{
		rodSubmitTask.execute(rodDTO.getStrUserName(), humanTaskForROD);
		}catch(Exception e){
			e.printStackTrace();
		}
	*/}

	private void submitBillEntryTaskToBPM(ReceiptOfDocumentsDTO rodDTO) {/*
		SubmitBillEntryTask submitBillEntryTask = BPMClientContext
				.getSubmitBillEntryTask(rodDTO.getStrUserName(),
						rodDTO.getStrPassword());
		HumanTask humanTaskForBillEntry = rodDTO.getHumanTask();
		PayloadBOType payloadBO = humanTaskForBillEntry.getPayload();
		humanTaskForBillEntry.setOutcome("APPROVE");
		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");

		PaymentInfoType paymentInfoType = new PaymentInfoType();
		paymentInfoType.setClaimedAmount(rodDTO.getTotalClaimedAmount());

		DocReceiptACKType docReceiptType = payloadBO.getDocReceiptACK();
		
		 * docReceiptType.setHospitalization(true);
		 * docReceiptType.setPartialhospitalization(false);
		 * docReceiptType.setPrehospitalization(false);
		 * docReceiptType.setPosthospitalization(false);
		 * docReceiptType.setLumpsumamount(false);
		 * docReceiptType.setAddonbenefitshospcash(false);
		 * docReceiptType.setAddonbenefitspatientcare(false);
		 
		if (null != rodDTO.getDocumentDetails().getHospitalization()
				&& rodDTO.getDocumentDetails().getHospitalization()) {
			docReceiptType.setHospitalization(true);
		} else {
			docReceiptType.setHospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPartialHospitalization()
				&& rodDTO.getDocumentDetails().getPartialHospitalization()) {
			docReceiptType.setPartialhospitalization(true);
		} else {
			docReceiptType.setPartialhospitalization(false);
		}
		if (null != rodDTO.getDocumentDetails().getPreHospitalization()
				&& rodDTO.getDocumentDetails().getPreHospitalization()) {
			docReceiptType.setPrehospitalization(true);
		} else {
			docReceiptType.setPrehospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getPostHospitalization()
				&& rodDTO.getDocumentDetails().getPostHospitalization()) {
			docReceiptType.setPosthospitalization(true);
		} else {
			docReceiptType.setPosthospitalization(false);
		}

		if (null != rodDTO.getDocumentDetails().getLumpSumAmount()
				&& rodDTO.getDocumentDetails().getLumpSumAmount()) {
			docReceiptType.setLumpsumamount(true);
		} else {
			docReceiptType.setLumpsumamount(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsHospitalCash()) {
			docReceiptType.setAddonbenefitshospcash(true);
		} else {
			docReceiptType.setAddonbenefitshospcash(false);
		}

		if (null != rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()
				&& rodDTO.getDocumentDetails().getAddOnBenefitsPatientCare()) {
			docReceiptType.setAddonbenefitspatientcare(true);
		} else {
			docReceiptType.setAddonbenefitspatientcare(false);
		}

		// Adding for BPMN filtering search.
		HospitalInfoType hospInfoType = new HospitalInfoType();
		hospInfoType.setHospitalType("Network Hospital Type");
		hospInfoType.setNetworkHospitalType("Agreed Network Hospital Type");

		ProductInfoType pdtTypeInfo = new ProductInfoType();
		pdtTypeInfo.setProductId("test value");

		
		 * IntimationType intimationType = payloadBO.getIntimation();
		 * intimationType.setIntimationSource("Call Center");
		 

		payloadBO.getClaimRequest().setClaimRequestType("All");
		payloadBO.getIntimation().setIntimationSource("Call Center");
		payloadBO.setHospitalInfo(hospInfoType);
		payloadBO.setProductInfo(pdtTypeInfo);

		// docReceiptType.setAddonbenefitshospcash(false);
		// docReceiptType.setAddonbenefitspatientcare(false);

		// payloadBO.getClaimRequest().setResult("ACK");
		payloadBO.getClaimRequest().setClientType("ACK");

		payloadBO.setDocReceiptACK(docReceiptType);
		payloadBO.setProductInfo(productInfo);
		payloadBO.setPaymentInfo(paymentInfoType);

		humanTaskForBillEntry.setPayload(payloadBO);

		try{
		submitBillEntryTask.execute(rodDTO.getStrUserName(),
				humanTaskForBillEntry);
		}catch(Exception e){
			e.printStackTrace();
		}

	*/}
	
	public Status getStatusByKey(Long key) {

		try {
			Query findType = entityManager.createNamedQuery("Status.findByKey")
					.setParameter("statusKey", key);
			//Status status = (Status) findType.getSingleResult();
			List<Status> statusList  = findType.getResultList();
			if(null != statusList && !statusList.isEmpty())
			{
				entityManager.refresh(statusList.get(0));
				return statusList.get(0);
			}
			return null;
		} catch (Exception e) {
			return null;
		}

	}


	public DocAcknowledgement getDocAckByClaim(Long claimKey)
	{
		Query query = entityManager.createNamedQuery("DocAcknowledgement.getByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(null != docAckList && !docAckList.isEmpty())
		{
			entityManager.refresh(docAckList.get(0));
			return docAckList.get(0);
		}
		return null;
	}

	public DocumentDetails getDocumentDetailsBasedOnDocToken(String documentToken)
	{
		Query query = entityManager.createNamedQuery("DocumentDetails.findByDocumentToken");
		if(null != documentToken)
		{
			query = query.setParameter("documentToken", Long.valueOf(documentToken));
			List<DocumentDetails> documentDetailsList = query.getResultList();
			if(null != documentDetailsList && !documentDetailsList.isEmpty())
			{
				entityManager.refresh(documentDetailsList.get(0));
				return documentDetailsList.get(0);
			}
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitPABillEntryValues(ReceiptOfDocumentsDTO rodDTO) {
		try {
//			CreateRODMapper rodMapper = new CreateRODMapper();
			DocAcknowledgement docAck = getDocAcknowledgementBasedOnKey(rodDTO
					.getDocumentDetails().getDocAcknowledgementKey());
			//docAck = getAcknowledgementByClaimKey(docAck.getClaim().getKey());

			DocAcknowledgement docAckNew = new DocAcknowledgement();

			if (docAck != null) {
				
				if(null != rodDTO.getClaimDTO())
				{
					DocAcknowledgement docAckObj = getDocAckByClaim(rodDTO.getClaimDTO().getKey());

					if (null != docAckObj.getAcknowledgeNumber()) {
						String ackNumber = docAckObj.getAcknowledgeNumber();
						String[] ack1 = ackNumber.split("/");
						String ackNo = "";
						for (int index = 0; index < ack1.length - 1; index++) {
							ackNo = ackNo + ack1[index] + "/";
						}
						
						String ack2 = ack1[ack1.length - 1];
						Long ack3 = Long.parseLong(ack2);
						ack3++;
						ackNo = ackNo + ack3.toString();
						docAckNew.setAcknowledgeNumber(ackNo);
					}	
				}


				if (docAck.getHospitalisationFlag() != null) {
					docAckNew.setHospitalisationFlag(docAck
							.getHospitalisationFlag());
				}

				if (docAck.getPreHospitalisationFlag() != null) {
					docAckNew.setPreHospitalisationFlag(docAck
							.getPreHospitalisationFlag());
				}			

				if (docAck.getPostHospitalisationFlag() != null) {
					docAckNew.setPostHospitalisationFlag(docAck
							.getPostHospitalisationFlag());
				}

				if (docAck.getClaim() != null) {
					docAckNew.setClaim(getClaimByClaimKey(docAck.getClaim().getKey()));
				}

				if (docAck.getRodKey() != null) {
					docAckNew.setRodKey(docAck.getRodKey());
				}

				if (docAck.getReconsiderationRequest() != null) {
					docAckNew.setReconsiderationRequest(docAck
							.getReconsiderationRequest());
				}

				if (docAck.getPartialHospitalisationFlag() != null) {
					docAckNew.setPartialHospitalisationFlag(docAck
							.getPartialHospitalisationFlag());
				}

				if (docAck.getLumpsumAmountFlag() != null) {
					docAckNew.setLumpsumAmountFlag(docAck
							.getLumpsumAmountFlag());
				}

				if (docAck.getHospitalCashFlag() != null) {
					docAckNew.setHospitalCashFlag(docAck.getHospitalCashFlag());
				}

				if (docAck.getPatientCareFlag() != null) {
					docAckNew.setPatientCareFlag(docAck.getPatientCareFlag());
				}

				if (docAck.getActiveStatus() != null) {
					docAckNew.setActiveStatus(docAck.getActiveStatus());
				}
				if (docAck.getOfficeCode() != null) {
					docAckNew.setOfficeCode(docAck.getOfficeCode());
				}
				if (docAck.getStage() != null) {
					docAckNew.setStage(docAck.getStage());
				}
				if (docAck.getStatus() != null) {
					Status status = getStatusByKey(ReferenceTable.ADD_ADDITIONAL_DOCUMENTS_STATUS);
					if(null != status)
						//docAckNew.setStatus(docAck.getStatus());
						/**
						 * The below code was added after
						 * a new status was introduced for
						 * add additional documents.
						 * */
						docAckNew.setStatus(status);
				}
				if (rodDTO.getStrUserName() != null) {
					docAckNew.setCreatedBy(rodDTO.getStrUserName());
				}
				if (docAck.getCreatedDate() != null) {
					docAckNew.setCreatedDate(docAck.getCreatedDate());
				}
				if (docAck.getModifiedBy() != null) {
//					docAckNew.setModifiedBy(docAck.getModifiedBy());
				}
				if (docAck.getModifiedDate() != null) {
//					docAckNew.setModifiedDate(docAck.getModifiedDate());
				}
				if (docAck.getHospitalizationClaimedAmount() != null) {
					docAckNew.setHospitalizationClaimedAmount(docAck
							.getHospitalizationClaimedAmount());
				}
				if (docAck.getPreHospitalizationClaimedAmount() != null) {
					docAckNew.setPreHospitalizationClaimedAmount(docAck
							.getPreHospitalizationClaimedAmount());
				}
				if (docAck.getPostHospitalizationClaimedAmount() != null) {
					docAckNew.setPostHospitalizationClaimedAmount(docAck
							.getPostHospitalizationClaimedAmount());
				}
				if (docAck.getHospitalizationRepeatFlag() != null) {
					docAckNew.setHospitalizationRepeatFlag(docAck
							.getHospitalizationRepeatFlag());
				}
				
				if(null != docAck.getBenifitFlag()){
					docAckNew.setBenifitFlag(docAck.getBenifitFlag());
				}
				if (null != docAck.getBenifitClaimedAmount()){
					docAckNew.setBenifitClaimedAmount(docAck.getBenifitClaimedAmount());
				}
				if(null != docAck.getDocumentTypeId()){
					docAckNew.setDocumentTypeId(docAck.getDocumentTypeId());
				}
				if(null != docAck.getProcessClaimType()){
					docAckNew.setProcessClaimType(docAck.getProcessClaimType());
				}
				
				if(null != docAck.getReconsiderationReasonId()){
					docAckNew.setReconsiderationReasonId(docAck.getReconsiderationReasonId());
				}

			}

			if (rodDTO.getDocumentDetails() != null
					&& rodDTO.getDocumentDetails().getDocumentsReceivedFrom() != null) {
				MastersValue docReceivedFrom = new MastersValue();
				docReceivedFrom.setKey(rodDTO.getDocumentDetails()
						.getDocumentsReceivedFrom().getId());
				docReceivedFrom.setValue(rodDTO.getDocumentDetails()
						.getDocumentsReceivedFrom().getValue());
				docAckNew.setDocumentReceivedFromId(docReceivedFrom);
			}

			if (rodDTO.getDocumentDetails() != null
					&& rodDTO.getDocumentDetails().getModeOfReceipt() != null) {
				MastersValue modeOfReceipt = new MastersValue();
				modeOfReceipt.setKey(rodDTO.getDocumentDetails()
						.getModeOfReceipt().getId());
				modeOfReceipt.setValue(rodDTO.getDocumentDetails()
						.getModeOfReceipt().getValue());
				docAckNew.setModeOfReceiptId(modeOfReceipt);
			}

			if (rodDTO.getDocumentDetails().getDocumentsReceivedDate() != null) {
				docAckNew.setDocumentReceivedDate(rodDTO.getDocumentDetails()
						.getDocumentsReceivedDate());
			}
			
			if(null != rodDTO.getDocumentDetails().getAcknowledgmentContactNumber())
			{
				docAckNew.setInsuredContactNumber(rodDTO.getDocumentDetails().getAcknowledgmentContactNumber());
			}

			if (rodDTO.getDocumentDetails().getEmailId() != null) {
				docAckNew.setInsuredEmailId(rodDTO.getDocumentDetails()
						.getEmailId());
			}

			if (rodDTO.getDocumentDetails().getAdditionalRemarks() != null) {
				docAckNew.setAdditionalRemarks(rodDTO.getDocumentDetails()
						.getAdditionalRemarks());
			}
			
			if(null != rodDTO.getDocumentDetails().getSourceOfDocumentValue()){
				
				docAckNew.setSourceOfDocument(rodDTO.getDocumentDetails().getSourceOfDocumentValue());
			}
			Boolean flag = true;
			int increment = 0;
			

			Reimbursement reimbursement = null;
			reimbursement = reimbursementService.getReimbursementbyRod(
					rodDTO.getDocumentDetails().getRodKey(), entityManager);
			
			if(null != rodDTO.getDocumentDetails().getSourceOfDocumentValue() &&
					(SHAConstants.SOURCE_DOC_INSURED.equalsIgnoreCase(rodDTO.getDocumentDetails().getSourceOfDocumentValue()))){

			do {
				if(increment>10){
					flag = false;
				}
				try {
					entityManager.persist(docAckNew);
					entityManager.flush();
					flag = false;
				} catch (Exception e) {
					e.printStackTrace();
					increment++;
					if (docAckNew.getAcknowledgeNumber() != null) {
						String ackNumber = docAckNew.getAcknowledgeNumber();
						String[] ack1 = ackNumber.split("/");
						String ackNo = "";
						for (int index = 0; index < ack1.length - 1; index++) {
							ackNo = ackNo + ack1[index] + "/";
						}
						
						String ack2 = ack1[ack1.length - 1];
						Long ack3 = Long.parseLong(ack2);
						ack3++;
						
						ackNo = ackNo + ack3.toString();						
						docAckNew.setAcknowledgeNumber(ackNo);
					}
					
				}
			} while (flag);

			//entityManager.refresh(docAckNew);
			docAck = getDocAcknowledgementBasedOnKey(docAckNew.getKey());


			if (rodDTO.getDocumentDetails() != null
					&& rodDTO.getDocumentDetails().getRodKey() != null) {				
				reimbursement.setDocAcknowLedgement(docAck);
				entityManager.merge(reimbursement);
			}
		}
			List<DocumentCheckListDTO> docCheckList = rodDTO.getDocumentDetails()
					.getDocumentCheckList();
			//if (rodDTO.getDocumentDetails().getDocumentCheckList().size() > 0) {
			if(null != docCheckList && !docCheckList.isEmpty()) {
				AcknowledgeDocumentReceivedMapper ackDocRecMapper =AcknowledgeDocumentReceivedMapper.getInstance();
//				AcknowledgeDocumentReceivedMapper ackDocRecMapper = new AcknowledgeDocumentReceivedMapper();
				for (DocumentCheckListDTO docCheckListDTO : docCheckList) {
					// if(null != docCheckListDTO.getNoOfDocuments())
					if (null != docCheckListDTO.getReceivedStatus()
							&& !("").equalsIgnoreCase(docCheckListDTO
									.getReceivedStatus().getValue())) {
						/*
						 * if(null != docCheckListDTO.getDocChkLstKey()) {
						 * RODDocumentCheckList documentDetails =
						 * findRODDocumentCheckListByKey
						 * (docCheckListDTO.getDocChkLstKey());
						 * entityManager.merge(documentDetails);
						 * entityManager.flush(); } else
						 */
						// {
						RODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
								.getRODDocumentCheckList(docCheckListDTO);
						rodDocumentCheckList.setDocAcknowledgement(docAck);
						// findRODDocumentCheckListByKey(masterService);
						entityManager.persist(rodDocumentCheckList);
						entityManager.flush();
						//log.info("------RODDocumentCheckList------>"+rodDocumentCheckList+"<------------");
						// }
					}
				}
				
				/*for (DocumentCheckListDTO documentCheckListDTO : rodDTO
						.getDocumentDetails().getDocumentCheckList()) {
					RODDocumentCheckList rodDocumentList = new RODDocumentCheckList();
					if (docAck != null) {
						rodDocumentList.setDocAcknowledgement(docAck);
					}
					if (documentCheckListDTO.getDocTypeId() != null) {
						rodDocumentList.setDocumentTypeId(documentCheckListDTO
								.getDocTypeId());
					}
					if (documentCheckListDTO.getReceivedStatus() != null) {
						MastersValue receivedStatus = new MastersValue();
						receivedStatus.setKey(documentCheckListDTO
								.getReceivedStatus().getId());
						receivedStatus.setValue(documentCheckListDTO
								.getReceivedStatus().getValue());
						rodDocumentList.setReceivedStatusId(receivedStatus);
					}
					if (documentCheckListDTO.getNoOfDocuments() != null) {
						rodDocumentList.setNoOfDocuments(documentCheckListDTO
								.getNoOfDocuments());
					}
					if (documentCheckListDTO.getRemarks() != null) {
						rodDocumentList.setRemarks(documentCheckListDTO
								.getRemarks());
					}
					if (documentCheckListDTO.getCreatedDate() != null) {
						rodDocumentList.setCreatedDate(documentCheckListDTO
								.getCreatedDate());
					}
					entityManager.persist(rodDocumentList);
					entityManager.flush();
					entityManager.refresh(rodDocumentList);
				}*/

				List<UploadDocumentDTO> uploadDocsDTO = rodDTO
						.getUploadDocsList();
				for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {

					saveBillEntryValues(uploadDocumentDTO);
					
					
					/*

					RODDocumentSummary rodSummary = CreateRODMapper
							.getDocumentSummary(uploadDocumentDTO);
					rodSummary.setReimbursement(reimbursement);
					if(null != rodSummary.getKey())
					{
						entityManager.merge(rodSummary);
					}
					else
					{
						entityManager.persist(rodSummary);
					}
					entityManager.flush();

					
					 * if (null != uploadDocumentDTO.getDocSummaryKey()) {
					 * entityManager.merge(rodSummary); entityManager.flush(); }
					 * else { entityManager.persist(rodSummary);
					 * entityManager.flush(); entityManager.refresh(rodSummary);
					 * }
					 

					List<BillEntryDetailsDTO> billEntryDetailsList = uploadDocumentDTO
							.getBillEntryDetailList();
					if (null != billEntryDetailsList
							&& !billEntryDetailsList.isEmpty()) {
						for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {

							if (uploadDocumentDTO.getBillNo().equalsIgnoreCase(
									billEntryDetailsDTO.getBillNo())) {
								RODBillDetails rodBillDetails = rodMapper
										.getRODBillDetails(billEntryDetailsDTO);
								rodBillDetails
										.setRodDocumentSummaryKey(rodSummary);
								rodBillDetails.setDeletedFlag(SHAConstants.N_FLAG);
								if(null != rodBillDetails.getKey())
								{
									entityManager.merge(rodBillDetails);
								}
								else
								{
									entityManager.persist(rodBillDetails);
								}
								entityManager.flush();

							}
						}
					}

				*/}
				
				List<UploadDocumentDTO> deletedDocsList = rodDTO.getUploadDocumentsDTO().getDeletedDocumentList();
				if (null != deletedDocsList && !deletedDocsList.isEmpty()) {
					for (UploadDocumentDTO uploadDocumentDTO2 : deletedDocsList) {

						
						
						RODDocumentSummary rodSummaryObj = CreateRODMapper.getInstance()
								.getDocumentSummary(uploadDocumentDTO2);
						rodSummaryObj.setReimbursement(reimbursement);
						rodSummaryObj.setDeletedFlag("Y");

						if (null != uploadDocumentDTO2.getDocSummaryKey()) {
							rodSummaryObj.setModifiedBy(uploadDocumentDTO2.getStrUserName());
							rodSummaryObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
							entityManager.merge(rodSummaryObj);
							entityManager.flush();
							//log.info("------RODDocumentSummary------>"+rodSummary+"<------------");
						} /*
						 * else { entityManager.persist(rodSummary);
						 * entityManager.flush(); entityManager.refresh(rodSummary);
						 * }
						 */
						
						DocumentDetails details = getDocumentDetailsBasedOnDocToken(rodSummaryObj.getDocumentToken());
						if(null != details)
						{
							details.setDeletedFlag("Y");
							entityManager.merge(details);
							entityManager.flush();
							//log.info("------Document details key which got deleted------>"+details.getKey()+"<------------");
						}

					}
					
				}
				
				
				if(null != rodDTO.getDocFilePath() && !("").equalsIgnoreCase(rodDTO.getDocFilePath()))
				{
					WeakHashMap dataMap = new WeakHashMap();
					dataMap.put("intimationNumber",docAck.getClaim().getIntimation().getIntimationId());
					Claim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
					if(null != objClaim)
					{
						dataMap.put("claimNumber",objClaim.getClaimId());
						if(null != objClaim.getClaimType())
						{
							if((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY).equals(objClaim.getClaimType().getKey()))
								{
									Preauth preauth = getPreauthClaimKey( objClaim.getKey());
									if(null != preauth)
										dataMap.put("cashlessNumber", preauth.getPreauthId());
								}
						}
					}
					dataMap.put("filePath", rodDTO.getDocFilePath());
					dataMap.put("docType", rodDTO.getDocType());
					dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
					dataMap.put("createdBy", rodDTO.getStrUserName());
					SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Already Submitted. Please Try Another Record.");
		}

	}
	
	
	public void saveDocAcknowledgementDetailsForPhyDoc(ReceiptOfDocumentsDTO rodDTO){

		List<PhysicalDocumentVerificationDetails> physicalVerificationDetails = getphysicalVerficationByKey(rodDTO.getDocumentDetails().getRodKey());
		PhysicalDocumentVerification physicalVerification = getReimbursementByKey(rodDTO.getDocumentDetails().getRodKey());
		Reimbursement reimbursementObjectByKey = getReimbursementObjectByKey(rodDTO.getDocumentDetails().getRodKey());
		DocAcknowledgement docAck = reimbursementObjectByKey.getDocAcknowLedgement();
		
		List<UploadDocumentDTO> uploadDocsDTO = rodDTO
				.getUploadDocsList();
		for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTO) {
			if(rodDTO.getStrUserName() != null){
				String username1 = rodDTO.getStrUserName();
				String userNameForDB1 = SHAUtils.getUserNameForDB(username1); 
			uploadDocumentDTO.setStrUserName(userNameForDB1);
			}
			saveBillEntryValues(uploadDocumentDTO);
			savePhysicalVerificationDetails(uploadDocumentDTO,physicalVerification,reimbursementObjectByKey);
			
		}
		ClaimPayment paymentDtls = ackDocReceivedService.getClaimPaymentByRodKey(reimbursementObjectByKey.getKey());
		if(null != rodDTO){

			if(null != rodDTO.getSelectedPhysicalDocumentsDTO().getIsDocumentVerified()){
				if(rodDTO.getScreenName() != null && rodDTO.getScreenName().equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
					Status status = new Status();
					status.setKey(ReferenceTable.PAYMENT_CHECKER_VERIFIED_STATUS);
					physicalVerification.setStatus(status);
					Stage stage = new Stage();
					stage.setKey(ReferenceTable.PHYSICAL_VERIFICATION_STAGE);
					physicalVerification.setStage(stage);
					physicalVerification.setCommunicationEnableFlag(SHAConstants.YES_FLAG);
					if(rodDTO.getStrUserName() != null){
						String username1 = rodDTO.getStrUserName();
						String userNameForDB1 = SHAUtils.getUserNameForDB(username1); 
						physicalVerification.setModifiedBy(userNameForDB1);
					}
					physicalVerification.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				}
				else {
					if(rodDTO.getScreenName() != null && rodDTO.getScreenName().equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT)){
						Status status = new Status();
						status.setKey(ReferenceTable.PAYMENT_MAKER_VERIFIED_STATUS);
						physicalVerification.setStatus(status);
						Stage stage = new Stage();
						stage.setKey(ReferenceTable.PHYSICAL_VERIFICATION_STAGE);
						physicalVerification.setStage(stage);
						if(rodDTO.getStrUserName() != null){
							String username1 = rodDTO.getStrUserName();
							String userNameForDB1 = SHAUtils.getUserNameForDB(username1); 
							physicalVerification.setModifiedBy(userNameForDB1);
						}
						physicalVerification.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					}
				}
				if(null != physicalVerification && null != physicalVerification.getKey()){
					entityManager.merge(physicalVerification);
					entityManager.flush();
				}
			}
			if(rodDTO.getScreenName() != null && rodDTO.getScreenName().equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER)){
				if(paymentDtls != null){
					Status status = new Status();
					status.setKey(ReferenceTable.PAYMENT_NEW_STATUS);
					paymentDtls.setStatusId(status);

					Stage stage = new Stage();
					stage.setKey(ReferenceTable.PAYMENT_PROCESS_STAGE);
					paymentDtls.setStageId(stage);

					if(rodDTO.getStrUserName() != null){
						String username1 = rodDTO.getStrUserName();
						String userNameForDB1 = SHAUtils.getUserNameForDB(username1); 
						paymentDtls.setModifiedBy(userNameForDB1);
						paymentDtls.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					}
					if(paymentDtls != null && paymentDtls.getKey() != null){
						entityManager.merge(paymentDtls);
						entityManager.flush();
					}
				}
			}

		}
	}
	
	private DocAcknowledgement getAcknowledgement(Long rodKey) {
		DocAcknowledgement ack = null;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"DocAcknowledgement.findByReimbursementKey").setParameter("rodKey",
				rodKey);
		try {
			List<DocAcknowledgement> resultList = (List<DocAcknowledgement>) findByReimbursementKey
					.getResultList();
			List<DocAcknowledgement> ackList = resultList;
			if (null != ackList && !ackList.isEmpty()) {
				ack = ackList.get(0);
			}
			return ack;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	@SuppressWarnings("unchecked")
	public Preauth getPreauthClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByClaimKey");
		query.setParameter("claimkey", claimKey);
		List<Preauth> preauthList = (List<Preauth>) query.getResultList();

		if (preauthList != null && !preauthList.isEmpty()) {
			entityManager.refresh(preauthList.get(0));
			return preauthList.get(0);
		}
		return null;
	}
	
	public PhysicalDocumentVerification getReimbursementByKey(Long key) {
		Query query = entityManager.createNamedQuery("PhysicalDocumentVerification.findByRodKey")
				.setParameter("primaryKey", key);

		List<PhysicalDocumentVerification> reimbursement =  query.getResultList();
		if(!reimbursement.isEmpty() && reimbursement != null) {
			return reimbursement.get(0);
		}
		return null;
	}
	
	public List<PhysicalDocumentVerificationDetails> getphysicalVerficationByKey(Long key) {
		Query query = entityManager.createNamedQuery("PhysicalDocumentVerificationDetails.findByRodKey")
				.setParameter("primaryKey", key);
		List<PhysicalDocumentVerificationDetails> resultList = (List<PhysicalDocumentVerificationDetails>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			for (PhysicalDocumentVerificationDetails implantDetails : resultList) {
				entityManager.refresh(implantDetails);
			}
		}
		return resultList;
		
	}
	
	public PhysicalDocumentVerificationDetails getPhysicalVerificationDtlsBySummaryKey(Long key) {
		Query query = entityManager.createNamedQuery("PhysicalDocumentVerificationDetails.findByDocSummaryKey")
				.setParameter("primaryKey", key);

		List<PhysicalDocumentVerificationDetails> reimbursement =  query.getResultList();
		if(!reimbursement.isEmpty() && reimbursement != null) {
			return reimbursement.get(0);
		}
		return null;
	}
	
	public void savePhysicalVerificationDetails(UploadDocumentDTO uploadDocumentDTO,PhysicalDocumentVerification physicalVerification,Reimbursement reimbursementObjectByKey)
	{
		PhysicalDocumentVerificationDetails physicalVerificationDtls = new PhysicalDocumentVerificationDetails();
		PhysicalDocumentVerificationDetails physicalVerificationDtls1 = getPhysicalVerificationDtlsBySummaryKey(uploadDocumentDTO.getDocSummaryKey());
		
		String verificationFlag = "R";
		if(uploadDocumentDTO.getIsIgnored()){
			verificationFlag = "I" ;
		}
		physicalVerificationDtls.setDocumentVerificationFlag(verificationFlag);
		physicalVerificationDtls.setPhysicalDocKey(physicalVerification.getKey());
		physicalVerificationDtls.setDocumentId(uploadDocumentDTO.getDocSummaryKey());
		physicalVerificationDtls.setActiveStatus("Y");
		physicalVerificationDtls.setReimbursement(reimbursementObjectByKey);
		physicalVerificationDtls.setPhysicalDocRecDate(uploadDocumentDTO.getDocReceivedDate());
		physicalVerificationDtls.setPhyDocRecType(uploadDocumentDTO.getDocumentType().getId());
		
		if(physicalVerificationDtls1 != null){
			physicalVerificationDtls.setKey(physicalVerificationDtls1.getKey());
			physicalVerificationDtls.setModifiedBy(uploadDocumentDTO.getStrUserName());
			physicalVerificationDtls.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(physicalVerificationDtls);

		} else {
			physicalVerificationDtls.setCreatedBy(uploadDocumentDTO.getStrUserName());
			physicalVerificationDtls.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(physicalVerificationDtls);
		}
		entityManager.flush();
		entityManager.clear();
	}
}
