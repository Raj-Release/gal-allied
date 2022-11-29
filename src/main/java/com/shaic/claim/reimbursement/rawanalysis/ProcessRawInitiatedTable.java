package com.shaic.claim.reimbursement.rawanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
















import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewEarlierRodDetails.EsclateToRawTableDTO;
import com.vaadin.v7.data.Container;
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
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ProcessRawInitiatedTable extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<RawInitiatedRequestDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<RawInitiatedRequestDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<RawInitiatedRequestDTO> data = new BeanItemContainer<RawInitiatedRequestDTO>(RawInitiatedRequestDTO.class);
	
	private Map<String, Object> referenceData;
	
	private Table table;
	
	private PreauthDTO bean;
	
	private List<String> errorMessages;
	
	private VerticalLayout layout;
	
	private BeanItemContainer<SelectValue> raws;
	
//	public Object[] VISIBLE_COLUMNS = new Object[] {"category","subCategory","remarksForEscalation","initiatedBy","intiatedDate","stageValue","resolutionfromRaw","remarksfromRaw"};
	public Object[] VISIBLE_COLUMNS = new Object[] {"category","subCategory","initiatedBy","intiatedDate","stageValue","resolutionfromRaw","remarksfromRaw"};	
	public void init(PreauthDTO bean){

		this.bean = bean;
		this.errorMessages = new ArrayList<String>();
		
		layout = new VerticalLayout();
		
		initTable(layout);
		//table.setWidth("100%");
		table.setHeight("160px");
		table.setPageLength(table.getItemIds().size());
		
		layout.addComponent(table);

		setCompositionRoot(layout);
	
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	void initTable(VerticalLayout layout){
		table = new Table("Requested Initiated to RAW",data);
		data.removeAllItems();
		table.setPageLength(table.getItemIds().size());
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.addStyleName("generateColumnTable");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("subCategory", "SubCategory");
//		table.setColumnHeader("remarksForEscalation", "Remarks for Escalation to RAW");
		table.setColumnHeader("initiatedBy", "Initiated By");
		table.setColumnHeader("intiatedDate", "Intiated Date & Time");
		table.setColumnHeader("stageValue", "Claim Stage");
		table.setColumnHeader("resolutionfromRaw", "Resolution from RAW Team");
		table.setColumnHeader("remarksfromRaw", "Remarks from RAW Team");
		table.setEditable(true);
		table.setWidth("100%");
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			
			RawInitiatedRequestDTO rawtableDTO = (RawInitiatedRequestDTO) itemId;
			
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(rawtableDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(rawtableDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(rawtableDTO);
			}
			
			if("category".equals(propertyId)){
				GComboBox box = new GComboBox();
				tableRow.put("category", box);
				box.setData(rawtableDTO);
				box.setReadOnly(true);
				
				return box;
			} else if("subCategory".equals(propertyId)){

				GComboBox box = new GComboBox();
				tableRow.put("subCategory", box);
				box.setData(rawtableDTO);
				box.setReadOnly(true);
				
				return box;
			
			} /*else if("remarksForEscalation".equals(propertyId)){

				TextField field = new TextField();
				field.setNullRepresentation("");				
				field.setData(rawtableDTO);								
				field.setEnabled(true);
				field.setReadOnly(true);				
				tableRow.put("remarksForEscalation", field);
				field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				handleShortCutForEscalationToRaw(field, null);
				return field;
			
			}*/ else if("initiatedBy".equals(propertyId)){

				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setData(rawtableDTO);
				tableRow.put("initiatedBy", field);
			
				return field;
			
			} else if("intiatedDate".equals(propertyId)){
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setData(rawtableDTO);
				field.setReadOnly(true);
				tableRow.put("intiatedDate", field);
			
				return field;
			} else if("stageValue".equals(propertyId)){
				TextField field = new TextField();
				field.setReadOnly(true);
				field.setData(rawtableDTO);
				tableRow.put("stageValue", field);
	
				return field;
			} else if("resolutionfromRaw".equals(propertyId)){
				GComboBox box = new GComboBox();
				tableRow.put("resolutionfromRaw", box);
				box.setData(rawtableDTO);
				box.setNullSelectionAllowed(false);
				addResolutionFromRaw(box);
				return box;
			} else if("remarksfromRaw".equals(propertyId)){
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setMaxLength(1000);
				field.setData(rawtableDTO);				
				field.setEnabled(true);
				field.setReadOnly(false);				
				tableRow.put("resolutionfromRaw", field);
				field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				addDetailPopupForEsclateRemarks(field, null);
				return field;
			} else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
			
		}
		
	}
	
	private void addResolutionFromRaw(ComboBox box){
		fireViewEvent(ProcessRawRequestPresenter.GET_RESOLUTION_RAW,box);
		box.setContainerDataSource(raws);
		box.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		box.setItemCaptionPropertyId("value");
	}
	
	public void addBeanToList(RawInitiatedRequestDTO initiateRequestDto){
		data.addItem(initiateRequestDto);
		
	}
	
	public void setTableList(final List<RawInitiatedRequestDTO> intiatedDtls){
		table.removeAllItems();
		for ( final RawInitiatedRequestDTO rawInitiatedRequestDTO : intiatedDtls) {
			table.addItem(rawInitiatedRequestDTO);
		}
	}
	
	public void setRawResolutions(BeanItemContainer<SelectValue> resolutionRaws) {
		raws = resolutionRaws;
	}
	
	public List<RawInitiatedRequestDTO> getValues(){
		List<RawInitiatedRequestDTO> listvalue = (List<RawInitiatedRequestDTO>) table.getItemIds();
		return listvalue;
	}
	private void addDetailPopupForEsclateRemarks(TextField txtFld, final  Listener listener) {
		  ShortcutListener enterShortCut = new ShortcutListener(
			        "Description", ShortcutAction.KeyCode.F8, null) {
				
			      private static final long serialVersionUID = 1L;
			      @Override
			      public void handleAction(Object sender, Object target) {
			        ((ShortcutListener) listener).handleAction(sender, target);
			      }
			    };
			    handleShortcutForTriggerRemarks(txtFld, getShortCutListenerForTriggerRemarks(txtFld));
			    
			  }
	public  void handleShortcutForTriggerRemarks(final TextField textField, final ShortcutListener shortcutListener) {
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
	
	
	private ShortcutListener getShortCutListenerForTriggerRemarks(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				RawInitiatedRequestDTO  searchPedDto = (RawInitiatedRequestDTO) txtFld.getData();
				VerticalLayout vLayout =  new VerticalLayout();
				
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				TextArea txtArea = new TextArea();
				txtArea.setMaxLength(1000);
				txtArea.setNullRepresentation("");				
				txtArea.setValue(txtFld.getValue());
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
					}
				});
				
				txtArea.setSizeFull();
				txtArea.setWidth("100%");
				txtArea.setRows(25);
				searchPedDto.setRemarksfromRaw(txtArea.getValue());
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				dialog.setCaption("Remarks from RAW Team");
				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(false);
				
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.setDraggable(true);
				
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
	
	public  void handleShortCutForEscalationToRaw(TextField searchField, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcutForEscalateRaw", ShortcutAction.KeyCode.F7, null) {
	      private static final long serialVersionUID = -2267576464623389045L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	   // handleShortcutForMedical(searchField, enterShortCut);
	    handleShortcutForFAReasons(searchField, getShortCutListenerForEscalateRaw(searchField));
	    
	  }
	
	public  void handleShortcutForFAReasons(final TextField textField, final ShortcutListener shortcutListener) {
		//textField.addFocusListener(F);
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				//textField.addShortcutListener(getShortCutListenerForMedicalReason(textField));
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		
	   textField.addBlurListener(new BlurListener() {
		
		@Override
		public void blur(BlurEvent event) {/*
			Collection<?> listeners = textField.getListeners(ShortcutListener.class);
			for (Object object : listeners) {
				textField.removeListener(ShortcutListener.class, object);
			}
			
		*/
		textField.removeShortcutListener(shortcutListener);	
		}
	});
	  }
	
	private ShortcutListener getShortCutListenerForEscalateRaw(final TextField txtFld) {
		ShortcutListener listener =  new ShortcutListener("EnterShortcutForEscalateRaw",KeyCodes.KEY_F8,null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				final Window dialog = new Window();
				
				VerticalLayout vLayout =  new VerticalLayout();
				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(1000);
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(21);
				txtArea.setWidth("100%");
				txtArea.setReadOnly(true);
				
				txtArea.addValueChangeListener(new Property.ValueChangeListener() {
					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
				
				String strCaption = "Remarks For Escalation To RAW";
				dialog.setCaption(strCaption);
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);
				dialog.setWidth("65%");
				
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
	

	public  void handleShortcutForBilling(final TextField textField, final ShortcutListener shortcutListener) {
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
}
