package com.shaic.claim.productbenefit.view;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PolicySource;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.restservices.bancs.GLXWBService;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


public class ViewProductBenefits extends Window {

	private static final long serialVersionUID = 100354609155493794L;

	private VerticalLayout mainLayout;
	FormLayout formLayoutLeft;
	FormLayout formLayoutRight;
	@Inject
	private Instance<ViewProductConditions> productCondition;

	@Inject
	private Instance<ViewDayCareBenefits> viewDayCareBenefits;

	@EJB
	private MasterService masterService;
	@EJB
	private PolicyService policyService;
	@EJB
	private IntimationService intimationService;
	@EJB
	private PremiaPullService premiaPullService;
	@Inject
	private GLXWBService glxWBService;
	
	@Inject
	private Instance<ViewProductBenefitImpl> viewProductBenefitImplInstance;
	
//	private TmpPolicy policy;
	
	private Policy policyDetails;
	
	private Product product;
	
	private PremPolicyDetails premPolicyDetails;
	
	private PremPolicy premPolicy;

	Intimation intimation;
	String productCode;
	String productName;
	private Button btnClose;
	
	
	@Inject
	private Instance<ViewProcedureDetails> viewProcedureDetails;

	@PostConstruct
	public void initView() {

		buildMainLayout();
		setContent(mainLayout);
		setCaption("Product Details");
		this.setHeight("400px");
//		this.setWidth("750px");
		this.setWidth("80%");
		this.setStyleName("topalignment");
		this.addStyleName("topalignment");
		btnClose = new Button("Close");
		btnClose.setStyleName(ValoTheme.BUTTON_DANGER);
		btnClose.setData(this);
		listener();
		setModal(true);
		setClosable(true);
		setResizable(true);

	}

	public ViewProductBenefits() {

		// TODO add user code here
	}

	@SuppressWarnings("deprecation")
	private VerticalLayout buildMainLayout() {
		formLayoutLeft = new FormLayout();
		formLayoutRight = new FormLayout();
		/***
		 * POLICY CLAUSE button added for GLX2020058
		 */
		Button policyClauseButton=new Button("Policy Clause");
		policyClauseButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		policyClauseButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				getViewPolicyClause(policyDetails.getPolicyNumber());
				
			}
		});

		FormLayout policyClausefl=new FormLayout();
		policyClausefl.addComponent(policyClauseButton);
		
		HorizontalLayout headerInfo = new HorizontalLayout(formLayoutRight,
				formLayoutLeft,policyClausefl);
		/*headerInfo.setWidth("100.0%");
		headerInfo.setHeight("100px");*/
		headerInfo.setMargin(true);
		headerInfo.setSpacing(true);

		mainLayout = new VerticalLayout(headerInfo);
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setMargin(false);
		//mainLayout.setSpacing(true);
		return mainLayout;
	}



	public void showValues(String policyNumber) {		
		bindProduct(policyNumber);
		
		product = masterService.getProductByProductId(policyDetails.getProduct().getKey());
		
//		product = masterService.getProductByProductCode(policyDetails
//				.getProduct().getCode());
		
		

		TabSheet productDetails = bindProductDetailsFromPolicyTab(policyNumber);
		mainLayout.addComponent(productDetails);
		
		mainLayout.addComponent(btnClose);
		mainLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
	}
	public void showValues(PremPolicyDetails policyNumber) {		
		bindProduct(policyNumber);
		product = masterService.getProductByProductCode(policyNumber
				.getProductCode());
		String premiaPolicyNumber = policyNumber.getPolicyNo();
		TabSheet productDetails = bindProductDetailsTabForPremia(premiaPolicyNumber);
		mainLayout.addComponent(productDetails);
		
		mainLayout.addComponent(btnClose);
		mainLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
	}
	
	public void showValues(PremPolicy policyNumber) {		
		bindProduct(policyNumber);
		
		//Bancs Changes Start
		if (policyNumber.getPolicySource() != null && policyNumber.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
			product = glxWBService.getProductByProductCode(policyNumber.getProductCode());
		}else{
			product = masterService.getProductByProductCode(policyNumber.getProductCode());
		}
		String premPolicyNumber = policyNumber.getPolicyNo();
		TabSheet productDetails = bindProductDetailsTabForSearchPolicy(premPolicyNumber);
		mainLayout.addComponent(productDetails);
		
		mainLayout.addComponent(btnClose);
		mainLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
	}
	
	
//	public void showValues(TmpPolicy tmpPolicy) {
//		
//		policy = tmpPolicy;
//		
//		bindProduct(tmpPolicy);
//
//		TabSheet productDetails = bindProductDetailsTab();
//
//		mainLayout.addComponent(productDetails);
//		mainLayout.addComponent(btnClose);
//		mainLayout.setComponentAlignment(btnClose, Alignment.BOTTOM_CENTER);
//		
//	}

	private void bindProduct(String policyNumber) {
//		intimation = intimationService.searchbyIntimationNo(intimationNumber);
//		policy = policyService.getTmpPolicy(policyNumber);
//		product=masterService.getProductByProductCode(policy.getPolProductCode());
		
		policyDetails = policyService.getPolicy(policyNumber);
		product=masterService.getProductByProductCode(policyDetails.getProduct().getCode());
	
		TextField productCodeTxt = new TextField();
		productCodeTxt.setCaption("Product Code");
		//productCodeTxt.setWidth("250px");
		productCodeTxt.setNullRepresentation("");
		productCodeTxt.setValue(product.getCode());
		productCodeTxt.setReadOnly(true);

	

		TextField productNameTxt = new TextField();
		productNameTxt.setCaption("Product Name");
		//productNameTxt.setWidth("250px");
		productNameTxt.setNullRepresentation("");
		productNameTxt.setValue(product.getValue());
		productNameTxt.setReadOnly(true);
		formLayoutLeft.addComponent(productCodeTxt);
		formLayoutRight.addComponent(productNameTxt);
	}
	private void bindProduct(PremPolicyDetails policyNumber) {
//		intimation = intimationService.searchbyIntimationNo(intimationNumber);
		//policy = policyService.getTmpPolicy(policyNumber);
		product=masterService.getProductByProductCode(policyNumber.getProductCode());
		premPolicyDetails = policyNumber;
		TextField productCodeTxt = new TextField();
		productCodeTxt.setCaption("Product Code");
		productCodeTxt.setWidth("250px");
		productCodeTxt.setNullRepresentation("");
		productCodeTxt.setValue(product.getCode());
		productCodeTxt.setReadOnly(true);

	

		TextField productNameTxt = new TextField();
		productNameTxt.setCaption("Product Name");
		productNameTxt.setWidth("250px");
		productNameTxt.setNullRepresentation("");
		productNameTxt.setValue(product.getValue());
		productNameTxt.setReadOnly(true);
		formLayoutLeft.addComponent(productCodeTxt);
		formLayoutRight.addComponent(productNameTxt);
	}
	
	private void bindProduct(PremPolicy policyNumber) {
//		intimation = intimationService.searchbyIntimationNo(intimationNumber);
		//policy = policyService.getTmpPolicy(policyNumber);
		
		//Bancs Changes Start
		if (policyNumber != null) {
			if (policyNumber.getPolicySource() != null && policyNumber.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
				product = glxWBService.getProductByProductCode(policyNumber.getProductCode());
			}else{
				product = masterService.getProductByProductCode(policyNumber.getProductCode());
			}
		}
				
		//product=masterService.getProductByProductCode(policyNumber.getProductCode());
	   //Banc changes end
		premPolicy = policyNumber;
		TextField productCodeTxt = new TextField();
		productCodeTxt.setCaption("Product Code");
		productCodeTxt.setWidth("250px");
		productCodeTxt.setNullRepresentation("");
		productCodeTxt.setValue(product.getCode());
		productCodeTxt.setReadOnly(true);

	

		TextField productNameTxt = new TextField();
		productNameTxt.setCaption("Product Name");
		productNameTxt.setWidth("250px");
		productNameTxt.setNullRepresentation("");
		productNameTxt.setValue(product.getValue());
		productNameTxt.setReadOnly(true);
		formLayoutLeft.addComponent(productCodeTxt);
		formLayoutRight.addComponent(productNameTxt);
	}
	
//	private void bindProduct(TmpPolicy tmpPolicy) {
////		intimation = intimationService.searchbyIntimationNo(intimationNumber);
////		policy = policyService.getPolicy(tmpPolicy.getPolNo());
////		if(null != policy)
////		{
////			showProductBenefitViewForPolicy(policy);
////		}
////		else
////		{
//			ViewProductBenefitImpl viewProductBenefits = viewProductBenefitImplInstance.get();
////			viewProductBenefits.invokeMenuPresenter(tmpPolicy);
////			policy = policyService.getPolicy(tmpPolicy.getPolNo());
//			showProductBenefitViewForPolicy(tmpPolicy.getPolProductCode());
////		}
//		
//	}
	
	private void showProductBenefitViewForPolicy(String productCode)
	{
		product=masterService.getProductByProductCode(productCode);
		
		TextField productCodeTxt = new TextField();
		productCodeTxt.setCaption("Product Code");
		productCodeTxt.setWidth("250px");
		productCodeTxt.setNullRepresentation("");
		productCodeTxt.setValue(product.getCode());
		productCodeTxt.setReadOnly(true);

	

		TextField productNameTxt = new TextField();
		productNameTxt.setCaption("Product Name");
		productNameTxt.setWidth("250px");
		productNameTxt.setNullRepresentation("");
		productNameTxt.setValue(product.getValue());
		productNameTxt.setReadOnly(true);
		formLayoutLeft.addComponent(productCodeTxt);
		formLayoutRight.addComponent(productNameTxt);
	}
	

	private TabSheet bindProductDetailsTab(String policyNumber) {
		TabSheet productDetails = new TabSheet();
		//Vaadin8-setImmediate() productDetails.setImmediate(true);
		productDetails.setWidth("100.0%");
		productDetails.setHeight("100.0%");

		ViewProductConditions a_productCondition = productCondition.get();
//		a_productCondition.showValues(policy);
		//a_productCondition.showValues(product);

		ViewDayCareBenefits a_viewDayCareBenefits = viewDayCareBenefits.get();
		a_viewDayCareBenefits.showValues(product);
		
		ViewProcedureDetails a_viewProcedureDetails = viewProcedureDetails.get();
		a_viewProcedureDetails.showValues(product,policyNumber);

		productDetails.addTab(a_productCondition, "Product Conditions");
		productDetails.addTab(a_viewDayCareBenefits, "Day care Benefits");
		productDetails.addTab(a_viewProcedureDetails, "Procedure Details");
		return productDetails;
	}
	
	private TabSheet bindProductDetailsFromPolicyTab(String policyNumber) {
		TabSheet productDetails = new TabSheet();
		//Vaadin8-setImmediate() productDetails.setImmediate(true);
		productDetails.setWidth("100.0%");
		productDetails.setHeight("100.0%");

		ViewProductConditions a_productCondition = productCondition.get();
		//a_productCondition.showValues(policyDetails);
		a_productCondition.showValues(product,policyNumber);

		ViewDayCareBenefits a_viewDayCareBenefits = viewDayCareBenefits.get();
		a_viewDayCareBenefits.showValues(product);
		
		ViewProcedureDetails a_viewProcedureDetails = viewProcedureDetails.get();
		a_viewProcedureDetails.showValues(product,policyNumber);

		productDetails.addTab(a_productCondition, "Product Conditions");
		productDetails.addTab(a_viewDayCareBenefits, "Day care Benefits");
		productDetails.addTab(a_viewProcedureDetails, "Procedure Details");
		return productDetails;
	}
	private TabSheet bindProductDetailsTabForSearchPolicy(String premPolicyNumber) {
		TabSheet productDetails = new TabSheet();
		//Vaadin8-setImmediate() productDetails.setImmediate(true);
		productDetails.setWidth("100.0%");
		productDetails.setHeight("100.0%");

		ViewProductConditions a_productCondition = productCondition.get();
		a_productCondition.showValues(product,premPolicyNumber);
		// a_productCondition.showValues(premPolicy); comment for bancs view


		ViewDayCareBenefits a_viewDayCareBenefits = viewDayCareBenefits.get();
		a_viewDayCareBenefits.showValues(product);
		
		ViewProcedureDetails a_viewProcedureDetails = viewProcedureDetails.get();
		a_viewProcedureDetails.showValues(product,premPolicyNumber);

		productDetails.addTab(a_productCondition, "Product Conditions");
		productDetails.addTab(a_viewDayCareBenefits, "Day care Benefits");
		productDetails.addTab(a_viewProcedureDetails, "Procedure Details");
		return productDetails;
	}
	
	private TabSheet bindProductDetailsTabForPremia(String policyNumber) {
		TabSheet productDetails = new TabSheet();
		//Vaadin8-setImmediate() productDetails.setImmediate(true);
		productDetails.setWidth("100.0%");
		productDetails.setHeight("100.0%");

		ViewProductConditions a_productCondition = productCondition.get();
		a_productCondition.showValues(premPolicyDetails);

		ViewDayCareBenefits a_viewDayCareBenefits = viewDayCareBenefits.get();
		a_viewDayCareBenefits.showValues(product);
		
		ViewProcedureDetails a_viewProcedureDetails = viewProcedureDetails.get();
		a_viewProcedureDetails.showValues(product,policyNumber);

		productDetails.addTab(a_productCondition, "Product Conditions");
		productDetails.addTab(a_viewDayCareBenefits, "Day care Benefits");
		productDetails.addTab(a_viewProcedureDetails, "Procedure Details");
		return productDetails;
	}
	
	private void listener(){
		btnClose.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Object w =  btnClose.getData();
				((Window) w).close();
				
			}
		});
		
	}
	
	protected void getViewPolicyClause(String policyNumber) {

		if (policyNumber != null){			
			//Added for version
			DBCalculationService dbCalculationService = new DBCalculationService();
			Map<String, Object> getproductUINvalues = dbCalculationService.getUINVersionNumberForrejectionCategory(policyDetails.getKey(),
					policyDetails.getPolicyNumber(),0l,0l);
			Long versionNumber =1l;
			if(getproductUINvalues != null){
				if(getproductUINvalues.containsKey("productversionNumber")){
					versionNumber = ((Long) getproductUINvalues.get("productversionNumber")); 
				}
			}

			System.out.println(String.format("Version Number [%s]", versionNumber));

			if(versionNumber !=null && versionNumber ==1L && policyDetails != null && policyDetails.getProduct().getDocumentToken() != null){
				String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyDetails.getProduct().getDocumentToken()));
				if(docViewURL!=null){
					getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
				}
			}
			else if(versionNumber!=null && versionNumber ==2L && policyDetails != null && policyDetails.getProduct().getDocumentToken2() != null){

				String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyDetails.getProduct().getDocumentToken2()));
				if(docViewURL!=null){
					getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
				}
			}
			else if(versionNumber!=null && versionNumber ==3L && policyDetails != null && policyDetails.getProduct().getDocumentToken3() != null){

				String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyDetails.getProduct().getDocumentToken3()));
				if(docViewURL!=null){
					getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
				}
			}
			else if(versionNumber!=null && versionNumber ==4L && policyDetails != null && policyDetails.getProduct().getDocumentToken4() != null){

				String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyDetails.getProduct().getDocumentToken4()));
				if(docViewURL!=null){
					getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
				}
			}
			else if(versionNumber!=null && versionNumber ==3L){

				if (policyDetails != null && policyDetails.getProduct().getDocumentToken3() != null) {
					String docViewURL = SHAFileUtils.viewFileByTokenService(String.valueOf(policyDetails.getProduct().getDocumentToken3()));
					if(docViewURL!=null){
						getUI().getPage().open(docViewURL+"#toolbar=0", "_blank",1200,780,BorderStyle.NONE);
					}
				}
			}
			else{
				HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
				buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
				GalaxyAlertBox.createErrorBox("Policy Clause Not Available", buttonsNamewithType);
			}
		}



	}

}
