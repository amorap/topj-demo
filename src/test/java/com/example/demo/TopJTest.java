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

        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "hmap", "key");

        TestCommon.getAccountInfo(topj, account);

        ResponseBase<XTransaction> callContractResult = topj.callContract(account, contractAccount.getAddress(), "opt_map", Arrays.asList("inkey", Long.valueOf(65)));
        System.out.println("***** call contract transaction >> ");
        System.out.println(JSON.toJSONString(callContractResult));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TestCommon.getMapProperty(topj, account, contractAccount.getAddress(), "hmap", "inkey");
        TestCommon.getStringProperty(topj, account, contractAccount.getAddress(), "temp_1");
        TestCommon.getStringProperty(topj, account, contractAccount.getAddress(), "temp_2");

        TestCommon.getListProperty(topj, account, contractAccount.getAddress(), "mlist");

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