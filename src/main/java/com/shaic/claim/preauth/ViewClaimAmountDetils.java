package com.shaic.claim.preauth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.shaic.arch.table.NewClaimedAmountTable;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.NoOfDaysCell;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

@ViewScoped
public class ViewClaimAmountDetils  extends Window{

	private static final long serialVersionUID = 5372951904613924447L;
	
	@Inject
	private Instance<NewClaimedAmountTable> claimedAmountDetailsTable;
	
	NewClaimedAmountTable claimedAmountDetailsTableObj;

	@PostConstruct
	public void init() {
		setCaption("Claim Amount Details");
		setWidth("1000px");
		 setModal(true);
		 setClosable(true);
		 setResizable(false);

	}
	
	@SuppressWarnings("unchecked")
	public void showDetails(PreauthDTO preauthDto, Map<String, Object> referenceData) {
		
		claimedAmountDetailsTableObj = claimedAmountDetailsTable.get();
		claimedAmountDetailsTableObj.setDBCalculationValues((Map<Integer, Object>)referenceData.get("claimDBDetails"));
		claimedAmountDetailsTableObj.initView(preauthDto, "");
		List<NoOfDaysCell> values = new ArrayList<NoOfDaysCell>();
		values.addAll(preauthDto.getPreauthDataExtractionDetails().getClaimedDetailsList());
		claimedAmountDetailsTableObj.setValues(preauthDto.getPreauthDataExtractionDetails().getClaimedDetailsList(), false);
		preauthDto.getPreauthDataExtractionDetails().setClaimedDetailsList(values);
		claimedAmountDetailsTableObj.enableOrdisableItem(false, null, false);
		
		VerticalLayout verticalLayout = new VerticalLayout(claimedAmountDetailsTableObj);
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		this.setContent(verticalLayout);
	}
	
	
	
}
