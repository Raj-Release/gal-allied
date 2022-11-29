package com.shaic.arch.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.SuggestingContainer.SuggestionFilter;
import com.shaic.domain.MasterService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Container.Filter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.filter.UnsupportedFilterException;

public class EmployeeContainer extends BeanItemContainer<SelectValue> {

	private ReimbursementService service;

	private boolean newItemAdded = false;

	private String comboBoxValue = "";

	public boolean isNewItemAdded() {
		return newItemAdded;
	}

	public void setNewItemAdded(boolean newItemAdded) {
		this.newItemAdded = newItemAdded;
	}

	public EmployeeContainer(ReimbursementService service) throws IllegalArgumentException {
		super(SelectValue.class);
		this.service = service;
	}

	@Override
	protected void addFilter(Filter filter) throws UnsupportedFilterException {
		EmployeeFilter employeeFilter = (EmployeeFilter) filter;
		filterItems(employeeFilter.getFilterString());
	}

	private void filterItems(String filterString) {
		if (!isNewItemAdded())
		{
			List<SelectValue> allItemIds2 = new CopyOnWriteArrayList<SelectValue>(this.getAllItemIds());

			List<SelectValue> selectValueList = new ArrayList<SelectValue>();

			Iterator<SelectValue> iterator = allItemIds2.iterator();

			while (iterator.hasNext()) {
				this.removeItem(iterator.next());
			}
			List<SelectValue> allItemIds3 = this.getAllItemIds();
			allItemIds3.clear();
			allItemIds3 = null;
			allItemIds2.clear();
			allItemIds2 = null;

			if(filterString.isEmpty() && SHAUtils.employeeListValue != null && comboBoxValue.isEmpty()){
				selectValueList = SHAUtils.employeeListValue.getItemIds();
			}else{
				if(comboBoxValue != null && ! comboBoxValue.isEmpty() && filterString.isEmpty()){
					selectValueList = service.employeeNameCriteriaSearchForRRC(comboBoxValue.toUpperCase());
				}else{
					selectValueList = service.employeeNameCriteriaSearchForRRC(filterString.toUpperCase());
				}
			}
			if(selectValueList != null){
				this.addAll(selectValueList);
			}
		}
		this.setNewItemAdded(false);

	}

	public void setSelectedBean(SelectValue selectValue) {
		removeAllItems();
		addBean(selectValue);
	}

	public static class EmployeeFilter implements Container.Filter {

		private String filterString;


		public EmployeeFilter(String filterString) {
			this.filterString = filterString;
		}

		public String getFilterString() {
			return filterString;
		}

		@Override
		public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
			// will never be used and can hence always return false
					return false;
		}

		@Override
		public boolean appliesToProperty(Object propertyId) {
			// will never be used and can hence always return false
			return false;
		}

	}

	public String getComboBoxValue() {
		return comboBoxValue;
	}

	public void setComboBoxValue(String comboBoxValue) {
		this.comboBoxValue = comboBoxValue;
	}

}
