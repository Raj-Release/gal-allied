package com.shaic.claim.scoring.ppcoding;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.scoring.HospitalScoringDetailView;
import com.shaic.claim.scoring.HospitalScoringService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;

public class ScoringAndPPDetailView extends ViewComponent {
	
	@Inject
	private HospitalScoringDetailView hospitalScoringView;
		
	@Inject
	private PPCodingDetailView ppCodingView;
	
	private VerticalLayout mainLayout;
	
	private HorizontalLayout buttonHorlayout;
	
	private String intimationNumber;
	
	private String screenName;
	
	private PreauthDTO dtoBean;
	
	private Button parentScoringButton;
	
	private Button cancelButton;
	
	private Button submitButton;
	
	private Window popupWindow;
	
	private Boolean scoringcompleted = false;
	
	private Boolean ppcompleted = false;
	
	private Boolean isoldppscoreview = false;
	
	@EJB
	private HospitalScoringService scoringService;
	
	@EJB
	private PPCodingService codingService;

	public Window getPopupWindow() {
		return popupWindow;
	}

	public void setPopupWindow(Window popupWindow) {
		this.popupWindow = popupWindow;
	}

	public void setParentScoringButton(Button parentScoringButton) {
		this.parentScoringButton = parentScoringButton;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setIntimationNumber(String intimationNumber) {
		this.intimationNumber = intimationNumber;
	}

	public void setDtoBean(PreauthDTO dtoBean) {
		this.dtoBean = dtoBean;
	}
	
	public Button getParentScoringButton() {
		return parentScoringButton;
	}

	public PreauthDTO getDtoBean() {
		return dtoBean;
	}
	
	public Boolean getIsoldppscoreview() {
		return isoldppscoreview;
	}

	public void setIsoldppscoreview(Boolean isoldppscoreview) {
		this.isoldppscoreview = isoldppscoreview;
	}

	public void init(boolean isButtonVisible){

		mainLayout = new VerticalLayout(buildscoringTabs());		
		mainLayout.setSpacing(true);
		setCompositionRoot(mainLayout);
	}
	
	private TabSheet buildscoringTabs() {
		TabSheet scoringPPTab = new TabSheet();
		scoringPPTab.setSizeFull();
		scoringPPTab.setStyleName(ValoTheme.TABSHEET_EQUAL_WIDTH_TABS);
		
		TabSheet scoringTab = new TabSheet();
		hospitalScoringView.setIsoldscoringview(getIsoldppscoreview());
		hospitalScoringView.init(intimationNumber, false);
		hospitalScoringView.setScreenName(screenName);	
		scoringTab.setHeight("100.0%");
		scoringTab.addComponent(hospitalScoringView);
		scoringPPTab.addTab(scoringTab, "Hospital Scoring", null);
		
		TabSheet ppTab = new TabSheet();
		ppCodingView.setIsoldppscoreview(getIsoldppscoreview());
		ppCodingView.setScreenName(screenName);
		ppCodingView.init(intimationNumber, false);
		ppTab.setHeight("100.0%");
		ppTab.addComponent(ppCodingView);
		scoringPPTab.addTab(ppTab, "PP coding", null);
		
		return scoringPPTab;
	}
	
}
