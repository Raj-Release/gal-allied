package com.shaic.claim.intimation.create;


import javax.inject.Inject;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreviousPolicy;
import com.shaic.domain.PreviousPolicyService;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

@UIScoped
public class ViewPreviousPolicyDetails extends Window {

	@Inject
	private ViewPolicyDetails viewPolicyDetails;
	
	private static final long serialVersionUID = -3083771386466544808L;
	
	private Panel intimationPanel;
	private Table policyDetailstable;
	private VerticalLayout mainLayout;
	private VerticalLayout policyDetailsverticalLayout;
	
	private IntimationService intimationService;
	private PolicyService policyService;
	private MasterService masterService;
	private PreviousPolicyService previousPolicyService;	
	
	private Policy policy;

	public ViewPreviousPolicyDetails(PolicyService policyService,
			MasterService masterService, IntimationService intimationService,
			PreviousPolicyService a_previousPolicyService, Policy aPolicy) {
		this.policyService = policyService;
		this.masterService = masterService;
		this.previousPolicyService = a_previousPolicyService;
		this.policy = aPolicy;
		this.intimationService = intimationService;
		
	}

	public void InitView() {
		buildMainLayout();
		setContent(mainLayout);
		setCaption("Previous Policy Details");

	}

	private VerticalLayout buildMainLayout() {
		mainLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		setWidth("70%");
		setHeight("400px");
		setModal(true);
		setClosable(true);
		setResizable(true);

		intimationPanel = buildVerticalLayout_2();
		mainLayout.addComponent(intimationPanel);

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
		/*policyDetailstable.setWidth("100.0%");
		policyDetailstable.setHeight("450px");*/
		policyDetailstable.setPageLength(7);

		policyDetailstable.setContainerDataSource(previousPolicyService
				.getPreviousPolicyDetails(policy));
			
		buildInsuredName();		
		buildPolicyToDate();
		buildPolicyNumber();
		buildPolicyFromDate();
		buildUnderWritingYear();
//		buildPreExistingDisease();
		
		/*policyDetailstable.setColumnHeader("proposerName", "Proposer Name");
		policyDetailstable.setColumnHeader("insuredName","Risk / Insured Name");
		policyDetailstable.setColumnHeader("previousInsurerName",
				"Previous Insurer Name");
		policyDetailstable.setColumnHeader("productName", "Product Name");
		policyDetailstable.setColumnHeader("sumInsured", "Sum Insured");
		policyDetailstable.setColumnHeader("premium", "Premium");
		policyDetailstable.setColumnHeader("policyNumber", "Policy No");
		
		Object[] columns = new Object[] { "proposerName", "insuredName", "policyNumber",
				"Policy From Date","Policy To Date",  "U/W Year", "previousInsurerName",
				"productName", "sumInsured", "premium","Pre Existing Disease" };*/
		
		
		policyDetailstable.setColumnHeader("proposerName", "Proposer Name");
		policyDetailstable.setColumnHeader("insuredName","Risk / Insured Name");
		policyDetailstable.setColumnHeader("previousInsurerName",
				"Previous Insurer Name");
		policyDetailstable.setColumnHeader("productName", "Product Name");
		policyDetailstable.setColumnHeader("sumInsured", "Sum Insured");
		/**
		 * Premium column was commented as per sathish sir suggestion.
		 * This was done on 13/07/2015 , for UAT setup.
		 * */
		//policyDetailstable.setColumnHeader("premium", "Premium");
		policyDetailstable.setColumnHeader("policyNumber", "Policy No");
//		policyDetailstable.setColumnHeader("policyFrmDate","Policy From Date");
//		policyDetailstable.setColumnHeader("policyToDate","Policy To Date");
//		policyDetailstable.setColumnHeader("underWritingYear","U/W Year");
		policyDetailstable.setColumnHeader("preExistingDisease","Pre Existing Disease");
		
		/**
		 * Premium column was commented as per sathish sir suggestion.
		 * This was done on 13/07/2015 , for UAT setup.
		 * */
		
		/*Object[] columns = new Object[] { "proposerName", "insuredName", "policyNumber",
				"Policy From Date","Policy To Date",  "U/W Year", "previousInsurerName",
				"productName", "sumInsured", "premium","preExistingDisease" };*/
		
		
		Object[] columns = new Object[] { "proposerName", "insuredName", "policyNumber",
				"Policy From Date","Policy To Date",  "U/W Year", "previousInsurerName",
				"productName", "sumInsured","preExistingDisease" };
		
		policyDetailstable.addGeneratedColumn("insuredName", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	 return null;
		      }
		});
		
		
		policyDetailstable.setVisibleColumns(columns);

		policyDetailstable.setPageLength(policyDetailstable.size());
		intimationPanel.setContent(policyDetailstable);
		return intimationPanel;
	}

	private void buildPolicyNumber() {
		policyDetailstable.addGeneratedColumn("Policy No",
				new Table.ColumnGenerator() {

				
					private static final long serialVersionUID = -4717867243916631844L;

					@SuppressWarnings("serial")
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						/*
						 * When the chekboc value changes, add/remove the itemId
						 * from the selectedItemIds set
						 */
						PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
						Policy policy = policyService
								.getPolicy(previousPolicy.getPolicyNumber());
						final Button policyNumberButton = new Button(policy
								.getPolicyNumber());
						policyNumberButton.setData(policy);
						policyNumberButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(ClickEvent event) {
										Policy policy = (Policy) event
												.getButton().getData();
										if (policy != null) {
											 viewPolicyDetails.setPolicyServiceAndPolicy(
													policyService, policy,
													masterService,intimationService);
											viewPolicyDetails.initView();
											UI.getCurrent().addWindow(
													viewPolicyDetails);
										}

									}
								});
						policyNumberButton.addStyleName("link");
						return policyNumberButton;
					}
				});
	}

	/*private void buildPreExistingDisease() {
		policyDetailstable.addGeneratedColumn("Pre Existing Disease",
				new Table.ColumnGenerator() {

				
					private static final long serialVersionUID = 8309518341796899882L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						return null;
					}
				});
	}*/

	private void buildInsuredName() {
		policyDetailstable.addGeneratedColumn("Risk / Insured  Name",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1968408766005228918L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						return null;
					}
				});
	}

	private void buildPolicyFromDate() {
		policyDetailstable.addGeneratedColumn("Policy From Date",
				new Table.ColumnGenerator() {

					
					private static final long serialVersionUID = -2856325751273013177L;

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

					private static final long serialVersionUID = -2541460070928855292L;

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

	private void buildUnderWritingYear() {
		policyDetailstable.addGeneratedColumn("U/W Year",
				new Table.ColumnGenerator() {

			
					private static final long serialVersionUID = -194053950020311141L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						PreviousPolicy previousPolicy = (PreviousPolicy) itemId;
					
						return previousPolicy.getUnderWritingYear();
					}
				});
	}

}
