/**
 * 
 */
package com.shaic.main;



/**
 * 
 */




import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;

import com.shaic.arch.PDFMergerUtil;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.outpatient.registerclaim.dto.DocumentDetailsDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.policy.search.ui.opsearch.ProcessOPRequestService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Hospitals;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.OPClaim;
import com.shaic.domain.PreauthService;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OutpatientService;
import com.vaadin.ui.Embedded;

public class ClaimsDMSServlet extends HttpServlet {
	


	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private OMPClaimService ompClaimService;
	
	@EJB
	private ProcessOPRequestService opService;
	
	@EJB
	private OMPProcessRODBillEntryService ompProcessRODBillEntryService;
	
	@EJB
	private OutpatientService outpatientService;
	
	@EJB
	private PreauthService preauthService;
  
  private final String USER_AGENT = "Mozilla/5.0";
  
  
  /*private List<String> fileNameList = null;
  
  private List<String> filePathList = null;*/
  
  private List<File> fileList = null;
  
  private Path tempDir = null;
  
  private Path imageTempDir = null;
  
  
  
 
  //final Embedded imageViewer = new Embedded();
  private final Embedded e = new Embedded();
  

  protected void doGet(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException 
  {
	  String queryString = request.getQueryString();
		if(queryString != null) {
			String[] split = queryString.split("=");
			String dmsDocumentToken = split[1];
			JSONObject jSONObject = null;
			jSONObject = getToken(split[1]);
			dmsDocumentToken = (String)jSONObject.get("intimationNo");

			String lumenAvailability = "";
			long lumenRequestKey = 0;
			/*Below Code commented for security reason
			String[] queryStringArr = dmsDocumentToken.split("&", -1);
			if(queryStringArr.length > 1) {
				dmsDocumentToken = queryStringArr[0];
				String[] lumenQueryStringArr = queryStringArr[1].split("-", -1);
				if(lumenQueryStringArr.length > 1) {
					lumenAvailability = lumenQueryStringArr[0];
					lumenRequestKey = Long.parseLong(lumenQueryStringArr[1]);
				}
			}*/
			if(jSONObject != null && jSONObject.get("lumenKey") != null){
				String lumenKey = (String) jSONObject.get("lumenKey");
				lumenAvailability = "lumen";
				lumenRequestKey = Long.parseLong(lumenKey);
			}
			
			/*Below Code Commented for Security reason
			if(request.getQueryString() != null && request.getQueryString().contains("dummystrin") ) {
				String[] viewcahsless = queryString.split("&&");*/
			if(jSONObject != null && jSONObject.get("cashlessDoc") != null){
				/**
				* Release Number : R3
				* Requirement Number:R0725
				* Modified By : Durga Rao
				* Modified On : 15th May 2017
				* Referred By  : Narasimha
				*/
				/*String token= viewcahsless[0];
				String[] view = token.split("=");
				String token2= view[1];*/
				String token2= (String)jSONObject.get("intimationNo");
				viewCashlessDocument(token2,request, response);
			} 
			/*Below code commented for security reason*/
			/*else if(request.getQueryString() != null && request.getQueryString().contains("ompdoc") ) {
					String[] viewcahsless = queryString.split("&&");*/
			if(jSONObject != null && jSONObject.get("ompdoc") != null){
					/**
					* Release Number : R3
					* Requirement Number:R0725
					* Modified By : Durga Rao
					* Modified On : 15th May 2017
					* Referred By  : Narasimha
					*/
					/*String token= viewcahsless[0];
					String[] view = token.split("=");*/
//					String token2= view[1];
					String token2= (String)jSONObject.get("intimationNo");
					showOmpPage(token2,request, response);
				}else {
				showPage(dmsDocumentToken, lumenAvailability, lumenRequestKey, request, response);
			}
		}
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	  String intimationNo = (String)request.getSession().getAttribute("intimationNo");
	 /* String intimationNo =  (String) request.getParameter("intimation_number");
	//  String healthCardNo = (String) request.getParameter("idCardNumber");
	  	showPage(intimationNo, request, response);*/
	  if(null != request && null != request.getAttribute("mergeDocumentsUrl"))
	  {
		  request.removeAttribute("mergeDocumentsUrl");  
	  }
	  if(null != request && null != request.getAttribute("mergedfileName"))
	  {
		  request.removeAttribute("mergedfileName");  
	  }
	  
	  if(null != request && null !=  request.getSession().getAttribute("mergeDocumentsUrl"))
	  {
		  request.getSession().removeAttribute("mergeDocumentsUrl");
	  }
	  if(null != request && null != request.getAttribute("mergedfileName"))
	  {
		  request.getSession().removeAttribute("mergedfileName");
	  }
	  
	  //String timeStamp = mergeDocuments(fileNameList);
	  
	 // File tmpFile = mergeDocuments(fileNameList);
	  
	 
	List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist = new ArrayList<DMSDocumentDetailsDTO>();
	DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
	dmsDTO.setIntimationNo(intimationNo);
	Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
	List<DMSDocumentDetailsDTO> dtoList =  null;
	if(claim != null ) {
		dmsDTO.setClaimNo(claim.getClaimId());
		dtoList =  billDetailsService.getAcknowledgeDocumentList(claim);
	}
	dmsDocumentDetailsDTOlist = billDetailsService
			.getDocumentDetailsData(intimationNo, 0);
	if(null != dtoList && !dtoList.isEmpty())
	{
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dtoList) {
			dmsDocumentDetailsDTOlist.add(dmsDocumentDetailsDTO);
		}
	}
	
	if (null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty()) {
		dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTOlist);
	} 
	  clearSessionandRequest(request,"mergeDocuments");
	  request = intializeAndMergeFileListBasedOnDocType(dmsDocumentDetailsDTOlist, request,intimationNo); 
	/*  String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" + File.separator + "mergeddocuments" + File.separator +
				 "CASHLESS_DOCUMENT"+"_"+timeStamp+ ".pdf";
	  String fileName = "CASHLESS_DOCUMENT"+"_"+timeStamp+ ".pdf";*/
	  if(null != request)
	  {
		request.getRequestDispatcher("/WEB-INF/ViewCashlessDocument.jsp").forward(request, response);
	  }

	
  }

private void showPage(String intimationNo, String lumenAvailability, long lumenRequestKey, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	
	
		
	  List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist = new ArrayList<DMSDocumentDetailsDTO>();
	
	
	DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
	dmsDTO.setIntimationNo(intimationNo);
	Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
	OPClaim opclaim = claimService.getOPClaimByIntimationNo(intimationNo);
	List<DMSDocumentDetailsDTO> dtoList =  null;
	DocumentDetails docDescDetail;
	String hospitalCode = "";
	if(claim != null ) {
	if(claim != null ) {
		dmsDTO.setClaimNo(claim.getClaimId());
		dtoList =  billDetailsService.getAcknowledgeDocumentList(claim);

	}
	dmsDocumentDetailsDTOlist = billDetailsService
	
			.getDocumentDetailsData(intimationNo, lumenRequestKey);
	
	if(claim != null){
		DocumentSearchApiDto docSearch = new DocumentSearchApiDto();
		if(claim.getIntimation() != null && claim.getIntimation().getHospital() != null) {
		Hospitals hospCode = claimService.getHospitalDetailsByKey(claim.getIntimation().getHospital());
		docSearch.setHospCode(hospCode.getHospitalCode());
		List<DMSDocumentDetailsDTO> doctDetails = PremiaService.getInstance().getDocumentDetails(docSearch.getHospCode());
		if(doctDetails != null) {
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : doctDetails) {
			dmsDocumentDetailsDTO.setHospitalCode(docSearch.getHospCode());
			dmsDocumentDetailsDTOlist.add(dmsDocumentDetailsDTO);
		} }
		}
		
	}
	
	if(claim != null && claim.getIntimation() != null && claim.getIntimation().getHospital() != null) {

		DocumentSearchApiDto docSearch = new DocumentSearchApiDto();
		Hospitals hospCode = claimService.getHospitalDetailsByKey(claim.getIntimation().getHospital());
		docSearch.setHospCode(hospCode.getHospitalCode());
		hospitalCode = hospCode.getHospitalCode();
		DMSDocumentDetailsDTO hospitalDiscount = PremiaService.getInstance().getHospitalDiscounttUrl(docSearch.getHospCode());
		if(hospitalDiscount!=null){
			hospitalDiscount.setFileName("Discount");
		}
		//DMSDocumentDetailsDTO hospitalTariff = PremiaService.getInstance().getHospitalTariffUrl(docSearch.getHospCode());
		//if(hospitalTariff!=null){
		//hospitalTariff.setFileName("Room Tariff");
		//}
		dmsDocumentDetailsDTOlist.add(hospitalDiscount);
		//dmsDocumentDetailsDTOlist.add(hospitalTariff);
	}

	if(null != dtoList && !dtoList.isEmpty())
	{
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dtoList) {
			dmsDocumentDetailsDTOlist.add(dmsDocumentDetailsDTO);
		}
	}
	
	if (null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty()) {
		
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
			if (dmsDocumentDetailsDTO.getDmsDocToken() != null) {
				List<RODDocumentSummary> documentSummaryList = billDetailsService
						.getRODDocumentSummaryByDocToken(dmsDocumentDetailsDTO
								.getDmsDocToken());
				if (documentSummaryList != null && !documentSummaryList.isEmpty()) {
					String remark = "";
					for (RODDocumentSummary rodDocumentSummary : documentSummaryList) {
						if (rodDocumentSummary.getCorporateRemarks() != null) {
								remark += rodDocumentSummary
										.getCorporateRemarks() + " ";
							}
					}
					dmsDocumentDetailsDTO.setDeductionRemarks(remark);
				}
			}
		}
		dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTOlist);
	}
	
	
	
	 //Path tempDir = createTempDirectory(intimationNo);
	
	/*request.setAttribute("intimationNo", intimationNo);
	request.setAttribute("preauthFileList", preauthFileList);
	request.setAttribute("enhancementFileList", enhancementFileList);
	request.setAttribute("queryReportFileList", queryReportFileList);
	request.setAttribute("fvrFileList", fvrFileList);
	request.setAttribute("othersFileList", othersFileList);
	request.setAttribute("rodFileList", rodFileList);
	request.setAttribute("rodNoList", rodNoList);
	request.setAttribute("rodNoFormatList", rodNoFormatList);*/
	/*request.setAttribute("preauthMap", preauthFileMap);
	request.setAttribute("enhancementMap", enhancementFileMap);
	request.setAttribute("fvrMap", fvrFileMap);
	request.setAttribute("queryReportMap", queryReportFileMap);
	request.setAttribute("othersFileMap", othersFileMap);
	request.setAttribute("rodFileMap", rodFileMap);*/
	
	
	/*if(null != preauthFileList && preauthFileList.isEmpty() &&  null != enhancementFileList && enhancementFileList.isEmpty() &&
			null != queryReportFileList && queryReportFileList.isEmpty() && null != fvrFileList && fvrFileList.isEmpty()
			 && null != othersFileList && othersFileList.isEmpty() && null != rodFileList && rodFileList.isEmpty() 
			 && null != rodNoList && rodNoList.isEmpty()  && null != rodNoFormatList && rodNoFormatList.isEmpty()  
			)*/
	if(null != dmsDocumentDetailsDTOlist && dmsDocumentDetailsDTOlist.isEmpty() &&  claim == null)
	{/*
	//	 request.setAttribute("error", error);
		  showOmpPage(intimationNo, request, response);	
		  //request.getRequestDispatcher("/WEB-INF/ClaimsDMSErrorPage.jsp").include(request, response);
	*/}
	else 
	{
		clearSessionandRequest(request,"claimsdms");
		request = intializeFileListBasedOnDocType(dmsDocumentDetailsDTOlist, request,intimationNo, lumenAvailability);
		request.setAttribute("intimationNo", intimationNo);
		request.setAttribute("hospitalCode", hospitalCode);
		
		//fix for IE
				String userAgent = request.getHeader("User-Agent");
			  	System.out.println("userAgent"+ userAgent);
				if(userAgent.indexOf("MSIE") > -1){
					response.setHeader("X-UA-Compatible", "IE=edge,IE=8");
				}
	
		request.getRequestDispatcher("/WEB-INF/ClaimsDMSView.jsp").include(request, response);
	}
	}
	else if(opclaim != null){
		showOPDocPage(intimationNo, request, response);
	}
	else if(null != dmsDocumentDetailsDTOlist && dmsDocumentDetailsDTOlist.isEmpty() && claim == null)
	{/*
	//	 request.setAttribute("error", error);
		  showOmpPage(intimationNo, request, response);	
		  //request.getRequestDispatcher("/WEB-INF/ClaimsDMSErrorPage.jsp").include(request, response);
	*/}
	
	else {
		request.getRequestDispatcher("/WEB-INF/NoClaimErrorPage.jsp").include(request, response);
	}
}

private void clearSessionandRequest(HttpServletRequest request,String methodName) {
	
	// clearing the session.....
	if(request != null &&  request.getSession() != null) {
		if(null != methodName && ("claimsdms").equalsIgnoreCase(methodName))
		{
			if(null != request.getAttribute("intimationNo"))
			{
				request.removeAttribute("intimationNo");
			}

			if(null != request.getAttribute("preauthFileList"))
			{
				request.removeAttribute("preauthFileList");
			}
			if(null != request.getAttribute("enhancementFileList"))
			{
				request.removeAttribute("enhancementFileList");
			}
			if(null != request.getAttribute("queryReportFileList"))
			{
				request.removeAttribute("queryReportFileList");
			}
			if(null != request.getAttribute("fvrFileList"))
			{
				request.removeAttribute("fvrFileList");
			}
			if(null != request.getAttribute("othersFileList"))
			{
				request.removeAttribute("othersFileList");
			}
			if(null != request.getAttribute("rodFileList"))
			{
				request.removeAttribute("rodFileList");
			}
			if(null != request.getAttribute("rodNoList"))
			{
				request.removeAttribute("rodNoList");
			}

			if(null != request.getAttribute("paymentRodNoList"))
			{
				request.removeAttribute("paymentRodNoList");
			}

			if(null != request.getAttribute("paymentRodNoFormatList"))
			{
				request.removeAttribute("paymentRodNoFormatList");
			}
			if(null != request.getAttribute("rodNoFormatList"))
			{
				request.removeAttribute("rodNoFormatList");
			}

			if(null != request.getAttribute("ackDocFileList"))
			{
				request.removeAttribute("ackDocFileList");
			}
			if(null != request.getAttribute("grievanceList"))
			{
				request.removeAttribute("grievanceList");
			}
			if(null != request.getAttribute("mergedfileName"))
			{
				request.removeAttribute("mergedfileName");
			}
			if(null != request.getAttribute("mergeDocumentsUrl"))
			{
				request.removeAttribute("mergeDocumentsUrl");
			}
			if(null != request.getAttribute("xRayReportList"))
			{
				request.removeAttribute("xRayReportList");
			}
			if(null != request.getAttribute("apiList"))
			{
				request.removeAttribute("apiList");
			}
			if(null != request.getAttribute("hospitalDiscountList"))
			{
				request.removeAttribute("hospitalDiscountList");
			}
			if(null != request.getAttribute("lumenDocFileList"))
			{
				request.removeAttribute("lumenDocFileList");
			}

			if(null != request.getAttribute("preauthURLlist"))
			{
				request.removeAttribute("preauthURLlist");
			}
			if(null != request.getAttribute("enhancementURLList"))
			{
				request.removeAttribute("enhancementURLList");
			}
			if(null != request.getAttribute("queryURLList"))
			{
				request.removeAttribute("queryURLList");
			}
			if(null != request.getAttribute("fvrURLList"))
			{
				request.removeAttribute("fvrURLList");
			}
			if(null != request.getAttribute("othersURLList"))
			{
				request.removeAttribute("othersURLList");
			}
			if(null != request.getAttribute("rodURLList"))
			{
				request.removeAttribute("rodURLList");
			}
			if(null != request.getAttribute("ackDocURLList"))
			{
				request.removeAttribute("ackDocURLList");
			}
			if(null != request.getAttribute("apiList"))
			{
				request.removeAttribute("apiList");
			}
			if(null != request.getAttribute("hospitalDiscountList"))
			{
				request.removeAttribute("hospitalDiscountList");
			}
			if(null != request.getAttribute("claimVerificationReportDocFileList")) {
				request.removeAttribute("claimVerificationReportDocFileList");
			}
			if(null != request.getAttribute("claimVerificationReportDocURLList")) {
				request.removeAttribute("claimVerificationReportDocURLList");
			}	
			if(null != request.getAttribute("lumenDocURLList"))	{
				request.removeAttribute("lumenDocURLList");
			}
			if(null != request.getAttribute("ompPaymentReportDocFileList"))	{
				request.removeAttribute("ompPaymentReportDocFileList");
			}
			if(null != request.getAttribute("pccDMSDocFileList")) {
				request.removeAttribute("pccDMSDocFileList");
			}
			if(null != request.getAttribute("PccDocURLList")) {
				request.removeAttribute("PccDocURLList");
			}
			if(null != request.getAttribute("ompAckFileList")) {
				request.removeAttribute("ompAckFileList");
			}
			if(null != request.getAttribute("ompackDocURLList")) {
				request.removeAttribute("ompackDocURLList");
			}
		}
		else if(null != methodName && ("mergeDocuments").equalsIgnoreCase(methodName))
		{
			if(null != request && null != request.getAttribute("mergeDocumentsUrl"))
			{
				request.removeAttribute("mergeDocumentsUrl");  
			}
			if(null != request && null != request.getAttribute("mergedfileName"))
			{
				request.removeAttribute("mergedfileName");  
			}
		}
	}
}

private HttpServletRequest intializeFileListBasedOnDocType( List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist, HttpServletRequest request, String intimationNo, String lumenAvailability)
{
	  List<DMSDocumentDetailsDTO> preauthFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> preauthURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> enhancementFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> enhancementURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> fvrFileList =  new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> fvrURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> othersFileList =  new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> othersURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> queryReportFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> queryURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> rodFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> rodURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> ackDocFileList = new ArrayList<DMSDocumentDetailsDTO>();

	  List<DMSDocumentDetailsDTO> grievanceList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> grievanceURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> xRayReportList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> ackDocURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> apiList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> apiUrlList = new ArrayList<String>();

	  List<DMSDocumentDetailsDTO> hospitalDiscountList = new ArrayList<DMSDocumentDetailsDTO>();
	  
	  List<String> rodNoList = new ArrayList<String>();
	  
	  List<String> docApiList = new ArrayList<String>();

	  List<DMSDocumentDetailsDTO> claimVerificationReportDocFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> claimVerificationReportDocURLList = new ArrayList<String>();
	  List<DMSDocumentDetailsDTO> lumenDocFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> lumenDocURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> referDocsFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> referDocsURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> pccDMSDocFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> PccDocURLList = new ArrayList<String>();
	  

	  
	  tempDir = createTempDirectory(intimationNo);
	  
	//  imageTempDir = createTempDirectoryForImage();
	
	  
	  if(null == fileList)
	  {
		  fileList = new ArrayList<File>();
	  }
	  else
	  {
		  fileList.clear();
	  }
	 

	if(null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty())
	{
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
			
			
			
			if( SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) 
					|| dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) 
					)
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imgUrl = "";
				// String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
			
				// dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				// dmsDocumentDetailsDTO.setFileViewURL(imgUrl);
				// System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 preauthFileList.add(dmsDocumentDetailsDTO);
				 preauthURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
				 
			}
			
			
			else if(null != dmsDocumentDetailsDTO.getFileName() && dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) && !(dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)))
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				/*String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 preauthFileList.add(dmsDocumentDetailsDTO);
				// preauthURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				 preauthURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());


				 
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())
					 || (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				/*//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
			
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 enhancementURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				 
			}
			/*
			 * The below else if condition is a duplicate one only. But supposing if any documents are missed
			 * out in above else if , the below else if will take care of adding in enhancement list.
			 * That's why had retained the below condition 
			 * 
			 * **/
			else if(null != dmsDocumentDetailsDTO.getFileName() && (dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT) || (dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE))))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
			/*	String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of enhancement---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 enhancementURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_REIMBURSEMENT_QUERY_REPORT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				
				
				/* String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 
				 
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 queryReportFileList.add(dmsDocumentDetailsDTO);
				 queryURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_FVR.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				//this.fvrFileList.add(dmsDocumentDetailsDTO.getFileName());
				
					/*String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 
				
				
				//String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of fvr---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/

				fvrFileList.add(dmsDocumentDetailsDTO);
				fvrURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && !((SHAConstants.ROD_DATA_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource())))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				/*String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 othersFileList.add(dmsDocumentDetailsDTO);
				 othersURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
			}
			else if((SHAConstants.SEARCH_UPLOAD_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource()))) 
					&& null != dmsDocumentDetailsDTO.getFileType() && !SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getFileType()))
			{
				/*//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 ackDocFileList.add(dmsDocumentDetailsDTO);
				 ackDocURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());

				
			}
			else if(SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getDocumentType()) 
					|| ((SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getFileType()))
							&& ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource()))))
			{
				/*//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 grievanceList.add(dmsDocumentDetailsDTO);
				 grievanceURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());

				
			}
			else if(SHAConstants.X_RAY_REPORT_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
					 //|| (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				/* String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of xray report---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 xRayReportList.add(dmsDocumentDetailsDTO);
				 
			}
			
			else if(SHAConstants.HOSPITAL_DISCOUNT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) {
				hospitalDiscountList.add(dmsDocumentDetailsDTO);
				System.out.println("Discounturl--"+dmsDocumentDetailsDTO.getHosiptalDiscount());
			}
			else if(SHAConstants.HOSPITAL_TARIFF.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) {
				hospitalDiscountList.add(dmsDocumentDetailsDTO);
				System.out.println("RoomTariffurl--"+dmsDocumentDetailsDTO.getHosiptalDiscount());
			}
			else if( (SHAConstants.CLAIM_VERIFICATION_REPORT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) || 
					(SHAConstants.DRAFT_INVESTIGATION_LETTER.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) || 
					(SHAConstants.DOC_TYPE_ASSIGN_INVESTIGATION_LETTER.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) ||
					(SHAConstants.DOC_TYPE_RE_ASSIGN_INVESTIGATION_LETTER.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))) {
				claimVerificationReportDocFileList.add(dmsDocumentDetailsDTO);
				claimVerificationReportDocURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
			}
			else if( (SHAConstants.DMS_LUMEN_DISPLAY_NAME.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) && ("lumen".equalsIgnoreCase(lumenAvailability)) ) {
				lumenDocFileList.add(dmsDocumentDetailsDTO);
				lumenDocURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
			}
			else if(SHAConstants.HOSPITAL_DOC.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) {
				apiList.add(dmsDocumentDetailsDTO);
				apiUrlList.add(dmsDocumentDetailsDTO.getDmsDocToken());
			}
			else if( SHAConstants.REFERDOCS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())){
				referDocsFileList.add(dmsDocumentDetailsDTO);
				referDocsURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());	 
			}
			
			else if( SHAConstants.POST_CASHLESS_CELL.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())){
				pccDMSDocFileList.add(dmsDocumentDetailsDTO);
				PccDocURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());	 
			}
			else 
			{
				//this.rodFileList.add(dmsDocumentDetailsDTO.getFileName());
			/*	String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of rod---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				
				if(null != dmsDocumentDetailsDTO.getReimbursementNumber())
				{
					/*Reimbursement reimbursement = billDetailsService.getReimbursementObject(rodNoList.get(i));
					if(null != reimbursement)
					{
						String rodNoFormat = "ROD_"+i+formatDate(reimbursement.getCreatedDate());
					}
					*/
					if(null != rodNoList && !rodNoList.contains(dmsDocumentDetailsDTO.getReimbursementNumber()))
					{
						rodNoList.add(dmsDocumentDetailsDTO.getReimbursementNumber());
					}
					rodFileList.add(dmsDocumentDetailsDTO);
					rodURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				}
				else
				{
					othersFileList.add(dmsDocumentDetailsDTO);
					othersURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				}
				
			} 
		
			/*else //if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				this.othersFileList.add(dmsDocumentDetailsDTO);
				
			}*/
		}
		
		
	}
	
	
	
	List<String> rodFormatList = getRodFormatList(rodNoList, rodFileList);
	
	request.setAttribute("preauthFileList", preauthFileList);
	request.setAttribute("enhancementFileList", enhancementFileList);
	request.setAttribute("queryReportFileList", queryReportFileList);
	request.setAttribute("fvrFileList", fvrFileList);
	request.setAttribute("othersFileList", othersFileList);
	request.setAttribute("rodFileList", rodFileList);
	request.setAttribute("rodNoList", rodNoList);
	request.setAttribute("rodNoFormatList", rodFormatList);
	request.setAttribute("ackDocFileList", ackDocFileList);
	request.setAttribute("grievanceList", grievanceList);
	request.setAttribute("xRayReportList",xRayReportList);
	request.setAttribute("apiList",apiList);
	request.setAttribute("hospitalDiscountList", hospitalDiscountList);
	request.setAttribute("claimVerificationReportDocFileList", claimVerificationReportDocFileList);	
	request.setAttribute("lumenDocFileList", lumenDocFileList);
	request.setAttribute("preauthURLlist", preauthURLList);
	request.setAttribute("enhancementURLList", enhancementURLList);
	request.setAttribute("queryURLList", queryURLList);
	request.setAttribute("fvrURLList", fvrURLList);
	request.setAttribute("othersURLList", othersURLList);
	request.setAttribute("rodURLList", rodURLList);
	request.setAttribute("ackDocURLList", ackDocURLList);
	request.setAttribute("apiURLList", apiUrlList);
	request.setAttribute("claimVerificationReportDocURLList", claimVerificationReportDocURLList);
	request.setAttribute("referDocsList",referDocsFileList);
	request.setAttribute("referDocsURLList", referDocsURLList);
	request.setAttribute("claimVerificationReportDocURLList", claimVerificationReportDocURLList);
	request.setAttribute("pccDMSDocFileList", pccDMSDocFileList);
	request.setAttribute("PccDocURLList", PccDocURLList);
	
	
	
	/*String timestamp = mergeDocuments(fileNameList);
	  String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" + File.separator + "mergeddocuments" + File.separator +
				 "CASHLESS_DOCUMENT"+"_"+timestamp+ ".pdf";
	  if(null != request)
	  {
		 request.setAttribute("mergeDocumentsUrl", filePath);
	  }*/
	return request;
}

private HttpServletRequest intializeOMPFileListBasedOnDocType( List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist, HttpServletRequest request,String intimationNo)
{
	
	  List<DMSDocumentDetailsDTO> preauthFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> preauthURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> enhancementFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> enhancementURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> fvrFileList =  new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> fvrURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> othersFileList =  new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> othersURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> queryReportFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> queryURLList = new ArrayList<String>();
		
	  List<DMSDocumentDetailsDTO> rodFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> rodURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> ackDocFileList = new ArrayList<DMSDocumentDetailsDTO>();

	  List<DMSDocumentDetailsDTO> grievanceList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> grievanceURLList = new ArrayList<String>();
	  
	  List<DMSDocumentDetailsDTO> xRayReportList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> ackDocURLList = new ArrayList<String>();
	  
//CR2019034
	  
	  List<DMSDocumentDetailsDTO> ompPaymentReportDocFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  
	  List<DMSDocumentDetailsDTO> ompAckFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> ompackDocURLList = new ArrayList<String>();

	  
	  List<String> rodNoList = new ArrayList<String>();
	  
	  List<String> paymentRodNoList = new ArrayList<String>();

	  
	  tempDir = createTempDirectory(intimationNo);
	  
	//  imageTempDir = createTempDirectoryForImage();
	
	  
	  if(null == fileList)
	  {
		  fileList = new ArrayList<File>();
	  }
	  else
	  {
		  fileList.clear();
	  }
	 

	if(null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty())
	{
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
			
			if( SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) 
					|| dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) 
					)
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imgUrl = "";
				// String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
			
				// dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				// dmsDocumentDetailsDTO.setFileViewURL(imgUrl);
				// System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 preauthFileList.add(dmsDocumentDetailsDTO);
				 preauthURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
				 
			}
			
			
			else if(null != dmsDocumentDetailsDTO.getFileName() && dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) && !(dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)))
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				/*String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 preauthFileList.add(dmsDocumentDetailsDTO);
				// preauthURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				 preauthURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());


				 
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())
					 || (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				/*//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
			
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 enhancementURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				 
			}
			/*
			 * The below else if condition is a duplicate one only. But supposing if any documents are missed
			 * out in above else if , the below else if will take care of adding in enhancement list.
			 * That's why had retained the below condition 
			 * 
			 * **/
			else if(null != dmsDocumentDetailsDTO.getFileName() && (dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT) || (dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE))))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
			/*	String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of enhancement---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 enhancementURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_REIMBURSEMENT_QUERY_REPORT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				
				
				/* String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 
				 
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 queryReportFileList.add(dmsDocumentDetailsDTO);
				 queryURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_FVR.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				//this.fvrFileList.add(dmsDocumentDetailsDTO.getFileName());
				
					/*String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 
				
				
				//String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of fvr---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/

				fvrFileList.add(dmsDocumentDetailsDTO);
				fvrURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && !((SHAConstants.ROD_DATA_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource())))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				/*String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 othersFileList.add(dmsDocumentDetailsDTO);
				 othersURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				
			}
			else if((SHAConstants.SEARCH_UPLOAD_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource()))) 
					&& null != dmsDocumentDetailsDTO.getFileType() && !SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getFileType()))
			{
				/*//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				ackDocFileList.add(dmsDocumentDetailsDTO);
				ackDocURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());

				
			}
			else if(SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getDocumentType()) 
					|| ((SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getFileType()))
							&& ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource()))))
			{
				/*//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 grievanceList.add(dmsDocumentDetailsDTO);
				 grievanceURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());

				
			}
			else if(SHAConstants.X_RAY_REPORT_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
					 //|| (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				/* String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of xray report---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 xRayReportList.add(dmsDocumentDetailsDTO);
				 
			}else if(SHAConstants.OMP_PAYMENT_LETTER.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())) {
				/*ompPaymentReportDocFileList.add(dmsDocumentDetailsDTO);
				for(DMSDocumentDetailsDTO payment : ompPaymentReportDocFileList){
					System.out.println("Payment RodList : " + payment.getRodNoFormat());
				}*/
				
				if(null != dmsDocumentDetailsDTO.getReimbursementNumber())
				{
					
					if(null != paymentRodNoList && !paymentRodNoList.contains(dmsDocumentDetailsDTO.getReimbursementNumber()))
					{
						paymentRodNoList.add(dmsDocumentDetailsDTO.getReimbursementNumber());
					}
					ompPaymentReportDocFileList.add(dmsDocumentDetailsDTO);
				}
				
			}else if(dmsDocumentDetailsDTO.getDocumentSource()!=null && SHAConstants.OMP_POST_PROCESS_ACK.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource())){
				
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 ackDocFileList.add(dmsDocumentDetailsDTO);
				 ackDocURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
			}
			else 
			{
				//this.rodFileList.add(dmsDocumentDetailsDTO.getFileName());
			/*	String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of rod---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				if(null != dmsDocumentDetailsDTO.getReimbursementNumber())
				{
					/*Reimbursement reimbursement = billDetailsService.getReimbursementObject(rodNoList.get(i));
					if(null != reimbursement)
					{
						String rodNoFormat = "ROD_"+i+formatDate(reimbursement.getCreatedDate());
					}
					*/
					if(null != rodNoList && !rodNoList.contains(dmsDocumentDetailsDTO.getReimbursementNumber()))
					{
						rodNoList.add(dmsDocumentDetailsDTO.getReimbursementNumber());
					}
					rodFileList.add(dmsDocumentDetailsDTO);
					rodURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				}
				else
				{
					othersFileList.add(dmsDocumentDetailsDTO);
					othersURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());
				}
				
			}
			/*else //if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				this.othersFileList.add(dmsDocumentDetailsDTO);
				
			}*/
		}
		
		
	}
	List<String> rodFormatList = getOMPRodFormatList(rodNoList, rodFileList);
	List<String> paymentRodNoFormatList = getOMPRodFormatList(paymentRodNoList, ompPaymentReportDocFileList);

	
	request.setAttribute("preauthFileList", preauthFileList);
	request.setAttribute("enhancementFileList", enhancementFileList);
	request.setAttribute("queryReportFileList", queryReportFileList);
	request.setAttribute("fvrFileList", fvrFileList);
	request.setAttribute("othersFileList", othersFileList);
	request.setAttribute("rodFileList", rodFileList);
	request.setAttribute("rodNoList", rodNoList);
	request.setAttribute("rodNoFormatList", rodFormatList);
	request.setAttribute("ackDocFileList", ackDocFileList);
	request.setAttribute("grievanceList", grievanceList);
	request.setAttribute("xRayReportList",xRayReportList);
	

	request.setAttribute("preauthURLlist", preauthURLList);
	request.setAttribute("enhancementURLList", enhancementURLList);
	request.setAttribute("queryURLList", queryURLList);
	request.setAttribute("fvrURLList", fvrURLList);
	request.setAttribute("othersURLList", othersURLList);
	request.setAttribute("rodURLList", rodURLList);
	request.setAttribute("ackDocURLList", ackDocURLList);
	request.setAttribute("ompPaymentReportDocFileList", ompPaymentReportDocFileList);
	request.setAttribute("paymentRodNoFormatList", paymentRodNoFormatList);
	request.setAttribute("paymentRodNoList", paymentRodNoList);
	
//	request.setAttribute("opAckFileList", ompAckFileList);
	request.setAttribute("ompackDocURLList", ompackDocURLList);



	/*String timestamp = mergeDocuments(fileNameList);
	  String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" + File.separator + "mergeddocuments" + File.separator +
				 "CASHLESS_DOCUMENT"+"_"+timestamp+ ".pdf";
	  if(null != request)
	  {
		 request.setAttribute("mergeDocumentsUrl", filePath);
	  }*/
	return request;
}

private HttpServletRequest intializeAndMergeFileListBasedOnDocType( List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist, HttpServletRequest request,String intimationNo)
{
	
	  List<DMSDocumentDetailsDTO> preauthFileList = new ArrayList<DMSDocumentDetailsDTO>();
		
	  List<DMSDocumentDetailsDTO> enhancementFileList = new ArrayList<DMSDocumentDetailsDTO>();
		
	  List<DMSDocumentDetailsDTO> fvrFileList =  new ArrayList<DMSDocumentDetailsDTO>();
		
	  List<DMSDocumentDetailsDTO> othersFileList =  new ArrayList<DMSDocumentDetailsDTO>();
	  
	  List<DMSDocumentDetailsDTO> queryReportFileList = new ArrayList<DMSDocumentDetailsDTO>();
		
	  List<DMSDocumentDetailsDTO> rodFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  
	  List<DMSDocumentDetailsDTO> ackDocFileList = new ArrayList<DMSDocumentDetailsDTO>();

	  List<DMSDocumentDetailsDTO> grievanceList = new ArrayList<DMSDocumentDetailsDTO>();
	  
	  List<DMSDocumentDetailsDTO> pccDMSDocFileList = new ArrayList<DMSDocumentDetailsDTO>();
	  
	  List<String> grievanceURLList = new ArrayList<String>();	  	  
	  
	  List<File> fileList = new ArrayList<File>();

	  
	  List<String> rodNoList = new ArrayList<String>();
	  
	  List<String> paymentRodNoList = new ArrayList<String>();

	  
	  
	  Path tempDir  = createTempDirectory(intimationNo);	
	 

	if(null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty())
	{
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
			
			if( SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) 
					|| dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) 
					)
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				 if(null != imageUrl)
				 {
					 imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
						/*SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl);
						 fileNameList.add(dmsDocumentDetailsDTO.getFileName());*/
					 
					 String sfFileName = dmsDocumentDetailsDTO.getFileName();
					 	if(!dmsDocumentDetailsDTO.getFileName().isEmpty() && dmsDocumentDetailsDTO.getFileName().contains("/")){
					 		sfFileName = sfFileName.replaceAll("/", "");
					 	}
					 
					 	File tmpFile = SHAFileUtils.downloadFileForCombinedView(sfFileName,imageUrl,tempDir);
						if(null != tmpFile)
						{
							fileList.add(tmpFile);
							/*fileNameList.add(tmpFile.getName());
							filePathList.add(tmpFile.getPath());*/
							
						}
					}
				/* else if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".jpg") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".JPG")))
					{
						SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl);
						 fileNameList.add(dmsDocumentDetailsDTO.getFileName());
					 File tmpFile =  SHAFileUtils.downloadImageToTemp(dmsDocumentDetailsDTO.getFileName(),imageUrl,imageTempDir);
					 imgUrl = tmpFile.getAbsolutePath();
					 	//File tmpFile = SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl,tempDir);
						if(null != tmpFile)
						{
							fileList.add(tmpFile);
							fileNameList.add(tmpFile.getName());
							filePathList.add(tmpFile.getPath());
							
						}
					}*/
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				// dmsDocumentDetailsDTO.setFileViewURL(imgUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 preauthFileList.add(dmsDocumentDetailsDTO);
				
				 
			}
			
			
			else if(null != dmsDocumentDetailsDTO.getFileName() && dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) && !(dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)))
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 preauthFileList.add(dmsDocumentDetailsDTO);
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
					 
					 String sfFileName = dmsDocumentDetailsDTO.getFileName();
					 	if(!dmsDocumentDetailsDTO.getFileName().isEmpty() && dmsDocumentDetailsDTO.getFileName().contains("/")){
					 		sfFileName = sfFileName.replaceAll("/", "");
					 	}
					 
					 File tmpFile = SHAFileUtils.downloadFileForCombinedView(sfFileName,imageUrl,tempDir);
						if(null != tmpFile)
						{
							/*fileNameList.add(tmpFile.getName());
							filePathList.add(tmpFile.getPath());*/
							fileList.add(tmpFile);
							
						}
					}
				 
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())
					 || (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				 if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
					 	String sfFileName = dmsDocumentDetailsDTO.getFileName();
					 	if(!dmsDocumentDetailsDTO.getFileName().isEmpty() && dmsDocumentDetailsDTO.getFileName().contains("/")){
					 		sfFileName = sfFileName.replaceAll("/", "");
					 	}
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(sfFileName,imageUrl,tempDir);
						if(null != tmpFile)
						{
							/*fileNameList.add(tmpFile.getName());
							filePathList.add(tmpFile.getPath());*/
							fileList.add(tmpFile);
							
						}
					}
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 
			}
			/*
			 * The below else if condition is a duplicate one only. But supposing if any documents are missed
			 * out in above else if , the below else if will take care of adding in enhancement list.
			 * That's why had retained the below condition 
			 * 
			 * **/
			else if(null != dmsDocumentDetailsDTO.getFileName() && (dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT) || (dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE))))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of enhancement---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
					 
					 String sfFileName = dmsDocumentDetailsDTO.getFileName();
					 	if(!dmsDocumentDetailsDTO.getFileName().isEmpty() && dmsDocumentDetailsDTO.getFileName().contains("/")){
					 		sfFileName = sfFileName.replaceAll("/", "");
					 	}
					 
					 File tmpFile = SHAFileUtils.downloadFileForCombinedView(sfFileName,imageUrl,tempDir);
						if(null != tmpFile)
						{
						/*	fileNameList.add(tmpFile.getName());
							filePathList.add(tmpFile.getPath());*/
							fileList.add(tmpFile);
							
						}
					}
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_REIMBURSEMENT_QUERY_REPORT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				
				
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				 if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
					 
					 String sfFileName = dmsDocumentDetailsDTO.getFileName();
					 	if(!dmsDocumentDetailsDTO.getFileName().isEmpty() && dmsDocumentDetailsDTO.getFileName().contains("/")){
					 		sfFileName = sfFileName.replaceAll("/", "");
					 	}
					 
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(sfFileName,imageUrl,tempDir);
						if(null != tmpFile)
						{
							/*fileNameList.add(tmpFile.getName());
							filePathList.add(tmpFile.getPath());*/
							fileList.add(tmpFile);
							
						}
					}
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 queryReportFileList.add(dmsDocumentDetailsDTO);
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_FVR.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				//this.fvrFileList.add(dmsDocumentDetailsDTO.getFileName());
				
					String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

					if(null != imageUrl)
					 {
						imageUrl = imageUrl.replaceAll(" ","%20");
					 }
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
					 
					 String sfFileName = dmsDocumentDetailsDTO.getFileName();
					 	if(!dmsDocumentDetailsDTO.getFileName().isEmpty() && dmsDocumentDetailsDTO.getFileName().contains("/")){
					 		sfFileName = sfFileName.replaceAll("/", "");
					 	}
					 
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(sfFileName,imageUrl,tempDir);
						if(null != tmpFile)
						{
							/*fileNameList.add(tmpFile.getName());
							filePathList.add(tmpFile.getPath());*/
							fileList.add(tmpFile);
							
						}
					}
				
				//String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of fvr---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());

				fvrFileList.add(dmsDocumentDetailsDTO);
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && !((SHAConstants.ROD_DATA_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource())))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 othersFileList.add(dmsDocumentDetailsDTO);
				
			}
			else if((SHAConstants.SEARCH_UPLOAD_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource()))) 
					&& null != dmsDocumentDetailsDTO.getFileType() && !SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getFileType()))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 ackDocFileList.add(dmsDocumentDetailsDTO);
				
			}
			else if(SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getDocumentType()) 
					|| ((SHAConstants.getGrievanceMasterValueList().contains(dmsDocumentDetailsDTO.getFileType()))
							&& ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource()))))
			{
				/*//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());*/
				 grievanceList.add(dmsDocumentDetailsDTO);
				 grievanceURLList.add(dmsDocumentDetailsDTO.getDmsDocToken());

				
			}
			/***
			 * This below else if condition is not required , since x ray report cannot be
			 * merged as pdf. 
			 ***/
		/*	else if(SHAConstants.X_RAY_REPORT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
				 //|| (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of xray report---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 xRayReportList.add(dmsDocumentDetailsDTO);
			}*/
			else 
			{
				//this.rodFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				if(null != imageUrl)
				 {
					imageUrl = imageUrl.replaceAll(" ","%20");
				 }
				dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of rod---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				if(null != dmsDocumentDetailsDTO.getReimbursementNumber())
				{
					/*Reimbursement reimbursement = billDetailsService.getReimbursementObject(rodNoList.get(i));
					if(null != reimbursement)
					{
						String rodNoFormat = "ROD_"+i+formatDate(reimbursement.getCreatedDate());
					}
					*/
					if(null != rodNoList && !rodNoList.contains(dmsDocumentDetailsDTO.getReimbursementNumber()))
					{
						rodNoList.add(dmsDocumentDetailsDTO.getReimbursementNumber());
					}
					rodFileList.add(dmsDocumentDetailsDTO);
				}
				else
				{
					othersFileList.add(dmsDocumentDetailsDTO);
				}
				
			}
			/*else //if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				this.othersFileList.add(dmsDocumentDetailsDTO);
				
			}*/
		}
		
		
	}
	List<String> rodFormatList = getRodFormatList(rodNoList, rodFileList);
	
	//mergeDocuments(fileList,tempDir,intimationNo);
	
	File tmpFile = mergeDocuments(fileList,tempDir,intimationNo);
	
	request.setAttribute("preauthFileList", preauthFileList);
	request.setAttribute("enhancementFileList", enhancementFileList);
	request.setAttribute("queryReportFileList", queryReportFileList);
	request.setAttribute("fvrFileList", fvrFileList);
	request.setAttribute("othersFileList", othersFileList);
	request.setAttribute("rodFileList", rodFileList);
	request.setAttribute("rodNoList", rodNoList);
	request.setAttribute("rodNoFormatList", rodFormatList);
	request.setAttribute("ackDocFileList", ackDocFileList);
	request.setAttribute("grievanceList", grievanceList);
	request.setAttribute("intimationNo", intimationNo);
	request.setAttribute("pccDMSDocFileList", pccDMSDocFileList);
	request.setAttribute("mergeDocumentsUrl", tmpFile.getAbsolutePath());
	System.out.println("---the abosulte path ----"+tmpFile.getAbsolutePath());
	request.setAttribute("mergedfileName",tmpFile.getName());
	System.out.println("---the abosulte file name ----"+tmpFile.getName());
	

	
	/*String timestamp = mergeDocuments(fileNameList);
	  String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" + File.separator + "mergeddocuments" + File.separator +
				 "CASHLESS_DOCUMENT"+"_"+timestamp+ ".pdf";
	  if(null != request)
	  {
		 request.setAttribute("mergeDocumentsUrl", filePath);
	  }*/
	return request;
}

	private List<String> getRodFormatList(List<String> rodNoList, List<DMSDocumentDetailsDTO> rodFileList)
	{
		 List<String> rodNoFormatList = null;
		/*if(null != rodNoFormatList && !rodNoFormatList.isEmpty())
		{
			this.rodNoFormatList.clear();
		}*/
		if(null != rodNoList && !rodNoList.isEmpty())
		{
			rodNoFormatList = new ArrayList<String>();
			int j =1;
			for (int i = 0; i < rodNoList.size(); i++) {
				String rodNo = rodNoList.get(i);
				//Reimbursement reimbursement = billDetailsService.getReimbursementObject(rodNo);	
				Reimbursement reimbursement = billDetailsService.getLatestReimbursementByRodNumber(rodNo);	
				
				if(null != reimbursement)
				{
					String claimType = "";
					String docRecFrom = "";
					if(null != reimbursement.getClaim() && null != reimbursement.getClaim().getClaimType())
					{
						String claimTypeVal = reimbursement.getClaim().getClaimType().getValue();
						if(null != claimTypeVal && SHAConstants.REIMBURSEMENT_CLAIM_TYPE.equalsIgnoreCase(claimTypeVal))
						{
							claimType = "R";
						}
						else
						{
							claimType = "C";
						}
					}
					if(null != reimbursement.getDocAcknowLedgement() && null != reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId())
					{
						docRecFrom = reimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
					}
					
				
					String rodNoFormat = "ROD_"+(j)+"("+formatDate(reimbursement.getCreatedDate()) +"_"+ docRecFrom+"_)("+claimType+")";
					j++;
							
					rodNoFormatList.add(rodNoFormat);
					if(null != rodFileList && !rodFileList.isEmpty())
					{
						for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : rodFileList) {
							if(null != dmsDocumentDetailsDTO.getReimbursementNumber() && dmsDocumentDetailsDTO.getReimbursementNumber().equalsIgnoreCase(rodNo))
							{
								dmsDocumentDetailsDTO.setRodNoFormat(rodNoFormat);
								//break;
							/*	List<RODDocumentSummary> rodSummaryList = billDetailsService.getRODSummaryDetailsByReimbursementKey(reimbursement.getKey());
								if(null != rodSummaryList && !rodSummaryList.isEmpty())
								{
									for (RODDocumentSummary rodDocumentSummary : rodSummaryList) {
										if(null != dmsDocumentDetailsDTO.getFileName() && dmsDocumentDetailsDTO.getFileName().equalsIgnoreCase(rodDocumentSummary.getFileName()))
										{
											if(null != rodDocumentSummary.getFileType())
											{
												dmsDocumentDetailsDTO.setFileType(rodDocumentSummary.getFileType().getValue());
											}
										}
									}
								}*/
								//break;
							}
						}
					}
				}
			}
		}
		return rodNoFormatList;
	}

	private List<String> getOMPRodFormatList(List<String> rodNoList, List<DMSDocumentDetailsDTO> rodFileList){ 
	 List<String> rodNoFormatList = new ArrayList<String>();
	/*if(null != rodNoFormatList && !rodNoFormatList.isEmpty())
	{
		this.rodNoFormatList.clear();
	}*/
	if(null != rodNoList && !rodNoList.isEmpty())
	{
		int j =1;
		for (int i = 0; i < rodNoList.size(); i++) {
			String rodNo = rodNoList.get(i);
			OMPReimbursement ompReimbursement = ompProcessRODBillEntryService.getReimbursementByRodNo(rodNo);
			
			if(null != ompReimbursement)
			{
				String claimType = "";
				String docRecFrom = "";
				if(null != ompReimbursement.getClaim() && null != ompReimbursement.getClaim().getClaimType())
				{
					String claimTypeVal = ompReimbursement.getClaim().getClaimType().getValue();
					if(null != claimTypeVal && SHAConstants.REIMBURSEMENT_CLAIM_TYPE.equalsIgnoreCase(claimTypeVal))
					{
						claimType = "R";
					}
					else
					{
						claimType = "C";
					}
				}
				if(null != ompReimbursement.getDocAcknowLedgement() && null != ompReimbursement.getDocAcknowLedgement().getDocumentReceivedFromId())
				{
					docRecFrom = ompReimbursement.getDocAcknowLedgement().getDocumentReceivedFromId().getValue();
				}
				
			
				String rodNoFormat = "ROD_"+(j)+"("+formatDate(ompReimbursement.getCreatedDate()) +"_"+ docRecFrom+"_)("+claimType+")";
				j++;
						
				rodNoFormatList.add(rodNoFormat);
				System.out.println("ROD List:" + rodNoFormat);
				if(null != rodFileList && !rodFileList.isEmpty())
				{
					for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : rodFileList) {
						if(null != dmsDocumentDetailsDTO.getReimbursementNumber() && dmsDocumentDetailsDTO.getReimbursementNumber().equalsIgnoreCase(rodNo))
						{
							dmsDocumentDetailsDTO.setRodNoFormat(rodNoFormat);
							System.out.println("Document List:" + rodNoFormat);
						}
					}
				}
			}
		}
	}
	return rodNoFormatList;
}
	
	private String formatDate(Date rodCreatedDate)
	{
		String dateFormat =new SimpleDateFormat("ddMMyyyy_hhmmss").format(rodCreatedDate);
		return dateFormat;
	}
	
	
	//public File mergeDocuments(List<String> filePath , List<String> fileName)
	public File mergeDocuments(List<File> fileList,Path path,String intimationNo)
	{
		String str[] = intimationNo.split("/");
		int iSize = str.length;
		String folderName = str[iSize-1];
		
		PDFMergerUtil pdfMergerUtil = PDFMergerUtil.getInstance();
		List<byte[]> byteArrayStream = SHAFileUtils.getByteArrayFromFiles(fileList);
		if(null != byteArrayStream && null != pdfMergerUtil)
		{
			return (pdfMergerUtil.mergeDocuments(byteArrayStream,path,folderName));
		}
		return null;
	}
	
	
	public Path createTempDirectory(String intimationNo)
	{
		Path tempDir = null;
		try
		{
			String str[] = intimationNo.split("/");
			int iSize = str.length;
			String folderName = str[iSize-1];
			
			tempDir = Files.createTempDirectory(folderName);
			 File file = tempDir.toFile();
			 System.out.println("-----the file path----"+file.getAbsolutePath());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return tempDir;
	}
	
	
	
	private void showOmpPage(String intimationNo, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

			
		  List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist = new ArrayList<DMSDocumentDetailsDTO>();
		
		
		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		OMPClaim claim = ompClaimService.getClaimsByIntimationNumber(intimationNo);
		List<DMSDocumentDetailsDTO> dtoList =  null;
		dmsDocumentDetailsDTOlist = ompProcessRODBillEntryService
				.getDocumentDetailsData(intimationNo);
		
		if (null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTOlist);
		}
		if(null != dmsDocumentDetailsDTOlist && dmsDocumentDetailsDTOlist.isEmpty())
		{
			request.getRequestDispatcher("/WEB-INF/NoClaimErrorPage.jsp").include(request, response);
			  //request.getRequestDispatcher("/WEB-INF/ClaimsDMSErrorPage.jsp").include(request, response);
		}
		else 
		{
			clearSessionandRequest(request,"claimsdms");
			request = intializeOMPFileListBasedOnDocType(dmsDocumentDetailsDTOlist, request,intimationNo);
			request.setAttribute("intimationNo", intimationNo);
			
			//fix for IE
			String userAgent = request.getHeader("User-Agent");
		  	System.out.println("userAgent"+ userAgent);
			if(userAgent.indexOf("MSIE") > -1){
				response.setHeader("X-UA-Compatible", "IE=edge,IE=8");
			}
		
			request.getRequestDispatcher("/WEB-INF/ClaimsDMSView.jsp").include(request, response);
		}
	}
	
	  protected void viewCashlessDocument(String intimationNo, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
		  /**
			 * Release Number : R3
			 * Requirement Number:R0725
			 * Modified By : Durga Rao
			 * Modified On : 15th May 2017
			 * Referred By  : Narasimha
			 */
		  String intimationNumber = (String)request.getSession().getAttribute("intimationNo");
		 /* String intimationNo =  (String) request.getParameter("intimation_number");
		//  String healthCardNo = (String) request.getParameter("idCardNumber");
		  	showPage(intimationNo, request, response);*/
		  if(null != request && null != request.getAttribute("mergeDocumentsUrl"))
		  {
			  request.removeAttribute("mergeDocumentsUrl");  
		  }
		  if(null != request && null != request.getAttribute("mergedfileName"))
		  {
			  request.removeAttribute("mergedfileName");  
		  }
		  
		  if(null != request && null !=  request.getSession().getAttribute("mergeDocumentsUrl"))
		  {
			  request.getSession().removeAttribute("mergeDocumentsUrl");
		  }
		  if(null != request && null != request.getAttribute("mergedfileName"))
		  {
			  request.getSession().removeAttribute("mergedfileName");
		  }
		  
		  //String timeStamp = mergeDocuments(fileNameList);
		  
		 // File tmpFile = mergeDocuments(fileNameList);
		  
		 
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist = new ArrayList<DMSDocumentDetailsDTO>();
		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		List<DMSDocumentDetailsDTO> dtoList =  null;
		if(claim != null ) {
			dmsDTO.setClaimNo(claim.getClaimId());
			dtoList =  billDetailsService.getAcknowledgeDocumentList(claim);
		}
		dmsDocumentDetailsDTOlist = billDetailsService.getDocumentDetailsData(intimationNo, 0);
		if(null != dtoList && !dtoList.isEmpty())
		{
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dtoList) {
				dmsDocumentDetailsDTOlist.add(dmsDocumentDetailsDTO);
			}
		}
		
		if (null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTOlist);
		} 
		  clearSessionandRequest(request,"mergeDocuments");
		  request = intializeAndMergeFileListBasedOnDocType(dmsDocumentDetailsDTOlist, request,intimationNo); 
		/*  String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" + File.separator + "mergeddocuments" + File.separator +
					 "CASHLESS_DOCUMENT"+"_"+timeStamp+ ".pdf";
		  String fileName = "CASHLESS_DOCUMENT"+"_"+timeStamp+ ".pdf";*/
		  if(null != request)
		  {
			request.getRequestDispatcher("/WEB-INF/ViewCashlessDocument.jsp").forward(request, response);
		  }

		
	  }

	  @SuppressWarnings("static-access")
	  private void showOPDocPage(String intimationNo, HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		  List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist = new ArrayList<DMSDocumentDetailsDTO>();
		  DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		  dmsDTO.setIntimationNo(intimationNo);
		  //OMPClaim claim = ompClaimService.getClaimsByIntimationNumber(intimationNo);
		  OPClaim opclaim = claimService.getOPClaimByIntimationNo(intimationNo);
		  OPHealthCheckup ophealth = outpatientService.getOpHealthByClaimKey(opclaim.getKey());
		  //		  List<DMSDocumentDetailsDTO> dtoList =  null;
//		  		  dmsDocumentDetailsDTOlist = opService.getOPDocumentDetailsData(intimationNo);
//		  dmsDocumentDetailsDTOlist = ompProcessRODBillEntryService.getDocumentDetailsData(intimationNo);


		  if(opclaim != null){
			  DocumentSearchApiDto docSearch = new DocumentSearchApiDto();
			  if(opclaim.getIntimation() != null && opclaim.getIntimation().getHospital() != null) {
				  Hospitals hospCode = claimService.getHospitalDetailsByKey(opclaim.getIntimation().getHospital());
				  docSearch.setHospCode(hospCode.getHospitalCode());
				  List<DMSDocumentDetailsDTO> doctDetails = PremiaService.getInstance().getDocumentDetails(docSearch.getHospCode());
				  if(doctDetails != null) {
					  for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : doctDetails) {
						  dmsDocumentDetailsDTO.setHospitalCode(docSearch.getHospCode());
						  dmsDocumentDetailsDTOlist.add(dmsDocumentDetailsDTO);
					  } }
			  }
		  }

		  if(opclaim != null && opclaim.getIntimation() != null && opclaim.getIntimation().getHospital() != null) {
			  DocumentSearchApiDto docSearch = new DocumentSearchApiDto();
			  Hospitals hospCode = claimService.getHospitalDetailsByKey(opclaim.getIntimation().getHospital());
			  docSearch.setHospCode(hospCode.getHospitalCode());
			  DMSDocumentDetailsDTO hospitalDiscount = PremiaService.getInstance().getHospitalDiscounttUrl(docSearch.getHospCode());
			  if(hospitalDiscount!=null){
				  hospitalDiscount.setFileName("Discount");
			  }
			  dmsDocumentDetailsDTOlist.add(hospitalDiscount);
		  }
		  
		  dmsDocumentDetailsDTOlist = outpatientService
					.getDocumentDetailsData(ophealth.getKey());
		  List<DMSDocumentDetailsDTO> dmsDetailsDTOlist = new ArrayList<DMSDocumentDetailsDTO>();
		  dmsDetailsDTOlist = billDetailsService
					.getDocumentDetailsData(intimationNo, 0);
		  dmsDocumentDetailsDTOlist.addAll(dmsDetailsDTOlist);

		  if (null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty()) {
			  dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTOlist);
		  }
		  if(null != dmsDocumentDetailsDTOlist && dmsDocumentDetailsDTOlist.isEmpty()) {
			  request.getRequestDispatcher("/WEB-INF/NoClaimErrorPage.jsp").include(request, response);
		  }else{
			  clearSessionandRequest(request,"claimsdms");
			  request = intializeOMPFileListBasedOnDocType(dmsDocumentDetailsDTOlist, request,intimationNo);
			  request.setAttribute("intimationNo", intimationNo);

			  //fix for IE
			  String userAgent = request.getHeader("User-Agent");
			  System.out.println("userAgent"+ userAgent);
			  if(userAgent.indexOf("MSIE") > -1){
				  response.setHeader("X-UA-Compatible", "IE=edge,IE=8");
			  }

			  request.getRequestDispatcher("/WEB-INF/ClaimsDMSView.jsp").include(request, response);
		  }
	  }
	  
	  private JSONObject getToken(String token){
			
			JSONObject jSONObject = null;
			jSONObject = preauthService.validateToken(token);
			if(jSONObject != null) {
				String requestJsonInString = jSONObject.toJSONString();
				System.out.println("requestJsonInString = " + requestJsonInString);
				
			}
			return jSONObject;
		}
	
}
  
