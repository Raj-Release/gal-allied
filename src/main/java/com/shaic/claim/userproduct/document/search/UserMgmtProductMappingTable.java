package com.shaic.claim.userproduct.document.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.bulkconvertreimb.search.SearchBulkConvertReimbTableDto;
import com.shaic.domain.Claim;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class UserMgmtProductMappingTable extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<UserMgmtProductMappingDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<UserMgmtProductMappingDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<UserMgmtProductMappingDTO> container = new BeanItemContainer<UserMgmtProductMappingDTO>(UserMgmtProductMappingDTO.class);

	private Table table;
	
	private CheckBox selectAllChk;
	
	private HorizontalLayout selectAllChkLayout;
	
	private UserMgmtProductMappingDTO UserMgmtProductMappingDTO;
	
	@PostConstruct
	public void init(){
		container.removeAllItems();
		
		selectAllChk = new CheckBox("Select All");		
		selectAllChkLayout = new HorizontalLayout();
		selectAllChkLayout.addComponent(selectAllChk);
		selectAllChkLayout.setWidth("66%");
		selectAllChkLayout.setComponentAlignment(selectAllChk, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		layout.addComponent(selectAllChkLayout);
		
		initTable();
		table.setWidth("65%");
		table.setPageLength(table.getItemIds().size());
		table.setFooterVisible(true);
		layout.addComponent(table);

		setCompositionRoot(layout);
		
		
		
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		selectAllChk.addValueChangeListener(getSelectAllValueChangeListener());
		table.removeGeneratedColumn("enable");	
		table.addGeneratedColumn("enable", new Table.ColumnGenerator() {
		    
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final UserMgmtProductMappingDTO rodDTO = (UserMgmtProductMappingDTO) itemId;
				if(rodDTO.getProductMappingEnable() != null){
					
						final CheckBox chkBox = new CheckBox();
						chkBox.setData(false);
						UserMgmtProductMappingDTO uploadDocDTO = (UserMgmtProductMappingDTO)itemId;
						if(uploadDocDTO.getProductMappingEnable() != null && uploadDocDTO.getProductMappingEnable())
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
									   //reConfirmMessage(rodDTO,chkBox);
									   rodDTO.setProductMappingEnable(true);
									  
								   }else {
									   rodDTO.setProductMappingEnable(false);
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
		
		
		table.setVisibleColumns(new Object[] {"sno","productCodeWithName","productType","enable"});
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("productCodeWithName", "Product Code With Name");
		table.setColumnHeader("productType", "Product Type");
		table.setColumnHeader("enable", "Enable");
		table.setColumnWidth("sno", 60);
		table.setColumnWidth("productCodeWithName", 450);
		
		
		table.setEditable(true);
		

		table.setTableFieldFactory(new ImmediateFieldFactory());       
		
	}
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
	
		private static final long serialVersionUID = 1L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext){
			UserMgmtProductMappingDTO productMappingDetailValue = (UserMgmtProductMappingDTO) itemId;

			if (tableItem.get(productMappingDetailValue) == null) {
				tableItem.put(productMappingDetailValue, new HashMap<String, AbstractField<?>>());
			}
				if("productCodeWithName".equals(propertyId)){
				
				TextField field = new TextField();
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("450px");
				field.setNullRepresentation("");
				field.setDescription(productMappingDetailValue.getProductCodeWithName());
				return field;
				
				}
				else {
					Field<?> field = super.createField(container, itemId,
							propertyId, uiContext);

					if (field instanceof TextField)
						((TextField) field).setNullRepresentation("");
						field.setReadOnly(true);
						field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
						field.setWidth("250px");
					return field;
				}
			
		}
		
	

	}
	
	 public void addBeanToList(UserMgmtProductMappingDTO productMappingTable) {
	    	container.addItem(productMappingTable);
	  }
	 
	 
	 /*public void reConfirmMessage(final UserMgmtProductMappingDTO  rodDTO,final CheckBox chkBox){
			
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
			
		}	*/
	 
	 public void addBeanToListUserMgmt(UserMgmtProductMappingDTO productMappingValue) {
		 container.addItem(productMappingValue);
	 }

	 public List<UserMgmtProductMappingDTO> getValues() {
		 @SuppressWarnings("unchecked")
		 List<UserMgmtProductMappingDTO> itemIds = (List<UserMgmtProductMappingDTO>) container.getItemIds() ;
		 return itemIds;
	 }

	 private ValueChangeListener getSelectAllValueChangeListener() {
		 return new Property.ValueChangeListener() {

			 @Override
			 public void valueChange(ValueChangeEvent event) {

				 Boolean value = (Boolean) event.getProperty().getValue();

				 if(value != null){
					 List<UserMgmtProductMappingDTO> itemIds = new ArrayList<UserMgmtProductMappingDTO>();
					 itemIds.addAll(getValues());
					 container.removeAllItems();
					 if(value){						 
						 setSelectAllTableList(itemIds,true);
					 }else{
						 setSelectAllTableList(itemIds,false);
					 }
				 }

			 }
		 };
	 }

	 public void setTableList(List<UserMgmtProductMappingDTO> list) {
		 for (final UserMgmtProductMappingDTO bean : list) {
			 addBeanToListUserMgmt(bean);
		 }
	 }
	 
	 public void setSelectAllTableList(List<UserMgmtProductMappingDTO> list,boolean selelcted) {
		 for (UserMgmtProductMappingDTO bean : list) {
			 bean.setSelected(selelcted);
			 bean.setProductMappingEnable(selelcted);
			 addBeanToListUserMgmt(bean);
		 }
	 } 

}
