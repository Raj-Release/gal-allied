package com.shaic.claim.userproduct.document.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class UserManagementLabelTable extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<UserManagementLabelDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UserManagementLabelDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<UserManagementLabelDTO> container = new BeanItemContainer<UserManagementLabelDTO>(UserManagementLabelDTO.class);

	private Table table;
	
	private UserManagementLabelDTO userManagementLabelDTO;
	
	@PostConstruct
	public void init(){
		container.removeAllItems();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		initTable();
		table.setWidth("70%");
		table.setPageLength(table.getItemIds().size());
		table.setFooterVisible(true);
		layout.addComponent(table);

		setCompositionRoot(layout);
		
		
		
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.removeGeneratedColumn("status");
		
		table.addGeneratedColumn("status", new Table.ColumnGenerator() {
		    
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final UserManagementLabelDTO labelDTO = (UserManagementLabelDTO) itemId;
				
				
				if(labelDTO.getValue() != null){
					
					final CheckBox chkBox = new CheckBox("Enable");
					chkBox.setData(false);
					
					OptionGroup includeExcludeRadio = new OptionGroup();
					includeExcludeRadio.setData(itemId);
					includeExcludeRadio.addItems(getReadioButtonOptions());
					includeExcludeRadio.setItemCaption(true, "Include");
					includeExcludeRadio.setItemCaption(false, "Exclude");
					includeExcludeRadio.setStyleName("horizontal");
					includeExcludeRadio.setEnabled(false);
			
					
					
					UserManagementLabelDTO labelcheckboxDTO= (UserManagementLabelDTO)itemId;
					if(labelcheckboxDTO.getLabelEnable() != null && labelcheckboxDTO.getLabelEnable())
					{
						includeExcludeRadio.setEnabled(true);
						chkBox.setValue(true);
						labelcheckboxDTO.setIncludeEnable(true);
						labelcheckboxDTO.setExcludeEnable(true);
						
						if(labelcheckboxDTO.getIncludeValue()!=null && labelcheckboxDTO.getIncludeValue().toString()  == "true"){
							includeExcludeRadio.setValue(true);
						}
						 if(labelcheckboxDTO.getExcludeValue()!=null && labelcheckboxDTO.getExcludeValue().toString() == "true"){
								includeExcludeRadio.setValue(false);
								
							}
					}
					else
					{
						chkBox.setValue(false);
						includeExcludeRadio.setValue(null);
						labelcheckboxDTO.setIncludeEnable(false);
						labelcheckboxDTO.setExcludeEnable(false);
						labelcheckboxDTO.setExcludeValue(false);
						labelcheckboxDTO.setIncludeValue(false);
		
					}
					
					chkBox.addValueChangeListener(new Property.ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							
							Boolean value = (Boolean) event.getProperty().getValue();
							
							if(value != null){
							   if(value){
								   chkBox.setValue(true);
								   labelDTO.setLabelEnable(true);
								   includeExcludeRadio.setEnabled(true);
								   
								   includeExcludeRadio.addValueChangeListener(new Property.ValueChangeListener() {
										
										@Override
										public void valueChange(ValueChangeEvent event) {
											
											Boolean select = (Boolean) event.getProperty().getValue();
											
											if(select != null){
											   if(select.toString() == "true"){
												   labelDTO.setIncludeValue(true);
												   labelDTO.setExcludeValue(false);
												   labelDTO.setIncludeEnable(true);
												   labelDTO.setExcludeEnable(false);
												  
											   }else if(select.toString() == "false"){
												   
												   labelDTO.setExcludeValue(true);
												   labelDTO.setIncludeValue(false);
												   labelDTO.setExcludeEnable(true);
												   labelDTO.setIncludeEnable(false);
											   }
											   
											  /* else{
												   
												   labelDTO.setExcludeValue(false);
												   labelDTO.setIncludeValue(false);
												   labelDTO.setExcludeEnable(false);
												   labelDTO.setIncludeEnable(false);
											   }*/
											}
											
										}
									});
							   }else {
								   labelDTO.setLabelEnable(false);
								   chkBox.setValue(false);
								   includeExcludeRadio.setValue(null);
								   includeExcludeRadio.setEnabled(false);
								   labelDTO.setIncludeEnable(false);
								   labelDTO.setExcludeEnable(false);
							   }
							}
							
						}
					});
					
					   includeExcludeRadio.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean select = (Boolean) event.getProperty().getValue();
								
								if(select != null){
									labelDTO.setLabelEnable(true);
								   if(select.toString() == "true"){
									   labelDTO.setIncludeValue(true);
									   labelDTO.setExcludeValue(false);
									   labelDTO.setIncludeEnable(true);
									   labelDTO.setExcludeEnable(false);
									  
								   }else if(select.toString() == "false"){
									   
									   labelDTO.setExcludeValue(true);
									   labelDTO.setIncludeValue(false);
									   labelDTO.setExcludeEnable(true);
									   labelDTO.setIncludeEnable(false);
								   }
								   
								  /* else{
									   
									   labelDTO.setExcludeValue(false);
									   labelDTO.setIncludeValue(false);
									   labelDTO.setExcludeEnable(false);
									   labelDTO.setIncludeEnable(false);
								   }*/
								}
								
							}
						});
					HorizontalLayout statusLayout = new HorizontalLayout();
					statusLayout.addComponents(chkBox,includeExcludeRadio);
					statusLayout.setSpacing(Boolean.TRUE);
					statusLayout.setComponentAlignment(includeExcludeRadio, Alignment.MIDDLE_CENTER);
					return statusLayout;
						
				}else {
					return null;
				}
		}
		});
		
		table.setVisibleColumns(new Object[] {"sno","value","status"});
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("value", "Value");
		table.setColumnHeader("status", "Status");
		table.setColumnWidth("sno", 45);
		table.setColumnWidth("value", 400);
		
		
		table.setEditable(true);
		

		table.setTableFieldFactory(new ImmediateFieldFactory());       
		
	}
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void setTableList(final  List<UserMgmtDaignosisDTO> list) {
		table.removeAllItems();
		for (final UserMgmtDaignosisDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {


		
		private static final long serialVersionUID = 1L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext){
			UserManagementLabelDTO dagnosisDetailValue = (UserManagementLabelDTO) itemId;

			if (tableItem.get(dagnosisDetailValue) == null) {
				tableItem.put(dagnosisDetailValue, new HashMap<String, AbstractField<?>>());
			}
				if("value".equals(propertyId)){
				
				TextField field = new TextField();
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("450px");
				field.setNullRepresentation("");
				field.setDescription(dagnosisDetailValue.getValue());
				return field;
				
				}
				else {
					Field<?> field = super.createField(container, itemId,
							propertyId, uiContext);

					if (field instanceof TextField)
						((TextField) field).setNullRepresentation("");
						field.setReadOnly(true);
						field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
						field.setWidth("450px");
					return field;
				}
			
		}
		
	

	}
	
	 public void addBeanToList(UserManagementLabelDTO labelDTO) {
	    	container.addItem(labelDTO);
	  }
	 
	 
	 public void reConfirmMessage(final UserManagementLabelDTO  rodDTO,final CheckBox chkBox){
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit?",
			        "No", "Yes", new ConfirmDialog.Listener() {
					
			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                	rodDTO.setLabelEnable(true);
			                	
			                } else {
			                    chkBox.setValue(false);
			                    dialog.close();
			                   
			                }
			            }
			        });
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
			
			dialog.addCloseListener(new CloseListener() {
	            private static final long serialVersionUID = -4381415904461841881L;

	            public void windowClose(CloseEvent e) {
	            	chkBox.setData(false);
	            }
	        });
			
		}	
	 
	 public void addBeanToListUserMgmt(UserManagementLabelDTO labelValue) {
	    	container.addItem(labelValue);
	    }
	 
	 public List<UserManagementLabelDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UserManagementLabelDTO> itemIds = (List<UserManagementLabelDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	    protected Collection<Boolean> getReadioButtonOptions() {
				Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
				coordinatorValues.add(true);
				coordinatorValues.add(false);
				
				return coordinatorValues;
			}
	    
	    public void showErrorPopUp(String eMsg) {
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

	}
		


}
