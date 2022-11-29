package com.shaic.arch.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.filter.UnsupportedFilterException;

/**
 * This is a specialized {@link BeanItemContainer} which redefines the filtering
 * functionality by overwriting method
 * {@link com.vaadin.v7.data.util.AbstractInMemoryContainer#addFilter(Filter)}.
 * This method is called internally by the filtering code of a ComboBox.
 */
public class InsuranceDiagnosisContainer extends BeanItemContainer<SelectValue> {

  private MasterService service;
  
	private boolean newItemAdded = false;
	
	private String comboBoxValue = "";

  public boolean isNewItemAdded() {
	return newItemAdded;
}

public void setNewItemAdded(boolean newItemAdded) {
	this.newItemAdded = newItemAdded;
}

public InsuranceDiagnosisContainer(MasterService service) throws IllegalArgumentException {
    super(SelectValue.class);
    this.service = service;
  }

  /**
   * This method will be called by ComboBox each time the user has entered a new
   * value into the text field of the ComboBox. For our custom ComboBox class
   * {@link SuggestingComboBox} it is assured by
   * {@link SuggestingComboBox#buildFilter(String, com.vaadin.v7.shared.ui.combobox.FilteringMode)}
   * that only instances of {@link SuggestionFilter} are passed into this
   * method. We can therefore safely cast the filter to this class. Then we
   * simply get the filterString from this filter and call the database service
   * with this filterString. The database then returns a list of country objects
   * whose country names begin with the filterString. After having removed all
   * existing items from the container we add the new list of freshly filtered
   * country objects.
   */
  @Override
  protected void addFilter(Filter filter) throws UnsupportedFilterException {
    SuggestionFilter suggestionFilter = (SuggestionFilter) filter;
    filterItems(suggestionFilter.getFilterString(), suggestionFilter.getUserId());
  }

  private void filterItems(String filterString, String userId) {
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
		
		if(comboBoxValue != null && ! comboBoxValue.isEmpty() && filterString.isEmpty()){
			selectValueList = service.getIcdCodeList(comboBoxValue.toUpperCase());
		}else{
			selectValueList = service.getIcdCodeList(filterString.toUpperCase());
		}

		if(selectValueList != null){
	    /*for (SelectValue selectValue : selectValueList) {
	    	this.addBean(selectValue);
		}*/
			this.addAll(selectValueList);
		}
	}
	this.setNewItemAdded(false);
	
  }
  
  public void setSelectedBean(SelectValue selectValue) {
	    removeAllItems();
	    addBean(selectValue);
 }


  /**
   * The sole purpose of this {@link Filter} implementation is to transport the
   * current filterString (which is a private property of ComboBox) to our
   * custom container implementation {@link InsuranceDiagnosisContainer}. Our container
   * needs that filterString in order to fetch a filtered country list from the
   * database.
   */
  public static class SuggestionFilter implements Container.Filter {

    private String filterString;
    
    private String userId;
    
    public SuggestionFilter(String filterString) {
      this.filterString = filterString;
    }
    
    public SuggestionFilter(String filterString, String userId) {
        this.filterString = filterString;
        this.userId = userId;
      }

    public String getFilterString() {
      return filterString;
    }
    
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
