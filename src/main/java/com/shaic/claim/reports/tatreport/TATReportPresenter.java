package com.shaic.claim.reports.tatreport;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.v7.data.util.BeanItemContainer;
@ViewInterface(TATReportView.class)

public class TATReportPresenter extends AbstractMVPPresenter<TATReportView > {

	
	private static final long serialVersionUID = 1L;

	public static final String SEARCH_BUTTON_CLICK_TAT = "doSearchForTAT";
	
	public static final String SEARCH_OFFICE_CODE = "doSearchForOfficeCode";
		
	@EJB
	private TATReportService searchService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private UsertoCPUMappingService userCPUMapService;
	
	public static final String GENERATE_REPORT = "generate_tat_report";

	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK_TAT) final ParameterDTO parameters) {
		
		TATReportFormDTO searchFormDTO = (TATReportFormDTO) parameters.getPrimaryParameter();
		
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		
//		view.list(searchService.search(searchFormDTO,userName,passWord,dbCalculationService,userCPUMapService));
		
		view.list(searchService.search(searchFormDTO,userName,passWord,dbCalculationService));
	}
	
	@SuppressWarnings({ "deprecation" })
	public void generateReport(@Observes @CDIEvent(GENERATE_REPORT) final ParameterDTO parameters) {
		view.generateReport();
		
	}

	@SuppressWarnings({ "deprecation" })
	public void getOfficeCode(@Observes @CDIEvent(SEARCH_OFFICE_CODE) final ParameterDTO parameters) {
		
		SelectValue cpuCodeValue = (SelectValue) parameters.getPrimaryParameter();
		
		String  cpuCode = null != cpuCodeValue ?cpuCodeValue.toString() : null;
		BeanItemContainer<SelectValue> officeCodeContainer = masterService.getOfficeCodeByCpuCode(cpuCode);
		view.setOfficeCodeDropDown(officeCodeContainer);
	}
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
}
