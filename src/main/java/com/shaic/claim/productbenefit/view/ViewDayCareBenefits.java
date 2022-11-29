package com.shaic.claim.productbenefit.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.ClaimLimitService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasProcedureDaycareMapping;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewDayCareBenefits extends ViewComponent {

	//private static final String DAY_CARE_LIMITS = "Day Care Limits";

	//private static final String DAY_CARE_BENEFITS = "Day Care Benefits";

	private static final long serialVersionUID = 6966526704812690940L;

	//private static final String PRODUCT_CONDITIONS = "Day care Benefits";

	@EJB
	private PolicyService policyService;

	@EJB
	private IntimationService intimationService;
	@EJB
	private ClaimLimitService claimLimitService;

	@EJB
	private MasterService masterService;

	VerticalLayout conditionslayout;

	@Inject
	private DayCareBenefitsTable dayCareBenefitsTable;

	@PostConstruct
	public void initView() {

		conditionslayout = new VerticalLayout();

		conditionslayout.setWidth("100.0%");
		conditionslayout.setHeight("100.0%");
		this.setCompositionRoot(conditionslayout);
	}

	public void showValues(Product product) {

//		Intimation intimation = intimationService
//				.searchbyIntimationNo(intimationNumber);

		List<MasProcedureDaycareMapping> procedureDaycareMappingList = masterService
				.getProcedureDaycareMapping(product.getKey());
		List<DayCareBenefitsTableDTO> benefitsTableDTOs = new ArrayList<DayCareBenefitsTableDTO>();
		for (MasProcedureDaycareMapping mapping : procedureDaycareMappingList) {
			DayCareBenefitsTableDTO benefitsTableDTO = new DayCareBenefitsTableDTO();
			benefitsTableDTO.setDayCareBenefits(mapping.getProcedure()
					.getProcedureName());
			if (mapping.getLimit() != null) {
				benefitsTableDTO
						.setDayCareLimits(mapping.getLimit().toString());
			}
			benefitsTableDTOs.add(benefitsTableDTO);
		}

		dayCareBenefitsTable.init("", false, false);
		dayCareBenefitsTable.setTableList(benefitsTableDTOs);
		dayCareBenefitsTable.getTable().setPageLength(5);
		conditionslayout.addComponent(dayCareBenefitsTable);

	}

}