package com.shaic.claim.policy.search.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.hibernate.type.descriptor.java.UUIDTypeDescriptor.ToBytesTransformer;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashLessTableMapper;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.ViewPreviousIntimation;
import com.shaic.claim.intimation.ViewPreviousIntimationService;
import com.shaic.claim.intimation.ViewPreviousIntimationTable;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.shaic.claim.viewEarlierRodDetails.ViewProductBenefitsTable;
import com.shaic.claim.viewEarlierRodDetails.ViewProductBenefitsTableDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PolicySource;
import com.shaic.domain.Product;
import com.shaic.domain.StageIntimation;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.restservices.bancs.GLXWBService;
import com.shaic.restservices.bancs.claimprovision.ClaimProvisionService;
import com.shaic.starfax.simulation.PremiaPullService;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
@SuppressWarnings("serial")
public class SearchPolicyUI extends ViewComponent {
	private static final long serialVersionUID = 4174091244258792154L;
	
	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;
	
	@EJB
	private PolicyService policyService;
	@EJB
	private MasterService masterService;
	@EJB
	private ViewPreviousIntimationService previousIntimation;
	@EJB
	private ClaimService claimService;
	@EJB
	private HospitalService hospitalService;
	@EJB
	private PremiaPullService premiaPullService;
	@Inject
	private GLXWBService glxWBService;
	
	@Inject
	private CashlessTable cashlessTable;
	@Inject
	private CashLessTableDetails cashLessTableDetails;
	@Inject
	private CashLessTableMapper cashLessTableMapper;
	@Inject
	private NewIntimationService newIntimationService;
	@Inject
	private IntimationService intimationService;
	@Inject
	private ViewPreviousIntimationTable viewPreviousIntimationTable;
	@Inject
	private Instance<ViewProductBenefits> viewProductBenefitInstance;
	@Inject 
	private ViewProductBenefitsTable viewProductBenefitsTable;

	
	
	private VerticalLayout mainVLayout;
	private Panel topPanel;
	private VerticalLayout topVLayout;
	private HorizontalLayout btnHLayout;
	private HorizontalLayout formHLayout;
	private VerticalLayout tableVLayout;
	private HorizontalLayout spaceHLayout;
	
	private TextField policyNumberTF;
	private TextField healthCardNoTF;
	private Button submitBtn;
	private Button resetBtn;
	private Table table;
	
	private HashMap<String, String> enteredValues = new HashMap<String, String>();
	private String emptyValue = "";
	
	@PostConstruct
	public void init() {
		buildTopPanel();
		buildSpaceLayout();
		buildBottomLayout();
		
		mainVLayout = new VerticalLayout();
		mainVLayout.setMargin(false);
		mainVLayout.setSpacing(false);
		mainVLayout.addComponent(topPanel);
		mainVLayout.addComponent(spaceHLayout);
		mainVLayout.addComponent(tableVLayout);
		mainVLayout.setComponentAlignment(topPanel, Alignment.TOP_LEFT);
		mainVLayout.setComponentAlignment(tableVLayout, Alignment.TOP_LEFT);
		setCompositionRoot(mainVLayout);
	}
	
	private void buildTopPanel() {
		// Policy Number TextField
		policyNumberTF = new TextField("Policy Number");
		//Vaadin8-setImmediate() policyNumberTF.setImmediate(true);
		policyNumberTF.setWidth("220px");
		policyNumberTF.setTabIndex(1);
		policyNumberTF.addStyleName("policyNumberUpperCase");
		policyNumberTF.setMaxLength(50);
//		policyNumberTF.setValue("P/151118/04/2018/000009");

		// Policy Number CSValidator
		CSValidator policyNumberValidator = new CSValidator();
		policyNumberValidator.extend(policyNumberTF);
		policyNumberValidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		policyNumberValidator.setPreventInvalidTyping(true);
		
		// Health Card No TextField
		healthCardNoTF = new TextField("Health Card No");
		//Vaadin8-setImmediate() healthCardNoTF.setImmediate(true);
		healthCardNoTF.setWidth("180px");
		healthCardNoTF.setMaxLength(25);
		healthCardNoTF.setTabIndex(2);
		
		// Helath Card No CSValidator
		CSValidator healthCardValidator = new CSValidator();
		healthCardValidator.extend(healthCardNoTF);
		healthCardValidator.setRegExp("^[a-zA-Z 0-9 -]*$");
		healthCardValidator.setPreventInvalidTyping(true);

		// Form Left
		FormLayout leftFLayout = new FormLayout(policyNumberTF);
		leftFLayout.setMargin(false);
		leftFLayout.setSpacing(false);
		
		// Form Right
		FormLayout rightFLayout = new FormLayout(healthCardNoTF);
		rightFLayout.setMargin(false);
		rightFLayout.setSpacing(false);
		
		// Form Layout
		formHLayout = new HorizontalLayout(leftFLayout, rightFLayout);
		formHLayout.setWidth("100%");
		formHLayout.setMargin(false);
		formHLayout.setSpacing(false);
		formHLayout.setComponentAlignment(leftFLayout, Alignment.MIDDLE_LEFT);
		formHLayout.setComponentAlignment(rightFLayout, Alignment.MIDDLE_LEFT);
				
		// Submit Button
		submitBtn = new Button();
		submitBtn.setCaption("Search");
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-1px");
		submitBtn.setTabIndex(3);
		submitBtn.setDisableOnClick(true);
		//Vaadin8-setImmediate() submitBtn.setImmediate(true);
		submitBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(isValidFields()) {
					fireViewEvent(SearchPolicyPresenter.SEARCH_SUBMIT, null);
					submitBtn.setEnabled(true);
				} else {
					showErrorPopup("Please provide atleast one value for search</br>");
					submitBtn.setEnabled(true);
				}
			}
		});
		
		// Reset Button
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-1px");
		resetBtn.setTabIndex(4);
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(SearchPolicyPresenter.RESET_VALUES, null);
			}
		});
		
		// Button Layout
		btnHLayout = new HorizontalLayout(submitBtn, resetBtn);
		btnHLayout.setWidth("100%");
		btnHLayout.setHeight("40px");
		btnHLayout.setMargin(false);
		btnHLayout.setSpacing(true);
		btnHLayout.setComponentAlignment(submitBtn, Alignment.BOTTOM_RIGHT);
		btnHLayout.setComponentAlignment(resetBtn, Alignment.BOTTOM_LEFT);
		
		topVLayout = new VerticalLayout();
		topVLayout.setMargin(true);
		topVLayout.setSpacing(false);
		topVLayout.addComponent(formHLayout);
		topVLayout.addComponent(btnHLayout);
		
		topPanel = new Panel();
		topPanel.setWidth("100%");
		topPanel.setCaption("Create Intimation");
		topPanel.addStyleName("panelHeader");
		topPanel.addStyleName("g-search-panel");
		//Vaadin8-setImmediate() topPanel.setImmediate(false);
		topPanel.setContent(topVLayout);
	}
	
	private void buildSpaceLayout() {
		// Space Layout
		spaceHLayout = new HorizontalLayout();
		spaceHLayout.setWidth("100%");
		spaceHLayout.setHeight("10px");
		spaceHLayout.setMargin(false);
		spaceHLayout.setSpacing(false);
	}
			
	public void resetAllValues() {
		removeTable();
		policyNumberTF.setValue(emptyValue);
		healthCardNoTF.setValue(emptyValue);
		policyNumberTF.focus();
	}
	
	private void buildBottomLayout() {
		tableVLayout = new VerticalLayout();
		tableVLayout.setWidth("100%");
		tableVLayout.setHeight("380px");
	}
	
	private boolean isValidFields() {
		boolean isValid = false;
		boolean isValidPolicyNumber = true;
		boolean isValidHealthCardNo = true;
		
		if(policyNumberTF.getValue() == null || emptyValue.equals(policyNumberTF.getValue().toString().trim())) {
			isValidPolicyNumber = false;
		}
		if(healthCardNoTF.getValue() == null || emptyValue.equals(healthCardNoTF.getValue().toString().trim())) {
			isValidHealthCardNo = false;
		}
		
		if(isValidPolicyNumber || isValidHealthCardNo) {
			isValid = true;
		}
		return isValid;
	}
	
	private void showErrorPopup(String errorMessage) {
		Label label = new Label(errorMessage, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		return;
	}
	
	public void showTable() {
		buildPolicyTableData();
		
		if(table.getItemIds().size() > 0) {
			table.setWidth(tableVLayout.getWidth(), Unit.PERCENTAGE);
			table.setPageLength(table.getItemIds().size() > 10 ? 10 : table.getItemIds().size());
			tableVLayout.addComponent(table);
		} else {
			Label successLabel = new Label("<b style = 'color: red;'>No records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			homeButton.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					removeTable();
//					fireViewEvent(MenuItemBean.SEARCH_POLICY, null);
				}
			});
		}
	}
	
	private String getValueFromComponent(Object component){
		if(component instanceof TextField) {
			TextField field = (TextField) component; 
			if(field.getValue() == null || field.getValue() == "" || field.getValue().toString().trim().length() == 0) {
				return null;
			}
			return field.getValue();
		}
		return null;
	}
	
	private void removeTable() {
		// Remove table
		tableVLayout.removeAllComponents();
	}
	
	private void buildPolicyTableData() {
		removeTable();
		
		table = new Table();
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.setWidth(100, Unit.PERCENTAGE);
		table.setSizeFull();
	
		try {
			enteredValues.clear();
			enteredValues.put("polNo", getValueFromComponent(policyNumberTF));
			enteredValues.put("healthCardNumber", getValueFromComponent(healthCardNoTF));
			
			BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremia(enteredValues);
			table.setContainerDataSource(policyContainer);
					
			/*table.addGeneratedColumn("serialNumber", new Table.ColumnGenerator() {
			    @Override
			    public Object generateCell(final Table source, final Object itemId, final Object columnId) {
			        Container.Indexed container = (Container.Indexed) source.getContainerDataSource();
			        return Integer.toString(container.indexOfId(itemId) + 1);
			    }
			});*/
			
			table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source, final Object itemId, Object columnId) {
					PremPolicy policy = (PremPolicy) itemId;
					
					final Button addIntimationButton = new Button("Add Intimation");
					//addIntimationButton.setWidth();
					//addIntimationButton.setWidth("22px");
					addIntimationButton.addClickListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							PremPolicy tmpPolicy = (PremPolicy) itemId;
//						    PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
						        	
						    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
							/*Date StartDate = new Date();
							Date endDate = new Date();
							try {
								StartDate = formatter.parse(policyDetails.getPolicyStartDate());
								endDate = formatter.parse(policyDetails.getPolicyEndDate());
							} catch (ParseException e) {
								e.printStackTrace();
							}*/
							//if(policyDetails.getPolicyStatus() != null && SHAConstants.NEW_POLICY.equalsIgnoreCase(policyDetails.getPolicyStatus()) || SHAConstants.ENDORESED_POLICY.equalsIgnoreCase(policyDetails.getPolicyStatus())) {
							if(tmpPolicy.getPolicyStatus() != null && ! SHAConstants.CANCELLED_POLICY.equalsIgnoreCase(tmpPolicy.getPolicyStatus())) {
								Boolean tataTrustPolicy = masterService.isTataTrustPolicy(tmpPolicy.getPolicyNo());
								if(! tataTrustPolicy){
									LinkedHashMap<String, String> policyValues = new LinkedHashMap<String, String>();
									
									PolicySource policySource = policyService.getByPolicySource(tmpPolicy.getPolicyNo());
									Product product = null;
									if (policySource != null && policySource.getPolicySourceFrom() != null && policySource.getPolicySourceFrom().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
										product = masterService.getProductBySource(tmpPolicy.getProductCode());
										policyValues.put("Policy Source", SHAConstants.BANCS_POLICY);
									}else{
										product = masterService.getProductByProductCode(tmpPolicy.getProductCode());
										policyValues.put("Policy Source", SHAConstants.PREMIA_POLICY);
									}
									
						    		OrganaizationUnit organaizationUnit = policyService.getOrgUnitName(tmpPolicy.getOfficeCode());
						    		policyValues.put("Policy Number", tmpPolicy.getPolicyNo());
						    		policyValues.put("Insured Name", "");
						    		policyValues.put("Product Name", product.getValue());
						    		policyValues.put("PIO Name", organaizationUnit.getOrganizationUnitName());
						    		

						    		fireViewEvent(MenuItemBean.NEW_INTIMATION, tmpPolicy, policyValues);
								}else{
									showErrorPopup("Cannot Create Intimation for TATA TRUST Policy in Claim Portal. Please Create Intimation in Hospital Portal.");		
								}
							} else {
								showErrorPopup("This Policy is Cancelled. Intimation can not be created !!! .");			
							}
						} 
					});
					
					addIntimationButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					List<GalaxyIntimation> intimationByPolicy = previousIntimation.getIntimationByPolicy(policy.getPolicyNo());
					if(intimationByPolicy != null && ! intimationByPolicy.isEmpty()) {
						addIntimationButton.addStyleName("linkButtonColor");
					} else {
						addIntimationButton.addStyleName("linkButton");
					}
					return addIntimationButton;
				}
			});
					
			table.addGeneratedColumn("Insured Office", new Table.ColumnGenerator() {
				@Override
			    public Object generateCell(final Table source, final Object itemId, Object columnId) {
					PremPolicy policy = (PremPolicy) itemId ;
			    	List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService.getInsuredOfficeNameByDivisionCode(policy.getOfficeCode());
			    	  
			    	TextField insuredOfficeName = new TextField();
			    	if(!insuredOfficeNameByDivisionCode.isEmpty()) {
			    		insuredOfficeName.setValue(insuredOfficeNameByDivisionCode.get(0).getOrganizationUnitName());
			    	}
			    	insuredOfficeName.setReadOnly(true);
		    		insuredOfficeName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			        return insuredOfficeName;
				}
			});
					
			Object[] columns = new Object[]{/*"serialNumber",*/ "Action","policyNo", "healthCardNo", "insuredName", "address","mobileNumber", "Insured Office", "productName", "sumInsured", "telephoneNo"};
			table.setVisibleColumns(columns);
			table.setColumnWidth("address", 120);
			//table.setColumnWidth("healthCardNo", 100);
			
//			table.removeGeneratedColumn("sumInsured");
//			table.addGeneratedColumn("sumInsured", new Table.ColumnGenerator() {
//				@Override
//			    public Object generateCell(final Table source, final Object itemId, Object columnId) {
//					String sumInsured = ((PremPolicy) itemId).getSumInsured();
//				    try {
//				    	if(sumInsured != null) {
//				    		return SHAUtils.getIndianFormattedNumber(NumberFormat.getInstance().parse(sumInsured));
//				    	}		
//				    } catch (ParseException e) {
//				    	e.printStackTrace();
//				    }
//				    return null;
//			    }
//			});
			
			table.addGeneratedColumn("Date Of Inception", new Table.ColumnGenerator() {
				@Override
			    public Object generateCell(final Table source, final Object itemId, Object columnId) {
				    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
				    String policySource = ((PremPolicy)itemId).getPolicySource();
				    if(policySource != null && policySource.equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
				    	formatter = new SimpleDateFormat("dd/MM/yyyy");
					}
				    try {
				    	Date date = formatter.parse(((PremPolicy)itemId).getDateofInception());
						return new SimpleDateFormat("dd/MM/yyyy").format(date);
				    } catch (ParseException e) {
				    	e.printStackTrace();
				    }
				    return ((PremPolicy)itemId).getDateofInception();
			    }
			});
					
			table.addGeneratedColumn("Date of Expiry", new Table.ColumnGenerator() {
				@Override
			    public Object generateCell(final Table source, final Object itemId, Object columnId) {
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
					String policySource = ((PremPolicy)itemId).getPolicySource();
				    if(policySource != null && policySource.equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
				    	formatter = new SimpleDateFormat("dd/MM/yyyy");
					}
			    	try {
			    		Date date = formatter.parse(((PremPolicy)itemId).getPolicyEndDate());
						return new SimpleDateFormat("dd/MM/yyyy").format(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
			    	return ((PremPolicy)itemId).getPolicyEndDate();
				}
			});	
					
			table.addGeneratedColumn("View", new Table.ColumnGenerator() {
				@Override
			    public Object generateCell(final Table source, final Object itemId, Object columnId) {
					VerticalLayout buttonLayout =new VerticalLayout();
			    	  
			    	final Button viewPreviousIntimationButton = new Button("View Previous Intimation");
			    	viewPreviousIntimationButton.addStyleName(ValoTheme.BUTTON_LINK);
			    	viewPreviousIntimationButton.addClickListener(new Button.ClickListener() {
			    		public void buttonClick(ClickEvent event) {
			    			PremPolicy tmpPolicy = (PremPolicy) itemId;
			    			
			    			String policyServiceType = "OTHERS";
							if(tmpPolicy.getProductCode() != null){
								//Product productByProductCode = masterService.getProductByProductCode(tmpPolicy.getProductCode());
								Product productByProductCode = null;
								if (tmpPolicy.getPolicySource() != null && tmpPolicy.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
									productByProductCode = glxWBService.getProductByProductCode(tmpPolicy.getProductCode());
								}else{
									productByProductCode = premiaPullService.getProductByProductCode(tmpPolicy.getProductCode());
								}
								if(productByProductCode != null && productByProductCode.getProductService() != null){
									policyServiceType = productByProductCode.getProductService();
								}
							}
				        	
			    			cashLessTableMapper.getAllMapValues();
			    			
			    			PremPolicyDetails policyDetails = null;
							
							if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
								policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
								
							}else if(policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)){
								policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
							}else if(policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)){
								policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(tmpPolicy
										.getPolicyNo());
							}else{
								policyDetails =  policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
							}
			    			
			    			Item item = source.getItem(itemId);						            
			    			ViewPreviousIntimation view = new ViewPreviousIntimation(
												policyDetails,
												masterService,
												policyService,
												hospitalService,
												claimService,
												cashLessTableDetails,
												cashlessTable,
												cashLessTableMapper,
												newIntimationService, 
												intimationService, 
												viewPreviousIntimationTable, 
												entityManager);
			    			
			    			/*if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
			    				view.setPositionX(50);
			    				view.setPositionY(50);
							}*/
			    			view.setPositionX(50);
			    			view.setPositionY(30);
			    			UI.getCurrent().addWindow(view);
			    		} 
			    	});
			    	  
			    	final Button viewPolicyConditonsButton = new Button("View Policy Condtions");
			    	viewPolicyConditonsButton.addStyleName("link");
			    	viewPolicyConditonsButton.addClickListener(new Button.ClickListener() {
			    		public void buttonClick(ClickEvent event) {
			    			Item item = source.getItem(itemId);
			    			String policyServiceType = "OTHERS";
			    			PremPolicyDetails policyDetails = null;
							PremPolicy tmpPolicy = (PremPolicy) itemId;
							if(tmpPolicy.getProductCode() != null){
								Product productByProductCode = null;
								if (tmpPolicy.getPolicySource() != null && tmpPolicy.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
									productByProductCode = glxWBService.getProductByProductCode(tmpPolicy.getProductCode());
								}else{
									productByProductCode = premiaPullService.getProductByProductCode(tmpPolicy.getProductCode());
								}
									
								if(productByProductCode != null && productByProductCode.getProductService() != null){
									policyServiceType = productByProductCode.getProductService();
								}
							}
							ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance.get();
							if(policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)){
								policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(tmpPolicy
										.getPolicyNo(), "0");
								List<PremGmcBenefitDetails> gmcPolicyConditions = policyDetails.getGmcPolicyConditions();
								 List<ViewProductBenefitsTableDTO> finalTableList = new ArrayList<ViewProductBenefitsTableDTO>();
								if(gmcPolicyConditions != null && ! gmcPolicyConditions.isEmpty()){
									 ViewProductBenefitsTableDTO productBenefitTableList = null;
									for (PremGmcBenefitDetails premGmcBenefitDetails : gmcPolicyConditions) {
										productBenefitTableList = new ViewProductBenefitsTableDTO();
										 productBenefitTableList.setConditionCode(premGmcBenefitDetails.getConditionCode());
										 productBenefitTableList.setDescription(premGmcBenefitDetails.getConditionDesc());
										 productBenefitTableList.setLongDescription(premGmcBenefitDetails.getConditionLongDesc());
										 finalTableList.add(productBenefitTableList);
									}
									getGMCProductBenefitTableValues(finalTableList);
								}
								
							}
							else{
							a_viewProductBenefits.showValues(tmpPolicy);
							
							//if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
								a_viewProductBenefits.setPositionX(250);
								a_viewProductBenefits.setPositionY(30);
							//}
						UI.getCurrent().addWindow(a_viewProductBenefits);
			    		} 
			    		}
			    	});
			    	  
			    	buttonLayout.addComponent(viewPolicyConditonsButton);
			    	buttonLayout.addComponent(viewPreviousIntimationButton);
			    	buttonLayout.setMargin(Boolean.FALSE);
			    	buttonLayout.setSpacing(Boolean.FALSE);
			    	return buttonLayout;
				}
			});
			
			//table.setColumnHeader("serialNumber", "S.No");
			table.setColumnHeader("policyNo", "Policy No");
			table.setColumnHeader("healthCardNo", "Health </br> Card No");
			table.setColumnHeader("insuredName", "Insured </br> Name");
			table.setColumnHeader("address", "Address");
			table.setColumnHeader("mobileNumber", "Registered </br> Mobile </br> Number");
			table.setColumnHeader("productName","Product </br> Name");
			table.setColumnHeader("sumInsured", "Sum </br> Insured (Rs.)");
			table.setColumnHeader("telephoneNo", "Telephone </br> No");
			table.setColumnHeader("Date Of Inception","Date Of </br> Inception");
			table.setColumnHeader("Date of Expiry","Date Of </br> Expiry");
			table.setColumnHeader("Insured Office","Insured </br> Office");
			
						
			table.setColumnWidth("healthCardNo",55);
			table.setColumnWidth("address",110);
			table.setColumnWidth("Insured Office",110);
			table.setColumnWidth("Date Of Inception",67);
			table.setColumnWidth("Date of Expiry",67);
			table.setColumnWidth("policyNo", 160);
			table.setColumnWidth("Action", 110);
			table.setColumnWidth("insuredName", 120);
			table.setColumnWidth("productName", 120);
			table.setColumnWidth("mobileNumber", 100);
			table.setColumnWidth("sumInsured", 70);
			table.setColumnWidth("telephoneNo", 70);
			
			
			/*table.setColumnWidth("serialNumber",15);
			table.setColumnWidth("policyNo",25);
			
			table.setColumnWidth("insuredName",15);
			table.setColumnWidth("address",15);
			table.setColumnWidth("mobileNumber",15);
			table.setColumnWidth("productName",15);
			table.setColumnWidth("sumInsured",15);
			table.setColumnWidth("telephoneNo",15);*/
			
		} catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		if(table.getItemIds().size() > 10) {
			table.setPageLength(10);
		} else {
			table.setPageLength(table.getItemIds().size());
		}
	}
	
	public void searchPolicyWithParameter(String policyNumber, String healthCardNumber){
		policyNumberTF.setValue(policyNumber);
		healthCardNoTF.setValue(healthCardNumber);
		
		if((policyNumber != null && ! policyNumber.isEmpty())|| (healthCardNumber != null && ! healthCardNumber.isEmpty())){
			fireViewEvent(SearchPolicyPresenter.SEARCH_SUBMIT, null);
		}else{
			Label msgLabel = new Label("<b style = 'color: red;'> Please Enter Policy No or HealthCard Number</b>",ContentMode.HTML);
			tableVLayout.addComponent(msgLabel);
			tableVLayout.setComponentAlignment(msgLabel, Alignment.TOP_CENTER);
			//tableVLayout
		}
		topPanel.setVisible(false);
		
		
	}
	 private void getGMCProductBenefitTableValues(List<ViewProductBenefitsTableDTO> finalTableList)
	 {		  
		 	viewProductBenefitsTable.init("", false, false);
		 	viewProductBenefitsTable.setTableList(finalTableList);	 	
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View Product Benefits");
			popup.setHeight("400px");
//			this.setWidth("750px");
			popup.setWidth("80%");
			popup.setContent(viewProductBenefitsTable);
			popup.setClosable(true);
//			popup.center();
			popup.setResizable(true);
			popup.setModal(true);
			popup.setPositionX(250);
			popup.setPositionY(30);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});
			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
 }
	 
}
