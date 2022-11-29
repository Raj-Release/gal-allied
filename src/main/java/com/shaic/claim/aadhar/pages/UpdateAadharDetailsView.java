package com.shaic.claim.aadhar.pages;

import com.shaic.arch.GMVPView;

public interface UpdateAadharDetailsView extends GMVPView{
	
	public void updateTableValues(Long intimationKey);
	public void result();
	public void popforAadhar();
	public void aadharfailureLayout();

}
