package com.shaic.claim.registration.balancesuminsured.view;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.InsuredDto;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.InsuredMapper;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class BalanceSumInsured extends ViewComponent {
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private VerticalLayout mainLayout;
	private HorizontalLayout balanceSuminsuredhorizontal;
	private HorizontalLayout balanceSuminsuredhorizontalBottom;
	private FormLayout formlayoutRight;
	private FormLayout formlayoutleft;
	private TextField txtSumInsuredAfterProvision;
	private TextField txtCurrentProvisionAmt;
	private TextField previousClaimsPaid;
	private TextField txtBalanceSumInsured;
	private TextField outstandingClaim;
	private TextField txtCummulativeBonus;
	private TextField txtOriginalSI;
	// private TextField age;
	private TextField txtBalanceSIInsuredName;
	private TextField dummyField6;
	private TextField dummyField7;
	private TextField dummyField8;
	private TextField rechargSI;
	private TextField restoreSI;
	private TextField limitOfCover;

	private TmpCPUCode tmpCpuCode;
	
	private Panel mainPanel;
	
	private TextField definedLimit;
	
	

	@Inject
	private Instance<BalanceSumInsuredTable> balanceSumInsuredTable;

	@EJB
	private IntimationService intimationService;

	@EJB
	private PolicyService policyService;

	@EJB
	private ReimbursementService reimbursementService;

	@EJB
	private ClaimService claimService;

	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private InsuredService insuredService;
	
	@Inject
	private Instance<ComprehensiveHospitalisationTable> hospitalisationTable;
	
	private ComprehensiveHospitalisationTable hospitalisationTableInstance;
	
	@Inject
	private Instance<BalanceSumInsuredViewForMediClassicGroup> balanceSumInsuredViewForMediClassicGroupTable;
	
	private BalanceSumInsuredViewForMediClassicGroup balanceSumInsuredViewForMediClassicGroupInstance;

	
	@Inject
	private Instance<ComprehensiveDeliveryNewBornTable> deliveryNewBornTable;
	
	private ComprehensiveDeliveryNewBornTable deliveryNewBornTableInstance;
	
	private ComprehensiveOutpatientTable outpatientTableInstance;
	
	private ComprehensiveHospitalCashTable hospitalCashTableInstance;
	
	private ComprehensiveHealthCheckTable healthCheckTableInstance;
	
	private ComprehensiveBariatricSurgeryTable bariatricSurgeryTableInstance;
	
	@Inject
	private Instance<ComprehensiveOutpatientTable> outpatientTable;
	
	@Inject
	private Instance<ComprehensiveHospitalCashTable> hospitalCashTable;
	
	@Inject
	private Instance<ComprehensiveHealthCheckTable> healthCheckTable;
	
	@Inject
	private Instance<ComprehensiveBariatricSurgeryTable> bariatricSurgeryTable;
	
	@Inject
	private Instance<LumpSumTable> lumpSumTable;
	
	private LumpSumTable lumpSumTableInstance;
	
	@Inject
	private Instance<BalanceSumInsuredViewForScrcFloater> scrcFloaterTableInstance;
	private BalanceSumInsuredViewForScrcFloater scrcFloaterTable;
	
	
	private TextField txtPolicyNo;
	private TextField txtIntimationNo;
	private TextField txtProductCode;
	private TextField txtProductName;
	private TextField txtnameOfInsured;
	private TextField txtRestoredSI;
	private TextField txtClaimsPaid;
	private TextField txtClaimsOutStanding;
	private TextField txtBalanceRestoredSI;
	private TextField txtCurrentClaimProvision;
	private TextField txtBalance;
	private FormLayout formlayoutBottom;
	
	private Double restoredSumInsured;
	private Double paidAmount;
	private Double claimsOutStanding;
	
	private TextField gmcMainMember;
	private TextField gmcInnerLimitAppl;
	private TextField gmcInnerLimitInsured;
	private TextField gmcInnerLimitUtilised;
	private TextField gmcInnerLimitAval;
	
	private TextField gmcPolicySectionCode;
	
	private TextField scrcPolicyTerm;
	private TextField scrcPeriodOfInsurance;
	
	private Boolean isGmc = false;
	
	private Boolean isSuperSurplus = false;
	
	private String sectionCode;
	
	private Boolean isClassicGroup = Boolean.FALSE;
	
	private Boolean isScrcRev = Boolean.FALSE;
	
	private TextField txtRechargeAmount;
	
	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public BalanceSumInsured() {

	}

//	@PostConstruct
	public void initView() {

		Panel buildMainLayout = buildMainLayout();
		setCompositionRoot(buildMainLayout);

	}

	public VerticalLayout bindFieldGroup(String intimationNo, Long rodKey) {
		
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationNo);
		
		sectionCode = intimation.getPolicy().getSectionCode();
		
		if(intimation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
			isGmc = true;
		}else{
			isGmc = false;
		}
		Product product = intimation.getPolicy().getProduct();
		if(product != null && ReferenceTable.getSuperSurplusKeys().containsKey(product.getKey())
				&& intimation.getPolicy().getPolicyPlan() != null && intimation.getPolicy().getPolicyPlan().equalsIgnoreCase("G")){
			isSuperSurplus = true;
		}else{
			isSuperSurplus = false;
		}


		if(intimation.getPolicy().getProduct() != null && intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.MEDI_CLASSIC_HELATH_GROUP_PRODUCT_KEY)){
			
			isClassicGroup = Boolean.TRUE;
		}
		
		if(intimation.getPolicy().getProduct() != null && (intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_INDIVIDUAL)
				|| intimation.getPolicy().getProduct().getKey().equals(ReferenceTable.SENIOR_CITIZEN_RED_CARPET_REVISED_PRODUCT_FLOATER))) {
			isScrcRev = Boolean.TRUE;
		}
		initView();
		
		Double provitionAmount = (double) 0;
		Claim claim = claimService.getClaimforIntimation(intimation.getKey());
		PolicyMapper mapper = new PolicyMapper();
		PolicyDto policyDto = mapper.getPolicyDto(intimation.getPolicy());

		InsuredMapper insuredMapper = new InsuredMapper();
		InsuredDto insuredDto = insuredMapper.getInsuredDto(intimation
				.getInsured());

		
		
		if (intimation != null && intimation.getInsured() != null) {
			if (intimation.getInsured().getInsuredGender() != null) {
				SelectValue insuredGender = new SelectValue();
				insuredGender.setId(intimation.getInsured().getInsuredGender()
						.getKey());
				insuredGender.setValue(intimation.getInsured()
						.getInsuredGender().getValue());
				insuredDto.setInsuredGender(insuredGender);
			}
			if (intimation.getInsured().getInsuredGender() != null) {
				SelectValue relationshipwithInsuredId = new SelectValue();
				if(intimation.getInsured()
						.getRelationshipwithInsuredId() != null) {
					relationshipwithInsuredId.setId(intimation.getInsured()
							.getRelationshipwithInsuredId().getKey());
					relationshipwithInsuredId.setValue(intimation.getInsured()
							.getRelationshipwithInsuredId().getValue());
					insuredDto
							.setRelationshipwithInsuredId(relationshipwithInsuredId);
				}
				
			}
		}

		BeanItem<PolicyDto> item = new BeanItem<PolicyDto>(policyDto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		if (policyDto != null && insuredDto != null) {

			txtPolicyNo.setValue(policyDto.getPolicyNumber() != null ? policyDto.getPolicyNumber() : "");
			txtPolicyNo.setNullRepresentation("");
			txtIntimationNo.setValue(intimation.getIntimationId() != null ? intimation.getIntimationId() : "");
			txtIntimationNo.setNullRepresentation("");
			txtProductCode.setValue(policyDto.getProduct().getCode() != null ? policyDto.getProduct().getCode() : "");
			txtProductCode.setNullRepresentation("");
			txtProductName.setValue(policyDto.getProduct().getValue() != null ? policyDto.getProduct().getValue() : "");
			txtProductName.setNullRepresentation("");
			BalanceSumInsuredDTO balanceSumInsuredDTO = dbCalculationService
					.getCumulativeBonusRestrictedSIRestoredSi(policyDto
							.getKey(), intimation.getInsured().getKey(), intimation.getKey());

			txtBalanceSIInsuredName
					.setValue(insuredDto.getInsuredName() != null ? insuredDto
							.getInsuredName() : "");
			txtBalanceSIInsuredName.setNullRepresentation("");
			
			gmcMainMember
			.setValue(insuredDto.getInsuredName() != null ? insuredDto
					.getInsuredName() : "");
			gmcMainMember.setNullRepresentation("");
			
			if(intimation.getInsured() != null){
				gmcInnerLimitAppl
				.setValue(intimation.getInsured().getInnerLimit() != null ? intimation.getInsured().getInnerLimit() >0  ? SHAConstants.YES : SHAConstants.No : SHAConstants.No);
				
			}
			
			gmcInnerLimitAppl.setNullRepresentation("");
			
			scrcPolicyTerm.setValue(policyDto.getPolicyTerm() != null ? policyDto.getPolicyTerm().toString() : "0");
			scrcPolicyTerm.setNullRepresentation("");
			
			scrcPeriodOfInsurance.setValue(SHAUtils.formatDate(policyDto.getPolicyFromDate()) + " - "
					+ SHAUtils.formatDate(policyDto.getPolicyToDate()));
			scrcPeriodOfInsurance.setNullRepresentation("");
			
			/*txtnameOfInsured
			.setValue(insuredDto.getInsuredName() != null ? insuredDto
					.getInsuredName() : "");
			txtnameOfInsured.setNullRepresentation("");*/
			
			Double originalSiValue = 0d;
			if(intimation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
				 originalSiValue = dbCalculationService.getInsuredSumInsuredForGMC( policyDto.getKey(),intimation.getInsured().getKey(),intimation.getPolicy().getSectionCode());
			}else{
				originalSiValue = dbCalculationService.getInsuredSumInsured(
						insuredDto.getInsuredId().toString(), policyDto.getKey(),intimation
						.getInsured().getLopFlag());
			}


			txtOriginalSI.setValue(originalSiValue != null ? SHAUtils
					.getIndianFormattedNumber(originalSiValue) : SHAUtils
					.getIndianFormattedNumber(0));
			txtOriginalSI.setNullRepresentation("");

			txtCummulativeBonus.setValue(balanceSumInsuredDTO
					.getCumulativeBonus() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getCumulativeBonus()) : SHAUtils
					.getIndianFormattedNumber(0));
			txtCummulativeBonus.setNullRepresentation("");

			rechargSI
					.setValue(balanceSumInsuredDTO.getRechargedSumInsured() != null ? SHAUtils
							.getIndianFormattedNumber(balanceSumInsuredDTO
									.getRechargedSumInsured()) : SHAUtils
							.getIndianFormattedNumber(0));
			rechargSI.setNullRepresentation("");

			restoreSI
					.setValue(balanceSumInsuredDTO.getRestoredSumInsured() != null ? SHAUtils
							.getIndianFormattedNumber(balanceSumInsuredDTO
									.getRestoredSumInsured()) : SHAUtils
							.getIndianFormattedNumber(0));
			restoreSI.setNullRepresentation("");

			Double rechargeValue = dbCalculationService.getRechargeAmount(policyDto.getKey(), intimation.getInsured().getKey());

			txtRechargeAmount
			.setValue((rechargeValue != null  && !rechargeValue.equals(0.0)) ? SHAUtils
					.getIndianFormattedNumber(rechargeValue) : SHAConstants.NOT_AVAILABLE);
			txtRechargeAmount.setNullRepresentation("");
			
			/*txtRestoredSI
			.setValue(balanceSumInsuredDTO.getRestoredSumInsured() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getRestoredSumInsured()) : SHAUtils
					.getIndianFormattedNumber(0));
			txtRestoredSI.setNullRepresentation("");*/
			
			restoredSumInsured = balanceSumInsuredDTO.getRestoredSumInsured() != null? balanceSumInsuredDTO.getRestoredSumInsured():0;
			
			if(isSuperSurplus){
				Double definedLimitAmt = intimation.getInsured().getDeductibleAmount() != null ? intimation.getInsured().getDeductibleAmount() : 300000d;
				definedLimit.setValue(definedLimitAmt.toString());
			}
			
			if(restoredSumInsured >= 1){
				
				txtClaimsPaid.setVisible(true);
				txtClaimsOutStanding.setVisible(true);
				txtCurrentClaimProvision.setVisible(true);
				/*++++ UI Custom look ++++*/ 
				dummyField6.setVisible(false);
				dummyField7.setVisible(false);
				dummyField8.setVisible(false);
			
			}
			else{
				
				txtClaimsPaid.setVisible(false);
				txtClaimsOutStanding.setVisible(false);
				txtCurrentClaimProvision.setVisible(false);
				/*++++ Custom look  ++++*/
				dummyField6.setVisible(true);
				dummyField7.setVisible(true);
				dummyField8.setVisible(true);
				
			}

			limitOfCover
					.setValue(balanceSumInsuredDTO.getLimitOfCoverage() != null ? SHAUtils
							.getIndianFormattedNumber(balanceSumInsuredDTO
									.getLimitOfCoverage()) : SHAUtils
							.getIndianFormattedNumber(0));
			
			
			Double variableForCalBalanceSumInured1 = originalSiValue
					+ balanceSumInsuredDTO.getCumulativeBonus()
					+ balanceSumInsuredDTO.getRechargedSumInsured()
					+ balanceSumInsuredDTO.getRestoredSumInsured();
//					+ balanceSumInsuredDTO.getLimitOfCoverage();

			balanceSumInsuredDTO = dbCalculationService
					.getClaimsOutstandingAmt(intimation.getInsured().getKey(),
							intimationNo, dbCalculationService
									.getInsuredSumInsured(String
											.valueOf(intimation.getInsured()
													.getInsuredId()),
											intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag()));

			/*previousClaimsPaid.setValue(balanceSumInsuredDTO
					.getPreviousClaimPaid() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getPreviousClaimPaid()) : SHAUtils
					.getIndianFormattedNumber(0));*/

			/*outstandingClaim.setValue(balanceSumInsuredDTO
					.getClaimOutStanding() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getClaimOutStanding()) : SHAUtils
					.getIndianFormattedNumber(0));*/
			
			txtClaimsOutStanding
			.setValue(balanceSumInsuredDTO.getOutstandingAmout() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getOutstandingAmout()) : SHAUtils
					.getIndianFormattedNumber(0));
						
			
			txtClaimsPaid
			.setValue(balanceSumInsuredDTO.getPaidAmout() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getPaidAmout()) : SHAUtils
					.getIndianFormattedNumber(0));
			
			paidAmount =balanceSumInsuredDTO.getPaidAmout() != null ? balanceSumInsuredDTO.getPaidAmout() :0;
			
			claimsOutStanding=balanceSumInsuredDTO.getOutstandingAmout() != null? balanceSumInsuredDTO.getOutstandingAmout() :0;

			Double variableForCalBalanceSumInured2 = balanceSumInsuredDTO
					.getPreviousClaimPaid()
					+ balanceSumInsuredDTO.getClaimOutStanding();

			Double balanceSumInsuredValue = (double) 0L;

			if (variableForCalBalanceSumInured1 > variableForCalBalanceSumInured2) {
				balanceSumInsuredValue = variableForCalBalanceSumInured1
						- variableForCalBalanceSumInured2;
			}

			/*txtBalanceSumInsured.setValue(String.valueOf(SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredValue)));*/
			if (claim != null) {
				/**
				 * Setting provAmt text box read only to false, since we are
				 * assingning a value in next step. Once done, this readonly is
				 * set to true. This is done to primarily to avoid property read
				 * only exception
				 * */
				if (claim.getCurrentProvisionAmount() != null) {
					provitionAmount = claim.getCurrentProvisionAmount();
					// TODO:
				}

				if (rodKey != null && rodKey != 0l) {
					Double provitionalAmt = reimbursementService
							.getReimbursementbyRod(rodKey)
							.getCurrentProvisionAmt();
					provitionAmount = provitionalAmt != null ? provitionalAmt
							: provitionAmount;
				}
			}/*
			 * else { tmpCpuCode = new TmpCPUCode(); if (intimation.getCpuCode()
			 * != null) { tmpCpuCode = intimation.getCpuCode();
			 * tmpCpuCode.getProvisionAmount(); provitionAmount =
			 * tmpCpuCode.getProvisionAmount() != null ? tmpCpuCode
			 * .getProvisionAmount() : 0D; } }
			 */
//
//			txtCurrentProvisionAmt.setValue(SHAUtils
//					.getIndianFormattedNumber(provitionAmount));
			
			/*txtCurrentProvisionAmt.setValue(balanceSumInsuredDTO
					.getProvisionAmout() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getProvisionAmout()) : SHAUtils
					.getIndianFormattedNumber(0));*/
			
			txtCurrentClaimProvision.setValue(balanceSumInsuredDTO
					.getProvisionAmout() != null ? SHAUtils
					.getIndianFormattedNumber(balanceSumInsuredDTO
							.getProvisionAmout()) : SHAUtils
					.getIndianFormattedNumber(0));
			
			Double balanceRestoredSI =(restoredSumInsured)-
					((paidAmount)+(claimsOutStanding));
					
			//txtBalanceRestoredSI.setValue(String.valueOf(balanceRestoredSI >0 ?balanceRestoredSI : 0));
			
			
			Double balance =balanceRestoredSI -(balanceSumInsuredDTO.getProvisionAmout() != null? balanceSumInsuredDTO.getProvisionAmout() :0);
			
			//txtBalance.setValue(String.valueOf(balance > 0 ? balance :0));
			
			
			Double balanceSI1Value = (double) 0L;
			
			
			Double provisionAmount= balanceSumInsuredDTO.getProvisionAmout() != null ? balanceSumInsuredDTO.getProvisionAmout() : 0d;

			if (balanceSumInsuredValue > provitionAmount) {
				balanceSI1Value = balanceSumInsuredValue - provisionAmount;
			}

			if (balanceSI1Value < 0) {
				balanceSI1Value = 0d;
			}
			/*txtSumInsuredAfterProvision.setValue(SHAUtils
					.getIndianFormattedNumber(balanceSI1Value));*/
		}
		
		
		
		
		Map<String, Integer> values = dbCalculationService.getGmcInnerLimit(intimation.getInsured().getKey(), (claim != null ? claim.getKey() :  0));
		
		if(values != null && !values.isEmpty()){
			
				gmcInnerLimitInsured.setValue(values.get(SHAConstants.GMC_INNER_LIMIT_SI) != null ? SHAUtils
						.getIndianFormattedNumber(values.get(SHAConstants.GMC_INNER_LIMIT_SI)) : SHAUtils
						.getIndianFormattedNumber(0));
				
				gmcInnerLimitAval.setValue(values.get(SHAConstants.GMC_INNER_LIMIT_AVAILABLE) != null ? SHAUtils
						.getIndianFormattedNumber(values.get(SHAConstants.GMC_INNER_LIMIT_AVAILABLE)) : SHAUtils
						.getIndianFormattedNumber(0));
				
				gmcInnerLimitUtilised.setValue(values.get(SHAConstants.GMC_INNER_LIMIT_UTILISED_AMT) != null ? SHAUtils
						.getIndianFormattedNumber(values.get(SHAConstants.GMC_INNER_LIMIT_UTILISED_AMT)) : SHAUtils
						.getIndianFormattedNumber(0));
			
		}
		
		String gmcSectionName = null;
		if(intimation.getPolicy().getSectionCode() != null && intimation.getPolicy().getSectionDescription() != null){
			gmcSectionName = intimation.getPolicy().getSectionCode()+" - "+intimation.getPolicy().getSectionDescription();	
		}
		
		gmcPolicySectionCode
		.setValue(gmcSectionName != null ? gmcSectionName : "");
		gmcPolicySectionCode.setNullRepresentation("");
		
		
		setReadOnly(formlayoutleft);
		setReadOnly(formlayoutRight);
		setReadOnly(formlayoutBottom);
		
		buildTableLayout(intimation);
//		mainLayout.addComponent(buildTableLayout);
		mainLayout.setSpacing(true);
		
		
		return mainLayout;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				TextField field = (TextField) c;
				 if(field.getCaption() != null && field.getCaption().equalsIgnoreCase("Product Name")){
                     field.setWidth("100%");
             }else{
                     field.setWidth("440px");
             }
				field.setReadOnly(true);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	private Panel buildMainLayout() {
		// common part: create layout

		BalanceSumInsuredTable BalanceSumInsuredTableInstance = balanceSumInsuredTable
				.get();
		BalanceSumInsuredTableInstance.init("", false, false);
		
		hospitalisationTableInstance = hospitalisationTable.get();
		hospitalisationTableInstance.init("", false, false);
		
		deliveryNewBornTableInstance = deliveryNewBornTable.get();
		deliveryNewBornTableInstance.init("", false, false);
		
		outpatientTableInstance = outpatientTable.get();
		outpatientTableInstance.init("", false, false);
		
		hospitalCashTableInstance = hospitalCashTable.get();
		hospitalCashTableInstance.init("", false, false);
		
		healthCheckTableInstance = healthCheckTable.get();
		healthCheckTableInstance.init("", false, false);
		
		bariatricSurgeryTableInstance = bariatricSurgeryTable.get();
		bariatricSurgeryTableInstance.init("", false, false);
		
		lumpSumTableInstance = lumpSumTable.get();
		lumpSumTableInstance.init("", false, false);
		
		balanceSumInsuredViewForMediClassicGroupInstance = balanceSumInsuredViewForMediClassicGroupTable.get();
		balanceSumInsuredViewForMediClassicGroupInstance.init("", false, false);
		
		scrcFloaterTable = scrcFloaterTableInstance.get();
		scrcFloaterTable.init("", false, false);
		scrcFloaterTable.setVisible(false);
		
		balanceSuminsuredhorizontal = new HorizontalLayout(
				buildFormlayoutleft(), buildFormlayoutRight(),buildFormlayoutBottom());
		balanceSuminsuredhorizontal.setWidth("100.0%");
		balanceSuminsuredhorizontal.setHeight("100.0%");
		
		//balanceSuminsuredhorizontalBottom = new HorizontalLayout(buildFormlayoutBottom());
	
		mainLayout = new VerticalLayout(balanceSuminsuredhorizontal,hospitalisationTableInstance,deliveryNewBornTableInstance,outpatientTableInstance,hospitalCashTableInstance,healthCheckTableInstance,bariatricSurgeryTableInstance,lumpSumTableInstance,balanceSumInsuredViewForMediClassicGroupInstance,scrcFloaterTable);
		
		
		mainLayout.setSpacing(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		
		mainPanel = new Panel();
		mainPanel.setSizeFull();
		mainPanel.setContent(mainLayout);

		return mainPanel;
	}

	private FormLayout buildFormlayoutleft() {
		txtPolicyNo = new TextField("Policy Number");
		txtProductCode = new TextField("Product Code");
		
		txtOriginalSI = new TextField("Original SI");
		txtCummulativeBonus = new TextField("Cumulative Bonus");
		rechargSI = new TextField("Recharged SI");
		//restoreSI = new TextField("Restored SI");
		limitOfCover = new TextField("Limit of Coverage");
		
		gmcPolicySectionCode = new TextField("Policy Section Code");
		
		scrcPolicyTerm = new TextField("Term Of the Policy");
		
		if(isGmc){
			formlayoutleft = new FormLayout(txtPolicyNo, txtProductCode, txtOriginalSI,
					txtCummulativeBonus, rechargSI/*, restoreSI*/, limitOfCover,gmcPolicySectionCode);
		} else if(isScrcRev){
			formlayoutleft = new FormLayout(txtPolicyNo, txtProductCode, scrcPolicyTerm, txtOriginalSI,
					txtCummulativeBonus, rechargSI/*, restoreSI*/, limitOfCover);
		}
		else{
			formlayoutleft = new FormLayout(txtPolicyNo, txtProductCode, txtOriginalSI,
					txtCummulativeBonus, rechargSI/*, restoreSI*/, limitOfCover);
		}
		
		
		
		
		formlayoutleft.addStyleName("layoutDesign");
		formlayoutleft.setWidth("100.0%");
		formlayoutleft.setMargin(false);
		formlayoutleft.setSpacing(true);
//		formlayoutleft.setReadOnly(false);
		return formlayoutleft;
	}

	private FormLayout buildFormlayoutRight() {
		//previousClaimsPaid = new TextField("Previous Claims Paid");
		/*
		 * Removed previous from previous claim paid fld,as per
		 * sathish sir comments as it was dept comments.
		 * */
		txtIntimationNo = new TextField("Intimation Number");
		txtProductName = new TextField("Product Name");
		/*previousClaimsPaid = new TextField("Claims Paid");
		outstandingClaim = new TextField("Claims Outstanding");
		txtBalanceSumInsured = new TextField("Balance SI");
		txtCurrentProvisionAmt = new TextField("Provision for Current Claim");
		txtSumInsuredAfterProvision = new TextField(
				"Balance SI (After Provision)");*/
		
		restoreSI = new TextField("Restored SI");
		txtClaimsPaid = new TextField("Restored SI Paid");
		txtClaimsOutStanding = new TextField("Restored SI Outstanding");
		txtCurrentClaimProvision = new TextField("Provision for Current Claim");
		definedLimit = new TextField("Defined Limit");
		
		scrcPeriodOfInsurance = new TextField("Period Of Insurance");
		
		/*++++ Custom look ++++*/
		dummyField6 = new TextField();
		dummyField6.setEnabled(false);
		dummyField6.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField6.setHeight("100%");
		
		dummyField7 = new TextField();
		dummyField7.setEnabled(false);
		dummyField7.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField7.setHeight("100%");
		
		dummyField8 = new TextField();
		dummyField8.setEnabled(false);
		dummyField8.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField8.setHeight("100%");
		
		if(!isGmc){
			
		   if(isSuperSurplus){
			   formlayoutRight = new FormLayout(txtIntimationNo, txtProductName,restoreSI,definedLimit,txtClaimsPaid,txtClaimsOutStanding,txtCurrentClaimProvision/*, previousClaimsPaid, outstandingClaim,
						txtBalanceSumInsured, txtCurrentProvisionAmt,
						txtSumInsuredAfterProvision*/,dummyField7,dummyField8);
		   } else if(isScrcRev){
			   formlayoutRight = new FormLayout(txtIntimationNo, txtProductName,scrcPeriodOfInsurance,restoreSI,definedLimit,txtClaimsPaid,txtClaimsOutStanding,txtCurrentClaimProvision/*, previousClaimsPaid, outstandingClaim,
						txtBalanceSumInsured, txtCurrentProvisionAmt,
						txtSumInsuredAfterProvision*/,dummyField7,dummyField8);
		   }  else{
			   formlayoutRight = new FormLayout(txtIntimationNo, txtProductName,restoreSI,txtClaimsPaid,txtClaimsOutStanding,txtCurrentClaimProvision/*, previousClaimsPaid, outstandingClaim,
						txtBalanceSumInsured, txtCurrentProvisionAmt,
						txtSumInsuredAfterProvision*/,dummyField6,dummyField7,dummyField8);
		   }
		}
		else
		{
			formlayoutRight = new FormLayout(txtIntimationNo, txtProductName,restoreSI,txtClaimsPaid,txtClaimsOutStanding,txtCurrentClaimProvision/*, previousClaimsPaid, outstandingClaim,
					txtBalanceSumInsured, txtCurrentProvisionAmt,
					txtSumInsuredAfterProvision*/,dummyField6,dummyField7,dummyField8);
		}
		formlayoutRight.addStyleName("layoutDesign");
		formlayoutRight.setWidth("100.0%");
		formlayoutRight.setMargin(false);
		formlayoutRight.setSpacing(true);
//		formlayoutRight.setReadOnly(false);

		return formlayoutRight;
	}
	
	private FormLayout buildFormlayoutBottom() {
		//previousClaimsPaid = new TextField("Previous Claims Paid");
		/*
		 * Removed previous from previous claim paid fld,as per
		 * sathish sir comments as it was dept comments.
		 * */
		/*txtnameOfInsured = new TextField("NAME OF THE INSURED");
		txtRestoredSI = new TextField("RESTORED SI");
		txtClaimsPaid = new TextField("CLAIMS PAID");
		txtClaimsOutStanding = new TextField("CLAIMS OUTSTANDING");
		txtBalanceRestoredSI = new TextField("BALANCE RESTORED SI");
		txtCurrentClaimProvision = new TextField("PROVISION FOR CURRENT CLAIM");
		txtBalance = new TextField("BALANCE (AFTER PROVISION)");*/
		
		txtBalanceSIInsuredName = new TextField("Name of the Insured");
		txtRechargeAmount = new TextField("Recharge Amount");
		
		/*++++ Custom look ++++*/
		TextField dummyField1 = new TextField();
		dummyField1.setEnabled(false);
		dummyField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField1.setHeight("100%");
		
		TextField dummyField2 = new TextField();
		dummyField2.setEnabled(false);
		dummyField2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField2.setHeight("100%");
		
		TextField dummyField3 = new TextField();
		dummyField3.setEnabled(false);
		dummyField3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField3.setHeight("100%");
		
		TextField dummyField4 = new TextField();
		dummyField4.setEnabled(false);
		dummyField4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField4.setHeight("100%");
		
		TextField dummyField5 = new TextField();
		dummyField5.setEnabled(false);
		dummyField5.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummyField5.setHeight("100%");
		
		
		gmcMainMember = new TextField("Main Member Name");
		gmcInnerLimitAppl = new TextField("Inner Limit Applicable");
		gmcInnerLimitInsured = new TextField("Inner Limit for the insured");
		gmcInnerLimitUtilised = new TextField("Inner Limit Utilised");
		gmcInnerLimitAval = new TextField("Inner Limit Available for this Claim");
		
		if(isGmc  && sectionCode != null && sectionCode.equalsIgnoreCase(SHAConstants.GMC_SECTION_D)){
			formlayoutBottom = new FormLayout(/*txtnameOfInsured, txtRestoredSI,
					txtClaimsPaid, txtClaimsOutStanding,
					txtBalanceRestoredSI,txtCurrentClaimProvision,txtBalance*/gmcMainMember,gmcInnerLimitAppl,gmcInnerLimitInsured,gmcInnerLimitUtilised,gmcInnerLimitAval);
		}
		else{
			formlayoutBottom = new FormLayout(/*txtnameOfInsured, txtRestoredSI,
					txtClaimsPaid, txtClaimsOutStanding,
					txtBalanceRestoredSI,txtCurrentClaimProvision,txtBalance*/txtBalanceSIInsuredName,txtRechargeAmount,dummyField1,dummyField2,dummyField3,dummyField4);
				
		}
		
		//formlayoutBottom.setCaption("RESTORED SUM INSURED BALANCE");
		formlayoutBottom.addStyleName("layoutDesign");
		formlayoutBottom.setWidth("100.0%");
		formlayoutBottom.setMargin(false);
		formlayoutBottom.setSpacing(true);

//		formlayoutBottom.setReadOnly(false);

		return formlayoutBottom;
	}
	
private void buildTableLayout(Intimation intimation) {
	
	Map<String, Object> list = null;
	List<ComprehensiveHospitalisationTableDTO> optinalCoversList= null;
	List<ComprehensiveHospitalisationTableDTO> scrcFloaterList = null;
	if(isGmc){
		list = dbCalculationService.getGmcSI(intimation.getInsured().getKey(), intimation.getIntimationId(), 
				dbCalculationService.getInsuredSumInsuredForGMC(intimation.getPolicy().getKey(),intimation.getInsured().getKey(),intimation.getPolicy().getSectionCode())
				,intimation.getPolicy().getProduct().getKey());
	}
	/*commented below code need to discuss with satish sir for scrc product
	else if(isScrcFloater) {
		scrcFloaterList = dbCalculationService.getComprehensiveSIForClassicGroup(intimation.getInsured().getKey(), intimation.getIntimationId(), dbCalculationService.getInsuredSumInsured(String
				.valueOf(intimation.getInsured().getInsuredId()), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag()),intimation.getPolicy().getProduct().getKey());
	}*/ else{
		
		list = dbCalculationService.getComprehensiveSI(intimation.getInsured().getKey(), intimation.getIntimationId(), dbCalculationService.getInsuredSumInsured(String
				.valueOf(intimation.getInsured().getInsuredId()), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag()),intimation.getPolicy().getProduct().getKey());
	}
	
	if(isClassicGroup){
		
		
		optinalCoversList = dbCalculationService.getComprehensiveSIForClassicGroup(intimation.getInsured().getKey(), intimation.getIntimationId(), dbCalculationService.getInsuredSumInsured(String
				.valueOf(intimation.getInsured().getInsuredId()), intimation.getPolicy().getKey(),intimation.getInsured().getLopFlag()),intimation.getPolicy().getProduct().getKey());
	}
		
	
	if(list != null && !list.isEmpty()){

		List<ComprehensiveHospitalisationTableDTO> tableList1 = (List<ComprehensiveHospitalisationTableDTO>)list.get(SHAConstants.SECTION_CODE_1);
			hospitalisationTableInstance.setTableList(tableList1);
			if(tableList1.isEmpty()){
				hospitalisationTableInstance.setVisible(false);
			}else{
				hospitalisationTableInstance.setVisible(true);
			}
		
		List<ComprehensiveDeliveryNewBornTableDTO> tableList2 = (List<ComprehensiveDeliveryNewBornTableDTO>)list.get(SHAConstants.SECTION_CODE_2);
			deliveryNewBornTableInstance.setTableList(tableList2);
			if(tableList2.isEmpty()){
				deliveryNewBornTableInstance.setVisible(false);
			}else{
				deliveryNewBornTableInstance.setVisible(true);
			}
		
		List<ComprehensiveOutpatientTableDTO> tableList3 = (List<ComprehensiveOutpatientTableDTO>)list.get(SHAConstants.SECTION_CODE_3);
			outpatientTableInstance.setTableList(tableList3);
			if(tableList3.isEmpty()){
				outpatientTableInstance.setVisible(false);
			}else{
				outpatientTableInstance.setVisible(true);
			}
		List<ComprehensiveHospitalCashTableDTO> tableList4 = (List<ComprehensiveHospitalCashTableDTO>)list.get(SHAConstants.SECTION_CODE_4);
			hospitalCashTableInstance.setTableList(tableList4);
			if(tableList4.isEmpty()){
				hospitalCashTableInstance.setVisible(false);
			}else{
				hospitalCashTableInstance.setVisible(true);
			}
		List<ComprehensiveHealthCheckTableDTO> tableList5 = (List<ComprehensiveHealthCheckTableDTO>)list.get(SHAConstants.SECTION_CODE_5);
			healthCheckTableInstance.setTableList(tableList5);
			if(tableList5.isEmpty()){
				healthCheckTableInstance.setVisible(false);
			}
			else{
				healthCheckTableInstance.setVisible(true);
			}
		List<ComprehensiveBariatricSurgeryTableDTO> tableList6 = (List<ComprehensiveBariatricSurgeryTableDTO>)list.get(SHAConstants.SECTION_CODE_6);
			bariatricSurgeryTableInstance.setTableList(tableList6);
			if(tableList6.isEmpty()){
				bariatricSurgeryTableInstance.setVisible(false);
			}
			else{
				bariatricSurgeryTableInstance.setVisible(true);
			}
			
		List<LumpSumTableDTO> tableList8 = (List<LumpSumTableDTO>)list.get(SHAConstants.SECTION_CODE_8);
			lumpSumTableInstance.setTableList(tableList8);
			if(tableList8.isEmpty()){
				lumpSumTableInstance.setVisible(false);
			}else{
				lumpSumTableInstance.setVisible(true);
			}
	}
	
	if(optinalCoversList != null && !optinalCoversList.isEmpty()){
		
		balanceSumInsuredViewForMediClassicGroupInstance.setTableList(optinalCoversList);
		if(isClassicGroup){
			balanceSumInsuredViewForMediClassicGroupInstance.setVisible(Boolean.TRUE);
		}else{
			balanceSumInsuredViewForMediClassicGroupInstance.setVisible(Boolean.FALSE);
		}
	}
	
	if(scrcFloaterList != null && !scrcFloaterList.isEmpty()){
		scrcFloaterTable.setTableList(scrcFloaterList);
		if(isScrcRev){
			scrcFloaterTable.setVisible(true);
			hospitalisationTableInstance.setVisible(false);
			deliveryNewBornTableInstance.setVisible(false);
			outpatientTableInstance.setVisible(false);
			hospitalCashTableInstance.setVisible(false);
			healthCheckTableInstance.setVisible(false);
			bariatricSurgeryTableInstance.setVisible(false);
			lumpSumTableInstance.setVisible(false);
			
		} else {
			scrcFloaterTable.setVisible(false);
		}
	}
			
//		mainLayout.addComponent(hospitalisationTableInstance);
//		mainLayout.addComponent(deliveryNewBornTableInstance);
//		mainLayout.addComponent(outpatientTableInstance);
//		mainLayout.addComponent(hospitalisationTableInstance);
//		mainLayout.addComponent(hospitalisationTableInstance);
//		mainLayout.addComponent(hospitalisationTableInstance);
		
//		VerticalLayout vertical = new VerticalLayout(hospitalisationTableInstance,deliveryNewBornTableInstance,outpatientTableInstance,hospitalCashTableInstance,healthCheckTableInstance,bariatricSurgeryTableInstance);
//		vertical.setWidth("100%");
//		vertical.setHeight("100%");
//		vertical.setSpacing(true);
			
			
		
		

//		return vertical;
	}
public void setClearReferenceData(){
//	SHAUtils.setClearReferenceData(referenceData);
	if(mainLayout != null){
		mainLayout.removeAllComponents();
	}
//	this.diagnosisDetailsTableObj.clearObject();
//	this.balanceSumInsuredTable = null;
	if(hospitalisationTableInstance!=null){
		this.hospitalisationTableInstance.removeRow();
		this.hospitalisationTableInstance = null;
	}
	if(this.balanceSumInsuredViewForMediClassicGroupInstance != null){
		this.balanceSumInsuredViewForMediClassicGroupInstance.removeRow();
		this.balanceSumInsuredViewForMediClassicGroupInstance = null;
	}
	if(this.deliveryNewBornTableInstance!=null){
		this.deliveryNewBornTableInstance.removeRow();
		this.deliveryNewBornTableInstance = null;
	}
	if(this.outpatientTableInstance!=null){
		this.outpatientTableInstance.removeRow();
		this.outpatientTableInstance = null;
	}
	if(this.hospitalCashTableInstance!=null){
		this.hospitalCashTableInstance.removeRow();
		this.hospitalCashTableInstance = null;
	}
	if(this.healthCheckTableInstance!=null){
		this.healthCheckTableInstance.removeRow();
		this.healthCheckTableInstance = null;
	}
	if(this.bariatricSurgeryTableInstance!=null){
		this.bariatricSurgeryTableInstance.removeRow();
		this.bariatricSurgeryTableInstance = null;
	}
	if(this.lumpSumTableInstance!=null){
		this.lumpSumTableInstance.removeRow();
		this.lumpSumTableInstance = null;
	}
	if(this.scrcFloaterTable!=null){
		this.scrcFloaterTable.removeRow();
		this.scrcFloaterTable = null;
	}
	
}

}
