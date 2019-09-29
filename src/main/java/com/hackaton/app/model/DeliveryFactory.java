package com.hackaton.app.model;

import com.hackaton.app.payload.requests.NewDeliveryRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryFactory {

    private static int counter;

    public static Delivery createDelivery(NewDeliveryRequest delivery){
        counter++;
        return Delivery.builder()
                .id(String.valueOf(counter))
                .initiator(delivery.getInitiator())
                .from(delivery.getFrom())
                .to(delivery.getTo())
                .description(delivery.getDescription())
                .tokens(delivery.getTokens())
                .build();
    }

}
