package com.hackaton.app.services;

import com.alibaba.fastjson.JSON;
import com.hackaton.app.InMemoryData;
import com.hackaton.app.connector.TopJConnector;
import com.hackaton.app.model.Delivery;
import com.hackaton.app.model.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.topj.account.Account;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alberto Mora Plata (moral12)
 */
@Service
@Slf4j
public class DeliveryService {

    public static final String DELIVERY = "delivery";
    private final TopJConnector topJConnector = TopJConnector.getInstance();

    private final InMemoryData data;

    public DeliveryService(InMemoryData data) {
        this.data = data;
    }

    @Getter
    @AllArgsConstructor
    private enum DeliveryActions {
        CREATE("create_delivery"),
        ASSIGN_DELIVERER("assign_deliverer"),
        CONFIRM_DELIVERY("confirm_delivery");

        private String actionName;
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Account create(Account account, Delivery delivery) {
        try {
            Account accountContract = publishContract(account);

            List<String> attributes = new LinkedList<>();
            attributes.add(delivery.getFrom());
            attributes.add(delivery.getTo());
            attributes.add(delivery.getDescription());
            attributes.add(String.valueOf(delivery.getTokens()));

            topJConnector.getAccountInfo(account);

            ResponseBase<XTransaction> callContractResult = topJConnector.getTopj()
                    .callContract(account, accountContract.getAddress(), DeliveryActions.CREATE.getActionName(), attributes);
            log.debug(JSON.toJSONString(callContractResult));
            Thread.sleep(2000);

            data.addContract(accountContract);
            return accountContract;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    private Account publishContract(Account account) throws IOException {
        Account contractAccount = topJConnector.getTopj().genAccount();
        log.debug(contractAccount.getAddress());
        log.debug(contractAccount.getPrivateKey());

        topJConnector.publishContract(account, contractAccount);
        return contractAccount;
    }

    public void assignDeliverer(Account deliverer, Account contractAccount){
        ResponseBase<XTransaction> callContractResult = topJConnector.getTopj()
                .callContract(deliverer, contractAccount.getAddress(), DeliveryActions.ASSIGN_DELIVERER.getActionName(), Collections.emptyList());
        log.debug(JSON.toJSONString(callContractResult));
        sleep();
    }

    public void confirmDelivery(Account account, Account contractAccount){
        ResponseBase<XTransaction> callContractResult = topJConnector.getTopj()
                .callContract(account, contractAccount.getAddress(), DeliveryActions.CONFIRM_DELIVERY.getActionName(), Collections.emptyList());
        log.debug(JSON.toJSONString(callContractResult));
    }

    public Delivery read(Account account, Account accountContract){
        String from = topJConnector.getMapProperty(account, accountContract.getAddress(), DELIVERY, "from");
        String to = topJConnector.getMapProperty(account, accountContract.getAddress(), DELIVERY, "to");
        String description = topJConnector.getMapProperty(account, accountContract.getAddress(), DELIVERY, "description");
        String tokens = topJConnector.getMapProperty(account, accountContract.getAddress(), DELIVERY, "tokens");
        String status = topJConnector.getMapProperty(account, accountContract.getAddress(), DELIVERY, "status");
        return Delivery.builder()
                .from(from)
                .to(to)
                .description(description)
                .tokens(Double.parseDouble(tokens))
                .status(DeliveryStatus.valueOf(status.toUpperCase()))
                .build();
    }

    public List<Delivery> readByStatus(Account account, DeliveryStatus status){
        List<Delivery> deliveries = readAll(account);
        return deliveries.stream().filter(delivery -> delivery.getStatus() == status).collect(Collectors.toList());
    }

    public List<Delivery> readAll(Account account){
        return data.getContracts().values().stream()
                .map(delivery -> read(account, delivery))
                .collect(Collectors.toList());
    }

}
