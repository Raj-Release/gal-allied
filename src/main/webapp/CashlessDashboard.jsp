<!doctype html>
<%@page contentType="text/html" import="java.util.*" %>
<%@ page import="com.shaic.ims.bpm.claim.BPMClientContext" %>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Automatic Switch Tabs Demo</title>
<link href="https://www.jqueryscript.net/css/jquerysctipttop.css" rel="stylesheet" type="text/css">
<link href='http://fonts.googleapis.com/css?family=Raleway' rel='stylesheet' type='text/css'>
<script src="https://code.jquery.com/jquery-1.12.4.min.js" integrity="sha384-nvAa0+6Qg9clwYCGGPpDQLVpLNn0fRaROjHqs13t4Ggj3Ez50XnGQqc/r8MhnRDZ" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<!-- <script type="text/javascript" src="js/jquery.ttpanel.min.js"></script> -->
<script type="text/javascript" src="js/jquery.ttpanel.js"></script>
<script type="text/javascript">
 $(function () {
 $("#tabs").tabs();//timerTabPanel({timeInterval:10000});
}); 
</script>
<style type="text/css">
html,* {font-family: 'Raleway'; }
body { min-height: 100vh; background-color: #fafafa; }
.container { margin: 150px ; max-width: 1600px; max-height:1800px; }
.button {
  border-radius: 4px;
  background-color: #0FA0FF;
  border: none;
  color: #FFFFFF;
  text-align: center;
  font-size: 15px;
  padding: 5px;
  width: 100px;
  transition: all 0.5s;
  cursor: pointer;
  margin: 5px;
  float: right;
  display: block;
}
.button span {
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
}
.button span:after {
  content: '\00bb';
  position: absolute;
  opacity: 0;
  top: 0;
  right: -20px;
  transition: 0.5s;
}
.button:hover span {
  padding-right: 25px;
}
.button:hover span:after {
  opacity: 1;
  right: 0;
}
.containers-fluid {
  padding: 20px 50px;
  background-color: #000000;
  color: white;
}
.clearfix{
 clear: both;
}
.hide{
  display:none;  
}
#tabs ul li:hover {
	background: #fff;
}
#tabs div {
	height: 1700px;
	width: 1500px;
	padding: 10px;
}
</style>
</head>
<body>
<div id="jquery-script-menu">
<div class="jquery-script-center">
<div class="jquery-script-clear"></div>
</div>
</div>
  <div class="container">
<h1></h1>
<div id="tabs">
<ul>
<li><a href="#tab1">Overall Summary</a></li>
<li><a href="#tab2">Transaction Breakup</a></li>
<!-- <li><a href="#tab3">Title 3</a></li>
<li><a href="#tab4">Title 4</a></li>
<li><a href="#tab5">Title 5</a></li> -->
<!-- <button class="button Pin float-right" id ="pinbutton">Pin</button></li> -->
</ul>
<div id="tab1">
<% BPMClientContext  bpmObj = new BPMClientContext();%>
<iframe
    src="<%= bpmObj.getSummaryUrl()%>"
    frameborder="0"
    width="1450"
    height="1600"
    allowtransparency
></iframe>
</div>
<div id="tab2">
<iframe
    src="<%= bpmObj.getTransactionUrl()%>"
    frameborder="0"
    width="1450"
    height="1600"
    allowtransparency
></iframe>
</div>
<!-- <div id="tab3">
<p>
tab3 contents tab3 contents tab3 contents tab3 contents tab3 contents
</p>
</div>
<div id="tab4">
<p>
tab4 contents tab4 contents tab4 contents tab4 contents tab4 contents
</p>
</div>
<div id="tab5">
<p>
tab5 contents tab5 contents tab5 contents tab5 contents tab5 contents
</p>
</div> -->
</div>
</div>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-36251023-1']);
  _gaq.push(['_setDomainName', 'jqueryscript.net']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
</body>
</html>