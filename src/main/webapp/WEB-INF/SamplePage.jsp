<%@page import="org.apache.http.HttpRequest"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
<%@page import="org.apache.http.HttpRequest"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
   <head>
      <meta charset="utf-8" />
      <title>Create Intimation</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />	 
	  <link rel="stylesheet" href="css/bootstrap.min.css">
      <link rel="stylesheet" href="css/font-awesome.css" >
      <link rel="stylesheet" href="css/bootstrap-theme.min.css">
      <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
      <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
      <script type="text/javascript" src="https://code.jquery.com/jquery-2.0.2.js"></script>
      <script type="text/javascript" src="https://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
      <link rel="stylesheet" type="text/css" href="https://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
	  <link rel="stylesheet" type="text/css" href="css/claims.css"> 
   </head>  
   <body>    
   
             <!-- <vaadin:ui url="/createIntimation" widgetset="com.shaic.WidgetSet.imsWidgetset" theme="mytheme"/>  -->
              
               <iframe src="http://192.168.2.29:8080/ims/createIntimation" style="width:100%; height :600px"> </iframe> 
              
	 <%--   <%
	   			if(response != null){
	            	response.sendRedirect("./createIntimation");
	            }
	   %> --%> 
   </body>
   <script type="text/javascript">
		$(document).ready(function() {
			jQuery(document).on("click", '#intimation_link', function() {	
				$('.intimation-div').removeClass('hidden');
			});
		});
	</script>
 </html>