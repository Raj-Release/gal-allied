package com.shaic.claim.hospitalCommunication;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class HospitalViewImpl extends AbstractMVPView implements HospitalView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private HospitalAcknowledgeDTO bean;
	
	@Inject
	private ViewDetails viewDetails;
	
	private BeanFieldGroup<HospitalAcknowledgeDTO> binder;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	private VerticalLayout mainPanel;

	private TextField claimedAmount;

	private TextField approvedAmount;

	private TextArea denialRemarks;

	private TextField hospitalcode;

	private TextField address1;

	private TextField address2;

	private TextField address3;
	
	private TextField state;

	private TextField city;

	private TextField pinCode;

	private TextField hospitalName;

	private TextField hospitalPhone;

	private TextField hospitalContactNo;

	private TextField authRepresentation;

	private TextField representName;

	private TextField hospitalCategory;

	private TextArea hospitalRemarks;

	private TextField roomCategory;

	private TextField designation;

	private TextArea remarks;
	
	private Button submitBtn;
	
	private Button cancelBtn;

	private TextField claimStatus;

	private FormLayout amountLayout;

	private FormLayout statusLayout;

	private HorizontalLayout submitLayout;

	private FormLayout communicationLayout;

	private HorizontalLayout firstHorizontal;

	private HorizontalLayout secondHorizontal;

	private FormLayout firstForm;

	private FormLayout secondForm;
	
	private Panel detailsPanel;

	private HorizontalLayout detailsHorizontal;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private VerticalLayout mainVertical;
	
	private NewIntimationDto intimationDto;
	
	private SearchAcknowledgeHospitalCommunicationTableDTO searchFormDto;
	
	private ComboBox patientStatus;
	
	private Long key;
	
	private ClaimDto claimDto;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	
	private RRCDTO rrcDTO;
	
	private PreauthDTO preauthDTO;


	@PostConstruct
	public void initView()
	{
	}

public void initView(HospitalAcknowledgeDTO bean,SearchAcknowledgeHospitalCommunicationTableDTO searchFormDto,NewIntimationDto intimationDto,ClaimDto claimDto)
{
	this.key=searchFormDto.getKey();
	this.searchFormDto=searchFormDto;
	this.rrcDTO = searchFormDto.getRrcDTO();
	
	preauthDTO = new PreauthDTO();
	preauthDTO.setRrcDTO(this.rrcDTO);
	
	this.bean=bean;
	this.claimDto = claimDto;
	this.intimationDto=intimationDto;
	this.binder= new BeanFieldGroup<HospitalAcknowledgeDTO>(HospitalAcknowledgeDTO.class);
	this.binder.setItemDataSource(this.bean);
	mainPanel = new VerticalLayout();
	//mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
	setHeight("100%");
	RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance.get();
	intimationDetailsCarousel.init(this.intimationDto,this.claimDto,"Acknowledge Hospital Communication", searchFormDto.getDiagnosis());
	intimationDetailsCarousel.setHeight("30%");
//	intimationDetailsCarousel.init(intimationDto, "Acknowledgment Hospital Communication");
	mainPanel.addComponent(intimationDetailsCarousel);
	mainPanel.addComponent(buildVerticalLayout());
	mainPanel.setSpacing(true);
	//mainPanel.setHeight("670px");
	setCompositionRoot(mainPanel);
}

public VerticalLayout buildVerticalLayout()
{
	binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	claimedAmount =(TextField) binder.buildAndBind("Amount Claimed   INR", "claimedAmount", TextField.class);
	claimedAmount.setReadOnly(true);
	claimedAmount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	approvedAmount = (TextField)binder.buildAndBind("Approved Amount INR","approvedAmount",TextField.class);
	approvedAmount.setReadOnly(true);
	approvedAmount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	approvedAmount.setNullRepresentation("0");
	
	amountLayout=new FormLayout(claimedAmount,approvedAmount);
	amountLayout.setMargin(false);
	
	claimStatus=(TextField)binder.buildAndBind("Claim Status","claimStatus",TextField.class);
	claimStatus.setReadOnly(true);
	
//	claimStatus.addItem("Approved");	
//	claimStatus.addItem("Rejected");
//	claimStatus.addItem("Query");
//	claimStatus.addItem("Denial of Cashless");
	
	denialRemarks=(TextArea)binder.buildAndBind("Remarks","denialRemarks",TextArea.class);
	denialRemarks.setReadOnly(true);
	denialRemarks.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	statusLayout=new FormLayout(claimStatus,denialRemarks);
	statusLayout.setMargin(false);
	
	viewDetails.initView(this.bean.getIntimationNumber(), ViewLevels.INTIMATION, false,"Acknowledge Hospital Communication");
	
//	HorizontalLayout viewHorizontal=new HorizontalLayout();
//	viewHorizontal.addComponent(viewDetails);
//	viewHorizontal.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
	
	firstHorizontal=new HorizontalLayout(amountLayout,statusLayout);
	
	hospitalcode=(TextField)binder.buildAndBind("Hospital Code","hopitalCode",TextField.class);
	hospitalcode.setReadOnly(true);
	hospitalcode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	address1=(TextField)binder.buildAndBind("Address 1","address1",TextField.class);
	address1.setReadOnly(true);
	address1.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	address2=(TextField)binder.buildAndBind("Address 2","address2",TextField.class);
	address2.setReadOnly(true);
	address2.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	address3=(TextField)binder.buildAndBind("Address 3","address3",TextField.class);
	address3.setReadOnly(true);
	address3.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	city=(TextField)binder.buildAndBind("City","city",TextField.class);
	city.setReadOnly(true);
	city.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	state=(TextField)binder.buildAndBind("State","state",TextField.class);
	state.setReadOnly(true);
	state.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	pinCode=(TextField)binder.buildAndBind("PinCode","pinCode",TextField.class);
	pinCode.setReadOnly(true);
	pinCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	hospitalName=(TextField)binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
	hospitalName.setReadOnly(true);
	hospitalName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	hospitalPhone=(TextField)binder.buildAndBind("Hospital Ph No","hospitalPhno",TextField.class);
	hospitalPhone.setReadOnly(true);
	hospitalPhone.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	authRepresentation=(TextField)binder.buildAndBind("Authorized Representation","authorizedRepresentative",TextField.class);
	authRepresentation.setReadOnly(true);
	authRepresentation.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	representName=(TextField)binder.buildAndBind("Name of Representation", "nameofRepresentative",TextField.class);
	representName.setReadOnly(true);
	representName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	hospitalCategory=(TextField)binder.buildAndBind("Hospital Category", "hospitalCategory",TextField.class);
	hospitalCategory.setReadOnly(true);
	hospitalCategory.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
	hospitalRemarks=(TextArea)binder.buildAndBind("Remarks","hospitalRemarks",TextArea.class);
	hospitalRemarks.setReadOnly(true);
	hospitalRemarks.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	hospitalRemarks.setHeight("100px");
	
	roomCategory=(TextField)binder.buildAndBind("Room Category", "roomCategory", TextField.class);
	roomCategory.setReadOnly(true);
	roomCategory.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	
    firstForm=new FormLayout(hospitalcode,address1,address2,address3,city,state,pinCode);
    firstForm.setMargin(false);
//    firstForm.addStyleName("layoutDesign");
	secondForm=new FormLayout(hospitalName,hospitalPhone,authRepresentation,representName,hospitalCategory,roomCategory,hospitalRemarks);
//    secondForm.addStyleName("layoutDesign");
	secondForm.setMargin(false);
	detailsHorizontal=new HorizontalLayout(firstForm,secondForm);
	detailsHorizontal.setCaption("Hospital Details");
	detailsHorizontal.addStyleName("panelHeader");
	detailsHorizontal.setMargin(false);
	detailsHorizontal.setSpacing(false);
	
	detailsPanel=new Panel(detailsHorizontal);
	//detailsPanel.addStyleName("panelHeader");
	detailsPanel.addStyleName("girdBorder");
	detailsPanel.setCaption("Hospital Details");
	detailsPanel.setSizeFull();
	
	hospitalContactNo=(TextField)binder.buildAndBind("Hospital Contact Name", "hospitalContactName", TextField.class);
	//Vaadin8-setImmediate() hospitalContactNo.setImmediate(true);
	hospitalContactNo.setWidth("-1px");
	hospitalContactNo.setNullRepresentation("");
	CSValidator hospitalValid = new CSValidator();
	hospitalValid.extend(hospitalContactNo);
	hospitalValid.setRegExp("^[a-zA-Z.' ]*$");
	hospitalValid.setPreventInvalidTyping(true);

	designation=(TextField)binder.buildAndBind("Designation","designation",TextField.class);
	//Vaadin8-setImmediate() designation.setImmediate(true);
	designation.setWidth("-1px");
	designation.setNullRepresentation("");
	CSValidator designationValid = new CSValidator();
	designationValid.extend(designation);
	designationValid.setRegExp("^[a-zA-Z 0-9.]*$");
	designationValid.setPreventInvalidTyping(true);
	
	remarks=(TextArea)binder.buildAndBind("Remarks","remarks",TextArea.class);
	//Vaadin8-setImmediate() remarks.setImmediate(true);
	remarks.setWidth("-1px");
	remarks.setNullRepresentation("");
//	CSValidator validator = new CSValidator();
//	validator.extend(remarks);
//	validator.setRegExp("^[a-zA-Z 0-9.]*$");
//	validator.setPreventInvalidTyping(true);
	
	communicationLayout=new FormLayout(hospitalContactNo,designation,remarks);
	communicationLayout.setMargin(false);
	
	Panel communicationPanel=new Panel(communicationLayout);
	communicationPanel.addStyleName("girdBorder");
	communicationPanel.setCaption("Hospital Communication");
//	communicationPanel.setHeight("327px");
	communicationPanel.setSizeFull();
	
	secondHorizontal=new HorizontalLayout(detailsPanel,communicationPanel);
	secondHorizontal.setSpacing(true);
	submitBtn=new Button("Submit");
	submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
	submitBtn.setWidth("-1px");
	submitBtn.setHeight("-10px");
	Panel secondHorizontalpanel =new Panel();
	secondHorizontalpanel.setContent(secondHorizontal);
	secondHorizontalpanel.setSizeFull();
	
	cancelBtn=new Button("Cancel");
	
	cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
	cancelBtn.setWidth("-1px");
	cancelBtn.setHeight("-10px");
	
	mandatoryFields.add(hospitalContactNo);
	mandatoryFields.add(remarks);
	
	showOrHideValidation(false);
	
	addListener();
	
	submitLayout=new HorizontalLayout(submitBtn,cancelBtn);
	submitLayout.setSpacing(true);
	
	//mainVertical=new VerticalLayout(viewDetails,firstHorizontal,secondHorizontal,submitLayout);
	mainVertical=new VerticalLayout(commonButtonsLayout(),firstHorizontal,secondHorizontalpanel,submitLayout);
	//mainVertical.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
	
	mainVertical.setComponentAlignment(submitLayout, Alignment.BOTTOM_CENTER);
	mainVertical.setSpacing(false);
	return mainVertical;

}



public HorizontalLayout commonButtonsLayout()
{
	Button btnRRC = new Button("RRC");
	btnRRC.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			validateUserForRRCRequestIntiation();
			
		}
		
	});
	
	
	Label dummyLabel =new Label();
	dummyLabel.setWidth("390px");
	HorizontalLayout alignmentHLayout = new HorizontalLayout(btnRRC,viewDetails);
	alignmentHLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
	alignmentHLayout.setWidth("100%");
	return alignmentHLayout;
}

private void validateUserForRRCRequestIntiation()
{
	fireViewEvent(AcknowledgeHospitalPresenter.VALIDATE_ACKNOWLEDGE_HOSPITAL_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
}

@Override
public void buildValidationUserRRCRequestLayout(Boolean isValid) {
	
		if (!isValid) {
			Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
		} 
	else
	{
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("85%");
		popup.setHeight("100%");
		rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
		//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
		//documentDetails.setClaimDto(bean.getClaimDTO());
		rewardRecognitionRequestViewObj.initPresenter(SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION);
		
		/*PreauthDTO preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);*/
		
		rewardRecognitionRequestViewObj.init(preauthDTO, popup);
		
		//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
		popup.setCaption("Reward Recognition Request");
		popup.setContent(rewardRecognitionRequestViewObj);
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

@Override
public void loadRRCRequestDropDownValues(
		BeanItemContainer<SelectValue> mastersValueContainer) {
	// TODO Auto-generated method stub
	rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
	
}


@Override
public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
	// TODO Auto-generated method stub
	rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
	
}

public void addListener() {
	
	submitBtn.addClickListener(new Button.ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			
			
			    if(("").equals(hospitalContactNo.getValue())){
			    	hospitalContactNo.setValue(null);
			    }
			    if(("").equals(remarks.getValue())){
			    	remarks.setValue(null);
			    }
				if (validatePage()) {
					bean.setDesignation(designation.getValue());
					bean.setHospitalContactName(hospitalContactNo.getValue());
					bean.setRemarks(remarks.getValue());

					fireViewEvent(AcknowledgeHospitalPresenter.SUBMIT_EVENT,
							bean, searchFormDto);
				}
			
		}
	});
	
	cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	
				                	VaadinSession session = getSession();
									SHAUtils.releaseHumanTask(searchFormDto.getUsername(), searchFormDto.getPassword(), searchFormDto.getTaskNumber(),session);
				                	
				                	fireViewEvent(MenuItemBean.ACKNOWLEDGE_HOSPITAL_COMMUNICATION, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
}


protected void showOrHideValidation(Boolean isVisible) {
	for (Component component : mandatoryFields) {
		AbstractField<?>  field = (AbstractField<?>)component;
		field.setRequired(!isVisible);
		field.setValidationVisible(isVisible);
		
	}
}

private boolean validatePage() {
	Boolean hasError = false;
	showOrHideValidation(true);
	StringBuffer eMsg = new StringBuffer();
	
	if (!this.binder.isValid()) {

		for (Field<?> field : this.binder.getFields()) {
			ErrorMessage errMsg = ((AbstractField<?>) field)
					.getErrorMessage();
			if (errMsg != null) {
				eMsg.append(errMsg.getFormattedHtmlMessage());
			}
			hasError = true;
		}
	}
	if (hasError) {
		setRequired(true);
		Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

		hasError = true;
		return !hasError;
	} 
		showOrHideValidation(false);
		return true;
	}

private void setRequired(Boolean isRequired) {

	if (!mandatoryFields.isEmpty()) {
		for (int i = 0; i < mandatoryFields.size(); i++) {
			AbstractField<?> field = (AbstractField<?>) mandatoryFields
					.get(i);
			field.setRequired(isRequired);
		}
	}
}

@Override
public void resetView() {
	// TODO Auto-generated method stub
	
}

@Override
public void setHospitalData(HospitalAcknowledgeDTO hospitalDto) {
	// TODO Auto-generated method stub
}

@Override
public void result() {
//	ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Hospital acknowledgement completed successfully !!!", new ConfirmDialog.Listener() {
//
//        public void onClose(ConfirmDialog dialog) {
//            if (dialog.isConfirmed()) {
//            	fireViewEvent(MenuItemBean.ACKNOWLEDGE_HOSPITAL_COMMUNICATION, true);
//            } else {
//                dialog.close();
//            }
//        }
//    });
//dialog.setStyleName(Reindeer.WINDOW_BLACK);
	
	Label successLabel = new Label("<b style = 'color: black;'>Hospital acknowledgement completed successfully !!!</b>", ContentMode.HTML);
	
//	Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
	
	Button homeButton = new Button("Home Page");
	homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
	layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
	layout.setSpacing(true);
	layout.setMargin(true);
	HorizontalLayout hLayout = new HorizontalLayout(layout);
	hLayout.setMargin(true);
	
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
			fireViewEvent(MenuItemBean.ACKNOWLEDGE_HOSPITAL_COMMUNICATION, true);
			
		}
	});
}

@Override
public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
	rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
}

@Override
public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
	rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
}

}
