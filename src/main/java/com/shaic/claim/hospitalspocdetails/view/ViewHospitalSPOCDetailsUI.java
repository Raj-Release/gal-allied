package com.shaic.claim.hospitalspocdetails.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.preauth.view.ViewPostCashlessRemarkTable;
import com.shaic.claim.preauth.wizard.dto.ViewPccRemarksDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewHospitalSPOCDetailsUI extends ViewComponent{
	
	private VerticalLayout mainLayout;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ViewHospitalSPOCDetailsTable hospitalSPOCDetails;

	@SuppressWarnings("deprecation")
	public void init(Intimation intimation){
		hospitalSPOCDetails.init("", false, false);
		Hospitals hospSPOCDetails = claimService.getHospitalSPOCDetailsByKey(intimation.getHospital());
		if(hospSPOCDetails != null){
			List<ViewHospSPOCDetailsDTO> hospitalSPOCDetailsDTO = new ArrayList<ViewHospSPOCDetailsDTO>();		
			hospitalSPOCDetailsDTO = preauthService.getHospitalSPOCDetails(hospSPOCDetails.getKey());
	
			hospitalSPOCDetails.setTableList(hospitalSPOCDetailsDTO);
		}
		mainLayout = new VerticalLayout(hospitalSPOCDetails);
		mainLayout.setMargin(true);
		mainLayout.setHeight("50%");
		setCompositionRoot(mainLayout);
	}
}
