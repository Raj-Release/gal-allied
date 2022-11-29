package com.shaic.claim.premedical.dto;

import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.ui.ComboBox;

/**
 * 
 * @author sivakumar.k
 *This inteface need to be extend when user need to listen the GEditabled table cell combo box selection.  
 */
public interface TableCellSelectionHandler
{
	public void itemSelected(ComboBox field, ValueChangeEvent event);
}
