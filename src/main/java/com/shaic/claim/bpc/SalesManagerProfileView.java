package com.shaic.claim.bpc;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.ClaimService;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.VerticalLayout;

public class SalesManagerProfileView extends ViewComponent{

	private static final long serialVersionUID = 1L;


	@Inject
	private SalesManagerProfileDetailTable salesManagerProfileDetailTable;

	@EJB
	private PreauthService preauthService;

	@EJB
	private ClaimService claimService;



	private VerticalLayout mainLayout;

	public void init(String salesManagerCode){
		salesManagerProfileDetailTable.init("", false, false);
			if(salesManagerCode != null) {
			 DBCalculationService dbCalculationService = new DBCalculationService();
			 List<ViewBusinessProfilChartDTO> salesManagerProfileList = dbCalculationService.getBusinessProfileChart(2,salesManagerCode);
			 //List<ViewBusinessProfilChartDTO> salesManagerProfileList = dbCalculationService.getSDIntimationListByHospitalCodeForBPC(salesManagerCode);
			 if(salesManagerProfileList != null && !salesManagerProfileList.isEmpty()) {
				 salesManagerProfileDetailTable.setTableList(salesManagerProfileList);
			 }
		 }
		 mainLayout = new VerticalLayout();
		 mainLayout.setMargin(true);
		 mainLayout.setHeight("100%");
		 mainLayout.setSpacing(true);
		 salesManagerProfileDetailTable.setSizeFull();
		 mainLayout.addComponent(salesManagerProfileDetailTable);
		 setCompositionRoot(mainLayout);
	}

}
