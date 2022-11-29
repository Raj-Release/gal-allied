package com.shaic.restservices.bancs.claimprovision.policydetail;

public class PolicyDetails {
	
	private String policyNumber;
	private String quotationNumber;
	private String productCode;
	private String productName;
	private String policyInceptionDate;
	private String policyExpiryDate;
	private String firstInceptionDateOfPolicy;
	private String policyDurationUnit;
	private String policyDuration;
	private String paymentFrequency;
	private String typeOfBusiness;
	private String typeOfProposal;
	private String modeOfBusiness;
	private String premiumCalculationType;
	private String policyRenewalApplicable;
	private String modeOfRenewal;
	private String proposalFormNo;
	private String proposalFormDate;
	private String policyHolderAlsoMember;
	private Integer onlineDiscountPercentage;
	private String planType;
	private Integer floaterSumInsured;
	private String premiumRatingOption;
	private String familySize;
	private Integer numberOfInsured;
	private String zone;
	private Integer bonus;
	private Integer definedLimit; // chk in original response
	private String tvcWaive;
	private String receiptAllocationWaive;
	private String outwardPortabilityApplied;
	private Integer commissionPercentage;
	private String commissionPayable;
	private String commissionToBeWithheld;
	private String nachMandate;
	private String nachMandateSINumber;
	private String proposalSubStatus;
	private String proposalMERStatus;
	private String policyStatus;
	private String businessChannel;
	private String selfAssistedPurchase;
	private String onlineDiscountApplicable;
	private PolicyPremiumDetails polPremiumDetails;
	private ProposerDetails proposerDetails; //To Be Checked
	private InsuredDetails insuredDetails;
	private PolicyRelation policyRelation;
	private PolicyPaymentInformation policyPaymentInformation;
	private EndorsementDetails endorsementDetails;
	private NomineeDetails nomineeDetails;
	private NACHMandates nachMandates;
	private PreviousPolicyDetails previousPolicyDetails;
	private WorkItemDetails workItemDetails;
	private TVCDetails tvcDetails;
	private DispatchDetails dispatchDetails;
	private DocumentDetails documentDetails;
	private String officeCode;
	private OfficeAddressDetails officeAddressDetails; // AddressDetails contains same fields verify...
	private String renewalFlag;
	private CoverSIDetails coverSIDetails;
	
}
