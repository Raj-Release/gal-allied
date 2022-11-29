package com.shaic.claim.previousclaim;

//import javax.enterprise.inject.Instance;
//import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.policy.search.ui.opsearch.ProcessOPRequestService;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.domain.outpatient.OPDocumentList;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
//import com.google.inject.Inject;
import com.vaadin.v7.ui.ComboBox;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.VerticalLayout;


public class PreviousClaimViewPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6698063331930098762L;
	
	private ComboBox policyYear;
	
	private OptionGroup optionSelect;
	
	private OptionGroup viewType;
	
	private VerticalLayout mainLayout;
	
	//private VLayout claimlayout;
	
	private final String POLICY_WISE="Policy Wise";
	
	private final String INSURED_WISE="Insured Wise";
	
	
	@Inject
	private ViewPreviousClaimDataTable viewPreviousClaimDataTable;
	
	@Inject
	private ViewPreviousClaimInsuredDataTable viewPreviousClaimInsuredDataTable;
	
	@Inject
	private PreviousClaimsTableDTO previousClaimTableDTO;
	
	@Inject 
	private Claim claim;
	
	@Inject
	private OPDocumentList opDocumentList;
	
	@Inject 
	private OPHealthCheckup opHealthCheckup;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ProcessOPRequestService processOPRequestService;

	@EJB
	private InsuredService insuredService;
	
	private String intimationNo;
	
	private ComboBox cbox;
	
	private Long claimkey;
	
	private Long healthCheckupKey;
	
	private String claimAmount=null;
	

//	public void init(List<PreviousClaimsTableDTO> previousClaimTableDTO)
//	{
//		mainLayout=new VerticalLayout();
//		viewPreviousClaimDataTable.init("",false,false);
//		viewPreviousClaimDataTable.setTableList(previousClaimTableDTO);
//		setHeight(550, Unit.PIXELS);
//		verticalLayout =new VerticalLayout();
//		optionSelect=new OptionGroup("Claims");
//		optionSelect.addItem("POLICY_WISE");
//		optionSelect.addItem("INSURED_WISE");
//		
//		NativeButton viewButton=new NativeButton();
//		viewButton.setCaption("VIEW");
//		
//		
//		horizontalLayout=new HorizontalLayout();
//		horizontalLayout.addComponent(optionSelect);
//		
//		horizontalLayout.setSpacing(true);
//		
//		
//		policyYear=new ComboBox();
//		policyYear.setCaption("Policy Year");
//		
//	
//		mainLayout.addComponent(horizontalLayout);
//		mainLayout.addComponent(viewButton);
//		mainLayout.addComponent(policyYear);
		
		//setCompositionRoot(mainLayout);
	//}
	public void showValuesForOP(Long policyKey,String intimationNo){
		
		mainLayout = getPrivousClaimsLayoutForOP(policyKey);
		this.intimationNo = intimationNo;
		
		this.setContent(mainLayout);
		
		setCompositionRoot(mainLayout);
		
//		getClaimByInsuredWiseForOP(intimationNo);	
		getClaimByPolicyWiseForOP(policyKey);
		
		
	}
	

	private void setContent(VerticalLayout mainLayout2) {
	// TODO Auto-generated method stub
	
}


	public VerticalLayout getPrivousClaimsLayoutForOP(Long policyKey) {
		HorizontalLayout optionLayout = buildViewTypeForOp(policyKey);
		
		optionLayout.setWidth("100%");
		optionLayout.setSpacing(true);
		optionLayout.setMargin(true);
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(false);
		mainLayout.addComponent(optionLayout);
		
		return mainLayout;
		
	}
	
	public HorizontalLayout buildViewTypeForOp(Long policyKey) {
		
		final Long policyId = policyKey;
       
		
		Policy policy = policyService.getPolicyByKey(policyKey);
		
		
		
		Intimation intimation=intimationService.getIntimationByNo(intimationNo);
		
		
		final OptionGroup viewType = getOptionGroup(policy,intimation);
		
		viewType.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {
						
						viewType.setValue(POLICY_WISE);
						
					} else if (viewType.getValue().equals(INSURED_WISE)) {
						
						viewType.setValue(INSURED_WISE);
						
					} 
				}
			
			}

			
		});
		NativeButton viewBtn = getViewButtonForOP(policyKey, viewType);
		viewBtn.setCaption("View");
		HorizontalLayout optionLayout = new HorizontalLayout();
		optionLayout.addComponent(viewType);
		optionLayout.addComponent(viewBtn);
		return optionLayout;
	}
		public OptionGroup getOptionGroup(Policy policy,Intimation intimation) {
		 viewType = new OptionGroup();
//			//Vaadin8-setImmediate() //Vaadin8-setImmediate() viewType.setImmediate(false);

			//Vaadin8-setImmediate() viewType.setImmediate(false);

			viewType.addItem(POLICY_WISE);
			viewType.addItem(INSURED_WISE);
			
			viewType.setStyleName("inlineStyle");

			viewType.setValue(POLICY_WISE);
			//viewType.setValue(INSURED_WISE);
			return viewType;
		}
	
//		public ComboBox getComboBox()
//		{
//			ComboBox cbox=new ComboBox();
//			cbox.setCaption("Policy Year");
//			mainLayout.addComponent(cbox);
//			return cbox;
//		}
		
		
		
		private NativeButton getViewButtonForOP(final Long policyKey,
				final OptionGroup viewType) {
			 final NativeButton viewBtn = new NativeButton();
			viewBtn.setCaption("View");
			viewBtn.setData(viewType);
		
			viewBtn.addClickListener(new Button.ClickListener() {

				private static final long serialVersionUID = -676865664871344469L;

				@Override
				public void buttonClick(ClickEvent event) {

					if (viewType.getValue() != null) {
						if (viewType.getValue().equals(POLICY_WISE)) {
							mainLayout.removeAllComponents();
							mainLayout.addComponent(viewType);
							mainLayout.setSpacing(true);
							mainLayout.setComponentAlignment(viewType, Alignment.MIDDLE_LEFT);
							mainLayout.addComponent(viewBtn);
							mainLayout.setComponentAlignment(viewBtn, Alignment.MIDDLE_CENTER);
							//Vaadin8-setImmediate() //Vaadin8-setImmediate() mainLayout.setImmediate(false);
							//Vaadin8-setImmediate() mainLayout.setImmediate(false);
							
							getClaimByPolicyWiseForOP(policyKey);
							
							
						} else if (viewType.getValue().equals(INSURED_WISE)) {
							
							mainLayout.removeAllComponents();
							mainLayout.addComponent(viewType);
							mainLayout.setSpacing(true);
						
							mainLayout.addComponent(viewBtn);
							mainLayout.setComponentAlignment(viewBtn, Alignment.MIDDLE_CENTER);
							//Vaadin8-setImmediate() //Vaadin8-setImmediate() mainLayout.setImmediate(false);
							//Vaadin8-setImmediate() mainLayout.setImmediate(false);
							 getClaimByInsuredWiseForOP(intimationNo);
							
						} 
					}

				}

			});
			return viewBtn;
		}
		private void getClaimByPolicyWiseForOP(Long policyKey){
			Policy policy = policyService.getPolicyByKey(policyKey);
			List<Claim> claimsByPolicyNumber = claimService
					.getClaimsByPolicyNumber(policy.getPolicyNumber());
			
			Long policyYear = null;
			if(policy.getPolicyYear()!=null)
			{
				policyYear=policy.getPolicyYear().longValue();
			}
			
			//List<Claim> claimType=claimService.getClaimByIntimation(policyKey);
			List<Claim> claimListOP=new ArrayList<Claim>();
			for(int i=1;i<claimsByPolicyNumber.size();i++)
			{
				Claim claim2 = claimsByPolicyNumber.get(i);
				
				
				if(claim2.getIntimation().getClaimType() != null && claim2.getIntimation().getClaimType().getKey().equals(352))
				{
					claimListOP.add(claim2);
					
				}
			}
			
			//List<PreviousClaimsTableDTO> claimList = preauthService.getPreviousClaimForOP(claimsByPolicyNumber);
			
			mainLayout.setSpacing(true);
			cbox =new ComboBox();
			cbox.addItem("All");
			cbox.addItem("2011-2012");
			cbox.addItem("2012-2013");
			cbox.addItem("2013-2014");
			cbox.addItem("2014-2015");
			cbox.setCaption("Policy Year");
			//mainLayout.removeComponent(cbox);
			mainLayout.addComponent(cbox);
			
			
			
			claimkey=claimService.getClaimsByIntimationNumber(intimationNo).getKey();
			OPHealthCheckup opHealthCheckup=processOPRequestService.getOpHealthByClaimKey(claimkey);
			List<OPDocumentList> opDocumentList =processOPRequestService.getOPDocumentListByClaimKey(claimkey);
			Double amountPayable = 0d;
			if(opHealthCheckup.getAmountPayable()!=null){
				amountPayable = opHealthCheckup.getAmountPayable().doubleValue();
			}
			
			
//			Long opHealthCheckUpKey = opHealthCheckup.getKey();
//			System.out.println("OPKey"+opHealthCheckUpKey);
			
			
			//Long claimAmount=opDocumentBillEntry.getClaimedAmount();
			healthCheckupKey=opHealthCheckup.getKey();
			OPDocumentBillEntry opDocumentBillEntry = processOPRequestService.getOpBillEntryByOPHealthKey(healthCheckupKey);
			
			if(opDocumentBillEntry.getClaimedAmount() != null)
			{
				claimAmount = opDocumentBillEntry.getClaimedAmount();
			}
			
			List<PreviousClaimsTableDTO> claimList = preauthService.getPreviousClaimForOP(claimsByPolicyNumber);
			for(PreviousClaimsTableDTO claimPolicyList:claimList)
			{
				claimPolicyList.setAmountPayable(amountPayable);
				claimPolicyList.setPolicyYear(policyYear);
				
			}
//			for(PreviousClaimsTableDTO claimPolicyList2:claimList)
//			{
//								
//				claimPolicyList2.setPolicyYear(policyYear);
//
//			}
//			
			viewPreviousClaimDataTable.init(POLICY_WISE, false, false);
			viewPreviousClaimDataTable.setTableList(claimList);
			//Vaadin8-setImmediate() //Vaadin8-setImmediate() mainLayout.setImmediate(false);
			//Vaadin8-setImmediate() mainLayout.setImmediate(false);
			mainLayout.addComponent(viewPreviousClaimDataTable);
		}
		
		
	
		private void getClaimByInsuredWiseForOP(String intimationNo){
			Intimation intimation=intimationService.getIntimationByNo(intimationNo);
			List<Claim> claimsByIntimationKey =claimService.getClaimsByPolicyNumber(intimation.getPolicyNumber());
			
			claimkey=claimService.getClaimsByIntimationNumber(intimationNo).getKey();
			OPHealthCheckup opHealthCheckup=processOPRequestService.getOpHealthByClaimKey(claimkey);
			List<OPDocumentList> opDocumentList =processOPRequestService.getOPDocumentListByClaimKey(claimkey);
			Double amountPayable = 0d;
			if(opHealthCheckup.getAmountPayable()!=null){
				amountPayable = opHealthCheckup.getAmountPayable().doubleValue();
			}
			
		
//			
//			PreviousClaimsTableDTO previousClaimsTableDTO=new PreviousClaimsTableDTO();
//			previousClaimsTableDTO.setAmountPayable(amountPayable);
			
//			Long opHealthCheckUpKey = opHealthCheckup.getKey();
//			System.out.println("OPKey"+opHealthCheckUpKey);
//			
			
			//Long claimAmount=opDocumentBillEntry.getClaimedAmount();
			healthCheckupKey=opHealthCheckup.getKey();
			OPDocumentBillEntry opDocumentBillEntry = processOPRequestService.getOpBillEntryByOPHealthKey(healthCheckupKey);
			
			if(opDocumentBillEntry.getClaimedAmount()!=null)
			{
				claimAmount =  opDocumentBillEntry.getClaimedAmount();
			}
			List<PreviousClaimsTableDTO> claimList=preauthService.getPreviousClaimForOP(claimsByIntimationKey);
			for(PreviousClaimsTableDTO previousClaims:claimList){
				previousClaims.setAmountPayable(amountPayable);
				previousClaims.setClaimAmount(claimAmount);
			}
			
//			for(PreviousClaimsTableDTO previousClaims1:claimList){
//				previousClaims1.setClaimedAmount(claimedAmount);
//			}
//			
			mainLayout.setSpacing(true);
			viewPreviousClaimInsuredDataTable.init(INSURED_WISE, false, false);
			viewPreviousClaimInsuredDataTable.setTableList(claimList);
			//Vaadin8-setImmediate() //Vaadin8-setImmediate() mainLayout.setImmediate(false);
			//Vaadin8-setImmediate() mainLayout.setImmediate(false);
			mainLayout.addComponent(viewPreviousClaimInsuredDataTable);
			
		
		}
		
		
}

