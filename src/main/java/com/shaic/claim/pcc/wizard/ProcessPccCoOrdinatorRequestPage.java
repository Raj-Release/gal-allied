package com.shaic.claim.pcc.wizard;

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
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.pcc.views.ProcessingDoctorDetailsTable;
import com.shaic.claim.pcc.views.QueryDetailsTable;
import com.shaic.claim.pcc.views.ReplyDetailsTable;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.premedical.listenerTables.ImplantTableListener;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.themes.Reindeer;

public class ProcessPccCoOrdinatorRequestPage extends ViewComponent {


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
	
	private TextField txtNegotiaitedAmmount;
	
	private TextField txtSavedAmount;
	
	private TextArea txtQueryforQuery;
	
	private ComboBox cmbSelectRole;
		
	private ComboBox cmbSelectUserName;
	
	private TextField dummytext;
	
	private Boolean isApprove = false;
	
	private Boolean isQuery = false;
	
	private Button btnApprove;
	
	private Button btnQuery;
	
	private FormLayout approveFLayout;
	
	private FormLayout queryFLayout;
	
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
		
		txtNegotiaitedAmmount=binder.buildAndBind("Negotiated Amount","negotiatioAmount",TextField.class);
		txtNegotiaitedAmmount.setNullRepresentation("");
		CSValidator txtAmtToNeg = new CSValidator();
		txtAmtToNeg.setRegExp("^[0-9']*$");
		txtAmtToNeg.setPreventInvalidTyping(true);
		txtAmtToNeg.extend(txtNegotiaitedAmmount);
		
		txtSavedAmount = binder.buildAndBind("Saved Amount","savedAmount",TextField.class);
		txtSavedAmount.setNullRepresentation("");
		CSValidator txtSaveAmt = new CSValidator();
		txtSaveAmt.setRegExp("^[0-9']*$");
		txtSaveAmt.setPreventInvalidTyping(true);
		txtSaveAmt.extend(txtSavedAmount);
		txtSavedAmount.addBlurListener(setNegotiationsaveListener());
		
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
			
		HorizontalLayout appBtnLayout=new HorizontalLayout(btnApprove,btnQuery);
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
			
		btnApprove.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isApprove = true;
				isQuery = false;
				fireViewEvent(ProcessPccCoOrdinateRequestPresenter.PCCCOORDINATE_GENERATE_APPROVE_LAYOUT,null);
			}
		});
		
		
		btnQuery.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isQuery = true;
				isApprove = false;
				fireViewEvent(ProcessPccCoOrdinateRequestPresenter.PCCCOORDINATE_GENERATE_QUERY_LAYOUT,null);
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
						fireViewEvent(ProcessPccCoOrdinateRequestPresenter.PCCCOORDINATE_GENERATE_USER_DETAILS,selectValue.getCommonValue(),cpuCode);
					}
				}
			}			
		});

	}


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
			if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() == null){			
				hasError=true;
				eMsg.append("Enter negotiated amount to proceed. </br> ");
			}
			if(txtSavedAmount != null && txtSavedAmount.getValue() == null){			
				hasError=true;
				eMsg.append("Enter saved amount to proceed. </br> ");
			}
			if(txtRemarksforApprove != null && txtRemarksforApprove.getValue() == null){
				hasError=true;
				eMsg.append("Enter Remarks for Approve to proceed </br> ");
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
			
			if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() == null){			
				hasError=true;
				eMsg.append("Enter negotiated amount to proceed. </br> ");
			}
			if(txtSavedAmount != null && txtSavedAmount.getValue() == null){			
				hasError=true;
				eMsg.append("Enter saved amount to proceed. </br> ");
			}
		} 
		
		if(!isApprove && !isQuery){
			hasError=true;
			eMsg.append("Select Approve / Query to proceed. </br> ");
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
		
			if (dyanamicVLayout != null
					&& dyanamicVLayout.getComponentCount() > 0) {
				dyanamicVLayout.removeAllComponents();
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
			
			unbindField(cmbSelectRole);
			unbindField(cmbSelectUserName);
			unbindField(txtNegotiaitedAmmount);
			unbindField(txtSavedAmount);
			unbindField(txtRemarksforApprove);
			unbindField(txtQueryforQuery);
			
			mandatoryFields.add(txtNegotiaitedAmmount);
			mandatoryFields.add(txtSavedAmount);
			mandatoryFields.add(txtRemarksforApprove);
			showOrHideValidation(false);

			approveFLayout=new FormLayout(txtNegotiaitedAmmount,txtSavedAmount,txtRemarksforApprove);
			HorizontalLayout hLyout = new HorizontalLayout(dummyForm,approveFLayout);
			dyanamicVLayout.addComponent(hLyout);
			dyanamicVLayout.setVisible(true);	
	}

	public void generateQuerryLayout() {

		if (dyanamicVLayout != null
				&& dyanamicVLayout.getComponentCount() > 0) {
			dyanamicVLayout.removeAllComponents();
		}
		if(queryFLayout !=null 
				&& queryFLayout.getComponentCount() >0){
			queryFLayout.removeAllComponents();
			this.queryFLayout = null;
		}	
		if(approveFLayout !=null 
				&& approveFLayout.getComponentCount() >0){
			approveFLayout.removeAllComponents();
			this.approveFLayout = null;
		}

		unbindField(cmbSelectRole);
		unbindField(cmbSelectUserName);
		unbindField(txtNegotiaitedAmmount);
		unbindField(txtSavedAmount);
		unbindField(txtQueryforQuery);
		unbindField(txtRemarksforApprove);
		
		mandatoryFields.add(cmbSelectRole);
		mandatoryFields.add(cmbSelectUserName);
		mandatoryFields.add(txtNegotiaitedAmmount);
		mandatoryFields.add(txtSavedAmount);
		mandatoryFields.add(txtQueryforQuery);
		showOrHideValidation(false);
		
		/*txtNegotiaitedAmmount.setRequired(false);
		txtNegotiaitedAmmount.setValidationVisible(false);	
		txtSavedAmount.setRequired(false);
		txtSavedAmount.setValidationVisible(false);*/
		if(pccDetailsTableDTO.getPccCategory() !=null &&
				pccDetailsTableDTO.getPccCategory().getId() !=null){
			if(pccDetailsTableDTO.getPccCategory().getId().equals(101L)){
				setRolesByActions("ANH_QUERY");
			}else{
				setRolesByActions("OTHERS_QUERY");
			}
		}
		
		queryFLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtNegotiaitedAmmount,txtSavedAmount,txtQueryforQuery);
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,queryFLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);	
	}

	public void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer) {

		unbindField(cmbSelectUserName);
		this.userDetailsContainer = userDetailsContainer;
		cmbSelectUserName.setContainerDataSource(this.userDetailsContainer);
		cmbSelectUserName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSelectUserName.setItemCaptionPropertyId("value");
	}
	
	public PccDTO getvalues(){


		//this.binder.commit();
		PccDTO pccDTO = binder.getItemDataSource().getBean();
		pccDTO.setPccKey(pccDetailsTableDTO.getPccKey());
		pccDTO.setIsApproved(isApprove);
		pccDTO.setIsQueryRaised(isQuery);
		if(isApprove){	
			if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() != null){	
				String negovalue = txtNegotiaitedAmmount.getValue().replace(",", "");
				pccDTO.setNegotiatioAmount(Long.parseLong(negovalue));
			}
			if(txtSavedAmount != null && txtSavedAmount.getValue() != null){			
				String negovalue = txtSavedAmount.getValue().replace(",", "");
				pccDTO.setSavedAmount(Long.parseLong(negovalue));
			}
			if(txtRemarksforApprove != null && txtRemarksforApprove.getValue() != null){
				pccDTO.setRemarksforApprove(txtRemarksforApprove.getValue());
			}
		}

		if(isQuery){
			if(cmbSelectRole != null && cmbSelectRole.getValue() != null){			
				pccDTO.setUserRoleAssigned((SelectValue)cmbSelectRole.getValue());
			}
			if(cmbSelectUserName != null && cmbSelectUserName.getValue() != null){			
				pccDTO.setUserNameAssigned((SelectValue)cmbSelectUserName.getValue());
			}
			if(txtQueryforQuery != null && txtQueryforQuery.getValue() != null){
				pccDTO.setRemarksforQuery(txtQueryforQuery.getValue());
			}
			if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() != null){	
				String negovalue = txtNegotiaitedAmmount.getValue().replace(",", "");
				pccDTO.setNegotiatioAmount(Long.parseLong(negovalue));
			}
			if(txtSavedAmount != null && txtSavedAmount.getValue() != null){			
				String negovalue = txtSavedAmount.getValue().replace(",", "");
				pccDTO.setSavedAmount(Long.parseLong(negovalue));
			}
		} 
		return pccDTO;
	}
	
	
	private void setRolesByActions(String action){

		 ArrayList<String> roles;
		BeanItemContainer<SelectValue> userRoles = null;	

		 if(action.equals("ANH_QUERY")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.DIVISION_HEAD_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.DIVISION_HEAD_ROLE);
			  addUserRole(userRoles,selectedrole,false);
		  }else if(action.equals("OTHERS_QUERY")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.DIVISION_HEAD_ROLE);
			  roles.add(SHAConstants.PCC_REVIEWER_ROLE);
			  roles.add(SHAConstants.PCC_PROCESSOR_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE);
			  addUserRole(userRoles,selectedrole,true);
		  }
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
	
	private BlurListener setNegotiationsaveListener(){

		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event){

				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					if(txtNegotiaitedAmmount !=null && txtNegotiaitedAmmount.getValue() !=null){
						Integer saveamunt = SHAUtils.getIntegerFromStringWithComma(value.getValue());
						Integer negoamt = SHAUtils.getIntegerFromStringWithComma(txtNegotiaitedAmmount.getValue());
						if(saveamunt > negoamt) {
							String eMsg = "Saved amount is greater than negotiated amount. Please enter a lesser amount in saved amount field";
							SHAUtils.showErrorMessageBoxWithCaption(eMsg, "Error");
							txtSavedAmount.setValue("");
						} 	
					}
				}
			}
		};
		return listener;
	}

}
