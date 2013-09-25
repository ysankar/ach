package com.bfds.ach.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 *  A  DataSourceInitializer that executes only after JPA is initialized.
 */
public class DataSourceInitializer extends org.springframework.jdbc.datasource.init.DataSourceInitializer {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void afterPropertiesSet() {
//        List x = entityManagerFactory.createEntityManager().createQuery("from com.bfds.ach.calc.harm.domain.ClaimFactorType").getResultList();
  //      x.clear();
        super.afterPropertiesSet();

    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

    }
}
