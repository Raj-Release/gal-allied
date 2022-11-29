package com.shaic.claim.userproduct.document.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.itextpdf.text.io.GetBufferedRandomAccessSource;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.userproduct.document.ApplicableCpuDTO;
import com.shaic.claim.userproduct.document.ApplicableCpuTable;
import com.shaic.claim.userproduct.document.ProductAndDocumentTypeDTO;
import com.shaic.claim.userproduct.document.ProductDocTypeDTO;
import com.shaic.claim.userproduct.document.ProductDocTypePresenter;
import com.shaic.claim.userproduct.document.UserActivationPresenter;
import com.shaic.claim.userproduct.document.UserProductMapping;
import com.shaic.claim.userproduct.document.UserProductMappingService;
import com.shaic.domain.MasAreaCodeMapping;
import com.shaic.domain.MasAreaOrgUserCpuMapping;
import com.shaic.domain.MasOrgCpuMapping;
import com.shaic.domain.MasSecUserDiagnosisMapping;
import com.shaic.domain.MasSecVSPUserLabelMapping;
import com.shaic.domain.MasUser;
import com.shaic.domain.MasUserDiagnosis;
import com.shaic.domain.MastersValue;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.UserToOrgMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class UserActivationUI extends ViewComponent{
	@Inject
	private Instance<UserMgmtApplicableCpuTable> applicableCpuTableInstance;
	
	@Inject
	private UserMgmtApplicableCpuTable userCpuTable;
	
	@Inject
	private LimitTable limitTable;
	
	@Inject
	private UserMgmtLimit userMgmtLimit;
	
	@EJB
	private UserProductMappingService cpuCodeService;
	
	@EJB
	private UserMagmtService userMgmtService;
	
	private UserMgmtApplicableCpuTable applicableCpuTableObj;
	
	private UserManagementDTO bean;
	
	private BeanFieldGroup<UserManagementDTO> binder;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private TextField userName;
	
	private TextField userId;
	
	private ComboBox userType;
	
	private Long cpuKey;
	
	private CheckBox highValueClaim;
	
	private CheckBox deactivateUser;
	
	private TextField shiftStartTime;
	
	private TextField shiftEndTime;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	TabSheet productDocumentTab = null;
	
	// CR2019213
	private CheckBox manualFlag;
	
	private CheckBox manualCodingFlag;

	@Inject
	private Instance<UserMgmtDaignosisTable> dagnosisTableInstance;
	
	@Inject
	private Instance<UserManagementLabelTable> labelTableInstance;
	
	@Inject
	private Instance<UserMgmtProductMappingTable> productMappingTableInstance;
	
	@Inject
	private UserMgmtDaignosisTable dagnosisTable;
	
	private UserMgmtDaignosisTable dagnosisTableObj;
	
	@Inject
	private UserManagementLabelTable labelTable;
	
	private UserManagementLabelTable labelTableObj;
	
	
	
	private UserMgmtProductMappingTable productMappingTable;
	
	private UserMgmtProductMappingTable productMappingTableObj;
	
	private ComboBox autoAllocationClaim;
	
	private ComboBox ClaimFlagInUserMaster;
	
	private CheckBox restrictToBand;
	
	private CheckBox manualPickMAFlag;
	
	private CheckBox manualPickCombinedProcessFlag;
	
	@PostConstruct
	public void initView() {
		
	}
	
	public void initBinder() {
		
	}
	
	@SuppressWarnings("static-access")
	public void init(UserManagementDTO userManageDTO, BeanItemContainer<SelectValue> userTypeContainer, BeanItemContainer<SelectValue> documentTypeContainer,BeanItemContainer<SelectValue> claimFlagTypeContainer ){

		
		this.bean = userManageDTO;
		
		this.binder = new BeanFieldGroup<UserManagementDTO>(UserManagementDTO.class);
		
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		userName = (TextField) binder.buildAndBind("User Name","userName", TextField.class);
		userName.setNullSettingAllowed(false);
		userName.setReadOnly(true);
		
		userId = (TextField) binder.buildAndBind("User ID","userId", TextField.class);
		userId.setNullSettingAllowed(false);
		userId.setReadOnly(true);
		
		userType = (ComboBox) binder.buildAndBind("User Type",
				"type", ComboBox.class);
		
		userType.setContainerDataSource(userTypeContainer);
		userType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		userType.setItemCaptionPropertyId("value");
		
		if (this.bean.getType() != null) {
			this.userType.setValue(this.bean.getType());
		}
		
		autoAllocationClaim =  (ComboBox) binder.buildAndBind("Document Type",
				"autoAlloccationType", ComboBox.class);

		autoAllocationClaim.setContainerDataSource(documentTypeContainer);
		autoAllocationClaim.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		autoAllocationClaim.setItemCaptionPropertyId("value");
		
		if (this.bean.getAutoAlloccationType() != null) {
			this.autoAllocationClaim.setValue(this.bean.getAutoAlloccationType());
		}
		
		ClaimFlagInUserMaster =  (ComboBox) binder.buildAndBind("Claim Flag In User Master","claimFlagInUserMaster", ComboBox.class);

		ClaimFlagInUserMaster.setContainerDataSource(claimFlagTypeContainer);
		ClaimFlagInUserMaster.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		ClaimFlagInUserMaster.setItemCaptionPropertyId("value");
		
		if (this.bean.getClaimFlagInUserMaster() != null) {
			this.ClaimFlagInUserMaster.setValue(this.bean.getClaimFlagInUserMaster());
		}
		
		HorizontalLayout restrictBand = new HorizontalLayout();
		restrictBand.setCaption("Restrict to Band");
		restrictToBand =  (CheckBox) binder.buildAndBind("","restrictToBand", CheckBox.class);

		if(bean.getRestrictToBand()!= null && bean.getRestrictToBand().equals(true)){
			restrictToBand.setValue(true);
		}
		
		//added for combined process CPU
		HorizontalLayout manualPickCombinedProcessedLayout = new HorizontalLayout();
		manualPickCombinedProcessedLayout.setCaption("Mixed Processing");
		manualPickCombinedProcessFlag = (CheckBox) binder.buildAndBind("", "manualPickCombinedProcessFlag",CheckBox.class);
		manualPickCombinedProcessFlag.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(manualPickCombinedProcessFlag.getValue()) {
					bean.setManualPickCombinedProcessFlag(true);
				}else if(manualPickCombinedProcessFlag.getValue().equals(false)) {
					bean.setManualPickCombinedProcessFlag(false);
				}
			}
		});
		
		manualPickCombinedProcessedLayout.addComponent(manualPickCombinedProcessFlag);
		TextField dummyTextField = new TextField();
		dummyTextField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		userCpuTable.init();
		dagnosisTable.init();
		labelTable.init();
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		
		Panel mainPanel = new Panel();
		//Vaadin8-setImmediate() mainPanel.setImmediate(true);
		mainPanel.setHeight("100%");
		mainPanel.setWidth("100%");
		
		HorizontalLayout highValue = new HorizontalLayout();
		highValue.setCaption("Deactivate User");
		HorizontalLayout deactiveUser = new HorizontalLayout();
		deactiveUser.setCaption("High Claim Value");
		
		
		highValueClaim = (CheckBox) binder.buildAndBind("", "highValueClaim",CheckBox.class);
		if(bean.getHighValueClaim()!= null && bean.getHighValueClaim().equals(true)){
			highValueClaim.setValue(true);
		}
		deactivateUser = (CheckBox) binder.buildAndBind("", "deActivateUser",CheckBox.class);
		if(bean.getDeActivateUser()!= null && bean.getDeActivateUser().equals(true)){
			deactivateUser.setEnabled(true);
			productDocumentTabs();
		}else if(bean.getDeActivateUser()!= null && bean.getDeActivateUser().equals(false)) {
			//deactivateUser.setEnabled(false);
			productDocumentTabs();
		}
		
		
		highValue.addComponent(deactivateUser);
		deactivateUser.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if(deactivateUser.getValue()) {
					bean.setDeActivateUser(true);
				}else if(deactivateUser.getValue().equals(false)) {
					bean.setDeActivateUser(false);
				}
				if(!deactivateUser.isEmpty()) {
					productDocumentTab.setEnabled(false);
				}else{
					productDocumentTab.setEnabled(true);
				}
			}
		});
		highValueClaim.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					 if(highValueClaim.isEmpty()){
						 shiftStartTime.setValue("");
						 shiftEndTime.setValue("");
							shiftStartTime.setEnabled(false);
							shiftEndTime.setEnabled(false);
						}
					 if(!highValueClaim.isEmpty()){
						 shiftStartTime.setEnabled(true);
						 shiftEndTime.setEnabled(true);
					 }
					 
					binder.commit();
				} catch (CommitException e) {
					e.printStackTrace();
				}
				
			}
		});
		deactiveUser.addComponent(highValueClaim);
		FormLayout firstFormLayout = new FormLayout(userName,deactiveUser,highValue,ClaimFlagInUserMaster);
		firstFormLayout.setSpacing(true);
		
		shiftStartTime = (TextField) binder.buildAndBind("Shift Start Time","shiftStartTime",TextField.class);
		if(highValueClaim.getValue() != null && highValueClaim.getValue().equals(true) && highValueClaim.isEnabled() && bean.getShiftStartTime()!=null) {
			shiftStartTime.setEnabled(true);
		}else{
			shiftStartTime.setEnabled(false);
		}
		shiftStartTime.setInputPrompt("HH:MM");
		shiftStartTime.setNullSettingAllowed(false);
		CSValidator shiftStartTimeValidator = new CSValidator();
		shiftStartTimeValidator.extend(shiftStartTime);
		shiftStartTimeValidator.setRegExp("^(|[0-9:]+)$");
		shiftStartTimeValidator.setPreventInvalidTyping(true);
		shiftStartTime.setNullRepresentation("");
		
		shiftEndTime = (TextField) binder.buildAndBind("Shift End Time","shiftEndTime",TextField.class);
		if(highValueClaim.getValue() != null && highValueClaim.getValue().equals(true) && highValueClaim.isEnabled() && bean.getShiftEndTime()!=null) {
			shiftEndTime.setEnabled(true);
		}else{
			shiftEndTime.setEnabled(false);
		}
		shiftEndTime.setInputPrompt("HH:MM");
		shiftEndTime.setNullSettingAllowed(false);
		CSValidator shiftEndTimeValidator = new CSValidator();
		shiftEndTimeValidator.extend(shiftEndTime);
		shiftEndTimeValidator.setRegExp("^(|[0-9:]+)$");
		shiftEndTimeValidator.setPreventInvalidTyping(true);
		shiftEndTime.setNullRepresentation("");
		

		// CR2019213
		HorizontalLayout manualFlagLayout = new HorizontalLayout();
		manualFlagLayout.setCaption("Allow Manual Picking of Claim");
		manualFlag = (CheckBox) binder.buildAndBind("", "manaulFlag",CheckBox.class);
		manualFlag.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(manualFlag.getValue()) {
					bean.setManaulFlag(true);
				}else if(manualFlag.getValue().equals(false)) {
					bean.setManaulFlag(false);
				}
			}
		});
		
		manualFlagLayout.addComponent(manualFlag);
		
		/**
		 * added Allow Manual coding field in User Management screen 
		 */
		HorizontalLayout manualCodingLayout = new HorizontalLayout();
		manualCodingLayout.setCaption("Allow Manual Picking of Code");
		manualCodingFlag = (CheckBox) binder.buildAndBind("", "manualCodingFlag",CheckBox.class);
		manualCodingFlag.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(manualCodingFlag.getValue()) {
					bean.setManualCodingFlag(true);
				}else if(manualCodingFlag.getValue().equals(false)) {
					bean.setManualCodingFlag(false);
				}
			}
		});
		
		manualCodingLayout.addComponent(manualCodingFlag);
		
		
		// GLX2021004
				HorizontalLayout manualPickMALayout = new HorizontalLayout();
				manualPickMALayout.setCaption("Allow Manual Picking of Claim(MA)");
				manualPickMAFlag = (CheckBox) binder.buildAndBind("", "manualPickMAFlag",CheckBox.class);
				manualPickMAFlag.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(manualPickMAFlag.getValue()) {
							bean.setManualPickMAFlag(true);
						}else if(manualPickMAFlag.getValue().equals(false)) {
							bean.setManualPickMAFlag(false);
						}
					}
				});
				
				manualPickMALayout.addComponent(manualPickMAFlag);
		
		FormLayout secondFormLayout = new FormLayout(userId,shiftStartTime,manualFlagLayout,manualCodingLayout,manualPickMALayout);
		secondFormLayout.setSpacing(true);
		restrictBand.addComponent(restrictToBand);
		FormLayout thirdFormLayout = new FormLayout(userType,shiftEndTime,autoAllocationClaim,restrictBand,manualPickCombinedProcessedLayout);
		thirdFormLayout.setSpacing(true);
		
		HorizontalLayout horzLayout = new HorizontalLayout(firstFormLayout,secondFormLayout,thirdFormLayout);
		horzLayout.setSpacing(true);
		productDocumentTabs().setSizeUndefined();
		HorizontalLayout dummyLayout = new HorizontalLayout();
		/*Panel tabPanel = new Panel();
		tabPanel.setContent(productDocumentTabs());*/
		VerticalLayout verticalMain = new VerticalLayout(horzLayout,dummyLayout,productDocumentTabs(),dummyLayout,buttonHor);
		//mainPanel.setContent(verticalMain);
		
		verticalMain.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		setCompositionRoot(verticalMain);
		
		addListener();
		
		/*mandatoryFields.add(queueCount);
		mandatoryFields.add(status);
		mandatoryFields.add(startTime);
		mandatoryFields.add(endTime);*/
		mandatoryFields.add(userType);
		
		showOrHideValidation(false);
	
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	private TabSheet productDocumentTabs(){
		
		productDocumentTab = new TabSheet();
		
		//Vaadin8-setImmediate() productDocumentTab.setImmediate(true);		
		
		productDocumentTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		if(bean.getDeActivateUser()!=null && bean.getDeActivateUser().equals(true)) {
			productDocumentTab.setEnabled(false);
		}else{
			productDocumentTab.setEnabled(true);
		}
		
		TabSheet applicableCpuTab =  getApplicableCpuTab();
		TabSheet limit = getLimitTab();
		//AutoAllocationCategory
		TabSheet diagnosis = getDiagnosisTab();
		
		TabSheet label = getLabelTab();
		//ProductMapping
		TabSheet productMapping = getProductMappingTab();
	//	productDocumentTab.setHeight("100.0%");
		productDocumentTab.addTab(applicableCpuTab, "Applicable CPU", null);
		productDocumentTab.addTab(limit, "Limit", null);
		productDocumentTab.addTab(diagnosis,"Diagnosis",null);
		productDocumentTab.addTab(label,"Label",null);
		productDocumentTab.addTab(productMapping,"Product Mapping",null);
		//applicableCpuTab.setSizeFull();
		//productDocumentTab.setSizeFull();
		productDocumentTab.setSizeFull();
		return productDocumentTab;
	}
	
	private TabSheet getApplicableCpuTab() {
		
		TabSheet applCpuTab = new TabSheet();
		if(bean.getDeActivateUser()!= null && bean.getDeActivateUser().equals(true)) {
			applCpuTab.setEnabled(false);
		}else{
			applCpuTab.setEnabled(true);
		}
		applCpuTab.hideTabs(true);
		//Vaadin8-setImmediate() applCpuTab.setImmediate(true);
		applCpuTab.setWidth("100%");
		applCpuTab.setHeight("100%");
		applCpuTab.setSizeFull();
		//Vaadin8-setImmediate() applCpuTab.setImmediate(true);
		if(!deactivateUser.isEmpty()) {
			applCpuTab.setEnabled(false);
		}
		applicableCpuTableObj = applicableCpuTableInstance.get();
		applicableCpuTableObj.init();
		applicableCpuTableObj.setReferenceData(this.referenceData);
		//applicableCpuTableObj.setEnabled(false);
		
		
		setApplicableCpuTableValue(cpuKey); // hard code cpu key for testing remove after db dev
		
		applCpuTab.addComponent(applicableCpuTableObj);
		
		return applCpuTab;
	}
	
	private void setApplicableCpuTableValue(Long cpuKey) {
		List<TmpCPUCode> tmpCpucode = cpuCodeService.getCpuCode();
		int sno=1;
		TmpEmployee doctorDetails  = userMgmtService.getEmployessValues(bean.getUserId());
		List<MasOrgCpuMapping> cpuList = new ArrayList<MasOrgCpuMapping>();
	if(null != doctorDetails){
		List<UserToOrgMapping> userMap = cpuCodeService.getOrgMappingByUser(doctorDetails.getEmpId());
		List<String> orgList = new ArrayList<String>();
		if(null != userMap){
			for (UserToOrgMapping userToOrgMapping : userMap) {
				orgList.add(userToOrgMapping.getOrgId());
			}
		}
	
		List<Long> cpuListByOrg = new ArrayList<Long>();
		
		if(orgList!=null && !orgList.isEmpty()) {
		List<TmpCPUCode> listOfCPU = cpuCodeService.getCPUByOrgList(orgList);
		
		if(null != listOfCPU){
			for (TmpCPUCode tmpCPUCode2 : listOfCPU) {
				cpuListByOrg.add(tmpCPUCode2.getCpuCode());
			}
			}
		}
		if(tmpCpucode != null) {
			for (TmpCPUCode applicableCpuDTO : tmpCpucode) {
			UserMgmtApplicableCpuDTO cpuDto = new UserMgmtApplicableCpuDTO();
			cpuDto.setSno(sno);
			cpuDto.setCpuCodewithName(applicableCpuDTO.getCpuCode()+ "-" +applicableCpuDTO.getDescription());
			cpuDto.setCpuCode(Long.toString(applicableCpuDTO.getCpuCode()));
			
			BeanItemContainer<SelectValue> listOfAreaCode= cpuCodeService.getAreaCodeByCPUList(Long.toString(applicableCpuDTO.getCpuCode()));
			if(listOfAreaCode != null){
			cpuDto.setAreaCodeList(listOfAreaCode);
			}
			List<MasAreaOrgUserCpuMapping> listOfSelectedAreaCode = cpuCodeService.getUserAreaOrgMappingDetails(Long.toString(applicableCpuDTO.getCpuCode()),doctorDetails.getEmpId());
			if(listOfSelectedAreaCode != null && !listOfSelectedAreaCode.isEmpty()){
				List<String> areaCodeList = new ArrayList<String>();
				for (MasAreaOrgUserCpuMapping userToOrgMapping : listOfSelectedAreaCode) {
					areaCodeList.add(userToOrgMapping.getAreaCode() + "-" +userToOrgMapping.getMasAreaMapping().getAreaName());
				}
				cpuDto.setSelectedAreCodList(areaCodeList);
			}
			if(cpuListByOrg!=null && !cpuListByOrg.isEmpty()) {
			if(cpuListByOrg.contains(applicableCpuDTO.getCpuCode())){
				cpuDto.setCheckBoxValueAccesibility(true);
				cpuDto.setAccessability(Boolean.TRUE);
				
				List<UserToOrgMapping> userMap1 = userMgmtService.getUserOrgMappingDetails(applicableCpuDTO.getCpuCode().toString(),doctorDetails.getEmpId());
							if (userMap1 != null && !userMap1.isEmpty() && userMap1.size() == 2) {
								int count = 0;
								for (UserToOrgMapping userToOrgMapping : userMap1) {
									if (userToOrgMapping.getActiveStatus()
											.equals(SHAConstants.N_FLAG)) {
										count++;
									}
								}
								if (count == 2) {
									cpuDto.setAccessability(false);
								}
							}
				
				for (UserToOrgMapping userToOrgMapping : userMap1) {
				if(userToOrgMapping != null) {
					if(userToOrgMapping.getActiveStatus().equals(SHAConstants.N_FLAG) && userToOrgMapping.getLobFlag() == null) {
						cpuDto.setGmcCheckBox(false);
						cpuDto.setRetailCheckBox(false);
					}
					if(userToOrgMapping.getActiveStatus().equals(SHAConstants.YES_FLAG) && userToOrgMapping.getLobFlag()==null) {
						cpuDto.setAccessability(true);
						cpuDto.setGmcCheckBox(false);
						cpuDto.setRetailCheckBox(false);
					}else if(userToOrgMapping.getLobFlag()!=null) {
						if(userToOrgMapping.getActiveStatus().equals(SHAConstants.YES_FLAG) && userToOrgMapping.getLobFlag().equals(SHAConstants.GMC_POL_SERIVICE)) {
					
						cpuDto.setGmcCheckBox(true);
					}else if(userToOrgMapping.getActiveStatus().equals(SHAConstants.YES_FLAG) && userToOrgMapping.getLobFlag().equals(SHAConstants.RETAIL_USER_MANAGEMENT)) {
						cpuDto.setRetailCheckBox(true);
					}else if(userToOrgMapping.getActiveStatus().equals(SHAConstants.N_FLAG) && userToOrgMapping.getLobFlag().equals(SHAConstants.GMC_POL_SERIVICE)) {
						cpuDto.setGmcCheckBox(false);
					}else if(userToOrgMapping.getActiveStatus().equals(SHAConstants.N_FLAG) && userToOrgMapping.getLobFlag().equals(SHAConstants.RETAIL_USER_MANAGEMENT)) {
						cpuDto.setRetailCheckBox(false);
					}
					}
				  }
				}
			  }
			}
			//cpuDetails.add(cpuList);
			this.applicableCpuTableObj.addBeanToListUserMgmt(cpuDto);
			sno++;
			
		}
		}
	 }
		
	}
	
	public void addListener() {

		
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.USER_MANAGEMENT, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage()) {
			    String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			    setTableValuestoDto();			   
				fireViewEvent(UserActivationPresenter.USER_MANAGEMENT_CPU_LIMIT,bean,userName);	
				}
				
			}
		});
		
	}
	public void setTableValuestoDto() {
		
		List<UserMgmtApplicableCpuDTO> applicableList = this.applicableCpuTableObj.getValues();
		List<UserMgmtDaignosisDTO> diagnosisList = this.dagnosisTableObj.getValues();
		
		List<UserManagementLabelDTO> labelDTOList = this.labelTableObj.getValues();
		List<UserMgmtProductMappingDTO> productMappingList = this.productMappingTableObj.getValues();
		this.bean.setApplicableCpuList(applicableList);
		this.bean.setLimitList(limitTable.getTableList());
		this.bean.setDeletedList(limitTable.getDeletedList());
		this.bean.setDiagnosisList(diagnosisList);
		this.bean.setLabelDTOList(labelDTOList);
		this.bean.setProductMappingList(productMappingList);
	}
	
	public Boolean validatePage() {
		Boolean hasSelected = Boolean.FALSE;
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		
		if(this.labelTableObj.getValues() !=null && !this.labelTableObj.getValues().isEmpty()){
				List<UserManagementLabelDTO> labelManagementTableValues = this.labelTableObj.getValues();
				if(null != labelManagementTableValues && !labelManagementTableValues.isEmpty()){
					for (UserManagementLabelDTO userLabelTableDTO : labelManagementTableValues) {
						if((userLabelTableDTO.getLabelEnable()!= null && userLabelTableDTO.getLabelEnable()) &&(userLabelTableDTO.getIncludeEnable()!=null && userLabelTableDTO.getIncludeEnable().equals(Boolean.FALSE))&& (userLabelTableDTO.getExcludeEnable() != null && userLabelTableDTO.getExcludeEnable().equals(Boolean.FALSE) ) ){
								
							hasError = true;
							eMsg.append("Please Select any one of Radio button to proceed further </br>");
							break;
						}
					}
				}
			
			
		}
		
		/*try {
			if(! this.labelTableObj.isValid()){
				hasError = true;
			List<String> errors = this.labelTableObj.getErrors();
			for (String error : errors) {
				eMsg.append(error).append("</br>");
			   }
			}
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		if ((this.shiftStartTime != null && this.shiftStartTime.getValue() != null && !this.shiftStartTime
				.getValue().isEmpty())) {

			String regExp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

			if (!(this.shiftStartTime.getValue().matches(regExp))) {
				hasError = true;
				eMsg.append("Please enter valid 24-hour time format for Shift Start Time.</br>");
			}
		}
			if ((this.shiftEndTime != null && this.shiftEndTime.getValue() != null && !this.shiftEndTime
					.getValue().isEmpty())) {

				String regExp = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

				if (!(this.shiftEndTime.getValue().matches(regExp))) {
					hasError = true;
					eMsg.append("Please enter valid 24-hour time format for Shift End Time.</br>");
				}
			}
			
			if(this.highValueClaim.getValue()!=null && this.highValueClaim.getValue().equals(false)) {
				shiftStartTime.setEnabled(false);
				shiftEndTime.setEnabled(false);
			}
			if(this.highValueClaim.getValue()!=null && this.highValueClaim.getValue().equals(true)) {
				if(shiftStartTime.isEmpty() || shiftEndTime.isEmpty()) {
					hasError = true;
					eMsg.append("Please Enter the Shift Timings");
				}
			}
			
			if(this.shiftStartTime.getValue() != null && this.shiftEndTime.getValue() != null && this.highValueClaim.getValue().equals(true) && 
					!this.shiftStartTime.isEmpty() && !this.shiftEndTime.isEmpty()) {
		String startTim = this.shiftStartTime.getValue();
		String endTim = this.shiftEndTime.getValue();
		if(!startTim.contains(":") && !endTim.contains(":")){
			hasError = true;
			eMsg.append("Please Enter the Time in HH:MM Format");
		}else {
		String diffTin[] = startTim.split(":");
		String diffEndTim[] = endTim.split(":");
		int std = 0,std1 = 0,etd = 0,etd1 = 0;
		for(int i=0;i<diffTin.length;i++) {
			std = Integer.parseInt(diffTin[i]);
			std1 = Integer.parseInt(diffTin[1]);
			break;
		}
		for(int i=0;i<diffEndTim.length;i++) {
			etd = Integer.parseInt(diffEndTim[i]);
			etd1 = Integer.parseInt(diffEndTim[1]);
			break;
		}
		
		if(std>etd) {
			hasError=true;
			eMsg.append("Shift End Time cannot be Lesser than Shift Start Time");
		}
		if(etd==std && std1>etd1){
			hasError=true;
			eMsg.append("Shift End Time cannot be Lesser than Shift Start Time");
		}
		if(std==etd && std1==etd1){
			hasError=true;
			eMsg.append("Shift Start Time and Shift End Time Cannot Be Same");
		}
			}
			}
			
		/*if(!hasSelected){
			hasError = true;
			eMsg.append("Please select atleast one accessability");
		}*/
	
		 
		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
	
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
	
			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
private TabSheet getLimitTab() {
		
		TabSheet limitTab = new TabSheet();
		
		limitTab.hideTabs(true);
		//Vaadin8-setImmediate() limitTab.setImmediate(true);
		limitTab.setWidth("100%");
		limitTab.setHeight("100%");
		limitTab.setSizeFull();
		//Vaadin8-setImmediate() limitTab.setImmediate(true);
		userMgmtLimit.init();
		limitTable.init("", false, false);
		if(null != bean.getAvailableRoleLimitList()){
			limitTable.setTableList(bean.getAvailableRoleLimitList());
		}
		VerticalLayout limitlayout = new VerticalLayout(userMgmtLimit,limitTable);
		
		limitTab.addComponent(limitlayout);
		return limitTab;
	}

public void showInformation(String eMsg) {
	MessageBox.create()
	.withCaptionCust("Information").withHtmlMessage(eMsg.toString())
    .withOkButton(ButtonOption.caption("OK")).open();
}

//CR autoAllocation
private TabSheet getDiagnosisTab(){
	TabSheet diagnosisTab = new TabSheet();
	if(bean.getDeActivateUser()!= null && bean.getDeActivateUser().equals(true)) {
		diagnosisTab.setEnabled(false);
	}else{
		diagnosisTab.setEnabled(true);
	}
	diagnosisTab.hideTabs(true);
	diagnosisTab.setWidth("100%");
	diagnosisTab.setHeight("100%");
	diagnosisTab.setSizeFull();
	if(!deactivateUser.isEmpty()) {
		diagnosisTab.setEnabled(false);
	}
	dagnosisTableObj = dagnosisTableInstance.get();
	dagnosisTableObj.init();
	setDagnosisTableValue();
	diagnosisTab.addComponent(dagnosisTableObj);
	return diagnosisTab;
}

	private void setDagnosisTableValue(){
		int sno=1;
		List<MasUserDiagnosis> dagonisisList = userMgmtService.getDiagnosisToMappingValues();
		List<MasSecUserDiagnosisMapping>  updateList = userMgmtService.getUpdateDiagnosisList(bean.getUserId());
		
		if(dagonisisList != null){
			for(MasUserDiagnosis list : dagonisisList ){
				UserMgmtDaignosisDTO dagnosisDto = new UserMgmtDaignosisDTO();
				System.out.println(list.getValue());
				dagnosisDto.setKey(list.getKey());
				dagnosisDto.setSno(sno);
				dagnosisDto.setValue(list.getValue());
				dagnosisDto.setDiagnosisEnable(false);
				if(updateList != null){
					for(MasSecUserDiagnosisMapping subList : updateList){
						if(subList.getDiagnosisKey() != null && subList.getDiagnosisKey().equals(list.getKey())){
							if(subList.getActiveStatus() == 1){

								dagnosisDto.setDiagnosisEnable(true);
							}else{
								dagnosisDto.setDiagnosisEnable(false);
							}

						}
					}
				}
				
				this.dagnosisTableObj.addBeanToListUserMgmt(dagnosisDto);
				sno++;
			}
		}
		
	}
	
	private TabSheet getProductMappingTab(){
		TabSheet productMappingTab = new TabSheet();
		if(bean.getDeActivateUser()!= null && bean.getDeActivateUser().equals(true)) {
			productMappingTab.setEnabled(false);
		}else{
			productMappingTab.setEnabled(true);
		}
		productMappingTab.hideTabs(true);
		productMappingTab.setWidth("100%");
		productMappingTab.setHeight("100%");
		productMappingTab.setSizeFull();
		if(!deactivateUser.isEmpty()) {
			productMappingTab.setEnabled(false);
		}
		productMappingTableObj = productMappingTableInstance.get();
		productMappingTableObj.init();
		setProductMappingTableValue();
		productMappingTab.addComponent(productMappingTableObj);
		return productMappingTab;
	}

		private void setProductMappingTableValue(){
			int sno=1;
			List<Product> productList = userMgmtService.getProductMappingList();
			
			
			if(productList != null){
				for(Product list : productList ){
					List<UserProductMapping>  updateList = userMgmtService.getUserProductMappingDetails(list.getKey(),bean.getUserId());
					UserMgmtProductMappingDTO productMappingDto = new UserMgmtProductMappingDTO();
					//System.out.println(list.getValue());
					productMappingDto.setKey(list.getKey());
					productMappingDto.setSno(sno);
					productMappingDto.setProductCodeWithName(list.getCode()+" - "+list.getValue());
					productMappingDto.setProductType(list.getProductType());
					productMappingDto.setProductMappingEnable(false);
					if(updateList != null){
						for(UserProductMapping subList : updateList){
							if(subList.getProductKey() != null && subList.getProductKey().equals(list.getKey())){
								if(subList.getActiveStatus() == 1){

									productMappingDto.setProductMappingEnable(true);
								}else{
									productMappingDto.setProductMappingEnable(false);
								}

							}
						}
					}
					
					this.productMappingTableObj.addBeanToListUserMgmt(productMappingDto);
					sno++;
				}
			}
			
		}
		
		
		//CR Label Tab
		private TabSheet getLabelTab(){
			TabSheet labelTab = new TabSheet();
			if(bean.getDeActivateUser()!= null && bean.getDeActivateUser().equals(true)) {
				labelTab.setEnabled(false);
			}else{
				labelTab.setEnabled(true);
			}
			labelTab.hideTabs(true);
			labelTab.setWidth("100%");
			labelTab.setHeight("100%");
			labelTab.setSizeFull();
			if(!deactivateUser.isEmpty()) {
				labelTab.setEnabled(false);
			}
			labelTableObj = labelTableInstance.get();
			labelTableObj.init();
			setLabelTableValue();
			labelTab.addComponent(labelTableObj);
			return labelTab;
		}
		
		
		private void setLabelTableValue(){
			int sno=1;
			List<MastersValue> vspList = userMgmtService.getVSPMasterByMasterTypeCode();
			List<MasSecVSPUserLabelMapping>  updateList = userMgmtService.getUpdateLabelList(bean.getUserId());
			
			if(vspList != null){
				for(MastersValue list : vspList ){
					UserManagementLabelDTO labelDto = new UserManagementLabelDTO();
					System.out.println(list.getValue());
					labelDto.setKey(list.getKey());
					labelDto.setSno(sno);
					labelDto.setValue(list.getValue());
					labelDto.setLabelEnable(false);
					if(updateList != null){
						for(MasSecVSPUserLabelMapping subList : updateList){
							if(subList.getKey() != null){
								if(subList.getActiveStatus().equalsIgnoreCase(SHAConstants.YES_FLAG)){

									labelDto.setLabelEnable(true);
									labelDto.setIncludeEnable(true);
									labelDto.setExcludeEnable(true);
									
									if(subList.getVspInclude() != null && subList.getVspInclude().equalsIgnoreCase(SHAConstants.YES_FLAG)){
										labelDto.setIncludeValue(true);

									}
									else {
										labelDto.setIncludeValue(false);
									}
									if(subList.getVspExclude() != null && subList.getVspExclude().equalsIgnoreCase(SHAConstants.YES_FLAG)){
										labelDto.setExcludeValue(true);
									}else {
										labelDto.setExcludeValue(false);
									}
								}else{
									labelDto.setLabelEnable(false);
								}

							}
						}
					}
					
					this.labelTableObj.addBeanToListUserMgmt(labelDto);
					sno++;
				}
			}
			
		}
		

}
