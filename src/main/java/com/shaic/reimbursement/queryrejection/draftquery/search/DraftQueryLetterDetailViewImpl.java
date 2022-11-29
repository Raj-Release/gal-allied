package com.shaic.reimbursement.queryrejection.draftquery.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.GWizard;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.reimbursement.dto.ReimbursementQueryDto;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
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

public class DraftQueryLetterDetailViewImpl extends AbstractMVPView implements DraftQueryLetterDetailView{

	////private static Window popup;
	
	@Inject
	private Instance<EarlierRodDetailsViewImpl> earlierRodDetailsViemImplInstance;
	
	private EarlierRodDetailsViewImpl earlierRodDetailsViewObj;
	
	@Inject
	private Instance<RevisedCarousel> commonCarouselInstance;
	
	private DraftQueryLetterDetailPage draftQueryLetterDetailPage;
	
	@Inject
	private Instance<DraftQueryLetterDetailPage> draftQueryLetterDetailPageInstance;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Toolbar toolbar;
	
	private VerticalSplitPanel mainPanel;	
	
	private SearchDraftQueryLetterTableDTO bean; 
	
	private String strCaptionString;
	
	@Inject
	private TextBundle tb;	
	
	@PostConstruct
	public void initView() {
		/*arrScreenName = new String[] {
					"documentDetails","acknowledgementReceipt"};*/
		mainPanel = new VerticalSplitPanel();
		draftQueryLetterDetailPage = draftQueryLetterDetailPageInstance.get();
		//captionMap = new HashMap<String, String>();
		addStyleName("view");
		setSizeFull();			
	}
	
	public void initView(SearchDraftQueryLetterTableDTO draftQueryLetterDto)
	{
		this.bean = draftQueryLetterDto;
		draftQueryLetterDetailPage = draftQueryLetterDetailPageInstance.get();
		draftQueryLetterDetailPage.initView(this.bean);
		
		RevisedCarousel intimationDetailsCarousel = commonCarouselInstance.get();
//		intimationDetailsCarousel.init(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto(),  "Draft Query Letter");
		intimationDetailsCarousel.init(this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto(), this.bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto(),  "Draft Query Letter",draftQueryLetterDto.getDiagnosis());
		mainPanel.setFirstComponent(intimationDetailsCarousel);
		
		VerticalLayout wizardLayout1 = new VerticalLayout(commonButtonsLayout());
		
		Panel panel1 = new Panel();
		panel1.setContent(wizardLayout1);
//		panel1.setHeight("50px");
		VerticalLayout wizardLayout2 = new VerticalLayout(panel1, draftQueryLetterDetailPage.getContent());
		wizardLayout2.setSpacing(true);
		mainPanel.setSecondComponent(wizardLayout2);
		
		
		mainPanel.setSizeFull();
		mainPanel.setSplitPosition(26, Unit.PERCENTAGE);
		mainPanel.setHeight("700px");
		setCompositionRoot(mainPanel);
		
			
	}
	
	private VerticalLayout commonButtonsLayout()
	{
		
		TextField acknowledgementNumber = new TextField("Acknowledgement Number");
		acknowledgementNumber.setValue(String.valueOf(this.bean.getReimbursementQueryDto().getReimbursementDto().getDocAcknowledgementDto().getAcknowledgeNumber()));
		
		//Vaadin8-setImmediate() acknowledgementNumber.setImmediate(true);
		acknowledgementNumber.setWidth("250px");
		acknowledgementNumber.setHeight("20px");
		acknowledgementNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		acknowledgementNumber.setReadOnly(true);
		acknowledgementNumber.setEnabled(false);
		acknowledgementNumber.setNullRepresentation("");
		FormLayout hLayout = new FormLayout (acknowledgementNumber);
//		hLayout.setComponentAlignment(acknowledgementNumber, Alignment.MIDDLE_LEFT);
		hLayout.setMargin(false);
		
		Button viewEarlierRODDetails = new Button("View Earlier ROD Details");
		viewEarlierRODDetails.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					
				
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("85%");
				earlierRodDetailsViewObj = earlierRodDetailsViemImplInstance.get();
				earlierRodDetailsViewObj.init(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getKey(),null);
				popup.setContent(earlierRodDetailsViewObj);
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
		
//		FormLayout viewEarlierRODLayout = new FormLayout(viewEarlierRODDetails);
//		
//		FormLayout viewDetailsForm = new FormLayout();
//		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
//		viewDetailsForm.setWidth("-1px");
//		viewDetailsForm.setHeight("-1px");
//		viewDetailsForm.setMargin(false);
//		viewDetailsForm.setSpacing(true);
//		//ComboBox viewDetailsSelect = new ComboBox()
		viewDetails.initView(bean.getReimbursementQueryDto().getReimbursementDto().getClaimDto().getNewIntimationDto().getIntimationId(), bean.getReimbursementQueryDto().getReimbursementDto().getKey(), ViewLevels.PREAUTH_MEDICAL, false,"");
//		viewDetailsForm.addComponent(viewDetails);
//		
//		HorizontalLayout componentsHLayout = new HorizontalLayout(hLayout, viewEarlierRODLayout, viewDetailsForm);
//		componentsHLayout.setWidth("100%");
//		componentsHLayout.setComponentAlignment(viewEarlierRODLayout, Alignment.BOTTOM_RIGHT);
//		return componentsHLayout;
		Label dummylabel =new Label();
		dummylabel.setWidth("270px");
		HorizontalLayout componentsHLayout = new HorizontalLayout(viewEarlierRODDetails, viewDetails);
		componentsHLayout.setWidth("100%");
		componentsHLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		VerticalLayout componentsvertical=new VerticalLayout(componentsHLayout,hLayout);
		return componentsvertical;
	}

	@Override
	public void resetView() {
		
	}
	
	@Override
	public String getCaption() {
		return strCaptionString;
	}
	public void cancelDraftQueryLetter(){
		ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
				"Are you sure you want to cancel ??",
				"No", "Yes", new ConfirmDialog.Listener() {

					private static final long serialVersionUID = 1L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isCanceled() && !dialog.isConfirmed()) {
							// Confirmed to continue
							VaadinSession session = getSession();
							SHAUtils.releaseHumanTask(bean.getUsername(), bean.getPassword(), bean.getTaskNumber(),session);
							
							fireViewEvent(MenuItemBean.DRAFT_QUERY_LETTER_SEARCH, null);
							
						}
					}
		});

		dialog.setStyleName(Reindeer.WINDOW_BLACK);
	}
	
	@Override
	public void deleteQueryDraftOrReDraftLetterRemarks(DraftQueryLetterDetailTableDto deletedRemarksDto){
		this.bean =  draftQueryLetterDetailPage.getupdatedBean();
		
		if(!this.bean.getHasError()){
		
			List<DraftQueryLetterDetailTableDto> deletedList = this.bean.getReimbursementQueryDto().getDeletedList();
			if(deletedList != null && !deletedList.isEmpty() && deletedRemarksDto != null){
				this.bean.getReimbursementQueryDto().getDeletedList().add(deletedRemarksDto);
			}
			else{
				deletedList = new ArrayList<DraftQueryLetterDetailTableDto>();
				deletedList.add(deletedRemarksDto);
				this.bean.getReimbursementQueryDto().setDeletedList(deletedList);
			}
			if(this.bean.getReimbursementQueryDto().getQueryDarftList() != null && ! this.bean.getReimbursementQueryDto().getQueryDarftList().isEmpty()){
				List<DraftQueryLetterDetailTableDto> draftedList = this.bean.getReimbursementQueryDto().getQueryDarftList();
				int remIndex = -1;
				for (DraftQueryLetterDetailTableDto draftQueryLetterDetailTableDto : draftedList) {
					if(draftQueryLetterDetailTableDto.getDraftOrRedraftRemarks().equalsIgnoreCase(deletedRemarksDto.getDraftOrRedraftRemarks())){
						remIndex = this.bean.getReimbursementQueryDto().getQueryDarftList().indexOf(draftQueryLetterDetailTableDto); 
					}
				}
				if(remIndex>=0){
					this.bean.getReimbursementQueryDto().getQueryDarftList().get(remIndex).setDeltedFlag("Y");;
				}			
			}
		}
	}
	
//	public void submitDraftQueryLetter(){
//		draftQueryLetterDetailPage.submitDraftQueryLetter(bean);
//	}
	public void buildSuccessLayout(){
		VerticalLayout layout = new VerticalLayout();
		
		Label msg = new Label("<b style = 'color: green;'>Claim record saved successfully !!!.</b>", ContentMode.HTML);
		layout.addComponent(msg);
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		
		Button OkBtn = new Button("Draft Query Letter Home");
		OkBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		OkBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
			if(bean.getReimbursementQueryDto() != null){
				bean.setReimbursementQueryDto(null);
			}
			
			bean = null;
			toolbar.countTool();
			fireViewEvent(MenuItemBean.DRAFT_QUERY_LETTER_SEARCH, null);
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
		dialog.setSizeUndefined();
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
				
	}

	@Override
	public void init(SearchDraftQueryLetterTableDTO bean) {
		
	}

	@Override
	public Component getContent() {
		Component comp =  draftQueryLetterDetailPage.getContent();
		
		return comp;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		
	}

	@Override
	public boolean onAdvance() {
		bean =  draftQueryLetterDetailPage.getupdatedBean();
		if(!bean.getHasError()){
			return true;
		}
	return false;
		
	}

	@Override
	public boolean onBack() {
		return false;
	}

	@Override
	public boolean onSave() {
		
		ReimbursementQueryDto reimbQueryDto = this.bean.getReimbursementQueryDto();
		fireViewEvent(DraftQueryLetterDetailPresenter.SAVE_DRAFT_QUERY, reimbQueryDto);
		return true;
	}

	@Override
	public void init(SearchDraftQueryLetterTableDTO bean, GWizard wizard) {
		localize(null);
		this.bean = bean;
		draftQueryLetterDetailPage.init(bean,wizard);
	}

	protected void localize(
            @Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
        strCaptionString = tb.getText(textBundlePrefixString() + "decideondraftquery");
    }
	private String textBundlePrefixString()
	{
		return "queryrejection-";
	}	
	
	@Override
	public void returnPreviousPage(SearchDraftQueryLetterTableDTO updatedBean) {
		this.bean = updatedBean;
//		draftQueryLetterDetailPage.setpreviousView(this.bean);
		onBack();		
	}

	@Override
	public SearchDraftQueryLetterTableDTO  getUpdatedBean() {
		bean = draftQueryLetterDetailPage.getupdatedBean();
		
		return bean;
		
	}

	@Override
	public void showSuccessSaveMessage(ReimbursementQueryDto queryDto) {
		this.bean.setReimbursementQueryDto(queryDto);
		draftQueryLetterDetailPage.repaintNomineeTableValues(queryDto.getReimbursementDto().getClaimDto().getNewIntimationDto().getNomineeList());

		VerticalLayout layout = new VerticalLayout();
		Label msg = new Label("<b style = 'color: green;'>Claim record saved successfully !!!.</b>", ContentMode.HTML);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		Button okBtn = new Button("OK");
		okBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		okBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
		
				dialog.close();
				return;
			}
		});
		
		layout.addComponents(msg,okBtn);
		layout.setComponentAlignment(okBtn, Alignment.MIDDLE_CENTER);
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
				
		dialog.setCaption("");
		dialog.setSizeUndefined();
		dialog.setClosable(true);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
	}

	@Override
	public void clearObject() {
		if(draftQueryLetterDetailPage != null){
			draftQueryLetterDetailPage.clearObject();
		}
		bean = null;
	}
}
