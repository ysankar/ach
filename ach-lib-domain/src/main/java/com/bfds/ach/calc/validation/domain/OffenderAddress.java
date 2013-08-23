package com.bfds.ach.calc.validation.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
public class OffenderAddress {
    private String address1;
    private String city;
    private String stateCode;
 
}
