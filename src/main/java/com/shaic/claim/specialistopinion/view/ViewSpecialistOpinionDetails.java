package com.shaic.claim.specialistopinion.view;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.ui.VerticalLayout;

public class ViewSpecialistOpinionDetails extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	@Inject
	private ViewSpecialistOpinionTable viewSpecialistOptionTable;

	@Inject
	private ViewSpecialistOpinionService viewSpecialistOpinionService;

	public void init(Long intimationKey) {
		viewSpecialistOptionTable.init("", false, false);
		viewSpecialistOptionTable.setTableList(viewSpecialistOpinionService
				.search(intimationKey));
		mainLayout = new VerticalLayout(viewSpecialistOptionTable);
		setCompositionRoot(mainLayout);
	}

}
