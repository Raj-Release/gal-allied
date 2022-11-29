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

import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.FVRGradingDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class MedicalDecisionFVRGrading extends ViewComponent {
	private static final long serialVersionUID = -4135096211808010097L;

	private ComboBox fvrSequence;
	
	private Label countOfFVR;
	
	private TextField reprentativeCode;
	
	private TextField representativeName;
	
	private PreauthDTO bean;
	
	@Inject
	private Instance<MedicalDecisionFVRGradingTable> fvrTableInstance;
	
	private MedicalDecisionFVRGradingTable fvrTableObj;
	
	@Inject
	private Instance<MedicalDecisionFVRGradingListenerTable> fvrListenerTableInstance;
	
	private MedicalDecisionFVRGradingListenerTable fvrListenerTableObj;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private FvrGradingDetailsDTO previousSelected = new FvrGradingDetailsDTO();
	
	public Map<Integer, FvrGradingDetailsDTO> dtoMapping = new HashMap<Integer, FvrGradingDetailsDTO>();

	private VerticalLayout tableLayout;
	
	private Boolean isCashless = false;
	
	@PostConstruct
	public void init() {
		
	}

	public void initView(PreauthDTO bean, Boolean isCashless) {
		this.bean = bean;
		this.isCashless = isCashless;
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
		
		HorizontalLayout fvrSeqLayout = new HorizontalLayout(new FormLayout(fvrSequence), countOfFVR);
		fvrSeqLayout.setSpacing(true);
		
		HorizontalLayout firstHLayout = new HorizontalLayout(fvrSeqLayout, new FormLayout(reprentativeCode) );
		firstHLayout.setSpacing(true);
		firstHLayout.setWidth("100%");
		
		if(!this.isCashless) {
			fvrTableObj = fvrTableInstance.get();
			fvrTableObj.init("FVR Grading", false);
			fvrTableObj.initTableDTO(value.getFvrGradingDTO());
			fvrTableObj.initTable();
			
			tableLayout = new VerticalLayout(fvrTableObj);
		} else {
			fvrListenerTableObj = fvrListenerTableInstance.get();
			fvrListenerTableObj.init(this.bean);
			fvrListenerTableObj.setTableList(value.getFvrGradingDTO());
			tableLayout = new VerticalLayout(fvrListenerTableObj);
		}
		
		VerticalLayout mainPanel = new VerticalLayout(firstHLayout, new FormLayout(representativeName), tableLayout);
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
				if((null != fvrTableObj  &&  !fvrTableObj.isValid()) || (null != fvrListenerTableObj && !fvrListenerTableObj.isValid())) {
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
					}
					if(previousSelected != value){
					List<String> errors = new ArrayList<String>();
					if(fvrTableObj != null) {
						errors = fvrTableObj.getErrors();
					} else if (fvrListenerTableObj != null) {
						errors = fvrListenerTableObj.getErrors();
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
				} else if(null != fvrTableObj || null != fvrListenerTableObj) {
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
					}
					countOfFVR.setWidth("100px");
					if(!isCashless) {
						fvrTableObj = fvrTableInstance.get();
						fvrTableObj.init("FVR Grading", false);
						fvrTableObj.initTableDTO(value.getFvrGradingDTO());
						fvrTableObj.initTable();
						fvrTableObj.setReference(referenceData);
						tableLayout.addComponent(fvrTableObj);
					} else {
						fvrListenerTableObj = fvrListenerTableInstance.get();
						fvrListenerTableObj.init(bean);
						fvrListenerTableObj.setReferenceData(referenceData);
						fvrListenerTableObj.setTableList(value.getFvrGradingDTO());
						tableLayout.addComponent(fvrListenerTableObj);
					}
					
//					countOfFVR = new Label(value != null ? String.valueOf(value.getNumber()) + " FVR" : "" );
					reprentativeCode.setValue(value.getRepresentiveCode());
					representativeName.setValue(value.getRepresentativeName());
					
					previousSelected = value;
					
					
				}
			}
		});
	}
	
	public void setTableList(List<FVRGradingDTO> dto) {
		if(fvrListenerTableObj != null) {
			for (FVRGradingDTO fvrGradingDTO : dto) {
				this.fvrListenerTableObj.addBeanToList(fvrGradingDTO);
			}
		}
	}
	
	public void setupReferences(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		if(fvrTableObj != null) {
			fvrTableObj.setReference(referenceData);
		}
		if(fvrListenerTableObj != null) {
			fvrListenerTableObj.setReferenceData(referenceData);
		}
	}
}
