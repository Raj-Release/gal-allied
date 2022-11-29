/**
 * 
 */
package com.shaic.claim.productbenefit.view;

import org.vaadin.addon.cdimvp.ViewComponent;

/**
 * This class will be used by Search policy 
 * screen alone. When user click view previous
 * policy in result table, and if there is 
 * no data present in CLS POLICY table,
 * then data needs to be copied from tmp policy to
 * cls policy and then view needs to be displayed.
 * For data copy, a method is already available
 * in menu presenter. fireViewEvent to be invoked
 * to call that method which is present in Menupresenter.
 * But  from ViewProductBenefit class
 * fireViewEvent cannot be invoked. Hence this
 * class was introduced. This class will have only one method.
 * That method will invoke fireViewEvent and navigate the
 * control to menu presenter.
 */
public class ViewProductBenefitImpl extends ViewComponent {
	
//	public void invokeMenuPresenter(TmpPolicy tmpPolicy)
//	{
//		fireViewEvent(MenuPresenter.POPULATE_POLICY_FROM_TMPPOLICY , tmpPolicy);
//	}

}
