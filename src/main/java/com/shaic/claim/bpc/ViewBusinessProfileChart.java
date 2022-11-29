package com.shaic.claim.bpc;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewBusinessProfileChart extends ViewComponent{

	private static final long serialVersionUID = 1L;

	@Inject
	HospitalProfileView hospitalProfileView;

	@Inject
	SalesManagerProfileView salesManagerProfileView;
	
	@Inject
	AgentProfileView agentProfileView;
	
	private PreauthDTO bean;


	private VerticalLayout mainLayout;
	@SuppressWarnings("deprecation")
	public void init(PreauthDTO dtoBean) {
		this.bean = dtoBean;
		mainLayout = new VerticalLayout();
		mainLayout.addComponent(buildProfileTabs(dtoBean));
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);

	}

	private TabSheet buildProfileTabs(PreauthDTO bean) {
		TabSheet profileChartTab = new TabSheet();
		profileChartTab.setSizeFull();
		profileChartTab.setStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		//String hospitalCode ="HOS-8561";

		TabSheet hospitalTab = new TabSheet();
		hospitalProfileView.init(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
		hospitalTab.setHeight("100.0%");
		hospitalTab.setWidth("100.0%");
		hospitalTab.addComponent(hospitalProfileView);
		profileChartTab.addTab(hospitalTab, "Hospital Profile", null);



		TabSheet smProfileTab = new TabSheet();
		//String smCode ="";
		salesManagerProfileView.init(bean.getNewIntimationDTO().getPolicy().getSmCode());
		smProfileTab.setHeight("100.0%");
		smProfileTab.setWidth("100.0%");
		smProfileTab.addComponent(salesManagerProfileView);
		profileChartTab.addTab(smProfileTab, "SalesManager Profile", null);

		TabSheet agentProfileTab = new TabSheet();
		//String agentCode = "";
		agentProfileView.init(bean.getNewIntimationDTO().getPolicy().getAgentCode());
		agentProfileTab.setHeight("100.0%");
		agentProfileTab.setWidth("100.0%");
		agentProfileTab.addComponent(agentProfileView);
		profileChartTab.addTab(agentProfileTab, "Agent Profile", null);

		return profileChartTab;
	}



}
