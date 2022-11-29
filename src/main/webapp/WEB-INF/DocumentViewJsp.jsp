<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uploaded Document</title>
</head>
<body>
<div>
<%
String fileName = (String)request.getAttribute("fileName");
String fileUrl = (String) request.getAttribute("url");
%>
<% if(fileName.endsWith("pdf") ||fileName.endsWith("PDF")) { %>
<iframe src="${url}"  style="width:1400px; height:700px;" frameborder="0" type="application/pdf"></iframe>
 <%-- <iframe src="${url}" style="width:718px; height:700px;" frameborder="0" type="application/pdf"></iframe> --%>
<%-- <a href='${url}'>Click here to download the file.</a>  --%>
<%-- <object data='${url}' 
        type='application/pdf' 
        width='100%' 
        height='700px'></object> --%>
 <%-- <iframe src="https://docs.google.com/viewer?url=${url}&embedded=true" style="width:718px; height:700px;" frameborder="0" type="application/x-google-chrome-pdf"></iframe> --%> 
<%-- <embed width="100%" height="100%"  src="${ur}" type="application/pdf" > --%>
	<%-- <embed src="${url}" width="800px" height="2100px"> --%>
<% System.out.println(fileName);} else if(fileName.endsWith(".JPG") ||fileName.endsWith(".jpg")) { %>
	<img src= "${url}" style="width:1400px;height:900px;">
	<% } %> 
</div>
</body>
</html>