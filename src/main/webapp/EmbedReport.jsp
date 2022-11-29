<!-- Copyright (c) Microsoft Corporation.
Licensed under the MIT license. -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="base" value="${fn:substring(url, 0, fn:length(url) - fn:length(req.requestURI))}${req.contextPath}/" />
 
<!DOCTYPE html> 
<html lang="en">
<head>
	<title>Embedded Power BI report</title>
	<base href="${base}" />
	<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
	<link href="css/powerbi_style.css" rel="stylesheet" />
	<style type="text/css">
			header {
				background-color: #007FFF;
				height: 75px;
				width: 100%;
			}
			
			header>div {
				color: #FFFFFF;
				font: bold 1.6em "segoe ui", arial, sans-serif;
				margin-left: 32px;
				padding-top: 19px;
			}
			
			main {
				margin: 0 auto;
				width: 100%;
			}
			
			section#text-container>div>div {
				font: 1.2em "segoe ui", arial, sans-serif;
			}
			
			section#report-container {
				 height: calc(0.5625 * 70vw); /* 16:9 aspect ratio */ 
			}
			
			@media only screen and (max-width: 575px) {
				section#report-container {
					 height: calc(0.5625 * 100vw); /* 16:9 aspect ratio */ 
				}
			}
			
			footer>p {
				font: 1em "segoe ui", arial, sans-serif;
			}
			
			iframe {
				border: none;
			}
	</style>
</head>
<body>
	<main class="row">
		<section id="report-container" class="embed-container col-lg-offset-4 col-lg-12 col-md-offset-5 col-md-12 col-sm-offset-5 col-sm-12 mt-5"></section>
		<!-- Used to display report embed error message -->
		<section class="error-container m-5"></section>
	</main>
	
	<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
	
	<!-- powerbi-client v2.10.3 -->
	<script type="text/javascript" src="js/powerbi.min.js"></script>
	<script>var baseUrl = '${base}';</script>
	<script>
		$(function () {
			models = window['powerbi-client'].models;
					
			reportContainer = $("#report-container").get(0);
			
			// Initialize iframe for embedding report
			powerbi.bootstrap(reportContainer, { type: "report" });
			
			// Request to get embed details
			$.ajax({
				type: "GET",
				url: baseUrl+"/reports/powerbicontroller",
				dataType: "json",
				success: function (embedData) {
					reportLoadConfig = {
						type: "report",
						tokenType: models.TokenType.Embed,
						accessToken: embedData.embedToken,
						
						// Use other embed report config based on the requirement. We have used the first one for demo purpose
						embedUrl: embedData.embedReports[0].embedUrl,
						/*
						// Enable this setting to remove gray shoulders from embedded report
						settings: {
							background: models.BackgroundType.Transparent
						}
						*/
					};
					
					// Use the token expiry to regenerate Embed token for seamless end user experience
					// Refer https://aka.ms/RefreshEmbedToken
					tokenExpiry = embedData["tokenExpiry"]
					
					// Embed Power BI report when Access token and Embed URL are available
					report = powerbi.embed(reportContainer, reportLoadConfig);
				
					// Triggers when a report schema is successfully loaded
					report.on("loaded", function () {
						console.log("Report load successful");
					});
				 	
					// Triggers when a report is successfully embedded in UI
					report.on("rendered", function () {
						console.log("Report render successful");
					});
				
					// Clear any other error handler event
					report.off("error");

					// Below patch of code is for handling errors that occur during embedding
					report.on("error", function (event) {
						errorMsg = event.detail;
						
						// Use errorMsg variable to log error in any destination of choice
						console.error(errorMsg);
						return;
					});
				},
				error: function (err) {
					
					// Show error container
					$(".embed-container").hide();
					var errorContainer = $(".error-container");
					errorContainer.show();
					
					// Format error message
					var errMessageHtml = "<strong> Error Details: </strong> <br/>"
					errMessageHtml += err.responseText.split("\n").join("<br/>")
					
					// Show error message on UI
					errorContainer.html(errMessageHtml);
				}
			});
		});
	</script>
</body>
</html>