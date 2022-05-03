package com.ecommerce.training.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
public class Trackable implements Serializable {

private static final long serialVersionUID = 1346562084432072428L;
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	public Date createdDate;
	
	@CreatedBy
	@Column(name = "created_by")
	public Long createdBy;
	
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date")
	public Date modifiedDate;
	
	@LastModifiedBy
	@Column(name = "modified_by")
	public Long modifiedBy;
	
	public Trackable()
	{
		createdDate=new Date();
	}
}
