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
import org.topj.core.Topj;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;

import javax.swing.*;
import java.util.*;
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

    public void create(Topj topj, Account account, Account accountContract, Delivery delivery) {
        List<String> attributes = new LinkedList<>();
        attributes.add(delivery.getInitiator());
        attributes.add(delivery.getFrom());
        attributes.add(delivery.getTo());
        attributes.add(delivery.getDescription());
        attributes.add(String.valueOf(delivery.getTokens()));

        TopJConnector.getAccountInfo(topj, account);

        ResponseBase<XTransaction> callContractResult = topj
                .callContract(account, accountContract.getAddress(), DeliveryActions.CREATE.getActionName(), attributes);
//        log.info(JSON.toJSONString(callContractResult));
    }

    public void update(Delivery delivery){

    }

    public Delivery read(String id){
        return null;
    }

    public List<Delivery> readByStatus(DeliveryStatus status){
        List<Delivery> deliveries = readAll();
        return deliveries.stream().filter(delivery -> delivery.getStatus() == status).collect(Collectors.toList());
    }

    public List<Delivery> readAll(){
        return new ArrayList<>();
    }

}
