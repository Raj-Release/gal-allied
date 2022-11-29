/**
 * 
 */
package com.shaic.main;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;


/**
 * @author ntv.vijayar
 *
 */
public class ViewProposalServlet extends HttpServlet {
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	////private static Window popup;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
		if(null != request.getAttribute("policyscheduleurl"))
		{
			request.removeAttribute("policyscheduleurl");
		}
		
		if(null != request.getSession().getAttribute("policyscheduleurl"))
		{
			 request.getSession().removeAttribute("policyscheduleurl");
		}
		
		String intimationNo = (String)request.getSession().getAttribute("intimationNo");
		Intimation intimationObj = intimationService.getIntimationByNo(intimationNo);
		if(null != intimationObj)
		{
			String url = null;
			Policy policyObj = null;
			BrowserFrame browserFrame = null;
			if(null != intimationObj && null != intimationObj.getPolicy())
			{
				Long insuredKey = intimationObj.getInsured().getKey();
				Insured	insured = intimationService.getInsuredByKey(insuredKey);

			if (intimationObj.getPolicy().getPolicyNumber() != null) {
				policyObj = policyService.getByPolicyNumber(intimationObj.getPolicy().getPolicyNumber());
				if (policyObj != null) {
					if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
						url = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
						url = url.replace("POLICY", intimationObj.getPolicy().getPolicyNumber());
						if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
							url = url.replace("MEMBER", insured!=null?String.valueOf(insured.getSourceRiskId()!=null?insured.getSourceRiskId():""):"");		
						}else{
							url = url.replace("MEMBER", "");
						}
					}else{
						url = BPMClientContext.DMS_VIEW_URL;
						String dmsToken = intimationService.createDMSToken(intimationObj.getPolicy().getPolicyNumber());
						url = url+dmsToken;
					}
				}
			}
				//String url = strDmsViewURL+intimationObj.getPolicy().getPolicyNumber();
				request.setAttribute("policyscheduleurl", url);
				//getViewDocumentByPolicyNo(intimationObj.getPolicy().getPolicyNumber());
//				request.getRequestDispatcher("/WEB-INF/ViewPolicySchedule.jsp").forward(request, response);
				response.sendRedirect(url);
			}
			
			
		}
	  }
	
	

	public void getViewDocumentByPolicyNo(String strPolicyNo) {
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		String dmsToken = intimationService.createDMSToken(strPolicyNo);
		UI.getCurrent().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
		/*BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+dmsToken));
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		
		UI.getCurrent().addWindow(popup);*/
	}
		
		
}
