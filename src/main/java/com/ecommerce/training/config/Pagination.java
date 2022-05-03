package com.ecommerce.training.config;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {

	private Long totalCount;
	private Integer pageSize;
	private Integer totalPage;
	private Integer pageNumber;
	private Boolean isLast;
	private Boolean isFirst;
	
	public static <T> Pagination createPagination(Page<T> page)
	{
		Pagination pagination=new Pagination();
		pagination.setIsFirst(page.isFirst());
		pagination.setIsLast(page.isLast());
		pagination.setPageNumber(page.getNumber());
		pagination.setPageSize(page.getSize());
		pagination.setTotalCount(page.getTotalElements());
		pagination.setTotalPage(page.getTotalPages());
		return pagination;
	}
	
}
