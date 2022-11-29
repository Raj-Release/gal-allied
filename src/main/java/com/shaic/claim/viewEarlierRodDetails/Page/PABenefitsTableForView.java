package com.shaic.claim.viewEarlierRodDetails.Page;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.ReferenceTable;
import com.shaic.paclaim.rod.wizard.dto.PABenefitsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PABenefitsTableForView extends GBaseTable<PABenefitsDTO> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"benefitCover",
		"percentage", "sumInsured", "eligibleAmount" };*/
	
	/*public static final Object[] NATURAL_COL_ORDER_TTD = new Object[] {"benefitCover",
		"noOfWeeks", "sumInsured", "eligibleAmountPerWeek", "totalEligibleAmount" };*/
	
	private Long benefitsId;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	public void initReferenceId(Long benefitsId)
	{
		this.benefitsId = benefitsId;
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<PABenefitsDTO>(
				PABenefitsDTO.class));
		if(null != benefitsId && benefitsId.equals(ReferenceTable.BENEFITS_TTD_MASTER_VALUE)){
			 Object[] NATURAL_COL_ORDER_TTD = new Object[] {"benefitCover",
				"noOfWeeks", "sumInsured", "eligibleAmountPerWeek", "totalEligibleAmount" };
					table.setVisibleColumns(NATURAL_COL_ORDER_TTD);
		}
		else
		{
			 Object[] NATURAL_COL_ORDER = new Object[] {"benefitCover",
				"percentage", "sumInsured", "eligibleAmount" };
			table.setVisibleColumns(NATURAL_COL_ORDER);
		}
		table.setHeight("200px");

	}

	@Override
	public void tableSelectHandler(PABenefitsDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view-pa-benefits-";
	}

}
