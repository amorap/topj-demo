package com.hackaton.app.controllers;

import com.hackaton.app.InMemoryData;
import com.hackaton.app.connector.TopJConnector;
import com.hackaton.app.model.Delivery;
import com.hackaton.app.model.DeliveryFactory;
import com.hackaton.app.model.DeliveryStatus;
import com.hackaton.app.payload.requests.NewDeliveryRequest;
import com.hackaton.app.services.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.topj.account.Account;

import java.util.List;

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

        List<Delivery> openDeliveries = deliveryService.readByStatus(account, DeliveryStatus.OPEN);
        return ResponseEntity.ok(openDeliveries);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity assignDeliverer(@RequestParam String privateKey, @PathVariable String id){
        Account deliverer = topJConnector.createAccount(privateKey);
        Account contractAccount = data.getContracts().get(id);
        deliveryService.assignDeliverer(deliverer, contractAccount);
        return ResponseEntity.ok("Deliverer assigned");
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
