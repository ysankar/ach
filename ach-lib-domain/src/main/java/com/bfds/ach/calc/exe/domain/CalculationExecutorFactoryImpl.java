package com.bfds.ach.calc.exe.domain;


import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * An implementation of {@link CalculationExecutorFactory} that looks up {@link Executor}s registered in the
 * {@link #applicationContext}.
 *
 */
@Service
public class CalculationExecutorFactoryImpl implements CalculationExecutorFactory, ApplicationContextAware, InitializingBean {

    /**
     * All {@link Executor}s are beans in the application context.
     */
    private ApplicationContext applicationContext;


    /**
     *
     * @see {@link CalculationExecutorFactory#getExecutor(Class)}
     */
    @Override
    public Executor getExecutor(Class calculationClass) {
        Executor executor = getExecutor(calculationClass, buildCalculationExecutors());
        if(executor == null) {
            throw new IllegalStateException(String.format("cannot find bean of type %s that can execute bean of type %s", Executor.class, calculationClass));
        }
        return executor;
    }

    private Executor getExecutor(Class calculationClass, Map<Class, Executor> classExecutorMap) {
        Executor ret = classExecutorMap.get(calculationClass);
        if(ret == null) {
            for(Map.Entry<Class, Executor> entry : classExecutorMap.entrySet()) {
                if(entry.getKey().isAssignableFrom(calculationClass)) {
                    ret = entry.getValue();
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Build a Map<Class, Executor>. A spring bean than implements {@link Executor} interface
     * is an executor for its type parameter.
     * Eg:- Executor<SomeClass> is an executor of instances of SomeClass.
     */
    private Map<Class, Executor> buildCalculationExecutors() {
        Map<Class, Executor> calculationExecutors = Maps.newHashMap();
        Map<String, Executor> beansMap = applicationContext.getBeansOfType(Executor.class);
        for(Executor executor : beansMap.values()) {
            Class<?> calculationClass = getCalculationClass(executor);
            if(calculationExecutors.containsKey(calculationClass)) {
                throw new IllegalStateException(String.format("More than one bean of type %s that can execute bean of type %s", Executor.class, calculationClass));
            }
            calculationExecutors.put(calculationClass, executor);
        }
        return calculationExecutors;
    }

    /**
     * Extracts the type parameter of executor. The type parameter is the calculation class.
     *
     * @param executor - An implementation of {@link Executor}.
     *
     * @return  The calculation class.
     */
    private Class<?> getCalculationClass(Executor executor) {
        for(Type type : executor.getClass().getGenericInterfaces()) {
            Class calcClass = getCalculationClass(type);
            if(calcClass != null) {
                return calcClass;
            }
        }
        Class calcClass = getCalculationClass(executor.getClass().getGenericSuperclass());
        if(calcClass != null) {
            return calcClass;
        }
        //This should never happen
        throw new IllegalStateException(String.format("bean of type %s must have a type parameter", executor.getClass()));
    }

    private Class getCalculationClass(Type type) {
        if(type instanceof ParameterizedType) {
            if(((ParameterizedType)type).getRawType() == Executor.class
               || ((ParameterizedType)type).getRawType() == AbstractExecutor.class) {
                return  ((Class) ((ParameterizedType)type).getActualTypeArguments()[0]);
            }
        }
        //This should never happen
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(applicationContext, "applicationContext is null");
    }
}
