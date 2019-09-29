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

import java.io.IOException;
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

    public static final String DELIVERY = "delivery";
    private final TopJConnector topJConnector = TopJConnector.getInstance();

    @Getter
    @AllArgsConstructor
    private enum DeliveryActions {
        CREATE("create_delivery");

        private String actionName;
    }

    public Account create(Account account, Delivery delivery) {
        Account accountContract = null;
        try {
            accountContract = publishContract(account);

            List<String> attributes = new LinkedList<>();
            attributes.add(delivery.getFrom());
            attributes.add(delivery.getTo());
            attributes.add(delivery.getDescription());
            attributes.add(String.valueOf(delivery.getTokens()));

            topJConnector.getAccountInfo(account);

            ResponseBase<XTransaction> callContractResult = topJConnector.getTopj()
                    .callContract(account, accountContract.getAddress(), DeliveryActions.CREATE.getActionName(), attributes);
            log.debug(JSON.toJSONString(callContractResult));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }
        return accountContract;
    }

    private Account publishContract(Account account) throws IOException {
        Account contractAccount = topJConnector.getTopj().genAccount();
        log.info(contractAccount.getAddress());
        log.info(contractAccount.getPrivateKey());

        topJConnector.publishContract(account, contractAccount);
        return contractAccount;
    }

    public void update(Delivery delivery){

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

    public List<Delivery> readByStatus(DeliveryStatus status){
        List<Delivery> deliveries = readAll();
        return deliveries.stream().filter(delivery -> delivery.getStatus() == status).collect(Collectors.toList());
    }

    public List<Delivery> readAll(){
        return new ArrayList<>();
    }

}
