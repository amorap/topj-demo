package com.example.demo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.topj.account.Account;
import org.topj.core.Topj;
import org.topj.methods.response.AccountInfoResponse;
import org.topj.methods.response.ResponseBase;
import org.topj.methods.response.XTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alberto Mora Plata (moral12)
 */
public class TestCommon {

    public static void publishContract(Topj topj, Account account, Account contractAccount) throws IOException {
        String codeStr = getContractContent();

        ResponseBase<XTransaction> transactionResponseBase = topj.publishContract(account, contractAccount, codeStr, 200);

        System.out.println("***** publish contract transaction >> ");
        System.out.println(JSON.toJSONString(transactionResponseBase));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException es) {
            es.printStackTrace();
        }
    }

    private static String getContractContent() throws IOException {
        InputStream resourceAsStream = TestCommon.class.getClassLoader().getResourceAsStream("contracts/backstreet.lua");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }

    public static void createAccount(Topj topj, Account account){
        ResponseBase<XTransaction> createAccountXt = topj.createAccount(account);
        System.out.print("createAccount transaction >> ");
        System.out.println(JSON.toJSONString(createAccountXt));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException eca) {
            eca.printStackTrace();
        }
    }

    public static void getAccountInfo(Topj topj, Account account){
        ResponseBase<AccountInfoResponse> accountInfoResponse2 = topj.accountInfo(account);
        System.out.print("accountInfo >>>>> ");
        System.out.println(JSON.toJSONString(accountInfoResponse2));
    }

    public static void getMapProperty(Topj topj, Account account, String contractAddress, String key1, String key2){
        List<String> getPropertyParams = new ArrayList<>();
        getPropertyParams.add(key1);
        getPropertyParams.add(key2);
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "map", getPropertyParams);
        System.out.print("get property >>>>> ");
        System.out.println(JSON.toJSONString(voteXt));
    }

    public static void getStringProperty(Topj topj, Account account, String contractAddress, String key){
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "string", key);
        System.out.print("get property >>>>> ");
        System.out.println(JSON.toJSONString(voteXt));
    }

    public static void getListProperty(Topj topj, Account account, String contractAddress, String key){
        ResponseBase<XTransaction> voteXt = topj.getProperty(account, contractAddress, "list", key);
        System.out.print("get property >>>>> ");
        System.out.println(JSON.toJSONString(voteXt));
    }

}
