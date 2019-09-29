package com.hackaton.app.controllers;

import com.hackaton.app.InMemoryData;
import com.hackaton.app.connector.TopJConnector;
import com.hackaton.app.model.Delivery;
import com.hackaton.app.model.DeliveryFactory;
import com.hackaton.app.payload.requests.NewDeliveryRequest;
import com.hackaton.app.payload.requests.OpenDeliveriesRequest;
import com.hackaton.app.services.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.topj.account.Account;

import java.util.List;
import java.util.stream.Collectors;

@RestController("/deliveries")
public class DeliveriesController {

    private final DeliveryService deliveryService;
    private final InMemoryData data;

    private TopJConnector topJConnector = TopJConnector.getInstance();

    public DeliveriesController(DeliveryService deliveryService, InMemoryData data) {
        this.deliveryService = deliveryService;
        this.data = data;
    }

    @GetMapping("/open")
    public ResponseEntity<List<Delivery>> getOpenDeliveries(@RequestParam String privateKey){
        Account account = topJConnector.createAccount(privateKey);
        List<Delivery> deliveries = data.getContracts().values().stream()
                .map(delivery -> deliveryService.read(account, delivery))
                .collect(Collectors.toList());
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/a")
    public ResponseEntity<List<Delivery>> getOpenDeliveries(@RequestBody OpenDeliveriesRequest request){
        Account account = topJConnector.createAccount(request.getPrivateKey());
        List<Delivery> deliveries = data.getContracts().values().stream()
                .map(delivery -> deliveryService.read(account, delivery))
                .collect(Collectors.toList());
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Delivery>> getMyOpenDeliveries(){
        return null;
    }

    @GetMapping("{id}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable String id){
        return null;
    }

    @PostMapping
    public ResponseEntity createDelivery(@RequestBody NewDeliveryRequest newDelivery){
        Account account = topJConnector.createAccount(newDelivery.getPrivateKey());
        Delivery delivery = DeliveryFactory.createDelivery(newDelivery);
        deliveryService.create(account, delivery);
        return ResponseEntity.ok().build();
    }

}
