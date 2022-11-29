package com.shaic.claim.preauth;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class PEDRequestDetailsList extends GBaseTable<OldPedEndorsementDTO> {

	private static final long serialVersionUID = -4306472437526474118L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"pedSuggestion", "pedName", "repudiationLetterDate", "remarks",
			"requestorId", "requestedDate", "requestStatus", };*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<OldPedEndorsementDTO>(
				OldPedEndorsementDTO.class));
		table.setHeight("200px");
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"pedSuggestion", "pedName", "repudiationLetterDate", "remarks",
			"requestorId", "requestedDate", "requestStatus", };
		table.setVisibleColumns(NATURAL_COL_ORDER);

	}

	@Override
	public void tableSelectHandler(OldPedEndorsementDTO t) {
//		fireViewEvent(PEDEndorsementPresenter.SHOW_PED_ENDORSEMENT, t);

	}

	@Override
	public String textBundlePrefixString() {
		return "preauth-old-ped-endorsement-";
	}

}
