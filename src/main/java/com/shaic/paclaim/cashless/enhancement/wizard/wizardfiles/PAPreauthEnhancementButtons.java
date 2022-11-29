package com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.HRMHospitalDetailsTableDTO;
import com.shaic.claim.preauth.HRMListenerTable;
import com.shaic.claim.preauth.HRMTable;
import com.shaic.claim.preauth.HRMTableDTO;
import com.shaic.claim.preauth.ViewEarlierHRMListenerTable;
import com.shaic.claim.preauth.ViewEarlierHRMTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTable;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.NegotiationDetails;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PAPreauthEnhancementButtons extends ViewComponent{

	private static final long serialVersionUID = -2228350512621831558L;

	@Inject
	private PreauthDTO bean;
	
	@Inject 
	private SearchPreauthTableDTO searchDTO;
	
	@Inject
	private PreAuthPreviousQueryDetailsTable preAuthPreviousQueryDetailsTable;
	
	@Inject
	private Intimation intimation;
	
	private GWizard wizard;
	
	@EJB
	private MasterService masterService;
	
	private File file;
	
	@EJB
	private PreauthService preauthService;
	
	private  Map<String, Object> referenceDataMap;
	
	private Window popup;
	
	@Inject
	private HRMListenerTable hrmListenerTable;
	
	@Inject
	private ViewEarlierHRMListenerTable viewEarilerHrmListenerTable;
	
	@Inject
	private HRMTable hrmtable;
	
	@Inject
	private ViewEarlierHRMTable viewEarlierHrmTable;
	
	private HRMTableDTO hrmDto;


	public Button referToHrm;
	private Button btnSubmit;
	private Button btnViewEarlierHrmDetails;
	
	
	@Inject
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	@EJB
	private IntimationService intimationService;
	
	public Button approveBtn;
	private Button queryBtn;
	private Button rejectBtn;
	private Button escalateClaimBtn;
	private Button referCoordinatorBtn;
	private Button withdrawPreauthBtn;
	private Button withdrawRejectBtn;
	public Button downSizePreauthBtn;
//	private Button btnSubmit;
	private Button btnCancel;
	private Button btnBack;
	public VerticalLayout wholeVlayout;
	private TextField initialApprovedAmtTxt;
	private TextField selectedCopyTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextField uniquePremiumAmt;
	private TextField amountToHospAftPremium;
	private TextArea approvalRemarksTxta;
	private TextArea queryRemarksTxta;
	private ComboBox cmbWithdraw;
	private TextArea txtWithdraw;
	private TextArea rejectionRemarksTxta;
	private ComboBox reasonForDenialCmb;
	private TextArea denialRemarksTxta;
	private ComboBox escalateToCmb;
	private ComboBox cmbSpecialistType;
	private Upload uploadFile;
	private TextArea escalationRemarksTxta;
	private ComboBox typeOfCoordinatorRequestCmb;
	private TextArea reasonForReferringTxta;
	public FormLayout dynamicFrmLayout;
	private OptionGroup sentToCPU;
	private TextArea remarksForCPU;
	private TextField totalApprvedAmtField;
	private TextField downsizedAmtField;
	private ComboBox cmbDownsizeReason;
	private TextArea downsizeRemarks;
	
	private ArrayList<Component> mandatoryFields; 
	
	private List<String> errorMessages;

	private ComboBox rejectionCategoryCmb;
	
	private TextField txtQueryCount;
	
	private OptionGroup negotiationOpinionTaken;	
	private TextArea txtNegotiationRemarks;	
	private FormLayout negotiationLayout;
	private TextField amtToNegotiate;
	
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	public void initView(PreauthDTO bean,GWizard wizard) 
	{
		errorMessages = new ArrayList<String>();
		mandatoryFields = new ArrayList<Component>();
		this.bean = bean;
		this.wizard = wizard;
		wholeVlayout = new VerticalLayout();
		wholeVlayout.setHeight("-1px");
		wholeVlayout.setWidth("-1px");
		wholeVlayout.setSpacing(true);
		HorizontalLayout buttonsHLayout = new HorizontalLayout();
		buttonsHLayout.setSizeFull();
		buttonsHLayout.setMargin(false);
		addListener();

		buttonsHLayout.addComponents(approveBtn,queryBtn,rejectBtn,escalateClaimBtn,referCoordinatorBtn, withdrawPreauthBtn, downSizePreauthBtn, withdrawRejectBtn,referToHrm);
//		buttonsHLayout.addComponents(approveBtn,queryBtn,rejectBtn,escalateClaimBtn,referCoordinatorBtn, withdrawPreauthBtn, downSizePreauthBtn);


		buttonsHLayout.setSpacing(true);
		wholeVlayout.addComponent(buttonsHLayout);
		dynamicFrmLayout = new FormLayout();
		dynamicFrmLayout.setHeight("100%");
		dynamicFrmLayout.setWidth("100%");
		dynamicFrmLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);
		wholeVlayout.addComponent(dynamicFrmLayout);
		
		enableOrDisable(false);
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getHospitalisationFlag() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.N_FLAG)) {
			bean.setIsDishonoured(true);
		}
		
		if (bean.getIsDishonoured()
				|| (bean.getPreauthDataExtractionDetails().getIllness() != null && bean
						.getPreauthDataExtractionDetails().getIllness().getId()
						.equals(ReferenceTable.RELATED_TO_EARLIER_CLAIMS))
				|| (null != bean.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo()
						&& !bean.getMaternityFlag() && bean
						.getPreauthDataExtractionDetails()
						.getHospitalisationDueTo().getId()
						.equals(ReferenceTable.MATERNITY_MASTER_ID))) {
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			withdrawPreauthBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
			downSizePreauthBtn.setEnabled(false);
		} else {
			approveBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			withdrawPreauthBtn.setEnabled(true);
			downSizePreauthBtn.setEnabled(true);
			escalateClaimBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
		}
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getHospitalisationFlag() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.N_FLAG)) {
			approveBtn.setEnabled(false);
		}
		setCompositionRoot(wholeVlayout);
	
	}
	
	public void setApprovedAmtValue(Integer amount) {
		if(initialApprovedAmtTxt != null) {
			initialApprovedAmtTxt.setValue(amount.toString());
		}
	}
	
	
	
	public String getApprovedAmount() {
		if(initialApprovedAmtTxt != null) {
			return initialApprovedAmtTxt.getValue();
		}
		return "0";
	}
	
	public void enableOrDisable(Boolean isDisableApproval) {
		
			if(approveBtn != null) {
				if(! bean.getIsDishonoured()){
				approveBtn.setEnabled(!isDisableApproval);
				}
			}
			if(downSizePreauthBtn != null) {
				if(! bean.getIsDishonoured()){
				downSizePreauthBtn.setEnabled(isDisableApproval);
				}
			}
			
			
	}
	
	
	private void addListener() {
		approveBtn = new Button("Approve");
		approveBtn.setHeight("-1px");
		//Vaadin8-setImmediate() approveBtn.setImmediate(true);
		approveBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
				 NegotiationDetails negotiationPending = preauthService.getNegotiationPending(bean.getClaimDTO().getKey());
				    if(negotiationPending != null){
				    	manageNegotation(intimationDtls,negotiationPending,SHAConstants.APPROVAL);
				    }else{
				    	 fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
				    }
			}
		});
		
		queryBtn = new Button("Query");
		queryBtn.setHeight("-1px");
		//Vaadin8-setImmediate() queryBtn.setImmediate(true);
		queryBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 4614951723748846970L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_QUERY_BUTTON_EVENT,null);
			}
		});
		
		rejectBtn = new Button("Reject");
		rejectBtn.setHeight("-1px");
		//Vaadin8-setImmediate() rejectBtn.setImmediate(true);
		rejectBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -4241727763379504532L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_REJECTION_EVENT,null);				
			}
		});
		
		
		escalateClaimBtn = new Button("Escalate Claim");
		escalateClaimBtn.setHeight("-1px");
		//Vaadin8-setImmediate() escalateClaimBtn.setImmediate(true);
		escalateClaimBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6641041437396264183L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_ESCALATE_EVENT, null);
			}
		});
		
		referCoordinatorBtn = new Button("Refer to Co-ordinator");		
		referCoordinatorBtn.setHeight("-1px");
		//Vaadin8-setImmediate() referCoordinatorBtn.setImmediate(true);
		referCoordinatorBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_REFERCOORDINATOR_EVENT,null);
			}
		});
		
        referCoordinatorBtn.setVisible(false);
		
		withdrawPreauthBtn = new Button("Withdraw Pre-auth");		
		withdrawPreauthBtn.setHeight("-1px");
		//Vaadin8-setImmediate() withdrawPreauthBtn.setImmediate(true);
		withdrawPreauthBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_EVENT,null);
			}
		});
		
		withdrawRejectBtn = new Button("Withdraw & Reject");		
		withdrawRejectBtn.setHeight("-1px");
		//Vaadin8-setImmediate() withdrawRejectBtn.setImmediate(true);
		withdrawRejectBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_WITHDRAW_AND_REJECT_EVENT,null);
			}
		});
		
		
		
		
		downSizePreauthBtn = new Button("Downsize Pre-auth");		
		downSizePreauthBtn.setHeight("-1px");
		//Vaadin8-setImmediate() downSizePreauthBtn.setImmediate(true);
		downSizePreauthBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				Intimation intimationDtls = intimationService.getIntimationByKey(bean.getIntimationKey());
				 NegotiationDetails negotiationPending = preauthService.getNegotiationPending(bean.getClaimDTO().getKey());
				    if(negotiationPending != null){
				    	manageNegotation(intimationDtls,negotiationPending,SHAConstants.DOWNSIZE_PRE_AUTH);
				    }else{
				    	fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);
				    }
			}
		});

		btnViewEarlierHrmDetails = new Button("View Earlier HRM");
		btnViewEarlierHrmDetails.setHeight("-1px");
		//Vaadin8-setImmediate() btnViewEarlierHrmDetails.setImmediate(true);
		
		btnBack = new Button("Back");
		btnBack.setHeight("-1px");
		//Vaadin8-setImmediate() btnBack.setImmediate(true);
		
		btnCancel = new Button("Cancel");
		btnCancel.setHeight("-1px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		btnCancel.addClickListener(new Button.ClickListener() {

    		 @Override
    		 public void buttonClick(ClickEvent event) {
	          
    			 
    			
    		 }
    	 });
		
		btnSubmit = new Button("Submit");
		btnSubmit.setHeight("-1px");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
		
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {				
				List<HRMTableDTO> tableList = hrmListenerTable.getValues();
				String err=preauthService.validate(bean.getNewIntimationDTO().getIntimationId(),tableList);
				if(err == null)
				{
					preauthService.submitHrmRequestDetails(tableList);
					buildSuccessLayout();
				}
				else
				{
					showErrorMessage(err);
				}
				
			} 
		});
		
		referToHrm = new Button("Refer To HRM");
		referToHrm.setHeight("-1px");
		//Vaadin8-setImmediate() referToHrm.setImmediate(true);
		referToHrm.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6641041437396264183L;

			@Override
			public void buttonClick(ClickEvent event) {				
			
				List<HRMHospitalDetailsTableDTO> hospitalDtoList = new ArrayList<HRMHospitalDetailsTableDTO>();	
				List<HRMTableDTO> hrmtableDtoList = new ArrayList<HRMTableDTO>();
				
					HRMHospitalDetailsTableDTO hospitalDto1 = new HRMHospitalDetailsTableDTO();
					hospitalDto1.setHardCodedString("Intimation Number");
					hospitalDto1.setHardCodedStringValue(bean.getNewIntimationDTO().getIntimationId());
					hospitalDto1.setHardCodedString1("Hoaspital Name");
					hospitalDto1.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getName());
					hospitalDtoList.add(hospitalDto1);
					HRMHospitalDetailsTableDTO hospitalDto2 = new HRMHospitalDetailsTableDTO();
					hospitalDto2.setHardCodedString("Hrm Id");
					hospitalDto2.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode());
					hospitalDto2.setHardCodedString1("Phone");
					hospitalDto2.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmContactNo());
					hospitalDtoList.add(hospitalDto2);
					HRMHospitalDetailsTableDTO hospitalDto3 = new HRMHospitalDetailsTableDTO();
					hospitalDto3.setHardCodedString("Name");
					hospitalDto3.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmUserName());
					hospitalDto3.setHardCodedString1("Email Id");
					hospitalDto3.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmMailId());
					hospitalDtoList.add(hospitalDto3);	
					
					hrmDto  = new HRMTableDTO();
					hrmDto.setAnhOrNanh(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
					hrmDto.setDiagnosis(bean.getNewIntimationDTO().getDiagnosis());
					hrmDto.setClaimedAmt(Double.parseDouble(bean.getAmountRequested()));
					//hrmDto.setRequestType(bean.getClaimDTO().getClaimType());
					hrmDto.setDocRemarks(bean.getDoctorNote());
					hrmDto.setIntimationNO(bean.getNewIntimationDTO().getIntimationId());
					hrmDto.setHrmId(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode());
					hrmDto.setCashlessKey(bean.getKey());
					hrmDto.setHospitalCode(bean.getNewIntimationDTO().getHospitalDto().getHospitalCode());
				
					List<ProcedureDTO>  procedureList =  bean.getPreauthDataExtractionDetails().getProcedureList();
					ArrayList<Long> packageList = new ArrayList<Long>();
					 
					 String procedureName = "";
					 for (ProcedureDTO procedureDTO : procedureList) {
						 
						 procedureName += procedureDTO.getProcedureName().getValue() + ",";
						 
						 packageList.add(procedureDTO.getPackageRate());
					}
					 if(null != packageList && !packageList.isEmpty())
					 {
					hrmDto.setPackageAmt(Collections.max(packageList));
					 }
					 
					 hrmDto.setSurgicalProcedure(procedureName);
					 
					 List<DiagnosisDetailsTableDTO> diagnosisList =  bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
					 
					 String diagnosisName = "";
					 for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						
						 diagnosisName += diagnosisDetailsTableDTO.getDiagnosis() + ",";
					}
					 
					 hrmDto.setDiagnosis(diagnosisName);
					
					 TmpEmployee employeeDetails  = preauthService.getLoginDetails(null != bean.getStrUserName() ? bean.getStrUserName().toLowerCase() : null);
					 if(null != employeeDetails){
						 
						 hrmDto.setDocName(employeeDetails.getEmpFirstName());
						 hrmDto.setDocUserId(employeeDetails.getEmpId());
					 }
					
					hrmDto.setAssigneeDateAndTime(new Timestamp(System.currentTimeMillis()));

					hrmtableDtoList.add(hrmDto);
					
				
				VerticalLayout horLayout = new VerticalLayout();
					
				hrmtable.viewInt(hospitalDtoList);
				hrmtable.init("",false,false);
				hrmtable.initTable();
				
				horLayout.addComponents(btnViewEarlierHrmDetails,hrmtable);				
			
				hrmListenerTable.init();			
				hrmListenerTable.initTable();
				referenceDataMap = new HashMap<String, Object>();
				
				SelectValue selectPreauth= new SelectValue();
				selectPreauth.setId(1l);
				selectPreauth.setValue("Pre-Auth");

				SelectValue selectEnhancement = new SelectValue();

				selectEnhancement.setId(2l);
				selectEnhancement.setValue("Enhancement");
				
				SelectValue selectInformation = new SelectValue();

				selectInformation.setId(3l);
				selectInformation.setValue("Information");
				

				List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
				selectVallueList.add(selectPreauth);
				selectVallueList.add(selectEnhancement);
				selectVallueList.add(selectInformation);				
				
				BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				selectValueContainer.addAll(selectVallueList);
				referenceDataMap.put("requestTypeContainer",selectValueContainer);
				hrmListenerTable.setReferenceData(referenceDataMap);
				setTableValues(hrmtableDtoList);
				popup = new com.vaadin.ui.Window();
		    	popup.setWidth("75%");
		    	popup.setHeight("90%");	
		    	HorizontalLayout hLayout = new HorizontalLayout();
		    	hLayout.addComponents(btnSubmit);
		    	VerticalLayout verticalLayout = new VerticalLayout();
		    	verticalLayout.addComponents(horLayout,hrmListenerTable,hLayout);			
				popup.setContent(verticalLayout);
				verticalLayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
//		    	popup.setContent();
		    	popup.setClosable(true);
		    	popup.center();
		    	popup.setResizable(false);
		    	popup.addCloseListener(new Window.CloseListener() {
		    		
		    		@Override
		    		public void windowClose(CloseEvent e) {
		    			System.out.println("Close listener called");
		    		}
		    	});

		    	popup.setModal(true);
		    	UI.getCurrent().addWindow(popup);		    	
		    				
			}
		});
		

		btnViewEarlierHrmDetails.addClickListener(new Button.ClickListener() {
			
		
			private static final long serialVersionUID = 7255298985095729669L;

			@Override
			public void buttonClick(ClickEvent event) {				
				
				List<HRMHospitalDetailsTableDTO> hospitalDtoList = new ArrayList<HRMHospitalDetailsTableDTO>();	
								
				HRMHospitalDetailsTableDTO hospitalDto1 = new HRMHospitalDetailsTableDTO();
				hospitalDto1.setHardCodedString("Intimation Number");
				hospitalDto1.setHardCodedStringValue(bean.getNewIntimationDTO().getIntimationId());
				hospitalDto1.setHardCodedString1("Hoaspital Name");
				hospitalDto1.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getName());
				hospitalDtoList.add(hospitalDto1);
				HRMHospitalDetailsTableDTO hospitalDto2 = new HRMHospitalDetailsTableDTO();
				hospitalDto2.setHardCodedString("Hrm Id");
				hospitalDto2.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmCode());
				hospitalDto2.setHardCodedString1("Phone");
				hospitalDto2.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmContactNo());
				hospitalDtoList.add(hospitalDto2);
				HRMHospitalDetailsTableDTO hospitalDto3 = new HRMHospitalDetailsTableDTO();
				hospitalDto3.setHardCodedString("Name");
				hospitalDto3.setHardCodedStringValue(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmUserName());
				hospitalDto3.setHardCodedString1("Email Id");
				hospitalDto3.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmMailId());
				hospitalDtoList.add(hospitalDto3);	
				
				viewEarlierHrmTable.viewInt(hospitalDtoList);
				viewEarlierHrmTable.init("",false,false);
				viewEarlierHrmTable.initTable();
				
				List<HRMTableDTO> earilerHrmDetails = preauthService.getEarlierHrmDetails(bean.getNewIntimationDTO().getIntimationId());
				List<DiagnosisDetailsTableDTO> diagnosisList =  bean.getPreauthDataExtractionDetails().getDiagnosisTableList();
				 
				for (HRMTableDTO hrmTableDTO : earilerHrmDetails) {
					
					String diagnosisName = "";
					
					 for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
						
						 diagnosisName += diagnosisDetailsTableDTO.getDiagnosis() + " ";
					}
					 
					 hrmTableDTO.setDiagnosis(diagnosisName);
				}
				 
				
				 
				viewEarilerHrmListenerTable.init();			
				viewEarilerHrmListenerTable.initTable();
			//	setTableValues(hrmtableDtoList);
				
				 
				setTableValuesForEarliarHrmDetails(earilerHrmDetails);
				VerticalLayout verticalLayout = new VerticalLayout();
		    	verticalLayout.addComponents(viewEarlierHrmTable,viewEarilerHrmListenerTable);	
		    	
				popup = new com.vaadin.ui.Window();
		    	popup.setWidth("75%");
		    	popup.setHeight("90%");					
				popup.setContent(verticalLayout);
//		    	popup.setContent();
		    	popup.setClosable(true);
		    	popup.center();
		    	popup.setResizable(false);
		    	popup.addCloseListener(new Window.CloseListener() {
		    		
		    		@Override
		    		public void windowClose(CloseEvent e) {
		    			System.out.println("Close listener called");
		    		}
		    	});

		    	popup.setModal(true);
		    	UI.getCurrent().addWindow(popup);
			}
		});
		if(this.bean.getIsCancelPolicy()){
			
            if(this.wizard != null){
            	this.wizard.getCancelButton().setEnabled(false);
            }
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			downSizePreauthBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
			withdrawPreauthBtn.setEnabled(false);
		}
		
	}
	
	
	private void setTableValues(List<HRMTableDTO> hrmtableDtoList)
	{
		
		if(null != hrmListenerTable)
	
			{
				if(null != hrmtableDtoList && !hrmtableDtoList.isEmpty())
				{
					for (HRMTableDTO hrmTableDto : hrmtableDtoList) {
						
						hrmListenerTable.addBeanToList(hrmTableDto);
					}
				}
			}
		}
	
	private void setTableValuesForEarliarHrmDetails(List<HRMTableDTO> hrmtableDtoList)
	{
		
		if(null != viewEarilerHrmListenerTable)
	
			{
				if(null != hrmtableDtoList && !hrmtableDtoList.isEmpty())
				{
					for (HRMTableDTO hrmTableDto : hrmtableDtoList) {
						
						viewEarilerHrmListenerTable.addBeanToList(hrmTableDto);
					}
				}
			}
		}
	
	public void setCancelPolicyProcess(PreauthDTO bean){
		this.bean = bean;
		if(this.bean.getIsCancelPolicy()){
				if(this.wizard != null){
	            	this.wizard.getCancelButton().setEnabled(false);
	         }
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			downSizePreauthBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
			withdrawPreauthBtn.setEnabled(false);
		}
		
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	@SuppressWarnings("unchecked")
	public void buildApproveLayout(Integer amt)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		initialApprovedAmtTxt = (TextField) binder.buildAndBind("Total Approved Amt", "initialTotalApprovedAmt", TextField.class);
		initialApprovedAmtTxt.setNullRepresentation("");
		initialApprovedAmtTxt.setValue(amt.toString());
		initialApprovedAmtTxt.setEnabled(false);
		
		
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			uniquePremiumAmt = (TextField) binder.buildAndBind("Less: II Installment Premium Amt","uniquePremiumAmt",TextField.class);
			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			uniquePremiumAmt.setValue(String.valueOf(uniqueInstallmentAmount));
			uniquePremiumAmt.setEnabled(false);
			uniquePremiumAmt.setNullRepresentation("0");
			
			amountToHospAftPremium = (TextField) binder.buildAndBind("Pre-auth Approved Amt - Hospital","amountToHospAftPremium",TextField.class);
			
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(initialApprovedAmtTxt.getValue()) - SHAUtils.getDoubleFromStringWithComma(uniquePremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt.longValue() < 0l ? 0l : aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");
			
		}
		
		initialTotalApprovedAmtTxt = (TextField) binder.buildAndBind("Pre-auth Approved Amt (till Date)", "initialApprovedAmt", TextField.class);
		initialTotalApprovedAmtTxt.setNullRepresentation("");
		initialTotalApprovedAmtTxt.setEnabled(false);
		initialTotalApprovedAmtTxt.setValue(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().toString() : null);
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			initialTotalApprovedAmtTxt.setValue(this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction() != null ? this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction().toString() : null);
		}
		
		TextField preauthAmtTillDate = new TextField("Enhancement App Amt");
		preauthAmtTillDate.setNullRepresentation("");
		preauthAmtTillDate.setEnabled(false);
		Integer value = this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().intValue() : 0;
		preauthAmtTillDate.setValue(String.valueOf(amt - value));
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			Double doubleFromStringWithComma = SHAUtils.getDoubleFromStringWithComma(amountToHospAftPremium.getValue());
			preauthAmtTillDate.setValue(String.valueOf(doubleFromStringWithComma.intValue() - SHAUtils.getDoubleFromStringWithComma(initialTotalApprovedAmtTxt.getValue()).intValue()));
		}
		
		approvalRemarksTxta = (TextArea) binder.buildAndBind("Enhancement App Remarks", "approvalRemarks",TextArea.class);

		approvalRemarksTxta.setMaxLength(4000);
		approvalRemarksTxta.setWidth("400px");

		addingSentToCPUFields();
		
		negotiationOpinionTaken = (OptionGroup) binder.buildAndBind(
				"Do you want to Refer for Settlement Negotiation", "negotiationDecisionTaken",
				OptionGroup.class);
		negotiationOpinionTaken.addItems(getReadioButtonOptions());
		negotiationOpinionTaken.setItemCaption(true, "Yes");
		negotiationOpinionTaken.setItemCaption(false, "No");
		negotiationOpinionTaken.setStyleName("horizontal");
	//	negotiationOpinionTaken.select(false);
		negotiationOpinionTaken.setVisible(false);
		
		bean.getPreauthMedicalDecisionDetails().setNegotiationDecisionTaken(null);
		bean.getPreauthMedicalDecisionDetails().setNegotiatePoints(null);
		
		fireViewEvent(PreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);
		addNegotiationListener();
		
		//negotiationLayout.addComponent(negotiationOpinionTaken);
	    //	negotiationLayout.setMargin(false);
		//negotiationLayout.setSpacing(false);	
		if(null != bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() && bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken()){
			genertateFieldsBasedOnNegotiation(true);
		}
		if(uniquePremiumAmt != null && amountToHospAftPremium != null) {
			dynamicFrmLayout.addComponents(negotiationOpinionTaken,initialApprovedAmtTxt, uniquePremiumAmt, amountToHospAftPremium, initialTotalApprovedAmtTxt, preauthAmtTillDate, approvalRemarksTxta);
		} else {
			dynamicFrmLayout.addComponents(negotiationOpinionTaken,initialApprovedAmtTxt, initialTotalApprovedAmtTxt, preauthAmtTillDate, approvalRemarksTxta);
		}
		if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
			if(bean.getIsNegotiationPending()){
				negotiationOpinionTaken.setEnabled(false);
				negotiationOpinionTaken.setValue(true);
				negotiationOpinionTaken.setVisible(true);
			}
			else
			{
				negotiationOpinionTaken.setVisible(true);
				negotiationOpinionTaken.setEnabled(true);
			}
			if(null != bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled() && bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled()){
				negotiationOpinionTaken.setVisible(false);
			}
		}
//		dynamicFrmLayout.addComponent(sentToCPU);
		wholeVlayout.addComponent(dynamicFrmLayout);
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
//		mandatoryFields.add(approvalRemarksTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
		
	}
	
	@SuppressWarnings("unchecked")
	public void buildDownsizeLayout(Integer amt, Object rejectionCategoryDropdownValues) {
		
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		totalApprvedAmtField = (TextField) binder.buildAndBind("Total Approved Amt", "approvedAmount", TextField.class);
		totalApprvedAmtField.setNullRepresentation("");
		totalApprvedAmtField.setValue(this.bean.getPreauthDataExtractionDetails().getTotalApprAmt() != null ? this.bean.getPreauthDataExtractionDetails().getTotalApprAmt().toString() : null);
		totalApprvedAmtField.setEnabled(false);
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			totalApprvedAmtField.setValue(this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction() != null ? this.bean.getPreauthDataExtractionDetails().getApprovedAmountAftDeduction().toString() : null);
		}
		
		downsizedAmtField = (TextField) binder.buildAndBind("Pre-auth Downsized Amount ", "downsizedAmt", TextField.class);
		downsizedAmtField.setNullRepresentation("");
		downsizedAmtField.setEnabled(false);
		downsizedAmtField.setValue(amt.toString());
		
		
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			uniquePremiumAmt = (TextField) binder.buildAndBind("Less: II Installment Premium Amt","uniquePremiumAmt",TextField.class);
			Integer uniqueInstallmentAmount = PremiaService.getInstance().getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			uniquePremiumAmt.setValue(String.valueOf(uniqueInstallmentAmount));
			uniquePremiumAmt.setEnabled(false);
			uniquePremiumAmt.setNullRepresentation("0");
			
			amountToHospAftPremium = (TextField) binder.buildAndBind("Pre-auth Downsized Amt - Hospital","amountToHospAftPremium",TextField.class);
			
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(downsizedAmtField.getValue()) - SHAUtils.getDoubleFromStringWithComma(uniquePremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt < 0d ? 0l : aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");
			
		}
		
		cmbDownsizeReason = (ComboBox) binder.buildAndBind("Downsize Reason ", "downSizeReason", ComboBox.class);
		cmbDownsizeReason.setContainerDataSource((BeanItemContainer<SelectValue>)rejectionCategoryDropdownValues);
		cmbDownsizeReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDownsizeReason.setItemCaptionPropertyId("value");
		
		if(bean.getPreauthMedicalDecisionDetails().getDownSizeReason() != null) {
			cmbDownsizeReason.setValue(bean.getPreauthMedicalDecisionDetails().getDownSizeReason());
		}
		
		downsizeRemarks = (TextArea) binder.buildAndBind("Downsize Remarks ", "downsizeRemarks", TextArea.class);
		downsizeRemarks.setWidth("400px");
		
		negotiationOpinionTaken = (OptionGroup) binder.buildAndBind(
				"Do you want to Refer for Settlement Negotiation", "negotiationDecisionTaken",
				OptionGroup.class);
		negotiationOpinionTaken.addItems(getReadioButtonOptions());
		negotiationOpinionTaken.setItemCaption(true, "Yes");
		negotiationOpinionTaken.setItemCaption(false, "No");
		negotiationOpinionTaken.setStyleName("horizontal");
	//	negotiationOpinionTaken.select(false);
		negotiationOpinionTaken.setVisible(false);
		
		bean.getPreauthMedicalDecisionDetails().setNegotiationDecisionTaken(null);
		bean.getPreauthMedicalDecisionDetails().setNegotiatePoints(null);
		
		fireViewEvent(PreauthEnhancemetWizardPresenter.NEGOTIATION_PENDING, bean);
		addNegotiationListener();
		
		//negotiationLayout.addComponent(negotiationOpinionTaken);
	    //	negotiationLayout.setMargin(false);
		//negotiationLayout.setSpacing(false);	
		if(null != bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken() && bean.getPreauthMedicalDecisionDetails().getNegotiationDecisionTaken()){
			genertateFieldsBasedOnNegotiation(true);
		}
		
		if(uniquePremiumAmt != null && amountToHospAftPremium != null) {
			dynamicFrmLayout.addComponents(negotiationOpinionTaken,totalApprvedAmtField, downsizedAmtField, uniquePremiumAmt, amountToHospAftPremium, cmbDownsizeReason, downsizeRemarks);
		} else {
			dynamicFrmLayout.addComponents(negotiationOpinionTaken,totalApprvedAmtField, downsizedAmtField, cmbDownsizeReason, downsizeRemarks);
		}
		
		if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
			if(bean.getIsNegotiationPending()){
				negotiationOpinionTaken.setEnabled(false);
				negotiationOpinionTaken.setValue(true);
				negotiationOpinionTaken.setVisible(true);
			}
			else
			{
				negotiationOpinionTaken.setVisible(true);
				negotiationOpinionTaken.setEnabled(true);
			}
			if(null != bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled() && bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled()){
				negotiationOpinionTaken.setVisible(false);
			}
		}
		
		wholeVlayout.addComponent(dynamicFrmLayout);
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(downsizedAmtField);
		mandatoryFields.add(cmbDownsizeReason);
		mandatoryFields.add(downsizeRemarks);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
		
	}
	
	public void buildQueryLayout()
	{
		intimation.setKey(this.bean.getIntimationKey());
		final List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO = preAuthPreviousQueryDetailsService.search(intimation);
		if(!PreAuthPreviousQueryDetailsTableDTO.isEmpty()) {
			alertMessage(SHAConstants.QUERY_RAISE_MESSAGE, PreAuthPreviousQueryDetailsTableDTO);
		} else {
			generateQueryDetails(PreAuthPreviousQueryDetailsTableDTO);
		}
		
	}
	
	public Boolean alertMessage(String message, final List<PreAuthPreviousQueryDetailsTableDTO> preauthQueryDetailsDto) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				generateQueryDetails(preauthQueryDetailsDto);
			}
		});
		return true;
	}

	private void generateQueryDetails(
			final List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO) {
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		preAuthPreviousQueryDetailsTable.init("Previous Query Details", false, false);
		intimation.setKey(this.bean.getIntimationKey());
		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);	
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(PreAuthPreviousQueryDetailsTableDTO.size()+"");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		
		queryRemarksTxta = (TextArea) binder.buildAndBind("Query Remarks","queryRemarks",TextArea.class);
		queryRemarksTxta.setMaxLength(4000);
		queryRemarksTxta.setWidth("400px");
		addingSentToCPUFields();
		dynamicFrmLayout.addComponent(txtQueryCount);
		dynamicFrmLayout.addComponent(queryRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		alignFormComponents();
		VerticalLayout vTblLayout = new VerticalLayout(preAuthPreviousQueryDetailsTable,dynamicFrmLayout);
		
		wholeVlayout.addComponent(vTblLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(queryRemarksTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
	}
	
	public void buildRejectLayout(Object rejectionCategoryDropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		addingSentToCPUFields();
		rejectionCategoryCmb = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
		rejectionCategoryCmb.setContainerDataSource((BeanItemContainer<SelectValue>)rejectionCategoryDropdownValues);
		rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rejectionCategoryCmb.setItemCaptionPropertyId("value");
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
			rejectionCategoryCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejectionCategory());
		}
		
		rejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks","rejectionRemarks",TextArea.class);
		rejectionRemarksTxta.setMaxLength(4000);

		rejectionRemarksTxta.setWidth("400px");

		dynamicFrmLayout.addComponent(rejectionCategoryCmb);
		dynamicFrmLayout.addComponent(rejectionRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(rejectionCategoryCmb);
		mandatoryFields.add(rejectionRemarksTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
		
	}
	
	@SuppressWarnings("unchecked")
	public void buildWithdrawLayout(Object withdrawDropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		addingSentToCPUFields();
		cmbWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal","withdrawReason",ComboBox.class);
		cmbWithdraw.setContainerDataSource((BeanItemContainer<SelectValue>)withdrawDropdownValues);
		cmbWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbWithdraw.setItemCaptionPropertyId("value");
		
		txtWithdraw = (TextArea) binder.buildAndBind("Withdrawal Remarks", "withdrawRemarks", TextArea.class);
		txtWithdraw.setMaxLength(4000);
		
		
		if(bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
			cmbWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
		}
		
		dynamicFrmLayout.addComponent(cmbWithdraw);
		dynamicFrmLayout.addComponent(txtWithdraw);
//		dynamicFrmLayout.addComponent(sentToCPU);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cmbWithdraw);
		mandatoryFields.add(txtWithdraw);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
	}
	
	
	@SuppressWarnings("unchecked")
	public void buildWithdrawAndRejectLayout(Object withdrawDropdownValues, Object rejectionDropDownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		addingSentToCPUFields();
		
		// WITHDRAW FIELDS
		
		cmbWithdraw = (ComboBox) binder.buildAndBind("Reason for Withdrawal","withdrawReason",ComboBox.class);
		cmbWithdraw.setContainerDataSource((BeanItemContainer<SelectValue>)withdrawDropdownValues);
		cmbWithdraw.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbWithdraw.setItemCaptionPropertyId("value");
		
		txtWithdraw = (TextArea) binder.buildAndBind("Withdrawal Remarks", "withdrawRemarks", TextArea.class);
		txtWithdraw.setMaxLength(4000);
		
		if(bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
			cmbWithdraw.setValue(bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
		}
		
		FormLayout withdrawFieldsLayout = new FormLayout();
		
		withdrawFieldsLayout.addComponent(cmbWithdraw);
		withdrawFieldsLayout.addComponent(txtWithdraw);
//		dynamicFrmLayout.addComponent(sentToCPU);
//		alignFormComponents();
//		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(cmbWithdraw);
		mandatoryFields.add(txtWithdraw);
//		showOrHideValidation(false);
		
		// REJECTION FIELDS.................
		rejectionCategoryCmb = (ComboBox) binder.buildAndBind("Rejection Category","rejectionCategory",ComboBox.class);
		rejectionCategoryCmb.setContainerDataSource((BeanItemContainer<SelectValue>)rejectionDropDownValues);
		rejectionCategoryCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		rejectionCategoryCmb.setItemCaptionPropertyId("value");
		
		if(bean.getPreauthMedicalDecisionDetails().getRejectionCategory() != null){
			rejectionCategoryCmb.setValue(bean.getPreauthMedicalDecisionDetails().getRejectionCategory());
		}
		
		rejectionRemarksTxta = (TextArea) binder.buildAndBind("Rejection Remarks","rejectionRemarks",TextArea.class);
		rejectionRemarksTxta.setMaxLength(4000);

		rejectionRemarksTxta.setWidth("400px");

		FormLayout rejectionFieldsLayout = new FormLayout();
		
		rejectionFieldsLayout.addComponent(rejectionCategoryCmb);
		rejectionFieldsLayout.addComponent(rejectionRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
//		alignFormComponents();
//		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(rejectionCategoryCmb);
		mandatoryFields.add(rejectionRemarksTxta);
		mandatoryFields.add(cmbWithdraw);
		mandatoryFields.add(txtWithdraw);
		showOrHideValidation(false);
		
		HorizontalLayout layout = new HorizontalLayout(withdrawFieldsLayout, rejectionFieldsLayout);
		layout.setSpacing(true);
		layout.setWidth("100%");
		dynamicFrmLayout.addComponent(layout);
		wholeVlayout.addComponent(dynamicFrmLayout);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
	}
	
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	public void buildEscalateLayout(Object escalateToValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
       final BeanItemContainer<SelectValue> masterValueByReference = masterService.getMasterValueByReference((ReferenceTable.SPECIALIST_TYPE));
		
		
		
//		cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistValue",ComboBox.class);
//		cmbSpecialistType.setContainerDataSource(masterValueByReference);
//		cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cmbSpecialistType.setItemCaptionPropertyId("value");
	
		escalateToCmb = (ComboBox) binder.buildAndBind("Escalate To","escalateTo",ComboBox.class);
		
		
		BeanItemContainer<SelectValue> escalateValue1 = (BeanItemContainer<SelectValue>) escalateToValues;
		List<SelectValue> escalateValues = escalateValue1.getItemIds();
		
		BeanItemContainer<SelectValue> escalateContainer2 = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		for (SelectValue selectValue : escalateValues) {
			if(selectValue.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
			escalateContainer2.addBean(selectValue);
			}	
		}
		
		if(bean.getDownSizePreauthDataExtrationDetails().getCMA5()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA5)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA4()){
			
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA4)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtrationDetails().getCMA3()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA3)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtractionDetails().getCMA2()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA2)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}else if(bean.getDownSizePreauthDataExtractionDetails().getCMA1()){
			for(SelectValue selectValue : escalateValues){
				if(selectValue.getId() > (ReferenceTable.CMA1)){
					escalateContainer2.addBean(selectValue);
				}
			}
		}
		
		
		
		escalateToCmb.setContainerDataSource(escalateContainer2);
		escalateToCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		escalateToCmb.setItemCaptionPropertyId("value");
		
		escalateToCmb.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				
				if(value != null && value.getId().equals(ReferenceTable.SPECIALIST_DOCTOR)){
					
					unbindField(cmbSpecialistType);
					cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistValue",ComboBox.class);
					cmbSpecialistType.setContainerDataSource(masterValueByReference);
					cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					cmbSpecialistType.setItemCaptionPropertyId("value");
					cmbSpecialistType.setVisible(true);
					
					dynamicFrmLayout.addComponent(cmbSpecialistType,1);
					mandatoryFields.add(cmbSpecialistType);
					
					showOrHideValidation(false);
					
				}else{
					unbindField(cmbSpecialistType);
					if(cmbSpecialistType != null){
						
						dynamicFrmLayout.removeComponent(cmbSpecialistType);
						mandatoryFields.remove(cmbSpecialistType);
						cmbSpecialistType.setVisible(false);
						
					}
					
				}
			}
		});
		
//		uploadFile = (Upload) binder.buildAndBind("File UpLoad","uploadFile",Upload.class);
		uploadFile  = new Upload("", new Receiver() {
			
			private static final long serialVersionUID = 4775959511314943621L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				// Create upload stream
		        FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		        	if(filename != null && ! filename.equalsIgnoreCase("")){
				            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
				            fos = new FileOutputStream(file);
		        	}
		        	else{
		        		  
		        
		        	}
//		        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//		        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//		        	}
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
		                .show(com.vaadin.server.Page.getCurrent());
		            return null;
		        }
		        return fos; // Return the output stream to write to
			}
		});	
		uploadFile.addSucceededListener(new SucceededListener() {
			
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				System.out.println("File uploaded" + event.getFilename());
				
				try{
					
					byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
					
					if(null != fileAsbyteArray )
					{
						
						Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
						boolean hasSpecialChar = p.matcher(event.getFilename()).find();
					//	if(hasSpecialChar)
						//{
						WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
							Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
							//TO read file after load
							if (flagUploadSuccess.booleanValue())
							{
								String token = "" + uploadStatus.get("fileKey");
								String fileName = event.getFilename();
							    bean.setTokenName(token);
							    bean.setFileName(fileName);
							    buildSuccessLayout();
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		uploadFile.setCaption(null);
		uploadFile.setButtonCaption(null);
		
		Button uploadBtn = new Button("Upload");
		HorizontalLayout uploadHor = new HorizontalLayout(uploadFile,uploadBtn);
		uploadHor.setSpacing(true);
		
		uploadBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				uploadFile.submitUpload();
			}
		});
		
		escalationRemarksTxta = (TextArea) binder.buildAndBind("Escalate Remarks","escalationRemarks",TextArea.class);
		escalationRemarksTxta.setMaxLength(100);
		escalationRemarksTxta.setWidth("400px");
		dynamicFrmLayout.addComponent(escalateToCmb);
//		dynamicFrmLayout.addComponent(cmbSpecialistType);
		dynamicFrmLayout.addComponent(uploadHor);
		dynamicFrmLayout.addComponent(escalationRemarksTxta);
//		cmbSpecialistType.setVisible(false);
	
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(escalationRemarksTxta);
		mandatoryFields.add(escalateToCmb);
		showOrHideValidation(false);
		
		
		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
		
	}
	
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> File Uploaded Successfully.</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				popup.close();
				//if(null != popup)
					
			}
		});
	}

	private void addingSentToCPUFields() {
//		unbindField(sentToCPU);
//		sentToCPU = (OptionGroup) binder.buildAndBind( "Send to CPU", "sentToCPUFlag", OptionGroup.class);
//		sentToCPU.addItems(getReadioButtonOptions());
//		sentToCPU.setItemCaption(true, "Yes");
//		sentToCPU.setItemCaption(false, "No");
//		sentToCPU.setStyleName("horizontal");
//		sentToCPU.select(false);
//		
//		sentToCPU.addValueChangeListener(new Property.ValueChangeListener() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				Boolean isChecked = false;
//				if(event.getProperty() != null && event.getProperty().getValue().toString() == "true") {
//					isChecked = true;
//				} 
//				
//				//fireViewEvent(PreauthWizardPresenter.PREAUTH_SENT_TO_CPU_SELECTED, isChecked);
//				fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_SENT_TO_CPU_SELECTED, isChecked);
//			}
//		});
	}
	
	@SuppressWarnings("unchecked")
	public void buildReferCoordinatorLayout(Object dropdownValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		typeOfCoordinatorRequestCmb = (ComboBox) binder.buildAndBind("Type of Coordinator Request","typeOfCoordinatorRequest",ComboBox.class);
		typeOfCoordinatorRequestCmb.setContainerDataSource((BeanItemContainer<SelectValue>)dropdownValues);
		reasonForReferringTxta = (TextArea) binder.buildAndBind("Reason For Refering","reasonForRefering",TextArea.class);
		reasonForReferringTxta.setMaxLength(100);
		reasonForReferringTxta.setWidth("400px");
		dynamicFrmLayout.addComponent(typeOfCoordinatorRequestCmb);
		dynamicFrmLayout.addComponent(reasonForReferringTxta);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(typeOfCoordinatorRequestCmb);
		mandatoryFields.add(reasonForReferringTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(true);
		wizard.getNextButton().setEnabled(false);
		
		
	}
	
	@SuppressWarnings("rawtypes")
	public void unbindAndRemoveComponents(AbstractComponent component)
	{
		for(int i=0;i<((FormLayout)component).getComponentCount();i++)
		{
			if(((FormLayout)component).getComponent(i) instanceof Upload)
			{
                continue;				
			}
			
			if(! (((FormLayout)component).getComponent(i) instanceof HorizontalLayout)){
				unbindField((AbstractField)((FormLayout)component).getComponent(i));
			}
		}
		dynamicFrmLayout.removeAllComponents();
		wholeVlayout.removeComponent(dynamicFrmLayout);		
		
		if( null != wholeVlayout && 0!=wholeVlayout.getComponentCount())
		{
			Iterator<Component> componentIterator = wholeVlayout.iterator();
			while(componentIterator.hasNext()) 
			{
				Component searchScrnComponent = componentIterator.next() ;
				if(searchScrnComponent instanceof  VerticalLayout )
				{
					((VerticalLayout) searchScrnComponent).removeAllComponents();
					
				}
			}
		}
	}
	
	private void unbindField(Field<?> field) {
		if (field != null)
		{
			Object propertyId = this.binder.getPropertyId(field);
			if(propertyId != null) {
				this.binder.unbind(field);	
			}
			
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void alignFormComponents()
	{
		if(dynamicFrmLayout != null)
		{
			for(int i=0; i<dynamicFrmLayout.getComponentCount();i++)
			{
				dynamicFrmLayout.setExpandRatio(dynamicFrmLayout.getComponent(i), 0.5f);
			}
		}
	}
	
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Please select Approve or Query or Reject or Denial or Escalte or Refer to coordinator or Downsize or Witdraw. </br>");
			return !hasError;
		}
		if(null != negotiationOpinionTaken && (null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable())){
			if(!(bean.getPreauthMedicalDecisionDetails().getIsNegotiationCancelled())){
				if(null == negotiationOpinionTaken.getValue())
				{
					errorMessages.add("Please select Refer for negotiation option. </br>");
					hasError = true;
				}
				else if((Boolean.TRUE.equals(negotiationOpinionTaken.getValue())) && 
						(null != txtNegotiationRemarks && null == txtNegotiationRemarks.getValue() || StringUtils.isBlank(txtNegotiationRemarks.getValue())))
				{
					errorMessages.add("Please Enter Points to Negotiate.</br>");
					hasError = true;
				}
				else if((Boolean.TRUE.equals(negotiationOpinionTaken.getValue())) && 
						(null != amtToNegotiate && null == amtToNegotiate.getValue() || StringUtils.isBlank(amtToNegotiate.getValue()))){
					errorMessages.add("Please Enter Amount to be Negotiated.</br>");
					hasError = true;
				}
			}
		}
		
		if(this.cmbDownsizeReason != null && this.cmbDownsizeReason.getValue() != null && bean.getStatusKey().equals(ReferenceTable.ENHANCEMENT_PREAUTH_DOWNSIZE_STATUS)) {
			if(downsizeRemarks.getValue() == null || downsizeRemarks.getValue().isEmpty()) {
				errorMessages.add("Please Enter Downsize Remarks.</br>");
				hasError = true;
			}	
		}

		
		if (!this.binder.isValid()) {
			 hasError = true;
			 for (Field<?> field : this.binder.getFields()) {
			    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
			    	if (errMsg != null) {
			    		errorMessages.add(errMsg.getFormattedHtmlMessage());
			    	}
			  }
		} else {
			try {
				this.binder.commit();
				
				if(this.bean.getPreauthMedicalDecisionDetails().getWithdrawReason() != null){
					this.bean.setWithdrawReason(this.bean.getPreauthMedicalDecisionDetails().getWithdrawReason());
				}
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		showOrHideValidation(false);
		return !hasError;
	}
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public void generateFieldsBasedOnSentToCPU(Boolean isChecked) {
		if(isChecked) {
			unbindField(remarksForCPU);
			remarksForCPU = (TextArea) binder.buildAndBind("Remarks for CPU","remarksForCPU",TextArea.class);
			remarksForCPU.setMaxLength(100);
			mandatoryFields.add(remarksForCPU);
			dynamicFrmLayout.addComponent(remarksForCPU);
			showOrHideValidation(false);
		} else {
			unbindField(remarksForCPU);
			mandatoryFields.remove(remarksForCPU);
			dynamicFrmLayout.removeComponent(remarksForCPU);
		}
	}
	
	private boolean validateSelectedCoPay (Double selectedCoPay)
	{
		if(null != this.bean.getCopay())
		{
			if(selectedCoPay >= this.bean.getCopay())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
	
	public void clearFields() {
		if(dynamicFrmLayout != null) {
			dynamicFrmLayout.removeAllComponents();
		}
	}
	
	 public void wizardCancelled() {
			ConfirmDialog dialog = ConfirmDialog
					.show(getUI(),
							"Confirmation",
							"Are you sure you want to cancel ?",
							"No", "Yes", new ConfirmDialog.Listener() {

								public void onClose(ConfirmDialog dialog) {
									if (!dialog.isConfirmed()) {
										// Confirmed to continue
										
										fireViewEvent(MenuItemBean.PROCESS_ENHANCEMENT,null);
									} else {
										// User did not confirm
									}
								}
							});

			dialog.setClosable(false);
			dialog.setStyleName(Reindeer.WINDOW_BLACK);

		}
	 
		public void showErrorMessage(String eMsg) {
			Label label = new Label(eMsg, ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);

			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		}
		
		
	public void disableApprove(Boolean isEnabled) {
		approveBtn.setEnabled(isEnabled);
	}
	
	public Double getHospitalAmtForUnique() {
		if(amountToHospAftPremium != null) {
			return SHAUtils.getDoubleFromStringWithComma(amountToHospAftPremium.getValue()) ;
		}
		return 0d;
	}
	
public void manageNegotation(final Intimation intimationDtls,final NegotiationDetails negotiation,final String decision){
		
		ConfirmDialog dialog = ConfirmDialog.show(UI.getCurrent(),"Confirmation", "Negotation is under progress. Do You Want to Cancel or Update?",
		        "Update", "Cancel", new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (!dialog.isConfirmed()) {
		                	 dialog.close();
		                	 showNagotiationRemarks(true, intimationDtls,negotiation,decision);
		                } else {
		                    dialog.close();
		                    showNagotiationRemarks(false, intimationDtls,negotiation,decision);
		                }
		            }
		        });
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
	}

public void showNagotiationRemarks(final Boolean isCancel,final Intimation intimationDtls
		,final NegotiationDetails negotiation,final String decision){

	VerticalLayout vLayout =  new VerticalLayout();
	final Window dialog = new Window();
	vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
	vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
	vLayout.setMargin(true);
	vLayout.setSpacing(true);
	final TextArea txtArea = new TextArea();
	txtArea.setMaxLength(4000);
	txtArea.setData(bean);
	//txtArea.setStyleName("Boldstyle");
	txtArea.setNullRepresentation("");
	//txtArea.setSizeFull();
	if(!isCancel){
		txtArea.setValue(negotiation.getNegotiationRemarks());
	}
	
	
//	if(("hospRmrks").equalsIgnoreCase(txtFld.getId()) || ("insuredRmrks").equalsIgnoreCase(txtFld.getId())){
		txtArea.setRows(25);
		txtArea.setHeight("30%");
		txtArea.setWidth("100%");
		dialog.setHeight("65%");
    	dialog.setWidth("65%");
		txtArea.setReadOnly(false);
//	}
		final TextField amtToNegotiatedFild = new TextField("Amount to be Negotiated");
		amtToNegotiatedFild.setValue(negotiation.getNegotiationAmt() != null ? String.valueOf(negotiation.getNegotiationAmt()) : "0");
		FormLayout negAmt = new FormLayout(amtToNegotiatedFild);
		negAmt.setVisible(false);
	
	
	Button okBtn = new Button("OK");
	okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	vLayout.addComponent(negAmt);
	vLayout.addComponent(txtArea);
	vLayout.addComponent(okBtn);
	vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
	
	
	String strCaption = "";
	
	if(isCancel){
		strCaption = "Cancel Remarks";
		negAmt.setVisible(false);
	}else{
		strCaption = "Update Remarks";
		negAmt.setVisible(true);
	}
	dialog.setCaption(strCaption);
			
	
	dialog.setClosable(true);
	
	dialog.setContent(vLayout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.setDraggable(true);
	
	dialog.addCloseListener(new Window.CloseListener() {
		
		@Override
		public void windowClose(CloseEvent e) {
			if(null != decision && SHAConstants.APPROVAL.equalsIgnoreCase(decision)){
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
			}
			else if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);
			}
			dialog.close();
		}
	});
	
	if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
		dialog.setPositionX(250);
		dialog.setPositionY(100);
	}
	getUI().getCurrent().addWindow(dialog);
	okBtn.addClickListener(new Button.ClickListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			if(txtArea.getValue() != null && ! txtArea.getValue().isEmpty() && !StringUtils.isBlank(txtArea.getValue())){
				if(isCancel){
					Status status = new Status();
					status.setKey(ReferenceTable.NEGOTIATION_CANCELLED);
					negotiation.setStatusId(status);
					negotiation.setNegotiationCancelRemarks(txtArea.getValue());
					bean.getPreauthMedicalDecisionDetails().setIsNegotiationCancelled(Boolean.TRUE);
				}else{
					negotiation.setCashlessKey(bean.getKey());
					negotiation.setNegotiationRemarks(txtArea.getValue());
					negotiation.setNegotiationAmt(Double.valueOf(amtToNegotiatedFild.getValue()));
					bean.getPreauthMedicalDecisionDetails().setIsNegotiationCancelled(Boolean.FALSE);
				}
				
				fireViewEvent(PAPreauthEnhancemetWizardPresenter.NEGOTIATION_CANCEL_OR_UPDATE, negotiation);
				SHAUtils.buildNegotiationSuccessLayout(getUI(),isCancel);
				
				if(null != decision && SHAConstants.APPROVAL.equalsIgnoreCase(decision)){
					fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_APPROVE_EVENT,null);
				}
				else if(null != decision && SHAConstants.DOWNSIZE_PRE_AUTH.equalsIgnoreCase(decision)){
					fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_DOWNSIZE_EVENT,null);
				}
				dialog.close();
			}else{
				showErrorMessage("Please Enter Remarks");
			}
		}
	});	

}

protected void addNegotiationListener() {
	negotiationOpinionTaken
	.addValueChangeListener(new Property.ValueChangeListener() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			Boolean isChecked = false;
			if (event.getProperty() != null && null != event.getProperty().getValue()
					&& event.getProperty().getValue().toString() == "true") {
				isChecked = true;
			}

			genertateFieldsBasedOnNegotiation(isChecked);
		}
	});
}

@SuppressWarnings("unchecked")
public void genertateFieldsBasedOnNegotiation(Boolean isChecked) {
	if (isChecked) {
		int componentIndex = dynamicFrmLayout.getComponentIndex(negotiationOpinionTaken);
		txtNegotiationRemarks = (TextArea) binder.buildAndBind("Points to Negotiate", "negotiatePoints", TextArea.class);
		txtNegotiationRemarks.setMaxLength(4000);
		txtNegotiationRemarks.setWidth("45%");	
		
		amtToNegotiate = (TextField) binder.buildAndBind("Amount to be Negotiated", "amtToNegotiated", TextField.class);
		CSValidator txtAmtToNeg = new CSValidator();
		txtAmtToNeg.setRegExp("^[0-9']*$");
		txtAmtToNeg.setPreventInvalidTyping(true);
		txtAmtToNeg.extend(amtToNegotiate);
		
		setRequiredAndValidation(txtNegotiationRemarks);
		setRequiredAndValidation(amtToNegotiate);
		//negotiationLayout.addComponent(txtNegotiationRemarks);
		dynamicFrmLayout.addComponent(amtToNegotiate,++componentIndex);
		dynamicFrmLayout.addComponent(txtNegotiationRemarks, ++componentIndex);
		mandatoryFields.add(txtNegotiationRemarks);	
		mandatoryFields.add(amtToNegotiate);
		if(null != bean.getIsNegotiationApplicable() && bean.getIsNegotiationApplicable()){
			if(bean.getIsNegotiationPending()){
				txtNegotiationRemarks.setReadOnly(false);
				txtNegotiationRemarks.setValue(bean.getPreauthMedicalDecisionDetails().getNegotiatePoints());
				txtNegotiationRemarks.setReadOnly(true);
				amtToNegotiate.setReadOnly(false);
				amtToNegotiate.setValue(bean.getPreauthMedicalDecisionDetails().getAmtToNegotiated());
				amtToNegotiate.setReadOnly(true);
			}
		}
		SHAUtils.handleTextAreaPopUp(txtNegotiationRemarks,null,getUI(),SHAConstants.STP_REMARKS,bean);
	} else {
		if (txtNegotiationRemarks != null) {
			unbindField(txtNegotiationRemarks);
			unbindField(amtToNegotiate);
			dynamicFrmLayout.removeComponent(txtNegotiationRemarks);
			dynamicFrmLayout.removeComponent(amtToNegotiate);
			mandatoryFields.remove(txtNegotiationRemarks);
			mandatoryFields.remove(amtToNegotiate);
		}
	}
}
private void setRequiredAndValidation(Component component) {
	AbstractField<Field> field = (AbstractField<Field>) component;
	field.setRequired(true);
	field.setValidationVisible(false);
}
}
