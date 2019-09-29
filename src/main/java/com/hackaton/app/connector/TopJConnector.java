package com.hackaton.app.connector;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.topj.account.Account;
import org.topj.core.Topj;
import org.topj.methods.response.AccountInfoResponse;
import org.topj.methods.response.RequestTokenResponse;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;
import org.topj.procotol.http.HttpService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class TopJConnector {

    private static TopJConnector instance;

    private final Topj topj;

    private TopJConnector(Topj topj){
        this.topj = topj;
    }

    public static TopJConnector getInstance() {
        if (instance == null){
            try {
                createInstance();
            } catch (IOException e) {
                log.error("Unable to connect to Top Network.", e);
            }
        }
        return instance;
    }

    private static void createInstance() throws IOException {
        log.info("Connect to TOP Network...");
        String url = Topj.getDefaultServerUrl("http://hacker.topnetwork.org");
        HttpService httpService = new HttpService(url);
        Topj topj = Topj.build(httpService);
        instance = new TopJConnector(topj);
    }

    public String publishContract(Account account, Account contractAccount) throws IOException {
        String codeStr = getContractContent();

        ResponseBase<XTransaction> transactionResponseBase = topj.publishContract(account, contractAccount, codeStr, 200);
        log.debug(JSON.toJSONString(transactionResponseBase));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }

        return "test";
    }

    private String getContractContent() throws IOException {
        InputStream resourceAsStream = TopJConnector.class.getClassLoader().getResourceAsStream("contracts/backstreet.lua");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }

    public Account createAccount(String privateKey){
        Account account = topj.genAccount(privateKey);
        ResponseBase<RequestTokenResponse> requestTokenResponse = topj.requestToken(account);
        log.debug(requestTokenResponse.toString());

        ResponseBase<XTransaction> createAccountXt = topj.createAccount(account);
        System.out.print("createAccount transaction >> ");
        log.info(JSON.toJSONString(createAccountXt));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }

        getAccountInfo(account);
        return account;
    }

    public void getAccountInfo(Account account){
        ResponseBase<AccountInfoResponse> accountInfoResponse2 = topj.accountInfo(account);
        System.out.print("accountInfo >>>>> ");
        log.info(JSON.toJSONString(accountInfoResponse2));
    }

    public String getMapProperty(Account account, String contractAddress, String key1, String key2){
        List<String> getPropertyParams = new ArrayList<>();
        getPropertyParams.add(key1);
        getPropertyParams.add(key2);

        ResponseBase<XTransaction> voteXt = topj.getMapProperty(account, contractAddress, getPropertyParams);
        log.debug("get property >>>>> {}", JSON.toJSONString(voteXt));
        return getPropertyValue(voteXt);
    }

    private String getPropertyValue(ResponseBase<XTransaction> xTransaction){
        return new JSONObject(JSON.toJSONString(xTransaction))
                .getJSONObject("data")
                .getJSONArray("property_value")
                .getString(0);
    }

    public void getStringProperty(Account account, String contractAddress, String key){
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "string", key);
        System.out.print("get property >>>>> ");
        log.info(JSON.toJSONString(voteXt));
    }

    public void getListProperty(Account account, String contractAddress, String key){
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "list", key);
        System.out.print("get property >>>>> ");
        log.info(JSON.toJSONString(voteXt));
    }

}
