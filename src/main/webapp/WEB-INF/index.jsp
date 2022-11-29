<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title> View Claim Status   </title>
		<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    	<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
	</head>	
	<body style="background-color: #0067B4;">	
	<div align="right" style="padding-top: 5cm;height: 70%;background-color: #F5F5F5 ">	
		<form action="./viewclaimstatus" method="post" style="padding-right: 20%;">			
	<!-- 		 Please enter a color <br> -->
	
	<table style="border-width:5mm ;border-color:#C3F2FC; border-style:solid; ;background-color: #81E1FA;height: 5cm;" >
	<tr ><td colspan="2"><label style="color: red;">${error}</label></td></tr>
	<tr><td><label style="color: white;">Intimation Number</label></td><td><input type="text" name="intimationNumber" id="intimationNumber"><br/></td></tr>
	<tr><td><label style="color: white;">ID Card Number</label></td><td><input type="text" name="idCardNumber" id="idCardNumber"></td></tr>
	<tr><td><input type="submit" value="Submit" style="background-color: yellow;"></td><td><input type="button" id="btnCancel" value="Clear" style="background-color: gray;"></td></tr>
			</table>				
		</form>		
		</div>
	</body>	
	<script type="text/javascript">
		$(document).ready(function(){
			$('#btnCancel').click(function() {
    			document.getElementById('intimationNumber').value = '';
    			document.getElementById('idCardNumber').value = '';
    			return false;
			});
		});
		
		function formAutoSubmit () {
			var btn= document.getElementById("btnCancel");
			btn.form.submit();
}
	</script>
</html>