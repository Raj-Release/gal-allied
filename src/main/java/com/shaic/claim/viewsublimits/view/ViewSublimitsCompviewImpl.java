package com.shaic.claim.viewsublimits.view;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.domain.ComprehensiveSublimitDTO;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewSublimitsCompviewImpl extends AbstractMVPView {

	
	@Inject
	private ViewSublimitsCompTable viewSublimtTable;
	
	private VerticalLayout mainLayout;
	
	public void init(List<ComprehensiveSublimitDTO> subLimtList){
		viewSublimtTable.init("", false, false);
		Integer sno = 1;
		for (ComprehensiveSublimitDTO sublimitObject : subLimtList) {
			if(sublimitObject.getSubLimitAmt() != null && sublimitObject.getSubLimitAmt() != 0l){
				sublimitObject.setSno(sno);
				viewSublimtTable.addBeanToList(sublimitObject);
				sno++;
			    }
		}
		mainLayout = new VerticalLayout(viewSublimtTable);
		setCompositionRoot(mainLayout);
	}
}
