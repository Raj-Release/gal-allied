package com.shaic.claim.fvrdetails.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.AddOnCoversTableDTO;
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
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class FVRTriggerPtsTable extends ViewComponent {

	private Map<ViewFVRDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ViewFVRDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<ViewFVRDTO> data = new BeanItemContainer<ViewFVRDTO>(ViewFVRDTO.class);

	private Table table;

	private Button btnAdd;
	
	private List<ViewFVRDTO> deletedList = null;
	private ViewFVRDTO dto;	
	private Boolean isDisable = Boolean.FALSE;
	
	public void init() {

		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("700px");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		deletedList = new ArrayList<ViewFVRDTO>();
		dto = new ViewFVRDTO();
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(btnLayout);
		layout.setMargin(true);
		
		initTable(layout);
		table.setWidth("700px");
		
		//table.setHeight("30%");
		/**
		 * Height is set for table visiblity.
		 * */
		table.setHeight("200px");
		
		table.setPageLength(table.getItemIds().size());
		
		addListener();
		
		layout.addComponent(table);
		setSizeFull();

		setCompositionRoot(layout);
	}
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("FVR Trigger Points", data);
		data.removeAllItems();
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "rollNo", "remarks"});

		table.setColumnHeader("rollNo", "S.No");
		table.setColumnHeader("remarks", "Description");
		
		table.setEditable(true);
		//		manageListeners();
		
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button deleteButton = new Button("Delete");
		    	dto = (ViewFVRDTO)itemId;   
		    	if(isDisable){
		    		deleteButton.setEnabled(!dto.getTriggerPointsAlreadyExist());
		    	}		    	
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						dto = (ViewFVRDTO) currentItemId;
						deletedList.add(dto);
						
						table.removeItem(currentItemId);
			        } 
			    });
		    	return deleteButton;
		      };
		});

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			ViewFVRDTO benefitsDTO = (ViewFVRDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(benefitsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(benefitsDTO, new HashMap<String, AbstractField<?>>());
			} 
			
			tableRow = tableItem.get(benefitsDTO);
			
			 if("rollNo".equals(propertyId)) {
					TextField box = new TextField();
					box.setWidth("20px");
					box.setNullRepresentation("");
					box.setEnabled(true);
					box.setData(benefitsDTO);
					tableRow.put("rollNo", box);
					box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					return box;
				}
			 else  if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("400px");
				field.setMaxLength(600);
				field.setNullRepresentation("");
				field.setData(benefitsDTO);
				if(isDisable){
					field.setEnabled(!benefitsDTO.getTriggerPointsAlreadyExist());
				}
				tableRow.put("remarks", field);
				
				addDetailPopupForTriggerRemarks(field, null);
				field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				
				final TextField txt = (TextField) tableRow.get("rollNo");
				generateSlNo(txt);
				
				return field;
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
	
	
	
	
	 public void addBeanToList(ViewFVRDTO benefitsdto) {
		 data.addItem(benefitsdto);

	    }
	 
	 @SuppressWarnings("unchecked")
		public List<ViewFVRDTO> getValues() {
			List<ViewFVRDTO> itemIds = (List<ViewFVRDTO>) this.table.getItemIds() ;
			dto.setDeletedList(deletedList);
	    	return itemIds;
		}
	 
	 public String getTriggerRemarks(){
			StringBuffer trgPts = new StringBuffer("");
			if(table.getItemIds().size() > 0){
				Collection<ViewFVRDTO> tableItems = (Collection<ViewFVRDTO>) table.getItemIds();
				
				if(tableItems != null && !tableItems.isEmpty()){
					for (ViewFVRDTO triggerPtsTableDto : tableItems) {
						trgPts.append(triggerPtsTableDto.getRollNo()).append(". ").append(triggerPtsTableDto.getRemarks()).append(" \n ");	
					}
				}
			}
			return trgPts.toString();
		}
		@SuppressWarnings("unchecked")
		public boolean isValid(){
			boolean notvalid = false;
			
			if(table.getItemIds().size() > 0){
			
				Collection<ViewFVRDTO> tableItems = (Collection<ViewFVRDTO>) table.getItemIds();
				if(tableItems != null && !tableItems.isEmpty()){
					List<ViewFVRDTO> dummyList = new ArrayList<ViewFVRDTO>();
					for (ViewFVRDTO triggerPtsTableDto : tableItems) {
						notvalid =	triggerPtsTableDto.getRemarks() == null || triggerPtsTableDto.getRemarks().isEmpty() ? false : true ; 
			        	if(!notvalid && this.table.getContainerDataSource().size() > 0){
			        		dummyList.add(triggerPtsTableDto);
			        	} 
					}
					if(dummyList != null && !dummyList.isEmpty()){
						for (ViewFVRDTO emptyTrgptsTableDto : dummyList) {
							this.table.getContainerDataSource().removeItem(emptyTrgptsTableDto);	
						}					
					}
				}	
				
			}
			if(table.getItemIds().size() > 0){	
				Collection<ViewFVRDTO> tableItems = (Collection<ViewFVRDTO>) table.getItemIds();
				if(tableItems != null && !tableItems.isEmpty()){
					for (ViewFVRDTO finalTrgPtsTableDto : tableItems) {
						finalTrgPtsTableDto.setSno(String.valueOf(((List<ViewFVRDTO>) tableItems).indexOf(finalTrgPtsTableDto)+1));
						notvalid =	finalTrgPtsTableDto.getRemarks() == null || finalTrgPtsTableDto.getRemarks().isEmpty() || finalTrgPtsTableDto.getRemarks().length() > 600 ? false : true ; 
					}
					
				}
				else{
					notvalid = false;
				}
		}
	    return notvalid;
		}
		
		public void setTableList(final List<ViewFVRDTO> list) {
			//int i = 1;
			table.removeAllItems();
			for (final ViewFVRDTO bean : list) {
				//bean.setSerialNumber(i);
				table.addItem(bean);
				//i++;
			}
		}
		
		private void addDetailPopupForTriggerRemarks(TextField txtFld, final  Listener listener) {
			  ShortcutListener enterShortCut = new ShortcutListener(
				        "remarks", ShortcutAction.KeyCode.F8, null) {
					
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
					ViewFVRDTO  searchPedDto = (ViewFVRDTO) txtFld.getData();
					VerticalLayout vLayout =  new VerticalLayout();
					
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					TextArea txtArea = new TextArea();
					txtArea.setMaxLength(600);
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
					searchPedDto.setRemarks(txtArea.getValue());
					
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
					
					final Window dialog = new Window();
					dialog.setCaption("Trigger Points");
					dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
					dialog.setWidth("45%");
					dialog.setClosable(false);
					
					dialog.setContent(vLayout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.setDraggable(true);
					
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

	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				
				/*List<ViewFVRDTO> dtoList = (List<ViewFVRDTO>)table.getItemIds();
				
				if(dtoList != null && !dtoList.isEmpty()){
					docDTO.setSerialNumber(dtoList.size()+1);
				}else{
					docDTO.setSerialNumber(1);
				}*/
				
				data.addItem(new ViewFVRDTO());

				
			}
		});
	}
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<ViewFVRDTO> itemIds = (Collection<ViewFVRDTO>) table.getItemIds();
		
		int i = 0;
		 for (ViewFVRDTO billEntryDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("rollNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}
		
	public void getIsDisableFields(Boolean isDisable){
		this.isDisable = isDisable;
	}
}
