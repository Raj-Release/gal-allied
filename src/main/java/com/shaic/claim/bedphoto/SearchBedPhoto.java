package com.shaic.claim.bedphoto;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Searchable;

public interface SearchBedPhoto extends Searchable {
	public void init();
	public void list(Page<SearchBedPhotoTableDTO> tableDto);
}
