package com.shaic.claim.stoploss.view;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewStopLossDetails extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;
	private HorizontalLayout stoplossHorizontal;
	private FormLayout formlayoutRight;
	private FormLayout formlayoutleft;
	private TextField txtPolicyNo;
	private TextField txtPremium;
	private TextField txtStopLossPer;
	private TextField txtStopLossAmt;
	private TextField txtClaimPaid;
	private TextField txtClaimOutStanding;
	private TextField txtTotalIncuredClaimsAmt;
	private TextField txtCurrentClaimsAmt;
	
	private BeanFieldGroup<ViewStopLossDTO> binder;	

	public void init() {
		initBinder();
		buildMainLayout();
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<ViewStopLossDTO>(
				ViewStopLossDTO.class);
		this.binder.setItemDataSource(new ViewStopLossDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	public void buildMainLayout(){		
		stoplossHorizontal = new HorizontalLayout(buildLeftLayout(), buildRightLayout());
		mainLayout = new VerticalLayout(stoplossHorizontal);
		mainLayout.setComponentAlignment(stoplossHorizontal, Alignment.MIDDLE_CENTER);
		mainLayout.setMargin(true);
		setCompositionRoot(mainLayout);
	}
	
	public FormLayout buildLeftLayout() {
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		txtPremium = binder.buildAndBind("Premium", "premium", TextField.class);
		txtStopLossPer = binder.buildAndBind("Stop Loss %", "stopLossPer",
				TextField.class);
		txtStopLossAmt = binder.buildAndBind("Stop Loss Amount", "stopLossAmt",
				TextField.class);		
		return new FormLayout(txtPolicyNo, txtPremium,
				txtStopLossPer, txtStopLossAmt);
	}
	
	public FormLayout buildRightLayout() {
		txtClaimPaid = binder.buildAndBind("Claim Paid", "claimPaid",
				TextField.class);
		txtClaimOutStanding = binder.buildAndBind("Claim Outstanding",
				"claimOutstanding", TextField.class);
		txtTotalIncuredClaimsAmt = binder.buildAndBind(
				"Total Incurred Claims Amount", "totalIncuredClaimAmt",
				TextField.class);
		txtCurrentClaimsAmt = binder.buildAndBind("Current Claims Amount",
				"currentClaimsAmt", TextField.class);
		return new FormLayout(txtClaimPaid, txtClaimOutStanding,
				txtTotalIncuredClaimsAmt, txtCurrentClaimsAmt);
	}

}
