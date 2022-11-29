package com.shaic.paclaim.reimbursement.draftrejection;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ReimbursementRejectionDto;
import com.shaic.claim.ReportDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.intimation.create.dto.DocumentGenerator;
import com.shaic.claim.pedrequest.view.ViewPEDRequestWindow;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.dto.LegalHeirDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.queryrejection.draftrejection.search.SearchDraftRejectionLetterTableDTO;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author Lakshminarayana
 *
 */

public class DraftPARejectionLetterDetailViewImpl extends AbstractMVPView implements DraftPARejectionLetterDetailView{

	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	private DraftPARejectionLetterDetailPage draftRejectionLetterDetailPage;
	
	@Inject
	private Instance<DraftPARejectionLetterDetailPage> draftRejectionLetterDetailPageInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	private Button initiatePedBtn;
	
	private VerticalSplitPanel mainPanel;	
	
	private SearchDraftRejectionLetterTableDTO bean;
	
	@Inject
	private Instance<ViewPEDRequestWindow> viewPedRequest;
	
	private ViewPEDRequestWindow viewPEDRequestObj;
	
	@Inject
	private Toolbar toolbar;
	
	@PostConstruct
	public void initView() {
		/*arrScreenName = new String[] {
					"documentDetails","acknowledgementReceipt"};*/
		mainPanel = new VerticalSplitPanel();
		//captionMap = new HashMap<String, String>();
		addStyleName("view");
		setSizeFull();			
	}
	
	public void initView(SearchDraftRejectionLetterTableDTO draftRejectionLetterDto)
	{
		this.bean = draftRejectionLetterDto;
		draftRejectionLetterDetailPage = draftRejectionLetterDetailPageInstance.get();
		draftRejectionLetterDetailPage.initView(this.bean);
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto(),  "Draft Rejection Letter");
		intimationDetailsCarousel.init(this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto(),  "Draft Rejection Letter",draftRejectionLetterDto.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
		panel1.setHeight("50px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, draftRejectionLetterDetailPage.getContent());
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
			
	}
	
	private HorizontalLayout commonButtonsLayout()
	{
		
		FormLayout viewDetailsForm = new FormLayout();
		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
		viewDetailsForm.setWidth("-1px");
		viewDetailsForm.setHeight("-1px");
		viewDetailsForm.setMargin(false);
		viewDetailsForm.setSpacing(true);
		//ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId(),bean.getReimbursementRejectionDto().getReimbursementDto().getKey(), ViewLevels.PREAUTH_MEDICAL, false,"");
		viewDetailsForm.addComponent(viewDetails);
		
		initiatePedBtn = new Button("Initiate PED Endorsement");
		
		HorizontalLayout componentsHLayout = new HorizontalLayout(initiatePedBtn,viewDetailsForm);
		componentsHLayout.setWidth("100%");
		componentsHLayout.setSpacing(true);
		componentsHLayout.setComponentAlignment(viewDetailsForm, Alignment.BOTTOM_RIGHT);
		addListener();
		return componentsHLayout;
	}

	@Override
	public void resetView() {
		
	}	

	@Override
	public void cancelDraftRejectionLetter() {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ??",
				"No", "Yes", new ConfirmDialog.Listener() {

					private static final long serialVersionUID = 1L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isCanceled() && !dialog.isConfirmed()) {
							// Confirmed to continue
							VaadinSession session = getSession();
							SHAUtils.releaseHumanTask(bean.getUsername(), bean.getPassword(), bean.getTaskNumber(),session);
							fireViewEvent(MenuItemBean.PA_DRAFT_REJECTION_LETTER, null);
						}
					}
		});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}
	
	@Override
	public void openPdfFileInWindow() {
		
		
		DocumentGenerator docGenarator = new DocumentGenerator();
		
		
		List<ReimbursementRejectionDto> a_beanList = new ArrayList<ReimbursementRejectionDto>();
		
		String fullAge = this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredFullAge();
		this.bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().setInsuredAge(fullAge);
//		a_beanList.add(bean.getReimbursementRejectionDto());
		
		ReportDto reportDto = new ReportDto();
		reportDto.setClaimId(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getClaimId());
//		reportDto.setBeanList(a_beanList);
//		final String filePath = docGenarator.generatePdfDocument("ReimbursementRejectionLetter", reportDto);
		
		String filePath = "";
		
		String templateName = "ReimbursementRejectionLetter";
		
		if(bean.getReimbursementRejectionDto().getRejectionLetterRemarks2()!=null && !bean.getReimbursementRejectionDto().getRejectionLetterRemarks2().isEmpty()){
			bean.getReimbursementRejectionDto().setRejectionLetterRemarks(bean.getReimbursementRejectionDto().getRejectionLetterRemarks()+bean.getReimbursementRejectionDto().getRejectionLetterRemarks2());
		}
		
		if(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId() != null
				&& ReferenceTable.RELATION_SHIP_SELF_KEY.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getInsuredPatient().getRelationshipwithInsuredId().getKey())
				&& ((SHAConstants.DEATH_FLAG).equalsIgnoreCase(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getIncidenceFlagValue())
				|| ReferenceTable.PATIENT_STATUS_DECEASED_REIMB.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()) 
				|| ReferenceTable.PATIENT_STATUS_DECEASED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getPatientStatusId()))
				&& ReferenceTable.RECEIVED_FROM_INSURED.equals(bean.getReimbursementRejectionDto().getReimbursementDto().getDocAcknowledgementDto().getDocumentReceivedFrom().getId())) {
			
			if(bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList() == null ||
					bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList().isEmpty()){
				
				List<LegalHeirDTO> legalHeirList = this.bean.getReimbursementRejectionDto().getReimbursementDto().getLegalHeirDTOList();
				if(legalHeirList != null && !legalHeirList.isEmpty()) {
					Path tempDir = SHAFileUtils.createTempDirectory("dummyHolder");
					ArrayList<File> filelistForMerge = new ArrayList<File>();
					
					for (LegalHeirDTO legalHeirDTO : legalHeirList) {
						bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeName(legalHeirDTO.getHeirName());
						bean.getReimbursementRejectionDto().getReimbursementDto().setNomineeAddr(legalHeirDTO.getAddress()+(legalHeirDTO.getPincode() != null ? ("\nPinCode : "+legalHeirDTO.getPincode()): ""));
						a_beanList = new ArrayList<ReimbursementRejectionDto>();
						a_beanList.add(bean.getReimbursementRejectionDto());
						reportDto.setBeanList(a_beanList);
						filePath = docGenarator.generatePdfDocument(templateName, reportDto);
						try{
							File fl = new File(filePath);
							filelistForMerge.add(fl);
						}
						catch(Exception e) {
//							e.printStackTrace();
						}
					}						
					if(filelistForMerge != null && !filelistForMerge.isEmpty()) {
						File mergedDoc = SHAFileUtils.mergeDocuments(filelistForMerge,tempDir,bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getClaimId().replaceAll("/", "_"));
						filePath =  mergedDoc.getAbsolutePath();
					}
					
				}
				
			}	
			else {
				a_beanList = new ArrayList<ReimbursementRejectionDto>();
				a_beanList.add(bean.getReimbursementRejectionDto());
				reportDto.setBeanList(a_beanList);
				filePath = docGenarator.generatePdfDocument(templateName, reportDto);
				
			}
			
		}	
		else {
			a_beanList = new ArrayList<ReimbursementRejectionDto>();
			a_beanList.add(bean.getReimbursementRejectionDto());
			reportDto.setBeanList(a_beanList);
			filePath = docGenarator.generatePdfDocument(templateName, reportDto);
			
		}		
		
		final String finalFilePath = filePath;
		
		final Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("");
		window.setWidth("800");
		window.setHeight("100%");
		window.setClosable(false);
		window.setModal(true);
		window.center();

		Path p = Paths.get(finalFilePath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(finalFilePath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		e.setHeight("700px");
		
		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				window.close();
				submitDraftRejectionLetter();
				
			}
		});
		
		HorizontalLayout hor = new HorizontalLayout(homeButton);
		hor.setWidth("100%");
		hor.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		
		VerticalLayout mainVertical = new VerticalLayout(e,hor);
		
		window.setContent(mainVertical);
		UI.getCurrent().addWindow(window);
	}
	
	public void addListener(){
		
		
		initiatePedBtn.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				if(bean.getIsPEDInitiatedForBtn()) {
//					alertMessageForPEDInitiate(SHAConstants.PED_RAISE_MESSAGE);
//				} else {
				PreauthDTO preauthDTO = new PreauthDTO();
				ClaimDto claimDto = bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto();
				NewIntimationDto newIntimationDto = bean.getReimbursementRejectionDto().getReimbursementDto().getClaimDto().getNewIntimationDto();
				preauthDTO.setClaimDTO(claimDto);
				preauthDTO.setNewIntimationDTO(newIntimationDto);
				
				Long reimbursementKey = bean.getReimbursementRejectionDto().getReimbursementDto().getKey();
				Long stageKey = bean.getReimbursementRejectionDto().getReimbursementDto().getStageSelectValue().getId();
				
				viewPEDRequestObj = viewPedRequest.get();
				viewPEDRequestObj.initView(preauthDTO, reimbursementKey,newIntimationDto.getKey(), newIntimationDto.getPolicy().getKey(), claimDto.getKey(),stageKey,false);
				viewPEDRequestObj.setPresenterString(SHAConstants.REIMBURSEMENT_SCREEN);
				showPopup(new VerticalLayout(viewPEDRequestObj));
//				}
				
//				showPopup(new VerticalLayout(viewPEDRequestObj));
			}
			
		});
		
	}
	
	private void showPopup(Layout layout) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(layout);
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

	@Override
	public void submitDraftRejectionLetter() {		

		VerticalLayout layout = new VerticalLayout();
		
		Label msg = new Label("<b style = 'color: green;'>Claim record saved successfully !!!.</b>", ContentMode.HTML);
		layout.addComponent(msg);
		layout.setMargin(true);
		layout.setSpacing(true);
		
		Button OkBtn = new Button("Draft Rejection Letter Home");
		OkBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		OkBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
			toolbar.countTool();
			fireViewEvent(MenuItemBean.PA_DRAFT_REJECTION_LETTER, null);
			ConfirmDialog dialog = (ConfirmDialog)event.getButton().getParent().getParent().getParent();
			dialog.close();
			}
		});
		
		layout.addComponent(OkBtn);
		layout.setComponentAlignment(OkBtn, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setSizeUndefined();
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
				
	}		
	

}
