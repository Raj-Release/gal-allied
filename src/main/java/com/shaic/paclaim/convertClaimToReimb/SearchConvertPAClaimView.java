package com.shaic.paclaim.convertClaimToReimb;

import java.util.Map;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;

public interface SearchConvertPAClaimView extends GMVPView {

	public void list(Page<SearchConvertClaimTableDto> tableRows);

	public void init(Map<String,Object> defaultValues);

}
