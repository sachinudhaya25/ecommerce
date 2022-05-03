package com.ecommerce.training.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ecommerce.training.config.Errors;
import com.ecommerce.training.models.Book;

@Component
public class BookValidator {

	public List<Errors> validateRequest(Book book) {
		List<Errors> errors=new ArrayList<>();
		if(book.getName()==null)
		{
		Errors error=new Errors();
		error.setTarget("Name");
		error.setMessage("Name is mandatory");
		errors.add(error);
		}
		if(book.getBookType()==null)
		{
		Errors error=new Errors();
		error.setTarget("Book Type");
		error.setMessage("Book Type is mandatory");
		errors.add(error);
		}
		
		return errors;
	}

	
}
