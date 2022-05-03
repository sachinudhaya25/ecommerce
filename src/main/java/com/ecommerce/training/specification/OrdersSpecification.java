package com.ecommerce.training.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.training.models.Orders;

public class OrdersSpecification implements Specification<Orders> {

	private SearchCriteria criteria;
	public OrdersSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}
	@Override
	public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		 if (criteria.getOperation().equalsIgnoreCase(">")) {
	            return builder.greaterThanOrEqualTo(
	              root.<String> get(criteria.getKey()), criteria.getValue().toString());
	        } 
	        else if (criteria.getOperation().equalsIgnoreCase("<")) {
	            return builder.lessThanOrEqualTo(
	              root.<String> get(criteria.getKey()), criteria.getValue().toString());
	        } 
	        else if (criteria.getOperation().equalsIgnoreCase(":")) {
	            if (root.get(criteria.getKey()).getJavaType() == String.class) {
	                return builder.like(
	                  root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
	            } else {
	                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
	            }
	        }else if (criteria.getOperation().equalsIgnoreCase("--")) {
	        	System.out.println("runnnng"+criteria.getKey()+criteria.getValue());
	            return builder.like(
	  	              root.<String> get(criteria.getKey()), criteria.getValue().toString());
	  	        } 
		return null;
	}

	

}
