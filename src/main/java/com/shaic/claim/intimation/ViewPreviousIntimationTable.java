package com.shaic.claim.intimation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ViewDetails;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.StageIntimation;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.CashlessDetailsDto;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreviousIntimationTable extends
		GBaseTable<ViewPreviousIntimationDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ClaimService claimService;

	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;

	@Inject
	private CashLessTableMapper cashLessTableMapper;

	@Inject
	private CashLessTableDetails cashLessTableDetails;

	@Inject
	private HospitalService hospitalService;

	@Inject
	private IntimationService intimationService;

	@Inject
	private ClaimStatusDto claimStatusDto;
	
	@Inject
	private CashlessDetailsService cashlessDetailsService;

	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private PolicyService policyService;
	
	boolean claimExists;

	private Window popup;

	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"intimationNo", "policyNo", "insuredPatientName",
			"callerOrIntimatorName","callerAddress", "contactNo", "status", "admittedDate",
			"createdOn", "modifiedOn", "saveAndSubmittedOn"/*,"attenderContactNo"*/,"attenderMobileNo" };

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<ViewPreviousIntimationDto>(
				ViewPreviousIntimationDto.class));
		
		table.removeGeneratedColumn("intimationNo");
		table.addGeneratedColumn("intimationNo", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				ViewPreviousIntimationDto viewPreviousIntimationDto = (ViewPreviousIntimationDto) itemId;				
				Button intimationNoButton= new Button(
						viewPreviousIntimationDto.getIntimatinoNo());
				if (viewPreviousIntimationDto.getIntimatinoNo() != null) {
					Intimation intimation = intimationService
							.getIntimationByNo(viewPreviousIntimationDto
									.getIntimatinoNo());
					if (intimation != null) {
				        Intimation intimationTable = intimationService
								.getIntimationByNo(intimation
										.getIntimationId());
				        if(intimationTable != null){
							List<Claim> claimByIntimation = claimService
									.getClaimByIntimation(intimationTable.getKey());
							claimExists = (claimByIntimation != null && claimByIntimation
									.size() > 0) ? true : false;
				        }

					// getButtonAction(claimExists, intimationNoButton);
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date date = new Date();
					
			       
					if (intimation.getCreatedDate() != null && dateFormat.format(date).compareTo(
							dateFormat.format(intimation.getCreatedDate())) == 0) {
						intimationNoButton.addStyleName("greenLink");
						source.setStyleName("green");
					}else  if(intimation.getStatus() != null
							&& intimation.getStatus().getProcessValue()
							.equalsIgnoreCase("PENDING")
					&& claimExists == false
					&& dateFormat.format(date).compareTo(
							dateFormat.format(intimation
									.getCreatedDate())) != 0){
			        	intimationNoButton.addStyleName("redLink");
			        }else if (claimExists == true
							&& intimation.getStatus() != null
							&& intimation.getStatus().getProcessValue()
									.equalsIgnoreCase("SUBMITTED")) {
						intimationNoButton.addStyleName("blueLink");
					} else if (intimation.getStatus() != null
							&& intimation.getStatus().getProcessValue()
									.equalsIgnoreCase("SUBMITTED")
							&& claimExists == false
							&& dateFormat.format(date).compareTo(
									dateFormat.format(intimation
											.getCreatedDate())) != 0) {
						intimationNoButton.addStyleName("redLink");
					} else if(intimation.getStatus() != null
							&& intimation.getStatus().getProcessValue()
							.equalsIgnoreCase("OP Registered")){
						intimationNoButton.addStyleName("blueLink");
					}
					}else{
						intimationNoButton.addStyleName("greenLink");
						source.setStyleName("green");
					}

					
				}
				intimationNoButton.addClickListener(new Button.ClickListener() {
					/**
						 * 
						 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						ViewPreviousIntimationDto viewPreviousIntimationDto = (ViewPreviousIntimationDto) itemId;
						
						if(viewPreviousIntimationDto.getApplicationFlag() != null && viewPreviousIntimationDto.getApplicationFlag().equalsIgnoreCase("G")){
						
						Intimation intimation = intimationService
								.getIntimationByNo(viewPreviousIntimationDto
										.getIntimatinoNo());
						
						/*List<Claim> claimByIntimation = claimService
								.getClaimByIntimation(intimation.getKey());
						claimExists = (claimByIntimation != null && claimByIntimation
								.size() > 0) ? true : false;*/					
						
						//NewIntimationDto newIntimationDto = intimationService.getGalaxyIntimationDto(intimation);
						

						Claim a_claim = claimService
								.getClaimsByIntimationNumber(intimation.getIntimationId());
						
						if (a_claim != null) {
							viewDetails.viewClaimStatusUpdated(intimation.getIntimationId());
						}
							else if (a_claim == null) {
									viewDetails.getViewGalaxyIntimation(intimation.getIntimationId());
						}

						}else if(viewPreviousIntimationDto.getApplicationFlag() != null && viewPreviousIntimationDto.getApplicationFlag().equalsIgnoreCase("P")){
							getViewClaimStatusFromPremia(viewPreviousIntimationDto.getIntimationNo());
						}
					}
				});

				intimationNoButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				intimationNoButton.addStyleName(ValoTheme.BUTTON_LINK);
				return intimationNoButton;
			}
		});
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
	}
	
	public void setColumnHeader(){
		
		table.setColumnHeader("intimationNo", "Intimation No");
		table.setColumnHeader("policyNo", "Policy No");
		table.setColumnHeader("insuredPatientName", "Insured Patient Name");
		table.setColumnHeader("callerOrIntimatorName", "Caller/Intimator  Name");
		table.setColumnHeader("callerAddress", "Caller's Address");
		table.setColumnHeader("contactNo", "Contact No");
		table.setColumnHeader("status", "Status");
		table.setColumnHeader("admittedDate", "Admitted Date");
		table.setColumnHeader("createdOn", "Created On");
		table.setColumnHeader("modifiedOn", "Modified On");
		table.setColumnHeader("attenderMobileNo", "Attender's Contact No(Mobile)");
		table.setColumnHeader("saveAndSubmittedOn", "Save & Submitted On");
	}

	@Override
	public void tableSelectHandler(ViewPreviousIntimationDto t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view_previous_intimation_table_";
	}
	
	public void getViewClaimStatusFromPremia(String intimationNo) {
		VerticalLayout vLayout = new VerticalLayout();

		String strDmsViewURL = BPMClientContext.PREMIA_CLAIM_VIEW_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/

		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+intimationNo));
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setWidth("100%");
		browserFrame.setHeight("200%");
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-20px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
		vLayout.setWidth("100%");
		vLayout.setHeight("100%");
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("50%");
		popup.setHeight("60%");
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
