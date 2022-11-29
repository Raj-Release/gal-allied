
<%@page import="org.apache.http.HttpRequest"%>
<%@page import="com.shaic.ims.bpm.claim.BPMClientContext"%>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Doctor Knowledge Bot</title>
    <meta http-equiv="Content-Type" content="no-cache"  charset=ISO-8859-1">
</head>

<script type="text/javascript" src= "<%=BPMClientContext.BOT_URL%>" jwt="<%=request.getAttribute("jwt").toString() %>"> </script>

<body>
 
       

</body>
</html>