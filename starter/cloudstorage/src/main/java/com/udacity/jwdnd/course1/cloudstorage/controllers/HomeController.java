package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;


@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private FileService fileService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private EncryptionService encryptionService;

    public HomeController(UserService userService, CredentialService credentialService,
                          FileService fileService, NoteService noteService,
                          EncryptionService encryptionService) {

        this.userService = userService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.encryptionService = encryptionService;
    }


    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        //System.out.println("Homepage requested");
        int userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("notes", noteService.getMyNotes(userId));
        model.addAttribute("credentials", credentialService.getMyCredentials(userId));
        model.addAttribute("files", fileService.getFileList(userId));
        model.addAttribute("encryptionService", encryptionService);

        return ("home");
    }



}


