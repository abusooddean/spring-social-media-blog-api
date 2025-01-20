package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;


@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    //## 3: Our API should be able to process the creation of new messages.
    public Message createMessage(Message message){
        //null/empty checks
        if(message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 255 || message.getPostedBy() == null){
            return null;
        }
        Account existingAccount = accountRepository.findByAccountId(message.getPostedBy());
        if(existingAccount == null){
            return null;
        }
        return messageRepository.save(message);
    }

    //## 4: Our API should be able to retrieve all messages.
    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    //## 5: Our API should be able to retrieve a message by its ID.
    public Message getMessageById(int messageId){
        return messageRepository.findById(messageId).orElse(null); //or optional
    }

    //## 6: Our API should be able to delete a message identified by a message ID.
    public int deleteMessageById(int messageId){
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    //## 7: Our API should be able to update a message text identified by a message ID.
    public int updateMessageById(int messageId, Message updatedMessage){
        if(updatedMessage.getMessageText().trim().isEmpty() || updatedMessage.getMessageText().length() > 255){
            return 0;
        }

        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(!optionalMessage.isPresent()){
            return 0;
        }

        Message message = optionalMessage.get();
        message.setMessageText(updatedMessage.getMessageText());
        messageRepository.save(message);
        return 1;
    }

    //## 8: Our API should be able to retrieve all messages written by a particular user.
    public List<Message> getAllMessagesByAccountId(Integer accountId){ //postedBy = accountId
        return messageRepository.findAllByPostedBy(accountId);
    }


}
