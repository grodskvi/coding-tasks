package taxcalculator.dto;

import java.math.BigDecimal;

public class ItemDTO {
	
	private String description;
	private BigDecimal unitPrice;
	private int count;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "ItemDTO [description=" + description + ", unitPrice="
				+ unitPrice + ", count=" + count + "]";
	}
	
	

}
