<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Claim Acknowledgement Status</title>
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

.billclassificationchkbox {
	display: inline-block;
	padding: 10pt;
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

<%!
	String filePath;
	String queryFilepath;
 %>
<%
	session.setAttribute("intimationNumber", request.getAttribute("intimationNumber"));
	session.setAttribute("daignosisName", request.getAttribute("daignosisName"));
	session.setAttribute("preauth", request.getAttribute("preauth"));
	session.setAttribute("claim", request.getAttribute("claim"));
	session.setAttribute("reimbursementList", request.getAttribute("reimbursementList"));
	session.setAttribute("intimation", request.getAttribute("intimation"));
	session.setAttribute("hospitals", request.getAttribute("hospitals"));
	session.setAttribute("policyList", request.getAttribute("policyList"));
	session.setAttribute("rejectList", request.getAttribute("rejectList"));
	session.setAttribute("dmsUrl", request.getAttribute("dmsUrl"));
 %>

<div class="container">

<div class="page-header"> 
    <!-- <h4 class="panel-heading text-center">Status of Intimation No : ${intimation.getIntimationId()}</h4>
       <a href= "${dmsUrl}" target= "_blank" style="padding-right:15px">View Documents</a> --->
       <h4 class="panel-heading text-center">View Acknowledgement Details</h4>
</div>
	<div class="row">
                <div class="col-xs-12 col-md-12 col-lg-12 pull-left" >
                    <div class="panel panel-default height">
                        <div class="panel-heading"><b></b></div>
                        <div class="panel-body panel-default table-responsive">

						<table class="table table-condensed">
								<tbody>
		
									<tr>
										<td><strong>Acknowledgement No</strong></td>
										<td style="width:50%">${docAckDetails.getAcknowledgementNumber()}</td>
										<td ><strong>Acknowledgement Created By-ID</strong></td>
										<td>${docAckDetails.getAcknowledgmentCreatedId()}</td>
									</tr>
									<tr>
										
									</tr>
									<tr>
										<td ><strong>Acknowledgement Created On</strong></td>
										<td>${docAckDetails.getAcknowledgmentCreateOn()} </td>
										<td ><strong>Acknowledgement Created By-Name</strong></td>
										<td>${docAckDetails.getAcknowledgmentCreatedName()} </td>
									</tr>
									<tr>
										
									</tr>
									<tr>
										<td ><strong>Docuemnt Received From</strong></td>
										<td>${docAckDetails.getDocumentReceivedFromValue()} </td>
										<td ><strong>Email ID</strong></td>
										<td>${docAckDetails.getEmailId()} </td>
									</tr>
									<tr>
										
									<tr>
										<td ><strong>Document Received Date</strong></td>
										<td>${docAckDetails.getDocumentsReceivedDate()} </td>
										<td ><strong>Acknowledgement Contact Number</strong></td>
										<td>${docAckDetails.getAcknowledgmentContactNumber()} </td>
									</tr>
									<tr>
										<td ><strong>Mode of Receipt</strong></td>
										<td>${docAckDetails.getModeOfReceiptValue()} </td>
										<td></td>
										<td></td>
										<!-- <td ><strong>Amount Claimed(Hospitalization)</strong></td>
										<td>${docAckDetails.getHospitalizationClaimedAmount()} </td> -->
									</tr>
									<tr>
										<td ><strong>Reconsideration Request</strong></td>
										<td>${docAckDetails.getReconsiderationRequestValue()} </td>
										<td></td>
										<td></td>
										<!-- <td ><strong>Amount Claimed(Pre-Hosp)</strong></td>
										<td>${docAckDetails.getPreHospitalizationClaimedAmount()} </td> -->
									</tr>
									<!-- <tr>
										<td></td>
										<td></td>
										 <td><strong>Amount Claimed(Post-Hosp)</strong></td>
										<td>${docAckDetails.getPostHospitalizationClaimedAmount()} </td> 
									</tr> -->
								</tbody>
						</table>
						</div>
					</div>
				</div>
					<c:if test="${reconsiderRODList.getRodNo() != null}">
					<div class="row">
				        <div class="col-md-12">
				            <div class="panel panel-default">
				                <div class="panel-heading">
				                    
				                </div>
				                <div class="panel-body">
				                    <div class="table-responsive">
					<table class="table table-condensed table-bordered table-hover">
					 <thead>
						
						<tr><td ><strong>ROD No</strong></td><td ><strong>Bill Classification </strong></td><td ><strong>Claimed Amount</strong></td>
						<td ><strong>Approved Amount</strong></td><td ><strong>Rod Status</strong></td><td ><strong>View Claim Status</strong></td></tr>
						</thead>
						<tbody>
						<c:forEach items="${reconsiderRODList}" var="reimbursement">
						
						<tr><td>${reimbursement.getRodNo()}</td><td>${reimbursement.getBillClassification()}</td><td >${reimbursement.getClaimedAmt()}</td><td >${reimbursement.getApprovedAmt()}</td>
						<td >${reimbursement.getRodStatus()}</td><td ></td>
						</tr>
						</c:forEach>
						</tbody>
							</table>
							</div>
				                </div>
				            </div>
				            
				        </div>
    			</div>
    			</c:if>
    			
				
				
				<div class="row">
				      <div class="col-md-12">
				          <div class="panel panel-default">
				               <div class="panel-heading">
				                    <p class="text-left"><strong>Bill Classification</strong></p>
				                </div>
				                <div class="panel-body">
				                    <div class="table-responsive">
										<table class="table table-condensed table-bordered table-hover">
										 <thead>
											<div class="billclassificationchkbox">
												<c:if test="${docAckDetails.getHospitalizationFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Hospitalization
												</c:if>
												<c:if test="${docAckDetails.getHospitalizationFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Hospitalization
												</c:if>
												
											</div>
											 <div class="billclassificationchkbox">
											<c:if test="${docAckDetails.getPreHospitalizationFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Pre-Hospitalization
												</c:if>
												<c:if test="${docAckDetails.getPreHospitalizationFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Pre-Hospitalization
												</c:if>
												
											</div>
											<div class="billclassificationchkbox">
											
												<c:if test="${docAckDetails.getPostHospitalizationFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Post-Hospitalization
												</c:if>
												<c:if test="${docAckDetails.getPostHospitalizationFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Post-Hospitalization
												</c:if>
												
											</div>
											<div class="billclassificationchkbox">
											
												<c:if test="${docAckDetails.getPartialHospitalizationFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Partial Hospitalization
												</c:if>
												<c:if test="${docAckDetails.getPartialHospitalizationFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Partial Hospitalization
												</c:if>
												
											</div> <br><br>
											<div class="billclassificationchkbox">
												
												<c:if test="${docAckDetails.getLumpSumAmountFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Lumpsump Amount
												</c:if>
												<c:if test="${docAckDetails.getLumpSumAmountFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Lumpsump Amount
												</c:if>
												
											</div>
											<div class="billclassificationchkbox">
												
												<c:if test="${docAckDetails.getHospitalExpensesCoverFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Add On Benefits(Hospital Cash)
												</c:if>
												<c:if test="${docAckDetails.getHospitalExpensesCoverFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Add On Benefits(Hospital Cash)
												</c:if>
												
											</div>
											<div class="billclassificationchkbox">
											
												<c:if test="${docAckDetails.getHospitalExpensesCoverFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Add On Benefits(Patient Care)
												</c:if>
												<c:if test="${docAckDetails.getHospitalExpensesCoverFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Add On Benefits(Patient Care)
												</c:if>
												
											</div>
											<div class="billclassificationchkbox">
												
												<c:if test="${docAckDetails.getHospitalizationRepeatFlag() == 'Y'}">
													<input type="checkbox" checked disabled readonly>Hospitalization (Repeat)
												</c:if>
												<c:if test="${docAckDetails.getHospitalizationRepeatFlag() == 'N'}">
													<input type="checkbox" disabled readonly>Hospitalization (Repeat)
												</c:if>
												
											</div>
										 </thead>
											
										</table>
									</div>
				                </div>
				            </div>
				            
				        </div>
    			</div>
    			
				
				<div class="row">
				        <div class="col-md-12">
				            <div class="panel panel-default">
				                <div class="panel-heading">
				                    <p class="text-left"><strong>View Query Details</strong></p>
				                </div>
				                <div class="panel-body">
				                    <div class="table-responsive">
					<table class="table table-condensed table-bordered table-hover">
					 <thead>
						
						<tr><td ><strong>ROD No</strong></td><td ><strong>Bill Classification </strong></td><td ><strong>Diagnosis</strong></td><td ><strong>Claimed Amount</strong></td><td ><strong>Query Raised Role</strong></td><td ><strong>Query Raised Date</strong></td>
						<td ><strong>Query Status</strong></td><td ><strong>Query Reply Status</strong></td><td ><strong>View Status</strong></td></tr>
						</thead>
						<tbody>
						<c:forEach items="${ackdQueryDetails}" var="reimbursement">
						
						<tr><td>${reimbursement.getRodNo()}</td><td>${reimbursement.getBillClassification()}</td><td >${reimbursement.getDiagnosis()}</td><td >${reimbursement.getClaimedAmount()}</td><td >${reimbursement.getQueryRaisedRole()}</td><td >${reimbursement.getQueryRaisedDate()}</td><td >${reimbursement.getQueryStatus()}</td>
						<td >${reimbursement.getQueryReplyStatus()}</td><td >${reimbursement.getQueryStatus()}</td>
						</tr>
						</c:forEach>
						</tbody>
							</table>
							</div>
				                </div>
				            </div>
				            
				        </div>
    			</div>
    			
    			<div class="row">
			        <div class="col-md-12">
			           <div class="panel panel-default">
			             <div class="panel-heading">
				             <p class="text-left"><strong>Document Checklist</strong></p>
			             </div>
		                <div class="panel-body">
			            <div class="table-responsive">
								<table class="table table-condensed table-bordered table-hover">
									 <thead>
										<tr><td ><strong>Particulars</strong></td><td ><strong>Received Status </strong></td><td ><strong>No of Documents</strong></td><td ><strong>Remarks</strong></td></tr>
									 </thead>
									<tbody>
										<c:forEach items="${ackDocList}" var="reimbursement">
								
											<tr><td>${reimbursement.getValue()}</td><td>${reimbursement.getReceivedStatus().getValue()}</td><td >${reimbursement.getNoOfDocuments()}</td><td >${reimbursement.getRemarks()}</td>
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
	</div>
	
	<input type="text" style="border: none;visibility: hidden;" id="getIntimationCreateDate" value="${intimation.getCreatedDate()}">
	<input type="text" style="border: none;visibility: hidden;" id="getAdmissionDate" value="${intimation.getAdmissionDate()}">	
	<input type="text" style="border: none;visibility: hidden;" id="getRejectedDate" value="${rejectList.getAdmissionDate()}">
</body>

<script type="text/javascript">
		$(document).ready(function(){
				
				var ict = String(document.getElementById('getIntimationCreateDate').value);
				document.getElementById('setIntimationCreateDate').innerHTML = ict.charAt(8)+""+ict.charAt(9)+"/"+ict.charAt(5)+""+ict.charAt(6)+"/"+ict.charAt(0)+""+ict.charAt(1)+""+ict.charAt(2)+""+ict.charAt(3)+" "+ict.charAt(11)+""+ict.charAt(12)+":"+ict.charAt(14)+""+ict.charAt(15)+":"+ict.charAt(17)+""+ict.charAt(18);
    			
    			var adt = String(document.getElementById('getAdmissionDate').value);
    				document.getElementById('setAdmissionDate').innerHTML = adt.charAt(8)+""+adt.charAt(9)+"/"+adt.charAt(5)+""+adt.charAt(6)+"/"+adt.charAt(0)+""+adt.charAt(1)+""+adt.charAt(2)+""+adt.charAt(3)+" "+adt.charAt(11)+""+adt.charAt(12)+":"+adt.charAt(14)+""+adt.charAt(15)+":"+adt.charAt(17)+""+adt.charAt(18);
    			var rdt = String(document.getElementById('getRejectedDate').value);
    				document.getElementById('setRejectedDate').innerHTML = rdt.charAt(8)+""+rdt.charAt(9)+"/"+rdt.charAt(5)+""+rdt.charAt(6)+"/"+rdt.charAt(0)+""+rdt.charAt(1)+""+rdt.charAt(2)+""+rdt.charAt(3)+" "+rdt.charAt(11)+""+rdt.charAt(12)+":"+rdt.charAt(14)+""+rdt.charAt(15)+":"+rdt.charAt(17)+""+rdt.charAt(18);
		});
		
		
	</script>
</html>
