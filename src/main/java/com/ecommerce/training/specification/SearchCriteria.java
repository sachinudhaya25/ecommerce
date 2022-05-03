package com.ecommerce.training.specification;



import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

	private String key;
    private String operation;
    private Object value;
    private Date value1;
    private Date value2;
    //this is constructor is must
	public SearchCriteria(String key, String operation, Object value) {
		this.key = key;
		this.operation = operation;
		this.value = value;
	}
	public SearchCriteria(String key, String operation, Date value1, Date value2) {
		super();
		this.key = key;
		this.operation = operation;
		this.value1 = value1;
		this.value2 = value2;
	}
    
    
}
