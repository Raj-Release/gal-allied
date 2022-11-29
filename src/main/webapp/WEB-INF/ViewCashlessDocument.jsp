<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="com.vaadin.server.StreamResource"%>
    <%@page import="java.io.InputStream"%>
    <%@page import="java.io.FileInputStream"%>
    <%@page import = "java.io.File" %>
    <%@page import="java.io.IOException"%>
    <%@ page trimDirectiveWhitespaces="true" %>
    <%@ page import="org.apache.commons.io.FileUtils" %>
    
 <%!
	String filePatha ;
 %>
 <%
 	session.removeAttribute("mergeDocumentsUrl"); 
	session.removeAttribute("mergedfileName");
	session.setAttribute("mergeDocumentsUrl",request.getAttribute("mergeDocumentsUrl") );
	session.setAttribute("mergedfileName",request.getAttribute("mergedfileName") );
 	filePatha = (String)session.getAttribute("mergeDocumentsUrl");
 	System.out.println("filePathaaaaaaa===value----->"+filePatha);
  %>
 <%
	final String filePath = (String)session.getAttribute("mergeDocumentsUrl");
	final String fileName = (String)session.getAttribute("mergedfileName");
	if(null != filePath && !("").equalsIgnoreCase(filePath)){
		response.reset();
	    response.setContentType("application/pdf");
	    response.setHeader("Content-Type", "application/pdf");
	    File file = new File(filePath);
	    if(null != file )
	    {
	    	response.setHeader("Content-Length", String.valueOf(file.length()));
	    	/* response.addHeader("Content-Disposition", "attachment; filename=" + file.getName()); */
	    }
	    
	    byte[] pdfByteArray = FileUtils.readFileToByteArray(file);
		response.setContentType("application/pdf");
		response.getOutputStream().write(pdfByteArray);
		response.getOutputStream().flush();
		
		/*
		FileInputStream in = new FileInputStream(file);
	    ServletOutputStream outs = response.getOutputStream();
	    response.setContentLength(in.available());
	    byte[] buf = new byte[100000000];
	    int c = 0;
	    try {
	        while ((c = in.read(buf, 0, buf.length)) > 0) {
	            //System.out.println("size:"+c);
	            outs.write(buf, 0, c);
	            out.write(outs.toString());
	        }
	
	    } catch (IOException ioe) {
	        ioe.printStackTrace(System.out);
	    } finally {
	        outs.flush();
	        outs.close();
	        in.close();
	      
	    }*/
    }
 %>