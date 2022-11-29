package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;
/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DecideOnRejectionView  extends GMVPView,WizardStep<ClaimRejectionDto> {
	
	 void init(ClaimRejectionDto bean, GWizard wizard);
	 void returnPreviousPage();
	 void buildRedraftRejectionLayout();
	 void buildDisapproveRejectionLayout();
	 void buildApproveRejectionLayout();
	 void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer);
	 void clearObject();
}
