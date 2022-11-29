package com.shaic.claim.pcc.hrmp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.cvc.auditaction.CVCAuditActionPagePresenter;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionTableDTO;
import com.shaic.claim.pcc.hrmprocessing.HRMProcessing;
import com.shaic.claim.rod.wizard.tables.SectionDetailsListenerTable;
import com.shaic.domain.Claim;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.UserLoginDetails;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class HRMPPageUI extends ViewComponent{

	private VerticalLayout mainLayout;

	private TextField proposerContactNumber;
	private TextField attenderContactNumber;
	private TextArea hrmRemarks;

	private Button submitBtn;

	private Button cancelBtn;

	private Button closeBtn;

	private FormLayout formLayout;

	private VerticalLayout verticalLayout;

	private HorizontalLayout horizontalLayout;

	private VerticalLayout vLayout;

	private HorizontalLayout buttonHorLayout;

	private SearchHRMPTableDTO bean;

	@EJB
	private SearchHRMPService hrmService;

	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;

	@EJB
	private MasterService  masterService;

	@Inject
	private Toolbar toolbar;

	@Inject
	private ViewDetails viewDetails;


	@Inject
	private Instance<SearchHRMPCompletedTable> hRMPCompletedTableObjTable;

	private SearchHRMPCompletedTable hRMPCompletedTableObj;

	public void init(SearchHRMPTableDTO	 bean) {
		this.bean = bean;
		setCompleteLayout();
	}	

	public void setCompleteLayout() {

		if(bean.getTabStatus().equalsIgnoreCase(SHAConstants.HRM_PENDING_TAB)){

			Intimation intimation = hrmService.getIntimationByNo(bean.getIntimationNo());
			ClaimMapper claimMapper = ClaimMapper.getInstance();

			ClaimDto claimDto = claimMapper.getClaimDto(hrmService.getClaimByIntimation(intimation.getKey()));

			NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance()
					.getNewIntimationDto(intimation);

			preauthIntimationDetailsCarousel.init(newIntimationDto, claimDto , "Long Stay Claims");

			viewDetails.initView(newIntimationDto.getIntimationId(),bean.getCashlessKey(), ViewLevels.INTIMATION, false,"Long Stay Claims");

			HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
			viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			viewDetailsLayout.setSpacing(true);
			viewDetailsLayout.setSizeFull();	

			Policy policy = intimation.getPolicy();

			vLayout = new VerticalLayout();
			vLayout.setSpacing(true);
			vLayout.setMargin(true);

			hrmRemarks = new TextArea("HRM Remarks");
			hrmRemarks.setWidth("500px");
			hrmRemarks.setHeight("190px");
			hrmRemarks.setMaxLength(500);
			hrmRemarks.setEnabled(true);
			hrmRemarks.setRequired(true);
			SHAUtils.handleTextAreaPopupDetails(hrmRemarks,null,getUI(),SHAConstants.HRM_PROCESSING_REMARKS);



			proposerContactNumber = new TextField("Proposer Contact Number");
			proposerContactNumber.setValue(policy.getRegisteredMobileNumber());
			proposerContactNumber.setReadOnly(true);

			attenderContactNumber = new TextField("Attender Contact Number");

			if(intimation != null &&intimation.getAttendersMobileNumber() != null){
				attenderContactNumber.setValue(intimation.getAttendersMobileNumber());
			}else if(intimation != null &&intimation.getAttendersLandlineNumber() != null){
				attenderContactNumber.setValue(intimation.getAttendersLandlineNumber());
			}
			attenderContactNumber.setReadOnly(true);

			formLayout = new FormLayout(proposerContactNumber,attenderContactNumber,hrmRemarks);



			horizontalLayout = new HorizontalLayout(formLayout);
			horizontalLayout.setSpacing(true);
			horizontalLayout.setMargin(true);

			verticalLayout = new VerticalLayout(horizontalLayout);
			verticalLayout.setSpacing(false);
			verticalLayout.setMargin(true);


			submitBtn=new Button("Submit");
			cancelBtn=new Button("Cancel");

			submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			submitBtn.setWidth("-1px");
			submitBtn.setHeight("-10px");

			cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
			cancelBtn.setWidth("-1px");
			cancelBtn.setHeight("-10px");

			buttonHorLayout=new HorizontalLayout(submitBtn,cancelBtn);

			buttonHorLayout.setSpacing(true);

			mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel,viewDetailsLayout,verticalLayout,buttonHorLayout);
			mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
			mainLayout.setSpacing(true);

			addListener();

			setCompositionRoot(mainLayout);

		}else{
			Intimation intimation = hrmService.getIntimationByNo(bean.getIntimationNo());
			ClaimMapper claimMapper = ClaimMapper.getInstance();

			ClaimDto claimDto = claimMapper.getClaimDto(hrmService.getClaimByIntimation(intimation.getKey()));

			NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance()
					.getNewIntimationDto(intimation);

			preauthIntimationDetailsCarousel.init(newIntimationDto, claimDto , "Long Stay Claims");

			viewDetails.initView(newIntimationDto.getIntimationId(),bean.getCashlessKey(), ViewLevels.INTIMATION, false,"Long Stay Claims");

			SearchHRMPCompletedTableDTO hrmCompleted = new SearchHRMPCompletedTableDTO();
			hrmCompleted.setIntimationNo(bean.getIntimationNo());
			hrmCompleted.setReferenceNo(bean.getReferenceNo());
			hrmCompleted.setDateOfAdmission(bean.getDateOfAdmission());
			hrmCompleted.setHrmUserId(bean.getHrmUserId());
			if(bean.getHrmObj() != null && bean.getHrmObj().getHrmCompletedDate() != null){
				hrmCompleted.setHrmDate( bean.getHrmObj().getHrmCompletedDate().toString());	
			}
			if(bean.getHrmObj() != null && bean.getHrmObj().getHrmRemarks() != null){
				hrmCompleted.setHrmRemarks(bean.getHrmObj().getHrmRemarks());	
			}
			if(bean.getHrmObj() != null && bean.getHrmObj().getZonalRemarks() != null){
				hrmCompleted.setDivissionHeadRemarks(bean.getHrmObj().getZonalRemarks());	
			}
			if(bean.getHrmObj() != null && bean.getStatus() != null){
				hrmCompleted.setStatus(bean.getStatus());	
			}
			if(bean != null && bean.getDivissionHeadUserId() != null){
				hrmCompleted.setDivissionHeadUserId(bean.getDivissionHeadUserId());	
			}
			if(bean.getHrmObj() != null && bean.getHrmObj().getZonalRemarks() != null){
				hrmCompleted.setDivissionHeadDate(bean.getHrmObj().getZonalCompletedDate().toString());	
				hrmCompleted.setDivissionHeadRemarks(bean.getHrmObj().getZonalRemarks());
			}

			this.hRMPCompletedTableObj = hRMPCompletedTableObjTable.get();
			this.hRMPCompletedTableObj.init("",false,false);
			this.hRMPCompletedTableObj.addBeanToList(hrmCompleted);

			HorizontalLayout viewDetailsLayout = new HorizontalLayout(viewDetails);
			viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
			viewDetailsLayout.setSpacing(true);
			viewDetailsLayout.setSizeFull();	

			closeBtn =new Button("Close");

			closeBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			closeBtn.setWidth("-1px");
			closeBtn.setHeight("-10px");

			buttonHorLayout=new HorizontalLayout(closeBtn);

			buttonHorLayout.setSpacing(true);

			mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel ,viewDetailsLayout,hRMPCompletedTableObj,buttonHorLayout);
			mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
			mainLayout.setSpacing(true);

			addCloseListener();
			setCompositionRoot(mainLayout);
		}

	}

	private void addCloseListener() {
		closeBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(MenuItemBean.HRMP, true);
			}
		});

	}

	private void addListener(){


		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
						"No", "Yes", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (!dialog.isConfirmed()) {
							fireViewEvent(MenuItemBean.HRMP, true);
						} else {
							dialog.close();
						}
					}
				});
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});

		submitBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@SuppressWarnings("deprecation")
			@Override
			public void buttonClick(ClickEvent event) {

				if(hrmRemarks.getValue() == null || hrmRemarks.getValue().isEmpty()){
					SHAUtils.showMessageBox("Please enter the remarks");

				}
				else if(hrmRemarks.getValue() != null && !("").equals(hrmRemarks.getValue()) && hrmRemarks.getValue().length() > 500){
					SHAUtils.showMessageBox("Maximum of 500 characters only allowed for Long Stay Claims Remarks");
				}else{
					Date createdDate = new Timestamp(System.currentTimeMillis());
					String remarkss = hrmRemarks.getValue();
					if(bean.getTabStatus().equalsIgnoreCase(SHAConstants.HRM_PENDING_TAB) && hrmRemarks != null && !hrmRemarks.isEmpty()
							&& ReferenceTable.HRM_COMPLETED_KEY != null && bean.getUserName() != null && !bean.getUserName().isEmpty() ){
						Status status = hrmService.getStatusByKey(ReferenceTable.HRM_COMPLETED_KEY);
						Stage stage = status.getStage();
						if(remarkss != null && status != null && status.getKey() != null  && stage.getKey() != null ){
							HRMProcessing hrmProcessing = hrmService.getHRMprocessingDataByID(bean.getKey());
							hrmProcessing.setHrmRemarks(remarkss);
							hrmProcessing.setStatus(status);
							hrmProcessing.setModifedBy(bean.getLoginId());
							hrmProcessing.setModifiedDate(createdDate);
							hrmProcessing.setStage(stage);
							hrmProcessing.setHrmCompletedDate(createdDate);
							Boolean updateStatus = hrmService.updatePendingHRMDetail(hrmProcessing);
							if(updateStatus){
								resultMsg();
							}
						}

					}
				}
			}
		});


	}

	public void resultMsg() {

		Label successLabel = new Label(
				"<b style = 'color: black;'>Remarks Submitted successfully !!! </b>",
				ContentMode.HTML);

		Button homeButton = new Button("Home Page");
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
				toolbar.countTool();
				fireViewEvent(MenuItemBean.HRMP, true);

			}
		});

	}
	
	public SearchHRMPCompletedTableDTO getHRMDivisionHeadViewDetails(Intimation intimation){
		try {
			Claim claim = hrmService.getClaimByIntimation(intimation.getKey());
			HRMProcessing hrmProcessing = hrmService.getHRMAndDivisionHeadRemarksDetails(intimation.getKey());
			SearchHRMPCompletedTableDTO hrmCompleted = new SearchHRMPCompletedTableDTO();
			hrmCompleted.setIntimationNo(intimation.getIntimationId());
			hrmCompleted.setReferenceNo(claim.getClaimId());
			hrmCompleted.setDateOfAdmission(SHAUtils.getDateFormat(claim.getDataOfAdmission().toString()));
			if(hrmProcessing != null && hrmProcessing.getModifedBy() != null){
				TmpEmployee userLoginDetails = masterService.getUserLoginDetail(hrmProcessing.getModifedBy());
				
				if(userLoginDetails != null && userLoginDetails.getLoginId() != null && userLoginDetails != null && userLoginDetails.getEmpFirstName() != null){
					hrmCompleted.setUserName( userLoginDetails.getLoginId() +" - "+userLoginDetails.getEmpFirstName());	
				}else if(userLoginDetails != null && userLoginDetails.getLoginId() != null){
					hrmCompleted.setUserName( userLoginDetails.getLoginId());	
				}
			}
			
			if(hrmProcessing != null && hrmProcessing.getModifiedDate() != null){
				hrmCompleted.setHrmDate( SHAUtils.getDateFormat(hrmProcessing.getModifiedDate().toString()));	
			}
			if(hrmProcessing != null && hrmProcessing.getHrmRemarks() != null){
				hrmCompleted.setHrmRemarks(hrmProcessing.getHrmRemarks());	
			}
			if(hrmProcessing != null && hrmProcessing.getZonalRemarks() != null){
				hrmCompleted.setDivissionHeadRemarks(hrmProcessing.getZonalRemarks());	
			}
			if(hrmProcessing != null ){
				Status status = hrmProcessing.getStatus();
				if(status != null && status.getKey() != null ){
					hrmCompleted.setStatus(status.getUserStatus());
				}	
			}
			return hrmCompleted;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
