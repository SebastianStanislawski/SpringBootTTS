package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
class AppController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("")
    public String homePage(){
        return "index";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/processRegister")
    public String processRegister(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPasswd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPasswd);
        userRepository.save(user);

        return "index";
    }

}


@RestController
class AppRestController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

}