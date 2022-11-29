package com.shaic.claim.bpc;

import org.vaadin.addon.cdimvp.ViewComponent;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;


import com.shaic.domain.ClaimService;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.VerticalLayout;

public class AgentProfileView extends ViewComponent{

	private static final long serialVersionUID = 1L;


	@Inject
	private AgentProfileDetailTable agentProfileDetailTable;

	@EJB
	private PreauthService preauthService;

	@EJB
	private ClaimService claimService;



	private VerticalLayout mainLayout;

	public void init(String agentCode){
		agentProfileDetailTable.init("", false, false);
			if(agentCode != null) {
			 DBCalculationService dbCalculationService = new DBCalculationService();
			 List<ViewBusinessProfilChartDTO> salesManagerProfileList = dbCalculationService.getBusinessProfileChart(3,agentCode);
			 //List<ViewBusinessProfilChartDTO> salesManagerProfileList = dbCalculationService.getSDIntimationListByHospitalCodeForBPC(salesManagerCode);
			 if(salesManagerProfileList != null && !salesManagerProfileList.isEmpty()) {
				 agentProfileDetailTable.setTableList(salesManagerProfileList);
			 }
		 }
		 mainLayout = new VerticalLayout();
		 mainLayout.setMargin(true);
		 mainLayout.setHeight("100%");
		 mainLayout.setSpacing(true);
		 agentProfileDetailTable.setSizeFull();
		 mainLayout.addComponent(agentProfileDetailTable);
		 setCompositionRoot(mainLayout);
	}


}
