package io.pillopl.eventsourcing.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


public interface DomainEvent {

    String getType();
}
