package com.shaic.restservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
//import org.jboss.resteasy.logging.Logger;

import com.shaic.arch.SHAFileUtils;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "document-url")
@Path("/document-url")
public class DocumentService{
    @GET
    @Path("/docToken/{id}")
    public String geFileUrl(@PathParam("id") Long id) {
    	final String imageUrl = SHAFileUtils.viewFileByToken(String.valueOf(id));
        return imageUrl;
    }
    
   /* @POST
    @Path("/users-registration")
    @Consumes("application/json")
    public String postUser(TestServicePojo test) {
        return "Sivakumar";
    }*/
}