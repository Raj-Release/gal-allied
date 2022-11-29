package com.shaic.claim;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.reports.PolicywiseClaimReportDto;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


public class ViewICRDetails extends ViewComponent {
		
		private Panel mainPanelLayout;
		private Panel searchPanel;
		private VerticalLayout searchVerticalLayout;
		private HorizontalLayout buttonLayout;
		private TextField insuredName; 
		private TextField policyNumber;
		private TextField policyPeriod;
		private TextField earnedPremium;
		private TextField totalnumofClaims;
		private TextField claimedAmt;
		private TextField incurredClaimsRatio;	
		
		public void initView(PolicywiseClaimReportDto  icrDetailsDto)
		{
			mainPanelLayout = new Panel();
			mainPanelLayout.setSizeFull();
			searchVerticalLayout = buildFieldsLayout(icrDetailsDto);
			
			mainPanelLayout.setContent(searchVerticalLayout);
			mainPanelLayout.addStyleName("panelHeader");
//			mainPanelLayout.addStyleName("g-search-panel");
			mainPanelLayout.setSizeFull();
			setSizeFull();
			setCompositionRoot(mainPanelLayout);
		}

		/*private Panel buildDetailView(PolicywiseClaimReportDto icrDetailsDto){
			searchVerticalLayout = buildFieldsLayout(icrDetailsDto);
			searchPanel = new Panel("ICR Details");
			searchPanel.addStyleName("panelHeader");
			searchPanel.addStyleName("g-search-panel");
			searchPanel.setContent(searchVerticalLayout);
			
			return searchPanel;
		}*/
		
		private VerticalLayout buildFieldsLayout(PolicywiseClaimReportDto icrDetailsDto){
			searchVerticalLayout = new VerticalLayout();
			searchVerticalLayout.setWidth("100%");
			searchVerticalLayout.setHeight("100%");
			
			
			insuredName = new TextField("Proposer Name");
			insuredName.setWidth("100%");
			insuredName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			policyNumber = new TextField("Policy Number");
			policyNumber.setWidth("250px");
			policyNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			policyPeriod = new TextField("Policy Period");
			policyPeriod.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			policyPeriod.setWidth("250px");
			
//			premium = new TextField("PREMIUM");
//			premium.setEnabled(false);
//			premium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			earnedPremium = new TextField("Earned Premium");
			earnedPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
//			endorsementPremium = new TextField("ENDORSEMENT PREMIUM");
//			endorsementPremium.setEnabled(false);
//			endorsementPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
//			totalnumofClaims = new TextField("Total No Of Claims");
//			totalnumofClaims.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			claimedAmt = new TextField("Claimed Amount");
			claimedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			incurredClaimsRatio = new TextField("Incurred Claims Ratio");
			incurredClaimsRatio.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

			setValues(icrDetailsDto);			
			
			FormLayout searchfilterFrm = new FormLayout();
			searchfilterFrm.addComponents(insuredName,policyNumber,policyPeriod,earnedPremium,claimedAmt,incurredClaimsRatio);
			searchfilterFrm.addStyleName("layoutDesign");
			searchfilterFrm.setMargin(true);
			searchfilterFrm.setSpacing(true);

			searchVerticalLayout.addComponent(searchfilterFrm);
			
			searchVerticalLayout.setComponentAlignment(searchfilterFrm, Alignment.MIDDLE_CENTER);
			
			return searchVerticalLayout;
		}
		

		public void setValues(PolicywiseClaimReportDto icrDto){
			
			if(icrDto != null){
				
				insuredName.setValue(icrDto.getMainMemberName());
				insuredName.setReadOnly(true);
				
				policyNumber.setValue(icrDto.getPolicyNumber());
				policyNumber.setReadOnly(true);
				
				policyPeriod.setValue("FROM : "+icrDto.getPolicyPeriodFrom()+"  TO : "+icrDto.getPolicyPeriodTo());
				policyPeriod.setReadOnly(true);
				
//				premium.setValue(icrDto.getPremium() != null ? icrDto.getPremium().toString() : "");
				
				earnedPremium.setValue(icrDto.getPremium() != null ? String.valueOf(icrDto.getPremium().intValue()) : "");
				earnedPremium.setReadOnly(true);
				
//				endorsementPremium.setValue(icrDto.getEndorsementPremium() != null ? icrDto.getEndorsementPremium().toString() : "");
				
//				totalnumofClaims.setValue(icrDto.getTotalNoOfClaims());
//				totalnumofClaims.setReadOnly(true);
				
				claimedAmt.setValue(icrDto.getClaimedAmount() != null ? String.valueOf(icrDto.getClaimedAmount().longValue()) : "");
				claimedAmt.setReadOnly(true);
				
				incurredClaimsRatio.setValue(icrDto.getIcrRatio() != null ?  String.valueOf(icrDto.getIcrRatio().longValue()) : "");
				incurredClaimsRatio.setWidth("55px");
				incurredClaimsRatio.setReadOnly(true);
				
				if(("green").equalsIgnoreCase(icrDto.getColor())){
					incurredClaimsRatio.addStyleName("green"/*"greenBkgc"*/);
				}
				else if(("red").equalsIgnoreCase(icrDto.getColor())){
					incurredClaimsRatio.setStyleName("red"/*"redBkgc"*/);
				}
				else if(("yellow").equalsIgnoreCase(icrDto.getColor())){
					incurredClaimsRatio.setStyleName("yellow");
				}
			
			}	
		}

}
