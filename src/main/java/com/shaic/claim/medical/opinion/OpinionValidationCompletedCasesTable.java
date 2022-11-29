package com.shaic.claim.medical.opinion;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.ReferenceTable;
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
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OpinionValidationCompletedCasesTable extends GBaseTable<OpinionValidationTableDTO> {

	private static final long serialVersionUID = 1L;

	private Button btnEdit;
	
	ComboBox cmbAction;	
	
	private TextField txtRemarks;	
	
	public HashMap<Long, Boolean> compMap = null;
	
	public HashMap<Long, String> remarksMap = null;

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<OpinionValidationTableDTO>(OpinionValidationTableDTO.class));
		Object[] BEFORE_EDIT_COLUMNS = new Object[] {
				"serialNumber",
				"intimationNumber",
				"createdBy",
				"createdDate",
				"updatedRemarks",
				"opinionStatusValue",
				"approveRejectRemarks",
				"modifiedDateTime",
				"edit"
		};
		generatecolumns();
		table.setVisibleColumns(BEFORE_EDIT_COLUMNS);
		table.setHeight("300px");
	}
	
	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7 ) {
			table.setPageLength(7);
		}
	}

	@Override
	public void tableSelectHandler(OpinionValidationTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "opinion-validation-completed-";
	}
	
	private void generatecolumns() {
		
		compMap =  new HashMap<Long, Boolean>();
		remarksMap = new HashMap<Long,String>();
		
		table.removeGeneratedColumn("edit");
		table.addGeneratedColumn("edit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)itemId;
				btnEdit = new Button("Edit");
				btnEdit.setData(tableDTO);
				Date validatedDate = tableDTO.getModifiedDateTime();
				Long status = tableDTO.getOpinionStatus();
				Boolean result = isOpinionEditOptionAvailable(validatedDate, status);
				if (result) {
					btnEdit.setVisible(true);
				} else {
					btnEdit.setVisible(false);
				}
				
				addBtnEditListener(btnEdit);
				
				return btnEdit;
			}

			private void addBtnEditListener(final Button editBtn) {
				editBtn
				.addClickListener(new Button.ClickListener() {

					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) editBtn.getData();
						
						if (tableDTO != null) {
							if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_APPROVE)) {
								compMap.put(tableDTO.getValidationkey(), true);
								remarksMap.put(tableDTO.getValidationkey(), tableDTO.getApproveRejectRemarks());
							} else if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_REJECT)) {
								compMap.put(tableDTO.getValidationkey(), false);
								remarksMap.put(tableDTO.getValidationkey(), tableDTO.getApproveRejectRemarks());
							}
						}
							
						TextField txtRemarksField = findComponentById(tableDTO.getValidationkey());
						if (txtRemarksField != null) {
							if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_APPROVE)) {
								txtRemarksField.setRequired(false);
								txtRemarksField.setRequiredError("");
							} else if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_REJECT)) {
								txtRemarksField.setRequired(true);
								txtRemarksField.setValidationVisible(true);
								txtRemarksField.setRequiredError("Enter remarks");
							} 
							txtRemarksField.setEnabled(true);
						}
						
						ComboBox editCombo = findComboComponentById(tableDTO.getValidationkey());
						if (editCombo != null) {
							editCombo.setEnabled(true);
						}
					}
				});
				
			}
		});	
		
		table.removeGeneratedColumn("opinionStatusValue");
		table.addGeneratedColumn("opinionStatusValue", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)itemId;
				Date validatedDate = tableDTO.getModifiedDateTime();
				Long status = tableDTO.getOpinionStatus();
				Boolean result = isOpinionEditOptionAvailable(validatedDate, status);
				if (result) {
					cmbAction = new ComboBox();
					cmbAction.setWidth("160px");
					cmbAction.setHeight("-1px");
					cmbAction.setData(tableDTO);
					cmbAction.setNullSelectionAllowed(false);
					
					SelectValue agree = new SelectValue();
					agree.setId(SHAConstants.OPINION_STATUS_APPROVE);
					agree.setValue("Agree");
					
					SelectValue disagree = new SelectValue();
					disagree.setId(SHAConstants.OPINION_STATUS_REJECT);
					disagree.setValue("Disagree");
						
					BeanItemContainer<SelectValue> sourceData = new BeanItemContainer<SelectValue>(SelectValue.class);
					sourceData.addBean(agree);
					sourceData.addBean(disagree);
						
					cmbAction.setContainerDataSource(sourceData);
					cmbAction.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbAction.setItemCaptionPropertyId("value");
					
					cmbAction.setEnabled(false);
					
					if (tableDTO.getOpinionStatus() != null ) {
						if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_APPROVE)) {
							cmbAction.setValue(sourceData.getIdByIndex(0));
						} else if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_REJECT)) {
							cmbAction.setValue(sourceData.getIdByIndex(1));
						}
					}
					
					addListener(cmbAction, source);
					return cmbAction;
				} else {
					Label lblAction = new Label();
					if (tableDTO.getOpinionStatus() != null ) {
						if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_APPROVE)) {
							lblAction.setValue("AGREED");
						} else if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_REJECT)) {
							lblAction.setValue("DISAGREED");
						} else  {
							lblAction.setValue(tableDTO.getOpinionStatusValue());
						}
					} 
					return lblAction;
				}
			}
		});
		
		table.removeGeneratedColumn("approveRejectRemarks");
		table.addGeneratedColumn("approveRejectRemarks", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)itemId;
				txtRemarks = new TextField();
				txtRemarks.setData(tableDTO);
				txtRemarks.setEnabled(false);
				txtRemarks.setValue(tableDTO.getApproveRejectRemarks() != null ? tableDTO.getApproveRejectRemarks()  : "");
				txtRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				
				if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_APPROVE)) {
					txtRemarks.setRequired(false);
					txtRemarks.setRequiredError("");
				} else if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_REJECT)) {
					txtRemarks.setRequired(true);
					txtRemarks.setValidationVisible(true);
					txtRemarks.setRequiredError("Enter remarks");
				} 
				
				handleTextFieldPopup(txtRemarks,null);
				addTextboxListener(txtRemarks);
				return txtRemarks;
			}
		});
		
	}
	
	private Boolean isOpinionEditOptionAvailable(Date validatedDate, Long status) {
		Boolean result = Boolean.FALSE;
		if (validatedDate != null && !status.equals(ReferenceTable.OPINION_VALIDATION_ELAPSED_KEY)) {
			long currentDate = new Date().getTime();
	        long diff = currentDate - validatedDate.getTime();        
	        long diffHours = diff / (60 * 60 * 1000);      
	        if (diffHours < 24) {            
	           result = Boolean.TRUE;
	        } 
		}
	    return result;
	}
	
	@SuppressWarnings("unused")
	public  void handleTextFieldPopup(TextField searchField, final  Listener listener) {

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

	public  void handleShortcutForRedraft(final TextField textField, final ShortcutListener shortcutListener) {
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
	
	private ShortcutListener getShortCutListenerForRemarks(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks(Agree/Disagree)",KeyCodes.KEY_F8,null) {

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
				txtArea.setNullRepresentation("");
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setMaxLength(1000);
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

				String strCaption = "Remarks(Agree/Disagree)";

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
	
	public TextField findComponentById(Long id) {
		Iterator<Component> iterator = table.iterator();
		Component component = null;
		while (iterator.hasNext()) {
			component = iterator.next();
			if (component instanceof TextField) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)((AbstractComponent) component).getData();
				if (tableDTO.getValidationkey().equals(id)) {
					return (TextField) component;
				}
			}
		}
		return null;
	}
	
	
	public ComboBox findComboComponentById(Long id) {
		Iterator<Component> iterator = table.iterator();
		Component component = null;
		while (iterator.hasNext()) {
			component = iterator.next();
			if (component instanceof ComboBox) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)((AbstractComponent) component).getData();
				if (tableDTO.getValidationkey().equals(id)) {
					return (ComboBox) component;
				}
			}
		}
		return null;
	}
	
	private void addListener(final ComboBox comboBox, final Table source) { 	
		comboBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) comboBox.getData();
				if (null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					String result = event.getProperty().getValue().toString();
					if (result.equalsIgnoreCase("Agree")) {
						compMap.put(tableDTO.getValidationkey() , true);
						TextField txtRemarksField = findComponentById(tableDTO.getValidationkey());
						if (txtRemarksField != null) {
							txtRemarksField.setEnabled(true);
							txtRemarksField.setRequired(false);
							txtRemarksField.setRequiredError("");
						}
					} else if (result.equalsIgnoreCase("Disagree")) {
						compMap.put(tableDTO.getValidationkey() , false);
						TextField txtRemarksField = findComponentById(tableDTO.getValidationkey());
						if (txtRemarksField != null) {
							txtRemarksField.setRequired(true);
							txtRemarksField.setValidationVisible(true);
							txtRemarksField.setRequiredError("Enter remarks");
							txtRemarksField.setEnabled(true);
						}
					} 					
				} 
			}

				
		});
		
	}
	
	public HashMap<Long, Boolean> getOpinionStatusValue() {
		return compMap;
	}
	
	public HashMap<Long, String> getOpinionStatusRemarks() {
		return remarksMap;
	}
	
	public HashMap<Long, Boolean> cancelOpinionStatusValue() {
		compMap.clear();
		remarksMap.clear();
		return compMap;
	}
	
	private void addTextboxListener(final TextField textRemarks) { 	
		textRemarks
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) textRemarks.getData();
				if (null != event && null != event.getProperty() && null != event.getProperty().getValue() && StringUtils.isNotBlank( event.getProperty().getValue().toString()))
				{
					String remarks = event.getProperty().getValue().toString();	
					remarksMap.put(tableDTO.getValidationkey(), remarks);
				} else {
					if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_REJECT)) {
						textRemarks.setRequired(true);
						textRemarks.setValidationVisible(true);
						textRemarks.setRequiredError("Enter remarks");
						remarksMap.remove(tableDTO.getValidationkey());
					} else if (tableDTO.getOpinionStatus().equals(SHAConstants.OPINION_STATUS_APPROVE)) {
						textRemarks.setRequired(false);
						textRemarks.setRequiredError("");
						remarksMap.put(tableDTO.getValidationkey(), null);
					}
				}
			}		
		});
		
	}

}
