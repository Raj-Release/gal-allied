package com.shaic.domain.gmcautomailer;


import java.util.List;

import com.shaic.arch.GMVPView;
import com.shaic.arch.table.Page;

public interface GmcAutomailerView extends GMVPView{

	public void list(GmcAutomailerTableDTO tableRows);
}
