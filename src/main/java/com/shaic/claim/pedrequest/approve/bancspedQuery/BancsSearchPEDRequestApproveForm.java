package com.shaic.claim.pedrequest.approve.bancspedQuery;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterFormDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class BancsSearchPEDRequestApproveForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private BancsSearchPEDRequestApproveTable searchPEDRequestApproveTable;

	private BeanFieldGroup<BancsSearchPEDRequestApproveFormDTO> binder;

	TextField txtIntimationNo;

	TextField txtPolicyNo;
	
	private ComboBox cmbType;
	
	private ComboBox cmbPedStatus;
	
	private ComboBox cmbPriority;
	
//	private ComboBox cmbPriorityNew;
	
	private Label dummyLbl;
	
	private Label priority;
	
	private CheckBox chkAll;
	
	private CheckBox chkCRM;
	
	private CheckBox chkVIP;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbPedSuggestion;
	
	private ComboBox cmbClaimType;
	
	private PopupDateField fromDate;
	
	private PopupDateField todate;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	

	Button pedappSearchBtn;
	
	private Searchable searchable;
	
	Button resetBtn;
	
	private ComboBox cbxCPUCode;
	
	
	private VerticalLayout buildPedRequestProcessApproveSearchLayout ;

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public BancsSearchPEDRequestApproveFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			BancsSearchPEDRequestApproveFormDTO bean = this.binder
					.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostConstruct
	public void init() {
		initBinder();
		//buildPreauthSearchLayout = buildPreauthSearchLayout();
		buildPedRequestProcessApproveSearchLayout  = new VerticalLayout();
		Panel processPEDRequestApprove	= new Panel();
		//Vaadin8-setImmediate() processPEDRequestApprove.setImmediate(false);
		//processPEDRequestApprove.setWidth("850px");
		processPEDRequestApprove.setWidth("100%");
		processPEDRequestApprove.setHeight("50%");
		processPEDRequestApprove.setCaption("Process PED Query Request");
		processPEDRequestApprove.addStyleName("panelHeader");
		processPEDRequestApprove.addStyleName("g-search-panel");
		processPEDRequestApprove.setContent(buildPedRequestProcessLayout());
		buildPedRequestProcessApproveSearchLayout.addComponent(processPEDRequestApprove);
		buildPedRequestProcessApproveSearchLayout.setComponentAlignment(processPEDRequestApprove, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildPedRequestProcessApproveSearchLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<BancsSearchPEDRequestApproveFormDTO>(
				BancsSearchPEDRequestApproveFormDTO.class);
		this.binder
				.setItemDataSource(new BancsSearchPEDRequestApproveFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildPedRequestProcessLayout()
	{
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
////	 absoluteLayout_3.setHeight("178px");
		 absoluteLayout_3.setHeight("178px");
////	 absoluteLayout_3.setHeight("150px");

		 
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		
		/*cmbPriority = binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
		
//		cmbPriorityNew = binder.buildAndBind("Priority","priorityNew",ComboBox.class);
		
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbPedSuggestion = binder.buildAndBind("Ped Type","pedSuggestion",ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		cmbPedStatus  = binder.buildAndBind("PED Status","pedStatus",ComboBox.class);
		
		SearchPEDRequestApproveFormDTO dto = this.binder.getItemDataSource().getBean();
		
		dto.setPedStatus(pedStatusselect1);
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		fromDate = binder.buildAndBind("From Date","fromDate",PopupDateField.class);
		fromDate.setDateFormat("dd/MM/yyyy");
		
		todate = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		todate.setDateFormat("dd/MM/yyyy");
		
		dummyLbl = new Label();
		dummyLbl.setEnabled(false);
		
		priority = new Label();
		priority.setCaption("Priority");
		
		chkAll = binder.buildAndBind("All","priorityAll",CheckBox.class);
		chkAll.setValue(Boolean.TRUE);
		
		chkCRM = binder.buildAndBind("CRM","crm",CheckBox.class);
		
		chkVIP = binder.buildAndBind("VIP","vip",CheckBox.class);*/
		
		/*HorizontalLayout priorityHorLayout = new HorizontalLayout(priority,chkAll,chkCRM,chkVIP);
		priorityHorLayout.setMargin(false);
		priorityHorLayout.setSpacing(true);
		FormLayout formLayoutChk = new FormLayout(priorityHorLayout);*/
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo/*cmbPriority,cmbSource*/);
		FormLayout formLayout2 = new FormLayout(txtPolicyNo/*,cmbType,cmbPedSuggestion*/);
				/*FormLayout formLayout3 = new FormLayout(cmbClaimType,cmbPedStatus,fromDate);
		FormLayout formLayout4 = new FormLayout(cbxCPUCode,dummyLbl,todate);*/
		
//		FormLayout fromDtLayout = new FormLayout(fromDate);
//		fromDtLayout.setMargin(false);
//		fromDtLayout.setWidth("250px");
//		FormLayout toDtLayout = new FormLayout(todate);
//		toDtLayout.setMargin(false);
//		toDtLayout.setWidth("250px");
//		HorizontalLayout dtLayout = new HorizontalLayout(fromDtLayout,toDtLayout);
//		dtLayout.setMargin(false);
//		dtLayout.setSpacing(true);
//		dtLayout.setWidth("100%");		
//		VerticalLayout pedDateLayout = new VerticalLayout(formLayout3,dtLayout);
//		pedDateLayout.setWidth("100%");
//		pedDateLayout.setSpacing(false);
//		//Vaadin8-setImmediate() pedDateLayout.setImmediate(true);
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1,formLayout2/*,formLayout3,formLayout4*/);
		searchFormLayout.setMargin(true);
		//searchFormLayout.setSpacing(false);
		searchFormLayout.setWidth("100%");
//		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
//		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_RIGHT);
		
		absoluteLayout_3.addComponent(searchFormLayout);
		/*absoluteLayout_3.addComponent(formLayoutChk,"top:120.0px;left:14.0px;");*/
		
//		verticalLayout.addComponent(searchFormLayout);
		
/*		HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("80%");
		absoluteLayout_3.addComponent(searchFormLayout);*/
		
		pedappSearchBtn = new Button();
		pedappSearchBtn.setCaption("Search");
		//Vaadin8-setImmediate() pedappSearchBtn.setImmediate(true);
		pedappSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		pedappSearchBtn.setWidth("-1px");
		pedappSearchBtn.setHeight("-10px");
		pedappSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(pedappSearchBtn, "top:140.0px;left:220.0px;");
		//Vaadin8-setImmediate() pedappSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:140.0px;left:329.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("650px");
        verticalLayout.setHeight("500px");
		
		//setDropDownValues();
		
		return verticalLayout;		
	}

	@SuppressWarnings("deprecation")
	public void addListener() {

		pedappSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				pedappSearchBtn.setEnabled(true);
				searchable.doSearch();
				searchPEDRequestApproveTable.tablesize();
				//fireViewEvent(MenuPresenter.PED_REQUEST_PAGE_APPROVED, null);
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
	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildPedRequestProcessApproveSearchLayout.iterator();
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
							
//								Component horizontalComp = vLayoutIter.next();
								
								if(horizontalComp instanceof HorizontalLayout)
								{
									HorizontalLayout hLayout = (HorizontalLayout)horizontalComp;
									Iterator<Component> formLayComp = hLayout.iterator();
									while(formLayComp.hasNext())
									{
										Component formComp = formLayComp.next();
										
										if(formComp instanceof FormLayout){
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
												}	
												
												if(indivdualComp instanceof PopupDateField){
													((PopupDateField) indivdualComp).setValue(null);
												}
												
									// Remove the table if exists..	
									//removeTableFromLayout();
											}
										}
									}
										else if(formComp instanceof VerticalLayout){
											Iterator<Component> vertLayoutIter = ((VerticalLayout)formComp).iterator();
											while(vertLayoutIter.hasNext())
											{
												Component vertFrmComponent = vertLayoutIter.next();
												
												if(vertFrmComponent instanceof FormLayout){
													FormLayout frmLayout = (FormLayout)vertFrmComponent;
													Iterator<Component> frmCompIter = frmLayout.iterator();
												
													while(frmCompIter.hasNext())
													{
														Component indivdualComp = frmCompIter.next();
														if(indivdualComp instanceof ComboBox){
															ComboBox comboBoxField = (ComboBox) indivdualComp;
															comboBoxField.setValue(null);
														}							
													}
												}
												else if(vertFrmComponent instanceof HorizontalLayout){
													
													Iterator<Component> hCompIter = ((HorizontalLayout) vertFrmComponent).iterator();
													
													while (hCompIter.hasNext()){
														FormLayout frmLayout = (FormLayout)hCompIter.next();
														Iterator<Component> frmCompIter = frmLayout.iterator();
													
														while(frmCompIter.hasNext())
														{
															Component indivdualComp = frmCompIter.next();
															
															if(indivdualComp instanceof PopupDateField){
																((PopupDateField) indivdualComp).setValue(null);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
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
