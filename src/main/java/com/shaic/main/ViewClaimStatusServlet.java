package com.shaic.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.v7.ui.VerticalLayout;

public class ViewClaimStatusServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4176044184398161574L;

	  @Inject
	  private ClaimService claimService;

	  @Inject
	  private IntimationService intimationService;
	  
	  @EJB
	  private HospitalService hospitalService;
	  
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
		  String intimationnumber = request.getParameter("IntimationNo");
		  String idCardNo = request.getParameter("IdCardNo");
		  doPost(request, response);
//	    request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
	  }
	  
	  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
		  String daignosisName = "";
		  String error = "";
		  String intimationNumber =  (String) request.getParameter("IntimationNo");
		  String healthCardNo = (String) request.getParameter("IdCardNo");
		  Intimation intimationByNo = null;
		  Intimation intimationByHCN = null;
		  Preauth preauth = null;
		  Hospitals hospitals = null;
//		  doPostPremeia(request, response);
		 
			  
		  
		  Claim claimforIntimation = null;
		  
		if (!healthCardNo.isEmpty() && !intimationNumber.isEmpty()) {

			Intimation intimationByNumber = intimationService
					.getIntimationByNo(intimationNumber);
			intimationByNo = intimationService.getIntimationByHealthcardNo(
					intimationNumber, healthCardNo);

			if (intimationByNumber != null && null == intimationByNo) {
				error = "The values provided ate invalid. Please close this window and try again.</br>";
			}

			if (error.isEmpty() && intimationByNo != null) {
				claimforIntimation = claimService
						.getClaimforIntimation(intimationByNo.getKey());
				hospitals = hospitalService
						.getHospitalDetailsByKey(intimationByNo.getHospital());
				if (claimforIntimation != null) {
					List<Preauth> preauthByClaimKey = preauthService.getPreauthByClaimKey(claimforIntimation
									.getKey());
					if(preauthByClaimKey != null && ! preauthByClaimKey.isEmpty()){
						preauth = preauthByClaimKey.get(preauthByClaimKey.size() -1);
					}
				}

			}

			if (intimationByNumber == null) {
				
				VerticalLayout vLayout = new VerticalLayout();
				String strDmsViewURL = BPMClientContext.PREMIA_CLAIM_VIEW_URL;
				/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
					    new ExternalResource(strDmsViewURL+strPolicyNo));*/
				
				request.setAttribute("url", strDmsViewURL+intimationNumber);
				
				request.getRequestDispatcher(
						"/WEB-INF/premiaclaimsview.jsp").include(
						request, response);
				
//				BrowserFrame browserFrame = new BrowserFrame("",
//					    new ExternalResource(strDmsViewURL+intimationNumber));
//				//browserFrame.setWidth("600px");
//				//browserFrame.setHeight("400px");
//				browserFrame.setWidth("100%");
//				browserFrame.setHeight("150%");
//				vLayout.addComponent(browserFrame);
//				
//				Button btnSubmit = new Button("OK");
//				
//				btnSubmit.setCaption("CLOSE");
//				//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
//				btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//				btnSubmit.setWidth("-1px");
//				btnSubmit.setHeight("-10px");
//				btnSubmit.setDisableOnClick(true);
//				//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
//				
//				vLayout.addComponent(btnSubmit);
//				vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
//				vLayout.setWidth("100%");
//				vLayout.setHeight("110%");
//				final Window popup = new com.vaadin.ui.Window();
//				
//				popup.setCaption("");
//				popup.setWidth("100%");
//				popup.setHeight("100%");
//				//popup.setSizeFull();
//				popup.setContent(vLayout);
//				popup.setClosable(true);
//				popup.center();
//				popup.setResizable(false);
//				
//				btnSubmit.addClickListener(new Button.ClickListener() {
//					
//					private static final long serialVersionUID = 1L;
//			
//					@Override
//					public void buttonClick(ClickEvent event) {
//							//binder.commit();
//								
//								//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
//								popup.close();
//							
//					}
//					
//				});
//
//				
//				popup.addCloseListener(new Window.CloseListener() {
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void windowClose(CloseEvent e) {
//						System.out.println("Close listener called");
//					}
//				});
//
//				popup.setModal(true);
//				UI.getCurrent().addWindow(popup);
			} else {
				if (null != preauth) {
					List<PedDetailsTableDTO> diagnosis = diagnosisService
							.getPedValidationList(preauth.getKey());

					for (PedDetailsTableDTO pedDetailsTableDTO : diagnosis) {

						daignosisName += pedDetailsTableDTO.getDiagnosisName()
								+ ", ";
					}
					request.setAttribute("daignosisName", daignosisName);
					request.setAttribute("preauth", preauth);
				}
				if (null != claimforIntimation) {

					request.setAttribute("claim", claimforIntimation);

					// List<DocAcknowledgement> docAcknowledgements=
					// acknowledgementDocumentsReceivedService.getAckDetailsForBillClassificationValidation(claimforIntimation.getKey());

					List<DocAcknowledgement> docAcknowledgements = acknowledgementDocumentsReceivedService
							.getAckDetailsForBillClassificationValidation(claimforIntimation
									.getKey());

					List<ReceiptOfDocumentAndMedicalProcess> docAcknowledgementList = new ArrayList<ReceiptOfDocumentAndMedicalProcess>();

					for (DocAcknowledgement docAcknowledgement : docAcknowledgements) {

						ReceiptOfDocumentAndMedicalProcess tableDTO = new ReceiptOfDocumentAndMedicalProcess();

						tableDTO.setAcknowledgmentNo(docAcknowledgement
								.getAcknowledgeNumber());
						tableDTO.setDocumentReceivedFrom(docAcknowledgement
								.getDocumentReceivedFromId().getValue());
						if (null != docAcknowledgement
								.getDocumentReceivedDate()) {
							tableDTO.setDocumentReceivedDate(SHAUtils
									.formatDate(docAcknowledgement
											.getDocumentReceivedDate()));
						}
						tableDTO.setModeOfRececipt(docAcknowledgement
								.getModeOfReceiptId().getValue());

						String billClassification = docAcknowledgement
								.getHospitalisationFlag().equalsIgnoreCase("y") ? "Hospitalisation, "
								: "";
						billClassification += docAcknowledgement
								.getPreHospitalisationFlag().equalsIgnoreCase(
										"y") ? "Pre-Hospitalisation, " : "";
						billClassification += docAcknowledgement
								.getPostHospitalisationFlag().equalsIgnoreCase(
										"y") ? "Post-Hospitalisation, " : "";
						billClassification += docAcknowledgement
								.getPartialHospitalisationFlag()
								.equalsIgnoreCase("y") ? "Partial-Hospitalisation"
								: "";
						billClassification += docAcknowledgement
								.getHospitalizationRepeatFlag()
								.equalsIgnoreCase("y") ? "Repeat-Hospitalisation"
								: "";
						billClassification += docAcknowledgement
								.getPatientCareFlag().equalsIgnoreCase("y") ? "Add on Benefits (Patient Care)"
								: "";
						billClassification += docAcknowledgement
								.getHospitalCashFlag().equalsIgnoreCase("y") ? "Add on Benefits (Hospital cash)"
								: "";
						billClassification += docAcknowledgement
								.getLumpsumAmountFlag().equalsIgnoreCase("y") ? "Lumpsum Amount"
								: "";
						tableDTO.setBillClassification(billClassification);

						tableDTO.setStatus(docAcknowledgement.getStatus()
								.getProcessValue());

						if (null != docAcknowledgement.getRodKey()) {

							Reimbursement reimbursement = reimbursementService
									.getReimbursementByKey(docAcknowledgement
											.getRodKey());
							if (null != reimbursement.getMedicalCompletedDate()) {
								tableDTO.setMedicalResponseDate(SHAUtils
										.formatDate(reimbursement
												.getMedicalCompletedDate()));
							}
							tableDTO.setReimbursementNo(reimbursement
									.getRodNumber());
							if (reimbursement
									.getStatus()
									.getKey()
									.equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)) {
								tableDTO.setRemarks(reimbursement
										.getApprovalRemarks());
							} else if (reimbursement
									.getStatus()
									.getKey()
									.equals(ReferenceTable.FINANCIAL_REJECT_STATUS)) {
								String reimbursementRejectionRemarks = null != acknowledgementDocumentsReceivedService
										.getRejection(reimbursement.getKey()) ? acknowledgementDocumentsReceivedService
										.getRejection(reimbursement.getKey())
										.getRejectionRemarks() : "";
								tableDTO.setRemarks(reimbursementRejectionRemarks);
							} else if (reimbursement
									.getStatus()
									.getKey()
									.equals(ReferenceTable.FINANCIAL_QUERY_STATUS)) {
								String latestQueryRemarks = null != reimbursementQueryService
										.getReimbursementyQueryByRodKey(reimbursement
												.getKey()) ? reimbursementQueryService
										.getReimbursementyQueryByRodKey(
												reimbursement.getKey())
										.getQueryRemarks() : "";
								tableDTO.setRemarks(latestQueryRemarks);
							}

							tableDTO.setStatus(reimbursement.getStatus()
									.getProcessValue());
						}
						docAcknowledgementList.add(tableDTO);
					}

					request.setAttribute("reimbursementList",
							docAcknowledgementList);

					List<Reimbursement> finacialList = new ArrayList<Reimbursement>();

					/*
					 * for(Reimbursement reimbursement : reimbursements){
					 * 
					 * ReceiptOfDocumentAndMedicalProcess tableDTO = new
					 * ReceiptOfDocumentAndMedicalProcess();
					 * 
					 * 
					 * String billClassification =
					 * docAcknowledgement.getHospitalisationFlag
					 * ().equalsIgnoreCase("y") ? "Hospitalisation, " : "";
					 * billClassification +=
					 * docAcknowledgement.getPreHospitalisationFlag
					 * ().equalsIgnoreCase("y") ? "Pre-Hospitalisation, ":"" ;
					 * billClassification +=
					 * docAcknowledgement.getPostHospitalisationFlag
					 * ().equalsIgnoreCase("y") ? "Post-Hospitalisation, " : "";
					 * billClassification +=
					 * docAcknowledgement.getPartialHospitalisationFlag
					 * ().equalsIgnoreCase("y") ? "Partial-Hospitalisation" :
					 * ""; tableDTO.setBillClassification(billClassification);
					 * 
					 * 
					 * 
					 * 
					 * 
					 * 
					 * tableDTO.setAcknowledgmentNo(reimbursement.
					 * getDocAcknowLedgement().getAcknowledgeNumber());
					 * tableDTO.
					 * setDocumentReceivedFrom(reimbursement.getDocAcknowLedgement
					 * ().getDocumentReceivedFromId().getValue());
					 * tableDTO.setDocumentReceivedDate
					 * (reimbursement.getDocAcknowLedgement
					 * ().getDocumentReceivedDate());
					 * tableDTO.setModeOfRececipt(
					 * reimbursement.getDocAcknowLedgement
					 * ().getModeOfReceiptId().getValue());
					 * tableDTO.setStatus(reimbursement
					 * .getDocAcknowLedgement().getStatus().getProcessValue());
					 * tableDTO
					 * .setDocumentReceivedDate(reimbursement.getCreatedDate());
					 * tableDTO
					 * .setReimbursementNo(reimbursement.getRodNumber());
					 * tableDTO
					 * .setRemarks(reimbursement.getFinancialApprovalRemarks());
					 * tableDTO.setAmount(reimbursement.getApprovedAmount());
					 * 
					 * reimbursementList.add(tableDTO); }
					 * 
					 * 
					 * request.setAttribute("reimbursementList",
					 * reimbursements);
					 */
					List<Reimbursement> reimbursements = reimbursementService
							.getReimbursementByClaimKey(claimforIntimation
									.getKey());
					// ${item.getFinancialApprovalRemarks()},${item.getTreatmentRemarks()},${item.getRejectionRemarks()},${item.getApprovalRemarks()},${item.getBillingRemarks()},${item.getRelapseRemarks()},${item.getMedicalRemarks()}
					int sno = 1;
					for (Reimbursement financial : reimbursements) {
						if (null != financial.getFinancialApprovedAmount()) {
							financial.setNumberOfDays(sno);// this for serial no
															// only here reuse
							// if(financial.getStatus().getKey() ==
							// ReferenceTable.FINANCIAL_APPROVE_STATUS){
							// financial.setFinancialApprovalRemarks(financial.getApprovalRemarks());
							// }else if(financial.getStatus().getKey() ==
							// ReferenceTable.FINANCIAL_QUERY_STATUS){
							// financial.setFinancialApprovalRemarks(financial.get);
							// }

							if (null != financial.getPaymentModeId()) {

								if (null != financial
										.getFinancialCompletedDate()) {
									financial
											.setOfficeCode(SHAUtils.formatDate(financial
													.getFinancialCompletedDate()));// this
																					// for
																					// date
																					// formant
								}
								financial.setPayableAt(masterService.getMaster(
										financial.getPaymentModeId())
										.getValue());
							}
							finacialList.add(financial);
						}
						sno++;
					}
					request.setAttribute("financial", finacialList);
				}

				if (!error.isEmpty()) {
					request.setAttribute("error", error);
					request.getRequestDispatcher(
							"/WEB-INF/ClaimDetailErrorPage.jsp").include(
							request, response);
				} else {
					request.setAttribute("intimation", intimationByNo);
					request.setAttribute("hospitals", hospitals);
					request.getRequestDispatcher("/WEB-INF/ViewClaimDetailPage.jsp")
							.include(request, response);
				}
			}
		} else{
			 
			  error += "The values provided ate invalid. Please close this window and try again.";
			
				 request.setAttribute("error", error);
				 request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
		}
	}
	  
	  
	  
//	  @SuppressWarnings("unused")
//	private String doPostPremeia(HttpServletRequest request, HttpServletResponse response1) throws IOException{
//		  
//		  String url = "http://starhealth.in/claimstatus.php";
//			URL obj = new URL(url);
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//			String intimationNumber = (String) request.getParameter("intimationNumber");
//			  String healthCardNo = (String) request.getParameter("idCardNumber");
//			//add reuqest header
//			con.setRequestMethod("POST");
//			con.setRequestProperty("User-Agent", USER_AGENT);
//			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//			con.setRequestProperty("intimationnumber", intimationNumber);
//			con.setRequestProperty("idcardnumber", healthCardNo);
//			
//			String urlParameters = "intimationnumber=C02G8416DRJM&idcardnumber=gfhty546";
//
//			// Send post request
//			con.setDoOutput(true);
//			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			wr.writeBytes(urlParameters);
//			wr.flush();
//			wr.close();
//
//			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'POST' request to URL : " + url);
//			System.out.println("Post parameters : " + urlParameters);
//			System.out.println("Response Code : " + responseCode);
//
//			BufferedReader in = new BufferedReader(
//			        new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//
//			//print result
//			System.out.println(response.toString());
//			 PrintWriter out = response1.getWriter();
//		  	  out.println (response.toString());  
//
//		
//		  
//		return null;
//		  
//	  }
	  }