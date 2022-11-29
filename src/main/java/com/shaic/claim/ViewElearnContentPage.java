package com.shaic.claim;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;


public class ViewElearnContentPage  extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ElearnContentTable eContentTable;
	
	private List<ELearnDto> eContentList;

	private VerticalLayout wholeLayout;
	public void initView(List<ELearnDto> eDtoList, Window parent) {
		
		this.eContentList = eDtoList;
		wholeLayout = new VerticalLayout();
		eContentTable.init("Please View the Document", false, false);
		eContentTable.setTableList(eContentList);
		wholeLayout.addComponent(eContentTable);
		final Button closeBtn = new Button("Close");
		closeBtn.setData(parent);
		closeBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window parentWindow = (Window)closeBtn.getData();		
				parentWindow.close();
			}
		});
		
		wholeLayout.addComponent(closeBtn);
		wholeLayout.setComponentAlignment(closeBtn, Alignment.BOTTOM_CENTER);
		
		
		
		setCompositionRoot(wholeLayout);		
	}

}
