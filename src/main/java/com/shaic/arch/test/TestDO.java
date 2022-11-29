package com.shaic.arch.test;

import com.shaic.arch.table.AbstractTableDTO;

public class TestDO extends AbstractTableDTO{
	private Long id;
	private String name;
	private Long salary;

	public TestDO(Long id,String name, Long salary)
	{
		this.id = id;
		this.name = name;
		this.salary = salary;
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

	public void setName(String name) {
		this.name = name;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

}
