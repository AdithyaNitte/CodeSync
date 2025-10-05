package com.example.java_file_upload;



import com.example.java_file_upload.JavaFile;
import com.example.java_file_upload.Session;
import com.example.java_file_upload.JavaFileRepository;
import com.example.java_file_upload.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Controller
public class FileUploadController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private JavaFileRepository javaFileRepository;
    
 // Delete a single file
    @PostMapping("/deleteFile")
    public String deleteFile(@RequestParam Long fileId,
                             @RequestParam String sessionCode) {
        javaFileRepository.deleteById(fileId);
        return "redirect:/view/" + sessionCode; // redirect back to session view
    }

    // Delete entire session
    @PostMapping("/deleteSession")
    public String deleteSession(@RequestParam String sessionCode) {
        Session session = sessionRepository.findBySessionCode(sessionCode);
        if (session != null) {
            sessionRepository.delete(session); // Cascade deletes all files
        }
        return "redirect:/upload"; // go back to upload page
    }


    // Teacher upload page
    @GetMapping("/upload")
    public String uploadPage(Model model){
        String sessionCode = UUID.randomUUID().toString().substring(0,8);
        model.addAttribute("sessionCode", sessionCode);
        return "upload";
    }

    // Handle file upload
    @PostMapping("/uploadFiles")
    public String uploadFiles(@RequestParam("files") List<MultipartFile> files,
                              @RequestParam("sessionCode") String sessionCode,
                              Model model) throws IOException {

        Session session = sessionRepository.findBySessionCode(sessionCode);
        if(session == null){
            session = new Session();
            session.setSessionCode(sessionCode);
            sessionRepository.save(session);
        }

        for(MultipartFile file : files){
            String code = new String(file.getBytes(), StandardCharsets.UTF_8);
            JavaFile javaFile = new JavaFile();
            javaFile.setSession(session);
            javaFile.setFilename(file.getOriginalFilename());
            javaFile.setCode(code);
            javaFileRepository.save(javaFile);
        }

        model.addAttribute("sessionCode", sessionCode);
        model.addAttribute("files", javaFileRepository.findBySession(session));
        return "uploadedFiles";
    }

    // Teacher view uploaded files
    @GetMapping("/view/{sessionCode}")
    public String viewUploadedFiles(@PathVariable String sessionCode, Model model) {
        Session session = sessionRepository.findBySessionCode(sessionCode);
        if (session != null) {
            List<JavaFile> files = javaFileRepository.findBySession(session);
            model.addAttribute("files", files);
            model.addAttribute("sessionCode", sessionCode);
        } else {
            model.addAttribute("error", "Session not found or expired.");
        }
        return "uploadedFiles";
    }

    
    

    // Student view files
    @GetMapping("/student")
    public String studentPage(){ return "student"; }

    @PostMapping("/student/view")
    public String studentView(@RequestParam String sessionCode, Model model){
        Session session = sessionRepository.findBySessionCode(sessionCode);
        if(session != null){
            List<JavaFile> files = javaFileRepository.findBySession(session);
            model.addAttribute("files", files);
            model.addAttribute("sessionCode", sessionCode);
        }
        return "uploadedFiles";
    }
    
    @GetMapping("/allSessions")
    public String viewAllSessions(Model model) {
        List<Session> sessions = sessionRepository.findAll();
        model.addAttribute("sessions", sessions);
        return "allSessions"; // Thymeleaf page
    }

    // Delete entire session with all files
    @PostMapping("/deleteSessionAll")
    public String deleteSessionAll(@RequestParam String sessionCode) {
        Session session = sessionRepository.findBySessionCode(sessionCode);
        if(session != null){
            sessionRepository.delete(session); // cascade deletes all files
        }
        return "redirect:/allSessions";
    }
}
