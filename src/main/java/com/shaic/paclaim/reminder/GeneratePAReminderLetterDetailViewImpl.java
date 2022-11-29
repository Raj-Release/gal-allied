//package com.shaic.paclaim.reminder;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.inject.Instance;
//import javax.inject.Inject;
//
//import org.vaadin.addon.cdimvp.AbstractMVPView;
//
//import com.shaic.claim.ViewDetails;
//import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
//import com.shaic.ims.carousel.RevisedCarousel;
//import com.shaic.ims.carousel.RevisedCashlessCarousel;
//import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
//import com.vaadin.ui.Button;
//import com.vaadin.v7.ui.HorizontalLayout;
//import com.vaadin.ui.Panel;
//import com.vaadin.v7.ui.VerticalLayout;
//
///**
// * 
// * @author Lakshminarayana
// *
// */
//
//public class GeneratePAReminderLetterDetailViewImpl extends AbstractMVPView implements GeneratePAReminderLetterDetailView{
//
//	@Inject
//	private Instance<RevisedCarousel> commonCarouselInstance;
//	
//	private GeneratePAReminderLetterDetailPage generateReminderLetterPage;
//	
//	@Inject
//	private Instance<GeneratePAReminderLetterDetailPage> generateReminderLetterDetailPageInstance;
//	
//	@Inject
//	private ViewDetails viewDetails;
//	
//	private Panel mainPanel = new Panel();
//	
//	private SearchDraftQueryLetterTableDTO bean; 
//	
//	private HorizontalLayout submitButtonLayout;
//	
//	private Button generatePdfBtn;
//	private Button submitButton;
//	private Button cancelButton;
//	
//	@PostConstruct
//	public void initView() {
//
//		addStyleName("view");
//		setSizeFull();			
//	}
//	
//	public void initView(SearchDraftQueryLetterTableDTO draftQueryLetterDto)
//	{
//		this.bean = draftQueryLetterDto;  
//		generateReminderLetterPage = generateReminderLetterDetailPageInstance.get();
//		generateReminderLetterPage.initView(this.bean);
//		
//		VerticalLayout wholeLayout = new VerticalLayout();
//		
//		
//		
//		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getClaimDto().getNewIntimationDto(), this.bean.getClaimDto(),  "Generate Reminder Letter");
//		wholeLayout.addComponents(intimationDetailsCarousel,viewDetails,generateReminderLetterPage.getContent());
//		wholeLayout.setSpacing(true);
//		mainPanel.setContent(wholeLayout);
////		submitButtonLayout = new HorizontalLayout();
////		
////		generatePdfBtn.addClickListener(new Button.ClickListener() {
////			
////			@Override
////			public void buttonClick(ClickEvent event) {
////				fireViewEvent(GenerateReminderLetterDetailPresenter.GENERATE_LETTER,
////						bean);
////				submitButton.setVisible(true);
////				
////			}
////		});
////		
////		
////		wholeLayout.addComponent(generatePdfBtn);
////		wholeLayout.setComponentAlignment(generatePdfBtn, Alignment.BOTTOM_RIGHT);
////		HorizontalLayout buttonLayout1 = buildSubmitAndCancelBtnLayout();
////		submitButtonLayout.addComponent(buttonLayout1);
////		submitButtonLayout.setWidth("100%");
////		submitButtonLayout.setSpacing(true);
////		submitButtonLayout.setMargin(true);
////		submitButtonLayout.setComponentAlignment(buttonLayout1,
////				Alignment.MIDDLE_CENTER);
////		
////		wholeLayout.addComponent(submitButtonLayout);
//		
//		mainPanel.setContent(wholeLayout);
//		setCompositionRoot(mainPanel);
//		
//			
//	}
//	
////	private HorizontalLayout buildSubmitAndCancelBtnLayout()
////	{
////		
////		submitButton = new Button();
////		submitButton.setCaption("Home Page");
////		//Vaadin8-setImmediate() submitButton.setImmediate(true);
////		submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
////		submitButton.setWidth("-1px");
////		submitButton.setHeight("-1px");
//////		mainLayout.addComponent(submitButton);
////		
////			submitButton.addClickListener(new Button.ClickListener() {
////
////				@Override
////				public void buttonClick(ClickEvent event) {
////					Map<String,Object> outcome = new HashMap<String,Object>();
////					String userId = UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString();
////					String password = UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString();
////					outcome.put(BPMClientContext.USERID, userId);
////					outcome.put(BPMClientContext.PASSWORD,password);
////					outcome.put("OUTCOME","APPROVE");
////					outcome.put("Bean",bean);
////					fireViewEvent(GenerateReminderLetterDetailPresenter.SUBMIT_REMINDER_LETTER,outcome);
////				}
////			});
////	
////		//Vaadin8-setImmediate() submitButton.setImmediate(true);
////		submitButton.setVisible(false);
////		cancelButton = new Button();
////		cancelButton.setCaption("Cancel");
////		//Vaadin8-setImmediate() cancelButton.setImmediate(true);
////		cancelButton.setWidth("-1px");
////		cancelButton.setHeight("-1px");
////		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
////
////		cancelButton.addClickListener(new Button.ClickListener() {
////
////				@Override
////				public void buttonClick(ClickEvent event) {
////					
////					
////					VaadinSession session = getSession();
////					SHAUtils.releaseHumanTask(bean.getUsername(), bean.getPassword(), bean.getTaskNumber(),session);
////
////					fireViewEvent(
////							GenerateReminderLetterDetailPresenter.CANCEL_GENERATE_REMINDER_LETTER,
////							null);
////				}
////			});
////		
////
////		HorizontalLayout newBtnLayout = new HorizontalLayout(submitButton,
////				cancelButton);
////		newBtnLayout.setSpacing(true);
////		return newBtnLayout;
////	}
//
//	@Override
//	public void resetView() {
//		
//	}
//	
//	public void cancelReminderLetter(){		
//		generateReminderLetterPage.cancelReminderLetter();		
//	}
//	@Override
//	public void generateReminderLetter(ReimbursementQueryDto  queryLetterBean){
//		generateReminderLetterPage.generateReminderLetter(queryLetterBean);
//	}
//
//	
//	@Override
//	public void submitReminderLetter(SearchDraftQueryLetterTableDTO queryDto) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
