package com.shaic.claim.pedquery;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.claim.pedrequest.PEDRequestDetailsTable;
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
import com.vaadin.ui.NativeButton;
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

@Alternative
public class PEDQueryViewImpl extends AbstractMVPView implements
		PEDQueryView, SelectPEDRequestListener {

	private static final long serialVersionUID = -9182066388418239095L;

	@Inject
	private PEDRequestDetailsTable pedRequestDetailsList;
	
	@Inject
	private OldPedEndorsementDTO bean;

//	@Inject
//	private Instance<InitiatePEDEndorsementTable> initiatePEDEndorsementTable;
//	
//	private InitiatePEDEndorsementTable initiatePEDEndorsementObj;
	
	@Inject
	private Instance<PedQueryPage> pedQueryPageInstance;
	
	private PedQueryPage pedQueryObj;
	
	@Inject
	private ViewDetails viewDetails;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;
	
	private SearchPEDQueryTableDTO searchDTO;
	
	//private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	//private Map<String, Object> referenceData;
	
	//private OldPedEndorsementDTO tableRows;
	
	private ClaimDto claimDto;
	
	private NewIntimationDto intimationDto;
	
	//private com.vaadin.ui.Window popupWindow;
	
	private RRCDTO rrcDTO;
	
	@Inject
	private Instance<RewardRecognitionRequestView> rewardRecognitionRequestViewInstance;
	
	private RewardRecognitionRequestView rewardRecognitionRequestViewObj;
	
	PreauthDTO preauthDTO = null;

    public PEDQueryViewImpl() {
		// TODO Auto-generated constructor stub
	}
    
	@PostConstruct
	public void initView() {
	}

	@SuppressWarnings("unchecked")
	public void initView(SearchPEDQueryTableDTO searchDTO,OldPedEndorsementDTO editDTO){
		
		
		this.searchDTO=searchDTO;
		this.rrcDTO = searchDTO.getRrcDTO();
		
		pedRequestDetailsList.init("PED Request Details",false, false);
		
		Pageable pageable = pedRequestDetailsList.getPageable();
		this.bean.setPageable(pageable);
		
		fireViewEvent(PEDQueryPresenter.SET_FIRST_TABLE, searchDTO);
		pedRequestDetailsList.setHeight("250px");
		
		pedQueryObj=pedQueryPageInstance.get();
		pedQueryObj.initView(searchDTO, editDTO);
		
		preauthDTO = new PreauthDTO();
		preauthDTO.setRrcDTO(this.rrcDTO);
		
		VerticalSplitPanel mainsplitPanel = new VerticalSplitPanel();
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance
				.get();

		intimationDetailsCarousel.init(this.intimationDto,this.claimDto,
				"Process PED Query", searchDTO.getDiagnosis());
		mainsplitPanel.setFirstComponent(intimationDetailsCarousel);
		
		viewDetails.initView(this.intimationDto.getIntimationId(), ViewLevels.INTIMATION, false,"Process PED Query");
		
		Panel tableVertical=new Panel(pedRequestDetailsList);
		tableVertical.setHeight("180px");
		
		//VerticalLayout verticalMain=new VerticalLayout(viewDetails,pedRequestDetailsList,pedQueryObj);
		VerticalLayout verticalMain=new VerticalLayout(commonButtonsLayout(),pedRequestDetailsList,pedQueryObj);
	//	verticalMain.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		//verticalMain.setComponentAlignment(commonButtonsLayout(), alignment);
		verticalMain.setSpacing(true);
		
		mainsplitPanel.setSecondComponent(verticalMain);

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
		HorizontalLayout alignmentHLayout = new HorizontalLayout(btnRRC,viewDetails);
		alignmentHLayout.setSizeFull();
		alignmentHLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
		return alignmentHLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(PEDQueryPresenter.VALIDATE_PROCESS_PED_QUERY_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
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
			rewardRecognitionRequestViewObj.initPresenter(SHAConstants.PROCESS_PED_QUERY);
			
			
			
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
	

	private HorizontalLayout buildFooterButton() {
		NativeButton submitBtn = new NativeButton("Submit");
		NativeButton cancelBtn = new NativeButton("Cancel");
		HorizontalLayout footerBtnLayout = new HorizontalLayout(submitBtn,
				cancelBtn);
		footerBtnLayout.setSpacing(true);
		footerBtnLayout.setMargin(true);
		footerBtnLayout.setWidth("100%");
		return footerBtnLayout;
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void tableRowSelectionListener(OldPedEndorsementDTO item) {
		
	}

	@Override
	public void list(Page<OldPedEndorsementDTO> tableRows,OldPedEndorsementDTO bean) {
		
		
		pedRequestDetailsList.setTableList(tableRows.getPageItems());
		pedRequestDetailsList.setRowColor(searchDTO.getKey());
		
	}

	@Override
	public void setReferenceData(OldPedEndorsementDTO bean,BeanItemContainer<SelectValue> selectValueContainer,NewIntimationDto intimationDto,ClaimDto claimDto,BeanItemContainer<SelectValue> pedValueContainer) {
		
		this.bean=bean;
		this.intimationDto=intimationDto;
		this.claimDto=claimDto;
		this.pedQueryObj.setReferenceData(bean, selectValueContainer, pedValueContainer);
	}

	@Override
	public void result(Boolean result) {

Label successLabel = new Label("<b style = 'color: black;'>Claim Record Saved Successfully!!! </b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("PED Query Home");
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
				bean = null;
				searchDTO = null;
				fireViewEvent(MenuItemBean.PROCESS_PED_QUERY,true);
				
			}
		});
		
	}
	
	@Override
	public void setPEDEndorsementTable(OldPedEndorsementDTO tableRows,Map<String, Object> referenceData,BeanItemContainer<SelectValue> pedCodeContainer,BeanItemContainer<SelectValue> icdChapterContainer) {
	
		pedQueryObj.setPEDEndorsementTable(tableRows, referenceData, pedCodeContainer, icdChapterContainer);	
	}

	@Override
	public void setEditReferenceData(Map<String, Object> referenceData) {
			
	}

	@Override
	public void setIcdBlock(BeanItemContainer<SelectValue> icdBlockContainer) {
		pedQueryObj.setIcdBlock(icdBlockContainer);
	}

	@Override
	public void setIcdCode(BeanItemContainer<SelectValue> icdCodeContainer) {
		pedQueryObj.setIcdCode(icdCodeContainer);
	}

	@Override
	public void setPEDCode(String pedCode) {
		pedQueryObj.setPEDCode(pedCode);
	}

	@Override
	public void setReferenceEditData(OldPedEndorsementDTO resultDto,
			BeanItemContainer<SelectValue> selectValueContainer,
			NewIntimationDto intimationDto, ClaimDto claimDto,
			BeanItemContainer<SelectValue> pedValueContainer) {
		pedQueryObj.setReferenceData(resultDto, selectValueContainer, pedValueContainer);
		
	}

	@Override
	public void setSearchDtoToTableDto(OldPedEndorsementDTO editDto) {
		pedQueryObj.initView(null, editDto);
		
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
