package com.shaic.claim.registration.balancesuminsured.view;

import java.util.Iterator;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class BalanceSumInsuredComp extends ViewComponent {
	
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;
	private HorizontalLayout balanceSuminsuredHorizontal;
	private FormLayout formlayoutRight;
	private FormLayout formlayoutLeft;
	
	private TextField txtInsuredName;
	private TextField originalSI;
	private TextField cumBonus;
	private TextField rechargedSI;
	private TextField restoredSI;
	private TextField limitOfCover;
	
	@Inject
	private Instance<ComprehensiveHospitalisationTable> hospitalisationTable;
	
	@Inject
	private Instance<ComprehensiveDeliveryNewBornTable> deliveryNewBornTable;
	
	@Inject
	private Instance<ComprehensiveOutpatientTable> outpatientTable;
	
	@Inject
	private Instance<ComprehensiveHospitalCashTable> hospitalCashTable;
	
	@Inject
	private Instance<ComprehensiveHealthCheckTable> healthCheckTable;
	
	@Inject
	private Instance<ComprehensiveBariatricSurgeryTable> bariatricSurgeryTable;
	
	public void init() {
		VerticalLayout buildMainLayout = buildMainLayout();

		mainLayout = new VerticalLayout(buildMainLayout);
		mainLayout.setComponentAlignment(buildMainLayout,
				Alignment.MIDDLE_CENTER);

		setCompositionRoot(mainLayout);
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				TextField field = (TextField) c;
				field.setWidth("440px");
				field.setReadOnly(true);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
	

	private VerticalLayout buildMainLayout() {
		
		ComprehensiveHospitalisationTable hospitalisationTableInstance = hospitalisationTable.get();
		hospitalisationTableInstance.init("", false, false);
		
		ComprehensiveDeliveryNewBornTable deliveryNewBornTableInstance = deliveryNewBornTable.get();
		deliveryNewBornTableInstance.init("", false, false);
		
		ComprehensiveOutpatientTable outpatientTableInstance = outpatientTable.get();
		outpatientTableInstance.init("", false, false);
		
		ComprehensiveHospitalCashTable hsopitalCashTableInstance = hospitalCashTable.get();
		hsopitalCashTableInstance.init("", false, false);
		
		ComprehensiveHealthCheckTable healthCheckTableInstance = healthCheckTable.get();
		healthCheckTableInstance.init("", false, false);
		
		ComprehensiveBariatricSurgeryTable bariatricSurgeryTableInstance = bariatricSurgeryTable.get();
		bariatricSurgeryTableInstance.init("", false, false);
		
		balanceSuminsuredHorizontal = new HorizontalLayout(
				buildFormLayoutLeft(), buildFormLayoutRight());
		balanceSuminsuredHorizontal.setWidth("100%");
		balanceSuminsuredHorizontal.setHeight("100%");
		
		VerticalLayout vertical = new VerticalLayout(balanceSuminsuredHorizontal,hospitalisationTableInstance,deliveryNewBornTableInstance,outpatientTableInstance,hsopitalCashTableInstance,healthCheckTableInstance,bariatricSurgeryTableInstance);
		vertical.setWidth("100%");
		vertical.setHeight("100%");
		
		return vertical;
	}
	
	private FormLayout buildFormLayoutLeft() {

		txtInsuredName = new TextField("Name of the Insured");
		originalSI = new TextField("Original SI");
		cumBonus = new TextField("Cumulative Bonus");
		formlayoutLeft = new FormLayout(txtInsuredName, originalSI,
				cumBonus);
		formlayoutLeft.addStyleName("layoutDesign");
		formlayoutLeft.setWidth("100%");
		formlayoutLeft.setMargin(true);
		formlayoutLeft.setSpacing(true);
//		formlayoutLeft.setReadOnly(false);
		setReadOnly(formlayoutLeft);
		return formlayoutLeft;
	}
	
	private FormLayout buildFormLayoutRight(){
		
		rechargedSI = new TextField("Recharged SI");
		restoredSI = new TextField("Restored SI");
		limitOfCover = new TextField("Limit of Coverage");
		formlayoutRight = new FormLayout(rechargedSI,restoredSI,limitOfCover);
		formlayoutRight.addStyleName("layoutDesign");
		formlayoutRight.setWidth("100%");
		formlayoutRight.setMargin(true);
		formlayoutRight.setSpacing(true);
//		formlayoutRight.setReadOnly(false);
		setReadOnly(formlayoutRight);
		return formlayoutRight;
	}
	

}
