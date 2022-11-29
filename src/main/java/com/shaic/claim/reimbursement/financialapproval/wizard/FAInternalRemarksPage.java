package com.shaic.claim.reimbursement.financialapproval.wizard;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.reimbursement.dto.FAInternalRemarksTableDTO;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FAInternalRemarksPage extends ViewComponent {

	@Inject
	private FAInternalRemarksTable faInternalRemarksTable;

	@Inject
	private ReimbursementService reimbursementService;

	private VerticalLayout mainLayout;

	public void loadData(Long intimationKey, Long stageKey) {		
		faInternalRemarksTable.init("", false, false);
		List<FAInternalRemarksTableDTO> resultList = reimbursementService.getFAInternalRemarksTrails(intimationKey, stageKey);
		faInternalRemarksTable.setTableList(resultList);
		
		mainLayout = new VerticalLayout(faInternalRemarksTable);
		mainLayout.setSizeFull();
		setCompositionRoot(mainLayout);
	}

	public VerticalLayout showViewTrailsPopup(Long intimationKey, Long stageKey) {		
		loadData(intimationKey, stageKey);
		return mainLayout;
	}
}
