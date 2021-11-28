package com.example.demo.controllers;

import com.example.demo.domain.Message;
import com.example.demo.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class DemoController {
    @Autowired
    private MessageRepo messageRepo;

    @PostMapping(path = "/add")
    public String add(@RequestParam Integer sender, @RequestParam Integer host,
                      @RequestParam(required = false) boolean file,
                      @RequestParam String text, Map<String, Object> model){

        boolean b = file;

        Message message = new Message(sender, host,b,text);

        messageRepo.save(message);

        return "index";
    }
    @GetMapping(path = "/all")
    public Iterable<Message> getMessages(){
        return messageRepo.findAll();
    }

}