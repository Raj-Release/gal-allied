<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.vaadin.server.StreamResource"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.util.List"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.IOException"%>
<%@ page import="com.shaic.claim.DMSDocumentDetailsDTO"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="utf-8" http-equiv="Cache-Control" content="no-cache" />
<title>DMS View - Claim Verification Report</title>
<script src="js/jquery-3.1.js" crossorigin="anonymous"></script>
<script>
$(function (){
	$.ajax({
	type : "POST",
	crossDomain : true ,
	contentType: "application/json; charset=utf-8",
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
table {
	color: #333;
	font-family: Helvetica, Arial, sans-serif;
	font-size: 12px;
	width: 850px;
	border-spacing: 0;
	position: relative;
}

p {
	color: #333;
	font-family: Helvetica, Arial, sans-serif;
	font-size: 15px;
	position: relative;
	padding-left: 350px;
}

td, tr {
	border: 1.5px solid black; /* No more visible border */
	height: 20px;
	transition: all 0.3s; /* Simple transition for hover effect */
}

.tableformat {
	table-layout: fixed;
}

th {
	background: #DFDFDF; /* Darken header a bit */
	font-weight: bold;
}

td {
	background: #FAFAFA;
	text-align: left;
	word-wrap: break-word
}

/* Cells in even rows (2,4,6...) are one color */
tr:nth-child(even) td {
	background: #F1F1F1;
}

/* Cells in odd rows (1,3,5...) are another (excludes header cells)  */
tr:nth-child(odd) td {
	background: #FEFEFE;
}

tr td:hover {
	background: #666;
	color: #FFF;
} 

/* Hover cell effect! */
#previousButton {
	padding-left: 25px;
	border-collapse: collapse;
	color: #333;
}

#nextButton {
	padding-left: 600px;
	border-collapse: collapse;
	color: #333;
}
</style>

<body>
<%!
	String url;
	String fileName;
	String test;
	String filePath;
	List<DMSDocumentDetailsDTO> claimVerificationReportDocFileList;
	List<String> claimVerificationReportDocURLList;
%>
<% 
 	url = request.getParameter("fileViewUrl");
 	fileName = request.getParameter("fileName"); 
	filePath = (String)session.getAttribute("mergeDocumentsUrl");
	claimVerificationReportDocFileList = (List<DMSDocumentDetailsDTO>) session.getAttribute("claimVerificationReportDocFileList");
	claimVerificationReportDocURLList = (List<String>) session.getAttribute("claimVerificationReportDocURLList");
 %>

<div>
	<c:set var="fileName" scope="session" value="" />
	<c:set var="createdDate" scope="session" value="" />
	<c:set var="fileType" scope="session" value="" />
	<c:set var="fileViewUrl" scope="session" value="" />
	<c:set var="indexValue" scope="session" value="" />
	<c:set var="doctorRemark" scope="session" value="" />

	<c:forEach items="${param}" var="currentParam">
		<c:if test="${currentParam.key == 'fileViewUrl'}">
			<c:set var="fileViewUrl" value="${currentParam.value}" />
			<c:set var="url" value="${currentParam.value}" />
		</c:if>
		<c:if test="${currentParam.key == 'fileType'}">
			<c:set var="fileType" value="${currentParam.value}" />
		</c:if>
		<c:if test="${currentParam.key == 'createdDate'}">
			<c:set var="createdDate" value="${currentParam.value}" />
		</c:if>
		<c:if test="${currentParam.key == 'fileName'}">
			<c:set var="fileName" value="${currentParam.value}" />
		</c:if>
		<c:if test="${currentParam.key == 'indexValue'}">
			<c:set var="indexValue" value="${currentParam.value}" />
		</c:if>
		<c:if test="${currentParam.key == 'restapiurl'}">
			<c:set var="restapiurl" value="${currentParam.value}" />
		</c:if>
		<c:if test="${currentParam.key == 'doctorRemark'}">
			<c:set var="doctorRemark" value="${currentParam.value}" />
		</c:if>
	</c:forEach>
	<p>
		<strong> Document Details Table</strong>
	</p>
	<div></div>

	<table id="documentDetailsTable" class="tableformat">
		<tr>
			<td><strong>Intimation No</strong></td>
			<td colspan="3">${intimationNo}</td>
		</tr>
		<tr>
			<td><strong>Folder Name</strong></td>
			<td>CLAIM VERIFICATION REPORT</td>
			<td><strong>File Name</strong></td>
			<td cellspacing=1 id="fname">${fileName}</td>
		</tr>
		<tr>
			<td cellspacing=1><strong>Uploaded On</strong></td>
			<td cellspacing=1 id="dDate">${createdDate}</td>
			<td cellspacing=1><strong>File Type</strong></td>
			<td cellspacing=1 id="dfileType">${fileType}</td>
		</tr>
		<tr>
			<td cellspacing =1><strong>Doctors Remarks</strong></td>
			<td colspan="3" id="dremarks">${doctorRemark}</td>
		</tr>
	</table>
	<p id="p1" hidden="true">${fileViewUrl}</p>
	<p id="p2" hidden="true">${restapiurl}</p>
	<input type="hidden" id="text1" value="34088"></input>
</div>
<br>
<div id="viewFramee">
	<iframe id="viewFrame" src="" width="80%" height="80%"
		style="position: relative;"></iframe>
</div>
<div>
	<a onclick="getCategoryIndexForPrevious(${indexValue},'${fileType}')"
		href="#" id="previousButton">PREVIOUS</a> 
	<a onclick="getCategoryIndexForNext(${indexValue},'${fileType}')"
		href="#" id="nextButton">NEXT</a>
</div>

<script type="text/javascript">
 	var indexVal = ${indexValue};
 	
	function getCategoryIndexForNext(indexValue,fileType) {
		var myArray = <%=net.sf.json.JSONSerializer.toJSON(claimVerificationReportDocURLList) %>;
		var fileList = <%=net.sf.json.JSONSerializer.toJSON(claimVerificationReportDocFileList) %>;
			 	
	 	var iSize = myArray.length;
		if(indexVal >= iSize-1) {
			alert("File list limit exceded. Cannot proceed further");
		} else {
			++indexVal;
	
			$.ajax({
				type : "POST",
				crossDomain : true ,
				contentType: "application/json; charset=utf-8",
				url : $("#p2").text() + myArray[indexVal],
			
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
				}
			});
		
			var fileVal = fileList[indexVal];
			document.getElementById("fname").innerHTML = fileVal["fileName"];
			document.getElementById("dDate").innerHTML = fileVal["documentCreatedDateValue"];
			document.getElementById("dfileType").innerHTML = fileVal["documentType"];
			document.getElementById("dremarks").innerHTML = fileVal["deductionRemarks"];
		}
	}
	
	function getCategoryIndexForPrevious(indexValue,fileType) {
		var myArray = <%=net.sf.json.JSONSerializer.toJSON(claimVerificationReportDocURLList) %>;
		var fileList = <%=net.sf.json.JSONSerializer.toJSON(claimVerificationReportDocFileList) %>;
		
		var iSize = myArray.length;
		if(indexVal <= 0) {
			alert("File list limit exceded. Cannot proceed further");
		} else {
			--indexVal;
		
			$.ajax({
				type : "POST",
				crossDomain : true ,
				contentType: "application/json; charset=utf-8",
				url : $("#p2").text() + myArray[indexVal],
			
				beforeSend: function(x) {
			  		if(x && x.overrideMimeType) {
				   		x.overrideMimeType("application/json; charset=utf-8");
				  	}
			 	},
				dataType : "json",
				error: function (jqXHR, textStatus, errorThrown) {
			    	$("#viewFrame").attr("src" , jqXHR.responseText);
				}, 
				success : function(result) {
					console.log(result);  
				}
			});
			
			var fileVal = fileList[indexVal];
			document.getElementById("fname").innerHTML = fileVal["fileName"];
			document.getElementById("dDate").innerHTML = fileVal["documentCreatedDateValue"];
			document.getElementById("dfileType").innerHTML = fileVal["documentType"];
			document.getElementById("dremarks").innerHTML = fileVal["deductionRemarks"];
		}
	}
</script>
</body>
</html>
