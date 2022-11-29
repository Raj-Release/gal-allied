package com.shaic.claim.OMPProcessNegotiation.search;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
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

public class OMPProcessNegotiationDetailTable  extends GBaseTable<OMPProcessNegotiationTableDTO>{
	
	
	private final static Object[] NATURAL_HDCOL_ORDER = new Object[]{"serialNumber","rodnumber","intimationNo","claimno","policyno","insuredName","eventcode","ailment"
			}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
	table.setContainerDataSource(new BeanItemContainer<OMPProcessNegotiationTableDTO>(OMPProcessNegotiationTableDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_ORDER);
		table.setHeight("250px");
		
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


	 public void alertMessage(final OMPProcessNegotiationTableDTO t, String message) {

	   		Label successLabel = new Label(
					"<b style = 'color: red;'>"+ message + "</b>",
					ContentMode.HTML);

			// Label noteLabel = new
			// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
			// ContentMode.HTML);

			Button homeButton = new Button("ok");
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
//			dialog.setCaption("Alert");
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
					 fireViewEvent(OMPMenuPresenter.OMP_PROCESS_OMP_NEGOTIATION, t);
				}
			});
		}
	
	 protected void tablesize(){
			table.setPageLength(table.size()+1);
			int length =table.getPageLength();
			if(length>=7){
				table.setPageLength(7);
			}
			
		}

		@Override
		public void tableSelectHandler(OMPProcessNegotiationTableDTO t) {
			
			 Boolean isActiveHumanTask = false;
			 VaadinSession session = getSession();
				Map<String, Object> wrkFlowMap = (Map<String, Object>) t.getDbOutArray();
				Long wrkFlowKey = (Long) wrkFlowMap.get(SHAConstants.WK_KEY);
				String currentqueue = (String) wrkFlowMap.get(SHAConstants.CURRENT_Q);
				
				if (getUI() != null && getUI().getSession() != null) {					
				
				String userID =(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				DBCalculationService dbCalculationService = new DBCalculationService();
				SHAUtils.setDBActiveOrDeactiveClaim(wrkFlowKey,session)	;
				String callLockProcedure = dbCalculationService.callOMPLockProcedure(wrkFlowKey, currentqueue, userID);
				String[] lockSplit = callLockProcedure.split(":");
				String sucessMsg = lockSplit[0];
				String userName = lockSplit[1];

				
				if (sucessMsg != null && sucessMsg.equalsIgnoreCase("SUCCESS")){
					isActiveHumanTask= true;
				}try{
					if(isActiveHumanTask){
						
						SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);  
				
						fireViewEvent(OMPMenuPresenter.OMP_PROCESS_NEGOTIATION_DETAIL_PAGE, t);
						
					}else{
						getErrorMessage(callLockProcedure); 
					}
				}catch(Exception e){
					Long lockedWorkFlowKey= (Long)session.getAttribute(SHAConstants.WK_KEY);  
					Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
					SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
					dbCalculationService.callOMPUnlockProcedure(lockedWorkFlowKey);
					e.printStackTrace();
				}
				
				}
			else {
				
				
				if(isActiveHumanTask){
				
					SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
			
					fireViewEvent(OMPMenuPresenter.OMP_PROCESS_NEGOTIATION_DETAIL_PAGE, t);
					
				}else{
//					getErrorMessage(callLockProcedure);  
				}
			}
			
		}
		@Override
		public String textBundlePrefixString() {
		return "ompprocessnegotiation-";
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
		
}
