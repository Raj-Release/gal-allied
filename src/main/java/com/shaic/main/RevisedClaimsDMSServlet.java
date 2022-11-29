/**
 * 
 */
package com.shaic.main;

/**
 * @author ntv.vijayar
 *
 */




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

import com.shaic.arch.PDFMergerUtil;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Reimbursement;
import com.vaadin.ui.Embedded;

public class RevisedClaimsDMSServlet extends HttpServlet {
	


	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private ClaimService claimService;
  
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
		  showPage(dmsDocumentToken, request, response);
	  }
//    request.getRequestDispatcher("/WEB-INF/index.jsp").include(request, response);
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
	  /*String filePath = System.getProperty("jboss.server.data.dir") + File.separator + "cashlessdocuments" + File.separator + "mergeddocuments" + File.separator +
				 "CASHLESS_DOCUMENT"+"_"+timeStamp+ ".pdf";
	  String fileName = "CASHLESS_DOCUMENT"+"_"+timeStamp+ ".pdf";*/
	  if(null != request)
	  {
		request.getRequestDispatcher("/WEB-INF/ViewCashlessDocument.jsp").forward(request, response);
	  }

	
  }

private void showPage(String intimationNo, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	

		
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
	if(null != dmsDocumentDetailsDTOlist && dmsDocumentDetailsDTOlist.isEmpty())
	{
	//	 request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimsDMSErrorPage.jsp").include(request, response);
	}
	else 
	{
		clearSessionandRequest(request,"claimsdms");
		request = intializeFileListBasedOnDocType(dmsDocumentDetailsDTOlist, request,intimationNo);
		request.setAttribute("intimationNo", intimationNo);
	
		request.getRequestDispatcher("/WEB-INF/ClaimsDMSView.jsp").include(request, response);
	}
}

private void clearSessionandRequest(HttpServletRequest request,String methodName) {
	if(null != methodName && ("claimsdms").equalsIgnoreCase(methodName))
	{
		if(null != request.getAttribute("intimationNo"))
		{
			request.removeAttribute("intimationNo");
		}
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
	if(null != request.getAttribute("rodNoFormatList"))
	{
		request.removeAttribute("rodNoFormatList");
	}
	
	if(null != request.getAttribute("ackDocFileList"))
	{
		request.removeAttribute("ackDocFileList");
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
	
	
	//
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
	//preauthURLlist
	
	
	// clearing the session.....
	if(request != null &&  request.getSession() != null) {
		if(null != methodName && ("claimsdms").equalsIgnoreCase(methodName))
		{
			if(null != request.getSession().getAttribute("intimationNo"))
			{
				 request.getSession().removeAttribute("intimationNo");
			}
		}
		if(null !=  request.getSession().getAttribute("preauthFileList"))
		{
			 request.getSession().removeAttribute("preauthFileList");
		}
		if(null !=  request.getSession().getAttribute("enhancementFileList"))
		{
			 request.getSession().removeAttribute("enhancementFileList");
		}
		if(null !=  request.getSession().getAttribute("queryReportFileList"))
		{
			 request.getSession().removeAttribute("queryReportFileList");
		}
		if(null !=  request.getSession().getAttribute("fvrFileList"))
		{
			 request.getSession().removeAttribute("fvrFileList");
		}
		if(null !=  request.getSession().getAttribute("othersFileList"))
		{
			 request.getSession().removeAttribute("othersFileList");
		}
		if(null !=  request.getSession().getAttribute("rodFileList"))
		{
			 request.getSession().removeAttribute("rodFileList");
		}
		if(null !=  request.getSession().getAttribute("rodNoList"))
		{
			 request.getSession().removeAttribute("rodNoList");
		}
		if(null !=  request.getSession().getAttribute("rodNoFormatList"))
		{
			 request.getSession().removeAttribute("rodNoFormatList");
		}
		if(null != request.getSession().getAttribute("ackDocFileList"))
		{
			request.getSession().removeAttribute("ackDocFileList");
		}
		if(null != request.getSession().getAttribute("mergedfileName"))
		{
			request.getSession().removeAttribute("mergedfileName");
		}
		if(null != request.getSession().getAttribute("mergeDocumentsUrl"))
		{
			request.getSession().removeAttribute("mergeDocumentsUrl");
		}

		if(null != request.getSession().getAttribute("xRayReportList"))
		{
			request.getSession().removeAttribute("xRayReportList");
		}

		if(null != request.getSession().getAttribute("preauthURLlist"))
		{
			request.getSession().removeAttribute("preauthURLlist");
		}
		if(null != request.getSession().getAttribute("enhancementURLList"))
		{
			request.getSession().removeAttribute("enhancementURLList");
		}
		if(null != request.getSession().getAttribute("queryURLList"))
		{
			request.getSession().removeAttribute("queryURLList");
		}
		if(null != request.getSession().getAttribute("fvrURLList"))
		{
			request.getSession().removeAttribute("fvrURLList");
		}
		if(null != request.getSession().getAttribute("othersURLList"))
		{
			request.getSession().removeAttribute("othersURLList");
		}
		if(null != request.getSession().getAttribute("ackDocURLList"))
		{
			request.getSession().removeAttribute("ackDocURLList");
		}
		
		
	}
	
}


private HttpServletRequest intializeFileListBasedOnDocType( List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOlist, HttpServletRequest request,String intimationNo)
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

	  
	  List<DMSDocumentDetailsDTO> xRayReportList = new ArrayList<DMSDocumentDetailsDTO>();
	  List<String> ackDocURLList = new ArrayList<String>();

	  
	  List<String> rodNoList = new ArrayList<String>();
	  
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
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
			
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				// dmsDocumentDetailsDTO.setFileViewURL(imgUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 preauthFileList.add(dmsDocumentDetailsDTO);
				 preauthURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				
				 
			}
			
			
			else if(null != dmsDocumentDetailsDTO.getFileName() && dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) && !(dmsDocumentDetailsDTO.getFileName().contains(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)))
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 preauthFileList.add(dmsDocumentDetailsDTO);
				 preauthURLList.add(dmsDocumentDetailsDTO.getFileViewURL());


				 
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_ENHANCEMENT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())
					 || (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
			
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 enhancementURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				 
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
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of enhancement---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 enhancementURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_REIMBURSEMENT_QUERY_REPORT.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				
				
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 
				 
				 
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 queryReportFileList.add(dmsDocumentDetailsDTO);
				 queryURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_FVR.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
			{
				//this.fvrFileList.add(dmsDocumentDetailsDTO.getFileName());
				
					String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 
				
				
				//String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());

				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of fvr---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());

				fvrFileList.add(dmsDocumentDetailsDTO);
				fvrURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				
			}
			else if(SHAConstants.PREMIA_DOC_TYPE_OTHERS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && !((SHAConstants.ROD_DATA_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource())))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 othersFileList.add(dmsDocumentDetailsDTO);
				 othersURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				
			}
			else if(SHAConstants.SEARCH_UPLOAD_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource())))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 ackDocFileList.add(dmsDocumentDetailsDTO);
				 ackDocURLList.add(dmsDocumentDetailsDTO.getFileViewURL());

				
			}
			else if(SHAConstants.X_RAY_REPORT_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()))
					 //|| (dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH_DOWNSIZE)) || (dmsDocumentDetailsDTO.getDocumentType().contains(SHAConstants.FILE_NAME_STARTS_WITH_ENHANCEMENT)))
			{
				//this.enhancementFileList.add(dmsDocumentDetailsDTO.getFileName());
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of xray report---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 xRayReportList.add(dmsDocumentDetailsDTO);
				 
			}
			else 
			{
				//this.rodFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
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
					
					if(!(SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) || SHAConstants.BILLASSESSMENTSHEETSCRC.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())))
							{
								rodFileList.add(dmsDocumentDetailsDTO);
							}
					rodURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
				}
				else
				{
					othersFileList.add(dmsDocumentDetailsDTO);
					othersURLList.add(dmsDocumentDetailsDTO.getFileViewURL());
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
	request.setAttribute("xRayReportList",xRayReportList);
	

	request.setAttribute("preauthURLlist", preauthURLList);
	request.setAttribute("enhancementURLList", enhancementURLList);
	request.setAttribute("queryURLList", queryURLList);
	request.setAttribute("fvrURLList", fvrURLList);
	request.setAttribute("othersURLList", othersURLList);
	request.setAttribute("rodURLList", rodURLList);
	request.setAttribute("ackDocURLList", ackDocURLList);
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
	  
	  
	  
	  List<File> fileList = new ArrayList<File>();

	  
	  List<String> rodNoList = new ArrayList<String>();
	  
	  Path tempDir  = createTempDirectory(intimationNo);
	  
	
	  
	
	 

	if(null != dmsDocumentDetailsDTOlist && !dmsDocumentDetailsDTOlist.isEmpty())
	{
		for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOlist) {
			
			if( SHAConstants.PREMIA_DOC_TYPE_PRE_AUTHORISATION.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) 
					|| dmsDocumentDetailsDTO.getDocumentType().startsWith(SHAConstants.FILE_NAME_STARTS_WITH_PREAUTH) 
					)
			{
				//this.preauthFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imgUrl = "";
				 String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
						/*SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl);
						 fileNameList.add(dmsDocumentDetailsDTO.getFileName());*/
					 	File tmpFile = SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl,tempDir);
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
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 preauthFileList.add(dmsDocumentDetailsDTO);
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
					 File tmpFile = SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl,tempDir);
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
				 
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl,tempDir);
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
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of enhancement---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 enhancementFileList.add(dmsDocumentDetailsDTO);
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
					 File tmpFile = SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl,tempDir);
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
				 
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl,tempDir);
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
				 
				 if(null != dmsDocumentDetailsDTO.getFileName() && ((dmsDocumentDetailsDTO.getFileName()).endsWith(".pdf") || (dmsDocumentDetailsDTO.getFileName()).endsWith(".PDF")))
					{
						File tmpFile = SHAFileUtils.downloadFileForCombinedView(dmsDocumentDetailsDTO.getFileName(),imageUrl,tempDir);
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
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 othersFileList.add(dmsDocumentDetailsDTO);
				
			}
			else if(SHAConstants.SEARCH_UPLOAD_DOC_TYPE.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) && ((SHAConstants.SEARCH_UPLOAD_DOC_SOURCE).equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentSource())))
			{
				//this.othersFileList.add(dmsDocumentDetailsDTO.getFileName());
				String imageUrl = SHAFileUtils.viewFileByToken(dmsDocumentDetailsDTO.getDmsDocToken());
				 dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				 System.out.println("FileViewURL of others---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
				 ackDocFileList.add(dmsDocumentDetailsDTO);
				
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
					if(!(SHAConstants.BILL_SUMMARY_OTHER_PRODUCTS.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType()) || SHAConstants.BILLASSESSMENTSHEETSCRC.equalsIgnoreCase(dmsDocumentDetailsDTO.getDocumentType())))
					{
						rodFileList.add(dmsDocumentDetailsDTO);
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
	request.setAttribute("intimationNo", intimationNo);
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
				Reimbursement reimbursement = billDetailsService.getReimbursementObject(rodNo);
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
	
	
	
}
  
