package com.shaic.arch.table;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;

@SuppressWarnings({"serial"})
public class PagerUI extends HorizontalLayout {
	
	private Button btnNext;
//	private Button btnLast;
	private Button btnFirst;
	private Button btnPrevious;
	
	private Label currentPageLbl;

	
	public static final int NO_OF_RECORDS_PER_PAGE = 10;
	
	private PagerListener listener;
	
	private Pageable pageable;
	
	public Pageable getPageable() {
		return pageable;
	}

	public PagerUI()
	{
		this.pageable = new Pageable(1, NO_OF_RECORDS_PER_PAGE, false);
		
		btnFirst = new Button();
		
		btnFirst.setIcon(FontAwesome.FAST_BACKWARD);
		btnPrevious = new Button(FontAwesome.BACKWARD);
		
		btnNext = new Button(FontAwesome.FORWARD);
//		btnLast = new Button(FontAwesome.FAST_FORWARD);
		
		currentPageLbl = new Label("");
		
		GridLayout fPanel = new GridLayout(2, 1);
		fPanel.addComponent(btnFirst,0, 0);
		fPanel.addComponent(btnPrevious,1, 0);
		
		GridLayout lPanel = new GridLayout(2,1);
		lPanel.addComponent(btnNext, 0, 0);
//		lPanel.addComponent(btnLast, 1, 0);
		
		GridLayout pageDetailsPanel = new GridLayout(3, 1);
		pageDetailsPanel.addComponent(currentPageLbl, 0, 0);
		
		HorizontalLayout footerLayout = new HorizontalLayout();
		footerLayout.setWidth("100%");
		footerLayout.setHeight("10px");
		
		footerLayout.addComponent(fPanel);
		footerLayout.setComponentAlignment(fPanel, Alignment.MIDDLE_LEFT);
		
		footerLayout.addComponent(pageDetailsPanel);
		footerLayout.setComponentAlignment(pageDetailsPanel, Alignment.MIDDLE_CENTER);
		
		footerLayout.addComponent(lPanel);
		footerLayout.setComponentAlignment(lPanel, Alignment.MIDDLE_RIGHT);
		
		this.addComponent(footerLayout);
		this.setWidth("100%");
		
		
		
		
		btnFirst.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				handlePageFirstAndLast();
				pageable.first();
				listener.changePage();
			}
		});
		
		btnPrevious.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				handlePageFirstAndLast();
				pageable.previousOrFirst();
				listener.changePage();
			}
		});
		
		btnNext.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				handlePageFirstAndLast();
				pageable.next();
				listener.changePage();
			}
		});
		
		if(currentPageLbl != null && currentPageLbl.getValue() != null && currentPageLbl.getValue().equals("0") || currentPageLbl.getValue().equals("") ){
			btnFirst.setEnabled(false);
			btnPrevious.setEnabled(false);
			btnNext.setEnabled(false);
		}else{
			btnFirst.setEnabled(true);
			btnPrevious.setEnabled(true);
			btnNext.setEnabled(true);
		}
		
//		btnLast.addClickListener(new Button.ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//				handlePageFirstAndLast(pageable);
//				listener.changePage(pageable.last());
//			}
//		});
	}
	
	public void handlePageFirstAndLast()
	{
		boolean firstFlag = true;
		if(!pageable.hasPrevious())
		{
			this.btnFirst.setEnabled(!firstFlag);
			this.btnPrevious.setEnabled(!firstFlag);
			
		}
		else
		{
			this.btnFirst.setEnabled(firstFlag);
			this.btnPrevious.setEnabled(firstFlag);
		}
		
		this.btnNext.setEnabled(pageable.hasNext());
	}
	
	@SuppressWarnings("rawtypes")
	public void setPageDetails(Page page)
	{
		pageable.setPageNumber(page.getPageNumber());
		pageable.setNextFlag(page.isHasNext());
		handlePageFirstAndLast();
		currentPageLbl.setValue(""+ page.getPageNumber());
		btnNext.setEnabled(page.isHasNext());
	}
	
	public void addListener(PagerListener pageListener)
	{
		this.listener = pageListener;
	}
	
	public void hasMoreRecords(boolean flag)
	{
		this.btnNext.setEnabled(flag);
	}
	
	public void resetPage()
	{
		this.currentPageLbl.setValue("");
		if(currentPageLbl != null && currentPageLbl.getValue() != null && currentPageLbl.getValue().equals("0") || currentPageLbl.getValue().equals("") ){
			btnFirst.setEnabled(false);
			btnPrevious.setEnabled(false);
			btnNext.setEnabled(false);
		}else{
			btnFirst.setEnabled(true);
			btnPrevious.setEnabled(true);
			btnNext.setEnabled(true);
		}
	}
}
