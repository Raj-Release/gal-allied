package com.shaic.claim.productbenefit.view;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.domain.ClaimLimitService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyCondition;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

abstract class ViewProductDetails extends ViewComponent {

	private static final long serialVersionUID = -2983487868639195843L;

	private Object[] columns = new Object[] { "description", "productRules" };
	private static final String PRODUCT_CONDITIONS = "Day care Benefits";
	@EJB
	private PolicyService policyService;
	
	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;

	@EJB
	private IntimationService intimationService;
	@EJB
	private ClaimLimitService claimLimitService;

	public void initView() {

	}

	protected Table createViewTable(VerticalLayout conditionslayou) {
		Table productConditionstable = new Table();
		productConditionstable.setWidth("100.0%");
		//Vaadin8-setImmediate() productConditionstable.setImmediate(false);
		productConditionstable.addStyleName("tableheight");
		productConditionstable.setPageLength(5);
		return productConditionstable;
	}

	protected void initTable(Table productConditionstable,
			BeanItemContainer<PolicyCondition> policyConditions,
			String columnHeader1, String columnHeader2) {
		productConditionstable.setContainerDataSource(policyConditions);
		productConditionstable.setVisibleColumns(columns);
		productConditionstable.setColumnHeader("description", columnHeader1);
		productConditionstable.setColumnHeader("productRules", columnHeader2);
	}

	protected BeanItemContainer<PolicyCondition> getPolicyCondition(
			Product product,Long version) {
		
//		BeanItemContainer<PolicyCondition> policyConditions = policyService
//				.getProductConditionByType(policy.getProduct().getCode());
		
		BeanItemContainer<PolicyCondition> policyCondition ;
		if(version <= 2){
			policyCondition = policyService.getProductConditionByVersion(product.getKey(),version);

		}else{
			policyCondition = policyService.getProductConditionByTypeWithVersion(product.getKey(),version);
		}

		
		return policyCondition;
	}

	protected BeanItemContainer<PolicyCondition> getPolicyCondition(
			PremPolicyDetails policy) {
		BeanItemContainer<PolicyCondition> policyConditions = policyService
				.getProductConditionByType(policy.getProductCode());
		return policyConditions;
	}
	
	protected BeanItemContainer<PolicyCondition> getPolicyCondition(
			PremPolicy policy) {
		BeanItemContainer<PolicyCondition> policyConditions = policyService
				.getProductConditionByType(policy.getProductCode());
		return policyConditions;
	}
//	protected BeanItemContainer<PolicyCondition> getPolicyCondition(
//			TmpPolicy policy) {
//
//		BeanItemContainer<PolicyCondition> policyConditions = policyService
//				.getProductConditionByType(policy.getPolProductCode());
//		
//		return policyConditions;
//	}

}