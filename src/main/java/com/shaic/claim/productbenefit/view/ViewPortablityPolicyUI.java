package com.shaic.claim.productbenefit.view;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ViewPortablityPolicyUI extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


//	@Inject
//	private PortablityPolicyTable portablityTable;
	
	@Inject
	private RevisedPortablityPolicyTable portablityTable;
	
	@EJB
	private PreauthService preauthService;
	
//	@EJB
//	private PremiaService premiaService;
	
	private ComboBox insuredCmb;
	
	private VerticalLayout mainLayout;
	
	private Intimation intimation_;
	
	public void init(Intimation intimation){
		BeanItemContainer<SelectValue> insuredContainer = new BeanItemContainer(SelectValue.class);
		SelectValue insuredSelect = null;
		Long patientId = null;
		intimation_ = intimation; 
		if(intimation.getInsured() != null){
			patientId = intimation.getInsured().getInsuredId();
			insuredSelect = new SelectValue(intimation.getInsured().getInsuredId(), intimation.getInsured().getInsuredName());
			insuredContainer.addBean(insuredSelect);
		}
		List<Insured> insuredList = intimation.getPolicy().getInsured();
		
		
		if(intimation.getPolicy() != null && intimation.getPolicy().getProduct() != null && ReferenceTable.getGMCProductCodeList().containsKey(intimation.getPolicy().getProduct().getCode())){
			if(intimation.getInsured() != null){
				if(intimation.getInsured().getDependentRiskId() != null){
					insuredList = preauthService.getInsuredListForGMC(intimation.getInsured().getDependentRiskId());	
				}else{
					insuredList = preauthService.getInsuredListForGMC(intimation.getInsured().getInsuredId());	
				}
			}else{
				insuredList = intimation.getPolicy().getInsured();
			}
		}
		
		
		
		if(insuredList != null && !insuredList.isEmpty()){
			for (Insured insured : insuredList) {
				if(insured.getInsuredId() != null && !insured.getInsuredId().equals(patientId)){
					SelectValue insuredselectValue = new SelectValue(insured.getInsuredId(),insured.getInsuredName());
					insuredContainer.addBean(insuredselectValue);
				}				
			}
		}
		
		insuredCmb = new ComboBox("Name of the Insured");
		insuredCmb.setContainerDataSource(insuredContainer);
		insuredCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		insuredCmb.setItemCaptionPropertyId("value");
		insuredCmb.setValue(insuredContainer.getIdByIndex(0));
		insuredCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue seletedInsured = (SelectValue)event.getProperty().getValue();
				
				/**
				 * Below code was Added as part of CR R1080
				 */
				List<PortablitiyPolicyDTO> portablityDetails = preauthService.getPortablityPolicyDetails(intimation_.getPolicy().getPolicyNumber(),seletedInsured.getValue().toString());

				if(portablityDetails != null && !portablityDetails.isEmpty()){
					portablityTable.removeRow();
					portablityTable.setTableList(portablityDetails);
				}
			}
		});
		FormLayout insuredFrmLayout = new FormLayout(insuredCmb);
		
		portablityTable.init("", false, false);
		portablityTable.setServices(preauthService);
		mainLayout = new VerticalLayout(insuredFrmLayout,portablityTable);
		mainLayout.addComponent(new FormLayout(new Label("<B>YES - denotes waiver/exclusion  allowed</B>",ContentMode.HTML),new Label("<B>NO - denotes waiver /exclusion not allowed</B>",ContentMode.HTML)));
		mainLayout.setSpacing(Boolean.FALSE);
		List<PortablitiyPolicyDTO> portablityDetails = preauthService.getPortablityPolicyDetails(intimation.getPolicy().getPolicyNumber(),((SelectValue)(insuredCmb.getValue())).getValue().toString());
//		portablityDetails = premiaService.getPortabilityPolicyDetailsFromPremia(intimation.getPolicy().getPolicyNumber(),((SelectValue)(insuredCmb.getValue())).getId());
		portablityTable.setTableList(portablityDetails);
		setCompositionRoot(mainLayout);
	}

}
