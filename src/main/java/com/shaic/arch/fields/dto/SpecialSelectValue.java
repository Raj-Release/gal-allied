package com.shaic.arch.fields.dto;

public class SpecialSelectValue extends SelectValue {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SpecialSelectValue(Long id, String value,String commonValue, String specialValue) {
		super(id, value, commonValue);
		this.specialValue = specialValue;
	}

	public SpecialSelectValue() {
		super();
	}

	private String specialValue;
	
	private Long specialId;

	public String getSpecialValue() {
		return specialValue;
	}

	public void setSpecialValue(String specialValue) {
		this.specialValue = specialValue;
	}

	public Long getSpecialId() {
		return specialId;
	}

	public void setSpecialId(Long specialId) {
		this.specialId = specialId;
	}

}
