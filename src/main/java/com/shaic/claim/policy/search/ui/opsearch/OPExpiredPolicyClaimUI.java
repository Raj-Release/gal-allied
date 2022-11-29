package com.shaic.claim.policy.search.ui.opsearch;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.jensjansson.pagedtable.PagedTable;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.claim.policy.search.ui.PremInsuredDetails;
import com.shaic.claim.policy.search.ui.PremInsuredNomineeDetails;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.PremiaToPolicyMapper;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyBankDetails;
import com.shaic.domain.PolicyEndorsementDetails;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreviousPolicy;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.fraudidentification.FraudIdentificationPresenter;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class OPExpiredPolicyClaimUI extends ViewComponent {

	private static final long serialVersionUID = 4174091244258792154L;
	
	HashMap<String, String> enteredValues = new HashMap<String, String>();
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private InsuredService insuredService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DBCalculationService dbcalculationService;
	
	@Inject
	private PremiaToPolicyMapper premiaPolicyMapper;
	
//	@Inject
//	private IntimationService intimationService;
//	
//	@Inject
//	private ViewPreviousIntimationTable viewPreviousIntimationTable;
	
	private Panel panel_2;
	
	private VerticalLayout verticalLayout_2;
	
	private VerticalLayout wholeVerticalLayout;
	
	private VerticalLayout mainVerticalLayout;
	
	private HorizontalLayout buttonHorLayout;
	
	private Button resetButton;
	
	private Button submitButton;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private TextField insuredName;
	
	
	private TextField proposerName;
	
	
	private PopupDateField proposerDOB;
	
	
	private TextField helathCardNo;
	
	
	private TextField mobileNumber;
	
	
	private TextField policyNumber;
	
	private TextField intimationNumber;
	
	private TextField claimNumber;
	
	private Label searchByLabel;
	
	private TextArea remarksTxta;
	
	private CheckBox chkBox;
	
	private ArrayList<Component> mandatoryFields;
	
	private PagedTable policyDetailsTable;
	
	private FormLayout  searchGridLayout;
	
	private FormLayout  secondFormLayout;
	
	private HorizontalLayout buildSearchByPolicyLayout;
	
	@Inject
	private Instance<ViewProductBenefits> viewProductBenefitInstance;
	
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private ViewPolicyDetails viewPolicyDetail;
	
	
	private Boolean allowClaimProcessing = false;
	
//	private static Properties properties;
//	private static String dataDir = System.getProperty("jboss.server.data.dir");
	
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.addComponent(buildMainLayout());
		wholeVerticalLayout.setComponentAlignment(panel_2, Alignment.MIDDLE_LEFT);
		setCompositionRoot(wholeVerticalLayout);
	}

	private Panel buildMainLayout() {
		panel_2 = buildPanel_2();
		return panel_2;
	}

	
	private Panel buildPanel_2() {
		panel_2 = new Panel();
		//Vaadin8-setImmediate() panel_2.setImmediate(false);
		panel_2.setCaption("Allow Claim for Expired Policies");
		panel_2.addStyleName("panelHeader");
		panel_2.addStyleName("g-search-panel");

		verticalLayout_2 = buildAbsoluteLayout_3();
		panel_2.setContent(verticalLayout_2);

		return panel_2;
	}
	public VerticalLayout buildAbsoluteLayout_3() {
		// common part: create layout
		mainVerticalLayout = new VerticalLayout();
		mainVerticalLayout.setSpacing(true);
		mainVerticalLayout.setMargin(true);
		HorizontalLayout tempLayout = buildSearchIntimationLayout();
		mainVerticalLayout.addComponent(tempLayout);
		mainVerticalLayout.addComponent(buildButtonLayout());
		return mainVerticalLayout;
	}
	
	private AbsoluteLayout buildButtonLayout() {
		// submitButton
		submitButton = new Button();
		submitButton.setCaption("Search");
		//Vaadin8-setImmediate() submitButton.setImmediate(true);
		submitButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.setDisableOnClick(true);

		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -4854461697920987945L;
			@Override
			public void buttonClick(ClickEvent event) {
				submitButton.setEnabled(true);
				/*if(((SelectValue)searchOptions.getValue()).getValue().equalsIgnoreCase("Create Intimation")){
					fireViewEvent(OPExpiredPolicyClaimPresenter.CREATE_INTIMATION_SUBMIT, null);
				}*/
				//if(((SelectValue)searchOptions.getValue()).getValue().equalsIgnoreCase("Search Intimation")){
					fireViewEvent(OPExpiredPolicyClaimPresenter.SEARCH_INTIMATION_SUBMIT_OP, null);
				//}
			}
		});
		// resetButton
		resetButton = new Button();
		resetButton.setCaption("Reset");
		resetButton.setTabIndex(10);
		resetButton.addStyleName(ValoTheme.BUTTON_DANGER);
		resetButton.setWidth("-1px");
		resetButton.setHeight("-1px");
		resetButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5472521730648387559L;
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(OPExpiredPolicyClaimPresenter.RESET_VALUES_OP, null);
			}
		});

		AbsoluteLayout searchIntimation_layout =  new AbsoluteLayout();
		searchIntimation_layout.addComponent(submitButton, "top:30.0px;left:220.0px;");
		searchIntimation_layout.addComponent(resetButton, "top:30.0px;left:329.0px;");
		searchIntimation_layout.setWidth("100.0%");
		searchIntimation_layout.setHeight("80px");
		
		return searchIntimation_layout;
	}
	
	private HorizontalLayout buildSearchIntimationLayout() {

		FormLayout policyLayout = new FormLayout();		
		policyLayout.setWidth("100%");
		policyLayout.setSpacing(false);
		// policyNumber
		policyNumber = new TextField("Policy Number");
		policyNumber.setWidth("180px");
		policyNumber.setMaxLength(25);
		
		HorizontalLayout holderLayout = new HorizontalLayout();
		policyLayout.addComponent(policyNumber);

		holderLayout.addComponent(policyLayout);
		holderLayout.setSpacing(true);
		holderLayout.setHeight("80px");

		return holderLayout;
}
	
	public void resetAlltheValues() {
			policyNumber.setValue("");
			removeTableFromLayout();
	}	
	
	private void removeTableFromLayout(){
		// Remove the Table also if it exists.
		ArrayList<Component> componentArray = new ArrayList<Component>();
		int componentCount = wholeVerticalLayout.getComponentCount();
		for (int i=0; i < componentCount; i++) {
			Component eachComponent = wholeVerticalLayout.getComponent(i);
			String componentClass = eachComponent.getClass().toString();
			if(StringUtils.equalsIgnoreCase(componentClass, "class com.vaadin.ui.Panel")){
			   continue;
			}
			componentArray.add(eachComponent);
		}
		for (int i=0; i < componentArray.size(); i++) {
			wholeVerticalLayout.removeComponent(componentArray.get(i));
		}
	}
	
	private String getValueFromComponent(Object component){
		if(component instanceof TextField) {
			TextField field = (TextField) component; 
			if(field.getValue() == null || field.getValue() == "" || field.getValue().toString().length() == 0){
				return null;
			}
			return field.getValue();
		} else if(component instanceof ComboBox) {
			ComboBox field = (ComboBox) component; 
			if(field.getValue() == null || field.getValue() == "" || field.getValue().toString().length() == 0){
				return null;
			}
			return field.getValue().toString();
		} 
		return null;
	}
	
	private String getValueFromMasterTable(ComboBox component){
		 
		if(component!= null && component.getValue() == null || component.getValue() == ""){
			return null;
		}
		 
		if(component.getValue() instanceof MastersValue) {
			return ((MastersValue) component.getValue()).getValue();
		} else if(component.getValue() instanceof OrganaizationUnit) {
			return ((OrganaizationUnit) component.getValue()).getOrganizationUnitId();
		}  else if(component.getValue() instanceof Product) {
			return ((Product) component.getValue()).getCode();
		}
		
			return null;
	}
	
	public Boolean validateFields() {
		// Always clear the Map.
		enteredValues.clear();
		enteredValues.put("polNo", getValueFromComponent(policyNumber));
				
		// Remove null values key, value pair from MAP.
		enteredValues.values().removeAll(Collections.singleton(null));
		
		String policyNumber = enteredValues.get("polNo");
		if(policyNumber == null || policyNumber.isEmpty() ) {
			String errorMessage = "Please provide Policy Number";
			showErrorPopup(errorMessage);
			return false;
		}

		//Prevent Active policies
		
		if((enteredValues.containsKey("polNo"))){
			
			String policyNumberInput = enteredValues.get("polNo");
			
			if(policyNumberInput != null){
				
				System.out.println(String.format("Policy Number [%s]", policyNumberInput));
				Policy dbPolicy = policyService.getByPolicyNumber(policyNumberInput);
				if(dbPolicy != null){
					Date dbpolicystartDate = dbPolicy.getPolicyFromDate();
					Date dbpolicyEndDate = dbPolicy.getPolicyToDate();
					Date currentDate= new Date();
					
					if(dbpolicystartDate != null && dbpolicyEndDate!=null){
						Boolean isPolicyExpired = false;
						if(!SHAUtils.isDateOfIntimationWithPolicyRange(dbpolicystartDate, dbpolicyEndDate, currentDate)){
							isPolicyExpired = true;
						}
						if(!isPolicyExpired){
							
							String errorMessage = "Please enter only expired policy number";
							showErrorPopup(errorMessage);
							return false;
						}
					}
					
				}
				else{
					
					BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremiaforOP(enteredValues);
					
					PremPolicy premiaPolicy = null;
					
					if(!policyContainer.getItemIds().isEmpty()){
						
						 premiaPolicy = policyContainer.getItemIds().get(0);
						 
					}
					
					if(premiaPolicy !=null){
						
						PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(premiaPolicy.getPolicyNo());
						SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
						Date premiPolicystartDate=null;
						Date premiaPolicyEndDate=null;
						if(policyDetails !=null){
							try {
								premiPolicystartDate = formatter.parse(policyDetails.getPolicyStartDate());
								premiaPolicyEndDate = formatter.parse(policyDetails.getPolicyEndDate());
							} catch (ParseException e) {
								e.printStackTrace();
							}
							Date currentDate= new Date();
							
							if(premiPolicystartDate != null && premiPolicystartDate!=null){
								Boolean isPolicyExpired = false;
								if(!SHAUtils.isDateOfIntimationWithPolicyRange(premiPolicystartDate, premiaPolicyEndDate, currentDate)){
									isPolicyExpired = true;
								}
								if(!isPolicyExpired){
									
									String errorMessage = "Please enter only expired policy number";
									showErrorPopup(errorMessage);
									return false;
								}
							}
						}else{
							
							System.out.println(String.format("Policy doesn't exist in premia policy Details", policyNumberInput ));
							
						}
						
						
					}else{
						System.out.println(String.format("Policy doesn't exist in DB & Premia [%s]", policyNumberInput ));
					}							
				}
				
			}
			
		}
		return true;
	}
	
	private PagedTable retrieveCorrespondingValues() {
		return retrieveCorrespondingValuesFromPremia();
		//retrieveOPIntimationFromDB();
		//return null;

	}
	
	
	private PagedTable retrieveCorrespondingValuesFromPremia()
	{
		PagedTable table = new PagedTable();
		policyDetailsTable = new PagedTable("Intimation Table");
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.setWidth(100, Unit.PERCENTAGE);
		table.setSizeFull();

		try {
				if(validateFields()) {
//					Add New Method for OP because PolicySource not using
//					BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremia(enteredValues);
					BeanItemContainer<PremPolicy> policyContainer =  policyService.filterPolicyDetailsPremiaforOP(enteredValues);
					if(policyContainer.getContainerPropertyIds().isEmpty()){
						return table;
					}
					table.setContainerDataSource(policyContainer);
					
					Object[] columns = new Object[]{"Allow Claim Processing", "View","View Policy Details","policyNo", "insuredName", "address","mobileNumber", "Insured Office", "productName", "Date Of Inception", "Date of Expiry"};
					
					
					
					table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
					table.setColumnHeader("Allow Claim Processing", "Allow Claim Processing");
					table.setColumnHeader("View", "View");
					table.setColumnHeader("View Policy Details", "View Policy Details");
					table.setColumnHeader("policyNo", "Policy No");
					table.setColumnHeader("insuredName", "Proposer Name");
					table.setColumnHeader("address", "Address");
					table.setColumnHeader("mobileNumber", "Registered Mobile Number");
					table.setColumnHeader("productName","Product Name");
					table.setColumnHeader("Date Of Inception", "Date Of Inception");
					table.setColumnHeader("Date of Expiry", "Date of Expiry");
					table.setColumnWidth("address", 180);
					
					table.removeGeneratedColumn("Date Of Inception");
					table.addGeneratedColumn("Date Of Inception", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;
						@Override
						public Object generateCell(final Table source, final Object itemId, Object columnId) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
							try {
								PremPolicy tmpPolicy = (PremPolicy) itemId;
								PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
						    	Policy policy = policyService.getByPolicyNumber(policyDetails.getPolicyNo());
								/*if(policy!=null){
									Date date = (((Policy)itemId).getPolicyFromDate());
									return new SimpleDateFormat("dd/MM/yyyy").format(date);*/
								/*}else{*/
									//Date date = formatter.parse(policyDetails.getPolicyStartDate());
									Date date = policy.getPolicyFromDate();
									return new SimpleDateFormat("dd/MM/yyyy").format(date);
								//}

							} catch (Exception e) {
								e.printStackTrace();
							}
							return ((PremPolicy)itemId).getDateofInception();
						}
					});
					table.removeGeneratedColumn("Date of Expiry");
					table.addGeneratedColumn("Date of Expiry", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;
						@Override
						public Object generateCell(final Table source, final Object itemId, Object columnId) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
							try {
								PremPolicy tmpPolicy = (PremPolicy) itemId;
								PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
						    	Policy policy = policyService.getByPolicyNumber(policyDetails.getPolicyNo());
								//Date date = formatter.parse(policyDetails.getPolicyEndDate());
						    	Date date = policy.getPolicyToDate();
								return new SimpleDateFormat("dd/MM/yyyy").format(date);
							} catch (Exception e) {

								e.printStackTrace();
							}
							return ((PremPolicy)itemId).getPolicyEndDate();
						}
					});
					
					table.removeGeneratedColumn("Insured Office");
					table.addGeneratedColumn("Insured Office", new Table.ColumnGenerator() {
					      
						private static final long serialVersionUID = 1L;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					     //    When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	  //TODO : Fix this column
					    	  PremPolicy policy = (PremPolicy) itemId ;
					    	  List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService.getInsuredOfficeNameByDivisionCode(policy.getOfficeCode());
//					    	  Label insuredOfficeName = null; 
					    	  TextField insuredOfficeName = new TextField();
					    	  if(!insuredOfficeNameByDivisionCode.isEmpty()){
//					    		  insuredOfficeName = new Label();
					    		  
					    		  insuredOfficeName.setValue(insuredOfficeNameByDivisionCode.get(0).getOrganizationUnitName());
					    		  					    		  
					    	  }
					    	  insuredOfficeName.setReadOnly(true);
				    		  insuredOfficeName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					        return insuredOfficeName;
					      }
					});
					
					table.removeGeneratedColumn("View");
					table.addGeneratedColumn("View", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					       //  When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	  HorizontalLayout buttonLayout =new HorizontalLayout();
					 
					    	 final Button viewPolicyConditonsButton = new Button("View Policy Condtions");
						        
						        viewPolicyConditonsButton.addClickListener(new Button.ClickListener() {
							    /**
									 * 
									 */
									private static final long serialVersionUID = 1L;

								public void buttonClick(ClickEvent event) {
								    	Item item = source.getItem(itemId);
								    	PremPolicy tmpPolicy = (PremPolicy) itemId;
								    	PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
								    	ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
												.get();
							
								    	a_viewProductBenefits.showValues(policyDetails.getPolicyNo());
										UI.getCurrent().addWindow(a_viewProductBenefits);
							        } 
							    });
						    	viewPolicyConditonsButton.addStyleName("link");
						    	buttonLayout.addComponent(viewPolicyConditonsButton);
					        return buttonLayout;
					      }
					    });
					
					table.removeGeneratedColumn("View Policy Details");
					table.addGeneratedColumn("View Policy Details", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;

						@Override
					      public Object generateCell(final Table source, final Object itemId, Object columnId) {
					       //  When the chekboc value changes, add/remove the itemId from the selectedItemIds set 
					    	  HorizontalLayout buttonLayout =new HorizontalLayout();
					 
					    	 final Button viewPolicyDetailsButton = new Button("View Policy Details");
						        
						        viewPolicyDetailsButton.addClickListener(new Button.ClickListener() {
							    /**
									 * 
									 */
									private static final long serialVersionUID = 1L;

								public void buttonClick(ClickEvent event) {
								    	Item item = source.getItem(itemId);
								    	PremPolicy tmpPolicy = (PremPolicy) itemId;
								    	PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(tmpPolicy.getPolicyNo());
								    	Policy policy = policyService.getByPolicyNumber(policyDetails.getPolicyNo());
										viewPolicyDetail.setPolicyServiceAndPolicy(policyService, policy,
												masterService, intimationService);	
										viewPolicyDetail.initView();
										UI.getCurrent().addWindow(viewPolicyDetail);
							        } 
							    });
						        viewPolicyDetailsButton.addStyleName("link");
						    	buttonLayout.addComponent(viewPolicyDetailsButton);
					        return buttonLayout;
					      }
					    });
					
					
					table.removeGeneratedColumn("Allow Claim Processing");
					table.addGeneratedColumn("Allow Claim Processing", new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;
						@Override
						public Object generateCell(Table source, final Object itemId, Object columnId) {
							// TODO Auto-generated method stub
							PremPolicy policy = (PremPolicy) itemId ;
							Policy fromDBPolicy = policyService.getByPolicyNumber(policy.getPolicyNo());
							String opAllowIntimation = "";
							if(fromDBPolicy !=null && fromDBPolicy.getOpAllowIntimation()!=null){
								 opAllowIntimation = fromDBPolicy.getOpAllowIntimation();
							}
							
							  
							Boolean isChecked = (opAllowIntimation.equalsIgnoreCase(SHAConstants.YES_FLAG))?true : false;
							allowClaimProcessing = isChecked;
							
							//final CheckBox chkBox = new CheckBox();
							chkBox = new CheckBox();
							chkBox.setValue(isChecked);
							if(isChecked){
								chkBox.setEnabled(false);
							}
							
							chkBox.addValueChangeListener(new Property.ValueChangeListener() {
								
								@Override
								public void valueChange(ValueChangeEvent event) {
									
									//List<OPSearchIntimationDTO> items = (List<OPSearchIntimationDTO>) table.getItemIds();
									
									boolean value = (Boolean) event.getProperty().getValue();
									allowClaimProcessing = value;
									
									PremPolicy policy = (PremPolicy) itemId ;
									System.out.println(String.format("Policy Numner [%s]", policy.getPolicyNo()));
									
									/*String dbValue = value ? "Y" : "N";									
									System.out.println(String.format("DBvalue [%s]", dbValue));
									Policy dbPolicy = policyService.getByPolicyNumber(policy.getPolicyNo());
									dbPolicy.setOpAllowIntimation(dbValue);
									policyService.updateOPAllowIntimation(dbPolicy);*/
									System.out.println(String.format("value [%s]", value));
									if(value){
										remarksForAllowClaimProcessing(policy.getPolicyNo());
									}
									
								}
							});
							return chkBox;
					}
					});	
					
					table.setVisibleColumns(columns);
				}else {
					return null;
				}
		} catch(Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		if(table.getItemIds().size() > 10){
			table.setPageLength(10);
		} else {
			table.setPageLength(table.getItemIds().size());
		}
		
		return table;
	
	}
	

	public void showTable(){

		// Remove the table from layout if it exists already.
		removeTableFromLayout();
		PagedTable policyTable = retrieveCorrespondingValues();
		if (!policyTable.getItemIds().isEmpty()) {
			PagedTable retrieveCorrespondingValuesFromDB = policyTable;
			retrieveCorrespondingValuesFromDB.setWidth(wholeVerticalLayout.getWidth(), Unit.PERCENTAGE);
			retrieveCorrespondingValuesFromDB.setPageLength(retrieveCorrespondingValuesFromDB.getItemIds().size() > 10 ? 10 :  retrieveCorrespondingValuesFromDB.getItemIds().size() );

			VerticalLayout tablevertical = new VerticalLayout();
			Panel tablepanel = new Panel(retrieveCorrespondingValuesFromDB);
//			tablevertical.addComponent(createControls);
			
			/*btnSubmit = new Button("Submit");
			btnCancel = new Button("Cancel");
			
			btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			btnSubmit.setWidth("-1px");
			btnSubmit.setHeight("-10px");
			addSubmitButtonListener();
			
			btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
			btnCancel.setWidth("-1px");
			btnCancel.setHeight("-10px");
			addCancelButtonListener();
			
			HorizontalLayout hLayout = new HorizontalLayout(btnSubmit, btnCancel);
			hLayout.setSpacing(true);*/
			
			
			tablepanel.setWidth("100%");
			tablepanel.setHeight("250px");
			tablevertical.addComponent(tablepanel);
			//tablevertical.addComponent(hLayout);
			wholeVerticalLayout.addComponent(tablevertical);
			//wholeVerticalLayout.addComponent(hLayout);
			//wholeVerticalLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
			
		} else {
			Label successLabel = new Label("<b style = 'color: black;'>No Record found for entered search criteria.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create Intimation Home");
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

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.EXPIRED_POLICIES_CLAIM_OP, null);

				}
			});
		}
	}
	
	public void remarksForAllowClaimProcessing(String policyNumber){
		
			remarksTxta = new TextArea("Remarks for allowing the expired policy for claim processing"); 
			remarksTxta.setMaxLength(1000);
			remarksTxta.setWidth("400px");
			//remarksTxta.setDescription(SHAConstants.OP_ALLOW_CLAIM_PROCESSING_REMARKS);
			SHAUtils.handleTextAreaPopupDetails(remarksTxta,null,getUI(),SHAConstants.OP_ALLOW_CLAIM_PROCESSING_REMARKS);
			
			mandatoryFields = new ArrayList<Component>();
			mandatoryFields.add(remarksTxta);
			showOrHideValidation(false);
			
			FormLayout fieldsFLayout;
			fieldsFLayout = new FormLayout(remarksTxta);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			Button submitButtonWithListener = getSubmitButtonWithListener(dialog,policyNumber);

			HorizontalLayout btnLayout = new HorizontalLayout(
					submitButtonWithListener, getCancelButton(dialog));
			btnLayout.setWidth("400px");
			btnLayout.setComponentAlignment(submitButtonWithListener,
					Alignment.MIDDLE_CENTER);
			btnLayout.setSpacing(true);
			
			VerticalLayout verticalLayout = new VerticalLayout(fieldsFLayout, btnLayout);
//			verticalLayout.setWidth("500px");
			verticalLayout.setWidth("800px");
			verticalLayout.setMargin(true);
			verticalLayout.setSpacing(true);
					
			showInPopup(verticalLayout, dialog);
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			if (field != null) {
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
		}
	}
	
	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("Allow Claim Processing");
		dialog.setClosable(true);

		Panel panel = new Panel();
		panel.setHeight("500px");
		panel.setWidth("850px");
		panel.setContent(layout);
		dialog.setContent(panel);
		dialog.setResizable(true);
		dialog.setDraggable(true);
		dialog.setModal(true);

		dialog.show(getUI().getCurrent(), null, true);

	}
	
private Button getSubmitButtonWithListener(final ConfirmDialog dialog, String policyNumber) {
Button submitButton = new Button("Submit");
submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
submitButton.addClickListener(new ClickListener() {
	private static final long serialVersionUID = -5934419771562851393L;

	@Override
	public void buttonClick(ClickEvent event) {
		StringBuffer eMsg = new StringBuffer();
		if (remarksTxta.getValue() != null && !remarksTxta.getValue().isEmpty()) {
			saveValues();
			if(chkBox.getValue()!= null){
				chkBox.setEnabled(false);
			}
			buildSuccessLayout();
			dialog.close();
		}
		else{
			eMsg.append("Please Enter the Remarks Field.");
			getErrorMessage(eMsg.toString());
		}
	}
});
return submitButton;
}

private Button getCancelButton(final ConfirmDialog mainDialog) {
Button	cancelButton = new Button("Cancel");
cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
cancelButton.addClickListener(new ClickListener() {
	@Override
	public void buttonClick(ClickEvent event) {
		
		ConfirmDialog cancelDialog = ConfirmDialog
				.show(getUI(),"Confirmation","Are you sure you want to cancel ?","No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								
								
								if (!dialog.isConfirmed()) {
									
									dialog.close();
									chkBox.setValue(false);
									mainDialog.close();
									
								} else {
									// User did not confirm
									//System.out.println(String.format("Inside Else block"));
								}
							}
						});
		
		cancelDialog.setClosable(false);
		cancelDialog.setStyleName(Reindeer.WINDOW_BLACK);
		
		//dialog.close();
		//chkBox.setValue(false);
		//mainDialog.close();
	}
});
return cancelButton;
}


@SuppressWarnings("static-access")
public void buildSuccessLayout() {

	StringBuffer successLabel = new StringBuffer("Record Saved Successfully.");
	
	final MessageBox msgBox = MessageBox
		    .createInfo()
		    .withCaptionCust("Information")
		    .withMessage(successLabel.toString())
		    .withOkButton(ButtonOption.caption("Allow Claim Processing"))
		    .open();
	Button homeButton=msgBox.getButton(ButtonType.OK);

	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;
		@Override
		public void buttonClick(ClickEvent event) {	
			msgBox.close();
			//toolbar.countTool();
			fireViewEvent(MenuItemBean.EXPIRED_POLICIES_CLAIM_OP, null);
		}
	});
	
}



	@SuppressWarnings("static-access")
	private void showErrorPopup(String errorMessage){
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
	}
	
	   public void getErrorMessage(String eMsg){
			
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	public void refresh(){
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}
	
	private void addCancelButtonListener() {
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure you want to cancel ?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											fireViewEvent(MenuItemBean.EXPIRED_POLICIES_CLAIM_OP,
													null);
										} else {
											// User did not confirm
										}
									}
								});

				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	}
	
	private void addSubmitButtonListener() {
		btnSubmit.addClickListener(new Button.ClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			
			saveValues();
			//chkBox.setEnabled(false);
			fireViewEvent(MenuItemBean.EXPIRED_POLICIES_CLAIM_OP, null);
		}
	});
	}
	
	public void saveValues(){
		String policyNumber = enteredValues.get("polNo");
		String dbValue= allowClaimProcessing ? "Y" : "N";									
		System.out.println(String.format("DBvalue [%s]", dbValue));
		Policy dbPolicy = policyService.getByPolicyNumber(policyNumber);
		if(dbPolicy !=null){
		dbPolicy.setOpAllowIntimationRemarks(remarksTxta.getValue());
		dbPolicy.setOpAllowIntimation(dbValue);
		policyService.updateOPAllowIntimation(dbPolicy);
		}
		else{
			PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(policyNumber);
			if(policyDetails!=null){
				System.out.println(String.format("Inside prempolicy details**************"));
	    	Policy policy = null;
	    	policy = populatePolicyFromTmpPolicy(policyDetails);
	    	policy.setOpAllowIntimationRemarks(remarksTxta.getValue());
	    	policy.setOpAllowIntimation(dbValue);
			policyService.updateOPAllowIntimation(policy);
			}
		}
	}
	
	
	public void viewUploadedDocumentDetails(String intimationNo) {		
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
	}
	
	
	private Policy populatePolicyFromTmpPolicy(PremPolicyDetails premPolicyDetails) {
		//tmpPolicy = policyService.findTmppolicyByPolicyNo(tmpPolicy.getPolNo());

		
		premiaPolicyMapper.getAllMapValues();
		Policy policy = policyService.getPolicy(premPolicyDetails.getPolicyNo());

		if (null == policy) {
		 policy = premiaPolicyMapper.getPolicyFromPremia(premPolicyDetails);
		 
		 Double totalAmount = 0d;
		 totalAmount += policy.getGrossPremium() != null ? policy.getGrossPremium() : 0d;
		 totalAmount += policy.getPremiumTax() != null ? policy.getPremiumTax() : 0d;
		 totalAmount += policy.getStampDuty() != null ? policy.getStampDuty() : 0d;
		 policy.setTotalPremium(totalAmount);
		 
		 if(premPolicyDetails.getPolSysId() != null){
			 Long polSysId = SHAUtils.getLongFromString(premPolicyDetails.getPolSysId());
	
			 policy.setPolicySystemId(polSysId);
		 }
		 
		 if(premPolicyDetails.getPolicyPlan() != null){
			 if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_A)){
				 policy.setPolicyPlan("A");
			 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_B)){
				 policy.setPolicyPlan("B");
			 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD)
					 || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_GOLD_NEW)){
				 policy.setPolicyPlan("G");
			 }else if(premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER) || premPolicyDetails.getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLAN_SILVER_SL)){
				 policy.setPolicyPlan("S");
			 }
		 }
		 
		 if(premPolicyDetails.getPolicyZone() != null){
			 policy.setPolicyZone(premPolicyDetails.getPolicyZone());
		 }
		 
		 String policyStrYear = SHAUtils.getTruncateWord(policy.getPolicyNumber(), 12, 16);
		 if(policyStrYear != null){
			 policy.setPolicyYear(SHAUtils.getLongFromString(policyStrYear));
		 }
		 
		 if(premPolicyDetails.getPolicyTerm() != null) {
			 // Below Cond for SCRC - MED-PRD-070 - R201811302
			 if(premPolicyDetails.getProductCode() != null && ReferenceTable.SENIOR_CITIZEN_REDCARPET_REVISED.equals(premPolicyDetails.getProductCode())){
				 if(premPolicyDetails.getPolicyTerm() != null && !premPolicyDetails.getPolicyTerm().isEmpty()) {
					 String policyTermYear[] = premPolicyDetails.getPolicyTerm().split(" ");
					 String policyTerm = policyTermYear[0];
					 policy.setPolicyTerm(SHAUtils.getLongFromString(policyTerm));
				 }
			 }
			 else  if(!"".equalsIgnoreCase(premPolicyDetails.getPolicyTerm())) {
				 Long policyTerm = SHAUtils.getLongFromString(premPolicyDetails.getPolicyTerm());
				 policy.setPolicyTerm(policyTerm);
			 } else if(premPolicyDetails.getProductCode() != null && ReferenceTable.STAR_UNIQUE_PRODUCT_CODE.equals(premPolicyDetails.getProductCode())) {
				 policy.setPolicyTerm(2l);
			 }
		 }
		 
		 if( (premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true))
		 policy.setCreatedDate(new Date(premPolicyDetails.getPolicyStartDate()));
		 
		 if(premPolicyDetails.getPolicyStartDate().equals("") || premPolicyDetails.getPolicyStartDate().isEmpty() ? false : true)
			 policy.setPolicyFromDate(new Date(premPolicyDetails.getPolicyStartDate()));
		 
		 if(premPolicyDetails.getPolicyEndDate().equals("") || premPolicyDetails.getPolicyEndDate().isEmpty() ? false : true)
		 policy.setPolicyToDate(new Date(premPolicyDetails.getPolicyEndDate()));
		 
		 if(premPolicyDetails.getReceiptDate().equals("") || premPolicyDetails.getReceiptDate().isEmpty() ? false : true)
		 policy.setReceiptDate(new Date(premPolicyDetails.getReceiptDate()));
		 
		 if(premPolicyDetails.getProposerDOB().equals("") || premPolicyDetails.getProposerDOB().isEmpty() ? false : true)
			 policy.setProposerDob(new Date(premPolicyDetails.getProposerDOB()));
		 
		 if(null != masterService.getMasterByValue(premPolicyDetails.getLob()))
		 policy.setLobId(masterService.getMasterByValue(premPolicyDetails.getLob()).getKey());
		 
		 if(null != masterService.getMasterByValue(premPolicyDetails.getPolicyType()))
		 policy.setPolicyType(masterService.getMasterByValueAndMasList(premPolicyDetails.getPolicyType(),ReferenceTable.POLICY_TYPE));
		 
		 if(premPolicyDetails.getSchemeType() != null && premPolicyDetails.getSchemeType().length() > 0){
			 
			 MastersValue schemeId = masterService.getMasterByValue(premPolicyDetails.getSchemeType());
		
			 policy.setSchemeId(schemeId != null ? schemeId.getKey() : null);
		 }
		 
		//TODO:Get product type from premia 
//		 if(null != masterService.getMasterByValue(premPolicyDetails.getProductName()))
//		 policy.setProductType(masterService.getMasterByValue(premPolicyDetails.getProductName()));
		 

		 policy.setProduct((policyService.getProductByProductCode(premPolicyDetails.getProductCode())));
		 policy.setProductType(masterService.getMasterByValueAndMasList(policy.getProduct().getProductType(),ReferenceTable.PRODUCT_TYPE));
		 
		 List<PolicyEndorsementDetails> endorsementDetailsList = premiaPolicyMapper .getPolicyEndorsementDetailsFromPremia(premPolicyDetails.getEndorsementDetails());
	
		 List<PreviousPolicy> previousPolicyList = premiaPolicyMapper.getPreviousPolicyDetailsFromPremia(premPolicyDetails.getPreviousPolicyDetails());
		 //set Endrosement Date
		 for(int index = 0;index<premPolicyDetails.getEndorsementDetails().size(); index++ ){
			 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt().isEmpty() ? false : true){
			 endorsementDetailsList.get(index).setEffectiveFromDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
			 endorsementDetailsList.get(index).setEndoresementDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffFmDt()));
			 }
			 if(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().equals("") || premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt().isEmpty() ? false : true){
				 endorsementDetailsList.get(index).setEffectiveToDate(new Date(premPolicyDetails.getEndorsementDetails().get(index).getEndEffToDt()));
				 }
		 }
		 //Set previous policy Date
		 for(int index = 0;index<premPolicyDetails.getPreviousPolicyDetails().size(); index++ ){
			 if(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate().isEmpty() ? false : true){
				 previousPolicyList.get(index).setPolicyFrmDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyFromDate()));
			 }
			 if(null == premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()||premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().equals("") || premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate().isEmpty() ? false : true){
				 previousPolicyList.get(index).setPolicyToDate(new Date(premPolicyDetails.getPreviousPolicyDetails().get(index).getPolicyToDate()));
			 }
		 }
		 
		 List<Insured> insured = premiaPolicyMapper.getInsuredFromPremia(premPolicyDetails.getInsuredDetails());
		

		 for(int index = 0; index < premPolicyDetails.getInsuredDetails().size(); index++){
			MastersValue genderMaster =  masterService.getKeyByValue(premPolicyDetails.getInsuredDetails().get(index).getGender());
			 insured.get(index).setInsuredGender(genderMaster);
			
			 if(premPolicyDetails.getInsuredDetails().get(index).getDob().equals("") || premPolicyDetails.getInsuredDetails().get(index).getDob().isEmpty() ? false : true){
				 
				 Date formatPremiaDate = SHAUtils.formatPremiaDate(premPolicyDetails.getInsuredDetails().get(index).getDob());
				 if(formatPremiaDate != null) {
					 
					 insured.get(index).setInsuredDateOfBirth(formatPremiaDate);

				 }
				 
			 }
			 Double insuredAge = SHAUtils.getDoubleValueFromString(premPolicyDetails.getInsuredDetails().get(index).getInsuredAge());
	 		 
	 		 if(insuredAge != null){
	 			 insured.get(index).setInsuredAge(insuredAge);
	 			 } 

		 }
		 
			 for(int index = 0 ; index < premPolicyDetails.getInsuredDetails().size(); index++){
				 insured.get(index).setInsuredPedList(premiaPolicyMapper.getInsuredPedFromPremia(premPolicyDetails.getInsuredDetails().get(index).getPedDetails()));
				 insured.get(index).setNomineeDetails(premiaPolicyMapper.getInsuredNomineeDetails(premPolicyDetails.getInsuredDetails().get(index).getNomineeDetails()));
				 
				 insured.get(index).setProposerInsuredNomineeDetails(premiaPolicyMapper.getProposerInsuredNomineeDetails(premPolicyDetails.getInsuredDetails().get(index).getNomineeDetails()));
		 }
			 
			   List<PremInsuredNomineeDetails> premProposerNomineeDetails = premPolicyDetails.getProperNomineeDetails();
		        
		        if(premProposerNomineeDetails != null && !premProposerNomineeDetails.isEmpty()){
		        	List<PolicyNominee> proposerNomineeDetails = premiaPolicyMapper.getProposerInsuredNomineeDetails(premProposerNomineeDetails);
		        	for (PolicyNominee proposerNominee : proposerNomineeDetails) {
						if(proposerNominee.getStrNomineeDOB() != null && !proposerNominee.getStrNomineeDOB().isEmpty()){
							proposerNominee.setNomineeDob(new Date(proposerNominee.getStrNomineeDOB()));
						}
					}
		        	
		        	policy.setProposerNomineeDetails(proposerNomineeDetails);
		        	
		        }
			 
			 
			 
		List<PremInsuredDetails> premiaInsured = premPolicyDetails.getInsuredDetails();
			 
		int i=0;
		InsuredPedDetails selfDeclaredPed = null;
		List<InsuredPedDetails> pedList = null ;
		for (Insured insured2 : insured) {
			
			if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
				Double totalSumInsured = policy.getTotalSumInsured();
				Double size = Double.valueOf(insured.size());
				Double sumInsured = 0d;
				if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_29)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_06)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_62)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_79)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_82)
						|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PROD_PAC_PRD_013)){
					if(totalSumInsured != null && ! totalSumInsured.equals(0d)){
						sumInsured = totalSumInsured/size;
					}
					Long roundOfSI = Math.round(sumInsured);
					insured2.setInsuredSumInsured(Double.valueOf(roundOfSI));
				}else{
					insured2.setInsuredSumInsured(policy.getTotalSumInsured());	
				}
				
//				policy.setProductType(masterService.getMaster(ReferenceTable.FLOATER_POLICY));
				
			}else{
//				policy.setProductType(masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY));
			}
			
			/*Below code commented - record inserting two times 
			if(premiaInsured.get(i).getSelfDeclaredPed() != null && premiaInsured.get(i).getSelfDeclaredPed().length() >0){
				selfDeclaredPed = new InsuredPedDetails();
				selfDeclaredPed.setInsuredKey(insured2.getInsuredId());
				selfDeclaredPed.setPedDescription(premiaInsured.get(i).getSelfDeclaredPed());
				pedList = new ArrayList<InsuredPedDetails>();
				pedList.add(selfDeclaredPed);
				insured2.setInsuredPedList(pedList);
			}*/
			i++;
					
		}
		
		

	    policy.setInsured(insured);
	    
		 if(premPolicyDetails.getBankDetails() != null && !premPolicyDetails.getBankDetails().isEmpty()){
	        	List<PolicyBankDetails> bankDetailsFromPremia = premiaPolicyMapper.getBankDetailsFromPremia(premPolicyDetails.getBankDetails());
	        	for (PolicyBankDetails policyBankDetails : bankDetailsFromPremia) {
					if(policyBankDetails.getStrEffectiveFrom() != null && ! policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("") && !policyBankDetails.getStrEffectiveFrom().equalsIgnoreCase("null") ){
						policyBankDetails.setEffectiveFrom(new Date(policyBankDetails.getStrEffectiveFrom()));
					}
					if(policyBankDetails.getStrEffectiveTo() != null && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("") && ! policyBankDetails.getStrEffectiveTo().equalsIgnoreCase("null")){
						policyBankDetails.setEffectiveTo(new Date(policyBankDetails.getStrEffectiveTo()));
					}
				}
	        	policy.setPolicyBankDetails(bankDetailsFromPremia);
	        }
	    
        if(premPolicyDetails.getProductCode() != null && premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_22)
        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_35) || 
        		premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_17)
        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_70)
        		|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_42)
	        	|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_44)
	        	|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.STAR_MICRO_RORAL_AND_FARMERS_CARE)
	        	|| premPolicyDetails.getProductCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_77)){
        	
        	 if(premPolicyDetails.getPolType() != null && premPolicyDetails.getPolType().equalsIgnoreCase(SHAConstants.PREMIA_POLTYPE_FLOATER)){
     	    	
        		 MastersValue productType = masterService.getMaster(ReferenceTable.FLOATER_POLICY);
        		 policy.setProductType(productType);
        		 
        		 Product product = masterService.getProductByCodeAndType(premPolicyDetails.getProductCode(), productType.getValue());
        		 if(product != null){
        			 policy.setProduct(product);
        		 }
        		 
     	    }else{
     	    	
     	    	 MastersValue productType = masterService.getMaster(ReferenceTable.INDIVIDUAL_POLICY);
        		 policy.setProductType(productType);
        		 
        		 Product product = masterService.getProductByCodeAndType(premPolicyDetails.getProductCode(), productType.getValue());
        		 if(product != null){
        			 policy.setProduct(product);
        		 }
     	    	
     	    }
        	
        }
	   
		policyService.create(policy, previousPolicyList, endorsementDetailsList);
		
		 policy = policyService.getPolicy(premPolicyDetails.getPolicyNo());
		}
		return policy;
	}
	
}
