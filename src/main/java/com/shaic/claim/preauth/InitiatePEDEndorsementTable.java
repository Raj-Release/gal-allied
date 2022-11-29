package com.shaic.claim.preauth;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.TextArea;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

public class InitiatePEDEndorsementTable extends GEditableTable<NewInitiatePedEndorsementDTO> {

	private static final long serialVersionUID = 7585650817470633555L;

	public InitiatePEDEndorsementTable() {
		super(ProcedureDTO.class);
		setUp();
	}
	
/*	public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"pedCode", "description", "ICDChapter",
		"ICDBlock", "ICDCode", "source"
		,"doctorRemarks","othersSpecify" };*/

/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

 {
	fieldMap.put("pedCode", new TableFieldDTO("pedCode",
			ComboBox.class, SelectValue.class, false));
	fieldMap.put("description", new TableFieldDTO("description",
			TextField.class, String.class, false));
	fieldMap.put("ICDChapter", new TableFieldDTO("ICDChapter",
			ComboBox.class, SelectValue.class, false));
	fieldMap.put("ICDBlock", new TableFieldDTO("ICDBlock",
			ComboBox.class, SelectValue.class, false));
	fieldMap.put("ICDCode", new TableFieldDTO("ICDCode",
			ComboBox.class, SelectValue.class, false));
	fieldMap.put("source", new TableFieldDTO("source",
			ComboBox.class, SelectValue.class, false));
	fieldMap.put("doctorRemarks", new TableFieldDTO("doctorRemarks",
			TextArea.class, String.class,false));
	fieldMap.put("othersSpecify", new TableFieldDTO("othersSpecify",
			TextField.class, String.class, false));
}*/
	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
		fieldMap.put("pedCode", new TableFieldDTO("pedCode",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("description", new TableFieldDTO("description",
				TextField.class, String.class, false));
		fieldMap.put("ICDChapter", new TableFieldDTO("ICDChapter",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("ICDBlock", new TableFieldDTO("ICDBlock",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("ICDCode", new TableFieldDTO("ICDCode",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("source", new TableFieldDTO("source",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("doctorRemarks", new TableFieldDTO("doctorRemarks",
				TextArea.class, String.class,false));
		fieldMap.put("othersSpecify", new TableFieldDTO("othersSpecify",
				TextField.class, String.class, false));
		return fieldMap;
	}

	@Override
	public void removeRow() {
		
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<NewInitiatePedEndorsementDTO>(NewInitiatePedEndorsementDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"pedCode", "description", "ICDChapter",
			"ICDBlock", "ICDCode", "source"
			,"doctorRemarks","othersSpecify" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(NewInitiatePedEndorsementDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "preauth-initiate-ped-endorsement-";
	}

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}

}
