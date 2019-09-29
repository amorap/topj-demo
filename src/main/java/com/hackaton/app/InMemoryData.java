package com.hackaton.app;

import org.springframework.stereotype.Component;
import org.topj.account.Account;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryData {

    private Map<String, Account> contracts = new HashMap<>();

    public void addContract(Account account){
        contracts.put(account.getPrivateKey(), account);
    }

}
