<%@page import="com.shaic.domain.DocumentDetails"%>
<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="com.shaic.claim.DMSDocumentDetailsDTO" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" http-equiv="Cache-Control" content="no-cache" />

<title>Insert title here</title>
<style>

button {
  background: none!important;
  border: none;
  padding: 0!important;
  /*optional*/
  font-family: arial, sans-serif;
  /*input has OS specific font-family*/
  color: #069;
  text-decoration: underline;
  cursor: pointer;
}

		*{margin:0;padding:0;}
		body {

		padding:35px;background:#E6E6E6;font-size:80%;font-family:"Helvetica, Arial, sans-serif";padding-top: 5px;height:100%; position:absolute;

		}
		input{
		font-size:1em;
		}
		ol.tree {
			position: relative;
		}
		li{list-style-type:none;color:#464646;position:relative;margin-left:-15px;}
		li label {
			padding-left:34px;cursor:pointer;
			background:url(VAADIN/themes/mytheme/images/folder-horizontal.png) no-repeat 15px 2px; 
			}
		li input{width:1em;height:1em;position:absolute;left:-0.5em;top:0;opacity:0;cursor:pointer;}
		li input + ol {height:1em;margin:-16px 0 0 -44px;background:url(VAADIN/themes/mytheme/images/toggle-small-expand.png) no-repeat 40px 0;
		}
		li input + ol > li{display:none;margin-left:-14px !important;padding-left:1px}
		li.file{margin-left:-1px !important;}
		li.file a{display:inline-block;padding-left:21px;color:#464646;text-decoration:none;background:url(VAADIN/themes/mytheme/images/document.png) no-repeat 0 0;}
		li input:checked + ol{height:auto;margin:-21px 0 0 -44px;padding:25px 0 0 80px;background:url(VAADIN/themes/mytheme/images/toggle-small.png) no-repeat 40px 5px;}
		li input:checked + ol > li{display:block;margin:0 0 0.063em;}
		li input:checked + ol > li:first-child{margin:0 0 0.125em;}
		li input:checked + ol > li:second-child{margin:0 0 0.125em;}
</style>
</head>
<body>

<%! 
	String intimationNo;
	String hospitalCode;
	String fileName;
	String createdDate;
	String fileType;
	String fileViewUrl;
	List<DMSDocumentDetailsDTO> preauthFileList;
	List<DMSDocumentDetailsDTO> enhancementFileList;
	List<DMSDocumentDetailsDTO> queryReportFileList;
	List<DMSDocumentDetailsDTO> fvrFileList;
	List<DMSDocumentDetailsDTO> othersFileList;
	List<DMSDocumentDetailsDTO> rodFileList;
	List<DMSDocumentDetailsDTO> ackDocFileList;
	List<DMSDocumentDetailsDTO> xRayReportList;
	List<DMSDocumentDetailsDTO> apiList;
	List<DMSDocumentDetailsDTO> hospitalDiscountList;
	List<DMSDocumentDetailsDTO> claimVerificationReportDocFileList;
	List<DMSDocumentDetailsDTO> lumenDocFileList;
	List<DMSDocumentDetailsDTO> grievanceList;
	List<DMSDocumentDetailsDTO> ompPaymentReportDocFileList;
	List<DMSDocumentDetailsDTO> referDocsList;
	List<DMSDocumentDetailsDTO> pccDMSDocFileList;
	//List<String> rodNoList;
	List<String> rodNoFormatList;
	List<String> paymentRodNoFormatList;
	List<DMSDocumentDetailsDTO> ompAckFileList;
	String cashlessUrl;
 %>
 <%
	intimationNo = (String)session.getAttribute("intimationNo");
	hospitalCode = (String)session.getAttribute("hospitalCode");
	
	
	preauthFileList = (List<DMSDocumentDetailsDTO>) session.getAttribute("preauthFileList");
	enhancementFileList = (List<DMSDocumentDetailsDTO>) session.getAttribute("enhancementFileList");
	queryReportFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("queryReportFileList");
	fvrFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("fvrFileList");
	othersFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("othersFileList" );
	rodFileList = (List<DMSDocumentDetailsDTO>) session.getAttribute("rodFileList" );
	//rodNoList = (List<String>)session.getAttribute("rodNoList");
	rodNoFormatList = (List<String>)session.getAttribute("rodNoFormatList");
	paymentRodNoFormatList = (List<String>)session.getAttribute("paymentRodNoFormatList");
	ackDocFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("ackDocFileList");
	cashlessUrl = (String)session.getAttribute("mergeDocumentsUrl");
	xRayReportList = (List<DMSDocumentDetailsDTO>)session.getAttribute("xRayReportList");
	apiList = (List<DMSDocumentDetailsDTO>)session.getAttribute("apiList");
	hospitalDiscountList = (List<DMSDocumentDetailsDTO>)session.getAttribute("hospitalDiscountList");

	claimVerificationReportDocFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("claimVerificationReportDocFileList");
	lumenDocFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("lumenDocFileList");
	grievanceList = (List<DMSDocumentDetailsDTO>)session.getAttribute("grievanceList");
	ompPaymentReportDocFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("ompPaymentReportDocFileList");
	referDocsList = (List<DMSDocumentDetailsDTO>)session.getAttribute("referDocsList" );
	pccDMSDocFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("pccDMSDocFileList" );
		//ompAckFileList = (List<DMSDocumentDetailsDTO>)session.getAttribute("opAckFileList" );
	
	
	intimationNo = (String)request.getAttribute("intimationNo");
	fileName = (String)request.getAttribute("fileName");
	createdDate = (String)request.getAttribute("createdDate");
	fileType = (String)request.getAttribute("fileType");
	fileViewUrl = (String)request.getAttribute("fileViewUrl");
	
	System.out.println("----url-----"+cashlessUrl);
  %>
         <ol class="tree">
         	<li>
         	<label for="menu">Root (${intimationNo})</label>
         	 <input type="checkbox" checked id="menu-1" />
         	 
         	<ol>
         	<%
         		if (null != preauthFileList && !preauthFileList.isEmpty()) {
         		
         	%>
         	<li>
              <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
			   <label for="menu-2" >PRE_AUTHORIZATION  </label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="PRE_AUTHORIZATION"/>
					</form> 
					
                <input type="checkbox" checked id="menu-2" />
                    <ol>
                    <c:forEach items="${preauthFileList}" var="dmsDocumentDTO" varStatus="countrow">
                    	<c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "PRE_AUTHORIZATION" />
  						<%-- <c:param name="fileViewUrl" value= "${dmsDocumentDTO.getFileViewURL()}"  /> --%>
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  /> 
  						<c:param name="indexValue" value= "${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							<li class="file"><a href="${theUrl}" target="mainWindow" >${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                   </ol>
                 </li>
                 <% } 
            	if(null != enhancementFileList && !enhancementFileList.isEmpty())
            	{
            %>
                    
                 <li>
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
                 	 <label for="menu-3">ENHANCEMENT</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="ENHANCEMENT"/>
					</form>
                 	
                 	 <input type="checkbox" id="menu-3" />
                		<ol>
						   <c:forEach items="${enhancementFileList}" var="dmsDocumentDTO" varStatus="countrow">
						   <c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "ENHANCEMENT" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
  						<c:param name="indexValue" value="${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							
						<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                     	</ol>
                </li>
                <% }
                if(null != fvrFileList && !fvrFileList.isEmpty())
            	{ %>
                
				 <li>
				 	<!-- <label for="menu-3">FIELD_VISIT_REPORT</label> -->
				 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-4" >FIELD_VISIT_REPORT  </label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="FIELD_VISIT_REPORT"/>
					</form>
				 	<input type="checkbox" id="menu-4" />
                   		<!--  <ul class="droprightMenu"> -->
                   		<ol>
						  <c:forEach items="${fvrFileList}" var="dmsDocumentDTO" varStatus="countrow">
						  <c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "FIELD_VISIT_REPORT" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
  						<c:param name="indexValue" value="${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                        <!--  -</ul>  --->
                        </ol>
                  </li>
                  <%}
               if(null != queryReportFileList && !queryReportFileList.isEmpty())
            	{ %>
                  
                  
                   <li>
                  		<!-- <label for="menu-4">QUERY_REPORT</label> -->
                  		 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-5" >QUERY_REPORT  </label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="QUERY_REPORT"/>
					</form>
                  		<input type="checkbox" id="menu-5" />
	                   		<!-- <ul class="droprightMenu">  --->
	                   	<ol>
							  <c:forEach items="${queryReportFileList}" var="dmsDocumentDTO" varStatus="countrow">
							  <c:url value="/DocumentDetails.jsp" var="theUrl">
	  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
	  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
	  						<c:param name="fileType" value= "QUERY_REPORT" />
	  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"/>
	  						<c:param name="indexValue" value="${countrow.index}"/>
	  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
	  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
							</c:url>
								<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
	                        </c:forEach>
	                        <!--  -</ul> -->
	                       </ol>
	                </li>
	                
	                <%}   if(null != othersFileList && !othersFileList.isEmpty())
            	{ %>
	                
                   <li>
                   	<!-- <label for="menu-5">OTHERS</label> -->
                   	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-6" >OTHERS</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="OTHERS"/>
					</form>
                   	<input type="checkbox" checked id="menu-6" />
                   		<!-- <ul class="droprightMenu">  --->
                   	<ol>
                  
						  <c:forEach items="${othersFileList}" var="dmsDocumentDTO" varStatus="countrow">
						   	<c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "OTHERS" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
  						<c:param name="indexValue" value="${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							<!--  --<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>--->
							<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                        	<!--  -</ul> -->
                      </ol>
                  </li>
 	 <%}   if(null != rodNoFormatList && !rodNoFormatList.isEmpty())
            	{ %>
                
               <!--  <li>
                	<label for="menu-6">ROD</label>
                		<input type="checkbox" checked id="menu-6" />
                 		<ul class="droprightMenu">  -
                 	<ol> -->
                 	<c:forEach items="${rodNoFormatList}" var="rodNoFormat">
                 		<li>
                 		<form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
							<label for="menu-7">${rodNoFormat}</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="ROD_FILELIST"/>
				  		<input type="hidden" name="RodNoFormat" value="${rodNoFormat}"/>
					</form>
                			<input type="checkbox" checked id="menu-7" />
                			<ol>
                				<c:forEach items="${rodFileList}" var="dmsDocumentDTO" varStatus="countrow">
                				 <c:if test="${rodNoFormat == dmsDocumentDTO.getRodNoFormat()}"> 
									<c:url value="/DocumentDetails.jsp" var="theUrl">
	  								<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
	  								<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
	  								<c:param name="fileType" value= "${dmsDocumentDTO.getDocumentType()}"/>
	  								<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
	  								<c:param name="indexValue" value= "${countrow.index}"/>
	  								<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
	  								<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
									</c:url>
										<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()} </a></li>
								</c:if> 
                        		</c:forEach>		
                			</ol>
                			</li>
                 	</c:forEach>
                 	<%} 
                 	if(null != ackDocFileList && !ackDocFileList.isEmpty())
            	{
            %>
                    
                 <li>
                 	<!-- <label for="menu-8">ACKNOWLEDGE DOCUMENTS</label> -->
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-8" >ACKNOWLEDGE DOCUMENTS</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="ACKNOWLEDGE_DOCUMENTS"/>
					</form>
                 	 <input type="checkbox" id="menu-8" />
                		<ol>
						   <c:forEach items="${ackDocFileList}" var="dmsDocumentDTO" varStatus="countrow">
						   <c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "ACKNOWLEDGE_DOCUMENTS" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
  						<c:param name="indexValue" value="${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							
						<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                     	</ol>
                </li>
                
                <% }
                if(null != apiList && !apiList.isEmpty())
            	{
            %>
                    
                 <li>
                 	<!-- <label for="menu-9">HOSPITAL DOCS</label> -->
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-9" >HOSPITAL DOCS  </label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="HOSPITAL_DOCS"/>
					</form>
                 	 <input type="checkbox" id="menu-9" />
                		<ol>
						   <c:forEach items="${apiList}" var="dmsDocumentDTO">
						   <c:url value="/DocumentDetailsHosp.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "HOSPITAL_DOCS" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getFileViewURL()}"  />
  						<c:param name="indexValue" value="${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							
						<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                     	</ol>
                </li>
                <% }
                
                if(null != hospitalDiscountList && !hospitalDiscountList.isEmpty())
            	{
            %>
                
                <li>
                 	<%-- <label for="menu-10">HOSPITAL DETAILS-(${hospitalCode})</label> --%>
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-10">HOSPITAL DETAILS-(${hospitalCode})</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="HOSPITAL_DETAILS"/>
					</form>
                 	 <input type="checkbox" id="menu-10" />
                		<ol>
						   <c:forEach items="${hospitalDiscountList}" var="dmsDocumentDTO">
						   <c:url value="/HospitalDiscount.jsp" var="theUrl">
						   <c:param name="fileName" value= "${dmsDocumentDTO.getHosiptalDiscount()}" />
  						<c:param name="fileType" value= "HOSPITAL_DETAILS" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getHosiptalDiscount()}"  />
  						<c:param name="restapiurl" value= ""/>
  						<c:param name="hospitalDiscount" value= "${dmsDocumentDTO.getHosiptalDiscount()}"  />
						</c:url>
							
						<li class="file"><a href="${dmsDocumentDTO.getHosiptalDiscount()}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                     	</ol>
                </li>
                <% }
                if(null != xRayReportList && !xRayReportList.isEmpty())
            	{
            %>
                    
                 <li>
                 	<!-- <label for="menu-11">X RAY</label> -->
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-11">X RAY</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="X_RAY"/>
					</form>
                 	 <input type="checkbox" id="menu-11" />
                		<ol>
						   <c:forEach items="${xRayReportList}" var="dmsDocumentDTO">
						   <c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "X RAY REPORT" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							
						<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                     	</ol>
                </li>
                <% 
                	} if(null != claimVerificationReportDocFileList && !claimVerificationReportDocFileList.isEmpty()) {
            	%>
                <li>
                 	<!-- <label for="menu-10">CLAIM VERIFICATION REPORT</label> -->
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-12" >CLAIM VERIFICATION REPORT</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="CLAIM_VERIFICATION_REPORT"/>
					</form>
                 	<input type="checkbox" id="menu-12" />
                    <ol>
						<c:forEach items="${claimVerificationReportDocFileList}" var="dmsDocumentDTO" varStatus="countrow">
						   	<c:url value="/ClaimVerificationReport.jsp" var="theUrl">
		  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
		  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
		  						<c:param name="fileType" value= "${dmsDocumentDTO.getDocumentType()}" />
		  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
		  						<c:param name="indexValue" value="${countrow.index}"/>
		  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
		  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
							</c:url>
							<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                    </ol>
                </li>
                
                <% 
                	} if(null != lumenDocFileList && !lumenDocFileList.isEmpty()) {
            	%>
                <li>
                 	<!-- <label for="menu-10">LUMEN</label> -->
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-13" >LUMEN</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="LUMEN"/>
					</form>
                 	<input type="checkbox" id="menu-13" />
                    <ol>
						<c:forEach items="${lumenDocFileList}" var="dmsDocumentDTO" varStatus="countrow">
						   	<c:url value="/Lumen.jsp" var="theUrl">
		  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
		  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
		  						<c:param name="fileType" value= "Lumen" />
		  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
		  						<c:param name="indexValue" value="${countrow.index}"/>
		  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
		  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
							</c:url>
							<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                    </ol>
                </li>
                <% 
                	}if(null != grievanceList && !grievanceList.isEmpty())
            	{
            %>
                    
                 <li>
                 	<!-- <label for="menu-12">Grievance</label> -->
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-14" >Grievance</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="GRIEVANCE"/>
					</form>
                 	 <input type="checkbox" id="menu-14" />
                		<ol>
						   <c:forEach items="${grievanceList}" var="dmsDocumentDTO" varStatus="countrow">
						   <c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "${dmsDocumentDTO.getDocumentType()}" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
  						<c:param name="indexValue" value="${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							
						<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                     	</ol>
                </li>
                
                 <% 
                	} 
                	
                	if(null != ompPaymentReportDocFileList && !ompPaymentReportDocFileList .isEmpty()) 
                {
            	%>
                <li>
                 	<!-- <label for="menu-13">PAYMENT DETAILS</label> -->
                 	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
						<label for="menu-15" >PAYMENT DETAILS</label>
							<button type="submit" >[Merge]</button>
				  		<input type="hidden" name="DocType" value="PAYMENT_DETAILS"/>
					</form>
                 	<input type="checkbox" id="menu-15" />
                    <ol>
                    <% 
					 if(null != paymentRodNoFormatList && !paymentRodNoFormatList.isEmpty()) 
					 {  %>
                 	<c:forEach items="${paymentRodNoFormatList}" var="omprodNoFormat">
                 		<li>
                 		<label for="menu-16">${omprodNoFormat}</label>
                			<input type="checkbox" checked id="menu-16" />
                		<ol>
						<c:forEach items="${ompPaymentReportDocFileList}" var="dmsDocumentDTO" varStatus="countrow">
						 <c:if test="${omprodNoFormat == dmsDocumentDTO.getRodNoFormat()}">
						   	<c:url value="/OMPPaymentReport.jsp" var="theUrl">
		  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
		  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
		  						<c:param name="fileType" value= "${dmsDocumentDTO.getDocumentType()}" />
		  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
		  						<c:param name="indexValue" value="${countrow.index}"/>
		  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
		  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
							</c:url>
							<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
							</c:if>
                        </c:forEach>
                    </ol>
                			</li>
                 	</c:forEach>
                 	<%
                 	} 
                 	%>
                    </ol>
                </li>
              <%
                }
                if(null != referDocsList && !referDocsList.isEmpty())
            	{ %>
	                
                   <li>
                   	<label for="menu-17">REFERENCE DOCUMENTS</label>
                   	<input type="checkbox" checked id="menu-17" />
                   		<!-- <ul class="droprightMenu">  --->
                   	<ol>
                  
						  <c:forEach items="${referDocsList}" var="dmsDocumentDTO" varStatus="countrow">
						   	<c:url value="/DocumentDetails.jsp" var="theUrl">
  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
  						<c:param name="fileType" value= "REFERDOCS" />
  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
  						<c:param name="indexValue" value="${countrow.index}"/>
  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
						</c:url>
							<!--  --<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>--->
							<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                        </c:forEach>
                        	<!--  -</ul> -->
                      </ol>
                  </li>
 	 <%}
                if(null != pccDMSDocFileList && !pccDMSDocFileList.isEmpty()) {
                	%>
                    <li>
                     	<!-- <label for="menu-18">POST CASHLESS CELL(PCC)</label> -->
                     	 <form action="mergeClaimsDMSServlet" method="post" target="mainWindow" >
    						<label for="menu-18" >POST CASHLESS CELL(PCC)</label>
    							<button type="submit" >[Merge]</button>
    				  		<input type="hidden" name="DocType" value="Post Cashless Cell(PCC)"/>
    					</form>
                     	<input type="checkbox" id="menu-18" />
                        <ol>
    						<c:forEach items="${pccDMSDocFileList}" var="dmsDocumentDTO" varStatus="countrow">
    						   	<c:url value="/DocumentDetails.jsp" var="theUrl">
    		  						<c:param name="fileName" value= "${dmsDocumentDTO.getFileName()}" />
    		  						<c:param name="createdDate" value= "${dmsDocumentDTO.getDocumentCreatedDateValue()}"  />
    		  						<c:param name="fileType" value= "Post Cashless Cell(PCC)" />
    		  						<c:param name="fileViewUrl" value= "${dmsDocumentDTO.getDmsDocToken()}"  />
    		  						<c:param name="indexValue" value="${countrow.index}"/>
    		  						<c:param name="restapiurl" value= "${dmsDocumentDTO.getDmsRestApiURL()}"/>
    		  						<c:param name="doctorRemark" value= "${dmsDocumentDTO.getDeductionRemarks()}"  />
    							</c:url>
    							<li class="file"><a href="${theUrl}" target="mainWindow">${dmsDocumentDTO.getFileName()}</a></li>
                            </c:forEach>
                        </ol>
                    </li>
                    
                    <% }
                    //if(ompAckFileList!=null && !ompAckFileList.isEmpty()){} %>
                 	
                   <!--  </ol>
                </li> -->
               </ol>
             </li>
         </ol>
    <%--      <div><a href = "${cashlessUrl}" target = "mainWindow">View Cashless Document</a></div> --%>
        <!--   <form action = "ClaimsDMSServlet" method="post" target="mainWindow">
            <div>
            	<input type="submit" value="View Cashless Document" />
            </div>
            </form>  -->
</body>

</html>


