package com.shaic.claim.cvc.auditqueryreplyprocessing.fa;

import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;


public class SearchCVCAuditFaQryForm extends SearchComponent<SearchCVCAuditClsQryFormDTO>{
	
	private SearchCVCAuditClsQryFormDTO searchDto;
	
	private SearchCVCAuditFaQryTable searchTable;
	
	@EJB
	private MasterService masterService;	
	
	private ComboBox cmbRole;	
	
	private ComboBoxMultiselect cmbEmpId;
	
	private BeanItemContainer<SelectValue> auditUserRoleContainer;
	
	private BeanItemContainer<SelectValue> empContainer;

	private ComboBoxMultiselect cpuCodeFilter;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Claim Audit Query (FA)");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
		resetBtnListener();
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		

		cmbRole = binder.buildAndBind("Role", "userRoleSelect", ComboBox.class);
		cmbRole.setNullSelectionAllowed(Boolean.FALSE);
		
		cmbRole.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
				if(event.getProperty().getValue() != null) {
					SelectValue roleSelect = (SelectValue)event.getProperty().getValue();
					empContainer = masterService.getAuditQryReplyUser(roleSelect.getValue(),SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN);
					cmbEmpId.setContainerDataSource(empContainer);
					binder.getItemDataSource().getBean().setUserRoleName(roleSelect.getValue().toLowerCase());
				}				
			}
		});
		
		/*if(searchDto != null && (SHAConstants.CVC_AUDIT_QRY_RPL_ZONAL_USER.equalsIgnoreCase(searchDto.getUserRoleName())
				|| SHAConstants.CVC_AUDIT_QRY_RPL_CLUSTER_HEAD.equalsIgnoreCase(searchDto.getUserRoleName()))){*/
			cmbRole.setEnabled(false);
			
		/*}
		else{
			cmbRole.setEnabled(true);
		}*/
		

		cmbEmpId = new ComboBoxMultiselect("User ID");
//		empContainer = masterService.getAuditReplyUser(SHAConstants.CASHLESS.toLowerCase(),SHAConstants.AUDIT_TEAM_MEDICAL.toLowerCase());
		
		cmbEmpId.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
				if(null != cmbEmpId){
					binder.getItemDataSource().getBean().setUserId(cmbEmpId.getValue().toString());
				}
			}
		});

		cpuCodeFilter = new ComboBoxMultiselect("CPU Code"); 
		cpuCodeFilter.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
				binder.getItemDataSource().getBean().setCpuCode(cpuCodeFilter.getValue().toString());
			}
		});

		FormLayout formLayoutMiddle = new FormLayout(cmbRole);
		FormLayout formLayoutfirst = new FormLayout(cpuCodeFilter);
		FormLayout formLayoutRight1 = new FormLayout(cmbEmpId);
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutfirst,formLayoutMiddle, formLayoutRight1);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		fieldLayout.setWidth("100%");
		fieldLayout.setSizeFull();
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:100.0px;left:220.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:100.0px;left:320.0px;");
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		mainVerticalLayout.setWidth("100%");
		mainVerticalLayout.setMargin(false);		 
		//Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("150px");
		 
		addListener();
		return mainVerticalLayout;

	}
	private void initBinder()

	{
		this.binder = new BeanFieldGroup<SearchCVCAuditClsQryFormDTO>(SearchCVCAuditClsQryFormDTO.class);
		this.binder.setItemDataSource(new SearchCVCAuditClsQryFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setDropDownValues(SearchCVCAuditClsQryFormDTO srchFrmDto) {
		
		searchDto = srchFrmDto;
		
		auditUserRoleContainer = masterService.getAuditReplyUserRoles();
		
		cmbRole.setContainerDataSource(auditUserRoleContainer);
		cmbRole.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRole.setItemCaptionPropertyId("value");
		
	/*	if(searchDto != null && SHAConstants.CVC_AUDIT_QRY_RPL_ZONAL_USER.equalsIgnoreCase(searchDto.getUserRoleName())){
			cmbEmpId.setEnabled(false);
		}
		else{
			cmbEmpId.setEnabled(true);
		}*/
		
		for(int i=0;i<auditUserRoleContainer.size();i++){
			if ((searchDto.getUserRoleName()).equalsIgnoreCase(auditUserRoleContainer.getIdByIndex(i).getValue())){
				cmbRole.setValue(auditUserRoleContainer.getIdByIndex(i));
				break;
			}
			
		}

		/*if(searchDto != null && (SHAConstants.CVC_AUDIT_QRY_RPL_ZONAL_USER.equalsIgnoreCase(searchDto.getUserRoleName())
				|| SHAConstants.CVC_AUDIT_QRY_RPL_CLUSTER_HEAD.equalsIgnoreCase(searchDto.getUserRoleName()))){
		
			btnReset.setEnabled(false);	
			cmbRole.setEnabled(false);
			
		}
		else{
			cmbRole.setEnabled(true);
		}*/
		cmbRole.setEnabled(false);
		
		empContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(searchDto != null) {
			empContainer.addBean(new SelectValue(1L,searchDto.getUsername()));
		}
		
		HashSet<SelectValue> emIdValues = new HashSet<SelectValue>();
		
		emIdValues.add((SelectValue)empContainer.getIdByIndex(0));		
		
		cmbEmpId.setContainerDataSource(empContainer);
		cmbEmpId.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbEmpId.setItemCaptionPropertyId("value");
		cmbEmpId.setValue(emIdValues);
		cmbEmpId.setEnabled(false);
		
		if(searchDto.getCpuListContainer() != null){
			HashSet<SelectValue> cpuIdValues = new HashSet<SelectValue>();
			
			binder.getItemDataSource().getBean().setCpuListContainer(searchDto.getCpuListContainer());
			
			cpuCodeFilter.setContainerDataSource(searchDto.getCpuListContainer());
			cpuCodeFilter.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cpuCodeFilter.setItemCaptionPropertyId("value");
			cpuCodeFilter.setValue(null);
		}
	}
	private void resetBtnListener(){
	btnReset.addClickListener(new Button.ClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			setDropDownValues(searchDto);		
		}
	});
	}
}
