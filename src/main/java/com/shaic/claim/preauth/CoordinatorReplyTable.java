package com.shaic.claim.preauth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.CoordinatorDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@SuppressWarnings("serial")
public class CoordinatorReplyTable extends GBaseTable<CoordinatorDTO>
{
	public static final Object[] COLUMN_HEADER = new Object[] {
		"requestType",
		"requesterRemarks",
		"viewFile",
		"coordinatorRemarks"
	};

	@Override
	public void removeRow() {
				
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<CoordinatorDTO>(CoordinatorDTO.class));
		table.setVisibleColumns(COLUMN_HEADER);
		
		List<CoordinatorDTO> coordinatorReplyList = new ArrayList<CoordinatorDTO>();
		CoordinatorDTO reply = new CoordinatorDTO();
		for(int i=0;i<5;i++)
		{
			reply.setCoordinatorRemarks("Traslation Completed pls view uploaded file for reference");
			reply.setCoordinatorReplyDate(new Date());
			reply.setRequesterRemarks("Please translate the Investigation Report");
			reply.setViewFile("TranslatedPageOfInvestinagionReport");
			coordinatorReplyList.add(reply);
		}
		
		setTableList(coordinatorReplyList);
		
		table.setHeight("100px");
	}

	@Override
	public void tableSelectHandler(CoordinatorDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "coordinator-reply-";
	}

}
