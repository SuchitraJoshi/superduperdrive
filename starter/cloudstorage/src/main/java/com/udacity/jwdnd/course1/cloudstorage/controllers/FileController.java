package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;
    private String fileMessage="";

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }
    @PostMapping("/savefile")
    public String saveFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile multipartFile, Model model, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            if(multipartFile.getOriginalFilename().length()==0){
                throw new Exception("No file selected.");
            }
            if(multipartFile.getSize()>1500000){
                throw new Exception("The file exceeds the size.");
            }

            fileService.storeFile(multipartFile, userId);
            fileMessage= "File saved Successfully.";
        }catch (Exception ex){
            ex.printStackTrace();
            fileMessage= "Couldn't save the file. "+ ex.getMessage();
        }
        redirectAttributes.addAttribute("fileMessage", fileMessage);
        return "redirect:/home";

    }
    @GetMapping("/viewFile/{fileId}")
    public ResponseEntity viewFile (@PathVariable("fileId") Integer fileId, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        FileForm myFile = null;
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            myFile = fileService.getFile(userId, fileId);
            System.out.println("retrieve filesize :" + myFile.getFileData().toString());
        }catch (Exception ex){
            ex.printStackTrace();
            fileMessage= "Couldn't retrieve the file. "+ ex.getMessage();
        }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(myFile.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" +myFile.getFileName() + "\"")
                    .body(myFile.getFileData());

       // redirectAttributes.addAttribute("fileMessage", fileMessage);
//        model.addAttribute("files", fileService.getFileList(userId));
//        return "redirect:/home";
    }
    @GetMapping("/deletefile/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            fileService.deleteFile(userId, fileId);
            fileMessage = "File deleted Successfully.";
        }catch (Exception ex){
            ex.printStackTrace();
            fileMessage= "Couldn't delete the file. "+ ex.getMessage();
        }
        redirectAttributes.addAttribute("fileMessage", fileMessage);
        //model.addAttribute("files", fileService.getFileList(userId));
        return "redirect:/home";
    }
}
