package com.shaic.claim.omp.ratechange;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPProcessRODBillEntryService;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.OMPReimbursement;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class OMPClaimRateChangeAndOsUpdationDetailTable extends GBaseTable<OMPClaimRateChangeAndOsUpdationTableDTO>{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@Inject
private OMPClaimRateChangeHistory viewClaimRateDetails;

@EJB
private OMPIntimationService intimationService;
@Inject
private OMPClaimRateChangeHistoryDetailTable claimRateHistory;

private OMPClaimProcessorDTO bean;
@EJB
private OMPProcessRODBillEntryService rodBillentryService;
	
	private final static Object[] NATURAL_HDCOL_ORDER = new Object[]{"serialNumber","intimationNo","rodNo","eventCode","classification","outstandingAmount","intimationDate",
		"conversionRate","history"}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}
	
	@Override
	public void initTable() {
		
	table.setContainerDataSource(new BeanItemContainer<OMPClaimRateChangeAndOsUpdationTableDTO>(OMPClaimRateChangeAndOsUpdationTableDTO.class));
		table.setVisibleColumns(NATURAL_HDCOL_ORDER);
		table.setHeight("250px");
		table.setSizeFull();
		
		table.removeGeneratedColumn("history");
		table.addGeneratedColumn("history", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 

//					ViewDocumentDetailsDTO documentDetails = (ViewDocumentDetailsDTO) itemId;
//					if(null != documentDetails.getStatus()){
//						
//						if(documentDetails.getStatus())
						
					Button button = new Button("History");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							OMPClaimRateChangeAndOsUpdationTableDTO claimRateDto = (OMPClaimRateChangeAndOsUpdationTableDTO)itemId;
							OMPIntimation intimation = new OMPIntimation();
							OMPReimbursement reimbursement = new OMPReimbursement();
//							if(claimRateDto.getClassification()!= null && !claimRateDto.getClassification().equalsIgnoreCase("OMP Other Expenses")){
							Long rodkey = claimRateDto.getKey();
						if(rodkey != null){
							reimbursement = rodBillentryService.getReimbursementByKey(claimRateDto.getKey());
						}
							if (reimbursement != null) {
								viewClaimRateDetails.init(reimbursement);
							
								if (intimation != null) {

//									viewClaimHistoryRequest.showReimbursementClaimHistory(intimation.getKey(),rodKey);
								   
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("History");
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewClaimRateDetails);
									popup.setClosable(true);
									popup.center();
									popup.setResizable(false);
									popup.addCloseListener(new Window.CloseListener() {
										/**
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
//						else{
//									getErrorMessage("History is not available");
//								}
							}
				        
//						}
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
					
					
		      }
		});
		
		table.setColumnHeader("history", "History");
		
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


 public void alertMessage(final OMPClaimRateChangeAndOsUpdationTableDTO t, String message) {

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
				 fireViewEvent(OMPMenuPresenter.OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION, t);
			}
		});
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

 
protected void tablesize(){
	table.setPageLength(table.size()+1);
	int length =table.getPageLength();
	if(length>=7){
		table.setPageLength(7);
	}
	
}

@Override
public void tableSelectHandler(OMPClaimRateChangeAndOsUpdationTableDTO t) {
// TODO Auto-generated method stub
	 fireViewEvent(OMPMenuPresenter.OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION, t);
	
}
@Override
public String textBundlePrefixString() {
return "ompclaimsratechangeandoutstandingupdation-";
}

}
