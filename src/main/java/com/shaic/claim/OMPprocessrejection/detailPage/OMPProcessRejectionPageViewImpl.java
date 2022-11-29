package com.shaic.claim.OMPprocessrejection.detailPage;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPProcessRejectionPageViewImpl extends AbstractMVPView implements
		OMPProcessRejectionView {

	private static final long serialVersionUID = 1L;

//	@Inject
//	private ViewDetails viewDetails;

	@Inject
	private ProcessRejectionDTO bean;

	@Inject
	private Instance<OMPProcessRejectionPage> processRejectionPageInstance;

	private OMPProcessRejectionPage processRejectionPageObj;

	// @Inject
	// private Instance<OMPProcessRejectionPreauthPage>
	// processRejectionPreauthPageInstance;
	//
	// private OMPProcessRejectionPreauthPage processRejectionPreauthPageObj;

	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;

//	private static BeanItemContainer<SelectValue> selectedValues = null;

	private VerticalLayout mainVertical;	

	private VerticalLayout verticalMain;	

	private VerticalLayout vertical;

	private HorizontalLayout buttonHorLayout;

	private NewIntimationDto intimationDto;

	private FormLayout confirmLayout = new FormLayout();	

	public static Boolean DESICION = false;

	private Boolean isButtonClicked = null;

	private SearchProcessRejectionTableDTO searchDTO;

	@Override
	public String getCaption() {
		return "Process Rejection";
	}

	@PostConstruct
	public void initView() {

	}

	@Override
	public void setReferenceData(SearchProcessRejectionTableDTO bean) {

		this.intimationDto = bean.getIntimationDTO();
		this.searchDTO = bean;
		processRejectionPageObj = processRejectionPageInstance.get();
		processRejectionPageObj.initView(this.searchDTO);
		
		initView(searchDTO);

		// else {
		// this.processRejectionPreauthPageObj.setReferenceData(this.searchDTO.getProcessRejectionDTO(),intimationDto);
		// }

		// this.searchDTO.getProcessRejectionDTO().setRejectionCategory(null);
		// this.searchDTO.getProcessRejectionDTO().setConfirmRemarks(null);
		// this.searchDTO.getProcessRejectionDTO().setMedicalRemarks(null);
		// this.searchDTO.getProcessRejectionDTO().setDoctorNote(null);
		// this.searchDTO.getProcessRejectionDTO().setWaiveRemarks(null);
	}

	public void initView(SearchProcessRejectionTableDTO searchDTO) {
		this.searchDTO = searchDTO;
		this.intimationDto = searchDTO.getIntimationDTO();
		mainVertical = new VerticalLayout();

		

		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();
		intimationDetailsCarousel.initOMPCarousal(this.intimationDto, "Process Rejection");
		mainVertical.addComponent(intimationDetailsCarousel);
		if (processRejectionPageObj != null) {
			verticalMain = new VerticalLayout(processRejectionPageObj);
			verticalMain.setSizeFull();
			
		}
		mainVertical.addComponent(verticalMain);
		mainVertical.setSizeFull();
		setCompositionRoot(mainVertical);
	}

	public void initView(SearchProcessRejectionTableDTO searchDTO,
			ProcessRejectionDTO bean) {

		this.searchDTO = searchDTO;
		this.bean = bean;

	}

//	public VerticalLayout buildLayout() {
//
//		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
//  
//		confirmRejectionBtn = new Button("Confirm Rejection");
//		confirmRejectionBtn.addStyleName("querybtn");
//		waiveRejectionBtn = new Button("Waive Rejection");
//		waiveRejectionBtn.addStyleName("querybtn");
//
//		unbindField(doctorNote);
//		unbindField(waiveProvisionAmt);
//
//		doctorNote = (TextArea) binder.buildAndBind(
//				"Doctor Note(Internal Remarks)", "doctorNote", TextArea.class);
//		doctorNote.setWidth("400px");
//		waiveProvisionAmt = (TextField) binder.buildAndBind("Provision Amount",
//				"waiveProvisionAmt", TextField.class);
//		doctorNote.setNullRepresentation("");
//
//		submitBtn = new Button("Submit");
//		cancelBtn = new Button("Cancel");
//
//		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		submitBtn.setWidth("-1px");
//		submitBtn.setHeight("-10px");
//
//		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
//		cancelBtn.setWidth("-1px");
//		cancelBtn.setHeight("-10px");
//
//		buttonHorLayout = new HorizontalLayout(submitBtn, cancelBtn);
//		buttonHorLayout.setSpacing(true);
//
//		rejectionButtonLayout = new HorizontalLayout(confirmRejectionBtn,
//				waiveRejectionBtn);
//		rejectionButton.setSpacing(true);
//
//		dynamicLayout = new FormLayout();
//
//		if (processRejectionPageObj != null) {
//			vertical = new VerticalLayout(processRejectionPageObj, rejectionButtonLayout,dynamicLayout,buttonHorLayout);
//			vertical.setSizeFull();
//			vertical.setComponentAlignment(rejectionButtonLayout,
//					Alignment.TOP_RIGHT);
//			
//		} else {
//		}
//
//		addListener();
//		vertical.setSpacing(true);
//		vertical.setComponentAlignment(rejectionButton, Alignment.BOTTOM_CENTER);
//		verticalMain = new VerticalLayout(vertical);
//		// verticalMain.setComponentAlignment(buttonHorLayout,
//		// Alignment.BOTTOM_CENTER);
//		verticalMain.setSizeFull();
//		verticalMain.setSpacing(true);
//
//		if (this.searchDTO.getStatusKey() != null
//				&& this.searchDTO.getStatusKey().equals(
//						ReferenceTable.PROCESS_REJECTED)) {
//
//			generateFieldBasedOnConfirmClick(selectedValues);
//
//		}
////		else if (bean.getStatusKey() != null
////				&& this.searchDTO.getStatusKey().equals(
////						ReferenceTable.PREMEDICAL_WAIVED_REJECTION)) {
////
////		}
//
//		if (processRejectionPageObj != null) {
//			vertical = new VerticalLayout(processRejectionPageObj);
//			vertical.setSizeFull();
//			
//		}
//		
//		return verticalMain;
//	}

//	public void addListener() {
//
//		confirmRejectionBtn.addClickListener(new Button.ClickListener() {
//			/**
//				 * 
//				 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				OMPProcessRejectionPageViewImpl.DESICION = false;
//				isButtonClicked = true;
//				fireViewEvent(OMPProcessRejectionPresenter.OMP_CONFIRM_BUTTON,
//						null);
//			}
//		});
//		waiveRejectionBtn.addClickListener(new Button.ClickListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				OMPProcessRejectionPageViewImpl.DESICION = true;
//				isButtonClicked = true;
//				fireViewEvent(OMPProcessRejectionPresenter.OMP_WAIVE_BUTTON,
//						true);
//			}
//		});
//
//		cancelBtn.addClickListener(new Button.ClickListener() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@SuppressWarnings("serial")
//			@Override
//			public void buttonClick(ClickEvent event) {
//
//				ConfirmDialog dialog = ConfirmDialog.show(getUI(),
//						"Confirmation", "Are you sure you want to cancel ?",
//						"No", "Yes", new ConfirmDialog.Listener() {
//
//							public void onClose(ConfirmDialog dialog) {
//								if (!dialog.isConfirmed()) {
//									// Confirmed to continue
//									releaseHumanTask();
//									fireViewEvent(
//											MenuItemBean.SEARCH_OMP_PROCESS_REJECTION,
//											true);
//								} else {
//									// User did not confirm
//								}
//							}
//						});
//
//				dialog.setClosable(false);
//				dialog.setStyleName(Reindeer.WINDOW_BLACK);
//			}
//		});
//
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
//				if (!OMPProcessRejectionPageViewImpl.DESICION) {
//
//					fireViewEvent(OMPProcessRejectionPresenter.OMP_SUBMIT_DATA,
//							searchDTO.getProcessRejectionDTO(), false,
//							SHAConstants.OUTCOME_FLP_NON_MED_CONFIRM_REJECTION,
//							searchDTO, intimationDto);
//
//				} else {
//					fireViewEvent(OMPProcessRejectionPresenter.OMP_SUBMIT_DATA,
//							searchDTO.getProcessRejectionDTO(), true,
//							SHAConstants.OUTCOME_OMP_REG_WAIVE_REJECTION,
//							searchDTO, intimationDto);
//				}
//
//			}
//		});
//
//	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateFieldBasedOnConfirmClick(
			BeanItemContainer<SelectValue> selectedValues) {

		processRejectionPageObj.generateFieldBasedOnConfirmClick(selectedValues);
		
//		// confirmLayout=new
//		// FormLayout(rejectionCategory,confirmRemarks,medicalRemarks,doctorNote);
//
//		this.searchDTO.setStatusKey(ReferenceTable.PROCESS_REJECTED);
//
//		this.selectedValues = selectedValues;
//		unbindField(rejectionCategory);
//		unbindField(confirmRemarks);
//		unbindField(medicalRemarks);
//		unbindField(waiveRemarks);
//
//		rejectionCategory = (ComboBox) binder.buildAndBind(
//				"Rejection Category", "rejectionCategory", ComboBox.class);
//
//		confirmRemarks = (TextArea) binder.buildAndBind("Rejection Remarks",
//				"confirmRemarks", TextArea.class);
//		confirmRemarks.setWidth("400px");
//		if (this.bean.getRejectionRemarks() != null) {
//			confirmRemarks.setValue(this.bean.getRejectionRemarks());
//		}
//		medicalRemarks = (TextArea) binder.buildAndBind("Medical Remarks",
//				"medicalRemarks", TextArea.class);
//		medicalRemarks.setWidth("400px");
//		rejectionCategory.setContainerDataSource(selectedValues);
//		rejectionCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		rejectionCategory.setItemCaptionPropertyId("value");
//
//		if (this.searchDTO.getProcessRejectionDTO().getRejectionCategory() != null) {
//			rejectionCategory.setValue(this.searchDTO.getProcessRejectionDTO()
//					.getRejectionCategory());
//		}
//
//		mandatoryFields.add(rejectionCategory);
//		mandatoryFields.add(confirmRemarks);
//		mandatoryFields.add(medicalRemarks);
//
//		showOrHideValidation(false);
//
//		confirmLayout.removeAllComponents();
//
//		confirmLayout.addComponent(rejectionCategory);
//		confirmLayout.addComponent(confirmRemarks);
//		confirmLayout.addComponent(medicalRemarks);
//		confirmLayout.addComponent(doctorNote);
//
//		waiveLayout.removeAllComponents();
//		dynamicLayout.removeComponent(waiveLayout);
//
//		dynamicLayout.addComponent(confirmLayout);
//		dynamicLayout.setComponentAlignment(confirmLayout,
//				Alignment.BOTTOM_LEFT);
//
		
	}

	@Override
	public void generateFieldBasedOnWaiveClick(Boolean isChecked) {

		processRejectionPageObj.generateFieldBasedOnWaiveClick(isChecked);
//		this.searchDTO.setStatusKey(ReferenceTable.CLAIM_REGISTRATION_WAIVED);
//
//		unbindField(rejectionCategory);
//		unbindField(confirmRemarks);
//		unbindField(medicalRemarks);
//		unbindField(waiveRemarks);
//
//		waiveRemarks = (TextArea) binder.buildAndBind("Remarks",
//				"waiveRemarks", TextArea.class);
//		waiveRemarks.setNullRepresentation("");
//		waiveRemarks.setWidth("400px");
//
//		mandatoryFields.add(waiveRemarks);
//
//		showOrHideValidation(false);
//
//		confirmLayout.removeAllComponents();
//		dynamicLayout.removeComponent(confirmLayout);
//		waiveLayout.removeAllComponents();
//		if (this.searchDTO.getProcessRejectionDTO() != null
//				&& this.searchDTO.getProcessRejectionDTO().getClaimedAmount() != null) {
//			waiveProvisionAmt.setValue(this.searchDTO.getProcessRejectionDTO()
//					.getClaimedAmount().toString());
//			waiveProvisionAmt.setEnabled(false);
//
//		}
//		// waiveProvisionAmt.setReadOnly(true);
//		waiveLayout.addComponent(waiveProvisionAmt);
//		waiveLayout.addComponent(waiveRemarks);
//		dynamicLayout.addComponent(waiveLayout);
//		dynamicLayout.setComponentAlignment(waiveLayout, Alignment.BOTTOM_LEFT);
//
		
	}

	@Override
	public void savedResult() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Claim Record Saved Successfully!!! </b>",
				ContentMode.HTML);

		Button homeButton = new Button("Process Rejection Home");
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
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
//				fireViewEvent(MenuItemBean.SEARCH_OMP_PROCESS_REJECTION, true);

			}
		});
	}

//	@Override
//	public void openPdfFileInWindow(Claim claim, PreauthDTO preauthDTO) {
//
//		DocumentGenerator docGen = new DocumentGenerator();
//		ReportDto reportDto = new ReportDto();
//
//		List<PreauthDTO> preauthDTOList = new ArrayList<PreauthDTO>();
//
//		preauthDTOList.add(preauthDTO);
//		if (claim != null) {
//			reportDto.setClaimId(claim.getClaimId());
//		}
//
//		reportDto.setBeanList(preauthDTOList);
//
//		String filePath = docGen.generatePdfDocument(
//				"RegistrationSuggestRejectionLetter", reportDto);
//
//		final String finalFilePath = filePath;
//
//		final Window window = new Window();
//		// ((VerticalLayout) window.getContent()).setSizeFull();
//		window.setResizable(true);
//		window.setCaption("");
//		window.setWidth("800");
//		window.setHeight("100%");
//		window.setClosable(false);
//		window.setModal(true);
//		window.center();
//
//		Path p = Paths.get(finalFilePath);
//		String fileName = p.getFileName().toString();
//
//		StreamResource.StreamSource s = new StreamResource.StreamSource() {
//
//			public FileInputStream getStream() {
//				try {
//
//					File f = new File(finalFilePath);
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
//		e.setHeight("700px");
//
//		Button homeButton = new Button("Ok");
//		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//
//		homeButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = 7396240433865727954L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				window.close();
//				savedResult();
//
//			}
//		});
//
//		HorizontalLayout hor = new HorizontalLayout(homeButton);
//		hor.setWidth("100%");
//		hor.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
//
//		VerticalLayout mainVertical = new VerticalLayout(e, hor);
//
//		window.setContent(mainVertical);
//		UI.getCurrent().addWindow(window);
//	}	

	// public Component getContent() {
	//
	// mainVertical = new VerticalLayout();
	//
	//
	// VerticalLayout verticalMain=buildLayout();
	//
	// // IntimationDetailsCarousel intimationDetailsCarousel =
	// commonCarouselInstance.get();
	// // intimationDetailsCarousel.init(this.intimationDto,
	// "Process Rejection");
	// // mainVertical.setFirstComponent(intimationDetailsCarousel);
	//
	// mainVertical.addComponent(verticalMain);
	// mainVertical.setSizeFull();
	// mainVertical.setHeight("670px");
	// // setCompositionRoot(mainVertical);
	// return mainVertical;
	// }

	private void releaseHumanTask() {
		VaadinSession session = getSession();
		Long wrkFlowKey = (Long) session.getAttribute(SHAConstants.WK_KEY);
		DBCalculationService dbService = new DBCalculationService();
		if (wrkFlowKey != null) {
			dbService.callOMPUnlockProcedure(wrkFlowKey);
			getSession().setAttribute(SHAConstants.WK_KEY, null);
		}
	}
}
