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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
@ViewScoped
public class ViewBasePolicyClaimsWindowOpen extends ViewComponent {
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



	public void showValues(Long policyKey) {
		claimlayout = getPrivousClaimsLayout(policyKey);
		claimlayout.setMargin(true);
		this.setCompositionRoot(claimlayout);		
			getClaimByPolicyWise(policyKey);
	}
	@PostConstruct
	public void initView() {
		setCaption("Claims of this policy");
		this.setHeight("712px");
		this.setWidth("1300px");
//		setModal(true);
//		setClosable(true);
//		setResizable(true);

	}

public VerticalLayout getPrivousClaimsLayout(Long policyKey) {
		
		claimlayout = new VerticalLayout();
		claimlayout.setMargin(true);
		
		
		Label policyLabel = new Label("Policy Year");
		policyLabel.addStyleName("");
		cmbPolicyYear = new ComboBox("Policy Year");
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		
		cmbPolicyYear.setContainerDataSource(policyYearValues);
		cmbPolicyYear.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPolicyYear.setItemCaptionPropertyId("value");
		
		
		addListener(policyKey);
		
		HorizontalLayout optionLayout = buildViewType(policyKey);
		optionLayout.setWidth("100%");
		optionLayout.setSpacing(true);
		optionLayout.setMargin(true);
		
		claimlayout.addComponent(optionLayout);
		claimlayout.addComponent(cmbPolicyYear);
		claimlayout.setComponentAlignment(cmbPolicyYear, Alignment.MIDDLE_LEFT);	
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
	

	public HorizontalLayout buildViewType(Long policyKey) {
		final OptionGroup viewType = getOptionGroup(null);		
		Collection<OptionGroup> itemIds = (Collection<OptionGroup>) viewType.getContainerDataSource().getItemIds();
		viewType.setValue(itemIds.toArray()[0]);
		viewType.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						getClaimByPolicyWise(policyKey);
						claimlayout.removeComponent(cmbPolicyYear);
						claimlayout.addComponent(cmbPolicyYear,1);
					} 
				}
			
			}

		});
		NativeButton viewBtn = getViewButton(policyKey, viewType);
		HorizontalLayout optionLayout = new HorizontalLayout();
		optionLayout.addComponent(viewType);
		
//		optionLayout.addComponent(viewBtn);
		return optionLayout;
	}
	
	
	
	public OptionGroup getOptionGroup(Policy policy) {
		OptionGroup viewType = new OptionGroup();
//		//Vaadin8-setImmediate() viewType.setImmediate(false);
		viewType.addItem(POLICY_WISE);
		/*viewType.addItem(INSURED_WISE);
//		if ((policy.getPolicyType() != null && !StringUtils.equals(policy.getPolicyType().getValue(),""))
//				&& StringUtils.equalsIgnoreCase(policy.getPolicyType().getValue(),GROUP))
			viewType.addItem(RISK_WISE);
		viewType.setStyleName("inlineStyle");*/

		viewType.setValue(POLICY_WISE);
		return viewType;
	}

	private NativeButton getViewButton(Long policyKey,
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
						getClaimByPolicyWise(policyKey);
					} 
				}

			}

		});
		return viewBtn;
	}
	
	
	private void addListener(Long policyKey){
		
		
		  cmbPolicyYear.addValueChangeListener(new ValueChangeListener() {
	   			
	   			@Override
	   			public void valueChange(ValueChangeEvent event) {
	   				SelectValue value = (SelectValue) event.getProperty().getValue();
	   				if(value != null){
	   					getClaimByPolicyYear(policyKey, value.getId());
	   				}
	   			}
	   		});
		
	}

	private void getClaimByPolicyWise(Long policyKey) {
		//Claim claimforIntimation = claimService.getClaimforIntimation(policyKey);
		DBCalculationService dbCalculationService = new DBCalculationService();
		
		List<PreviousClaimsTableDTO> previousClaims = new ArrayList<PreviousClaimsTableDTO>();
		previousClaims = dbCalculationService.getBasePolicyPreviousClaims(policyKey,SHAConstants.POLICY_WISE_SEARCH_TYPE);
		/*if(claimforIntimation != null){
			
			previousClaims = dbCalculationService.getBasePolicyPreviousClaims(policyKey,
					SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}else{
			//Intimation intimationByKey = intimationService.getIntimationByKey(policyKey);
			
			previousClaims = dbCalculationService.getBasePolicyPreviousClaims(Policy.getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}*/

		preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
		
		/*if(policyKey.getProcessClaimType() !=null  && policyKey.getProcessClaimType().equalsIgnoreCase("P"))
		{
			preauthPreviousClaimsTable.setPAColumnsForPreClaims();
		}
	*/
		preauthPreviousClaimsTable.setWidth("100%");
		preauthPreviousClaimsTable.setTableList(previousClaims);
		
		renewedPolicyClaimDetailsTable.setVisible(false);
		/*List<PreviousClaimsTableDTO> renewalPreviousClaimList = new ArrayList<PreviousClaimsTableDTO>();
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
			
		List<PreviousClaimsTableDTO> renewedPreviousClaimDtls =  dbCalculationService.getRenewedPolicyPreviousClaims(claimforIntimation.getKey(),claimforIntimation.getIntimation().getPolicy().getKey(),
				claimforIntimation.getIntimation().getInsured().getKey(),SHAConstants.POLICY_WISE_SEARCH_TYPE);
		if(renewalPreviousClaimList != null && !renewalPreviousClaimList.isEmpty()){
			renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
			renewedPolicyClaimDetailsTable.setWidth("100%");
			renewedPolicyClaimDetailsTable.setTableList(renewalPreviousClaimList);
			renewedPolicyClaimDetailsTable.setVisible(true);
			
		}
		}
	}*/
		claimlayout.addComponent(preauthPreviousClaimsTable);
		//claimlayout.addComponent(renewedPolicyClaimDetailsTable);
		
	}
	
	private void getClaimByPolicyYear(Long policyKey,Long policyYear){
		Long policyYearFrom = policyYear;
		Long policyYearTo = policyYear + 1l;
		//Long policyKey = intimation.getPolicy();
		Policy policyByKey = policyService.getPolicyByKey(policyKey);
		if(policyByKey.getPolicyYear() != null && policyYearTo != null){
			getPolicyByYearWise(policyByKey.getKey(),policyYearTo);
		}else{
			preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
			
			/*if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
			{
				preauthPreviousClaimsTable.setPAColumnsForPreClaims();
			}*/
			
			
			preauthPreviousClaimsTable.setWidth("100%");
			claimlayout.addComponent(preauthPreviousClaimsTable);
			Notification.show("No records for this time period", Type.ERROR_MESSAGE);
		}
	}
	
	private void getPolicyByYearWise(Long policyKey,Long policyYear) {

		
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
		
		//Claim claimforIntimation = claimService.getClaimforIntimation(intimation.getKey());
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = new ArrayList<PreviousClaimsTableDTO>();
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		previousClaimDTOList = dbCalculationService.getBasePolicyPreviousClaims(policyKey,
				SHAConstants.POLICY_WISE_SEARCH_TYPE);
		/*if(claimforIntimation != null){
			
			previousClaimDTOList = dbCalculationService.getBasePolicyPreviousClaims(claimforIntimation.getIntimation().getPolicy().getKey(),
					SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}else{
			//Intimation intimationByKey = intimationService.getIntimationByKey(intimation.getKey());
			previousClaimDTOList = dbCalculationService.getBasePolicyPreviousClaims(policyKey,
					SHAConstants.POLICY_WISE_SEARCH_TYPE);
		}*/
		
		
		renewedPolicyClaimDetailsTable.setVisible(false);
		
		//List<PreviousClaimsTableDTO> renewalPreviousClaimList = new ArrayList<PreviousClaimsTableDTO>();
		/*if(claimforIntimation != null){
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
		}*/
		
		if(policyYear != null & policyYear != dummyYear) {
			List<PreviousClaimsTableDTO> byPolicyYear = new ArrayList<PreviousClaimsTableDTO>();
			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimDTOList) {
				if(previousClaimsTableDTO.getPolicyYear().equals(String.valueOf(policyYear))) {
					byPolicyYear.add(previousClaimsTableDTO);
				}
			}
			preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
			
			/*if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
			{
				preauthPreviousClaimsTable.setPAColumnsForPreClaims();
			}
			*/
			preauthPreviousClaimsTable.setWidth("100%");
			preauthPreviousClaimsTable.setTableList(byPolicyYear);
			
			/*List<PreviousClaimsTableDTO> renewedbyPolicyYear = new ArrayList<PreviousClaimsTableDTO>();
			for (PreviousClaimsTableDTO previousClaimsTableDTO : renewalPreviousClaimList) {
				if(previousClaimsTableDTO.getPolicyYear().equals(String.valueOf(policyYear))) {
					renewedbyPolicyYear.add(previousClaimsTableDTO);
				}
				
			}*/
			/*renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
			renewedPolicyClaimDetailsTable.setWidth("100%");
			renewedPolicyClaimDetailsTable.setTableList(renewedbyPolicyYear);
			renewedPolicyClaimDetailsTable.setVisible(true);*/
			claimlayout.addComponent(preauthPreviousClaimsTable);
			//claimlayout.addComponent(renewedPolicyClaimDetailsTable);
			//Vaadin8-setImmediate() claimlayout.setImmediate(false);
		}
		else {
		preauthPreviousClaimsTable.init(POLICY_WISE, false, false);
		
		/*if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
		{
			preauthPreviousClaimsTable.setPAColumnsForPreClaims();
		}*/
		
		preauthPreviousClaimsTable.setWidth("100%");
		preauthPreviousClaimsTable.setTableList(previousClaimDTOList);
		
		/*renewedPolicyClaimDetailsTable.init(RENEWED_POLICIES, false, false);
		renewedPolicyClaimDetailsTable.setWidth("100%");
		renewedPolicyClaimDetailsTable.setTableList(renewalPreviousClaimList);
		renewedPolicyClaimDetailsTable.setVisible(true);*/
		
		claimlayout.addComponent(preauthPreviousClaimsTable);
		//claimlayout.addComponent(renewedPolicyClaimDetailsTable);
		//Vaadin8-setImmediate() claimlayout.setImmediate(false);
		}
		
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
