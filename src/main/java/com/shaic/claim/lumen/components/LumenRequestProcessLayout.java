package com.shaic.claim.lumen.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LumenRequestProcessLayout extends ViewComponent{

	//buttons
	private Button approveButton;
	private Button rejectButton;
	private Button replyButton;
	private Button queryButton;
	private Button closeButton;
	private Button referToMIS;

	//approve fields
	private ComboBox cmbApproveTo;
	private TextArea approveComments;

	//reject fields
	private TextArea rejectComments;

	//reply fields
	private ComboBox cmbReplyTo;
	private TextArea replyComments;

	//query fields
	private ComboBox cmbQueryTo;
	private TextArea queryComments;

	//close fields
	private TextArea closeComments;
	
	//Level 2 Approve option box
	private OptionGroup generateLetterOption;

	private String actionName;

	private FormLayout tempLayout;

	private VerticalLayout mainLayout;

	private HorizontalLayout buttonHorlayout;

//	@Inject
//	private Instance<ReferToMISTableOld> referToMISTableInstance;
//
//	private ReferToMISTableOld referToMISTableObj;
	
	@Inject
	private Instance<ReferToMISTable> referToMISTableInstance;

	private ReferToMISTable referToMISTableObj;
	
	@Inject
	private Instance<GenerateLetterTable> letterTableInstance;
	
	private GenerateLetterTable letterTableObj;

	private String screenName;
	
	private AbsoluteLayout actionLayout;
	
	private HorizontalLayout containerHorlayout;
	
	private VerticalLayout containerVlayout;
	
	private String queryRaisedRole;	
	
	public GenerateLetterTable getLetterTableObj() {
		return letterTableObj;
	}

	public void setLetterTableObj(GenerateLetterTable letterTableObj) {
		this.letterTableObj = letterTableObj;
	}

	public String getQueryRaisedRole() {
		return queryRaisedRole;
	}

	public void setQueryRaisedRole(String queryRaisedRole) {
		this.queryRaisedRole = queryRaisedRole;
	}
	
	public FormLayout getTempLayout() {
		return tempLayout;
	}

	public void setTempLayout(FormLayout tempLayout) {
		this.tempLayout = tempLayout;
	}

	public void init(boolean isApproveVisible, boolean isRejectVisible, boolean isReplyVisible, boolean isQueryVisible, boolean isCloseVisible, boolean isMISVisible, String argScreenName) {
		this.screenName = argScreenName;
		mainLayout = new VerticalLayout();
		buttonHorlayout = new HorizontalLayout();
		tempLayout = new FormLayout();
		generateButtons(isApproveVisible, isRejectVisible, isReplyVisible, isQueryVisible, isCloseVisible, isMISVisible);	
		addListeners();
		buttonHorlayout.addComponent(approveButton);
		buttonHorlayout.addComponent(rejectButton);
		buttonHorlayout.addComponent(replyButton);
		buttonHorlayout.addComponent(queryButton);
		buttonHorlayout.addComponent(closeButton);
		buttonHorlayout.addComponent(referToMIS);
		buttonHorlayout.setSpacing(true);
		
		containerHorlayout = new HorizontalLayout();
		containerHorlayout.addComponent(tempLayout);
		containerHorlayout.setMargin(true);
		containerHorlayout.setSizeFull();
		
//		containerVHorlayout = new VerticalLayout();
//		containerVHorlayout.addComponent(tempLayout);
//		containerVHorlayout.setComponentAlignment(tempLayout, Alignment.MIDDLE_CENTER);
//		containerVHorlayout.setMargin(true);
//		containerVHorlayout.setSizeFull();
		
		if(screenName.equals("Process Level II")){
			containerVlayout = new VerticalLayout();
			containerVlayout.setSizeFull();
		}

		actionLayout =  new AbsoluteLayout();
		actionLayout.addComponent(containerHorlayout, "left: 30%; top: 0%;");		
		actionLayout.setWidth("100%");
		actionLayout.setHeight("300px");
		
		
		mainLayout.addComponent(buttonHorlayout);
		mainLayout.setComponentAlignment(buttonHorlayout, Alignment.TOP_CENTER);
		mainLayout.setSpacing(false);
		setCompositionRoot(mainLayout);
	}

	public void generateButtons(boolean isApproveVisible, boolean isRejectVisible, boolean isReplyVisible, boolean isQueryVisible, boolean isCloseVisible, boolean isMISVisible){
		approveButton = new Button("Approve");
		approveButton.setVisible(isApproveVisible);

		rejectButton = new Button("Reject");
		rejectButton.setVisible(isRejectVisible);

		replyButton = new Button("Reply");
		replyButton.setVisible(isReplyVisible);

		queryButton = new Button("Query");
		queryButton.setVisible(isQueryVisible);

		closeButton = new Button("Close");
		closeButton.setVisible(isCloseVisible);

		referToMIS = new Button("Refer to MIS");
		referToMIS.setVisible(isMISVisible);
	}



	public void generateFields(String buttonName){

		if(tempLayout != null){
			tempLayout.removeAllComponents();
		}
		/*if(mainLayout.getComponentCount() == 3){
			mainLayout.removeComponent(actionLayout);
		}*/
		mainLayout.addComponent(actionLayout);

		if(buttonName.equals("Approve") && !screenName.equals("Process Level II")){
			cmbApproveTo = new ComboBox("To");
			cmbApproveTo.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					int valueLen = cmbApproveTo.getValue().toString().length();
					if(valueLen > 30){
						cmbApproveTo.setWidth("250px");
					}else{
						cmbApproveTo.setWidth("-1");
					}
				}
			});
			
			generateDropDownValues("Approve");
			approveComments = new TextArea("Comments");
			approveComments.setRows(6);
			approveComments.setColumns(22);
			approveComments.setMaxLength(4000);
			handleTextAreaPopup(approveComments,null);
			tempLayout.addComponent(cmbApproveTo);
			tempLayout.addComponent(approveComments);
		}
		if(buttonName.equals("Approve") && screenName.equals("Process Level II")){
			generateLetterOption = new OptionGroup("Do you want to Generate Letter ?");
			generateLetterOption.addItems(getRadioButtonOptions());
			generateLetterOption.setItemCaption(true, "Yes");
			generateLetterOption.setItemCaption(false, "No");
			generateLetterOption.setStyleName("horizontal");
			generateLetterOption.select(false);
			generateLetterOption.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange(ValueChangeEvent event) {
					Boolean isChecked = false;
					if (event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
						isChecked = (Boolean) event.getProperty().getValue();
						if(isChecked){
							if(tempLayout.getComponentCount() == 3){
								Component tableComponent = tempLayout.getComponent(2);
								tempLayout.removeComponent(tableComponent);
							}
							letterTableObj = letterTableInstance.get();
							letterTableObj.init(new LetterDetailsDTO());
							letterTableObj.setVisibleColumns();
							containerVlayout.removeAllComponents();
							containerVlayout.addComponent(letterTableObj);
							containerVlayout.setComponentAlignment(letterTableObj, Alignment.MIDDLE_RIGHT);
							mainLayout.addComponent(containerVlayout);
						}/*else{
							//if(tempLayout.getComponentCount() == 3){
//								Component table1Component = tempLayout.getComponent(1);
//								tempLayout.removeComponent(table1Component);
								if(mainLayout.getComponentIndex(containerVlayout) != -1){
									mainLayout.removeComponent(containerVlayout);
								}
//								Component table2Component = tempLayout.getComponent(2);
//								tempLayout.removeComponent(table2Component);
							//}
						}*/
						//fireViewEvent(ProcessLevelTwoWizardPresenter.SHOW_GENERATE_LETTER_TABLE, isChecked);
					}else{
						if(mainLayout.getComponentIndex(containerVlayout) != -1){
							containerVlayout.removeAllComponents();
							mainLayout.removeComponent(containerVlayout);
						}
					}
				}
			});
			approveComments = new TextArea("Comments");
			approveComments.setRows(6);
			approveComments.setColumns(22);
			approveComments.setMaxLength(4000);
			handleTextAreaPopup(approveComments,null);
			tempLayout.addComponent(generateLetterOption);
			tempLayout.addComponent(approveComments);
		}
		if(buttonName.equals("Reject")){
			rejectComments = new TextArea("Comments");
			rejectComments.setRows(6);
			rejectComments.setColumns(22);
			rejectComments.setMaxLength(4000);
			handleTextAreaPopup(rejectComments,null);
			tempLayout.addComponent(rejectComments);
			if(screenName.equals("Process Level II") && mainLayout.getComponentIndex(containerVlayout) != -1){
				containerVlayout.removeAllComponents();
				mainLayout.removeComponent(containerVlayout);
			}
		}
		if(buttonName.equals("Reply")){
			cmbReplyTo = new ComboBox("To");
			cmbReplyTo.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					int valueLen = cmbReplyTo.getValue().toString().length();
					if(valueLen > 30){
						cmbReplyTo.setWidth("250px");
					}else{
						cmbReplyTo.setWidth("-1");
					}
				}
			});
			generateDropDownValues("Reply");
			replyComments = new TextArea("Reply Remarks (Lumen)");
			replyComments.setRows(6);
			replyComments.setColumns(22);
			replyComments.setMaxLength(4000);
			handleTextAreaPopup(replyComments,null);
			tempLayout.addComponent(cmbReplyTo);
			tempLayout.addComponent(replyComments);
			if(screenName.equals("Process Level II") && mainLayout.getComponentIndex(containerVlayout) != -1){
				containerVlayout.removeAllComponents();
				mainLayout.removeComponent(containerVlayout);
			}
		}
		if(buttonName.equals("Query")){
			cmbQueryTo = new ComboBox("To");
			cmbQueryTo.addValueChangeListener(new ValueChangeListener() {
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					int valueLen = cmbQueryTo.getValue().toString().length();
					if(valueLen > 30){
						cmbQueryTo.setWidth("250px");
					}else{
						cmbQueryTo.setWidth("-1");
					}
				}
			});
			generateDropDownValues("Query");
			queryComments = new TextArea("Query Remarks (Lumen)");
			queryComments.setRows(6);
			queryComments.setColumns(22);
			queryComments.setMaxLength(4000);
			handleTextAreaPopup(queryComments,null);
			tempLayout.addComponent(cmbQueryTo);
			tempLayout.addComponent(queryComments);
			if(screenName.equals("Process Level II") && mainLayout.getComponentIndex(containerVlayout) != -1){
				containerVlayout.removeAllComponents();
				mainLayout.removeComponent(containerVlayout);
			}
		}
		if(buttonName.equals("Close")){
			closeComments = new TextArea("Comments");
			closeComments.setRows(6);
			closeComments.setColumns(22);
			closeComments.setMaxLength(4000);
			handleTextAreaPopup(closeComments,null);
			tempLayout.addComponent(closeComments);
			if(screenName.equals("Process Level II") && mainLayout.getComponentIndex(containerVlayout) != -1){
				containerVlayout.removeAllComponents();
				mainLayout.removeComponent(containerVlayout);
			}
		}
		if(buttonName.equals("Refer to MIS")){
			referToMISTableObj = referToMISTableInstance.get();
//			referToMISTableObj.init("", true, true);
//			referToMISTableObj.setReference(new HashMap<String, Object>());
			referToMISTableObj.init(new MISDTO());
			referToMISTableObj.setVisibleColumns();
			tempLayout.addComponent(referToMISTableObj);
			if(screenName.equals("Process Level II") && mainLayout.getComponentIndex(containerVlayout) != -1){
				containerVlayout.removeAllComponents();
				mainLayout.removeComponent(containerVlayout);
			}
		}	
		
		if(buttonName.equals("Refer to MIS")){
			actionLayout.addComponent(containerHorlayout, "left: 10%; top: 0%;");
		}else{
			actionLayout.addComponent(containerHorlayout, "left: 30%; top: 0%;");
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private void generateDropDownValues(String argButtonName) {
		
		// Process Level I dropdown values
		if(screenName.equals("Process Level I") && argButtonName.equals("Approve")){
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s1 = new SelectValue();
			s1.setValue("Process Lumen Request (Level - II)");
			selectValueList.add(s1);
			SelectValue s2 = new SelectValue();
			s2.setValue("Co-ordinator");
			selectValueList.add(s2);			
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbApproveTo.setContainerDataSource(approveContainer);
			cmbApproveTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbApproveTo.setItemCaptionPropertyId("value");
			if(cmbApproveTo.getValue() != null){
				cmbApproveTo.setDescription(((SelectValue)cmbApproveTo.getValue()).getValue());
				cmbApproveTo.setDescription(((SelectValue)cmbApproveTo.getValue()).getValue());
				cmbApproveTo.setReadOnly(true);
			}
		}
		
		if(screenName.equals("Process Level I") && argButtonName.equals("Reply")){
			int level2Index = 0;
			int coordIndex = 1;
			Integer selectedIndex = null;
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s1 = new SelectValue();
			s1.setValue("Process Lumen Request (Level - II)");
			selectValueList.add(s1);
			SelectValue s2 = new SelectValue();
			s2.setValue("Co-ordinator");
			selectValueList.add(s2);
			
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbReplyTo.setContainerDataSource(approveContainer);
			cmbReplyTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbReplyTo.setItemCaptionPropertyId("value");
			if(getQueryRaisedRole().equals("Level 2")){
				selectedIndex = level2Index;
			}else if(getQueryRaisedRole().equals("Coordinator")){
				selectedIndex = coordIndex;
			}			
			List<SelectValue> defaultStatus = (List<SelectValue>) cmbReplyTo.getContainerDataSource().getItemIds();
			if(selectedIndex != null){
				cmbReplyTo.setValue(defaultStatus.get(selectedIndex));
				cmbReplyTo.setDescription(((SelectValue)cmbReplyTo.getValue()).getValue());
				cmbReplyTo.setReadOnly(true);
			}
		}
		
		if(screenName.equals("Process Level I") && argButtonName.equals("Query")){
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s1 = new SelectValue();
			s1.setValue("Initiator");
			selectValueList.add(s1);
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbQueryTo.setContainerDataSource(approveContainer);
			cmbQueryTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbQueryTo.setItemCaptionPropertyId("value");
			if(cmbQueryTo.getValue() != null){
				cmbQueryTo.setDescription(((SelectValue)cmbQueryTo.getValue()).getValue());
			}
			
			List<SelectValue> defaultStatus = (List<SelectValue>) cmbQueryTo.getContainerDataSource().getItemIds();
			cmbQueryTo.setValue(defaultStatus.get(0));
			cmbQueryTo.setReadOnly(true);
		}
		//Initiator Query Cases dropdown values
		if(screenName.equals("Initiator Query Case") && argButtonName.equals("Reply")){
			int level2Index = 0;
			int level1Index = 1;
			int coordIndex = 2;
			Integer selectedIndex = null;
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s1 = new SelectValue();
			s1.setValue("Process Lumen Request (Level - II)");
			selectValueList.add(s1);
			SelectValue s2 = new SelectValue();
			s2.setValue("Process Lumen Request (Level - I)");
			selectValueList.add(s2);
			SelectValue s3 = new SelectValue();
			s3.setValue("Co-ordinator");
			selectValueList.add(s3);
			
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbReplyTo.setContainerDataSource(approveContainer);
			cmbReplyTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbReplyTo.setItemCaptionPropertyId("value");			
			if(getQueryRaisedRole().equals("Level 1")){
				selectedIndex = level1Index;
			}else if(getQueryRaisedRole().equals("Level 2")){
				selectedIndex = level2Index;
			}else if(getQueryRaisedRole().equals("Coordinator")){
				selectedIndex = coordIndex;
			}
			
			List<SelectValue> defaultStatus = (List<SelectValue>) cmbReplyTo.getContainerDataSource().getItemIds();
			if(selectedIndex != null){
				cmbReplyTo.setValue(defaultStatus.get(selectedIndex));
				cmbReplyTo.setDescription(((SelectValue)cmbReplyTo.getValue()).getValue());
				cmbReplyTo.setReadOnly(true);
			}
		}
		
		//Coordinator Dropdown Values
		if(screenName.equals("Process Coordinator") && argButtonName.equals("Approve")){
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s1 = new SelectValue();
			s1.setValue("Process Lumen Request (Level - II)");
			selectValueList.add(s1);
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbApproveTo.setContainerDataSource(approveContainer);
			cmbApproveTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbApproveTo.setItemCaptionPropertyId("value");
			if(cmbApproveTo.getValue() != null){
				cmbApproveTo.setDescription(((SelectValue)cmbApproveTo.getValue()).getValue());
			}
			
			List<SelectValue> defaultStatus = (List<SelectValue>) cmbApproveTo.getContainerDataSource().getItemIds();
			cmbApproveTo.setValue(defaultStatus.get(0));
			//cmbApproveTo.setReadOnly(true);
		}
		
		if(screenName.equals("Process Coordinator") && argButtonName.equals("Query")){
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s1 = new SelectValue();
			s1.setValue("Initiator");
			selectValueList.add(s1);
			/*SelectValue s2 = new SelectValue();
			s2.setValue("Process Lumen Request (Level - I)");
			selectValueList.add(s2);*/
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbQueryTo.setContainerDataSource(approveContainer);
			cmbQueryTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbQueryTo.setItemCaptionPropertyId("value");
			if(cmbQueryTo.getValue() != null){
				cmbQueryTo.setDescription(((SelectValue)cmbQueryTo.getValue()).getValue());
			}
			
			List<SelectValue> defaultStatus = (List<SelectValue>) cmbQueryTo.getContainerDataSource().getItemIds();
			cmbQueryTo.setValue(defaultStatus.get(0));
			cmbQueryTo.setReadOnly(true);
		}
		
		if(screenName.equals("Process Coordinator") && argButtonName.equals("Reply")){
			int level2Index = 0;
			int level1Index = 1;
			Integer selectedIndex = null;
			
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s1 = new SelectValue();
			s1.setValue("Process Lumen Request (Level - II)");
			selectValueList.add(s1);
			SelectValue s2 = new SelectValue();
			s2.setValue("Process Lumen Request (Level - I)");
			selectValueList.add(s2);
			
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbReplyTo.setContainerDataSource(approveContainer);
			cmbReplyTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbReplyTo.setItemCaptionPropertyId("value");
			if(cmbReplyTo.getValue() != null){
				cmbReplyTo.setDescription(((SelectValue)cmbReplyTo.getValue()).getValue());
			}
			
			if(getQueryRaisedRole().equals("Level 1")){
				selectedIndex = level1Index;
			}else if(getQueryRaisedRole().equals("Level 2")){
				selectedIndex = level2Index;
			}
			
			List<SelectValue> defaultStatus = (List<SelectValue>) cmbReplyTo.getContainerDataSource().getItemIds();
			if(selectedIndex != null){
				cmbReplyTo.setValue(defaultStatus.get(selectedIndex));
				cmbReplyTo.setDescription(((SelectValue)cmbReplyTo.getValue()).getValue());
				cmbReplyTo.setReadOnly(true);
			}
		}
		//Process Level II 
		if(screenName.equals("Process Level II") && argButtonName.equals("Query")){
			int initiatorIndex = 0;
			int level1Index = 1;
			int coordIndex = 2;
			Integer selectedIndex = null;
			
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue s3 = new SelectValue();
			s3.setValue("Initiator");
			selectValueList.add(s3);
			SelectValue s1 = new SelectValue();
			s1.setValue("Process Lumen Request (Level - I)");
			selectValueList.add(s1);
			SelectValue s2 = new SelectValue();
			s2.setValue("Co-ordinator");
			selectValueList.add(s2);			
			BeanItemContainer<SelectValue> approveContainer = new BeanItemContainer<SelectValue>(selectValueList);
			cmbQueryTo.setContainerDataSource(approveContainer);
			cmbQueryTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbQueryTo.setItemCaptionPropertyId("value");
			if(cmbQueryTo.getValue() != null){
				cmbQueryTo.setDescription(((SelectValue)cmbQueryTo.getValue()).getValue());
			}
			
			if(getQueryRaisedRole().equals("Level 1")){
				selectedIndex = level1Index;
			}else if(getQueryRaisedRole().equals("Initiator")){
				selectedIndex = initiatorIndex;
			}else if(getQueryRaisedRole().equals("Coordinator")){
				selectedIndex = coordIndex;
			}
			
			List<SelectValue> defaultStatus = (List<SelectValue>) cmbQueryTo.getContainerDataSource().getItemIds();
			if(selectedIndex != null){
				cmbQueryTo.setValue(defaultStatus.get(selectedIndex));
			}
		}
		
	}

	public void addListeners(){

		approveButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setActionName(event.getButton().getCaption());
				generateFields(event.getButton().getCaption());	
			}
		});
		rejectButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setActionName(event.getButton().getCaption());
				generateFields(event.getButton().getCaption());	
			}
		});

		replyButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setActionName(event.getButton().getCaption());
				generateFields(event.getButton().getCaption());	
			}
		});
		queryButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setActionName(event.getButton().getCaption());
				generateFields(event.getButton().getCaption());
			}
		});
		closeButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setActionName(event.getButton().getCaption());
				generateFields(event.getButton().getCaption());	
			}
		});
		referToMIS.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setActionName(event.getButton().getCaption());
				generateFields(event.getButton().getCaption());	
			}
		});		
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public ComboBox getCmbApproveTo() {
		return cmbApproveTo;
	}

	public void setCmbApproveTo(ComboBox cmbApproveTo) {
		this.cmbApproveTo = cmbApproveTo;
	}

	public TextArea getApproveComments() {
		return approveComments;
	}

	public void setApproveComments(TextArea approveComments) {
		this.approveComments = approveComments;
	}

	public TextArea getRejectComments() {
		return rejectComments;
	}

	public void setRejectComments(TextArea rejectComments) {
		this.rejectComments = rejectComments;
	}

	public ComboBox getCmbReplyTo() {
		return cmbReplyTo;
	}

	public void setCmbReplyTo(ComboBox cmbReplyTo) {
		this.cmbReplyTo = cmbReplyTo;
	}

	public TextArea getReplyComments() {
		return replyComments;
	}

	public void setReplyComments(TextArea replyComments) {
		this.replyComments = replyComments;
	}

	public ComboBox getCmbQueryTo() {
		return cmbQueryTo;
	}

	public void setCmbQueryTo(ComboBox cmbQueryTo) {
		this.cmbQueryTo = cmbQueryTo;
	}

	public TextArea getQueryComments() {
		return queryComments;
	}

	public void setQueryComments(TextArea queryComments) {
		this.queryComments = queryComments;
	}

	public TextArea getCloseComments() {
		return closeComments;
	}

	public void setCloseComments(TextArea closeComments) {
		this.closeComments = closeComments;
	}

	public ReferToMISTable getReferToMISTableObj() {
		return referToMISTableObj;
	}

	public void setReferToMISTableObj(ReferToMISTable referToMISTableObj) {
		this.referToMISTableObj = referToMISTableObj;
	}
	
	public OptionGroup getGenerateLetterOption() {
		return generateLetterOption;
	}

	public void setGenerateLetterOption(OptionGroup generateLetterOption) {
		this.generateLetterOption = generateLetterOption;
	}

	@SuppressWarnings("unused")
	public  void handleTextAreaPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
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
	
	private ShortcutListener getShortCutListenerForRemarks(final TextArea txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				txtArea.setStyleName("Boldstyle"); 
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(4000);
				txtArea.setReadOnly(false);
				txtArea.setRows(25);

				txtArea.addValueChangeListener(new ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Comments";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}
	
	protected Collection<Boolean> getRadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}
}
