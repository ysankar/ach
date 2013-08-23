package com.bfds.ach.calc.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity to hold caliculation configuration data. The data is used by one or more calculations.
 * There must be exactly one instance of this class in the database at any given time.
 */
@RooJavaBean(settersByDefault = false)
@RooToString
@Entity
@Table(name = "event_calc_config")
public class EventCalcConfig implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "harm_start_date", nullable = false)
    private Date harmStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "harm_end_date", nullable = false)
    private Date harmEndDate;
    
    public EventCalcConfig()
    {}
    public EventCalcConfig(Date harmstartDate, Date harmEndDate)
    {
    	this.harmStartDate = harmstartDate;
    	this.harmEndDate = harmEndDate;
    }

}
