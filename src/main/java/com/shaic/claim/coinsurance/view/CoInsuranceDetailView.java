package com.shaic.claim.coinsurance.view;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class CoInsuranceDetailView extends ViewComponent{

	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;


	@Inject
	private CoInsuranceTable coInsuranceTable;
	
	
	public void init(List<CoInsuranceTableDTO> coInsuranceList){

		coInsuranceTable.init("", false, false);
		coInsuranceTable.setTableList(coInsuranceList);
		mainLayout = new VerticalLayout(coInsuranceTable);
		setCompositionRoot(mainLayout);
	}
}
