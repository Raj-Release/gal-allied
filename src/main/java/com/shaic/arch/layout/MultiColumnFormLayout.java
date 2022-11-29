package com.shaic.arch.layout;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;

@SuppressWarnings("serial")
public class MultiColumnFormLayout extends GridLayout{
    private final int rows;
    private int componentCount;
    
    public MultiColumnFormLayout(int columns, int componentCount) {
        super(columns, 1);    // will make as many FormLayouts as columns were defined
        
        final int additionalRow = (componentCount % columns == 0) ? 0 : 1;
        this.rows = (componentCount / columns) + additionalRow;
        
        for (int i = 0; i < columns; i++)    {
            final FormLayout formLayout = new FormLayout();
            super.addComponent(formLayout, i, 0, i, 0);
        }
    }
    
    @Override
    public void addComponent(Component component) {
        final int column = componentCount / rows;
        componentCount++;
        final FormLayout formLayout = (FormLayout) getComponent(column, 0);
        formLayout.addComponent(component);
    }
    
    @Override
    public void addComponent(Component component, int column1, int row1, int column2, int row2) throws OverlapsException, OutOfBoundsException {
        throw new UnsupportedOperationException("Sorry, this is a FormLayout delegate!");
    }
}