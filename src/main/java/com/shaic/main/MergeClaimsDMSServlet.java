package com.shaic.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shaic.arch.PDFMergerUtil;
import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.MasterService;
import com.vaadin.ui.Embedded;

public class MergeClaimsDMSServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;	

	@EJB
	private ClaimService claimService;

	@EJB
	private MasterService masterService;

	private final String USER_AGENT = "Mozilla/5.0";

	private List<File> fileList = null;

	private Path tempDir = null;

	private Path imageTempDir = null;

	private final Embedded e = new Embedded();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("doPost");

		String intimationNo = (String)request.getSession().getAttribute("intimationNo");
		String docType = request.getParameter("DocType");
		if(docType !=null){
			String rodNoFormat ="";
			if(docType.equalsIgnoreCase("ROD_FILELIST")){
				rodNoFormat = request.getParameter("RodNoFormat");
			}
			List<DMSDocumentDetailsDTO> detailsDTOs = getDocDetailsByDocType(request,docType,rodNoFormat);
			clearSessionandRequest(request);
			request = intializeAndMergeFileListBasedOnDocType(detailsDTOs,request,intimationNo);
		}	
		if(null != request)
		{
			request.getRequestDispatcher("/WEB-INF/ViewCashlessDocument.jsp").forward(request, response);
		}


	}

	private void clearSessionandRequest(HttpServletRequest request) {

		if(null != request && null != request.getAttribute("mergeDocumentsUrl"))
		{
			request.removeAttribute("mergeDocumentsUrl");  
		}
		if(null != request && null != request.getAttribute("mergedfileName"))
		{
			request.removeAttribute("mergedfileName");  
		}
	}

	private HttpServletRequest intializeAndMergeFileListBasedOnDocType(List<DMSDocumentDetailsDTO> dmsDocDetailsDTOList,HttpServletRequest request,String intimationNo)
	{

		Path tempDir  = createTempDirectory(intimationNo);	
		List<File> fileList = new ArrayList<File>();

		if(null != dmsDocDetailsDTOList && !dmsDocDetailsDTOList.isEmpty())
		{   
			for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocDetailsDTOList) {

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
						fileList.add(tmpFile);
					}
				}					
				dmsDocumentDetailsDTO.setFileViewURL(imageUrl);
				System.out.println("FileViewURL of preauth---------"+imageUrl+"----------intimation no----"+dmsDocumentDetailsDTO.getIntimationNo());
			}
		}

		File tmpFile = mergeDocuments(fileList,tempDir,intimationNo);

		request.setAttribute("intimationNo", intimationNo);
		request.setAttribute("mergeDocumentsUrl", tmpFile.getAbsolutePath());
		System.out.println("---the abosulte path ----"+tmpFile.getAbsolutePath());
		request.setAttribute("mergedfileName",tmpFile.getName());
		System.out.println("---the abosulte file name ----"+tmpFile.getName());

		return request;
	}	

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

	private List<DMSDocumentDetailsDTO> getDocDetailsByDocType(HttpServletRequest request,String docType,String rodNoFormat){

		List<DMSDocumentDetailsDTO> mergeDocs =null;
		List<DMSDocumentDetailsDTO> rodDocs =null;
		if(docType.equalsIgnoreCase("PRE_AUTHORIZATION")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("preauthFileList");
		}
		if(docType.equalsIgnoreCase("ENHANCEMENT")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("enhancementFileList");
		}
		if(docType.equalsIgnoreCase("FIELD_VISIT_REPORT")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("fvrFileList");
		}
		if(docType.equalsIgnoreCase("QUERY_REPORT")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("queryReportFileList");
		}
		if(docType.equalsIgnoreCase("OTHERS")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("othersFileList");
		}
		
		if(docType.equalsIgnoreCase("Post Cashless Cell(PCC)")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("pccDMSDocFileList");
		}
		if(docType.equalsIgnoreCase("ROD_FILELIST")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("rodFileList");
			if(mergeDocs !=null
					&& !mergeDocs.isEmpty() && rodNoFormat != null){
				rodDocs = new ArrayList<DMSDocumentDetailsDTO>();
				for(DMSDocumentDetailsDTO documentDetailsDTO:mergeDocs){
					if(rodNoFormat.equalsIgnoreCase(documentDetailsDTO.getRodNoFormat())){
						rodDocs.add(documentDetailsDTO);
					}
				}
				return rodDocs;
			}
		}
		if(docType.equalsIgnoreCase("ACKNOWLEDGE_DOCUMENTS")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("ackDocFileList");
		}
		if(docType.equalsIgnoreCase("HOSPITAL_DOCS")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("apiList");
		}
		if(docType.equalsIgnoreCase("HOSPITAL_DETAILS")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("hospitalDiscountList");
		}
		if(docType.equalsIgnoreCase("X_RAY")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("xRayReportList");
		}
		if(docType.equalsIgnoreCase("CLAIM_VERIFICATION_REPORT")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("claimVerificationReportDocFileList");
		}
		if(docType.equalsIgnoreCase("LUMEN")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("lumenDocFileList");
		}
		if(docType.equalsIgnoreCase("GRIEVANCE")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("grievanceList");
		}
		if(docType.equalsIgnoreCase("PAYMENT_DETAILS")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("ompPaymentReportDocFileList");
		}
		if(docType.equalsIgnoreCase("ACKNOWLEDGE_DOCUMENTS")){
			mergeDocs = (List<DMSDocumentDetailsDTO>)request.getSession().getAttribute("ompAckFileList");
		}

		return mergeDocs;

	}
}

