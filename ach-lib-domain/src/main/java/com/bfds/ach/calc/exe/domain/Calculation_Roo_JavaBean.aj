// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bfds.ach.calc.exe.domain;

import java.util.Map;
import org.springframework.context.ApplicationContext;

privileged aspect Calculation_Roo_JavaBean {
    
    public Long Calculation.getId() {
        return this.id;
    }
    
    public String Calculation.getCalcType() {
        return this.calcType;
    }
    
    public String Calculation.getExecutableId() {
        return this.executableId;
    }
    
    public String Calculation.getDescription() {
        return this.description;
    }
    
    public Map<String, String> Calculation.getCalculationParameters() {
        return this.calculationParameters;
    }
    
    public Object Calculation.getExecutable() {
        return this.executable;
    }
    
    public ApplicationContext Calculation.getApplicationContext() {
        return this.applicationContext;
    }
    
}