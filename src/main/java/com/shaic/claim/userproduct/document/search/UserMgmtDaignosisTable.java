package com.shaic.claim.userproduct.document.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.userproduct.document.search.UserMgmtApplicableCpuTable.ImmediateFieldFactory;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class UserMgmtDaignosisTable  extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<UserMgmtDaignosisDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UserMgmtDaignosisDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<UserMgmtDaignosisDTO> container = new BeanItemContainer<UserMgmtDaignosisDTO>(UserMgmtDaignosisDTO.class);

	private Table table;
	
	private UserMgmtDaignosisDTO userMgmtDaignosisDTO;
	
	@PostConstruct
	public void init(){
		container.removeAllItems();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		initTable();
		table.setWidth("60%");
		table.setPageLength(table.getItemIds().size());
		table.setFooterVisible(true);
		layout.addComponent(table);

		setCompositionRoot(layout);
		
		
		
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.removeGeneratedColumn("enable");
		
		table.addGeneratedColumn("enable", new Table.ColumnGenerator() {
		    
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final UserMgmtDaignosisDTO rodDTO = (UserMgmtDaignosisDTO) itemId;
				if(rodDTO.getDiagnosisEnable() != null){
					
						final CheckBox chkBox = new CheckBox();
						chkBox.setData(false);
						UserMgmtDaignosisDTO uploadDocDTO = (UserMgmtDaignosisDTO)itemId;
						if(uploadDocDTO.getDiagnosisEnable() != null && uploadDocDTO.getDiagnosisEnable())
						{
							chkBox.setValue(true);
						}
						else
						{
							chkBox.setValue(false);
			
						}
						
						
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean value = (Boolean) event.getProperty().getValue();
								
								if(value != null){
								   if(value){
									   reConfirmMessage(rodDTO,chkBox);
									  
								   }else {
									   rodDTO.setDiagnosisEnable(false);
									   chkBox.setValue(false);
								   }
								}
								
							}
						});
						return chkBox;
				}else {
					return null;
				}
		}
		});
		
		
		table.setVisibleColumns(new Object[] {"sno","value","enable"});
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("value", "Value");
		table.setColumnHeader("enable", "Enable");
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
			UserMgmtDaignosisDTO dagnosisDetailValue = (UserMgmtDaignosisDTO) itemId;

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
	
	 public void addBeanToList(UserMgmtDaignosisDTO dagnosisTable) {
	    	container.addItem(dagnosisTable);
	  }
	 
	 
	 public void reConfirmMessage(final UserMgmtDaignosisDTO  rodDTO,final CheckBox chkBox){
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit?",
			        "No", "Yes", new ConfirmDialog.Listener() {
					
			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                	rodDTO.setDiagnosisEnable(true);
			                	
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
	 
	 public void addBeanToListUserMgmt(UserMgmtDaignosisDTO dagnosisValue) {
	    	container.addItem(dagnosisValue);
	    }
	 
	 public List<UserMgmtDaignosisDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<UserMgmtDaignosisDTO> itemIds = (List<UserMgmtDaignosisDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }


}
