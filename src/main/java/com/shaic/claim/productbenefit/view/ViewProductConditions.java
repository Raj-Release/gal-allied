package com.shaic.claim.productbenefit.view;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyCondition;
import com.shaic.domain.Product;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewProductConditions extends ViewProductDetails {

	private static final String CONDITIONS = "Conditions";

	private static final String PARTICULARS = "Particulars";

	private static final long serialVersionUID = 1867571388861150404L;

	private static final String PRODUCT_CONDITIONS = "Product Conditions";
	VerticalLayout conditionslayout;

	@PostConstruct
	public void initView() {

		conditionslayout = new VerticalLayout();
		conditionslayout.setWidth("100.0%");
		conditionslayout.setHeight("100.0%");

		this.setCompositionRoot(conditionslayout);
	}

//	public void showValues(TmpPolicy policy) {
//		BeanItemContainer<PolicyCondition> policyConditions = getPolicyCondition(
//				policy);
//		Table createViewTable = createViewTable(conditionslayout);
//		initTable(createViewTable, policyConditions,PARTICULARS,CONDITIONS);
//		conditionslayout.addComponent(createViewTable);
//	}
	
	public void showValues(Product product,String policyNumber) {
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, Object> getproductUINvalues = dbCalculationService.getUINVersionNumberForrejectionCategory(0l,policyNumber,0l,0l);
		Long versionNumber =1l;
		if(getproductUINvalues != null){
			if(getproductUINvalues.containsKey("productversionNumber")){
				versionNumber = ((Long) getproductUINvalues.get("productversionNumber")); 
			}
		}
		System.out.println(String.format("Version Number in policy Condiditon [%s]", versionNumber));
		
		BeanItemContainer<PolicyCondition> policyConditions = getPolicyCondition(
				product,versionNumber);
		Table createViewTable = createViewTable(conditionslayout);
		initTable(createViewTable, policyConditions,PARTICULARS,CONDITIONS);
		conditionslayout.addComponent(createViewTable);
	}

	public void showValues(PremPolicyDetails policy) {
/*		DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, Object> getproductUINvalues = dbCalculationService.getUINVersionNumberForrejectionCategory(0l,policyNumber,0l,0l);
		Long versionNumber =1l;
		if(getproductUINvalues != null){
			if(getproductUINvalues.containsKey("productversionNumber")){
				versionNumber = ((Long) getproductUINvalues.get("productversionNumber")); 
			}
		}
		System.out.println(String.format("Version Number in policy Condiditon [%s]", versionNumber));*/
		BeanItemContainer<PolicyCondition> policyConditions = getPolicyCondition(
				policy);
		Table createViewTable = createViewTable(conditionslayout);
		initTable(createViewTable, policyConditions,PARTICULARS,CONDITIONS);
		conditionslayout.addComponent(createViewTable);
	}
	
	public void showValues(PremPolicy policy) {
		/*DBCalculationService dbCalculationService = new DBCalculationService();
		Map<String, Object> getproductUINvalues = dbCalculationService.getUINVersionNumberForrejectionCategory(0l,policyNumber,0l,0l);
		Long versionNumber =1l;
		if(getproductUINvalues != null){
			if(getproductUINvalues.containsKey("productversionNumber")){
				versionNumber = ((Long) getproductUINvalues.get("productversionNumber")); 
			}
		}
		System.out.println(String.format("Version Number in policy Condiditon [%s]", versionNumber));*/
		BeanItemContainer<PolicyCondition> policyConditions = getPolicyCondition(
				policy);
		Table createViewTable = createViewTable(conditionslayout);
		initTable(createViewTable, policyConditions,PARTICULARS,CONDITIONS);
		conditionslayout.addComponent(createViewTable);
	}
	
}