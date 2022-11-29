package com.shaic.feedback.managerfeedback.previousFeedback;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class PreviousFeedbackView extends ViewComponent {
	
	private static final String VERY_GOOD = "VeryGood";
	private static final String GOOD = "Good";
	private static final String SATISFACTORY = "Satisfactory";
	private static final String AVERAGE = "Average";
	private static final String BELOW_AVERAGE = "BelowAverage";
	
	@Inject
	PreviousFeedbackViewLinkTable previousFeedbackReply;
	
	private Window popup;
	protected BeanFieldGroup<BranchManagerPreviousFeedbackTableDTO> binder;
	private BranchManagerPreviousFeedbackTableDTO bean;
	private TextField branchManagerName;
	private TextField branchName;
	private TextField mobile;
	//private ComboBox feedbackFor;
	private CheckBox feedbackFor;
	private TextField reportedDate;
	private TextField repliedDate;
	private TextArea feedbackRemarks;
	private TextArea techTeamReply;
	private TextArea proposalTeamReply;
	private TextArea claimsTeamReply;
	private TextField feedbackType;
	private VerticalLayout mainVerticalLayout;
	private Panel mainPanel;
	private VerticalLayout generatedLayout;
	private Button submit;
	private Button close;
	private Label totalCount;
	private Window popUp;
	private TextField txtFeedBack = new TextField();
	private TextField txtCategory = new TextField();
	private TextField txtFeedBackType;
	Label feedBack;
	Label category;
	private OptionGroup branchManagerReview;
	private OptionGroup feedbackOptnGrp;

	
	@PostConstruct
	public void init(BranchManagerPreviousFeedbackTableDTO dto) {
		this.bean = dto;	
		initBinder();
		mainPanel = new Panel();
		/*policyFeedbackReply.init("", false, false);
		policyFeedbackReply.setTableList(dto.getPolicyList());*/
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Branch Manager's Feedback");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	public void setPopUp(Window popup){
		this.popUp = popup;
	}
	public VerticalLayout mainVerticalLayout() {
		mainVerticalLayout = new VerticalLayout();
		bean.setFeedbackOption(Boolean.TRUE);
		
		txtFeedBackType= new TextField();
		txtFeedBackType.setStyleName("rosered");
		txtFeedBackType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtFeedBackType.setWidth("120px");
		
		if(bean.getFeedBackTypeId().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)){
			txtFeedBackType.setValue("Claims Feedback");
			feedBack = new Label("Intimation Number");
			category = new Label("Feedback Category");
			txtFeedBack.setReadOnly(false);
			txtFeedBack.setValue(null != bean.getDocumentNumber() ? bean.getDocumentNumber() :null);
			txtFeedBack.setReadOnly(true);			
			txtCategory.setReadOnly(false);
			txtCategory.setValue(null != bean.getFbCategory() ? bean.getFbCategory() :null);
			txtCategory.setReadOnly(true);		
		}else if(bean.getFeedBackTypeId().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {
			txtFeedBackType.setValue("Proposal Feedback");	
			feedBack = new Label("MER Number");
			category = new Label("Feedback Category");
			txtFeedBack.setReadOnly(false);
			txtFeedBack.setValue(null != bean.getDocumentNumber() ? bean.getDocumentNumber() :null);
			txtFeedBack.setReadOnly(true);			
			txtCategory.setReadOnly(false);
			txtCategory.setValue(null != bean.getFbCategory() ? bean.getFbCategory() :null);
			txtCategory.setReadOnly(true);
		}else if(bean.getFeedBackTypeId().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {
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
		
		txtFeedBack.setReadOnly(true);
		txtCategory.setReadOnly(true);
		
		HorizontalLayout headerLayout = new HorizontalLayout();
		Label dummy = new Label();
		headerLayout.addComponents(txtFeedBackType/*,feedBack,dummy,txtFeedBack*/);
		headerLayout.setMargin(true);
		mainVerticalLayout.addComponent(headerLayout);
		
		feedbackRemarks = binder.buildAndBind("Feedback Reason(s)", "feedbackRemarksOverall", TextArea.class);		
		feedbackType = binder.buildAndBind("Feedback Type", "feedbackType", TextField.class);
		feedbackType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);	
		
		reportedDate = binder.buildAndBind(" ", "reportedDate", TextField.class);
		reportedDate.setStyleName("green");
		reportedDate.setWidth("100px");
		repliedDate = binder.buildAndBind(" ", "repliedDate", TextField.class);
		
		Image replyBasedImg = null;
		
		if(repliedDate.getValue() != null && !repliedDate.getValue().isEmpty()){
			repliedDate.setStyleName("green");
			replyBasedImg = new Image("", new ThemeResource("images/selected.png"));
		}else{
			repliedDate.setStyleName("grey");
			replyBasedImg = new Image("", new ThemeResource("images/loading.png"));
		}
		repliedDate.setWidth("100px");
		reportedDate.setReadOnly(true);
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
		
		
		
		/*FormLayout formLayoutLeft = new FormLayout(feedbackFor,feedbackRemarks,reportedDate);	
		
		formLayoutLeft.addComponent(repliedDate);
		formLayoutLeft.setSpacing(true);
		VerticalLayout dummy = new VerticalLayout();
		VerticalLayout dummy1 = new VerticalLayout();*/
		Label dummyImg = new Label();
		Label dummyImg1 = new Label();
		FormLayout formLayoutRight = new FormLayout(dummyImg,imageSrcFeedbackType,dummyImg1,replyBasedImg);
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
		
		
		VerticalLayout rightVerticalLayout1 = new VerticalLayout(txtFeedBack,txtCategory, txtArea1);
		rightVerticalLayout1.setSpacing(true);
		
		HorizontalLayout childHorizontalLayout1 = new HorizontalLayout(leftVerticalLayout1, rightVerticalLayout1);
		childHorizontalLayout1.setSpacing(true);
		
		// Bottom
		Label replyRemarksLbl = new Label();
		TextArea txtArea2 = new TextArea();
		txtArea2.setWidth("300px");
		txtArea2.setNullRepresentation("");
		if(bean.getFeedBackTypeId().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)){
			replyRemarksLbl = new Label("Claims Department Reply");	
			txtArea2.setReadOnly(Boolean.FALSE);
			txtArea2.setValue(bean.getTechnicalTeamReply());
			if(null != bean.getTechnicalTeamReply()){
				txtArea2.setDescription(bean.getTechnicalTeamReply());
			}
			txtArea2.setReadOnly(Boolean.TRUE);
			lbl2.setWidth("150px");
			replyRemarksLbl.setWidth("150px");
			replyRemarksLbl.setEnabled(false);
		}else if(bean.getFeedBackTypeId().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {
			replyRemarksLbl = new Label("Corporate Medical Underwriting Reply");
			txtArea2.setReadOnly(Boolean.FALSE);
			if(null != bean.getCorporateMedicalUnderwritingReply()){
				txtArea2.setValue(bean.getCorporateMedicalUnderwritingReply());
			}
			txtArea2.setDescription(bean.getCorporateMedicalUnderwritingReply());
			txtArea2.setReadOnly(Boolean.TRUE);
			
			lbl2.setWidth("180px");
			replyRemarksLbl.setWidth("180px");
			replyRemarksLbl.setEnabled(false);
		}else if(bean.getFeedBackTypeId().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {
			replyRemarksLbl = new Label("Claims Department Reply");
			txtArea2.setReadOnly(Boolean.FALSE);
			if(null != bean.getClaimsDepartmentReply()){
				txtArea2.setValue(bean.getClaimsDepartmentReply());
			}
			txtArea2.setDescription(bean.getClaimsDepartmentReply());
			txtArea2.setReadOnly(Boolean.TRUE);			
			lbl2.setWidth("150px");
			replyRemarksLbl.setWidth("150px");
			replyRemarksLbl.setEnabled(false);
		}
		
		Label branchManagerReviewLbl = new Label("Branch Manager Review");
		Label dummyLable = new Label();
		dummyLable.setHeight("34px");
		Label feedBackReview = new Label("Feedback Review");
		Label dummyLable1 = new Label();
		dummyLable1.setHeight("36px");
		
		branchManagerReview = (OptionGroup) binder.buildAndBind("", "branchManagerReview",
				OptionGroup.class);
		branchManagerReview.setRequired(false);
		branchManagerReview.addItems(getReadioButtonOptions());
		branchManagerReview.setItemCaption(true, "Agree");
		branchManagerReview.setItemCaption(false, "Disagree");
		branchManagerReview.setStyleName("horizontal");
		if(null != bean.getFeedBackReviewFlag() && bean.getFeedBackReviewFlag().equalsIgnoreCase("A")){
			branchManagerReview.setValue(Boolean.TRUE);
		}
		else if(null != bean.getFeedBackReviewFlag() && bean.getFeedBackReviewFlag().equalsIgnoreCase("D")){
			branchManagerReview.setValue(Boolean.FALSE);
		}
		
		VerticalLayout optionimageLayout = getOptionGroupForImage(null);
		
		if(null != bean.getFbStatusId() && (ReferenceTable.FEEDBACK_RESPONDED_KEY.equals(bean.getFbStatusId()))){
			branchManagerReview.setEnabled(Boolean.TRUE);
			feedbackOptnGrp.setEnabled(Boolean.TRUE);
		}
		else
		{
			branchManagerReview.setEnabled(Boolean.FALSE);
			feedbackOptnGrp.setEnabled(Boolean.FALSE);

		}
		
		VerticalLayout leftVerticalLayout2 = new VerticalLayout(replyRemarksLbl, repliedDate,dummyLable,branchManagerReviewLbl,dummyLable1,feedBackReview);
		
	
		VerticalLayout rightVerticalLayout2 = new VerticalLayout(txtArea2,branchManagerReview,optionimageLayout);
		
		HorizontalLayout childHorizontalLayout2 = new HorizontalLayout(leftVerticalLayout2, rightVerticalLayout2);
		childHorizontalLayout2.setSpacing(true);
		
		// Top + Bottom
		VerticalLayout parentVerticalLayout = new VerticalLayout(childHorizontalLayout1, childHorizontalLayout2);
		parentVerticalLayout.setSpacing(true);
		
//		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft, formLayoutRight);
		HorizontalLayout fieldLayout = new HorizontalLayout(parentVerticalLayout, formLayoutRight);
		
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		Button cancel = new Button("Close");
		cancel.addStyleName(ValoTheme.BUTTON_DANGER);
		cancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popUp.close();
			}
		});	
		
		Button submit = new Button("Submit");
		submit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				try {
					binder.commit();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String err = validate();
				if(err == null || err.isEmpty())
				{
					if(null != feedbackOptnGrp && null != feedbackOptnGrp.getValue()){
						if(feedbackOptnGrp.isSelected(VERY_GOOD)){
							bean.setRatingfor(ReferenceTable.RATING_FOR_VERYGOOD);
						}
						else if(feedbackOptnGrp.isSelected(GOOD)){
							bean.setRatingfor(ReferenceTable.RATING_FOR_GOOD);
						}
						else if(feedbackOptnGrp.isSelected(SATISFACTORY)){
							bean.setRatingfor(ReferenceTable.RATING_FOR_SASTIFACTORY);
						}
						else if(feedbackOptnGrp.isSelected(AVERAGE)){
							bean.setRatingfor(ReferenceTable.RATING_FOR_AVERAGE);
						}
						else if(feedbackOptnGrp.isSelected(BELOW_AVERAGE)){
							bean.setRatingfor(ReferenceTable.RATING_FOR_BELOWAVERAGE);
						}
					}
					if(null != bean.getBranchManagerReview() && bean.getBranchManagerReview()){
						bean.setFeedBackReviewFlag("A");
					}
					else
					{
						bean.setFeedBackReviewFlag("D");
					}
					fireViewEvent(BranchManagerPreviousFeedbackPresenter.INSERT_BRANCH_MANAGER_REVIEW_DETAILS, bean,popUp);					
				}
				else
				{
					showErrorMessage(err);
				}
				//popUp.close();
			}
		});	
		 mainVerticalLayout.addComponent(fieldLayout);
			
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		// mainVerticalLayout.setWidth("800px");
		 mainVerticalLayout.setMargin(false);
		 HorizontalLayout hLayout = new HorizontalLayout();
		 hLayout.addComponents(submit,cancel);
		 hLayout.setMargin(false);
		 hLayout.setSpacing(true);		
		 mainVerticalLayout.addComponents(hLayout);		
		 mainVerticalLayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
		 return mainVerticalLayout;
		 
	}
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<BranchManagerPreviousFeedbackTableDTO>(BranchManagerPreviousFeedbackTableDTO.class);
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
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
		
			if(null != bean.getFeedBackReviewRatingId() && bean.getFeedBackReviewRatingId().equals(ReferenceTable.RATING_FOR_VERYGOOD)){
				feedbackOptnGrp.select(VERY_GOOD);
			}
			else if(null != bean.getFeedBackReviewRatingId() && bean.getFeedBackReviewRatingId().equals(ReferenceTable.RATING_FOR_GOOD)){
				feedbackOptnGrp.select(GOOD);
			}
			else if(null != bean.getFeedBackReviewRatingId() && bean.getFeedBackReviewRatingId().equals(ReferenceTable.RATING_FOR_SASTIFACTORY)){
				feedbackOptnGrp.select(SATISFACTORY);
			}
			else if(null != bean.getFeedBackReviewRatingId() && bean.getFeedBackReviewRatingId().equals(ReferenceTable.RATING_FOR_AVERAGE)){
				feedbackOptnGrp.select(AVERAGE);
			}
			else if(null != bean.getFeedBackReviewRatingId() && bean.getFeedBackReviewRatingId().equals(ReferenceTable.RATING_FOR_BELOWAVERAGE)){
				feedbackOptnGrp.select(BELOW_AVERAGE);
			}
		
	    HorizontalLayout optionsLayout = new HorizontalLayout(feedbackOptnGrp);
		optionsLayout.setSpacing(true);
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponents(iconLayout,optionsLayout);
		
		return vLayout;
	}
	
	public String validate(){

		StringBuffer err = new StringBuffer();
		
		if((null != branchManagerReview && null == branchManagerReview.getValue()) && (null != feedbackOptnGrp && null == feedbackOptnGrp.getValue()))
		{
			err.append("Please choose any feedback");
		}	
		else if((null != feedbackOptnGrp && null != feedbackOptnGrp) && (null != branchManagerReview && null == branchManagerReview.getValue()))
		{
			err.append("Please choose Branch Manager Review");
		}
		else if((null != branchManagerReview && null != branchManagerReview.getValue()) && (null != feedbackOptnGrp && null == feedbackOptnGrp.getValue()))
		{
			err.append("Please select Feedback Review<br>");
		}	
		
		
		return err.toString();
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