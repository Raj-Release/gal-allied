package com.shaic.reimburement.gatewayAddAdditinalDocument.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryUploadDocumentsPresenter;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Page;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UploadedReceivedPhysicalDocumentsTable extends GEditableTable<UploadDocumentDTO>{
	
	//private List<String> errorMessages;
	
	private String presenterString;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	private Page currentPage;
	
	@EJB
	private MasterService masterService;
	
	public Boolean isDocClicked = false;
	
	private WeakHashMap<UploadDocumentDTO, OptionGroup> receivedMap = new WeakHashMap<UploadDocumentDTO, OptionGroup>();
	private WeakHashMap<UploadDocumentDTO, OptionGroup> ignoreMap = new WeakHashMap<UploadDocumentDTO, OptionGroup>();
	
	public UploadedReceivedPhysicalDocumentsTable() {
		super(UploadedReceivedPhysicalDocumentsTable.class);
		setUp();
	}

/*	public static final Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };*/

	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();
	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
	//Have a map for storing container value

	static {
		
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
		fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
		fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
		fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
	}*/

	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
	}

	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		 final BeanItemContainer<SelectValue> selectValueContainer = masterService
					.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_STATUS);
		 final BeanItemContainer<SelectValue> receivedValueConatiner =new BeanItemContainer<SelectValue>(SelectValue.class); 
		 
		 List<SelectValue> selectValue = selectValueContainer.getItemIds();
		 List<SelectValue> newSelectValue = new ArrayList<SelectValue>();
		 for(SelectValue selectValue1 : selectValue){
			if(!selectValue1.getId().equals(1563l)) {
				newSelectValue.add(selectValue1);
			}
		 }
		 receivedValueConatiner.addAll(newSelectValue);
		 Collection<Boolean> Arrayselect = new ArrayList<Boolean>(1);
			Arrayselect.add(true);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		
		table.removeGeneratedColumn("received");
		table.addGeneratedColumn("received", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO)itemId;
		    	 OptionGroup received = new OptionGroup();
		    	received.setData(itemId);
		    	received.addItem(getReadioButtonOptions());
		    	
		    	received.setItemCaptionMode(ItemCaptionMode.ICON_ONLY);
		    	receivedMap.put(uploadDocsDTO, received);
		    	UploadDocumentDTO docDto = (UploadDocumentDTO)itemId;
		    	if(docDto.getIsReceived() != null && docDto.getIsReceived()){
		    		received.select(true);
		    		received.setValue(Arrayselect);
		    	}
		    	
		    	received.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
						OptionGroup component = (OptionGroup)event.getProperty();
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO)component.getData();
						if(event.getProperty() != null && event.getProperty().getValue() != null) {
							ArrayList<Boolean> valueList = (ArrayList<Boolean>)event.getProperty().getValue();
//							documentValueChangeListener("received");
							if(! valueList.isEmpty()){
							if(ignoreMap != null && ! ignoreMap.isEmpty()){
								OptionGroup optionGroup = ignoreMap.get(uploadDocsDTO);
								optionGroup.select(null);
							  }		
							    uploadDocsDTO.setIsReceived(true);
							
							}else{
								uploadDocsDTO.setIsReceived(false);
							}
							uploadDocsDTO.setIsIgnored(false);
							uploadDocsDTO.setPhysicalDocumentReceived(true);
							uploadDocsDTO.setPhysicalDocumentIgnored(false);
						}
						//UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) currentItemId;
						//tableSelectHandler(uploadDocsDTO);
					}
				}); 
				
		        return received;
		}
		    });
		
		
		
		table.removeGeneratedColumn("ignore");
		table.addGeneratedColumn("ignore", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO)itemId;
		    	 OptionGroup ignore = new OptionGroup();
		    	ignore.setData(itemId);
		    	ignore.addItem(getReadioButtonOptions());	
		    	ignore.setItemCaptionMode(ItemCaptionMode.ICON_ONLY);
		    	ignoreMap.put(uploadDocsDTO, ignore);
		    	UploadDocumentDTO docDto = (UploadDocumentDTO)itemId;
		    	if(docDto.getIsIgnored() != null && docDto.getIsIgnored()){
		    		ignore.select(true);
		    		ignore.setValue(Arrayselect);
		    	}
		    	
		    	ignore.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {						
						
						OptionGroup component = (OptionGroup)event.getProperty();
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO)component.getData();
						
						if(event.getProperty() != null && event.getProperty().getValue() != null) {
							ArrayList<Boolean> valueList = (ArrayList<Boolean>)event.getProperty().getValue();
//							documentValueChangeListener("ignore");
							if(! valueList.isEmpty()){
							if(receivedMap != null && ! receivedMap.isEmpty()){
								OptionGroup optionGroup =receivedMap.get(uploadDocsDTO);
								optionGroup.select(null);
							}
							uploadDocsDTO.setIsIgnored(true);
						  }else{
							  uploadDocsDTO.setIsIgnored(false);
						  }
							uploadDocsDTO.setIsReceived(false);
							uploadDocsDTO.setPhysicalDocumentReceived(false);
							uploadDocsDTO.setPhysicalDocumentIgnored(true);
						}
						
					}
				}); 
				
		        return ignore;
		}
		    });
		
		table.removeGeneratedColumn("docType");
		table.addGeneratedColumn("docType", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final ComboBox docType = new ComboBox();
		    	UploadDocumentDTO docDto = (UploadDocumentDTO)itemId;
		    	docType.setData(itemId);
		    	if(docDto.getIsReceived()){
		    		docType.setContainerDataSource(receivedValueConatiner);
		    	}else{
		    	docType.setContainerDataSource(selectValueContainer);
		    	}
		    	docType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		    	docType.setItemCaptionPropertyId("value");
		    	
		    	if(docDto.getDocumentType() != null){
		    		docType.setValue(docDto.getDocumentType());
		    	}
		    	docType.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
						ComboBox cmbDocType = (ComboBox)event.getProperty();
//						cmbDocType.removeAllItems();
//						if(docDto.getIsReceived()){
//							cmbDocType.setContainerDataSource(receivedValueConatiner);
//				    	}else{
//				    		cmbDocType.setContainerDataSource(selectValueContainer);
//				    	}
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO)cmbDocType.getData();
						if(cmbDocType.getValue() != null){
							SelectValue selected = (SelectValue)cmbDocType.getValue();
							uploadDocsDTO.setDocumentType(selected);
						}
						
					}
				});
		    	
		    	docType.addFocusListener(new FocusListener() {
					
					@Override
					public void focus(FocusEvent event) {
						ComboBox cmbDocType = (ComboBox)event.getComponent();
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO)cmbDocType.getData();
						if(uploadDocsDTO.getIsReceived()){
							cmbDocType.setContainerDataSource(receivedValueConatiner);
				    	}else{
				    		cmbDocType.setContainerDataSource(selectValueContainer);
				    	}
						
					}
				});
		        return docType;
		      }
		    });	
		
		table.removeGeneratedColumn("receivedDate");
		table.addGeneratedColumn("receivedDate", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
				PopupDateField receivedDate = new PopupDateField();
				receivedDate.setData(itemId);
				receivedDate.setDateFormat("dd/MM/yyyy");
				//Vaadin8-setImmediate() receivedDate.setImmediate(true);
				UploadDocumentDTO docDto = (UploadDocumentDTO)itemId;
				if(docDto.getDocReceivedDate() != null){
					receivedDate.setValue(docDto.getDocReceivedDate());
				}
				receivedDate.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(com.vaadin.v7.data.Property.ValueChangeEvent event) {
						PopupDateField cmbDocType = (PopupDateField)event.getProperty();
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO)cmbDocType.getData();
						Date value = cmbDocType.getValue();
						if(value != null){
							uploadDocsDTO.setDocReceivedDate(value);
						}
					}
				});
				return receivedDate;
			}
		    });
		
		table.removeGeneratedColumn("edit");
		table.addGeneratedColumn("edit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("Edit");
		    	editButton.setData(itemId);
		    	
		    	UploadDocumentDTO uploadDTOForEdit = (UploadDocumentDTO) itemId;		   
		    	
		    	editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
						UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;

						uploadDTO.setIsEdit(true);
						
						 if (presenterString != null && (presenterString.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT) || presenterString.equalsIgnoreCase(SHAConstants.PHYSICAL_DOCUMENT_CHECKER))){
							fireViewEvent(UploadReceivedPhysicalDocumentsPresenter.EDIT_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS, uploadDTO);				
			        } else {
						fireViewEvent(UploadReceivedPhysicalDocumentsPresenter.EDIT_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS, uploadDTO);				
	
			        }
					}
			    });
		    	editButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return editButton;
		      }
		    });
		
//		table.removeGeneratedColumn("delete");
//		table.addGeneratedColumn("delete", new Table.ColumnGenerator() {
//			private static final long serialVersionUID = 5936665477260011479L;
//
//			@Override
//		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
//		    	final Button deleteButton = new Button("Delete");
//		    	deleteButton.setData(itemId);
//		    	deleteButton.addClickListener(new Button.ClickListener() {
//					private static final long serialVersionUID = 6100598273628582002L;
//
//					public void buttonClick(ClickEvent event) {
//			        	Object currentItemId = event.getButton().getData();						
//			        	
//						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) currentItemId;
//						uploadDocsDTO.setIsEdit(false);
//						//BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) currentItemId;
//						if(null != uploadDocsDTO.getDocSummaryKey())
//						{
//							//billEntryDetailsDTO.setDeletedFlag("Y");
//							deletedList.add(uploadDocsDTO);
//						}
//						table.removeItem(currentItemId);
//						tableSelectHandler(uploadDocsDTO);
//						
//			        } 
//			    });
//		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
//		        return deleteButton;
//		      }
//		    });	
		
		table.removeGeneratedColumn("view");
		table.addGeneratedColumn("view", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button viewBtn = new Button("view");
		    	viewBtn.setData(itemId);
		    	viewBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();	
			        	UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) currentItemId;
						Window popup = new com.vaadin.ui.Window();
						popup.setWidth("75%");
						popup.setHeight("90%");
						if(uploadDocsDTO != null && uploadDocsDTO.getFileName() != null && uploadDocsDTO.getDmsDocToken() != null){
							fileViewUI.setCurrentPage(currentPage);
							fileViewUI.init(popup,uploadDocsDTO.getFileName(), uploadDocsDTO.getDmsDocToken());
						}
						popup.setContent(fileViewUI);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
			        } 
			    });
		    	viewBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	viewBtn.addStyleName(ValoTheme.BUTTON_LINK);
		        return viewBtn;
		      }
		    });
		
		table.setColumnHeader("docType", "Doc Type");
		table.setColumnHeader("receivedDate", "Received Date");
		
		
	}
	
	public void addLisenter(ComboBox cmbDocumentType){
	}
	
	public void initPresenter(String presenterString)
	{
		this.presenterString = presenterString;
		
	}

	@Override
	public String textBundlePrefixString() {
		return "bill-entry-uploaded-documents-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
			
			fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
			fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
			fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
			fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
			fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
			fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
			fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
			
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

	}

	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData);
	}
	

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	
	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {
		// TODO Auto-generated method stub
		if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString)){
			fireViewEvent(BillEntryUploadDocumentsPresenter.BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS,t);
		}else{
			fireViewEvent(UploadReceivedPhysicalDocumentsPresenter.DELETE_RECEIVED_PHYSICAL_UPLOADED_DOCUMENTS,t);
		}
		
		
	}
	
	public List<UploadDocumentDTO> getDeletedDocumentList()
	{
		return deletedList;
	}
	
	  protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(1);
			coordinatorValues.add(true);
			return coordinatorValues;
		}
	

}
