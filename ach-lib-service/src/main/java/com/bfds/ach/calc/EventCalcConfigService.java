package com.bfds.ach.calc;


import com.bfds.ach.calc.domain.EventCalcConfig;

public interface EventCalcConfigService {
    /**
     * Only one {@link EventCalcConfig} must exist in repository.
     * @return The {@link EventCalcConfig}
     * @throws IllegalStateException if there is zero or more than one {@link EventCalcConfig} in repository.
     */
    EventCalcConfig getEventCalcConfig();
}
