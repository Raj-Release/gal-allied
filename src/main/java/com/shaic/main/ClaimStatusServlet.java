package com.shaic.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.NewClaimedAmountTable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryDTO;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryService;
import com.shaic.claim.enhacement.table.PreviousPreAuthService;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRMapper;
import com.shaic.claim.fvrdetails.view.ViewFVRService;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsService;
import com.shaic.claim.pedrequest.view.ViewDoctorRemarksDTO;
import com.shaic.claim.pedrequest.view.ViewpedRequestService;
import com.shaic.claim.policy.search.ui.PremPolicySchedule;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.dto.ResidualAmountDTO;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationService;
import com.shaic.claim.preauth.pedvalidation.view.ViewPedValidationTableDTO;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDetailDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.preauth.wizard.pages.AmountConsideredTable;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.procedureexclusioncheck.view.ViewProcedureExclusionCheckDTO;
import com.shaic.claim.procedureexclusioncheck.view.ViewProcedureExclusionCheckService;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotService;
import com.shaic.claim.reimbursement.dto.ReceiptOfDocumentAndMedicalProcess;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuMapper;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuService;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetails;
import com.shaic.claim.reports.negotiationreport.NegotiationAmountDetailsDTO;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionService;
import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionTableDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.FinancialApprovalTableDTO;
import com.shaic.claim.viewEarlierRodDetails.PreauthToTmpPreauthMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPreauthMoreDetailsPresenter;
import com.shaic.claim.viewEarlierRodDetails.Table.RevisedDiagnosisPreauthViewTable;
import com.shaic.claim.viewEarlierRodDetails.dto.HospitalisationDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.shaic.claim.viewEarlierRodDetails.dto.ViewRejectionDTO;
import com.shaic.domain.BillingPreHospitalisation;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.ClaimService;
import com.shaic.domain.CoPayService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasClaimPortal;
import com.shaic.domain.MasCopay;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PostHospitalisation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Product;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.preauth.ClaimAmountDetails;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.preauth.DiagnosisPED;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.ResidualAmount;
import com.shaic.domain.preauth.ViewTmpPreauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.portal.service.ClaimStatusService;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Layout;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;

public class ClaimStatusServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  
  private final Logger logger = LoggerFactory.getLogger(ClaimStatusServlet.class);

  @Inject
  private ClaimService claimService;
  
  @Inject
  private IntimationService intimationService;
  
  @Inject
  private ViewProcedureExclusionCheckService viewProcedureExclusionService;
  
  @Inject
  private ViewPedValidationService viewPedValidationService;
  
  @Inject
  private ViewBillSummaryPage viewBillSummaryPage;
  
  @Inject
  private Instance<NewClaimedAmountTable> claimedAmountDetailsTable;
  
  private NewClaimedAmountTable claimedAmountDetailsTableObj;
  
  @Inject
  private Instance<RevisedDiagnosisPreauthViewTable> diagnosisListenerTableInstance;
	
  private RevisedDiagnosisPreauthViewTable diagnosisListenerTableObj;
  
  @Inject
  private Instance<AmountConsideredTable> amountConsideredTableInstance;

  private AmountConsideredTable amountConsideredTable;
  
  @EJB
  private HospitalService hospitalService;
  
  @EJB
  private DBCalculationService dbCalculationService;
  
  @EJB
  private PaymentProcessCpuService paymentProcessCpuService;
  
  @EJB
  private PolicyService	policyservice;
  
  @EJB
  private PreauthService preauthService;
  
  @EJB
  private PreviousPreAuthService previousPreAuthService;
  
  @EJB
  private DiagnosisService diagnosisService;
  
  @EJB
  private CreateAndSearchLotService searchService;
  
  @EJB
  private AcknowledgementDocumentsReceivedService acknowledgementDocumentsReceivedService;
  
  @EJB
  private ReimbursementService reimbursementService;
  
  @EJB
  private ReimbursementQueryService reimbursementQueryService;
  
  @EJB
  private MasterService masterService;
  
  @EJB
  private ViewClaimHistoryService viewClaimHistoryService;
  
  @EJB
  private ReimbursementRejectionService reimbursementRejectionService;
  
  @EJB
  private FieldVisitRequestService fieldVisitRequestService;
  
  @EJB
  private CreateRODService billDetailsService;
  
  @EJB
	private ViewFVRService viewFVRService;
  
  @EJB
	private CoPayService coPayService;
  
  @EJB
  private ViewpedRequestService pedRequestService;
  
  @EJB
  private PEDQueryService pedQueryService;
  
  @EJB
  private ClaimStatusService claimStatusService;
  
  @EJB
  private ViewPEDEndoresementDetailsService pedEndorsementService;
  
  @EJB
  private PolicyService policyService;

  @EJB
  private ViewSpecialistOpinionService viewSpecialistOpinionService;

  @EJB
  private ViewpedRequestService viewPedRequest;
  
  @PersistenceContext
  protected EntityManager entityManager;
  
  private PreauthDTO bean;
  
  private String diagnosisName;
  
  private Map<String, Object> sublimitCalculatedValues;
  
  private BeanItemContainer<ExclusionDetails> exlusionContainer;
  
  @Inject
  private ViewFVRMapper viewFVRMapper;
  
  private Long reimkey = null;
  
  private String rodNUmber;
  
  private String claimNumber = null;
  
  private final String USER_AGENT = "Mozilla/5.0";
  
  protected void doGet(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException 
  {
	  String queryString = request.getQueryString();
	  
	  rodNUmber = null;
	  
	  String error = "";
	  if(queryString != null && queryString.contains("token")){
	  JSONObject jSONObject = null;
	  jSONObject =getToken(request.getParameter("token"));
	  if(jSONObject != null ){
	  String source = (String) jSONObject.get(SHAConstants.JSON_APPLICATION_NAME);
	  claimNumber = (String) jSONObject.get(SHAConstants.JSON_CLAIM_NUMBER);
	/*  claimNumber="CLI/2018/121116/0219499";
	  source="HOSPITAL";*/
	  //String  claimNo = claimNumber.replaceAll("\\", "");
	  String sourceName = null;
	  if(source != null && !source.isEmpty()){
	  MasClaimPortal masClaimPortal = preauthService.getPortalViewByApplicationName(source);
	  
	  if(masClaimPortal != null){
		  sourceName =   masClaimPortal.getSourceName();
	  }
	  else
	  {
		  request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
	  }
	  }
	  else
	  {
		  request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
	  }
	  if(source != null && (source.equalsIgnoreCase(SHAConstants.UNDERWRITING)|| source.equalsIgnoreCase(SHAConstants.CMU_PORTAL)) ){
		  List<PreviousClaimsTableDTO> intimationDtls = new ArrayList<PreviousClaimsTableDTO>();
		  if(jSONObject != null){
			  String policyNo = (String)jSONObject.get("policyNumber");
			  String healthCardNo = (String)jSONObject.get("healthIdCard");
			  String insurdKey = (String)jSONObject.get("mainMemberId");
			  intimationDtls = dbCalculationService.getBancsIntimationDetails(policyNo, healthCardNo, insurdKey);
			  for (PreviousClaimsTableDTO previousClaimsTableDTO : intimationDtls) {
				  if(previousClaimsTableDTO.getIntimationNumber() != null){
					  
					  if(previousClaimsTableDTO.getSubLimitName() != null){
						  String diagnosisNames = previousClaimsTableDTO.getSubLimitName().replace(",", "");
						  if(diagnosisNames != null && diagnosisNames.isEmpty()){
							  previousClaimsTableDTO.setSubLimitName("");
						  }
					  }
					  
					  String token = null;
					  try {
						  token = preauthService.createJWTToken(previousClaimsTableDTO.getIntimationNumber());
					  } catch (NoSuchAlgorithmException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  } catch (ParseException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
					  }
					  if(token != null){
						  previousClaimsTableDTO.setTokenString(token);
					  }

				  }
			  }
		  }
		  request.setAttribute("intimationDtlsList",intimationDtls);
		  request.getRequestDispatcher("/WEB-INF/BancsClaimDetailsPage.jsp").include(request, response);
	  }
	  else if(null != claimNumber && !claimNumber.isEmpty()){	  

		  if(sourceName != null && sourceName.equalsIgnoreCase(SHAConstants.CLAIM_STATUS_PAGE)){
			  showClaimPage(claimNumber,request, response);
		  }
		  else if(sourceName != null && sourceName.equalsIgnoreCase(SHAConstants.CLAIM_AGENT_PAGE)){
			  showAgentPage(claimNumber,request, response);
		  }
		  else if(sourceName != null && sourceName.equalsIgnoreCase(SHAConstants.CLAIM_HOSPITAL_PAGE)){
			  showHospitalPage(claimNumber,request, response);
		  }
		  else if(sourceName != null && sourceName.equalsIgnoreCase(SHAConstants.CLAIM_AGGREGATOR_PAGE)){
			  showAggregatorPage(claimNumber,request, response);
		  }
		  else if(sourceName != null && sourceName.equalsIgnoreCase(SHAConstants.CLAIM_CORPORATE_PAGE)){
			  showCorporatePage(claimNumber,request, response);
		  }
		  else if(sourceName != null && sourceName.equalsIgnoreCase(SHAConstants.CLAIM_CRC_PAGE)){
			  showCRCPage(claimNumber,request, response);
		  }
		  else if(sourceName != null && sourceName.equalsIgnoreCase(SHAConstants.CLAIM_RETAIL_PAGE)){
			  showWebsitePortalPage(claimNumber,request, response);
		  }
	  }
	  else
	  {
		  request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
	  }
	 /* else if(queryString != null && (!(queryString.contains("View1")) && !(queryString.contains("View2")) 
			  && !(queryString.contains("View3")) && !(queryString.contains("View4")))) {
		  String[] split = queryString.split("=");
		   
		   rodNUmber = request.getParameter("rod_num");
		  if(request.getQueryString().contains("&")) {
		  String[] no = split[1].split("&");
		  intimationNumber = no[0];
		  }else {
			  intimationNumber = split[1];
		  }
		  
		  
		  
		  showPage(intimationNumber,request, response);
		  
	  }*/
	  }  else
	  {
		  request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
	  }
	  }  else if (queryString.contains("preAuthIntimationKey")) {
		  PreauthDetailDTO preauthDetails = getPreAuthDetails(Long.parseLong(request.getParameter("preAuthIntimationKey")));
		  if(preauthDetails != null) {
			  response.setContentType("application/json");   
			  ObjectMapper mapper = new ObjectMapper();
			  response.setCharacterEncoding("UTF-8");
			  response.getWriter().write(mapper.writeValueAsString(preauthDetails));
		  }
		  
	  }  else if(queryString.contains("int_num") || queryString.contains("reimb_num") || queryString.contains("cashless_num") || queryString.contains("bill_details") ||
			  queryString.contains("bill_summary") || queryString.contains("intim_num") || queryString.contains("key") ||
			  queryString.contains("reim_query")  || queryString.contains("rejection_intimation_numbr") || queryString.contains("intimation_numb")){
		  rodNUmber = request.getParameter("rod_num");
		  if(queryString.contains("intim_num")){
			  claimNumber = request.getParameter("intim_num");
		  }else if(queryString.contains("int_num")){
			  claimNumber = request.getParameter("int_num");
		  }else if(queryString.contains("rejection_intimation_numbr")){
			  claimNumber = request.getParameter("rejection_intimation_numbr");
		  }
		  else if(queryString.contains("intimation_numb")){
			  claimNumber = request.getParameter("intimation_numb");
		  }

		 showPage(claimNumber, request, response); 
	  }
	  else if(queryString.contains("underwrinting_view")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("underwrinting_view"));
		  claimNumber = (String) jSONObject.get("intimationNO");
		  showClaimPage(claimNumber,request, response);
	  } else if(queryString.contains("doctor_remarks")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("doctor_remarks"));
		  claimNumber = (String) jSONObject.get("intimationNO");
		  ViewTmpIntimation viewTmpIntimation = intimationService.searchbyIntimationNoFromViewIntimation(claimNumber);
		  List<ViewDoctorRemarksDTO> docRemarks = new ArrayList<ViewDoctorRemarksDTO>();
		  List<ViewTmpClaim> claimByIntimation = claimService.getTmpClaimByIntimation(viewTmpIntimation.getKey());
		  if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			  docRemarks = preauthService.getDoctorInternalNotesHistoryDetails(claimByIntimation.get(0).getKey());
		  }
		  request.setAttribute("docRemarks",docRemarks);
		  request.getRequestDispatcher("/WEB-INF/BancsDoctorInternalNote.jsp").include(request, response);
	  }
	  else if(queryString.contains("specialist_details")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("specialist_details"));
		  claimNumber = (String) jSONObject.get("intimationNO");
		  ViewTmpIntimation viewTmpIntimation = intimationService.searchbyIntimationNoFromViewIntimation(claimNumber);
		  ViewTmpClaim claim = claimService.getTmpClaimforIntimation(viewTmpIntimation.getKey());
		  List<ViewSpecialistOpinionTableDTO> specialistList = new ArrayList<ViewSpecialistOpinionTableDTO>();
		  if(claim != null) {
			  if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
				  if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
						  && ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
					  specialistList = viewSpecialistOpinionService.search(claim.getIntimation().getKey());
				  }
			  }
		  }
		  request.setAttribute("specialistList",specialistList);
		  request.getRequestDispatcher("/WEB-INF/ViewSpeacialistTrails.jsp").include(request, response);
	  }
	  else if(queryString.contains("ped_details")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("ped_details"));
		  claimNumber = (String) jSONObject.get("intimationNO");
		  ViewTmpIntimation viewTmpIntimation = intimationService.searchbyIntimationNoFromViewIntimation(claimNumber);
		  ViewTmpClaim claim = claimService.getTmpClaimforIntimation(viewTmpIntimation.getKey());
		  List<OldPedEndorsementDTO> pedList  = new ArrayList<OldPedEndorsementDTO>();
		  if(claim != null){
			  pedList = viewPedRequest.search(claim.getIntimation().getKey());
		  }
		  request.setAttribute("pedList",pedList);
		  request.getRequestDispatcher("/WEB-INF/ViewPedDetails.jsp").include(request, response);
	  }
	  else if(queryString.contains("acknowledgementDetails")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("acknowledgementDetails"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String acknowledgementNo = (String) jSONObject.get("acknowledgementNo");
		  showAcknowledgementDetails(intimationNumber,acknowledgementNo,request,response);
		  request.getRequestDispatcher("/WEB-INF/ViewAcknowledgeDetailDesc.jsp").include(request, response);
		  
		  
	  }
	  else if(queryString.contains("acknowledgement")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("acknowledgement"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String reimbursementKey = (String) jSONObject.get("reimbursementkey");
		  Long rodKey= Long.valueOf(reimbursementKey);
		  List<ViewDocumentDetailsDTO> acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(rodKey);
		  for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : acknowledgeDocsList) {
			
			  Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("acknowledgementNo", viewDocumentDetailsDTO.getAcknowledgeNumber());
				 String ackNoToken = null;
				  try {
					  ackNoToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  viewDocumentDetailsDTO.setAcknowledgementNoToken(ackNoToken);
		}
		  request.setAttribute("docAcknList", acknowledgeDocsList);
		  request.setAttribute("docAckNo", intimationNumber); ;
//		  request.setAttribute("reimbursementList", docAcknowledgementList);
		  request.getRequestDispatcher("/WEB-INF/ViewAcknowledgementDetails.jsp").include(request, response);
	  }
	  else if(queryString.contains("rejection")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("rejection"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String reimbursementKey = (String) jSONObject.get("reimbursementkey");
		  Long rodKey= Long.valueOf(reimbursementKey);
		  Intimation intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  Hospitals hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
		  String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(rodKey);
		  Policy policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());

		  ViewRejectionDTO rejectioDto = new ViewRejectionDTO();
		  if(reimbursementKey != null) {
			 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(rodKey);
			 if(rejectionKeyByReimbursement != null) {
				rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
				rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
				String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
				rejectioDto.setAdmissionDate(admisionDate);
				String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
				rejectioDto.setRejectedDate(rejectDate);
				String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
				rejectioDto.setApproveRejectionDate(approverejectDate);
				rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
				rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
				rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
				
			 	}
			 }
		  String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
		  rejectioDto.setAdmissionDate(admisionDate);
		 
		  request.setAttribute("intimation", intimationByNo);
		  request.setAttribute("daignosisName", diagnosisName);
		  request.setAttribute("rejectList",rejectioDto);
		  request.setAttribute("policyList",policy);
		  request.setAttribute("hospitals", hospitals);
		  request.getRequestDispatcher("/WEB-INF/ViewRejectionDetails.jsp").include(request, response);
	  }else if(queryString.contains("queryDetails")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("queryDetails"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String reimbursementKey = (String) jSONObject.get("reimbursementkey");
		  Long rodKey= Long.valueOf(reimbursementKey);
		  Intimation intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  Claim claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
		  Hospitals hospitalDetails = null;
		  if(claimforIntimation != null){
				 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
				 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
			 }
		  String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(rodKey);
		  request.setAttribute("intimation", intimationByNo);
		  request.setAttribute("hospitals", hospitalDetails);
		  request.setAttribute("daignosisName", diagnosisName);
		  showQueryDetails(intimationNumber,reimbursementKey,request,response);
		  request.getRequestDispatcher("/WEB-INF/ViewQueryDetailDesc.jsp").include(request, response);
	  }
	  else if(queryString.contains("query")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("query"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String reimbursementKey = (String) jSONObject.get("reimbursementkey");
		  Long rodKey= Long.valueOf(reimbursementKey);
		  Intimation intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  Claim claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
		  
		  Policy policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
		  
		  List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(rodKey);
			 Hospitals hospitalDetails=null; 
			 
			 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(rodKey);
			 List<ViewQueryDTO> viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
			 //need to implement

			 if(claimforIntimation != null){
				 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
				 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
			 }	
			 
			 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
				 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
				 viewQueryDTO.setQueryDate(rejectDate);
				 viewQueryDTO.setDiagnosis(diagnosisName);
				 viewQueryDTO.setReimbursementKey(rodKey);
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("reimbursementkey", rodKey.toString());
				 String queryDtlsToken = null;
				  try {
					  queryDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  viewQueryDTO.setQueryToken(queryDtlsToken);
				 
				 if(hospitalDetails != null){
					 viewQueryDTO.setHospitalName(hospitalDetails.getName());
					 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
					 viewQueryDTOvalues.add(viewQueryDTO);
				 }
//				viewQueryDTO.setClaim(claimforIntimation);
				 
				 
			 }

			 request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("daignosisName", diagnosisName);
			  request.setAttribute("queryList", viewQueryDTOvalues);
			  request.setAttribute("policyList",policy);
			  request.setAttribute("hospitals", hospitalDetails);
			  request.getRequestDispatcher("/WEB-INF/ViewQueryDetails.jsp").include(request, response);
	  }  
	  else if(queryString.contains("billdetails")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("billdetails"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String reimbursementKey = (String) jSONObject.get("reimbursementkey");
		  Long rodKey= Long.valueOf(reimbursementKey);
		  Intimation intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  Claim claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
		  Hospitals hospitalDetails = null;
		  if(claimforIntimation != null){
				 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
				 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
			 }
		  String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(rodKey);
		  request.setAttribute("intimation", intimationByNo);
		  request.setAttribute("hospitals", hospitalDetails);
		  showBillDetails(intimationNumber,reimbursementKey,request,response);
	  }
	  else if(queryString.contains("billsummary")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("billsummary"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String reimbursementKey = (String) jSONObject.get("reimbursementkey");
		  Long rodKey= Long.valueOf(reimbursementKey);
		  Intimation intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  Claim claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
		  Hospitals hospitalDetails = null;
		  if(claimforIntimation != null){
				 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
				 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
			 }
		  request.setAttribute("intimation", intimationByNo);
		  request.setAttribute("hospitals", hospitalDetails);
		  showBillSummaryDetails(intimationNumber,reimbursementKey,request,response);
	  }
	  else if(queryString.contains("cashlessDoc")){
		  JSONObject jSONObject = null;
		  jSONObject = getToken(request.getParameter("cashlessDoc"));
		  String intimationNumber = (String) jSONObject.get("intimationNo");
		  String preauthKey = (String) jSONObject.get("cashlessKey");
		  Long cashlessKey= Long.valueOf(preauthKey);

		  showCashlessDocPage(intimationNumber,cashlessKey, request, response);
	  }
	  else
	  {
		  request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
	  }
//  }  request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
  }
  
  	private PreauthDetailDTO getPreAuthDetails(Long preauthKey) {
  		PreauthDetailDTO preAuthDetail = null;
  		Integer totalDiagReverseAllocAmt = 0;
  		Integer totalDiagConsAmt = 0;
  		Integer totalDiagPayableAmt = 0;
  		Integer totalDiagCopayAmt = 0;
  		Integer totalDiagNetAmt = 0;
  		if(preauthKey != null) {
  			Preauth preauth = preauthService.getPreauthById(preauthKey);
  			if(preauth != null) {
  				PreauthDTO preauthDTO = getPreauthDTO(preauth);
  				getResidualAmount(preauth, preauthDTO);

  				Double insuredSumInsured = dbCalculationService
  						.getInsuredSumInsured(preauthDTO.getNewIntimationDTO()
  								.getInsuredPatient().getInsuredId().toString(),
  								preauthDTO.getNewIntimationDTO().getPolicy()
  										.getKey(),preauthDTO.getNewIntimationDTO()
  										.getInsuredPatient().getLopFlag());
  				
  				Double balanceSI = 0d;
  				List<Double> copayValue = new ArrayList<Double>();
  				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
  					balanceSI = dbCalculationService.getBalanceSIForGMC(
  							preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
  							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
  							preauthDTO.getClaimKey());
  					copayValue = dbCalculationService.getProductCoPayForGMC(
  							preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
  				}else{
  					balanceSI = dbCalculationService.getBalanceSI(
  							preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
  							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
  							preauthDTO.getClaimKey(), insuredSumInsured,
  							preauthDTO.getNewIntimationDTO().getKey()).get(
  							SHAConstants.TOTAL_BALANCE_SI);
  					 copayValue = dbCalculationService.getProductCoPay(
  							preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
  									.getKey(), preauthDTO.getNewIntimationDTO()
  									.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
  				}
  				preauthDTO.setBalanceSI(balanceSI);
  				preauthDTO.setProductCopay(copayValue);
  				preauthDTO.getPreauthMedicalProcessingDetails().setMedicalRemarks(
  						preauthDTO.getMedicalRemarks());
  				
  				List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues = setValueToMedicalDecisionValues(preauthDTO);
  				
  				claimedAmountDetailsTableObj = claimedAmountDetailsTable.get();
  				claimedAmountDetailsTableObj.initView(preauthDTO,SHAConstants.VIEW_PREAUTH_CLAIM_STATUS);
  				claimedAmountDetailsTableObj.setValues(preauthDTO.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
  				
  				List<NoOfDaysCell> claimedAmtDetailsValues = claimedAmountDetailsTableObj.getValues();
  				preauthDTO.getPreauthDataExtractionDetails().setClaimedDetailsList(claimedAmtDetailsValues);
			 						
  				preAuthDetail = new PreauthDetailDTO(preauth, preauthDTO);
  				preAuthDetail.setDiagnosisTableList(setValueToMedicalDecisionValues);
  				
  				preAuthDetail.setTotalClaimedAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalClaimedAmt());
  				preAuthDetail.setTotalDeductAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalDeductibleAmount());
  				preAuthDetail.setTotalNonPayableAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalDeductableAmount());
  				preAuthDetail.setTotalPayableAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalPayableAmt());
  				preAuthDetail.setTotalNetAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalNetAmount());
  				preAuthDetail.setTotalProductAmtforAmtconsd(claimedAmountDetailsTableObj.getTotalProductAmount());
  		
 				
  				Map<Integer, String> amountConsideredValues = new HashMap<Integer, String>();
  				
  				amountConsideredValues.put(0, "Amount Considered");
  				amountConsideredValues.put(1, "Co-Pay");
  				amountConsideredValues.put(2, "Amount Considered (After Co-Pay)");
  				
  				Map<Integer, String> balanceSumInsuredValues = new HashMap<Integer, String>();
  				balanceSumInsuredValues.put(0, "Balance Sum Insured");
  				balanceSumInsuredValues.put(1, "Co-Pay");
  				balanceSumInsuredValues.put(2, "Balance Sum Insured (After Co-Pay)");
  				
  				amountConsideredTable = amountConsideredTableInstance.get();
  				amountConsideredTable.initView(preauthDTO, true);
  				HorizontalLayout HorizontalLayout = new HorizontalLayout();
  				Layout layout = null;
  				
  				amountConsideredTable.initTable(preauthDTO, HorizontalLayout, HorizontalLayout , layout, true, false);
  				
  				Integer balanceSumInsuredAmt = amountConsideredTable.getBalanceSumInsuredAmt();
  				Integer minimumValue = amountConsideredTable.getMinimumValue();

  				if(preauthDTO.getAmountConsidered() != null){		
  					minimumValue = Integer.valueOf(preauthDTO.getAmountConsidered());		
  				}
  				
  				preAuthDetail.setAmountConsideredValues(amountConsideredValues);
  				preAuthDetail.setMinimumValue(minimumValue);
  				preAuthDetail.setBalanceSumInsuredValues(balanceSumInsuredValues);
  				preAuthDetail.setBalanceSumInsured(balanceSumInsuredAmt);
  				
  				List<ViewPedValidationTableDTO> viewPedValidationTableDTO = viewPedValidationService.search(preauth.getKey());
  				if (viewPedValidationTableDTO != null) {
  					String policyAgeing = null;
  					if(preauthDTO.getNewIntimationDTO() != null){
  						policyAgeing = preauthDTO.getNewIntimationDTO().getPolicyYear();
  					}
  				  				
  					for (ViewPedValidationTableDTO viewPedValidationTableDTO2 : viewPedValidationTableDTO) {
  						viewPedValidationTableDTO2.setPolicyAgeing(policyAgeing);
  					}
  					preAuthDetail.setPedValidationList(viewPedValidationTableDTO);
  				}
  				
  				FieldVisitRequest fieldVisitByPreauthKey = fieldVisitRequestService.getFieldVisitByPreauthKey(preauth.getKey());
  				if(fieldVisitByPreauthKey != null) {
  					preAuthDetail.setAllocationTo((fieldVisitByPreauthKey.getAllocationTo() != null ? fieldVisitByPreauthKey.getAllocationTo().getValue() : "-"));
  					preAuthDetail.setFvrTriggeredPoints(fieldVisitByPreauthKey.getFvrTriggerPoints());
  				}
  				  				
  				if(preauth.getSpecialistConsulted() != null){
  					MastersValue specialistConsulted = masterService.getSpecialistConsulted(preauth.getSpecialistConsulted());
  					preAuthDetail.setSpecialistConsulted(specialistConsulted != null ? specialistConsulted.getValue() : "-");
  				}
  				
 				for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : setValueToMedicalDecisionValues) {
 					Integer amountCons = diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() :0 ;
 					totalDiagConsAmt += amountCons != null ? amountCons : 0;
 					Integer minAmt = diagnosisProcedureTableDTO.getMinimumAmount() != null ? diagnosisProcedureTableDTO.getMinimumAmount() : 0;
 					totalDiagPayableAmt += minAmt != null ? minAmt : 0;
 					Integer diagCopay = diagnosisProcedureTableDTO.getCoPayAmount() != null ? diagnosisProcedureTableDTO.getCoPayAmount() : 0;
 					totalDiagCopayAmt += diagCopay != null ? diagCopay : 0;
 					Integer netAmt = diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount() : 0;
 					totalDiagNetAmt += netAmt != null ? netAmt : 0;
 					Integer reverseAllocAmt = diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ? diagnosisProcedureTableDTO.getReverseAllocatedAmt() : 0;
 					totalDiagReverseAllocAmt += reverseAllocAmt != null ? reverseAllocAmt :0;
  				}
 				preAuthDetail.setTotalDiagReverseAllocAmt(totalDiagReverseAllocAmt);
 				preAuthDetail.setTotalDiagAllocAmt(totalDiagConsAmt);
 				preAuthDetail.setTotalDiagPayableAmt(totalDiagPayableAmt);
 				preAuthDetail.setTotalDiagCopayAmt(totalDiagCopayAmt);
 				preAuthDetail.setTotalDiagNetAmt(totalDiagNetAmt);
  				
  			}
		 }
  		return preAuthDetail;
  	}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	  String intimationNumber =  (String) request.getParameter("intimationNumber");
	  String healthCardNo = (String) request.getParameter("idCardNumber");
	  String rodNumber = (String) request.getParameter("rodNumber");
	  	showPage(intimationNumber, request, response);
	  }

private void showPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String daignosisName = "";
	  String error = "";
	  Intimation intimationByNo = null;
	  Intimation intimationByHCN = null;
	  Preauth preauth = null;
	  Reimbursement remibNo = null;
	  Policy policy = null;
	  Hospitals hospitals = null;
	  ViewRejectionDTO rejectioDto = null;
	  List<ViewQueryDTO> viewQueryDTOvalues = null;
	  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
	  
//	  doPostPremeia(request, response);
	 
		  
	  
	  Claim claimforIntimation = null;
	  
	  if(intimationNumber != null && !intimationNumber.isEmpty()){
		 
//		  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
		 
		 /* if(null == intimationByNo && intimationNumber == null){
			  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
		  	}*/
		  
		 
		  if(error.isEmpty()){
			  if(intimationByNo != null) {
			  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
			  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
			  if(claimforIntimation != null) {
				  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
							.getKey());
					if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
						preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
					}
			    }
			  }
			  
		  }
		  
	 
	
	  if(null != preauth){
		  
		 
		  
		  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
	  
		  int i=1;
		  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
			  if(diagnosis.size() == i){
				  daignosisName += pedDetailsTableDTO.getDiagnosisName();
			  } else {
				  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
			  }

			  i++;
		  }
		  request.setAttribute("daignosisName", daignosisName);
		  request.setAttribute("preauth", preauth);
	  }
	  
	  if(null != claimforIntimation){
		  
		  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
					.searchFromTmpPreauth(claimforIntimation.getKey());
		  
			List<Long> keys = new ArrayList<Long>();
			if(null != previousPreauthList && !previousPreauthList.isEmpty()){
				for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
					keys.add(previousPreAuthTableDTO.getKey());
				}
			}
		  
		  ViewTmpPreauth prevpreauth = null;
			Long preauthKey = null;
			Double reqAmount=new Double(0);
			Double approvedAmount=new Double(0);
			if (!keys.isEmpty()) {
				preauthKey = Collections.max(keys);

				prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
			}
			
		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {

			previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
			reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
			approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
			newList.add(previousPreAuthTableDTO);

		}
		
		request.setAttribute("cashless", newList);
		if(previousPreauthList != null) {
		request.setAttribute("requestedAmt", reqAmount);
		request.setAttribute("totalApprovedAmount", approvedAmount);
		}
	  }
	  
	  if(rodNUmber != null) {
		  	 Long reimbusementKey = Long.valueOf(rodNUmber);
			 Reimbursement reimbursementKey = getReimbursementObjectByKey(reimbusementKey);
			  rejectioDto = new ViewRejectionDTO();
			 if(reimbursementKey != null) {
			 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
			 
			 if(rejectionKeyByReimbursement != null) {
				rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
				rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
				String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
				rejectioDto.setAdmissionDate(admisionDate);
				String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
				//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//				rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
				rejectioDto.setRejectedDate(rejectDate);
				String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
				rejectioDto.setApproveRejectionDate(approverejectDate);
				rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
				rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
				rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
				
			 }
			
			 }
			 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
				rejectioDto.setAdmissionDate(admisionDate);
			 request.setAttribute("rejectList",rejectioDto);
			}
	  
	  if(null != claimforIntimation){
		  
		 request.setAttribute("claim", claimforIntimation);
		  
//		 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
		 
//		 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
		 
		 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
		 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
		 
		 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
		 
		 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
		 
		 
		 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
			 
			 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
			 
			 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
			 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
			 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//			 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
			 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
			 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
			 }
			 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
			 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
			 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
			 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
			 tableDTO.setStatus(docAcknowledgement.getStatus());
			 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
			 
			 tableDTO.setAmount((docAcknowledgement.getApprovedAmount() != null ? Double.valueOf(docAcknowledgement.getApprovedAmount()) : 0));
		
			 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
				 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
				 }
			 
			/* Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
			 tableDTO.setAmount(Double.valueOf(approvedAmount));*/
			 
			 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
			// tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate() != null ? docAcknowledgement.getStrLastDocumentReceivedDate() : ""));
			 
//			 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceiptId().getValue());
//			
//			 
//			 String billClassification = docAcknowledgement.getHospitalisationFlag() != null ? docAcknowledgement.getHospitalisationFlag().equalsIgnoreCase("y") ? "Hospitalisation, " : "" : "";
//			 billClassification += docAcknowledgement.getPreHospitalisationFlag() != null ? docAcknowledgement.getPreHospitalisationFlag().equalsIgnoreCase("y") ? "Pre-Hospitalisation, ":"" :"";
//			 billClassification += docAcknowledgement.getPostHospitalisationFlag() != null ? docAcknowledgement.getPostHospitalisationFlag().equalsIgnoreCase("y")  ? "Post-Hospitalisation, " : "":"";
//			 billClassification += docAcknowledgement.getPartialHospitalisationFlag() != null ? docAcknowledgement.getPartialHospitalisationFlag().equalsIgnoreCase("y") ? "Partial-Hospitalisation" : "":"";
//			 billClassification += docAcknowledgement.getHospitalizationRepeatFlag() != null ? docAcknowledgement.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? "Repeat-Hospitalisation" : "":"";
//			 billClassification += docAcknowledgement.getPatientCareFlag() != null ? docAcknowledgement.getPatientCareFlag().equalsIgnoreCase("y") ? "Add on Benefits (Patient Care)" : "":"";
//			 billClassification += docAcknowledgement.getHospitalCashFlag() != null ? docAcknowledgement.getHospitalCashFlag().equalsIgnoreCase("y") ? "Add on Benefits (Hospital cash)": "":"";
//			 billClassification += docAcknowledgement.getLumpsumAmountFlag() != null ? docAcknowledgement.getLumpsumAmountFlag().equalsIgnoreCase("y") ? "Lumpsum Amount": "":"";
//			 tableDTO.setBillClassification(billClassification);
//			
//
//			 tableDTO.setStatus(docAcknowledgement.getStatus().getProcessValue());
//			 
//			 if(null != docAcknowledgement.getRodKey()){
//				 
//				 Reimbursement reimbursement = reimbursementService.getReimbursementByKey(docAcknowledgement.getRodKey());
//				if(null != reimbursement.getMedicalCompletedDate()){
//					tableDTO.setMedicalResponseDate(SHAUtils.formatDate(reimbursement.getMedicalCompletedDate()));
//				}
//				 tableDTO.setReimbursementNo(reimbursement.getRodNumber());
//				 if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) || ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey())){
//					 tableDTO.setRemarks(reimbursement.getApprovalRemarks());
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REJECT_STATUS)){
//					 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getRejectionRemarks() : ""  ;
//					 tableDTO.setRemarks(reimbursementRejectionRemarks);
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_QUERY_STATUS)){
//					 String latestQueryRemarks = null !=  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()) ?  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()).getQueryRemarks() : "";
//					 tableDTO.setRemarks(latestQueryRemarks);
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)){
//					 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getRejectionRemarks() : ""  ;
//					 tableDTO.setRemarks(reimbursementRejectionRemarks);
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)){
//					 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getRejectionRemarks() : ""  ;
//					 tableDTO.setRemarks(reimbursementRejectionRemarks);
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS)){
//					 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getDisapprovedRemarks() : ""  ;
//					 tableDTO.setRemarks(reimbursementRejectionRemarks);
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)){
//					 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getDisapprovedRemarks() : ""  ;
//					 tableDTO.setRemarks(reimbursementRejectionRemarks);
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)){
//					 String latestQueryRemarks = null !=  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()) ?  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()).getQueryRemarks() : "";
//					 tableDTO.setRemarks(latestQueryRemarks);
//				 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
//					 String latestQueryRemarks = null !=  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()) ?  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()).getQueryRemarks() : "";
//					 tableDTO.setRemarks(latestQueryRemarks);
//				 }
//				 
//				 
//				 tableDTO.setStatus(reimbursement.getStatus().getProcessValue());
//			 }
			 
			 Map<String,String> tokenInputs = new HashMap<String, String>();
			 tokenInputs.put("intimationNo", intimationNumber);
			 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
			 String ackToken = null;
			  try {
				  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
			  } catch (NoSuchAlgorithmException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  } catch (ParseException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  tokenInputs = null;
			  tableDTO.setAcknowledgementToken(ackToken);
			 docAcknowledgementList.add(tableDTO);
		
		 }
		 	
			 request.setAttribute("reimbursementList", docAcknowledgementList);
	 
			 if(rodNUmber != null) {
				 Long reimbusementKey = Long.valueOf(rodNUmber);
				 Reimbursement reimbursementKey = getReimbursementObjectByKey(reimbusementKey);
				 
				 if(reimbursementKey != null){
					 
					 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
					 Hospitals hospitalDetails=null; 
					 
					 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
					 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
					 //need to implement
					 if(claimforIntimation != null){
						 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
						 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
					 }	
					 
					 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
						 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
						 viewQueryDTO.setQueryDate(rejectDate);
						 viewQueryDTO.setDiagnosis(diagnosisName);
						 viewQueryDTO.setReimbursementKey(reimbursementKey.getKey());
						 
						 if(hospitalDetails != null){
							 viewQueryDTO.setHospitalName(hospitalDetails.getName());
							 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
							 viewQueryDTOvalues.add(viewQueryDTO);
						 }
//						viewQueryDTO.setClaim(claimforIntimation);
						 
						 
					 }
					 request.setAttribute("queryList", viewQueryDTOvalues);
				 }
				 
				
			 }
			 
		  
		List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claimforIntimation.getKey(), reimkey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		 
		 List<ViewClaimHistoryDTO> trailsList = new ArrayList<ViewClaimHistoryDTO>();
		 for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
			 
			 ViewClaimHistoryDTO viewHistoryDto = new ViewClaimHistoryDTO();
			 
			 viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getClaimStage());
			 viewHistoryDto.setReimbursementKey(viewClaimHistoryDTO.getReimbursementKey());
			 viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
			 viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
			 viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
			 viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
			 viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
			 trailsList.add(viewHistoryDto);
		}
		 
		 request.setAttribute("viewtrailsList", trailsList);
		 
		 
		 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
		 
		 
		
		 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
		 
		 //Financial List
		 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
					.getViewTmpRimbursementDetails(claimforIntimation.getKey());

			List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
			List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
			if (reimbursementDetails != null) {
				int i =1;
				int snoFinance = 1;
				for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
					if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
						
						if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
		                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
		                 
		                 dto.setSno(i);
		                 i++;
		                 if(null != reimbursement.getClaim()){
		                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
		                 }
		//                 if(null != reimbursement.getBillingApprovedAmount()){
		//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
		//                 }
		                 
		                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
							if(approvedAmount >0){
								dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
						}
		//                 if(reimbursement.getBillingCompletedDate() != null){
		                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
		                 if(reimbursement.getBillingApprovedAmount() != null){
		                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
		                 }
		//                 }
		                 dto.setStatus(reimbursement.getStatus().getProcessValue());
		                 dto.setRodKey(reimbursement.getKey());
		                 Map<String,String> tokenInputs = new HashMap<String, String>();
						 tokenInputs.put("intimationNo", intimationNumber);
						 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
						 String billDtlsToken = null;
						  try {
							  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
						  } catch (NoSuchAlgorithmException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (ParseException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  tokenInputs = null;
						  dto.setBillDtslToken(billDtlsToken);
		                 
		                 billDetails.add(dto);
						}
						
						if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
							FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
							dto1.setSno(snoFinance);
							snoFinance++;
							dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
							Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
							if(approvedAmount >0){
								dto1.setAmount(String.valueOf(approvedAmount));
						     }
		//					if(reimbursement.getFinancialCompletedDate() != null){
							dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
							
							if(reimbursement.getFinancialApprovedAmount() != null){
								dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
							}
							
		//					}
							dto1.setStatus(reimbursement.getStatus().getProcessValue());
			                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
			                dto1.setRodKey(reimbursement.getKey());
			                
			                if(reimbursement.getDocAcknowLedgement() != null && 
			                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
			                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
			                }
			                
			                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
			                {
			                	ClaimPayment claimPayment;	
			                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//			                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
			                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
			                	
			                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
			                	{
			                		claimPayment = claimPaymetList.get(0);
			                		dto1.setPaymentType(claimPayment.getPaymentType());
			                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
			                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
			                		
			                	}
			                	
			                }
			                
			                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
			                	dto1.setAmount("0");
			                }
			                
//			                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
			                financialDetails.add(dto1);
						}
					}
				}
			}
			 
			request.setAttribute("billing", billDetails);
			request.setAttribute("financial", financialDetails);
			
			/* Payment Details */
			 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
			 	
			 	
			 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
				
				List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
				
				List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
				int snum =1;
				for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
					if(paymentProcessCpuTableDTO2.getChequeDate() != null){
						paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
						
					}
					paymentProcessCpuTableDTO2.setSerialNo(snum);
					paymentList.add(paymentProcessCpuTableDTO2);
					snum++;
				}
				
				request.setAttribute("payment", paymentList);
			 
		 }
	  	BPMClientContext bpmClientContext = new BPMClientContext();
	  	 if(null != claimforIntimation){
	  		Map<String,String> tokenInputs = new HashMap<String, String>();
			 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
			 String intimationNoToken = null;
			  try {
				  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
			  } catch (NoSuchAlgorithmException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  } catch (ParseException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  tokenInputs = null;
			String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
			/*Below Code commented due to security issue
			String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
			//getUI().getPage().open(url, "_blank");
			request.setAttribute("dmsUrl",url);
	  	 }
		String queryString = request.getQueryString();
		String rodNo = request.getParameter("reimb_num");
		String cashlessNo = request.getParameter("cashless_num");
		String ackRod = request.getParameter("ack_num");
		String rodQuery = request.getParameter("reim_query");
		String rodNumber = request.getParameter("rod_num");
		String rodKey = request.getParameter("rod_key");
		
		if(!error.isEmpty()){
			  request.setAttribute("error", error);
			  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
		  } else if((queryString.contains("int_num") && queryString.contains("reimb_num"))) {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  showAcknowledgement(intimationNumber,rodNo,request,response);
//			  request.setAttribute("reimbursementList", docAcknowledgementList);
			  request.getRequestDispatcher("/WEB-INF/ViewAcknowledgementDetails.jsp").include(request, response);
		  } else if(queryString.contains("int_num") && queryString.contains("cashless_num")) {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  showPreauthDetailsPage(intimationNumber,cashlessNo,request,response);
			  
		  } else if(queryString.contains("int_num") && queryString.contains("bill_details")) {
			  String reimNo = request.getParameter("bill_details");
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  showBillDetails(intimationNumber,reimNo,request,response);
			  
		  } else if(queryString.contains("int_num") && queryString.contains("bill_summary")) {
			  String reimNo = request.getParameter("bill_summary");
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  showBillSummaryDetails(intimationNumber,reimNo,request,response);
//			  request.getRequestDispatcher("/WEB-INF/ViewBillSummaryDetials.jsp").include(request, response);
			  
		  } 
		  else if(queryString.contains("intim_num")) {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("queryList", viewQueryDTOvalues);
			  request.setAttribute("policyList",policy);
			  request.setAttribute("hospitals", hospitals);
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  request.getRequestDispatcher("/WEB-INF/ViewQueryDetails.jsp").include(request, response);
		  } /* else if(queryString.contains("intimation_nomber")) {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  request.getRequestDispatcher("/WEB-INF/ViewTrails.jsp").include(request, response);
		  }*/
		  else if(queryString != null && queryString.contains("int_num") && queryString.contains("key")){
			  intimationNumber = request.getParameter("int_num");
			  String  preauthKey = request.getParameter("key");
			  long cashlessKey = Long.parseLong(preauthKey);
			  showCashlessDocPage(intimationNumber,cashlessKey, request, response);
		  } else if(queryString.contains("int_num") && queryString.contains("ack_num")) {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  showAcknowledgementDetails(intimationNumber,ackRod,request,response);
			  request.getRequestDispatcher("/WEB-INF/ViewAcknowledgeDetailDesc.jsp").include(request, response);
	  }
		  else if(queryString.contains("int_num") && queryString.contains("reim_query")) {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  request.setAttribute("daignosisName", daignosisName);
			  showQueryDetails(intimationNumber,rodQuery,request,response);
			  request.getRequestDispatcher("/WEB-INF/ViewQueryDetailDesc.jsp").include(request, response);
		  }
		  else if(queryString.contains("int_num")) {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("rejectList",rejectioDto);
			  request.setAttribute("policyList",policy);
			  request.setAttribute("hospitals", hospitals);
			  request.getRequestDispatcher("/WEB-INF/ViewRejectionDetails.jsp").include(request, response);
		  }	
		  else if(queryString.contains("rejection_intimation_numbr") && queryString.contains("rod_num") && queryString.contains("rod_key")) {
			  request.setAttribute("intimation", intimationByNo);
			  showRejectionLetter(intimationNumber,rodNumber,rodKey, request, response);	 
		  }	
		  else if(queryString.contains("intimation_numb") && queryString.contains("rod_num") && queryString.contains("rod_key")) {
			  request.setAttribute("intimation", intimationByNo);
			  showQueryLetter(intimationNumber, rodNumber,rodKey,request, response);	 
		  }	
		 
			  
		  else
		  {
			  request.setAttribute("error", error);
			  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
		  }
		
		 
	  }
//	  else{
//		  if(intimationNumber.isEmpty()){
//			  error += "Please Enter Intimation Number";
//		  }
////	      else if(healthCardNo.isEmpty()){
////	    	  error += "Please Enter  Health Card Number";
////		  }
////	      else if(intimationNumber.isEmpty()){
////			  error += "Please Enter Intimation Number";
////			  
////		  }
//			 request.setAttribute("error", error);
//			 request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
//	  }
}


private void showAcknowledgement(String intimationNumber,String rodNo, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	Long reimNumber = Long.valueOf(rodNo);
	List<ViewDocumentDetailsDTO> acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimNumber);
	request.setAttribute("docAcknList", acknowledgeDocsList);
	request.setAttribute("docAckNo", intimationNumber);
}

private void showBillDetails(String intimationNumber,String rodNo, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	Long reimNumber = Long.valueOf(rodNo);
	
	Double sumInsured = null;
	List<HospitalisationDTO> billDetails = new ArrayList<HospitalisationDTO>();
	
	Intimation intimationByNo = intimationService.getIntimationByNo(intimationNumber);
	NewIntimationDto intimationDto = intimationService
			.getIntimationDto(intimationByNo);
	Long insuredId = intimationDto.getInsuredPatient().getInsuredId();
	Long policyKey = intimationDto.getPolicy().getKey();
	Long productId = intimationDto.getPolicy().getProduct().getKey();
	
	Reimbursement reimbursement = acknowledgementDocumentsReceivedService.getReimbursement(reimNumber);

    if(insuredId != null){
    sumInsured = dbCalculationService.getInsuredSumInsured(insuredId.toString(), policyKey,"H");
    }
    Hospitals hospitalById = hospitalService.getHospitalById(intimationByNo.getHospital());
    Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
    
    if(reimbursement.getSectionCategory() != null){
    	valuesMap = dbCalculationService.getHospitalizationDetails(productId, sumInsured,hospitalById.getCityClass().toString(), insuredId, intimationByNo.getKey(),reimbursement.getSectionCategory(),"0");
    }else{
    	 valuesMap = dbCalculationService.getHospitalizationDetails(productId, sumInsured,hospitalById.getCityClass().toString(), insuredId, intimationByNo.getKey(),0l,"0");
    }
   
  	 
    
	
	List<RODDocumentSummary> rodDocumentSummaryList = billDetailsService.getBillDetailsByRodKey(reimNumber);
	
	List<HospitalisationDTO> hospitalisationDetailsList = new ArrayList<HospitalisationDTO>();
	
	int sno=1;

	for (RODDocumentSummary rodDocumentSummary : rodDocumentSummaryList) {
		Long documentSummaryKey = rodDocumentSummary.getKey();

		List<RODBillDetails> rodBillDetails = billDetailsService
				.getBilldetailsByDocumentSummayKey(documentSummaryKey);

		if (rodBillDetails != null) {
			List<HospitalisationDTO> hospitalisationList = new ArrayList<HospitalisationDTO>();
			int i = 1;
			for (RODBillDetails rodBillDetails2 : rodBillDetails) {
				HospitalisationDTO dto = new HospitalisationDTO();
				if (i == 1) {
					dto.setSno(sno);
					dto.setRodNumber(rodDocumentSummary.getReimbursement()
							.getRodNumber());
					if (null != rodDocumentSummary.getFileType()) {
						dto.setFileType(rodDocumentSummary.getFileType()
								.getValue());
					}
					dto.setFileName(rodDocumentSummary.getFileName());
					dto.setBillNumber(rodDocumentSummary.getBillNumber());
					if (null != rodDocumentSummary.getBillDate()) {

						String formatDate = SHAUtils
								.formatDate(rodDocumentSummary
										.getBillDate());
						dto.setBillDate(formatDate);
					}
					dto.setNoOfItems(rodDocumentSummary.getNoOfItems());
					dto.setBillValue(rodDocumentSummary.getBillAmount());
					sno++;
				}
				dto.setItemNo(i);
				dto.setItemName(rodBillDetails2.getItemName());
				if (null != rodBillDetails2.getBillClassification()) {
					dto.setClassification(rodBillDetails2
							.getBillClassification().getValue());
				}
				if (null != rodBillDetails2.getBillCategory()) {
					dto.setCategory(rodBillDetails2.getBillCategory()
							.getValue());
				}
				
			
			if(null != rodBillDetails2.getNoOfDaysBills())
			{
				dto.setNoOfDays(rodBillDetails2.getNoOfDaysBills().longValue());
			}
				if (null != rodBillDetails2.getPerDayAmountBills()) {
					dto.setPerDayAmt(rodBillDetails2.getPerDayAmountBills()
							.longValue());
				}
				if (null != rodBillDetails2.getClaimedAmountBills()) {
					dto.setClaimedAmount(rodBillDetails2
							.getClaimedAmountBills().longValue());
				}
				if (null != rodBillDetails2.getNoOfDaysPolicy()) {
					dto.setEntitlementNoOfDays(rodBillDetails2
							.getNoOfDaysPolicy().longValue());
				}
				if (null != rodBillDetails2.getPerDayAmountPolicy()) {
					dto.setEntitlementPerDayAmt(rodBillDetails2
							.getPerDayAmountPolicy().longValue());
				}
				if (null != rodBillDetails2.getTotalAmount()) {
					dto.setAmount(rodBillDetails2.getTotalAmount()
							.longValue());
				}
				if (null != rodBillDetails2.getDeductibleAmount()) {
					dto.setDeductionNonPayableAmount(rodBillDetails2
							.getPayableAmount().longValue());
				}
				if (null != rodBillDetails2.getClaimedAmountBills()
						&& null != rodBillDetails2.getPayableAmount()) {
					Double claimedAmount = rodBillDetails2
							.getClaimedAmountBills();
					Double disAllowance = rodBillDetails2
							.getPayableAmount();
					Double netAmount = claimedAmount - disAllowance;
					dto.setPayableAmount(netAmount.longValue());
				}
				dto.setReason(rodBillDetails2.getNonPayableReason());
				dto.setMedicalRemarks(rodBillDetails2.getMedicalRemarks());
				if (null != rodBillDetails2.getIrdaLevel1Id()) {

					SelectValue irdaLevel1ValueByKey = masterService
							.getIRDALevel1ValueByKey(rodBillDetails2
									.getIrdaLevel1Id());
					if (irdaLevel1ValueByKey != null) {
						dto.setIrdaLevel1(irdaLevel1ValueByKey.getValue());
					}
				}
				if (null != rodBillDetails2.getIrdaLevel2Id()) {
					SelectValue irdaLevel2ValueByKey = masterService
							.getIRDALevel2ValueByKey(rodBillDetails2
									.getIrdaLevel2Id());
					if (irdaLevel2ValueByKey != null) {
						dto.setIrdaLevel2(irdaLevel2ValueByKey.getValue());
					}
				}
				if (null != rodBillDetails2.getIrdaLevel3Id()) {
					SelectValue irdaLevel3ValueByKey = masterService
							.getIRDALevel3ValueByKey(rodBillDetails2
									.getIrdaLevel3Id());
					if (irdaLevel3ValueByKey != null) {
						dto.setIrdaLevel3(irdaLevel3ValueByKey.getValue());
					}
				}

				hospitalisationList.add(dto);
				i++;
			}
			for (HospitalisationDTO hospitalisationDTO : hospitalisationList) {
            	
				billDetails.add(hospitalisationDTO);
				
			}
		}
	}
			
    request.setAttribute("billDetailsList", billDetails);
	request.getRequestDispatcher("/WEB-INF/ViewBillDetails.jsp").include(request, response);
}

private void showBillSummaryDetails(String intimationNumber,String rodNo, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	
	Long rodKey = Long.valueOf(rodNo);
	Double preTotalCLaimedAmt = 0d;
	Double preNonPayableAmt = 0d;
	Double preNetAmt = 0d;
	Double postTotalCLaimedAmt = 0d;
	Double postNonPayableAmt = 0d;
	Double postNetAmt = 0d;
	
	if(rodKey != null){
		dbCalculationService.getBillDetailsSummary(rodKey);
	}
	
	Reimbursement reimbursement = masterService.getReimbursementDetailsByKey(rodKey);
	
	Intimation intimation = reimbursement.getClaim().getIntimation();
	
	NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
	PreauthDTO preauthDto = new PreauthDTO();
	preauthDto.setNewIntimationDTO(intimationDto);
	
	preauthDto = getProrataFlagFromProduct(preauthDto);
	viewBillSummaryPage.init(preauthDto,rodKey,true);
	Map<String, Double> footerValues = viewBillSummaryPage.getFooterValue();
	List<BillingPreHospitalisation> preHospitalisation = billDetailsService.getBillingPreHospitalisationList(rodKey);
	
	List<BillEntryDetailsDTO> hospitalizationTabValues = viewBillSummaryPage.getHospitalizationTabValues();
	List<PreHospitalizationDTO> preHospitalizationValues = viewBillSummaryPage.getPreHospitalizationTabValues();
	List<PreHospitalizationDTO> postHospitalizationValues = viewBillSummaryPage.getPostHospitalizationTabValues();
	
	
	for (BillingPreHospitalisation billingPreHospitalisation : preHospitalisation) {
		preTotalCLaimedAmt = billingPreHospitalisation.getClaimedAmountBills();
		 preNonPayableAmt = billingPreHospitalisation.getNonPayableAmount();
		 preNetAmt = billingPreHospitalisation.getNetAmount();
		
	}
	
	
	List<PostHospitalisation> postHospitalisation = billDetailsService.getPostHospitalisationList(rodKey);
	
	for (PostHospitalisation postHospitalisation2 : postHospitalisation) {
		postTotalCLaimedAmt = postHospitalisation2.getClaimedAmountBills();
		 postNonPayableAmt = postHospitalisation2.getNonPayableAmount();
		 postNetAmt = postHospitalisation2.getNetAmount();
	}
	
	if(null != hospitalizationTabValues && !hospitalizationTabValues.isEmpty())
	{
		preauthDto.setHospitalizationTabSummaryList(hospitalizationTabValues);
		if(footerValues != null){
			preauthDto.setTotalApprovedAmt(footerValues.get(SHAConstants.NET_AMOUNT));
			preauthDto.setTotalClaimedAmt(footerValues.get(SHAConstants.TOTAL_CLAIMED_AMOUNT));
			preauthDto.setToatlNonPayableAmt(footerValues.get(SHAConstants.TOTALDISALLOWANCE));
			preauthDto.setAmountTotal(footerValues.get(SHAConstants.TOTAL_CLAIMED_AMOUNT));
			if(preauthDto.getNonPayableTotalAmt() != null){
			preauthDto.setNonPayableTotalAmt(preauthDto.getNonPayableTotalAmt() + footerValues.get(SHAConstants.TOTALDISALLOWANCE));
			}
			preauthDto.setNonPayableTotalAmt(footerValues.get(SHAConstants.TOTALDISALLOWANCE));
			preauthDto.setAmountTotal(footerValues.get(SHAConstants.TOTAL_CLAIMED_AMOUNT));
			preauthDto.setNonpayableProdTotal(footerValues.get(SHAConstants.NONPAYABLE_PRODUCT_BASED));
			preauthDto.setNonpayableTotal(footerValues.get(SHAConstants.NONPAYABLE));
			preauthDto.setPropDecutTotal(footerValues.get(SHAConstants.PROPORTION_DEDUCTION_AMOUNT));
			preauthDto.setReasonableDeducTotal(footerValues.get(SHAConstants.REASONABLE_DEDUCTION));
			preauthDto.setDisallowanceTotal(footerValues.get(SHAConstants.TOTALDISALLOWANCE));
			
		}
	}
	if(null != preHospitalizationValues && !preHospitalizationValues.isEmpty())
	{
		preauthDto.setPreHospitalizationTabSummaryList(preHospitalizationValues);
		
		
	}
	if(null != postHospitalizationValues && !postHospitalizationValues.isEmpty())
	{
		preauthDto.setPostHospitalizationTabSummaryList(postHospitalizationValues);
		
	}
	request.setAttribute("hospitalizationDto", preauthDto);
	request.setAttribute("preHospitalisationDto", preauthDto);
	request.setAttribute("postHospitalisationDto", preauthDto);
	request.setAttribute("preclaimedAmt", preTotalCLaimedAmt);
	request.setAttribute("prePayableAmt", preNetAmt);
	request.setAttribute("preNonPayableAmt", preNonPayableAmt);
	request.setAttribute("postclaimedAmt", postTotalCLaimedAmt);
	request.setAttribute("postPayableAmt", postNetAmt);
	request.setAttribute("postNonPayableAmt", postNonPayableAmt);

	request.getRequestDispatcher("/WEB-INF/ViewBillSummaryDetails.jsp").include(request, response);
	
}

private void showPreauthDetailsPage(String intimationNumber,String cashlessNo, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	String specialistConsultedValue = null;
	TextField textField = new TextField();
	String policyAgeing = "";
	Integer totalDiagConsAmt = 0;
	Integer totalDiagPayableAmt = 0;
	Integer totalDiagCopayAmt = 0;
	Integer totalDiagNetAmt = 0;
	Integer totalDiagReverseAllocAmt = 0;
	String claimNo = null;
	
	Long cashlessKey = Long.valueOf(cashlessNo);
	ViewTmpPreauth tmpPreauth = preauthService.getViewTmpPreauthById(cashlessKey);
	PreauthToTmpPreauthMapper preauthConverter = PreauthToTmpPreauthMapper.getInstance();
	Preauth preauthDtls = preauthConverter.getPreauth(tmpPreauth);
	Preauth preauth = preauthService.getPreauthById(cashlessKey);
	claimNo = preauth.getPreauthId();
	PreauthDTO preauthDto = getPreauthDTO(preauth);
	if(preauthDto.getNewIntimationDTO() != null){
		policyAgeing = preauthDto.getNewIntimationDTO().getPolicyYear();
	}
	getResidualAmount(preauth, preauthDto);
	List<SpecialityDTO> specialityList = preauthDto.getPreauthDataExtractionDetails().getSpecialityList();
	for (SpecialityDTO specialityDTO : specialityList) {
		specialityDTO.setStatusFlag(true);
	}
	List<ViewProcedureExclusionCheckDTO> procedureList = viewProcedureExclusionService.search(cashlessKey);
	FieldVisitRequest fieldVisitByPreauthKey = fieldVisitRequestService.getFieldVisitByPreauthKey(cashlessKey);
	if(preauth.getSpecialistConsulted() != null){
		MastersValue specialistConsulted = masterService.getSpecialistConsulted(preauth.getSpecialistConsulted());
		specialistConsultedValue = specialistConsulted.getValue();
	}
	List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues = setValueToMedicalDecisionValues(preauthDto);
	diagnosisListenerTableObj = diagnosisListenerTableInstance.get();
	diagnosisListenerTableObj.init(preauthDto,textField);
	
	for (DiagnosisProcedureTableDTO diagnosisProcedureTableDTO : setValueToMedicalDecisionValues) {
		Integer amountCons = diagnosisProcedureTableDTO.getAmountConsidered() != null ? diagnosisProcedureTableDTO.getAmountConsidered() :0 ;
		totalDiagConsAmt += amountCons != null ? amountCons : 0;
		Integer minAmt = diagnosisProcedureTableDTO.getMinimumAmount() != null ? diagnosisProcedureTableDTO.getMinimumAmount() : 0;
		totalDiagPayableAmt += minAmt != null ? minAmt : 0;
		Integer diagCopay = diagnosisProcedureTableDTO.getCoPayAmount() != null ? diagnosisProcedureTableDTO.getCoPayAmount() : 0;
		totalDiagCopayAmt += diagCopay != null ? diagCopay : 0;
		Integer netAmt = diagnosisProcedureTableDTO.getNetAmount() != null ? diagnosisProcedureTableDTO.getNetAmount() : 0;
		totalDiagNetAmt += netAmt != null ? netAmt : 0;
		Integer reverseAllocAmt = diagnosisProcedureTableDTO.getReverseAllocatedAmt() != null ? diagnosisProcedureTableDTO.getReverseAllocatedAmt() : 0;
		totalDiagReverseAllocAmt += reverseAllocAmt != null ? reverseAllocAmt :0;
		
	}
	
	List<ViewPedValidationTableDTO> ViewPedValidationTableDTO = viewPedValidationService
			.search(preauth.getKey());
	for (ViewPedValidationTableDTO viewPedValidationTableDTO2 : ViewPedValidationTableDTO) {
		viewPedValidationTableDTO2.setPolicyAgeing(policyAgeing);
	}
	
	List<ProcedureDTO> procedureExclusionCheckTableList = preauthDto.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
	
	Hospitals hospitalById = hospitalService.getHospitalById(bean.getHospitalKey());
	PolicyDto policyDTO = bean.getPolicyDto();
	//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
	Double sumInsured = dbCalculationService.getInsuredSumInsured(bean.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), bean.getPolicyDto().getKey()
			,bean.getNewIntimationDTO().getInsuredPatient().getLopFlag());
	if (sumInsured == 0) {
		sumInsured = policyDTO.getTotalSumInsured();
	}
     Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();
	
	if(bean.getPreauthDataExtractionDetails().getSection() != null){
		try{
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(bean.getNewIntimationDTO().getPolicy().getKey(),
						bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
						bean.getNewIntimationDTO().getKey(),bean.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
						bean.getNewIntimationDTO().getKey(),bean.getPreauthDataExtractionDetails().getSection().getId(),"A");
			}
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}else{
		try{
			if(ReferenceTable.getGMCProductList().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())){
				sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(bean.getNewIntimationDTO().getPolicy().getKey(),
						bean.getNewIntimationDTO().getInsuredPatient().getKey(),bean.getNewIntimationDTO().getPolicy().getSectionCode());
				hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), bean.getNewIntimationDTO().getKey(),0l,"0");
			}else{
				hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
						sumInsured, hospitalById.getCityClass(), bean.getNewIntimationDTO().getInsuredPatient().getInsuredId(), bean.getNewIntimationDTO().getKey(),0l,"0");
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	
	claimedAmountDetailsTableObj = claimedAmountDetailsTable.get();
	ViewPreauthMoreDetailsPresenter preauthMoreDetailsPresenter = new ViewPreauthMoreDetailsPresenter();
//	ParameterDTO parameterDTO = new ParameterDTO(preauthDto, null);


	Map<Integer, Object> hospitalizationDetailsVal = new HashMap<Integer, Object>();
	hospitalizationDetailsVal = preauthMoreDetailsPresenter.getHospitalizationDetails(preauthDto, hospitalService, dbCalculationService);
	preauthDto.setHospitalizationDetailsVal(hospitalizationDetailsVal);
	
	claimedAmountDetailsTableObj.initView(preauthDto,SHAConstants.VIEW_PREAUTH_CLAIM_STATUS);
	claimedAmountDetailsTableObj.setValues(this.bean.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
	
	List<NoOfDaysCell> claimedAmtDetailsValues = claimedAmountDetailsTableObj.getValues();
	this.bean.getPreauthDataExtractionDetails().setClaimedDetailsList(claimedAmtDetailsValues);
	
	Integer totalClaimedAmtforAmtconsd = claimedAmountDetailsTableObj.getTotalClaimedAmt();
	Integer totalDeductAmtforAmtconsd = claimedAmountDetailsTableObj.getTotalDeductibleAmount();
	Integer totalNonPayableAmtforAmtconsd = claimedAmountDetailsTableObj.getTotalDeductableAmount();
	Integer totalPayableAmtforAmtconsd = claimedAmountDetailsTableObj.getTotalPayableAmt();
	Integer totalProductAmtforAmtconsd = claimedAmountDetailsTableObj.getTotalProductAmount();
	Integer totalNetAmtforAmtconsd = claimedAmountDetailsTableObj.getTotalNetAmount();
	
	amountConsideredTable = amountConsideredTableInstance.get();
	amountConsideredTable.initView(preauthDto, true);
	HorizontalLayout HorizontalLayout = new HorizontalLayout();
	Layout layout = null;
	
	amountConsideredTable.initTable(preauthDto, HorizontalLayout, HorizontalLayout , layout, true, false);
	
	Integer balanceSumInsuredAmt = amountConsideredTable.getBalanceSumInsuredAmt();
	Integer minimumValue = amountConsideredTable.getMinimumValue();

	if(bean.getAmountConsidered() != null){		
		minimumValue = Integer.valueOf(bean.getAmountConsidered());		
	}

	Integer minimumValueForGmc = amountConsideredTable.getMinimumValueForGMC();
	Integer finalapprovalAmt = amountConsideredTable.getApprovedAmount();
	String otherInsurerAmt = amountConsideredTable.getOtherInsurerAmount();
	
	Map<Integer, String> firstableValues = new HashMap<Integer, String>();
	
	firstableValues.put(0, "Amount Considered");
	firstableValues.put(1, "Co-Pay");
	firstableValues.put(2, "Amount Considered (After Co-Pay)");
	
	Map<Integer, String> secondtableValues = new HashMap<Integer, String>();
	secondtableValues.put(0, "Balance Sum Insured");
	secondtableValues.put(1, "Co-Pay");
	secondtableValues.put(2, "Balance Sum Insured (After Co-Pay)");
	
		
	request.setAttribute("preauthDto", preauthDto);
	request.setAttribute("preauthDetails", preauth);
	request.setAttribute("claimNumber", claimNo);
	request.setAttribute("speciality", specialityList);
	request.setAttribute("procedureList", procedureList);
	request.setAttribute("fieldVisitByPreauthKey", fieldVisitByPreauthKey);
	request.setAttribute("specialistConsulted", specialistConsultedValue);
	request.setAttribute("pedValidationDetails", ViewPedValidationTableDTO);
	request.setAttribute("procedureExclusion", procedureExclusionCheckTableList);
	request.setAttribute("medicalDecisionValues", setValueToMedicalDecisionValues);
	request.setAttribute("calimedAmtDetails", claimedAmtDetailsValues);
	request.setAttribute("firstTableValues", firstableValues);
	request.setAttribute("secondTableValues", secondtableValues);
	request.setAttribute("balanceSumInsuredAmt", balanceSumInsuredAmt);
	request.setAttribute("minimumValue", minimumValue);
	request.setAttribute("minimumValueForGmc", minimumValueForGmc);
	request.setAttribute("finalapprovalAmt", finalapprovalAmt);
	request.setAttribute("otherInsurerAmt", otherInsurerAmt);
	request.setAttribute("totalClaimedAmtforAmtconsd", totalClaimedAmtforAmtconsd);
	request.setAttribute("totalDeductAmtforAmtconsd", totalDeductAmtforAmtconsd);
	request.setAttribute("totalNonPayableAmtforAmtconsd", totalNonPayableAmtforAmtconsd);
	request.setAttribute("totalPayableAmtforAmtconsd", totalPayableAmtforAmtconsd);
	request.setAttribute("totalProductAmtforAmtconsd", totalProductAmtforAmtconsd);
	request.setAttribute("totalNetAmtforAmtconsd", totalNetAmtforAmtconsd);
	request.setAttribute("totalDiagConsAmt", totalDiagConsAmt);
	request.setAttribute("totalDiagPayableAmt", totalDiagPayableAmt);
	request.setAttribute("totalDiagCopayAmt", totalDiagCopayAmt);
	request.setAttribute("totalDiagNetAmt", totalDiagNetAmt);
	request.setAttribute("totalDiagReverseAllocAmt", totalDiagReverseAllocAmt);
	
	
	request.getRequestDispatcher("/WEB-INF/ViewPreauthDetails.jsp").include(request, response);
}
private void showAcknowledgementDetails(String intimationNumber,String ackNo, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	
	DocAcknowledgement docAck = getDocAcknowledgement(ackNo);
	DocAcknowledgement acknowledgeDocsList = acknowledgementDocumentsReceivedService.getDocAcknowledgementBasedOnKey(docAck.getKey());
	List<DocumentCheckListDTO> rodDocumentList = acknowledgementDocumentsReceivedService.getDocumentList(docAck.getKey());
	DocumentDetailsDTO docackValues = acknowledgementDocumentsReceivedService.getAcknowledgementDetails(docAck.getKey());
	DocAcknowledgement docAcknowledgement = acknowledgementDocumentsReceivedService.getDocAcknowledgementBasedOnKey(docAck.getKey());
	List<ReconsiderRODRequestTableDTO> reconsiderRODList = acknowledgementDocumentsReceivedService.getReconsiderationDetailsList(docAcknowledgement);
	
	List<RODQueryDetailsDTO> rodQueryList = acknowledgementDocumentsReceivedService.getQueryDetailsList(docAck.getKey());
	if(docackValues != null) {
		docackValues.setAcknowledgmentCreatedId(acknowledgeDocsList.getCreatedBy());
		docackValues.setAcknowledgmentCreatedName(acknowledgeDocsList.getCreatedBy());
	}
	request.setAttribute("ackDocList", rodDocumentList);
	request.setAttribute("ackdQueryDetails",rodQueryList);
	request.setAttribute("docAckDetails", docackValues);
}
private void showQueryDetails(String intimationNumber,String rodQuery,HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
	
	List<ViewQueryDTO> viewQueryDTOvalues = null;
	Claim claimforIntimation = null;
	Intimation intimationByNo = null;
	
	Reimbursement reimbursementKey = getReimbursementObjectByKey(Long.valueOf(rodQuery));
	
	if(reimbursementKey.getKey() != null) {
		
		
		ReimbursementQuery queryKey = reimbursementService.getReimbursementQueryByReimbursmentKey(reimbursementKey.getKey());
		ReimbursementQuery reimbursementQuery = acknowledgementDocumentsReceivedService.getReimbursementQuery(queryKey.getKey());
		ViewQueryDTO viewDetails = EarlierRodMapper.getInstance().getviewDetailsQuery(reimbursementQuery);
		String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
		DocumentDetailsDTO docackValues = acknowledgementDocumentsReceivedService.getAcknowledgementDetails(reimbursementKey.getDocAcknowLedgement().getKey());
		
		
		if(reimbursementQuery != null){
			String billClassification = getBillClassification(reimbursementQuery.getReimbursement().getDocAcknowLedgement());
			viewDetails.setBillClassification(billClassification);

			if(reimbursementQuery.getStatus() != null)
				viewDetails.setQueryStatus(reimbursementQuery.getStatus().getProcessValue());
			
			if(diagnosisName != null) {
				viewDetails.setDiagnosis(diagnosisName);
			}
		}
		
		
//		viewDetails.setQueryRaiseRole(viewQueryDto.getQueryRaiseRole());
//		viewDetails.setQueryRaised(viewQueryDto.getQueryRaised());
		
		if(viewDetails.getQueryRaisedDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getQueryRaisedDate().toString());
			viewDetails.setQueryRaisedDateStr(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getAdmissionDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getAdmissionDate());
			viewDetails.setAdmissionDate(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getQueryDraftedDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getQueryDraftedDate());
			viewDetails.setQueryDraftedDate(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getApprovedRejectedDate()!=null){
			Date tempDate = SHAUtils.formatTimestamp(viewDetails.getApprovedRejectedDate());
			viewDetails.setApprovedRejectedDate(SHAUtils.formatDate(tempDate));
		}
		
		if(viewDetails.getIntimationNo() != null){
			Long hospitalId = reimbursementQuery.getReimbursement().getClaim().getIntimation().getHospital();
			
			Hospitals hospitals = hospitalService.getHospitalById(hospitalId);
			if(hospitals != null){
			viewDetails.setHospitalName(hospitals.getName());
			viewDetails.setHospitalCity(hospitals.getCity());
			viewDetails.setHospitalType(hospitals.getHospitalTypeName());
			}
		}
		
		
		
		request.setAttribute("queryDetails",viewDetails);
		request.setAttribute("acknowledgeDocsList", docackValues);
		request.setAttribute("reimbursementNumber",rodQuery);
	}
	
	DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
	List<DMSDocumentDetailsDTO> querywithDocs = new ArrayList<DMSDocumentDetailsDTO>();
	List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
			.getQueryDocumentDetailsData(intimationNumber,SHAConstants.QUERY);
	for (DMSDocumentDetailsDTO dmsDocumentDetails : dmsDocumentDetailsDTO) {
		 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetails.getDmsDocToken());
		 dmsDocumentDetails.setFileViewURL(imageUrl);
		 querywithDocs.add(dmsDocumentDetails);
	}
	
	if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
		dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		request.setAttribute("queryDocDetails", querywithDocs);
	}
			
	
}
public String getBillClassification(DocAcknowledgement docAcknowledgement){
		
		String classification="";
  	if(docAcknowledgement.getPreHospitalisationFlag() != null){
  		if(docAcknowledgement.getPreHospitalisationFlag().equals("Y")){
  			if(classification.equals("")){
  				classification ="Pre-Hospitalisation";
  			}
  			else{
  			classification =classification+","+"Pre-Hospitalisation";
  			}
  		}
  	}
  	if(docAcknowledgement.getHospitalisationFlag() != null){
  		if(docAcknowledgement.getHospitalisationFlag().equals("Y")){

  			if(classification.equals("")){
  				classification ="Hospitalisation";
  			}
  			else{
  			classification =classification+","+" Hospitalisation";
  			}
  		}
  	}
		if (docAcknowledgement.getPostHospitalisationFlag() != null) {

			if (docAcknowledgement.getPostHospitalisationFlag().equals("Y")) {

				if (classification.equals("")) {
					classification = "Post-Hospitalisation";
				} else {
					classification = classification + ","
							+ " Post-Hospitalisation";
				}
			}
		}
		
		 if(docAcknowledgement.getHospitalCashFlag() != null){
				
				if (docAcknowledgement.getHospitalCashFlag().equals("Y")) {

					if (classification.equals("")) {
						classification = "Add on Benefits (Hospital cash)";
					} else {
						classification = classification + ","
								+ "Add on Benefits (Hospital cash)";
					}
				}
			}
	         
	         if(docAcknowledgement.getLumpsumAmountFlag() != null){
	 			
	 			if (docAcknowledgement.getLumpsumAmountFlag().equals("Y")) {

	 				if (classification.equals("")) {
	 					classification = "Lumpsum Amount";
	 				} else {
	 					classification = classification + ","
	 							+ "Lumpsum Amount";
	 				}
	 			}
	 		}
	         
	         if(docAcknowledgement.getPartialHospitalisationFlag() != null){
	  			
	  			if (docAcknowledgement.getPartialHospitalisationFlag().equals("Y")) {

	  				if (classification.equals("")) {
	  					classification = "Partial Hospitalisation";
	  				} else {
	  					classification = classification + ","
	  							+ "Partial Hospitalisation";
	  				}
	  			}
	  		}
	         
	         if(docAcknowledgement.getOtherBenefitsFlag() != null){
		       		if(docAcknowledgement.getOtherBenefitsFlag().equals("Y")){

		       			if(classification.equals("")){
		       				classification ="Other Benefit";
		       			}
		       			else{
		       			classification =classification+","+" Other Benefit";
		       			}
		       		}
		       	}
	         return classification;
	}
	private Reimbursement getReimbursementObject(String rodNo) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findRodByNumber");
		query = query.setParameter("rodNumber", rodNo);
		List<Reimbursement> reimbursementObjectList = query.getResultList();
		if (null != reimbursementObjectList
				&& !reimbursementObjectList.isEmpty()) {
			//entityManager.refresh(reimbursementObjectList.get(0));
			return reimbursementObjectList.get(0);
		}
		return null;
	}
	
	private Reimbursement getReimbursementObjectByKey(Long rodKey) {
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey");
		query = query.setParameter("primaryKey", rodKey);
		List<Reimbursement> reimbursementObjectList = query.getResultList();
		if (null != reimbursementObjectList
				&& !reimbursementObjectList.isEmpty()) {
			//entityManager.refresh(reimbursementObjectList.get(0));
			return reimbursementObjectList.get(0);
		}
		return null;
	}

     public PreauthDTO getPreauthDTO(Preauth preauth){
		
		
		
		PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//		PreMedicalMapper.getAllMapValues();
		
		PreauthDTO preauthDTO = premedicalMapper.getPreauthDTO(preauth);
	    setpreauthTOPreauthDTO(premedicalMapper, preauth.getClaim(), preauth,
				preauthDTO, false);
	 

		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO
						.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
			 copayValue = dbCalculationService.getProductCoPay(
					preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
							.getKey(), preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
		}
		
		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);
		
		return preauthDTO;
	}
	private DocAcknowledgement getDocAcknowledgement(String acknowledgementNo) {
		
		Query query = entityManager.createNamedQuery("DocAcknowledgement.findAckNumber");
		query = query.setParameter("ackNo", acknowledgementNo);
		List<DocAcknowledgement> docAckList = query.getResultList();
		if(docAckList != null && !docAckList.isEmpty()) {
			return docAckList.get(0);
		}
	return null;
	
	}
	
 	private void getResidualAmount(Preauth previousPreauth,
			PreauthDTO preauthDTO) {
		ResidualAmount residualAmtByPreauthKey = preauthService
				.getLatestResidualAmtByPreauthKey(previousPreauth.getKey());
		if (null != residualAmtByPreauthKey) {
			ResidualAmountDTO residualDTO = new ResidualAmountDTO();
			residualDTO.setKey(residualAmtByPreauthKey.getKey());
			residualDTO.setPreauthKey(previousPreauth.getKey());
			residualDTO.setAmountConsideredAmount(residualAmtByPreauthKey
					.getAmountConsideredAmount());
			residualDTO.setMinimumAmount(residualAmtByPreauthKey.getMinimumAmount());
			residualDTO.setNetAmount(residualAmtByPreauthKey.getNetAmount());
			residualDTO.setApprovedAmount(residualAmtByPreauthKey
					.getApprovedAmount());
			residualDTO.setRemarks(residualAmtByPreauthKey.getRemarks());
			residualDTO.setNetApprovedAmount(residualAmtByPreauthKey.getNetApprovedAmount());
			if(null != preauthDTO.getNewIntimationDTO().getIsJioPolicy() && preauthDTO.getNewIntimationDTO().getIsJioPolicy()){
				if(null != residualAmtByPreauthKey.getCoPayTypeId()){		
					SelectValue copayTypeValue = new SelectValue();
					copayTypeValue.setValue(residualAmtByPreauthKey.getCoPayTypeId().getValue());
					copayTypeValue.setId(residualAmtByPreauthKey.getCoPayTypeId().getKey());
					residualDTO.setCoPayTypeId(copayTypeValue);
				}
			}
			preauthDTO.setResidualAmountDTO(residualDTO);
		}

	}
 	
 	public void setpreauthTOPreauthDTO(PreMedicalMapper premedicalMapper,
			Claim claimByKey, Preauth previousPreauth, PreauthDTO preauthDTO,
			Boolean isEnabled) {
		if (claimByKey != null) {
			setClaimValuesToDTO(preauthDTO, claimByKey);
			NewIntimationDto newIntimationDto = intimationService
					.getIntimationDto(claimByKey.getIntimation());
			ClaimDto claimDTO = ClaimMapper.getInstance().getClaimDto(claimByKey);
			preauthDTO.setNewIntimationDTO(newIntimationDto);
			preauthDTO.setClaimDTO(claimDTO);
		}
		String policyNumber = preauthDTO.getPolicyDto().getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		Policy byPolicyNumber = policyservice.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);

//		List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
//				.getPreviousClaims(claimsByPolicyNumber,                                   used from shautils
//						claimByKey.getClaimId(), pedValidationService,
//						masterService);
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						claimByKey.getClaimId());                                     //used from preauthservice

		// List<PreviousClaimsTableDTO> previousClaimDTOList = new
		// PreviousClaimMapper().getPreviousClaimDTOList(claimsByPolicyNumber);

		preauthDTO.setPreviousClaimsList(previousClaimDTOList);

		if (previousPreauth.getCoordinatorFlag() != null && previousPreauth.getCoordinatorFlag().equalsIgnoreCase("y")) {

			CoordinatorDTO coordinatorDTO = premedicalMapper
					.getCoordinatorDTO(preauthService
							.findCoordinatorByClaimKey(previousPreauth
									.getClaim().getKey()));
			coordinatorDTO.setRefertoCoordinator(true);
			preauthDTO.setCoordinatorDetails(coordinatorDTO);
		}

		List<SpecialityDTO> specialityDTOList = premedicalMapper
				.getSpecialityDTOList(preauthService
						.findSpecialityByClaimKey(previousPreauth.getClaim()
								.getKey()));
		for (SpecialityDTO specialityDTO : specialityDTOList) {
			specialityDTO.setEnableOrDisable(isEnabled);
		}
		preauthDTO.getPreauthDataExtractionDetails().setSpecialityList(
				specialityDTOList);

		List<ProcedureDTO> procedureMainDTOList = premedicalMapper
				.getProcedureMainDTOList(preauthService
						.findProcedureByPreauthKey(previousPreauth.getKey()));
		for (ProcedureDTO procedureDTO : procedureMainDTOList) {
			procedureDTO.setEnableOrDisable(isEnabled);
			if (procedureDTO.getSublimitName() != null) {
				ClaimLimit limit = claimService.getClaimLimitByKey(procedureDTO
						.getSublimitName().getLimitId());
				procedureDTO.setSublimitName(getSublimitFunctionObj(limit));
				procedureDTO.setSublimitDesc(limit.getLimitDescription());
				procedureDTO.setSublimitAmount(limit.getMaxPerPolicyPeriod()
						.toString());
			}
			
			procedureDTO.setNetApprovedAmount(procedureDTO.getNetApprovedAmount());
		}

		preauthDTO.getPreauthMedicalProcessingDetails()
				.setProcedureExclusionCheckTableList(procedureMainDTOList);

		List<PedValidation> findPedValidationByPreauthKey = preauthService
				.findPedValidationByPreauthKey(previousPreauth.getKey());
		List<DiagnosisDetailsTableDTO> newPedValidationTableListDto = premedicalMapper
				.getNewPedValidationTableListDto(findPedValidationByPreauthKey);

		// Fix for issue 732 starts.
		DBCalculationService dbCalculationService = new DBCalculationService();

		Double insuredSumInsured = dbCalculationService.getInsuredSumInsured(
				preauthDTO.getNewIntimationDTO().getInsuredPatient()
						.getInsuredId().toString(), preauthDTO.getPolicyDto()
						.getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());

		Double balanceSI = 0d;
		List<Double> copayValue = new ArrayList<Double>();
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			balanceSI = dbCalculationService.getBalanceSIForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey());
			copayValue = dbCalculationService.getProductCoPayForGMC(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey());
		}else{
			balanceSI = dbCalculationService.getBalanceSI(
					preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),
					preauthDTO.getClaimKey(), insuredSumInsured,
					preauthDTO.getNewIntimationDTO().getKey()).get(
					SHAConstants.TOTAL_BALANCE_SI);
			 copayValue = dbCalculationService.getProductCoPay(
					preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
							.getKey(), preauthDTO.getNewIntimationDTO()
							.getInsuredPatient().getKey(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(),preauthDTO.getNewIntimationDTO());
		}
		preauthDTO.setBalanceSI(balanceSI);
		preauthDTO.setProductCopay(copayValue);

		if (preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()
				.equals(ReferenceTable.SUPER_SURPLUS_INDIVIDUAL)
				|| preauthDTO.getNewIntimationDTO().getPolicy().getProduct()
						.getKey().equals(ReferenceTable.SUPER_SURPLUS_FLOATER)) {
			// preauthDTO.setSpecificProductDeductibleDTO(getPreviousClaimsSuperSurplusTable(claimsByPolicyNumber
			// ,claimByKey.getClaimId(), preauthDTO));
		}

		Map<Long, SublimitFunObject> sublimitFunObjMap = getSublimitFunObjMap(
				preauthDTO.getPolicyDto().getProduct().getKey(),
				insuredSumInsured, preauthDTO.getNewIntimationDTO()
						.getInsuredPatient().getInsuredAge(),preauthDTO);
		// Fix for issue 732 ends

		for (DiagnosisDetailsTableDTO pedValidationTableDTO : newPedValidationTableListDto) {
			pedValidationTableDTO.setEnableOrDisable(isEnabled);
			if (pedValidationTableDTO.getDiagnosisName() != null) {
				
				pedValidationTableDTO.setNetApprovedAmount(pedValidationTableDTO.getNetApprovedAmount());
				
				String diagnosis = masterService
						.getDiagnosis(pedValidationTableDTO.getDiagnosisName()
								.getId());
				pedValidationTableDTO.setDiagnosis(diagnosis);
				pedValidationTableDTO.getDiagnosisName().setValue(diagnosis);
			}

			if (pedValidationTableDTO.getSublimitName() != null) {
				// Fix for issue 732 starts.
				SublimitFunObject objSublimitFun = sublimitFunObjMap
						.get(pedValidationTableDTO.getSublimitName()
								.getLimitId());
				// ClaimLimit limit =
				// claimService.getClaimLimitByKey(pedValidationTableDTO.getSublimitName().getLimitId());
				if (objSublimitFun != null) {
					pedValidationTableDTO.setSublimitName(objSublimitFun);
					pedValidationTableDTO.setSublimitAmt(String
							.valueOf(objSublimitFun.getAmount()));
				}
				// Fix for issue 732 ends
			}

			if (pedValidationTableDTO.getSumInsuredRestriction() != null) {
				MastersValue master = masterService
						.getMaster(pedValidationTableDTO
								.getSumInsuredRestriction().getId());
				pedValidationTableDTO.getSumInsuredRestriction().setValue(
						master.getValue());

			}
			List<DiagnosisPED> pedDiagnosisByPEDValidationKey = preauthService
					.getPEDDiagnosisByPEDValidationKey(pedValidationTableDTO
							.getKey());
			List<PedDetailsTableDTO> dtoList = new ArrayList<PedDetailsTableDTO>();
			for (DiagnosisPED diagnosisPED : pedDiagnosisByPEDValidationKey) {
				PedDetailsTableDTO dto = new PedDetailsTableDTO();
				// Added for disabling the procedure that is coming from
				// preauth.
				dto.setEnableOrDisable(isEnabled);
				dto.setDiagnosisName(pedValidationTableDTO.getDiagnosis());
				dto.setPolicyAgeing(pedValidationTableDTO.getPolicyAgeing());
				dto.setKey(diagnosisPED.getKey());
				dto.setPedCode(diagnosisPED.getPedCode());
				dto.setPedName(diagnosisPED.getPedName());

				if (diagnosisPED.getDiagonsisImpact() != null) {
					SelectValue value = new SelectValue();
					value.setId(diagnosisPED.getDiagonsisImpact().getKey());
					value.setValue(diagnosisPED.getDiagonsisImpact().getValue());
					dto.setPedExclusionImpactOnDiagnosis(value);
				}

				if (diagnosisPED.getExclusionDetails() != null) {
					SelectValue exclusionValue = new SelectValue();
					exclusionValue.setId(diagnosisPED.getExclusionDetails()
							.getKey());
					exclusionValue.setValue(diagnosisPED.getExclusionDetails()
							.getExclusion());
					dto.setExclusionDetails(exclusionValue);
				}

				dto.setRemarks(diagnosisPED.getDiagnosisRemarks());
				dtoList.add(dto);
			}
			pedValidationTableDTO.setPedList(dtoList);
		}
		preauthDTO.getPreauthDataExtractionDetails().setDiagnosisTableList(
				newPedValidationTableListDto);

		// preauthDTO.getPreauthMedicalProcessingDetails().setPedValidationTableList(newPedValidationTableListDto);

		List<ClaimAmountDetails> findClaimAmountDetailsByPreauthKey = preauthService
				.findClaimAmountDetailsByPreauthKey(previousPreauth.getKey());
		Float amountConsider = 0.0f;
		if (findClaimAmountDetailsByPreauthKey != null) {
			for (ClaimAmountDetails claimAmountDetails : findClaimAmountDetailsByPreauthKey) {
                      amountConsider += claimAmountDetails.getPaybleAmount();
			}
		}
		preauthDTO.setAmountConsidered(String.valueOf(amountConsider.intValue()));
		preauthDTO
				.getPreauthDataExtractionDetails()
				.setClaimedDetailsList(
						premedicalMapper
								.getClaimedAmountDetailsDTOList(findClaimAmountDetailsByPreauthKey));
		Integer sumInsured = preauthService.getSumInsured(preauthDTO
				.getPolicyDto().getProduct().getKey(),
				(insuredSumInsured == 0) ? preauthDTO.getPolicyDto()
						.getTotalSumInsured() : insuredSumInsured);
		preauthDTO.getPolicyDto().setInsuredSumInsured(insuredSumInsured);
		String strAutoRestorationFlg = preauthDTO.getNewIntimationDTO()
				.getPolicy().getProduct().getAutoRestorationFlag();

		if (("Y").equalsIgnoreCase(strAutoRestorationFlg)) {
			if (sumInsured != null && sumInsured.intValue() > 0) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(
								SHAConstants.AUTO_RESTORATION_NOTDONE);
			} else if (null != sumInsured && 0 == sumInsured.intValue()) {
				preauthDTO.getPreauthDataExtractionDetails()
						.setAutoRestoration(SHAConstants.AUTO_RESTORATION_DONE);
			}
		} else {
			preauthDTO.getPreauthDataExtractionDetails().setAutoRestoration(
					SHAConstants.AUTO_RESTORATION_NOTAPPLICABLE);
		}

	}
 	
 	
 	
 	private void setClaimValuesToDTO(PreauthDTO preauthDTO, Claim claimByKey) {
		PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey
				.getIntimation().getPolicy());
		preauthDTO.setHospitalKey(claimByKey.getIntimation().getHospital());
		Long hospital = claimByKey.getIntimation().getHospital();
		Hospitals hospitalById = hospitalService.getHospitalById(hospital);
		preauthDTO.setHospitalCode(hospitalById.getHospitalCode());
		preauthDTO.setClaimNumber(claimByKey.getClaimId());
		preauthDTO.setPolicyDto(policyDto);
		preauthDTO.setDateOfAdmission(claimByKey.getIntimation()
				.getAdmissionDate());
		preauthDTO.setReasonForAdmission(claimByKey.getIntimation()
				.getAdmissionReason());
		preauthDTO.setIntimationKey(claimByKey.getIntimation().getKey());
		preauthDTO
				.setPolicyKey(claimByKey.getIntimation().getPolicy().getKey());
		preauthDTO.setClaimKey(claimByKey.getKey());
	}
 	
	private SublimitFunObject getSublimitFunctionObj(ClaimLimit limit) {
		SublimitFunObject obj = new SublimitFunObject();
		obj.setLimitId(limit.getKey());
		obj.setAmount(Double.valueOf(limit.getMaxPerClaimAmount()));
		obj.setName(limit.getLimitName());
		obj.setDescription(limit.getLimitDescription());
		return obj;
	}
	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyservice.getByPolicyNumber(policyNumber);
			List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
			if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
				for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
					if(!generatedList.contains(viewTmpClaim)) {
						generatedList.add(viewTmpClaim);
					}
				}
			}
			if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
				getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
			} else {
				return generatedList;
			}
			
			
		} catch(Exception e) {
			
		}
		return generatedList;
	}
	
	private Map<Long, SublimitFunObject> getSublimitFunObjMap(Long productKey,
			Double insuredSumInsured, Double insuredAge,PreauthDTO preauthDTO) {
		
		Preauth preauth = null;
		
		if(preauthDTO.getKey() != null){
			List<Preauth> preauthList = preauthService.getPreauthByKey(preauthDTO.getKey());
			if(preauthList != null && ! preauthList.isEmpty()){
				preauth = preauthList.get(0);
				}
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<Long, SublimitFunObject> sublimitFunMap = new HashMap<Long, SublimitFunObject>();
		
		List<SublimitFunObject> sublimitList = new ArrayList<SublimitFunObject>();
		
		if(preauth != null && preauth.getSectionCategory() != null){
			
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l, 
							insuredAge,preauth.getSectionCategory(),preauthDTO.getPolicyDto().getPolicyPlan() != null ? preauthDTO.getPolicyDto().getPolicyPlan() : "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
		}else{
			
			sublimitList = dbCalculationService
					.getClaimedAmountDetailsForSection(productKey, insuredSumInsured, 0l, 
							insuredAge,0l, "0", (preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() != null ? preauthDTO.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue() : preauthDTO.getClaimDTO().getClaimSubCoverCode() ),preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId());
			
		}
		
		if (null != sublimitList && !sublimitList.isEmpty()) {
			for (SublimitFunObject sublimitFunObj : sublimitList) {
				sublimitFunMap.put(sublimitFunObj.getLimitId(), sublimitFunObj);
			}
		}
		return sublimitFunMap;

	}
	
	/*public List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues(PreauthDTO preauthDto){
		Double sublimitPackageAmount = 0d;
		List<DiagnosisProcedureTableDTO> diagnosisProcedureValues = new ArrayList<DiagnosisProcedureTableDTO>();
		this.bean = preauthDto;
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {

								exclusionValues(pedDetailsTableDTO
										.getExclusionDetails().getId());

								pedDetailsTableDTO
										.setExclusionAllDetails(this.exlusionContainer
												.getItemIds());

								List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO
										.getExclusionAllDetails();
								String paymentFlag = "y";
								for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
									if (null != pedDetailsTableDTO
											.getExclusionDetails()
											&& exclusionDetails
													.getKey()
													.equals(pedDetailsTableDTO
															.getExclusionDetails()
															.getId())) {
										paymentFlag = exclusionDetails
												.getPaymentFlag();
									}
								}

								if (paymentFlag.toLowerCase().equalsIgnoreCase(
										"n")) {
									isPaymentAvailable = false;
									break;
								}
							}
						}
					}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
					dto.setRestrictionSI("NA");
				} else {
					dto.setRestrictionSI(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSumInsuredRestriction()
									.getValue()).toString());
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSublimitAmt()).toString());
				}
				dto.setPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);

				medicalDecisionDTOList.add(dto);
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setProcedureDTO(procedureDTO);
				if (procedureDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = procedureDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				dto.setRestrictionSI("NA");

				dto.setPackageAmt("NA");

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				medicalDecisionDTOList.add(dto);
			}

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				int diagCount = 0;
				int proCount = 0;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					medicalDecisionDto.setIsEnabled(false);
					// if(medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Diag "+(diag++));
					// caluculationInputValues.put("restrictedSI",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction().getValue()
					// : null);
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey() ==
					// null ? 0l :
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey());
					// caluculationInputValues.put("diagnosisId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId().toString());
					// caluculationInputValues.put("referenceFlag", "D" );
					// } else if(medicalDecisionDto.getProcedureDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Proc " +
					// (proc++));
					// caluculationInputValues.put("restrictedSI", null );
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getProcedureDTO().getSublimitName() !=
					// null ?
					// medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getProcedureDTO().getKey() == null ?
					// 0l : medicalDecisionDto.getProcedureDTO().getKey());
					// caluculationInputValues.put("referenceFlag", "P" );
					// }
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getId()
										: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? 0l
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? "0"
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId()
												.toString());
						caluculationInputValues.put("referenceFlag", "D");
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}

					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId",
										medicalDecisionDto.getProcedureDTO()
												.getProcedureName() == null ? 0l
												: medicalDecisionDto
														.getProcedureDTO()
														.getProcedureName()
														.getId() == null ? 0l
														: medicalDecisionDto
																.getProcedureDTO()
																.getProcedureName()
																.getId());
						caluculationInputValues.put("referenceFlag", "P");
						if (null != this.bean
								.getPreauthMedicalProcessingDetails()
								.getProcedureExclusionCheckTableList()
								&& !this.bean
										.getPreauthMedicalProcessingDetails()
										.getProcedureExclusionCheckTableList()
										.isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthMedicalProcessingDetails()
									.getProcedureExclusionCheckTableList()
									.get(proCount).getAmountConsideredAmount();
							medicalDecisionDto
									.getProcedureDTO()
									.setAmountConsideredAmount(amountConsidered);
							medicalDecisionDto
									.setAmountConsidered(amountConsidered != null ? amountConsidered
											.intValue() : 0);
							
							Double approvedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
							}
							
							Double netApprovedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getNetApprovedAmount();
							medicalDecisionDto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
//							Double minimumAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getMinimumAmount();
//							medicalDecisionDto.getProcedureDTO().setMinimumAmount(minimumAmount);
//							if(minimumAmount != null){
//								sublimitPackageAmount += minimumAmount;
//							medicalDecisionDto.setMinimumAmount(minimumAmount.intValue());
//							}
							
							proCount++;
						}
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}
					}
					caluculationInputValues.put("preauthKey",
							this.bean.getPreviousPreauthKey());
					
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, this.bean.getClaimDTO().getKey());
					
					sumInsuredCalculation(caluculationInputValues);
					Map<String, Object> values = this.sublimitCalculatedValues;

					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					if (values.get("restrictedAvailAmt") != null) {
						medicalDecisionDto.setAvailableAmout(((Double) values
								.get("restrictedAvailAmt")).intValue());
					}
					medicalDecisionDto.setUtilizedAmt(((Double) values
							.get("restrictedUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAmount(((Double) values
							.get("currentSL")).intValue() > 0 ? (String
							.valueOf(((Double) values.get("currentSL"))
									.intValue())) : "NA");
					medicalDecisionDto.setSubLimitUtilAmount(((Double) values
							.get("SLUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
							.get("SLAvailAmt")).intValue());
					medicalDecisionDto
							.setCoPayPercentageValues((List<String>) values
									.get("copay"));

					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.getDiagnosisDetailsDTO()
								.setDiagnosis(this.diagnosisName);
						if (null != this.bean.getPreauthDataExtractionDetails()
								.getDiagnosisTableList()
								&& !this.bean.getPreauthDataExtractionDetails()
										.getDiagnosisTableList().isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthDataExtractionDetails()
									.getDiagnosisTableList().get(diagCount)
									.getAmountConsideredAmount();
							medicalDecisionDto
									.getDiagnosisDetailsDTO()
									.setAmountConsideredAmount(amountConsidered);
							if(amountConsidered != null){
							medicalDecisionDto
									.setAmountConsidered(amountConsidered
											.intValue());
							}
							
							Double approvedAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
								
							}
							
//							Double minimumAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getMinimumAmount();
//							medicalDecisionDto.getDiagnosisDetailsDTO().setMinimumAmount(minimumAmount);
//							if(minimumAmount != null){
//							sublimitPackageAmount += minimumAmount;
//							medicalDecisionDto.setMinimumAmount(minimumAmount.intValue());
//							}
							diagCount++;
						}
					}

					// need to implement in new medical listener table
					medicalDecisionDto.setIsEnabled(false);
//					this.diagnosisProcedureValues
//							.addBeanToList(medicalDecisionDto);
					diagnosisProcedureValues.add(medicalDecisionDto);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				if (null != this.bean.getResidualAmountDTO()) {
					if (null != this.bean.getResidualAmountDTO()
							.getAmountConsideredAmount()) {
						dto.setAmountConsidered(this.bean
								.getResidualAmountDTO()
								.getAmountConsideredAmount().intValue());
						dto.setMinimumAmount(this.bean.getResidualAmountDTO().getMinimumAmount().intValue());
						if(this.bean.getResidualAmountDTO().getMinimumAmount() != null){
						sublimitPackageAmount += this.bean.getResidualAmountDTO().getMinimumAmount();
						}
					}
				}
				dto.setIsEnabled(false);
//				this.diagnosisListenerTableObj.addBeanToList(dto);
				diagnosisProcedureValues.add(dto);
			}
		} else {
//			this.diagnosisListenerTableObj.addList(filledDTO);
		}
		
		this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(sublimitPackageAmount);
		
		 
		 return diagnosisProcedureValues;
		
	}*/
	
	public List<DiagnosisProcedureTableDTO> setValueToMedicalDecisionValues(PreauthDTO preauthDto){
		Double sublimitPackageAmount = 0d;
		List<DiagnosisProcedureTableDTO> diagnosisProcedureValues = new ArrayList<DiagnosisProcedureTableDTO>();
		this.bean = preauthDto;
		List<DiagnosisProcedureTableDTO> filledDTO = this.bean
				.getPreauthMedicalDecisionDetails()
				.getMedicalDecisionTableDTO();

		List<DiagnosisProcedureTableDTO> medicalDecisionDTOList = new ArrayList<DiagnosisProcedureTableDTO>();
		if (filledDTO.isEmpty()) {
			List<DiagnosisDetailsTableDTO> pedValidationTableList = this.bean
					.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO pedValidationTableDTO : pedValidationTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				if (pedValidationTableDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = pedValidationTableDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (isPaymentAvailable) {
						List<PedDetailsTableDTO> pedList = pedValidationTableDTO
								.getPedList();
						if (!pedList.isEmpty()) {
							for (PedDetailsTableDTO pedDetailsTableDTO : pedList) {
								if (pedDetailsTableDTO.getExclusionDetails() != null) {
									exclusionValues(pedDetailsTableDTO
										.getExclusionDetails().getId());

									pedDetailsTableDTO.setExclusionAllDetails(this.exlusionContainer
											.getItemIds());

									List<ExclusionDetails> exclusionAllDetails = pedDetailsTableDTO.getExclusionAllDetails();
									String paymentFlag = "y";
									for (ExclusionDetails exclusionDetails : exclusionAllDetails) {
										if (null != pedDetailsTableDTO
												.getExclusionDetails()
												&& exclusionDetails
														.getKey()
														.equals(pedDetailsTableDTO
																.getExclusionDetails()
																.getId())) {
											paymentFlag = exclusionDetails
													.getPaymentFlag();
										}
									}

									if (paymentFlag.toLowerCase().equalsIgnoreCase(
											"n")) {
										isPaymentAvailable = false;
										break;
									}
								}
							}
						}
					}

					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				if (pedValidationTableDTO.getSumInsuredRestriction() == null) {
					dto.setRestrictionSI("NA");
				} else {
					dto.setRestrictionSI(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSumInsuredRestriction()
									.getValue()).toString());
				}

				if (pedValidationTableDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							pedValidationTableDTO.getSublimitAmt()).toString());
				}
				dto.setPackageAmt("NA");
				dto.setDiagnosisDetailsDTO(pedValidationTableDTO);

				medicalDecisionDTOList.add(dto);
			}
			List<ProcedureDTO> procedureExclusionCheckTableList = this.bean
					.getPreauthMedicalProcessingDetails()
					.getProcedureExclusionCheckTableList();
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setProcedureDTO(procedureDTO);
				if (procedureDTO.getConsiderForPaymentFlag() != null) {
					Boolean isPaymentAvailable = procedureDTO
							.getConsiderForPaymentFlag().toLowerCase()
							.equalsIgnoreCase("y") ? true : false;
					if (!isPaymentAvailable) {
						dto.setMinimumAmount(0);
						dto.setConsiderForPayment("No");
					} else {
						dto.setConsiderForPayment("Yes");
					}
					dto.setIsPaymentAvailable(isPaymentAvailable);
				}
				dto.setRestrictionSI("NA");

				dto.setPackageAmt("NA");

				if (procedureDTO.getSublimitName() == null) {
					dto.setSubLimitAmount("NA");
				} else {
					dto.setSubLimitAmount(SHAUtils.getIntegerFromString(
							procedureDTO.getSublimitAmount()).toString());
				}
				medicalDecisionDTOList.add(dto);
			}

			Map<String, Object> caluculationInputValues = new WeakHashMap<String, Object>();
			caluculationInputValues.put("policyNumber", this.bean
					.getPolicyDto().getPolicyNumber());
			// caluculationInputValues.put("insuredId",
			// this.bean.getPolicyDto().getInsuredId());
			caluculationInputValues.put("insuredId", this.bean
					.getNewIntimationDTO().getInsuredPatient().getInsuredId());

			DBCalculationService dbCalculationService = new DBCalculationService();
			Double insuredSumInsured = dbCalculationService
					.getInsuredSumInsured(this.bean.getNewIntimationDTO()
							.getInsuredPatient().getInsuredId().toString(),
							this.bean.getPolicyDto().getKey(),this.bean.getNewIntimationDTO()
							.getInsuredPatient().getLopFlag());
			caluculationInputValues.put(
					"policySI",
					insuredSumInsured != 0 ? String.valueOf(insuredSumInsured)
							: String.valueOf(this.bean.getPolicyDto()
									.getTotalSumInsured()));

			if (null != medicalDecisionDTOList
					&& medicalDecisionDTOList.size() > 0) {
				int diag = 1;
				int proc = 1;
				int diagCount = 0;
				int proCount = 0;
				for (int i = 0; i < medicalDecisionDTOList.size(); i++) {
					DiagnosisProcedureTableDTO medicalDecisionDto = medicalDecisionDTOList
							.get(i);
					medicalDecisionDto.setIsEnabled(false);
					// if(medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Diag "+(diag++));
					// caluculationInputValues.put("restrictedSI",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSumInsuredRestriction().getValue()
					// : null);
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName()
					// != null ?
					// medicalDecisionDto.getDiagnosisDetailsDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey() ==
					// null ? 0l :
					// medicalDecisionDto.getDiagnosisDetailsDTO().getKey());
					// caluculationInputValues.put("diagnosisId",
					// medicalDecisionDto.getDiagnosisDetailsDTO().getDiagnosisName().getId().toString());
					// caluculationInputValues.put("referenceFlag", "D" );
					// } else if(medicalDecisionDto.getProcedureDTO() != null) {
					// medicalDecisionDto.setDiagOrProcedure("Proc " +
					// (proc++));
					// caluculationInputValues.put("restrictedSI", null );
					// caluculationInputValues.put("sublimitId",
					// medicalDecisionDto.getProcedureDTO().getSublimitName() !=
					// null ?
					// medicalDecisionDto.getProcedureDTO().getSublimitName().getLimitId()
					// : null);
					// caluculationInputValues.put("diagOrProcId",
					// medicalDecisionDto.getProcedureDTO().getKey() == null ?
					// 0l : medicalDecisionDto.getProcedureDTO().getKey());
					// caluculationInputValues.put("referenceFlag", "P" );
					// }
					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Diag "
								+ (diag++));
						caluculationInputValues
								.put("restrictedSI",
										medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getId()
										: null);
						caluculationInputValues
						.put("restrictedSIAmount",
								medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction() != null ? medicalDecisionDto
										.getDiagnosisDetailsDTO()
										.getSumInsuredRestriction().getValue()
										: null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues.put("diagOrProcId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? 0l
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId());
						caluculationInputValues.put("diagnosisId",
								medicalDecisionDto.getDiagnosisDetailsDTO()
										.getDiagnosisName() == null ? "0"
										: medicalDecisionDto
												.getDiagnosisDetailsDTO()
												.getDiagnosisName().getId()
												.toString());
						caluculationInputValues.put("referenceFlag", "D");
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}

					} else if (medicalDecisionDto.getProcedureDTO() != null) {
						medicalDecisionDto.setDiagOrProcedure("Proc "
								+ (proc++));
						caluculationInputValues.put("restrictedSI", null);
						caluculationInputValues.put("restrictedSIAmount", null);
						caluculationInputValues
								.put("sublimitId",
										medicalDecisionDto.getProcedureDTO()
												.getSublimitName() != null ? medicalDecisionDto
												.getProcedureDTO()
												.getSublimitName().getLimitId()
												: null);
						caluculationInputValues
								.put("diagOrProcId",
										medicalDecisionDto.getProcedureDTO()
												.getProcedureName() == null ? 0l
												: medicalDecisionDto
														.getProcedureDTO()
														.getProcedureName()
														.getId() == null ? 0l
														: medicalDecisionDto
																.getProcedureDTO()
																.getProcedureName()
																.getId());
						caluculationInputValues.put("referenceFlag", "P");
						if (null != this.bean
								.getPreauthMedicalProcessingDetails()
								.getProcedureExclusionCheckTableList()
								&& !this.bean
										.getPreauthMedicalProcessingDetails()
										.getProcedureExclusionCheckTableList()
										.isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthMedicalProcessingDetails()
									.getProcedureExclusionCheckTableList()
									.get(proCount).getAmountConsideredAmount();
							medicalDecisionDto
									.getProcedureDTO()
									.setAmountConsideredAmount(amountConsidered);
							medicalDecisionDto
									.setAmountConsidered(amountConsidered != null ? amountConsidered
											.intValue() : 0);
							
							Double approvedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
							}
							
							Double netApprovedAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getNetApprovedAmount();
							medicalDecisionDto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
							
							//////copay amount
							Double copayAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getCopayAmount();
							if(copayAmount != null){
							medicalDecisionDto.setCoPayAmount(copayAmount.intValue());
							}
							
							Double copayPercentage = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getCopayPercentage();
							if(copayPercentage != null){
								medicalDecisionDto.setCopayPercentageAmount(copayPercentage.doubleValue());
							}
							Double netAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getNetAmount();

							if(netAmount != null){
							medicalDecisionDto.setNetAmount(netAmount.intValue());
							}

							
//							Double minimumAmount = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList().get(proCount).getMinimumAmount();
//							medicalDecisionDto.getProcedureDTO().setMinimumAmount(minimumAmount);
//							if(minimumAmount != null){
//								sublimitPackageAmount += minimumAmount;
//							medicalDecisionDto.setMinimumAmount(minimumAmount.intValue());
//							}
							
							proCount++;
						}
						if (caluculationInputValues.get("sublimitId") != null) {
							Long sublimitId = (Long) caluculationInputValues
									.get("sublimitId");
							ClaimLimit claimLimit = diagnosisService
									.getClaimLimit(sublimitId);
							medicalDecisionDto.setSublimitName(claimLimit
									.getLimitName());
							medicalDecisionDto.setSublimitApplicable("Yes");
						} else {
							medicalDecisionDto.setSublimitApplicable("No");
						}
					}
					caluculationInputValues.put("preauthKey",
							this.bean.getPreviousPreauthKey());
					
					caluculationInputValues.put(SHAConstants.CLAIM_KEY, this.bean.getClaimDTO().getKey());
					
					sumInsuredCalculation(caluculationInputValues);
					Map<String, Object> values = this.sublimitCalculatedValues;

					medicalDecisionDto.setRestrictionSI(caluculationInputValues
							.get("restrictedSIAmount") != null ? SHAUtils
							.getIntegerFromString(
									(String) caluculationInputValues
											.get("restrictedSIAmount")).toString()
							: "NA");
					if (values.get("restrictedAvailAmt") != null) {
						medicalDecisionDto.setAvailableAmout(((Double) values
								.get("restrictedAvailAmt")).intValue());
					}
					medicalDecisionDto.setUtilizedAmt(((Double) values
							.get("restrictedUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAmount(((Double) values
							.get("currentSL")).intValue() > 0 ? (String
							.valueOf(((Double) values.get("currentSL"))
									.intValue())) : "NA");
					medicalDecisionDto.setSubLimitUtilAmount(((Double) values
							.get("SLUtilAmt")).intValue());
					medicalDecisionDto.setSubLimitAvaliableAmt(((Double) values
							.get("SLAvailAmt")).intValue());
					medicalDecisionDto
							.setCoPayPercentageValues((List<String>) values
									.get("copay"));

					if (medicalDecisionDto.getDiagnosisDetailsDTO() != null) {
						medicalDecisionDto.getDiagnosisDetailsDTO()
								.setDiagnosis(this.diagnosisName);
						if (null != this.bean.getPreauthDataExtractionDetails()
								.getDiagnosisTableList()
								&& !this.bean.getPreauthDataExtractionDetails()
										.getDiagnosisTableList().isEmpty()) {
							Double amountConsidered = this.bean
									.getPreauthDataExtractionDetails()
									.getDiagnosisTableList().get(diagCount)
									.getAmountConsideredAmount();
							medicalDecisionDto
									.getDiagnosisDetailsDTO()
									.setAmountConsideredAmount(amountConsidered);
							if(amountConsidered != null){
							medicalDecisionDto
									.setAmountConsidered(amountConsidered
											.intValue());
							}
							
							Double approvedAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getApprovedAmount();
							if(approvedAmount != null){
								medicalDecisionDto.setMinimumAmount(approvedAmount.intValue());
								sublimitPackageAmount += approvedAmount;
								
							}
							
							Double netApprovedAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getNetApprovedAmount();
							medicalDecisionDto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
							
							
							Double copayAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getCopayAmount();
							if(copayAmount != null){
							medicalDecisionDto.setCoPayAmount(copayAmount.intValue());
							
							}
							
							Double copayPercentage = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getCopayPercentage();
							if(copayPercentage != null){
								medicalDecisionDto.setCopayPercentageAmount(copayPercentage.doubleValue());
							}
							
							////////netamount After Copay f
							Double netAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getNetAmount();
							if(netAmount != null){
							medicalDecisionDto.setNetAmount(netAmount.intValue());
							}
//							Double minimumAmount = this.bean.getPreauthDataExtractionDetails().getDiagnosisTableList().get(diagCount).getMinimumAmount();
//							medicalDecisionDto.getDiagnosisDetailsDTO().setMinimumAmount(minimumAmount);
//							if(minimumAmount != null){
//							sublimitPackageAmount += minimumAmount;
//							medicalDecisionDto.setMinimumAmount(minimumAmount.intValue());
//							}
							diagCount++;
						}
					}

					// need to implement in new medical listener table
					medicalDecisionDto.setIsEnabled(false);
//					this.diagnosisProcedureValues
//							.addBeanToList(medicalDecisionDto);
					diagnosisProcedureValues.add(medicalDecisionDto);
				}
				DiagnosisProcedureTableDTO dto = new DiagnosisProcedureTableDTO();
				dto.setDiagOrProcedure("Residual Treatment / Procedure Amount");
				dto.setPackageAmt("NA");
				dto.setSubLimitAmount("NA");
				dto.setRestrictionSI("NA");
				if (null != this.bean.getResidualAmountDTO()) {
					if (null != this.bean.getResidualAmountDTO()
							.getAmountConsideredAmount()) {
						dto.setAmountConsidered(this.bean
								.getResidualAmountDTO()
								.getAmountConsideredAmount().intValue());
						dto.setMinimumAmount(this.bean.getResidualAmountDTO().getMinimumAmount().intValue());
						if(this.bean.getResidualAmountDTO().getMinimumAmount() != null){
						sublimitPackageAmount += this.bean.getResidualAmountDTO().getMinimumAmount();
						}
						
						Double netApprovedAmount = this.bean.getResidualAmountDTO().getNetApprovedAmount();
						
						dto.setReverseAllocatedAmt(netApprovedAmount != null ? netApprovedAmount.intValue() : 0);
						if(this.bean.getResidualAmountDTO().getNetAmount() != null){
							dto.setNetAmount(this.bean.getResidualAmountDTO().getNetAmount().intValue());
						/*	Double netAmount=this.bean.getResidualAmountDTO().getNetAmount();
							dto.setNetAmount(netAmount != null ? netAmount.intValue() : 0);*/
						
					}
				    ///copay amount calculation///
					if(this.bean.getResidualAmountDTO().getCopayAmount() != null){
						dto.setCoPayAmount(this.bean.getResidualAmountDTO().getCopayAmount().intValue());
						}
						Double copayPercentage = this.bean.getResidualAmountDTO().getCopayPercentage();
					if(copayPercentage != null){
						dto.setCopayPercentageAmount(copayPercentage.doubleValue());
				}
					///////// net amount after copay f
					/*if(this.bean.getResidualAmountDTO().getNetAmount() != null){
						dto.setNetAmount(this.bean.getResidualAmountDTO().getNetAmount().intValue());
					}*/
				}
				/*dto.setIsEnabled(false);
//				this.diagnosisListenerTableObj.addBeanToList(dto);
				diagnosisProcedureValues.add(dto);*/
				}dto.setIsEnabled(false);
//				this.diagnosisListenerTableObj.addBeanToList(dto);
				diagnosisProcedureValues.add(dto);
			}
		} 
		else {
//			this.diagnosisListenerTableObj.addList(filledDTO);
		}
		
		this.bean.getPreauthMedicalDecisionDetails().setInitialApprovedAmt(sublimitPackageAmount);
		
		 
		 return diagnosisProcedureValues;
		
	}
	
	public void exclusionValues(Long impactDiagnosisKey){
		
		BeanItemContainer<ExclusionDetails> icdCodeContainer = masterService.getExclusionDetailsByImpactKey(impactDiagnosisKey);
		this.exlusionContainer=icdCodeContainer;
		
		
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
				
				return paymentDetailsList;
			}
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return null;
	}
	public void sumInsuredCalculation(Map<String, Object> values){
		
		String diagnosis = null;
		if(values.containsKey("diagnosisId")) {
			diagnosis = masterService.getDiagnosis(Long.valueOf((String) values.get("diagnosisId")));
		}
		
		Map<String, Object> medicalDecisionTableValue = dbCalculationService.getMedicalDecisionTableValue(values,bean.getNewIntimationDTO());
		this.diagnosisName = diagnosis;
		this.sublimitCalculatedValues = medicalDecisionTableValue;
		
   }
	
	private PreauthDTO getProrataFlagFromProduct(PreauthDTO preauthDTO)
	{
		Product product = masterService.getProrataForProduct(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		if(null != product)
		{
			preauthDTO.setProrataDeductionFlag(null != product.getProrataFlag() ? product.getProrataFlag() : null);
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			preauthDTO.setProductBasedProRata(null != product.getProrataFlag() ? product.getProrataFlag() : null);
			preauthDTO.setProductBasedPackage(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : null);
			//ends.
			preauthDTO.setPackageAvailableFlag(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : null);
		}
		return preauthDTO;
	}


	public List<ClaimPayment> getPaymentDetailsByClaimNumberForView(String claimNumber)
	{
		List<ClaimPayment> paymentDetailsList = new ArrayList<ClaimPayment>();
		if(claimNumber != null){
		Query findByPaymentKey = entityManager.createNamedQuery(
				"ClaimPayment.findByClaimNumber").setParameter("claimNumber", claimNumber);
		try{
			paymentDetailsList = (List<ClaimPayment>) findByPaymentKey.getResultList();
			
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		}
		return paymentDetailsList;
	}
//  @SuppressWarnings("unused")
//private String doPostPremeia(HttpServletRequest request, HttpServletResponse response1) throws IOException{
//	  
//	  String url = "http://starhealth.in/claimstatus.php";
//		URL obj = new URL(url);
//		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//		String intimationNumber = (String) request.getParameter("intimationNumber");
//		  String healthCardNo = (String) request.getParameter("idCardNumber");
//		//add reuqest header
//		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);
//		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//		con.setRequestProperty("intimationnumber", intimationNumber);
//		con.setRequestProperty("idcardnumber", healthCardNo);
//		
//		String urlParameters = "intimationnumber=C02G8416DRJM&idcardnumber=gfhty546";
//
//		// Send post request
//		con.setDoOutput(true);
//		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//		wr.writeBytes(urlParameters);
//		wr.flush();
//		wr.close();
//
//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + urlParameters);
//		System.out.println("Response Code : " + responseCode);
//
//		BufferedReader in = new BufferedReader(agent
//		        new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
//
//		//print result
//		System.out.println(response.toString());
//		 PrintWriter out = response1.getWriter();
//	  	  out.println (response.toString());  
//
//	
//	  
//	return null;
//	  
//  }
	private JSONObject getToken(String token){
		
		JSONObject jSONObject = null;
		jSONObject = preauthService.validateToken(token);
		if(jSONObject != null) {
			String requestJsonInString = jSONObject.toJSONString();
			System.out.println("requestJsonInString = " + requestJsonInString);
			
		}
		return jSONObject;
	}
	
	
	private void showClaimPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			String daignosisName = "";
		  String error = "";
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Reimbursement remibNo = null;
		  Policy policy = null;
		  Hospitals hospitals = null;
		  ViewRejectionDTO rejectioDto = null;
		  List<ViewQueryDTO> viewQueryDTOvalues = null;
		  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
		  
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		  if(!intimationNumber.isEmpty()){
			 
//			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
			 
			 /* if(null == intimationByNo && intimationNumber == null){
				  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
			  	}*/
			  
			 
			  if(error.isEmpty()){
				  if(intimationByNo != null) {
				  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
				  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
				  if(claimforIntimation != null) {
					  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
								.getKey());
						if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
							preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
							
							if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){		
								preauth.setTotalApprovalAmount(0d);		
							}
						}
				    }
				  }
				  
			  }
			  
		 
		
		  if(null != preauth){
			  
			 
			  
			  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
		  
			  int i=1;
			  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
				  if(diagnosis.size() == i){
					  daignosisName += pedDetailsTableDTO.getDiagnosisName();
				  } else {
					  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
				  }

				  i++;
			  }
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("preauth", preauth);
		  }
		  
		  if(null != claimforIntimation){
			  
			  int snoPreauthSummary = 1;
			  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.searchFromTmpPreauth(claimforIntimation.getKey());
			  
				List<Long> keys = new ArrayList<Long>();
				if(null != previousPreauthList && !previousPreauthList.isEmpty()){
					for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
						keys.add(previousPreAuthTableDTO.getKey());
					}
				}
			  
			  ViewTmpPreauth prevpreauth = null;
				Long preauthKey = null;
				Double reqAmount=new Double(0);
				Double approvedAmount=new Double(0);
				if (!keys.isEmpty()) {
					preauthKey = Collections.max(keys);

					prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
				}
				
			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				previousPreAuthTableDTO.setSno(snoPreauthSummary);
				snoPreauthSummary++;
				previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
				reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
				approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
				/*Negotiation Details */
				if(previousPreAuthTableDTO.getKey() !=null){
					NegotiationAmountDetails negoDetails = preauthService.getNegotiationAmountDetailsByTransactionKey(previousPreAuthTableDTO.getKey());
					if(negoDetails !=null
							&& negoDetails.getNegotiationWith() !=null){
						previousPreAuthTableDTO.setNegotiatedWith(negoDetails.getNegotiationWith());
					}
				}
				newList.add(previousPreAuthTableDTO);

			}
			
			request.setAttribute("cashless", newList);
			if(previousPreauthList != null) {
			request.setAttribute("requestedAmt", reqAmount);
			request.setAttribute("totalApprovedAmount", approvedAmount);
			}
		  }
		  
		  if(rodNUmber != null) {
				 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
				  rejectioDto = new ViewRejectionDTO();
				 if(reimbursementKey != null) {
				 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
				 
				 if(rejectionKeyByReimbursement != null) {
					rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
					rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
					String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
					String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
					//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//					rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
					rejectioDto.setRejectedDate(rejectDate);
					String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
					rejectioDto.setApproveRejectionDate(approverejectDate);
					rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
					rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
					rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
					
				 }
				
				 }
				 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
				 request.setAttribute("rejectList",rejectioDto);
				}
		  
		  if(null != claimforIntimation){
			  
			 request.setAttribute("claim", claimforIntimation);
			  
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
			 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
			 
			 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
			 
			 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
			 
			 
			 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
				 
				 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
				 
				 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
				 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
				 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//				 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
				 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
				 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
				 }
				 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
				 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
				 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
				 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
				 tableDTO.setStatus(docAcknowledgement.getStatus());
				 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
				 tableDTO.setTypeOfClaim(claimforIntimation.getClaimType().getValue());
			
				 tableDTO.setAmount(docAcknowledgement.getApprovedAmount());
				 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
					 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
					 }
				 
				/* Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
				 tableDTO.setAmount(Double.valueOf(docAcknowledgement.getApprovedAmount()));*/
				 
				 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
				 String ackToken = null;
				  try {
					  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  tableDTO.setAcknowledgementToken(ackToken);
				
				 docAcknowledgementList.add(tableDTO);
			
			 }
			 	
				 request.setAttribute("reimbursementList", docAcknowledgementList);
		 
				 if(rodNUmber != null) {
					 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
					 
					 if(reimbursementKey != null){
						 
						 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
						 Hospitals hospitalDetails=null; 
						 
						 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
						 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
						 //need to implement
						 if(claimforIntimation != null){
							 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
							 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
						 }	
						 
						 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
							 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
							 viewQueryDTO.setQueryDate(rejectDate);
							 viewQueryDTO.setDiagnosis(diagnosisName);
							 
							 if(hospitalDetails != null){
								 viewQueryDTO.setHospitalName(hospitalDetails.getName());
								 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
								 viewQueryDTOvalues.add(viewQueryDTO);
							 }
//							viewQueryDTO.setClaim(claimforIntimation);
							 
							 
						 }
						 request.setAttribute("queryList", viewQueryDTOvalues);
					 }
					 
					
				 }
				 
			  
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claimforIntimation.getKey(), reimkey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 
			 List<ViewClaimHistoryDTO> trailsList = new ArrayList<ViewClaimHistoryDTO>();
			 for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
				 
				 ViewClaimHistoryDTO viewHistoryDto = new ViewClaimHistoryDTO();
				 
				 viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setReimbursementKey(viewClaimHistoryDTO.getReimbursementKey());
				 viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
				 viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
				 viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
				 viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
				 trailsList.add(viewHistoryDto);
			}
			 
			 request.setAttribute("viewtrailsList", trailsList);
			 
			 
			 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
			 
			 
			
			 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
			 
			 //Financial List
			 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
						.getViewTmpRimbursementDetails(claimforIntimation.getKey());

				List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
				List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
				if (reimbursementDetails != null) {
					int i =1;
					int snoFinance = 1;
					for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
						if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
							
							if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
			                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
			                 
			                 dto.setSno(i);
			                 i++;
			                 if(null != reimbursement.getClaim()){
			                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
			                 }
			//                 if(null != reimbursement.getBillingApprovedAmount()){
			//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			//                 }
			                 
			                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
							}
			//                 if(reimbursement.getBillingCompletedDate() != null){
			                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
			                 if(reimbursement.getBillingApprovedAmount() != null){
			                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			                 }
			//                 }
			                 dto.setStatus(reimbursement.getStatus().getProcessValue());
			                 dto.setRodKey(reimbursement.getKey());
			                 Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", intimationNumber);
							 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
							 String billDtlsToken = null;
							  try {
								  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
							  } catch (NoSuchAlgorithmException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  } catch (ParseException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  }
							  tokenInputs = null;
							  dto.setBillDtslToken(billDtlsToken);
			                 billDetails.add(dto);
							}
							
							if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
								FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
								dto1.setSno(snoFinance);
								snoFinance++;
								dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
								Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto1.setAmount(String.valueOf(approvedAmount));
							     }
			//					if(reimbursement.getFinancialCompletedDate() != null){
								dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
								
								if(reimbursement.getFinancialApprovedAmount() != null){
									dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
								}
								
			//					}
								dto1.setStatus(reimbursement.getStatus().getProcessValue());
				                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
				                dto1.setRodKey(reimbursement.getKey());
				                
				                if(reimbursement.getDocAcknowLedgement() != null && 
				                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
				                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
				                }
				                
				                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
				                {
				                	ClaimPayment claimPayment;	
				                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//				                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
				                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
				                	
				                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
				                	{
				                		claimPayment = claimPaymetList.get(0);
				                		dto1.setPaymentType(claimPayment.getPaymentType());
				                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
				                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
				                		
				                	}
				                	
				                }
				                
				                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
				                	dto1.setAmount("0");
				                }
				                
//				                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
				                financialDetails.add(dto1);
							}
						}
					}
				}
				 
				request.setAttribute("billing", billDetails);
				request.setAttribute("financial", financialDetails);
				
				/* Payment Details */
				 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
				 	
				 	
				 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
					
					List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
					
					List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
					int snum =1;
					for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
						if(paymentProcessCpuTableDTO2.getChequeDate() != null){
							paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
							
						}
						paymentProcessCpuTableDTO2.setSerialNo(snum);
						paymentList.add(paymentProcessCpuTableDTO2);
						snum++;
					}
					
					request.setAttribute("payment", paymentList);
				 
			 }
		  	BPMClientContext bpmClientContext = new BPMClientContext();
		  	 if(null != claimforIntimation){
		  		Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
				 String intimationNoToken = null;
				  try {
					  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
				/*Below code commented due to security issue
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
				//getUI().getPage().open(url, "_blank");
				request.setAttribute("dmsUrl",url);
		  	 }
			String queryString = request.getQueryString();
			String rodNo = request.getParameter("reimb_num");
			String cashlessNo = request.getParameter("cashless_num");
			
			if(!error.isEmpty()){
				  request.setAttribute("error", error);
				  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
			  } 
			  else {
				  request.setAttribute("intimation", intimationByNo);
				  request.setAttribute("hospitals", hospitals);
				  request.getRequestDispatcher("/WEB-INF/ClaimStatusPage.jsp").include(request, response);
				  }
		  }
//		  else{
//			  if(intimationNumber.isEmpty()){
//				  error += "Please Enter Intimation Number";
//			  }
////		      else if(healthCardNo.isEmpty()){
////		    	  error += "Please Enter  Health Card Number";
////			  }
////		      else if(intimationNumber.isEmpty()){
////				  error += "Please Enter Intimation Number";
////				  
////			  }
//				 request.setAttribute("error", error);
//				 request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
//		  }
	}
	private void showAgentPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String daignosisName = "";
		  String error = "";
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Reimbursement remibNo = null;
		  Policy policy = null;
		  Hospitals hospitals = null;
		  ViewRejectionDTO rejectioDto = null;
		  List<ViewQueryDTO> viewQueryDTOvalues = null;
		  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
		  
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		  if(!intimationNumber.isEmpty()){
			 
//			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
			 
			 /* if(null == intimationByNo && intimationNumber == null){
				  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
			  	}*/
			  
			 
			  if(error.isEmpty()){
				  if(intimationByNo != null) {
				  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
				  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
				  if(claimforIntimation != null) {
					  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
								.getKey());
						if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
							preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
							
							if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){		
								preauth.setTotalApprovalAmount(0d);		
							
							}
						}
				    }
				  }
				  
			  }
			  
		 
		
		  if(null != preauth){
			  
			 
			  
			  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
		  
			  int i=1;
			  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
				  if(diagnosis.size() == i){
					  daignosisName += pedDetailsTableDTO.getDiagnosisName();
				  } else {
					  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
				  }

				  i++;
			  }
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("preauth", preauth);
		  }
		  
		  if(null != claimforIntimation){
			  Integer snoPreauthSummary=1;
			  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.searchFromTmpPreauth(claimforIntimation.getKey());
			  
				List<Long> keys = new ArrayList<Long>();
				if(null != previousPreauthList && !previousPreauthList.isEmpty()){
					for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
						keys.add(previousPreAuthTableDTO.getKey());
					}
				}
			  
			  ViewTmpPreauth prevpreauth = null;
				Long preauthKey = null;
				Double reqAmount=new Double(0);
				Double approvedAmount=new Double(0);
				if (!keys.isEmpty()) {
					preauthKey = Collections.max(keys);

					prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
				}
				
			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				previousPreAuthTableDTO.setSno(snoPreauthSummary);
				snoPreauthSummary++;
				previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
				reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
				approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
				newList.add(previousPreAuthTableDTO);

			}
			
			request.setAttribute("cashless", newList);
			if(previousPreauthList != null) {
			request.setAttribute("requestedAmt", reqAmount);
			request.setAttribute("totalApprovedAmount", approvedAmount);
			}
		  }
		  
		  if(rodNUmber != null) {
				 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
				  rejectioDto = new ViewRejectionDTO();
				 if(reimbursementKey != null) {
				 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
				 
				 if(rejectionKeyByReimbursement != null) {
					rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
					rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
					String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
					String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
					//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//					rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
					rejectioDto.setRejectedDate(rejectDate);
					String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
					rejectioDto.setApproveRejectionDate(approverejectDate);
					rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
					rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
					rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
					
				 }
				
				 }
				 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
				 request.setAttribute("rejectList",rejectioDto);
				}
		  
		  if(null != claimforIntimation){
			  
			 request.setAttribute("claim", claimforIntimation);
			  
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
			 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
			 
			 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
			 
			 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
			 
			 
			 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
				 
				 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
				 
				 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
				 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
				 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//				 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
				 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
				 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
				 }
				 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
				 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
				 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
				 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
				 tableDTO.setStatus(docAcknowledgement.getPortalStatusVal());
				 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
				 tableDTO.setTypeOfClaim(claimforIntimation.getClaimType().getValue());
				 tableDTO.setAmount(docAcknowledgement.getApprovedAmount());
				 
				 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
					 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
					 }
				 
				/* Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
				 tableDTO.setAmount(Double.valueOf(approvedAmount));*/
				 
				 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
				 
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
				 String ackToken = null;
				  try {
					  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  tableDTO.setAcknowledgementToken(ackToken);

				 docAcknowledgementList.add(tableDTO);
			
			 }
			 	
				 request.setAttribute("reimbursementList", docAcknowledgementList);
		 
				 if(rodNUmber != null) {
					 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
					 
					 if(reimbursementKey != null){
						 
						 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
						 Hospitals hospitalDetails=null; 
						 
						 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
						 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
						 //need to implement
						 if(claimforIntimation != null){
							 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
							 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
						 }	
						 
						 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
							 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
							 viewQueryDTO.setQueryDate(rejectDate);
							 viewQueryDTO.setDiagnosis(diagnosisName);
							 
							 if(hospitalDetails != null){
								 viewQueryDTO.setHospitalName(hospitalDetails.getName());
								 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
								 viewQueryDTOvalues.add(viewQueryDTO);
							 }
//							viewQueryDTO.setClaim(claimforIntimation);
							 
							 
						 }
						 request.setAttribute("queryList", viewQueryDTOvalues);
					 }
					 
					
				 }
				 
			  
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claimforIntimation.getKey(), reimkey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 
			 List<ViewClaimHistoryDTO> trailsList = new ArrayList<ViewClaimHistoryDTO>();
			 for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
				 
				 ViewClaimHistoryDTO viewHistoryDto = new ViewClaimHistoryDTO();
				 
				 viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setReimbursementKey(viewClaimHistoryDTO.getReimbursementKey());
				 viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
				 viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
				 viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
				 viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
				 trailsList.add(viewHistoryDto);
			}
			 
			 request.setAttribute("viewtrailsList", trailsList);
			 
			 
			 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
			 
			 
			
			 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
			 Map<String, String> remarksReimbursement =new HashMap<String, String>();
			 
			 //Financial List
			 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
						.getViewTmpRimbursementDetails(claimforIntimation.getKey());
              
				List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
				List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
				Map<String, String> rodRemarks = new HashMap<String, String>();
				
				if (reimbursementDetails != null) {
					int i =1;
					int snoFinance = 1;
					
					for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
						rodRemarks.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
						if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
							//////newly added for fa and paymentremarks//////
							if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && 
							ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey())){
							remarksReimbursement.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
							}
							if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
			                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
			                 
			                 dto.setSno(i);
			                 i++;
			                 if(null != reimbursement.getClaim()){
			                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
			                 }
			//                 if(null != reimbursement.getBillingApprovedAmount()){
			//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			//                 }
			                 
			                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
							}
			//                 if(reimbursement.getBillingCompletedDate() != null){
			                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
			                 if(reimbursement.getBillingApprovedAmount() != null){
			                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			                 }
			//                 }
//			                 dto.setStatus(reimbursement.getStatus().getProcessValue());
			                 dto.setStatus(reimbursement.getStatus().getPortalStatus()); //CR2019064
			                 dto.setRodKey(reimbursement.getKey());
			                 Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", intimationNumber);
							 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
							 String billDtlsToken = null;
							  try {
								  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
							  } catch (NoSuchAlgorithmException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  } catch (ParseException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  }
							  tokenInputs = null;
							  dto.setBillDtslToken(billDtlsToken);
			                 billDetails.add(dto);
							}
							
							if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
								FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
								dto1.setSno(snoFinance);
								snoFinance++;
								dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
								Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto1.setAmount(String.valueOf(approvedAmount));
							     }
			//					if(reimbursement.getFinancialCompletedDate() != null){
								dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
								
								if(reimbursement.getFinancialApprovedAmount() != null){
									dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
								}
								
			//					}
//								dto1.setStatus(reimbursement.getStatus().getProcessValue());
								dto1.setStatus(reimbursement.getStatus().getPortalStatus()); //CR2019064
				                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
				                dto1.setRodKey(reimbursement.getKey());
				                
				                if(reimbursement.getDocAcknowLedgement() != null && 
				                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
				                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
				                }
				                
				                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
				                {
				                	ClaimPayment claimPayment;	
				                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//				                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
				                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
				                	
				                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
				                	{
				                		claimPayment = claimPaymetList.get(0);
				                		dto1.setPaymentType(claimPayment.getPaymentType());
				                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
				                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
				                		
				                	}
				                	
				                }
				                
				                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
				                	dto1.setAmount("0");
				                }
				                
//				                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
				                financialDetails.add(dto1);
							}
						}
					}
				}
				 
				request.setAttribute("billing", billDetails);
				request.setAttribute("financial", financialDetails);
				
				/* Payment Details */
				 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
				 	
				 	
				 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
					
					List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
					
					List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
					int snum =1;
					for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
						if(paymentProcessCpuTableDTO2.getChequeDate() != null){
							paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
							
						}if(paymentProcessCpuTableDTO2.getFaApprovalDate() != null){
							paymentProcessCpuTableDTO2.setFaApprovalDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getFaApprovalDate()));
							
						}
						if(paymentProcessCpuTableDTO2.getRodNo() != null){
							String remarks = remarksReimbursement.get(paymentProcessCpuTableDTO2.getRodNo());
							paymentProcessCpuTableDTO2.setFinancialRemarks(remarks);
						}
						/*for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
							if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && 
									ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey())){
								paymentProcessCpuTableDTO2.setFinancialRemarks(remarksReimbursement.get(reimbursement.getRodNumber()));
							}
					 	}*/
						paymentProcessCpuTableDTO2.setSerialNo(snum);
						paymentList.add(paymentProcessCpuTableDTO2);
						snum++;
					}
					
					request.setAttribute("payment", paymentList);
				 
			 }
		  	BPMClientContext bpmClientContext = new BPMClientContext();
		  	 if(null != claimforIntimation){
		  		Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
				 String intimationNoToken = null;
				  try {
					  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
				/*Below code commented due to security reason
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
				//getUI().getPage().open(url, "_blank");
				request.setAttribute("dmsUrl",url);
		  	 }
			String queryString = request.getQueryString();
			String rodNo = request.getParameter("reimb_num");
			String cashlessNo = request.getParameter("cashless_num");
			
			if(!error.isEmpty()){
				  request.setAttribute("error", error);
				  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
			  } 
			  else {
				  request.setAttribute("intimation", intimationByNo);
				  request.setAttribute("hospitals", hospitals);
				  request.getRequestDispatcher("/WEB-INF/ClaimAgentPage.jsp").include(request, response);
				  }
		  }

	}
	private void showHospitalPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String daignosisName = "";
		  String error = "";
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Reimbursement remibNo = null;
		  Policy policy = null;
		  Hospitals hospitals = null;
		  ViewRejectionDTO rejectioDto = null;
		  List<ViewQueryDTO> viewQueryDTOvalues = null;
		  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
		  Boolean doc = true;
		  
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		  if(!intimationNumber.isEmpty()){
			 
//			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
			 
			 /* if(null == intimationByNo && intimationNumber == null){
				  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
			  	}*/
			  
			 
			  if(error.isEmpty()){
				  if(intimationByNo != null) {
				  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
				  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
				  if(claimforIntimation != null) {
					  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
								.getKey());
						if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
							preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
							if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){
								preauth.setTotalApprovalAmount(0d);
							}
						}
				    }
				  }
				  
				 /* if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)
							|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)
							|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)
							|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS))){
					  doc =false;
				  
				  }*/
				  
			  }
			  
		 
		
		  if(null != preauth){
			  
			 
			  
			  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
		  
			  int i=1;
			  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
				  if(diagnosis.size() == i){
					  daignosisName += pedDetailsTableDTO.getDiagnosisName();
				  } else {
					  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
				  }

				  i++;
			  }
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("preauth", preauth);
		  }
		  
		  if(null != claimforIntimation){
			  Integer snoPreauthSummary=1;
			  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.searchFromTmpPreauth(claimforIntimation.getKey());
			  
				List<Long> keys = new ArrayList<Long>();
				if(null != previousPreauthList && !previousPreauthList.isEmpty()){
					for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
						keys.add(previousPreAuthTableDTO.getKey());
					}
				}
			  
			  ViewTmpPreauth prevpreauth = null;
				Long preauthKey = null;
				Double reqAmount=new Double(0);
				Double approvedAmount=new Double(0);
				if (!keys.isEmpty()) {
					preauthKey = Collections.max(keys);

					prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
				}
				
			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				previousPreAuthTableDTO.setSno(snoPreauthSummary);
				snoPreauthSummary++;
				previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
				reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
				approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
				Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("cashlessKey", previousPreAuthTableDTO.getKey().toString());
				 String cashlessDocToken = null;
				  try {
					  cashlessDocToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  previousPreAuthTableDTO.setCashlessDocToken(cashlessDocToken);
				newList.add(previousPreAuthTableDTO);

			}
			
			
			
			request.setAttribute("cashless", newList);
			if(previousPreauthList != null) {
			request.setAttribute("requestedAmt", reqAmount);
			request.setAttribute("totalApprovedAmount", approvedAmount);
			}
		  }
		  
		  if(rodNUmber != null) {
				 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
				  rejectioDto = new ViewRejectionDTO();
				 if(reimbursementKey != null) {
				 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
				 
				 if(rejectionKeyByReimbursement != null) {
					rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
					rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
					String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
					String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
					//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//					rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
					rejectioDto.setRejectedDate(rejectDate);
					String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
					rejectioDto.setApproveRejectionDate(approverejectDate);
					rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
					rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
					rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
					
				 }
				
				 }
				 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
				 rejectioDto.setAdmissionDate(admisionDate);
				 request.setAttribute("rejectList",rejectioDto);
				}
		  
		  if(null != claimforIntimation){
			  
			 request.setAttribute("claim", claimforIntimation);
			  
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
			 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
			 
			 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
			 
			 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
			 
			 
			 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
				 
				 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
				 
				 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
				 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
				 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//				 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
				 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
				 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
				 }
				 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
				 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
				 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
				 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
				 tableDTO.setStatus(docAcknowledgement.getStatus());
				 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
				 tableDTO.setTypeOfClaim(claimforIntimation.getClaimType().getValue());
				 tableDTO.setAmount(docAcknowledgement.getApprovedAmount());
				 
				 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
					 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
					 }
				 
//				 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
//				 tableDTO.setAmount(Double.valueOf(approvedAmount));
				 
				 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
				 
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
				 String ackToken = null;
				  try {
					  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  tableDTO.setAcknowledgementToken(ackToken);
		
				 docAcknowledgementList.add(tableDTO);
			
			 }
			 	
				 request.setAttribute("reimbursementList", docAcknowledgementList);
		 
				 if(rodNUmber != null) {
					 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
					 
					 if(reimbursementKey != null){
						 
						 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
						 Hospitals hospitalDetails=null; 
						 
						 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
						 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
						 //need to implement
						 if(claimforIntimation != null){
							 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
							 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
						 }	
						 
						 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
							 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
							 viewQueryDTO.setQueryDate(rejectDate);
							 viewQueryDTO.setDiagnosis(diagnosisName);
							 
							 if(hospitalDetails != null){
								 viewQueryDTO.setHospitalName(hospitalDetails.getName());
								 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
								 viewQueryDTOvalues.add(viewQueryDTO);
							 }
//							viewQueryDTO.setClaim(claimforIntimation);
							 
							 
						 }
						 request.setAttribute("queryList", viewQueryDTOvalues);
					 }
					 
					
				 }
				 
			  
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claimforIntimation.getKey(), reimkey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 
			 List<ViewClaimHistoryDTO> trailsList = new ArrayList<ViewClaimHistoryDTO>();
			 for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
				 
				 ViewClaimHistoryDTO viewHistoryDto = new ViewClaimHistoryDTO();
				 
				 viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setReimbursementKey(viewClaimHistoryDTO.getReimbursementKey());
				 viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
				 viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
				 viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
				 viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
				 trailsList.add(viewHistoryDto);
			}
			 
			 request.setAttribute("viewtrailsList", trailsList);
			 
			 
			 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
			 
			 Map<String, String> rodRemarks =new HashMap<String, String>();
			 Map<String, String> remarksReimbursement =new HashMap<String, String>();
			 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
			 
			 //Financial List
			 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
						.getViewTmpRimbursementDetails(claimforIntimation.getKey());

				List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
				List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
				if (reimbursementDetails != null) {
					int i =1;
					int snoFinance = 1;
					for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
						rodRemarks.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
						if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
							//////newly added for fa and paymentremarks//////
							if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && 
							ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey())){
							remarksReimbursement.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
							}
							if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
			                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
			                 
			                 dto.setSno(i);
			                 i++;
			                 if(null != reimbursement.getClaim()){
			                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
			                 }
			//                 if(null != reimbursement.getBillingApprovedAmount()){
			//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			//                 }
			                 
			                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
							}
			//                 if(reimbursement.getBillingCompletedDate() != null){
			                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
			                 if(reimbursement.getBillingApprovedAmount() != null){
			                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			                 }
			//                 }
			                 dto.setStatus(reimbursement.getStatus().getProcessValue());
			                 dto.setRodKey(reimbursement.getKey());
			                 Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", intimationNumber);
							 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
							 String billDtlsToken = null;
							  try {
								  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
							  } catch (NoSuchAlgorithmException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  } catch (ParseException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  }
							  tokenInputs = null;
							  dto.setBillDtslToken(billDtlsToken);
			                 billDetails.add(dto);
							}
							
							if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
								FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
								dto1.setSno(snoFinance);
								snoFinance++;
								dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
								Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto1.setAmount(String.valueOf(approvedAmount));
							     }
			//					if(reimbursement.getFinancialCompletedDate() != null){
								dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
								
								if(reimbursement.getFinancialApprovedAmount() != null){
									dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
								}
								
			//					}
								dto1.setStatus(reimbursement.getStatus().getProcessValue());
				                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
				                dto1.setRodKey(reimbursement.getKey());
				                
				                if(reimbursement.getDocAcknowLedgement() != null && 
				                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
				                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
				                }
				                
				                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
				                {
				                	ClaimPayment claimPayment;	
				                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//				                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
				                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
				                	
				                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
				                	{
				                		claimPayment = claimPaymetList.get(0);
				                		dto1.setPaymentType(claimPayment.getPaymentType());
				                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
				                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
				                		
				                	}
				                	
				                }
				                
				                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
				                	dto1.setAmount("0");
				                }
				                
//				                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
				                financialDetails.add(dto1);
							}
						}
					}
				}
				 
				request.setAttribute("billing", billDetails);
				request.setAttribute("financial", financialDetails);
				
				/* Payment Details */
				
				 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
				 	
				 	
				 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
					
					List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
					
					List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
					int snum =1;
					for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
						if(paymentProcessCpuTableDTO2.getChequeDate() != null){
							paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
							
						}if(paymentProcessCpuTableDTO2.getFaApprovalDate() != null){
							paymentProcessCpuTableDTO2.setFaApprovalDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getFaApprovalDate()));
							
						}
						if(paymentProcessCpuTableDTO2.getRodNo() != null){
							String remarks = remarksReimbursement.get(paymentProcessCpuTableDTO2.getRodNo());
							paymentProcessCpuTableDTO2.setFinancialRemarks(remarks);
						}
						paymentProcessCpuTableDTO2.setSerialNo(snum);
						paymentList.add(paymentProcessCpuTableDTO2);
						snum++;
					}
					
					request.setAttribute("payment", paymentList);
				 
			 }
		  	BPMClientContext bpmClientContext = new BPMClientContext();
		  	 if(null != claimforIntimation){
		  		Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
				 String intimationNoToken = null;
				  try {
					  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
				/*Below code commneted due to security reason
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
				//getUI().getPage().open(url, "_blank");
				request.setAttribute("dmsUrl",url);
		  	 }
			String queryString = request.getQueryString();
			String rodNo = request.getParameter("reimb_num");
			String cashlessNo = request.getParameter("cashless_num");
			
			if(!error.isEmpty()){
				  request.setAttribute("error", error);
				  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
			  } 
			  else {
				  request.setAttribute("intimation", intimationByNo);
				  request.setAttribute("hospitals", hospitals);
				  request.setAttribute("docAvailable", doc);
				  request.getRequestDispatcher("/WEB-INF/ClaimHospitalPage.jsp").include(request, response);
				  }
		  }

	}
	private void showAggregatorPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String daignosisName = "";
		  String error = "";
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Reimbursement remibNo = null;
		  Policy policy = null;
		  Hospitals hospitals = null;
		  ViewRejectionDTO rejectioDto = null;
		  List<ViewQueryDTO> viewQueryDTOvalues = null;
		  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
		  
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		  if(!intimationNumber.isEmpty()){
			 
//			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
			 
			 /* if(null == intimationByNo && intimationNumber == null){
				  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
			  	}*/
			  
			 
			  if(error.isEmpty()){
				  if(intimationByNo != null) {
				  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
				  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
				  if(claimforIntimation != null) {
					  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
								.getKey());
						if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
							preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
							
							if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){		
								preauth.setTotalApprovalAmount(0d);		
							
							}
						}
				    }
				  }
				  
			  }
			  
		 
		
		  if(null != preauth){
			  
			 
			  
			  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
		  
			  int i=1;
			  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
				  if(diagnosis.size() == i){
					  daignosisName += pedDetailsTableDTO.getDiagnosisName();
				  } else {
					  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
				  }

				  i++;
			  }
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("preauth", preauth);
		  }
		  
		  if(null != claimforIntimation){
			  Integer snoPreauthSummary=1;
			  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.searchFromTmpPreauth(claimforIntimation.getKey());
			  
				List<Long> keys = new ArrayList<Long>();
				if(null != previousPreauthList && !previousPreauthList.isEmpty()){
					for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
						keys.add(previousPreAuthTableDTO.getKey());
					}
				}
			  
			  ViewTmpPreauth prevpreauth = null;
				Long preauthKey = null;
				Double reqAmount=new Double(0);
				Double approvedAmount=new Double(0);
				if (!keys.isEmpty()) {
					preauthKey = Collections.max(keys);

					prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
				}
				
			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				previousPreAuthTableDTO.setSno(snoPreauthSummary);
				snoPreauthSummary++;
				previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
				reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
				approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
				newList.add(previousPreAuthTableDTO);

			}
			
			request.setAttribute("cashless", newList);
			if(previousPreauthList != null) {
			request.setAttribute("requestedAmt", reqAmount);
			request.setAttribute("totalApprovedAmount", approvedAmount);
			}
		  }
		  
		  if(rodNUmber != null) {
				 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
				  rejectioDto = new ViewRejectionDTO();
				 if(reimbursementKey != null) {
				 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
				 
				 if(rejectionKeyByReimbursement != null) {
					rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
					rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
					String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
					String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
					//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//					rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
					rejectioDto.setRejectedDate(rejectDate);
					String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
					rejectioDto.setApproveRejectionDate(approverejectDate);
					rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
					rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
					rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
					
				 }
				
				 }
				 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
				 rejectioDto.setAdmissionDate(admisionDate);
				 request.setAttribute("rejectList",rejectioDto);
				}
		  
		  if(null != claimforIntimation){
			  
			 request.setAttribute("claim", claimforIntimation);
			  
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
			 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
			 
			 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
			 
			 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
			 
			 
			 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
				 
				 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
				 
				 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
				 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
				 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//				 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
				 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
				 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
				 }
				 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
				 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
				 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
				 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
				 tableDTO.setStatus(docAcknowledgement.getPortalStatusVal());
				 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
				 tableDTO.setTypeOfClaim(claimforIntimation.getClaimType().getValue());			
				 tableDTO.setAmount(docAcknowledgement.getApprovedAmount());
				 
				 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
					 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
					 }
				 
				/* Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
				 tableDTO.setAmount(Double.valueOf(approvedAmount));
				 */
				 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
				 
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
				 String ackToken = null;
				  try {
					  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  tableDTO.setAcknowledgementToken(ackToken);
			
				 docAcknowledgementList.add(tableDTO);
			
			 }
			 	
				 request.setAttribute("reimbursementList", docAcknowledgementList);
		 
				 if(rodNUmber != null) {
					 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
					 
					 if(reimbursementKey != null){
						 
						 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
						 Hospitals hospitalDetails=null; 
						 
						 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
						 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
						 //need to implement
						 if(claimforIntimation != null){
							 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
							 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
						 }	
						 
						 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
							 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
							 viewQueryDTO.setQueryDate(rejectDate);
							 viewQueryDTO.setDiagnosis(diagnosisName);
							 
							 if(hospitalDetails != null){
								 viewQueryDTO.setHospitalName(hospitalDetails.getName());
								 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
								 viewQueryDTOvalues.add(viewQueryDTO);
							 }
//							viewQueryDTO.setClaim(claimforIntimation);
							 
							 
						 }
						 request.setAttribute("queryList", viewQueryDTOvalues);
					 }
					 
					
				 }
				 
			  
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claimforIntimation.getKey(), reimkey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 
			 List<ViewClaimHistoryDTO> trailsList = new ArrayList<ViewClaimHistoryDTO>();
			 for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
				 
				 ViewClaimHistoryDTO viewHistoryDto = new ViewClaimHistoryDTO();
				 
				 viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setReimbursementKey(viewClaimHistoryDTO.getReimbursementKey());
				 viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
				 viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
				 viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
				 viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
				 trailsList.add(viewHistoryDto);
			}
			 
			 request.setAttribute("viewtrailsList", trailsList);
			 
			 
			 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
			 
			 Map<String, String> rodRemarks =new HashMap<String, String>();
			 Map<String, String> remarksReimbursement =new HashMap<String, String>();
			 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
			 
			 //Financial List
			 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
						.getViewTmpRimbursementDetails(claimforIntimation.getKey());

				List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
				List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
				if (reimbursementDetails != null) {
					int i =1;
					int snoFinance = 1;
					for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
						rodRemarks.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
						if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
							//////newly added for fa and paymentremarks//////
							if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && 
							ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey())){
							remarksReimbursement.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
							
                         }
							if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
			                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
			                 
			                 dto.setSno(i);
			                 i++;
			                 if(null != reimbursement.getClaim()){
			                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
			                 }
			//                 if(null != reimbursement.getBillingApprovedAmount()){
			//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			//                 }
			                 
			                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
							}
			//                 if(reimbursement.getBillingCompletedDate() != null){
			                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
			                 if(reimbursement.getBillingApprovedAmount() != null){
			                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			                 }
			//                 }
//			                 dto.setStatus(reimbursement.getStatus().getProcessValue());
			                 dto.setStatus(reimbursement.getStatus().getPortalStatus()); // CR2019064
			                 dto.setRodKey(reimbursement.getKey());
			                 Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", intimationNumber);
							 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
							 String billDtlsToken = null;
							  try {
								  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
							  } catch (NoSuchAlgorithmException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  } catch (ParseException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  }
							  tokenInputs = null;
							  dto.setBillDtslToken(billDtlsToken);
			                 billDetails.add(dto);
							}
							
							if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
								FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
								dto1.setSno(snoFinance);
								snoFinance++;
								dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
								Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto1.setAmount(String.valueOf(approvedAmount));
							     }
			//					if(reimbursement.getFinancialCompletedDate() != null){
								dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
								
								if(reimbursement.getFinancialApprovedAmount() != null){
									dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
								}
								
			//					}
//								dto1.setStatus(reimbursement.getStatus().getProcessValue());
								dto1.setStatus(reimbursement.getStatus().getPortalStatus()); //CR2019064
				                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
				                dto1.setRodKey(reimbursement.getKey());
				                
				                if(reimbursement.getDocAcknowLedgement() != null && 
				                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
				                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
				                }
				                
				                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
				                {
				                	ClaimPayment claimPayment;	
				                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//				                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
				                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
				                	
				                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
				                	{
				                		claimPayment = claimPaymetList.get(0);
				                		dto1.setPaymentType(claimPayment.getPaymentType());
				                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
				                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
				                		
				                	}
				                	
				                }
				                
				                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
				                	dto1.setAmount("0");
				                }
				                
//				                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
				                financialDetails.add(dto1);
							}
						}
					}
				}
				 
				request.setAttribute("billing", billDetails);
				request.setAttribute("financial", financialDetails);
				
				/* Payment Details */
				 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
				 	
				 	
				 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
				 	
				 	
				 	
					
					List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
					
					List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
					int snum =1;
					for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
						if(paymentProcessCpuTableDTO2.getChequeDate() != null){
							paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
							
						}if(paymentProcessCpuTableDTO2.getFaApprovalDate() != null){
							paymentProcessCpuTableDTO2.setFaApprovalDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getFaApprovalDate()));
							
						}
						if(paymentProcessCpuTableDTO2.getRodNo() != null){
							String remarks = remarksReimbursement.get(paymentProcessCpuTableDTO2.getRodNo());
							paymentProcessCpuTableDTO2.setFinancialRemarks(remarks);
						}
						paymentProcessCpuTableDTO2.setSerialNo(snum);
						paymentList.add(paymentProcessCpuTableDTO2);
						snum++;
					}
					
					request.setAttribute("payment", paymentList);
				 
			 }
		  	BPMClientContext bpmClientContext = new BPMClientContext();
		  	 if(null != claimforIntimation){
		  		Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
				 String intimationNoToken = null;
				  try {
					  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
				/*Below code commented due to security reason
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
				//getUI().getPage().open(url, "_blank");
				request.setAttribute("dmsUrl",url);
		  	 }
			String queryString = request.getQueryString();
			String rodNo = request.getParameter("reimb_num");
			String cashlessNo = request.getParameter("cashless_num");
			
			if(!error.isEmpty()){
				  request.setAttribute("error", error);
				  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
			  } 
			  else {
				  request.setAttribute("intimation", intimationByNo);
				  request.setAttribute("hospitals", hospitals);
				  request.getRequestDispatcher("/WEB-INF/ClaimAggregatorPage.jsp").include(request, response);
				  }
		  }

	}
	private void showCorporatePage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String daignosisName = "";
		  String error = "";
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Reimbursement remibNo = null;
		  Policy policy = null;
		  Hospitals hospitals = null;
		  ViewRejectionDTO rejectioDto = null;
		  List<ViewQueryDTO> viewQueryDTOvalues = null;
		  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
		  
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		  if(!intimationNumber.isEmpty()){
			 
//			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
			 
			 /* if(null == intimationByNo && intimationNumber == null){
				  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
			  	}*/
			  
			 
			  if(error.isEmpty()){
				  if(intimationByNo != null) {
				  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
				  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
				  if(claimforIntimation != null) {
					  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
								.getKey());
						if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
							preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
							
							if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){		
								preauth.setTotalApprovalAmount(0d);		
							
							}
						}
				    }
				  }
				  
			  }
			  
		 
		
		  if(null != preauth){
			  
			 
			  
			  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
		  
			  int i=1;
			  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
				  if(diagnosis.size() == i){
					  daignosisName += pedDetailsTableDTO.getDiagnosisName();
				  } else {
					  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
				  }

				  i++;
			  }
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("preauth", preauth);
		  }
		  
		  if(null != claimforIntimation){
			  Integer snoPreauthSummary=1;
			  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.searchFromTmpPreauth(claimforIntimation.getKey());
			  
				List<Long> keys = new ArrayList<Long>();
				if(null != previousPreauthList && !previousPreauthList.isEmpty()){
					for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
						keys.add(previousPreAuthTableDTO.getKey());
					}
				}
			  
			  ViewTmpPreauth prevpreauth = null;
				Long preauthKey = null;
				Double reqAmount=new Double(0);
				Double approvedAmount=new Double(0);
				if (!keys.isEmpty()) {
					preauthKey = Collections.max(keys);

					prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
				}
				
			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				previousPreAuthTableDTO.setSno(snoPreauthSummary);
				snoPreauthSummary++;
				previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
				reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
				approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
				Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("cashlessKey", previousPreAuthTableDTO.getKey().toString());
				 String cashlessDocToken = null;
				  try {
					  cashlessDocToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  previousPreAuthTableDTO.setCashlessDocToken(cashlessDocToken);
				newList.add(previousPreAuthTableDTO);

			}
			
			request.setAttribute("cashless", newList);
			if(previousPreauthList != null) {
			request.setAttribute("requestedAmt", reqAmount);
			request.setAttribute("totalApprovedAmount", approvedAmount);
			}
		  }
		  
		  if(rodNUmber != null) {
				 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
				  rejectioDto = new ViewRejectionDTO();
				 if(reimbursementKey != null) {
				 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
				 
				 if(rejectionKeyByReimbursement != null) {
					rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
					rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
					String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
					String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
					//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//					rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
					rejectioDto.setRejectedDate(rejectDate);
					String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
					rejectioDto.setApproveRejectionDate(approverejectDate);
					rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
					rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
					rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
					
				 }
				
				 }
				 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
				 rejectioDto.setAdmissionDate(admisionDate);
				 request.setAttribute("rejectList",rejectioDto);
				}
		  
		  if(null != claimforIntimation){
			  
			 request.setAttribute("claim", claimforIntimation);
			  
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
			 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
			 
			 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
			 
			 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
			 
			 
			 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
				 
				 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
				 
				 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
				 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
				 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//				 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
				 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
				 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
				 }
				 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
				 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
				 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
				 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
				 tableDTO.setStatus(docAcknowledgement.getPortalStatusVal());
				 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
				 tableDTO.setTypeOfClaim(claimforIntimation.getClaimType().getValue());			
				 tableDTO.setAmount(docAcknowledgement.getApprovedAmount());
				 
				 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
					 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
					 }
				 
				/* Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
				 tableDTO.setAmount(Double.valueOf(approvedAmount));
				 */
				 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
				 
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
				 String ackToken = null;
				  try {
					  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  tableDTO.setAcknowledgementToken(ackToken);
			
				 docAcknowledgementList.add(tableDTO);
			
			 }
			 	
				 request.setAttribute("reimbursementList", docAcknowledgementList);
		 
				 if(rodNUmber != null) {
					 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
					 
					 if(reimbursementKey != null){
						 
						 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
						 Hospitals hospitalDetails=null; 
						 
						 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
						 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
						 //need to implement
						 if(claimforIntimation != null){
							 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
							 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
						 }	
						 
						 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
							 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
							 viewQueryDTO.setQueryDate(rejectDate);
							 viewQueryDTO.setDiagnosis(diagnosisName);
							 
							 if(hospitalDetails != null){
								 viewQueryDTO.setHospitalName(hospitalDetails.getName());
								 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
								 viewQueryDTOvalues.add(viewQueryDTO);
							 }
//							viewQueryDTO.setClaim(claimforIntimation);
							 
							 
						 }
						 request.setAttribute("queryList", viewQueryDTOvalues);
					 }
					 
					
				 }
				 
			  
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claimforIntimation.getKey(), reimkey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 
			 List<ViewClaimHistoryDTO> trailsList = new ArrayList<ViewClaimHistoryDTO>();
			 for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
				 
				 ViewClaimHistoryDTO viewHistoryDto = new ViewClaimHistoryDTO();
				 
				 viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setReimbursementKey(viewClaimHistoryDTO.getReimbursementKey());
				 viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
				 viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
				 viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
				 viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
				 trailsList.add(viewHistoryDto);
			}
			 
			 request.setAttribute("viewtrailsList", trailsList);
			 
			 
			 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
			 
			 Map<String, String> rodRemarks =new HashMap<String, String>();
			 Map<String, String> remarksReimbursement =new HashMap<String, String>();
			 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
			 
			 //Financial List
			 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
						.getViewTmpRimbursementDetails(claimforIntimation.getKey());

				List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
				List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
				if (reimbursementDetails != null) {
					int i =1;
					int snoFinance = 1;
					for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
						rodRemarks.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
						if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
							//////newly added for fa and paymentremarks//////
							if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && 
							ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey())){
							remarksReimbursement.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
							
                         }
							if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
			                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
			                 
			                 dto.setSno(i);
			                 i++;
			                 if(null != reimbursement.getClaim()){
			                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
			                 }
			//                 if(null != reimbursement.getBillingApprovedAmount()){
			//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			//                 }
			                 
			                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
							}
			//                 if(reimbursement.getBillingCompletedDate() != null){
			                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
			                 if(reimbursement.getBillingApprovedAmount() != null){
			                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			                 }
			//                 }
//			                 dto.setStatus(reimbursement.getStatus().getProcessValue());
			                 dto.setStatus(reimbursement.getStatus().getPortalStatus()); //CR2019064
			                 dto.setRodKey(reimbursement.getKey());
			                 Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", intimationNumber);
							 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
							 String billDtlsToken = null;
							  try {
								  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
							  } catch (NoSuchAlgorithmException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  } catch (ParseException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  }
							  tokenInputs = null;
							  dto.setBillDtslToken(billDtlsToken);
			                 billDetails.add(dto);
							}
							
							if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
								FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
								dto1.setSno(snoFinance);
								snoFinance++;
								dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
								Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto1.setAmount(String.valueOf(approvedAmount));
							     }
			//					if(reimbursement.getFinancialCompletedDate() != null){
								dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
								
								if(reimbursement.getFinancialApprovedAmount() != null){
									dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
								}
								
			//					}
//								dto1.setStatus(reimbursement.getStatus().getProcessValue());
								dto1.setStatus(reimbursement.getStatus().getPortalStatus()); //CR2019064
				                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
				                dto1.setRodKey(reimbursement.getKey());
				                
				                if(reimbursement.getDocAcknowLedgement() != null && 
				                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
				                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
				                }
				                
				                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
				                {
				                	ClaimPayment claimPayment;	
				                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//				                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
				                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
				                	
				                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
				                	{
				                		claimPayment = claimPaymetList.get(0);
				                		dto1.setPaymentType(claimPayment.getPaymentType());
				                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
				                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
				                		
				                	}
				                	
				                }
				                
				                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
				                	dto1.setAmount("0");
				                }
				                
//				                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
				                financialDetails.add(dto1);
							}
						}
					}
				}
				 
				request.setAttribute("billing", billDetails);
				request.setAttribute("financial", financialDetails);
				
				/* Payment Details */
				 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
				 	
				 	
				 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
				 	
				 	
				 	
					
					List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
					
					List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
					int snum =1;
					for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
						if(paymentProcessCpuTableDTO2.getChequeDate() != null){
							paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
							
						}if(paymentProcessCpuTableDTO2.getFaApprovalDate() != null){
							paymentProcessCpuTableDTO2.setFaApprovalDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getFaApprovalDate()));
							
						}
						if(paymentProcessCpuTableDTO2.getRodNo() != null){
							String remarks = remarksReimbursement.get(paymentProcessCpuTableDTO2.getRodNo());
							paymentProcessCpuTableDTO2.setFinancialRemarks(remarks);
						}
						paymentProcessCpuTableDTO2.setSerialNo(snum);
						paymentList.add(paymentProcessCpuTableDTO2);
						snum++;
					}
					
					request.setAttribute("payment", paymentList);
				 
			 }
		  	BPMClientContext bpmClientContext = new BPMClientContext();
		  	 if(null != claimforIntimation){
		  		Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
				 String intimationNoToken = null;
				  try {
					  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
				  /*Below code commented due to security reason
				  String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
				//getUI().getPage().open(url, "_blank");
				request.setAttribute("dmsUrl",url);
		  	 }
			String queryString = request.getQueryString();
			String rodNo = request.getParameter("reimb_num");
			String cashlessNo = request.getParameter("cashless_num");
			
			if(!error.isEmpty()){
				  request.setAttribute("error", error);
				  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
			  } 
			  else {
				  request.setAttribute("intimation", intimationByNo);
				  request.setAttribute("hospitals", hospitals);
				  request.getRequestDispatcher("/WEB-INF/ViewCorporatePortal.jsp").include(request, response);
				  }
		  }

	}
	
	private void showCRCPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		String daignosisName = "";
	  String error = "";
	  Intimation intimationByNo = null;
	  Intimation intimationByHCN = null;
	  Preauth preauth = null;
	  Reimbursement remibNo = null;
	  Policy policy = null;
	  Hospitals hospitals = null;
	  ViewRejectionDTO rejectioDto = null;
	  Double compulsaryCopayAmount = null;
	  String compulsaryCopayPercent = "0";
	  String voluntaryCopayPercent = "0";
	  Double totalCopay = null;
	  Double voluntaryCopayAmount = 0d;
	  List<ViewQueryDTO> viewQueryDTOvalues = null;
	  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
	  List<ViewQueryDTO> viewCrcQueryDTOvalues = null;
	  ObjectMapper mapper = new ObjectMapper();
	  
//	  doPostPremeia(request, response);
	 
		  
	  
	  Claim claimforIntimation = null;
	  
	  if(!intimationNumber.isEmpty()){
		 
//		  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
		 
		 /* if(null == intimationByNo && intimationNumber == null){
			  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
		  	}*/
		  
		 
		  if(error.isEmpty()){
			  if(intimationByNo != null) {
			  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
			  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
			  if(claimforIntimation != null) {
				  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
							.getKey());
					if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
						preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
						
						if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)		
								|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)		
								|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)		
								|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
								|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){		
							preauth.setTotalApprovalAmount(0d);		
						
						}
					}
			    }
			  }
			  
		  }
		  
	 
	
	  if(null != preauth){
		  
		 
		  
		  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
	  
		  int i=1;
		  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
			  if(diagnosis.size() == i){
				  daignosisName += pedDetailsTableDTO.getDiagnosisName();
			  } else {
				  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
			  }

			  i++;
		  }
		  request.setAttribute("daignosisName", daignosisName);
		  request.setAttribute("preauth", preauth);
	  }
	  
	  if(null != claimforIntimation){
		  
		  int snoPreauthSummary = 1;
		  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
					.searchFromTmpPreauth(claimforIntimation.getKey());
		  
			List<Long> keys = new ArrayList<Long>();
			if(null != previousPreauthList && !previousPreauthList.isEmpty()){
				for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
					keys.add(previousPreAuthTableDTO.getKey());
				}
			}
		  
		  ViewTmpPreauth prevpreauth = null;
			Long preauthKey = null;
			Double reqAmount=new Double(0);
			Double approvedAmount=new Double(0);
			if (!keys.isEmpty()) {
				preauthKey = Collections.max(keys);

				prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
			}
			
		List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
		for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
			previousPreAuthTableDTO.setSno(snoPreauthSummary);
			snoPreauthSummary++;
			previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
			reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
			approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
			newList.add(previousPreAuthTableDTO);

		}
		
		request.setAttribute("cashless", newList);
		if(previousPreauthList != null) {
		request.setAttribute("requestedAmt", reqAmount);
		request.setAttribute("totalApprovedAmount", approvedAmount);
		}
	  }
	  
	  if(rodNUmber != null) {
			 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
			  rejectioDto = new ViewRejectionDTO();
			 if(reimbursementKey != null) {
			 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
			 
			 if(rejectionKeyByReimbursement != null) {
				rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
				rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
				String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
				rejectioDto.setAdmissionDate(admisionDate);
				String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
				//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//				rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
				rejectioDto.setRejectedDate(rejectDate);
				String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
				rejectioDto.setApproveRejectionDate(approverejectDate);
				rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
				rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
				rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
				
			 }
			
			 }
			 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
			 rejectioDto.setAdmissionDate(admisionDate);
			 request.setAttribute("rejectList",rejectioDto);
			}
	  
	  if(null != claimforIntimation){
		  
		 request.setAttribute("claim", claimforIntimation);
		  
//		 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
		 
//		 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
		 
		 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
		 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
		 
		 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
		 
		 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
		 
		 
		 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
			 
			 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
			 
			 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
			 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
			 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//			 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
			 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
			 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
			 }
			 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
			 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
			 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
			 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
			 tableDTO.setStatus(docAcknowledgement.getStatus());
			 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
			 tableDTO.setTypeOfClaim(claimforIntimation.getClaimType().getValue());
			 //added for CR-2020046 on 23-04-2020
			 if(docAcknowledgement.getReconsiderationRequestFlag() != null){
				 tableDTO.setReconsiderationRequestFlagged(docAcknowledgement.getReconsiderationRequestFlag());
			 }
			 if(docAcknowledgement.getReconsiderationRejectionFlagRemarks() != null){
				 tableDTO.setReconsiderationRequestFlagRemarks(docAcknowledgement.getReconsiderationRejectionFlagRemarks());
			 }
			 tableDTO.setAmount(docAcknowledgement.getApprovedAmount());
			 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
				 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
				 }
			 
			/* Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
			 tableDTO.setAmount(Double.valueOf(docAcknowledgement.getApprovedAmount()));*/
			 
			 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
			 
			 Map<String,String> tokenInputs = new HashMap<String, String>();
			 tokenInputs.put("intimationNo", intimationNumber);
			 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
			 String ackToken = null;
			  try {
				  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
			  } catch (NoSuchAlgorithmException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  } catch (ParseException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  tokenInputs = null;
			  tableDTO.setAcknowledgementToken(ackToken);
			
			 docAcknowledgementList.add(tableDTO);
		
		 }
		 	
			 request.setAttribute("reimbursementList", docAcknowledgementList);
	 
			 if(rodNUmber != null) {
				 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
				 if(reimbursementKey != null){
					 
					 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
					 Hospitals hospitalDetails=null; 
					 
					 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
					 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
					 //need to implement
					 if(claimforIntimation != null){
						 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
						 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
					 }	
					 
					 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
						 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
						 viewQueryDTO.setQueryDate(rejectDate);
						 viewQueryDTO.setDiagnosis(diagnosisName);
						 
						 if(hospitalDetails != null){
							 viewQueryDTO.setHospitalName(hospitalDetails.getName());
							 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
							 viewQueryDTOvalues.add(viewQueryDTO);
						 }
//						viewQueryDTO.setClaim(claimforIntimation);
						 
						 
					 }
					 request.setAttribute("queryList", viewQueryDTOvalues);
				 }
				
			 }
			 		  
		 
		 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
		 
		 
		
		 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
		 
		 //Financial List
		 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
					.getViewTmpRimbursementDetails(claimforIntimation.getKey());

			List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
			List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
			if (reimbursementDetails != null) {
				int i =1;
				int snoFinance = 1;
				for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
					if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
						
						if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
		                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
		                 
		                 dto.setSno(i);
		                 i++;
		                 if(null != reimbursement.getClaim()){
		                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
		                 }
		//                 if(null != reimbursement.getBillingApprovedAmount()){
		//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
		//                 }
		                 
		                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
							if(approvedAmount >0){
								dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
						}
		//                 if(reimbursement.getBillingCompletedDate() != null){
		                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
		                 if(reimbursement.getBillingApprovedAmount() != null){
		                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
		                 }
		//                 }
		                 dto.setStatus(reimbursement.getStatus().getProcessValue());
		                 dto.setRodKey(reimbursement.getKey());
		                 dto.setRodNumber(reimbursement.getRodNumber());
		                 
		                 if (reimbursement.getDocAcknowLedgement() != null) {
			                DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
			                if (reimbursement.getReconsiderationRequest() != null && reimbursement.getReconsiderationRequest().equalsIgnoreCase("Y")) {
			                	Long version = reimbursement.getVersion() != null ? reimbursement.getVersion() - 1l : 1l;
			                	dto.setRodType("Reconsideration - " + version);
			                } else {
			                	dto.setRodType("Original");
			                }
			                
			                if (docAcknowledgement.getDocumentReceivedFromId() != null) {
			                	dto.setDocReceivedFrom(docAcknowledgement.getDocumentReceivedFromId().getValue());
			                }
			                	 
			                if (docAcknowledgement != null) {
			                	String billClassificationValue = SHAUtils.getBillClassificationValue(docAcknowledgement);
			                	dto.setBillClassification(billClassificationValue);
			                }
			             }
		                 Map<String,String> tokenInputs = new HashMap<String, String>();
						 tokenInputs.put("intimationNo", intimationNumber);
						 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
						 String billDtlsToken = null;
						  try {
							  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
						  } catch (NoSuchAlgorithmException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (ParseException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  tokenInputs = null;
						  dto.setBillDtslToken(billDtlsToken);
		                 billDetails.add(dto);
						}
						
						if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
							FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
							dto1.setSno(snoFinance);
							snoFinance++;
							dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
							Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
							if(approvedAmount >0){
								dto1.setAmount(String.valueOf(approvedAmount));
						     }
		//					if(reimbursement.getFinancialCompletedDate() != null){
							dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
							
							if(reimbursement.getFinancialApprovedAmount() != null){
								dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
							}
							
		//					}
							dto1.setStatus(reimbursement.getStatus().getProcessValue());
			                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
			                dto1.setRodKey(reimbursement.getKey());
			                dto1.setRodNumber(reimbursement.getRodNumber());
			                			                
			                if (reimbursement.getDocAcknowLedgement() != null) {
			                	DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
			                	if (reimbursement.getReconsiderationRequest() != null && reimbursement.getReconsiderationRequest().equalsIgnoreCase("Y")) {
			                		Long version = reimbursement.getVersion() != null ? reimbursement.getVersion() - 1l : 1l;
			                		dto1.setRodType("Reconsideration - " + version);
			                	} else {
			                		dto1.setRodType("Original");
			                	}
			                	
			                	if (docAcknowledgement.getDocumentReceivedFromId() != null) {
			                		dto1.setDocumentReceivedFrom(docAcknowledgement.getDocumentReceivedFromId().getValue());
			                	}
			                	 
			                	if (docAcknowledgement != null) {
			                		String billClassificationValue = SHAUtils.getBillClassificationValue(docAcknowledgement);
			                		dto1.setBillClassification(billClassificationValue);
			                	}
			                }
			                
			                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
			                {
			                	ClaimPayment claimPayment;	
			                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//			                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
			                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
			                	
			                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
			                	{
			                		claimPayment = claimPaymetList.get(0);
			                		dto1.setPaymentType(claimPayment.getPaymentType());
			                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
			                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
			                		
			                	}
			                	
			                }
			                
			                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
			                	dto1.setAmount("0");
			                }
			                
//			                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
			                financialDetails.add(dto1);
						}
					}
				}
			}
			 
			request.setAttribute("billing", billDetails);
			request.setAttribute("financial", financialDetails);
			
			/* Payment Details */
			 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
			 	
			 	
			 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
				
				List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
				
				List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
				int snum =1;
				for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
					if(paymentProcessCpuTableDTO2.getChequeDate() != null){
						paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
						
					}
					paymentProcessCpuTableDTO2.setSerialNo(snum);
					
					Reimbursement reimbursement = acknowledgementDocumentsReceivedService.getReimbursementByRodNo(paymentProcessCpuTableDTO2.getRodNo());
					
					if (reimbursement !=null && reimbursement.getDocAcknowLedgement() != null) {
						DocAcknowledgement docAcknowledgement = reimbursement.getDocAcknowLedgement();
						if (reimbursement.getReconsiderationRequest() != null && reimbursement.getReconsiderationRequest().equalsIgnoreCase("Y")) {
							Long version = reimbursement.getVersion() != null ? reimbursement.getVersion() - 1l : 1l;
							paymentProcessCpuTableDTO2.setRodType("Reconsideration - " + version);
						} else {
							paymentProcessCpuTableDTO2.setRodType("Original");
						}
		           	 
		           	    if (docAcknowledgement != null) { 
		           	    	String billClassificationValue = SHAUtils.getBillClassificationValue(docAcknowledgement);
		           	    	paymentProcessCpuTableDTO2.setBillClassification(billClassificationValue);
		           	    }
		           	 
		           	 
					}
					paymentList.add(paymentProcessCpuTableDTO2);
					snum++;
				}
				
				request.setAttribute("payment", paymentList);
				
				
				//View Details
				
				List<Reimbursement> reimbList = acknowledgementDocumentsReceivedService
						.findReimbursementByClaimKey(claimforIntimation.getKey());

				if (reimbList != null && !reimbList.isEmpty()) {
					viewCrcQueryDTOvalues = new ArrayList<ViewQueryDTO>();
					for (Reimbursement reimbursement : reimbList) {
						setCrcQueryValues(reimbursement.getKey(), viewCrcQueryDTOvalues, claimforIntimation);
					}
				}
				
				request.setAttribute("crcQueryList", viewCrcQueryDTOvalues);
				
				
				if (intimationByNo != null && intimationByNo.getPolicy() != null
						&& intimationByNo.getPolicy().getPolicyNumber() != null) {

					PremPolicySchedule fetchPolicyScheduleFromPremia = policyservice
							.fetchPolicyScheduleFromPremia(intimationByNo.getPolicy()
									.getPolicyNumber(), 0);
					if (fetchPolicyScheduleFromPremia != null
							&& fetchPolicyScheduleFromPremia.getResultUrl() != null) {
						String url = fetchPolicyScheduleFromPremia.getResultUrl();
						request.setAttribute("policyUrl",url);
					}

				}
			 
		 }
	  
	  //Bonus Logic 
	  if (intimationByNo != null && intimationByNo.getPolicy().getPolicyNumber() != null && 
				ReferenceTable.getCumulativeProductBonusList().contains(intimationByNo.getPolicy().getProduct().getCode())) {
		  
		//Bancs Changes Start
			Policy policyObj = null;
			Builder builder = null;
			String strDmsViewURL = null;
			
			if(intimationByNo.getPolicy().getPolicyNumber() != null){
				policyObj = policyService.getByPolicyNumber(intimationByNo.getPolicy().getPolicyNumber());
				if (policyObj != null) {
					 if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						strDmsViewURL = BPMClientContext.BANCS_CUMULATIVE_BONUS_URL;
						strDmsViewURL = strDmsViewURL.replace("POLICY", policyObj.getPolicyNumber());		
						if(policyObj != null && policyObj.getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)){
							strDmsViewURL = strDmsViewURL.replace("MEMBER", intimationByNo.getInsured() !=null?String.valueOf(intimationByNo.getInsured().getSourceRiskId()!=null?intimationByNo.getInsured().getSourceRiskId():""):"");
							}else{
								strDmsViewURL = strDmsViewURL.replace("MEMBER","");	
							}
						System.out.println("Bancs Bonus URL" + strDmsViewURL);
					}else{
						strDmsViewURL = BPMClientContext.CUMULATIVE_BONUS_URL;
						strDmsViewURL = strDmsViewURL.concat(intimationByNo.getPolicyNumber());
					}
				}
			}
			//Bancs Changes End
		   // String strDmsViewURL = BPMClientContext.CUMULATIVE_BONUS_URL;
			
			BrowserFrame browserFrame = new BrowserFrame("",
				    new ExternalResource(strDmsViewURL));
			request.setAttribute("bonusLogicUrl", strDmsViewURL);
	  }
		 
		 //Portability Policy
		 /*if(!intimationNumber.isEmpty()){
		    intimationByNo = intimationService.getIntimationByNo(intimationNumber);
		    List<PortablitiyPolicyDTO> viewPortablityDetails = new ArrayList<PortablitiyPolicyDTO>();
		    List<PortablitiyPolicyDTO> portablityDetails = preauthService.getPortablityDetails(intimationByNo.getPolicy().getPolicyNumber());
		    int serialNo = 1;
		    for (PortablitiyPolicyDTO viewPortablityDetail : portablityDetails){
		    	viewPortablityDetail.setSerialNo(serialNo);
		    	viewPortablityDetail.setPortabilityJson(mapper.writeValueAsString(viewPortablityDetail));
		    	viewPortablityDetails.add(viewPortablityDetail);
		    	serialNo++;
		    }
		    request.setAttribute("portablityDetails",viewPortablityDetails);	
		    
		 }*/
		 if(!intimationNumber.isEmpty()){
		/*	    MasCopay copay = coPayService.getByProduct(intimationByNo.getPolicy()
						.getProduct().getKey());*/
			 
			 //added for CRC view-IMSSUPPOR-27994
			 MasCopay copay; 
			 if(intimationByNo.getPolicy().getProduct().getKey() != null && (intimationByNo.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)||
						intimationByNo.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER))) {
				 copay = coPayService.getByProductAndSi(intimationByNo.getPolicy().getProduct().getKey(),policy.getTotalSumInsured()!= null ? policy.getTotalSumInsured() : 0);
				} else {
					copay = coPayService.getByProduct(intimationByNo.getPolicy()
						.getProduct().getKey());
				}
			 //for CRC view-IMSSUPPOR-27994
			    Date strDob = intimationByNo.getInsured().getInsuredDateOfBirth();
			    if(copay != null && policy != null  ){
			    	compulsaryCopayPercent = (String) (copay.getMaxPercentage() == null ? 0 : copay.getMaxPercentage().toString() + "%");
			    	if(copay.getEntryAgeFrom()==null || copay.getEntryAgeFrom() <= Long.parseLong(SHAUtils.getAge(strDob))){
			    		compulsaryCopayAmount = (double) (((policy.getTotalSumInsured()!= null ? policy.getTotalSumInsured() : 0 )* (copay.getMaxPercentage()!= null ? copay.getMaxPercentage() : 0 )) / 100);
			    		totalCopay = compulsaryCopayAmount + voluntaryCopayAmount;
			    	}
			    	 else 
					    {
					    	compulsaryCopayAmount = 0d;
					    	voluntaryCopayAmount = 0d;
					    	totalCopay = 0d;
					    	compulsaryCopayPercent="0";
					    	voluntaryCopayPercent="0";
					    }
			    }
			    else 
			    {
			    	compulsaryCopayAmount = 0d;
			    	voluntaryCopayAmount = 0d;
			    	totalCopay = 0d;
			    	compulsaryCopayPercent="0";
			    	voluntaryCopayPercent="0";
			    }
		 }
		 request.setAttribute("compulsaryCopayPercent",compulsaryCopayPercent);
		 request.setAttribute("voluntaryCopayPercent",voluntaryCopayPercent);
		 request.setAttribute("totalCopay",totalCopay);
 

	  	BPMClientContext bpmClientContext = new BPMClientContext();
	  	 if(null != claimforIntimation){
	  		 
	  		Map<String,String> tokenInputs = new HashMap<String, String>();
			 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
			 String intimationNoToken = null;
			  try {
				  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
			  } catch (NoSuchAlgorithmException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  } catch (ParseException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  tokenInputs = null;
			String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
			/*Below code commented due to security reason
			String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
			//getUI().getPage().open(url, "_blank");
			request.setAttribute("dmsUrl",url);
		
			if(hospitals.getHospitalCode() != null){
			String packageUrl = bpmClientContext.getHospitalPackageDetails() + hospitals.getHospitalCode();
			request.setAttribute("packageUrl",packageUrl);
		}
		
		if(intimationByNo.getPolicy().getPolicyNumber() != null){
			String strDmsViewURL = bpmClientContext.getDMSViewUrl() + intimationByNo.getPolicy().getPolicyNumber();	
			request.setAttribute("dmsViewUrl",strDmsViewURL);
		}
		
		
		
	  	 }
		String queryString = request.getQueryString();
		String rodNo = request.getParameter("reimb_num");
		String cashlessNo = request.getParameter("cashless_num");
		
		if(!error.isEmpty()){
			  request.setAttribute("error", error);
			  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
		  } 
		  else {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  request.getRequestDispatcher("/WEB-INF/ViewCRCPortal.jsp").include(request, response);
			  }
	  }
//	  else{
//		  if(intimationNumber.isEmpty()){
//			  error += "Please Enter Intimation Number";
//		  }
////	      else if(healthCardNo.isEmpty()){
////	    	  error += "Please Enter  Health Card Number";
////		  }
////	      else if(intimationNumber.isEmpty()){
////			  error += "Please Enter Intimation Number";
////			  
////		  }
//			 request.setAttribute("error", error);
//			 request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
//	  }

	
	}
	
	/*CR2019064*/
	private void showWebsitePortalPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String daignosisName = "";
		  String error = "";
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Reimbursement remibNo = null;
		  Policy policy = null;
		  Hospitals hospitals = null;
		  ViewRejectionDTO rejectioDto = null;
		  List<ViewQueryDTO> viewQueryDTOvalues = null;
		  List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList= null;
		  
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		  if(!intimationNumber.isEmpty()){
			 
//			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  policy = policyservice.getPolicyByPolicyNubember(intimationByNo.getPolicyNumber());
			 
			 /* if(null == intimationByNo && intimationNumber == null){
				  error = "Please Enter valid Intimation No and Health Card Number </br>"; 
			  	}*/
			  
			 
			  if(error.isEmpty()){
				  if(intimationByNo != null) {
				  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
				  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
				  if(claimforIntimation != null) {
					  	List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
								.getKey());
						if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
							preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
							
							if(preauth.getStatus().getKey() != null && (preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)		
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)
									|| preauth.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS))){		
								preauth.setTotalApprovalAmount(0d);		
							
							}
						}
				    }
				  }
				  
			  }
			  
		 
		
		  if(null != preauth){
			  
			 
			  
			  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
		  
			  int i=1;
			  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
				  if(diagnosis.size() == i){
					  daignosisName += pedDetailsTableDTO.getDiagnosisName();
				  } else {
					  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
				  }

				  i++;
			  }
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("preauth", preauth);
		  }
		  
		  if(null != claimforIntimation){
			  Integer snoPreauthSummary=1;
			  List<PreviousPreAuthTableDTO> previousPreauthList = previousPreAuthService
						.searchFromTmpPreauth(claimforIntimation.getKey());
			  
				List<Long> keys = new ArrayList<Long>();
				if(null != previousPreauthList && !previousPreauthList.isEmpty()){
					for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
						keys.add(previousPreAuthTableDTO.getKey());
					}
				}
			  
			  ViewTmpPreauth prevpreauth = null;
				Long preauthKey = null;
				Double reqAmount=new Double(0);
				Double approvedAmount=new Double(0);
				if (!keys.isEmpty()) {
					preauthKey = Collections.max(keys);

					prevpreauth = preauthService.getViewTmpPreauthById(preauthKey);
				}
				
			List<PreviousPreAuthTableDTO> newList = new ArrayList<PreviousPreAuthTableDTO>();
			for (PreviousPreAuthTableDTO previousPreAuthTableDTO : previousPreauthList) {
				previousPreAuthTableDTO.setSno(snoPreauthSummary);
				snoPreauthSummary++;
				previousPreAuthTableDTO.setRequestedAmt(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreAuthTableDTO.getKey(), previousPreAuthTableDTO.getClaimKey()));
				reqAmount = reqAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getRequestedAmt()));
				approvedAmount  = approvedAmount+new Double(SHAUtils.getDoubleValueFromString(previousPreAuthTableDTO.getApprovedAmt()));
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("cashlessKey", previousPreAuthTableDTO.getKey().toString());
				 String cashlessDocToken = null;
				  try {
					  cashlessDocToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  previousPreAuthTableDTO.setCashlessDocToken(cashlessDocToken);
				newList.add(previousPreAuthTableDTO);

			}
			
			request.setAttribute("cashless", newList);
			if(previousPreauthList != null) {
			request.setAttribute("requestedAmt", reqAmount);
			request.setAttribute("totalApprovedAmount", approvedAmount);
			}
		  }
		  
		  if(rodNUmber != null) {
				 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
				  rejectioDto = new ViewRejectionDTO();
				 if(reimbursementKey != null) {
				 ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reimbursementKey.getKey());
				 
				 if(rejectionKeyByReimbursement != null) {
					rejectioDto.setRejectionRemarks(rejectionKeyByReimbursement.getRejectionRemarks());
					rejectioDto.setLetterRectionRemarks(rejectionKeyByReimbursement.getRejectionLetterRemarks());
					String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
					String rejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getRejectionDraftDate());
					//rejectioDto.setRejectedDate(Long.valueOf(rejectDate));
//					rejectioDto.setReDraftedDate(rejectionKeyByReimbursement.getRedraftDate().toString());
					rejectioDto.setRejectedDate(rejectDate);
					String approverejectDate = SHAUtils.formatDate(rejectionKeyByReimbursement.getApprovedRejectionDate());
					rejectioDto.setApproveRejectionDate(approverejectDate);
					rejectioDto.setReDraftRemarks(rejectionKeyByReimbursement.getRedraftRemarks());
					rejectioDto.setRejectionStatus(rejectionKeyByReimbursement.getStatus().getProcessValue());
					rejectioDto.setRejectedName(rejectionKeyByReimbursement.getCreatedBy());
					
				 }
				
				 }
				 String  admisionDate = SHAUtils.formatDate(intimationByNo.getAdmissionDate());
					rejectioDto.setAdmissionDate(admisionDate);
				 request.setAttribute("rejectList",rejectioDto);
				}
		  
		  if(null != claimforIntimation){
			  
			 request.setAttribute("claim", claimforIntimation);
			  
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<ViewDocumentDetailsDTO> listDocumentDetails = acknowledgementDocumentsReceivedService.getReceiptOfMedicalApproverByReimbursement(claimforIntimation.getKey(),reimkey);
			 List<ViewDocumentDetailsDTO> acknowledgeDocsList = null;
			 
			 docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
			 
			 List<ViewRejectionDTO> viewRejectionList = new ArrayList<ViewRejectionDTO>();
			 
			 
			 for(ViewDocumentDetailsDTO docAcknowledgement : listDocumentDetails){
				 
				 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
				 
				 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
				 tableDTO.setReimbursementNo(docAcknowledgement.getRodNumber());
				 //tableDTO.setDocumentReceivedFrom(docAcknowledgement.getD);
//				 tableDTO.setDocumentReceivedDate(docAcknowledgement.getDocumentReceivedDate().toString());
				 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
				 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
				 }
				 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceipt().getValue());
				 tableDTO.setBillClassification(docAcknowledgement.getBillClassification());
				 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocAcknowledgement().getDocumentReceivedFromId().getValue());
				 tableDTO.setMedicalResponseDate(docAcknowledgement.getMedicalResponseTime());
				 tableDTO.setStatus(docAcknowledgement.getWebsiteStatusVal());
				 tableDTO.setReimbursementKey(docAcknowledgement.getReimbursementKey());
				 tableDTO.setTypeOfClaim(claimforIntimation.getClaimType().getValue());			
				 tableDTO.setAmount(docAcknowledgement.getApprovedAmount());
				 
				 if(docAcknowledgement.getStrLastDocumentReceivedDate() != null) {
					 tableDTO.setReminderDate(SHAUtils.formatIntimationDate(docAcknowledgement.getStrLastDocumentReceivedDate())); 
					 }
				 
				/* Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(docAcknowledgement.getReimbursementKey());
				 tableDTO.setAmount(Double.valueOf(approvedAmount));
				 */
				 acknowledgeDocsList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(docAcknowledgement.getReimbursementKey());
				 
				 Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationNumber);
				 tokenInputs.put("reimbursementkey", docAcknowledgement.getReimbursementKey().toString());
				 String ackToken = null;
				  try {
					  ackToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				  tableDTO.setAcknowledgementToken(ackToken);
			
				 docAcknowledgementList.add(tableDTO);
			
			 }
			 	
				 request.setAttribute("reimbursementList", docAcknowledgementList);
		 
				 if(rodNUmber != null) {
					 Reimbursement reimbursementKey = getReimbursementObject(rodNUmber);
					 
					 if(reimbursementKey != null){
						 
						 List<ViewQueryDTO> QuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey.getKey());
						 Hospitals hospitalDetails=null; 
						 
						 String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(reimbursementKey.getKey());
						 viewQueryDTOvalues = new ArrayList<ViewQueryDTO>();
						 //need to implement
						 if(claimforIntimation != null){
							 Long hospitalKey = claimforIntimation.getIntimation().getHospital();
							 hospitalDetails = hospitalService.getHospitalById(hospitalKey);
						 }	
						 
						 for (ViewQueryDTO viewQueryDTO : QuerytableValues) {
							 String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
							 viewQueryDTO.setQueryDate(rejectDate);
							 viewQueryDTO.setDiagnosis(diagnosisName);
							 
							 if(hospitalDetails != null){
								 viewQueryDTO.setHospitalName(hospitalDetails.getName());
								 viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
								 viewQueryDTOvalues.add(viewQueryDTO);
							 }
//							viewQueryDTO.setClaim(claimforIntimation);
							 
							 
						 }
						 request.setAttribute("queryList", viewQueryDTOvalues);
					 }
					 
					
				 }
				 
			  
			List<ViewClaimHistoryDTO> claimHistoryForReimbursement = viewClaimHistoryService.getClaimHistoryForReimbursement(claimforIntimation.getKey(), reimkey,ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
			 
			 List<ViewClaimHistoryDTO> trailsList = new ArrayList<ViewClaimHistoryDTO>();
			 for (ViewClaimHistoryDTO viewClaimHistoryDTO : claimHistoryForReimbursement) {
				 
				 ViewClaimHistoryDTO viewHistoryDto = new ViewClaimHistoryDTO();
				 
				 viewHistoryDto.setTypeofClaim(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setReimbursementKey(viewClaimHistoryDTO.getReimbursementKey());
				 viewHistoryDto.setUserID(viewClaimHistoryDTO.getUserID());
				 viewHistoryDto.setUserName(viewClaimHistoryDTO.getUserName());
				 viewHistoryDto.setUserRemark(viewClaimHistoryDTO.getUserRemark());
				 viewHistoryDto.setClaimStage(viewClaimHistoryDTO.getClaimStage());
				 viewHistoryDto.setStatus(viewClaimHistoryDTO.getStatus());
				 trailsList.add(viewHistoryDto);
			}
			 
			 request.setAttribute("viewtrailsList", trailsList);
			 
			 
			 List<ViewDocumentDetailsDTO> viewAcknowledgementList = acknowledgementDocumentsReceivedService.getViewAcknowledgementList(reimkey);
			 
			 Map<String, String> rodRemarks =new HashMap<String, String>();
			 Map<String, String> remarksReimbursement =new HashMap<String, String>();
			 List<Reimbursement> finacialList = new ArrayList<Reimbursement>();
			 
			 //Financial List
			 List<ViewTmpReimbursement> reimbursementDetails = acknowledgementDocumentsReceivedService
						.getViewTmpRimbursementDetails(claimforIntimation.getKey());

				List<ViewClaimStatusDTO> billDetails = new ArrayList<ViewClaimStatusDTO>();
				List<FinancialApprovalTableDTO> financialDetails = new ArrayList<FinancialApprovalTableDTO>();
				if (reimbursementDetails != null) {
					int i =1;
					int snoFinance = 1;
					for (ViewTmpReimbursement reimbursement : reimbursementDetails) {
						rodRemarks.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
						if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey())){
							//////newly added for fa and paymentremarks//////
							if(!ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) && 
							ReferenceTable.FINANCIAL_APPROVE_STATUS.equals(reimbursement.getStatus().getKey())){
							remarksReimbursement.put(reimbursement.getRodNumber(), reimbursement.getFinancialApprovalRemarks());
							
                         }
							if(reimbursement.getBillingCompletedDate() != null && reimbursement.getBillingApprovedAmount() != null){
			                 ViewClaimStatusDTO dto = new ViewClaimStatusDTO();
			                 
			                 dto.setSno(i);
			                 i++;
			                 if(null != reimbursement.getClaim()){
			                 dto.setBillingType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement"); 
			                 }
			//                 if(null != reimbursement.getBillingApprovedAmount()){
			//                 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			//                 }
			                 
			                 Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto.setBillAssessmentAmt(String.valueOf(approvedAmount));
							}
			//                 if(reimbursement.getBillingCompletedDate() != null){
			                 dto.setBillingDate(SHAUtils.formatDate(reimbursement.getBillingCompletedDate()));
			                 if(reimbursement.getBillingApprovedAmount() != null){
			                	 dto.setBillAssessmentAmt(String.valueOf(reimbursement.getBillingApprovedAmount()));
			                 }
			//                 }
//			                 dto.setStatus(reimbursement.getStatus().getProcessValue());
			                 dto.setStatus(reimbursement.getStatus().getWebsiteStatus()); //CR2019064
			                 dto.setRodKey(reimbursement.getKey());
			                 Map<String,String> tokenInputs = new HashMap<String, String>();
							 tokenInputs.put("intimationNo", intimationNumber);
							 tokenInputs.put("reimbursementkey", reimbursement.getKey().toString());
							 String billDtlsToken = null;
							  try {
								  billDtlsToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
							  } catch (NoSuchAlgorithmException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  } catch (ParseException e) {
								  // TODO Auto-generated catch block
								  e.printStackTrace();
							  }
							  tokenInputs = null;
							  dto.setBillDtslToken(billDtlsToken);
			                 billDetails.add(dto);
							}
							
							if(reimbursement.getFinancialCompletedDate() != null && reimbursement.getFinancialApprovedAmount() != null){
								FinancialApprovalTableDTO dto1 = new FinancialApprovalTableDTO();
								dto1.setSno(snoFinance);
								snoFinance++;
								dto1.setApprovalType(reimbursement.getClaim().getClaimType() != null ? reimbursement.getClaim().getClaimType().getValue() : "Reimbursement");
								Long approvedAmount = reimbursementService.getReimbursementApprovedAmount(reimbursement.getKey());
								if(approvedAmount >0){
									dto1.setAmount(String.valueOf(approvedAmount));
							     }
			//					if(reimbursement.getFinancialCompletedDate() != null){
								dto1.setFaDate(SHAUtils.formatDate(reimbursement.getFinancialCompletedDate()));
								
								if(reimbursement.getFinancialApprovedAmount() != null){
									dto1.setAmount(String.valueOf(reimbursement.getFinancialApprovedAmount()));
								}
								
			//					}
//								dto1.setStatus(reimbursement.getStatus().getProcessValue());
								dto1.setStatus(reimbursement.getStatus().getWebsiteStatus()); //CR2019064
				                dto1.setFinancialRemarks(reimbursement.getFinancialApprovalRemarks());
				                dto1.setRodKey(reimbursement.getKey());
				                
				                if(reimbursement.getDocAcknowLedgement() != null && 
				                		reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId() != null){
				                	dto1.setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue());
				                }
				                
				                if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_SETTLED))
				                {
				                	ClaimPayment claimPayment;	
				                	PaymentProcessCpuService paymentDetails = new PaymentProcessCpuService();
//				                	List<ClaimPayment> paymentDetailsByClaimNumberForView = paymentProcessCpuService.getPaymentDetailsByClaimNumberForView(reimbursement.getClaim().getClaimId());
				                	List<ClaimPayment> claimPaymetList = getPaymentDetailsByRodNumber(reimbursement.getRodNumber(),ReferenceTable.PAYMENT_SETTLED);
				                	
				                	if(null != claimPaymetList && !claimPaymetList.isEmpty())
				                	{
				                		claimPayment = claimPaymetList.get(0);
				                		dto1.setPaymentType(claimPayment.getPaymentType());
				                		dto1.setTransactionDate(SHAUtils.formatDate(claimPayment.getPaymentDate()));
				                		dto1.setTransactionNo(claimPayment.getChequeDDNumber());
				                		
				                	}
				                	
				                }
				                
				                if(ReferenceTable.CLOSE_CLAIM_STATUS_KEYS.containsKey(reimbursement.getStatus().getKey())){
				                	dto1.setAmount("0");
				                }
				                
//				                dto1.setAmount(String.valueOf(reimbursement.getCurrentProvisionAmt()));
				                financialDetails.add(dto1);
							}
						}
					}
				}
				 
				request.setAttribute("billing", billDetails);
				request.setAttribute("financial", financialDetails);
				
				/* Payment Details */
				 	List<PaymentProcessCpuTableDTO> paymentList = new ArrayList<PaymentProcessCpuTableDTO>();
				 	
				 	
				 	Claim claim = claimService.getClaimByKey(claimforIntimation.getKey());
				 	
				 	
				 	
					
					List<ClaimPayment> paymentDetailsByClaimNumberForView = getPaymentDetailsByClaimNumberForView(claim.getClaimId());
					
					List<PaymentProcessCpuTableDTO> paymentProcessCpuTableDto = PaymentProcessCpuMapper.getInstance().getpaymentCpuTableObjects(paymentDetailsByClaimNumberForView);
					int snum =1;
					for (PaymentProcessCpuTableDTO paymentProcessCpuTableDTO2 : paymentProcessCpuTableDto) {
						if(paymentProcessCpuTableDTO2.getChequeDate() != null){
							paymentProcessCpuTableDTO2.setChequeDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getChequeDate()));
							
						}if(paymentProcessCpuTableDTO2.getFaApprovalDate() != null){
							paymentProcessCpuTableDTO2.setFaApprovalDateValue(SHAUtils.formatDate(paymentProcessCpuTableDTO2.getFaApprovalDate()));
							
						}
						if(paymentProcessCpuTableDTO2.getRodNo() != null){
							String remarks = remarksReimbursement.get(paymentProcessCpuTableDTO2.getRodNo());
							paymentProcessCpuTableDTO2.setFinancialRemarks(remarks);
						}
						paymentProcessCpuTableDTO2.setSerialNo(snum);
						paymentList.add(paymentProcessCpuTableDTO2);
						snum++;
					}
					
					request.setAttribute("payment", paymentList);
				 
			 }
		  	BPMClientContext bpmClientContext = new BPMClientContext();
		  	 if(null != claimforIntimation){
		  		Map<String,String> tokenInputs = new HashMap<String, String>();
				 tokenInputs.put("intimationNo", intimationByNo.getIntimationId());
				 String intimationNoToken = null;
				  try {
					  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
				  } catch (NoSuchAlgorithmException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  } catch (ParseException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				  }
				  tokenInputs = null;
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
				/*Below code commented due to security reason
				String url = bpmClientContext.getGalaxyDMSUrl() + intimationByNo.getIntimationId();*/
				//getUI().getPage().open(url, "_blank");
				request.setAttribute("dmsUrl",url);
		  	 }
			String queryString = request.getQueryString();
			String rodNo = request.getParameter("reimb_num");
			String cashlessNo = request.getParameter("cashless_num");
			
			if(!error.isEmpty()){
				  request.setAttribute("error", error);
				  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
			  } 
			  else {
				  request.setAttribute("intimation", intimationByNo);
				  request.setAttribute("hospitals", hospitals);
				  request.getRequestDispatcher("/WEB-INF/ViewWebsitePortalPage.jsp").include(request, response);
				  }
		  }

	}
	
	private void showCashlessDocPage(String intimationNumber, Long cashlessKey, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String error="";
		
		
		 List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist = new ArrayList<DMSDocumentDetailsDTO>();
			dmsDocumentDetailsDTOlist = billDetailsService.getDocumentDetailsData(intimationNumber, 0);
			
		Preauth preauthDetails = claimService.getPreauthListByKey(cashlessKey);
		
		DMSDocumentDetailsDTO docDetails = new DMSDocumentDetailsDTO();
		
		if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("PreauthInitialApprovalLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("CashlessEnhancementApprovalLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.PREAUTH_REJECT_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("PreauthRejectionLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && (preauthDetails.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_PRE_AUTH_QUERY_STATUS))){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("PreauthQueryLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && (preauthDetails.getStatus().getKey().equals(ReferenceTable.PREAUTH_QUERY_STATUS))){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("PreauthQueryLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.PREAUTH_DENIAL_OF_CASHLESS_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("PreauthDenialLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_REJECT_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementRejectionLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.WITHDRAW_APPROVED_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementWithDrawLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		
		
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_WITHDRAW_AND_REJECT)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementWithDrawAndRejectionLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_QUERY_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("PreauthQueryLetter") && dmsDocumentDetailsDTO.getDocumentSource().equalsIgnoreCase("PreMedicalPreAuth")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementDownsizeLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementWithDrawLetter") && dmsDocumentDetailsDTO.getDocumentSource().equalsIgnoreCase("PreAuth")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_POST_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementWithDrawLetter") && dmsDocumentDetailsDTO.getDocumentSource().equalsIgnoreCase("PreAuth")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.STANDALONE_WITHDRAW_AND_REJECT_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementWithDrawAndRejectionLetter") && dmsDocumentDetailsDTO.getDocumentSource().equalsIgnoreCase("PreAuth")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.DOWNSIZE_APPROVED_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("EnhancementDownsizeLetter")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
		else if(preauthDetails != null && preauthDetails.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_QUERY_STATUS)){
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
				if(dmsDocumentDetailsDTO.getDocumentType().equalsIgnoreCase("CashlessEnhancementQueryLetter") && dmsDocumentDetailsDTO.getDocumentSource().equalsIgnoreCase("PreAuth")) {
					docDetails = dmsDocumentDetailsDTO;
				}
			}
		}
    if( docDetails.getDmsDocToken() == null){
    	  request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response); 
    	
    }
    else{
		request.setAttribute("preauthDocument", docDetails);
		request.setAttribute("dmrestApiUrl", docDetails.getDmsRestApiURL());
		request.setAttribute("fileViewUrl", docDetails.getDmsDocToken());
		
		
		request.getRequestDispatcher("/WEB-INF/ViewPreauthDocument.jsp").forward(request, response);
    }
	}
	private void showQueryLetter(String intimationNumber,String rodNumber, String rodKey,HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String error="";
		Long reimbursementKey = Long.valueOf(rodKey);
		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		BPMClientContext context = new BPMClientContext();
		String dmsAPIUrl = context.getDMSRestApiUrl();
		DMSDocumentDetailsDTO querywithDocs = new DMSDocumentDetailsDTO();
		querywithDocs.setDmsRestApiURL(dmsAPIUrl);
		Reimbursement reimDtls = getReimbursementObjectByKey(reimbursementKey);
		 List<ViewQueryDTO> querytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(reimbursementKey);
		if(querytableValues != null && !querytableValues.isEmpty()){
			DocumentDetails dmsDocumentDetailsDTO = billDetailsService
				.getReimbursementQueryDocumentDetailsData(intimationNumber,reimDtls.getRodNumber(),SHAConstants.REIMBURSEMENT_QUERY_LETTER);
			if(dmsDocumentDetailsDTO != null && dmsDocumentDetailsDTO.getDocumentToken() != null && dmsDocumentDetailsDTO.getDocumentType() != null){
			 Long imageUrl = dmsDocumentDetailsDTO.getDocumentToken();
			 String documentType = dmsDocumentDetailsDTO.getDocumentType();
				request.setAttribute("fileViewUrl", imageUrl);
				request.setAttribute("preauthDocument", querywithDocs);
				request.setAttribute("dmrestApiUrl", querywithDocs.getDmsRestApiURL());
				
				request.getRequestDispatcher("/WEB-INF/ViewPreauthDocument.jsp").forward(request, response);
			}
		}
		  else{
			    request.setAttribute("error", error);
				request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
		}
	}	
	
	private void showRejectionLetter(String intimationNumber,String rodNumber, String rodKey, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String error="";
		Long reibursementKey = Long.valueOf(rodKey);
		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		DMSDocumentDetailsDTO querywithDocs = new DMSDocumentDetailsDTO();
		BPMClientContext context = new BPMClientContext();
		String dmsAPIUrl = context.getDMSRestApiUrl();
		querywithDocs.setDmsRestApiURL(dmsAPIUrl);
		Reimbursement reimDtls = getReimbursementObjectByKey(reibursementKey);
		DocumentDetails dmsDocumentDetailsDTO = billDetailsService
				.getReimbursementRejectionDocumentDetailsData(intimationNumber,reimDtls.getRodNumber(),SHAConstants.REIMBURSEMENT_REJECTION_LETTER);
		ReimbursementRejection rejectionKeyByReimbursement = reimbursementRejectionService.getRejectionKeyByReimbursement(reibursementKey);
		if(rejectionKeyByReimbursement != null){
			if(dmsDocumentDetailsDTO != null && dmsDocumentDetailsDTO.getDocumentToken() != null && dmsDocumentDetailsDTO.getDocumentType() != null){
			 Long imageUrl = dmsDocumentDetailsDTO.getDocumentToken();
			 String documentType = dmsDocumentDetailsDTO.getDocumentType();
				    request.setAttribute("fileViewUrl", imageUrl);
					request.setAttribute("preauthDocument", querywithDocs);
					request.setAttribute("dmrestApiUrl", querywithDocs.getDmsRestApiURL());
				
				request.getRequestDispatcher("/WEB-INF/ViewPreauthDocument.jsp").forward(request, response);
			
			} 
		}else{
			request.setAttribute("error", error);
			request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
		}
	}
	
	public void setCrcQueryValues(Long key, List<ViewQueryDTO> viewCrcQueryDTOvalues, Claim claimforIntimation){

		List<ViewQueryDTO> crcQuerytableValues = acknowledgementDocumentsReceivedService.getQueryDetails(key);
		Hospitals hospitalDetails=null; 

		String diagnosisName = acknowledgementDocumentsReceivedService.getDiagnosisName(key);
		
		//need to implement
		if(claimforIntimation != null){
			Long hospitalKey = claimforIntimation.getIntimation().getHospital();
			hospitalDetails = hospitalService.getHospitalById(hospitalKey);
		}	

		for (ViewQueryDTO viewQueryDTO : crcQuerytableValues) {
			String rejectDate = SHAUtils.formatDate(viewQueryDTO.getQueryRaisedDate());
			viewQueryDTO.setQueryDate(rejectDate);
			viewQueryDTO.setDiagnosis(diagnosisName);

			if(hospitalDetails != null){
				viewQueryDTO.setHospitalName(hospitalDetails.getName());
				viewQueryDTO.setHospitalCity(hospitalDetails.getCity());
				viewCrcQueryDTOvalues.add(viewQueryDTO);
			}
			//		viewQueryDTO.setClaim(claimforIntimation);


		}

	}
}