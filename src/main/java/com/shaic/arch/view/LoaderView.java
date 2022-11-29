package com.shaic.arch.view;

import com.shaic.arch.GMVPView;

public interface LoaderView extends GMVPView {
	public void loadTarget(String target, Object primaryParameter, Object[] secondaryParameter);
	
	public void initView(String url);
}
