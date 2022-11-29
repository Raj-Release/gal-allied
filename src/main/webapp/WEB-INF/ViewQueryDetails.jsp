<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Claim Query Status</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- <link rel="stylesheet"  href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" />
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css"> 
    
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap-theme.min.css"> -->
	
	<link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.css" >
    <link rel="stylesheet" href="css/bootstrap-theme.min.css">

    <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
    <style>
.height {
    min-height: 200px;
}

.icon {
    font-size: 47px;
    color: #5CB85C;
}

.iconbig {
    font-size: 77px;
    color: #5CB85C;
}

.col-xs-12 col-md-12 col-lg-12 pull-left {
	align-content: spacebetween;
}
.col-xs-12 col-md-12 col-lg-12 {
	padding-left: 30px;
}
.panel panel-default height{
	padding-right: 30px;

}

.table.table.table-condensed {
	border-collapse: collapse;
}

.table > tbody > tr > .emptyrow {
    border-top: none;
}

.table > thead > tr > .emptyrow {
    border-bottom: none;
}

.table > tbody > tr > .highrow {
    border-top: 3px solid;
}

.col-lg-1, .col-lg-10, .col-lg-11, .col-lg-12, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-md-1, .col-md-10, .col-md-11, .col-md-12, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9 {
	position: static !important;
    min-height: 1px;
    padding-right: 15px;
    padding-left: 15px;
}

</style>

		
</head>
<body>
<%
	session.setAttribute("intimationNumber", request.getAttribute("intimationNumber"));
	session.setAttribute("daignosisName", request.getAttribute("daignosisName"));
	session.setAttribute("preauth", request.getAttribute("preauth"));
	session.setAttribute("claim", request.getAttribute("claim"));
	session.setAttribute("reimbursementList", request.getAttribute("reimbursementList"));
	session.setAttribute("intimation", request.getAttribute("intimation"));
	session.setAttribute("hospitals", request.getAttribute("hospitals"));
	session.setAttribute("dmsUrl", request.getAttribute("dmsUrl"));
	session.setAttribute("policyList", request.getAttribute("policyList"));
	session.setAttribute("rejectList", request.getAttribute("rejectList"));
	session.setAttribute("queryList", request.getAttribute("queryList"));
	session.setAttribute("viewtrailsList", request.getAttribute("viewtrailsList"));
 %>
	<div class="container">
			<div class="page-header"> 
			    <!-- <h4 class="panel-heading text-center">Status of Intimation No : ${intimation.getIntimationId()}</h4>
			       <a href= "${dmsUrl}" target= "_blank" style="padding-right:15px">View Documents</a> --->
			       <h4 class="panel-heading text-center">View Query Details</h4>
			</div>
			<div class="row">
        		<div class="col-md-12">
            		<div class="panel panel-default">
                		<div class="panel-heading">
                    		<p class = "text-left"><strong>View Query Details</strong></p>
               			 </div>
                		<div class="panel-body">
                    		<div class="table-responsive">
								<table class="table table-condensed table-bordered table-hover">
								<thead>
									<tr><td><strong>Acknowledge No</strong></td><td ><strong>ROD No</strong></td><td ><strong>Diagnosis</strong></td><td ><strong>Query Remarks</strong></td>
									<td ><strong>Query Raised Role</strong></td><td ><strong>Designation</strong></td><td ><strong>Query Raised Date</strong></td><td ><strong>Query Status</strong></td><td ></td></tr>
								</thead>
								<tbody>
									<c:forEach items="${queryList}" var="query">
									
									<tr><td>${query.getAcknowledgementNo()}</td><td>${query.getRodNumber()}</td><td>${query.getDiagnosis()}</td><td>${query.getQueryRemarks()}</td>
									<td>${query.getQueryRaised()}</td><td>${query.getDesignation()}</td>
									 <td>${query.getQueryDate()}</td>
									<td>${query.getQueryStatus()}</td>
									<%-- <td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&reim_query=${query.getReimbursementKey()}" target="_blank">View Query Details</a></form></td> --%>
									<td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?queryDetails=${query.getQueryToken()}" target="_blank">View Query Details</a></form></td>
									</tr>
									
							
									</c:forEach>
								</tbody>
								</table>	
							</div>
                		</div>
            		</div>
       		 	</div>
   		 	</div>
		</div>
					<input type="text" style="border: none;visibility: hidden;" id="getQueryDate" value="${query.getQueryRaisedDateStr()}">
					
	</body>
					<script type="text/javascript">
						$(document).ready(function(){
								
								var ict = String(document.getElementById('getQueryDate').value);
								if(ict != "")
								document.getElementById('setQueryDate').innerHTML = ict.charAt(8)+""+ict.charAt(9)+"/"+ict.charAt(5)+""+ict.charAt(6)+"/"+ict.charAt(0)+""+ict.charAt(1)+""+ict.charAt(2)+""+ict.charAt(3)+" "+ict.charAt(11)+""+ict.charAt(12)+":"+ict.charAt(14)+""+ict.charAt(15)+":"+ict.charAt(17)+""+ict.charAt(18);
				    			
				    			
				    		
						});
		
		
	</script>
</html>