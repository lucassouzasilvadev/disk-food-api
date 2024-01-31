//package com.diskfood.diskfoodapi.api.controller;
//
//import com.diskfood.diskfoodapi.domain.model.ChatMessage;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class ChatController {
//
//    @MessageMapping("/biscoito")
//    @SendTo("/topic/messages")
//    public ChatMessage send(ChatMessage message){
//        System.out.println(message.toString());
//        return message;
//    }
//
//}
