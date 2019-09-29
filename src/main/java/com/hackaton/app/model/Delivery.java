package com.hackaton.app.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
public class Delivery {

    @NonNull
    private String id;
    @NonNull
    private String initiator;
    private String deliverer;
    @NonNull
    private String from;
    @NonNull
    private String to;
    @NonNull
    private String description;
    @NonNull
    private double tokens;
    private DeliveryStatus status;

    @Builder
    public Delivery(String id, String initiator, String deliverer, String from, String to, String description, double tokens, DeliveryStatus status) {
        this.id = id;
        this.initiator = initiator;
        this.deliverer = deliverer;
        this.from = from;
        this.to = to;
        this.description = description;
        this.tokens = tokens;
        this.status = status;
    }
}
