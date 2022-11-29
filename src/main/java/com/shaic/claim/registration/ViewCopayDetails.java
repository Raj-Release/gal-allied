package com.shaic.claim.registration;

import java.util.Date;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.MasCopay;
import com.shaic.domain.Policy;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewCopayDetails extends Window {
	private Panel copayPanel;
	private VerticalLayout mainLayout;
	private Label compulsaryCopaylbl;
	private Label voluntaryCopaylbl;
	private Label percentagelbl;
	private Label copayAmountlbl;
	private Label totalLbl;
	private TextField compulsaryCopayAmountTxt;
	private TextField voluntaryCopayAmountTxt;
	private TextField compulsaryCopayPercentTxt;
	private TextField voluntaryCopayPercentTxt;
	private TextField totalAmountTxt;

	public ViewCopayDetails() {
		super("Co-pay Details");
		this.setModal(true);
		this.setHeight("250px");
		this.setWidth("500px");
		copayPanel = buildCopayPanel();
		this.setContent(copayPanel);
	}

	private Panel buildCopayPanel() {
		copayPanel = new Panel();
		mainLayout = buildVerticalLayout();
		copayPanel.setContent(mainLayout);

		return copayPanel;
	}

	private VerticalLayout buildVerticalLayout() {
		mainLayout = new VerticalLayout(buildGridLayout());
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
	
		return mainLayout;

	}

	private HorizontalLayout buildGridLayout() {

		compulsaryCopaylbl = new Label("Percentage ");
		voluntaryCopaylbl = new Label("Amount");
		percentagelbl = new Label("Percentage");
		copayAmountlbl = new Label("Amount");
		totalLbl = new Label("Total");

		compulsaryCopayPercentTxt = new TextField("Compulsory Co-Pay");
		compulsaryCopayAmountTxt = new TextField();

		voluntaryCopayPercentTxt = new TextField("Voluntary Co-Pay");
		voluntaryCopayAmountTxt = new TextField();
		totalAmountTxt = new TextField();


		FormLayout secondFormLayout = new FormLayout(compulsaryCopaylbl,
				compulsaryCopayPercentTxt,voluntaryCopayPercentTxt);
		secondFormLayout.addStyleName("layoutDesign");
		secondFormLayout.setWidth("100.0%");
		secondFormLayout.setMargin(true);
		secondFormLayout.setSpacing(true);
//		secondFormLayout.setReadOnly(false);
		FormLayout threedFormLayout = new FormLayout(voluntaryCopaylbl,
				compulsaryCopayAmountTxt, voluntaryCopayAmountTxt,
				totalAmountTxt);
		threedFormLayout.addStyleName("layoutDesign");
		threedFormLayout.setWidth("100.0%");
		threedFormLayout.setMargin(true);
		threedFormLayout.setSpacing(true);

//		threedFormLayout.setReadOnly(false);
		HorizontalLayout copayhLayout = new HorizontalLayout();
//		copayhLayout.addComponent(firstFormLayout);
		copayhLayout.addComponent(secondFormLayout);
//		copayhLayout.addComponent(threedFormLayout);
		copayhLayout.setWidth("100.0%");
		copayhLayout.setHeight("100.0%");
		return copayhLayout;
	}

	public void showValues(MasCopay copay, Policy policy, Date strDob) {

		if (copay != null && policy != null  ) {
			if(copay.getEntryAgeFrom()==null || copay.getEntryAgeFrom() <= Long.parseLong(SHAUtils.getAge(strDob))  ){
			compulsaryCopayPercentTxt.setValue((String) (copay
					.getMaxPercentage() == null ? 0 : copay.getMaxPercentage()
					.toString() + "%"));
			Double compulsaryCopayAmount = (double) (((policy.getTotalSumInsured()!= null ? policy.getTotalSumInsured() : 0 )* (copay.getMaxPercentage()!= null ? copay.getMaxPercentage() : 0 )) / 100);
			compulsaryCopayAmountTxt.setValue(compulsaryCopayAmount.toString().split("\\.")[0]);
			voluntaryCopayPercentTxt.setValue("0");
			voluntaryCopayAmountTxt.setValue("0");
			Double totalCopay = (Double.parseDouble(compulsaryCopayAmountTxt
					.getValue()) + Double.parseDouble(voluntaryCopayAmountTxt
					.getValue()));
			totalAmountTxt.setValue(totalCopay.toString().split("\\.")[0]);
			}
			else
			{
				compulsaryCopayPercentTxt.setValue("0");
				compulsaryCopayAmountTxt.setValue("0");
				voluntaryCopayPercentTxt.setValue("0");
				voluntaryCopayAmountTxt.setValue("0");
				totalAmountTxt.setValue("0");
			}
		} else {
			compulsaryCopayPercentTxt.setValue("0");
			compulsaryCopayAmountTxt.setValue("0");
			voluntaryCopayPercentTxt.setValue("0");
			voluntaryCopayAmountTxt.setValue("0");
			totalAmountTxt.setValue("0");

		}

		compulsaryCopayPercentTxt.setReadOnly(true);
		compulsaryCopayPercentTxt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		compulsaryCopayAmountTxt.setReadOnly(true);
		compulsaryCopayAmountTxt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		voluntaryCopayPercentTxt.setReadOnly(true);
		voluntaryCopayPercentTxt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		voluntaryCopayAmountTxt.setReadOnly(true);
		voluntaryCopayAmountTxt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		totalAmountTxt.setReadOnly(true);
		totalAmountTxt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	}

}
