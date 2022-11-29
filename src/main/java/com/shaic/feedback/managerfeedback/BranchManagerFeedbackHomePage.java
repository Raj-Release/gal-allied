package com.shaic.feedback.managerfeedback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.hene.flexibleoptiongroup.FlexibleOptionGroup;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.ReferenceTable;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackPresenter;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackSearchDTO;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackViewImpl;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
public class BranchManagerFeedbackHomePage extends ViewComponent{
    
  private Button previousButton;
  private Button newfeedbackButton;
  private VerticalLayout verticalLayout;
  private HorizontalLayout btnLayout;
  private Panel mainPanel;
  
  private ComboBox branchCmb; 
  
  @Inject
  private FeedbackStatsTable feedbackStatsTableObj;
  
  @Inject 
  private Instance<ReviewStatsTable> reviewStasTable;
  
  private ReviewStatsTable reviewStasTableObj;
  
  private BranchManagerFeedbackhomePageDto homePageDto;
  
  private HorizontalLayout tableLayout;
  
  @Inject
	private Instance<BranchManagerPreviousFeedbackViewImpl> branchManagerFeedbackUIInstance;
	
	private BranchManagerPreviousFeedbackViewImpl branchManagerFeedbackUIInstanceObj;
  
  @PostConstruct
	public void init() {

	}
  
  	public void initView(BranchManagerFeedbackhomePageDto homePageDto) {

  		this.homePageDto = homePageDto;
	  	mainPanel = new Panel();
	    verticalLayout = new VerticalLayout();
	    tableLayout = new HorizontalLayout();
	    tableLayout.setHeight("100%");

		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		
		FormLayout branchfLayout = new FormLayout();
		branchCmb = new ComboBox("Branch Name");
		branchfLayout.addComponents(branchCmb);
		branchfLayout.setMargin(true);
		
		newfeedbackButton = new Button("New FeedBack");
		previousButton = new Button("Previous FeedBack");
		previousButton.setStyleName("backgroundColour");
		newfeedbackButton.setStyleName("backgroundColour");
		btnLayout = new HorizontalLayout(newfeedbackButton,previousButton);
		btnLayout.setMargin(false);
		btnLayout.setSpacing(true);
		
		mainPanel.setCaption("<b style = 'color: blue;'><H2> Welcome "+user.getEmpName()+",</H2>");
		mainPanel.setCaptionAsHtml(true);
		mainPanel.setSizeFull();		 
		verticalLayout.addComponents(mainPanel, branchfLayout, tableLayout);
		
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(false);
		verticalLayout.setStyleName("g-search-panel");
		verticalLayout.setSizeFull();
		
//		mainPanel.setContent(verticalLayout);

		
//		buildTableDetails(homePageDto);
		
		verticalLayout.addComponent(btnLayout);
		verticalLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		
		setCompositionRoot(verticalLayout);
		setSizeFull();
		
		branchChangeListener();
		addListener();
	}
  	
  	public void buildTableDetails(BranchManagerFeedbackhomePageDto homePageDto){

  		this.homePageDto = homePageDto;

  		tableLayout.removeAllComponents();
  		feedbackStatsTableObj.init("", false, false);
  		feedbackStatsTableObj.setTableList(homePageDto.getFeedbackStatsList());
		feedbackStatsTableObj.calculateTotal();
		tableLayout.addComponent(feedbackStatsTableObj.getTable());
		
		reviewStasTableObj = reviewStasTable.get();
		reviewStasTableObj.initView();
		if(homePageDto.getReviewStatsList() != null && !homePageDto.getReviewStatsList().isEmpty()){
			
			reviewStasTableObj.addBeanToList(homePageDto.getReviewStatsList().get(0));
		}
		tableLayout.addComponent(reviewStasTableObj);
		
		tableLayout.setSpacing(true);
		tableLayout.setMargin(true);
  	}
 
  public void addListener(){
	
  		newfeedbackButton.addClickListener(new ClickListener() {

  			private static final long serialVersionUID = 1L;

  				@Override
  				public void buttonClick(ClickEvent event) {
  					fireViewEvent(MenuItemBean.MANAGER_FEEDBACK_FORM,null);
  					
  				}
  		});
  		
  		previousButton.addClickListener(new ClickListener() {

  			private static final long serialVersionUID = 1L;

  				@Override
  				public void buttonClick(ClickEvent event) {


					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("");
					popup.setWidth("85%");
					popup.setHeight("100%");
					branchManagerFeedbackUIInstanceObj = branchManagerFeedbackUIInstance.get();
					branchManagerFeedbackUIInstanceObj.initView();
					BranchManagerPreviousFeedbackSearchDTO previousFeedbackDTO = new BranchManagerPreviousFeedbackSearchDTO();
					fireViewEvent(BranchManagerPreviousFeedbackPresenter.LOAD_PREVIOUS_FEED_BACK_SEARCH_COMPONENTS_VALUE,previousFeedbackDTO);
					popup.setCaption("Previous Feedback");
					popup.setContent(branchManagerFeedbackUIInstanceObj);
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
  		});
  		
  	}

	public void setDropDownValues(BeanItemContainer<SelectValue> branchNameContainer) 
	{
	
		branchCmb.setContainerDataSource(branchNameContainer);
		branchCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		branchCmb.setItemCaptionPropertyId("value");
		
		//branchCmb.setValue(homePageDto.getHomeBranch());
		
		if(branchNameContainer != null && branchNameContainer.getItemIds() != null && ! branchNameContainer.getItemIds().isEmpty()){
			if(this.homePageDto.getHomeBranch() != null){
				branchCmb.setValue(this.homePageDto.getHomeBranch());
			}else{
				branchCmb.setValue(branchNameContainer.getItemIds().get(0));
			}
////			branchCmb.setEnabled(false);
		}
		
	}
	
	public void branchChangeListener(){
		branchCmb
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
				
				if(null != value){
				
					homePageDto.setHomeBranch(value);
					fireViewEvent(ManagerFeedBackUIPresenter.GET_HOME_PAGE_STATS, homePageDto, user.getUserName());
				}
			}
		});
	}

}
