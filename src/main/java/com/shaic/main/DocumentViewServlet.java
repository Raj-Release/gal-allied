/**
 * 
 */
package com.shaic.main;



import java.io.IOException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.ui.Embedded;

public class DocumentViewServlet extends HttpServlet {
	

  @Inject
  private ClaimService claimService;
  
 /* @Inject
  private DocumentViewPopup documentView;*/
  
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
	  String documentToken =  (String) request.getParameter("documentToken");
	//  String healthCardNo = (String) request.getParameter("idCardNumber");
	  	showPage(documentToken, request, response);
	  }

private void showPage(String dmsDocumentToken, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	
	DocumentDetails docDetails = claimService.getDocumentDetailsBasedOnDocToken(dmsDocumentToken);
	if(null != docDetails)
	{
		if(null != docDetails.getDocumentToken())
		{
			//final String imageUrl = SHAFileUtils.viewFileByToken(String.valueOf(docDetails.getDocumentToken()));
			final String imageUrl = SHAFileUtils.viewFileByToken(String.valueOf(docDetails.getDocumentToken()));
			/*BrowserFrame browserFrame = null;
			if(null != docDetails.getSfFileName() && (docDetails.getSfFileName().endsWith(".JPG") || docDetails.getSfFileName().endsWith(".jpg")))
			{
				
				e.setSource(new ExternalResource(imageUrl));
			    e.setVisible(true);  
			    e.setHeight("500px");
				
				browserFrame = new BrowserFrame("MER Details",
					    new ExternalResource(imageUrl));
			}
			else if(null != docDetails.getSfFileName() && (docDetails.getSfFileName().endsWith(".PDF") || docDetails.getSfFileName().endsWith(".pdf")))
			{
				browserFrame = new BrowserFrame("MER Details",
					    new ExternalResource(imageUrl));
				
				 //Embedded e = new Embedded();
			        e.setSizeFull();
			        e.setType(Embedded.TYPE_BROWSER);
		         StreamResource.StreamSource source = new StreamResource.StreamSource() {
                     public InputStream getStream() {
                        
                     	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     	InputStream is = null;
                     	URL u = null;
                     	try {
                     		u =  new URL(imageUrl);
                     	  is = u.openStream();
                     	  
                     	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
                     	  int n;

                     	  while ( (n = is.read(byteChunk)) > 0 ) {
                     	    baos.write(byteChunk, 0, n);
                     	  }
                     	}
                     	catch (IOException e) {
                     	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
                     	  e.printStackTrace ();
                     	  // Perform any other exception handling that's appropriate.
                     	}
                     	finally {
                     	  if (is != null) {
                     		  try
                     		  {
                     			  is.close();
                     		  }
                     		  catch(Exception e)
                     		  {
                     			  e.printStackTrace();
                     		  }
                     		  }
                     	}
                     	return new ByteArrayInputStream(baos.toByteArray());
                     }
             };
             StreamResource r = new StreamResource(source, docDetails.getSfFileName());
             r.setMIMEType("application/pdf");
             r.setStreamSource(source);
			    e.setSource(r);
				
			}
			else if(null != docDetails.getSfFileName() && (docDetails.getSfFileName().endsWith(".xlsx")))
			{
				
			}
			
			documentView.init(e);*/
			
			  request.setAttribute("url",imageUrl);
			  if(null != docDetails.getSfFileName())
				  request.setAttribute("fileName", docDetails.getSfFileName());
			  else if(null != docDetails.getFileName())
				  request.setAttribute("fileName", docDetails.getFileName());
			  request.getRequestDispatcher("/WEB-INF/DocumentViewJsp.jsp").include(request, response);
		}
	}
	
	  
	  /*if(!error.isEmpty()){
		  request.setAttribute("error", error);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailErrorPage.jsp").include(request, response);
	  }else{
		  request.setAttribute("intimation", intimationByNo);
		  request.setAttribute("hospitals", hospitals);
		  request.getRequestDispatcher("/WEB-INF/ClaimDetailPage.jsp").include(request, response);
		  }
	  }*/
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
  


  
/*  @SuppressWarnings("unused")
private String doPostPremeia(HttpServletRequest request, HttpServletResponse response1) throws IOException{
	  
	  String url = "http://starhealth.in/claimstatus.php";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		String intimationNumber = (String) request.getParameter("intimationNumber");
		  String healthCardNo = (String) request.getParameter("idCardNumber");
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("intimationnumber", intimationNumber);
		con.setRequestProperty("idcardnumber", healthCardNo);
		
		String urlParameters = "intimationnumber=C02G8416DRJM&idcardnumber=gfhty546";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		 PrintWriter out = response1.getWriter();
	  	  out.println (response.toString());  

	
	  
	return null;
	  
  }*/
  
}