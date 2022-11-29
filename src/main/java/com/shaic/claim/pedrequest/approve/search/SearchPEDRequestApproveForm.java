package com.shaic.claim.pedrequest.approve.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.search.specialist.search.SubmitSpecialistFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterFormDTO;
import com.vaadin.v7.data.Property;
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

public class SearchPEDRequestApproveForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchPEDRequestApproveTable searchPEDRequestApproveTable;

	private BeanFieldGroup<SearchPEDRequestApproveFormDTO> binder;

	TextField txtIntimationNo;

	TextField txtPolicyNo;
	
	private ComboBox cmbType;
	
	private ComboBox cmbPedStatus;
	
	private ComboBox cmbPriority;
	
//	private ComboBox cmbPriorityNew;
	
	private Label dummyLbl;
	
	private ComboBoxMultiselect priority;
	
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
	
	private List<String> selectedPriority;
	
	private SearchPEDRequestApproveFormDTO dto =  new SearchPEDRequestApproveFormDTO();

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public SearchPEDRequestApproveFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SearchPEDRequestApproveFormDTO bean = this.binder
					.getItemDataSource().getBean();
			if(selectedPriority !=null && !selectedPriority.isEmpty()){
				bean.setSelectedPriority(selectedPriority);
			}
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
		processPEDRequestApprove.setHeight("70%");
		processPEDRequestApprove.setCaption("Process PED Request");
		processPEDRequestApprove.addStyleName("panelHeader");
		processPEDRequestApprove.addStyleName("g-search-panel");
		processPEDRequestApprove.setContent(buildPedRequestProcessLayout());
		buildPedRequestProcessApproveSearchLayout.addComponent(processPEDRequestApprove);
		buildPedRequestProcessApproveSearchLayout.setComponentAlignment(processPEDRequestApprove, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildPedRequestProcessApproveSearchLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchPEDRequestApproveFormDTO>(
				SearchPEDRequestApproveFormDTO.class);
		this.binder
				.setItemDataSource(new SearchPEDRequestApproveFormDTO());
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
		 absoluteLayout_3.setHeight("250px");
////	 absoluteLayout_3.setHeight("150px");

		 
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		
		cmbPriority = binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
		
//		cmbPriorityNew = binder.buildAndBind("Priority","priorityNew",ComboBox.class);
		
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbPedSuggestion = binder.buildAndBind("Ped Type","pedSuggestion",ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		cmbPedStatus  = binder.buildAndBind("PED Status","pedStatus",ComboBox.class);
		
		/*SearchPEDRequestApproveFormDTO dto = this.binder.getItemDataSource().getBean();
		
		dto.setPedStatus(pedStatusselect1);*/
		cbxCPUCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		fromDate = binder.buildAndBind("From Date","fromDate",PopupDateField.class);
		fromDate.setDateFormat("dd/MM/yyyy");
		
		todate = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		todate.setDateFormat("dd/MM/yyyy");
		
		dummyLbl = new Label();
		dummyLbl.setEnabled(false);
		
		/*priority = new Label();
		priority.setCaption("Priority");*/
		
		chkAll = binder.buildAndBind("All","priorityAll",CheckBox.class);
		//chkAll.setValue(Boolean.TRUE);
		
		chkCRM = binder.buildAndBind("CRM","crm",CheckBox.class);
		
		chkVIP = binder.buildAndBind("VIP","vip",CheckBox.class);
		
		/*HorizontalLayout priorityHorLayout = new HorizontalLayout(priority,chkAll,chkCRM,chkVIP);
		priorityHorLayout.setMargin(false);
		priorityHorLayout.setSpacing(true);*/
		BeanItemContainer<SpecialSelectValue> container = getSelectValueForPriority();

		priority = new ComboBoxMultiselect("Priority");
		priority.setShowSelectedOnTop(true);
		//priority.setComparator(SHAUtils.getComparator());
		priority.setContainerDataSource(container);
		priority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		priority.setItemCaptionPropertyId("value");	
		priority.setData(container);
		FormLayout formLayoutChk = new FormLayout();
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cmbPriority,cmbSource,priority);
		FormLayout formLayout2 = new FormLayout(txtPolicyNo,cmbType,cmbPedSuggestion);
		FormLayout formLayout3 = new FormLayout(cmbClaimType,cmbPedStatus,fromDate);
		FormLayout formLayout4 = new FormLayout(cbxCPUCode,dummyLbl,todate);
		
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
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3,formLayout4);
		searchFormLayout.setMargin(true);
		searchFormLayout.setSpacing(false);
		searchFormLayout.setWidth("100%");
//		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
//		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_RIGHT);
		
		absoluteLayout_3.addComponent(searchFormLayout);
		absoluteLayout_3.addComponent(formLayoutChk,"top:120.0px;left:14.0px;");
		
//		verticalLayout.addComponent(searchFormLayout);
		
/*		HorizontalLayout searchFormLayout = new HorizontalLayout(new FormLayout(txtIntimationNo),new FormLayout(txtPolicyNo));
		searchFormLayout.setMargin(true);
		searchFormLayout.setWidth("80%");
		absoluteLayout_3.addComponent(searchFormLayout);*/
		
		pedappSearchBtn = new Button();
		pedappSearchBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() pedappSearchBtn.setImmediate(true);
		pedappSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		pedappSearchBtn.setWidth("-1px");
		pedappSearchBtn.setHeight("-10px");
		pedappSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(pedappSearchBtn, "top:185.0px;left:320.0px;");
		//Vaadin8-setImmediate() pedappSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:185.0px;left:430.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		
		verticalLayout.addComponent(absoluteLayout_3);
		//verticalLayout.setHeight("500px");
		
		setDropDownValues();
		
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
				//searable.doSearch();
				resetAlltheValues();
				//chkAll.setValue(true);
				/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
				
				cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
				cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbPriorityNew.setItemCaptionPropertyId("value");
				cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
			}
		});
		
		priority.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				/*BeanItem<PreauthMedicalDecisionDTO> dtoBeanObject = binder.getItemDataSource();
								PreauthMedicalDecisionDTO dtoObject = dtoBeanObject.getBean();
								dtoObject.setUserRoleMulti(null);
								dtoObject.setUserRoleMulti(event.getProperty().getValue());*/

				if(null != event && null != event.getProperty() && null != event.getProperty().getValue()){
					BeanItemContainer<SpecialSelectValue> listOfDoctors = (BeanItemContainer<SpecialSelectValue>) priority.getData();
					List<String> docList = preauthService.getListFromMultiSelectComponent(event.getProperty().getValue());
					BeanItemContainer<SpecialSelectValue> listOfRoleContainer = (BeanItemContainer<SpecialSelectValue>)priority.getData();
					List<String> roles = new ArrayList<String>();
					List<SpecialSelectValue> listOfRoles = listOfRoleContainer.getItemIds();
					chkAll.setValue(false);
					chkCRM.setValue(false);
					chkVIP.setValue(false);

					if(docList != null)
					{
						setselectedPriority("CRM",false);
						setselectedPriority("VIP",false);
						setselectedPriority("COVID",false);
						setselectedPriority("ATOS",false);


						for (String selValue : docList) {

							if(selValue.equalsIgnoreCase("All"))
							{	
								chkAll.setValue(true);
							}
							if(selValue.equalsIgnoreCase("CRM Flagged"))
							{	
								chkCRM.setValue(true);
								setselectedPriority("CRM",true);
							}
							if(selValue.equalsIgnoreCase("VIP"))
							{	
								chkVIP.setValue(true);
								setselectedPriority("VIP",true);
							}
							if(selValue.equalsIgnoreCase("Corporate - High Priority"))
							{	
								//chkAll.setValue(true);
								setselectedPriority("ATOS",true);
							}

						}
					}

				}

			}
		});
		
		/*chkCRM.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();

					if(value || (chkVIP != null && chkVIP.getValue() != null && chkVIP.getValue().equals(Boolean.TRUE)))
					{
						chkAll.setValue(false);
						chkAll.setEnabled(false);
					}
					else{
						chkAll.setEnabled(true);
					}	 						 
					
				}
			}
		});
		
		chkVIP.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();

					if(value || (chkCRM != null && chkCRM.getValue() != null && chkCRM.getValue().equals(Boolean.TRUE)))
					{
						chkAll.setValue(false);
						chkAll.setEnabled(false);
					}
					else{
						chkAll.setEnabled(true);
					}	 						 
					
				}
			}
		});*/
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
			
			//cmbPedStatus.setValue(cmbPedStatus.getContainerDataSource().getItemIds().toArray()[0]);
	}
	
	private void removeTableFromLayout()
	{
		if(null != searchable)
		{
			searchable.resetSearchResultTableValues();
			clearFields();
		}
	}
   public void setDropDownValues(){
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriorityIRDA();
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		
		cmbClaimType.setContainerDataSource(masterService.getMasterValueByReference(ReferenceTable.CLAIM_TYPE));
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
		Status statusByKey1 = preauthService.getStatusByKey(ReferenceTable.PED_INITIATE);
		Status statusByKey2 = preauthService.getStatusByKey(ReferenceTable.PED_ESCALATED); 
		Status statusByKey3 = preauthService.getStatusByKey(ReferenceTable.SPECIALIST_REPLY_RECEIVED);
		BeanItemContainer<SelectValue> statusByKeyContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
				
		SelectValue pedStatusselect1 = new SelectValue();
		pedStatusselect1.setId(statusByKey1.getKey());
		pedStatusselect1.setValue(statusByKey1.getProcessValue());
	
		statusByKeyContainer.addBean(pedStatusselect1);
		
//Commented as per Requirement document by Raja on 5th july
		/*SelectValue pedStatusselect2 = new SelectValue(statusByKey2.getKey(),statusByKey2.getProcessValue());
		statusByKeyContainer.addBean(pedStatusselect2);*/
		
		SelectValue pedStatusselect3 = new SelectValue(statusByKey3.getKey(),statusByKey3.getProcessValue());
		statusByKeyContainer.addBean(pedStatusselect3);
				
		cmbPedStatus.setContainerDataSource(statusByKeyContainer);
		cmbPedStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPedStatus.setItemCaptionPropertyId("value");

//		cmbPedStatus.setValue(statusByKeyContainer.getItemIds().get(0));

		
		/*SearchPEDRequestApproveFormDTO dto = this.binder.getItemDataSource().getBean();
		
		dto.setPedStatus(pedStatusselect1);*/
				
		Stage stageByKey2 = preauthService.getStageByKey(ReferenceTable.PREAUTH_STAGE);
		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.ENHANCEMENT_STAGE); 
		Stage stageByKey4 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE); 
		Stage stageByKey5 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);
		Stage stageByKey6 = preauthService.getStageByKey(ReferenceTable.DOWNSIZE_STAGE);
		Stage stageByKey7 = preauthService.getStageByKey(ReferenceTable.STANDALONE_WITHDRAW_STAGE);
		
		
//		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey3.getKey());
		selectValue2.setValue(stageByKey3.getStageName());
		
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(stageByKey4.getKey());
		selectValue3.setValue(stageByKey4.getStageName());
		
		SelectValue selectValue4 = new SelectValue();
		selectValue4.setId(stageByKey5.getKey());
		selectValue4.setValue(stageByKey5.getStageName());
		
		SelectValue selectValue5 = new SelectValue();
		selectValue5.setId(stageByKey6.getKey());
		selectValue5.setValue(stageByKey6.getStageName());
		
		SelectValue selectValue6 = new SelectValue();
		selectValue6.setId(stageByKey7.getKey());
		selectValue6.setValue(stageByKey7.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		statusByStage.addBean(selectValue3);
		statusByStage.addBean(selectValue4);
		statusByStage.addBean(selectValue5);
		statusByStage.addBean(selectValue6);
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getSelectValueContainer(ReferenceTable.PED_SUGGESTION);
		
		cmbPedSuggestion.setContainerDataSource(selectValueContainer);
		cmbPedSuggestion.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPedSuggestion.setItemCaptionPropertyId("value");
		
		statusByStage.sort(new Object[] {"value"}, new boolean[] {true});
		selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		BeanItemContainer<SelectValue> selectValueContainerForCPUCode = masterService
				.getTmpCpuCodes();
		
		cbxCPUCode.setContainerDataSource(selectValueContainerForCPUCode);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
		clearFields();
		/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
	}
	
	public void clearFields()
    {
		chkAll.setValue(false);
		chkCRM.setValue(false);
		chkVIP.setValue(false);
		priority.setValue(null);
    }
	
	public static BeanItemContainer<SpecialSelectValue> getSelectValueForPriority(){
		BeanItemContainer<SpecialSelectValue> container = new BeanItemContainer<SpecialSelectValue>(SelectValue.class);

		SpecialSelectValue selectValue1 = new SpecialSelectValue();
		selectValue1.setId(1l);
		selectValue1.setValue("All");

		SpecialSelectValue selectValue3 = new SpecialSelectValue();
		selectValue3.setId(2l);
		selectValue3.setValue("CRM Flagged");

		SpecialSelectValue selectValue4 = new SpecialSelectValue();
		selectValue4.setId(3l);
		selectValue4.setValue("VIP");

		/*SpecialSelectValue selectValue2 = new SpecialSelectValue();
					selectValue2.setId(4l);
					selectValue2.setValue("COVID");*/

		SpecialSelectValue selectValue5 = new SpecialSelectValue();
		selectValue5.setId(5l);
		selectValue5.setValue("Corporate - High Priority");

		container.addBean(selectValue1);
		container.addBean(selectValue3);
		container.addBean(selectValue4);
		//container.addBean(selectValue2);
		container.addBean(selectValue5);
		container.sort(new Object[] {"value"}, new boolean[] {true});

		return container;
	}
	
	private void setselectedPriority(String priority,Boolean ischk){
		if(ischk){
			if(selectedPriority !=null 
					&& !selectedPriority.contains(priority)){
				selectedPriority.add(priority);
			}else{
				selectedPriority = new ArrayList<String>();
				selectedPriority.add(priority);
			}
		}else{
			if(selectedPriority !=null 
					&& selectedPriority.contains(priority)){
				selectedPriority.remove(priority);
			}
		}
	}
	
	public void initwithSearchForm(SearchPEDRequestApproveFormDTO dto) {
		initBinderGetTask(dto);
		buildPedRequestProcessApproveSearchLayout  = new VerticalLayout();
		Panel processPEDRequestApprove	= new Panel();
		//Vaadin8-setImmediate() processPEDRequestApprove.setImmediate(false);
		//processPEDRequestApprove.setWidth("850px");
		processPEDRequestApprove.setWidth("100%");
		processPEDRequestApprove.setHeight("70%");
		processPEDRequestApprove.setCaption("Process PED Request");
		processPEDRequestApprove.addStyleName("panelHeader");
		processPEDRequestApprove.addStyleName("g-search-panel");
		processPEDRequestApprove.setContent(buildPedRequestProcessLayout());
		buildPedRequestProcessApproveSearchLayout.addComponent(processPEDRequestApprove);
		buildPedRequestProcessApproveSearchLayout.setComponentAlignment(processPEDRequestApprove, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildPedRequestProcessApproveSearchLayout);
		addListener();
		
		if(dto.getIntimationNo() != null)
		{
			this.txtIntimationNo.setValue(dto.getIntimationNo());
		}
		if(this.dto.getSource() != null)
		{
			this.cmbSource.setValue(this.dto.getSource());
		}
		if(this.dto.getCpuCode() != null)
		{
			this.cbxCPUCode.setValue(this.dto.getCpuCode());
		}
		
		if(this.dto.getPolicyNo() != null)
		{
			this.txtPolicyNo.setValue(this.dto.getPolicyNo());
		}
		
		if(this.dto.getPriority() != null)
		{
			this.cmbPriority.setValue(this.dto.getPriority());
		}
		
		if(this.dto.getCpuCode() != null)
		{
			this.cbxCPUCode.setValue(this.dto.getCpuCode());
		}
		
		if(this.dto.getType() != null)
		{
			this.cmbType.setValue(this.dto.getType());
		}
		
		if(this.dto.getPedSuggestion() != null)
		{
			this.cmbPedSuggestion.setValue(this.dto.getPedSuggestion());
		}
		
		if(this.dto.getClaimType() != null)
		{
			this.cmbClaimType.setValue(this.dto.getClaimType());
		}
		
		if(this.dto.getPedStatus() != null)
		{
			this.cmbPedStatus.setValue(this.dto.getPedStatus());
		}
		
		if(this.dto.getFromDate() != null)
		{
			this.fromDate.setValue(this.dto.getFromDate());
		}
		
		if(this.dto.getToDate() != null)
		{
			this.todate.setValue(this.dto.getToDate());
		}
		
		
		//addListener();
	}
	
	private void initBinderGetTask(SearchPEDRequestApproveFormDTO dto) {
		
		if(dto != null) {
			this.dto = dto;
		} else {
			this.dto = new SearchPEDRequestApproveFormDTO();
		}
		
		this.binder = new BeanFieldGroup<SearchPEDRequestApproveFormDTO>(SearchPEDRequestApproveFormDTO.class);
		this.binder.setItemDataSource(this.dto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	}
