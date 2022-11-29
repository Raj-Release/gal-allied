package com.shaic.claim.policy.search.ui.opsearch;


import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;

/*
 * create intimation presenter is the presenter for CreateIntimationView. EJBs and other resources should
 * mainly be accessed in the presenter and results are pushed to the view
 * implementation trough it's API.
 */
@SuppressWarnings("serial")
@ViewInterface(OPExpiredPolicyClaimView.class)
public class OPExpiredPolicyClaimPresenter extends AbstractMVPPresenter<OPExpiredPolicyClaimView> {
    // CDI MVP includes a built-in CDI event qualifier @CDIEvent which
    // uses a String (method identifier) as it's member

    public static final String SEARCH_INTIMATION_SUBMIT_OP = "search_intimation_submit_op";
    //public static final String SERACH_OPTION_CHANGES = "OP_presenter_search_option_changes";
    public static final String RESET_VALUES_OP = "OP_presenter_reset_values_Expired";
    //public static final String NAVIGATE_ADD_INTIMATION_SCREEN = "OP_presenter_navigate_add_intimation_screen";
    //public static final String SUBMIT_ALLOW_EXPIRED_POLICY_CLAIM = "OP_allow_expired_policy_claim_submit";

    
    protected void submitSearch(@Observes @CDIEvent(SEARCH_INTIMATION_SUBMIT_OP) final ParameterDTO parameters) {
        view.searchSubmit();
    }
    
    protected void showPolicySearch(@Observes @CDIEvent(MenuItemBean.EXPIRED_POLICIES_CLAIM_OP) final ParameterDTO parameters)
    {
    	view.showSearchPolicy();
    }
    
    protected void resetTheFormValues(
            @Observes @CDIEvent(RESET_VALUES_OP) final ParameterDTO parameters) {
        view.resetAlltheValues();
    }
    
    /*protected void submitCreateIntimationSearch( @Observes @CDIEvent(SUBMIT_ALLOW_EXPIRED_POLICY_CLAIM) final ParameterDTO parameters) {
    //view.searchSubmit();
    }*/

    
    @Override
    public void viewEntered() {
        // NOP
    }

}
