package com.shaic.claim.productbenefit.view;

import javax.annotation.PostConstruct;

import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewProductExclusions extends ViewProductDetails {

	private static final String EXCLUSIONS = "Exclusions";

	private static final String PARTICULARS = "Particulars";

	private static final long serialVersionUID = -7689224425784709809L;

	private static final String PRODUCT_CONDITIONS = "Product Exclusions";
	VerticalLayout conditionslayout;

	@PostConstruct
	public void initView() {
		conditionslayout = new VerticalLayout();
		conditionslayout.setWidth("100.0%");
		conditionslayout.setHeight("100.0%");
		this.setCompositionRoot(conditionslayout);
	}

	public void showValues(String intimationNumber) {
		Table createViewTable = createViewTable(conditionslayout);
//		BeanItemContainer<PolicyCoditions> policyConditions = getPolicyCondition(
//				intimationNumber, PRODUCT_CONDITIONS);
//		initTable(createViewTable, policyConditions,PARTICULARS,EXCLUSIONS);
		conditionslayout.addComponent(createViewTable);

	}

}