package com.hackaton.app.model;

import com.hackaton.app.payload.requests.NewDeliveryRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryFactory {

    public static Delivery createDelivery(NewDeliveryRequest delivery){
        return Delivery.builder()
                .from(delivery.getFrom())
                .to(delivery.getTo())
                .description(delivery.getDescription())
                .tokens(delivery.getTokens())
                .build();
    }

}
