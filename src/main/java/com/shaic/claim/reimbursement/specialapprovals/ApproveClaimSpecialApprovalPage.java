package com.shaic.claim.reimbursement.specialapprovals;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.viewEarlierRodDetails.Table.HospitalisationTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewPreHospitalizationTable;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class ApproveClaimSpecialApprovalPage extends ViewComponent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private RevisedCashlessCarousel preauthIntimationDetailsCarousel;
	
	@Inject
	private HospitalisationTable hospitalisationTable;
	
	@Inject
	private ViewPreHospitalizationTable viewPreHospitalizationTable;
	
	@Inject
	private ViewPreHospitalizationTable viewPostHospitalizationTable;
	
	private Button btnViewMedicalSummary;
	
	private Button btnViewIRDABillSummary;
	
	private Button btnBillSummary;
	
	private Button btnViewEarlierRODDetails;
	
	private VerticalLayout mainLayout;
	
	public void init(ApproveClaimSpecialApprovalDTO approveClaimSpecialApprovalDTO){
		approveClaimSpecialApprovalDTO.setIntimationId("CLI/2015/121215/0001049");
		viewDetails.initView(approveClaimSpecialApprovalDTO.getIntimationId(), ViewLevels.INTIMATION, true,"");
		HorizontalLayout btnLayout1 = new HorizontalLayout(btnViewMedicalSummary, btnViewIRDABillSummary);
		HorizontalLayout btnLayout2 = new HorizontalLayout(btnBillSummary, btnViewEarlierRODDetails);
		hospitalisationTable.init();
		viewPreHospitalizationTable.init();
		viewPostHospitalizationTable.init();
		HorizontalLayout amtClaimedLayout = new HorizontalLayout(hospitalisationTable, viewPreHospitalizationTable, viewPostHospitalizationTable);
		amtClaimedLayout.setSpacing(true);
		mainLayout = new VerticalLayout(viewDetails, btnLayout1, btnLayout2, amtClaimedLayout);
		setCompositionRoot(mainLayout);
	}
	
}
