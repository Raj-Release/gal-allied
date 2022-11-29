package com.shaic.paclaim.registration;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.registration.ClaimRegistrationSearchTable;
import com.shaic.claim.registration.SearchClaimRegisterationFormDto;
import com.shaic.claim.registration.SearchClaimRegistrationTableDto;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
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
public class SearchPAClaimRegister  extends SearchComponent<SearchClaimRegisterationFormDto>{
	
		@Inject
		private Instance<PAClaimRegistrationSearchTable> paClaimRegistrationTableinstance;
		
		@EJB
		private PreauthService preauthService;
		
		private PAClaimRegistrationSearchTable paClaimRegistrationSearchTable;
		
		private List<SearchClaimRegistrationTableDto> searchList;
		
		private NewIntimationDto newIntimationDto;
		
		private Map<String, Object> referenceData;
		
		private Panel claimRegistrationSearchPanel;

		private VerticalLayout searchVerticalLayout;
		
		private TextField txtIntimationNo;
		
		private ComboBox cmbIncedentType;
		
		private TextField txtPolicyNo;
		
		private PopupDateField intimationDate;
		
		private ComboBox cmbCPUCode;
		
		private ComboBox hospitalTypeCmb;		
		
		private Button claimRegistrationSearchBtn;
	
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
				bean.setLob(SHAConstants.PA_LOB);
				bean.setLobType(SHAConstants.PA_LOB_TYPE);
				
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
			wholelayout.setSizeFull();
			claimRegistrationSearchPanel = new Panel();
			//Vaadin8-setImmediate() claimRegistrationSearchPanel.setImmediate(false);			
			claimRegistrationSearchPanel.setSizeFull();
//			claimRegistrationSearchPanel.setHeight("50%");
			claimRegistrationSearchPanel.setWidth("100%");
			claimRegistrationSearchPanel.setCaption("Claim Registration");
			claimRegistrationSearchPanel.addStyleName("panelHeader");
			claimRegistrationSearchPanel.addStyleName("g-search-panel");
			paClaimRegistrationSearchTable = paClaimRegistrationTableinstance.get();
			paClaimRegistrationSearchTable.init("", false, false);
		}
		public void initView(Map<String,Object> referenceMaster){
			
			referenceData = referenceMaster;
			claimRegistrationSearchPanel.setContent(buildClaimRegistrationSearchLayout());
			wholelayout.addComponent(claimRegistrationSearchPanel);
			wholelayout.setComponentAlignment(claimRegistrationSearchPanel, Alignment.TOP_LEFT);
			setSizeFull();
			setCompositionRoot(wholelayout);
			addListener();
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
//			 VerticalLayout verticalLayout = new VerticalLayout();
//			 //Vaadin8-setImmediate() verticalLayout.setImmediate(false);
//			 verticalLayout.setWidth("100.0%");
//			 verticalLayout.setMargin(false);		 
			 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
			 absoluteLayout_3.setWidth("100.0%");
			 absoluteLayout_3.setHeight("140px");
			 
		  mainVerticalLayout = new VerticalLayout();
//			  //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
////			  searchVerticalLayout.setSizeFull();
//			  mainVerticalLayout.setHeight("170px");
////			  searchVerticalLayout.setSpacing(true);
//			  mainVerticalLayout.setWidth("100%");			  
//			  mainVerticalLayout.setMargin(true);
			
			txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNumber",
					TextField.class);
			txtIntimationNo.setMaxLength(26);
			txtIntimationNo.setTabIndex(1);
			
			txtPolicyNo = binder.buildAndBind("Policy No", "policyNumber",
					TextField.class);
			txtPolicyNo.setMaxLength(23);
			txtPolicyNo.setTabIndex(2);
			
			intimationDate = (PopupDateField) binder.buildAndBind("Intimation Date", "intimatedDate",
					PopupDateField.class);
			intimationDate.setTabIndex(3);

			cmbCPUCode = binder.buildAndBind("CPU Code", "cpucode",
					ComboBox.class);
			cmbCPUCode.setTabIndex(4);
			
			hospitalTypeCmb = binder.buildAndBind("Hospital Type", "hospitalType",
					ComboBox.class);
			hospitalTypeCmb.setTabIndex(5);

			cmbIncedentType = binder.buildAndBind("Accident / Death", "accDeath",
					ComboBox.class);
			cmbIncedentType.setTabIndex(6);			
			FormLayout form1 = new FormLayout(txtIntimationNo,cmbCPUCode);
			FormLayout form2 = new FormLayout(txtPolicyNo,hospitalTypeCmb);
			FormLayout form3 = new FormLayout(intimationDate,cmbIncedentType);
			HorizontalLayout searchFormHLayout = new HorizontalLayout(form1,form2,form3);
			searchFormHLayout.setSpacing(true);
//			searchFormHLayout.setMargin(true);
			searchFormHLayout.setMargin(new MarginInfo(false, true, true, true));
			searchFormHLayout.setWidth("890px");
			searchFormHLayout.setHeight("500px");
//			searchFormLayout.setMargin(true);
//			searchFormLayout.setWidth("100%");
//			absoluteLayout.addComponent(searchFormLayout);
			absoluteLayout_3.addComponent(searchFormHLayout);
			
			btnSearch.setDisableOnClick(true);
			absoluteLayout_3
			.addComponent(btnSearch, "top:100.0px;left:230.0px;");
		
//			absoluteLayout.addComponent(claimRegistrationSearchBtn,  "top:125.0px;left:240.0px;");

			btnReset = new Button();
			btnReset.setCaption("Reset");
			//Vaadin8-setImmediate() btnReset.setImmediate(true);
			btnReset.addStyleName(ValoTheme.BUTTON_DANGER);
			btnReset.setWidth("-1px");
			btnReset.setHeight("-1px");
			btnReset.setTabIndex(6);
			absoluteLayout_3.addComponent(btnReset, "top:100.0px;left:339.0px;");
			//Vaadin8-setImmediate() btnReset.setImmediate(true);
//			absoluteLayout.addComponent(resetBtn, "top:125.0px;left:349.0px;");
//			verticalLayout.addComponent(absoluteLayout);
			mainVerticalLayout.addComponent(absoluteLayout_3);
			mainVerticalLayout.setWidth("900px");
//			mainVerticalLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
//			verticalLayout.setWidth("750px");
//			verticalLayout.setHeight("250px");
//			verticalLayout.addStyleName("g-search-panel");
			addListener();
			
			setDropDownValues();
			
			return mainVerticalLayout;

		}
		
		public void resetAlltheValues(){
			
			txtIntimationNo.setValue("");
			txtPolicyNo.setValue("");
			hospitalTypeCmb.setValue(null);
			cmbCPUCode.setValue(null);
			intimationDate.setValue(null);
			cmbIncedentType.setValue(null);
			
			if(null != paClaimRegistrationSearchTable && wholelayout.getComponentCount() >1){
				wholelayout.removeComponent(paClaimRegistrationSearchTable);
			}
			
			btnSearch.setEnabled(true);
			
		}
		
		protected void setSearchResultList(Page<SearchClaimRegistrationTableDto>  searchResultList)
		{
			
			if(searchResultList != null && !searchResultList.getPageItems().isEmpty()){
				
				paClaimRegistrationSearchTable.setTableList(searchResultList);
				paClaimRegistrationSearchTable.tablesize();
				paClaimRegistrationSearchTable.setHasNextPage(searchResultList.isHasNext());
				
				wholelayout.addComponent(paClaimRegistrationSearchTable);					
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
						fireViewEvent(MenuItemBean.SEARCH_PA_CLAIM_REGISTER, null);
						
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
		
		public PAClaimRegistrationSearchTable getSearchTable(){
			return this.paClaimRegistrationSearchTable;
		}
		
		public void resetValues(){
			
			paClaimRegistrationSearchTable.getPageable().setPageNumber(1);
			paClaimRegistrationSearchTable.removeRow();
			paClaimRegistrationSearchTable.resetTable();
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
					
			
			BeanItemContainer<SelectValue> cpuCodeContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("cpuCodeContainer");
			
			cmbCPUCode.setContainerDataSource(cpuCodeContainer);
			cmbCPUCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCPUCode.setItemCaptionPropertyId("value");

			
			BeanItemContainer<SelectValue> incedentTypeContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("incedentTypeContainer");
			
			cmbIncedentType.setContainerDataSource(incedentTypeContainer);
			cmbIncedentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbIncedentType.setItemCaptionPropertyId("value");
		}		
		
		public void refresh()
		{
			System.out.println("---inside the refresh----");
			resetAlltheValues();
		}
	
}
