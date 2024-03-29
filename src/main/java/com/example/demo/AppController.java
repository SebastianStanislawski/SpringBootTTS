package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String test(Model model, HttpServletRequest request) throws InterruptedException {

        String onnx = request.getParameter("selectedOnnx");
        String text = request.getParameter("typedInText");
       try {

           File modelFile = new File("src/main/resources/static/onnx/myTrainedVoice.onnx");
           File output = new File("src/main/resources/static/output");
           File piperFile = new File("src/main/resources/static/piper/piper.exe");
           //TODO change full path to files inside the project
           String piperPath = piperFile.getPath();
           String modelPath = modelFile.getPath();
           String outputPath = output.getPath();
           String command = "echo '" + text + "' | " + piperPath + " -m " + modelPath + " -f " + outputPath + "\\output.wav";

           ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/k", command);
           Process process = processBuilder.start();


//           FileSystem fileSystem = FileSystems.getDefault();
//           WatchService watchService = fileSystem.newWatchService();
//           Path audioPath = Paths.get("D:/SpringBoot/demo/src/main/resources/static/output");
//           audioPath.register(watchService, new WatchEvent.Kind[] {ENTRY_MODIFY, ENTRY_CREATE, ENTRY_DELETE}, FILE_TREE);
//
//           while (true) {
//               WatchKey key = watchService.take();
//               for(WatchEvent<?> e : key.pollEvents()){
//                   Object c = e.context();
//                   System.out.printf("%s %d %s\n", e.kind(), e.count(), c);
//                   if(key.pollEvents() == ENTRY_CREATE){
//                       break;
//                   }
//               }
//               key.reset();
//               break;
//           }




        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        listUsers(model);
        return "tts";
    }
}
