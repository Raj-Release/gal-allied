package com.shaic.main;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.reimbursement.dto.ReceiptOfDocumentAndMedicalProcess;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuService;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;

public class ClaimRejectionStatusServlet extends HttpServlet {

	

	  @Inject
	  private ClaimService claimService;
	  
	  @Inject
	  private IntimationService intimationService;
	  
	  @EJB
	  private HospitalService hospitalService;
	  
	  @EJB
	  private PaymentProcessCpuService paymentProcessCpuService;
	  
	  @EJB
	  private PreauthService preauthService;
	  
	  @EJB
	  private DiagnosisService diagnosisService;
	  
	  @EJB
	  private AcknowledgementDocumentsReceivedService acknowledgementDocumentsReceivedService;
	  
	  @EJB
	  private ReimbursementService reimbursementService;
	  
	  @EJB
	  private ReimbursementQueryService reimbursementQueryService;
	  
	  @EJB
	  private MasterService masterService;
	  
	  private final String USER_AGENT = "Mozilla/5.0";
	  
	  protected void doGet(HttpServletRequest request, 
	      HttpServletResponse response) throws ServletException, IOException 
	  {
		  String queryString = request.getQueryString();
		  if(queryString != null) {
			  String[] split = queryString.split("=");
			  String intimationNumber = split[1];
			  showPage(intimationNumber, request, response);
		  }
//	    request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
	  }
	  
	  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
		  String intimationNumber =  (String) request.getParameter("intimationNumber");
		  String healthCardNo = (String) request.getParameter("idCardNumber");
		  	showPage(intimationNumber, request, response);
		  }

	private void showPage(String intimationNumber, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			String daignosisName = "";
		  String error = "";
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Hospitals hospitals = null;
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		  if(!intimationNumber.isEmpty()){
			 
//			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			  intimationByNo = intimationService.getIntimationByNo(intimationNumber);
			 
			  if(null == intimationByNo){
				  error = "Please Enter valid Intimation No </br>"; 
			  	}
			  
			 
			  if(error.isEmpty()){
				  claimforIntimation = claimService.getClaimforIntimation(intimationByNo.getKey());
				  hospitals = hospitalService.getHospitalDetailsByKey(intimationByNo.getHospital());
				  if(claimforIntimation != null) {
					  preauth = preauthService.getLatestPreauthByClaim(claimforIntimation.getKey());
				  }
				  
			  }
			  
		  
		 
		
		  if(null != preauth){
			  
			 
			  
			  List<PedDetailsTableDTO> diagnosis = diagnosisService.getPedValidationList(preauth.getKey());
		  
			  for(PedDetailsTableDTO pedDetailsTableDTO : diagnosis){
			  
				  daignosisName += pedDetailsTableDTO.getDiagnosisName()+", ";
			  }
			  request.setAttribute("daignosisName", daignosisName);
			  request.setAttribute("preauth", preauth);
		  }
		  if(null != claimforIntimation){
			  
			 request.setAttribute("claim", claimforIntimation);
			  
//			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<DocAcknowledgement> docAcknowledgements= acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());
			 
			 List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();
			 
			 for(DocAcknowledgement docAcknowledgement : docAcknowledgements){
				 
				 ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();
				 
				 tableDTO.setAcknowledgmentNo(docAcknowledgement.getAcknowledgeNumber());
				 tableDTO.setDocumentReceivedFrom(docAcknowledgement.getDocumentReceivedFromId().getValue());
				 if(null != docAcknowledgement.getDocumentReceivedDate()){ 
				 tableDTO.setDocumentReceivedDate(SHAUtils.formatDate(docAcknowledgement.getDocumentReceivedDate()));
				 }
				 tableDTO.setModeOfRececipt(docAcknowledgement.getModeOfReceiptId().getValue());
				
				 
				 String billClassification = docAcknowledgement.getHospitalisationFlag() != null ? docAcknowledgement.getHospitalisationFlag().equalsIgnoreCase("y") ? "Hospitalisation, " : "" : "";
				 billClassification += docAcknowledgement.getPreHospitalisationFlag() != null ? docAcknowledgement.getPreHospitalisationFlag().equalsIgnoreCase("y") ? "Pre-Hospitalisation, ":"" :"";
				 billClassification += docAcknowledgement.getPostHospitalisationFlag() != null ? docAcknowledgement.getPostHospitalisationFlag().equalsIgnoreCase("y")  ? "Post-Hospitalisation, " : "":"";
				 billClassification += docAcknowledgement.getPartialHospitalisationFlag() != null ? docAcknowledgement.getPartialHospitalisationFlag().equalsIgnoreCase("y") ? "Partial-Hospitalisation" : "":"";
				 billClassification += docAcknowledgement.getHospitalizationRepeatFlag() != null ? docAcknowledgement.getHospitalizationRepeatFlag().equalsIgnoreCase("y") ? "Repeat-Hospitalisation" : "":"";
				 billClassification += docAcknowledgement.getPatientCareFlag() != null ? docAcknowledgement.getPatientCareFlag().equalsIgnoreCase("y") ? "Add on Benefits (Patient Care)" : "":"";
				 billClassification += docAcknowledgement.getHospitalCashFlag() != null ? docAcknowledgement.getHospitalCashFlag().equalsIgnoreCase("y") ? "Add on Benefits (Hospital cash)": "":"";
				 billClassification += docAcknowledgement.getLumpsumAmountFlag() != null ? docAcknowledgement.getLumpsumAmountFlag().equalsIgnoreCase("y") ? "Lumpsum Amount": "":"";
				 tableDTO.setBillClassification(billClassification);
				

				 tableDTO.setStatus(docAcknowledgement.getStatus().getProcessValue());
				 
				 if(null != docAcknowledgement.getRodKey()){
					 
					 Reimbursement reimbursement = reimbursementService.getReimbursementByKey(docAcknowledgement.getRodKey());
					if(null != reimbursement.getMedicalCompletedDate()){
						tableDTO.setMedicalResponseDate(SHAUtils.formatDate(reimbursement.getMedicalCompletedDate()));
					}
					 tableDTO.setReimbursementNo(reimbursement.getRodNumber());
					 if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS) || ReferenceTable.getPaymentStatus().containsKey(reimbursement.getStatus().getKey())){
						 tableDTO.setRemarks(reimbursement.getApprovalRemarks());
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_REJECT_STATUS)){
						 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getRejectionRemarks() : ""  ;
						 tableDTO.setRemarks(reimbursementRejectionRemarks);
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_QUERY_STATUS)){
						 String latestQueryRemarks = null !=  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()) ?  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()).getQueryRemarks() : "";
						 tableDTO.setRemarks(latestQueryRemarks);
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)){
						 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getRejectionRemarks() : ""  ;
						 tableDTO.setRemarks(reimbursementRejectionRemarks);
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)){
						 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getRejectionRemarks() : ""  ;
						 tableDTO.setRemarks(reimbursementRejectionRemarks);
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS)){
						 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getDisapprovedRemarks() : ""  ;
						 tableDTO.setRemarks(reimbursementRejectionRemarks);
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_DISAPPROVE_REJECT_STATUS)){
						 String  reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService.getRejection(reimbursement.getKey()).getDisapprovedRemarks() : ""  ;
						 tableDTO.setRemarks(reimbursementRejectionRemarks);
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_QUERY_APPROVE_STATUS)){
						 String latestQueryRemarks = null !=  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()) ?  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()).getQueryRemarks() : "";
						 tableDTO.setRemarks(latestQueryRemarks);
					 }else if(reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS)){
						 String latestQueryRemarks = null !=  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()) ?  reimbursementQueryService.getReimbursementyQueryByRodKey(reimbursement.getKey()).getQueryRemarks() : "";
						 tableDTO.setRemarks(latestQueryRemarks);
					 }
					 
					 
					 tableDTO.setStatus(reimbursement.getStatus().getProcessValue());
				 }
				 docAcknowledgementList.add(tableDTO);
			 }
			 
				 request.setAttribute("reimbursementList", docAcknowledgementList);
			 
			  
		  	BPMClientContext bpmClientContext = new BPMClientContext();
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
			
			 String queryString = request.getQueryString();

		  if(!error.isEmpty()){
			  request.setAttribute("error", error);
			  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
		  }else if(queryString.contains("int_number")){
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  request.getRequestDispatcher("/WEB-INF/ViewQueryDetails.jsp").include(request, response);
			  }
		  else {
			  request.setAttribute("intimation", intimationByNo);
			  request.setAttribute("hospitals", hospitals);
			  request.getRequestDispatcher("/WEB-INF/ViewRejectionDetails.jsp").include(request, response);
		  }
		 

		  }
		  }
	}
}
	  



