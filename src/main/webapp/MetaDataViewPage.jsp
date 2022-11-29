
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="org.apache.http.HttpRequest"%>
<%@ page import="com.shaic.ims.bpm.claim.BPMClientContext" %>
<%@ page import="com.shaic.arch.SHAUtils" %>
<%@ page import="java.util.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<style>
.panel.with-nav-tabs .panel-heading{
    padding: 5px 5px 0 5px;
	background-color: #003366;
	font-size: smaller;
}

.panel.with-nav-tabs .nav-tabs{
	border-bottom: none;
}
.panel.with-nav-tabs .nav-justified{
	margin-bottom: -1px;
}
.panel-body {
    padding: 2px;
}

/*** PANEL PRIMARY ***/
.with-nav-tabs.panel-primary {
	border-color: #003366;
	border-left-width: 5px;
	border-right-width: 5px;
	border-bottom-width: 5px;
}
.with-nav-tabs.panel-primary .nav-tabs > li > a,
.with-nav-tabs.panel-primary .nav-tabs > li > a:hover,
.with-nav-tabs.panel-primary .nav-tabs > li > a:focus {
    color: #fff;
}
.with-nav-tabs.panel-primary .nav-tabs > .open > a,
.with-nav-tabs.panel-primary .nav-tabs > .open > a:hover,
.with-nav-tabs.panel-primary .nav-tabs > .open > a:focus,
.with-nav-tabs.panel-primary .nav-tabs > li > a:hover,
.with-nav-tabs.panel-primary .nav-tabs > li > a:focus {
	color: #fff;
	background-color: #3071a9;
	border-color: transparent;
}
.with-nav-tabs.panel-primary .nav-tabs > li.active > a,
.with-nav-tabs.panel-primary .nav-tabs > li.active > a:hover,
.with-nav-tabs.panel-primary .nav-tabs > li.active > a:focus {
	color: #428bca;
	background-color: #fff;
	border-color: #428bca;
	border-bottom-color: transparent;
}

#customers {
  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
  border-collapse: collapse;
  
}

#customers td, #customers th {
  border: 1px solid #ddd;
  padding: 1px 5px 1px 5px;
  font-size: smaller;
}

#customers tr:nth-child(even){background-color: #f2f2f2;}

#customers tr:hover {background-color: #ddd;}

#customers th {
  padding-top: 7px;
  padding-bottom: 7px;
  text-align: left;
  background-color: #003366;
  color: white;
}

.container { overflow: hidden; }

.floatLeft { width: 30%; float: left; }
.floatRight {width: 40%; float: right; }


</style>
</head>
<body>
<%!
	BPMClientContext bpmClientContext;
	String flpurl;	
	String doctorurl;
	Map<Long,String> cpuCodeMap;
	Map<Long,String> divisionCodeMap;	
 %>
 <%
 	bpmClientContext = new BPMClientContext();
 	flpurl = bpmClientContext.getMetabaseFLPViewUrl();
 	doctorurl = bpmClientContext.getMetabaseDoctorViewUrl();
 	cpuCodeMap = SHAUtils.getCPUCodeMapForJSP();
 	divisionCodeMap = SHAUtils.getDivisionCodeMapForJSP();
 %>
	<div class="row">
		<div class="col-md-12">
			<div class="panel with-nav-tabs panel-primary">
				<div class="panel-heading">
					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab" href="#tab1primary">FLP View</a></li>
						<li><a data-toggle="tab" href="#tab2primary">Doctor View</a></li>
						<li><a data-toggle="tab" href="#tab3primary">CPU Details</a></li>
					</ul>
				</div>
				<div class="panel-body">
					<div class="tab-content">
						<div class="tab-pane fade in active" id="tab1primary">
							<iframe src="<%=flpurl%>" style="height: 680px; width: 100%;"></iframe>
						</div>
						<div class="tab-pane fade" id="tab2primary">
							<iframe src="<%=doctorurl%>" style="height: 680px; width: 100%;"></iframe>
						</div>
						<div class="tab-pane fade" id="tab3primary">
							<div class="container">
								<div class="floatLeft">
									<table id="customers">
										<tr>
											<th>CPU CODE</th>
											<th>DESCRIPTION</th>
										</tr>
										<%
														for (Map.Entry<Long,String> cpucode : cpuCodeMap.entrySet()) {
															out.print("<tr>");
															out.print("<td>" + cpucode.getKey() + "</td>");
															out.print("<td>" + cpucode.getValue() + "</td>");
															out.print("</tr>");
														}
													%>
									</table>
								</div>
								<div style="right: 410px; position: relative;">
									<div class="floatRight">
										<table id="customers">
											<tr>
												<th>DIVISION CODE</th>
												<th>DESCRIPTION</th>
											</tr>
											<%
												for (Map.Entry<Long, String> divisionCode : divisionCodeMap
														.entrySet()) {
													out.print("<tr>");
													out.print("<td>" + divisionCode.getKey() + "</td>");
													out.print("<td>" + divisionCode.getValue() + "</td>");
													out.print("</tr>");
												}
											%>
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
</body>
</html>