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
	session.removeAttribute("preauthFileList");
	session.removeAttribute("enhancementFileList");
	session.removeAttribute("queryReportFileList");
	session.removeAttribute("fvrFileList");
	session.removeAttribute("othersFileList");
	session.removeAttribute("rodFileList");
	session.removeAttribute("rodNoList");
	session.removeAttribute("rodNoFormatList");
	session.removeAttribute("mergeDocumentsUrl"); 
	 
	
	
    session.setAttribute("intimationNo", request.getAttribute("intimationNo"));
	session.setAttribute("preauthFileList", request.getAttribute("preauthFileList"));
	session.setAttribute("enhancementFileList", request.getAttribute("enhancementFileList") );
	session.setAttribute("queryReportFileList", request.getAttribute("queryReportFileList"));
	session.setAttribute("fvrFileList", request.getAttribute("fvrFileList") );
	session.setAttribute("othersFileList", request.getAttribute("othersFileList") );
	session.setAttribute("rodFileList",request.getAttribute("rodFileList") );
	session.setAttribute("rodNoList",request.getAttribute("rodNoList") );
	session.setAttribute("rodNoFormatList",request.getAttribute("rodNoFormatList") );
	session.setAttribute("mergeDocumentsUrl",request.getAttribute("mergeDocumentsUrl") );
	session.setAttribute("referDocsList", request.getAttribute("referDocsList"));
	session.setAttribute("referDocsURLList", request.getAttribute("referDocsURLList"));
%>
<frameset cols="23%,*">
	<!--  --<frameset rows="50%,*">

		<frame src="DmsMenus.jsp?value=" scrolling="yes">
		<frame src="MenuButton.jsp">  
	</frameset>
	<frameset rows="100%,*"> --->
		<frame src="DmsMenus.jsp"  style='height:100%' scrolling="yes">
			<frame src = "ViewCashlessDocument.jsp" name = "mainWindow">
	</frameset>
</frameset>
</html>




