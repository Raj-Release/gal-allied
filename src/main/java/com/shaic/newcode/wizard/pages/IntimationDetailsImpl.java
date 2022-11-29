package com.shaic.newcode.wizard.pages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationCreateCarousel;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimatino.view.ViewIntimationDetails;
import com.shaic.claim.intimation.create.ViewPolicyDetailsForIntimation;
import com.shaic.claim.intimation.create.ViewPreviousPolicyDetails;
import com.shaic.claim.policy.search.ui.PremPolicySchedule;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreviousPolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.UnFreezHospitals;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
@ViewScoped
@CDIView(value = MenuItemBean.NEW_INTIMATION)
public class IntimationDetailsImpl extends AbstractMVPView implements IntimationDetailsView,View {
	private static final long serialVersionUID = 1L;

	@EJB
	private ClaimService claimService;
	@EJB
	private PolicyService policyService;
	@EJB
	private MasterService masterService;
	@EJB
	private HospitalService hospitalService;
	@EJB
	private PreviousPolicyService previousPolicyService;
	@EJB
	private CashlessDetailsService cashlessDetailsService;

	@Inject
	private IntimationService intimationService;
	@Inject
	private ViewIntimationDetails viewIntimationDetails;
	@Inject
	private NewIntimationDto bean;
	@Inject
	private IntimationDetailsPage intimationDetailPage;
	@Inject
	private ViewPolicyDetailsForIntimation viewPolicyDetails;
	@Inject
	private ViewDetails viewDetails;

	private Map<String, Object> referenceData = new HashMap<String, Object>();
	private VerticalSplitPanel mainHorizontalSplitPanel;
	private Button viewPreviousPolicyDetailsButton;
	private Button viewPolicyDetailsButton;
	private Button viewPolicyDocument;
	private Button viewPolicySchedule;
	
	
	@Inject
	private Instance<IntimationCreateCarousel> commonCarouselInstance;
	
	private IntimationCreateCarousel intimationCarousel;
//	private HumanTask editHospitalHumanTask;

	@PostConstruct
	public void init() {
		addStyleName("view");
		setSizeFull();
	}

	public void initView(NewIntimationDto newIntimationDto) {
		this.bean = newIntimationDto;
		getComponent();
	}

	private void getComponent() {
		mainHorizontalSplitPanel = new VerticalSplitPanel();
		
		/*toggleBtn.setIcon(FontAwesome.TOGGLE_RIGHT);
		toggleBtn.setStyleName("link");
		toggleBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				boolean flag = !informationPanel.isVisible();
				if(flag) {
					mainHorizontalSplitPanel.setSplitPosition(80, Unit.PERCENTAGE);
					toggleBtn.setIcon(FontAwesome.TOGGLE_RIGHT);
				} else {
					mainHorizontalSplitPanel.setSplitPosition(100, Unit.PERCENTAGE);
					toggleBtn.setIcon(FontAwesome.TOGGLE_LEFT);
				}
				informationPanel.setVisible(flag);
			}
		});*/
		
/*		vPanel = new VerticalLayout();
		vPanel.addComponent(toggleBtn);
		vPanel.setComponentAlignment(toggleBtn, Alignment.BOTTOM_RIGHT);
		vPanel.addComponent(mainHorizontalSplitPanel);*/
		
		intimationCarousel = commonCarouselInstance.get();
		if(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID) != null){
			String userName = (String) UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID);
			bean.setUsername(userName);
		}
		intimationCarousel.init(bean, "Create Intimation");
		intimationDetailPage.init(bean);
		
		Component content = intimationDetailPage.getContent();
		fireViewEvent(IntimationDetailsPresenter.SET_REFERENCE_DATA, bean);
		fireViewEvent(IntimationDetailsPresenter.GET_REASON_FOR_ADMISSION, bean);
		LinkedHashMap<String, String> policyValues = copyPolicyValues(this.referenceData.get("policyValues"));

		HorizontalLayout policyBtnLayout = createViewPolicyDetailsLayout(bean.getPolicy());
		policyBtnLayout.setWidth("100%");
		policyBtnLayout.setMargin(false);
		policyBtnLayout.setSpacing(false);
		
/*		Label captionLbl = new Label("<b style='font-size: 18px; font-weight: 600'>Create Intimation</b>", ContentMode.HTML);
		
		HorizontalLayout captionAndButtonLayout = new HorizontalLayout(policyBtnLayout);
		captionAndButtonLayout.setWidth("100%");
		captionAndButtonLayout.setHeight("40px");
		captionAndButtonLayout.setMargin(false);
		captionAndButtonLayout.setSpacing(false);

		captionAndButtonLayout.setComponentAlignment(captionLbl, Alignment.MIDDLE_LEFT);
		captionAndButtonLayout.setComponentAlignment(policyBtnLayout, Alignment.MIDDLE_RIGHT);*/
//		contentLayout.setSpacing(false);
//		contentLayout.addComponents(captionAndButtonLayout, content);
//		contentLayout.setWidth("80%");
//		contentLayout.setSizeFull();
		intimationDetailPage.setupReferences(referenceData);

		if(this.bean.getStatus() != null && StringUtils.equalsIgnoreCase(this.bean.getStatus().getProcessValue(),"pending")) {
			intimationDetailPage.setHospitalContentEditable(true);
			intimationDetailPage.setIntimationContentEditable(false);
		} else {
			if(this.bean.getHospitalDto() != null && this.bean.getHospitalDto().getHospitalType() != null && StringUtils.containsIgnoreCase(bean.getHospitalDto().getHospitalType().getValue(), "network")) {
				intimationDetailPage.setHospitalContentEditable(false);
			} else {
				intimationDetailPage.setHospitalContentEditable(true);
			}
			intimationDetailPage.setIntimationContentEditable(true);
		}
		
		VerticalLayout mainVLayout = new VerticalLayout(policyBtnLayout, content);
		mainVLayout.setMargin(true);
		mainVLayout.setSpacing(false);
		
		mainHorizontalSplitPanel.setFirstComponent(intimationCarousel);
		mainHorizontalSplitPanel.setSecondComponent(mainVLayout);
		mainHorizontalSplitPanel.setSplitPosition(18, Unit.PERCENTAGE);
		mainHorizontalSplitPanel.setSizeFull();
		mainHorizontalSplitPanel.setWidth("100%");
		
		if(UI.getCurrent().getSession().getAttribute(SHAConstants.ACTION_FOR_CREATE_INTIMATION) != null){
			mainHorizontalSplitPanel.setHeight("450px");
		}else{
			mainHorizontalSplitPanel.setHeight("650px");
		}
		
		
		setCompositionRoot(mainHorizontalSplitPanel);
		setWidth("100%");
		setHeight("100%");
	}

	@Override
	public void resetView() {
	}

	@Override
	public void showIntimationDetails() {
		addStyleName("view");
		setHeight("100%");
		setWidth("100%");
		setCompositionRoot(intimationDetailPage);
		setVisible(true);
	}

	private GridLayout buildInformationPanel(LinkedHashMap<String, String> policyValues) {
		GridLayout headergridLayout = new GridLayout(2, policyValues.entrySet().size());
		//Vaadin8-setImmediate() headergridLayout.setImmediate(true);
		headergridLayout.setWidth("100%");
		headergridLayout.setHeight("-1px");
		headergridLayout.setStyleName("info-panel");
		headergridLayout.setStyleName("readOnlyFields");
		headergridLayout.setSizeFull();
		
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty("Field", String.class, "");
		container.addContainerProperty("Value", String.class, "");

		if (policyValues != null && !policyValues.isEmpty()) {
			for (Map.Entry<String, String> entry : policyValues.entrySet()) {
				if(null != entry.getKey() || entry.getKey().isEmpty()) {
					Item rowItem = container.getItem(container.addItem());
					rowItem.getItemProperty("Field").setValue(entry.getKey());
					rowItem.getItemProperty("Value").setValue(entry.getValue());
				}
			}
		}
		Table table = new Table("Quick View");
		table.setContainerDataSource(container);
		table.setPageLength(container.size());
		
		headergridLayout.addComponent(table, 0, 0);
		headergridLayout.setComponentAlignment(table, Alignment.TOP_LEFT);
		return headergridLayout;
	}

	private HorizontalLayout createViewPolicyDetailsLayout(final Policy policy) {
		viewPolicyDetailsButton = new Button();
		viewPolicyDetailsButton.setCaption("View Policy Details");
		//Vaadin8-setImmediate() viewPolicyDetailsButton.setImmediate(true);
		viewPolicyDetailsButton.setHeight("-1px");
		viewPolicyDetailsButton.setData(bean.getPolicy());
		viewPolicyDetailsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewPolicyDetailsButton.setStyleName(ValoTheme.BUTTON_LINK);
		viewPolicyDetailsButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2206691737881802049L;
			@Override
			public void buttonClick(ClickEvent event) {
				if(((Button)event.getComponent()).getData() != null){
					Policy policy = (Policy)((Button)event.getComponent()).getData();
					viewPolicyDetails.setPolicyServiceAndPolicy(policyService, policy, masterService);
					viewPolicyDetails.initView();
					viewPolicyDetails.setPositionX(130);
					viewPolicyDetails.setPositionY(30);
					viewPolicyDetails.setHeight("400px");
					UI.getCurrent().addWindow(viewPolicyDetails);
				}
				//viewDetails.getPortabilityViewtByPolicyNo(policy);
			}
		});
		
		// View Previous Policy Details Button
		viewPreviousPolicyDetailsButton = new Button();
		viewPreviousPolicyDetailsButton.setCaption("View Previous Policy Details");
		//Vaadin8-setImmediate() viewPreviousPolicyDetailsButton.setImmediate(true);
		viewPreviousPolicyDetailsButton.setHeight("-1px");
		viewPreviousPolicyDetailsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewPreviousPolicyDetailsButton.setStyleName(ValoTheme.BUTTON_LINK);
		viewPreviousPolicyDetailsButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2206691737881802049L;
			@Override
			public void buttonClick(ClickEvent event) {
				ViewPreviousPolicyDetails viewPreviousPolicyDetails = new ViewPreviousPolicyDetails(
						policyService, masterService, intimationService, previousPolicyService, policy);
				viewPreviousPolicyDetails.InitView();
				viewPreviousPolicyDetails.setPositionX(250);
				viewPreviousPolicyDetails.setPositionY(30);
				UI.getCurrent().addWindow(viewPreviousPolicyDetails);
			}
		});
		
		viewPolicyDocument = new Button();
		viewPolicyDocument.setCaption("Download Document");
		//Vaadin8-setImmediate() viewPolicyDocument.setImmediate(true);
		viewPolicyDocument.setHeight("-1px");
		viewPolicyDocument.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewPolicyDocument.setStyleName(ValoTheme.BUTTON_LINK);
		viewPolicyDocument.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2206691737881802049L;
			@Override
			public void buttonClick(ClickEvent event) {
				getViewDocumentByPolicyNo(bean.getPolicySummary().getPolicyNo());
			}
		});
		
		viewPolicySchedule = new Button();
		viewPolicySchedule.setCaption("Policy Schedule");
		//Vaadin8-setImmediate() viewPolicySchedule.setImmediate(true);
		viewPolicySchedule.setHeight("-1px");
		viewPolicySchedule.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		viewPolicySchedule.setStyleName(ValoTheme.BUTTON_LINK);
		viewPolicySchedule.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2206691737881802049L;
			@Override
			public void buttonClick(ClickEvent event) {
				getViewPolicyScheduleByPolicyNumber(bean.getPolicySummary().getPolicyNo());
			}
		});
		
		if((bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_PRODUCT_CODE)
				|| bean.getPolicySummary().getProduct().getCode().equalsIgnoreCase(SHAConstants.GPA_ACCIDENT_CARE_CODE)
				|| ReferenceTable.getGMCProductCodeList().containsKey(bean.getPolicySummary().getProduct().getCode()))){
			viewPolicyDetailsButton.setEnabled(false);
		}
		
//		Label lbl = new Label("");
//		lbl.setWidth("10px");
		
		HorizontalLayout buttonLayout = new HorizontalLayout(viewPolicyDocument,viewPolicySchedule,viewPolicyDetailsButton,viewPreviousPolicyDetailsButton, new Label(""));
		//buttonLayout.setWidth("350px");
		buttonLayout.setSpacing(true);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout(buttonLayout);
		buttonsLayout.setWidth("100%");
		buttonsLayout.setMargin(false);
		buttonsLayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
		return buttonsLayout;
	}

	public void setModel(NewIntimationDto newIntimationDto) {
		this.bean.setInsuredPatient(newIntimationDto.getInsuredPatient());
	}
	
	public void getViewDocumentByPolicyNo(String strPolicyNo) {
		VerticalLayout vLayout = new VerticalLayout();
		
		
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		Long insuredKey = bean.getInsuredKey();
		Insured	insured = intimationService.getInsuredByKey(insuredKey);
			
		String strDmsViewURL = null;
		Policy policyObj = null;
		BrowserFrame browserFrame = null;
	
		if (strPolicyNo != null) {
			policyObj = policyService.getByPolicyNumber(strPolicyNo);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					strDmsViewURL = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
					strDmsViewURL = strDmsViewURL.replace("POLICY", strPolicyNo);
					if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
						strDmsViewURL = strDmsViewURL.replace("MEMBER", insured!=null?String.valueOf(insured.getSourceRiskId()!=null?insured.getSourceRiskId():""):"");		
					}else{
						strDmsViewURL = strDmsViewURL.replace("MEMBER", "");
					}
					getUI().getPage().open(strDmsViewURL, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL));
				}else{
					strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
					String dmsToken = intimationService.createDMSToken(strPolicyNo);
					getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL+dmsToken));
				}
			}
		}
	/*String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		//popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		//popup.center();
		popup.setResizable(false);
		
		popup.setPositionX(50);
		popup.setPositionY(30);
		popup.setHeight("400px");
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
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

		//popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}

	@Override
	public NewIntimationDto getModel() {
		return this.bean;
	}

	@Override
	public void setReferenceData(Map<String, Object> referenceData) {
		for (Map.Entry<String, Object> entry : referenceData.entrySet()) {
			this.referenceData.put(entry.getKey(), entry.getValue());
		}
		intimationDetailPage.setReasonforAdmission(this.referenceData);
	}

	@SuppressWarnings("unchecked")
	private LinkedHashMap<String, String> copyPolicyValues(Object policyValues) {
		LinkedHashMap<String, String> tempPolicyValues = new LinkedHashMap<String, String>();
		for (Map.Entry<String, String> entry : ((LinkedHashMap<String, String>) policyValues).entrySet()) {
			tempPolicyValues.put(entry.getKey(), entry.getValue());
		}
		return tempPolicyValues;
	}

	public void cancelIntimation() {
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ?",
				"No", "Yes", new ConfirmDialog.Listener() {
			private static final long serialVersionUID = 1L;

			public void onClose(ConfirmDialog dialog) {
				if (dialog.isCanceled() && !dialog.isConfirmed()) {
					
					fireViewEvent(MenuItemBean.SEARCH_POLICY, null);
					
					callSearchIntimationPage();
					
/*					if(bean.getKey() == null) {
						fireViewEvent(MenuItemBean.SEARCH_POLICY, null);
					} else {
//						if(editHospitalHumanTask == null) {
//							fireViewEvent(MenuItemBean.SEARCH_INTIMATION, null);
//						} else {
							fireViewEvent(MenuItemBean.EDIT_HOSPITAL_INFORMATION,null);
//						}
					}*/
				}
			}

			
		});
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}

	private VerticalLayout buildSuccessVerticalLayout(GalaxyIntimation intimation) {
		VerticalLayout SuccessverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() SuccessverticalLayout.setImmediate(false);
		SuccessverticalLayout.setWidth("100.0%");
		SuccessverticalLayout.setHeight("530px");
		SuccessverticalLayout.setMargin(false);

		Label successLabel = new Label();
		//Vaadin8-setImmediate() successLabel.setImmediate(false);
		successLabel.setWidth("-1px");
		successLabel.setHeight("-1px");

		SuccessverticalLayout.addComponent(successLabel);
		SuccessverticalLayout.setComponentAlignment(successLabel, new Alignment(24));

		String statusMessage = null;
		String status = intimation.getStatus().getProcessValue();
		if(status.equalsIgnoreCase("Submitted")) {
			status = " has been successfully submitted.";
		} else if(status.equalsIgnoreCase("Drafted")) {
			status = " has got generated and will be available in view intimation with option to edit.";
		} else if(status.equalsIgnoreCase("Pending")) {
			status = " has been successfully generated and pending for submission.";
		}
		statusMessage = "Claim Intimation No " + intimation.getIntimationId() + status;
		successLabel.setValue(statusMessage);
		
		System.out.println("intimation.getKey() = " + intimation.getKey());
		HorizontalLayout successhorizontalLayout = buildSuccessHorizontalLayout(intimation);
		SuccessverticalLayout.addComponent(successhorizontalLayout);
		SuccessverticalLayout.setComponentAlignment(successhorizontalLayout, Alignment.MIDDLE_RIGHT);
		return SuccessverticalLayout;
	}

	private HorizontalLayout buildSuccessHorizontalLayout(GalaxyIntimation intimation) {
		// Common part: create layout
		HorizontalLayout successhorizontalLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() successhorizontalLayout.setImmediate(false);
		successhorizontalLayout.setWidth("-1px");
		successhorizontalLayout.setHeight("-1px");
		successhorizontalLayout.setMargin(true);
		successhorizontalLayout.setSpacing(true);

		Button intimationHomenativeButton = new Button();
		intimationHomenativeButton.setCaption("Intimations Home");
		//Vaadin8-setImmediate() intimationHomenativeButton.setImmediate(false);
		intimationHomenativeButton.setWidth("-1px");
		intimationHomenativeButton.setHeight("-1px");
		intimationHomenativeButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.SEARCH_POLICY, null);
				callSearchIntimationPage();
			}
		});
		successhorizontalLayout.addComponent(intimationHomenativeButton);

		Button intimationViewnativeButton = new Button();
		intimationViewnativeButton.setCaption("View Intimation");
		//Vaadin8-setImmediate() intimationViewnativeButton.setImmediate(false);
		intimationViewnativeButton.setWidth("-1px");
		intimationViewnativeButton.setHeight("-1px");
		intimationViewnativeButton.setData(intimation);
//		if (intimation.getStatus() != null && ! intimation.getStatus().getProcessValue().equalsIgnoreCase("SUBMITTED")) {
//			intimationViewnativeButton.setEnabled(false);
//		}
		successhorizontalLayout.addComponent(intimationViewnativeButton);
		intimationViewnativeButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -8202541282135091981L;

			public void buttonClick(ClickEvent event) {
				GalaxyIntimation intimation = (GalaxyIntimation) event.getButton().getData();
				if (intimation != null) {
					
					viewDetails.getViewGalaxyIntimation(intimation.getIntimationId());
					
/*					Hospitals hospital = policyService.getVWHospitalByKey(intimation.getHospital());

					// Claim a_claim = claimService.getClaimforIntimation(intimation.getKey());
					NewIntimationDto intimationToIntimationDetailsDTO = intimationService.getIntimationDto(intimation);
					if (intimation.getStatus() != null) {
						// ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);
						// final Intimation intimation = intimationService.searchbyIntimationNo(registrationBean);
						if(intimation != null){
							ViewTmpIntimation tmpIntimationByKey = intimationService.getTmpIntimationByKey(intimation.getKey());
							viewIntimationDetails.init(tmpIntimationByKey);
							
							Window popup = new com.vaadin.ui.Window();
							popup.setCaption("View Intimation Details");
							popup.setWidth("75%");
							popup.setHeight("75%");
							popup.setContent(viewIntimationDetails);
							popup.setClosable(true);
							popup.center();
							popup.setResizable(true);
							popup.addCloseListener(new Window.CloseListener() {
								private static final long serialVersionUID = 1L;
								@Override
								public void windowClose(CloseEvent e) {
									System.out.println("Close listener called");
								}
							});
							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
						}	
						
//						ViewIntimationDetails intimationDetails = new ViewIntimationDetails(
//								intimationToIntimationDetailsDTO,
//								hospitalService);
//						UI.getCurrent().addWindow(intimationDetails);
					}*/
				}
			}
		});
		return successhorizontalLayout;
	}
	
	private VerticalLayout buildunSuccessverticalLayout(GalaxyIntimation intimation) {
		VerticalLayout unSuccessverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() unSuccessverticalLayout.setImmediate(false);
		unSuccessverticalLayout.setWidth("100.0%");
		unSuccessverticalLayout.setHeight("530px");
		unSuccessverticalLayout.setMargin(false);

		Label unSuccessLabel = new Label();
		//Vaadin8-setImmediate() unSuccessLabel.setImmediate(false);
		unSuccessLabel.setWidth("-1px");
		unSuccessLabel.setHeight("-1px");

		unSuccessverticalLayout.addComponent(unSuccessLabel);
		unSuccessverticalLayout.setComponentAlignment(unSuccessLabel, new Alignment(24));

		String unsuccess = "Duplicate Intimation!!! Similar intimation with "
				+ intimation.getIntimationId() + " is already available !!";
		unSuccessLabel.setValue(unsuccess);
		unSuccessLabel.addStyleName("errMessage");
		
		HorizontalLayout unSuccesshorizontalLayout = buildunSuccesshorizontalLayout(intimation);
		unSuccessverticalLayout.addComponent(unSuccesshorizontalLayout);
		unSuccessverticalLayout.setComponentAlignment(unSuccesshorizontalLayout, new Alignment(10));
		return unSuccessverticalLayout;
	}
	
	public void showFailure(String msg) {
		VerticalLayout unSuccessverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() unSuccessverticalLayout.setImmediate(false);
		unSuccessverticalLayout.setWidth("100.0%");
		unSuccessverticalLayout.setHeight("530px");
		unSuccessverticalLayout.setMargin(false);

		Label unSuccessLabel = new Label();
		//Vaadin8-setImmediate() unSuccessLabel.setImmediate(false);
		unSuccessLabel.setWidth("-1px");
		unSuccessLabel.setHeight("-1px");

		unSuccessverticalLayout.addComponent(unSuccessLabel);
		unSuccessverticalLayout.setComponentAlignment(unSuccessLabel, new Alignment(24));

		String unsuccess = "Insured Details is not available";
		unSuccessLabel.setValue(unsuccess);
		unSuccessLabel.addStyleName("errMessage");
		setCompositionRoot(unSuccessverticalLayout);
	}

	private HorizontalLayout buildunSuccesshorizontalLayout(GalaxyIntimation intimation) {
		HorizontalLayout unSuccesshorizontalLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() unSuccesshorizontalLayout.setImmediate(false);
		unSuccesshorizontalLayout.setWidth("-1px");
		unSuccesshorizontalLayout.setHeight("-1px");
		unSuccesshorizontalLayout.setMargin(true);
		unSuccesshorizontalLayout.setSpacing(true);

		Button homeunSuccessnativeButton = new Button();
		homeunSuccessnativeButton.setCaption("Intimations Home");
		//Vaadin8-setImmediate() homeunSuccessnativeButton.setImmediate(false);
		homeunSuccessnativeButton.setWidth("-1px");
		homeunSuccessnativeButton.setHeight("-1px");
		homeunSuccessnativeButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.SEARCH_POLICY, null);
			}
		});
		unSuccesshorizontalLayout.addComponent(homeunSuccessnativeButton);

		Button intimationViewunSuccessnativeButton = new Button();
		intimationViewunSuccessnativeButton.setCaption("Intimation Views");
		//Vaadin8-setImmediate() intimationViewunSuccessnativeButton.setImmediate(false);
		intimationViewunSuccessnativeButton.setWidth("-1px");
		intimationViewunSuccessnativeButton.setHeight("-1px");
		intimationViewunSuccessnativeButton.setData(intimation);
		intimationViewunSuccessnativeButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				Intimation intimation = (Intimation) event.getButton().getData();
				if (intimation != null) {
					//UnFreezHospitals hospital = policyService.getUnFreezVWHospitalByKey(intimation.getHospital());
					//NewIntimationDto intimationToIntimationDetailsDTO = intimationService.getIntimationDto(intimation);
					List<Claim> claimByIntimation = claimService.getClaimByIntimation(intimation.getKey());
					//CashlessDetailsDto cashlessDetailsDto = cashlessDetailsService.getCashlessDetails(intimation.getKey());

					final boolean claimExists = (claimByIntimation != null && claimByIntimation.size() > 0) ? true : false;
					if (intimation.getStatus() != null
							&& intimation.getStatus().getProcessValue().equalsIgnoreCase("SUBMITTED")
									&& claimExists == false) {
						if(intimation != null) {
							ViewTmpIntimation tmpIntimationByKey = intimationService.getTmpIntimationByKey(intimation.getKey());
							viewIntimationDetails.init(tmpIntimationByKey);
							
							Window	popup = new com.vaadin.ui.Window();
							popup.setCaption("View Intimation Details");
							popup.setWidth("75%");
							popup.setHeight("75%");
							popup.setContent(viewIntimationDetails);
							popup.setClosable(true);
							popup.center();
							popup.setResizable(false);
							popup.addCloseListener(new Window.CloseListener() {
								private static final long serialVersionUID = 1L;
								@Override
								public void windowClose(CloseEvent e) {
									System.out.println("Close listener called");
								}
							});
							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
						}
					} else {								
						if(intimation != null) {
							ViewTmpIntimation tmpIntimationByKey = intimationService.getTmpIntimationByKey(intimation.getKey());	
							viewIntimationDetails.init(tmpIntimationByKey);
							
							Window	popup = new com.vaadin.ui.Window();
							popup.setCaption("View Intimation Details");
							popup.setWidth("75%");
							popup.setHeight("75%");
							popup.setContent(viewIntimationDetails);
							popup.setClosable(true);
							popup.center();
							popup.setResizable(false);
							popup.addCloseListener(new Window.CloseListener() {
								private static final long serialVersionUID = 1L;
								@Override
								public void windowClose(CloseEvent e) {
									System.out.println("Close listener called");
								}
							});
							popup.setModal(true);
							UI.getCurrent().addWindow(popup);
						}
						/*ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
										intimationToIntimationDetailsDTO,
										intimation.getPolicy().getActiveStatus() == null,
										cashLessTableDetails, cashlessTable,
										cashLessTableMapper,
										newIntimationService, intimation);
								UI.getCurrent().addWindow(intimationStatus);*/
					}
				}

			}
		});
		unSuccesshorizontalLayout.addComponent(intimationViewunSuccessnativeButton);
		return unSuccesshorizontalLayout;
	}

	public void showSuccessPanel(GalaxyIntimation intimation) {
		setCompositionRoot(buildSuccessVerticalLayout(intimation));
	}

	public void showFailurePanel(GalaxyIntimation intimation) {
		setCompositionRoot(buildunSuccessverticalLayout(intimation));
	}

	@Override
	public void buildNewBornBabyTable(Boolean isNewBorn, Map<String, Object> referenceData) {
		intimationDetailPage.buildNewBornBabyTable(isNewBorn, referenceData);
	}

	@Override
	public void setRegisteredHospital(UnFreezHospitals hospital) {
		if (bean.getHospitalDto() != null) {
			bean.getHospitalDto().setRegistedUnFreezHospitals(hospital);
		}
	}

	@Override
	public void handleSave(GalaxyIntimation intimation) {
		bean.setPolicy(intimation.getPolicy());
		bean.setPolicyKey(intimation.getPolicy().getKey());
		bean.setKey(intimation.getKey());
	}

//	public void setEditHospitalHumanTask(HumanTask editHospitalHumanTask){
//		this.editHospitalHumanTask = editHospitalHumanTask;
//		fireViewEvent(IntimationDetailsPresenter.SET_EDIT_HOSPITAL_HUMANTASK, editHospitalHumanTask);
//	}

	@Override
	public void addedReasonForAdmission(SelectValue selectValue) {
		this.intimationDetailPage.addTokenValue(selectValue);
	}

	@Override
	public void intializeReasonForAdmission(BeanItemContainer<SelectValue> selectValue) {
		intimationDetailPage.setReasonForAdmissionValues(selectValue);
	}

	@Override
	public void setSelectedInsured(GmcMainMemberList insured,List<GmcMainMemberList> dependentRisk) {
		bean.setSelectedGmcInsuredList(insured);
		bean.setGmcInsuredList(dependentRisk);
		intimationDetailPage.setGmcInsuredDetails(bean);
		
	}
	
	private void callSearchIntimationPage() {
		if(UI.getCurrent().getSession().getAttribute(SHAConstants.ACTION_FOR_CREATE_INTIMATION) != null){
			String action = (String)UI.getCurrent().getSession().getAttribute(SHAConstants.ACTION_FOR_CREATE_INTIMATION);
			if(action.equalsIgnoreCase("create")){
				String policyNumber = null;
				String healthCardNumber = null;
				
				if(UI.getCurrent().getSession().getAttribute(SHAConstants.SEARCH_POLICY_NUMBER) != null){
					policyNumber = (String)UI.getCurrent().getSession().getAttribute(SHAConstants.SEARCH_POLICY_NUMBER);
				}
				if(UI.getCurrent().getSession().getAttribute(SHAConstants.SEARCH_HEALTH_CARD_NUMBER) != null){
					healthCardNumber = (String) UI.getCurrent().getSession().getAttribute(SHAConstants.SEARCH_HEALTH_CARD_NUMBER);
				}
				if(policyNumber != null || healthCardNumber != null){
					fireViewEvent(MenuItemBean.SEARCH_POLICY_PARAMETER, policyNumber,healthCardNumber);
				}
			}
		}
	}
	
	public void getViewPolicyScheduleByPolicyNumber(String policyNumber) {


		PremPolicySchedule fetchPolicyScheduleFromPremia = policyService
				.fetchPolicyScheduleFromPremia(policyNumber, 0);
		if (fetchPolicyScheduleFromPremia != null
				&& fetchPolicyScheduleFromPremia.getResultUrl() != null) {
			String url = fetchPolicyScheduleFromPremia.getResultUrl();
			// getUI().getPage().open(url, "_blank");
			getUI().getPage().open(url, "_blank", 1550, 650,
					BorderStyle.NONE);
		} else {
			getErrorMessage("Policy Schedule Not Available");
		}

}
	
	public void getErrorMessage(String eMsg) {

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

	

}
