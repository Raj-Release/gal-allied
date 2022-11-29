package com.shaic.claim.bpc;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.pedrequest.view.RevisedViewDoctorRemarksTable;
import com.shaic.claim.pedrequest.view.RevisedViewSeriousDeficiencyTable;
import com.shaic.claim.pedrequest.view.ViewSeriousDeficiencyDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ClaimService;
import com.shaic.domain.PreauthService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.util.List;
import java.util.Map;

public class HospitalProfileView extends ViewComponent{
	
private static final long serialVersionUID = 1L;
	
	
	@Inject
	private HospitalProfileDetailTable hospitalProfileDetailTable;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	
	
	private VerticalLayout mainLayout;
	
	public void init(String hospitalCode){
		hospitalProfileDetailTable.init("", false, false);
		if(hospitalCode != null) {
			DBCalculationService dbCalculationService = new DBCalculationService();
			 List<ViewBusinessProfilChartDTO> hospitalProfileList = dbCalculationService.getBusinessProfileChart(1,hospitalCode);
			if(hospitalProfileList != null && !hospitalProfileList.isEmpty()) {
				hospitalProfileDetailTable.setTableList(hospitalProfileList);
			}
		}
		mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setHeight("100%");
		mainLayout.setSpacing(true);
		hospitalProfileDetailTable.setSizeFull();
		mainLayout.addComponent(hospitalProfileDetailTable);
		setCompositionRoot(mainLayout);
	}


}
