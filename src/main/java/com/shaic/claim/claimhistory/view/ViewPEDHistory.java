
package com.shaic.claim.claimhistory.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.preauth.PEDEndormentTrails;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewPEDHistory extends ViewComponent{
	
private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewPedHistoryTable viewPedHistoryTable;
	
	@EJB
	private ViewClaimHistoryService viewClaimHistoryService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private PEDQueryService pedQueryService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	private VerticalLayout mainLayout;
	
	public Boolean init(Long key) {	
		
		List<PEDEndormentTrails> pedEndorsementTrails = pedQueryService.getPedEndorsementTrails(key);
		
		List<PedHistoryDTO> resultList = new ArrayList<PedHistoryDTO>();
		
		for (PEDEndormentTrails pedEndormentTrails : pedEndorsementTrails) {
			PedHistoryDTO tableDto = new PedHistoryDTO();
			tableDto.setStatus(pedEndormentTrails.getStatus());
			tableDto.setDateAndTime(pedEndormentTrails.getCreatedDate());
			tableDto.setRemarks(pedEndormentTrails.getStatusRemarks());
			tableDto.setUserName(pedEndormentTrails.getCreatedBy());
			resultList.add(tableDto);
			
		}
		
		viewPedHistoryTable.init("", false, false);
		viewPedHistoryTable.setTableList(resultList);
		
		mainLayout = new VerticalLayout(/*labelHeader,*/ viewPedHistoryTable);
		
		setCompositionRoot(mainLayout);

		return true;
//
	}
	
	
	



}

