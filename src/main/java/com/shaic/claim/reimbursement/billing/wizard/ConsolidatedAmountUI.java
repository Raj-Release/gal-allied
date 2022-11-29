package com.shaic.claim.reimbursement.billing.wizard;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.coinsurance.view.CoInsuranceTableDTO;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billing.dto.ConsolidatedAmountDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ConsolidatedAmountUI extends ViewComponent {
	
	private static final long serialVersionUID = -4103793305223450982L;

	private BeanFieldGroup<ConsolidatedAmountDetailsDTO> binder;
	
	private PreauthDTO bean;
	
	private TextField hospPayableAmt;
	
	private TextField preHospPayableAmt;
	
	private TextField postHospPayableAmt;
	
	private TextField addOnBenefitsPayableAmt;
	
	private TextField lumpsumAmount;
	
	private TextField totalAmt;
	
	private TextField txtOtherBenefits;
	
	private TextField balancePremium;
	
	private TextField amountPayable;
	
	private TextField leader;
	private TextField follower;
	
	
	private Boolean isFinancial = false;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<ConsolidatedAmountDetailsDTO>(ConsolidatedAmountDetailsDTO.class);
		this.binder.setItemDataSource(bean.getConsolidatedAmtDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void initView(PreauthDTO bean, Boolean isFinancial) 
	{	
		
		this.bean = bean;
		this.isFinancial = isFinancial;
		hospPayableAmt = new TextField("Hospitalization Payable Amount");
		hospPayableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		hospPayableAmt.setValue(bean.getConsolidatedAmtDTO().getHospPayableAmt() != null ? bean.getConsolidatedAmtDTO().getHospPayableAmt().toString() : "0");
		hospPayableAmt.setReadOnly(true);
		
		preHospPayableAmt = new TextField("Pre-Hospitalization Payable Amount");
		preHospPayableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		preHospPayableAmt.setValue(bean.getConsolidatedAmtDTO().getPreHospPayableAmt() != null ? bean.getConsolidatedAmtDTO().getPreHospPayableAmt().toString() : "0");
		preHospPayableAmt.setReadOnly(true);
		
		postHospPayableAmt = new TextField("Post-Hospitalizaton Payable Amount");
		postHospPayableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		postHospPayableAmt.setValue(bean.getConsolidatedAmtDTO().getPostHospPayableAmt() != null ? bean.getConsolidatedAmtDTO().getPostHospPayableAmt().toString() : "0");
		postHospPayableAmt.setReadOnly(true);
		
		addOnBenefitsPayableAmt = new TextField("Add on Benefits (Hospital Cash / Patient Care)");
		addOnBenefitsPayableAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		Double adOnBenefitAmt = (bean.getPatientCareAmt() + bean.getHospitalCashAmt());
		addOnBenefitsPayableAmt.setValue(adOnBenefitAmt != null ? String.valueOf((adOnBenefitAmt.longValue()))  : "0");
		addOnBenefitsPayableAmt.setReadOnly(true);
		if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
			lumpsumAmount = new TextField("Lumpsum Amount");
			lumpsumAmount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			bean.getConsolidatedAmtDTO().setLumpusmPayableAmt(bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() : bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue());
			lumpsumAmount.setValue(String.valueOf(bean.getIsReconsiderationRequest() ? bean.getHospitalizationCalculationDTO().getBalanceToBePaid() : bean.getPreauthMedicalDecisionDetails().getInitialTotalApprovedAmt().intValue()));
			lumpsumAmount.setReadOnly(true);
		}
		
		txtOtherBenefits = new TextField("Other Benefits");
		txtOtherBenefits.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtOtherBenefits.setValue(bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null ?bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt().toString():"0");
		txtOtherBenefits.setReadOnly(true);
		bean.getConsolidatedAmtDTO().setOtherBenefitAmt(bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null ?bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt().intValue():0);
		txtOtherBenefits.setNullRepresentation("");
		
		

		totalAmt = new TextField("Total Claim Payable Amount");
		totalAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		Integer totalAmount  = SHAUtils.getIntegerFromStringWithComma(hospPayableAmt.getValue()) + SHAUtils.getIntegerFromStringWithComma(preHospPayableAmt.getValue()) + SHAUtils.getIntegerFromStringWithComma(postHospPayableAmt.getValue()) +SHAUtils.getIntegerFromStringWithComma(addOnBenefitsPayableAmt.getValue());
		if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
			totalAmount += SHAUtils.getIntegerFromStringWithComma(lumpsumAmount.getValue());
		}
		
		if(bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null){
			totalAmount += bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt().intValue();
		}
		
		if(bean.getPreauthDataExtractionDetails().getBufferTypeValue() != null &&
				!bean.getPreauthDataExtractionDetails().getBufferTypeValue().isEmpty() && bean.getPreauthDataExtractionDetails().getBufferTypeValue().equalsIgnoreCase(SHAConstants.NAC_BUFFER_TABID)){
			totalAmount += bean.getPreauthDataExtractionDetails().getNacBufferUtilizedAmount();
		}
		totalAmt.setValue(totalAmount != null ? String.valueOf((totalAmount))  : "0");
		totalAmt.setReadOnly(true);
		
		bean.setConsolidatedFinalAppAmnt(totalAmount != null ? String.valueOf((totalAmount))  : "0");;

		//CR2019199
		if(bean.getIsCoInsurance()){

			leader = new TextField("Leader Share");
			String leaderValue = "";
			String ShareValue ="";
			leader.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS); 
			for(CoInsuranceTableDTO coInsurance : bean.getCoInsuranceList()){
				
				if(coInsurance.getShareType().equalsIgnoreCase("LEADER SHARE")){
					Double leaderShareValue = (Double) (Integer.parseInt(totalAmt.getValue())*(coInsurance.getSharePercentage()/100));
					leaderValue = leaderShareValue.toString();
				}
				
				if(coInsurance.getShareType().equalsIgnoreCase("FOLLOWER SHARE")){
					Double followerShareValue = (Double) (Integer.parseInt(totalAmt.getValue())*(coInsurance.getSharePercentage()/100));
					ShareValue = followerShareValue.toString();
				}
			}

			leader.setValue(leaderValue);
			leader.setReadOnly(true);
			
			follower = new TextField("Follower Share");
			follower.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			follower.setValue(ShareValue);
			follower.setReadOnly(true);
		}
	

		
		FormLayout formLayout = null;
		if((ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))) {
			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				this.bean.getConsolidatedAmtDTO().setPremiumAmt(uniqueInstallmentAmount);
				balancePremium = new TextField("Less : Premium Payable - Premium Amount");
				balancePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				balancePremium.setValue(uniqueInstallmentAmount != null ? String.valueOf((uniqueInstallmentAmount.intValue()))  : "0");
				balancePremium.setReadOnly(true);
				
				amountPayable = new TextField("Amount payable to Insured (After Premium Deduction)");
				amountPayable.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
				Integer totalPremiumAmount = 0;
				
				if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && (bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)) && (bean.getHospitalizaionFlag())) {
					totalPremiumAmount  = SHAUtils.getIntegerFromStringWithComma(totalAmt.getValue()) - SHAUtils.getIntegerFromStringWithComma("0");
				} else {
					totalPremiumAmount  = SHAUtils.getIntegerFromStringWithComma(totalAmt.getValue()) - SHAUtils.getIntegerFromStringWithComma(balancePremium.getValue());
				}
				
				this.bean.getConsolidatedAmtDTO().setAmountPayableAfterPremium(totalPremiumAmount < 0 ? 0 : totalPremiumAmount);
				amountPayable.setValue((totalPremiumAmount != null && totalPremiumAmount > 0)? String.valueOf((totalPremiumAmount))  : "0");
				amountPayable.setReadOnly(true);
				if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, lumpsumAmount, totalAmt, balancePremium, amountPayable);
				} else {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, totalAmt, balancePremium, amountPayable);
				}
				
			}  else {
				if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, lumpsumAmount, totalAmt);
				} else {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, totalAmt);
				}
				
			}
			
		}  //cr2019184
		else if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG) && isFinancial){

			if(bean.getPreauthDataExtractionDetails().getDocAckknowledgement() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId() != null && bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_INSURED)) {
				Integer uniqueInstallmentAmount = PremiaService.getInstance().getPolicyInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
				this.bean.getConsolidatedAmtDTO().setPremiumAmt(uniqueInstallmentAmount);
				balancePremium = new TextField("Less : Premium Payable - Premium Amount");
				balancePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				balancePremium.setValue(uniqueInstallmentAmount != null ? String.valueOf((uniqueInstallmentAmount.intValue()))  : "0");
				balancePremium.setReadOnly(true);
				
				amountPayable = new TextField("Amount payable to Insured (After Premium Deduction)");
				amountPayable.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
				Integer totalPremiumAmount = 0;
				
				if(bean.getClaimDTO().getClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY) && (bean.getHospitalizaionFlag())) {
					totalPremiumAmount  = SHAUtils.getIntegerFromStringWithComma(totalAmt.getValue()) - SHAUtils.getIntegerFromStringWithComma("0");
				} else {
					totalPremiumAmount  = SHAUtils.getIntegerFromStringWithComma(totalAmt.getValue()) - SHAUtils.getIntegerFromStringWithComma(balancePremium.getValue());
				}
				
				this.bean.getConsolidatedAmtDTO().setAmountPayableAfterPremium(totalPremiumAmount < 0 ? 0 : totalPremiumAmount);
				amountPayable.setValue((totalPremiumAmount != null && totalPremiumAmount > 0)? String.valueOf((totalPremiumAmount))  : "0");
				amountPayable.setReadOnly(true);
				if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, lumpsumAmount, totalAmt, balancePremium, amountPayable);
				} else {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, totalAmt, balancePremium, amountPayable);
				}
				
			}  else {
				if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, lumpsumAmount, totalAmt);
				} else {
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, totalAmt);
				}
				
			}
			
		
		}
		/**
		 * Added for ticket 5263.
		 * */
		else {
			if(bean.getLumpSumAmountFlag() != null && bean.getLumpSumAmountFlag()) {
				formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, lumpsumAmount, totalAmt);
			} else {
				
				if(ReferenceTable.getFHORevisedKeys().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						|| ReferenceTable.JET_PRIVILEGE_PRODUCT.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) ||
						ReferenceTable.STAR_SPECIAL_CARE_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())
						|| /*ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())*/
							(bean.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& ((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							&& ("G").equalsIgnoreCase(bean.getNewIntimationDTO().getInsuredPatient().getPolicyPlan()))
						|| (bean.getNewIntimationDTO().getPolicy().getProduct() != null 
						&& (SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(bean.getNewIntimationDTO().getPolicy().getProduct().getCode())))){
					
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt,txtOtherBenefits, totalAmt);
				}else{
					formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt,totalAmt);
				}
				
			}
			//formLayout = new FormLayout(hospPayableAmt, preHospPayableAmt, postHospPayableAmt, addOnBenefitsPayableAmt, totalAmt);
		}
		
		if(bean.getIsCoInsurance()){
			formLayout.addComponent(leader);
			formLayout.addComponent(follower);
		}
		formLayout.setSpacing(false);
		
		setCompositionRoot(formLayout);
//		isValid();
	}
	
	
	
	public void isValid(){
		try {
			binder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getProvisionAmount() {
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && isFinancial) {
			return totalAmt != null ? totalAmt.getValue() : totalAmt.getValue();
		}
		/*if(bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt() != null && totalAmt.getValue() != null){
			Integer otherBenefiit = bean.getPreauthDataExtractionDetails().getTotalOtherBenefitsApprovedAmt().intValue();
			Integer totalAmt = SHAUtils.getIntegerFromStringWithComma(this.totalAmt.getValue());
			totalAmt = totalAmt - otherBenefiit;
			this.totalAmt.setValue(totalAmt.toString());
		}*/
		return totalAmt.getValue();
	}
	
	public String getTotalConsolidatedAmt() {
		return totalAmt.getValue();
	}
	
	public String getUniqueDeductedAmount() {
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			return amountPayable != null ? amountPayable.getValue() : totalAmt.getValue();
		}//cr2019184
		else if(bean.getPolicyInstalmentFlag() != null && bean.getPolicyInstalmentFlag().equals(SHAConstants.YES_FLAG) && isFinancial){
			return amountPayable != null ? amountPayable.getValue() : totalAmt.getValue();
		}
		return totalAmt.getValue();
	}
	
	
	public String getUniquePayableAmount() {
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()) && isFinancial) {
			return totalAmt != null ? totalAmt.getValue() : totalAmt.getValue();
		}
		return totalAmt.getValue();
	}
	
	public void setOtherBenefitAmount(String otherBenefitsAmt){
		txtOtherBenefits.setReadOnly(false);
		txtOtherBenefits.setValue(otherBenefitsAmt);
		txtOtherBenefits.setReadOnly(true);
		Integer integerFromStringWithComma = SHAUtils.getIntegerFromStringWithComma(totalAmt.getValue());
		Integer otherBenefiit = SHAUtils.getIntegerFromStringWithComma(otherBenefitsAmt);
		if(integerFromStringWithComma != null && otherBenefitsAmt != null){
			Integer totalAmount = otherBenefiit + integerFromStringWithComma;
			totalAmt.setReadOnly(false);
			totalAmt.setValue(totalAmount.toString());
			totalAmt.setReadOnly(true);
		}
		
	}
	
	public String getHospitalizationPayableAmt() {
		return hospPayableAmt.getValue();
	}
	
	
}
