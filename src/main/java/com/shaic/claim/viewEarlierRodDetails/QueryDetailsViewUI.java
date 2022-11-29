package com.shaic.claim.viewEarlierRodDetails;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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

public class QueryDetailsViewUI extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ViewQueryDTO bean;
	
	private BeanFieldGroup<ViewQueryDTO> binder;
	
	private TextField txtIntimationNo;
	
	private TextField txtClaimNo;
	
	private TextField txtPolicyNo;
	
	private TextField txtAcknowledNo;
	
	private TextField txtRodNumber;
	
	private TextField txtReceivedFrom;
	
	private TextField txtBillClassification;
	
	private TextField txtProductName;
	
	private TextField txtClaimType;
	
	private TextField txtInsuredPatient;
	
	private TextField txtHospitaName;
	
	private TextField txtHospitalCity;
	
	private TextField txtHospitalType;
	
	private TextField txtDateOfAdmission;
	
	private TextField txtDiagnosis;
	
	private TextField txtQueryRaisedRole;
	
	private TextField txtQueryRaisedByName;
	
	private TextField txtQueryRaisedDate;
	
	private TextArea txtQueryRemarks;
	
	private TextField txtDraftedDate;
	
	private TextField txtLetterRemarks;
	
	private TextField txtApprovedDate;
	
	private TextField txtRejectionRemarks;
	
	private TextField txtRedraftRemarks;
	
	private TextField txtQueryStatus;
	
	private Button viewQueryLetterBtn;
	
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private ClaimService claimService;
	
	////private static Window popup;

	
	public void init(ViewQueryDTO bean){
    	this.bean = bean;
    	
//    	this.bean.setDesignation(SHAConstants.QUERY_RAISED_ROLE);
//    	this.bean.setQueryRaised(SHAConstants.QUERY_RAISED_ROLE);
    	
    	this.binder = new BeanFieldGroup<ViewQueryDTO>(ViewQueryDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		viewQueryLetterBtn = new Button("View Query Letter");
		
		txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtClaimNo = (TextField) binder.buildAndBind("Claim Number","claimNo",TextField.class);
		txtPolicyNo = (TextField) binder.buildAndBind("Policy No","policyNo",TextField.class);
		txtAcknowledNo = (TextField) binder.buildAndBind("Acknowledgement No","acknowledgementNo",TextField.class);
		txtRodNumber = (TextField) binder.buildAndBind("ROD No","rodNumber",TextField.class);
		txtReceivedFrom = (TextField) binder.buildAndBind("Documents Recieved From","receivedFrom",TextField.class);
		txtBillClassification = (TextField) binder.buildAndBind("Bill Classification","billClassification",TextField.class);
		txtProductName = (TextField) binder.buildAndBind("Product Name","productName",TextField.class);
		txtClaimType = (TextField) binder.buildAndBind("Claim Type","claimType",TextField.class);
		txtInsuredPatient = (TextField) binder.buildAndBind("Insured Patient Name","insuredPatientName",TextField.class);
		txtHospitaName = (TextField) binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		txtHospitalCity = (TextField) binder.buildAndBind("Hospital City","hospitalCity",TextField.class);
		txtHospitalType = (TextField) binder.buildAndBind("Hospital Type","hospitalType",TextField.class);
		txtDateOfAdmission = (TextField) binder.buildAndBind("Date of Admission","admissionDate",TextField.class);
		txtDiagnosis = (TextField) binder.buildAndBind("Diagnosis","diagnosis",TextField.class);
		
		txtQueryRaisedRole = (TextField) binder.buildAndBind("Query Raised By Role","designation",TextField.class);
		txtQueryRaisedByName = (TextField) binder.buildAndBind("Query Raised By ID/Name","queryRaised",TextField.class);
		txtQueryRaisedDate = (TextField) binder.buildAndBind("Query Raised Date","queryRaisedDateStr",TextField.class);
		txtQueryRemarks = (TextArea) binder.buildAndBind("Query Remarks","queryRemarks",TextArea.class);
		txtDraftedDate = (TextField) binder.buildAndBind("Query Drafted Date","queryDraftedDate",TextField.class);
		txtDraftedDate.setNullRepresentation("-");
		txtLetterRemarks = (TextField) binder.buildAndBind("Query Letter Remarks","queryLetterRemarks",TextField.class);
		txtApprovedDate = (TextField) binder.buildAndBind("Query Approved/Rejected /Redraft date","approvedRejectedDate",TextField.class);
		txtRejectionRemarks = (TextField) binder.buildAndBind("Query Rejected Remarks","rejectedRemarks",TextField.class);
		txtRedraftRemarks = (TextField) binder.buildAndBind("Query Redraft Remarks","redraftRemarks",TextField.class);
		txtQueryStatus = (TextField) binder.buildAndBind("Query Status","queryStatus",TextField.class);
		
		Label lable = new Label("<H3>Query Details</H3>",ContentMode.HTML);
		
		Label lable1 = new Label("<H3>Claim Details</H3>",ContentMode.HTML);
		
		FormLayout firstForm = new FormLayout(lable1,txtIntimationNo,txtClaimNo,txtPolicyNo,txtAcknowledNo,txtRodNumber,txtReceivedFrom,
				txtBillClassification,txtProductName,txtClaimType,txtInsuredPatient,txtHospitaName,txtHospitalCity,txtHospitalType,txtDateOfAdmission,txtDiagnosis
				,lable,txtQueryRaisedRole,txtQueryRaisedByName,txtQueryRaisedDate,txtQueryRemarks,txtDraftedDate,txtLetterRemarks,
				txtApprovedDate,txtRejectionRemarks,txtRedraftRemarks,txtQueryStatus);
//		firstForm.setCaption("Claim Details");
		firstForm.addStyleName("layoutDesign");
		
//		FormLayout secondForm= new FormLayout(txtQueryRaisedRole,txtQueryRaisedByName,txtQueryRaisedDate,txtQueryRemarks,txtDraftedDate,txtLetterRemarks,
//				txtApprovedDate,txtRejectionRemarks,txtRedraftRemarks,txtQueryStatus);
//		secondForm.setCaption("Query Details");
//		secondForm.setStyleName("layoutDesign");
		FormLayout dummyLayout =  new FormLayout();
		dummyLayout.setWidth("250px");
//	    dummyLayout.setStyleName("layoutDesign");
		HorizontalLayout claimhor = new HorizontalLayout(firstForm);
		claimhor.setComponentAlignment(firstForm, Alignment.TOP_RIGHT);
		
		setReadOnly(firstForm,true);
//		setReadOnly(secondForm,true);
		
		FormLayout dummyQuery =  new FormLayout();
		dummyQuery.setWidth("250px");
//		dummyQuery.setStyleName("layoutDesign");
//		HorizontalLayout queryLayout = new HorizontalLayout(secondForm);
//		queryLayout.setComponentAlignment(secondForm, Alignment.TOP_RIGHT);
		
//		Panel queryPanel = new Panel(queryLayout);
//		queryPanel.setCaption("Query Details");
		
	    VerticalLayout verticalMain = new VerticalLayout(viewQueryLetterBtn,claimhor/*,queryLayout*/);
	    verticalMain.setComponentAlignment(viewQueryLetterBtn, Alignment.TOP_RIGHT);
//	    verticalMain.setStyleName("layoutDesign");
	    
	    FormLayout mainPanel = new FormLayout(verticalMain);
//	    mainPanel.addStyleName("layoutDesign");
	
	    mainPanel.setCaption("View Query Details");
	    
	    addListener();
	    
	    setCompositionRoot(mainPanel);



	}
	
	public void addListener(){
		
		viewQueryLetterBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;
			

			@Override
			public void buttonClick(ClickEvent event) {
				
				viewUploadedQueryDocumentDetails(bean.getIntimationNo(),SHAConstants.QUERY);
     
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
				// field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				// field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
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
