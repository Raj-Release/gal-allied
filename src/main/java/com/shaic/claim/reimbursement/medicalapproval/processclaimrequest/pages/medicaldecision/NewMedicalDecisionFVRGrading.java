package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.fvrgrading.page.FvrReportGradingPageDto;
import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class NewMedicalDecisionFVRGrading extends ViewComponent {
	private static final long serialVersionUID = -4135096211808010097L;

	private ComboBox fvrSequence;
	
	private Label countOfFVR;
	
	private TextField reprentativeCode;
	
	private TextField representativeName;
	
	private PreauthDTO bean;
	
	/*@Inject
	private Instance<MedicalDecisionFVRGradingTable> fvrTableInstance;
	
	private MedicalDecisionFVRGradingTable fvrTableObj;*/
	
	/*@Inject
	private Instance<MedicalDecisionFVRGradingListenerTable> fvrListenerTableInstance;
	
	private MedicalDecisionFVRGradingListenerTable fvrListenerTableObj;*/
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private FvrGradingDetailsDTO previousSelected = new FvrGradingDetailsDTO();
	
	public Map<Integer, FvrGradingDetailsDTO> dtoMapping = new HashMap<Integer, FvrGradingDetailsDTO>();

	private VerticalLayout tableLayout;
	
	private Boolean isCashless = false;
	
	@Inject
	private Instance<NewMedicalDecisionFVRGradingBListenerTable> fvrListenerTableInstance;
	
	private NewMedicalDecisionFVRGradingBListenerTable fvrListenerTableObj;
	
	@Inject
	private Instance<NewMedicalDecisionFVRGradingCListenerTable> fvrCListenerTableInstance;
	
	private NewMedicalDecisionFVRGradingCListenerTable fvrCListenerTableObj;
	
	@Inject
	private Instance<NewMedicalDecisionFVRGradingAListenerTable> fvrTableInstance;
	
	private NewMedicalDecisionFVRGradingAListenerTable fvrTableObj;
	
	private Button clearAll;
	
	private FvrGradingDetailsDTO currentValue = new FvrGradingDetailsDTO();
	
	private FvrReportGradingPageDto fvrBean;
	
	private TextArea remarks;
	
	private Map<Long, AbstractField<?>> tableItem = new HashMap<Long, AbstractField<?>>();
	
	@PostConstruct
	public void init() {
		
	}

	public void initView(PreauthDTO bean, Boolean isCashless) {
		this.bean = bean;
		this.isCashless = isCashless;
		
		remarks = null;
		tableItem.clear();
		List<FvrGradingDetailsDTO> fvrGradingDTO = this.bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();
		//List<FvrGradingDetailsDTO> values = new ArrayList<FvrGradingDetailsDTO>();
		int i = 1;
		for (FvrGradingDetailsDTO value : fvrGradingDTO) {
			value.setNumber(i++);
			
		}
		BeanItemContainer<FvrGradingDetailsDTO> dtoList = new BeanItemContainer<FvrGradingDetailsDTO>(FvrGradingDetailsDTO.class);
		dtoList.addAll(fvrGradingDTO);
		
		fvrSequence = new ComboBox("FVR Sequence");
		fvrSequence.setWidth("100px");
		fvrSequence.setContainerDataSource(dtoList);
		fvrSequence.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrSequence.setItemCaptionPropertyId("number");
		Collection<?> itemIds = fvrSequence.getContainerDataSource().getItemIds();
		fvrSequence.setValue(itemIds.toArray()[0]);
		fvrSequence.setNullSelectionAllowed(false);
		
		
		FvrGradingDetailsDTO value = (FvrGradingDetailsDTO) fvrSequence.getValue();
		
		countOfFVR = new Label(value != null ? String.valueOf(value.getNumber()) + "st FVR" : "" );
		countOfFVR.setWidth("100px");
		
		reprentativeCode = new TextField("Representative Code");
		reprentativeCode.setValue(value.getRepresentiveCode());
		reprentativeCode.setNullRepresentation("-");
		reprentativeCode.setEnabled(false);
		reprentativeCode.setWidth("100px");
		
		representativeName = new TextField("Representative Name");
		representativeName.setValue(value.getRepresentativeName());
		representativeName.setNullRepresentation("-");
		representativeName.setEnabled(false);
		representativeName.setWidth("100px");
		
		clearAll = new Button("Clear All");
		clearAll.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		clearAll.setWidth("150px");
		
		currentValue = value;
		
		if(currentValue != null){
			/*if((currentValue.getIsSegmentANotEdit() != null && currentValue.getIsSegmentANotEdit()) && (currentValue.getIsSegmentBNotEdit() != null && currentValue.getIsSegmentBNotEdit()) && (currentValue.getIsSegmentCNotEdit() != null && currentValue.getIsSegmentCNotEdit())){
				clearAll.setEnabled(false);
			}*/
			if(currentValue.getIsClearAllEnabled() != null && !currentValue.getIsClearAllEnabled()){
				clearAll.setEnabled(false);
			}
		}
		
		addClearAllListener();
		
		
		HorizontalLayout fvrSeqLayout = new HorizontalLayout(new FormLayout(fvrSequence), countOfFVR);
		fvrSeqLayout.setSpacing(true);
		
		HorizontalLayout firstHLayout = new HorizontalLayout(fvrSeqLayout, new FormLayout(reprentativeCode) );
		firstHLayout.setSpacing(true);
		firstHLayout.setWidth("100%");
		
		if(!this.isCashless) {
			/*fvrTableObj = fvrTableInstance.get();
			fvrTableObj.init("FVR Grading", false);
			fvrTableObj.initTableDTO(value.getFvrGradingDTO());
			fvrTableObj.initTable();
			
			tableLayout = new VerticalLayout(fvrTableObj);*/
			
			fvrTableObj = fvrTableInstance.get();
			fvrTableObj.init(this.bean);
			fvrTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout = new VerticalLayout(fvrTableObj);
			
			
			fvrListenerTableObj = fvrListenerTableInstance.get();
			fvrListenerTableObj.init(this.bean);
			fvrListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrListenerTableObj);
			
			fvrCListenerTableObj = fvrCListenerTableInstance.get();
			fvrCListenerTableObj.init(this.bean);
			fvrCListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrCListenerTableObj);
			
			addSegmentAListener(fvrTableObj, fvrCListenerTableObj);
			addSegmentBListener(fvrListenerTableObj, fvrCListenerTableObj);
			addSegmentCListener(fvrCListenerTableObj, fvrListenerTableObj, fvrTableObj);
			
			/*if(value.getIsSegmentANotEdit() != null && value.getIsSegmentBNotEdit() != null && !value.getIsSegmentANotEdit() && !value.getIsSegmentBNotEdit()){
				fvrTableObj.dummyField.setValue(SHAConstants.YES);
				fvrListenerTableObj.dummyField.setValue(SHAConstants.YES);
				fvrCListenerTableObj.dummyField.setValue(SHAConstants.No);
			}else if(value.getIsSegmentCNotEdit() != null && !value.getIsSegmentCNotEdit()){
				fvrTableObj.dummyField.setValue(SHAConstants.No);
				fvrListenerTableObj.dummyField.setValue(SHAConstants.No);
				fvrCListenerTableObj.dummyField.setValue(SHAConstants.YES);
			}else{
				fvrTableObj.dummyField.setValue(null);
				fvrListenerTableObj.dummyField.setValue(null);
				fvrCListenerTableObj.dummyField.setValue(null);
			}*/
			
			
		} else {
			/*fvrListenerTableObj = fvrListenerTableInstance.get();
			fvrListenerTableObj.init(this.bean);
			fvrListenerTableObj.setTableList(value.getFvrGradingDTO());
			tableLayout = new VerticalLayout(fvrListenerTableObj);*/
			
			fvrTableObj = fvrTableInstance.get();
			fvrTableObj.init(this.bean);
			fvrTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout = new VerticalLayout(fvrTableObj);
			
			fvrListenerTableObj = fvrListenerTableInstance.get();
			fvrListenerTableObj.init(this.bean);
			fvrListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrListenerTableObj);
			
			fvrCListenerTableObj = fvrCListenerTableInstance.get();
			fvrCListenerTableObj.init(this.bean);
			fvrCListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrCListenerTableObj);
			
			addSegmentAListener(fvrTableObj, fvrCListenerTableObj);
			addSegmentBListener(fvrListenerTableObj, fvrCListenerTableObj);
			addSegmentCListener(fvrCListenerTableObj, fvrListenerTableObj, fvrTableObj);
			
			
		}
		
		VerticalLayout mainPanel = new VerticalLayout(firstHLayout, new FormLayout(representativeName),clearAll, tableLayout);
		/*if(value.getIsSegmentANotEdit() != null && value.getIsSegmentBNotEdit() != null && !value.getIsSegmentANotEdit() && !value.getIsSegmentBNotEdit()){
			fvrTableObj.dummyField.setValue(SHAConstants.YES);
			fvrListenerTableObj.dummyField.setValue(SHAConstants.YES);
			fvrCListenerTableObj.dummyField.setValue(SHAConstants.No);
		}else if(value.getIsSegmentCNotEdit() != null && !value.getIsSegmentCNotEdit()){
			fvrTableObj.dummyField.setValue(SHAConstants.No);
			fvrListenerTableObj.dummyField.setValue(SHAConstants.No);
			fvrCListenerTableObj.dummyField.setValue(SHAConstants.YES);
		}else{
			fvrTableObj.dummyField.setValue(null);
			fvrListenerTableObj.dummyField.setValue(null);
			fvrCListenerTableObj.dummyField.setValue(null);
		}*/
		
		if(value.getIsFVRReceived() != null && value.getIsFVRReceived()){
			remarks = new TextArea("Doctor's Grading suggestions (FVR)");
			if(value.getGradingRemarks() != null){
				remarks.setValue(value.getGradingRemarks());
			}
			remarks.setNullRepresentation("");
			remarks.setWidth("400px");
			remarks.setMaxLength(500);
			
			remarks.setId("gradingRmrks");
			gradingRemarksListener(remarks,null);
			remarks.setData(value);
			remarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
			
			tableItem.put(value.getKey(), remarks);
			
			tableLayout.addComponent(remarks);
			tableLayout.setComponentAlignment(remarks, Alignment.MIDDLE_CENTER);
		}
		
		dtoMapping.put(value.getNumber(), value);
		addListener();
		setCompositionRoot(mainPanel);
	
	}
	
	public void addListener() {
		fvrSequence.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5082302116473500749L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//Boolean hasError = false;
				StringBuffer eMsg = new StringBuffer();
				if((null != fvrTableObj  &&  !fvrTableObj.isValid()) || (null != fvrListenerTableObj && !fvrListenerTableObj.isValid()) || (null != fvrCListenerTableObj && !fvrCListenerTableObj.isValid())) {
					FvrGradingDetailsDTO value = (FvrGradingDetailsDTO) fvrSequence.getValue();
					countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + " FVR" : "" );
					if(value != null){
						if(String.valueOf(value.getNumber()).equals("1")){
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "st FVR" : "" );
						}
						else if(String.valueOf(value.getNumber()).equals("2")){
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "nd FVR" : "" );
						}else if(String.valueOf(value.getNumber()).equals("3")){
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "rd FVR" : "" );
						}else{
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "th FVR" : "" );
						}
						currentValue = value;
						
						if(currentValue != null && currentValue.getIsClearAllEnabled() != null){
							/*if((currentValue.getIsSegmentANotEdit() != null && currentValue.getIsSegmentANotEdit()) && (currentValue.getIsSegmentBNotEdit() != null && currentValue.getIsSegmentBNotEdit()) && (currentValue.getIsSegmentCNotEdit() != null && currentValue.getIsSegmentCNotEdit())){
								clearAll.setEnabled(false);
							}*/
							if(!currentValue.getIsClearAllEnabled()){
								clearAll.setEnabled(false);
							}else{
								clearAll.setEnabled(true);
							}
						}else{
							clearAll.setEnabled(true);
						}
					}
					if(previousSelected != value){
					List<String> errors = new ArrayList<String>();
					if(fvrTableObj != null) {
						errors = fvrTableObj.getErrors();
					} if (fvrListenerTableObj != null) {
						errors = fvrListenerTableObj.getErrors();
					}if (fvrCListenerTableObj != null){
						errors = fvrCListenerTableObj.getErrors();
					}
					
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
					
					Label label = new Label(eMsg.toString(), ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);

					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Alert");
					dialog.setClosable(true);
					dialog.setContent(label);
					dialog.setResizable(false);
					dialog.show(getUI().getCurrent(), null, true);
					}
					if(previousSelected != null) {
						fvrSequence.setValue(previousSelected);
					}
				} else if(null != fvrTableObj || null != fvrListenerTableObj || null != fvrCListenerTableObj) {
					
					tableLayout.removeAllComponents();
					FvrGradingDetailsDTO value = (FvrGradingDetailsDTO) fvrSequence.getValue();
					countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + " FVR" : "" );
					if(value != null){
						if(String.valueOf(value.getNumber()).equals("1")){
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "st FVR" : "" );
						}
						else if(String.valueOf(value.getNumber()).equals("2")){
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "nd FVR" : "" );
						}else if(String.valueOf(value.getNumber()).equals("3")){
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "rd FVR" : "" );
						}else{
							countOfFVR.setValue(value != null ? String.valueOf(value.getNumber()) + "th FVR" : "" );
						}
						currentValue = value;
						
						if(currentValue != null && currentValue.getIsClearAllEnabled() != null){
							/*if((currentValue.getIsSegmentANotEdit() != null && currentValue.getIsSegmentANotEdit()) && (currentValue.getIsSegmentBNotEdit() != null && currentValue.getIsSegmentBNotEdit()) && (currentValue.getIsSegmentCNotEdit() != null && currentValue.getIsSegmentCNotEdit())){
								clearAll.setEnabled(false);
							}*/
							if(!currentValue.getIsClearAllEnabled()){
								clearAll.setEnabled(false);
							}else{
								clearAll.setEnabled(true);
							}
						}else{
							clearAll.setEnabled(true);
						}
					}
					countOfFVR.setWidth("100px");
					if(!isCashless) {
						/*fvrTableObj = fvrTableInstance.get();
						fvrTableObj.init("FVR Grading", false);
						fvrTableObj.initTableDTO(value.getFvrGradingDTO());
						fvrTableObj.initTable();
						fvrTableObj.setReference(referenceData);
						tableLayout.addComponent(fvrTableObj);*/
						fvrTableObj = fvrTableInstance.get();
						fvrTableObj.init(bean);
						fvrTableObj.setReferenceData(referenceData);
						fvrTableObj.setTableList(value.getNewFvrGradingDTO());
						fvrTableObj.dummyField.setValue(null);
						tableLayout.addComponent(fvrTableObj);
						
						
						fvrListenerTableObj = fvrListenerTableInstance.get();
						fvrListenerTableObj.init(bean);
						fvrListenerTableObj.setReferenceData(referenceData);
						fvrListenerTableObj.setTableList(value.getNewFvrGradingDTO());
						fvrListenerTableObj.dummyField.setValue(null);
						tableLayout.addComponent(fvrListenerTableObj);
						
						fvrCListenerTableObj = fvrCListenerTableInstance.get();
						fvrCListenerTableObj.init(bean);
						fvrCListenerTableObj.setReferenceData(referenceData);
						fvrCListenerTableObj.setTableList(value.getNewFvrGradingDTO());
						fvrCListenerTableObj.dummyField.setValue(null);
						tableLayout.addComponent(fvrCListenerTableObj);
						
						addSegmentAListener(fvrTableObj, fvrCListenerTableObj);
						addSegmentBListener(fvrListenerTableObj, fvrCListenerTableObj);
						addSegmentCListener(fvrCListenerTableObj, fvrListenerTableObj, fvrTableObj);
						
						/*if(value.getIsSegmentANotEdit() != null && value.getIsSegmentBNotEdit() != null && !value.getIsSegmentANotEdit() && !value.getIsSegmentBNotEdit()){
							fvrTableObj.dummyField.setValue(SHAConstants.YES);
							fvrListenerTableObj.dummyField.setValue(SHAConstants.YES);
							fvrCListenerTableObj.dummyField.setValue(SHAConstants.No);
						}else if(value.getIsSegmentCNotEdit() != null && !value.getIsSegmentCNotEdit()){
							fvrTableObj.dummyField.setValue(SHAConstants.No);
							fvrListenerTableObj.dummyField.setValue(SHAConstants.No);
							fvrCListenerTableObj.dummyField.setValue(SHAConstants.YES);
						}else{
							fvrTableObj.dummyField.setValue(null);
							fvrListenerTableObj.dummyField.setValue(null);
							fvrCListenerTableObj.dummyField.setValue(null);
						}*/
						
						
					} else {
						/*fvrListenerTableObj = fvrListenerTableInstance.get();
						fvrListenerTableObj.init(bean);
						fvrListenerTableObj.setReferenceData(referenceData);
						fvrListenerTableObj.setTableList(value.getFvrGradingDTO());
						tableLayout.addComponent(fvrListenerTableObj);*/
						fvrTableObj = fvrTableInstance.get();
						fvrTableObj.init(bean);
						fvrTableObj.setReferenceData(referenceData);
						fvrTableObj.setTableList(value.getNewFvrGradingDTO());
						fvrTableObj.dummyField.setValue(null);
						tableLayout.addComponent(fvrTableObj);
						
						
						fvrListenerTableObj = fvrListenerTableInstance.get();
						fvrListenerTableObj.init(bean);
						fvrListenerTableObj.setReferenceData(referenceData);
						fvrListenerTableObj.setTableList(value.getNewFvrGradingDTO());
						fvrListenerTableObj.dummyField.setValue(null);
						tableLayout.addComponent(fvrListenerTableObj);
						
						fvrCListenerTableObj = fvrCListenerTableInstance.get();
						fvrCListenerTableObj.init(bean);
						fvrCListenerTableObj.setReferenceData(referenceData);
						fvrCListenerTableObj.setTableList(value.getNewFvrGradingDTO());
						fvrCListenerTableObj.dummyField.setValue(null);
						tableLayout.addComponent(fvrCListenerTableObj);
						
						addSegmentAListener(fvrTableObj, fvrCListenerTableObj);
						addSegmentBListener(fvrListenerTableObj, fvrCListenerTableObj);
						addSegmentCListener(fvrCListenerTableObj, fvrListenerTableObj, fvrTableObj);
						
						/*if(value.getIsSegmentANotEdit() != null && value.getIsSegmentBNotEdit() != null && !value.getIsSegmentANotEdit() && !value.getIsSegmentBNotEdit()){
							fvrTableObj.dummyField.setValue(SHAConstants.YES);
							fvrListenerTableObj.dummyField.setValue(SHAConstants.YES);
							fvrCListenerTableObj.dummyField.setValue(SHAConstants.No);
						}else if(value.getIsSegmentCNotEdit() != null && !value.getIsSegmentCNotEdit()){
							fvrTableObj.dummyField.setValue(SHAConstants.No);
							fvrListenerTableObj.dummyField.setValue(SHAConstants.No);
							fvrCListenerTableObj.dummyField.setValue(SHAConstants.YES);
						}else{
							fvrTableObj.dummyField.setValue(null);
							fvrListenerTableObj.dummyField.setValue(null);
							fvrCListenerTableObj.dummyField.setValue(null);
						}*/
					}
					
//					countOfFVR = new Label(value != null ? String.valueOf(value.getNumber()) + " FVR" : "" );
					reprentativeCode.setValue(value.getRepresentiveCode());
					representativeName.setValue(value.getRepresentativeName());
					
					remarks = null;
					
					if(value.getIsFVRReceived() != null && value.getIsFVRReceived()){
						remarks = new TextArea("Doctor's Grading suggestions (FVR)");
							if(tableItem != null && !tableItem.isEmpty()){
								if(tableItem.get(value.getKey()) != null){
									TextArea remark = (TextArea)tableItem.get(value.getKey());
									remarks.setValue(remark.getValue());
								}else if(value.getGradingRemarks() != null){
									remarks.setValue(value.getGradingRemarks());
								}
							}else if(value.getGradingRemarks() != null){
								remarks.setValue(value.getGradingRemarks());
							}
						
						remarks.setNullRepresentation("");
						remarks.setWidth("400px");
						remarks.setMaxLength(500);
						
						remarks.setId("gradingRmrks");
						gradingRemarksListener(remarks,null);
						remarks.setData(value);
						remarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
						
						tableItem.put(value.getKey(), remarks);
						
						tableLayout.addComponent(remarks);
						tableLayout.setComponentAlignment(remarks, Alignment.MIDDLE_CENTER);
					}
					
					previousSelected = value;
					
					if(previousSelected != null && previousSelected.getIsClearAllEnabled() != null){/*
						if((previousSelected.getIsSegmentANotEdit() != null && previousSelected.getIsSegmentANotEdit()) && (previousSelected.getIsSegmentBNotEdit() != null && previousSelected.getIsSegmentBNotEdit()) && (previousSelected.getIsSegmentCNotEdit() != null && previousSelected.getIsSegmentCNotEdit())){
							clearAll.setEnabled(false);
						}	
					*/
						if(!previousSelected.getIsClearAllEnabled()){
							clearAll.setEnabled(false);
						}else{
							clearAll.setEnabled(true);
						}
					}else{
						clearAll.setEnabled(true);
					}
					
				}
			}
		});
	}
	
	public void setTableList(List<NewFVRGradingDTO> dto) {
		if(fvrListenerTableObj != null) {
			for (NewFVRGradingDTO fvrGradingDTO : dto) {
				this.fvrListenerTableObj.addBeanToList(fvrGradingDTO);
			}
		}
		
		if(fvrCListenerTableObj != null) {
			for (NewFVRGradingDTO fvrGradingDTO : dto) {
				this.fvrCListenerTableObj.addBeanToList(fvrGradingDTO);
			}
		}
	}
	
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		if(fvrTableObj != null) {
			fvrTableObj.setReferenceData(referenceData);
		}
		if(fvrListenerTableObj != null) {
			fvrListenerTableObj.setReferenceData(referenceData);
		}
		
		if(fvrCListenerTableObj != null) {
			fvrCListenerTableObj.setReferenceData(referenceData);
		}
	}
	
	public int getSegmentCListenerTableSelectCount(){
		int selectcount = fvrCListenerTableObj.getSelectedCount();
		return selectcount; 
	}
	public void addSegmentBListener(
			NewMedicalDecisionFVRGradingBListenerTable fvrBListenerTableObj,
			final NewMedicalDecisionFVRGradingCListenerTable fvrCListenerTableObj) {

		fvrBListenerTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						String value = (String) event.getProperty().getValue();
						if (value != null
								&& value.equalsIgnoreCase(SHAConstants.YES)) {
							// fvrCListenerTableObj.resetTableList(((FvrGradingDetailsDTO)
							// fvrSequence.getValue()).getNewFvrGradingDTO());
							FvrGradingDetailsDTO detailDTO = (FvrGradingDetailsDTO) fvrSequence.getValue();
							detailDTO.setIsSegmentCNotEdit(true);
							detailDTO.setIsSegmentBNotEdit(false);
							detailDTO.setIsSegmentANotEdit(false);
							fvrCListenerTableObj
									.nonEditFields(detailDTO.getNewFvrGradingDTO());
						}

					}
				});

	}
	
	public void addSegmentCListener(
			NewMedicalDecisionFVRGradingCListenerTable fvrCListenerTableObj,
			final NewMedicalDecisionFVRGradingBListenerTable fvrBListenerTableObj,final NewMedicalDecisionFVRGradingAListenerTable fvrAListenerTableObj) {

		fvrCListenerTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						String value = (String) event.getProperty().getValue();
						if (value != null
								&& value.equalsIgnoreCase(SHAConstants.YES)) {
							// fvrListenerTableObj.resetTableList(((FvrGradingDetailsDTO)
							// fvrSequence.getValue()).getNewFvrGradingDTO());
							FvrGradingDetailsDTO detailDTO = (FvrGradingDetailsDTO) fvrSequence.getValue();
							detailDTO.setIsSegmentCNotEdit(false);
							detailDTO.setIsSegmentBNotEdit(true);
							detailDTO.setIsSegmentANotEdit(true);
							fvrAListenerTableObj.nonEditFields(detailDTO.getNewFvrGradingDTO());
							fvrBListenerTableObj
									.nonEditFields(detailDTO.getNewFvrGradingDTO());
						}

					}
				});

	}
	
	
	public void addSegmentAListener(
			NewMedicalDecisionFVRGradingAListenerTable fvrAListenerTableObj,
			final NewMedicalDecisionFVRGradingCListenerTable fvrCListenerTableObj) {

		fvrAListenerTableObj.dummyField
				.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {

						String value = (String) event.getProperty().getValue();
						if (value != null
								&& value.equalsIgnoreCase(SHAConstants.YES)) {
							// fvrCListenerTableObj.resetTableList(((FvrGradingDetailsDTO)
							// fvrSequence.getValue()).getNewFvrGradingDTO());
							FvrGradingDetailsDTO detailDTO = (FvrGradingDetailsDTO) fvrSequence.getValue();
							detailDTO.setIsSegmentCNotEdit(true);
							detailDTO.setIsSegmentBNotEdit(false);
							detailDTO.setIsSegmentANotEdit(false);
							fvrCListenerTableObj
									.nonEditFields(detailDTO.getNewFvrGradingDTO());
						}

					}
				});

	}
	
	public void addClearAllListener(){
		clearAll.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(currentValue.getNewFvrGradingDTO() != null){
					List<FvrGradingDetailsDTO> fvrGradingDTO = new ArrayList<FvrGradingDetailsDTO>();
					if(fvrBean != null){
						fvrGradingDTO = fvrBean.getFvrGradingDTO();
					}else{
						fvrGradingDTO = bean.getPreauthMedicalDecisionDetails().getFvrGradingDTO();	
					}
					
					
					int position = 0;
					boolean isValue = false;
					
					currentValue.setIsSegmentANotEdit(true);
					currentValue.setIsSegmentBNotEdit(true);
					currentValue.setIsSegmentCNotEdit(true);
					fvrTableObj.resetTableList(currentValue.getNewFvrGradingDTO());
					fvrListenerTableObj.resetTableList(currentValue.getNewFvrGradingDTO());
					fvrCListenerTableObj.resetTableList(currentValue.getNewFvrGradingDTO());
					
					for (FvrGradingDetailsDTO fvrGradingDetailsDTO : fvrGradingDTO) {
						
						if(fvrGradingDetailsDTO.getKey().equals(currentValue.getKey())){
							position = fvrGradingDTO.indexOf(fvrGradingDetailsDTO);
							isValue = true;
							break;
						}
					}
					if(isValue){
						fvrGradingDTO.set(position, currentValue);
					}
					if(fvrBean != null){
						fvrBean.setFvrGradingDTO(fvrGradingDTO);
					}else{
						bean.getPreauthMedicalDecisionDetails().setFvrGradingDTO(fvrGradingDTO);	
					}
					
				}
				
				
			}
		});
	}
	
	public void initView(FvrReportGradingPageDto bean, Boolean isCashless) {
		this.fvrBean = bean;
		this.isCashless = isCashless;
		List<FvrGradingDetailsDTO> fvrGradingDTO = this.fvrBean.getFvrGradingDTO();
		//List<FvrGradingDetailsDTO> values = new ArrayList<FvrGradingDetailsDTO>();
		int i = 1;
		for (FvrGradingDetailsDTO value : fvrGradingDTO) {
			value.setNumber(i++);
			
		}
		BeanItemContainer<FvrGradingDetailsDTO> dtoList = new BeanItemContainer<FvrGradingDetailsDTO>(FvrGradingDetailsDTO.class);
		dtoList.addAll(fvrGradingDTO);
		
		fvrSequence = new ComboBox("FVR Sequence");
		fvrSequence.setWidth("100px");
		fvrSequence.setContainerDataSource(dtoList);
		fvrSequence.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		fvrSequence.setItemCaptionPropertyId("number");
		Collection<?> itemIds = fvrSequence.getContainerDataSource().getItemIds();
		fvrSequence.setValue(itemIds.toArray()[0]);
		fvrSequence.setNullSelectionAllowed(false);
		
		
		FvrGradingDetailsDTO value = (FvrGradingDetailsDTO) fvrSequence.getValue();
		
		countOfFVR = new Label(value != null ? String.valueOf(value.getNumber()) + "st FVR" : "" );
		countOfFVR.setWidth("100px");
		
		reprentativeCode = new TextField("Representative Code");
		reprentativeCode.setValue(value.getRepresentiveCode());
		reprentativeCode.setNullRepresentation("-");
		reprentativeCode.setEnabled(false);
		reprentativeCode.setWidth("100px");
		
		representativeName = new TextField("Representative Name");
		representativeName.setValue(value.getRepresentativeName());
		representativeName.setNullRepresentation("-");
		representativeName.setEnabled(false);
		representativeName.setWidth("100px");
		
		clearAll = new Button("Clear All");
		clearAll.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		clearAll.setWidth("150px");
		
		currentValue = value;
		
		if(currentValue != null){
			/*if((currentValue.getIsSegmentANotEdit() != null && currentValue.getIsSegmentANotEdit()) && (currentValue.getIsSegmentBNotEdit() != null && currentValue.getIsSegmentBNotEdit()) && (currentValue.getIsSegmentCNotEdit() != null && currentValue.getIsSegmentCNotEdit())){
				clearAll.setEnabled(false);
			}*/
			if(currentValue.getIsClearAllEnabled() != null && !currentValue.getIsClearAllEnabled()){
				clearAll.setEnabled(false);
			}
		}
		
		addClearAllListener();
		
		
		HorizontalLayout fvrSeqLayout = new HorizontalLayout(new FormLayout(fvrSequence), countOfFVR);
		fvrSeqLayout.setSpacing(true);
		
		HorizontalLayout firstHLayout = new HorizontalLayout(fvrSeqLayout, new FormLayout(reprentativeCode) );
		firstHLayout.setSpacing(true);
		firstHLayout.setWidth("100%");
		
		if(!this.isCashless) {
			/*fvrTableObj = fvrTableInstance.get();
			fvrTableObj.init("FVR Grading", false);
			fvrTableObj.initTableDTO(value.getFvrGradingDTO());
			fvrTableObj.initTable();
			
			tableLayout = new VerticalLayout(fvrTableObj);*/
			
			fvrTableObj = fvrTableInstance.get();
			fvrTableObj.init();
			fvrTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout = new VerticalLayout(fvrTableObj);
			
			
			fvrListenerTableObj = fvrListenerTableInstance.get();
			fvrListenerTableObj.init();
			fvrListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrListenerTableObj);
			
			fvrCListenerTableObj = fvrCListenerTableInstance.get();
			fvrCListenerTableObj.init();
			fvrCListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrCListenerTableObj);
			
			addSegmentAListener(fvrTableObj, fvrCListenerTableObj);
			addSegmentBListener(fvrListenerTableObj, fvrCListenerTableObj);
			addSegmentCListener(fvrCListenerTableObj, fvrListenerTableObj, fvrTableObj);
			
			/*if(value.getIsSegmentANotEdit() != null && value.getIsSegmentBNotEdit() != null && !value.getIsSegmentANotEdit() && !value.getIsSegmentBNotEdit()){
				fvrTableObj.dummyField.setValue(SHAConstants.YES);
				fvrListenerTableObj.dummyField.setValue(SHAConstants.YES);
				fvrCListenerTableObj.dummyField.setValue(SHAConstants.No);
			}else if(value.getIsSegmentCNotEdit() != null && !value.getIsSegmentCNotEdit()){
				fvrTableObj.dummyField.setValue(SHAConstants.No);
				fvrListenerTableObj.dummyField.setValue(SHAConstants.No);
				fvrCListenerTableObj.dummyField.setValue(SHAConstants.YES);
			}else{
				fvrTableObj.dummyField.setValue(null);
				fvrListenerTableObj.dummyField.setValue(null);
				fvrCListenerTableObj.dummyField.setValue(null);
			}*/
			
			
		} else {
			/*fvrListenerTableObj = fvrListenerTableInstance.get();
			fvrListenerTableObj.init(this.bean);
			fvrListenerTableObj.setTableList(value.getFvrGradingDTO());
			tableLayout = new VerticalLayout(fvrListenerTableObj);*/
			
			fvrTableObj = fvrTableInstance.get();
			fvrTableObj.init();
			fvrTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout = new VerticalLayout(fvrTableObj);
			
			fvrListenerTableObj = fvrListenerTableInstance.get();
			fvrListenerTableObj.init();
			fvrListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrListenerTableObj);
			
			fvrCListenerTableObj = fvrCListenerTableInstance.get();
			fvrCListenerTableObj.init();
			fvrCListenerTableObj.setTableList(value.getNewFvrGradingDTO());
			tableLayout.addComponent(fvrCListenerTableObj);
			
			addSegmentAListener(fvrTableObj, fvrCListenerTableObj);
			addSegmentBListener(fvrListenerTableObj, fvrCListenerTableObj);
			addSegmentCListener(fvrCListenerTableObj, fvrListenerTableObj, fvrTableObj);
			
			
		}
		VerticalLayout mainPanel = new VerticalLayout(firstHLayout, new FormLayout(representativeName),clearAll, tableLayout);
		/*if(value.getIsSegmentANotEdit() != null && value.getIsSegmentBNotEdit() != null && !value.getIsSegmentANotEdit() && !value.getIsSegmentBNotEdit()){
			fvrTableObj.dummyField.setValue(SHAConstants.YES);
			fvrListenerTableObj.dummyField.setValue(SHAConstants.YES);
			fvrCListenerTableObj.dummyField.setValue(SHAConstants.No);
		}else if(value.getIsSegmentCNotEdit() != null && !value.getIsSegmentCNotEdit()){
			fvrTableObj.dummyField.setValue(SHAConstants.No);
			fvrListenerTableObj.dummyField.setValue(SHAConstants.No);
			fvrCListenerTableObj.dummyField.setValue(SHAConstants.YES);
		}else{
			fvrTableObj.dummyField.setValue(null);
			fvrListenerTableObj.dummyField.setValue(null);
			fvrCListenerTableObj.dummyField.setValue(null);
		}*/
		dtoMapping.put(value.getNumber(), value);
		addListener();
		setCompositionRoot(mainPanel);
	
	}
	
	public Map<Long, AbstractField<?>> getGradingRemarks(){
		return tableItem;
	}
	
	public  void gradingRemarksListener(TextArea searchField, final  Listener listener) {
		
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "ShortcutForGradingRemarks", ShortcutAction.KeyCode.F8, null) {
		
	      private static final long serialVersionUID = 1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    handleShortcutForGradingRemarks(searchField, getShortCutListenerForGradingRemarks(searchField));
	    
	  }
	
	public  void handleShortcutForGradingRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private ShortcutListener getShortCutListenerForGradingRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();
				final Window dialog = new Window();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(500);
				txtArea.setData(bean);
				//txtArea.setStyleName("Boldstyle");
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				//txtArea.setSizeFull();
				
				
				if(("gradingRmrks").equalsIgnoreCase(txtFld.getId())){
					txtArea.setRows(25);
					txtArea.setHeight("30%");
					txtArea.setWidth("100%");
					dialog.setHeight("75%");
			    	dialog.setWidth("65%");
					txtArea.setReadOnly(false);
				}
				
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(("gradingRmrks").equalsIgnoreCase(txtFld.getId())){
							txtFld.setValue(((TextArea)event.getProperty()).getValue());						
						}
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				
				String strCaption = "";
				
				if(("gradingRmrks").equalsIgnoreCase(txtFld.getId())){
					strCaption = "Doctor's Grading suggestions (FVR)";
				}
			   				    	
				dialog.setCaption(strCaption);
						
				
				dialog.setClosable(true);
				
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				
				dialog.addCloseListener(new Window.CloseListener() {
					
					@Override
					public void windowClose(CloseEvent e) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
//						TextArea txtArea = (TextArea)dialog.getData();
//						txtArea.setValue(bean.getReimbursementRejectionDto().getRejectionLetterRemarks());
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
}
