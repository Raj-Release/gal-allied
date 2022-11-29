package com.shaic.claim.policy.search.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
@SuppressWarnings("serial")
public class SearchNewPolicyUIForm extends ViewComponent 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BeanFieldGroup<NewSearchPolicyFormDTO> binder;
	
	private ComboBox cmbSearchBy;
	private CheckBox chkFromPremia;
	private TextField txtPolicyNumber;
	private TextField txtHealthCardNo;
	private TextField txtReceiptNumber;
	private TextField txtRegisteredMobileNumber;
	private TextField txtProposerName;
	private TextField txtInsuredName;
	private DateField dateOfBirth;
	private ComboBox cmbProductName;
	private ComboBox cmbProductType;
	private ComboBox cmbPolicyIssuingOffCodeName;
	
	private Button policySearchBtn;
	private Button resetBtn;
	
	private VerticalLayout buildPolicySearchLayout;
	private  VerticalLayout verticalLayout;
	private  HorizontalLayout mainLayout;
	HorizontalLayout proposerOrInsuredLayout;
	HorizontalLayout policyLayout;
	private Searchable searchable;

	
	public void addSearchListener(Searchable searable)
	{
		this.searchable = searable;
	}
	
	
	public NewSearchPolicyFormDTO getSearchDTO()
	{
		try 
		{
			this.binder.commit();
			NewSearchPolicyFormDTO bean = this.binder.getItemDataSource().getBean();
			return  bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@PostConstruct
	public void init() {
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildPolicySearchLayout  = new VerticalLayout();
		Panel policySearchPanel	= new Panel();
		//Vaadin8-setImmediate() policySearchPanel.setImmediate(false);
		policySearchPanel.setWidth("60%");
		policySearchPanel.setHeight("50%");
		policySearchPanel.setCaption("Create Intimation - Search Policy");
		policySearchPanel.addStyleName("panelHeader");
		policySearchPanel.setContent(buildSearchPolicyLayout());
		buildPolicySearchLayout.addComponent(policySearchPanel);
		buildPolicySearchLayout.setComponentAlignment(policySearchPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildPolicySearchLayout);
		addListener();
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<NewSearchPolicyFormDTO>(NewSearchPolicyFormDTO.class);
		this.binder.setItemDataSource(new NewSearchPolicyFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	
	private VerticalLayout buildSearchPolicyLayout() {
		
		// AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth(20,UNITS_CM);
		 verticalLayout.setMargin(false);		 
		 
		cmbSearchBy     =  binder.buildAndBind("Search By", "searchBy", ComboBox.class);
		chkFromPremia   =  binder.buildAndBind(">From Premia", "fromPremia", CheckBox.class);
		
		chkFromPremia.setValue(false);
		
		txtPolicyNumber = binder.buildAndBind("Policy Number", "policyNo", TextField.class);
		CSValidator validator = new CSValidator();
		validator.extend(txtPolicyNumber);
		validator.setRegExp("^[a-zA-Z 0-9/]*$");
		validator.setPreventInvalidTyping(true);
		
		txtHealthCardNo = binder.buildAndBind("Health Card No", "healthCardNumber", TextField.class);
		CSValidator healthCardValidator = new CSValidator();
		healthCardValidator.extend(txtHealthCardNo);
		healthCardValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		healthCardValidator.setPreventInvalidTyping(true);
		
		txtReceiptNumber = binder.buildAndBind("Receipt Number", "policyReceiptNo", TextField.class);
		CSValidator receiptValidator = new CSValidator();
		receiptValidator.extend(txtReceiptNumber);
		receiptValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		receiptValidator.setPreventInvalidTyping(true);
		
		txtRegisteredMobileNumber =  binder.buildAndBind("Registered Mobile Number", "registerdMobileNumber", TextField.class);
		CSValidator mobileValidator=new CSValidator();
		mobileValidator.extend(txtRegisteredMobileNumber);
		mobileValidator.setRegExp("^[0-9/]*$");
		mobileValidator.setPreventInvalidTyping(true);
		
		cmbProductName = binder.buildAndBind("Product Name", "productName", ComboBox.class);
		cmbProductType = binder.buildAndBind("Product Type", "productType",ComboBox.class);
		cmbPolicyIssuingOffCodeName = binder.buildAndBind("Policy Issuing Off-code / Name", "polhDivnCode",ComboBox.class);
		
		txtProposerName = binder.buildAndBind("Proposer Name", "polAssrName",TextField.class);
		
		CSValidator nameValidator = new CSValidator();
		nameValidator.extend(txtProposerName);
		nameValidator.setRegExp("^[a-zA-Z 0-9. /']*$");
		nameValidator.setPreventInvalidTyping(true);
		
		txtInsuredName = binder.buildAndBind("Insured Name", "insuredName",TextField.class);
		CSValidator insuredNameValidator = new CSValidator();
		insuredNameValidator.extend(txtInsuredName);
		insuredNameValidator.setRegExp("^[a-zA-Z 0-9./s ']*$");
		insuredNameValidator.setPreventInvalidTyping(true);
		
		dateOfBirth = binder.buildAndBind("DOB", "insuredDateOfBirth" , DateField.class);
		
		policySearchBtn = new Button();
		policySearchBtn.setCaption("Search");
		//Vaadin8-setImmediate() policySearchBtn.setImmediate(true);
		policySearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		policySearchBtn.setWidth("-1px");
		policySearchBtn.setHeight("-10px");
		policySearchBtn.setDisableOnClick(true);
	
		//Vaadin8-setImmediate() policySearchBtn.setImmediate(true);
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		
		FormLayout fl1 = new FormLayout(cmbSearchBy);
		FormLayout fl2 = new  FormLayout(chkFromPremia);
		HorizontalLayout criteriaLayout = new HorizontalLayout(fl1,fl2);
		//criteriaLayout.setMargin(true);
		criteriaLayout.setComponentAlignment(fl1, Alignment.BOTTOM_LEFT);
		criteriaLayout.setComponentAlignment(fl2, Alignment.BOTTOM_LEFT);
		//criteriaLayout.setWidth("90%");
		criteriaLayout.setHeight(2,UNITS_CM);
		
		FormLayout formLayout1 = new FormLayout(txtPolicyNumber,txtReceiptNumber,cmbProductName,cmbPolicyIssuingOffCodeName);
		FormLayout formLayout2 = new FormLayout( txtHealthCardNo,txtRegisteredMobileNumber,cmbProductType);
		HorizontalLayout searchPolicyLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
		searchPolicyLayout.setComponentAlignment(formLayout1, Alignment.BOTTOM_LEFT);
		searchPolicyLayout.setComponentAlignment(formLayout2, Alignment.TOP_LEFT);
		//searchPolicyLayout.setMargin(true);
		//searchPolicyLayout.setWidth("110%");
		searchPolicyLayout.setHeight(36,UNITS_MM);
		searchPolicyLayout.setSpacing(true);
			
		HorizontalLayout hButtonLayout = new HorizontalLayout(policySearchBtn , resetBtn);
		hButtonLayout.setSpacing(true);
		hButtonLayout.setMargin(true);
		
		FormLayout proposerFormLayout1 = new FormLayout(txtProposerName,dateOfBirth);
		FormLayout proposerFormLayout2 = new FormLayout(txtRegisteredMobileNumber,txtInsuredName);
		
		HorizontalLayout searchProposerOrInsurerLayout = new HorizontalLayout(proposerFormLayout1 , proposerFormLayout2) ;
		//searchProposerOrInsurerLayout.setMargin(true);
		//searchProposerOrInsurerLayout.setWidth("90%");
		searchProposerOrInsurerLayout.setComponentAlignment(proposerFormLayout1,Alignment.BOTTOM_RIGHT);
		searchProposerOrInsurerLayout.setComponentAlignment(proposerFormLayout2,Alignment.BOTTOM_RIGHT);
		searchProposerOrInsurerLayout.setHeight(20,UNITS_MM);
	
		verticalLayout.addComponent(criteriaLayout);
		verticalLayout.addComponent(searchPolicyLayout);
		verticalLayout.addComponent(searchProposerOrInsurerLayout);
		verticalLayout.addComponent(hButtonLayout);
		verticalLayout.setComponentAlignment(criteriaLayout, Alignment.MIDDLE_CENTER);
		verticalLayout.setComponentAlignment(searchPolicyLayout, Alignment.MIDDLE_RIGHT);
		verticalLayout.setComponentAlignment(searchProposerOrInsurerLayout, Alignment.MIDDLE_CENTER);
		verticalLayout.setComponentAlignment(hButtonLayout, Alignment.MIDDLE_CENTER);
		//verticalLayout.setSpacing(false);
		/*HorizontalLayout hlayout = new HorizontalLayout(verticalLayout);
		hlayout.setMargin(true);
		 mainLayout = new VerticalLayout(hlayout);
		*/
		 
		return verticalLayout; 
	}
	
	
	public void setSearchByDropDown (BeanItemContainer<SelectValue> parameter){		
		
		cmbSearchBy.setContainerDataSource(parameter);
		cmbSearchBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSearchBy.setItemCaptionPropertyId("value");
		Collection<?> itemIds = cmbSearchBy.getContainerDataSource().getItemIds();
		cmbSearchBy.setValue(itemIds.toArray()[0]);
		cmbSearchBy.setNullSelectionAllowed(false);
		
	}
	
	public void setProductNameDropDown (BeanItemContainer<SelectValue> parameter)
	{
		cmbProductName.setContainerDataSource(parameter);
		cmbProductName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductName.setItemCaptionPropertyId("value");
	}
	
	public void setProductTypeDropDown(BeanItemContainer<SelectValue> parameter)
	{
		cmbProductType.setContainerDataSource(parameter);
		cmbProductType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductType.setItemCaptionPropertyId("value");
	}
	
	public void setPolicyIssueOfficeCodeDropDown(BeanItemContainer<SelectValue> parameter)
	{
		cmbPolicyIssuingOffCodeName.setContainerDataSource(parameter);
		cmbPolicyIssuingOffCodeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPolicyIssuingOffCodeName.setItemCaptionPropertyId("value");
	}
	
	private void setValueForSearchByCombox()
	{
		
	}
	
	public void addListener() {
		
		cmbSearchBy.addValueChangeListener(new ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				if(value != null && value.getValue() != null && ("Proposer/Insured").equals(value.getValue().toString()))
				{
					showProposerOrInsuredLayout();
				}
				else
				{
					showPolicyLayout();
				}
				
			}
		});

		policySearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				policySearchBtn.setEnabled(true);
				//TODO values for all the child dto s inside preauthDto must be set b4 calling detail page
				searchable.doSearch();

			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();
			}
		});
	}
	
	private void showPolicyLayout()
	{		
		verticalLayout.getComponent(1).setVisible(true);
		verticalLayout.getComponent(2).setVisible(false);
		/*proposerOrInsuredLayout = null;
		FormLayout formLayout1 = new FormLayout(cmbSearchBy, txtPolicyNumber,txtReceiptNumber,cmbProductName,cmbPolicyIssuingOffCodeName);
		FormLayout formLayout2 = new FormLayout(chkFromPremia, txtHealthCardNo,txtRegisteredMobileNumber,cmbProductType);
		policyLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
		policyLayout.setMargin(true);
		policyLayout.setWidth("100%");
		return proposerOrInsuredLayout;*/
		
	}
	
	private void showProposerOrInsuredLayout()
	{
		verticalLayout.getComponent(2).setVisible(true);
		verticalLayout.getComponent(1).setVisible(false);
	}
		

	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildPolicySearchLayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component searchScrnComponent = componentIterator.next() ;
				if(searchScrnComponent instanceof  Panel )
				{	
					Panel panel = (Panel)searchScrnComponent;
					Iterator<Component> searchScrnCompIter = panel.iterator();
					while (searchScrnCompIter.hasNext())
					{
						Component verticalLayoutComp = searchScrnCompIter.next();
						VerticalLayout vLayout = (VerticalLayout)verticalLayoutComp;
						Iterator<Component> vLayoutIter = vLayout.iterator();
						while(vLayoutIter.hasNext())
						{
							Component horizontalComponent = vLayoutIter.next();
							HorizontalLayout absLayout = (HorizontalLayout)horizontalComponent;
							Iterator<Component> absLayoutIter = absLayout.iterator();
							while(absLayoutIter.hasNext())
							{
								Component formLayoutComp = absLayoutIter.next();
								if(formLayoutComp instanceof FormLayout)
								{
									FormLayout hLayout = (FormLayout)formLayoutComp;
									Iterator<Component> formLayComp = hLayout.iterator();
									while(formLayComp.hasNext())
									{
										Component indivdualComp = formLayComp.next();
										if(indivdualComp != null) 
										{
											if(indivdualComp instanceof Label) 
											{
												continue;
											}	
											if(indivdualComp instanceof TextField) 
											{
												TextField field = (TextField) indivdualComp;
												field.setValue("");
											} 
											else if(indivdualComp instanceof ComboBox)
											{
												ComboBox field = (ComboBox) indivdualComp;
												if(!field.getCaption().equals("Search By"))
												{
													field.setValue(null);
												}
											}	
											else if(indivdualComp instanceof DateField)
											{
												DateField field = (DateField) indivdualComp;
												field.setValue(null);
											}	
										}
									}
								}
							}
						}
					}
				}
				//Method to reset search table.
				removeTableFromLayout();
			}
		}


	
	private void removeTableFromLayout()
	{
		if(null != searchable)
		{
			searchable.resetSearchResultTableValues();
		}
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}

}


