package com.shaic.claim.pedrequest.teamlead.search;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
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
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchPEDRequestTeamLeadForm extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchPEDRequestTeamLeadTable searchPEDRequestApproveTable;

	private BeanFieldGroup<SearchPEDRequestApproveFormDTO> binder;

	TextField txtIntimationNo;

	TextField txtPolicyNo;
	
	private PopupDateField fromDate;
	
	private PopupDateField todate;
	
	private ComboBox cmbType;
	private ComboBox cmbPriority;
	
	private ComboBox cmbSource;
	
	private ComboBox cmbPedSuggestion;
	
	private ComboBox cmbClaimType;
	
	private ComboBox cmbPedStatus;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private MasterService masterService;
	

	Button pedappSearchBtn;
	
	private Searchable searchable;
	
	Button resetBtn;
	
	
	private VerticalLayout buildPedRequestProcessApproveSearchLayout ;

	public void addSearchListener(Searchable searchable) {
		this.searchable = searchable;
	}

	public SearchPEDRequestApproveFormDTO getSearchDTO() {
		try {
			this.binder.commit();
			SearchPEDRequestApproveFormDTO bean = this.binder
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
//		 absoluteLayout_3.setHeight("178px");
		 absoluteLayout_3.setHeight("250px");
//		 absoluteLayout_3.setHeight("150px");

		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		cmbPedSuggestion = binder.buildAndBind("Ped Type","pedSuggestion",ComboBox.class);
		
		cmbClaimType = binder.buildAndBind("Claim Type","claimType",ComboBox.class);
		
		cmbPedStatus  = binder.buildAndBind("PED Status","pedStatus",ComboBox.class);
		
		fromDate = binder.buildAndBind("From Date","fromDate",PopupDateField.class);
		fromDate.setDateFormat("dd/MM/yyyy");
		
		todate = binder.buildAndBind("To Date","toDate",PopupDateField.class);
		todate.setDateFormat("dd/MM/yyyy");
				
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cmbPriority,cmbSource);
		FormLayout formLayout2 = new FormLayout(txtPolicyNo,cmbType,cmbPedSuggestion);
		FormLayout formLayout3 = new FormLayout(cmbClaimType,cmbPedStatus,fromDate);
		FormLayout formLayout4 = new FormLayout(new Label(),new Label(),todate);
		
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3,formLayout4);
		searchFormLayout.setMargin(true);
		
		searchFormLayout.setWidth("100%");
//		searchFormLayout.setComponentAlignment(formLayout1 , Alignment.MIDDLE_LEFT);
//		searchFormLayout.setComponentAlignment(formLayout2 , Alignment.MIDDLE_RIGHT);
		absoluteLayout_3.addComponent(searchFormLayout);
		
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
		.addComponent(pedappSearchBtn, "top:140.0px;left:220.0px;");
		//Vaadin8-setImmediate() pedappSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-10px");
		absoluteLayout_3.addComponent(resetBtn, "top:140.0px;left:329.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		verticalLayout.addComponent(absoluteLayout_3);
//		verticalLayout.setWidth("900px");
		//verticalLayout.setHeight("500px");
		
		setDropDownValues();
		
		return verticalLayout; 
	}

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
												
												if(indivdualComp instanceof PopupDateField)
												{
													PopupDateField field = (PopupDateField) indivdualComp;
													field.setValue(null);
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
   public void setDropDownValues(){
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		cmbType.setContainerDataSource(selectValueForType);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForPriority = SHAUtils.getSelectValueForPriority();
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		
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

		SelectValue pedStatusselect2 = new SelectValue(statusByKey2.getKey(),statusByKey2.getProcessValue());
		statusByKeyContainer.addBean(pedStatusselect2);
		
		SelectValue pedStatusselect3 = new SelectValue(statusByKey3.getKey(),statusByKey3.getProcessValue());
		statusByKeyContainer.addBean(pedStatusselect3);
				
		cmbPedStatus.setContainerDataSource(statusByKeyContainer);
		cmbPedStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPedStatus.setItemCaptionPropertyId("value");
		
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
		
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetAlltheValues();
	}

	}
