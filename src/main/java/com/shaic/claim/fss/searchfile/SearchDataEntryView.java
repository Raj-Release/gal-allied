/**
 * 
 */
package com.shaic.claim.fss.searchfile;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

/**
 * @author ntv.vijayar
 *
 */
public interface SearchDataEntryView extends Searchable  {
	public void list(Page<SearchDataEntryTableDTO> tableRows);
}

