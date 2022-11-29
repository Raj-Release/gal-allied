/**
 * 
 */
package com.shaic.claim.OMPDocumentRelatedService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPViewDocumentDetailsTableDTO;
import com.shaic.claim.OMPProcessOmpAcknowledgementDocuments.Upload.OMPViewUploadDocumentDetailsTableDTO;
import com.shaic.claim.claimhistory.view.ompView.OMPAckDocReceivedMapper;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.ClaimVerification;
import com.shaic.domain.DocumentCheckListMaster;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPDocumentDetails;
import com.shaic.domain.OMPRODDocumentCheckList;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.RODDocumentCheckList;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ReimbursementBenefits;
import com.shaic.domain.ReimbursementBenefitsDetails;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.Status;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Speciality;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.reimbursement.ReimbursementCalCulationDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Notification;

@Stateless
public class OMPAckDocService extends
		AbstractDAO<OMPDocAcknowledgement> {

	@PersistenceContext
	protected EntityManager entityManager;

	private final Logger log = LoggerFactory
			.getLogger(OMPAckDocService.class);

	/*
	 * public Page<SearchAcknowledgementDocumentReceiverTableDTO> search(
	 * SearchAcknowledgementDocumentReceiverFormDTO searchFormDTO, String
	 * userName, String passWord) {
	 * 
	 * 
	 * List<SearchAcknowledgementDocumentReceiverTableDTO> result = new
	 * ArrayList<SearchAcknowledgementDocumentReceiverTableDTO>();
	 * SearchAcknowledgementDocumentReceiverTableDTO
	 * searchAcknowledgementDocumentReceiverTableDTO = new
	 * SearchAcknowledgementDocumentReceiverTableDTO();
	 * searchAcknowledgementDocumentReceiverTableDTO.setIntimationNo("12213");
	 * searchAcknowledgementDocumentReceiverTableDTO.setClaimNo("45645");
	 * searchAcknowledgementDocumentReceiverTableDTO.setCpuCode("4564");
	 * searchAcknowledgementDocumentReceiverTableDTO.setDateOfAdmission(new
	 * Date());
	 * searchAcknowledgementDocumentReceiverTableDTO.setHealthCardNo("54654");
	 * searchAcknowledgementDocumentReceiverTableDTO
	 * .setHospitalAddress("tryrty");
	 * searchAcknowledgementDocumentReceiverTableDTO.setHospitalCity("tytry");
	 * searchAcknowledgementDocumentReceiverTableDTO.setHospitalName("tytry");
	 * searchAcknowledgementDocumentReceiverTableDTO
	 * .setInsuredPatiendName("yttr");
	 * searchAcknowledgementDocumentReceiverTableDTO.setPolicyNo("yty");
	 * searchAcknowledgementDocumentReceiverTableDTO
	 * .setReasonForAdmission("tty");
	 * result.add(searchAcknowledgementDocumentReceiverTableDTO);
	 * Page<SearchAcknowledgementDocumentReceiverTableDTO> page = new
	 * Page<SearchAcknowledgementDocumentReceiverTableDTO>();
	 * 
	 * 
	 * page.setPageItems(result);
	 * 
	 * 
	 * return page; }
	 */

	public List<DocumentCheckListDTO> getDocumentCheckListValues(
			MasterService masterService) {
		int i = 1;
		List<DocumentCheckListMaster> masterDocumentCheckList = masterService
				.getDocumentCheckListValuesByType(SHAConstants.MASTER_TYPE_REIMBURSEMENT);

		OMPAckDocReceivedMapper OMPAckDocMapper = OMPAckDocReceivedMapper
				.getInstance();

		List<DocumentCheckListDTO> documentCheckListDTO = OMPAckDocMapper
				.getMasDocumentCheckList(masterDocumentCheckList);
		List<DocumentCheckListDTO> documentCheckListDTOList = new ArrayList<DocumentCheckListDTO>();
		for (DocumentCheckListDTO documentCheckListDTO1 : documentCheckListDTO) {

			documentCheckListDTO1.setSlNo(documentCheckListDTO
					.indexOf(documentCheckListDTO1) + 1);
			documentCheckListDTOList.add(documentCheckListDTO1);
		}
		return documentCheckListDTOList;
	}

	public List<DocumentCheckListDTO> getRODDocumentList(
			MasterService masterService, OMPDocAcknowledgement objDocAck) {

		List<DocumentCheckListDTO> documentCheckListDTO = null;
		List<RODDocumentCheckList> rodDocCheckList = masterService
				.getOMPRODDocumentListValues(objDocAck);

		if (null != rodDocCheckList && !rodDocCheckList.isEmpty()) {
			// documentCheckListDTO =
			// OMPAckDocReceivedMapper.getRODDocumentCheckList(rodDocCheckList);
		}

		return documentCheckListDTO;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitAckDocReceived(ReceiptOfDocumentsDTO rodDTO) {
		log.info("Submit ACKNOWLEDGEMENT ________________"
				+ (rodDTO.getNewIntimationDTO() != null ? rodDTO
						.getNewIntimationDTO().getIntimationId()
						: "NULL INTIMATION"));
		try {
			saveAcknowledgeDocRecValues(rodDTO);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			Notification.show("Already Submitted. Please Try Another Record.");
		}

	}

	private void saveAcknowledgeDocRecValues(ReceiptOfDocumentsDTO rodDTO) {

		// OMPAckDocReceivedMapper ackDocRecMapper = new
		// OMPAckDocReceivedMapper();
		OMPAckDocReceivedMapper ackDocRecMapper = OMPAckDocReceivedMapper
				.getInstance();
		Boolean isQueryReplyReceived = false;
		Boolean isReconsideration = false;
		Boolean isQueryReplyNo = false;
		Long rodKey = null;
		// rodDTO.getDocumentDetails().se
		/**
		 * Since we dont know the possible status of the ROD, sample values are
		 * set. Later this will be changed.
		 * */
		rodDTO.setStatusKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
		rodDTO.setStageKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		OMPDocAcknowledgement docAck = ackDocRecMapper.getDocAckRecDetails(rodDTO);
		// setClaimedAmountFields(docAck,false ,rodDTO);
		if ((null != rodDTO.getDocumentDetails()
				.getHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getHospitalizationClaimedAmount())))
			docAck.setHospitalizationClaimedAmount(Double.parseDouble(rodDTO
					.getDocumentDetails().getHospitalizationClaimedAmount()));
		if ((null != rodDTO.getDocumentDetails()
				.getPreHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getPreHospitalizationClaimedAmount())))
			docAck.setPreHospitalizationClaimedAmount(Double.parseDouble(rodDTO
					.getDocumentDetails().getPreHospitalizationClaimedAmount()));
		if ((null != rodDTO.getDocumentDetails()
				.getPostHospitalizationClaimedAmount())
				&& !("").equals(rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())
				&& (!("null").equalsIgnoreCase(rodDTO.getDocumentDetails()
						.getPostHospitalizationClaimedAmount())))
			docAck.setPostHospitalizationClaimedAmount(Double
					.parseDouble(rodDTO.getDocumentDetails()
							.getPostHospitalizationClaimedAmount()));

		if (null != rodDTO.getReconsiderRODdto()
				&& null != rodDTO.getReconsiderRODdto().getRodKey()) {

			ReconsiderRODRequestTableDTO reconsiderDTO = rodDTO
					.getReconsiderRODdto();

			docAck.setRodKey(reconsiderDTO.getRodKey());
			rodKey = reconsiderDTO.getRodKey();
			/*
			 * if ((null != reconsiderDTO. getHospitalizationClaimedAmt()))
			 * docAck.setHospitalizationClaimedAmount( reconsiderDTO.
			 * getHospitalizationClaimedAmt()); if ((null != reconsiderDTO
			 * .getPreHospClaimedAmt()))
			 * docAck.setPreHospitalizationClaimedAmount(reconsiderDTO
			 * .getPreHospClaimedAmt()); if ((null != reconsiderDTO
			 * .getPostHospClaimedAmt()))
			 * docAck.setPostHospitalizationClaimedAmount(reconsiderDTO
			 * .getPostHospClaimedAmt());
			 */

			docAck.setHospitalisationFlag(reconsiderDTO
					.getHospitalizationFlag());
			docAck.setPreHospitalisationFlag(reconsiderDTO
					.getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(reconsiderDTO
					.getPostHospitalizationFlag());
			docAck.setPartialHospitalisationFlag(reconsiderDTO
					.getPartialHospitalizationFlag());
			docAck.setLumpsumAmountFlag(reconsiderDTO.getLumpSumAmountFlag());
			docAck.setHospitalCashFlag(reconsiderDTO
					.getAddOnBenefitsHospitalCashFlag());
			docAck.setPatientCareFlag(reconsiderDTO
					.getAddOnBenefitsPatientCareFlag());
			docAck.setHospitalizationRepeatFlag(reconsiderDTO
					.getHospitalizationRepeatFlag());
			docAck.setReconsideredDate((new Timestamp(System
					.currentTimeMillis())));

			if (null != rodDTO.getDocumentDetails()
					.getReasonForReconsideration()) {
				MastersValue masValue = new MastersValue();
				masValue.setKey(rodDTO.getDocumentDetails()
						.getReasonForReconsideration().getId());
				masValue.setValue(rodDTO.getDocumentDetails()
						.getReasonForReconsideration().getValue());
				docAck.setReconsiderationReasonId(masValue);
			}
			if (null != rodDTO.getDocumentDetails()
					.getPaymentCancellationNeededFlag()) {
				docAck.setPaymentCancellationFlag(rodDTO.getDocumentDetails()
						.getPaymentCancellationNeededFlag());
			}
			SelectValue reasonForReconsideration = rodDTO.getDocumentDetails()
					.getReasonForReconsideration();
			if (null != rodDTO.getDocumentDetails()
					.getReasonForReconsideration()) {
				MastersValue masReasonForReconsideration = new MastersValue();
				masReasonForReconsideration.setKey(reasonForReconsideration
						.getId());
				masReasonForReconsideration.setValue(reasonForReconsideration
						.getValue());
				docAck.setReconsiderationReasonId(masReasonForReconsideration);
			}

			isReconsideration = true;

			// setClaimedAmountFields(docAck,true,rodDTO);
		}
		// else if (null != rodDTO.getRodqueryDTO()
		// && null != rodDTO.getRodqueryDTO().getReimbursementKey())
		// {
		// docAck.setRodKey(rodDTO.getRodqueryDTO().getReimbursementKey());
		// isQueryReplyReceived = true;
		//
		// }
		else if (null != rodDTO.getRodqueryDTO()
				&& rodDTO.getRodqueryDTO().getReimbursementKey() != null) {

			isQueryReplyReceived = true;
			RODQueryDetailsDTO rodQueryDetailsDTO = rodDTO.getRodqueryDTO();
			rodKey = rodQueryDetailsDTO.getReimbursementKey();
			docAck.setRodKey(rodKey);
			docAck.setHospitalisationFlag(rodQueryDetailsDTO
					.getHospitalizationFlag());
			docAck.setPreHospitalisationFlag(rodQueryDetailsDTO
					.getPreHospitalizationFlag());
			docAck.setPostHospitalisationFlag(rodQueryDetailsDTO
					.getPostHospitalizationFlag());
			docAck.setPartialHospitalisationFlag(rodQueryDetailsDTO
					.getPartialHospitalizationFlag());
			docAck.setLumpsumAmountFlag(rodQueryDetailsDTO
					.getAddOnBenefitsLumpsumFlag());
			docAck.setHospitalCashFlag(rodQueryDetailsDTO
					.getAddOnBeneftisHospitalCashFlag());
			docAck.setPatientCareFlag(rodQueryDetailsDTO
					.getAddOnBenefitsPatientCareFlag());
			docAck.setHospitalizationRepeatFlag(rodQueryDetailsDTO
					.getHospitalizationRepeatFlag());

			if (null != rodQueryDetailsDTO.getReimbursementQueryKey()) {
				// ReimbursementQuery reimbursmentQuery =
				// getReimbursementQueryData(rodQueryDetailsDTO.getReimbursementQueryKey());
				// reimbursmentQuery.setQueryReply(SHAConstants.YES_FLAG);
				//
				// if(null != reimbursmentQuery)
				// {
				// // reimbursmentQuery.setQueryReplyDate((new Timestamp(System
				// // .currentTimeMillis())));
				// if(null != rodDTO)
				// {
				// // if(reimbursmentQuery.getStage() != null &&
				// reimbursmentQuery.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)){
				// // Status status = new Status();
				// //
				// status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
				// // reimbursmentQuery.setStatus(status);
				// // }else{
				// // Status status = new Status();
				// // status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
				// // reimbursmentQuery.setStatus(status);
				// // }
				// reimbursmentQuery.setModifiedBy(rodDTO.getStrUserName());
				// reimbursmentQuery.setModifiedDate(new
				// Timestamp(System.currentTimeMillis()));
				// }
				// entityManager.merge(reimbursmentQuery);
				// entityManager.flush();
				// log.info("------ReimbursementQuery------>"+reimbursmentQuery+"<------------");
				//
				// }
			}
		}

		docAck.setActiveStatus(1l);
		docAck.setClaim(searchByClaimKey(rodDTO.getClaimDTO().getKey()));
		docAck.setAcknowledgeNumber(rodDTO.getDocumentDetails()
				.getAcknowledgementNumber());
		String strUserName = rodDTO.getStrUserName();
		String userNameForDB = SHAUtils.getUserNameForDB(strUserName);
		docAck.setCreatedBy(userNameForDB);

		/**
		 * As per DB Team(Prakash) suggestion, Updating claim object before
		 * acknowledgement creation, since the status updated by the trigger is
		 * getting overridden by the application in claim table.
		 * 
		 * **/

		OMPClaim claimObj = docAck.getClaim();
		if (null != claimObj) {
			Status status = new Status();
			status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);

			Stage stage = new Stage();
			stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

			if (!((null != docAck && null != docAck.getReconsiderationRequest() && ("Y")
					.equalsIgnoreCase(docAck.getReconsiderationRequest())) || isQueryReplyReceived)) {
				if (null != docAck
						&& null != docAck.getClaim()
						&& null != docAck.getClaim().getClaimType()
						&& docAck
								.getClaim()
								.getClaimType()
								.getKey()
								.equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)
						&& null != docAck.getHospitalisationFlag()
						&& (SHAConstants.YES_FLAG).equalsIgnoreCase(docAck
								.getHospitalisationFlag())) {
					// claimObj.set
					updateProvisionAndClaimStatus(docAck, claimObj, false,
							rodDTO);
				} else if (rodDTO.getIsConversionAllowed() != null
						&& rodDTO.getIsConversionAllowed()) {
					if (claimObj.getStatus().equals(
							ReferenceTable.INTIMATION_REGISTERED_STATUS)) {
						updateProvisionAndClaimStatus(docAck, claimObj, false,
								rodDTO);
					}

					/*
					 * Status claimStatus = new Status();
					 * claimStatus.setKey(ReferenceTable
					 * .CLAIM_REGISTERED_STATUS);
					 * claimObj.setStatus(claimStatus);
					 */

					/**
					 * For those cases where ack is created for claim which got
					 * converted via, convert to OMPReimbursement process, claim
					 * registered date should not be updated. Hence the below
					 * condition of null check is added.
					 * */
					/**
					 * 
					 * Only if the claim is not registered, then the
					 * registration date would be null and hence we update the
					 * status and date. If this is not null, then claim is
					 * already registered and this is a conversion case.
					 * */

					if (null == claimObj.getClaimRegisteredDate()) {
						claimObj.setClaimRegisteredDate((new Timestamp(System
								.currentTimeMillis())));

						Status claimStatus = new Status();
						claimStatus
								.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
						// SECTION IMPLEMENTED FOR COMPREHENSIVE AND UPDATED THE
						// SECTION VALUES IN CLAIM LEVEL...
						SectionDetailsTableDTO sectionDetailsDTO = rodDTO
								.getSectionDetailsDTO();
						if (sectionDetailsDTO != null) {
							claimObj.setClaimSectionCode(sectionDetailsDTO
									.getSection() != null ? sectionDetailsDTO
									.getSection().getCommonValue() : null);
							claimObj.setClaimCoverCode(sectionDetailsDTO
									.getCover() != null ? sectionDetailsDTO
									.getCover().getCommonValue() : null);
							claimObj.setClaimSubCoverCode(sectionDetailsDTO
									.getSubCover() != null ? sectionDetailsDTO
									.getSubCover().getCommonValue() : null);
						}
						claimObj.setStatus(claimStatus);
						/*
						 * Modified by user updation code is commented, since
						 * modified user id and created user id would be diff in
						 * this scenario. This is not required. Hence commenting
						 * based on prakash inputs. *
						 */
						/*
						 * if(rodDTO != null){
						 * claimObj.setModifiedBy(rodDTO.getStrUserName()); }
						 */
						claimObj.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
					}

					/*
					 * if(null != rodDTO) { if(null != rodDTO)
					 * claimObj.setModifiedBy(rodDTO.getStrUserName());
					 * claimObj.setModifiedDate(new Timestamp(System
					 * .currentTimeMillis())); }
					 */

					SectionDetailsTableDTO sectionDetailsDTO = rodDTO
							.getSectionDetailsDTO();
					if (sectionDetailsDTO != null) {
						claimObj.setClaimSectionCode(sectionDetailsDTO
								.getSection() != null ? sectionDetailsDTO
								.getSection().getCommonValue() : null);
						claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO
								.getCover().getCommonValue() : null);
						claimObj.setClaimSubCoverCode(sectionDetailsDTO
								.getSubCover() != null ? sectionDetailsDTO
								.getSubCover().getCommonValue() : null);
					}
					entityManager.merge(claimObj);
					entityManager.flush();
					log.info("------OMPClaim------>" + claimObj + "<------------");

				}
				/**
				 * Added for lumpsum change.
				 * */
				else if (("N")
						.equalsIgnoreCase(docAck.getHospitalisationFlag())
						&& ("N").equalsIgnoreCase(docAck
								.getPartialHospitalisationFlag())
						&& ("N").equalsIgnoreCase(docAck
								.getPreHospitalisationFlag())
						&& ("N").equalsIgnoreCase(docAck
								.getPostHospitalisationFlag())
						&& ("N").equalsIgnoreCase(docAck.getHospitalCashFlag())
						&& ("N").equalsIgnoreCase(docAck.getPatientCareFlag())
						&& ("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag())) {
					updateProvisionAndClaimStatus(docAck, claimObj, true,
							rodDTO);
				}

			}

			/**
			 * As per sathish sir suggestion, in all cases where claim
			 * registered date is null, then only claim registered date will be
			 * updated. Else it won't get updated.
			 * 
			 * */

			if (null == claimObj.getClaimRegisteredDate()) {
				claimObj.setClaimRegisteredDate((new Timestamp(System
						.currentTimeMillis())));
				Status claimStatus = new Status();
				claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
				claimObj.setStatus(claimStatus);

				/*
				 * if(rodDTO != null){
				 * claimObj.setModifiedBy(rodDTO.getStrUserName()); }
				 */
				claimObj.setModifiedDate(new Timestamp(System
						.currentTimeMillis()));

				SectionDetailsTableDTO sectionDetailsDTO = rodDTO
						.getSectionDetailsDTO();
				if (sectionDetailsDTO != null) {
					claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO
							.getSection().getCommonValue() : null);
					claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO
							.getCover().getCommonValue() : null);
					claimObj.setClaimSubCoverCode(sectionDetailsDTO
							.getSubCover() != null ? sectionDetailsDTO
							.getSubCover().getCommonValue() : null);
				}

				entityManager.merge(claimObj);
				entityManager.flush();
			}

			// claimObj.setStatus(status);
			// claimObj.setStage(stage);
			// claimObj.setModifiedDate(new
			// Timestamp(System.currentTimeMillis()));
			// entityManager.merge(claimObj);
			// entityManager.flush();

		}

		entityManager.persist(docAck);
		entityManager.flush();
		log.info("------OMPDocAcknowledgement------>"
				+ docAck.getAcknowledgeNumber() + "<------------");
		entityManager.refresh(docAck);

		/**
		 * If reconsideration request is selected, then the current
		 * acknowledgement needs to be updated in OMPReimbursement table.
		 * */
		if (null != rodKey) {

			OMPReimbursement OMPReimbursement = getReimbursement(rodKey);

			// if(OMPReimbursement.getStatus() != null &&
			// OMPReimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)){
			// Status status = new Status();
			// status.setKey(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS);
			// OMPReimbursement.setStatus(status);
			// }else if(OMPReimbursement.getStatus() != null &&
			// OMPReimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
			// Status status = new Status();
			// status.setKey(ReferenceTable.FA_QUERY_REPLY_STATUS);
			// OMPReimbursement.setStatus(status);
			// }

			String userName = rodDTO.getStrUserName();
			userName = SHAUtils.getUserNameForDB(userName);
			OMPReimbursement.setModifiedBy(userName);
			OMPReimbursement.setDocAcknowLedgement(docAck);
			OMPReimbursement.setModifiedDate(new Timestamp(System
					.currentTimeMillis()));
			entityManager.merge(OMPReimbursement);
			entityManager.flush();
			log.info("------OMPReimbursement------>" + OMPReimbursement
					+ "<------------");
		}

		// if(isQueryReplyReceived)
		// {
		// RODQueryDetailsDTO rodQueryDetailsDTO = rodDTO.getRodqueryDTO();
		// ReimbursementQuery reimbQuery =
		// getReimbursementQuery(rodQueryDetailsDTO.getReimbursementQueryKey());
		// if(("No").equalsIgnoreCase(rodQueryDetailsDTO.getReplyStatus()))
		// {
		// reimbQuery.setQueryReply("N");
		// }
		// else
		// if(("Yes").equalsIgnoreCase(rodQueryDetailsDTO.getReplyStatus()))
		// {
		// reimbQuery.setQueryReply("Y");
		// }
		// reimbQuery.setDocAcknowledgement(docAck);
		// if(null != reimbQuery.getKey())
		// {
		// entityManager.merge(reimbQuery);
		// entityManager.flush();
		// }
		// }else

		Boolean isQueryStatusYes = false;

//		if (isQueryReplyReceived) {
//
//			List<RODQueryDetailsDTO> rodQueryDetailsDTO = rodDTO
//					.getRodQueryDetailsList();
//			for (RODQueryDetailsDTO rodQueryDetailsDTO2 : rodQueryDetailsDTO) {
//				ReimbursementQuery reimbQuery = getReimbursementQuery(rodQueryDetailsDTO2
//						.getReimbursementQueryKey());
//				if (("No").equalsIgnoreCase(rodQueryDetailsDTO2
//						.getReplyStatus())) {
//					reimbQuery.setQueryReply("N");
//				} else if (("Yes").equalsIgnoreCase(rodQueryDetailsDTO2
//						.getReplyStatus())) {
//					reimbQuery.setQueryReply("Y");
//					reimbQuery.setQueryReplyDate(new Timestamp(System
//							.currentTimeMillis()));
//					rodDTO.setCreatedByForQuery(reimbQuery.getCreatedBy());
//					isQueryStatusYes = true;
//				}
//				reimbQuery.setDocAcknowledgement(docAck);
//				if (null != reimbQuery.getKey()) {
//					if (null != rodDTO) {
//						reimbQuery.setModifiedBy(rodDTO.getStrUserName());
//						reimbQuery.setModifiedDate(new Timestamp(System
//								.currentTimeMillis()));
//					}
//					entityManager.merge(reimbQuery);
//					entityManager.flush();
//					log.info("------ReimbursementQuery------>" + reimbQuery
//							+ "<------------");
//				}
//			}
//		}

		List<DocumentCheckListDTO> docCheckList = rodDTO.getDocumentDetails()
				.getDocumentCheckList();
		if (!docCheckList.isEmpty()) {
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
					OMPRODDocumentCheckList rodDocumentCheckList = ackDocRecMapper
							.getRODDocumentCheckList(docCheckListDTO);
					rodDocumentCheckList.setDocAcknowledgement(docAck);
					// findRODDocumentCheckListByKey(masterService);
					entityManager.persist(rodDocumentCheckList);
					entityManager.flush();
					log.info("------RODDocumentCheckList------>"
							+ rodDocumentCheckList + "<------------");
					// }
				}
			}
		}

		/*
		 * OMPClaim claimObj = docAck.getClaim(); if(null != claimObj) { Status
		 * status = new Status();
		 * status.setKey(ReferenceTable.ACKNOWLEDGE_STATUS_KEY);
		 * 
		 * Stage stage = new Stage();
		 * stage.setKey(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		 * 
		 * if(!((null != docAck && null != docAck.getReconsiderationRequest() &&
		 * ("Y").equalsIgnoreCase(docAck.getReconsiderationRequest())) ||
		 * isQueryReplyReceived)) { if(null != docAck && null !=
		 * docAck.getClaim() && null != docAck.getClaim().getClaimType() &&
		 * docAck.getClaim().getClaimType().getKey().equals(ReferenceTable.
		 * REIMBURSEMENT_CLAIM_TYPE_KEY) && null !=
		 * docAck.getHospitalisationFlag() &&
		 * (SHAConstants.YES_FLAG).equalsIgnoreCase
		 * (docAck.getHospitalisationFlag())) {
		 * updateProvisionAndClaimStatus(docAck, claimObj); } else
		 * if(rodDTO.getIsConversionAllowed() != null &&
		 * rodDTO.getIsConversionAllowed()) {
		 * if(claimObj.getStatus().equals(ReferenceTable
		 * .INTIMATION_REGISTERED_STATUS)) {
		 * updateProvisionAndClaimStatus(docAck, claimObj); }
		 * 
		 * Status claimStatus = new Status();
		 * claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
		 * claimObj.setStatus(claimStatus);
		 * 
		 * claimObj.setClaimRegisteredDate((new Timestamp(System
		 * .currentTimeMillis())));
		 * 
		 * if(null != rodDTO) { if(null != rodDTO)
		 * claimObj.setModifiedBy(rodDTO.getStrUserName());
		 * claimObj.setModifiedDate(new Timestamp(System .currentTimeMillis()));
		 * } entityManager.merge(claimObj); entityManager.flush();
		 * log.info("------OMPClaim------>"+claimObj+"<------------");
		 * 
		 * } }
		 * 
		 * // claimObj.setStatus(status); // claimObj.setStage(stage); //
		 * claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		 * // entityManager.merge(claimObj); // entityManager.flush(); }
		 */
		if (null != rodDTO.getDocFilePath()
				&& !("").equalsIgnoreCase(rodDTO.getDocFilePath())) {
			WeakHashMap dataMap = new WeakHashMap();
			dataMap.put("intimationNumber", docAck.getClaim().getIntimation()
					.getIntimationId());
			OMPClaim objClaim = getClaimByClaimKey(docAck.getClaim().getKey());
			if (null != objClaim) {
				dataMap.put("claimNumber", objClaim.getClaimId());
				if (null != objClaim.getClaimType()) {
					if ((ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
							.equals(objClaim.getClaimType().getKey())) {
						Preauth preauth = getPreauthClaimKey(objClaim.getKey());
						if (null != preauth)
							dataMap.put("cashlessNumber",
									preauth.getPreauthId());
					}
				}
			}
			dataMap.put("filePath", rodDTO.getDocFilePath());
			dataMap.put("docType", rodDTO.getDocType());
			dataMap.put("docSources", SHAConstants.ACKNOWLEDGE_DOC_RECEIVED);
			dataMap.put("createdBy", rodDTO.getStrUserName());
			SHAUtils.uploadGeneratedLetterToDMS(entityManager, dataMap);
		}
		submitTaskToBPM(rodDTO, docAck, isQueryStatusYes, isReconsideration);
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

	private void updateProvisionAndClaimStatus(OMPDocAcknowledgement docAck,
			OMPClaim claimObj, Boolean isLumpSumOnly, ReceiptOfDocumentsDTO rodDTO) {
		if (!isLumpSumOnly) {
			Double totalClaimedAmt = 0d;
			if (null != docAck.getHospitalizationClaimedAmount()) {
				totalClaimedAmt += docAck.getHospitalizationClaimedAmount();
			}
			if (null != docAck.getPreHospitalizationClaimedAmount()) {
				totalClaimedAmt += docAck.getPreHospitalizationClaimedAmount();
			}
			if (null != docAck.getPostHospitalizationClaimedAmount()) {
				totalClaimedAmt += docAck.getPostHospitalizationClaimedAmount();
			}

			DBCalculationService dbCalculationService = new DBCalculationService();
			if (null != docAck.getClaim()
					&& null != docAck.getClaim().getIntimation()
					&& null != docAck.getClaim().getIntimation().getInsured()
					&& null != docAck.getClaim().getIntimation().getInsured()
							.getInsuredId()
					&& null != docAck.getClaim().getIntimation().getPolicy()
							.getKey()) {
				Double insuredSumInsured = dbCalculationService
						.getInsuredSumInsured(docAck.getClaim().getIntimation()
								.getInsured().getInsuredId().toString(), docAck
								.getClaim().getIntimation().getPolicy()
								.getKey(),docAck.getClaim().getIntimation()
								.getInsured().getLopFlag());
				Double balSI = dbCalculationService
						.getBalanceSI(
								docAck.getClaim().getIntimation().getPolicy()
										.getKey(),
								docAck.getClaim().getIntimation().getInsured()
										.getKey(), docAck.getClaim().getKey(),
								insuredSumInsured,
								docAck.getClaim().getIntimation().getKey())
						.get(SHAConstants.TOTAL_BALANCE_SI);
				Double amt = Math.min(balSI, totalClaimedAmt);

				claimObj.setCurrentProvisionAmount(amt);
				claimObj.setClaimedAmount(amt);
				claimObj.setProvisionAmount(amt);
			}
		} else if (isLumpSumOnly) {
			DBCalculationService dbCalculationService = new DBCalculationService();
			Long productKey = 0l;
			if (null != claimObj.getIntimation().getPolicy().getProduct())
				productKey = claimObj.getIntimation().getPolicy().getProduct()
						.getKey();
			Double lumpSumAmt = dbCalculationService.getLumpSumAmount(
					productKey, docAck.getClaim().getKey(),
					ReferenceTable.LUMPSUM_SUB_COVER_CODE);
			if (null != claimObj) {
				claimObj.setCurrentProvisionAmount(lumpSumAmt);
				claimObj.setClaimedAmount(lumpSumAmt);
				claimObj.setProvisionAmount(lumpSumAmt);
			}
		}

		/*
		 * Status claimStatus = new Status();
		 * claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
		 * claimObj.setStatus(claimStatus);
		 */
		/**
		 * As per the rule, Lumpsum is going to be first and last rod for an
		 * intimation. Hence for first rod, this below validation will happen
		 * irrespective of any classification.
		 * 
		 * */
		if (null == claimObj.getClaimRegisteredDate()) {
			Status claimStatus = new Status();
			claimStatus.setKey(ReferenceTable.CLAIM_REGISTERED_STATUS);
			claimObj.setStatus(claimStatus);

			claimObj.setClaimRegisteredDate((new Timestamp(System
					.currentTimeMillis())));

			claimObj.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		}
		claimObj.setNormalClaimFlag("N");
		claimObj.setDocumentReceivedDate(new Timestamp(System
				.currentTimeMillis()));

		SectionDetailsTableDTO sectionDetailsDTO = rodDTO
				.getSectionDetailsDTO();
		if (sectionDetailsDTO != null) {
			claimObj.setClaimSectionCode(sectionDetailsDTO.getSection() != null ? sectionDetailsDTO
					.getSection().getCommonValue() : null);
			claimObj.setClaimCoverCode(sectionDetailsDTO.getCover() != null ? sectionDetailsDTO
					.getCover().getCommonValue() : null);
			claimObj.setClaimSubCoverCode(sectionDetailsDTO.getSubCover() != null ? sectionDetailsDTO
					.getSubCover().getCommonValue() : null);
		}

		entityManager.merge(claimObj);
		entityManager.flush();
		log.info("------OMPClaim------>" + claimObj + "<------------");
	}

	private ReimbursementQuery getReimbursementQueryData(Long queryKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByKey");
		query = query.setParameter("primaryKey", queryKey);
		List<ReimbursementQuery> reimbursementQueryList = query.getResultList();
		if (null != reimbursementQueryList && !reimbursementQueryList.isEmpty()) {
			entityManager.refresh(reimbursementQueryList.get(0));
			return reimbursementQueryList.get(0);
		}
		return null;
	}

	/*
	 * public void uploadGeneratedLetterToDMS(EntityManager
	 * entityManager,HashMap dataMap) { HashMap fileUploadMap =
	 * SHAUtils.uploadFileToDMS((String)dataMap.get("filePath")); if(null !=
	 * fileUploadMap && !fileUploadMap.isEmpty()) { String docToken =
	 * (String)fileUploadMap.get("fileKey"); String fileName =
	 * (String)fileUploadMap.get("fileName"); dataMap.put("fileKey", docToken);
	 * dataMap.put("fileName", fileName); dataMap.put("fileSize",
	 * (Long)fileUploadMap.get("fileSize"));
	 * populateDocumentDetailsObject(entityManager , dataMap); } }
	 * 
	 * public void populateDocumentDetailsObject(EntityManager
	 * entityManager,HashMap fileUploadMap) { DocumentDetails docDetails = new
	 * DocumentDetails(); String docToken =
	 * (String)fileUploadMap.get("fileKey"); docDetails.setDocumentToken((null
	 * != docToken && !docToken.isEmpty())? Long.parseLong(docToken):null);
	 * docDetails
	 * .setIntimationNumber((String)fileUploadMap.get("intimationNumber"));
	 * docDetails.setClaimNumber((String)fileUploadMap.get("claimNumber"));
	 * docDetails
	 * .setCashlessNumber((String)fileUploadMap.get("cashlessNumber"));
	 * docDetails.setDocumentType((String)fileUploadMap.get("docType"));
	 * docDetails.setDocumentSource((String)fileUploadMap.get("docSource"));
	 * docDetails.setSfFileSize((Long)fileUploadMap.get("fileSize"));
	 * docDetails.setSfFileName((String)fileUploadMap.get("fileName"));
	 * docDetails.setDocSubmittedDate((new Timestamp(System
	 * .currentTimeMillis()))); docDetails.setDocAcknowledgementDate((new
	 * Timestamp(System .currentTimeMillis())));
	 * entityManager.persist(docDetails); entityManager.flush(); }
	 */

	/**
	 * Commenting temporarily.
	 * */

	/*
	 * private void setClaimedAmountFields(OMPDocAcknowledgement docAck , Boolean
	 * isReconsiderationRequest, ReceiptOfDocumentsDTO rodDTO) {
	 * 
	 * if(isReconsiderationRequest) {
	 * 
	 * } else {
	 * 
	 * } }
	 */
	
	public void submitTaskFromConvertToROD(ReceiptOfDocumentsDTO rodDTO,
			OMPDocAcknowledgement docAck, Boolean isQueryReply,
			Boolean isReconsideration,EntityManager entityManager){
		this.entityManager = entityManager;
		submitTaskToBPM(rodDTO, docAck, isQueryReply, isReconsideration);
		
	}
	
	
	public void submitTaskToBPM(ReceiptOfDocumentsDTO rodDTO,
			OMPDocAcknowledgement docAck, Boolean isQueryReply,
			Boolean isReconsideration) {/*
		Acknowledgement acknowledgemnetTask = BPMClientContext
				.getAcknowledgementTask(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);

		InitiateAckProcessPayloadType payload = new InitiateAckProcessPayloadType();

		PayloadBOType payloadBO = new PayloadBOType();

		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(rodDTO.getClaimDTO()
				.getNewIntimationDto().getIntimationId());

		intimationType.setIntimationSource(rodDTO.getClaimDTO()
				.getNewIntimationDto().getIntimationSource().getValue());

		if (rodDTO.getClaimDTO().getNewIntimationDto().getAdmissionDate() != null) {
			String intimDate = SHAUtils.formatIntimationDateValue(rodDTO
					.getClaimDTO().getNewIntimationDto().getAdmissionDate());
			intimationType.setStatus(intimDate);
		}

		PolicyType policyType = new PolicyType();
		policyType.setPolicyId(rodDTO.getClaimDTO().getNewIntimationDto()
				.getPolicy().getPolicyNumber());

		ProductInfoType productInfo = new ProductInfoType();
		productInfo.setLob("HEALTH");

		if (rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct()
				.getKey() != null) {
			productInfo.setProductId(rodDTO.getClaimDTO().getNewIntimationDto()
					.getPolicy().getProduct().getKey().toString());
		}

		productInfo.setProductName(rodDTO.getClaimDTO().getNewIntimationDto()
				.getPolicy().getProduct().getValue());

		*//**
		 * @author yosuva.a
		 *//*
		ClaimType claim = new ClaimType();
		claim.setClaimId(docAck.getClaim().getClaimId());
		claim.setKey(docAck.getClaim().getKey());
		if (docAck.getClaim().getClaimType() != null) {
			claim.setClaimType(docAck.getClaim().getClaimType().getValue() != null ? docAck
					.getClaim().getClaimType().getValue().toString()
					.toUpperCase()
					: "OMPReimbursement");
		}

		ClaimRequestType claimRequest = new ClaimRequestType();

		claimRequest.setKey(rodDTO.getClaimDTO().getKey());
		claimRequest.setCpuCode(rodDTO.getClaimDTO().getNewIntimationDto()
				.getCpuCode());
		claimRequest.setClaimRequestType("All");
		claimRequest.setClientType("ACK");

		DocReceiptACKType docReceiptAck = new DocReceiptACKType();
		docReceiptAck.setAckNumber(docAck.getAcknowledgeNumber());
		docReceiptAck.setKey(docAck.getKey());

		if (null != rodDTO.getIsConversionAllowed()
				&& rodDTO.getIsConversionAllowed()) {
			docReceiptAck
					.setStatus(SHAConstants.PRE_AUTH_STATUS_FOR_CONVERSION);
		} else {
			docReceiptAck.setStatus(docAck.getStatus().getProcessValue());
		}
		if (("Y").equalsIgnoreCase(docAck.getHospitalisationFlag()))
			docReceiptAck.setHospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getPostHospitalisationFlag()))
			docReceiptAck.setPosthospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getPreHospitalisationFlag()))
			docReceiptAck.setPrehospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getPartialHospitalisationFlag()))
			docReceiptAck.setPartialhospitalization(true);
		if (("Y").equalsIgnoreCase(docAck.getLumpsumAmountFlag()))
			docReceiptAck.setLumpsumamount(true);
		if (("Y").equalsIgnoreCase(docAck.getHospitalCashFlag()))
			docReceiptAck.setAddonbenefitshospcash(true);
		if (("Y").equalsIgnoreCase(docAck.getPatientCareFlag()))
			docReceiptAck.setAddonbenefitspatientcare(true);
		if (("Y").equalsIgnoreCase(docAck.getHospitalizationRepeatFlag())) {
			*//**
			 * Instead of below line, add
			 * docReceiptAck.setPartialhospitalization(true);
			 * *//*
			*//***
			 * Below line added for hospitalization repeat flow. Repeat should
			 * follow normal flow from create rod, bill entry zmr.
			 *//*
			docReceiptAck.setPartialhospitalization(true);

			// docReceiptAck.setHospitalization(true);
		}

		HospitalInfoType hospInfoType = new HospitalInfoType();

		
		 * String hospitalType =
		 * rodDTO.getClaimDTO().getNewIntimationDto().getHospitalDto
		 * ().getRegistedHospitals().getHospitalType().getValue(); if(null !=
		 * hospitalType &&
		 * (SHAConstants.NETWORK_HOSPITAL_TYPE).equalsIgnoreCase(hospitalType))
		 * { hospInfoType.setHospitalType("NET"); } else if(null != hospitalType
		 * && ("Non-Network").equalsIgnoreCase(hospitalType)) {
		 * hospInfoType.setHospitalType("NONNT"); } else if(null != hospitalType
		 * &&
		 * (SHAConstants.NOT_REGISTERED_HOSPITAL_TYPE).equalsIgnoreCase(hospitalType
		 * )) { hospInfoType.setHospitalType("NOTREGISTERED"); }
		 * hospInfoType.setHospitalType(hospitalType);
		 

		hospInfoType.setHospitalType(rodDTO.getClaimDTO().getNewIntimationDto()
				.getHospitalDto().getRegistedHospitals().getHospitalType()
				.getValue());
		hospInfoType.setNetworkHospitalType(rodDTO.getClaimDTO()
				.getNewIntimationDto().getHospitalDto().getRegistedHospitals()
				.getNetworkHospitalType());

		// ClaimRequestType claimRequestType = new ClaimRequestType();
		// /claimRequestType.setCpuCode(rodDTO.getClaimDTO().getNewIntimationDto().getCpuCode());

		OMPClaim claimObj = entityManager.find(OMPClaim.class, rodDTO.getClaimDTO()
				.getKey());

		entityManager.refresh(claimObj);

		Insured insured = claimObj.getIntimation().getInsured();

		ClassificationType classificationType = new ClassificationType();
		if (claimObj != null && claimObj.getIsVipCustomer() != null
				&& claimObj.getIsVipCustomer().equals(1l)) {

			classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		} else if (insured != null && insured.getInsuredAge() != null
				&& insured.getInsuredAge() > 60) {
			classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
		} else {
			classificationType.setPriority(SHAConstants.NORMAL);
		}

		if (isReconsideration) {
			classificationType.setType(SHAConstants.RECONSIDERATION);
			claimRequest.setIsReconsider(false);
			claimRequest.setClientType(SHAConstants.MEDICAL);
			claimRequest
					.setReimbReqBy(SHAConstants.RECONSIDERATION_REIMB_REPLY_BY);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);

			classificationType.setSource(SHAConstants.RECONSIDERATION);
		} else if (isQueryReply) {
			claimRequest.setIsReconsider(false);
			classificationType.setType(SHAConstants.QUERY_REPLY);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser(rodDTO.getCreatedByForQuery());
			payloadBO.setProcessActorInfo(processActor);
			classificationType.setSource(SHAConstants.QUERY_REPLY);
		} else {
			claimRequest.setIsReconsider(false);
			classificationType.setType(SHAConstants.TYPE_FRESH);
			ProcessActorInfoType processActor = new ProcessActorInfoType();
			processActor.setEscalatedByUser("");
			payloadBO.setProcessActorInfo(processActor);
		}

		// classificationType.setSource(SHAConstants.NORMAL);
		if (!isReconsideration && !isQueryReply) {
			classificationType.setSource(docAck.getStatus().getProcessValue());
		}

		payloadBO.setIntimation(intimationType);
		payloadBO.setPolicy(policyType);
		payloadBO.setClaimRequest(claimRequest);
		payloadBO.setDocReceiptACK(docReceiptAck);
		payloadBO.setClaim(claim);
		payloadBO.setHospitalInfo(hospInfoType);
		payloadBO.setProductInfo(productInfo);
		payloadBO.setClassification(classificationType);
		// payload.setPayloadBO(payloadBO);
		// acknowledgemnetTask.initiate(rodDTO.getStrUserName(), payloadBO,
		// "submit");
		// acknowledgemnetTask.execute(rodDTO.getStrUserName(), payloadBO,
		// "submit",ReferenceTable.ACK_TASK_BPM_FORM_NAME);
		// acknowledgemnetTask.execute(rodDTO.getStrUserName(), payloadBO);
		try {
			
			
			if (null != rodDTO.getIsConversionAllowed()
					&& rodDTO.getIsConversionAllowed()) {
				
				Hospitals hospitals = getHospitalById(claimObj.getIntimation().getHospital());
								
				Object[] arrayListForDBCall = SHAUtils.getOMPArrayListForDBCall(claimObj, hospitals);
				
				Object[] inputArray = (Object[])arrayListForDBCall[0];

				inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTCOME_ACK_CONVERSION;
				inputArray[SHAConstants.INDEX_REFERENCE_USER_ID] = docAck.getKey().toString();
				
				Object[] parameter = new Object[1];
				parameter[0] = inputArray;
				
				DBCalculationService dbCalculationService = new DBCalculationService();
				
				dbCalculationService.initiateTaskProcedure(parameter);
				
				
			}else{
				acknowledgemnetTask.initiate(BPMClientContext.BPMN_TASK_USER,
						payloadBO);
			}
			
			

			// CancelAcknowledgement cancelAcknowledgement =
			// BPMClientContext.intiateAcknowledgmentTask(SHAConstants.WEB_LOGIC,rodDTO.getStrPassword());
			// cancelAcknowledgement.initiate(SHAConstants.WEB_LOGIC,
			// payloadBO);

		} catch (Exception e) {
			e.printStackTrace();

			log.error("&@#(#(#(#($#&@)()@!#* BPMN ERROR IN CREATE_ROD STAGE (#)#)#)#)#)#)%(%^)#)#"
					+ e.toString());

			try {
				acknowledgemnetTask.initiate("claimshead", payloadBO);

			} catch (Exception u) {
				log.error("*#*#*#*# SECOND SUBMIT ERROR IN CREATE ROD (#*#&*#*#*#*#*#*#");
			}
		}

	*/}
	
	private Hospitals getHospitalById(Long key) {

		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);

		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0);
		}

		return null;

	}

	public Boolean isConversionAllowed(Preauth preauth) {
		Boolean allowConversion = false;
		Long valueForConversion = ReferenceTable.getConversionStatusMap().get(
				preauth.getStatus().getKey());
		if (null != valueForConversion) {
			allowConversion = true;
		} else if (null != preauth.getClaim()) {
			Long conversionVal = ReferenceTable.getConversionStatusMap().get(
					preauth.getStatus().getKey());
			if (null != conversionVal) {
				allowConversion = true;
			}
		}
		return allowConversion;
	}

	public Boolean getWaitingForPreauthTask(OMPClaim objClaim) {
		Boolean isWaitingForPreauth = false;
		/*
		Boolean isWaitingForPreauth = false;
		ReceivePreAuthTask receivePreauthTask = BPMClientContext
				.getPreAuthReceived(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);

		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = receivePreauthTask
				.getTasks(BPMClientContext.BPMN_TASK_USER, new Pageable(),
						payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if (null != humanTaskList && !humanTaskList.isEmpty()) {
			for (HumanTask humanTask : humanTaskList) {
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask
						.getPayloadCashless();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj
						.getClaim();
				// String claimId = claimType.getClaimId();
				Long claimKey = claimType.getKey();
				if (objClaim.getKey().equals(claimKey)) {
					isWaitingForPreauth = true;
					break;
				}
			}
		}
	*/	return isWaitingForPreauth;
	}

	public Boolean getConvertClaimTask(OMPClaim objClaim) {
		Boolean isWaitingForConversion = false;/*
		AckProcessConvertClaimToReimbTask ackProcessConvertClaim = BPMClientContext
				.getConvertClaimTaskFromAck(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		PayloadBOType payloadBO = new PayloadBOType();
		IntimationType objIntimationType = new IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = ackProcessConvertClaim
				.getTasks("claimshead", new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if (null != humanTaskList && !humanTaskList.isEmpty()) {
			for (HumanTask humanTask : humanTaskList) {
				PayloadBOType payloadBOObj = humanTask.getPayload();
				ClaimType claimType = payloadBOObj.getClaim();
				Long claimKey = claimType.getKey();
				if (objClaim.getKey().equals(claimKey)) {
					isWaitingForConversion = true;
					break;
				}
			}
		}*/
		return isWaitingForConversion;

	}
	
public Boolean getDBTaskForPreauth(Intimation intimation,String currentQ){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, currentQ);
		
		Object[] setMapValues = SHAUtils.setObjArrayForGetTask(mapValues);
		
		DBCalculationService db = new DBCalculationService();
		 List<Map<String, Object>> taskProcedure = db.getTaskProcedure(setMapValues);
		if (taskProcedure != null && !taskProcedure.isEmpty()){
			return true;
		} 
		return false;
	}

	public Boolean getProcessRejectionTaks(OMPClaim objClaim, String userName,
			String password) {
		Boolean isWaitingInRejectionQ = false;
		/*com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);

		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = null;
		com.shaic.ims.bpm.claim.servicev2.registration.search.ProcessRejectionTask processRejectionTask = BPMClientContext
				.getProcessRejectionTask(userName, password);
		// com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks =
		// processRejectionTask.getTasks(userName, pageable, payloadBO);
		tasks = processRejectionTask.getTasks("claimshead", new Pageable(),
				payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		{
			if (null != humanTaskList && !humanTaskList.isEmpty()) {
				for (HumanTask humanTask2 : humanTaskList) {
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask2
							.getPayloadCashless();
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationType = payloadBOObj
							.getIntimation();
					if (null != intimationType) {
						String intimationId = intimationType
								.getIntimationNumber();
						if (null != intimationId
								&& (intimationId).equalsIgnoreCase(objClaim
										.getIntimation().getIntimationId())) {
							isWaitingInRejectionQ = true;
							break;
						}
					}

				}

			}

		}

		if (!isWaitingInRejectionQ) {
			ProcessRejectionNonMedicalTask processRejectionNonMedicalTask = BPMClientContext
					.getProcessRejectionNonMedicalTask(userName, password);
			PagedTaskList nonMedicalTask = processRejectionNonMedicalTask
					.getTasks(userName, new Pageable(), payloadBO);
			List<HumanTask> nonMedicalHumanTaksList = nonMedicalTask
					.getHumanTasks();
			if (null != nonMedicalHumanTaksList
					&& !nonMedicalHumanTaksList.isEmpty()) {
				for (HumanTask humanTask : nonMedicalHumanTaksList) {
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask
							.getPayloadCashless();
					com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType intimationType = payloadBOObj
							.getIntimation();
					if (null != intimationType) {
						String intimationId = intimationType
								.getIntimationNumber();
						if (null != intimationId
								&& (intimationId).equalsIgnoreCase(objClaim
										.getIntimation().getIntimationId())) {
							isWaitingInRejectionQ = true;
							break;
						}
					}

				}
			}

		}*/

		return isWaitingInRejectionQ;

	}

	public Boolean getConvertClaimTaskForROD(OMPClaim objClaim) {
		Boolean isWaitingForConversion = false;/*
		com.shaic.ims.bpm.claim.servicev2.conversion.search.ClaimConvTask ackProcessConvertClaim = BPMClientContext
				.getConvertClaimSearchTask(BPMClientContext.BPMN_TASK_USER,
						BPMClientContext.BPMN_PASSWORD);
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBO = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType();
		com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType objIntimationType = new com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.intimation.IntimationType();
		objIntimationType.setIntimationNumber(objClaim.getIntimation()
				.getIntimationId());
		payloadBO.setIntimation(objIntimationType);
		com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = ackProcessConvertClaim
				.getTasks("claimshead", new Pageable(), payloadBO);
		List<HumanTask> humanTaskList = tasks.getHumanTasks();
		if (null != humanTaskList && !humanTaskList.isEmpty()) {
			for (HumanTask humanTask : humanTaskList) {
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.payloadbo.PayloadBOType payloadBOObj = humanTask
						.getPayloadCashless();
				com.oracle.xmlns.bpm.bpmobject.claimregcabusinessobjects.claim.ClaimType claimType = payloadBOObj
						.getClaim();
				PreAuthReqType preauthReqType = payloadBOObj.getPreAuthReq();
				Long claimKey = null;
				*//**
				 * For those cases which as moved to convert claim to reimb as
				 * per the date of admission rule, the claim key would be in
				 * claim type. Hence first claim key from claim type is obtained
				 * and if thats null then we get from preauth req type.
				 * *//*
				if (null != claimType && null != claimType.getKey()) {
					claimKey = claimType.getKey();
				} else if (null != preauthReqType.getKey()) {
					claimKey = preauthReqType.getKey();
				}

				// Long claimKey = preauthReqType.getKey();
				if (objClaim.getKey().equals(claimKey)) {
					isWaitingForConversion = true;
					break;
				}
			}
		}*/
		return isWaitingForConversion;

	}

	private RODDocumentCheckList findRODDocumentCheckListByKey(Long key) {
		RODDocumentCheckList documentChkLst = null;
		Query query = entityManager
				.createNamedQuery("RODDocumentCheckList.findByKey");
		query.setParameter("docListKey", key);
		if (null != query.getSingleResult()) {
			documentChkLst = (RODDocumentCheckList) query.getSingleResult();
		}
		return documentChkLst;
	}

//	public ViewRejectionDTO getViewRejectionDTO(Long key) {
//
//		Query query = entityManager
//				.createNamedQuery("ReimbursementRejection.findByKey");
//		query.setParameter("primaryKey", key);
//
//		ReimbursementRejection rejectionDetails = (ReimbursementRejection) query
//				.getSingleResult();
//
//		if (rejectionDetails != null) {
//			entityManager.refresh(rejectionDetails);
//		}
//
//		ViewRejectionDTO viewRejectionDto = OMPEarlierRodMapper.getInstance()
//				.getRejectionDTO(rejectionDetails);
//		if (viewRejectionDto.getRejectedDate() != null) {
//
//			viewRejectionDto.setRejectedDate(SHAUtils
//					.formatDate(rejectionDetails.getCreatedDate()));
//		}
//		if (viewRejectionDto.getAdmissionDate() != null) {
//			viewRejectionDto.setAdmissionDate(SHAUtils
//					.formatDate(rejectionDetails.getReimbursement().getClaim()
//							.getIntimation().getAdmissionDate()));
//		}
//		if (rejectionDetails.getRejectionDraftDate() != null) {
//			viewRejectionDto.setDraftedDate(SHAUtils
//					.formatDate(rejectionDetails.getRejectionDraftDate()));
//		}
//		if (rejectionDetails.getRedraftDate() != null) {
//			viewRejectionDto.setReDraftedDate(SHAUtils
//					.formatDate(rejectionDetails.getRedraftDate()));
//		}
//		if (rejectionDetails.getApprovedRejectionDate() != null) {
//			viewRejectionDto.setReDraftedDate(SHAUtils
//					.formatDate(rejectionDetails.getApprovedRejectionDate()));
//		}
//
//		if (rejectionDetails.getDisapprovedDate() != null) {
//			viewRejectionDto.setReDraftedDate(SHAUtils
//					.formatDate(rejectionDetails.getDisapprovedDate()));
//		}
//
//		return viewRejectionDto;
//
//	}

	public ReimbursementRejection getReimbursementRejection(Long key) {

		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByKey");
		query.setParameter("primaryKey", key);

		List<ReimbursementRejection> rejectionDetails = (List<ReimbursementRejection>) query
				.getResultList();

		for (ReimbursementRejection reimbursementRejection : rejectionDetails) {
			entityManager.refresh(reimbursementRejection);
		}

		if (rejectionDetails != null && !rejectionDetails.isEmpty()) {
			return rejectionDetails.get(0);
		}
		return null;

	}

	public Boolean isRejectionExistOrNot(Long key) {

		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);

		List<ReimbursementRejection> rejectionDetails = (List<ReimbursementRejection>) query
				.getResultList();

		if (rejectionDetails != null && !rejectionDetails.isEmpty()) {
			return true;
		}

		return false;

	}

	public ReimbursementRejection getRejection(Long key) {

		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
		query.setParameter("reimbursementKey", key);

		List<ReimbursementRejection> rejectionDetails = (List<ReimbursementRejection>) query
				.getResultList();

		if (rejectionDetails != null && !rejectionDetails.isEmpty()) {
			return rejectionDetails.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public List<OMPReimbursement> getRimbursementDetails(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();

		for (OMPReimbursement OMPReimbursement : reimbursementList) {
			entityManager.refresh(OMPReimbursement);
		}

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public List<ViewTmpReimbursement> getViewTmpRimbursementDetails(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<ViewTmpReimbursement> reimbursementList = (List<ViewTmpReimbursement>) query
				.getResultList();

		for (ViewTmpReimbursement OMPReimbursement : reimbursementList) {
			entityManager.refresh(OMPReimbursement);
		}

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public ViewTmpClaimPayment getRimbursementForPayment(String claimNumber) {

		Query query = entityManager
				.createNamedQuery("ViewTmpClaimPayment.findByClaimNumber");
		query.setParameter("claimNumber", claimNumber);

		List<ViewTmpClaimPayment> reimbursementList = (List<ViewTmpClaimPayment>) query
				.getResultList();

		if (reimbursementList != null && !reimbursementList.isEmpty()) {
			return reimbursementList.get(0);
		}

		return null;

	}

	public BankMaster getBankDetails(Long key) {
		Query query = entityManager.createNamedQuery("BankMaster.findByKey");
		query.setParameter("key", key);

		BankMaster bankMaster = (BankMaster) query.getSingleResult();

		return bankMaster;
	}

	/**
	 * Below method fetches list of records from OMPReimbursement based on claim
	 * key. From that list , only latest OMPReimbursement record will be taken into
	 * consideration and that latest record will be returned by this method.
	 * */

	@SuppressWarnings("unchecked")
	public OMPReimbursement getLatestReimbursementDetails(Long claimKey) {

		OMPReimbursement OMPReimbursement = null;
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			OMPReimbursement = reimbursementList.get(0);
		}

		return OMPReimbursement;

	}

	@SuppressWarnings("unchecked")
	public ViewTmpReimbursement getLatestViewTmpReimbursementDetails(
			Long claimKey) {

		ViewTmpReimbursement OMPReimbursement = null;
		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<ViewTmpReimbursement> reimbursementList = (List<ViewTmpReimbursement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			OMPReimbursement = reimbursementList.get(0);
		}

		return OMPReimbursement;

	}

	@SuppressWarnings("unchecked")
	public ViewTmpReimbursement getLatestViewTmpReimbursementSettlementDetails(
			Long claimKey) {

		ViewTmpReimbursement OMPReimbursement = null;
		Query query = entityManager
				.createNamedQuery("ViewTmpReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<ViewTmpReimbursement> reimbursementList = (List<ViewTmpReimbursement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {

			for (ViewTmpReimbursement viewTmpReimbursement : reimbursementList) {
				if (viewTmpReimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_SETTLED)) {
					OMPReimbursement = viewTmpReimbursement;
					break;
				}
			}

		}

		return OMPReimbursement;

	}

	public ViewTmpClaimPayment getClaimPaymentByRodNumber(String rodNumber) {

		Query query = entityManager
				.createNamedQuery("ViewTmpClaimPayment.findByRodNo");
		query.setParameter("rodNumber", rodNumber);

		List<ViewTmpClaimPayment> result = (List<ViewTmpClaimPayment>) query
				.getResultList();

		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public List<OMPReimbursement> getReimbursementDetailsForBillClassificationValidation(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();

		for (OMPReimbursement OMPReimbursement : reimbursementList) {
			entityManager.refresh(OMPReimbursement);
		}

		/*
		 * if(null != reimbursementList && !reimbursementList.isEmpty()) {
		 * reimbursementFinalList = new ArrayList<OMPReimbursement>(); for
		 * (OMPReimbursement OMPReimbursement : reimbursementList) {
		 * 
		 * } //OMPReimbursement = reimbursementList.get(0); }
		 */

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public List<OMPReimbursement> getReimbursementDetailsForBillClassificationValidationWithoutCancelAck(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findLatestNonCanceledRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		query.setParameter("statusId",
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();

		for (OMPReimbursement OMPReimbursement : reimbursementList) {
			entityManager.refresh(OMPReimbursement);
		}

		/*
		 * if(null != reimbursementList && !reimbursementList.isEmpty()) {
		 * reimbursementFinalList = new ArrayList<OMPReimbursement>(); for
		 * (OMPReimbursement OMPReimbursement : reimbursementList) {
		 * 
		 * } //OMPReimbursement = reimbursementList.get(0); }
		 */

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public List<OMPDocAcknowledgement> getAckDetailsForBillClassificationValidation(
			Long claimKey) {

		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findNonCancelledAck");
		query.setParameter("claimKey", claimKey);
		query.setParameter("statusId",
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);

		List<OMPDocAcknowledgement> reimbursementList = (List<OMPDocAcknowledgement>) query
				.getResultList();

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			for (OMPDocAcknowledgement OMPDocAcknowledgement : reimbursementList) {
				entityManager.refresh(OMPDocAcknowledgement);
			}
		}

		return reimbursementList;

	}

	@SuppressWarnings("unchecked")
	public OMPReimbursement getReimbursement(Long rodkey) {

		Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey");
		query.setParameter("primaryKey", rodkey);

		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();

		if (reimbursementList != null && !reimbursementList.isEmpty()) {

			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public OMPDocAcknowledgement getDocAcknowledgment(Long acknowledgementKey) {

		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByKey");
		query.setParameter("ackDocKey", acknowledgementKey);

		List<OMPDocAcknowledgement> reimbursementList = (List<OMPDocAcknowledgement>) query
				.getResultList();

		for (OMPDocAcknowledgement OMPDocAcknowledgement : reimbursementList) {
			entityManager.refresh(OMPDocAcknowledgement);
		}

		if (reimbursementList.size() > 0) {
			return reimbursementList.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public Long getReimbursementByAckKey(Long ackKey) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByAcknowledgement");
		query.setParameter("docAcknowledgmentKey", ackKey);

		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();
		List<Long> keys = new ArrayList<Long>();
		for (OMPReimbursement OMPReimbursement : reimbursementList) {
			keys.add(OMPReimbursement.getKey());
		}
		if (!keys.isEmpty()) {
			Long key = Collections.max(keys);
			return key;
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public OMPReimbursement getReimbursementByRodNo(String rodNo) {

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findRodByNumber");
		query.setParameter("rodNumber", rodNo);
		OMPReimbursement OMPReimbursement = null;

		List<OMPReimbursement> reimbursementList = (List<OMPReimbursement>) query
				.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			OMPReimbursement = reimbursementList.get(0);
			entityManager.refresh(OMPReimbursement);
		}
		return OMPReimbursement;

	}

	public List<DiagnosisDetailsTableDTO> getDiagnosisList(Long rodKey) {

		// rodKey = 5026l;
		Query query = entityManager
				.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", rodKey);

		List<PedValidation> procedure = (List<PedValidation>) query
				.getResultList();

		List<DiagnosisDetailsTableDTO> diagnosisList = new ArrayList<DiagnosisDetailsTableDTO>();

		for (PedValidation pedValidation : procedure) {

			DiagnosisDetailsTableDTO dto = new DiagnosisDetailsTableDTO();
			Long diagnosisId = pedValidation.getDiagnosisId();
			if (diagnosisId != null) {
				try {
					Query diagnosis = entityManager
							.createNamedQuery("Diagnosis.findDiagnosisByKey");
					diagnosis.setParameter("diagnosisKey", diagnosisId);

					Diagnosis diagnosisValue = (Diagnosis) diagnosis
							.getSingleResult();
					dto.setDiagnosis(diagnosisValue.getValue());
					Query diagnosisPED = entityManager.createNamedQuery(
							"DiagnosisPED.findByPEDValidationKey")
							.setParameter("pedValidationKey",
									pedValidation.getKey());
					List<DiagnosisPED> pedDiagnosis = (List<DiagnosisPED>) diagnosisPED
							.getResultList();

					String exclusionDetails = "";
					for (DiagnosisPED diagnosisPED2 : pedDiagnosis) {
						if (null != diagnosisPED2.getExclusionDetails()) {
							if (diagnosisPED2.getExclusionDetails() != null) {
								MastersValue master = getMaster(diagnosisPED2
										.getExclusionDetails().getImpactId());
								if (master != null)
									dto.setExclusionDiagnosis(master.getValue());
							}
							exclusionDetails += diagnosisPED2
									.getExclusionDetails().getExclusion()
									+ "; ";
						}
					}

					dto.setExclusionDetailsValue(exclusionDetails);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			dto.setRemarks(pedValidation.getApprovedRemarks());
			if (null != pedValidation.getCopayPercentage()) {
				dto.setCoPayValue(pedValidation.getCopayPercentage().toString());
			}
			if (null != pedValidation.getConsiderForPayment()) {
				if (pedValidation.getConsiderForPayment().equals("Y")) {
					dto.setConsiderForPaymentValue("Yes");
				} else {
					dto.setConsiderForPaymentValue("No");
				}
			}

			if (pedValidation.getSubLimitApplicable() != null
					&& ("Y").equalsIgnoreCase(pedValidation
							.getSubLimitApplicable())) {
				dto.setSublimitApplicableValue("Yes");
			} else {
				dto.setSublimitApplicableValue("No");
			}

			if (pedValidation.getSumInsuredRestrictionId() != null) {
				MastersValue masterValue = getMaster(pedValidation
						.getSumInsuredRestrictionId());
				if (masterValue != null) {
					dto.setSumInsuredRestrictionValue(Integer
							.valueOf(masterValue.getValue()));
				}
			}

			if (null != pedValidation.getIcdChpterId()) {
				dto.setIcdChapterKey(pedValidation.getIcdChpterId());
			}
			if (null != pedValidation.getIcdBlockId()) {
				dto.setIcdBlockKey(pedValidation.getIcdBlockId());
			}
			if (null != pedValidation.getIcdCodeId()) {
				dto.setIcdCodeKey(pedValidation.getIcdCodeId());
			}
			if (null != pedValidation.getSublimitId()) {

				Query limit = entityManager
						.createNamedQuery("ClaimLimit.findByKey");
				limit.setParameter("limitKey", pedValidation.getSublimitId());
				ClaimLimit claimLimit = (ClaimLimit) limit.getSingleResult();
				dto.setSublimitNameValue(claimLimit.getLimitName());
				if (null != claimLimit.getLimitName()) {
					dto.setSublimitNameValue(claimLimit.getLimitName());
					if (null != claimLimit.getMaxPerClaimAmount()) {
						dto.setSublimitAmt(claimLimit.getMaxPerClaimAmount()
								.toString());
					}
				}

			}
			diagnosisList.add(dto);
		}

		return diagnosisList;
	}

	public MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			if (mastersValueList != null && !mastersValueList.isEmpty()) {
				return mastersValueList.get(0);
			}
		}

		return null;
	}

	public List<MedicalVerificationDTO> getMedicalVerification(
			BeanItemContainer<SelectValue> selectValueContainer, Long rodKey) {

		Query query = entityManager
				.createNamedQuery("ClaimVerification.findByReimbursementKey");
		query.setParameter("reimbursementKey", rodKey);

		List<ClaimVerification> claimVerification = (List<ClaimVerification>) query
				.getResultList();

		List<SelectValue> mastersValue = selectValueContainer.getItemIds();

		List<MedicalVerificationDTO> medicalVerificationList = new ArrayList<MedicalVerificationDTO>();

		for (SelectValue selectValue : mastersValue) {
			MedicalVerificationDTO dto = new MedicalVerificationDTO();
			if (claimVerification != null) {
				for (ClaimVerification claimVerification2 : claimVerification) {
					if (null != claimVerification2.getVerificationTypeId()) {
						if (selectValue.getId().equals(
								claimVerification2.getVerificationTypeId())) {
							dto.setDescription(selectValue.getValue());
							if (null != claimVerification2.getVerifiedFlag()) {
								if (claimVerification2.getVerifiedFlag()
										.equals("Y")) {
									dto.setVerifiedFlag("Yes");
								} else {
									dto.setVerifiedFlag("No");
								}
							}
							dto.setRemarks(claimVerification2
									.getMedicalRemarks());
						}
					}
				}
			}
			medicalVerificationList.add(dto);

		}

		return medicalVerificationList;

	}

	@SuppressWarnings("unchecked")
	public List<Speciality> getSpecialityListByClaim(Long claimKey) {

		Query query = entityManager
				.createNamedQuery("Speciality.findByClaimKey");
		query.setParameter("claimKey", claimKey);

		List<Speciality> speciality = (List<Speciality>) query.getResultList();

		return speciality;

	}

	@SuppressWarnings({ "unchecked" })
	public List<ProcedureDTO> getProcedureDto(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("Procedure.findByTransactionKey");
		query.setParameter("transactionKey", rodKey);

		List<Procedure> procedure = (List<Procedure>) query.getResultList();

		List<ProcedureDTO> procedureDtoList = new ArrayList<ProcedureDTO>();

		for (Procedure procedure2 : procedure) {
			ProcedureDTO dto = new ProcedureDTO();
			dto.setProcedureNameValue(procedure2.getProcedureName());
			dto.setProcedureCodeValue(procedure2.getProcedureCode());
			dto.setPackageRate(procedure2.getPackageRate());
			if (procedure2.getDayCareProcedure() != null) {
				if (procedure2.getDayCareProcedure().equals("Y")) {
					dto.setDayCareProcedure("Yes");
				} else {
					dto.setDayCareProcedure("No");
				}
			}
			if (procedure2.getConsiderForDayCare() != null) {
				if (procedure2.getConsiderForDayCare().equals("Y")) {
					dto.setConsiderForDayCareValue("Yes");
				} else {
					dto.setConsiderForDayCareValue("No");
				}
			}

			if (procedure2.getConsiderForPayment() != null) {
				if (procedure2.getConsiderForPayment().equals("Y")) {
					dto.setConsiderForPaymentValue("Yes");
				} else {
					dto.setConsiderForPaymentValue("No");
				}
			}
			dto.setRemarks(procedure2.getProcedureRemarks());
			dto.setSubLimitApplicable(procedure2.getSubLimitApplicable());
			if (procedure2.getSubLimitApplicable() != null
					&& ("Y").equalsIgnoreCase(procedure2
							.getSubLimitApplicable())) {
				dto.setSublimitApplicableFlag("Yes");
			} else {
				dto.setSublimitApplicableFlag("No");
			}
			if (null != procedure2.getSublimitNameId()) {
				Query limit = entityManager
						.createNamedQuery("ClaimLimit.findByKey");
				limit.setParameter("limitKey", procedure2.getSublimitNameId());
				ClaimLimit claimLimit = (ClaimLimit) limit.getSingleResult();
				dto.setSublimitNameValue(claimLimit.getLimitName());
				if (null != claimLimit.getSumInsured()) {
					dto.setSublimitAmount(claimLimit.getSumInsured().toString());
					dto.setSubLimitAmount(Double.valueOf(claimLimit
							.getSumInsured()));
				}
			}
			procedureDtoList.add(dto);
		}
		return procedureDtoList;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<DocumentCheckListDTO> getDocumentList(Long ackKey) {
		List<RODDocumentCheckList> documentChkLst = null;
		Query query = entityManager
				.createNamedQuery("RODDocumentCheckList.findByDocKey");
		query.setParameter("docKey", ackKey);
		if (null != query.getResultList()) {
			documentChkLst = (List<RODDocumentCheckList>) query.getResultList();
		}
		List<DocumentCheckListDTO> documentDetailList = new ArrayList<DocumentCheckListDTO>();
		if (documentChkLst != null) {
			for (int i = 0; i < documentChkLst.size(); i++) {
				DocumentCheckListDTO dto = new DocumentCheckListDTO();
				if (null != documentChkLst.get(i).getReceivedStatusId()) {
					SelectValue selected = new SelectValue();
					selected.setId(documentChkLst.get(i).getReceivedStatusId()
							.getKey());
					selected.setValue(documentChkLst.get(i)
							.getReceivedStatusId().getValue());
					dto.setReceivedStatus(selected);
				}
				dto.setNoOfDocuments(documentChkLst.get(i).getNoOfDocuments());
				dto.setRemarks(documentChkLst.get(i).getRemarks());
				if (null != documentChkLst.get(i).getDocumentTypeId()) {
					Query documentQuery = entityManager
							.createNamedQuery("DocumentCheckListMaster.findByKey");
					documentQuery.setParameter("primaryKey", documentChkLst
							.get(i).getDocumentTypeId());
					DocumentCheckListMaster masterValue = (DocumentCheckListMaster) documentQuery
							.getSingleResult();
					dto.setValue(masterValue.getValue());
					dto.setMandatoryDocFlag(masterValue.getMandatoryDocFlag());
					dto.setRequiredDocType(masterValue.getRequiredDocType());
				}
				documentDetailList.add(dto);
			}
		}
		return documentDetailList;
	}

	public DocumentDetailsDTO getAcknowledgementDetails(Long ackKey) {

		OMPDocAcknowledgement OMPDocAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);
		DocumentDetailsDTO documentDetailsDto = null;
		if (OMPDocAcknowledgement != null) {

			documentDetailsDto = OMPEarlierRodMapper.getInstance()
					.getAcknowledgementDetail(OMPDocAcknowledgement);
			
			if (OMPDocAcknowledgement.getHospitalisationFlag() != null) {
				documentDetailsDto.setHospitalization(OMPDocAcknowledgement
						.getHospitalisationFlag().equals("Y") ? true : false);
			}
//			if (OMPDocAcknowledgement.getPreHospitalisationFlag() != null) {
//				documentDetailsDto
//						.setPreHospitalization(OMPDocAcknowledgement
//								.getPreHospitalisationFlag().equals("Y") ? true
//								: false);
//			}
//			if (OMPDocAcknowledgement.getPostHospitalisationFlag() != null) {
//				documentDetailsDto.setPostHospitalization(OMPDocAcknowledgement
//						.getPostHospitalisationFlag().equals("Y") ? true
//						: false);
//			}
//			if (OMPDocAcknowledgement.getPartialHospitalisationFlag() != null) {
//				documentDetailsDto.setPartialHospitalization(OMPDocAcknowledgement
//						.getPartialHospitalisationFlag().equals("Y") ? true
//						: false);
//			}
//			if (OMPDocAcknowledgement.getLumpsumAmountFlag() != null) {
//				documentDetailsDto.setLumpSumAmount(OMPDocAcknowledgement
//						.getLumpsumAmountFlag().equals("Y") ? true : false);
//			}
//			if (OMPDocAcknowledgement.getHospitalCashFlag() != null) {
//				documentDetailsDto
//						.setAddOnBenefitsHospitalCash(OMPDocAcknowledgement
//								.getHospitalCashFlag().equals("Y") ? true
//								: false);
//			}
//			if (OMPDocAcknowledgement.getPatientCareFlag() != null) {
//				documentDetailsDto
//						.setAddOnBenefitsPatientCare(OMPDocAcknowledgement
//								.getPatientCareFlag().equals("Y") ? true
//								: false);
//			}
			
			if (OMPDocAcknowledgement.getReconsiderationRequest() != null) {
				documentDetailsDto
						.setReconsiderationRequestValue(OMPDocAcknowledgement
								.getReconsiderationRequest().equals("Y") ? "Yes"
								: "No");
			}
			if (OMPDocAcknowledgement.getHospitalizationRepeatFlag() != null) {
				documentDetailsDto.setHospitalizationRepeat(OMPDocAcknowledgement
						.getHospitalizationRepeatFlag().equals("Y") ? true
						: false);
			}

		}

		return documentDetailsDto;

	}

	/**
	 * Method to retrieve data for select earlier ROD request to re consider.
	 * **/
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ReconsiderRODRequestTableDTO> getReconsiderRequestTableValues(
			OMPClaim claim) {

		List<ReconsiderRODRequestTableDTO> reconsiderRODRequestDTO = null;

		/*
		 * List<OMPDocAcknowledgement> docAcknowledgements = masterService
		 * .getDocumentAcknowledgeByClaim(claim);
		 */
		/*
		 * List<OMPDocAcknowledgement> docAcknowledgements = masterService
		 * .getLatestDocumentAcknowledgeByClaim(claim);
		 */

		// if (null != docAcknowledgements && !docAcknowledgements.isEmpty()) {
		/*
		 * List<ReconsiderRODRequestTableDTO> docAckList =
		 * OMPAckDocReceivedMapper
		 * .getDocAcknowledgeList(docAcknowledgements);
		 * 
		 * if(null != docAckList && !docAckList.isEmpty()) { docAckData =
		 * docAckList.get(0); }
		 */

		List<OMPReimbursement> reimbursementDetails = getReimbursementDetails(claim
				.getKey());

		if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {

			OMPAckDocReceivedMapper OMPAckDocMapper = OMPAckDocReceivedMapper
					.getInstance();
			List<ReconsiderRODRequestTableDTO> reimbursementList = OMPAckDocMapper
					.getReimbursementDetails(reimbursementDetails);

			if (null != reimbursementList && !reimbursementList.isEmpty()) {
				reconsiderRODRequestDTO = new ArrayList<ReconsiderRODRequestTableDTO>();
				// for (ReconsiderRODRequestTableDTO docAckData : docAckList) {

				for (ReconsiderRODRequestTableDTO reimbursementData : reimbursementList) {
					// if(docAckData.getRodKey().equals(reimbursementData.getKey()))
					/**
					 * Since settled claim functionality is not yet implemented,
					 * no status is available for the same. Hence as of now,
					 * only rejected claims will be shown for reconsideration.
					 * */
					// if((reimbursementData.getKey().equals(docAckData.getRodKey()))
					// &&
					// ("Settled").equals(reimbursementData.getRodStatus()))
					if (
					/*
					 * (ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS
					 * .equals(reimbursementData.getStatusId()) ||
					 * ReferenceTable
					 * .PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS
					 * .equals(reimbursementData.getStatusId()) ||
					 * (ReferenceTable
					 * .CREATE_ROD_CLOSED_STATUS.equals(reimbursementData
					 * .getStatusId()) ||
					 * ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS
					 * .equals(reimbursementData.getStatusId()) ||
					 * ReferenceTable
					 * .PROCESS_CLAIM_REQUEST_CLSOED_STATUS.equals(
					 * reimbursementData.getStatusId()) || ReferenceTable.
					 * ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS
					 * .equals(reimbursementData.getStatusId()) ||
					 * ReferenceTable
					 * .BILLING_CLOSED_STATUS.equals(reimbursementData
					 * .getStatusId()) ||
					 * ReferenceTable.FINANCIAL_CLOSED_STATUS.
					 * equals(reimbursementData.getStatusId()))
					 * 
					 * )
					 */

					(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS
							.equals(reimbursementData.getStatusId())
							|| ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS
									.equals(reimbursementData.getStatusId())
							|| (ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS
									.equals(reimbursementData.getStatusId()))
							|| (ReferenceTable.FINANCIAL_CLOSED_STATUS
									.equals(reimbursementData.getStatusId()))
							|| (ReferenceTable.REIMBURSEMENT_SETTLED_STATUS
									.equals(reimbursementData.getStatusId())) || (ReferenceTable.REIMBURSEMENT_PAYMENT_RECONSIDERATION_STATUS
								.equals(reimbursementData.getStatusId()))))

					{
						OMPDocAcknowledgement docAck = getDocAcknowledgment(reimbursementData
								.getDocAcknowledgementKey());
						ReconsiderRODRequestTableDTO docAckData = OMPAckDocMapper
								.getDocAckRecDetails(docAck);
						/*
						 * if (null != docAcknowledgements &&
						 * !docAcknowledgements.isEmpty()) {
						 * List<ReconsiderRODRequestTableDTO> docAckList =
						 * OMPAckDocReceivedMapper
						 * .getDocAcknowledgeList(docAcknowledgements); if(null
						 * != docAckList && !docAckList.isEmpty()) { docAckData
						 * = docAckList.get(0); } }
						 */

						if (reimbursementData.getKey().equals(
								docAckData.getRodKey())) {
							/*
							 * docAckData.setApprovedAmt(reimbursementData
							 * .getApprovedAmt());
							 */
							docAckData.setRodStatus(reimbursementData
									.getRodStatus());
							if (ReferenceTable.FINANCIAL_STAGE
									.equals(reimbursementData.getStageId())) {
								docAckData.setApprovedAmt(reimbursementData
										.getFinancialApprovedAmount());
							} else if (ReferenceTable.BILLING_STAGE
									.equals(reimbursementData.getStageId())) {
								docAckData.setApprovedAmt(reimbursementData
										.getBillingApprovedAmount());
							} else {
								docAckData.setApprovedAmt(reimbursementData
										.getApprovedAmt());
							}

							docAckData.setRodNo(reimbursementData.getRodNo());
							docAckData
									.setBillClassification(getBillClassificationValue(docAckData));
							// if (null !=
							// docAckData.getHospitalizationClaimedAmt())
							docAckData.setClaimedAmt(calculatedClaimedAmt(
									docAckData.getHospitalizationClaimedAmt(),
									docAckData.getPreHospClaimedAmt(),
									docAckData.getPostHospClaimedAmt()));

							if (null != docAckData
									.getAddOnBenefitsHospitalCashFlag()
									&& ("Y").equalsIgnoreCase(docAckData
											.getAddOnBenefitsHospitalCashFlag())) {
								ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(
										reimbursementData.getKey(),
										SHAConstants.HOSPITAL_CASH_FLAG);
								if (null != reimbursementBenefits) {
									docAckData
											.setHospitalCashNoOfDaysBills(reimbursementBenefits
													.getNumberOfDaysBills());
									docAckData
											.setHospitalCashPerDayAmtBills(reimbursementBenefits
													.getPerDayAmountBills());
									docAckData
											.setHospitalCashTotalClaimedAmount(reimbursementBenefits
													.getTotalClaimAmountBills());
									docAckData
											.setHospitalCashReimbursementBenefitsKey(reimbursementBenefits
													.getKey());
								}
							}
							if (null != docAckData
									.getAddOnBenefitsPatientCareFlag()
									&& ("Y").equalsIgnoreCase(docAckData
											.getAddOnBenefitsPatientCareFlag())) {
								ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(
										reimbursementData.getKey(),
										SHAConstants.PATIENT_CARE_FLAG);
								if (null != reimbursementBenefits) {
									docAckData
											.setPatientCareNoOfDaysBills(reimbursementBenefits
													.getNumberOfDaysBills());
									docAckData
											.setPatientCarePerDayAmtBills(reimbursementBenefits
													.getPerDayAmountBills());
									docAckData
											.setPatientCareTotalClaimedAmount(reimbursementBenefits
													.getTotalClaimAmountBills());
									docAckData
											.setPatientCareReimbursementBenefitsKey(reimbursementBenefits
													.getKey());
									List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = getReimbursementBenefitsDetailsList(reimbursementBenefits
											.getKey());
									if (null != reimbBenefitsDetailsList
											&& !reimbBenefitsDetailsList
													.isEmpty()) {
										List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
										for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
											PatientCareDTO patientCareDTO = new PatientCareDTO();
											patientCareDTO
													.setEngagedFrom(reimbursementBenefitsDetails
															.getEngagedFrom());
											patientCareDTO
													.setEngagedTo(reimbursementBenefitsDetails
															.getEngagedTo());
											patientCareDTO
													.setReconsiderReimbursementBenefitsKey(reimbursementBenefitsDetails
															.getKey());
											patientCareList.add(patientCareDTO);
										}
										docAckData
												.setPatientCareDTOList(patientCareList);
									}
								}
							}

							if (("Y").equalsIgnoreCase(docAck
									.getReconsiderationRequest()))
								docAckData.setSelect(true);
							docAckData.setRodKey(reimbursementData.getKey());
							reconsiderRODRequestDTO.add(docAckData);
							// break;
						}
					}
				}
				// }
			}
		}
		return reconsiderRODRequestDTO;
	}

	public List<ReconsiderRODRequestTableDTO> getReconsiderationDetailsList(
			OMPDocAcknowledgement OMPDocAcknowledgement) {

		List<ReconsiderRODRequestTableDTO> reconsiderationTable = new ArrayList<ReconsiderRODRequestTableDTO>();

		ReconsiderRODRequestTableDTO reconsideration = new ReconsiderRODRequestTableDTO();
		if (OMPDocAcknowledgement != null) {
			if (OMPDocAcknowledgement.getRodKey() != null) {
				OMPReimbursement reimbursementByKey = getReimbursementByKey(OMPDocAcknowledgement
						.getRodKey());
				if (reimbursementByKey != null) {
					reconsideration.setRodNo(reimbursementByKey.getRodNumber());
					String billClassificationValue = getBillClassificationValue(OMPDocAcknowledgement);
					reconsideration
							.setBillClassification(billClassificationValue);
					Double claimedAmount = getClaimedAmount(reimbursementByKey);
					reconsideration.setClaimedAmt(claimedAmount);
					reconsideration.setRodStatus(reimbursementByKey.getStatus()
							.getProcessValue());

				}
			}
		}
		reconsiderationTable.add(reconsideration);

		return reconsiderationTable;
	}

	@SuppressWarnings("unchecked")
	public OMPReimbursement getReimbursementByKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("OMPReimbursement.findByKey")
				.setParameter("primaryKey", rodKey);
		List<OMPReimbursement> rodList = query.getResultList();

		if (rodList != null && !rodList.isEmpty()) {
			for (OMPReimbursement OMPReimbursement : rodList) {
				entityManager.refresh(OMPReimbursement);
			}
			return rodList.get(0);
		}
		return null;
	}

	public List<OMPReimbursement> getReimbursementDetails(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findLatestRODByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<OMPReimbursement> reimbursementDetails = query.getResultList();
		if (null != reimbursementDetails && !reimbursementDetails.isEmpty()) {
			for (OMPReimbursement OMPReimbursement : reimbursementDetails) {
				entityManager.refresh(OMPReimbursement);
			}

		}
		return reimbursementDetails;
	}

//	public Boolean getReimbursementQueryStatus(Long reimbursementKey) {
//		Boolean queryStatus = false;
//		Query query = entityManager
//				.createNamedQuery("ReimbursementQuery.findLatestQueryByKey");
//		query = query.setParameter("primaryKey", reimbursementKey);
//		List<ReimbursementQuery> reimbQueryList = query.getResultList();
//		if (null != reimbQueryList && !reimbQueryList.isEmpty()) {
//			ReimbursementQuery reimbQuery = reimbQueryList.get(0);
//			if (reimbQuery
//					.getStatus()
//					.getKey()
//					.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)
//					|| (reimbQuery.getStatus().getKey()
//							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))) {
//				queryStatus = true;
//			} else {
//				queryStatus = false;
//			}
//		} else {
//			queryStatus = false;
//		}
//		return queryStatus;
//	}

	@SuppressWarnings("unchecked")
	public ReimbursementBenefits getReimbursementBenefits(
			Long reimbBenefitsKey, String benefitsFlag) {

		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKeyAndBenefitsFlag");
		query.setParameter("rodKey", reimbBenefitsKey);
		query.setParameter("benefitsFlag", benefitsFlag);

		List<ReimbursementBenefits> reimbursementList = (List<ReimbursementBenefits>) query
				.getResultList();

		for (ReimbursementBenefits reimbursementBenefits : reimbursementList) {
			entityManager.refresh(reimbursementBenefits);
		}

		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			return reimbursementList.get(0);
		}

		return null;

	}

	public List<ReimbursementBenefitsDetails> getReimbursementBenefitsDetailsList(
			Long reimbBenefitsKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
		query.setParameter("benefitsKey", reimbBenefitsKey);
		List<ReimbursementBenefitsDetails> reimburesmentBenefitsDetails = query
				.getResultList();
		return reimburesmentBenefitsDetails;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<ReconsiderRODRequestTableDTO> getReconsiderRequestTableValuesForBillEntry(
			Long rodKey) {

		List<ReconsiderRODRequestTableDTO> reconsiderRODRequestDTO = null;
		OMPReimbursement reimbursementDetails = getReimbursement(rodKey);

		if (null != reimbursementDetails) {
			OMPAckDocReceivedMapper OMPAckDocMapper = OMPAckDocReceivedMapper
					.getInstance();
			ReconsiderRODRequestTableDTO reconsiderList = OMPAckDocMapper
					.getReimbursementDetails(reimbursementDetails);
			if (null != reconsiderList) {
				reconsiderRODRequestDTO = new ArrayList<ReconsiderRODRequestTableDTO>();
				OMPDocAcknowledgement docAck = getDocAcknowledgment(reconsiderList
						.getDocAcknowledgementKey());
				ReconsiderRODRequestTableDTO docAckData = OMPAckDocMapper
						.getDocAckRecDetails(docAck);
				if (reconsiderList.getKey().equals(docAckData.getRodKey())) {
					/*
					 * docAckData.setApprovedAmt(reimbursementData
					 * .getApprovedAmt());
					 */
					docAckData.setRodStatus(reconsiderList.getRodStatus());

					ReimbursementRejection reimbRejection = getReimbRejectRecord(docAckData
							.getRodKey());
					if (null != reimbRejection
							&& null != reimbRejection.getStatus()) {
						if (reimbRejection
								.getStatus()
								.getKey()
								.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
								|| reimbRejection
										.getStatus()
										.getKey()
										.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)) {
							docAckData.setIsRejectReconsidered(true);
						} else {
							docAckData.setIsRejectReconsidered(false);
						}
					}
					if (null != reimbursementDetails
							&& null != reimbursementDetails
									.getFinancialCompletedDate()
							&& null != reimbRejection
							&& null != reimbRejection.getModifiedDate()
							&& reimbursementDetails.getFinancialCompletedDate()
									.after(reimbRejection.getModifiedDate())) {
						docAckData.setIsRejectReconsidered(false);
						docAckData.setIsSettledReconsideration(true);
					}

					/**
					 * The below condition is commented, since in bill entry
					 * stage, the status and stage of rod will be updated as per
					 * create ROD process. Hence financial approval status or
					 * billing send to financial approver status will be not be
					 * applicable and hence below condition will now work. This
					 * needs to be discussed with sathish sir once.
					 * */
					/*
					 * if(ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(
					 * reconsiderList.getStatusId())) {
					 * docAckData.setApprovedAmt
					 * (reconsiderList.getFinancialApprovedAmount()); } else
					 * if(ReferenceTable
					 * .BILLING_SEND_TO_FINANCIAL_APPROVER.equals
					 * (reconsiderList.getStatusId())) {
					 * docAckData.setApprovedAmt
					 * (reconsiderList.getBillingApprovedAmount()); } else {
					 * docAckData
					 * .setApprovedAmt(reconsiderList.getApprovedAmt()); }
					 */

					docAckData.setApprovedAmt(reconsiderList.getApprovedAmt());
					docAckData.setRodNo(reconsiderList.getRodNo());
					docAckData
							.setBillClassification(getBillClassificationValue(docAckData));
					// if (null != docAckData.getHospitalizationClaimedAmt())
					docAckData.setClaimedAmt(calculatedClaimedAmt(
							docAckData.getHospitalizationClaimedAmt(),
							docAckData.getPreHospClaimedAmt(),
							docAckData.getPostHospClaimedAmt()));

					if (null != docAckData.getAddOnBenefitsHospitalCashFlag()
							&& ("Y").equalsIgnoreCase(docAckData
									.getAddOnBenefitsHospitalCashFlag())) {
						ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(
								reconsiderList.getKey(),
								SHAConstants.HOSPITAL_CASH_FLAG);
						if (null != reimbursementBenefits) {
							docAckData
									.setHospitalCashNoOfDaysBills(reimbursementBenefits
											.getNumberOfDaysBills());
							docAckData
									.setHospitalCashPerDayAmtBills(reimbursementBenefits
											.getPerDayAmountBills());
							docAckData
									.setHospitalCashTotalClaimedAmount(reimbursementBenefits
											.getTotalClaimAmountBills());
							docAckData
									.setHospitalCashReimbursementBenefitsKey(reimbursementBenefits
											.getKey());
						}
					}
					if (null != docAckData.getAddOnBenefitsPatientCareFlag()
							&& ("Y").equalsIgnoreCase(docAckData
									.getAddOnBenefitsPatientCareFlag())) {
						ReimbursementBenefits reimbursementBenefits = getReimbursementBenefits(
								reconsiderList.getKey(),
								SHAConstants.PATIENT_CARE_FLAG);
						if (null != reimbursementBenefits) {
							docAckData
									.setPatientCareNoOfDaysBills(reimbursementBenefits
											.getNumberOfDaysBills());
							docAckData
									.setPatientCarePerDayAmtBills(reimbursementBenefits
											.getPerDayAmountBills());
							docAckData
									.setPatientCareTotalClaimedAmount(reimbursementBenefits
											.getTotalClaimAmountBills());
							docAckData
									.setPatientCareReimbursementBenefitsKey(reimbursementBenefits
											.getKey());
							List<ReimbursementBenefitsDetails> reimbBenefitsDetailsList = getReimbursementBenefitsDetailsList(reimbursementBenefits
									.getKey());
							if (null != reimbBenefitsDetailsList
									&& !reimbBenefitsDetailsList.isEmpty()) {
								List<PatientCareDTO> patientCareList = new ArrayList<PatientCareDTO>();
								for (ReimbursementBenefitsDetails reimbursementBenefitsDetails : reimbBenefitsDetailsList) {
									PatientCareDTO patientCareDTO = new PatientCareDTO();
									patientCareDTO
											.setEngagedFrom(reimbursementBenefitsDetails
													.getEngagedFrom());
									patientCareDTO
											.setEngagedTo(reimbursementBenefitsDetails
													.getEngagedTo());
									patientCareDTO
											.setReconsiderReimbursementBenefitsKey(reimbursementBenefitsDetails
													.getKey());
									patientCareList.add(patientCareDTO);
								}
								docAckData
										.setPatientCareDTOList(patientCareList);
							}
						}
					}

					if (("Y").equalsIgnoreCase(docAck
							.getReconsiderationRequest()))
						docAckData.setSelect(true);
					docAckData.setRodKey(reconsiderList.getKey());
					reconsiderRODRequestDTO.add(docAckData);
					// break;
				}
			}
		}
		return reconsiderRODRequestDTO;
	}

	private Double calculatedClaimedAmt(Double hospAmt, Double preHospAmt,
			Double postHospAmt) {
		Double total = 0d;
		if (null != hospAmt) {
			total = total + hospAmt;
		}
		if (null != preHospAmt) {
			total = total + preHospAmt;
		}
		if (null != postHospAmt) {
			total = total + postHospAmt;
		}

		return total;
	}

	private String getBillClassificationValue(
			ReconsiderRODRequestTableDTO reconsiderReqDTO) {
		StringBuilder strBuilder = new StringBuilder();
		if (("Y").equals(reconsiderReqDTO.getHospitalizationFlag())) {
			strBuilder.append("Hospitalization");
			strBuilder.append(",");
		}

		if (("Y").equals(reconsiderReqDTO.getHospitalizationRepeatFlag())) {
			strBuilder.append("Hospitalization Repeat");
			strBuilder.append(",");
		}

		if (("Y").equals(reconsiderReqDTO.getPreHospitalizationFlag())) {
			strBuilder.append("Pre-Hospitalization");
			strBuilder.append(",");
		}
		if (("Y").equals(reconsiderReqDTO.getPostHospitalizationFlag())) {
			strBuilder.append("Post-Hospitalization");
			strBuilder.append(",");
		}

		if (("Y").equals(reconsiderReqDTO.getPartialHospitalizationFlag())) {
			strBuilder.append("Partial-Hospitalization");
			strBuilder.append(",");
		}

		if (("Y").equals(reconsiderReqDTO.getLumpSumAmountFlag())) {
			strBuilder.append("Lumpsum Amount");
			strBuilder.append(",");

		}
		if (("Y").equals(reconsiderReqDTO.getAddOnBenefitsHospitalCashFlag())) {
			strBuilder.append("Add on Benefits (Hospital cash)");
			strBuilder.append(",");

		}
		if (("Y").equals(reconsiderReqDTO.getAddOnBenefitsPatientCareFlag())) {
			strBuilder.append("Add on Benefits (Patient Care)");
			strBuilder.append(",");
		}

		return strBuilder.toString();
	}

	private String getBillClassificationValue(OMPDocAcknowledgement docAck) {
		StringBuilder strBuilder = new StringBuilder();
		// StringBuilder amtBuilder = new StringBuilder();
		// Double total = 0d;
		try {
			if (("Y").equals(docAck.getHospitalisationFlag())) {
				strBuilder.append("Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPreHospitalisationFlag())) {
				strBuilder.append("Pre-Hospitalization");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getPostHospitalisationFlag())) {
				strBuilder.append("Post-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getPartialHospitalisationFlag())) {
				strBuilder.append("Partial-Hospitalization");
				strBuilder.append(",");
			}

			if (("Y").equals(docAck.getLumpsumAmountFlag())) {
				strBuilder.append("Lumpsum Amount");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getHospitalCashFlag())) {
				strBuilder.append("Add on Benefits (Hospital cash)");
				strBuilder.append(",");

			}
			if (("Y").equals(docAck.getPatientCareFlag())) {
				strBuilder.append("Add on Benefits (Patient Care)");
				strBuilder.append(",");
			}
			if (("Y").equals(docAck.getHospitalizationRepeatFlag())) {
				strBuilder.append("Hospitalization Repeat");
				strBuilder.append(",");
			}
			// rodQueryDTO.setClaimedAmount(total);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuilder.toString();
	}

	public Long getCountOfAckByClaimKey(Long a_key) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.CountAckByClaimKey");
		query = query.setParameter("claimkey", a_key);
		// Integer.parseInt(Strin)
		Long countOfAck = (Long) query.getSingleResult();
		return countOfAck;
	}

//	public List<ViewQueryDTO> getQueryDetails(Long rodKey) {
//		Query query = entityManager
//				.createNamedQuery("ReimbursementQuery.findByReimbursement");
//		query = query.setParameter("reimbursementKey", rodKey);
//
//		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
//				.getResultList();
//
//		for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
//			entityManager.refresh(reimbursementQuery2);
//			entityManager.refresh(reimbursementQuery2.getReimbursement());
//		}
//
//		List<ViewQueryDTO> queryDetails = OMPEarlierRodMapper.getInstance()
//				.getViewQueryDTO(reimbursementQuery);
//
//		if (reimbursementQuery != null && !reimbursementQuery.isEmpty()) {
//			if (queryDetails != null && !queryDetails.isEmpty()) {
//				for (ReimbursementQuery rQuery : reimbursementQuery) {
//					String billClassification = getBillClassificationValue(rQuery
//							.getReimbursement().getDocAcknowLedgement());
//					for (ViewQueryDTO viewQueryDTO : queryDetails) {
//						if (viewQueryDTO.getKey() == rQuery.getKey()) {
//							viewQueryDTO
//									.setBillClassification(billClassification);
//						}
//					}
//				}
//			}
//		}
//
//		return queryDetails;
//	}
//
//	public List<ViewQueryDTO> getQueryDetails(Long rodKey, EntityManager em) {
//		entityManager = em;
//		List<ViewQueryDTO> queryDetails = getQueryDetails(rodKey);
//
//		return queryDetails;
//	}

	public List<PedValidation> search(Long preAuthKey) {

		List<PedValidation> resultList = new ArrayList<PedValidation>();

		Query query = entityManager
				.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", preAuthKey);

		resultList = (List<PedValidation>) query.getResultList();

		return resultList;

	}

	public String getDiagnosisName(Long key) {

		String diagnosisName = "";

		List<PedValidation> pedValidationList = search(key);

		for (PedValidation pedValidation : pedValidationList) {

			Query diagnosis = entityManager
					.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey",
					pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			if (masters != null) {
				diagnosisName += masters.getValue() + ",";
			}
		}

		return diagnosisName;
	}

//	public List<RODQueryDetailsDTO> getQueryDetailsList(Long ackKey) {
//
//		OMPDocAcknowledgement OMPDocAcknowledgement = getDocAcknowledgementBasedOnKey(ackKey);
//
//		if (OMPDocAcknowledgement != null) {
//
//			Query query = entityManager
//					.createNamedQuery("ReimbursementQuery.findByReimbursement");
//			query = query.setParameter("reimbursementKey",
//					OMPDocAcknowledgement.getRodKey());
//
//			List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
//					.getResultList();
//
//			List<RODQueryDetailsDTO> queryDetails = OMPEarlierRodMapper
//					.getInstance().getAckQueryDTO(reimbursementQuery);
//
//			for (RODQueryDetailsDTO rodQueryDetailsDTO : queryDetails) {
//				if (rodQueryDetailsDTO.getDocAcknowledgment() != null) {
//					String billClassificationValue = getBillClassificationValue(rodQueryDetailsDTO
//							.getOmpDocAcknowledgment());
//					rodQueryDetailsDTO
//							.setBillClassification(billClassificationValue);
//				}
//
//				if (rodQueryDetailsDTO.getReimbursementKey() != null) {
//					String diagnosisForPreauthByKey = getDiagnosisForPreauthByKey(rodQueryDetailsDTO
//							.getReimbursementKey());
//					rodQueryDetailsDTO.setDiagnosis(diagnosisForPreauthByKey);
//				}
//
//				if (rodQueryDetailsDTO.getReplyStatus() != null
//						&& rodQueryDetailsDTO.getReplyStatus()
//								.equalsIgnoreCase("Y")) {
//					rodQueryDetailsDTO.setQueryReplyStatus("YES");
//				} else {
//					rodQueryDetailsDTO.setQueryReplyStatus("NO");
//				}
//
//				if (rodQueryDetailsDTO.getReimbursement() != null) {
//					Double claimedAmount = getClaimedAmount(rodQueryDetailsDTO
//							.getOmpReimbursement());
//					rodQueryDetailsDTO.setClaimedAmount(claimedAmount);
//				}
//			}
//
//			return queryDetails;
//		}
//
//		return null;
//	}

	public String getDiagnosisForPreauthByKey(Long preauthKey) {
		String diagnosis = "";
		try {
			List<PedValidation> findPedValidationByPreauthKey = findPedValidationByPreauthKey(preauthKey);
			List<DiagnosisDetailsTableDTO> diagDtoList = (PreMedicalMapper
					.getInstance())
					.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

			if (diagDtoList != null && !diagDtoList.isEmpty()) {

				for (DiagnosisDetailsTableDTO diagDto : diagDtoList) {
					Long diagKey = diagDto.getDiagnosisId();
					if (diagKey != null) {
						Diagnosis diagObj = entityManager.find(Diagnosis.class,
								diagKey);
						if (diagObj != null) {
							diagnosis = ("").equals(diagnosis) ? diagObj
									.getValue() : diagnosis + " , "
									+ diagObj.getValue();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		return diagnosis;
	}

	@SuppressWarnings("unchecked")
	public List<PedValidation> findPedValidationByPreauthKey(Long preauthKey) {
		Query query = entityManager
				.createNamedQuery("PedValidation.findByPreauthKey");
		query.setParameter("preauthKey", preauthKey);
		List<PedValidation> resultList = (List<PedValidation>) query
				.getResultList();

		if (resultList != null && !resultList.isEmpty()) {
			for (PedValidation pedValidation : resultList) {
				entityManager.refresh(pedValidation);
			}
		}
		return resultList;
	}

	public ReimbursementQuery getReimbursementQuery(Long primaryKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByKey");
		query = query.setParameter("primaryKey", primaryKey);

		ReimbursementQuery reimbursementQuery = (ReimbursementQuery) query
				.getSingleResult();

		entityManager.refresh(reimbursementQuery);

		return reimbursementQuery;

	}

	public OMPDocAcknowledgement getDocAcknowledgementBasedOnKey(Long docAckKey) {
		OMPDocAcknowledgement OMPDocAcknowledgement = null;
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByKey");
		query = query.setParameter("ackDocKey", docAckKey);
		try {
			if (null != query.getSingleResult()) {
				OMPDocAcknowledgement = (OMPDocAcknowledgement) query
						.getSingleResult();

				entityManager.refresh(OMPDocAcknowledgement);
			}
			return OMPDocAcknowledgement;
		} catch (Exception e) {
			System.out.println("No elements");
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ViewDocumentDetailsDTO> listOfEarlierAckByClaimKey(Long a_key,
			Long reimbursementKey) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", a_key);

		// Integer.parseInt(Strin)
		List<Long> keysList = new ArrayList<Long>();
		List<OMPDocAcknowledgement> docAcknowledgmentList = (List<OMPDocAcknowledgement>) query
				.getResultList();

		List<OMPDocAcknowledgement> earlierAck = new ArrayList<OMPDocAcknowledgement>();
		for (OMPDocAcknowledgement OMPDocAcknowledgement : docAcknowledgmentList) {
			// if(OMPDocAcknowledgement.getRodKey() != null){
			// if(! OMPDocAcknowledgement.getRodKey().equals(reimbursementKey)){
			// earlierAck.add(OMPDocAcknowledgement);
			// }
			earlierAck.add(OMPDocAcknowledgement);
			keysList.add(OMPDocAcknowledgement.getKey());
			// }
		}

		Long maximumKey = 0l;
		if (!keysList.isEmpty()) {
			maximumKey = Collections.max(keysList);
		}

		for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {
			entityManager.refresh(OMPDocAcknowledgement);
		}

		List<ViewDocumentDetailsDTO> listDocumentDetails = OMPEarlierRodMapper
				.getInstance().getDocumentDetailsTableDTO(earlierAck);

		for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {

			String date = SHAUtils.getDateFormat(documentDetailsDTO
					.getReceivedDate());

			if (documentDetailsDTO.getRodKey() != null) {
				Long rodKey = new Long(documentDetailsDTO.getRodKey());
				Query rodQuery = entityManager
						.createNamedQuery("OMPReimbursement.findByKey");
				rodQuery = rodQuery.setParameter("primaryKey", rodKey);

				OMPReimbursement OMPReimbursement = (OMPReimbursement) rodQuery
						.getSingleResult();
				if (OMPReimbursement != null) {
					entityManager.refresh(OMPReimbursement);
				}
				documentDetailsDTO.setRodNumber(OMPReimbursement.getRodNumber());
				documentDetailsDTO.setReimbursementKey(OMPReimbursement.getKey());

				if (documentDetailsDTO.getKey() != null
						&& !documentDetailsDTO.getKey().equals(
								OMPReimbursement.getDocAcknowLedgement().getKey())) {
					documentDetailsDTO.setIsReadOnly(true);
				}

				// documentDetailsDTO.setMedicalResponseTime(OMPReimbursement
				// .getMedicalCompletedDate());
				documentDetailsDTO.setApprovedAmount(OMPReimbursement
						.getApprovedAmount());

				// if(OMPReimbursement.getStatus().getKey().equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY)
				// ||
				// OMPReimbursement.getStatus().getKey().equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
				// ||
				// OMPReimbursement.getStatus().getKey().equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)){
				//
				// documentDetailsDTO.setApprovedAmount(OMPReimbursement.getCurrentProvisionAmt());
				//
				// }

				documentDetailsDTO.setStatus(OMPReimbursement.getStatus()
						.getProcessValue());
				documentDetailsDTO.setStatusKey(OMPReimbursement.getStatus()
						.getKey());
				if (OMPReimbursement.getMedicalCompletedDate() != null) {
					documentDetailsDTO
							.setMedicalResponseTime(SHAUtils
									.formatDate(OMPReimbursement
											.getMedicalCompletedDate()));
				}

				documentDetailsDTO.setApprovedAmount(OMPReimbursement
						.getCurrentProvisionAmt());
				// documentDetailsDTO.setApprovedAmount(OMPReimbursement.getCurrentProvisionAmt());

				if (OMPReimbursement.getStage().getKey()
						.equals(ReferenceTable.BILLING_STAGE)) {

					// Long approvedAmount =
					// getReimbursementApprovedAmount(OMPReimbursement.getKey());
					// if(approvedAmount >0){
					// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
					// }
					if(OMPReimbursement
							.getBillingApprovedAmount().equals(0)){
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getCurrentProvisionAmt());
					}else{
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getBillingApprovedAmount());
					}
					

				} else if (OMPReimbursement.getStage().getKey()
						.equals(ReferenceTable.FINANCIAL_STAGE)) {
					// Long approvedAmount =
					// getReimbursementApprovedAmount(OMPReimbursement.getKey());
					// if(approvedAmount >0){
					// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
					// }
					if(OMPReimbursement
							.getFinancialApprovedAmount().equals(0)){
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getCurrentProvisionAmt());
					}
					else{
						documentDetailsDTO.setApprovedAmount(OMPReimbursement
								.getFinancialApprovedAmount());	
					}
					
				}

				// if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(OMPReimbursement.getStatus().getKey())){
				// Double provisionAmount = getProvisionAmount(OMPReimbursement);
				// documentDetailsDTO.setApprovedAmount(provisionAmount);
				// }

				if (ReferenceTable.RE_OPEN_CLAIM_STATUS_KEYS
						.containsKey(OMPReimbursement.getStatus().getKey())) {
					documentDetailsDTO.setApprovedAmount(OMPReimbursement
							.getCurrentProvisionAmt());
				}

				if (OMPReimbursement
						.getStatus()
						.getKey()
						.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
						|| OMPReimbursement
								.getStatus()
								.getKey()
								.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)) {

					documentDetailsDTO.setApprovedAmount(OMPReimbursement
							.getCurrentProvisionAmt());

				}

				if (ReferenceTable.CANCEL_ROD_KEYS.containsKey(OMPReimbursement
						.getStatus().getKey())) {
					documentDetailsDTO.setApprovedAmount(OMPReimbursement
							.getCurrentProvisionAmt());
				}

				if (OMPReimbursement.getStatus().getKey()
						.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)) {

					String statusValue = "MA - "
							+ OMPReimbursement.getStatus().getProcessValue();
					documentDetailsDTO.setStatus(statusValue);

				} else if (OMPReimbursement.getStatus().getKey()
						.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {

					String statusValue = "FA - "
							+ OMPReimbursement.getStatus().getProcessValue();
					documentDetailsDTO.setStatus(statusValue);
				}

			}
		}

		List<String> hospitalization = new ArrayList<String>();

		for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {

			getDocumentBillClassification(hospitalization, OMPDocAcknowledgement);

		}

		for (int i = 0; i < listDocumentDetails.size(); i++) {

			listDocumentDetails.get(i).setBillClassification(
					hospitalization.get(i));
			// listDocumentDetails.get(0).setLatestKey(maximumKey);
			OMPReimbursement OMPReimbursement = null;
			if (reimbursementKey != null) {
				OMPReimbursement = getReimbursement(reimbursementKey);
			}
			if (OMPReimbursement != null
					&& OMPReimbursement.getDocAcknowLedgement() != null) {
				listDocumentDetails.get(0).setLatestKey(
						OMPReimbursement.getDocAcknowLedgement().getKey());
			} else {
				listDocumentDetails.get(0).setLatestKey(maximumKey);
			}
		}

		return listDocumentDetails;
	}

	public List<ViewDocumentDetailsDTO> getDocumentDetailsForCloseClaim(
			String intimationNo, String userName, String password) {

		/*PayloadBOType payloadType = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadType.setIntimation(intimationType);

		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(
				userName, password);

		PagedTaskList tasks = closeClaimTask.getTasks(userName, null,
				payloadType);*/

		List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

		/*OMPClaim claim = getClaimsByIntimationNumber(intimationNo);

		List<OMPReimbursement> reimbursementList = getReimbursementDetailsForBillClassificationValidation(claim
				.getKey());

		for (OMPReimbursement OMPReimbursement : reimbursementList) {
			if (OMPReimbursement.getStatus().getKey()
					.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)
					|| OMPReimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					|| OMPReimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
					|| ReferenceTable.getPaymentStatus().containsKey(
							OMPReimbursement.getStatus().getKey())) {
				OMPDocAcknowledgement OMPDocAcknowledgement = OMPReimbursement
						.getDocAcknowLedgement();

				ViewDocumentDetailsDTO documentDetailsDTO = OMPEarlierRodMapper
						.getInstance().getDocumentDetails(OMPDocAcknowledgement);

				setReimbursementToDTO(documentList, OMPReimbursement,
						OMPDocAcknowledgement, documentDetailsDTO);
			}
		}

		if (tasks != null) {
			getDocumentListByHumanTask(tasks, documentList);
		}*/

		return documentList;

	}

	public List<ViewDocumentDetailsDTO> getDocumentDetailsForReOpenClaim(
			String intimationNo, String userName, String password) {

		/*PayloadBOType payloadType = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadType.setIntimation(intimationType);

		ReopenAllClaimTask reOpenClaimTask = BPMClientContext
				.getReOpAllClaimTask(userName, password);

		PagedTaskList tasks = reOpenClaimTask.getTasks(userName, null,
				payloadType);*/

		List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

		/*OMPClaim claim = getClaimsByIntimationNumber(intimationNo);

		List<OMPReimbursement> reimbursementList = getReimbursementDetailsForBillClassificationValidation(claim
				.getKey());

		List<Long> rodKeyList = new ArrayList<Long>();

		List<HumanTask> humanTasks = tasks.getHumanTasks();

		for (HumanTask humanTask : humanTasks) {
			if (humanTask.getPayload() != null
					&& humanTask.getPayload().getRod() != null) {
				RODType rod = humanTask.getPayload().getRod();
				rodKeyList.add(rod.getKey());
			}
		}

		for (OMPReimbursement OMPReimbursement : reimbursementList) {
			entityManager.refresh(OMPReimbursement);
			if (!rodKeyList.contains(OMPReimbursement.getKey())) {
				OMPDocAcknowledgement OMPDocAcknowledgement = OMPReimbursement
						.getDocAcknowLedgement();

				ViewDocumentDetailsDTO documentDetailsDTO = OMPEarlierRodMapper
						.getInstance().getDocumentDetails(OMPDocAcknowledgement);
				setReimbursementToDTO(documentList, OMPReimbursement,
						OMPDocAcknowledgement, documentDetailsDTO);
			}
		}

		if (tasks != null) {
			getDocumentListByHumanTask(tasks, documentList);
		}*/

		return documentList;

	}

	public List<ViewDocumentDetailsDTO> getDocumentDetailsForCloseClaimInProcess(
			String intimationNo, String userName, String passWord) {

		/*PayloadBOType payloadType = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadType.setIntimation(intimationType);

		CloseClaimTask closeClaimInProcessTask = BPMClientContext
				.getCloseClaimInProcessTask(userName, passWord);

		PagedTaskList tasks = closeClaimInProcessTask.getTasks(userName, null,
				payloadType);*/

		List<ViewDocumentDetailsDTO> documentList = new ArrayList<ViewDocumentDetailsDTO>();

		/*if (tasks != null) {

			getDocumentListByHumanTask(tasks, documentList);

		}*/
		return documentList;
	}

	/*private void getDocumentListByHumanTask(PagedTaskList tasks,
			List<ViewDocumentDetailsDTO> documentList) {
		List<HumanTask> humanTasks = tasks.getHumanTasks();
		for (HumanTask humanTask : humanTasks) {
			PayloadBOType reimbursementPayload = humanTask.getPayload();
			RODType rod = reimbursementPayload.getRod();
			if (rod != null && rod.getKey() != null) {

				OMPReimbursement OMPReimbursement = getReimbursement(rod.getKey());
				OMPDocAcknowledgement OMPDocAcknowledgement = OMPReimbursement
						.getDocAcknowLedgement();

				ViewDocumentDetailsDTO documentDetailsDTO = OMPEarlierRodMapper
						.getInstance().getDocumentDetails(OMPDocAcknowledgement);
				documentDetailsDTO.setTaskNumber(humanTask.getNumber());

				setReimbursementToDTO(documentList, OMPReimbursement,
						OMPDocAcknowledgement, documentDetailsDTO);
			}
		}
	}*/

	private void setReimbursementToDTO(
			List<ViewDocumentDetailsDTO> documentList,
			OMPReimbursement OMPReimbursement, OMPDocAcknowledgement OMPDocAcknowledgement,
			ViewDocumentDetailsDTO documentDetailsDTO) {
		String documentClassification = documentClassification(OMPDocAcknowledgement);
		documentDetailsDTO.setBillClassification(documentClassification);

		documentDetailsDTO.setRodNumber(OMPReimbursement.getRodNumber());
		documentDetailsDTO.setReimbursementKey(OMPReimbursement.getKey());
		// documentDetailsDTO.setMedicalResponseTime(OMPReimbursement
		// .getMedicalCompletedDate());

		documentDetailsDTO.setApprovedAmount(OMPReimbursement
				.getCurrentProvisionAmt());

		documentDetailsDTO.setStatus(OMPReimbursement.getStatus()
				.getProcessValue());

		if (OMPReimbursement.getMedicalCompletedDate() != null) {
			documentDetailsDTO.setMedicalResponseTime(SHAUtils
					.formatDate(OMPReimbursement.getMedicalCompletedDate()));
		}

		if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.BILLING_STAGE)) {

			Long approvedAmount = getReimbursementApprovedAmount(OMPReimbursement
					.getKey());
			if (approvedAmount > 0) {
				documentDetailsDTO.setApprovedAmount(approvedAmount
						.doubleValue());
			}

		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.FINANCIAL_STAGE)) {
			Long approvedAmount = getReimbursementApprovedAmount(OMPReimbursement
					.getKey());
			if (approvedAmount > 0) {
				documentDetailsDTO.setApprovedAmount(approvedAmount
						.doubleValue());
			}
		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_STAGE)) {
			if (OMPReimbursement.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
					|| OMPReimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
					|| OMPReimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
					|| OMPReimbursement.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_CLOSED)
					&& OMPReimbursement.getApprovalRemarks() != null
					|| OMPReimbursement.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_REOPENED)
					&& OMPReimbursement.getApprovalRemarks() != null) {

				Double approvedAmount = OMPReimbursement.getApprovedAmount();
				if (OMPReimbursement.getDocAcknowLedgement() != null) {
					if (OMPReimbursement.getDocAcknowLedgement()
							.getPreHospitalizationClaimedAmount() != null) {
						approvedAmount += OMPReimbursement.getDocAcknowLedgement()
								.getPreHospitalizationClaimedAmount();
					}
					if (OMPReimbursement.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null) {
						approvedAmount += OMPReimbursement.getDocAcknowLedgement()
								.getPostHospitalizationClaimedAmount();
					}
				}
				documentDetailsDTO.setApprovedAmount(approvedAmount);
			} else {
				documentDetailsDTO
						.setApprovedAmount(getClaimedAmount(OMPReimbursement));
			}

		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.BILL_ENTRY_STAGE_KEY)) {
			documentDetailsDTO
					.setApprovedAmount(getClaimedAmount(OMPReimbursement));
		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.CREATE_ROD_STAGE_KEY)) {
			documentDetailsDTO
					.setApprovedAmount(getClaimedAmount(OMPReimbursement));
		}

		if (ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(OMPReimbursement
				.getStatus().getKey())) {

			// Double provisionAmount = getProvisionAmount(OMPReimbursement);
			// documentDetailsDTO.setApprovedAmount(provisionAmount);

			// if(OMPReimbursement.getCurrentProvisionAmt() > 0){
			documentDetailsDTO.setApprovedAmount(OMPReimbursement
					.getCurrentProvisionAmt());
			// }

		} else if (ReferenceTable.RE_OPEN_CLAIM_STATUS_KEYS
				.containsKey(OMPReimbursement.getStatus().getKey())) {
			// Double provisionAmount = getProvisionAmount(OMPReimbursement);
			// documentDetailsDTO.setApprovedAmount(provisionAmount);
			// if(OMPReimbursement.getCurrentProvisionAmt() > 0){
			documentDetailsDTO.setApprovedAmount(OMPReimbursement
					.getCurrentProvisionAmt());
			// }
		}

		documentList.add(documentDetailsDTO);
	}

	public Double getProvisionAmount(OMPReimbursement OMPReimbursement) {

		Double claimedAmount = 0d;

		if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.CREATE_ROD_STAGE_KEY)) {

			claimedAmount = getClaimedAmount(OMPReimbursement);

		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.BILL_ENTRY_STAGE_KEY)) {

			claimedAmount = getClaimedAmount(OMPReimbursement);

		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.ZONAL_REVIEW_STAGE)) {

			claimedAmount = getClaimedAmount(OMPReimbursement);

		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.CLAIM_REQUEST_STAGE)) {

			if (OMPReimbursement.getStatus().getKey()
					.equals(ReferenceTable.CLAIM_REQUEST_APPROVE_STATUS)
					|| OMPReimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_STATUS)
					|| OMPReimbursement
							.getStatus()
							.getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_SEND_REPLY_FA_STATUS)
					|| OMPReimbursement.getStatus().getKey()
							.equals(ReferenceTable.CLAIM_REQUEST_CLOSED)
					&& OMPReimbursement.getApprovalRemarks() != null) {

				claimedAmount = OMPReimbursement.getApprovedAmount();

				if (OMPReimbursement.getDocAcknowLedgement() != null) {
					if (OMPReimbursement.getDocAcknowLedgement()
							.getPreHospitalizationClaimedAmount() != null) {
						claimedAmount += OMPReimbursement.getDocAcknowLedgement()
								.getPreHospitalizationClaimedAmount();
					}
					if (OMPReimbursement.getDocAcknowLedgement()
							.getPostHospitalizationClaimedAmount() != null) {
						claimedAmount += OMPReimbursement.getDocAcknowLedgement()
								.getPostHospitalizationClaimedAmount();
					}
				}

			} else {
				claimedAmount = getClaimedAmount(OMPReimbursement); // will be
																	// executed
																	// if staus
																	// is refer
																	// to
																	// specialist,
																	// escalate,
																	// investigation
																	// from MA
																	// stage
			}

		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.BILLING_STAGE)) {

			Long reimbursementApprovedAmount = getReimbursementApprovedAmount(OMPReimbursement
					.getKey());

			claimedAmount = Double.valueOf(reimbursementApprovedAmount);

		} else if (OMPReimbursement.getStage().getKey()
				.equals(ReferenceTable.FINANCIAL_STAGE)) {

			Long reimbursementApprovedAmount = getReimbursementApprovedAmount(OMPReimbursement
					.getKey());

			claimedAmount = Double.valueOf(reimbursementApprovedAmount);
		}

		return claimedAmount;

	}

	public Double getClaimedAmount(OMPReimbursement OMPReimbursement) {

		OMPDocAcknowledgement OMPDocAcknowledgement = OMPReimbursement
				.getDocAcknowLedgement();

		Double totalClaimedAmount = 0d;

		totalClaimedAmount = getClaimedAmountForReimbursement(OMPReimbursement);

		if (OMPReimbursement.getClaim().getClaimType() != null
				&& OMPReimbursement.getClaim().getClaimType().getKey()
						.equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) {

			if (OMPDocAcknowledgement.getDocumentReceivedFromId() != null
					&& OMPDocAcknowledgement.getDocumentReceivedFromId().getKey()
							.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)
					&& OMPDocAcknowledgement.getHospitalisationFlag() != null
					&& OMPDocAcknowledgement.getHospitalisationFlag()
							.equalsIgnoreCase("Y")
					&& OMPDocAcknowledgement.getPreHospitalisationFlag() != null
					&& OMPDocAcknowledgement.getPreHospitalisationFlag()
							.equalsIgnoreCase("N")
					&& OMPDocAcknowledgement.getPostHospitalisationFlag() != null
					&& OMPDocAcknowledgement.getPostHospitalisationFlag()
							.equalsIgnoreCase("N")
					&& OMPDocAcknowledgement.getPartialHospitalisationFlag() != null
					&& OMPDocAcknowledgement.getPartialHospitalisationFlag()
							.equalsIgnoreCase("N")
					&& OMPDocAcknowledgement.getLumpsumAmountFlag() != null
					&& OMPDocAcknowledgement.getLumpsumAmountFlag()
							.equalsIgnoreCase("N")
					&& OMPDocAcknowledgement.getPatientCareFlag() != null
					&& OMPDocAcknowledgement.getPatientCareFlag()
							.equalsIgnoreCase("N")
					&& OMPDocAcknowledgement.getHospitalCashFlag() != null
					&& OMPDocAcknowledgement.getHospitalCashFlag()
							.equalsIgnoreCase("N")) {

				totalClaimedAmount = getPreauthApprovedAmt(OMPReimbursement
						.getClaim().getKey());

			}
		}

		Double balanceSI = getBalanceSI(OMPReimbursement);
		if (balanceSI != null) {

			totalClaimedAmount = Math.min(balanceSI, totalClaimedAmount);

		}

		return totalClaimedAmount;
	}

	public Double getBalanceSI(OMPReimbursement OMPReimbursement) {
		DBCalculationService dbCalculationService = new DBCalculationService();
		Double sumInsured = 0d;
		Long policyKey = OMPReimbursement.getClaim().getIntimation().getPolicy()
				.getKey();
		if (null != OMPReimbursement.getClaim().getIntimation().getInsured()
				.getInsuredId()) {
			sumInsured = dbCalculationService.getInsuredSumInsured(
					String.valueOf(OMPReimbursement.getClaim().getIntimation()
							.getInsured().getInsuredId()), policyKey,OMPReimbursement.getClaim().getIntimation()
							.getInsured().getLopFlag());
		}
		Map balanceSIMap = dbCalculationService.getBalanceSI(policyKey,
				OMPReimbursement.getClaim().getIntimation().getInsured().getKey(),
				OMPReimbursement.getClaim().getKey(), sumInsured, OMPReimbursement
						.getClaim().getIntimation().getKey());
		Double balanceSI = (Double) balanceSIMap
				.get(SHAConstants.TOTAL_BALANCE_SI);
		return balanceSI;

	}

	private Double getPreauthApprovedAmt(Long claimKey) {

		Double approvedAmt = 0d;

		Query query = entityManager
				.createNamedQuery("Preauth.findByClaimKeyInDescendingOrder");
		query.setParameter("claimkey", claimKey);

		List<Preauth> preauthList = (List<Preauth>) query.getResultList();
		if (null != preauthList && !preauthList.isEmpty()) {
			Preauth preauth = preauthList.get(0);
			approvedAmt += preauth.getTotalApprovalAmount() != null ? preauth
					.getTotalApprovalAmount() : 0d;
			/*
			 * for (Preauth preauth : preauthList) { approvedAmt +=
			 * preauth.getTotalApprovalAmount() != null ?
			 * preauth.getTotalApprovalAmount() : 0d; }
			 */
		}
		return approvedAmt;

	}

	public Double getClaimedAmountForReimbursement(OMPReimbursement OMPReimbursement) {
		try {
			Double claimedAmount = 0.0;

			OMPDocAcknowledgement docAcknowledgment = OMPReimbursement
					.getDocAcknowLedgement();

			Query findType1 = entityManager.createNamedQuery(
					"ReimbursementBenefits.findByRodKey").setParameter(
					"rodKey", OMPReimbursement.getKey());
			List<ReimbursementBenefits> reimbursementBenefitsList = (List<ReimbursementBenefits>) findType1
					.getResultList();
			Double currentProvisionalAmount = 0.0;
			for (ReimbursementBenefits reimbursementBenefits : reimbursementBenefitsList) {
				currentProvisionalAmount += reimbursementBenefits
						.getTotalClaimAmountBills();

			}

			Double hospitalizationClaimedAmount = null != docAcknowledgment
					.getHospitalizationClaimedAmount() ? docAcknowledgment
					.getHospitalizationClaimedAmount() : 0.0;
			Double postHospitalizationClaimedAmount = null != docAcknowledgment
					.getPostHospitalizationClaimedAmount() ? docAcknowledgment
					.getPostHospitalizationClaimedAmount() : 0.0;
			Double preHospitalizationClaimedAmount = null != docAcknowledgment
					.getPreHospitalizationClaimedAmount() ? docAcknowledgment
					.getPreHospitalizationClaimedAmount() : 0.0;
			claimedAmount = hospitalizationClaimedAmount
					+ postHospitalizationClaimedAmount
					+ preHospitalizationClaimedAmount
					+ currentProvisionalAmount;

			return claimedAmount;
		} catch (Exception e) {

		}
		return null;
	}

	public List<ViewDocumentDetailsDTO> getAcknowledgmentForCancel(
			Long acknowledgementKey) {

		OMPDocAcknowledgement OMPDocAcknowledgement = getDocAcknowledgementBasedOnKey(acknowledgementKey);
		List<ViewDocumentDetailsDTO> documentDetailsDTO = new ArrayList<ViewDocumentDetailsDTO>();
		if (OMPDocAcknowledgement != null) {
			ViewDocumentDetailsDTO docAcknowledgementDTO = new ViewDocumentDetailsDTO();
			docAcknowledgementDTO.setAcknowledgeNumber(OMPDocAcknowledgement
					.getAcknowledgeNumber());
			if (OMPDocAcknowledgement.getDocumentReceivedFromId() != null) {
				docAcknowledgementDTO.setReceivedFromValue(OMPDocAcknowledgement
						.getDocumentReceivedFromId().getValue());
			}
			docAcknowledgementDTO.setDocumentReceivedDate(OMPDocAcknowledgement
					.getDocumentReceivedDate());
			if (OMPDocAcknowledgement.getModeOfReceiptId() != null) {
				docAcknowledgementDTO.setModeOfReceiptValue(OMPDocAcknowledgement
						.getModeOfReceiptId().getValue());
			}
			String documentClassification = documentClassification(OMPDocAcknowledgement);
			docAcknowledgementDTO.setBillClassification(documentClassification);

			Double claimedAmount = 0d;
			if (OMPDocAcknowledgement.getHospitalizationClaimedAmount() != null) {
				claimedAmount += OMPDocAcknowledgement
						.getHospitalizationClaimedAmount();
			}
			if (OMPDocAcknowledgement.getPreHospitalizationClaimedAmount() != null) {
				claimedAmount += OMPDocAcknowledgement
						.getPreHospitalizationClaimedAmount();
			}
			if (OMPDocAcknowledgement.getPostHospitalizationClaimedAmount() != null) {
				claimedAmount += OMPDocAcknowledgement
						.getPostHospitalizationClaimedAmount();
			}

			docAcknowledgementDTO.setApprovedAmount(claimedAmount);
			docAcknowledgementDTO.setStatus(OMPDocAcknowledgement.getStatus()
					.getProcessValue());

			documentDetailsDTO.add(docAcknowledgementDTO);

		}

		return documentDetailsDTO;
	}

	public String documentClassification(OMPDocAcknowledgement OMPDocAcknowledgement) {

		String classification = "";
		if (OMPDocAcknowledgement.getPreHospitalisationFlag() != null) {
			if (OMPDocAcknowledgement.getPreHospitalisationFlag().equals("Y")) {
				if (classification.equals("")) {
					classification = "Pre-Hospitalisation";
				} else {
					classification = classification + ","
							+ "Pre-Hospitalisation";
				}
			}
		}
		if (OMPDocAcknowledgement.getHospitalisationFlag() != null) {
			if (OMPDocAcknowledgement.getHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalisation";
				} else {
					classification = classification + "," + " Hospitalisation";
				}
			}
		}
		if (OMPDocAcknowledgement.getPostHospitalisationFlag() != null) {

			if (OMPDocAcknowledgement.getPostHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Post-Hospitalisation";
				} else {
					classification = classification + ","
							+ " Post-Hospitalisation";
				}
			}
		}

		if (OMPDocAcknowledgement.getHospitalCashFlag() != null) {

			if (OMPDocAcknowledgement.getHospitalCashFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Hospital cash)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Hospital cash)";
				}
			}
		}

		if (OMPDocAcknowledgement.getPatientCareFlag() != null) {

			if (OMPDocAcknowledgement.getPatientCareFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Patient Care)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Patient Care)";
				}
			}
		}

		if (OMPDocAcknowledgement.getLumpsumAmountFlag() != null) {

			if (OMPDocAcknowledgement.getLumpsumAmountFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Lumpsum Amount";
				} else {
					classification = classification + "," + "Lumpsum Amount";
				}
			}
		}

		if (OMPDocAcknowledgement.getHospitalizationRepeatFlag() != null) {

			if (OMPDocAcknowledgement.getHospitalizationRepeatFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalization Repeat";
				} else {
					classification = classification + ","
							+ "Hospitalization Repeat";
				}
			}
		}

		if (OMPDocAcknowledgement.getPartialHospitalisationFlag() != null) {

			if (OMPDocAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Partial Hospitalisation";
				} else {
					classification = classification + ","
							+ "Partial Hospitalisation";
				}
			}
		}

		return classification;
	}

	public void submitCancelAcknowlegement(ReceiptOfDocumentsDTO rodDTO) {

		Long acknowledgementKey = rodDTO.getDocumentDetails()
				.getDocAcknowledgementKey();

		OMPDocAcknowledgement OMPDocAcknowledgement = getDocAcknowledgementBasedOnKey(acknowledgementKey);

		Status status = entityManager.find(Status.class,
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);

		Stage stage = entityManager.find(Stage.class,
				ReferenceTable.ACKNOWLEDGE_STAGE_KEY);

		OMPDocAcknowledgement.setStage(stage);
		OMPDocAcknowledgement.setStatus(status);

		String cancelAcknowledgementRemarks = rodDTO.getDocumentDetails()
				.getCancelAcknowledgementRemarks();
		OMPDocAcknowledgement.setCancelRemarks(cancelAcknowledgementRemarks);

		SelectValue cancelAcknowledgementReason = rodDTO.getDocumentDetails()
				.getCancelAcknowledgementReason();

		MastersValue reason = entityManager.find(MastersValue.class,
				cancelAcknowledgementReason.getId());
		OMPDocAcknowledgement.setCancelReason(reason);

		entityManager.merge(OMPDocAcknowledgement);
		entityManager.flush();
		log.info("------OMPDocAcknowledgement------>" + OMPDocAcknowledgement
				+ "<------------");

		// need to implement bpmn task

	}

	@SuppressWarnings("unchecked")
	public List<ReimbursementCalCulationDetails> getReimbursementCalculationDetails(
			Long reimbursementKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementCalCulationDetails.findByRodKey");
		query = query.setParameter("reimbursementKey", reimbursementKey);

		List<ReimbursementCalCulationDetails> reimbursmentCalculationDetails = (List<ReimbursementCalCulationDetails>) query
				.getResultList();

		return reimbursmentCalculationDetails;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ReimbursementBenefits> getReimbursmentBenefits(
			Long reimbursementKey) {

		Query query = entityManager.createNamedQuery(
				"ReimbursementBenefits.findByRodKey").setParameter("rodKey",
				reimbursementKey);

		List<ReimbursementBenefits> reimbursmentBenefits = (List<ReimbursementBenefits>) query
				.getResultList();
		return reimbursmentBenefits;

	}

	public Long getReimbursementApprovedAmount(Long reimbursementKey) {

		Long approvedAmount = 0l;

		List<ReimbursementCalCulationDetails> reimbursementCalculationDetails = getReimbursementCalculationDetails(reimbursementKey);

		OMPReimbursement OMPReimbursement = getReimbursement(reimbursementKey);

		if (reimbursementCalculationDetails != null) {
			for (ReimbursementCalCulationDetails reimbursementCalCulationDetails2 : reimbursementCalculationDetails) {

				if (OMPReimbursement.getDocAcknowLedgement()
						.getDocumentReceivedFromId() != null
						&& OMPReimbursement.getDocAcknowLedgement()
								.getDocumentReceivedFromId().getKey()
								.equals(ReferenceTable.RECEIVED_FROM_HOSPITAL)) {
					if (reimbursementCalCulationDetails2
							.getPayableToHospAftTDS() != null) {
						if (reimbursementCalCulationDetails2
								.getPayableToHospAftTDS() > 0) {
							approvedAmount += reimbursementCalCulationDetails2
									.getPayableToHospAftTDS();
						}
					}
					// if(reimbursementCalCulationDetails2.getPayableToInsured()
					// != null){
					// if(reimbursementCalCulationDetails2.getPayableToInsured()>0){
					// approvedAmount +=
					// reimbursementCalCulationDetails2.getPayableToInsured();
					// }
					// }
				} else {
					if (reimbursementCalCulationDetails2.getPayableToInsured() != null) {
						approvedAmount += reimbursementCalCulationDetails2
								.getPayableInsuredAfterPremium();
					}
				}
			}
		}

		List<ReimbursementBenefits> reimbursmentBenefits = getReimbursmentBenefits(reimbursementKey);

		if (reimbursmentBenefits != null) {
			for (ReimbursementBenefits reimbursementBenefits : reimbursmentBenefits) {
				if (reimbursementBenefits.getPayableAmount() != null) {
					approvedAmount += reimbursementBenefits.getPayableAmount()
							.longValue();
				}
			}
		}
		return approvedAmount;
	}

	public OMPDocAcknowledgement findAcknowledgmentByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claimKey);

		// Integer.parseInt(Strin)
		List<OMPDocAcknowledgement> earlierAck = (List<OMPDocAcknowledgement>) query
				.getResultList();
		List<Long> keys = new ArrayList<Long>();

		for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {

			keys.add(OMPDocAcknowledgement.getKey());

		}
		if (!keys.isEmpty()) {
			Long maxKey = Collections.max(keys);
			OMPDocAcknowledgement OMPDocAcknowledgement = getDocAcknowledgementBasedOnKey(maxKey);
			return OMPDocAcknowledgement;
		}
		return null;

	}

	public OMPDocAcknowledgement findAcknowledgment(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByRODKey");
		query = query.setParameter("rodKey", rodKey);

		// Integer.parseInt(Strin)
		List<OMPDocAcknowledgement> earlierAck = (List<OMPDocAcknowledgement>) query
				.getResultList();
		List<Long> keys = new ArrayList<Long>();

		for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {

			keys.add(OMPDocAcknowledgement.getKey());

		}
		if (!keys.isEmpty()) {
			Long maxKey = Collections.max(keys);
			OMPDocAcknowledgement OMPDocAcknowledgement = getDocAcknowledgementBasedOnKey(maxKey);
			return OMPDocAcknowledgement;
		}
		return null;
	}

	public OMPClaim searchByClaimKey(Long a_key) {
		OMPClaim find = entityManager.find(OMPClaim.class, a_key);
		entityManager.refresh(find);
		return find;
	}

	@Override
	public Class<OMPDocAcknowledgement> getDTOClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<List<DocumentCheckListDTO>> getDocumentListForClaim(
			MasterService masterService, OMPClaim claim) {
		List<List<DocumentCheckListDTO>> previousDocCheckList = null;
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claim.getKey());

		List<OMPDocAcknowledgement> docAckObjList = (List<OMPDocAcknowledgement>) query
				.getResultList();
		if (null != docAckObjList && !docAckObjList.isEmpty()) {
			previousDocCheckList = new ArrayList<List<DocumentCheckListDTO>>();
			// List<DocumentCheckListDTO> docCheckList = new
			// ArrayList<DocumentCheckListDTO>();
			for (OMPDocAcknowledgement docAck : docAckObjList) {
				Query query1 = entityManager
						.createNamedQuery("RODDocumentCheckList.findByDocAck");
				query1.setParameter("docAck", docAck);
				List<OMPRODDocumentCheckList> docList = (List<OMPRODDocumentCheckList>) query1
						.getResultList();
				List<DocumentCheckListDTO> docChkList = new ArrayList<DocumentCheckListDTO>();

				OMPAckDocReceivedMapper OMPAckDocMapper = OMPAckDocReceivedMapper
						.getInstance();
				docChkList = OMPAckDocMapper
						.getRODDocumentCheckList(docList);
				previousDocCheckList.add(docChkList);
			}
			return previousDocCheckList;
		} else {
			return previousDocCheckList;
		}

	}

	@SuppressWarnings("unchecked")
	public List<RODQueryDetailsDTO> getRODQueryDetails(OMPClaim claim) {
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<OMPReimbursement> rodList = (List<OMPReimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			for (OMPReimbursement rod : rodList) {

				OMPReimbursement OMPReimbursement = getReimbursement(rod.getKey());

				List<ReimbursementQuery> rodQueryList = getRodQueryBasedOnRODKey(rod
						.getKey());

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				OMPDocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());

				for (ReimbursementQuery rodQuery : rodQueryList) {

					RODQueryDetailsDTO rodQueryDetails = new RODQueryDetailsDTO();
					if (OMPReimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(OMPReimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount()));
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rod.getStatus()
									.getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails
										.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());
							}
							if (rodQuery.getQueryReply() != null) {
								rodQueryDetails.setOnPageLoad(true);
							}
							rodQueryDetailsList.add(rodQueryDetails);

						}
					}
				}

			}
		}
		return rodQueryDetailsList;
	}

	public String getDiagnosisBasedOnRODKey(Long key) {
		Query query = entityManager
				.createNamedQuery("PedValidation.findByTransactionKey");
		query.setParameter("transactionKey", key);
		List<PedValidation> pedValList = query.getResultList();
		StringBuilder strDiag = null;
		if (null != pedValList && !pedValList.isEmpty()) {
			strDiag = new StringBuilder();
			for (PedValidation pedValidation : pedValList) {
				Query query1 = entityManager
						.createNamedQuery("Diagnosis.findDiagnosisByKey");
				query1.setParameter("diagnosisKey",
						pedValidation.getDiagnosisId());
				Diagnosis diag = (Diagnosis) query1.getSingleResult();
				strDiag.append(diag.getValue());
				strDiag.append(",");
			}
		}
		return String.valueOf(strDiag);
	}

	private List<ReimbursementQuery> getRodQueryBasedOnRODKey(Long rodKey) {

		List<ReimbursementQuery> filterQuery = new ArrayList<ReimbursementQuery>();

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursementForQuery");
		query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> mainResultQuery = query.getResultList();

		for (ReimbursementQuery reimbursementQuery : mainResultQuery) {
			entityManager.refresh(reimbursementQuery);
			if (reimbursementQuery.getStatus() != null
					&& (reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQuery
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))) {
				filterQuery.add(reimbursementQuery);
			}
		}
		return filterQuery;

	}

	public List<RODQueryDetailsDTO> getRODQueryDetailsForCreateRodAndBillEntry(
			OMPClaim claim, Long ackKey) {
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByClaimKey");
		query.setParameter("claimKey", claim.getKey());
		List<OMPReimbursement> rodList = (List<OMPReimbursement>) query
				.getResultList();
		List<RODQueryDetailsDTO> rodQueryDetailsList = null;

		if (null != rodList && !rodList.isEmpty()) {
			rodQueryDetailsList = new ArrayList<RODQueryDetailsDTO>();
			for (OMPReimbursement rod : rodList) {

				OMPReimbursement OMPReimbursement = getReimbursement(rod.getKey());

				List<ReimbursementQuery> rodQueryList = getRodQueryBasedOnAckKey(
						rod.getKey(), ackKey);

				if (rodQueryList != null) {
					for (ReimbursementQuery reimbursementQuery : rodQueryList) {
						entityManager.refresh(reimbursementQuery);
					}
				}
				// String getDiagnosisName =
				// (("null").equalsIgnoreCase(getDiagnosisBasedOnRODKey(rod.getKey()))
				// ? getDiagnosisBasedOnRODKey(rod.getKey()) : "");
				String getDiagnosisName = getDiagnosisBasedOnRODKey(rod
						.getKey());
				OMPDocAcknowledgement doc = getDocAcknowledgementBasedOnKey(rod
						.getDocAcknowLedgement().getKey());

				for (ReimbursementQuery rodQuery : rodQueryList) {

					RODQueryDetailsDTO rodQueryDetails = new RODQueryDetailsDTO();
					if (OMPReimbursement.getKey().equals(
							rodQuery.getReimbursement().getKey())) {
						if (!((ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_REJECT_STATUS)
								.equals(OMPReimbursement.getStatus().getKey()))) {
							rodQueryDetails.setRodNo(rod.getRodNumber());
							rodQueryDetails
									.setBillClassification(getBillClassificationValue(doc));
							rodQueryDetails.setDiagnosis(getDiagnosisName);
							rodQueryDetails
									.setClaimedAmount(calculatedClaimedAmt(
											doc.getHospitalizationClaimedAmount(),
											doc.getPreHospitalizationClaimedAmount(),
											doc.getPostHospitalizationClaimedAmount()));
							rodQueryDetails.setQueryRaisedRole(rodQuery
									.getCreatedBy());
							rodQueryDetails.setQueryRaisedDate(rodQuery
									.getCreatedDate());
							rodQueryDetails.setQueryStatus(rod.getStatus()
									.getProcessValue());
							rodQueryDetails.setReimbursementKey(rod.getKey());
							rodQueryDetails.setReimbursementQueryKey(rodQuery
									.getKey());
							rodQueryDetails.setAcknowledgementKey(rod
									.getDocAcknowLedgement().getKey());
							rodQueryDetails.setReplyStatus(rodQuery
									.getQueryReply());
							if (null != rod.getDocAcknowLedgement()
									&& null != rod.getDocAcknowLedgement()
											.getDocumentReceivedFromId()) {
								rodQueryDetails
										.setDocReceivedFrom(rod
												.getDocAcknowLedgement()
												.getDocumentReceivedFromId()
												.getValue());
							}

							if (rodQuery.getQueryReply() != null
									&& rodQuery.getQueryReply()
											.equalsIgnoreCase("N")) {
								rodQueryDetails.setOnPageLoad(true);
							}
							// rodQueryDetails.setOnPageLoad(true);
							rodQueryDetailsList.add(rodQueryDetails);

						}
					}
				}

			}
		}
		return rodQueryDetailsList;
	}

	private List<ReimbursementQuery> getRodQueryBasedOnAckKey(Long rodKey,
			Long ackKey) {

		Query query = entityManager
				.createNamedQuery("ReimbursementQuery.findByReimbursement");
		query.setParameter("reimbursementKey", rodKey);

		List<ReimbursementQuery> reimbursementQuery = (List<ReimbursementQuery>) query
				.getResultList();
		List<ReimbursementQuery> approvedQuery = new ArrayList<ReimbursementQuery>();

		for (ReimbursementQuery reimbursementQuery2 : reimbursementQuery) {
			if (reimbursementQuery2.getStatus() != null
					&& (reimbursementQuery2
							.getStatus()
							.getKey()
							.equals(ReferenceTable.MA_CORPORATE_QUERY_REPLY_STATUS)
							|| reimbursementQuery2
									.getStatus()
									.getKey()
									.equals(ReferenceTable.FA_QUERY_REPLY_STATUS)
							|| reimbursementQuery2
									.getStatus()
									.getKey()
									.equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS) || reimbursementQuery2
							.getStatus()
							.getKey()
							.equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS))) {
				approvedQuery.add(reimbursementQuery2);
			}
		}

		List<ReimbursementQuery> filterRodQuery = new ArrayList<ReimbursementQuery>();

		for (ReimbursementQuery reimbursementQuery2 : approvedQuery) {
			entityManager.refresh(reimbursementQuery2);

			if (reimbursementQuery2.getQueryReply() == null
					|| (reimbursementQuery2.getQueryReply() != null && reimbursementQuery2
							.getQueryReply().equalsIgnoreCase("N"))) {
				filterRodQuery.add(reimbursementQuery2);
			} else if (reimbursementQuery2.getQueryReply() != null
					&& reimbursementQuery2.getQueryReply()
							.equalsIgnoreCase("Y")) {
				if (reimbursementQuery2.getDocAcknowledgement() != null
						&& reimbursementQuery2.getDocAcknowledgement().getKey()
								.equals(ackKey)) {
					filterRodQuery.add(reimbursementQuery2);
				}
			}

		}
		return filterRodQuery;
	}

	/**
	 * Not in use
	 * */
	public Boolean getStatusOfFirstRODForAckValidationForRepeat(Long claimKey) {
		Boolean isFirstRODApproved = true;

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findFirstRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<OMPReimbursement> reimbursementList = query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			int iListSize = reimbursementList.size();
			// if(iListSize >1)
			{
				OMPReimbursement OMPReimbursement = reimbursementList.get(0);

				// OMPDocAcknowledgement OMPDocAcknowledgement =
				// OMPReimbursement.getDocAcknowLedgement();
				/*
				 * if(OMPDocAcknowledgement != null
				 * &&(("Y").equalsIgnoreCase(OMPDocAcknowledgement
				 * .getHospitalisationFlag())||
				 * ("Y").equalsIgnoreCase(OMPDocAcknowledgement
				 * .getPartialHospitalisationFlag()))){ return true; }
				 */
				if (!(ReferenceTable.FINANCIAL_APPROVE_STATUS
						.equals(OMPReimbursement.getStatus().getKey()))
						&& !ReferenceTable.getPaymentStatus().containsKey(
								OMPReimbursement.getStatus().getKey())) {
					isFirstRODApproved = false;
				} else {
					isFirstRODApproved = true;
				}
			}/*
			 * else{ isFirstRODApproved = true; }
			 */
		} else {
			isFirstRODApproved = false;
		}
		return isFirstRODApproved;
	}

	public Boolean getStatusOfFirstRODForAckValidation(Long claimKey) {
		Boolean isFirstRODApproved = true;

		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findFirstRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<OMPReimbursement> reimbursementList = query.getResultList();
		if (null != reimbursementList && !reimbursementList.isEmpty()) {
			for (OMPReimbursement OMPReimbursement : reimbursementList) {
				OMPDocAcknowledgement OMPDocAcknowledgement = OMPReimbursement
						.getDocAcknowLedgement();
				if (("Y").equalsIgnoreCase(OMPDocAcknowledgement
						.getHospitalisationFlag())
						|| ("Y").equalsIgnoreCase(OMPDocAcknowledgement
								.getPartialHospitalisationFlag())) {

					if ((ReferenceTable.FINANCIAL_APPROVE_STATUS
							.equals(OMPReimbursement.getStatus().getKey()))
							|| ReferenceTable.getPaymentStatus().containsKey(
									OMPReimbursement.getStatus().getKey())) {
						isFirstRODApproved = true;
						break;

					} else {
						isFirstRODApproved = false;

					}

				}
			}
		} else {
			isFirstRODApproved = false;
		}
		return isFirstRODApproved;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ViewDocumentDetailsDTO> getReceiptOfMedicalApproverClaim(
			Long a_key, Long reimbursementKey) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", a_key);

		// Integer.parseInt(Strin)
		List<Long> keysList = new ArrayList<Long>();
		List<OMPDocAcknowledgement> docAcknowledgmentList = (List<OMPDocAcknowledgement>) query
				.getResultList();

		List<OMPDocAcknowledgement> earlierAck = new ArrayList<OMPDocAcknowledgement>();
		for (OMPDocAcknowledgement OMPDocAcknowledgement : docAcknowledgmentList) {
			// if(OMPDocAcknowledgement.getRodKey() != null){
			// if(! OMPDocAcknowledgement.getRodKey().equals(reimbursementKey)){
			// earlierAck.add(OMPDocAcknowledgement);
			// }
			earlierAck.add(OMPDocAcknowledgement);
			keysList.add(OMPDocAcknowledgement.getKey());
			// }
		}

		Long maximumKey = 0l;
		if (!keysList.isEmpty()) {
			maximumKey = Collections.max(keysList);
		}

		for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {
			entityManager.refresh(OMPDocAcknowledgement);
		}

		List<ViewDocumentDetailsDTO> listDocumentDetails = OMPEarlierRodMapper
				.getInstance().getDocumentDetailsTableDTO(earlierAck);

		for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {

			String date = SHAUtils.getDateFormat(documentDetailsDTO
					.getReceivedDate());

			if (documentDetailsDTO.getRodKey() != null) {
				Long rodKey = new Long(documentDetailsDTO.getRodKey());
				Query rodQuery = entityManager
						.createNamedQuery("OMPReimbursement.findByKey");
				rodQuery = rodQuery.setParameter("primaryKey", rodKey);

				OMPReimbursement OMPReimbursement = (OMPReimbursement) rodQuery
						.getSingleResult();
				if (OMPReimbursement != null) {
					entityManager.refresh(OMPReimbursement);
				}
				documentDetailsDTO.setRodNumber(OMPReimbursement.getRodNumber());
				documentDetailsDTO.setReimbursementKey(OMPReimbursement.getKey());
				// documentDetailsDTO.setMedicalResponseTime(OMPReimbursement
				// .getMedicalCompletedDate());
				documentDetailsDTO.setApprovedAmount(OMPReimbursement
						.getApprovedAmount());

				if (OMPReimbursement.getStatus().getKey()
						.equals(ReferenceTable.ACKNOWLEDGE_STATUS_KEY)
						|| OMPReimbursement.getStatus().getKey()
								.equals(ReferenceTable.CREATE_ROD_STATUS_KEY)
						|| OMPReimbursement.getStatus().getKey()
								.equals(ReferenceTable.BILL_ENTRY_STATUS_KEY)) {

					documentDetailsDTO.setApprovedAmount(null);

				}

				documentDetailsDTO.setStatus(OMPReimbursement.getStatus()
						.getProcessValue());
				if (OMPReimbursement.getMedicalCompletedDate() != null) {
					documentDetailsDTO
							.setMedicalResponseTime(SHAUtils
									.formatDate(OMPReimbursement
											.getMedicalCompletedDate()));
				}

				documentDetailsDTO.setApprovedAmount(OMPReimbursement
						.getCurrentProvisionAmt());

				// if (OMPReimbursement.getStatus().getKey()
				// .equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
				// documentDetailsDTO.setApprovedAmount(OMPReimbursement
				// .getFinancialApprovedAmount());
				// } else if (OMPReimbursement
				// .getStatus()
				// .getKey()
				// .equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)) {
				// documentDetailsDTO.setApprovedAmount(OMPReimbursement
				// .getBillingApprovedAmount());
				// }

				// documentDetailsDTO.setApprovedAmount(OMPReimbursement.getApprovedAmount());

				// if
				// (OMPReimbursement.getStatus().getKey().equals(ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER)){
				//
				// Long approvedAmount =
				// getReimbursementApprovedAmount(OMPReimbursement.getKey());
				// if(approvedAmount >0){
				// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
				// }
				//
				// }else
				// if(OMPReimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
				// Long approvedAmount =
				// getReimbursementApprovedAmount(OMPReimbursement.getKey());
				// if(approvedAmount >0){
				// documentDetailsDTO.setApprovedAmount(approvedAmount.doubleValue());
				// }
				// }

			}
		}

		List<String> hospitalization = new ArrayList<String>();

		for (OMPDocAcknowledgement OMPDocAcknowledgement : earlierAck) {

			getDocumentBillClassification(hospitalization, OMPDocAcknowledgement);

		}

		for (int i = 0; i < listDocumentDetails.size(); i++) {

			listDocumentDetails.get(i).setBillClassification(
					hospitalization.get(i));
			listDocumentDetails.get(0).setLatestKey(maximumKey);
		}

		return listDocumentDetails;
	}

	private String getDocumentBillClassification(List<String> hospitalization,
			OMPDocAcknowledgement OMPDocAcknowledgement) {
		String classification = "";
		if (OMPDocAcknowledgement.getPreHospitalisationFlag() != null) {
			if (OMPDocAcknowledgement.getPreHospitalisationFlag().equals("Y")) {
				if (classification.equals("")) {
					classification = "Pre-Hospitalisation";
				} else {
					classification = classification + ","
							+ "Pre-Hospitalisation";
				}
			}
		}
		if (OMPDocAcknowledgement.getHospitalisationFlag() != null) {
			if (OMPDocAcknowledgement.getHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalisation";
				} else {
					classification = classification + "," + " Hospitalisation";
				}
			}
		}
		if (OMPDocAcknowledgement.getPostHospitalisationFlag() != null) {

			if (OMPDocAcknowledgement.getPostHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Post-Hospitalisation";
				} else {
					classification = classification + ","
							+ " Post-Hospitalisation";
				}
			}
		}

		if (OMPDocAcknowledgement.getHospitalCashFlag() != null) {

			if (OMPDocAcknowledgement.getHospitalCashFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Hospital cash)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Hospital cash)";
				}
			}
		}

		if (OMPDocAcknowledgement.getPatientCareFlag() != null) {

			if (OMPDocAcknowledgement.getPatientCareFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Add on Benefits (Patient Care)";
				} else {
					classification = classification + ","
							+ "Add on Benefits (Patient Care)";
				}
			}
		}

		if (OMPDocAcknowledgement.getLumpsumAmountFlag() != null) {

			if (OMPDocAcknowledgement.getLumpsumAmountFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Lumpsum Amount";
				} else {
					classification = classification + "," + "Lumpsum Amount";
				}
			}
		}

		if (OMPDocAcknowledgement.getHospitalizationRepeatFlag() != null) {

			if (OMPDocAcknowledgement.getHospitalizationRepeatFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Hospitalization Repeat";
				} else {
					classification = classification + ","
							+ "Hospitalization Repeat";
				}
			}
		}

		if (OMPDocAcknowledgement.getPartialHospitalisationFlag() != null) {

			if (OMPDocAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Partial Hospitalisation";
				} else {
					classification = classification + ","
							+ "Partial Hospitalisation";
				}
			}
		}

		hospitalization.add(classification);

		return classification;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<ViewDocumentDetailsDTO> getReceiptOfMedicalApproverByReimbursement(
			Long a_key, Long reimbursementKey) {

		List<OMPReimbursement> reimbursementDetails = getReimbursementDetails(a_key);

		List<ViewDocumentDetailsDTO> listDocumentDetails = OMPEarlierRodMapper
				.getInstance().getDocumentDetailsTableReimbursemntWise(
						reimbursementDetails);

		for (ViewDocumentDetailsDTO documentDetailsDTO : listDocumentDetails) {
			if (documentDetailsDTO.getMedicalCompleteDate() != null) {
				documentDetailsDTO
						.setMedicalResponseTime(SHAUtils
								.formatDate(documentDetailsDTO
										.getMedicalCompleteDate()));
			}

			List<OMPDocAcknowledgement> docAcknowledgmentList = getDocumentAckListByROD(documentDetailsDTO
					.getReimbursementKey());

			if (docAcknowledgmentList != null
					&& !docAcknowledgmentList.isEmpty()) {
				documentDetailsDTO.setStrLastDocumentReceivedDate(SHAUtils
						.formatDate(docAcknowledgmentList.get(0)
								.getDocumentReceivedDate()));
			}

			if (documentDetailsDTO.getDocAcknowledgement() != null) {
				String billClassificationValue = getBillClassificationValue(documentDetailsDTO
						.getOmpDocAcknowledgement());
				documentDetailsDTO
						.setBillClassification(billClassificationValue);
				documentDetailsDTO.setLatestKey(reimbursementKey);
			}

			if (documentDetailsDTO.getReimbursementKey() != null) {
				OMPDocAcknowledgement firstAcknowledgmentDetails = getFirstAcknowledgmentDetails(documentDetailsDTO
						.getReimbursementKey());
				if (firstAcknowledgmentDetails != null) {
					documentDetailsDTO
							.setDocumentReceivedDate(firstAcknowledgmentDetails
									.getDocumentReceivedDate());
				}
			}

		}

		return listDocumentDetails;

	}

	private OMPDocAcknowledgement getFirstAcknowledgmentDetails(Long rodKey) {
		OMPDocAcknowledgement ack = null;

		Query findByReimbursementKey = entityManager.createNamedQuery(
				"OMPDocAcknowledgement.findByReimbursement").setParameter(
				"rodKey", rodKey);
		try {
			List<OMPDocAcknowledgement> ackList = (List<OMPDocAcknowledgement>) findByReimbursementKey
					.getResultList();
			if (null != ackList && !ackList.isEmpty()) {
				ack = ackList.get(0);
			}
			return ack;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<ViewDocumentDetailsDTO> getViewAcknowledgementList(Long rodKey) {

		List<OMPDocAcknowledgement> docAcknowledgmentList = getDocumentAckListByROD(rodKey);

		List<ViewDocumentDetailsDTO> listDocumentDetails = OMPEarlierRodMapper
				.getInstance()
				.getDocumentDetailsTableDTO(docAcknowledgmentList);

		List<String> hospitalization = new ArrayList<String>();

		for (OMPDocAcknowledgement OMPDocAcknowledgement : docAcknowledgmentList) {

			getDocumentBillClassification(hospitalization, OMPDocAcknowledgement);

		}

		for (int i = 0; i < listDocumentDetails.size(); i++) {

			listDocumentDetails.get(i).setBillClassification(
					hospitalization.get(i));
			listDocumentDetails.get(0).setLatestKey(
					listDocumentDetails.get(i).getKey());
		}

		return listDocumentDetails;

	}

	private List<com.shaic.domain.OMPDocAcknowledgement> getDocumentAckListByROD(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByRODKey");
		query = query.setParameter("rodKey", rodKey);

		// Integer.parseInt(Strin)
		List<Long> keysList = new ArrayList<Long>();
		List<OMPDocAcknowledgement> docAcknowledgmentList = (List<OMPDocAcknowledgement>) query
				.getResultList();
		return docAcknowledgmentList;
	}

	@SuppressWarnings("unchecked")
	public OMPClaim getClaimsByIntimationNumber(String intimationNumber) {
		OMPClaim resultClaim = null;
		if (intimationNumber != null) {

			Query findByIntimationNum = entityManager.createNamedQuery(
					"OMPClaim.findByIntimationNumber").setParameter(
					"intimationNumber", intimationNumber);

			try {
				resultClaim = (OMPClaim) findByIntimationNum.getSingleResult();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultClaim;
	}

	/**
	 * Added for claim legal requirement.
	 * */

	public String getLegalFlagFromClaim(Long claimKey) {
		Query query = entityManager.createNamedQuery("OMPClaim.findByKey");
		query = query.setParameter("primaryKey", claimKey);
		List<OMPClaim> claimList = query.getResultList();
		if (null != claimList && !claimList.isEmpty()) {
			entityManager.refresh(claimList.get(0));
			OMPClaim objClaim = claimList.get(0);
			if (null != objClaim) {
				return objClaim.getLegalFlag();
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public OMPClaim getClaimByClaimKey(Long claimKey) {
		Query query = entityManager.createNamedQuery("OMPClaim.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<OMPClaim> claim = (List<OMPClaim>) query.getResultList();

		if (claim != null && !claim.isEmpty()) {
			for (OMPClaim claim2 : claim) {
				entityManager.refresh(claim2);
			}
			return claim.get(0);
		} else {
			return null;
		}
	}

	public ReimbursementRejection getReimbRejectRecord(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("ReimbursementRejection.findByReimbursementKey");
		query = query.setParameter("reimbursementKey", rodKey);
		List<ReimbursementRejection> reimbRejection = query.getResultList();
		if (null != reimbRejection && !reimbRejection.isEmpty()) {
			entityManager.refresh(reimbRejection.get(0));
			return reimbRejection.get(0);
		}
		return null;
	}

	public Boolean isLumpSumExistsForClaim(Long claimKey) {
		Boolean validationFlag = false;
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findLatestNonCanceledRODByClaimKey");
		query = query.setParameter("claimKey", claimKey);
		query = query.setParameter("statusId",
				ReferenceTable.CANCEL_ACKNOWLEDGEMENT_STATUS);
		List<OMPReimbursement> rodList = query.getResultList();
		if (rodList != null && !rodList.isEmpty()) {
			for (OMPReimbursement rodObj : rodList) {
				entityManager.refresh(rodObj);
			}
			for (OMPReimbursement OMPReimbursement : rodList) {
				if (SHAConstants.YES_FLAG.equalsIgnoreCase(OMPReimbursement
						.getDocAcknowLedgement().getLumpsumAmountFlag())) {
					validationFlag = true;
					break;
				}
			}
		}
		return validationFlag;
	}

	public List<OMPReimbursement> getReimbursementByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByOMPClaimKey");
		query = query.setParameter("claimKey", claimKey);
		List<OMPReimbursement> reimbursementList = query.getResultList();
		return reimbursementList;
	}

	public List<OMPViewDocumentDetailsTableDTO> getOMPDocumentListForClaim(Long claimKey) {
		List<OMPViewDocumentDetailsTableDTO> detailsTableDTOList = new ArrayList();
		Query query = entityManager.createNamedQuery("OMPDocAcknowledgement.findByClaimKeyByDesc");
		query = query.setParameter("claimkey", claimKey);

		List<OMPDocAcknowledgement> docAckObjList = (List<OMPDocAcknowledgement>) query.getResultList();
		if (null != docAckObjList && !docAckObjList.isEmpty()) {
			
			for (OMPDocAcknowledgement docAck : docAckObjList) {
				OMPViewDocumentDetailsTableDTO oMPViewDocumentDetailsTableDTO = new OMPViewDocumentDetailsTableDTO();
				if(docAck.getClassificationId() != null)
				oMPViewDocumentDetailsTableDTO.setClassification(docAck.getClassificationId().getValue());
				if(docAck.getSubClassificationId() != null)
				oMPViewDocumentDetailsTableDTO.setSubClassification(docAck.getSubClassificationId().getValue());
				if(docAck.getCategoryId() != null)
				oMPViewDocumentDetailsTableDTO.setCategory(docAck.getCategoryId().getValue());
				if(docAck.getClaim() != null)
					oMPViewDocumentDetailsTableDTO.setClaimKey(docAck.getClaim().getKey());
				if(docAck.getKey() != null)
					oMPViewDocumentDetailsTableDTO.setAckKey(docAck.getKey());
				
				detailsTableDTOList.add(oMPViewDocumentDetailsTableDTO);
				break;
			}
			return detailsTableDTOList;
		}
       return detailsTableDTOList;
	}
	
	public List<OMPDocAcknowledgement> findAcknowledgmentListByClaimKey(Long claimKey) {
		Query query = entityManager
				.createNamedQuery("OMPDocAcknowledgement.findByClaimKey");
		query = query.setParameter("claimkey", claimKey);

		// Integer.parseInt(Strin)
		List<OMPDocAcknowledgement> earlierAck = (List<OMPDocAcknowledgement>) query
				.getResultList();
		return earlierAck;

	}
	
	public List<OMPViewUploadDocumentDetailsTableDTO> getDocumentDetailsData(String intimationNumber)
	{
		List<OMPViewUploadDocumentDetailsTableDTO> detailsTableDTOList = new ArrayList<OMPViewUploadDocumentDetailsTableDTO>();
		try {
			Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByIntimationNoOrderByCreatedDate");
			query = query.setParameter("intimationNumber", intimationNumber);
			
			List<DMSDocumentDetailsDTO> documentDetailsDTOList =  null;
			List<OMPDocumentDetails> documentDetailsList  = query.getResultList();
			
			if(documentDetailsList != null && !documentDetailsList.isEmpty()){
				for (OMPDocumentDetails documentDetails : documentDetailsList) {
					OMPViewUploadDocumentDetailsTableDTO detailsTableDTO = new OMPViewUploadDocumentDetailsTableDTO();
					detailsTableDTO.setFileType(documentDetails.getFileType());
					detailsTableDTO.setDocumentType(documentDetails.getDocumentType());
					detailsTableDTO.setReceivedStatus(documentDetails.getRecievedStatus());
					if(documentDetails.getNoOfDoc() != null)
					detailsTableDTO.setNoOfDocuments(documentDetails.getNoOfDoc().toString());
					detailsTableDTO.setRemarks(documentDetails.getRemarks());
					detailsTableDTOList.add(detailsTableDTO);
				}
				return detailsTableDTOList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detailsTableDTOList;
	}
	
	public List<OMPViewUploadDocumentDetailsTableDTO> getDocumentsByAckNumber(String ackNumber)
	{
		List<OMPViewUploadDocumentDetailsTableDTO> detailsTableDTOList = new ArrayList<OMPViewUploadDocumentDetailsTableDTO>();
		Query query = entityManager.createNamedQuery("OMPDocumentDetails.findByAckNoDesc");
		query = query.setParameter("acknowledgeNumber", ackNumber);
		List<OMPDocumentDetails> documentDetailsList  = query.getResultList();
		
		if(documentDetailsList != null && !documentDetailsList.isEmpty()){
			for (OMPDocumentDetails documentDetails : documentDetailsList) {
				OMPViewUploadDocumentDetailsTableDTO detailsTableDTO = new OMPViewUploadDocumentDetailsTableDTO();
				detailsTableDTO.setFileType(documentDetails.getFileType());
				detailsTableDTO.setDocumentType(documentDetails.getDocumentType());
				detailsTableDTO.setReceivedStatus(documentDetails.getRecievedStatus());
				if(documentDetails.getNoOfDoc() != null)
				detailsTableDTO.setNoOfDocuments(documentDetails.getNoOfDoc().toString());
				detailsTableDTO.setRemarks(documentDetails.getRemarks());
				detailsTableDTOList.add(detailsTableDTO);
				break;
			}
			return detailsTableDTOList;
		}
		return null;
	
	}
}
