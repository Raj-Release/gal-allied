/**
 * 
 */
package com.shaic.claim.policy.search.ui.opsearch;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.claim.outpatient.processOPpages.OPSpecialityListenerTable;
import com.shaic.claim.outpatient.processOPpages.OPUploadedDocuments;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewSpecialityTable;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

/**
 * @author ntv.narasimhaj
 *
 */
public class OPViewClaimStatus extends ViewComponent {
	
	  private TextField insuredPatientName;
	  
	  private DateField opConsulationDate;
	  
	  private DateField documentReceivedDate;
	  
	  private TextField modeOfReceipt;
	  
	  private TextField contactNo;
	  
	  private TextField reasonForConsulation;
	  
	  private CheckBox accident;
	  
	  private CheckBox emergency;
	  
	  private TextArea remarksForEmerAccd;
	  
	  private TextField treatmentType;
	  
	  private TextField name;
	  
	  private TextField emailId;
	  
	  private TextField amountClaimed;
	  
	  private TextField coverSection;
	  
	  private OutPatientDTO bean;
	  
	  private BeanFieldGroup<OutPatientDTO> binder;
	  
	  private Button btnViewPolicyDetails;
	    
	  private Button btnViewDocument;
	    
	  private Button btnViewTrails;
	  
	  private Long rodKey;
	  
	  private DateField physicalDocumentReceivedDate;
	  
	  @Inject
	  private ViewClaimHistoryRequest viewClaimHistoryRequest;
	  
	  @Inject
	  private ViewPolicyDetails viewPolicyDetail;
	  
	  @Inject
	  private OPViewSpecialityTable specialityListener;
	  
	  /*@Inject
	  private Instance<OPViewUploadedDocuments> uploadedDocsTableInst;*/
	  
	  @Inject
	  private OPViewUploadedDocuments uploadedDocsTable;
	  
	  @Inject
	  private OPViewClaimPaymentTable paymentTable;
	  
	  @EJB
	  private IntimationService intimationService;
	  
	  @EJB
	  private PolicyService policyService;
	  
	  @EJB
	  private MasterService masterService;
	  
	  private VerticalLayout mainVerticalLayout;
	  
	  
	  public void init(OutPatientDTO bean,Long rodKey){
		  
		  this.bean = bean;
		  this.rodKey = rodKey;
	    	/*
	    	if(bean!=null && bean.getDateOfIntimation()!=null){
				Date tempDate = SHAUtils.formatTimestamp(bean.getDateOfIntimation());
				bean.setDateOfIntimation(SHAUtils.formatDate(tempDate));
			}
			
			if(bean!=null && bean.getAdmissionDate()!=null){
				bean.setAdmissionDate(bean.getAdmissionDate());
			}*/
	 
	    	this.binder = new BeanFieldGroup<OutPatientDTO>(OutPatientDTO.class);
			this.binder.setItemDataSource(this.bean);
			binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
			
			btnViewPolicyDetails = new Button("View Policy");
			btnViewPolicyDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			btnViewPolicyDetails.addStyleName(ValoTheme.BUTTON_LINK);
			
			btnViewDocument = new Button("View Document");
			btnViewDocument.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			btnViewDocument.addStyleName(ValoTheme.BUTTON_LINK);
			
			btnViewTrails = new Button("View Trails");
			btnViewTrails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			btnViewTrails.addStyleName(ValoTheme.BUTTON_LINK);
	    	
			HorizontalLayout mainHor = new HorizontalLayout(btnViewPolicyDetails,btnViewDocument,btnViewTrails);
			
			insuredPatientName = new TextField("Insured Patient Name");
			insuredPatientName.setNullRepresentation("");
			insuredPatientName.setValue(bean.getNewIntimationDTO().getInsuredPatientName() != null ?
					bean.getNewIntimationDTO().getInsuredPatientName() : "-");
			
			opConsulationDate = new DateField("OP Consultation Date");
			opConsulationDate.setDateFormat(("dd/MM/yyyy"));
			opConsulationDate.setRangeEnd(new Date());
			opConsulationDate.setValue(bean.getDocumentDetails().getOPCheckupDate());
			opConsulationDate.setEnabled(false);
			
			documentReceivedDate = new DateField("Documents Recieved Date");
			documentReceivedDate.setDateFormat(("dd/MM/yyyy"));
			documentReceivedDate.setRangeEnd(new Date());
			documentReceivedDate.setValue(bean.getDocumentDetails().getBillReceivedDate());
			documentReceivedDate.setEnabled(false);
			
			modeOfReceipt = new TextField("Mode of Receipt");
			modeOfReceipt.setNullRepresentation("");
			modeOfReceipt.setValue(bean.getDocumentDetails().getModeOfReceipt() != null ? bean.getDocumentDetails().getModeOfReceipt().getValue() :"");
			
			contactNo = new TextField("Contact No (Doc.Submitted Name)");
			contactNo.setNullRepresentation("");
			contactNo.setValue(String.valueOf(bean.getDocumentDetails().getDocSubmittedContactNo()));
			
			reasonForConsulation = new TextField("Reason for Consultation");
			reasonForConsulation.setNullRepresentation("");
			reasonForConsulation.setValue(bean.getDocumentDetails().getReasonforConsultation());
			
			accident = new CheckBox("Accident");
			
			accident.setValue(bean.getDocumentDetails().getAccidentFlag() != null && 
					bean.getDocumentDetails().getAccidentFlag().equalsIgnoreCase("Y") ? true : false);
			accident.setReadOnly(true);
			
			emergency = new CheckBox("Emergency");
			
			emergency.setValue(bean.getDocumentDetails().getAccidentFlag() != null && 
					bean.getDocumentDetails().getEmergencyFlag().equalsIgnoreCase("Y") ? true : false);
			emergency.setReadOnly(true);

			remarksForEmerAccd = new TextArea("Remarks of Emergency / Accident");
			remarksForEmerAccd.setNullRepresentation("");
			remarksForEmerAccd.setValue(bean.getDocumentDetails().getRemarksForEmergencyAccident());
			
			treatmentType = new TextField("Treatment Type");
			treatmentType.setNullRepresentation("");
			treatmentType.setValue(bean.getDocumentDetails().getTreatmentType().getValue());
			
			name = new TextField("Name (Doc.Submitted Name)");
			name.setNullRepresentation("");
			name.setValue(bean.getNewIntimationDTO().getDoctorName());
			
			emailId = new TextField("Email ID");
			emailId.setNullRepresentation("");
			emailId.setValue(bean.getDocumentDetails().getDocEmailId());
			
			amountClaimed  = new TextField("Amount Claimed");
			amountClaimed.setNullRepresentation("");
			amountClaimed.setValue(String.valueOf(bean.getDocumentDetails().getAmountClaimed().longValue()));
			
			coverSection = new TextField("Cover Section");
			coverSection.setNullRepresentation("");
			if(bean.getDocumentDetails().getConsultationType() != null &&  bean.getDocumentDetails().getConsultationType().getValue() != null){
				coverSection.setValue(bean.getDocumentDetails().getConsultationType().getValue());
			}
			
			FormLayout chxEmergencylayout = new FormLayout(emergency);
			chxEmergencylayout.setSpacing(false);;
			FormLayout chxAccidentlayout = new FormLayout(accident);
			chxAccidentlayout.setSpacing(false);;
			HorizontalLayout chxLayout = new HorizontalLayout(emergency,accident);
			chxLayout.setSpacing(true);
			
			OptionGroup physicalDocsReceived = new OptionGroup();
			physicalDocsReceived.addItems(getReadioButtonOptions());
			physicalDocsReceived.setItemCaption(true, "Yes");
			physicalDocsReceived.setItemCaption(false, "No");
			physicalDocsReceived.setStyleName("horizontal");
			physicalDocsReceived.setCaption("Physical Documents Received"); 
			physicalDocsReceived.setCaptionAsHtml(true);
			physicalDocsReceived.setValue(bean.getDocumentDetails().getPhysicalDocsReceivedFlag() != null && bean.getDocumentDetails().getPhysicalDocsReceivedFlag().equalsIgnoreCase("Y") ? true : false);
			physicalDocsReceived.setReadOnly(true);
			
			physicalDocumentReceivedDate = new DateField("Physical Documents<br>Recieved Date");
			physicalDocumentReceivedDate.setCaptionAsHtml(true);
			physicalDocumentReceivedDate.setDateFormat(("dd/MM/yyyy"));
			physicalDocumentReceivedDate.setRangeEnd(new Date());
			physicalDocumentReceivedDate.setValue(bean.getDocumentDetails().getPhysicalDocsReceivedDate());
			physicalDocumentReceivedDate.setEnabled(false);
			
			FormLayout firstForm = new FormLayout(insuredPatientName,opConsulationDate,documentReceivedDate,modeOfReceipt,contactNo,reasonForConsulation,physicalDocsReceived);
			FormLayout secondForm = new FormLayout(remarksForEmerAccd,treatmentType,coverSection,name,emailId,amountClaimed,physicalDocumentReceivedDate);
			setReadOnly(firstForm,true);
			setReadOnly(secondForm,true);
			
			TextField dump = new TextField();
			dump.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			VerticalLayout vChkLayout = new VerticalLayout(dump,chxLayout,secondForm);
			
			VerticalLayout vLayout = new VerticalLayout();
			
			HorizontalLayout intimationHor = new HorizontalLayout(firstForm,vChkLayout);
			intimationHor.setComponentAlignment(firstForm, Alignment.TOP_CENTER);
			intimationHor.setComponentAlignment(vChkLayout, Alignment.MIDDLE_RIGHT);
			intimationHor.setWidth("110%");
			
			vLayout.addComponents(intimationHor);
			vLayout.setSpacing(true);
			
			Panel mainPanel = new Panel(vLayout);
		    mainPanel.addStyleName("girdBorder");
		    mainPanel.setCaption("Intimation Details");
		    
		    Panel consulationDtls = consulationDetails();
		    consulationDtls.addStyleName("girdBorder");
		    
		    Panel billingDtls = billingDetails();
		    billingDtls.addStyleName("girdBorder");
		    billingDtls.setCaption("Billing Details");
		    
		    Panel settlementDtls = settlementDtls();
		    settlementDtls.addStyleName("girdBorder");
		    settlementDtls.setCaption("Settlement Details");
		    
		    mainVerticalLayout = new VerticalLayout(mainHor,mainPanel,consulationDtls,billingDtls,settlementDtls);
		    mainVerticalLayout.setSpacing(true);
		    mainVerticalLayout.setComponentAlignment(mainHor, Alignment.TOP_RIGHT);
		    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);

		    mainVerticalLayout.setMargin(true);
		    
		    if(bean.getDocumentDetails().getModeOfReceipt().getValue() != null && bean.getDocumentDetails().getModeOfReceipt().getValue().equalsIgnoreCase("Online")){
				physicalDocsReceived.setVisible(true);
				physicalDocumentReceivedDate.setVisible(true);
			}else {
				physicalDocsReceived.setVisible(false);
				physicalDocumentReceivedDate.setVisible(false);
			}
		    
		    addListener();
		    
		    setCompositionRoot(mainVerticalLayout);

	  }
	  
	  private Panel consulationDetails(){
		  
		  	TextField chkBoxLayout = new TextField("Benefits Availed");
			chkBoxLayout.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			chkBoxLayout.setEnabled(false);
			CheckBox chkConsultation = new CheckBox("Consultation");

			CheckBox chkDiagnositics = new CheckBox("Diagnostics");

			CheckBox chkPhysiotherapy = new CheckBox("Physiotherapy");

			CheckBox chkMedicine = new CheckBox("Medicine");

			
			
			
			if(!bean.getConsulation()){
				chkConsultation.setValue(true);
			}
			if(bean.getDiagnosis()){
				chkDiagnositics.setValue(true);
			}
			if(bean.getPhysiotherapy()){
				chkPhysiotherapy.setValue(true);
			}
			if(bean.getMedicine()){
				chkMedicine.setValue(true);
			}
			chkConsultation.setReadOnly(true);
			chkDiagnositics.setReadOnly(true);
			chkPhysiotherapy.setReadOnly(true);
			chkMedicine.setReadOnly(true);
			
			HorizontalLayout chkBoxes = new HorizontalLayout(chkConsultation,chkDiagnositics,chkPhysiotherapy,chkMedicine);
			chkBoxes.setSpacing(true);
			HorizontalLayout benefitsAvailed = new HorizontalLayout(chkBoxLayout,chkBoxes);
			
			OptionGroup providerType = new OptionGroup();
			providerType.addItems(getReadioButtonOptions());
			providerType.setItemCaption(true, "Network");
			providerType.setItemCaption(false, "Other than Network");
			providerType.setStyleName("horizontal");
			providerType.setCaption("Provider Type"); 
			providerType.setCaptionAsHtml(true);
			providerType.setValue(true);
			
			if(bean.getBenefitsAvailedDto().getConsulationProvider() != null
					&& bean.getBenefitsAvailedDto().getConsulationProvider().equals(Boolean.TRUE)){
				providerType.setValue(true);
			} else 	if(bean.getBenefitsAvailedDto().getConsulationProvider() != null
					&& bean.getBenefitsAvailedDto().getConsulationProvider().equals(Boolean.FALSE)){
				providerType.setValue(false);
			}
			providerType.setReadOnly(true);
//			providerType.setEnabled(false);
			
			TextField txtState = new TextField("State");	
			txtState.setNullRepresentation("");
			txtState.setValue(bean.getBenefitsAvailedDto().getState());
			
			TextField txtCity = new TextField("City");	
			txtCity.setNullRepresentation("");
			txtCity.setValue(bean.getBenefitsAvailedDto().getCity());
			
			TextField txtHospitalName = new TextField("Hospital Name");	
			txtHospitalName.setNullRepresentation("");
			txtHospitalName.setValue(bean.getBenefitsAvailedDto().getHospitalName());
			
			TextField txtHospitalAddress = new TextField("Hospital Address");
			txtHospitalAddress.setNullRepresentation("");
			txtHospitalAddress.setValue(bean.getBenefitsAvailedDto().getHospitalAddress());

			TextField hospitalContactNumber = new TextField("Hospital Contact Number");
			hospitalContactNumber.setNullRepresentation("");
			hospitalContactNumber.setValue(bean.getBenefitsAvailedDto().getHospitalContactNumber());

			TextField hospitalFaxNo = new TextField("Hospital Fax No");
			hospitalFaxNo.setNullRepresentation("");
			hospitalFaxNo.setValue(bean.getBenefitsAvailedDto().getHospitalFaxNo());
			
			TextField hospitalDoctorName =new TextField("Hospital Doctor Name");
			hospitalDoctorName.setNullRepresentation("");
			hospitalDoctorName.setValue(bean.getBenefitsAvailedDto().getHospitalDoctorName());
			
			CheckBox chkClinic = new CheckBox("Clinic");
			chkClinic.setValue(false);
			chkClinic.setVisible(false);
			
			
			TextField dummy1 = new TextField();
			dummy1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			FormLayout leftFormLayout  = new FormLayout(providerType,txtState,txtCity,txtHospitalName,txtHospitalAddress);
			FormLayout rightFormLayout = new FormLayout(dummy1,chkClinic,hospitalContactNumber,hospitalFaxNo,hospitalDoctorName);
			setReadOnly(leftFormLayout,true);
			setReadOnly(rightFormLayout,true);
			
			if(bean.getBenefitsAvailedDto().getConsulationProvider() != null
					&& bean.getBenefitsAvailedDto().getConsulationProvider().equals(Boolean.FALSE)){
				chkClinic.setVisible(true);
				if(bean.getBenefitsAvailedDto().getClinic() != null && bean.getBenefitsAvailedDto().getClinic()){
					chkClinic.setValue(true);
				}
				rightFormLayout.addComponentAsFirst(chkClinic);
				rightFormLayout.removeComponent(hospitalFaxNo);
			}
		
			
			HorizontalLayout hLayout = new HorizontalLayout(leftFormLayout,rightFormLayout);
			hLayout.setSpacing(true);
			hLayout.setMargin(true);
			
			specialityListener.init("", false, false);
			if(bean.getSpecialityDTOList() != null && !bean.getSpecialityDTOList().isEmpty()){
				specialityListener.setTableList(bean.getSpecialityDTOList());
			}
				
			
			VerticalLayout vlayout = new VerticalLayout(hLayout,specialityListener);
			vlayout.setSpacing(false);
			vlayout.setCaption("Consultation Details");
			
			FormLayout diag = new FormLayout();
			OptionGroup diagnosis = new OptionGroup();
			diagnosis.addItems(getReadioButtonOptions());
			diagnosis.setItemCaption(true, "Yes");
			diagnosis.setItemCaption(false, "No");
			diagnosis.setStyleName("horizontal");
			diagnosis.setCaption("Same as Consulation"); 
			if(bean.getDiagnosis()){
				if(bean.getBenefitsAvailedDto().getHospitalName() != null && bean.getBenefitsAvailedDto().getProviderName() != null
					&& bean.getBenefitsAvailedDto().getHospitalName().equalsIgnoreCase(bean.getBenefitsAvailedDto().getProviderName())){
					diagnosis.setValue(true);
				} else {
					diagnosis.setValue(false);
				}
			}
			diagnosis.setReadOnly(true);
			diag.addComponent(diagnosis);
			diag.setCaption("Diagnostics Details");
			
			OptionGroup diagproviderType = new OptionGroup();
			diagproviderType.addItems(getReadioButtonOptions());
			diagproviderType.setItemCaption(true, "Network");
			diagproviderType.setItemCaption(false, "Other than Network");
			diagproviderType.setStyleName("horizontal");
			diagproviderType.setCaption("Provider Type"); 
			diagproviderType.setCaptionAsHtml(true);
			diagproviderType.setValue(true);
		
			if(bean.getBenefitsAvailedDto().getDiagnosisProvider() != null
					&& bean.getBenefitsAvailedDto().getDiagnosisProvider().equals(Boolean.TRUE)){
				diagproviderType.setValue(true);
			} else 	if(bean.getBenefitsAvailedDto().getDiagnosisProvider() != null
					&& bean.getBenefitsAvailedDto().getDiagnosisProvider().equals(Boolean.FALSE)){
				diagproviderType.setValue(false);
			}
			diagproviderType.setReadOnly(true);
//			providerType.setEnabled(false);
			
			TextField txtDigState = new TextField("State");	
			txtDigState.setNullRepresentation("");
			txtDigState.setValue(bean.getBenefitsAvailedDto().getProviderState());
			
			TextField txtDiagCity = new TextField("City");	
			txtDiagCity.setNullRepresentation("");
			txtDiagCity.setValue(bean.getBenefitsAvailedDto().getProviderCity());
			
			TextField txtDiagProName = new TextField("Provider Name");	
			txtDiagProName.setNullRepresentation("");
			txtDiagProName.setValue(bean.getBenefitsAvailedDto().getProviderName());
			
			TextField txtProvAddress = new TextField("Provider Address");
			txtProvAddress.setNullRepresentation("");
			txtProvAddress.setValue(bean.getBenefitsAvailedDto().getProviderAddress());

			TextField provContactNumber = new TextField("Provider Contact Number");
			provContactNumber.setNullRepresentation("");
			provContactNumber.setValue(bean.getBenefitsAvailedDto().getProviderContactNo());

			
			TextField dummyD1 = new TextField();
			dummy1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			FormLayout leftDiagFormLayout  = new FormLayout(diagproviderType,txtDigState,txtDiagCity,txtDiagProName);
			FormLayout rightDiagFormLayout = new FormLayout(dummyD1,txtProvAddress,provContactNumber);
			setReadOnly(leftDiagFormLayout,true);
			setReadOnly(rightDiagFormLayout,true);
			
			HorizontalLayout hDiaLayout = new HorizontalLayout(leftDiagFormLayout,rightDiagFormLayout);
			hDiaLayout.setSpacing(true);
			hDiaLayout.setMargin(true);
			
			FormLayout pysio = new FormLayout();
			OptionGroup phsysio = new OptionGroup();
			phsysio.addItems(getReadioButtonOptions());
			phsysio.setItemCaption(true, "Yes");
			phsysio.setItemCaption(false, "No");
			phsysio.setStyleName("horizontal");
			phsysio.setCaption("Same as Consulation"); 
			if(bean.getPhysiotherapy()){
				if(bean.getBenefitsAvailedDto().getHospitalName() != null && bean.getBenefitsAvailedDto().getPhysiotherapistName() != null
					&& bean.getBenefitsAvailedDto().getHospitalName().equalsIgnoreCase(bean.getBenefitsAvailedDto().getPhysiotherapistName())){
				phsysio.setValue(true);
				} else {
					phsysio.setValue(false);
				}
			}
			phsysio.setReadOnly(true);
			pysio.addComponent(phsysio);
			pysio.setCaption("Physiotherapy Details");
			
			
			TextField txtPhyProName = new TextField("Physiotherapist Name");	
			txtPhyProName.setNullRepresentation("");
			txtPhyProName.setValue(bean.getBenefitsAvailedDto().getPhysiotherapistName());
			
			TextField txtPhyAddress = new TextField("Clinic Address");
			txtPhyAddress.setNullRepresentation("");
			txtPhyAddress.setValue(bean.getBenefitsAvailedDto().getClinicAddress());

			TextField phyContactNumber = new TextField("Physiotherapist Contact Number");
			phyContactNumber.setNullRepresentation("");
			phyContactNumber.setValue(bean.getBenefitsAvailedDto().getPhysiotherapistContactNo());
			
			TextField phyEmail = new TextField("Email Id");
			phyEmail.setNullRepresentation("");
			phyEmail.setValue(bean.getBenefitsAvailedDto().getEmailId());

			
			TextField dummyP1 = new TextField();
			dummyP1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			FormLayout leftPhyFormLayout  = new FormLayout(txtPhyProName,txtPhyAddress);
			FormLayout rightPhyFormLayout = new FormLayout(phyContactNumber,phyEmail);
			setReadOnly(leftPhyFormLayout,true);
			setReadOnly(rightPhyFormLayout,true);
			
			HorizontalLayout hPhyLayout = new HorizontalLayout(leftPhyFormLayout,rightPhyFormLayout);
			hPhyLayout.setSpacing(true);
			hPhyLayout.setMargin(true);
			
			FormLayout phar = new FormLayout();
			OptionGroup pharmacy = new OptionGroup();
			pharmacy.addItems(getReadioButtonOptions());
			pharmacy.setItemCaption(true, "Yes");
			pharmacy.setItemCaption(false, "No");
			pharmacy.setStyleName("horizontal");
			pharmacy.setCaption("Same as Consulation"); 
			if(bean.getMedicine()){
				if(bean.getBenefitsAvailedDto().getHospitalName() != null && bean.getBenefitsAvailedDto().getPharmacytName() != null
					&& bean.getBenefitsAvailedDto().getHospitalName().equalsIgnoreCase(bean.getBenefitsAvailedDto().getPharmacytName())){
					pharmacy.setValue(true);
				} else {
					pharmacy.setValue(false);
				}
			}
			pharmacy.setReadOnly(true);
			phar.addComponent(pharmacy);
			phar.setCaption("Pharmacy Details");
			
			TextField txtMedProName = new TextField("Pharmacy Name");	
			txtMedProName.setNullRepresentation("");
			txtMedProName.setValue(bean.getBenefitsAvailedDto().getPharmacytName());
			
			TextField txtMedAddress = new TextField("Pharmacy Address");
			txtMedAddress.setNullRepresentation("");
			txtMedAddress.setValue(bean.getBenefitsAvailedDto().getPharmacyAddress());

			TextField medContactNumber = new TextField("Pharmacy Contact Number");
			medContactNumber.setNullRepresentation("");
			medContactNumber.setValue(bean.getBenefitsAvailedDto().getPharmacyContactNo());
			
			TextField medEmail = new TextField("Email Id");
			medEmail.setNullRepresentation("");
			medEmail.setValue(bean.getBenefitsAvailedDto().getPharmacyemailId());

			
			TextField dummym1 = new TextField();
			dummym1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			
			FormLayout leftMedFormLayout  = new FormLayout(txtMedProName,txtMedAddress);
			FormLayout rightMedFormLayout = new FormLayout(medContactNumber,medEmail);
			setReadOnly(leftMedFormLayout,true);
			setReadOnly(rightMedFormLayout,true);
			
			HorizontalLayout hMedLayout = new HorizontalLayout(leftMedFormLayout,rightMedFormLayout);
			hMedLayout.setSpacing(true);
			hMedLayout.setMargin(true);
			
			FormLayout formLayout = new FormLayout(benefitsAvailed);
			
			VerticalLayout mainlayout = new VerticalLayout(benefitsAvailed,vlayout,diag,hDiaLayout,pysio,hPhyLayout,phar,hMedLayout);
			mainlayout.setComponentAlignment(diag, Alignment.MIDDLE_RIGHT);
			mainlayout.setComponentAlignment(pysio, Alignment.MIDDLE_RIGHT);
			mainlayout.setComponentAlignment(phar, Alignment.MIDDLE_RIGHT);
			mainlayout.setSpacing(true);
			
			hDiaLayout.setVisible(false);
			hPhyLayout.setVisible(false);
			hMedLayout.setVisible(false);
			
			if(bean.getDiagnosis() && bean.getBenefitsAvailedDto().getHospitalName() != null && bean.getBenefitsAvailedDto().getProviderName() != null
					&& !bean.getBenefitsAvailedDto().getHospitalName().equalsIgnoreCase(bean.getBenefitsAvailedDto().getProviderName())){
				hDiaLayout.setVisible(true);
			}
			
			if(bean.getPhysiotherapy() && bean.getBenefitsAvailedDto().getHospitalName() != null && bean.getBenefitsAvailedDto().getPhysiotherapistName() != null
					&& !bean.getBenefitsAvailedDto().getHospitalName().equalsIgnoreCase(bean.getBenefitsAvailedDto().getPhysiotherapistName())){
				hPhyLayout.setVisible(true);
			}
			
			if(bean.getMedicine() && bean.getBenefitsAvailedDto().getHospitalName() != null && bean.getBenefitsAvailedDto().getPharmacytName() != null
					&& !bean.getBenefitsAvailedDto().getHospitalName().equalsIgnoreCase(bean.getBenefitsAvailedDto().getPharmacytName())){
				hMedLayout.setVisible(true);
			}
			
			Panel panel = new Panel(mainlayout);
			return panel;
	  }
	  
	  private Panel billingDetails(){
//		  uploadedDocsTable = uploadedDocsTableInst.get();
		  uploadedDocsTable.init("", false, false);
		  if(bean.getUploadedDocsTableList() != null && !bean.getUploadedDocsTableList().isEmpty()){
			  for (UploadDocumentDTO doc : bean.getUploadedDocsTableList()) {
				  uploadedDocsTable.addBeanToList(doc);
			}
		  }
//		  uploadedDocsTable.setTableList(bean.getUploadedDocsTableList());
		  Panel billingPanel = new Panel(uploadedDocsTable);
		  billingPanel.addStyleName("girdBorder");
		  billingPanel.setCaption("Billing Details");
		  
		  return billingPanel;
	  }
	  
	  private Panel settlementDtls(){
		  
		  TextField approvedAmt = new TextField("Approved Amount");
		  approvedAmt.setNullRepresentation("");
		  approvedAmt.setValue(bean.getApprovedAmt() != null ? bean.getApprovedAmt().toString() : "");
		  
		  TextField approvedRemarks = new TextField("Approval Remarks");
		  approvedRemarks.setNullRepresentation("");
		  approvedRemarks.setValue(bean.getApprovalRemarks() != null ? bean.getApprovalRemarks() : "");
		  
		  FormLayout amtLayout = new FormLayout(approvedAmt);
		  FormLayout remarksLayout = new FormLayout(approvedRemarks);
		  setReadOnly(amtLayout,true);
		  setReadOnly(remarksLayout,true);
		  HorizontalLayout txtLayout = new HorizontalLayout(amtLayout,remarksLayout);
		  txtLayout.setSpacing(true);
		  
		  paymentTable.init("", false, false);
		  if(bean.getViewPaymentDto() != null){
			  paymentTable.addBeanTolist(bean.getViewPaymentDto());
		  }
		  
		  VerticalLayout paymLayout = new VerticalLayout(txtLayout,paymentTable);
		  
		  Panel panel = new Panel(paymLayout);
		  panel.addStyleName("girdBorder");
		  panel.setCaption("Settlement Details");
		  
		  return panel;
	  }
	  
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
			
		return coordinatorValues;
	}
	  
	  public void addListener(){
	    	
	    	btnViewDocument.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					
					if(bean.getIntimationId() != null){
						viewUploadedDocumentDetails(bean.getIntimationId());
					}
					
				}
			});
	    	
	    	btnViewPolicyDetails.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					
					if(bean.getIntimationId() != null){
						getViewPolicy(bean.getIntimationId());
					}
					
				}
			});
	    	
	    	btnViewTrails.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					
					if(bean.getIntimationId() != null){
						getViewClaimHistory(bean.getIntimationId());
					}
					
				}
			});
	    	
	    }
	  
		public void getViewClaimHistory(String intimationNo) {

			if(intimationNo != null){
				OPIntimation intimation = intimationService.getOPIntimationByNo(intimationNo);
				Boolean result = true;		
				if (intimation != null) {
					result = viewClaimHistoryRequest.showOPCashlessAndReimbursementHistory(intimation);
					if(result){
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("View History");
						popup.setWidth("75%");
						popup.setHeight("75%");
						popup.setContent(viewClaimHistoryRequest);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							private static final long serialVersionUID = 1L;		
							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
					}else{
						getErrorMessage("History is not available");
					}
				}

			}else{
				getErrorMessage("History is not available");
			}
		}
	  
		public void viewUploadedDocumentDetails(String intimationNo) {
			BPMClientContext bpmClientContext = new BPMClientContext();
			Map<String,String> tokenInputs = new HashMap<String, String>();
			 tokenInputs.put("intimationNo", intimationNo);
			 String intimationNoToken = null;
			  try {
				  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
			  } catch (NoSuchAlgorithmException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  } catch (ParseException e) {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }
			  tokenInputs = null;
			String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
			/*Below code commented due to security reason
			String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
			getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
		}
		
		public void getViewPolicy(String intimationNo) {
			final Intimation intimation = intimationService.searchbyIntimationNo(intimationNo);
			Policy policy = null;
			OPIntimation opintimation = null;
			if(intimation != null){
				policy = policyService.getPolicy(intimation.getPolicy().getPolicyNumber());
			}else if(intimation == null){
				opintimation = intimationService.searchbyOPIntimationNo(intimationNo);
				policy = policyService.getPolicy(opintimation.getPolicy().getPolicyNumber());
			}
			
			
			if(policy.getProduct() != null && (ReferenceTable.getGMCProductCodeList().containsKey(policy.getProduct().getCode()) || policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.JET_PRIVILEGE_GOLD_PRODUCT)
					 || policy.getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_AROGYA_SANJEEVANI_PROD_CODE))){
				if(intimation != null){
					viewPolicyDetail.setPolicyServiceAndPolicyForGMC(policyService, policy,intimation.getInsured(), masterService, intimationService);
				}else if(intimation == null){
					viewPolicyDetail.setPolicyServiceAndPolicyForGMC(policyService, policy,opintimation.getInsured(), masterService, intimationService);
				}
			}else{		
				viewPolicyDetail.setPolicyServiceAndPolicy(policyService, policy, masterService, intimationService);
			}
			viewPolicyDetail.initView();
			UI.getCurrent().addWindow(viewPolicyDetail);
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
				} else if (c instanceof TextArea) {
					TextArea field = (TextArea) c;
					field.setWidth("350px");
			        field.setNullRepresentation("-");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
			}
		}
	   
		public void getErrorMessage(String eMsg) {
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
		}

}
