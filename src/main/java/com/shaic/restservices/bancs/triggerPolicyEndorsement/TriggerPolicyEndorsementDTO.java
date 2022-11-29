package com.shaic.restservices.bancs.triggerPolicyEndorsement;

import java.util.ArrayList;

public class TriggerPolicyEndorsementDTO {
	

	  private String serviceTransactionId;
	  
	  private String businessChannel;
	  
	  private String userCode;
	  
	  private String roleCode;
	  
	  private String policyNumber;
	  
	  private String typeOfEndorsement;
	
     // private ArrayList<TriggerPolicyEndorseNomineeDetails> nomineeDetails;
      
      TriggerPolicyEndorseNomineeDetails nomineeDetails;
    
      private ArrayList<TriggerPolicyEndorseNachMandates> nachMandates;
    
      private ArrayList<TriggerPolicyEndorseJourneyDetails> journeyDetails;

      private ArrayList<TriggerPolicyEndorsePreviousInsuranceDetails> previousInsuranceDetails;

	public String getServiceTransactionId() {
		return serviceTransactionId;
	}

	public void setServiceTransactionId(String serviceTransactionId) {
		this.serviceTransactionId = serviceTransactionId;
	}

	public String getBusinessChannel() {
		return businessChannel;
	}

	public void setBusinessChannel(String businessChannel) {
		this.businessChannel = businessChannel;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getTypeOfEndorsement() {
		return typeOfEndorsement;
	}

	public void setTypeOfEndorsement(String typeOfEndorsement) {
		this.typeOfEndorsement = typeOfEndorsement;
	}
	
	

/*	public ArrayList<TriggerPolicyEndorseNomineeDetails> getNomineeDetails() {
		return nomineeDetails;
	}

	public void setNomineeDetails(
			ArrayList<TriggerPolicyEndorseNomineeDetails> nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}*/

	public TriggerPolicyEndorseNomineeDetails getNomineeDetails() {
		return nomineeDetails;
	}

	public void setNomineeDetails(TriggerPolicyEndorseNomineeDetails nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}

	public ArrayList<TriggerPolicyEndorseNachMandates> getNachMandates() {
		return nachMandates;
	}

	public void setNachMandates(
			ArrayList<TriggerPolicyEndorseNachMandates> nachMandates) {
		this.nachMandates = nachMandates;
	}

	public ArrayList<TriggerPolicyEndorseJourneyDetails> getJourneyDetails() {
		return journeyDetails;
	}

	public void setJourneyDetails(
			ArrayList<TriggerPolicyEndorseJourneyDetails> journeyDetails) {
		this.journeyDetails = journeyDetails;
	}

	public ArrayList<TriggerPolicyEndorsePreviousInsuranceDetails> getPreviousInsuranceDetails() {
		return previousInsuranceDetails;
	}

	public void setPreviousInsuranceDetails(
			ArrayList<TriggerPolicyEndorsePreviousInsuranceDetails> previousInsuranceDetails) {
		this.previousInsuranceDetails = previousInsuranceDetails;
	}


/*	private String serviceTransactionId;

    private String roleCode;

    private String[] riskDetails;

    private String policyNumber;

    private PolicyProperty policyProperty;

    private String typeOfEndorsement;

    private String userCode;

    private String businessChannel;

    public String getServiceTransactionId ()
    {
        return serviceTransactionId;
    }

    public void setServiceTransactionId (String serviceTransactionId)
    {
        this.serviceTransactionId = serviceTransactionId;
    }

    public String getRoleCode ()
    {
        return roleCode;
    }

    public void setRoleCode (String roleCode)
    {
        this.roleCode = roleCode;
    }

    public String[] getRiskDetails ()
    {
        return riskDetails;
    }

    public void setRiskDetails (String[] riskDetails)
    {
        this.riskDetails = riskDetails;
    }

    public String getPolicyNumber ()
    {
        return policyNumber;
    }

    public void setPolicyNumber (String policyNumber)
    {
        this.policyNumber = policyNumber;
    }

    public PolicyProperty getPolicyProperty ()
    {
        return policyProperty;
    }

    public void setPolicyProperty (PolicyProperty policyProperty)
    {
        this.policyProperty = policyProperty;
    }

    public String getTypeOfEndorsement ()
    {
        return typeOfEndorsement;
    }

    public void setTypeOfEndorsement (String typeOfEndorsement)
    {
        this.typeOfEndorsement = typeOfEndorsement;
    }

    public String getUserCode ()
    {
        return userCode;
    }

    public void setUserCode (String userCode)
    {
        this.userCode = userCode;
    }

    public String getBusinessChannel ()
    {
        return businessChannel;
    }

    public void setBusinessChannel (String businessChannel)
    {
        this.businessChannel = businessChannel;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [serviceTransactionId = "+serviceTransactionId+", roleCode = "+roleCode+", riskDetails = "+riskDetails+", policyNumber = "+policyNumber+", policyProperty = "+policyProperty+", typeOfEndorsement = "+typeOfEndorsement+", userCode = "+userCode+", businessChannel = "+businessChannel+"]";
    }
	*/
	

}

