package com.shaic.claim.viewEarlierRodDetails;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class ViewDiagnosisDetailsTable extends GBaseTable<DiagnosisDetailsTableDTO> {
	

	private static final long serialVersionUID = 1L;
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"diagnosisName.value","icdChapter.value","icdBlock.value","icdCode.value","sublimitApplicable.value"
		,"sublimitName.name","sublimitAmt","considerForPayment.value","sumInsuredRestriction"};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		BeanItemContainer<DiagnosisDetailsTableDTO> beanItemContainer = new BeanItemContainer<DiagnosisDetailsTableDTO>(DiagnosisDetailsTableDTO.class);
		beanItemContainer.addNestedContainerProperty("diagnosisName.value");
		beanItemContainer.addNestedContainerProperty("icdChapter.value");
		beanItemContainer.addNestedContainerProperty("icdBlock.value");
		beanItemContainer.addNestedContainerProperty("icdCode.value");
		beanItemContainer.addNestedContainerProperty("sublimitApplicable.value");
		beanItemContainer.addNestedContainerProperty("sublimitName.name");
		beanItemContainer.addNestedContainerProperty("sublimitAmt");
		beanItemContainer.addNestedContainerProperty("considerForPayment.value");
		beanItemContainer.addNestedContainerProperty("sumInsuredRestriction");
		table.setContainerDataSource(beanItemContainer);
		Object[] NATURAL_COL_ORDER = new Object[] {"diagnosisName.value","icdChapter.value","icdBlock.value","icdCode.value","sublimitApplicable.value"
			,"sublimitName.name","sublimitAmt","considerForPayment.value","sumInsuredRestriction"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(DiagnosisDetailsTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		
	      return "view-diagnosis-details-table-";
		
	}
	

}
