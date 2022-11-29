package com.shaic.claim.reimbursement.billing.wizard;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.reimbursement.billing.dto.BillingInternalRemarksTableDTO;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BillingInternalRemarksPage extends ViewComponent {

	@Inject
	private BillingInternalRemarksTable billingInternalRemarksTable;

	@Inject
	private ReimbursementService reimbursementService;

	private VerticalLayout mainLayout;

	public void loadData(Long intimationKey, Long stageKey) {		
		billingInternalRemarksTable.init("", false, false);
		List<BillingInternalRemarksTableDTO> resultList = reimbursementService.getBillingInternalRemarksTrails(intimationKey, stageKey);
		billingInternalRemarksTable.setTableList(resultList);
		mainLayout = new VerticalLayout(billingInternalRemarksTable);
		setCompositionRoot(mainLayout);
	}

	public VerticalLayout showViewTrailsPopup(Long intimationKey, Long stageKey) {		
		loadData(intimationKey, stageKey);
		return mainLayout;
	}
}