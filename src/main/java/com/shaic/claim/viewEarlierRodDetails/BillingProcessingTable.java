package com.shaic.claim.viewEarlierRodDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewBillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewPABillSummaryPage;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewBillDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.dto.HospitalisationDTO;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasRoomRentLimit;
import com.shaic.domain.MasterService;
import com.shaic.domain.Product;
import com.shaic.domain.RODBillDetails;
import com.shaic.domain.RODDocumentSummary;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class BillingProcessingTable extends GBaseTable<ViewClaimStatusDTO> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ViewBillSummaryPage viewBillSummaryPage;
	
	@Inject
	private ViewPABillSummaryPage viewPABillSummaryPage;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private DBCalculationService DBCalculationService;
	
	@EJB
	private MasterService masterService;
	
	@Inject
	private Instance<ViewBillDetailsTable> viewBillDetailsTableInstance;
	
	@EJB
	private CreateRODService billDetailsService;
	
	private ViewBillDetailsTable viewBillDetailsTableObj;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	////private static Window popup;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"sno","billingType","billingDate","billAssessmentAmt","status"};*/

	/*public static final Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno", "rodNumber","billingType","benefitCover","docReceivedFrom","rodType","billingDate","billAssessmentAmt","status"};*/
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
	}
	
	public void setPAColumnsForBilling()
	{
		Object[] NATURAL_COL_ORDER_PA = new Object[] {"sno", "rodNumber","billingType","benefitCover","docReceivedFrom","rodType","billingDate","billAssessmentAmt","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER_PA);
		table.setColumnHeader("benefitCover", "Benefit/Cover");
		table.setColumnHeader("rodNumber", "ROD No");
		table.setColumnHeader("rodType", "ROD Type");
		table.setColumnHeader("billingType", "Claim Type");
		table.setColumnHeader("docReceivedFrom", "Document Received From");
		table.removeGeneratedColumn("viewBillDetails");
		table.addGeneratedColumn("viewBillDetails",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						Button button = new Button("View Bill Details");
						
				    	ViewClaimStatusDTO dto = (ViewClaimStatusDTO) itemId;
				    	
				    	final Long rodKey = dto.getRodKey();
				    	
				    	button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								if(rodKey != null){
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								viewBillDetailsTableObj = viewBillDetailsTableInstance.get();
								viewBillDetailsTableObj.init();
								viewBillDetailsTableObj.setReferenceData(referenceData);
								setBillDetailsTable(rodKey);
								popup.setContent(viewBillDetailsTableObj);
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

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		table.removeGeneratedColumn("viewBillSummary");
		table.addGeneratedColumn("viewBillSummary",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						Button button = new Button("View Bill Summary");
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	
				    	ViewClaimStatusDTO dto = (ViewClaimStatusDTO) itemId;
				    	
				    	final Long rodKey = dto.getRodKey();
				    	
				    	button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								viewPABillSummaryPage.init(rodKey);
									Panel mainPanel = new Panel(viewPABillSummaryPage);
							        mainPanel.setWidth("2000px");
									Window popup = new com.vaadin.ui.Window();
									// popup.setCaption("Pre-auth Details");
									popup.setWidth("75%");
									popup.setHeight("90%");
									popup.setContent(mainPanel);
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

						});
				    	
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
	
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<ViewClaimStatusDTO>(
				ViewClaimStatusDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"sno","rodNumber","typeOfClaim","billClassification","docReceivedFrom","rodType"/*,"billingType"*/,"billingDate","billAssessmentAmt","status"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");

		table.removeGeneratedColumn("viewBillDetails");
		table.addGeneratedColumn("viewBillDetails",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						Button button = new Button("View Bill Details");
						
				    	ViewClaimStatusDTO dto = (ViewClaimStatusDTO) itemId;
				    	
				    	final Long rodKey = dto.getRodKey();
				    	
				    	button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								if(rodKey != null){
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								viewBillDetailsTableObj = viewBillDetailsTableInstance.get();
								viewBillDetailsTableObj.init();
								viewBillDetailsTableObj.setReferenceData(referenceData);
								setBillDetailsTable(rodKey);
								popup.setContent(viewBillDetailsTableObj);
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

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		table.removeGeneratedColumn("viewBillSummary");
		table.addGeneratedColumn("viewBillSummary",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, Object itemId,
							Object columnId) {
						Button button = new Button("View Bill Summary");
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	
				    	ViewClaimStatusDTO dto = (ViewClaimStatusDTO) itemId;
				    	
				    	final Long rodKey = dto.getRodKey();
				    	
				    	button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
                               Reimbursement reimbursement = masterService.getReimbursementDetailsByKey(rodKey);
								
								Intimation intimation = reimbursement.getClaim().getIntimation();
								
								NewIntimationDto intimationDto = intimationService.getIntimationDto(intimation);
								PreauthDTO preauthDto = new PreauthDTO();
								preauthDto.setNewIntimationDTO(intimationDto);
								preauthDto = getProrataFlagFromProduct(preauthDto);
								if(rodKey != null){
									//added by noufel fro GMC prop CR
									if(!(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
											|| preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
										DBCalculationService.getBillDetailsSummary(reimbursement.getKey());
									} 
									else if((preauthDto.getProrataDeductionFlag() != null && preauthDto.getProrataDeductionFlag().equalsIgnoreCase("N")) &&
											(preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)
											|| preauthDto.getNewIntimationDTO().getPolicy().getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))){
										DBCalculationService.getBillDetailsSummary(reimbursement.getKey());
									}
								}
								
								
								viewBillSummaryPage.init(preauthDto,rodKey,true);
									Panel mainPanel = new Panel(viewBillSummaryPage);
							        mainPanel.setWidth("2000px");
							        Window popup = new com.vaadin.ui.Window();
									// popup.setCaption("Pre-auth Details");
									popup.setWidth("75%");
									popup.setHeight("90%");
									popup.setContent(mainPanel);
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

						});
				    	
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
	}

	@Override
	public void tableSelectHandler(ViewClaimStatusDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "view-billing-processing-table-";
	}
	
	public void setBillDetailsTable(Long rodKey){
		
//	    rodKey = 5029l;
		
		List<RODDocumentSummary> rodDocumentSummaryList = billDetailsService.getBillDetailsByRodKey(rodKey);
		int sno=1;
		for (RODDocumentSummary rodDocumentSummary : rodDocumentSummaryList) {
			Long documentSummaryKey = rodDocumentSummary.getKey();
			
			List<RODBillDetails> rodBillDetails = billDetailsService.getBilldetailsByDocumentSummayKey(documentSummaryKey);
		
			if(rodBillDetails != null){
				List<HospitalisationDTO> hospitalisationList = new ArrayList<HospitalisationDTO>();
				int i=1;
				for (RODBillDetails rodBillDetails2 : rodBillDetails) {
					HospitalisationDTO dto = new HospitalisationDTO();
					if(i==1){
						dto.setSno(sno);
						dto.setRodNumber(rodDocumentSummary.getReimbursement().getRodNumber());
						if(null != rodDocumentSummary.getFileType()){
						dto.setFileType(rodDocumentSummary.getFileType().getValue());
						}
						dto.setFileName(rodDocumentSummary.getFileName());
						dto.setBillNumber(rodDocumentSummary.getBillNumber());
						if(null != rodDocumentSummary.getBillDate()){
							
						String formatDate = SHAUtils.formatDate(rodDocumentSummary.getBillDate());
						dto.setBillDate(formatDate);
						}
						dto.setNoOfItems(rodDocumentSummary.getNoOfItems());
						dto.setBillValue(rodDocumentSummary.getBillAmount());
						sno++;
					}
					dto.setItemNo(i);
					dto.setItemName(rodBillDetails2.getItemName());
					if(null != rodBillDetails2.getBillClassification()){
						dto.setClassification(rodBillDetails2.getBillClassification().getValue());
					}
					if(null != rodBillDetails2.getBillCategory()){
						dto.setCategory(rodBillDetails2.getBillCategory().getValue());
					}
					if(rodBillDetails2.getNoOfDaysBills() != null){
						dto.setNoOfDays(rodBillDetails2.getNoOfDaysBills().longValue());
					}

					if(null != rodBillDetails2.getPerDayAmountBills()){
						dto.setPerDayAmt(rodBillDetails2.getPerDayAmountBills().longValue());
					}
					if(null != rodBillDetails2.getClaimedAmountBills()){
						dto.setClaimedAmount(rodBillDetails2.getClaimedAmountBills().longValue());
					}
					if(null != rodBillDetails2.getNoOfDaysPolicy()){
						dto.setEntitlementNoOfDays(rodBillDetails2.getNoOfDaysPolicy().longValue());
					}
					if(null != rodBillDetails2.getPerDayAmountPolicy()){
						dto.setEntitlementPerDayAmt(rodBillDetails2.getPerDayAmountPolicy().longValue());
					}
					if(null != rodBillDetails2.getTotalAmount()){
						dto.setAmount(rodBillDetails2.getTotalAmount().longValue());
					}
					if(null != rodBillDetails2.getDeductibleAmount()){
						dto.setDeductionNonPayableAmount(rodBillDetails2.getPayableAmount().longValue());
					}
					if(null != rodBillDetails2.getClaimedAmountBills() && null != rodBillDetails2.getPayableAmount()){
						Double claimedAmount = rodBillDetails2.getClaimedAmountBills();
						Double disAllowance = rodBillDetails2.getPayableAmount();
						Double netAmount = claimedAmount - disAllowance;
						dto.setPayableAmount(netAmount.longValue());
					}
					dto.setReason(rodBillDetails2.getNonPayableReason());
					dto.setMedicalRemarks(rodBillDetails2.getMedicalRemarks());
					if(null != rodBillDetails2.getIrdaLevel1Id()){
						
						SelectValue irdaLevel1ValueByKey = masterService
								.getIRDALevel1ValueByKey(rodBillDetails2.getIrdaLevel1Id());
						if(irdaLevel1ValueByKey != null){
							dto.setIrdaLevel1(irdaLevel1ValueByKey.getValue());
						}
					}
					if(null != rodBillDetails2.getIrdaLevel2Id()){
						SelectValue irdaLevel2ValueByKey = masterService
								.getIRDALevel2ValueByKey(rodBillDetails2.getIrdaLevel2Id());
						if(irdaLevel2ValueByKey != null){
							dto.setIrdaLevel2(irdaLevel2ValueByKey.getValue());
						}
					}
					if(null != rodBillDetails2.getIrdaLevel3Id()){
						SelectValue irdaLevel3ValueByKey = masterService
								.getIRDALevel3ValueByKey(rodBillDetails2.getIrdaLevel3Id());
						if(irdaLevel3ValueByKey != null){
							dto.setIrdaLevel3(irdaLevel3ValueByKey.getValue());
						}
					}
					
					
					hospitalisationList.add(dto);
					i++;
				}
	            for (HospitalisationDTO hospitalisationDTO : hospitalisationList) {
//	            	hospitalisationDTO.setSno(sno);
					viewBillDetailsTableObj.addBeanToList(hospitalisationDTO);
//					sno++;
					
				}
			}
		}
				
		}
	
	private PreauthDTO getProrataFlagFromProduct(PreauthDTO preauthDTO)
	{
		Product product = masterService.getProrataForProduct(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey());
		if(null != product)
		{
			preauthDTO.setProrataDeductionFlag(null != product.getProrataFlag() ? product.getProrataFlag() : null);
			/**
			 * product based variable is added to enable or disable the component in page level.
			 * This would be static. -- starts
			 * */
			preauthDTO.setProductBasedProRata(null != product.getProrataFlag() ? product.getProrataFlag() : null);
			preauthDTO.setProductBasedPackage(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : null);
			//ends.
			preauthDTO.setPackageAvailableFlag(null != product.getPackageAvailableFlag() ? product.getPackageAvailableFlag() : null);
			
			//added for CR GLX2020069 GMC prorata calculation
			if(product.getCode() != null && (product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_PRODUCT_CODE) || product.getCode().equalsIgnoreCase(ReferenceTable.STAR_GMC_NBFC_PRODUCT_CODE))){
				Double sumInsured = DBCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
				MasRoomRentLimit gmcProrataFlag = intimationService.getMasRoomRentLimitbySuminsured(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),sumInsured);
				if(gmcProrataFlag != null && gmcProrataFlag.getProportionateFlag() != null){
					preauthDTO.setProrataDeductionFlag(null != gmcProrataFlag.getProportionateFlag() ? gmcProrataFlag.getProportionateFlag() : null);	
					preauthDTO.setProductBasedProRata(null != gmcProrataFlag.getProportionateFlag() ? gmcProrataFlag.getProportionateFlag() : null);
				}else {
					preauthDTO.setProrataDeductionFlag("N");	
					preauthDTO.setProductBasedProRata("N");
				}
			}
		}
		return preauthDTO;
	}

}
