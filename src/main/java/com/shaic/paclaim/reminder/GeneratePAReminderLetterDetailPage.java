//package com.shaic.paclaim.reminder;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.PostConstruct;
//import javax.inject.Inject;
//
//import org.vaadin.addon.cdimvp.ViewComponent;
//import org.vaadin.dialogs.ConfirmDialog;
//import org.vaadin.teemu.wizards.GWizard;
//
//import com.shaic.arch.EnhancedFieldGroupFieldFactory;
//import com.shaic.arch.SHAUtils;
//import com.shaic.arch.validation.ValidatorUtils;
//import com.shaic.claim.ClaimDto;
//import com.shaic.claim.ReportDto;
//import com.shaic.claim.intimation.create.dto.DocumentGenerator;
//import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
//import com.shaic.claim.rod.wizard.dto.RODQueryDetailsDTO;
//import com.shaic.claim.rod.wizard.tables.RODQueryDetailsTable;
//import com.shaic.claim.viewEarlierRodDetails.ViewQueryDTO;
//import com.shaic.claim.viewEarlierRodDetails.ViewQueryDetailsTable;
//import com.shaic.domain.Claim;
//import com.shaic.ims.bpm.claim.BPMClientContext;
//import com.shaic.main.navigator.domain.MenuItemBean;
//import com.shaic.reimbursement.queryrejection.draftquery.search.SearchDraftQueryLetterTableDTO;
//import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
//import com.vaadin.server.StreamResource;
//import com.vaadin.server.VaadinSession;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Embedded;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.v7.ui.HorizontalLayout;
//import com.vaadin.v7.ui.TextArea;
//import com.vaadin.v7.ui.TextField;
//import com.vaadin.ui.UI;
//import com.vaadin.v7.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.v7.ui.themes.Reindeer;
//import com.vaadin.ui.themes.ValoTheme;
//
///**
// * 
// * @author Lakshminarayana
// *
// */
//
//public class GeneratePAReminderLetterDetailPage extends ViewComponent{
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	@Inject
//	private ViewQueryDetailsTable queryDetailsTableObj;
//		
//	@Inject
//	private SearchDraftQueryLetterTableDTO bean;
//	
//	private BeanFieldGroup<SearchDraftQueryLetterTableDTO> binder;
//	
//	
//	private HorizontalLayout buttonsLayout;
//	
//	private VerticalLayout generateReminderLetterLayout;
//	
//	private Button submitBtn;
//	
//	private Button cancelBtn;
//
//	
//	@PostConstruct
//	public void init() {
//
//	}
//	
//	public void initView(SearchDraftQueryLetterTableDTO bean) {
//		this.bean = bean;
//		generateReminderLetterLayout = getContent();
//	}
//	
//	public void initBinder() {
//		this.binder = new BeanFieldGroup<SearchDraftQueryLetterTableDTO>(
//				SearchDraftQueryLetterTableDTO.class);
//		this.binder.setItemDataSource(this.bean);
//	}
//	
//	public VerticalLayout getContent() {
//		
//		initBinder();
//		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
//		
//		return buildDecideOnQueryPageLayout();
//	}
//	
//	private VerticalLayout buildDecideOnQueryPageLayout(){
//		generateReminderLetterLayout = new VerticalLayout();
//		
//		List<ViewQueryDTO> queryDetailsList = this.bean.getQueryDetailsList();
//		if(queryDetailsList != null && !queryDetailsList.isEmpty())
//		{
//			queryDetailsTableObj.setTableList(queryDetailsList);
//		}
//
//		
//		generateReminderLetterLayout.addComponent(queryDetailsTableObj);
//		generateReminderLetterLayout.setSpacing(true);
//	
//		Button generateReminderLetterBtn = new Button("Generate Reminder Letter");
//		generateReminderLetterBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//		
//		generateReminderLetterBtn.addClickListener(new Button.ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
////				fireViewEvent(GenerateReminderLetterDetailPresenter.GENERATE_LETTER, bean);
//				
//			}
//		});
//		
//		
//	generateReminderLetterLayout.addComponent(generateReminderLetterBtn);
//	generateReminderLetterLayout.setComponentAlignment(generateReminderLetterBtn, Alignment.MIDDLE_CENTER);
//	generateReminderLetterLayout.setSpacing(true);
//	
//	submitBtn = new Button("Submit");
//	submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
//	
//	cancelBtn = new Button("Cancel");
//	
//	cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
//	
//	addListener();
//
//	buttonsLayout = new HorizontalLayout(submitBtn,cancelBtn);
//	
//	buttonsLayout.setSpacing(true);
//	
//	generateReminderLetterLayout.addComponent(buttonsLayout);
//	generateReminderLetterLayout.setComponentAlignment(buttonsLayout,Alignment.MIDDLE_CENTER);
//	
//	
//	return generateReminderLetterLayout;
//	}
//	
//	public void addListener(){
//		submitBtn.addClickListener(new Button.ClickListener() {
//			
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				
////				Map<String,Object> outcome = new HashMap<String,Object>();
////				String userId = UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString();
////				String password = UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString();
////				outcome.put(BPMClientContext.USERID, userId);
////				outcome.put(BPMClientContext.PASSWORD,password);
////				outcome.put("OUTCOME","APPROVE");
////				outcome.put("Bean",bean);
////				fireViewEvent(GenerateReminderLetterDetailPresenter.SUBMIT_REMINDER_LETTER,outcome);
//				
//			}
//		});
//		
//	cancelBtn.addClickListener(new Button.ClickListener() {
//		
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public void buttonClick(ClickEvent event) {
//			
//			
//			VaadinSession session = getSession();
//			SHAUtils.releaseHumanTask(bean.getUsername(), bean.getPassword(), bean.getTaskNumber(),session);
//
//			fireViewEvent(
//					GeneratePAReminderLetterDetailPresenter.CANCEL_GENERATE_PA_REMINDER_LETTER,
//					null);
//			
//			
////			fireViewEvent(GenerateReminderLetterDetailPresenter.CANCEL_GENERATE_REMINDER_LETTER, null);
//			
//		}
//	});
//	
//	}
//	public void cancelReminderLetter() {
//
//		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
//				"Are you sure you want to cancel ?", "No",
//				"Yes", new ConfirmDialog.Listener() {
//
//					public void onClose(ConfirmDialog dialog) {
//						if (dialog.isCanceled() && !dialog.isConfirmed()) {
//							// Confirmed to continue
//							fireViewEvent(MenuItemBean.GENERATE_PA_REMINDER_LETTER_CLAIM_WISE,
//									null);
//						} else {
//							// User did not confirm
//						}
//					}
//				});
//
//		dialog.setStyleName(Reindeer.WINDOW_BLACK);
//	}
//	
//	public void generateReminderLetter(ReimbursementQueryDto queryDto)
//	{
//		
//		List<ReimbursementQueryDto> queryDtoList = new ArrayList<ReimbursementQueryDto>();
//		queryDtoList.add(queryDto);
//		DocumentGenerator docGenarator = new DocumentGenerator();
//		String fileUrl = null;
//		ReportDto reportDto = new ReportDto();
//		reportDto.setClaimId(queryDto.getClaimId());
//		reportDto.setBeanList(queryDtoList);
//		
//		fileUrl = docGenarator.generatePdfDocument("ReminderLetter", reportDto);
//		
//		if(!ValidatorUtils.isNull(fileUrl))
//		{
//			openPdfFileInWindow(fileUrl);
//		}
//		else
//		{
//			//Exception while PDF Letter Generation
//		}	
//		
//	}
//	
//	public void openPdfFileInWindow(final String filepath) {
//		
//		Window window = new Window();
//		// ((VerticalLayout) window.getContent()).setSizeFull();
//		window.setResizable(true);
//		window.setCaption("Query Reminder Letter PDF");
//		window.setWidth("800");
//		window.setHeight("600");
//		window.setModal(true);
//		window.center();
//
//		Path p = Paths.get(filepath);
//		String fileName = p.getFileName().toString();
//
//		StreamResource.StreamSource s = new StreamResource.StreamSource() {
//
//			public FileInputStream getStream() {
//				try {
//
//					File f = new File(filepath);
//					FileInputStream fis = new FileInputStream(f);
//					return fis;
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					return null;
//				}
//			}
//		};
//
//		StreamResource r = new StreamResource(s, fileName);
//		Embedded e = new Embedded();
//		e.setSizeFull();
//		e.setType(Embedded.TYPE_BROWSER);
//		r.setMIMEType("application/pdf");
//		e.setSource(r);
//		window.setContent(e);
//		UI.getCurrent().addWindow(window);
//	}
//
//}
