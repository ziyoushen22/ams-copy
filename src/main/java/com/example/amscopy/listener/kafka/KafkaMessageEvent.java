package com.example.amscopy.listener.kafka;

public enum KafkaMessageEvent {
    PRODUCT_EVENT("aInfo","产品信息","PMS"),
    PRODUCT_BIDTERM_EVENT("bInfo","产品可投期限信息","PMS"),
    TRADE_EVENT("cInfo","交易订单信息","PMS"),
    CASH_EVENT("dInfo","兑付订单信息","PMS"),
    ASSET_EVENT("eInfo","资产方信息","PMS");


    private String eventType;
    private String eventSource;
    private String eventName;

    KafkaMessageEvent(String eventType, String eventSource, String eventName) {
        this.eventType = eventType;
        this.eventSource = eventSource;
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventSource() {
        return eventSource;
    }

    public String getEventName() {
        return eventName;
    }
}
