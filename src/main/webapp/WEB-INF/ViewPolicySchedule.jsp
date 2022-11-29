<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Policy Schedule</title>
</head>
<body>

<%!
String fileViewURL ;
 %>
<% 
 	
 	 fileViewURL = (String)request.getAttribute("policyscheduleurl");
 	System.out.println("the url"+fileViewURL);
	  
%>

<script>
function viewProposalPopup()
{
	window.open('<%=fileViewURL%>',"_self",'width:650, height: 500');
}

</script>

<br>
	
	   <iframe id ="viewFrame" src="<%=fileViewURL%>" width="" height="" style="position:relative;" onload="viewProposalPopup();"></iframe>
	  <!-- <iframe id ="viewFrame" src= "http://192.168.1.237:7575/GalaxyDMS//Galaxy_View.aspx?Process_type=POLICY&args=POLICYNO=P/700002/01/2016/024641" width="500%" height="500%" style="position:relative;"></iframe> -->
	
</br>

</body>
</html>