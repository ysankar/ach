package com.bfds.ach.calc.exe.domain;

import com.google.common.base.Preconditions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@RooJavaBean(settersByDefault = false)
@RooToString
@Entity
@Table(name = "calculations")
@Configurable
public class Calculation implements java.io.Serializable, ApplicationContextAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calc_type", nullable = false)
    private String calcType;
    /**
     * The unique id of the executable. It can be the id of a spring bean.
     */
    @Column(name = "class_name", nullable = false, unique = true)
    private String executableId;

    @Column(name = "description")
    private String description;
    /**
     * Execution parameters for executable identified by {@link #executableId}
     */
    @ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="name")
    @Column(name="value", nullable=false)
    @CollectionTable(name="calculation_parameter", joinColumns=@JoinColumn(name="calculation_id"))
    Map<String, String> calculationParameters = new HashMap<String, String>();

    /**
     * The executable. This is most likely a spring bean.
     */
    @Transient
    private Object executable;

    @Transient
    private ApplicationContext applicationContext;

    @Deprecated
    /**
     * @deprecated for public use by frameworks only
     */
    public Calculation() {}

    @PostLoad
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(executableId, "executableId is null");
        executable = applicationContext.getBean(executableId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Preconditions.checkNotNull(applicationContext, "applicationContext is null");
        this.applicationContext = applicationContext;
    }

    /**
     * A fluent builder
     */
    public static final class Factory {
        private Long id;

        private String calcType;

        private String executableId;

        private String description;

        Map<String, String> calculationParameters = new HashMap<String, String>();

        private Object executable;

        public Factory id(Long id) {
            this.id = id;
            return this;
        }

        public Factory calcType(String calcType) {
            this.calcType = calcType;
            return this;
        }

        public Factory executableId(String executableId) {
            this.executableId = executableId;
            return this;
        }

        public Factory description(String description) {
            this.description = description;
            return this;
        }

        public Factory calculationParameters(Map<String, String> calculationParameters) {
            this.calculationParameters = calculationParameters;
            return this;
        }

        public Factory executable(Object executable) {
            this.executable = executable;
            return this;
        }

        public Calculation get() {
            @SuppressWarnings("deprecated")
            final Calculation calculation = new Calculation();
            calculation.id = id;
            calculation.description = description;
            calculation.executable = executable;
            Preconditions.checkNotNull(calculation.calcType = calcType, "calcType is null");
            Preconditions.checkNotNull(calculation.executableId = executableId, "executableId is null");
            return calculation;
        }
    }
}
