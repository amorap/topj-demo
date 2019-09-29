package com.example.demo;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.topj.account.Account;
import org.topj.core.Topj;
import org.topj.methods.response.AccountInfoResponse;
import org.topj.methods.response.RequestTokenResponse;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;
import org.topj.procotol.http.HttpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * @author Alberto Mora Plata (moral12)
 */
public class TopJTest {

    private Topj topj = null;
    private Account account = null;

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
    }

    @Test
    public void testAccountInfo() throws IOException {
        ResponseBase<RequestTokenResponse> requestTokenResponse = topj.requestToken(account);
        assert (account.getToken() != null);
        Objects.requireNonNull(account);
        System.out.println(JSON.toJSONString(requestTokenResponse));

        TestCommon.createAccount(topj, account);
        TestCommon.getAccountInfo(topj, account);

        Account contractAccount = topj.genAccount();
        System.out.println(contractAccount.getAddress());
        System.out.println(contractAccount.getPrivateKey());

        TestCommon.publishContract(topj, account, contractAccount);
        TestCommon.getAccountInfo(topj, account);
        // initiator, from, to, description, tokens

        topj.accountInfo(account);
        ResponseBase<XTransaction> callContractResult = topj.callContract(account, contractAccount.getAddress(), "create_delivery", Arrays.asList("yo", "Palmovka", "Paralelni Polis", "Droja", "20")); //initiator, from, to, description, tokens
        System.out.println(JSON.toJSONString(callContractResult));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        topj.accountInfo(account);
        ResponseBase<XTransaction> callContractResult2 = topj.callContract(account, contractAccount.getAddress(), "create_delivery", Arrays.asList("alex", "Pocer", "Pankrac", "Drone", "300")); //initiator, from, to, description, tokens
        System.out.println(JSON.toJSONString(callContractResult2));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "1", "id");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "1", "initiator");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "1", "from");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "1", "to");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "1", "description");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "1", "tokens");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "2", "id");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "2", "initiator");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "2", "from");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "2", "to");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "2", "description");
        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "2", "tokens");

    }

}