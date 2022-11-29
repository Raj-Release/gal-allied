package com.shaic.claim.cashlessprocess.processicac.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.google.gwt.i18n.client.Messages.Select;
import com.google.gwt.user.client.ui.RadioButton;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fileUpload.selectrod.CoordinatorRODService;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.scoring.HospitalScoringDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;


public class ProcessICACRequestViewImpl extends AbstractMVPView implements ProcessICACRequestView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8551456904860347414L;

	@Inject
	private SearchProcessICACTableDTO bean;

	private VerticalLayout verticalMain;
	
	private HorizontalLayout buttonHorLayout;
	
	private Map<String, Object> referenceData;
	
	private TextArea txtRemarks;
	
	private NewIntimationDto newIntimationDTO;
	
	private ClaimDto claimDTO;

	private PreauthDTO preauthDto;
	
	private BeanFieldGroup<SearchProcessICACTableDTO> binder;

	private Button submitBtn;

	private Button cancelBtn;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@EJB
	private DBCalculationService dbCalculationService;

	@EJB
	private HospitalService hospitalService;
	
	private TextArea icacRequestRemarks;
			
	private TextArea icacResponseRemarks;
	private TextArea icacFinalResponseRemarks;

	private OptionGroup fiancalDecisionOption;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Instance<ProcessingDoctorDetails> processingDoctorDetailsInstance;

	private ProcessingDoctorDetails processingDoctorDetailsObj;
	
	@Inject
	private Instance<ProcessingICACTeamResponse> processIcacTeamRespInstance;

	private ProcessingICACTeamResponse processIcacTeamRespsObj;
	
	private TextField txtimplantApplicable;
	
	private FormLayout icacresponseRemarksform;
	
	private ArrayList<Component> mandatoryFields;
	
	private VerticalLayout icrAGBR ;
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			if (field != null) {
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
		}
	}

	@Override
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData=referenceData;	
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<SearchProcessICACTableDTO>(SearchProcessICACTableDTO.class);
		this.binder.setItemDataSource(bean);
		
	}

	@SuppressWarnings("deprecation")
	public void initView(SearchProcessICACTableDTO bean) {
		
		this.bean=bean;
		this.newIntimationDTO = bean.getClaimDto().getNewIntimationDto();
		this.claimDTO = bean.getClaimDto();
		this.preauthDto = bean.getPreauthDto();
		initBinder();
		addStyleName("view");
		setSizeFull();
		mandatoryFields = new ArrayList<Component>();
		
		VerticalSplitPanel mainPanel = new VerticalSplitPanel();
		
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(newIntimationDTO,claimDTO,"Process ICAC Request");
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		icacresponseRemarksform = new FormLayout();
		
		
		processingDoctorDetailsObj = processingDoctorDetailsInstance.get();
		processingDoctorDetailsObj.init(false);
		if(bean.getProcessingDoctorDetailsDTOs() !=null && !bean.getProcessingDoctorDetailsDTOs().isEmpty()){
			processingDoctorDetailsObj.addBeansToList(bean.getProcessingDoctorDetailsDTOs());
		}
		processIcacTeamRespsObj = processIcacTeamRespInstance.get();
		processIcacTeamRespsObj.init(false);
		if(bean.getProcessingICACTeamResponseDTO() !=null && !bean.getProcessingICACTeamResponseDTO().isEmpty()){
			processIcacTeamRespsObj.addBeansToList(bean.getProcessingICACTeamResponseDTO());
		}
		
		icacRequestRemarks = new TextArea("Refer to ICAC Remarks  <B style=color:red;> * </B>");
		icacRequestRemarks.setCaptionAsHtml(true);
		icacRequestRemarks.setNullRepresentation("");
		icacRequestRemarks.setWidth("350px");
		icacRequestRemarks.setHeight("80px");
		icacRequestRemarks.setMaxLength(3000);
		icacRequestRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(icacRequestRemarks,null,getUI(),SHAConstants.ICAC_REMARKS);
		
		icacResponseRemarks = new TextArea("ICAC Response  <B style=color:red;> * </B>");
		icacResponseRemarks.setCaptionAsHtml(true);
		icacResponseRemarks.setNullRepresentation("");
		icacResponseRemarks.setWidth("350px");
		icacResponseRemarks.setHeight("80px");
		icacResponseRemarks.setMaxLength(3000);
		icacResponseRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(icacResponseRemarks,null,getUI(),SHAConstants.ICAC_REMARKS);
		
		
		fiancalDecisionOption = new OptionGroup();
		fiancalDecisionOption.setCaption("ICAC Final Decision");
		fiancalDecisionOption.addItems(getReadioButtonOptions());
		fiancalDecisionOption.setItemCaption(true, "Yes");
		fiancalDecisionOption.setItemCaption(false, "No");
		fiancalDecisionOption.setStyleName("horizontal");
		fiancalDecisionOption.setValue(false);


		fiancalDecisionOption.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {

				boolean selected = (boolean)event.getProperty().getValue();
				if(selected){
					icacFinalResponseRemarks.setEnabled(true);
					icacresponseRemarksform.addComponent(icacFinalResponseRemarks);
					fiancalDecisionOption.setValue(selected);
				}else{
					icacresponseRemarksform.removeComponent(icacFinalResponseRemarks);
					icacFinalResponseRemarks.setEnabled(false);
					icacFinalResponseRemarks.clear();
					fiancalDecisionOption.setValue(false);
				}
			}
		});
		

		icacFinalResponseRemarks = new TextArea("ICAC Final Decision Remarks <B style=color:red;> * </B>");
		icacFinalResponseRemarks.setCaptionAsHtml(true);
		icacFinalResponseRemarks.setNullRepresentation("");
		icacFinalResponseRemarks.setEnabled(false);
		icacFinalResponseRemarks.setWidth("350px");
		icacFinalResponseRemarks.setHeight("80px");
		icacFinalResponseRemarks.setMaxLength(3000);
		icacFinalResponseRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(icacFinalResponseRemarks,null,getUI(),SHAConstants.ICAC_REMARKS);
		
		
		if(bean.getDirectIcacReq() != null && bean.getDirectIcacReq().equalsIgnoreCase(SHAConstants.N_FLAG)){
			icacresponseRemarksform =new FormLayout(icacResponseRemarks,fiancalDecisionOption);
		}else{
			icacresponseRemarksform =new FormLayout(icacRequestRemarks,icacResponseRemarks,fiancalDecisionOption);
		}
		
		HorizontalLayout remarkHLayout = new HorizontalLayout(icacresponseRemarksform);
		remarkHLayout.setMargin(true);
		remarkHLayout.setSpacing(true);
			
		getbuttonLayout();
		addListener();	
	
		
		VerticalLayout tableLayout = null;
		
		if(bean.getDirectIcacReq() != null && bean.getDirectIcacReq().equalsIgnoreCase(SHAConstants.N_FLAG)){
			tableLayout = new VerticalLayout(processingDoctorDetailsObj,processIcacTeamRespsObj,remarkHLayout);
		}else{
			tableLayout = new VerticalLayout(remarkHLayout);
		}
	    	
		
		tableLayout.setSpacing(true);
		
		viewDetails.initView(newIntimationDTO.getIntimationId(),bean.getTransactionKey(), ViewLevels.PREAUTH_MEDICAL,"Process ICAC Screen");
		
		verticalMain = new VerticalLayout(viewDetailsLayout(),tableLayout,buttonHorLayout);
		verticalMain.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		verticalMain.setSpacing(false);

		mainPanel.setSecondComponent(verticalMain);
		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainPanel.setSizeFull();
		mainPanel.setHeight("670px");
		setCompositionRoot(mainPanel);
	}
	
	@SuppressWarnings("deprecation")
	public HorizontalLayout commonTopLayout(){
		
		TextField txtClaimCount = new TextField("Claim Count");
		txtClaimCount.setValue(this.bean.getClaimCount().toString());
		txtClaimCount.setReadOnly(true);
		TextField dummyField = new TextField();
		dummyField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		FormLayout firstForm = new FormLayout(txtClaimCount,dummyField);
		dummyField.setReadOnly(true);
		Panel claimCount = new Panel(firstForm);
		claimCount.setWidth("130px");
		txtClaimCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.setHeight("30px");
		firstForm.setMargin(false);
		firstForm.setComponentAlignment(txtClaimCount, Alignment.TOP_LEFT);
		if(this.bean.getClaimCount() > 1 && this.bean.getClaimCount() <=2){
			claimCount.addStyleName("girdBorder1");
		}else if(this.bean.getClaimCount() >2){
			claimCount.addStyleName("girdBorder2");
		}
		if(preauthDto != null){
			HorizontalLayout formLayout = SHAUtils.newImageCRM(preauthDto);
			HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(preauthDto);
			HorizontalLayout hoapitalScore = SHAUtils.hospitalScore(preauthDto,hospitalService);
			HorizontalLayout crmLayout = new HorizontalLayout(claimCount,formLayout,hoapitalScore,hopitalFlag);
			HorizontalLayout icrAgentBranchLayout = SHAUtils.icrAgentBranch(preauthDto);
			icrAGBR = new VerticalLayout(crmLayout,icrAgentBranchLayout);
			crmLayout.setSpacing(true);
			crmLayout.setWidth("100%");

		}
		HorizontalLayout HLayout1 = new HorizontalLayout(icrAGBR);
		HLayout1.setSpacing(true);
		
		return HLayout1;
	 
	}
	
	private void getbuttonLayout(){
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout = new HorizontalLayout(submitBtn, cancelBtn);
		buttonHorLayout.setSpacing(true);
	}
	
	@SuppressWarnings("deprecation")
	private void addContainerNames() {
		
	}
	
	
	public void addListener() {

		submitBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				if(isvalid()){
					setvalue();
					String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					if(bean.getDirectIcacReq() != null && bean.getDirectIcacReq().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						fireViewEvent(ProcessICACRequestPresenter.SUBMIT_BUTTON_DIRECT_ICAC_REQ,bean,userName);
					}else{
						fireViewEvent(ProcessICACRequestPresenter.SUBMIT_BUTTON_ICAC_RESPONSE,bean,userName);	
					}				
				}
					
			}
		});

		cancelBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
						"NO", "Yes", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
							fireViewEvent(MenuItemBean.PROCESS_ICAC_REQUEST, true);
						} else {
							dialog.close();
						}
					}
				});
			}
		});		
		
	}
	
	@SuppressWarnings("deprecation")
	private void setvalue(){
		
		if(bean.getDirectIcacReq().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			bean.setDirectIcacRemarks(icacRequestRemarks.getValue()); 
		}
		bean.setResponseIcacRemark(icacResponseRemarks.getValue());
		if(fiancalDecisionOption.getValue().equals(true)){
			bean.setFinalResponseIcacRemark(icacFinalResponseRemarks.getValue());
			bean.setFinalResDecFlag(SHAConstants.YES_FLAG);
		}else{
			bean.setFinalResDecFlag(SHAConstants.N_FLAG);
		}
	
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}
	
	@SuppressWarnings("deprecation")
	public boolean isvalid(){
		
		boolean hasError =false;
		boolean haschanges =false;
		StringBuffer eMsg = new StringBuffer();
		
		if(icacResponseRemarks !=null && (icacResponseRemarks.getValue() == null
				|| icacResponseRemarks.getValue().isEmpty())){
			hasError = true;
			eMsg.append("Please Enter ICAC Response Remarks to Proceed Further. </br>");
		}
		
		if(icacRequestRemarks !=null && (icacRequestRemarks.getValue() == null
		|| icacRequestRemarks.getValue().isEmpty()) && bean.getDirectIcacReq().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			hasError = true;
			eMsg.append("Please Enter  the Refer to ICAC Remarks to Proceed Further. </br>");
		}	
		if(icacFinalResponseRemarks !=null && (icacFinalResponseRemarks.getValue() == null
				|| icacFinalResponseRemarks.getValue().isEmpty()) && fiancalDecisionOption.getValue().equals(true)){
			hasError = true;
			eMsg.append("Please Enter ICAC Final Decision Remarks to Proceed Further. </br>");
		}	
			
		
		if (hasError) {
			
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();
			hasError = true;
			return !hasError;
		} 
		return true;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void buildSuccessLayout() {

		StringBuffer successLabel = new StringBuffer("Process ICAC Record Saved Successfully.");
		
		final MessageBox msgBox = MessageBox
			    .createInfo()
			    .withCaptionCust("Information")
			    .withMessage(successLabel.toString())
			    .withOkButton(ButtonOption.caption("Process ICAC Request"))
			    .open();
		Button homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;
			@Override
			public void buttonClick(ClickEvent event) {	
				msgBox.close();
				fireViewEvent(MenuItemBean.PROCESS_ICAC_REQUEST, true);
			}
		});
		
	}
	
	@SuppressWarnings("deprecation")
	private HorizontalLayout viewDetailsLayout(){
		
		HorizontalLayout commonButtons =  null;
		if(bean.getDirectIcacReq() != null && bean.getDirectIcacReq().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			commonButtons = new HorizontalLayout(commonTopLayout(),viewDetails);
		}else{
			commonButtons = new HorizontalLayout(viewDetails);
		}
		
		commonButtons.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		commonButtons.setWidth("100%");
		commonButtons.setSpacing(false);
		
		return commonButtons;
	}
	
	 protected Collection<Boolean> getReadioButtonOptions() {
			Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
			coordinatorValues.add(true);
			coordinatorValues.add(false);
			
			return coordinatorValues;
		}
	
	 public void addFinalResponseRemarkLayout(){
		 
	 }
}
