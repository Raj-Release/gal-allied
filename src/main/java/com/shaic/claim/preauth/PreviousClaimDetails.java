package com.shaic.claim.preauth;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.shaic.claim.ClaimDto;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashLessTableMapper;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.ViewIntimationStatus;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Insured;
import com.shaic.domain.InsuredService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;

public class PreviousClaimDetails {

	private static final String GROUP = "Group";
	private static final String FRESH = "Fresh";
	private static final String RISK_WISE = "Risk Wise";
	private static final String POLICY_WISE = "Policy Wise";
	private static final String INSURED_WISE = "Insured Wise";

	private Object[] columns = new Object[] { "claimId",
			"intimation.policy.insuredId",
			"intimation.policy.insuredFirstName", "Diagnosis",
			"intimation.admissionDate", "status.processValue", "claimedAmount",
			"action" };

	private Table createViewTable;
	private VerticalLayout claimlayout;
	private IndexedContainer iContainer;

	@EJB
	private ClaimService claimService;
	@EJB
	private PolicyService policyService;
	@EJB
	private IntimationService intimationService;
	@EJB
	private InsuredService insuredService;

	@Inject
	private CashLessTableDetails cashLessTableDetails;

	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;

	@Inject
	private CashLessTableMapper cashLessTableMapper;

	/*@Inject
	private Intimation intimation;*/

	@Inject
	private ClaimStatusDto claimStatusDto;

	public VerticalLayout getPrivousClaims(String intimationNumber) {
		HorizontalLayout optionLayout = buildViewType(intimationNumber);
		optionLayout.setWidth("100%");
		optionLayout.setSpacing(true);
		optionLayout.setMargin(true);
		claimlayout = new VerticalLayout();
		//Vaadin8-setImmediate() claimlayout.setImmediate(false);
		claimlayout.setMargin(false);
		iContainer = createContainer();
		createViewTable = createTable(iContainer, "");

		claimlayout.addComponent(optionLayout);
		claimlayout.addComponent(createViewTable);
		return claimlayout;
	}

	public HorizontalLayout buildViewType(String intimationNo) {
		System.out
				.println("+++++++++++++++++++++++++PreviousClaims+++++++++++++++++++++++++++++++"
						+ intimationNo);
		final Intimation intimation = intimationService
				.searchbyIntimationNo("I/2014/0000001");

		Policy policy = policyService.getPolicy(intimation.getPolicy()
				.getPolicyNumber());

		System.out.println("Policy Number " + policy);

		OptionGroup viewType = getOptionGroup(policy);
		NativeButton viewBtn = getViewButton(intimation, viewType);
		HorizontalLayout optionLayout = new HorizontalLayout();
		optionLayout.addComponent(viewType);
		optionLayout.addComponent(viewBtn);
		return optionLayout;
	}

	public OptionGroup getOptionGroup(Policy policy) {
		OptionGroup viewType = new OptionGroup();
		//Vaadin8-setImmediate() viewType.setImmediate(false);
		viewType.addItem(POLICY_WISE);
		viewType.addItem(INSURED_WISE);
		if (StringUtils.containsIgnoreCase(policy.getProductType().getValue(),
				GROUP))
			viewType.addItem(RISK_WISE);
		viewType.setStyleName("inlineStyle");
		return viewType;
	}

	private NativeButton getViewButton(final Intimation intimation,
			final OptionGroup viewType) {
		NativeButton viewBtn = new NativeButton();
		viewBtn.setCaption("View");
		viewBtn.setData(viewType);
		viewBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -676865664871344469L;

			@Override
			public void buttonClick(ClickEvent event) {

				if (viewType.getValue() != null) {
					if (viewType.getValue().equals(POLICY_WISE)) {

						List<Intimation> intimationlist = getPolicyWiseClaimList(intimation);
						getClaimList(intimationlist);
						createViewTable = createTable(iContainer, POLICY_WISE);
						claimlayout.addComponent(createViewTable);
					} else if (viewType.getValue().equals(INSURED_WISE)) {

						List<Intimation> intimationlist = getInsuredWiseClaimList(intimation);
						getPreviousInsuredNumber(intimation);
						getClaimList(intimationlist);
						createViewTable = createTable(iContainer, INSURED_WISE);
						claimlayout.addComponent(createViewTable);
					} else if (viewType.getValue().equals(RISK_WISE)) {

						List<Intimation> intimationlist = getRiskWiseClaimList(intimation);
						getClaimList(intimationlist);
						createViewTable = createTable(iContainer, RISK_WISE);
						claimlayout.addComponent(createViewTable);
					}
				}

			}

		});
		return viewBtn;
	}

	private List<Intimation> getRiskWiseClaimList(final Intimation intimation) {
		List<Intimation> intimationlist = policyService
				.getIntimationListByPolicy(intimation.getPolicy()
						.getPolicyNumber());
		Policy policy = policyService.getPolicy(intimation.getPolicy()
				.getPolicyNumber());

		if (policy.getRenewalPolicyNumber() != null
				&& !policy.getPolicyType().equals(FRESH)) {
			List<Intimation> previousIntimationlist = policyService
					.getIntimationListByPolicy(policy.getRenewalPolicyNumber());
			if (previousIntimationlist.size() != 0)
				intimationlist.addAll(previousIntimationlist);

		}
		return intimationlist;
	}

	private List<Intimation> getInsuredWiseClaimList(final Intimation intimation) {
		List<Intimation> intimationlist = policyService
				.getIntimationListByInsured(String.valueOf(intimation
						.getInsured().getInsuredId()));
		String PreviousInsuredId = getPreviousInsuredNumber(intimation);

		if (PreviousInsuredId != null) {
			List<Intimation> previousIntimationlist = policyService
					.getIntimationListByInsured(PreviousInsuredId);
			if (previousIntimationlist.size() != 0)
				intimationlist.addAll(previousIntimationlist);

		}
		return intimationlist;
	}

	private List<Intimation> getPolicyWiseClaimList(final Intimation intimation) {
		List<Intimation> intimationlist = policyService
				.getIntimationListByPolicy(intimation.getPolicy()
						.getPolicyNumber());
		Policy policy = policyService.getPolicy(intimation.getPolicy()
				.getPolicyNumber());

		if (policy.getRenewalPolicyNumber() != null) {
			List<Intimation> previousIntimationlist = policyService
					.getIntimationListByPolicy(policy.getRenewalPolicyNumber());
			if (previousIntimationlist.size() != 0)
				intimationlist.addAll(previousIntimationlist);

		}
		return intimationlist;
	}

	private void columnMapper(Table table, IndexedContainer container) {

		table.setColumnHeader("claimId", "Claim No");
		table.setColumnHeader("intimation.policy.insuredId", "Customer ID");
		table.setColumnHeader("intimation.policy.insuredFirstName",
				"Insured Patient Name");
		table.setColumnHeader("intimation.admissionDate", "Date of Admission");
		table.setColumnHeader("status.processValue", "Claim Status");
		table.setColumnHeader("claimedAmount", "Claim Amount");

		table.addContainerProperty("key", Long.class, null);
		table.addContainerProperty("claimId", String.class, null);
		table.addContainerProperty("intimation.policy.insuredId", String.class,
				null);
		table.addContainerProperty("intimation.policy.insuredFirstName",
				String.class, null);
		table.addContainerProperty("intimation.admissionDate", Date.class, null);
		table.addContainerProperty("status.processValue", String.class, null);
		table.addContainerProperty("claimedAmount", Double.class, null);
		table.setContainerDataSource(container);
		table.setVisibleColumns(columns);
		table.setPageLength(table.size() + 1);
	}

	public IndexedContainer createContainer() {
		IndexedContainer container = new IndexedContainer();

		container.addContainerProperty("key", Long.class, null);
		container.addContainerProperty("claimId", String.class, null);
		container.addContainerProperty("intimation.policy.insuredId",
				String.class, null);
		container.addContainerProperty("intimation.policy.insuredFirstName",
				String.class, null);

		container.addContainerProperty("Diagnosis", String.class, null);

		container.addContainerProperty("intimation.admissionDate", Date.class,
				null);
		container.addContainerProperty("status.processValue", String.class,
				null);
		container.addContainerProperty("claimedAmount", Double.class, null);
		container.addContainerProperty("action", Button.class, null);

		return container;
	}

	public Table createTable(IndexedContainer container, String tableHeader) {

		final Table table = new Table(tableHeader);
		columnMapper(table, container);
		table.setWidth("100%");
		table.setHeight("100%");
		table.setEditable(false);
		return table;
	}

	@SuppressWarnings("unchecked")
	public void addItem(final Table table, final IndexedContainer container,
			final Claim claim) {
		Object itemId = container.addItem();
		container.getItem(itemId).getItemProperty("key")
				.setValue(claim.getKey());
		container.getItem(itemId).getItemProperty("status.processValue")
				.setValue(claim.getStatus().getProcessValue());
		container.getItem(itemId).getItemProperty("claimId")
				.setValue(claim.getClaimId());
		container.getItem(itemId).getItemProperty("claimedAmount")
				.setValue(claim.getClaimedAmount());
		container.getItem(itemId)
				.getItemProperty("intimation.policy.insuredId")
				.setValue(claim.getIntimation().getInsured().getInsuredId());
		container.getItem(itemId)
				.getItemProperty("intimation.policy.insuredFirstName")
				.setValue(claim.getIntimation().getInsured().getInsuredName());
		container.getItem(itemId).getItemProperty("intimation.admissionDate")
				.setValue(claim.getIntimation().getAdmissionDate());

		Button viewClaimBtn = getViewClaimStatusButton(container, itemId);
		container.getItem(itemId).getItemProperty("action")
				.setValue(viewClaimBtn);
		viewClaimBtn.addStyleName("link");

		container.getItem(itemId).getItemProperty("Diagnosis");

	}

	private Button getViewClaimStatusButton(final IndexedContainer container,
			Object itemId) {
		Button viewClaimBtn = new Button("View Claim Status");
		viewClaimBtn.setData(itemId);
		viewClaimBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				final Object itemId = event.getButton().getData();
				Item item = container.getItem(itemId);
				ViewIntimationStatus intimationStatus = getClaimStatus(item);
				UI.getCurrent().addWindow(intimationStatus);
			}
		});
		return viewClaimBtn;
	}

	private ViewIntimationStatus getClaimStatus(Item item) {
		Claim a_claim = claimService.getClaimByKey((Long) (item
				.getItemProperty("key").getValue()));

		Intimation intimation = a_claim.getIntimation();

		/*Hospitals hospital = policyService.getVWHospitalByKey(intimation
				.getHospital());*/

		// IntimationsDto a_intimationDto = DtoConverter
		// .intimationToIntimationDTO(intimation, hospital);
		// ClaimDto a_claimDto = DtoConverter.claimToClaimDTO(a_claim,
		// hospital);

//		DtoConverter dtoConverter = new DtoConverter();
		ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);

		// ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
		// a_claimDto, a_intimationDto,
		// intimation.getPolicy().getStatus() == null);
		
			claimStatusDto.setClaimDto(a_claimDto);
			claimStatusDto
					.setNewIntimationDto(a_claimDto.getNewIntimationDto());
			cashLessTableMapper.getAllMapValues();
			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
					claimStatusDto,
					intimation.getPolicy().getActiveStatus() == null,
					cashLessTableDetails, cashlessTable, cashLessTableMapper,
					newIntimationService, intimation);

		return intimationStatus;
	}

	private String getPreviousInsuredNumber(Intimation a_intimation) {
		Insured insured = insuredService.getCLSInsured(a_intimation.getPolicy()
				.getPolicyNumber(), a_intimation.getInsured().getInsuredName(),
				a_intimation.getInsured().getInsuredDateOfBirth());
		if (insured != null)
			return insured.getRelationshipwithInsuredId().getValue();
		return null;
	}

	private void getClaimList(List<Intimation> intimationlist) {
		claimlayout.removeComponent(createViewTable);
		iContainer.removeAllContainerFilters();
		this.iContainer = createContainer();
		for (Intimation a_intimation : intimationlist) {
			Claim a_claim = claimService.getClaimforIntimation(a_intimation
					.getKey());
			if (a_claim != null) {
				addItem(createViewTable, iContainer, a_claim);
			}
		}
	}
}
