package com.shaic.claim.pcc.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.pcc.views.ProcessingDoctorDetailsTable;
import com.shaic.claim.pcc.views.QueryDetailsTable;
import com.shaic.claim.pcc.views.ReplyDetailsTable;
import com.shaic.claim.pcc.wizard.ProcessPccCoOrdinateRequestPresenter;
import com.shaic.claim.pcc.wizard.ProcessPccCoOrdinatorRequestPage;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableBancs;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ProcessPccProcessorRequestPage extends ViewComponent {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2738608714074662400L;
	
	@Inject
	private Instance<ProcessingDoctorDetailsTable> processingDoctorDetailsTableInst;
	
	private ProcessingDoctorDetailsTable processingDoctorDetailsTable;
	
	@Inject
	private Instance<QueryDetailsTable> queryDetailsTableInst;
	
	private QueryDetailsTable queryDetailsTable;

	@Inject
	private Instance<ReplyDetailsTable> replyDetailsTableInst;
	
	private ReplyDetailsTable replyDetailsTable;
	
	private PccDTO bean;

	private BeanFieldGroup<PccDTO> binder;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private PccDetailsTableDTO pccDetailsTableDTO;
	
	private String presenterString;
	
	private TextArea txtRemarksforApprove;
	
	private TextArea txtQueryforQuery;
	
	private TextArea txtRemarkForDisapprove;
	
	private TextArea txtResponse;
	
	private ComboBox cmbSelectRole;
		
	private ComboBox cmbSelectUserName;
	
	private TextField dummytext;
	
	private Boolean isApprove = false;
	
	private Boolean isQuery = false;
	
	private Boolean isResponse = false;
	
	private Boolean isDisapprove = false;
	
	private Button btnApprove;
	
	private Button btnQuery;
	
	private Button btnResponse;
	
	private Button btnDisapprove;
	
	private FormLayout approveFLayout;
	
	private FormLayout queryFLayout;
	
    private FormLayout disApproveFLayout;
	
	private FormLayout responseFLayout;
	
	private VerticalLayout dyanamicVLayout;
	
	private VerticalLayout mainVLayout;
	
	private FormLayout dummyForm;
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;
	
	private BeanItemContainer<SelectValue> userDetailsContainer;

	@PostConstruct
	protected void initView() {

	}

	public void init(PccDetailsTableDTO pccDetailsTableDTO, String presenterString) {
		
		this.presenterString=presenterString;
		this.pccDetailsTableDTO = pccDetailsTableDTO;
		
		this.binder = new BeanFieldGroup<PccDTO>(PccDTO.class);
		this.binder.setItemDataSource(new PccDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		dummyForm = new FormLayout();
		dummyForm.setWidth("500px");
		dummyForm.setHeight("45px");
		
		dummytext = new TextField();
		dummytext.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummytext.setWidth("-1px");
		dummytext.setHeight("-10px");
		
		cmbSelectRole = binder.buildAndBind("Select Role","userRoleAssigned",ComboBox.class);	
		cmbSelectUserName = binder.buildAndBind("Select User Name","userNameAssigned",ComboBox.class);
		
		txtRemarkForDisapprove = binder.buildAndBind("Remarks", "remarkForDisapprove", TextArea.class);
		txtRemarkForDisapprove.setMaxLength(1000);
		txtRemarkForDisapprove.setWidth("278px");
		txtRemarkForDisapprove.setHeight("130px");
		txtRemarkForDisapprove.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtRemarkForDisapprove,null,getUI(),SHAConstants.STP_REMARKS);

		
		txtQueryforQuery = binder.buildAndBind("Query", "remarksforQuery", TextArea.class);
		txtQueryforQuery.setMaxLength(1000);
		txtQueryforQuery.setWidth("278px");
		txtQueryforQuery.setHeight("130px");
		txtQueryforQuery.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtQueryforQuery,null,getUI(),SHAConstants.STP_REMARKS);
		
		txtRemarksforApprove = binder.buildAndBind("Remarks", "remarksforApprove", TextArea.class);
		txtRemarksforApprove.setMaxLength(1000);
		txtRemarksforApprove.setWidth("278px");
		txtRemarksforApprove.setHeight("130px");
		txtRemarksforApprove.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtRemarksforApprove,null,getUI(),SHAConstants.STP_REMARKS);
		
		txtResponse = binder.buildAndBind("Remarks", "remarkForResponse", TextArea.class);
		txtResponse.setMaxLength(1000);
		txtResponse.setWidth("278px");
		txtResponse.setHeight("130px");
		txtResponse.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtResponse,null,getUI(),SHAConstants.STP_REMARKS);
		
		processingDoctorDetailsTable = processingDoctorDetailsTableInst.get();
		processingDoctorDetailsTable.init("", false, false);
		processingDoctorDetailsTable.setCaption("Processing Doctor Details");
		if(pccDetailsTableDTO !=null){
			List<PccDetailsTableDTO> pccDetailsTableDTOs = new ArrayList<PccDetailsTableDTO>();
			pccDetailsTableDTOs.add(pccDetailsTableDTO);
			processingDoctorDetailsTable.setTableList(pccDetailsTableDTOs);
		}
		
		queryDetailsTable = queryDetailsTableInst.get();
		queryDetailsTable.init("", false, false);
		queryDetailsTable.setCaption("Query Details");
		if(pccDetailsTableDTO.getQueryDetails() !=null 
				&& !pccDetailsTableDTO.getQueryDetails().isEmpty()){
			queryDetailsTable.setTableList(pccDetailsTableDTO.getQueryDetails());
		}
		
		replyDetailsTable = replyDetailsTableInst.get();
		replyDetailsTable.init("", false, false);
		replyDetailsTable.setCaption("Reply Details");
		if(pccDetailsTableDTO.getReplyDetails() !=null 
				&& !pccDetailsTableDTO.getReplyDetails().isEmpty()){
			replyDetailsTable.setTableList(pccDetailsTableDTO.getReplyDetails());
		}
				
		btnApprove=new Button("Approve");
		btnQuery=new Button("Query");
		btnResponse=new Button("Response");
		btnDisapprove= new Button("Disapprove");
			
		HorizontalLayout appBtnLayout= null;
		
		if(pccDetailsTableDTO.getIsResponceRecord()){
			appBtnLayout = new HorizontalLayout(btnQuery,btnResponse);
		}else{
			appBtnLayout = new HorizontalLayout(btnDisapprove,btnQuery,btnApprove);
		}
		appBtnLayout.setSpacing(true);
		appBtnLayout.setMargin(false);
		
		dyanamicVLayout = new VerticalLayout();
		dyanamicVLayout.setVisible(false);

		mainVLayout = new VerticalLayout(processingDoctorDetailsTable,queryDetailsTable,replyDetailsTable,dummytext,appBtnLayout,dyanamicVLayout);
		mainVLayout.setComponentAlignment(appBtnLayout, Alignment.BOTTOM_LEFT);
		mainVLayout.setComponentAlignment(dyanamicVLayout, Alignment.BOTTOM_CENTER);

		addListener();
		setCompositionRoot(mainVLayout);

	}
	
	public void addListener() {
		
		btnDisapprove.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isDisapprove = true;
				isApprove = false;
				isQuery = false;
				isResponse = false;
				fireViewEvent(ProcessPccProcessorRequestPresenter.PCCCPROCESSOR_GENERATE_DISAPPROVE_LAYOUT,null);
			}
		});
		
		btnResponse.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isDisapprove = false;
				isApprove = false;
				isQuery = false;
				isResponse = true;
				fireViewEvent(ProcessPccProcessorRequestPresenter.PCCCPROCESSOR_GENERATE_RESPONSE_LAYOUT,null);
			}
		});
			
		btnApprove.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isDisapprove = false;
				isApprove = true;
				isQuery = false;
				isResponse = false;
				fireViewEvent(ProcessPccProcessorRequestPresenter.PCCCPROCESSOR_GENERATE_APPROVE_LAYOUT,null);
			}
		});
		
		
		btnQuery.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isDisapprove = false;
				isApprove = false;
				isQuery = true;
				isResponse = false;
				fireViewEvent(ProcessPccProcessorRequestPresenter.PCCCPROCESSOR_GENERATE_QUERY_LAYOUT,null);
			}
		});
		
		cmbSelectRole.addListener(new Listener(){
			@Override
			public void componentEvent(Event event) {
				if (cmbSelectRole.getValue() !=null) {
					SelectValue selectValue = (SelectValue)cmbSelectRole.getValue();
					Long pccKey = pccDetailsTableDTO.getPccKey();
					PCCRequest pccRequest =pccRequestService.getPCCRequestByKey(pccKey);
					Long cpuCode = 0L;
					if(pccRequest!=null){
						TmpCPUCode cpuCodeObject=  pccRequest.getIntimation().getCpuCode();
						if(cpuCodeObject !=null){
							 cpuCode= cpuCodeObject.getCpuCode();
							System.out.println(String.format("Cpu Code in process Page [%s]", cpuCode));
						}
					}
					if(selectValue.getCommonValue() !=null){
						/*if(!isDisapprove){*/
							fireViewEvent(ProcessPccProcessorRequestPresenter.PCCCPROCESSOR_GENERATE_USER_DETAILS,selectValue.getCommonValue(),cpuCode);
						/*}*/
					}
				}
			}			
		});

	}

	/*private void getCpuCode() {
		
		Long pccKey = pccDetailsTableDTO.getPccKey();
		System.out.println(String.format("PCC KEY In Processor Page [%s]", pccKey));
		PCCRequest pccRequest =pccRequestService.getPCCRequestByKey(pccKey);
		Long cpuCode = 0L;
		if(pccRequest!=null){
			TmpCPUCode cpuCodeObject=  pccRequest.getIntimation().getCpuCode();
			if(cpuCodeObject !=null){
				
				 cpuCode= cpuCodeObject.getCpuCode();
				System.out.println(String.format("Cpu Code in process Page [%s]", cpuCode));
			}
		}
		
	}*/
	

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}

	public Boolean validatePage() {

		Boolean hasError=false;
		StringBuffer eMsg = new StringBuffer(); 

		if(isApprove){	
			
			if(txtRemarksforApprove != null && txtRemarksforApprove.getValue() == null){
				hasError=true;
				eMsg.append("Enter Remarks for Approve to proceed </br> ");
			}
			if(cmbSelectUserName != null && cmbSelectUserName.getValue() == null){			
				hasError=true;
				eMsg.append("Select UserName to proceed. </br> ");
			}
		}

		if(isQuery){
			if(cmbSelectRole != null && cmbSelectRole.getValue() == null){			
				hasError=true;
				eMsg.append("Select Role to proceed. </br> ");
			}
			if(cmbSelectUserName != null && cmbSelectUserName.getValue() == null){			
				hasError=true;
				eMsg.append("Select UserName to proceed. </br> ");
			}
			if(txtQueryforQuery != null && txtQueryforQuery.getValue() == null){
				hasError=true;
				eMsg.append("Enter Remarks for Query to proceed. </br> ");
			}
		} 
		
		if(isDisapprove){

			if(cmbSelectRole != null && cmbSelectRole.getValue() == null){			
				hasError=true;
				eMsg.append("Select Role to proceed. </br> ");
			}
			if(txtRemarkForDisapprove != null && txtRemarkForDisapprove.getValue() == null){
				hasError=true;
				eMsg.append("Enter Remarks to proceed. </br> ");
			}
			if(cmbSelectUserName != null && cmbSelectUserName.getValue() == null){			
				hasError=true;
				eMsg.append("Select UserName to proceed. </br> ");
			}
		
		}
		
		if(isResponse){

			if(cmbSelectRole != null && cmbSelectRole.getValue() == null){			
				hasError=true;
				eMsg.append("Select Role to proceed. </br> ");
			}
			if(cmbSelectUserName != null && cmbSelectUserName.getValue() == null){			
				hasError=true;
				eMsg.append("Select UserName to proceed. </br> ");
			}
			if( txtResponse != null && txtResponse.getValue() == null){
				hasError=true;
				eMsg.append("Enter Remarks to proceed. </br> ");
			}
		
		}
		
		if(!isApprove && !isQuery && !isDisapprove && !isResponse){
			hasError=true;
			eMsg.append("Select Approve / Query / Response / Disapprove to proceed. </br> ");
		}
		if (hasError) {		
			MessageBox.createError()
			.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
			.withOkButton(ButtonOption.caption("OK")).open();
			hasError = true;
			return !hasError;
		} 
		return !hasError;
	}
	
	public void generateapproveLayout() {
		
			unbindandRemoveComponents();
			
			unbindField(txtResponse);
			unbindField(txtQueryforQuery);
			unbindField(txtRemarkForDisapprove);
			unbindField(cmbSelectUserName);
			
			mandatoryFields.add(cmbSelectRole);
			mandatoryFields.add(cmbSelectUserName);
			mandatoryFields.add(txtRemarksforApprove);
			showOrHideValidation(false);

			approveFLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtRemarksforApprove);
			HorizontalLayout hLyout = new HorizontalLayout(dummyForm,approveFLayout);
			dyanamicVLayout.addComponent(hLyout);
			dyanamicVLayout.setVisible(true);	
			setRolesByActions("PCC_PROCESSOR_APPROVE");
	}

	public void generateQuerryLayout() {

		unbindandRemoveComponents();
		unbindField(txtResponse);
		unbindField(txtRemarksforApprove);
		unbindField(txtRemarkForDisapprove);
		
		mandatoryFields.add(cmbSelectRole);
		mandatoryFields.add(cmbSelectUserName);
		mandatoryFields.add(txtQueryforQuery);
		showOrHideValidation(false);
		
		cmbSelectUserName.setEnabled(true);
		cmbSelectUserName.setValue(null);
		queryFLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtQueryforQuery);
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,queryFLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);	
		setRolesByActions("PCC_PROCESSOR_QUERY");
	}
	
	public void generateDisapproveLayout() {
		
		unbindandRemoveComponents();
		unbindField(txtResponse);
		unbindField(txtQueryforQuery);
		unbindField(txtRemarksforApprove);
		unbindField(cmbSelectUserName);
		
		mandatoryFields.add(cmbSelectRole);
		mandatoryFields.add(cmbSelectUserName);
		mandatoryFields.add(txtRemarkForDisapprove);
		showOrHideValidation(false);

		disApproveFLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtRemarkForDisapprove);
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,disApproveFLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);	
		setRolesByActions("PCC_PROCESSOR_DISAPPROVE");
		
	}

	public void generateResponseLayout() {

		unbindandRemoveComponents();
		
		unbindField(txtRemarksforApprove);
		unbindField(txtQueryforQuery);
		unbindField(txtRemarkForDisapprove);
		
		mandatoryFields.add(txtResponse);		
		showOrHideValidation(false);
		
		responseFLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtResponse);
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,responseFLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);	
		setRolesByActions("PCC_PROCESSOR_RESPONCE");

		
	}
	
	private void addUserRole(BeanItemContainer<SelectValue> userRoles,SelectValue selectedrole,Boolean isEnabel) {

		unbindField(cmbSelectRole);
		cmbSelectRole.setContainerDataSource(userRoles);
		cmbSelectRole.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSelectRole.setItemCaptionPropertyId("value");
		cmbSelectRole.setEnabled(isEnabel);
		
		if(selectedrole !=null){
			cmbSelectRole.setValue(selectedrole);
		}
		
	}

	public void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer) {

		unbindField(cmbSelectUserName);
		this.userDetailsContainer = userDetailsContainer;
		cmbSelectUserName.setContainerDataSource(this.userDetailsContainer);
		cmbSelectUserName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSelectUserName.setItemCaptionPropertyId("value");
		if(isResponse){
			cmbSelectUserName.setValue(pccDetailsTableDTO.getResponceUser());
			cmbSelectUserName.setEnabled(false);
		}
	}
	
	public PccDTO getvalues(){


		PccDTO pccDTO = binder.getItemDataSource().getBean();
		if(cmbSelectRole !=null && cmbSelectRole.getValue() !=null){
			pccDTO.setUserRoleAssigned((SelectValue)cmbSelectRole.getValue());
		}
		//if(!isApprove && !isDisapprove){
			if(cmbSelectUserName !=null && cmbSelectUserName.getValue() !=null){
				pccDTO.setUserNameAssigned((SelectValue)cmbSelectUserName.getValue());
			}
		//}
		if(isApprove){	

			if(txtRemarksforApprove != null && txtRemarksforApprove.getValue() != null){
				pccDTO.setRemarksforApprove(txtRemarksforApprove.getValue());
			}
		}

		if(isQuery){		
			if(txtQueryforQuery != null && txtQueryforQuery.getValue() != null){
				pccDTO.setRemarksforQuery(txtQueryforQuery.getValue());
			}
		} 

		if(isDisapprove){
			if(txtRemarkForDisapprove != null && txtRemarkForDisapprove.getValue() != null){
				pccDTO.setRemarkForDisapprove(txtRemarkForDisapprove.getValue());
			}
		}

		if(isResponse){
			if( txtResponse != null && txtResponse.getValue() != null){
				pccDTO.setRemarkForResponse(txtResponse.getValue());
			}
		}

		pccDTO.setPccKey(pccDetailsTableDTO.getPccKey());
		pccDTO.setIsApproved(isApprove);
		pccDTO.setIsQueryRaised(isQuery);
		pccDTO.setIsDisapproved(isDisapprove);
		pccDTO.setIsResponse(isResponse);
		return pccDTO;

	}
	
	 private void setRolesByActions(String action){

		  ArrayList<String> roles;
		BeanItemContainer<SelectValue> userRoles = null;	

		 if(action.equals("PCC_PROCESSOR_QUERY")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.DIVISION_HEAD_ROLE);
			  roles.add(SHAConstants.PCC_REVIEWER_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  addUserRole(userRoles,null,true);
		  }else if(action.equals("PCC_PROCESSOR_APPROVE")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE);
			  addUserRole(userRoles,selectedrole,false);
		  }else if(action.equals("PCC_PROCESSOR_DISAPPROVE")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.PCC_COORDINATOR_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.PCC_COORDINATOR_ROLE);
			  addUserRole(userRoles,selectedrole,false);
		  }else if(action.equals("PCC_PROCESSOR_RESPONCE")){
			  userRoles = new BeanItemContainer<SelectValue>(SelectValue.class);
			  userRoles.addBean(pccDetailsTableDTO.getResponceRole());
			  addUserRole(userRoles,pccDetailsTableDTO.getResponceRole(),false);
		  }
	  }

	private void unbindandRemoveComponents(){
		
		if (dyanamicVLayout != null
				&& dyanamicVLayout.getComponentCount() > 0) {
			dyanamicVLayout.removeAllComponents();
		}
		if(responseFLayout !=null 
				&& responseFLayout.getComponentCount() >0){
			responseFLayout.removeAllComponents();
			this.responseFLayout = null;
		}
		if(disApproveFLayout !=null 
				&& disApproveFLayout.getComponentCount() >0){
			disApproveFLayout.removeAllComponents();
			this.disApproveFLayout = null;
		}
		
		if(approveFLayout !=null 
				&& approveFLayout.getComponentCount() >0){
			approveFLayout.removeAllComponents();
			this.approveFLayout = null;
		}	
		if(queryFLayout !=null 
				&& queryFLayout.getComponentCount() >0){
			queryFLayout.removeAllComponents();
			this.queryFLayout = null;
		}	

	}

}
