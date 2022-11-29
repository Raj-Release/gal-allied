package com.shaic.claim.intimation;

import java.util.List;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Intimation;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.preauth.Preauth;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
public class CashLessTableDetails extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VerticalLayout init(CashlessTable cashlessTable,
			NewIntimationService newIntimationService,
			CashLessTableMapper cashLessTableMapper, Intimation intimation) {
		cashlessTable.init("", false, true);
		List<Preauth> preAuthList = newIntimationService
				.getPreauthListByIntimationKey(intimation.getKey());
			List<CashLessTableDTO> cashLessTableDTO = cashLessTableMapper
					.getCashLessTableDTO(preAuthList);
			cashlessTable.setTableList(cashLessTableDTO);
		return new VerticalLayout(cashlessTable);
	}

}
