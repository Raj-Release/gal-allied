package com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PAInvestigationReviewRemarksTable extends ViewComponent{

	@EJB
	private MasterService masterService;
	
	private Map<AssignedInvestigatiorDetails, HashMap<String, AbstractField<?>>> tableItem = new HashMap<AssignedInvestigatiorDetails, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<AssignedInvestigatiorDetails> data = new BeanItemContainer<AssignedInvestigatiorDetails>(AssignedInvestigatiorDetails.class);

	private Table table;
	private AssignedInvestigatiorDetails dto;	
	Map<String, AbstractField<?>> tableRow = null;
	AssignedInvestigatiorDetails invsDTO = null;
	public void init() {

		dto = new AssignedInvestigatiorDetails();
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		initTable(layout);
		table.setWidth("1000px");
		table.setHeight("170px");
		table.setPageLength(table.getItemIds().size());
		layout.addComponent(table);
		setSizeFull();
		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		table = new Table("", data);
		data.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());		
		generateColumn();
		table.setVisibleColumns(new Object[] { "rollNo", "chkSelect", "investigatorName", "reviewRemarkskey", "rvoFindingsKey" ,"rvoReasonKey"});
		table.setColumnHeader("rollNo", "S.</br>No");
		table.setColumnHeader("chkSelect", " ");
		table.setColumnHeader("investigatorName", "Investigator Name");
		table.setColumnHeader("reviewRemarkskey", "Investigator/ RVO grade");
		table.setColumnHeader("rvoFindingsKey", "RVO findings- Claim decision");
		table.setColumnHeader("rvoReasonKey", "Reasons for not accepting RVO findings");
		table.setColumnWidth("rollNo", 35);
		table.setEditable(true);		
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@SuppressWarnings("deprecation")
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			invsDTO = (AssignedInvestigatiorDetails) itemId;

			if (tableItem.get(invsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(invsDTO, new HashMap<String, AbstractField<?>>());
			} 

			tableRow = tableItem.get(invsDTO);
			
			if("investigatorName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(invsDTO);
				tableRow.put("investigatorName", field);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);	
				return field;
			} 
			else  if("reviewRemarkskey".equals(propertyId)) {
				String reviewRemarks = invsDTO.getReviewRemarks();
				if(reviewRemarks != null && !reviewRemarks.isEmpty()){
					MastersValue mastersValue = masterService.getMaster(reviewRemarks.toLowerCase() , ReferenceTable.MASTER_TYPE_CODE_RVO_GRADE.toLowerCase());
					if(mastersValue != null && mastersValue.getKey() != null){
						SelectValue selectValue = new SelectValue();
						selectValue.setId(mastersValue.getKey());
						selectValue.setValue(mastersValue.getValue());
						invsDTO.setReviewRemarkskey(selectValue);
					}
				}
				GComboBox box = new GComboBox();
				//addRVOGradeValues(box);
				tableRow.put("reviewRemarkskey", box);
				final ComboBox procedureValue = (ComboBox) tableRow.get("reviewRemarkskey");
				box.setData(invsDTO);
				box.setEnabled(false);
				addProcedureNameListener(box, procedureValue);
				return box;
			}
			else if("rvoFindingsKey".equals(propertyId)) {
				MastersValue masterValue = invsDTO.getRvoFindings();
				if(masterValue != null && masterValue.getKey() != null){
					SelectValue selectValue = masterService.getMasterValueForNatureCause(masterValue.getKey());
					if(selectValue != null){
						invsDTO.setRvoFindingsKey(selectValue);
					}
				}
				GComboBox box = new GComboBox();
				//addRVOFindingValues(box);
				tableRow.put("rvoFindingsKey", box);
				final ComboBox procedureValue = (ComboBox) tableRow.get("rvoFindingsKey");
				box.setData(invsDTO);
				box.setEnabled(false);
				clickEventForFindings(box, procedureValue);
				return box;
			}
			else if("rvoReasonKey".equals(propertyId)) {
				MastersValue masterValue = invsDTO.getRvoReason();
				if(masterValue != null && masterValue.getKey() != null){
					SelectValue selectValue = masterService.getMasterValueForNatureCause(masterValue.getKey());
					if(selectValue != null){
						invsDTO.setRvoReasonKey(selectValue);
					}
				}
				GComboBox box = new GComboBox();
				//addRVOReasonValues(box);
				tableRow.put("rvoReasonKey", box);
				final ComboBox procedureValue = (ComboBox) tableRow.get("rvoReasonKey");
				box.setData(invsDTO);
				box.setEnabled(false);
				addProcedureNameListener(box, procedureValue);
				return box;
			}

			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				return field;
			}
		}

		
	}
	 public void addBeanToList(AssignedInvestigatiorDetails benefitsdto) {
		 data.addItem(benefitsdto);

	    }
	 
	 @SuppressWarnings("unchecked")
		public List<AssignedInvestigatiorDetails> getValues() {
			List<AssignedInvestigatiorDetails> itemIds = (List<AssignedInvestigatiorDetails>) this.table.getItemIds() ;
	    	return itemIds;
		}
	 
		public void setTableList(final List<AssignedInvestigatiorDetails> list) {
			table.removeAllItems();
			int i = 1;
			for (final AssignedInvestigatiorDetails bean : list) {
				bean.setRollNo(i);
				table.addItem(bean);
				i++;
			}
		}

	private void generateColumn(){
		table.removeGeneratedColumn("chkSelect");
		table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final AssignedInvestigatiorDetails tableDTO = (AssignedInvestigatiorDetails) itemId;
				  CheckBox chkBox = new CheckBox();
				  
					if(null != tableDTO && null != tableDTO.getReportReviewed() && tableDTO.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG))
					{
						chkBox.setValue(true);
						chkBox.setReadOnly(true);
					}else{
						chkBox.setValue(false);
						chkBox.setReadOnly(true);
					}
				return chkBox;
			}
		});
	}
	
	@SuppressWarnings("unused")
	public  void handleTextFieldPopup(TextArea searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {

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
				final TextArea txtField = new TextArea();
				txtField.setValue(txtFld.getValue());
				txtField.setNullRepresentation("");
				txtField.setSizeFull();
				txtField.setWidth("100%");
				txtField.setMaxLength(1000);
				txtField.setReadOnly(false);
				txtField.setHeight("400px");
//				txtField.setRows(25);

				txtField.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtField);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Investigation Review Remarks";

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
					dialog.setPositionX(250);
					dialog.setPositionY(100);
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
	public void addRVOFindingValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> rvoFindingsList = masterService.getTypeContainer(ReferenceTable.MASTER_TYPE_CODE_RVO_FINDINGS);
		comboBox.setContainerDataSource(rvoFindingsList);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}	
	
	public void addRVOReasonValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> rvoReasonList = masterService.getTypeContainer(ReferenceTable.MASTER_TYPE_CODE_RVO_REASON);
		comboBox.setContainerDataSource(rvoReasonList);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}
	
	public void addRVOGradeValues(ComboBox comboBox) {
		BeanItemContainer<SelectValue> rvoGradeList = masterService.getTypeContainer(ReferenceTable.MASTER_TYPE_CODE_RVO_GRADE);
		comboBox.setContainerDataSource(rvoGradeList);
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
	}
	
	private void addProcedureNameListener(ComboBox speclbox,ComboBox procBox){
		if(speclbox != null){
			speclbox.addListener(new Listener(){

				@Override
				public void componentEvent(Event event) {

					ComboBox component = (ComboBox) event.getComponent();
					AssignedInvestigatiorDetails specialityDTO = (AssignedInvestigatiorDetails) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(specialityDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("rvoFindingsKey");
					if (specialityDTO != null) {
						/*if (specialityDTO.getSpecialityType() != null && specialityDTO.getSpecialityType().getValue() != null) {
							if (comboBox != null) {
								//addProcedureValues(specialityDTO.getSpecialityType().getId(), comboBox,specialityDTO.getProcedure());
							}
						}*/
					}
				
				}
				
			});
		}
		
	}
	
	private void clickEventForFindings(ComboBox speclbox,ComboBox procBox){
		if(speclbox != null){
			speclbox.addListener(new Listener(){

				@Override
				public void componentEvent(Event event) {

					ComboBox component = (ComboBox) event.getComponent();
					AssignedInvestigatiorDetails assignedInvestigatiorDetails = (AssignedInvestigatiorDetails) component
							.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem
							.get(assignedInvestigatiorDetails);
					ComboBox comboBox = (ComboBox) hashMap.get("rvoFindingsKey");
					ComboBox reasoncomboBox = (ComboBox) hashMap.get("rvoReasonKey");
					if(comboBox != null){
						SelectValue selectValue = (SelectValue) comboBox.getValue();
						if (comboBox != null && comboBox.getValue() == null || reasoncomboBox != null && selectValue != null && selectValue.getId() != null && (selectValue.getId().equals(ReferenceTable.RVO_FINDINGS_ACCEPTED_CLAIM_REJECTED_KEY) || selectValue.getId().equals(ReferenceTable.RVO_FINDINGS_ACCEPTED_CLAIM_APPROVED_KEY))) {
							reasoncomboBox.setRequired(false);
							reasoncomboBox.setEnabled(false);
							reasoncomboBox.clear();
						}
						else{
							if(reasoncomboBox != null){
								reasoncomboBox.setRequired(true);
								reasoncomboBox.setEnabled(true);
							}
						}
					}
				}
				
			});
		}
		
	}
}
