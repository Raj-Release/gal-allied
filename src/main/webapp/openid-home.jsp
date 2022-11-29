<%@page import="com.shaic.ims.bpm.claim.BPMClientContext"%>
<%@page import="org.keycloak.KeycloakPrincipal"%>
<%@page import="org.keycloak.KeycloakSecurityContext"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>IMS</title>
</head>
<body>
<%
	if(request.getAttribute(KeycloakSecurityContext.class.getName()) != null) {
		KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
		if (keycloakSecurityContext != null) {
			KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal) request.getUserPrincipal();
			String username = keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername();

			session.setAttribute(BPMClientContext.KEYCLOAK_PRINCIPAL, keycloakPrincipal);
			session.setAttribute(BPMClientContext.USERID, username);
            if(response != null){
            	response.sendRedirect("./claims");
            }
		}
	} else {
		session.setAttribute(KeycloakSecurityContext.class.getName(), null);
%>
        <script type="text/javascript">
            location.href = "index.html";
        </script>
<%
        return;
    }
%>
</body>
</html>