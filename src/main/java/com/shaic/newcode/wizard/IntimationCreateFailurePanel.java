package com.shaic.newcode.wizard;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashLessTableMapper;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.intimation.ViewIntimation;
import com.shaic.claim.intimation.ViewIntimationStatus;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;

public class IntimationCreateFailurePanel extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6082764399827475812L;

	VerticalLayout layout;

	@EJB
	private PolicyService policyService;

	@EJB
	private HospitalService hospitalService;

	@Inject
	private CashLessTableDetails cashLessTableDetails;

	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;

	@Inject
	private CashLessTableMapper cashLessTableMapper;
	
	@Inject
	private IntimationService intimationService;

	@PostConstruct
	public void init() {
		layout = new VerticalLayout();
		layout.setSizeFull();
		setCompositionRoot(layout);
	}

	public void createSuccessLayout(Intimation intimation) {
		layout.addComponent(buildunSuccessverticalLayout(intimation));
		layout.setSizeFull();
	}

	private VerticalLayout buildunSuccessverticalLayout(Intimation intimation) {
		VerticalLayout unSuccessverticalLayout = new VerticalLayout();
		//Vaadin8-setImmediate() unSuccessverticalLayout.setImmediate(false);
		unSuccessverticalLayout.setWidth("100.0%");
		unSuccessverticalLayout.setHeight("530px");
		unSuccessverticalLayout.setMargin(false);

		Label unSuccessLabel = new Label();
		//Vaadin8-setImmediate() unSuccessLabel.setImmediate(false);
		unSuccessLabel.setWidth("-1px");
		unSuccessLabel.setHeight("-1px");

		unSuccessverticalLayout.addComponent(unSuccessLabel);
		unSuccessverticalLayout.setComponentAlignment(unSuccessLabel,
				new Alignment(24));

		String unsuccess = "Duplicate Intimation!!! Similar intimation with "
				+ intimation.getIntimationId() + " is already available !!";
		unSuccessLabel.setValue(unsuccess);
		unSuccessLabel.addStyleName("errMessage");
		// horizontalLayout_1
		HorizontalLayout unSuccesshorizontalLayout = buildunSuccesshorizontalLayout(intimation);
		unSuccessverticalLayout.addComponent(unSuccesshorizontalLayout);
		unSuccessverticalLayout.setComponentAlignment(
				unSuccesshorizontalLayout, new Alignment(10));

		return unSuccessverticalLayout;
	}

	private HorizontalLayout buildunSuccesshorizontalLayout(
			Intimation intimation) {
		HorizontalLayout unSuccesshorizontalLayout = new HorizontalLayout();
		//Vaadin8-setImmediate() unSuccesshorizontalLayout.setImmediate(false);
		unSuccesshorizontalLayout.setWidth("-1px");
		unSuccesshorizontalLayout.setHeight("-1px");
		unSuccesshorizontalLayout.setMargin(true);
		unSuccesshorizontalLayout.setSpacing(true);

		Button homeunSuccessnativeButton = new Button();
		homeunSuccessnativeButton.setCaption("Intimations Home");
		//Vaadin8-setImmediate() homeunSuccessnativeButton.setImmediate(false);
		homeunSuccessnativeButton.setWidth("-1px");
		homeunSuccessnativeButton.setHeight("-1px");
		homeunSuccessnativeButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment(
						"!" + MenuItemBean.SEARCH_POLICY);
			}
		});

		unSuccesshorizontalLayout.addComponent(homeunSuccessnativeButton);

		Button intimationViewunSuccessnativeButton = new Button();
		intimationViewunSuccessnativeButton.setCaption("Intimation View");
		//Vaadin8-setImmediate() intimationViewunSuccessnativeButton.setImmediate(false);
		intimationViewunSuccessnativeButton.setWidth("-1px");
		intimationViewunSuccessnativeButton.setHeight("-1px");
		intimationViewunSuccessnativeButton.setData(intimation);
		intimationViewunSuccessnativeButton
				.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						Intimation intimation = (Intimation) event.getButton()
								.getData();
						if (intimation != null) {

							Hospitals hospital = policyService
									.getVWHospitalByKey(intimation
											.getHospital());
							NewIntimationDto intimationToIntimationDetailsDTO = intimationService.getIntimationDto(intimation);

							if (intimation.getStatus() != null
									&& intimation.getStatus().getProcessValue().equalsIgnoreCase(
											"SUBMITTED")) {
								ViewIntimation intimationDetails = new ViewIntimation(
										intimationToIntimationDetailsDTO,
										hospitalService);
								UI.getCurrent().addWindow(intimationDetails);
							} else {
								ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
										intimationToIntimationDetailsDTO,
										intimation.getPolicy().getActiveStatus() == null,
										cashLessTableDetails, cashlessTable,
										cashLessTableMapper,
										newIntimationService, intimation);
								UI.getCurrent().addWindow(intimationStatus);

							}
						}

					}
				});
		unSuccesshorizontalLayout
				.addComponent(intimationViewunSuccessnativeButton);

		return unSuccesshorizontalLayout;
	}
}
