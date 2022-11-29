package com.shaic.reimbursement.rod.createrod.search;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateRODTable extends GBaseTable<SearchCreateRODTableDTO>{

	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","crmFlagged","intimationNo", "claimNo", "policyNo","acknowledgementNumber", 
		"insuredPatientName", "strCpuCode", "claimType", "hospitalName",  "hospitalCity", "dateOfAdmission", "reasonForAdmission","isDocumentUploaded"}; 
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchCreateRODTableDTO>(SearchCreateRODTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("250px");
		
//		table.removeGeneratedColumn("viewProcess");
//		table.addGeneratedColumn("viewProcess",
//				new Table.ColumnGenerator() {
//
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public Object generateCell(Table source, final Object itemId,
//							Object columnId) {
//						Button button = new Button("Audit Image");
//						button.addClickListener(new Button.ClickListener() {
//
//							@Override
//							public void buttonClick(ClickEvent event) {
//
//								SearchCreateRODTableDTO tableDto = (SearchCreateRODTableDTO)itemId;
//								
//								  AuditImageService auditImageService = BPMClientContext.getAuditImageService(tableDto.getUsername(), tableDto.getPassword());
//						    	  
//						    	  if(tableDto.getTaskNumber() != null){    		  
//						    		  SHAUtils.showBPMNProcess(tableDto.getUsername(), tableDto.getTaskNumber().toString());
//						    	  }
//
//							}
//
//						});
//						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
//				    	button.setWidth("150px");
//				    	button.addStyleName(ValoTheme.BUTTON_LINK);
//						return button;
//					}
//				});
//
//		table.setColumnHeader("viewProcess", "Audit Image");
		
	}
	
	  public static BufferedImage  byteArrayToImage(byte[] bytes){  
	        BufferedImage bufferedImage=null;
	        try {
	            InputStream inputStream = new ByteArrayInputStream(bytes);
	            bufferedImage = ImageIO.read(inputStream);
	        } catch (IOException ex) {
	            System.out.println(ex.getMessage());
	        }
	        return bufferedImage;
	}
	
	public void alertMessage(final SearchCreateRODTableDTO t, String message) {

   		Label successLabel = new Label(
				"<b style = 'color: red;'>"+ message + "</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

/*		Button homeButton = new Button("Ok");
		homeButton.setData(t);
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
   		
   		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(message, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK.toString());

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				 //dialog.close();
				 
				 VaadinSession session = getSession();
//					Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
					
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
//				 fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, t);
					try{
						if(isActiveHumanTask){
							//SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
							SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
							fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, t);
						}else{
							getErrorMessage("This record is already opened by another user");
						}
					}catch(Exception e){
//						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
//						if(existingTaskNumber != null){
//							BPMClientContext.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(), existingTaskNumber, SHAConstants.SYS_RELEASE);
//						}
//						System.out.println("Error is occured. Task is to be released");
//						e.printStackTrace();
						
						Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
						Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
						SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
						dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
						e.printStackTrace();
					}
			}
		});
	}

	@Override
	public void tableSelectHandler(
			SearchCreateRODTableDTO t) {
	String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
	String errorMessage = "";
	
	if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
		String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyNo(), t.getIntimationNo());
		if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
			errorMessage = "Cheque Status is Dishonoured";
			alertMessage(t, errorMessage);
		} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
			errorMessage = "Cheque Status is Pending";
			alertMessage(t, errorMessage);
		}  else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
			errorMessage = "Cheque Status is Due";
			alertMessage(t, errorMessage);
		}else {
			
			VaadinSession session = getSession();
//			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			
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
					//SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
					SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
					fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, t);
				}else{
					getErrorMessage("This record is already opened by another user");
				}
			}catch(Exception e){
//				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
//				if(existingTaskNumber != null){
//					BPMClientContext.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(), existingTaskNumber, SHAConstants.SYS_RELEASE);
//				}
//				System.out.println("Error is occured. Task is to be released");
//				e.printStackTrace();
				
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
				e.printStackTrace();
			}
		}
	} else {
		
		VaadinSession session = getSession();
		//Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
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
				//SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
				fireViewEvent(MenuPresenter.SHOW_CREATE_ROD_WIZARD, t);
			}else{
				getErrorMessage("This record is already opened by another user");
			}
		}catch(Exception e){
			/*Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			if(existingTaskNumber != null){
				BPMClientContext.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(), existingTaskNumber, SHAConstants.SYS_RELEASE);
			}
			System.out.println("Error is occured. Task is to be released");
			e.printStackTrace();*/
			Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
			Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);
			SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
			dbCalculationService.callUnlockProcedure(lockedWorkFlowKey);
			e.printStackTrace();
		}
	}
		
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-create-rod-";
	}
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	public void getErrorMessage(String eMsg){
		
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}

}
