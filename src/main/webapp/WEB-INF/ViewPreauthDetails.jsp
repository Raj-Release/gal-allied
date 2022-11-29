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
.col-xs-13 {
	align-content: spacebetween;
	width: 1680px;
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

.radiobtn {
	display: inline-block;
	
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
	session.setAttribute("preauthDetails", request.getAttribute("preauthDetails"));
	session.setAttribute("specialistConsulted", request.getAttribute("specialistConsulted"));
	
 %>

<div class="container">

<div class="page-header"> 
   
       <b>Preauth Reference No :${claimNumber}</b> 
</div>
	<div class="row">
                <div class="col-xs-12 col-md-12 col-lg-12 pull-left" >
                    <div class="panel panel-default height">
                        <div class="panel-heading"><b></b></div>
                        <div class="panel-body">

		<table class="table table-condensed">
			<tbody>
                   <tr>
                       <td><strong>Preauth Received Data & Time</strong></td>
                       <td><p id="setReceivedDate"></p></td>
                       <td><strong>Pre-auth Response Date & Time </strong></td>
                    	<td> <p id="setResponseDate"></p></td>
                       
                    </tr> 
                    <tr>
                    	<td><strong>Status</strong></td>
                    	<td>${preauthDto.getStatusValue()}</td>
                    	<td><strong>Remarks</strong></td>
                    	<td>${preauthDetails.getRemarks()}</td>
                    	
                    </tr>
                    <tr>
                    	<td><strong>Pre-auth Approved Amt</strong></td>
                    	<td>${preauthDto.getPreauthDataExtractionDetails().getTotalApprAmt()}</td>
                    	<%-- <td><strong>Doctor Note (Internal remarks)</strong></td>
                    	<td>${preauthDto.getDoctorNote()}</td> --%>
                    	
                    </tr>  
                    <tr>
                    	<td><strong>Medical Remarks</strong></td>
                    	<td>${preauthDetails.getMedicalRemarks()}</td>
                    </tr>    
                     <tr>
                    	<td><strong>Date of Admission </strong></td>
                    	<td>${preauthDto.getPreauthDataExtractionDetails().getAdmissionDate()} </td>
                    	<td><strong>Reason For Admission</strong></td>
                    	<td>${preauthDto.getReasonForAdmission()}</td>
                    	
                    </tr>
                     <tr>
                     <td><strong>Reason For Change in DOA</strong></td>
                    	<td>${preauthDto.getPreauthDataExtractionDetails().getChangeInReasonDOA()}</td>
                    	<td><strong>No of Days</strong></td>
                    	<td>${preauthDto.getPreauthDataExtractionDetails().getNoOfDays()} </td>
                    	
                    </tr>
                    
                    <tr>
                    <td><strong>Room Category</strong></td>
                       <td>${preauthDto.getPreauthDataExtractionDetails().getRoomCategory().getValue()}</td>
                    	<td><strong>Nature of Treatment</strong></td>
                    	<td>${preauthDto.getPreauthDataExtractionDetails().getNatureOfTreatment().getValue()}</td>
                    	
                    </tr>
                    <tr>
                   		<td><strong>Specify</strong></td>
                   		<td>${preauthDto.getPreauthDataExtractionDetails().getSpecifyIllness().getValue()}</td>
                    	<td ><strong>1st Consultation Date </strong></td>
                    	<td>${preauthDetails.getConsultationDate()}</td>
                    	<td></td>
                    </tr>
                    <tr>
                    	
                    	<td><strong>Treatment Type</strong></td>
                    	<td>${preauthDetails.getTreatmentType().getValue()}</td>
                    	<td ><strong>Automatic Restoration</strong></td>
                    	<td>${preauthDetails.getAutoRestoration()}</td>
                    </tr>
                   
                    <tr>
                    	<td><strong>Patient Status</strong></td>
                    	<td>${preauthDetails.getPatientStatus().getValue()}</td>
                    	<td><strong>Illness</strong></td>
                    	<td>${preauthDetails.getIllness().getValue()}</td>
                    	
                    </tr>
              
                    <tr>
                    	<td><strong>Date of Death </strong></td>
                    	<td> ${preauthDetails.getDateOfDeath()}</td>
                    	<td><strong>Relapse of Illness</strong></td>
                    	<td></td>
                    </tr>
                     <tr>
                    	
                    	<td><strong>Reason for Death </strong></td>
                    	<td>${preauthDetails.getDeathReason()}</td>
                    	<td><strong>Remarks (Relapse)</strong></td>
                    	<td>${preauthDetails.getRelapseRemarks()}</td>
                    </tr>
                       <tr>
                    	
                    	<td><strong>Terminate Cover </strong></td>
                    	<td>${preauthDetails.getTerminatorCover()}</td>
                    	<td><strong>Attach to Previous Claim</strong></td>
                    	<td></td>
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
                    <p class = "text-left"><strong>Procedure List</strong></p> 
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	<thead>
		<tr><td ><strong>Procedure</strong></td><td ><strong>Procedure Code</strong></td><td ><strong>Package Rate</strong></td><td ><strong>DayCare Procedure</strong></td><td ><strong>Consider for Day Care</strong></td>
		<td ><strong>Sub Limit Applicable</strong></td><td ><strong>Sub Limit Name</strong></td><td ><strong>Sub Limit Desc</strong></td><td ><strong>Sub Limit Amt</strong></td><td ><strong>Consider for Payment</strong></td><td ><strong>Remarks</strong></td></tr>
		</thead>
		<tbody>
		<c:forEach items="${procedureList}" var="procedureDetails">
		
		<tr><td >${procedureDetails.getProcedure()}</td><td>${procedureDetails.getProcedureCode()}</td><td >${procedureDetails.getProcedurePackageRate()}</td><td >${procedureDetails.getDayCareProcedure()}</td><td>${procedureDetails.getConsiderForDayCare()}</td>
		<td>${procedureDetails.getSubLimitApplicable()}</td><td>${procedureDetails.getSubLimitName()}</td><td>${procedureDetails.getSubLimitDesc()}</td><td>${procedureDetails.getSubLimitAmt()}</td><td>${procedureDetails.getConsiderForPayment()}</td><td>${procedureDetails.getRemarks()}</td></tr>

		</c:forEach>
		
		</tbody>
			</table>	
			</div>
                </div>
            </div>
        </div>
    </div>
    
   <%--  <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <p class = "text-left"><strong>Speciality</strong></p> 
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	<thead>
		<tr><td ><strong>Speciality</strong></td><td ><strong>Remarks</strong></td>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${speciality}" var="speciality">
		
		<tr><td >${speciality.getSpecialityType()}</td><td>${speciality.getRemarks()}</td>
		</tr>

		</c:forEach>
		
		</tbody>
			</table>	
			</div>
                </div>
            </div>
        </div>
    </div> --%>
   <%--  <div class="row">
      <div class="col-xs-12 col-md-12 col-lg-12 pull-left" >
                    <div class="panel panel-default height">
                        <div class="panel-heading"></div>
                        <div class="panel-body">

		<table class="table table-condensed">
			<tbody>
                   <tr>
                   		
                       <td><strong>Initiate Field Visit Request</strong></td>
                      <c:if test="${preauthDetails.getInitiateFvr() == '1'}"> 
     					 <div class="radiobtn"><td><input type="radio" name="branchfld" value="YES">YES</td>
                       		<td><input type="radio" name="branchfld" value="NO" checked>NO</td>
							</div>
						</c:if>
							<c:if test="${preauthDetails.getInitiateFvr() == '0'}">
     					 <div class="radiobtn"><td><input type="radio" name="branch" value="YES">YES</td>
                       		<td><input type="radio" name="branch" value="NO" checked>NO</td>
							</div>
							</c:if>
                       <!-- <td style="width:50%"><input type="radio" name="branch" value="YES" checked>YES</td>
                       <td><input type="radio" name="branch" value="NO">NO</td> -->
                       <td><strong>Specialist Opinion Taken</strong></td>
                    	 <c:if test="${preauthDetails.getSpecialistOpinionTaken() == '1'}">
     					 <div class="radiobtn"><td><input type="radio" name="branch" value="YES" checked>YES</td>
                       		<td><input type="radio" name="branch" value="NO">NO</td>
							</div>
							</c:if>
							<c:if test="${preauthDetails.getSpecialistOpinionTaken() == '0'}">
     					 <div class="radiobtn"><td><input type="radio" name="branch" value="YES" >YES</td>
                       		<td><input type="radio" name="branch" value="NO" checked>NO</td>
							</div>
							</c:if>
                    </tr> 
                    <tr>
                    	<td><strong>FVR Not Required Remarks</strong></td>
                    	<td>${preauthDetails.getFvrNotRequiredRemarks()}</td>
                    	<td><strong>Specialist Type</strong></td>
                    	<td>${preauthDetails.getSpecialistType().getValue()}</td>
                    	
                    </tr>
                    <tr>
                    	<td><strong>Allocation to</strong></td>
                    	<td>${fieldVisitByPreauthKey.getAllocationTo().getValue()}</td>
                    	<td><strong>Specialist Consulted</strong></td>
                    	<td>${specialistConsulted}</td>
                    	
                    </tr>  
                    <tr>
                    	<td><strong>FVR Trigger Points</strong></td>
                    	<td>${fieldVisitByPreauthKey.getFvrTriggerPoints()}</td>
                    	<td><strong>Remarks by Specialist</strong></td>
                    	<td>${preauthDetails.getSpecialistRemarks()}</td>
                    </tr>    
                     <tr>
                    	<td><strong>Investigator Name</strong></td>
                    	<td> </td>
                    	<td><strong>Remarks for CPU</strong></td>
                    	<td></td>
                    	
                    </tr>
                     <tr>
                     <td><strong>Investigation Review Remarks</strong></td>
                    	<td></td>
                    </tr>
			</tbody>
		</table>
		</div>
       </div>
      </div>
    </div>  --%>
 
<div class="row">
        <div class="col-md-12">
        <div class="col-xs-13">
            <div class="panel panel-default">
            	<div class="panel panel-default-left">
                <div class="panel-heading">
                    <p class="text-left"><strong>Sub limits, Package & SI Restriction Table</strong></p>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	 <thead>
		
		<tr><td ><strong>Diagnosis / Procedure</strong></td><td ><strong>Description</strong></td><td ><strong>PED / Exclusion Details</strong></td><td ><strong>Ambulance Charge Applicable</strong></td><td ><strong>Ambulance Charges</strong></td><td ><strong>Amount Considered</strong></td>
		<td ><strong>Package Amount</strong></td><td ><strong>Min of A,B, (C)</strong></td><td ><strong>Co-pay % (D)</strong></td><td ><strong>Co-pay Amount(E)</strong></td><td ><strong>Net Amount(After Co-pay)(F)</strong></td><td ><strong>Amount With Ambulance Charges</strong></td><td><strong>Sublimit Amount(G)</strong></td>
		<td><strong>Sublimit Utilized Amount(H)</strong></td><td><strong>Sublimit Available Amount(I)</strong></td><td><strong>SI Restriction(J)</strong></td><td><strong>SI Restriction Utilized Amt(K)</strong></td><td><strong>SI Restriction Available Amount(L)(J-K)</strong></td><td><strong>Min of column(F,I,L)(M)</strong></td><td><strong>Apportion Final App Amt</strong></td></tr>
		</thead>
		<tbody>
		<c:forEach items="${medicalDecisionValues}" var="reimbursement">
		
		<tr><td>${reimbursement.getDiagOrProcedure()}</td><td>${reimbursement.getDescription()}</td><td >${reimbursement.getPedOrExclusion()}</td><td >${reimbursement.getAmbulanceCharge()}</td><td >${reimbursement.getAmtWithAmbulanceCharge()}</td><td >${reimbursement.getAmountConsidered()}</td><td >${reimbursement.getPackageAmt()}</td><td >${reimbursement.getMinimumAmountOfAmtconsideredAndPackAmt()}</td>
		<td ><c:choose>
   				 <c:when test="${reimbursement.getDiagnosisDetailsDTO() != null}">
       		 		${reimbursement.getDiagnosisDetailsDTO().getCopayPercentage()}
    				</c:when>
   			 	<c:otherwise>
      			  ${reimbursement.getCoPayPercentage()}
    			</c:otherwise>
			</c:choose></td>
		<td ><c:choose>
   				 <c:when test="${reimbursement.getDiagnosisDetailsDTO() != null}">
       		 		${reimbursement.getDiagnosisDetailsDTO().getCopayAmount()}
    				</c:when>
   			 	<c:otherwise>
      			  ${reimbursement.getCoPayAmount()}
    			</c:otherwise>
			</c:choose></td><td>
				<c:choose>
   				 <c:when test="${reimbursement.getDiagnosisDetailsDTO() != null}">
       		 		${reimbursement.getDiagnosisDetailsDTO().getNetAmount()}
    				</c:when>
   			 	<c:otherwise>
      			  ${reimbursement.getNetAmount()}
    			</c:otherwise>
			</c:choose></td><td>${reimbursement.getAmtWithAmbulanceCharge()}</td><td >${reimbursement.getSubLimitAmount()}</td><td >${reimbursement.getSubLimitUtilAmount()}</td><td >${reimbursement.getSubLimitAvaliableAmt()}</td><td >${reimbursement.getRestrictionSI()}</td><td >${reimbursement.getUtilizedAmt()}</td><td >${reimbursement.getAvailableAmout()}</td>
		<td >
			<c:choose>
   				 <c:when test="${reimbursement.getDiagnosisDetailsDTO() != null}">
       		 		${reimbursement.getDiagnosisDetailsDTO().getApprovedAmount()}
    				</c:when>
   			 	<c:otherwise>
      			  ${reimbursement.getMinimumAmount()}
    			</c:otherwise>
			</c:choose>
		</td><td >
		<c:choose>
   				 <c:when test="${reimbursement.getDiagnosisDetailsDTO() != null}">
       		 		${reimbursement.getDiagnosisDetailsDTO().getNetApprovedAmount()}
    				</c:when>
   			 	<c:otherwise>
      			  ${reimbursement.getReverseAllocatedAmt()}
    			</c:otherwise>
			</c:choose>
		</td>
		<td ></td></tr>
		</c:forEach>
		<tr><td></td><td></td><td ><b>Total</b></td><td ></td><td ></td><td ><b>${totalDiagConsAmt}</b></td><td ></td><td ></td>
		<td ></td><td></td><td><b><c:choose>
   				 <c:when test="${reimbursement.getDiagnosisDetailsDTO() != null}">
       		 		${reimbursement.getDiagnosisDetailsDTO().getNetAmount()}
    				</c:when>
   			 	<c:otherwise>
      			  ${reimbursement.getMinimumAmount()}
    			</c:otherwise>
			</c:choose></b></td><td ></td><td ></td><td ></td><td ></td><td ></td><td ></td><td ></td>
		<td ><b>${totalDiagPayableAmt}</b></td><td ><b>${totalDiagReverseAllocAmt}</b></td>
		<td ></td></tr>
		</tbody>
			</table>
			</div>
                </div>
              </div>
            </div>  	
        </div>
         </div>
    </div>
     <div class="col-xs-6 col-md-6 col-lg-6">
        <div class="panel panel-default height">
             <div class="panel-heading"><b>A) Amount Considered Table</b></div>
                <div class="panel-body">
					<table  class="table table-condensed table-bordered table-hover">
							<thead>
						<tr><td ><strong>Particulars</strong></td><td ><strong>Amount</strong></td>
						</tr>
						</thead>
						<tbody>
						
						 <c:forEach items="${firstTableValues}" var="speciality">
							<c:if test="${speciality.key == 0}">
								<tr><td >${speciality.value}</td><td>${minimumValue}</td>
								</tr>
						   </c:if>
						   <c:if test="${speciality.key == 1}">
								<tr><td >${speciality.value}</td><td>0</td>
								</tr>
						   </c:if>
						    <c:if test="${speciality.key == 2}">
								<tr><td >${speciality.value}</td><td>${minimumValue}</td>
								</tr>
						   </c:if>
						</c:forEach>  
					</table>	
				</div>
          </div>
 </div> 
    <div class="col-xs-6 col-md-6 col-lg-6">
        <div class="panel panel-default height">
             <div class="panel-heading"><b>B) Balance Sum Insured</b></div>
                 <div class="panel-body">
					<table  class="table table-condensed table-bordered table-hover">
							<thead>
						<tr><td ><strong>Particulars</strong></td><td ><strong>Amount</strong></td>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${secondTableValues}" var="secondtable">
							<c:if test="${secondtable.key == 0}">
								<tr><td >${secondtable.value}</td><td>${balanceSumInsuredAmt}</td>
								</tr>
						   </c:if>
						   <c:if test="${secondtable.key == 1}">
								<tr><td >${secondtable.value}</td><td>0</td>
								</tr>
						   </c:if>
						    <c:if test="${secondtable.key == 2}">
								<tr><td >${secondtable.value}</td><td>${balanceSumInsuredAmt}</td>
								</tr>
						   </c:if>
						</c:forEach>
					</table>	
				</div>
       </div>
 </div>
 
     <div>
         <b>C) Sum limits,package<br> & SI Restriction Amount : ${totalDiagReverseAllocAmt}</b>
            
        </div><br><br>
 
   	 <div class="row">
        		<div class="col-md-12">
        		<div class="col-xs-13">
            		<div class="panel panel-default">
                		
                		<div class="panel-body">
                    		<div class="table-responsive">
                    		       
								<table class="table table-condensed table-bordered table-hover">
								<thead>
									<tr><td><strong>Details</strong></td><td ><strong>No of Days(A)</strong></td><td ><strong>Per Day Amount(B)</strong></td><td ><strong>Claimed Amount<br>(C)</strong></td>
									<td ><strong>Deductible<br>(D)</strong></td><td ><strong>Net Amount<br>(E)</strong></td><td ><strong>No of Days<br>(F)</strong></td><td ><strong>Per Day Amount<br>(Product Based)(G)</strong></td>
									<td ><strong>Amount<br>(H)</strong></td><td ><strong>Considered PerDay<br>Amount</strong></td><td ><strong>Non Payables<br>(Inclu Deductibles)(I)</strong></td><td ><strong>Payable Amount<br>(J)</strong></td><td ><strong>Deductiable /<br>Non Payable<br>Reason</strong></td>
									
							        
								</thead>
								<tbody>
									<c:forEach items="${calimedAmtDetails}" var="ack">
									
									<tr><td>${ack.getDescription()}</td><td>${ack.getTotalBillingDays()}</td><td>${ack.getBillingPerDayAmount()}</td>
									<td>${ack.getClaimedBillAmount()}</td><td>${ack.getDeductibleAmount()}</td><td>${ack.getNetAmount()}</td><td>${ack.getTotalBillingDays()}</td>
									<td>${ack.getPolicyPerDayPayment()}</td><td>${ack.getNetAmount()}</td><td>${ack.getConsiderPerDayAmt()}</td><td>${ack.getNonPayableAmount()}</td><td>${ack.getPaybleAmount()}</td><td>${ack.getNonPayableReason()}</td>
									
									</tr>
							
									</c:forEach>
									<tr><td><b>Total</b></td><td></td><td></td>
									<td><b>${totalClaimedAmtforAmtconsd}</b></td><td><b>${totalDeductAmtforAmtconsd}</b></td><td><b>${totalNetAmtforAmtconsd}</b></td><td></td>
									<td><b>${totalProductAmtforAmtconsd}</b></td><td></td><td><b></b></td><td></td><td><b>${totalPayableAmtforAmtconsd}</b></td><td></td>
									
									</tr>
								</tbody>
								</table>	
							</div>
                		</div>
            		</div>
       		 	</div>
   		 	</div>
		</div> 
</div>	
	<%-- <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <p class = "text-left"><strong>Ped Validation</strong></p>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	<thead>
		<tr><td ><strong>Diagnosis</strong></td><td ><strong>PED Name</strong></td><td ><strong>Policy Ageing</strong></td><td ><strong>PED / Exclusion Impact On Diagnosis</strong></td><td ><strong>Exclusion Details</strong></td>
		<td ><strong>Remarks</strong></td>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${pedValidationDetails}" var="pedDetails">
		
		<tr><td >${pedDetails.getDiagnosis()}</td><td >${pedDetails.getPedName()}</td><td>${pedDetails.getPolicyAgeing()}</td><td >${pedDetails.getImpactOnDiagnosis()}</td><td >${pedDetails.getConsideredForExclusion()}</td>
		<td>${pedDetails.getRemarks()}</td>
		</tr>

		</c:forEach>
		</tbody>
			</table>	
			</div>
                </div>
            </div>
        </div>
    </div> --%>
	<%-- <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <p class = "text-left"><strong>Procedure Exclusion Check</strong></p>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
	<table class="table table-condensed table-bordered table-hover">
	<thead>
		<tr><td ><strong>Procedure Name</strong></td><td ><strong>Procedure Code</strong></td><td ><strong>Package Amount </strong></td><td ><strong>Policy Ageing</strong></td><td ><strong>Procedure Status</strong></td><td ><strong>Exclusion Details</strong></td>
		<td ><strong>Remarks</strong></td></tr>
		</thead>
		<tbody>
		<c:forEach items="${procedureExclusion}" var="procedureList">
		
		<tr><td >${procedureList.getProcedureNameValue()}</td><td >${procedureList.getProcedureCodeValue()}</td><td >${procedureList.getPackageAmount()}</td><td>${procedureList.getPolicyAging()}</td><td >${procedureList.getProcedureStatus()}</td><td >${procedureList.getExclusionDetails()}</td>
		<td >${procedureList.getRemarks()}</td>
		</tr>

		</c:forEach>
		</tbody>
			</table>	
			</div>
                </div>
            </div>
        </div>
    </div> --%>

</div>	 

<input type="text" style="border: none;visibility: hidden;" id="getReceivedDate" value="${preauthDetails.getCreatedDate()}">
<input type="text" style="border: none;visibility: hidden;" id="getResponseDate" value="${preauthDetails.getModifiedDate()}">	
	
</body>

	<script type="text/javascript">
		$(document).ready(function(){
				
				var ict = String(document.getElementById('getReceivedDate').value);
    			if(null != ict && !ict==""){
    			
				 document.getElementById('setReceivedDate').innerHTML = ict.charAt(8)+""+ict.charAt(9)+"/"+ict.charAt(5)+""+ict.charAt(6)+"/"+ict.charAt(0)+""+ict.charAt(1)+""+ict.charAt(2)+""+ict.charAt(3)+" "+ict.charAt(11)+""+ict.charAt(12)+":"+ict.charAt(14)+""+ict.charAt(15)+":"+ict.charAt(17)+""+ict.charAt(18);

    			}
    			var adt = String(document.getElementById('getResponseDate').value);
    			if(null != adt && !adt==""){
    				document.getElementById('setResponseDate').innerHTML = adt.charAt(8)+""+adt.charAt(9)+"/"+adt.charAt(5)+""+adt.charAt(6)+"/"+adt.charAt(0)+""+adt.charAt(1)+""+adt.charAt(2)+""+adt.charAt(3)+" "+adt.charAt(11)+""+adt.charAt(12)+":"+adt.charAt(14)+""+adt.charAt(15)+":"+adt.charAt(17)+""+adt.charAt(18);
    		}
		});
		
		function pop1() {
			
			window.open('','rejectionDetails','width:650, height: 500');
		}
		
		function pop2() {
			
			window.open('','queryDetails','width:650, height: 500');
		}
	</script>

</html>