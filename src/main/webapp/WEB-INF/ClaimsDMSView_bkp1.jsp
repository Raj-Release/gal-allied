<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*,java.lang.*" %>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Uploaded Document</title>
</head>
<style>
body {
    background-color: #444;
    margin: 0;
}

#wrapper {
     width: 1005px;
     margin: 0 auto;
    position: relative;
}
#leftcolumn, #rightcolumn {
    border: 1px solid white;
    min-height: 750px;
    color: white;
}
#leftcolumn {
     width: 450px;
     background-color: #111;
    min-height: 100px;
    position: fixed;
}
#rightcolumn {
     width: 1000px;
     background-color: #777;
    float: right;
}

</style>
<body>
		<div id="wrapper">
	    <div id="leftcolumn">
	        <ul class="dropdownMenu">
	                <li>Hospital Portal
	                	<ul class="droprightMenu">
							<li><a href="DocumentDetails.jsp"s.jsp" target="#rightcolumn">Preauth Query</a></li>
	                        <li><a href="#">Enhancement Query</a></li>
	                     </ul>
	                </li> 
	         </ul>
	    </div>
	    <div id="rightcolumn">
	        Right
	    </div>
	</div>
</body>
</html>