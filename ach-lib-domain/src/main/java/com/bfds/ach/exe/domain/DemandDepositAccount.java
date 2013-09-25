package com.bfds.ach.exe.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import com.bfds.ach.domain.reference.SystemName;


@RooJpaEntity(table="demandDepositAccount")
@RooJavaBean
@RooToString(excludeFields = { "id", "version" })
public class DemandDepositAccount implements Serializable{
	
	@Column(name="mgmtCompanyNo",unique = true, nullable = false)
	private Integer mgmtCompanyNo;
	
	@Column(name="mgmtCode",length=2)
	private String mgmtCode;
	
	@Column(name="mgmtCodeName",length=40)
	private String mgmtCodeName;
	
	@Column(name="dda1")
	private Integer dda1;
	
	@Column(name="dda2")
	private Integer dda2;
	
	@Column(name="oldCashBookNo")
	private Integer oldCashBookNo;
	
	@Enumerated(EnumType.STRING)
	@Column(name="systemName",length=3)
	private SystemName systemName;
	
	@Column(name="numberOfFunds")
	private Integer numberOfFunds;
	
	@Column(name="cashBookNo")
	private Integer cashBookNo;

	@Column(name="ddpsUsageFlag")
	private Integer ddpsUsageFlag;
	
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "dda")
	private Set<Fund> fund;
}
