package com.shaic.claim.adviseonped;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.adviseonped.search.SearchAdviseOnPEDTableDTO;
import com.shaic.claim.pedrequest.SelectPEDRequestListener;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.viewEarlierRodDetails.RewardRecognitionRequestView;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

@Default
public class AdviseOnPEDViewImpl extends AbstractMVPView implements
		AdviseOnPEDView, SelectPEDRequestListener {

	private static final long serialVersionUID = -9182066388418239095L;

	@Inject
	private AdviseOnPedTable pedRequestDetailsList;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private ClaimDto claimDto;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	@Inject 
	private Instance<AdviseOnPedPage> adviseOnPedPageInstance;
	
	private AdviseOnPedPage adviseOnpedPagePageObj;
	
	private NewIntimationDto intimationDto;
	
	private SearchAdviseOnPEDTableDTO searchDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	private PreauthDTO preauthDTO = null;
	
	private RRCDTO rrcDTO;
	
	public AdviseOnPEDViewImpl() {
	}

	@PostConstruct
	public void initView() {
	}

	@SuppressWarnings("unchecked")
	public void initView(SearchAdviseOnPEDTableDTO searchDTO,OldPedEndorsementDTO editDTO) {

		this.searchDTO = searchDTO;
		this.rrcDTO = searchDTO.getRrcDTO();
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(searchDTO.getRrcDTO().getNewIntimationDTO().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		pedRequestDetailsList.init("PED Request Details",false, true);
		pedRequestDetailsList.setHeight("250px");
		
		fireViewEvent(AdviseOnPEDPresenter.SET_FIRST_TABLE, searchDTO);

		pedRequestDetailsList.addListener(this);
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		preauthDTO.setNewIntimationDTO(this.rrcDTO.getNewIntimationDTO());
		
		adviseOnpedPagePageObj=adviseOnPedPageInstance.get();
		adviseOnpedPagePageObj.initView(searchDTO, editDTO);
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"PED Request Details");

	    Panel tablePanel=new Panel(pedRequestDetailsList);
	    //tablePanel.setWidth("100%");
	    tablePanel.setHeight("180px");
	    tablePanel.setWidth("100%");

		VerticalSplitPanel mainsplitPanel = new VerticalSplitPanel();
		mainsplitPanel.setCaption("Advice On PED");
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();

		intimationDetailsCarousel.init(this.intimationDto,this.claimDto,"Advice On PED", searchDTO.getDiagnosis());
		mainsplitPanel.setFirstComponent(intimationDetailsCarousel);
		

		/*VerticalLayout verticalLayout = new VerticalLayout(viewDetails,
				pedRequestDetailsList,
				adviseOnpedPagePageObj);*/
		HorizontalLayout hLayout =new HorizontalLayout(commonButtonsLayout(),viewDetails);
		hLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		hLayout.setSizeFull();
		VerticalLayout verticalLayout = new VerticalLayout(hLayout,
				pedRequestDetailsList,
				adviseOnpedPagePageObj);
//		verticalLayout.setMargin(true);
		verticalLayout.setWidth("100%");
		verticalLayout.setSpacing(true);
//		verticalLayout.setComponentAlignment(hLayout, Alignment.TOP_RIGHT);
	
		mainsplitPanel.setSecondComponent(verticalLayout);

		mainsplitPanel.setSplitPosition(22, Unit.PERCENTAGE);
		setHeight("100%");
		mainsplitPanel.setSizeFull();
		mainsplitPanel.setHeight("650px");
		
		setCompositionRoot(mainsplitPanel);
	}
	
	public HorizontalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(preauthDTO);
		
		HorizontalLayout alignmentHLayout = new HorizontalLayout(
				btnRRC,hopitalFlag);
		alignmentHLayout.setSpacing(true);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(AdviseOnPEDPresenter.VALIDATE_ADVISE_ON_PED_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}

	@Override
	public void buildValidationUserRRCRequestLayout(Boolean isValid) {
		
			if (!isValid) {
				Label label = new Label("Same user cannot raise request more than once from same stage", ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
			} 
		else
		{
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("");
			popup.setWidth("85%");
			popup.setHeight("100%");
			rewardRecognitionRequestViewObj = rewardRecognitionRequestViewInstance.get();
			//ViewDocumentDetailsDTO documentDetails =  new ViewDocumentDetailsDTO();
			//documentDetails.setClaimDto(bean.getClaimDTO());
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.ADVISE_ON_PED);
			rewardRecognitionRequestViewObj.init(preauthDTO, popup);
			
			//earlierRodDetailsViewObj.init(bean.getClaimDTO().getKey(),bean.getKey());
			popup.setCaption("Reward Recognition Request");
			popup.setContent(rewardRecognitionRequestViewObj);
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
		}
	
	@Override
	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.loadRRCRequestDropDownValues(mastersValueContainer)	;
		
	}
	
 
	@Override
	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		// TODO Auto-generated method stub
		rewardRecognitionRequestViewObj.buildRRCRequestSuccessLayout(rrcRequestNo);
		
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tableRowSelectionListener(OldPedEndorsementDTO item) {
	}

	@Override
	public void list(Page<OldPedEndorsementDTO> tableRows) {
		
		pedRequestDetailsList.setTableList(tableRows.getPageItems());
		pedRequestDetailsList.setRowColor(searchDTO.getKey());
	}

	@Override
	public void setReferenceData(OldPedEndorsementDTO bean,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto,Map<String, Object> referenceData,OldPedEndorsementDTO tableRows,ClaimDto claimDto) {
		
		this.intimationDto=intimationDto;
		this.claimDto=claimDto;
		
		adviseOnpedPagePageObj.setReferenceData(bean, selectValueContainer, intimationDto, referenceData, tableRows, claimDto);
		
	}

	@Override
	public void result() {
		
Label successLabel = new Label("<b style = 'color: gray;'>Specialist Remarks Updated Successfully!!! </b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Advise on PED Home");
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
				fireViewEvent(MenuItemBean.ADVISE_ON_PED, true);
				
			}
		});
		
	}

	@Override
	public void setSearchDtoToTableDto(OldPedEndorsementDTO editDTO) {
		
		adviseOnpedPagePageObj.initView(null,editDTO);
		
	}
	
	@Override
	public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 rewardRecognitionRequestViewObj.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 @Override
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 rewardRecognitionRequestViewObj.setsourceValues(selectValueContainer, source, value);
	 }
}