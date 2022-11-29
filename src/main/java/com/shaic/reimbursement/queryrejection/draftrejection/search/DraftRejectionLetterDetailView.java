package com.shaic.reimbursement.queryrejection.draftrejection.search;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;


/**
 * 
 * @author Lakshminarayana
 *
 */
public interface DraftRejectionLetterDetailView extends GMVPView {

	public void cancelDraftRejectionLetter();
	public void submitDraftRejectionLetter();
	public void openPdfFileInWindow();
	public void setSubCategContainer(BeanItemContainer<SelectValue> rejSubcategContainer);
}
