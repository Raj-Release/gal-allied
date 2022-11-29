package com.shaic.claim.fvrgrading.search;

import java.util.Calendar;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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

public class SearchFvrReportGradingPage extends ViewComponent {

	/**
	 * 
	 */
	
	@Inject
	private SearchFvrReportGradingTable searchResultTable;	
	
	@EJB
	private PreauthService preauthService;
	
	private static final long serialVersionUID = 1L;
	
	private TextField txtIntimationNo;
	
	private TextField txtPolicyNo;
	
	private ComboBox cmbClaimType;
	
	private DateField fromDate;
	
	private DateField toDate;
	
	private ComboBox cpuCodeCmb;
	
	private Button convertClaimSearchBtn;
	
	private Button resetBtn;
	
	private BeanFieldGroup<SearchFvrReportGradingFormDto> binder;
	
	private VerticalLayout buildConvertClaimSearchLayout;

	private Searchable searchable;
	
	public void addSearchListener(Searchable searable) {
		this.searchable = searable;
	}
	
	public SearchFvrReportGradingFormDto getSearchDTO() {
		try {
			
			this.binder.commit();
			SearchFvrReportGradingFormDto bean = this.binder.getItemDataSource()
					.getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@PostConstruct
	public void init()
	{
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildConvertClaimSearchLayout  = new VerticalLayout();
		Panel convertClaimPanel	= new Panel();
		//Vaadin8-setImmediate() convertClaimPanel.setImmediate(false);
		convertClaimPanel.setWidth("100%");
//		convertClaimPanel.setHeight("50%");
		convertClaimPanel.setCaption("Field Visit Report Grading");
		convertClaimPanel.addStyleName("panelHeader");
		convertClaimPanel.addStyleName("g-search-panel");
		convertClaimPanel.setContent(buildConvertClaimLayout());
		buildConvertClaimSearchLayout.addComponent(convertClaimPanel);
		buildConvertClaimSearchLayout.setComponentAlignment(convertClaimPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildConvertClaimSearchLayout);
		addListener();
	}
	
	/*public void init(BeanItemContainer<TmpCPUCode> parameter) {
		initBinder();
		this.cpuCodeValues = parameter;
		VerticalLayout buildConvertClaimForm = buildConvertClaim();
		buildConvertClaimForm.setWidth("100%");
		buildConvertClaimForm.setHeight("100%");
		buildConvertClaimForm.setSpacing(true);
		buildConvertClaimForm.setMargin(true);
		Panel preauthPanel = new Panel("Convert Claim type to Reimbursement");
		preauthPanel.setContent(buildConvertClaimForm);
		setCompositionRoot(preauthPanel);		
		addListener();
	}*/
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchFvrReportGradingFormDto>(
				SearchFvrReportGradingFormDto.class);
		this.binder
				.setItemDataSource(new SearchFvrReportGradingFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildConvertClaimLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("230px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNumber",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNumber",
				TextField.class);
		cpuCodeCmb = binder.buildAndBind("CPU Code", "cpuCode",
				ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		fromDate = binder.buildAndBind("From Date","fromDate",DateField.class);
		
		toDate = binder.buildAndBind("To Date","toDate",DateField.class);
		
		/*FormLayout formLayout1 = new FormLayout(txtIntimationNo,cpuCodeCmb,cmbType,cmbSource);
		FormLayout formLayout2 = new FormLayout( txtPolicyNo,cmbPriority);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2);*/
		//searchFormLayout.setComponentAlignment(formLayout1, Alignment.MIDDLE_RIGHT);
		//searchFormLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_LEFT);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo,cpuCodeCmb,fromDate);
		FormLayout formLayoutMiddle = new FormLayout(txtPolicyNo,cmbClaimType,toDate);		
		//FormLayout formLayoutRight = new FormLayout(cmbType,cmbSource);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayoutLeft,formLayoutMiddle);

		//HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo,cpuCodeCmb),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(false);
		searchFormLayout.setWidth("100%");
		absoluteLayout_3.addComponent(searchFormLayout,"top:-20.0px;left:0.0px;");
		
		convertClaimSearchBtn = new Button();
		convertClaimSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		convertClaimSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		convertClaimSearchBtn.setWidth("-1px");
		convertClaimSearchBtn.setHeight("-10px");
		absoluteLayout_3
		.addComponent(convertClaimSearchBtn, "top:120.0px;left:220.0px;");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:120.0px;left:329.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("990px");
		verticalLayout.setHeight("150px");
		
		setDropDownValues();
		
		return verticalLayout; 
		


	}

	public void addListener() {

		convertClaimSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(!validatePage()){
					searchable.doSearch();
					searchResultTable.tablesize();	
				}
				
//				fireViewEvent(MenuPresenter.SHOW_CONVERT_CLAIM, tableDto);      //to testing purpose by yosuva
				
				
			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//searable.doSearch();
				resetAlltheValues();
			}
		});
	}
	
	public void setCpuCode(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> claimType){		
		cpuCodeCmb.setContainerDataSource(parameter);
		cpuCodeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cpuCodeCmb.setItemCaptionPropertyId("value");
		
		cmbClaimType.setContainerDataSource(claimType);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
		parameter.sort(new Object[] {"value"}, new boolean[] {true});
		
	}
	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildConvertClaimSearchLayout.iterator();
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
							Component absoluteComponent = vLayoutIter.next();
							AbsoluteLayout absLayout = (AbsoluteLayout)absoluteComponent;
							Iterator<Component> absLayoutIter = absLayout.iterator();
							while(absLayoutIter.hasNext())
							{
								Component horizontalComp = absLayoutIter.next();
								if(horizontalComp instanceof HorizontalLayout)
								{
									HorizontalLayout hLayout = (HorizontalLayout)horizontalComp;
									Iterator<Component> formLayComp = hLayout.iterator();
									while(formLayComp.hasNext())
									{
										Component formComp = formLayComp.next();
										FormLayout fLayout = (FormLayout)formComp;
										Iterator<Component> formComIter = fLayout.iterator();
									
										while(formComIter.hasNext())
										{
											Component indivdualComp = formComIter.next();
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
													field.setValue(null);
												}else if(indivdualComp instanceof DateField){
													DateField field = (DateField) indivdualComp;
													field.setValue(null);
													
												}
									// Remove the table if exists..	
									//removeTableFromLayout();
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

	public void setDropDownValues(){
	
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
	
	private Boolean validatePage(){
		Boolean hasError = false;
		try {			
				StringBuffer errorMsg = new StringBuffer();			
				
				if(fromDate.getValue() != null && toDate.getValue() != null){
					if((fromDate.getValue().getYear() - toDate.getValue().getYear() == 0)){
						if(fromDate.getValue().getMonth() - toDate.getValue().getMonth() > 1){
							errorMsg.append("The From and To date range should not Exceed 30 Days.<br>");
							hasError = true;
						}else if(toDate.getValue().getMonth() - fromDate.getValue().getMonth() == 1) {
							Calendar lastYearDate = Calendar.getInstance();
							lastYearDate.setTime(fromDate.getValue());
								int lastYearDays = lastYearDate.getMaximum(Calendar.DAY_OF_MONTH) - fromDate.getValue().getDate()+1;
								int curYearDays = toDate.getValue().getDate();
								int totalDays = lastYearDays + curYearDays;
								if(totalDays < 0 ||  totalDays > 30){
									errorMsg.append("The From and To date range should not Exceed 30 Days.<br>");
									hasError = true;
							    }
					}else if(toDate.getValue().getMonth() - fromDate.getValue().getMonth() > 1) {
						errorMsg.append("The From and To date range should not Exceed 30 Days.<br>");
						hasError = true;
					}else if(toDate.getValue().getMonth() - fromDate.getValue().getMonth() < 0) {
						errorMsg.append("The From and To date range should not Exceed 30 Days.<br>");
						hasError = true;
					}
						
					}else if(toDate.getValue().getYear() - fromDate.getValue().getYear() == 1 ){
						if(fromDate.getValue().getMonth() - toDate.getValue().getMonth() == 11 ){
							
							Calendar lastYearDate = Calendar.getInstance();
							lastYearDate.setTime(fromDate.getValue());
								int lastYearDays = lastYearDate.getMaximum(Calendar.DAY_OF_MONTH) - fromDate.getValue().getDate()+1;
								int curYearDays = toDate.getValue().getDate();
								int totalDays = lastYearDays + curYearDays;
								if(totalDays < 0 ||  totalDays > 30){
									errorMsg.append("The From and To date range should not Exceed 30 Days.<br>");
									hasError = true;
							    }
						}
					}else{
						errorMsg.append("The From and To date range should not Exceed 30 Days.<br>");
						hasError = true;
					}
							
				}else{
					if((txtIntimationNo != null && txtIntimationNo.getValue() != null && txtIntimationNo.getValue().isEmpty()) && (txtPolicyNo != null && txtPolicyNo.getValue() != null && txtPolicyNo.getValue().isEmpty()) && (cmbClaimType != null && cmbClaimType.getValue() == null) && (cpuCodeCmb != null && cpuCodeCmb.getValue() == null))
					{errorMsg.append("The From and To date should be mandatory.<br>");
					hasError = true;}
				}
				
							
				if(hasError){
					showErrorMsg(errorMsg.toString());
					
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasError;
	}
	
	public void showErrorMsg(String errorMessage) {
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
}
