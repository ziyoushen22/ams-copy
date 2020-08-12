package com.example.amscopy.dto;

import lombok.Data;

@Data
public class EventDataEntity<T> {

    private String eventType;
    private String eventDataType;
    private T eventData;
}
