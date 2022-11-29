package com.shaic.claim.reports.metabasereport;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.table.Searchable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class MetabaseReportForm extends ViewComponent{

	private static final long serialVersionUID = -3616823920991467671L;

	Button reportBtn;
	
	Button dboardBtn;

	VerticalLayout buildPreauthSearchLayuout;

	private Searchable searchable;	
	

	@PostConstruct
	public void init() {
		
		buildPreauthSearchLayuout  = new VerticalLayout();
		Panel preauthPremedicalPanel	= new Panel();
		preauthPremedicalPanel.setCaption("Cashless Operational Monitoring");
		preauthPremedicalPanel.addStyleName("panelHeader");
		preauthPremedicalPanel.addStyleName("g-search-panel");
		preauthPremedicalPanel.setContent(buildPreauthSearchLayout());
		buildPreauthSearchLayuout.addComponent(preauthPremedicalPanel);
		buildPreauthSearchLayuout.setComponentAlignment(preauthPremedicalPanel, Alignment.MIDDLE_LEFT);
		buildPreauthSearchLayuout.setMargin(false);
		setCompositionRoot(buildPreauthSearchLayuout);
		addListener();
	}

	private VerticalLayout buildPreauthSearchLayout() 
	{
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100%");
		verticalLayout.setMargin(true);		 	

		reportBtn = new Button();
		reportBtn.setCaption("Monitoring Report");
		reportBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		reportBtn.setWidth("-1px");
		reportBtn.setHeight("-10px");
		reportBtn.setVisible(false);
		dboardBtn = new Button();
		dboardBtn.setCaption("Floor Monitoring Report");
		dboardBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		dboardBtn.setWidth("-1px");
		dboardBtn.setHeight("-10px");
		dboardBtn.setVisible(false);
		
		if(BPMClientContext.showMetaBaseMonitoringRpt()){
			reportBtn.setVisible(true);
		}
		
		if(BPMClientContext.showMetaBaseFlrMonitoringRpt()){
			dboardBtn.setVisible(true);
		}

		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(reportBtn,dboardBtn);
		hLayout.setSpacing(true);
		FormLayout fLayout = new FormLayout();
		fLayout.setMargin(false);
		fLayout.addComponents(hLayout);
		verticalLayout.addComponent(fLayout);
		verticalLayout.setHeight("50px");
		verticalLayout.addStyleName("g-search-panel");
		verticalLayout.setComponentAlignment(fLayout, Alignment.TOP_LEFT);
		return verticalLayout; 

	}

	public void addListener() {

		reportBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				BPMClientContext bpmClientContext = new BPMClientContext();
				String url = bpmClientContext.getGalaxyMetabaseUrl();
				getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
			}
		});	
		
		dboardBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				BPMClientContext bpmClientContext = new BPMClientContext();
				String url = bpmClientContext.getDashboardUrl();
				getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
			}
		});
	}

	public void refresh()
	{
		System.out.println("---inside the refresh----");
	}
}
