package com.shaic.claim.fvrdetails.view;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewFVRDetailsViewImpl extends AbstractMVPView implements
		ViewFVRDetailsView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ViewFVRDetailsTable viewFVRDetailsTable;

	@Inject
	private ViewFVRService viewFVRService;

	@Inject
	private ViewFVRFormDTO bean;

	@Inject
	private Intimation intimation;

//	@Inject 
//	private FVRTriggerPtsTable triggerPtsTable;
	
	private BeanFieldGroup<ViewFVRFormDTO> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private ComboBox cmbAllocationTo;
	
	private ComboBox cmbFvrAssignTo;
	
	private ComboBox cmbFvrPriority;

//	private Label reMark;

	private FormLayout layout;

	private OptionGroup select;

	private Button submit;
	
	private VerticalLayout mainlayout;
	
	private boolean isFVRDisabled = false;
	
	private Long preauthKey;
	
	private Long stageKey;

	public Long getPreauthKey() {
		return preauthKey;
	}

	public void setPreauthKey(Long preauthKey) {
		this.preauthKey = preauthKey;
	}
	
	public void setStageKey(Long stageKey){
		this.stageKey = stageKey;
	}

	public void init(Intimation intimation,boolean isFVRDisabled) {
		initBinder();
		viewFVRDetailsTable.init("", false, false);		
		viewFVRDetailsTable.setTableList(viewFVRService.searchFVR(intimation
				.getKey()));
		//this.isFVRDisabled = isFVRDisabled;
		this.isFVRDisabled = false;
		isFVRDisabled = false;
		VerticalLayout table = new VerticalLayout(viewFVRDetailsTable);
		this.intimation = intimation;
		VerticalLayout visit = new VerticalLayout();
		visit.addComponent(fVRVisit());
		fireViewEvent(ViewFVRDetailsPresenter.SET_REFFER_DATA, null);
		mainlayout = new VerticalLayout();
		mainlayout.addComponent(table);
		if(isFVRDisabled){
			mainlayout.addComponent(visit);
		}		
		setCompositionRoot(mainlayout);
	}

	public ViewFVRService getViewFVRService() {
		return viewFVRService;
	}

	public void setViewFVRService(ViewFVRService viewFVRService) {
		this.viewFVRService = viewFVRService;
	}

	public VerticalLayout fVRVisit() {
		final VerticalLayout submainlayout = new VerticalLayout();
		select = new OptionGroup();
		select.addStyleName("horizontal");
		select.addItem("yes");
		select.addItem("no");
		select.setItemCaption("yes", "Yes");
		select.setItemCaption("no", "NO");
		select.select("no");
		
		/*if(isFVRDisabled)
		{
			select.setEnabled(false);
			select.setReadOnly(true);
		}
		else 
		{
			select.setEnabled(true);
			select.setReadOnly(false);
		}*/
		HorizontalLayout selectlayout = new HorizontalLayout(new Label(
				"Initiate Field Visit Request:"), select);
		selectlayout.setSpacing(true);
		submainlayout.addComponent(selectlayout);
		submainlayout.setComponentAlignment(selectlayout,
				Alignment.MIDDLE_CENTER);
		final Panel panel = new Panel(new VerticalLayout(formlayout()));
		panel.setWidth("70%");
		panel.setVisible(false);
		select.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (select.getValue().equals("yes")) {					
					panel.setVisible(true);
//					((VerticalLayout)panel.getContent()).addComponent(triggerPtsTable);
					
				} else if (select.getValue().equals("no")) {
//					((VerticalLayout)panel.getContent()).removeComponent(triggerPtsTable);
					panel.setVisible(false);
				}
			}
		});
		submainlayout.addComponent(panel);
		submainlayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		return submainlayout;
	}

	private void initBinder() {
		this.binder = new BeanFieldGroup<ViewFVRFormDTO>(ViewFVRFormDTO.class);
		this.binder.setItemDataSource(new ViewFVRFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public FormLayout formlayout() {
		
		unbindField(cmbAllocationTo);
		unbindField(cmbFvrAssignTo);
		unbindField(cmbFvrPriority);
//		unbindField(reMark);
		
		mandatoryFields.remove(cmbAllocationTo);
		mandatoryFields.remove(cmbFvrAssignTo);
		mandatoryFields.remove(cmbFvrPriority);
//		mandatoryFields.remove(reMark);
		
		cmbAllocationTo = (ComboBox) binder.buildAndBind("Allocation To ",
				"allocateTo", ComboBox.class);

		
		cmbFvrAssignTo = (ComboBox) binder.buildAndBind("Assign To",
				"assignTo", ComboBox.class);

		
		cmbFvrPriority = (ComboBox) binder.buildAndBind("Priority",
				"fvrPriority", ComboBox.class);
//		reMark = new Label("triggerPoints");
		
//		triggerPtsTable.init("", true, true);
//		reMark = (TextArea) binder.buildAndBind("Remark (trigger Points)",
//				"triggerPoints", TextArea.class);
//		reMark.setMaxLength(4000);
//		reMark.setWidth("100%");
//		// Fix for issue 770.
//		CSValidator validator = new CSValidator();
//		validator.extend(reMark);
//		validator.setRegExp("^[a-zA-Z 0-9.]*$");
//		validator.setPreventInvalidTyping(true);

//		cmbAllocationTo.addValidator(new BeanValidator(ViewFVRFormDTO.class,
//				"allocateTo"));
//		cmbFvrAssignTo.addValidator(new BeanValidator(ViewFVRFormDTO.class,
//				"assignTo"));
//		cmbFvrPriority.addValidator(new BeanValidator(ViewFVRFormDTO.class,
//				"fvrPriority"));
		
		mandatoryFields.add(cmbAllocationTo);
		mandatoryFields.add(cmbFvrAssignTo);
		mandatoryFields.add(cmbFvrPriority);
//		mandatoryFields.add(reMark);

		showOrHideValidation(false);
		
//		reMark.addValidator(new BeanValidator(ViewFVRFormDTO.class,
//				"triggerPoints"));
		submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		layout = new FormLayout(cmbAllocationTo,cmbFvrAssignTo,cmbFvrPriority,reMark, submit);
		layout = new FormLayout(cmbAllocationTo,cmbFvrAssignTo,cmbFvrPriority, submit);
		layout.setSpacing(true);
		layout.setMargin(true);
		submit.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					

					commitValue();
					
				} catch (Exception e) {

				}

			}
		});

		return layout;
	}

	@Override
	public void resetView() {

	}

	@Override
	public void list(BeanItemContainer<SelectValue> selectValueContainer,BeanItemContainer<SelectValue> fvrAssignTo,BeanItemContainer<SelectValue> fvrPriority) {
		cmbAllocationTo.setContainerDataSource(selectValueContainer);
		cmbFvrAssignTo.setContainerDataSource(fvrAssignTo);
		cmbFvrPriority.setContainerDataSource(fvrPriority);
	}
	
	public void setTableValue(List<ViewFVRDTO> viewFVRDTOList){		
		viewFVRDetailsTable.setTableList(viewFVRDTOList);
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

	private void commitValue() {
		try {
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
			
			if (select.getValue().equals("yes")) {
//				if(triggerPtsTable != null){
//					hasError = triggerPtsTable.isValid();
//					eMsg.append(" <br>Please Provide atleast one Trigger Points. (OR) <br>Trigger Points size will be Max. of 300"); 
//				}
			}
			
			if(hasError){
				
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
				
			}else{
			
				this.binder.commit();
				SelectValue selected = new SelectValue();
				selected = (SelectValue) cmbAllocationTo.getValue();
				bean.setAllocateTo(selected);
				SelectValue selected1 = new SelectValue();
				selected1 = (SelectValue) cmbFvrAssignTo.getValue();
				bean.setAssignTo(selected1);
				SelectValue selected2 = new SelectValue();
				selected2 = (SelectValue) cmbFvrPriority.getValue();
				bean.setFvrPriority(selected2);
//				bean.setTriggerPoints(reMark.getValue());
//				bean.setTrgrPtsList(triggerPtsTable.getValues());
				bean.setUsername(UI.getCurrent().getUI().getSession()
						.getAttribute(BPMClientContext.USERID).toString());
				bean.setPassword(UI.getCurrent().getUI().getSession()
						.getAttribute(BPMClientContext.PASSWORD).toString());
				fireViewEvent(ViewFVRDetailsPresenter.SUBMIT_BUTTON_CLICKED, bean,
						intimation,this.preauthKey,this.stageKey);
				mainlayout.removeAllComponents();
				init(intimation,isFVRDisabled);
			}
			showOrHideValidation(false);
		} catch (CommitException e) {
		    e.printStackTrace();
			Notification.show("Error", "Please Enter Allocation To and Remarks",
					Notification.Type.ERROR_MESSAGE);
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	public void clearFVRDetailsPopup() {
		if(mainlayout != null){
			mainlayout.removeAllComponents();
    	}
    	if(this.viewFVRDetailsTable != null){
    		this.viewFVRDetailsTable.getTable().clear();
    	}
		
	}

}
