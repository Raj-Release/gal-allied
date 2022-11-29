package com.shaic.claim.policy.search.ui;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DmsUrlResponse {
	
	
	
	@JsonProperty("result")
	private DmsUrlResponseValue result;

	
    public DmsUrlResponseValue getResult() {
		return result;
	}




	public void setResult(DmsUrlResponseValue result) {
		this.result = result;
	}




	@Override
    public String toString()
    {
        return "ClassPojo [JsonElement = "+result+"]";
    }	


}
