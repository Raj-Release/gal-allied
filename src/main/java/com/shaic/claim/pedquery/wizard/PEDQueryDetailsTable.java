package com.shaic.claim.pedquery.wizard;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.newcode.wizard.dto.PedQueryPageDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PEDQueryDetailsTable extends GBaseTable<PedQueryPageDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final Object[] VISIBLE_COL_ORDER = new Object[] {
		"select",
		"pedSuggestion",
		"nameOfPed",
		"repudiationLetterDate",
		"remarks",
		"requestorId",
		"requestedDate",
		"requestedStatus",
		"blank"
	};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchPEDQueryTableDTO>(SearchPEDQueryTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		
	}

	@Override
	public String textBundlePrefixString() {
		return "search-pedquery-";
	}

	@Override
	public void tableSelectHandler(PedQueryPageDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	
}
