package com.hackaton.app;

import com.alibaba.fastjson.JSON;
import com.hackaton.app.connector.TopJConnector;
import com.hackaton.app.model.Delivery;
import com.hackaton.app.model.DeliveryFactory;
import com.hackaton.app.payload.requests.NewDeliveryRequest;
import com.hackaton.app.services.DeliveryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.topj.account.Account;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class TopJTest {

    private TopJConnector topJConnector;
    private Account account = null;
    private Account contractAccount = null;

    @Before
    @SneakyThrows
    public void setUp() {
        topJConnector = TopJConnector.getInstance();
        account = TopJConnector.getInstance().getAccount();
        contractAccount = TopJConnector.getInstance().getContractAccount();
//        account = topJConnector.getTopj().genAccount("a3aab9c186458ffd07ce1c01ba7edf9919724224c34c800514c60ac34084c63e");
        System.out.println(account.getAddress());
        System.out.println(account.getPrivateKey());
    }

    @Test
    @SneakyThrows
    public void createNewDelivery() {
        NewDeliveryRequest request = NewDeliveryRequest.builder()
                .initiator("alberto")
                .from("A")
                .to("B")
                .description("Test")
                .tokens(5)
                .build();
        Delivery delivery = DeliveryFactory.createDelivery(request);

        DeliveryService deliveryService = new DeliveryService();
        deliveryService.create(account, contractAccount, delivery);
        
        Delivery readDelivery = deliveryService.read(account, contractAccount, "1");
        log.info(readDelivery.toString());
    }

    @Test
    public void testAccountInfo() throws IOException {
        topJConnector.getMapProperty(account, contractAccount.getAddress(), "hmap", "key");

        topJConnector.getAccountInfo(account);

        ResponseBase<XTransaction> callContractResult = topJConnector.getTopj().callContract(account, contractAccount.getAddress(), "opt_map", Arrays.asList("inkey", Long.valueOf(65)));
        System.out.println("***** call contract transaction >> ");
        System.out.println(JSON.toJSONString(callContractResult));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        topJConnector.getMapProperty(account, contractAccount.getAddress(), "hmap", "inkey");
        topJConnector.getStringProperty(account, contractAccount.getAddress(), "temp_1");
        topJConnector.getStringProperty(account, contractAccount.getAddress(), "temp_2");

        topJConnector.getListProperty(account, contractAccount.getAddress(), "mlist");

        ResponseBase<XTransaction> stringProperty = topJConnector.getTopj().getStringProperty(account, contractAccount.getAddress(), "temp_1");
        ResponseBase<XTransaction> listProperty = topJConnector.getTopj().getListProperty(account, contractAccount.getAddress(), "mlist");
        List<String> getPropertyParams = new ArrayList<>();
        getPropertyParams.add("hmap");
        getPropertyParams.add("inkey");
        ResponseBase<XTransaction> mapProperty = topJConnector.getTopj().getMapProperty(account, contractAccount.getAddress(), getPropertyParams);
        System.out.println(JSON.toJSONString(stringProperty));
        System.out.println(JSON.toJSONString(listProperty));
        System.out.println(JSON.toJSONString(mapProperty));

//        ResponseBase<XTransaction> accountTransaction = topJConnector.getTopj().accountTransaction(account, account.getLastHash());
//        System.out.println("accountTransaction >> ");
//        System.out.println(JSON.toJSONString(accountTransaction));
    }

}