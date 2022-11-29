<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="no-cache"  charset=ISO-8859-1">
<title>Claims Documents</title>
</head>
<%
	session.removeAttribute("intimationNo");
	session.removeAttribute("hospitalCode");
	session.removeAttribute("preauthFileList");
	session.removeAttribute("enhancementFileList");
	session.removeAttribute("queryReportFileList");
	session.removeAttribute("fvrFileList");
	session.removeAttribute("othersFileList");
	session.removeAttribute("rodFileList");
	session.removeAttribute("rodNoList");
	session.removeAttribute("rodNoFormatList");
	session.removeAttribute("mergeDocumentsUrl"); 
	session.removeAttribute("mergedfileName");
	session.removeAttribute("ackDocFileList");
	session.removeAttribute("xRayReportList");
	session.removeAttribute("ackDocFileList"); 
	session.removeAttribute("preauthURLlist");
	session.removeAttribute("enhancementURLList");
	session.removeAttribute("queryURLList");
	session.removeAttribute("fvrURLList");
	session.removeAttribute("othersURLList");
	session.removeAttribute("rodURLList");
	session.removeAttribute("ackDocURLList");
	session.removeAttribute("apiList");
	session.removeAttribute("hospitalDiscountList");
	session.removeAttribute("claimVerificationReportDocFileList");
	session.removeAttribute("claimVerificationReportDocURLList");
	session.removeAttribute("lumenDocFileList");
	session.removeAttribute("lumenDocURLList");
	session.removeAttribute("ompPaymentReportDocFileList");
	session.removeAttribute("paymentRodNoFormatList");
	session.removeAttribute("paymentRodNoList");
	session.removeAttribute("referDocsList");
	session.removeAttribute("referDocsURLList");
	session.removeAttribute("pccDMSDocFileList");
	session.removeAttribute("PccDocURLList");
	//session.removeAttribute("opAckFileList");
	session.removeAttribute("ompackDocURLList");

    session.setAttribute("intimationNo", request.getAttribute("intimationNo"));
  	session.setAttribute("hospitalCode", request.getAttribute("hospitalCode")); 
	session.setAttribute("preauthFileList", request.getAttribute("preauthFileList"));
	session.setAttribute("enhancementFileList", request.getAttribute("enhancementFileList") );
	session.setAttribute("queryReportFileList", request.getAttribute("queryReportFileList"));
	session.setAttribute("fvrFileList", request.getAttribute("fvrFileList") );
	session.setAttribute("othersFileList", request.getAttribute("othersFileList") );
	session.setAttribute("rodFileList",request.getAttribute("rodFileList") );
	session.setAttribute("rodNoList",request.getAttribute("rodNoList") );
	session.setAttribute("rodNoFormatList",request.getAttribute("rodNoFormatList") );
	session.setAttribute("mergeDocumentsUrl",request.getAttribute("mergeDocumentsUrl") );
	session.setAttribute("mergedfileName",request.getAttribute("mergedfileName") );
	session.setAttribute("ackDocFileList",request.getAttribute("ackDocFileList") );
	session.setAttribute("xRayReportList",request.getAttribute("xRayReportList") );
	session.setAttribute("preauthURLlist",request.getAttribute("preauthURLlist") );
	session.setAttribute("enhancementURLList",request.getAttribute("enhancementURLList") );
	session.setAttribute("fvrURLList",request.getAttribute("fvrURLList") );
	session.setAttribute("othersURLList",request.getAttribute("othersURLList") );
	session.setAttribute("rodURLList",request.getAttribute("rodURLList") );
	session.setAttribute("ackDocURLList",request.getAttribute("ackDocURLList") );
	session.setAttribute("apiList", request.getAttribute("apiList"));
	session.setAttribute("hospitalDiscountList", request.getAttribute("hospitalDiscountList"));
	session.setAttribute("claimVerificationReportDocFileList", request.getAttribute("claimVerificationReportDocFileList"));
	session.setAttribute("claimVerificationReportDocURLList", request.getAttribute("claimVerificationReportDocURLList"));
	session.setAttribute("lumenDocFileList", request.getAttribute("lumenDocFileList"));
	session.setAttribute("lumenDocURLList", request.getAttribute("lumenDocURLList"));
	session.setAttribute("grievanceList", request.getAttribute("grievanceList"));
	session.setAttribute("ompPaymentReportDocFileList", request.getAttribute("ompPaymentReportDocFileList"));
	session.setAttribute("paymentRodNoList", request.getAttribute("paymentRodNoList"));
	session.setAttribute("paymentRodNoFormatList", request.getAttribute("paymentRodNoFormatList"));
	session.setAttribute("referDocsList", request.getAttribute("referDocsList"));
	session.setAttribute("referDocsURLList", request.getAttribute("referDocsURLList")); 
	session.setAttribute("pccDMSDocFileList", request.getAttribute("pccDMSDocFileList"));
	session.setAttribute("PccDocURLList", request.getAttribute("PccDocURLList"));
	//session.setAttribute("opAckFileList", request.getAttribute("opAckFileList"));
	session.setAttribute("ompackDocURLList", request.getAttribute("ompackDocURLList"));
	
%>

<frameset cols="20%,*">
		<frameset rows="65%,*">
		<frame src="DmsMenus.jsp"  style='height:100%' scrolling="yes">
		<frame src="DmsCashlessFile.jsp">
		</frameset>
		<frameset rows="5%,*">
		<frame src = "ViewProposal.jsp">
		<%
		String filePath = (String)session.getAttribute("mergeDocumentsUrl");
			if(null != filePath && !("").equalsIgnoreCase(filePath)){
			System.out.println("inside the view cashless document.jsp method------->");
			%>	
			<frame src = "" name = mainWindow>
			<%}else{%>
		  <frame src=""
			name="mainWindow"> 
			<%} %>
		</frameset>	
	</frameset>
</html>