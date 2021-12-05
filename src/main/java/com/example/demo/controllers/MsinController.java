package com.example.demo.controllers;


import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import com.example.demo.repos.MessageRepo;
import com.example.demo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MsinController {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/main")
     public String mainCont( Model model) {
        model.addAttribute("name",userRepository.findByUsername("Admin"));

        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages",messages);

        return "main";
    }
    @GetMapping("/main/add")
    public String mainAdd(Model model){ return "main-add";}

    @PostMapping( "/main/add")
    public String add(@RequestParam Long sender, @RequestParam Long host,
                      @RequestParam(required = false) boolean isfile,
                      @RequestParam String text,
                      @RequestParam("file") MultipartFile file,
                      Map<String, Object> model) throws IOException {



        boolean b = isfile;

        Message message = new Message(sender, host,b,text);

        if(file != null){

           File uploadDir =  new File(uploadPath);

           if(!uploadDir.exists()){
               uploadDir.mkdir();
           }
           if(file.getOriginalFilename().length()==0){

           }else{
               String uuidFile = UUID.randomUUID().toString();
               String resultFilename = uuidFile + "."+file.getOriginalFilename();

               file.transferTo(new File(uploadPath +"/"+ resultFilename));

               message.setFilename(resultFilename);
           }
        }

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
