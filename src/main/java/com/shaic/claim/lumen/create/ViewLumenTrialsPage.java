package com.shaic.claim.lumen.create;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.LumenTrialsDTO;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ViewLumenTrialsPage extends ViewComponent{

	@Inject
	private ViewLumenTrialsTable viewlumenTrialsTable;

	@Inject
	private LumenDbService lumenDbService;

	private VerticalLayout mainLayout;

	public void loadData(Long argLumenKey) {		
		viewlumenTrialsTable.init("", false, false);
		List<LumenTrialsDTO> lumentrailsList = lumenDbService.getLumenTrailsData(argLumenKey);
		viewlumenTrialsTable.setTableList(lumentrailsList);
		mainLayout = new VerticalLayout(viewlumenTrialsTable);
		setCompositionRoot(mainLayout);
	}

	public VerticalLayout showViewTrailsPopup(Long argLumenKey) {		
		loadData(argLumenKey);
		return mainLayout;
	}
}
