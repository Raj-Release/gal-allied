<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
<%@page import="org.apache.http.HttpRequest"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
   <head>
      <meta charset="utf-8" />
      <title>Claim status</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
	 
	  <link rel="stylesheet" href="css/bootstrap.min.css">
      <link rel="stylesheet" href="css/font-awesome.css" >
      <link rel="stylesheet" href="css/bootstrap-theme.min.css">
      <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
      <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
      <script type="text/javascript" src="https://code.jquery.com/jquery-2.0.2.js"></script>
      <script type="text/javascript" src="https://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
      <link rel="stylesheet" type="text/css" href="https://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
	  
     <!-- <link rel="stylesheet" href="css/bootstrap.min.css">
      <link rel="stylesheet" href="css/font-awesome.css" >
      <link rel="stylesheet" href="css/bootstrap-theme.min.css">
      <script type="text/javascript" src="js/jquery-2.0.2.js"></script>
      <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>   
      <script type="text/javascript" src="js/jquery-ui.js"></script>
      <link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
	   <script type="text/javascript" src="js/textlayerbuilder.js"></script>
	  <script type="text/javascript" src="js/pdf.js"></script>-->
      <style>
			.height {
				min-height: 200px;
			}
			 .icon {
				 font-size: 47px;
				 color: #5CB85C;
			}
			canvas {
				width: 100% !important;
			}
			 .iconbig {
				 font-size: 77px;
				 color: #5CB85C;
			}
			.custom-panel {
				width: 1300px !important;
			}
			 .col-xs-12 col-md-12 col-lg-12 pull-left {
				 align-content: spacebetween;
			}
			 .col-md-13 {
				 width: 1250px 
			}
			 .col-xs-12 col-md-12 col-lg-12 {
				 padding-left: 30px;
			}
			 .panel panel-default height{
				 padding-right: 30px;
			}
			 .panel panel-default panel-default-left{
				 width: 1500px;
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
			 .ui-dialog-buttonset{
				 float:ri;
			}
			
			.overlay {
				opacity: 0.5;
				filter: alpha(opacity=5);
				width: 100%;
				height: 100%;
				z-index: 10;
				top: 0;
				left: 0;
				position: fixed;
				background: url(VAADIN/themes/mytheme/images/151.GIF) center no-repeat #fff;
				background-position: 50% 75%;
				background-size : 3%;
			}
								
			span.sub-heading.preauth-sublimit-head {
				padding-left: 35%;
			}
			
			table.riskDetailsTable {
				margin-left: 11%;
				width: 75%;
			}
			
			table.riskDetailsTable td {
				padding-top: 2% !important;
				padding-right: 3% !important;
			}
			
			table.sumInsuredTable td {
				padding-top: 1% !important;
			}
			
			input.preauthTitleText {
				width: 30%;
			}
			 .sub-heading{
				 font-size: 10px;
				 color:#696969;
				 font-weight: bold;
				 padding-left: 2px;
			}
			table.preauth-details span{
				 font-size: 11px !important; 			
			}
			table.preauth-details {
				 font-size: 11px !important; 			
			}
			
			.preauth-total-amount label {
				font-size: 12px;
				width: 28%;
				margin-left: 5%;
			}
			table.ackQryDetailsTable {
				width:70% !important;
			}
			table.ackQryDetailsTable td {
				padding-top : 1% !important;
			}								
			button.ackViewQryLetter {
				float: right;
				margin-right: 25%;
			}
			 table.fvr-details {
				 margin-top : 3%;
				 margin-bottom : 4%;
			}
			 table.fvr-details tr {
				 padding-bottom : 7%;
			}
			 table.fvr-details td {
				 padding: 0px 12px 12px;
			}
			 table.fvr-details span{
				 font-size: 14px;
				 font-weight: bold;
				 color: #484848;
			}
			 table.fvr-details input{
				 margin : auto;
				 background-color: white;
			}
			 table.fvr-details label{
				 font-size : small;
			}
			
			table.fvr-sub-table.preauthamt thead{
				background-color : #003366 !important;
				color : white;
			}
			
			table.fvr-sub-table.auth-balance thead {
				background-color : #003366 !important;
				color : white;
			}
			
			 table.fvr-sub-table {
				 width: 100%;
				 margin-top: 1%;
				 margin-bottom: 3%;
			}
			
			table.preauth-balance {
				margin-left : 2px;
				
			}
			
			.preauth-amount {
				float: left;
				margin-right: 2%;
				margin-top: 0%;
				width: 30%
			}
			
			.preauth-balance {
				float: left;
				margin-right: 6%;
				width: 30%
			}
			
			.preauth-amt-sub {
				float: left;
				width: 30%;
				margin-right: -28% !important;
			}
			
			.hreflink {
				text-decoration: underline;
				color: #428bca !important;
			}
		
			 .fvr-sub-table td, .fvr-sub-table th {
				 border: 1px solid #ddd;
				 padding: 8px;
				 font-size: 11px;
			}
			 .fvr-sub-table th {
				 background-color:#003366 ;
				 color: #ffffff;
				 text-align: center;
			}
			/* Modal Content */
			 .custom-modal-content {
				 background-color: #fefefe;
				 margin: auto;
				 padding: 20px;
				 border: 1px solid #888;
				 width: 80%;
			}
			/* Modal Content */
			 .custom-modal {
				 left: 0;
				 top: 0;
				 width: 100%;
				/* Full width */
				 height: 100%;
				/* Full height */
				 overflow: auto;
				/* Enable scroll if needed */
				 background-color: rgb(0,0,0);
				 padding-top:6%;
				/* Fallback color */
			}
			 .scroll {
				 overflow: scroll;
			}
			 .ellipsis {
				 text-overflow: ellipsis;
				/* Required for text-overflow to do anything */
				 white-space: nowrap;
				 overflow: hidden;
			}
			
			.preauth-title {
				font-size: 13px !important;
				margin: 1%;
			}
			
			.ped-panel {
				 width: 100%;
				 background: -webkit-linear-gradient(#FAFAFA, #F38484); /* Chrome, firefox */
				 background: -ms-linear-gradient(#FAFAFA, #F38484);  /* IE */
				 height: 23px;
				 text-align: left;
				 padding-top: 6px;
				 font-weight: bold;
				 font-size: 13px;
				 padding-left: 15px;
			}
			
			.ped-panel-content {
				 background-color: #ddf6fc;
				 width: 100%;
			}
			 .ped-panel.ped-panel-empty {
				 background: white;
			}
			 .ped-table-body tr {
				 line-height: 25px;
			}
			 .fvrFrame{
				 width : 100%;
				 height : 50%;
			}
			 .docFrame{
				 width : 100%;
				 height : 1000px;
			}
			.preselect{
				margin-left: 2%;
				width: auto;
				height: 23px;
				margin-bottom: 3%;
				margin-right: 12%;
			}
			.prevalue{
               padding-left: 100px !important;
               width:auto;
            }
			.fvrSegB input[type="radio"] {
				margin-left: 5%;
			}
			
			.fvrSegB input[type="radio"]:checked:before { {
				background: #8cbef0;
			}
			
			.scrollit {
				overflow:scroll;
				height:100px;
			}
			
			.ped-ammended-div {
				background-color : white !important;
			}
			
			.preauth-values{
				font-weight:normal;
				 font-size: 11px !important; 
			}
			
			table.preauth-details td {
				padding: 0px 12px 12px 12px;
			}
			
						
		    .resizer {
				position: absolute;
				top: 0;
				right: -8px;
				bottom: 0;
				left: auto;
				width: 16px;    
				cursor: col-resize;
			}
			
			.preauth-sub table {
				float : left;
			}
			
			input.preauthTitleText {
				margin-left: 7% !important;
				width: 34%;
			}
			
			.preauth-speciality {
				text-align: center;
				height: 250px;
				margin-top: 18px;
			}
			
			.preauth-speciality, .preauth-pedvalidation, .preauth-exclusion {
				height:200px;
			}
			
			.preauth-modal table > tr:nth-child(odd) {
					background-color: #c2cbce;
			}
			
			.PA_sum_insured_tables table > tr:nth-child(odd) {
					background-color: #c2cbce;
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
         session.setAttribute("dmsUrl", request.getAttribute("dmsUrl"));
         session.setAttribute("policyList", request.getAttribute("policyList"));
         session.setAttribute("rejectList", request.getAttribute("rejectList"));
         session.setAttribute("queryList", request.getAttribute("queryList"));
         session.setAttribute("viewtrailsList", request.getAttribute("viewtrailsList"));
         session.setAttribute("crcQueryList", request.getAttribute("crcQueryList"));
         session.setAttribute("policyUrl", request.getAttribute("policyUrl"));
         session.setAttribute("packageUrl", request.getAttribute("packageUrl"));
         session.setAttribute("dmsViewUrl", request.getAttribute("dmsViewUrl"));
         session.setAttribute("doctorRemarks", request.getAttribute("doctorRemarks"));
         session.setAttribute("fvrDetails", request.getAttribute("fvrDetails"));
         session.setAttribute("portablityDetails", request.getAttribute("portablityDetails"));
         session.setAttribute("compulsaryCopayPercent", request.getAttribute("compulsaryCopayPercent"));
         session.setAttribute("voluntaryCopayPercent", request.getAttribute("voluntaryCopayPercent"));
         session.setAttribute("totalCopay", request.getAttribute("totalCopay"));
         session.setAttribute("pedRequestDetails", request.getAttribute("pedRequestDetails"));
		 session.setAttribute("preAuthIntimationKeys", request.getAttribute("preAuthIntimationKeys"));
		 session.setAttribute("preAuthDetails", request.getAttribute("preAuthDetails"));
         session.setAttribute("bonusLogicUrl", request.getAttribute("bonusLogicUrl"));
         
         %>
		<div class="container">
		<div class="overlay" style="display:none"></div> 
		   <div class="page-header">
			  <h4 class="panel-heading text-center">Status of Intimation No : ${intimation.getIntimationId()}</h4>
			  <label>
				 View Details 
				 <select id="selectbox" value="View Details">
					<option value="0">- Select -</option>
					<option value="intimation">Intimation</option>
					<option value="queryDetails">Query Details</option>
					<option value="policySchedule">View Policy Schedule</option>
					<option value="hospDetails">Hospital Details</option>
					<option value="copayDetails">Co-pay Details</option>
					<option value="fvrDetails">FVR Details</option>
					<option value="history">History</option>
					<option value="preauthDetails">Pre Auth Details</option> 
					<option value="doctorNote">Doctor Note (Trails)</option>
					<option value="pedRequest">PED Request Details</option>
					<!-- <option value="portability">Portability Policy</option> -->
					<option value="policyDocs">Policy Documents</option>
					<option value="ackDetails">Acknowledgment Details</option>
					<option value="coorReply">Co-ordinator Reply</option>
					<option value="specilaistTrail">Specialist Trail</option>
					<option value="claimDocs" data-url="${dmsUrl}">Claim Documents</option>
					<!-- <option value="64vbCompliance">64Vb Compliance</option>-->
					<option value="sublimits">Sublimits</option>
					<option value="hrmDetails">Hrm Details</option>
					<option value="riskDetails">Risk Details</option>
					<option value="previousInsurance">Previous Insurance Details</option>
					<option value="sumInsured">Balance Sum Insured</option>
					<option value="viewBonusLogic">View Bonus Logic</option>
					<option value="viewnegotiationdetails">Negotiation Details</option>
				 </select>
			  </label>
			  <input type="button" name="test" value="Go" id="gobutton">
			  <a href= "${dmsUrl}" target= "_blank" style="padding-right:15px">View Documents</a>
		   </div>
		   <div class="row">
			  <div class="col-xs-12 col-md-12 col-lg-12 pull-left" >
				 <div class="panel panel-default height">
					<div class="panel-heading"><b>Intimation Details</b></div>
					<div class="panel-body">
					   <table class="table table-condensed">
						  <tbody>
							 <tr>
								<td><strong>Intimation No </strong></td>
								<td style="width:50%">${intimation.getIntimationId()}</td>
								<td><strong>Policy No </strong></td>
								<td>${intimation.getPolicy().getPolicyNumber()} </td>
							 </tr>
							 <tr>
								<td><strong>Date & Time Of Intimation </strong></td>
								<td>
								   <p id="setIntimationCreateDate"></p>
								</td>
								<td><strong>Intimation Created By </strong></td>
								<td>${intimation.getCreatedBy()}</td>
							 </tr>
							 <tr>
								<td><strong>CPU Code </strong></td>
								<td>${intimation.getCpuCode().getCpuCode()}</td>
								<td><strong>Policy Issuing Office </strong></td>
								<td>${intimation.getPolicy().getHomeOfficeCode()}</td>
							 </tr>
							 <tr>
								<td><strong>Insured Patient Name </strong></td>
								<td>${intimation.getInsured().getInsuredName()} </td>
								<td><strong>Product Name </strong></td>
								<td>${intimation.getPolicy().getProduct().getValue()}</td>
							 </tr>
							 <tr>
								<td><strong>Health Card No </strong></td>
								<td>${intimation.getInsured().getHealthCardNumber()}</td>
								<td><strong>Proposer  Name </strong></td>
								<td>${intimation.getPolicy().getProposerFirstName()}</td>
							 </tr>
							 <tr>
								<td><strong>Admission Date </strong></td>
								<td>
								   <p id="setAdmissionDate"></p>
								</td>
								<td><strong>Hospital Name </strong></td>
								<td>${hospitals.getName()} </td>
							 </tr>
							 <tr>
								<td><strong>Reason of Admission </strong></td>
								<td>${intimation.getAdmissionReason()}</td>
								<td><strong>Address </strong></td>
								<td>${hospitals.getAddress()}</td>
							 </tr>
							 <tr>
								<td><strong>SM Code </strong></td>
								<td>${intimation.getPolicy().getSmCode()}</td>
								<td ><strong>Hospital Type </strong></td>
								<td>${hospitals.getHospitalType()}</td>
							 </tr>
							 <tr>
								<td><strong>SM Name </strong></td>
								<td>${intimation.getPolicy().getSmName()}</td>
								<td ><strong>Network Hospital Type</strong></td>
								<td>${ hospitals.getNetworkHospitalType()}</td>
							 </tr>
							 <tr>
								<td><strong>Agent/Broker Code </strong></td>
								<td> ${intimation.getPolicy().getAgentCode()}</td>
								<td><strong>Hospital Code(Internal) </strong></td>
								<td>${hospitals.getHospitalCode()}</td>
							 </tr>
							 <tr>
								<td><strong>Agent/Broker Name </strong></td>
								<td>${intimation.getPolicy().getAgentName()}</td>
								<td><strong>Hospital Code(IRDA) </strong></td>
								<td>${hospitals.getHospitalIrdaCode()}</td>
							 </tr>
						  </tbody>
					   </table>
					</div>
				 </div>
			  </div>
			  <div class="col-xs-12 col-md-12 col-lg-12">
				 <div class="panel panel-default height">
					<div class="panel-heading"><b>Registration Details</b></div>
					<div class="panel-body">
					   <table  class="table table-condensed">
						  <tbody>
							 <tr>
								<td><strong>Claim No </strong></td>
								<td>${claim.getClaimId()}</td>
								<td><strong>Provision Amount </strong></td>
								<td>${claim.getProvisionAmount()}</td>
							 </tr>
							 <tr>
								<td><strong>Registration Status </strong></td>
								<td>${claim.getRegistrationRemarks()}</td>
								<td><strong>Claim Type </strong></td>
								<td>${claim.getClaimType().getValue()}</td>
							 </tr>
							 <tr>
								<td></td>
								<td></td>
								<td><strong>Remarks </strong></td>
								<td>${claim.getRegistrationRemarks()}</td>
							 </tr>
						  </tbody>
					   </table>
					</div>
				 </div>
			  </div>
			  <div class="col-xs-6 col-md-6 col-lg-6">
				 <div class="panel panel-default height">
					<div class="panel-heading"><b>Cashless Details</b></div>
					<div class="panel-body">
					   <table  class="table table-condensed">
						  <tbody>
							 <tr>
								<td><strong>Diagnosis </strong></td>
								<td>${daignosisName}</td>
							 </tr>
							 <tr>
								<td ><strong>Status of Cashless </strong></td>
								<td>${preauth.getStatus().getProcessValue()}</td>
							 </tr>
							 <tr>
								<td><strong>Total Pre-Auth Approved Amount </strong></td>
								<td>${preauth.getTotalApprovalAmount()}</td>
							 </tr>
						  </tbody>
					   </table>
					</div>
				 </div>
			  </div>
			  <div class="row">
				 <div class="col-md-12">
					<div class="panel panel-default">
					   <div class="panel-heading">
						  <p class = "text-left"><strong>Preauth Summary</strong></p>
					   </div>
					   <div class="panel-body">
						  <div class="table-responsive">
							 <table class="table table-condensed table-bordered table-hover">
								<thead>
								   <tr>
									  <td ><strong>S.No</strong></td>
									  <td ><strong>Reference Number </strong></td>
									  <td ><strong>Reference Type</strong></td>
									  <td ><strong>Preauth Received Data & Time</strong></td>
									  <td ><strong>Pre-auth Response Date & Time</strong></td>
									  <td ><strong>Approved Amount</strong></td>
									  <td ><strong>Status</strong></td>
									  <td ><strong>Remarks</strong></td>
									  <td ><strong>Medical Remarks</strong></td>
								   </tr>
								</thead>
								<tbody>
								   <c:forEach items="${cashless}" var="cashless">
									  <tr>
										 <td >${cashless.getSno()}</td>
										 <td >${cashless.getReferenceNo()}</td>
										 <td>${cashless.getReferenceType()}</td>
										 <td >${cashless.getPreauthCreatedDate()}</td>
										 <td >${cashless.getPreauthModifiedDate()}</td>
										 <td>${cashless.getApprovedAmt()}</td>
										 <td>${cashless.getStatus()}</td>
										 <td>${cashless.getRemarks()}</td>
										 <td>${cashless.getMedicalRemarks()}</td>
										 <%-- 		 <td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&cashless_num=${cashless.getKey()}" target="_blank">View Details</a></form></td> 
											--%>		
									  </tr>
								   </c:forEach>
								   <tr>
									  <td ><strong></strong></td>
									  <td ><strong> </strong></td>
									  <td ><strong></strong></td>
									  <td ><strong>Total Amount</strong>
									  <td ><strong>${requestedAmt}</strong></td>
									  <td ><strong>${totalApprovedAmount}</strong></td>
									  <td ><strong> </strong></td>
								   </tr>
								</tbody>
							 </table>
						  </div>
					   </div>
					</div>
				 </div>
			  </div>
			  <div class="row">
				 <div class="col-md-12">
					<div class="col-md-13">
					   <div class="panel panel-default">
						  <div class="panel panel-default-left">
							 <div class="panel-heading">
								<p class="text-left"><strong>Receipt of Documents and Medical Processing</strong></p>
							 </div>
							 <div class="panel-body">
								<div class="table-responsive">
								   <table class="table table-condensed table-bordered table-hover">
									  <thead>
										 <tr>
										    <td ><strong>Flag</strong></td>
											<td ><strong>Type</strong></td>
											<td ><strong>ROD No</strong></td>
											<td ><strong>Document Received From </strong></td>
											<td ><strong>Document Received Date</strong></td>
											<td ><strong>Medical Response Date & time</strong></td>
											<td ><strong>Claimed Amount</strong></td>
											<td ><strong>Mode of Receipt</strong></td>
											<td ><strong>Bill Classification</strong></td>
											<td ><strong>Status</strong></td>
											<td ><strong>Remarks</strong></td>
											<td></td>
											<td></td>
											<td></td>
										 </tr>
									  </thead>
									  <tbody>
										 <c:forEach items="${reimbursementList}" var="reimbursement">
											<tr>
											
											<c:if test="${reimbursement.getReconsiderationRequestFlagged() == 'Y'}">
												<%--	<td><img src="../images/greenflag.png"></img></td> --%>
													<td><img title="${reimbursement.getReconsiderationRequestFlagRemarks()}" src="./images/greenflag.png"></img></td>
												</c:if>
												<c:if test="${reimbursement.getReconsiderationRequestFlagged() == 'N'}">
													<td><img src="./images/greyflag.png"></img></td>
													
												</c:if>
												<c:if test="${reimbursement.getReconsiderationRequestFlagged() == null}">
													<td></td>
												</c:if>
												
											   <td>${reimbursement.getTypeOfClaim()}</td>
											   <td>${reimbursement.getReimbursementNo()}</td>
											   <td>${reimbursement.getDocumentReceivedFrom()}</td>
											   <td >${reimbursement.getDocumentReceivedDate()}</td>
											   <td >${reimbursement.getMedicalResponseDate()}</td>
											   <td >${reimbursement.getAmount()}</td>
											   <td >${reimbursement.getModeOfRececipt()}</td>
											   <td >${reimbursement.getBillClassification()}</td>
											   <td >${reimbursement.getStatus()}</td>
											   <td >${reimbursement.getRemarks()}</td>
											   <%-- <td>
												  <form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&reimb_num=${reimbursement.getReimbursementKey()}" target="_blank">View Acknowledgement Details</a></form>
											   </td>
											   <td>
												  <form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&rod_num=${reimbursement.getReimbursementKey()}" target="_blank">View Rejection Details</a></form>
											   </td>
											   <td>
												  <form action = "ClaimStatusServlet" method="post"><a href="claimstatus?intim_num=${intimation.getIntimationId()}&rod_num=${reimbursement.getReimbursementKey()}" target="_blank">View Query Details</a></form>
											   </td> --%>
											   <td>
											   	<form action = "ClaimStatusServlet" method="post"><a href="claimstatus?acknowledgement=${reimbursement.getAcknowledgementToken()}" target="_blank">View Acknowledgement Details</a></form>
											   </td>
												<td>
													<form action = "ClaimStatusServlet" method="post"><a href="claimstatus?rejection=${reimbursement.getAcknowledgementToken()}" target="_blank">View Rejection Details</a></form>
												</td>
												<td>
													<form action = "ClaimStatusServlet" method="post"><a href="claimstatus?query=${reimbursement.getAcknowledgementToken()}" target="_blank">View Query Details</a></form>
												</td>
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
		   </div>
		   <div class="row">
			  <div class="col-md-12">
				 <div class="panel panel-default">
					<div class="panel-heading">
					   <p class = "text-left"><strong>Billing Processing</strong></p>
					</div>
					<div class="panel-body">
					   <div class="table-responsive">
						  <table class="table table-condensed table-bordered table-hover">
							 <thead>
								<tr>
								   <td ><strong>S.No</strong></td>
								   <td ><strong>ROD No </strong></td>
								   <td ><strong>Claim Type</strong></td>
								   <td><strong>Bill Classification</strong></td>
								   <td><strong>Doc Received From</strong></td>
								   <td><strong>ROD Type</strong></td>								  
								   <td ><strong>Billing Completed Date</strong></td>
								   <td ><strong>Bill Assessment Amount</strong></td>
								   <td ><strong>Status</strong></td>
								   <td></td>
								   <td></td>
								</tr>
							 </thead>
							 <tbody>
								<c:forEach items="${billing}" var="item">
								   <tr>
									  <td >${item.getSno()}</td>
									  <td >${item.getRodNumber()}</td>
									  <td >${item.getBillingType()}</td>
									  <td >${item.getBillClassification()}</td>
									  <td >${item.getDocReceivedFrom()}</td>
									  <td >${item.getRodType()}</td>									  
									  <td>${item.getBillingDate()}</td>
									  <td >${item.getBillAssessmentAmt()}</td>
									  <td >${item.getStatus()}</td>
									  <%-- <td>
										 <form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&bill_details=${item.getRodKey()}" target="_blank">View Bill Details</a></form>
									  </td>
									  <td>
										 <form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&bill_summary=${item.getRodKey()}" target="_blank">View Bill Summary</a></form>
									  </td> --%>
									  <td>
									  	<form action = "ClaimStatusServlet" method="post"><a href="claimstatus?billdetails=${item.getBillDtslToken()}" target="_blank">View Bill Details</a></form>
									  </td>
									  <td>
									  	<form action = "ClaimStatusServlet" method="post"><a href="claimstatus?billsummary=${item.getBillDtslToken()}" target="_blank">View Bill Summary</a></form>
									  </td> 
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
				 <div class="panel panel-default custom-panel">
					<div class="panel-heading">
					   <p class = "text-left"><strong>Financial Approval</strong></p>
					</div>
					<div class="panel-body">
					   <div class="table-responsive scroll">
						  <table class="table table-condensed table-bordered table-hover">
							 <thead>
								<tr>
								   <td ><strong>S.No</strong></td>
								   <td ><strong>ROD No </strong></td>
								   <td ><strong>Claim Type </strong></td>
								   <td><strong>Bill Classification</strong></td>
								   <td ><strong>Document Received From</strong></td>
								   <td><strong>ROD Type</strong></td>								  
								   <td ><strong>FA Date</strong></td>
								   <td ><strong>Amount</strong></td>
								   <td ><strong>Status</strong></td>
								   <td ><strong>Type Of Payment</strong></td>
								   <td ><strong>Cheque/Transaction No</strong></td>
								   <td ><strong>Cheque/Transaction-Date </strong></td>
								   <td ><strong>FA Remarks(Approval/Rejection/Query/Other)</strong></td>
								</tr>
							 </thead>
							 <tbody>
								<c:forEach items="${financial}" var="item">
								   <tr>
									  <td >${item.getSno()}</td>
									  <td >${item.getRodNumber()}</td>
									  <td >${item.getApprovalType()}</td>
									  <td >${item.getBillClassification()}</td>
									  <td >${item.getDocumentReceivedFrom()}</td>
									  <td >${item.getRodType()}</td>
									  <td>${item.getFaDate()}</td>
									  <td >${item.getAmount()}</td>
									  <td >${item.getStatus()}</td>
									  <td >${item.getPaymentType()}</td>
									  <td >${item.getTransactionNo()}</td>
									  <td >${item.getTransactionDate()}</td>
									  <td>${item.getFinancialRemarks()}</td>
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
				 <div class="col-md-13">
					<div class="panel panel-default custom-panel">
					   <div class="panel-heading">
						  <p class = "text-left"><strong>Payment Details</strong></p>
					   </div>
					   <div class="panel-body">
						  <div class="table-responsive scroll">
							 <table class="table table-condensed table-bordered table-hover">
								<thead>
								   <tr>
									  <td ><strong>S.No</strong></td>
									  <td ><strong>ROD No</strong></td>
									  <td ><strong>Claim Type</strong></td>
									  <td><strong>Bill Classification</strong></td>
									  <td ><strong>Document Received From</strong></td>
								      <td><strong>ROD Type</strong></td>
								      <td ><strong>Payee Name</strong>
									  <td ><strong>Payment Type</strong></td>
									  <td ><strong>Bank Name</strong></td>
									  <td ><strong>Account No</strong></td>
									  <td ><strong>IFSC Code</strong></td>
									  <td ><strong>Branch Name</strong></td>
									  <td ><strong>Cheque Date</strong></td>
									  <td ><strong>Cheque No</strong></td>
								   </tr>
								</thead>
								<tbody>
								   <c:forEach items="${payment}" var="payment">
									  <tr>
										 <td >${payment.getSerialNo()}</td>
										 <td >${payment.getRodNo()}</td>										 
										 <td>${payment.getClaimType()}</td>
										 <td>${payment.getBillClassification()}</td>
										 <td>${payment.getDocReceivedFrom()}</td>
										 <td>${payment.getRodType()}</td>
										 <td>${payment.getPayeeName()}</td>
										 <td >${payment.getPaymentType()}</td>
										 <td >${payment.getBankName()}</td>
										 <td >${payment.getAccountNumber()}</td>
										 <td >${payment.getIfscCode()}</td>
										 <td >${payment.getBranchName()}</td>
										 <td>${payment.getChequeDateValue()}</td>
										 <td>${payment.getChequeNo()}</td>
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
		   <!-- Intimation Modal -->
			<div id="intimation-dialog" class="modal intimation custom-modal">
			   <div class="ped-panel-content" style="padding: 2%;">
				  <div>
					 <table class="fvr-details preauth-details"  cellpadding="5" >
						<tbody>
						   <tr>
							  <td><span>Intimation No</span></td>
							  <td class="preauth-values" id="intNo"> </td>
							  <td class="prevalue"><span>Policy No</span></td>
							  <td class="preauth-values" id="intPolNo"></td>
						   </tr>
						   <tr>
							  <td><span>Date & Time of Intimation	</span></td>
							  <td class="preauth-values" id="intDate">	</td>
							  <td class="prevalue"><span>Policy Issuing Office</span></td>
							  <td class="preauth-values" id="intIssueOffice" > </td>
						   </tr>
						   <tr>
							  <td><span>CPU Code</span></td>
							  <td class="preauth-values" id="intCpuCode"> </td>
							  <td class="prevalue"><span>Product Name</span></td>
							  <td class="preauth-values" id="intProductName" >  </td>
						   </tr>
						   <tr>
							  <td><span>Mode of Intimation</span></td>
							  <td class="preauth-values" id="intMode"> </td>
							  <td class="prevalue"><span>Proposer Name </span></td>
							  <td class="preauth-values" id="intProposerName" >  </td>
						   </tr>
						   <tr>
							  <td><span>Intimated By</span></td>
							  <td class="preauth-values" id="intIntimatedBy">   </td>
							  <td class="prevalue"><span>State </span></td>
							  <td class="preauth-values" id="intState" >  </td>
						   </tr>
						   <tr>
							  <td><span>Insured Patient Name</span></td>
							  <td class="preauth-values" id="intPatientName">   </td>
							  <td class="prevalue"><span>City </span></td>
							  <td class="preauth-values" id="intCity" >  </td>
						   </tr>
						   <tr>
							  <td><span></span></td>
							  <td class="preauth-values" id="intIsCovered">   </td>
							  <td class="prevalue"><span>Area </span></td>
							  <td class="preauth-values" id="intArea" >  </td>
						   </tr>
						   <tr>
							  <td><span>Health Card No</span></td>
							  <td class="preauth-values" id="intHealthCardNo"> </td>
							  <td class="prevalue"><span>Hospital Name </span></td>
							  <td class="preauth-values" id="intHospName" >  </td>
						   </tr>
						   <tr>
							  <td><span>Name</span></td>
							  <td class="preauth-values" id="intName">   </td>
							  <td class="prevalue"><span>Address </span></td>
							  <td class="preauth-values" id="intAddress" >  </td>
						   </tr>
						   <tr>
							  <td><span>Relationship </span></td>
							  <td class="preauth-values" id="intRelation">   </td>
						   </tr>
						   <tr>
							  <td><span>Admission Date </span></td>
							  <td class="preauth-values" id="intAdDate">   </td>
						   </tr>
						   <tr>
							  <td><span>Admission Type </span></td>
							  <td class="preauth-values" id="intAdType">   </td>
						   </tr>
						   <tr>
							  <td><span>Inpatient Number</span></td>
							  <td class="preauth-values" id="intInpatient">   </td>
							  <td class="prevalue"><span>Hospital Type </span></td>
							  <td class="preauth-values" id="intHosptype" >  </td>
						   </tr>
						   <tr>
							  <td><span>Reason for late Intimation</span></td>
							  <td class="preauth-values" id="intLateIntimation"> </td>
							  <td class="prevalue"><span>Hospital Code (Internal) </span></td>
							  <td class="preauth-values" id="intHospCode" >  </td>
						   </tr>
						   <tr>
							  <td><span>Reason for Admission</span></td>
							  <td class="preauth-values" id="intAdReason">   </td>
							  <td class="prevalue"><span>Hospital Code (IRDA) </span></td>
							  <td class="preauth-values" id="intHospCodeIrda" >  </td>
						   </tr>
						   <tr>
							  <td><span></span></td>
							  <td class="preauth-values" id="">   </td>
							  <td class="prevalue"><span>Comments </span></td>
							  <td class="preauth-values" id="intComments" >  </td>
						   </tr>
						   <tr>
							  <td><span></span></td>
							  <td class="preauth-values" id="">   </td>
							  <td class="prevalue"><span>SM Code </span></td>
							  <td class="preauth-values" id="intSmCode" >  </td>
						   </tr>
						   <tr>
							  <td><span></span></td>
							  <td class="preauth-values" id="">   </td>
							  <td class="prevalue"><span>SM Name </span></td>
							  <td class="preauth-values" id="intSmName" >  </td>
						   </tr>
						   <tr>
							  <td><span></span></td>
							  <td class="preauth-values" id="">   </td>
							  <td class="prevalue"><span>Agent / Broker Code </span></td>
							  <td class="preauth-values" id="intBrokerCode" >  </td>
						   </tr>
						   <tr>
							  <td><span></span></td>
							  <td class="preauth-values" id="">   </td>
							  <td class="prevalue"><span>Agent / Broker Name </span></td>
							  <td class="preauth-values" id="intBrokerName" >  </td>
						   </tr>
						</tbody>
					 </table>
				  </div>
			   </div>
			</div>
			<div id="query_details_modal" class="modal query_details_modal custom-modal">				
				<div class="ped-panel-content" style="background-color: #ffffff !important; height:500px;">
					<div class="">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>		
								 <tr>
									<td><strong>S.No</strong></td>
									<td><strong>Hospital Name</strong></td>
									<td><strong>Hospital City</strong></td>
									<td><strong>Diagnosis</strong></td>
									<td><strong>Query Remarks</strong></td>
									<td><strong>Query Raised Role</strong></td>
									<td><strong>Designation</strong></td>
									<td><strong>Query Raised Date</strong></td>
									<td><strong>Query Status</strong></td>
								</tr>
							</thead>
							<tbody>
							 <c:forEach items="${crcQueryList}" var="query"  varStatus="count">
								<tr>
								   <td>${count.count}</td>
								   <td>${query.getHospitalName()}</td>
								   <td>${query.getHospitalCity()}</td>
								   <td>${query.getDiagnosis()}</td>
								   <td>${query.getQueryRemarks()}</td>
								   <td>${query.getQueryRaised()}</td>
								   <td>${query.getDesignation()}</td>
								   <td>${query.getQueryDate()}</td>
								   <td>${query.getQueryStatus()}</td>
								</tr>
							 </c:forEach>
						  </tbody>
					   </table>
					</div>
				</div>
			 </div>
		   
		   <div>
			  <input type="text" style="border: none;visibility: hidden;" id="getPolicyUrl" value="${policyUrl}">
		   </div>
		   
		    <div>
			  <input type="text" style="border: none;visibility: hidden;" id="getBonusLogicUrl" value="${bonusLogicUrl}">
		   </div>
		   
		   
		   
		  <!-- Hospital Details Dialog-->	
			<div id="hospitals-details-dialog" class="modal hospitals-details-dialog custom-modal">
			   <div class="ped-panel-content" style="background-color: #ffffff !important;">
				  <div>
					 <table class="fvr-details preauth-details riskDetailsTable" border="0" cellspacing="0" cellpadding="0">
						<tbody>
						   <tr>
							  <td><span>Hospital Name</span></td>
							  <td class="preauth-values" id="hosp_name"> </td>
							  <td class="prevalue"><span>Hospital Code</span></td>
							  <td class="preauth-values" id="hosp_code"></td>
						   </tr>
						   <tr>
							  <td><span>Hospital Ph No</span></td>
							  <td class="preauth-values" id="hosp_phone"></td>
							  <td class="prevalue"><span>Address</span></td>
							  <td class="preauth-values" id="hosp_address" > </td>
						   </tr>
						   <tr>
							  <td><span>Authorized Representative</span></td>
							  <td class="preauth-values" id="hosp_rep"> </td>
							  <td class="prevalue"><span>City</span></td>
							  <td class="preauth-values" id="hosp_city" >  </td>
						   </tr>
						   <tr>
							  <td><span>Name of Representative</span></td>
							  <td class="preauth-values" id="hosp_rep_name"> </td>
							  <td class="prevalue"><span>State</span></td>
							  <td class="preauth-values" id="hosp_state" >  </td>
						   </tr>
						   <tr>
							  <td><span>Hospital Category</span></td>
							  <td class="preauth-values" id="hosp_category"> </td>
							  <td class="prevalue"><span>PinCode</span></td>
							  <td class="preauth-values" id="hosp_pin" >  </td>
						   </tr>
						   <tr>
							  <td><span>Room Category</span></td>
							  <td class="preauth-values" id="hosp_room_category"> </td>
							  <td class="prevalue"></td>
							  <td class="preauth-values" id="hosp_package_url"></td>
						   </tr>
						   <tr>
							  <td><span>Remarks</span></td>
							  <td class="preauth-values" id="hosp_remarks"> </td>
							  <td class="prevalue"><span></span></td>
							  <td class="preauth-values">  </td>
						   </tr>
						</tbody>
					 </table>
				  </div>
			   </div>
			</div>
		   <!-- Co-pay Details Dialog-->
		   <div id="copay-dialog" class="modal copay-dialog custom-modal">
			   <div class="ped-panel-content" style="background-color: #ffffff !important;">
				  <div>
					 <table class="fvr-details preauth-details riskDetailsTable" border="0" cellspacing="0" cellpadding="0">
						<tbody>
						   <tr>
							  <td><span>Percentage</span></td>
							  <td class="preauth-values"> </td>
						   </tr>
						   <tr>
							  <td><span>Compulsory Co-Pay</span></td>
							  <td class="preauth-values">${compulsaryCopayPercent}</td>
						   </tr>
						   <tr>
							  <td><span>Voluntary Co-Pay</span></td>
							  <td class="preauth-values">${voluntaryCopayPercent}</td>
						   </tr>
						</tbody>
					 </table>
				  </div>
			   </div>
			</div>
		   <!-- History Details Dialog-->
		   <div id="claim_history_modal" class="modal claim_history_modal custom-modal">				
				<div class="ped-panel-content" style="background-color: #ffffff !important; height:500px;">
					<div class="">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>		
								 <tr>
									<td><strong>Type of Claim</strong></td>
									<td><strong>Doc Received From</strong></td>
									<td><strong>ROD Type</strong></td>
									<td><strong>Bill Classification</strong></td>
									<td><strong>Reference No/ROD No</strong></td>
									<td><strong>Date & Time</strong></td>
									<td><strong>UserID</strong></td>
									<td><strong>UserName</strong></td>
									<td><strong>Claim Stage</strong></td>
									<td><strong>Status</strong></td>
									<td><strong>User Remarks</strong></td>									
								 </tr>
							  </thead>
						  <tbody class="claim_history_tbody">
							
						  </tbody>
					   </table>
					</div>
				</div>
			 </div>
		   
		   
		   <!-- FVR Details Modal -->
			<div id="fvr-detail-modal" class="modal fvr-detail-modal custom-modal">				
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div class="scroll">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>
							 <tr>
								<td><strong>S.No</strong></td>
								<td ><strong>Representative Name</strong></td>
								<td ><strong>Representative Code</strong></td>
								<td ><strong>Representative ContactNo</strong></td>
								<td ><strong>Hospital Name</strong></td>
								<td ><strong>Hospital Visited Date</strong></td>
								<td ><strong>Remarks</strong></td>
								<td ><strong>FVR Assigned Date</strong></td>
								<td ><strong>FVR Received Date</strong></td>
								<td ><strong>FVR TAT</strong></td>
								<td ><strong>FVR STATUS</strong></td>
								<td ><strong>FVR GRADING</strong></td>
								<td></td>
								<td></td>
							 </tr>
						  </thead>
							<tbody class="fvr-details-tbody">
							</tbody>
						</table>
					</div>					
				</div>
			</div>
		   
		    <!-- Doctor Trails Modal -->
			<div id="doctor-trails-modal" class="modal doctor-trails-modal custom-modal">				
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div class="scroll">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>
							 <tr>
								<td><strong>Date / Time</strong></td>
								<td><strong>User ID</strong></td>
								<td><strong>Transaction</strong></td>
								<td><strong>Transaction Type</strong></td>
								<td><strong>Remarks</strong></td>
							 </tr>
						  </thead>
							<tbody class="doctor-trails-tbody">
							</tbody>
						</table>
					</div>					
				</div>
			</div>		   
  
		   <!-- PED Request Details Modal -->
		  <div id="PED-request-dialog" class="modal PED-request-dialog custom-modal">				
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div class="">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>
							 <tr>
								<td><strong>S.No</strong></td>
								<td><strong>Intimation No</strong></td>
								<td><strong>PED Suggestion</strong></td>
								<td><strong>Name of PED</strong></td>
								<td><strong>Repudiation Letter Date</strong></td>
								<td><strong>Remarks</strong></td>
								<td><strong>Requestor ID</strong></td>
								<td><strong>Requested Date</strong></td>
								<td><strong>Request Status</strong></td>
								<td><strong></strong></td>
								<td><strong>View Trails</strong></td>
							 </tr>
						  </thead>
						  <tbody class="ped-request-detail-tbody">
							
						  </tbody>
						</table>
					</div>					
				</div>
			</div>	
		   
		   <!-- Portability policy Modal -->
		 <!--  <div id="dialog-modal-thirteen" class="col-md-12">
			  <div class="panel panel-default">
				 <div class="panel-heading">
					<p class = "text-left"><strong>Portability policy</strong></p>
				 </div>
				 <div class="panel-body">
					<div class="table-responsive">
					   <table class="table table-condensed table-bordered table-hover">
						  <thead>
							 <tr>
								<td><strong>S.No</strong></td>
								<td ><strong>Pre Insurers Name</strong></td>
								<td ><strong>Product ID and Desc</strong></td>
								<td ><strong>Policy Number</strong></td>
								<td ><strong>Policy Type</strong></td>
								<td ></td >
							 </tr>
						  </thead>
						  <tbody>
							 <c:forEach items="${portablityDetails}" var="portability">
								<tr>
								   <td>${portability.getSerialNo()}</td>
								   <td>${portability.getInsuredName()}</td>
								   <td>${portability.getProductName()}</td>
								   <td>${portability.getPolicyNo()}</td>
								   <td>${portability.getPolicyType()}</td>
								   <td><button class="portDetailsBtn" data-json='${portability.getPortabilityJson()}' style="padding-left:15px" >View Details</button>
								   <td>
								</tr>
							 </c:forEach>
						  </tbody>
					   </table>
					</div>
				 </div>
			  </div>
		   </div>-->
		   
		   <!-- Portability policy detail view modal -->
		   <div id="myModal" class="modal portDetailsModal">
				<div class="modal-content">
				 <span class="close">&times;</span>
				 <div class="row">
					<div class="col-md-12">
					   <div class="panel panel-default">
						  <div class="panel-heading">
							 <p class = "text-left"><strong>Portability Policy</strong></p>
						  </div>
						  <div class="panel-body">
							 <div class="table-responsive">
								<table class="table table-condensed table-bordered table-hover">
								   <tbody class="portDetailsBody">
								   </tbody>
								</table>
							 </div>
						  </div>
					   </div>
					</div>
				 </div>
			  </div>
		   </div>
		   
		   <!-- FVR Grading modal -->
		   <div id="myModal" class="modal fvr-grading-modal custom-modal">
				 <div class="fvr-modal-content fvr-grading custom-modal-content">
				 <span class="close fvrGradingCloseIcon">&times;</span>
				 <h3>FVR Grading Details</h3>
				 <span class="sub-heading fvrSeqValue"></span>
				 <table class="fvr-details"  cellpadding="5">
					<tbody>
					   <tr>
						  <td><span>Representative Code</span></td>
						  <td><input type="text" id="repCode" disabled ></td>
						  <td><span>FVR Assigned Date</span></td>
						  <td><input type="text" id="fvrAssignedDate" disabled></td>
					   </tr>
					   <tr>
						  <td><span>Representative Name</span></td>
						  <td><input type="text" id="repName" disabled></td>
						  <td><span>FVR Received Date</span></td>
						  <td><input type="text" id="fvrReceivedDate" disabled></td>
					   </tr>
					   <tr>
						  <td></td>
						  <td></td>
						  <td><span>FVR TAT</span></td>
						  <td><input type="text" id="fvrTat" disabled></td>
					   </tr>
					</tbody>
				 </table>
				 <div>
					<span class="sub-heading">SEGMENT: A</span>
					<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
					   <thead>
						  <tr>
							 <th> S.No </th>
							 <th> A. POINTS TO FOCUS FOR VERIFICATION </th>
							 <th></th>
						  </tr>
					   </thead>
					   <tbody class="fvrSegA">
						  
					   </tbody>
					</table>
				 </div>
				 <div>
					<span class="sub-heading">SEGMENT: B</span>
					<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
					   <thead>
						  <tr>
							 <th> S.No </th>
							 <th> B. STANDARD VERIFICATION POINTS </th>
							 <th></th>
						  </tr>
					   </thead>
					   <tbody class="fvrSegB">
						  
					   </tbody>
					</table>
				 </div>
				 <div>
					<span class="sub-heading">SEGMENT: C</span>
					<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
					   <thead>
						  <tr>
							 <th> S.No </th>
							 <th> C. OTHERS </th>
							 <th></th>
						  </tr>
					   </thead>
					   <tbody class="fvrSegC">
						 
					   </tbody>
					</table>
				 </div>
				 <div>
					<div align="center"><button class="fvrGradingCloseBtn">Close</button></div>
				 </div>
			  </div>
		   </div>
		   
		   <!-- FVR view document modal -->
		   <div id="myModal" class="modal fvr-document-modal custom-modal">
			  <div class="modal-content fvr-grading custom-modal-content">
				 <span class="close fvrDocumentCloseIcon">&times;</span>
				 <h3>View Uploaded Documents</h3>
				 <table class="fvr-details"  cellpadding="5">
					<tbody>
					   <tr>
						  <td><span>Intimation No</span></td>
						  <td><label id="intimationNo"></label></td>
						  <td><span>Claim No</span></td>
						  <td><label id="claimNo"></label></td>
					   </tr>
				 </table>
				 <span class="sub-heading">Document Details Table</span>
				 <div class="scroll">
					<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
					   <thead>
						  <tr>
						  <tr>
							 <th style="width: 3%;"> S.No </th>
							 <th style="width: 10%;"> Document Type </th>
							 <th style="width: 12%;"> Cashless/ROD Number </th>
							 <th style="width: 10%;"> File Name </th>
							 <th style="width: 8%;" class="ellipsis"> Document Received/Sent Date And Time </th>
							 <th style="width: 7%;"> Document  Source </th>
							 <th style="width: 5%;"> View Document </th>
						  </tr>
					   </thead>
					   <tbody class="fvrDocumentBody">
					   </tbody>
					</table>
				 </div>
				 <div>
					<div align="center"><button class="fvrDocumentCloseBtn">OK</button></div>
				 </div>
			  </div>
		   </div>
		   
		   <!-- PED Endorsement Details modal -->
		   <div id="myModal" class="modal ped-detail-modal custom-modal">
			  <div class="modal-content fvr-grading custom-modal-content">
				 <span class="close pedDetailCloseIcon">&times;</span>
				 <h3>View PED Endorsement Details</h3>
				 <div class="ped-panel-content" >
					<div class="ped-panel">
					   <span>PED Request Initiation Details</span>
					</div>
					<div >
					   <table class="fvr-details"  cellpadding="5" style="margin-left: 2%" class="scrollit">
						  <tbody class="ped-table-body">
							 <tr>
								<td><span>PED Suggestion</span></td>
								<td class="viewPedSug"></td>
							 </tr>
							 <tr>
								<td><span>Name of PED</span></td>
								<td class="viewPedName" ></td>
							 </tr>
							 <tr>
								<td><span>Remarks</span></td>
								<td class="viewPedRemarks"></td>
							 </tr>
							 <tr>
								<td><span>Repudiation Letter Date</span></td>
								<td class="viewRepdate"></td>
							 </tr>
							 <tr class="discussedWith">
								<td><span>Discussed Wtih</span></td>
								<td class="viewDiscussedWith"></td>
							 </tr>
							 <tr class="suggestion">
								<td><span>Suggestion</span></td>
								<td class="viewSuggestion"></td>
							 </tr>
					   </table>
					   <div  class="">
						  <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0" >
							 <thead>
								<tr>
								<tr>
								   <th style="width: 5%;"> Description</th>
								   <th style="width: 3%;"> PED Code </th>
								   <th style="width: 10%;"> ICD Chapter </th>
								   <th style="width: 12%;"> ICD Block </th>
								   <th style="width: 10%;"> ICD Code </th>
								   <th style="width: 8%;"> Source </th>
								   <th style="width: 7%;">Others Specify </th>
								   <th style="width: 5%;">Doctor Remarks</th>
								</tr>
							 </thead>
							 <tbody style="background-color: white;" class="pedDetailBody">
							 </tbody>
						  </table>
					   </div>
					   <div class="" >
						  <div class="ped-panel">
							 <span>Requestor Details</span>
						  </div>
						  <div >
						  </div>
						  <table class="fvr-details"  cellpadding="5" style="margin-left: 2%">
							 <tbody>
								<tr>
								   <td><span>Requestor Role</span></td>
								   <td></td>
								</tr>
								<tr>
								   <td><span>Requestor ID / Name</span></td>
								   <td class="viewReqId"></td>
								</tr>
								<tr>
								   <td><span>Requested Date</span></td>
								   <td class="viewReqDate"></td>
								</tr>
								 </tbody>
						  </table>
						  <div  class="ped-ammended-div hidden">
						  <div class="ped-panel">
							 <span>PED Request Ammended Details</span>
						  </div>
						  <table class="fvr-details"  cellpadding="5" style="margin-left: 2%" class="scrollit">
						  <tbody>
							 <tr>
								<td><span>PED Suggestion</span></td>
								<td><input type="text" class="viewPedSugAmd" disabled></td>
							 </tr>
							 <tr>
								<td><span>Name of PED</span></td>
								<td><input type="text" class="viewPedNameAmd" disabled></td>
							 </tr>
							 <tr>
								<td><span>Remarks</span></td>
								<td><input type="text" class="viewPedRemarksAmd" disabled></td>
							 </tr>
							 <tr>
								<td><span>Repudiation Letter Date	
								   </span>
								</td>
								<td><input type="text" class="viewRepdateAmd" disabled></td>
							 </tr>
					   </table>
					   
						  <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0" >
							 <thead>
								<tr>
								<tr>
								   <th style="width: 5%;"> Description</th>
								   <th style="width: 3%;"> PED Code </th>
								   <th style="width: 10%;"> ICD Chapter </th>
								   <th style="width: 12%;"> ICD Block </th>
								   <th style="width: 10%;"> ICD Code </th>
								   <th style="width: 8%;"> Source </th>
								   <th style="width: 7%;">Others Specify </th>
								   <th style="width: 5%;">Doctor Remarks</th>
								</tr>
							 </thead>
							 <tbody style="background-color: white;" class="ped-ammended-table-body">
							 </tbody>
						  </table>
						    <table class="fvr-details"  cellpadding="5" style="margin-left: 2%">
							 <tbody>
								<tr>
								   <td><span>Requestor Role</span></td>
								   <td><input type="text" class="" disabled></td>
								</tr>
								<tr>
								   <td><span>Requestor ID / Name</span></td>
								   <td><input type="text" class="viewReqIdAmd" disabled></td>
								</tr>
								<tr>
								   <td><span>Requested Date</span></td>
								   <td><input type="text" class="viewReqDateAmd" disabled></td>
								</tr>
						  </table>
					   </div>
					   
					   	<div  class="ped-approved-div hidden">
						  <div class="ped-panel">
							 <span>PED Request Approver Details</span>
						  </div>
						  <table class="fvr-details"  cellpadding="5" style="margin-left: 2%" class="scrollit">
						  <tbody>
							 <tr>
								<td><span>PED Suggestion</span></td>
								<td><input type="text" class="viewPedSugApp" disabled></td>
							 </tr>
							 <tr>
								<td><span>Name of PED</span></td>
								<td><input type="text" class="viewPedNameApp" disabled></td>
							 </tr>
							 <tr>
								<td><span>Remarks</span></td>
								<td><input type="text" class="viewPedRemarksApp" disabled></td>
							 </tr>
							 <tr>
								<td><span>Repudiation Letter Date	
								   </span>
								</td>
								<td><input type="text" class="viewRepdateApp" disabled></td>
							 </tr>
					   </table>
					   
						  <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0" >
							 <thead>
								<tr>
								<tr>
								   <th style="width: 5%;"> Description</th>
								   <th style="width: 3%;"> PED Code </th>
								   <th style="width: 10%;"> ICD Chapter </th>
								   <th style="width: 12%;"> ICD Block </th>
								   <th style="width: 10%;"> ICD Code </th>
								   <th style="width: 8%;"> Source </th>
								   <th style="width: 7%;">Others Specify </th>
								   <th style="width: 5%;">Doctor Remarks</th>
								</tr>
							 </thead>
							 <tbody style="background-color: white;" class="ped-approved-table-body">
							 </tbody>
						  </table>
						    <table class="fvr-details"  cellpadding="5" style="margin-left: 2%">
							 <tbody>
								<tr>
								   <td><span>PED Status</span></td>
								   <td><input type="text" class="viewPedStatusApp" disabled></td>
								</tr>
								<tr>
								   <td><span>Query Remarks(Approver)</span></td>
								   <td><input type="text" class="viewQueryApp" disabled></td>
								</tr>
								<tr>
								   <td><span>Specialist Type</span></td>
								   <td><input type="text" class="viewSplApp" disabled></td>
								</tr>
								<tr>
								   <td><span>Reason For Referring(Approver)</span></td>
								   <td><input type="text" class="viewReasonApp" disabled></td>
								</tr>
								<tr>
								   <td><span>Rejection Remarks</span></td>
								   <td><input type="text" class="viewRejectionApp" disabled></td>
								</tr>
								<tr>
								   <td><span>Approval Remarks</span></td>
								   <td><input type="text" class="viewApprovalApp" disabled></td>
								</tr>
						  </table>
					   </div>
					   
						  <div class="ped-panel">
							 <span>PED Request Processed - Query Replied Details</span>
						  </div>
						  <div class="ped-panel ped-panel-empty">
							 <span></span>
						  </div>
						   <div  class="">
						  <div class="ped-panel">
							 <span>PED Request Processed - Specialist Advise Details</span>
							</div>
						<div class="ped-specialist-div hidden">							
							 <table class="fvr-details"  cellpadding="5" style="margin-left: 2%">
							<tbody class="ped-specialist-table-body">
							 <tr>
								<td><span>PED Status</span></td>
								<td class="viewPedStatusSpl"></td>
							 </tr>
							 <tr>
								<td><span>Specialist Remarks</span></td>
								<td class="viewPedRemarksSpl" ></td>
							 </tr>

							 </tbody>
					   </table>
						  </div>
						  <div class="ped-panel ped-panel-empty ped-specialist-empty-div">
							 <span></span>
						  </div>
					   </div>
					</div>
						  <div class="ped-panel">
							 <span>PED Request Processed - Escalation Details</span>
						  </div>
						  <div class="ped-panel ped-panel-empty">
							 <span></span>
						  </div>
					   </div>
					
				 </div>
			  </div>
		   </div>
		   
		   <!-- PED view history modal -->
		   <div id="myModal" class="modal pedHistoryModal custom-modal">
			  <div class="modal-content fvr-grading custom-modal-content">
				 <span class="close pedHistoryCloseIcon">&times;</span>
				 <h3>View History</h3>
				 <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
					<thead>
					   <tr>
					   <tr>
						  <th style="width: 3%;"> Status </th>
						  <th style="width: 10%;"> Date & Time </th>
						  <th style="width: 12%;"> User Name </th>
						  <th style="width: 10%;"> Remarks </th>
					   </tr>
					</thead>
					<tbody class="pedHistoryBody">
					</tbody>
				 </table>
			  </div>
		   </div>
		   
		  <!-- FVR Document modal -->
		   <div id="myModal" class="modal fvrDocumentViewModal custom-modal">
			  <div class="modal-content fvr-grading custom-modal-content">
				 <span class="close fvrDocumentViewCloseIcon">&times;</span>
				 <h3 class="frame-title" >View Uploaded Document</h3>
				 <div class="fvrFrame">
					<iframe class="docFrame" id="frame"></iframe>
				 </div>
				 <div align="center"><button class="fvrDocumentViewCloseBtn" style="background-color:#ed473b;color:white">Close</button></div>
			  </div>			   
		   </div>   
	   
		   <!-- PreAuth Details -->
		 <div id="preauth-details-dialog" class="modal preauth-modal custom-modal">
		 <div class="overlay" style="display:none"></div> 
			<div class="preauth-title">
					<label> Pre-auth Reference No </label>
					<select class="preselect">
						
				   </select>
				   <input class="preauthTitleText hidden" type="text" value=""  />
				   <button id="detailedView">View Pre-auth (Detailed)</button>
				   <span class="close detailClose hidden">&times;</span>
				</div>
			   <div class="ped-panel-content">
				  <div class="ped-panel">
					 <span>Preauth Details</span>
				  </div>
				  <div class="scroll" >
					 <table class="fvr-details preauth-details"  cellpadding="5" >
						<tbody>
						   <tr>
							  <td><span>Pre-auth Recieved Date & Time</span></td>
							  <td class="preauth-values" id="preauth-created-date" > </td>
							  <td class="prevalue"><span>Pre-auth Response Date & Time</span></td>
							  <td class="preauth-values" id="preauth-modified-date" ></td>
						   </tr>
						   <tr>
							  <td><span>Status</span></td>
							  
							  <td class="preauth-values" id="preauth-status">	
											
										
							  </td>

							  <td class="prevalue"><span>Remarks</span></td>
							  <td class="preauth-values" id="preauth-remarks" > </td>
						   </tr>
						   <tr>
							  <td><span>Pre-auth Approved Amt</span></td>
							   <td class="preauth-values " id="preauth-approved-amt">  </td>
						   </tr>
						   <tr>
							  <td><span>Medical Remarks</span></td>
							  <td class="preauth-values" id="preauth-medical-remarks" >  </td>
						   </tr>
						   <tr>
							  <td></td>
							  <td></td>
							  <td class="prevalue"><span>Doctor Note (Internal remarks)</span></td>
							   <td class="preauth-values" id="preauth-doctor-note" ></td>
						   </tr>
						   <tr>
							  <td></td>
						   </tr>
						   <tr>
							  <td><span>Date of Admission</span></td>
							   <td class="preauth-values" id="preauth-doa">  </td>
							  <td class="prevalue"><span>Reason For Admission</span></td>
							   <td class="preauth-values" id="preauth-admission-reason"  >   </td>
						   </tr>
						   <tr>
							  <td><span>Reason For Change in DOA</span></td>
							   <td class="preauth-values" id="preauth-change-doa"> </td>
							  <td class="prevalue"><span>No of Days</span></td>
							   <td class="preauth-values" id="preauth-days">  </td>
						   </tr>
						   <tr>
							  <td><span>Room Category</span></td>
							  <td class="preauth-values" id="preauth-room-cat"  > </td>
							  <td class="prevalue"><span>Nature of Treatment</span></td>
							   <td class="preauth-values" id="preauth-nature"  ></td>
						   </tr>
						   <tr>
							  <td><span>Critical Illness</span></td>
							  <td class="preauth-values" id="preauth-critical-illness" > 
							  
							  </td>
							  <td class="prevalue"><span>1st Consultation Date</span></td>
							  <td class="preauth-values" id="preauth-cons-date" >  </td>
						   </tr>
						   <tr>
							  <td><span>Specify</span></td>
							  <td class="preauth-values" id="preauth-specify-illness" > </td>
							  <td class="prevalue"><span>Corp Buffer</span></td>
							  <td class="preauth-values" id="preauth-corp-buffer" >
							   
									 </td>
						   </tr>
						   <tr>
							  <td><span>Treatment Type</span></td>
							  <td class="preauth-values" id="preauth-treatment-type" > </td>
							  <td class="prevalue"><span>Automatic Restoration</span></td>
							  <td class="preauth-values" id="preauth-auto-rest" >  </td>
						   </tr>
						   <tr>
							  <td><span>Patient Status</span></td>
							  <td class="preauth-values" id="preauth-patient-status" >  </td>
							  <td class="prevalue"><span>Illness</span></td>
							  <td class="preauth-values" id="preauth-illness"> </td>
						   </tr>
						   <tr>
							  <td><span>Date of Death</span></td>
							  <td class="preauth-values" id="preauth-death-date">  </td>
							  <td class="prevalue"><span>Relapse of Illness</span></td>
							  <td class="preauth-values" id="preauth-relapse-illness"> </td>
						   </tr>
						   <tr>
							  <td><span>Reason for Death</span></td>
							  <td class="preauth-values" id="preauth-death-reason">  </td>
							  <td class="prevalue"><span>Remarks (Relapse)</span></td>
							  <td class="preauth-values" id="preauth-relapse-remarks"> </td>
						   </tr>
						   <tr>
							  <td><span>Terminate Cover</span></td>
							  <td class="preauth-values" id="preauth-terminate-cover"> </td>
							  <td class="prevalue"><span>Attach to Previous Claim</span></td>
							  <td class="preauth-values" id="preauth-previous-claim"> </td>
						   </tr>
						   
						   <tr>
								  <td><span>Initiate Field Visit Request</span></td>
								   <td class="preauth-values" id="preauth-field-request">
								 
									</td>
									<td class="prevalue"><span>Specialist Opinion Taken</span></td>
									<td class="preauth-values" id="preauth-specialist-option">
									
									</td>
								</tr> 
								<tr>
									<td><span>FVR Not Required Remarks</span></td>
									<td class="preauth-values" id="preauth-fvr"></td>
									<td class="prevalue" ><span>Specialist Type</span></td>
									<td class="preauth-values" id="preauth-spl-type"></td>
								</tr>
								<tr>
									<td><span>Allocation to</span></td>
									<td class="preauth-values" id="preauth-allocation"></td>
									<td class="prevalue"><span>Specialist Consulted</span></td>
									<td class="preauth-values" id="preauth-spl-consulted"></td>
								</tr>  
								<tr>
									<td><span>FVR Trigger Points</span></td>
									<td class="preauth-values" id="preauth-fvr-trigger"></td>
									<td class="prevalue"><span>Remarks by Specialist</span></td>
									<td class="preauth-values" id="preauth-spl-remarks"></td>
								</tr>    
								<tr>
									<td><span>Investigator Name</span></td>
									<td class="preauth-values" id="preauth-inv-name">  </td>
									<td class="prevalue"><span>Remarks for CPU</span></td>
									<td></td>
								</tr>
								<tr>
									 <td><span>Investigation Review Remarks</span></td>
								     <td></td>
									 <td class="prevalue"><span>Treatment Remarks</span></td>
									 <td class="preauth-values" id="preauth-treatment-remarks"></td>
								</tr>
								<tr>
									<td><span>Negotiated with</span></td>
									<td class="preauth-values" id="preauth-nego-with"> </td>
								</tr>
						  </tbody>
					 </table>
				  </div>
				</div>  
				<div class="preauth-speciality hidden" style="text-align:center;">
				 <span class="sub-heading center">Speciality</span>
				 <table class="fvr-sub-table " border="0" cellspacing="0" cellpadding="0">
					<thead>
					   <tr>
						  <th>Speciality</th>
						  <th>Remarks</th>
					   </tr>
					</thead>
					<tbody>
					   <c:forEach items="${speciality}" var="speciality">
						  <tr>
							 <td >${speciality.getSpecialityType()}</td>
							 <td>${speciality.getRemarks()}</td>
						  </tr>
					   </c:forEach>
					</tbody>
				 </table>
			  </div>
				
				<!-- Sub limits, Package & SI Restriction Table  -->
				  <div class="preauth-restriction" style="text-align:center;">
					 <span class="sub-heading">Sub limits, Package & SI Restriction</span>
				  
				  <div class="scroll">
					 <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
						<thead>
						   <tr>
							  <th>Diagnosis / Procedure</th>
							  <th>Description</th>
							  <th>PED / Exclusion Details</th>
							  <th>Ambulance Charge Applicable</th>
							  <th>Ambulance Charges</th>
							  <th>Amount Considered (A)</th>
							  <th>Package Amt (B)</th>
							  <th>Min of A, B (C)</th>
							  <th>Co-Pay % (D)</th>
							  <th>Co-Pay Amount (E)</th>
							  <th>Net Amount (After Co-pay) (F)</th>
							  <th>Amount With Ambulance Charges</th>
							  <th>Sub Limit Amount (G)</th>
							  <th>Sub Limit Utilized Amt (H) </th>
							  <th>Sub Limit Available Amt (I)</th>
							  <th>SI Restriction (J)</th>
							  <th>SI Restriction Utilized Amt (K)</th>
							  <th>SI Restriction Available Amt (L) (J-K)</th>
							  <th>Min of Column ( F, I, L) (M)</th>
							  <th>Apportion Final App Amt</th>
						   </tr>
						</thead>
						<tbody class="preauth-sublimit-table-body">
						   
						   
						
						</tbody>
					 </table>
				  </div>
			   </div>
			   
			   <div>
			   <div class="preauth-amount" >
				  <span class="sub-heading">A) Amount Considered</span>
				  <table  class="fvr-sub-table preauthamt" border="0" cellspacing="0" cellpadding="0">
							<thead>
						<tr><td ><strong>Particulars</strong></td><td ><strong>Amount</strong></td>
						</tr>
						</thead>
						<tbody class="preauth-amt-table-body">
						 
						</tbody>
					</table>
			  </div>
			  <div class="preauth-balance" >
				  <span class="sub-heading">B) Balance Sum Insured</span>
				  <table class="fvr-sub-table auth-balance" border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr><td><strong>Particulars</strong></td><td ><strong>Amount</strong></td>
						</tr>
					</thead>
						<tbody class="preauth-balance-body">
						
						</tbody>
					</table>

			   </div>
			   </div>
			   
			   <div class="preauth-amt-sub">
			  		<span class="sub-heading"> C) Sum limits,package & SI Restriction Amount : </span>
					<input type="text" style="margin-top:2%" class="preauth-amt-sublimit" disabled value="" />
			   </div>
			   
			  
				<div class="preauth-sublimit hidden">
					<span class="sub-heading preauth-sublimit-head"> </span>	
				   <div class="scroll">
					<table class="fvr-sub-table " border="0" cellspacing="0" cellpadding="0">
					 <thead>
					   <tr>
							<th>Details</th>
							<th>No of Days(A)</th>
							<th>Per Day Amount(B)</th>
							<th>Claimed Amount<br>(C)</th>
							<th>Deductible<br>(D)</th>
							<th>Net Amount<br>(E)</th>
							<th>No of Days<br>(F)</th>
							<th>Per Day Amount<br>(Product Based)(G)</th>
							<th>Amount<br>(H)</th>
							<th>Considered PerDay<br>Amount</th>
							<th>Non Payables<br>(Inclu Deductibles)(I)</th>
							<th>Payable Amount<br>(J)</th>
							<th>Deductiable /<br>Non Payable<br>Reason</th>
						</tr>				
					</thead>
					<tbody class="preauth-restrict-table-body">
						
						</tbody>
					</table>	
					</div>
						
						</div>
					
							
							<div class="preauth-pedvalidation hidden" style="text-align:center;">
								<span class="sub-heading center">Ped Validation</span>
									<table class="fvr-sub-table " border="0" cellspacing="0" cellpadding="0">
										<thead>
											<tr>
											   <th>Diagnosis</th>
											   <th>PED Name</th>
											   <th>ICD Chapter</th>
											   <th>ICD Block</th>
											   <th>ICD Code</th>
											   <th>Policy Ageing</th>
											   <th>PED / Exclusion Impact On Diagnosis</th>
											   <th>Exclusion Details</th>
											   <th>Remarks</th>
											</tr>
										</thead>
										<tbody class="ped-validation-tbody">
										  
										</tbody>
									</table>	
							</div>
							
							<div class="preauth-exclusion hidden" style="text-align:center;">
								<span class="sub-heading center">Procedure Exclusion Check</span>
									<table class="fvr-sub-table " border="0" cellspacing="0" cellpadding="0">
										<thead>
										   <tr>
											  <th>Procedure Name</th>
											  <th>Procedure Code</th>
											  <th>Package Amount </th>
											  <th>Policy Ageing</th>
											  <th>Procedure Status</th>
											  <th>Exclusion Details</th>
											  <th>Remarks</th>
										   </tr>
										</thead>
										<tbody  class="ped-exclusion-tbody">
										   
										</tbody>
									</table>	
							</div>
							
			
				  <div class="preauth-total-amount">
						<label>Pre-auth Approved Amt</label><input type="text" value="" id="preauth-amount"/>
				  </div>
	 
            </div>
			
			<!-- Acknowledgement Details modal -->
			<div id="acknowledge-details-dialog" class="modal preauth-modal custom-modal">
					<div class="preauth-title">
					<label> View Acknowledgement Details </label>
				</div>
			   <div class="ped-panel-content" style="padding: 2%;">
				  <div>
					 <table class="fvr-details preauth-details"  cellpadding="5" >
						<tbody>
						   <tr>
							  <td><span>Acknowledgement No</span></td>
							  <td class="preauth-values" id="ackNo"> </td>
							  <td class="prevalue"><span>Acknowledgement Created By - ID</span></td>
							  <td class="preauth-values" id="ackCreatedById"></td>
						   </tr>
						   <tr>
							  <td><span>Acknowledgement Created on	</span></td>
							  <td class="preauth-values" id="ackCreatedOn">	</td>

							  <td class="prevalue"><span>Acknowledgement Created By - Name</span></td>
							  <td class="preauth-values" id="ackCreatedByName" > </td>
						   </tr>
						   <tr>
							  <td><span>Documents  Recieved  From</span></td>
							   <td class="preauth-values" id="ackRcvdFrom"> </td>
						  
							  <td class="prevalue"><span>Email  ID</span></td>
							  <td class="preauth-values" id="ackEmailId" >  </td>
						   </tr>
						   <tr>
							  <td><span>Documents  Recieved  Date</span></td>
							   <td class="preauth-values" id="ackRcvdDate"> </td>
						  
							  <td class="prevalue"><span>Acknowledgement  Contact  Number </span></td>
							  <td class="preauth-values" id="ackContact" >  </td>
						   </tr>
						   
						   <tr>
							  <td><span>Mode  of  Receipt</span></td>
							   <td class="preauth-values" id="ackRecipt">   </td>
													   
						   </tr>
						   <tr>
							  <td><span>Reconsideration  Request</span></td>
							   <td class="preauth-values" id="ackReconsider">  </td>
							
						   </tr>
						 
						  </tbody>
					 </table>
				  </div>
				  <div>
				  <label> Bill Classification </label>
				  <table class="fvr-details preauth-details"  cellpadding="5" >
				  
						<tbody>
						   <tr class="billRow1">
						   </tr>
						   <tr class="billRow2">
						   </tr>
						   <tr>
							   <td></td>
							   <td></td>
						   </tr>
						   <tr class="billRow3">
							  <td></td>
							  <td></td>
						   </tr>
						   
						   <tr class="billRow4">				  													   
						   </tr>
						 
						  </tbody>
						  </table>
				  </div> 
				  
				  <div style="text-align:center;" class="hidden ackReconsiderROD">
					<span class="sub-heading center">Reconsider ROD List</span>
						<table class="fvr-sub-table " border="0" cellspacing="0" cellpadding="0">
							<thead>
								<tr>
									<th>ROD No</th>
									<th>Claimed Amount</th>
									<th>Approved Amount</th>
									<th>Rod Status</th>
									<th>View Claim Status</th>																	
								</tr>
							</thead>
							<tbody class="ackRODBody">
								
							</tbody>
						</table>	
				</div>
				  
				  <div style="text-align:center;">
					<span class="sub-heading center">View Query Details</span>
						<table class="fvr-sub-table " border="0" cellspacing="0" cellpadding="0">
							<thead>
								<tr>
									<th>S.No</th>
									<th>ROD No</th>
									<th>Bill Classification</th>
									<th>Diagnosis</th>
									<th>Claimed Amount</th>
									<th>Query Raised Role</th>
									<th>Query Raised date</th>
									<th>Query Status</th>
									<th>Query Reply Status</th>
									<th>View Status</th>
									
								</tr>
							</thead>
							<tbody class="ackQueryDetailsBody">
								
							</tbody>
						</table>	
				</div>
				
				<div style="text-align:center;">
					<span class="sub-heading center">Document Checklist</span>
						<table class="fvr-sub-table " border="0" cellspacing="0" cellpadding="0">
							<thead>
								<tr>
									<th>S.No</th>
									<th>Particulars</th>
									<th>Received Status</th>
									<th>No of Documents</th>
									<th>Remarks</th>																	
								</tr>
							</thead>
							<tbody class="ackChecklistBody">
								
							</tbody>
						</table>	
					</div>
				</div>
			</div>
			<!-- Acknowledgement Details - Query Letter modal -->
       		<div id="myModal" class="modal ackQryDetailsModal custom-modal">
				<div class="modal-content fvr-grading custom-modal-content">
					<span class="close ackQryDetailCloseBtn">&times;</span>
						<button class="ackViewQryLetter" id="ackViewQryLetter" > View Query Letter </button>
							<table class="fvr-details preauth-details ackQryDetailsTable"  cellpadding="5" >
							<tbody>
								<tr>
									<td><span style="padding-left:50%;font-size:16px !important;" >Claim Details</span></td>									
								</tr>
								<tr>
									<td><span>Intimation No</span></td>
									<td class="preauth-values" id="ackQryIntNo"> </td>
								</tr>
								<tr>
									 <td><span>Claim Number	</span></td>
									 <td class="preauth-values" id="ackQryclaimNo">	</td>
								</tr>
								<tr>
									  <td><span>Policy No</span></td>
									  <td class="preauth-values" id="ackQryPolicyNo" > </td>
								</tr>
								<tr>
									  <td><span>Acknowledgement No</span></td>
							          <td class="preauth-values" id="ackQryAckNo"> </td>
								</tr>
								<tr>
									  <td><span>ROD No</span></td>
									  <td class="preauth-values" id="ackQryRODNo" >  </td>
								</tr>
							   <tr>
								  <td><span>Documents  Recieved  From</span></td>
								   <td class="preauth-values" id="ackQryRcvdFrom"> </td>
							   </tr>
							   <tr>
								  <td><span>Bill Classification </span></td>
								  <td class="preauth-values" id="ackQryBill" >  </td>
							   </tr>
							   
							   <tr>
								  <td><span>Product Name</span></td>
								   <td class="preauth-values" id="ackQryProductName">   </td>
														   
							   </tr>
							   <tr>
								  <td><span>Claim Type</span></td>
								   <td class="preauth-values" id="ackQryClaimType">  </td>
								
							   </tr>
							   <tr>
								  <td><span>Insured Patient Name</span></td>
								   <td class="preauth-values" id="ackQryInsuredName"> </td>
							   </tr>
							   <tr>
								  <td><span>Hospital Name</span></td>
								  <td class="preauth-values" id="ackQryHospName" >  </td>
							   </tr>
							   
							   <tr>
								  <td><span>Hospital City</span></td>
								   <td class="preauth-values" id="ackQryHospCity">   </td>
														   
							   </tr>
							   <tr>
								  <td><span>Hospital Type</span></td>
								   <td class="preauth-values" id="ackQryHospType">  </td>
								</tr>
								<tr>
								  <td><span>Date of Admission</span></td>
								   <td class="preauth-values" id="ackQryDOA">   </td>
														   
							   </tr>
							   <tr>
								  <td><span>Diagnosis</span></td>
								   <td class="preauth-values" id="ackQryDiagnosis">  </td>
								</tr>
								<tr>
									<td><span style="padding-left:50%;font-size:16px !important;">Query Details</span></td>									
								</tr>
								<tr>
									<td><span>Query Raised By Role</span></td>
									<td class="preauth-values" id="ackQryRaisedRole"> </td>
								</tr>
								<tr>
									 <td><span>Query Raised By ID/Name	</span></td>
									 <td class="preauth-values" id="ackQryRaisedId">	</td>
								</tr>
								<tr>
									  <td><span>Query Raised Date</span></td>
									  <td class="preauth-values" id="ackQryRaisedDate" > </td>
								</tr>
								<tr>
									  <td><span>Query Remarks</span></td>
							          <td class="preauth-values" id="ackQryRemarks"> </td>
								</tr>
								<tr>
									  <td><span>Query Drafted Date</span></td>
									  <td class="preauth-values" id="ackQryDraftDate" >  </td>
								</tr>
							   <tr>
								  <td><span>Query Letter Remarks</span></td>
								   <td class="preauth-values" id="ackQryLetterRemarks"> </td>
							   </tr>
							   <tr>
								  <td><span>Query Approved/Rejected /Redraft date</span></td>
								  <td class="preauth-values" id="ackQryDate" >  </td>
							   </tr>
							   <tr>
								   <td><span>Query Rejected Remarks</span></td>
								   <td class="preauth-values" id="ackQryRejRemarks" >  </td>
								</tr>
							   <tr>
								  <td><span>Query Redraft Remarks</span></td>
								   <td class="preauth-values" id="ackQryRedraftRemarks"> </td>
							   </tr>
							   <tr>
								  <td><span>Query Status</span></td>
								  <td class="preauth-values" id="ackQryStatus" >  </td>
							   </tr>
						 
						  </tbody>
					 </table>
				  </div>
			</div>
			<!-- Coordinator Reply Dialog -->	
			<div id="coordinator-reply-dialog" class="modal coordinator-reply-dialog custom-modal">
				<div class="preauth-title">
					<label> View Coordinator Reply </label>
				</div>
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div class="scroll">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>
							   <tr>
								  <th>S.No</th>
								  <th>Requested Date</th>
								  <th>Replied Date</th>
								  <th>Request Type</th>
								  <th>Requestor Role</th>
								  <th>Requestor Name Id</th>
								  <th>Requestor Remarks</th>
								  <th>View File</th>
								  <th>Co Ordinator Replied Id</th>
								  <th>Co-ordinator Remarks</th>
							   </tr>
							</thead>
							<tbody class="coordinator-reply-body">
							</tbody>
						</table>
					</div>					
				</div>
		</div>
		
		<!-- Specialist Trail Dialog -->	
		<div id="specialist-trail-dialog" class="modal specialist-trail-dialog custom-modal">
				<div class="preauth-title">
					<label> Specialist Opinion </label>
				</div>
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div class="scroll">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>
							   <tr>
								<th>S.No</th>
								<th>Requested Date</th>
								<th>Replied Date</th>
								<th>Specialist Type</th>
								<th>Specialist Dr Name Id</th>
								<th>Requestor Name Id</th>
								<th>Requestor Remarks</th>
								<th>View File</th>
								<th>Specialist Remarks</th>
							   </tr>
							</thead>
							<tbody class="specialist-trail-body">
							</tbody>
						</table>
					</div>					
				</div>
		</div>
			
			<!-- Coordinator-Reply / Specialist Trail View File -->	
			<div id="myModal" class="modal coorViewFileModal custom-modal">
			  <div class="modal-content fvr-grading custom-modal-content">
				 <span class="close coorViewFileCloseIcon">&times;</span>
				 <h3>Document List</h3>
				 <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
					<thead>
					   <tr>
						  <th style="width: 3%;"> S.No </th>
						  <th style="width: 15%;"> File Name </th>
						  <th style="width: 15%;"> View Document </th>
					   </tr>
					</thead>
					<tbody class="coorViewFileBody">
					</tbody>
				 </table>
			  </div>
		   </div>	  
				  
			<!-- Sublimits View Modal Dialog-->	
			<div id="sublimits-details-dialog" class="modal sublimits-details-dialog custom-modal">
			   <div class="preauth-title">
				  <label> Sub Limits Details </label>
			   </div>
			   <div class="ped-panel-content" style="background-color: #ffffff !important;">
				  <div class="scroll">
					 <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
						<thead>
						   <tr>
							  <th>S.No</th>
							  <th>Section</th>
							  <th>Sub Limit Name</th>
							  <th>Sub Limit Amount</th>
							  <th>Including Current Claim Amount</th>
							  <th>Including Current Claim Balance</th>
							  <th>Excluding Current Claim Amount</th>
							  <th>Excluding Current Claim Balance</th>
						   </tr>
						</thead>
						<tbody class="sublimits-details-body">
						</tbody>
					 </table>
				  </div>
			   </div>
			</div>
		
		<!-- HRM Details Dialog-->			
		<div id="hrm-details-dialog" class="modal hrm-details-dialog custom-modal">
				<div class="preauth-title">
					<label> HRM Details </label>
				</div>
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div class="hrm_table_A">
						<table class="fvr-details preauth-details" border="0" cellspacing="0" cellpadding="0">
							<tbody class="hrm-tableA-body">
							   <tr>
								  <td><span>Intimation Number</span></td>
								  <td class="preauth-values" id="hrmIntimationno"> </td>
								  <td class="prevalue"><span>Hospital Name</span></td>
								  <td class="preauth-values" id="hrmHospName"></td>
							   </tr>
							   <tr>
								  <td><span>Hrm Id	</span></td>
								  <td class="preauth-values" id="hrmId">	</td>

								  <td class="prevalue"><span>Phone</span></td>
								  <td class="preauth-values" id="hrmPhone" > </td>
							   </tr>
							   <tr>
								  <td><span>Name</span></td>
								   <td class="preauth-values" id="hrmName"> </td>
							  
								  <td class="prevalue"><span>Email  ID</span></td>
								  <td class="preauth-values" id="hrmEmailId" >  </td>
							   </tr>
							</tbody>
						</table>
					</div>
					
					<div class="scroll hrm_table_B">
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>
							   <tr>
									<th>ANH/NANH</th>
									<th>Diagnosis</th>
									<th>Surgical Procedure</th>
									<th>Claim Amount</th>
									<th>Package Amount</th>
									<th>Request Type</th>
									<th>Doctor Remarks</th>
									<th>Assignee Date &amp; Time</th>
									<th>HRM Reply Remarks</th>
									<th>Reply Date &amp; Time</th>
									<th>Dr.User Id</th>
									<th>Dr.Name</th>
									<th>Dr.Desk Number</th>
							   </tr>
							</thead>
							<tbody class="hrm-diagnosis-body">
							</tbody>
						</table>
					</div>

					
				</div>
		</div>
		
			<!-- Risk Details Dialog-->	
			<div id="risk-details-dialog" class="modal risk-details-dialog custom-modal">
				<div class="preauth-title">
					<label style="margin-left:10% !important"> Risk Details </label>
				</div>
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div>
						<table class="fvr-details preauth-details riskDetailsTable" border="0" cellspacing="0" cellpadding="0">
							<tbody>
							   <tr>
								  <td><span>Policy Number</span></td>
								  <td class="preauth-values" id="riskPolicyNo"> </td>
								  <td class="prevalue"><span>Risk Name</span></td>
								  <td class="preauth-values" id="riskName"></td>
							   </tr>
							   <tr>
								  <td><span>Policy From Date</span></td>
								  <td class="preauth-values" id="riskPolicyFrom"></td>
								  <td class="prevalue"><span>Sum Insured</span></td>
								  <td class="preauth-values" id="riskSumInsured" > </td>
							   </tr>
							   <tr>
								  <td><span>Policy To Date</span></td>
								   <td class="preauth-values" id="riskPolicyTo"> </td>
							  
								  <td class="prevalue"><span>Relationship</span></td>
								  <td class="preauth-values" id="riskRelationship" >  </td>
							   </tr>
							   <tr>
								  <td><span>Section Code</span></td>
								   <td class="preauth-values" id="riskSectionCode"> </td>
							  
								  <td class="prevalue"><span>Age</span></td>
								  <td class="preauth-values" id="riskAge" >  </td>
							   </tr>
							   <tr>
								  <td><span>Cover Code</span></td>
								   <td class="preauth-values" id="riskCoverCode"> </td>
							  
								  <td class="prevalue"><span>Risk PED</span></td>
								  <td class="preauth-values" id="riskPED" >  </td>
							   </tr>
							   <tr>
								  <td><span></span></td>
								   <td class="preauth-values"> </td>
							  
								  <td class="prevalue"><span>Portal PED</span></td>
								  <td class="preauth-values" id="riskPortalPED" >  </td>
							   </tr>
							</tbody>
						</table>
					</div>			
				</div>
		</div>
			<!-- Previous Insurance Details Dialog -->	
			<div id="previous-insurance-details-dialog" class="modal previous-insurance-details-dialog custom-modal">
				<div class="preauth-title">
					<label> Previous Insurance Details</label>
				</div>
				<div class="ped-panel-content" style="background-color: #ffffff !important;">
					<div>
						<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
							<thead>
								<tr>
								<th>Previous Insurer Name</th>
								<th>Policy number</th>
								<th>Policy From Date</th>
								<th>Policy To Date</th>
								<th>U/W Year</th>
								<th>Sum Insured</th>
								<th>Product Name</th>
								<th>View Policy Schedule</th>
								<th>View Insured Details</th>
							</tr>				
						</thead>
						<tbody class="previous-insurance-table-body">
						</tbody>
						</table>
					</div>			
				</div>
		</div>	  
			<!-- Previous Insurance Details - Insured Dialog -->		
			<div id="myModal" class="modal insuredDetailModal custom-modal">
			  <div class="modal-content fvr-grading custom-modal-content">
				 <span class="close insuredDetailCloseIcon">&times;</span>
				 <h3>Insured Details</h3>
				 <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
					<thead>
					   <tr>
						  <th style="width: 3%;"> S.No </th>
						  <th style="width: 10%;"> Insured Name </th>
						  <th style="width: 3%;"> Sex </th>
						  <th style="width: 10%;"> DOB </th>
						  <th style="width: 10%;"> Age </th>
						  <th style="width: 10%;"> Relation </th>
						  <th style="width: 10%;"> Sum Insured </th>
						  <th style="width: 10%;"> PRE-EXISTING DISEASE </th>
					   </tr>
					</thead>
					<tbody class="insuredDetailBody">
					</tbody>
				 </table>
			  </div>
		   </div>  
		   
		    <!-- 64VB complaince modal -->
		   <div id="myModal" class="modal 64VBDocumentViewModal custom-modal">
			  <div class="modal-content fvr-grading custom-modal-content">
				 <span class="close 64VBDocumentViewCloseIcon">&times;</span>
				 <h3 class="frame-title" >64 VB Compliance Report</h3>
				 <div class="64VBFrame">
					
				 </div>
				 <div align="center"><button class="64VBDocumentViewCloseBtn" style="background-color:#ed473b;color:white">Close</button></div>
			  </div>			   
		   </div>  
			<!-- Balance sum Insured - Dialog -->	
			<div id="balance-sum-dialog" class="modal balance-sum-dialog custom-modal">
			   <div class="preauth-title">
				  <label> Balance Sum Insured </label>
			   </div>
			   <div class="ped-panel-content" style="background-color: #ffffff !important;">
				  <div class="sum_insured">
					 <table class="fvr-details preauth-details sumInsuredTable" border="0" cellspacing="0" cellpadding="0">
						<tbody>
						   <tr>
							  <td><span>Policy Number</span></td>
							  <td class="preauth-values" id="blPolicyNo"> </td>
							  <td class="prevalue"><span>Intimation Number</span></td>
							  <td class="preauth-values" id="blIntNo"></td>
							  <td class="prevalue"><span>Name of the Insured</span></td>
							  <td class="preauth-values" id="blInsName"></td>
						   </tr>
						   <tr>
							  <td><span>Product Code</span></td>
							  <td class="preauth-values" id="blProductCode"></td>
							  <td class="prevalue"><span>Product Name</span></td>
							  <td class="preauth-values" id="blProductName" > </td>
							  <td class="prevalue gmcMainMember hidden"><span>Main Member Name</span></td>
							  <td class="preauth-values gmcMainMember hidden" id="gmcMainMember"> </td>
						   </tr>
						   <tr>
							  <td><span>Original SI</span></td>
							  <td class="preauth-values" id="blOriginalSI"> </td>
							  <td class="prevalue"><span>Restored SI</span></td>
							  <td class="preauth-values" id="blRestoredSI" ></td>
							  <td class="prevalue gmcInnerLimitAppl hidden"><span>Inner Limit Applicable</span></td>
							  <td class="preauth-values gmcInnerLimitAppl hidden" id="gmcInnerLimitAppl"> </td>
						   </tr>
						   <tr>
							  <td><span>Cummulative Bonus</span></td>
							  <td class="preauth-values" id="blCumBonus"> </td>
							  <td class="prevalue definedLimit hidden"><span>Defined Limit</span></td>
							  <td class="preauth-values definedLimit hidden" id="definedLimit"> </td>
							  <td class="prevalue gmcInnerLimitInsured hidden"><span>Inner Limit for the insured</span></td>
							  <td class="preauth-values gmcInnerLimitInsured hidden" id="gmcInnerLimitInsured"> </td>
						   </tr>
						   <tr>
							  <td><span>Recharged SI</span></td>
							  <td class="preauth-values" id="blRechargedSI"> </td>
							  <td class="prevalue claimsPaid hidden"><span>Restored SI Paid</span></td>
							  <td class="preauth-values claimsPaid hidden" id="claimsPaid"> </td>
							  <td class="prevalue gmcInnerLimitAval hidden"><span>Inner Limit Available for this Claim</span></td>
							  <td class="preauth-values gmcInnerLimitAval hidden" id="gmcInnerLimitAval"> </td>
						   </tr>
						   <tr>
							  <td><span>Limit of Coverage</span></td>
							  <td class="preauth-values" id="blLimit"> </td>
							  <td class="prevalue siOutStand hidden"><span>Restored SI Outstanding</span></td>
							  <td class="preauth-values siOutStand hidden" id="siOutStand"> </td>
							  <td class="prevalue gmcInnerLimitUtilised hidden"><span>Inner Limit Utilised</span></td>
							  <td class="preauth-values gmcInnerLimitUtilised hidden" id="gmcInnerLimitUtilised"> </td>
						   </tr>
						   <tr>
							  <td class="blSecCode hidden"><span>Policy Section Code</span></td>
							  <td class="preauth-values blSecCode hidden" id="blSecCode"> </td>
							  <td class="prevalue provisionClaim hidden"><span>Provision for Current Claim</span></td>
							  <td class="preauth-values provisionClaim hidden" id="provisionClaim"> </td>							  
						   </tr>
						</tbody>
					 </table>
				  </div>
				  <div class="PA_sum_insured hidden">
					 <table class="fvr-details preauth-details sumInsuredTable" border="0" cellspacing="0" cellpadding="0">
						<tbody>
						   <tr>
							  <td><span>Name of the Insured</span></td>
							  <td class="preauth-values" id="blPAInsName"> </td>
							  <td class="prevalue"><span>Age</span></td>
							  <td class="preauth-values" id="blPAInsAge"></td>							 
						   </tr>
						 </tbody>
					 </table>
				  </div>  
				  <div class="scroll sum_insured_tables">
						
				  </div>
				  <div class="scroll PA_sum_insured_tables hidden">
				  <table class="fvr-sub-table paInsTables" border="0" cellspacing="0" cellpadding="0" >
							 <thead>
								<tr>								
								    <th>Original SI</th>
									<th>Cummulative Bonus</th>
									<th>Total SI</th>
									<th>Claim Paid</th>
									<th>Claim OS</th>
									<th>Balance SI</th>
									<th>Provision for Current Claim</th>
									<th>Available SI</th>
								</tr>
							 </thead>
							 <tbody style="background-color: white;" class="balanceSumInsured">
							 </tbody>
					 </table>
					
					 <table class="fvr-sub-table paInsTables" border="0" cellspacing="0" cellpadding="0" >
							 <thead>
								<tr>
									<th>Cover Description</th>
									<th>Original SI</th>
									<th>Cummulative Bonus</th>
									<th>Cover SI</th>
									<th>Claim Paid</th>
									<th>Claim Outstanding</th>
									<th>Provision for Current Claim</th>
									<th>Cover Balance</th>
								</tr>
							 </thead>
							 <tbody style="background-color: white;" class="balanceSumInsuredBenefits">
							 </tbody>
					 </table>
					  <div class="preauth-title">
						<label> Add on Covers </label>
					</div>
					 <table class="fvr-sub-table paInsTables" border="0" cellspacing="0" cellpadding="0" >
							 <thead>
								<tr>
									<th>Cover Description</th>
									<th>Cover SI</th>
									<th>Claim Paid</th>
									<th>Claim Outstanding</th>
									<th>Provision for Current Claim</th>
									<th>Cover Balance</th>
								</tr>
							 </thead>
							 <tbody style="background-color: white;" class="balanceSumInsuredAddOn">
							 </tbody>
					 </table>
					
					  <div class="preauth-title">
						<label> Optional Covers </label>
					</div>
					 <table class="fvr-sub-table paInsTables" border="0" cellspacing="0" cellpadding="0" >
							 <thead>
								<tr>
									<th>Cover Description</th>
									<th>Cover SI</th>
									<th>Claim Paid</th>
									<th>Claim Outstanding</th>
									<th>Provision for Current Claim</th>
									<th>Cover Balance</th>
								</tr>
							 </thead>
							 <tbody style="background-color: white;" class="gpaSumInsuredCovers">
							 </tbody>
					 </table>
					
				  </div>
				  <div class="scroll optional_sum_insured">
				  	 <table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
						<thead>
						   <tr>
							 <th> Optional Covers </th>
							 <th> Policy Level Limit </th>
							 <th> Claim Paid </th>
							 <th> Claim Outstanding </th>
							 <th> Balance </th>
							 <th> Provision for Current Claim </th>
							 <th> Balance Sum Insured after Provision </th>
						   </tr>
						</thead>
						<tbody class="optional_sum_insured_tbody">
						</tbody>
					 </table>

				  </div>
				  
				<!-- Negotiation Details Dialog-->
				   <div id="negotiation_details_modal" class="modal negotiation_details_modal custom-modal">				
						<div class="ped-panel-content" style="background-color: #ffffff !important; height:500px;">
							<div class="">
								<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">
									<thead>		
										 <tr>
											<td><strong>Intimation No</strong></td>
											<td><strong>Stage</strong></td>
											<td><strong>Status</strong></td>
											<td><strong>Highest Cashless transaction</strong></td>
											<td><strong>Claim Amount</strong></td>
											<td><strong>Negotiated With</strong></td>
											<td><strong>Negotiated Amount/Discount</strong></td>
											<td><strong>Approved Amount</strong></td>
											<td><strong>Saved Amount</strong></td>
											<td><strong>Total Negotiated Savings</strong></td>							
										 </tr>
									  </thead>
								  <tbody class="negotiation_details_tbody">
									
								  </tbody>
							   </table>
							</div>
						</div>
					 </div>
			   </div>
			</div>				 

				 </div>
            		</div>
       		 	</div>
   		 	</div>
		</div> 
</div>	

		
		   
		</div>
		<input type="text" style="border: none;visibility: hidden;" id="getDmsViewUrl" value="${dmsViewUrl}">
		<input type="text" style="border: none;visibility: hidden;" id="getIntimationCreateDate" value="${intimation.getCreatedDate()}">
		<input type="text" style="border: none;visibility: hidden;" id="getAdmissionDate" value="${intimation.getAdmissionDate()}">
   </body>
   <script type="text/javascript">
		$(document).ready(function() {
			var authToken = getUrlParameter("token");
			var scale = 2; 
			
			var ict = String(document.getElementById('getIntimationCreateDate').value);
			document.getElementById('setIntimationCreateDate').innerHTML = ict.charAt(8) + "" + ict.charAt(9) + "/" + ict.charAt(5) + "" + ict.charAt(6) + "/" + ict.charAt(0) + "" + ict.charAt(1) + "" + ict.charAt(2) + "" + ict.charAt(3) + " " + ict.charAt(11) + "" + ict.charAt(12) + ":" + ict.charAt(14) + "" + ict.charAt(15) + ":" + ict.charAt(17) + "" + ict.charAt(18);			

			var adt = String(document.getElementById('getAdmissionDate').value);
			document.getElementById('setAdmissionDate').innerHTML = adt.charAt(8) + "" + adt.charAt(9) + "/" + adt.charAt(5) + "" + adt.charAt(6) + "/" + adt.charAt(0) + "" + adt.charAt(1) + "" + adt.charAt(2) + "" + adt.charAt(3) + "" + adt.charAt(4);
			
			$(window).load(function() {				
							
				$('#intimation-dialog').dialog({
					modal: true,
					autoOpen: false,
					width: '85%',
					resizable: true,
					title: 'View Intimation Details'
		        }).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});

				$('#query_details_modal').dialog({
					modal: true,
					autoOpen: false,
					width: '85%',
					resizable: true,
					title: 'View Query Details'
				}).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				$("#hospitals-details-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: '85%',
					resizable: true,
					title: 'Hospital Details'
		        }).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				$("#copay-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: '50%',
					resizable: true,
					title: 'Co-pay Details'
				}).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				$("#fvr-detail-modal").dialog({
					modal: true,
					autoOpen: false,
					width: '85%',
					resizable: true,
					title: 'FVR Details'
		        }).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				$("#claim_history_modal").dialog({
					modal: true,
					autoOpen: false,
					width: '85%',
					resizable: true,
					title: 'History'
		        }).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				$("#doctor-trails-modal").dialog({
					modal: true,
					autoOpen: false,
					width: 'auto',
					resizable: true,
					title: 'Doctor Note (Trails)'
		        }).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				$("#PED-request-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "95%",
					resizable: true,
					title: 'PED Request Details'
		        }).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				/*$("#dialog-modal-thirteen").dialog({
					modal: true,
					autoOpen: false,
					width: 'auto',
					resizable: true
				});*/
				$("#preauth-details-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#acknowledge-details-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#coordinator-reply-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#specialist-trail-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#sublimits-details-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#hrm-details-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#risk-details-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#previous-insurance-details-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#balance-sum-dialog").dialog({
					modal: true,
					autoOpen: false,
					width: "80%",
					resizable: true
				}).prev(".ui-dialog-titlebar").css("background","#003366");
				
				$("#negotiation_details_modal").dialog({
					modal: true,
					autoOpen: false,
					width: '85%',
					resizable: true,
					title: 'Negotiation Details'
		        }).prev(".ui-dialog-titlebar").css({"background":"#003366","color": "White"});
				
				$('#gobutton').click(function() {
					if ($('#selectbox').val() == "intimation") {
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Intimation"								
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									if (data.intimation) {										
										var intimation = data.intimation;
										$('#intNo').html((intimation.intimationNo?intimation.intimationNo:""));
										$('#intPolNo').html((intimation.policyNo?intimation.policyNo:""));
										$('#intDate').html((intimation.dateAndTime?intimation.dateAndTime:""));
										$('#intIssueOffice').html((intimation.issueOffice?intimation.issueOffice:""));
										$('#intCpuCode').html((intimation.cpuCode?intimation.cpuCode:""));
										$('#intProductName').html((intimation.productName?intimation.productName:""));
										$('#intMode').html((intimation.intimationMode?intimation.intimationMode:""));
										$('#intProposerName').html((intimation.proposerName?intimation.proposerName:""));
										$('#intIntimatedBy').html((intimation.intimatedBy?intimation.intimatedBy:""));
										$('#intState').html((intimation.state?intimation.state:""));
										$('#intPatientName').html((intimation.patientName?intimation.patientName:""));
										$('#intCity').html((intimation.city?intimation.city:""));
										$('#intIsCovered').html((intimation.isPatientCovered == true ? '<input disabled type="checkbox" checked />Patient Not Covered' : '<input disabled type="checkbox"  />Patient Not Covered'));
										$('#intArea').html((intimation.area?intimation.area:""));
										$('#intHealthCardNo').html((intimation.healthCardNo?intimation.healthCardNo:""));
										$('#intHospName').html((intimation.hospName?intimation.hospName:""));
										$('#intName').html((intimation.name?intimation.name:""));
										$('#intAddress').html((intimation.address?intimation.address:""));
										$('#intRelation').html((intimation.relationship?intimation.relationship:""));
										$('#intAdDate').html((intimation.admissionDate?intimation.admissionDate:""));
										$('#intAdType').html((intimation.admissionType?intimation.admissionType:""));
										$('#intInpatient').html((intimation.inpatient?intimation.inpatient:""));
										$('#intHosptype').html((intimation.hospType?intimation.hospType:""));
										$('#intLateIntimation').html((intimation.lateIntimation?intimation.lateIntimation:""));
										$('#intHospCode').html((intimation.hospCode?intimation.hospCode:""));
										$('#intAdReason').html((intimation.admissionReason?intimation.admissionReason:""));
										$('#intHospCodeIrda').html((intimation.irdaHospCode?intimation.irdaHospCode:""));
										$('#intComments').html((intimation.comments?intimation.comments:""));
										$('#intSmCode').html((intimation.smCode?intimation.smCode:""));
										$('#intSmName').html((intimation.smName?intimation.smName:""));
										$('#intBrokerCode').html((intimation.brokerCode?intimation.brokerCode:""));
										$('#intBrokerName').html((intimation.brokerName?intimation.brokerName:""));
									}									
									$('#intimation-dialog').dialog('open');
								} else if (xhr.status == 204) {
									$('#intimation-dialog').dialog('open');
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}			
					}

					if ($('#selectbox').val() == "queryDetails") {
						$('#query_details_modal').dialog('open');
					}

					if ($('#selectbox').val() == "policySchedule") {
						var url = $('#getPolicyUrl').val();					
						if(url) {
							window.open(url, "popUp", "resizable=yes");							
						}							
					}

					if ($('#selectbox').val() == "hospDetails") {
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Hospitals"								
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									$('#hosp_name').html((data.hospName?data.hospName:""));
									$('#hosp_code').html((data.hospCode?data.hospCode:""));
									$('#hosp_phone').html((data.hospPhoneNo?data.hospPhoneNo:""));
									$('#hosp_address').html((data.address?data.address:""));
									$('#hosp_rep').html((data.authRep?data.authRep:""));
									$('#hosp_city').html((data.city?data.city:""));
									$('#hosp_rep_name').html((data.repName?data.repName:""));
									$('#hosp_state').html((data.state?data.state:""));
									$('#hosp_category').html((data.hospCategory?data.hospCategory:""));
									$('#hosp_pin').html((data.pincode?data.pincode:""));
									$('#hosp_room_category').html((data.roomCategory?data.roomCategory:""));
									$('#hosp_package_url').html((data.packageUrl? '<a href="'+data.packageUrl + '" class="hreflink" target="_blank"> View Package Rates </a>' :""));
									$('#hosp_remarks').html((data.remarks?data.remarks:""));
									$("table.riskDetailsTable tr:even").css("background-color", "#c5e0dc");									
									$('#hospitals-details-dialog').dialog('open');
								} else if (xhr.status == 204) {
									$('#hospitals-details-dialog').dialog('open');
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}
					}
					
					if ($('#selectbox').val() == "copayDetails") {
						$("table.riskDetailsTable tr:even").css("background-color", "#c5e0dc");		
						$('#copay-dialog').dialog('open');
					}
					if ($('#selectbox').val() == "fvrDetails") {
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "FVR Details"								
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									console.log("FVR Details response formation initiated");
									$('.fvr-details-tbody').html('');
									if (data.fvrDetails) {										
										var fvrDetails = data.fvrDetails;
										for (var i = 0; i < fvrDetails.length; i++) {
											$('.fvr-details-tbody').append('<tr id="fvr_' + (i + 1) +'" data-grading="" data-json="" 	><td>' + (i + 1) + '</td><td>' +
												(fvrDetails[i].representativeName ? fvrDetails[i].representativeName : '') + '</td><td>' + (fvrDetails[i].representativeCode ? fvrDetails[i].representativeCode : '') + '</td><td>' + (fvrDetails[i].representativeContactNo ? fvrDetails[i].representativeContactNo : '') + '</td><td>' + (fvrDetails[i].hospitalName ? fvrDetails[i].hospitalName : '') + '</td><td>' +
												(fvrDetails[i].hospitalVisitedDate ? fvrDetails[i].hospitalVisitedDate : '') + '</td><td>' + (fvrDetails[i].remarks ? fvrDetails[i].remarks : '') + '</td><td>' + (fvrDetails[i].fvrassignedDate ? fvrDetails[i].fvrassignedDate : '') + '</td><td>' + (fvrDetails[i].fvrReceivedDate ? fvrDetails[i].fvrReceivedDate : '') + '</td><td>' +
												(fvrDetails[i].fvrTat ? fvrDetails[i].fvrTat : '') + '</td><td>' + (fvrDetails[i].status ? fvrDetails[i].status : '') + '</td><td>' + (fvrDetails[i].fvrGrading ? fvrDetails[i].fvrGrading : '') + '</td><td><a class="hreflink fvrGradingBtn" > View FVR Grading </a></td></td><td><a class="hreflink fvrViewDocBtn">View Document</a></td><tr>');
												$("#fvr_" + (i + 1)).attr('data-grading', JSON.stringify(fvrDetails[i]));
												$("#fvr_" + (i + 1)).attr('data-json', JSON.stringify(fvrDetails[i].dmsDocumentList));

										}
									}
									console.log("FVR Details response formation completed");									
									$('#fvr-detail-modal').dialog('open');
								} else if (xhr.status == 204) {
									alert("Details not found");
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}						
					}

					if ($('#selectbox').val() == "history") {
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "History"								
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									$('.claim_history_tbody').html('');
									if (data.claimHistory) {										
										var history = data.claimHistory;
										for (var i = 0; i < history.length; i++) {
											$('.claim_history_tbody').append('<tr><td>' + (history[i].typeofClaim ? history[i].typeofClaim : '') + '</td><td>' + (history[i].docrecdfrom ? history[i].docrecdfrom : '') + '</td><td>' + (history[i].rodtype ? history[i].rodtype : '') + '</td><td>' + (history[i].classification ? history[i].classification : '') + '</td><td>' + (history[i].referenceNo ? history[i].referenceNo : '') + '</td><td>' + (history[i].dateAndTime ? history[i].dateAndTime : '') + '</td><td>' + (history[i].userID ? history[i].userID : '') + '</td><td>' + (history[i].userName ? history[i].userName : '') + '</td><td>' + (history[i].claimStage ? history[i].claimStage : '')  + '</td><td>' + (history[i].status ? history[i].status : '') +  '</td><td>' + (history[i].userRemark ? history[i].userRemark : '') + '</td></tr>');
										}
									}									
									$('#claim_history_modal').dialog('open');
								} else if (xhr.status == 204) {
									$('#claim_history_modal').dialog('open');
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}						
					}
					
					if ($('#selectbox').val() == "preauthDetails") {	
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Pre Auth Details",
								preIntimationKey : 0
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									if(data.preAuthIntimationKeys) {
										$('.preselect').html('');
										$.each(data.preAuthIntimationKeys, function(key, value) {
											$('.preselect').append('<option value="' + key + '">' + value + '</option>' );
										});
									}
									if (data.preAuthDetails) {
										getPreAuthDetails(data.preAuthDetails);
									}
									$('#preauth-details-dialog').dialog('open');
								} else if (xhr.status == 204) {
									alert("Details not found");
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}						
					}

					if ($('#selectbox').val() == "doctorNote") {
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Doctor Notes"								
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									$('.doctor-trails-tbody').html('');
									if (data.doctorNotes) {										
										var docNotes = data.doctorNotes;
										for (var i = 0; i < docNotes.length; i++) {
											$('.doctor-trails-tbody').append('<tr><td>' + (docNotes[i].strNoteDate ? docNotes[i].strNoteDate : '') + '</td><td>' + (docNotes[i].userId ? docNotes[i].userId : '') + '</td><td>' + (docNotes[i].transaction ? docNotes[i].transaction : '') + '</td><td>' + (docNotes[i].transactionType ? docNotes[i].transactionType : '') + '</td><td>' + (docNotes[i].remarks ? docNotes[i].remarks : '') + '</td><tr>');
										}
									}									
									$('#doctor-trails-modal').dialog('open');
								} else if (xhr.status == 204) {
									alert("Details not found");
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}						
					}

					if ($('#selectbox').val() == "pedRequest") {
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "PED Request"								
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									$('.ped-request-detail-tbody').html('');
									if (data.pedRequestDetails) {										
										var pedDetails = data.pedRequestDetails;
										for (var i = 0; i < pedDetails.length; i++) {
											$('.ped-request-detail-tbody').append('<tr id="ped_' + (i + 1) + '" data-json="" data-ammended=""  data-processed="" data-reviewed="" data-approved="" data-history=""><td>' + (i + 1) + '</td><td>' + (pedDetails[i].intimationNo ? pedDetails[i].intimationNo : '') + '</td><td>' + (pedDetails[i].pedSuggestionName ? pedDetails[i].pedSuggestionName : '') + '</td><td>' + (pedDetails[i].pedName ? pedDetails[i].pedName : '') + '</td><td>' + (pedDetails[i].repLetterDate ? pedDetails[i].repLetterDate : '') + '</td><td>' + (pedDetails[i].remarks ? pedDetails[i].remarks : '') + '</td><td>' + (pedDetails[i].requestorId ? pedDetails[i].requestorId : '') + '</td><td>' + (pedDetails[i].requestedDate ? pedDetails[i].requestedDate : '') + '</td><td>' + (pedDetails[i].requestStatus ? pedDetails[i].requestStatus : '') + '</td><td><a class="hreflink pedViewBtn" > View Details </a></td><td><a class="hreflink pedHistoryBtn">View Trails</a></td><tr>');
											$("#ped_" + (i + 1)).attr('data-json', JSON.stringify(pedDetails[i]));
											if (pedDetails[i].pedAmmended) {
												$("#ped_" + (i + 1)).attr('data-ammended', JSON.stringify(pedDetails[i].pedAmmended));
											}
											if (pedDetails[i].pedProcessed) {
												$("#ped_" + (i + 1)).attr('data-processed', JSON.stringify(pedDetails[i].pedProcessed));
											}
											if (pedDetails[i].pedReviewed) {
												$("#ped_" + (i + 1)).attr('data-reviewed', JSON.stringify(pedDetails[i].pedReviewed));
											}
											if (pedDetails[i].pedApproved) {
												$("#ped_" + (i + 1)).attr('data-approved', JSON.stringify(pedDetails[i].pedApproved));
											}
											if (pedDetails[i].pedHistoryList) {
												$("#ped_" + (i + 1)).attr('data-history', JSON.stringify(pedDetails[i].pedHistoryList));
											}
										}
									}									
									$('#PED-request-dialog').dialog('open');
								} else if (xhr.status == 204) {
									$('#PED-request-dialog').dialog('open');
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}					
						
					}

					if ($('#selectbox').val() == "portability") {
						$('#dialog-modal-thirteen').dialog('open');
					}
					
					if ($('#selectbox').val() == "policyDocs") {						
						var url = $('#getDmsViewUrl').val();					
						if(url) {
							window.open(url, "popUp", "resizable=yes");							
						}				
					}
					
					if ($('#selectbox').val() == "ackDetails") {
						$('.overlay').show();
						if	(authToken)
							$.get('crcportal', {
								token : authToken,
								pageName : "Acknowledgment Details"
							}, function(data, statusText, xhr) {
								if(xhr.status == 200) {
									var showReconsiderTable = false;
									if	(data.documentValues) {
										$('#ackNo').html((data.documentValues.acknowledgementNumber ? data.documentValues.acknowledgementNumber :''));
										$('#ackCreatedById').html((data.documentValues.acknowledgmentCreatedId ? data.documentValues.acknowledgmentCreatedId : ''));
										$('#ackCreatedOn').html((data.documentValues.acknowledgmentCreateOn ? data.documentValues.acknowledgmentCreateOn : ''));
										$('#ackCreatedByName').html((data.documentValues.acknowledgmentCreatedName ? data.documentValues.acknowledgmentCreatedName : ''));
										$('#ackRcvdFrom').html((data.documentValues.documentReceivedFromValue?data.documentValues.documentReceivedFromValue:''));
										$('#ackEmailId').html((data.documentValues.emailId ? data.documentValues.emailId : ''));
										$('#ackRcvdDate').html((data.documentValues.documentReceivedDate ? data.documentValues.documentReceivedDate :''));
										$('#ackContact').html((data.documentValues.acknowledgmentContactNumber ? data.documentValues.acknowledgmentContactNumber : ''));
										$('#ackRecipt').html((data.documentValues.modeOfReceiptValue ? data.documentValues.modeOfReceiptValue : ''));
										$('#ackReconsider').html((data.documentValues.reconsiderationRequestValue ? data.documentValues.reconsiderationRequestValue : ''));
										
										if(data.documentValues.reconsiderationRequestValue == "Yes") {
											showReconsiderTable = true;
										}
										
										$('.billRow1').html((data.documentValues.hospitalizationFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Hospitalization</td>' : '<td><input  type="checkbox" disabled />Hospitalization</td>') + (data.documentValues.preHospitalizationFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Pre-Hospitalization</td>' : '<td><input  type="checkbox" disabled />Pre-Hospitalization</td>') + (data.documentValues.postHospitalizationFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Post-Hospitalization</td>' : '<td><input  type="checkbox" disabled />Post-Hospitalization</td>') +  (data.documentValues.partialHospitalizationFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Partial Hospitalization</td>' : '<td><input  type="checkbox" disabled />Partial Hospitalization</td>') + (data.documentValues.hospitalizationRepeat == 'Y'? '<td><input  type="checkbox" checked disabled />Hospitalisation(Repeat)</td>' : '<td><input  type="checkbox" disabled />Hospitalisation(Repeat)</td>'));

										$('.billRow2').html((data.documentValues.lumpSumAmountFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Lumpsum Amount</td>' : '<td><input  type="checkbox" disabled />Lumpsum Amount</td>') + (data.documentValues.hospitalExpensesCoverFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Add on Benefits (Hospital cash)</td>' : '<td><input  type="checkbox" disabled />Add on Benefits (Hospital cash)</td>') + (data.documentValues.hospitalExpensesCoverFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Add on Benefits (Patient Care)</td>' : '<td><input  type="checkbox" disabled />Add on Benefits (Patient Care)</td>') +  (data.documentValues.hospitalizationRepeatFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Hospitalisation(Repeat)</td>' : '<td><input  type="checkbox" disabled />Hospitalisation(Repeat)</td>') );

										$('.billRow3').html((data.documentValues.emergencyMedicalEvaluationFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Emergency Medical Evacuation</td>' : '<td><input  type="checkbox" disabled />Emergency Medical Evacuation</td>') + (data.documentValues.compassionateTravelFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Compassionate Travel</td>' : '<td><input  type="checkbox" disabled />Compassionate Travel</td>') + (data.documentValues.repatriationOfMortalRemainsFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Repatriation Of Mortal Remains</td>' : '<td><input  type="checkbox" disabled />Repatriation Of Mortal Remains</td>'));

										$('.billRow4').html((data.documentValues.preferredNetworkHospitalFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Preferred Network Hospital</td>' : '<td><input  type="checkbox" disabled />Preferred Network Hospital</td>') + (data.documentValues.sharedAccomodationFlag == 'Y'? '<td><input  type="checkbox" checked disabled />Shared Accomodation</td>' : '<td><input  type="checkbox" disabled />Shared Accomodation</td>'));
									}
									
									if(data.ackCheckList) {
										$('.ackChecklistBody').html('');
										var checklist = data.ackCheckList;
										for (var i = 0; i < checklist.length; i++) {
											$('.ackChecklistBody').append('<tr><td>' + (i + 1) + '</td><td style="width: 30%">' + (checklist[i].value ? checklist[i].value : '') + '</td><td>' + (checklist[i].receivedStatus ? checklist[i].receivedStatus.value : '') + '</td><td>' + (checklist[i].noOfDocuments ? checklist[i].noOfDocuments : '') + '</td><td>' + (checklist[i].remarks ? checklist[i].remarks : '') + '</td></tr>');
										}
									}
									
									if(data.ackQueryList) {
										$('.ackQueryDetailsBody').html('');
										var queryList = data.ackQueryList;
										for (var i = 0; i < queryList.length; i++) {
											$('.ackQueryDetailsBody').append('<tr id="ack_' + (i + 1) +'" data-key="" data-dms=""><td>' + (i + 1) + '</td><td style="width: 30%">' + (queryList[i].rodNo ? queryList[i].rodNo : '') + '</td><td>' + (queryList[i].billClassification ? queryList[i].billClassification : '') + '</td><td>' + (queryList[i].diagnosis ? queryList[i].diagnosis : '') + '</td><td>' + (queryList[i].claimedAmount ? queryList[i].claimedAmount : '') + '</td><td>' + (queryList[i].queryRaisedRole ? queryList[i].queryRaisedRole : '') + '</td><td>' + (queryList[i].queryRaisedDateString ? queryList[i].queryRaisedDateString : '') + 
											'</td><td>' + (queryList[i].queryStatus ? queryList[i].queryStatus : '') + '</td><td>' + (queryList[i].queryReplyStatus ? queryList[i].queryReplyStatus : '') + '</td><td><a class="ackDetailBtn hreflink" >View Details </a></td></tr>');
											$("#ack_" + (i + 1)).attr('data-key', JSON.stringify(queryList[i].queryDetails));
											$("#ack_" + (i + 1)).attr('data-dms', JSON.stringify(queryList[i].dmsDocumentDto));
										}
									}
									
									if(showReconsiderTable == true && data.ackReconsiderRODList) {
										$('.ackReconsiderROD').removeClass('hidden');
										$('.ackRODBody').html('');
										var rodList = data.ackReconsiderRODList;
										for (var i = 0; i < rodList.length; i++) {
											$('.ackRODBody').append('<tr><td>' + (rodList[i].rodNo ? rodList[i].rodNo : '') + '</td><td>' + (rodList[i].claimedAmt ? rodList[i].claimedAmt : '') + '</td><td>' + (rodList[i].approvedAmt ? rodList[i].approvedAmt : '') + '</td><td>' + (rodList[i].rodStatus ? rodList[i].rodStatus : '') + '</td><td></td></tr>');
										}
									} else {
										$('.ackReconsiderROD').addClass('hidden');
									}
									
									$('#acknowledge-details-dialog').dialog('open');	
																		
								} else if (xhr.status == 204) {
									alert("Details not found");
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
					    
					}
					
					if ($('#selectbox').val() == "coorReply") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Coordinator Reply"
								}, function(data, statusText, xhr) {
									if(xhr.status == 200) {
										$('.coordinator-reply-body').html('');
										if	(data.coordinateDetails) {
											var coorDetail = data.coordinateDetails;
											for(var i = 0; i < coorDetail.length; i++) {
												$('.coordinator-reply-body').append('<tr id="coor_' + (i + 1) +'" data-docs="" data-name="coordinator"><td>' + (i + 1) + '</td><td>' + (coorDetail[i].requestedDate ? coorDetail[i].requestedDate : '') + '</td><td>' +
												(coorDetail[i].repliedDate ? coorDetail[i].repliedDate : '') + '</td><td>' + (coorDetail[i].requestType ? coorDetail[i].requestType : '') + '</td><td>' + (coorDetail[i].requestorRole ? coorDetail[i].requestorRole : '') + '</td><td>' + (coorDetail[i].requestroNameId ? coorDetail[i].requestroNameId : '') + '</td><td>' +
												(coorDetail[i].requestorRemarks ? coorDetail[i].requestorRemarks : '') + '</td><td><a class="viewFileBtn hreflink" > View File </a></td> <td>' +
												(coorDetail[i].coOrdinatorRepliedId ? coorDetail[i].coOrdinatorRepliedId : '') + '</td><td>' +
												(coorDetail[i].coOrdinatorRemarks ? coorDetail[i].coOrdinatorRemarks : '') + '</td><tr>');
												$("#coor_" + (i + 1)).attr('data-docs', JSON.stringify(coorDetail[i].uploadedDocumentList));
											}
										}
										$('#coordinator-reply-dialog').dialog('open');										
									} else if (xhr.status == 204) {
										alert("Details not found");
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});
						}
						
					}
					
					if ($('#selectbox').val() == "specilaistTrail") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Specialist Trail"
								}, function(data, statusText, xhr) {
									if(xhr.status == 200) {
										$('.specialist-trail-body').html('');
										if	(data.specialistOpinionDetails) {
											var splDetail = data.specialistOpinionDetails;
											for(var i = 0; i < splDetail.length; i++) {
												$('.specialist-trail-body').append('<tr id="spl_' + (i + 1) +'" data-docs="" data-name="specialist"><td>' + (i + 1) + '</td><td>' + (splDetail[i].requestedDate ? splDetail[i].requestedDate : '') + '</td><td>' +
												(splDetail[i].repliedDate ? splDetail[i].repliedDate : '') + '</td><td>' + (splDetail[i].specialistType ? splDetail[i].specialistType : '') + '</td><td>' + (splDetail[i].specialistDrNameId ? splDetail[i].specialistDrNameId : '') + '</td><td>' + (splDetail[i].requestorNameId ? splDetail[i].requestorNameId : '') + '</td><td>' +
												(splDetail[i].requestorRemarks ? splDetail[i].requestorRemarks : '') + '</td><td><a class="viewFileBtn hreflink" > View File </a></td> <td>' +
												(splDetail[i].specialistRemarks ? splDetail[i].specialistRemarks : '') + '</td><tr>');
												$("#spl_" + (i + 1)).attr('data-docs', JSON.stringify(splDetail[i].uploadedDocumentList));
											}
										}
										$('#specialist-trail-dialog').dialog('open');	
									} else if (xhr.status == 204) {
										alert("Details not found");
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});
						}
					}
					
					if ($('#selectbox').val() == "claimDocs") {
						var url = $("#selectbox option:selected").attr("data-url");					
						if(url) {
							window.open(url, "popUp", "resizable=yes");							
						}					
					}
					
					/*if ($('#selectbox').val() == "64vbCompliance") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "64Vb Compliance"
								}, function(data, statusText, xhr) {
									if (xhr.status == 200) {
										if(data.filePath) {		
											var fileName = "64vbCompliance";										
												if (window.navigator && window.navigator.msSaveOrOpenBlob) {
													var pdfData = base64ToUint8Array(data.filePath);
													loadPdf(pdfData); 
													document.getElementsByClassName('64VBDocumentViewModal')[0].style.display = "block";
												} else { 
												$('#frame').attr("src", "data:application/pdf;base64, " + data.filePath);
												$('.frame-title').html('64 VB Compliance Report');
												document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "block";
											}		
										}
									} else if (xhr.status == 204) {
										alert("Details not found");
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});	
						}								
					}*/
					
					if ($('#selectbox').val() == "sublimits") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Sublimits"
								}, function(data, statusText, xhr) {
									if (xhr.status == 200) {
										$('.sublimits-details-body').html('');
										if	(data.sublimitDetails) {
											var subDetail = data.sublimitDetails;
											for(var i = 0; i < subDetail.length; i++) {
												if(subDetail[i].subLimitAmt) {
													$('.sublimits-details-body').append('<tr><td>' + (i + 1) + '</td><td>' + (subDetail[i].section ? subDetail[i].section : '') + '</td><td>' +
													(subDetail[i].subLimitName ? subDetail[i].subLimitName : '') + '</td><td>' + (subDetail[i].subLimitAmt ? addCommas(subDetail[i].subLimitAmt) : '0') + '</td><td>' + (subDetail[i].includingCurrentClaimAmt ? addCommas(subDetail[i].includingCurrentClaimAmt) : '0') + '</td><td>' + (subDetail[i].includingCurrentClaimBal ? addCommas(subDetail[i].includingCurrentClaimBal) : '') + '</td><td>' +
													(subDetail[i].excludingCurrentClaimAmt ? addCommas(subDetail[i].excludingCurrentClaimAmt) : '0') + '</td> <td>' +
													(subDetail[i].excludingCurrentClaimBal ? addCommas(subDetail[i].excludingCurrentClaimBal) : '') + '</td><tr>');
												}												
											}
											$('#sublimits-details-dialog').dialog('open');	
										}
									} else if (xhr.status == 204) {
										alert("Details not found");
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});	
						}								
					}
					
					if ($('#selectbox').val() == "hrmDetails") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Hrm Details"
								}, function(data, statusText, xhr) {
									$('.hrm-diagnosis-body').html('');
									if (xhr.status == 200) {
										if (data) {
											$('#hrmIntimationno').html((data.intimationNo?data.intimationNo:''));
											$('#hrmHospName').html((data.hospitalName? data.hospitalName: ''));
											$('#hrmId').html((data.hrmId? data.hrmId:''));
											$('#hrmPhone').html((data.hrmPhone?data.hrmPhone:''));
											$('#hrmName').html((data.hrmName?data.hrmName:''));
											$('#hrmEmailId').html((data.hrmEmail?data.hrmEmail:''));
											
											if (data.hrmDiagnosisList) {
												var hrmDetail = data.hrmDiagnosisList;
												for(var i = 0; i < hrmDetail.length; i++) {
													$('.hrm-diagnosis-body').append('<tr><td>' + (hrmDetail[i].anhOrNanh ? hrmDetail[i].anhOrNanh : '') + '</td><td>' + (hrmDetail[i].diagnosis ? hrmDetail[i].diagnosis : '') + '</td><td>' + (hrmDetail[i].surgicalProcedure ? hrmDetail[i].surgicalProcedure : '') + '</td><td>' + (hrmDetail[i].claimedAmt ? hrmDetail[i].claimedAmt : '') + '</td><td>' +
													(hrmDetail[i].packageAmt ? hrmDetail[i].packageAmt : '') + '</td> <td>' +(hrmDetail[i].requestTypeValue ? hrmDetail[i].requestTypeValue : '') + '</td><td>' +
													(hrmDetail[i].docRemarks ? hrmDetail[i].docRemarks : '') + '</td> <td>' +
													(hrmDetail[i].assigneeDateAndTimeStr ? hrmDetail[i].assigneeDateAndTimeStr : '') + '</td><td>' + (hrmDetail[i].hrmReplyRemarks ? hrmDetail[i].hrmReplyRemarks : '') + '</td><td>' + (hrmDetail[i].replyDateAndTime ? hrmDetail[i].replyDateAndTime : '') + '</td> <td>' + (hrmDetail[i].docUserId ? hrmDetail[i].docUserId : '') + '</td><td>' +  (hrmDetail[i].docName ? hrmDetail[i].docName : '') + '</td> <td>' + (hrmDetail[i].docDeskNumber ? hrmDetail[i].docDeskNumber : '') + '</td><tr>');
												}												
											}
										}
										$('#hrm-details-dialog').dialog('open');	
									} else if (xhr.status == 204) {
										alert("Details not found");
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});	
						}								
					}
					
					if ($('#selectbox').val() == "riskDetails") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Risk Details"
								}, function(data, statusText, xhr) {
									if (xhr.status == 200) {
										if (data) {
											$('#riskPolicyNo').html((data.policyNo != null ? data.policyNo : ''));
											$('#riskName').html((data.policyName != null ? data.policyName : ''));
											$('#riskPolicyFrom').html((data.policyFromDate != null ? data.policyFromDate : ''));
											$('#riskSumInsured').html((data.sumInsured != null ? data.sumInsured : ''));
											$('#riskPolicyTo').html((data.policyToDate != null ? data.policyToDate : ''));
											$('#riskRelationship').html((data.relationship != null ? data.relationship : ''));
											$('#riskAge').html((data.age != null ? data.age : ''));
											$('#riskPED').html((data.riskPED != null ? data.riskPED : ''));
											$('#riskPortalPED').html((data.portalPED != null ? data.portalPED : ''));
										}									
											$("table.riskDetailsTable tr:even").css("background-color", "#c5e0dc");
											$('#risk-details-dialog').dialog('open');	
									} else if (xhr.status == 204) {
										alert("Details not found");
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});	
						}								
					}	
					
					
					if ($('#selectbox').val() == "previousInsurance") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Previous Insurance Details"
								}, function(data, statusText, xhr) {
									if (xhr.status == 200) {
										$('.previous-insurance-table-body').html('');	
										if (data.prevInsuranceDetails) {
											var insuranceDetail = data.prevInsuranceDetails;
												for(var i = 0; i < insuranceDetail.length; i++) {
													$('.previous-insurance-table-body').append('<tr id="prevIns_'+ (i + 1) +'" data-json=""><td>' + (insuranceDetail[i].previousInsurerName ? insuranceDetail[i].previousInsurerName : '') + '</td><td>' + (insuranceDetail[i].policyNumber ? insuranceDetail[i].policyNumber : '') + '</td><td>' + (insuranceDetail[i].policyFromDate ? insuranceDetail[i].policyFromDate : '') + '</td><td>' + (insuranceDetail[i].policyToDate ? insuranceDetail[i].policyToDate : '') + '</td><td>' + (insuranceDetail[i].underWritingYear ? insuranceDetail[i].underWritingYear : '') + '</td> <td>' +(insuranceDetail[i].sumInsured ? insuranceDetail[i].sumInsured : '') + '</td><td>' + (insuranceDetail[i].productName ? insuranceDetail[i].productName : '') + '</td><td><a class="prevPolicyView hreflink" data-url="' + (insuranceDetail[i].policyScheduleUrl ? insuranceDetail[i].policyScheduleUrl : '') +'"> View Policy Schedule </a></td></td><td><a class="prevInsDetails hreflink"> View Insured Details </a></td><tr>');
													$("#prevIns_" + (i + 1)).attr('data-json', JSON.stringify(insuranceDetail[i].previousInsuredDetails));
													
												}												
											}										
										$('#previous-insurance-details-dialog').dialog('open');	
									} else if (xhr.status == 204) {
										$('#previous-insurance-details-dialog').dialog('open');	
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});	
						}	
					}
					if ($('#selectbox').val() == "sumInsured") {
						$('.overlay').show();
						if(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Balance Sum Insured"
								}, function(data, statusText, xhr) {
									if (xhr.status == 200) {										
										if (data.isPASumInsured == false) {
											$('#blPolicyNo').html((data.policyNo ? data.policyNo : ''));
											$('#blIntNo').html((data.intimatioNo ? data.intimatioNo : ''));
											$('#blInsName').html((data.insuredName ? data.insuredName : ''));
											$('#blProductCode').html((data.productCode ? data.productCode : ''));
											$('#blProductName').html((data.productName ? data.productName : ''));
											$('#blOriginalSI').html((data.originalSI ? data.originalSI : ''));
											$('#blRestoredSI').html((data.restoredSI ? data.restoredSI : ''));
											$('#blCumBonus').html((data.cummulativeBonus ? data.cummulativeBonus : ''));
											$('#blRechargedSI').html((data.rechargedSI ? data.rechargedSI : ''));
											$('#blLimit').html((data.limitcoverage ? data.limitcoverage : ''));	
											
											if(data.isGmc == true) {
												$('.blSecCode').removeClass('hidden');											
												$('#blSecCode').html((data.gmcPolicySecCode ? data.gmcPolicySecCode : ''));
												if(data.restoredSumInsured >= 1){
													$('.provisionClaim').removeClass('hidden');
													$('.siOutStand').removeClass('hidden');
													$('.claimsPaid').removeClass('hidden');
													$('#provisionClaim').html((data.currentClaimProvision ? data.currentClaimProvision : ''));
													$('#siOutStand').html((data.claimsOutStanding ? data.claimsOutStanding : ''));
													$('#claimsPaid').html((data.claimsPaid ? data.claimsPaid : ''));
												}
											} else {
												$('.blSecCode').addClass('hidden'); 	
												$('.provisionClaim').addClass('hidden');
												$('.siOutStand').addClass('hidden');
												$('.claimsPaid').addClass('hidden');
												$('#blSecCode').html('');	
												$('#siOutStand').html('');
												$('#provisionClaim').html('');
												$('#claimsPaid').html('');													
												if (data.isSuperSurplus == true) {
													$('.definedLimit').removeClass('hidden');
													$('#definedLimit').html((data.definedLimit ? data.definedLimit : ''));
												} else {
													$('.definedLimit').addClass('hidden');
													$('#definedLimit').html('');
												}		   
											}
											
											if(data.isGmcSections == true) {
												$('.gmcMainMember').removeClass('hidden');
												$('.gmcInnerLimitAppl').removeClass('hidden');
												$('.gmcInnerLimitInsured').removeClass('hidden');
												$('.gmcInnerLimitUtilised').removeClass('hidden');
												$('.gmcInnerLimitAval').removeClass('hidden');
												$('#gmcMainMember').html((data.gmcMainMember ? data.gmcMainMember : ''));
												$('#gmcInnerLimitAppl').html((data.gmcInnerLimitAppl ? data.gmcInnerLimitAppl : ''));
												$('#gmcInnerLimitInsured').html((data.gmcInnerLimitInsured ? data.gmcInnerLimitInsured : ''));
												$('#gmcInnerLimitUtilised').html((data.gmcInnerLimitUtilised ? data.gmcInnerLimitUtilised : ''));
												$('#gmcInnerLimitAval').html((data.gmcInnerLimitAval ? data.gmcInnerLimitAval : ''));
											} else {
												$('.gmcMainMember').addClass('hidden');
												$('.gmcInnerLimitAppl').addClass('hidden');
												$('.gmcInnerLimitInsured').addClass('hidden');
												$('.gmcInnerLimitUtilised').addClass('hidden');
												$('.gmcInnerLimitAval').addClass('hidden');
												$('#gmcMainMember').html('');
												$('#gmcInnerLimitAppl').html('');
												$('#gmcInnerLimitInsured').html('');
												$('#gmcInnerLimitUtilised').html('');
												$('#gmcInnerLimitAval').html('');
											}
											
											$("table.sumInsuredTable tr:even").css("background-color", "#c5e0dc");
																				
											$('.sum_insured_tables').html('');
											if (data.hasHospTable == true) {
												$('.sum_insured_tables').append(sumInsuredTableFormat(data.hospTableDetails, 'sectionI'));
											}
											if (data.hasNewBornTable == true) {
												$('.sum_insured_tables').append(sumInsuredTableFormat(data.newBornTableDetails, 'sectionII'));
											}
											if (data.hasOutPatientTable == true) {
												$('.sum_insured_tables').append(sumInsuredTableFormat(data.outPatientTableDetails, 'sectionIII'));
											}
											if (data.hasHospCashTable == true) {
												$('.sum_insured_tables').append(sumInsuredTableFormat(data.hospCashTableDetails, 'sectionIV'));
											}
											if (data.hasHealthCheckTable == true) {
												$('.sum_insured_tables').append(sumInsuredTableFormat(data.healthCheckTableDetails, 'sectionV'));
											}										
											if (data.hasBariatricTable == true) {
												$('.sum_insured_tables').append(sumInsuredTableFormat(data.bariatricTableDetails, 'sectionVI'));
											}
											if (data.hasLumpSumTable == true) {
												$('.sum_insured_tables').append(sumInsuredTableFormat(data.lumpSumTableDetails, 'sectionVIII'));
											}	

											$('.optional_sum_insured_tbody').html('');
											if(data.hasOptionalTable) {
												var optionalData = data.hasOptionalTable;
												$('.optional_sum_insured_tbody').append('<tr><td>' +(json[i].subCover ?json[i].subCover :'') + '</td><td>' + (json[i].limit ? addCommas(json[i].limit) : '') + '</td><td>'  + (json[i].claimPaid ? addCommas(json[i].claimPaid) :'') + '</td><td>'  + (json[i].claimOutstanding ? addCommas(json[i].claimOutstanding) : '' ) + '</td><td>'  + (json[i].balance ? addCommas(json[i].balance) : '') +'</td><td>' +  (json[i].provisionCurrentClaim ? addCommas(json[i].provisionCurrentClaim) :'') + '</td><td>'  + (json[i].balanceSI?addCommas(json[i].balanceSI) :'') + '</td><tr>' );
											}
											$('.PA_sum_insured_tables').addClass('hidden');
											$('.PA_sum_insured').addClass('hidden');
											$('.sum_insured_tables').removeClass('hidden');
											$('.sum_insured').removeClass('hidden');
											$('.optional_sum_insured').removeClass('hidden');
										} else {
											$("table.sumInsuredTable").css("background-color", "#c5e0dc");	
											$('#blPAInsName').html((data.insuredName ? data.insuredName : ''));
											$('#blPAInsAge').html((data.insuredAge ? data.insuredAge : ''));
											$('.PA_sum_insured_tables').removeClass('hidden');
											$('.PA_sum_insured').removeClass('hidden');
											$('.sum_insured_tables').addClass('hidden');
											$('.sum_insured').addClass('hidden');		
											$('.balanceSumInsured').html('');
											$('.optional_sum_insured').addClass('hidden');
											if (data.balanceSumInsured) {												
												var sumIns = data.balanceSumInsured;
												for(var i = 0; i < sumIns.length; i++) {
													$('.balanceSumInsured').append('<tr><td>' + (sumIns[i].orginalSI ? sumIns[i].orginalSI : '') + '</td><td>' + (sumIns[i].cumulativeBonus ? sumIns[i].cumulativeBonus : '0') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td> <td>' +(sumIns[i].balanceSI ? sumIns[i].balanceSI : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td><tr>');													
												}												
											}
											
											$('.balanceSumInsuredBenefits').html('');
											if (data.balanceSumInsuredBenefits) {
											var sumIns = data.balanceSumInsuredBenefits;
												for(var i = 0; i < sumIns.length; i++) {
													$('.balanceSumInsuredBenefits').append('<tr><td>' + (sumIns[i].coverDesc ? sumIns[i].coverDesc : '') + '</td><td>' + (sumIns[i].orginalSI ? sumIns[i].orginalSI : '0') + '</td><td>' + (sumIns[i].cumulativeBonus ? sumIns[i].cumulativeBonus : '0') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td><tr>');												
												}												
											}
											
											$('.gpaSumInsuredCovers').html('');
											if (data.gpaSumInsuredCovers) {
											var sumIns = data.gpaSumInsuredCovers;
												for(var i = 0; i < sumIns.length; i++) {
													$('.gpaSumInsuredCovers').append('<tr><td>' + (sumIns[i].coverDesc ? sumIns[i].coverDesc : '') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td><tr>');												
												}												
											}
											
											$('.balanceSumInsuredAddOn').html('');
											if (data.balanceSumInsuredAddOn) {
											var sumIns = data.balanceSumInsuredAddOn;
												for(var i = 0; i < sumIns.length; i++) {
													$('.balanceSumInsuredAddOn').append('<tr><td>' + (sumIns[i].coverDesc ? sumIns[i].coverDesc : '') + '</td><td>' + (sumIns[i].totalSI ? sumIns[i].totalSI : '0') + '</td><td>' + (sumIns[i].claimPaid ? sumIns[i].claimPaid : '0') + '</td><td>' + (sumIns[i].claimOutStanding ? sumIns[i].claimOutStanding : '0') + '</td><td>' + (sumIns[i].currentClaim ? sumIns[i].currentClaim : '0') + '</td><td>' + (sumIns[i].availableSI ? sumIns[i].availableSI : '0') + '</td><tr>');												
												}												
											}
											
											
										}
										$('#balance-sum-dialog').dialog('open');	
									} else if (xhr.status == 204) {
										alert("Details not found");
									} else if (xhr.status == 400) {
										alert("Bad Request");
									}
									$('.overlay').hide();
								});	
						}	
					}
					if ($('#selectbox').val() == "viewBonusLogic") {
				
						var url = $('#getBonusLogicUrl').val();					
						if(!url) 
						{
							window.alert("View Bonus Logic is not applicable for this claim");
						}
						else if(url){
							window.open(url, "popUp", "resizable=yes");		
						}	
					}
						
					if ($('#selectbox').val() == "viewnegotiationdetails") {
						$('.overlay').show();
						if	(authToken) {
							$.get('crcportal', {
								token : authToken,
								pageName : "Negotiationdetails"								
							}, function(data, statusText, xhr) {
								if (xhr.status == 200) {
									$('.negotiation_details_tbody').html('');
									if (data.negotiationDetails) {										
										var negotiation = data.negotiationDetails;
										for (var i = 0; i < negotiation.length; i++) {
											$('.negotiation_details_tbody').append('<tr><td>' + (negotiation[i].intimationNo ? negotiation[i].intimationNo : '') + '</td><td>' + (negotiation[i].stage ? negotiation[i].stage : '') + '</td><td>' + (negotiation[i].status ? negotiation[i].status : '') + '</td><td>' + (negotiation[i].hstCLTrans ? negotiation[i].hstCLTrans : '') + '</td><td>' + (negotiation[i].claimedAmt ? negotiation[i].claimedAmt : '') + '</td><td>' + (negotiation[i].negotiationWith ? negotiation[i].negotiationWith : '') + '</td><td>' + (negotiation[i].negotiatedAmt ? negotiation[i].negotiatedAmt : '') + '</td><td>' + (negotiation[i].claimAppAmt ? negotiation[i].claimAppAmt : '') + '</td><td>' + (negotiation[i].savedAmt ? negotiation[i].savedAmt : '')  + '</td><td>' + (negotiation[i].totalNegotiationSaved ? negotiation[i].totalNegotiationSaved : '') +  '</td></tr>');
										}
									}									
									$('#negotiation_details_modal').dialog('open');
								} else if (xhr.status == 204) {
									$('#negotiation_details_modal').dialog('open');
								} else if (xhr.status == 400) {
									alert("Bad Request");
								}
								$('.overlay').hide();
							});
						}						
					}
					
				});

				$(".hospitalpackage").click(function() {
					$("#hospitaldialog").attr('src', $(this).attr("href"));
					$("#hospitaldiv").dialog({
						width: 1000,
						height: 1000,
						modal: true,
						close: function() {
							$("#hospitaldialog").attr('src', "about:blank");
						}
					});
					return false;
				});


			});
			
			/**
			* JS code to display base64-encoded string as pdf in Internet Explorer
			*/
			/*function base64ToUint8Array(base64) {
				var raw = atob(base64); //This is a native function that decodes a base64-encoded string.
				var uint8Array = new Uint8Array(new ArrayBuffer(raw.length));
				for (var i = 0; i < raw.length; i++) {
					uint8Array[i] = raw.charCodeAt(i);
				}

				return uint8Array;
			}

			function loadPdf(pdfData) {
				PDFJS.disableWorker = true; 
				var pdf = PDFJS.getDocument(pdfData);
				pdf.then(renderPdf);
			}

			function renderPdf(pdf) {
			    pdf.getPage(1).then(renderPage);
			}

			function renderPage(page) {
			    var viewport = page.getViewport(scale);
			    var $canvas = jQuery("<canvas></canvas>");

			    //Set the canvas height and width to the height and width of the viewport
			    var canvas = $canvas.get(0);
			    var context = canvas.getContext("2d");
			    canvas.height = viewport.height;
			    canvas.width = viewport.width;

				//Append the canvas to the pdf container div
			    jQuery(".64VBFrame").append($canvas);

			    //The following few lines of code set up scaling on the context if we are on a HiDPI display
			    var outputScale = getOutputScale();
			    if (outputScale.scaled) {
					var cssScale = 'scale(' + (1 / outputScale.sx) + ', ' + (1 / outputScale.sy) + ')';
					CustomStyle.setProp('transform', canvas, cssScale);
					CustomStyle.setProp('transformOrigin', canvas, '0% 0%');

					if ($textLayerDiv.get(0)) {
					    CustomStyle.setProp('transform', $textLayerDiv.get(0), cssScale);
					    CustomStyle.setProp('transformOrigin', $textLayerDiv.get(0), '0% 0%');
					}
				}

				context._scaleX = outputScale.sx;
				context._scaleY = outputScale.sy;
				if (outputScale.scaled) {
				    context.scale(outputScale.sx, outputScale.sy);
				}
			    var canvasOffset = $canvas.offset();

				page.getTextContent().then(function(textContent) {
				var renderContext = {
				    canvasContext: context,
				    viewport: viewport
				};

				page.render(renderContext);
				});
			} */

			// View Portability Details
			$(".portDetailsBtn").click(function() {
				var portDetails = JSON.parse($(this).attr('data-json'));
				$('.portDetailsBody').html('');
				$('.portDetailsBody').append(portabilityTableFormat(portDetails));
				document.getElementsByClassName('portDetailsModal')[0].style.display = "block";
			});
			
			function ordinal_suffix_of(i) {
				var j = i % 10;
				var k = i % 100;
				if (j == 1 && k != 11) {
					return i + "st";
				}
				if (j == 2 && k != 12) {
					return i + "nd";
				}
				if (j == 3 && k != 13) {
					return i + "rd";
				}
				return i + "th";
			}	

			function addCommas(nStr) {
				nStr += '';
				var x = nStr.split('.');
				var x1 = x[0];
				var x2 = x.length > 1 ? '.' + x[1] : '';
				var rgx = /(\d+)(\d{3})/;
				while (rgx.test(x1)) {
						x1 = x1.replace(rgx, '$1' + ',' + '$2');
				}
				return x1 + x2;
			}			
			
			$(".fvrGradingCloseBtn, .fvrGradingCloseIcon").click(function() {
				document.getElementsByClassName('fvr-grading-modal')[0].style.display = "none";
			});
			
			function getUrlParameter(sParam) {
				var sPageURL = decodeURIComponent(window.location.search.substring(1)),
				sURLVariables = sPageURL.split('&'),
				sParameterName,
				i;

				for (i = 0; i < sURLVariables.length; i++) {
					sParameterName = sURLVariables[i].split('=');

					if (sParameterName[0] === sParam) {
						return sParameterName[1] === undefined ? true : sParameterName[1];
					}
				}
			}

			
			
			function formFVRDocumentModalData(docDetails) {
				$('#intimationNo').text((docDetails.intimationNo ? docDetails.intimationNo : ''));
				$('#claimNo').text((docDetails.claimNo ? docDetails.claimNo : ''));
				$('.fvrDocumentBody').html('');
				var documents = docDetails.dmsDocumentDetailsDTOList;
				if(documents) {					
					for (var i = 0; i < documents.length; i++) {
						$('.fvrDocumentBody').append('<tr><td>' + (i + 1) + '</td><td>' + (documents[i].documentType ? documents[i].documentType : '') + '</td><td>' + (documents[i].cashlessOrReimbursement ? documents[i].cashlessOrReimbursement : '') + '</td><td style="width: 10%">' + (documents[i].fileName ? documents[i].fileName : '') + '</td><td>' + (documents[i].documentCreatedDateValue ? documents[i].documentCreatedDateValue : '') + '</td><td>' + (documents[i].documentSource ? documents[i].documentSource : '') + '</td><td>' +
						(documents[i].fileViewURL ? '<a class="fvrViewDocumentDetailBtn" data-url="' + documents[i].fileViewURL + '">View Document </a>' : '')+'</td></tr>');
					}
				}
				document.getElementsByClassName('fvr-document-modal')[0].style.display = "block";
			}

			$(".fvrDocumentCloseBtn, .fvrDocumentCloseIcon").click(function() {
				document.getElementsByClassName('fvr-document-modal')[0].style.display = "none";
				$('.acknowledge-details-dialog').removeClass('hidden');
				$('.ackQryDetailsModal').removeClass('hidden');
			});
			
			$(".64VBDocumentViewCloseIcon, .64VBDocumentViewCloseBtn").click(function() {
				$(".64VBFrame").html('');
				document.getElementsByClassName('64VBDocumentViewModal')[0].style.display = "none";
			});

			$(".insuredDetailCloseIcon").click(function() {
				document.getElementsByClassName('insuredDetailModal')[0].style.display = "none";
			});

			$(".fvrDocumentViewCloseIcon, .fvrDocumentViewCloseBtn").click(function() {
				document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "none";
				if($(this).hasClass('temp')) {
					$('.coorViewFileModal').removeClass('hidden');
					if($(this).hasClass('coordinator')) {
						$('.coordinator-reply-dialog').removeClass('hidden');
						$(this).removeClass('coordinator');
					} else {
						$('.specialist-trail-dialog').removeClass('hidden');
						$(this).removeClass('specialist');
					}
					$(this).removeClass('temp');
				}
				$('.frame-title').html('View Uploaded Document');
			});
						
			// View PED Details
			jQuery(document).on("click", '.pedViewBtn', function() {
				var pedDetails = JSON.parse($(this).closest('tr').attr('data-json'));
				$('.pedDetailBody').html('');
				if (pedDetails) {	
					var repLetterDate = (pedDetails.repLetterDate ? pedDetails.repLetterDate : '');
					var requestedDate = (pedDetails.requestedDate ? pedDetails.requestedDate : ''); 
					$('.viewPedSug').html(pedDetails.pedSuggestionName ? pedDetails.pedSuggestionName : '');
					$('.viewPedName').html(pedDetails.pedName ? pedDetails.pedName : '');
					$('.viewRepdate').html(pedDetails.repLetterDate ? pedDetails.repLetterDate : '');
					$('.viewPedRemarks').html(pedDetails.remarks ? pedDetails.remarks : '');
					// View PED Endorsement Details
					var pedEndorse = pedDetails.viewPEDEndoresementDetailsDTO;
					for (var i = 0; i < pedEndorse.length; i++) {
						$('.pedDetailBody').append('<tr><td>' + (pedEndorse[i].pedCode ? pedEndorse[i].pedCode : '') + '</td><td>' + (pedEndorse[i].description ? pedEndorse[i].description : '') + '</td><td style="width: 10%">' + (pedEndorse[i].icdChapter ? pedEndorse[i].icdChapter : '') + '</td><td>' + (pedEndorse[i].icdBlock ? pedEndorse[i].icdBlock : '') + '</td><td>' + (pedEndorse[i].icdCode ? pedEndorse[i].icdCode : '') + '</td><td>' + (pedEndorse[i].source ? pedEndorse[i].source : '') + '</td><td>' + (pedEndorse[i].othersSpecify ? pedEndorse[i].othersSpecify : '') + '</td><td>' + (pedEndorse[i].doctorRemarks ? pedEndorse[i].doctorRemarks : '') + '</td></tr>');
					}
					
					if (pedDetails.hasPedSpecialistDetails) {
						$('.viewPedStatusSpl').html((pedDetails.pedSpecialistStatus ? pedDetails.pedSpecialistStatus : '') );
						$('.viewPedRemarksSpl').html((pedDetails.pedSpecialistRemarks ? pedDetails.pedSpecialistRemarks : ''));
						$('.ped-specialist-div').removeClass('hidden');
						$('.ped-specialist-empty-div').addClass('hidden');
					} else {
						$('.ped-specialist-div').addClass('hidden');
						$('.ped-specialist-empty-div').removeClass('hidden');
					}
					
					if (pedDetails.pedDiscussedWith) {
						$('.discussedWith').show();
						$('.viewDiscussedWith').html(pedDetails.pedDiscussedWith);
					} else {
						$('.discussedWith').hide();
					}
					
					if (pedDetails.pedSuggestion) {
						$('.suggestion').show();
						$('.viewSuggestion').html(pedDetails.pedSuggestion);
					} else {
						$('.suggestion').hide();
					}
						
					$('.viewReqId').html((pedDetails.pedRequestorId ? pedDetails.pedRequestorId : ''));
					$('.viewReqDate').html((pedDetails.pedRequestorDate ? pedDetails.pedRequestorDate : ''));
						
				} else {
					$('.pedDetailBody').append('<tr><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>');
				}

				// View PED Ammended Details
				if ($(this).closest('tr').attr('data-ammended')) {
					var pedAmmendedDetails = JSON.parse($(this).closest('tr').attr('data-ammended'));
										
					$('.viewPedSugAmd').val((pedAmmendedDetails.pedSuggestionName ? pedAmmendedDetails.pedSuggestionName : '') );
					$('.viewPedNameAmd').val((pedAmmendedDetails.pedName ? pedAmmendedDetails.pedName : ''));
					$('.viewRepdateAmd').val(repLetterDate);
					$('.viewPedRemarksAmd').val((pedAmmendedDetails.remarks ? pedAmmendedDetails.remarks : ''));
					$('.viewReqIdAmd').val((pedAmmendedDetails.requestorId ? pedAmmendedDetails.requestorId : ''));
					$('.viewReqDateAmd').val(requestedDate);
				    var endorseDetails = pedAmmendedDetails.viewPEDEndoresementDetailsDTO;
						$('.ped-ammended-table-body').html('');
						for (var i = 0; i < endorseDetails.length; i++) {
							$('.ped-ammended-table-body').append('<tr><td>' + (endorseDetails[i].description ? endorseDetails[i].description : '') + '</td><td>' + (endorseDetails[i].pedCode ? endorseDetails[i].pedCode : '') + '</td><td style="width: 10%">' + (endorseDetails[i].icdChapter ? endorseDetails[i].icdChapter : '') + '</td><td>' + (endorseDetails[i].icdBlock ? endorseDetails[i].icdBlock : '') + '</td><td>' + (endorseDetails[i].icdCode ? endorseDetails[i].icdCode : '') + '</td><td>' + (endorseDetails[i].source ? endorseDetails[i].source : '') + '</td><td>' + (endorseDetails[i].othersSpecify ? endorseDetails[i].othersSpecify : '') + '</td><td>' + (endorseDetails[i].doctorRemarks ? endorseDetails[i].doctorRemarks : '') + '</td></tr>');
						}
						$('.ped-ammended-div').removeClass('hidden');
					} else {
						$('.ped-ammended-div').addClass('hidden');
					}
					
					// View PED Specialist Details
					if ($(this).attr('data-add-ped')) {
						var pedAdditionalDetails = JSON.parse($(this).attr('data-add-ped'));
						if(pedAdditionalDetails.hasPedSpecialistDetails) {
							$('.viewPedStatusSpl').html((pedAdditionalDetails.pedSpecialistStatus ? pedAdditionalDetails.pedSpecialistStatus : '') );
							$('.viewPedRemarksSpl').html((pedAdditionalDetails.pedSpecialistRemarks ? pedAdditionalDetails.pedSpecialistRemarks : ''));
							$('.ped-specialist-div').removeClass('hidden');
							$('.ped-specialist-empty-div').addClass('hidden');
						} else {
							$('.ped-specialist-div').addClass('hidden');
							$('.ped-specialist-empty-div').removeClass('hidden');
						}
					}
					
					// View PED Approved Details
					if ($(this).closest('tr').attr('data-approved')) {
						var pedApprovedDetails = JSON.parse($(this).closest('tr').attr('data-approved'));
										
						$('.viewPedSugApp').val((pedApprovedDetails.pedSuggestionName ? pedApprovedDetails.pedSuggestionName : '') );
						$('.viewPedNameApp').val((pedApprovedDetails.pedName ? pedApprovedDetails.pedName : ''));
						$('.viewRepdateApp').val(repLetterDate);
						$('.viewPedRemarksApp').val((pedApprovedDetails.remarks ? pedApprovedDetails.remarks : ''));

				        var endorseDetails = pedApprovedDetails.viewPEDEndoresementDetailsDTO;
						$('.ped-approved-table-body').html('');
						for (var i = 0; i < endorseDetails.length; i++) {
							$('.ped-approved-table-body').append('<tr><td>' + (endorseDetails[i].description ? endorseDetails[i].description : '') + '</td><td>' + (endorseDetails[i].pedCode ? endorseDetails[i].pedCode : '') + '</td><td style="width: 10%">' + (endorseDetails[i].icdChapter ? endorseDetails[i].icdChapter : '') + '</td><td>' + (endorseDetails[i].icdBlock ? endorseDetails[i].icdBlock : '') + '</td><td>' + (endorseDetails[i].icdCode ? endorseDetails[i].icdCode : '') + '</td><td>' + (endorseDetails[i].source ? endorseDetails[i].source : '') + '</td><td>' + (endorseDetails[i].othersSpecify ? endorseDetails[i].othersSpecify : '') + '</td><td>' + (endorseDetails[i].doctorRemarks ? endorseDetails[i].doctorRemarks : '') + '</td></tr>');
						}
						
						$('.viewPedStatusApp').val((pedApprovedDetails.pedSuggestionName ? pedApprovedDetails.pedSuggestionName : ''));
						$('.viewQueryApp').val((pedApprovedDetails.queryRemarks ? pedApprovedDetails.queryRemarks : ''));
						$('.viewSplApp').val((pedApprovedDetails.specialistType ? pedApprovedDetails.specialistType : ''));
						$('.viewReasonApp').val((pedApprovedDetails.reasonforReferring ? pedApprovedDetails.reasonforReferring : ''));
						$('.viewRejectionApp').val((pedApprovedDetails.rejectionRemarks ? pedApprovedDetails.rejectionRemarks : ''));
						$('.viewApprovalApp').val((pedApprovedDetails.approvalRemarks ? pedApprovedDetails.approvalRemarks : ''));
						
						
						$('.ped-approved-div').removeClass('hidden');
					} else {
						$('.ped-approved-div').addClass('hidden');
					}
					
					document.getElementsByClassName('ped-detail-modal')[0].style.display = "block";
				
			});

			$(".pedDetailCloseIcon").click(function() {
				document.getElementsByClassName('ped-detail-modal')[0].style.display = "none";
			});

			// View PED Trail Details
			jQuery(document).on("click", '.pedHistoryBtn', function() {
				var pedHistory = JSON.parse($(this).closest('tr').attr('data-history'));
				if (pedHistory) {
					$('.pedHistoryBody').html('');
					for (var i = 0; i < pedHistory.length; i++) {
						$('.pedHistoryBody').append('<tr><td>' + (pedHistory[i].status ? pedHistory[i].status : '') + '</td><td>' + (pedHistory[i].strDateAndTime ? pedHistory[i].strDateAndTime : '') + '</td><td style="width: 10%">' + (pedHistory[i].userName ? pedHistory[i].userName : '') + '</td><td>' + (pedHistory[i].remarks ? pedHistory[i].remarks : '') + '</td></tr>');
					}
					document.getElementsByClassName('pedHistoryModal')[0].style.display = "block";
				}
			}); 

			$(".pedHistoryCloseIcon").click(function() {
				document.getElementsByClassName('pedHistoryModal')[0].style.display = "none";
			});
			
			$(".ackQryDetailCloseBtn").click(function() {
				document.getElementsByClassName('ackQryDetailsModal')[0].style.display = "none";
			});
			
			$(".coorViewFileCloseIcon").click(function() {
				document.getElementsByClassName('coorViewFileModal')[0].style.display = "none";
			});
			
			$('#ackViewQryLetter').click(function() {
				var docDetails = JSON.parse($(this).attr('data-dms'));
				if(docDetails) {
					formFVRDocumentModalData(docDetails);
					$('.acknowledge-details-dialog').addClass('hidden');
					$('.ackQryDetailsModal').addClass('hidden');
				}
			});
			
			$('#detailedView').click(function() {
				
				$('.preauthTitleText').val($(".preselect option:selected").text());
				$('.preauthTitleText').removeClass('hidden');
				$('.preselect').addClass('hidden');
				$('.detailClose').removeClass('hidden');
				$('.preauth-sublimit').removeClass('hidden');
				$('#detailedView').addClass('hidden');
				$('.preauth-pedvalidation').removeClass('hidden');
				$('.preauth-exclusion').removeClass('hidden');
				//$('.preauth-additional-fields').removeClass('hidden');
				$('.preauth-speciality').removeClass('hidden');
			});
			
			$('.detailClose').click(function() {
				
				$('.preauthTitleText').addClass('hidden');
				$('.preselect').removeClass('hidden');
				$('.detailClose').addClass('hidden');
				$('.preauth-sublimit').addClass('hidden');
				$('#detailedView').removeClass('hidden');
				$('.preauth-pedvalidation').addClass('hidden');
				$('.preauth-exclusion').addClass('hidden');
				//$('.preauth-additional-fields').addClass('hidden');
				$('.preauth-speciality').addClass('hidden');
			});
			
			$('.preselect').on('change', function() {

				$('.overlay').show();
				var preauthKey =  $(this).val();
				if (preauthKey && authToken) {
					$.get('crcportal', {
						token : authToken,
						pageName : "Pre Auth Details",
						preIntimationKey : preauthKey
					}, function(data, statusText, xhr) {
						if (xhr.status == 200) {						
							getPreAuthDetails(data.preAuthDetails);
							$('#preauth-details-dialog').dialog('open');
						} else if (xhr.status == 204) {
							alert("Details not found");
						} else if (xhr.status == 400) {
							alert("Bad Request");
						}
						$('.overlay').hide();
					});
				}
			});
			
			// View FVR Grading Details
			jQuery(document).on("click", '.fvrGradingBtn', function() {
	
				var fvrDetails = JSON.parse($(this).closest('tr').attr('data-grading'));
				$('.fvrSeqValue').html("FVR Sequence " + ordinal_suffix_of(fvrDetails.serialNumber) +  " FVR");
				$('#repCode').val((fvrDetails.representativeCode ? fvrDetails.representativeCode : ''));
				$('#repName').val((fvrDetails.representativeName ? fvrDetails.representativeName : ''));
				$('#fvrAssignedDate').val((fvrDetails.fvrassignedDate ? fvrDetails.fvrassignedDate : ''));
				$('#fvrReceivedDate').val((fvrDetails.fvrReceivedDate ? fvrDetails.fvrReceivedDate : ''));
				$('#fvrTat').val((fvrDetails.fVRTAT ? fvrDetails.fVRTAT : '0'));
				var gradingSegmentDetails = fvrDetails.newFvrGradingDTO;
				$('.fvrSegA').html('');
				$('.fvrSegB').html('');
				$('.fvrSegC').html('');
				if(gradingSegmentDetails) {
					for(var i=0; i < gradingSegmentDetails.length; i++) {
						if(gradingSegmentDetails[i].statusFlagSegmentA != null && gradingSegmentDetails[i].statusFlagSegmentA == 'Y') {
							$('.fvrSegA').append('<tr><td>' + (i + 1) + '</td><td>' + (gradingSegmentDetails[i].category ? gradingSegmentDetails[i].category: '') + '</td><td><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlagSegmentA == 'Y' ? "checked" :"" ) + '/> <label> Yes </label><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlagSegmentA == 'N' ? "checked" :"" )  + '/> <label> No </label>' + '</td><tr>');
						} else if(gradingSegmentDetails[i].statusFlagSegmentC != null && gradingSegmentDetails[i].statusFlagSegmentC == 'Y') {
						    $('.fvrSegC').append('<tr><td>' + (i + 1) + '</td><td>' + (gradingSegmentDetails[i].category ? gradingSegmentDetails[i].category: '') + '</td><td><input disabled type="checkbox"' + (gradingSegmentDetails[i].statusFlagSegmentC == 'Y' ? "checked" :"" ) + '/> </td><tr>');
						} else {
							$('.fvrSegB').append('<tr><td>' + (i + 1) + '</td><td>' + (gradingSegmentDetails[i].category ? gradingSegmentDetails[i].category: '') + '</td><td><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlag == 'Y' ? "checked" :"" )  + '/> <label> Yes </label><input disabled type="radio"' + (gradingSegmentDetails[i].statusFlag == 'N' ? "checked" :"" )  + '/> <label> No </label>' + '</td><tr>');
						}
					}
				}
				document.getElementsByClassName('fvr-grading-modal')[0].style.display = "block";
			});
			
			// View FVR Document Details
			jQuery(document).on("click", '.fvrViewDocBtn', function() {
				if ($(this).closest('tr').attr('data-json')) {
					var docDetails = JSON.parse($(this).closest('tr').attr('data-json'));
					formFVRDocumentModalData(docDetails);
				}
			});
			
			function getPreAuthDetails(response) {
				if (response) {
					$('#preauth-created-date').html(response.createdDate);
						 $('#preauth-modified-date').html(response.modifiedDate); 
						 $('#preauth-remarks').html(response.remarks);
						 $('#preauth-medical-remarks').html(response.medicalRemarks);					
						 $('#preauth-approved-amt').html(response.approvedAmt);
						 $('#preauth-status').html(response.statusValue);
						 $('#preauth-doctor-note').html(response.doctorNote);
						 $('#preauth-doa').html(response.dateOfAdmisssion);
						 $('#preauth-admission-reason').html(response.admissionReason);
						 $('#preauth-change-doa').html(response.changeDOA);
						 $('#preauth-room-cat').html(response.roomCategory);
						 $('preauth-specialist-opinion').html(response.specialistOpinionTaken);
						 $('#preauth-treatment-type').html(response.treatmentType);
						 $('#preauth-specify-illness').html(response.specifyIllness);
						 $('#preauth-days').html(response.noOfDays);
						 $('#preauth-nature').html(response.natureOfTreatment);
						 $('#preauth-critical-illness').html((response.criticalIllness == 'Y' ? '<input disabled type="checkbox" checked />Critical Illness' :
							  '<input disabled type="checkbox"  />Critical Illness'));
						 $('#preauth-corp-buffer').html((response.corpBuffer == 'Y' ? '<input disabled type="checkbox" checked />Corp Buffer' :
							  '<input disabled type="checkbox"  />Corp Buffer'));
						 $('#preauth-cons-date').html(response.firstConsultedDate);
						 $('#preauth-specify-illness').html(response.specifyIllness);
						 $('#preauth-auto-rest').html(response.autoRestore);
						 $('#preauth-patient-status').html(response.patientStatus);
						 $('#preauth-illness').html(response.illness);
						 $('#preauth-death-date').html(response.dateOfDeath);
						 $('#preauth-death-reason').html(response.deathReason);
						 $('#preauth-relapse-remarks').html(response.relapseRemarks);
						 $('#preauth-terminate-cover').html(response.terminateCover);
						 $('#preauth-specialist-option').html((response.specialistOpinionTaken == '1' ? '<input type="radio" disabled name="branchfld" value="YES" checked />YES <input type="radio" name="branchfld" value="NO" disabled />NO': '<input type="radio" name="branchfld" value="YES" disabled  />YES <input type="radio" name="branchfld" value="NO" disabled checked />NO' ));
						 
						 $('#preauth-field-request').html((response.initiateFvr == '1' ? '<input type="radio" name="branch" value="YES" checked disabled/>YES <input type="radio" name="branch" value="NO"  disabled />NO': '<input type="radio" name="branch" value="YES" disabled />YES <input type="radio" name="branch" value="NO" checked disabled />NO' ));
						 $('#preauth-fvr').html(response.fvrNotRequiredRemarks);
						 $('#preauth-spl-type').html(response.specialistType);
						 $('#preauth-allocation').html(response.allocationTo);
						 $('#preauth-spl-consulted').html(response.specialistConsulted);
						 $('#preauth-fvr-trigger').html(response.fvrTriggeredPoints);
						 $('#preauth-spl-remarks').html(response.specialistRemarks);
						 $('#preauth-inv-name').html(response.investigatorName);
						 $('#preauth-treatment-remarks').html(response.treatmentRemarks);
						 $('#preauth-nego-with').html(response.negotiationWith);
						 
						 
						 //Sub limits, Package & SI Restriction
						 $('.preauth-sublimit-table-body').html('');
						 var diagnosis = response.diagnosisTableList;
						 for (var i = 0; i < diagnosis.length; i++) {
						 $('.preauth-sublimit-table-body').append('<tr><td>' + (diagnosis[i].diagOrProcedure ? diagnosis[i].diagOrProcedure : '') + '</td><td>' + (diagnosis[i].description ? diagnosis[i].description : '') + '</td><td>' + (diagnosis[i].pedOrExclusion ? diagnosis[i].pedOrExclusion : '') + '</td><td>' + (diagnosis[i].isAmbChargeFlag == 'Y' ? '<input disabled type="checkbox" checked />' : '<input disabled type="checkbox" />') + '</td><td>' + (diagnosis[i].ambulanceCharge ? diagnosis[i].ambulanceCharge : '0') + '</td><td>' +  (diagnosis[i].amountConsidered ? diagnosis[i].amountConsidered : '0')  + '</td><td>' +  (diagnosis[i].packageAmt ? diagnosis[i].packageAmt : 'NA')  + '</td><td>' +  (diagnosis[i].minimumAmountOfAmtconsideredAndPackAmt ? diagnosis[i].minimumAmountOfAmtconsideredAndPackAmt : '0') + '</td><td>' +  (diagnosis[i].coPayPercentage ? diagnosis[i].coPayPercentage : '0') + '</td><td>' +  (diagnosis[i].coPayAmount ? diagnosis[i].coPayAmount : '0') + '</td><td>' +  (diagnosis[i].netAmount ? diagnosis[i].netAmount : '0') + '</td><td>' +  (diagnosis[i].amtWithAmbulanceCharge ? diagnosis[i].amtWithAmbulanceCharge : '0')  + '</td><td>' +  (diagnosis[i].subLimitAmount ? diagnosis[i].subLimitAmount : 'NA') + '</td><td>' +  (diagnosis[i].subLimitUtilAmount ? diagnosis[i].subLimitUtilAmount : '0') + '</td><td>' +  (diagnosis[i].subLimitAvaliableAmt ? diagnosis[i].subLimitAvaliableAmt : '0')  + '</td><td>' +  (diagnosis[i].restrictionSI ? diagnosis[i].restrictionSI : 'NA')  + '</td><td>' +  (diagnosis[i].utilizedAmt ? diagnosis[i].utilizedAmt : '0')  + '</td><td>' +  (diagnosis[i].availableAmout ? diagnosis[i].availableAmout : '0') + '</td><td>' +  (diagnosis[i].minimumAmount ? diagnosis[i].minimumAmount : '0') + '</td><td>' + (diagnosis[i].reverseAllocatedAmt ? diagnosis[i].reverseAllocatedAmt : '0') + '</td> </tr>');
						}
						
						
						$('.preauth-sublimit-table-body').append('<tr><td></td><td></td><td><b>Total</b></td><td></td><td></td><td><b>' + (response.totalDiagAllocAmt?response.totalDiagAllocAmt:'')  + '</b></td><td></td><td></td><td></td><td></td> <td><b>'+ (response.totalDiagNetAmt?response.totalDiagNetAmt:response.totalDiagPayableAmt) + '</b></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td><b>' + (response.totalDiagPayableAmt?response.totalDiagPayableAmt:'')  + 
						'</b></td> <td><b>'+ (response.totalDiagReverseAllocAmt?response.totalDiagReverseAllocAmt:'')   + '</b></td> </tr>');
										
						$('.preauth-amt-table-body').html('');
						$.each(response.amountConsideredValues, function(key, value) {
						    if(key == 0) {
								$('.preauth-amt-table-body').append('<tr><td>' + value + '</td><td>' + response.minimumValue  + '</td></tr>' );
							}
							
							 if(key == 1) {
								$('.preauth-amt-table-body').append('<tr><td>' + value + '</td><td>' +  0 + '</td></tr>' );
							}
							
							 if(key == 2) {
								$('.preauth-amt-table-body').append('<tr><td>' + value + '</td><td>' +   response.minimumValue  + '</td></tr>' );
							}
						});
						
						$('.preauth-balance-body').html('');
						$.each(response.balanceSumInsuredValues, function(key, value) {
						    if(key == 0) {
								$('.preauth-balance-body').append('<tr><td>' + value + '</td><td>' + response.balanceSumInsured  + '</td></tr>' );
							}
							
							 if(key == 1) {
								$('.preauth-balance-body').append('<tr><td>' + value + '</td><td>' +  0 + '</td></tr>' );
							}
							
							 if(key == 2) {
								$('.preauth-balance-body').append('<tr><td>' + value + '</td><td>' +   response.balanceSumInsured  + '</td></tr>' );
							}
						});
						
						$('#preauth-amount').val(response.approvedAmt);
						$('.preauth-amt-sublimit').val(response.totalDiagReverseAllocAmt);
						
						$('.preauth-restrict-table-body').html('');
						var claim = response.claimedDetailsList;
						for (var i = 0; i < claim.length; i++) {
							 $('.preauth-restrict-table-body').append('<tr><td>' + (claim[i].description ? claim[i].description : '') + '</td><td>' + (claim[i].totalBillingDays ? claim[i].totalBillingDays : '') + '</td><td>'  + (claim[i].billingPerDayAmount ? claim[i].billingPerDayAmount : '') + '</td><td>' +  (claim[i].claimedBillAmount ? claim[i].claimedBillAmount : '')  + '</td><td>' +  (claim[i].deductibleAmount ? claim[i].deductibleAmount : '')  + '</td><td>' +  (claim[i].netAmount ? claim[i].netAmount : '0') + '</td><td>' +  (claim[i].totalBillingDays ? claim[i].totalBillingDays : '') + '</td><td>' +  (claim[i].policyPerDayPayment ? claim[i].policyPerDayPayment : '') + '</td><td>' +  (claim[i].netAmount ? claim[i].netAmount : '0') + '</td><td>' +   (claim[i].considerPerDayAmt ? claim[i].considerPerDayAmt : '') + '</td><td>' +  (claim[i].nonPayableAmount ? claim[i].nonPayableAmount : '0')  + '</td><td>' + (claim[i].paybleAmount ? claim[i].paybleAmount : '0')  + '</td><td>' +  (claim[i].nonPayableReason ? claim[i].nonPayableReason : '') + '</td> </tr>');

						}
						
					    $('.preauth-restrict-table-body').append('<tr><td><b>Total</b></td><td></td><td></td><td><b>' +  (response.totalClaimedAmtforAmtconsd ? response.totalClaimedAmtforAmtconsd : '0')  + '</b></td><td><b>' +  (response.totalDeductAmtforAmtconsd? response.totalDeductAmtforAmtconsd : '0') + '</b></td><td><b>' + (response.totalNetAmtforAmtconsd?response.totalNetAmtforAmtconsd:'0') + '</b></td><td></td><td><b>' +  (response.totalProductAmtforAmtconsd? response.totalProductAmtforAmtconsd : '0') + '</b></td><td></td><td></td><td></td><td><b>' +  (response.totalPayableAmtforAmtconsd? response.totalPayableAmtforAmtconsd : '') + '</b></td><td></td></tr>');
						
						$('.ped-validation-tbody').html('');
						
						var validation = response.pedValidationList;
						for (var i = 0; i < validation.length; i++) {
							$('.ped-validation-tbody').append('<tr><td>' + (validation[i].diagnosis ? validation[i].diagnosis  : '') + '</td><td>' + (validation[i].pedName ? validation[i].pedName : '')+ '</td><td>' + (validation[i].icdChapterValue ? validation[i].icdChapterValue : '')   + '</td><td>' + (validation[i].icdBlockValue ? validation[i].icdBlockValue : '') + '</td><td>' + (validation[i].icdCodeValue ? validation[i].icdCodeValue : '')  + '</td><td>'  +  (validation[i].policyAgeing ? validation[i].policyAgeing : '') + '</td><td>' +  (validation[i].impactOnDiagnosis ? validation[i].impactOnDiagnosis : '')  + '</td><td>' +  (validation[i].consideredForExclusion ? validation[i].consideredForExclusion : '')  + '</td><td>' +  (validation[i].remarks ? validation[i].remarks : '') + '</td> </tr>');
						}
						
						$('.ped-exclusion-tbody').html('');
						var exclusion = response.procedureExclusionCheckTableList;
						for (var i = 0; i < exclusion.length; i++) {
							$('.ped-exclusion-tbody').append('<tr><td>' + (exclusion[i].procedureNameValue ? exclusion[i].procedureNameValue  : '') + '</td><td>' + (exclusion[i].procedureCodeValue ? exclusion[i].procedureCodeValue : '')+ '</td><td>' + (exclusion[i].packageAmount ? exclusion[i].packageAmount : '')  + + '</td><td>' + (exclusion[i].policyAging ? exclusion[i].policyAging : '') + + '</td><td>' + (exclusion[i].procedureStatus ? exclusion[i].procedureStatus : '')  + '</td><td>'  +  (exclusion[i].exclusionDetails ? exclusion[i].exclusionDetails : '') + '</td><td>' +  (exclusion[i].remarks ? exclusion[i].remarks : '')  + '</td> </tr>');
						}
						$('.overlay').hide();
						
					}
			}



			// Get the modal
			var modal = document.getElementById('myModal');

			// Get the button that opens the modal
			//var btn = document.getElementById("myBtn");

			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("close")[0];

			// When the user clicks the button, open the modal 
			/* btn.onclick = function() {
				modal.style.display = "block";
			} */

			// When the user clicks on <span> (x), close the modal
			span.onclick = function() {
				modal.style.display = "none";
			}

			// When the user clicks anywhere outside of the modal, close it
			window.onclick = function(event) {
				if (event.target == modal) {
					modal.style.display = "none";
				}
			}

			function pop1() {

				window.open('', 'rejectionDetails', 'width:650, height: 500');
			}

			function pop2() {

				window.open('', 'queryDetails', 'width:650, height: 500');
			}

			/* Formatting function for row details */
			function portabilityTableFormat(portDetails) {

				var detailViewHtml = '<tr>' +
					'<td><strong>TBA code</strong></td>' +
					'<td >' + (portDetails.tbaCode ? portDetails.tbaCode : '') + '</td>' +
					'<td><strong>1st SI</strong></td>' +
					'<td>' + (portDetails.siFist ? portDetails.siFist : '0') + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td><strong>Policy Start</strong></td>' +
					'<td>' + (portDetails.policyStartDate ? portDetails.policyStartDate : '') + '</td>' +
					'<td><strong>11nd SI</strong></td>' +
					'<td>' + (portDetails.siSecond ? portDetails.siSecond : '0') + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td><strong>Period elapsed</strong></td>' +
					'<td>' + (portDetails.periodElapsed ? portDetails.periodElapsed : '0') + '</td>' +
					'<td><strong>3rd SI</strong></td>' +
					'<td>' + (portDetails.siThird ? portDetails.siThird : '0') + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td><strong>policy Term</strong></td>' +
					'<td>' + (portDetails.policyTerm ? portDetails.policyTerm : '') + '</td>' +
					'<td><strong>4th SI</strong></td>' +
					'<td>' + (portDetails.siFourth ? portDetails.siFourth : '0') + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td><strong>Date of Birth</strong></td>' +
					'<td>' + (portDetails.dateOfBirth ? portDetails.dateOfBirth : '') + '</td>' +
					'<td><strong>I st  Float SI</strong></td>' +
					'<td>' + (portDetails.siFirstFloat ? portDetails.siFirstFloat : '0') + '</td>' +
					'</tr> ' +
					'<tr><td></td><td></td>' +
					'<td><strong>II nd  float SI</strong></td>' +
					'<td>' + (portDetails.siSecondFloat ? portDetails.siSecondFloat : '0') + '</td>' +
					'</tr> <tr></tr> ' +
					'<tr>' +
					'<td><strong>3rd Float SI</strong></td>' +
					'<td>' + (portDetails.siThirdFloat ? portDetails.siThirdFloat : '0') + '</td>' +
					'<td><strong>Remarks</strong></td>' +
					'<td>' + (portDetails.remarks ? portDetails.remarks : '') + '</td>' +
					'</tr> ' +
					'<tr>' +
					'<td><strong>4th  Float SI</strong></td>' +
					'<td>' + (portDetails.siFourthFloat ? portDetails.siFourthFloat : '0') + '</td>' +
					'<td><strong>Ped Declared</strong></td>' +
					'<td>' + (portDetails.pedDeclared ? portDetails.pedDeclared : '') + '</td>' +
					'</tr> ' +
					'<tr>' +
					'<td><strong>I st Change SI</strong></td>' +
					'<td>' + (portDetails.siFirstChange ? portDetails.siFirstChange : '0') + '</td>' +
					'<td><strong>PED ICD code</strong></td>' +
					'<td>' + (portDetails.pedIcdCode ? portDetails.pedIcdCode : '') + '</td>' +
					'</tr> ' +
					'<tr>' +
					'<td><strong>II nd Change SI</strong></td>' +
					'<td>' + (portDetails.siSecondChange ? portDetails.siSecondChange : '0') + '</td>' +
					'<td><strong>PED Description</strong></td>' +
					'<td>' + (portDetails.pedDescription ? portDetails.pedDescription : '') + '</td>' +
					'</tr> ' +
					'<tr>' +
					'<td><strong>3rd Change SI</strong></td>' +
					'<td>' + (portDetails.siThirdChange ? portDetails.siThirdChange : '0') + '</td>' +
					'<td><strong>Family size</strong></td>' +
					'<td>' + (portDetails.familySize ? portDetails.familySize : '0') + '</td>' +
					'</tr> ' +
					'<tr>' +
					'<td><strong>4th Change SI</strong></td>' +
					'<td>' + (portDetails.siFourthChange ? portDetails.siFourthChange : '0') + '</td>' +
					'<td><strong>Request ID</strong></td>' +
					'<td>' + (portDetails.requestId ? portDetails.requestId : '') + '</td>' +
					'</tr>' +
					'<tr>' +
					'<td></td>' +
					'<td ></td>' +
					'<td><strong>Member entry dt.</strong></td>' +
					'<td>' + (portDetails.memberEntryDate ? portDetails.memberEntryDate : '') + '</td>' +
					'</tr> ';

				return detailViewHtml;

			}
			
			function sumInsuredTableFormat(jsonDetails, tableName) {
				
				var json = jsonDetails;
				var section;
				var insuredTableBody = '';
				for(var i= 0; i < json.length; i++) {
					if (tableName == "sectionI") {
						section = json[i].sectionI;
					} else if(tableName == "sectionII") {
						section = json[i].sectionII;
					} else if(tableName == "sectionIII") {
						section = json[i].sectionIII;
					} else if(tableName == "sectionIV") {
						section = json[i].sectionIV;
					} else if(tableName == "sectionV") {
						section = json[i].sectionV;
					}  else if(tableName == "sectionVI") {
						section = json[i].sectionV;
					} else if(tableName == "sectionVIII") {
						section = json[i].sectionIII;
					}
					
					insuredTableBody += '<tr><td>' + (section ? section : '' ) + '</td><td>' + (json[i].cover ? json[i].cover :'') + '</td><td>' + (json[i].subCover ?json[i].subCover :'') + '</td><td>' + (tableName == "sectionI" ? addCommas(json[i].sumInsured) : addCommas(json[i].limit)) + '</td><td>'  + (json[i].claimPaid ? addCommas(json[i].claimPaid) :'0') + '</td><td>'  + (json[i].claimOutStanding ? addCommas(json[i].claimOutStanding) : '0' ) + '</td><td>'  + (json[i].balance ? addCommas(json[i].balance) : '0') +'</td><td>' +  (json[i].provisionCurrentClaim ? addCommas(json[i].provisionCurrentClaim) :'0') + '</td><td>'  + (json[i].balanceSI?addCommas(json[i].balanceSI) :'0') + '</td><tr>' ;
				}
						
				var formattedTables = '<table class="fvr-sub-table" border="0" cellspacing="0" cellpadding="0">' + 
						'<thead>' +
							'<tr>' +
								'<th>Section</th>'  +
								'<th>Cover</th>' +
								'<th>Sub Cover</th>' + (tableName == "sectionI" ? '<th>Sum Insured</th>' : '<th>limit</th>') +
								'<th>Claim Paid</th>' +
								'<th>Claim Outstanding</th>' +
								'<th>Balance</th>' +
								'<th>Provision for Current Claim</th>' +
								'<th>Balance Sum Insured after Provision</th>' +
							'</tr>' +
						'</thead>' +
						'<tbody class="' + tableName + '">' + insuredTableBody
						'</tbody>' +
						'</table>';										
						
				return formattedTables;
			}
		});			

		jQuery(document).on("click", '.fvrViewDocumentDetailBtn', function() {
			var docUrl = $(this).attr('data-url');
			$('#frame').attr("src", docUrl);
			document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "block";
		});
		
		jQuery(document).on("click", '.ackDetailBtn', function() {
			var ackDetail = JSON.parse($(this).closest('tr').attr('data-key'));
			if(ackDetail) {
				$('#ackQryIntNo').html((ackDetail.intimationNo?ackDetail.intimationNo:''));
				$('#ackQryclaimNo').html((ackDetail.claimNo ? ackDetail.claimNo : '')); 
				$('#ackQryPolicyNo').html((ackDetail.policyNo?ackDetail.policyNo : '')); 
				$('#ackQryAckNo').html((ackDetail.acknowledgementNo ? ackDetail.acknowledgementNo : '')); 
				$('#ackQryRODNo').html((ackDetail.rodNumber ? ackDetail.rodNumber : ''));
				$('#ackQryRcvdFrom').html((ackDetail.receivedFrom ? ackDetail.receivedFrom : ''));
				$('#ackQryBill').html((ackDetail.billClassification ? ackDetail.billClassification : ''));
				$('#ackQryProductName').html((ackDetail.productName?ackDetail.productName:''));
				$('#ackQryClaimType').html((ackDetail.claimType?ackDetail.claimType:''));
				$('#ackQryInsuredName').html((ackDetail.insuredPatientName?ackDetail.insuredPatientName:''));
				$('#ackQryHospName').html((ackDetail.hospitalName?ackDetail.hospitalName:''));
				$('#ackQryHospCity').html((ackDetail.hospitalCity?ackDetail.hospitalCity:''));
				$('#ackQryHospType').html((ackDetail.hospitalType?ackDetail.hospitalType:''));
				$('#ackQryDOA').html((ackDetail.admissionDate?ackDetail.admissionDate:''));
				$('#ackQryDiagnosis').html((ackDetail.diagnosis?ackDetail.diagnosis:''));
				$('#ackQryRaisedRole').html((ackDetail.queryRaiseRole?ackDetail.queryRaiseRole:''));
				$('#ackQryRaisedId').html((ackDetail.queryRaised?ackDetail.queryRaised:''));
				$('#ackQryRaisedDate').html((ackDetail.queryRaisedDateStr?ackDetail.queryRaisedDateStr:''));
				$('#ackQryRemarks').html((ackDetail.queryRemarks?ackDetail.queryRemarks:''));
				$('#ackQryDraftDate').html((ackDetail.queryDraftedDate?ackDetail.queryDraftedDate:'-'));
				$('#ackQryLetterRemarks').html((ackDetail.queryLetterRemarks?ackDetail.queryLetterRemarks:''));
				$('#ackQryDate').html((ackDetail.approvedRejectedDate?ackDetail.approvedRejectedDate:''));
				$('#ackQryRejRemarks').html((ackDetail.rejectedRemarks?ackDetail.rejectedRemarks:''));
				$('#ackQryRedraftRemarks').html((ackDetail.redraftRemarks?ackDetail.redraftRemarks:''));
				$('#ackQryStatus').html((ackDetail.queryStatus?ackDetail.queryStatus:''));
				var ackQryLetter = $(this).closest('tr').attr('data-dms');
				if(ackQryLetter) {
					$('#ackViewQryLetter').attr('data-dms', ackQryLetter);
				}
				$("table.ackQryDetailsTable tr:odd").css("background-color", "#c5e0dc");
				document.getElementsByClassName('ackQryDetailsModal')[0].style.display = "block";
			}
		});
		
		jQuery(document).on("click", '.viewFileBtn', function() {
			var docDetail = JSON.parse($(this).closest('tr').attr('data-docs'));
			var optionName = $(this).closest('tr').attr('data-name');
			$('.coorViewFileBody').html('');
			if(docDetail) {
				for(var i=0; i < docDetail.length; i++) {
					$('.coorViewFileBody').append('<tr><td>' + (i + 1) + '</td><td>' + docDetail[i].fileName +  '</td><td>' +
						(docDetail[i].fileUrl ? '<a data-name="' + optionName +'"' + (docDetail[i].fileUrl.indexOf("docx") == -1 ? 'class="coorViewDocumentDetailBtn"' : 'href="' + docDetail[i].fileUrl + '" target="_blank"' ) + ' data-url="' + docDetail[i].fileUrl : '') + '">View Document </a></td></tr>');
				}
				document.getElementsByClassName('coorViewFileModal')[0].style.display = "block"; 
			}
		});
		
		jQuery(document).on("click", '.coorViewDocumentDetailBtn', function() {
			var docUrl = $(this).attr('data-url');
			$('#frame').attr("src", docUrl);
			$('#frame').parent('div').parent('div').find('.fvrDocumentViewCloseIcon, .fvrDocumentViewCloseBtn').addClass('temp ' + $(this).attr('data-name'));
			$('.coorViewFileModal').addClass('hidden');
			if($(this).attr('data-name') == "coordinator") {
				$('.coordinator-reply-dialog').addClass('hidden');
			} else {
				$('.specialist-trail-dialog').addClass('hidden');
			}
			document.getElementsByClassName('fvrDocumentViewModal')[0].style.display = "block";
		});

		jQuery(document).on("click", '.prevInsDetails', function() {	   
			var insDetail = JSON.parse($(this).closest('tr').attr('data-json'));
			if(insDetail) {
				var optionName = $(this).closest('tr').attr('data-name');
				$('.insuredDetailBody').html('');
				if(insDetail) {
					for(var i=0; i < insDetail.length; i++) {
						$('.insuredDetailBody').append('<tr><td>' + (i + 1) + '</td><td>' + (insDetail[i].insuredName ? insDetail[i].insuredName : '') + '</td><td>' + (insDetail[i].sex ? insDetail[i].sex : '') + '</td><td>' + (insDetail[i].dateOfBirth ? insDetail[i].dateOfBirth : '') + '</td><td>' + (insDetail[i].age ? insDetail[i].age : '') + '</td><td>' + (insDetail[i].relation ? insDetail[i].relation : '') + '</td><td>' + (insDetail[i].sumInsured ? insDetail[i].sumInsured : '0') + '</td> <td>' + (insDetail[i].pedDescription ? insDetail[i].pedDescription : '') + '</td><tr>');	
					}
					document.getElementsByClassName('insuredDetailModal')[0].style.display = "block"; 
				}
			} else {
				alert("Insured Details not found")
			}
		});

	jQuery(document).on("click", '.prevPolicyView', function() {	
		var url = $(this).attr('data-url');
		if (url) {
			window.open(url, "popUp", "resizable=yes");							
		}					   
	});
	
	


		
   </script>
</html>