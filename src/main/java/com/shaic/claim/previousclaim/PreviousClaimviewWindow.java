package com.shaic.claim.previousclaim;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class PreviousClaimviewWindow extends Window {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7181320560132564580L;
	
	private VerticalLayout mainlayout;
	
	private CheckBox policywise;
	
	private CheckBox insuredwise;
	
	private ComboBox yearwise;
	
	@Inject 
	private PreviousClaimViewPage previousclaimviewPage;
	
	private List<PreviousClaimsTableDTO> previousClaimTableDTO;

	@PostConstruct
	public void init()
	{
	
		setWidth("800px");
		setHeight("280px");
		setModal(true);
		setClosable(true);
		setResizable(true);
	
		setCaption("View PreviousCliam Details");
//		previousclaimviewPage.init(previousClaimTableDTO);
		
		setContent(previousclaimviewPage);
		
	}

	

}
