package com.hackaton.app.model;

import lombok.Builder;
import lombok.Data;

@Data
public class Delivery {

    private String id;
    private String from;
    private String to;
    private String description;
    private double tokens;
    private DeliveryStatus status;

    @Builder
    public Delivery(String id, String from, String to, String description, double tokens, DeliveryStatus status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.description = description;
        this.tokens = tokens;
        this.status = status;
    }
}
