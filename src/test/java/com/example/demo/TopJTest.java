package com.example.demo;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.topj.account.Account;
import org.topj.core.Topj;
import org.topj.methods.response.RequestTokenResponse;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;
import org.topj.procotol.http.HttpService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Alberto Mora Plata (moral12)
 */
public class TopJTest {

    private Topj topj = null;
    private Account account = null;
    private Account contractAccount = null;
    private Account deliverer = null;

    @Before
    @SneakyThrows
    public void setUp() {
        String url = Topj.getDefaultServerUrl("http://hacker.topnetwork.org");
        HttpService httpService = new HttpService(url);
        topj = Topj.build(httpService);
        account = new Account();
//        account = topj.genAccount("a3aab9c186458ffd07ce1c01ba7edf9919724224c34c800514c60ac34084c63e");
        System.out.println(account.getAddress());
        System.out.println(account.getPrivateKey());

        ResponseBase<RequestTokenResponse> requestTokenResponse = topj.requestToken(account);
        assert (account.getToken() != null);
        Objects.requireNonNull(account);
        System.out.println(JSON.toJSONString(requestTokenResponse));

        TestCommon.createAccount(topj, account);
        TestCommon.getAccountInfo(topj, account);

        deliverer = new Account();
        System.out.println(deliverer.getAddress());
        System.out.println(deliverer.getPrivateKey());


        ResponseBase<RequestTokenResponse> requestTokenResponse2 = topj.requestToken(deliverer);
        assert (deliverer.getToken() != null);
        Objects.requireNonNull(deliverer);
        System.out.println(JSON.toJSONString(requestTokenResponse2));

        TestCommon.createAccount(topj, deliverer);
        TestCommon.getAccountInfo(topj, deliverer);
        
        contractAccount = topj.genAccount();
        System.out.println(contractAccount.getAddress());
        System.out.println(contractAccount.getPrivateKey());

        TestCommon.publishContract(topj, account, contractAccount);

    }

    @Test
    public void testAccountInfo() throws IOException {

        TestCommon.getAccountInfo(topj, account);

        topj.accountInfo(account);
        ResponseBase<XTransaction> callContractResult = topj.callContract(account, contractAccount.getAddress(), "create_delivery", Arrays.asList("Palmovka", "Paralelni Polis", "Droja", "200")); //from, to, description, tokens
        System.out.println(JSON.toJSONString(callContractResult));

        sleep();

        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "delivery", "from");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "delivery", "to");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "delivery", "description");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "delivery", "tokens");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "delivery", "status");

        topj.accountInfo(deliverer);
        ResponseBase<XTransaction> callContractResult2 = topj.callContract(deliverer, contractAccount.getAddress(), "assign_delivery", Arrays.asList()); //from, to, description, tokens
        System.out.println(JSON.toJSONString(callContractResult2));

        sleep();

        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "delivery", "status");

        topj.accountInfo(account);
        ResponseBase<XTransaction> callContractResult3 = topj.callContract(account, contractAccount.getAddress(), "confirm_delivery", Arrays.asList()); //from, to, description, tokens
        System.out.println(JSON.toJSONString(callContractResult3));

        sleep();

        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "delivery", "status");
        TestCommon.getAccountInfo(topj, deliverer);
    }

    public void sleep() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}