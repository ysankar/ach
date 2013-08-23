package com.ach.test;


import com.google.common.base.Preconditions;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class AchTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        Object bean = getPreparedTestInstance(testContext);
        Method m = ReflectionUtils.findMethod(bean.getClass(), "beforeAllTests");
        ReflectionUtils.invokeMethod(m, bean);
    }


    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        Object bean = getPreparedTestInstance(testContext);
        Method m = ReflectionUtils.findMethod(bean.getClass(), "afterAllTests");
        ReflectionUtils.invokeMethod(m, bean);
    }

    protected Object getPreparedTestInstance(TestContext testContext)throws Exception {
        Object bean = testContext.getTestClass().newInstance();
        injectDependencies(bean, testContext.getApplicationContext());
        return bean;
    }

    protected void injectDependencies(final Object bean, ApplicationContext applicationContext) throws Exception {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.initializeBean(bean, bean.getClass().getName());
    }
}
