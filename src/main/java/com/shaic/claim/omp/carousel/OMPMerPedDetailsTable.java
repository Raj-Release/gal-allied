package com.shaic.claim.omp.carousel;

import com.shaic.arch.table.GBaseTable;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPMerPedDetailsTable extends GBaseTable<OMPMerDetailTableDto>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		}

		@Override
		public void initTable() {
			table.removeAllItems();
			table.setWidth("100%");
			table.setContainerDataSource(new BeanItemContainer<OMPMerDetailTableDto>(OMPMerDetailTableDto.class));
			Object[] NATURAL_COL_ORDER = new Object[] {"pedCode","pedDescription","icdChapterCode","icdBlockCode","icdBlockDesc","icdCode","icdCodeDesc","remarks"};
			table.setVisibleColumns(NATURAL_COL_ORDER);
			table.setPageLength(table.size() + 4);
			table.setHeight("200px");
		}

		@Override
		public void tableSelectHandler(OMPMerDetailTableDto t) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String textBundlePrefixString() {
			// TODO Auto-generated method stub
			return "ompmerpeds-details-";
		}
	

}
