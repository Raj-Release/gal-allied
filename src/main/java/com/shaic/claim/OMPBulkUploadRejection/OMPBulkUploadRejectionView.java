package com.shaic.claim.OMPBulkUploadRejection;

import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface OMPBulkUploadRejectionView extends GMVPView {

	void init();

	void exportToExcelList(
			List<SearchShadowProvisionDTO> errorLogDetailsForShadow);
	
	void showBulkUploadRejectionDetails(
			List<OMPBulkUploadRejectionResultDto> bulkUploadRejectionList);

	void initView(BeanItemContainer<SelectValue> statusContainer);
}