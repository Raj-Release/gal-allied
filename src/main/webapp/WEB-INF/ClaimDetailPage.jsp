<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
    <%@page import="org.apache.http.HttpRequest"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Claim status</title>
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
	
	
 %>

<div class="container">

<div class="page-header"> 
    <h4 class="panel-heading text-center">Status of Intimation No : ${intimation.getIntimationId()}</h4>
     <!--<a href= "${dmsUrl}" target= "_blank" style="padding-right:15px">View Documents</a> -->
       <%-- <a href= "${dmsUrl}" target= "_blank" style="padding-right:15px">View Documents</a> --%>
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
                    	<td><p id="setIntimationCreateDate"></p></td>
                    	<td><strong>Policy Issuing Office </strong></td>
                    	<td>${intimation.getPolicy().getHomeOfficeCode()}</td>
                    	
                    </tr>
                    <tr>
                    	<td><strong>CPU Code </strong></td>
                    	<td>${intimation.getCpuCode().getCpuCode()}</td>
                    	<td><strong>Product Name </strong></td>
                    	<td>${intimation.getPolicy().getProduct().getValue()}</td>
                    	
                    </tr>      
                     <tr>
                    	<td><strong>Insured Patient Name </strong></td>
                    	<td>${intimation.getInsured().getInsuredName()} </td>
                    	<td><strong>Proposer  Name </strong></td>
                    	<td>${intimation.getPolicy().getProposerFirstName()}</td>
                    	
                    </tr>
                     <tr>
                     <td><strong>Health Card No </strong></td>
                    	<td>${intimation.getInsured().getHealthCardNumber()}</td>
                    	<td><strong>Hospital Name </strong></td>
                    	<td>${hospitals.getName()} </td>
                    	
                    </tr>
                    
                    <tr>
                    <td><strong>Admission Date </strong></td>
                       <td><p id="setAdmissionDate"></p></td>
                    	<td><strong>Address </strong></td>
                    	<td>${hospitals.getAddress()}</td>
                    	
                    </tr>
                    <tr>
                   		<td><strong>Reason of Admission </strong></td>
                    	<td>${intimation.getAdmissionReason()}</td>
                    	<td ><strong>Hospital Type </strong></td>
                    	<td>${intimation.getHospitalType().getValue()}</td>
                    </tr>
                    <tr>
                    	
                    	<td><strong>SM Code </strong></td>
                    	<td>${intimation.getPolicy().getSmCode()}</td>
                    	<td ><strong>Network Hospital Type</strong></td>
                    	<td>${ hospitals.getNetworkHospitalType()}</td>
                    </tr>
                   
                    <tr>
                    	<td><strong>SM Name </strong></td>
                    	<td>${intimation.getPolicy().getSmName()}</td>
                    	<td><strong>Hospital Code(Internal) </strong></td>
                    	<td>${hospitals.getHospitalCode()}</td>
                    	
                    </tr>
              
                    <tr>
                    	<td><strong>Agent/Broker Code </strong></td>
                    	<td> ${intimation.getPolicy().getAgentCode()}</td>
                    	<td><strong>Hospital Code(IRDA) </strong></td>
                    	<td>${hospitals.getHospitalIrdaCode()}</td>
                    </tr>
                     <tr>
                    	
                    	<td><strong>Agent/Broker Name </strong></td>
                    	<td>${intimation.getPolicy().getAgentName()}</td>
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
                   			<td></td><td></td>
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
		<tr><td ><strong>S.No</strong></td><td ><strong>Reference Number </strong></td><td ><strong>Reference Type</strong></td><td ><strong>Treatment Type</strong></td><td ><strong>Requested Amount</strong></td><td ><strong>Approved Amount</strong></td>
		<td ></td>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${cashless}" var="cashless">
		
		<tr><td >${cashless.getSno()}</td><td >${cashless.getReferenceNo()}</td><td>${cashless.getReferenceType()}</td><td >${cashless.getTreatementType()}</td><td >${cashless.getRequestedAmt()}</td><td>${cashless.getApprovedAmt()}</td>
		
		<%-- <td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&cashless_num=${cashless.getKey()}" target="_blank">View Details</a></form></td> --%>
		</tr>
		</c:forEach>
		
		<tr><td ><strong></strong></td><td ><strong> </strong></td><td ><strong></strong></td><td ><strong>Total Amount</strong><td ><strong>${requestedAmt}</strong></td></td><td ><strong>${totalApprovedAmount}</strong></td><td ><strong> </strong></td></tr>
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
            	<div class="panel panel-default-left">
                <div class="panel-heading">
                    <p class="text-left"><strong>Receipt of Documents and Medical Processing</strong></p>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	 <thead>
		
		<tr><td ><strong>ROD No</strong></td><td ><strong>Document Received From </strong></td><td ><strong>Document Received Date</strong></td><td ><strong>Medical Response Date & time</strong></td><td ><strong>Claimed Amount</strong></td><td ><strong>Mode of Receipt</strong></td>
		<td ><strong>Bill Classification</strong></td><td ><strong>Status</strong></td><td ><strong>Remarks</strong></td><td ><strong>Letter Generated Date</strong></td><td ><strong>No of Reminder Letters Generated (Query)</strong></td><td ><strong>Reminder Date(Last remainder letter date)</strong></td><td></td><td></td><td></td></tr>
		</thead>
		<tbody>
		<c:forEach items="${reimbursementList}" var="reimbursement">
		
		<tr><td>${reimbursement.getReimbursementNo()}</td><td>${reimbursement.getDocumentReceivedFrom()}</td><td >${reimbursement.getDocumentReceivedDate()}</td><td >${reimbursement.getMedicalResponseDate()}</td><td >${reimbursement.getAmount()}</td><td >${reimbursement.getModeOfRececipt()}</td><td >${reimbursement.getModeOfRececipt()}</td><td >${reimbursement.getBillClassification()}</td>
		<td >${reimbursement.getStatus()}</td><td >${reimbursement.getRemarks()}</td><td ></td>
		<td ></td>
		<%-- <td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&reimb_num=${reimbursement.getReimbursementKey()}" target="_blank">View Acknowledgement Details</a></form></td>
		<td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&rod_num=${reimbursement.getReimbursementNo()}" target="_blank">View Rejection Details</a></form></td>
		<td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?intim_num=${intimation.getIntimationId()}&rod_num=${reimbursement.getReimbursementNo()}" target="_blank">View Query Details</a></form></td> --%></tr>

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
		<tr><td ><strong>S.No</strong></td><td ><strong>Type </strong></td><td ><strong>Billing Completed Date</strong></td><td ><strong>Bill Assessment Amount</strong></td><td ><strong>Status</strong></td><td></td><td></td>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${billing}" var="item">
		
		<tr><td >${item.getSno()}</td><td >${item.getBillingType()}</td><td>${item.getBillingDate()}</td><td >${item.getBillAssessmentAmt()}</td><td >${item.getStatus()}</td>
		<%-- <td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&bill_details=${item.getRodKey()}" target="_blank">View Bill Details</a></form></td>
		<td><form action = "ClaimStatusServlet" method="post"><a href="claimstatus?int_num=${intimation.getIntimationId()}&bill_summary=${item.getRodKey()}" target="_blank">View Bill Summary</a></form></td> --%>
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
                    <p class = "text-left"><strong>Financial Approval</strong></p>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	<thead>
		<tr><td ><strong>S.No</strong></td><td ><strong>Document Received From</strong></td><td ><strong>Type </strong></td><td ><strong>FA Date</strong></td><td ><strong>Amount</strong></td><td ><strong>Status</strong></td>
		<td ><strong>Type Of Payment</strong></td><td ><strong>Cheque/Transaction No</strong></td><td ><strong>Cheque/Transaction-Date </strong></td><td ><strong>FA Remarks(Approval/Rejection/Query/Other)</strong></td></tr>
		</thead>
		<tbody>
		<c:forEach items="${financial}" var="item">
		
		<tr><td >${item.getSno()}</td><td >${item.getDocumentReceivedFrom()}</td><td >${item.getApprovalType()}</td><td>${item.getFaDate()}</td><td >${item.getAmount()}</td><td >${item.getStatus()}</td>
		<td >${item.getPaymentType()}</td><td >${item.getTransactionNo()}</td><td >${item.getTransactionDate()}</td><td>${item.getFinancialRemarks()}</td></tr>

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
                    <p class = "text-left"><strong>Payment Details</strong></p>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	<thead>
		<tr><td ><strong>S.No</strong></td><td ><strong>ROD No</strong></td><td ><strong>Claim Type</strong></td><td ><strong>Payment Type</strong></td><td ><strong>Bank Name</strong></td>
		<td ><strong>Account No</strong></td><td ><strong>IFSC Code</strong></td><td ><strong>Branch Name</strong></td><td ><strong>Cheque Date</strong></td><td ><strong>Cheque No</strong></td></tr>
		</thead>
		<tbody>
		<c:forEach items="${payment}" var="payment">
		
		<tr><td >${payment.getSerialNo()}</td><td >${payment.getRodNo()}</td><td>${payment.getClaimType()}</td><td >${payment.getPaymentType()}</td><td >${payment.getBankName()}</td>
		<td >${payment.getAccountNumber()}</td><td >${payment.getIfscCode()}</td><td >${payment.getBranchName()}</td><td>${payment.getChequeDateValue()}</td><td>${payment.getChequeNo()}</td></tr>

		</c:forEach>
		</tbody>
	</table>	
			</div>
                </div>
            </div>
        </div>
    </div>
</div>	

<input type="text" style="border: none;visibility: hidden;" id="getIntimationCreateDate" value="${intimation.getCreatedDate()}">
<input type="text" style="border: none;visibility: hidden;" id="getAdmissionDate" value="${intimation.getAdmissionDate()}">	
	
</body>

	<script type="text/javascript">
		$(document).ready(function(){
				
				var ict = String(document.getElementById('getIntimationCreateDate').value);
				document.getElementById('setIntimationCreateDate').innerHTML = ict.charAt(8)+""+ict.charAt(9)+"/"+ict.charAt(5)+""+ict.charAt(6)+"/"+ict.charAt(0)+""+ict.charAt(1)+""+ict.charAt(2)+""+ict.charAt(3)+" "+ict.charAt(11)+""+ict.charAt(12)+":"+ict.charAt(14)+""+ict.charAt(15)+":"+ict.charAt(17)+""+ict.charAt(18);
    			
    			var adt = String(document.getElementById('getAdmissionDate').value);
    				document.getElementById('setAdmissionDate').innerHTML = adt.charAt(8)+""+adt.charAt(9)+"/"+adt.charAt(5)+""+adt.charAt(6)+"/"+adt.charAt(0)+""+adt.charAt(1)+""+adt.charAt(2)+""+adt.charAt(3)+""+adt.charAt(4);
    		
		});
		
		function pop1() {
			
			window.open('','rejectionDetails','width:650, height: 500');
		}
		
		function pop2() {
			
			window.open('','queryDetails','width:650, height: 500');
		}
	</script>

</html>