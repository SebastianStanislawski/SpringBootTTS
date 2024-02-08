package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
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

    @PostMapping("/tts")
    public String test(Model model, HttpServletRequest request){
        String onnx = request.getParameter("selectedOnnx");
        String text = request.getParameter("typedInText");

        try {
            //TODO change full path to files inside the project
            String modelPath = "D:\\SpringBoot\\demo\\src\\main\\resources\\static\\onnx\\" + onnx + ".onnx";
            String outputPath = "D:\\SpringBoot\\demo\\src\\main\\resources\\static\\output";
            String piperPath = "D:\\SpringBoot\\demo\\src\\main\\resources\\static\\piper\\piper.exe";
            String command = "echo '" + text + "' | " + piperPath + " -m " + modelPath + " -f " + outputPath + "\\output.wav";
            Runtime.getRuntime().exec("cmd.exe /K F: && " + command);
            System.out.println("Cudownie!");
            System.out.println(command);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        listUsers(model);
        System.out.println(onnx + "\n" + text + "\n\n");
        return "/tts";
    }
}
