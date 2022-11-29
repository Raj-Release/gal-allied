package com.shaic.claim.registration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.ViewIntimationStatus;
import com.shaic.claim.intimation.create.dto.DtoConverter;
import com.shaic.claim.policy.search.ui.RenewedPolicyDetails;
import com.shaic.claim.preauth.view.ViewPreviousClaimsTable;
import com.shaic.claim.preauth.view.ViewRenewedPolicyPreviousClaims;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.PEDValidationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.preauth.ViewTmpDiagnosis;
import com.shaic.domain.preauth.ViewTmpPreauth;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PreviousClaimMapper;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;


@ViewScoped
public class ViewPreviousClaimWindowOpen extends ViewComponent {

	private static final String GROUP = "Group";
	private static final String FRESH = "Fresh";
	private static final String RISK_WISE = "Risk Wise";
	private static final String POLICY_WISE = "Policy Wise";
	private static final String INSURED_WISE = "Insured Wise";
	private static final String RENEWED_POLICIES = "Renewed Policies";

	@EJB
	private PEDValidationService pedValidationService;
	
	@EJB
	private PreauthService preauthService;

	@EJB
	private ClaimService claimService;
	@EJB
	private PolicyService policyService;
	@EJB
	private IntimationService intimationService;
	@EJB
	private InsuredService insuredService;
	@EJB
	private MasterService masterService;
	
	@EJB
	private PremiaPullService premiaPullService;

	@EJB
	private AcknowledgementDocumentsReceivedService acknowledgmentDocumentService;
	
	private VerticalLayout claimlayout;
	
	private VerticalLayout renwelDetailsLayout;
	
	private ComboBox cmbPolicyYear;
	
	private Long dummyYear = 2l;
	
	private Boolean isJioPolicy = Boolean.FALSE;
	

	@Inject
	private ViewPreviousClaimsTable preauthPreviousClaimsTable;
	
	private ViewRenewedPolicyPreviousClaims renewedPolicyClaimDetailsTable;

	private static final long serialVersionUID = -2905296721396984772L;
	
	public void setPreviousClaimTable(ViewPreviousClaimsTable previousClaimTable){
		this.preauthPreviousClaimsTable = previousClaimTable;
	}
	
	public void setRenewedPolicyClaimDetailsTable(
			ViewRenewedPolicyPreviousClaims renewedPolicyClaimDetailsTable) {
		this.renewedPolicyClaimDetailsTable = renewedPolicyClaimDetailsTable;
	}



	public void showValues(String intimationNumber) {
				
		claimlayout = getPrivousClaimsLayout(intimationNumber);
		claimlayout.setMargin(true);
		this.setCompositionRoot(claimlayout);		

		final Intimation intimation = intimationService.getIntimationByNo(intimationNumber);
		isJioPolicy = intimationService.getJioPolicyDetails(intimation.getPolicy().getPolicyNumber());
		if(!isJioPolicy && !(intimation != null && intimation.getPolicy() != null && intimation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()))){
			getClaimByPolicyWise(intimation);
		}
		else
		{
			getClaimByInsuredWise(intimation);
		}
	}
	
	public void showValuesForOP(String intimationNumber,Long policyKey){
		final OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNumber);
//		Policy policyDtls = policyService.getPolicyByKey(intimation.getPolicy().getKey());
		if(intimationNumber != null){
			claimlayout = getPrivousClaimsLayoutForOP(intimation.getPolicy().getKey(),intimation.getIntimationId());
		} else {
			claimlayout = getPrivousClaimsLayoutForOP(policyKey,null);
		}
		claimlayout.setMargin(true);	
		this.setCompositionRoot(claimlayout);
		if(intimationNumber != null){
			getClaimByPolicyWiseForOP(intimation.getPolicy().getKey(),intimation.getIntimationId());
		}else {
			getClaimByPolicyWiseForOP(policyKey,null);
		}
		
	}

	@PostConstruct
	public void initView() {
		setCaption("Previous Claim Details");
		this.setHeight("712px");
		this.setWidth("1300px");
//		setModal(true);
//		setClosable(true);
//		setResizable(true);

	}

	public VerticalLayout getPrivousClaimsLayout(String intimationNumber) {
		
		claimlayout = new VerticalLayout();
		claimlayout.setMargin(true);
		
		
		Label policyLabel = new Label("Policy Year");
		policyLabel.addStyleName("");
		cmbPolicyYear = new ComboBox("Policy Year");
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		
		cmbPolicyYear.setContainerDataSource(policyYearValues);
		cmbPolicyYear.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPolicyYear.setItemCaptionPropertyId("value");
		
		final ViewTmpIntimation intimation = intimationService
				.searchbyIntimationNoFromViewIntimation(intimationNumber);
		
		addListener(intimation);
		
		HorizontalLayout optionLayout = buildViewType(intimationNumber);
		optionLayout.setWidth("100%");
		optionLayout.setSpacing(true);
		optionLayout.setMargin(true);
		
		claimlayout.addComponent(optionLayout);
//		claimlayout.addComponent(policyLabel);
		claimlayout.addComponent(cmbPolicyYear);
		claimlayout.setComponentAlignment(cmbPolicyYear, Alignment.MIDDLE_LEFT);	
		 Boolean isJioPolicy = intimationService.getJioPolicyDetails(intimation.getPolicyNumber());
		 Boolean isGmc = intimationService.isGMCPolicyByPolicyNubember(intimation.getPolicyNumber());
		if(isJioPolicy || (isGmc)){
			claimlayout.removeComponent(cmbPolicyYear);
		}
		
		return claimlayout;
	}
	
	public BeanItemContainer<SelectValue> getPolicyYearValues(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		SelectValue selected = new SelectValue();
		selected.setId(1l);
		selected.setValue("All");
		selectValueList.add(selected);
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year;i>=year-6;i--){
			SelectValue selectValue = new SelectValue();
			Long j = i-1;
			selectValue.setId(j);
			selectValue.setValue(""+j.intValue()+" - "+i.intValue());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}
	
	public VerticalLayout getPrivousClaimsLayoutForOP(Long policyKey,String intimationNo) {
		HorizontalLayout optionLayout = buildViewTypeForOp(policyKey,intimationNo);
		optionLayout.setWidth("100%");
		optionLayout.setSpacing(true);
		optionLayout.setMargin(true);
		claimlayout = new VerticalLayout();
		claimlayout.setMargin(false);
		claimlayout.addComponent(optionLayout);
		return claimlayout;
		
	}
	

	public HorizontalLayout buildViewType(String intimationNo) {
//		final ViewTmpIntimation intimation = intimationService
//				.searchbyIntimationNoFromViewIntimation(intimationNo);
		final Intimation intimation = intimationService.getIntimationByNo(intimationNo);
		String policyNumber = intimation.getPolicyNumber();
		//Policy policy = policyService.getPolicy(policyNumber);
		final OptionGroup viewType = getOptionGroup(null);		
		Collection<OptionGroup> itemIds = (Collection<OptionGroup>) viewType.getContainerDataSource().getItemIds();
		 Boolean isJioPolicy = intimationService.getJioPolicyDetails(intimation.getPolicy().getPolicyNumber());
		if(itemIds != null && !itemIds.isEmpty()) {
			
			if((null != isJioPolicy && isJioPolicy) || (intimation != null && intimation.getPolicy() != null && intimation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey()))){	
					
					viewType.removeItem(itemIds.toArray()[0]);
			}
				
			    	viewType.setValue(itemIds.toArray()[0]);
		}
		
		viewType.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						getClaimByPolicyWise(intimation);
						claimlayout.removeComponent(cmbPolicyYear);
						claimlayout.addComponent(cmbPolicyYear,1);
					} else if (viewType.getValue().equals(INSURED_WISE)) {
						getClaimByInsuredWise(intimation);
						claimlayout.removeComponent(cmbPolicyYear);
						
					} else if (viewType.getValue().equals(RISK_WISE)) {
						getClaimByRiskWise(intimation);
						claimlayout.removeComponent(cmbPolicyYear);
					}
				}
			
			}

		});
		NativeButton viewBtn = getViewButton(intimation, viewType);
		HorizontalLayout optionLayout = new HorizontalLayout();
		optionLayout.addComponent(viewType);
		
//		optionLayout.addComponent(viewBtn);
		return optionLayout;
	}
	
	
	
	public HorizontalLayout buildViewTypeForOp(Long policyKey, String intimationNo) {
		
		final Long policyId = policyKey;
       
		Policy policy = policyService.getPolicyByKey(policyKey);
		final OptionGroup viewType = getOptionGroup(policy);
		viewType.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						getClaimByPolicyWiseForOP(policyId,intimationNo);
					} else if (viewType.getValue().equals(INSURED_WISE)) {
					//getClaimByInsuredWise(intimation);
					} else if (viewType.getValue().equals(RISK_WISE)) {
//						getClaimByRiskWise(intimation);
					}
				}
			
			}

			
		});
		NativeButton viewBtn = getViewButtonForOP(policyKey,intimationNo, viewType);
		HorizontalLayout optionLayout = new HorizontalLayout();
		optionLayout.addComponent(viewType);
//		optionLayout.addComponent(viewBtn);
		return optionLayout;
	}
	

	public OptionGroup getOptionGroup(Policy policy) {
		OptionGroup viewType = new OptionGroup();
//		//Vaadin8-setImmediate() viewType.setImmediate(false);
		viewType.addItem(POLICY_WISE);
		viewType.addItem(INSURED_WISE);
//		if ((policy.getPolicyType() != null && !StringUtils.equals(policy.getPolicyType().getValue(),""))
//				&& StringUtils.equalsIgnoreCase(policy.getPolicyType().getValue(),GROUP))
			viewType.addItem(RISK_WISE);
		viewType.setStyleName("inlineStyle");

		viewType.setValue(POLICY_WISE);
		return viewType;
	}

	private NativeButton getViewButton(final Intimation intimation,
			final OptionGroup viewType) {
		NativeButton viewBtn = new NativeButton();
		viewBtn.setCaption("View");
		viewBtn.setData(viewType);
		viewBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						getClaimByPolicyWise(intimation);
					} else if (viewType.getValue().equals(INSURED_WISE)) {
						getClaimByInsuredWise(intimation);
					} else if (viewType.getValue().equals(RISK_WISE)) {
						getClaimByRiskWise(intimation);
					}
				}

			}

		});
		return viewBtn;
	}
	
	private NativeButton getViewButtonForOP(final Long policyKey,String intimationNo,
			final OptionGroup viewType) {
		NativeButton viewBtn = new NativeButton();
		viewBtn.setCaption("View");
		viewBtn.setData(viewType);
		viewBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						getClaimByPolicyWiseForOP(policyKey,intimationNo);
					} else if (viewType.getValue().equals(INSURED_WISE)) {
//						getClaimByInsuredWise(intimation);
					} else if (viewType.getValue().equals(RISK_WISE)) {
//						getClaimByRiskWise(intimation);
					}
				}

			}

		});
		return viewBtn;
	}
	
	private void addListener(final ViewTmpIntimation intimation){
		
		
		  cmbPolicyYear.addValueChangeListener(new ValueChangeListener() {
	   			
	   			@Override
	   			public void valueChange(ValueChangeEvent event) {
	   				SelectValue value = (SelectValue) event.getProperty().getValue();
	   				if(value != null){
	   					getClaimByPolicyYear(intimation, value.getId());
	   				}
	   			}
	   		});
		
	}

	private void getClaimByRiskWise(final Intimation intimation) {
		/*List<ViewTmpIntimation> intimationlist = getRiskWiseClaimList(intimation);
	List<PreviousClaimsTableDTO> claimList = getClaimList(intimationlist);*/
		
	Claim claimforIntimation = claimService.getClaimforIntimation(intimation.getKey());
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = new ArrayList<PreviousClaimsTableDTO>();
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		if(claimforIntimation != null){
			
			previousClaimDTOList = dbCalculationService.getPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
					claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.RISK_WISE_SEARCH_TYPE);
		}else{
			Intimation intimationByKey = intimationService.getIntimationByKey(intimation.getKey());
			previousClaimDTOList = dbCalculationService.getPreviousClaims(0l,intimationByKey.getPolicy().getKey(),
					intimationByKey.getInsured().getKey(),SHAConstants.RISK_WISE_SEARCH_TYPE);
		}
		preauthPreviousClaimsTable.init(RISK_WISE, false, false);
		preauthPreviousClaimsTable.setTableList(previousClaimDTOList);
		preauthPreviousClaimsTable.setWidth("100%");;
	
		
		if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		{
			preauthPreviousClaimsTable.setPAColumnsForPreClaims();
		}
		
		renewedPolicyClaimDetailsTable.setVisible(false);
		List<RenewedPolicyDetails> renewedPolicy = premiaPullService.getRenewedPolicyDetails(claimforIntimation.getIntimation().getPolicy().getPolicyNumber());
		if(renewedPolicy != null && !renewedPolicy.isEmpty()){
		List<PreviousClaimsTableDTO> renewalPreviousClaimList = new ArrayList<PreviousClaimsTableDTO>();
		if(claimforIntimation.getIntimation().getPolicy().getPolicyNumber() != null) {
			if(renewedPolicy != null && !renewedPolicy.isEmpty()) {
				for (RenewedPolicyDetails renewedPolicyDetails : renewedPolicy) {
					Policy policyDetails = intimationService.getPolicyByPolicyNubember(renewedPolicyDetails.getPolicyNumber());
					if(policyDetails != null){
					renewalPreviousClaimList =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
							claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.RISK_WISE_SEARCH_TYPE);
					break;
					//renewalPreviousClaimList.addAll(renewedPreviousClaimDtls);
					}
				}
			}
		}
		/*List<PreviousClaimsTableDTO> renewedPreviousClaimDtls =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
				claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.RISK_WISE_SEARCH_TYPE);*/
		if(renewalPreviousClaimList != null && !renewalPreviousClaimList.isEmpty()){
			renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
			renewedPolicyClaimDetailsTable.setWidth("100%");
			renewedPolicyClaimDetailsTable.setTableList(renewalPreviousClaimList);
			renewedPolicyClaimDetailsTable.setVisible(true);
			
		}
		}
		
		claimlayout.addComponent(preauthPreviousClaimsTable);
		claimlayout.addComponent(renewedPolicyClaimDetailsTable);
		//Vaadin8-setImmediate() claimlayout.setImmediate(false);
	}

	private void getClaimByInsuredWise(final Intimation intimation) {
		
/*		List<ViewTmpIntimation> intimationlist = getInsuredWiseClaimList(intimation);
		getPreviousInsuredNumber(intimation);
		
		List<ViewTmpClaim> currentClaim = claimService.getTmpClaimByIntimation(intimation.getKey());
		
		ViewTmpClaim claim = null;
		if(currentClaim.size() > 0)
		{
			claim = currentClaim.get(0);
		}
		
		List<ViewTmpClaim> previousClaimList = new ArrayList<ViewTmpClaim>();
		
		String policyNumber =intimation.getPolicyNumber();
		
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		
		for (ViewTmpClaim viewTmpClaim : claimsByPolicyNumber) {
			if(null != claim){
				if(null != claim.getIntimation() && null != claim.getIntimation().getInsured() && null != claim.getIntimation().getInsured().getHealthCardNumber() 
					&& claim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber())){
			//if(claim != null && claim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber())){
				previousClaimList.add(viewTmpClaim);
				}
				}else{
					if(null != intimation && null != intimation.getInsured() && null != intimation.getInsured().getHealthCardNumber() 
							&& intimation.getInsured().getHealthCardNumber().equalsIgnoreCase(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber())){
						previousClaimList.add(viewTmpClaim);
					}
			}
		}
		
//		for (ViewTmpIntimation viewTmpIntimation : intimationlist) {
//			 previousClaimList = claimService.getTmpClaimByIntimation(viewTmpIntimation.getKey());
//		}
		try{
			Policy policy = policyService.getPolicy(intimation.getPolicyNumber());
			previousClaimList = getPreviousClaimInsuedWiseForPreviousPolicy(
					policy.getRenewalPolicyNumber(),
					previousClaimList,
					((claim != null) ? (null != claim.getIntimation()
							.getInsured().getHealthCardNumber()) ? claim
							.getIntimation().getInsured().getHealthCardNumber()
							: "" : (null != intimation.getInsured()
							.getHealthCardNumber()) ? intimation.getInsured()
							.getHealthCardNumber() : ""));
			
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
		/*List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousClaimList, ((claim != null) ? claim.getClaimId() : ""));*/
		
		Claim claimforIntimation = claimService.getClaimforIntimation(intimation.getKey());
		
		List<PreviousClaimsTableDTO> previousClaims = new ArrayList<PreviousClaimsTableDTO>();
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		if(claimforIntimation != null){
			
			previousClaims = dbCalculationService.getPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
					claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.INSURED_WISE_SEARCH_TYPE);
		}else{
			Intimation intimationByKey = intimationService.getIntimationByKey(intimation.getKey());
			previousClaims = dbCalculationService.getPreviousClaims(0l,intimationByKey.getPolicy().getKey(),
					intimationByKey.getInsured().getKey(),SHAConstants.INSURED_WISE_SEARCH_TYPE);
		}
		
//		List<PreviousClaimsTableDTO> claimList = getClaimList(intimationlist);
		preauthPreviousClaimsTable.init(INSURED_WISE, false, false);
		
		if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		{
			preauthPreviousClaimsTable.setPAColumnsForPreClaims();
		}

		preauthPreviousClaimsTable.setWidth("100%");
		preauthPreviousClaimsTable.setTableList(previousClaims);
		
		renewedPolicyClaimDetailsTable.setVisible(false);
		List<RenewedPolicyDetails> renewedPolicy = premiaPullService.getRenewedPolicyDetails(claimforIntimation.getIntimation().getPolicy().getPolicyNumber());
		if(renewedPolicy != null && !renewedPolicy.isEmpty()){
		List<PreviousClaimsTableDTO> renewalPreviousClaimList = new ArrayList<PreviousClaimsTableDTO>();
		if(claimforIntimation.getIntimation().getPolicy().getPolicyNumber() != null) {
			if(renewedPolicy != null && !renewedPolicy.isEmpty()) {
				for (RenewedPolicyDetails renewedPolicyDetails : renewedPolicy) {
					Policy policyDetails = intimationService.getPolicyByPolicyNubember(renewedPolicyDetails.getPolicyNumber());
					if(policyDetails != null){
					renewalPreviousClaimList =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
							claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.INSURED_WISE_SEARCH_TYPE);
					break;
					}
				}
			}
		}
		
		
		
		/*List<PreviousClaimsTableDTO> renewedPreviousClaimDtls =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
				claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.INSURED_WISE_SEARCH_TYPE);*/
		if(renewalPreviousClaimList != null && !renewalPreviousClaimList.isEmpty()){
			
			renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
			renewedPolicyClaimDetailsTable.setWidth("100%");
			renewedPolicyClaimDetailsTable.setTableList(renewalPreviousClaimList);
			renewedPolicyClaimDetailsTable.setVisible(true);
		}
	
		}

		claimlayout.addComponent(preauthPreviousClaimsTable);
		claimlayout.addComponent(renewedPolicyClaimDetailsTable);
		//Vaadin8-setImmediate() claimlayout.setImmediate(false);
	}

	private void getClaimByPolicyWise(final Intimation intimation) {

		
		/*List<ViewTmpClaim> currentClaim = claimService.getTmpClaimByIntimation(intimation.getKey());
		
		String policyNumber =intimation.getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);
		
		List<PreviousClaimsTableDTO> previousClaimDTOList= null;
		
		if(currentClaim != null && ! currentClaim.isEmpty()){
			previousClaimDTOList = preauthService
					.getPreviousClaims(previousclaimsList,
							currentClaim.get(0).getClaimId());
		}else{
			previousClaimDTOList = preauthService
					.getPreviousClaims(previousclaimsList,
							"");
		}*/
		
		
		Claim claimforIntimation = claimService.getClaimforIntimation(intimation.getKey());
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		List<PreviousClaimsTableDTO> previousClaims = new ArrayList<PreviousClaimsTableDTO>();
		
		if(claimforIntimation != null){
			
			previousClaims = dbCalculationService.getPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
					claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}else{
			Intimation intimationByKey = intimationService.getIntimationByKey(intimation.getKey());
			previousClaims = dbCalculationService.getPreviousClaims(0l,intimationByKey.getPolicy().getKey(),
					intimationByKey.getInsured().getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}

		preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
		
		if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		{
			preauthPreviousClaimsTable.setPAColumnsForPreClaims();
		}
	
		preauthPreviousClaimsTable.setWidth("100%");
		preauthPreviousClaimsTable.setTableList(previousClaims);
		
		renewedPolicyClaimDetailsTable.setVisible(false);
		List<PreviousClaimsTableDTO> renewalPreviousClaimList = new ArrayList<PreviousClaimsTableDTO>();
		if(claimforIntimation != null){
		List<RenewedPolicyDetails> renewedPolicy = premiaPullService.getRenewedPolicyDetails(claimforIntimation.getIntimation().getPolicy().getPolicyNumber());
		if(renewedPolicy != null && !renewedPolicy.isEmpty()){
		
		if(claimforIntimation.getIntimation().getPolicy().getPolicyNumber() != null) {
			if(renewedPolicy != null && !renewedPolicy.isEmpty()) {
				for (RenewedPolicyDetails renewedPolicyDetails : renewedPolicy) {
					Policy policyDetails = intimationService.getPolicyByPolicyNubember(renewedPolicyDetails.getPolicyNumber());
					if(policyDetails != null){
					renewalPreviousClaimList =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
							claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);
					//renewalPreviousClaimList.addAll(renewedPreviousClaimDtls);
					break;
					}
				}
		}
		}
			
		/*List<PreviousClaimsTableDTO> renewedPreviousClaimDtls =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
				claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);*/
		if(renewalPreviousClaimList != null && !renewalPreviousClaimList.isEmpty()){
			renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
			renewedPolicyClaimDetailsTable.setWidth("100%");
			renewedPolicyClaimDetailsTable.setTableList(renewalPreviousClaimList);
			renewedPolicyClaimDetailsTable.setVisible(true);
			
		}
		}
	}
		claimlayout.addComponent(preauthPreviousClaimsTable);
		claimlayout.addComponent(renewedPolicyClaimDetailsTable);
		//Vaadin8-setImmediate() claimlayout.setImmediate(false);
		
	}
	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
					for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
						if(!generatedList.contains(viewTmpClaim)) {
							generatedList.add(viewTmpClaim);
						}
					}
				}
				if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
					getPreviousClaimForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList);
				} else {
					return generatedList;
				}
			}
		} catch(Exception e) {
			
		}
		return generatedList;
	}
	
	public List<ViewTmpClaim> getPreviousClaimInsuedWiseForPreviousPolicy(String policyNumber, List<ViewTmpClaim> generatedList,String healthCardNumber) {
		
			try {
				Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
				if(renewalPolNo != null) {
					List<ViewTmpClaim> previousPolicyPreviousClaims = claimService.getViewTmpClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
					if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
						for (ViewTmpClaim viewTmpClaim : previousPolicyPreviousClaims) {
							if(viewTmpClaim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(healthCardNumber)){
								if(!generatedList.contains(viewTmpClaim)) {
									generatedList.add(viewTmpClaim);
								}
							}
						}
					}
					if(renewalPolNo != null && renewalPolNo.getRenewalPolicyNumber() != null ) {
						getPreviousClaimInsuedWiseForPreviousPolicy(renewalPolNo.getRenewalPolicyNumber(), generatedList,healthCardNumber);
					} else {
						return generatedList;
					}
				}
			} catch(Exception e) {
			
		}
		return generatedList;
	}
	
	
	public List<ViewTmpClaim> getPreviousClaimForPreviousPolicy(List<ViewTmpClaim> claimsListByPolicyNumber, List<ViewTmpClaim> generatedList) {
		for (ViewTmpClaim viewTmpClaim : claimsListByPolicyNumber) {
			if(viewTmpClaim.getIntimation() != null && viewTmpClaim.getIntimation().getPolicy() != null && viewTmpClaim.getIntimation().getPolicyNumber() != null) {
				System.out.println("PRevious Policy Number " + viewTmpClaim.getIntimation().getPolicyNumber());
				if(!generatedList.contains(viewTmpClaim)) {
					generatedList.add(viewTmpClaim);
					List<ViewTmpClaim> viewTmpClaimsByPolicyNumber = claimService.getViewTmpClaimsByPolicyNumber(viewTmpClaim.getIntimation().getPolicyNumber());
					getPreviousClaimForPreviousPolicy(viewTmpClaimsByPolicyNumber, generatedList);
				}
			}
		}
		return generatedList;
	}
	
	private void getClaimByPolicyYear(final ViewTmpIntimation intimation,Long policyYear){
		Long policyYearFrom = policyYear;
		Long policyYearTo = policyYear + 1l;
		Long policyKey = intimation.getPolicy();
		Policy policyByKey = policyService.getPolicyByKey(policyKey);
		if(policyByKey.getPolicyYear() != null && policyYearTo != null){
			getPolicyByYearWise(intimation,policyYearTo);
		}else{
			preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
			
			if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
			{
				preauthPreviousClaimsTable.setPAColumnsForPreClaims();
			}
			
			
			preauthPreviousClaimsTable.setWidth("100%");
			claimlayout.addComponent(preauthPreviousClaimsTable);
			Notification.show("No records for this time period", Type.ERROR_MESSAGE);
		}
	}
	
	private void getPolicyByYearWise(final ViewTmpIntimation intimation,Long policyYear) {

		
		//List<ViewTmpClaim> currentClaim = claimService.getTmpClaimByIntimation(intimation.getKey());

		
	/*	String policyNumber =intimation.getPolicyNumber();
		List<ViewTmpClaim> previousclaimsList = new ArrayList<ViewTmpClaim>();
		List<ViewTmpClaim> claimsByPolicyNumber = claimService
				.getViewTmpClaimsByPolicyNumber(policyNumber);
		
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = preauthService
				.getPreviousClaims(previousclaimsList,
						(viewTmpClaim != null) ? viewTmpClaim.getClaimId() : "");*/
		
		Claim claimforIntimation = claimService.getClaimforIntimation(intimation.getKey());
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = new ArrayList<PreviousClaimsTableDTO>();
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		if(claimforIntimation != null){
			
			previousClaimDTOList = dbCalculationService.getPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
					claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}else{
			Intimation intimationByKey = intimationService.getIntimationByKey(intimation.getKey());
			previousClaimDTOList = dbCalculationService.getPreviousClaims(0l,intimationByKey.getPolicy().getKey(),
					intimationByKey.getInsured().getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}
		
		
		renewedPolicyClaimDetailsTable.setVisible(false);
		
		List<PreviousClaimsTableDTO> renewalPreviousClaimList = new ArrayList<PreviousClaimsTableDTO>();
		if(claimforIntimation != null){
			List<RenewedPolicyDetails> renewedPolicy = premiaPullService.getRenewedPolicyDetails(claimforIntimation.getIntimation().getPolicy().getPolicyNumber());
			if(renewedPolicy != null && !renewedPolicy.isEmpty()){
			if(claimforIntimation.getIntimation().getPolicy().getPolicyNumber() != null) {
				if(renewedPolicy != null && !renewedPolicy.isEmpty()) {
					for (RenewedPolicyDetails renewedPolicyDetails : renewedPolicy) {
						Policy policyDetails = intimationService.getPolicyByPolicyNubember(renewedPolicyDetails.getPolicyNumber());
						if(policyDetails != null){
						List<PreviousClaimsTableDTO> renewedPreviousClaimDtls =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),policyDetails.getKey(),claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.INSURED_WISE_SEARCH_TYPE);
						renewalPreviousClaimList.addAll(renewedPreviousClaimDtls);
						}
					}
				}
			}
			}
		}
		
		if(policyYear != null & policyYear != dummyYear) {
			List<PreviousClaimsTableDTO> byPolicyYear = new ArrayList<PreviousClaimsTableDTO>();
			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimDTOList) {
				if(previousClaimsTableDTO.getPolicyYear().equals(String.valueOf(policyYear))) {
					byPolicyYear.add(previousClaimsTableDTO);
				}
			}
			preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
			
			if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
			{
				preauthPreviousClaimsTable.setPAColumnsForPreClaims();
			}
			
			preauthPreviousClaimsTable.setWidth("100%");
			preauthPreviousClaimsTable.setTableList(byPolicyYear);
			
			List<PreviousClaimsTableDTO> renewedbyPolicyYear = new ArrayList<PreviousClaimsTableDTO>();
			for (PreviousClaimsTableDTO previousClaimsTableDTO : renewalPreviousClaimList) {
				if(previousClaimsTableDTO.getPolicyYear().equals(String.valueOf(policyYear))) {
					renewedbyPolicyYear.add(previousClaimsTableDTO);
				}
				
			}
			renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
			renewedPolicyClaimDetailsTable.setWidth("100%");
			renewedPolicyClaimDetailsTable.setTableList(renewedbyPolicyYear);
			renewedPolicyClaimDetailsTable.setVisible(true);
			claimlayout.addComponent(preauthPreviousClaimsTable);
			claimlayout.addComponent(renewedPolicyClaimDetailsTable);
			//Vaadin8-setImmediate() claimlayout.setImmediate(false);
		}
		else {
		preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
		
		if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		{
			preauthPreviousClaimsTable.setPAColumnsForPreClaims();
		}
		
		preauthPreviousClaimsTable.setWidth("100%");
		preauthPreviousClaimsTable.setTableList(previousClaimDTOList);
		
		renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
		renewedPolicyClaimDetailsTable.setWidth("100%");
		renewedPolicyClaimDetailsTable.setTableList(renewalPreviousClaimList);
		renewedPolicyClaimDetailsTable.setVisible(true);
		
		claimlayout.addComponent(preauthPreviousClaimsTable);
		claimlayout.addComponent(renewedPolicyClaimDetailsTable);
		//Vaadin8-setImmediate() claimlayout.setImmediate(false);
		}
		
	}
	
	private void getClaimByPolicyWiseForOP(Long policyKey,String intimationNo){
		List<PreviousClaimsTableDTO> claimList = null;
		Policy policy = policyService.getPolicyByKey(policyKey);
		List<OPClaim> claimsByPolicyNumber = claimService
				.getOPClaimsByPolicyNumber(policy.getPolicyNumber());
		
		List<OPClaim> listClaim = new ArrayList<OPClaim>();
		if(intimationNo != null){
			for (OPClaim opClaim : claimsByPolicyNumber) {
				if(!opClaim.getIntimation().getIntimationId().equalsIgnoreCase(intimationNo)){
					listClaim.add(opClaim);
				}
				
			}
	
			claimList = preauthService.getPreviousClaimForOP(listClaim);
		} else {
			claimList = preauthService.getPreviousClaimForOP(claimsByPolicyNumber);
		}

		preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
		preauthPreviousClaimsTable.setWidth("100%");;
		preauthPreviousClaimsTable.setTableList(claimList);
		preauthPreviousClaimsTable.setOPColumnsForPreClaims();
		claimlayout.addComponent(preauthPreviousClaimsTable);
	}

	private List<ViewTmpIntimation> getRiskWiseClaimList(final ViewTmpIntimation intimation) {
		List<ViewTmpIntimation> intimationlist = policyService
				.getTmpIntimationListByPolicy(intimation
						.getPolicyNumber());
		Policy policy = policyService.getPolicy(intimation
				.getPolicyNumber());
		intimationlist.remove(intimation);
		if (policy.getRenewalPolicyNumber() != null
				&& !policy.getPolicyType().equals(FRESH)) {
			List<ViewTmpIntimation> previousIntimationlist = policyService
					.getTmpIntimationListByPolicy(policy.getRenewalPolicyNumber());
			if (previousIntimationlist.size() != 0)
				intimationlist.addAll(previousIntimationlist);

		}
		return intimationlist;
	}

	private List<ViewTmpIntimation> getInsuredWiseClaimList(final ViewTmpIntimation intimation) {
		List<ViewTmpIntimation> intimationlist = policyService
				.getViewTmpIntimationListByInsured(String.valueOf(intimation.getInsured()
						.getInsuredId()));
		String PreviousInsuredId = getPreviousInsuredNumber(intimation);
		intimationlist.remove(intimation);
		if (PreviousInsuredId != null) {
			List<ViewTmpIntimation> previousIntimationlist = policyService
					.getViewTmpIntimationListByInsured(PreviousInsuredId);
			if (previousIntimationlist.size() != 0) {
				intimationlist.addAll(previousIntimationlist);
			}

		}
		
		return intimationlist;
	}
	

	private List<ViewTmpIntimation> getPolicyWiseClaimList(final ViewTmpIntimation intimation) {
		List<ViewTmpIntimation> intimationlist = policyService
				.getTmpIntimationListByPolicy(intimation
						.getPolicyNumber());
		Policy policy = policyService.getPolicy(intimation
				.getPolicyNumber());
		intimationlist.remove(intimation);
		if (policy.getRenewalPolicyNumber() != null) {
			List<ViewTmpIntimation> previousIntimationlist = policyService
					.getTmpIntimationListByPolicy(policy.getRenewalPolicyNumber());
			if (previousIntimationlist.size() != 0)
				intimationlist.addAll(previousIntimationlist);

		}
		return intimationlist;
	}

	private ViewIntimationStatus getClaimStatus(Item item) {
		Claim a_claim = claimService.getClaimByKey((Long) (item
				.getItemProperty("key").getValue()));

		Intimation intimation = a_claim.getIntimation();

		Hospitals hospital = policyService.getVWHospitalByKey(intimation
				.getHospital());

		// IntimationsDto a_intimationDto = DtoConverter
		// .intimationToIntimationDTO(intimation, hospital);
		// ClaimDto a_claimDto = DtoConverter.claimToClaimDTO(a_claim,
		// hospital);

		DtoConverter dtoConverter = new DtoConverter();
		ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);

		// ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
		// a_claimDto, a_intimationDto,
		// intimation.getPolicy().getStatus() == null);

		ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
				a_claimDto, a_claimDto.getNewIntimationDto(), intimation
						.getPolicy().getActiveStatus() == null);

		return intimationStatus;
	}

	private String getPreviousInsuredNumber(ViewTmpIntimation a_intimation) {

		/*TmpInsured tmpInsured = insuredService.getInsured(a_intimation
				.getPolicy().getInsuredId());*/
		Insured insured = insuredService.getCLSInsured(String.valueOf(a_intimation
				.getInsured().getInsuredId()));
		
		// TmpInsured tmpInsured = insuredService.getInsured(a_intimation
		// .getPolicy().getPolicyNumber(), a_intimation.getPolicy()
		// .getInsuredFirstName(), a_intimation.getPolicy()
		// .getInsuredDob());
		if (insured != null)
			return insured.getRelationshipwithInsuredId().getValue();
		return null;

	}

	private List<PreviousClaimsTableDTO> getClaimList(
			List<ViewTmpIntimation> intimationlist) {
		List<PreviousClaimsTableDTO> claimsTableDTOs = new ArrayList<PreviousClaimsTableDTO>();
		PreviousClaimMapper previousClaimMapper = PreviousClaimMapper.getInstance();
		for (ViewTmpIntimation a_intimation : intimationlist) {
			ViewTmpClaim a_claim = claimService.getTmpClaimforIntimation(a_intimation
					.getKey());
			List<ViewTmpDiagnosis> pedValidationsList = pedValidationService
					.getDiagnosisFromViewTmpDiagnosis(a_intimation.getKey());
			if (a_claim != null) {
				PreviousClaimsTableDTO previousClaimsTableDTO = previousClaimMapper
						.getPreviousClaimsTableDTO(a_claim);
				
				if(previousClaimsTableDTO.getKey() != null){
					ViewTmpPreauth previousPreauth = preauthService.getPreviousPreauthFromTmpPreauth(previousClaimsTableDTO.getKey());
					if(previousPreauth != null){
						previousClaimsTableDTO.setClaimAmount(preauthService.getPreauthReqAmtFromTmpPreauth(previousPreauth.getKey(), previousClaimsTableDTO.getKey()));
					}
					Long claimedAmountForROD = preauthService.getClaimedAmountFromTmpReimbursement(previousClaimsTableDTO.getKey());
					if(claimedAmountForROD != null && claimedAmountForROD > 0){
						previousClaimsTableDTO.setClaimAmount(String.valueOf(claimedAmountForROD));
					}
				}
				
				Date tempDate = SHAUtils.formatTimestamp(previousClaimsTableDTO.getAdmissionDate());
				if(a_intimation.getAdmissionDate() != null){
					previousClaimsTableDTO.setAdmissionDate(SHAUtils.formatDate(a_intimation.getAdmissionDate()));
				}
				
				claimsTableDTOs.add(previousClaimsTableDTO);
				String digonizeString = " ";
				if (pedValidationsList.size() != 0
						&& pedValidationsList != null) {

					for (ViewTmpDiagnosis pedValidation : pedValidationsList) {
						digonizeString = (digonizeString == " " ? ""
								: digonizeString + ", ")
								+ masterService.getDiagnosis(pedValidation
										.getDiagnosisId());
					}
				}
				previousClaimsTableDTO.setDiagnosis(digonizeString);
				
				
				List<ViewTmpReimbursement> reimbursement = preauthService.getTmpReimbursment(previousClaimsTableDTO.getKey());
				
				if(reimbursement != null){
					String benefitcover="";
					String coverValue = "";
					Boolean isPA = false;
					Double paApprovedAmt = 0d;
					Double paClaimedAmt = 0d;
					for (ViewTmpReimbursement viewTmpReimbursement : reimbursement) {
						
						if(viewTmpReimbursement.getBenefitsId() != null)
						{
							MastersValue masterValue = viewTmpReimbursement.getBenefitsId();
							
							if(masterValue != null && masterValue.getValue() != null)
							{
								benefitcover += masterValue.getValue() + ", ";
									
							}
						}
						coverValue = preauthService.getCoverValueForViewBasedOnrodKey(viewTmpReimbursement.getKey());
						
						Reimbursement reimbursementObjectByKey = acknowledgmentDocumentService.getReimbursement(viewTmpReimbursement.getKey());
						if(reimbursementObjectByKey != null){
							Claim claim = reimbursementObjectByKey.getClaim();
							
						if(claim != null && claim.getLobId()!= null && (ReferenceTable.PA_LOB_KEY).equals(claim.getLobId())){
							
							isPA = true;
							paApprovedAmt = paApprovedAmt + acknowledgmentDocumentService.getBenefitAddOnOptionalApprovedAmt(reimbursementObjectByKey);
							
							DocAcknowledgement docAcknowledgement = reimbursementObjectByKey.getDocAcknowLedgement();
							
							if(docAcknowledgement != null && docAcknowledgement.getProcessClaimType() != null){
								if(docAcknowledgement.getProcessClaimType().equalsIgnoreCase(SHAConstants.HEALTH_TYPE) && docAcknowledgement.getBenifitClaimedAmount() != null){
									paClaimedAmt = paClaimedAmt + docAcknowledgement.getBenifitClaimedAmount();
								}else if(docAcknowledgement.getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_TYPE)){
									paClaimedAmt = paClaimedAmt + acknowledgmentDocumentService.getClaimedAmountValueForView(docAcknowledgement);
								}
							}
							}
						}
						
						
					}
					if(isPA){
						previousClaimsTableDTO.setApprovedAmount(String.valueOf(paApprovedAmt));
						previousClaimsTableDTO.setClaimAmount(String.valueOf(paClaimedAmt));
					}
					if(benefitcover.isEmpty())
					{
						previousClaimsTableDTO.setBenefits(coverValue);
					}else
					{
						previousClaimsTableDTO.setBenefits(benefitcover+", "+coverValue);
					}
				}
			}
		}

		return claimsTableDTOs;

	}

	
}
