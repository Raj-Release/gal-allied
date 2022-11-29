package com.shaic.claim.registration.previousinsurance.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.policy.search.ui.RenewedPolicyDetails;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredPedDetails;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreviousPolicy;
import com.shaic.domain.PreviousPolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.starfax.simulation.PremiaPullService;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreviousInsurance extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5735294604657752597L;

	private VerticalLayout mainLayout;
	
	private VerticalLayout renewalPolicy;

	private VerticalLayout policyDetailsverticalLayout;

	private Table policyDetailstable;
	private Table renewalPolicyDetailTable;
	@EJB
	private PreviousPolicyService previousPolicyService;
	
	@EJB
	private PremiaPullService premiaPullService;
	
	//private static Window popup;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private IntimationService intimationService;

	@EJB
	private InsuredService insuredService;
	private Panel intimationPanel;
	private Panel renewalPolicyPanel;
	public String dataDir = null;
	private Policy policy;
	private Intimation intimation;

	@Inject
	private Instance<ViewPreviousInsuranceInsuredDetails> viewPreviousInsuranceInsuredDetails;

	/*@Inject
	private Instance<PolicySchedulepopupUI> policySchedulepopupUI;*/
	
	public void InitView() {

		buildMainLayout();
		setContent(mainLayout);
		setCaption("Previous Insurance Details");

	}

	private VerticalLayout buildMainLayout() {

		mainLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		renewalPolicy = new VerticalLayout();
		//Vaadin8-setImmediate() renewalPolicy.setImmediate(false);

		setWidth("1000px");
		setHeight("500px");
		setModal(true);
		setClosable(true);
		setResizable(true);

		intimationPanel = buildVerticalLayout_2();
		renewalPolicyPanel = bulidRenewalPolicyPanel();
		renewalPolicy.addComponent(renewalPolicyPanel);
		mainLayout.addComponent(intimationPanel);
		mainLayout.addComponent(renewalPolicy);
		mainLayout.setSpacing(true);

		return mainLayout;
	}

	private Panel buildVerticalLayout_2() {

		intimationPanel = new Panel();

		intimationPanel.setSizeFull();
		policyDetailsverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() policyDetailsverticalLayout.setImmediate(false);

		policyDetailsverticalLayout.setMargin(false);

		// table_1
		policyDetailstable = new Table();
		policyDetailstable.addStyleName("tableheight");
		//Vaadin8-setImmediate() policyDetailstable.setImmediate(false);
		policyDetailstable.setWidth("100.0%");
		policyDetailstable.setHeight("-1px");

		/*policyDetailstable.setContainerDataSource(previousPolicyService
				.getPreviousPolicy(policy.getProposerCode()));*/
		
		policyDetailstable.setContainerDataSource(previousPolicyService
				.getPreviousPolicyDetails(policy));
		
		buildPreviousInsurerName();
		buildPolicyNumber();
		buildSumInsured();
		buildProductName();
		buildViewPolicyScheduleColumn();
		buildViewInsuredDetailsColumn();
		buildPolicyFromDate();
		buildPolicyToDate();
		buildUWYear();

//		policyDetailstable.setColumnHeader("previousInsurerName",
//				"Previous Insurer Name");
		//policyDetailstable.setColumnHeader("polNo", "Policy No");
		//policyDetailstable.setColumnHeader("polOrgSiLc1", "Sum Insured");
		//policyDetailstable.setColumnHeader("polProdName", "Product Name");

		
		Object[] columns = new Object[] { "Previous Insurer Name","Policy number",
				"Policy From Date", "Policy To Date", "U/W Year", "Sum Insured",
				"Product Name", "View Policy Schedule", "View Insured Details" };
		
		/*Object[] columns = new Object[] { 
				"Policy From Date", "Policy To Date", "U/W Year", 
				 "View Policy Schedule", "View Insured Details" };*/
		
		policyDetailstable.setVisibleColumns(columns);

		policyDetailstable.setPageLength(policyDetailstable.size());
		intimationPanel.setContent(policyDetailstable);

		return intimationPanel;
	}
	
private Panel bulidRenewalPolicyPanel(){
		
		renewalPolicyPanel = new Panel();
		renewalPolicyPanel.setSizeFull();
		renewalPolicyPanel.setVisible(false);
		renewalPolicyPanel.setCaption("Renewed Insurance Details");
		renewalPolicyDetailTable = new Table();
		renewalPolicyDetailTable.addStyleName("tableheight");
		//Vaadin8-setImmediate() renewalPolicyDetailTable.setImmediate(false);
		renewalPolicyDetailTable.setWidth("100.0%");
		renewalPolicyDetailTable.setHeight("-1px");
		renewalPolicyDetailTable.setVisible(false);
		
		
		if(policy.getPolicyNumber() != null){
			BeanItemContainer<RenewedPolicyDetails> renewedPolicies  = premiaPullService.fetchRenewedPolicyDetails(policy.getPolicyNumber());
			renewalPolicyDetailTable.setContainerDataSource(renewedPolicies);
			renewalPolicyDetailTable.setVisible(true);
			renewalPolicyPanel.setVisible(true);
		}
		
		buildRenewalPolicyInsurerName();
		buildRenewalPolicyNumber();
		buildRenewalPolicyFromDate();
		buildRenewalPolicyToDate();
		buildRenewalUWYear();
		buildRenewalPolicySumInsured();
		buildRenewalPolicyProductName();
		buildRenewalPolicyInsuredDetailsColumn();
		buildRenewalPolicyScheduleColumn();
		
		Object[] renewalTablecolumns = new Object[] { "Insurer Name","Policy number",
				"Policy From Date", "Policy To Date", "U/W Year", "Sum Insured",
				"Product Name", "View Policy Schedule", "View Insured Details" };
		
		renewalPolicyDetailTable.setVisibleColumns(renewalTablecolumns);
		renewalPolicyDetailTable.setPageLength(renewalPolicyDetailTable.size());
		renewalPolicyDetailTable.setCaption("Renewed Insurance Details");
		renewalPolicyPanel.setContent(renewalPolicyDetailTable);
		
		return renewalPolicyPanel;
	}

	
	private void buildPreviousInsurerName(){
		policyDetailstable.addGeneratedColumn("Previous Insurer Name", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
				return previousPolicy.getPreviousInsurerName();
			}
		});
	}
	
	private void buildPolicyNumber(){
		policyDetailstable.addGeneratedColumn("Policy number", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
				return previousPolicy.getPolicyNumber();
			}
		});
	}
	
	
	private void buildSumInsured(){
		policyDetailstable.addGeneratedColumn("Sum Insured", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
				return previousPolicy.getSumInsured();
			}
		});
	}

	private void buildProductName(){
		policyDetailstable.addGeneratedColumn("Product Name", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
				return previousPolicy.getProductName();
			}
		});
	}
	
	private void buildUWYear() {
		policyDetailstable.addGeneratedColumn("U/W Year",
				new Table.ColumnGenerator() {

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
					
						return previousPolicy.getUnderWritingYear();
					}
				});
	}

	private void buildPolicyFromDate() {
		policyDetailstable.addGeneratedColumn("Policy From Date",
				new Table.ColumnGenerator() {

			
					private static final long serialVersionUID = 6985531028582687633L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
						try {
							return SHAUtils.formatDate(previousPolicy.getPolicyFrmDate());
							
						} catch (ReadOnlyException e) {
							e.printStackTrace();
						}
						
						return null;
					}
				});
	}

	private void buildPolicyToDate() {
		policyDetailstable.addGeneratedColumn("Policy To Date",
				new Table.ColumnGenerator() {

			
					private static final long serialVersionUID = 92230227417318304L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
						try {
							return SHAUtils.formatDate(previousPolicy.getPolicyToDate());
							
						} catch (ReadOnlyException e) {
							e.printStackTrace();
						}
						return null;
					}
				});
	}

	private void buildViewInsuredDetailsColumn() {
		policyDetailstable.addGeneratedColumn("View Insured Details",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 4552678237193144123L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewInsuredDetailsBtn = new Button(

						"View Insured Details");
						viewInsuredDetailsBtn
								.addClickListener(new Button.ClickListener() {

									private static final long serialVersionUID = 4227420299483939727L;

									public void buttonClick(ClickEvent event) {
										PreviousPolicy tmpPrevPolicy = (PreviousPolicy) itemId;
										try{
										Policy policy = policyService.getPolicyByRenewalPolicyNumber(String.valueOf(tmpPrevPolicy
												.getPolicyNumber()));
										
										List<Insured> insuredList = insuredService
												.getInsuredListByPolicyNo(String.valueOf(policy
														.getKey()));
										List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuranceInsuredDetailsList = new ArrayList<PreviousInsuranceInsuredDetailsTableDTO>();
										for (Insured insured : insuredList) {
											PreviousInsuranceInsuredDetailsTableDTO previousInsuranceInsuredDetailsTableDTO = new PreviousInsuranceInsuredDetailsTableDTO();
											previousInsuranceInsuredDetailsTableDTO
													.setInsuredName(insured
															.getInsuredName() !=null ? insured
																	.getInsuredName() :"");
											previousInsuranceInsuredDetailsTableDTO.setAge(insured.getInsuredAge());
											previousInsuranceInsuredDetailsTableDTO.setDOB(insured.getInsuredDateOfBirth());
											if(insured.getRelationshipwithInsuredId() != null){
												previousInsuranceInsuredDetailsTableDTO.setRelation(insured.getRelationshipwithInsuredId().getValue());
											}
											previousInsuranceInsuredDetailsTableDTO.setSumInsured(insured.getInsuredSumInsured());
											if(insured.getInsuredGender() != null){
												previousInsuranceInsuredDetailsTableDTO.setSex(insured.getInsuredGender().getValue());
											}
											List<InsuredPedDetails> insuredPedDetails =insuredService.getInsuredKeyListByInsuredkey(insured.getInsuredId());
											StringBuffer description = new StringBuffer();
											for(InsuredPedDetails insuredPedDetail : insuredPedDetails){
												//PreviousInsuranceInsuredDetailsTableDTO previousInsuranceInsuredDetailsTableDTO1 =new PreviousInsuranceInsuredDetailsTableDTO();
												 description.append((String)insuredPedDetail.getPedDescription()).append(" ,");
											}
											previousInsuranceInsuredDetailsTableDTO.setPedDescription(description.toString());
											previousInsuranceInsuredDetailsList.add(previousInsuranceInsuredDetailsTableDTO);
										}
										ViewPreviousInsuranceInsuredDetails viewPreviousInsuranceInsuredDetailsInstance = viewPreviousInsuranceInsuredDetails
												.get();
										viewPreviousInsuranceInsuredDetailsInstance
												.showValues(previousInsuranceInsuredDetailsList);
										UI.getCurrent()
												.addWindow(
														viewPreviousInsuranceInsuredDetailsInstance);
										}catch(Exception e){
											Label successLabel = new Label("<b style = 'color: black;'>No record found!!! </b>", ContentMode.HTML);
											
//											Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
											
											Button homeButton = new Button("insured Details");
											homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
											VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
											layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
											layout.setSpacing(true);
											layout.setMargin(true);
											HorizontalLayout hLayout = new HorizontalLayout(layout);
											hLayout.setMargin(true);
											
											final ConfirmDialog dialog = new ConfirmDialog();
											dialog.setCaption("");
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
												
													
												}
											});
											
										}
									}

								});
						viewInsuredDetailsBtn.addStyleName("link");
						return viewInsuredDetailsBtn;
					}
				});
	}

	private void buildViewPolicyScheduleColumn() {
		policyDetailstable.addGeneratedColumn("View Policy Schedule",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 7228208524226378048L;
					
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewPolicyScheduleBtn = new Button(
								"View Policy Schedule");
						viewPolicyScheduleBtn
								.addClickListener(new Button.ClickListener() {

									private static final long serialVersionUID = 1034187828701338725L;

									public void buttonClick(ClickEvent event) {
										
										PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
										
										getViewDocumentByPolicyNo(previousPolicy.getPolicyNumber());
//										PolicySchedulepopupUI window = policySchedulepopupUI.get();
//										
//										UI.getCurrent()
//										.addWindow(window);
									}
								});

						viewPolicyScheduleBtn.addStyleName("link");
						return viewPolicyScheduleBtn;
					}
				});
	}
	
	private void buildRenewalPolicyInsurerName(){
		renewalPolicyDetailTable.addGeneratedColumn("Insurer Name", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
				return renewedPolicyDtls.getInsurerName();
			}
		});
	}
	
	private void buildRenewalPolicyNumber(){
		renewalPolicyDetailTable.addGeneratedColumn("Policy number", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
				return renewedPolicyDtls.getPolicyNumber();
			}
		});
	}
	
	
	private void buildRenewalPolicySumInsured(){
		renewalPolicyDetailTable.addGeneratedColumn("Sum Insured", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
				return renewedPolicyDtls.getSumInsured();
			}
		});
	}

	private void buildRenewalPolicyProductName(){
		renewalPolicyDetailTable.addGeneratedColumn("Product Name", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
				return renewedPolicyDtls.getProductName();
			}
		});
	}
	
	private void buildRenewalUWYear() {
		renewalPolicyDetailTable.addGeneratedColumn("U/W Year",
				new Table.ColumnGenerator() {

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
					
						return renewedPolicyDtls.getUnderWriteYear();
					}
				});
	}

	private void buildRenewalPolicyFromDate() {
		renewalPolicyDetailTable.addGeneratedColumn("Policy From Date",
				new Table.ColumnGenerator() {

			
					private static final long serialVersionUID = 6985531028582687633L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
						try {
							
							Date fromDate = new Date(renewedPolicyDtls.getFromDate());
							return new SimpleDateFormat("dd/MM/yyyy").format(fromDate);
							
						} catch (ReadOnlyException e) {
							e.printStackTrace();
						}
						
						return null;
					}
				});
	}

	private void buildRenewalPolicyToDate() {
		renewalPolicyDetailTable.addGeneratedColumn("Policy To Date",
				new Table.ColumnGenerator() {

			
					private static final long serialVersionUID = 92230227417318304L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
						try {
							Date toDate = new Date(renewedPolicyDtls.getToDate());
							return new SimpleDateFormat("dd/MM/yyyy").format(toDate);
							
						} catch (ReadOnlyException e) {
							e.printStackTrace();
						}
						return null;
					}
				});
	}
	
	private void buildRenewalPolicyInsuredDetailsColumn() {
		renewalPolicyDetailTable.addGeneratedColumn("View Insured Details",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 4552678237193144123L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewInsuredDetailsBtn = new Button(

						"View Insured Details");
						viewInsuredDetailsBtn
								.addClickListener(new Button.ClickListener() {

									private static final long serialVersionUID = 4227420299483939727L;

									public void buttonClick(ClickEvent event) {
										RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
										try{
										Policy policy = policyService.getPolicyByRenewalPolicyNumber(String.valueOf(renewedPolicyDtls
												.getPolicyNumber()));
										
										List<Insured> insuredList = insuredService
												.getInsuredListByPolicyNo(String.valueOf(policy
														.getKey()));
										List<PreviousInsuranceInsuredDetailsTableDTO> previousInsuranceInsuredDetailsList = new ArrayList<PreviousInsuranceInsuredDetailsTableDTO>();
										for (Insured insured : insuredList) {
											PreviousInsuranceInsuredDetailsTableDTO previousInsuranceInsuredDetailsTableDTO = new PreviousInsuranceInsuredDetailsTableDTO();
											previousInsuranceInsuredDetailsTableDTO
													.setInsuredName(insured
															.getInsuredName() !=null ? insured
																	.getInsuredName() :"");
											previousInsuranceInsuredDetailsTableDTO.setAge(insured.getInsuredAge());
											previousInsuranceInsuredDetailsTableDTO.setDOB(insured.getInsuredDateOfBirth());
											if(insured.getRelationshipwithInsuredId() != null){
												previousInsuranceInsuredDetailsTableDTO.setRelation(insured.getRelationshipwithInsuredId().getValue());
											}
											previousInsuranceInsuredDetailsTableDTO.setSumInsured(insured.getInsuredSumInsured());
											if(insured.getInsuredGender() != null){
												previousInsuranceInsuredDetailsTableDTO.setSex(insured.getInsuredGender().getValue());
											}
											List<InsuredPedDetails> insuredPedDetails =insuredService.getInsuredKeyListByInsuredkey(insured.getInsuredId());
											StringBuffer description = new StringBuffer();
											for(InsuredPedDetails insuredPedDetail : insuredPedDetails){
												//PreviousInsuranceInsuredDetailsTableDTO previousInsuranceInsuredDetailsTableDTO1 =new PreviousInsuranceInsuredDetailsTableDTO();
												 description.append((String)insuredPedDetail.getPedDescription()).append(" ,");
											}
											previousInsuranceInsuredDetailsTableDTO.setPedDescription(description.toString());
											previousInsuranceInsuredDetailsList.add(previousInsuranceInsuredDetailsTableDTO);
										}
										ViewPreviousInsuranceInsuredDetails viewPreviousInsuranceInsuredDetailsInstance = viewPreviousInsuranceInsuredDetails
												.get();
										viewPreviousInsuranceInsuredDetailsInstance
												.showValues(previousInsuranceInsuredDetailsList);
										UI.getCurrent()
												.addWindow(
														viewPreviousInsuranceInsuredDetailsInstance);
										}catch(Exception e){
											Label successLabel = new Label("<b style = 'color: black;'>No record found!!! </b>", ContentMode.HTML);
											
//											Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
											
											Button homeButton = new Button("insured Details");
											homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
											VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
											layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
											layout.setSpacing(true);
											layout.setMargin(true);
											HorizontalLayout hLayout = new HorizontalLayout(layout);
											hLayout.setMargin(true);
											
											final ConfirmDialog dialog = new ConfirmDialog();
											dialog.setCaption("");
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
												
													
												}
											});
											
										}
									}

								});
						viewInsuredDetailsBtn.addStyleName("link");
						return viewInsuredDetailsBtn;
					}
				});
	}
	
	private void buildRenewalPolicyScheduleColumn() {
		renewalPolicyDetailTable.addGeneratedColumn("View Policy Schedule",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 7228208524226378048L;
					
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewPolicyScheduleBtn = new Button(
								"View Policy Schedule");
						viewPolicyScheduleBtn
								.addClickListener(new Button.ClickListener() {

									private static final long serialVersionUID = 1034187828701338725L;

									public void buttonClick(ClickEvent event) {
										
										RenewedPolicyDetails renewedPolicyDtls = (RenewedPolicyDetails) itemId;
										
										getViewDocumentByPolicyNo(renewedPolicyDtls.getPolicyNumber());
//										PolicySchedulepopupUI window = policySchedulepopupUI.get();
//										
//										UI.getCurrent()
//										.addWindow(window);
									}
								});

						viewPolicyScheduleBtn.addStyleName("link");
						return viewPolicyScheduleBtn;
					}
				});
	}

	@SuppressWarnings("deprecation")
	public void showViewPolicySchedule() {

		Window window = new Window();
		// ((VerticalLayout)
		// window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("View Policy Schedule PDF");
		window.setWidth("800");
		window.setHeight("600");
		window.setModal(true);
		window.center();
		dataDir = System.getProperty("jboss.server.data.dir");
		final String filepath = dataDir +"/Medi-Classic-Individual.pdf";

		Path p = Paths.get(filepath);
		String fileName = p.getFileName().toString();

		StreamResource.StreamSource s = new StreamResource.StreamSource() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 9138325634649289303L;

			public InputStream getStream() {
				try {

					File f = new File(filepath);
					System.out.println(f.getCanonicalPath());
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
	
	public void getViewDocumentByPolicyNo(String strPolicyNo) {
		
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = null;
		Policy policyObj = null;
		BrowserFrame browserFrame = null;
		
		Long insuredKey = intimation.getInsured().getKey();
		Insured	insured = intimationService.getInsuredByKey(insuredKey);
		
		if (strPolicyNo != null) {
			policyObj = policyService.getByPolicyNumber(strPolicyNo);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					strDmsViewURL = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
					strDmsViewURL = strDmsViewURL.replace("POLICY", strPolicyNo);
					if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
						strDmsViewURL = strDmsViewURL.replace("MEMBER", insured!=null?String.valueOf(insured.getSourceRiskId()!=null?insured.getSourceRiskId():""):"");	
					}else{
						strDmsViewURL = strDmsViewURL.replace("MEMBER", "");
					}
					getUI().getPage().open(strDmsViewURL, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("View Documents",new ExternalResource(strDmsViewURL));
				}else{
					strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
					String dmsToken = intimationService.createDMSToken(strPolicyNo);
					getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("View Documents",new ExternalResource(strDmsViewURL+dmsToken));
				}
			}
		}
		/*String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		/*browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("View Uploaded Documents");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
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
		UI.getCurrent().addWindow(popup);*/
	}

	public void showValue(Policy policy,Intimation intimations) {
		this.policy = policy;
		this.intimation = intimations;
		buildMainLayout();
		setContent(mainLayout);
		setCaption("Previous Insurance Details");

	}

}
