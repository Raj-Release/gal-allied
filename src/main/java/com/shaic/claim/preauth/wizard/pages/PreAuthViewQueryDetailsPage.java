package com.shaic.claim.preauth.wizard.pages;

import java.util.Iterator;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PreAuthViewQueryDetailsPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PreAuthViewQueryDetailsService preAuthViewQueryDetailsService;
	
	private TextField txtIntimationNo;
	private TextField txtClaimNo;
	private TextField txtPolicyNo;
	private TextField txtProductName;
	private TextField txtClaimType;
	private TextField txtInsuredPatientName;
	private TextField txtHospitalName;
	private TextField txtHospitalCity;
	private TextField txtHospitalType;
	private TextField txtDateOfAdmission;
	private TextField txtdiagnosis;
	private TextField txtQueryRaisedByRole;
	private TextField txtQueryRaisedByIdOrName;
	private TextField txtQueryRaisedByDesignation;
	private TextField txtQueryRaisedDate;
	private TextField txtQueryRemarks;
	private TextField txtQueryStatus;
	
	private VerticalLayout mainLayout;
	
	private FormLayout formLayout;
	
	private BeanFieldGroup<PreAuthViewQueryDetailsPageDTO> binder;
	
	public void init(PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO){
		initBinder();
		buildMainLayout();
		formLayout = new FormLayout(txtIntimationNo, txtClaimNo,
				txtPolicyNo, txtProductName, txtClaimType,
				txtInsuredPatientName, txtHospitalName, txtHospitalCity,
				txtHospitalType, txtDateOfAdmission, txtdiagnosis,
				txtQueryRaisedByRole, txtQueryRaisedByIdOrName,
				txtQueryRaisedByDesignation, txtQueryRaisedDate,
				txtQueryRemarks, txtQueryStatus);
		setReadOnly(formLayout, false);
		mainLayout = new VerticalLayout(formLayout);		
		PreAuthViewQueryDetailsPageDTO viewDetailsDTo = preAuthViewQueryDetailsService.search(preAuthPreviousQueryDetailsTableDTO);
		
		txtIntimationNo.setValue(viewDetailsDTo.getIntimationNo());
		txtPolicyNo.setValue(viewDetailsDTo.getPolicyNo());
		txtProductName.setValue(viewDetailsDTo.getProductName());
		txtInsuredPatientName.setValue(viewDetailsDTo.getInsuredPatientName());
		txtHospitalName.setValue(viewDetailsDTo.getHospitalName());
		txtHospitalCity.setValue(viewDetailsDTo.getHospitalCity());
		txtHospitalType.setValue(viewDetailsDTo.getHospitalType());
		txtDateOfAdmission.setValue(viewDetailsDTo.getDateOfAdmission());
		txtQueryRaisedDate.setValue(viewDetailsDTo.getQueryRaisedDate());
		txtQueryRemarks.setValue(viewDetailsDTo.getQueryRemarks());
		txtQueryStatus.setValue(viewDetailsDTo.getQueryStatus());
		
		setCompositionRoot(mainLayout);
	}
	
	public void initBinder(){
		this.binder = new BeanFieldGroup<PreAuthViewQueryDetailsPageDTO>(
				PreAuthViewQueryDetailsPageDTO.class);
		this.binder.setItemDataSource(new PreAuthViewQueryDetailsPageDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void buildMainLayout(){
		
		txtIntimationNo = binder.buildAndBind("Intimation No",
				"intimationNo", TextField.class);
		txtClaimNo = binder.buildAndBind("Claim No",
				"claimNo", TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy No",
				"policyNo", TextField.class);
		txtProductName = binder.buildAndBind("Product Name",
				"productName", TextField.class);
		txtClaimType = binder.buildAndBind("Claim Type",
				"claimType", TextField.class);
		txtInsuredPatientName = binder.buildAndBind("Insured Patient Name",
				"insuredPatientName", TextField.class);
		txtHospitalName = binder.buildAndBind("Hospital Name",
				"hospitalName", TextField.class);
		txtHospitalCity = binder.buildAndBind("Hospital City",
				"hospitalCity", TextField.class);
		txtHospitalType = binder.buildAndBind("Hospital Type",
				"hospitalType", TextField.class);
		txtDateOfAdmission = binder.buildAndBind("Date Of Admission",
				"dateOfAdmission", TextField.class);
		txtdiagnosis = binder.buildAndBind("Diagnosis",
				"diagnosis", TextField.class);
		txtQueryRaisedByRole = binder.buildAndBind("Query Raised By Role",
				"queryRaisedByRole", TextField.class);
		txtQueryRaisedByIdOrName = binder.buildAndBind("Raised By Id / Name",
				"queryRaisedByIdOrName", TextField.class);
		txtQueryRaisedByDesignation = binder.buildAndBind("Query Raised By Designation",
				"queryRaisedByDesignation", TextField.class);
		txtQueryRaisedDate = binder.buildAndBind("Query Raised Date",
				"queryRaisedDate", TextField.class);
		txtQueryRemarks = binder.buildAndBind("Query Remarks",
				"queryRemarks", TextField.class);
		txtQueryStatus = binder.buildAndBind("Query Status",
				"queryStatus", TextField.class);
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				TextField field = (TextField) c;
				field.setWidth("250px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
	
	
	
	

}
