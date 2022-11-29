package com.shaic.claim.registration.balancesuminsured.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.OPClaim;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.VerticalLayout;

public class OPBalanceSumInsured extends ViewComponent {
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	
	private Boolean isHealthcheckUp = true;
	
	private String claimTypes;
	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private ClaimService claimService;
	@Inject
	private OPBalanceSumInsuredTable balanceSUmInsuredTable;

	OPClaim claim;

	Double provisionAmount;

	public void init(Long claimKey, Long insuredKey, String claimType) {
		VerticalLayout buildMainLayout = buildMainLayout(claimKey, insuredKey,
				claimType,claimTypes);

		mainLayout = new VerticalLayout(buildMainLayout);
		mainLayout.setComponentAlignment(buildMainLayout,
				Alignment.MIDDLE_CENTER);

		setCompositionRoot(mainLayout);
	}

	public VerticalLayout buildMainLayout(Long claimKey, Long insuredKey,
			String claimType, String claimType2) {
		balanceSUmInsuredTable.init("", false, false);
		balanceSUmInsuredTable.setHeight("100.0%");
		balanceSUmInsuredTable.setWidth("100.0%");
		claim = claimService.getOPClaimByKey(claimKey);
		
		if(claimType.equalsIgnoreCase("OP")){
		Map<String, Object> opBalanceSumInsured = dbCalculationService
				.getOPBalanceSumInsured(claimKey, insuredKey, claimType,claim.getOpcoverSection() != null ? claim.getOpcoverSection() : "SEC-OP-COV-01");

		OPBalanceSumInsuredTableDTO obj1 = new OPBalanceSumInsuredTableDTO(
				"OP Sum insured",
				(Double) opBalanceSumInsured.get("sumInsured"));
		OPBalanceSumInsuredTableDTO obj2 = new OPBalanceSumInsuredTableDTO(
				"Balance OP Sum insured  - Carried Forward",
				(Double) opBalanceSumInsured.get("balanceOPSumImsured"));
		OPBalanceSumInsuredTableDTO obj3 = new OPBalanceSumInsuredTableDTO(
				"Claims settled  ( Current year)",
				(Double) opBalanceSumInsured.get("claimsSettled"));
		OPBalanceSumInsuredTableDTO obj4 = new OPBalanceSumInsuredTableDTO(
				"Claims Outstanding",
				(Double) opBalanceSumInsured.get("claimsOutstanding"));
		Double availableOPSumInsured = (obj1.getAmount()/* + obj2.getAmount()*/)
				- (obj3.getAmount() + obj4.getAmount());
		if (availableOPSumInsured < 0) {
			availableOPSumInsured = 0.0;
		}
		OPBalanceSumInsuredTableDTO obj5 = new OPBalanceSumInsuredTableDTO(
				"Available OP Sum insured  ", availableOPSumInsured);
		if (claimKey == 0) {
			provisionAmount = 0.0;

		} else {
//			claim = claimService.getClaimByClaimKey(claimKey);
			 claim = claimService.getOPClaimByKey(claimKey);
			provisionAmount = claim.getCurrentProvisionAmount() != null ? claim.getCurrentProvisionAmount() : 0d;
		}
		OPBalanceSumInsuredTableDTO obj6 = new OPBalanceSumInsuredTableDTO(
				"Provision for the current claim", provisionAmount);
		Double balanceOPSumInsured = (obj5.getAmount()) - (obj6.getAmount());
		if (balanceOPSumInsured < 0) {
			balanceOPSumInsured = 0.0;
		}

		OPBalanceSumInsuredTableDTO obj7 = new OPBalanceSumInsuredTableDTO(
				"Balance OP  Sum insured ", balanceOPSumInsured);
		
		List<OPBalanceSumInsuredTableDTO> tableList = new ArrayList<OPBalanceSumInsuredTableDTO>();
		tableList.add(obj1);
		tableList.add(obj2);
		tableList.add(obj3);
		tableList.add(obj4);
		tableList.add(obj5);
		tableList.add(obj6);
		tableList.add(obj7);
		balanceSUmInsuredTable.setTableList(tableList);
		VerticalLayout vertical = new VerticalLayout(balanceSUmInsuredTable);
		return vertical;
		
		}else{
			
			if(isHealthcheckUp){
				claimTypes = "OP";
			}
			
			Map<String, Object> opBalanceSumInsured = dbCalculationService
					.getOPBalanceSumInsured(claimKey, insuredKey, claimTypes,claim.getOpcoverSection() != null ? claim.getOpcoverSection() : "SEC-OP-COV-01");

			OPBalanceSumInsuredTableDTO obj1 = new OPBalanceSumInsuredTableDTO(
					"Health OP Sum insured",
					(Double) opBalanceSumInsured.get("sumInsured"));
			OPBalanceSumInsuredTableDTO obj2 = new OPBalanceSumInsuredTableDTO(
					"Balance OP Sum insured  - Carried Forward",
					(Double) opBalanceSumInsured.get("balanceOPSumImsured"));
			OPBalanceSumInsuredTableDTO obj3 = new OPBalanceSumInsuredTableDTO(
					"Claims settled  ( Current year)",
					(Double) opBalanceSumInsured.get("claimsSettled"));
			OPBalanceSumInsuredTableDTO obj4 = new OPBalanceSumInsuredTableDTO(
					"Claims Outstanding",
					(Double) opBalanceSumInsured.get("claimsOutstanding"));
			Double availableOPSumInsured = (obj1.getAmount() /*+ obj2.getAmount()*/)
					- (obj3.getAmount() + obj4.getAmount());
			if (availableOPSumInsured < 0) {
				availableOPSumInsured = 0.0;
			}
			OPBalanceSumInsuredTableDTO obj5 = new OPBalanceSumInsuredTableDTO(
					"Available OP Sum insured  ", availableOPSumInsured);
			if (claimKey == 0) {
				provisionAmount = 0.0;

			} else {
				claim = claimService.getOPClaimByKey(claimKey);
				provisionAmount = claim.getCurrentProvisionAmount();
			}
			OPBalanceSumInsuredTableDTO obj6 = new OPBalanceSumInsuredTableDTO(
					"Provision for the current claim", provisionAmount);
			Double balanceOPSumInsured = (obj5.getAmount()) - (obj6.getAmount());
			if (balanceOPSumInsured < 0) {
				balanceOPSumInsured = 0.0;
			}

			OPBalanceSumInsuredTableDTO obj7 = new OPBalanceSumInsuredTableDTO(
					"Balance OP Sum insured ", balanceOPSumInsured);
			
			List<OPBalanceSumInsuredTableDTO> tableList = new ArrayList<OPBalanceSumInsuredTableDTO>();
			tableList.add(obj1);
			tableList.add(obj2);
			tableList.add(obj3);
			tableList.add(obj4);
			tableList.add(obj5);
			tableList.add(obj6);
			tableList.add(obj7);
			balanceSUmInsuredTable.setTableList(tableList);
			VerticalLayout vertical = new VerticalLayout(balanceSUmInsuredTable);
			return vertical;
			
			
		}
		
	}

}
