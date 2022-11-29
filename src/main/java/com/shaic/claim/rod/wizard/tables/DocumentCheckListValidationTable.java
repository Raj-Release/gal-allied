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

import com.shaic.arch.table.GListenerTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;

/**
 * @author ntv.vijayar
 *
 */
@Alternative
public class DocumentCheckListValidationTable extends GListenerTable<DocumentCheckListDTO> implements TableCellSelectionHandler {

	
	private static final long serialVersionUID = 1L;

	//public TextField dummyField;
	
	private static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
	private List<String> errorMessages =  new ArrayList<String>();

	
	public Object[] VISIBLE_COLUMNS = new Object[] {"slNo",
		 "value", "mandatoryDocFlag","requiredDocType", "ackReceivedStatus", "noOfDocuments" , "remarks" ,"rodReceivedStatus", "rodRemarks"};
	

	public DocumentCheckListValidationTable() {
		super(DocumentCheckListValidationTable.class);
		setUp();
	}

	{
		fieldMap.put("slNo", new TableFieldDTO("slNo", TextField.class , String.class,false));
		fieldMap.put("value", new TableFieldDTO("value", TextField.class,String.class, false));
		fieldMap.put("mandatoryDocFlag", new TableFieldDTO("mandatoryDocFlag", TextField.class, String.class, false));
		fieldMap.put("requiredDocType", new TableFieldDTO("requiredDocType", TextField.class, String.class, false ));
		fieldMap.put("ackReceivedStatus", new TableFieldDTO("ackReceivedStatus", TextField.class, String.class, false ));
		fieldMap.put("noOfDocuments", new TableFieldDTO("noOfDocuments", TextField.class, String.class, false ));
		fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class, String.class, false ));
		fieldMap.put("rodReceivedStatus", new TableFieldDTO("rodReceivedStatus", OptionGroup.class, Boolean.class, true, 3 ));
		//fieldMap.put("rodReceivedStatus", new TableFieldDTO("rodReceivedStatus", getOptionGroup(), Boolean.class, true ));
		fieldMap.put("rodRemarks", new TableFieldDTO("rodRemarks", TextField.class, String.class, true, 100 ));
		
	
	}
	
	public Class getOptionGroup()
	{
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.addItems(getReadioButtonOptions());
		optionGroup.setItemCaption(true, "Yes");
		optionGroup.setItemCaption(false, "No");
		optionGroup.setStyleName("horizontal");
		optionGroup.setRequired(true);
		
		return (optionGroup).getClass();
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		
		BeanItemContainer<DocumentCheckListDTO> beanItemContainer = new BeanItemContainer<DocumentCheckListDTO>(DocumentCheckListDTO.class);
		table.setContainerDataSource(beanItemContainer);
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		//table.setHeight("70%");
		System.out.println("--the size---"+table.getItemIds().size());
		//table.setPageLength(table.getItemIds().size());
	
	}

	@Override
	public void tableSelectHandler(DocumentCheckListDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "rod-checklist-validation-";
	} 
	
	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData, this);
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
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		@SuppressWarnings("unchecked")
		Collection<DocumentCheckListDTO> itemIds = (Collection<DocumentCheckListDTO>) table.getItemIds();
		/*Map<Long, String> valuesMap = new HashMap<Long, String>();
		Map<Long, String> validationMap = new HashMap<Long, String>();*/
		for (DocumentCheckListDTO bean : itemIds) {
			
			if((null != bean.getAckReceivedStatus() && ("Received").equalsIgnoreCase(bean.getAckReceivedStatus())) && ("N").equalsIgnoreCase(bean.getRodReceivedStatusFlag()))
			{
				if(null == bean.getRodRemarks() || ("").equalsIgnoreCase(bean.getRodRemarks()))
				{
					hasError = true;
					errorMessages.add("Please Enter ROD Remarks if acknowledgement status is Received and rod status is No");
				}
			}
			
			else if((null != bean.getAckReceivedStatus() && ("Not Received").equalsIgnoreCase(bean.getAckReceivedStatus())) && ("Y").equalsIgnoreCase(bean.getRodReceivedStatusFlag()))
			{
				if(null == bean.getRodRemarks() || ("").equalsIgnoreCase(bean.getRodRemarks()))
				{
					hasError = true;
					errorMessages.add("Please Enter ROD Remarks if acknowledgement status is Not Received and rod status is Yes");
				}
			}
			
			if((null != bean.getAckReceivedStatus() && !("").equalsIgnoreCase(bean.getAckReceivedStatus()) && !("Not Applicable").equalsIgnoreCase(bean.getAckReceivedStatus())
					))
			{
				if(null == bean.getRodReceivedStatus())
				{
					hasError = true;
					errorMessages.add("Please select ROD Received Status");
				}
			}
			
			
			
			
			/*Set<ConstraintViolation<ProcedureDTO>> validate = validator.validate(bean);

			if(bean.getSublimitName() != null) {
				if(valuesMap.containsKey(bean.getSublimitName().getLimitId()) && (bean.getConsiderForPayment() == null || (null != bean.getConsiderForPayment() && bean.getConsiderForPayment().getId().equals(ReferenceTable.COMMONMASTER_YES)))) {
					validationMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				} else {
					valuesMap.put(bean.getSublimitName().getLimitId(), bean.getSublimitName().getLimitId().toString());
				}
			}
			if (validate.size() > 0) {
				hasError = true;
				for (ConstraintViolation<ProcedureDTO> constraintViolation : validate) {
					errorMessages.add(constraintViolation.getMessage());
				}
			}
		}
		if(!validationMap.isEmpty()) {
			hasError = true;
			errorMessages.add("Procedure Table - Consider For Payment cannot be yes for all the entries for which Same Sublimit is seleced .");
		}
*/		}
			return !hasError;
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
}
	
