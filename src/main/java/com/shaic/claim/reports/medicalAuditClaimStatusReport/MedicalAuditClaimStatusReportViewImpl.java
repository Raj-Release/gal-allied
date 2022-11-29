package com.shaic.claim.reports.medicalAuditClaimStatusReport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class MedicalAuditClaimStatusReportViewImpl extends AbstractMVPView implements MedicalAuditClaimStatusReportView{
	
	
	@Inject
    private Instance<MedicalAuditClaimStatusReport> medicalAuditClaimStatusReportUI; 

	@Override
	public void resetView() {
		medicalAuditClaimStatusReportUI.get().resetAlltheValues();
	}
	
	public void init(BeanItemContainer<SelectValue> statusContainer){
		medicalAuditClaimStatusReportUI.get().setStatusDropDownValue(statusContainer);
	}
	
	@PostConstruct
	public void initView() {
		showMedicalAuditClaimStatusReport();
    	resetView();
    }
    
	public void medicalAuditClaimStatusReportDetailsView(List<MedicalAuditClaimStatusReportDto> medicalAuditClaimStatusDtoList) {
		
		medicalAuditClaimStatusReportUI.get().showTableResult(medicalAuditClaimStatusDtoList);
	}
	
	@Override
	public void showMedicalAuditClaimStatusReport() {
		addStyleName("view");
        setSizeFull();
        medicalAuditClaimStatusReportUI.get().init();
        setCompositionRoot(medicalAuditClaimStatusReportUI.get());
        setVisible(true);
	}	

}
