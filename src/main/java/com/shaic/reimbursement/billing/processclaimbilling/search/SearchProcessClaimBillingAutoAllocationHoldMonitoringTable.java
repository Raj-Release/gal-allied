package com.shaic.reimbursement.billing.processclaimbilling.search;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.EJB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremBonusDetails;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.financialapprover.processclaimfinance.search.SearchProcessClaimFinancialsTableDTO;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class SearchProcessClaimBillingAutoAllocationHoldMonitoringTable extends GBaseTable<SearchHoldMonitorScreenTableDTO>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2373637272388561555L;
	private final Logger log = LoggerFactory.getLogger(SearchProcessClaimBillingAutoAllocationHoldMonitoringTable.class);
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNumber","reqDate","type","leg","holdBy","holdDate","holdRemarks"};
	private String screenName;

	@EJB
	private IntimationService intimationService;

	@EJB
	private SearchProcessClaimBillingAutoAllocationService searchService;

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<SearchHoldMonitorScreenTableDTO>(SearchHoldMonitorScreenTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("380px");
		table.addStyleName("generateColumnTable");
		table.removeGeneratedColumn("holdRemarks");
		table.removeGeneratedColumn("actions");
		table.addGeneratedColumn("holdRemarks", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button moreBtn;
				final TextArea holdRemarks;

				holdRemarks = new TextArea();
				holdRemarks.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				holdRemarks.setData(itemId);				
				holdRemarks.setWidth("500px");
				holdRemarks.setRows(2);
				holdRemarks.setMaxLength(110);
				SearchHoldMonitorScreenTableDTO data = (SearchHoldMonitorScreenTableDTO) itemId;
				if(null != data){
					holdRemarks.setReadOnly(Boolean.FALSE);
					holdRemarks.setValue(data.getHoldRemarks());
					holdRemarks.setReadOnly(Boolean.TRUE);
				}

				moreBtn = new Button("MORE");
				moreBtn.setEnabled(true);
				moreBtn.setData(itemId);
				moreBtn.setWidth("60px");
				moreBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);

				HorizontalLayout remarksLayout = new HorizontalLayout();
				remarksLayout.addComponents(holdRemarks,moreBtn);
				remarksLayout.setSpacing(Boolean.FALSE);

				moreBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						getHoldRemarks(holdRemarks);
					} 
				});
				return remarksLayout;
			}
		});

		table.addGeneratedColumn("actions", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button releaseBtn;

				releaseBtn = new Button("RESUME");
				releaseBtn.setEnabled(true);
				releaseBtn.setData(itemId);
				releaseBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {

						Object currentItemId = event.getButton().getData();
						SearchHoldMonitorScreenTableDTO data = (SearchHoldMonitorScreenTableDTO) itemId;
						if(data !=null){
							setTableDtoValues(data);
							table.removeItem(currentItemId);
						}
					} 
				});
				releaseBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				return releaseBtn;
			}
		});


	}

	@Override
	public void tableSelectHandler(SearchHoldMonitorScreenTableDTO t) {
		// TODO Auto-generated method stub


	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "hold-moniter-screen-";
	}

	public void setTableDtoValues(SearchHoldMonitorScreenTableDTO holdMonitorDto){
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		SearchProcessClaimBillingTableDTO preauthDto = new SearchProcessClaimBillingTableDTO();
		List<SearchProcessClaimBillingTableDTO> tableDTO = new ArrayList<SearchProcessClaimBillingTableDTO>();
		String errorMessage = "";		
		PremBonusDetails getBonusDetails = null;
		String chequeStatus = null;
		String get64vbStatus = null;
		Map<String, Object> wrkFlowMap = (Map<String, Object>) holdMonitorDto.getDbOutArray();
		Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
		String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
		String policyNo = (String) wrkFlowMap.get(SHAConstants.POLICY_NUMBER);
		String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
		Long rodKey = (Long) wrkFlowMap.get(SHAConstants.PAYLOAD_ROD_KEY);
		Long claimKey = (Long) wrkFlowMap.get(SHAConstants.DB_CLAIM_KEY);
		Long productKey = (Long) wrkFlowMap.get(SHAConstants.PRODUCT_KEY);

		preauthDto.setKey(rodKey);
		preauthDto.setClaimKey(claimKey);
		preauthDto.setDbOutArray(holdMonitorDto.getDbOutArray());
		preauthDto.setIsAutoAllocationTrue(true);

		ImsUser imsUser = (ImsUser)getUI().getSession().getAttribute(BPMClientContext.USER_OBJECT);
		String usrName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		//		preauthDto.setIsAutoAllocationTrue(imsUser);
		preauthDto.setUsername(usrName);
		preauthDto.setPassword(passWord);
		preauthDto.setIsAutoAllocationTrue(Boolean.TRUE);
		tableDTO = (searchService.getIntimationData(intimationNo, rodKey, usrName));
		preauthDto = tableDTO.get(0);

		preauthDto.setKey(rodKey);
		preauthDto.setClaimKey(claimKey);
		preauthDto.setDbOutArray(holdMonitorDto.getDbOutArray());
		preauthDto.setIsAutoAllocationTrue(true);
		if(preauthDto.getProductKey() != null && ! ReferenceTable.getGMCProductList().containsKey(preauthDto.getProductKey())){
			 getBonusDetails=PremiaService.getInstance().getBonusDetails(preauthDto.getPolicyNo());
			 if(getBonusDetails != null){
				 intimationService.updateCumulativeBonusFromWebService(getBonusDetails);
				  chequeStatus=getBonusDetails.getChequeStatus(); 
			 }
		   }
		  
		
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			 if(preauthDto.getProductKey() != null && ReferenceTable.getGMCProductList().containsKey(preauthDto.getProductKey())){
				   get64vbStatus = PremiaService.getInstance().get64VBStatus(preauthDto.getPolicyNo(), preauthDto.getIntimationNo());
		    }
				else{
					get64vbStatus = chequeStatus;
			}
			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Dishonoured";
				alertMessage(preauthDto, errorMessage);
			} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
				errorMessage = "Cheque Status is Pending";
				alertMessage(preauthDto, errorMessage);
			} else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Due";
				alertMessage(preauthDto, errorMessage);
			}else {
				
				VaadinSession session = getSession();				
				//Boolean activeHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
				Boolean isActiveHumanTask = false;
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				DBCalculationService dbCalculationService = new DBCalculationService();
				String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
				String[] lockSplit = callLockProcedure.split(":");
				String sucessMsg = lockSplit[0];
				String userName = lockSplit[1];
				
				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}
				
				try{
				
					if(isActiveHumanTask){
						SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
						preauthDto.setScreenName(SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);
						fireViewEvent(MenuPresenter.SHOW_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN, preauthDto);
					}else{
						getErrorMessage("This record is already opened by another user");
					}
				}catch(Exception e){
					Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
					Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
					SHAUtils.releaseHumanTask(preauthDto.getUsername(), preauthDto.getPassword(), existingTaskNumber, session);
					dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
					e.printStackTrace();
				}				
			}
		} else {
			
			VaadinSession session = getSession();
			
			//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			Boolean isActiveHumanTask = false;
			String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			DBCalculationService dbCalculationService = new DBCalculationService();
			String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
			String[] lockSplit = callLockProcedure.split(":");
			String sucessMsg = lockSplit[0];
			String userName = lockSplit[1];
			
			if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
				isActiveHumanTask= true;
			}
			try{
				if(isActiveHumanTask){
					SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
					preauthDto.setScreenName(SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);
					fireViewEvent(MenuPresenter.SHOW_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN, preauthDto);
					
				}else{
					getErrorMessage("This record is already opened by another user");
				}
			}catch(Exception e){
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				SHAUtils.releaseHumanTask(preauthDto.getUsername(), preauthDto.getPassword(), existingTaskNumber, session);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				e.printStackTrace();
			}
		}

	}

	public void alertMessage(final SearchProcessClaimBillingTableDTO t, String message) {
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
					VaadinSession session = getSession();
					Boolean isActiveHumanTask = false;
					
					Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
					Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
					String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
					String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					DBCalculationService dbCalculationService = new DBCalculationService();
					String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
					String[] lockSplit = callLockProcedure.split(":");
					String sucessMsg = lockSplit[0];
					String userName = lockSplit[1];
					
					if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
						isActiveHumanTask= true;
					}
					try{
					
						if(isActiveHumanTask){							
							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							t.setScreenName(SHAConstants.PROCESS_CLAIM_BILLING_AUTO_ALLOCATION);
							fireViewEvent(MenuPresenter.SHOW_MEDICAL_APPROVAL_CLAIM_BILLING_SCREEN, t);
							
						}else{
							getErrorMessage("This record is already opened by another user");
						}
					}catch(Exception e){
						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
						e.printStackTrace();
					}
			}
		});
	}

	public void getErrorMessage(String eMsg){

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


	@SuppressWarnings("deprecation")
	public void getHoldRemarks(final TextArea txtFld)
	{
		VerticalLayout vLayout =  new VerticalLayout();
		vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
		vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
		vLayout.setMargin(true);
		vLayout.setSpacing(true);
		final TextArea txtArea = new TextArea();
		txtArea.setValue(txtFld.getValue());
		txtArea.setNullRepresentation("");
		txtArea.setSizeFull();
		txtArea.setWidth("100%");
		txtArea.setReadOnly(Boolean.TRUE);
		txtArea.setRows(25);

		txtArea.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				txtFld.setValue(((TextArea)event.getProperty()).getValue());
				SearchHoldMonitorScreenTableDTO mainDto = (SearchHoldMonitorScreenTableDTO)txtFld.getData();
				if(null != mainDto){
					mainDto.setHoldRemarks(txtFld.getValue());
				}
			}
		});
		Button okBtn = new Button("OK");
		okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		vLayout.addComponent(txtArea);
		vLayout.addComponent(okBtn);
		vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

		final Window dialog = new Window();

		String strCaption = "Hold Remarks";
		dialog.setCaption(strCaption);
		dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
		dialog.setWidth("45%");
		dialog.setClosable(true);

		dialog.setContent(vLayout);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.setDraggable(true);
		dialog.setData(txtFld);

		dialog.addCloseListener(new Window.CloseListener() {
			@Override
			public void windowClose(CloseEvent e) {
				dialog.close();
			}
		});

		if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
			dialog.setPositionX(450);
			dialog.setPositionY(500);
		}
		getUI().getCurrent().addWindow(dialog);
		okBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});	

	}

	public void getScreenName (String screenName){
		this.screenName =screenName;
	}

	public String getScreenName() {
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
}
