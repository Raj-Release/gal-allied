package com.shaic.arch.view;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.vaadin.v7.ui.Label;

public class LoaderViewImpl extends AbstractMVPView implements LoaderView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6682178536776671314L;

	@Override
	public void loadTarget(String target, Object primaryParameter, Object[] secondaryParameter) {
		fireViewEvent(target, primaryParameter, secondaryParameter);
	}
	
	@PostConstruct
	public void init()
	{
		setCompositionRoot(new Label("Loading...."));
		System.out.println("loader loaded....");
	}

	public void initView(String url)
	{
		fireViewEvent(LoaderPresenter.LOAD_TARGET_FRAME, url);
	}
	@Override
	public void resetView() {
		
	}

}
