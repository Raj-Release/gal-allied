package com.shaic.main.navigator.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuItem implements Serializable {
	
	private static final long serialVersionUID = -8496636363766112695L;
	private Long id;
	
	private String name;
	
	private String code;
	
	private boolean accessFlag = false;
	
	private List<MenuItem> childMenus;
	
	public MenuItem(Long id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.accessFlag = false;
		childMenus = new ArrayList<MenuItem>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public boolean isAccessFlag() {
		return accessFlag;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAccessFlag(boolean accessFlag) {
		this.accessFlag = accessFlag;
	}
	
	public List<MenuItem> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<MenuItem> childMenus) {
		this.childMenus = childMenus;
	}

	public boolean hasChild()
	{
		return this.childMenus.size() > 0;
	}
	
	public boolean isChild()
	{
		return this.childMenus.size() == 0;
	}
	
	public void addChild(MenuItem item)
	{
		this.childMenus.add(item);
	}
	
	public void removeChild(MenuItem item)
	{
		this.childMenus.remove(item);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
