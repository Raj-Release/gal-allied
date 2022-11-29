package com.shaic.claim.userproduct.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class ApplicableCpuTable extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<ApplicableCpuDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ApplicableCpuDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<ApplicableCpuDTO> container = new BeanItemContainer<ApplicableCpuDTO>(ApplicableCpuDTO.class);

	private Table table;
	
	//private Button btnAdd;
	
	//private Map<String, Object> referenceData;
	
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
	
	public void setReferenceData(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setPageLength(table.getItemIds().size());
		
		table.removeGeneratedColumn("Accessibility");
		
		table.addGeneratedColumn("Accessibility", new Table.ColumnGenerator() {
		    
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final ApplicableCpuDTO rodDTO = (ApplicableCpuDTO) itemId;
				if(rodDTO.getCpuCodewithName() != null){
					
						final CheckBox chkBox = new CheckBox();
						chkBox.setData(false);
						ApplicableCpuDTO uploadDocDTO = (ApplicableCpuDTO)itemId;
						if(uploadDocDTO.getCheckBoxValue() != null && uploadDocDTO.getCheckBoxValue())
						{
							
							chkBox.setValue(true);
						}
						else
						{
							chkBox.setValue(false);
							
						}
						
						/*if(uploadDocDTO.getIsEnabled()){
							chkBox.setEnabled(true);
						}else{
							chkBox.setEnabled(false);
						}*/
						
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean value = (Boolean) event.getProperty().getValue();
								
//								Boolean isAlreadyOpened = (Boolean)chkBox.getData();
								
								if(value != null){
								   if(value){
									   reConfirmMessage(rodDTO,chkBox);
								   }else {
									   rodDTO.setAccessability(false);
									   rodDTO.setCheckBoxValue(false);
									   rodDTO.setActiveStatus(0l);
									   chkBox.setValue(false);
//									   reConfirmMessageForCancel(rodDTO,chkBox);
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
		
		
		table.setVisibleColumns(new Object[] {"sno","cpuCodewithName","Accessibility"});
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("cpuCodewithName", "CPU Code With Name");
		table.setColumnWidth("sno", 45);
		table.setColumnWidth("cpuCodewithName", 400);
		
		
		table.setEditable(true);
		

		table.setTableFieldFactory(new ImmediateFieldFactory());       
	}
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void setTableList(final List<ApplicableCpuDTO> list) {
		table.removeAllItems();
		for (final ApplicableCpuDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

	
		private static final long serialVersionUID = 1L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext){
			ApplicableCpuDTO preHospitalizationDTO = (ApplicableCpuDTO) itemId;
			//Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(preHospitalizationDTO) == null) {
				//tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(preHospitalizationDTO, new HashMap<String, AbstractField<?>>());
			} /*else {
				tableRow = tableItem.get(preHospitalizationDTO);
			}*/
			
			
				if("cpucodewithName".equals(propertyId)){
				
				TextField field = new TextField();
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("450px");
				field.setNullRepresentation("");
				field.setDescription(preHospitalizationDTO.getCpuCodewithName());
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
	
	 public void addBeanToList(ApplicableCpuDTO cpuDetails) {
	    	container.addItem(cpuDetails);
	    }
	 
	 
	 public List<ApplicableCpuDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<ApplicableCpuDTO> itemIds = (List<ApplicableCpuDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

//	public void addBeanToList(List<ApplicableCpuDTO> cpuDetails) {
//		
//		// TODO Auto-generated method stub
//		container.addAll(cpuDetails);
//	}
	
	public void reConfirmMessage(final ApplicableCpuDTO rodDTO,final CheckBox chkBox){
		
		ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit Accessibility?",
		        "No", "Yes", new ConfirmDialog.Listener() {
				
		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	rodDTO.setAccessability(true);
		                	rodDTO.setCheckBoxValue(true);
		                	
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
//                System.out.println("close called");
            	chkBox.setData(false);
            }
        });
		
	}	
	
	public void reConfirmMessageForCancel(final ApplicableCpuDTO rodDTO,final CheckBox chkBox){
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit Accessibility?",
			        "No", "Yes", new ConfirmDialog.Listener() {
	
			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                	rodDTO.setAccessability(false);
			                	rodDTO.setCheckBoxValue(false);
			                	rodDTO.setActiveStatus(0l);
			                	chkBox.setValue(false);
			                	chkBox.setData(false);
			                } else {
			                    chkBox.setData(true);
			                    chkBox.setValue(true);
			                    dialog.close();
			                    
			                }
			            }
			           
			        });
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
			
			dialog.addCloseListener(new CloseListener() {
	            private static final long serialVersionUID = -4381415904461841881L;

	            public void windowClose(CloseEvent e) {
//	                System.out.println("close called");
	            	chkBox.setData(false);
	            }
	        });
			
		}

}