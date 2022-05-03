package com.ecommerce.training.specification;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.training.models.Bill;


public class BillsSpecification implements Specification<Bill> {

	private SearchCriteria criteria;
	private Date fromDate;
	private Date toDate;
	public BillsSpecification(SearchCriteria criteria) {
		super();
		this.criteria = criteria;
		this.fromDate=criteria.getValue1();
		this.toDate= criteria.getValue2();
	}
	
	@Override
	public Predicate toPredicate(Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Predicate p=builder.conjunction();
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
        
            return builder.between(root.get(criteria.getKey()), fromDate, toDate);
           // builder.and(p,builder.between(root.get(criteria.getKey()), fromDate, toDate));
  	        }
//        else if (criteria.getOperation().equalsIgnoreCase("++")) {
//            System.out.println("&&&&&&&&&&&&&&");
//            return builder.equal(root.get(criteria.getKey()).in(Customer), criteria.getValue());
//          
//  	        }
	return null;
	}

}
