package com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillAssessmentSheet;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
@CDIUI("BillAssessmentUIFinancial")
public class BillAssessmentUI extends UI{

	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Bill Assessment Sheet");
		if( request.getWrappedSession().getAttribute(SHAConstants.PREAUTH_DTO) != null && request.getWrappedSession().getAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE) != null) {
			ViewBillAssessmentSheet viewAssessmentView = (ViewBillAssessmentSheet) request.getWrappedSession().getAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE);
			PreauthDTO bean = (PreauthDTO) request.getWrappedSession().getAttribute(SHAConstants.PREAUTH_DTO);
			if(bean.getBillingDate() != null) {
				viewAssessmentView.init(bean,bean.getKey());
				Panel mainPanel = new Panel(viewAssessmentView);
//				mainPanel.setHeight("1000px");
				setContent(mainPanel);
			} else {
				Label label = new Label("<b style = 'color:red;'>Billing Assessment Sheet is not applicable. </b>", ContentMode.HTML);
				setContent(label);
			}
			
		} else {
			Label label = new Label("<b style = 'color:red;'>Billing Assessment Sheet is not applicable. </b>", ContentMode.HTML);
			setContent(label);
		}
		
	}

}
