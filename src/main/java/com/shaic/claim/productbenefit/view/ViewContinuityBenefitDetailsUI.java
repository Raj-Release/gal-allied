package com.shaic.claim.productbenefit.view;

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
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewContinuityBenefitDetailsUI extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RevisedContinuityBenefitTable continuityBenefitDetailsTable;
	
	@EJB
	private PreauthService preauthService;
	
	private ComboBox insuredNameCmb;
	
	private VerticalLayout mainLayout;
	
	private Intimation intimationDtls;
	
	public void init(Intimation intimation){
		BeanItemContainer<SelectValue> selectValueInsuredCont = new BeanItemContainer<SelectValue>(SelectValue.class);
		intimationDtls = intimation;
		SelectValue insuredSelectValue = null;
		Long insuredPatientId = null;
		
		if(intimationDtls.getInsured() != null && intimationDtls.getInsured().getKey() != null){
			insuredPatientId = intimationDtls.getInsured().getKey();
			insuredSelectValue = new SelectValue(intimationDtls.getInsured().getKey(),intimationDtls.getInsured().getInsuredName());
			selectValueInsuredCont.addBean(insuredSelectValue);
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
				if(insured.getInsuredId() != null && !insured.getKey().equals(insuredPatientId)){
					SelectValue insuredselectValue = new SelectValue(insured.getKey(),insured.getInsuredName());
					selectValueInsuredCont.addBean(insuredselectValue);
				}				
			}
		}
		
		insuredNameCmb = new ComboBox("Name of the Insured");
		insuredNameCmb.setContainerDataSource(selectValueInsuredCont);
		insuredNameCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		insuredNameCmb.setItemCaptionPropertyId("value");
//		insuredNameCmb.setValue(selectValueInsuredCont.getIdByIndex(0));
		insuredNameCmb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SelectValue seletedInsured = (SelectValue)event.getProperty().getValue();
				if(seletedInsured != null && seletedInsured.getValue() != null){
				Long insuredKey = Long.valueOf(seletedInsured.getId());
				List<ContinuityBenefitDTO> continuityBenfitDetails = preauthService.getContinuityBenefitDetails(intimationDtls.getPolicy().getKey(),insuredKey);

				if(continuityBenfitDetails != null && !continuityBenfitDetails.isEmpty()){
					continuityBenefitDetailsTable.removeRow();
					continuityBenefitDetailsTable.setTableList(continuityBenfitDetails);
				}
				}
			}
		});
		FormLayout insuredFrmLayout = new FormLayout(insuredNameCmb);
		continuityBenefitDetailsTable.init("", false, false);
		continuityBenefitDetailsTable.setServices(preauthService);
		mainLayout = new VerticalLayout(insuredFrmLayout,continuityBenefitDetailsTable);
		mainLayout.setSpacing(Boolean.FALSE);
		String insuredId = (String) insuredNameCmb.getId();
		List<ContinuityBenefitDTO> continuityBenfitDetails = preauthService.getContinuityBenefitDetails(intimationDtls.getPolicy().getKey(),insuredId != null ? Long.valueOf(insuredId) : null);

		continuityBenefitDetailsTable.setTableList(continuityBenfitDetails);
		setCompositionRoot(mainLayout);
		
	}
}
