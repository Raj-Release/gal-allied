<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
    <title>View Bill Summary Details</title>
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
.table-responsive
{
    overflow-x: auto;
}
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
body {
    font-family: "Lato", sans-serif;
    padding-left: 90px;
    padding-right: 90px;
    padding-top: 55px;
}

.table-responsive {
    height: 600px;
}
/* Style the tab */
div.tab {
    overflow: hidden;
    border: 1px solid #ccc;
    background-color: #f1f1f1;
}

/* Style the buttons inside the tab */
div.tab button {
    background-color: inherit;
    float: left;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 14px 16px;
    transition: 0.3s;
    font-size: 13px;
}

/* Change background color of buttons on hover */
div.tab button:hover {
    background-color: #ddd;
}

/* Create an active/current tablink class */
div.tab button.active {
    background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
    display: none;
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-top: none;
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
	<div class="tab">
	  <button class="tablinks" onclick="openCity(event, 'Hospitalisation')" id="defaultOpen">Hospitalisation</button>
	  <button class="tablinks" onclick="openCity(event, 'PreHospitalisation')">PreHospitalisation</button>
	  <button class="tablinks" onclick="openCity(event, 'PostHospitalisation')">PostHospitalisation</button>
	</div>
	<div id="Hospitalisation" class="tabcontent">
  
			    <!-- <h4 class="panel-heading text-center">Status of Intimation No : ${intimation.getIntimationId()}</h4>
			       <a href= "${dmsUrl}" target= "_blank" style="padding-right:15px">View Documents</a> --->
			       <!-- <h4 class="panel-heading text-center">Acknowledgement Details</h4> -->
			       	<div class="row">
        		<div class="col-md-12">
            		<div class="panel panel-default">
                		
                		<div class="panel-body">
                    		<div class="table-responsive">
                    		       <p>Proportinal Deduction</p><br>
                    				<select disabled="disabled">
									  <option value="proportionateflag">${hospitalizationDto.getProrataDeductionFlag()}</option>
									</select><br>
									<p>Proportinal deduction % age</p><br>
									<input type="text" name="proportinal" value="${hospitalizationDto.getProrataPercentage()}"><br>
								<table class="table table-condensed table-bordered table-hover">
								<thead>
									<tr><td><strong>Details</strong></td><td ><strong>No of Days Claimed</strong></td><td ><strong>Per Day Amount</strong></td><td ><strong>Claimed Amount<br>(C)=A*B</strong></td>
									<td ><strong>No of Days<br>Allowed</strong></td><td ><strong>PerDay Amount<br>(Product Based)</strong></td><td ><strong>Amount</strong></td><td ><strong>Non Payable<br>(Product Based)</strong></td><td ><strong>Non Payable</strong></td>
									<td ><strong>Proportioanate<br>Deduction</strong></td><td ><strong>Reasonable<br>Deduction</strong></td><td ><strong>Total Disallowances</strong></td><td ><strong>Net Payable<br>Amount</strong></td><td ><strong>Deductiable /<br>Non Payable<br>Reason</strong></td>
									
							        <td ><strong>Deductible /<br>Non Payables<br>Reason - Billing</strong></td><td ><strong>Deductible /<br>Non Payables<br>Reason - FA</strong></td></tr>
								</thead>
								<tbody>
									<c:forEach items="${hospitalizationDto.getHospitalizationTabSummaryList()}" var="ack">
									
									<tr><td>${ack.getItemName()}</td><td>${ack.getNoOfDays()}</td><td>${ack.getPerDayAmt()}</td><td>${ack.getItemValue()}</td>
									<td>${ack.getNoOfDaysAllowed()}</td><td>${ack.getPerDayAmtProductBased()}</td><td>${ack.getAmountAllowableAmount()}</td><td>${ack.getNonPayableProductBased()}</td><td>${ack.getNonPayable()}</td>
									<td>${ack.getProportionateDeduction()}</td><td>${ack.getReasonableDeduction()}</td><td>${ack.getTotalDisallowances()}</td><td>${ack.getNetPayableAmount()}</td><td>${ack.getDeductibleOrNonPayableReason()}</td>
									<td>${ack.getBillingRemarks()}</td><td></td>
									</tr>
							
									</c:forEach>
									<tr><td>Total</td><td></td><td></td><td>${hospitalizationDto.getTotalClaimedAmt()}</td>
									<td></td><td></td><td>${hospitalizationDto.getTotalApprovedAmt()}</td><td>${hospitalizationDto.getNonpayableProdTotal()}</td><td>${hospitalizationDto.getNonpayableTotal()}</td>
									<td>${hospitalizationDto.getPropDecutTotal()}</td><td>${hospitalizationDto.getReasonableDeducTotal()}</td><td>${hospitalizationDto.getDisallowanceTotal()}</td><td>${hospitalizationDto.getTotalApprovedAmt()}</td><td></td>
									<td></td><td></td></tr>
								</tbody>
								</table>	
							</div>
                		</div>
            		</div>
       		 	</div>
   		 	
		</div>
			       
			</div>
	
		

<div id="PreHospitalisation" class="tabcontent">
  
         	<div class="row">
        		<div class="col-md-12">
            		<div class="panel panel-default">
                		
                		<div class="panel-body">
                    		<div class="table-responsive">
								<table class="table table-condensed table-bordered table-hover">
								<thead>
									<tr><td><strong>Details</strong></td><td ><strong>Claimed Amount(A)</strong></td><td ><strong>Non Payable(B)</strong></td><td ><strong>Reasonable Deduction(C)</strong></td>
									<td ><strong>Net Amount(D)</strong></td><td ><strong>Deductible / Non Payable Reason</strong></td>
							        <td ><strong>Deductible /<br>Non Payables<br>Reason - Billing</strong></td><td ><strong>Deductible /<br>Non Payables<br>Reason - FA</strong></td></tr>
								</thead>
								<tbody>
									<c:forEach items="${preHospitalisationDto.getPreHospitalizationTabSummaryList()}" var="ack">
									
									<tr><td>${ack.getDetails()}</td><td>${ack.getClaimedAmt()}</td><td>${ack.getBillingNonPayable()}</td>
									<td>${ack.getReasonableDeduction()}</td><td>${ack.getNetAmount()}</td><td>${ack.getReason()}</td><td>${ack.getReason()}</td><td>${ack.getReason()}</td>
									</tr>
							
									</c:forEach>
									<tr><td>Total</td><td>${preclaimedAmt}</td><td>${preNonPayableAmt}</td>
									<td></td><td>${prePayableAmt}</td><td></td><td></td><td></td></tr>
								</tbody>
								</table>
							</div>
                		</div>
            		</div>
       		 	</div>
   		 	
		</div>
</div>

<div id="PostHospitalisation" class="tabcontent">

  <div class="row">
        		<div class="col-md-12">
            		<div class="panel panel-default">
                		
                		<div class="panel-body">
                    		<div class="table-responsive">
								<table class="table table-condensed table-bordered table-hover">
								<thead>
									<tr><td><strong>Details</strong></td><td ><strong>Claimed Amount(A)</strong></td><td ><strong>Non Payable(B)</strong></td><td ><strong>Reasonable Deduction(C)</strong></td>
									<td ><strong>Net Amount(D)</strong></td><td ><strong>Deductible / Non Payable Reason</strong></td>
							        <td ><strong>Deductible /<br>Non Payables<br>Reason - Billing</strong></td><td ><strong>Deductible /<br>Non Payables<br>Reason - FA</strong></td></tr>
								</thead>
								<tbody>
									<c:forEach items="${postHospitalisationDto.getPostHospitalizationTabSummaryList()}" var="ack">
									
									<tr><td>${ack.getDetails()}</td><td>${ack.getClaimedAmt()}</td><td>${ack.getBillingNonPayable()}</td>
									<td>${ack.getReasonableDeduction()}</td><td>${ack.getNetAmount()}</td><td>${ack.getReason()}</td><td>${ack.getReason()}</td><td>${ack.getReason()}</td>
									</tr>
							
									</c:forEach>
									<tr><td>Total</td><td>${postclaimedAmt}</td><td>${postNonPayableAmt}</td>
									<td></td><td>${postPayableAmt}</td><td></td><td></td><td></td></tr>
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

<script>
function openCity(evt, cityName) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}

// Get the element with id="defaultOpen" and click on it
document.getElementById("defaultOpen").click();
</script>
     
</body>
</html> 
