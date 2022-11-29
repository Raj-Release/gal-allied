<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
      <%@ page import="com.vaadin.server.StreamResource" %>
       <%@ page import="java.io.*" %>
       <%@ page import="java.net.URL" %>	
         <%@ page import="java.util.List" %>
    <%@page import="java.io.InputStream"%>
    <%@page import="java.io.FileInputStream"%>
    <%@page import = "java.io.File" %>
    <%@page import="java.io.IOException"%>
    <%@ page import="com.shaic.claim.DMSDocumentDetailsDTO" %>
       

       
   
<html>
<head>



<meta charset="utf-8" http-equiv="Cache-Control" content="no-cache" />
<title>Insert title here</title>
<!-- <script   src="https://code.jquery.com/jquery-3.1.1.js" crossorigin="anonymous"></script> -->
<script   src="js/jquery-3.1.js" crossorigin="anonymous"></script> 
<script>
$(function (){
	$.ajax({
	type : "POST",
	crossDomain : true ,
	contentType: "application/json; charset=utf-8",
	//url : "http://localhost:9870/ims/rest/document-url/documentToken/"+ $("#p1").text(),
	url : $("#p2").text() + $("#p1").text(),
	beforeSend: function(x) {
  		if(x && x.overrideMimeType) {
   		x.overrideMimeType("application/json; charset=utf-8");
  	}
 	},
	dataType : "json",
	error: function (jqXHR, textStatus, errorThrown) {
      $("#viewFrame").attr("src" , jqXHR.responseText);
	}, 
	success : function(result){
	console.log(result);  
	}});

});
</script>

</head>
<style>
	

</style>


<body>

<%!

String url;
String fileName;
StreamResource resource;
String test ;
String filePath;
String nextfile;
String previousFile;
DMSDocumentDetailsDTO preauthDocumentList;

 %>
 
 <% 
 	url = request.getParameter("fileViewUrl");
 	fileName = request.getParameter("fileName"); 
	filePath = (String)session.getAttribute("mergeDocumentsUrl"); 
	preauthDocumentList = (DMSDocumentDetailsDTO)session.getAttribute("preauthDocument");
 %>
 
 <!-- <script type="text/javascript" src="http://code.jquery.com/jquery-1.4.3.min.js"></script> --> 

<div>

  <c:set var="fileName" scope="session" value=""/>
  
  <c:set var="fileType" scope="session" value=""/>
  <c:set var="fileViewUrl" scope="session" value=""/>
 
  
  
   <c:forEach items="${param}" var="currentParam">
   		<c:if test="${currentParam.key == 'fileViewUrl'}">
   			<c:set var="fileViewUrl" value="${currentParam.value}"/>
   			<c:set var="url" value="${currentParam.value}"/>
   		</c:if>
   		<c:if test="${currentParam.key == 'fileType'}">
   			<c:set var="fileType" value="${currentParam.value}"/>
   		</c:if>
   	
   		<c:if test="${currentParam.key == 'fileName'}">
   			<c:set var="fileName" value="${currentParam.value}"/>
   		</c:if>
   		
   		<c:if test="${currentParam.key == 'restapiurl'}"> 
   			<c:set var="restapiurl" value="${currentParam.value}"/>
   		</c:if>
   		
      </c:forEach>
      
      
	<p id = "p1" hidden ="true">${fileViewUrl}</p>
	<p id = "p2" hidden ="true">${dmrestApiUrl}</p>
	
	<input type = "hidden" id = "text1" value="34088">
	</input> 
	
	</div>

	<div id = "viewFramee">
	   <iframe id ="viewFrame" src="" width="100%" height="100%" style="position:relative;"></iframe>
	  </div>
	  
	  
</body>
</html>
