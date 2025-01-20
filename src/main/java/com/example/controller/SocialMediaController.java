package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    //## 1: Our API should be able to process new User registrations.
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account){
        //make check here to simplify httpstatus
        if(account.getUsername().trim().isEmpty() || account.getPassword().length() < 4){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username empty or password too short");
        }

        Account newAccount = accountService.createAccount(account);
        if(newAccount != null){
            return ResponseEntity.status(HttpStatus.OK).body(newAccount);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username taken");
    }

    //## 2: Our API should be able to process User logins.
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        Account loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
        if(loggedInAccount == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(loggedInAccount); 
    }

    //## 3: Our API should be able to process the creation of new messages.
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(createdMessage); 
    }

    //## 4: Our API should be able to retrieve all messages.
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    //## 5: Our API should be able to retrieve a message by its ID.
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    //## 6: Our API should be able to delete a message identified by a message ID.
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId){
        int updatedRows = messageService.deleteMessageById(messageId);
        if(updatedRows == 0){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedRows);
    }

    //## 7: Our API should be able to update a message text identified by a message ID.
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessageById(@PathVariable Integer messageId, @RequestBody Message updatedMessage){
        int updatedRows = messageService.updateMessageById(messageId, updatedMessage);
        if(updatedRows == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedRows);
    }

    //## 8: Our API should be able to retrieve all messages written by a particular user.
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable Integer accountId){
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

}
