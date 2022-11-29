package com.shaic.claim.reports.fraudanalysis;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

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

@SuppressWarnings({ "deprecation", "serial" })
public class FraudReportForm extends ViewComponent {

	Button reportBtn;
	Button dboardBtn;

	VerticalLayout buildPreauthSearchLayuout;


	@PostConstruct
	public void init() {

		buildPreauthSearchLayuout  = new VerticalLayout();
		Panel preauthPremedicalPanel	= new Panel();
		preauthPremedicalPanel.setCaption("Fraud Analysis Reports");
		preauthPremedicalPanel.addStyleName("panelHeader");
		preauthPremedicalPanel.addStyleName("g-search-panel");
		preauthPremedicalPanel.setContent(buildPreauthSearchLayout());
		buildPreauthSearchLayuout.addComponent(preauthPremedicalPanel);
		buildPreauthSearchLayuout.setComponentAlignment(preauthPremedicalPanel, Alignment.MIDDLE_LEFT);
		buildPreauthSearchLayuout.setMargin(false);
		setCompositionRoot(buildPreauthSearchLayuout);
		addListener();
	}

	
	private VerticalLayout buildPreauthSearchLayout() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setWidth("100%");
		verticalLayout.setMargin(true);		 	

		reportBtn = new Button();
		reportBtn.setCaption("Fraud Analysis Report");
		reportBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		reportBtn.setWidth("-1px");
		reportBtn.setHeight("-10px");
		reportBtn.setVisible(true);

		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(reportBtn);
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
				String url = "";
				if(BPMClientContext.REPORTS_URL.endsWith("/")){
					url = BPMClientContext.REPORTS_URL+"reports/powerbiui";
				}else{
					url = BPMClientContext.REPORTS_URL+"/reports/powerbiui";
				}
				getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
			}
		});	
	}

	public void refresh() {
		System.out.println("---inside the refresh----");
	}
}
