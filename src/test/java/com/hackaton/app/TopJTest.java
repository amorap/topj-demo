package com.hackaton.app;

import com.hackaton.app.connector.TopJConnector;
import com.hackaton.app.model.Delivery;
import com.hackaton.app.model.DeliveryFactory;
import com.hackaton.app.payload.requests.NewDeliveryRequest;
import com.hackaton.app.services.DeliveryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.topj.account.Account;

@Slf4j
public class TopJTest {

    @Test
    @SneakyThrows
    public void createNewDelivery() {
        NewDeliveryRequest request = NewDeliveryRequest.builder()
                .privateKey(TopJConnector.getInstance().getTopj().genAccount().getPrivateKey())
                .from("A")
                .to("B")
                .description("Test")
                .tokens(200)
                .build();
        Delivery delivery = DeliveryFactory.createDelivery(request);

        InMemoryData data = new InMemoryData();
        DeliveryService deliveryService = new DeliveryService(data);

        TopJConnector topJConnector = TopJConnector.getInstance();
        Account account = topJConnector.createAccount(request.getPrivateKey());

        topJConnector.getAccountInfo(account);

        Account contractAccount = deliveryService.create(account, delivery);

        Delivery readDelivery = deliveryService.read(account, contractAccount);
        log.info(readDelivery.toString());

        Account deliverer = topJConnector.createAccount("6dc191d4d6debf2d0fa84e1778be47bfcf7c3b694aca8de00a488f1ed05a3c8d");
        deliveryService.assignDeliverer(deliverer, contractAccount);

        readDelivery = deliveryService.read(account, contractAccount);
        log.info(readDelivery.toString());

        topJConnector.getAccountInfo(deliverer);
        deliveryService.confirmDelivery(account, contractAccount);

        readDelivery = deliveryService.read(account, contractAccount);
        log.info(readDelivery.toString());

        topJConnector.getAccountInfo(account);
        topJConnector.getAccountInfo(deliverer);

    }

}