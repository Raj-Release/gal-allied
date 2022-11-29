package com.shaic.claim.pancard.search.pages;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;


/**
 * @author ntv.narenj
 *
 */
public interface SearchUploadPanCardView extends Searchable  {
	public void list(Page<SearchUploadPanCardTableDTO> tableRows);
	public void init();
	public void showErrorPopUp(String emsg);
}
