package com.shaic.claim.reports.negotiationreport;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewNegotiationDetailsDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;
import com.vaadin.v7.data.util.BeanItemContainer;

@UIScoped
@CDIView(value = MenuItemBean.NEGOTIATION_REPORT)
public class NegotiationReportViewImpl extends AbstractMVPView implements NegotiationReportView,View{
	
	@Inject
	private SearchNegotiationReportUI negotiationReportUI;
	
	private BeanItemContainer<SelectValue> cpuSelectValueContainer;
	private BeanItemContainer<SelectValue> empSelectValueContainer;
	
	
	
	public void init(BeanItemContainer<SelectValue> cpuCode,BeanItemContainer<SelectValue> empContainer){
		this.cpuSelectValueContainer = cpuCode;
		this.empSelectValueContainer = empContainer;
        setSizeFull();
        negotiationReportUI.init();
        negotiationReportUI.setCpuDropDownValues(cpuSelectValueContainer,empSelectValueContainer);
        setCompositionRoot(negotiationReportUI);
        setVisible(true);

	}


	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void negotionReportDtls(List<ViewNegotiationDetailsDTO> reportList) {
		// TODO Auto-generated method stub
		negotiationReportUI.showTable(reportList);
	}

}
