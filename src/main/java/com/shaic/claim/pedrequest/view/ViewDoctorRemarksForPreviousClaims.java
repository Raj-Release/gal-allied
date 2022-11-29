package com.shaic.claim.pedrequest.view;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewDoctorRemarksForPreviousClaims extends ViewComponent {
	
//	@Inject
//	private ViewDoctorRemarksTable viewDoctorRemarksTable;

	@Inject
	private RevisedViewDoctorRemarksTable viewDoctorRemarksTable;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;

	private VerticalLayout mainLayout;
	
	public void init(String intimationNo){
		
		ViewTmpIntimation viewTmpIntimation = intimationService.searchbyIntimationNoFromViewIntimation(intimationNo);
		
		viewDoctorRemarksTable.init("", false, false);
//		pedRequestDetailsTable.setTableList(viewpedRequestService.search(intimation.getKey()));
		List<ViewTmpClaim> claimByIntimation = claimService.getTmpClaimByIntimation(viewTmpIntimation.getKey());
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
//		 viewDoctorRemarksTable.setTableList(preauthService.getDoctorRemarksDetailsForPreviousClaims(claimByIntimation.get(0).getKey()));
			viewDoctorRemarksTable.setTableList(preauthService.getDoctorInternalNotesHistoryDetails(claimByIntimation.get(0).getKey()));
		}
		mainLayout = new VerticalLayout(viewDoctorRemarksTable);
		setCompositionRoot(mainLayout);
	}

}
