package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.coderscampus.assignment13.domain.User;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public Account saveAccount(Account account){

        return accountRepo.save(account);
    }

    public Account saveAccountsForUser(User user, String accountType){

        Account account = new Account();
        account.setAccountName(accountType);//Define Account Type
        account.getUsers().add(user);//Define User
        user.getAccounts().add(account);


        return accountRepo.save(account);

    }

    public Account findByAccountId(Long accountId){
        return accountRepo.findByAccountId(accountId);

    }



}
