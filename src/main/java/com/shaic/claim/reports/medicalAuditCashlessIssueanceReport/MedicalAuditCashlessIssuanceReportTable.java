package com.shaic.claim.reports.medicalAuditCashlessIssueanceReport;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.claim.intimation.create.ViewPreviousPolicyDetails;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreviousPolicyService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.BaseTheme;

public class MedicalAuditCashlessIssuanceReportTable extends GBaseTable<MedicalAuditCashlessIssuanceReportDto> {
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PreviousPolicyService previousPolicyService;
	
	@EJB
	private MasterService masterService;
	
	@Inject
	private ViewPolicyDetails policyViewUI;
	
	@Inject
	private Instance<ViewProductBenefits> viewProductBenefitInstance;
	
	@Inject 
	private ViewDetails viewDetails;
	
	@Inject
	private IntimationService intimationService;
	
	
	private static final Object[] COLUMN_HEADER = new Object[] {
		
		"intimationNo","product","diagnosis","generalRemarks","reBillingOrReQuery",
		"queryRaisedOrMedRejReq","finalRemarks","doctorNote" 	};
		
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<MedicalAuditCashlessIssuanceReportDto>(MedicalAuditCashlessIssuanceReportDto.class));
			table.setVisibleColumns(COLUMN_HEADER);
			
			table.removeGeneratedColumn("dms");
			table.addGeneratedColumn("dms",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
							
							String policyNumber = ((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber();
						
							String intimationNo = ((MedicalAuditCashlessIssuanceReportDto)itemId).getIntimationNo();
							
							final Button viewDMSButton = new Button(
									"DMS View");

							viewDMSButton.setData(intimationNo);
							viewDMSButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											String intimationNo = (String) event
													.getButton()
													.getData();
											
													if(intimationNo != null){
											viewUploadedDocumentDetails(intimationNo);
											}

										}
									});
							viewDMSButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return viewDMSButton;
								 
					}else{
					        return "";
					}	
					}
					});
			
			
			table.removeGeneratedColumn("dashboard");
			table.addGeneratedColumn("dashboard",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
						
							final String intimationNo = ((MedicalAuditCashlessIssuanceReportDto)itemId).getIntimationNo();
							
							final String claimNo = ((MedicalAuditCashlessIssuanceReportDto)itemId).getClaimNumber();
							
							final Button viewDashBoardButton = new Button(
									"DashBoard View");

							viewDashBoardButton.setData(claimNo);
							viewDashBoardButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											String claimNumber = (String) event
													.getButton()
													.getData();
											if(claimNumber != null){
												viewDetails.viewClaimStatusUpdated(intimationNo);
											}
										}
									});
							viewDashBoardButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return viewDashBoardButton;
								 
					}else{
					        return "";
					}	
					}
					});
			
			table.removeGeneratedColumn("audit");
			table.addGeneratedColumn("audit",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
						
							final String intimationNo = ((MedicalAuditCashlessIssuanceReportDto)itemId).getIntimationNo();
							
							final String claimNo = ((MedicalAuditCashlessIssuanceReportDto)itemId).getClaimNumber();
							
							final Button auditButton = new Button(
									"Audit");

							auditButton.setData(claimNo);
							auditButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											String claimNumber = (String) event
													.getButton()
													.getData();
											if(claimNumber != null){
//												audit Link integration to be done
											}
										}
									});
							auditButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return auditButton;
								 
					}else{
					        return "";
					}	
					}
					});
			
			table.removeGeneratedColumn("viewpolicydetails");
			table.addGeneratedColumn("viewpolicydetails",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
						
							final MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto)itemId;
							
							final Button viewPolicyButton = new Button(
									"View Policy Details");

							viewPolicyButton.setData(medicalAuditClaimStatusReportDto);
							viewPolicyButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto) event
													.getButton()
													.getData();
											Policy policyObj = medicalAuditClaimStatusReportDto.getPolicyObj();
											
											if(policyObj != null){
											
											policyViewUI.setPolicyServiceAndPolicy(
													policyService, policyObj, masterService, intimationService);
											policyViewUI.initView();

											UI.getCurrent().addWindow(policyViewUI);
											}
											
										}
									});
							viewPolicyButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return viewPolicyButton;
								 
					}else{
					        return "";
					}	
					}
					});
			
			table.removeGeneratedColumn("previouspolicies");
			table.addGeneratedColumn("previouspolicies",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
						
							final MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto)itemId;
							
							final Button viewPrevPolicyButton = new Button(
									"View Previous Policy Details");

							viewPrevPolicyButton.setData(medicalAuditClaimStatusReportDto);
							viewPrevPolicyButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto) event
													.getButton()
													.getData();
											Policy policyObj = medicalAuditClaimStatusReportDto.getPolicyObj();
											
											if(policyObj != null){
											
											ViewPreviousPolicyDetails viewPreviousPolicyDetails = new ViewPreviousPolicyDetails(
													policyService, masterService, intimationService, previousPolicyService,
													policyObj);
											viewPreviousPolicyDetails.InitView();
											UI.getCurrent().addWindow(viewPreviousPolicyDetails);
											}
											
										}
									});
							viewPrevPolicyButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return viewPrevPolicyButton;
								 
					}else{
					        return "";
					}	
					}
					});
			
			table.removeGeneratedColumn("previousclaims");
			table.addGeneratedColumn("previousclaims",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getIntimationNo() != null){
						
							final MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto)itemId;
							
							final Button viewPrevClaimsButton = new Button(
									"Previous Claims");

							viewPrevClaimsButton.setData(medicalAuditClaimStatusReportDto);
							viewPrevClaimsButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto) event
													.getButton()
													.getData();
											String intimationNo = medicalAuditClaimStatusReportDto.getIntimationNo();
											if(intimationNo != null){
												viewDetails.getViewPreviousClaimDetails(intimationNo);
											}
										}
									});
							viewPrevClaimsButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return viewPrevClaimsButton;
								 
					}else{
					        return "";
					}	
					}
					});
			
			
			table.removeGeneratedColumn("conditions");
			table.addGeneratedColumn("conditions",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
						
							final MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto)itemId;
							
							final Button viewPolicyCondButton = new Button(
									"Conditions");

							viewPolicyCondButton.setData(medicalAuditClaimStatusReportDto);
							viewPolicyCondButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto) event
													.getButton()
													.getData();
											Policy policyObj = medicalAuditClaimStatusReportDto.getPolicyObj();
											
											if(policyObj != null && policyObj.getPolicyNumber() != null){
											PremPolicyDetails policyDetails = policyService.fetchPolicyDetailsFromPremia(policyObj.getPolicyNumber());
									    	ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
													.get();
								
									    	a_viewProductBenefits.showValues(policyDetails);
											UI.getCurrent().addWindow(a_viewProductBenefits);
											}
								        } 
								    });
								viewPolicyCondButton.addStyleName("link");
						        return viewPolicyCondButton;
								}
						else{
							return "";
						}
						}
					});
						
			table.removeGeneratedColumn("schedule");
			table.addGeneratedColumn("schedule",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
						
							final MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto)itemId;
							
							final Button viewPolicyScheduleButton = new Button(
									"Schedule");

							viewPolicyScheduleButton.setData(medicalAuditClaimStatusReportDto);
							viewPolicyScheduleButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto) event
													.getButton()
													.getData();
											Policy policyObj = medicalAuditClaimStatusReportDto.getPolicyObj();
											if(policyObj != null && policyObj.getPolicyNumber() != null){
												policyViewUI.getViewDocumentByPolicyNo(policyObj.getPolicyNumber());
											}
								        } 
								    });
							viewPolicyScheduleButton.addStyleName("link");
						        return viewPolicyScheduleButton;
								}
						else{
							return "";
						}
						}
					});
					
			table.removeGeneratedColumn("packagerates");
			table.addGeneratedColumn("packagerates",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						
						if(((MedicalAuditCashlessIssuanceReportDto)itemId).getPolicyNumber() != null){
						
							final MedicalAuditCashlessIssuanceReportDto medicalAuditClaimStatusReportDto = (MedicalAuditCashlessIssuanceReportDto)itemId;
							
							final Button viewPackageReatesButton = new Button(
									"Package Rates");

							viewPackageReatesButton.setData(medicalAuditClaimStatusReportDto);
							viewPackageReatesButton
									.addClickListener(new Button.ClickListener() {
										public void buttonClick(
												ClickEvent event) {
											MedicalAuditCashlessIssuanceReportDto medicalAuditCashlessReportDto = (MedicalAuditCashlessIssuanceReportDto) event
													.getButton()
													.getData();
											String hospitalCode = medicalAuditCashlessReportDto.getHospitalCode();
											
											if(hospitalCode != null){
												BPMClientContext bpmClientContext = new BPMClientContext();
												String url = bpmClientContext.getHospitalPackageDetails() + hospitalCode;
												//getUI().getPage().open(url, "_blank");
												getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
											}
											
//											HospitalPackageRatesDto packageRatesDto = hospitalService.getHospitalPackageRates(hospitalCode);
											
//											if(packageRatesDto != null){
//											ReportDto reportDto = new ReportDto();
//											reportDto.setClaimId(medicalAuditClaimStatusReportDto.getClaimNumber());
//											List<HospitalPackageRatesDto> beanList = new ArrayList<HospitalPackageRatesDto>();
//											beanList.add(packageRatesDto);
//											reportDto.setBeanList(beanList);
//											DocumentGenerator docGen = new DocumentGenerator();
//											String fileUrl = docGen.generatePdfDocument("HospitalPackageRates", reportDto);
//											openPdfFileInWindow(fileUrl);
//											}
//											else{
//												showErrorPopup("Package Not Available for the selected Hospital");
//											}
								        } 
								    });
							viewPackageReatesButton.addStyleName("link");
						        return viewPackageReatesButton;
								}
						else{
							return "";
						}
						}
					});			
			
			
			table.setColumnCollapsingAllowed(false);
			table.setHeight("480px");
	}

	@Override
	public void tableSelectHandler(MedicalAuditCashlessIssuanceReportDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "medical-audit-claim-status-report-";
	}
	
	private void openPdfFileInWindow(final String filepath) {
		
		Window window = new Window();
		// ((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Hospital Package Rate");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.center();

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			public FileInputStream getStream() {
				try {

					File f = new File(filepath);
					FileInputStream fis = new FileInputStream(f);
					return fis;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		StreamResource r = new StreamResource(s, fileName);
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");
		e.setSource(r);
		window.setContent(e);
		UI.getCurrent().addWindow(window);
	}
	
	public void showErrorPopup(String eMsg)
	{
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	public void viewUploadedDocumentDetails(String intimationNo) {
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
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
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		
		getUI().getPage().open(url, "_blank",1500,800,BorderStyle.NONE);
	}

}
