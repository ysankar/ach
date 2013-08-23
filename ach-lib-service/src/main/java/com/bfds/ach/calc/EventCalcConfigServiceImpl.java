package com.bfds.ach.calc;


import com.google.common.base.Preconditions;
import com.bfds.ach.calc.domain.EventCalcConfig;
import com.bfds.ach.calc.repositories.EventCalcConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCalcConfigServiceImpl implements EventCalcConfigService {

    @Autowired
    private EventCalcConfigRepository eventCalcConfigRepository;

    @Override
    public EventCalcConfig getEventCalcConfig() {
        List<EventCalcConfig> list = eventCalcConfigRepository.findAll();
        Preconditions.checkState(list.size() == 1, "Exactly one instance of %s must be present. Found %s", EventCalcConfig.class, list.size());
        return list.get(0);
    }
}
