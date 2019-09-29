package com.hackaton.app.services;

import com.alibaba.fastjson.JSON;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alberto Mora Plata (moral12)
 */
@Service
@Slf4j
public class DeliveryService {

    private final TopJConnector topJConnector = TopJConnector.getInstance();

    @Getter
    @AllArgsConstructor
    private enum DeliveryActions {
        CREATE("create_delivery");

        private String actionName;
    }

    public void create(Account account, Account accountContract, Delivery delivery) {
        List<String> attributes = new LinkedList<>();
        attributes.add(delivery.getInitiator());
        attributes.add(delivery.getFrom());
        attributes.add(delivery.getTo());
        attributes.add(delivery.getDescription());
        attributes.add(String.valueOf(delivery.getTokens()));

        topJConnector.getAccountInfo(account);

        ResponseBase<XTransaction> callContractResult = topJConnector.getTopj()
                .callContract(account, accountContract.getAddress(), DeliveryActions.CREATE.getActionName(), attributes);
        log.debug(JSON.toJSONString(callContractResult));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }
    }

    public void update(Delivery delivery){

    }

    public Delivery read(Account account, Account accountContract, String id){
        String initiator = topJConnector.getMapProperty(account, accountContract.getAddress(), id, "initiator");
        String from = topJConnector.getMapProperty(account, accountContract.getAddress(), id, "from");
        String to = topJConnector.getMapProperty(account, accountContract.getAddress(), id, "to");
        String description = topJConnector.getMapProperty(account, accountContract.getAddress(), id, "description");
        String tokens = topJConnector.getMapProperty(account, accountContract.getAddress(), id, "tokens");
        return Delivery.builder()
                .id(id)
                .initiator(initiator)
                .from(from)
                .to(to)
                .description(description)
                .tokens(Double.parseDouble(tokens))
                .build();
    }

    public List<Delivery> readByStatus(DeliveryStatus status){
        List<Delivery> deliveries = readAll();
        return deliveries.stream().filter(delivery -> delivery.getStatus() == status).collect(Collectors.toList());
    }

    public List<Delivery> readAll(){
        return new ArrayList<>();
    }

}
