package com.shaic.claim.pcc.hrmp;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.swing.plaf.multi.MultiComboBoxUI;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTable;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.MasClmAuditUserMapping;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.UserLoginDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

/**
 * @author PanneerSelvam.M
 *
 */
public class SearchHRMPForm extends SearchComponent<SearchHRMPFormDTO>{
	
	private SearchHRMPFormDTO searchDto;
	
	private SearchHRMPTable searchTable;
	
	@EJB
	private MasterService masterService;
	
	private TextField intimationNumber;
	
//	private ComboBox cmbStatus;
	
	private TextField txtuserId;
	
	private ComboBox claimType;

	private ComboBoxMultiselect pendingReason;
	
	private HorizontalLayout fieldLayout;
	
	TabSheet auditQueryTab = null;
	
	private BeanItemContainer<SelectValue> clmTypeContainer;

	private BeanItemContainer<SelectValue> pendingReasonContainer;
	
	private MasClmAuditUserMapping auditUserMapClaimUser;
	
	private ComboBox cpuCode;
	private ComboBox hospitalCode;
	
	private FormLayout cpuForm;
	private FormLayout hospitalForm;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Long Stay Claims");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		resetBtnListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();

		


		intimationNumber = binder.buildAndBind("Intimation Number","intimationNumber",TextField.class);
		intimationNumber.setWidth("160px");
		intimationNumber.setHeight("-1px");
		intimationNumber.setMaxLength(30);
		CSValidator intimationNumValidator = new CSValidator();
		intimationNumValidator.extend(intimationNumber);
		intimationNumValidator.setPreventInvalidTyping(true);
		intimationNumValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		
		cpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		cpuCode.setWidth("160px");
		hospitalCode = binder.buildAndBind("Hospital Code","hospitalCode",ComboBox.class);
		
		txtuserId = binder.buildAndBind("User Id", "userId", TextField.class);
		fieldLayout = new HorizontalLayout();
		
		/*claimType = binder.buildAndBind("Claim Type", "clmType", ComboBox.class);
		claimType.setNullSelectionAllowed(Boolean.FALSE);
		clmTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);*/
		
		/*pendingReason = new ComboBoxMultiselect("Pending Reason");
		pendingReasonContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		loadSelectValueCombBox();
		pendingReason.setContainerDataSource(pendingReasonContainer);
		pendingReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		pendingReason.setItemCaptionPropertyId("value");
		pendingReason.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
				if(null != pendingReason){
//					binder.getItemDataSource().getBean().setPendingReason(pendingReason.getValue().toString());
				}
			}
		});
		
		SelectValue cashlessClmSelect = new SelectValue(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY, SHAConstants.CASHLESS_CLAIM_TYPE);
		SelectValue reimbClmSelect = new SelectValue(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY, SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
		clmTypeContainer.addBean(cashlessClmSelect);
		clmTypeContainer.addBean(reimbClmSelect);*/
		
		
		
		/*claimType.setContainerDataSource(clmTypeContainer);
		claimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		claimType.setItemCaptionPropertyId("value");*/
	
//		intimationNumber = binder.buildAndBind("Intimation No", "intimationNumber", TextField.class);
		
	/*	cmbStatus = binder.buildAndBind("Status", "cmbauditStatus", ComboBox.class);
		cmbStatus.setNullSelectionAllowed(Boolean.FALSE);
		BeanItemContainer<SelectValue> auditStatusContainer = masterService.getMasterValueByReference(SHAConstants.CVC_REMEDIATION_STATUS);
		
		cmbStatus.setContainerDataSource(auditStatusContainer);
		cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatus.setItemCaptionPropertyId("value");
		cmbStatus.setValue(auditStatusContainer.getItemIds().get(1));*/
		
//		txtuserId = binder.buildAndBind("User Id", "userId", TextField.class);
//		FormLayout formLayoutLeft = new FormLayout(intimationNumber);
//		formLayoutLeft.setSpacing(true);
//		FormLayout formLayoutMiddle = new FormLayout(cmbStatus);
//		formLayoutMiddle.setSpacing(true);
//		FormLayout formLayoutRight = new FormLayout(txtuserId);
//		formLayoutRight.setSpacing(true);
//		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft, formLayoutRight);

		
//		fieldLayout.setMargin(true);
//		fieldLayout.setSpacing(true);
//		fieldLayout.setWidth("100%");		
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(auditQueryTabs());		
		absoluteLayout_3.addComponent(btnSearch, "top:130.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:130.0px;left:320.0px;");
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("1000px");
		mainVerticalLayout.setMargin(false);
		mainVerticalLayout.setSpacing(true);
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("180px");
		 
		addListener();
		return mainVerticalLayout;

	}
	private void initBinder(){
		this.binder = new BeanFieldGroup<SearchHRMPFormDTO>(SearchHRMPFormDTO.class);
		this.binder.setItemDataSource(new SearchHRMPFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(String userName) {
		
		if(userName != null && !userName.isEmpty()){
			TmpEmployee userLoginDetails = masterService.getUserLoginDetail(userName);
			txtuserId.setValue(userLoginDetails.getLoginId() +" - "+userLoginDetails.getEmpFirstName());
			txtuserId.setReadOnly(true);
		}
		
		BeanItemContainer<SelectValue> cpuList = masterService.getTmpCpuCodeList();
		cpuCode.setContainerDataSource(cpuList);
		cpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cpuCode.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> hospitalList = masterService.getHospitalCodeListByHRMCode(userName);
		hospitalCode.setContainerDataSource(hospitalList);
		hospitalCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		hospitalCode.setItemCaptionPropertyId("value");
		
	/*	BeanItemContainer<SelectValue> auditStatusContainer = masterService.getMasterValueByReference(SHAConstants.CVC_REMEDIATION_STATUS);
		
		cmbStatus.setContainerDataSource(auditStatusContainer);
		cmbStatus.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbStatus.setItemCaptionPropertyId("value");
		cmbStatus.setValue(auditStatusContainer.getItemIds().get(1));*/
		
		ImsUser imsUser = null;
		String[] userRoles = null;
		if(VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT) != null) {
			imsUser = (ImsUser) VaadinSession.getCurrent().getAttribute(BPMClientContext.USER_OBJECT);
			userRoles = imsUser.getUserRoleList();
		}
		SearchHRMPFormDTO searchBean = this.binder.getItemDataSource().getBean();
		/*if(Arrays.asList(userRoles).contains(MenuPresenter.USER_ROLE_CVC_AUDIT)) { //CLM_CVC_AUDIT_NORMAL_USER
			
			searchBean.setClmAuditHeadUser(false);
		}

		if(Arrays.asList(userRoles).contains(MenuPresenter.USER_ROLE_CVC_AUDIT_CLUSTER_HEAD) ) {  //CLM_CVC_AUDIT_CLUSTER_HEAD_USER
			searchBean.setClmAuditHeadUser(true);
		}
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		if(searchBean.isClmAuditHeadUser()) {
			txtuserId.setEnabled(true);	
		}
		else {
			txtuserId.setEnabled(false);
		}*/
		
//		txtuserId.setValue(userName != null ? userName.toUpperCase() : "");
		
		if(claimType != null ){
			claimType.setValue(null);
		}
		if(pendingReason != null){
			pendingReason.setValue(null);
		}
		
	}
	private void resetBtnListener(){
		btnReset.addClickListener(new Button.ClickListener() {
	
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				intimationNumber.setValue(null);
				cpuCode.setValue(null);
				hospitalCode.setValue(null);
			}
		});
	}
	
	private TabSheet auditQueryTabs(){
		
		auditQueryTab = new TabSheet();
		auditQueryTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		TabSheet queryPendingTab =  getQueryPendingTab();
//		TabSheet queryReplyRecievedTab =  getQueryReplyRecievedTab();
		TabSheet queryCompletedTab =  getQueryCompletedTab();
		auditQueryTab.addTab(queryCompletedTab, "Completed", null);
		auditQueryTab.addTab(queryPendingTab, "Pending", null);
//		auditQueryTab.addTab(queryReplyRecievedTab, "Reply Recieved", null);
	
		this.binder.getItemDataSource().getBean().setTabStatus(auditQueryTab.getSelectedTab().getId());
		auditQueryTab.addSelectedTabChangeListener(event -> {
			
			if(auditQueryTab.getSelectedTab().getId() != null && auditQueryTab.getSelectedTab().getId().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_COMPLETED)){
				//fieldLayout.removeAllComponents();
				removeAllExistsFields();
				HorizontalLayout formFieldLayout = getFormFields();
//				formFieldLayout.setSpacing(true);
				queryCompletedTab.addComponent(formFieldLayout);
				this.binder.getItemDataSource().getBean().setTabStatus(auditQueryTab.getSelectedTab().getId());
				
				
				
			}else if(auditQueryTab.getSelectedTab().getId() != null && auditQueryTab.getSelectedTab().getId().equalsIgnoreCase(SHAConstants.CVC_PENDING)){
				//fieldLayout.removeAllComponents();
				removeAllExistsFields();
				HorizontalLayout formFieldLayout = getFormFields();
//				formFieldLayout.setSpacing(true);
				/*FormLayout formLayoutMiddle = new FormLayout(pendingReason);
				formLayoutMiddle.setSpacing(true);
				formFieldLayout.addComponent(formLayoutMiddle);*/
				queryPendingTab.addComponent(formFieldLayout);
				if(pendingReason != null){
					pendingReason.setValue(null);
				}
				this.binder.getItemDataSource().getBean().setTabStatus(auditQueryTab.getSelectedTab().getId());
			}/*else if(auditQueryTab.getSelectedTab().getId() != null && auditQueryTab.getSelectedTab().getId().equalsIgnoreCase(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED)){
				fieldLayout.removeAllComponents();
				HorizontalLayout formFieldLayout = getFormFields();
				FormLayout formLayoutMiddle = new FormLayout(claimType);
				formLayoutMiddle.setSpacing(true);
				formFieldLayout.addComponent(formLayoutMiddle);
				queryReplyRecievedTab.addComponent(formFieldLayout);
				// new changes add 
				SearchHRMPFormDTO searchBean = this.binder.getItemDataSource().getBean();
				if(searchBean != null && !searchBean.isClmAuditHeadUser()){
					if(auditUserMapClaimUser != null && clmTypeContainer != null){
						
						for(int i = 0; i < clmTypeContainer.getItemIds().size(); i++) {
							if(auditUserMapClaimUser.getClmType().equalsIgnoreCase(clmTypeContainer.getIdByIndex(i).getValue())) {
								claimType.setValue(clmTypeContainer.getIdByIndex(i));
							}
						}
					}
				}else{
					claimType.setValue(null);
				}
				this.binder.getItemDataSource().getBean().setTabStatus(auditQueryTab.getSelectedTab().getId());
			}*/

	        });
		
		return auditQueryTab;
	}
	
	private TabSheet getQueryCompletedTab(){
		
		TabSheet queryCompletedTab = new TabSheet();

		queryCompletedTab.setStyleName("g-search-panel");
		queryCompletedTab.setId(SHAConstants.AUDIT_QUERY_COMPLETED);
		queryCompletedTab.hideTabs(true);
		queryCompletedTab.setWidth("100%");
		queryCompletedTab.setHeight("100%");
		queryCompletedTab.setSizeFull();
		
		HorizontalLayout formFieldLayout = getFormFields();
		queryCompletedTab.addComponent(formFieldLayout);
		return queryCompletedTab;
		
	}
	private TabSheet getQueryPendingTab(){
		
		TabSheet queryPendingTab = new TabSheet();

		queryPendingTab.setStyleName("g-search-panel");
		queryPendingTab.setId(SHAConstants.CVC_PENDING);
		queryPendingTab.hideTabs(true);
		queryPendingTab.setWidth("100%");
		queryPendingTab.setHeight("100%");
		queryPendingTab.setSizeFull();
		return queryPendingTab;
		
	}
	
	/*private TabSheet getQueryReplyRecievedTab(){
			
			TabSheet queryReplyRecievedTab = new TabSheet();
			queryReplyRecievedTab.setId(SHAConstants.AUDIT_QUERY_REPLY_RECEIVED);
			queryReplyRecievedTab.setStyleName("g-search-panel");
			queryReplyRecievedTab.hideTabs(true);
			queryReplyRecievedTab.setWidth("100%");
			queryReplyRecievedTab.setHeight("100%");
			queryReplyRecievedTab.setSizeFull();
			return queryReplyRecievedTab;
		
	}*/
	
	private HorizontalLayout getFormFields(){
		fieldLayout.removeAllComponents();
		intimationNumber.setValue(null);
		FormLayout formLayoutLeft = new FormLayout(intimationNumber,cpuCode);
		//formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(txtuserId,hospitalCode);
		//formLayoutRight.setSpacing(true);
		fieldLayout.addComponent(formLayoutLeft);
		fieldLayout.addComponent(formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		fieldLayout.setWidth("100%");
		
		
		return fieldLayout;
		
	}
	
	public void setAuditUser(MasClmAuditUserMapping auditUserMap) {
		auditUserMapClaimUser = auditUserMap;
		SearchHRMPFormDTO searchBean = this.binder.getItemDataSource().getBean();
		/*if(searchBean != null && !searchBean.isClmAuditHeadUser()) {
			if(auditUserMap != null && clmTypeContainer != null) {
				for(int i = 0; i < clmTypeContainer.getItemIds().size(); i++) {
					if(auditUserMap.getClmType().equalsIgnoreCase(clmTypeContainer.getIdByIndex(i).getValue())) {
						claimType.setValue(clmTypeContainer.getIdByIndex(i));
						claimType.setEnabled(false);
					}
				}
			}
		}
		else {
			claimType.setEnabled(true);
			claimType.setValue(null);
		}*/
	}
	
	public void loadSelectValueCombBox(){
		
		SelectValue approvalSelect = new SelectValue(2L, SHAConstants.AUDIT_QUERY_APPROVAL_PENDING);
		SelectValue qryReplyPendingSelect = new SelectValue(3L,SHAConstants.AUDIT_QUERY_REPLY_PENDING);
		SelectValue qryReplyRecievedSelect = new SelectValue(4L,SHAConstants.AUDIT_QUERY_REPLY_RECEIVED);
		SelectValue qryReplyRecievedSavedSelect = new SelectValue(1L,SHAConstants.AUDIT_QUERY_REPLY_RECEIVED_SAVED);
		pendingReasonContainer.addBean(approvalSelect);
		pendingReasonContainer.addBean(qryReplyPendingSelect);
		pendingReasonContainer.addBean(qryReplyRecievedSelect);
		pendingReasonContainer.addBean(qryReplyRecievedSavedSelect);
		
		
	}
	
	private void removeAllExistsFields(){
		intimationNumber.setValue(null);
		cpuCode.setValue(null);
		hospitalCode.setValue(null);
		refresh();
	}
}
