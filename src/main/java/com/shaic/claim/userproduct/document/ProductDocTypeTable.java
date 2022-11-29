package com.shaic.claim.userproduct.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ProductDocTypeTable extends ViewComponent{
	
/**
	 * 
	 */
	

	private Map<ProductDocTypeDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ProductDocTypeDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<ProductDocTypeDTO> container = new BeanItemContainer<ProductDocTypeDTO>(ProductDocTypeDTO.class);

	private Table table;
	
	/*private Button btnAdd;
	
	private Map<String, Object> referenceData;*/
	
	public void init(){
		container.removeAllItems();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		initTable();
		table.setWidth("90%");
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
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.removeGeneratedColumn("Preauth");
		
		table.addGeneratedColumn("Preauth", new Table.ColumnGenerator() {
		    
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final ProductDocTypeDTO rodDTO = (ProductDocTypeDTO) itemId;
				if(rodDTO.getProductDocType() != null){
				
						final CheckBox chkBox = new CheckBox();
						chkBox.setData(false);
						ProductDocTypeDTO uploadDocDTO = (ProductDocTypeDTO)itemId;
						if(uploadDocDTO.getPreauthcheckBoxValue() != null && uploadDocDTO.getPreauthcheckBoxValue())
						{
							
							chkBox.setValue(true);
						}
						else
						{
							chkBox.setValue(false);
							
						}
						
						if(uploadDocDTO.getIsEnabled()) {
							chkBox.setEnabled(true);
						}else {
							chkBox.setEnabled(false);
						}
						
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean value = (Boolean) event.getProperty().getValue();
								
//								Boolean isAlreadyOpened = (Boolean)chkBox.getData();
								
								if(value != null){
								   if(value){
									   reConfirmMessageforPreauth(rodDTO,chkBox);
								   }else {
										rodDTO.setPreauthValue(false);
					                	rodDTO.setPreauthcheckBoxValue(false);
					                	rodDTO.setActiveStatus(0l);
					                	chkBox.setValue(false);
//									   cancelledMessageforPreauth(rodDTO,chkBox);
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
		
		table.removeGeneratedColumn("Enhancement");
		
		table.addGeneratedColumn("Enhancement", new Table.ColumnGenerator() {
		    
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("serial")
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final ProductDocTypeDTO rodDTO = (ProductDocTypeDTO) itemId;
				if(rodDTO.getProductDocType() != null){
				
						final CheckBox chkBox = new CheckBox();
						
						if(rodDTO.getEnhancheckBoxValue() !=null && rodDTO.getEnhancheckBoxValue()) {
							chkBox.setValue(true);
						} else {
							chkBox.setValue(false);
						}
						
						if(rodDTO.getIsEnabled()) {
							chkBox.setEnabled(true);
						}else {
							chkBox.setEnabled(false);
						}
						
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean value = (Boolean) event.getProperty().getValue();
								
//								Boolean isAlreadyOpened = (Boolean)chkBox.getData();
								
								if(value != null){
								   if(value){
									   reConfirmMessageforEnhancement(rodDTO,chkBox);
								   }else {
									   	rodDTO.setEnhancementValue(false);
					                	rodDTO.setEnhancheckBoxValue(false);
					                	rodDTO.setActiveStatus(0l);
					                	chkBox.setValue(false);
//									   cancelledMessageforEnhancemnet(rodDTO,chkBox);
									  
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
		
		
		table.setVisibleColumns(new Object[] { "sno","productDocType","Preauth","Enhancement"});
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("productDocType", "Product / Doc Type");
		table.setColumnWidth("sno", 45);
		table.setColumnWidth("productDocType", 600);
		
		table.setEditable(true);
		

		table.setTableFieldFactory(new ImmediateFieldFactory());       
	}
	
	public void setTableList(final List<ProductDocTypeDTO> list) {
		table.removeAllItems();
		for (final ProductDocTypeDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

	
		private static final long serialVersionUID = 1L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext){
			ProductDocTypeDTO prodDocTypeDto = (ProductDocTypeDTO) itemId;
			//Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(prodDocTypeDto) == null) {
				//tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(prodDocTypeDto, new HashMap<String, AbstractField<?>>());
			} /*else {
				tableRow = tableItem.get(prodDocTypeDto);
			}*/
			
			
				if("cpucodewithName".equals(propertyId)){
				
				TextField field = new TextField();
				field.setReadOnly(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("450px");
				field.setNullRepresentation("");
				field.setDescription(prodDocTypeDto.getProductDocType());
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
	
	 public void addBeanToList(ProductDocTypeDTO preHospitalizationDTO) {
	    	container.addItem(preHospitalizationDTO);

	    }
	 
	 public List<ProductDocTypeDTO> getValues() {
	    	if(null!= table){
				List<ProductDocTypeDTO> itemProducts = (List<ProductDocTypeDTO>) this.table.getItemIds() ;
		    	return itemProducts;
	    	}
	    	return null;
	    }
	 
	 public void reConfirmMessageforPreauth(final ProductDocTypeDTO rodDTO,final CheckBox chkBox){
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit Preauth?",
			        "No", "Yes", new ConfirmDialog.Listener() {

			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                	rodDTO.setPreauthValue(true);
			                	rodDTO.setPreauthcheckBoxValue(true);
			                	rodDTO.setActiveStatus(1l);
			                	chkBox.setData(false);
			                } else {
			                	chkBox.setValue(false);
			                    chkBox.setData(true);
			                    dialog.close();
			                    
			                }
			            }
			        });
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
		    dialog.setClosable(false);
			
			dialog.addCloseListener(new CloseListener() {
	            private static final long serialVersionUID = -4381415904461841881L;

	            public void windowClose(CloseEvent e) {
//	                System.out.println("close called");
	            	chkBox.setData(false);
	            	
	            }
	        });
			
		}	
	 
	 public void cancelledMessageforPreauth(final ProductDocTypeDTO rodDTO,final CheckBox chkBox){
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit Preauth?",
			        "No", "Yes", new ConfirmDialog.Listener() {
	
			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                	rodDTO.setPreauthValue(false);
			                	rodDTO.setPreauthcheckBoxValue(false);
			                	rodDTO.setActiveStatus(0l);
			                	chkBox.setValue(false);
			                	chkBox.setData(false);
			                } else {
			                	chkBox.setValue(true);
				                chkBox.setData(true);
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
	 
	 public void reConfirmMessageforEnhancement(final ProductDocTypeDTO rodDTO,final CheckBox chkBox){
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit Enhancement?",
			        "No", "Yes", new ConfirmDialog.Listener() {

			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                	rodDTO.setEnhancementValue(true);
			                	rodDTO.setEnhancheckBoxValue(true);
			                	rodDTO.setActiveStatus(1l);
			                	chkBox.setData(false);
			                } else {
			                	chkBox.setValue(false);
			                    chkBox.setData(true);
			                    dialog.close();
			                    
			                }
			            }
			        });
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
			dialog.setClosable(false);
			
			dialog.addCloseListener(new CloseListener() {
	            private static final long serialVersionUID = -4381415904461841881L;

	            public void windowClose(CloseEvent e) {
//	                System.out.println("close called");
	            	chkBox.setData(false);
	            }
	        });
			
		}
	 
	 public void cancelledMessageforEnhancemnet(final ProductDocTypeDTO rodDTO,final CheckBox chkBox){
			
			ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Do You want to Edit Enhancement?",
			        "No", "Yes", new ConfirmDialog.Listener() {
	
			            public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                	rodDTO.setEnhancementValue(false);
			                	rodDTO.setEnhancheckBoxValue(false);
			                	rodDTO.setActiveStatus(0l);
			                	chkBox.setValue(false);
			                	chkBox.setData(false);
			                } else {
			                	rodDTO.setEnhancementValue(false);
				                chkBox.setValue(true);
				                chkBox.setData(true);
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

	public void addBeanToList(List<ProductDocTypeDTO> productDTO) {
		// TODO Auto-generated method stub
		container.addItem(productDTO);
	}	

}