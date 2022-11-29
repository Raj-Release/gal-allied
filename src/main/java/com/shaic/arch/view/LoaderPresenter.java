package com.shaic.arch.view;

import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@SuppressWarnings("serial")
@ViewInterface(LoaderView.class)
public class LoaderPresenter extends AbstractMVPPresenter<LoaderView>{

	public static final String LOAD_URL = "load_url_from_any_screen";
	public static final String LOAD_TARGET_FRAME = "load_target_view";
	
	@Override
	public void viewEntered() {
	}
	
	
	public void handleTarget(@Observes @CDIEvent(LoaderPresenter.LOAD_TARGET_FRAME) final ParameterDTO parameters) {
		String url = (String) parameters.getPrimaryParameter(String.class);
		view.loadTarget(url, null, null);
	}
	
	

}
