package com.shaic.claim.preauth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class HRMListenerTable extends ViewComponent{

private Map<HRMTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<HRMTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<HRMTableDTO> container = new BeanItemContainer<HRMTableDTO>(HRMTableDTO.class);
	
	private Table table;
	
	private HRMTableDTO bean;
	
	private Map<String, Object> referenceData;
	private VerticalLayout layout;
	private VerticalLayout vLayout ;
	private String fileName ;
	
	private List<HRMTableDTO> hrmDtoList = null;
	
	public void init() {		
		layout = new VerticalLayout();
		vLayout = new VerticalLayout();
		initTable();
		table.setWidth("100%");
		table.setHeight("350px");
		table.setPageLength(table.getItemIds().size());
		
	/*	BeanItemContainer<HRMTableDTO> container = new BeanItemContainer<HRMTableDTO>(HRMTableDTO.class);
		
		if(null != hrmDtoList && !hrmDtoList.isEmpty()){
			container.addAll(hrmDtoList);
		}
		table.setContainerDataSource(container);*/
		layout.addComponent(table);	

		setCompositionRoot(layout);
	}
	
	/*public void viewInt(List<HRMTableDTO> hrmDtoList )
	{
		this.hrmDtoList = hrmDtoList;
	}*/
	
	public void initTable(){

		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());	
	
		
			table.setVisibleColumns(new Object[] { "anhOrNanh", "diagnosis", "surgicalProcedure", "claimedAmt","packageAmt", "requestType" , "docRemarks" ,"assigneeDateAndTime", "hrmReplyRemarks", 
					"replyDateAndTime" , "docUserId" , "docName" , "docDeskNumber"});

			table.setColumnHeader("anhOrNanh", "ANH/NANH");
			table.setColumnHeader("diagnosis", "Diagnosis");
			table.setColumnHeader("surgicalProcedure", "Surgical </br> Procedure");
			table.setColumnHeader("claimedAmt", "Claimed</br>Amount");
			table.setColumnHeader("packageAmt", "Package </br> Amount");
			table.setColumnHeader("requestType", "Request Type");
			table.setColumnHeader("docRemarks", "Doctor</br> Remarks");
			table.setColumnHeader("assigneeDateAndTime","Assignee </br> Date & Time");
			table.setColumnHeader("hrmReplyRemarks", "HRM </br> Reply Remarks");
			table.setColumnHeader("replyDateAndTime", "Reply </br> Date & Time");
			table.setColumnHeader("docUserId", "Dr.User Id");
			table.setColumnHeader("docName", "Dr.Name");
			table.setColumnHeader("docDeskNumber", "Dr.Desk Number");
				
		table.setEditable(true);
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		table.setFooterVisible(true);	
	}
	
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			HRMTableDTO entryDTO = (HRMTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
			
			
			if ("anhOrNanh".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(50);		
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z , .]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("anhOrNanh", field);
				if(null != entryDTO.getAnhOrNanh())
				{
				field.setDescription(entryDTO.getAnhOrNanh());
				}
				return field;
			}
			
			else if ("diagnosis".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("diagnosis", field);
				if(null != entryDTO.getDiagnosis())
				{
				field.setDescription(entryDTO.getDiagnosis());
				}

				return field;
			}
			else if ("surgicalProcedure".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getSurgicalProcedure())
				{
				field.setDescription(entryDTO.getSurgicalProcedure());
				}
				tableRow.put("surgicalProcedure", field);

				return field;
			}
			else if ("claimedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getClaimedAmt())
				{
				field.setDescription(String.valueOf(entryDTO.getClaimedAmt()));
				}
				tableRow.put("claimedAmt", field);

				return field;
			}
			else if ("packageAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getPackageAmt())
				{
				field.setDescription(String.valueOf(entryDTO.getPackageAmt()));
				}
				tableRow.put("packageAmt", field);

				return field;
			}
			else if ("requestType".equals(propertyId)) {
				GComboBox comboBox = new GComboBox();
				BeanItemContainer<SelectValue> requestTypeContainer = (BeanItemContainer<SelectValue>) referenceData
						.get("requestTypeContainer");
				comboBox.setContainerDataSource(requestTypeContainer);
				comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				comboBox.setItemCaptionPropertyId("value");

				//box.focus();
				comboBox.setWidth("150px");
				tableRow.put("requestType", comboBox);
				comboBox.setData(entryDTO);
				return comboBox;
			}	
			else if ("docRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(4000);
				field.setData(entryDTO);			
//				CSValidator validator = new CSValidator();
//				validator.extend(field);
				handleEnter(field, null);
				/*validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);*/
				if(null != entryDTO.getDocRemarks())
				{
					
				}
				tableRow.put("docRemarks", field);

				return field;
			}			
			else if ("assigneeDateAndTime".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getAssigneeDateAndTime())
				{
				field.setDescription(String.valueOf(entryDTO.getAssigneeDateAndTime()));
				}
				tableRow.put("assigneeDateAndTime", field);

				return field;
			}			
			else if ("hrmReplyRemarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getHrmReplyRemarks())
				{
				field.setDescription(entryDTO.getHrmReplyRemarks());
				}
				tableRow.put("hrmReplyRemarks", field);

				return field;
			}			
			else if ("replyDateAndTime".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getReplyDateAndTime())
				{
				field.setDescription(String.valueOf(entryDTO.getReplyDateAndTime()));
				}
				tableRow.put("replyDateAndTime", field);

				return field;
			}	
			
			else if ("docUserId".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getDocUserId())
				{
				field.setDescription(entryDTO.getDocUserId());
				}
				tableRow.put("docUserId", field);

				return field;
			}	
			
			else if ("docName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getDocName())
				{
				field.setDescription(entryDTO.getDocName());
				}
				tableRow.put("docName", field);

				return field;
			}	
			
			else if ("docDeskNumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setMaxLength(6);
				field.setData(entryDTO);			
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				if(null != entryDTO.getAnhOrNanh())
				{
				field.setDescription(entryDTO.getDocDeskNumber());
				}
				tableRow.put("docDeskNumber", field);

				return field;
			}	
			
			
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}
	
	public  void handleEnter(TextField searchField, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcut", ShortcutAction.KeyCode.F8, null) {
	      private static final long serialVersionUID = -2267576464623389044L;
	      @Override
	      public void handleAction( Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    
	   // handleShortcut(searchField, enterShortCut);
	    handleShortcut(searchField, getShortCutListener(searchField));
	  }
	
	public  void handleShortcut(final TextField textField, final ShortcutListener shortcutListener) {
		//textField.addFocusListener(F);
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				//shortcutListener = getShortCutListener(textField);
				//textField.addShortcutListener(getShortCutListener(textField));
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		
	   textField.addBlurListener(new BlurListener() {
		
		@Override
		public void blur(BlurEvent event) {
			
			/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
			for (Object object : listeners) {
				textField.removeListener(ShortcutListener.class, object);
			}*/
			
			textField.removeShortcutListener(shortcutListener);
			/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
			for (Object object : listeners) {
				textField.removeListener(ShortcutListener.class, object);
			}*/
			
		}
	});
	  }
	
	private ShortcutListener getShortCutListener(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				HRMTableDTO hrmDTO = (HRMTableDTO) txtFld.getData();
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(hrmDTO);
				 TextField txtItemValue = (TextField) hashMap.get("docRemarks");
				
				 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
				 
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				
				TextArea txtArea = new TextArea();
				txtArea.setNullRepresentation("");
				txtArea.setMaxLength(4000);
				
				txtArea.setValue(hrmDTO.getDocRemarks());
				//txtArea.setValue(txtFld.getValue());
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
						txtFld.setDescription(txt.getValue());
						// TODO Auto-generated method stub
						
					}
				});
				
				hrmDTO.setDocRemarks(txtArea.getValue());
			//	txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
				txtFld.setDescription(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
				
				
				
				final Window dialog = new Window();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				//dialog.show(getUI().getCurrent(), null, true);
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((null != fileName && fileName.endsWith(".PDF")) || (null != fileName && fileName.endsWith(".pdf")))) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				
				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}

	 public List<HRMTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<HRMTableDTO> itemIds = (List<HRMTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 public void addBeanToList(HRMTableDTO hrmTableDto) {
	    	
		 container.addItem(hrmTableDto);


	    }
	 
	 public void setReferenceData(Map<String, Object> referenceData) {
			this.referenceData = referenceData;
		}
	 
	
	
}
