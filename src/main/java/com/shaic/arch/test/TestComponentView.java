package com.shaic.arch.test;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;

public interface TestComponentView extends GMVPView{
	public void testDiagnosisComponent();
	
	public void newItemAdded(SelectValue value);
	
	public void showSearchResult(List<SelectValue> list);
}
