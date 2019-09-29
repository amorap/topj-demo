package com.hackaton.app.controllers;

import com.hackaton.app.connector.TopJConnector;
import com.hackaton.app.model.Delivery;
import com.hackaton.app.model.DeliveryFactory;
import com.hackaton.app.payload.requests.NewDeliveryRequest;
import com.hackaton.app.payload.responses.DeliveryResponse;
import com.hackaton.app.services.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.topj.account.Account;

import java.util.List;

@RestController("/deliveries")
public class DeliveriesController {

    private final DeliveryService deliveryService;

    private TopJConnector topJConnector = TopJConnector.getInstance();

    public DeliveriesController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/open")
    public ResponseEntity<List<DeliveryResponse>> getPendingDeliveries(){
        return null;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DeliveryResponse>> getMyOpenDeliveries(){
        return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<DeliveryResponse> getDelivery(@PathVariable String id){
        return null;
    }

    @PostMapping
    public ResponseEntity createDelivery(NewDeliveryRequest newDelivery){
        Account account = topJConnector.createAccount(newDelivery.getPrivateKey());
        Delivery delivery = DeliveryFactory.createDelivery(newDelivery);
        deliveryService.create(account, delivery);
        return ResponseEntity.ok().build();
    }

}
