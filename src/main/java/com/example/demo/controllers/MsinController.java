package com.example.demo.controllers;


import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import com.example.demo.repos.MessageRepo;
import com.example.demo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
