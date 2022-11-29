package com.shaic.claim.pcc.reviewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.pcc.views.ProcessingDoctorDetailsTable;
import com.shaic.claim.pcc.views.QueryDetailsTable;
import com.shaic.claim.pcc.views.ReplyDetailsTable;
import com.shaic.claim.pcc.wizard.ProcessPccCoOrdinateRequestPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
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
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ProcessPccReviwerRequestPage extends ViewComponent {


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

	private SearchProcessPCCRequestTableDTO dto;

	private String presenterString;

	private ComboBox cmbSelectRole;

	private ComboBox cmbSelectUserName;

	private TextArea txtQuery;

	private TextArea txtResponse;

	private Boolean isQuery;

	private Boolean isResponse;

	private Button btnQuery;

	private Button btnResponse;

	private FormLayout responseFLayout;

	private FormLayout queryFLayout;

	private TextField dummytext;

	private FormLayout dummyForm;

	private PccDetailsTableDTO pccDetailsTableDTO;

	private VerticalLayout mainVLayout;

	private VerticalLayout dyanamicVLayout;

	private BeanItemContainer<SelectValue> userDetailsContainer;

	@EJB
	private SearchProcessPCCRequestService pccRequestService;

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

		txtQuery = binder.buildAndBind("Query", "remarksforQuery", TextArea.class);
		txtQuery.setMaxLength(1000);
		txtQuery.setWidth("278px");
		txtQuery.setHeight("130px");
		txtQuery.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtQuery,null,getUI(),SHAConstants.STP_REMARKS);


		txtResponse = binder.buildAndBind("Response", "remarkForResponse", TextArea.class);
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

		btnResponse=new Button("Response");
		btnQuery=new Button("Query");

		HorizontalLayout appBtnLayout=new HorizontalLayout(btnResponse,btnQuery);
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

	private Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}


	public void addListener() {

		btnResponse.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				isResponse = true;
				isQuery = false;
				fireViewEvent(ProcessPccReviewerRequestPresenter.PCC_REVIEWER_GENERATE_RESPONSE_LAYOUT,null);
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
				isResponse = false;
				fireViewEvent(ProcessPccReviewerRequestPresenter.PCC_REVIEWER_GENERATE_QUERY_LAYOUT,null);
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
						fireViewEvent(ProcessPccReviewerRequestPresenter.PCC_REVIEWER_GENERATE_USER_DETAILS,selectValue.getCommonValue(),cpuCode);
					}
				}
			}			
		});
	}



	protected void generatedResponseLayout() {


		if (dyanamicVLayout != null
				&& dyanamicVLayout.getComponentCount() > 0) {
			dyanamicVLayout.removeAllComponents();
		}
		if(responseFLayout !=null 
				&& responseFLayout.getComponentCount() >0){
			responseFLayout.removeAllComponents();
			this.responseFLayout = null;
		}	
		if(queryFLayout !=null 
				&& queryFLayout.getComponentCount() >0){
			queryFLayout.removeAllComponents();
			this.queryFLayout = null;
		}	

		mandatoryFields.add(cmbSelectRole);
		mandatoryFields.add(cmbSelectUserName);
		mandatoryFields.add(txtResponse);
		showOrHideValidation(false);

		setRolesByActions("PCC_REVIEWER_RESPONCE");
		responseFLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtResponse);
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,responseFLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);	

	}

	protected void generatedQueryLayout() {


		if (dyanamicVLayout != null
				&& dyanamicVLayout.getComponentCount() > 0) {
			dyanamicVLayout.removeAllComponents();
		}
		if(queryFLayout !=null 
				&& queryFLayout.getComponentCount() >0){
			queryFLayout.removeAllComponents();
			this.queryFLayout = null;
		}	
		if(responseFLayout !=null 
				&& responseFLayout.getComponentCount() >0){
			responseFLayout.removeAllComponents();
			this.responseFLayout = null;
		}

		mandatoryFields.add(cmbSelectRole);
		mandatoryFields.add(cmbSelectUserName);
		mandatoryFields.add(txtQuery);
		showOrHideValidation(false);

		cmbSelectRole.setEnabled(true);
		cmbSelectUserName.setEnabled(true);

		setRolesByActions("PCC_REVIEWER_QUERY");
		queryFLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtQuery);
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,queryFLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);

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

		if(isResponse){
			if(cmbSelectRole != null && cmbSelectRole.getValue() == null){			
				hasError=true;
				eMsg.append("Select Role to proceed. </br> ");
			}
			if(cmbSelectUserName != null && cmbSelectUserName.getValue() == null){			
				hasError=true;
				eMsg.append("Select UserName to proceed. </br> ");
			}
			if(txtResponse != null && txtResponse.getValue() == null){
				hasError=true;
				eMsg.append("Enter Remarks for Response to proceed. </br> ");
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
			if(txtQuery != null && txtQuery.getValue() == null){
				hasError=true;
				eMsg.append("Enter Remarks for Query to proceed. </br> ");
			}
		} 

		if(!isResponse && !isQuery){
			hasError=true;
			eMsg.append("Select Response / Query to proceed. </br> ");
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
		cmbSelectUserName.setEnabled(true);
		if(isResponse){
			SelectValue selectValue = (SelectValue)cmbSelectRole.getValue();
			if(selectValue.getCommonValue().equals(SHAConstants.PCC_COORDINATOR_ROLE)
					&& pccDetailsTableDTO.getIsdirectResponceRecord()){
				if(pccDetailsTableDTO.getDirectresponceUser() !=null){
					cmbSelectUserName.setValue(pccDetailsTableDTO.getDirectresponceUser());
					cmbSelectUserName.setEnabled(false);
				}	
			}else if(selectValue.getCommonValue().equals(SHAConstants.PCC_PROCESSOR_ROLE)
							&&pccDetailsTableDTO.getResponceUser() != null){
				cmbSelectUserName.setValue(pccDetailsTableDTO.getResponceUser());
				cmbSelectUserName.setEnabled(false);
			}	
		}
	}

	public PccDTO getvalues(){


		PccDTO pccDTO = binder.getItemDataSource().getBean();
		if(cmbSelectRole !=null && cmbSelectRole.getValue() !=null){
			pccDTO.setUserRoleAssigned((SelectValue)cmbSelectRole.getValue());
		}
		if(cmbSelectUserName !=null && cmbSelectUserName.getValue() !=null){
			pccDTO.setUserNameAssigned((SelectValue)cmbSelectUserName.getValue());
		}
		if(isResponse){
			if(txtResponse != null && txtResponse.getValue() != null){
				pccDTO.setRemarkForResponse(txtResponse.getValue());
			}
		}

		if(isQuery){
			if(txtQuery != null && txtQuery.getValue() != null){
				pccDTO.setRemarksforQuery(txtQuery.getValue());	
			}
		} 
		pccDTO.setPccKey(pccDetailsTableDTO.getPccKey());
		pccDTO.setIsResponse(isResponse);
		pccDTO.setIsQueryRaised(isQuery);
		if(cmbSelectUserName.getValue() !=null){
			pccDTO.setUserNameAssigned((SelectValue)cmbSelectUserName.getValue());
		}
		return pccDTO;

	}

	private void setRolesByActions(String action){

		ArrayList<String> roles;
		BeanItemContainer<SelectValue> userRoles = null;	

		if(action.equals("PCC_REVIEWER_QUERY")){
			roles = new ArrayList<String>();
			roles.add(SHAConstants.DIVISION_HEAD_ROLE);
			userRoles = pccRequestService.getPCCRoles(roles);
			SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.DIVISION_HEAD_ROLE);
			addUserRole(userRoles,selectedrole,false);
		}else if(action.equals("PCC_REVIEWER_RESPONCE")){
				roles = new ArrayList<String>();
				roles.add(SHAConstants.PCC_PROCESSOR_ROLE);
				roles.add(SHAConstants.PCC_COORDINATOR_ROLE);
				userRoles = pccRequestService.getPCCRoles(roles);
				addUserRole(userRoles,null,true);					
		}
	}

}
