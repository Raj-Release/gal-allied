package com.shaic.claim.pedrequest.view;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewDoctorRemarksUI extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@Inject
//	private ViewDoctorRemarksTable viewDoctorRemarksTable;
	
	@Inject
	private RevisedViewDoctorRemarksTable viewDoctorRemarksTable;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	
	
	private VerticalLayout mainLayout;
	
	public void init(Intimation intimation){
		viewDoctorRemarksTable.init("", false, false);
//		pedRequestDetailsTable.setTableList(viewpedRequestService.search(intimation.getKey()));
		List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
		if(claimByIntimation != null && ! claimByIntimation.isEmpty()){
			
//		 viewDoctorRemarksTable.setTableList(preauthService.getDoctorRemarksDetails(claimByIntimation.get(0).getKey()));
			
			viewDoctorRemarksTable.setTableList(preauthService.getDoctorInternalNotesHistoryDetails(claimByIntimation.get(0).getKey()));
		}
		mainLayout = new VerticalLayout(viewDoctorRemarksTable);
		mainLayout.setMargin(true);
		mainLayout.setHeight("100%");
		setHeight("100%");
		setCompositionRoot(mainLayout);
	}
	
	public void clearDoctorNotePopup(){
    	if(mainLayout != null){
    		mainLayout.removeAllComponents();
    	}
    	if(this.viewDoctorRemarksTable != null){
    		this.viewDoctorRemarksTable.getTable().clear();
    	}
    }

}
