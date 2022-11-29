package com.shaic.arch;

public class UploadResponse {

	 private Result result;

	    public Result getResult ()
	    {
	        return result;
	    }

	    public void setResult (Result result)
	    {
	        this.result = result;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [result = "+result+"]";
	    }
}
