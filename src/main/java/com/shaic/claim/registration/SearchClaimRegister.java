package com.shaic.claim.registration;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
@SuppressWarnings("serial")
public class SearchClaimRegister  extends SearchComponent<SearchClaimRegisterationFormDto>{
	
		@Inject
		private Instance<ClaimRegistrationSearchTable> claimRegistrationTableinstance;
		
		@EJB
		private PreauthService preauthService;
		
		private ClaimRegistrationSearchTable claimRegistrationSearchTable;
		
		//private List<SearchClaimRegistrationTableDto>  searchList;
		
		private NewIntimationDto newIntimationDto;
		
		private Map<String, Object> referenceData;
		
		private Panel claimRegistrationSearchPanel;

		//private VerticalLayout searchVerticalLayout;
		
		private TextField txtIntimationNo;
		
		private TextField txtPolicyNo;
		
		private ComboBox cmbPriority;
		
		private ComboBox cmbSource;
		
		private ComboBox cmbType;
		
		private PopupDateField intimationDateField;
		
		private ComboBox hospitalTypeCmb;
		
		//private Button claimRegistrationSearchBtn;
		
		
		
		private BeanFieldGroup<SearchClaimRegisterationFormDto> binder;
		
		private VerticalLayout wholelayout;
		
		private SearchClaimRegisterationFormDto bean;
		
		
		public SearchClaimRegisterationFormDto getSearchDTO() {
			bean = new SearchClaimRegisterationFormDto();
		
			try {				
				if(this.binder.isValid()){
				this.binder.commit();
				
				bean = this.binder.getItemDataSource()
						.getBean();
				bean.setLob(SHAConstants.HEALTH_LOB);
				bean.setLobType(SHAConstants.HEALTH_LOB_FLAG);
				}
			} catch (CommitException e) {
				e.printStackTrace();
			}
			
			bean.setUserId(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
			bean.setPassword(UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString());
			
			return bean;
		}
		
		public void init()
		{
			initBinder();
			wholelayout  = new VerticalLayout();
			claimRegistrationSearchPanel = new Panel();
			//Vaadin8-setImmediate() claimRegistrationSearchPanel.setImmediate(false);
			claimRegistrationSearchPanel.setWidth("100%");
			claimRegistrationSearchPanel.setHeight("50%");
			claimRegistrationSearchPanel.setCaption("Claim Registration");
			claimRegistrationSearchPanel.addStyleName("panelHeader");
			claimRegistrationSearchPanel.addStyleName("g-search-panel");
			claimRegistrationSearchTable = claimRegistrationTableinstance.get();
			claimRegistrationSearchTable.init("", false, false);
		}
		public void initView(Map<String,Object> referenceMaster){
			
			referenceData = referenceMaster;
			claimRegistrationSearchPanel.setContent(buildClaimRegistrationSearchLayout());
			wholelayout.addComponent(claimRegistrationSearchPanel);
			wholelayout.setComponentAlignment(claimRegistrationSearchPanel, Alignment.MIDDLE_LEFT);
			setCompositionRoot(wholelayout);
			
		}
		
		private void initBinder() {
			this.binder = new BeanFieldGroup<SearchClaimRegisterationFormDto>(
					SearchClaimRegisterationFormDto.class);
			this.binder
					.setItemDataSource(new SearchClaimRegisterationFormDto());
			this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		}

		@SuppressWarnings("unchecked")
		private VerticalLayout buildClaimRegistrationSearchLayout() {
			
			btnSearch.setCaption(SearchComponent.GET_TASK_CAPTION);
			btnSearch.setDisableOnClick(true);
			 
			 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
			 VerticalLayout verticalLayout = new VerticalLayout();
			 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
			 verticalLayout.setWidth("100.0%");
			 verticalLayout.setMargin(false);		 
			 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
			 absoluteLayout_3.setWidth("100.0%");
			 absoluteLayout_3.setHeight("235px");
			 
		  mainVerticalLayout = new VerticalLayout();
//			  //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
////			  searchVerticalLayout.setSizeFull();
//			  mainVerticalLayout.setHeight("170px");
////			  searchVerticalLayout.setSpacing(true);
//			  mainVerticalLayout.setWidth("100%");			  
//			  mainVerticalLayout.setMargin(true);
			
			txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNumber",
					TextField.class);
			txtIntimationNo.setMaxLength(25);
			txtIntimationNo.setTabIndex(4);
			
			txtPolicyNo = binder.buildAndBind("Policy No", "policyNumber",
					TextField.class);
			txtPolicyNo.setMaxLength(25);
			txtPolicyNo.setTabIndex(3);
			
			intimationDateField = (PopupDateField) binder.buildAndBind("Intimation Date", "intimatedDate",
					PopupDateField.class);
			intimationDateField.setTabIndex(2);
			

			cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
			
			cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
			
			cmbType =  binder.buildAndBind("Type", "type", ComboBox.class);
			
			hospitalTypeCmb = binder.buildAndBind("Hospital Type", "hospitalType",
					ComboBox.class);
			
			BeanItemContainer<SelectValue> hospitalType = (BeanItemContainer<SelectValue>) referenceData
					.get("hospitalType");
			
				for (SelectValue component : hospitalType.getItemIds()) {
					 if(StringUtils.containsIgnoreCase(component.getValue(), "registered")){
						 hospitalType.removeItem(component);	
						 break;
					 }					 	
				}
				
			
			hospitalTypeCmb.setContainerDataSource(hospitalType);
			hospitalTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			hospitalTypeCmb.setItemCaptionPropertyId("value");
			hospitalTypeCmb.setTabIndex(1);
			FormLayout form1=new FormLayout(hospitalTypeCmb,intimationDateField,cmbPriority,cmbType);
			FormLayout form2 =new FormLayout(txtPolicyNo,txtIntimationNo,cmbSource);
			HorizontalLayout searchFormHLayout = new HorizontalLayout(form1,form2);
			searchFormHLayout.setSpacing(true);
			searchFormHLayout.setMargin(true);
			searchFormHLayout.setWidth("100%");
//			searchFormLayout.setMargin(true);
//			searchFormLayout.setWidth("100%");
//			absoluteLayout.addComponent(searchFormLayout);
			absoluteLayout_3.addComponent(searchFormHLayout);
			
			btnSearch.setDisableOnClick(true);
			absoluteLayout_3
			.addComponent(btnSearch, "top:180.0px;left:230.0px;");
		
//			absoluteLayout.addComponent(claimRegistrationSearchBtn,  "top:125.0px;left:240.0px;");

			btnReset = new Button();
			btnReset.setCaption("Reset");
			//Vaadin8-setImmediate() btnReset.setImmediate(true);
			btnReset.addStyleName(ValoTheme.BUTTON_DANGER);
			btnReset.setWidth("-1px");
			btnReset.setHeight("-1px");
			btnReset.setTabIndex(6);
			absoluteLayout_3.addComponent(btnReset, "top:180.0px;left:339.0px;");
			//Vaadin8-setImmediate() btnReset.setImmediate(true);
//			absoluteLayout.addComponent(resetBtn, "top:125.0px;left:349.0px;");
//			verticalLayout.addComponent(absoluteLayout);
			mainVerticalLayout.addComponent(absoluteLayout_3);
//			mainVerticalLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
			verticalLayout.setWidth("750px");
			verticalLayout.setHeight("250px");
			verticalLayout.addStyleName("g-search-panel");
			addListener();
			
			setDropDownValues();
			
			return mainVerticalLayout;

		}

//		public void addListener() {
//
//			claimRegistrationSearchBtn.addClickListener(new Button.ClickListener() {
//
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {	
//					claimRegistrationSearchBtn.setEnabled(true);
//					if(searchList != null && !searchList.isEmpty())
//					{
//						searchList = new ArrayList<SearchClaimRegistrationTableDto>();
//						if(claimRegistrationSearchTable != null && wholelayout.getComponentCount()>1){
//							wholelayout.removeComponent(claimRegistrationSearchTable);
//						}
//					}
//				    getSearchDTO();
////				    Pageable pageable = claimRegistrationSearchTable.getPageable();
////					bean.setPageable(pageable);
////					fireViewEvent(SearchClaimRegistrationPresenter.SEARCH_CLAIMREGISTER_TABLE_SUBMIT,bean);		
//				   
//				}
//			});
//
//		
//			resetBtn.addClickListener(new Button.ClickListener() {
//
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {
//					resetAlltheValues();
//					
//					if(null != claimRegistrationSearchTable && wholelayout.getComponentCount() >1){
//					wholelayout.removeComponent(claimRegistrationSearchTable);
//					}
//									
//				}
//			});
//		}
		
		public void resetAlltheValues(){
			
			txtIntimationNo.setValue("");
			txtPolicyNo.setValue("");
			hospitalTypeCmb.setValue(null);
			intimationDateField.setValue(null);		
			
			if(null != claimRegistrationSearchTable && wholelayout.getComponentCount() >1){
				wholelayout.removeComponent(claimRegistrationSearchTable);
			}
			
			btnSearch.setEnabled(true);
			
		}
		
		protected void setSearchResultList(Page<SearchClaimRegistrationTableDto>  searchResultList)
		{
//			if(!searchResult.isEmpty()){
//				searchList= new ArrayList<SearchClaimRegistrationTableDto>();
//				for(SearchClaimRegistrationTableDto result : searchResult)
//				{
//					searchList.add(result);		
//				}
//			}
			
			if(searchResultList != null && !searchResultList.getPageItems().isEmpty()){
				

				claimRegistrationSearchTable.setTableList(searchResultList);
				claimRegistrationSearchTable.tablesize();
				claimRegistrationSearchTable.setHasNextPage(searchResultList.isHasNext());
				
				for (SearchClaimRegistrationTableDto tableDto : searchResultList.getPageItems()) {
					if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
						claimRegistrationSearchTable.setRowColor(tableDto);
					}
				}
				
				wholelayout.addComponent(claimRegistrationSearchTable);					
				}
			else{
				Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
				Button homeButton = new Button("Register Claim Home");
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
						fireViewEvent(MenuItemBean.SEARCH_REGISTER_CLAIM, null);
						
					}
				});
			}
		
			
		}

		public void setIntimationDto(NewIntimationDto intimationDto)
		{
			newIntimationDto = intimationDto;
			newIntimationDto.setPolicy(intimationDto.getPolicy());
		}
		
		public void setupReferences(Map<String, Object> referenceData) {
			this.referenceData = referenceData;
		}	
		
		public ClaimRegistrationSearchTable getSearchTable(){
			return this.claimRegistrationSearchTable;
		}
		
		public void resetValues(){
			
			claimRegistrationSearchTable.getPageable().setPageNumber(1);
			claimRegistrationSearchTable.removeRow();
			claimRegistrationSearchTable.resetTable();
			Iterator<Component> componentIter = wholelayout.getComponentIterator();
			while(componentIter.hasNext())
			{
				Component comp = (Component)componentIter.next();
				if(comp instanceof ClaimRegistrationSearchTable)
				{
					((ClaimRegistrationSearchTable) comp).removeRow();
				}
			}
			resetAlltheValues();
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
			
			Stage statusByKey = preauthService.getStageByKey(ReferenceTable.INTIMATION_STAGE_KEY); 
			
//			Stage stageByKey3 = preauthService.getStageByKey(ReferenceTable.FINANCIAL_STAGE);

			SelectValue selectValue1 = new SelectValue();
			selectValue1.setId(statusByKey.getKey());
			selectValue1.setValue(statusByKey.getStageName());
			
//			SelectValue selectValue2 = new SelectValue();
//			selectValue2.setId(stageByKey3.getKey());
//			selectValue2.setValue(stageByKey3.getStageName());
			
			BeanItemContainer<SelectValue> statusByStage = new BeanItemContainer<SelectValue>(SelectValue.class);
			statusByStage.addBean(selectValue1);
			
			cmbSource.setContainerDataSource(statusByStage);
			cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbSource.setItemCaptionPropertyId("value");
		}
		
		
		public void refresh()
		{
			System.out.println("---inside the refresh----");
			resetAlltheValues();
		}
	
}
