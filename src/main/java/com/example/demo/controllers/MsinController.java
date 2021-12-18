package com.example.demo.controllers;


import com.example.demo.domain.Message;
import com.example.demo.domain.User;
import com.example.demo.domain.messShow;
import com.example.demo.repos.MessageRepo;
import com.example.demo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class MsinController  {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public String findbyID(long id){

        Iterable<User> users = userRepository.findAll();

        for (User u : users) {
            if(u.getId().equals(id)){
                return u.getUsername();
            }
        }
        return "";
    }

    @GetMapping("/main")
     public String mainCont(Model model, Authentication authentication) {

        String name = authentication.getName();
        User user = userRepository.findByUsername(name);

        model.addAttribute("name",user.getUsername());
        model.addAttribute("role",user.getRoles().toString());
        model.addAttribute("id_u",user.getId());

        Iterable<Message> messages = messageRepo.findAll();

        ArrayList<messShow> rev = new ArrayList<>();

        for (Message message : messages) {
            rev.add(0,new messShow(findbyID(message.getSender()),findbyID(message.getHost()),message));
        }


        model.addAttribute("messages",rev);

        return "main";
    }
    @GetMapping("/main/add")
    public String mainAdd(Model model)
    {
        ArrayList<String> s = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();

        for (User u : users) {
            s.add(u.getUsername());
        }

        model.addAttribute("users",s);
        return "main-add";
    }

    @PostMapping( "/main/add")
    public String add( @RequestParam String host,
                      @RequestParam String text,
                      @RequestParam("file") MultipartFile file,
                      Map<String, Object> model,
                      Authentication authentication) throws IOException {

        String name = authentication.getName();
        User user = userRepository.findByUsername(name);
        User user2 = userRepository.findByUsername(host);

        Message message = new Message(user.getId(), user2.getId(),false,text,new Date());

        if(file != null && !file.getOriginalFilename().isEmpty()){

           File uploadDir =  new File(uploadPath);

           message.setDoc(isPic(file.getOriginalFilename()));

           if(!uploadDir.exists()){
               uploadDir.mkdir();
           }

               String uuidFile = UUID.randomUUID().toString();
               String resultFilename = uuidFile + "."+file.getOriginalFilename();

               file.transferTo(new File(uploadPath +"/"+ resultFilename));

               message.setFilename(resultFilename);
            }

        messageRepo.save(message);

        return "main-add";
    }

    public boolean isPic(String str){

        String img = "img";
        String png = "png";
        String jpg = "jpg";
        String[] s = str.split("\\.");

        if(img.equals(s[s.length-1]) ||
            jpg.equals(s[s.length-1]) ||
                png.equals(s[s.length-1])) {
            return false;
        }
        else return true;
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
