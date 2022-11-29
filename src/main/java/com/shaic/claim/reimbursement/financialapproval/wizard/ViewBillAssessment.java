package com.shaic.claim.reimbursement.financialapproval.wizard;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.financialapproval.pages.communicationPage.FinancialDecisionCommunicationPageUI;
import com.shaic.domain.ReferenceTable;
import com.shaic.newcode.wizard.IWizard;
import com.shaic.paclaim.health.reimbursement.financial.pages.communicationpage.PAHealthFinancialDecisionCommunicationPageUI;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
@CDIUI
public class ViewBillAssessment extends UI {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Bill Assessment Sheet");
		if( request.getWrappedSession().getAttribute(SHAConstants.PREAUTH_DTO) != null && request.getWrappedSession().getAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE) != null) {
			
			PreauthDTO  bean = (PreauthDTO) request.getWrappedSession().getAttribute(SHAConstants.PREAUTH_DTO);
								
				  if(null != bean && null!= bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
						   !(ReferenceTable.getGPAProducts().containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())))
				  {
					 // FinancialDecisionCommunicationPageUI viewAssessmentView = (FinancialDecisionCommunicationPageUI) request.getWrappedSession().getAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE);
					  SHAUtils.setConsolidatedAmtDTO(bean, true);
					  Object attribute = request.getWrappedSession().getAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE);
					  if(attribute != null){
						  if(attribute instanceof FinancialDecisionCommunicationPageUI){
							  FinancialDecisionCommunicationPageUI viewAssessmentView = (FinancialDecisionCommunicationPageUI) attribute;
							  viewAssessmentView.init(bean, new IWizard(), null);
							  Component content= viewAssessmentView.getContent();
							   Panel panel = (Panel)content;
								panel.setHeight("1000px");
//								panel.setHeight("1000px");
								setContent(panel);
						  }else if(attribute instanceof PAHealthFinancialDecisionCommunicationPageUI){
							  PAHealthFinancialDecisionCommunicationPageUI viewAssessmentView = (PAHealthFinancialDecisionCommunicationPageUI) attribute;
							  viewAssessmentView.init(bean, new IWizard());
							  Component content= viewAssessmentView.getContent();
							  Panel panel = (Panel)content;
							  panel.setHeight("1000px");
//							  panel.setHeight("1000px");
							  setContent(panel);
							  
						  }
					  }
						
						
//						if(bean.getBillingDate() != null) {
						//Component content = null;
						   
						  
				  }
				  
				  else				  
				  {
					  PAHealthFinancialDecisionCommunicationPageUI viewAssessmentViewGPA = (PAHealthFinancialDecisionCommunicationPageUI) request.getWrappedSession().getAttribute(SHAConstants.BILL_ASSESSMENT_VIEW_PAGE);
					  SHAUtils.setConsolidatedAmtDTO(bean, true);
//						if(bean.getBillingDate() != null) {
					  viewAssessmentViewGPA.init(bean, new IWizard());
					  Component content = viewAssessmentViewGPA.getContent();
				   
				Panel panel = (Panel)content;
				panel.setHeight("1000px");
//				panel.setHeight("1000px");
				setContent(panel);
				  }
//			} else {
//				Label label = new Label("<b style = 'color:red;'>Billing Assessment Sheet is not applicable. </b>", ContentMode.HTML);
//				setContent(label);
//			}
			
		} else {
			Label label = new Label("<b style = 'color:red;'>Billing Assessment Sheet is not applicable. </b>", ContentMode.HTML);
			setContent(label);
		}
		
	}

}
