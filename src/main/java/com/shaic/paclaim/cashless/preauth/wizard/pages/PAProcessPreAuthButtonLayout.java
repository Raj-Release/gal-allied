package com.shaic.paclaim.cashless.preauth.wizard.pages;

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
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.coordinator.view.UploadedFileViewUI;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.HRMHospitalDetailsTableDTO;
import com.shaic.claim.preauth.HRMListenerTable;
import com.shaic.claim.preauth.HRMTable;
import com.shaic.claim.preauth.HRMTableDTO;
import com.shaic.claim.preauth.ViewEarlierHRMListenerTable;
import com.shaic.claim.preauth.ViewEarlierHRMTable;
import com.shaic.claim.preauth.search.PreAuthSearchService;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthMedicalDecisionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsService;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTable;
import com.shaic.claim.preauth.wizard.pages.PreAuthPreviousQueryDetailsTableDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
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
import com.vaadin.ui.themes.ValoTheme;
@UIScoped
public class PAProcessPreAuthButtonLayout extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 13676978370725064L;

	@Inject
	private PreauthDTO bean;
	
	@Inject
	private PreAuthPreviousQueryDetailsTable preAuthPreviousQueryDetailsTable;
	
	@Inject
	private SearchPreauthTableDTO preauthTableDto;
	
	@Inject
	private Intimation intimation;
	
	@EJB
	private MasterService masterService;
	
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PreAuthSearchService preauthSearchService;
	
	private File file;
	
	
	@Inject
	private HRMTable hrmtable;
	
	@Inject
	private ViewEarlierHRMTable viewEarlierHrmTable;
	
	
	private  Map<String, Object> referenceDataMap;
	
	@Inject
	private HRMListenerTable hrmListenerTable;
	
	@Inject
	private ViewEarlierHRMListenerTable viewEarilerHrmListenerTable;
	
	private Button referToHrm;
	private Button btnSubmit;
	private Button btnViewEarlierHrmDetails;
	
	private HRMTableDTO hrmDto;
	

	@Inject
	private PreAuthPreviousQueryDetailsService preAuthPreviousQueryDetailsService;
	
	private BeanFieldGroup<PreauthMedicalDecisionDTO> binder;
	
	private Button approveBtn;
	private Button queryBtn;
	private Button rejectBtn;
	private Button denialCashlessBtn;
	private Button escalateClaimBtn;
	private Button referCoordinatorBtn;
	private Button btnCancel;
	private Button btnBack;
	private VerticalLayout wholeVlayout;
	private TextField initialApprovedAmtTxt;
	private TextField selectedCopyTxt;
	private TextField initialTotalApprovedAmtTxt;
	private TextField uniquePremiumAmt;
	private TextField amountToHospAftPremium;
	private TextArea approvalRemarksTxta;
	private TextArea queryRemarksTxta;
	private ComboBox rejectionCategoryCmb;
	private TextArea rejectionRemarksTxta;
	private ComboBox reasonForDenialCmb;
	private TextArea denialRemarksTxta;
	private ComboBox escalateToCmb;
	private ComboBox cmbSpecialistType;
	private Upload uploadFile;
	private TextArea escalationRemarksTxta;
	private ComboBox typeOfCoordinatorRequestCmb;
	private TextArea reasonForReferringTxta;
	private FormLayout dynamicFrmLayout;
    private VerticalLayout vTblLayout;
	
	//Added for query table.
	//private FormLayout dynamicTblLayout;
	
	
	
	private OptionGroup sentToCPU;
	private TextArea remarksForCPU;
	
	private TextField txtQueryCount;
	
	private ArrayList<Component> mandatoryFields; 
	
	private List<String> errorMessages;
	
	@Inject
	private UploadedFileViewUI fileViewUI;
	
	private Button tmpViewBtn;
	
	private GWizard wizard;
	
	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() 
	{
		
	}
	
	public void initView(PreauthDTO bean, GWizard wizard) 
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
		addListener();
		buttonsHLayout.addComponents(approveBtn,queryBtn,rejectBtn,denialCashlessBtn,escalateClaimBtn,referCoordinatorBtn);
		buttonsHLayout.setSpacing(true);
		wholeVlayout.addComponent(buttonsHLayout);
		dynamicFrmLayout = new FormLayout();
		dynamicFrmLayout.setHeight("100%");
		dynamicFrmLayout.setWidth("100%");
		dynamicFrmLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);
		
		
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
			denialCashlessBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);
		} else {
			approveBtn.setEnabled(true);
			queryBtn.setEnabled(true);
			denialCashlessBtn.setEnabled(true);
			escalateClaimBtn.setEnabled(true);
			referCoordinatorBtn.setEnabled(true);
		}
		
		if(this.bean.getIsCancelPolicy()) {
			if(this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}
			
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			denialCashlessBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
		}
		
		if(bean.getNewIntimationDTO().getPolicy().getProduct().getHospitalisationFlag() != null && bean.getNewIntimationDTO().getPolicy().getProduct().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.N_FLAG)) {
			approveBtn.setEnabled(false);
		}
		
		if(null != bean.getNewIntimationDTO().getPolicy().getProduct().getKey() &&
				(((ReferenceTable.PACKAGE_PAC_PRD_003).equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) ||
						((ReferenceTable.FAMILY_HEALTH_OPTIMA_PRODUCT_KEY).equals(bean.getNewIntimationDTO().getPolicy().getProduct().getKey()))))
		{
			approveBtn.setEnabled(false);
		}
		
		wholeVlayout.addComponent(dynamicFrmLayout);
		binder = null;
		setCompositionRoot(wholeVlayout);
	
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
                fireViewEvent(PAPreauthWizardPresenter.PREAUTH_APPROVE_EVENT,null);
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
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_QUERY_BUTTON_EVENT,null);
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
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_REJECTION_EVENT,null);				
			}
		});
		
		denialCashlessBtn = new Button("Denial of Cashless");
		denialCashlessBtn.setHeight("-1px");
		//Vaadin8-setImmediate() denialCashlessBtn.setImmediate(true);
		denialCashlessBtn.addClickListener(new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -2447164537241200160L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_DENIAL_EVENT,null);
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
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_ESCALATE_EVENT, null);
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
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_REFERCOORDINATOR_EVENT,null);
			}
		});
		referCoordinatorBtn.setVisible(false);

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
				//	fireViewEvent(PreauthWizardPresenter.,null);
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
					hospitalDto3.setHardCodedStringValue1(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getHrmMailId() );
					hospitalDtoList.add(hospitalDto3);	
					
					hrmDto  = new HRMTableDTO();
					hrmDto.setAnhOrNanh(bean.getNewIntimationDTO().getHospitalDto().getRegistedHospitals().getNetworkHospitalType());
					hrmDto.setDiagnosis(bean.getNewIntimationDTO().getDiagnosis());
					hrmDto.setClaimedAmt(Double.parseDouble(bean.getAmountRequested()));
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
				selectPreauth.setValue(SHAConstants.HRM_PRE_AUTH);

				SelectValue selectEnhancement = new SelectValue();

				selectEnhancement.setId(2l);
				selectEnhancement.setValue(SHAConstants.HRM_ENHANCEMENT);
				
				SelectValue selectInformation = new SelectValue();

				selectInformation.setId(3l);
				selectInformation.setValue(SHAConstants.HRM_INFORMATION);
				

				List<SelectValue> selectVallueList = new ArrayList<SelectValue>();
				selectVallueList.add(selectPreauth);
				selectVallueList.add(selectEnhancement);
				selectVallueList.add(selectInformation);				
				
				BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				selectValueContainer.addAll(selectVallueList);
				referenceDataMap.put("requestTypeContainer",selectValueContainer);
				hrmListenerTable.setReferenceData(referenceDataMap);
				setTableValues(hrmtableDtoList);
				Window popup = new com.vaadin.ui.Window();
		    	popup.setWidth("75%");
		    	popup.setHeight("90%");	
		    	HorizontalLayout hLayout = new HorizontalLayout();
		    	hLayout.addComponents(btnSubmit);		    	
		    	VerticalLayout verticalLayout = new VerticalLayout();
		    	verticalLayout.addComponents(horLayout,hrmListenerTable,hLayout);
		    	verticalLayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
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
		    	
				Window popup = new com.vaadin.ui.Window();
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
	}


	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PreauthMedicalDecisionDTO>(PreauthMedicalDecisionDTO.class);
		this.binder.setItemDataSource(bean.getPreauthMedicalDecisionDetails());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void setCancelPolicyProcess(PreauthDTO bean){
		this.bean = bean;
		if(this.bean.getIsCancelPolicy()){
			if(this.wizard != null) {
				this.wizard.getCancelButton().setEnabled(false);
			}
			approveBtn.setEnabled(false);
			queryBtn.setEnabled(false);
			denialCashlessBtn.setEnabled(false);
			escalateClaimBtn.setEnabled(false);
			referCoordinatorBtn.setEnabled(false);	
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void buildApproveLayout(Integer amt)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		initialTotalApprovedAmtTxt = (TextField) binder.buildAndBind("Pre-auth Approved Amt","initialTotalApprovedAmt",TextField.class);
		initialTotalApprovedAmtTxt.setNullRepresentation("");
		initialTotalApprovedAmtTxt.setEnabled(false);
		initialTotalApprovedAmtTxt.setValue(amt.toString());
		
		if(ReferenceTable.PREMIUM_DEDUCTION_PRODUCT_KEYS.containsKey(bean.getNewIntimationDTO().getPolicy().getProduct().getKey())) {
			PremiaService premiaService = new PremiaService();
			uniquePremiumAmt = (TextField) binder.buildAndBind("Less: II Installment Premium","uniquePremiumAmt",TextField.class);
			uniquePremiumAmt.setNullRepresentation("0");
			Integer uniqueInstallmentAmount = premiaService.getUniqueInstallmentAmount(bean.getNewIntimationDTO().getPolicy().getPolicyNumber());
			uniquePremiumAmt.setValue(String.valueOf(uniqueInstallmentAmount));
			uniquePremiumAmt.setEnabled(false);
			amountToHospAftPremium = (TextField) binder.buildAndBind("Pre-auth Approved Amt - Hospital","amountToHospAftPremium",TextField.class);
			
			Double aftPremAmt = SHAUtils.getDoubleFromStringWithComma(initialTotalApprovedAmtTxt.getValue()) - SHAUtils.getDoubleFromStringWithComma(uniquePremiumAmt.getValue());
			amountToHospAftPremium.setValue(String.valueOf(aftPremAmt.longValue()) );
			amountToHospAftPremium.setEnabled(false);
			amountToHospAftPremium.setNullRepresentation("0");
		}
		
		approvalRemarksTxta = (TextArea) binder.buildAndBind("Approval Remarks", "approvalRemarks",TextArea.class);
		approvalRemarksTxta.setMaxLength(4000);
		addingSentToCPUFields();
		approvalRemarksTxta.setWidth("400px");
		if(uniquePremiumAmt != null && amountToHospAftPremium != null) {
			dynamicFrmLayout.addComponents(initialTotalApprovedAmtTxt, uniquePremiumAmt, amountToHospAftPremium, approvalRemarksTxta);
		} else {
			dynamicFrmLayout.addComponents(initialTotalApprovedAmtTxt, approvalRemarksTxta);
		}
		
//		dynamicFrmLayout.addComponent(sentToCPU);
		wholeVlayout.addComponent(dynamicFrmLayout);
		alignFormComponents();
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(initialApprovedAmtTxt);
		mandatoryFields.add(approvalRemarksTxta);
		showOrHideValidation(false);
		
		wizard.getFinishButton().setEnabled(false);
		wizard.getNextButton().setEnabled(true);
		
	}
	
	public void setApprovedAmtValue(Integer amount) {
		if(initialTotalApprovedAmtTxt != null) {
			initialTotalApprovedAmtTxt.setValue(amount.toString());
		}
	}
	
	public String getApprovedAmount() {
		if(initialTotalApprovedAmtTxt != null) {
			return initialTotalApprovedAmtTxt.getValue();
		}
		return "0";
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

	private void generateQueryDetails(List<PreAuthPreviousQueryDetailsTableDTO> PreAuthPreviousQueryDetailsTableDTO) {
		initBinder();
		if(null != vTblLayout )
		{
			wholeVlayout.removeComponent(vTblLayout);
			vTblLayout.removeAllComponents();
		}
		unbindAndRemoveComponents(dynamicFrmLayout);
		preAuthPreviousQueryDetailsTable.init("Previous Query Details", false, false);
		intimation.setKey(this.bean.getIntimationKey());
	
		/*for (PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO2 : PreAuthPreviousQueryDetailsTableDTO) {
			try{
				Date tempDate = SHAUtils.formatTimestamp(preAuthPreviousQueryDetailsTableDTO2.getQueryRaisedDate());
				preAuthPreviousQueryDetailsTableDTO2.setQueryRaisedDate(SHAUtils.formatDate(tempDate));
			}catch(Exception e){
				
			}
		}*/
		preAuthPreviousQueryDetailsTable.setTableList(PreAuthPreviousQueryDetailsTableDTO);
		txtQueryCount = new TextField("Query Count");
		txtQueryCount.setValue(PreAuthPreviousQueryDetailsTableDTO.size()+"");
		txtQueryCount.setReadOnly(true);
		txtQueryCount.setEnabled(false);
		queryRemarksTxta = (TextArea) binder.buildAndBind("Query Remarks","queryRemarks",TextArea.class);
		queryRemarksTxta.setMaxLength(4000);
		queryRemarksTxta.setWidth("400px");

		addingSentToCPUFields();
		//dynamicFrmLayout = new FormLayout(txtQueryCount,queryRemarksTxta);
		dynamicFrmLayout.addComponent(txtQueryCount);
		dynamicFrmLayout.addComponent(queryRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		//dynamicFrmLayout.addComponent(txtQueryCount,queryRemarksTxta);
		
		/*VerticalLayout verticalQueryLayout  = new VerticalLayout();
		verticalQueryLayout.setHeight("100%");
		verticalQueryLayout.setWidth("100%");
		verticalQueryLayout.addComponent(preAuthPreviousQueryDetailsTable);
		verticalQueryLayout.addComponent(dynamicFrmLayout);*/
		
		alignFormComponents();
		/*dynamicTblLayout.setHeight("100%");
		dynamicTblLayout.setWidth("100%");
		dynamicTblLayout.setMargin(true);
		dynamicFrmLayout.setSpacing(true);*/
		vTblLayout = new VerticalLayout(preAuthPreviousQueryDetailsTable,dynamicFrmLayout);
		//wholeVlayout.addComponent(new VerticalLayout(preAuthPreviousQueryDetailsTable,dynamicFrmLayout));
		wholeVlayout.addComponent(vTblLayout);
		//wholeVlayout.addComponent(new VertdynamicFrmLayout);
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
	
	public void buildDenialLayout(Object denialValues)
	{
		initBinder();
		unbindAndRemoveComponents(dynamicFrmLayout);
		
		addingSentToCPUFields();
		reasonForDenialCmb = (ComboBox) binder.buildAndBind("Reason For Denial","reasonForDenial",ComboBox.class);
		reasonForDenialCmb.setContainerDataSource((BeanItemContainer<SelectValue>)denialValues);
		reasonForDenialCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		reasonForDenialCmb.setItemCaptionPropertyId("value");
		denialRemarksTxta = (TextArea) binder.buildAndBind("Denial Remarks","denialRemarks",TextArea.class);
		denialRemarksTxta.setMaxLength(4000);

		denialRemarksTxta.setWidth("400px");

		dynamicFrmLayout.addComponent(reasonForDenialCmb);
		dynamicFrmLayout.addComponent(denialRemarksTxta);
//		dynamicFrmLayout.addComponent(sentToCPU);
		alignFormComponents();
		wholeVlayout.addComponent(dynamicFrmLayout);
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(reasonForDenialCmb);
		mandatoryFields.add(denialRemarksTxta);
		showOrHideValidation(false);
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
		
		
//       cmbSpecialistType = (ComboBox) binder.buildAndBind("Specialist Type","specialistValue",ComboBox.class);
//	   cmbSpecialistType.setContainerDataSource(masterValueByReference);
//	   cmbSpecialistType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//	   cmbSpecialistType.setItemCaptionPropertyId("value");
		
	
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
		
		escalateToCmb.setNullSelectionAllowed(false);
		
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
					
					String strFileName = event.getFilename();
					if (null != fileAsbyteArray) {
						//file gets uploaded in data directory when code comes here.
						if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
								event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
						{
							/*File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
							strFileName = convertedFile.getName();
							fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);*/
						}
						
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
							    bean.setFileName(strFileName);
			                    tmpViewBtn.setEnabled(true);
							    buildSuccessLayout();
//							    thisObj.close();
							}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		uploadFile.setCaption("");
		uploadFile.setButtonCaption(null);
		
		tmpViewBtn = new Button("View File");
	    tmpViewBtn.setEnabled(false);
	    tmpViewBtn.setStyleName(ValoTheme.BUTTON_BORDERLESS);
	    tmpViewBtn.addStyleName(ValoTheme.BUTTON_LINK);
	    tmpViewBtn.setWidth("50%");
	
        tmpViewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(bean.getFileName() != null && bean.getTokenName() != null){
					Window popup = new com.vaadin.ui.Window();
					popup.setWidth("75%");
					popup.setHeight("90%");
					fileViewUI.init(popup,bean.getFileName(), bean.getTokenName());
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
		});
		
		Button uploadBtn = new Button("Upload");
		HorizontalLayout uploadHor = new HorizontalLayout(uploadFile,uploadBtn,tmpViewBtn);
		uploadHor.setComponentAlignment(uploadBtn, Alignment.BOTTOM_CENTER);
		uploadHor.setComponentAlignment(tmpViewBtn, Alignment.BOTTOM_RIGHT);
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
				"<b style = 'color: green;'> Request Submitted Successfully.</b>",
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
				//popup.close();
				//if(null != popup)
					
				//fireViewEvent(MenuItemBean.PROCESS_PREAUTH, null);
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
//				fireViewEvent(PreauthWizardPresenter.PREAUTH_SENT_TO_CPU_SELECTED, isChecked);
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
	private void unbindAndRemoveComponents(AbstractComponent component)
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
	
	/*private void removePreviousQueryTbl(AbstractComponent component)
	{
		if(null != dynamicTblLayout && 0 != dynamicTblLayout.getComponentCount())
		{
			dynamicTblLayout.removeAllComponents();
			wholeVlayout.removeComponent(dynamicTblLayout);
		}
	}*/
	
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
			if(field != null) {
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
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
		
		/*if(!(null != remarksForCPU && null != remarksForCPU.getValue() && !("").equals( remarksForCPU.getValue())))
		{
			hasError = true;
			errorMessages.add("Please Enter Remarks For CPU.");
			return !hasError;
		}
		
		if(!(null != approvalRemarksTxta && null != approvalRemarksTxta.getValue() && !("").equals( approvalRemarksTxta.getValue())))
		{
			hasError = true;
			errorMessages.add("Please Enter Approval Remarks.");
			return !hasError;
		}*/
		
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Please select Approve or Query or Reject or Denial of Cashless or Escalate Claim or Refer to coordinator. </br>");
			return !hasError;
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
	
	
}
