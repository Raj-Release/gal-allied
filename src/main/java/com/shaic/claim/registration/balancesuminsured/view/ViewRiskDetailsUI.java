package com.shaic.claim.registration.balancesuminsured.view;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class ViewRiskDetailsUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TextField txtPolicyNumber;

	private TextField txtRiskName;
	
	private TextField txtFromDate;
	
	private TextField txtToDate;
	
	private TextField txtSectionCode;
	
	private TextField txtCoverCode;
	
	private TextField txtSuminsured;
	
	private TextField txtRelationShip;
	
	private TextField txtAge;
	
	private TextField txtRiskPED;
	
	private TextField txtPortalPED;
	
	private TextField txtEffectiveFromDate;
	
	private TextField txtEffectiveToDate;
	
	@EJB
	private InsuredService insuredService;

	public void init(Intimation intimation){
		
		txtPolicyNumber = new TextField("Policy Number");
		txtPolicyNumber.setValue(intimation.getPolicy().getPolicyNumber());
	    
		txtRiskName = new TextField("Risk Name");
		txtRiskName.setValue(intimation.getInsured().getInsuredName());
		
		txtFromDate = new TextField("Policy From Date");
		
		String fromDate = SHAUtils.formatDate(intimation.getPolicy().getPolicyFromDate());
		txtFromDate.setValue(fromDate);
		
		txtToDate = new TextField("Policy To Date");
		
		String toDate = SHAUtils.formatDate(intimation.getPolicy().getPolicyToDate());
		txtToDate.setValue(toDate);
		
		txtSuminsured = new TextField("Sum Insured");
		
		txtSuminsured.setValue(intimation.getInsured().getInsuredSumInsured() != null ? intimation.getInsured().getInsuredSumInsured().toString():"");
		
		txtRelationShip = new TextField("Relationship");
		txtRelationShip.setValue(intimation.getInsured().getRelationshipwithInsuredId() != null ? intimation.getInsured().getRelationshipwithInsuredId().getValue() : "");
		
		txtSectionCode = new TextField("Section Code");

		txtAge = new TextField("Age");
		txtAge.setValue(intimation.getInsured().getInsuredAge() != null ? intimation.getInsured().getInsuredAge().toString() : "");
		
		txtCoverCode = new TextField("Cover Code");
		
		List<InsuredPedDetails> insuredKeyListByInsuredkey = insuredService.getInsuredKeyListByInsuredkey(intimation.getInsured().getInsuredId());
		
		StringBuffer riskPED = new StringBuffer();
		
		StringBuffer portalPED = new StringBuffer();
		
		if(insuredKeyListByInsuredkey != null){
			for (InsuredPedDetails insuredPedDetails : insuredKeyListByInsuredkey) {
				if(insuredPedDetails.getPedCode() != null){
					riskPED.append(insuredPedDetails.getPedDescription()).append(", ");
				}
				if(insuredPedDetails.getPedDescription() != null){
					portalPED.append(insuredPedDetails.getPedDescription()).append(", ");
				}
			}
		}
		txtRiskPED = new TextField("Risk PED");
		txtRiskPED.setValue(riskPED.toString());
		
		if(! riskPED.toString().equalsIgnoreCase("")){
			txtRiskPED.setDescription(riskPED.toString());
		}
		
		txtPortalPED = new TextField("Portal PED");
		txtPortalPED.setValue(portalPED.toString());
		
		if(! portalPED.toString().equalsIgnoreCase("")){
			txtPortalPED.setDescription(portalPED.toString());
		}
		
		txtEffectiveFromDate = new TextField("Effective Start Date");
		txtEffectiveToDate = new TextField("Effective End Date");
		if(intimation != null && intimation.getPolicy() != null && intimation.getPolicy().getProductType() != null && intimation.getPolicy().getProductType().getKey() != null && intimation.getPolicy().getProductType().getKey().equals(ReferenceTable.GROUP_POLICY) && intimation.getInsured() != null){
			if(intimation.getInsured().getEffectiveFromDate() != null){
				txtEffectiveFromDate.setValue( intimation.getInsured().getEffectiveFromDate() != null ? intimation.getInsured().getEffectiveFromDate().toString() : "");
			}
			if(intimation.getInsured().getEffectiveToDate() != null){
				txtEffectiveToDate.setValue( intimation.getInsured().getEffectiveToDate() != null ? intimation.getInsured().getEffectiveToDate().toString() : "");
			}
		}
		FormLayout firstForm ;
		if(intimation != null && intimation.getPolicy() != null && intimation.getPolicy().getProductType() != null && intimation.getPolicy().getProductType().getKey() != null && intimation.getPolicy().getProductType().getKey().equals(ReferenceTable.GROUP_POLICY)){
			firstForm = new FormLayout(txtPolicyNumber,txtFromDate,txtToDate,txtSectionCode,txtCoverCode,txtEffectiveFromDate,txtEffectiveToDate);
			firstForm.setSpacing(true);
		}else{
			firstForm = new FormLayout(txtPolicyNumber,txtFromDate,txtToDate,txtSectionCode,txtCoverCode);
			firstForm.setSpacing(true);
		}
		
		FormLayout secondForm = new FormLayout(txtRiskName,txtSuminsured,txtRelationShip,txtAge,txtRiskPED,txtPortalPED);
		secondForm.setSpacing(true);
		
		HorizontalLayout mainHor = new HorizontalLayout(firstForm,secondForm);
		mainHor.setSpacing(true);
		
		Panel mainPanel = new Panel(mainHor);
		mainPanel.setCaption("");
		
		firstForm.addStyleName("layoutDesign");
		secondForm.addStyleName("layoutDesign");
		
        setReadOnly(firstForm,true);
        setReadOnly(secondForm,true);

		setCompositionRoot(mainPanel);

	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				if(c instanceof TextField){
					TextField field = (TextField) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}else if(c instanceof TextArea){
					
					TextArea field = (TextArea) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				
			}
		}
	}

}
