package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.Iterator;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.dto.ViewRejectionDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReimbursementRejection;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewRejectionDetailsPage extends ViewComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ViewRejectionDTO bean;
	
	private BeanFieldGroup<ViewRejectionDTO> binder;
	
	private TextField txtIntimationNo;
	
	private TextField txtClaimNo;
	
	private TextField txtPolicyNo;
	
	private TextField txtProductName;
	
	private TextField txtClaimType;
	
	private TextField txtInsuredPatient;
	
	private TextField txtHospitaName;
	
	private TextField txtHospitalCity;
	
	private TextField txtHospitalType;
	
	private TextField txtDateOfAdmission;
	
	private TextField txtDiagnosis;
	
	private TextField txtRejectedRole;
	
	private TextField txtRejectedByName;
	
	private TextField txtRejectedDate;
	
	private TextField txtRejectCategory;
	
	private TextArea txtRejectionRemarks;
	
	private TextField txtDraftedDate;
	
	private TextArea txtLetterRemarks;
	
	private TextField txtApprovedDate;
	
	private TextArea txtDisapprovedRemarks;
	
	private TextArea txtRedraftRemarks;
	
	private TextField txtRejectionStatus;
	
	private Button rejectionBtn;
	
	private ReimbursementRejectionDetailsDto dto;
	
	
	@EJB
	private AcknowledgementDocumentsReceivedService rejectionService;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	////private static Window popup;
	
	@EJB
	private HospitalService hospitalService;
	
	private ReimbursementRejection reimbursementRejection;
	
	public void init(Long rejectionKey, ReimbursementRejectionDetailsDto dto){
		this.dto = dto;
	}
	public void init(Long rejectionKey){
		if(rejectionKey != null){
		this.bean = rejectionService.getViewRejectionDTO(rejectionKey);
		}
		
		if(this.bean== null){
			this.bean = new ViewRejectionDTO();
		}
		
		reimbursementRejection = rejectionService.getReimbursementRejection(rejectionKey);
		
		
		if(this.bean != null && this.bean.getClaim() != null){
			Long hospitalId =this.bean.getClaim().getIntimation().getHospital();
			
			Hospitals hospitals = hospitalService.getHospitalById(hospitalId);
			if(hospitals != null){
				this.bean.setHospitalName(hospitals.getName());
				this.bean.setHospitalCity(hospitals.getCity());
				this.bean.setHospitalType(hospitals.getHospitalTypeName());
			}
			
		}
		this.binder = new BeanFieldGroup<ViewRejectionDTO>(ViewRejectionDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
         Label lable = new Label("<H3>Claim Details</H3>",ContentMode.HTML);
		
		Label lable1 = new Label("<H3>Rejection Details</H3>",ContentMode.HTML);
		
		rejectionBtn = new Button("View Rejection Letter");
		
		txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtClaimNo = (TextField) binder.buildAndBind("Claim Number","claimNo",TextField.class);
		txtPolicyNo = (TextField) binder.buildAndBind("Policy No","policyNo",TextField.class);
		txtProductName = (TextField) binder.buildAndBind("Product Name","productName",TextField.class);
		txtClaimType = (TextField) binder.buildAndBind("Claim Type","claimType",TextField.class);
		txtInsuredPatient = (TextField) binder.buildAndBind("Insured Patient Name","insuredPatientName",TextField.class);
		txtHospitaName = (TextField) binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		txtHospitalCity = (TextField) binder.buildAndBind("Hospital City","hospitalCity",TextField.class);
		txtHospitalType = (TextField) binder.buildAndBind("Hospital Type","hospitalType",TextField.class);
		txtDateOfAdmission = (TextField) binder.buildAndBind("Date of Admission","admissionDate",TextField.class);
		txtDiagnosis = (TextField) binder.buildAndBind("Diagnosis","diagnosis",TextField.class);
		
		
		TextField dummyText = new TextField("View Rejection Details");
		dummyText.setVisible(false);
		
		
		txtRejectedRole = (TextField) binder.buildAndBind("Rejected By Role","rejectedRole",TextField.class);
		txtRejectedByName = (TextField) binder.buildAndBind("Rejected By ID/Name","rejectedName",TextField.class);
		txtRejectedDate = (TextField) binder.buildAndBind("Rejected Date","rejectedDate",TextField.class);
		txtRejectionRemarks = (TextArea) binder.buildAndBind("Rejection Remarks","rejectionRemarks",TextArea.class);
		txtDraftedDate = (TextField) binder.buildAndBind("Rejection Letter Drafted Date","draftedDate",TextField.class);
		txtRejectCategory = (TextField) binder.buildAndBind("Rejection Category","rejCategValue",TextField.class);
		txtLetterRemarks = (TextArea) binder.buildAndBind("Rejection Letter Remarks","letterRemarks",TextArea.class);
		txtApprovedDate = (TextField) binder.buildAndBind("Rejection Approved / Disapproved / Redraft date","reDraftedDate",TextField.class);
		txtRedraftRemarks = (TextArea) binder.buildAndBind("Rejection Redraft Remarks","reDraftRemarks",TextArea.class);
		txtRejectionStatus = (TextField) binder.buildAndBind("Rejection Status","rejectionStatus",TextField.class);
		txtDisapprovedRemarks =(TextArea) binder.buildAndBind("Rejection Disapproved Remarks","disApprovalRemarks",TextArea.class);
		
		
		if(reimbursementRejection != null){
			String diagnosisName = rejectionService.getDiagnosisBasedOnRODKey(reimbursementRejection.getReimbursement().getKey());
			txtDiagnosis.setValue(diagnosisName);
			
			if(reimbursementRejection != null){
//				Reimbursement reimbursement = rejectionService.getReimbursement(reimbursementRejection.getReimbursement().getKey());
//				if(reimbursement != null){
						txtRejectionStatus.setValue(reimbursementRejection.getStatus() != null ? reimbursementRejection.getStatus().getProcessValue() : "");
//					}
				}
		}
		
		    
		if(this.dto != null){
			txtRejectedRole.setValue(this.dto.getRejectedByRole());
			txtRejectedByName.setValue(this.dto.getRejectedByRole());
//			txtRejectionStatus.setValue(this.dto.getRejectionStatus());
		}
//		FormLayout secondForm= new FormLayout(txtRejectedRole,txtRejectedByName,txtRejectedDate,txtRejectionRemarks,txtDraftedDate,txtLetterRemarks,
//				txtApprovedDate,txtRejectionRemarks,txtDisapprovedRemarks,txtRedraftRemarks,txtRejectionStatus);
//		secondForm.setCaption("Query Details");
//		secondForm.setStyleName("layoutDesign");
//		FormLayout dummyLayout =  new FormLayout();
//		dummyLayout.setWidth("250px");
//	    dummyLayout.setStyleName("layoutDesign");
	    

		FormLayout firstForm = new FormLayout(lable,txtIntimationNo,txtClaimNo,txtPolicyNo
			,txtProductName,txtClaimType,txtInsuredPatient,txtHospitaName,txtHospitalCity,txtHospitalType,txtDateOfAdmission,txtDiagnosis
			,lable1,txtRejectedRole,txtRejectedByName,txtRejectedDate,txtRejectionRemarks,txtDraftedDate,txtRejectCategory,txtLetterRemarks,
			txtApprovedDate,txtRejectionRemarks,txtDisapprovedRemarks,txtRedraftRemarks,txtRejectionStatus);
	
//		firstForm.setCaption("Claim Details");
		firstForm.addStyleName("layoutDesign");
		
		HorizontalLayout claimhor = new HorizontalLayout(firstForm);
		claimhor.setComponentAlignment(firstForm, Alignment.TOP_RIGHT);
		
		setReadOnly(firstForm,true);
//		setReadOnly(secondForm,true);
		
		
		FormLayout dummyQuery =  new FormLayout();
		dummyQuery.setWidth("250px");
		dummyQuery.setStyleName("layoutDesign");
//		HorizontalLayout queryLayout = new HorizontalLayout(secondForm);
//		queryLayout.setComponentAlignment(secondForm, Alignment.TOP_RIGHT);
		
//		Panel queryPanel = new Panel(queryLayout);
//		queryPanel.setCaption("Query Details");
//		
	    VerticalLayout verticalMain = new VerticalLayout(rejectionBtn,claimhor);
	    verticalMain.setComponentAlignment(rejectionBtn, Alignment.TOP_RIGHT);
	    verticalMain.setStyleName("layoutDesign");
	    
	    FormLayout mainPanel = new FormLayout(verticalMain);
	    mainPanel.addStyleName("layoutDesign");
	
	    mainPanel.setCaption("View Query Details");
	    
	    addListener();
	    
	    setCompositionRoot(mainPanel);

	}
	
	public void addListener(){
		
		rejectionBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
                showRejectionLetter(reimbursementRejection);
			}
		});
	}
	
	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("600px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
	
	public void showRejectionLetter(ReimbursementRejection rejection){
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("75%");
		popup.setHeight("90%");
		if(rejection != null && rejection.getFileToken() != null){
			fileViewUI.setCurrentPage(getUI().getPage());
			fileViewUI.init(popup,"rejection.pdf", rejection.getFileToken());
		}
		popup.setContent(fileViewUI);
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
