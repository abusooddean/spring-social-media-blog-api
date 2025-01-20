package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    // helper method
    private boolean ifUserExists(String username){
        if(accountRepository.getAccountByUsername(username) != null){
            return true;
        }
        return false;
    }

    //## 1: Our API should be able to process new User registrations.
    public Account createAccount(Account account){
        if(ifUserExists(account.getUsername())){
            return null;
        }
        return accountRepository.save(account);
    }

    //## 2: Our API should be able to process User logins.
    public Account loginAccount(String username, String password){
        Account account = accountRepository.getAccountByUsername(username);
        if(account != null && account.getPassword().equals(password)){
            return account;
        }
        return null;
    }

}
