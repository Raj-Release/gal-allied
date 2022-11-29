package com.shaic.claim.reimbursement.talktalktalk;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.domain.IntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class DialerLoginPageUI extends SearchComponent<InitiateTalkTalkTalkDTO>{
	
	private Window popupWindow;
	
	private TextField extnCode;
	
	private Panel mainPanel;
	
	private Button signIn;
	
	private Button cancel;
	
	private InitiateTalkTalkTalkDTO initiateTalkDto = null;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@PostConstruct
	public void init(){
		
	}
	
	public void initView(Window popup,InitiateTalkTalkTalkDTO talkDto) {
		initiateTalkDto = talkDto;
		popupWindow = popup;
		initBinder();
		mainPanel = new Panel();
//		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
//		mainPanel.setCaption("Dialler Login");
		mainPanel.setHeight("400px");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	public VerticalLayout mainVerticalLayout(){
		
		mainVerticalLayout = new VerticalLayout();
		 mainVerticalLayout.setMargin(false);
//		 mainVerticalLayout.addStyleName("layout-with-border");
		 extnCode = binder.buildAndBind("Extn.Code", "extnCode", TextField.class);
		CSValidator userIdValidator = new CSValidator();
		userIdValidator.extend(extnCode);
		userIdValidator.setRegExp("^[0-9]*$");
		userIdValidator.setPreventInvalidTyping(true);
		signIn = new Button("Sign In");
//		signIn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		signIn.setDisableOnClick(false);
		cancel = new Button("Cancel");
		cancel.setDisableOnClick(true);
//		cancel.addStyleName(ValoTheme.BUTTON_DANGER);
		cancel.setDisableOnClick(false);
		
		cancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				extnCode.setValue(null);
				popupWindow.close();
				
			}
		});
		signIn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
			if(extnCode != null && extnCode.getValue() != null && !extnCode.isEmpty()) {
				String extn = extnCode.getValue();
				initiateTalkDto.setExtnCode(extn);
				String refId = dbCalculationService.generateReminderBatchId(SHAConstants.DIALER_REF_SEQUENCE_NAME);
				initiateTalkDto.setReferenceId(refId);
				DialerLoginLogoutResponse loginResponse = intimationService.dialerLogin(initiateTalkDto);
				if(loginResponse != null){
					if(loginResponse.getSTATUS() != null && (loginResponse.getSTATUS().equalsIgnoreCase("LI000") || loginResponse.getSTATUS().equalsIgnoreCase("LI015")
							|| loginResponse.getSTATUS().equalsIgnoreCase("LI0014"))){
						getSession().setAttribute(SHAConstants.EXTN_CODE, extn);
						if(initiateTalkDto.getConvoxid() != null){
							DialerEndCallResponse endCallResponse = intimationService.dialerEndCall(initiateTalkDto);
							if(endCallResponse != null && endCallResponse.getSTATUS() != null && endCallResponse.getSTATUS().equalsIgnoreCase("EC000")){
								System.out.println("Call Ended Successfully");
							}
						}
						popupWindow.close();
						Timer timer = new Timer();
						Date sysdate = new Date();
						System.out.println("Before Place Call Date and Time "+sysdate);
						Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
						calendar.add(Calendar.SECOND, 3);
						Date date = calendar.getTime();
						timer.schedule(new DialerTask(intimationService,initiateTalkDto,getSession()),date);
						/*timer.schedule(new TimerTask() {
						    @Override
						    public void run() {
						    	System.out.println("Place Call Date and Time "+date);
						    	DialerPlaceCallResponse placeCallResponse = intimationService.dialerPlaceCall(initiateTalkDto);
								if(placeCallResponse != null){
									if(placeCallResponse.getSTATUS() != null && placeCallResponse.getSTATUS().equalsIgnoreCase("CL000")){
//										endCallBtn.setEnabled(true);
										if(placeCallResponse.getLEAD_ID() != null){
											initiateTalkDto.setConvoxid(placeCallResponse.getLEAD_ID());
											fireViewEvent(InitiateTalkTalkTalkWizardPresenter.ENABLE_DIALER_BUTTONS,initiateTalkDto);
											getSession().setAttribute(SHAConstants.LEAD_ID, placeCallResponse.getLEAD_ID());
											getSession().setAttribute(SHAConstants.REF_ID, placeCallResponse.getRefno());
											popupWindow.close();
										}
									}else {
										showErrorMessage(placeCallResponse.getMESSAGE());
										popupWindow.close();
									}
								}
						    }
						}, date);*/
						fireViewEvent(InitiateTalkTalkTalkWizardPresenter.ENABLE_DIALER_BUTTONS,initiateTalkDto);
						/*DialerPlaceCallResponse placeCallResponse = intimationService.dialerPlaceCall(initiateTalkDto);
						if(placeCallResponse != null){
							if(placeCallResponse.getSTATUS() != null && placeCallResponse.getSTATUS().equalsIgnoreCase("CL000")){
//								endCallBtn.setEnabled(true);
								if(placeCallResponse.getLEAD_ID() != null){
									initiateTalkDto.setConvoxid(placeCallResponse.getLEAD_ID());
									fireViewEvent(InitiateTalkTalkTalkWizardPresenter.ENABLE_DIALER_BUTTONS,initiateTalkDto);
									getSession().setAttribute(SHAConstants.LEAD_ID, placeCallResponse.getLEAD_ID());
									getSession().setAttribute(SHAConstants.REF_ID, placeCallResponse.getRefno());
									popupWindow.close();
								}
							}else {
								showErrorMessage(placeCallResponse.getMESSAGE());
								popupWindow.close();
							}
						}*/
					} else {
						showErrorMessage(loginResponse.getMESSAGE());
					}
				}
			}else{
					showErrorMessage("Please enter Extn Code");
				}
				
			}
		});
		
		Panel name= new Panel("Dialler Login");
//		name.setCaptionAsHtml(true);
		name.setStyleName(ValoTheme.PANEL_BORDERLESS);
		HorizontalLayout headerLayout = new HorizontalLayout(name);
		FormLayout formLayoutLeft = new FormLayout(extnCode);
		HorizontalLayout dummyLayout = new HorizontalLayout();
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		/*AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(signIn, "top:80.0px;left:100.0px;");
		absoluteLayout_3.addComponent(cancel, "top:80.0px;left:209.0px;");*/
		HorizontalLayout buttonLayout = new HorizontalLayout(signIn,cancel);
		buttonLayout.setSpacing(true);
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponents(headerLayout,fieldLayout,buttonLayout);
		vLayout.setSpacing(true);
		vLayout.setMargin(true);
		vLayout.setComponentAlignment(headerLayout, Alignment.TOP_CENTER);
		vLayout.setComponentAlignment(fieldLayout, Alignment.TOP_CENTER);
		vLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		vLayout.setWidth("70%");
		vLayout.addStyleName("layout-with-border");

		mainVerticalLayout.addComponent(vLayout);
		 mainVerticalLayout.setWidth("100%");
		 mainVerticalLayout.setHeight("100%");
		 mainVerticalLayout.setComponentAlignment(vLayout, Alignment.MIDDLE_CENTER);
//		 mainVerticalLayout.setMargin(true);		 
		addListener();
		return mainVerticalLayout;
		}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<InitiateTalkTalkTalkDTO>(InitiateTalkTalkTalkDTO.class);
		this.binder.setItemDataSource(new InitiateTalkTalkTalkDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	public void setUserValue(InitiateTalkTalkTalkDTO viewSearchCriteriaDTO) {
//		userId.setValue(viewSearchCriteriaDTO.getEmpId());
	}
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
		

}
