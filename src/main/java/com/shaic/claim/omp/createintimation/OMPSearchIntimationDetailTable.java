package com.shaic.claim.omp.createintimation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.claimhistory.view.ompView.ViewOMPClaimHistoryRequest;
import com.shaic.claim.omp.carousel.OMPNewViewIntimationDetailsUI;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationAndViewDetailsUI;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.OMPMenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
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
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;


public class OMPSearchIntimationDetailTable extends GBaseTable<OMPCreateIntimationTableDTO>{

	private static final long serialVersionUID = -3502494454120278002L;

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"Action","viewDocument","viewTrails","intimationno","policyNo","claimno",
		"insuredName","intimationdateString","intimatername","intimatedby","intimaticmode","hospitalname","status" 
		}; 

	@EJB
	private ClaimService claimService;
	
	@EJB
	private OMPIntimationService intimationService;
	
	@EJB
	private IntimationService intimService;
	
	@Inject
	private ViewOMPClaimHistoryRequest viewClaimHistoryRequest;
	
	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private HospitalService hospitalService;
	
	private Long rodKey;
	
	@Inject
	private RodIntimationAndViewDetailsUI rodIntimationAndViewDetails;
	
	@EJB
	private OMPCreateIntimationService ompIntimationService;
	
	@Inject
	private OMPNewViewIntimationDetailsUI ompnewViewIntimationUI;
	
	//R1276
	private boolean isTPAUserLogin;

	public boolean isTPAUserLogin() {
		return isTPAUserLogin;
	}

	public void setTPAUserLogin(boolean isTPAUserLogin) {
		this.isTPAUserLogin = isTPAUserLogin;
	}
	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@SuppressWarnings("serial")
	@Override
	public void initTable() {
			//R1276
			ImsUser user = (ImsUser)UI.getCurrent().getSession().getAttribute("imsUser");
			isTPAUserLogin = user.getFilteredRoles().contains("CLM_OMP_TPA_INTIMATION");
		
			table.setContainerDataSource(new BeanItemContainer<OMPCreateIntimationTableDTO>(OMPCreateIntimationTableDTO.class));
			
			table.removeGeneratedColumn("Action");
			table.addGeneratedColumn("Action",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source, final Object itemId, Object columnId) {
			
								/*final Button viewIntimationDetailsButton = new Button("View Claim Status");
								{
									viewIntimationDetailsButton.setCaption("View Claim Status");
								}
								viewIntimationDetailsButton.addStyleName(BaseTheme.BUTTON_LINK);
								return viewIntimationDetailsButton;*/
						
						final Button viewIntimationDetailsButton = new Button();
						
						
						//viewIntimationDetailsButton.setData(itemId);
						Long intimationKey = ((OMPCreateIntimationTableDTO) itemId).getKey();

						Claim a_claim = claimService.getClaimforIntimation(intimationKey);
						//R1276 - Commenting the caption condition and displaying the caption as "View Claim Status" all the time.
						/*if (a_claim != null && a_claim.getClaimId() != null){
							viewIntimationDetailsButton.setCaption("View Claim Status");
						} else {
							viewIntimationDetailsButton.setCaption("View Intimation Details");
						}*/
						
						viewIntimationDetailsButton.setCaption("View Claim Status");
						
						viewIntimationDetailsButton.addClickListener(new Button.ClickListener() {
									public void buttonClick(ClickEvent event) {

										OMPCreateIntimationTableDTO newIntimationDto = (OMPCreateIntimationTableDTO) itemId;
										//newIntimationDto.setKey(Long.valueOf("40008628"));
										OMPIntimation a_intimation = intimationService.getIntimationByKey(newIntimationDto.getIntimationKey());
										
										Claim a_claim = claimService.getClaimforIntimation(newIntimationDto.getKey());
										
										if (a_claim != null) {
												//viewClaimStatusAndViewDetails(a_intimation.getIntimationId());
										}
										else if (a_claim == null) {

											
											OMPIntimation intimation =intimationService.getIntimationByNo(newIntimationDto.getIntimationno());
//											ompViewIntimationDetails.init(intimation);
											ompnewViewIntimationUI.initView(newIntimationDto,newIntimationDto.getIntimationno());
//											ompnewViewIntimationUI.initView(dtoBean,dtoBean.getIntimationno());
											final Window	popup = new Window();
											popup.setCaption("View Claim Status");
											popup.setWidth("85%");
											popup.setHeight("85%");
											popup.setClosable(true);
//											popup.setContent(ompnewViewIntimationUI);
											popup.center();
											popup.setModal(true);
											popup.setResizable(false);
											//popup.setContent(buildRegisterSuccessLayout(claim));
											popup.addCloseListener(new Window.CloseListener() {
												private static final long serialVersionUID = 1L;

												@Override
												public void windowClose(CloseEvent e) {
													System.out.println("Close listener called");
													popup.close();
//													fireViewEvent(MenuItemBean.OMP_SEARCHINTIMATION_CREATE, null);
												}
											});
											Button okBtn = new Button("Close");
											okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
											okBtn.addClickListener(new Button.ClickListener() {
												
												@Override
												public void buttonClick(ClickEvent event) {
//													dialog.close();
													popup.close();
												}
											});
											
											VerticalLayout vlayout = new VerticalLayout(ompnewViewIntimationUI,okBtn);
											vlayout.setComponentAlignment(okBtn, Alignment.BOTTOM_CENTER);
											popup.setContent(vlayout);
											UI.getCurrent().addWindow(popup);
											
//											fireViewEvent(MenuItemBean.OMP_SEARCHINTIMATION_CREATE, null);
										
//											viewDetails.getViewIntimation(a_intimation.getIntimationId());
										}
									}
								});
						viewIntimationDetailsButton.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
							}
						});
			
				
			table.removeGeneratedColumn("viewDocument");
			table.addGeneratedColumn("viewDocument",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source, final Object itemId, Object columnId) {
			
						final Button viewIntimationDetailsButton = new Button("View Document Details");
						viewIntimationDetailsButton.addClickListener(new Button.ClickListener() {
									public void buttonClick(ClickEvent event) {
										viewIntimationDetailsButton.setData(itemId);
										Long intimationKey = ((OMPCreateIntimationTableDTO) itemId).getIntimationKey();
										
										OMPCreateIntimationTableDTO ompIntimationDTO = (OMPCreateIntimationTableDTO) itemId;
										//ompIntimationDTO.setKey(Long.valueOf("40008628"));
										//intimationKey = ompIntimationDTO.getKey();
										
										//Claim a_claim = claimService.getClaimforIntimation(intimationKey);										
										//Intimation intimation = intimationService.getIntimationByKey(intimationKey);
										viewUploadedDocumentDetails(ompIntimationDTO.getIntimationno());
									}
								});
						viewIntimationDetailsButton.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});
			
			
			table.removeGeneratedColumn("viewTrails");
			table.addGeneratedColumn("viewTrails",
					new Table.ColumnGenerator() {

						@Override
						public Object generateCell(final Table source,final Object itemId, Object columnId) {
			
							final Button viewTrailsButton = new Button("View Trails");
							viewTrailsButton.addClickListener(new Button.ClickListener() {
								public void buttonClick(ClickEvent event) {
									OMPCreateIntimationTableDTO ompIntimationDTO = (OMPCreateIntimationTableDTO)itemId;
									viewTrailsButton.setData(itemId);

//									final Long intimationKey = (ompIntimationDTO).getIntimationKey();
//									OMPClaim a_claim = ompIntimationService.getClaimforOMPIntimation(intimationKey);
//									OMPIntimation intimation = ompIntimationService.getOMPIntimationByKey(intimationKey);
									viewDetails.getOmpViewClaimHistory(ompIntimationDTO.getIntimationno());
									//getViewClaimandViewDetailsHistory(ompIntimationDTO.getIntimationno());
								}
							});
							if(isTPAUserLogin()){
								viewTrailsButton.setEnabled(false);
							}
							viewTrailsButton.addStyleName(BaseTheme.BUTTON_LINK);
							return viewTrailsButton;
						}
					});
			
			    table.setColumnHeader("Action", "Action");
			    table.setColumnHeader("viewDocument", "View Document");
				table.setColumnHeader("viewTrails", "view Trails");
			//	table.setContainerDataSource(newIntimationDtoContainer);
			//	table.setVisibleColumns(NATURAL_HDCOL_CREATE_ORDER);
				table.setSizeFull();
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
	
	
	 @SuppressWarnings("static-access")
	public void alertMessage(final OMPCreateIntimationTableDTO t, String message) {

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
					 fireViewEvent(OMPMenuPresenter.OMP_SEARCHINTIMATION_CREATE, t);
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
/*
	public void setDraftTableHeader(){
		table.setVisibleColumns(COLUM_HEADER_DRAFT_INTIMATION);
		table.removeGeneratedColumn("Action");
		table.removeGeneratedColumn("viewDocument");
		table.removeGeneratedColumn("viewTrails");
	}
*/
	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setColumnHeader("Action", "Action");
		table.setColumnHeader("viewDocument", "View Document");
		table.setColumnHeader("viewTrails","View Trails");
	}
	
	
	
@Override
public void tableSelectHandler(OMPCreateIntimationTableDTO t) {
	// TODO Auto-generated method stub
	
}
@Override
public String textBundlePrefixString() {
	return "ompsearchIntimation-";
}

//---------------------------------------------------------------- Method need to be corrected as per the -----------------------------------------------------

public void getViewClaimandViewDetailsHistory(String intimationNo) {
	
	if(intimationNo != null){
	OMPIntimation intimation = ompIntimationService.getOMPIntimationByNo(intimationNo);
	Boolean result = true;	
	if (intimation != null) {
		result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation); // this method has to moved to proper place.
		if(result){
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("View History");
				popup.setWidth("75%");
				popup.setHeight("75%");
				popup.setContent(viewClaimHistoryRequest);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
				popup.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});
	
				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
		}else{
				getErrorMessage("Claim is not available");
		}
	 }
	}else{
		getErrorMessage("History is not available");
	}
}

	@SuppressWarnings("static-access")
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
	/*	
	public void viewClaimStatusAndViewDetails(String intimationNo) {

		ViewTmpClaim claim = null;

		try {
			final ViewTmpIntimation intimation = intimationService
					.searchbyIntimationNoFromViewIntimation(intimationNo);
			// Hospitals hospital =
			// hospitalService.searchbyHospitalKey(intimation
			// .getKey());
			claim = claimService.getTmpClaimforIntimation(intimation.getKey());
			if (claim != null) {
				ClaimStatusRegistrationDTO registrationDetails = EarlierRodMapper.getInstance()
						.getRegistrationDetails(claim);
				// Intimation intimation =
				// intimationService.getIntimationByKey(claim.getIntimation().getKey());
				ViewClaimStatusDTO intimationDetails = EarlierRodMapper
						.getViewClaimStatusDto(intimation);
				Hospitals hospitals = hospitalService
						.getHospitalById(intimation.getHospital());
				getHospitalDetails(intimationDetails, hospitals);
				intimationDetails
						.setClaimStatusRegistrionDetails(registrationDetails);
				intimationDetails.setClaimKey(claim.getKey());
				String name = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey()).getName()
						: "";
				intimationDetails.setPatientNotCoveredName(name);
				String relationship = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey())
						.getBabyRelationship().getValue() : "";
				intimationDetails.setRelationshipWithInsuredId(relationship);
				List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService
						.listOfEarlierAckByClaimKey(claim.getKey(), this.rodKey);
				intimationDetails
						.setReceiptOfDocumentValues(listDocumentDetails);

				setPaymentDetails(intimationDetails, claim);

				DocAcknowledgement docAcknowledgement = reimbursementService
						.findAcknowledgment(this.rodKey);
				if (docAcknowledgement != null
						&& docAcknowledgement.getHospitalisationFlag() != null
						&& !docAcknowledgement.getHospitalisationFlag()
								.equalsIgnoreCase("Y")) {
					rodIntimationAndViewDetails
							.init(intimationDetails, this.rodKey);
				} else {
					rodIntimationAndViewDetails.init(intimationDetails, null);
				}
				popup = new com.vaadin.ui.Window();
				popup.setCaption("View Claim Status");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(rodIntimationAndViewDetails);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
				popup.addCloseListener(new Window.CloseListener() {
					*//**
					 * 
					 *//*
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		} catch (Exception e) {
			Notification.show("Claim Number is not generated",
					Notification.TYPE_ERROR_MESSAGE);
		}

		if (claim == null) {
			getErrorMessage("Claim Number is not generated");
		}

	}
	
	private void getHospitalDetails(ViewClaimStatusDTO intimationDetails,
			Hospitals hospitals) {
		ViewClaimStatusDTO hospitalDetails = EarlierRodMapper.getInstance()
				.gethospitalDetails(hospitals);
		intimationDetails.setState(hospitalDetails.getState());
		intimationDetails.setCity(hospitalDetails.getCity());
		intimationDetails.setArea(hospitalDetails.getArea());
		intimationDetails.setHospitalAddress(hospitals.getAddress());
		intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
		intimationDetails.setHospitalTypeValue(hospitalDetails
				.getHospitalTypeValue());
		intimationDetails.setHospitalIrdaCode(hospitalDetails
				.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails
				.getHospitalInternalCode());

	}
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean, ViewTmpClaim claim) {
		try {
			ViewTmpClaimPayment reimbursementForPayment = null;			
			ViewTmpReimbursement settledReimbursement = reimbursementService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){				
			     reimbursementForPayment = reimbursementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
			}else{
				reimbursementForPayment = reimbursementService.getRimbursementForPayment(claim.getClaimId());
			}

			if(reimbursementForPayment != null){
				bean.setBankName(reimbursementForPayment.getBankName());
				bean.setTypeOfPayment(reimbursementForPayment.getPaymentType());
				bean.setAccountName(reimbursementForPayment.getAccountNumber());
				bean.setBranchName(reimbursementForPayment.getBranchName());
				bean.setChequeNumber(reimbursementForPayment.getChequeDDNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setChequeDate(formatDate);
				}
			}
			return reimbursementForPayment;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	public void viewUploadedDocumentDetails(String intimationNo) {
		BPMClientContext bpmClientContext = new BPMClientContext();
		Long dummyno = 1l;
		String dummystrin = "";
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 tokenInputs.put("ompdoc", dummyno.toString());
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo + "&&ompdoc?" + dummyno;*/
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
	}
	
	
}
