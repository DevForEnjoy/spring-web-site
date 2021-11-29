package com.example.demo.controllers;


import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import com.example.demo.repos.MessageRepo;
import com.example.demo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Controller
public class MsinController {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/main")
     public String mainCont(Model model) {

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages",messages);
        model.addAttribute("name",userRepository.findByUsername("Admin"));
        return "main";
    }
    @GetMapping("/main/add")
    public String mainAdd(Model model){ return "main-add";}

    @PostMapping( "/main/add")
    public String add(@RequestParam Long sender, @RequestParam Long host,
                      @RequestParam(required = false) boolean file,
                      @RequestParam String text, Map<String, Object> model){

        boolean b = file;

        Message message = new Message(sender, host,b,text);

        messageRepo.save(message);

        return "main-add";
    }

    @GetMapping("/main/{id}")
    public String messDetails(@PathVariable(value = "id") long id, Model model){
        Optional<Message> message = messageRepo.findById(id);

        ArrayList<Message> res = new ArrayList<>();
        message.ifPresent(res::add);
        model.addAttribute("message",res);

        return "main-details";
    }

@GetMapping(path = "/all")
public Iterable<Message> getMessages(){
    return messageRepo.findAll();
}
}
