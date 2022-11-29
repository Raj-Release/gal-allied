package com.shaic.claim.fvrdetails.view;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jfree.util.Log;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.fvrdetailedview.FvrDetailedViewUI;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ViewFVRDetailsTable extends GBaseTable<ViewFVRDTO> {


	private static Object NATURAL_COL_ORDER[] = new Object[] {"serialNumber",
			/*"representativeName", */"representativeCode",
			/*"representativeContactNo",*/ "hospitalName", "hospitalVisitedDate",
			"remarks", "fvrassignedDate", "fVRreceivedDate", "fVRTAT",
			"fVRStatus", "fvrGrading" };

	private Window popup;

	@Inject
	private ViewFVRGradingUI viewFVRGradingUI;
	
	private ViewFVRDetailsTable instance;
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private FieldVisitRequestService fieldVisitRequestService;

	@Inject
	private FvrDetailedViewUI fvrDetailedViewUI;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewFVRDTO>(
				ViewFVRDTO.class));
		table.setPageLength(5);
		table.setVisibleColumns(NATURAL_COL_ORDER);
		this.instance = this;
		
		try {
			table.removeGeneratedColumn("FVRDetails");
		} catch (Exception e) {

		}
		try {
			table.addGeneratedColumn("FVRDetails",
					new Table.ColumnGenerator() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {
							Button buttonFvrDetails = new Button("FVR Detailed View");
							buttonFvrDetails.addClickListener(new Button.ClickListener() {

										@Override
										public void buttonClick(ClickEvent event) {
											ViewFVRDTO viewFVRFormDTO = (ViewFVRDTO) itemId;
											popup = new com.vaadin.ui.Window();
												fvrDetailedViewUI.init(viewFVRFormDTO, fvrDetailedViewUI);											
												popup.setCaption("");
												popup.setWidth("75%");
												popup.setHeight("75%");
												popup.setContent(fvrDetailedViewUI);
												popup.setClosable(true);
												popup.center();
												popup.setResizable(true);
												popup.addCloseListener(new Window.CloseListener() {
													
													private static final long serialVersionUID = 1L;

													@Override
													public void windowClose(
															CloseEvent e) {
														System.out.println("Close listener called");
													}
												});

												popup.setModal(true);
												UI.getCurrent().addWindow(popup);
										}
									});

							return buttonFvrDetails;
						}
					});
		} catch (Exception e) {

		}

		try {
			table.removeGeneratedColumn("ViewFVRGrading");
		} catch (Exception e) {

		}
		try {
			table.addGeneratedColumn("ViewFVRGrading",
					new Table.ColumnGenerator() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {
							Button buttonFvrGrading = new Button(
									"View FVR Grading");
							buttonFvrGrading
									.addClickListener(new Button.ClickListener() {

										@Override
										public void buttonClick(ClickEvent event) {
											ViewFVRDTO viewFVRFormDTO = (ViewFVRDTO) itemId;
											popup = new com.vaadin.ui.Window();
											viewFVRGradingUI
													.init(viewFVRFormDTO.getKey(), instance, viewFVRFormDTO);											
											popup.setCaption("");
											popup.setWidth("75%");
											popup.setHeight("75%");
											popup.setContent(viewFVRGradingUI);
											popup.setClosable(true);
											popup.center();
											popup.setResizable(true);
											popup.addCloseListener(new Window.CloseListener() {
												/**
								 * 
								 */
												private static final long serialVersionUID = 1L;

												@Override
												public void windowClose(
														CloseEvent e) {
													System.out
															.println("Close listener called");
												}
											});

											popup.setModal(true);
											UI.getCurrent().addWindow(popup);

										}
									});

							return buttonFvrGrading;
						}
					});
		} catch (Exception e) {

		}

		try {
			table.removeGeneratedColumn("ViewDocument");
		} catch (Exception e) {

		}
		try {
			table.addGeneratedColumn("ViewDocument",new Table.ColumnGenerator() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {
							Button button = new Button("View Document");
							
							final ViewFVRDTO viewFVRFormDTO = (ViewFVRDTO) itemId;
							
										
							button.addClickListener(new Button.ClickListener() {

										@Override
										public void buttonClick(ClickEvent event) {
											try{
											List<FieldVisitRequest> fieldVisitRequestByKey = fieldVisitRequestService.getFieldVisitRequestByKey(viewFVRFormDTO.getKey());
											String intimationNo = null;
											if(fieldVisitRequestByKey != null && ! fieldVisitRequestByKey.isEmpty()){
												FieldVisitRequest fieldVisitRequest = fieldVisitRequestByKey.get(0);
												if(fieldVisitRequest != null && fieldVisitRequest.getIntimation() != null
														&& fieldVisitRequest.getIntimation().getIntimationId() != null){
													intimationNo = fieldVisitRequest.getIntimation().getIntimationId();
												}
											}
											
											    if(intimationNo != null){
												  viewUploadedDocumentDetails(intimationNo);
											    }
											}catch(Exception e){
												Log.info("view document exception for FVR");
											}
										}
									});
							
							
							
							return button;
						}
					});
		} catch (Exception e) {

		}
	}

	
	public void viewUploadedDocumentDetails(String intimationNo) {

		DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
				.getDocumentDetailsData(intimationNo, 0);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("View Uploaded Documents");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
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
	
	public void closePopup(){
		if (popup != null) {
			popup.close();
		}
	}

	@Override
	public void tableSelectHandler(ViewFVRDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "FVR-Details-";
	}

}
