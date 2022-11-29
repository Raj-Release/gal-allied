package com.shaic.claim.search.specialist.search;

import java.util.Iterator;
import java.util.List;

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
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestFormDTO;
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
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SubmitSpecialistForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SubmitSpecialistTable submitSpecialistTable;
	
	@Inject
	private SpecialistCompletedCasesUI specialistCompletedCases;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SubmitSpecialistService specialistService;
	
	private BeanFieldGroup<SubmitSpecialistFormDTO> binder;

	TextField txtIntimationNo;

	TextField txtPolicyNo;
	
	ComboBox comRefferedBy;
	
	private ComboBox cmbType;
	
	private ComboBox cmbPriority;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbSpecialitType;
	
//	private ComboBox cmbPriorityNew;
	
	private Label priority;
	
	private CheckBox chkAll;
	
	private CheckBox chkCRM;
	
	private CheckBox chkVIP;

	Button submitSpecialistFormBtn;
	
	private Button resetBtn;
	
	private Button btnCompleted;

	
	private Searchable searchable;
	
	private Boolean isReimbursement = false;
	
	private ComboBox cbxCPUCode;
	
	////private static Window popup;
	
	private VerticalLayout buildSearchSubmitSpecialistFormLayout;
	
	private SubmitSpecialistFormDTO dto =  new SubmitSpecialistFormDTO();


	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public SubmitSpecialistFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SubmitSpecialistFormDTO bean = this.binder
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
		buildSearchSubmitSpecialistFormLayout  = new VerticalLayout();
		Panel specialistSubmitFormPanel	= new Panel();
		//Vaadin8-setImmediate() specialistSubmitFormPanel.setImmediate(false);
		specialistSubmitFormPanel.setWidth("100%");
		specialistSubmitFormPanel.setHeight("50%");
		specialistSubmitFormPanel.setCaption("Submit Specialist Advise");
		specialistSubmitFormPanel.addStyleName("panelHeader");
		specialistSubmitFormPanel.addStyleName("g-search-panel");
		specialistSubmitFormPanel.setContent(buildSubmitSpecialFormLayout());
		buildSearchSubmitSpecialistFormLayout.addComponent(specialistSubmitFormPanel);
		buildSearchSubmitSpecialistFormLayout.setComponentAlignment(specialistSubmitFormPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildSearchSubmitSpecialistFormLayout);
		addListener();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SubmitSpecialistFormDTO>(
				SubmitSpecialistFormDTO.class);
		this.binder
				.setItemDataSource(new SubmitSpecialistFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildSubmitSpecialFormLayout() {
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 VerticalLayout verticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
		 verticalLayout.setWidth("100.0%");
		 verticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("250px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
					TextField.class);
		comRefferedBy =(ComboBox) binder.buildAndBind("Referred by (Doctor)","refferedBy",ComboBox.class);
		
		cmbPriority = (ComboBox) binder.buildAndBind("Priority(IRDA)","priority",ComboBox.class);
		cmbType =(ComboBox) binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbSource =(ComboBox) binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbSpecialitType =(ComboBox) binder.buildAndBind("Specialist Type","specialistType",ComboBox.class);
//		cmbPriorityNew = binder.buildAndBind("Priority", "priorityNew", ComboBox.class);
		cbxCPUCode =(ComboBox) binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		btnCompleted = new Button("Completed Cases");
		btnCompleted.setVisible(true);
		
		priority = new Label();
		priority.setCaption("Priority");
		
		chkAll = binder.buildAndBind("All","priorityAll",CheckBox.class);
		chkAll.setValue(Boolean.TRUE);
		
		chkCRM = binder.buildAndBind("CRM","crm",CheckBox.class);
		
		chkVIP = binder.buildAndBind("VIP","vip",CheckBox.class);
		
		HorizontalLayout priorityHorLayout = new HorizontalLayout(priority,chkAll,chkCRM,chkVIP);
		priorityHorLayout.setMargin(false);
		priorityHorLayout.setSpacing(true);
		FormLayout formLayoutChk = new FormLayout(priorityHorLayout);

		FormLayout formLayout1;
		if(isReimbursement.equals(Boolean.TRUE)){
			formLayout1 = new FormLayout(txtIntimationNo,comRefferedBy,cmbPriority,cmbSource);
		}else{
			formLayout1 = new FormLayout(txtIntimationNo,comRefferedBy,cmbPriority,cmbSource);
		}
		
		FormLayout formLayout2 = new FormLayout( txtPolicyNo,cmbType,cmbSpecialitType,cbxCPUCode/*, cmbPriorityNew*/);
		FormLayout formLayout3 = new FormLayout(btnCompleted);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2,formLayout3);
		searchFormLayout.setSpacing(true);
		searchFormLayout.setComponentAlignment(formLayout3, Alignment.TOP_RIGHT);
		/*searchFormLayout.setComponentAlignment(formLayout1, Alignment.MIDDLE_LEFT);
		searchFormLayout.setComponentAlignment(formLayout2, Alignment.MIDDLE_RIGHT);*/
		searchFormLayout.setMargin(true);
//		searchFormLayout.setWidth("100%");
		absoluteLayout_3.addComponent(searchFormLayout);
		absoluteLayout_3.addComponent(formLayoutChk,"top:155.0px;left:14.0px;");
		
		submitSpecialistFormBtn = new Button();
		submitSpecialistFormBtn.setCaption("Get Tasks");
		//Vaadin8-setImmediate() submitSpecialistFormBtn.setImmediate(true);
		submitSpecialistFormBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitSpecialistFormBtn.setWidth("-1px");
		submitSpecialistFormBtn.setHeight("-10px");
		submitSpecialistFormBtn.setDisableOnClick(true);
		//Vaadin8-setImmediate() submitSpecialistFormBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
		verticalLayout.setWidth("900px");
		
		if(isReimbursement.equals(Boolean.TRUE)){
			absoluteLayout_3.addComponent(submitSpecialistFormBtn, "top:205.0px;left:280.0px;");
			absoluteLayout_3.addComponent(resetBtn, "top:205.0px;left:389.0px;");
			
		}else{
			absoluteLayout_3.addComponent(submitSpecialistFormBtn, "top:205.0px;left:280.0px;");
			absoluteLayout_3.addComponent(resetBtn, "top:205.0px;left:389.0px;");
		}
		//verticalLayout.setHeight("600px");
		
//		setDropDownValues();
	
		return verticalLayout; 
	}

	@SuppressWarnings("deprecation")
	public void addListener() {

		submitSpecialistFormBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				submitSpecialistFormBtn.setEnabled(true);
				searchable.doSearch();
				submitSpecialistTable.tablesize();
			//	fireViewEvent(MenuPresenter.SUBMIT_SPECIALIST_ADVISE_PAGE,null);
			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//searable.doSearch();
				resetAlltheValues();
				chkAll.setValue(true);
				
				/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
				
				cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
				cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbPriorityNew.setItemCaptionPropertyId("value");
				cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
			}
		});
		
		btnCompleted.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//searable.doSearch();
				showPopupForCompletion();
			}
		});
		
		chkCRM.addValueChangeListener(new ValueChangeListener() {

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
		});

	}
	
	public void setRefByDocList(BeanItemContainer<SelectValue> parameter)
	{
		comRefferedBy.setContainerDataSource(parameter);
		comRefferedBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comRefferedBy.setItemCaptionPropertyId("value");
	}	
	
	public void setReimbursement(Boolean isReimbursement){
		this.isReimbursement = isReimbursement;
	}
	
   public void setDropDownValues(BeanItemContainer<SelectValue> cpuCodeContainer){
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriorityIRDA = SHAUtils.getSelectValueForPriorityIRDA();
		cmbPriority.setContainerDataSource(selectValueForPriorityIRDA);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
		/*BeanItemContainer<SelectValue> selectValueForPriorityNew = SHAUtils.getSelectValueForPriorityNew();
		cmbPriorityNew.setContainerDataSource(selectValueForPriorityNew);
		cmbPriorityNew.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriorityNew.setItemCaptionPropertyId("value");
		cmbPriorityNew.setValue(selectValueForPriorityNew.getItemIds().get(0));*/
		
		String username = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		BeanItemContainer<SelectValue> specialistValue = specialistService.getSpecialistValue(username);

		cmbSpecialitType.setContainerDataSource(specialistValue);
		cmbSpecialitType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSpecialitType.setItemCaptionPropertyId("value");
		
		cbxCPUCode.setContainerDataSource(cpuCodeContainer);
		cbxCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxCPUCode.setItemCaptionPropertyId("value");
		
		List<SelectValue> itemIds = specialistValue.getItemIds();
		if(itemIds != null && itemIds.isEmpty()){
			submitSpecialistFormBtn.setEnabled(false);
		}
		
		
		
		Stage stageByKey2 = null;
		Stage stageByKey = null;
		
		if(! isReimbursement){
			stageByKey2 = preauthService.getStageByKey(ReferenceTable.PREAUTH_STAGE);
			stageByKey = preauthService.getStageByKey(ReferenceTable.ENHANCEMENT_STAGE); 
			
		}else{
			stageByKey2 = preauthService.getStageByKey(ReferenceTable.CLAIM_REQUEST_STAGE);
			stageByKey = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);
		}
		
		
		Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.DOWNSIZE_STAGE);

		SelectValue selectValue1 = new SelectValue();
		selectValue1.setId(stageByKey2.getKey());
		selectValue1.setValue(stageByKey2.getStageName());
		
		SelectValue selectValue2 = new SelectValue();
		selectValue2.setId(stageByKey.getKey());
		selectValue2.setValue(stageByKey.getStageName());
		
		SelectValue selectValue3 = new SelectValue();
		selectValue3.setId(stageByKey3.getKey());
		selectValue3.setValue(stageByKey3.getStageName());
		
		BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
		statusByStage.addBean(selectValue1);
		statusByStage.addBean(selectValue2);
		
		if(! isReimbursement){
			statusByStage.addBean(selectValue3);
		}
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		statusByStage.sort(new Object[] {"value"}, new boolean[] {true});
		
		if(dto != null)
		{
			if(this.dto.getSource() != null)
			{
				this.cmbSource.setValue(this.dto.getSource());
			}
			if(this.dto.getCpuCode() != null)
			{
				this.cbxCPUCode.setValue(this.dto.getCpuCode());
			}

			if(this.dto.getRefferedBy() != null)
			{
				this.comRefferedBy.setValue(this.dto.getRefferedBy());
			}

			if(this.dto.getPriority() != null)
			{
				this.cmbPriority.setValue(this.dto.getPriority());
			}

			if(this.dto.getType() != null)
			{
				this.cmbType.setValue(this.dto.getType());
			}
		}
		
	}
	
	/**
	 * Method to reset all form values 
	 *
	 * */
	
	public void resetAlltheValues() 
	{
		
		Iterator<Component> componentIterator = buildSearchSubmitSpecialistFormLayout.iterator();
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


	private void removeTableFromLayout()
	{
		if(null != searchable)
		{
			searchable.resetSearchResultTableValues();
			clearFields();
		}
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
		clearFields();
	}
	
	public void showPopupForCompletion(){
		
		String username = (String) getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		specialistCompletedCases.init(username);
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Completed Cases");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(specialistCompletedCases);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
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
	
	public void clearFields()
    {
		chkAll.setValue(true);
		chkCRM.setValue(false);
		chkVIP.setValue(false);
    }
	
	public void initwithSearchForm(SubmitSpecialistFormDTO dto) {
		initBinderGetTask(dto);
		buildSearchSubmitSpecialistFormLayout  = new VerticalLayout();
		Panel specialistSubmitFormPanel	= new Panel();
		//Vaadin8-setImmediate() specialistSubmitFormPanel.setImmediate(false);
		specialistSubmitFormPanel.setWidth("100%");
		specialistSubmitFormPanel.setHeight("50%");
		specialistSubmitFormPanel.setCaption("Submit Specialist Advise");
		specialistSubmitFormPanel.addStyleName("panelHeader");
		specialistSubmitFormPanel.addStyleName("g-search-panel");
		specialistSubmitFormPanel.setContent(buildSubmitSpecialFormLayout());
		buildSearchSubmitSpecialistFormLayout.addComponent(specialistSubmitFormPanel);
		buildSearchSubmitSpecialistFormLayout.setComponentAlignment(specialistSubmitFormPanel, Alignment.MIDDLE_LEFT);
		setCompositionRoot(buildSearchSubmitSpecialistFormLayout);
		
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
		
		if(this.dto.getRefferedBy() != null)
		{
			this.comRefferedBy.setValue(this.dto.getRefferedBy());
		}
		
		if(this.dto.getPriority() != null)
		{
			this.cmbPriority.setValue(this.dto.getPriority());
		}
		
		if(this.dto.getPolicyNo() != null)
		{
			this.txtPolicyNo.setValue(this.dto.getPolicyNo());
		}
		
		if(this.dto.getType() != null)
		{
			this.cmbType.setValue(this.dto.getType());
		}
		
		
		addListener();
	}
	
	private void initBinderGetTask(SubmitSpecialistFormDTO dto) {
		
		if(dto != null) {
			this.dto = dto;
		} else {
			this.dto = new SubmitSpecialistFormDTO();
		}
		
		this.binder = new BeanFieldGroup<SubmitSpecialistFormDTO>(SubmitSpecialistFormDTO.class);
		this.binder.setItemDataSource(this.dto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	

	}
