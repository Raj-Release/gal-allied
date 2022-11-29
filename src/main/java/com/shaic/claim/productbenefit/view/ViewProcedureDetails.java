package com.shaic.claim.productbenefit.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.ClaimLimitService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasProcedureDaycareMapping;
import com.shaic.domain.MasProcedureDetailsMapping;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewProcedureDetails extends ViewComponent{
	
	@EJB
	private PolicyService policyService;

	
	private IntimationService intimationService;
	@EJB
	private ClaimLimitService claimLimitService;

	@EJB
	private MasterService masterService;

	VerticalLayout conditionslayout;
	
	@Inject
	private ProcedureDetailsTable procedureDetailsTable;
	
	@PostConstruct
	public void initView() {

		conditionslayout = new VerticalLayout();

		conditionslayout.setWidth("100.0%");
		conditionslayout.setHeight("100.0%");
		this.setCompositionRoot(conditionslayout);
	}

	public void showValues(Product product,String policyNumber) {
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, Object> getproductUINvalues = dbCalculationService.getUINVersionNumberForrejectionCategory(0l,policyNumber,0l,0l);
		Long versionNumber =1l;
		if(getproductUINvalues != null){
			if(getproductUINvalues.containsKey("productversionNumber")){
				versionNumber = ((Long) getproductUINvalues.get("productversionNumber")); 
			}
		}
		System.out.println(String.format("Version Number in product benefits [%s]", versionNumber));

		List<MasProcedureDetailsMapping> procedureDetailsMappingList = masterService
				.getProcedureDetailsMappingwithVersion(product.getKey(),versionNumber);
		List<ProcedureDetailsTableDTO> benefitsTableDTOs = new ArrayList<ProcedureDetailsTableDTO>();
		for (MasProcedureDetailsMapping procedureMapping : procedureDetailsMappingList) {
			ProcedureDetailsTableDTO benefitsTableDTO = new ProcedureDetailsTableDTO();
			benefitsTableDTO.setProcedureDetails(procedureMapping.getProcedureName());
			benefitsTableDTOs.add(benefitsTableDTO);
		}

		procedureDetailsTable.init("", false, false);
		procedureDetailsTable.setTableList(benefitsTableDTOs);
		procedureDetailsTable.getTable().setPageLength(5);
		conditionslayout.addComponent(procedureDetailsTable);

	}

}

