package com.shaic.paclaim.cashless.preauth.wizard.pages;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PAPreauthPreviousClaimsPageUI extends ViewComponent{

	public static final String RISK_WISE = "RISK WISE";
	public static final String INSURED_WISE = "Insured Wise";
	public static final String POLICY_WISE = "Policy Wise";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2068278273934991625L;
	
	public VerticalLayout wholeVLayout;
	public HorizontalLayout preauthPreviousClaimDetailsPageHLayout;
	public FormLayout rightFLayout;
	public FormLayout leftFLayout;
	public ComboBox cmbAttachtoPreviouClaim;
	public ComboBox cmbRelapseOfIllness;
	public TextField txtRemarks;
	
	
	
	
	/*public OptionGroup getOptionGroup(TmpPolicy tmpPolicy) {
		OptionGroup viewType = new OptionGroup();
		//Vaadin8-setImmediate() viewType.setImmediate(false);
		viewType.addItem(POLICY_WISE);
		viewType.addItem(INSURED_WISE);
//		if (tmpPolicy.getPolType().equalsIgnoreCase("group"))
			viewType.addItem(RISK_WISE);
		viewType.setStyleName("inlineStyle");
		return viewType;
	}*/
	
	public OptionGroup getOptionGroup(String policyType) {
		OptionGroup viewType = new OptionGroup();
		//Vaadin8-setImmediate() viewType.setImmediate(false);
		viewType.addItem(POLICY_WISE);
		viewType.addItem(INSURED_WISE);
		if (policyType.equalsIgnoreCase("group"))
			viewType.addItem(RISK_WISE);
		viewType.setStyleName("inlineStyle");
		return viewType;
	}
	
	
	public Button getViewButton() {
		Button viewBtn = new Button();
		viewBtn.setCaption("View");
		return viewBtn;
	}
	
	

}
