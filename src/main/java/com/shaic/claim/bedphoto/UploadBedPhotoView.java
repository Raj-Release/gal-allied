package com.shaic.claim.bedphoto;

import com.shaic.arch.GMVPView;

public interface UploadBedPhotoView extends GMVPView{
	
	public void updateTableValues(Long intimationKey);
	
	public void buildSuccessLayout();

}
