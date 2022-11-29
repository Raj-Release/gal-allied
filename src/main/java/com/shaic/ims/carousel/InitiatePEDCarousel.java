package com.shaic.ims.carousel;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.pedrequest.initiateped.SearchPEDInitiateService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyService;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class InitiatePEDCarousel extends ViewComponent  {
	
	
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private SearchPEDInitiateService searchPEDService;
	
	private TextField policyNumber;
	private TextField endrosmentNumber;
	private TextField proposerCode;
	private TextField proposerName;
	
	private TextField productName;
	private TextField issuingOfficeName;
	private TextField periodOfInsurance;
	private TextField classOfBussiness;
	
	private TextField noOfinsured;
	private TextField premiumRecepitNumber;
	private TextField premiumRecepitDate;
	private TextField noClaimBonus;
	
	private TextField grossPremium;
	private TextField serviceTax;
	private TextField stampDuty;
	private TextField totalPremium;
	
	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;
	
	@PostConstruct
	public void initView() {

	}
	
	public void init(PolicyDto policyDTO,
			String caption) {

		Panel panel = new Panel(caption);
		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildCarousel());
		BeanItem<PolicyDto> item = new BeanItem<PolicyDto>(
				policyDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		
		setAddtionalValuesforCarousel(policyDTO);
		setCompositionRoot(panel);
	}
	
	private HorizontalCarousel buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setWidth("400px");
		firstForm.setMargin(false);
		FormLayout secondForm = new FormLayout();
		secondForm.setSpacing(false);
		secondForm.setMargin(false);
		FormLayout thirdForm = new FormLayout();
		thirdForm.setSpacing(false);
		thirdForm.setMargin(false);
		FormLayout fourthForm = new FormLayout();
		fourthForm.setSpacing(false);
		fourthForm.setMargin(false);
		
		
		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("-1px");
		policyNumber.setHeight("20px");
		policyNumber.setReadOnly(true);
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		policyNumber.setNullRepresentation("");
		
		endrosmentNumber = new TextField("Endrosement No");
		//Vaadin8-setImmediate() endrosmentNumber.setImmediate(true);
		endrosmentNumber.setWidth("-1px");
		endrosmentNumber.setHeight("20px");
		endrosmentNumber.setReadOnly(true);
		endrosmentNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		endrosmentNumber.setNullRepresentation("");
		
		proposerCode = new TextField("Proposer Code");
		//Vaadin8-setImmediate() proposerCode.setImmediate(true);
		proposerCode.setWidth("-1px");
		proposerCode.setHeight("20px");
		proposerCode.setReadOnly(true);
		proposerCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		proposerCode.setNullRepresentation("");
		
		proposerName = new TextField("Proposer Name");
		//Vaadin8-setImmediate() proposerName.setImmediate(true);
		proposerName.setWidth("-1px");
		proposerName.setHeight("20px");
		proposerName.setReadOnly(true);
		proposerName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		proposerName.setNullRepresentation("");
		
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("700px");
		productName.setHeight("20px");
		productName.setReadOnly(true);
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		productName.setNullRepresentation("");
		
		issuingOfficeName = new TextField("Issuing Office Name");
		//Vaadin8-setImmediate() issuingOfficeName.setImmediate(true);
		issuingOfficeName.setWidth("-1px");
		issuingOfficeName.setHeight("20px");
		issuingOfficeName.setReadOnly(true);
		issuingOfficeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		issuingOfficeName.setNullRepresentation("");
		
		periodOfInsurance = new TextField("Period Of Insurance");
		//Vaadin8-setImmediate() periodOfInsurance.setImmediate(true);
		periodOfInsurance.setWidth("-1px");
		periodOfInsurance.setHeight("20px");
		periodOfInsurance.setReadOnly(true);
		periodOfInsurance.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		periodOfInsurance.setNullRepresentation("");
		
		classOfBussiness = new TextField("Class Of Bussiness");
		//Vaadin8-setImmediate() classOfBussiness.setImmediate(true);
		classOfBussiness.setWidth("-1px");
		classOfBussiness.setHeight("20px");
		classOfBussiness.setReadOnly(true);
		classOfBussiness.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		classOfBussiness.setNullRepresentation("");
		
		noOfinsured = new TextField("No of Insured");
		//Vaadin8-setImmediate() noOfinsured.setImmediate(true);
		noOfinsured.setWidth("-1px");
		noOfinsured.setHeight("20px");
		noOfinsured.setReadOnly(true);
		noOfinsured.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		noOfinsured.setNullRepresentation("");
		
		premiumRecepitNumber = new TextField("Dep.Prem.Recpt.No");
		//Vaadin8-setImmediate() premiumRecepitNumber.setImmediate(true);
		premiumRecepitNumber.setWidth("-1px");
		premiumRecepitNumber.setHeight("20px");
		premiumRecepitNumber.setReadOnly(true);
		premiumRecepitNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		premiumRecepitNumber.setNullRepresentation("");
		
		premiumRecepitDate = new TextField("Dep.Prem.Recpt.Dt");
		//Vaadin8-setImmediate() premiumRecepitDate.setImmediate(true);
		premiumRecepitDate.setWidth("-1px");
		premiumRecepitDate.setHeight("20px");
		premiumRecepitDate.setReadOnly(true);
		premiumRecepitDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		premiumRecepitDate.setNullRepresentation("");
		
		noClaimBonus = new TextField("No Claim Bonus");
		//Vaadin8-setImmediate() noClaimBonus.setImmediate(true);
		noClaimBonus.setWidth("-1px");
		noClaimBonus.setHeight("20px");
		noClaimBonus.setReadOnly(true);
		noClaimBonus.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		noClaimBonus.setNullRepresentation("");
		
		grossPremium = new TextField("Gross Premium");
		//Vaadin8-setImmediate() grossPremium.setImmediate(true);
		grossPremium.setWidth("-1px");
		grossPremium.setHeight("20px");
		grossPremium.setReadOnly(true);
		grossPremium.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		grossPremium.setNullRepresentation("");
		
		serviceTax = new TextField("Service Tax");
		//Vaadin8-setImmediate() serviceTax.setImmediate(true);
		serviceTax.setWidth("-1px");
		serviceTax.setHeight("20px");
		serviceTax.setReadOnly(true);
		serviceTax.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		serviceTax.setNullRepresentation("");
		
		stampDuty = new TextField("Stamp Duty");
		//Vaadin8-setImmediate() stampDuty.setImmediate(true);
		stampDuty.setWidth("-1px");
		stampDuty.setHeight("20px");
		stampDuty.setReadOnly(true);
		stampDuty.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		stampDuty.setNullRepresentation("");
		
		totalPremium = new TextField("Total Premium");
		//Vaadin8-setImmediate() totalPremium.setImmediate(true);
		totalPremium.setWidth("-1px");
		totalPremium.setHeight("20px");
		totalPremium.setReadOnly(true);
		totalPremium.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		totalPremium.setNullRepresentation("");
		
		firstForm.addComponent(policyNumber);
		firstForm.addComponent(endrosmentNumber);
		firstForm.addComponent(proposerCode);
		firstForm.addComponent(proposerName);
		
		secondForm.addComponent(productName);
		secondForm.addComponent(issuingOfficeName);
		secondForm.addComponent(periodOfInsurance);
		secondForm.addComponent(classOfBussiness);
		
		thirdForm.addComponent(noOfinsured);
		thirdForm.addComponent(premiumRecepitNumber);
		thirdForm.addComponent(premiumRecepitDate);
		thirdForm.addComponent(noClaimBonus);
		
		
		fourthForm.addComponent(grossPremium);
		fourthForm.addComponent(serviceTax);
		fourthForm.addComponent(stampDuty);
		fourthForm.addComponent(totalPremium);
		
		formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);
		
		formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
		// Fetch children lazily
		formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
		// Transition animations between the children run 500 milliseconds
		formsHorizontalLayout.setTransitionDuration(50);
		
		layout1 = new HorizontalLayout(firstForm, secondForm);
		layout1.setWidth("100%");
		layout1.setSpacing(true);
		layout2 = new HorizontalLayout(thirdForm,fourthForm);
		
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
		
		formsHorizontalLayout.setHeight("110px");
	     
		return formsHorizontalLayout;
		
		
		
		
		
	}
	
	private void setAddtionalValuesforCarousel(PolicyDto policyDTO) {
		

		policyNumber.setReadOnly(false);
		policyNumber.setValue(policyDTO.getPolicyNumber() != null ? policyDTO.getPolicyNumber() : "" );
		policyNumber.setReadOnly(true);
		
		endrosmentNumber.setReadOnly(false);
		
		StringBuffer strEndrosementNumber = new StringBuffer();
		List<PolicyEndorsementDetails> endorsementList = policyService.getEndorsementList(policyDTO.getPolicyNumber());
		if(endorsementList != null && ! endorsementList.isEmpty()){
			for (PolicyEndorsementDetails policyEndorsementDetails : endorsementList) {
				strEndrosementNumber.append(policyEndorsementDetails.getEndorsementNumber()).append(",");
				endrosmentNumber.setValue(strEndrosementNumber.substring(0, strEndrosementNumber.length()-1));
			}
		}
		endrosmentNumber.setReadOnly(true);
		
		proposerCode.setReadOnly(false);
		proposerCode.setValue(policyDTO.getProposerCode() !=null ? policyDTO.getProposerCode() :"");
		proposerCode.setReadOnly(true);
		
		proposerName.setReadOnly(false);
		proposerName.setValue(policyDTO.getProposerFirstName() !=null ?policyDTO.getProposerFirstName():"");
		proposerName.setReadOnly(true);
		
		productName.setReadOnly(false);
		productName.setValue(policyDTO.getProduct() != null ? policyDTO.getProduct().getValue() : "");
		productName.setReadOnly(true);
		
		issuingOfficeName.setReadOnly(false);
		if(policyDTO.getHomeOfficeCode() != null){
			String branchName = searchPEDService.getBranchName(policyDTO.getHomeOfficeCode());
			issuingOfficeName.setValue(branchName);
		}
		issuingOfficeName.setReadOnly(true);
		
		periodOfInsurance.setReadOnly(false);
		if(null != policyDTO.getPolicyYear()){
			if(policyDTO.getPolicyFromDate() != null && policyDTO.getPolicyToDate() != null){
				String formatDate = SHAUtils.formatDate(policyDTO.getPolicyFromDate());
				String formatDate2 = SHAUtils.formatDate(policyDTO.getPolicyToDate());
				if(formatDate != null && formatDate2 != null){
					String policyDate = formatDate +" - "+ formatDate2;
					periodOfInsurance.setValue(policyDate);
				}
			}
		}
		periodOfInsurance.setReadOnly(true);
		
		classOfBussiness.setReadOnly(false);
		MastersValue lobVAlue = masterService.getMaster(policyDTO.getLobId());
		classOfBussiness.setValue(lobVAlue.getValue());
		classOfBussiness.setReadOnly(true);
		
		List<Insured> insuredListByPolicyNumber = insuredService.getInsuredListByPolicyNumber(policyDTO.getPolicyNumber());
		if(insuredListByPolicyNumber != null && ! insuredListByPolicyNumber.isEmpty()){
			noOfinsured.setReadOnly(false);
			noOfinsured.setValue(String.valueOf(insuredListByPolicyNumber.size()));
			noOfinsured.setReadOnly(true);
		}
	
		premiumRecepitNumber.setReadOnly(false);
		premiumRecepitNumber.setValue(policyDTO.getReceiptNumber() != null ?policyDTO.getReceiptNumber() :"");
		premiumRecepitNumber.setReadOnly(true);
		
		premiumRecepitDate.setReadOnly(false);
		premiumRecepitDate.setValue(policyDTO.getReceiptDate() !=null ? SHAUtils.formatDate(policyDTO.getReceiptDate()) :"");
		premiumRecepitDate.setReadOnly(true);
		
		noClaimBonus.setReadOnly(false);
		noClaimBonus.setValue(policyDTO.getNoClaimBonus() != null ? String.valueOf(policyDTO.getNoClaimBonus()) : "");
		noClaimBonus.setReadOnly(true);
		
		grossPremium.setReadOnly(false);
		grossPremium.setValue(policyDTO.getGrossPremium() != null ?policyDTO.getGrossPremium().toString() : "" );
		grossPremium.setReadOnly(true);
		
		serviceTax.setReadOnly(false);
		serviceTax.setValue(policyDTO.getPremiumTax() != null ? String.valueOf(policyDTO.getPremiumTax()) :"");
		serviceTax.setReadOnly(true);
		
		stampDuty.setReadOnly(false);
		stampDuty.setValue(policyDTO.getStampDuty() != null? policyDTO.getStampDuty().toString() :"");
		stampDuty.setReadOnly(true);
		
		totalPremium.setReadOnly(false);
		totalPremium.setValue(policyDTO.getTotalPremium() != null? policyDTO.getTotalPremium().toString() :"");
		totalPremium.setReadOnly(true);
		
	}

}
