<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title> View Claim Status   </title>
		<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    	<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
	</head>	
	<body style="background-color: #0067B4;">	
	<body>
<div>
<%
String url = (String)request.getAttribute("url");%>
 <iframe src="${url}" style="width:100%; height:800px;" frameborder="0"></iframe> 
</div>
</body>
	
</html>