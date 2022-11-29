package com.shaic.claim.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.PreauthQuery;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PreAuthPreviousQueryDetailsTableView extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;

	@Inject
	private HospitalService hospitalService;
	

	@Inject
	private MasterService masterService;

	// Components for Claim Details
	private TextField txtIntimationNo;
	private TextField txtClaimNumber;
	private TextField txtPolicyNo;
	private TextField txtProductName;
	private TextField txtClaimType;
	private TextField txtInsuredPatientName;
	private TextField txtHospitalName;
	private TextField txtHospitalType;
	private TextField txtHosptialCity;
	private TextField txtDateOfAdmission;
	private TextField txtDiagnosis;
	// Components for QueryDetails
	private TextField txtQueryRaisedByRole;
	private TextField txtQueryRaisedByIdOrName;
	private TextField txtQueryRaisedByDesignation;
	private TextField txtQueryRaisedDate;
	private TextArea txtQueryRemarks;
	private TextField txtQueryStatus;

	// Layout
	private VerticalLayout vrtMainLayout;
	private HorizontalLayout horizontalLayout;
	private VerticalLayout verticalLayout;
	private FormLayout frmClaimDetailsLayout;
	private FormLayout frmQueryDetailslayout;
	private Panel pnlClaimDetails;
	private Panel pnlQueryDetails;

	
	private PreAuthPreviousQueryDetailsTableDTO bean;
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private ClaimService claimService;
	
	

	// button for dms
	private Button btnViewQueryLetter;

	// binder
	private BeanFieldGroup<PreAuthPreviousQueryDetailsTableViewDTO> binder;

	private void initBinder() {
		this.binder = new BeanFieldGroup<PreAuthPreviousQueryDetailsTableViewDTO>(
				PreAuthPreviousQueryDetailsTableViewDTO.class);
		this.binder
				.setItemDataSource(new PreAuthPreviousQueryDetailsTableViewDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private void bindingComponents() {
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
				TextField.class);
		txtClaimNumber = binder.buildAndBind("Claim Number", "claimNumber",
				TextField.class);
		txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
				TextField.class);
		txtProductName = binder.buildAndBind("Product Name", "productName",
				TextField.class);
		txtProductName.setWidth("450px");
		txtClaimType = binder.buildAndBind("Claim Type", "claimType",
				TextField.class);
		txtInsuredPatientName = binder.buildAndBind("Insured Patient Name",
				"insuredPatientName", TextField.class);
		txtHospitalName = binder.buildAndBind("Hospital Name", "hospitalName",
				TextField.class);
		txtHospitalType = binder.buildAndBind("Hospital Type", "hospitalType",
				TextField.class);
		txtHosptialCity = binder.buildAndBind("Hospital City", "hosptialCity",
				TextField.class);
		txtDateOfAdmission = binder.buildAndBind("Date of Admission",
				"dateOfAdmission", TextField.class);
		txtDiagnosis = binder.buildAndBind("Diagnosis", "diagnosis",
				TextField.class);
		txtQueryRaisedByRole = binder.buildAndBind("Query Raised By Role",
				"queryRaisedByRole", TextField.class);
		txtQueryRaisedByIdOrName = binder.buildAndBind(
				"Query Raised By ID/Name", "queryRaisedByIdOrName",
				TextField.class);
		txtQueryRaisedByDesignation = binder.buildAndBind(
				"Query Raised By Designation", "queryRaisedByDesignation",
				TextField.class);
		txtQueryRaisedDate = binder.buildAndBind("Query Raised Date",
				"queryRaisedDate", TextField.class);
		txtQueryRemarks = binder.buildAndBind("Query Remarks", "queryRemarks",
				TextArea.class);
		txtQueryStatus = binder.buildAndBind("Query Status", "queryStatus",
				TextField.class);
	}

	@SuppressWarnings("unchecked")
	public void init(
			PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO) {
		initBinder();
		bindingComponents();
		bean = preAuthPreviousQueryDetailsTableDTO;
		if (preAuthPreviousQueryDetailsTableDTO != null) {
			String diagnosis = "";
			
			List<PedValidation> pedValidationList = new ArrayList<PedValidation>();
			Long transactionKey = 0l;
			if(preAuthPreviousQueryDetailsTableDTO.getPreAuth() != null){
				transactionKey = preAuthPreviousQueryDetailsTableDTO.getPreAuth().getKey();
			}else if(preAuthPreviousQueryDetailsTableDTO.getReimbursement() != null){
				transactionKey = preAuthPreviousQueryDetailsTableDTO.getReimbursement().getKey();
			}
			
			Query queryDiagnosis = entityManager
					.createNamedQuery("PedValidation.findByPreauthKey");
			queryDiagnosis.setParameter("preauthKey",
					transactionKey);
			pedValidationList = (List<PedValidation>) queryDiagnosis
						.getResultList();
			
			for (PedValidation pedValidation : pedValidationList) {
				diagnosis = masterService.getDiagnosis(
						pedValidation.getDiagnosisId(), entityManager);
			}
			preAuthPreviousQueryDetailsTableDTO.setDiagnosis(diagnosis);
			txtDiagnosis.setValue(preAuthPreviousQueryDetailsTableDTO
					.getDiagnosis());
			
			if(preAuthPreviousQueryDetailsTableDTO.getQueryRaisedDate()!=null){
				Date tempDate = SHAUtils.formatTimestamp(preAuthPreviousQueryDetailsTableDTO
						.getQueryRaisedDate().toString());
				txtQueryRaisedDate.setValue(SHAUtils.formatDate(tempDate));
			}
			
			txtQueryRemarks
					.setValue(preAuthPreviousQueryDetailsTableDTO
							.getQueryRemarks() != null ? preAuthPreviousQueryDetailsTableDTO
							.getQueryRemarks() : "");

			txtQueryStatus
					.setValue(preAuthPreviousQueryDetailsTableDTO
							.getQueryStatus() != null ? preAuthPreviousQueryDetailsTableDTO
							.getQueryStatus() : "");

			Query query = entityManager
					.createNamedQuery("PreauthQuery.findKey");
			query.setParameter("preAuthKey",
					preAuthPreviousQueryDetailsTableDTO.getKey());
			List<PreauthQuery> singleResult = (List<PreauthQuery>) query
					.getResultList();
			if (!singleResult.isEmpty()) {
				PreauthQuery preAuthQuery = singleResult.get(0);
				if (preAuthQuery != null) {
					if (preAuthQuery.getPreauth() != null) {
						if (preAuthQuery.getPreauth().getIntimation() != null) {
							txtIntimationNo
									.setValue(preAuthQuery.getPreauth()
											.getIntimation().getIntimationId() != null ? preAuthQuery
											.getPreauth().getIntimation()
											.getIntimationId()
											: "");
							txtInsuredPatientName
									.setValue(preAuthQuery.getPreauth()
											.getIntimation()
											.getInsuredPatientName() != null ? preAuthQuery
											.getPreauth().getIntimation()
											.getInsuredPatientName()
											: "");
							
							Date tempDate = SHAUtils.formatTimestamp(preAuthQuery
									.getPreauth().getIntimation()
									.getAdmissionDate().toString());
							txtDateOfAdmission.setValue(SHAUtils.formatDate(tempDate));
							
							
							
							/*txtDateOfAdmission.setValue(preAuthQuery
									.getPreauth().getIntimation()
									.getAdmissionDate() != null ? preAuthQuery
									.getPreauth().getIntimation()
									.getAdmissionDate().toString() : "");*/
						}
						if (preAuthQuery.getPreauth().getClaim() != null) {
							txtClaimNumber
									.setValue(preAuthQuery.getPreauth()
											.getClaim().getClaimId() != null ? preAuthQuery
											.getPreauth().getClaim()
											.getClaimId()
											: "");
						}
						if (preAuthQuery.getPreauth().getPolicy() != null) {
							txtPolicyNo
									.setValue(preAuthQuery.getPreauth()
											.getPolicy().getPolicyNumber() != null ? preAuthQuery
											.getPreauth().getPolicy()
											.getPolicyNumber()
											: "");
							if (preAuthQuery.getPreauth().getPolicy()
									.getProduct() != null) {
								txtProductName.setValue(preAuthQuery
										.getPreauth().getPolicy().getProduct()
										.getValue() != null ? preAuthQuery
										.getPreauth().getPolicy().getProduct()
										.getValue() : "");
							}
						}
						if (preAuthQuery.getPreauth().getClaim() != null) {
							txtClaimType
									.setValue(preAuthQuery.getPreauth()
											.getClaim().getClaimType() != null ? preAuthQuery
											.getPreauth().getClaim()
											.getClaimType().getValue()
											: "");
						}
					}
					
					if(preAuthQuery.getCreatedDate() != null){
						String formatDate = SHAUtils.formatDate(preAuthQuery.getCreatedDate());
						txtQueryRaisedDate.setValue(formatDate);
					}
					txtQueryRaisedByRole.setValue(preAuthQuery.getCreatedBy());
					txtQueryRaisedByIdOrName.setValue(preAuthQuery.getCreatedBy());
					
				}
				Hospitals hospital = hospitalService
						.getHospitalDetailsByKey(preAuthQuery.getPreauth()
								.getIntimation().getHospital());
				if (hospital != null) {
					txtHospitalName
							.setValue(hospital.getName() != null ? hospital
									.getName() : "");
					txtHospitalType
							.setValue(hospital.getHospitalType() != null ? hospital
									.getHospitalType().getValue() : "");
					txtHosptialCity
							.setValue(hospital.getCity() != null ? hospital
									.getCity() : "");
				}
				
				
			}
		}

		frmClaimDetailsLayout = new FormLayout(txtIntimationNo, txtClaimNumber,
				txtPolicyNo, txtProductName, txtClaimType,
				txtInsuredPatientName, txtHospitalName, txtHosptialCity,
				txtHospitalType, txtDateOfAdmission, txtDiagnosis);
		frmQueryDetailslayout = new FormLayout(txtQueryRaisedByRole,
				txtQueryRaisedByIdOrName, txtQueryRaisedByDesignation,
				txtQueryRaisedDate, txtQueryRemarks, txtQueryStatus);
		pnlClaimDetails = new Panel("Claim Details");
		pnlClaimDetails.setContent(frmClaimDetailsLayout);
		pnlClaimDetails.setWidth("650px");

		pnlQueryDetails = new Panel("Query Details");
		pnlQueryDetails.setContent(frmQueryDetailslayout);
		pnlQueryDetails.setWidth("650px");
		verticalLayout = new VerticalLayout(pnlClaimDetails, pnlQueryDetails);
		verticalLayout.setSpacing(true);
		horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(verticalLayout);
		horizontalLayout.setSpacing(true);
		btnViewQueryLetter = new Button("View Query Letter");
		horizontalLayout.addComponent(btnViewQueryLetter);
		vrtMainLayout = new VerticalLayout(horizontalLayout);
		vrtMainLayout.setComponentAlignment(horizontalLayout,
				Alignment.MIDDLE_CENTER);
		vrtMainLayout.setMargin(true);
		setReadOnly(frmClaimDetailsLayout, true);
		setReadOnly(frmQueryDetailslayout, true);
		
		addListener();
		setCompositionRoot(vrtMainLayout);

	}
	
   public void addListener(){
		
	   btnViewQueryLetter.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;
			

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getPreAuth() != null){
					viewUploadedQueryDocumentDetails(bean.getPreAuth().getIntimation().getIntimationId(),SHAConstants.QUERY);
				}
     
			}
		});
	}

	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				try {
					TextField field = (TextField) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				} catch (ClassCastException e) {
					TextArea field = (TextArea) c;
					field.setWidth("250px");
					field.setNullRepresentation("");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
			}
		}
	}
	
	public void viewUploadedQueryDocumentDetails(String intimationNo,String docType) {

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());	
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getQueryDocumentDetailsData(intimationNo,docType);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		Window popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);

	}

}
