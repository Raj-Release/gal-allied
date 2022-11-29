package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.Investigation;
import com.shaic.domain.InvestigationService;
import com.vaadin.v7.ui.VerticalLayout;

public class InvestigationDetailsTableUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private VerticalLayout mainLayout;

	@Inject
	private InvestigationDetailsTable investigationDetailsTable;

	@Inject
	private InvestigationDetailsMapper investigationDetailsMapper;

	@EJB
	private InvestigationService investigationService;

	private List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTO;

	private Long investigationKey = 0l;

	public void initView(Long investigationKey,
			List<Investigation> investigationList) {
		this.investigationKey = investigationKey;
		investigationDetailsTable.init("Investigation Details", false, false);
		fireViewEvent(
				AcknowledgeInvestigationCompletedPresenter.SET_REFERENCE_FOR_INVESTIGATION_DETAILS_TABLE,
				investigationList);
	}

	public void setReferenceForDto(	
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList) {
		this.investigationDetailsReimbursementTableDTO = investigationDetailsReimbursementTableDTOList;
	}

	public void setCompleteLayout() {
		int sno =1;
		if(investigationDetailsReimbursementTableDTO != null && ! investigationDetailsReimbursementTableDTO.isEmpty()){
			for (InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO2 : investigationDetailsReimbursementTableDTO) {
				investigationDetailsTable.addBeanToList(investigationDetailsReimbursementTableDTO2);
				investigationDetailsReimbursementTableDTO2.setSno(sno);
				sno++;
			}
		}
//		investigationDetailsTable
//				.setTableList(investigationDetailsReimbursementTableDTO);
		investigationDetailsTable.setRadioButtonr(this.investigationKey);
		mainLayout = new VerticalLayout(investigationDetailsTable);
		setCompositionRoot(mainLayout);
	}

}
