package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
class AppController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("")
    public String homePage(){
        return "login";
    }

    @GetMapping("/login")
    String login() {
        return "login";
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

        return "login";
    }

    @GetMapping("/tts")
    public String listUsers(Model model) {
        //List<User> listUsers = userRepository.findAll();
        //model.addAttribute("listUsers", listUsers);

        List<String> voicesAvailable = new ArrayList<String>();
        File[] files = new File("src/main/resources/static/onnx").listFiles();
        assert files != null;
        for(File file : files){
            if (file.isFile()){
                voicesAvailable.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
            }
        }
        model.addAttribute("onnx", voicesAvailable);
        //System.out.println(voicesAvailable);
        return "tts";
    }

}
