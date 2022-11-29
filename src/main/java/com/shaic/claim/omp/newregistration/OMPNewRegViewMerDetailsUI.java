package com.shaic.claim.omp.newregistration;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.omp.carousel.OMPMerDoctorRemarksTable;
import com.shaic.claim.omp.carousel.OMPMerPedDetailsTable;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTable;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class OMPNewRegViewMerDetailsUI extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private TextField txtProposalNo;

	private TextField txtProductName;
	
	private TextField txtProposarName;
	
	private TextField txtEntryDate;
	
	private TextField txtissuingOffice;
	
	private TextField txtProposAddress;
	
	
	private TextField txtinsuredName;
	
	private TextField txtGender;
	
	private TextField txtMobileNo;
	
	private TextField txtProposalPed;
	
	private TextField txtinsuredDob;
	
	private TextField txtSumInsured;
	
	private DateField dateofAppointment;
	
	private TextField txtState;
	
	private TextField txtcity;
	
	private TextField txtDiagnostics;
	
	private TextField txtLabAdder;
	
	private TextField txtLabPhno;
	
	private TextArea txtLabRemarks;
	
	
	
	private Policy policy;
	
	private PolicyService policyService;
	
	private MasterService masterService;
	
	private VerticalLayout mainLayout;
	
	private VerticalLayout policyVerticalLayout;
	
	private OMPMerDoctorRemarksTable merDoctorRemarksTable;
	
	@Inject
    private Instance<OMPMerDoctorRemarksTable> merDoctorRemarksTableInstance;
	
	private OMPMerPedDetailsTable ompmerPedDetail;
	@Inject
	private Instance<OMPMerPedDetailsTable> ompmerPedDetailInstance;
	
	@EJB
	private InsuredService insuredService;
	
	public void setPolicyServiceAndPolicy(PolicyService a_policyService, Policy policy,
			MasterService masterService) {
		//super("View Policy Details");
		this.policyService = a_policyService;
		this.policy = policy;
		this.masterService = masterService;

	}
	
	public void initView() {
		
		Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
		
		buildMainLayout();
		setCaption("MER Details");
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(mainLayout);
		this.setWidth("90%");
		this.setHeight(640, Unit.PIXELS);
		
	}
	
	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		policyVerticalLayout = buildPolicyVLayout();
//		insuredVerticalLayout = buildInsuredVLayout();
		mainLayout.addComponent(policyVerticalLayout);

		return mainLayout;
	}
	
	private VerticalLayout buildPolicyVLayout() {
		policyVerticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() policyVerticalLayout.setImmediate(false);
		policyVerticalLayout.setMargin(true);
		policyVerticalLayout.setSpacing(true);

		Panel buildProposelPanel = buildProposalGridLayout();
		policyVerticalLayout.setSpacing(true);
		
		Panel buildDiagnosticsPanel = buildDiagnositcLayout();
		
		Panel buildLabRemarksPanel = buildLabRemarksLayout();
		
		Panel buildDoctorPanel = buildDoctorRemarksLayout();
		
		policyVerticalLayout.addComponent(buildProposelPanel);
		policyVerticalLayout.addComponent(buildDiagnosticsPanel);
		policyVerticalLayout.addComponent(buildLabRemarksPanel);
		policyVerticalLayout.addComponent(buildDoctorPanel);
		
		merDoctorRemarksTable = merDoctorRemarksTableInstance.get();
		merDoctorRemarksTable.init("", false, false);
		merDoctorRemarksTable.setCaption("Doctor Remarks");
		policyVerticalLayout.addComponent(merDoctorRemarksTable);
		
		ompmerPedDetail = ompmerPedDetailInstance.get();
		ompmerPedDetail.init("", false, false);
		ompmerPedDetail.setCaption("PED Details");
		policyVerticalLayout.addComponent(ompmerPedDetail);
		
		return policyVerticalLayout;
}
	private Panel buildProposalGridLayout() {
		Policy apolicy = policyService.getPolicy(policy.getPolicyNumber());
		txtProposalNo = new TextField("Proposal No");
		txtProposalNo.setValue(apolicy.getProposerCode());
		txtProposalNo.setNullRepresentation("");
		txtProposalNo.setWidth("200px");
		txtProposalNo.setReadOnly(true);
		txtProposalNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtProductName = new TextField("Product Name");
		txtProductName.setNullRepresentation("");
		txtProductName.setWidth("200px");
		txtProductName.setValue(apolicy.getProductName());
		txtProductName.setReadOnly(true);
		txtProductName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtProposarName = new TextField("Proposar Name");
		txtProposarName.setValue(apolicy.getProposerFirstName());
		txtProposarName.setNullRepresentation("");
		txtProposarName.setWidth("200px");
		txtProposarName.setReadOnly(true);
		txtProposarName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtinsuredName = new TextField("Insured Name");
		Insured insured = insuredService.getInsuredByPolicyNo(apolicy.getKey().toString());
		txtinsuredName.setNullRepresentation("");
		txtinsuredName.setWidth("200px");
		txtinsuredName.setValue(insured.getInsuredName());
		txtinsuredName.setReadOnly(true);
		txtinsuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtGender = new TextField("Gender");
		txtGender.setValue(insured != null && insured.getInsuredGender().getValue() !=null ? insured.getInsuredGender().getValue() : "");
		txtGender.setNullRepresentation("");
		txtGender.setWidth("200px");
		txtGender.setReadOnly(true);
		txtGender.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtMobileNo = new TextField("Mobile Number");
		txtMobileNo.setValue(insured.getRegisterdMobileNumber());
		txtMobileNo.setNullRepresentation("");
		txtMobileNo.setWidth("200px");
		txtMobileNo.setReadOnly(true);
		txtMobileNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtProposalPed = new TextField("Proposal PED");
//		txtProposalPed.setValue(insured.getpe);
		txtProposalPed.setNullRepresentation("");
		txtProposalPed.setWidth("200px");
		txtProposalPed.setReadOnly(true);
		txtProposalPed.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtEntryDate = new TextField("System Entry Date");
		txtEntryDate.setValue(String.valueOf(apolicy.getCreatedDate()));
		txtEntryDate.setNullRepresentation("");
		txtEntryDate.setWidth("200px");
		txtEntryDate.setReadOnly(true);
		txtEntryDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtissuingOffice = new TextField("Policy-issuing Office");
		List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService
				.getInsuredOfficeNameByDivisionCode(apolicy.getHomeOfficeCode());
		if (insuredOfficeNameByDivisionCode != null && !insuredOfficeNameByDivisionCode.isEmpty()) {
			txtissuingOffice.setValue(insuredOfficeNameByDivisionCode.get(0)
					.getOrganizationUnitName());
		}
//		txtissuingOffice.setValue(apolicy.getPolOfficeAddr());
		txtissuingOffice.setNullRepresentation("");
		txtissuingOffice.setWidth("200px");
		txtissuingOffice.setReadOnly(true);
		txtissuingOffice.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtProposAddress = new TextField("Proposer Address");
		txtProposAddress.setValue(apolicy.getProposerAddress());
		txtProposAddress.setNullRepresentation("");
		txtProposAddress.setWidth("200px");
		txtProposAddress.setReadOnly(true);
		txtProposAddress.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtinsuredDob = new TextField("Insured DOB");
		String insureddob = SHAUtils.formatDate(insured.getInsuredDateOfBirth());
		txtinsuredDob.setValue(insureddob);
		txtinsuredDob.setNullRepresentation("");
		txtinsuredDob.setWidth("200px");
		txtinsuredDob.setReadOnly(true);
		txtinsuredDob.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtSumInsured = new TextField("Sum Insured");
		txtSumInsured.setValue(String.valueOf(insured.getInsuredSumInsured()));
		txtSumInsured.setNullRepresentation("");
		txtSumInsured.setWidth("200px");
		txtSumInsured.setReadOnly(true);
		txtSumInsured.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		FormLayout leftForm= new FormLayout(txtProposalNo,txtProductName,txtProposarName,txtinsuredName,txtGender,txtMobileNo,txtProposalPed);
		FormLayout rightForm = new FormLayout(txtEntryDate,txtissuingOffice,txtProposAddress,txtinsuredDob,txtSumInsured);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(leftForm,rightForm);
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();
		horizontalLayout.addStyleName("gridBorder");
		setReadOnly(horizontalLayout);
		Panel mainPanel = new Panel(horizontalLayout);
		mainPanel.setCaption("Proposal Details");
		mainPanel.addStyleName("girdBorder");
		
		return mainPanel;
		
	}
	
	private Panel buildDiagnositcLayout() {
		
		dateofAppointment = new DateField("Date Of Appointment");
		dateofAppointment.setWidth("200px");
		dateofAppointment.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dateofAppointment.setReadOnly(true);
//		dateofAppointment.setValue(a);
		txtState = new TextField("State");
		txtState.setNullRepresentation("");
		txtState.setWidth("200px");
		txtState.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtcity = new TextField("City");
		txtcity.setNullRepresentation("");
		txtcity.setWidth("200px");
		txtcity.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtDiagnostics = new TextField("Diagnostics Lab");
		txtDiagnostics.setNullRepresentation("");
		txtDiagnostics.setWidth("200px");
		txtDiagnostics.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtLabAdder = new TextField("Lab Address");
		txtLabAdder.setNullRepresentation("");
		txtLabAdder.setWidth("200px");
		txtLabAdder.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtLabPhno = new TextField("Lab Phone Number");
		txtLabPhno.setNullRepresentation("");
		txtLabPhno.setWidth("200px");
		txtLabPhno.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		FormLayout formLayout = new FormLayout(dateofAppointment,txtState,txtcity,txtDiagnostics,txtLabAdder,txtLabPhno);
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout);
		horizontalLayout.setMargin(false);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();
		horizontalLayout.addStyleName("gridBorder");
		setReadOnly(horizontalLayout);
		Panel panel = new Panel(horizontalLayout);
		panel.setCaption("Diagnostics Center Details");
		panel.addStyleName("girdBorder");
		
		return panel;
	}

	private Panel buildLabRemarksLayout() {
		txtLabRemarks = new TextArea("Lab Remarks");
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(txtLabRemarks);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();
		horizontalLayout.addStyleName("gridBorder");
		Panel panel = new Panel(horizontalLayout);
		panel.setCaption("Lab Remarks");
		panel.addStyleName("girdBorder");
		return panel;
	}
	
	private Panel buildDoctorRemarksLayout() {
		// TODO Auto-generated method stub
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		Panel panel = new Panel(horizontalLayout);

		return panel;
	}
	
	private void setReadOnly(HorizontalLayout horizontalLayout) {
		@SuppressWarnings("deprecation")
		Iterator<Component> a = horizontalLayout.getComponentIterator();
		while (a.hasNext()) {
			Component c = a.next();
			if (c instanceof AbstractField) {
				@SuppressWarnings("rawtypes")
				AbstractField field = (AbstractField) c;
				field.setReadOnly(true);
			}

		}
	}
}
