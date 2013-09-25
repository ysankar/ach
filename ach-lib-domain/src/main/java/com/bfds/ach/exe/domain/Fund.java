package com.bfds.ach.exe.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

@RooJpaEntity(table="fund")
@RooJavaBean
@RooToString(excludeFields = { "id", "version" })
public class Fund implements Serializable{
	
	@Column(name="fundNo",unique = true, nullable = false)
	private Integer fundNo;
	
	@Column(name="fundName",length = 40)
	private String fundName;
	
	@Column(name="pricingFlag")
	private Integer pricingFlag;
	
	@Column(name="floatDays")
	private Integer floatDays;
	
	@Column(name="loadFlag")
	private Integer loadFlag;
	
	@ManyToOne
	@JoinColumn(name = "demandDepositAccountFk", nullable = false)
	private DemandDepositAccount dda;
}
