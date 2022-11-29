package com.shaic.claim.outpatient.registerclaim.wizard;

import java.util.Map;

import org.vaadin.teemu.wizards.event.GWizardListener;

import com.shaic.arch.GMVPView;

/**
 * @author Saravana Kumar P
 *
 */

public interface OPRegisterClaimWizard  extends GMVPView {
	void buildSuccessLayout();
	void cancelIntimation();
	void setValueToBalanceSumInsured(Integer opBalanceSumInsured);
}
