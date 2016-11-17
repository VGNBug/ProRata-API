package com.pawsey.prorata.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;

public class TaxRuleEntity {

    @Id
    @Column(name = "tax_rule_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_rule_tax_rule_id_pk_seq")
    @SequenceGenerator(name = "tax_rule_tax_rule_id_pk_seq", sequenceName = "tax_rule_tax_rule_id_pk_seq")
    protected Integer taxRuleId;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS
    //----------------------------------------------------------------------
    @Column(name = "personal_allowance")
    protected BigDecimal personalAllowance;

    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @JsonBackReference("TaxRuleEntity_TaxBracketEntity")
    @ManyToOne
    @JoinColumn(name="tax_bracket_id", referencedColumnName="tax_bracket_id")
    protected TaxBracketEntity taxBracket;
}