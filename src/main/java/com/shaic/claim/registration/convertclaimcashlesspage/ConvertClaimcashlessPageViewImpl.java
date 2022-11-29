package com.shaic.claim.registration.convertclaimcashlesspage;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.registration.convertclaimcashless.SearchConverClaimCashlessTableDTO;
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
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ConvertClaimcashlessPageViewImpl extends AbstractMVPView implements ConvertClaimCashlessPageView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<ConvertClaimCashlessPage> ConvertClaimPageInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private ConvertClaimDTO bean;
	
	private ConvertClaimCashlessPage convertClaimPage;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private NewIntimationDto intimationDto;
	
	private SearchConverClaimCashlessTableDTO searchFormDto;
	
	private VerticalLayout mainPanel;
	
	private PreauthDTO preauthDTO;
	
	
	
	@PostConstruct
	public void initView(){
		
	}
	
	public void init(ConvertClaimDTO bean,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto,SearchConverClaimCashlessTableDTO searchFormDto)
	{
		this.bean=bean;
		this.searchFormDto=searchFormDto;
		
		preauthDTO = new PreauthDTO();
		
		this.intimationDto=intimationDto;
		mainPanel = new VerticalLayout();
		setHeight("100%");
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.intimationDto,"Convert Claim to Cashless",bean.getClaimDTO());
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Convert Claim to Cashless");
		
		convertClaimPage=ConvertClaimPageInstance.get();
		convertClaimPage.initView(this.bean,selectValueContainer,this.searchFormDto);
		
		VerticalLayout firstVertical=new VerticalLayout(viewDetails);
		firstVertical.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		
		VerticalLayout wholeVertical=new VerticalLayout(firstVertical,convertClaimPage);
		
		
		mainPanel.addComponent(intimationDetailsCarousel);
		mainPanel.addComponent(wholeVertical);
		setCompositionRoot(mainPanel);
	}
	
	
	
	

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void result() {

		
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
				fireViewEvent(MenuItemBean.CONVERT_CLAIM_CASHLESS, true);
				
			}
		});
	}

}
