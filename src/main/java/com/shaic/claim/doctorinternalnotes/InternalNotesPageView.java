package com.shaic.claim.doctorinternalnotes;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.fieldvisit.search.SearchFieldVisitTableDTO;
import com.shaic.claim.fvrdetails.view.ViewFVRDTO;
import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface InternalNotesPageView extends GMVPView {
	
	public void result();
}
