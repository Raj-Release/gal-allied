package com.shaic.claim.ped.outsideprocess;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.claim.lumen.createinpopup.PopupInitiateLumenRequestWizardImpl;
import com.shaic.claim.pedquery.PEDQueryDTO;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.InitiatePEDCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class InitiatePedViewImpl extends AbstractMVPView implements InitiatePedView{
	
	
	@Inject
	private InitiatePedEndorsementUI initiatePedEndorsementObj;
	
	private Button viewInsuredDetailsBtn;
	
	private Button viewClaimDetailsBtn;
	
	private Button viewEarlierPedBtn;
	
	private Button btnLumen;
	
	@Inject
	private ViewDetails viewDetails;
	
	private PEDQueryDTO bean;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private ViewPEDRequestWindow viewPEDRequestObj;
	

	
	@Inject
	private Instance<InitiatePEDCarousel> commonCarouselInstance;
	
	@Inject
	private LumenDbService lumenService;
	
	@Inject
	private Instance<PopupInitiateLumenRequestWizardImpl> initiateLumenRequestWizardInstance;
	
	private PopupInitiateLumenRequestWizardImpl initiateLumenRequestWizardObj;
	
	
	public void initView(PEDQueryDTO bean){
		
		this.bean = bean;
		
		viewDetails.initView(bean.getClaimDto().getNewIntimationDto().getIntimationId(), ViewLevels.PED_OUTSIDE_PROCESS, false,"Process PED Request");
		
		InitiatePEDCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
	//	intimationDetailsCarousel.init(new NewIntimationDto(), "PED Request");
		intimationDetailsCarousel.init(this.bean.getPolicyDto(),"Process PED Request");
		
		initiatePedEndorsementObj.init(bean);
		
		viewInsuredDetailsBtn = new Button("View Insured Details");
		viewClaimDetailsBtn = new Button("View Claim Details");
		viewEarlierPedBtn = new Button("View Earlier PED");
		btnLumen = new Button("Initiate Lumen");
		
		HorizontalLayout btnHorizontalLayout = new HorizontalLayout(viewInsuredDetailsBtn,viewClaimDetailsBtn,viewEarlierPedBtn,viewDetails);
		btnHorizontalLayout.setComponentAlignment(viewDetails, Alignment.TOP_LEFT);
		btnHorizontalLayout.setSpacing(true);
		
		HorizontalLayout btnHorizontalLayout2 = new HorizontalLayout(btnLumen);
		btnHorizontalLayout2.setComponentAlignment(btnLumen, Alignment.TOP_LEFT);
		btnHorizontalLayout2.setSpacing(true);
		
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean.getPreauthDTO());
		
		HorizontalLayout hlayout = new HorizontalLayout(btnHorizontalLayout2,hopitalFlag);
		hlayout.setSpacing(true);
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel,btnHorizontalLayout,hlayout,initiatePedEndorsementObj);
		mainVertical.setComponentAlignment(btnHorizontalLayout, Alignment.TOP_LEFT);
		mainVertical.setSpacing(true);
		
		setCompositionRoot(mainVertical);
		
		addListener();

	}
	
	public void addListener(){

		viewInsuredDetailsBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
	
				viewDetails.getInsuredDetailsList(bean.getClaimDto().getNewIntimationDto().getPolicy());
			}
			
		});
		
		viewClaimDetailsBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
	
				viewDetails.getViewPreviousClaimDetails(bean.getClaimDto().getNewIntimationDto().getIntimationId());
			}
			
		});
		
		viewEarlierPedBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				PreauthDTO preauthDTO = new PreauthDTO();
				ClaimDto claimDto = bean.getClaimDto();
				NewIntimationDto newIntimationDto = bean.getClaimDto().getNewIntimationDto();
				preauthDTO.setClaimDTO(claimDto);
				preauthDTO.setNewIntimationDTO(newIntimationDto);

				viewPEDRequestObj = viewPedRequest.get();
				viewPEDRequestObj.initView(preauthDTO, null,newIntimationDto.getKey(), newIntimationDto.getPolicy().getKey(), claimDto.getKey(),0l,false);
				viewPEDRequestObj.setPresenterString(SHAConstants.OUTSIDE_PROCESS_SCREEN);
				viewPEDRequestObj.setEnableorDisbleOptionGroup();
				showPopup(new VerticalLayout(viewPEDRequestObj));

			}
			
		});
		
		btnLumen.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				invokePreMedicalLumenRequest();
			}
		});
	}
	
	
	private void showPopup(Layout layout) {
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(layout);
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


	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
		// TODO Auto-generated method stub
		initiatePedEndorsementObj.setIcdBlock(icdBlockContainer);
		
	}

	@Override
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		// TODO Auto-generated method stub
		initiatePedEndorsementObj.setIcdCode(icdCodeContainer);
	}

	@Override
	public void setPEDCode(String pedCode) {
		// TODO Auto-generated method stub
		initiatePedEndorsementObj.setPEDCode(pedCode);
		
	}

	@Override
	public void buildSuccessLayout() {
		Label successLabel = new Label(
				"<b style = 'color: green;'>PED Endorsement Submitted Successfully </b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("Home");
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
				if(initiatePedEndorsementObj!=null){
					initiatePedEndorsementObj.clearObject();
				}
				fireViewEvent(MenuItemBean.PED_INITIATE, null);

			}
		});
	}
	
	private void invokePreMedicalLumenRequest(){
		/*List<Long> listOfClosedStatus = new ArrayList<Long>();
		listOfClosedStatus.add(ReferenceTable.BILL_ENTRY_PROCESS_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.FINANCIAL_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.CLAIM_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.BILLING_CLOSED_STATUS);
		listOfClosedStatus.add(140L);		
		listOfClosedStatus.add(ReferenceTable.PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.ZONAL_REVIEW_PROCESS_CLAIM_REQUEST_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.PREAUTH_CLOSED_STATUS);
		listOfClosedStatus.add(ReferenceTable.CREATE_ROD_CLOSED_STATUS);
		listOfClosedStatus.add(214L);
		
		List<Long> listOfSettledStatus = new ArrayList<Long>();
		listOfSettledStatus.add(ReferenceTable.FINANCIAL_SETTLED);
		listOfSettledStatus.add(ReferenceTable.PAYMENT_SETTLED);
		listOfSettledStatus.add(ReferenceTable.CLAIM_APPROVAL_APPROVE_STATUS);
		
		if(!listOfClosedStatus.contains(bean.getClaimDto().getStatusId())){
			if(!listOfSettledStatus.contains(bean.getClaimDto().getStatusId())){
				fireViewEvent(InitiatePedPresenter.INITIATE_PED_USER_LUMEN_REQUEST, bean);
			}else{
				showErrorMessage("Claim is settled, lumen cannot be initiated");
				return;
			}
		}else{
			showErrorMessage("Claim is closed, lumen cannot be initiated");
			return;
		}*/fireViewEvent(InitiatePedPresenter.INITIATE_PED_USER_LUMEN_REQUEST, bean);
	}
	
	@SuppressWarnings("static-access")
	private void showErrorMessage(String eMsg) {
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
	
	@Override
	public void buildInitiateLumenRequest(String intimationNumber){
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Initiate Lumen Request");
		popup.setWidth("75%");
		popup.setHeight("90%");
		LumenSearchResultTableDTO resultObj = lumenService.buildInitiatePage(intimationNumber);
		initiateLumenRequestWizardObj = initiateLumenRequestWizardInstance.get();
		initiateLumenRequestWizardObj.initView(resultObj, popup, "Initiate PED");
		
		VerticalLayout containerLayout = new VerticalLayout();
		containerLayout.addComponent(initiateLumenRequestWizardObj);
		popup.setContent(containerLayout);
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
	}
	
	@Override
	public void showPEDAlreadyAvailable(String pedAvailableMsg) {
		
		showErrorMessage(pedAvailableMsg);
		
		if(!pedAvailableMsg.toLowerCase().contains("icd")){
			initiatePedEndorsementObj.resetSuggestionCmb();
			
		}	
		
	}
	
	@Override
	public void resetPEDDetailsTable(ViewPEDTableDTO newInitiatePedDTO){

		initiatePedEndorsementObj.resetPEDDetailsTable(newInitiatePedDTO);
	}

}
