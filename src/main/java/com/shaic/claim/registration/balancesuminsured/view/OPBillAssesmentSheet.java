package com.shaic.claim.registration.balancesuminsured.view;

import java.util.Iterator;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.components.GTextField;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OPBillAssesmentSheet extends ViewComponent {
	private VerticalLayout mainLayout;


	private TextField claimNo;

	private TextField insuredIDNo;

	private TextField idNO;

	private TextField policyNo;

	private TextField policyType;

	private TextField coveragePeriodFromStr;

	private TextField coveragePeriodToStr;

	private TextField sumInsuredOP;

	private TextField insuredName;

	private TextField claimantName;

	private TextField age;

	private TextField preparedBy;

	private TextField netPayAmount;

	private TextField deductions;

	private TextField dateofApproval;

	private TextField billReceivedDateStr;

	private TextField relationship;

	private TextField payeeName;

	private TextField amountClaimed;

	private TextField balanceSIAvailable;

	private TextField mainBillDate;

	private TextField totalBillAmount;

	private TextField approvedAmount;

	public void init(OPBillAssesmentSheetDTO dto) {
		FormLayout buildMainLayout = buildMainLayout();

		mainLayout = new VerticalLayout(buildMainLayout);
		mainLayout.setComponentAlignment(buildMainLayout,
				Alignment.MIDDLE_CENTER);
		mainLayout.setMargin(true);
		BeanItem<OPBillAssesmentSheetDTO> item = new BeanItem<OPBillAssesmentSheetDTO>(
				dto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		setReadOnly(buildMainLayout, true);
		setCompositionRoot(mainLayout);

	}

	public FormLayout buildMainLayout() {
		
		Label address1 = new Label("STAR HEALTH AND ALLIED INSURANCE CO. LTD;");
		Label address2 = new Label(
				"KRM Centre, VI Floor, No.2, Harrington Road, Chetpet, Chennai-600031");
		Label address3 = new Label(
				"Toll Free No: 1800 425 2255/ Toll Free Fax: 1800 425 5522, www.starhealth.in");
		VerticalLayout addressVerticalLayout = new VerticalLayout(address1,
				address2, address3);
		addressVerticalLayout.addStyleName("inline");
		addressVerticalLayout.setSpacing(true);
		addressVerticalLayout.setSizeFull();
		ThemeResource res = new ThemeResource(
				"images/logo.png");
		Image img = new Image("", res);
		HorizontalLayout hl = new HorizontalLayout(img, addressVerticalLayout);
		hl.setWidth("500px");
		hl.setSpacing(true);
		Panel claimantDetails = new Panel("Claimant Details");
		claimantDetails.addStyleName(ValoTheme.PANEL_BORDERLESS);
		Panel billDetails = new Panel("Bill Details");
		billDetails.addStyleName(ValoTheme.PANEL_BORDERLESS);

		claimNo = new TextField("Claim No");
		claimNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		idNO = new TextField("ID No");
		idNO.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		policyNo = new TextField("Policy No");
		policyNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		policyType = new TextField("Policy Type");
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		coveragePeriodFromStr = new TextField("Coverage Period From");
		coveragePeriodFromStr.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		coveragePeriodToStr = new TextField("Coverage Period To");
		coveragePeriodToStr.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sumInsuredOP = new TextField("Sum Insured - OP/HC");
		sumInsuredOP.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		insuredName = new TextField("Insured Name");
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		claimantName = new TextField("Claimant Name");
		claimantName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		age = new TextField("Age");
		age.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		relationship = new TextField("Relationship");
		relationship.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		payeeName = new TextField("Payee Name");
		payeeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		amountClaimed = new TextField("Amount Claimed");
		amountClaimed.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		balanceSIAvailable = new TextField(
				"Balance SI Available(Post this Claim)");
		balanceSIAvailable.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		billReceivedDateStr = new TextField("Bill Received Date");
		billReceivedDateStr.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		mainBillDate = new TextField("Main Bill Date");


		mainBillDate.setWidth("100px");

		mainBillDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		totalBillAmount = new TextField("Total Bill Amount");
		totalBillAmount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		deductions = new TextField("Deductions");
		deductions.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		approvedAmount = new TextField("Approved Amount");
		approvedAmount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		netPayAmount = new TextField("Net Pay-Amount");
		
		netPayAmount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dateofApproval = new TextField("Date of Approval");
		dateofApproval.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		preparedBy = new TextField("Prepared By");
		preparedBy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		FormLayout mainlayout = new FormLayout(hl, claimantDetails, claimNo,
				idNO, policyNo, policyType, coveragePeriodFromStr,
				coveragePeriodToStr, sumInsuredOP, insuredName, claimantName, age,
				relationship, payeeName, amountClaimed, balanceSIAvailable,
				billDetails, billReceivedDateStr, mainBillDate, totalBillAmount,
				deductions, approvedAmount, netPayAmount, dateofApproval,
				preparedBy);

		mainlayout.setMargin(true);
		return mainlayout;
	}

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof GTextField) {
				GTextField field = (GTextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

}
