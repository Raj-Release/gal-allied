package com.shaic.claim.fileUpload;

import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.collections.map.HashedMap;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fileUpload.selectrod.CoordinatorRODService;
import com.shaic.claim.fileUpload.selectrod.CoordinatorRODTable;
import com.shaic.claim.fileUpload.selectrod.CoordinatorRODTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class FileUploadViewImpl extends AbstractMVPView implements
		FileUploadView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<FileUploadTable> tableViewInstance;

	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;

	private FileUploadTable UploadlistsTable;
	
	@Inject
	private FileUploadDTO bean;
	
	private SearchProcessTranslationTableDTO fileUploadDTO;
	
	@Inject
	private ViewDetails viewDetails;

	private VerticalLayout verticalMain;
	
	private Map<String, Object> referenceData;
	
	private NewIntimationDto intimationDto;
	
	private ClaimDto claimDto;
	
	private TextArea txtCoordRemarks;

//	private Button submitBtn;
//
//	private Button cancelBtn;
	
	@Inject
	private Instance<ReferToCoordinatorGrid> referToCoordinatorTableInstance;
	
	private ReferToCoordinatorGrid referToCoordinatorObj;

	//private HorizontalLayout buttonHorLayout;
	
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	PreauthDTO preauthDTO = null;
	
	@Inject
	private Instance<CoordinatorRODTable> rodTableInstance;

	private CoordinatorRODTable rodTableObj;
	
	@Inject
	private CoordinatorRODService coordinatorrodservice;
	
	@Inject
	private ReimbursementService reimbursementService;
	
	@Inject
	private Toolbar toolBar;
	
	@Override
	public void setReferenceData(Map<String, Object> referenceData,NewIntimationDto intimationDto,FileUploadDTO bean,SearchProcessTranslationTableDTO fileUploadDTO,ClaimDto claimDto) {
		this.referenceData=referenceData;
		this.intimationDto=intimationDto;
		this.bean=bean;
		this.claimDto=claimDto;
		this.fileUploadDTO.setDecisionKey(fileUploadDTO.getDecisionKey());
		
	}

	public void initView(SearchProcessTranslationTableDTO fileUploadDTO) {
		
		this.fileUploadDTO=fileUploadDTO;
		this.rrcDTO = fileUploadDTO.getRrcDTO();
		
		addStyleName("view");

		setSizeFull();
		
		fireViewEvent(fileUploadPresenter.SET_TABLE_DATA,fileUploadDTO);
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		//preauthDTO.setRodHumanTask(fileUploadDTO.getHumanTask());

		UploadlistsTable = tableViewInstance.get();
		UploadlistsTable.init("", false);
		UploadlistsTable.setWidth("100.0%");
		
		UploadlistsTable.setReference(new HashedMap());
		UploadlistsTable.addBeanToList(this.bean);
		
		referToCoordinatorObj = referToCoordinatorTableInstance.get();
		referToCoordinatorObj.init(bean,this.fileUploadDTO,SHAConstants.CASHLESS_REFER_TO_COORDINATOR_TABLE);
		
		VerticalSplitPanel mainPanel = new VerticalSplitPanel();
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.init(this.intimationDto,this.claimDto,
				"Process Translation / Misc Request", fileUploadDTO.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);

//		submitBtn = new Button("Submit");
//		cancelBtn = new Button("Cancel");
//		
//		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		submitBtn.setWidth("-1px");
//		submitBtn.setHeight("-10px");
//		
//		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
//		cancelBtn.setWidth("-1px");
//		cancelBtn.setHeight("-10px");
//		
//		buttonHorLayout = new HorizontalLayout(submitBtn, cancelBtn);
//		buttonHorLayout.setSpacing(true);
		
		viewDetails.initView(this.intimationDto.getIntimationId(),fileUploadDTO.getRodKey(), ViewLevels.INTIMATION, false,"Process Translation / Misc Request");
		
		addListener();
		
		//verticalMain = new VerticalLayout(viewDetails,/*UploadlistsTable*/referToCoordinatorObj/*buttonHorLayout*/);
		List<CoordinatorRODTableDTO> rodList = coordinatorrodservice.getRODByKey(fileUploadDTO.getRodKey());
		rodTableObj = rodTableInstance.get();
		rodTableObj.init(new CoordinatorRODTableDTO());
		rodTableObj.setVisibleColumns();
		rodTableObj.addList(rodList);
		
		Reimbursement reimbursementByKey = reimbursementService.getReimbursementByKey(fileUploadDTO.getRodKey());
//		List<DocAcknowledgement> docAckListByClaim = reimbursementService.getDocAckListByClaim(claimDto.getKey());
		
		VerticalLayout tableLayout = null;
		if ((null != fileUploadDTO.getLOBID() && fileUploadDTO.getLOBID().equals(ReferenceTable.PA_LOB_KEY))
				|| (null != intimationDto.getProcessClaimType() && intimationDto.getProcessClaimType().equalsIgnoreCase(SHAConstants.PA_TYPE))) {
			tableLayout = new VerticalLayout(buildCoordRmksDetailsLayout(reimbursementByKey),rodTableObj, referToCoordinatorObj);
		}else {
			tableLayout = new VerticalLayout(rodTableObj, referToCoordinatorObj);
		}
		tableLayout.setSpacing(true);
		
		verticalMain = new VerticalLayout(commonButtonsLayout(),/*UploadlistsTable*/tableLayout/*buttonHorLayout*/);
		verticalMain.setSpacing(false);
//		verticalMain.setComponentAlignment(buttonHorLayout,
//				Alignment.BOTTOM_CENTER);
		mainPanel.setSecondComponent(verticalMain);

		mainPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainPanel.setSizeFull();
		mainPanel.setHeight("670px");
		setCompositionRoot(mainPanel);
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
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(btnRRC,viewDetails);
		horizontalLayout.setComponentAlignment(viewDetails,Alignment.TOP_RIGHT);
		horizontalLayout.setSizeFull();
		return horizontalLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(fileUploadPresenter.VALIDATE_COORDINATOR_REPLY_QUERY_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_COORDINATOR_REPLY);
			
			
			
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
		
//		submitBtn.addClickListener(new Button.ClickListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
//				fireViewEvent(fileUploadPresenter.SUBMIT_BUTTON,bean,fileUploadDTO);
//				
//				
//			}
//		});
//		
//		cancelBtn.addClickListener(new Button.ClickListener() {
//				/**
//				 * 
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void buttonClick(ClickEvent event) {
//					ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
//					        "NO", "Yes", new ConfirmDialog.Listener() {
//
//					            public void onClose(ConfirmDialog dialog) {
//					                if (!dialog.isConfirmed()) {
//					                	fireViewEvent(MenuItemBean.UPLOAD_TRANSLATED_DOCUMENTS, true);
//					                } else {
//					                    dialog.close();
//					                }
//					            }
//					        });
//					dialog.setStyleName(Reindeer.WINDOW_BLACK);
//				}
//			});
	}


	@Override
	public void lists(List<FileUploadDTO> lists) {
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void result() {
//		ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Claim record saved successfully !!!", new ConfirmDialog.Listener() {
//
//            public void onClose(ConfirmDialog dialog) {
//                if (dialog.isConfirmed()) {
//                	fireViewEvent(MenuItemBean.UPLOAD_TRANSLATED_DOCUMENTS, true);
//                } else {
//                    dialog.close();
//                }
//            }
//        });
//dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
		Label successLabel = new Label("<b style = 'color: black;'>Claim record saved successfully !!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Upload Translated Document Home");
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
				toolBar.countTool();
				fireViewEvent(MenuItemBean.UPLOAD_TRANSLATED_DOCUMENTS, true);
				
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
	 
	 public HorizontalLayout buildCoordRmksDetailsLayout(Reimbursement reimbursementByKey) 
	 {
	 	txtCoordRemarks = new TextArea("Reason for Referring");//binder.buildAndBind("Reason for Referring","referToBillEntryBillingRemarks",TextArea.class);
	 	
	 	if (bean.getRequestorMarks() != null) {
	 		txtCoordRemarks.setValue(bean.getRequestorMarks());
	 	}
	 	
	 	txtCoordRemarks.setReadOnly(true);
	 	txtCoordRemarks.setEnabled(true);
		
		FormLayout billingLayout = new FormLayout(txtCoordRemarks);
		HorizontalLayout bLayout = new HorizontalLayout(billingLayout);
		
		if (null != reimbursementByKey.getStage() ) {
			if (reimbursementByKey.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
					&& null != reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag() 
					&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.N_FLAG)) {
				bLayout.setCaption("Referred from MA (Non-Hospitalization)");
			}else if (reimbursementByKey.getStage().getKey().equals(ReferenceTable.CLAIM_REQUEST_STAGE)
					&& null != reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag() 
					&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				bLayout.setCaption("Referred from MA (Hospitalization)");
			}else if (reimbursementByKey.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
					&& null != reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag() 
					&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.N_FLAG)) {
				bLayout.setCaption("Referred from Billing (Non-Hospitalization)");
			}else if (reimbursementByKey.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
					&& null != reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag() 
					&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				bLayout.setCaption("Referred from Billing (Hospitalization)");
			}else if (reimbursementByKey.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)
					&& null != reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag() 
					&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.N_FLAG)) {
				bLayout.setCaption("Referred from FA (Non-Hospitalization)");
			}else if (reimbursementByKey.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)
					&& null != reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag() 
					&& reimbursementByKey.getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
				bLayout.setCaption("Referred from FA (Hospitalization)");
			}else {
				bLayout.setCaption("Refer To Coordinator Details");
			}
		}else {
			bLayout.setCaption("Refer To Coordinator Details");
		}
		
		HorizontalLayout referToBillEntryLayout = new HorizontalLayout(bLayout);
		referToBillEntryLayout.setSpacing(true);
		referToBillEntryLayout.setMargin(true);
		
		
		return referToBillEntryLayout;
	}
	
}
