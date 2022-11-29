package com.shaic.claim.reports;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
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

@SuppressWarnings("serial")
@UIScoped
public class SearchClaimPolicyReport extends ViewComponent {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
//	private HorizontalLayout buttonLayout;
	private AbsoluteLayout buttonLayout;
//	private TextField insuredName; 
	private TextField policyNumber;
//	private TextField policyPeriod;
//	private TextField premium;
//	private TextField basePremium;
//	private TextField endorsementPremium;
//	private TextField totalnumofClaims;
//	private TextField claimedAmt;
//	private TextField incurredClaimsRatio;	
	private Button exportBtn;
	private Label dummyLabel;
	private ExcelExport excelExport;
	
//	private Double totalClaimedAmt;
	//private Double claimRatio;
	
	@Inject
	private PolicywiseClaimReportTable policywiseClaimTable;
	
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.setHeight("100%");
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		policywiseClaimTable.init("", false, false);
		setCompositionRoot(wholeVerticalLayout);
	}

	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Policy Wise Claim Details");
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel");
		searchPanel.setContent(searchVerticalLayout);
		
		return searchPanel;
	}
	
	private VerticalLayout buildSearchLayout(){
		searchVerticalLayout = new VerticalLayout();
		searchVerticalLayout.setSpacing(false);
//		insuredName = new TextField("INSURED NAME");
//		insuredName.setWidth("250px");
//		insuredName.setEnabled(false);
//		insuredName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		policyNumber = new TextField("Policy No");
		
		CSValidator policyNumvalidator = new CSValidator();
		policyNumvalidator.extend(policyNumber);
		policyNumvalidator.setRegExp("^[a-zA-Z 0-9/-]*$");
		policyNumvalidator.setPreventInvalidTyping(true);
		policyNumvalidator.extend(policyNumber);
		
//		policyPeriod = new TextField("POLICY PERIOD");
//		policyPeriod.setEnabled(false);
//		policyPeriod.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		policyPeriod.setWidth("250px");		
//		premium = new TextField("PREMIUM");
//		premium.setEnabled(false);
//		premium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		basePremium = new TextField("BASE PREMIUM");
//		basePremium.setEnabled(false);
//		basePremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		endorsementPremium = new TextField("ENDORSEMENT PREMIUM");
//		endorsementPremium.setEnabled(false);
//		endorsementPremium.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		totalnumofClaims = new TextField("TOTAL NO OF CLAIMS");
//		totalnumofClaims.setEnabled(false);
//		totalnumofClaims.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		claimedAmt = new TextField("CLAIMED AMOUNT");
//		claimedAmt.setEnabled(false);
//		claimedAmt.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		incurredClaimsRatio = new TextField("INCURRED CLAIMS RATIO");
//		incurredClaimsRatio.setEnabled(false);
//		incurredClaimsRatio.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dummyLabel =new Label();
		dummyLabel.setWidth("30px");
		
		FormLayout searchfilterFrm = new FormLayout();
//		searchfilterFrm.addComponents(insuredName,policyNumber,policyPeriod,premium,basePremium,endorsementPremium,totalnumofClaims,claimedAmt,incurredClaimsRatio);
		searchfilterFrm.addComponents(policyNumber);
		
		HorizontalLayout fieldHLayout = new HorizontalLayout(searchfilterFrm);
		fieldHLayout.setMargin(true);
		fieldHLayout.setSpacing(true);
		
//		buttonLayout = new HorizontalLayout();
		buttonLayout = new AbsoluteLayout();
		buttonLayout.setHeight("140px");				
		buttonLayout.addComponent(fieldHLayout);

		Button searchBtn = new Button("Search");
		searchBtn.setWidth("-1px");
		searchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchBtn.setDisableOnClick(true);
		searchBtn.addStyleName("hover");
		//Vaadin8-setImmediate() searchBtn.setImmediate(true);
		searchBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				((Button)event.getSource()).setEnabled(true);
				String policyNo = policyNumber.getValue();
				if(policyNo == null){
					Notification.show("Please Enter Policy Number", Notification.TYPE_ERROR_MESSAGE);
				}
				else{
					
					if(("").equalsIgnoreCase(policyNo)){
						Notification.show("Please Enter Policy Number", Notification.TYPE_ERROR_MESSAGE);
					}
					else{
						String userName = (String) getUI().getSession().getAttribute(
								BPMClientContext.USERID);
						fireViewEvent(SearchClaimPolicyReportPresenter.SEARCH_CLM_POLICY_WISE, policyNo,userName);
					}						
				}
				
				
			}
		});
		
		Button resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.addClickListener(new Button.ClickListener() {
			
							private static final long serialVersionUID = 1L;
			
							@Override
							public void buttonClick(ClickEvent event) {
								resetAlltheValues();
								
								if(null != policywiseClaimTable && searchVerticalLayout.getComponentCount() >1){
									searchVerticalLayout.removeComponent(policywiseClaimTable);
								}
												
							}
						});	
//		Label dummyLabel1 =new Label();
//		dummyLabel1.setWidth("30px");
//		buttonLayout.addComponents(searchBtn,dummyLabel1,resetBtn);
//		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(searchBtn, "top:85.0px;left:190.0px;");
		buttonLayout.addComponent(resetBtn, "top:85.0px;left:299.0px;");
		
		searchVerticalLayout.addComponent(buttonLayout);
//		searchVerticalLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);
		
		return searchVerticalLayout;
	}

	private void addExportButton() {
		exportBtn = new Button("Export to Excel");
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(policywiseClaimTable.getTable());
				excelExport.setReportTitle("Policy Wise Claim Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
//		buttonLayout.addComponent(dummyLabel);
//		buttonLayout.addComponent(exportBtn);
		
		buttonLayout.addComponent(exportBtn, "top:85.0px;left:408.0px;");
		
	}

	public void resetAlltheValues(){

		resetSearchFields();
		policywiseClaimTable.setTableList(new ArrayList<PolicywiseClaimReportDto>());
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
//			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
	}
	public void resetSearchFields(){
		policyNumber.setValue("");
//		insuredName.setValue("");
//		policyPeriod.setValue("");
//		premium.setValue("");
//		basePremium.setValue("");
//		endorsementPremium.setValue("");
//		totalnumofClaims.setValue("");
//		claimedAmt.setValue("");
//		incurredClaimsRatio.setValue("");
	
	}
	

	public void showTable(List<PolicywiseClaimReportDto> claimList){
		
		if(claimList != null && !claimList.isEmpty()){
			
//			insuredName.setValue(claimList.get(0).getMainMemberName());
//			
//			policyPeriod.setValue("FROM : "+claimList.get(0).getPolicyPeriodFrom()+" TO : "+claimList.get(0).getPolicyPeriodTo());
//			
//			premium.setValue(claimList.get(0).getPremium() != null ? claimList.get(0).getPremium().toString() : "");
//			basePremium.setValue(claimList.get(0).getBasepremium() != null ? String.valueOf(claimList.get(0).getBasepremium().intValue()) : "");
//			endorsementPremium.setValue(claimList.get(0).getEndorsementPremium() != null ? claimList.get(0).getEndorsementPremium().toString() : "");
//			totalnumofClaims.setValue(claimList.get(0).getTotalNoOfClaims());
//			
//			totalClaimedAmt = 0d;
//			for(PolicywiseClaimReportDto resultDto :claimList){
//				totalClaimedAmt += resultDto.getClaimedAmount() != null ? resultDto.getClaimedAmount() : 0d;
//			}
//			
//			claimedAmt.setValue(String.valueOf(totalClaimedAmt.intValue()));
//		
//			incurredClaimsRatio.setValue(totalClaimedAmt != 0d  && claimList.get(0).getBasepremium() != null ?  String.format("%.2f",(totalClaimedAmt / claimList.get(0).getBasepremium())*100)  : "");			
			
			
			policywiseClaimTable.setTableList(claimList);
			if(exportBtn == null){
			addExportButton();
			}
			searchVerticalLayout.addComponent(policywiseClaimTable);
		}
		
		else{
//			Notification.show("No Records Found", Notification.TYPE_WARNING_MESSAGE);
			
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dummyLabel);
				exportBtn = null;
			}
			
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search Claim Policy Wise Report Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					resetAlltheValues();
					fireViewEvent(MenuItemBean.SEARCH_CLAIM_POLICY_REPORT, null);
					
				}
			});			
			
		}
	}


}
