package com.shaic.reimbursement.uploadTranslatedDocument;

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
import com.shaic.claim.fileUpload.FileUploadDTO;
import com.shaic.claim.fileUpload.FileUploadTable;
import com.shaic.claim.fileUpload.ReferToCoordinatorGrid;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class UploadTranslatedDocumentViewImpl extends AbstractMVPView implements UploadTranslatedDocumentView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Instance<FileUploadTable> tableViewInstance;

	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;

	private FileUploadTable UploadlistsTable;
	
	@Inject
	private FileUploadDTO bean;
	
	private SearchProcessTranslationTableDTO fileUploadDTO;
	
	@Inject
	private ViewDetails viewDetails;

	private VerticalLayout verticalMain;
	
	private Map<String, Object> referenceData;
	
	private NewIntimationDto intimationDto;
	
	@Inject
	private Instance<ReferToCoordinatorGrid> referToCoordinatorTableInstance;
	
	private ReferToCoordinatorGrid referToCoordinatorObj;
	
	private ClaimDto claimDto;
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	////private static Window popup;
	PreauthDTO preauthDTO = null;
	
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
		
		fireViewEvent(UploadTranslatedDocumentPresenter.SET_TABLE_DATA,fileUploadDTO);
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(rrcDTO);
		//preauthDTO.setRodHumanTask(fileUploadDTO.getHumanTaskR3());

		UploadlistsTable = tableViewInstance.get();
		UploadlistsTable.init("", false);
		UploadlistsTable.setWidth("100.0%");
		
		UploadlistsTable.setReference(new HashedMap());
		UploadlistsTable.addBeanToList(this.bean);
		
		referToCoordinatorObj = referToCoordinatorTableInstance.get();
		referToCoordinatorObj.init(bean,this.fileUploadDTO,SHAConstants.REIMBURSEMENT_REFER_TO_COORDINATOR);
		
		VerticalSplitPanel mainPanel = new VerticalSplitPanel();
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.init(this.intimationDto,this.claimDto,
				"Process Translation / Misc Request",fileUploadDTO.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		viewDetails.initView(this.intimationDto.getIntimationId(),this.fileUploadDTO.getRodKey(), ViewLevels.INTIMATION, false,"Process Translation / Misc Request");
		
//		addListener();
		HorizontalLayout horimain = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		horimain.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		horimain.setWidth("100%");
		//verticalMain = new VerticalLayout(viewDetails,/*UploadlistsTable*/referToCoordinatorObj/*buttonHorLayout*/);
		verticalMain = new VerticalLayout(horimain,referToCoordinatorObj);
		verticalMain.setSpacing(true);
//		verticalMain.setComponentAlignment(buttonHorLayout,
//				Alignment.BOTTOM_CENTER);
		mainPanel.setSecondComponent(verticalMain);

		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
//		setHeight("100%");
		mainPanel.setSizeFull();
		mainPanel.setHeight("700px");
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
		HorizontalLayout alignmentHLayout = new HorizontalLayout(
				btnRRC);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(UploadTranslatedDocumentPresenter.VALIDATE_COORDINATOR_REPLY_FOR_REIMB_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT);
			
			
			
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

	@Override
	public void lists(List<FileUploadDTO> lists) {
		
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void result() {
		
		Label successLabel = new Label("<b style = 'color: black;'> Claim Record Saved Successfully !!!</b>", ContentMode.HTML);
		
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
				fireViewEvent(MenuItemBean.UPLOAD_TRANSLATED_DOCUMENTSR3, true);
				
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
