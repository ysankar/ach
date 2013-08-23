package com.bfds.ach.calc.validation.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;

@RooJavaBean
@RooToString
@Entity
@Table(name = "claimant_validation")
public class ValidationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claimant_claim_id", nullable = false)
    private Long claimantClaimId;

    @Column(name = "validation_Type", nullable = false)
    private String validationType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "comments", nullable = true)
    private String comments;


}
