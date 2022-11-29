package com.shaic.claim.processdatacorrection.viewtable;

import java.util.ArrayList;
import java.util.Collection;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.processdatacorrection.dto.DiganosisCorrectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ViewDiganosisCorrectionTable extends GBaseTable<DiganosisCorrectionDTO>{

	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		if(table!=null){
			table.clear();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<DiganosisCorrectionDTO>(DiganosisCorrectionDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"serialNo","primaryDiagnosis","diagnosisName","proposeddiagnosisName","icdCode","proposedicdCode"};

		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setColumnWidth("serialNo",80);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.removeGeneratedColumn("primaryDiagnosis");
		table.addGeneratedColumn("primaryDiagnosis", new Table.ColumnGenerator() {

			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {

				final DiganosisCorrectionDTO dto = (DiganosisCorrectionDTO) itemId;
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
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(1);
		coordinatorValues.add(true);

		return coordinatorValues;
	}

	@Override
	public void tableSelectHandler(DiganosisCorrectionDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-diganosis-correction-table-";
	}
}
