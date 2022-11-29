package com.shaic.claim.fvrgrading.page;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.fvrgrading.search.SearchFvrReportGradingTableDto;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FvrReportGradingPageViewImpl extends AbstractMVPView implements FvrReportGradingPageView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<FvrReportGradingPage> fvrGradingPageInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private FvrReportGradingPageDto bean;
	
	private FvrReportGradingPage fvrGradingPage;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private NewIntimationDto intimationDto;
	
	private SearchFvrReportGradingTableDto searchFormDto;
	
	private VerticalLayout mainPanel;
	
	
	@PostConstruct
	public void initView(){
		
	}
	
	public void init(FvrReportGradingPageDto bean,NewIntimationDto intimationDto,SearchFvrReportGradingTableDto searchFormDto)
	{
		this.bean=bean;
		this.searchFormDto=searchFormDto;
		
		this.intimationDto=intimationDto;
		mainPanel = new VerticalLayout();
		//mainPanel.setSplitPosition(24, Unit.PERCENTAGE);
		setHeight("100%");
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.intimationDto,"Field Visit Report Grading");
		//intimationDetailsCarousel.setHeight("200px");
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.FVR_GRADING_PROCESS, false,"Field Visit Report Grading");
		
		fvrGradingPage=fvrGradingPageInstance.get();
		fvrGradingPage.initView(this.bean,this.searchFormDto);
		
		VerticalLayout wholeVertical=new VerticalLayout(fvrGradingPage);
		//wholeVertical.setComponentAlignment(firstVertical, Alignment.TOP_RIGHT);
		
//		VerticalLayout spaceVeritcLayout=new VerticalLayout());
//		spaceVeritcLayout.setHeight("100px");
		
		mainPanel.addComponent(intimationDetailsCarousel);
		//mainPanel.addComponent(spaceVeritcLayout);
		mainPanel.addComponent(wholeVertical);
		//mainPanel.setHeight("670px");
		setCompositionRoot(mainPanel);
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
		
		Button homeButton = new Button("Field Visit Report Grading Home");
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
			
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.FVR_GRADING, true);
				
			}
		});
	}

}
