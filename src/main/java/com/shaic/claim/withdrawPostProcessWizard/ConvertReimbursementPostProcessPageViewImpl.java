package com.shaic.claim.withdrawPostProcessWizard;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;


public class ConvertReimbursementPostProcessPageViewImpl extends AbstractMVPView implements ConvertReimbursementPostProcessPageView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<ConvertReimbursementPostProcessPage> ConvertClaimPageInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private ConvertClaimDTO bean;
	
	private ConvertReimbursementPostProcessPage convertClaimPage;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private NewIntimationDto intimationDto;
	
	private SearchConvertClaimTableDto searchFormDto;
	
	private VerticalLayout mainPanel;
	
	private RRCDTO rrcDTO;	
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	////private static Window popup;
	
	private PreauthDTO preauthDTO;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	@PostConstruct
	public void initView(){
		
	}
	
	public void init(ConvertClaimDTO bean,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto,SearchConvertClaimTableDto searchFormDto)
	{
		this.bean=bean;
		this.searchFormDto=searchFormDto;
		this.rrcDTO = searchFormDto.getRrcDTO();
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		
		this.intimationDto=intimationDto;
		mainPanel = new VerticalLayout();
		//mainPanel.setSplitPosition(24, Unit.PERCENTAGE);
		setHeight("100%");
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.intimationDto,"Convert Claim type to Reimbursement(In Process)");
		//intimationDetailsCarousel.setHeight("200px");
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Convert Claim type to Reimbursement(In Process)");
		
		convertClaimPage=ConvertClaimPageInstance.get();
		convertClaimPage.initView(this.bean,selectValueContainer);
		
		//VerticalLayout firstVertical=new VerticalLayout(viewDetails);
		HorizontalLayout firstVertical = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		firstVertical.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		firstVertical.setWidth("100%");
//		HorizontalLayout crmFlaggedLayout = SHAUtils.crmFlaggedLayout(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		HorizontalLayout hTempLayout = new HorizontalLayout(convertClaimPage,crmFlaggedComponents);
//		hTempLayout.setComponentAlignment(crmFlaggedLayout, Alignment.BOTTOM_RIGHT);
		hTempLayout.setSpacing(true);
		VerticalLayout wholeVertical=new VerticalLayout(firstVertical,/*convertClaimPage*/hTempLayout);
		wholeVertical.setSpacing(true);
		//wholeVertical.setComponentAlignment(firstVertical, Alignment.TOP_RIGHT);
		
//		VerticalLayout spaceVeritcLayout=new VerticalLayout());
//		spaceVeritcLayout.setHeight("100px");
		
		mainPanel.addComponent(intimationDetailsCarousel);
		//mainPanel.addComponent(spaceVeritcLayout);
		mainPanel.addComponent(wholeVertical);
		//mainPanel.setHeight("670px");
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
		
		
		Label dummyLabel =new Label();
		dummyLabel.setWidth("750px");
		HorizontalLayout alignmentHLayout = new HorizontalLayout(btnRRC,dummyLabel,viewDetails);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(ConvertReimbursementPostProcessPagePresenter.VALIDATE_CONVERT_CLAIM_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		isValid = true;
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
			//rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_CONVERT_CLAIM);
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_CONVERT_CLAIM_SEARCH_BASED);
			
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
	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void result() {
//		ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Claim converted successfully to reimbursement!!!", new ConfirmDialog.Listener() {
//
//            public void onClose(ConfirmDialog dialog) {
//                if (dialog.isConfirmed()) {
//                	fireViewEvent(MenuItemBean.CONVERT_CLAIM, true);
//                } else {
//                    dialog.close();
//                }
//            }
//        });
//dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
Label successLabel = new Label("<b style = 'color: black;'>Claim Record Saved Successfully!!! </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Convert Claim Home");
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
				fireViewEvent(MenuItemBean.CONVERT_CLAIM_OUTSIDE_PROCESS, true);
				
			}
		});
	}

}

