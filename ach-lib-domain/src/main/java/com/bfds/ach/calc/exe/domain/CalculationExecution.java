package com.bfds.ach.calc.exe.domain;

import org.springframework.roo.addon.equals.RooEquals;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.util.Date;

@RooJavaBean
@RooToString
@RooEquals(excludeFields = "id")
@Entity
@Table(name = "calculation_execution")
public class CalculationExecution implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calc_type", nullable = false)
    private String calcType;

    @Column(name = "class_name", nullable = false)
    private String executableId;

    @Column(name = "description")
    private String description;

    @Column(name = "calculation_parameters", length = 5000)
    private String calculationParameters;

    @Column(name = "start_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "exit_Status")
    @Enumerated(EnumType.STRING)
    private ExitStatus exitStatus;

    @Column(name = "exit_message", length = 5000)
    private String exitMessage;

    public CalculationExecution() {}

    public CalculationExecution(Calculation calculation) {
        this.executableId = calculation.getExecutableId();
        this.calcType = calculation.getCalcType();
        this.description = calculation.getDescription();
        this.calculationParameters = calculation.getCalculationParameters() == null ? null : calculation.getCalculationParameters().toString();
    }

    public enum ExitStatus {
        COMPLETED, ERROR;
    }

    /**
     * Is the calculation currently running.
     * @return tru if the calculation is in progress false if the calculation has completed.
     */
    public boolean isRunning() {
        return endTime == null;
    }
}
