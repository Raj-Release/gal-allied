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
public class ManagerFeedBackUI extends ViewComponent{
    private TextField dayCount;
    private TextField overallCount;
    private Button previousButton;
    private Button homePageButton;
    
    private TextArea area;
    private ManagerFeedBackPolicyTableDto feedbackDTO = new ManagerFeedBackPolicyTableDto();

 
  
	private static final String POLICY = "Claims-Retail";
	private static final String PROPOSAL = "MER";
	private static final String CLAIM = "Claims-GMC";
	
	private static final String VERY_GOOD = "VeryGood";
	private static final String GOOD = "Good";
	private static final String SATISFACTORY = "Satisfactory";
	private static final String AVERAGE = "Average";
	private static final String BELOW_AVERAGE = "BelowAverage";
	
  
	@Inject
	private Instance<ManagerFeedBackPolicyTable> managaerFeedbackPolicyTableInstance;
	
	private ManagerFeedBackPolicyTable managaerFeedbackPolicyTableInstanceObj;
	@Inject
	private Instance<ManagerFeedBackProposalTable > managaerFeedbackProposalTableInstance;
	
	private ManagerFeedBackProposalTable  managaerFeedbackProposalTableInstanceObj;
	
	@Inject
	private Instance<ManagerFeedBackClaimTable > managaerFeedbackClaimTableInstance;
	
	private ManagerFeedBackClaimTable  managaerFeedbackClaimTableInstanceObj;
	
	@Inject
	private Instance<BranchManagerPreviousFeedbackViewImpl> branchManagerFeedbackUIInstance;
	
	private BranchManagerPreviousFeedbackViewImpl branchManagerFeedbackUIInstanceObj;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
  
//	public void setPreviousClaimTable(ManagerFeedBackPolicyTable previousClaimTable){
//		this.managaerFeedbackPolicyTableInstanceObj = previousClaimTable;
//	}
  
  private VerticalLayout verticalLayout;
  private HorizontalLayout horizontalLayout3;
  private VerticalLayout image;
  private Panel mainPanel;
  
  private OptionGroup vGoodOptionGrp;
  private OptionGroup goodOptionGrp;
  private OptionGroup satisfactoryOptionGrp;
  private OptionGroup averageOptionGrp;
  private OptionGroup bAverageOptionGrp;
  
  protected FlexibleOptionGroup flexibleOptionGroup;  
  private OptionGroup viewType;
  private OptionGroup feedbackOptnGrp;
  private ComboBox branch; 
  
  @PostConstruct
	public void init() {

	}
  
  public void initView() {
	  
	    verticalLayout = new VerticalLayout();
	    horizontalLayout3 = new HorizontalLayout();
	    horizontalLayout3.setWidth("100%");
		mainPanel = new Panel();
//		final VerticalLayout verticalLayout3 =new VerticalLayout();
		
		ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
		
	//	Label label=new Label("<H1> Dear "+user.getEmpName()+", Please provide your feedback on Processing</H1>",ContentMode.HTML);
		
		HorizontalLayout horizontalLayout=new HorizontalLayout();
		HorizontalLayout optionLayout = buildViewType();
		optionLayout.setWidth("100%");

		Label label1=new Label("Feedback For");
		FormLayout lab=new FormLayout(label1);	
		lab.setSpacing(false);
		lab.setMargin(false);
		FormLayout lab1=new FormLayout();
		horizontalLayout.addComponents(lab,lab1,optionLayout);
//		horizontalLayout.setComponentAlignment(h1, Alignment.MIDDLE_RIGHT);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(false);
		
		HorizontalLayout hLayout = new HorizontalLayout();
		FormLayout fLayout = new FormLayout();
		branch = new ComboBox("Branch Name");
		fLayout.addComponents(branch);
		fLayout.setMargin(false);
		hLayout.addComponents(horizontalLayout);
		
		DBCalculationService dbService = new DBCalculationService();
		Long dailycount = 0L;
		Long overallcount = 0L;
		Map<String, Object> feedBackCount = dbService.getFeedBackCountDetails(user.getUserName(),null);
		if(feedBackCount != null){
			if(feedBackCount.containsKey("dailyCount")){
				dailycount = (Long) feedBackCount.get("dailyCount");
			}
			if(feedBackCount.containsKey("overAllcount")){
				overallcount = (Long) feedBackCount.get("overAllcount");
			}
		}
		
		HorizontalLayout horizontalLayout2=new HorizontalLayout();
		dayCount=new TextField("Day Count:");
		dayCount.setValue(String.valueOf(dailycount));
		dayCount.setEnabled(false);
		dayCount.setWidth("60px");
	    dayCount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		 
		overallCount=new TextField("Overall Count:");
		overallCount.setValue(String.valueOf(overallcount));
		overallCount.setEnabled(false);
		overallCount.setWidth("60px");
		overallCount.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		 
		FormLayout formLayoutLeft = new FormLayout(dayCount);
		FormLayout formLayoutLeft1 = new FormLayout(overallCount);
		formLayoutLeft.setSpacing(true);
		formLayoutLeft.setMargin(false);
		formLayoutLeft.setWidth("40%");
		formLayoutLeft1.setSpacing(true);
		formLayoutLeft1.setMargin(false);
		formLayoutLeft1.setWidth("40%");
		homePageButton = new Button("Home");
	//	homePageButton.setIcon(new ThemeResource("images/homeIcon.png"));
		previousButton = new Button("Previous FeedBack");
		previousButton.setStyleName("backgroundColour");
		homePageButton.setStyleName("backgroundColour");
		HorizontalLayout formLayoutLeft2 = new HorizontalLayout(homePageButton,previousButton);
		formLayoutLeft2.setMargin(false);
		formLayoutLeft2.setSpacing(true);
		 
		horizontalLayout2.addComponents(fLayout,formLayoutLeft,formLayoutLeft1,formLayoutLeft2);
		horizontalLayout2.setComponentAlignment(formLayoutLeft2, Alignment.MIDDLE_RIGHT);
		horizontalLayout2.setSpacing(true);
		horizontalLayout2.setMargin(false);
		verticalLayout.addComponents(horizontalLayout2,hLayout,horizontalLayout3);
		//addClickListener();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(false);
		verticalLayout.setStyleName("g-search-panel");
		mainPanel.setContent(verticalLayout);
		mainPanel.setCaption("<b style = 'color: blue;'><H2> Dear "+user.getEmpName()+", Please provide your feedback on Processing</H2>");
		mainPanel.setCaptionAsHtml(true);
		
		 mandatoryFields.add(branch);
		 showOrHideValidation(false);
		setCompositionRoot(mainPanel);
		branchListener();
		//image =buildImageViewType();
		addListener();
	}




private VerticalLayout buildImageViewType() {
 VerticalLayout optionimageLayout = getOptionGroupForImage(null);
 return optionimageLayout;
}

public HorizontalLayout buildViewType(){
		viewType = getOptionGroup();		
		viewType.addValueChangeListener(new Property.ValueChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				if (viewType.getValue() != null) {
					horizontalLayout3.removeAllComponents();
					if (viewType.getValue().equals(POLICY)) {
						
						managerFeedbackpolicy();
						
					} else if (viewType.getValue().equals(PROPOSAL)) {
						
						managerFeedbackProposl();
						
					} else if (viewType.getValue().equals(CLAIM)) {
						
						managerFeedbackClaim();
					}
				}
			
			}

		});

		HorizontalLayout optionLayout = new HorizontalLayout();
		optionLayout.addComponent(viewType);
	return optionLayout;
}

protected VerticalLayout managerFeedbackpolicy() {
	// TODO Auto-generated method stub
	 VerticalLayout checkboxPolicy=new VerticalLayout();
	    HorizontalLayout hpolicy2=new HorizontalLayout();
	    Label lpolicy=new Label("Feedback Remarks(Overall)");
	    lpolicy.setWidth("100px");
	    area =new TextArea();
	    area.setSizeFull();
	    area.setMaxLength(4000);
	    area.setData(feedbackDTO);
	    addDetailPopupForFeedBackOverallRemarks(area,null);
	    FormLayout fpol1=new FormLayout(lpolicy);
	    FormLayout fpol2=new FormLayout(area);
	    fpol2.setWidth("300px");
	    
	    image =buildImageViewType();
	    	    
	    hpolicy2.addComponents(fpol1,fpol2,image);
	    hpolicy2.setMargin(false);
	    hpolicy2.setSpacing(true);
	    HorizontalLayout hpolicy3=new HorizontalLayout();
	    hpolicy3 = buildButtonLayout();
	    HorizontalLayout hpolicy4=new HorizontalLayout();
	    
	    managaerFeedbackPolicyTableInstanceObj = managaerFeedbackPolicyTableInstance.get();
		managaerFeedbackPolicyTableInstanceObj.init("",true, true);
		Map<String, Object> draftrefData = new HashMap<String, Object>();
		managaerFeedbackPolicyTableInstanceObj.setReference(draftrefData);
		
		List<ManagerFeedBackPolicyTableDto> defaultList = new ArrayList<ManagerFeedBackPolicyTableDto>();
		
		for(int k = 0; k<1; k++){			
			ManagerFeedBackPolicyTableDto draftDetailDto = new ManagerFeedBackPolicyTableDto();		
			draftDetailDto.setSerialNumber((k+1));
			draftDetailDto.setProcessType("D");
			defaultList.add(draftDetailDto);				
		}	
		managaerFeedbackPolicyTableInstanceObj.setTableList(defaultList);
		
	    checkboxPolicy.addComponents(managaerFeedbackPolicyTableInstanceObj,hpolicy4,hpolicy3);
	    checkboxPolicy.setSizeFull();
	    checkboxPolicy.setComponentAlignment(hpolicy3, Alignment.MIDDLE_CENTER);
	    mandatoryFields.add(area);
	    showOrHideValidation(false);
	    horizontalLayout3.addComponent(checkboxPolicy);
		return checkboxPolicy;
	
}

protected VerticalLayout managerFeedbackClaim() {
    
	 VerticalLayout checkboxPolicy=new VerticalLayout();
//	    HorizontalLayout hpolicy1=new HorizontalLayout();
	    HorizontalLayout hpolicy2=new HorizontalLayout();
	    HorizontalLayout hpolicy3=new HorizontalLayout();
	    Label lpolicy=new Label("Feedback Remarks(Overall)");
	    lpolicy.setWidth("100px");
	    area =new TextArea();
	    area.setSizeFull();
	    area.setMaxLength(4000);
	    area.setData(feedbackDTO);
	    addDetailPopupForFeedBackOverallRemarks(area,null);
	    FormLayout fpol1=new FormLayout(lpolicy);
	    FormLayout fpol2=new FormLayout(area);
	    fpol2.setWidth("300px");
	    
	    image =buildImageViewType();
	    
	    VerticalLayout memoLayout = new VerticalLayout();
	   // memoLayout.addComponent(newImage());
	    memoLayout.addComponent(image);
	    hpolicy2.addComponents(fpol1,fpol2,memoLayout);
	    hpolicy2.setMargin(false);
	    hpolicy2.setSpacing(true);
	    hpolicy3 = buildButtonLayout();
		HorizontalLayout hpolicy4=new HorizontalLayout();
	    
	    managaerFeedbackClaimTableInstanceObj = managaerFeedbackClaimTableInstance.get();
	    managaerFeedbackClaimTableInstanceObj.init("",true, true);
		Map<String, Object> draftrefData = new HashMap<String, Object>();
		managaerFeedbackClaimTableInstanceObj.setReference(draftrefData);
		
		List<ManagerFeedBackPolicyTableDto> defaultList = new ArrayList<ManagerFeedBackPolicyTableDto>();
		
		for(int k = 0; k<1; k++){			
			ManagerFeedBackPolicyTableDto draftDetailDto = new ManagerFeedBackPolicyTableDto();		
			draftDetailDto.setSerialNumber((k+1));
			draftDetailDto.setProcessType("D");
			defaultList.add(draftDetailDto);				
		}	
		managaerFeedbackClaimTableInstanceObj.setTableList(defaultList);
		
	    checkboxPolicy.addComponents(managaerFeedbackClaimTableInstanceObj,hpolicy4,hpolicy3);
	    checkboxPolicy.setComponentAlignment(hpolicy3, Alignment.MIDDLE_CENTER);
	    checkboxPolicy.setSizeFull();
	    mandatoryFields.add(area);
	    showOrHideValidation(false);
	    horizontalLayout3.addComponent(checkboxPolicy);
		return checkboxPolicy;
}

protected VerticalLayout managerFeedbackProposl() {
	// TODO Auto-generated method stub
	 VerticalLayout checkboxPolicy=new VerticalLayout();
//	    HorizontalLayout hpolicy1=new HorizontalLayout();
	    HorizontalLayout hpolicy2=new HorizontalLayout();
	    HorizontalLayout hpolicy3=new HorizontalLayout();
	    Label lpolicy=new Label("Feedback Remarks(Overall)");
	    lpolicy.setWidth("100px");
	    area =new TextArea();
	    area.setSizeFull();
	    area.setMaxLength(4000);
	    area.setData(feedbackDTO);
	    addDetailPopupForFeedBackOverallRemarks(area,null);
	    FormLayout fpol1=new FormLayout(lpolicy);
	    FormLayout fpol2=new FormLayout(area);
	    fpol2.setWidth("300px");
	    
	    image =buildImageViewType();
	    
	    hpolicy2.addComponents(fpol1,fpol2,image);
	    hpolicy2.setMargin(false);
	    hpolicy2.setSpacing(true);
	    
	    managaerFeedbackProposalTableInstanceObj = managaerFeedbackProposalTableInstance.get();
	    managaerFeedbackProposalTableInstanceObj.init("",true, true);
	    Map<String, Object> draftrefData = new HashMap<String, Object>();
	    managaerFeedbackProposalTableInstanceObj.setReference(draftrefData);
	    List<ManagerFeedBackPolicyTableDto> defaultList = new ArrayList<ManagerFeedBackPolicyTableDto>();
		
		for(int k = 0; k<1; k++){			
			ManagerFeedBackPolicyTableDto draftDetailDto = new ManagerFeedBackPolicyTableDto();		
			draftDetailDto.setSerialNumber((k+1));
			draftDetailDto.setProcessType("D");
			defaultList.add(draftDetailDto);				
		}	
		managaerFeedbackProposalTableInstanceObj.setTableList(defaultList);
	    
	    hpolicy3 = buildButtonLayout();
	    HorizontalLayout hpolicy4=new HorizontalLayout();
	   
	    checkboxPolicy.addComponents(managaerFeedbackProposalTableInstanceObj,hpolicy4,hpolicy3);
	    checkboxPolicy.setComponentAlignment(hpolicy3, Alignment.MIDDLE_CENTER);
	    checkboxPolicy.setSizeFull();
	    mandatoryFields.add(area);
	    showOrHideValidation(false);
	    horizontalLayout3.addComponent(checkboxPolicy);
		return checkboxPolicy;
	
}

private OptionGroup getOptionGroup() {
	OptionGroup viewType = new OptionGroup();
	viewType.addItem(PROPOSAL);
	viewType.addItem(POLICY);
	viewType.addItem(CLAIM);
	viewType.setStyleName("inlineStyle");
	return viewType;
}

private VerticalLayout getOptionGroupForImage(Object object) {
	// TODO Auto-generated method stub

	Image vGood = new Image("", new ThemeResource("images/veryGood.png"));
	Image good = new Image ("", new ThemeResource("images/good.png"));
	Image satisfactory = new Image ("",new ThemeResource("images/satisfactory.png"));
	Image average = new Image ("", new ThemeResource("images/Average.png"));
	Image bAverage = new Image ("", new ThemeResource("images/belowAvg.png"));

	feedbackOptnGrp = new OptionGroup();
	feedbackOptnGrp.addItem(VERY_GOOD);
	feedbackOptnGrp.addItem(GOOD);
	feedbackOptnGrp.addItem(SATISFACTORY);
	feedbackOptnGrp.addItem(AVERAGE);
	feedbackOptnGrp.addItem(BELOW_AVERAGE);
	feedbackOptnGrp.setStyleName("inlineStyle");	
	TextField dummy = new TextField();
	dummy.setWidth("25px");
	dummy.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	dummy.setEnabled(false);
	TextField dummy1 = new TextField();
	dummy1.setWidth("25px");
	dummy1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	dummy1.setEnabled(false);
	TextField dummy2 = new TextField();
	dummy2.setWidth("20px");
	dummy2.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	dummy2.setEnabled(false);
	TextField dummy3 = new TextField();
	dummy3.setWidth("25px");
	dummy3.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	dummy3.setEnabled(false);
	TextField dummy4 = new TextField();
	dummy4.setWidth("9px");
	dummy4.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	dummy4.setEnabled(false);
	HorizontalLayout iconLayout = new HorizontalLayout();
	iconLayout.addComponents(/*dummy4,*/vGood,dummy,good,dummy1,satisfactory,dummy2,average,dummy3,bAverage);
	iconLayout.setSpacing(true);
	iconLayout.setWidth("201%");
	
    HorizontalLayout optionsLayout = new HorizontalLayout(feedbackOptnGrp);
	optionsLayout.setSpacing(true);
	VerticalLayout vLayout = new VerticalLayout();
	vLayout.addComponents(iconLayout,optionsLayout);
	
	return vLayout;
}

protected boolean validatePage(Boolean hasError) {

	showOrHideValidation(false);
	StringBuffer eMsg = new StringBuffer();	
	

/*	if(null!= area && (null== area.getValue() || ("").equalsIgnoreCase(area.getValue()))){
		hasError = Boolean.TRUE;
		eMsg.append("Please Enter the Feedback Remarks(Overall).</br>");
	}
	if((null != feedbackOptnGrp  && null == feedbackOptnGrp.getValue()))
	{
		hasError = Boolean.TRUE;
		eMsg.append("Please choose any feedback.</br>");
	}*/
	if(null != branch && null == branch.getValue())
	{
		hasError = Boolean.TRUE;
		eMsg.append("Please select Branch.</br>");
	}
	if(viewType != null && viewType.getValue() != null){
		List<ManagerFeedBackPolicyTableDto> tableList = null;
		if(viewType.getValue().equals(POLICY)){
			 tableList = managaerFeedbackPolicyTableInstanceObj.getValues();			
			 if(null != tableList && !tableList.isEmpty()){
					for (ManagerFeedBackPolicyTableDto managerFeedBackPolicyTableDto : tableList) {
							if(null == managerFeedBackPolicyTableDto.getPolicyNumber() || ("").equals(managerFeedBackPolicyTableDto.getPolicyNumber())){
								hasError = Boolean.TRUE;
								eMsg.append("Please enter the Intimation number. </br>");
								//break;
							}
							if(null == managerFeedBackPolicyTableDto.getRemarks() || ("").equals(managerFeedBackPolicyTableDto.getRemarks())){
								hasError = Boolean.TRUE;
								eMsg.append("Please enter the Remarks. </br>");
							}
							if(null == managerFeedBackPolicyTableDto.getFeedBackRating()){
								hasError = Boolean.TRUE;
								eMsg.append("Please select feedback rating </br>");
							}
							if(null == managerFeedBackPolicyTableDto.getFbCategory()){
								hasError = Boolean.TRUE;
								eMsg.append("Please select Category </br>");
							}
					}
				}
			 else
			 {
				 hasError = Boolean.TRUE;
				 eMsg.append("Please enter detail feedback. </br>");
			 }
		}
		else if((viewType.getValue().equals(PROPOSAL))){
			tableList = managaerFeedbackProposalTableInstanceObj.getValues();
			if(null != tableList && !tableList.isEmpty()){
				for (ManagerFeedBackPolicyTableDto managerFeedBackPolicyTableDto : tableList) {
					if(null == managerFeedBackPolicyTableDto.getProposalNO() || ("").equals(managerFeedBackPolicyTableDto.getProposalNO())){
						hasError = Boolean.TRUE;
						eMsg.append("Please enter the MER number. </br>");
					}
					if(null == managerFeedBackPolicyTableDto.getRemarks() || ("").equals(managerFeedBackPolicyTableDto.getRemarks())){
						hasError = Boolean.TRUE;
						eMsg.append("Please enter the Remarks. </br>");
					}
					if(null == managerFeedBackPolicyTableDto.getFeedBackRating()){
						hasError = Boolean.TRUE;
						eMsg.append("Please select feedback rating </br>");
					}
					if(null == managerFeedBackPolicyTableDto.getFbCategory()){
						hasError = Boolean.TRUE;
						eMsg.append("Please select Category </br>");
					}
				}
			}			
		}
		else if((viewType.getValue().equals(CLAIM))){
			tableList = managaerFeedbackClaimTableInstanceObj.getValues();
			if(null != tableList && !tableList.isEmpty()){
				for (ManagerFeedBackPolicyTableDto managerFeedBackPolicyTableDto : tableList) {
					if(null == managerFeedBackPolicyTableDto.getIntimationNO() || ("").equals(managerFeedBackPolicyTableDto.getIntimationNO())){
						hasError = Boolean.TRUE;
						eMsg.append("Please enter the Intimation number. </br>");
					}
					if(null == managerFeedBackPolicyTableDto.getRemarks() || ("").equals(managerFeedBackPolicyTableDto.getRemarks())){
						hasError = Boolean.TRUE;
						eMsg.append("Please enter the Remarks. </br>");
					}
					if(null == managerFeedBackPolicyTableDto.getFeedBackRating()){
						hasError = Boolean.TRUE;
						eMsg.append("Please select feedback rating </br>");
					}
					if(null == managerFeedBackPolicyTableDto.getFbCategory()){
						hasError = Boolean.TRUE;
						eMsg.append("Please select Category </br>");
					}
				}
			}
		
		}
		
		
	}
	
	if (hasError) {
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

protected void showOrHideValidation(Boolean isVisible) {
	for (Component component : mandatoryFields) {
		AbstractField<?> field = (AbstractField<?>) component;
		field.setRequired(!isVisible);
		//field.setValidationVisible(isVisible);
	}
}

private FocusListener addFocusListenerForVerygood(){
	
	FocusListener focus = new FocusListener() {
		
		@Override
		public void focus(FocusEvent event) {
			// TODO Auto-generated method stub
			feedbackDTO.setClickType("VeryGood");
			vGoodOptionGrp.removeFocusListener(this);
		}
	};
	return focus;
}

private FocusListener addFocusListenerForgood(){
	
	FocusListener focus = new FocusListener() {
		
		@Override
		public void focus(FocusEvent event) {
			// TODO Auto-generated method stub
			feedbackDTO.setClickType("Good");
			goodOptionGrp.removeFocusListener(this);
			
			
		}
	};
	return focus;
}

public HorizontalLayout buildButtonLayout(){
	
	  Button submit=new Button("Submit");
	    Button cancel=new Button("Cancel");
	  
	    //Vaadin8-setImmediate() submit.setImmediate(true);
	    submit.setWidth("-1px");
	    submit.setTabIndex(12);
	    submit.setHeight("-1px");
	    submit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//	    submit.setDisableOnClick(true);
	    submit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {

				
				Boolean hasError=false;
				if(area.getValue()==null){
					hasError=true;
				}
				if(validatePage(hasError)){
					//feedbackDTO = new ManagerFeedBackPolicyTableDto();
					String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					feedbackDTO.setUsername(userName);
					
					
					if(viewType != null && viewType.getValue() != null){
						if(viewType.getValue().equals(POLICY)){
							feedbackDTO.setFeedbackFor(ReferenceTable.FEEDBACK_TYPE_CLAIM_RETAIL);
							List<ManagerFeedBackPolicyTableDto> policyTable= managaerFeedbackPolicyTableInstanceObj.getValues();
							feedbackDTO.setTableDtoList(policyTable);
						}else if(viewType.getValue().equals(PROPOSAL)){
							feedbackDTO.setFeedbackFor(ReferenceTable.FEEDBACK_TYPE_MER);
							List<ManagerFeedBackPolicyTableDto> proposalTable= managaerFeedbackProposalTableInstanceObj.getValues();
							feedbackDTO.setTableDtoList(proposalTable);
						}else if(viewType.getValue().equals(CLAIM)){
							feedbackDTO.setFeedbackFor(ReferenceTable.FEEDBACK_TYPE_CLAIM_GMC);
							List<ManagerFeedBackPolicyTableDto> claimTable= managaerFeedbackClaimTableInstanceObj.getValues();
							feedbackDTO.setTableDtoList(claimTable);
						}
					}
					
					if(area != null && area.getValue() != null){
						feedbackDTO.setOverAllRmrks(area.getValue());
					}
					
					if(null != feedbackOptnGrp && null != feedbackOptnGrp.getValue()){
						if(feedbackOptnGrp.isSelected(VERY_GOOD)){
							feedbackDTO.setRatingfor(ReferenceTable.RATING_FOR_VERYGOOD);
						}
						else if(feedbackOptnGrp.isSelected(GOOD)){
							feedbackDTO.setRatingfor(ReferenceTable.RATING_FOR_GOOD);
						}
						else if(feedbackOptnGrp.isSelected(SATISFACTORY)){
							feedbackDTO.setRatingfor(ReferenceTable.RATING_FOR_SASTIFACTORY);
						}
						else if(feedbackOptnGrp.isSelected(AVERAGE)){
							feedbackDTO.setRatingfor(ReferenceTable.RATING_FOR_AVERAGE);
						}
						else if(feedbackOptnGrp.isSelected(BELOW_AVERAGE)){
							feedbackDTO.setRatingfor(ReferenceTable.RATING_FOR_BELOWAVERAGE);
						}
					}
				
				fireViewEvent(ManagerFeedBackUIPresenter.SUBMIT_FEEDBACK, feedbackDTO);
				}
			
				
				//horizontalLayout3.removeAllComponents();
				
				
			}
		});
	    
	  
		
	    cancel.addStyleName(ValoTheme.BUTTON_DANGER);
	    cancel.setWidth("-1px");
	    cancel.setHeight("-10px");
	    cancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure you want to cancel ?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											// Confirmed to continue
											viewType.clear();
											horizontalLayout3.removeAllComponents();
											}
										 else {
											// User did not confirm
											 dialog.close();
										}
									}
								});

				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			
				
				
			}
		});
	    HorizontalLayout hLayout = new HorizontalLayout();
	    hLayout.addComponents(submit,cancel);
	    hLayout.setSpacing(true);
	  /*  hLayout.setComponentAlignment(submit, Alignment.BOTTOM_RIGHT);
	    hLayout.setComponentAlignment(cancel, Alignment.BOTTOM_RIGHT);*/
		return hLayout;
}

public void buildSuccessLayout() {
	
	Label successLabel = new Label("<b style = 'color: green;'>Feedback Submitted Successfully</b>", ContentMode.HTML);
	Button homeButton = new Button("FeedBack Home");
	homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
	VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
	layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
	layout.setSpacing(true);
	layout.setMargin(true);
	layout.setStyleName("borderLayout");
	
	final ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCaption("");
	dialog.setClosable(false);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(getUI().getCurrent(), null, true);
	
	//getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	
	homeButton.addClickListener(new ClickListener() {
		private static final long serialVersionUID = 7396240433865727954L;

		@Override
		public void buttonClick(ClickEvent event) {
			dialog.close();
			fireViewEvent(MenuItemBean.MANAGER_FEEDBACK_FORM, null);
    		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
			SHAUtils.clearSessionObject(currentRequest);
		}
	});
}

public void setValuesForCompletedCase(Long dailycount,Long overallcount){
	dayCount.setValue(String.valueOf(dailycount));
	overallCount.setValue(String.valueOf(overallcount));
}

private void addDetailPopupForFeedBackOverallRemarks(TextArea txtFld, final  Listener listener) {
	  ShortcutListener enterShortCut = new ShortcutListener(
		        "Feedback Remarks(Overall)", ShortcutAction.KeyCode.F8, null) {
			
		      private static final long serialVersionUID = 1L;
		      @Override
		      public void handleAction(Object sender, Object target) {
		        ((ShortcutListener) listener).handleAction(sender, target);
		      }
		    };
		    handleShortcutForTriggerRemarks(txtFld, getShortCutListenerForTriggerRemarks(txtFld));
		    
		  }

public  void handleShortcutForTriggerRemarks(final TextArea textField, final ShortcutListener shortcutListener) {
	textField.addFocusListener(new FocusListener() {
		
		@Override
		public void focus(FocusEvent event) {
			textField.addShortcutListener(shortcutListener);
			
		}
	});
	textField.addBlurListener(new BlurListener() {

		@Override
		public void blur(BlurEvent event) {

			textField.removeShortcutListener(shortcutListener);

		}
	});
}


private ShortcutListener getShortCutListenerForTriggerRemarks(final TextArea txtFld)
{
	ShortcutListener listener =  new ShortcutListener("Description",KeyCodes.KEY_F8,null) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void handleAction(Object sender, Object target) {
			ManagerFeedBackPolicyTableDto  searchPedDto = (ManagerFeedBackPolicyTableDto) txtFld.getData();
			VerticalLayout vLayout =  new VerticalLayout();
			
			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			TextArea txtArea = new TextArea();
			txtArea.setMaxLength(4000);
			txtArea.setNullRepresentation("");
			txtArea.setValue(txtFld.getValue());
			
			txtArea.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					TextArea txt = (TextArea)event.getProperty();
					txtFld.setValue(txt.getValue());
				}
			});
			
			
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setRows(25);
			searchPedDto.setOverAllRmrks(txtArea.getValue());
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			final Window dialog = new Window();
			dialog.setCaption("Feedback Remarks(Overall)");
			dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
			dialog.setWidth("45%");
			dialog.setClosable(false);
			
			dialog.setContent(vLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.setDraggable(true);
			
			if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(250);
				dialog.setPositionY(100);
			}
			getUI().getCurrent().addWindow(dialog);
			
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}
public void addListener(){
	
	homePageButton.addClickListener(new ClickListener() {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			
			SelectValue selectedBranch = branch.getValue() != null ? (SelectValue) branch.getValue() : null;
			
			//if(selectedBranch != null){
			
				//fireViewEvent(MenuPresenter.SHOW_BRANCH_MANAGER_FEEDBACK_HOME_PAGE, branch.getContainerDataSource(), selectedBranch);
			//}
			/*else{
				showErrorMessage("Please Select Branch Name");
			}*/
			fireViewEvent(MenuPresenter.SHOW_BRANCH_MANAGER_FEEDBACK_HOME_PAGE,selectedBranch);
			
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

	public void setDropDownValues(BeanItemContainer<SelectValue> branchName) 
	{
	
		branch.setContainerDataSource(branchName);
		branch.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		branch.setItemCaptionPropertyId("value");
		
		if(branchName != null && branchName.getItemIds() != null && ! branchName.getItemIds().isEmpty() && branchName.getItemIds().size() == 1){
			branch.setValue(branchName.getItemIds().get(0));
			branch.setEnabled(false);
			
		}
		else{
			branch.setValue(branchName.getItemIds().get(0));
		}
	}
	
	public void branchListener(){
		branch
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				SelectValue value = (SelectValue) event.getProperty().getValue();
				ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
				if(null != value)
				{
					feedbackDTO.setBranch(null != value.getId()? value.getId().toString() : null);
					feedbackDTO.setZone(null != value.getCommonValue() ? value.getCommonValue() : null );					
					fireViewEvent(ManagerFeedBackUIPresenter.SEARCH_FOR_COMPLETED_CASE, user.getUserName(),value.getId(),SHAConstants.BRANCH_BASED);
				}
				else
				{
					fireViewEvent(ManagerFeedBackUIPresenter.SEARCH_FOR_COMPLETED_CASE, user.getUserName(),null);
				}
				
			}
		});
	}
	
	public void showErrorMessage(String errMsg) {
		
		Label successLabel = new Label("<b style = 'color: red;'>" + errMsg + "</b>", ContentMode.HTML);
		VerticalLayout layout = new VerticalLayout(successLabel);
		layout.setSpacing(true);
		layout.setWidth("100%");
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setWidth("12%");
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}	
	
	public void buildReviewReplySuccessLayout() {
		
		Label successLabel = new Label("<b style = 'color: green;'>Feedback Submitted Successfully</b>", ContentMode.HTML);
		Button homeButton = new Button("FeedBack Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		//getSession().setAttribute(SHAConstants.TOKEN_ID, null);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.MANAGER_FEEDBACK, null);
	    		VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
			}
		});
	}
}
