package com.shaic.main;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentDetailsApi {
	

	
	private DocumentDetailsValue[] JsonElement;

    public DocumentDetailsValue[] getJsonElement ()
    {
        return JsonElement;
    }

    public void setJsonElement (DocumentDetailsValue[] JsonElement)
    {
        this.JsonElement = JsonElement;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [JsonElement = "+JsonElement+"]";
    }	

	

}
