package com.shaic.claim.premedical.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Alternative;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableCellSelectionHandler;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;
@Alternative
public class DiagnosisDetailsTable extends GEditableTable<DiagnosisDetailsTableDTO> implements TableCellSelectionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4761953963306075112L;

	private BeanItemContainer<SelectValue> icdBlock;
	
	private BeanItemContainer<SelectValue> icdCode;
	
	
	public DiagnosisDetailsTable() {
		super(DiagnosisDetailsTable.class);
		setUp();
	}
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"diagnosisName", "icdChapter", "icdBlock","icdCode", "sublimitApplicable", "sublimitName","sublimitAmt","considerForPayment", "sumInsuredRestriction" };*/

	public Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	@Override
	protected void newRowAdded() {
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		fieldMap.put("diagnosisName", new TableFieldDTO("diagnosisName", ComboBox.class , SelectValue.class, true, this));
		fieldMap.put("icdChapter", new TableFieldDTO("icdChapter", ComboBox.class,SelectValue.class, true, this));
		fieldMap.put("icdBlock", new TableFieldDTO("icdBlock", ComboBox.class, SelectValue.class, true, this ));
		fieldMap.put("icdCode", new TableFieldDTO("icdCode", ComboBox.class, SelectValue.class, true, this ));
		fieldMap.put("sublimitApplicable", new TableFieldDTO("sublimitApplicable", ComboBox.class, SelectValue.class, true, this));
		fieldMap.put("sublimitName", new TableFieldDTO("sublimitName", ComboBox.class, SelectValue.class, true, this));
		fieldMap.put("sublimitAmt", new TableFieldDTO("sublimitAmt", TextField.class, String.class, false, this));
		fieldMap.put("considerForPayment", new TableFieldDTO("considerForPayment", ComboBox.class, SelectValue.class, true, this));
		fieldMap.put("sumInsuredRestriction", new TableFieldDTO("sumInsuredRestriction", ComboBox.class, SelectValue.class, true, this));
		return fieldMap;
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}

	@Override
	public void removeRow() {
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<DiagnosisDetailsTableDTO>(DiagnosisDetailsTableDTO.class));
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"diagnosisName", "icdChapter", "icdBlock","icdCode", "sublimitApplicable", "sublimitName","sublimitAmt","considerForPayment", "sumInsuredRestriction" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setWidth("100%");
		
	}

	@Override
	public void tableSelectHandler(DiagnosisDetailsTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "premedical-diagnosis-";
	}
	
	public void validateFields() {
		/*Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}*/
	}
	
	public void setTableList(List<DiagnosisDetailsTableDTO> tableRows) {
	}

	@Override
	public void itemSelected(ComboBox field, ValueChangeEvent event) {
		System.out.println(";;;;;;;;;;;;;;;;;;;;;;; " + field.getId() + " :" + event.getProperty().getValue());
		
		if(field.getId().toLowerCase().contains("chapter")){
			
			Long chapterKey = ((SelectValue)field.getValue()).getId();

			fireViewEvent(PreMedicalPreauthWizardPresenter.GET_ICD_BLOCK,chapterKey);
			
			ComboBox p = (ComboBox) fieldMap.get("icdBlock").getField();
			
			p.setContainerDataSource(icdBlock);
		}
		
		if(field.getId().toLowerCase().contains("block"))
		{
			Long blockKey = ((SelectValue)field.getValue()).getId();
			fireViewEvent(PreMedicalPreauthWizardPresenter.GET_ICD_CODE, blockKey);
		
			ComboBox p = (ComboBox) fieldMap.get("icdCode").getField();
			
			p.setContainerDataSource(icdCode);
				
		}
		
	}
	
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockSelectValueContainer){
		icdBlock =	icdBlockSelectValueContainer;
	}
	
    public void setIcdCode(BeanItemContainer<SelectValue> icdCodeSelectValueContainer){
        
    icdCode = icdCodeSelectValueContainer;
    System.out.println("icdCode                        "+icdCode);
    
    }

}
