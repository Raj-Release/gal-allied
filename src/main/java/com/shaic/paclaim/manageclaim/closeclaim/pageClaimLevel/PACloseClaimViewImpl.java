package com.shaic.paclaim.manageclaim.closeclaim.pageClaimLevel;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PACloseClaimViewImpl extends AbstractMVPView implements PACloseClaimView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@Inject
	private Instance<PACloseClaimPageUI> closeClaimInstance;
	
	private PACloseClaimPageUI closeClaimObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Toolbar toolbar;
	
	private Button btnViewBalanceSI;
	
	
	private Button btnViewClaimStatus;
	
	private PACloseClaimPageDTO bean;
	
	@PostConstruct
	public void init(){
		
	}
	
	public void init(PACloseClaimPageDTO bean){
		this.bean = bean;
		
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
//		intimationDetailsCarousel.init(preauthDTO.getNewIntimationDTO(),
//				preauthDTO.getClaimDTO(), "Process Pre-authorization");
		intimationDetailsCarousel.init(this.bean.getNewIntimationDto(),this.bean.getClaimDto(), "Close Claim(Search Based)",bean.getDiagnosis());
		
		viewDetails.initView(this.bean.getNewIntimationDto().getIntimationId(), ViewLevels.CLOSE_CLAIM, false,"Close Claim(Search Based)");
		
		btnViewBalanceSI = new Button("Balance SI");
		btnViewClaimStatus = new Button("Claim Status");
		
		closeClaimObj = closeClaimInstance.get();
		
		closeClaimObj.init(this.bean);
		
		HorizontalLayout horLayout = new HorizontalLayout(btnViewBalanceSI, btnViewClaimStatus,viewDetails);
		horLayout.setWidth("100%");
		horLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel,horLayout,closeClaimObj);
		mainVertical.setSpacing(true);
		
		
		setCompositionRoot(mainVertical);
		
		addListener();
		
	}
	
public void addListener(){
		
	btnViewBalanceSI.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				viewDetails.getViewBalanceSumInsured(bean.getIntimationNumber());

			}
		});
		
	btnViewClaimStatus.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				viewDetails.viewSearchClaimStatus(bean.getIntimationNumber(),0l);
				
				
			}
		});
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUploadDocumentsDTO(
			PAUploadDocumentCloseClaimDTO uploadDocumentForCloseClaim) {
	
		closeClaimObj.setUploadDocumentTable(uploadDocumentForCloseClaim);
	}


	@Override
	public void result() {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Intimation Closed Successfully</b>",
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
				toolbar.countTool();
				fireViewEvent(MenuItemBean.PA_CLOSE_CLAIM_CLAIM_LEVEL, null);

			}
		});
	}
	
	@Override
	public void alreadyClosed(String message) {
		Label successLabel = new Label(
				"<b style = 'color: green;'> Claim Already Closed</b>",
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
				fireViewEvent(MenuItemBean.PA_CLOSE_CLAIM_CLAIM_LEVEL, null);

			}
		});
	}

}
