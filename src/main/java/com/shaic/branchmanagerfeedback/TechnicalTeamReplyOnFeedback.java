package com.shaic.branchmanagerfeedback;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.domain.ReferenceTable;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class TechnicalTeamReplyOnFeedback extends ViewComponent {

/*	
	@Inject
	PolicyFeedbackReply policyFeedbackReply;
	
	@Inject
	ClaimFeedbackReply claimFeedbackReply;
	
	@Inject
	ProposalFeedbackReply proposalFeedbackReply;*/
	private Window popup;
	protected BeanFieldGroup<BranchManagerFeedbackTableDTO> binder;
	private BranchManagerFeedbackTableDTO bean;
	private TextField branchManagerName;
	private TextField branchName;
	private TextField mobile;
	//private ComboBox feedbackFor;
//	private CheckBox feedbackFor;
	private TextField reportedDate;
	private TextField repliedDate;
	private TextArea feedbackRemarks;
	private TextArea techTeamReply;
	private TextArea proposalTeamReply;
	private TextArea claimsTeamReply;
	private TextField feedbackType;
	private VerticalLayout mainVerticalLayout;
	private Panel mainPanel;
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();	
	Label feedBack;
	Label category;
	private TextField policyNumber;
	private TextField proposalNumber;	
	private TextField claimNumber;	
	private TextField txtFeedBack = new TextField();
	private TextField txtCategory = new TextField();
	private TextField txtFeedBackType;
	@PostConstruct
	public void init(BranchManagerFeedbackTableDTO dto) {
		this.bean = dto;
		initBinder();
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Reply To Branch Manager's Feedback");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public void setPopUp(Window popUp){
		this.popup = popUp;
	}
		
	public Boolean isValid() {
		try{
		if(binder.isValid() && validatePage())  {
			binder.commit();
			/*if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)){
				bean.setPolicyList((List)policyFeedbackReply.getTable().getItemIds());
			}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {
				bean.setProposalList((List) proposalFeedbackReply.getTable().getItemIds());
			}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {
				bean.setClaimList((List)claimFeedbackReply.getTable().getItemIds());
			}*/
			return true;
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		return false;
	}
	
	private Boolean validatePage(){
		
		StringBuffer error = new StringBuffer();
		Boolean isValid = true;
				
		if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)){
			if(techTeamReply != null && (techTeamReply.getValue() == null || techTeamReply.getValue().isEmpty() || StringUtils.isBlank(techTeamReply.getValue()))){
				error.append("Please enter Claims Department Reply Remarks <br/>");
				isValid = false;
			}
		}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {
			if(proposalTeamReply != null && (proposalTeamReply.getValue() == null || proposalTeamReply.getValue().isEmpty() || StringUtils.isBlank(proposalTeamReply.getValue()))){
				error.append("Please Enter Corporate Medical Underwriting Reply Remarks <br/>");
				isValid = false;
			}
					
		}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {
			if(claimsTeamReply != null && (claimsTeamReply.getValue() == null || claimsTeamReply.getValue().isEmpty() || StringUtils.isBlank(claimsTeamReply.getValue()))){
				error.append("Please enter Claims Department Reply Remarks <br/>");
				isValid = false;
			}
		}
		
		if(!isValid)
		getErrorMessage(error.toString());
		
		return isValid;
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<BranchManagerFeedbackTableDTO>(BranchManagerFeedbackTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public void showResult(){
		Label successLabel = new Label("<b style = 'color: black;'>Records Saved Successfully.</b>", ContentMode.HTML);			
		Button homeButton = new Button("Reply to Branch Manager's Feedback Home");
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
				if(popup != null){
					popup.close();
				}
				fireViewEvent(MenuItemBean.MANAGER_FEEDBACK, null);
				
			}
		});
	}
	
	public VerticalLayout mainVerticalLayout() {
		
		mainVerticalLayout = new VerticalLayout();	
		TextField managerName = new TextField("Branch Manager's Name        " + bean.getBranchManagerName());		
		branchManagerName = new TextField();
		managerName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField branchNamae = new TextField("Branch Name          " + bean.getBranchName());
		branchName = new TextField();			
		branchNamae.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		TextField imageLable = new TextField(bean.getMobile());
		imageLable.setIcon(new ThemeResource("images/mobile.png"));
		imageLable.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);	
		
		txtFeedBackType= new TextField();
		txtFeedBackType.setStyleName("rosered");
		txtFeedBackType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtFeedBackType.setWidth("120px");
		
		if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)){
			txtFeedBackType.setValue("Claim Feedback");
			feedBack = new Label("Intimation Number");
			category = new Label("Feedback Category");
			txtFeedBack.setReadOnly(false);
			txtFeedBack.setValue(null != bean.getDocumentNumber() ? bean.getDocumentNumber() :null);
			txtFeedBack.setReadOnly(true);			
			txtCategory.setReadOnly(false);
			txtCategory.setValue(null != bean.getFbCategory() ? bean.getFbCategory() :null);
			txtCategory.setReadOnly(true);
		}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {
			txtFeedBackType.setValue("MER Feedback");	
			feedBack = new Label("MER Number");
			category = new Label("Feedback Category");
			txtFeedBack.setReadOnly(false);
			txtFeedBack.setValue(null != bean.getDocumentNumber() ? bean.getDocumentNumber() :null);
			txtFeedBack.setReadOnly(true);			
			txtCategory.setReadOnly(false);
			txtCategory.setValue(null != bean.getFbCategory() ? bean.getFbCategory() :null);
			txtCategory.setReadOnly(true);
		}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {
			txtFeedBackType.setValue("Claim Feedback");	
			feedBack = new Label("Intimation Number");
			category = new Label("Feedback Category");
			txtFeedBack.setReadOnly(false);
			txtFeedBack.setValue(null != bean.getDocumentNumber() ? bean.getDocumentNumber() :null);
			txtFeedBack.setReadOnly(true);			
			txtCategory.setReadOnly(false);
			txtCategory.setValue(null != bean.getFbCategory() ? bean.getFbCategory() :null);
			txtCategory.setReadOnly(true);
		}
					
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.addComponents(managerName,imageLable,branchNamae);
		hLayout.setWidth("100%");
		hLayout.setSpacing(true);
		hLayout.setMargin(true);
		
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.addComponent(txtFeedBackType);
		headerLayout.setMargin(true);
		mainVerticalLayout.addComponent(headerLayout);
		mainVerticalLayout.addComponent(hLayout);
				
		feedbackRemarks = binder.buildAndBind("Feedback Reason(s)", "feedbackRemarksOverall", TextArea.class);		
		feedbackType = binder.buildAndBind("Feedback Type", "feedbackType", TextField.class);
		feedbackType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);	
		
		reportedDate = binder.buildAndBind(" ", "reportedDate", TextField.class);
		reportedDate.setReadOnly(true);
		reportedDate.setStyleName("green");
		reportedDate.setWidth("100px");
		repliedDate = binder.buildAndBind(" ", "repliedDate", TextField.class);
		repliedDate = new TextField();	
		repliedDate.setStyleName("grey");
		repliedDate.setWidth("100px");	
		repliedDate.setEnabled(false);
		repliedDate.setReadOnly(true);
		
		Image imageSrcFeedbackType = null;
		if(bean.getFeedbackType().equals(ReferenceTable.VERY_GOOD_FEEDBACK)){
			imageSrcFeedbackType = new Image("", new ThemeResource("images/ftVeryGood.png"));
		}else if(bean.getFeedbackType().equals(ReferenceTable.GOOD_FEEDBACK)){
			imageSrcFeedbackType = new Image("", new ThemeResource("images/ftGood.png"));
		}else if(bean.getFeedbackType().equals(ReferenceTable.AVERAGE_FEEDBACK)) {
			imageSrcFeedbackType = new Image("", new ThemeResource("images/ftAverage.png"));
		}else if(bean.getFeedbackType().equals(ReferenceTable.SATISFACTORY_FEEDBACK)){
			imageSrcFeedbackType = new Image("", new ThemeResource("images/ftSatisfactory.png"));
		}else if(bean.getFeedbackType().equals(ReferenceTable.BELOW_AVERAGE_FEEDBACK)){
			imageSrcFeedbackType = new Image("", new ThemeResource("images/ftBelowAverage.png"));
			
		}

		feedbackRemarks.setEnabled(false);
		feedbackType.setEnabled(false);
		
		
		
		FormLayout formLayoutLeft = new FormLayout(txtFeedBack,feedbackRemarks,reportedDate);	
		
		formLayoutLeft.addComponent(repliedDate);
		formLayoutLeft.setSpacing(true);
		Label dummyImg = new Label();
		FormLayout formLayoutRight = new FormLayout(dummyImg,imageSrcFeedbackType);
		formLayoutRight.setSpacing(true);
		formLayoutRight.setMargin(true);
		
		// Top
		Label lbl1 = new Label("");		
		Label lbl2 = new Label("Feedback Remarks");
		Label lbl3 = new Label("");		
		lbl3.setHeight("3px");
		VerticalLayout leftVerticalLayout1 = new VerticalLayout(feedBack,lbl3,category,lbl1, lbl2, reportedDate);
		
		TextArea txtArea1 = new TextArea();
		txtArea1.setWidth("300px");
		txtArea1.setReadOnly(Boolean.FALSE);
		txtArea1.setValue(bean.getFeedbackRemarksOverall());
		txtArea1.setDescription(bean.getFeedbackRemarksOverall());
		txtArea1.setReadOnly(Boolean.TRUE);
		txtArea1.setMaxLength(4000);
		
		VerticalLayout rightVerticalLayout1 = new VerticalLayout(txtFeedBack,txtCategory,txtArea1);
		rightVerticalLayout1.setSpacing(true);
		
		HorizontalLayout childHorizontalLayout1 = new HorizontalLayout(leftVerticalLayout1, rightVerticalLayout1);
		childHorizontalLayout1.setSpacing(true);
		
		// Bottom
		Label replyRemarksLbl = null;
		VerticalLayout rightVerticalLayout2 = 	new VerticalLayout();
		techTeamReply = binder.buildAndBind("", "technicalTeamReply", TextArea.class);
		proposalTeamReply = binder.buildAndBind("", "corporateMedicalUnderwritingReply", TextArea.class);
		claimsTeamReply = binder.buildAndBind("", "claimsDepartmentReply", TextArea.class);
		
		techTeamReply.setWidth("300px");
		proposalTeamReply.setWidth("300px");
		claimsTeamReply.setWidth("300px");
		techTeamReply.setMaxLength(4000);
		proposalTeamReply.setMaxLength(4000);
		claimsTeamReply.setMaxLength(4000);
		
		Label hint = new Label("<b style = 'color: red;'>" + SHAConstants.FEEDBACK_REPLY_HINT + "</b>",ContentMode.HTML);
		
		if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)){
			replyRemarksLbl = new Label("Claims Department Reply<b style= 'color: red'>*</b>",ContentMode.HTML);
			replyRemarksLbl.setCaptionAsHtml(true);
			if(null != bean.getTechnicalTeamReply()){
				techTeamReply.setValue(bean.getTechnicalTeamReply());
				techTeamReply.setDescription("Technical Team Reply");
			}
			lbl2.setWidth("150px");
			replyRemarksLbl.setWidth("150px");
			replyRemarksLbl.setEnabled(true);
			techTeamReply.setData(bean);
			rightVerticalLayout2 = new VerticalLayout(techTeamReply,hint);
			addDetailPopupForFeedBackReplyRemarks(techTeamReply, null);
		}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {
			replyRemarksLbl = new Label("Corporate Medical <br>Underwriting Reply <b style= 'color: red'>*</b>",ContentMode.HTML);
			if(null != bean.getCorporateMedicalUnderwritingReply()){
				proposalTeamReply.setValue(bean.getCorporateMedicalUnderwritingReply());
				proposalTeamReply.setDescription("Corporate Medical Underwriting Reply");
			}
			lbl2.setWidth("180px");
			replyRemarksLbl.setWidth("180px");
			replyRemarksLbl.setEnabled(true);
			rightVerticalLayout2 = new VerticalLayout(proposalTeamReply,hint);
			proposalTeamReply.setData(bean);
			addDetailPopupForFeedBackReplyRemarks(proposalTeamReply, null);
		}else if(bean.getFeedbackKeyForFeedbackValue().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {
			replyRemarksLbl = new Label("Claims Department Reply <b style= 'color: red'>*</b>",ContentMode.HTML);
			replyRemarksLbl.setCaptionAsHtml(true);
			if(null != bean.getClaimsDepartmentReply()){
				claimsTeamReply.setValue(bean.getClaimsDepartmentReply());
			}
			claimsTeamReply.setDescription("Claims Department Reply");
			lbl2.setWidth("150px");
			replyRemarksLbl.setWidth("150px");
			replyRemarksLbl.setEnabled(true);
			rightVerticalLayout2 = new VerticalLayout(claimsTeamReply,hint);
			claimsTeamReply.setData(bean);
			addDetailPopupForFeedBackReplyRemarks(claimsTeamReply, null);
		}
		
		Label replyLbl = new Label("");
		replyLbl.setHeight("5px");
		VerticalLayout leftVerticalLayout2 = new VerticalLayout(replyLbl,replyRemarksLbl, repliedDate);	
	
		HorizontalLayout childHorizontalLayout2 = new HorizontalLayout(leftVerticalLayout2, rightVerticalLayout2);
		childHorizontalLayout2.setSpacing(true);		
		
		// Top + Bottom
		VerticalLayout parentVerticalLayout = new VerticalLayout(childHorizontalLayout1, childHorizontalLayout2);
		parentVerticalLayout.setSpacing(true);
		parentVerticalLayout.setMargin(true);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(parentVerticalLayout, formLayoutRight);
		
		
		fieldLayout.setMargin(false);
		fieldLayout.setWidth("100%");		
		
		mainVerticalLayout.addComponent(fieldLayout);
		Button submit =new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submit.setData(bean);
		submit.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(isValid()) {
				fireViewEvent(BranchManagerFeedbackPresenter.SUBMIT_CLICK, bean);
				fireViewEvent(MenuItemBean.MANAGER_FEEDBACK, null);
				}
				
			}
			
		});
		
		Button cancel = new Button("Close");
		
		cancel.addStyleName(ValoTheme.BUTTON_DANGER);
		cancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				
			}
			
		});	
		
		
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1000px");
		 mainVerticalLayout.setMargin(true);
		 HorizontalLayout buttons = new HorizontalLayout();
		 buttons.setSpacing(true);
		 buttons.setMargin(true);
		 buttons.addComponents(submit,cancel);		 
		 mainVerticalLayout.addComponent(buttons);
		 mainVerticalLayout.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
		 
		/* mandatoryFields.add(techTeamReply);
		 mandatoryFields.add(proposalTeamReply);
		 mandatoryFields.add(claimsTeamReply);*/
		 showOrHideValidation(false);
		 return mainVerticalLayout;
		 
	}
	
	
	
	private void addDetailPopupForFeedBackReplyRemarks(TextArea txtFld, final  Listener listener) {
		  ShortcutListener enterShortCut = new ShortcutListener(
			        "Reply Remarks(Overall)", ShortcutAction.KeyCode.F8, null) {
				
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
				BranchManagerFeedbackTableDTO  searchPedDto = (BranchManagerFeedbackTableDTO) txtFld.getData();
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
				
				String caption =  txtFld.getDescription();
				
				if(caption.equalsIgnoreCase("Claims Department Reply")){
					searchPedDto.setTechnicalTeamReply(txtArea.getValue());
				}else if(caption.equalsIgnoreCase("Corporate Medical Underwriting Reply")){
					searchPedDto.setCorporateMedicalUnderwritingReply(txtArea.getValue());
				}else if(caption.equalsIgnoreCase("Claims Department Reply")){
					searchPedDto.setClaimsDepartmentReply(txtArea.getValue());
				}
				
				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
				
				final Window dialog = new Window();
				
				dialog.setCaption(caption);
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
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			//field.setValidationVisible(isVisible);
		}
	}
	
}
