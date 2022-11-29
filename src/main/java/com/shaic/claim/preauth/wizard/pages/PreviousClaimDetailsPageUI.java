package com.shaic.claim.preauth.wizard.pages;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class PreviousClaimDetailsPageUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6899758986501494773L;
	
	private VerticalLayout wholeVLayout;
	private HorizontalLayout preauthPreviousClaimDetailsPageHLayout;
	private FormLayout rightFLayout;
	private FormLayout leftFLayout;
	private ComboBox cmbAttachtoPreviouClaim;
	private ComboBox cmbRelapseOfIllness;
	private TextField txtRemarks;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initview() {
		
	}
	
	private VerticalLayout buildMainLayout() {
		wholeVLayout = new VerticalLayout();
		//Vaadin8-setImmediate() wholeVLayout.setImmediate(false);
		wholeVLayout.setWidth("100%");
		wholeVLayout.setHeight("100%");
		wholeVLayout.setMargin(false);
		
		setWidth("100.0%");
		setHeight("100.0%");
		
		preauthPreviousClaimDetailsPageHLayout = buildRelapseOfIllness();
		wholeVLayout.addComponent(preauthPreviousClaimDetailsPageHLayout);
		wholeVLayout.setExpandRatio(preauthPreviousClaimDetailsPageHLayout, 1.0f);
		
		return wholeVLayout;
	}

	private HorizontalLayout buildRelapseOfIllness() {
		preauthPreviousClaimDetailsPageHLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() preauthPreviousClaimDetailsPageHLayout.setImmediate(false);
		preauthPreviousClaimDetailsPageHLayout.setWidth("100.0%");
		preauthPreviousClaimDetailsPageHLayout.setHeight("100.0%");
		preauthPreviousClaimDetailsPageHLayout.setMargin(true);
		preauthPreviousClaimDetailsPageHLayout.setSpacing(true);
		
		//PassIntimation Number To get privies claim table		
		String intimationNumber = "I/2014/0000048";//TODO
//		VerticalLayout claimlayout = previousClaims.get().getPrivousClaims(intimationNumber);
		
//		preauthPreviousClaimDetailsPageHLayout.addComponent(claimlayout);
		
		return preauthPreviousClaimDetailsPageHLayout;
	}

	private FormLayout buildLeftFormLayout() {
		leftFLayout = new FormLayout();
		//Vaadin8-setImmediate() leftFLayout.setImmediate(false);
		leftFLayout.setMargin(true);
		leftFLayout.setSpacing(true);
		
		cmbRelapseOfIllness = new ComboBox();
		cmbRelapseOfIllness.setCaption("Relapse of Illness");
		//Vaadin8-setImmediate() cmbRelapseOfIllness.setImmediate(false);
		leftFLayout.addComponent(cmbRelapseOfIllness);
		
		cmbAttachtoPreviouClaim = new ComboBox();
		cmbAttachtoPreviouClaim.setCaption("Attach to Previous Claim");
		//Vaadin8-setImmediate() cmbAttachtoPreviouClaim.setImmediate(false);
		leftFLayout.addComponent(cmbAttachtoPreviouClaim);
		
		return leftFLayout;
	}

	private FormLayout buildRightFormLayout() {
		rightFLayout = new FormLayout();
		//Vaadin8-setImmediate() rightFLayout.setImmediate(false);
		rightFLayout.setMargin(true);
		rightFLayout.setSpacing(true);
		
		txtRemarks = new TextField();
		txtRemarks.setCaption("Remarks");
		//Vaadin8-setImmediate() txtRemarks.setImmediate(false);
		rightFLayout.addComponent(txtRemarks);
		
		return rightFLayout;
	}
	

}
