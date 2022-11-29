package com.shaic.claim.OMPprocessrejection.detailPage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.search.OMPReceiptofDocumentsAndBillEntryService;
import com.shaic.claim.intimation.ViewIntimationStatus;
import com.shaic.claim.intimation.create.dto.DtoConverter;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocAcknowledgement;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.omp.OMPBenefitCover;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.newcode.wizard.domain.omp.OMPPreviousClaimMapper;
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
public class ViewOMPPreviousClaimWindowOpen extends ViewComponent {

	private static final String GROUP = "Group";
	private static final String FRESH = "Fresh";
//	private static final String RISK_WISE = "Risk Wise";
	private static final String POLICY_WISE = "Policy Wise";
	private static final String INSURED_WISE = "Insured Wise";

//	@EJB
//	private PEDValidationService pedValidationService;
//	
//	@EJB
//	private PreauthService preauthService;

	@EJB
	private OMPClaimService claimService;
	@EJB
	private PolicyService policyService;
	@EJB
	private OMPIntimationService intimationService;
	@EJB
	private InsuredService insuredService;
	@EJB
	private MasterService masterService;

	@EJB
	private OMPReceiptofDocumentsAndBillEntryService acknowledgmentDocumentService;
	
	private VerticalLayout claimlayout;
	
	private ComboBox cmbPolicyYear;
	
	private Long dummyYear = 2l;

	private ViewOMPPreviousClaimsTable previousClaimsTable;

	private static final long serialVersionUID = -2905296721396984772L;
	
	public void setPreviousClaimTable(ViewOMPPreviousClaimsTable previousClaimTable){
		this.previousClaimsTable = previousClaimTable;
	}

	public void showValues(String intimationNumber) {

		claimlayout = getPrivousClaimsLayout(intimationNumber);
		claimlayout.setMargin(true);
		this.setCompositionRoot(claimlayout);
		final OMPIntimation intimation = intimationService
				.getIntimationByNo(intimationNumber);
		getClaimByPolicyWise(intimation);
	}

	@PostConstruct
	public void initView() {
		setCaption("Previous Claim Details");
		this.setHeight("712px");
		this.setWidth("1300px");
	}

	public VerticalLayout getPrivousClaimsLayout(String intimationNumber) {
		HorizontalLayout optionLayout = buildViewType(intimationNumber);
		optionLayout.setWidth("100%");
		optionLayout.setSpacing(true);
		optionLayout.setMargin(true);
		claimlayout = new VerticalLayout();
		claimlayout.setMargin(true);
		
		
		Label policyLabel = new Label("Policy Year");
		policyLabel.addStyleName("");
		cmbPolicyYear = new ComboBox("Policy Year");
		BeanItemContainer<SelectValue> policyYearValues = getPolicyYearValues();
		
		cmbPolicyYear.setContainerDataSource(policyYearValues);
		cmbPolicyYear.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPolicyYear.setItemCaptionPropertyId("value");
		
		claimlayout.addComponent(optionLayout);
//		claimlayout.addComponent(policyLabel);
		claimlayout.addComponent(cmbPolicyYear);
		claimlayout.setComponentAlignment(cmbPolicyYear, Alignment.MIDDLE_LEFT);
		
		final OMPIntimation intimation = intimationService
				.getIntimationByNo(intimationNumber);
		
		addListener(intimation);	
		
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
	
	public HorizontalLayout buildViewType(String intimationNo) {
		final OMPIntimation intimation = intimationService
				.getIntimationByNo(intimationNo);
		String policyNumber = intimation.getPolicy().getPolicyNumber();
		Policy policy = policyService.getPolicy(policyNumber);
		final OptionGroup viewType = getOptionGroup(policy);
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
						
					} 
//					else if (viewType.getValue().equals(RISK_WISE)) {
//						getClaimByRiskWise(intimation);
//						claimlayout.removeComponent(cmbPolicyYear);
//					}
				}
			
			}

		});
		NativeButton viewBtn = getViewButton(intimation, viewType);
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
//		viewType.addItem(RISK_WISE);
		viewType.setStyleName("inlineStyle");

		viewType.setValue(POLICY_WISE);
		return viewType;
	}

	private NativeButton getViewButton(final OMPIntimation intimation,
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
					} 
//					else if (viewType.getValue().equals(RISK_WISE)) {
//						getClaimByRiskWise(intimation);
//					}
				}

			}

		});
		return viewBtn;
	}
	
	private void addListener(final OMPIntimation intimation){
		
		
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

//	private void getClaimByRiskWise(final OMPIntimation intimation) {
//		List<OMPIntimation> intimationlist = getRiskWiseClaimList(intimation);
//	List<PreviousClaimsTableDTO> claimList = getClaimList(intimationlist);
//	
//		previousClaimsTable.setTableList(claimList);
//		previousClaimsTable.setWidth("100%");;
//		previousClaimsTable.init(RISK_WISE, false, false);
//		previousClaimsTable.setPAColumnsForPreClaims();
//				
//		claimlayout.addComponent(previousClaimsTable);
//	}

	private void getClaimByInsuredWise(final OMPIntimation intimation) {
		
		List<OMPIntimation> intimationlist = getInsuredWiseClaimList(intimation);
		getPreviousInsuredNumber(intimation);
		
		List<OMPClaim> currentClaim = claimService.getClaimByIntimation(intimation.getKey());
		
		OMPClaim claim = null;
		if(currentClaim.size() > 0)
		{
			claim = currentClaim.get(0);
		}
		
		List<OMPClaim> previousClaimList = new ArrayList<OMPClaim>();
		
		String policyNumber =intimation.getPolicy().getPolicyNumber();
		
		List<OMPClaim> claimsByPolicyNumber = claimService
				.getClaimsByPolicyNumber(policyNumber);
		
		for (OMPClaim OMPClaim : claimsByPolicyNumber) {
			if(null != claim){
				if(null != claim.getIntimation() && null != claim.getIntimation().getInsured() && null != claim.getIntimation().getInsured().getHealthCardNumber() 
					&& claim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(OMPClaim.getIntimation().getInsured().getHealthCardNumber())){
			//if(claim != null && claim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(OMPClaim.getIntimation().getInsured().getHealthCardNumber())){
				previousClaimList.add(OMPClaim);
				}
				}else{
					if(null != intimation && null != intimation.getInsured() && null != intimation.getInsured().getHealthCardNumber() 
							&& intimation.getInsured().getHealthCardNumber().equalsIgnoreCase(OMPClaim.getIntimation().getInsured().getHealthCardNumber())){
						previousClaimList.add(OMPClaim);
					}
			}
		}
		
//		for (ViewTmpIntimation viewTmpIntimation : intimationlist) {
//			 previousClaimList = claimService.getTmpClaimByIntimation(viewTmpIntimation.getKey());
//		}
		try{
			
			previousClaimList = getPreviousClaimInsuedWiseForPreviousPolicy(
					intimation.getPolicy().getRenewalPolicyNumber(),
					previousClaimList,
					((claim != null) ? (null != claim.getIntimation()
							.getInsured().getHealthCardNumber()) ? claim
							.getIntimation().getInsured().getHealthCardNumber()
							: "" : (null != intimation.getInsured()
							.getHealthCardNumber()) ? intimation.getInsured()
							.getHealthCardNumber() : ""));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = claimService
				.getPreviousClaims(previousClaimList, ((claim != null) ? claim.getClaimId() : ""));
		
//		List<PreviousClaimsTableDTO> claimList = getClaimList(intimationlist);
		previousClaimsTable.init(INSURED_WISE, false, false);
		
//		if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
//		{
//			previousClaimsTable.setPAColumnsForPreClaims();
//		}
		
		
		previousClaimsTable.setWidth("100%");
		previousClaimsTable.setTableList(previousClaimDTOList);

		claimlayout.addComponent(previousClaimsTable);
	}

	private void getClaimByPolicyWise(final OMPIntimation intimation) {

		
		List<OMPClaim> currentClaim = claimService.getClaimByIntimation(intimation.getKey());
		
		String policyNumber =intimation.getPolicy().getPolicyNumber();
		List<OMPClaim> previousclaimsList = new ArrayList<OMPClaim>();
		List<OMPClaim> claimsByPolicyNumber = claimService
				.getClaimsByPolicyNumber(policyNumber);
		
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);
		
		List<PreviousClaimsTableDTO> previousClaimDTOList= null;
		
		if(currentClaim != null && ! currentClaim.isEmpty()){
			previousClaimDTOList = claimService
					.getPreviousClaims(previousclaimsList,
							currentClaim.get(0).getClaimId());
		}else{
			previousClaimDTOList = claimService
					.getPreviousClaims(previousclaimsList,
							"");
		}

		previousClaimsTable.init(POLICY_WISE, false, false);
		
//		if(intimation.getProcessClaimType() !=null  && intimation.getProcessClaimType().equalsIgnoreCase("P"))
//		{
//			previousClaimsTable.setPAColumnsForPreClaims();
//		}
		
		
		previousClaimsTable.setWidth("100%");
		previousClaimsTable.setTableList(previousClaimDTOList);
		claimlayout.addComponent(previousClaimsTable);
		
	}
	
	public List<OMPClaim> getPreviousClaimForPreviousPolicy(String policyNumber, List<OMPClaim> generatedList) {
		try {
			Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
			if(renewalPolNo != null) {
				List<OMPClaim> previousPolicyPreviousClaims = claimService.getClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
				if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
					for (OMPClaim OMPClaim : previousPolicyPreviousClaims) {
						if(!generatedList.contains(OMPClaim)) {
							generatedList.add(OMPClaim);
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
	
	public List<OMPClaim> getPreviousClaimInsuedWiseForPreviousPolicy(String policyNumber, List<OMPClaim> generatedList,String healthCardNumber) {
		
			try {
				Policy renewalPolNo = policyService.getByPolicyNumber(policyNumber);
				if(renewalPolNo != null) {
					List<OMPClaim> previousPolicyPreviousClaims = claimService.getClaimsByPolicyNumber(renewalPolNo.getPolicyNumber());
					if(previousPolicyPreviousClaims != null && !previousPolicyPreviousClaims.isEmpty()) {
						for (OMPClaim OMPClaim : previousPolicyPreviousClaims) {
							if(OMPClaim.getIntimation().getInsured().getHealthCardNumber().equalsIgnoreCase(healthCardNumber)){
								if(!generatedList.contains(OMPClaim)) {
									generatedList.add(OMPClaim);
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
	
	
	public List<OMPClaim> getPreviousClaimForPreviousPolicy(List<OMPClaim> claimsListByPolicyNumber, List<OMPClaim> generatedList) {
		for (OMPClaim OMPClaim : claimsListByPolicyNumber) {
			if(OMPClaim.getIntimation() != null && OMPClaim.getIntimation().getPolicy() != null && OMPClaim.getIntimation().getPolicy().getPolicyNumber() != null) {
				System.out.println("PRevious Policy Number " + OMPClaim.getIntimation().getPolicy().getPolicyNumber());
				if(!generatedList.contains(OMPClaim)) {
					generatedList.add(OMPClaim);
					List<OMPClaim> OMPClaimsByPolicyNumber = claimService.getClaimsByPolicyNumber(OMPClaim.getIntimation().getPolicy().getPolicyNumber());
					getPreviousClaimForPreviousPolicy(OMPClaimsByPolicyNumber, generatedList);
				}
			}
		}
		return generatedList;
	}
	
	private void getClaimByPolicyYear(final OMPIntimation intimation,Long policyYear){
		Long policyYearFrom = policyYear;
		Long policyYearTo = policyYear + 1l;
		if(intimation.getPolicy().getPolicyYear() != null && policyYearTo != null){
			getPolicyByYearWise(intimation,policyYearTo);
		}else{
			previousClaimsTable.init(POLICY_WISE, false, false);
//			previousClaimsTable.setPAColumnsForPreClaims();
			previousClaimsTable.setWidth("100%");
			claimlayout.addComponent(previousClaimsTable);
			Notification.show("No records for this time period", Type.ERROR_MESSAGE);
		}
	}
	
	private void getPolicyByYearWise(final OMPIntimation intimation,Long policyYear) {

		
		List<OMPClaim> currentClaim = claimService.getClaimByIntimation(intimation.getKey());
		
		OMPClaim OMPClaim = null;
		
		if(currentClaim.size() > 0)
		{
			OMPClaim = currentClaim.get(0);
			}
		
		String policyNumber =intimation.getPolicy().getPolicyNumber();
		List<OMPClaim> previousclaimsList = new ArrayList<OMPClaim>();
		List<OMPClaim> claimsByPolicyNumber = claimService
				.getClaimsByPolicyNumber(policyNumber);
		
		Policy byPolicyNumber = policyService.getByPolicyNumber(policyNumber);
		previousclaimsList.addAll(claimsByPolicyNumber);
		
		previousclaimsList = getPreviousClaimForPreviousPolicy(byPolicyNumber.getRenewalPolicyNumber(), previousclaimsList);
		
		List<PreviousClaimsTableDTO> previousClaimDTOList = claimService
				.getPreviousClaims(previousclaimsList,
						(OMPClaim != null) ? OMPClaim.getClaimId() : "");
		if(policyYear != null & policyYear != dummyYear) {
			List<PreviousClaimsTableDTO> byPolicyYear = new ArrayList<PreviousClaimsTableDTO>();
			for (PreviousClaimsTableDTO previousClaimsTableDTO : previousClaimDTOList) {
				if(previousClaimsTableDTO.getPolicyYear().equals(String.valueOf(policyYear))) {
					byPolicyYear.add(previousClaimsTableDTO);
				}
			}
			previousClaimsTable.init(POLICY_WISE, false, false);
//			previousClaimsTable.setPAColumnsForPreClaims();
			previousClaimsTable.setWidth("100%");
			previousClaimsTable.setTableList(byPolicyYear);
			claimlayout.addComponent(previousClaimsTable);
		}
		else {
		previousClaimsTable.init(POLICY_WISE, false, false);
//		previousClaimsTable.setPAColumnsForPreClaims();
		
		previousClaimsTable.setWidth("100%");
		previousClaimsTable.setTableList(previousClaimDTOList);
		claimlayout.addComponent(previousClaimsTable);
		}
		
	}
	
	private List<OMPIntimation> getRiskWiseClaimList(final OMPIntimation intimation) {
		List<OMPIntimation> intimationlist = policyService
				.getOMPIntimationListByPolicy(intimation.getPolicy()
						.getPolicyNumber());
		Policy policy = policyService.getPolicy(intimation.getPolicy()
				.getPolicyNumber());
		intimationlist.remove(intimation);
		if (policy.getRenewalPolicyNumber() != null
				&& !policy.getPolicyType().equals(FRESH)) {
			List<OMPIntimation> previousIntimationlist = policyService
					.getOMPIntimationListByPolicy(policy.getRenewalPolicyNumber());
			if (previousIntimationlist.size() != 0)
				intimationlist.addAll(previousIntimationlist);

		}
		return intimationlist;
	}

	private List<OMPIntimation> getInsuredWiseClaimList(final OMPIntimation intimation) {
		List<OMPIntimation> intimationlist = policyService
				.getOmpIntimationListByInsured(String.valueOf(intimation.getInsured()
						.getInsuredId()));
		String PreviousInsuredId = getPreviousInsuredNumber(intimation);
		intimationlist.remove(intimation);
		if (PreviousInsuredId != null) {
			List<OMPIntimation> previousIntimationlist = policyService
					.getOmpIntimationListByInsured(PreviousInsuredId);
			if (previousIntimationlist.size() != 0) {
				intimationlist.addAll(previousIntimationlist);
			}
		}
		
		return intimationlist;
	}	

	private List<OMPIntimation> getPolicyWiseClaimList(final OMPIntimation intimation) {
		List<OMPIntimation> intimationlist = policyService
				.getOMPIntimationListByPolicy(intimation.getPolicy()
						.getPolicyNumber());
		Policy policy = policyService.getPolicy(intimation.getPolicy()
				.getPolicyNumber());
		intimationlist.remove(intimation);
		if (policy.getRenewalPolicyNumber() != null) {
			List<OMPIntimation> previousIntimationlist = policyService
					.getOMPIntimationListByPolicy(policy.getRenewalPolicyNumber());
			if (previousIntimationlist.size() != 0)
				intimationlist.addAll(previousIntimationlist);

		}
		return intimationlist;
	}

	private ViewIntimationStatus getClaimStatus(Item item) {
		OMPClaim a_claim = claimService.getClaimByKey((Long) (item
				.getItemProperty("key").getValue()));

		OMPIntimation intimation = a_claim.getIntimation();

		Hospitals hospital = policyService.getVWHospitalByKey(intimation
				.getHospital());
	
		DtoConverter dtoConverter = new DtoConverter();
		ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);

		ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
				a_claimDto, a_claimDto.getNewIntimationDto(), intimation
						.getPolicy().getActiveStatus() == null);

		return intimationStatus;
	}

	private String getPreviousInsuredNumber(OMPIntimation a_intimation) {
		Insured insured = insuredService.getCLSInsured(String.valueOf(a_intimation
				.getInsured().getInsuredId()));
		if (insured != null)
			return insured.getRelationshipwithInsuredId().getValue();
		return null;

	}

	private List<PreviousClaimsTableDTO> getClaimList(
			List<OMPIntimation> intimationlist) {
		List<PreviousClaimsTableDTO> claimsTableDTOs = new ArrayList<PreviousClaimsTableDTO>();
		OMPPreviousClaimMapper previousClaimMapper = OMPPreviousClaimMapper.getInstance();
		for (OMPIntimation a_intimation : intimationlist) {
			OMPClaim a_claim = claimService.getClaimforIntimation(a_intimation
					.getKey());
			if (a_claim != null) {
				PreviousClaimsTableDTO previousClaimsTableDTO = previousClaimMapper
						.getPreviousClaimsTableDTO(a_claim);
				
				if(a_intimation.getAdmissionDate() != null){
					previousClaimsTableDTO.setAdmissionDate(SHAUtils.formatDate(a_intimation.getAdmissionDate()));
				}
				
				claimsTableDTOs.add(previousClaimsTableDTO);
				String digonizeString = " ";
				previousClaimsTableDTO.setDiagnosis(digonizeString);
				
				List<OMPReimbursement> reimbursement = claimService.getReimbursmentListByClaimKey(previousClaimsTableDTO.getKey());
				
				if(reimbursement != null){
					String benefitcover="";
					String coverValue = "";
					Boolean isPA = false;
					Double paApprovedAmt = 0d;
					Double paClaimedAmt = 0d;
					for (OMPReimbursement ompReimbursement : reimbursement) {
						
						List<OMPBenefitCover> ompBenefitsList =  acknowledgmentDocumentService.getOMPBenefitsByRodKey(ompReimbursement.getKey());
								
						if(ompBenefitsList != null && !ompBenefitsList.isEmpty()){
							for (OMPBenefitCover ompBenefitCover : ompBenefitsList) {
//								benefitcover += ompBenefitCover.getcategory() + ", ";
							}
						}
						
//						if(ompReimbursement.getBenefitsId() != null)
//						{
//							MastersValue masterValue = ompReimbursement.getBenefitsId();
//							
//							if(masterValue != null && masterValue.getValue() != null)
//							{
//								benefitcover += masterValue.getValue() + ", ";
//									
//							}
//						}
//						coverValue = preauthService.getCoverValueForViewBasedOnrodKey(ompReimbursement.getKey());
						
						OMPReimbursement reimbursementObjectByKey = acknowledgmentDocumentService.getReimbursement(ompReimbursement.getKey());
						if(reimbursementObjectByKey != null){
							OMPClaim claim = reimbursementObjectByKey.getClaim();
							isPA = true;
							paApprovedAmt = paApprovedAmt + acknowledgmentDocumentService.getBenefitAddOnOptionalApprovedAmt(reimbursementObjectByKey.getKey());
							
							OMPDocAcknowledgement docAcknowledgement = reimbursementObjectByKey.getDocAcknowLedgement();
							paClaimedAmt = paClaimedAmt + acknowledgmentDocumentService.getClaimedAmountValueForView(reimbursementObjectByKey.getKey());
						}
					}
					if(isPA){
						previousClaimsTableDTO.setApprovedAmount(String.valueOf(paApprovedAmt));
						previousClaimsTableDTO.setClaimAmount(String.valueOf(paClaimedAmt));
					}
					previousClaimsTableDTO.setBenefits(benefitcover);
				}
			}
		}
		return claimsTableDTOs;
	}

	
}
