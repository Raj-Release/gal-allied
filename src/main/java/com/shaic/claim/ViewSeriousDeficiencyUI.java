package com.shaic.claim;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.pedrequest.view.RevisedViewDoctorRemarksTable;
import com.shaic.claim.pedrequest.view.RevisedViewSeriousDeficiencyTable;
import com.shaic.claim.pedrequest.view.ViewSeriousDeficiencyDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.VerticalLayout;

public class ViewSeriousDeficiencyUI extends ViewComponent {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private RevisedViewDoctorRemarksTable viewDoctorRemarksTable;
	
	@Inject
	private RevisedViewSeriousDeficiencyTable viewSeriousDeficiencyTable;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	
	
	private VerticalLayout mainLayout;
	
	public void init(String hospitalCode){
		viewSeriousDeficiencyTable.init("", false, false);
/*		List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){						
			viewSeriousDeficiencyTable.setTableList(preauthService.getDoctorInternalNotesHistoryDetails(claimByIntimation.get(0).getKey()));
		}
*/		if(hospitalCode != null) {
			DBCalculationService dbCalculationService = new DBCalculationService();
			List<ViewSeriousDeficiencyDTO> seriousDeficiencyIntimationList = dbCalculationService.getSDIntimationListByHospitalCode(hospitalCode);
			if(seriousDeficiencyIntimationList != null && !seriousDeficiencyIntimationList.isEmpty()) {
				viewSeriousDeficiencyTable.setTableList(seriousDeficiencyIntimationList);
			}
		}
		//mainLayout = new VerticalLayout(viewSeriousDeficiencyTable);
mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setHeight("100%");
//		setHeight("100%");
		mainLayout.setSpacing(true);
		viewSeriousDeficiencyTable.setSizeFull();
		mainLayout.addComponent(viewSeriousDeficiencyTable);
		setCompositionRoot(mainLayout);
	}

}
