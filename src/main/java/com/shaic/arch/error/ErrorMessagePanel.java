package com.shaic.arch.error;

import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;

public class ErrorMessagePanel extends VerticalLayout {

	private static final long serialVersionUID = -2373410804253153016L;
	
	private Button btClose = new Button("[X]");
	
    private Label lbl;
    private String content = ""; 
    
    public Button getBtClose() {
        return btClose;
    }
    public ErrorMessagePanel(String content, Button.ClickListener listener) {
    	this.content = content;
        show();
        this.btClose.addClickListener(listener);
    }
    public void showAgain() {
        removeAllComponents();
        show();
    }

    public void show() {
    	this.setCaption("Error");
        btClose.setStyleName("link");
        addComponent(btClose);
        setComponentAlignment(btClose, Alignment.TOP_RIGHT);
        setWidth("-1px");
        setHeight("-1px");
        lbl = new Label(this.content, ContentMode.HTML);
        lbl.addStyleName("errMessage");
        addComponent(lbl);
        setComponentAlignment(lbl, Alignment.MIDDLE_CENTER);
    }
    
    public void show(String errorMessage)
    {
    	this.content = "<div class='errMessage'>" + errorMessage + "</div>";
    	this.showAgain();
    }
}
