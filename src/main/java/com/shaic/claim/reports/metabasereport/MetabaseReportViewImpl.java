package com.shaic.claim.reports.metabasereport;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
@CDIView(value = MenuItemBean.MONITORING_REPORT)
public class MetabaseReportViewImpl extends AbstractMVPView  implements MetabaseReportView,View{
	private VerticalLayout wholeVerticalLayout;
	
	@Inject
	private MetabaseReportForm metabaseReportForm;
	
	public void init(){
		
		setSizeFull();
		setVisible(true);
		wholeVerticalLayout = new VerticalLayout();
		metabaseReportForm.init();
		wholeVerticalLayout.addComponent(metabaseReportForm);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
		resetView();
		
		//To Display JSP in Vaddin screen 
		/*Embedded jsp = new Embedded();
		jsp.setType(Embedded.TYPE_BROWSER);
		jsp.setWidth("100%");
		jsp.setHeight("550px");
		jsp.setSizeFull();
		jsp.setSource(new ExternalResource ("MetaDataViewPage.jsp"));*/
		
	}
	
	@Override
	public void resetView() {
		
		
	}

}
