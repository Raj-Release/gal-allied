package com.shaic.claim.registration.balancesuminsured.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ViewBalenceSumInsured extends Window {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private VerticalLayout mainLayout;

	@Inject
	private BalanceSumInsured balanceSumInsured;

	public ViewBalenceSumInsured() {

	}

	@PostConstruct
	public void initView() {

		setCaption("Balance Sum Insured");

		setHeight("90%");
		setWidth("65%");
		setModal(true);
		setClosable(true);
		setResizable(true);

	}

	public void bindFieldGroup(String intimationNo, Long rodKey) {
		mainLayout = balanceSumInsured.bindFieldGroup(intimationNo, rodKey);
		setContent(mainLayout);
	}

}
