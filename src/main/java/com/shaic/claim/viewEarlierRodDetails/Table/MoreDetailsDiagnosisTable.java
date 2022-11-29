package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.ArrayList;
import java.util.Collection;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class MoreDetailsDiagnosisTable extends GBaseTable<DiagnosisDetailsTableDTO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber",
		"diagnosis","sublimitApplicableValue", "sublimitNameValue","sublimitAmt","considerForPaymentValue", "sumInsuredRestrictionValue" };
	*/
	/*public static final Object[] NATURAL_COLUMNS = new Object[] {"serialNumber",
		"diagnosis","sublimitApplicableValue", "sublimitNameValue","sublimitAmt","exclusionDiagnosis","exclusionDetailsValue","coPayValue","considerForPaymentValue", "sumInsuredRestrictionValue","diagnosisRemarks" };*/
	
	/*public static final Object[] MORE_COLUMNS = new Object[] {"serialNumber",
		"diagnosis","icdChapterValue","icdBlockValue","icdCodeValue","sublimitApplicableValue", "sublimitNameValue","sublimitAmt","exclusionDiagnosis","exclusionDetailsValue","coPayValue","considerForPaymentValue", "sumInsuredRestrictionValue","diagnosisRemarks" };*/
	
	
	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();

		BeanItemContainer<DiagnosisDetailsTableDTO> diagnosisDetailsContainer = new BeanItemContainer<DiagnosisDetailsTableDTO>(
				DiagnosisDetailsTableDTO.class); 
		
		diagnosisDetailsContainer.addNestedContainerProperty("pedImpactOnDiagnosis.value");
		diagnosisDetailsContainer.addNestedContainerProperty("reasonForNotPaying.value");
		table.setContainerDataSource(diagnosisDetailsContainer);
		
		 Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","primaryDiagnosis",
			"diagnosis","sublimitApplicableValue", "sublimitNameValue","sublimitAmt","considerForPaymentValue", "sumInsuredRestrictionValue" };
		
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		table.setWidth("100%");
		
		
	}

	@Override
	public void tableSelectHandler(DiagnosisDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public void setVisibleColumn(){
		 Object[] NATURAL_COLUMNS = new Object[] {"serialNumber","primaryDiagnosis",
			"diagnosis","sublimitApplicableValue", "sublimitNameValue","sublimitAmt","exclusionDiagnosis","exclusionDetailsValue","coPayValue",
			"considerForPaymentValue", "sumInsuredRestrictionValue","diagnosisRemarks" };
		table.setVisibleColumns(NATURAL_COLUMNS);
		table.setColumnHeader("exclusionDiagnosis", "Exclusion Details");
		table.setColumnHeader("exclusionDetailsValue", "Exclusion Details Value");
		table.setColumnHeader("diagnosisRemarks", "Diagnosis Remarks");
		table.setColumnHeader("coPayValue", "Co-Pay");
		
		table.removeGeneratedColumn("primaryDiagnosis");
		table.addGeneratedColumn("primaryDiagnosis", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				final DiagnosisDetailsTableDTO dto = (DiagnosisDetailsTableDTO) itemId;
				
				final OptionGroup optionType = new OptionGroup();
				optionType.addItems(getReadioButtonOptions());
				optionType.setItemCaption(Boolean.valueOf("true"), "");
				optionType.setEnabled(false);
				optionType.setStyleName("inlineStyle");
				optionType.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
				 if(dto.getPrimaryDiagnosis() != null){
					 if(dto.getPrimaryDiagnosis().booleanValue()){
						 optionType.select(Boolean.valueOf("true"));
					 }
				 }else{
					 optionType.select(null);
				 }
						return optionType;
				
		}
		});
		
	}
	
	public void setMoreColumn(){
		Object[] MORE_COLUMNS = new Object[] {"serialNumber","primaryDiagnosis",
			"diagnosis",/*"icdChapterValue","icdBlockValue",*/"icdCodeValue","pedImpactOnDiagnosis.value", "sublimitApplicableValue", "sublimitNameValue","sublimitAmt","exclusionDiagnosis","exclusionDetailsValue","coPayValue","considerForPaymentValue",
			"reasonForNotPaying.value", "sumInsuredRestrictionValue","diagnosisRemarks" };
		table.setVisibleColumns(MORE_COLUMNS);
		table.setColumnHeader("exclusionDiagnosis", "Exclusion Details");
		table.setColumnHeader("exclusionDetailsValue", "Exclusion Details Value");
		table.setColumnHeader("diagnosisRemarks", "Diagnosis Remarks");
		table.setColumnHeader("coPayValue", "Co-Pay");
		table.setColumnHeader("icdChapterValue","Icd Chapter");
		table.setColumnHeader("icdBlockValue", "Icd Block");
		table.setColumnHeader("icdCodeValue", "Icd Code");
		table.setColumnHeader("pedImpactOnDiagnosis.value", "Ped Impact On Diagnosis"); 
		table.setColumnHeader("reasonForNotPaying.value", "Reason for not Paying");
	}
 
	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-diagnosis-table-";
	}
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(1);
		coordinatorValues.add(true);

		return coordinatorValues;
	}

}
