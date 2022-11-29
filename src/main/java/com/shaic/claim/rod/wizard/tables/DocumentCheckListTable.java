/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

/**
 * @author ntv.vijayar
 *
 */
@Alternative
public class DocumentCheckListTable extends GListenerTable<DocumentCheckListDTO> implements TableCellSelectionHandler {

	
	private static final long serialVersionUID = 1L;
	
	private List<String> errorMessages =  new ArrayList<String>();


	//public TextField dummyField;
	
	private static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"slNo",
		 "value", "mandatoryDocFlag","requiredDocType", "receivedStatus", "noOfDocuments" , "remarks"};
	
	public Object[] VISIBLE_COLUMNS_1 = new Object[] {"slNo",
			 "value", "mandatoryDocFlag","requiredDocType", "strReceivedStatus", "noOfDocuments" , "remarks"};
	
	
	
	//private BeanItemContainer<SelectValue> receivedStatusSource; 
	
	//private Map<String, Object> referenceData;

	public DocumentCheckListTable() {
		super(DocumentCheckListTable.class);
		setUp();
	}

	{
		fieldMap.put("slNo", new TableFieldDTO("slNo", TextField.class , String.class, false));
		fieldMap.put("value", new TableFieldDTO("value", TextField.class,String.class, false,60, true));
		fieldMap.put("mandatoryDocFlag", new TableFieldDTO("mandatoryDocFlag", TextField.class, String.class, false,5,true));
		fieldMap.put("requiredDocType", new TableFieldDTO("requiredDocType", TextField.class, String.class, false,15 ,true));
		fieldMap.put("receivedStatus", new TableFieldDTO("receivedStatus", ComboBox.class, SelectValue.class, true ));
		fieldMap.put("noOfDocuments", new TableFieldDTO("noOfDocuments", TextField.class, String.class, true,6,true ));
		fieldMap.put("strReceivedStatus", new TableFieldDTO("strReceivedStatus", TextField.class, String.class,true,20,true ));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class, String.class, true,100,true ));
	
	}
	
	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	public void removeItem(DocumentCheckListDTO itemId)
	{
		table.removeItem(itemId);
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<DocumentCheckListDTO> itemIds = (Collection<DocumentCheckListDTO>) table.getItemIds();
		/*Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();*/
		for (DocumentCheckListDTO bean : itemIds) {
			
		//if(null != bean && ( null != bean.getReceivedStatus() || null != bean.getNoOfDocuments() || null != bean.getRemarks()))
				{
					if(null != bean.getReceivedStatus())	
					{
						/**
						 * As a part of live issues, not applicable needs to be defaulted in document checklist table.
						 * Since this being defaulted, the remarks validation for this string needs to be removed. 
						 * This change is done as per sathish sir comments.
						 * */
						if((null != bean.getMandatoryDocFlag() || ("Y").equalsIgnoreCase(bean.getMandatoryDocFlag()))   
								&& (((ReferenceTable.ACK_DOC_NOT_RECEIVED).equals(bean.getReceivedStatus().getId()) /*|| (ReferenceTable.ACK_DOC_NOT_APPLICABLE).equals(bean.getReceivedStatus().getId())*/) && 
								(null == bean.getRemarks() || ("").equalsIgnoreCase(bean.getRemarks())))
								
								)
						{
							hasError = true;
							errorMessages.add("Item No :"+bean.getSlNo()+" Please Enter Remarks for document which are mandatory and whose status is Not Received or Not Applicable");
							break;
							
						}
						if(("Partially Received").equalsIgnoreCase(bean.getReceivedStatus().getValue().trim()))
						{
							if(null == bean.getRemarks() || ("").equals(bean.getRemarks()))
							{
								hasError = true;
								errorMessages.add("Item No :"+bean.getSlNo()+" Please Enter Remarks for document whose status is partially received");
								
							}
							if(null == bean.getNoOfDocuments())
							{
								hasError = true;
								errorMessages.add("Please Enter No of documents which are partially received");
							}
							break;
						}
						if(!("").equalsIgnoreCase(bean.getReceivedStatus().getValue()) && 
								((ReferenceTable.ACK_DOC_RECEIVED_PHOTOCOPY).equals(bean.getReceivedStatus().getId()) || (ReferenceTable.ACK_DOC_RECEIVED_ORIGINAL).equals(bean.getReceivedStatus().getId())) && null == bean.getNoOfDocuments())
						{
							hasError = true;
							errorMessages.add("Item No :"+bean.getSlNo()+" Please enter no. of documents whose status is received");
							break;
						}
					}
					else
					{
						hasError = true;
						errorMessages.add("Item No :"+bean.getSlNo()+" Please select received status");
						break;
					}
						/*if(null != bean.getReceivedStatus() && ("Partially Received").equalsIgnoreCase(bean.getReceivedStatus().getValue()))
						{
							if(null == bean.getRemarks() || ("").equals(bean.getRemarks()))
							{
								hasError = true;
								errorMessages.add("Please Enter Remarks for document whose status is partially received");
							}
							if(null == bean.getNoOfDocuments())
							{
								hasError = true;
								errorMessages.add("Please Enter Remarks No of documents which are partially received");
							}
								
						}
						if((null != bean.getReceivedStatus()) && !("").equalsIgnoreCase(bean.getReceivedStatus().getValue()) && 
								(("Received").equalsIgnoreCase(bean.getReceivedStatus().getValue())) && null == bean.getNoOfDocuments())
						{
							hasError = true;
							errorMessages.add("Please enter no of documents");
						}*/
				}
		}
			return !hasError;
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}

	@Override
	public void initTable() {
		
		table.removeAllItems();
		BeanItemContainer<DocumentCheckListDTO> beanItemContainer = new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class);
		table.setContainerDataSource(beanItemContainer);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		table.setHeight("500px");
		table.setNullSelectionItemId(false);
		
		/*TableFieldDTO tableCell = fieldMap.get("receivedStatus");
		tableCell.getField();*/
	}
	
	public void setVisibleColumn(){
		table.setVisibleColumns(VISIBLE_COLUMNS_1);
		
		table.setColumnHeader("strReceivedStatus", "Received Status");
		
	}

	@Override
	public void tableSelectHandler(DocumentCheckListDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "document-checklist-";
	} 
	
	public void setReference(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;
		super.setReference(referenceData, this);
		
		//setDefaultDropDownValue(referenceData);
	}

	/*private void addListener()
	{
		TableFieldDTO tableFieldDTO = fieldMap.get("receivedStatus");
		ComboBox cmb = (ComboBox)tableFieldDTO.getField();
		TableFieldDTO tableFieldDTO1 = fieldMap.get("remarks");
		TextField txtfld = (TextField)tableFieldDTO1.getField();
		
		
	}*/
	
	public void setReceivedStatus(BeanItemContainer<SelectValue> receivedStatusSource)
	{
		//this.receivedStatusSource = receivedStatusSource;
	}
	
	@Override
	public List<DocumentCheckListDTO> getValues() 
	{
		Collection<DocumentCheckListDTO> coll = (Collection<DocumentCheckListDTO>) table.getItemIds();
		List list;
		if (coll instanceof List){
			list = (List)coll;
		}
		else{
			list = new ArrayList(coll);
		}
		return list;
	}
	

}
