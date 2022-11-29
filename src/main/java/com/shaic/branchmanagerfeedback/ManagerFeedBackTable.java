package com.shaic.branchmanagerfeedback;

import java.util.Calendar;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;

public class ManagerFeedBackTable extends ViewComponent{
	
	private static final long serialVersionUID = 2499717380886927304L;
	private Table table;
	BeanItemContainer<Integer> date = new BeanItemContainer<Integer>(Integer.class);
	public Object[] VISIBLE_COLUMNS = new Object[] { };
	
	public void init() {
		table =new Table();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
	      int month = calendar.get(Calendar.MONTH);      // 0 to 11
	      int day = calendar.get(Calendar.DAY_OF_MONTH);
		for(Integer i=0;i<=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);i++) {
			table.setColumnHeader(i, i.toString());
		}
	}
	
}
