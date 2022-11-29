package com.shaic.restservices.bancs.triggerPolicyEndorsement;

public class TriggerPolicyEndorsementResponse {
    private String policyPremium;

    private String errorDescription;

    private String stampDuty;

    private String proposalNumber;

    private String errorCode;

    private String gst;

    private String totalPremium;

    private String status;

    public String getPolicyPremium ()
    {
        return policyPremium;
    }

    public void setPolicyPremium (String policyPremium)
    {
        this.policyPremium = policyPremium;
    }

    public String getErrorDescription ()
    {
        return errorDescription;
    }

    public void setErrorDescription (String errorDescription)
    {
        this.errorDescription = errorDescription;
    }

    public String getStampDuty ()
    {
        return stampDuty;
    }

    public void setStampDuty (String stampDuty)
    {
        this.stampDuty = stampDuty;
    }

    public String getProposalNumber ()
    {
        return proposalNumber;
    }

    public void setProposalNumber (String proposalNumber)
    {
        this.proposalNumber = proposalNumber;
    }

    public String getErrorCode ()
    {
        return errorCode;
    }

    public void setErrorCode (String errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getGst ()
    {
        return gst;
    }

    public void setGst (String gst)
    {
        this.gst = gst;
    }

    public String getTotalPremium ()
    {
        return totalPremium;
    }

    public void setTotalPremium (String totalPremium)
    {
        this.totalPremium = totalPremium;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

}
