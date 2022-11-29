package com.shaic.claim.intimatino.view;

import java.util.Date;
import java.util.Iterator;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GTextField;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.ClaimService;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersEvents;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.ViewTmpIntimation;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
//import com.shaic.claim.viewEarlierRodDetails.Table.ViewDiagnosisTable;

public class ViewGalaxyIntimation extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private TextField txtIntimationNo;
	    
	    private TextField txtIntimationTime;
	    
	    private GTextField txtCpuCode;
	    
	    private TextField cmbModeOfIntimation;
	    
	    private TextField txtIntimatedBy;
	    
	    private TextField cmbInsuredPatientName;
	    
	    private TextField txtPolicyNo;
	    
	    private TextField txtIssuingOffice;
	    
	    private TextField txtProductName;
	    
	    private TextField txtInsuredName;
	    
	    private TextField txtInpatientNumber;
	    
	    private TextField txtLateIntimation;
	    
	    private TextField admissionDate;
	    
	    private TextField txtAdmissionType;
	    
	    private TextArea txtReasonForAdmission;
	   
	    private TextField txtComments;
	    
	    private TextField txtState;
	    
	    private TextField txtCity;
	    
	    private TextField txtArea;
	    
	    private TextField txtSmCode;
	    
	    private TextField txtSmName;
	    
	    private TextField txtBrokerCode;
	    
	    private TextField txtBrokerName;
	    
	    private TextField txtHospitalName;
	    
	    private TextField txtName;
	    
	    private TextField txtRelationship;
	    
	    private TextArea txtAddress;
	    
	    private TextField txtHealthCardNo;
	    
	    private TextField txtEmpId;
	    
	    private TextField txtHospitalType;
	    
	    private TextField txtHospitalCodeInternal;
	    
	    private TextField txtHospitalCodeIrda;
	    
	    private CheckBox chkPatientNotCovered;
	    
	    private VerticalLayout mainVerticalLayout;
	     
	    private BeanFieldGroup<ViewClaimStatusDTO> binder;
	    
	    private OptionGroup acctorDeath;
	    
	    private final String acct="Accident";
		
		private final String death="Death";
	    
		private TextField categoryType;
		
		private TextField parentName;
		
		private TextField age;
		
		private TextField patientName;
		
		private TextField dob;
		
		private TextField txtHospitalPhoneNo;
		
		private TextField txtHospitalFaxNo;
		
		private TextField txtHospitalDoctorName;
		
		private TextField txtRoomCategory;
		
		private TextField txtIntimatorName;
		
		private TextField txtCallerContactNo;
		
		private TextField txtCallerAddress;
		
		private TextField txtCallerEmail;
		
		private TextField txtHospitalflag;
		
        @EJB
        private IntimationService intimationService;
        
        @EJB
        private ClaimService claimService;
        
        @EJB
        private HospitalService hospitalService;
        
        @EJB
        private MasterService masterService;
	    
	    private ViewClaimStatusDTO bean;
	    
	    public void init(GalaxyIntimation intimation){
	    	GalaxyIntimation intimationByKey = intimationService.getGalaxyIntimationByKey(intimation.getKey());
	    	EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ViewClaimStatusDTO intimationDetails = instance.getViewGalaxyIntimationStatusDto(intimationByKey);
			if(intimationByKey.getStatus() != null && intimationByKey.getStatus().getKey().equals(ReferenceTable.INTIMATION_PENDING_STATUS_KEY)){
				TmpHospital tmpHospitalById = hospitalService.getTmpHospitalById(intimation.getHospital());
				getTmpHospitalDetails(intimationDetails, tmpHospitalById);
			}else{
				Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
				getHospitalDetails(intimationDetails, hospitals);
			}
			
			GalaxyIntimation intimationObj = intimationByKey;
			
			 if(intimationObj.getPolicy().getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)){
				 boolean isjiopolicy = false;	
					isjiopolicy = intimationService.getJioPolicyDetails(intimationObj.getPolicy().getPolicyNumber());
					  
					intimationDetails.setJioPolicy(isjiopolicy);
				      Insured insuredByKey = intimationService.getInsuredByKey(intimationObj.getInsured().getKey());
				      Insured MainMemberInsured = null;
				      
				      if(insuredByKey.getDependentRiskId() == null){
				    	  MainMemberInsured = insuredByKey;
				      }else{
				    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
				    	  MainMemberInsured = insuredByPolicyAndInsuredId;
				      }
				      
				      if(MainMemberInsured != null){
				    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
				      }		
				}
			
			this.bean = intimationDetails;
			EarlierRodMapper.invalidate(instance);
	    	if(bean!=null && bean.getDateOfIntimation()!=null){
				Date tempDate = SHAUtils.formatTimestamp(bean.getDateOfIntimation());
				bean.setDateOfIntimation(SHAUtils.formatDate(tempDate));
			}
			
			if(bean!=null && bean.getAdmissionDate()!=null){
				bean.setAdmissionDateStr(SHAUtils.formatDate(bean.getAdmissionDate()));
			}
	    	
	    	this.binder = new BeanFieldGroup<ViewClaimStatusDTO>(ViewClaimStatusDTO.class);
			this.binder.setItemDataSource(this.bean);
			binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
			
			txtIntimationNo = (TextField) binder.buildAndBind("Intimation No","intimationId",TextField.class);
			txtIntimationTime = (TextField) binder.buildAndBind("Date & Time of Intimation","dateOfIntimation",TextField.class);
			txtCpuCode = (GTextField) binder.buildAndBind("CPU Code", "cpuId", GTextField.class);
//			txtCpuCode = new GTextField();
//			txtCpuCode.setValue(this.bean.getCpuId().toString());
			txtCpuCode.setNullRepresentation("");
			cmbModeOfIntimation = (TextField) binder.buildAndBind("Mode of Intimation", "intimationMode", TextField.class);
			txtIntimatedBy = (TextField) binder.buildAndBind("Intimated By", "intimatedBy", TextField.class);
			txtIntimatedBy.setNullRepresentation("");
			txtIntimatedBy.setRequired(false);
			txtIntimatedBy.setValidationVisible(false);
			cmbInsuredPatientName = (TextField) binder.buildAndBind("Insured Patient Name", "insuredPatientName", TextField.class);
			txtHealthCardNo = (TextField) binder.buildAndBind("Health Card No", "healthCardNo", TextField.class);
			
			if(this.bean.isJioPolicy()){
				txtEmpId = (TextField) binder.buildAndBind("JACID", "employeeCode", TextField.class);
			}
			
			txtName = (TextField) binder.buildAndBind("Name ", "patientNotCoveredName", TextField.class);
			txtRelationship = (TextField) binder.buildAndBind("Relationship ", "relationshipWithInsuredId", TextField.class);
			admissionDate =(TextField) binder.buildAndBind("Admission Date","admissionDateStr",TextField.class);
			admissionDate.setRequired(false);
			admissionDate.setValidationVisible(false);
			txtAdmissionType = (TextField) binder.buildAndBind("Admission Type", "admissionType", TextField.class);
			txtAdmissionType.setRequired(false);
			txtAdmissionType.setValidationVisible(false);
			txtAdmissionType.setNullRepresentation("");
			txtReasonForAdmission = (TextArea) binder.buildAndBind("Reason for Admission","reasonForAdmission",TextArea.class);
			txtReasonForAdmission.setRequired(false);
			txtReasonForAdmission.setValidationVisible(false);
			
			txtIntimatorName = (TextField) binder.buildAndBind("Intimator Name", "intimatorName", TextField.class);
			txtCallerContactNo = (TextField) binder.buildAndBind("Caller Contact No", "callerMobileNumber", TextField.class);
			txtCallerAddress = (TextField) binder.buildAndBind("Caller Address", "callerAddress", TextField.class);
			txtCallerEmail = (TextField) binder.buildAndBind("Caller Email", "callerEmail", TextField.class);
			
			txtInpatientNumber = (TextField) binder.buildAndBind("Inpatient Number","inpatientNumber",TextField.class);
			txtInpatientNumber.setValidationVisible(false);
			txtLateIntimation = (TextField) binder.buildAndBind("Reason for late Intimation","lateIntimationReason",TextField.class);
			txtLateIntimation.setValidationVisible(false);
			
			chkPatientNotCovered = new CheckBox("Patient Not Covered");
			chkPatientNotCovered.setValue(intimation.getPatientNotCovered());
			chkPatientNotCovered.setReadOnly(true);
			
			categoryType = new TextField("Category Type");
			parentName = new TextField("Parent Name");
			age = new TextField("Age");
			patientName = new TextField("Patient Name");
			dob = new TextField("Date of Birth");
			
			if(bean.getClaimProcessType() !=null && bean.getClaimProcessType().equalsIgnoreCase("P"))
			{
				categoryType.setVisible(true);
				parentName.setVisible(true);
				age.setVisible(true);
				patientName.setVisible(true);
				dob.setVisible(true);
				
			}
			else
			{
				categoryType.setVisible(false);
				parentName.setVisible(false);
				age.setVisible(false);
				patientName.setVisible(false);
				dob.setVisible(false);
			}
			
			
			if(null != intimationByKey && null != intimationByKey.getPolicy() && 
					null != intimationByKey.getPolicy().getProduct() && 
					null != intimationByKey.getPolicy().getProduct().getKey() &&
					(ReferenceTable.GPA_PRODUCT_KEY.equals(intimationByKey.getPolicy().getProduct().getKey()))){
				
				cmbInsuredPatientName.setValue(intimationObj.getPaPatientName());
			}
			
		    FormLayout firstForm = new FormLayout(txtIntimationNo,txtIntimationTime,txtCpuCode,cmbModeOfIntimation,txtIntimatedBy,cmbInsuredPatientName
		    		,chkPatientNotCovered,txtHealthCardNo);
		    
		    if(this.bean.isJioPolicy()){
		    	firstForm.addComponent(txtEmpId);
		    }
		    
		    firstForm.addComponents(txtName,txtRelationship,admissionDate,txtAdmissionType,txtInpatientNumber,txtLateIntimation,
		    		txtIntimatorName, txtCallerContactNo, txtCallerAddress, txtCallerEmail,
		    		categoryType, parentName, age, patientName, dob, txtReasonForAdmission);
		    firstForm.setSpacing(true);
		    setReadOnly(firstForm, true);
		    
		    txtPolicyNo = (TextField) binder.buildAndBind("Policy No","policyNumber",TextField.class);
		    txtIssuingOffice = (TextField) binder.buildAndBind("Policy Issuing Office","policyIssuing",TextField.class);
		    txtProductName = (TextField) binder.buildAndBind("Product Name","productName",TextField.class);
		    txtInsuredName = (TextField) binder.buildAndBind("Proposer Name","patientName",TextField.class);  
		    txtState = (TextField) binder.buildAndBind("State", "state", TextField.class);
		    txtState.setNullRepresentation("");
		    txtCity = (TextField) binder.buildAndBind("City", "city", TextField.class);
		    txtCity.setNullRepresentation("");
		    txtArea = (TextField) binder.buildAndBind("Area", "area", TextField.class);
		    txtArea.setNullRepresentation("");
		    txtHospitalName = (TextField) binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		    txtAddress = (TextArea) binder.buildAndBind("Address","hospitalAddress",TextArea.class);
		    
		    txtHospitalPhoneNo = (TextField) binder.buildAndBind("Hospital Phone No", "hospitalPhoneNo", TextField.class);
		    txtHospitalFaxNo = (TextField) binder.buildAndBind("Hospital Fax No", "hospitalFaxNo", TextField.class);
		    txtHospitalDoctorName = (TextField) binder.buildAndBind("Hospital Doctor Name", "hospitalDoctorName", TextField.class);
		    txtRoomCategory = (TextField) binder.buildAndBind("Room Category", "roomCategory", TextField.class);
		    txtHospitalType = (TextField) binder.buildAndBind("Hospital Type", "hospitalTypeValue", TextField.class);
		    
		    txtHospitalCodeInternal = (TextField) binder.buildAndBind("Hospital Code (Internal)","hospitalInternalCode",TextField.class);
		    txtHospitalCodeIrda = (TextField) binder.buildAndBind("Hospital Code (IRDA)","hospitalIrdaCode",TextField.class);
		    txtComments = (TextField) binder.buildAndBind("Comments","comments",TextField.class);
		    txtSmCode = (TextField) binder.buildAndBind("SM Code","smCode",TextField.class);
		    txtSmName = (TextField) binder.buildAndBind("SM Name","smName",TextField.class);
		    txtBrokerCode = (TextField) binder.buildAndBind("Agent / Broker Code","agentBrokerCode",TextField.class);
		    txtBrokerName = (TextField) binder.buildAndBind("Agent / Broker Name","agentBrokerName",TextField.class);
		    
		    acctorDeath = new OptionGroup("Accident / Death");
		    acctorDeath.addItem(acct);
		    acctorDeath.addItem(death);
		    acctorDeath.addStyleName("horizontal");
		    acctorDeath.setEnabled(false);
		    
		    txtHospitalflag = (TextField) binder.buildAndBind("Hospital Flag","hospitalFlag",TextField.class);
		    
		    
		    if(bean.getClaimProcessType() !=null && bean.getClaimProcessType().equalsIgnoreCase("P"))
		    {
		    	if(intimation.getIncidenceFlag() != null)
		    	{
		    	if(intimation.getIncidenceFlag().equalsIgnoreCase("A"))
		    	{
		    		acctorDeath.setValue(acct);
		    	}
		    	else if(intimation.getIncidenceFlag().equalsIgnoreCase("D"))
		    	{
		    		acctorDeath.setValue(death);
		    	}
		    	acctorDeath.setVisible(true);
		    	}
		    }
		    else
		    {
		    	acctorDeath.setVisible(false);
		    }   

		
		    
		    FormLayout secondForm = new FormLayout(acctorDeath,txtPolicyNo,txtIssuingOffice,txtProductName,txtInsuredName,txtState,txtCity,txtArea,txtHospitalName,txtHospitalflag,txtAddress,
		    		txtHospitalPhoneNo, txtHospitalFaxNo, txtHospitalDoctorName, txtRoomCategory, 
		    		txtHospitalType,txtHospitalCodeInternal,txtHospitalCodeIrda,txtComments,txtSmCode,txtSmName,txtBrokerCode,txtBrokerName);
		    
		    setReadOnly(secondForm,true);
		
		    HorizontalLayout intimationHor = new HorizontalLayout(firstForm,secondForm);
		    intimationHor.setComponentAlignment(firstForm, Alignment.TOP_CENTER);
		    intimationHor.setComponentAlignment(secondForm, Alignment.MIDDLE_RIGHT);
		    intimationHor.setWidth("100%");
		    intimationHor.setMargin(true);
		    
		    Panel mainPanel = new Panel(intimationHor);
		    mainPanel.addStyleName("girdBorder");    
		    
//		    mainVerticalLayout = new VerticalLayout(mainPanel);
//		    mainVerticalLayout.setSpacing(true);
//		    mainVerticalLayout.setComponentAlignment(mainPanel, Alignment.TOP_CENTER);
		    
		    setCompositionRoot(mainPanel);
		    
		  	
	    }
	    
	    private void getHospitalDetails(
				ViewClaimStatusDTO intimationDetails,
				Hospitals hospitals) {
	    	EarlierRodMapper instance = EarlierRodMapper.getInstance();
			ViewClaimStatusDTO hospitalDetails = instance.gethospitalDetails(hospitals);
//			String name = null != intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()) ? intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()).getName() : "";
//			intimationDetails.setPatientNotCoveredName(name);
//			String relationship = null != intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()) ? intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()).getBabyRelationship().getValue() : "";
//			intimationDetails.setRelationshipWithInsuredId(relationship);
			if(hospitalDetails != null){
				intimationDetails.setState(hospitalDetails.getState());
				intimationDetails.setCity(hospitalDetails.getCity());
				intimationDetails.setArea(hospitalDetails.getArea());
				intimationDetails.setHospitalAddress(hospitals.getAddress());
				intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
				intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue());
				intimationDetails.setHospitalIrdaCode(hospitalDetails.getHospitalIrdaCode());
				intimationDetails.setHospitalInternalCode(hospitalDetails.getHospitalInternalCode());
				intimationDetails.setHospitalPhoneNo(hospitalDetails.getHospitalPhoneNo());
				intimationDetails.setHospitalFaxNo(hospitalDetails.getHospitalFaxNo());
				intimationDetails.setHospitalFlag(hospitals.getSuspiciousType());
			}
			EarlierRodMapper.invalidate(instance);
		}
	    
	    private void getTmpHospitalDetails(
				ViewClaimStatusDTO intimationDetails,
				TmpHospital hospitals) {
			//ViewClaimStatusDTO hospitalDetails = EarlierRodMapper.getInstance().gethospitalDetails(hospitals);
//			String name = null != intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()) ? intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()).getName() : "";
//			intimationDetails.setPatientNotCoveredName(name);
//			String relationship = null != intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()) ? intimationService.getNewBabyByIntimationKey(intimationDetails.getIntimationKey()).getBabyRelationship().getValue() : "";
//			intimationDetails.setRelationshipWithInsuredId(relationship);
			if(hospitals != null){
				
				if(hospitals.getStateId() != null && hospitals.getStateId() != null){
					State state = masterService.getStateByKey(hospitals.getStateId());
					if(state != null){
						intimationDetails.setState(state.getValue());
					}
				}
				
				intimationDetails.setState(hospitals.getState());
				if(hospitals.getCityId() != null && hospitals.getCityId() != null){
					CityTownVillage cityByKey = masterService.getCityByKey(hospitals.getCityId());
					if(cityByKey != null){
						intimationDetails.setCity(cityByKey.getValue());
					}
				}
				
				//intimationDetails.setArea(hospitals.getArea());
				intimationDetails.setHospitalAddress(hospitals.getAddress());
				intimationDetails.setHospitalName(hospitals.getHospitalName());
				intimationDetails.setHospitalTypeValue("Not-Registered");
				//intimationDetails.setHospitalIrdaCode(hospitals.getHospitalName());
				//intimationDetails.setHospitalInternalCode(hospitalDetails.getHospitalInternalCode());
				intimationDetails.setHospitalPhoneNo(hospitals.getContactNumber());
				intimationDetails.setHospitalFaxNo(hospitals.getFaxNumber() != null ? hospitals.getFaxNumber().toString() : "");
			}
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
				} else if(c instanceof GTextField){
					GTextField field = (GTextField) c;
					field.setWidth("300px");
					field.setNullRepresentation("-");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					
				}else if (c instanceof TextArea) {
					TextArea field = (TextArea) c;
					field.setWidth("350px");
			        field.setNullRepresentation("-");
					field.setReadOnly(readOnly);
					field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
			}
		}
	

}
