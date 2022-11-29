package com.shaic.claim.preauth.search.flpautoallocation;

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
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.preauth.search.autoallocation.SearchPreAuthAutoAllocationTable;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.claim.premedical.search.ProcessPreMedicalTableDTO;
import com.shaic.claim.process.premedical.enhancement.search.SearchPreMedicalProcessingEnhancementTableDTO;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.reimbursement.manageclaim.HoldMonitorScreen.SearchHoldMonitorScreenTableDTO;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchHoldFLPAutoAllocationTable extends GBaseTable<SearchHoldMonitorScreenTableDTO>{

	
	private final Logger log = LoggerFactory.getLogger(SearchPreAuthAutoAllocationTable.class);
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNumber","reqDate","type","leg","holdBy","holdDate","holdRemarks"};
	private String screenName;
	
	@EJB
	private FLPSearchAutoAllocationService autoAllocationService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

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
				holdRemarks.setWidth("400px");
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
		    	remarksLayout.setComponentAlignment(moreBtn, Alignment.MIDDLE_CENTER);
		    	
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
			    	    
		Map<String, Object> wrkFlowMap = (Map<String, Object>) holdMonitorDto.getDbOutArray();
		String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
		String intimationNo = (String) wrkFlowMap.get(SHAConstants.INTIMATION_NO);
		Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
		Map<Long,Object> workFlowObj = new HashMap<Long,Object>();
		
		String usrName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		
		List<Claim> claims= autoAllocationService.getClaimsByIntimationNO(intimationNo);
		if(claims !=null && !claims.isEmpty()
				&& claims.get(0)!=null){
			workFlowObj.put(claims.get(0).getKey(), wrkFlowMap);
		}
		
		if(currentqueue.equalsIgnoreCase(SHAConstants.FLP_CURRENT_QUEUE))
		{
			ProcessPreMedicalTableDTO preMedicalTableDTO = autoAllocationService.getPreMedicalDTO(workFlowObj,claims,usrName,passWord);
			flpTableSelectHandler(preMedicalTableDTO,holdMonitorDto);
		}else if(currentqueue.equalsIgnoreCase(SHAConstants.FLE_CURRENT_QUEUE)){
			SearchPreMedicalProcessingEnhancementTableDTO enhancementTableDTO = autoAllocationService.getProcessingEnhancementDTO(workFlowObj,claims,usrName,passWord);
			fleTableSelectHandler(enhancementTableDTO,holdMonitorDto);
		}
						
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
	
	
	public void getHoldRemarks(final TextArea txtFld)
	{
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtArea = new TextArea();
				//txtArea.setStyleName("Boldstyle"); 
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
	
	private void flpTableSelectHandler(ProcessPreMedicalTableDTO t,SearchHoldMonitorScreenTableDTO holdMonitorDto) {

		System.out.println("--the Auto FLP key ---"+t.getKey());

		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		String errorMessage = "";
		VaadinSession session = getSession();

		Boolean isActiveHumanTask = false;
		Map<String, Object> wrkFlowMap = (Map<String, Object>) holdMonitorDto.getDbOutArray();
		Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
		String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
		String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
		String[] lockSplit = callLockProcedure.split(":");
		String sucessMsg = lockSplit[0];

		if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
			isActiveHumanTask= true;
		}

		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());

			if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Dishonoured";
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
				alertMessage(t, errorMessage,holdMonitorDto);
			} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
				errorMessage = "Cheque Status is Pending";
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
				alertMessage(t, errorMessage,holdMonitorDto);
			}else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
				errorMessage = "Cheque Status is Due";
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
				alertMessage(t, errorMessage,holdMonitorDto);
			} else {
				try{
					if(isActiveHumanTask){
						SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
						fireViewEvent(MenuPresenter.SHOW_PREMEDICAL_WIZARD, t);
					}else{
						getErrorMessage(callLockProcedure);
					}
				}catch(Exception e){				
					Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
					Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
					SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
					dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
					e.printStackTrace();
				}			
			}
		} else {

			try{
				if(isActiveHumanTask){
					SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session);
					fireViewEvent(MenuPresenter.SHOW_PREMEDICAL_WIZARD, t);
				}else{
					getErrorMessage(callLockProcedure);
				}
			}catch(Exception e){

				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				e.printStackTrace();
			}
		}

	}

	public void alertMessage(ProcessPreMedicalTableDTO t, String message,SearchHoldMonitorScreenTableDTO holdMonitorDto) {

		final MessageBox msgBox = MessageBox
				.createWarning()
				.withCaptionCust("Warning")
				.withMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();

		Button homeButton=msgBox.getButton(ButtonType.OK);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				msgBox.close();
				VaadinSession session = getSession();
				
				Long lockedWorkFlowKey1= (Long)session.getAttribute(SHAConstants.WK_KEY);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey1);
				Boolean isActiveHumanTask = false;
				Map<String, Object> wrkFlowMap = (Map<String, Object>) holdMonitorDto.getDbOutArray();
				Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);

				String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
				String[] lockSplit = callLockProcedure.split(":");
				String sucessMsg = lockSplit[0];

				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}

				try{
					if(isActiveHumanTask){
						fireViewEvent(MenuPresenter.SHOW_PREMEDICAL_WIZARD, t);
					}else{
						getErrorMessage(callLockProcedure);
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

	private void fleTableSelectHandler(SearchPreMedicalProcessingEnhancementTableDTO t,SearchHoldMonitorScreenTableDTO holdMonitorDto) {

		System.out.println("--the Auto FLE key ---"+t.getKey());
		Boolean isRodNotAvailable = true;

		List<Reimbursement> reimbursementDetails = masterService.getReimbursementDetails(t.getKey());

		for (Reimbursement reimbursement : reimbursementDetails) {

			if(! ReferenceTable.CANCEL_ROD_KEYS.containsKey(reimbursement.getStatus().getKey()) 
					&& ! reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS)
					&& ! reimbursement.getStatus().getKey().equals(ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS)
					&& ! reimbursement.getStatus().getKey().equals(ReferenceTable.PAYMENT_REJECTED)){
				isRodNotAvailable = false;
			}
		}

		Preauth preauth = preauthService.getLatestPreauthDetails(t.getKey());

		if(preauth != null && preauth.getClaim() != null && preauth.getClaim().getClaimType().getKey().equals(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY)){
			getErrorMessage("Enhancement is not applicable. Since claim type is Reimbursement.");
		}else if(preauth != null){

			if(isRodNotAvailable){
				String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
				String errorMessage = "";
				VaadinSession session = getSession();

				Boolean isActiveHumanTask = false;
				Map<String, Object> wrkFlowMap = (Map<String, Object>) holdMonitorDto.getDbOutArray();
				Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
				String[] lockSplit = callLockProcedure.split(":");
				String sucessMsg = lockSplit[0];

				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}

				if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
					String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
					if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
						errorMessage = "Cheque Status is Dishonoured";
						SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
						alertMessage(t, errorMessage,holdMonitorDto);
					} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
						errorMessage = "Cheque Status is Pending";
						SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
						alertMessage(t, errorMessage,holdMonitorDto);
					} else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
						errorMessage = "Cheque Status is Due";
						SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
						alertMessage(t, errorMessage,holdMonitorDto);
					}else {
						try{
							if(isActiveHumanTask){
								SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
								fireViewEvent(MenuPresenter.SHOW_PREMEDICAL_ENHANCEMENT_WIZARD, t);
							}else{
								getErrorMessage(callLockProcedure);
							}
						}catch(Exception e){

							Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
							Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
							SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
							dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
							e.printStackTrace();
						}
					}
				} else {

					try{
						if(isActiveHumanTask){
							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							fireViewEvent(MenuPresenter.SHOW_PREMEDICAL_ENHANCEMENT_WIZARD, t);
						}else{
							getErrorMessage(callLockProcedure);
						}
					}catch(Exception e){
						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
						e.printStackTrace();
					}
				}
			}else{
				getErrorMessage("Enhancement is not applicable, since ROD is available for this Claim");
			}
		}else{
			getErrorMessage("Preauth is Witdrawn. Hence First Level Enhancement is not possible");
		}
	}

	public void alertMessage(final SearchPreMedicalProcessingEnhancementTableDTO t, String message,SearchHoldMonitorScreenTableDTO holdMonitorDto) {

		final MessageBox msgBox = MessageBox
				.createWarning()
				.withCaptionCust("Warning")
				.withMessage(message)
				.withOkButton(ButtonOption.caption(ButtonType.OK.name()))
				.open();

		Button homeButton=msgBox.getButton(ButtonType.OK);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {

				VaadinSession session = getSession();

				Long lockedWorkFlowKey1= (Long)session.getAttribute(SHAConstants.WK_KEY);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey1);
				Boolean isActiveHumanTask = false;
				Map<String, Object> wrkFlowMap = (Map<String, Object>) holdMonitorDto.getDbOutArray();
				Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				String callLockProcedure = dbCalculationService.callLockProcedure(wrkFlowKey, currentqueue, userID);
				String[] lockSplit = callLockProcedure.split(":");
				String sucessMsg = lockSplit[0];

				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}
				msgBox.close();
				try{
					if(isActiveHumanTask){
						fireViewEvent(MenuPresenter.SHOW_PREMEDICAL_ENHANCEMENT_WIZARD, t);
					}else{
						getErrorMessage(callLockProcedure);
					}
				} catch(Exception e){
					e.printStackTrace();
					Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
					dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				}
			}
		});

	}
}
