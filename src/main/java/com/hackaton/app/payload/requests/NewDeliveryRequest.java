package com.hackaton.app.payload.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewDeliveryRequest {

    private String privateKey;
    private String from;
    private String to;
    private String description;
    private int tokens;

}
