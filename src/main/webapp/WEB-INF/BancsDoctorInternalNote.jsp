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
<title>ClaimDetails</title>
</head>
<body>
	 <%
         session.setAttribute("docRemarks", request.getAttribute("docRemarks"));
      %>
	<div class="container"> 
		<div class="page-header"> 
    		<h4 class="panel-heading text-center"><%-- Status of Intimation No : ${intimation.getIntimationId()} --%></h4>
		</div>
		<!-- <div class="col-md-12"> -->
					<div class="panel panel-default">
					  <!--  <div class="panel-heading"> -->
						  <p class = "text-center" style="background-color: #003366 !important;color:#f7fff7"><strong>Doctor Remarks</strong></p>
					   <!-- </div> -->
					   <div class="panel-body">
						  <div class="table-responsive">
							 <table class="table table-condensed table-bordered table-hover">
								<thead>
								   <tr style="background-color: #003366 !important;color:#f7fff7;text-align: center;font-size: 11px;">
									   
									   <td><strong>Date/Time</strong></td>
									   <td><strong>User Name/ID</strong></td>
									   <td><strong>Transaction</strong></td>
									   <td><strong>Transaction Type</strong></td>
									   <td><strong>Remarks</strong></td>
							
								   </tr>
								</thead>
								<tbody>
								   <c:forEach items="${docRemarks}" var="docRemarks">
									  <tr>
										 
										 <td>${docRemarks.getStrNoteDate()}</td>
										 <td>${docRemarks.getUserId()}</td>
										 <td>${docRemarks.getTransaction()}</td>
										 <td>${docRemarks.getTransactionType()}</td>
										 <td>${docRemarks.getRemarks()}</td>
									  </tr>
								   </c:forEach>
								</tbody>
							 </table>
						  </div>
					   </div>
					</div>
			<!-- </div> -->

	</div>
</body>
</html>