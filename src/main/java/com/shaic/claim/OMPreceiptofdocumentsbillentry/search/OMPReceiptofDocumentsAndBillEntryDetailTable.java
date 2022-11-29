package com.shaic.claim.OMPreceiptofdocumentsbillentry.search;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPReceiptofDocumentsAndBillEntryDetailTable extends GBaseTable<OMPReceiptofDocumentsAndBillEntryTableDTO>{
	
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
//	@EJB
//	private CreateRODService billDetailsService;
	
	private final static Object[] NATURAL_HDCOL_ORDER = new Object[]{"serialNumber","intimationNo","claimno","policyno","insuredPatientName","lossDate","eventCode","ailment" 
		}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
	table.setContainerDataSource(new BeanItemContainer<OMPReceiptofDocumentsAndBillEntryTableDTO>(OMPReceiptofDocumentsAndBillEntryTableDTO.class));
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
	
	public void alertMessage(final OMPReceiptofDocumentsAndBillEntryTableDTO t, String message) {

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
//		dialog.setCaption("Alert");
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
				 fireViewEvent(OMPMenuPresenter.OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY, t);
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
		public void tableSelectHandler(OMPReceiptofDocumentsAndBillEntryTableDTO t) {
			
			


			String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
			String errorMessage = "";
			if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
				String get64vbStatus = PremiaService.getInstance().get64VBStatus(t.getPolicyno(), t.getIntimationNo());
				if(get64vbStatus != null && SHAConstants.DISHONOURED.equalsIgnoreCase(get64vbStatus)) {
					errorMessage = "Cheque Status is Dishonoured";
					alertMessage(t, errorMessage);
				} else if(get64vbStatus != null && (SHAConstants.PENDING.equalsIgnoreCase(get64vbStatus) || SHAConstants.UNIQUE_64VB_PENDING.equalsIgnoreCase(get64vbStatus))) {
					errorMessage = "Cheque Status is Pending";
					alertMessage(t, errorMessage);
				} else if(get64vbStatus != null && SHAConstants.UNIQUE_64VB_DUE.equalsIgnoreCase(get64vbStatus)) {
					errorMessage = "Cheque Status is Due";
					alertMessage(t, errorMessage);
				}else {
					if(t.getClaimno() != null) {
//						fireViewEvent(MenuItemBean.OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY, t);
					}
					
				}
			} else {
				if(t.getClaimno() != null) {
//					fireViewEvent(MenuItemBean.OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY, t);
				}
				
			}
		
			fireViewEvent(OMPMenuPresenter.SHOW_OMP_RECEIPT_OF_DOCUMENTS_BILL_ENTRY, t);

		}
		@Override
		public String textBundlePrefixString() {
		return "ompreceptofdocandbillentry-";
		}
	
	
}
