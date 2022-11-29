package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;

public class RevisedPreauthViewPage extends ViewComponent {
	
	
	private GComboBox cmbPreauth;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private Instance<ViewPreauthDetailsPage> viewPreauthDetailsPageInstance;
	
	private ViewPreauthDetailsPage viewPreauthDetailsPage;
	
	private Panel panel = new Panel();
	
	private VerticalLayout mainForm;
	
	
	private HorizontalLayout referenceHorizontal;
	

	
	public void init(String intimationId){
		
		Intimation intimation = intimationService
				.searchbyIntimationNo(intimationId);

		// String intimationNo ="I/2015/0000890"; //need to implements;

		// Intimation intimation2 =
		// intimationService.getIntimationByNo(intimationNo);
//			PreMedicalMapper premedicalMapper = new PreMedicalMapper();

			List<Preauth> preauthList = preauthService
					.getPreauthByIntimationKey(intimation.getKey());
		

			Label referLabel = new Label("Pre-auth Reference No");
			referenceHorizontal = new HorizontalLayout(referLabel);
			
			cmbPreauth = new GComboBox();
			referenceHorizontal.addComponent(cmbPreauth);
			referenceHorizontal.setSpacing(true);

			cmbPreauth.setNullSelectionAllowed(false);
			cmbPreauth.setWidth("250px");
			
			List<SelectValue> approvedPreauth = new ArrayList<SelectValue>();
			
			for (Preauth singlePreauth : preauthList) {
//				if(singlePreauth.getStatus().getKey().equals(ReferenceTable.PREAUTH_APPROVE_STATUS)||
//						 singlePreauth.getStatus().getKey().equals(ReferenceTable.ENHANCEMENT_APPROVE_STATUS)){
				if(! singlePreauth.getStatus().getKey().equals(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE)){
					SelectValue selected = new SelectValue();
					selected.setId(singlePreauth.getKey());
					selected.setValue(singlePreauth.getPreauthId());
					approvedPreauth.add(selected);
				}
//			     }
			}
			
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			selectValueContainer.addAll(approvedPreauth);
			
			cmbPreauth.setContainerDataSource(selectValueContainer);
			cmbPreauth.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbPreauth.setItemCaptionPropertyId("value");
			
			mainForm = new VerticalLayout(referenceHorizontal);
			
			if(! approvedPreauth.isEmpty()){
			cmbPreauth.setValue(approvedPreauth.get(0));
			viewPreauthDetailsPage = viewPreauthDetailsPageInstance.get();
			viewPreauthDetailsPage.init(approvedPreauth.get(0).getId());
			}
			
			mainForm.addComponent(viewPreauthDetailsPage);
			mainForm.setComponentAlignment(viewPreauthDetailsPage, Alignment.BOTTOM_LEFT);
			
			panel.setContent(mainForm);
			
			addListener();
			
			setCompositionRoot(panel);

	}
	
	public void addListener(){
		
		cmbPreauth.addValueChangeListener(new ValueChangeListener(){

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue value = (SelectValue) event.getProperty().getValue();
				
				if(value != null){
					mainForm.removeComponent(viewPreauthDetailsPage);
					viewPreauthDetailsPage.init(value.getId());
					mainForm.addComponent(viewPreauthDetailsPage);
				}
			}
			
		});
	}
	
	public void setClearReferenceData(){
//    	SHAUtils.setClearReferenceData(referenceData);
		if(this.viewPreauthDetailsPage!=null){
			this.viewPreauthDetailsPage.setClearReferenceData();
			this.viewPreauthDetailsPage = null;
		}
		if(referenceHorizontal != null){
    		referenceHorizontal.removeAllComponents();
    	}
    	if(mainForm != null){
    		mainForm.removeAllComponents();
    	}
//    	this.diagnosisDetailsTableObj.clearObject();
    	  	
    }

}
