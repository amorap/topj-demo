package com.hackaton.app.payload.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewDeliveryRequest {

    private String initiator;
    private String from;
    private String to;
    private String description;
    private double tokens;

}