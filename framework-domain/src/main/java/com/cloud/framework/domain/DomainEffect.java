package com.cloud.framework.domain;

import java.util.List;

public interface DomainEffect {
    List<DomainEvent> events();
}
