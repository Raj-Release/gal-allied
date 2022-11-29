package com.shaic.claim.registration.previousinsurance.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
@UIScoped
public class ViewPreviousInsuranceInsuredDetails extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PreviousInsuranceInsuredDetailsTable previousInsuranceInsuredDetailsTable;
	
	@Inject
	private ViewPolicyDetails viewPolicyDetails;
	
	private VerticalLayout mainlayout;


	@PostConstruct
	public void initView() {
		this.setHeight("500px");
		this.setWidth("1000px");
		 setModal(true);
		 setClosable(true);
		 setResizable(true);	

	}

	public void showValues(
		List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuranceInsuredDetailsList) {
		
		mainlayout = new VerticalLayout();
		mainlayout.setMargin(true);
		mainlayout.setSpacing(true);
		
		previousInsuranceInsuredDetailsTable.init("", false, false);	
		previousInsuranceInsuredDetailsTable.setTableList(previousInsuranceInsuredDetailsList);	
		previousInsuranceInsuredDetailsTable.setTableSize();
		mainlayout.addComponent(previousInsuranceInsuredDetailsTable);
		setCaption("Insured Details");
		setContent(mainlayout);
		
	}

	

}
