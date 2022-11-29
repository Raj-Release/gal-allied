package com.shaic.claim.registration.balancesuminsured.view;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewBillDetails extends ViewComponent {
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	@Inject
	private BillDetailsTable billDetailsTable;

	public void init(List<OPBillDetailsDTO> billDetailsTableDTO) {
		VerticalLayout buildMainLayout = buildMainLayout();
		mainLayout = new VerticalLayout(buildMainLayout);
		mainLayout.setComponentAlignment(buildMainLayout,
				Alignment.MIDDLE_CENTER);
		billDetailsTable.setTableList(billDetailsTableDTO);
		billDetailsTable.calculateTotal();
		setCompositionRoot(mainLayout);

	}

	public VerticalLayout buildMainLayout() {
		billDetailsTable.init("", false, false);
		billDetailsTable.setHeight("100.0%");
		billDetailsTable.setWidth("100.0%");
		VerticalLayout vertical = new VerticalLayout(billDetailsTable);
		return vertical;
	}

}
