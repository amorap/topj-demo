package com.hackaton.app.connector;

import com.alibaba.fastjson.JSON;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.topj.account.Account;
import org.topj.core.Topj;
import org.topj.methods.response.AccountInfoResponse;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;
import org.topj.procotol.http.HttpService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TopJConnector {

    private static TopJConnector instance;

    @Getter
    private Topj topj1;

    private TopJConnector(Topj topj){
        this.topj1 = topj;
    }

    public static TopJConnector getInstance() {
        if (instance == null){
            try {
                createInstance();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return instance;
    }

    public static TopJConnector createInstance() throws IOException {
//        String url = Topj.getDefaultServerUrl("http://hacker.topnetwork.org");
//        HttpService httpService = new HttpService(url);
//        Topj topj = Topj.build(httpService);
        instance = new TopJConnector(null);
        return instance;
    }

    public static void publishContract(Topj topj, Account account, Account contractAccount) throws IOException {
        String codeStr = getContractContent();

        ResponseBase<XTransaction> transactionResponseBase = topj.publishContract(account, contractAccount, codeStr, 200);

        log.info("***** publish contract transaction >> ");
        log.info(JSON.toJSONString(transactionResponseBase));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }
    }

    private static String getContractContent() throws IOException {
        InputStream resourceAsStream = TopJConnector.class.getClassLoader().getResourceAsStream("contracts/Test.lua");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }

    public static void createAccount(Topj topj, Account account){
        ResponseBase<XTransaction> createAccountXt = topj.createAccount(account);
        System.out.print("createAccount transaction >> ");
        log.info(JSON.toJSONString(createAccountXt));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }
    }

    public static void getAccountInfo(Topj topj, Account account){
        ResponseBase<AccountInfoResponse> accountInfoResponse2 = topj.accountInfo(account);
        System.out.print("accountInfo >>>>> ");
        log.info(JSON.toJSONString(accountInfoResponse2));
    }

    public static void getMapProperty(Topj topj, Account account, String contractAddress, String key1, String key2){
        List<String> getPropertyParams = new ArrayList<>();
        getPropertyParams.add(key1);
        getPropertyParams.add(key2);
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "map", getPropertyParams);
        System.out.print("get property >>>>> ");
        log.info(JSON.toJSONString(voteXt));
    }

    public static void getStringProperty(Topj topj, Account account, String contractAddress, String key){
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "string", key);
        System.out.print("get property >>>>> ");
        log.info(JSON.toJSONString(voteXt));
    }

    public static void getListProperty(Topj topj, Account account, String contractAddress, String key){
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "list", key);
        System.out.print("get property >>>>> ");
        log.info(JSON.toJSONString(voteXt));
    }

}
