package com.example.demo.controllers;

import com.example.demo.domain.*;
import com.example.demo.repos.AD_Repo;
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
public class homeController {


    @Autowired
    private AD_Repo ad_repo;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("/home")
    public String mainCont(Model model, Authentication authentication) {

        if(authentication!=null) {
        String name = authentication.getName();
        User user = userRepository.findByUsername(name);

        model.addAttribute("role",user.getRoles().toString());
        }
        Iterable<AD> messages = ad_repo.findAll();

        ArrayList<AD> rev = new ArrayList<>();

        for (AD message : messages) {
            rev.add(0,new AD_show(findbyID(message.getSender()), message));
        }


        model.addAttribute("messages",rev);

        return "home";
    }
    public String findbyID(long id){

        Iterable<User> users = userRepository.findAll();

        for (User u : users) {
            if(u.getId().equals(id)){
                return u.getUsername();
            }
        }
        return "";
    }

    @GetMapping("/main/addAD")
    public String mainAdd(Model model)
    {
        ArrayList<String> s = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();

        for (User u : users) {
            s.add(u.getUsername());
        }

        model.addAttribute("users",s);
        return "home-addAD";
    }

    @PostMapping( "/main/addAD")
    public String add(@RequestParam String text,
                       @RequestParam("file") MultipartFile file,
                       Map<String, Object> model,
                       Authentication authentication) throws IOException {

        String name = authentication.getName();
        User user = userRepository.findByUsername(name);

        AD ad = new AD(user.getId(),false,text,new Date());

        if(file != null && !file.getOriginalFilename().isEmpty()){

            File uploadDir =  new File(uploadPath);

            ad.setDoc(isPic(file.getOriginalFilename()));

            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "."+file.getOriginalFilename();

            file.transferTo(new File(uploadPath +"/"+ resultFilename));

            ad.setFilename(resultFilename);
        }

        ad_repo.save(ad);

        return "home-addAD";
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

    @GetMapping("/home/{id}/edit")
    public String messDetails(@PathVariable(value = "id") long id, Model model){
        Optional<AD> message = ad_repo.findById(id);

        ArrayList<AD> res = new ArrayList<>();
        message.ifPresent(res::add);
        model.addAttribute("ad",res);

        return "ad-edit";
    }
    @PostMapping( "/home/{id}/edit")
    public String addRed(@PathVariable(value = "id") long id,
            @RequestParam String text,
                      @RequestParam("file") MultipartFile file,
                      Map<String, Object> model,
                      Authentication authentication) throws IOException {

            AD ad = ad_repo.findById(id).orElse(new AD());

        String name = authentication.getName();

        User user = userRepository.findByUsername(name);

        ad.setSender(user.getId());
        ad.setText(text);

        if(file != null && !file.getOriginalFilename().isEmpty()){

            File uploadDir =  new File(uploadPath);

            ad.setDoc(isPic(file.getOriginalFilename()));

            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "."+file.getOriginalFilename();

            file.transferTo(new File(uploadPath +"/"+ resultFilename));

            ad.setFilename(resultFilename);
        }

        ad_repo.save(ad);

        return "home-addAD";
    }

}
