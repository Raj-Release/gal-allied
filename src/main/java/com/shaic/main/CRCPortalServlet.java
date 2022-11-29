package com.shaic.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.claimhistory.view.PedHistoryDTO;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryDTO;
import com.shaic.claim.coordinator.view.ViewCoOrdinatorDTO;
import com.shaic.claim.coordinator.view.ViewCoOrdinatorReplyMapper;
import com.shaic.claim.fileUpload.CoordinatorService;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRMapper;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsDTO;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsService;
import com.shaic.claim.pedrequest.view.ViewDoctorRemarksDTO;
import com.shaic.claim.pedrequest.view.ViewpedRequestService;
import com.shaic.claim.preauth.HRMTableDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.IntimationDetailDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDetailDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDetailDTO;
import com.shaic.claim.preauth.wizard.dto.PreviousPolicyDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.registration.previousinsurance.view.PreviousInsuranceInsuredDetailsTableDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionService;
import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionTableDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ComprehensiveSublimitDTO;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.FVRGradingDetail;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PreviousPolicy;
import com.shaic.domain.PreviousPolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PedEndorementHistory;
import com.shaic.domain.preauth.PedQuery;
import com.shaic.domain.preauth.PedSpecialist;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.portal.service.ClaimStatusService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public class CRCPortalServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private final Logger logger = LoggerFactory.getLogger(CRCPortalServlet.class);
	
	@EJB
	private PreauthService preauthService;
			
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimStatusService claimStatusService;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private PreauthService preAuthService;
	
	@EJB
	private OMPIntimationService ompIntimationService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private PreviousPolicyService previousPolicyService;
	
	@EJB
	private ViewFVRService viewFVRService;
	
	@EJB
	private FieldVisitRequestService fieldVisitRequestService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ViewpedRequestService pedRequestService;
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private ViewPEDEndoresementDetailsService pedEndorsementService;
	
	@Inject
	private DBCalculationService DBCalculationService;
	
	@Inject
	private IntimationService intimationService;
	
	@Inject
	private ClaimService claimService;
	
	@Inject
	private AcknowledgementDocumentsReceivedService acknowledgementDocumentsReceivedService;
	
	@Inject
	private CoordinatorService coordinatorService;
	
	@Inject
	private ViewSpecialistOpinionService viewSpecialistOpinionService;
	
	@Inject
	private ViewFVRMapper viewFVRMapper;
	
	private static final String VIEW_ACKNOWLEDGMENT_DETAILS = "Acknowledgment Details";
	
	private static final String VIEW_COORDINATOR_REPLY = "Coordinator Reply";
	
	private static final String VIEW_SPECIALIST_TRAIL = "Specialist Trail";
	
	private static final String VIEW_64VB_COMPLIANCE = "64Vb Compliance";
	
	private static final String VIEW_SUBLIMITS = "Sublimits";
	
	private static final String VIEW_HRM_DETAILS = "Hrm Details";
	
	private static final String VIEW_RISK_DETAILS = "Risk Details";
	
	private static final String VIEW_PREVIOUS_INSURANCE_DETAILS = "Previous Insurance Details";
	
	private static final String VIEW_BALANCE_SUM_INSURED = "Balance Sum Insured";
	
	private static final String VIEW_PRE_AUTH_DETAILS = "Pre Auth Details";
	
	private static final String VIEW_FVR_DETAILS = "FVR Details";
	
	private static final String VIEW_DOCTOR_NOTES = "Doctor Notes";
	
	private static final String VIEW_PED_REQUEST = "PED Request";
	
	private static final String VIEW_HISTORY = "History";
	
	private static final String VIEW_INTIMATION = "Intimation";
	
	private static final String VIEW_HOSPITALS = "Hospitals";
	
	private static final String PAGE_NAME = "pageName";
	
	private static final String VIEW_NEGOTIATION_DETAILS = "Negotiationdetails";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 logger.debug("Enter CRCPortalServlet.doGet() Method");
		 String queryString = request.getQueryString();
		 response.setContentType("application/json");
		 response.setCharacterEncoding("UTF-8");
		 if (queryString != null && queryString.contains("token")) {
			 JSONObject jsonObject = null;
			 jsonObject = getClaimStatusByToken(request.getParameter("token"));
			 if (jsonObject != null ) {
				 String intimationNo = (String) jsonObject.get(SHAConstants.JSON_CLAIM_NUMBER);
				 if (intimationNo != null) {
					 Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);
					 if(intimation != null) {
						 Claim claim = claimService.getClaimforIntimation(intimation.getKey());
						 if (request.getParameter(PAGE_NAME) != null && !request.getParameter(PAGE_NAME).isEmpty()) {
							 String pageName = request.getParameter(PAGE_NAME);
							 logger.debug("CRC portal view name " + pageName);
							 JSONObject jsonResponse = null;
							 if (pageName.equalsIgnoreCase(VIEW_ACKNOWLEDGMENT_DETAILS)) {
								 jsonResponse = getAcknowlegmentDetails(claim.getKey());
							 } else if (pageName.equalsIgnoreCase(VIEW_COORDINATOR_REPLY)) {
								 jsonResponse =  getCoordinatorReplyDetails(intimation.getKey());
							 } else if (pageName.equalsIgnoreCase(VIEW_SPECIALIST_TRAIL)) {
								 jsonResponse =  getSpecialistTrailDetails(intimation.getKey());
							 } else if (pageName.equalsIgnoreCase(VIEW_64VB_COMPLIANCE)) {
								 jsonResponse =  get64VBComplianceDetails(intimationNo);
							 } else if (pageName.equalsIgnoreCase(VIEW_SUBLIMITS)) {
								 jsonResponse =  getSubLimitDetails(intimation, claim);
							 } else if (pageName.equalsIgnoreCase(VIEW_HRM_DETAILS)) {
								 jsonResponse =  getHrmDetails(intimation, claim);
							 } else if (pageName.equalsIgnoreCase(VIEW_RISK_DETAILS)) {
								 jsonResponse =  getRiskDetails(intimation);
							 } else if (pageName.equalsIgnoreCase(VIEW_PREVIOUS_INSURANCE_DETAILS)) {
								 jsonResponse =  getPreviousInsuranceDetails(intimation);
							 } else if (pageName.equalsIgnoreCase(VIEW_BALANCE_SUM_INSURED)) {
								 jsonResponse =  getBalanceSumInsuredDetails(intimation, claim);
							 } else if (pageName.equalsIgnoreCase(VIEW_PRE_AUTH_DETAILS)) {
								 if (request.getParameter("preIntimationKey") != null && !request.getParameter("preIntimationKey").isEmpty()) {
									 String preIntimationKey = request.getParameter("preIntimationKey");
									 jsonResponse =  getPreAuthDetails(intimation, preIntimationKey);
								 }
							 } else if (pageName.equalsIgnoreCase(VIEW_FVR_DETAILS)) {
								 jsonResponse =  getFvrDetails(intimation);
							 } else if (pageName.equalsIgnoreCase(VIEW_DOCTOR_NOTES)) {
								 jsonResponse = getDoctorNotes(intimation);
							 } else if (pageName.equalsIgnoreCase(VIEW_PED_REQUEST)) {
								 jsonResponse = getPedRequestDetails(intimationNo);
							 } else if (pageName.equalsIgnoreCase(VIEW_HISTORY)) {
								 jsonResponse = getClaimHistoryDetails(intimationNo);
							 } else if(pageName.equalsIgnoreCase(VIEW_INTIMATION)) {
								 jsonResponse = getIntimationDetails(intimation);
							 } else if(pageName.equalsIgnoreCase(VIEW_HOSPITALS)) {
								 jsonResponse = getHospitalDetails(intimation);
							 } else if (pageName.equalsIgnoreCase(VIEW_NEGOTIATION_DETAILS)) {
								 jsonResponse = getNegotiationDetails(intimationNo);
							 }
							if (jsonResponse != null) {
								response.setStatus(HttpServletResponse.SC_OK);
								response.getWriter().write(jsonResponse.toJSONString());
							} else {
								response.setStatus(HttpServletResponse.SC_NO_CONTENT);
							}							 
						 } else {
							 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							 logger.info("Invalid page name");							
						 }
					 } else {
						 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						 logger.info("Invalid intimation no");
					 }
				 } else {
					 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					 logger.info("Intimation no is required");
				 }
			 } else {
				 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				 logger.info("Invalid token");
			 }
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			logger.info("Auth token is required");
		}
	}

	private JSONObject getAcknowlegmentDetails(Long claimKey) {
		logger.debug("Enter CRCPortalServlet.getAcknowlegmentDetails() Method");
		JSONObject jsonObject = new JSONObject();
		if (claimKey != null) {
			DocAcknowledgement docAcknowledgement = acknowledgementDocumentsReceivedService.findAcknowledgmentByClaimKey(claimKey);
			if (docAcknowledgement != null ) {
					DocumentDetailsDTO docackValues = acknowledgementDocumentsReceivedService.getAcknowledgementDetails(docAcknowledgement.getKey());
					if (docackValues != null) {
						docackValues.setAcknowledgmentCreatedId(docAcknowledgement.getCreatedBy());
						docackValues.setAcknowledgmentCreatedName(docAcknowledgement.getCreatedBy());
						if(docAcknowledgement.getDocumentReceivedDate() != null) {
							String formatDate = SHAUtils.getDateWithoutTime(docAcknowledgement.getDocumentReceivedDate());
							docackValues.setDocumentReceivedDate(formatDate);
						}
						jsonObject.put("documentValues", docackValues);
					}
					
					List<DocumentCheckListDTO> ackCheckList = acknowledgementDocumentsReceivedService.getDocumentList(docAcknowledgement.getKey());
					if(ackCheckList != null) {
						jsonObject.put("ackCheckList", ackCheckList);
					}
					
					List<RODQueryDetailsDTO> ackQueryList = acknowledgementDocumentsReceivedService.getQueryDetailsList(docAcknowledgement.getKey());
					if(ackQueryList != null) {
						for(RODQueryDetailsDTO query : ackQueryList) {
							if(query.getQueryRaisedDate() != null) {
								String formatDate = SHAUtils.formateDateForHistory(query.getQueryRaisedDate());
								query.setQueryRaisedDateString(formatDate);
								query.setQueryDetails(claimStatusService.getQueryDetails(query));
							}
							
							if(query.getQueryDetails() != null && query.getQueryDetails().getIntimationNo() != null) {
						        String intimationNo = query.getQueryDetails().getIntimationNo();
								DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
								dmsDTO.setIntimationNo(intimationNo);
								Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
								dmsDTO.setClaimNo(claim.getClaimId());	
								List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService.getQueryDocumentDetailsData(intimationNo, SHAConstants.QUERY);
								if (dmsDocumentDetailsDTO != null && !dmsDocumentDetailsDTO.isEmpty()) {
									 for (DMSDocumentDetailsDTO docDTO : dmsDocumentDetailsDTO){
										 docDTO.setFileViewURL(claimStatusService.getDocumentFileURL(docDTO.getFileName(), docDTO.getDmsDocToken()));
									 }									 
									dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
								}
								query.setDmsDocumentDto(dmsDTO);
							}
							jsonObject.put("ackQueryList", ackQueryList);
						}
					}
					
					List<ReconsiderRODRequestTableDTO> reconsiderRODList = acknowledgementDocumentsReceivedService.getReconsiderationDetailsList(docAcknowledgement);
					if(reconsiderRODList != null) {
						jsonObject.put("ackReconsiderRODList", reconsiderRODList);
					}
				}
			}
		return jsonObject;
	}
	
	private JSONObject getClaimStatusByToken(String token) {
		logger.debug("Enter CRCPortalServlet.getClaimStatusByToken() Method");			
		JSONObject jsonObject = null;
		jsonObject = preauthService.validateToken(token);
		if (jsonObject != null) {
			String requestJsonInString = jsonObject.toJSONString();
			logger.info("View Claim Status - CRC Portal Json Input " + requestJsonInString);
		}
		return jsonObject;
	}
	
	private JSONObject getCoordinatorReplyDetails(Long intimationkey) {
		logger.debug("Enter CRCPortalServlet.getCoordinatorReplyDetails() Method");	
		JSONObject jsonObject = new JSONObject();
		if(intimationkey != null) {
			List<Coordinator> coorinatorList = coordinatorService.getCoordinatorListByIntimationKey(intimationkey);
			if(coorinatorList != null) {
				List<ViewCoOrdinatorDTO> coordinateDetails = ViewCoOrdinatorReplyMapper.getInstance().getViewCoOrdinatorDTO(coorinatorList);
				if(coordinateDetails != null) {
					List<MultipleUploadDocumentDTO> documentList = null;
					for(ViewCoOrdinatorDTO coordinateDTO : coordinateDetails) {
						documentList = reimbursementService.getUpdateDocumentDetails(coordinateDTO.getKey());
						if(documentList != null) {
							for (MultipleUploadDocumentDTO document : documentList){
								document.setFileUrl(claimStatusService.getDocumentFileURL(document.getFileName(), document.getFileToken()));
							}
							coordinateDTO.setUploadedDocumentList(documentList);
						}
					}
					jsonObject.put("coordinateDetails", coordinateDetails);
				}
			}
		}
		return jsonObject;
	}
	
	private JSONObject getSpecialistTrailDetails(Long intimationKey) {
		logger.debug("Enter CRCPortalServlet.getSpecialistTrailDetails() Method");	
		JSONObject jsonObject = new JSONObject();
		if (intimationKey != null) {
			List<ViewSpecialistOpinionTableDTO> viewSpecilaistOpinionDTO = viewSpecialistOpinionService.search(intimationKey);
			if (viewSpecilaistOpinionDTO != null) {
				List<MultipleUploadDocumentDTO> documentList = null;
				for (ViewSpecialistOpinionTableDTO specialistDto : viewSpecilaistOpinionDTO) {						
					documentList = reimbursementService.getUpdateDocumentDetails(specialistDto.getKey());
					if(documentList != null) {
						for (MultipleUploadDocumentDTO document : documentList){
							document.setFileUrl(claimStatusService.getDocumentFileURL(document.getFileName(), document.getFileToken()));
						}
						specialistDto.setUploadedDocumentList(documentList);
					}
				}
				jsonObject.put("specialistOpinionDetails", viewSpecilaistOpinionDTO);
			}
		}
		return jsonObject;
	}
	 
	private JSONObject get64VBComplianceDetails(String intimationNo) {
		logger.debug("Enter CRCPortalServlet.get64VBComplianceDetails() Method");	
		JSONObject jsonObject = null;
		if (intimationNo != null) {
			String filePath = claimStatusService.get64VBComplainceFilePath(intimationNo);
			if(filePath != null) {
				File pdfFile = new File(filePath);
				byte[] encodedBytes = null;
				try {
					encodedBytes = Base64.encodeBase64(FileUtils.readFileToByteArray(pdfFile));
				} catch (IOException e) {
					logger.error("Error while converting 64VBCompliance file to base64" + e );
				}
				if (encodedBytes != null) {
					jsonObject = new JSONObject();
					jsonObject.put("filePath", new String(encodedBytes));
				}
			}
		}
		return jsonObject;
	}
	
	private JSONObject getSubLimitDetails(Intimation intimation, Claim claim) {
		logger.debug("Enter CRCPortalServlet.getSubLimitDetails() Method");	
		JSONObject jsonObject = null;
		if (intimation != null && claim != null) {
			if (intimation.getPolicy() != null) {
				Policy policy = policyService.getPolicyByKey(intimation.getPolicy().getKey());
				if (policy != null) {
					Long productKey = (policy.getProduct() != null ? policy.getProduct().getKey() : 0l);
					if (intimation.getInsured() != null) {
						Double insuredAge = intimation.getInsured().getInsuredAge() != null ? intimation.getInsured().getInsuredAge() : 0d;
						Long insuredNumber = intimation.getInsured().getInsuredId();
						Preauth latestPreauthByIntimationKey = preAuthService.getLatestPreauthByIntimationKey(intimation.getKey());
						if( claim != null && claim.getKey() != null) {
							List<ComprehensiveSublimitDTO> comprehensiveSublimitDTO = null;
							if (latestPreauthByIntimationKey != null) {				
								Long section = latestPreauthByIntimationKey.getSectionCategory() != null ? latestPreauthByIntimationKey.getSectionCategory() : 0l;
								String plan = policy.getPolicyPlan() != null ? policy.getPolicyPlan() : "0";
								
								/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(policy.getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(policy.getProduct().getKey())) {*/
								if(intimation.getPolicy().getProduct() != null 
										&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(intimation.getPolicy().getProduct().getCode()) ||
												SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(intimation.getPolicy().getProduct().getCode()))
												|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(intimation.getPolicy().getProduct().getCode())
												|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(intimation.getPolicy().getProduct().getCode()))
												|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(intimation.getPolicy().getProduct().getCode()) ||
														SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(intimation.getPolicy().getProduct().getCode()))
														&& intimation.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))) {
									plan = intimation.getInsured().getPolicyPlan() != null ? intimation.getInsured().getPolicyPlan() : "0";
								}
								
								comprehensiveSublimitDTO = DBCalculationService.getSublimitUtilizationBasedOnProduct(insuredNumber, productKey, insuredAge, section, plan, claim.getKey());
							} else {
								comprehensiveSublimitDTO = DBCalculationService.getSublimitUtilizationBasedOnProduct(insuredNumber, productKey, insuredAge, 0l , "0", claim.getKey());
							}
							
							if (comprehensiveSublimitDTO != null) {
								jsonObject = new JSONObject();
								jsonObject.put("sublimitDetails", comprehensiveSublimitDTO);
							}
						}
					}
				}
			}
		}
		return jsonObject;
	}
	
	private JSONObject getHrmDetails(Intimation intimation, Claim claim) {
		logger.debug("Enter CRCPortalServlet.getHrmDetails() Method");	
		JSONObject jsonObject = null;
		if (intimation != null) {
			List<Preauth> preauthList = preAuthService.getPreauthByIntimationKey(intimation.getKey());
			if(preauthList != null) {
				PreauthDTO preauthDTO = new PreauthDTO();
				PreMedicalMapper premedicalMapper = new PreMedicalMapper();
				if (preauthList != null && !preauthList.isEmpty()) {
					preauthDTO = premedicalMapper.getPreauthDTO(preauthList.get(0));
				}
				NewIntimationDto newIntimationDto = intimationService.getIntimationDto(intimation);	
				preauthDTO.setNewIntimationDTO(newIntimationDto);
				if (preauthDTO != null) {
					List<HRMTableDTO> earilerHrmDetails = preAuthService.getEarlierHrmDetails(preauthDTO.getNewIntimationDTO().getIntimationId());
					if (preauthDTO.getPreauthDataExtractionDetails() != null) {
						List<DiagnosisDetailsTableDTO> diagnosisList =  preauthDTO.getPreauthDataExtractionDetails().getDiagnosisTableList();
						
						if (diagnosisList == null || CollectionUtils.isEmpty(diagnosisList)) {
							List<PedValidation> findPedValidationByPreauthKey = preauthService.findPedValidationByPreauthKey(preauthDTO.getKey());
							diagnosisList = premedicalMapper
									.getNewPedValidationTableListDto(findPedValidationByPreauthKey);
							
							for (DiagnosisDetailsTableDTO pedValidationTableDTO : diagnosisList) {
								if (pedValidationTableDTO.getDiagnosisName() != null) {
									String diagnosis = masterService.getDiagnosis(pedValidationTableDTO.getDiagnosisName().getId());
									pedValidationTableDTO.setDiagnosis(diagnosis);
									pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
								}
							}		
						}
						
						for (HRMTableDTO hrmTableDTO : earilerHrmDetails) {					
							String diagnosisName = StringUtils.EMPTY;
							for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
								diagnosisName += diagnosisDetailsTableDTO.getDiagnosis() + " ";
							}
							if(hrmTableDTO.getAssigneeDateAndTime() != null) {
								String formatedDate = SHAUtils.formateDateForHistory(hrmTableDTO.getAssigneeDateAndTime());
								hrmTableDTO.setAssigneeDateAndTimeStr(formatedDate);
							}
							hrmTableDTO.setDiagnosis(diagnosisName);
						}
					}
					
					jsonObject = new JSONObject();
					jsonObject.put("intimationNo", (preauthDTO.getNewIntimationDTO() != null ? preauthDTO.getNewIntimationDTO().getIntimationId() : ""));
					jsonObject.put("hospitalName", (preauthDTO.getNewIntimationDTO() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto() != null ? preauthDTO.getNewIntimationDTO().getHospitalDto().getName() : "" ) : "" ));
					jsonObject.put("hrmId", (preauthDTO.getNewIntimationDTO() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null ? preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode(): "" ) : "" ) : "" ));
					jsonObject.put("hrmPhone", (preauthDTO.getNewIntimationDTO() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null ? preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmContactNo(): "" ) : "" ) : "" ));
					jsonObject.put("hrmName", (preauthDTO.getNewIntimationDTO() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null ? preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmUserName(): "" ) : "" ) : "" ));
					jsonObject.put("hrmEmail", (preauthDTO.getNewIntimationDTO() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto() != null ? (preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals() != null ? preauthDTO.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getEmailId(): "" ) : "" ) : "" ));
					jsonObject.put("hrmDiagnosisList", earilerHrmDetails);
				}
			}
		}
		return jsonObject;
	}
	
	private JSONObject getRiskDetails(Intimation intimation) {
		logger.debug("Enter CRCPortalServlet.getRiskDetails() Method");	
		JSONObject jsonObject = null;
		if (intimation != null) {
			jsonObject = new JSONObject();
			jsonObject.put("policyNo", (intimation.getPolicy() != null ? intimation.getPolicy().getPolicyNumber() : ""));
			jsonObject.put("policyName", (intimation.getInsured() != null ? intimation.getInsured().getInsuredName() : ""));
			jsonObject.put("policyFromDate", (intimation.getPolicy() != null ? SHAUtils.formatDate(intimation.getPolicy().getPolicyFromDate()) : ""));
			jsonObject.put("policyToDate", (intimation.getPolicy() != null ? SHAUtils.formatDate(intimation.getPolicy().getPolicyToDate()) : ""));
			jsonObject.put("sumInsured", (intimation.getInsured() != null ? (intimation.getInsured().getInsuredSumInsured() != null ? intimation.getInsured().getInsuredSumInsured() :"") : "" ));
			jsonObject.put("relationship", (intimation.getInsured() != null ? (intimation.getInsured().getRelationshipwithInsuredId() != null ? intimation.getInsured().getRelationshipwithInsuredId().getValue() :"") : "" ));
			jsonObject.put("age", (intimation.getInsured() != null ? intimation.getInsured().getInsuredAge() : ""));
			StringBuffer riskPED = null;
			StringBuffer portalPED = null;
			List<InsuredPedDetails> insuredKeyListByInsuredkey = insuredService.getInsuredKeyListByInsuredkey(intimation.getInsured().getInsuredId());
			if(insuredKeyListByInsuredkey != null){
				riskPED = new StringBuffer();
				portalPED = new StringBuffer();
				for (InsuredPedDetails insuredPedDetails : insuredKeyListByInsuredkey) {
					if(insuredPedDetails.getPedCode() != null){
						riskPED.append(insuredPedDetails.getPedDescription()).append(", ");
					}
					if(insuredPedDetails.getPedDescription() != null){
						portalPED.append(insuredPedDetails.getPedDescription()).append(", ");
					}
				}
			}
			jsonObject.put("riskPED", (riskPED != null ? riskPED.toString() : ""));
			jsonObject.put("portalPED", (portalPED != null ? portalPED.toString() : ""));
		}
		return jsonObject;
	}
	
	private JSONObject getPreviousInsuranceDetails(Intimation intimation) {
		logger.debug("Enter CRCPortalServlet.getPreviousInsuranceDetails() Method");
		JSONObject jsonObject = null;
		if (intimation != null && intimation.getPolicy() != null) {
			Policy policy = policyService.getPolicy(intimation.getPolicy().getPolicyNumber());
			if (policy != null) {
				BeanItemContainer<PreviousPolicy> previousPolicyList= previousPolicyService.getPreviousPolicyDetails(policy);						
				if (previousPolicyList != null && previousPolicyList.size() > 0) {
					List<PreviousPolicyDTO> previousPolicies = new ArrayList<PreviousPolicyDTO>();
					PreviousPolicyDTO previousPolicyDto = null;
					for (PreviousPolicy previousPolicy : previousPolicyList.getItemIds()) {
						previousPolicyDto = new PreviousPolicyDTO(previousPolicy);
						//Bancs changes - set member for GMC Policy
						Long insuredKey = intimation.getInsured().getKey();
						Insured	insured = intimationService.getInsuredByKey(insuredKey);
							
						String strDmsViewURL = null;
						Policy policyObj = null;
						
						if (previousPolicy.getPolicyNumber() != null) {
							policyObj = policyService.getByPolicyNumber(previousPolicy.getPolicyNumber());
							if (policyObj != null) {
								if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
									strDmsViewURL = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
									strDmsViewURL = strDmsViewURL.replace("POLICY", previousPolicy.getPolicyNumber());
									if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
										strDmsViewURL = strDmsViewURL.replace("MEMBER", insured!=null?String.valueOf(insured.getSourceRiskId()!=null?insured.getSourceRiskId():""):"");		
									}else{
										strDmsViewURL = strDmsViewURL.replace("MEMBER", "");
									}
									previousPolicyDto.setPolicyScheduleUrl(strDmsViewURL);
								}else{
									strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
									String dmsToken = intimationService.createDMSToken(previousPolicy.getPolicyNumber());
									previousPolicyDto.setPolicyScheduleUrl(strDmsViewURL+dmsToken);
								}
							}
						}
						List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuredDetails = claimStatusService.getPreviousInsuredDetails(previousPolicy);
						if (previousInsuredDetails != null) {
							for(PreviousInsuranceInsuredDetailsTableDTO insuredDetails : previousInsuredDetails) {
								insuredDetails.setDateOfBirth((insuredDetails.getDOB() != null ? SHAUtils.formatDateTime(insuredDetails.getDOB()) : ""));
							}
							previousPolicyDto.setPreviousInsuredDetails(previousInsuredDetails);
						}
						previousPolicies.add(previousPolicyDto);
					}
					jsonObject = new JSONObject();
					jsonObject.put("prevInsuranceDetails", previousPolicies);
				}				
			}
		}
		return jsonObject;		
	}
	
	private JSONObject getBalanceSumInsuredDetails(Intimation intimation, Claim claim) {
		logger.debug("Enter CRCPortalServlet.getBalanceSumInsuredDetails() Method");	
		JSONObject jsonObject = null;
		if (intimation != null) {	
			if (intimation.getProcessClaimType() != null && intimation.getProcessClaimType().equalsIgnoreCase("P")) {
				jsonObject = claimStatusService.getPASumInsuredDetails(intimation);
			} else {
				jsonObject = claimStatusService.getSumInsuredDetails(intimation, claim);				
			}
		}
		return jsonObject;		
	}
	
	private JSONObject getFvrDetails(Intimation intimation) {
		logger.debug("Enter CRCPortalServlet.getFvrDetails() Method");
		JSONObject jsonObject = null;
		if (intimation != null) {			 
			 List<ViewFVRDTO> fvrDTOList = null;
			 DMSDocumentDTO dmsDocumentList = null;
			 Long intimationKey =  intimation.getKey();
			 fvrDTOList = viewFVRService.searchFVR(intimationKey);
			 List<FieldVisitRequest> fvrIntimationList = null;
			 
			 for (ViewFVRDTO fvrDTO : fvrDTOList){
				 // Logic to find FVR sequence no
				 List<FieldVisitRequest> fieldVisitRequestList = fieldVisitRequestService.getFieldVisitRequestByKey(fvrDTO.getKey());
				 if (!fieldVisitRequestList.isEmpty()) {
					 fvrIntimationList = fieldVisitRequestService.getFieldVisitRequestByIntimationKey(fieldVisitRequestList.get(0).getIntimation().getKey());
					 if(fvrIntimationList != null) {
						 for (int index = 0; index < fvrIntimationList.size(); index++) {
							if (fvrDTO.getKey().equals(fvrIntimationList.get(index).getKey())) {
								fvrDTO.setSerialNumber(index + 1);
							}
						 }
					 }
				 }
				 
				 List<FVRGradingDetail> fvrGradingDetailsList = fieldVisitRequestService.getFvrGradingDetailsByFvrKey(fvrDTO.getKey());
				 if (!fvrGradingDetailsList.isEmpty() && fvrGradingDetailsList != null) {
					 List<NewFVRGradingDTO> fvrGradingDtoList = viewFVRMapper.getFvrGraddingDetailsNew(fvrGradingDetailsList);
						if(fvrGradingDtoList != null && !fvrGradingDtoList.isEmpty()){
							fvrDTO.setNewFvrGradingDTO(fvrGradingDtoList);
							
						}
				 }	 				 
				 
				 List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService.getDocumentDetailsData(intimation.getIntimationId(), 0);
				 if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
					 dmsDocumentList = new DMSDocumentDTO();
					 dmsDocumentList.setIntimationNo(intimation.getIntimationId());
					 Claim claim = claimService.getClaimsByIntimationNumber(intimation.getIntimationId());
					 dmsDocumentList.setClaimNo(claim.getClaimId());
					 for (DMSDocumentDetailsDTO docDTO : dmsDocumentDetailsDTO){
						 docDTO.setFileViewURL(claimStatusService.getDocumentFileURL(docDTO.getFileName(), docDTO.getDmsDocToken()));
					 }
					 dmsDocumentList.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
					 fvrDTO.setDmsDocumentList(dmsDocumentList);
				 }
				 fvrDTO.setStatus(fvrDTO.getfVRStatus());
				 fvrDTO.setFvrTat(fvrDTO.getfVRTAT() != 0L ? String.valueOf(fvrDTO.getfVRTAT()) : "0" );
				 fvrDTO.setFvrReceivedDate(fvrDTO.getfVRreceivedDate());
			 }
			 jsonObject = new JSONObject();
			 jsonObject.put("fvrDetails", fvrDTOList);
			 
		 }
		logger.debug("CRCPortalServlet.getFvrDetails() Method Completed" + jsonObject);
		return jsonObject;
	}

	private JSONObject getPreAuthDetails(Intimation intimation, String preIntimationKey) {
		logger.debug("Enter CRCPortalServlet.getPreAuthDetails() Method");
		JSONObject jsonObject = null;
		if (intimation != null && preIntimationKey != null) {
			List<Preauth> preAuthList = preauthService.getPreauthByIntimationKey(intimation.getKey());
			if (preAuthList != null ) {
				Map<Long,String> preAuthIntimationKeys = new TreeMap<Long,String>();
				for (Preauth preAuth : preAuthList) {
					if (!preAuth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE)) {
						preAuthIntimationKeys.put(preAuth.getKey(), preAuth.getPreauthId());
					}
				}
				jsonObject = new JSONObject();
				jsonObject.put("preAuthIntimationKeys", preAuthIntimationKeys);
				PreauthDetailDTO preauthDetailDto = null;
				if (preAuthIntimationKeys.size() > 0 && preIntimationKey.equals("0")) {
					Map.Entry<Long,String> entry = preAuthIntimationKeys.entrySet().iterator().next();
					preauthDetailDto = claimStatusService.getPreAuthDetails(entry.getKey());					
				} else {
					preauthDetailDto = claimStatusService.getPreAuthDetails(Long.parseLong(preIntimationKey));
				}
				jsonObject.put("preAuthDetails", preauthDetailDto);
			}
		}
		return jsonObject;
	}
	
	public JSONObject getDoctorNotes(Intimation intimation) {
		logger.debug("Enter CRCPortalServlet.getDoctorNotes() Method");
		JSONObject jsonObject = null;
		if (intimation != null ) {			
			List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
			if (claimByIntimation != null && !claimByIntimation.isEmpty()) {
				List<ViewDoctorRemarksDTO> doctorRemarks = preauthService.getDoctorInternalNotesHistoryDetails(claimByIntimation.get(0).getKey());
				jsonObject = new JSONObject();
				jsonObject.put("doctorNotes", doctorRemarks);
			}
		 }
		return jsonObject;
	}
	
	private JSONObject getPedRequestDetails(String intimationNo) {
		logger.debug("Enter CRCPortalServlet.getPedRequestDetails() Method");
		JSONObject jsonObject = null;
		if (intimationNo != null) {
			 Intimation intimation = intimationService.getIntimationByNo(intimationNo);
			 	if(intimation != null) {
					jsonObject = new JSONObject();
					List<OldPedEndorsementDTO> oldPedEndorsementDTOList = pedRequestService.search(intimation.getKey());
					if (oldPedEndorsementDTOList != null) {
						List<OldPedEndorsementDetailDTO> oldPedEndorsementDetailDTO = new ArrayList<OldPedEndorsementDetailDTO>();
						for (OldPedEndorsementDTO pedEndorsement : oldPedEndorsementDTOList) {
							OldPedEndorsementDetailDTO oldPedDto = new OldPedEndorsementDetailDTO(pedEndorsement);
							List<PedHistoryDTO> pedHistoryList = claimStatusService.getPedHistoryDetails(pedEndorsement.getKey());
							oldPedDto.setPedHistoryList(pedHistoryList);
							
							//PED Request Processed - Query Replied Details
							List<PedQuery> pedQueryDetailsList = pedQueryService.getPedQueryDetailsList(pedEndorsement.getKey());
							if (pedQueryDetailsList != null && pedQueryDetailsList.size() > 0) {
								oldPedDto.setHasPedQueryDetails(Boolean.TRUE);
								for (PedQuery pedQuery : pedQueryDetailsList) {
									oldPedDto.setPedQueryStatus(pedQuery.getStatus() != null ? pedQuery.getStatus().getProcessValue() : "");
									oldPedDto.setReplyRemarks(pedQuery.getReplyRemarks());
								}
							} 
							
							//PED Request Processed - Escalation Details
							OldInitiatePedEndorsement initiate = pedQueryService.findByKey(pedEndorsement.getKey());
							if (initiate.getEscalateRemarks() != null && ! initiate.getEscalateRemarks().isEmpty()) {
								oldPedDto.setHasPedEsclateDetails(Boolean.TRUE);
								oldPedDto.setPedEsclateRemarks(initiate.getEscalateRemarks());
							}
								 
							//PED Request Processed - Requestor Details
							oldPedDto.setPedDiscussedWith(initiate.getUwDiscussWith() != null ? initiate.getUwDiscussWith().getValue() : "");
							oldPedDto.setPedSuggestion(initiate.getUwSuggestion() != null ? initiate.getUwSuggestion() : "");
							if (initiate.getModifiedBy() != null){
								oldPedDto.setPedRequestorId(initiate.getModifiedBy() != null ? initiate.getModifiedBy() : "");
							} else {
								oldPedDto.setPedRequestorId(initiate.getCreatedBy() != null ? initiate.getCreatedBy() : "");
							}
								 
							if (initiate.getCreatedDate() != null) {
								oldPedDto.setPedRequestorDate(initiate.getCreatedDate() != null ? SHAUtils.formatDate(initiate.getCreatedDate()) : "");
							}
								 
							//PED Request Processed - Specialist Advise Details
							List<PedSpecialist> specialistTypeList = pedQueryService.getSpecialistDetailsListByKey(initiate.getKey());
							if (specialistTypeList != null && specialistTypeList.size() > 0) {
								oldPedDto.setHasPedSpecialistDetails(Boolean.TRUE);
								for (PedSpecialist pedSpecialist : specialistTypeList) {
									oldPedDto.setPedSpecialistStatus( pedSpecialist.getStatus() != null ?  pedSpecialist.getStatus().getProcessValue() : "");
									oldPedDto.setPedSpecialistRemarks(pedSpecialist.getSpecialistRemarks());
								}
							} 
								 
							List<PedEndorementHistory> pedEndorsementHistoryList = pedQueryService.getPedEndorsementHistory(initiate.getKey());
							if (pedEndorsementHistoryList != null) {
								//PED Request Ammended Details
								for (PedEndorementHistory pedEndorsementHistory : pedEndorsementHistoryList) {
									if (pedEndorsementHistory.getProcessType() != null && pedEndorsementHistory.getProcessType().equalsIgnoreCase("I")
											&& pedEndorsementHistory.getStatus() != null && ! pedEndorsementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_PROCESSOR)
											&& ! pedEndorsementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_APPROVER)) {
											OldPedEndorsementDTO ammendedEndorsement = claimStatusService.setPedEndorsementByHistory(pedEndorsementHistory);
												 if(ammendedEndorsement != null) {
													 oldPedDto.setPedAmmended(new OldPedEndorsementDetailDTO(ammendedEndorsement));										 
												 }
										  }
									  }
								
								//PED Request Reviewed Details
								for (PedEndorementHistory pedEndorsementHistory : pedEndorsementHistoryList) {
									if (pedEndorsementHistory.getProcessType() != null && pedEndorsementHistory.getProcessType().equalsIgnoreCase("I")
											&& pedEndorsementHistory.getStatus() != null && pedEndorsementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_PROCESSOR)
											&& pedEndorsementHistory.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_RELEASE_APPROVER)) {
										OldPedEndorsementDTO reviewEndorsement = claimStatusService.setPedEndorsementByHistory(pedEndorsementHistory);
										if (reviewEndorsement != null) {
											oldPedDto.setPedReviewed(new OldPedEndorsementDetailDTO(reviewEndorsement));
										}
									}
								}
								
								//PED Request Processed & Approved Details
								for (PedEndorementHistory pedEndorsementHistory : pedEndorsementHistoryList) {
									if (pedEndorsementHistory.getProcessType() != null && pedEndorsementHistory.getProcessType().equalsIgnoreCase("P")) {
										OldPedEndorsementDTO processEndorsement = claimStatusService.setPedEndorsementByHistory(pedEndorsementHistory);
										if (processEndorsement != null) {
											oldPedDto.setPedProcessed(new OldPedEndorsementDetailDTO(processEndorsement));
										}
									} else if (pedEndorsementHistory.getProcessType() != null && pedEndorsementHistory.getProcessType().equalsIgnoreCase("A")) {
										OldPedEndorsementDTO approverEndorsement = claimStatusService.setPedEndorsementByHistory(pedEndorsementHistory);
										if (approverEndorsement != null) {
											oldPedDto.setPedApproved(new OldPedEndorsementDetailDTO(approverEndorsement));
										}
									}
								}
							}
							
							if (pedEndorsementService.getIntitiatePedEndorsementDetails(pedEndorsement.getKey()) != null) {
								List<ViewPEDEndoresementDetailsDTO> pedEndorsementDetailList = pedEndorsementService.search(pedEndorsement.getKey());
								if (pedEndorsementDetailList != null) {
									oldPedDto.setViewPEDEndoresementDetailsDTO(pedEndorsementDetailList);
								}
							}
							oldPedEndorsementDetailDTO.add(oldPedDto);					
						}
						jsonObject.put("pedRequestDetails", oldPedEndorsementDetailDTO);
				}
			}
		}
		return jsonObject;
	}
	

	private JSONObject getClaimHistoryDetails(String intimationNo) {
		logger.debug("Enter CRCPortalServlet.getClaimHistoryDetails() Method");
		JSONObject jsonObject = null;
		if (intimationNo != null) {
			 Intimation intimation = intimationService.getIntimationByNo(intimationNo);
			 	if (intimation != null) {
			 		List<ViewClaimHistoryDTO> trailsList = 	claimStatusService.getClaimHistoryDetails(intimation);
			 		jsonObject = new JSONObject();
			 		jsonObject.put("claimHistory", trailsList);
			 	}
		}
		return jsonObject;
	}
	
	private JSONObject getIntimationDetails(Intimation intimation) {
		logger.debug("Enter CRCPortalServlet.getIntimationDetails() Method");
		JSONObject jsonObject = null;
		if (intimation != null) {
			jsonObject = new JSONObject();
			EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ViewClaimStatusDTO intimationDetails = instance.getViewClaimStatusDto(intimation);			
			Hospitals hospital = hospitalService.getHospitalDetailsByKey(intimation.getHospital());
			ViewClaimStatusDTO claimStatusDto = claimStatusService.getHospitalDetails(intimationDetails, hospital);
	 		jsonObject.put("intimation", new IntimationDetailDTO(intimation, hospital, claimStatusDto));
	 		EarlierRodMapper.invalidate(instance);
		}
		return jsonObject;
	}
	
	private JSONObject getHospitalDetails(Intimation intimation) {
		logger.debug("Enter CRCPortalServlet.getHospitalDetails() Method");
		JSONObject jsonObject = null;
		if (intimation != null) {			
			Hospitals hospital = hospitalService.getHospitalDetailsByKey(intimation.getHospital());
			if (hospital != null) {
				jsonObject = new JSONObject();
				jsonObject.put("hospName", hospital.getName());
				jsonObject.put("hospCode", hospital.getHospitalCode());
				jsonObject.put("hospPhoneNo", hospital.getPhoneNumber());
				jsonObject.put("authRep", hospital.getAuthorizedRepresentative());
				jsonObject.put("city", hospital.getCity());
				jsonObject.put("repName", hospital.getRepresentativeName());
				jsonObject.put("state", hospital.getState());
				jsonObject.put("hospCategory", hospital.getHospitalCategory());
				jsonObject.put("pincode", hospital.getPincode());
				jsonObject.put("roomCategory", intimation.getRoomCategory() != null ? intimation.getRoomCategory().getValue() : "");
				jsonObject.put("remarks", hospital.getRemark());
				jsonObject.put("address", hospital.getAddress());
				if (hospital.getHospitalCode() != null) {
					BPMClientContext bpmClientContext = new BPMClientContext();
					String packageUrl = bpmClientContext.getHospitalPackageDetails() + hospital.getHospitalCode();
					jsonObject.put("packageUrl", packageUrl);
				}
			}
		}		
		return jsonObject;	
	}
	
	private JSONObject getNegotiationDetails(String intimationNo) {
		logger.debug("Enter CRCPortalServlet.getNegotiationDetails() Method");
		JSONObject jsonObject = null;
		if (intimationNo != null) {
			 Intimation intimation = intimationService.getIntimationByNo(intimationNo);
			 	if (intimation != null) {
			 		List<NegotiationAmountDetailsDTO> trailsList = 	preauthService.getNegotiationAmountDetailsByIntimationKey(intimation.getKey());
			 		jsonObject = new JSONObject();
			 		jsonObject.put("negotiationDetails", trailsList);
			 	}
		}
		return jsonObject;
	}
	
}
