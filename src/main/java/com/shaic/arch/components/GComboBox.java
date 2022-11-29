package com.shaic.arch.components;

import java.util.List;

import com.vaadin.v7.ui.ComboBox;

public class GComboBox extends ComboBox{

	/**
	 * 
	 */
	private static final long serialVersionUID = -342594178412157281L;
	
	private boolean inFilterMode;

    @Override
    public void containerItemSetChange (com.vaadin.v7.data.Container.ItemSetChangeEvent event)
    {
            if (inFilterMode) {
                    super.containerItemSetChange(event);
            }
    }

    @Override
    protected List<?> getOptionsWithFilter (boolean needNullSelectOption)
    {
            try {
                    inFilterMode = true;
                    return super.getOptionsWithFilter(needNullSelectOption);
            }
            finally {
                    inFilterMode = false;
            }
    }

}
