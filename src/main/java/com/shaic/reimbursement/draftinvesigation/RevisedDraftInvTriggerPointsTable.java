package com.shaic.reimbursement.draftinvesigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
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

public class RevisedDraftInvTriggerPointsTable extends ViewComponent {

	private Map<DraftTriggerPointsToFocusDetailsTableDto, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DraftTriggerPointsToFocusDetailsTableDto, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<DraftTriggerPointsToFocusDetailsTableDto> data = new BeanItemContainer<DraftTriggerPointsToFocusDetailsTableDto>(DraftTriggerPointsToFocusDetailsTableDto.class);

	private Table table;

	private Button btnAdd;
	
	private List<DraftTriggerPointsToFocusDetailsTableDto> deletedInvestigationDescList;
	
	public void init(String caption) {

		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("590px");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		
		layout.addComponent(btnLayout);
		
		layout.setMargin(true);
		initTable(layout);
		table.setWidth("590px");
		table.setHeight("200px");
		table.setCaption(caption);
		
		table.setPageLength(5);
		
		addListener();
		
		
		layout.addComponent(table);
		setSizeFull();

		setCompositionRoot(layout);
	}
	
	void setTableNonEditable(){
		
//		List<DraftTriggerPointsToFocusDetailsTableDto> tableList =  getValues();
////		
//		if(tableList != null && !tableList.isEmpty()){
//		
//			Map<String, AbstractField<?>> tableRow = null;
//			
//			for (DraftTriggerPointsToFocusDetailsTableDto draftTriggerPointsToFocusDetailsTableDto : tableList) {
//		
//		        tableRow = tableItem.get(draftTriggerPointsToFocusDetailsTableDto);
//				
//				if(null != tableRow && !tableRow.isEmpty())
//				 {
//					TextField itemNoFld = (TextField)tableRow.get("remarks");
//					 if(null != itemNoFld)
//					 {
//						 itemNoFld.setReadOnly(true);
//					 }
//				 }
//			}
//		}
		table.setReadOnly(true);
	}	
	
	void initTable(VerticalLayout layout) {
		// Create a data source and bind it to a table
		table = new Table("", data);
		data.removeAllItems();
		deletedInvestigationDescList = new ArrayList<DraftTriggerPointsToFocusDetailsTableDto>();
		
		table.addStyleName("generateColumnTable");
//		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "sno", "remarks"});

		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("remarks", "Description");
		
		table.setColumnWidth("sno", 55);
		
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
		    	deleteButton.setData(itemId);
		    	deleteButton.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						
						((DraftTriggerPointsToFocusDetailsTableDto)itemId).setDeltedFlag("Y");
						deletedInvestigationDescList.add((DraftTriggerPointsToFocusDetailsTableDto)itemId);
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
			DraftTriggerPointsToFocusDetailsTableDto invTrigpointsDTO = (DraftTriggerPointsToFocusDetailsTableDto) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(invTrigpointsDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(invTrigpointsDTO, new HashMap<String, AbstractField<?>>());
			} 
			
			tableRow = tableItem.get(invTrigpointsDTO);
			
			 if("sno".equals(propertyId)) {
					TextField box = new TextField();
					box.setWidth("20px");
					box.setNullRepresentation("");
					box.setEnabled(true);
					box.setData(invTrigpointsDTO);
					tableRow.put("sno", box);
					box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					return box;
				}
			 else  if("remarks".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("450px");
				field.setMaxLength(2000);
				field.setNullRepresentation("");
				field.setData(invTrigpointsDTO);
				tableRow.put("remarks", field);
				addDetailPopupForTriggerRemarks(field, null);
				field.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				
				final TextField txt = (TextField) tableRow.get("sno");
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
	
	 public void addBeanToList(DraftTriggerPointsToFocusDetailsTableDto invTrigpointsDTO) {
		 data.addItem(invTrigpointsDTO);

	    }
	 
	 @SuppressWarnings("unchecked")
		public List<DraftTriggerPointsToFocusDetailsTableDto> getValues() {
			List<DraftTriggerPointsToFocusDetailsTableDto> itemIds = (List<DraftTriggerPointsToFocusDetailsTableDto>) this.table.getItemIds() ;
	    	return itemIds;
		}
	 
	 public String getTriggerRemarks(){
			StringBuffer trgPts = new StringBuffer("");
			if(table.getItemIds().size() > 0){
				Collection<DraftTriggerPointsToFocusDetailsTableDto> tableItems = (Collection<DraftTriggerPointsToFocusDetailsTableDto>) table.getItemIds();
				
				if(tableItems != null && !tableItems.isEmpty()){
					for (DraftTriggerPointsToFocusDetailsTableDto triggerPtsTableDto : tableItems) {
						trgPts.append(triggerPtsTableDto.getSno()).append(". ").append(triggerPtsTableDto.getRemarks()).append(" \n ");	
					}
				}
			}
			return trgPts.toString();
		}
	 
	 public List<DraftTriggerPointsToFocusDetailsTableDto> getDeletedDraftInvgDescList() {
			return deletedInvestigationDescList;
	}
	 
		public void deleteEmptyRows(){
			boolean notvalid = false;
			
			if(table.getItemIds().size() > 0){
			
				List<DraftTriggerPointsToFocusDetailsTableDto> tableItems = (List<DraftTriggerPointsToFocusDetailsTableDto>) this.getValues();
				if(tableItems != null && !tableItems.isEmpty()){
					List<DraftTriggerPointsToFocusDetailsTableDto> dummyList = new ArrayList<DraftTriggerPointsToFocusDetailsTableDto>();
					for (DraftTriggerPointsToFocusDetailsTableDto draftTriggerDetailTableDto : tableItems) {
						notvalid =	draftTriggerDetailTableDto.getRemarks() == null || draftTriggerDetailTableDto.getRemarks().isEmpty() ? false : true ; 
			        	if(!notvalid && this.table.getContainerDataSource().size() > 0){
			        		dummyList.add(draftTriggerDetailTableDto);
			        	} 
					}
					if(dummyList != null && !dummyList.isEmpty()){
						for (DraftTriggerPointsToFocusDetailsTableDto draftTriggerDetailTableDto : dummyList) {
							this.table.getContainerDataSource().removeItem(draftTriggerDetailTableDto);	
						}					
					}
				}	
				
			}
		} 
	 
		@SuppressWarnings("unchecked")
		public boolean isValid(){
			boolean notvalid = false;
			
			if(table.getItemIds().size() > 0){
			
				Collection<DraftTriggerPointsToFocusDetailsTableDto> tableItems = (Collection<DraftTriggerPointsToFocusDetailsTableDto>) table.getItemIds();
				if(tableItems != null && !tableItems.isEmpty()){
					List<DraftTriggerPointsToFocusDetailsTableDto> dummyList = new ArrayList<DraftTriggerPointsToFocusDetailsTableDto>();
					for (DraftTriggerPointsToFocusDetailsTableDto triggerPtsTableDto : tableItems) {
						notvalid =	triggerPtsTableDto.getRemarks() == null || triggerPtsTableDto.getRemarks().isEmpty() ? false : true ; 
			        	if(!notvalid && this.table.getContainerDataSource().size() > 0){
			        		dummyList.add(triggerPtsTableDto);
			        	} 
					}
					if(dummyList != null && !dummyList.isEmpty()){
						for (DraftTriggerPointsToFocusDetailsTableDto emptyTrgptsTableDto : dummyList) {
							this.table.getContainerDataSource().removeItem(emptyTrgptsTableDto);	
						}					
					}
				}	
				
			}
			if(table.getItemIds().size() > 0){	
				Collection<DraftTriggerPointsToFocusDetailsTableDto> tableItems = (Collection<DraftTriggerPointsToFocusDetailsTableDto>) table.getItemIds();
				if(tableItems != null && !tableItems.isEmpty()){
					for (DraftTriggerPointsToFocusDetailsTableDto finalTrgPtsTableDto : tableItems) {
						finalTrgPtsTableDto.setSno(((List<DraftTriggerPointsToFocusDetailsTableDto>) tableItems).indexOf(finalTrgPtsTableDto)+1);
						notvalid =	finalTrgPtsTableDto.getRemarks() == null || finalTrgPtsTableDto.getRemarks().isEmpty() || finalTrgPtsTableDto.getRemarks().length() > 2000 ? false : true ; 
					}
					
				}
				else{
					notvalid = false;
				}
		}
	    return notvalid;
		}
		
		public void setTableList(final List<DraftTriggerPointsToFocusDetailsTableDto> list) {
			int i = 1;
			table.removeAllItems();
			for (final DraftTriggerPointsToFocusDetailsTableDto bean : list) {
				bean.setSno(i);
				data.addBean(bean);
				table.addItem(bean);
				i++;
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
					DraftTriggerPointsToFocusDetailsTableDto  invTrigPtsDto = (DraftTriggerPointsToFocusDetailsTableDto) txtFld.getData();
					VerticalLayout vLayout =  new VerticalLayout();
					
					vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
					vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
					vLayout.setMargin(true);
					vLayout.setSpacing(true);
					TextArea txtArea = new TextArea();
					txtArea.setMaxLength(2000);
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
					invTrigPtsDto.setRemarks(txtArea.getValue());
					
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
					
					final Window dialog = new Window();
					dialog.setCaption("Description");
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
				
				data.addItem(new DraftTriggerPointsToFocusDetailsTableDto());
				
			}
		});
	}
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<DraftTriggerPointsToFocusDetailsTableDto> itemIds = (Collection<DraftTriggerPointsToFocusDetailsTableDto>) table.getItemIds();
		
		int i = 0;
		 for (DraftTriggerPointsToFocusDetailsTableDto invTriggerPointsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(invTriggerPointsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("sno");
				 if(null != itemNoFld)
				 {
					 //IMSSUPPOR-31095
					 itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setReadOnly(true);
				 }
			 }
		 }
		
	}
		
}
