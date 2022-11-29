package com.shaic.claim;

import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewRTAsumInsuredUI extends ViewComponent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(Long policyKey, Long insuredKey, Long claimKey){
		
		TextField txtBalanceSumInsured;
		TextField txtRtaSumInsured;
		TextField txtTotalSumInsured;
		
		
		txtBalanceSumInsured = new TextField("Balance Sum Insured");
		txtRtaSumInsured = new TextField("Additional SI for Road Traffic Accident");
		txtTotalSumInsured = new TextField("Total Balance Sum Insured");
		
		Double balanceSI = 0d;
		Double rtaSumInsured = 0d;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, Double> balanceSIViewForRTA = dbCalculationService.getBalanceSIViewForRTA(policyKey, insuredKey, claimKey);
		if(balanceSIViewForRTA.get(SHAConstants.TOTAL_BALANCE_SI) != null){
			balanceSI = balanceSIViewForRTA.get(SHAConstants.TOTAL_BALANCE_SI);
			txtBalanceSumInsured.setValue(balanceSI.toString());
			txtBalanceSumInsured.setReadOnly(true);
		}
		if(balanceSIViewForRTA.get(SHAConstants.RTA_SUM_INSURED) != null){
			rtaSumInsured = balanceSIViewForRTA.get(SHAConstants.RTA_SUM_INSURED);
			txtRtaSumInsured.setValue(rtaSumInsured.toString());
			txtRtaSumInsured.setReadOnly(true);
		}
		Double totalAmt = balanceSI + rtaSumInsured;
		txtTotalSumInsured.setValue(totalAmt.toString());
		txtRtaSumInsured.setReadOnly(true);
		
		FormLayout mainForm = new FormLayout(txtBalanceSumInsured,txtRtaSumInsured,txtTotalSumInsured);
		mainForm.addStyleName("layoutDesign");
		mainForm.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(mainForm);
		setCompositionRoot(mainVertical);
		
		
		
		
	}

}
