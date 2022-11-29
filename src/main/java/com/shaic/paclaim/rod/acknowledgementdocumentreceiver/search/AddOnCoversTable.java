package com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.rod.createrod.search.PACreateRODDocumentDetailsPresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillEntryDocumentDetailsPresenter;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class AddOnCoversTable extends GListenerTable<AddOnCoversTableDTO> implements TableCellSelectionHandler{

	
	private static final long serialVersionUID = -2239330323400228835L;
	private String presenterString;

	private List<AddOnCoversTableDTO> deltedAddOnCoverList;
	
	public AddOnCoversTable() {
		super(AddOnCoversTable.class);
		setUp();
		deltedAddOnCoverList = new ArrayList<AddOnCoversTableDTO>();
	}
	
	
	
	private List<String> errorMessages;
	
	
	
//	public NewBornBabyTable(NewBabyIntimationDto bean) {50005562
//		super(NewBornBabyTable.class);
//		setUp();
//	}
	
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"covers", "claimedAmount" };*/

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	 {
	
		//fieldMap.put("S.No", new TableFieldDTO("serialNumber", TextField.class, String.class, true));
		fieldMap.put("covers", new TableFieldDTO("covers", ComboBox.class, SelectValue.class, true,true,800,this));
		fieldMap.put("claimedAmount", new TableFieldDTO("claimedAmount",TextField.class,String.class, true,15,true)); 
	}*/
	
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
//		table.setColumnWidth("covers", 550);
		table.setColumnWidth("claimedAmount", 150);
		
		
		table.setContainerDataSource(new BeanItemContainer<AddOnCoversTableDTO>(AddOnCoversTableDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {
			"covers", "claimedAmount" };
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
				        	deltedAddOnCoverList.add((AddOnCoversTableDTO)itemId);
				        	 deleteRow(itemId);
				        } 
				    });
			    	return deleteButton;
			      };
			});
		}
		
		table.setColumnWidth("Delete", 100);
		table.setWidth("600px");
//		table.setSizeFull();
		table.setPageLength(5);
	}

	
	@Override
	public void tableSelectHandler(AddOnCoversTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "addoncoverstable-";
	}
	
	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();			
			
			fieldMap.put("covers", new TableFieldDTO("covers", ComboBox.class, SelectValue.class, true,true,800,this));
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
			 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,deletedDTO.getCovers(),true);
		 }else if(presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)){
			 fireViewEvent(PACreateRODDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,deletedDTO.getCovers(),true);
		 }else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
			// fireViewEvent(PABillEntryDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,selected.getValue());
		 }
	}

	public List<AddOnCoversTableDTO> getDeltedAddOnCoversList() {
		return deltedAddOnCoverList;
	}

	public void setDeltedAddOnCoversList(List<AddOnCoversTableDTO> deltedCoverList) {
		this.deltedAddOnCoverList = deltedCoverList;
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
					if(null != bean.getCovers()) 
					{
						if(null != duplicateItemMap && !duplicateItemMap.isEmpty())
						{
							if(duplicateItemMap.containsKey(bean.getCovers().getId()))
							{
								hasError = true;
							
								{
									errorMessages.add("Same covers value cannot be selected twice in Add on Covers Table. Please change the covers to proceed further");
								}
								
								break;
							}
							
						}
							duplicateItemMap.put(bean.getCovers().getId(), bean.getCovers().getValue());
					}
					else
					{
						hasError = true;
						errorMessages.add("Covers value cannot be empty in Add on Covers Table. Please select the covers value in row " + bean.getSlNo() +" to proceed further");
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
	 
	 
	 public void setReference(Map<String, Object> referenceData) {
			super.setReference(referenceData, this);
		}
	 
	 
	 public void setScreenName(String presenterString){
		 this.presenterString = presenterString;
	 }

	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		
		if(event != null && event.getProperty() != null){
				if(event.getProperty() instanceof ComboBox){
				 ComboBox comboBox = (ComboBox)event.getProperty();
				 if(comboBox != null && comboBox.getValue() != null){
					 SelectValue selected = (SelectValue)comboBox.getValue();
					 if(selected.getId() != null && selected.getId().equals(ReferenceTable.EARNING_PARENT_SI)){
						 validateEarningParent(comboBox);
					 }
					 if(presenterString.equalsIgnoreCase(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED)){
						 fireViewEvent(PAAcknowledgementDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,selected,false);
					 }else if(presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)){
						 fireViewEvent(PACreateRODDocumentDetailsPresenter.RESET_PARTICULARS_VALUES_FOR_ADD_ON_COVER,selected,false);
					 }else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
						 fireViewEvent(PABillEntryDocumentDetailsPresenter.RESET_PARTICULARS_VALUES,selected);
					 }
				 }
				}
		}
		
	}
	
	public void validateEarningParent(ComboBox field)
	{
		List<AddOnCoversTableDTO> addOnCoversValue = (List<AddOnCoversTableDTO>) this.table.getItemIds();
		for (AddOnCoversTableDTO addOnCoversTableDTO : addOnCoversValue) {
			if(addOnCoversTableDTO.getCovers() != null && addOnCoversTableDTO.getCovers().getValue() != null){
				SelectValue selected = (SelectValue)field.getValue();
				if(selected.getId() != null && selected.getId().equals(ReferenceTable.EARNING_PARENT_SI)){
					if(! selected.getId().equals(addOnCoversTableDTO.getCovers().getId())){
						field.setValue(null);
						showErrorMessage("");
						break;
					}
				}
			}
	}
	 
	}
	
	private void showErrorMessage(String eMsg) {

		/*Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

}
