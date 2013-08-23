package com.bfds.ach.calc.validation.domain;

import com.google.common.collect.Sets;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import java.io.Serializable;
import java.util.Set;

@RooJavaBean
@RooToString
public class Offender implements Serializable{

    private String firstName;

    private String lastName;

    private String ssn;

    private String tin;

    OffenderAddress address;

    private Set<Long> offenderClaimId = Sets.newHashSet();
}
