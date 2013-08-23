package com.bfds.ach.calc.exe.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;

/**
 * A request to execute the {@link Calculation#getExecutableId()}.
 * Multiple  {@link CalculationExecutionRequest} can be submitted and the order of execution is determined by
 * {@link #priority} with lower values being picked first.
 */
@RooJavaBean
@RooToString
@Entity
@Table(name = "calculation_execution_request")
public class CalculationExecutionRequest implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Byte priority;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;

}
