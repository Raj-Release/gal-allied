package com.shaic.claim.viewsublimits.view;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.domain.SublimitFunObject;
import com.vaadin.v7.ui.VerticalLayout;

public class ViewSublimitsviewImpl extends AbstractMVPView {

	@Inject
	private ViewSublimitsTable table;
private VerticalLayout mainLayout;
	
	public void init(List<SublimitFunObject> nameAndAmt){
		table.init("", false, false);
		int rowCount =1;
		for (SublimitFunObject sublimitFunObject : nameAndAmt) {
			if(sublimitFunObject.getAmount() != null && sublimitFunObject.getAmount() != 0l){
				table.addBeanToList(sublimitFunObject);
			    sublimitFunObject.setSerialNumber(rowCount);
			    rowCount++;
			}
		}
		mainLayout = new VerticalLayout(table);
		setCompositionRoot(mainLayout);
	}

	
	
}
