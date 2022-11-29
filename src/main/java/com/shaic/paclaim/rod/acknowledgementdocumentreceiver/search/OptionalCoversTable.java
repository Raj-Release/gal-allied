package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.paclaim.rod.createrod.search.PACreateRODDocumentDetailsPresenter;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
@Alternative
public class OptionalCoversTable extends GListenerTable<AddOnCoversTableDTO> implements TableCellSelectionHandler{

	
	private static final long serialVersionUID = -2239330323400228835L;
	
	private String presenterString;

	public OptionalCoversTable() {
		super(OptionalCoversTable.class);
		setUp();
		deltedAddOnCoverList = new ArrayList<AddOnCoversTableDTO>();
	}
	
	private List<AddOnCoversTableDTO> deltedAddOnCoverList;
	
	private List<String> errorMessages;
	
//	public NewBornBabyTable(NewBabyIntimationDto bean) {
//		super(NewBornBabyTable.class);
//		setUp();
//	}
	
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"optionalCover", "claimedAmount" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	 {
	
		//fieldMap.put("S.No", new TableFieldDTO("serialNumber", TextField.class, String.class, true));
		fieldMap.put("optionalCover", new TableFieldDTO("optionalCover", ComboBox.class, SelectValue.class, true,true,800,this));
		fieldMap.put("claimedAmount", new TableFieldDTO("claimedAmount",TextField.class,String.class, true,15,true));
	}*/
	 
	 public void setScreenName(String presenterString){
		 this.presenterString = presenterString;
	 }
	
	@Override
	protected void newRowAdded() {

	}

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		this.errorMessages = new ArrayList<String>();
		table.setColumnWidth("claimedAmount", 150);
		table.setContainerDataSource(new BeanItemContainer<AddOnCoversTableDTO>(AddOnCoversTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {
			"optionalCover", "claimedAmount" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		if(!table.getVisibleItemIds().contains("Delete")){
			table.removeGeneratedColumn("Delete");
			table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			      @Override
			      public Object generateCell(final Table source, final Object itemId, Object columnId) {
			    	Button deleteButton = new Button("Delete");
			    	deleteButton.addClickListener(new Button.ClickListener() {
				        public void buttonClick(ClickEvent event) {
				        	AddOnCoversTableDTO deletedDTO = (AddOnCoversTableDTO)itemId;
				        	deltedAddOnCoverList.add(deletedDTO);
				        	 deleteRow(itemId);
				        	 if(presenterString.equalsIgnoreCase(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)){
								 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,deletedDTO.getOptionalCover());
							 }else if(presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)){
								 fireViewEvent(PACreateRODDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,deletedDTO.getOptionalCover());
							 }else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
								// fireViewEvent(PABillEntryDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,selected.getValue());
							 }
				        } 
				    });
			    	return deleteButton;
			      };
			});
		}
		table.setColumnWidth("Delete", 100);
		table.setWidth("600px");
		table.setPageLength(5);
	}

	
	@Override
	public void tableSelectHandler(AddOnCoversTableDTO t) {
		
	}
	
	 public void setReference(Map<String, Object> referenceData) {
			super.setReference(referenceData, this);
	}

	@Override
	public String textBundlePrefixString() {
		return "addoncoverstable-";
	}
	
	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
	 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		
			fieldMap.put("optionalCover", new TableFieldDTO("optionalCover", ComboBox.class, SelectValue.class, true,true,800,this));
			fieldMap.put("claimedAmount", new TableFieldDTO("claimedAmount",TextField.class,String.class, true,15,true));
			
			return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
			System.out.println(object);
		}
	}

	@Override
	public void deleteRow(Object itemId) {
		AddOnCoversTableDTO deletedDTO = (AddOnCoversTableDTO)itemId;
		this.table.getContainerDataSource().removeItem(itemId);
    	 if(presenterString.equalsIgnoreCase(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)){
			 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,deletedDTO.getOptionalCover().getValue(),true);
		 }else if(presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)){
			 fireViewEvent(PACreateRODDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,deletedDTO.getOptionalCover().getValue(),true);
		 }else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
			// fireViewEvent(PABillEntryDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,selected.getValue());
		 }
	}

	public List<AddOnCoversTableDTO> getDeltedAddOnCoversList() {
		return deltedAddOnCoverList;
	}

	public void setDeltedAddOnCoversList(List<AddOnCoversTableDTO> deltedBabyList) {
		this.deltedAddOnCoverList = deltedBabyList;
	}

	public List<AddOnCoversTableDTO> getTableList()
	{
		List<AddOnCoversTableDTO> addOnCovers = (List<AddOnCoversTableDTO>) table.getItemIds();
		
		return addOnCovers;
		
	}
	
	 public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			List<AddOnCoversTableDTO> itemIds = (List<AddOnCoversTableDTO>) this.table.getItemIds();
			Map<Long, String> duplicateItemMap = new HashMap<Long, String>();
			Map<Long, String> validationMap = new HashMap<Long, String>();
			if(null != itemIds && !itemIds.isEmpty())
			{
				for (AddOnCoversTableDTO bean : itemIds) {
					if(null != bean.getOptionalCover()) 
					{
						if(null != duplicateItemMap && !duplicateItemMap.isEmpty())
						{
							if(duplicateItemMap.containsKey(bean.getOptionalCover().getId()))
							{
								hasError = true;
							
								{
									errorMessages.add("Same covers value cannot be selected twice in Optional Covers Table. Please change the covers to proceed further");
								}
								
								break;
							}
							
						}
							duplicateItemMap.put(bean.getOptionalCover().getId(), bean.getOptionalCover().getValue());
					}
					else
					{
						hasError = true;
						errorMessages.add("Covers value cannot be empty in Optional Covers Table. Please select the covers value in row " + bean.getSlNo() +" to proceed further");
						break;
					}
			}
		}
			return !hasError;

		}
	 
	 public List<String> getErrors()
		{
			return this.errorMessages;
		}

	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		
		if(event != null && event.getProperty() != null){
			if(event.getProperty() instanceof ComboBox){
			 ComboBox comboBox = (ComboBox)event.getProperty();
			 if(comboBox != null && comboBox.getValue() != null){
				 SelectValue selected = (SelectValue)comboBox.getValue();
				 if(presenterString.equalsIgnoreCase(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)){
					 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,selected,false);
				 }else if(presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)){
					 fireViewEvent(PACreateRODDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,selected,false);
				 }else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
					// fireViewEvent(PABillEntryDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,selected.getValue());
				 }
			 }
			}
	}
		
	}


}
