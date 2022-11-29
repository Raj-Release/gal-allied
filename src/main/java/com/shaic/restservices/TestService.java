package com.shaic.restservices;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
//import org.jboss.resteasy.logging.Logger;

import com.shaic.arch.SHAFileUtils;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "user-management")
@Path("/user-management")
public class TestService{
    @GET
    @Path("/users/{id}")
    public TestServicePojo getUserById(@PathParam("id") Integer id) {
        
        TestServicePojo user = new TestServicePojo();
        user.setFirstName("Sivakumar");
        user.setLastName("Krishnasamy");
        return user;
    }
    
    @POST
    @Path("/users-registration")
    @Consumes("application/json")
    public String postUser(TestServicePojo test) {
        return "Sivakumar";
    }
    
    
    @POST
 	@Path("/documentToken/{documentToken}/query_id/{q_id}")
    @Consumes("application/json")
    public String getDocumentURL(@PathParam("documentToken") String documentToken, @PathParam("q_id") String qId) {
    	System.out.println("documentToken :"+ documentToken);
    	System.out.println("qId :"+ qId);
    	String imageUrl = SHAFileUtils.viewFileByToken(String.valueOf(documentToken));
    	return imageUrl;
    }
}