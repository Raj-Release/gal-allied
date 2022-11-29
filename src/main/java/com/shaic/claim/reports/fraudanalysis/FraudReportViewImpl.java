package com.shaic.claim.reports.fraudanalysis;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.vaadin.navigator.View;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings({ "deprecation", "serial" })
public class FraudReportViewImpl extends AbstractMVPView  implements FraudReportView, View{
	
	private VerticalLayout wholeVerticalLayout;

	@Inject
	private FraudReportForm reportForm;
	
	public void init(){
		setSizeFull();
		setVisible(true);
		wholeVerticalLayout = new VerticalLayout();
		reportForm.init();
		wholeVerticalLayout.addComponent(reportForm);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
		resetView();
	}
	
	@Override
	public void resetView() {
		
	}
}
