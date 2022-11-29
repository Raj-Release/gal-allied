package com.shaic.claim.lumen.policyupload;

import com.shaic.arch.table.Searchable;

public interface LumenPolicyUploadDocumentsView extends Searchable{

	public void reloadDocumentPopup(PolicyDocumentTableDTO docTblDTO);

}
