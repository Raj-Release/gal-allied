package com.shaic.claim.omp.ratechange;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.domain.OMPClaimRateChange;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class OMPClaimRateChangeHistory extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    private TextField txtIntimationNo;
    
    private TextField txtEventCode;
    
    private TextField txtClassification;
    
    private TextField txtNRCurrency;
    
    private TextField txtRodNo;
    
    private TextField txtINRConversionRate;
    
    private VerticalLayout mainVerticalLayout;
    
    private BeanFieldGroup<OMPClaimProcessorDTO> binder;
    
    private OMPClaimProcessorDTO bean;
    @Inject
    private Instance<OMPClaimRateChangeHistoryDetailTable> ompClaimRateDetailsInstance;
    
    private OMPClaimRateChangeHistoryDetailTable ompClaimRateDetailsObj;
    
    @EJB
	private OMPIntimationService intimationService;
    
    @EJB
    private OMPProcessRODBillEntryService rodBillentryService;
    
    public void init(/*OMPClaimProcessorDTO bean*/ OMPReimbursement reimbursement ){
    	initBinder();
    	OMPClaimProcessorDTO dto = new OMPClaimProcessorDTO();
    	
    	txtIntimationNo = new TextField("Intimation No");
    	txtIntimationNo.setValue(reimbursement.getClaim().getIntimation().getIntimationId());
    	txtIntimationNo.setReadOnly(true);
    	txtEventCode = new TextField("Event Code");
    	if(reimbursement.getEventCodeId()!= null && reimbursement.getEventCodeId().getEventDescription() != null){
    	txtEventCode.setValue(reimbursement.getEventCodeId().getEventDescription());
    	}
    	txtEventCode.setReadOnly(true);
    	txtClassification = new TextField("Classification");
    	if(reimbursement.getClassificationId() != null && reimbursement.getClassificationId().getValue() != null){
    	txtClassification.setValue(reimbursement.getClassificationId().getValue());
    	}
    	txtClassification.setReadOnly(true);
    	txtNRCurrency = new TextField("INR Currency");
    	txtNRCurrency.setValue("INR");
//    	txtNRCurrency.setValue(reimbursement.getInrTotalAmount().toString());
    	txtNRCurrency.setReadOnly(true);
    	txtRodNo = new TextField("ROD No");
    	if(reimbursement.getRodNumber() != null && !reimbursement.getRodNumber().equalsIgnoreCase("null")){
    	txtRodNo.setValue(reimbursement.getRodNumber());
    	}
    	txtRodNo.setReadOnly(true);
    	FormLayout firstForm = new FormLayout(txtIntimationNo,txtEventCode,txtClassification,txtNRCurrency);
    	firstForm.setSpacing(true);
//    	setReadOnly(secondForm,true);
    	
    	FormLayout secondForm = new FormLayout(txtRodNo);
    
//    	firstForm.setSpacing(true);
    	HorizontalLayout intimationLayout = new HorizontalLayout(firstForm);
    	intimationLayout.setWidth("70%");
//    	intimationLayout.setComponentAlignment(firstForm, Alignment.TOP_CENTER);
    	VerticalLayout vLayout = new VerticalLayout(intimationLayout,secondForm);
    	vLayout.setComponentAlignment(secondForm, Alignment.MIDDLE_RIGHT);
    	vLayout.setWidth("110%");
    	
    	
    	Panel mainPanel = new Panel(vLayout);
	    mainPanel.addStyleName("girdBorder");
	    Label lable = new Label("INR CHANGE HISTORY");
	    Label lable1 = new Label();
	    lable1.setWidth("600%");
//	    mainPanel.setCaption("INR CHANGE HISTORY");
	    
	    ompClaimRateDetailsObj = ompClaimRateDetailsInstance.get();
	    ompClaimRateDetailsObj.init("", false, false);
	    List<OMPClaimRateChangeTableDto> claimHistoryForCashless = new ArrayList<OMPClaimRateChangeTableDto>();
	    List<OMPClaimRateChange> claimRateChange=	rodBillentryService.getClaimRateHistory(reimbursement.getKey());
	    claimHistoryForCashless = OMPEarlierRodMapper.getInstance().getClaimRateChangeDetails(claimRateChange);
	    if(claimHistoryForCashless != null){
	    ompClaimRateDetailsObj.setTableList(claimHistoryForCashless);
	    ompClaimRateDetailsObj.setCaption("");
	    }
//	    
//	    setProcessingDetailsTable();
	   
	    
	    	    	        	    
	    mainVerticalLayout = new VerticalLayout(lable1,lable,mainPanel,ompClaimRateDetailsObj);
	    mainVerticalLayout.setSpacing(true);
//	    mainVerticalLayout.setWidth("70%");
	    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
	    mainVerticalLayout.setMargin(true);
	    
	    
	//    addListener();
	    
	    setCompositionRoot(mainVerticalLayout);
    	    
    }
	public void initBinder() {
		this.binder = new BeanFieldGroup<OMPClaimProcessorDTO>(
				OMPClaimProcessorDTO.class);
		this.binder.setItemDataSource(bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
			
	}

}
