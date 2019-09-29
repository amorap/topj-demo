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
import org.topj.core.Topj;
import org.topj.methods.response.RequestTokenResponse;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Alberto Mora Plata (moral12)
 */
@Slf4j
public class TopJTest {

    private Topj topj = null;
    private Account account = null;
    private Account contractAccount = null;

    @Before
    @SneakyThrows
    public void setUp() {
        topj = TopJConnector.getInstance().getTopj();
        account = new Account();
//        account = topj.genAccount("a3aab9c186458ffd07ce1c01ba7edf9919724224c34c800514c60ac34084c63e");
        System.out.println(account.getAddress());
        System.out.println(account.getPrivateKey());
    }

    @Test
    @SneakyThrows
    public void createNewDelivery(){
        publishContract();

        NewDeliveryRequest request = NewDeliveryRequest.builder()
                .initiator("alberto")
                .from("A")
                .to("B")
                .description("Test")
                .tokens(5)
                .build();
        Delivery delivery = DeliveryFactory.createDelivery(request);

        DeliveryService deliveryService = new DeliveryService();
        deliveryService.create(topj, account, contractAccount, delivery);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }

        TopJConnector.getMapProperty(topj, account, contractAccount.getAddress(), "1", "initiator");
    }

    private void publishContract() throws IOException {
        ResponseBase<RequestTokenResponse> requestTokenResponse = topj.requestToken(account);
        assert (account.getToken() != null);
        Objects.requireNonNull(account);
        System.out.println(JSON.toJSONString(requestTokenResponse));

        TopJConnector.createAccount(topj, account);

        TopJConnector.getAccountInfo(topj, account);

        contractAccount = topj.genAccount();
        System.out.println(contractAccount.getAddress());
        System.out.println(contractAccount.getPrivateKey());

        TopJConnector.publishContract(topj, account, contractAccount);
    }

    @Test
    public void testAccountInfo() throws IOException {
        publishContract();

        TopJConnector.getMapProperty(topj, account, contractAccount.getAddress(), "hmap", "key");

        TopJConnector.getAccountInfo(topj, account);

        ResponseBase<XTransaction> callContractResult = topj.callContract(account, contractAccount.getAddress(), "opt_map", Arrays.asList("inkey", Long.valueOf(65)));
        System.out.println("***** call contract transaction >> ");
        System.out.println(JSON.toJSONString(callContractResult));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TopJConnector.getMapProperty(topj, account, contractAccount.getAddress(), "hmap", "inkey");
        TopJConnector.getStringProperty(topj, account, contractAccount.getAddress(), "temp_1");
        TopJConnector.getStringProperty(topj, account, contractAccount.getAddress(), "temp_2");

        TopJConnector.getListProperty(topj, account, contractAccount.getAddress(), "mlist");

        ResponseBase<XTransaction> stringProperty = topj.getStringProperty(account, contractAccount.getAddress(), "temp_1");
        ResponseBase<XTransaction> listProperty = topj.getListProperty(account, contractAccount.getAddress(), "mlist");
        List<String> getPropertyParams = new ArrayList<>();
        getPropertyParams.add("hmap");
        getPropertyParams.add("inkey");
        ResponseBase<XTransaction> mapProperty = topj.getMapProperty(account, contractAccount.getAddress(), getPropertyParams);
        System.out.println(JSON.toJSONString(stringProperty));
        System.out.println(JSON.toJSONString(listProperty));
        System.out.println(JSON.toJSONString(mapProperty));

//        ResponseBase<XTransaction> accountTransaction = topj.accountTransaction(account, account.getLastHash());
//        System.out.println("accountTransaction >> ");
//        System.out.println(JSON.toJSONString(accountTransaction));
    }

}