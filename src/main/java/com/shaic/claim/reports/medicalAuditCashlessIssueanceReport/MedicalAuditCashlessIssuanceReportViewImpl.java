package com.shaic.claim.reports.medicalAuditCashlessIssueanceReport;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.v7.data.util.BeanItemContainer;

public class MedicalAuditCashlessIssuanceReportViewImpl extends AbstractMVPView implements MedicalAuditCashlessIssuanceReportView{
	
	
	@Inject
    private Instance<MedicalAuditCashlessIssuanceReportUI> medicalAuditClaimStatusReportUI; 

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
    
	public void medicalAuditClaimStatusReportDetailsView(List<MedicalAuditCashlessIssuanceReportDto> medicalAuditClaimStatusDtoList) {
		
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
